package com.samwagg.gravity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.samwagg.gravity.objects.GameState;
import com.samwagg.gravity.objects.HighScores;
import com.sun.corba.se.pept.encoding.InputObject;

public class GravityGame extends Game {

	public SpriteBatch batch;
	public ShapeRenderer shapeRenderer;
	public BitmapFont font;
	public TextureAtlas atlas;
	
	
	public static final int CONTACT_DECREMENT = 100;

	private GameState gameState;
	
	@Override
	public void create() {
	
		FileHandle file = Gdx.files.local("gamestate.bin");
		
		InputStream stream = null;
		if (file.exists()) {
			System.out.println("here");
			stream = file.read();
			ObjectInputStream objStream = null;
			
			try {
				objStream = new ObjectInputStream(stream);

				gameState = (GameState) objStream.readObject();
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

		System.out.println(gameState.maxLevelReached);
			
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);
		font = new BitmapFont();
		
		Constants.initConstants();
		
		setScreen(new MainMenu(this));
	}
	
	public void setScreenAndDispose(Screen screen, Screen oldScreen) {
		setScreen(screen);
		
	}
	
	public void resetGameProgress() {
		gameState = new GameState();
	}
	
	public void mainMenuStartPressed(Screen oldScreen) {
		setScreen(new LevelSelectScreen(this, gameState));
	}
	
	public void render() {
		super.render();
	}
	
	public void dispose() {
		batch.dispose();
		shapeRenderer.dispose();
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
