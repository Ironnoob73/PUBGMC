package dev.toma.pubgmc.common.entity;

import dev.toma.pubgmc.capability.IWorldCap;
import dev.toma.pubgmc.capability.world.WorldDataFactory;
import dev.toma.pubgmc.capability.world.WorldDataProvider;
import dev.toma.pubgmc.common.entity.goal.GunAttackGoal;
import dev.toma.pubgmc.common.inventory.IHasInventory;
import dev.toma.pubgmc.common.inventory.PMCInventoryItem;
import dev.toma.pubgmc.common.item.gun.GunCategory;
import dev.toma.pubgmc.common.item.gun.GunItem;
import dev.toma.pubgmc.common.item.utility.BackpackSlotItem;
import dev.toma.pubgmc.common.item.wearable.IPMCArmor;
import dev.toma.pubgmc.games.Game;
import dev.toma.pubgmc.games.interfaces.IKeyHolder;
import dev.toma.pubgmc.games.interfaces.IZone;
import dev.toma.pubgmc.network.NetworkManager;
import dev.toma.pubgmc.network.packet.CPacketSyncEntity;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.items.IItemHandler;

public class BotEntity extends CreatureEntity implements IKeyHolder, IHasInventory, IEntityAdditionalSpawnData {

    private final InventoryManager botInventory;
    private GunCategory weaponPreference;
    private long gameID;
    private final Game game;
    private byte variant;

    public BotEntity(EntityType<? extends CreatureEntity> type, World world) {
        super(type, world);
        this.botInventory = new InventoryManager();
        IWorldCap cap = WorldDataFactory.getData(world);
        game = cap.getGame();
        if(game != null && game.isRunning()) {
            this.gameID = game.getGameID();
        }
        variant = (byte) world.rand.nextInt(4);
        weaponPreference = GunCategory.getRandomBotCategory(world);
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        buffer.writeByte(variant);
        buffer.writeLong(gameID);
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        variant = additionalData.readByte();
        gameID = additionalData.readLong();
    }

    @Override
    public void tick() {
        super.tick();
        if(isRunningGame()) {
            IZone zone = game.getZone();
            if(!world.isRemote && !zone.isIn(this) && world.getGameTime() % 30 == 0L) {
                this.attackEntityFrom(IZone.ZONE_DAMAGE, zone.getZoneDamage());
            }
        }
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        if(gameID <= 0) return;
        world.getCapability(WorldDataProvider.CAP).ifPresent(cap -> {
            Game game = cap.getGame();
            if(game != null && game.isRunning()) {
                if(!game.test(BotEntity.this)) {
                    BotEntity.this.remove();
                } else {
                    game.processBotSpawn(this);
                }
            }
        });
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(4, new GunAttackGoal(this));
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
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        if(game != null && game.isRunning() && gameID == 0) {
            gameID = game.getGameID();
        }
        compound.putLong("gameID", gameID);
        compound.put("inventory", botInventory.serializeNBT());
        compound.putByte("variant", variant);
        compound.putByte("preference", (byte) weaponPreference.ordinal());
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        gameID = compound.getLong("gameID");
        variant = compound.getByte("variant");
        weaponPreference = GunCategory.values()[compound.getByte("preference")];
        if(compound.contains("inventory", Constants.NBT.TAG_LIST)) {
            botInventory.deserializeNBT(compound.getList("inventory", Constants.NBT.TAG_COMPOUND));
        }
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

    @Override
    public boolean canPickUpLoot() {
        return true;
    }

    @Override
    protected void updateEquipmentIfNeeded(ItemEntity itemEntity) {
        ItemStack itemstack = itemEntity.getItem();
        EquipmentSlotType equipmentslottype = getSlotForItemStack(itemstack);
        ItemStack itemstack1;
        if(equipmentslottype == EquipmentSlotType.MAINHAND) {
            itemstack1 = ItemStack.EMPTY;
            if(itemstack.getItem() instanceof PMCInventoryItem) {
                itemstack1 = botInventory.getEquipment(((PMCInventoryItem) itemstack.getItem()).getSlotType().ordinal());
            }
        } else {
            itemstack1 = getItemStackFromSlot(equipmentslottype);
        }
        boolean flag = this.shouldExchangeEquipment(itemstack, itemstack1, equipmentslottype);
        if (flag && this.canEquipItem(itemstack)) {
            double d0 = this.getDropChance(equipmentslottype);
            if (!itemstack1.isEmpty() && (double)(this.rand.nextFloat() - 0.1F) < d0) {
                this.entityDropItem(itemstack1);
            }
            this.setItemStackToSlot(equipmentslottype, itemstack);
            switch(equipmentslottype.getSlotType()) {
                case HAND:
                    this.inventoryHandsDropChances[equipmentslottype.getIndex()] = 2.0F;
                    break;
                case ARMOR:
                    this.inventoryArmorDropChances[equipmentslottype.getIndex()] = 2.0F;
            }
            this.enablePersistence();
            this.onItemPickup(itemEntity, itemstack.getCount());
            itemEntity.remove();
        }
    }

    @Override
    public void setItemStackToSlot(EquipmentSlotType slotIn, ItemStack stack) {
        if(stack.getItem() instanceof PMCInventoryItem) {
            int i = ((PMCInventoryItem) stack.getItem()).getSlotType().ordinal();
            this.botInventory.equipmentInventory.setInventorySlotContents(i, stack.copy());
            if(!world.isRemote) {
                CompoundNBT nbt = writeWithoutTypeId(new CompoundNBT());
                NetworkManager.sendToAll(world, new CPacketSyncEntity(this.getEntityId(), nbt));
            }
        } else {
            super.setItemStackToSlot(slotIn, stack);
        }
    }

    @Override
    protected boolean shouldExchangeEquipment(ItemStack candidate, ItemStack existing, EquipmentSlotType slotType) {
        if(candidate.getItem() instanceof IPMCArmor) {
            IPMCArmor armor_n = (IPMCArmor) candidate.getItem();
            if(existing.getItem() instanceof IPMCArmor) {
                IPMCArmor armor_c = (IPMCArmor) existing.getItem();
                if(armor_n.damageMultiplier() < armor_c.damageMultiplier()) {
                    return true;
                } else return armor_n.damageMultiplier() == armor_c.damageMultiplier() && candidate.getDamage() < existing.getDamage();
            } else return true;
        } else if(candidate.getItem() instanceof PMCInventoryItem) {
            int i = ((PMCInventoryItem) candidate.getItem()).getSlotType().ordinal();
            ItemStack equipment = botInventory.getEquipment(i);
            if(i == 2 && candidate.getItem() instanceof BackpackSlotItem) {
                if(equipment.getItem() instanceof BackpackSlotItem) {
                    BackpackSlotItem currentBackpack = (BackpackSlotItem) equipment.getItem();
                    int res = ((BackpackSlotItem) candidate.getItem()).compareTo(currentBackpack);
                    return res < 0;
                } else return true;
            } else {
                return equipment.isEmpty();
            }
        } else if(slotType == EquipmentSlotType.MAINHAND) {
            if(candidate.getItem() instanceof GunItem) {
                if(existing.getItem() instanceof GunItem) {
                    GunItem current = (GunItem) existing.getItem();
                    GunItem toPick = (GunItem) candidate.getItem();
                    if(current.getCategory() != weaponPreference) {
                        return current.getGunDamage() < toPick.getGunDamage();
                    } else return toPick.getCategory() == weaponPreference && toPick.getGunDamage() > current.getGunDamage();
                } else {
                    return true;
                }
            }
        }
        return super.shouldExchangeEquipment(candidate, existing, slotType);
    }

    @Override
    protected float getDropChance(EquipmentSlotType slotIn) {
        return 1.0F;
    }

    public boolean isRunningGame() {
        return game != null && game.isRunning();
    }

    @Override
    public InventoryManager getInventory() {
        return botInventory;
    }

    public byte getVariant() {
        return variant;
    }

    @Override
    public void transferTo(IItemHandler handler) {
        for (EquipmentSlotType slotType : EquipmentSlotType.values()) {
            ItemStack stack = getItemStackFromSlot(slotType);
            if(!stack.isEmpty()) {
                handler.insertItem(slotType.ordinal(), stack.copy(), false);
            }
            setItemStackToSlot(slotType, ItemStack.EMPTY);
        }
        int index = EquipmentSlotType.values().length;
        Inventory storage = botInventory.storageInventory;
        for (int i = 0; i < storage.getSizeInventory(); i++) {
            ItemStack stack = storage.getStackInSlot(i);
            if(!stack.isEmpty()) {
                handler.insertItem(index + i, stack.copy(), false);
            }
        }
        storage.clear();
        index += storage.getSizeInventory();
        Inventory equipment = botInventory.equipmentInventory;
        for (int i = 0; i < equipment.getSizeInventory(); i++) {
            ItemStack stack = equipment.getStackInSlot(i);
            if(!stack.isEmpty()) {
                handler.insertItem(index + i, stack.copy(), false);
            }
        }
        equipment.clear();
    }

    public static class InventoryManager implements EquipmentHolder, INBTSerializable<ListNBT> {

        private final Inventory equipmentInventory = new Inventory(3);
        private final Inventory storageInventory = new Inventory(9);

        public ItemStack getEquipment(int slot) {
            return equipmentInventory.getStackInSlot(slot);
        }

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

        @Override
        public ListNBT serializeNBT() {
            ListNBT list = new ListNBT();
            serializeInventory(equipmentInventory, list, false);
            serializeInventory(storageInventory, list, true);
            return list;
        }

        @Override
        public void deserializeNBT(ListNBT nbt) {
            for(int i = 0; i < equipmentInventory.getSizeInventory() + storageInventory.getSizeInventory(); i++) {
                CompoundNBT slotData = nbt.getCompound(i);
                int slotIndex = slotData.getInt("index");
                boolean isStorage = slotData.getBoolean("storage");
                ItemStack stack = ItemStack.read(slotData.getCompound("item"));
                Inventory inventory = isStorage ? storageInventory : equipmentInventory;
                inventory.setInventorySlotContents(slotIndex, stack);
            }
        }

        private void serializeInventory(Inventory inventory, ListNBT nbt, boolean isStorage) {
            for (int i = 0; i < inventory.getSizeInventory(); i++) {
                ItemStack stack = inventory.getStackInSlot(i);
                if(!stack.isEmpty()) {
                    CompoundNBT slot = new CompoundNBT();
                    CompoundNBT item = stack.serializeNBT();
                    slot.putBoolean("storage", false);
                    slot.putInt("index", i);
                    slot.put("item", item);
                    nbt.add(slot);
                }
            }
        }
    }
}
