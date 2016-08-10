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

public class Navigator implements CreditsMenuListener, GalaxySelectCallback,
										LevelSelectMenuListener, MainGameExternalRequestsListener, MainMenuListener {

	GravityGame game;
	MainGameFacade facade;

	private int currentGalaxy = 0;
	private int currentLevel = 0;
	
	public Navigator(GravityGame game) {
		this.game = game;
	}

	@Override
	public void mainMenuStartClicked() {
		System.out.println("Start button clicked");
		openGalaxySelect();
	}

	@Override
	public void galaxySelected(int galaxy) {
		currentGalaxy = galaxy;
		game.getScreen().dispose();
		LevelSelectScreen screen = new LevelSelectScreen(game, galaxy,
				game.getGameState().currentLevelByGalaxy.get(galaxy-1),
				game.getGameState().maxLevelReachedByGalaxy.get(galaxy-1), game.constants);
		screen.registerLevelSelectMenuListener(this);
		game.setScreen(screen);

	}

	@Override
	public void levelSelected(int galaxy, int level) {
		currentLevel = level;
		game.stopMenuMusic();
		facade = new MainGameFacade(game, galaxy, level);
		facade.registerMainGameExternalRequestsListener(this);
		System.out.println("level selected");

	}

	@Override
	public void levelSelectBackSelected() {
		openGalaxySelect();
	}



	/*
	 * Begin Credits screen listener
	 */

	@Override
	public void screenTouched() {
		openMainScreen();
	}

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
		screen.registerCreditsMenuListener(this);
		game.setScreen(screen);
		game.playMenuMusic();
		facade.dispose();
	}

	/*
	 * End Credits screen listener
	 */

//	@Override
//	public void levComplete(int score) {
//
//		game.getScreen().dispose();
//		game.updateGameState(currentGalaxy, currentLevel, score);
//		game.setScreen(new LevelCompleteScreen(game, this, score,
//				game.getGameState().hs.galaxies.get(currentGalaxy-1)[currentLevel-1].intValue(),
//				currentLevel == game.constants.N_LEVELS));
//	}

//	@Override
//	public void levCompleteFinishGalaxy() {
//		game.getScreen().dispose();
//		game.setScreen(new CreditsScreen(this, game));
//	}


	private void openGalaxySelect() {
		game.getScreen().dispose();
		GalaxySelectScreen screen = new GalaxySelectScreen(game.getGameState().galaxiesUnlocked);
		game.setScreen(screen);
		screen.registerGalaxySelectCallback(this);
	}

	private void openMainScreen() {
		game.getScreen().dispose();
		MainScreen screen = new MainScreen(game);
		screen.registerMainMenuListener(this);
		game.setScreen(screen);
	}
}
