package com.samwagg.gravity.controller;

public interface GravityGameController {

	void mainMenuStartClicked();	
	void galaxySelected(int galaxy);
	void levelSelected(int level);
	void levelSelectBackSelected();
	
	void creditsScreenTouched();
	
	void levCompleteNextLevel();
	void levCompleteRetryLevel();
	void levCompleteMenu();
	
	void pauseMenuLevelRestart();
	void pauseMenuLevelResume();
	void pauseMenuMenu();
	
	void healthDepleted();
}
