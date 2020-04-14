package dev.toma.pubgmc.config;

import toma.config.object.builder.ConfigBuilder;

public class ClientConfig {

    public boolean specialHUDRender = true;

    public ConfigBuilder populate(ConfigBuilder builder) {
        return builder
                .push()
                .name("Client Config")
                .init()
                    .addBoolean(specialHUDRender)
                    .name("Advanced HUD")
                    .add(type -> specialHUDRender = type.value())
                .pop();
    }
}
