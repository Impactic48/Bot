package blade.impl.action.pvp.sword;

import blade.impl.ConfigKeys;
import blade.impl.StateKeys;
import blade.inventory.Slot;
import blade.inventory.SlotFlag;
import blade.planner.score.ScoreAction;
import blade.state.BladeState;
import blade.util.BotMath;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.phys.Vec3;

public class UseHealing extends ScoreAction implements Sword {
    public Slot getSwordSlot() {
        return bot.getInventory().findFirst(stack -> stack.is(ItemTags.SWORDS), SlotFlag.HOT_BAR);
    }

    public Slot getHealingSlot() {
        return bot.getInventory().getBestFood(FoodProperties::canAlwaysEat, SlotFlag.OFF_HAND, SlotFlag.HOT_BAR);
    }

    @Override
    public void onTick() {
        Slot healingSlot = getHealingSlot();
        if (healingSlot == null) return;
        Slot swordSlot = getSwordSlot();
        if (healingSlot.isOffHand() && swordSlot != null) {
            bot.getInventory().setSelectedSlot(swordSlot.getHotBarIndex());
        } else if (healingSlot.isHotBar()) {
            bot.getInventory().setSelectedSlot(healingSlot.getHotBarIndex());
        }
        lookAtEnemy(bot, tick);
        bot.setMoveForward(false);
        bot.setMoveBackward(false);
        bot.setSprint(false);
        bot.interact(true);
    }

    @Override
    public boolean isSatisfied() {
        return isPvPSatisfied(bot);
    }

    @Override
    public void getResult(BladeState result) {
        result.setValue(StateKeys.IS_HEALING, 1);
        result.setValue(StateKeys.DOING_PVP, 1.0);
    }

    @Override
    public double getScore() {
        LivingEntity target = bot.getBlade().get(ConfigKeys.TARGET);
        Vec3 eyePos = bot.getVanillaPlayer().getEyePosition();
        Vec3 closestPoint = BotMath.getClosestPoint(eyePos, target.getBoundingBox());
        double distSq = closestPoint.distanceToSqr(eyePos);
        float ourHealthRatio = bot.getVanillaPlayer().getHealth() / bot.getVanillaPlayer().getMaxHealth();
        float targetHealthRatio = target.getHealth() / target.getMaxHealth();
        return (getSwordScore(bot) / 2.0) +
                Math.min(distSq / 24, 3) +
                (bot.getVanillaPlayer().isUsingItem() ? (-bot.getVanillaPlayer().getUseItemRemainingTicks() + 16) / 2.0 : 0) +
                ((targetHealthRatio - ourHealthRatio) * 3 - bot.getBlade().get(ConfigKeys.DIFFICULTY)) +
                (getHealingSlot() == null ? -12 : 0);
    }

    @Override
    public void onRelease(ScoreAction next) {
        super.onRelease(next);
        bot.interact(false);
    }
}
