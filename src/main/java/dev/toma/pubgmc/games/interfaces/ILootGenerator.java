package dev.toma.pubgmc.games.interfaces;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.common.item.gun.AmmoType;
import dev.toma.pubgmc.common.item.gun.GunItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

public interface ILootGenerator extends ITileLoadHandler {

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
     * Override method provided by {@link ITileLoadHandler} to automatically update
     * game ID and generate loot
     */
    @Override
    default void load(IKeyHolder key) {
        if(!test(key)) { // if ids are different
            generateLoot(); // generate loot content
            setGameID(key.getGameID()); // set new id to prevent duplicate loot gen calls in same game
        }
    }

    /**
     * Generates ammo for weapons based on their {@link AmmoType}
     * @param stack The stack which has been generated
     * @param index Current index in inventory
     * @param inventory Inventory holder
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
