package com.samwagg.gravity.main_game_module.game_objects;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.samwagg.gravity.Constants;

public class FinishSensor extends GameObject {
	
	public FinishSensor(float x, float y, World world, Constants constants) {

		super(x, y, constants);

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(this.physX + .5f * Wall.WALL_TEXT.getWidth() * constants.PHYS_SCALE, this.physY + .5f * Wall.WALL_TEXT.getHeight() *constants.PHYS_SCALE);
		PolygonShape brickBox = new PolygonShape();
		brickBox.setAsBox(Wall.WALL_TEXT.getWidth()*constants.PHYS_SCALE*.5f, Wall.WALL_TEXT.getHeight()*constants.PHYS_SCALE*.5f);
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