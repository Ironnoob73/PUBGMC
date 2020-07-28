package dev.toma.pubgmc.client;

import net.minecraft.util.ResourceLocation;

public class ScopeInfo {

    private final ResourceLocation overlay;
    private final float zoom;
    private final boolean renderWorldOverlay;

    public ScopeInfo(ResourceLocation overlay, float zoom, boolean renderWorldOverlay) {
        this.overlay = overlay;
        this.zoom = zoom;
        this.renderWorldOverlay = renderWorldOverlay;
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
