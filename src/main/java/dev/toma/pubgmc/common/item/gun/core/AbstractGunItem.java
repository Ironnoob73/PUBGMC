package dev.toma.pubgmc.common.item.gun.core;

import dev.toma.pubgmc.capability.IPlayerCap;
import dev.toma.pubgmc.capability.player.PlayerCapFactory;
import dev.toma.pubgmc.client.ScopeInfo;
import dev.toma.pubgmc.client.animation.Animations;
import dev.toma.pubgmc.client.animation.HandAnimate;
import dev.toma.pubgmc.client.animation.gun.GunAnimationPack;
import dev.toma.pubgmc.common.item.PMCItem;
import dev.toma.pubgmc.common.item.gun.*;
import dev.toma.pubgmc.common.item.gun.attachment.AttachmentCategory;
import dev.toma.pubgmc.common.item.gun.attachment.AttachmentItem;
import dev.toma.pubgmc.common.item.gun.attachment.GunAttachments;
import dev.toma.pubgmc.network.NetworkManager;
import dev.toma.pubgmc.network.packet.CPacketAnimation;
import dev.toma.pubgmc.util.function.Bool2FloatFunction;
import dev.toma.pubgmc.util.function.Bool2ObjFunction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AbstractGunItem extends PMCItem implements HandAnimate {

    protected final float damage;
    protected final float headshotMultiplier;
    protected final float initialVelocity;
    protected final float gravityEffect;
    protected final int gravityResistantTime;
    protected final int firerate;
    protected final GunCategory gunCategory;
    protected final Firemode defaultFiremode;
    protected final Function<Firemode, Firemode> firemodeSwitchFunction;
    protected final ReloadManager reloadManager;
    protected final int reloadTime;
    protected final ShootManager shootManager;
    protected final BiFunction<AbstractGunItem, ItemStack, Integer> ammoLimit;
    protected final AmmoType ammoType;
    protected final float verticalRecoil;
    protected final float horizontalRecoil;
    protected final Bool2ObjFunction<SoundEvent> shootSound;
    protected final Bool2FloatFunction shootVolume;
    protected final GunAnimationPack animationPack;
    @Nullable
    protected final ScopeInfo customScopeFactory;
    protected final GunAttachments attachments;

    protected <B extends AbstractGunBuilder<?>> AbstractGunItem(String name, Properties properties, B builder) {
        super(name, properties);
        this.damage = builder.damage;
        this.headshotMultiplier = builder.headshotMultiplier;
        this.initialVelocity = builder.initialVelocity;
        this.gravityEffect = builder.gravity;
        this.gravityResistantTime = builder.gravityResistance;
        this.firerate = builder.firerate;
        this.gunCategory = builder.category;
        this.defaultFiremode = builder.defaultFiremode;
        this.firemodeSwitchFunction = builder.firemodeSwitchFunction;
        this.reloadManager = builder.reloadManager;
        this.shootManager = builder.shootManager;
        this.ammoLimit = builder.ammoLimit;
        this.ammoType = builder.ammoType;
        this.verticalRecoil = builder.verticalRecoil;
        this.horizontalRecoil = builder.horizontalRecoil;
        this.reloadTime = builder.reloadTime;
        this.shootSound = builder.shootSound;
        this.shootVolume = builder.shootVolumeFunction;
        Supplier<GunAnimationPack> tmp = DistExecutor.callWhenOn(Dist.CLIENT, builder.animations);
        if (tmp != null) {
            this.animationPack = tmp.get();
        } else animationPack = null;
        this.customScopeFactory = builder.customScopeFactory;
        this.attachments = builder.attachments;
    }

    public abstract SoundEvent getReloadSound(ItemStack stack);

    public void doReload(PlayerEntity player, World world, ItemStack stack) {
        this.reloadManager.doReload(player, world, stack);
    }

    public void onEquip(LivingEntity entity, World world, ItemStack stack) {

    }

    public boolean canShoot(ItemStack stack) {
        return true;
    }

    /**
     * Called server-side only
     *
     * @param source - the shooter
     * @param world  - the world
     * @param stack  - the gun itemstack object
     */
    public void shoot(LivingEntity source, World world, ItemStack stack) {
        int ammo = getAmmo(stack);
        boolean silent = getAttachment(AttachmentCategory.BARREL, stack).isSilent();
        if (source instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) source;
            CooldownTracker tracker = player.getCooldownTracker();
            IPlayerCap cap = PlayerCapFactory.get(player);
            if (ammo > 0 && !tracker.hasCooldown(stack.getItem()) && !cap.getReloadInfo().isReloading() && canShoot(stack)) {
                shootManager.shoot(source, world, stack);
                addAmmo(stack, -1);
                world.playSound(null, source.posX, source.posY, source.posZ, getShootSound(silent), SoundCategory.MASTER, getVolume(silent), 1.0F);
                tracker.setCooldown(stack.getItem(), this.firerate);
                NetworkManager.sendToClient((ServerPlayerEntity) player, new CPacketAnimation(Animations.RECOIL, CPacketAnimation.Result.PLAY));
            }
        } else {
            world.playSound(null, source.posX, source.posY, source.posZ, getShootSound(silent), SoundCategory.MASTER, getVolume(silent), 1.0F);
            shootManager.shoot(source, world, stack);
        }
    }

    public float getVolume(boolean silent) {
        return shootVolume.apply(silent);
    }

    public SoundEvent getShootSound(boolean silent) {
        return shootSound.apply(silent);
    }

    public SoundEvent getShootSound(ItemStack stack) {
        return shootSound.apply(getAttachment(AttachmentCategory.BARREL, stack).isSilent());
    }

    public int getReloadTime(ItemStack stack) {
        return getAttachment(AttachmentCategory.MAGAZINE, stack).isQuickdraw() ? (int)(reloadTime * 0.7) : reloadTime;
    }

    public boolean switchFiremode(PlayerEntity player, ItemStack stack) {
        Firemode current = getFiremode(stack);
        Firemode next = firemodeSwitchFunction.apply(current);
        if (!player.world.isRemote && next != current) {
            getOrCreateTag(stack).putInt("firemode", next.ordinal());
            player.sendStatusMessage(new TranslationTextComponent("firemode.switch." + next.name().toLowerCase()), true);
        }
        return current != next;
    }

    public Firemode getFiremode(ItemStack stack) {
        return Firemode.from(getOrCreateTag(stack).getInt("firemode"));
    }

    public ScopeInfo getScopeData(ItemStack stack) {
        return customScopeFactory != null ? customScopeFactory : this.getAttachment(AttachmentCategory.SCOPE, stack).getScopeInfo();
    }

    public AttachmentItem getAttachment(AttachmentCategory category, ItemStack stack) {
        CompoundNBT nbt = getOrCreateTag(stack).getCompound("attachments");
        String key = category.ordinal() + "";
        if (nbt.contains(key)) {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(nbt.getString(key)));
            if (item instanceof AttachmentItem) {
                return (AttachmentItem) item;
            }
        }
        return AttachmentItem.EMPTY;
    }

    public float getVerticalRecoil(PlayerEntity player, ItemStack stack) {
        float playerStateMultiplier = player.isSneaking() ? 0.7F : 1.0F;
        float barrelMultiplier = getAttachment(AttachmentCategory.BARREL, stack).getVerticalRecoilMultiplier();
        float gripMultiplier = getAttachment(AttachmentCategory.GRIP, stack).getVerticalRecoilMultiplier();
        float totalMultiplier = playerStateMultiplier * barrelMultiplier * gripMultiplier;
        return verticalRecoil * totalMultiplier;
    }

    public float getHorizontalRecoil(ItemStack stack) {
        float barrelMultiplier = getAttachment(AttachmentCategory.BARREL, stack).getHorizontalRecoilMultiplier();
        float gripMultiplier = getAttachment(AttachmentCategory.GRIP, stack).getHorizontalRecoilMultiplier();
        return horizontalRecoil * barrelMultiplier * gripMultiplier;
    }

    public void addAmmo(ItemStack stack, int amount) {
        int a = getAmmo(stack);
        setAmmo(stack, a + amount);
    }

    public void setAmmo(ItemStack stack, int ammo) {
        getOrCreateTag(stack).putInt("ammo", Math.min(ammoLimit.apply(this, stack), Math.max(0, ammo)));
    }

    public int getAmmo(ItemStack stack) {
        return getOrCreateTag(stack).getInt("ammo");
    }

    public ReloadManager getReloadManager() {
        return reloadManager;
    }

    public int getMaxAmmo(ItemStack stack) {
        return ammoLimit.apply(this, stack);
    }

    public GunCategory getCategory() {
        return gunCategory;
    }

    public float getGunDamage() {
        return damage;
    }

    public float getHeadshotMultiplier() {
        return headshotMultiplier;
    }

    public float getInitialVelocity() {
        return initialVelocity;
    }

    public float getGravityEffect() {
        return gravityEffect;
    }

    public int getGravityResistantTime() {
        return gravityResistantTime;
    }

    public int getFirerate() {
        return firerate;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void renderRightArm() {
        animationPack.applyOnRightArm();
        renderHand(HandSide.RIGHT);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void renderLeftArm() {
        animationPack.applyOnLeftArm();
        renderHand(HandSide.LEFT);
    }

    @OnlyIn(Dist.CLIENT)
    public GunAnimationPack getAnimations() {
        return animationPack;
    }

    public CompoundNBT getOrCreateTag(ItemStack stack) {
        CompoundNBT tag = stack.getTag();
        if (!stack.hasTag() || !tag.contains("ammo") || !tag.contains("firemode") || !tag.contains("attachments")) {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putInt("ammo", 0);
            nbt.putInt("firemode", defaultFiremode.ordinal());
            nbt.put("attachments", new CompoundNBT());
            stack.setTag(nbt);
        }
        return stack.getTag();
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        return true;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    public AmmoType getAmmoType() {
        return ammoType;
    }

    public GunAttachments getAttachmentList() {
        return attachments;
    }
}
