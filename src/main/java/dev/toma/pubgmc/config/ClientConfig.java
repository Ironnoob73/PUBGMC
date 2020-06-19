package dev.toma.pubgmc.config;

import dev.toma.pubgmc.config.enums.EnumSpeedUnit;
import toma.config.object.builder.ConfigBuilder;

public class ClientConfig {

    public boolean specialHUDRender = true;
    public EnumSpeedUnit speedUnit = EnumSpeedUnit.METRIC;
    public AnimationBuilderConfig builderConfig = new AnimationBuilderConfig();

    public ConfigBuilder populate(ConfigBuilder builder) {
        return builder
                .push("Client Config")
                    .addBoolean(specialHUDRender)
                    .name("Advanced HUD")
                    .add(type -> specialHUDRender = type.value())
                    .addEnum(speedUnit)
                    .name("Speed Unit")
                    .add(type -> speedUnit = type.value())
                    .exec(builderConfig::build)
                .pop();
    }
}
