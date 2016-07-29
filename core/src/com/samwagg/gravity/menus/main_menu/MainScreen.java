package com.samwagg.gravity.menus.main_menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.samwagg.gravity.GravityGame;
import com.samwagg.gravity.ScreenFlowController;

public class MainMenu implements Screen {

	private Texture texture;
	
	private GravityGame game;
	private ScreenFlowController controller;
	
	private OrthographicCamera camera;
	
	private Vector3 touchInput;
	
	public MainMenu(ScreenFlowController controller, GravityGame game) {
		this.game = game;
		this.controller = controller;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		touchInput = new Vector3(0,0,0);
	}
	
	@Override
	public void show() {
		texture = new Texture(Gdx.files.internal("Start.png"));
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    game.batch.setProjectionMatrix(camera.combined);
	    game.batch.begin();
		game.batch.draw(texture,0,0,camera.viewportWidth,camera.viewportHeight);
		game.batch.end();


		if (Gdx.input.justTouched()) {
			touchInput = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchInput);

			if (touchInput.x < 500 && touchInput.x > 300 && touchInput.y < 200 && touchInput.y > 100) {
				controller.mainMenuStartClicked();
			}

			if (touchInput.x < 450 && touchInput.x > 350 && touchInput.y > camera.viewportHeight - 50) {
//				game.resetGameProgress();
//				System.out.println("here");
			}

			if (touchInput.x < 300 && touchInput.y < 300) {
				for (int i = 0; i < game.getGameState().totalGalaxies; i++) {
//					game.getGameState().maxLevelReachedByGalaxy.add(i, Constants.N_LEVELS);
				}
			}
		}
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
