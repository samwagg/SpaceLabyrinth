package com.samwagg.gravity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.samwagg.gravity.controller.GravityGameController;
import com.samwagg.gravity.controller.MenusController;
import com.samwagg.gravity.objects.GameState;


public class GravityGame extends Game {

	public SpriteBatch batch;
	public ShapeRenderer shapeRenderer;
	public BitmapFont font;
	public TextureAtlas atlas;
	
	private GravityGameController controller;
	
	
	public static final int CONTACT_DECREMENT = 100;

	private GameState gameState;
	
	@Override
	public void create() {
		
		controller = new MenusController(this);
	
		FileHandle file = Gdx.files.local("gamestate.bin");
		
		InputStream stream = null;
		if (file.exists()) {
			System.out.println("here");
			stream = file.read();
			ObjectInputStream objStream = null;
			
			try {
				objStream = new ObjectInputStream(stream);

				gameState = (GameState) objStream.readObject();
			} catch (InvalidClassException e) {
				gameState = new GameState();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					objStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} else {
			gameState = new GameState();
		}
		
		if (gameState.hs.highScores.length != Constants.N_LEVELS) {
			gameState = new GameState();
		}

		System.out.println(gameState.maxLevelReached);
			
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);
		font = new BitmapFont();
		
		Constants.initConstants();
		
		
		
		setScreen(new MainMenu(controller, this));
	}
	
	public void setScreenAndDispose(Screen screen, Screen oldScreen) {
		setScreen(screen);
		
	}
	
	public void resetGameProgress() {
		gameState = new GameState();
	}
	
	public void mainMenuStartPressed(Screen oldScreen) {
		//setScreen(new LevelSelectScreen(this, gameState));
	}
	
	public void render() {
		super.render();
	}
	
	public void dispose() {
		batch.dispose();
		shapeRenderer.dispose();
	}
	
	public GameState getGameState() {
		return this.gameState;
	}
	
	public int readHighScore(int level) {
		return gameState.hs.highScores[level-1];
	}
	
	public void writeHighScore(int level, int score) {
		
		gameState.currentLevel = level + 1;
		
		if (gameState.maxLevelReached < level + 1) {
			gameState.maxLevelReached = level + 1;
		}
		
		if (gameState.hs.highScores[level-1] < score) { 	
			gameState.hs.highScores[level-1] = score;
			FileHandle file = Gdx.files.local("gamestate.bin");
			OutputStream stream = file.write(false, 100000);
			ObjectOutputStream objStream = null;
			try {
				objStream = new ObjectOutputStream(stream);
				objStream.writeObject(gameState);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					stream.close();
					objStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}
	}

}
