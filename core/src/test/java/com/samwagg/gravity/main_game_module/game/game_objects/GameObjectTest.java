package com.samwagg.gravity.main_game_module.game.game_objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Provides tests for inherited behavior. Note that World is not mocked here, as the game object hierarchy is largely
 * a wrapper for
 */
public abstract class GameObjectTest {

    /**
     * Creates a GameObject instance to run inherited unit tests on. Should return an instance of the implementing
     * class.
     * @return GameObject instance to test
     */
    protected abstract GameObject makeGameObject(float initX, float initY, float width, float height, World world);


    /*
     * These getter tests are necessary because getX isn't really a direct getter method. It is pulling the X
     * coordinate of the box2D body and if that body wasn't correctly setup, this test could fail
     */

    @Test
    public void getXShouldMatchInitializedXDirectlyAfterInstantiation() {
        float delta = .001f;
        float x = 5.5f;
        GameObject obj = makeGameObject(x, 999.123f, 44f, 23.3f, new World(new Vector2(0, 0), false));
        assertEquals(x, obj.getX(), delta);
    }

    @Test
    public void getYShouldMatchInitializedYDirectlyAfterInstantiation() {
        float delta = .001f;
        float y = -222.34f;
        GameObject obj = makeGameObject(12f, y, 44f, 23.3f, new World(new Vector2(0, 0), false));
        assertEquals(y, obj.getY(), delta);
    }

    @Test
    public void getWidthShouldMatchInitializedWidthDirectlyAfterInstantiation() {
        float delta = .001f;
        float width = -3.25f;
        GameObject obj = makeGameObject(123, 999.123f, width, 23.3f, new World(new Vector2(0, 0), false));
        assertEquals(width, obj.getWidth(), delta);
    }

    @Test
    public void getHeightShouldMatchInitializedHeightDirectlyAfterInstantiation() {
        float delta = .001f;
        float height = 4222.34f;
        GameObject obj = makeGameObject(12f, 47.2f, 44f, height, new World(new Vector2(0, 0), false));
        assertEquals(height, obj.getHeight(), delta);
    }


}
