package dev.toma.pubgmc;

import dev.toma.pubgmc.client.ScopeInfo;
import dev.toma.pubgmc.client.animation.gun.*;
import dev.toma.pubgmc.client.model.baked.DummyBakedModel;
import dev.toma.pubgmc.client.model.baked.DummyGunBakedModel;
import dev.toma.pubgmc.client.render.item.GunRenderer;
import dev.toma.pubgmc.client.render.item.VehicleSpawnerRenderer;
import dev.toma.pubgmc.common.block.AirdropBlock;
import dev.toma.pubgmc.common.block.DeathCrateBlock;
import dev.toma.pubgmc.common.block.LootSpawnerBlock;
import dev.toma.pubgmc.common.block.PMCHorizontalBlock;
import dev.toma.pubgmc.common.block.crafting.AmmoFactoryBlock;
import dev.toma.pubgmc.common.block.crafting.WeaponFactoryBlock;
import dev.toma.pubgmc.common.entity.AirdropEntity;
import dev.toma.pubgmc.common.entity.BotEntity;
import dev.toma.pubgmc.common.entity.BulletEntity;
import dev.toma.pubgmc.common.entity.ParachuteEntity;
import dev.toma.pubgmc.common.entity.throwable.FlashEntity;
import dev.toma.pubgmc.common.entity.throwable.GrenadeEntity;
import dev.toma.pubgmc.common.entity.throwable.MolotovEntity;
import dev.toma.pubgmc.common.entity.throwable.SmokeEntity;
import dev.toma.pubgmc.common.entity.vehicle.air.GliderEntity;
import dev.toma.pubgmc.common.entity.vehicle.land.UAZEntity;
import dev.toma.pubgmc.common.item.PMCItem;
import dev.toma.pubgmc.common.item.gun.*;
import dev.toma.pubgmc.common.item.gun.attachment.AttachmentCategory;
import dev.toma.pubgmc.common.item.gun.attachment.AttachmentItem;
import dev.toma.pubgmc.common.item.gun.core.AbstractGunItem;
import dev.toma.pubgmc.common.item.gun.core.GunBuilder;
import dev.toma.pubgmc.common.item.healing.*;
import dev.toma.pubgmc.common.item.utility.*;
import dev.toma.pubgmc.common.item.wearable.BulletProofArmor;
import dev.toma.pubgmc.common.item.wearable.GhillieSuitItem;
import dev.toma.pubgmc.common.tileentity.AirdropTileEntity;
import dev.toma.pubgmc.common.tileentity.FlareAirdropTileEntity;
import dev.toma.pubgmc.games.BattleRoyaleGame;
import dev.toma.pubgmc.games.DeathmatchGame;
import dev.toma.pubgmc.games.GameType;
import dev.toma.pubgmc.games.args.ArgumentProvider;
import dev.toma.pubgmc.init.PMCItems;
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
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static dev.toma.pubgmc.init.PMCSounds.*;
import static dev.toma.pubgmc.init.PMCItems.*;

public class RegistryHandler {

    public static IForgeRegistry<GameType<?>> GAMES;

    @Mod.EventBusSubscriber(modid = Pubgmc.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class CommonHandler {

        public static List<BlockItem> blockItemList = new ArrayList<>();

        @SubscribeEvent
        public static void onItemRegister(RegistryEvent.Register<Item> event) {
            IForgeRegistry<Item> registry = event.getRegistry();
            registry.registerAll(
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
                    new VehicleSpawnerItem("spawn_uaz", UAZEntity::new, UAZEntity.class, new Item.Properties().maxStackSize(1).group(PMCItem.ITEMS).setTEISR(() -> VehicleSpawnerRenderer::new)),
                    new VehicleSpawnerItem("spawn_glider", GliderEntity::new, GliderEntity.class, new Item.Properties().maxStackSize(1).group(PMCItem.ITEMS).setTEISR(() -> VehicleSpawnerRenderer::new)),
                    new FuelCanItem("fuel_can"),
                    new AmmoItem("crossbow_bolt", AmmoType.CROSSBOW_BOLT),
                    new AmmoItem("ammo_12g", AmmoType.AMMO_12G),
                    new AmmoItem("ammo_9mm", AmmoType.AMMO_9MM),
                    new AmmoItem("ammo_45acp", AmmoType.AMMO_45ACP),
                    new AmmoItem("ammo_556mm", AmmoType.AMMO_556MM),
                    new AmmoItem("ammo_762mm", AmmoType.AMMO_762MM),
                    new AmmoItem("ammo_300m", AmmoType.AMMO_300M),
                    new AttachmentItem.Barrel("shotgun_choke", 1.0F, 1.0F, false, 0.5F),
                    new AttachmentItem.Barrel("suppressor_smg", true),
                    new AttachmentItem.Barrel("compensator_smg", 0.7F, 0.7F, false),
                    new AttachmentItem.Barrel("suppressor_ar", true),
                    new AttachmentItem.Barrel("compensator_ar", 0.7F, 0.7F, false),
                    new AttachmentItem.Barrel("suppressor_sr", true),
                    new AttachmentItem.Barrel("compensator_sr", 0.7F, 0.7F, false),
                    new AttachmentItem.Grip("angled_grip", 0.95F, 0.7F),
                    new AttachmentItem.Grip("vertical_grip", 0.7F, 0.95F),
                    new AttachmentItem.Magazine("quickdraw_smg", true, false),
                    new AttachmentItem.Magazine("extended_smg", false, true),
                    new AttachmentItem.Magazine("quickdraw_extended_smg", true, true),
                    new AttachmentItem.Magazine("quickdraw_ar", true, false),
                    new AttachmentItem.Magazine("extended_ar", false, true),
                    new AttachmentItem.Magazine("quickdraw_extended_ar", true, true),
                    new AttachmentItem.Magazine("quickdraw_sr", true, false),
                    new AttachmentItem.Magazine("extended_sr", false, true),
                    new AttachmentItem.Magazine("quickdraw_extended_sr", true, true),
                    new AttachmentItem.Stock("folding_stock", false, 1.25F),
                    new AttachmentItem.Stock("tactical_stock", false, 1.25F),
                    new AttachmentItem.Stock("cheekpad", false, 1.25F),
                    new AttachmentItem.Stock("bullet_loops", true, 1.0F),
                    new AttachmentItem.Scope("red_dot", new ScopeInfo(ScopeInfo.OVERLAY_RED_DOT)),
                    new AttachmentItem.Scope("holographic", new ScopeInfo(ScopeInfo.OVERLAY_HOLOGRAPHIC)),
                    new AttachmentItem.Scope("x2_scope", new ScopeInfo(ScopeInfo.OVERLAY_2X, ScopeInfo.BLUR_SMALL, 34)),
                    new AttachmentItem.Scope("x3_scope", new ScopeInfo(ScopeInfo.OVERLAY_3X, ScopeInfo.BLUR_SMALL, 26)),
                    new AttachmentItem.Scope("x4_scope", new ScopeInfo(ScopeInfo.OVERLAY_4X, ScopeInfo.BLUR_MEDIUM, 20)),
                    new AttachmentItem.Scope("x6_scope", new ScopeInfo(ScopeInfo.OVERLAY_6X, ScopeInfo.BLUR_MEDIUM, 16)),
                    new AttachmentItem.Scope("x8_scope", new ScopeInfo(ScopeInfo.OVERLAY_8X, ScopeInfo.BLUR_LARGE, 12)),
                    new AttachmentItem.Scope("x15_scope", new ScopeInfo(ScopeInfo.OVERLAY_15X, ScopeInfo.BLUR_LARGE, 8)),
                    new GunBuilder()
                            .reloadSound(quickdraw -> quickdraw ? P92_RELOAD_FAST : P92_RELOAD)
                            .category(GunCategory.PISTOL)
                            .gunProperties(4.0F, 2.5F, 11.0F, 0.05F, 3)
                            .recoil(1.5F, 0.75F)
                            .firerate(2)
                            .firemodes(Firemode.SINGLE, Function.identity())
                            .ammo(AmmoType.AMMO_9MM, (p92, stack) -> p92.getAttachment(AttachmentCategory.MAGAZINE, stack).isExtended() ? 20 : 15)
                            .reload(ReloadManager.Magazine.instance, 40)
                            .ister(() -> GunRenderer.P92Renderer::new)
                            .attachments()
                            .barrel(() -> new AttachmentItem.Barrel[]{SUPPRESSOR_SMG})
                            .scope(() -> new AttachmentItem.Scope[]{RED_DOT})
                            .magazine(() -> new AttachmentItem.Magazine[]{QUICKDRAW_SMG, EXTENDED_SMG, QUICKDRAW_EXTENDED_SMG})
                            .build()
                            .animations(() -> () -> AnimationPackP92::new)
                            .shootingSound(silenced -> silenced ? P92_SHOOT_SILENT : P92_SHOOT)
                            .shootingVolume(silenced -> silenced ? 5.0F : 9.0F)
                            .build("p92"),
                    new GunBuilder()
                            .reloadSound(q -> q ? P1911_RELOAD_FAST : P1911_RELOAD)
                            .category(GunCategory.PISTOL)
                            .gunProperties(5.0F, 2.5F, 13.0F, 0.05F, 3)
                            .recoil(1.5F, 0.75F)
                            .firerate(1)
                            .firemodes(Firemode.SINGLE, Function.identity())
                            .ammo(AmmoType.AMMO_45ACP, (gunItem, stack) -> gunItem.getAttachment(AttachmentCategory.MAGAZINE, stack).isExtended() ? 12 : 7)
                            .reload(ReloadManager.Magazine.instance, 25)
                            .ister(() -> GunRenderer.P1911Renderer::new)
                            .attachments()
                            .barrel(() -> new AttachmentItem.Barrel[] {SUPPRESSOR_SMG})
                            .scope(() -> new AttachmentItem.Scope[]{RED_DOT})
                            .magazine(() -> new AttachmentItem.Magazine[]{QUICKDRAW_SMG, EXTENDED_SMG, QUICKDRAW_EXTENDED_SMG})
                            .build()
                            .animations(() -> () -> AnimationPackP1911::new)
                            .shootingSound(s -> s ? P1911_SHOOT_SILENT : P1911_SHOOT)
                            .shootingVolume(s -> s ? 5.0F : 9.0F)
                            .build("p1911"),
                    new GunBuilder()
                            .reloadSound(q -> q ? P18C_RELOAD_FAST : P18C_RELOAD)
                            .category(GunCategory.PISTOL)
                            .gunProperties(4.0F, 2.5F, 11.0F, 0.05F, 3)
                            .recoil(1.2F, 0.5F)
                            .firerate(1)
                            .firemodes(Firemode.SINGLE, Firemode::singleToAuto)
                            .ammo(AmmoType.AMMO_9MM, (gunItem, stack) -> gunItem.getAttachment(AttachmentCategory.MAGAZINE, stack).isExtended() ? 23 : 18)
                            .reload(ReloadManager.Magazine.instance, 30)
                            .ister(() -> GunRenderer.P18CRenderer::new)
                            .attachments()
                            .barrel(() -> new AttachmentItem.Barrel[] {SUPPRESSOR_SMG})
                            .scope(() -> new AttachmentItem.Scope[]{RED_DOT})
                            .magazine(() -> new AttachmentItem.Magazine[]{QUICKDRAW_SMG, EXTENDED_SMG, QUICKDRAW_EXTENDED_SMG})
                            .build()
                            .animations(() -> () -> AnimationPackP18C::new)
                            .shootingSound(s -> s ? P18C_SHOOT_SILENT : P18C_SHOOT)
                            .shootingVolume(s -> s ? 5.0F : 9.0F)
                            .build("p18c"),
                    new GunBuilder()
                            .reloadSound(q -> q ? DEAGLE_RELOAD_FAST : DEAGLE_RELOAD)
                            .category(GunCategory.PISTOL)
                            .gunProperties(8.0F, 2.5F, 14.0F, 0.05F, 3)
                            .recoil(2.6F, 0.7F)
                            .firerate(4)
                            .firemodes(Firemode.SINGLE, Function.identity())
                            .ammo(AmmoType.AMMO_45ACP, (gunItem, stack) -> gunItem.getAttachment(AttachmentCategory.MAGAZINE, stack).isExtended() ? 12 : 7)
                            .reload(ReloadManager.Magazine.instance, 30)
                            .ister(() -> GunRenderer.DeagleRenderer::new)
                            .attachments()
                            .scope(() -> new AttachmentItem.Scope[]{RED_DOT})
                            .magazine(() -> new AttachmentItem.Magazine[]{QUICKDRAW_SMG, EXTENDED_SMG, QUICKDRAW_EXTENDED_SMG})
                            .build()
                            .animations(() -> () -> AnimationPackDeagle::new)
                            .shootingSound(s -> DEAGLE_SHOOT)
                            .shootingVolume(s -> 9.0F)
                            .build("deagle"),
                    new GunBuilder()
                            .reloadSound(q -> R1895_RELOAD)
                            .category(GunCategory.PISTOL)
                            .gunProperties(8.0F, 2.5F, 17.0F, 0.05F, 3)
                            .recoil(2.0F, 0.8F)
                            .firerate(8)
                            .firemodes(Firemode.SINGLE, Function.identity())
                            .ammo(AmmoType.AMMO_762MM, (gunItem, stack) -> 7)
                            .reload(ReloadManager.Single.instance, 25)
                            .ister(() -> GunRenderer.R1895Renderer::new)
                            .attachments()
                            .barrel(() -> new AttachmentItem.Barrel[] {SUPPRESSOR_SMG})
                            .build()
                            .animations(() -> () -> AnimationPackR1895::new)
                            .shootingSound(s -> s ? R1895_SHOOT_SILENT : R1895_SHOOT)
                            .shootingVolume(s -> s ? 5.0F : 9.0F)
                            .build("r1895"),
                    new GunBuilder()
                            .reloadSound(q -> R45_RELOAD)
                            .category(GunCategory.PISTOL)
                            .gunProperties(7.0F, 2.5F, 14.0F, 0.05F, 3)
                            .recoil(1.6F, 0.75F)
                            .firerate(6)
                            .firemodes(Firemode.SINGLE, Function.identity())
                            .ammo(AmmoType.AMMO_45ACP, (gunItem, stack) -> 6)
                            .reload(ReloadManager.Magazine.instance, 25)
                            .ister(() -> GunRenderer.R45Renderer::new)
                            .attachments()
                            .scope(() -> new AttachmentItem.Scope[] {RED_DOT})
                            .build()
                            .animations(() -> () -> AnimationPackR45::new)
                            .shootingSound(s -> R45_SHOOT)
                            .shootingVolume(s -> 9.0F)
                            .build("r45"),
                    new GunBuilder()
                            .reloadSound(q -> q ? SCORPION_RELOAD_FAST : SCORPION_RELOAD)
                            .category(GunCategory.PISTOL)
                            .gunProperties(3.0F, 2.5F, 11.0F, 0.05F, 2)
                            .recoil(1.2F, 0.35F)
                            .firerate(1)
                            .firemodes(Firemode.SINGLE, Firemode::singleToAuto)
                            .ammo(AmmoType.AMMO_9MM, (gunItem, stack) -> gunItem.getAttachment(AttachmentCategory.MAGAZINE, stack).isExtended() ? 30 : 20)
                            .reload(ReloadManager.Magazine.instance, 35)
                            .ister(() -> GunRenderer.ScorpionRenderer::new)
                            .attachments()
                            .scope(() -> new AttachmentItem.Scope[] {RED_DOT})
                            .grip(() -> new AttachmentItem.Grip[] {VERTICAL_GRIP})
                            .magazine(() -> new AttachmentItem.Magazine[]{QUICKDRAW_SMG, EXTENDED_SMG, QUICKDRAW_EXTENDED_SMG})
                            .build()
                            .animations(() -> () -> AnimationPackScorpion::new)
                            .shootingSound(s -> s ? SCORPION_SHOOT_SILENT : SCORPION_SHOOT)
                            .shootingVolume(s -> s ? 5.0F : 9.0F)
                            .build("scorpion"),
                    new GunBuilder()
                            .reloadSound(q -> SAWED_OFF_RELOAD)
                            .category(GunCategory.SHOTGUN)
                            .gunProperties(3.0F, 2.5F, 11.0F, 0.15F, 3)
                            .recoil(1.5F, 0.7F)
                            .firerate(3)
                            .firemodes(Firemode.SINGLE, Function.identity())
                            .ammo(AmmoType.AMMO_12G, (gunItem, stack) -> 2)
                            .reload(ReloadManager.Magazine.instance, 30)
                            .shoot(ShootManager::handleShotgun)
                            .ister(() -> GunRenderer.SawedOffRenderer::new)
                            .animations(() -> () -> AnimationPackSawedOff::new)
                            .shootingSound(s -> SAWED_OFF_SHOOT)
                            .shootingVolume(s -> 9.0F)
                            .build("sawed_off"),
                    new GunBuilder()
                            .reloadSound(q -> SAWED_OFF_RELOAD)
                            .category(GunCategory.SHOTGUN)
                            .gunProperties(4.0F, 2.5F, 11.0F, 0.15F, 3)
                            .recoil(1.5F, 0.7F)
                            .firerate(15)
                            .firemodes(Firemode.SINGLE, Function.identity())
                            .ammo(AmmoType.AMMO_12G, (item, stack) -> 5)
                            .reload(ReloadManager.Single.instance, 16)
                            .shoot(ShootManager::handleShotgun)
                            .ister(() -> GunRenderer.S1897Renderer::new)
                            .animations(() -> () -> AnimationPackS1897::new)
                            .shootingSound(s -> SAWED_OFF_SHOOT)
                            .shootingVolume(s -> 9.0F)
                            .build("s1897"),
                    new GunBuilder()
                            .reloadSound(q -> SAWED_OFF_RELOAD)
                            .category(GunCategory.SHOTGUN)
                            .gunProperties(4.0F, 2.5F, 11.0F, 0.15F, 3)
                            .recoil(1.5F, 0.7F)
                            .firerate(4)
                            .firemodes(Firemode.SINGLE, Function.identity())
                            .ammo(AmmoType.AMMO_12G, (item, stack) -> 2)
                            .reload(ReloadManager.Magazine.instance, 30)
                            .shoot(ShootManager::handleShotgun)
                            .ister(() -> GunRenderer.S686Renderer::new)
                            .animations(() -> () -> AnimationPackS686::new)
                            .shootingSound(s -> SAWED_OFF_SHOOT)
                            .shootingVolume(s -> 9.0F)
                            .build("s686"),
                    new GunBuilder()
                            .reloadSound(q -> SAWED_OFF_RELOAD)
                            .category(GunCategory.SHOTGUN)
                            .gunProperties(4.0F, 2.5F, 11.0F, 0.15F, 3)
                            .recoil(3.5F, 2.7F)
                            .firerate(4)
                            .firemodes(Firemode.SINGLE, Function.identity())
                            .ammo(AmmoType.AMMO_12G, (item, stack) -> item.getAttachment(AttachmentCategory.MAGAZINE, stack).isExtended() ? 8 : 5)
                            .reload(ReloadManager.Magazine.instance, 35)
                            .shoot(ShootManager::handleShotgun)
                            .ister(() -> GunRenderer.S12KRenderer::new)
                            .attachments()
                            .barrel(() -> new AttachmentItem.Barrel[] {SUPPRESSOR_AR})
                            .scope(() -> new AttachmentItem.Scope[] {RED_DOT, HOLOGRAPHIC, X2_SCOPE, X4_SCOPE})
                            .magazine(() -> new AttachmentItem.Magazine[] {QUICKDRAW_AR, EXTENDED_AR, QUICKDRAW_EXTENDED_AR})
                            .build()
                            .animations(() -> () -> AnimationPackS12K::new)
                            .shootingSound(s -> SAWED_OFF_SHOOT)
                            .shootingVolume(s -> 9.0F)
                            .build("s12k"),
                    new BackpackItem("small_backpack_desert", BackpackSlotItem.BackpackType.SMALL, BackpackItem.Variant.DESERT),
                    new BackpackItem("medium_backpack_desert", BackpackSlotItem.BackpackType.MEDIUM, BackpackItem.Variant.DESERT),
                    new BackpackItem("large_backpack_desert", BackpackSlotItem.BackpackType.LARGE, BackpackItem.Variant.DESERT),
                    new BackpackItem("small_backpack_forest", BackpackSlotItem.BackpackType.SMALL, BackpackItem.Variant.FOREST),
                    new BackpackItem("medium_backpack_forest", BackpackSlotItem.BackpackType.MEDIUM, BackpackItem.Variant.FOREST),
                    new BackpackItem("large_backpack_forest", BackpackSlotItem.BackpackType.LARGE, BackpackItem.Variant.FOREST),
                    new BackpackItem("small_backpack_snow", BackpackSlotItem.BackpackType.SMALL, BackpackItem.Variant.SNOW),
                    new BackpackItem("medium_backpack_snow", BackpackSlotItem.BackpackType.MEDIUM, BackpackItem.Variant.SNOW),
                    new BackpackItem("large_backpack_snow", BackpackSlotItem.BackpackType.LARGE, BackpackItem.Variant.SNOW),
                    new GhillieSuitItem("ghillie_suit"),
                    new PMCItem("grass_plate", new Item.Properties().group(PMCItem.ITEMS))
            );
            blockItemList.stream().filter(Objects::nonNull).forEach(registry::register);
            blockItemList = null;
        }

        @SubscribeEvent
        public static void onBlockRegister(RegistryEvent.Register<Block> event) {
            event.getRegistry().registerAll(
                    new PMCHorizontalBlock("generator", Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(2.5F)),
                    new PMCHorizontalBlock("storage", Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(2.5F)),
                    new PMCHorizontalBlock("interface", Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(2.5F)),
                    new PMCHorizontalBlock("producer", Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(2.5F)),
                    new WeaponFactoryBlock("weapon_factory"),
                    new AmmoFactoryBlock("ammo_factory"),
                    new LootSpawnerBlock("loot_spawner"),
                    new AirdropBlock("airdrop", AirdropTileEntity::new),
                    new AirdropBlock("flare_airdrop", FlareAirdropTileEntity::new),
                    new DeathCrateBlock("death_crate")
            );
        }

        @SubscribeEvent
        public static void onEntityTypeRegister(RegistryEvent.Register<EntityType<?>> event) {
            event.getRegistry().registerAll(
                    registerEntity("parachute", trackBuilder(ParachuteEntity::new, EntityClassification.MISC, 256).size(1.5F, 2.5F)),
                    registerEntity("grenade", trackBuilder(GrenadeEntity::new, EntityClassification.MISC, 32).size(0.2F, 0.2F)),
                    registerEntity("smoke", trackBuilder(SmokeEntity::new, EntityClassification.MISC, 32).size(0.2F, 0.2F)),
                    registerEntity("molotov", trackBuilder(MolotovEntity::new, EntityClassification.MISC, 32).size(0.2F, 0.2F)),
                    registerEntity("flash", trackBuilder(FlashEntity::new, EntityClassification.MISC, 32).size(0.2F, 0.2F)),
                    registerEntity("uaz", trackBuilder(UAZEntity::new, EntityClassification.MISC, 64).size(2.25F, 2.0F)),
                    registerEntity("glider", trackBuilder(GliderEntity::new, EntityClassification.MISC, 64).size(2.5F, 2.0F)),
                    registerEntity("bullet", trackBuilder(BulletEntity::new, EntityClassification.MISC, 64).size(0.1F, 0.1F)),
                    registerEntity("airdrop", trackBuilder(AirdropEntity::new, EntityClassification.MISC, 256).size(1.0F, 1.0F)),
                    registerEntity("bot", builder(BotEntity::new, EntityClassification.MONSTER).setTrackingRange(256).setUpdateInterval(3).setShouldReceiveVelocityUpdates(true).size(0.6F, 1.95F))
            );
        }

        @SubscribeEvent
        public static void onSoundRegister(RegistryEvent.Register<SoundEvent> event) {
            event.getRegistry().registerAll(
                    sound("stun_effect"),
                    sound("stun_effect_short"),
                    sound("p92_shoot"),
                    sound("p92_shoot_silent"),
                    sound("p92_reload"),
                    sound("p92_reload_fast"),
                    sound("p1911_shoot"),
                    sound("p1911_shoot_silent"),
                    sound("p1911_reload"),
                    sound("p1911_reload_fast"),
                    sound("p18c_shoot"),
                    sound("p18c_shoot_silent"),
                    sound("p18c_reload"),
                    sound("p18c_reload_fast"),
                    sound("deagle_shoot"),
                    sound("deagle_reload"),
                    sound("deagle_reload_fast"),
                    sound("sawed_off_shoot"),
                    sound("sawed_off_reload"),
                    sound("uaz_idle"),
                    sound("uaz_accelerate"),
                    sound("uaz_brake"),
                    sound("uaz_cruise"),
                    sound("glider_idle"),
                    sound("glider_accelerate"),
                    sound("glider_brake"),
                    sound("glider_cruise")
            );
        }

        @SubscribeEvent
        public static void onGameRegister(RegistryEvent.Register<GameType<?>> event) {
            event.getRegistry().registerAll(
                    GameType.Builder.create(DeathmatchGame::new)
                            .name("deathmatch")
                            .addArgument(map -> map.putInt(ArgumentProvider.DURATION, 12000))
                            .description(
                                    new StringTextComponent(TextFormatting.YELLOW + "Standart deathmacth game. Get the highest score"),
                                    new StringTextComponent(TextFormatting.YELLOW + "Supported arguments:"),
                                    new StringTextComponent(UsefulFunctions.addArgumentDesc(ArgumentProvider.DURATION, "Set match length", "int"))
                            )
                            .build().setRegistryName("deathmatch"),
                    GameType.Builder.create(BattleRoyaleGame::new)
                            .name("battle_royale")
                            .addArgument(map -> map.putInt(ArgumentProvider.TEAM_SIZE, 1))
                            .addArgument(map -> map.putIntArray(ArgumentProvider.ZONE_SHRINK_TIMES, 200, 2000, 1800, 1500, 1200, 1000, 800, 600))
                            .addArgument(map -> map.putInt(ArgumentProvider.AIRDROP_AMOUNT, 8))
                            .addArgument(map -> map.putBoolean(ArgumentProvider.CENTERED, false))
                            .addArgument(map -> map.putDouble(ArgumentProvider.ZONE_MODIFIER, 1.5D))
                            .description(
                                    new StringTextComponent(TextFormatting.YELLOW + "Battle royale mode. Be the last one standing"),
                                    new StringTextComponent(TextFormatting.YELLOW + "Supported arguments:"),
                                    new StringTextComponent(UsefulFunctions.addArgumentDesc(ArgumentProvider.TEAM_SIZE, "Team size", "int")),
                                    new StringTextComponent(UsefulFunctions.addArgumentDesc(ArgumentProvider.ZONE_SHRINK_TIMES, "Set zone shrink delays", "int[8]")),
                                    new StringTextComponent(UsefulFunctions.addArgumentDesc(ArgumentProvider.AIRDROP_AMOUNT, "Amount of airdrops which will spawn", "int")),
                                    new StringTextComponent(UsefulFunctions.addArgumentDesc(ArgumentProvider.CENTERED, "Zones always in center", "boolean")),
                                    new StringTextComponent(UsefulFunctions.addArgumentDesc(ArgumentProvider.ZONE_MODIFIER, "Zone scale modifier", "double"))
                            )
                            .build().setRegistryName("battle_royale")
            );
        }

        @SuppressWarnings("unchecked")
        @SubscribeEvent
        public static void createRegistries(RegistryEvent.NewRegistry event) {
            newRegistry(Pubgmc.makeResource("games"), GameType.class).create();
            GAMES = RegistryManager.ACTIVE.getRegistry(GameType.class);
        }

        private static <V extends IForgeRegistryEntry<V>> RegistryBuilder<V> newRegistry(ResourceLocation location, Class<V> type) {
            return new RegistryBuilder<V>().setName(location).setType(type).setMaxID(Integer.MAX_VALUE - 1);
        }

        private static SoundEvent sound(String key) {
            ResourceLocation location = Pubgmc.makeResource(key);
            return new SoundEvent(location).setRegistryName(location);
        }

        private static <T extends Entity> EntityType<T> registerEntity(String name, EntityType.Builder<T> builder) {
            EntityType<T> type = builder.build(Pubgmc.MODID + ":" + name);
            type.setRegistryName(Pubgmc.makeResource(name));
            return type;
        }

        private static <T extends Entity> EntityType.Builder<T> builder(EntityType.IFactory<T> factory, EntityClassification classification) {
            return EntityType.Builder.create(factory, classification);
        }

        private static <T extends Entity> EntityType.Builder<T> trackBuilder(EntityType.IFactory<T> factory, EntityClassification classification, int range) {
            return EntityType.Builder.create(factory, classification).setTrackingRange(range).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true);
        }
    }

    @Mod.EventBusSubscriber(modid = Pubgmc.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientHandler {

        @SubscribeEvent
        public static void bake(ModelBakeEvent event) {
            Map<ResourceLocation, IBakedModel> registry = event.getModelRegistry();
            IBakedModel model = new DummyBakedModel();
            registry.put(get(PMCItems.SPAWN_UAZ), model);
            registry.put(get(PMCItems.SPAWN_GLIDER), model);

            DummyGunBakedModel gunBakedModel = new DummyGunBakedModel();
            ForgeRegistries.ITEMS.getValues()
                    .stream()
                    .filter(item -> item instanceof AbstractGunItem)
                    .forEach(item -> registry.put(get(item), gunBakedModel));
        }

        public static ModelResourceLocation get(Item item) {
            // debug purposes
            return new ModelResourceLocation(item == null ? new ResourceLocation("unknown", "null") : item.getRegistryName(), "inventory");
        }
    }
}
