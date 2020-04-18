package dev.toma.pubgmc.common.entity.throwable;

import dev.toma.pubgmc.Registry;
import dev.toma.pubgmc.client.ClientManager;
import dev.toma.pubgmc.network.NetworkManager;
import dev.toma.pubgmc.network.packet.CPacketFlashStatus;
import dev.toma.pubgmc.util.RenderHelper;
import dev.toma.pubgmc.util.UsefulFunctions;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class FlashEntity extends ThrowableEntity {

    public FlashEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public FlashEntity(EntityType<?> type, World world, LivingEntity thrower, EnumEntityThrowState state) {
        super(type, world, thrower, state);
    }

    public FlashEntity(World world, LivingEntity thrower, EnumEntityThrowState state, int timeLeft) {
        super(Registry.PMCEntityTypes.FLASH, world, thrower, state, timeLeft);
    }

    @Override
    public void bounce() {
        if(timesBounced == 1) fuse = Math.min(fuse, 14);
    }

    @Override
    public void onExplode() {
        if (world.isRemote) {
            ClientManager.playDelayedSoundAt(SoundEvents.ENTITY_GENERIC_EXPLODE, posX, posY, posZ, 5.0F, 1.0F);
            world.addParticle(ParticleTypes.EXPLOSION, posX, posY, posZ, 0, 0, 0);
            for (int i = 0; i < 7; i++) {
                world.addParticle(ParticleTypes.CLOUD, posX, posY, posZ, rand.nextDouble() / 8 - rand.nextDouble() / 8, rand.nextDouble() / 4, rand.nextDouble() / 8 - rand.nextDouble() / 8);
            }
        }
        List<PlayerEntity> entityList = world.getEntitiesWithinAABB(PlayerEntity.class, VoxelShapes.fullCube().getBoundingBox().offset(getPosition()).grow(30));
        Vec3d start = this.getPositionVec();
        entityList.forEach(e -> {
            Vec3d entityVec = new Vec3d(e.posX, e.posY + e.getEyeHeight() + 0.25, e.posZ);
            if (this.canReachTo(start, entityVec)) {
                FlashHandler.flashPlayer(e, this);
            }
        });
        if(!world.isRemote) this.remove();
    }

    protected boolean canReachTo(Vec3d from, Vec3d to) {
        Vec3d diffVec = to.subtract(from);
        int checks = (int) UsefulFunctions.getDistance(from, to);
        double x = diffVec.x / checks;
        double y = diffVec.y / checks;
        double z = diffVec.z / checks;
        for(int i = 0; i < checks; i++) {
            BlockPos pos = new BlockPos(from.x + i * x, from.y + i * y, from.z + i * z);
            BlockState state = world.getBlockState(pos);
            if(world.isAirBlock(pos) || !world.getBlockState(pos).isOpaqueCube(world, pos))
                continue;
            return false;
        }
        return true;
    }

    private int getPartialChecksAmount(Vec3d from, Vec3d to) {
        return (int) Math.max(1.0D, UsefulFunctions.getDistance(from.x, from.y, from.z, to.x, to.y, to.z) * 2);
    }

    public static class FlashHandler {

        public static final Map<UUID, Integer> FLASHED_PLAYERS = new HashMap<>();
        public static final int MAX_FLASH_RANGE = 20;

        public static void flashPlayer(PlayerEntity player, FlashEntity from) {
            UUID uuid = player.getUniqueID();
            if (isInRangeToFlash(player, from)) {
                int amount = getFlashAmountFor(player, from);
                if (amount > 0) {
                    FLASHED_PLAYERS.put(uuid, amount);
                    if (player instanceof ServerPlayerEntity) {
                        NetworkManager.sendToClient((ServerPlayerEntity) player, new CPacketFlashStatus(true));
                    }
                }
            } else if (getFlashAmountFor(player, from) > 0) {
                // TODO
                //player.playSound(PMCSounds.flash_short, 10f, 1f);
            }
        }

        public static int getFlashAmountFor(PlayerEntity player, FlashEntity flash) {
            float f0 = (float) UsefulFunctions.getDistance(player.getPosition(), flash.getPosition());
            int amount = 180;
            if (f0 > MAX_FLASH_RANGE + 5) {
                return 0;
            } else if (f0 > MAX_FLASH_RANGE - 5 && f0 <= MAX_FLASH_RANGE + 5) {
                amount = 1;
            }
            // TODO
            //SoundEvent e = amount == 1 ? PMCSounds.flash_short : PMCSounds.flash;
            //player.playSound(e, 10f, 1f);
            return amount;
        }

        public static boolean isInRangeToFlash(PlayerEntity player, FlashEntity flash) {
            Vec3d playerPos = player.getPositionVec();
            Vec3d playerLook = player.getLookVec();
            Vec3d playerLookNegativeYaw = playerPos.add(playerLook.rotateYaw(-40f));
            Vec3d playerLookPositiveYaw = playerPos.add(playerLook.rotateYaw(40f));
            Vec3d flashPos = flash.getPositionVec();
            double d0 = area(playerPos, playerLookNegativeYaw, playerLookPositiveYaw) * 10000;
            double d1 = area(flashPos, playerLookNegativeYaw, playerLookPositiveYaw) * 10000;
            double d2 = area(playerPos, flashPos, playerLookPositiveYaw) * 10000;
            double d3 = area(playerPos, playerLookNegativeYaw, flashPos) * 10000;
            return ((int) d1 == (int) (d0 + d2 + d3));
        }

        private static double area(Vec3d yawNeg, Vec3d yawPos, Vec3d flash) {
            return Math.abs((yawNeg.x * (yawPos.z - flash.z) + yawPos.x * (flash.z - yawNeg.z) + flash.x * (yawNeg.z - yawPos.z)) / 2.0D);
        }

        @Mod.EventBusSubscriber
        public static class ServerHandler {

            @SubscribeEvent
            public static void worldTick(TickEvent.WorldTickEvent e) {
                if (!FLASHED_PLAYERS.isEmpty()) {
                    List<UUID> list = FLASHED_PLAYERS.keySet().stream().filter(uuid -> e.world.getPlayerByUuid(uuid) != null).collect(Collectors.toList());
                    for (int i = 0; i < list.size(); i++) {
                        UUID player = list.get(i);
                        int amount = FLASHED_PLAYERS.get(player);
                        amount--;
                        if (amount > 0) {
                            FLASHED_PLAYERS.put(player, amount);
                        } else {
                            FLASHED_PLAYERS.remove(player);
                            PlayerEntity p = e.world.getPlayerByUuid(player);
                            if (p instanceof ServerPlayerEntity) {
                                NetworkManager.sendToClient((ServerPlayerEntity) p, new CPacketFlashStatus(false));
                            }
                        }
                    }
                }
            }
        }

        @Mod.EventBusSubscriber(Dist.CLIENT)
        public static class ClientHandler {

            static boolean blind = false;
            static int progress;

            @SubscribeEvent(priority = EventPriority.LOWEST)
            public static void renderOverlay(RenderGameOverlayEvent.Post e) {
                if (blind || progress > 0) {
                    if (e.getType() == RenderGameOverlayEvent.ElementType.ALL) {
                        Minecraft mc = Minecraft.getInstance();
                        RenderHelper.drawColoredShape(0, 0, mc.mainWindow.getScaledWidth(), mc.mainWindow.getScaledHeight(), 1.0F, 1.0F, 1.0F, blind ? 1f : progress / 100F);
                    } else if (e.isCancelable()) {
                        e.setCanceled(true);
                    }
                }
            }

            @SubscribeEvent
            public static void onTick(TickEvent.ClientTickEvent e) {
                if (e.phase == TickEvent.Phase.START && Minecraft.getInstance().player != null) {
                    if (progress > 0) {
                        --progress;
                    }
                }
            }

            public static void update(boolean b) {
                blind = b;
                if (!b) {
                    progress = 100;
                }
            }
        }
    }
}
