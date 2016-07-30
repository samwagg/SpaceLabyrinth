package com.samwagg.gravity.menus.galaxy_menu;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class GalaxySelectScreen implements Screen, GalaxySelector {


	GalaxySelectCallback callbackObject;

	private final TextureAtlas unlockedAtlas = new TextureAtlas(Gdx.files.internal("galaxy_unlocked_pack.atlas"));
	private final TextureAtlas lockedAtlas = new TextureAtlas(Gdx.files.internal("galaxy_locked_pack.atlas"));

	Array<Sprite> galaxySprites;
	
	List<Integer> unlockedGalaxies;
	
	Stage stage;
	Container<ScrollPane> container;
	ScrollPane pane;;
	HorizontalGroup group;

	
	
	public GalaxySelectScreen(List<Integer> unlockedGalaxies) {
		this.unlockedGalaxies = unlockedGalaxies;
	}
	
	@Override
	public void show() {
				

	    stage = new Stage(new ExtendViewport(1000,600));
	    Gdx.input.setInputProcessor(stage);
		group = new HorizontalGroup();
	    pane = new ScrollPane(group);
		container = new Container<ScrollPane>();

		int i = 1;
		
		AtlasRegion galaxyRegion = 	unlockedGalaxies.contains(i)? 
									unlockedAtlas.findRegion("galaxy_unlocked", i) :
									lockedAtlas.findRegion("galaxy_locked", i);

		while (galaxyRegion != null) {
			ImageButton button = new ImageButton(new Image(galaxyRegion).getDrawable());
          
			final int buttonLevel = i;
			
			button.addListener(new ClickListener() {
	
				@Override
				public void clicked(InputEvent event, float x, float y) {
					if (unlockedGalaxies.contains(buttonLevel) && callbackObject != null) callbackObject.galaxySelected(buttonLevel);
				}
			});
			
			group.addActor(button);
			
			//button.setPosition((float) Math.random() * stage.getViewport().getScreenWidth(), (float) Math.random() * stage.getViewport().getScreenHeight());
			//button.getImage().setOrigin(Align.center);
			
			System.out.println("Adding button");
			
			i++;	
			
			galaxyRegion = 	unlockedGalaxies.contains(i)? 
							unlockedAtlas.findRegion("galaxy_unlocked", i) :
							lockedAtlas.findRegion("galaxy_locked", i);
		}
		
		stage.addActor(container);
		container.setActor(pane);
		container.setFillParent(true);

		//pane.setFillParent(true);
		//pane.setFlickScroll(true);
		//container.setBounds(0, 0, stage.getWidth(), stage.getHeight());
//		container.setFillParent(true);
//		container.setActor(group);
//		container.center();
		//group.setBounds(0, 0, container.getWidth(), container.getHeight());
		//group.setWidth(stage.getWidth());
		//group.center();
		//group.center();
		//		group.align(Align.right);
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    
	    
	    
		stage.act();
		stage.draw();
		
		
		
//		for (Actor actor: stage.getActors()) {
//			
//			if (((ImageButton) actor).getActions().size == 0) {
//				RotateByAction rotateAction = new RotateByAction();
//				rotateAction.setDuration(10);
//				rotateAction.setAmount(180);
//				((ImageButton) actor).getImage().addAction(rotateAction);
//			}
//
//			
//			if (actor.getActions().size == 0) {
//				System.out.println(actor.getActions().size);
//				MoveToAction moveAction = new MoveToAction();
//				moveAction.setDuration(10);
//				moveAction.setPosition( (float) Math.random() * stage.getWidth(), (float) Math.random() * stage.getViewport().getScreenHeight());
//				actor.addAction(moveAction);
//			}
//			
//
//
//		}
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {		
	}

	@Override
	public void resume() {		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void dispose() {
		stage.dispose();
		lockedAtlas.dispose();
		unlockedAtlas.dispose();
	}

	@Override
	public void registerGalaxySelectCallback(GalaxySelectCallback callbackObject) {
		this.callbackObject = callbackObject;
	}
}
