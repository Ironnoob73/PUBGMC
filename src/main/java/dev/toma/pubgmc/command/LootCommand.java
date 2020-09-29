package dev.toma.pubgmc.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import dev.toma.pubgmc.common.tileentity.AbstractInventoryTileEntity;
import dev.toma.pubgmc.data.loot.LootManager;
import dev.toma.pubgmc.data.loot.LootTable;
import dev.toma.pubgmc.data.loot.LootTableConstants;
import dev.toma.pubgmc.games.interfaces.LootGenerator;
import dev.toma.pubgmc.network.NetworkManager;
import dev.toma.pubgmc.network.packet.CPacketDisplayLootSpawners;
import net.minecraft.block.BlockState;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import java.util.stream.Collectors;

public class LootCommand {

    private static final SimpleCommandExceptionType SPECIFY_ACTION = new SimpleCommandExceptionType(new StringTextComponent("Unspecified action! Use /loot [generate, clear]"));
    private static final SimpleCommandExceptionType INVALID_SENDER = new SimpleCommandExceptionType(new StringTextComponent("Only player can execute this command!"));

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                Commands.literal("loot")
                        .requires(src -> src.hasPermissionLevel(4))
                        .then(
                                Commands.literal("generate")
                                        .executes(LootCommand::executeGen)
                        )
                        .then(
                                Commands.literal("clear")
                                        .executes(LootCommand::executeClear)
                        )
                        .then(
                                Commands.literal("show")
                                        .executes(LootCommand::executeShow)
                        )
                        .then(
                                Commands.literal("destroy")
                                        .executes(LootCommand::executeDestroy)
                        )
                        .executes(LootCommand::executeClear)
                .executes(LootCommand::executeEmpty)
        );
    }

    private static <T extends TileEntity & LootGenerator> int executeDestroy(CommandContext<CommandSource> ctx) {
        World world = ctx.getSource().getWorld();
        for (T tileEntity : world.loadedTileEntityList.stream()
                .filter(te -> te instanceof LootGenerator && ((LootGenerator) te).shouldDestroyByCommand())
                .map(te -> (T) te)
                .collect(Collectors.toList())) {
            BlockPos pos = tileEntity.getPos();
            world.destroyBlock(pos, false);
        }
        ctx.getSource().sendFeedback(new StringTextComponent("All loaded lootspawners have been removed"), false);
        return 0;
    }

    private static <T extends TileEntity & LootGenerator> int executeGen(CommandContext<CommandSource> ctx) {
        World world = ctx.getSource().getWorld();
        LootTable table = LootManager.getLootTable(LootTableConstants.LOOT_BLOCK);
        world.loadedTileEntityList.stream()
                .filter(te -> te instanceof LootGenerator)
                .map(te -> (T) te)
                .forEach(te -> {
                    BlockState state = world.getBlockState(te.getPos());
                    te.generateLoot();
                    world.notifyBlockUpdate(te.getPos(), state, state, 3);
                });
        ctx.getSource().sendFeedback(new StringTextComponent("Loot has been generated"), false);
        return 0;
    }

    private static <T extends AbstractInventoryTileEntity & LootGenerator> int executeClear(CommandContext<CommandSource> ctx) {
        World world = ctx.getSource().getWorld();
        world.loadedTileEntityList.stream()
                .filter(te -> te instanceof AbstractInventoryTileEntity && te instanceof LootGenerator)
                .map(te -> (T) te)
                .forEach(te -> {
                    BlockState state = world.getBlockState(te.getPos());
                    te.clearInventory();
                    world.notifyBlockUpdate(te.getPos(), state, state, 3);
                });
        ctx.getSource().sendFeedback(new StringTextComponent("Loot has been deleted from loaded tile entities"), false);
        return 0;
    }

    private static int executeShow(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        Entity entity = ctx.getSource().getEntity();
        if (!(entity instanceof ServerPlayerEntity)) {
            throw INVALID_SENDER.create();
        }
        NetworkManager.sendToClient((ServerPlayerEntity) entity, new CPacketDisplayLootSpawners());
        return 0;
    }

    private static int executeEmpty(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        throw SPECIFY_ACTION.create();
    }
}
