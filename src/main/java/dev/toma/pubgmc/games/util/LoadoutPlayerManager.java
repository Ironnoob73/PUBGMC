package dev.toma.pubgmc.games.util;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.data.loadout.Loadout;
import dev.toma.pubgmc.games.Game;
import dev.toma.pubgmc.games.interfaces.IPlayerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class LoadoutPlayerManager extends IPlayerManager.DefaultImpl {

    private final Loadout loadout;

    public LoadoutPlayerManager(ResourceLocation resourceLocation) {
        this.loadout = Pubgmc.getLoadoutManager().getLoadout(resourceLocation);
        if(loadout == null) {
            Pubgmc.pubgmcLog.error(Game.marker, "Loadout {} doesn't exist! Don't expect this to work well", resourceLocation);
        }
    }

    public LoadoutPlayerManager(String path) {
        this(new ResourceLocation(path));
    }

    @Override
    public void gatherPlayers(World world) {
        super.gatherPlayers(world);
        if (loadout != null)
            for (PlayerEntity entity : getPlayerList()) {
                this.loadout.getRandom(entity.world).processFor(entity);
            }
    }

    @Override
    public void handleLogIn(PlayerEntity entity) {
        super.handleLogIn(entity);
        if (loadout != null)
            this.loadout.getRandom(entity.world).processFor(entity);
    }

    @Override
    public void handlePlayerRespawn(PlayerEntity player, GameStorage storage) {
        super.handlePlayerRespawn(player, storage);
        if (loadout != null)
            this.loadout.getRandom(player.world).processFor(player);
    }
}
