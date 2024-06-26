package blade.impl.action.pvp.sword;

import blade.impl.ConfigKeys;
import blade.planner.score.ScoreAction;
import blade.state.BladeState;
import net.minecraft.world.entity.LivingEntity;

import java.util.concurrent.ThreadLocalRandom;

public class STap extends ScoreAction implements Sword {
    @Override
    public void onTick() {
        lookAtEnemy(bot, tick);

        bot.setMoveBackward(true);
        bot.setMoveForward(false);
    }

    @Override
    public void getResult(BladeState result) {

    }

    @Override
    public double getScore() {
        LivingEntity target = bot.getBlade().get(ConfigKeys.TARGET);
        return getSwordScore(bot) +
                target.hurtTime / 6.0 +
                ThreadLocalRandom.current().nextDouble() * 0.6;
    }
}
