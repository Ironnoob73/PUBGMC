package dev.toma.pubgmc.games.interfaces;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.common.item.gun.AmmoType;
import dev.toma.pubgmc.common.item.gun.GunItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

public interface LootGenerator extends ChunkLoadListener {

    /**
     * Method for loot generation
     */
    void generateLoot();

    /**
     * Called when someone executes the loot generate command
     */
    default void onExecuteLootGenCommand() {
        this.generateLoot();
    }

    /**
     * @return whether this holder should be destroyed when destruction command is executed
     */
    default boolean shouldDestroyByCommand() {
        return true;
    }

    /**
     * Override method provided by {@link ChunkLoadListener} to automatically update
     * game ID and generate loot
     *
     * @param chunk - chunk which has been loaded
     * @param world - the world where chunk is at
     */
    @Override
    default void loadChunk(IChunk chunk, World world) {
        long id = -1; // todo get actual game id
        if(!test(id)) { // if ids are different
            generateLoot(); // generate loot content
            setGameID(id); // set new id to prevent duplicate loot gen calls in same game
        }
    }

    /**
     * Generates ammo for weapons based on their {@link AmmoType}
     * @param stack - the stack which has been generated
     * @param index - current index in inventory
     * @param inventory - inventory holder
     * @return index of last item in inventory
     */
    default int postItemGenerated(ItemStack stack, int index, LazyOptional<? extends IItemHandler> inventory) {
        if(stack.getItem() instanceof GunItem && inventory.isPresent()) {
            IItemHandler handler = inventory.orElseThrow(NullPointerException::new);
            GunItem gun = (GunItem) stack.getItem();
            AmmoType ammoType = gun.getAmmoType();
            int ammoBoxes = 1 + Pubgmc.rand.nextInt(3);
            for(int a = 0; a < ammoBoxes; a++) {
                if(index >= handler.getSlots()) break;
                handler.insertItem(index, ammoType.generateAmmoDrop(), false);
                ++index;
            }
        }
        return index;
    }
}
