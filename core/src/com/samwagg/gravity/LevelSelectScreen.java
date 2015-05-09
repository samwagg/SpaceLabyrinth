package com.samwagg.gravity;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.samwagg.gravity.objects.GameState;

public class LevelSelectScreen implements Screen {

	private GravityGame game;
	GameState state;
	private final Texture background;
	
	private final Sprite level1;
	private final Sprite level2;
	private final Sprite level3;
	private final Sprite level4;
	
	private final Sprite ship;
	
	private final TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("menu_pack.atlas"));
	
	private final List<Sprite> levels;

	private OrthographicCamera camera;
	
	private Vector3 touchInput;
	
	private int currentSelection;
	
	public LevelSelectScreen(GravityGame game, GameState state) {
		this.game = game;
		this.state = state;
		
		currentSelection = state.currentLevel-1;
		
		touchInput = new Vector3(0,0,0);
		
		background = new Texture(Gdx.files.internal("Space-02.png"));
		
		levels = new ArrayList<Sprite>();
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		
		level1 = atlas.createSprite("Dots1");
		level1.setScale(.25f);
		level1.setCenter(camera.viewportWidth*.1f, camera.viewportHeight*.1f);
		levels.add(level1);
		
		level2 = state.maxLevelReached >= 2 ? atlas.createSprite("Dots2") : atlas.createSprite("Dotsr2");
		level2.setScale(.25f);
		level2.setCenter(camera.viewportWidth*.3f, camera.viewportHeight*.5f);
		levels.add(level2);

		level3 = state.maxLevelReached >= 3 ? atlas.createSprite("Dots3") : atlas.createSprite("Dotsr3");
		level3.setScale(.25f);
		level3.setCenter(camera.viewportWidth*.7f, camera.viewportHeight*.4f);
		levels.add(level3);
		
		level4 = state.maxLevelReached >= 4 ? atlas.createSprite("Dots4") : atlas.createSprite("Dotsr4");
		level4.setScale(.25f);
		level4.setCenter(camera.viewportWidth*.9f, camera.viewportHeight*.75f);
		levels.add(level4);
		
		Sprite currentLevel = levels.get(currentSelection);
		ship = atlas.createSprite("menu_ship");
		ship.setScale(.5f);
		ship.setCenter(currentLevel.getX() + currentLevel.getWidth()*.5f, currentLevel.getY() + .75f*currentLevel.getHeight());
		

		

				
		
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    game.batch.setProjectionMatrix(camera.combined);
	    game.batch.begin();
	    game.batch.draw(background, 0, 0, camera.viewportWidth, camera.viewportHeight);
	    level1.draw(game.batch);
	    level2.draw(game.batch);
	    level3.draw(game.batch);
	    level4.draw(game.batch);
	    ship.draw(game.batch);
	    game.batch.end();
	    
	    touchInput.set(Gdx.input.getX(), Gdx.input.getY(), 0);
	    camera.unproject(touchInput);
	    
	    for (int i = 0; i < levels.size(); i++) {
	    	if (Gdx.input.justTouched() && levels.get(i).getBoundingRectangle().contains(touchInput.x, touchInput.y) && i+1 <= state.maxLevelReached) {
	    		if (i == currentSelection) {
	    			game.setScreen(new GravityGameScreen(game, currentSelection+1));
		    		return;
	   
	    		} else {
	    			System.out.println(state.maxLevelReached);
	    		    System.out.println("currentSelection = " + currentSelection);

	    			currentSelection = i;
	    			ship.setCenter(levels.get(i).getX() + levels.get(i).getWidth()*.5f, levels.get(i).getY() + .75f*levels.get(i).getHeight());
	    		}
	    	}
	    }
		
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
		dispose();
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		background.dispose();
		atlas.dispose();
		// TODO Auto-generated method stub
		
	}
	
	private static class Level {

	}

}
