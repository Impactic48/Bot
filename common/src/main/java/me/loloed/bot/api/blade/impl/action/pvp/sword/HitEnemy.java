package me.loloed.bot.api.blade.impl.action.pvp.sword;

import me.loloed.bot.api.blade.impl.ConfigKeys;
import me.loloed.bot.api.blade.planner.score.ScoreAction;
import me.loloed.bot.api.blade.state.BladeState;
import me.loloed.bot.api.inventory.Slot;
import me.loloed.bot.api.inventory.SlotFlag;
import me.loloed.bot.api.util.BotMath;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class HitEnemy extends ScoreAction implements Sword {
    public Slot getSwordSlot() {
        return bot.getInventory().findFirst(stack -> stack.is(ItemTags.SWORDS), SlotFlag.HOT_BAR);
    }

    @Override
    public void onTick() {
        lookAtEnemy(bot, tick);
        LivingEntity target = bot.getBlade().get(ConfigKeys.TARGET);
        if (bot.getCrossHairTarget() instanceof EntityHitResult entityHitResult && entityHitResult.getEntity() == target) {
            bot.attack();
        }
    }

    @Override
    public void getResult(BladeState result) {

    }

    @Override
    public double getScore() {
        LivingEntity target = bot.getBlade().get(ConfigKeys.TARGET);
        Vec3 eyePos = bot.getVanillaPlayer().getEyePosition();
        Vec3 closestPoint = BotMath.getClosestPoint(eyePos, target.getBoundingBox());
        double distSq = closestPoint.distanceToSqr(eyePos);
        return getSwordScore(bot) +
                (distSq > 3 * 3 ? -8 : (Math.min(distSq / 2, 4))) +
                (getSwordSlot() == null ? -4 : 0);
    }
}
