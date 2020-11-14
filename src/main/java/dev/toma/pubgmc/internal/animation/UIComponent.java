package dev.toma.pubgmc.internal.animation;

import dev.toma.pubgmc.util.RenderHelper;
import dev.toma.pubgmc.util.object.Optional;
import net.minecraft.client.gui.FontRenderer;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class UIComponent {

    protected final int x, y, w, h;
    protected final Optional<String> name;
    protected final Consumer<UIComponent> onPress;
    public boolean enabled;

    public UIComponent(int x, int y, int w, int h) {
        this(x, y, w, h, null);
    }

    public UIComponent(int x, int y, int w, int h, @Nullable String display) {
        this(x, y, w, h, display, b -> {});
    }

    public UIComponent(int x, int y, int w, int h, @Nullable String display, Consumer<UIComponent> onPress) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.name = Optional.of(display);
        this.onPress = onPress;
    }

    public UIComponent setState(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void draw(FontRenderer renderer) {
        RenderHelper.drawColoredShape(x, y, x + w, y + h, enabled ? 0.0F : 0.7F, enabled ? 0.7F : 0.0F, 0.0F, 1.0F);
        if(name.isPresent()) {
            String text = name.get();
            int tw = renderer.getStringWidth(text) / 2;
            renderer.drawStringWithShadow(text, x + w / 2f - tw, y + 6, 0xffffff);
        }
    }

    public boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
    }

    public void onClick(double mousex, double mousey) {
        enabled = !enabled;
        onPress.accept(this);
    }
}
