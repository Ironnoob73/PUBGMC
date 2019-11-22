package com.toma.pubgmc.api.games;

import com.toma.pubgmc.Pubgmc;
import com.toma.pubgmc.api.Game;
import com.toma.pubgmc.api.GameUtils;
import com.toma.pubgmc.api.Lobby;
import com.toma.pubgmc.common.capability.IGameData;
import com.toma.pubgmc.common.capability.IPlayerData;
import com.toma.pubgmc.common.entity.bot.EntityAIPlayer;
import com.toma.pubgmc.common.items.guns.GunBase;
import com.toma.pubgmc.init.PMCRegistry;
import com.toma.pubgmc.network.PacketHandler;
import com.toma.pubgmc.network.server.PacketChooseLocation;
import com.toma.pubgmc.util.PUBGMCUtil;
import com.toma.pubgmc.util.game.ZoneSettings;
import com.toma.pubgmc.util.game.loot.LootManager;
import com.toma.pubgmc.util.game.loot.LootType;
import com.toma.pubgmc.util.math.ZonePos;
import com.toma.pubgmc.world.BlueZone;
import com.toma.pubgmc.world.MapLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class GameBattleRoyale extends Game {

    private int zoneTimer;
    private List<BlockPos> scheduledAirdrops = new ArrayList<>();
    private boolean hadRegenActive = false;
    private int botsLeft = 0;

    public GameBattleRoyale(String name) {
        super(name);
        this.setGameInfo(new GameInfo("Toma", "- Classic BR mode", "- One life per game", "- Shrinking zone"));
    }

    @Override
    public BlueZone initializeZone(World world) {
        ZoneSettings settings = ZoneSettings.Builder.create().damage(0.1f).speed(0.25f).build();
        return new BlueZone(this.getGameData(world), settings);
    }

    @Override
    public void onGameStart(World world) {
        if(world.isRemote) return;
        GameUtils.createAndFillPlanes(world);
        IGameData gameData = this.getGameData(world);
        zoneTimer = 0;
        scheduledAirdrops.clear();
        List<EntityPlayer> joinedPlayers = this.getOnlinePlayers(world);
        joinedPlayers.forEach(p -> {
            p.setHealth(20.0F);
            p.getFoodStats().setFoodLevel(20);
            p.getFoodStats().setFoodSaturationLevel(15000.0F);
            p.sendMessage(new TextComponentString("Choose one drop location"));
            for(MapLocation location : gameData.getSpawnLocations()) {
                TextComponentString msg = new TextComponentString("- " + location.name());
                msg.setStyle(msg.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "") {
                    @Override
                    public Action getAction() {
                        PacketHandler.sendToServer(new PacketChooseLocation(location.pos(), p.getRidingEntity().getEntityId()));
                        for(int i = 0; i < 50; i++) {
                            p.sendMessage(new TextComponentString(""));
                        }
                        p.sendMessage(new TextComponentString(TextFormatting.GREEN + "Successfully selected drop location, you will be dropped automatically"));
                        return super.getAction();
                    }
                }).setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString(location.pos().toString()))));
                p.sendMessage(msg);
            }
        });
        hadRegenActive = world.getGameRules().getBoolean("naturalRegeneration");
        world.getGameRules().setOrCreateGameRule("naturalRegeneration", "false");
        this.botsLeft = 60 - this.onlinePlayers;
    }

    @Override
    public void onPlayerKilled(EntityPlayer player, @Nullable EntityLivingBase entityLivingBase, ItemStack gun, boolean headshot) {
        TextComponentString deathMessage = new TextComponentString(entityLivingBase == null ? "You have been killed!" : "You have been killed by " + entityLivingBase.getDisplayName() + (gun != null ?
                " using " + gun.getDisplayName() + "!" + (headshot ? "(headshot)" : "" ) : "!"));
        player.sendMessage(deathMessage);
        if(entityLivingBase != null) {
            int range = (int)PUBGMCUtil.getDistanceToBlockPos(player.getPosition(), entityLivingBase.getPosition());
            TextComponentString killerMSG = new TextComponentString("You have killed " + player.getDisplayName() + " [" + range + "m]");
            TextComponentString msgForOthers = new TextComponentString(player.getDisplayName() + (entityLivingBase == null ? "has been killed!" : "has been killed by ") + entityLivingBase.getDisplayName() + (gun != null ?
                    " using " + gun.getDisplayName() + "!" + (headshot ? "(headshot)" : "" ) : "!"));
            getOnlinePlayers(player.world).stream().filter(p -> p.equals(player) || p.equals(entityLivingBase)).forEach(p -> p.sendMessage(msgForOthers));
        }
    }

    @Override
    public void onBotDeath(EntityAIPlayer bot) {
        super.onBotDeath(bot);
        this.botsLeft--;
    }

    @Override
    public void onGameStopped(World world) {
        if(hadRegenActive) {
            world.getGameRules().setOrCreateGameRule("naturalRegeneration", "true");
        }
        this.getOnlinePlayers(world).forEach(player -> player.getFoodStats().setFoodSaturationLevel(15.0F));
    }

    @Override
    public void populatePlayerList(World world) {
        world.playerEntities.forEach(p -> this.getJoinedPlayers().add(p.getUniqueID()));
    }

    @Override
    public boolean respawnPlayer(EntityPlayer player) {
        Lobby lobby = this.getGameData(player.world).getLobby();
        BlockPos pos = lobby.center;
        player.setPositionAndUpdate(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        IPlayerData data = IPlayerData.PlayerData.get(player);
        data.setBoost(0F);
        data.setBackpackLevel(0);
        data.hasEquippedNV(false);
        return super.respawnPlayer(player);
    }

    @Override
    public void onGameTick(World world) {
        if(!zone.isShrinking()) {
            zoneTimer++;
            if(zone.currentStage == 0) {
                if(zoneTimer >= 2400) {
                    zone.notifyFirstZoneCreation(world);
                    this.scheduleAirdrop(world);
                    zoneTimer = 0;
                }
            }
            else if(zoneTimer >= 2000) {
                zone.shrink();
                if(zone.currentStage < 5) {
                    this.scheduleAirdrop(world);
                }
                zoneTimer = 0;
            }
        }
        if(gameTimer % 20 == 0) {
            this.tickScheduledDrops(world);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderGameInfo(ScaledResolution res) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.fontRenderer.drawStringWithShadow("Players left: " + (onlinePlayers + botsLeft), res.getScaledWidth() - 85, 10, 0xFFFFFF);
        boolean shrink = zone.isShrinking();
        int ticksLeft = zone.currentStage == 0 ? 2400 : 2000;
        int secs = (ticksLeft-zoneTimer)/20;
        mc.fontRenderer.drawStringWithShadow("Zone: ", 10, 10, 0xFFFFFF);
        mc.fontRenderer.drawStringWithShadow(shrink ? "Shrinking.." : (zone.currentStage > 0 ? "Shrinking in " + secs + "s" : "-"), 40, 10, shrink ? 0xFF2323 : 0x23FF23);
    }

    @Override
    public void writeDataToNBT(NBTTagCompound nbt) {
        nbt.setInteger("zoneTimer", zoneTimer);
        NBTTagList scheduledDrops = new NBTTagList();
        scheduledAirdrops.forEach(p -> scheduledDrops.appendTag(NBTUtil.createPosTag(p)));
        nbt.setTag("scheduledAirdrops", scheduledDrops);
        nbt.setBoolean("regen", hadRegenActive);
        nbt.setInteger("botsLeft", this.botsLeft);
    }

    @Override
    public void readDataFromNBT(NBTTagCompound nbt) {
        scheduledAirdrops.clear();
        zoneTimer = nbt.getInteger("zoneTimer");
        NBTTagList list = nbt.getTagList("scheduledAirdrops", Constants.NBT.TAG_COMPOUND);
        list.forEach(tag -> scheduledAirdrops.add(NBTUtil.getPosFromTag((NBTTagCompound) tag)));
        hadRegenActive = nbt.getBoolean("regen");
        this.botsLeft = nbt.getInteger("botsLeft");
    }

    private void scheduleAirdrop(World world) {
        ZonePos min = zone.nextBounds.min();
        ZonePos max = zone.nextBounds.max();
        // to allow multiple airdrops
        boolean hasDroppedOnce = false;
        while(world.rand.nextFloat() <= 0.01F || !hasDroppedOnce) {
            int x = Pubgmc.rng().nextInt(Math.abs((int)max.distanceX(min)));
            int z = Pubgmc.rng().nextInt(Math.abs((int)max.distanceZ(min)));
            int y = world.getTopSolidOrLiquidBlock(new BlockPos(min.x + x, 250, min.z + z)).getY();
            y += 100;
            if(y > 255) y = 255;
            BlockPos airdrop = new BlockPos(min.x + x, y, min.z + z);
            scheduledAirdrops.add(airdrop);
            this.notifyAllPlayers(world, TextFormatting.BOLD + "Airdrop is appearing at [ " + airdrop.getX() + " ; " + airdrop.getZ() + " ]! Hurry up!");
            hasDroppedOnce = true;
        }
    }

    private void tickScheduledDrops(World world) {
        Iterator<BlockPos> it = this.scheduledAirdrops.iterator();
        while (it.hasNext()) {
            BlockPos pos = it.next();
            if(world.isBlockLoaded(pos)) {
                PUBGMCUtil.spawnAirdrop(world, pos, false);
                it.remove();
            }
        }
    }

    @Override
    public boolean canSpawnBots() {
        return botsLeft > 0 && botsInGame < 5;
    }

    @Override
    public Consumer<EntityAIPlayer> getLootDistributor() {
        return this::addLootByZone;
    }

    private void addLootByZone(EntityAIPlayer player) {
        switch (zone.currentStage) {
            case 0: return;
            case 1: {
                GunBase gun = (GunBase) LootManager.getRandomObject(LootType.GUN, new GunBase.GunType[] {GunBase.GunType.PISTOL, GunBase.GunType.SHOTGUN, GunBase.GunType.SMG}, (byte) 0);
                EntityItem item = new EntityItem(player.world, player.posX, player.posY, player.posZ, new ItemStack(gun));
                player.inventory.set(0, new ItemStack(player.getRNG().nextInt(2) == 0 ? PMCRegistry.PMCItems.PAINKILLERS : PMCRegistry.PMCItems.ENERGYDRINK));
                Item ammo = gun.getAmmoType().ammo();
                player.inventory.set(1, new ItemStack(ammo, 30));
                player.inventory.set(2, new ItemStack(ammo, 30));
                if(player.getRNG().nextInt(2) == 0) {
                    player.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(PMCRegistry.PMCItems.ARMOR1HELMET));
                } else player.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(PMCRegistry.PMCItems.ARMOR1BODY));
                player.world.spawnEntity(item);
                break;
            }
            case 2: case 3: {
                GunBase gun = (GunBase) LootManager.getRandomObject(LootType.GUN, new GunBase.GunType[] {GunBase.GunType.AR, GunBase.GunType.DMR, GunBase.GunType.SMG}, (byte) 0);
                EntityItem item = new EntityItem(player.world, player.posX, player.posY, player.posZ, new ItemStack(gun));
                player.inventory.set(0, new ItemStack(player.getRNG().nextInt(2) == 0 ? PMCRegistry.PMCItems.FIRSTAIDKIT : PMCRegistry.PMCItems.BANDAGE));
                Item ammo = gun.getAmmoType().ammo();
                player.inventory.set(1, new ItemStack(ammo, 30));
                player.inventory.set(2, new ItemStack(ammo, 30));
                player.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(PMCRegistry.PMCItems.ARMOR2HELMET));
                player.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(PMCRegistry.PMCItems.ARMOR1BODY));
                player.world.spawnEntity(item);
                break;
            }
            default: {
                GunBase gun = (GunBase) LootManager.getRandomObject(LootType.GUN, new GunBase.GunType[] {GunBase.GunType.AR, GunBase.GunType.DMR, GunBase.GunType.SR}, (byte) 1);
                EntityItem item = new EntityItem(player.world, player.posX, player.posY, player.posZ, new ItemStack(gun));
                player.inventory.set(0, new ItemStack(player.getRNG().nextInt(2) == 0 ? PMCRegistry.PMCItems.FIRSTAIDKIT : PMCRegistry.PMCItems.MEDKIT));
                Item ammo = gun.getAmmoType().ammo();
                player.inventory.set(1, new ItemStack(ammo, 30));
                player.inventory.set(2, new ItemStack(ammo, 30));
                player.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(player.getRNG().nextInt(5) == 0 ? PMCRegistry.PMCItems.ARMOR3HELMET : PMCRegistry.PMCItems.ARMOR2HELMET));
                player.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(PMCRegistry.PMCItems.ARMOR2BODY));
                player.world.spawnEntity(item);
                break;
            }
        }
    }
}
