package dev.toma.pubgmc.client.model.gun;

import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;
import net.minecraft.item.ItemStack;

public class P92Model extends AbstractGunModel {

    private final RendererModel p92;
    private final RendererModel bone16;
    private final RendererModel bone15;
    private final RendererModel bone;
    private final RendererModel bone11;
    private final RendererModel bone12;
    private final RendererModel bone13;
    private final RendererModel bone7;
    private final RendererModel bone8;
    private final RendererModel bone14;
    private final RendererModel bone9;
    private final RendererModel bone10;
    private final RendererModel bone4;
    private final RendererModel bone5;
    private final RendererModel bone6;
    private final RendererModel bone2;
    private final RendererModel bone3;
    private final RendererModel magazine;

    public void setRotationAngle(RendererModel modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public void doModelRender(ItemStack stack) {
        p92.render(1.0F);
    }

    public P92Model() {
        textureWidth = 128;
        textureHeight = 128;

        p92 = new RendererModel(this);
        p92.setRotationPoint(0.0F, 24.0F, 0.0F);
        p92.cubeList.add(new ModelBox(p92, 92, 22, -1.0F, -1.0F, -12.0F, 2, 1, 14, 0.0F, false));
        p92.cubeList.add(new ModelBox(p92, 92, 22, -1.0F, -1.0234F, 1.3984F, 2, 1, 1, 0.0F, false));
        p92.cubeList.add(new ModelBox(p92, 96, 41, -1.0F, -4.0F, 0.0F, 2, 3, 2, 0.0F, false));
        p92.cubeList.add(new ModelBox(p92, 96, 41, -2.0F, -4.0F, 2.0F, 4, 1, 6, 0.0F, false));
        p92.cubeList.add(new ModelBox(p92, 96, 41, -2.0F, -4.0F, 8.0F, 4, 3, 5, 0.0F, false));
        p92.cubeList.add(new ModelBox(p92, 96, 41, -1.0F, -0.124F, 3.4321F, 2, 1, 3, 0.0F, false));
        p92.cubeList.add(new ModelBox(p92, 96, 41, -2.0F, 3.5015F, 9.3825F, 4, 2, 5, 0.0F, false));
        p92.cubeList.add(new ModelBox(p92, 83, 16, 1.0F, -4.0F, -12.0F, 1, 3, 14, 0.0F, false));
        p92.cubeList.add(new ModelBox(p92, 53, 18, -1.0F, -3.0F, -11.6641F, 2, 2, 1, 0.0F, false));
        p92.cubeList.add(new ModelBox(p92, 88, 17, -2.0F, -4.0F, -12.0F, 1, 3, 14, 0.0F, false));
        p92.cubeList.add(new ModelBox(p92, 88, 17, -1.0F, -4.0F, -12.0F, 2, 1, 13, 0.0F, false));
        p92.cubeList.add(new ModelBox(p92, 33, 7, -2.0F, -5.5F, -12.4F, 4, 2, 9, 0.0F, false));
        p92.cubeList.add(new ModelBox(p92, 33, 7, -2.342F, -6.4397F, -12.4F, 1, 2, 13, 0.0F, false));
        p92.cubeList.add(new ModelBox(p92, 33, 7, -2.342F, -6.4397F, 0.6F, 1, 2, 13, 0.0F, false));
        p92.cubeList.add(new ModelBox(p92, 33, 7, 0.5F, -6.4397F, 12.6938F, 1, 2, 1, 0.0F, false));
        p92.cubeList.add(new ModelBox(p92, 33, 7, -1.5F, -6.4397F, 12.6938F, 1, 2, 1, 0.0F, true));
        p92.cubeList.add(new ModelBox(p92, 33, 7, -0.5F, -5.4397F, 12.6938F, 1, 1, 1, 0.0F, true));
        p92.cubeList.add(new ModelBox(p92, 33, 7, 1.342F, -6.4397F, -12.4F, 1, 2, 13, 0.0F, true));
        p92.cubeList.add(new ModelBox(p92, 33, 7, 1.342F, -6.4397F, 0.6F, 1, 2, 13, 0.0F, true));
        p92.cubeList.add(new ModelBox(p92, 33, 7, 1.342F, -8.4397F, 3.6F, 1, 2, 9, 0.0F, true));
        p92.cubeList.add(new ModelBox(p92, 33, 7, -1.4627F, -8.4397F, 3.6F, 3, 2, 1, 0.0F, true));
        p92.cubeList.add(new ModelBox(p92, 33, 7, -1.4627F, -8.967F, 3.6F, 3, 1, 9, 0.0F, true));
        p92.cubeList.add(new ModelBox(p92, 33, 7, -1.4627F, -8.967F, -12.4F, 3, 1, 3, 0.0F, true));
        p92.cubeList.add(new ModelBox(p92, 7, 77, -1.0F, -10.7092F, -12.2594F, 2, 2, 1, 0.0F, true));
        p92.cubeList.add(new ModelBox(p92, 7, 77, 0.6623F, -10.7092F, 8.7406F, 1, 2, 2, 0.0F, true));
        p92.cubeList.add(new ModelBox(p92, 7, 77, -1.6623F, -10.7092F, 8.7406F, 1, 2, 2, 0.0F, false));
        p92.cubeList.add(new ModelBox(p92, 7, 77, -1.0F, -9.9045F, 8.7406F, 2, 1, 2, 0.0F, false));
        p92.cubeList.add(new ModelBox(p92, 15, 73, -0.5F, -8.885F, 11.7172F, 1, 1, 1, 0.0F, true));
        p92.cubeList.add(new ModelBox(p92, 33, 7, -2.342F, -8.4397F, 3.6F, 1, 2, 9, 0.0F, false));
        p92.cubeList.add(new ModelBox(p92, 33, 7, 1.342F, -7.4397F, -7.4F, 1, 1, 11, 0.0F, true));
        p92.cubeList.add(new ModelBox(p92, 33, 7, 1.342F, -7.4397F, -12.4F, 1, 1, 5, 0.0F, true));
        p92.cubeList.add(new ModelBox(p92, 33, 7, -0.658F, -8.4397F, -12.4F, 3, 1, 3, 0.0F, true));
        p92.cubeList.add(new ModelBox(p92, 33, 7, -2.342F, -8.4397F, -12.4F, 2, 1, 3, 0.0F, false));
        p92.cubeList.add(new ModelBox(p92, 33, 7, -2.342F, -7.4397F, -12.4F, 1, 1, 5, 0.0F, false));
        p92.cubeList.add(new ModelBox(p92, 33, 7, -2.342F, -7.4397F, -7.4F, 1, 1, 11, 0.0F, false));
        p92.cubeList.add(new ModelBox(p92, 33, 7, -2.0F, -4.5F, 5.6F, 4, 1, 8, 0.0F, false));
        p92.cubeList.add(new ModelBox(p92, 33, 7, -2.0F, -4.5F, -3.4F, 4, 1, 9, 0.0F, false));

        bone16 = new RendererModel(this);
        bone16.setRotationPoint(1.0F, 0.0F, 0.0F);
        setRotationAngle(bone16, -0.6109F, 0.0F, 0.0F);
        p92.addChild(bone16);
        bone16.cubeList.add(new ModelBox(bone16, 96, 41, -2.0F, -2.251F, 0.3139F, 2, 1, 3, 0.0F, false));

        bone15 = new RendererModel(this);
        bone15.setRotationPoint(1.0F, 4.7969F, 0.0F);
        setRotationAngle(bone15, 0.2618F, 0.0F, 0.0F);
        p92.addChild(bone15);
        bone15.cubeList.add(new ModelBox(bone15, 96, 41, -3.0F, -3.5288F, 9.2277F, 4, 6, 5, 0.0F, false));
        bone15.cubeList.add(new ModelBox(bone15, 96, 41, -2.0F, -3.1225F, 7.2277F, 2, 1, 2, 0.0F, false));

        bone = new RendererModel(this);
        bone.setRotationPoint(1.0F, 0.0F, 0.0F);
        setRotationAngle(bone, 0.0F, 0.0F, -0.7854F);
        p92.addChild(bone);
        bone.cubeList.add(new ModelBox(bone, 83, 18, 0.4142F, -1.0F, -12.0F, 1, 1, 14, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 83, 18, 1.8284F, -2.4142F, -12.0F, 1, 1, 1, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 82, 21, 0.0F, -1.0F, -12.0F, 1, 1, 14, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 82, 14, -1.4142F, -2.4142F, -12.0F, 1, 1, 14, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 82, 17, -1.4142F, -2.8284F, -12.0F, 1, 1, 14, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 82, 17, 0.0F, -4.2426F, -12.0F, 1, 1, 1, 0.0F, false));

        bone11 = new RendererModel(this);
        bone11.setRotationPoint(1.158F, 0.0F, -0.8828F);
        setRotationAngle(bone11, -0.1745F, 0.0F, 0.0F);
        p92.addChild(bone11);
        bone11.cubeList.add(new ModelBox(bone11, 38, 42, -1.658F, -10.8811F, 12.475F, 1, 3, 1, 0.0F, true));

        bone12 = new RendererModel(this);
        bone12.setRotationPoint(1.158F, -0.418F, -0.8828F);
        setRotationAngle(bone12, -0.1745F, 0.0F, 0.0F);
        p92.addChild(bone12);
        bone12.cubeList.add(new ModelBox(bone12, 38, 42, -1.658F, -9.7039F, 11.5476F, 1, 1, 1, 0.0F, true));
        bone12.cubeList.add(new ModelBox(bone12, 38, 42, -1.658F, -10.571F, 13.5476F, 1, 1, 1, 0.0F, true));

        bone13 = new RendererModel(this);
        bone13.setRotationPoint(-1.158F, -10.071F, 14.0476F);
        setRotationAngle(bone13, 0.1222F, 0.0F, 0.0F);
        bone12.addChild(bone13);
        bone13.cubeList.add(new ModelBox(bone13, 38, 42, -0.5F, -0.5572F, -1.4353F, 1, 1, 1, 0.0F, true));
        bone13.cubeList.add(new ModelBox(bone13, 38, 42, -0.5F, 0.2503F, -1.393F, 1, 1, 1, 0.0F, true));

        bone7 = new RendererModel(this);
        bone7.setRotationPoint(1.0F, 0.0F, 0.0F);
        setRotationAngle(bone7, 0.5236F, 0.0F, 0.0F);
        p92.addChild(bone7);
        bone7.cubeList.add(new ModelBox(bone7, 33, 7, -0.658F, -0.7769F, 13.9978F, 2, 2, 1, 0.0F, true));
        bone7.cubeList.add(new ModelBox(bone7, 33, 7, -3.342F, -0.7769F, 13.9978F, 2, 2, 1, 0.0F, false));

        bone8 = new RendererModel(this);
        bone8.setRotationPoint(1.0F, 0.0F, 0.0F);
        setRotationAngle(bone8, 0.6109F, 0.0F, 0.0F);
        p92.addChild(bone8);
        bone8.cubeList.add(new ModelBox(bone8, 33, 7, -0.5F, 1.5793F, 13.9109F, 1, 1, 1, 0.0F, true));
        bone8.cubeList.add(new ModelBox(bone8, 33, 7, -2.5F, 1.5793F, 13.9109F, 1, 1, 1, 0.0F, false));

        bone14 = new RendererModel(this);
        bone14.setRotationPoint(0.0373F, -9.7092F, -9.7594F);
        setRotationAngle(bone14, 0.4363F, 0.0F, 0.0F);
        p92.addChild(bone14);
        bone14.cubeList.add(new ModelBox(bone14, 7, 77, -1.0373F, -1.5402F, -1.9368F, 2, 2, 1, 0.0F, true));
        bone14.cubeList.add(new ModelBox(bone14, 7, 77, 0.625F, 7.7574F, 18.0019F, 1, 2, 1, 0.0F, true));
        bone14.cubeList.add(new ModelBox(bone14, 7, 77, -1.6997F, 7.7574F, 18.0019F, 1, 2, 1, 0.0F, false));

        bone9 = new RendererModel(this);
        bone9.setRotationPoint(1.0F, 0.0F, 0.0F);
        setRotationAngle(bone9, 0.0F, 0.0F, 0.5236F);
        p92.addChild(bone9);
        bone9.cubeList.add(new ModelBox(bone9, 33, 7, -4.0576F, -7.98F, 3.6F, 1, 1, 9, 0.0F, true));
        bone9.cubeList.add(new ModelBox(bone9, 33, 7, -4.0576F, -7.98F, -12.4F, 1, 1, 3, 0.0F, true));

        bone10 = new RendererModel(this);
        bone10.setRotationPoint(-1.0F, 0.0F, 0.0F);
        setRotationAngle(bone10, 0.0F, 0.0F, -0.5236F);
        p92.addChild(bone10);
        bone10.cubeList.add(new ModelBox(bone10, 33, 7, 3.0576F, -7.98F, 3.6F, 1, 1, 9, 0.0F, false));
        bone10.cubeList.add(new ModelBox(bone10, 33, 7, 3.0576F, -7.98F, -12.4F, 1, 1, 3, 0.0F, false));

        bone4 = new RendererModel(this);
        bone4.setRotationPoint(1.0F, 0.0F, 0.0F);
        p92.addChild(bone4);
        bone4.cubeList.add(new ModelBox(bone4, 34, 34, -1.5F, -8.0764F, -14.0289F, 1, 1, 13, 0.0F, true));
        bone4.cubeList.add(new ModelBox(bone4, 34, 34, -1.5F, -8.0764F, -2.0289F, 1, 1, 6, 0.0F, true));
        bone4.cubeList.add(new ModelBox(bone4, 34, 34, -1.5F, -5.3444F, -14.0289F, 1, 1, 13, 0.0F, true));
        bone4.cubeList.add(new ModelBox(bone4, 4, 80, -2.0F, -7.3444F, -12.6852F, 2, 2, 1, 0.0F, true));
        bone4.cubeList.add(new ModelBox(bone4, 34, 34, -1.5F, -5.3444F, -2.0289F, 1, 1, 6, 0.0F, true));
        bone4.cubeList.add(new ModelBox(bone4, 34, 34, -2.866F, -6.7104F, -14.0289F, 1, 1, 13, 0.0F, true));
        bone4.cubeList.add(new ModelBox(bone4, 34, 34, -2.866F, -6.7104F, -2.0289F, 1, 1, 6, 0.0F, true));
        bone4.cubeList.add(new ModelBox(bone4, 34, 34, -0.134F, -6.7104F, -14.0289F, 1, 1, 13, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 34, 34, -0.134F, -6.7104F, -2.0289F, 1, 1, 6, 0.0F, false));

        bone5 = new RendererModel(this);
        bone5.setRotationPoint(1.842F, -10.9397F, 6.6F);
        setRotationAngle(bone5, 0.0F, 0.0F, 0.5236F);
        p92.addChild(bone5);
        bone5.cubeList.add(new ModelBox(bone5, 34, 34, 0.2694F, 3.1507F, -20.6289F, 1, 1, 13, 0.0F, true));
        bone5.cubeList.add(new ModelBox(bone5, 34, 34, 0.2694F, 3.1507F, -8.6289F, 1, 1, 6, 0.0F, true));
        bone5.cubeList.add(new ModelBox(bone5, 34, 34, -1.0966F, 4.5167F, -20.6289F, 1, 1, 13, 0.0F, true));
        bone5.cubeList.add(new ModelBox(bone5, 34, 34, -1.0966F, 4.5167F, -8.6289F, 1, 1, 6, 0.0F, true));
        bone5.cubeList.add(new ModelBox(bone5, 34, 34, 1.6354F, 4.5167F, -20.6289F, 1, 1, 13, 0.0F, true));
        bone5.cubeList.add(new ModelBox(bone5, 34, 34, 1.6354F, 4.5167F, -8.6289F, 1, 1, 6, 0.0F, true));
        bone5.cubeList.add(new ModelBox(bone5, 34, 34, 0.2694F, 5.8827F, -20.6289F, 1, 1, 13, 0.0F, true));
        bone5.cubeList.add(new ModelBox(bone5, 34, 34, 0.2694F, 5.8827F, -8.6289F, 1, 1, 6, 0.0F, true));

        bone6 = new RendererModel(this);
        bone6.setRotationPoint(-1.842F, -10.9397F, 6.6F);
        setRotationAngle(bone6, 0.0F, 0.0F, -0.5236F);
        p92.addChild(bone6);
        bone6.cubeList.add(new ModelBox(bone6, 34, 34, -1.2694F, 3.1507F, -20.6289F, 1, 1, 13, 0.0F, false));
        bone6.cubeList.add(new ModelBox(bone6, 34, 34, -1.2694F, 3.1507F, -8.6289F, 1, 1, 6, 0.0F, false));
        bone6.cubeList.add(new ModelBox(bone6, 34, 34, 0.0966F, 4.5167F, -20.6289F, 1, 1, 13, 0.0F, false));
        bone6.cubeList.add(new ModelBox(bone6, 34, 34, 0.0966F, 4.5167F, -8.6289F, 1, 1, 6, 0.0F, false));
        bone6.cubeList.add(new ModelBox(bone6, 34, 34, -2.6354F, 4.5167F, -20.6289F, 1, 1, 13, 0.0F, false));
        bone6.cubeList.add(new ModelBox(bone6, 34, 34, -2.6354F, 4.5167F, -8.6289F, 1, 1, 6, 0.0F, false));
        bone6.cubeList.add(new ModelBox(bone6, 34, 34, -1.2694F, 5.8827F, -20.6289F, 1, 1, 13, 0.0F, false));
        bone6.cubeList.add(new ModelBox(bone6, 34, 34, -1.2694F, 5.8827F, -8.6289F, 1, 1, 6, 0.0F, false));

        bone2 = new RendererModel(this);
        bone2.setRotationPoint(-1.0F, 0.0F, 0.0F);
        setRotationAngle(bone2, 0.0F, 0.0F, -0.3491F);
        p92.addChild(bone2);
        bone2.cubeList.add(new ModelBox(bone2, 32, 6, 0.2574F, -4.6309F, -12.4F, 1, 1, 13, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 32, 6, 0.2574F, -4.6309F, 0.6F, 1, 1, 13, 0.0F, false));

        bone3 = new RendererModel(this);
        bone3.setRotationPoint(1.0F, 0.0F, 0.0F);
        setRotationAngle(bone3, 0.0F, 0.0F, 0.3491F);
        p92.addChild(bone3);
        bone3.cubeList.add(new ModelBox(bone3, 32, 6, -1.2574F, -4.6309F, -12.4F, 1, 1, 13, 0.0F, true));
        bone3.cubeList.add(new ModelBox(bone3, 32, 6, -1.2574F, -4.6309F, 0.6F, 1, 1, 13, 0.0F, true));

        magazine = new RendererModel(this);
        magazine.setRotationPoint(1.5F, 14.1953F, -1.1953F);
        setRotationAngle(magazine, 0.0873F, 0.0F, 0.0F);
        magazine.cubeList.add(new ModelBox(magazine, 96, 41, -3.0F, 7.7046F, 9.3825F, 1, 1, 4, 0.0F, true));
        magazine.cubeList.add(new ModelBox(magazine, 96, 41, -1.0F, 7.7046F, 9.3825F, 1, 1, 4, 0.0F, false));
        magazine.cubeList.add(new ModelBox(magazine, 96, 41, -3.0F, 8.7046F, 9.3825F, 3, 7, 4, 0.0F, false));
        magazine.cubeList.add(new ModelBox(magazine, 96, 41, -3.0F, 15.7046F, 8.3825F, 3, 1, 5, 0.0F, false));
        magazine.cubeList.add(new ModelBox(magazine, 11, 104, -2.0F, 8.0015F, 10.0232F, 1, 1, 3, 0.0F, true));

        addAnimatedPart(0, magazine);
    }
}
