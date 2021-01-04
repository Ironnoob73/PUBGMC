package dev.toma.pubgmc.common.item.gun.core;

import dev.toma.pubgmc.client.ScopeInfo;
import dev.toma.pubgmc.client.animation.gun.pack.GunAnimationPack;
import dev.toma.pubgmc.common.item.PMCItem;
import dev.toma.pubgmc.common.item.gun.*;
import dev.toma.pubgmc.common.item.gun.attachment.GunAttachments;
import dev.toma.pubgmc.util.function.Bool2FloatFunction;
import dev.toma.pubgmc.util.function.Bool2IntFunction;
import dev.toma.pubgmc.util.function.Bool2ObjFunction;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AbstractGunBuilder<G extends AbstractGunItem> {

    protected static Logger log = LogManager.getLogger("pubgmc-gunbuilder");
    float damage;
    float headshotMultiplier;
    float initialVelocity;
    float gravity;
    int gravityResistance;
    int firerate;
    float verticalRecoil;
    float horizontalRecoil;
    GunCategory category = GunCategory.MISC;
    Firemode defaultFiremode = Firemode.SINGLE;
    Function<Firemode, Firemode> firemodeSwitchFunction = Firemode::allModes;
    ReloadManager reloadManager = ReloadManager.Magazine.instance;
    int reloadTime;
    ShootManager shootManager = ShootManager::handleNormal;
    GunAttachments attachments = new GunAttachments();
    Bool2IntFunction ammoLimit;
    AmmoType ammoType;
    Item.Properties properties = new Item.Properties().group(PMCItem.GUNS).maxStackSize(1);
    Supplier<Callable<ItemStackTileEntityRenderer>> ister;
    Supplier<Callable<Supplier<GunAnimationPack>>> animations;
    Bool2ObjFunction<SoundEvent> shootSound;
    Bool2FloatFunction shootVolumeFunction = silent -> silent ? 6.0F : 12.0F;
    ScopeInfo customScopeFactory;

    public AbstractGunBuilder<G> gunProperties(float damage, float headshotMultiplier, float initialVelocity, float gravity, int gravityResistance) {
        this.damage = damage;
        this.headshotMultiplier = headshotMultiplier;
        this.initialVelocity = initialVelocity;
        this.gravity = gravity;
        this.gravityResistance = gravityResistance;
        return this;
    }

    public AbstractGunBuilder<G> firemodes(Firemode defaultFiremode, Function<Firemode, Firemode> firemodeSwitchFunction) {
        this.defaultFiremode = defaultFiremode;
        this.firemodeSwitchFunction = firemodeSwitchFunction;
        return this;
    }

    public AbstractGunBuilder<G> firerate(int delayBetweenShots) {
        this.firerate = delayBetweenShots;
        return this;
    }

    public AbstractGunBuilder<G> category(GunCategory category) {
        this.category = category;
        return this;
    }

    public AbstractGunBuilder<G> reload(ReloadManager manager, int reloadTime) {
        this.reloadManager = manager;
        this.reloadTime = reloadTime;
        return this;
    }

    public AbstractGunBuilder<G> shoot(ShootManager manager) {
        this.shootManager = manager;
        return this;
    }

    public AttachmentBuilder<AbstractGunBuilder<G>> attachments() {
        return new AttachmentBuilder<>(this);
    }

    public AbstractGunBuilder<G> ammo(AmmoType type, Bool2IntFunction ammoLimit) {
        this.ammoType = type;
        this.ammoLimit = ammoLimit;
        return this;
    }

    public AbstractGunBuilder<G> ammo(AmmoType type, int limit) {
        return this.ammo(type, ex -> limit);
    }

    public AbstractGunBuilder<G> shootingSound(Bool2ObjFunction<SoundEvent> shootSound) {
        this.shootSound = shootSound;
        return this;
    }

    public AbstractGunBuilder<G> shootingVolume(Bool2FloatFunction volumeFunction) {
        this.shootVolumeFunction = volumeFunction;
        return this;
    }

    public AbstractGunBuilder<G> itemProperties(Item.Properties properties) {
        this.properties = properties;
        return this;
    }

    public AbstractGunBuilder<G> ister(Supplier<Callable<ItemStackTileEntityRenderer>> ister) {
        this.ister = ister;
        return this;
    }

    public AbstractGunBuilder<G> animations(Supplier<Callable<Supplier<GunAnimationPack>>> animations) {
        this.animations = animations;
        return this;
    }

    public AbstractGunBuilder<G> recoil(float verticalRecoil, float horizontalRecoil) {
        this.verticalRecoil = verticalRecoil;
        this.horizontalRecoil = horizontalRecoil;
        return this;
    }

    public AbstractGunBuilder<G> scope(ScopeInfo scopeInfo) {
        this.customScopeFactory = scopeInfo;
        return this;
    }

    protected abstract G createGunObject(String name);

    public final G build(String name) {
        damage = validOrCorrectAndLog(damage, 1.0F, 100.0F, name, "damage");
        headshotMultiplier = validOrCorrectAndLog(headshotMultiplier, 1.0F, 5.0F, name, "headShotmultiplier");
        initialVelocity = validOrCorrectAndLog(initialVelocity, 2.0F, 50.0F, name, "initialVelocity");
        gravity = validOrCorrectAndLog(gravity, 0.0F, 2.0F, name, "gravity");
        gravityResistance = validOrCorrectAndLog(gravityResistance, 0, Integer.MAX_VALUE, name, "gravityResistantTime");
        firerate = validOrCorrectAndLog(firerate, 1, 500, name, "firerate");
        category = nonnullOrCorrectAndLog(category, GunCategory.MISC, "Weapon category cannot be null", name);
        defaultFiremode = nonnullOrThrow(defaultFiremode, "Default firemode cannot be null!", name);
        firemodeSwitchFunction = nonnullOrThrow(firemodeSwitchFunction, "No function defined for switching firemodes. This is bad", name);
        shootManager = nonnullOrThrow(shootManager, "Undefined shoot action!", name);
        reloadManager = nonnullOrCorrectAndLog(reloadManager, ReloadManager.Magazine.instance, "Cannot use invalid reload manager", name);
        reloadTime = validOrCorrectAndLog(reloadTime, 0, Integer.MAX_VALUE, name, "Reload time");
        ammoType = nonnullOrThrow(ammoType, "Ammo type is undefined!", name);
        ammoLimit = nonnullOrThrow(ammoLimit, "Unknown max ammo amount", name);
        animations = nonnullOrThrow(animations, "Undefined gun animations", name);
        verticalRecoil = validOrCorrectAndLog(verticalRecoil, 0.0F, 5.0F, name, "verticalRecoil");
        horizontalRecoil = validOrCorrectAndLog(horizontalRecoil, 0.0F, 5.0F, name, "horizontalRecoil");
        shootSound = nonnullOrThrow(shootSound, "Undefined shooting sounds", name);
        shootVolumeFunction = nonnullOrThrow(shootVolumeFunction, "Undefined shooting volume", name);
        animations = nonnullOrThrow(animations, "Undefined animations", name);
        shootVolumeFunction = nonnullOrThrow(shootVolumeFunction, "Undefined weapon volume", name);
        return createGunObject(name);
    }

    protected static <T> T nonnullOrCorrectAndLog(T t, T other, String message, String name) {
        if (t == null) {
            log.warn("{} - Corrected null -> {}: {}", name, other, message);
            return other;
        }
        return t;
    }

    protected static int validOrCorrectAndLog(int input, int min, int max, String name, String property) {
        if (input >= min && input <= max) {
            return input;
        } else {
            int corrected = Math.min(max, Math.max(min, input));
            log.warn("{} - Corrected value {}: {} -> {}", name, property, input, corrected);
            return corrected;
        }
    }

    protected static float validOrCorrectAndLog(float input, float min, float max, String name, String property) {
        if (input >= min && input <= max) {
            return input;
        } else {
            float corrected = Math.min(max, Math.max(min, input));
            log.warn("{} - Corrected value {}: {} -> {}", name, property, input, corrected);
            return corrected;
        }
    }

    protected static <T> T nonnullOrThrow(T object, String message, String name) {
        return nonnullOrThrow(object, () -> new NullPointerException(name + " - " + message));
    }

    protected static <T, E extends RuntimeException> T nonnullOrThrow(T object, Supplier<E> supplier) {
        if (object == null) {
            throw supplier.get();
        }
        return object;
    }
}
