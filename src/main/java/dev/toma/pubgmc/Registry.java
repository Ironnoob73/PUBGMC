package dev.toma.pubgmc;

import dev.toma.pubgmc.capability.player.PlayerCapHelper;
import dev.toma.pubgmc.client.model.baked.DriveableSpawnerBakedModel;
import dev.toma.pubgmc.common.block.PMCHorizontalBlock;
import dev.toma.pubgmc.common.block.crafting.AmmoFactoryBlock;
import dev.toma.pubgmc.common.block.crafting.WeaponFactoryBlock;
import dev.toma.pubgmc.common.entity.ParachuteEntity;
import dev.toma.pubgmc.common.entity.throwable.FlashEntity;
import dev.toma.pubgmc.common.entity.throwable.GrenadeEntity;
import dev.toma.pubgmc.common.entity.throwable.MolotovEntity;
import dev.toma.pubgmc.common.entity.throwable.SmokeEntity;
import dev.toma.pubgmc.common.entity.vehicle.AirDriveableEntity;
import dev.toma.pubgmc.common.entity.vehicle.LandDriveableEntity;
import dev.toma.pubgmc.common.item.healing.HealingItem;
import dev.toma.pubgmc.common.item.utility.FuelCanItem;
import dev.toma.pubgmc.common.item.utility.ParachuteItem;
import dev.toma.pubgmc.common.item.utility.ThrowableItem;
import dev.toma.pubgmc.common.item.utility.VehicleSpawnerItem;
import dev.toma.pubgmc.common.item.wearable.BulletProofArmor;
import dev.toma.pubgmc.util.UsefulFunctions;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Registry {

    @ObjectHolder(Pubgmc.MODID)
    public static class PMCItems {
        public static final HealingItem BANDAGE = null;
        public static final HealingItem FIRST_AID_KIT = null;
        public static final HealingItem MEDKIT = null;
        public static final HealingItem ENERGY_DRINK = null;
        public static final HealingItem PAINKILLERS = null;
        public static final HealingItem ADRENALINE_SYRINGE = null;
        public static final BulletProofArmor BASIC_HELMET = null;
        public static final BulletProofArmor BASIC_VEST = null;
        public static final BulletProofArmor POLICE_HELMET = null;
        public static final BulletProofArmor POLICE_VEST = null;
        public static final BulletProofArmor MILITARY_HELMET = null;
        public static final BulletProofArmor MILITARY_VEST = null;
        public static final ParachuteItem PARACHUTE = null;
        public static final ThrowableItem GRENADE = null;
        public static final ThrowableItem SMOKE = null;
        public static final ThrowableItem FLASH = null;
        public static final ThrowableItem MOLOTOV = null;
        public static final VehicleSpawnerItem SPAWN_UAZ = null;
        public static final VehicleSpawnerItem SPAWN_GLIDER = null;
        public static final FuelCanItem FUEL_CAN = null;
    }

    @ObjectHolder(Pubgmc.MODID)
    public static class PMCBlocks {
        public static final PMCHorizontalBlock GENERATOR = null;
        public static final PMCHorizontalBlock STORAGE = null;
        public static final PMCHorizontalBlock INTERFACE = null;
        public static final PMCHorizontalBlock PRODUCER = null;
        public static final WeaponFactoryBlock WEAPON_FACTORY = null;
        public static final AmmoFactoryBlock AMMO_FACTORY = null;
    }

    @ObjectHolder(Pubgmc.MODID)
    public static class PMCEntityTypes {
        public static final EntityType<ParachuteEntity> PARACHUTE = null;
        public static final EntityType<GrenadeEntity> GRENADE = null;
        public static final EntityType<GrenadeEntity> SMOKE = null;
        public static final EntityType<GrenadeEntity> MOLOTOV = null;
        public static final EntityType<FlashEntity> FLASH = null;
        public static final EntityType<LandDriveableEntity.UAZDriveable> UAZ = null;
        public static final EntityType<AirDriveableEntity.GliderDriveable> GLIDER = null;
    }

    @Mod.EventBusSubscriber(modid = Pubgmc.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class CommonHandler {

        @SubscribeEvent
        public static void onItemRegister(RegistryEvent.Register<Item> event) {
            IForgeRegistry<Item> registry = event.getRegistry();
            registry.registerAll(
                    HealingItem.Builder.create()
                            .stackSize(5)
                            .canUse(player -> player.getHealth() < 15)
                            .useDuration(80)
                            .messages("pubgmc.heal.bandage.success", "pubgmc.heal.bandage.fail")
                            .onFinish(player -> player.setHealth(Math.min(15, player.getHealth() + 3)))
                            .build("bandage"),
                    HealingItem.Builder.create()
                            .stackSize(1)
                            .canUse(player -> player.getHealth() < 15)
                            .useDuration(120)
                            .messages("pubgmc.heal.first_aid.success", "pubgmc.heal.first_aid.fail")
                            .onFinish(player -> player.setHealth(15))
                            .build("first_aid_kit"),
                    HealingItem.Builder.create()
                            .stackSize(1)
                            .canUse(player -> player.getHealth() < 20)
                            .useDuration(160)
                            .messages("pubgmc.heal.medkit.success", "pubgmc.heal.medkit.fail")
                            .onFinish(player -> player.setHealth(20))
                            .build("medkit"),
                    HealingItem.Builder.create()
                            .stackSize(3)
                            .canUse(UsefulFunctions.alwaysTruePredicate())
                            .useDuration(80)
                            .messages("pubgmc.heal.energy_drink.success", null)
                            .onFinish(player -> PlayerCapHelper.addBoostValue(player, 8))
                            .build("energy_drink"),
                    HealingItem.Builder.create()
                            .stackSize(3)
                            .canUse(UsefulFunctions.alwaysTruePredicate())
                            .useDuration(120)
                            .messages("pubgmc.heal.painkillers.success", null)
                            .onFinish(player -> PlayerCapHelper.addBoostValue(player, 12))
                            .build("painkillers"),
                    HealingItem.Builder.create()
                            .stackSize(1)
                            .canUse(UsefulFunctions.alwaysTruePredicate())
                            .useDuration(120)
                            .messages("pubgmc.heal.adrenaline_syringe.success", null)
                            .onFinish(player -> PlayerCapHelper.addBoostValue(player, 20))
                            .build("adrenaline_syringe"),
                    new BulletProofArmor("basic_helmet", 0.7F, BulletProofArmor.Material.BASIC, EquipmentSlotType.HEAD).textureMap(0, 0, 0, 1),
                    new BulletProofArmor("basic_vest", 0.7F, BulletProofArmor.Material.BASIC, EquipmentSlotType.CHEST).textureMap(3, 0, 3, 1),
                    new BulletProofArmor("police_helmet", 0.6F, BulletProofArmor.Material.POLICE, EquipmentSlotType.HEAD).textureMap(1, 0, 1, 1),
                    new BulletProofArmor("police_vest", 0.6F, BulletProofArmor.Material.POLICE, EquipmentSlotType.CHEST).textureMap(4, 0, 4, 1),
                    new BulletProofArmor("military_helmet", 0.45F, BulletProofArmor.Material.MILITARY, EquipmentSlotType.HEAD).textureMap(2, 0, 2, 1),
                    new BulletProofArmor("military_vest", 0.45F, BulletProofArmor.Material.MILITARY, EquipmentSlotType.CHEST).textureMap(5, 0, 5, 1),
                    new ParachuteItem("parachute"),
                    new ThrowableItem("grenade", 100, GrenadeEntity::new),
                    new ThrowableItem("smoke", 20, SmokeEntity::new),
                    new ThrowableItem("molotov", 0, MolotovEntity::new),
                    new ThrowableItem("flash", 100, FlashEntity::new),
                    new VehicleSpawnerItem("spawn_uaz", LandDriveableEntity.UAZDriveable::new, instance -> instance.add(() -> PMCItems.SPAWN_UAZ, LandDriveableEntity.UAZDriveable.class)),
                    new VehicleSpawnerItem("spawn_glider", AirDriveableEntity.GliderDriveable::new, instance -> instance.add(() -> PMCItems.SPAWN_GLIDER, AirDriveableEntity.GliderDriveable.class)),
                    new FuelCanItem("fuel_can")
            );
            blockItemList.stream().filter(Objects::nonNull).forEach(registry::register);
            blockItemList = null;
        }

        public static List<BlockItem> blockItemList = new ArrayList<>();

        @SubscribeEvent
        public static void onBlockRegister(RegistryEvent.Register<Block> event) {
            event.getRegistry().registerAll(
                    new PMCHorizontalBlock("generator", Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(2.5F)),
                    new PMCHorizontalBlock("storage", Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(2.5F)),
                    new PMCHorizontalBlock("interface", Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(2.5F)),
                    new PMCHorizontalBlock("producer", Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(2.5F)),
                    new WeaponFactoryBlock("weapon_factory"),
                    new AmmoFactoryBlock("ammo_factory")
            );
        }

        @SubscribeEvent
        public static void onEntityTypeRegister(RegistryEvent.Register<EntityType<?>> event) {
            event.getRegistry().registerAll(
                    registerType("parachute", track_builder(ParachuteEntity::new, EntityClassification.MISC, 256).size(1.5F, 2.5F)),
                    registerType("grenade", track_builder(GrenadeEntity::new, EntityClassification.MISC, 32).size(0.2F, 0.2F)),
                    registerType("smoke", track_builder(SmokeEntity::new, EntityClassification.MISC, 32).size(0.2F, 0.2F)),
                    registerType("molotov", track_builder(MolotovEntity::new, EntityClassification.MISC, 32).size(0.2F, 0.2F)),
                    registerType("flash", track_builder(FlashEntity::new, EntityClassification.MISC, 32).size(0.2F, 0.2F)),
                    registerType("uaz", track_builder(LandDriveableEntity.UAZDriveable::new, EntityClassification.MISC, 64).size(2.25F, 2.0F)),
                    registerType("glider", track_builder(AirDriveableEntity.GliderDriveable::new, EntityClassification.MISC, 64).size(2.5F, 2.0F))
            );
        }

        @SuppressWarnings("unchecked")
        private static <E extends Entity> EntityType<E> registerType(String name, EntityType.Builder<E> builder) {
            return (EntityType<E>) builder.build(Pubgmc.MODID + ":" + name).setRegistryName(Pubgmc.makeResource(name));
        }

        private static <E extends Entity> EntityType.Builder<E> builder(EntityType.IFactory<E> factory, EntityClassification classification) {
            return EntityType.Builder.create(factory, classification);
        }

        private static <E extends Entity> EntityType.Builder<E> track_builder(EntityType.IFactory<E> factory, EntityClassification classification, int range) {
            return EntityType.Builder.create(factory, classification).setTrackingRange(range).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true);
        }
    }

    @Mod.EventBusSubscriber(modid = Pubgmc.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientHandler {

        @SubscribeEvent
        public static void bake(ModelBakeEvent event) {
            Map<ResourceLocation, IBakedModel> registry = event.getModelRegistry();
            IBakedModel model = new DriveableSpawnerBakedModel();
            registry.put(get(PMCItems.SPAWN_UAZ), model);
            registry.put(get(PMCItems.SPAWN_GLIDER), model);
        }

        public static ModelResourceLocation get(Item item) {
            return new ModelResourceLocation(item.getRegistryName(), "inventory");
        }
    }
}
