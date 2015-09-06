package com.samwagg.gravity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;

public class Constants {
	
	public final float PHYS_SCALE = .05f;
	public final int TILE_SIZE = 64;
	public final int N_LEVELS = 7;

	public TextureAtlas ATLAS;

	public Sprite CHAR_SPRITE;
	public AtlasRegion WALL_REGION;


	public Constants() {
		ATLAS = new TextureAtlas("pack.atlas");
		CHAR_SPRITE = ATLAS.createSprite("Ship");
		WALL_REGION = ATLAS.findRegion("wall");
	}

	public Sprite createArrowLit() {
		Sprite sprite = ATLAS.createSprite("arrow_lit");
		sprite.setSize(TILE_SIZE * 2, TILE_SIZE * 2);
		sprite.setOriginCenter();

		return sprite;
	}

	public Sprite createArrowUnlit() {
		Sprite sprite = ATLAS.createSprite("arrow_unlit");
		sprite.setSize(TILE_SIZE * 2, TILE_SIZE * 2);
		sprite.setOriginCenter();
		return sprite;
	}
}
