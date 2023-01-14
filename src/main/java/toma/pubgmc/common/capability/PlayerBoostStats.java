package toma.pubgmc.common.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import toma.pubgmc.api.capability.BoostStats;
import toma.pubgmc.network.Networking;
import toma.pubgmc.network.s2c.S2C_SendBoostData;

public final class PlayerBoostStats implements BoostStats {

    public static final float MAX_BOOST_VALUE = 100.0F;
    private final Player attachedPlayer;
    private float boost;

    public PlayerBoostStats(Player attachedPlayer) {
        this.attachedPlayer = attachedPlayer;
    }

    @Override
    public float getBoostValue() {
        return this.boost;
    }

    @Override
    public void setBoostValue(float value) {
        this.boost = value;
    }

    @Override
    public void sendToClient() {
        if (!this.attachedPlayer.level.isClientSide) {
            Networking.dispatchClientMessage((ServerPlayer) this.attachedPlayer, new S2C_SendBoostData(this.serializeNBT()));
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("boost", boost);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        boost = nbt.getFloat("boost");
    }
}
