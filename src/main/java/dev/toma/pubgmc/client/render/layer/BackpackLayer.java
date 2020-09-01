package dev.toma.pubgmc.client.render.layer;

import dev.toma.pubgmc.capability.InventoryFactory;
import dev.toma.pubgmc.capability.PMCInventoryHandler;
import dev.toma.pubgmc.common.item.utility.BackpackSlotItem;
import dev.toma.pubgmc.common.item.utility.GhillieSlotItem;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

@SuppressWarnings({"unchecked", "rawtypes"})
public class BackpackLayer<T extends LivingEntity, M extends BipedModel<T>> extends LayerRenderer<T, M> {

    private final BipedModel[] models = {new SmallBackpackModel<T>(), new MediumBackpackModel<T>(), new LargeBackpackModel<T>()};

    public BackpackLayer(IEntityRenderer<T, M> renderer) {
        super(renderer);
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

    @Override
    public void render(T entityIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float headYaw, float headPitch, float scale) {
        if(entityIn instanceof PlayerEntity) {
            PMCInventoryHandler handler = InventoryFactory.getInventoryHandler((PlayerEntity) entityIn);
            ItemStack stack = handler.getStackInSlot(1);
            if(!stack.isEmpty()) {
                if(stack.getItem() instanceof GhillieSlotItem && ((GhillieSlotItem) stack.getItem()).blocksBackpackRender()) {
                    return;
                }
            }
            ItemStack itemStack = handler.getStackInSlot(2);
            if(itemStack.getItem() instanceof BackpackSlotItem) {
                BackpackSlotItem slotItem = (BackpackSlotItem) itemStack.getItem();
                BipedModel<T> model = getModel(slotItem);
                getEntityModel().func_217148_a(model);
                model.setLivingAnimations(entityIn, limbSwing, limbSwingAmount, partialTicks);
                bindTexture(slotItem.getBackpackTexture());
                model.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, headYaw, headPitch, scale);
            }
        }
    }

    private BipedModel<T> getModel(BackpackSlotItem slotItem) {
        return models[slotItem.getType().ordinal()];
    }

    static class SmallBackpackModel<T extends LivingEntity> extends BipedModel<T> {

        private final RendererModel backpack;

        public SmallBackpackModel() {
            textureWidth = 64;
            textureHeight = 64;
            backpack = new RendererModel(this);
            backpack.setRotationPoint(-4.0F, 0.0F, 2.0F);
            backpack.cubeList.add(new ModelBox(backpack, 0, 0, 2.0F, 2.6F, 0.0F, 4, 6, 2, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 12, 0, 2.5F, 5.35F, 1.5F, 3, 3, 1, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 12, 4, 3.5F, 3.35F, 1.25F, 1, 1, 1, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 20, 0, 0.75F, -0.05F, -4.0F, 1, 1, 4, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 20, 0, 6.25F, -0.05F, -4.0F, 1, 1, 4, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 31, 0, 6.25F, 0.0F, -4.01F, 1, 10, 0, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 31, 0, 0.75F, 0.0F, -4.01F, 1, 10, 0, 0.0F, false));
            RendererModel right = new RendererModel(this);
            right.setRotationPoint(-2.0F, 1.3F, 0.1F);
            backpack.addChild(right);
            setRotationAngle(right, 0.0F, 0.0F, -0.3491F);
            right.cubeList.add(new ModelBox(right, 0, 8, 3.0F, 0.0F, -1.0F, 1, 4, 1, 0.0F, false));
            RendererModel left = new RendererModel(this);
            left.setRotationPoint(-2.0F, 1.3F, 0.1F);
            backpack.addChild(left);
            setRotationAngle(left, 0.0F, 0.0F, 0.3491F);
            left.cubeList.add(new ModelBox(left, 0, 8, 7.2555F, -4.0922F, -1.0F, 1, 4, 1, 0.0F, false));
        }

        @Override
        public void render(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch, float scale) {
            backpack.render(scale);
        }

        public void setRotationAngle(RendererModel modelRenderer, float x, float y, float z) {
            modelRenderer.rotateAngleX = x;
            modelRenderer.rotateAngleY = y;
            modelRenderer.rotateAngleZ = z;
        }
    }

    static class MediumBackpackModel<T extends LivingEntity> extends BipedModel<T> {

        private final RendererModel backpack;

        public MediumBackpackModel() {
            textureWidth = 64;
            textureHeight = 64;
            backpack = new RendererModel(this);
            backpack.setRotationPoint(-4.0F, 0.0F, 2.0F);
            backpack.cubeList.add(new ModelBox(backpack, 0, 0, 1.0F, 4.6F, 0.0F, 6, 4, 4, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 0, 8, 1.0F, 0.0F, 0.0F, 6, 5, 2, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 25, 0, 0.75F, -0.05F, -4.0F, 1, 1, 4, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 25, 0, 6.25F, -0.05F, -4.0F, 1, 1, 4, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 24, 0, 6.25F, 0.0F, -4.01F, 1, 10, 0, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 24, 0, 0.75F, 0.0F, -4.01F, 1, 10, 0, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 0, 22, 7.0F, 3.6F, 0.0F, 1, 5, 3, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 0, 22, 0.0F, 3.6F, 0.0F, 1, 5, 3, 0.0F, false));
            RendererModel angle = new RendererModel(this);
            angle.setRotationPoint(0.0F, 1.05F, -0.8F);
            backpack.addChild(angle);
            setRotationAngle(angle, 0.3927F, 0.0F, 0.0F);
            angle.cubeList.add(new ModelBox(angle, 0, 15, 1.0F, 0.1307F, 1.0541F, 6, 5, 2, 0.0F, false));
            angle.cubeList.add(new ModelBox(angle, 16, 17, 3.5F, 3.4689F, 2.1492F, 1, 1, 1, 0.0F, false));
        }

        @Override
        public void render(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch, float scale) {
            backpack.render(scale);
        }

        public void setRotationAngle(RendererModel modelRenderer, float x, float y, float z) {
            modelRenderer.rotateAngleX = x;
            modelRenderer.rotateAngleY = y;
            modelRenderer.rotateAngleZ = z;
        }
    }

    static class LargeBackpackModel<T extends LivingEntity> extends BipedModel<T> {

        private final RendererModel backpack;

        public LargeBackpackModel() {
            textureWidth = 64;
            textureHeight = 64;
            backpack = new RendererModel(this);
            backpack.setRotationPoint(-4.0F, 0.0F, 2.0F);
            backpack.cubeList.add(new ModelBox(backpack, 0, 0, 0.0F, 0.0F, 0.0F, 8, 10, 4, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 24, 0, 0.5F, -0.05F, -4.0F, 1, 1, 4, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 24, 0, 6.5F, -0.05F, -4.0F, 1, 1, 4, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 24, 0, 6.5F, 0.0F, -4.01F, 1, 10, 0, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 24, 0, 0.5F, 0.0F, -4.01F, 1, 10, 0, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 0, 14, -1.0F, 5.75F, 0.5F, 1, 4, 3, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 0, 14, 8.0F, 5.75F, 0.5F, 1, 4, 3, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 0, 21, 0.5F, 8.5F, 3.25F, 7, 1, 1, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 0, 21, 0.5F, 6.5F, 3.25F, 7, 1, 1, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 0, 21, 0.5F, 4.5F, 3.25F, 7, 1, 1, 0.0F, false));
        }

        @Override
        public void render(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch, float scale) {
            backpack.render(scale);
        }

        public void setRotationAngle(RendererModel modelRenderer, float x, float y, float z) {
            modelRenderer.rotateAngleX = x;
            modelRenderer.rotateAngleY = y;
            modelRenderer.rotateAngleZ = z;
        }
    }
}
