package dev.toma.pubgmc.common.item.gun.core;

import dev.toma.pubgmc.common.item.gun.attachment.AttachmentCategory;
import dev.toma.pubgmc.common.item.gun.attachment.AttachmentItem;
import dev.toma.pubgmc.common.item.gun.attachment.GunAttachments;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class AttachmentBuilder<B extends AbstractGunBuilder<?>> {

    private B builder;
    private final Map<AttachmentCategory, Supplier<? extends AttachmentItem[]>> attachmentCategorySupplierMap = new HashMap<>();

    public AttachmentBuilder(B builder) {
        this.builder = builder;
    }

    public AttachmentBuilder<B> barrel(Supplier<AttachmentItem.Barrel[]> supplier) {
        return insert(AttachmentCategory.BARREL, supplier);
    }

    public AttachmentBuilder<B> grip(Supplier<AttachmentItem.Grip[]> supplier) {
        return insert(AttachmentCategory.GRIP, supplier);
    }

    public AttachmentBuilder<B> magazine(Supplier<AttachmentItem.Magazine[]> supplier) {
        return insert(AttachmentCategory.MAGAZINE, supplier);
    }

    public AttachmentBuilder<B> stock(Supplier<AttachmentItem.Stock[]> supplier) {
        return insert(AttachmentCategory.STOCK, supplier);
    }

    public AttachmentBuilder<B> scope(Supplier<AttachmentItem.Scope[]> supplier) {
        return insert(AttachmentCategory.SCOPE, supplier);
    }

    public B build() {
        builder.attachments = new GunAttachments(attachmentCategorySupplierMap);
        return builder;
    }

    private AttachmentBuilder<B> insert(AttachmentCategory category, Supplier<? extends AttachmentItem[]> supplier) {
        attachmentCategorySupplierMap.put(category, supplier);
        return this;
    }
}
