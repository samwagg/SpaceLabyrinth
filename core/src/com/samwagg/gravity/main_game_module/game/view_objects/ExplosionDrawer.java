package com.samwagg.gravity.main_game_module.game.view_objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.samwagg.gravity.main_game_module.game.game_objects.Explosion;

public class ExplosionDrawer {

	private static final int TOTAL_FRAMES = 32;
	private static final int FRAMES_PER_ROW = 4;
	private static final int FRAME_SIZE = 128;
	private static final String EXPLOSION_FILE = "explosion.png";

	private Texture explosionPack;

	public ExplosionDrawer() {
		explosionPack = new Texture(Gdx.files.internal(EXPLOSION_FILE));
		//explosionPack = new Texture(Gdx.files.internal("arrow.png"));
	}

	/**
	 * Draw next explosion frame in its current position
	 * @param batch
     */
	public void drawFrame(SpriteBatch batch, Explosion explosion, float physScale) {

		int currentFrame = (int) ((1 - explosion.getDurationRemaining() / explosion.getInitialDuration())  * TOTAL_FRAMES);
		System.out.println("width = " + explosion.getWidth());
		if (!explosion.done()) {

			batch.draw(explosionPack, explosion.getX()/physScale - FRAME_SIZE/2,
					explosion.getY()/physScale - FRAME_SIZE/2, (currentFrame % FRAMES_PER_ROW) * FRAME_SIZE,
					(currentFrame / FRAMES_PER_ROW) * FRAME_SIZE, FRAME_SIZE, FRAME_SIZE);
			//batch.draw(explosionPack, x, y);
		}
	}

	public void dispose() {
		explosionPack.dispose();
	}
}
