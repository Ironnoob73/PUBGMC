package dev.toma.pubgmc.common;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.capability.IPlayerCap;
import dev.toma.pubgmc.capability.player.PlayerCapFactory;
import dev.toma.pubgmc.capability.player.PlayerCapProvider;
import dev.toma.pubgmc.common.container.PlayerExtendedContainer;
import dev.toma.pubgmc.common.inventory.PlayerExtendedInventory;
import dev.toma.pubgmc.common.item.gun.GunItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.CooldownTracker;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Pubgmc.MODID)
public class CommonEventHandler {

    @SubscribeEvent
    public static void joinWorldEvent(EntityJoinWorldEvent event) {
        if(event.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntity();
            PlayerExtendedInventory extendedInventory = new PlayerExtendedInventory(player);
            PlayerExtendedContainer container = new PlayerExtendedContainer(extendedInventory, !event.getWorld().isRemote, player);
            // TODO improve for saving data from added slots
            ListNBT nbt = player.inventory.write(new ListNBT());
            //player.inventory = extendedInventory;
            //player.inventory.read(nbt);
            //player.container = container;
        }
    }

    @SubscribeEvent
    public static void livingChangeEquipment(LivingEquipmentChangeEvent event) {
        LivingEntity entity = event.getEntityLiving();
        ItemStack stack = event.getTo();
        EquipmentSlotType slotType = event.getSlot();
        if(entity instanceof PlayerEntity) {
            if (stack.getItem() instanceof GunItem) {
                if(slotType == EquipmentSlotType.MAINHAND) {
                    CooldownTracker tracker = ((PlayerEntity) entity).getCooldownTracker();
                    tracker.setCooldown(stack.getItem(), 20);
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
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        IPlayerCap cap = PlayerCapFactory.get(player);
        cap.onTick();
    }
}
