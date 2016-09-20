package com.samwagg.gravity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.sun.glass.ui.Window;

/**
 * Created by sam on 9/7/16.
 */
public class GalaxyState {

    private final Preferences galaxyProgress;
    private static final String HI_SCORE_KEY_PREFIX = "hi-score-level-";
    private static final String LEVEL_REACHED_KEY = "level-reached";
    private static final String CURRENT_LEVEL_KEY = "current-level";
    private static final String IS_UNLOCKED_KEY = "is-unlocked";
    private static final String IS_FINISHED_KEY = "is-finished";

    public GalaxyState(String galaxyId) {
        galaxyProgress = Gdx.app.getPreferences(galaxyId);
    }

    public void changeHighScore(int level, int score) {
        galaxyProgress.putInteger(HI_SCORE_KEY_PREFIX + level, score);
    }

    public void changeLevelReached(int level) {
        galaxyProgress.putInteger(LEVEL_REACHED_KEY, level);
    }

    public void changeCurrentLevel(int level) {
        galaxyProgress.putInteger(CURRENT_LEVEL_KEY, level);
    }

    public void changeIsFinished(boolean finished) {
        galaxyProgress.putBoolean(IS_FINISHED_KEY, finished);
    }

    public void changeIsUnlocked(boolean unlocked) {
        galaxyProgress.putBoolean(IS_UNLOCKED_KEY, unlocked);
    }

    public int getLevelReached() {
        return galaxyProgress.getInteger(LEVEL_REACHED_KEY, 0);
    }

    public int getCurrentLevel() {
        return galaxyProgress.getInteger(CURRENT_LEVEL_KEY, 0);
    }

    public int getHighScore(int level) {
        return galaxyProgress.getInteger(HI_SCORE_KEY_PREFIX + level, 0);
    }

    public boolean isFinished() {
        return galaxyProgress.getBoolean(IS_FINISHED_KEY, false);
    }

    public boolean isUnlocked() {
        return galaxyProgress.getBoolean(IS_UNLOCKED_KEY, false);
    }

    public void clearState() {
        galaxyProgress.clear();
    }

    public void persistChanges() {
        galaxyProgress.flush();
    }




}
