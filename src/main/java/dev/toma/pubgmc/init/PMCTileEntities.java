package dev.toma.pubgmc.init;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.common.tileentity.AirdropTileEntity;
import dev.toma.pubgmc.common.tileentity.LootSpawnerTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class PMCTileEntities {
    public static final DeferredRegister<TileEntityType<?>> TE_TYPES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, Pubgmc.MODID);
    public static final RegistryObject<TileEntityType<LootSpawnerTileEntity>> LOOT_SPAWNER = TE_TYPES.register("loot_spawner", () -> TileEntityType.Builder.create(LootSpawnerTileEntity::new, PMCBlocks.LOOT_SPAWNER).build(null));
    public static final RegistryObject<TileEntityType<AirdropTileEntity>> AIRDROP = TE_TYPES.register("airdrop", () -> TileEntityType.Builder.create(AirdropTileEntity::new, PMCBlocks.AIRDROP).build(null));
}
