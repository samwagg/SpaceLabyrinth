package com.samwagg.gravity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;

public class Constants {
	
	public static final float PHYS_SCALE = .05f;
	public static final int TILE_SIZE = 64;
	public static final int N_LEVELS = 5;
	
	public static final TextureAtlas ATLAS = new TextureAtlas(Gdx.files.internal("pack.atlas"));
//
	public static final Sprite CHAR_SPRITE =  ATLAS.createSprite("Ship");
	public static final AtlasRegion WALL_REGION = ATLAS.findRegion("wall");

	
	public static Sprite createArrowLit() {
		return ATLAS.createSprite("arrow_lit");
	}
	
	public static Sprite createArrowUnlit() {
		return ATLAS.createSprite("arrow_unlit");
	}
	
	public static void initConstants() {
		WALL_REGION.setRegionHeight(WALL_REGION.originalHeight);
		WALL_REGION.setRegionWidth(WALL_REGION.originalWidth);
	}

	

}
