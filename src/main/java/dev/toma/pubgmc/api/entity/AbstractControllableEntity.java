package dev.toma.pubgmc.api.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public abstract class AbstractControllableEntity extends Entity implements IControllableEntity {

    public boolean forwardKey;
    public boolean backwardKey;
    public boolean rightKey;
    public boolean leftKey;

    public AbstractControllableEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public void onInputUpdate(boolean forward, boolean backward, boolean right, boolean left) {
        this.forwardKey = forward;
        this.backwardKey = backward;
        this.rightKey = right;
        this.leftKey = left;
    }

    @Override
    public final void tick() {
        this.updateMovement();
        this.updateEntityPre();
        super.tick();
        this.updateEntityPost();
    }

    @Nullable
    @Override
    public Entity getControllingPassenger() {
        List<Entity> list = this.getPassengers();
        return list.isEmpty() ? null : list.get(0);
    }

    protected void updateMovement() {
        if(forwardKey && !backwardKey) {
            this.moveForward();
        }
        if(!forwardKey && backwardKey) {
            this.moveBackward();
        }
        if(rightKey && !leftKey) {
            this.moveRight();
        }
        if(!rightKey && leftKey) {
            this.moveLeft();
        }
    }

    /** Called before {@link Entity#tick()} is called */
    protected void updateEntityPre() {

    }

    /** Called after {@link Entity#tick()} has been called*/
    protected void updateEntityPost() {

    }

    /** Use this to apply logic when forward key is being held down */
    protected void moveForward() {

    }

    /** Use this to apply logic when backward key is being held down */
    protected void moveBackward() {

    }

    /** Use this to apply logic when right key is being held down */
    protected void moveRight() {

    }

    /** Use this to apply logic when left key is being held down */
    protected void moveLeft() {

    }

    protected boolean noKeyInput() {
        return noVerticalKey() && noRotateKey();
    }

    protected boolean noVerticalKey() {
        return !forwardKey && !backwardKey;
    }

    protected boolean noRotateKey() {
        return !rightKey && !leftKey;
    }
}
