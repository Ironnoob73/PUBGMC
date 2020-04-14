package dev.toma.pubgmc.common.item.wearable;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.api.IPMCArmor;
import dev.toma.pubgmc.util.RenderHelper;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BulletProofArmor extends ArmorItem implements IPMCArmor {

    private final float damageMultiplier;
    private int fgx, fgy, bgx, bgy;

    public BulletProofArmor(String name, float damageMultiplier, IArmorMaterial material, EquipmentSlotType slotType, Properties properties) {
        super(material, slotType, properties);
        this.setRegistryName(Pubgmc.makeResource(name));
        this.damageMultiplier = damageMultiplier;
    }

    public BulletProofArmor textureMap(int fgX, int fgY, int bgX, int bgY) {
        this.fgx = fgX;
        this.fgy = fgY;
        this.bgx = bgX;
        this.bgy = bgY;
        return this;
    }

    @Override
    public float damageMultiplier() {
        return damageMultiplier;
    }

    @Override
    public EquipmentSlotType armorSlot() {
        return slot;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void renderIcon(int x, int y, int x2, int y2, ItemStack stack) {
        float damage = stack.getDamage() / (float) stack.getMaxDamage();
        RenderHelper.color_x32Blit(x, y, x2, y2, bgx, bgy, 1, 1, 1.0F, 1.0F - damage, 1.0F - damage, 1.0F);
        RenderHelper.x32Blit(x, y, x2, y2, fgx, fgy, 1, 1);
    }
}
