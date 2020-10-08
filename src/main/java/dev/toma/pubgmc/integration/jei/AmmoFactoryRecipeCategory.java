package dev.toma.pubgmc.integration.jei;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.init.PMCBlocks;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.util.ResourceLocation;

public class AmmoFactoryRecipeCategory extends FactoryCategory {

    final ResourceLocation uid = Pubgmc.makeResource("ammo_factory");

    public AmmoFactoryRecipeCategory(IGuiHelper helper) {
        super(helper, PMCBlocks.AMMO_FACTORY);
    }

    @Override
    public ResourceLocation getUid() {
        return uid;
    }
}
