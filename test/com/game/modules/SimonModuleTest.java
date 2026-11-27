package com.game.modules;

import com.game.core.Bomb;
import com.game.modules.SimonModule.Color;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimonModuleTest {

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

        assertEquals(Color.BLUE, simon.getExpectedColor(Color.RED));
        assertEquals(Color.RED, simon.getExpectedColor(Color.BLUE));
        assertEquals(Color.YELLOW, simon.getExpectedColor(Color.GREEN));
        assertEquals(Color.GREEN, simon.getExpectedColor(Color.YELLOW));

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

        assertEquals(Color.BLUE, simon.getExpectedColor(Color.RED));
        assertEquals(Color.YELLOW, simon.getExpectedColor(Color.BLUE));
        assertEquals(Color.GREEN, simon.getExpectedColor(Color.GREEN));
        assertEquals(Color.RED, simon.getExpectedColor(Color.YELLOW));

        bomb.addStrike();
        assertEquals(Color.RED, simon.getExpectedColor(Color.RED));
        assertEquals(Color.BLUE, simon.getExpectedColor(Color.BLUE));
        assertEquals(Color.YELLOW, simon.getExpectedColor(Color.GREEN));
        assertEquals(Color.GREEN, simon.getExpectedColor(Color.YELLOW));

        bomb.addStrike();
        assertEquals(Color.YELLOW, simon.getExpectedColor(Color.RED));
        assertEquals(Color.GREEN, simon.getExpectedColor(Color.BLUE));
        assertEquals(Color.BLUE, simon.getExpectedColor(Color.GREEN));
        assertEquals(Color.RED, simon.getExpectedColor(Color.YELLOW));
    }

    @Test
    void testSolveFullSequenceCorrectly() {
        Bomb bomb = createBomb(true); 
        SimonModule simon = new SimonModule(bomb, List.of(Color.RED, Color.GREEN));

        assertEquals(1, simon.getCurrentStage());
        assertTrue(simon.pressColor(Color.BLUE));
        assertFalse(simon.isSolved());

        assertEquals(2, simon.getCurrentStage());
        assertTrue(simon.pressColor(Color.BLUE));
        assertTrue(simon.pressColor(Color.YELLOW));

        assertTrue(simon.isSolved());
        assertEquals(0, bomb.getStrikes());
    }

    @Test
    void testWrongPressTriggersStrikeAndUpdatesMapping() {
        Bomb bomb = createBomb(true); 
        SimonModule simon = new SimonModule(bomb, List.of(Color.RED, Color.BLUE));

        assertFalse(simon.pressColor(Color.RED));
        assertEquals(1, bomb.getStrikes());
        assertEquals(1, simon.getCurrentStage());

        assertTrue(simon.pressColor(Color.YELLOW)); 
        assertEquals(2, simon.getCurrentStage());

        assertTrue(simon.pressColor(Color.YELLOW));
        assertTrue(simon.pressColor(Color.GREEN));
        assertTrue(simon.isSolved());
    }
}
