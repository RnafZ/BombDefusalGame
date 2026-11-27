package com.game.modules;

import com.game.core.Bomb;
import com.game.modules.SimonModule.Color;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimonModuleTest {

    // Helper untuk membuat Bomb dengan kondisi vokal yang pasti (tidak acak)
    private Bomb createBomb(boolean hasVowel) {
        return new Bomb() {
            @Override
            public boolean hasVowelInSerial() {
                return hasVowel;
            }
        };
    }

    @Test
    void testMappingWithVowel_AllStrikes() {
        Bomb bomb = createBomb(true);
        SimonModule simon = new SimonModule(bomb, List.of(Color.RED));

        // 0 Strike: R->B, B->R, G->Y, Y->G
        assertEquals(Color.BLUE, simon.getExpectedColor(Color.RED));
        assertEquals(Color.RED, simon.getExpectedColor(Color.BLUE));
        assertEquals(Color.YELLOW, simon.getExpectedColor(Color.GREEN));
        assertEquals(Color.GREEN, simon.getExpectedColor(Color.YELLOW));

        // 1 Strike: R->Y, B->G, G->B, Y->R
        bomb.addStrike();
        assertEquals(Color.YELLOW, simon.getExpectedColor(Color.RED));
        assertEquals(Color.GREEN, simon.getExpectedColor(Color.BLUE));
        assertEquals(Color.BLUE, simon.getExpectedColor(Color.GREEN));
        assertEquals(Color.RED, simon.getExpectedColor(Color.YELLOW));

        // 2 Strike: R->G, B->R, G->Y, Y->B
        bomb.addStrike();
        assertEquals(Color.GREEN, simon.getExpectedColor(Color.RED));
        assertEquals(Color.RED, simon.getExpectedColor(Color.BLUE));
        assertEquals(Color.YELLOW, simon.getExpectedColor(Color.GREEN));
        assertEquals(Color.BLUE, simon.getExpectedColor(Color.YELLOW));
    }

    @Test
    void testMappingWithoutVowel_AllStrikes() {
        Bomb bomb = createBomb(false);
        SimonModule simon = new SimonModule(bomb, List.of(Color.RED));

        // 0 Strike: R->B, B->Y, G->G, Y->R
        assertEquals(Color.BLUE, simon.getExpectedColor(Color.RED));
        assertEquals(Color.YELLOW, simon.getExpectedColor(Color.BLUE));
        assertEquals(Color.GREEN, simon.getExpectedColor(Color.GREEN));
        assertEquals(Color.RED, simon.getExpectedColor(Color.YELLOW));

        // 1 Strike: R->R, B->B, G->Y, Y->G
        bomb.addStrike();
        assertEquals(Color.RED, simon.getExpectedColor(Color.RED));
        assertEquals(Color.BLUE, simon.getExpectedColor(Color.BLUE));
        assertEquals(Color.YELLOW, simon.getExpectedColor(Color.GREEN));
        assertEquals(Color.GREEN, simon.getExpectedColor(Color.YELLOW));

        // 2 Strike: R->Y, B->G, G->B, Y->R
        bomb.addStrike();
        assertEquals(Color.YELLOW, simon.getExpectedColor(Color.RED));
        assertEquals(Color.GREEN, simon.getExpectedColor(Color.BLUE));
        assertEquals(Color.BLUE, simon.getExpectedColor(Color.GREEN));
        assertEquals(Color.RED, simon.getExpectedColor(Color.YELLOW));
    }

    @Test
    void testSolveFullSequenceCorrectly() {
        Bomb bomb = createBomb(true); // 0 Strike + Vokal: RED->BLUE, GREEN->YELLOW
        SimonModule simon = new SimonModule(bomb, List.of(Color.RED, Color.GREEN));

        // Stage 1: berkedip [RED] -> harus tekan BLUE
        assertEquals(1, simon.getCurrentStage());
        assertTrue(simon.pressColor(Color.BLUE));
        assertFalse(simon.isSolved());

        // Stage 2: berkedip [RED, GREEN] -> harus tekan BLUE, lalu YELLOW
        assertEquals(2, simon.getCurrentStage());
        assertTrue(simon.pressColor(Color.BLUE));
        assertTrue(simon.pressColor(Color.YELLOW));

        assertTrue(simon.isSolved());
        assertEquals(0, bomb.getStrikes());
    }

    @Test
    void testWrongPressTriggersStrikeAndUpdatesMapping() {
        Bomb bomb = createBomb(true); // Vokal
        SimonModule simon = new SimonModule(bomb, List.of(Color.RED, Color.BLUE));

        // Stage 1 (0 Strike): berkedip RED -> benar BLUE. Pemain salah tekan RED.
        assertFalse(simon.pressColor(Color.RED));
        assertEquals(1, bomb.getStrikes());
        assertEquals(1, simon.getCurrentStage());

        // Karena sekarang 1 Strike (Vokal): RED -> YELLOW, BLUE -> GREEN
        assertTrue(simon.pressColor(Color.YELLOW)); // Stage 1 selesai -> naik Stage 2
        assertEquals(2, simon.getCurrentStage());

        assertTrue(simon.pressColor(Color.YELLOW));
        assertTrue(simon.pressColor(Color.GREEN));
        assertTrue(simon.isSolved());
    }
}