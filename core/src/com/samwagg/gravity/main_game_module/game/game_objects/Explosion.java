package com.samwagg.gravity.main_game_module.game.game_objects;

import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by sam on 9/16/16.
 */
public class Explosion extends GameObject {

    private float initialDuration;
    private float durationLeft;

    public Explosion(float x, float y, float duration) {
        // only center position is important for explosions
        super(x, y, 0, 0, null);
        durationLeft = duration;
        initialDuration = duration;
    }

    public boolean done() {
        return durationLeft <= 0;
    }

    public float getInitialDuration() {
        return initialDuration;
    }

    public float getDurationRemaining() {
        return durationLeft;
    }

    @Override
    public void step(float delta) {
        if (durationLeft > 0) durationLeft -= delta;
    }

    @Override
    public float getX() {
        return initX;
    }

    @Override
    public float getY() {
        return initY;
    }

    @Override
    protected void setupBody(World world) {
        // N/A for explosion. body will be null
    }


}
