package com.samwagg.gravity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Deprecated: using libgdx preferences via GalaxyState class going forward. This class remains to enable
 * migration to the new persistence implementation
 *
 * Data class holding game state information to be persisted.
 */
@Deprecated
public class GameState implements Serializable {
	
	public HighScores hs;
	
	public List<Integer> maxLevelReachedByGalaxy;
	public List<Integer> currentLevelByGalaxy;

	public int totalGalaxies = 3;
	public List<Integer> galaxiesUnlocked;

	public GameState(Constants constants) {
		hs = new HighScores(this, constants);
		
		maxLevelReachedByGalaxy = new ArrayList<Integer>();
		currentLevelByGalaxy = new ArrayList<Integer>();
		for (int i = 0; i < totalGalaxies; i++) {
			currentLevelByGalaxy.add(i, 1);
			//maxLevelReachedByGalaxy.add(i, 1);
		}
		maxLevelReachedByGalaxy.add(0,1);
		maxLevelReachedByGalaxy.add(1,1);
		maxLevelReachedByGalaxy.add(2,2);
		
		galaxiesUnlocked = new ArrayList<Integer>();
		galaxiesUnlocked.add(1);
//		galaxiesUnlocked.add(2);
//		galaxiesUnlocked.add(3);
	}
}
