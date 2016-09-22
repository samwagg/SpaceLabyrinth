package com.samwagg.gravity.main_game_module.game;

/**
 * Implement to listen for MainGameView callbacks
 */
public interface MainGameViewListener {

    void optionsClicked();
    void resumeClicked();
    void restartClicked();
    void mainMenuClicked();
    void vSetterState(float magnitude, float xComponenet, float yComponent);
}
