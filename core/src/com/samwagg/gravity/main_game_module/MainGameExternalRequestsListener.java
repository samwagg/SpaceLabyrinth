package com.samwagg.gravity.main_game_module;

import com.samwagg.gravity.Galaxy;

/**
 * Created by sam on 7/28/16.
 */
public interface MainGameExternalRequestsListener {

    void onMainMenuRequested();
    void onGalaxyCompleted(Galaxy galaxy);
}
