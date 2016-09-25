package com.samwagg.gravity.menus.level_select_menu;

import com.samwagg.gravity.Galaxy;

/**
 * Created by sam on 7/29/16.
 */
public interface LevelSelectMenuListener {

    void levelSelected(Galaxy galaxy, int level);
    void levelSelectBackSelected();
}
