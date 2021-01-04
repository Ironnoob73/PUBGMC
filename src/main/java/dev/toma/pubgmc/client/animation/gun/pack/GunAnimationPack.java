package dev.toma.pubgmc.client.animation.gun.pack;

import dev.toma.pubgmc.client.animation.Animation;
import dev.toma.pubgmc.client.animation.GunPartAnimation;
import dev.toma.pubgmc.client.animation.gun.ReloadAnimation;
import dev.toma.pubgmc.client.animation.types.AimingAnimation;
import dev.toma.pubgmc.client.animation.types.RecoilAnimation;
import dev.toma.pubgmc.common.item.gun.core.AbstractGunItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class GunAnimationPack {

    public void applyOnLeftArm() {

    }

    public void applyOnRightArm() {

    }

    public float getAimYOffset(AbstractGunItem item, ItemStack stack) {
        return 0.06F;
    }

    public abstract AimingAnimation getAimingAnimation(AbstractGunItem gunItem, ItemStack stack);

    public abstract ReloadAnimation getReloadAnimation(AbstractGunItem gunItem, ItemStack stack, boolean isContinued);

    public Animation getShootAnimation(int time) {
        return new RecoilAnimation();
    }

    public interface IBoltPack {

        GunPartAnimation getBoltAnimation(int ticks);
    }
}
