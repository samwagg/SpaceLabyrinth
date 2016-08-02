package com.samwagg.gravity.main_game_module;


import com.samwagg.gravity.GravityGame;
import com.samwagg.gravity.main_game_module.game.*;
import com.samwagg.gravity.main_game_module.game.GravityGameModel;
import com.samwagg.gravity.main_game_module.game.GravityGameScreen;
import com.samwagg.gravity.main_game_module.game.GravityGameController;
import com.samwagg.gravity.menus.level_complete_menu.LevelCompleteMenu;
import com.samwagg.gravity.menus.level_complete_menu.LevelCompleteMenuListener;
import com.samwagg.gravity.menus.level_complete_menu.LevelCompleteScreen;

import java.io.FileNotFoundException;

/**
 * Created by sam on 7/28/16.
 */
public class MainGameFacade implements MainGameControllerListener, MainGameExternalRequestor, LevelCompleteListener,
        LevelCompleteMenuListener {

    private GravityGame game;
    private GravityGameController controller;
    private GravityGameModel model;
    private GravityGameScreen screen;

    private MainGameExternalRequestsListener listener;

    private int currLevel;
    private int currGalaxy;
    private int maxLevel;

    //temporary, until I decide how to make this (and other properties) dynamic
    private static final int NUM_LEVELS = 7;

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
    public void onLevelCompleted(int score) {
        LevelCompleteScreen screen = new LevelCompleteScreen(game, currLevel, score, currLevel == NUM_LEVELS);
        game.setScreen(screen);
        screen.registerLevelCompleteMenuListener(this);
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

    public void changeLevel(int galaxy, int level) {

        if (currLevel + 1 <= NUM_LEVELS) {
            Map map = null;

            try {
                map = new Map(galaxy, level);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (Map.MapFormatException e) {
                e.printStackTrace();
            }

            model.changeLevel(map);
        }
    }


    @Override
    public void nextLevelSelected() {
        System.out.println("next level selected");
        currLevel++;
        changeLevel(currGalaxy, currLevel);
        game.setScreenAndDispose(screen, game.getScreen());
    }

    @Override
    public void galaxyFinished() {
        listener.onLevelCompleted(currGalaxy, currLevel);
    }

    @Override
    public void retryLevelSelected() {
        changeLevel(currGalaxy, currLevel);
        game.setScreenAndDispose(screen, game.getScreen());
    }

    @Override
    public void mainMenuSelected() {
        listener.onMainMenuRequested();
    }
}
