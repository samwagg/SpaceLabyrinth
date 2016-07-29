package com.samwagg.gravity.main_game_module;


import com.samwagg.gravity.GravityGame;

import java.io.FileNotFoundException;

/**
 * Created by sam on 7/28/16.
 */
public class MainGameFacade implements MainGameExternalRequestsListener, LevelCompleteCallback {

    private GravityGame game;
    private GravityGameController controller;
    private GravityGameModel model;
    private GravityGameScreen screen;

    private int currGalaxy;
    private int currLevel;

    public MainGameFacade(GravityGame game, int galaxy, int level)  {
        this.game = game;
        Map map = null;

        try {
            map = new Map(galaxy, level);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Map.MapFormatException e) {
            e.printStackTrace();
        }

        model = new GravityGameModel(game, map);
        screen = new GravityGameScreen(model, game);
        controller = new GravityGameController(model, screen, this);
        screen.registerUserInputListener(controller);

        game.setScreenAndDispose(screen, game.getScreen());

    }

    @Override
    public void onMainMenuRequested() {

    }

    @Override
    public void onLevelCompleted() {

    }
}
