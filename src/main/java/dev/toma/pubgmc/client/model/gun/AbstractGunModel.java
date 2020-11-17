package dev.toma.pubgmc.client.model.gun;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.client.animation.*;
import dev.toma.pubgmc.config.Config;
import dev.toma.pubgmc.util.object.LazyLoader;
import dev.toma.pubgmc.util.object.Optional;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public abstract class AbstractGunModel extends Model {

    private final Map<Integer, RendererModel> animatedPartMap = new HashMap<>();
    private final AnimationType[] listeningTo;

    public AbstractGunModel() {
        if(Config.animationTool.get()) {
            listeningTo = new AnimationType[] {Animations.RELOADING, Animations.DEBUG};
        } else {
            listeningTo = new AnimationType[] {Animations.RELOADING};
        }
    }

    public abstract void doModelRender(ItemStack stack);

    public final void render(ItemStack stack) {
        this.doModelRender(stack);
        for(Map.Entry<Integer, RendererModel> entry : animatedPartMap.entrySet()) {
            GlStateManager.pushMatrix();
            for(AnimationType type : listeningTo) {
                Optional<Animation> animationOptional = AnimationManager.getAnimationFromID(type);
                if(animationOptional.isPresent()) {
                    Animation animation = animationOptional.get();
                    if(animation instanceof GunPartAnimation) {
                        ((GunPartAnimation) animation).animateModel(entry.getKey());
                    }
                }
            }
            entry.getValue().render(1.0F);
            GlStateManager.popMatrix();
        }
    }

    public final void addAnimatedParts(RendererModel... models) {
        for (RendererModel model : models) {
            this.animatedPartMap.put(this.animatedPartMap.size(), model);
        }
    }

    public static void setRotationAngle(RendererModel model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public Map<Integer, RendererModel> getAnimatedPartMap() {
        return animatedPartMap;
    }
}
