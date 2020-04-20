package dev.toma.pubgmc.common.block.crafting;

import dev.toma.pubgmc.Registry;
import net.minecraft.util.Direction;

public class WeaponFactoryBlock extends AbstractFactoryBlock {

    public WeaponFactoryBlock(String name) {
        super(name);
    }

    @Override
    public void createStructureObject(StructureObject builder) {
        builder
                .addPart(() -> Registry.PMCBlocks.WEAPON_FACTORY.getDefaultState().with(PART, FactoryPart.STORAGE), Direction.WEST)
                .addPart(() -> Registry.PMCBlocks.WEAPON_FACTORY.getDefaultState().with(PART, FactoryPart.UI), Direction.UP)
                .addPart(() -> Registry.PMCBlocks.WEAPON_FACTORY.getDefaultState().with(PART, FactoryPart.PRODUCER), Direction.UP, Direction.WEST);
    }
}
