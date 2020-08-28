package dev.toma.pubgmc.inv;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.inv.network.SPacketOpenPlayerInventory;
import dev.toma.pubgmc.network.NetworkManager;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Pubgmc.MODID, value = Dist.CLIENT)
public class ClientHandler {

    @SubscribeEvent
    public static void openGui(GuiOpenEvent event) {
        if(event.getGui() instanceof InventoryScreen) {
            NetworkManager.sendToServer(new SPacketOpenPlayerInventory());
            event.setCanceled(true);
        }
    }
}
