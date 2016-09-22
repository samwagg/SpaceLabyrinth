package com.samwagg.gravity.main_game_module.game.game_objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.samwagg.gravity.Constants;

/**
 * Abstract representation of a model game object
 */
public abstract class GameObject {

    protected float initX;
    protected float initY;
    protected float width;
    protected float height;

    protected Body body;

    /**
     * Model coordinates correspond to Box2D coordinate system (units in meters)
     * @param initX x coordinate for center of object
     * @param initY y coordinate for center of object
     * @param width width of object
     * @param height height of object
     * @param world
     */
    public GameObject(float initX, float initY, float width, float height, World world) {
        this.initX = initX;
        this.initY = initY;
        this.width = width;
        this.height = height;

        setupBody(world);
    }

    /**
     * @return x coordinate for center of object
     */
    public float getX() {
        return body.getPosition().x;
    }

    /**
     * @return y coordinate for center of object
     */
    public float getY() {
        return body.getPosition().y;
    }

    /**
     * @return Box2D body associated with this object
     */
    public Body getBody() {
        return body;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    /**
     * Notify object that time is passed in case it needs to perform some action (like move)
     * @param delta
     */
    public abstract void step(float delta);

    /**
     * Initialize box2d body. Body can be blank. Null bodies are allowed
     */
    protected abstract void setupBody(World world);


}
