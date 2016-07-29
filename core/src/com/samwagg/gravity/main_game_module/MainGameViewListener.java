package com.samwagg.gravity.main_game_module;

/**
 * Created by sam on 7/28/16.
 */
public interface MainGameViewListener {

    void optionsClicked();
    void resumeClicked();
    void restartClicked();
    void mainMenuClicked();
    void vSetterState(float magnitude, float xComponenet, float yComponent);
}
