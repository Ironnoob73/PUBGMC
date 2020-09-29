package dev.toma.pubgmc.games.interfaces;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.IChunk;

public interface IObjectManager {

    void handleEntityJoin(Entity entity);

    void handleChunkLoad(IChunk chunk);

    class DefaultImpl implements IObjectManager {

        protected final IKeyHolder keyHolder;

        public DefaultImpl(IKeyHolder keyHolder) {
            this.keyHolder = keyHolder;
        }

        @Override
        public void handleEntityJoin(Entity entity) {
            if(entity instanceof IKeyHolder) {
                long key = ((IKeyHolder) entity).getGameID();
                if(key > 0 && !keyHolder.test(key)) {
                    entity.remove();
                }
            }
        }

        @Override
        public void handleChunkLoad(IChunk chunk) {
            IWorld world = chunk.getWorldForge();
            for(BlockPos pos : chunk.getTileEntitiesPos()) {
                TileEntity tileEntity = world.getTileEntity(pos);
                if(tileEntity instanceof ITileLoadHandler) {
                    ((ITileLoadHandler) tileEntity).load(keyHolder.getGameID());
                }
            }
        }
    }
}
