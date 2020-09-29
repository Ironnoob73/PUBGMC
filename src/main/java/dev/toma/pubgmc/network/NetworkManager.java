package dev.toma.pubgmc.network;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.network.packet.*;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class NetworkManager {

    protected static String protocolVersion = "pmc1";
    private static final SimpleChannel network = NetworkRegistry.ChannelBuilder
            .named(Pubgmc.makeResource("network"))
            .networkProtocolVersion(() -> protocolVersion)
            .clientAcceptedVersions(protocolVersion::equals)
            .serverAcceptedVersions(protocolVersion::equals)
            .simpleChannel();

    public static void init() {
        register(CPacketSendNBT.class, new CPacketSendNBT(null, 0));
        register(CPacketSyncEntity.class, new CPacketSyncEntity());
        register(CPacketFlashStatus.class, new CPacketFlashStatus());
        register(CPacketSendRecipes.class, new CPacketSendRecipes());
        register(CPacketBulletImpactParticle.class, new CPacketBulletImpactParticle());
        register(CPacketAnimation.class, new CPacketAnimation());
        register(CPacketDisplayLootSpawners.class, new CPacketDisplayLootSpawners());
        register(CPacketSyncInventory.class, new CPacketSyncInventory());
        register(CPacketSyncWorldData.class, new CPacketSyncWorldData());
        register(SPacketControllableInput.class, new SPacketControllableInput());
        register(SPacketCookThrowable.class, new SPacketCookThrowable());
        register(SPacketShoot.class, new SPacketShoot());
        register(SPacketSetAiming.class, new SPacketSetAiming());
        register(SPacketSetReloading.class, new SPacketSetReloading());
        register(SPacketFiremode.class, new SPacketFiremode());
        register(SPacketOpenPlayerInventory.class, new SPacketOpenPlayerInventory());
        register(SPacketOpenAttachmentMenu.class, new SPacketOpenAttachmentMenu());
    }

    public static void sendToClient(ServerPlayerEntity player, NetworkPacket<?> packet) {
        network.sendTo(packet, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToAll(World world, NetworkPacket<?> packet) {
        world.getServer().getPlayerList().getPlayers().forEach(player -> sendToClient(player, packet));
    }

    public static void sendToServer(NetworkPacket<?> packet) {
        network.sendToServer(packet);
    }

    private static <T> void register(Class<T> tClass, NetworkPacket<T> packet) {
        network.registerMessage(++id, tClass, packet::encode, packet::decode, packet::handle);
    }

    private static int id = -1;
}
