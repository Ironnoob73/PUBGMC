package dev.toma.pubgmc.client.render.layer;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.client.render.entity.EquipmentLayer;
import dev.toma.pubgmc.common.entity.EquipmentHolder;
import dev.toma.pubgmc.common.item.utility.GhillieSlotItem;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.function.Function;

public class GhillieLayer<E extends LivingEntity, M extends BipedModel<E>, A extends BipedModel<E>> extends EquipmentLayer<E, M> {

    private static final ResourceLocation MAIN_TEXTURE = Pubgmc.makeResource("textures/entity/layer/ghillie_main.png");
    private static final ResourceLocation OVERLAY_TEXTURE = Pubgmc.makeResource("textures/entity/layer/ghillie_overlay.png");
    private final A mainModel;
    private final A overlay;

    @SuppressWarnings("unchecked")
    public GhillieLayer(IEntityRenderer<E, M> renderer, Function<E, EquipmentHolder> inventory) {
        this(renderer, (A) new BipedModel<E>(1.2F), (A) new BipedModel<E>(1.3F), inventory);
    }

    public GhillieLayer(IEntityRenderer<E, M> renderer, A main, A overlay, Function<E, EquipmentHolder> inventory) {
        super(renderer, inventory);
        this.mainModel = main;
        this.overlay = overlay;
    }

    @Override
    public void render(E entityIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float headYaw, float headPitch, float scale) {
        EquipmentHolder holder = this.getInventory(entityIn);
        ItemStack stack = holder.getGhillie();
        if(!stack.isEmpty() && stack.getItem() instanceof GhillieSlotItem) {
            int color = ((GhillieSlotItem) stack.getItem()).getColor(stack);
            float red = ((color >> 16) & 0xff) / 255.0F;
            float green = ((color >> 8) & 0xff) / 255.0F;
            float blue = (color & 0xff) / 255.0F;
            getEntityModel().func_217148_a(mainModel);
            mainModel.setLivingAnimations(entityIn, limbSwing, limbSwingAmount, partialTicks);
            bindTexture(MAIN_TEXTURE);
            GlStateManager.color4f(red, green, blue, 1.0F);
            mainModel.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, headYaw, headPitch, scale);
            getEntityModel().func_217148_a(overlay);
            overlay.setLivingAnimations(entityIn, limbSwing, limbSwingAmount, partialTicks);
            bindTexture(OVERLAY_TEXTURE);
            overlay.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, headYaw, headPitch, scale);
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
