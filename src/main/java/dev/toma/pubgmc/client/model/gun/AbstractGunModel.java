package dev.toma.pubgmc.client.model.gun;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.client.animation.Animation;
import dev.toma.pubgmc.client.animation.AnimationManager;
import dev.toma.pubgmc.client.animation.GunPartAnimation;
import dev.toma.pubgmc.client.animation.builder.AnimationType;
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
    private LazyLoader<AnimationType[]> listeningTo = new LazyLoader<>(() -> new AnimationType[0]);

    public abstract void doModelRender(ItemStack stack);

    public final void render(ItemStack stack) {
        this.doModelRender(stack);
        for(Map.Entry<Integer, RendererModel> entry : animatedPartMap.entrySet()) {
            GlStateManager.pushMatrix();
            for(AnimationType type : listeningTo.get()) {
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

    public final void registerTypes(Supplier<AnimationType[]> supplier) {
        this.listeningTo = new LazyLoader<>(supplier);
    }

    public Map<Integer, RendererModel> getAnimatedPartMap() {
        return animatedPartMap;
    }
}
