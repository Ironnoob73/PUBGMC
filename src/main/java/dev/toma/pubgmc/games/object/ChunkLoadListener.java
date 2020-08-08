package dev.toma.pubgmc.games.object;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunk;

public interface ChunkLoadListener extends GameKeyHolder {

    /**
     * Called when new chunk is loaded in world while
     * some Game instance is running
     * @param chunk - chunk which has been loaded
     * @param world - the world where chunk is at
     */
    void loadChunk(IChunk chunk, World world);
}
