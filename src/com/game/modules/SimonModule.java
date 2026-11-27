package com.game.modules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import com.game.core.Bomb;

public class SimonModule extends AbstractModule {
    private final Bomb bombRef;

    public enum Color {
        RED, BLUE, GREEN, YELLOW
    }

    private List<Color> fullSequence;
    private int totalStages;
    private int currentStage;
    private int currentInputIndex;

    public SimonModule(Bomb bomb, int totalStages) {
        super(bomb);
        this.bombRef = bomb;
        this.totalStages = totalStages;
        generatePuzzle();
    }

    public SimonModule(Bomb bomb, List<Color> fixedSequence) {
        super(bomb);
        this.bombRef = bomb;
        if (fixedSequence == null || fixedSequence.isEmpty()) {
            throw new IllegalArgumentException("Sequence tidak boleh kosong");
        }
        this.totalStages = fixedSequence.size();
        this.fullSequence = new ArrayList<>(fixedSequence);
        reset();
    }

    private List<Color> generateRandomSequence(int length) {
        Random random = new Random();
        Color[] colors = Color.values();
        List<Color> seq = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            seq.add(colors[random.nextInt(colors.length)]);
        }
        return seq;
    }

    @Override
    public String getModuleName() {
        return "Simon Says";
    }

    @Override
    public String getModuleId() {
        return "SIMON";
    }

    @Override
    public void generatePuzzle() {
        int stages = (totalStages > 0) ? totalStages : 4;
        this.fullSequence = generateRandomSequence(stages);
        reset();
    }

    public void reset() {
        this.solved = false;
        this.currentStage = 1;
        this.currentInputIndex = 0;
    }

    public List<Color> getCurrentFlashSequence() {
        return Collections.unmodifiableList(fullSequence.subList(0, currentStage));
    }

    public int getCurrentStage() {
        return currentStage;
    }

    public int getTotalStages() {
        return fullSequence.size();
    }

    public Color getExpectedColor(Color flashedColor) {
        boolean hasVowel = bombRef.hasVowelInSerial();
        int strikes = Math.min(bombRef.getStrikes(), 2);

        if (hasVowel) {
            return switch (strikes) {
                case 0 -> switch (flashedColor) {
                    case RED -> Color.BLUE;
                    case BLUE -> Color.RED;
                    case GREEN -> Color.YELLOW;
                    case YELLOW -> Color.GREEN;
                };
                case 1 -> switch (flashedColor) {
                    case RED -> Color.YELLOW;
                    case BLUE -> Color.GREEN;
                    case GREEN -> Color.BLUE;
                    case YELLOW -> Color.RED;
                };
                default -> switch (flashedColor) { // 2 Strike
                    case RED -> Color.GREEN;
                    case BLUE -> Color.RED;
                    case GREEN -> Color.YELLOW;
                    case YELLOW -> Color.BLUE;
                };
            };
        } else {
            return switch (strikes) {
                case 0 -> switch (flashedColor) {
                    case RED -> Color.BLUE;
                    case BLUE -> Color.YELLOW;
                    case GREEN -> Color.GREEN;
                    case YELLOW -> Color.RED;
                };
                case 1 -> switch (flashedColor) {
                    case RED -> Color.RED;
                    case BLUE -> Color.BLUE;
                    case GREEN -> Color.YELLOW;
                    case YELLOW -> Color.GREEN;
                };
                default -> switch (flashedColor) { // 2 Strike
                    case RED -> Color.YELLOW;
                    case BLUE -> Color.GREEN;
                    case GREEN -> Color.BLUE;
                    case YELLOW -> Color.RED;
                };
            };
        }
    }

    public boolean pressColor(Color pressedColor) {
        if (isSolved()) {
            return true;
        }

        Color flashedColor = fullSequence.get(currentInputIndex);
        Color expectedColor = getExpectedColor(flashedColor);

        if (pressedColor == expectedColor) {
            currentInputIndex++;
            if (currentInputIndex >= currentStage) {
                if (currentStage >= fullSequence.size()) {
                    markSolved();
                } else {
                    currentStage++;
                    currentInputIndex = 0;
                }
            }
            return true;
        } else {
            triggerStrike();
            currentInputIndex = 0;
            return false;
        }
    }
}
