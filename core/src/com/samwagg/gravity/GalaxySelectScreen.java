package com.samwagg.gravity;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.samwagg.gravity.controller.GravityGameController;

public class GalaxySelectScreen implements Screen {

	GravityGameController controller;
	
	private final TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("galaxy_unlocked_pack.atlas"));
	Array<Sprite> galaxySprites;
	
	List<Integer> unlockedGalaxies;
	
	Stage stage;
	Container<ScrollPane> container;
	ScrollPane pane;;
	HorizontalGroup group;

	
	
	public GalaxySelectScreen(GravityGameController controller, List<Integer> unlockedGalaxies) {
		this.unlockedGalaxies = unlockedGalaxies;
		this.controller = controller;
	}
	
	@Override
	public void show() {
				

	    stage = new Stage(new ExtendViewport(1000,600));
	    Gdx.input.setInputProcessor(stage);
		group = new HorizontalGroup();
	    pane = new ScrollPane(group);
		container = new Container<ScrollPane>();

		
		
		int i = 1;
		
		AtlasRegion galaxyRegion = atlas.findRegion("galaxy_unlocked",i);

		while (galaxyRegion != null) {
			ImageButton button = new ImageButton(new Image(galaxyRegion).getDrawable());
          
			final int buttonLevel = i;
			
			button.addListener(new ClickListener() {
	
				@Override
				public void clicked(InputEvent event, float x, float y) {
					controller.galaxySelected(buttonLevel);
				}
				
			});
			
			group.addActor(button);
			
			//button.setPosition((float) Math.random() * stage.getViewport().getScreenWidth(), (float) Math.random() * stage.getViewport().getScreenHeight());
			//button.getImage().setOrigin(Align.center);
			
			System.out.println("Adding button");
			
			i++;					
			galaxyRegion = atlas.findRegion("galaxy_unlocked", i);
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
		atlas.dispose();
	}

}
