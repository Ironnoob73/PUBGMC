package dev.toma.pubgmc.common.item.wearable;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.common.item.PMCItem;
import dev.toma.pubgmc.util.RenderHelper;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BulletProofArmor extends ArmorItem implements IPMCArmor {

    private final float damageMultiplier;
    private int fgx, fgy, bgx, bgy;

    public BulletProofArmor(String name, float damageMultiplier, IArmorMaterial material, EquipmentSlotType slotType) {
        super(material, slotType, new Properties().group(PMCItem.ITEMS).maxStackSize(1));
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

    @OnlyIn(Dist.CLIENT)
    @Override
    public void renderIcon(int x, int y, int x2, int y2, ItemStack stack) {
        float damage = stack.getDamage() / (float) stack.getMaxDamage();
        double step = 32 / 256.0D;
        double u = bgx * 32 / 256.0D;
        double v = bgy * 32 / 256.0D;
        RenderHelper.color_x32Blit(x, y, x2, y2, bgx, bgy, 1, 1, 1.0F, 1.0F - damage, 1.0F - damage, 1.0F);
        RenderHelper.x32Blit(x, y, x2, y2, fgx, fgy, 1, 1);
    }

    public enum Material implements IArmorMaterial {

        BASIC("basic", 150, new int[] {0, 0, 4, 2}, 0, 0.0F, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, Ingredient.fromStacks(ItemStack.EMPTY)),
        POLICE("police", 200, new int[] {0, 0, 8, 4}, 0, 0.0F, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, Ingredient.fromStacks(ItemStack.EMPTY)),
        MILITARY("military", 250, new int[] {0, 0, 12, 6}, 0, 0.0F, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, Ingredient.fromStacks(ItemStack.EMPTY));

        final String name;
        final int durability;
        final int[] reductions;
        final int enchantability;
        final float toughness;
        final SoundEvent event;
        final Ingredient ingredient;

        Material(String name, int durability, int[] reductions, int enchantability, float toughness, SoundEvent event, Ingredient repair) {
            this.name = name;
            this.durability = durability;
            this.reductions = reductions;
            this.enchantability = enchantability;
            this.toughness = toughness;
            this.event = event;
            this.ingredient = repair;
        }

        @Override
        public int getDurability(EquipmentSlotType slot) {
            return durability;
        }

        @Override
        public int getDamageReductionAmount(EquipmentSlotType slotIn) {
            return reductions[slotIn.getIndex()];
        }

        @Override
        public int getEnchantability() {
            return enchantability;
        }

        @Override
        public SoundEvent getSoundEvent() {
            return event;
        }

        @Override
        public Ingredient getRepairMaterial() {
            return ingredient;
        }

        @Override
        public String getName() {
            return Pubgmc.MODID + ":" + name;
        }

        @Override
        public float getToughness() {
            return toughness;
        }
    }
}
