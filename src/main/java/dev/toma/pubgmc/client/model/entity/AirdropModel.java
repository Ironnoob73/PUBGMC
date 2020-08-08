package dev.toma.pubgmc.client.model.entity;

import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelBox;

public abstract class AirdropModel extends Model {

    public abstract void render();

    public static void setRotationAngle(RendererModel model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public static class Normal extends AirdropModel {

        private final RendererModel drop;

        public Normal() {
            textureWidth = 128;
            textureHeight = 128;

            drop = new RendererModel(this);
            drop.setRotationPoint(0.0F, 24.0F, 0.0F);

            RendererModel box = new RendererModel(this);
            box.setRotationPoint(0.0F, 0.0F, 0.0F);
            drop.addChild(box);
            box.cubeList.add(new ModelBox(box, 0, 0, -7.5F, -0.9F, -7.5F, 15, 1, 15, 0.0F, false));
            box.cubeList.add(new ModelBox(box, 82, 0, -7.0F, -12.0F, 6.0F, 14, 12, 1, 0.0F, false));
            box.cubeList.add(new ModelBox(box, 85, 0, -7.0F, -12.0F, -7.0F, 14, 12, 1, 0.0F, false));
            box.cubeList.add(new ModelBox(box, 82, 0, -7.0F, -12.0F, -6.0F, 1, 12, 12, 0.0F, false));
            box.cubeList.add(new ModelBox(box, 82, 0, 6.0F, -12.0F, -6.0F, 1, 12, 12, 0.0F, false));

            RendererModel detail = new RendererModel(this);
            detail.setRotationPoint(0.0F, 0.0F, 0.0F);
            box.addChild(detail);
            detail.cubeList.add(new ModelBox(detail, 0, 0, -7.5F, -12.0F, -7.5F, 2, 12, 2, 0.0F, false));
            detail.cubeList.add(new ModelBox(detail, 0, 0, 5.5F, -12.0F, -7.5F, 2, 12, 2, 0.0F, false));
            detail.cubeList.add(new ModelBox(detail, 0, 0, 5.5F, -12.0F, 5.5F, 2, 12, 2, 0.0F, false));
            detail.cubeList.add(new ModelBox(detail, 0, 0, -7.5F, -12.0F, 5.5F, 2, 12, 2, 0.0F, false));
            detail.cubeList.add(new ModelBox(detail, 0, 0, -3.25F, -12.0F, -7.5F, 2, 12, 1, 0.0F, false));
            detail.cubeList.add(new ModelBox(detail, 0, 0, 1.25F, -12.0F, -7.5F, 2, 12, 1, 0.0F, false));
            detail.cubeList.add(new ModelBox(detail, 0, 0, -3.25F, -12.0F, 6.5F, 2, 12, 1, 0.0F, false));
            detail.cubeList.add(new ModelBox(detail, 0, 0, 1.25F, -12.0F, 6.5F, 2, 12, 1, 0.0F, false));
            detail.cubeList.add(new ModelBox(detail, 0, 0, -7.5F, -12.0F, 1.25F, 1, 12, 2, 0.0F, false));
            detail.cubeList.add(new ModelBox(detail, 0, 0, -7.5F, -12.0F, -3.25F, 1, 12, 2, 0.0F, false));
            detail.cubeList.add(new ModelBox(detail, 0, 0, 6.5F, -12.0F, 1.25F, 1, 12, 2, 0.0F, false));
            detail.cubeList.add(new ModelBox(detail, 0, 0, 6.5F, -12.0F, -3.25F, 1, 12, 2, 0.0F, false));

            RendererModel cloth = new RendererModel(this);
            cloth.setRotationPoint(0.0F, 0.0F, 0.0F);
            drop.addChild(cloth);
            cloth.cubeList.add(new ModelBox(cloth, 82, 71, -8.0F, -13.0F, -8.0F, 16, 1, 7, 0.0F, false));
            cloth.cubeList.add(new ModelBox(cloth, 82, 71, -8.0F, -13.0F, 1.0F, 16, 1, 7, 0.0F, false));
            cloth.cubeList.add(new ModelBox(cloth, 82, 71, -8.0F, -13.0F, -1.0F, 16, 1, 2, 0.0F, false));
            cloth.cubeList.add(new ModelBox(cloth, 82, 71, -8.0F, -12.0F, 7.0F, 16, 2, 1, 0.0F, false));
            cloth.cubeList.add(new ModelBox(cloth, 82, 71, -8.0F, -12.0F, -8.0F, 16, 2, 1, 0.0F, false));
            cloth.cubeList.add(new ModelBox(cloth, 82, 71, -8.0F, -12.0F, -7.0F, 1, 2, 14, 0.0F, false));
            cloth.cubeList.add(new ModelBox(cloth, 82, 71, 7.0F, -12.0F, -7.0F, 1, 2, 14, 0.0F, false));

            RendererModel detail2 = new RendererModel(this);
            detail2.setRotationPoint(0.0F, 0.0F, 0.0F);
            cloth.addChild(detail2);
            detail2.cubeList.add(new ModelBox(detail2, 82, 114, 7.1F, -13.1F, 3.0F, 1, 3, 1, 0.0F, false));
            detail2.cubeList.add(new ModelBox(detail2, 82, 114, 7.1F, -13.1F, -4.0F, 1, 3, 1, 0.0F, false));
            detail2.cubeList.add(new ModelBox(detail2, 82, 114, -8.1F, -13.1F, 3.0F, 1, 3, 1, 0.0F, false));
            detail2.cubeList.add(new ModelBox(detail2, 82, 114, -8.1F, -13.1F, -4.0F, 1, 3, 1, 0.0F, false));
            detail2.cubeList.add(new ModelBox(detail2, 82, 114, 3.0F, -13.1F, -8.1F, 1, 3, 1, 0.0F, false));
            detail2.cubeList.add(new ModelBox(detail2, 82, 114, -4.0F, -13.1F, -8.1F, 1, 3, 1, 0.0F, false));
            detail2.cubeList.add(new ModelBox(detail2, 82, 114, -4.0F, -13.1F, 7.1F, 1, 3, 1, 0.0F, false));
            detail2.cubeList.add(new ModelBox(detail2, 82, 114, 3.0F, -13.1F, 7.1F, 1, 3, 1, 0.0F, false));
            detail2.cubeList.add(new ModelBox(detail2, 82, 114, -7.4F, -13.1F, -4.0F, 15, 1, 1, 0.0F, false));
            detail2.cubeList.add(new ModelBox(detail2, 82, 114, -7.4F, -13.1F, 3.0F, 15, 1, 1, 0.0F, false));
            detail2.cubeList.add(new ModelBox(detail2, 82, 114, -4.0F, -13.1F, -5.7F, 1, 1, 13, 0.0F, false));
            detail2.cubeList.add(new ModelBox(detail2, 82, 114, 3.0F, -13.1F, -5.7F, 1, 1, 13, 0.0F, false));
            detail2.cubeList.add(new ModelBox(detail2, 82, 114, 3.0F, -13.1F, -7.7F, 1, 1, 2, 0.0F, false));
            detail2.cubeList.add(new ModelBox(detail2, 82, 114, -4.0F, -13.1F, -7.7F, 1, 1, 2, 0.0F, false));

            RendererModel chute = new RendererModel(this);
            chute.setRotationPoint(0.0F, 0.0F, 0.0F);
            drop.addChild(chute);

            RendererModel strings = new RendererModel(this);
            strings.setRotationPoint(0.0F, 0.0F, 0.0F);
            chute.addChild(strings);
            strings.cubeList.add(new ModelBox(strings, 0, 70, -1.0F, -13.2F, -1.0F, 2, 1, 2, 0.0F, false));

            RendererModel a0 = new RendererModel(this);
            a0.setRotationPoint(0.0F, 0.0F, 0.0F);
            setRotationAngle(a0, -0.3491F, 0.0F, 0.0F);
            strings.addChild(a0);
            a0.cubeList.add(new ModelBox(a0, 0, 70, -0.5F, -53.0F, -4.0F, 1, 41, 1, 0.0F, false));

            RendererModel a1 = new RendererModel(this);
            a1.setRotationPoint(0.0F, 0.0F, 0.0F);
            setRotationAngle(a1, -0.3491F, 0.0F, -0.3491F);
            strings.addChild(a1);
            a1.cubeList.add(new ModelBox(a1, 0, 70, 3.25F, -52.0F, -4.0F, 1, 40, 1, 0.0F, false));

            RendererModel a2 = new RendererModel(this);
            a2.setRotationPoint(0.0F, 0.0F, 0.0F);
            setRotationAngle(a2, 0.0F, 0.0F, -0.3491F);
            strings.addChild(a2);
            a2.cubeList.add(new ModelBox(a2, 0, 70, 3.0F, -52.0F, -0.5F, 1, 40, 1, 0.0F, false));

            RendererModel a3 = new RendererModel(this);
            a3.setRotationPoint(0.0F, 0.0F, 0.0F);
            setRotationAngle(a3, 0.3491F, 0.0F, -0.3491F);
            strings.addChild(a3);
            a3.cubeList.add(new ModelBox(a3, 0, 70, 3.0F, -51.0F, 3.0F, 1, 39, 1, 0.0F, false));

            RendererModel a4 = new RendererModel(this);
            a4.setRotationPoint(0.0F, 0.0F, 0.0F);
            setRotationAngle(a4, 0.3491F, 0.0F, 0.0F);
            strings.addChild(a4);
            a4.cubeList.add(new ModelBox(a4, 0, 70, -0.5F, -53.0F, 3.0F, 1, 41, 1, 0.0F, false));

            RendererModel a5 = new RendererModel(this);
            a5.setRotationPoint(0.0F, 0.0F, 0.0F);
            setRotationAngle(a5, 0.3491F, 0.0F, 0.3491F);
            strings.addChild(a5);
            a5.cubeList.add(new ModelBox(a5, 0, 70, -4.0F, -52.0F, 3.0F, 1, 40, 1, 0.0F, false));

            RendererModel a6 = new RendererModel(this);
            a6.setRotationPoint(0.0F, 0.0F, 0.0F);
            setRotationAngle(a6, 0.0F, 0.0F, 0.3491F);
            strings.addChild(a6);
            a6.cubeList.add(new ModelBox(a6, 0, 70, -4.0F, -53.0F, -0.5F, 1, 41, 1, 0.0F, false));

            RendererModel a7 = new RendererModel(this);
            a7.setRotationPoint(0.0F, 0.0F, 0.0F);
            setRotationAngle(a7, -0.3491F, 0.0F, 0.3491F);
            strings.addChild(a7);
            a7.cubeList.add(new ModelBox(a7, 0, 70, -4.0F, -52.0F, -4.0F, 1, 40, 1, 0.0F, false));

            RendererModel base = new RendererModel(this);
            base.setRotationPoint(0.0F, 0.0F, 0.0F);
            chute.addChild(base);
            base.cubeList.add(new ModelBox(base, 0, 70, -10.0F, -54.0F, 3.0F, 20, 1, 7, 0.0F, false));
            base.cubeList.add(new ModelBox(base, 0, 70, -10.0F, -54.0F, -10.0F, 20, 1, 7, 0.0F, false));
            base.cubeList.add(new ModelBox(base, 0, 70, 3.0F, -54.0F, -3.0F, 7, 1, 6, 0.0F, false));
            base.cubeList.add(new ModelBox(base, 0, 70, -10.0F, -54.0F, -3.0F, 7, 1, 6, 0.0F, false));

            RendererModel b0 = new RendererModel(this);
            b0.setRotationPoint(0.0F, 0.0F, 0.0F);
            setRotationAngle(b0, 0.5236F, 0.0F, 0.0F);
            base.addChild(b0);
            b0.cubeList.add(new ModelBox(b0, 0, 70, -10.0F, -51.7F, -1.6F, 20, 1, 20, 0.0F, false));

            RendererModel b1 = new RendererModel(this);
            b1.setRotationPoint(0.0F, 0.0F, 0.0F);
            setRotationAngle(b1, -0.5236F, 0.0F, 0.0F);
            base.addChild(b1);
            b1.cubeList.add(new ModelBox(b1, 0, 70, -10.0F, -51.8F, -18.3F, 20, 1, 20, 0.0F, false));

            RendererModel b2 = new RendererModel(this);
            b2.setRotationPoint(0.0F, 0.0F, 0.0F);
            setRotationAngle(b2, 0.0F, 0.0F, -0.5236F);
            base.addChild(b2);
            b2.cubeList.add(new ModelBox(b2, 0, 70, -1.7F, -51.7F, -10.0F, 20, 1, 20, 0.0F, false));

            RendererModel b3 = new RendererModel(this);
            b3.setRotationPoint(0.0F, 0.0F, 0.0F);
            setRotationAngle(b3, 0.0F, 0.0F, 0.5236F);
            base.addChild(b3);
            b3.cubeList.add(new ModelBox(b3, 0, 70, -18.3F, -51.8F, -10.0F, 20, 1, 20, 0.0F, false));

            RendererModel b4 = new RendererModel(this);
            b4.setRotationPoint(0.0F, 0.0F, 0.0F);
            setRotationAngle(b4, -0.5236F, -0.0873F, -0.5236F);
            base.addChild(b4);
            b4.cubeList.add(new ModelBox(b4, 0, 70, 0.0F, -49.1F, -18.6F, 19, 1, 19, 0.0F, false));

            RendererModel b5 = new RendererModel(this);
            b5.setRotationPoint(0.0F, 0.0F, 0.0F);
            setRotationAngle(b5, 0.5236F, 0.1571F, -0.5236F);
            base.addChild(b5);
            b5.cubeList.add(new ModelBox(b5, 0, 70, 0.6F, -48.5F, 0.7F, 19, 1, 19, 0.0F, false));

            RendererModel b6 = new RendererModel(this);
            b6.setRotationPoint(0.0F, 0.0F, 0.0F);
            setRotationAngle(b6, 0.5236F, -0.1222F, 0.5236F);
            base.addChild(b6);
            b6.cubeList.add(new ModelBox(b6, 0, 70, -19.4F, -48.9F, 0.1F, 19, 1, 19, 0.0F, false));

            RendererModel b7 = new RendererModel(this);
            b7.setRotationPoint(0.0F, 0.0F, 0.0F);
            setRotationAngle(b7, -0.5236F, 0.1745F, 0.5236F);
            base.addChild(b7);
            b7.cubeList.add(new ModelBox(b7, 0, 70, -19.7F, -48.5F, -19.9F, 19, 1, 19, 0.0F, false));
        }

        @Override
        public void render() {
            drop.render(1f);
        }
    }

    public static class Flare extends AirdropModel {

        private final RendererModel drop;

        public Flare() {
            textureWidth = 128;
            textureHeight = 128;

            drop = new RendererModel(this);
            drop.setRotationPoint(0.0F, 24.0F, 0.0F);

            RendererModel chute = new RendererModel(this);
            chute.setRotationPoint(0.0F, 0.0F, 0.0F);
            drop.addChild(chute);

            RendererModel strings = new RendererModel(this);
            strings.setRotationPoint(0.0F, 0.0F, 0.0F);
            chute.addChild(strings);
            strings.cubeList.add(new ModelBox(strings, 0, 70, -1.0F, -13.2F, -1.0F, 2, 1, 2, 0.0F, false));

            RendererModel a0 = new RendererModel(this);
            a0.setRotationPoint(0.0F, 0.0F, 0.0F);
            setRotationAngle(a0, -0.3491F, 0.0F, 0.0F);
            strings.addChild(a0);
            a0.cubeList.add(new ModelBox(a0, 0, 70, -0.5F, -53.0F, -4.0F, 1, 41, 1, 0.0F, false));

            RendererModel a1 = new RendererModel(this);
            a1.setRotationPoint(0.0F, 0.0F, 0.0F);
            setRotationAngle(a1, -0.3491F, 0.0F, -0.3491F);
            strings.addChild(a1);
            a1.cubeList.add(new ModelBox(a1, 0, 70, 3.25F, -52.0F, -4.0F, 1, 40, 1, 0.0F, false));

            RendererModel a2 = new RendererModel(this);
            a2.setRotationPoint(0.0F, 0.0F, 0.0F);
            setRotationAngle(a2, 0.0F, 0.0F, -0.3491F);
            strings.addChild(a2);
            a2.cubeList.add(new ModelBox(a2, 0, 70, 3.0F, -52.0F, -0.5F, 1, 40, 1, 0.0F, false));

            RendererModel a3 = new RendererModel(this);
            a3.setRotationPoint(0.0F, 0.0F, 0.0F);
            setRotationAngle(a3, 0.3491F, 0.0F, -0.3491F);
            strings.addChild(a3);
            a3.cubeList.add(new ModelBox(a3, 0, 70, 3.0F, -51.0F, 3.0F, 1, 39, 1, 0.0F, false));

            RendererModel a4 = new RendererModel(this);
            a4.setRotationPoint(0.0F, 0.0F, 0.0F);
            setRotationAngle(a4, 0.3491F, 0.0F, 0.0F);
            strings.addChild(a4);
            a4.cubeList.add(new ModelBox(a4, 0, 70, -0.5F, -53.0F, 3.0F, 1, 41, 1, 0.0F, false));

            RendererModel a5 = new RendererModel(this);
            a5.setRotationPoint(0.0F, 0.0F, 0.0F);
            setRotationAngle(a5, 0.3491F, 0.0F, 0.3491F);
            strings.addChild(a5);
            a5.cubeList.add(new ModelBox(a5, 0, 70, -4.0F, -52.0F, 3.0F, 1, 40, 1, 0.0F, false));

            RendererModel a6 = new RendererModel(this);
            a6.setRotationPoint(0.0F, 0.0F, 0.0F);
            setRotationAngle(a6, 0.0F, 0.0F, 0.3491F);
            strings.addChild(a6);
            a6.cubeList.add(new ModelBox(a6, 0, 70, -4.0F, -53.0F, -0.5F, 1, 41, 1, 0.0F, false));

            RendererModel a7 = new RendererModel(this);
            a7.setRotationPoint(0.0F, 0.0F, 0.0F);
            setRotationAngle(a7, -0.3491F, 0.0F, 0.3491F);
            strings.addChild(a7);
            a7.cubeList.add(new ModelBox(a7, 0, 70, -4.0F, -52.0F, -4.0F, 1, 40, 1, 0.0F, false));

            RendererModel base = new RendererModel(this);
            base.setRotationPoint(0.0F, 0.0F, 0.0F);
            chute.addChild(base);
            base.cubeList.add(new ModelBox(base, 0, 70, -10.0F, -54.0F, 3.0F, 20, 1, 7, 0.0F, false));
            base.cubeList.add(new ModelBox(base, 0, 70, -10.0F, -54.0F, -10.0F, 20, 1, 7, 0.0F, false));
            base.cubeList.add(new ModelBox(base, 0, 70, 3.0F, -54.0F, -3.0F, 7, 1, 6, 0.0F, false));
            base.cubeList.add(new ModelBox(base, 0, 70, -10.0F, -54.0F, -3.0F, 7, 1, 6, 0.0F, false));

            RendererModel b0 = new RendererModel(this);
            b0.setRotationPoint(0.0F, 0.0F, 0.0F);
            setRotationAngle(b0, 0.5236F, 0.0F, 0.0F);
            base.addChild(b0);
            b0.cubeList.add(new ModelBox(b0, 0, 70, -10.0F, -51.7F, -1.6F, 20, 1, 20, 0.0F, false));

            RendererModel b1 = new RendererModel(this);
            b1.setRotationPoint(0.0F, 0.0F, 0.0F);
            setRotationAngle(b1, -0.5236F, 0.0F, 0.0F);
            base.addChild(b1);
            b1.cubeList.add(new ModelBox(b1, 0, 70, -10.0F, -51.8F, -18.3F, 20, 1, 20, 0.0F, false));

            RendererModel b2 = new RendererModel(this);
            b2.setRotationPoint(0.0F, 0.0F, 0.0F);
            setRotationAngle(b2, 0.0F, 0.0F, -0.5236F);
            base.addChild(b2);
            b2.cubeList.add(new ModelBox(b2, 0, 70, -1.7F, -51.7F, -10.0F, 20, 1, 20, 0.0F, false));

            RendererModel b3 = new RendererModel(this);
            b3.setRotationPoint(0.0F, 0.0F, 0.0F);
            setRotationAngle(b3, 0.0F, 0.0F, 0.5236F);
            base.addChild(b3);
            b3.cubeList.add(new ModelBox(b3, 0, 70, -18.3F, -51.8F, -10.0F, 20, 1, 20, 0.0F, false));

            RendererModel b4 = new RendererModel(this);
            b4.setRotationPoint(0.0F, 0.0F, 0.0F);
            setRotationAngle(b4, -0.5236F, -0.0873F, -0.5236F);
            base.addChild(b4);
            b4.cubeList.add(new ModelBox(b4, 0, 70, 0.0F, -49.1F, -18.6F, 19, 1, 19, 0.0F, false));

            RendererModel b5 = new RendererModel(this);
            b5.setRotationPoint(0.0F, 0.0F, 0.0F);
            setRotationAngle(b5, 0.5236F, 0.1571F, -0.5236F);
            base.addChild(b5);
            b5.cubeList.add(new ModelBox(b5, 0, 70, 0.6F, -48.5F, 0.7F, 19, 1, 19, 0.0F, false));

            RendererModel b6 = new RendererModel(this);
            b6.setRotationPoint(0.0F, 0.0F, 0.0F);
            setRotationAngle(b6, 0.5236F, -0.1222F, 0.5236F);
            base.addChild(b6);
            b6.cubeList.add(new ModelBox(b6, 0, 70, -19.4F, -48.9F, 0.1F, 19, 1, 19, 0.0F, false));

            RendererModel b7 = new RendererModel(this);
            b7.setRotationPoint(0.0F, 0.0F, 0.0F);
            setRotationAngle(b7, -0.5236F, 0.1745F, 0.5236F);
            base.addChild(b7);
            b7.cubeList.add(new ModelBox(b7, 0, 70, -19.7F, -48.5F, -19.9F, 19, 1, 19, 0.0F, false));

            RendererModel base2 = new RendererModel(this);
            base2.setRotationPoint(0.0F, 0.0F, 0.0F);
            drop.addChild(base2);
            base2.cubeList.add(new ModelBox(base2, 82, 70, 5.0F, 3.0F, -9.0F, 5, 1, 18, 0.0F, false));
            base2.cubeList.add(new ModelBox(base2, 82, 70, -15.0F, 3.0F, -9.0F, 5, 1, 18, 0.0F, false));
            base2.cubeList.add(new ModelBox(base2, 82, 70, -10.0F, 3.0F, -9.0F, 5, 1, 18, 0.0F, false));
            base2.cubeList.add(new ModelBox(base2, 82, 70, 0.0F, 3.0F, -9.0F, 5, 1, 18, 0.0F, false));
            base2.cubeList.add(new ModelBox(base2, 82, 70, -5.0F, 3.0F, -9.0F, 5, 1, 18, 0.0F, false));
            base2.cubeList.add(new ModelBox(base2, 82, 70, 10.0F, 3.0F, -9.0F, 5, 1, 18, 0.0F, false));
            base2.cubeList.add(new ModelBox(base2, 0, 0, -14.5F, -13.0F, -8.5F, 29, 16, 17, 0.0F, false));
        }

        @Override
        public void render() {
            drop.render(1f);
        }
    }
}
