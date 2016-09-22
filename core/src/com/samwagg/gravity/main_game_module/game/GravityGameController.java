package com.samwagg.gravity.main_game_module.game;

/**
 * Controller as part of a model-view-controller implementation of the main game screen. Accepts input a
 * GravityGameScreen instance and manipulates the screen and GravityGameModel object accordingly.
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
