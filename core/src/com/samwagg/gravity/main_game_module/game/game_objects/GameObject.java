package com.samwagg.gravity.main_game_module.game.game_objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.samwagg.gravity.Constants;

public abstract class GameObject  {

	protected float initX;
	protected float initY;
	protected float width;
	protected float height;

	protected Body body;
	
	public GameObject(float initX, float initY, float width, float height, World world) {
		this.initX = initX;
		this.initY = initY;
		this.width = width;
		this.height = height;

		setupBody(world);
	}

	public float getX() {
		return body.getPosition().x;
	}
	
	public float getY() {
		return body.getPosition().y;
	}
	
	public Body getBody() {
		return body;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public abstract void step(float delta);

	/**
	 * Initialize box2d body. Body can be blank. Null bodies are allowed
     */
	protected abstract void setupBody(World world);


}
