package dev.toma.pubgmc.common.entity;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IControllableEntity {

    void onInputUpdate(boolean forward, boolean backward, boolean right, boolean left, boolean rotorAccelerate, boolean rotorSlowdown);

    @OnlyIn(Dist.CLIENT)
    default void drawOnScreen(Minecraft mc, MainWindow window) {}
}
