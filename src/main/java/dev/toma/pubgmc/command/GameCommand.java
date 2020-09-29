package dev.toma.pubgmc.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import dev.toma.pubgmc.RegistryHandler;
import dev.toma.pubgmc.capability.IWorldCap;
import dev.toma.pubgmc.capability.WorldDataFactory;
import dev.toma.pubgmc.games.Game;
import dev.toma.pubgmc.games.GameType;
import dev.toma.pubgmc.games.util.Area;
import dev.toma.pubgmc.games.util.GameStorage;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.command.arguments.NBTCompoundTagArgument;
import net.minecraft.command.arguments.ResourceLocationArgument;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class GameCommand {

    static final SimpleCommandExceptionType INVALID_GAME = new SimpleCommandExceptionType(new StringTextComponent("Invalid game ID!"));
    static final SimpleCommandExceptionType GAME_RUNNING = new SimpleCommandExceptionType(new StringTextComponent("Game is already runnning!"));
    static final SimpleCommandExceptionType GAME_NOT_RUNNING = new SimpleCommandExceptionType(new StringTextComponent("There's no active game!"));
    static final SimpleCommandExceptionType MAP_SETUP_INCOMPLETE = new SimpleCommandExceptionType(new StringTextComponent("You must setup map first! /game setup map <pos> <size>"));
    static final SimpleCommandExceptionType LOBBY_SETUP_INCOMPLETE = new SimpleCommandExceptionType(new StringTextComponent("You must setup lobby first! /game setup lobby <pos> [size]"));
    static final DynamicCommandExceptionType CANNOT_START = new DynamicCommandExceptionType(o -> new StringTextComponent("Something went wrong during game start cycle: " + o));
    static final SuggestionProvider<CommandSource> GAME_SUGGESTIONS = (context, builder) -> ISuggestionProvider.func_212476_a(RegistryHandler.GAMES.getValues().stream().map(IForgeRegistryEntry::getRegistryName), builder);

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                Commands.literal("game")
                        .requires(src -> src.hasPermissionLevel(4))
                        // game <type>
                        .then(
                                Commands.argument("gameType", ResourceLocationArgument.resourceLocation())
                                        .suggests(GAME_SUGGESTIONS)
                                        .executes(GameCommand::game_type_exec)
                                        // game <type> start
                                        .then(
                                                Commands.literal("start")
                                                        .executes(ctx -> startExec(ctx, ResourceLocationArgument.getResourceLocation(ctx, "gameType")))
                                                        // game <type> start [data]
                                                        .then(
                                                                Commands.argument("data", NBTCompoundTagArgument.nbt())
                                                                        .executes(ctx -> startExec(ctx, ResourceLocationArgument.getResourceLocation(ctx, "gameType"), NBTCompoundTagArgument.getNbt(ctx, "data")))
                                                        )
                                        )
                                        // game <type> info
                                        .then(
                                                Commands.literal("info")
                                                        .executes(ctx -> gameInfoExec(ctx, ResourceLocationArgument.getResourceLocation(ctx, "gameType")))
                                        )
                        )
                        // game stop
                        .then(
                                Commands.literal("stop")
                                        .executes(GameCommand::stopExec)
                        )
                        // game setup
                        .then(
                                Commands.literal("setup")
                                        .executes(GameCommand::setupEmptyExec)
                                        // game setup lobby
                                        .then(
                                                Commands.literal("lobby")
                                                        .executes(GameCommand::lobbyEmptyExec)
                                                        // game setup lobby <pos>
                                                        .then(
                                                                Commands.argument("location", BlockPosArgument.blockPos())
                                                                        .executes(ctx -> lobbySetupExec(ctx, BlockPosArgument.getBlockPos(ctx, "location"), 5))
                                                                        // game setup lobby <pos> [radius]
                                                                        .then(
                                                                                Commands.argument("radius", IntegerArgumentType.integer(1, 30))
                                                                                        .executes(ctx -> lobbySetupExec(ctx, BlockPosArgument.getBlockPos(ctx, "location"), IntegerArgumentType.getInteger(ctx, "radius")))
                                                                        )
                                                        )
                                        )
                                        // game setup map
                                        .then(
                                                Commands.literal("map")
                                                        .executes(GameCommand::mapEmptyExec)
                                                        // game setup map <pos>
                                                        .then(
                                                                Commands.argument("center", BlockPosArgument.blockPos())
                                                                        .executes(GameCommand::mapEmptyExec)
                                                                        // game setup map <pos> <size>
                                                                        .then(
                                                                                Commands.argument("size", IntegerArgumentType.integer(10))
                                                                                        .executes(context -> mapSetupExec(context, BlockPosArgument.getBlockPos(context, "center"), IntegerArgumentType.getInteger(context, "size")))
                                                                        )
                                                        )
                                        )
                        )
        );
    }

    private static int game_type_exec(CommandContext<CommandSource> context) {
        context.getSource().sendFeedback(new StringTextComponent(TextFormatting.RED + "Usage: /game <type> [start, info]"), false);
        return 0;
    }

    private static int startExec(CommandContext<CommandSource> context, ResourceLocation type) throws CommandSyntaxException {
        return startExec(context, type, new CompoundNBT());
    }

    private static GameType<?> getGameType(ResourceLocation location) throws CommandSyntaxException {
        GameType<?> type = RegistryHandler.GAMES.getValue(location);
        if(type == null)
            throw INVALID_GAME.create();
        return type;
    }

    private static int startExec(CommandContext<CommandSource> context, ResourceLocation location, CompoundNBT nbt) throws CommandSyntaxException {
        GameType<?> type = getGameType(location);
        IWorldCap cap = WorldDataFactory.getData(context.getSource().getWorld());
        Game game = cap.getGame();
        GameStorage storage = cap.getStorage();
        if(!storage.getLobby().isValid()) {
            throw LOBBY_SETUP_INCOMPLETE.create();
        }
        if(!storage.getArena().isValid()) {
            throw MAP_SETUP_INCOMPLETE.create();
        }
        if(game != null && game.isRunning()) {
            throw GAME_RUNNING.create();
        }
        Game newGame = type.newGame(context.getSource().getWorld());
        try {
            newGame.exec_GameStart(nbt);
            cap.setGame(newGame);
            context.getSource().sendFeedback(new StringTextComponent(TextFormatting.GREEN + "Game started"), false);
        } catch (Exception exception) {
            throw CANNOT_START.create(exception);
        }
        return 0;
    }

    private static int gameInfoExec(CommandContext<CommandSource> context, ResourceLocation location) throws CommandSyntaxException {
        GameType<?> type = getGameType(location);
        CommandSource src = context.getSource();
        src.sendFeedback(new StringTextComponent(String.format("%s%s information", TextFormatting.YELLOW, type.getName().getFormattedText())), false);
        ITextComponent[] lines = type.getDescription();
        if(lines == null || lines.length == 0) {
            src.sendFeedback(new StringTextComponent(TextFormatting.RED + "Missing description"), false);
        } else {
            for(ITextComponent component : lines) {
                src.sendFeedback(component, false);
            }
        }
        return 0;
    }

    private static int stopExec(CommandContext<CommandSource> context) throws CommandSyntaxException {
        IWorldCap cap = WorldDataFactory.getData(context.getSource().getWorld());
        Game game = cap.getGame();
        if(game == null || !game.isRunning()) {
            throw GAME_NOT_RUNNING.create();
        }
        game.exec_GameStop();
        cap.setGame(null);
        context.getSource().sendFeedback(new StringTextComponent(TextFormatting.GREEN + "Game stopped"), false);
        return 0;
    }

    private static int setupEmptyExec(CommandContext<CommandSource> context) {
        context.getSource().sendFeedback(new StringTextComponent(TextFormatting.RED + "Usage: /game setup [map, lobby] ..."), false);
        return 0;
    }

    private static int lobbyEmptyExec(CommandContext<CommandSource> context) {
        context.getSource().sendFeedback(new StringTextComponent(TextFormatting.RED + "You must define lobby position and radius"), false);
        return 0;
    }

    private static int lobbySetupExec(CommandContext<CommandSource> context, BlockPos pos, int rad) {
        rad = Math.max(rad, 1);
        GameStorage storage = WorldDataFactory.getData(context.getSource().getWorld()).getStorage();
        storage.setLobby(new Area(pos, rad));
        context.getSource().sendFeedback(new StringTextComponent(String.format("%sCreated new lobby at %d,%d,%d with size of %s", TextFormatting.GREEN, pos.getX(), pos.getY(), pos.getZ(), rad)), false);
        return 0;
    }

    private static int mapEmptyExec(CommandContext<CommandSource> context) {
        context.getSource().sendFeedback(new StringTextComponent(TextFormatting.RED + "You must define map center and map size"), false);
        return 0;
    }

    private static int mapSetupExec(CommandContext<CommandSource> context, BlockPos pos, int size) {
        if(size == 0) {
            context.getSource().sendFeedback(new StringTextComponent(TextFormatting.RED + "You must define map size"), false);
            return 0;
        }
        size = Math.max(size, 10);
        GameStorage storage = WorldDataFactory.getData(context.getSource().getWorld()).getStorage();
        storage.setArena(new Area(pos, size));
        context.getSource().sendFeedback(new StringTextComponent(String.format("%sMap created. Center: %d,%d,%d; Size: %d", TextFormatting.GREEN, pos.getX(), pos.getY(), pos.getZ(), size)), false);
        return 0;
    }
}
