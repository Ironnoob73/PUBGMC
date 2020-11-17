package dev.toma.pubgmc.client.model.gun;

import dev.toma.pubgmc.client.animation.Animations;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;
import net.minecraft.item.ItemStack;

public class SawedOffModel extends AbstractGunModel {

    private final RendererModel bone;
    private final RendererModel bone12;
    private final RendererModel bone13;
    private final RendererModel bone14;
    private final RendererModel bone6;
    private final RendererModel bone7;
    private final RendererModel bone8;
    private final RendererModel bone9;
    private final RendererModel bone10;
    private final RendererModel bone11;
    private final RendererModel bone5;
    private final RendererModel joint;
    private final RendererModel bone3;
    private final RendererModel bone2;
    private final RendererModel bone4;

    public SawedOffModel() {
        textureWidth = 128;
        textureHeight = 128;

        bone = new RendererModel(this);
        bone.setRotationPoint(0.0F, 24.0F, 0.0F);
        bone.cubeList.add(new ModelBox(bone, 64, 13, -5.0F, -4.0F, 8.0F, 10, 7, 6, 0.0F, true));
        bone.cubeList.add(new ModelBox(bone, 64, 13, -5.5F, -3.0F, 8.0F, 11, 6, 8, 0.0F, true));
        bone.cubeList.add(new ModelBox(bone, 64, 13, -5.0F, 3.0F, -1.0F, 10, 3, 15, 0.0F, true));
        bone.cubeList.add(new ModelBox(bone, 64, 13, 2.0F, 3.0F, -5.0F, 3, 3, 4, 0.0F, true));
        bone.cubeList.add(new ModelBox(bone, 64, 13, -5.0F, 3.0F, -5.0F, 3, 3, 4, 0.0F, true));
        bone.cubeList.add(new ModelBox(bone, 64, 13, -2.0F, -4.0F, 14.0F, 4, 1, 6, 0.0F, true));
        bone.cubeList.add(new ModelBox(bone, 64, 13, -2.0F, -5.0F, 10.0F, 4, 1, 2, 0.0F, true));
        bone.cubeList.add(new ModelBox(bone, 64, 13, 1.0F, -6.0F, 10.0F, 1, 1, 2, 0.0F, true));
        bone.cubeList.add(new ModelBox(bone, 64, 13, -2.0F, -6.0F, 10.0F, 1, 1, 2, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 64, 13, -5.0F, 2.0F, 14.0F, 10, 4, 7, 0.0F, true));

        bone12 = new RendererModel(this);
        bone12.setRotationPoint(-2.0F, -1.0F, 0.0F);
        bone.addChild(bone12);
        setRotationAngle(bone12, -0.0524F, 0.0F, 0.0F);
        bone12.cubeList.add(new ModelBox(bone12, 64, 13, 0.5F, -4.0426F, 19.8156F, 3, 2, 6, 0.0F, true));

        bone13 = new RendererModel(this);
        bone13.setRotationPoint(0.0F, -8.0F, 0.0F);
        bone12.addChild(bone13);
        setRotationAngle(bone13, -0.1745F, 0.0F, 0.0F);
        bone13.cubeList.add(new ModelBox(bone13, 64, 13, 1.0F, -0.5856F, 26.1106F, 2, 2, 6, 0.0F, true));

        bone14 = new RendererModel(this);
        bone14.setRotationPoint(1.0F, 0.0F, 0.0F);
        bone13.addChild(bone14);
        setRotationAngle(bone14, -0.7854F, 0.0F, 0.0F);
        bone14.cubeList.add(new ModelBox(bone14, 64, 13, 0.0F, -23.9645F, 21.3371F, 2, 2, 2, 0.0F, true));

        bone6 = new RendererModel(this);
        bone6.setRotationPoint(-5.0F, 0.0F, 0.0F);
        bone.addChild(bone6);
        setRotationAngle(bone6, -0.1745F, 0.0F, 0.0F);
        bone6.cubeList.add(new ModelBox(bone6, 64, 13, 0.0F, -6.7585F, 17.53F, 10, 9, 5, 0.0F, true));
        bone6.cubeList.add(new ModelBox(bone6, 0, 37, 1.0F, -6.7585F, 22.53F, 8, 9, 7, 0.0F, true));
        bone6.cubeList.add(new ModelBox(bone6, 0, 37, 0.0F, -5.7585F, 22.53F, 10, 7, 7, 0.0F, true));
        bone6.cubeList.add(new ModelBox(bone6, 0, 38, 1.0F, -6.7585F, 29.53F, 8, 9, 6, 0.0F, true));
        bone6.cubeList.add(new ModelBox(bone6, 0, 38, 0.0F, -5.7585F, 29.53F, 10, 7, 6, 0.0F, true));

        bone7 = new RendererModel(this);
        bone7.setRotationPoint(5.0F, -11.2585F, 32.53F);
        bone6.addChild(bone7);
        setRotationAngle(bone7, 0.0F, 0.0F, -0.7854F);
        bone7.cubeList.add(new ModelBox(bone7, 0, 38, -1.3536F, 6.4246F, -10.0F, 1, 1, 13, 0.0F, true));
        bone7.cubeList.add(new ModelBox(bone7, 0, 38, -6.3033F, 11.3744F, -10.0F, 1, 1, 13, 0.0F, true));
        bone7.cubeList.add(new ModelBox(bone7, 0, 38, -6.7175F, 11.3744F, -10.0F, 1, 1, 13, 0.0F, true));
        bone7.cubeList.add(new ModelBox(bone7, 0, 38, -1.3536F, 6.0104F, -10.0F, 1, 1, 13, 0.0F, true));
        bone7.cubeList.add(new ModelBox(bone7, 0, 38, -7.0104F, 0.3536F, -10.0F, 1, 1, 13, 0.0F, true));
        bone7.cubeList.add(new ModelBox(bone7, 0, 38, -7.4246F, 0.3536F, -10.0F, 1, 1, 13, 0.0F, true));
        bone7.cubeList.add(new ModelBox(bone7, 0, 38, -12.3744F, 5.3033F, -10.0F, 1, 1, 13, 0.0F, true));
        bone7.cubeList.add(new ModelBox(bone7, 0, 38, -12.3744F, 5.7175F, -10.0F, 1, 1, 13, 0.0F, true));

        bone8 = new RendererModel(this);
        bone8.setRotationPoint(-5.0F, 0.0F, 18.0F);
        bone.addChild(bone8);
        setRotationAngle(bone8, -0.6981F, 0.0F, 0.0F);
        bone8.cubeList.add(new ModelBox(bone8, 0, 37, 1.0F, -12.0479F, 13.6018F, 8, 9, 7, 0.0F, true));
        bone8.cubeList.add(new ModelBox(bone8, 0, 37, 0.0F, -11.0479F, 13.6018F, 10, 7, 7, 0.0F, true));
        bone8.cubeList.add(new ModelBox(bone8, 0, 38, 1.0F, -12.0479F, 20.6018F, 8, 9, 3, 0.0F, true));
        bone8.cubeList.add(new ModelBox(bone8, 0, 38, 0.0F, -11.0479F, 20.6018F, 10, 7, 3, 0.0F, true));

        bone9 = new RendererModel(this);
        bone9.setRotationPoint(5.0F, -11.2585F, 32.53F);
        bone8.addChild(bone9);
        setRotationAngle(bone9, 0.0F, 0.0F, -0.7854F);
        bone9.cubeList.add(new ModelBox(bone9, 0, 38, 2.3866F, 2.6845F, -18.9282F, 1, 1, 10, 0.0F, true));
        bone9.cubeList.add(new ModelBox(bone9, 0, 38, -2.5632F, 7.6342F, -18.9282F, 1, 1, 10, 0.0F, true));
        bone9.cubeList.add(new ModelBox(bone9, 0, 38, -2.9774F, 7.6342F, -18.9282F, 1, 1, 10, 0.0F, true));
        bone9.cubeList.add(new ModelBox(bone9, 0, 38, 2.3866F, 2.2703F, -18.9282F, 1, 1, 10, 0.0F, true));
        bone9.cubeList.add(new ModelBox(bone9, 0, 38, -3.2703F, -3.3866F, -18.9282F, 1, 1, 10, 0.0F, true));
        bone9.cubeList.add(new ModelBox(bone9, 0, 38, -3.6845F, -3.3866F, -18.9282F, 1, 1, 10, 0.0F, true));
        bone9.cubeList.add(new ModelBox(bone9, 0, 38, -8.6342F, 1.5632F, -18.9282F, 1, 1, 10, 0.0F, true));
        bone9.cubeList.add(new ModelBox(bone9, 0, 38, -8.6342F, 1.9774F, -18.9282F, 1, 1, 10, 0.0F, true));

        bone10 = new RendererModel(this);
        bone10.setRotationPoint(-5.0F, 0.0F, 17.0F);
        bone.addChild(bone10);
        setRotationAngle(bone10, -1.2217F, 0.0F, 0.0F);
        bone10.cubeList.add(new ModelBox(bone10, 0, 37, 1.0F, -23.1744F, 14.7579F, 8, 9, 7, 0.0F, true));
        bone10.cubeList.add(new ModelBox(bone10, 0, 37, 0.0F, -22.1744F, 14.7579F, 10, 7, 7, 0.0F, true));
        bone10.cubeList.add(new ModelBox(bone10, 0, 38, 1.0F, -23.1744F, 21.7579F, 8, 9, 3, 0.0F, true));
        bone10.cubeList.add(new ModelBox(bone10, 0, 38, 0.0F, -22.1744F, 21.7579F, 10, 7, 3, 0.0F, true));

        bone11 = new RendererModel(this);
        bone11.setRotationPoint(5.0F, -11.2585F, 32.53F);
        bone10.addChild(bone11);
        setRotationAngle(bone11, 0.0F, 0.0F, -0.7854F);
        bone11.cubeList.add(new ModelBox(bone11, 0, 38, 10.2542F, -5.1831F, -17.7721F, 1, 1, 10, 0.0F, true));
        bone11.cubeList.add(new ModelBox(bone11, 0, 38, 5.3045F, -0.2334F, -17.7721F, 1, 1, 10, 0.0F, true));
        bone11.cubeList.add(new ModelBox(bone11, 0, 38, 4.8902F, -0.2334F, -17.7721F, 1, 1, 10, 0.0F, true));
        bone11.cubeList.add(new ModelBox(bone11, 0, 38, 10.2542F, -5.5974F, -17.7721F, 1, 1, 10, 0.0F, true));
        bone11.cubeList.add(new ModelBox(bone11, 0, 38, 4.5974F, -11.2542F, -17.7721F, 1, 1, 10, 0.0F, true));
        bone11.cubeList.add(new ModelBox(bone11, 0, 38, 4.1831F, -11.2542F, -17.7721F, 1, 1, 10, 0.0F, true));
        bone11.cubeList.add(new ModelBox(bone11, 0, 38, -0.7666F, -6.3045F, -17.7721F, 1, 1, 10, 0.0F, true));
        bone11.cubeList.add(new ModelBox(bone11, 0, 38, -0.7666F, -5.8902F, -17.7721F, 1, 1, 10, 0.0F, true));

        bone5 = new RendererModel(this);
        bone5.setRotationPoint(0.0F, -7.0F, 17.0F);
        bone.addChild(bone5);
        setRotationAngle(bone5, -0.0873F, 0.0F, 0.0F);
        bone5.cubeList.add(new ModelBox(bone5, 64, 13, -5.0F, 3.2501F, -3.2729F, 10, 6, 5, 0.0F, true));

        joint = new RendererModel(this);
        joint.setRotationPoint(0.0F, 24.0F, 0.0F);
        joint.cubeList.add(new ModelBox(joint, 70, 11, 0.0F, -2.0F, -16.0F, 2, 2, 24, 0.0F, false));
        joint.cubeList.add(new ModelBox(joint, 10, 106, 1.75F, -2.8232F, 4.7626F, 4, 4, 2, 0.0F, false));
        joint.cubeList.add(new ModelBox(joint, 10, 106, -5.5F, -2.8232F, 4.7626F, 4, 4, 2, 0.0F, false));
        joint.cubeList.add(new ModelBox(joint, 79, 26, 0.0F, -2.0F, -30.0F, 2, 2, 14, 0.0F, false));
        joint.cubeList.add(new ModelBox(joint, 70, 11, -7.4641F, -2.0F, -16.0F, 2, 2, 24, 0.0F, false));
        joint.cubeList.add(new ModelBox(joint, 79, 26, -7.4641F, -2.0F, -30.0F, 2, 2, 14, 0.0F, false));
        joint.cubeList.add(new ModelBox(joint, 70, 11, -2.0F, -2.0F, -16.0F, 2, 2, 24, 0.0F, true));
        joint.cubeList.add(new ModelBox(joint, 79, 26, -2.0F, -2.0F, -30.0F, 2, 2, 14, 0.0F, true));
        joint.cubeList.add(new ModelBox(joint, 70, 11, 5.4641F, -2.0F, -16.0F, 2, 2, 24, 0.0F, true));
        joint.cubeList.add(new ModelBox(joint, 70, 11, -4.7321F, -4.7321F, -16.0F, 2, 2, 24, 0.0F, true));
        joint.cubeList.add(new ModelBox(joint, 79, 26, -4.7321F, -4.7321F, -30.0F, 2, 2, 14, 0.0F, true));
        joint.cubeList.add(new ModelBox(joint, 70, 11, 2.7321F, -4.7321F, -16.0F, 2, 2, 24, 0.0F, false));
        joint.cubeList.add(new ModelBox(joint, 79, 26, 2.7321F, -4.7321F, -30.0F, 2, 2, 14, 0.0F, false));
        joint.cubeList.add(new ModelBox(joint, 70, 11, -4.7321F, 0.7321F, -16.0F, 2, 2, 24, 0.0F, true));
        joint.cubeList.add(new ModelBox(joint, 79, 26, -4.7321F, 0.7321F, -30.0F, 2, 2, 14, 0.0F, true));
        joint.cubeList.add(new ModelBox(joint, 70, 11, 2.7321F, 0.7321F, -16.0F, 2, 2, 24, 0.0F, false));
        joint.cubeList.add(new ModelBox(joint, 79, 26, 2.7321F, 0.7321F, -30.0F, 2, 2, 14, 0.0F, false));
        joint.cubeList.add(new ModelBox(joint, 79, 26, 5.4641F, -2.0F, -30.0F, 2, 2, 14, 0.0F, true));
        joint.cubeList.add(new ModelBox(joint, 64, 13, -2.0F, 3.0F, -6.0F, 4, 3, 4, 0.0F, true));
        joint.cubeList.add(new ModelBox(joint, 64, 13, -5.0F, 3.0F, -10.0F, 10, 3, 4, 0.0F, true));
        joint.cubeList.add(new ModelBox(joint, 32, 67, -4.5F, 3.0F, -17.0F, 9, 2, 7, 0.0F, true));
        joint.cubeList.add(new ModelBox(joint, 32, 67, -4.5F, 3.0F, -24.0F, 9, 2, 7, 0.0F, true));
        joint.cubeList.add(new ModelBox(joint, 32, 67, -4.5F, 5.0F, -11.0F, 9, 2, 1, 0.0F, true));

        bone3 = new RendererModel(this);
        bone3.setRotationPoint(0.0F, 0.0F, 0.0F);
        joint.addChild(bone3);
        setRotationAngle(bone3, 0.0F, 0.0F, -0.5236F);
        bone3.cubeList.add(new ModelBox(bone3, 70, 11, -1.0F, -3.7321F, -16.0F, 2, 2, 24, 0.0F, true));
        bone3.cubeList.add(new ModelBox(bone3, 79, 26, -1.0F, -3.7321F, -30.0F, 2, 2, 14, 0.0F, true));
        bone3.cubeList.add(new ModelBox(bone3, 70, 11, 5.4641F, 0.0F, -16.0F, 2, 2, 24, 0.0F, true));
        bone3.cubeList.add(new ModelBox(bone3, 79, 26, 5.4641F, 0.0F, -30.0F, 2, 2, 14, 0.0F, true));
        bone3.cubeList.add(new ModelBox(bone3, 70, 11, 2.7321F, -2.7321F, -16.0F, 2, 2, 24, 0.0F, true));
        bone3.cubeList.add(new ModelBox(bone3, 79, 26, 2.7321F, -2.7321F, -30.0F, 2, 2, 14, 0.0F, true));
        bone3.cubeList.add(new ModelBox(bone3, 70, 11, -3.7321F, -6.4641F, -16.0F, 2, 2, 24, 0.0F, true));
        bone3.cubeList.add(new ModelBox(bone3, 79, 26, -3.7321F, -6.4641F, -30.0F, 2, 2, 14, 0.0F, true));
        bone3.cubeList.add(new ModelBox(bone3, 70, 11, -3.7321F, -1.0F, -16.0F, 2, 2, 24, 0.0F, true));
        bone3.cubeList.add(new ModelBox(bone3, 79, 26, -3.7321F, -1.0F, -30.0F, 2, 2, 14, 0.0F, true));
        bone3.cubeList.add(new ModelBox(bone3, 70, 11, 2.7321F, 2.7321F, -16.0F, 2, 2, 24, 0.0F, true));
        bone3.cubeList.add(new ModelBox(bone3, 79, 26, 2.7321F, 2.7321F, -30.0F, 2, 2, 14, 0.0F, true));
        bone3.cubeList.add(new ModelBox(bone3, 70, 11, 0.0F, 0.0F, -16.0F, 2, 2, 24, 0.0F, true));
        bone3.cubeList.add(new ModelBox(bone3, 79, 26, 0.0F, 0.0F, -30.0F, 2, 2, 14, 0.0F, true));
        bone3.cubeList.add(new ModelBox(bone3, 70, 11, -6.4641F, -3.7321F, -16.0F, 2, 2, 24, 0.0F, true));
        bone3.cubeList.add(new ModelBox(bone3, 79, 26, -6.4641F, -3.7321F, -30.0F, 2, 2, 14, 0.0F, true));

        bone2 = new RendererModel(this);
        bone2.setRotationPoint(0.0F, 0.0F, 0.0F);
        joint.addChild(bone2);
        setRotationAngle(bone2, 0.0F, 0.0F, 0.5236F);
        bone2.cubeList.add(new ModelBox(bone2, 70, 11, -1.0F, -3.7321F, -16.0F, 2, 2, 24, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 79, 26, -1.0F, -3.7321F, -30.0F, 2, 2, 14, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 70, 11, -7.4641F, 0.0F, -16.0F, 2, 2, 24, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 79, 26, -7.4641F, 0.0F, -30.0F, 2, 2, 14, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 70, 11, 1.7321F, -1.0F, -16.0F, 2, 2, 24, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 79, 26, 1.7321F, -1.0F, -30.0F, 2, 2, 14, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 70, 11, -4.7321F, 2.7321F, -16.0F, 2, 2, 24, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 79, 26, -4.7321F, 2.7321F, -30.0F, 2, 2, 14, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 70, 11, -4.7321F, -2.7321F, -16.0F, 2, 2, 24, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 79, 26, -4.7321F, -2.7321F, -30.0F, 2, 2, 14, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 70, 11, 1.7321F, -6.4641F, -16.0F, 2, 2, 24, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 79, 26, 1.7321F, -6.4641F, -30.0F, 2, 2, 14, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 70, 11, -2.0F, 0.0F, -16.0F, 2, 2, 24, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 79, 26, -2.0F, 0.0F, -30.0F, 2, 2, 14, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 70, 11, 4.4641F, -3.7321F, -16.0F, 2, 2, 24, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 79, 26, 4.4641F, -3.7321F, -30.0F, 2, 2, 14, 0.0F, false));

        bone4 = new RendererModel(this);
        bone4.setRotationPoint(0.0F, 6.0F, -13.5F);
        joint.addChild(bone4);
        setRotationAngle(bone4, -0.1745F, 0.0F, 0.0F);
        bone4.cubeList.add(new ModelBox(bone4, 32, 67, -4.5F, -1.4493F, -3.3643F, 9, 2, 6, 0.0F, true));
        bone4.cubeList.add(new ModelBox(bone4, 32, 67, -4.5F, -0.4493F, -9.3643F, 9, 1, 6, 0.0F, true));

        addAnimatedParts(joint);
    }

    @Override
    public void doModelRender(ItemStack stack) {
        bone.render(1.0f);
    }
}
