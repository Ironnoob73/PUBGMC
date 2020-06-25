package dev.toma.pubgmc.common.item.gun;

import dev.toma.pubgmc.common.entity.BulletEntity;
import dev.toma.pubgmc.common.item.PMCItem;
import dev.toma.pubgmc.common.item.gun.attachment.AttachmentCategory;
import dev.toma.pubgmc.common.item.gun.attachment.AttachmentItem;
import dev.toma.pubgmc.common.item.gun.attachment.GunAttachments;
import dev.toma.pubgmc.util.UsefulFunctions;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class GunItem extends PMCItem {

    protected final float damage;
    protected final float headshotMultiplier;
    protected final float initialVelocity;
    protected final float gravityEffect;
    protected final int gravityResistantTime;
    protected final int firerate;
    protected final Firemode defaultFiremode;
    protected final Function<Firemode, Firemode> firemodeSwitchFunction;
    protected final ReloadManager reloadManager;
    protected final ShootManager shootManager;
    protected final BiFunction<GunItem, ItemStack, Integer> ammoLimit;
    protected final AmmoType ammoType;

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
    }

    public void doReload(PlayerEntity player, World world, ItemStack stack) {
        this.reloadManager.doReload(player, world, stack);
    }

    /**
     * Called server-side only
     * @param source - the shooter
     * @param world - the world
     * @param stack - the gun itemstack object
     */
    public void shoot(LivingEntity source, World world, ItemStack stack) {
        int ammo = getAmmo(stack);
        // TODO all the basic logic
        if(source instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) source;
            CooldownTracker tracker = player.getCooldownTracker();
            // TODO not reloading etc - maybe handle from client
            if((ammo > 0 || player.isCreative()) && !tracker.hasCooldown(stack.getItem())) {
                shootManager.shoot(source, world, stack);
                if(!player.isCreative()) addAmmo(stack, -1);
                // TODO sound
                tracker.setCooldown(stack.getItem(), this.firerate);
            }
        } else {
            if(ammo > 0) {
                // TODO sound
                shootManager.shoot(source, world, stack);
                addAmmo(stack, -1);
            }
        }
    }

    public void switchFiremode(PlayerEntity player, ItemStack stack) {
        Firemode current = getFiremode(stack);
        Firemode next = firemodeSwitchFunction.apply(current);
        if(!player.world.isRemote && next != current) {
            getOrCreateTag(stack).putInt("firemode", next.ordinal());
            player.sendStatusMessage(new TranslationTextComponent("firemode.switch." + next.name().toLowerCase()), true);
        }
    }

    public Firemode getFiremode(ItemStack stack) {
        return Firemode.from(getOrCreateTag(stack).getInt("firemode"));
    }

    public AttachmentItem getAttachment(AttachmentCategory category, ItemStack stack) {
        CompoundNBT nbt = getOrCreateTag(stack).getCompound("attachments");
        String key = category.ordinal() + "";
        if(nbt.contains(key)) {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(nbt.getString(key)));
            if(item instanceof AttachmentItem) {
                return (AttachmentItem) item;
            }
        }
        return AttachmentItem.EMPTY;
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

    private CompoundNBT getOrCreateTag(ItemStack stack) {
        if(!stack.hasTag()) {
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

    public static class GunBuilder {

        protected static Logger log = LogManager.getLogger("pubgmc-gunbuilder");
        private float damage;
        private float headshotMultiplier;
        private float initialVelocity;
        private float gravity;
        private int gravityResistance;
        private int firerate;
        private Firemode defaultFiremode = Firemode.SINGLE;
        private Function<Firemode, Firemode> firemodeSwitchFunction = Firemode::allModes;
        private ReloadManager reloadManager = ReloadManager.Magazine.instance;
        private ShootManager shootManager = (source, world, stack) -> {
            GunItem gun = (GunItem) stack.getItem();
            int inaccuracy = 18;
            // TODO improve accuracy when aiming
            world.addEntity(new BulletEntity(world, source, stack, gun.damage, gun.headshotMultiplier, gun.initialVelocity, gun.gravityEffect, gun.gravityResistantTime, inaccuracy));
        };
        private GunAttachments attachments = new GunAttachments();
        private BiFunction<GunItem, ItemStack, Integer> ammoLimit;
        private AmmoType ammoType;
        private Item.Properties properties = new Properties().group(GUNS).maxStackSize(1);
        // TODO accept only specific renderers
        private Supplier<Callable<ItemStackTileEntityRenderer>> ister;

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

        public GunBuilder reload(ReloadManager manager) {
            this.reloadManager = manager;
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

        public GunBuilder itemProperties(Item.Properties properties) {
            this.properties = properties;
            return this;
        }

        public GunBuilder ister(Supplier<Callable<ItemStackTileEntityRenderer>> ister) {
            this.ister = ister;
            return this;
        }

        public GunItem build(String name) {
            damage = UsefulFunctions.correctAndLog(damage, 1.0F, 100.0F, log);
            headshotMultiplier = UsefulFunctions.correctAndLog(headshotMultiplier, 1.0F, 5.0F, log);
            initialVelocity = UsefulFunctions.correctAndLog(initialVelocity, 2.0F, 50.0F, log);
            gravity = UsefulFunctions.correctAndLog(gravity, 0.0F, 2.0F, log);
            gravityResistance = UsefulFunctions.correctAndLog(gravityResistance, 0, Integer.MAX_VALUE, log);
            firerate = UsefulFunctions.correctAndLog(firerate, 1, 500, log);
            defaultFiremode = UsefulFunctions.nonnullOrThrow(defaultFiremode, () -> new NullPointerException("Default firemode cannot be null!"));
            firemodeSwitchFunction = UsefulFunctions.nonnullOrThrow(firemodeSwitchFunction, () -> new NullPointerException("No function defined for switching firemodes. This is bad"));
            shootManager = UsefulFunctions.nonnullOrThrow(shootManager, () -> new NullPointerException("Undefined shoot action!"));
            reloadManager = UsefulFunctions.correctAndLog(reloadManager, Objects::nonNull, ReloadManager.Magazine.instance, "Cannot use invalid reload manager", log);
            ammoType = UsefulFunctions.nonnullOrThrow(ammoType, () -> new NullPointerException("Ammo type is undefined!"));
            ammoLimit = UsefulFunctions.nonnullOrThrow(ammoLimit, () -> new NullPointerException("Unknown max ammo amount"));
            return new GunItem(
                    name,
                    UsefulFunctions.validateOrThrow(properties, Objects::nonNull, () -> new NullPointerException("Item properties cannot be null!")).setTEISR(UsefulFunctions.validateOrThrow(ister, Objects::nonNull, () -> new NullPointerException("Gun renderer cannot be null!"))),
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
