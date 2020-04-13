package dev.toma.pubgmc;

import dev.toma.pubgmc.common.item.healing.HealingItem;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

public class Registry {

    @ObjectHolder(Pubgmc.MODID)
    public static class PMCItems {
        public static final HealingItem BANDAGE = null;
        public static final HealingItem FIRST_AID_KIT = null;
        public static final HealingItem MEDKIT = null;
        public static final HealingItem ENERGY_DRINK = null;
        public static final HealingItem PAINKILLERS = null;
        public static final HealingItem ADRENALINE_SYRINGE = null;
    }

    @ObjectHolder(Pubgmc.MODID)
    public static class PMCBlocks {

    }

    @Mod.EventBusSubscriber(modid = Pubgmc.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class CommonHandler {

        @SubscribeEvent
        public static void onItemRegister(RegistryEvent.Register<Item> event) {
            event.getRegistry().registerAll(
                    HealingItem.Builder.create()
                            .stackSize(5)
                            .canUse(player -> player.getHealth() < 15)
                            .useDuration(80)
                            .errorMsg(() -> "pubgmc.heal.bandage.fail")
                            .onFinish(player -> {
                                player.setHealth(Math.min(15, player.getHealth() + 3));
                                player.sendStatusMessage(new TranslationTextComponent("pubgmc.heal.bandage.success"), true);
                            })
                            .build("bandage"),
                    HealingItem.Builder.create()
                            .stackSize(1)
                            .canUse(player -> player.getHealth() < 15)
                            .useDuration(120)
                            .errorMsg(() -> "pubgmc.heal.first_aid.fail")
                            .onFinish(player -> {
                                player.setHealth(15);
                                player.sendStatusMessage(new TranslationTextComponent("pubgmc.heal.first_aid.success"), true);
                            })
                            .build("first_aid_kit"),
                    HealingItem.Builder.create()
                            .stackSize(1)
                            .canUse(player -> player.getHealth() < 20)
                            .useDuration(160)
                            .errorMsg(() -> "pubgmc.heal.medkit.fail")
                            .onFinish(player -> {
                                player.setHealth(20);
                                player.sendStatusMessage(new TranslationTextComponent("pubgmc.heal.medkit.success"), true);
                            })
                            .build("medkit")
            );
        }

        @SubscribeEvent
        public static void onBlockRegister(RegistryEvent.Register<Block> event) {

        }
    }
}
