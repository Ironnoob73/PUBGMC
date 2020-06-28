package dev.toma.pubgmc.network.packet;

import dev.toma.pubgmc.client.animation.Animation;
import dev.toma.pubgmc.client.animation.AnimationManager;
import dev.toma.pubgmc.client.animation.Animations;
import dev.toma.pubgmc.client.animation.types.RecoilAnimation;
import dev.toma.pubgmc.network.NetworkPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CPacketCreateAnimation implements NetworkPacket<CPacketCreateAnimation> {

    private int animationID;

    public CPacketCreateAnimation() {}

    public CPacketCreateAnimation(int id) {
        this.animationID = id;
    }

    @Override
    public void encode(CPacketCreateAnimation instance, PacketBuffer buf) {
        buf.writeInt(instance.animationID);
    }

    @Override
    public CPacketCreateAnimation decode(PacketBuffer buf) {
        return new CPacketCreateAnimation(buf.readInt());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handle(CPacketCreateAnimation instance, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            switch (instance.animationID) {
                case 5:
                    AnimationManager.playNewAnimation(Animations.RECOIL, new RecoilAnimation());
                    break;
            }
        });
        context.setPacketHandled(true);
    }
}
