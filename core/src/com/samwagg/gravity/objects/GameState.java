package com.samwagg.gravity.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameState implements Serializable {
	
	public HighScores hs;
	
	public List<Integer> maxLevelReachedByGalaxy;
	public List<Integer> currentLevelByGalaxy;
	
	public int asdf = 3;
	
	public int totalGalaxies = 2;
	public List<Integer> galaxiesUnlocked;
	
	public GameState() {
		hs = new HighScores(this);
		
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
