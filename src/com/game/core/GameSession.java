package com.game.core;

public class GameSession {
    public enum GameState {
        PLAYING, WIN, LOSE
    }

    public static final int MODULE_COUNT = 4;
    public static final int TIMER_SECOND = 420;

    private GameState state;
    private int finalTimeRemaining;
    private String endReason;

    public GameSession() {
        this.state = GameState.PLAYING;
        this.finalTimeRemaining = TIMER_SECOND;
        this.endReason = "";
    }

    public void finishWin(int timeRemaining) {
        if (this.state == GameState.PLAYING) {
            this.state = GameState.WIN;
            this.finalTimeRemaining = timeRemaining;
            this.endReason = "Bom berhasil dijinakkan!";
        }
    }

    public void finishLose(int timeRemaining, String reason) {
        if (this.state == GameState.PLAYING) {
            this.state = GameState.LOSE;
            this.finalTimeRemaining = timeRemaining;
            this.endReason = reason;
        }
    }

    public int getModuleCount() { return MODULE_COUNT; }
    public int getInitialDurationSeconds() { return TIMER_SECOND; }
    public GameState getState() { return state; }
    public int getFinalTimeRemaining() { return finalTimeRemaining; }
    public String getEndReason() { return endReason; }

    public boolean isGameOver() {
        return state != GameState.PLAYING;
    }
}