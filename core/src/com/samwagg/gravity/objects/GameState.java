package com.samwagg.gravity.objects;

import java.io.Serializable;

public class GameState implements Serializable {
	
	public HighScores hs;
	
	public int maxLevelReached;
	public int currentLevel;
	
	public GameState() {
		hs = new HighScores();
		maxLevelReached = 1;
		currentLevel = 1;
	}
}
