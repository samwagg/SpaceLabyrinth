package com.samwagg.gravity.main_game_module.game.game_objects;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.samwagg.gravity.Constants;

public class FinishSensor extends GameObject {
	
	public FinishSensor(float physX, float initY, float width, float height, World world) {
		super(physX, initY, width, height, world);
	}

	@Override
	protected void setupBody(World world) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(this.initX + .5f*width , this.initY + .5f*height);
		PolygonShape brickBox = new PolygonShape();
		brickBox.setAsBox(width*.5f, height*.5f);
		//ystem.out.println("brickBox " + brickBox.ge + " " + brickBox.height);
		FixtureDef fixDef = new FixtureDef();
		fixDef.shape = brickBox;
		fixDef.isSensor = true;
		body = world.createBody(bodyDef);
		Fixture fixture = body.createFixture(fixDef);
		fixture.setUserData(this);

		brickBox.dispose();
	}

	@Override
	public void step(float delta) {
		// nothing to do for FinishSensor
	}

}
