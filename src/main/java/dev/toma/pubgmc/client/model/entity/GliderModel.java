package dev.toma.pubgmc.client.model.entity;

import dev.toma.pubgmc.common.entity.vehicle.air.GliderEntity;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;
import net.minecraft.util.math.MathHelper;

public class GliderModel extends DriveableModel<GliderEntity> {

    private final RendererModel main;
    private final RendererModel rotorMain;

    public GliderModel() {
        textureWidth = 128;
        textureHeight = 128;

        main = new RendererModel(this);
        main.setRotationPoint(0.0F, 24.0F, 0.0F);

        RendererModel cockpit = new RendererModel(this);
        cockpit.setRotationPoint(0.0F, 0.0F, 0.0F);
        main.addChild(cockpit);
        cockpit.cubeList.add(new ModelBox(cockpit, 0, 0, -6.0F, -2.0F, -10.0F, 12, 1, 32, 0.0F, false));
        cockpit.cubeList.add(new ModelBox(cockpit, 0, 0, -6.0F, -4.15F, -12.15F, 12, 1, 1, 0.0F, false));
        cockpit.cubeList.add(new ModelBox(cockpit, 0, 0, -6.0F, -3.0F, -10.4F, 12, 1, 9, 0.0F, false));
        cockpit.cubeList.add(new ModelBox(cockpit, 0, 0, 5.0F, -4.0F, -2.0F, 1, 2, 22, 0.0F, false));
        cockpit.cubeList.add(new ModelBox(cockpit, 0, 0, -6.0F, -4.0F, -2.0F, 1, 2, 18, 0.0F, false));
        cockpit.cubeList.add(new ModelBox(cockpit, 0, 0, 5.0F, -9.35F, 15.7F, 1, 8, 6, 0.0F, false));
        cockpit.cubeList.add(new ModelBox(cockpit, 0, 0, -6.0F, -9.35F, 15.7F, 1, 8, 6, 0.0F, false));
        cockpit.cubeList.add(new ModelBox(cockpit, 0, 0, -6.0F, -3.0F, 17.0F, 12, 1, 5, 0.0F, false));

        RendererModel bone3 = new RendererModel(this);
        bone3.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone3, -0.7854F, 0.0F, 0.0F);
        cockpit.addChild(bone3);
        bone3.cubeList.add(new ModelBox(bone3, 0, 0, -6.0F, 2.35F, -10.8F, 12, 4, 3, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 0, 0, -6.0F, -21.95F, 5.7F, 1, 4, 3, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 0, 0, 5.0F, -21.95F, 5.7F, 1, 4, 3, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 0, 0, -6.0F, -21.95F, 6.7F, 12, 5, 7, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 0, 0, 5.0F, -4.05F, -7.5F, 1, 3, 6, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 0, 0, -6.0F, -4.05F, -7.5F, 1, 3, 6, 0.0F, false));

        RendererModel bone4 = new RendererModel(this);
        bone4.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone4, 0.7854F, 0.0F, 0.0F);
        cockpit.addChild(bone4);
        bone4.cubeList.add(new ModelBox(bone4, 0, 0, -6.0F, -11.55F, -5.65F, 12, 4, 4, 0.0F, false));

        RendererModel bone5 = new RendererModel(this);
        bone5.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone5, 0.1745F, 0.0F, 0.0F);
        cockpit.addChild(bone5);
        bone5.cubeList.add(new ModelBox(bone5, 0, 0, -6.0F, -8.5F, -8.0F, 12, 6, 7, 0.0F, false));

        RendererModel side = new RendererModel(this);
        side.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(side, 0.4363F, 0.0F, 0.0F);
        cockpit.addChild(side);
        side.cubeList.add(new ModelBox(side, 0, 0, 5.0F, -2.15F, 3.6F, 1, 2, 19, 0.0F, false));
        side.cubeList.add(new ModelBox(side, 0, 0, -6.0F, -2.15F, 3.6F, 1, 2, 19, 0.0F, false));
        side.cubeList.add(new ModelBox(side, 0, 0, 5.0F, -0.95F, 9.0F, 1, 4, 10, 0.0F, false));
        side.cubeList.add(new ModelBox(side, 0, 0, -6.0F, -0.95F, 9.0F, 1, 4, 10, 0.0F, false));
        side.cubeList.add(new ModelBox(side, 0, 0, -5.0F, 0.65F, 6.6F, 10, 1, 16, 0.0F, false));

        RendererModel seat = new RendererModel(this);
        seat.setRotationPoint(0.0F, 0.0F, 0.0F);
        main.addChild(seat);
        seat.cubeList.add(new ModelBox(seat, 0, 89, -4.0F, -3.0F, -1.0F, 8, 1, 7, 0.0F, false));

        RendererModel angle = new RendererModel(this);
        angle.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(angle, 1.1345F, 0.0F, 0.0F);
        seat.addChild(angle);
        angle.cubeList.add(new ModelBox(angle, 2, 88, -4.0F, 4.0F, 5.0F, 8, 1, 7, 0.0F, false));

        RendererModel seat2 = new RendererModel(this);
        seat2.setRotationPoint(0.0F, 0.0F, 0.0F);
        main.addChild(seat2);
        seat2.cubeList.add(new ModelBox(seat2, 0, 91, -4.0F, -8.0F, 10.0F, 8, 1, 7, 0.0F, false));

        RendererModel angle2 = new RendererModel(this);
        angle2.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(angle2, 1.1345F, 0.0F, 0.0F);
        seat2.addChild(angle2);
        angle2.cubeList.add(new ModelBox(angle2, 5, 92, -4.0F, 11.8563F, 14.1803F, 8, 1, 7, 0.0F, false));

        RendererModel glass = new RendererModel(this);
        glass.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(glass, -0.5236F, 0.0F, 0.0F);
        main.addChild(glass);
        glass.cubeList.add(new ModelBox(glass, 4, 117, -3.0F, -11.2804F, -7.0464F, 6, 6, 1, 0.0F, false));

        RendererModel left2 = new RendererModel(this);
        left2.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(left2, 0.0F, -0.3491F, 0.0F);
        glass.addChild(left2);
        left2.cubeList.add(new ModelBox(left2, 7, 114, 0.3841F, -10.2804F, -7.6436F, 3, 5, 1, 0.0F, false));

        RendererModel right2 = new RendererModel(this);
        right2.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(right2, 0.0F, 0.3491F, 0.0F);
        glass.addChild(right2);
        right2.cubeList.add(new ModelBox(right2, 2, 115, -3.3841F, -10.2804F, -7.6436F, 3, 5, 1, 0.0F, false));

        RendererModel wheel = new RendererModel(this);
        wheel.setRotationPoint(0.0F, 0.0F, 0.0F);
        main.addChild(wheel);
        wheel.cubeList.add(new ModelBox(wheel, 0, 57, -1.0F, -1.0F, -5.0F, 2, 1, 3, 0.0F, false));
        wheel.cubeList.add(new ModelBox(wheel, 0, 65, -1.0F, 1.15F, -7.15F, 2, 2, 1, 0.0F, false));
        wheel.cubeList.add(new ModelBox(wheel, 0, 70, -1.0F, 1.15F, -0.85F, 2, 2, 1, 0.0F, false));
        wheel.cubeList.add(new ModelBox(wheel, 0, 60, -1.0F, 4.3F, -5.0F, 2, 1, 3, 0.0F, false));
        wheel.cubeList.add(new ModelBox(wheel, 0, 90, -0.5F, -0.3F, -5.45F, 1, 5, 4, 0.0F, false));
        wheel.cubeList.add(new ModelBox(wheel, 7, 92, -0.5F, 0.7F, -6.45F, 1, 3, 6, 0.0F, false));

        RendererModel wh0 = new RendererModel(this);
        wh0.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(wh0, -0.7854F, 0.0F, 0.0F);
        wheel.addChild(wh0);
        wh0.cubeList.add(new ModelBox(wh0, 0, 53, -1.0F, 0.6937F, -2.0936F, 2, 1, 3, 0.0F, false));
        wh0.cubeList.add(new ModelBox(wh0, 0, 60, -1.0F, 6.2937F, -2.7936F, 2, 1, 3, 0.0F, false));

        RendererModel wh1 = new RendererModel(this);
        wh1.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(wh1, 0.7854F, 0.0F, 0.0F);
        wheel.addChild(wh1);
        wh1.cubeList.add(new ModelBox(wh1, 3, 64, -1.0F, -4.2436F, -5.8437F, 2, 1, 3, 0.0F, false));
        wh1.cubeList.add(new ModelBox(wh1, 0, 56, -1.0F, 1.3564F, -5.1437F, 2, 1, 3, 0.0F, false));

        RendererModel wheel2 = new RendererModel(this);
        wheel2.setRotationPoint(0.0F, 0.0F, 0.0F);
        main.addChild(wheel2);
        wheel2.cubeList.add(new ModelBox(wheel2, 0, 54, 14.0F, 0.0F, 15.0F, 2, 1, 3, 0.0F, false));
        wheel2.cubeList.add(new ModelBox(wheel2, 0, 58, 14.0F, 2.15F, 12.85F, 2, 2, 1, 0.0F, false));
        wheel2.cubeList.add(new ModelBox(wheel2, 0, 58, 14.0F, 2.15F, 19.15F, 2, 2, 1, 0.0F, false));
        wheel2.cubeList.add(new ModelBox(wheel2, 0, 56, 14.0F, 5.3F, 15.0F, 2, 1, 3, 0.0F, false));
        wheel2.cubeList.add(new ModelBox(wheel2, 0, 90, 14.5F, 0.7F, 14.55F, 1, 5, 4, 0.0F, false));
        wheel2.cubeList.add(new ModelBox(wheel2, 0, 93, 14.5F, 1.7F, 13.55F, 1, 3, 6, 0.0F, false));

        RendererModel wh2 = new RendererModel(this);
        wh2.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(wh2, -0.7854F, 0.0F, 0.0F);
        wheel2.addChild(wh2);
        wh2.cubeList.add(new ModelBox(wh2, 0, 58, 14.0F, -12.7414F, 12.7556F, 2, 1, 3, 0.0F, false));
        wh2.cubeList.add(new ModelBox(wh2, 0, 55, 14.0F, -7.1414F, 12.0556F, 2, 1, 3, 0.0F, false));

        RendererModel wh3 = new RendererModel(this);
        wh3.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(wh3, 0.7854F, 0.0F, 0.0F);
        wheel2.addChild(wh3);
        wh3.cubeList.add(new ModelBox(wh3, 0, 59, 14.0F, 10.6056F, 7.5914F, 2, 1, 3, 0.0F, false));
        wh3.cubeList.add(new ModelBox(wh3, 0, 56, 14.0F, 16.2056F, 8.2914F, 2, 1, 3, 0.0F, false));

        RendererModel wheel3 = new RendererModel(this);
        wheel3.setRotationPoint(0.0F, 0.0F, 0.0F);
        main.addChild(wheel3);
        wheel3.cubeList.add(new ModelBox(wheel3, 0, 51, -16.0F, 0.0F, 15.0F, 2, 1, 3, 0.0F, false));
        wheel3.cubeList.add(new ModelBox(wheel3, 0, 68, -16.0F, 2.15F, 12.85F, 2, 2, 1, 0.0F, false));
        wheel3.cubeList.add(new ModelBox(wheel3, 0, 54, -16.0F, 2.15F, 19.15F, 2, 2, 1, 0.0F, false));
        wheel3.cubeList.add(new ModelBox(wheel3, 0, 53, -16.0F, 5.3F, 15.0F, 2, 1, 3, 0.0F, false));
        wheel3.cubeList.add(new ModelBox(wheel3, 3, 90, -15.5F, 0.7F, 14.55F, 1, 5, 4, 0.0F, false));
        wheel3.cubeList.add(new ModelBox(wheel3, 12, 90, -15.5F, 1.7F, 13.55F, 1, 3, 6, 0.0F, false));

        RendererModel wh4 = new RendererModel(this);
        wh4.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(wh4, -0.7854F, 0.0F, 0.0F);
        wheel3.addChild(wh4);
        wh4.cubeList.add(new ModelBox(wh4, 0, 53, -16.0F, -12.7414F, 12.7556F, 2, 1, 3, 0.0F, false));
        wh4.cubeList.add(new ModelBox(wh4, 0, 54, -16.0F, -7.1414F, 12.0556F, 2, 1, 3, 0.0F, false));

        RendererModel wh5 = new RendererModel(this);
        wh5.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(wh5, 0.7854F, 0.0F, 0.0F);
        wheel3.addChild(wh5);
        wh5.cubeList.add(new ModelBox(wh5, 0, 58, -16.0F, 10.6056F, 7.5914F, 2, 1, 3, 0.0F, false));
        wh5.cubeList.add(new ModelBox(wh5, 0, 56, -16.0F, 16.2056F, 8.2914F, 2, 1, 3, 0.0F, false));

        RendererModel back = new RendererModel(this);
        back.setRotationPoint(0.0F, 0.0F, 0.0F);
        main.addChild(back);
        back.cubeList.add(new ModelBox(back, 0, 0, -1.0F, -9.0F, 22.0F, 2, 2, 5, 0.0F, false));

        rotorMain = new RendererModel(this);
        rotorMain.setRotationPoint(0.0F, -8.0F, 29.0F);
        back.addChild(rotorMain);
        rotorMain.cubeList.add(new ModelBox(rotorMain, 0, 64, -1.0F, -1.0F, -2.0F, 2, 2, 3, 0.0F, false));

        RendererModel rotor0 = new RendererModel(this);
        rotor0.setRotationPoint(0.0F, 0.0F, 0.0F);
        rotorMain.addChild(rotor0);
        rotor0.cubeList.add(new ModelBox(rotor0, 0, 59, -1.0F, -11.0F, -1.0F, 2, 10, 1, 0.0F, false));

        RendererModel rotor1 = new RendererModel(this);
        rotor1.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(rotor1, 0.0F, 0.0F, -2.0944F);
        rotorMain.addChild(rotor1);
        rotor1.cubeList.add(new ModelBox(rotor1, 0, 59, -1.0F, -11.0F, -1.0F, 2, 11, 1, 0.0F, false));

        RendererModel rotor2 = new RendererModel(this);
        rotor2.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(rotor2, 0.0F, 0.0F, 2.0944F);
        rotorMain.addChild(rotor2);
        rotor2.cubeList.add(new ModelBox(rotor2, 0, 59, -1.0F, -11.0F, -1.0F, 2, 11, 1, 0.0F, false));

        RendererModel wheelcover = new RendererModel(this);
        wheelcover.setRotationPoint(0.0F, 0.0F, 0.0F);
        main.addChild(wheelcover);
        wheelcover.cubeList.add(new ModelBox(wheelcover, 0, 0, 13.0F, -1.5F, 14.0F, 4, 3, 5, 0.0F, false));
        wheelcover.cubeList.add(new ModelBox(wheelcover, 0, 0, 13.0F, 1.35F, 20.8F, 4, 2, 1, 0.0F, false));
        wheelcover.cubeList.add(new ModelBox(wheelcover, 0, 0, 13.0F, 1.35F, 11.2F, 4, 2, 1, 0.0F, false));
        wheelcover.cubeList.add(new ModelBox(wheelcover, 0, 0, 16.0F, 1.35F, 12.2F, 1, 2, 9, 0.0F, false));
        wheelcover.cubeList.add(new ModelBox(wheelcover, 0, 0, 13.0F, 1.35F, 12.2F, 1, 2, 9, 0.0F, false));
        wheelcover.cubeList.add(new ModelBox(wheelcover, 0, 0, 14.0F, -6.4F, 20.35F, 2, 2, 2, 0.0F, false));

        RendererModel bone = new RendererModel(this);
        bone.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone, 0.7854F, 0.0F, 0.0F);
        wheelcover.addChild(bone);
        bone.cubeList.add(new ModelBox(bone, 0, 0, 13.0F, 8.85F, 6.95F, 4, 2, 4, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 0, 0, 14.0F, 9.9F, 11.95F, 2, 2, 7, 0.0F, false));

        RendererModel bone6 = new RendererModel(this);
        bone6.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone6, 0.2618F, 0.0F, 0.0F);
        bone.addChild(bone6);
        bone6.cubeList.add(new ModelBox(bone6, 0, 0, 14.0F, 15.15F, 10.0F, 2, 2, 5, 0.0F, false));

        RendererModel bone2 = new RendererModel(this);
        bone2.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone2, -0.7854F, 0.0F, 0.0F);
        wheelcover.addChild(bone2);
        bone2.cubeList.add(new ModelBox(bone2, 0, 0, 13.0F, -14.5F, 12.35F, 4, 2, 4, 0.0F, false));

        RendererModel wheelcover2 = new RendererModel(this);
        wheelcover2.setRotationPoint(0.0F, 0.0F, 0.0F);
        main.addChild(wheelcover2);
        wheelcover2.cubeList.add(new ModelBox(wheelcover2, 0, 0, -17.0F, -1.5F, 14.0F, 4, 3, 5, 0.0F, false));
        wheelcover2.cubeList.add(new ModelBox(wheelcover2, 0, 0, -17.0F, 1.35F, 20.8F, 4, 2, 1, 0.0F, false));
        wheelcover2.cubeList.add(new ModelBox(wheelcover2, 0, 0, -17.0F, 1.35F, 11.2F, 4, 2, 1, 0.0F, false));
        wheelcover2.cubeList.add(new ModelBox(wheelcover2, 0, 0, -14.0F, 1.35F, 12.2F, 1, 2, 9, 0.0F, false));
        wheelcover2.cubeList.add(new ModelBox(wheelcover2, 0, 0, -17.0F, 1.35F, 12.2F, 1, 2, 9, 0.0F, false));
        wheelcover2.cubeList.add(new ModelBox(wheelcover2, 0, 0, -16.0F, -6.4F, 20.35F, 2, 2, 2, 0.0F, false));

        RendererModel bone7 = new RendererModel(this);
        bone7.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone7, 0.7854F, 0.0F, 0.0F);
        wheelcover2.addChild(bone7);
        bone7.cubeList.add(new ModelBox(bone7, 0, 0, -17.0F, 8.85F, 6.95F, 4, 2, 4, 0.0F, false));
        bone7.cubeList.add(new ModelBox(bone7, 0, 0, -16.0F, 9.9F, 11.95F, 2, 2, 7, 0.0F, false));

        RendererModel bone8 = new RendererModel(this);
        bone8.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone8, 0.2618F, 0.0F, 0.0F);
        bone7.addChild(bone8);
        bone8.cubeList.add(new ModelBox(bone8, 0, 0, -16.0F, 15.15F, 10.0F, 2, 2, 5, 0.0F, false));

        RendererModel bone9 = new RendererModel(this);
        bone9.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone9, -0.7854F, 0.0F, 0.0F);
        wheelcover2.addChild(bone9);
        bone9.cubeList.add(new ModelBox(bone9, 0, 0, -17.0F, -14.5F, 12.35F, 4, 2, 4, 0.0F, false));

        RendererModel holders = new RendererModel(this);
        holders.setRotationPoint(0.0F, 0.0F, 0.0F);
        main.addChild(holders);
        holders.cubeList.add(new ModelBox(holders, 48, 88, -6.5F, -7.5F, 0.0F, 13, 1, 1, 0.0F, false));

        RendererModel bone10 = new RendererModel(this);
        bone10.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone10, 0.0F, 0.0F, 0.6981F);
        holders.addChild(bone10);
        bone10.cubeList.add(new ModelBox(bone10, 51, 100, 0.5F, -8.75F, 16.0F, 12, 1, 1, 0.0F, false));

        RendererModel bone11 = new RendererModel(this);
        bone11.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone11, 0.0F, 0.0F, -0.6981F);
        holders.addChild(bone11);
        bone11.cubeList.add(new ModelBox(bone11, 56, 99, -12.0F, -8.25F, 16.0F, 12, 1, 1, 0.0F, false));

        RendererModel bone12 = new RendererModel(this);
        bone12.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone12, 0.0F, 0.0F, 0.2618F);
        holders.addChild(bone12);
        bone12.cubeList.add(new ModelBox(bone12, 51, 92, -0.3F, -2.75F, 16.0F, 14, 1, 1, 0.0F, false));

        RendererModel bone13 = new RendererModel(this);
        bone13.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone13, 0.0F, 0.0F, -0.2618F);
        holders.addChild(bone13);
        bone13.cubeList.add(new ModelBox(bone13, 54, 101, -13.7F, -2.75F, 16.0F, 14, 1, 1, 0.0F, false));

        RendererModel bone17 = new RendererModel(this);
        bone17.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone17, 0.3491F, 0.0F, -0.2618F);
        holders.addChild(bone17);
        bone17.cubeList.add(new ModelBox(bone17, 48, 88, 7.5F, -23.5F, 21.0F, 1, 22, 1, 0.0F, false));

        RendererModel bone18 = new RendererModel(this);
        bone18.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone18, 0.3491F, 0.0F, 0.2618F);
        holders.addChild(bone18);
        bone18.cubeList.add(new ModelBox(bone18, 48, 88, -8.5F, -23.5F, 21.0F, 1, 22, 1, 0.0F, false));

        RendererModel bone19 = new RendererModel(this);
        bone19.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone19, -0.6109F, 0.0F, 0.0F);
        holders.addChild(bone19);
        bone19.cubeList.add(new ModelBox(bone19, 48, 88, -0.5F, -30.5F, -10.0F, 1, 29, 1, 0.0F, false));

        RendererModel bone20 = new RendererModel(this);
        bone20.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone20, -0.3491F, 0.0F, 0.2618F);
        holders.addChild(bone20);
        bone20.cubeList.add(new ModelBox(bone20, 48, 88, -8.1F, -30.2F, -1.8F, 1, 25, 1, 0.0F, false));

        RendererModel bone21 = new RendererModel(this);
        bone21.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone21, -0.3491F, 0.0F, -0.2618F);
        holders.addChild(bone21);
        bone21.cubeList.add(new ModelBox(bone21, 48, 88, 7.1F, -30.2F, -1.9F, 1, 25, 1, 0.0F, false));

        RendererModel frontwheelcover = new RendererModel(this);
        frontwheelcover.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(frontwheelcover, 0.2618F, 0.0F, 0.0F);
        main.addChild(frontwheelcover);
        frontwheelcover.cubeList.add(new ModelBox(frontwheelcover, 0, 0, -2.0F, -4.0F, -9.0F, 4, 4, 1, 0.0F, false));
        frontwheelcover.cubeList.add(new ModelBox(frontwheelcover, 0, 0, 1.0F, -4.0F, -8.0F, 1, 4, 7, 0.0F, false));
        frontwheelcover.cubeList.add(new ModelBox(frontwheelcover, 0, 0, 1.0F, -2.0F, -1.0F, 1, 2, 5, 0.0F, false));
        frontwheelcover.cubeList.add(new ModelBox(frontwheelcover, 0, 0, -2.0F, -4.0F, -8.0F, 1, 4, 7, 0.0F, false));
        frontwheelcover.cubeList.add(new ModelBox(frontwheelcover, 0, 0, -2.0F, -2.0F, -1.0F, 1, 2, 5, 0.0F, false));

        RendererModel top = new RendererModel(this);
        top.setRotationPoint(0.0F, 0.0F, 0.0F);
        main.addChild(top);
        top.cubeList.add(new ModelBox(top, 0, 50, 52.7F, -32.0F, 28.8F, 1, 1, 3, 0.0F, false));
        top.cubeList.add(new ModelBox(top, 0, 53, -0.5F, -31.0F, 9.0F, 1, 1, 4, 0.0F, false));
        top.cubeList.add(new ModelBox(top, 0, 50, 52.7F, -35.0F, 31.8F, 1, 4, 1, 0.0F, false));
        top.cubeList.add(new ModelBox(top, 0, 50, 52.7F, -32.1F, 28.9F, 1, 1, 3, 0.0F, false));
        top.cubeList.add(new ModelBox(top, 0, 50, -53.8F, -35.0F, 31.8F, 1, 4, 1, 0.0F, false));
        top.cubeList.add(new ModelBox(top, 0, 50, -53.8F, -32.0F, 28.8F, 1, 1, 3, 0.0F, false));
        top.cubeList.add(new ModelBox(top, 0, 0, -23.1F, -31.5F, 12.75F, 48, 0, 9, 0.0F, false));

        RendererModel left = new RendererModel(this);
        left.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(left, 0.0F, -0.5236F, 0.0F);
        top.addChild(left);
        left.cubeList.add(new ModelBox(left, 0, 50, 1.9F, -32.0F, -2.0F, 59, 1, 4, 0.0F, false));
        left.cubeList.add(new ModelBox(left, 0, 50, -1.1F, -32.0F, 2.0F, 46, 1, 2, 0.0F, false));
        left.cubeList.add(new ModelBox(left, 0, 50, -1.1F, -32.0F, 4.0F, 39, 1, 2, 0.0F, false));
        left.cubeList.add(new ModelBox(left, 0, 50, -1.1F, -32.0F, -2.0F, 3, 1, 4, 0.0F, false));

        RendererModel left3 = new RendererModel(this);
        left3.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(left3, 0.0F, -0.3491F, 0.0F);
        top.addChild(left3);
        left3.cubeList.add(new ModelBox(left3, 0, 50, 3.9F, -32.0F, 9.75F, 57, 1, 3, 0.0F, false));
        left3.cubeList.add(new ModelBox(left3, 0, 50, 1.9F, -32.0F, 4.75F, 19, 1, 6, 0.0F, false));

        RendererModel right3 = new RendererModel(this);
        right3.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(right3, 0.0F, 0.3491F, 0.0F);
        top.addChild(right3);
        right3.cubeList.add(new ModelBox(right3, 0, 50, -61.1F, -32.0F, 9.65F, 58, 1, 3, 0.0F, false));
        right3.cubeList.add(new ModelBox(right3, 0, 50, -33.1F, -32.0F, 5.65F, 31, 1, 4, 0.0F, false));

        RendererModel right = new RendererModel(this);
        right.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(right, 0.0F, 0.5236F, 0.0F);
        top.addChild(right);
        right.cubeList.add(new ModelBox(right, 0, 65, -61.0F, -32.0F, -2.0F, 52, 1, 4, 0.0F, false));
        right.cubeList.add(new ModelBox(right, 0, 50, -44.0F, -32.0F, 2.0F, 45, 1, 3, 0.0F, false));
        right.cubeList.add(new ModelBox(right, 0, 65, -9.0F, -32.0F, -2.0F, 10, 1, 4, 0.0F, false));

        RendererModel bone14 = new RendererModel(this);
        bone14.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone14, -0.7854F, 0.0F, 0.0F);
        top.addChild(bone14);
        bone14.cubeList.add(new ModelBox(bone14, 0, 50, 52.7F, -47.2F, -2.25F, 1, 4, 1, 0.0F, false));
        bone14.cubeList.add(new ModelBox(bone14, 0, 50, 52.7F, -46.2F, -1.3F, 1, 2, 1, 0.0F, false));
        bone14.cubeList.add(new ModelBox(bone14, 0, 50, -53.8F, -47.2F, -2.25F, 1, 4, 1, 0.0F, false));
        bone14.cubeList.add(new ModelBox(bone14, 0, 50, -53.8F, -46.2F, -1.3F, 1, 2, 1, 0.0F, false));

        RendererModel bone15 = new RendererModel(this);
        bone15.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone15, 0.0F, -0.0873F, 0.0F);
        top.addChild(bone15);
        bone15.cubeList.add(new ModelBox(bone15, 0, 0, 1.9F, -31.5F, 23.75F, 53, 0, 4, 0.0F, false));
        bone15.cubeList.add(new ModelBox(bone15, 0, 0, 1.9F, -31.5F, 19.75F, 38, 0, 4, 0.0F, false));

        RendererModel bone16 = new RendererModel(this);
        bone16.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone16, 0.0F, 0.0873F, 0.0F);
        top.addChild(bone16);
        bone16.cubeList.add(new ModelBox(bone16, 0, 0, -55.1F, -31.5F, 23.75F, 53, 0, 4, 0.0F, false));
        bone16.cubeList.add(new ModelBox(bone16, 0, 0, -47.1F, -31.5F, 19.75F, 48, 0, 4, 0.0F, false));
    }

    @Override
    public void doRender(GliderEntity glider, float partialTicks) {
        if(glider != null) {
            float throttle = glider.throttle;
            setRotationAngle(rotorMain, 0, 0, MathHelper.lerp(partialTicks, glider.ticksExisted - 1, glider.ticksExisted) * throttle * 0.5F);
        }
        main.render(1f);
    }
}
