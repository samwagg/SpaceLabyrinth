package com.samwagg.gravity.main_game_module.game.game_objects;

import com.badlogic.gdx.physics.box2d.World;

/**
 * Wall just represents the size and location of a wall, so only inherited behavior needs to be tested
 */
public class WallTest extends GameObjectTest {

    @Override
    protected GameObject makeGameObject(float initX, float initY, float width, float height, World world) {
        return null;
    }
}
