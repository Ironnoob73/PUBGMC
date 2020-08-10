package dev.toma.pubgmc.common.item.gun;

import dev.toma.pubgmc.capability.IPlayerCap;
import dev.toma.pubgmc.capability.player.PlayerCapFactory;
import dev.toma.pubgmc.client.ScopeInfo;
import dev.toma.pubgmc.client.animation.Animations;
import dev.toma.pubgmc.client.animation.HandAnimate;
import dev.toma.pubgmc.client.animation.gun.GunAnimationPack;
import dev.toma.pubgmc.common.item.PMCItem;
import dev.toma.pubgmc.common.item.gun.attachment.AttachmentCategory;
import dev.toma.pubgmc.common.item.gun.attachment.AttachmentItem;
import dev.toma.pubgmc.common.item.gun.attachment.GunAttachments;
import dev.toma.pubgmc.network.NetworkManager;
import dev.toma.pubgmc.network.packet.CPacketAnimation;
import dev.toma.pubgmc.util.function.Bool2FloatFunction;
import dev.toma.pubgmc.util.function.Bool2IntFunction;
import dev.toma.pubgmc.util.function.Bool2ObjFunction;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class GunItem extends PMCItem implements HandAnimate {

    protected final float damage;
    protected final float headshotMultiplier;
    protected final float initialVelocity;
    protected final float gravityEffect;
    protected final int gravityResistantTime;
    protected final int firerate;
    protected final Firemode defaultFiremode;
    protected final Function<Firemode, Firemode> firemodeSwitchFunction;
    protected final ReloadManager reloadManager;
    protected final Bool2IntFunction reloadTime;
    protected final ShootManager shootManager;
    protected final BiFunction<GunItem, ItemStack, Integer> ammoLimit;
    protected final AmmoType ammoType;
    protected final float verticalRecoil;
    protected final float horizontalRecoil;
    protected final Bool2ObjFunction<SoundEvent> shootSound;
    protected final Bool2FloatFunction shootVolume;
    protected final Bool2ObjFunction<SoundEvent> reloadSound;
    protected final GunAnimationPack animationPack;
    @Nullable
    protected final ScopeInfo customScopeFactory;
    protected final GunAttachments attachments;

    protected GunItem(String name, Properties properties, GunBuilder builder) {
        super(name, properties);
        this.damage = builder.damage;
        this.headshotMultiplier = builder.headshotMultiplier;
        this.initialVelocity = builder.initialVelocity;
        this.gravityEffect = builder.gravity;
        this.gravityResistantTime = builder.gravityResistance;
        this.firerate = builder.firerate;
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
        this.reloadSound = builder.reloadSound;
        Supplier<GunAnimationPack> tmp = DistExecutor.callWhenOn(Dist.CLIENT, builder.animations);
        if (tmp != null) {
            this.animationPack = tmp.get();
        } else animationPack = null;
        this.customScopeFactory = builder.customScopeFactory;
        this.attachments = builder.attachments;
    }

    public void doReload(PlayerEntity player, World world, ItemStack stack) {
        this.reloadManager.doReload(player, world, stack);
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
            if ((ammo > 0 || player.isCreative()) && !tracker.hasCooldown(stack.getItem()) && !cap.getReloadInfo().isReloading()) {
                shootManager.shoot(source, world, stack);
                if (!player.isCreative()) addAmmo(stack, -1);
                world.playSound(null, source.posX, source.posY, source.posZ, getShootSound(silent), SoundCategory.MASTER, getVolume(silent), 1.0F);
                tracker.setCooldown(stack.getItem(), this.firerate);
                NetworkManager.sendToClient((ServerPlayerEntity) player, new CPacketAnimation(Animations.RECOIL, CPacketAnimation.Result.PLAY));
            }
        } else {
            if (ammo > 0) {
                world.playSound(null, source.posX, source.posY, source.posZ, getShootSound(silent), SoundCategory.MASTER, getVolume(silent), 1.0F);
                shootManager.shoot(source, world, stack);
                addAmmo(stack, -1);
            }
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

    public SoundEvent getReloadSound(ItemStack stack) {
        return reloadSound.apply(getAttachment(AttachmentCategory.MAGAZINE, stack).isQuickdraw());
    }

    public int getReloadTime(ItemStack stack) {
        return reloadTime.apply(getAttachment(AttachmentCategory.MAGAZINE, stack).isQuickdraw());
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

    private CompoundNBT getOrCreateTag(ItemStack stack) {
        if (!stack.hasTag()) {
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

    public static class GunBuilder {

        protected static Logger log = LogManager.getLogger("pubgmc-gunbuilder");
        private float damage;
        private float headshotMultiplier;
        private float initialVelocity;
        private float gravity;
        private int gravityResistance;
        private int firerate;
        private float verticalRecoil;
        private float horizontalRecoil;
        private Firemode defaultFiremode = Firemode.SINGLE;
        private Function<Firemode, Firemode> firemodeSwitchFunction = Firemode::allModes;
        private ReloadManager reloadManager = ReloadManager.Magazine.instance;
        private Bool2IntFunction reloadTime;
        private ShootManager shootManager = ShootManager::handleNormal;
        private GunAttachments attachments = new GunAttachments();
        private BiFunction<GunItem, ItemStack, Integer> ammoLimit;
        private AmmoType ammoType;
        private Item.Properties properties = new Properties().group(GUNS).maxStackSize(1);
        private Supplier<Callable<ItemStackTileEntityRenderer>> ister;
        private Supplier<Callable<Supplier<GunAnimationPack>>> animations;
        private Bool2ObjFunction<SoundEvent> shootSound;
        private Bool2ObjFunction<SoundEvent> reloadSound;
        private Bool2FloatFunction shootVolumeFunction = silent -> silent ? 6.0F : 12.0F;
        private ScopeInfo customScopeFactory;

        private static <T> T nonullOrCorrectAndLog(T t, T other, String message, String name) {
            if (t == null) {
                log.warn("{} - Corrected {} -> {}: {}", name, t, other, message);
                return other;
            }
            return t;
        }

        private static int validOrCorrectAndLog(int input, int min, int max, String name, String property) {
            if (input >= min && input <= max) {
                return input;
            } else {
                int corrected = Math.min(max, Math.max(min, input));
                log.warn("{} - Corrected value {}: {} -> {}", name, property, input, corrected);
                return corrected;
            }
        }

        private static float validOrCorrectAndLog(float input, float min, float max, String name, String property) {
            if (input >= min && input <= max) {
                return input;
            } else {
                float corrected = Math.min(max, Math.max(min, input));
                log.warn("{} - Corrected value {}: {} -> {}", name, property, input, corrected);
                return corrected;
            }
        }

        private static <T> T nonnullOrThrow(T object, String message, String name) {
            return nonnullOrThrow(object, () -> new NullPointerException(name + " - " + message));
        }

        private static <T, E extends RuntimeException> T nonnullOrThrow(T object, Supplier<E> supplier) {
            if (object == null) {
                throw supplier.get();
            }
            return object;
        }

        public GunBuilder gunProperties(float damage, float headshotMultiplier, float initialVelocity, float gravity, int gravityResistance) {
            this.damage = damage;
            this.headshotMultiplier = headshotMultiplier;
            this.initialVelocity = initialVelocity;
            this.gravity = gravity;
            this.gravityResistance = gravityResistance;
            return this;
        }

        public GunBuilder firemodes(Firemode defaultFiremode, Function<Firemode, Firemode> firemodeSwitchFunction) {
            this.defaultFiremode = defaultFiremode;
            this.firemodeSwitchFunction = firemodeSwitchFunction;
            return this;
        }

        public GunBuilder firerate(int delayBetweenShots) {
            this.firerate = delayBetweenShots;
            return this;
        }

        public GunBuilder reload(ReloadManager manager, Bool2IntFunction reloadTime) {
            this.reloadManager = manager;
            this.reloadTime = reloadTime;
            return this;
        }

        public GunBuilder shoot(ShootManager manager) {
            this.shootManager = manager;
            return this;
        }

        public AttachmentBuilder attachments() {
            return new AttachmentBuilder(this);
        }

        public GunBuilder ammo(AmmoType type, BiFunction<GunItem, ItemStack, Integer> ammoLimit) {
            this.ammoType = type;
            this.ammoLimit = ammoLimit;
            return this;
        }

        public GunBuilder shootingSound(Bool2ObjFunction<SoundEvent> shootSound) {
            this.shootSound = shootSound;
            return this;
        }

        public GunBuilder shootingVolume(Bool2FloatFunction volumeFunction) {
            this.shootVolumeFunction = volumeFunction;
            return this;
        }

        public GunBuilder reloadingSound(Bool2ObjFunction<SoundEvent> reloadSound) {
            this.reloadSound = reloadSound;
            return this;
        }

        public GunBuilder itemProperties(Item.Properties properties) {
            this.properties = properties;
            return this;
        }

        public GunBuilder ister(Supplier<Callable<ItemStackTileEntityRenderer>> ister) {
            this.ister = ister;
            return this;
        }

        public GunBuilder animations(Supplier<Callable<Supplier<GunAnimationPack>>> animations) {
            this.animations = animations;
            return this;
        }

        public GunBuilder recoil(float verticalRecoil, float horizontalRecoil) {
            this.verticalRecoil = verticalRecoil;
            this.horizontalRecoil = horizontalRecoil;
            return this;
        }

        public GunBuilder scope(ScopeInfo scopeInfo) {
            this.customScopeFactory = scopeInfo;
            return this;
        }

        public GunItem build(String name) {
            damage = validOrCorrectAndLog(damage, 1.0F, 100.0F, name, "damage");
            headshotMultiplier = validOrCorrectAndLog(headshotMultiplier, 1.0F, 5.0F, name, "headShotmultiplier");
            initialVelocity = validOrCorrectAndLog(initialVelocity, 2.0F, 50.0F, name, "initialVelocity");
            gravity = validOrCorrectAndLog(gravity, 0.0F, 2.0F, name, "gravity");
            gravityResistance = validOrCorrectAndLog(gravityResistance, 0, Integer.MAX_VALUE, name, "gravityResistantTime");
            firerate = validOrCorrectAndLog(firerate, 1, 500, name, "firerate");
            defaultFiremode = nonnullOrThrow(defaultFiremode, "Default firemode cannot be null!", name);
            firemodeSwitchFunction = nonnullOrThrow(firemodeSwitchFunction, "No function defined for switching firemodes. This is bad", name);
            shootManager = nonnullOrThrow(shootManager, "Undefined shoot action!", name);
            reloadManager = nonullOrCorrectAndLog(reloadManager, ReloadManager.Magazine.instance, "Cannot use invalid reload manager", name);
            reloadTime = nonnullOrThrow(reloadTime, "Undefined reload time", name);
            ammoType = nonnullOrThrow(ammoType, "Ammo type is undefined!", name);
            ammoLimit = nonnullOrThrow(ammoLimit, "Unknown max ammo amount", name);
            animations = nonnullOrThrow(animations, "Undefined gun animations", name);
            verticalRecoil = validOrCorrectAndLog(verticalRecoil, 0.0F, 5.0F, name, "verticalRecoil");
            horizontalRecoil = validOrCorrectAndLog(horizontalRecoil, 0.0F, 5.0F, name, "horizontalRecoil");
            shootSound = nonnullOrThrow(shootSound, "Undefined shooting sounds", name);
            reloadSound = nonnullOrThrow(reloadSound, "Undefined reloading sounds", name);
            shootVolumeFunction = nonnullOrThrow(shootVolumeFunction, "Undefined shooting volume", name);
            return new GunItem(
                    name,
                    nonnullOrThrow(properties, "Item properties cannot be null!", name).setTEISR(nonnullOrThrow(ister, "Gun renderer cannot be null!", name)),
                    this
            );
        }
    }

    public static class AttachmentBuilder {

        private final GunBuilder parent;
        private final Map<AttachmentCategory, Supplier<? extends AttachmentItem[]>> attachmentCategorySupplierMap = new HashMap<>();

        private AttachmentBuilder(GunBuilder parent) {
            this.parent = parent;
        }

        public AttachmentBuilder barrel(Supplier<AttachmentItem.Barrel[]> supplier) {
            return insert(AttachmentCategory.BARREL, supplier);
        }

        public AttachmentBuilder grip(Supplier<AttachmentItem.Grip[]> supplier) {
            return insert(AttachmentCategory.GRIP, supplier);
        }

        public AttachmentBuilder magazine(Supplier<AttachmentItem.Magazine[]> supplier) {
            return insert(AttachmentCategory.BARREL, supplier);
        }

        public AttachmentBuilder stock(Supplier<AttachmentItem.Stock[]> supplier) {
            return insert(AttachmentCategory.STOCK, supplier);
        }

        public AttachmentBuilder scope(Supplier<AttachmentItem.Scope[]> supplier) {
            return insert(AttachmentCategory.SCOPE, supplier);
        }

        public GunBuilder build() {
            parent.attachments = new GunAttachments(attachmentCategorySupplierMap);
            return parent;
        }

        private AttachmentBuilder insert(AttachmentCategory category, Supplier<? extends AttachmentItem[]> supplier) {
            attachmentCategorySupplierMap.put(category, supplier);
            return this;
        }
    }
}
