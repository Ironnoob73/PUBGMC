package dev.toma.pubgmc.client;

import dev.toma.pubgmc.Pubgmc;
import net.minecraft.util.ResourceLocation;

public class ScopeInfo {

    public static final ResourceLocation OVERLAY_RED_DOT = Pubgmc.makeResource("textures/scope/overlay_red_dot.png");
    public static final ResourceLocation OVERLAY_HOLOGRAPHIC = Pubgmc.makeResource("textures/scope/overlay_holographic.png");
    public static final ResourceLocation OVERLAY_2X = Pubgmc.makeResource("textures/scope/overlay_2x.png");
    public static final ResourceLocation OVERLAY_3X = Pubgmc.makeResource("textures/scope/overlay_3x.png");
    public static final ResourceLocation OVERLAY_4X = Pubgmc.makeResource("textures/scope/overlay_4x.png");
    public static final ResourceLocation OVERLAY_6X = Pubgmc.makeResource("textures/scope/overlay_6x.png");
    public static final ResourceLocation OVERLAY_8X = Pubgmc.makeResource("textures/scope/overlay_8x.png");
    public static final ResourceLocation OVERLAY_15X = Pubgmc.makeResource("textures/scope/overlay_15x.png");
    public static final ResourceLocation OVERLAY_VSS = Pubgmc.makeResource("textures/scope/overlay_vss.png");
    public static final ResourceLocation OVERLAY_WINCHESTER = Pubgmc.makeResource("textures/scope/overlay_winchester.png");

    public static final ScopeInfo VSS_SCOPE = new ScopeInfo(OVERLAY_VSS, 15.0F, true);
    public static final ScopeInfo WINCHESTER_SCOPE = new ScopeInfo(OVERLAY_WINCHESTER, 20.0F, true);

    private final ResourceLocation overlay;
    private final float zoom;
    private final float sensitivityModifier;
    private final boolean renderWorldOverlay;

    public ScopeInfo(ResourceLocation overlay) {
        this(overlay, -1, false);
    }

    public ScopeInfo(ResourceLocation overlay, float zoom) {
        this(overlay, zoom, 1.0F, true);
    }

    public ScopeInfo(ResourceLocation overlay, float zoom, boolean renderWorldOverlay) {
        this(overlay, zoom, 1.0F, renderWorldOverlay);
    }

    public ScopeInfo(ResourceLocation overlay, float zoom, float sensitivityModifier) {
        this(overlay, zoom, sensitivityModifier, true);
    }

    public ScopeInfo(ResourceLocation overlay, float zoom, float sensitivityModifier, boolean renderWorldOverlay) {
        this.overlay = overlay;
        this.zoom = zoom;
        this.sensitivityModifier = sensitivityModifier;
        this.renderWorldOverlay = renderWorldOverlay;
    }

    public float getSensitivityModifier() {
        return sensitivityModifier;
    }

    public boolean shouldRenderPiP() {
        return renderWorldOverlay;
    }

    public float getZoom() {
        return zoom;
    }

    public ResourceLocation getTextureOverlay() {
        return overlay;
    }
}
