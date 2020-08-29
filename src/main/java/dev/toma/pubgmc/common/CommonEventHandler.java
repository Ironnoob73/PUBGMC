package dev.toma.pubgmc.common;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.capability.IPlayerCap;
import dev.toma.pubgmc.capability.InventoryFactory;
import dev.toma.pubgmc.capability.InventoryProvider;
import dev.toma.pubgmc.capability.PMCInventoryHandler;
import dev.toma.pubgmc.capability.player.PlayerCapFactory;
import dev.toma.pubgmc.capability.player.PlayerCapProvider;
import dev.toma.pubgmc.common.item.gun.GunItem;
import dev.toma.pubgmc.network.NetworkManager;
import dev.toma.pubgmc.network.packet.CPacketSendRecipes;
import dev.toma.pubgmc.network.packet.CPacketSyncInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber(modid = Pubgmc.MODID)
public class CommonEventHandler {

    @SubscribeEvent
    public static void livingChangeEquipment(LivingEquipmentChangeEvent event) {
        LivingEntity entity = event.getEntityLiving();
        ItemStack stack = event.getTo();
        EquipmentSlotType slotType = event.getSlot();
        if(entity instanceof PlayerEntity) {
            if (stack.getItem() instanceof GunItem) {
                if(slotType == EquipmentSlotType.MAINHAND) {
                    CompoundNBT old = event.getFrom().getTag();
                    CompoundNBT updated = stack.getTag();
                    if(old == null || updated == null) return;
                    if(updated.getInt("ammo") != old.getInt("ammo") - 1) {
                        CooldownTracker tracker = ((PlayerEntity) entity).getCooldownTracker();
                        tracker.setCooldown(stack.getItem(), 20);
                    }
                } else if(slotType == EquipmentSlotType.OFFHAND) {
                    ItemStack copyOf = stack.copy();
                    stack.setCount(0);
                    if(!entity.world.isRemote) {
                        ItemEntity itemEntity = new ItemEntity(entity.world, entity.posX, entity.posY, entity.posZ, copyOf);
                        itemEntity.setPickupDelay(0);
                        entity.world.addEntity(itemEntity);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void attachEntityCap(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof PlayerEntity) {
            event.addCapability(Pubgmc.makeResource("playercap"), new PlayerCapProvider((PlayerEntity) event.getObject()));
            event.addCapability(Pubgmc.makeResource("inv"), new InventoryProvider(new InventoryFactory((PlayerEntity) event.getObject())));
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        IPlayerCap cap = PlayerCapFactory.get(player);
        cap.onTick();
    }

    @SubscribeEvent
    public static void onPlayerLogIn(PlayerEvent.PlayerLoggedInEvent event) {
        IPlayerCap cap = PlayerCapFactory.get(event.getPlayer());
        cap.syncAllData();
        NetworkManager.sendToClient((ServerPlayerEntity) event.getPlayer(), new CPacketSendRecipes(Pubgmc.recipeManager.recipeMap));
        syncInventory(event.getPlayer(), Collections.singletonList(event.getPlayer()));
    }

    @SubscribeEvent
    public static void onLogOut(PlayerEvent.PlayerLoggedOutEvent event) {
        syncMap.remove(event.getPlayer().getUniqueID());
    }

    @SubscribeEvent
    public static void clonePlayer(PlayerEvent.Clone event) {
        try {
            InventoryFactory oldData = (InventoryFactory) InventoryFactory.getInventoryHandler(event.getOriginal());
            InventoryFactory createdData = (InventoryFactory) InventoryFactory.getInventoryHandler(event.getPlayer());
            createdData.deserializeNBT(oldData.serializeNBT());
            IPlayerCap oldCap = PlayerCapFactory.get(event.getOriginal());
            IPlayerCap newCap = PlayerCapFactory.get(event.getPlayer());
            newCap.deserializeNBT(oldCap.serializeNBT());
            newCap.syncNetworkData();
        } catch (Exception e) {
            Pubgmc.pubgmcLog.error("Exception cloning player data: {}", e.toString());
        }
    }

    //@SubscribeEvent
    public static void _onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(event.phase == TickEvent.Phase.END) {
            PlayerEntity player = event.player;
            PMCInventoryHandler handler = InventoryFactory.getInventoryHandler(player);
            for(int i = 0; i < handler.getSlots(); i++) {
                ItemStack stack = handler.getStackInSlot(i);

            }
        }
    }

    @SubscribeEvent
    public static void startTracking(PlayerEvent.StartTracking event) {
        Entity entity = event.getTarget();
        if(entity instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) entity;
            syncInventory(player, Collections.singletonList(player));
        }
    }

    @SubscribeEvent
    public static void onPlayerDeath(LivingDropsEvent event) {
        if(event.getEntity() instanceof PlayerEntity) {
            World world = event.getEntity().world;
            GameRules rules = world.getGameRules();
            if(!world.isRemote && !rules.getBoolean(GameRules.KEEP_INVENTORY)) {
                dropItems((PlayerEntity) event.getEntity(), event.getDrops());
            }
        }
    }

    @SubscribeEvent
    public static void pickupItem(EntityItemPickupEvent event) {
        ItemStack stack = event.getItem().getItem();
        PlayerInventory inventory = event.getPlayer().inventory;

    }

    private static final Map<UUID, ItemStack[]> syncMap = new HashMap<>();
    private static final boolean autoSync = false;

    private static void sync(PlayerEntity player, PMCInventoryHandler handler) {
        ItemStack[] stacks = syncMap.get(player.getUniqueID());
        if(stacks == null) {
            stacks = new ItemStack[handler.getSlots()];
            Arrays.fill(stacks, ItemStack.EMPTY);
            syncMap.put(player.getUniqueID(), stacks);
        }
        Set<PlayerEntity> players = null;
        for(int i = 0; i < handler.getSlots(); i++) {
            ItemStack stack = handler.getStackInSlot(i);
            if(autoSync && handler.hasChanged(i) && !ItemStack.areItemStacksEqual(stack, stacks[i])) {
                if(players == null) {
                    players = new HashSet<>(player.world.getPlayers());
                }
                syncSlot(player, i, stack, players);
                handler.setChanged(i, false);
                stacks[i] = stack == null ? ItemStack.EMPTY : stack.copy();
            }
        }
    }

    private static void syncInventory(PlayerEntity player, Collection<? extends PlayerEntity> receiveUpdate) {
        PMCInventoryHandler inventoryHandler = InventoryFactory.getInventoryHandler(player);
        for (int s = 0; s < inventoryHandler.getSlots(); s++) {
            syncSlot(player, s, inventoryHandler.getStackInSlot(s), receiveUpdate);
        }
    }

    private static void syncSlot(PlayerEntity player, int slot, ItemStack stack, Collection<? extends PlayerEntity> receiveUpdate) {
        CPacketSyncInventory cPacketSyncInventory = new CPacketSyncInventory(player.getEntityId(), slot, stack);
        for(PlayerEntity receiver : receiveUpdate) {
            NetworkManager.sendToClient((ServerPlayerEntity) receiver, cPacketSyncInventory);
        }
    }

    private static void dropItems(PlayerEntity player, Collection<ItemEntity> items) {
        PMCInventoryHandler handler = InventoryFactory.getInventoryHandler(player);
        for(int i = 0; i < handler.getSlots(); i++) {
            ItemStack stack = handler.getStackInSlot(i);
            if(!stack.isEmpty()) {
                World world = player.world;
                ItemEntity ie = new ItemEntity(world, player.posX, player.posY + player.getEyeHeight(), player.posZ, stack.copy());
                ie.setPickupDelay(40);
                float f1 = world.rand.nextFloat() * 0.5F;
                float f2 = world.rand.nextFloat() * (float) Math.PI * 2.0F;
                ie.setMotion(-MathHelper.sin(f2) * f1, 0.2, MathHelper.cos(f2) * f1);
                items.add(ie);
                handler.setStackInSlot(i, ItemStack.EMPTY);
            }
        }
    }
}
