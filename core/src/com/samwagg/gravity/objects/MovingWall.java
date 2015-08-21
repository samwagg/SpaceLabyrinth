package com.samwagg.gravity.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
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

public class MovingWall extends GameObject {
	
	public float speed;
	
	private final boolean isVert; // moves vertically. If false, moves horizontally]
	private final Vector2 minMaxY;
	private final Vector2 minMaxX; 
	
	
	TextureRegion texReg;
	TiledDrawable tiledTex;
	
	
	public MovingWall(float startScreenX, float startScreenY, float screenWidth, float screenHeight, Vector2 minMaxX, Vector2 minMaxY,  World world, Boolean isVert, float speed) {
		super(startScreenX, startScreenY, screenWidth, screenHeight);
		AtlasRegion texReg = Constants.WALL_REGION;
		texReg.getRegionHeight();
		tiledTex = new TiledDrawable(texReg);	
		
		this.speed = speed;
		this.isVert = isVert;
		this.minMaxY = minMaxY;
		this.minMaxX = minMaxX;
		
		float physWidth = screenWidth * Constants.PHYS_SCALE;
		float physHeight = screenHeight * Constants.PHYS_SCALE;
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.KinematicBody;
	    bodyDef.position.set(this.physX + .5f*physWidth , this.physY + .5f*physHeight);
		PolygonShape brickBox = new PolygonShape();
		brickBox.setAsBox(physWidth*.5f, physHeight*.5f);
		//ystem.out.println("brickBox " + brickBox.ge + " " + brickBox.height);
		FixtureDef fixDef = new FixtureDef();
		fixDef.shape = brickBox;
		
		body = world.createBody(bodyDef);
		Fixture fixture = body.createFixture(fixDef);
		fixture.setUserData(this);
		
		brickBox.dispose();	
	}
	
	public void draw(SpriteBatch batch, Camera camera) {
		//test
		
		Vector3 screenCoords = new Vector3(screenX, screenY, 0);
		Vector3 screenDims = new Vector3(screenWidth, screenHeight, 0);
		camera.project(screenCoords);		
		
		// Only draw if all or part of object is within the viewport
//		if ( screenX <= camera.position.x + camera.viewportWidth*.5f &&
//				screenX + screenWidth > camera.position.x - camera.viewportWidth*.5f &&
//				screenY <= camera.position.y + camera.viewportHeight*.5f &&
//				screenY + screenHeight > camera.position.y - camera.viewportHeight*.5f) {
			
		BoundingBox testBox = new BoundingBox(new Vector3(screenX, screenY, 0), new Vector3(screenX + screenWidth, screenY + screenHeight, 0));
		if ( camera.frustum.boundsInFrustum(testBox)) {
			
//			System.out.println("screenX = " + screenCoords.x + ", screenY = " + screenCoords.y);
//			System.out.println("screenHeight = " + screenHeight + " and screenDims.y = " + screenDims.y );
//			System.out.println("viewport width = " + camera.viewportWidth + ", viewport height = " + camera.viewportHeight);
			
//			int xLowLimit = 0;
//			int xHighLimit = (int) camera.viewportWidth;
//			int yLowLimit = 0;
//			int yHighLimit = (int) camera.viewportHeight;
//	
//			
//			int xStart = screenCoords.x >= xLowLimit ? (int)screenCoords.x : xLowLimit - (xLowLimit % 128);  
//			int yStart = screenCoords.y >= yLowLimit ? (int)screenCoords.y : yLowLimit - ((yLowLimit % 128));
//			
//			System.out.println("xStart = " + xStart);
//			System.out.println("yStart = " + yStart);
//			
//			int xEnd = screenCoords.x + screenWidth < xHighLimit ? (int) (screenCoords.x + screenWidth) : xHighLimit+128; 
//			int yEnd = screenCoords.y + screenHeight < yHighLimit? (int) (screenCoords.y + screenHeight) : yHighLimit+128;
//			
//			tiledTex.draw(batch, xStart-64, yStart+64, xEnd-xStart, yEnd-yStart);
//			tiledTex.draw(batch, xStart+64, yStart+64, xEnd-xStart, yEnd-yStart);
//			tiledTex.draw(batch, xStart-64, yStart-64, xEnd-xStart, yEnd-yStart);
//			tiledTex.draw(batch, xStart+64, yStart-64, xEnd-xStart, yEnd-yStart);			
//			
//			int xLowLimit = (int) (camera.position.x - camera.viewportWidth*.5f);
//			int xHighLimit = (int) (camera.position.x + camera.viewportWidth*.5f);
//			int yLowLimit = (int) (camera.position.y - camera.viewportHeight*.5f);
//			int yHighLimit = (int) (camera.position.y + camera.viewportHeight*.5f);
//			
//			System.out.println("viewportwidth = " + camera.viewportWidth);
//			System.out.println("viewportheight = " + camera.viewportHeight);
//			
//			camera.frustum.
			
//			int xStart = screenX >= xLowLimit ? (int)screenX : xLowLimit - (xLowLimit % 128);  
//			int yStart = screenY >= yLowLimit ? (int)screenY : yLowLimit - ((yLowLimit % 128));
//			
//			int xEnd = screenX + screenWidth < xHighLimit ? (int) (screenX + screenWidth) : xHighLimit+128; 
//			int yEnd = screenY + screenHeight < yHighLimit? (int) (screenY + screenHeight) : yHighLimit+128;
			
			int xStart = (int) screenX;
			int yStart = (int) screenY;
			int xEnd = (int) (screenX + screenWidth);
			int yEnd = (int) (screenY + screenHeight);
			
			
			tiledTex.draw(batch, xStart-64, yStart+64, xEnd-xStart+64, yEnd-yStart-64 );
			tiledTex.draw(batch, xStart+64, yStart+64, xEnd-xStart-64, yEnd-yStart-64);
			tiledTex.draw(batch, xStart-64, yStart-64, xEnd-xStart+64, yEnd-yStart+64);
			tiledTex.draw(batch, xStart+64, yStart-64, xEnd-xStart-64, yEnd-yStart+64);
		}	
	}

	
	
//	public void draw(SpriteBatch batch, Camera camera) {
//		tiledTex.draw(batch, screenX, screenY, screenWidth, screenHeight);
//	}
	
	public void move() {
		
		if (isVert) {
			if (body.getLinearVelocity().y >= 0 && screenY < minMaxY.y) {
				body.setLinearVelocity(0,speed);
			} else if  (screenY > minMaxY.x) {
				body.setLinearVelocity(0,-speed);	
			} else body.setLinearVelocity(0,speed);
		} else {
			if (body.getLinearVelocity().x >= 0 && screenX < minMaxX.y) {
				body.setLinearVelocity(speed,0);
			} else if  (screenX > minMaxX.x) {
				body.setLinearVelocity(-speed,0);	
			} else body.setLinearVelocity(speed,0);			
		}
			
		updatePosition(); // update the corresponding screen coordinates
	}
	
	

}
