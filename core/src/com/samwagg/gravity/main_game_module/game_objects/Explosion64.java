package com.samwagg.gravity.main_game_module.game_objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Explosion64 {

	private final int TOTAL_FRAMES = 32;

	private Texture explosionPack;

	private int currentFrameIndex;
	private int currentFrame;
	
	private float x;
	private float y;
	
	private boolean explosionGrowing;
	
	public Explosion64(float x, float y) {
		explosionPack = new Texture(Gdx.files.internal("explosion.png"));
		//explosionPack = new Texture(Gdx.files.internal("arrow.png"));
		currentFrameIndex = 15;
		currentFrame = 0;
		
		this.x = x;
		this.y = y;
		
		explosionGrowing = true;
	}

	/**
	 * Draw next explosion frame in its current position
	 * @param batch
     */
	public void drawNextFrame(SpriteBatch batch) {

		if (!done()) {
			batch.draw(explosionPack, x - 32, y - 32, (currentFrameIndex % 4) * 128, (currentFrameIndex / 4) * 128, 128, 128);
			//batch.draw(explosionPack, x, y);
			if (currentFrameIndex > 0 && explosionGrowing) currentFrameIndex--;
			else if (currentFrameIndex == 0) {
				explosionGrowing = false;
				currentFrameIndex++;
			}
			else currentFrameIndex++;

			currentFrame++;
		}
	}

	public void updatePosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getCurrentFrame() {
		return currentFrame;
	}

	public int getTotalFrames() {
		return TOTAL_FRAMES;
	}

	public boolean done() {
		return currentFrame == TOTAL_FRAMES;
	}
	
	public void dispose() {
		explosionPack.dispose();
	}
}
