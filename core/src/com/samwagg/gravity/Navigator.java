package com.samwagg.gravity;

import com.samwagg.gravity.main_game_module.MainGameExternalRequestsListener;
import com.samwagg.gravity.main_game_module.MainGameFacade;
import com.samwagg.gravity.menus.credits.CreditsMenuListener;
import com.samwagg.gravity.menus.credits.CreditsScreen;
import com.samwagg.gravity.menus.galaxy_menu.GalaxySelectCallback;
import com.samwagg.gravity.menus.galaxy_menu.GalaxySelectScreen;
import com.samwagg.gravity.menus.level_complete_menu.LevelCompleteMenuListener;
import com.samwagg.gravity.menus.level_select_menu.LevelSelectMenuListener;
import com.samwagg.gravity.menus.level_select_menu.LevelSelectScreen;
import com.samwagg.gravity.menus.main_menu.MainMenuListener;
import com.samwagg.gravity.menus.main_menu.MainScreen;

public class Navigator implements CreditsMenuListener, GalaxySelectCallback, LevelCompleteMenuListener,
										LevelSelectMenuListener, MainGameExternalRequestsListener, MainMenuListener {

	GravityGame game;
	
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
		MainGameFacade facade = new MainGameFacade(game, galaxy, level);
		facade.registerMainGameExternalRequestsListener(this);
		System.out.println("level selected");

	}

	@Override
	public void levelSelectBackSelected() {
		game.getScreen().dispose();
		GalaxySelectScreen screen = new GalaxySelectScreen(game.getGameState().galaxiesUnlocked);
		screen.registerGalaxySelectCallback(this);
		game.setScreen(screen);
	}

	@Override
	public void nextLevelSelected() {
		currentLevel++;
		game.getScreen().dispose();
		MainGameFacade facade = new MainGameFacade(game, currentGalaxy, currentLevel);
		facade.registerMainGameExternalRequestsListener(this);
	}

	@Override
	public void galaxyFinished() {
		game.getScreen().dispose();
		game.setScreen(new CreditsScreen(game));
	}

	@Override
	public void retryLevelSelected() {
		game.getScreen().dispose();
		new MainGameFacade(game, currentGalaxy, currentLevel);
	}

	@Override
	public void mainMenuSelected() {
		openGalaxySelect();

	}


	/*
	 * Begin Credits screen listener
	 */

	@Override
	public void screenTouched() {
		game.getScreen().dispose();
		game.setScreen(new MainScreen(game));
	}

	@Override
	public void onMainMenuRequested() {
		openGalaxySelect();
	}

	@Override
	public void onLevelCompleted(int galaxy, int level) {
		System.out.println("levelCompleted");
		nextLevelSelected();
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
}
