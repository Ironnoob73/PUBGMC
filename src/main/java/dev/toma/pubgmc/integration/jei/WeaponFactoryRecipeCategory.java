package dev.toma.pubgmc.integration.jei;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.init.PMCBlocks;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.util.ResourceLocation;

public class WeaponFactoryRecipeCategory extends FactoryCategory {

    final ResourceLocation uid = Pubgmc.makeResource("weapon_factory");

    public WeaponFactoryRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, PMCBlocks.WEAPON_FACTORY);
    }

    @Override
    public ResourceLocation getUid() {
        return uid;
    }
}
