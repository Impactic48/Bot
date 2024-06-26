package blade.scheduler;

import blade.Bot;
import blade.util.TimeUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

public class BotScheduler {
    private final Map<Long, List<BotTask>> scheduled = new HashMap<>();
    private final Executor executor;
    private long tick = 0L;

    public BotScheduler(Executor executor) {
        this.executor = executor;
    }

    public void schedule(long delay, BotTask task) {
        scheduled.computeIfAbsent(tick + Math.max(1, delay), point -> new ArrayList<>()).add(task);
    }

    public void schedule(Duration delay, BotTask task) {
        scheduled.computeIfAbsent(tick + Math.max(1, TimeUtils.convertDurationToTicks(delay)), point -> new ArrayList<>()).add(task);
    }

    public void runAsync(Runnable runnable) {
        executor.execute(runnable);
    }

    public void tick(Bot bot) {
        tick++;
        List<BotTask> tasks = scheduled.get(tick);
        if (tasks != null) {
            for (BotTask task : tasks) {
                task.run(bot);
            }
        }
    }
}
