package dev.toma.pubgmc.client.screen.menu;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;

public class ComponentScreen extends Screen {

    private final List<Component> componentList = new ArrayList<>();

    public ComponentScreen(ITextComponent title) {
        super(title);
    }

    public void addComponent(Component component) {
        componentList.add(component);
    }

    @Override
    protected final void init() {
        componentList.clear();
        this.initComponents();
    }

    @Override
    public final void tick() {
        tickComponents();
    }

    public void initComponents() {

    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        for(Component component : componentList) {
            component.draw(minecraft, mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        for (Component component : componentList) {
            if(component.isMouseOver(mouseX, mouseY)) {
                component.handleClicked(mouseX, mouseY, mouseButton);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        for (Component component : componentList) {
            if(component.isMouseOver(mouseX, mouseY) || component.allowUnhoveredScrolling()) {
                component.handleScrolled(mouseX, mouseY, amount);
            }
        }
        return false;
    }

    protected final void tickComponents() {
        for (Component component : componentList) {
            if(component instanceof Component.Tickable) {
                ((Component.Tickable) component).tickComponent();
            }
        }
    }
}
