package dev.toma.pubgmc.client.screen.util;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.client.screen.ComponentScreen;
import dev.toma.pubgmc.client.screen.component.ButtonComponent;
import dev.toma.pubgmc.client.screen.component.Component;
import dev.toma.pubgmc.client.screen.component.PlainTextComponent;
import dev.toma.pubgmc.client.screen.component.PressableComponent;
import dev.toma.pubgmc.common.item.gun.GunItem;
import dev.toma.pubgmc.util.RenderHelper;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AttachmentSetupScreen extends ComponentScreen {

    ItemStack stack;
    Tab[] tabArr = new Tab[2];
    Tab tab;

    public AttachmentSetupScreen(ItemStack stack) {
        super(new StringTextComponent("Attachment setup"));
        this.stack = stack;
    }

    @Override
    public void initComponents() {
        stack = minecraft.player.getHeldItemMainhand();
        if(!(stack.getItem() instanceof GunItem)) {
            minecraft.displayGuiScreen(null);
            minecraft.player.sendStatusMessage(new StringTextComponent(TextFormatting.RED + "Gun not found"), true);
        }
        int tabs = tabArr.length;
        int tabWidth = width / tabs;
        tabArr[0] = new ViewTab(0, 0, tabWidth, 10, p -> setTab(tabArr[0]));
        tabArr[1] = new SelectAttachmentTab(tabWidth, 0, tabWidth, 10, p -> setTab(tabArr[1]), this);
        if(tab == null) {
            this.tab = tabArr[0];
        }
        for (Tab t : tabArr)
            addComponent(t);
        tab.init(stack);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        RenderHelper.drawColoredShape(0, 0, width, height, 0.0F, 0.0F, 0.0F, 0.75F);
        super.render(mouseX, mouseY, partialTicks);
        tab.draw(stack, mouseX, mouseY, partialTicks);
    }

    public void setTab(Tab tab) {
        this.tab = tab;
        this.init(minecraft, width, height);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if(tab.clicked(mouseX, mouseY, mouseButton)) {
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    static class Tab extends PressableComponent {

        private String text = "tab";

        public Tab(int x, int y, int w, int h, Consumer<PressableComponent> componentConsumer) {
            super(x, y, w, h, componentConsumer);
        }

        public void setText(String text) {
            this.text = text;
        }

        public void init(ItemStack stack) {}

        public void draw(ItemStack stack, int mouseX, int mouseY, float partialTicks) {
        }

        @Override
        public void draw(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            RenderHelper.drawColoredShape(x, y, x + width, y + height, 0.0F, 0.0F, 0.0F, 1.0F);
            mc.fontRenderer.drawString(text, x + (width - mc.fontRenderer.getStringWidth(text)) / 2.0f, y + 2, 0xffffff);
        }

        public boolean clicked(double mouseX, double mouseY, int buttonID) {
            return false;
        }
    }

    static class SelectAttachmentTab extends Tab {

        private final AttachmentSetupScreen screen;
        private final AttachmentSettings settings = AttachmentSettings.instance();
        private final List<Component> components = new ArrayList<>();

        public SelectAttachmentTab(int x, int y, int w, int h, Consumer<PressableComponent> consumer, AttachmentSetupScreen screen) {
            super(x, y, w, h, consumer);
            setText("Render");
            this.screen = screen;
        }

        @Override
        public boolean clicked(double mouseX, double mouseY, int buttonID) {
            for (Component component : components) {
                if(component.isMouseOver(mouseX, mouseY)) {
                    component.handleClicked(mouseX, mouseY, buttonID);
                    return true;
                }
            }
            return false;
        }

        @Override
        public void init(ItemStack stack) {
            components.clear();
            int j = 0;
            for (AttachmentSettings.ModelSettings settings : this.settings.settingsMap.values()) {
                components.add(new CheckBoxComponent(25, 25 + j * 20, 20, 20, settings));
                components.add(new OptionsComponent(200, 25 + j * 20, 20, 20, settings, screen));
                ++j;
            }
        }

        @Override
        public void draw(ItemStack stack, int mouseX, int mouseY, float partialTicks) {
            components.forEach(c -> c.draw(Minecraft.getInstance(), mouseX, mouseY, partialTicks));
        }

        static class OptionsComponent extends ButtonComponent {

            public OptionsComponent(int x, int y, int w, int h, AttachmentSettings.ModelSettings settings, AttachmentSetupScreen screen) {
                super(x, y, w, h, "...", c -> screen.setTab(new OptionsTab(settings)));
            }
        }

        static class OptionsTab extends Tab {

            final AttachmentSettings.ModelSettings settings;
            final List<Component> componentList = new ArrayList<>();

            public OptionsTab(AttachmentSettings.ModelSettings settings) {
                super(-1, -1, 0, 0, c -> {});
                this.settings = settings;
            }

            @Override
            public void init(ItemStack stack) {
                componentList.clear();
                componentList.add(new PlainTextComponent(25, 25, 50, 20, "Position", false));
                componentList.add(new PlainTextComponent(35, 40, 20, 20, "X", false));
                componentList.add(new PlainTextComponent(35, 55, 20, 20, "Y", false));
                componentList.add(new PlainTextComponent(35, 70, 20, 20, "Z", false));
                componentList.add(new PlainTextComponent(25, 100, 50, 20, "Scale", false));
                componentList.add(new PlainTextComponent(25, 130, 50, 20, "Rotation", false));
                componentList.add(new PlainTextComponent(35, 145, 20, 20, "X", false));
                componentList.add(new PlainTextComponent(35, 160, 20, 20, "Y", false));
                componentList.add(new PlainTextComponent(35, 175, 20, 20, "Z", false));
            }

            @Override
            public void draw(ItemStack stack, int mouseX, int mouseY, float partialTicks) {
                for (Component component : componentList) {
                    component.draw(Minecraft.getInstance(), mouseX, mouseY, partialTicks);
                }
            }

            @Override
            public void draw(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            }

            @Override
            public boolean clicked(double mouseX, double mouseY, int buttonID) {
                for (Component component : componentList) {
                    if(component.isMouseOver(mouseX, mouseY)) {
                        component.handleClicked(mouseX, mouseY, buttonID);
                        return true;
                    }
                }
                return false;
            }
        }

        static class CheckBoxComponent extends PressableComponent {

            final AttachmentSettings.ModelSettings settings;

            CheckBoxComponent(int x, int y, int w, int h, AttachmentSettings.ModelSettings settings) {
                super(x, y, w, h, c -> settings.setEnabled(!settings.isEnabled()));
                this.settings = settings;
            }

            @Override
            public void draw(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
                RenderHelper.drawColoredShape(x, y, x + 16, y + 16, 0.2F, 0.2F, 0.2F, 1.0F);
                if(settings.isEnabled()) {
                    RenderHelper.drawColoredShape(x + 2, y + 2, x + 14, y + 14, 1.0F, 1.0F, 1.0F, 1.0F);
                }
                mc.fontRenderer.drawString(settings.getName(), x + 20, y + 4, 0xffffff);
            }

            @Override
            public boolean isMouseOver(double mouseX, double mouseY) {
                return mouseX >= x && mouseX <= x + 16 && mouseY >= y && mouseY <= y + 16;
            }
        }
    }

    static class ViewTab extends Tab {

        List<Component> components = new ArrayList<>();

        public ViewTab(int x, int y, int w, int h, Consumer<PressableComponent> onClicked) {
            super(x, y, w, h, onClicked);
            setText("Overview");
        }

        @Override
        public void init(ItemStack stack) {
            components.clear();
            MainWindow window = Minecraft.getInstance().mainWindow;
            int x1 = 15;
            int y1 = 25;
            int halfWidth = window.getScaledWidth() / 2;
            int componentWidth = halfWidth - 30;
            int halfHeight = window.getScaledHeight() / 2;
            int componentHeight = halfHeight - 25;
            components.add(new DisplayComponent(x1, y1, componentWidth, componentHeight - 15, stack, DisplayType.FRONT));
            components.add(new DisplayComponent(halfWidth + 15, y1, componentWidth, componentHeight - 15, stack, DisplayType.SIDE));
            components.add(new DisplayComponent(x1, halfHeight + 15, componentWidth, componentHeight - 15, stack, DisplayType.TOP));
            components.add(new DisplayComponent(halfWidth + 15, halfHeight + 15, componentWidth, componentHeight - 15, stack, DisplayType.DIAGONAL));
        }

        @Override
        public void draw(ItemStack stack, int mouseX, int mouseY, float partialTicks) {
            for (Component component : components) {
                component.draw(Minecraft.getInstance(), mouseX, mouseY, partialTicks);
            }
            super.draw(stack, mouseX, mouseY, partialTicks);
        }

        static class DisplayComponent extends Component {

            final ItemStack stack;
            final DisplayType type;
            final int cx, cy;

            public DisplayComponent(int x, int y, int w, int h, ItemStack stack, DisplayType type) {
                super(x, y, w, h);
                this.stack = stack;
                this.type = type;
                this.cx = x + (w - 8) / 2;
                this.cy = y + (h - 8) / 2;
            }

            @Override
            public void draw(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
                RenderHelper.drawColoredShape(x, y, x + width, y + height, 0.6F, 0.6F, 0.6F, 1.0F);
                GlStateManager.pushMatrix();
                mc.textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
                mc.textureManager.getTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
                GlStateManager.enableRescaleNormal();
                GlStateManager.enableAlphaTest();
                GlStateManager.alphaFunc(516, 0.1F);
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                IBakedModel model = mc.getItemRenderer().getModelWithOverrides(stack);
                GlStateManager.translatef(cx, cy, 100.0F);
                GlStateManager.translatef(8.0F, 16.0F, 0.0F);
                GlStateManager.scalef(1.0F, -1.0F, 1.0F);
                GlStateManager.scalef(16.0F, 16.0F, 16.0F);
                model = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GUI, false);
                GlStateManager.rotated(-30.0, 1.0, 0.0, 0.0);
                GlStateManager.rotated(-30.0, 0.0, 0.0, 1.0);
                GlStateManager.scaled(4.0, 4.0, 4.0);
                this.type.applyTransforms();
                mc.getItemRenderer().renderItem(stack, model);
                GlStateManager.disableAlphaTest();
                GlStateManager.disableRescaleNormal();
                GlStateManager.disableLighting();
                GlStateManager.popMatrix();
                mc.textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
                mc.textureManager.getTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
            }
        }

        enum DisplayType {
            FRONT(() -> {
                GlStateManager.rotated(103.0, 0.0, 1.0, 0.0);
                GlStateManager.rotated(-15.0, 1.0, 0.0, 0.0);
            }),
            SIDE(() -> {

            }),
            TOP(() -> {
                GlStateManager.rotated(10, 0, 1, 0);
                GlStateManager.rotated(90, 0, 0, 1);
            }),
            DIAGONAL(() -> {
                GlStateManager.translated(1, -0.2, 0);
                GlStateManager.rotated(30, 1, 0, 0);
                GlStateManager.rotated(30, 0, 0, 1);
            });

            final Runnable runnable;

            DisplayType(Runnable runnable) {
                this.runnable = runnable;
            }

            void applyTransforms() {
                runnable.run();
            }
        }
    }
}
