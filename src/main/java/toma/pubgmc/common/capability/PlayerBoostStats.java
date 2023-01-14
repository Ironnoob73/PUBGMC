package toma.pubgmc.common.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import toma.pubgmc.api.capability.BoostStats;

public final class PlayerBoostStats implements BoostStats {

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
        // TODO implement once Networking is done
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
