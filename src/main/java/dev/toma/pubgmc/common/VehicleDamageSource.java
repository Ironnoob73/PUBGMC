package dev.toma.pubgmc.common;

import dev.toma.pubgmc.common.entity.vehicle.DriveableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class VehicleDamageSource extends DamageSource {

    private final DriveableEntity riddenVehicle;

    VehicleDamageSource(DriveableEntity driveableEntity) {
        super("vehicleCollide");
        this.riddenVehicle = driveableEntity;
    }

    public static VehicleDamageSource from(DriveableEntity riddenVehicle) {
        return new VehicleDamageSource(riddenVehicle);
    }

    @Override
    public Entity getTrueSource() {
        return riddenVehicle != null ? riddenVehicle.getControllingPassenger() != null ? riddenVehicle.getControllingPassenger() : riddenVehicle : null;
    }

    @Override
    public ITextComponent getDeathMessage(LivingEntity entityLivingBaseIn) {
        String key = "death.attack." + this.damageType;
        Entity entity = this.getTrueSource();
        return entity != null ? entity == riddenVehicle ? new TranslationTextComponent(key + ".vehicle", entityLivingBaseIn.getDisplayName(), entity.getDisplayName()) : new TranslationTextComponent(key + ".player", entityLivingBaseIn.getDisplayName(), entity.getDisplayName()) : new TranslationTextComponent(key, entityLivingBaseIn.getDisplayName());
    }
}
