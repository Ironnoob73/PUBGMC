package dev.toma.pubgmc.common.block.crafting;

import dev.toma.pubgmc.Registry;
import net.minecraft.util.Direction;

public class AmmoFactoryBlock extends AbstractFactoryBlock {

    public AmmoFactoryBlock(String name) {
        super(name);
    }

    @Override
    public void createStructureObject(StructureObject builder) {
        builder.addPart(() -> Registry.PMCBlocks.AMMO_FACTORY.getDefaultState().with(PART, FactoryPart.PRODUCER), Direction.UP);
    }
}
