package com.samwagg.gravity.main_game_module;

import com.samwagg.gravity.Galaxy;

/**
 * Must implement this interface to register with MainGameExternalRequestor and receive callbacks
 */
public interface MainGameExternalRequestsListener {

    void onMainMenuRequested();
    void onGalaxyCompleted(Galaxy galaxy);
}
