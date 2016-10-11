package com.samwagg.gravity.main_game_module.game.game_objects;

import static org.junit.Assert.*;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import org.junit.Before;
import org.junit.Test;


public class GameCharacterTest extends GameObjectTest {

    private World world;
    private GameCharacter character;

    @Before
    public void initializeCharacterAndWorld() {
        world = new World(new Vector2(0,0), false);
        character = new GameCharacter(0, 0, .2f, world);
    }

    @Test
    public void explodeShouldCauseCharacterToBeExploding() {
        character.explode();
        assertTrue(character.isExploding());
    }

    @Test
    public void defaultStateShouldBeNotExploding() {
        assertFalse(character.isExploding());
    }

    /*
     * Magnitude of acceleration is up to Box2D stuff, and not tested here. The point of these tests is to define
     * character's physics as dependent on the Box2D body it wraps.
     */
    @Test
    public void applyForceWithPositiveXComponentShouldAccelerateBody() {
        character.applyForce(1, 1);
        world.step(1, 20, 5);
        assertTrue(character.getBody().getLinearVelocity().x > 0);
    }

    @Test
    public void applyForceWithNegativeXComponentShouldAccelerateBody() {
        World world = new World(new Vector2(22.2f, 5f), false);
        GameCharacter character = new GameCharacter(0, 0, .2f, world);
        character.applyForce(-3, 6);
        world.step(1, 20, 5);
        assertTrue(character.getBody().getLinearVelocity().x < 0);
    }

    @Test
    public void applyForceWithNegativeYComponentShouldAccelerateBody() {
        World world = new World(new Vector2(0,3.3f), false);
        GameCharacter character = new GameCharacter(0, 0, .2f, world);
        character.applyForce(-4, -9);
        world.step(1, 20, 5);
        assertTrue(character.getBody().getLinearVelocity().y < 0);
    }

    @Override
    protected GameObject makeGameObject(float initX, float initY, float width, float height, World world) {
        return new GameCharacter(initX, initY, width/2, world);
    }

}
