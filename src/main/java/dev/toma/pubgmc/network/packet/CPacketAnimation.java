package dev.toma.pubgmc.network.packet;

import dev.toma.pubgmc.client.animation.AnimationManager;
import dev.toma.pubgmc.client.animation.Animations;
import dev.toma.pubgmc.client.animation.builder.AnimationType;
import dev.toma.pubgmc.network.NetworkPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CPacketAnimation implements NetworkPacket<CPacketAnimation> {

    private AnimationType type;
    private Result result;

    public CPacketAnimation() {}

    public CPacketAnimation(AnimationType type, Result result) {
        this.type = type;
        this.result = result;
    }

    @Override
    public void encode(CPacketAnimation instance, PacketBuffer buf) {
        buf.writeInt(instance.type.index);
        buf.writeEnumValue(instance.result);
    }

    @Override
    public CPacketAnimation decode(PacketBuffer buf) {
        return new CPacketAnimation(Animations.getByID(buf.readInt()), buf.readEnumValue(Result.class));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handle(CPacketAnimation instance, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            switch (instance.result) {
                case PLAY:
                    AnimationManager.playNewAnimation(instance.type, instance.type.getDefaultInstance(Minecraft.getInstance().player));
                    break;
                case STOP:
                    AnimationManager.stopAnimation(instance.type);
                    break;
            }
        });
        context.setPacketHandled(true);
    }

    public enum Result {
        PLAY, STOP
    }
}
