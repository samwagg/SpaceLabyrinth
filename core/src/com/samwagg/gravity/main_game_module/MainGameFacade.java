package com.samwagg.gravity.main_game_module;


import com.samwagg.gravity.GravityGame;
import com.samwagg.gravity.main_game_module.game.*;
import com.samwagg.gravity.main_game_module.game.GravityGameModel;
import com.samwagg.gravity.main_game_module.game.GravityGameScreen;
import com.samwagg.gravity.main_game_module.game.GravityGameController;

import java.io.FileNotFoundException;

/**
 * Created by sam on 7/28/16.
 */
public class MainGameFacade implements MainGameControllerListener, MainGameExternalRequestor, LevelCompleteListener {

    private GravityGame game;
    private GravityGameController controller;
    private GravityGameModel model;
    private GravityGameScreen screen;

    private MainGameExternalRequestsListener listener;

    private int currLevel;
    private int currGalaxy;

    public MainGameFacade(GravityGame game, int galaxy, int level)  {
        this.game = game;
        Map map = null;

        currGalaxy = galaxy;
        currLevel = level;

        try {
            map = new Map(galaxy, level);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Map.MapFormatException e) {
            e.printStackTrace();
        }

        model = new GravityGameModel(game, map);
        screen = new GravityGameScreen(model, game);
        controller = new GravityGameController(model, screen);
        screen.registerUserInputListener(controller);
        controller.registerMainGameControllerListener(this);
        model.registerLevelCompleteCallback(this);
        game.setScreenAndDispose(screen, game.getScreen());
    }

    @Override
    public void onLevelCompleted() {
        if (listener != null) listener.onLevelCompleted(currGalaxy, currLevel);
    }

    @Override
    public void registerMainGameExternalRequestsListener(MainGameExternalRequestsListener listener) {
        if (listener != null) this.listener = listener;
    }

    @Override
    public void mainMenuClicked() {
        System.out.println("main menu requested");
        if (listener != null) listener.onMainMenuRequested();
    }
}
