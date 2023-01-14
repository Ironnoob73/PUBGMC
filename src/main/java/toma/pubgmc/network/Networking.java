package toma.pubgmc.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import toma.pubgmc.Pubgmc;
import toma.pubgmc.network.s2c.S2C_SendBoostData;
import toma.pubgmc.util.ResourceUtil;

public final class Networking {

    private static final int VERSION_INDEX = 1;
    private static final String NETWORK_VERSION = String.format("%s-v%d", Pubgmc.MODID, VERSION_INDEX);
    private static final SimpleChannel NETWORK_CHANNEL = NetworkRegistry.ChannelBuilder
            .named(ResourceUtil.of("network_channel")).networkProtocolVersion(() -> NETWORK_VERSION)
            .serverAcceptedVersions(NETWORK_VERSION::equals).clientAcceptedVersions(NETWORK_VERSION::equals)
            .simpleChannel();

    public static void dispatchServerMessage(Message message) {
        NETWORK_CHANNEL.sendToServer(message);
    }

    public static void dispatchClientMessage(ServerPlayer serverPlayerRef, Message message) {
        NETWORK_CHANNEL.sendTo(message, serverPlayerRef.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static final class MessageRegistry {

        private static int messageIndex;

        public static void registerMessages() {
            registerMessage(S2C_SendBoostData.class, S2C_SendBoostData::new);
        }

        private static <M extends Message> void registerMessage(Class<M> messageType, Message.Decoder<M> decoder) {
            NETWORK_CHANNEL.registerMessage(messageIndex++, messageType, Message::encode, decoder, Message::handleMessage);
        }
    }
}
