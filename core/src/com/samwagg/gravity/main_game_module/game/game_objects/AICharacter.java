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

    private Sprite pursuingSprite;

    public AICharacter(float screenX, float screenY, World world, Constants constants) {
        super(screenX, screenY, world, constants.ATLAS.createSprite("enemy"), constants);
        pursuingSprite = constants.ATLAS.createSprite("enemy_lit");
        driver = new EnemySteeringAgent(physX, physY, this);
        body.getFixtureList().get(0).setUserData(this);
    }

    public void pursue(GameCharacter charToPursue) {
        sprite = pursuingSprite;
        driver.pursue(charToPursue);
    }

    public boolean isPursuing() {
        return driver.getSteeringBehavior() != null;
    }

    public void update(float delta) {
        driver.update(delta);
    }
}
