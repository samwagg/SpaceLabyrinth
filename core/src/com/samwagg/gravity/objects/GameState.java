package com.samwagg.gravity.objects;

import com.samwagg.gravity.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameState implements Serializable {
	
	public HighScores hs;
	
	public List<Integer> maxLevelReachedByGalaxy;
	public List<Integer> currentLevelByGalaxy;

	public int totalGalaxies = 2;
	public List<Integer> galaxiesUnlocked;
	
	public GameState(Constants constants) {
		hs = new HighScores(this, constants);
		
		maxLevelReachedByGalaxy = new ArrayList<Integer>();
		currentLevelByGalaxy = new ArrayList<Integer>();
		for (int i = 0; i < totalGalaxies; i++) {
			currentLevelByGalaxy.add(i, 1);
			maxLevelReachedByGalaxy.add(i, 1);
		}
		
		galaxiesUnlocked = new ArrayList<Integer>();
		galaxiesUnlocked.add(1);
	}
}
