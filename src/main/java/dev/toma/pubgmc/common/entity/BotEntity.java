package dev.toma.pubgmc.common.entity;

import dev.toma.pubgmc.capability.IWorldCap;
import dev.toma.pubgmc.capability.world.WorldDataFactory;
import dev.toma.pubgmc.capability.world.WorldDataProvider;
import dev.toma.pubgmc.games.Game;
import dev.toma.pubgmc.games.interfaces.IKeyHolder;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

public class BotEntity extends CreatureEntity implements IKeyHolder {

    private long gameID;
    private final InventoryManager botInventory;

    public BotEntity(EntityType<? extends CreatureEntity> type, World world) {
        super(type, world);
        this.botInventory = new InventoryManager();
        IWorldCap cap = WorldDataFactory.getData(world);
        Game game = cap.getGame();
        if(game != null && game.isRunning()) {
            this.gameID = game.getGameID();
        }
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        if(gameID <= 0) return;
        world.getCapability(WorldDataProvider.CAP).ifPresent(cap -> {
            Game game = cap.getGame();
            if(game != null && game.isRunning()) {
                if(!game.test(gameID)) {
                    BotEntity.this.remove();
                } else {
                    game.processBotSpawn(this);
                }
            }
        });
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 16.0F));
        this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, BotEntity.class, true));
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0);
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putLong("gameID", gameID);
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        gameID = compound.getLong("gameID");
    }

    @Override
    public void setGameID(long gameID) {
        this.gameID = gameID;
    }

    @Override
    public long getGameID() {
        return gameID;
    }

    @Override
    public boolean isLeftHanded() {
        return false;
    }

    public InventoryManager getInventory() {
        return botInventory;
    }

    static class InventoryManager implements EquipmentHolder {

        private final Inventory equipmentInventory = new Inventory(3);
        private final Inventory mainInventory = new Inventory(9);

        @Override
        public ItemStack getNightVision() {
            return equipmentInventory.getStackInSlot(0);
        }

        @Override
        public ItemStack getGhillie() {
            return equipmentInventory.getStackInSlot(1);
        }

        @Override
        public ItemStack getBackpack() {
            return equipmentInventory.getStackInSlot(2);
        }
    }
}
