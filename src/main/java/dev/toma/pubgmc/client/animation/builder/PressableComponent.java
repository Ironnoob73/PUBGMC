package dev.toma.pubgmc.client.animation.builder;

import java.util.function.Consumer;

public class PressableComponent extends UIComponent {

    public PressableComponent(int x, int y, int w, int h, String text, Consumer<UIComponent> pressable) {
        super(x, y, w, h, text, pressable);
    }

    @Override
    public void onClick(double mousex, double mousey) {
        this.onPress.accept(this);
    }
}
