package com.samwagg.gravity.main_game_module.game.game_objects;

import static org.junit.Assert.*;

import com.badlogic.gdx.physics.box2d.World;
import org.junit.Before;
import org.junit.Test;

public class ExplosionTest extends GameObjectTest {

    @Test
    public void explosionShouldLastForDuration() {
        float duration = 10;
        Explosion exp = new Explosion(0, 0, duration);
        exp.step(duration + .001f); // high level of precision not super important
        assertTrue(exp.done());
    }

    @Test
    public void durationRemainingShouldBeInitialMinusCumulativeStepDeltas() {
        float duration = 19;
        float step1Duration = 3.2f;
        float step2Duration = 1.4f;
        float errorTolerance = .001f;
        Explosion exp = new Explosion(0, 0, duration);
        exp.step(step1Duration);
        assertEquals(duration - step1Duration, exp.getDurationRemaining(), errorTolerance);
        exp.step(step2Duration);
        assertEquals(duration - step1Duration - step2Duration, exp.getDurationRemaining(), errorTolerance);
    }


    /*
     * Explosion doesn't have an associated body and so overrides coordinate getter
     * behavior to use init values. Unfortunately, method names are misleading in
     * these overriden forms
     */
    @Override
    public void getXShouldMatchInitializedXDirectlyAfterInstantiation() {
        float errorTolerance = 0f;
        float x = 4.326f;
        Explosion exp = new Explosion(x, -1.1f, 7.5f);
        assertEquals(x, exp.getX(), errorTolerance);
    }

    @Override
    public void getYShouldMatchInitializedYDirectlyAfterInstantiation() {
        float errorTolerance = 0f;
        float y = -2.334f;
        Explosion exp = new Explosion(4.3f, y, 7.5f);
        assertEquals(y, exp.getY(), errorTolerance);
    }

    /*
     * Width and height are defined as 0 for the bodyless Explosion. So again, it's necessary
     * to override the parent behavior, and again, the method names are unfortunately misleading.
     */
    @Override
    public void getWidthShouldMatchInitializedWidthDirectlyAfterInstantiation() {
        Explosion exp = new Explosion(1.3f, 9, 10);
        assertEquals(0, exp.getWidth(), 0);
    }

    @Override
    public void getHeightShouldMatchInitializedHeightDirectlyAfterInstantiation() {
        Explosion exp = new Explosion(1.3f, 9, 10);
        assertEquals(0, exp.getHeight(), 0);
    }

    @Override
    protected GameObject makeGameObject(float initX, float initY, float width, float height, World world) {
        return null;
    }
}

