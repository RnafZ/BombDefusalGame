package com.game.modules;

public interface GameModule {

    String getModuleId();
    String getModuleName();
    boolean isSolved();

    void generatePuzzle();
    void resetModule();

    void setOnSolvedCallBack(Runnable onSolved);
    void setOnStrikeCallBack(Runnable onStrike);
}