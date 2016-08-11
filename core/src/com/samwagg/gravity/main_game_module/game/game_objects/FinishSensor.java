package com.samwagg.gravity.main_game_module.game.game_objects;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.samwagg.gravity.Constants;

public class FinishSensor extends GameObject {
	
	public FinishSensor(float x, float y, World world, Constants constants) {

		super(x + constants.TILE_SIZE, y + constants.TILE_SIZE, constants);

		float physWidth = screenWidth * constants.PHYS_SCALE;
		float physHeight = screenHeight * constants.PHYS_SCALE;

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(this.physX + .5f*physWidth , this.physY + .5f*physHeight);
		PolygonShape brickBox = new PolygonShape();
		brickBox.setAsBox(physWidth*.5f, physHeight*.5f);
		//ystem.out.println("brickBox " + brickBox.ge + " " + brickBox.height);
		FixtureDef fixDef = new FixtureDef();
		fixDef.shape = brickBox;
		fixDef.isSensor = true;
		body = world.createBody(bodyDef);
		Fixture fixture = body.createFixture(fixDef);
		fixture.setUserData(this);
		
		brickBox.dispose();	
	}

}
