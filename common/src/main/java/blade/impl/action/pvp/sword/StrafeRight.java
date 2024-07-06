package blade.impl.action.pvp.sword;

import blade.impl.ConfigKeys;
import blade.impl.StateKeys;
import blade.impl.util.AttackUtil;
import blade.planner.score.ScoreState;
import blade.util.BotMath;
import blade.util.blade.BladeAction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.concurrent.ThreadLocalRandom;

public class StrafeRight extends BladeAction implements Sword {
    @Override
    public void onTick() {
        lookAtEnemy(bot, tick);

        bot.setMoveRight(true);
        bot.setMoveLeft(false);
    }

    @Override
    public boolean isSatisfied() {
        return isPvPSatisfied(bot);
    }

    @Override
    public void onRelease(BladeAction next) {
        super.onRelease(next);
        bot.setMoveRight(false);
    }

    @Override
    public void getResult(ScoreState result) {
        result.setValue(StateKeys.DOING_PVP, 1.0);
    }

    @Override
    public double getScore() {
        LivingEntity target = bot.getBlade().get(ConfigKeys.TARGET);
        Vec3 eyePos = bot.getVanillaPlayer().getEyePosition();
        Vec3 closestPoint = BotMath.getClosestPoint(eyePos, target.getBoundingBox());
        double distSq = closestPoint.distanceToSqr(eyePos);

        return getSwordScore(bot) +
                (1 - Math.min(distSq / (24 * 24), 1)) +
                AttackUtil.isAttacking(target, bot.getVanillaPlayer()) +
                Math.min(tick / 3.0, 1.4) +
                ThreadLocalRandom.current().nextDouble() * 0.6;
    }
}
