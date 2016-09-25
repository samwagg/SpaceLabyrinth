package com.samwagg.gravity.main_game_module.widgets;

/**
 * Must implement interface to register with PauseMenu and receive callbacks
 */
public interface PauseMenuListener {

    void onResumeClicked();
    void onRestartClicked();
    void onMainMenuClicked();
}
