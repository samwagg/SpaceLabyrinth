package com.samwagg.gravity.ai;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.samwagg.gravity.main_game_module.game.game_objects.GameCharacter;

// This file currently is a heavily modified version of public libgdx documentation code from here https://github.com/libgdx/gdx-ai/wiki/Steering-Behaviors

/**
 * Encapsulation of AI steering logic for a pursuer
 */
public class EnemySteeringAgent implements Steerable<Vector2> {

    private static final SteeringAcceleration<Vector2> steeringOutput =
            new SteeringAcceleration<Vector2>(new Vector2());

    private SteeringBehavior<Vector2> steeringBehavior;
    private GameCharacter vehicle;

    /**
     * @param vehicle GameCharacter instance to steer
     */
    public EnemySteeringAgent(GameCharacter vehicle) {
        this.vehicle = vehicle;
    }

    /**
     * @param thingToPursue
     */
    public void pursue(Steerable<Vector2> thingToPursue) {
        Seek<Vector2> seek = new Seek<Vector2>(this, thingToPursue);
        steeringBehavior = seek;
    }

    public SteeringBehavior<Vector2> getSteeringBehavior() {
        return steeringBehavior;
    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return (float) Math.atan2(-vector.x, vector.y);
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        outVector.x = -(float) Math.sin(angle);
        outVector.y = (float) Math.cos(angle);
        return outVector;
    }

    public void update(float delta) {
        if (steeringBehavior != null) {
            steeringBehavior.calculateSteering(steeringOutput);
            applySteering(steeringOutput, delta);
        }
    }

    private void applySteering(SteeringAcceleration<Vector2> steering, float time) {
        vehicle.getBody().setLinearVelocity(vehicle.getBody().getLinearVelocity().mulAdd(steering.linear, time).limit(getMaxLinearSpeed()));
    }

    public GameCharacter getVehicle() {
        return vehicle;
    }

    @Override
    public float getMaxLinearSpeed() {
        // TODO Auto-generated method stub
        return 100f;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        // TODO Auto-generated method stub

    }

    @Override
    public float getMaxLinearAcceleration() {
        // TODO Auto-generated method stub
        return 20f;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        // TODO Auto-generated method stub

    }

    @Override
    public float getMaxAngularSpeed() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        // TODO Auto-generated method stub

    }

    @Override
    public float getMaxAngularAcceleration() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        // TODO Auto-generated method stub

    }

    @Override
    public Vector2 getPosition() {
        // TODO Auto-generated method stub
        return vehicle.getBody().getPosition();
    }

    @Override
    public float getOrientation() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Vector2 getLinearVelocity() {
        // TODO Auto-generated method stub
        return vehicle.getBody().getLinearVelocity();
    }

    @Override
    public float getAngularVelocity() {
        // TODO Auto-generated method stub
        return vehicle.getBody().getAngularVelocity();
    }

    @Override
    public float getBoundingRadius() {
        // TODO Auto-generated method stub
        return 64;
    }

    @Override
    public boolean isTagged() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setTagged(boolean tagged) {
        // TODO Auto-generated method stub

    }

    @Override
    public Vector2 newVector() {
        // TODO Auto-generated method stub
        return new Vector2();
    }
}