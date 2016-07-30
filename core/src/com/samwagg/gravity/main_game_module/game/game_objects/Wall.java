package com.samwagg.gravity.main_game_module.game.game_objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.samwagg.gravity.Constants;

public class Wall extends GameObject {

	public final static Texture WALL_TEXT = new Texture(Gdx.files.internal("wall.png"));
	
	private TextureRegion texReg;
	private TiledDrawable tiledTex;
	
	
	public Wall(float screenX, float screenY, float screenWidth, float screenHeight, World world, Constants constants) {
		super(screenX, screenY, screenWidth, screenHeight, constants);
		this.texture = WALL_TEXT;
		
		AtlasRegion texReg = constants.WALL_REGION;
//		texReg.offsetX = 64;
//		texReg.offsetY = 64;
		tiledTex = new TiledDrawable(texReg);

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
//			System.3out.println("screenHeight = " + screenHeight + " and screenDims.y = " + screenDims.y );
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
			
			tiledTex.draw(batch, xStart-64, yStart+64, screenWidth%256 == 0? screenWidth+64 : screenWidth+128, screenHeight%256 == 0? screenHeight : screenHeight-64);
			tiledTex.draw(batch, xStart+64, yStart+64, screenWidth%256 == 0? screenWidth : screenWidth-64, screenHeight%256 == 0? screenHeight : screenHeight-64);
			tiledTex.draw(batch, xStart-64, yStart-64, screenWidth%256 == 0? screenWidth+64 : screenWidth+128, screenHeight%256 == 0? screenHeight+64 : screenHeight+128);
			tiledTex.draw(batch, xStart+64, yStart-64, screenWidth%256 == 0? screenWidth : screenWidth-64, screenHeight%256 == 0? screenHeight+64 : screenHeight+128);
		}	
	}
}
