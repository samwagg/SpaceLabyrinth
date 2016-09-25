package com.samwagg.gravity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.sun.glass.ui.Window;

/**
 * Represents the current state of game progress for a galaxy. To persist any changes to storage, must call persistChanges()
 */
public class GalaxyState {

    private static final String HI_SCORE_KEY_PREFIX = "hi-score-level-";
    private static final String LEVEL_REACHED_KEY = "level-reached";
    private static final String CURRENT_LEVEL_KEY = "current-level";
    private static final String IS_UNLOCKED_KEY = "is-unlocked";
    private static final String IS_FINISHED_KEY = "is-finished";

    private final Preferences galaxyProgress;

    /**
     * Initialize GalaxyState object. If the galaxy has been used and persisted previously, object will load state from
     * storage; otherwise, will create a new file for the galaxy
     * @param galaxyId unique identifier for galaxy
     */
    public GalaxyState(String galaxyId) {
        galaxyProgress = Gdx.app.getPreferences(galaxyId);
    }

    /**
     * Update high score for a particular level
     * @param level
     * @param score
     */
    public void changeHighScore(int level, int score) {
        galaxyProgress.putInteger(HI_SCORE_KEY_PREFIX + level, score);
    }

    /**
     * Update the highest level reached so far
     * @param level
     */
    public void changeLevelReached(int level) {
        galaxyProgress.putInteger(LEVEL_REACHED_KEY, level);
    }

    /**
     * Update the level the user is currently playing or should play next (if a level was just completed)
     * @param level
     */
    public void changeCurrentLevel(int level) {
        galaxyProgress.putInteger(CURRENT_LEVEL_KEY, level);
    }

    /**
     * Update whether all levels have been completed
     * @param finished
     */
    public void changeIsFinished(boolean finished) {
        galaxyProgress.putBoolean(IS_FINISHED_KEY, finished);
    }

    /**
     * Update whether galaxy is unlocked and is playable by the user
     * @param unlocked
     */
    public void changeIsUnlocked(boolean unlocked) {
        galaxyProgress.putBoolean(IS_UNLOCKED_KEY, unlocked);
    }

    /**
     * @return the highest level reached so far. Default is 0
     */
    public int getLevelReached() {
        return galaxyProgress.getInteger(LEVEL_REACHED_KEY, 0);
    }

    /**
     * @return level the user is currently playing or should play next (if a level was just completed). Default is 0
     */
    public int getCurrentLevel() {
        return galaxyProgress.getInteger(CURRENT_LEVEL_KEY, 0);
    }

    /**
     * @param level
     * @return high score. Default is 0
     */
    public int getHighScore(int level) {
        return galaxyProgress.getInteger(HI_SCORE_KEY_PREFIX + level, 0);
    }

    /**
     * @return whether all levels have been completed. Default is false
     */
    public boolean isFinished() {
        return galaxyProgress.getBoolean(IS_FINISHED_KEY, false);
    }

    /**
     * @return whether galaxy is unlocked and is playable by the user. Default is false
     */
    public boolean isUnlocked() {
        return galaxyProgress.getBoolean(IS_UNLOCKED_KEY, false);
    }

    /**
     * Remove all values and restore defaults (like all other write methods, must call persistChanges() to save)
     */
    public void clearState() {
        galaxyProgress.clear();
    }

    /**
     * Persist all new updates to this instance
     */
    public void persistChanges() {
        galaxyProgress.flush();
    }
}
