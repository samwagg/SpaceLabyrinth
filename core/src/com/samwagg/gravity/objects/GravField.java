package com.samwagg.gravity.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.samwagg.gravity.Constants;

public class GravField extends GameObject {

	public static final Texture G_FIELD_TEXT_UNLIT = new Texture(Gdx.files.internal("arrow_unlit.png"));
	public static final Texture G_FIELD_TEXT_LIT = new Texture(Gdx.files.internal("arrow_lit.png"));

	private final float rotation;
	
	private final Sprite LIT_SPRITE;
	private final Sprite UNLIT_SPRITE;
	
	public GravField(float screenX, float screenY, float rotation, World world) {
		super(screenX, screenY, Constants.createArrowUnlit());
		UNLIT_SPRITE = sprite;
		UNLIT_SPRITE.setX(screenX);
		UNLIT_SPRITE.setY(screenY);
		UNLIT_SPRITE.setRotation(rotation);
		
		LIT_SPRITE = Constants.createArrowLit();
		LIT_SPRITE.setX(screenX);
		LIT_SPRITE.setY(screenY);
		LIT_SPRITE.setRotation(rotation);
		
		this.rotation = rotation;
		this.texture = G_FIELD_TEXT_UNLIT;
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
	    bodyDef.position.set(this.physX+.5f*texture.getWidth()*Constants.PHYS_SCALE, this.physY+.5f*texture.getHeight()*Constants.PHYS_SCALE);
	    Body body = world.createBody(bodyDef);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.isSensor = true;
		PolygonShape box = new PolygonShape();
		box.setAsBox(texture.getWidth()*Constants.PHYS_SCALE*.5f, texture.getHeight()*Constants.PHYS_SCALE*.5f);
		fixtureDef.shape = box;
		Fixture fixture = body.createFixture(fixtureDef);	
		
		fixture.setUserData(this);
	}
	
	public void light() {
		sprite = LIT_SPRITE;
	}
	
	public void unlight() {
		sprite = UNLIT_SPRITE;
	}
	
	public float getRotation() {
		return rotation;
	}

}
