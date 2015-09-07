package com.samwagg.gravity.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.samwagg.gravity.Constants;

public class HighScores implements Serializable {

	public List<Integer[]> galaxies = new ArrayList<Integer[]>();
	public HighScores(GameState state, Constants constants) {
		
		for (int i = 0; i < state.totalGalaxies; i++) {
			
			Integer levels[] = new Integer[constants.N_LEVELS];
			for (int j = 0; j < levels.length; j++) {
				levels[j] = 0;
			}
			
			galaxies.add(levels);
			
		}
	}
}
