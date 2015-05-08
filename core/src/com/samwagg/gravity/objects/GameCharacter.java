package com.samwagg.gravity.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.samwagg.gravity.Constants;

public class GameCharacter extends GameObject implements Steerable<Vector2> {
	
	public static final Texture CHAR_TEXT = new Texture(Gdx.files.internal("Ship.png"));
	
	
	public GameCharacter(float screenX, float screenY, World world) {
		super(screenX+CHAR_TEXT.getWidth(), screenY+CHAR_TEXT.getHeight(), Constants.CHAR_SPRITE);
		

		

		
		BodyDef bodyDef = new BodyDef();
	    bodyDef.type = BodyType.DynamicBody;
	    bodyDef.allowSleep = false;
	    bodyDef.position.set(this.physX, this.physY);
	    FixtureDef charFixtureDef = new FixtureDef();
	    CircleShape circle = new CircleShape();
	    circle.setRadius(Constants.CHAR_SPRITE.getHeight()/2*Constants.PHYS_SCALE);
	    charFixtureDef.shape = circle;
	    charFixtureDef.density = 10f;
	    charFixtureDef.friction = 0f;
	    charFixtureDef.restitution = .9f;
	    
		body = world.createBody(bodyDef);
		Fixture fixture = body.createFixture(charFixtureDef);
		fixture.setUserData(this);
		
	    circle.dispose();		
	}


	
	@Override
	public float getMaxLinearSpeed() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setMaxLinearSpeed(float maxLinearSpeed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getMaxLinearAcceleration() {
		// TODO Auto-generated method stub
		return 0;
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
		return getBody().getPosition();
	}

	@Override
	public float getOrientation() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Vector2 getLinearVelocity() {
		// TODO Auto-generated method stub
		return getBody().getLinearVelocity();
	}

	@Override
	public float getAngularVelocity() {
		// TODO Auto-generated method stub
		return getBody().getAngularVelocity();
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
	

}
