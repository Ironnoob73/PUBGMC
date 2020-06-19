package dev.toma.pubgmc.network.packet;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.network.NetworkPacket;
import dev.toma.pubgmc.util.recipe.PMCRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class CPacketSendRecipes implements NetworkPacket<CPacketSendRecipes> {

    private Map<String, List<PMCRecipe>> recipeMap;

    public CPacketSendRecipes() {

    }

    public CPacketSendRecipes(Map<String, List<PMCRecipe>> recipeMap) {
        this.recipeMap = recipeMap;
    }

    @Override
    public void encode(CPacketSendRecipes instance, PacketBuffer buf) {
        buf.writeVarInt(instance.recipeMap.size());
        for(String string : instance.recipeMap.keySet()) {
            buf.writeString(string);
            List<PMCRecipe> list = instance.recipeMap.get(string);
            int j = list.size();
            buf.writeVarInt(j);
            for(int i = 0; i < j; i++) {
                PMCRecipe recipe = list.get(i);
                buf.writeItemStack(recipe.getRaw());
                buf.writeVarInt(recipe.ingredientList().size());
                for(int k = 0; k < recipe.ingredientList().size(); k++) {
                    buf.writeItemStack(recipe.ingredientList().get(k));
                }
            }
        }
    }

    @Override
    public CPacketSendRecipes decode(PacketBuffer buf) {
        Map<String, List<PMCRecipe>> recipeMap = new HashMap<>();
        int keys = buf.readVarInt();
        for(int i = 0; i < keys; i++) {
            String key = buf.readString();
            int j = buf.readVarInt();
            List<PMCRecipe> list = new ArrayList<>(j);
            for(int k = 0; k < j; k++) {
                ItemStack res = buf.readItemStack();
                int l = buf.readVarInt();
                List<ItemStack> ing = new ArrayList<>();
                for(int m = 0; m < l; m++) {
                    ing.add(buf.readItemStack());
                }
                list.add(new PMCRecipe(res, ing));
            }
            recipeMap.put(key, list);
        }
        return new CPacketSendRecipes(recipeMap);
    }

    @Override
    public void handle(CPacketSendRecipes instance, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> Pubgmc.recipeManager.recipeMap = instance.recipeMap);
        ctx.get().setPacketHandled(true);
    }
}
