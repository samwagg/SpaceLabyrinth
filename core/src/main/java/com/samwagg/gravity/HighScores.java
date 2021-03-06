package com.samwagg.gravity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper around a list of arrays, where each value is a high score
 */
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
