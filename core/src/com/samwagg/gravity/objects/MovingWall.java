package com.samwagg.gravity.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.samwagg.gravity.Constants;

public class MovingWall extends GameObject {

	public final static Texture WALL_TEXT = new Texture(Gdx.files.internal("wall.png"));
	
	public float speed;
	
	private final boolean isVert; // moves vertically. If false, moves horizontally]
	private final Vector2 minMaxY;
	private final Vector2 minMaxX; 
	
	private boolean movingPos; // whether this objects is currently moving in the positive direction
	
	TextureRegion texReg;
	TiledDrawable tiledTex;
	
	
	public MovingWall(float startScreenX, float startScreenY, float screenWidth, float screenHeight, Vector2 minMaxX, Vector2 minMaxY,  World world, Boolean isVert, float speed) {
		super(startScreenX, startScreenY, screenWidth, screenHeight);
		AtlasRegion texReg = Constants.WALL_REGION;
		texReg.getRegionHeight();
		tiledTex = new TiledDrawable(texReg);
		
		
		this.speed = speed;
		this.isVert = isVert;
		movingPos = true;
		this.minMaxY = minMaxY;
		this.minMaxX = minMaxX;
		
		
		//float verts[] = {physX, physY, physX, physY + screenHeight*Constants.PHYS_SCALE, physX + screenWidth*Constants.PHYS_SCALE, physY + screenHeight*Constants.PHYS_SCALE, physX + screenWidth*Constants.PHYS_SCALE, physY}; 
		float physWidth = screenWidth * Constants.PHYS_SCALE;
		float physHeight = screenHeight * Constants.PHYS_SCALE;
		//float verts[] = {physX, physY, physX, physY + screenHeight*Constants.PHYS_SCALE, physX + screenWidth*Constants.PHYS_SCALE, physY + screenHeight*Constants.PHYS_SCALE, physX + screenWidth*Constants.PHYS_SCALE, physY}; 
		
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
		tiledTex.draw(batch, screenX, screenY, screenWidth, screenHeight);
	}
	
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
