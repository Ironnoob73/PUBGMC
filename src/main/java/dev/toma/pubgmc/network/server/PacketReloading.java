package dev.toma.pubgmc.network.server;

import dev.toma.pubgmc.api.capability.IPlayerData;
import dev.toma.pubgmc.api.capability.PlayerDataProvider;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketReloading implements IMessage {
    private boolean reload;

    public PacketReloading() {
    }

    public PacketReloading(boolean reloading) {
        this.reload = reloading;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(reload);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        reload = buf.readBoolean();
    }

    public static class Handler implements IMessageHandler<PacketReloading, IMessage> {
        @Override
        public IMessage onMessage(PacketReloading message, MessageContext ctx) {
            EntityPlayer player = ctx.getServerHandler().player;
            player.getServer().addScheduledTask(() -> {
                IPlayerData data = PlayerDataProvider.get(player);
                //data.setReloading(message.reload);
                data.sync();
            });
            return null;
        }
    }
}
