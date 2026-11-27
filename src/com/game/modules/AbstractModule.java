package com.game.modules;

import com.game.core.Bomb;

public abstract class AbstractModule implements GameModule {
    private final Bomb bomb;
    protected boolean solved;
    private Runnable onSolvedCallback;
    private Runnable onStrikeCallback;

    public AbstractModule(Bomb bomb) {
        this.bomb = bomb;
    }

    @Override 
    public boolean isSolved() {
        return solved;
    }

    @Override 
    public void resetModule() {}

    @Override
    public void setOnSolvedCallback(Runnable onSolved) {
        this.onSolvedCallback = onSolved;
    }

    @Override
    public void setOnStrikeCallback(Runnable onStrike) {
        this.onStrikeCallback = onStrike;
    }

    public void markSolved() {
        if (!this.solved) {
            this.solved = true;
            if (onSolvedCallback != null) {
                onSolvedCallback.run();
            }
        }
    }

    public void triggerStrike() {
        bomb.addStrike();
        if (onStrikeCallback != null) {
            onStrikeCallback.run();
        }
    }
}
