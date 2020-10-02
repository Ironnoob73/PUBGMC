package dev.toma.pubgmc.games;

import dev.toma.pubgmc.games.args.ArgumentMap;
import dev.toma.pubgmc.games.args.GameArgument;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.function.Consumer;

public class GameType<G extends Game> extends ForgeRegistryEntry<GameType<?>> {

    private final GameInstanceFactory<G> factory;
    private final ITextComponent name;
    private final ITextComponent[] description;
    private final ArgumentMap argumentMap;

    private GameType(Builder<G> builder) {
        this.factory = builder.factory;
        this.name = builder.name;
        this.argumentMap = builder.map;
        this.description = builder.description;
    }

    /**
     * @return Component for display in various places
     */
    public ITextComponent getName() {
        return name;
    }

    public ITextComponent[] getDescription() {
        return description;
    }

    /**
     * Creates new plain {@link Game} instance
     * @param world World where this game is running
     * @return new {@link G} instance
     */
    public G newGame(World world) {
        return factory.create(world);
    }

    public boolean hasArguments() {
        return argumentMap != null && !argumentMap.isEmpty();
    }

    public ArgumentMap getGameArguments() {
        return argumentMap;
    }

    /**
     * Handles creation of new game instances
     * when new game is started or when game is
     * being recreated from NBT.
     * <p>Basically just {@link java.util.function.Function} with <code>T</code> parameter of {@link World} and <code>R</code> parameter of {@link G}
     * @param <G> Type of {@link Game}
     */
    @FunctionalInterface
    public interface GameInstanceFactory<G> {
        G create(World world);
    }

    /**
     * Handles creation of new {@link GameType}s
     * @param <G> Type of {@link Game}
     */
    public static class Builder<G extends Game> {

        final GameInstanceFactory<G> factory;
        ITextComponent name;
        ITextComponent[] description;
        ArgumentMap map;

        /**
         * Private constructor, use {@link Builder#create(GameInstanceFactory)} instead
         * @param factory Instance factory for {@link G} object
         */
        private Builder(GameInstanceFactory<G> factory) {
            this.factory = factory;
        }

        /**
         * Creates new {@link Builder} instance
         * @param factory Instance factory for {@link G} object
         * @param <G> Type of {@link Game}
         * @return new {@link Builder} instance
         */
        public static <G extends Game> Builder<G> create(GameInstanceFactory<G> factory) {
            return new Builder<>(factory);
        }

        /**
         * Sets display name
         * @param component Name component
         */
        public Builder<G> name(ITextComponent component) {
            this.name = component;
            return this;
        }

        /**
         * Adds description to this game
         * @param components - description lines
         */
        public Builder<G> description(ITextComponent... components) {
            this.description = components;
            return this;
        }

        /**
         * Sets display name
         * @param name Name substring - complete name is going to be {@link TranslationTextComponent} in format <code>game.yourStringHere</code>
         */
        public Builder<G> name(String name) {
            return name(new TranslationTextComponent(String.format("game.%s", name)));
        }

        /**
         * Registers new argument for this game
         * @param mapConsumer - use this to register your argument ({@link ArgumentMap#put(String, GameArgument)})
         */
        public Builder<G> addArgument(Consumer<ArgumentMap> mapConsumer) {
            if(map == null) {
                map = new ArgumentMap();
            }
            mapConsumer.accept(map);
            return this;
        }

        /**
         * @return new {@link GameType} object with parameters of this {@link Builder}
         */
        public GameType<G> build() {
            return new GameType<>(this);
        }
    }
}
