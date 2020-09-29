package dev.toma.pubgmc.games.interfaces;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

public interface IDataHandler<K, V extends INBTSerializable<CompoundNBT>> extends Function<K, UUID> {

    Map<UUID, V> getStoredData();

    @Nullable
    V get(K k);

    Set<UUID> keys();

    Collection<V> values();

    class Impl<E extends LivingEntity, D extends BaseDataHolder> implements IDataHandler<E, D> {

        private final Map<UUID, D> map = new HashMap<>();

        @Override
        public Map<UUID, D> getStoredData() {
            return map;
        }

        @Nullable
        @Override
        public D get(E e) {
            return map.get(apply(e));
        }

        @Override
        public Set<UUID> keys() {
            return map.keySet();
        }

        @Override
        public Collection<D> values() {
            return map.values();
        }

        @Override
        public UUID apply(E e) {
            return e.getUniqueID();
        }
    }

    class BaseDataHolder implements INBTSerializable<CompoundNBT> {

        private int killCount;

        public int getKillCount() {
            return killCount;
        }

        public void addKill() {
            ++killCount;
        }

        public void reset() {
            setKillCount(0);
        }

        void setKillCount(int killCount) {
            this.killCount = killCount;
        }

        @Override
        public CompoundNBT serializeNBT() {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putInt("kills", killCount);
            return nbt;
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            setKillCount(nbt.getInt("kills"));
        }
    }
}
