package com.samwagg.gravity.main_game_module.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PauseMenu {
	
	private PauseMenuListener listener;

	private Stage stage;
	private Container<HorizontalGroup> container;
	


	public PauseMenu(Viewport stageViewport) {
		stage = new Stage(stageViewport);

		container = new Container<HorizontalGroup>();

		container.setFillParent(true);
		HorizontalGroup group = new HorizontalGroup();

		TextureAtlas atlas = new TextureAtlas("menu_buttons_pack.atlas");

		ImageButton resumeButton = new ImageButton(new TextureRegionDrawable(atlas.findRegion("resume")));
		ImageButton restartButton = new ImageButton(new TextureRegionDrawable(atlas.findRegion("restart")));
		ImageButton menuButton = new ImageButton(new TextureRegionDrawable(atlas.findRegion("menu")));

////		ImageButton resumeButton = new ImageButton(new Image(new Texture(Gdx.files.internal("resume.png"))).getDrawable());
//		ImageButton restartButton = new ImageButton(new Image(new Texture(Gdx.files.internal("restart.png"))).getDrawable());
//		ImageButton menuButton = new ImageButton(new Image(new Texture(Gdx.files.internal("menu.png"))).getDrawable());
		
		resumeButton.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				listener.onResumeClicked();
			}
			
		});
		
		restartButton.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				listener.onRestartClicked();
			}
			
		});
		
		menuButton.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("AHHH!"); listener.onMainMenuClicked();
			}
			
		});
		
		group.addActor(resumeButton);
		resumeButton.padRight(20);
		group.addActor(restartButton);
		restartButton.padRight(20);

		group.addActor(menuButton);
		menuButton.padRight(20);

		container.setActor(group);
		container.bottom();

		stage.clear();
		stage.addActor(container);
//		container.setSize(stage.getWidth(), stage.getHeight());
		System.out.println("container height = "+container.getPrefHeight());
		container.setPosition(0, -container.getPrefHeight());

	}

	public boolean isTransitioning() {
		return container.getActions().size > 0;
	}

	public void startInTransition() {
		MoveToAction action = new MoveToAction();
		action.setPosition(0,0);
		action.setDuration(.25f);
		container.addAction(action);
	}

	public void startOutTransition() {
		MoveToAction action = new MoveToAction();
		action.setPosition(0, -container.getPrefHeight());
		action.setDuration(.25f);
		container.addAction(action);
	}

	public Stage getStage() {
		return stage;
	}

	public void registerPauseMenuListener(PauseMenuListener listener) {
		this.listener = listener;
	}
}
