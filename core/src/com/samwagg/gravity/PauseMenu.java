package com.samwagg.gravity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class PauseMenu {
	
	private Stage stage;
	Container<HorizontalGroup> container;
	
	private PauseMenuReturn currPauseState = PauseMenuReturn.REMAIN;
	private boolean justPaused;
	
	public PauseMenu(Stage stage) {
		this.stage = stage;
		
		justPaused = true;
		
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
				System.out.println("actual resume button pressed");
				resume();
			}
			
		});
		
		restartButton.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				restart();
			}
			
		});
		
		menuButton.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				menu();
				System.out.println("Main menu button pressed");
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
	}
	
	/**
	 * Displays pause menu
	 * 
	 * @param stage the stage in which to display menu
	 * @return false after resume clicked, true otherwise
	 */
	public PauseMenuReturn displayIfPaused() {
		
		if (justPaused) {
			justPaused = false;
			setStage();
		}
		
		stage.act();
		stage.draw();
		return currPauseState;
	}
	
	private void setStage() {
		System.out.println("set stage");
		stage.clear();
		stage.addActor(container);
	}
	
	/**
	 * Must call reset() before resuming game to reset state
	 */
	public void reset() {
		currPauseState = PauseMenuReturn.REMAIN;
		justPaused = true;
		stage.clear();
	}
	
	public static enum PauseMenuReturn {
		RESUME, RESTART, MAIN_MENU, REMAIN
	}
	
	public void resume() {
		currPauseState = PauseMenuReturn.RESUME;
	}
	
	public void restart() {
		currPauseState = PauseMenuReturn.RESTART;		
	}
	
	public void menu() {
		currPauseState = PauseMenuReturn.MAIN_MENU;
	}
}
