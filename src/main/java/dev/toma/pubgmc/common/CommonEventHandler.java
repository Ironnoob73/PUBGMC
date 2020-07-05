package dev.toma.pubgmc.common;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.capability.IPlayerCap;
import dev.toma.pubgmc.capability.player.PlayerCapFactory;
import dev.toma.pubgmc.capability.player.PlayerCapProvider;
import dev.toma.pubgmc.common.container.PlayerExtendedContainer;
import dev.toma.pubgmc.common.inventory.PlayerExtendedInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
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
            player.inventory = extendedInventory;
            player.inventory.read(nbt);
            player.container = container;
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
