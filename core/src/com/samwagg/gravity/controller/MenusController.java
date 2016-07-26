package com.samwagg.gravity.controller;

import com.samwagg.gravity.GalaxyCompleteScreen;
import com.samwagg.gravity.menus.galaxy_menu.GalaxySelectScreen;
import com.samwagg.gravity.GravityGame;
import com.samwagg.gravity.main_game_module.GravityGameScreen;
import com.samwagg.gravity.menus.level_complete_menu.LevelCompleteMenu;
import com.samwagg.gravity.menus.level_select_menu.LevelSelectScreen;
import com.samwagg.gravity.menus.main_menu.MainMenu;

public class MenusController implements GravityGameController {

	GravityGame game;
	
	int currentGalaxy = 0;
	int currentLevel = 0;
	
	public MenusController(GravityGame game) {
		this.game = game;
	}
	
	public void mainMenuStartClicked() {
		System.out.println("Start button clicked");
		game.getScreen().dispose();
		game.setScreen(new GalaxySelectScreen(this, game.getGameState().galaxiesUnlocked));
	}

	@Override
	public void galaxySelected(int galaxy) {
		currentGalaxy = galaxy;
		game.getScreen().dispose();
		game.setScreen(new LevelSelectScreen(this, game, galaxy, 
				game.getGameState().currentLevelByGalaxy.get(galaxy-1), 
				game.getGameState().maxLevelReachedByGalaxy.get(galaxy-1), game.constants));
	}

	@Override
	public void levelSelected(int galaxy, int level) {
		currentLevel = level;
		game.setScreen(new GravityGameScreen(this, game, galaxy, level ));
	}

	@Override
	public void levelSelectBackSelected() {
		game.getScreen().dispose();
		game.setScreen(new GalaxySelectScreen(this, game.getGameState().galaxiesUnlocked));
	}

	@Override
	public void levCompleteNextLevel() {
		currentLevel++;
		game.getScreen().dispose();
		game.setScreen(new GravityGameScreen(this, game, currentGalaxy, currentLevel));	
	}

	@Override
	public void levCompleteRetryLevel() {
		game.getScreen().dispose();
		game.setScreen(new GravityGameScreen(this, game, currentGalaxy, currentLevel));			
	}

	@Override
	public void levCompleteMenu() {
		game.getScreen().dispose();
		game.setScreen(new GalaxySelectScreen(this, game.getGameState().galaxiesUnlocked));		
	}

	@Override
	public void pauseMenuLevelRestart() {
		game.getScreen().dispose();
		game.setScreen(new GravityGameScreen(this, game, currentGalaxy, currentLevel));		
	}

	@Override
	public void pauseMenuLevelResume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pauseMenuMenu() {
		game.getScreen().dispose();
		game.setScreen(new GalaxySelectScreen(this, game.getGameState().galaxiesUnlocked));
		
	}

	@Override
	public void healthDepleted() {
		game.getScreen().dispose();
		game.setScreen(new GravityGameScreen(this, game, currentGalaxy, currentLevel));
		
	}

	@Override
	public void creditsScreenTouched() {
		game.getScreen().dispose();
		game.setScreen(new MainMenu(this, game));	
	}

	@Override
	public void levComplete(int score) {
		
		game.getScreen().dispose();
		game.updateGameState(currentGalaxy, currentLevel, score);
		game.setScreen(new LevelCompleteMenu(game, this, score, 
				game.getGameState().hs.galaxies.get(currentGalaxy-1)[currentLevel-1].intValue(), 
				currentLevel == game.constants.N_LEVELS));
	}

	@Override
	public void levCompleteFinishGalaxy() {
		game.getScreen().dispose();
		game.setScreen(new GalaxyCompleteScreen(this, game));
	}
}
