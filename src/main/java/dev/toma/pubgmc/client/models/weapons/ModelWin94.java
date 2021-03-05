package dev.toma.pubgmc.client.models.weapons;

import dev.toma.pubgmc.animation.ReloadAnimation;
import dev.toma.pubgmc.animation.ReloadAnimation.ReloadStyle;
import dev.toma.pubgmc.client.util.ModelTransformationHelper;
import dev.toma.pubgmc.common.capability.player.IPlayerData;
import dev.toma.pubgmc.common.capability.player.PlayerDataProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;

public class ModelWin94 extends ModelGun {

    private final ModelRenderer bone;
    private final ModelRenderer bone2;
    private final ModelRenderer bone3;
    private final ModelRenderer bone4;
    private final ModelRenderer bone5;
    private final ModelRenderer bone6;
    private final ModelRenderer bone7;
    private final ModelRenderer bone8;
    private final ModelRenderer bone9;
    private final ModelRenderer bone10;
    private final ModelRenderer bone11;
    private final ModelRenderer bone12;
    private final ModelRenderer bone13;
    private final ModelRenderer bone17;
    private final ModelRenderer bone14;
    private final ModelRenderer bone15;
    private final ModelRenderer bone16;
    private final ModelRenderer barrel;
    private final ModelRenderer bone18;
    private final ModelRenderer barrel4;
    private final ModelRenderer bone21;
    private final ModelRenderer barrel5;
    private final ModelRenderer bone22;
    private final ModelRenderer barrel2;
    private final ModelRenderer bone19;
    private final ModelRenderer bone20;

    @Override
    public String textureName() {
        return "win94";
    }

    @Override
    public void initAnimations() {
        initAimAnimation(-0.5625f, 0.365f, 0.29f);
        initAimingAnimationStates(0.365f);
        reloadAnimation = new ReloadAnimation(null, ReloadStyle.SINGLE);
    }

    @Override
    public void render(ItemStack stack, ItemCameraTransforms.TransformType transformType) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        if (player != null && player.hasCapability(PlayerDataProvider.PLAYER_DATA, null)) {
            IPlayerData data = player.getCapability(PlayerDataProvider.PLAYER_DATA, null);

            GlStateManager.pushMatrix();
            {
                renderWin(data.isAiming());
            }
            GlStateManager.popMatrix();
        }
    }

    private void renderWin(boolean aim) {
        GlStateManager.pushMatrix();
        ModelTransformationHelper.defaultARTransform();
        GlStateManager.translate(-0.1, 0.85, -5.0);
        bone.render(1f);
        GlStateManager.popMatrix();
    }

    public ModelWin94() {
        super();
        textureWidth = 128;
        textureHeight = 128;

        bone = new ModelRenderer(this);
        bone.setRotationPoint(0.0F, 24.0F, 0.0F);
        bone.cubeList.add(new ModelBox(bone, 96, 38, -2.0F, -12.0F, -1.0F, 4, 2, 3, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 96, 38, 1.0F, -12.0F, 2.0F, 1, 2, 9, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 96, 38, -2.0F, -12.0F, 2.0F, 1, 2, 9, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 96, 38, 2.0F, -14.0F, -1.0F, 1, 3, 12, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 86, 39, -3.0F, -13.0F, -1.0F, 1, 2, 9, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 86, 39, -3.0F, -17.0F, 8.0F, 1, 6, 7, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 88, 36, -3.0F, -16.0F, -1.0F, 1, 3, 2, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 96, 38, 2.0F, -17.0F, -1.0F, 1, 3, 12, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 38, 42, -2.8906F, -16.0F, 0.5F, 1, 3, 8, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 15, 15, -2.9375F, -15.5F, 1.0F, 1, 2, 7, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 86, 39, -3.0F, -17.0F, -1.0F, 1, 1, 9, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 88, 36, -0.4142F, -18.4142F, -1.0F, 2, 1, 12, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 88, 43, -1.5858F, -18.4142F, -1.0F, 2, 1, 12, 0.0F, true));
        bone.cubeList.add(new ModelBox(bone, 88, 36, -2.0F, -14.0F, 11.0F, 4, 4, 4, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 104, 38, 2.0F, -14.0F, 11.0F, 1, 3, 4, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 93, 38, 1.0F, -17.0F, 11.0F, 2, 3, 4, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 86, 39, -0.4142F, -18.4142F, 11.0F, 2, 1, 4, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 86, 39, -1.5858F, -18.4142F, 11.0F, 2, 1, 4, 0.0F, true));
        bone.cubeList.add(new ModelBox(bone, 92, 38, -3.0F, -17.0F, -5.0F, 1, 5, 4, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 96, 38, 2.0F, -17.0F, -5.0F, 1, 5, 4, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 96, 38, -0.4142F, -18.4142F, -5.0F, 2, 1, 4, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 93, 42, -1.5858F, -18.4142F, -5.0F, 2, 1, 4, 0.0F, true));
        bone.cubeList.add(new ModelBox(bone, 96, 38, 2.0F, -12.5F, -5.0F, 1, 1, 4, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 88, 36, -3.0F, -12.5F, -5.0F, 1, 1, 4, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 96, 38, -2.0F, -11.5F, -5.0F, 4, 1, 4, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 33, 67, -2.5F, -17.2969F, -16.0F, 1, 6, 11, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 33, 67, 1.5F, -17.2969F, -16.0F, 1, 6, 11, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 33, 67, -1.5F, -18.2969F, -16.0F, 3, 1, 11, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 3, 73, -1.0F, -11.1563F, 2.0F, 2, 1, 9, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 82, 25, -1.0F, -18.633F, -3.5F, 2, 1, 18, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 33, 67, -1.5F, -11.2969F, -16.0F, 3, 1, 11, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 33, 67, -1.5F, -11.2969F, -27.0F, 3, 1, 11, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 33, 67, 1.5F, -17.2969F, -27.0F, 1, 6, 11, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 33, 67, -1.5F, -18.2969F, -27.0F, 3, 1, 11, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 33, 67, -2.5F, -17.2969F, -27.0F, 1, 6, 11, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 33, 67, -1.5F, -11.2969F, -39.0F, 3, 1, 11, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 33, 67, 1.5F, -17.2969F, -39.0F, 1, 6, 11, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 33, 67, -1.5F, -18.2969F, -39.0F, 3, 1, 11, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 33, 67, -2.5F, -17.2969F, -39.0F, 1, 6, 11, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 48, 12, -1.5F, -11.2969F, -28.0F, 3, 1, 1, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 48, 12, 1.5F, -17.2969F, -28.0F, 1, 6, 1, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 48, 12, -1.5F, -18.2969F, -28.0F, 3, 1, 1, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 48, 12, -2.5F, -17.2969F, -28.0F, 1, 6, 1, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 13, 73, -1.5F, -17.2969F, -33.3125F, 3, 6, 1, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 82, 29, -1.5F, -19.258F, 5.0313F, 3, 1, 4, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 82, 25, -1.0F, -18.633F, -21.5F, 2, 1, 18, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 82, 25, -1.0F, -18.633F, -28.5F, 2, 1, 7, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 92, 40, -1.0781F, -18.9611F, -1.75F, 2, 1, 7, 0.0F, true));
        bone.cubeList.add(new ModelBox(bone, 92, 40, -0.9219F, -18.9611F, -1.75F, 2, 1, 7, 0.0F, true));
        bone.cubeList.add(new ModelBox(bone, 84, 45, -1.5F, -18.5392F, 3.6875F, 3, 1, 1, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 84, 45, -1.5F, -18.5392F, 0.6875F, 3, 1, 1, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 84, 45, -1.5F, -18.5392F, -2.3125F, 3, 1, 1, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 82, 29, 0.25F, -19.7423F, 7.0313F, 1, 1, 1, 0.0F, true));
        bone.cubeList.add(new ModelBox(bone, 82, 29, -1.25F, -19.7423F, 7.0313F, 1, 1, 1, 0.0F, false));

        bone2 = new ModelRenderer(this);
        bone2.setRotationPoint(-3.5F, -12.5F, 5.0F);
        setRotationAngle(bone2, 0.0F, 0.0F, -0.7854F);
        bone.addChild(bone2);
        bone2.cubeList.add(new ModelBox(bone2, 86, 39, -0.7071F, 1.8284F, -6.0F, 1, 1, 12, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 86, 39, -0.7071F, 1.4142F, -6.0F, 1, 1, 12, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 96, 38, 2.5355F, 4.6569F, -6.0F, 1, 1, 12, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 96, 38, 2.1213F, 4.6569F, -6.0F, 1, 1, 12, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 92, 41, 2.1213F, 4.6569F, 6.0F, 1, 1, 4, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 88, 36, -0.7071F, 1.8284F, 6.0F, 1, 1, 4, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 86, 39, -0.7071F, 1.4142F, 6.0F, 1, 1, 4, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 101, 41, 2.5355F, 4.6569F, 6.0F, 1, 1, 4, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 96, 38, -0.3536F, 1.4749F, -10.0F, 1, 1, 4, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 96, 38, 2.4749F, 4.3033F, -10.0F, 1, 1, 4, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 96, 38, 2.8891F, 4.3033F, -10.0F, 1, 1, 4, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 96, 38, -0.3536F, 1.0607F, -10.0F, 1, 1, 4, 0.0F, false));

        bone3 = new ModelRenderer(this);
        bone3.setRotationPoint(-3.4375F, -14.5F, 2.5F);
        setRotationAngle(bone3, 0.0F, 0.3491F, 0.0F);
        bone.addChild(bone3);
        bone3.cubeList.add(new ModelBox(bone3, 15, 15, 0.4258F, -0.5F, -0.8902F, 1, 1, 2, 0.0F, false));

        bone4 = new ModelRenderer(this);
        bone4.setRotationPoint(-3.5F, -19.0F, 5.0F);
        setRotationAngle(bone4, 0.0F, 0.0F, 0.7854F);
        bone.addChild(bone4);
        bone4.cubeList.add(new ModelBox(bone4, 86, 39, 1.7678F, -0.9393F, -6.0F, 1, 2, 12, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 88, 34, 4.0104F, -3.182F, -6.0F, 2, 1, 12, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 88, 36, 4.0104F, -3.182F, 6.0F, 2, 2, 4, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 86, 39, 1.7678F, -0.9393F, 6.0F, 2, 2, 4, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 88, 36, 1.7678F, -0.9393F, -10.0F, 1, 2, 4, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 90, 42, 4.0104F, -3.182F, -10.0F, 2, 1, 4, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 33, 67, 4.4469F, -3.0383F, -21.0F, 1, 1, 11, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 33, 67, 1.9114F, -0.5028F, -21.0F, 1, 1, 11, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 33, 67, 4.0327F, -3.0383F, -21.0F, 1, 1, 11, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 33, 67, 1.9114F, -0.917F, -21.0F, 1, 1, 11, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 33, 67, 6.154F, 3.7398F, -21.0F, 1, 1, 11, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 33, 67, 6.5683F, 3.7398F, -21.0F, 1, 1, 11, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 33, 67, 8.6896F, 1.6185F, -21.0F, 1, 1, 11, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 33, 67, 8.6896F, 1.2043F, -21.0F, 1, 1, 11, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 33, 67, 6.154F, 3.7398F, -32.0F, 1, 1, 11, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 33, 67, 6.5683F, 3.7398F, -32.0F, 1, 1, 11, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 33, 67, 8.6896F, 1.6185F, -32.0F, 1, 1, 11, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 33, 67, 8.6896F, 1.2043F, -32.0F, 1, 1, 11, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 33, 67, 4.4469F, -3.0383F, -32.0F, 1, 1, 11, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 33, 67, 4.0327F, -3.0383F, -32.0F, 1, 1, 11, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 33, 67, 1.9114F, -0.917F, -32.0F, 1, 1, 11, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 33, 67, 1.9114F, -0.5028F, -32.0F, 1, 1, 11, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 33, 67, 6.154F, 3.7398F, -44.0F, 1, 1, 11, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 33, 67, 6.5683F, 3.7398F, -44.0F, 1, 1, 11, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 33, 67, 8.6896F, 1.6185F, -44.0F, 1, 1, 11, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 33, 67, 8.6896F, 1.2043F, -44.0F, 1, 1, 11, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 33, 67, 4.4469F, -3.0383F, -44.0F, 1, 1, 11, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 33, 67, 4.0327F, -3.0383F, -44.0F, 1, 1, 11, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 33, 67, 1.9114F, -0.917F, -44.0F, 1, 1, 11, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 33, 67, 1.9114F, -0.5028F, -44.0F, 1, 1, 11, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 48, 12, 6.154F, 3.7398F, -33.0F, 1, 1, 1, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 48, 12, 6.5683F, 3.7398F, -44.0F, 1, 1, 1, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 48, 12, 8.6896F, 1.6185F, -33.0F, 1, 1, 1, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 48, 12, 8.6896F, 1.2043F, -44.0F, 1, 1, 1, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 48, 12, 4.4469F, -3.0383F, -33.0F, 1, 1, 1, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 48, 12, 4.0327F, -3.0383F, -33.0F, 1, 1, 1, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 48, 12, 1.9114F, -0.917F, -33.0F, 1, 1, 1, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 48, 12, 1.9114F, -0.5028F, -44.0F, 1, 1, 1, 0.0F, false));

        bone5 = new ModelRenderer(this);
        bone5.setRotationPoint(0.0F, -17.6017F, 17.0F);
        setRotationAngle(bone5, -0.7854F, 0.0F, 0.0F);
        bone.addChild(bone5);
        bone5.cubeList.add(new ModelBox(bone5, 88, 43, -1.5F, 1.2706F, -1.5578F, 3, 1, 4, 0.0F, true));
        bone5.cubeList.add(new ModelBox(bone5, 104, 40, 1.2079F, 1.9777F, 0.1453F, 1, 1, 1, 0.0F, true));
        bone5.cubeList.add(new ModelBox(bone5, 88, 43, -2.2079F, 1.9777F, 0.1453F, 1, 1, 1, 0.0F, false));
        bone5.cubeList.add(new ModelBox(bone5, 12, 80, -0.5F, 1.0496F, -1.7007F, 1, 1, 1, 0.0F, true));
        bone5.cubeList.add(new ModelBox(bone5, 12, 80, -0.5F, 1.0496F, 0.2993F, 1, 1, 1, 0.0F, true));
        bone5.cubeList.add(new ModelBox(bone5, 12, 80, -1.0F, 0.9715F, -1.2007F, 2, 1, 2, 0.0F, true));
        bone5.cubeList.add(new ModelBox(bone5, 12, 80, -0.5F, 0.3934F, -0.4038F, 1, 1, 1, 0.0F, true));
        bone5.cubeList.add(new ModelBox(bone5, 12, 80, -0.5F, -0.3566F, -0.7788F, 1, 1, 1, 0.0F, true));
        bone5.cubeList.add(new ModelBox(bone5, 12, 80, -0.5F, 0.284F, -0.8726F, 1, 1, 1, 0.0F, true));

        bone6 = new ModelRenderer(this);
        bone6.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone6, 0.0F, 0.0F, 0.7854F);
        bone5.addChild(bone6);
        bone6.cubeList.add(new ModelBox(bone6, 88, 43, -0.1622F, 1.9591F, -1.5578F, 1, 1, 4, 0.0F, true));
        bone6.cubeList.add(new ModelBox(bone6, 90, 46, 1.9591F, -0.1622F, -1.5578F, 1, 1, 4, 0.0F, false));
        bone6.cubeList.add(new ModelBox(bone6, 103, 37, 2.7872F, -0.1622F, -1.761F, 1, 1, 2, 0.0F, false));
        bone6.cubeList.add(new ModelBox(bone6, 88, 43, -0.1622F, 2.7872F, -1.761F, 1, 1, 2, 0.0F, true));

        bone7 = new ModelRenderer(this);
        bone7.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone7, 0.0F, -0.5236F, 0.0F);
        bone.addChild(bone7);
        bone7.cubeList.add(new ModelBox(bone7, 96, 39, 9.0981F, -16.0F, 11.4904F, 1, 4, 1, 0.0F, false));

        bone8 = new ModelRenderer(this);
        bone8.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone8, 0.0F, 0.5236F, 0.0F);
        bone.addChild(bone8);
        bone8.cubeList.add(new ModelBox(bone8, 88, 43, -10.0981F, -16.0F, 11.4904F, 1, 4, 1, 0.0F, true));

        bone9 = new ModelRenderer(this);
        bone9.setRotationPoint(0.0F, -15.5469F, 16.4219F);
        setRotationAngle(bone9, -0.4363F, 0.0F, 0.0F);
        bone.addChild(bone9);
        bone9.cubeList.add(new ModelBox(bone9, 33, 75, -1.5F, -0.5625F, -1.9688F, 3, 5, 12, 0.0F, false));
        bone9.cubeList.add(new ModelBox(bone9, 38, 76, 1.2071F, 0.1446F, -1.9688F, 1, 2, 12, 0.0F, false));
        bone9.cubeList.add(new ModelBox(bone9, 37, 76, -2.2071F, 0.1446F, -1.9688F, 1, 2, 12, 0.0F, true));
        bone9.cubeList.add(new ModelBox(bone9, 34, 68, -2.207F, 1.7304F, -1.9687F, 1, 2, 12, 0.0F, true));
        bone9.cubeList.add(new ModelBox(bone9, 38, 75, 1.207F, 1.7304F, -1.9687F, 1, 2, 12, 0.0F, false));
        bone9.cubeList.add(new ModelBox(bone9, 36, 73, -1.5F, 3.4375F, 10.0313F, 3, 1, 10, 0.0F, false));
        bone9.cubeList.add(new ModelBox(bone9, 36, 73, -1.5F, 3.4375F, 20.0313F, 3, 1, 5, 0.0F, false));
        bone9.cubeList.add(new ModelBox(bone9, 32, 72, 1.207F, -0.2696F, 10.0313F, 1, 4, 15, 0.0F, false));
        bone9.cubeList.add(new ModelBox(bone9, 32, 74, -2.207F, -0.2696F, 10.0313F, 1, 4, 15, 0.0F, true));
        bone9.cubeList.add(new ModelBox(bone9, 32, 74, -2.207F, -1.2696F, 11.0313F, 1, 1, 14, 0.0F, true));
        bone9.cubeList.add(new ModelBox(bone9, 35, 68, -1.5F, -3.5625F, 24.0313F, 3, 7, 1, 0.0F, false));
        bone9.cubeList.add(new ModelBox(bone9, 32, 74, 1.207F, -1.2696F, 11.0313F, 1, 1, 14, 0.0F, false));
        bone9.cubeList.add(new ModelBox(bone9, 32, 74, -2.207F, -2.2696F, 12.0313F, 1, 1, 13, 0.0F, true));
        bone9.cubeList.add(new ModelBox(bone9, 32, 74, 1.207F, -2.2696F, 12.0313F, 1, 1, 13, 0.0F, false));
        bone9.cubeList.add(new ModelBox(bone9, 32, 74, -2.207F, -4.2696F, 19.0313F, 1, 2, 6, 0.0F, true));
        bone9.cubeList.add(new ModelBox(bone9, 32, 74, 1.207F, -4.2696F, 19.0313F, 1, 2, 6, 0.0F, false));
        bone9.cubeList.add(new ModelBox(bone9, 33, 75, -1.5001F, -0.9767F, 10.0313F, 3, 1, 1, 0.0F, false));
        bone9.cubeList.add(new ModelBox(bone9, 33, 75, -1.5001F, -1.9767F, 11.0313F, 3, 1, 1, 0.0F, false));
        bone9.cubeList.add(new ModelBox(bone9, 33, 75, -1.5F, -2.2696F, 12.0313F, 3, 1, 1, 0.0F, false));
        bone9.cubeList.add(new ModelBox(bone9, 33, 75, -1.4999F, -2.9767F, 12.0313F, 3, 1, 2, 0.0F, false));
        bone9.cubeList.add(new ModelBox(bone9, 35, 68, -1.5F, -4.5625F, 24.0313F, 3, 1, 1, 0.0F, false));
        bone9.cubeList.add(new ModelBox(bone9, 35, 68, -1.5F, -4.1094F, 24.3438F, 3, 3, 1, 0.0F, false));
        bone9.cubeList.add(new ModelBox(bone9, 35, 68, -1.5F, -1.4063F, 24.6094F, 3, 2, 1, 0.0F, false));
        bone9.cubeList.add(new ModelBox(bone9, 35, 68, -2.0F, 0.8906F, 24.6094F, 4, 2, 1, 0.0F, false));
        bone9.cubeList.add(new ModelBox(bone9, 35, 68, -2.0F, -1.1094F, 24.3438F, 4, 2, 1, 0.0F, false));
        bone9.cubeList.add(new ModelBox(bone9, 35, 68, -1.5F, 0.5822F, 24.7515F, 3, 2, 1, 0.0F, false));

        bone10 = new ModelRenderer(this);
        bone10.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone10, 0.0F, 0.0F, 0.7854F);
        bone9.addChild(bone10);
        bone10.cubeList.add(new ModelBox(bone10, 33, 67, 0.6629F, -1.4584F, -1.9688F, 1, 1, 12, 0.0F, false));
        bone10.cubeList.add(new ModelBox(bone10, 33, 67, -1.4584F, 0.6629F, -1.9687F, 1, 1, 12, 0.0F, false));
        bone10.cubeList.add(new ModelBox(bone10, 38, 74, 3.1984F, 1.0771F, -1.9687F, 1, 1, 12, 0.0F, false));
        bone10.cubeList.add(new ModelBox(bone10, 33, 67, 1.0771F, 3.1984F, -1.9687F, 1, 1, 12, 0.0F, false));
        bone10.cubeList.add(new ModelBox(bone10, 32, 71, 3.1984F, 1.0771F, 10.0313F, 1, 1, 15, 0.0F, false));
        bone10.cubeList.add(new ModelBox(bone10, 32, 69, 1.0771F, 3.1984F, 10.0313F, 1, 1, 15, 0.0F, false));
        bone10.cubeList.add(new ModelBox(bone10, 33, 67, 0.3699F, -1.7512F, 10.0313F, 1, 1, 1, 0.0F, false));
        bone10.cubeList.add(new ModelBox(bone10, 33, 67, -1.7514F, 0.3701F, 10.0313F, 1, 1, 1, 0.0F, false));
        bone10.cubeList.add(new ModelBox(bone10, 33, 67, -0.3372F, -2.4583F, 11.0313F, 1, 1, 1, 0.0F, false));
        bone10.cubeList.add(new ModelBox(bone10, 33, 67, -2.4585F, -0.337F, 11.0313F, 1, 1, 1, 0.0F, false));
        bone10.cubeList.add(new ModelBox(bone10, 33, 67, -3.1654F, -1.0443F, 12.0313F, 1, 1, 3, 0.0F, false));
        bone10.cubeList.add(new ModelBox(bone10, 33, 67, -1.0441F, -3.1656F, 12.0313F, 1, 1, 3, 0.0F, false));

        bone11 = new ModelRenderer(this);
        bone11.setRotationPoint(1.707F, -4.2696F, 19.0313F);
        setRotationAngle(bone11, 0.2967F, 0.0F, 0.0F);
        bone9.addChild(bone11);
        bone11.cubeList.add(new ModelBox(bone11, 32, 74, -0.5F, 0.0F, -7.0F, 1, 2, 13, 0.0F, false));
        bone11.cubeList.add(new ModelBox(bone11, 32, 74, -3.207F, 0.0F, 5.0313F, 3, 2, 1, 0.0F, true));

        bone12 = new ModelRenderer(this);
        bone12.setRotationPoint(-1.707F, -4.2696F, 19.0313F);
        setRotationAngle(bone12, 0.2967F, 0.0F, 0.0F);
        bone9.addChild(bone12);
        bone12.cubeList.add(new ModelBox(bone12, 32, 74, -0.5F, 0.0F, -7.0F, 1, 2, 13, 0.0F, true));

        bone13 = new ModelRenderer(this);
        bone13.setRotationPoint(-1.707F, -4.2696F, 19.0313F);
        setRotationAngle(bone13, 0.2967F, 0.0F, 0.0F);
        bone9.addChild(bone13);
        bone13.cubeList.add(new ModelBox(bone13, 32, 70, 0.207F, -0.2801F, -6.9144F, 3, 1, 13, 0.0F, true));

        bone17 = new ModelRenderer(this);
        bone17.setRotationPoint(0.0F, 3.9375F, 22.5313F);
        setRotationAngle(bone17, -0.3491F, 0.0F, 0.0F);
        bone9.addChild(bone17);
        bone17.cubeList.add(new ModelBox(bone17, 36, 73, -2.0F, -2.375F, -0.4375F, 4, 2, 3, 0.0F, false));

        bone14 = new ModelRenderer(this);
        bone14.setRotationPoint(0.0F, -3.4531F, 6.375F);
        setRotationAngle(bone14, -0.5236F, 0.0F, 0.0F);
        bone.addChild(bone14);
        bone14.cubeList.add(new ModelBox(bone14, 32, 38, -1.0F, -9.875F, 5.0F, 2, 1, 11, 0.0F, false));
        bone14.cubeList.add(new ModelBox(bone14, 32, 38, -1.0F, -7.1429F, 17.7321F, 2, 1, 1, 0.0F, false));
        bone14.cubeList.add(new ModelBox(bone14, 32, 38, -1.0F, -4.4109F, 5.0F, 2, 1, 11, 0.0F, false));
        bone14.cubeList.add(new ModelBox(bone14, 32, 38, -1.0F, -8.1845F, 3.3748F, 2, 4, 1, 0.0F, false));
        bone14.cubeList.add(new ModelBox(bone14, 32, 38, -1.0F, -5.1845F, 2.3748F, 2, 1, 1, 0.0F, false));
        bone14.cubeList.add(new ModelBox(bone14, 32, 38, -1.0F, -9.741F, 3.7679F, 2, 1, 1, 0.0F, false));
        bone14.cubeList.add(new ModelBox(bone14, 32, 38, -1.0F, -4.3185F, -0.8573F, 2, 1, 2, 0.0F, false));
        bone14.cubeList.add(new ModelBox(bone14, 32, 38, -1.0F, -8.7412F, -3.8325F, 2, 2, 1, 0.0F, false));

        bone15 = new ModelRenderer(this);
        bone15.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone15, -0.5236F, 0.0F, 0.0F);
        bone14.addChild(bone15);
        bone15.cubeList.add(new ModelBox(bone15, 32, 38, -1.0F, -16.552F, 8.9189F, 2, 1, 2, 0.0F, false));
        bone15.cubeList.add(new ModelBox(bone15, 32, 38, -1.0F, -14.686F, 12.151F, 2, 2, 1, 0.0F, false));
        bone15.cubeList.add(new ModelBox(bone15, 32, 38, -1.0F, -6.4539F, -0.3753F, 2, 1, 3, 0.0F, false));
        bone15.cubeList.add(new ModelBox(bone15, 32, 38, -1.0F, -11.186F, -1.1074F, 2, 1, 1, 0.0F, false));
        bone15.cubeList.add(new ModelBox(bone15, 32, 38, -1.0F, -7.4539F, -0.3753F, 2, 1, 1, 0.0F, false));
        bone15.cubeList.add(new ModelBox(bone15, 32, 38, -1.0F, -10.7754F, -1.1696F, 2, 2, 1, 0.0F, false));
        bone15.cubeList.add(new ModelBox(bone15, 32, 38, -1.0F, -3.4453F, -4.4017F, 2, 1, 2, 0.0F, false));

        bone16 = new ModelRenderer(this);
        bone16.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone16, -1.0472F, 0.0F, 0.0F);
        bone14.addChild(bone16);
        bone16.cubeList.add(new ModelBox(bone16, 32, 38, -1.0F, -19.7939F, 1.18F, 2, 1, 2, 0.0F, false));
        bone16.cubeList.add(new ModelBox(bone16, 32, 38, -1.0F, -17.5619F, 4.0461F, 2, 2, 1, 0.0F, false));
        bone16.cubeList.add(new ModelBox(bone16, 32, 38, -1.0F, -9.1337F, -6.552F, 2, 1, 1, 0.0F, false));
        bone16.cubeList.add(new ModelBox(bone16, 32, 38, -1.0F, -9.1428F, -5.9395F, 2, 1, 1, 0.0F, false));
        bone16.cubeList.add(new ModelBox(bone16, 32, 38, -1.0F, -6.4515F, -2.5989F, 2, 1, 1, 0.0F, false));
        bone16.cubeList.add(new ModelBox(bone16, 32, 38, -1.0F, -5.6489F, -3.3025F, 2, 3, 1, 0.0F, false));
        bone16.cubeList.add(new ModelBox(bone16, 32, 38, -1.0F, -0.9168F, -9.0346F, 2, 1, 4, 0.0F, false));

        barrel = new ModelRenderer(this);
        barrel.setRotationPoint(0.0F, -0.7031F, 0.0F);
        bone.addChild(barrel);
        barrel.cubeList.add(new ModelBox(barrel, 0, 9, -0.5F, -14.9531F, -48.3125F, 1, 1, 15, 0.0F, true));
        barrel.cubeList.add(new ModelBox(barrel, 0, 9, -1.2071F, -15.6602F, -48.3125F, 1, 1, 15, 0.0F, true));
        barrel.cubeList.add(new ModelBox(barrel, 0, 9, -0.5F, -16.3673F, -48.3125F, 1, 1, 15, 0.0F, true));
        barrel.cubeList.add(new ModelBox(barrel, 0, 9, 0.2071F, -15.6602F, -48.3125F, 1, 1, 15, 0.0F, true));

        bone18 = new ModelRenderer(this);
        bone18.setRotationPoint(0.0F, -14.4531F, -48.8125F);
        setRotationAngle(bone18, 0.0F, 0.0F, -0.7854F);
        barrel.addChild(bone18);
        bone18.cubeList.add(new ModelBox(bone18, 0, 9, -0.7071F, -1.0F, 0.5F, 1, 1, 15, 0.0F, true));
        bone18.cubeList.add(new ModelBox(bone18, 0, 9, 0.0F, -0.2929F, 0.5F, 1, 1, 15, 0.0F, true));
        bone18.cubeList.add(new ModelBox(bone18, 0, 9, 0.0F, -1.7071F, 0.5F, 1, 1, 15, 0.0F, true));
        bone18.cubeList.add(new ModelBox(bone18, 0, 9, 0.7071F, -1.0F, 0.5F, 1, 1, 15, 0.0F, true));

        barrel4 = new ModelRenderer(this);
        barrel4.setRotationPoint(0.0F, 2.0F, 0.0F);
        bone.addChild(barrel4);
        barrel4.cubeList.add(new ModelBox(barrel4, 0, 9, -0.5F, -14.9531F, -48.3125F, 1, 1, 15, 0.0F, true));
        barrel4.cubeList.add(new ModelBox(barrel4, 0, 9, -1.2071F, -15.6602F, -48.3125F, 1, 1, 15, 0.0F, true));
        barrel4.cubeList.add(new ModelBox(barrel4, 0, 9, -0.5F, -16.3673F, -48.3125F, 1, 1, 15, 0.0F, true));
        barrel4.cubeList.add(new ModelBox(barrel4, 0, 9, 0.2071F, -15.6602F, -48.3125F, 1, 1, 15, 0.0F, true));

        bone21 = new ModelRenderer(this);
        bone21.setRotationPoint(0.0F, -14.4531F, -48.8125F);
        setRotationAngle(bone21, 0.0F, 0.0F, -0.7854F);
        barrel4.addChild(bone21);
        bone21.cubeList.add(new ModelBox(bone21, 0, 9, -0.7071F, -1.0F, 0.5F, 1, 1, 15, 0.0F, true));
        bone21.cubeList.add(new ModelBox(bone21, 0, 9, 0.0F, -0.2929F, 0.5F, 1, 1, 15, 0.0F, true));
        bone21.cubeList.add(new ModelBox(bone21, 0, 9, 0.0F, -1.7071F, 0.5F, 1, 1, 15, 0.0F, true));
        bone21.cubeList.add(new ModelBox(bone21, 0, 9, 0.7071F, -1.0F, 0.5F, 1, 1, 15, 0.0F, true));

        barrel5 = new ModelRenderer(this);
        barrel5.setRotationPoint(0.0F, 2.0F, -15.0F);
        bone.addChild(barrel5);
        barrel5.cubeList.add(new ModelBox(barrel5, 0, 9, -0.5F, -14.9531F, -48.3125F, 1, 1, 15, 0.0F, true));
        barrel5.cubeList.add(new ModelBox(barrel5, 0, 9, -1.2071F, -15.6602F, -48.3125F, 1, 1, 15, 0.0F, true));
        barrel5.cubeList.add(new ModelBox(barrel5, 0, 9, -0.5F, -16.3673F, -48.3125F, 1, 1, 15, 0.0F, true));
        barrel5.cubeList.add(new ModelBox(barrel5, 0, 9, 0.2071F, -15.6602F, -48.3125F, 1, 1, 15, 0.0F, true));

        bone22 = new ModelRenderer(this);
        bone22.setRotationPoint(0.0F, -14.4531F, -48.8125F);
        setRotationAngle(bone22, 0.0F, 0.0F, -0.7854F);
        barrel5.addChild(bone22);
        bone22.cubeList.add(new ModelBox(bone22, 0, 9, -0.7071F, -1.0F, 0.5F, 1, 1, 15, 0.0F, true));
        bone22.cubeList.add(new ModelBox(bone22, 0, 9, 0.0F, -0.2929F, 0.5F, 1, 1, 15, 0.0F, true));
        bone22.cubeList.add(new ModelBox(bone22, 0, 9, 0.0F, -1.7071F, 0.5F, 1, 1, 15, 0.0F, true));
        bone22.cubeList.add(new ModelBox(bone22, 0, 9, 0.7071F, -1.0F, 0.5F, 1, 1, 15, 0.0F, true));

        barrel2 = new ModelRenderer(this);
        barrel2.setRotationPoint(0.0F, -0.7031F, -15.0F);
        bone.addChild(barrel2);
        barrel2.cubeList.add(new ModelBox(barrel2, 0, 9, -0.5F, -14.9531F, -48.3125F, 1, 1, 15, 0.0F, true));
        barrel2.cubeList.add(new ModelBox(barrel2, 0, 9, -1.2071F, -15.6602F, -48.3125F, 1, 1, 15, 0.0F, true));
        barrel2.cubeList.add(new ModelBox(barrel2, 0, 9, -0.5F, -16.3673F, -48.3125F, 1, 1, 15, 0.0F, true));
        barrel2.cubeList.add(new ModelBox(barrel2, 0, 9, 0.2071F, -15.6602F, -48.3125F, 1, 1, 15, 0.0F, true));
        barrel2.cubeList.add(new ModelBox(barrel2, 22, 18, -1.0F, -14.6602F, -42.3125F, 2, 2, 1, 0.0F, false));
        barrel2.cubeList.add(new ModelBox(barrel2, 0, 9, -1.0F, -16.8048F, -47.1875F, 2, 1, 2, 0.0F, true));
        barrel2.cubeList.add(new ModelBox(barrel2, 0, 9, -0.5F, -18.1017F, -47.25F, 1, 2, 1, 0.0F, true));
        barrel2.cubeList.add(new ModelBox(barrel2, 0, 9, -1.9063F, -18.8048F, -47.1875F, 1, 2, 2, 0.0F, true));
        barrel2.cubeList.add(new ModelBox(barrel2, 0, 9, 0.9063F, -18.8048F, -47.1875F, 1, 2, 2, 0.0F, false));
        barrel2.cubeList.add(new ModelBox(barrel2, 0, 9, -1.5F, -17.1017F, -47.1875F, 3, 1, 2, 0.0F, true));

        bone19 = new ModelRenderer(this);
        bone19.setRotationPoint(0.0F, -14.4531F, -48.8125F);
        setRotationAngle(bone19, 0.0F, 0.0F, -0.7854F);
        barrel2.addChild(bone19);
        bone19.cubeList.add(new ModelBox(bone19, 0, 9, -0.7071F, -1.0F, 0.5F, 1, 1, 15, 0.0F, true));
        bone19.cubeList.add(new ModelBox(bone19, 0, 9, 0.0F, -0.2929F, 0.5F, 1, 1, 15, 0.0F, true));
        bone19.cubeList.add(new ModelBox(bone19, 0, 9, 0.0F, -1.7071F, 0.5F, 1, 1, 15, 0.0F, true));
        bone19.cubeList.add(new ModelBox(bone19, 0, 9, 0.7071F, -1.0F, 0.5F, 1, 1, 15, 0.0F, true));

        bone20 = new ModelRenderer(this);
        bone20.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone20, 0.0F, 0.0F, -0.7854F);
        bone.addChild(bone20);
        bone20.cubeList.add(new ModelBox(bone20, 82, 29, 12.7832F, -15.1367F, 7.0313F, 1, 1, 1, 0.0F, true));
        bone20.cubeList.add(new ModelBox(bone20, 82, 29, 14.1367F, -13.7832F, 7.0313F, 1, 1, 1, 0.0F, true));
        initAnimations();
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
