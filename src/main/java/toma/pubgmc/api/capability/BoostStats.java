package toma.pubgmc.api.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;

@AutoRegisterCapability
public interface BoostStats extends INBTSerializable<CompoundTag> {

    float getBoostValue();

    void setBoostValue(float value);

    void sendToClient();

    default void addBoostValue(float toAdd) {
        setBoostValue(getBoostValue() + toAdd);
    }
}
