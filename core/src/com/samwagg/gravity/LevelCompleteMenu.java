package com.samwagg.gravity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class LevelCompleteMenu implements Screen {
	
	private Stage stage;
	private Table table;
	// For debug drawing
	private ShapeRenderer shapeRenderer;
	private int level;
	private GravityGame game;
	
    private boolean buttonClicked = false;


	public LevelCompleteMenu (final GravityGame game, int level, int score) {
		game.writeHighScore(level, score);
		
	    stage = new Stage();
	    Gdx.input.setInputProcessor(stage);
	    
	    this.level = level;
	    this.game = game;
	    
	    table = new Table();
	    table.setFillParent(true);
	    stage.addActor(table);
	    Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
	    table.setSkin(skin);
	    Button nextLevelButton = new Button(skin);
	    nextLevelButton.add("Next Level");
	    Button retryLevelButton = new Button(skin);
	    retryLevelButton.add("Retry Level");
	    
	    
	    nextLevelButton.addListener(new EventListener() {

			@Override
			public boolean handle(Event event) {
				// TODO Auto-generated method stub
				if (!buttonClicked) {
					buttonClicked = true;
					toNextLevel();
				}
				return true;
			}
	    	
	    });
	    
	    retryLevelButton.addListener(new EventListener() {

			@Override
			public boolean handle(Event event) {
				if (!buttonClicked) {
					buttonClicked = true;
				retryLevel();
				}
				return true;
			}
	    	
	    });
	    
	    Label headLabel = new Label("Level Complete", skin);
	    Label scoreLabel = new Label("Score: " + score, skin);
	    Label hScoreLabel = new Label("High Score = " + game.readHighScore(level), skin);
	    
	    table.add(headLabel);
	    table.row();
	    table.add(scoreLabel);
	    table.row();
	    table.add(hScoreLabel);
	    table.row();
	    
	  
	    table.add(nextLevelButton).padBottom(75).padTop(100);
	    table.row(); 
	    table.add(retryLevelButton);
	    
	    shapeRenderer = new ShapeRenderer();

	    // Add widgets to the table here.
	}
	
	private void toNextLevel() {
		game.setScreen(new GravityGameScreen(game, level + 1));
	}
	
	private void retryLevel() {
		game.setScreen(new GravityGameScreen(game, level));		
	}

	public void resize (int width, int height) {
	    stage.getViewport().update(width, height, true);
	}



	public void dispose() {
	    stage.dispose();
	    shapeRenderer.dispose();
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    stage.act(delta);
	    stage.draw();

	    table.drawDebug(shapeRenderer); // This is optional, but enables debug lines for tables.
		
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

}
