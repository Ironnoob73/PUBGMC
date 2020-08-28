package dev.toma.pubgmc.common;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.capability.IPlayerCap;
import dev.toma.pubgmc.capability.player.PlayerCapFactory;
import dev.toma.pubgmc.capability.player.PlayerCapProvider;
import dev.toma.pubgmc.common.item.gun.GunItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.CooldownTracker;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        IPlayerCap cap = PlayerCapFactory.get(player);
        cap.onTick();
    }
}
