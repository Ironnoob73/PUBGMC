package dev.toma.pubgmc.common.item.gun.attachment;

import dev.toma.pubgmc.client.ScopeInfo;
import dev.toma.pubgmc.common.item.PMCItem;

public class AttachmentItem extends PMCItem {

    public static final AttachmentItem EMPTY = new AttachmentItem("null", null);
    private final AttachmentCategory category;

    public AttachmentItem(String name, AttachmentCategory category) {
        super(name, new Properties().maxStackSize(1).group(UTILITY));
        this.category = category;
    }

    public boolean isEmpty(AttachmentItem item) {
        return item == EMPTY;
    }

    public float getInaccuracyModifier() {
        return 1.0F;
    }

    public float getVerticalRecoilMultiplier() {
        return 1.0F;
    }

    public float getHorizontalRecoilMultiplier() {
        return 1.0F;
    }

    public boolean isSilent() {
        return false;
    }

    public boolean isQuickdraw() {
        return false;
    }

    public boolean isExtended() {
        return false;
    }

    public float getAimSpeedMultiplier() {
        return 1.0F;
    }

    public ScopeInfo getScopeInfo() {
        return null;
    }

    public AttachmentCategory getCategory() {
        return category;
    }

    public static class Barrel extends AttachmentItem {

        private final float vertical, horizontal, inaccuracy;
        private final boolean silent;

        public Barrel(String name, float vertical, float horizontal, boolean silent, float inaccuracy) {
            super(name, AttachmentCategory.BARREL);
            this.vertical = vertical;
            this.horizontal = horizontal;
            this.silent = silent;
            this.inaccuracy = inaccuracy;
        }

        public Barrel(String name, float vertical, float horizontal, boolean silent) {
            this(name, vertical, horizontal, silent, 1.0F);
        }

        public Barrel(String name, boolean silent) {
            this(name, 1.0F, 1.0F, silent);
        }

        @Override
        public float getInaccuracyModifier() {
            return inaccuracy;
        }

        @Override
        public float getVerticalRecoilMultiplier() {
            return vertical;
        }

        @Override
        public float getHorizontalRecoilMultiplier() {
            return horizontal;
        }

        @Override
        public boolean isSilent() {
            return silent;
        }
    }

    public static class Grip extends AttachmentItem {

        private final float vertical, horizontal;

        public Grip(String name, float vertical, float horizontal) {
            super(name, AttachmentCategory.GRIP);
            this.vertical = vertical;
            this.horizontal = horizontal;
        }

        @Override
        public float getVerticalRecoilMultiplier() {
            return vertical;
        }

        @Override
        public float getHorizontalRecoilMultiplier() {
            return horizontal;
        }
    }

    public static class Magazine extends AttachmentItem {

        private final boolean quickdraw, extended;

        public Magazine(String name, boolean quickdraw, boolean extended) {
            super(name, AttachmentCategory.MAGAZINE);
            this.quickdraw = quickdraw;
            this.extended = extended;
        }

        @Override
        public boolean isQuickdraw() {
            return quickdraw;
        }

        @Override
        public boolean isExtended() {
            return extended;
        }
    }

    public static class Stock extends AttachmentItem {

        private final boolean quickdraw;
        private final float aimSpeedMultiplier;

        public Stock(String name, boolean quickdraw, float aimSpeedMultiplier) {
            super(name, AttachmentCategory.STOCK);
            this.quickdraw = quickdraw;
            this.aimSpeedMultiplier = aimSpeedMultiplier;
        }

        @Override
        public float getAimSpeedMultiplier() {
            return aimSpeedMultiplier;
        }

        @Override
        public boolean isQuickdraw() {
            return quickdraw;
        }
    }

    public static class Scope extends AttachmentItem {

        private final ScopeInfo info;

        public Scope(String name, ScopeInfo info) {
            super(name, AttachmentCategory.SCOPE);
            this.info = info;
        }

        @Override
        public ScopeInfo getScopeInfo() {
            return this.info;
        }
    }
}
