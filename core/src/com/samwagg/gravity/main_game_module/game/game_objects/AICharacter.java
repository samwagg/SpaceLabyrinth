package com.samwagg.gravity.main_game_module.game.game_objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;
import com.samwagg.gravity.Constants;
import com.samwagg.gravity.ai.EnemySteeringAgent;

/**
 * Model representation of an ai entity. Composed of an EnemeySteeringAgent, this class interprets game state to instruct
 * the EnemySteeringAgent instance how to behave. AICharacter is circular
 */
public class AICharacter extends GameCharacter {

    private EnemySteeringAgent driver;

    /**
     * Model coordinates correspond to Box2D coordinate system (units in meters)
     * @param initX x coordinate for center of object
     * @param initY y coordinate for center of object
     * @param radius
     * @param world
     */
    public AICharacter(float initX, float initY, float radius, World world) {
        super(initX, initY, radius, world);
        driver = new EnemySteeringAgent(this);
        body.getFixtureList().get(0).setUserData(this);
    }

    /**
     * @param charToPursue
     */
    public void pursue(GameCharacter charToPursue) {
        driver.pursue(charToPursue);
    }

    /**
     * @return whether this instance is currently pursuing something
     */
    public boolean isPursuing() {
        return driver.getSteeringBehavior() != null;
    }

    @Override
    public void step(float delta) {
        driver.update(delta);
    }
}
