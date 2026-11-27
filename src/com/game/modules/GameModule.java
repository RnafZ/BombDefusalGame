package com.game.modules;

public interface GameModule {

    String getModuleId();
    String getModuleName();
    boolean isSolved();

    void generatePuzzle();
    void resetModule();

    void setOnSolvedCallback(Runnable onSolved);
    void setOnStrikeCallback(Runnable onStrike);
}