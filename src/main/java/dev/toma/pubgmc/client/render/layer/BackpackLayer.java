package dev.toma.pubgmc.client.render.layer;

import dev.toma.pubgmc.capability.PMCInventoryHandler;
import dev.toma.pubgmc.capability.player.InventoryFactory;
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
            backpack.cubeList.add(new ModelBox(backpack, 5, 10, 2.0F, 4.6F, 0.0F, 4, 2, 2, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 12, 17, 2.5F, 6.466F, 0.0F, 3, 1, 2, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 14, 12, 2.5F, 1.6F, 0.5F, 3, 1, 1, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 10, 9, 3.0F, 0.6F, 0.0F, 2, 4, 2, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 10, 14, 2.5F, 5.08F, 1.5F, 3, 1, 1, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 39, 52, 3.5F, 2.35F, 1.25F, 1, 1, 1, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 12, 9, 0.75F, -0.05F, -4.0F, 1, 1, 4, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 12, 9, 6.25F, -0.05F, -4.0F, 1, 1, 4, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 6, 10, 6.25F, 0.0F, -4.01F, 1, 8, 0, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 6, 10, 0.75F, 0.0F, -4.01F, 1, 8, 0, 0.0F, false));

            RendererModel bone = new RendererModel(this);
            bone.setRotationPoint(4.0F, 5.1F, 1.0F);
            backpack.addChild(bone);
            setRotationAngle(bone, 0.0F, 0.0F, -0.2182F);
            bone.cubeList.add(new ModelBox(bone, 12, 11, 1.0608F, -4.0553F, -1.001F, 1, 4, 2, 0.0F, false));

            RendererModel bone4 = new RendererModel(this);
            bone4.setRotationPoint(4.0F, 5.1F, 1.0F);
            backpack.addChild(bone4);
            setRotationAngle(bone4, 0.0F, 0.0F, 0.5236F);
            bone4.cubeList.add(new ModelBox(bone4, 19, 10, 1.4821F, 0.299F, -1.001F, 1, 1, 2, 0.0F, false));

            RendererModel bone5 = new RendererModel(this);
            bone5.setRotationPoint(4.0F, 5.1F, 1.0F);
            backpack.addChild(bone5);
            setRotationAngle(bone5, 0.0F, 0.0F, -0.5236F);
            bone5.cubeList.add(new ModelBox(bone5, 15, 9, -2.4821F, 0.299F, -1.001F, 1, 1, 2, 0.0F, true));

            RendererModel bone2 = new RendererModel(this);
            bone2.setRotationPoint(4.0F, 5.1F, 1.0F);
            backpack.addChild(bone2);
            setRotationAngle(bone2, 0.0F, 0.0F, 0.2182F);
            bone2.cubeList.add(new ModelBox(bone2, 18, 14, -2.0608F, -4.0553F, -1.001F, 1, 4, 2, 0.0F, true));

            RendererModel bone3 = new RendererModel(this);
            bone3.setRotationPoint(4.0F, 5.16F, 2.17F);
            backpack.addChild(bone3);
            setRotationAngle(bone3, -0.3491F, 0.0F, 0.0F);
            bone3.cubeList.add(new ModelBox(bone3, 12, 12, -1.5F, -1.0F, -0.5F, 3, 1, 1, 0.0F, false));

            RendererModel left = new RendererModel(this);
            left.setRotationPoint(-2.0F, 1.3F, 0.1F);
            backpack.addChild(left);
            setRotationAngle(left, 0.0F, 0.0F, 0.3491F);
            left.cubeList.add(new ModelBox(left, 13, 9, 7.2555F, -4.0922F, -1.0F, 1, 4, 1, 0.0F, false));
            left.cubeList.add(new ModelBox(left, 16, 11, 5.3612F, 2.9825F, -1.0F, 1, 2, 1, 0.0F, false));

            RendererModel right = new RendererModel(this);
            right.setRotationPoint(10.0F, 1.3F, 0.1F);
            backpack.addChild(right);
            setRotationAngle(right, 0.0F, 0.0F, -0.3491F);
            right.cubeList.add(new ModelBox(right, 13, 9, -8.2555F, -4.0922F, -1.0F, 1, 4, 1, 0.0F, true));
            right.cubeList.add(new ModelBox(right, 16, 11, -6.3612F, 2.9825F, -1.0F, 1, 2, 1, 0.0F, true));
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
        private final RendererModel bone3;
        private final RendererModel bone4;
        private final RendererModel bone5;
        private final RendererModel bone6;
        private final RendererModel bone7;
        private final RendererModel bone8;
        private final RendererModel bb_main;

        public MediumBackpackModel() {
            textureWidth = 64;
            textureHeight = 64;

            backpack = new RendererModel(this);
            backpack.setRotationPoint(-4.0F, 0.0F, 2.0F);
            backpack.cubeList.add(new ModelBox(backpack, 11, 18, 6.0F, -0.5F, -4.0F, 1, 1, 4, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 27, 12, 6.0F, 0.366F, -4.5F, 1, 9, 1, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 17, 17, 6.0F, 0.366F, -0.5F, 1, 3, 1, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 11, 18, 1.0F, -0.5F, -4.0F, 1, 1, 4, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 27, 12, 1.0F, 0.366F, -4.5F, 1, 9, 1, 0.0F, false));
            backpack.cubeList.add(new ModelBox(backpack, 17, 17, 1.0F, 0.366F, -0.5F, 1, 3, 1, 0.0F, false));

            RendererModel bone = new RendererModel(this);
            bone.setRotationPoint(4.0F, 0.0F, -2.0F);
            backpack.addChild(bone);
            setRotationAngle(bone, -0.5236F, 0.0F, 0.0F);
            bone.cubeList.add(new ModelBox(bone, 16, 19, 2.0F, 0.567F, -1.9821F, 1, 1, 1, 0.0F, false));
            bone.cubeList.add(new ModelBox(bone, 16, 19, -3.0F, 0.567F, -1.9821F, 1, 1, 1, 0.0F, false));

            RendererModel bone2 = new RendererModel(this);
            bone2.setRotationPoint(4.0F, 0.0F, -2.0F);
            backpack.addChild(bone2);
            setRotationAngle(bone2, 0.5236F, 0.0F, 0.0F);
            bone2.cubeList.add(new ModelBox(bone2, 9, 16, 2.0F, 0.567F, 0.9821F, 1, 1, 1, 0.0F, false));
            bone2.cubeList.add(new ModelBox(bone2, 9, 16, -3.0F, 0.567F, 0.9821F, 1, 1, 1, 0.0F, false));

            bone3 = new RendererModel(this);
            bone3.setRotationPoint(-3.46F, 6.636F, 3.52F);
            setRotationAngle(bone3, 0.0F, 0.0F, -0.1309F);
            bone3.cubeList.add(new ModelBox(bone3, 12, 19, -0.5F, -1.5F, -1.0F, 1, 1, 2, 0.0F, true));

            bone4 = new RendererModel(this);
            bone4.setRotationPoint(3.46F, 6.636F, 3.52F);
            setRotationAngle(bone4, 0.0F, 0.0F, 0.1309F);
            bone4.cubeList.add(new ModelBox(bone4, 9, 11, -0.5F, -1.5F, -1.0F, 1, 1, 2, 0.0F, false));

            bone5 = new RendererModel(this);
            bone5.setRotationPoint(0.0F, 6.366F, 3.5F);
            setRotationAngle(bone5, 0.0F, 0.0F, 0.5236F);
            bone5.cubeList.add(new ModelBox(bone5, 14, 11, -4.0981F, -3.0981F, -1.502F, 1, 2, 3, 0.0F, false));

            bone6 = new RendererModel(this);
            bone6.setRotationPoint(0.0F, 6.366F, 3.5F);
            setRotationAngle(bone6, 0.0F, 0.0F, -0.5236F);
            bone6.cubeList.add(new ModelBox(bone6, 12, 12, 3.0981F, -3.0981F, -1.502F, 1, 2, 3, 0.0F, true));

            bone7 = new RendererModel(this);
            bone7.setRotationPoint(4.01F, 9.5F, 5.95F);
            setRotationAngle(bone7, -0.1309F, 0.0F, 0.0F);
            bone7.cubeList.add(new ModelBox(bone7, 11, 21, -6.0F, -4.6538F, -1.4026F, 4, 1, 1, 0.0F, false));
            bone7.cubeList.add(new ModelBox(bone7, 7, 17, -4.53F, -7.1324F, -1.7289F, 2, 1, 1, 0.0F, false));

            bone8 = new RendererModel(this);
            bone8.setRotationPoint(-0.59F, 5.876F, 3.59F);
            setRotationAngle(bone8, 0.0F, 0.0F, 0.2182F);
            bone8.cubeList.add(new ModelBox(bone8, 38, 38, -3.0F, -3.0F, -0.5F, 1, 2, 2, 0.0F, false));

            bb_main = new RendererModel(this);
            bb_main.setRotationPoint(0.0F, 24.0F, 0.0F);
            bb_main.cubeList.add(new ModelBox(bb_main, 13, 10, -2.0F, -22.366F, 1.999F, 4, 2, 3, 0.0F, false));
            bb_main.cubeList.add(new ModelBox(bb_main, 13, 17, 3.0F, -18.634F, 2.5F, 1, 3, 2, 0.0F, false));
            bb_main.cubeList.add(new ModelBox(bb_main, 10, 19, -4.0F, -18.634F, 2.5F, 1, 3, 2, 0.0F, true));
            bb_main.cubeList.add(new ModelBox(bb_main, 7, 16, -3.0F, -20.634F, 2.0F, 6, 6, 3, 0.0F, false));
            bb_main.cubeList.add(new ModelBox(bb_main, 7, 18, -2.0F, -19.134F, 5.0F, 4, 4, 1, 0.0F, false));
            bb_main.cubeList.add(new ModelBox(bb_main, 20, 14, -0.51F, -21.634F, 5.0F, 2, 2, 1, 0.0F, false));
            bb_main.cubeList.add(new ModelBox(bb_main, 36, 54, -0.2F, -23.0F, 2.799F, 2, 1, 2, 0.0F, false));
        }

        @Override
        public void render(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch, float scale) {
            backpack.render(scale);
            bone3.render(scale);
            bone4.render(scale);
            bone5.render(scale);
            bone6.render(scale);
            bone7.render(scale);
            bone8.render(scale);
            bb_main.render(scale);
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
