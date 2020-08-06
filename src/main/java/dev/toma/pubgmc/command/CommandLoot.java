package dev.toma.pubgmc.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import dev.toma.pubgmc.common.tileentity.LootSpawnerTileEntity;
import dev.toma.pubgmc.data.loot.LootManager;
import dev.toma.pubgmc.data.loot.LootTable;
import dev.toma.pubgmc.data.loot.LootTableConstants;
import dev.toma.pubgmc.network.NetworkManager;
import dev.toma.pubgmc.network.packet.CPacketDisplayLootSpawners;
import net.minecraft.block.BlockState;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import java.util.stream.Collectors;

public class CommandLoot {

    private static final SimpleCommandExceptionType SPECIFY_ACTION = new SimpleCommandExceptionType(new StringTextComponent("Unspecified action! Use /loot [generate, clear]"));
    private static final SimpleCommandExceptionType INVALID_SENDER = new SimpleCommandExceptionType(new StringTextComponent("Only player can execute this command!"));

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                Commands.literal("loot")
                        .requires(src -> src.hasPermissionLevel(4))
                        .then(
                                Commands.literal("generate")
                                        .executes(CommandLoot::executeGen)
                        )
                        .then(
                                Commands.literal("clear")
                                        .executes(CommandLoot::executeClear)
                        )
                        .then(
                                Commands.literal("show")
                                        .executes(CommandLoot::executeShow)
                        )
                        .then(
                                Commands.literal("destroy")
                                        .executes(CommandLoot::executeDestroy)
                        )
                        .executes(CommandLoot::executeClear)
                .executes(CommandLoot::executeEmpty)
        );
    }

    private static int executeDestroy(CommandContext<CommandSource> ctx) {
        World world = ctx.getSource().func_197023_e();
        for (LootSpawnerTileEntity tileEntity : world.loadedTileEntityList.stream()
                .filter(te -> te instanceof LootSpawnerTileEntity)
                .map(te -> (LootSpawnerTileEntity) te)
                .collect(Collectors.toList())) {
            BlockPos pos = tileEntity.getPos();
            world.destroyBlock(pos, false);
        }
        ctx.getSource().sendFeedback(new StringTextComponent("All loaded lootspawners have been removed"), false);
        return 0;
    }

    private static int executeGen(CommandContext<CommandSource> ctx) {
        World world = ctx.getSource().func_197023_e();
        LootTable table = LootManager.getLootTable(LootTableConstants.LOOT_BLOCK);
        world.loadedTileEntityList.stream()
                .filter(te -> te instanceof LootSpawnerTileEntity)
                .map(te -> (LootSpawnerTileEntity) te)
                .forEach(te -> {
                    BlockState state = world.getBlockState(te.getPos());
                    te.genLoot(table);
                    world.notifyBlockUpdate(te.getPos(), state, state, 3);
                });
        ctx.getSource().sendFeedback(new StringTextComponent("Loot has been generated"), false);
        return 0;
    }

    private static int executeClear(CommandContext<CommandSource> ctx) {
        World world = ctx.getSource().func_197023_e();
        world.loadedTileEntityList.stream()
                .filter(te -> te instanceof LootSpawnerTileEntity)
                .map(te -> (LootSpawnerTileEntity) te)
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
