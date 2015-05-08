package com.samwagg.gravity.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
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
	
	TextureRegion texReg;
	TiledDrawable tiledTex;
	
	public Wall(float screenX, float screenY, float screenWidth, float screenHeight, World world) {
		super(screenX, screenY, screenWidth, screenHeight);
		this.texture = WALL_TEXT;
		
		AtlasRegion texReg = Constants.WALL_REGION;
		tiledTex = new TiledDrawable(texReg);

		float physWidth = screenWidth * Constants.PHYS_SCALE;
		float physHeight = screenHeight * Constants.PHYS_SCALE;
		
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
		tiledTex.draw(batch, screenX, screenY, screenWidth, screenHeight);
	}

}
