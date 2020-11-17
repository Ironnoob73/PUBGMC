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
    private AnimationType[] listeningTo = new AnimationType[0];

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

    public final void addAnimatedPart(int modelID, RendererModel model) {
        this.animatedPartMap.put(modelID, model);
    }

    public final void registerTypes(AnimationType... types) {
        if(Config.animationTool.get()) {
            int l = types.length;
            AnimationType[] array = new AnimationType[l + 1];
            System.arraycopy(types, 0, array, 0, l);
            array[l] = Animations.DEBUG;
            this.listeningTo = array;
        }
        this.listeningTo = types;
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
