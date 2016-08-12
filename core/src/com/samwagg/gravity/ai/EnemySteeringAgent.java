package com.samwagg.gravity.ai;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.samwagg.gravity.main_game_module.game.game_objects.GameCharacter;

// A simple steering agent for 2D.
// Of course, for 3D (well, actually for 2.5D) you have to replace all occurrences of Vector2 with  Vector3.
public class EnemySteeringAgent implements Steerable<Vector2> {

    private static final SteeringAcceleration<Vector2> steeringOutput = 
        new SteeringAcceleration<Vector2>(new Vector2());

    Vector2 position;
    float orientation;
    Vector2 linearVelocity;
    float angularVelocity;
    float maxSpeed;
    boolean independentFacing;
    private SteeringBehavior<Vector2> steeringBehavior;
    
    private GameCharacter vehicle;
    
    public EnemySteeringAgent(float x, float y,  GameCharacter vehicle) {
    	this.vehicle = vehicle;
		position = new Vector2(x,y);
     }

     public void pursue(Steerable<Vector2> thingToPursue) {
		 Seek<Vector2> seek = new Seek<Vector2>(this, thingToPursue);
		 steeringBehavior = seek;
	 }

	 public SteeringBehavior<Vector2> getSteeringBehavior() {
	 	return steeringBehavior;
	 }

    /* Here you should implement missing methods inherited from Steerable */

    // Actual implementation depends on your coordinate system.
    // Here we assume the y-axis is pointing upwards.
    @Override
    public float vectorToAngle (Vector2 vector) {
        return (float)Math.atan2(-vector.x, vector.y);
    }

    // Actual implementation depends on your coordinate system.
    // Here we assume the y-axis is pointing upwards.
    @Override
    public Vector2 angleToVector (Vector2 outVector, float angle) {
        outVector.x = -(float)Math.sin(angle);
        outVector.y = (float)Math.cos(angle);
        return outVector;
    }

    public void update (float delta) {
        if (steeringBehavior != null) {
            // Calculate steering acceleration
            steeringBehavior.calculateSteering(steeringOutput);
            /*
             * Here you might want to add a motor control layer filtering steering accelerations.
             * 
             * For instance, a car in a driving game has physical constraints on its movement:
             * - it cannot turn while stationary
             * - the faster it moves, the slower it can turn (without going into a skid)
             * - it can brake much more quickly than it can accelerate
             * - it only moves in the direction it is facing (ignoring power slides)
             */

            // Apply steering acceleration to move this agent
            applySteering(steeringOutput, delta);
        }
    }

    private void applySteering (SteeringAcceleration<Vector2> steering, float time) {
        // Update position and linear velocity. Velocity is trimmed to maximum speed
        //this.position.mulAdd(linearVelocity, time);
        //this.linearVelocity.mulAdd(steering.linear, time).limit(this.getMaxLinearSpeed());
        vehicle.getBody().setLinearVelocity(vehicle.getBody().getLinearVelocity().mulAdd(steering.linear, time).limit(getMaxLinearSpeed()));
        // Update orientation and angular velocity
        
        //this.orientation += angularVelocity * time;
        //this.angularVelocity += steering.angular * time;

    }
    
    public GameCharacter getVehicle() {
    	return vehicle;
    }

	@Override
	public float getMaxLinearSpeed() {
		// TODO Auto-generated method stub
		return 10f;
	}

	@Override
	public void setMaxLinearSpeed(float maxLinearSpeed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getMaxLinearAcceleration() {
		// TODO Auto-generated method stub
		return 10f;
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