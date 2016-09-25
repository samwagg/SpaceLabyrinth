package com.samwagg.gravity.main_game_module;


import com.samwagg.gravity.Galaxy;
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
 * Initializes, manages, and provides a simplified interface to the objects that make up the main game
 * (which are organized according to model-view-controller)
 */
public class MainGameFacade implements MainGameControllerListener, MainGameExternalRequestor, LevelCompleteListener,
        LevelCompleteMenuListener {

    private GravityGame game;
    private GravityGameController controller;
    private GravityGameModel model;
    private GravityGameScreen screen;

    private MainGameExternalRequestsListener listener;

    private int currLevel;
    private Galaxy currGalaxy;

    //temporary, until I decide how to make this (and other properties) dynamic
    private static final int NUM_LEVELS = 7;

    /**
     * @param game
     * @param resources
     * @param galaxy galaxy to play
     * @param level level to play
     */
    public MainGameFacade(GravityGame game, ViewResources resources, Galaxy galaxy, int level)  {
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
        screen = new GravityGameScreen(resources, model, game);
        controller = new GravityGameController(model, screen);
        screen.registerUserInputListener(controller);
        controller.registerMainGameControllerListener(this);
        model.registerLevelCompleteCallback(this);
        game.setScreenAndDispose(screen, game.getScreen());
    }

    /**
     * @param galaxy
     * @param level
     */
    public void changeLevel(Galaxy galaxy, int level) {

        currLevel = level;
        currGalaxy = galaxy;

        if (level <= NUM_LEVELS) {
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

    public void dispose() {
        screen.dispose();
    }

    @Override
    public void onLevelCompleted(int score) {
        game.updateGameState(currGalaxy, currLevel, score);
        LevelCompleteScreen screen = new LevelCompleteScreen(game, score, game.getHighScore(currGalaxy, currLevel), currLevel == NUM_LEVELS);
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

    @Override
    public void nextLevelSelected() {
        System.out.println("next level selected");
        currLevel++;
        changeLevel(currGalaxy, currLevel);
        game.setScreenAndDispose(screen, game.getScreen());
    }

    @Override
    public void galaxyFinished() {
        listener.onGalaxyCompleted(currGalaxy);
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
