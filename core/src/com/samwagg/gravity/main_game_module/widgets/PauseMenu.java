package com.samwagg.gravity.main_game_module.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PauseMenu {
	
	private PauseMenuListener listener;

	private Stage stage;
	private Container<HorizontalGroup> container;
	


	public PauseMenu(Viewport stageViewport) {
		stage = new Stage(stageViewport);

		container = new Container<HorizontalGroup>();
		Gdx.input.setInputProcessor(stage);

		container.setFillParent(true);
		HorizontalGroup group = new HorizontalGroup();
		
		ImageButton resumeButton = new ImageButton(new Image(new Texture(Gdx.files.internal("resume.png"))).getDrawable());
		ImageButton restartButton = new ImageButton(new Image(new Texture(Gdx.files.internal("restart.png"))).getDrawable());
		ImageButton menuButton = new ImageButton(new Image(new Texture(Gdx.files.internal("menu.png"))).getDrawable());
		
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
				listener.onMainMenuClicked();
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
	}
	
	public Stage getStage() {
		return stage;
	}

	public void registerPauseMenuListener(PauseMenuListener listener) {
		this.listener = listener;
	}
}
