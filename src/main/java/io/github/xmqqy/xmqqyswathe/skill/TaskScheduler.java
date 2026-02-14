package io.github.xmqqy.xmqqyswathe.skill;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class TaskScheduler {
    private final List<DelayedTask> tasks = new LinkedList<>();

    public void init() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            Iterator<DelayedTask> iterator = tasks.iterator();
            while (iterator.hasNext()) {
                DelayedTask task = iterator.next();
                task.ticksLeft--;
                if (task.ticksLeft <= 0) {
                    task.runnable.run();
                    iterator.remove();
                }
            }
        });
    }

    public void schedule(Runnable runnable, int delayTicks) {
        tasks.add(new DelayedTask(runnable, delayTicks));
    }

    private static class DelayedTask {
        final Runnable runnable;
        int ticksLeft;

        DelayedTask(Runnable runnable, int ticksLeft) {
            this.runnable = runnable;
            this.ticksLeft = ticksLeft;
        }
    }
}