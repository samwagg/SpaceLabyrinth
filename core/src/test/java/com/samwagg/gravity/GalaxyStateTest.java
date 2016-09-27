package com.samwagg.gravity;

import static org.junit.Assert.*;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import org.junit.*;
import org.mockito.Mock;
import org.mockito.Mockito;

/**
 * Created by sam on 9/25/16.
 */
public class GalaxyStateTest {

    private final static String TEST_PREF_NAME = "test-prefs";


    private  HeadlessApplication app = null;
    private  Game mockGame = Mockito.mock(Game.class);

    @Before
    public void before() {
        app = new HeadlessApplication(mockGame);
    }

    @After
    public void after() {
        GalaxyState state = new GalaxyState(TEST_PREF_NAME);
        state.clearState();
        state.persistChanges();
        app.exit();
        app = null;
    }

    @Test
    public void highScoreChangeShouldPersist() {
        String errorMessage = "Error - High score change did not persist";

        int highScore = 1304;
        int level = 3;

        GalaxyState state = new GalaxyState(TEST_PREF_NAME);
        state.changeHighScore(level, highScore);
        state.persistChanges();
        GalaxyState newState = new GalaxyState(TEST_PREF_NAME);
        assertEquals(errorMessage, state.getHighScore(level), newState.getHighScore(level));
    }

    @Test
    public void currentLevelChangeShouldPersist() {
        String errorMessage = "Error - Current level change did not persist";

        int currLevel = 2;

        GalaxyState state = new GalaxyState(TEST_PREF_NAME);
        state.changeCurrentLevel(currLevel);
        state.persistChanges();
        GalaxyState newState = new GalaxyState(TEST_PREF_NAME);
        assertEquals(errorMessage, state.getCurrentLevel(), newState.getCurrentLevel());
    }

    @Test
    public void highestLevelChangeShouldPersist() {
        String errorMessage = "Error - Highest level change did not persist";

        int highLevel = 6;

        GalaxyState state = new GalaxyState(TEST_PREF_NAME);
        state.changeCurrentLevel(highLevel);
        state.persistChanges();
        GalaxyState newState = new GalaxyState(TEST_PREF_NAME);
        assertEquals(errorMessage, state.getLevelReached(), newState.getLevelReached());
    }
}
