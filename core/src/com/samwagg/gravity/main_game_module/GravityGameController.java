package com.samwagg.gravity.main_game_module;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.samwagg.gravity.main_game_module.widgets.PauseMenu;

/**
 * Created by sam on 7/26/16.
 * This class is a work in progress as I work on separating game logic from graphic implementation
 */
public class GravityGameController implements MainGameViewListener {

//    private Music music;
//
//
//
//    public GravityGameController() {

//    }

    private GravityGameModel model;
    private GravityGameScreen screen;

    public GravityGameController(GravityGameModel model, GravityGameScreen screen, MainGameExternalRequestsListener listener) {
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

    }

    public void vSetterState(float magnitude, float xComponenet, float yComponent) {
        model.setGravity(xComponenet/10, yComponent/10);
    }





}
