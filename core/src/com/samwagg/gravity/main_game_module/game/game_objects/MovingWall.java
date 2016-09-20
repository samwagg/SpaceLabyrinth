package com.samwagg.gravity.main_game_module.game.game_objects;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.samwagg.gravity.Constants;

public class MovingWall extends Wall {
	
	public float speed;
	
	private final boolean isVert; // moves vertically. If false, moves horizontally]
	private final Vector2 minMaxY;
	private final Vector2 minMaxX;
	
	public MovingWall(float initX, float initY, float physWidth, float physHeight, Vector2 minMaxX, Vector2 minMaxY,  World world, Boolean isVert, float speed) {
		super(initX, initY, physWidth, physHeight, world);
		
		this.speed = speed;
		this.isVert = isVert;
		this.minMaxY = minMaxY;
		this.minMaxX = minMaxX;
	}

	@Override
	public void step(float delta) {
		move();
	}

	@Override
	protected void setupBody(World world) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.KinematicBody;
		bodyDef.position.set(this.initX , this.initY);
		PolygonShape brickBox = new PolygonShape();
		brickBox.setAsBox(width*.5f, height*.5f);
		//ystem.out.println("brickBox " + brickBox.ge + " " + brickBox.height);
		FixtureDef fixDef = new FixtureDef();
		fixDef.shape = brickBox;

		body = world.createBody(bodyDef);
		Fixture fixture = body.createFixture(fixDef);
		fixture.setUserData(this);

		brickBox.dispose();
	}
//	public void draw(SpriteBatch batch, Camera camera) {
//		//test
//
//		Vector3 screenCoords = new Vector3(screenX, screenY, 0);
//		Vector3 screenDims = new Vector3(screenWidth, screenHeight, 0);
//		camera.project(screenCoords);
//
//		// Only draw if all or part of object is within the viewport
////		if ( screenX <= camera.position.x + camera.viewportWidth*.5f &&
////				screenX + screenWidth > camera.position.x - camera.viewportWidth*.5f &&
////				screenY <= camera.position.y + camera.viewportHeight*.5f &&
////				screenY + screenHeight > camera.position.y - camera.viewportHeight*.5f) {
//
//		BoundingBox testBox = new BoundingBox(new Vector3(screenX, screenY, 0), new Vector3(screenX + screenWidth, screenY + screenHeight, 0));
//		if ( camera.frustum.boundsInFrustum(testBox)) {
//
//			int xStart = (int) screenX;
//			int yStart = (int) screenY;
//			int xEnd = (int) (screenX + screenWidth);
//			int yEnd = (int) (screenY + screenHeight);
//
//			tiledTex.draw(batch, xStart-64, yStart+64, screenWidth%256 == 0? screenWidth+64 : screenWidth+128, screenHeight%256 == 0? screenHeight : screenHeight-64);
//			tiledTex.draw(batch, xStart+64, yStart+64, screenWidth%256 == 0? screenWidth : screenWidth-64, screenHeight%256 == 0? screenHeight : screenHeight-64);
//			tiledTex.draw(batch, xStart-64, yStart-64, screenWidth%256 == 0? screenWidth+64 : screenWidth+128, screenHeight%256 == 0? screenHeight+64 : screenHeight+128);
//			tiledTex.draw(batch, xStart+64, yStart-64, screenWidth%256 == 0? screenWidth : screenWidth-64, screenHeight%256 == 0? screenHeight+64 : screenHeight+128);
//		}
//	}

	
	
//	public void draw(SpriteBatch batch, Camera camera) {
//		tiledTex.draw(batch, screenX, screenY, screenWidth, screenHeight);
//	}
	
	public void move() {


		if (isVert) {
			if (body.getLinearVelocity().y >= 0 && getY() < minMaxY.y) {
				body.setLinearVelocity(0,speed);
			} else if  (getY() > minMaxY.x) {
				body.setLinearVelocity(0,-speed);
			} else  {
				body.setLinearVelocity(0,speed);
			}
		} else {
			if (body.getLinearVelocity().x >= 0 && getX() < minMaxX.y) {
				body.setLinearVelocity(speed,0);
			} else if  (getX() > minMaxX.x) {
				body.setLinearVelocity(-speed,0);	
			} else body.setLinearVelocity(speed,0);			
		}
	}
}
