package com.samwagg.gravity;

import com.samwagg.gravity.main_game_module.MainGameExternalRequestsListener;
import com.samwagg.gravity.main_game_module.MainGameFacade;
import com.samwagg.gravity.menus.credits.CreditsMenuListener;
import com.samwagg.gravity.menus.credits.CreditsScreen;
import com.samwagg.gravity.menus.galaxy_menu.GalaxySelectCallback;
import com.samwagg.gravity.menus.galaxy_menu.GalaxySelectScreen;
import com.samwagg.gravity.menus.level_complete_menu.LevelCompleteMenuListener;
import com.samwagg.gravity.menus.level_complete_menu.LevelCompleteScreen;
import com.samwagg.gravity.menus.level_select_menu.LevelSelectMenuListener;
import com.samwagg.gravity.menus.level_select_menu.LevelSelectScreen;
import com.samwagg.gravity.menus.main_menu.MainMenuListener;
import com.samwagg.gravity.menus.main_menu.MainScreen;

public class Navigator {

	private GravityGame game;
	private MainGameFacade facade;

	private CreditsMenuNavigator creditsMenuNavigator;
	private GalaxySelectNavigator galaxySelectNavigator;
	private LevelSelectNavigator levelSelectNavigator;
	private MainGameNavigator mainGameNavigator;
	private MainMenuNavigator mainMenuNavigator;


	public Navigator(GravityGame game) {
		this.game = game;
		creditsMenuNavigator = new CreditsMenuNavigator();
		galaxySelectNavigator = new GalaxySelectNavigator();
		levelSelectNavigator = new LevelSelectNavigator();
		mainGameNavigator = new MainGameNavigator();
		mainMenuNavigator = new MainMenuNavigator();
	}

	public void startGame() {
		openMainScreen();
	}

	private class CreditsMenuNavigator implements CreditsMenuListener {
		@Override
		public void screenTouched() {
			game.getScreen().dispose();
			openMainScreen();
		}
	}

	private class GalaxySelectNavigator implements GalaxySelectCallback {
		@Override
		public void galaxySelected(int galaxy) {
			game.getScreen().dispose();
			LevelSelectScreen screen = new LevelSelectScreen(game, galaxy,
					game.getGameState().currentLevelByGalaxy.get(galaxy-1),
					game.getGameState().maxLevelReachedByGalaxy.get(galaxy-1), game.constants);
			screen.registerLevelSelectMenuListener(levelSelectNavigator);
			game.setScreen(screen);

		}
	}

	private class LevelSelectNavigator implements LevelSelectMenuListener {
		@Override
		public void levelSelected(int galaxy, int level) {
			game.stopMenuMusic();
			facade = new MainGameFacade(game, galaxy, level);
			facade.registerMainGameExternalRequestsListener(mainGameNavigator);
			System.out.println("level selected");

		}

		@Override
		public void levelSelectBackSelected() {
			openGalaxySelect();
		}
	}

	private class MainGameNavigator implements MainGameExternalRequestsListener {
		@Override
		public void onMainMenuRequested() {
			facade.dispose();
			openGalaxySelect();
			game.playMenuMusic();
		}

		@Override
		public void onGalaxyCompleted(int galaxy) {
			System.out.println("levelCompleted");
			CreditsScreen screen = new CreditsScreen(game);
			screen.registerCreditsMenuListener(creditsMenuNavigator);
			game.setScreen(screen);
			game.playMenuMusic();
			facade.dispose();
		}
	}

	private class MainMenuNavigator implements MainMenuListener {
		@Override
		public void mainMenuStartClicked() {
			System.out.println("Start button clicked");
			openGalaxySelect();
		}
	}

	private void openGalaxySelect() {
		game.getScreen().dispose();
		GalaxySelectScreen screen = new GalaxySelectScreen(game.getGameState().galaxiesUnlocked);
		game.setScreen(screen);
		screen.registerGalaxySelectCallback(galaxySelectNavigator);
	}

	private void openMainScreen() {
		MainScreen screen = new MainScreen(game);
		screen.registerMainMenuListener(mainMenuNavigator);
		game.setScreen(screen);
	}
}
