package com.samwagg.gravity.controller;

public interface GravityGameController {

	void mainMenuStartClicked();	
	void galaxySelected(int galaxy);
	void levelSelected(int galaxy, int level);
	void levelSelectBackSelected();
	
	void creditsScreenTouched();
	
	void levComplete(int score);
	void levCompleteNextLevel();
	void levCompleteRetryLevel();
	void levCompleteMenu();
	void levCompleteFinishGalaxy();
	
	void pauseMenuLevelRestart();
	void pauseMenuLevelResume();
	void pauseMenuMenu();
	
	void healthDepleted();
}
