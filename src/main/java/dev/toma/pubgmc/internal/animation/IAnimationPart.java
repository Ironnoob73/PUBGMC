package dev.toma.pubgmc.internal.animation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public interface IAnimationPart {

    List<IAnimationPart> PARTS = new ArrayList<>();
    IAnimationPart ITEM_AND_HANDS = new Hand("ITEM AND HANDS", "itemHand");
    IAnimationPart HANDS = new Hand("HANDS", "hands");
    IAnimationPart RIGHT_HAND = new Hand("RIGHT HAND", "rightHand");
    IAnimationPart LEFT_HAND = new Hand("LEFT HAND", "leftHand");
    IAnimationPart ITEM = new Hand("ITEM", "item");

    String getDisplayName();

    String getFunctionName();

    String parameters();

    boolean isSpecial();

    static void refresh() {
        PARTS.clear();
        PARTS.add(ITEM_AND_HANDS);
        PARTS.add(HANDS);
        PARTS.add(RIGHT_HAND);
        PARTS.add(LEFT_HAND);
        PARTS.add(ITEM);
    }

    class Hand implements IAnimationPart {

        final String displayName;
        final String funcName;

        Hand(String displayName, String name) {
            this.displayName = displayName;
            this.funcName = name;
            PARTS.add(this);
        }

        @Override
        public String getDisplayName() {
            return displayName;
        }

        @Override
        public String getFunctionName() {
            return funcName;
        }

        @Override
        public String parameters() {
            return "(f";
        }

        @Override
        public boolean isSpecial() {
            return false;
        }
    }

    class Model implements IAnimationPart {

        private final int index;

        public Model(int index) {
            this.index = index;
        }

        public Model addToList() {
            PARTS.add(this);
            return this;
        }

        @Override
        public String getDisplayName() {
            return "" + index;
        }

        @Override
        public String getFunctionName() {
            return "model";
        }

        @Override
        public String parameters() {
            return "((i, f)";
        }

        @Override
        public boolean isSpecial() {
            return true;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Model model = (Model) o;
            return index == model.index;
        }

        @Override
        public int hashCode() {
            return Objects.hash(index);
        }
    }
}
