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
            backpack.cubeList.add(new ModelBox(backpack, 14, 14, 5.5F, -0.5F, -4.0F, 1, 1, 4, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 7, 11, 5.5F, 0.366F, -4.5F, 1, 9, 1, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 13, 18, 5.5F, 0.366F, -0.5F, 1, 10, 1, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 33, 50, 1.0F, 1.366F, 0.0F, 6, 10, 1, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 0, 8, 0.5F, 3.196F, 1.0F, 7, 8, 2, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 12, 14, 1.0F, 3.196F, 2.866F, 6, 8, 1, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 0, 8, 1.5F, 6.5F, 3.456F, 5, 4, 2, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 0, 8, 4.3F, 3.5F, 3.866F, 2, 2, 1, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 37, 52, 4.8F, 3.5F, 4.066F, 1, 1, 1, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 0, 8, 1.7F, 3.5F, 3.866F, 2, 2, 1, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 37, 55, 2.2F, 3.5F, 4.066F, 1, 1, 1, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 32, 38, 1.0F, 2.366F, 1.5F, 6, 3, 2, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 50, 35, 5.8F, 2.206F, 2.3F, 1, 1, 1, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 38, 51, 4.7F, 2.036F, 2.3F, 1, 1, 1, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 38, 38, 3.6F, 2.096F, 2.3F, 1, 1, 1, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 43, 40, 1.5F, -1.5F, 2.0F, 1, 4, 1, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 7, 13, 1.5F, -0.5F, -4.0F, 1, 1, 4, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 15, 13, 1.5F, 0.366F, -4.5F, 1, 9, 1, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 39, 60, 2.2F, 1.37F, -4.19F, 2, 1, 1, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 38, 54, 3.8F, 1.55F, -4.09F, 2, 1, 1, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 13, 19, 1.5F, 0.366F, -0.5F, 1, 10, 1, 0.0F, false));

            RendererModel bone5 = new RendererModel(this);
            bone5.setRotationPoint(3.97F, 6.296F, 4.366F);
            backpack.addChild(bone5);
            setRotationAngle(bone5, -0.1309F, 0.0F, 0.0F);
            bone5.cubeList.add(new ModelBox(bone5, 0, 8, -2.48F, 0.0256F, -0.8842F, 5, 1, 2, 0.0F, false));
            bone5.cubeList.add(new ModelBox(bone5, 0, 8, 0.33F, -2.8717F, -0.8607F, 2, 1, 1, 0.0F, false));
            bone5.cubeList.add(new ModelBox(bone5, 0, 8, -2.27F, -2.8717F, -0.8607F, 2, 1, 1, 0.0F, false));

            RendererModel bone3 = new RendererModel(this);
            bone3.setRotationPoint(4.0F, 6.866F, 2.0F);
            backpack.addChild(bone3);
            setRotationAngle(bone3, 0.0F, 0.5236F, 0.0F);
            bone3.cubeList.add(new ModelBox(bone3, 22, 9, 2.5311F, -3.67F, -0.116F, 1, 8, 1, 0.0F, false));
            bone3.cubeList.add(new ModelBox(bone3, 0, 8, -3.5311F, -3.67F, -0.884F, 1, 8, 1, 0.0F, false));

            RendererModel bone4 = new RendererModel(this);
            bone4.setRotationPoint(4.0F, 6.866F, 2.0F);
            backpack.addChild(bone4);
            setRotationAngle(bone4, 0.0F, -0.5236F, 0.0F);
            bone4.cubeList.add(new ModelBox(bone4, 8, 10, -3.5311F, -3.67F, -0.116F, 1, 8, 1, 0.0F, true));
            bone4.cubeList.add(new ModelBox(bone4, 21, 9, 2.5311F, -3.67F, -0.884F, 1, 8, 1, 0.0F, true));

            RendererModel bone = new RendererModel(this);
            bone.setRotationPoint(4.0F, 0.0F, -2.0F);
            backpack.addChild(bone);
            setRotationAngle(bone, -0.5236F, 0.0F, 0.0F);
            bone.cubeList.add(new ModelBox(bone, 17, 20, 1.499F, 0.567F, -1.9821F, 1, 1, 1, 0.0F, false));
            bone.cubeList.add(new ModelBox(bone, 13, 22, -2.499F, 0.567F, -1.9821F, 1, 1, 1, 0.0F, false));

            RendererModel bone2 = new RendererModel(this);
            bone2.setRotationPoint(4.0F, 0.0F, -2.0F);
            backpack.addChild(bone2);
            setRotationAngle(bone2, 0.5236F, 0.0F, 0.0F);
            bone2.cubeList.add(new ModelBox(bone2, 18, 18, 1.499F, 0.567F, 0.9821F, 1, 1, 1, 0.0F, false));
            bone2.cubeList.add(new ModelBox(bone2, 11, 14, -2.499F, 0.567F, 0.9821F, 1, 1, 1, 0.0F, false));
        }

        public void setRotationAngle(RendererModel modelRenderer, float x, float y, float z) {
            modelRenderer.rotateAngleX = x;
            modelRenderer.rotateAngleY = y;
            modelRenderer.rotateAngleZ = z;
        }

        @Override
        public void render(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch, float scale) {
            backpack.render(scale);
        }
    }
}
