package dev.toma.pubgmc.client.model.gun.attachment;

import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;

public class VerticalGripModel extends AttachmentModel {

    private final RendererModel gripsegment1;
    private final RendererModel gripsegment2;

    public VerticalGripModel() {
        textureWidth = 128;
        textureHeight = 128;

        gripsegment1 = new RendererModel(this);
        gripsegment1.setRotationPoint(0.0F, 24.0F, 0.0F);

        RendererModel grip3 = new RendererModel(this);
        grip3.setRotationPoint(0.0F, 0.0F, 0.0F);
        gripsegment1.addChild(grip3);
        grip3.cubeList.add(new ModelBox(grip3, 80, 70, -0.5F, -15.0F, 2.7321F, 1, 14, 1, 0.0F, false));
        grip3.cubeList.add(new ModelBox(grip3, 80, 70, 2.7321F, -15.0F, -0.5F, 1, 14, 1, 0.0F, false));
        grip3.cubeList.add(new ModelBox(grip3, 80, 70, -3.7321F, -15.0F, -0.5F, 1, 14, 1, 0.0F, true));
        grip3.cubeList.add(new ModelBox(grip3, 80, 70, -0.5F, -15.0F, -3.7321F, 1, 14, 1, 0.0F, false));
        grip3.cubeList.add(new ModelBox(grip3, 80, 70, -0.5F, -1.875F, -3.0F, 1, 1, 6, 0.0F, false));
        grip3.cubeList.add(new ModelBox(grip3, 80, 70, -3.0F, -1.875F, -0.5F, 6, 1, 1, 0.0F, false));
        grip3.cubeList.add(new ModelBox(grip3, 80, 70, -3.0F, -15.7344F, -0.5F, 6, 1, 1, 0.0F, false));
        grip3.cubeList.add(new ModelBox(grip3, 80, 70, -0.5F, -15.7344F, -3.0F, 1, 1, 6, 0.0F, false));

        RendererModel grip2 = new RendererModel(this);
        grip2.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(grip2, 0.0F, -0.2618F, 0.0F);
        gripsegment1.addChild(grip2);
        grip2.cubeList.add(new ModelBox(grip2, 80, 70, -0.5F, -15.0F, 2.7321F, 1, 14, 1, 0.0F, false));
        grip2.cubeList.add(new ModelBox(grip2, 80, 70, 2.7321F, -15.0F, -0.5F, 1, 14, 1, 0.0F, false));
        grip2.cubeList.add(new ModelBox(grip2, 80, 70, -3.7321F, -15.0F, -0.5F, 1, 14, 1, 0.0F, true));
        grip2.cubeList.add(new ModelBox(grip2, 80, 70, -0.5F, -15.0F, -3.7321F, 1, 14, 1, 0.0F, false));
        grip2.cubeList.add(new ModelBox(grip2, 80, 70, -0.5F, -1.875F, -3.0F, 1, 1, 6, 0.0F, false));
        grip2.cubeList.add(new ModelBox(grip2, 80, 70, -3.0F, -1.875F, -0.5F, 6, 1, 1, 0.0F, false));
        grip2.cubeList.add(new ModelBox(grip2, 80, 70, -3.0F, -15.7344F, -0.5F, 6, 1, 1, 0.0F, false));
        grip2.cubeList.add(new ModelBox(grip2, 80, 70, -0.5F, -15.7344F, -3.0F, 1, 1, 6, 0.0F, false));

        RendererModel grip4 = new RendererModel(this);
        grip4.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(grip4, 0.0F, -0.5236F, 0.0F);
        gripsegment1.addChild(grip4);
        grip4.cubeList.add(new ModelBox(grip4, 80, 70, -0.5F, -15.0F, 2.7321F, 1, 14, 1, 0.0F, false));
        grip4.cubeList.add(new ModelBox(grip4, 80, 70, 2.7321F, -15.0F, -0.5F, 1, 14, 1, 0.0F, false));
        grip4.cubeList.add(new ModelBox(grip4, 80, 70, -3.7321F, -15.0F, -0.5F, 1, 14, 1, 0.0F, true));
        grip4.cubeList.add(new ModelBox(grip4, 80, 70, -0.5F, -15.0F, -3.7321F, 1, 14, 1, 0.0F, false));
        grip4.cubeList.add(new ModelBox(grip4, 80, 70, -0.5F, -1.875F, -3.0F, 1, 1, 6, 0.0F, false));
        grip4.cubeList.add(new ModelBox(grip4, 80, 70, -3.0F, -1.875F, -0.5F, 6, 1, 1, 0.0F, false));
        grip4.cubeList.add(new ModelBox(grip4, 80, 70, -3.0F, -15.7344F, -0.5F, 6, 1, 1, 0.0F, false));
        grip4.cubeList.add(new ModelBox(grip4, 80, 70, -0.5F, -15.7344F, -3.0F, 1, 1, 6, 0.0F, false));

        RendererModel grip5 = new RendererModel(this);
        grip5.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(grip5, 0.0F, -0.7854F, 0.0F);
        gripsegment1.addChild(grip5);
        grip5.cubeList.add(new ModelBox(grip5, 80, 70, -0.5F, -15.0F, 2.7321F, 1, 14, 1, 0.0F, false));
        grip5.cubeList.add(new ModelBox(grip5, 80, 70, 2.7321F, -15.0F, -0.5F, 1, 14, 1, 0.0F, false));
        grip5.cubeList.add(new ModelBox(grip5, 80, 70, -3.7321F, -15.0F, -0.5F, 1, 14, 1, 0.0F, true));
        grip5.cubeList.add(new ModelBox(grip5, 80, 70, -0.5F, -15.0F, -3.7321F, 1, 14, 1, 0.0F, false));
        grip5.cubeList.add(new ModelBox(grip5, 80, 70, -0.5F, -1.875F, -3.0F, 1, 1, 6, 0.0F, false));
        grip5.cubeList.add(new ModelBox(grip5, 80, 70, -3.0F, -1.875F, -0.5F, 6, 1, 1, 0.0F, false));
        grip5.cubeList.add(new ModelBox(grip5, 80, 70, -3.0F, -15.7344F, -0.5F, 6, 1, 1, 0.0F, false));
        grip5.cubeList.add(new ModelBox(grip5, 80, 70, -0.5F, -15.7344F, -3.0F, 1, 1, 6, 0.0F, false));

        RendererModel grip6 = new RendererModel(this);
        grip6.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(grip6, 0.0F, -1.0472F, 0.0F);
        gripsegment1.addChild(grip6);
        grip6.cubeList.add(new ModelBox(grip6, 80, 70, -0.5F, -15.0F, 2.7321F, 1, 14, 1, 0.0F, false));
        grip6.cubeList.add(new ModelBox(grip6, 80, 70, 2.7321F, -15.0F, -0.5F, 1, 14, 1, 0.0F, false));
        grip6.cubeList.add(new ModelBox(grip6, 80, 70, -3.7321F, -15.0F, -0.5F, 1, 14, 1, 0.0F, true));
        grip6.cubeList.add(new ModelBox(grip6, 80, 70, -0.5F, -15.0F, -3.7321F, 1, 14, 1, 0.0F, false));
        grip6.cubeList.add(new ModelBox(grip6, 80, 70, -0.5F, -1.875F, -3.0F, 1, 1, 6, 0.0F, false));
        grip6.cubeList.add(new ModelBox(grip6, 80, 70, -3.0F, -1.875F, -0.5F, 6, 1, 1, 0.0F, false));
        grip6.cubeList.add(new ModelBox(grip6, 80, 70, -0.5F, -15.7344F, -3.0F, 1, 1, 6, 0.0F, false));
        grip6.cubeList.add(new ModelBox(grip6, 80, 70, -3.0F, -15.7344F, -0.5F, 6, 1, 1, 0.0F, false));

        RendererModel grip7 = new RendererModel(this);
        grip7.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(grip7, 0.0F, -1.309F, 0.0F);
        gripsegment1.addChild(grip7);
        grip7.cubeList.add(new ModelBox(grip7, 80, 70, -0.5F, -15.0F, 2.7321F, 1, 14, 1, 0.0F, false));
        grip7.cubeList.add(new ModelBox(grip7, 80, 70, 2.7321F, -15.0F, -0.5F, 1, 14, 1, 0.0F, false));
        grip7.cubeList.add(new ModelBox(grip7, 80, 70, -3.7321F, -15.0F, -0.5F, 1, 14, 1, 0.0F, true));
        grip7.cubeList.add(new ModelBox(grip7, 80, 70, -0.5F, -15.0F, -3.7321F, 1, 14, 1, 0.0F, false));
        grip7.cubeList.add(new ModelBox(grip7, 80, 70, -0.5F, -1.875F, -3.0F, 1, 1, 6, 0.0F, false));
        grip7.cubeList.add(new ModelBox(grip7, 80, 70, -3.0F, -1.875F, -0.5F, 6, 1, 1, 0.0F, false));
        grip7.cubeList.add(new ModelBox(grip7, 80, 70, -0.5F, -15.7344F, -3.0F, 1, 1, 6, 0.0F, false));
        grip7.cubeList.add(new ModelBox(grip7, 80, 70, -3.0F, -15.7344F, -0.5F, 6, 1, 1, 0.0F, false));

        gripsegment2 = new RendererModel(this);
        gripsegment2.setRotationPoint(0.0F, 24.0F, 0.0F);
        gripsegment2.cubeList.add(new ModelBox(gripsegment2, 93, 76, -3.5F, -17.6406F, -5.0F, 7, 2, 10, 0.0F, false));
        gripsegment2.cubeList.add(new ModelBox(gripsegment2, 93, 76, 1.5F, -18.6406F, -5.0F, 2, 1, 10, 0.0F, false));
        gripsegment2.cubeList.add(new ModelBox(gripsegment2, 93, 76, -3.5F, -18.6406F, -5.0F, 2, 1, 10, 0.0F, false));
        gripsegment2.cubeList.add(new ModelBox(gripsegment2, 119, 103, 2.6875F, -17.125F, 3.4844F, 1, 1, 1, 0.0F, false));
        gripsegment2.cubeList.add(new ModelBox(gripsegment2, 119, 103, 2.6875F, -17.125F, 1.4844F, 1, 1, 1, 0.0F, false));
        gripsegment2.cubeList.add(new ModelBox(gripsegment2, 119, 103, -3.6875F, -17.125F, -1.0F, 1, 1, 2, 0.0F, true));
    }

    @Override
    protected void render() {
        gripsegment1.render(1f);
        gripsegment2.render(1f);
    }
}
