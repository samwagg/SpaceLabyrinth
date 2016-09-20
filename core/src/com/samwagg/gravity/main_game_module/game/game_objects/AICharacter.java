package com.samwagg.gravity.main_game_module.game.game_objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;
import com.samwagg.gravity.Constants;
import com.samwagg.gravity.ai.EnemySteeringAgent;

/**
 * Created by sam on 8/10/16.
 */
public class AICharacter extends GameCharacter {

    public EnemySteeringAgent driver;

    public AICharacter(float physX, float physY, float radius, World world) {
        super(physX, physY, radius, world);
        driver = new EnemySteeringAgent(physX, physY, this);
        body.getFixtureList().get(0).setUserData(this);
    }

    public void pursue(GameCharacter charToPursue) {
        driver.pursue(charToPursue);
    }

    public boolean isPursuing() {
        return driver.getSteeringBehavior() != null;
    }

    public void update(float delta) {
        driver.update(delta);
    }
}
