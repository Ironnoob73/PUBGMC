package dev.toma.pubgmc.common.block.crafting;

import dev.toma.pubgmc.init.PMCBlocks;
import net.minecraft.util.Direction;

public class AmmoFactoryBlock extends AbstractFactoryBlock {

    public AmmoFactoryBlock(String name) {
        super(name);
    }

    @Override
    public void createStructureObject(StructureObject builder) {
        builder.addPart(() -> PMCBlocks.AMMO_FACTORY.getDefaultState().with(PART, FactoryPart.PRODUCER), Direction.UP);
    }
}
