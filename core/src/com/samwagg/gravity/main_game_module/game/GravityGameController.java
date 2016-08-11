package com.samwagg.gravity.main_game_module.game;

import com.samwagg.gravity.main_game_module.MainGameExternalRequestsListener;

/**
 * Created by sam on 7/26/16.
 * This class is a work in progress as I work on separating game logic from graphic implementation
 */
public class GravityGameController implements MainGameViewListener, MainGameController {

    private GravityGameModel model;
    private GravityGameScreen screen;
    private MainGameControllerListener listener;

    public GravityGameController(GravityGameModel model, GravityGameScreen screen) {
        this.model = model;
        this.screen = screen;
    }

    public void optionsClicked() {
        if (!screen.isOptionsMenuDisplayed()) {
            model.pause(true);
            screen.displayOptionsMenu(true);
        }
        else {
            model.pause(false);
            screen.displayOptionsMenu(false);
        }
    }

    public void resumeClicked() {
        screen.displayOptionsMenu(false);
        model.pause(false);
    }

    public void restartClicked() {
        screen.displayOptionsMenu(false);
        model.restart();
    }

    public void mainMenuClicked() {
        if (listener != null) listener.mainMenuClicked();
    }

    public void vSetterState(float magnitude, float xComponenet, float yComponent) {
        model.setGravity(xComponenet*5, yComponent*5);
    }

    @Override
    public void registerMainGameControllerListener(MainGameControllerListener listener) {
        this.listener = listener;
    }


}
