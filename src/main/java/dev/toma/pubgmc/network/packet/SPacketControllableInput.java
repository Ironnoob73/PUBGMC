package dev.toma.pubgmc.network.packet;

import dev.toma.pubgmc.api.entity.IControllableEntity;
import dev.toma.pubgmc.network.NetworkPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SPacketControllableInput implements NetworkPacket<SPacketControllableInput> {

    public SPacketControllableInput() {
    }

    private boolean f, b, r, l;

    public SPacketControllableInput(boolean f, boolean b, boolean r, boolean l) {
        this.f = f;
        this.b = b;
        this.r = r;
        this.l = l;
    }

    @Override
    public void encode(SPacketControllableInput instance, PacketBuffer buf) {
        buf.writeBoolean(instance.f);
        buf.writeBoolean(instance.b);
        buf.writeBoolean(instance.r);
        buf.writeBoolean(instance.l);
    }

    @Override
    public SPacketControllableInput decode(PacketBuffer buf) {
        return new SPacketControllableInput(buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean());
    }

    @Override
    public void handle(SPacketControllableInput instance, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if(player.getRidingEntity() instanceof IControllableEntity) {
                ((IControllableEntity) player.getRidingEntity()).onInputUpdate(instance.f, instance.b, instance.r, instance.l);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
