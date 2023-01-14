package toma.pubgmc.network.s2c;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import toma.pubgmc.api.capability.PubgmcCapabilities;
import toma.pubgmc.network.Message;

public class S2C_SendBoostData extends Message {

    private final CompoundTag tag;

    public S2C_SendBoostData(CompoundTag tag) {
        this.tag = tag;
    }

    // Decoder
    public S2C_SendBoostData(FriendlyByteBuf buffer) {
        this(buffer.readNbt());
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeNbt(tag);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void process(NetworkEvent.Context context) {
        Minecraft client = Minecraft.getInstance();
        Player player = client.player;
        if (player != null) {
            player.getCapability(PubgmcCapabilities.BOOST_STATS).ifPresent(stats -> stats.deserializeNBT(tag));
        }
    }
}
