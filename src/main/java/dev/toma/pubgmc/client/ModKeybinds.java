package dev.toma.pubgmc.client;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.common.item.utility.ThrowableItem;
import dev.toma.pubgmc.network.NetworkManager;
import dev.toma.pubgmc.network.packet.SPacketCookThrowable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

public class ModKeybinds {

    private static final List<Keybind> LIST = new ArrayList<>();
    private static final String CATEGORY = "pubgmc.key.category";

    public static void init() {
        register("reload", 82, ModKeybinds::R_keyPress);
    }

    public static void register(String key, int code, Runnable onPressed) {
        Keybind keybind = new Keybind(key, code, onPressed);
        LIST.add(keybind);
        ClientRegistry.registerKeyBinding(keybind);
    }

    private static void R_keyPress() {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        if(player.getHeldItemMainhand().getItem() instanceof ThrowableItem) {
            NetworkManager.sendToServer(new SPacketCookThrowable());
        }
    }

    private static class Keybind extends KeyBinding {

        private final Runnable onPress;

        private Keybind(String key, int code, Runnable pressed) {
            super(String.format("pubgmc.key.%s", key), code, CATEGORY);
            this.onPress = pressed;
        }
    }

    @Mod.EventBusSubscriber(modid = Pubgmc.MODID, value = Dist.CLIENT)
    public static class InputKeyListener {

        @SubscribeEvent
        public static void onKeyPressed(InputEvent.KeyInputEvent event) {
            LIST.stream().filter(Keybind::isPressed).forEach(key -> key.onPress.run());
        }
    }
}
