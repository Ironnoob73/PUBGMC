package dev.toma.pubgmc.client;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.common.item.gun.GunItem;
import dev.toma.pubgmc.common.item.utility.ThrowableItem;
import dev.toma.pubgmc.network.NetworkManager;
import dev.toma.pubgmc.network.packet.SPacketCookThrowable;
import dev.toma.pubgmc.network.packet.SPacketSetReloading;
import dev.toma.pubgmc.util.UsefulFunctions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

public class ModKeybinds {

    private static final String CATEGORY = "pubgmc.key.category";
    public static KeyBinding RELOAD_COOK;

    public static void init() {
        RELOAD_COOK = register("reload", GLFW.GLFW_KEY_R);
    }

    public static KeyBinding register(String key, int code) {
        KeyBinding keybind = new KeyBinding("pubgmc.key." + key, code, CATEGORY);
        ClientRegistry.registerKeyBinding(keybind);
        return keybind;
    }

    @Mod.EventBusSubscriber(modid = Pubgmc.MODID, value = Dist.CLIENT)
    public static class InputKeyListener {

        @SubscribeEvent
        public static void onKeyPressed(InputEvent.KeyInputEvent event) {
            Minecraft mc = Minecraft.getInstance();
            PlayerEntity player = mc.player;
            if(RELOAD_COOK.isPressed()) {
                ItemStack stack = player.getHeldItemMainhand();
                if(stack.getItem() instanceof ThrowableItem) {
                    NetworkManager.sendToServer(new SPacketCookThrowable());
                } else if(stack.getItem() instanceof GunItem) {
                    GunItem gun = (GunItem) stack.getItem();
                    int ammo = gun.getAmmo(stack);
                    int limit = gun.getMaxAmmo(stack);
                    if(ammo < limit && (player.isCreative() || UsefulFunctions.totalItemCountInInventory(gun.getAmmoType().getAmmo(), player.inventory) > 0)) {
                        NetworkManager.sendToServer(new SPacketSetReloading(true, gun.getReloadTime(stack)));
                        // TODO reload animation
                        player.playSound(gun.getReloadSound(stack), 1.0F, 1.0F);
                    }
                }
            }
        }
    }
}