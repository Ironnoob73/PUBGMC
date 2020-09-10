package dev.toma.pubgmc.init;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.common.container.*;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class PMCContainers {
    public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = new DeferredRegister<>(ForgeRegistries.CONTAINERS, Pubgmc.MODID);
    //public static final RegistryObject<ContainerType<WeaponFactoryContainer>> WEAPON_FACTORY = register("name", WeaponFactoryContainer::new);
    public static final RegistryObject<ContainerType<LootSpawnerContainer>> LOOT_SPAWNER = CONTAINER_TYPES.register("loot_spawner", () -> IForgeContainerType.create(LootSpawnerContainer::new));
    public static final RegistryObject<ContainerType<AirdropContainer>> AIRDROP = CONTAINER_TYPES.register("airdrop", () -> IForgeContainerType.create(AirdropContainer::new));
    public static final RegistryObject<ContainerType<FlareAirdropContainer>> FLARE_AIRDROP = CONTAINER_TYPES.register("flare_airdrop", () -> IForgeContainerType.create(FlareAirdropContainer::new));
    public static final RegistryObject<ContainerType<PMCPlayerContainer>> PLAYER_CONTAINER = CONTAINER_TYPES.register("player_container", () -> IForgeContainerType.create(PMCPlayerContainer::new));
    public static final RegistryObject<ContainerType<AttachmentContainer>> ATTACHMENT_CONTAINER = CONTAINER_TYPES.register("attachment_container", () -> IForgeContainerType.create(AttachmentContainer::new));
}
