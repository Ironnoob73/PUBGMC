package dev.toma.pubgmc.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.config.enums.EnumSpeedUnit;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

import java.nio.file.Path;

@Mod.EventBusSubscriber(modid = Pubgmc.MODID)
public class Config {

    public static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec COMMON;
    public static ForgeConfigSpec CLIENT;

    /*  =========================[ CLIENT ]========================= */
    // ANIMATION BUILDER
    public static ForgeConfigSpec.BooleanValue animationTool;
    public static ForgeConfigSpec.DoubleValue animationTranslate;
    public static ForgeConfigSpec.DoubleValue animationTranslateSneak;
    public static ForgeConfigSpec.DoubleValue animationRotate;
    public static ForgeConfigSpec.DoubleValue animationRotateSneak;
    // GENERAL
    public static ForgeConfigSpec.EnumValue<EnumSpeedUnit> speedUnit;
    public static ForgeConfigSpec.BooleanValue specialHUDRenderer;

    public static void load(ForgeConfigSpec spec, Path path) {
        CommentedFileConfig commentedFileConfig = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();
        commentedFileConfig.load();
        spec.setConfig(commentedFileConfig);
    }

    static {
        COMMON = COMMON_BUILDER.comment("General")
                .push("general")
                .pop()
                .build();

        CLIENT_BUILDER.comment("Animation builder settings").push("animation builder");
        animationTool = CLIENT_BUILDER.comment("Enables tool for animation creation").define("animationTool", false);
        animationTranslate = CLIENT_BUILDER.comment("Value for translation context").defineInRange("animationTranslate", 0.1, 0.0, 1.0);
        animationTranslateSneak = CLIENT_BUILDER.comment("Value for translation context", "When sneaking").defineInRange("animationTranslateSneak", 0.01, 0.0, 1.0);
        animationRotate = CLIENT_BUILDER.comment("Value for rotation context").defineInRange("animationRotate", 10.0, 1.0, 45.0);
        animationRotateSneak = CLIENT_BUILDER.comment("Value for rotation context", "When sneaking").defineInRange("animationRotateSneak", 1.0, 0.1, 10.0);
        CLIENT_BUILDER.pop();
        CLIENT_BUILDER.comment("General settings").push("general");
        speedUnit = CLIENT_BUILDER.comment("Displayed speed unit").defineEnum("speedUnit", EnumSpeedUnit.METRIC);
        specialHUDRenderer = CLIENT_BUILDER.comment("Better HUD").define("specialHUD", true);
        CLIENT = CLIENT_BUILDER.build();
    }
}
