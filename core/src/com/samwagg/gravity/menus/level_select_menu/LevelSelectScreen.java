package com.samwagg.gravity.menus.level_select_menu;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.samwagg.gravity.Constants;
import com.samwagg.gravity.GravityGame;
import com.samwagg.gravity.controller.GravityGameController;

public class LevelSelectScreen implements Screen {

	private GravityGame game;

	private final Texture background;
	GravityGameController controller;
	
	int galaxy;
	
	Stage stage;
	
//	private final Sprite level1;
//	private final Sprite level2;
//	private final Sprite level3;
//	private final Sprite level4;
//	private final Sprite level5;
//	private final Sprite level6;
//	private final Sprite level7;
	
	private float icon_pos[] = { .1f, .3f, .5f, .3f, .4f, .7f, .4f, .5f, .75f  };
	
	private final Actor ship;
	
	private final TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("menu_pack.atlas"));
	private final TextureAtlas backAtlas = new TextureAtlas(Gdx.files.internal("galaxy_unlocked_pack.atlas"));
	
	private ImageButton backButton;
	
	private final List<Sprite> levels;

	private OrthographicCamera camera;
	
	private Vector3 touchInput;
	
	private int currentSelection;
	private int maxLevelReached;
	
	private final float SHIP_ANIM_DUR = .5f;
	
	public LevelSelectScreen(GravityGameController menuController, GravityGame game, int galaxy, int currentLevel, int maxLevelReached, Constants constants) {
		this.game = game;
		this.galaxy = galaxy;
		this.controller = menuController;

		System.out.println("Max level reached = " + maxLevelReached + " galaxy = " + galaxy);

		currentSelection = currentLevel;
		this.maxLevelReached = maxLevelReached;
		
		touchInput = new Vector3(0,0,0);
		
		background = new Texture(Gdx.files.internal("Space-02.png"));
		
		levels = new ArrayList<Sprite>();
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		
	    stage = new Stage(new ExtendViewport(1600,800));

		for (int i = 1; i <= constants.N_LEVELS; i++) {
			System.out.println("Making icon for level " + i);
			Sprite leveli = maxLevelReached >= i ? atlas.createSprite("Dots", i) : atlas.createSprite("Dotsr", i);
			leveli.setScale(.25f);
			leveli.setCenter(camera.viewportWidth*(i-.5f)*1/constants.N_LEVELS,  camera.viewportHeight* icon_pos[i-1]);
			levels.add(leveli);
		}
		
//		level1 = atlas.createSprite("Dots1");
//		level1.setScale(.25f);
//		level1.setCenter(camera.viewportWidth*.1f, camera.viewportHeight*.1f);
//		levels.add(level1);
//		
//		level2 = state.maxLevelReached >= 2 ? atlas.createSprite("Dots2") : atlas.createSprite("Dotsr2");
//		level2.setScale(.25f);
//		level2.setCenter(camera.viewportWidth*.2f, camera.viewportHeight*.5f);
//		levels.add(level2);
//
//		level3 = state.maxLevelReached >= 3 ? atlas.createSprite("Dots3") : atlas.createSprite("Dotsr3");
//		level3.setScale(.25f);
//		level3.setCenter(camera.viewportWidth*.4f, camera.viewportHeight*.4f);
//		levels.add(level3);
//		
//		level4 = state.maxLevelReached >= 4 ? atlas.createSprite("Dots4") : atlas.createSprite("Dotsr4");
//		level4.setScale(.25f);
//		level4.setCenter(camera.viewportWidth*.6f, camera.viewportHeight*.75f);
//		levels.add(level4);
//		
//		level5 = state.maxLevelReached >= 4 ? atlas.createSprite("Dots4") : atlas.createSprite("Dotsr4");
//		level5.setScale(.25f);
//		level5.setCenter(camera.viewportWidth*.7f, camera.viewportHeight*.75f);
//		levels.add(level5);
//		
//		level6 = state.maxLevelReached >= 4 ? atlas.createSprite("Dots4") : atlas.createSprite("Dotsr4");
//		level6.setScale(.25f);
//		level6.setCenter(camera.viewportWidth*.8f, camera.viewportHeight*.75f);
//		levels.add(level6);
//		
//		level7 = state.maxLevelReached >= 4 ? atlas.createSprite("Dots4") : atlas.createSprite("Dotsr4");
//		level7.setScale(.25f);
//		level7.setCenter(camera.viewportWidth*.9f, camera.viewportHeight*.75f);
//		levels.add(level7);
		
		Container<ImageButton> container = new Container<ImageButton>();
		
		Image backImage = new Image(backAtlas.findRegion("galaxy_unlocked", 1));
		backButton = new ImageButton(backImage.getDrawable());
		backButton.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				controller.levelSelectBackSelected();
			}
			
		});			
	
		//backButton.getImage().scaleBy(-.5f);
		//backButton.setPosition(0, stage.getViewport().getScreenHeight() - backButton.getImage().getHeight() );
		
		stage.addActor(container);
		Gdx.input.setInputProcessor(stage);
		container.setFillParent(true);
		container.setActor(backButton);
		container.top();
		container.left();

		
		Sprite currentLevelSprite = levels.get(currentSelection-1);
		ship = new ShipActor(currentLevelSprite.getX() + currentLevelSprite.getWidth()*.5f, currentLevelSprite.getY() + .75f*currentLevelSprite.getHeight());
		ship.setScale(.5f);
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
	    for (Sprite level : levels) {
	    	level.draw(game.batch);
	    }
	    
	    ship.act(delta);
	    ship.draw(game.batch, .5f);
	    game.batch.end();
	    
	    touchInput.set(Gdx.input.getX(), Gdx.input.getY(), 0);
	    camera.unproject(touchInput);
	    
	    for (int i = 0; i < levels.size(); i++) {
	    	if (Gdx.input.justTouched() && levels.get(i).getBoundingRectangle().contains(touchInput.x, touchInput.y) && i+1 <= maxLevelReached) {
	    		if (i == currentSelection-1) {
	    			
	    			// only allow level select if ship is done moving
	    			if (ship.getActions().size == 0) {
	    				controller.levelSelected(galaxy, currentSelection);
		    			return;
	    			}
	   
	    		} else {
	    			System.out.println(maxLevelReached);
	    		    System.out.println("currentSelection = " + currentSelection);

	    			currentSelection = i + 1;
	    			
	    			MoveToAction moveAction = new MoveToAction();
	    			moveAction.setDuration(SHIP_ANIM_DUR);
	    			moveAction.setPosition(levels.get(i).getX() + levels.get(i).getWidth()*.5f, levels.get(i).getY() + .75f*levels.get(i).getHeight());
	    			ship.addAction(moveAction);
	    			//ship.setCenter(levels.get(i).getX() + levels.get(i).getWidth()*.5f, levels.get(i).getY() + .75f*levels.get(i).getHeight());
	    		}
	    	}
	    }
	    
	    stage.act();
	    stage.draw();
		
	}
	
	public class ShipActor extends Actor {
		private Sprite sprite = atlas.createSprite("menu_ship");
		
		public ShipActor(float x, float y) {
			sprite.setScale(.5f);
			this.setPosition(x, y);
		}
		
		@Override
		public void draw(Batch batch, float alpha) {
			sprite.setCenter(getX(), getY());
			sprite.draw(batch);
		}
	}
	

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
		//backButton.setPosition(0, stage.getHeight() - backButton.getImage().getHeight() );

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
