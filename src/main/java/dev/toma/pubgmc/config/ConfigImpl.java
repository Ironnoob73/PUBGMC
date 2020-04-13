package dev.toma.pubgmc.config;

import toma.config.IConfig;
import toma.config.datatypes.ConfigObject;
import toma.config.object.builder.ConfigBuilder;

public class ConfigImpl implements IConfig {

    @Override
    public ConfigObject getConfig(ConfigBuilder builder) {
        return builder.build();
    }
}
