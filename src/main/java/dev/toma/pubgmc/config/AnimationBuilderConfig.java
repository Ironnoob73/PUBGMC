package dev.toma.pubgmc.config;

import toma.config.object.builder.ConfigBuilder;

public class AnimationBuilderConfig {

    public boolean enabled = false;
    public float translate = 0.1F;
    public float translateSmall = 0.01F;
    public float rotate = 10.0F;
    public float rotateSmall = 1.0F;

    public ConfigBuilder build(ConfigBuilder builder) {
        return builder.push("Animation Builder")
                .addBoolean(enabled).name("Enabled").add(t -> enabled = t.value())
                .addFloat(translate).name("Translate").add(t -> translate = t.value())
                .addFloat(translateSmall).name("Translate S").add(t -> translateSmall = t.value())
                .addFloat(rotate).name("Rotate").add(t -> rotate = t.value())
                .addFloat(rotateSmall).name("Rotate S").add(t -> rotateSmall = t.value())
                .pop();
    }
}
