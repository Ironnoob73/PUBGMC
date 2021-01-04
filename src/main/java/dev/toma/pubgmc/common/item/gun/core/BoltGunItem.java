package dev.toma.pubgmc.common.item.gun.core;

import dev.toma.pubgmc.client.animation.AnimationManager;
import dev.toma.pubgmc.client.animation.Animations;
import dev.toma.pubgmc.client.animation.gun.pack.GunAnimationPack;
import dev.toma.pubgmc.common.item.gun.attachment.AttachmentCategory;
import dev.toma.pubgmc.util.function.Bool2IntFunction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class BoltGunItem extends GunItem {

    private final Supplier<SoundEvent> boltSound;
    private final Bool2IntFunction boltTime;

    public BoltGunItem(String name, Properties properties, BoltGunBuilder boltGunBuilder) {
        super(name, properties, boltGunBuilder);
        this.boltSound = boltGunBuilder.boltSound;
        this.boltTime = boltGunBuilder.chamberTime;
    }

    public SoundEvent getBoltSound() {
        return boltSound.get();
    }

    @Override
    public void onEquip(LivingEntity entity, World world, ItemStack stack) {
        if(!isChambered(stack) && this.getAmmo(stack) > 0) {
            world.playSound(null, entity.posX, entity.posY, entity.posZ, this.getBoltSound(), SoundCategory.MASTER, 1.0F, 1.0F);
            setChambered(stack, true);
            if(world.isRemote) {
                AnimationManager.playNewAnimation(Animations.BOLT, ((GunAnimationPack.IBoltPack) getAnimations()).getBoltAnimation(boltTime.apply(this.getAttachment(AttachmentCategory.MAGAZINE, stack).isQuickdraw())));
            }
        }
    }

    @Override
    public boolean canShoot(ItemStack stack) {
        return this.isChambered(stack);
    }

    protected boolean isChambered(ItemStack stack) {
        return getOrCreateTag(stack).getBoolean("chambered");
    }

    protected void setChambered(ItemStack stack, boolean chambered) {
        getOrCreateTag(stack).putBoolean("chambered", chambered);
    }
}
