package dev.toma.pubgmc;

import com.mojang.datafixers.types.Type;
import dev.toma.pubgmc.client.model.baked.DriveableSpawnerBakedModel;
import dev.toma.pubgmc.client.render.item.VehicleSpawnerRenderer;
import dev.toma.pubgmc.common.block.PMCHorizontalBlock;
import dev.toma.pubgmc.common.block.crafting.AmmoFactoryBlock;
import dev.toma.pubgmc.common.block.crafting.WeaponFactoryBlock;
import dev.toma.pubgmc.common.entity.BulletEntity;
import dev.toma.pubgmc.common.entity.ParachuteEntity;
import dev.toma.pubgmc.common.entity.throwable.FlashEntity;
import dev.toma.pubgmc.common.entity.throwable.GrenadeEntity;
import dev.toma.pubgmc.common.entity.throwable.MolotovEntity;
import dev.toma.pubgmc.common.entity.throwable.SmokeEntity;
import dev.toma.pubgmc.common.entity.vehicle.AirDriveableEntity;
import dev.toma.pubgmc.common.entity.vehicle.LandDriveableEntity;
import dev.toma.pubgmc.common.item.PMCItem;
import dev.toma.pubgmc.common.item.gun.TestGun;
import dev.toma.pubgmc.common.item.healing.*;
import dev.toma.pubgmc.common.item.utility.FuelCanItem;
import dev.toma.pubgmc.common.item.utility.ParachuteItem;
import dev.toma.pubgmc.common.item.utility.ThrowableItem;
import dev.toma.pubgmc.common.item.utility.VehicleSpawnerItem;
import dev.toma.pubgmc.common.item.wearable.BulletProofArmor;
import dev.toma.pubgmc.common.tileentity.TileEntityWeaponFactory;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class Registry {

    @ObjectHolder(Pubgmc.MODID)
    public static class PMCItems {
        public static final TestGun TESTGUN = null;
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
        public static final EntityType<BulletEntity> BULLET = null;
    }

    @ObjectHolder(Pubgmc.MODID)
    public static class PMCTileEntities {
        public static final TileEntityType<TileEntityWeaponFactory> WEAPON_FACTORY = null;
    }

    public static class PMCContainers {
        public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = new DeferredRegister<>(ForgeRegistries.CONTAINERS, Pubgmc.MODID);
        //public static final RegistryObject<ContainerType<WeaponFactoryContainer>> WEAPON_FACTORY = register("name", WeaponFactoryContainer::new);

        private static <T extends Container> RegistryObject<ContainerType<T>> register(String name, ContainerType.IFactory<T> factory) {
            return CONTAINER_TYPES.register(name, () -> new ContainerType<>(factory));
        }
    }

    @Mod.EventBusSubscriber(modid = Pubgmc.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class CommonHandler {

        @SubscribeEvent
        public static void onItemRegister(RegistryEvent.Register<Item> event) {
            IForgeRegistry<Item> registry = event.getRegistry();
            registry.registerAll(
                    new TestGun(),
                    new BandageItem("bandage"),
                    new FirstAidKitItem("first_aid_kit"),
                    new MedkitItem("medkit"),
                    new EnergyDrinkItem("energy_drink"),
                    new PainkillerItem("painkillers"),
                    new AdrenalineSyringeItem("adrenaline_syringe"),
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
                    new VehicleSpawnerItem("spawn_uaz", LandDriveableEntity.UAZDriveable::new, new Item.Properties().maxStackSize(1).group(PMCItem.ITEMS).setTEISR(() -> () -> VehicleSpawnerRenderer.instance)),
                    new VehicleSpawnerItem("spawn_glider", AirDriveableEntity.GliderDriveable::new, new Item.Properties().maxStackSize(1).group(PMCItem.ITEMS).setTEISR(() -> () -> VehicleSpawnerRenderer.instance)),
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
                    registerEntity("parachute", track_builder(ParachuteEntity::new, EntityClassification.MISC, 256).size(1.5F, 2.5F)),
                    registerEntity("grenade", track_builder(GrenadeEntity::new, EntityClassification.MISC, 32).size(0.2F, 0.2F)),
                    registerEntity("smoke", track_builder(SmokeEntity::new, EntityClassification.MISC, 32).size(0.2F, 0.2F)),
                    registerEntity("molotov", track_builder(MolotovEntity::new, EntityClassification.MISC, 32).size(0.2F, 0.2F)),
                    registerEntity("flash", track_builder(FlashEntity::new, EntityClassification.MISC, 32).size(0.2F, 0.2F)),
                    registerEntity("uaz", track_builder(LandDriveableEntity.UAZDriveable::new, EntityClassification.MISC, 64).size(2.25F, 2.0F)),
                    registerEntity("glider", track_builder(AirDriveableEntity.GliderDriveable::new, EntityClassification.MISC, 64).size(2.5F, 2.0F)),
                    registerEntity("bullet", track_builder(BulletEntity::new, EntityClassification.MISC, 64).size(0.01F, 0.01F))
            );
        }

        @SubscribeEvent
        public static void onTileEntityRegister(RegistryEvent.Register<TileEntityType<?>> event) {
            event.getRegistry().registerAll(
                registerTileEntity("weapon_factory", TileEntityWeaponFactory::new, PMCBlocks.WEAPON_FACTORY)
            );
        }

        private static <T extends TileEntity> TileEntityType<T> registerTileEntity(String name, Supplier<T> factory, Block... blocks) {
            return registerTileEntity(name, factory, null, blocks);
        }

        private static <T extends TileEntity> TileEntityType<T> registerTileEntity(String name, Supplier<T> factory, @Nullable Type<?> fixer, Block... blocks) {
            TileEntityType<T> type = TileEntityType.Builder.create(factory, blocks).build(fixer);
            type.setRegistryName(Pubgmc.makeResource(name));
            return type;
        }

        private static <T extends Entity> EntityType<T> registerEntity(String name, EntityType.Builder<T> builder) {
            EntityType<T> type = builder.build(Pubgmc.MODID + ":" + name);
            type.setRegistryName(Pubgmc.makeResource(name));
            return type;
        }

        private static <T extends Entity> EntityType.Builder<T> builder(EntityType.IFactory<T> factory, EntityClassification classification) {
            return EntityType.Builder.create(factory, classification);
        }

        private static <T extends Entity> EntityType.Builder<T> track_builder(EntityType.IFactory<T> factory, EntityClassification classification, int range) {
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
