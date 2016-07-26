package com.samwagg.gravity.main_game_module.game_objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Disposable;
import com.samwagg.gravity.Constants;

public class GameObject implements Disposable {

	protected float screenX;
	protected float screenY;
	protected float screenWidth;
	protected float screenHeight;

	protected Constants constants;
	
	protected float physX;
	protected float physY;
	
	protected Texture texture;
	
	protected Body body;
	protected Sprite sprite;

	
	public GameObject(float screenX, float screenY, float screenWidth, float screenHeight, Sprite sprite, Constants constants) {
		this.screenX = screenX;
		this.screenY = screenY;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.constants = constants;
		physX = screenX * constants.PHYS_SCALE;
		physY = screenY * constants.PHYS_SCALE;
		
		this.sprite = sprite;
		sprite.setX(screenX);
		sprite.setY(screenY);
	}
	
	public GameObject(float screenX, float screenY, float screenWidth, float screenHeight, Constants constants) {
		this.screenX = screenX;
		this.screenY = screenY;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.constants = constants;
		physX = screenX * constants.PHYS_SCALE;
		physY = screenY * constants.PHYS_SCALE;

	}
	
	public GameObject(float screenX, float screenY, Sprite sprite, Constants constants) {
		this.screenX = screenX;
		this.screenY = screenY;
		this.constants = constants;
		physX = screenX * constants.PHYS_SCALE;
		physY = screenY * constants.PHYS_SCALE;
		screenWidth = constants.TILE_SIZE;
		screenHeight = constants.TILE_SIZE;
		this.sprite = sprite;
		sprite.setX(screenX);
		sprite.setY(screenY);
		
	}
	
	public GameObject(float screenX, float screenY, Constants constants) {
		this.constants = constants;
		this.screenX = screenX;
		this.screenY = screenY;
		physX = screenX * constants.PHYS_SCALE;
		physY = screenY * constants.PHYS_SCALE;
		screenWidth = constants.TILE_SIZE;
		screenHeight = constants.TILE_SIZE;
	}
	


	public void updatePosition() {
		
		this.screenX = body.getPosition().x / constants.PHYS_SCALE - screenWidth/2;
		this.screenY = body.getPosition().y / constants.PHYS_SCALE - screenHeight/2;
		if (sprite != null) {
			sprite.setX(screenX);
			sprite.setY(screenY);
		}
	}
	
	public Sprite getSprite() {
		return sprite;
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public float getScreenX() {
		return screenX;
	}
	
	public float getScreenY() {
		return screenY;
	}
	
	public float getPhyX() {
		return physX;
	}
	
	public float getPhysY() {
		return physY;
	}
	
	public Body getBody() {
		return body;
	}

	@Override
	public void dispose() {
		
		
	}
	
	
	
}
