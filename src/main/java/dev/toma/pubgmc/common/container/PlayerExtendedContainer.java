package dev.toma.pubgmc.common.container;

import dev.toma.pubgmc.common.inventory.PlayerExtendedInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.PlayerContainer;

public class PlayerExtendedContainer extends PlayerContainer {

    public PlayerExtendedContainer(PlayerExtendedInventory inventory, boolean server, PlayerEntity player) {
        super(inventory, server, player);
    }
}
