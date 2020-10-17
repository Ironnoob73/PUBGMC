package dev.toma.pubgmc.client.screen.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.util.SoundEvents;

import java.util.function.Consumer;

public class PressableComponent extends Component {

    private final Consumer<PressableComponent> onPressed;
    private boolean enabled = true;

    public PressableComponent(int x, int y, int width, int height, Consumer<PressableComponent> onPressed) {
        super(x, y, width, height);
        this.onPressed = onPressed;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void handleClicked(double mouseX, double mouseY, int mouseButton) {
        if(mouseButton == 0 && isEnabled()) {
            playPressSound();
            onPressed.accept(this);
        }
    }

    protected void playPressSound() {
        Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }
}
