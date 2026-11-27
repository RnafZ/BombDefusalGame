package com.game.core;

import java.util.concurrent.*;
import java.util.function.IntConsumer;

public class TimeController {
    private ScheduledExecutorService scheduler;
    private int remainingSeconds;
    private int strikeCount = 0;
    private boolean running = false;

    private IntConsumer onTickCallback;
    private Runnable onTimeUpCallback;

    public TimeController(int initialTime) {
        this.remainingSeconds = initialTime;
    }

    public void start() {
        if (running) return;
        running = true;
        scheduleNextTick();
    }

    private void scheduleNextTick() {
        if (!running) return;
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
        }

        scheduler = Executors.newSingleThreadScheduledExecutor();
        long delayMillis = getTickIntervalMillis();

        scheduler.scheduleAtFixedRate(() -> {
            if (!running) return;

            remainingSeconds--;
            if (onTickCallback != null) {
                onTickCallback.accept(remainingSeconds);
            }

            if (remainingSeconds <= 0) {
                stop();
                if (onTimeUpCallback != null) {
                    onTimeUpCallback.run();
                }
            }
        }, delayMillis, delayMillis, TimeUnit.MILLISECONDS);
    }

    private long getTickIntervalMillis() {
        return switch (strikeCount) {
            case 1 -> 800L;
            case 2 -> 667L;
            default -> 1000L;
        };
    }

    public void setStrikeCount(int strikes) {
        this.strikeCount = strikes;
        if (running && remainingSeconds > 0) {
            scheduleNextTick();
        }
    }

    public void stop() {
        running = false;
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
        }
    }

    public void setOnTickCallback(IntConsumer callback) { this.onTickCallback = callback; }
    public void setOnTimeUpCallback(Runnable callback) { this.onTimeUpCallback = callback; }
    public int getRemainingSeconds() { return remainingSeconds; }
}
