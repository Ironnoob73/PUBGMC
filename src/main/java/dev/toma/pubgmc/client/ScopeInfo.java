package dev.toma.pubgmc.client;

import dev.toma.pubgmc.Pubgmc;
import net.minecraft.util.ResourceLocation;

public class ScopeInfo {

    public static final ScopeInfo DUMMY_INSTANCE = new ScopeInfo();
    public static final ResourceLocation BLUR_SMALL = Pubgmc.makeResource("shaders/blur_small.json");
    public static final ResourceLocation BLUR_MEDIUM = Pubgmc.makeResource("shaders/blur_medium.json");
    public static final ResourceLocation BLUR_LARGE = Pubgmc.makeResource("shaders/blur_large.json");
    public static final ResourceLocation OVERLAY_2X = Pubgmc.makeResource("textures/scope/overlay_2x.png");
    public static final ResourceLocation OVERLAY_3X = Pubgmc.makeResource("textures/scope/overlay_3x.png");
    public static final ResourceLocation OVERLAY_4X = Pubgmc.makeResource("textures/scope/overlay_4x.png");
    public static final ResourceLocation OVERLAY_6X = Pubgmc.makeResource("textures/scope/overlay_6x.png");
    public static final ResourceLocation OVERLAY_8X = Pubgmc.makeResource("textures/scope/overlay_8x.png");
    public static final ResourceLocation OVERLAY_15X = Pubgmc.makeResource("textures/scope/overlay_15x.png");
    public static final ResourceLocation OVERLAY_VSS = Pubgmc.makeResource("textures/scope/overlay_vss.png");
    public static final ResourceLocation OVERLAY_WINCHESTER = Pubgmc.makeResource("textures/scope/overlay_winchester.png");

    public static final ScopeInfo VSS_SCOPE = new ScopeInfo(OVERLAY_VSS, BLUR_SMALL, 15.0F, true);
    public static final ScopeInfo WINCHESTER_SCOPE = new ScopeInfo(OVERLAY_WINCHESTER, BLUR_MEDIUM, 20.0F, true);

    private final ResourceLocation overlay;
    private final ResourceLocation blurOverlay;
    private final float zoom;
    private final float sensitivityModifier;
    private final boolean renderWorldOverlay;
    private boolean overlayDisabled;

    public ScopeInfo() {
        this(null);
        overlayDisabled = true;
    }

    public ScopeInfo(ResourceLocation overlay) {
        this(overlay, null, -1, false);
    }

    public ScopeInfo(ResourceLocation overlay, ResourceLocation blur, float zoom) {
        this(overlay, blur, zoom, 1.0F, true);
    }

    public ScopeInfo(ResourceLocation overlay, ResourceLocation blur, float zoom, boolean renderWorldOverlay) {
        this(overlay, blur, zoom, 1.0F, renderWorldOverlay);
    }

    public ScopeInfo(ResourceLocation overlay, ResourceLocation blur, float zoom, float sensitivityModifier) {
        this(overlay, blur, zoom, sensitivityModifier, true);
    }

    public ScopeInfo(ResourceLocation overlay, ResourceLocation blur, float zoom, float sensitivityModifier, boolean renderWorldOverlay) {
        this.overlay = overlay;
        this.blurOverlay = blur;
        this.zoom = zoom;
        this.sensitivityModifier = sensitivityModifier;
        this.renderWorldOverlay = renderWorldOverlay;
    }

    public ScopeInfo disableOverlay() {
        this.overlayDisabled = true;
        return this;
    }

    public float getSensitivityModifier() {
        return sensitivityModifier;
    }

    public boolean shouldRenderPiP() {
        return renderWorldOverlay;
    }

    public boolean isOverlayDisabled() {
        return overlayDisabled;
    }

    public float getZoom() {
        return zoom;
    }

    public ResourceLocation getTextureOverlay() {
        return overlay;
    }

    public ResourceLocation getBlurShader() {
        return blurOverlay;
    }
}
