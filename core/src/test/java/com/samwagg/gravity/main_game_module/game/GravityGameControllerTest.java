package com.samwagg.gravity.main_game_module.game;

import static org.junit.Assert.*;

import com.samwagg.gravity.main_game_module.game.GravityGameController;
import com.samwagg.gravity.main_game_module.game.GravityGameModel;
import com.samwagg.gravity.main_game_module.game.GravityGameScreen;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class GravityGameControllerTest {

    private GravityGameModel model;
    private GravityGameScreen screen;
    private GravityGameController controller;

    @Before
    public void intitializeController() {
        model = Mockito.mock(GravityGameModel.class);
        screen = Mockito.mock(GravityGameScreen.class);
        controller = new GravityGameController(model, screen);
    }

    @Test
    public void optionsClickedShouldPauseModelIfModelIsNotAlreadyPaused() {
        Mockito.when(model.isPaused()).thenReturn(false);
        controller.optionsClicked();
        Mockito.verify(model).pause(true);
    }

    @Test
    public void optionsClickedShouldUnpauseModelIfModelIsAlreadyPaused() {
        Mockito.when(model.isPaused()).thenReturn(true);
        controller.optionsClicked();
        Mockito.verify(model).pause(false);
    }

    @Test
    public void optionsClickedShouldDisplayOptionsIfOptionsArentAlreadyDisplayed() {
        Mockito.when(screen.isOptionsMenuDisplayed()).thenReturn(false);
        controller.optionsClicked();
        Mockito.verify(screen).displayOptionsMenu(true);
    }

    @Test
    public void optionsClickedShouldRemoveOptionsIfOptionsAreAlreadyDisplayed() {
        Mockito.when(screen.isOptionsMenuDisplayed()).thenReturn(true);
        controller.optionsClicked();
        Mockito.verify(screen).displayOptionsMenu(false);
    }
}
