package dev.toma.pubgmc.network.packet;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.common.tileentity.AbstractFactoryTileEntity;
import dev.toma.pubgmc.network.NetworkPacket;
import dev.toma.pubgmc.data.recipe.PMCRecipe;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.function.Supplier;

public class SPacketCraft implements NetworkPacket<SPacketCraft> {

    final BlockPos pos;
    final PMCRecipe recipe;

    public SPacketCraft() {
        this(null, null);
    }

    public SPacketCraft(BlockPos pos, PMCRecipe recipe) {
        this.pos = pos;
        this.recipe = recipe;
    }

    @Override
    public void encode(SPacketCraft instance, PacketBuffer buf) {
        buf.writeInt(instance.pos.getX());
        buf.writeInt(instance.pos.getY());
        buf.writeInt(instance.pos.getZ());
        buf.writeResourceLocation(instance.recipe.getRaw().getItem().getRegistryName());
    }

    @Override
    public SPacketCraft decode(PacketBuffer buf) {
        BlockPos pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        ResourceLocation location = buf.readResourceLocation();
        Item item = ForgeRegistries.ITEMS.getValue(location);
        PMCRecipe pmcRecipe = Pubgmc.getRecipeManager().getAmmoRecipes().get(0);
        main:
        for (List<PMCRecipe> list : Pubgmc.getRecipeManager().recipeMap.values()) {
            for (PMCRecipe recipe : list) {
                if(recipe.getRaw().getItem() == item) {
                    pmcRecipe = recipe;
                    break main;
                }
            }
        }
        return new SPacketCraft(pos, pmcRecipe);
    }

    @Override
    public void handle(SPacketCraft instance, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            ServerWorld world = player.getServerWorld();
            BlockPos pos = instance.pos;
            if(!world.isBlockLoaded(pos)) {
                return;
            }
            TileEntity tileEntity = world.getTileEntity(pos);
            if(tileEntity instanceof AbstractFactoryTileEntity) {
                tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(inventory -> {
                    PMCRecipe recipe = instance.recipe;
                    List<ItemStack> ingredients = recipe.ingredientList();
                    boolean canCraft = true;
                    for (ItemStack stack : ingredients) {
                        Item item = stack.getItem();
                        int count = 0;
                        for (int i = 0; i < 8; i++) {
                            ItemStack stack1 = inventory.getStackInSlot(i);
                            if(!stack1.isEmpty()) {
                                if(stack1.getItem() == item) {
                                    count += stack1.getCount();
                                    if(count >= stack.getCount()) {
                                        break;
                                    }
                                }
                            }
                        }
                        if(count < stack.getCount()) {
                            canCraft = false;
                            break;
                        }
                    }
                    if(canCraft) {
                        ((IItemHandlerModifiable) inventory).setStackInSlot(8, recipe.craft());
                        for (ItemStack stack : ingredients) {
                            int left = stack.getCount();
                            for (int i = 0; i < 8; i++) {
                                ItemStack stack1 = inventory.getStackInSlot(i);
                                if(stack.getItem() == stack1.getItem()) {
                                    if(stack1.getCount() >= left) {
                                        stack1.shrink(left);
                                        left = 0;
                                    } else {
                                        left -= stack1.getCount();
                                        stack1.setCount(0);
                                    }
                                }
                                if(left == 0) {
                                    break;
                                }
                            }
                        }
                    }
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
