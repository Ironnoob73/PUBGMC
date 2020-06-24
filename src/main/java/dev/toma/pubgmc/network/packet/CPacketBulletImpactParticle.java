package dev.toma.pubgmc.network.packet;

import dev.toma.pubgmc.network.NetworkPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;
import java.util.function.Supplier;

public class CPacketBulletImpactParticle implements NetworkPacket<CPacketBulletImpactParticle> {

    private BlockPos pos;
    private Vec3d hit;

    public CPacketBulletImpactParticle() {
    }

    public CPacketBulletImpactParticle(BlockPos pos, Vec3d hit) {
        this.pos = pos;
        this.hit = hit;
    }

    @Override
    public void encode(CPacketBulletImpactParticle instance, PacketBuffer buf) {
        buf.writeBlockPos(instance.pos);
        buf.writeDouble(instance.hit.x);
        buf.writeDouble(instance.hit.y);
        buf.writeDouble(instance.hit.z);
    }

    @Override
    public CPacketBulletImpactParticle decode(PacketBuffer buf) {
        return new CPacketBulletImpactParticle(buf.readBlockPos(), new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble()));
    }

    @SuppressWarnings("unchecked")
    @OnlyIn(Dist.CLIENT)
    @Override
    public void handle(CPacketBulletImpactParticle instance, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientWorld world = Minecraft.getInstance().world;
            BlockParticleData particleData = new BlockParticleData((ParticleType<BlockParticleData>) ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation("minecraft:block")), world.getBlockState(instance.pos));
            Random rand = new Random();
            for(int i = 0; i < 5; i++) {
                world.addParticle(particleData, true, instance.hit.x, instance.hit.y, instance.hit.z, rng(rand), rand.nextDouble() * 2, rng(rand));
            }
        });
        ctx.get().setPacketHandled(true);
    }

    private double rng(Random rand) {
        return rand.nextDouble() / 5.0D - rand.nextDouble() / 5.0D;
    }
}
