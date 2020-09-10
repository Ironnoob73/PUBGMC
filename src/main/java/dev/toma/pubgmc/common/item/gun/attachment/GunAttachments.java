package dev.toma.pubgmc.common.item.gun.attachment;

import dev.toma.pubgmc.util.UsefulFunctions;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class GunAttachments {

    private static final AttachmentItem[] EMPTY = new AttachmentItem[0];
    private Map<AttachmentCategory, Supplier<? extends AttachmentItem[]>> raw;
    private Map<AttachmentCategory, AttachmentItem[]> attachments;

    public GunAttachments() {
        this(new HashMap<>());
    }

    public GunAttachments(Map<AttachmentCategory, Supplier<? extends AttachmentItem[]>> raw) {
        this.raw = raw;
    }

    public boolean supports(AttachmentCategory category) {
        return get(category).length > 0;
    }

    public boolean canAttach(AttachmentItem item) {
        return UsefulFunctions.contains(this.get(item.getCategory()), item);
    }

    private AttachmentItem[] get(AttachmentCategory category) {
        if(attachments == null) {
            attachments = new HashMap<>();
            for(Map.Entry<AttachmentCategory, Supplier<? extends AttachmentItem[]>> entry : raw.entrySet()) {
                AttachmentItem[] array = entry.getValue().get();
                attachments.put(entry.getKey(), array);
            }
            raw = null;
        }
        return UsefulFunctions.getNonnullFromMap(attachments, category, EMPTY);
    }
}
