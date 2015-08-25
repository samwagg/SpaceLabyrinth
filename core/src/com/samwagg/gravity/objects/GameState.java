package com.samwagg.gravity.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameState implements Serializable {
	
	public HighScores hs;
	
	public int maxLevelReached;
	public int currentLevel;
	public List<Integer> galaxiesUnlocked;
	
	public GameState() {
		hs = new HighScores();
		maxLevelReached = 1;
		currentLevel = 1;
		galaxiesUnlocked = new ArrayList<Integer>();
	}
}
