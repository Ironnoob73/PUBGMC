package dev.toma.pubgmc.util;

import dev.toma.pubgmc.common.item.gun.GunItem;
import dev.toma.pubgmc.common.item.gun.attachment.AttachmentCategory;
import dev.toma.pubgmc.common.item.gun.attachment.AttachmentItem;
import dev.toma.pubgmc.common.item.gun.attachment.GunAttachments;
import dev.toma.pubgmc.init.PMCItems;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.StringNBT;

public class AttachmentHelper {

    public static ItemStack attach(GunItem item, ItemStack weapon, ItemStack stack) {
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

    public static boolean hasCompensator(GunItem item, ItemStack stack) {
        return get(item, stack, AttachmentCategory.BARREL).getVerticalRecoilMultiplier() < 1.0F;
    }

    public static boolean hasSilencer(GunItem item, ItemStack stack) {
        return get(item, stack, AttachmentCategory.BARREL).isSilent();
    }

    public static boolean hasVerticalGrip(GunItem item, ItemStack stack) {
        return has(PMCItems.VERTICAL_GRIP, item, stack);
    }

    public static boolean hasAngledGrip(GunItem item, ItemStack stack) {
        return has(PMCItems.ANGLED_GRIP, item, stack);
    }

    public static boolean hasRedDot(GunItem item, ItemStack stack) {
        return has(PMCItems.RED_DOT, item, stack);
    }

    public static boolean hasHolographic(GunItem item, ItemStack stack) {
        return has(PMCItems.HOLOGRAPHIC, item, stack);
    }

    public static boolean has2x(GunItem item, ItemStack stack) {
        return has(PMCItems.X2_SCOPE, item, stack);
    }

    public static boolean has3x(GunItem item, ItemStack stack) {
        return has(PMCItems.X3_SCOPE, item, stack);
    }

    public static boolean has4x(GunItem item, ItemStack stack) {
        return has(PMCItems.X4_SCOPE, item, stack);
    }

    public static boolean has6x(GunItem item, ItemStack stack) {
        return has(PMCItems.X6_SCOPE, item, stack);
    }

    public static boolean has8x(GunItem item, ItemStack stack) {
        return has(PMCItems.X8_SCOPE, item, stack);
    }

    public static boolean has15x(GunItem item, ItemStack stack) {
        return has(PMCItems.X15_SCOPE, item, stack);
    }

    public static boolean isBarrelSlotEmpty(GunItem gun, ItemStack stack) {
        return isEmpty(gun, stack, AttachmentCategory.BARREL);
    }

    public static boolean isGripSlotEmpty(GunItem gun, ItemStack stack) {
        return isEmpty(gun, stack, AttachmentCategory.GRIP);
    }

    public static boolean isMagazineSlotEmpty(GunItem gun, ItemStack stack) {
        return isEmpty(gun, stack, AttachmentCategory.MAGAZINE);
    }

    public static boolean isStockSlotEmpty(GunItem gun, ItemStack stack) {
        return isEmpty(gun, stack, AttachmentCategory.STOCK);
    }

    public static boolean isScopeSlotEmpty(GunItem gun, ItemStack stack) {
        return isEmpty(gun, stack, AttachmentCategory.SCOPE);
    }

    public static boolean isEmpty(GunItem item, ItemStack stack, AttachmentCategory category) {
        return item.getAttachment(category, stack).isEmpty();
    }

    public static AttachmentItem get(GunItem item, ItemStack stack, AttachmentCategory category) {
        return item.getAttachment(category, stack);
    }

    public static boolean has(AttachmentItem item, GunItem gun, ItemStack gunStack) {
        return gun.getAttachment(item.getCategory(), gunStack) == item;
    }
}
