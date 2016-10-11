package com.samwagg.gravity.main_game_module.game.game_objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * GravField is mostly a collection of getters and setters controlling rotation and lighting logic. As of writing this,
 * only inherited behavior needs to be tested
 */
public class GravFieldTest extends GameObjectTest {

    @Override
    protected GameObject makeGameObject(float initX, float initY, float width, float height, World world) {
        return new GravField(initX, initY, width, height, 0, world);
    }
}
