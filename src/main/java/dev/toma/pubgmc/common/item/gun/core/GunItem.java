package dev.toma.pubgmc.common.item.gun.core;

import dev.toma.pubgmc.common.item.gun.attachment.AttachmentCategory;
import dev.toma.pubgmc.util.function.Bool2ObjFunction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;

public class GunItem extends AbstractGunItem {

    protected final Bool2ObjFunction<SoundEvent> reloadSound;

    public GunItem(String name, Item.Properties properties, GunBuilder builder) {
        super(name, properties, builder);
        this.reloadSound = builder.reloadSound;
    }

    @Override
    public SoundEvent getReloadSound(ItemStack stack) {
        return reloadSound.apply(this.getAttachment(AttachmentCategory.MAGAZINE, stack).isQuickdraw());
    }
}
