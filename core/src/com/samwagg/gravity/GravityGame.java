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
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
//import com.samwagg.gravity.controller.LevelDownloadController;
import com.samwagg.gravity.menus.main_menu.MainScreen;


public class GravityGame extends Game {

	public SpriteBatch batch;
	public ShapeRenderer shapeRenderer;
	public BitmapFont font;
	public TextureAtlas atlas;
	
	private Navigator controller;
	private Music music;
	
	public static final float CONTACT_DECREMENT = .1f;

	private GameState gameState;

	public Constants constants;

//	private LevelDownloadController downloadController;

//	public GravityGame(LevelDownloadController controller) {
//		downloadController = controller;
//	}

	@Override
	public void create() {

//		downloadController.launchLevelAquisitionModule();

		constants = new Constants();
		controller = new Navigator(this);

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
				gameState = new GameState(constants);
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
			gameState = new GameState(constants);
		}

		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);
		font = new BitmapFont();

		music = Gdx.audio.newMusic(Gdx.files.internal("alliance.mp3"));
		music.play();

		controller.startGame();
	}

	public void playMenuMusic() {
		music.setLooping(true);
		music.play();
	}

	public void stopMenuMusic() {
		music.stop();
	}
	
	public void setScreenAndDispose(Screen screen, Screen oldScreen) {
		setScreen(screen);
		
	}
	
	public void resetGameProgress() {
		gameState = new GameState(constants);
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
	
	public void updateGameState(int galaxy, int level, int score) {
		
		gameState.currentLevelByGalaxy.set(galaxy-1, level);

		System.out.println("galaxy = " + galaxy);
		System.out.println("level = " + level);

		if (gameState.maxLevelReachedByGalaxy.get(galaxy-1) < level + 1) {
			gameState.maxLevelReachedByGalaxy.set(galaxy-1, level + 1);
		}

		System.out.println("maxLevel = " + gameState.maxLevelReachedByGalaxy.get(0) + " and galaxiesUnlocked = " + gameState.galaxiesUnlocked.contains(2));
		if (gameState.maxLevelReachedByGalaxy.get(0) == 8 && !gameState.galaxiesUnlocked.contains(2)) {
			gameState.galaxiesUnlocked.add(galaxy+1);
			gameState.maxLevelReachedByGalaxy.set(galaxy, 1);
			gameState.currentLevelByGalaxy.set(galaxy, 1);
		}
		
		if (gameState.hs.galaxies.get(galaxy-1)[level-1] < score) { 	
			gameState.hs.galaxies.get(galaxy-1)[level-1] = score;
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
