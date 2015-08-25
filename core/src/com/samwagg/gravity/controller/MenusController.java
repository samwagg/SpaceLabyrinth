package com.samwagg.gravity.controller;

import com.samwagg.gravity.GalaxySelectScreen;
import com.samwagg.gravity.GravityGame;
import com.samwagg.gravity.MainMenu;

public class MenusController implements GravityGameController {

	GravityGame game;
	
	public MenusController(GravityGame game) {
		this.game = game;
	}
	
	public void mainMenuStartClicked() {
		System.out.println("Start button clicked");
		game.setScreen(new GalaxySelectScreen(this, game.getGameState().galaxiesUnlocked));
	}

	@Override
	public void galaxySelected(int galaxy) {
		System.out.println("Galaxy Selected");
	}

	@Override
	public void levelSelected(int level) {
		
	}

	@Override
	public void levelSelectBackSelected() {
		
	}

	@Override
	public void levCompleteNextLevel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void levCompleteRetryLevel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void levCompleteMenu() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pauseMenuLevelRestart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pauseMenuLevelResume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pauseMenuMenu() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void healthDepleted() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void creditsScreenTouched() {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
