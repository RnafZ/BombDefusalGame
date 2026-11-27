package com.game.core;

import com.game.modules.GameModule;

import java.util.List;
import java.util.function.Consumer;

public class GameController {
    private Bomb bomb;
    private GameSession currentSession;
    private TimeController timeController;

    private Consumer<GameSession> onGameEndedCallback;

    public void startGame(List<GameModule> modules) {
        stopCurrentGame();

        this.bomb = new Bomb();
        this.currentSession = new GameSession();
        this.timeController = new TimeController(GameSession.TIMER_SECOND);

        for (GameModule module : modules) {
            bomb.addModule(module);

            module.setOnSolvedCallback(() -> {
                checkWinCondition();
            });

            module.setOnStrikeCallback(() -> {
                timeController.setStrikeCount(bomb.getStrikes());
            });
        }

        bomb.setOnBombExplodedCallback(() -> {
            endGameLose("Mencapai 3 strike!");
        });

        timeController.setOnTimeUpCallback(() -> {
            endGameLose("Waktu habis!");
        });

        timeController.start();
    }

    public void checkWinCondition() {
        if (currentSession != null && !currentSession.isGameOver() && bomb.areAllModulesSolved()) {
            timeController.stop();
            currentSession.finishWin(timeController.getRemainingSeconds());
            notifyGameEnded();
        }
    }

    private void endGameLose(String reason) {
        if (currentSession != null && !currentSession.isGameOver()) {
            timeController.stop();
            currentSession.finishLose(timeController.getRemainingSeconds(), reason);
            notifyGameEnded();
        }
    }

    private void notifyGameEnded() {
        if (onGameEndedCallback != null) {
            onGameEndedCallback.accept(currentSession);
        }
    }

    public void stopCurrentGame() {
        if (timeController != null) {
            timeController.stop();
        }
    }

    public Bomb getCurrentBomb() { return bomb; }
    public GameSession getCurrentSession() { return currentSession; }
    public TimeController getTimerController() { return timeController; }

    public void setOnGameEndedCallback(Consumer<GameSession> callback) {
        this.onGameEndedCallback = callback;
    }
}
