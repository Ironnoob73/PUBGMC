package dev.toma.pubgmc.games.interfaces;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.IChunk;

import java.util.Set;

public interface IObjectManager {

    void handleEntityJoin(Entity entity);

    void handleChunkLoad(IChunk chunk);

    void handleTileEntityUpdate(TileEntity tileEntity);

    class DefaultImpl implements IObjectManager {

        protected final IKeyHolder keyHolder;

        public DefaultImpl(IKeyHolder keyHolder) {
            this.keyHolder = keyHolder;
        }

        @Override
        public void handleEntityJoin(Entity entity) {
            if(entity instanceof IKeyHolder) {
                IKeyHolder key = (IKeyHolder) entity;
                if(key.getGameID() > 0 && !keyHolder.test(key)) {
                    entity.remove();
                }
            }
        }

        @Override
        public void handleChunkLoad(IChunk chunk) {
            IWorld world = chunk.getWorldForge();
            Set<BlockPos> positions = chunk.getTileEntitiesPos();
            for (BlockPos pos : positions) {
                TileEntity tileEntity = chunk.getTileEntity(pos);
                handleTileEntityUpdate(tileEntity);
            }
        }

        @Override
        public void handleTileEntityUpdate(TileEntity tileEntity) {
            if(tileEntity instanceof ITileLoadHandler) {
                ITileLoadHandler loadHandler = (ITileLoadHandler) tileEntity;
                loadHandler.load(this.keyHolder);
            }
        }
    }
}
