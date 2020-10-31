package dev.toma.pubgmc.client.model.gun.attachment;

import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;

public class ARSuppressorModel extends AttachmentModel {

    private final RendererModel bone;
    private final RendererModel bone2;
    private final RendererModel bone3;

    public ARSuppressorModel() {
        textureWidth = 128;
        textureHeight = 128;

        bone = new RendererModel(this);
        bone.setRotationPoint(0.0F, 24.0F, 0.0F);
        bone.cubeList.add(new ModelBox(bone, 67, 66, -1.0F, 1.7321F, -2.1719F, 2, 2, 23, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 67, 66, -3.7321F, -1.0F, -2.1719F, 3, 2, 23, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 67, 66, 0.7321F, -1.0F, -2.1719F, 3, 2, 23, 0.0F, true));
        bone.cubeList.add(new ModelBox(bone, 67, 66, -1.0F, -3.7321F, -2.1719F, 2, 2, 23, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 70, 68, -1.0F, 1.7321F, -7.1719F, 2, 2, 5, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 70, 68, -3.7321F, -1.0F, -7.1719F, 2, 2, 5, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 70, 68, 1.7321F, -1.0F, -7.1719F, 2, 2, 5, 0.0F, true));
        bone.cubeList.add(new ModelBox(bone, 70, 68, -1.0F, -3.7321F, -7.1719F, 2, 2, 5, 0.0F, false));

        bone2 = new RendererModel(this);
        bone2.setRotationPoint(0.0F, 24.0F, 0.0F);
        setRotationAngle(bone2, 0.0F, 0.0F, 0.5236F);
        bone2.cubeList.add(new ModelBox(bone2, 67, 66, -1.0F, -0.2679F, -2.1719F, 2, 4, 23, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 67, 66, 0.7321F, -1.0F, -2.1719F, 3, 2, 23, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 67, 66, -3.7321F, -1.0F, -2.1719F, 3, 2, 23, 0.0F, true));
        bone2.cubeList.add(new ModelBox(bone2, 67, 66, -1.0F, -3.7321F, -2.1719F, 2, 4, 23, 0.0F, true));
        bone2.cubeList.add(new ModelBox(bone2, 70, 68, -1.0F, 1.7321F, -7.1719F, 2, 2, 5, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 70, 68, 1.7321F, -1.0F, -7.1719F, 2, 2, 5, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 70, 68, -3.7321F, -1.0F, -7.1719F, 2, 2, 5, 0.0F, true));
        bone2.cubeList.add(new ModelBox(bone2, 70, 68, -1.0F, -3.7321F, -7.1719F, 2, 2, 5, 0.0F, true));

        bone3 = new RendererModel(this);
        bone3.setRotationPoint(0.0F, 24.0F, 0.0F);
        setRotationAngle(bone3, 0.0F, 0.0F, -0.5236F);
        bone3.cubeList.add(new ModelBox(bone3, 67, 66, -1.0F, -0.2679F, -2.1719F, 2, 4, 23, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 67, 66, -3.7321F, -1.0F, -2.1719F, 3, 2, 23, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 67, 66, -1.0F, -3.7321F, -2.1719F, 2, 4, 23, 0.0F, true));
        bone3.cubeList.add(new ModelBox(bone3, 67, 66, 0.7321F, -1.0F, -2.1719F, 3, 2, 23, 0.0F, true));
        bone3.cubeList.add(new ModelBox(bone3, 70, 68, -1.0F, 1.7321F, -7.1719F, 2, 2, 5, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 70, 68, -3.7321F, -1.0F, -7.1719F, 2, 2, 5, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 70, 68, -1.0F, -3.7321F, -7.1719F, 2, 2, 5, 0.0F, true));
        bone3.cubeList.add(new ModelBox(bone3, 70, 68, 1.7321F, -1.0F, -7.1719F, 2, 2, 5, 0.0F, true));
    }

    @Override
    public void render() {
        bone.render(1f);
        bone2.render(1f);
        bone3.render(1f);
    }
}
