package com.samwagg.gravity.menus.level_complete_menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.samwagg.gravity.GravityGame;

public class LevelCompleteScreen implements Screen, LevelCompleteMenu {

	private LevelCompleteMenuListener listener;
	private Stage stage;
	private Table table;
	// For debug drawing
	private ShapeRenderer shapeRenderer;
	private int level;
	private GravityGame game;

	private boolean finalLevel;
	
    private boolean buttonClicked = false;
    
    private final static String NEXT_LEVEL_BUT_TEXT = "Next Level";
    private final static String RETRY_BUT_TEXT = "Retry";
    private final static String MENU_BUT_TEXT = "Menu";
    private final static String FINISH_TEXT = "Finish";
    private final static float TRANSITION_DURATION = 1f;

    private final float BUT_WIDTH = 500;

	public LevelCompleteScreen(final GravityGame game, int score, int highScore, boolean isFinalLevel) {
		
		finalLevel = isFinalLevel;

	    stage = new Stage(new ExtendViewport(1800,900));
	    
	    Gdx.input.setInputProcessor(stage);
	    
	    this.game = game;
	    
	    table = new Table();
//	    table.setFillParent(true);
	    stage.addActor(table);
	    Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
	    table.setSkin(skin);

		table.setBounds(0,0,stage.getWidth(),stage.getHeight());
		table.getColor().a=0;
//		MoveToAction transAct = new MoveToAction();
//		transAct.setPosition(0,0);
//		transAct.setDuration(TRANSITION_DURATION);

		AlphaAction act = new AlphaAction();
		act.setAlpha(1f);
		act.setDuration(TRANSITION_DURATION);
		table.addAction(act);


		Button nextLevelButton = new TextButton(finalLevel? FINISH_TEXT : NEXT_LEVEL_BUT_TEXT , skin);
		Button retryLevelButton = new TextButton(RETRY_BUT_TEXT ,skin);
		Button menuButton = new TextButton(MENU_BUT_TEXT, skin);
	    
	    nextLevelButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (listener != null) {
					if (finalLevel) listener.galaxyFinished();
					else listener.nextLevelSelected();
				}
			}
	    });
	    
	    retryLevelButton.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (listener != null) listener.retryLevelSelected();
			}
	    	
	    });
	    
	    menuButton.addListener(new ClickListener() {
	    	@Override
			public void clicked(InputEvent event, float x, float y) { 				
	    		if (listener != null) listener.mainMenuSelected();
	    	}
	    });
	    
	    Label headLabel = new Label("Level Complete", skin);
	    Label scoreLabel = new Label("Score: " + score, skin);
	    Label hScoreLabel = new Label("High Score = " + highScore, skin);
	    
	    table.add(headLabel);
	    table.row();
	    table.add(scoreLabel);
	    table.row();
	    table.add(hScoreLabel);
	    table.row();
	    
	  
	    table.add(nextLevelButton).padTop(50).width(BUT_WIDTH);
	    table.row(); 
	    table.add(retryLevelButton).width(BUT_WIDTH).padTop(50);
	    table.row();
	    table.add(menuButton).width(BUT_WIDTH).padTop(50);
	    
	    shapeRenderer = new ShapeRenderer();

	    // Add widgets to the table here.
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
		Gdx.gl.glClearColor(0, 0, 0, 1);
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

	@Override
	public void registerLevelCompleteMenuListener(LevelCompleteMenuListener listener) {
		this.listener = listener;
	}
}
