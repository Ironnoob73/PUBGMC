package dev.toma.pubgmc;

import dev.toma.pubgmc.capability.player.PlayerCapHelper;
import dev.toma.pubgmc.common.entity.ParachuteEntity;
import dev.toma.pubgmc.common.entity.throwable.FlashEntity;
import dev.toma.pubgmc.common.entity.throwable.GrenadeEntity;
import dev.toma.pubgmc.common.entity.throwable.MolotovEntity;
import dev.toma.pubgmc.common.entity.throwable.SmokeEntity;
import dev.toma.pubgmc.common.item.healing.HealingItem;
import dev.toma.pubgmc.common.item.utility.ParachuteItem;
import dev.toma.pubgmc.common.item.utility.ThrowableItem;
import dev.toma.pubgmc.common.item.wearable.BulletProofArmor;
import dev.toma.pubgmc.util.UsefulFunctions;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

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
    }

    @ObjectHolder(Pubgmc.MODID)
    public static class PMCBlocks {

    }

    @ObjectHolder(Pubgmc.MODID)
    public static class PMCEntityTypes {
        public static final EntityType<ParachuteEntity> PARACHUTE = null;
        public static final EntityType<GrenadeEntity> GRENADE = null;
        public static final EntityType<GrenadeEntity> SMOKE = null;
        public static final EntityType<GrenadeEntity> MOLOTOV = null;
        public static final EntityType<FlashEntity> FLASH = null;
    }

    @Mod.EventBusSubscriber(modid = Pubgmc.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class CommonHandler {

        @SubscribeEvent
        public static void onItemRegister(RegistryEvent.Register<Item> event) {
            event.getRegistry().registerAll(
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
                    new ThrowableItem("grenade", 100, () -> PMCEntityTypes.GRENADE).factory(GrenadeEntity::new),
                    new ThrowableItem("smoke", 20, () -> PMCEntityTypes.SMOKE).factory(SmokeEntity::new),
                    new ThrowableItem("molotov", 0, () -> PMCEntityTypes.MOLOTOV).factory(MolotovEntity::new),
                    new ThrowableItem("flash", 100, () -> PMCEntityTypes.FLASH).factory(FlashEntity::new)
            );
        }

        @SubscribeEvent
        public static void onBlockRegister(RegistryEvent.Register<Block> event) {

        }

        @SubscribeEvent
        public static void onEntityTypeRegister(RegistryEvent.Register<EntityType<?>> event) {
            event.getRegistry().registerAll(
                    EntityType.Builder.create(ParachuteEntity::new, EntityClassification.MISC)
                            .setUpdateInterval(1)
                            .setTrackingRange(256)
                            .setShouldReceiveVelocityUpdates(true)
                            .size(1.5F, 2.5F)
                            .build("pubgmc:parachute")
                    .setRegistryName(Pubgmc.makeResource("parachute")),
                    EntityType.Builder.create(GrenadeEntity::new, EntityClassification.MISC)
                            .setUpdateInterval(1)
                            .setTrackingRange(32)
                            .setShouldReceiveVelocityUpdates(true)
                            .size(0.2F, 0.2F)
                            .build("pubgmc:grenade")
                    .setRegistryName(Pubgmc.makeResource("grenade")),
                    EntityType.Builder.create(SmokeEntity::new, EntityClassification.MISC)
                            .setUpdateInterval(1)
                            .setTrackingRange(32)
                            .setShouldReceiveVelocityUpdates(true)
                            .size(0.2F, 0.2F)
                            .build("pubgmc:smoke")
                    .setRegistryName(Pubgmc.makeResource("smoke")),
                    EntityType.Builder.create(MolotovEntity::new, EntityClassification.MISC)
                            .setUpdateInterval(1)
                            .setTrackingRange(32)
                            .setShouldReceiveVelocityUpdates(true)
                            .size(0.2F, 0.2F)
                            .build("pubgmc:molotov")
                    .setRegistryName(Pubgmc.makeResource("molotov")),
                    EntityType.Builder.create(FlashEntity::new, EntityClassification.MISC)
                            .setUpdateInterval(1)
                            .setTrackingRange(32)
                            .setShouldReceiveVelocityUpdates(true)
                            .size(0.2F, 0.2F)
                            .build("pubgmc:flash")
                    .setRegistryName(Pubgmc.makeResource("flash"))
            );
        }
    }
}
