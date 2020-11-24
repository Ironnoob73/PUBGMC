package dev.toma.pubgmc.util;

import dev.toma.pubgmc.common.item.gun.core.AbstractGunItem;
import dev.toma.pubgmc.common.item.gun.attachment.AttachmentCategory;
import dev.toma.pubgmc.common.item.gun.attachment.AttachmentItem;
import dev.toma.pubgmc.common.item.gun.attachment.GunAttachments;
import dev.toma.pubgmc.init.PMCItems;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.StringNBT;

public class AttachmentHelper {

    public static ItemStack attach(AbstractGunItem item, ItemStack weapon, ItemStack stack) {
        if(stack.getItem() instanceof AttachmentItem) {
            AttachmentItem attachmentItem = (AttachmentItem) stack.getItem();
            AttachmentCategory category = attachmentItem.getCategory();
            GunAttachments attachments = item.getAttachmentList();
            if(attachments.canAttach(attachmentItem)) {
                ItemStack prev = isEmpty(item, weapon, attachmentItem.getCategory()) ? ItemStack.EMPTY : new ItemStack(get(item, weapon, category));
                CompoundNBT nbt = weapon.getTag().getCompound("attachments");
                String key = "" + category.ordinal();
                nbt.put(key, new StringNBT(attachmentItem.getRegistryName().toString()));
                return prev;
            }
        }
        return stack;
    }

    public static boolean hasCompensator(AbstractGunItem item, ItemStack stack) {
        return get(item, stack, AttachmentCategory.BARREL).getVerticalRecoilMultiplier() < 1.0F;
    }

    public static boolean hasSilencer(AbstractGunItem item, ItemStack stack) {
        return get(item, stack, AttachmentCategory.BARREL).isSilent();
    }

    public static boolean hasVerticalGrip(AbstractGunItem item, ItemStack stack) {
        return has(PMCItems.VERTICAL_GRIP, item, stack);
    }

    public static boolean hasAngledGrip(AbstractGunItem item, ItemStack stack) {
        return has(PMCItems.ANGLED_GRIP, item, stack);
    }

    public static boolean hasScope(AbstractGunItem item, ItemStack stack) {
        return !item.getAttachment(AttachmentCategory.SCOPE, stack).isEmpty();
    }

    public static boolean hasRedDot(AbstractGunItem item, ItemStack stack) {
        return has(PMCItems.RED_DOT, item, stack);
    }

    public static boolean hasHolographic(AbstractGunItem item, ItemStack stack) {
        return has(PMCItems.HOLOGRAPHIC, item, stack);
    }

    public static boolean has2x(AbstractGunItem item, ItemStack stack) {
        return has(PMCItems.X2_SCOPE, item, stack);
    }

    public static boolean has3x(AbstractGunItem item, ItemStack stack) {
        return has(PMCItems.X3_SCOPE, item, stack);
    }

    public static boolean has4x(AbstractGunItem item, ItemStack stack) {
        return has(PMCItems.X4_SCOPE, item, stack);
    }

    public static boolean has6x(AbstractGunItem item, ItemStack stack) {
        return has(PMCItems.X6_SCOPE, item, stack);
    }

    public static boolean has8x(AbstractGunItem item, ItemStack stack) {
        return has(PMCItems.X8_SCOPE, item, stack);
    }

    public static boolean has15x(AbstractGunItem item, ItemStack stack) {
        return has(PMCItems.X15_SCOPE, item, stack);
    }

    public static boolean isBarrelSlotEmpty(AbstractGunItem gun, ItemStack stack) {
        return isEmpty(gun, stack, AttachmentCategory.BARREL);
    }

    public static boolean isGripSlotEmpty(AbstractGunItem gun, ItemStack stack) {
        return isEmpty(gun, stack, AttachmentCategory.GRIP);
    }

    public static boolean isMagazineSlotEmpty(AbstractGunItem gun, ItemStack stack) {
        return isEmpty(gun, stack, AttachmentCategory.MAGAZINE);
    }

    public static boolean isStockSlotEmpty(AbstractGunItem gun, ItemStack stack) {
        return isEmpty(gun, stack, AttachmentCategory.STOCK);
    }

    public static boolean isScopeSlotEmpty(AbstractGunItem gun, ItemStack stack) {
        return isEmpty(gun, stack, AttachmentCategory.SCOPE);
    }

    public static boolean isEmpty(AbstractGunItem item, ItemStack stack, AttachmentCategory category) {
        return item.getAttachment(category, stack).isEmpty();
    }

    public static AttachmentItem get(AbstractGunItem item, ItemStack stack, AttachmentCategory category) {
        return item.getAttachment(category, stack);
    }

    public static boolean has(AttachmentItem item, AbstractGunItem gun, ItemStack gunStack) {
        return gun.getAttachment(item.getCategory(), gunStack) == item;
    }
}
