package com.samwagg.gravity.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Explosion64 {

	
	private int framesPerTick = 100;

	
	private Texture explosionPack;
	private int currentFrame;
	
	private float x;
	private float y;
	
	private boolean explosionGrowing;
	
	public Explosion64(float x, float y) {
		explosionPack = new Texture(Gdx.files.internal("explosion.png"));
		//explosionPack = new Texture(Gdx.files.internal("arrow.png"));
		currentFrame = 15;
		
		this.x = x;
		this.y = y;
		
		explosionGrowing = true;
	}
	
	public boolean drawNextFrame(SpriteBatch batch) {
		batch.draw(explosionPack, x-32, y-32,(currentFrame % 4)*128,(currentFrame /4)*128,128,128);
		//batch.draw(explosionPack, x, y);
		if (currentFrame > 0 && explosionGrowing) currentFrame--;
		else if (currentFrame == 0) {
			explosionGrowing = false;
			currentFrame ++;
		}
		else currentFrame++;
		
		
		if (currentFrame > 15) return true;
		else return false;
	}
	
	public boolean drawNextFrame(SpriteBatch batch, float x, float y) {
		batch.draw(explosionPack, x-32, y-32,(currentFrame % 4)*128,(currentFrame /4)*128,128,128);
		//batch.draw(explosionPack, x, y);
		if (currentFrame > 0 && explosionGrowing) currentFrame--;
		else if (currentFrame == 0) {
			explosionGrowing = false;
			currentFrame ++;
		}
		else currentFrame++;
		
		this.x = x;
		this.y = y;
		
		if (currentFrame > 15) return true;
		else return false;
	}
	
	public boolean halfDone() {
		if (currentFrame < 7) return true;
		else return false;
	}
	
	public void dispose() {
		explosionPack.dispose();
	}
}
