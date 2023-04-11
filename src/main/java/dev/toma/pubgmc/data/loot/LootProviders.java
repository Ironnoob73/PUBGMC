package dev.toma.pubgmc.data.loot;

import dev.toma.pubgmc.Pubgmc;
import net.minecraft.util.ResourceLocation;

public final class LootProviders {

    public static final LootProviderType<ItemStackLootProvider> ITEM = create("item", new ItemStackLootProvider.Serializer());
    public static final LootProviderType<RandomChanceLootProvider> RANDOM_CHANCE = create("random_chance", new RandomChanceLootProvider.Serializer());
    public static final LootProviderType<CountLootProvider> COUNT = create("count", new CountLootProvider.Serializer());
    public static final LootProviderType<MultiValueLootProvider> MULTI_VALUE = create("multi", new MultiValueLootProvider.Serializer());
    public static final LootProviderType<RandomLootProvider> RANDOM_ITEM = create("random_item", new RandomLootProvider.Serializer());
    public static final LootProviderType<WeightedLootProvider> WEIGHTED_ITEM = create("weighted_item", new WeightedLootProvider.Serializer());

    public static void registerLootProviders() {
        register(ITEM);
        register(RANDOM_CHANCE);
        register(COUNT);
        register(MULTI_VALUE);
        register(RANDOM_ITEM);
        register(WEIGHTED_ITEM);
    }

    private static void register(LootProviderType<?> type) {
        Pubgmc.LOOT_PROVIDER_TYPE_REGISTRY.register(type);
    }

    private static <P extends LootProvider> LootProviderType<P> create(String id, LootProviderSerializer<P> serializer) {
        ResourceLocation key = Pubgmc.getResource(id);
        return LootProviderType.create(key, serializer);
    }
}