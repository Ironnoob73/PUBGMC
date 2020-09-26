package dev.toma.pubgmc.client.screen.menu;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.content.ContentManager;
import dev.toma.pubgmc.content.ContentResult;
import dev.toma.pubgmc.util.RenderHelper;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.RenderComponentsUtil;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.ForgeHooks;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class NewsScreen extends ComponentScreen implements RefreshListener {

    private final Screen lastScreen;
    private final ContentManager manager;
    private final List<ITextComponent> list = new ArrayList<>();
    private int atIndex;
    private int lineCount;

    public NewsScreen(Screen lastScreen, ContentManager manager) {
        super(new StringTextComponent("News screen"));
        this.lastScreen = lastScreen;
        this.manager = manager;
    }

    @Override
    public void initComponents() {
        int panelHeight = height - 50;
        atIndex = 0;
        list.clear();
        lineCount = panelHeight / (minecraft.fontRenderer.FONT_HEIGHT + 1);
        int quarter = width / 4;
        int w = quarter * 2;
        addComponent(new ButtonComponent(quarter, height - 30, w, 30, "Back to menu", pc -> minecraft.displayGuiScreen(lastScreen)));
        ContentResult result = manager.getCachedResult();
        String[] news = new String[0];
        int max = w - 20;
        if(result != null) {
            news = result.getNews();
        }
        if(news.length == 0) {
            news = new String[] {"Couldn't get latest mod news. Check your internet connection and try again (or wait 30 seconds for auto-refresh)"};
        }
        FontRenderer renderer = minecraft.fontRenderer;
        for(String string : news) {
            ITextComponent component = ForgeHooks.newChatWithLinks(string, false);
            if(max >= 0) {
                list.addAll(RenderComponentsUtil.splitText(component, max, renderer, true, true));
            }
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        minecraft.getTextureManager().bindTexture(MainMenuScreen.BACKGROUND_TEXTURE);
        RenderHelper.drawTexturedShape(0, 0, width, height);
        boolean mustFix = width % 2 != 0;
        int shadeWidth = width / 2;
        float minA = 0.05F;
        float maxA = 0.95F;
        GlStateManager.color3f(1.0F, 1.0F, 1.0F);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        GlStateManager.disableTexture();
        GlStateManager.enableBlend();
        GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        builder.pos(0, height, 0).color(0.0F, 0.0F, 0.0F, maxA).endVertex();
        builder.pos(shadeWidth + (mustFix ? 1 : 0), height, 0).color(0.0F, 0.0F, 0.0F, minA).endVertex();
        builder.pos(shadeWidth + (mustFix ? 1 : 0), 0, 0).color(0.0F, 0.0F, 0.0F, minA).endVertex();
        builder.pos(0, 0, 0).color(0.0F, 0.0F, 0.0F, maxA).endVertex();
        builder.pos(width - shadeWidth, height, 0).color(0.0F, 0.0F, 0.0F, minA).endVertex();
        builder.pos(width, height, 0).color(0.0F, 0.0F, 0.0F, maxA).endVertex();
        builder.pos(width, 0, 0).color(0.0F, 0.0F, 0.0F, maxA).endVertex();
        builder.pos(width - shadeWidth, 0, 0).color(0.0F, 0.0F, 0.0F, minA).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableBlend();
        GlStateManager.enableTexture();
        int quarter = width / 4;
        int w = quarter * 2;
        RenderHelper.drawColoredShape(quarter, 0, quarter + w, height - 30, 0.0F, 0.0F, 0.0F, 0.5F);
        for(int i = atIndex; i < atIndex + lineCount; i++) {
            if(i >= list.size()) break;
            String text = list.get(i).getFormattedText();
            int j = i - atIndex;
            minecraft.fontRenderer.drawString(text, quarter + 10, 10 + j * 10, 0xffffff);
        }
        if(list.size() > lineCount) {
            drawScrollbar(quarter + w);
        }
        super.render(mouseX, mouseY, partialTicks);
        RenderHelper.drawColoredShape(quarter, height - 31, quarter + w, height - 29, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        int n = -(int)(amount);
        int m = atIndex + n;
        if(m >= 0 && m <= list.size() - lineCount) {
            atIndex = m;
        }
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public void onRefresh() {
        init(minecraft, width, height);
    }

    private void drawScrollbar(int edge) {
        RenderHelper.drawColoredShape(edge - 2, 0, edge, height - 30, 0.0F, 0.0F, 0.0F, 1.0F);
        double step = (height - 30) / (double) list.size();
        double start = atIndex * step;
        double end = Math.min(height - 30, (atIndex + lineCount) * step);
        RenderHelper.drawColoredShape(edge - 2, (int)start, edge, (int) end, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}
