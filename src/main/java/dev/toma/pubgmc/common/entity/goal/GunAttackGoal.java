package dev.toma.pubgmc.common.entity.goal;

import dev.toma.pubgmc.common.entity.BotEntity;
import dev.toma.pubgmc.common.item.gun.core.AbstractGunItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.ItemStack;

import java.util.EnumSet;

public class GunAttackGoal extends Goal {

    private final BotEntity entity;
    private int seeTime;
    private boolean attacking;
    private int attackDelay;
    private int shotsFired;

    public GunAttackGoal(BotEntity entity) {
        this.entity = entity;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean shouldExecute() {
        LivingEntity target = entity.getAttackTarget();
        boolean f = hasGun();
        return entity.getAttackTarget() != null && hasGun();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return (entity.getAttackTarget() != null || !entity.getNavigator().noPath()) && hasGun();
    }

    @Override
    public void startExecuting() {

    }

    @Override
    public void resetTask() {
        seeTime = 0;
        attacking = false;
        attackDelay = 10;
    }

    @Override
    public void tick() {
        LivingEntity target = entity.getAttackTarget();
        if(target != null) {
            float distanceToTarget = entity.getDistance(target);
            boolean canSee = entity.getEntitySenses().canSee(target);
            boolean hasSeen = seeTime > 0;
            if(canSee != hasSeen) {
                seeTime = 0;
            }
            if(canSee) {
                ++seeTime;
            } else {
                --seeTime;
            }
            if(hasGun()) {
                ItemStack stack = entity.getHeldItemMainhand();
                AbstractGunItem gun = (AbstractGunItem) stack.getItem();
                int requiredSeeTime = requiredSeeTime(distanceToTarget);
                if(!(distanceToTarget > gun.getCategory().getRange()) && seeTime > requiredSeeTime) {
                    entity.getNavigator().clearPath();
                    this.attacking = true;
                } else {
                    entity.getNavigator().tryMoveToEntityLiving(target, 1.0);
                    this.attacking = false;
                }
                entity.getLookController().setLookPositionWithEntity(target, 30, 30);
                this.entity.faceEntity(target, 30.0F, 30.0F);
                if(attacking) {
                    if(canSee) {
                        --attackDelay;
                        if(attackDelay <= 0) {
                            gun.shoot(entity, entity.world, stack);
                            attackDelay = gun.getFirerate() + 2;
                            ++shotsFired;
                            if(shotsFired >= gun.getMaxAmmo(stack)) {
                                shotsFired = 0;
                                attackDelay = gun.getReloadTime(stack);
                            }
                        }
                    } else {
                        attacking = false;
                    }
                }
            }
        }
    }

    protected int requiredSeeTime(float distance) {
        return Math.max(5, (int)(distance * 1.1f));
    }

    protected boolean hasGun() {
        return entity.getHeldItemMainhand().getItem() instanceof AbstractGunItem;
    }

    protected AbstractGunItem getGun() {
        return (AbstractGunItem) entity.getHeldItemMainhand().getItem();
    }
}
