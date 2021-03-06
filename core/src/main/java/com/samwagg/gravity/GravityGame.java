package com.samwagg.gravity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.samwagg.gravity.main_game_module.game.ViewResources;
import com.samwagg.gravity.menus.main_menu.MainScreen;

/**
 * Top level class that provides point of entry and top level services such as saving game progress, playing game music,
 * and setting the game's current screen
 */
public class GravityGame extends Game {

    private static final String GALAXIES_DIRECTORY = "galaxies";
    private static final String GALAXY_DIRECTORY_BASE = "galaxy";

    private static final String RESOURCE_PACK = "pack.atlas";
    private static final String RESOURCE_SHIP = "Ship";
    private static final String RESOURCE_FORCE_LIT = "arrow_lit";
    private static final String RESOURCE_FORCE_UNLIT = "arrow_unlit";
    private static final String RESOURCE_ENEMY_UNLIT = "enemy";
    private static final String RESOURCE_ENEMY_LIT = "enemy_lit";
    private static final String RESOURCE_WALL = "wall";

    private static final String MENU_MUSIC_FILE = "alliance.mp3";

    public SpriteBatch batch;
    public ShapeRenderer shapeRenderer;
    public BitmapFont font;

    private Navigator controller;
    private Music music;

    public static final float CONTACT_DECREMENT = .1f;

    private List<Galaxy> galaxies;
    private Map<String, GalaxyState> galaxyIdToState;

    private GameState gameState;
    private ViewResources gameResources;


    @Override
    public void create() {

        GalaxyReader galaxyReader = new GalaxyReader();
        FileHandle resourcePack = Gdx.files.internal(RESOURCE_PACK);

        controller = new Navigator(this);

        gameResources = new ViewResources(resourcePack, RESOURCE_SHIP, RESOURCE_ENEMY_UNLIT, RESOURCE_ENEMY_LIT,
                RESOURCE_WALL, RESOURCE_FORCE_LIT, RESOURCE_FORCE_UNLIT);

        galaxies = new ArrayList<Galaxy>();
        galaxyIdToState = new HashMap<String, GalaxyState>();
        int i = 0;
        Galaxy galaxy;
        GalaxyState state;
        FileHandle galDir = Gdx.files.internal(GALAXIES_DIRECTORY + "/" + GALAXY_DIRECTORY_BASE + i);
        while (galDir.exists()) {
            try {
                galaxy = galaxyReader.readGalaxy(galDir);
                state = new GalaxyState(galaxy.getGalaxyId());
                if (i == 0) state.changeIsUnlocked(true);
                galaxies.add(galaxy);
                galaxyIdToState.put(galaxy.getGalaxyId(), state);
            } catch (IOException e) {
                // TODO display error message to user if galaxy doesn't load as expected
                e.printStackTrace();
            }
            i++;
            galDir = Gdx.files.internal(GALAXIES_DIRECTORY + "/" + GALAXY_DIRECTORY_BASE + i);
        }

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        font = new BitmapFont();

        music = Gdx.audio.newMusic(Gdx.files.internal(MENU_MUSIC_FILE));
        music.play();

        controller.startGame();
    }

    @Override
    public void render() {
        super.render();
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

    /**
     * dispose all disposable resources managed by GravityGame instance
     */
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
    }

    /**
     * Updates current GameState object to reflect progress of completed level
     *
     * @param galaxy
     * @param level
     * @param score
     */
    public void updateGameState(Galaxy galaxy, int level, int score) {

        GalaxyState state = new GalaxyState(galaxy.getGalaxyId());

        if (state.getHighScore(level) < score) {
            state.changeHighScore(level, score);
        }

        if (level + 2 <= galaxy.getLevels().size()) {
            state.changeCurrentLevel(level + 1);

            if (state.getLevelReached() < level) {
                state.changeLevelReached(level + 1);
            }
        }
        else if (!state.isFinished()) {
            state.changeIsFinished(true);
            unlockNextGalaxy();
        }

        state.persistChanges();
    }

    private List<Galaxy> getGalaxiesByUnlockedStatus(boolean unlocked) {
        System.out.println("Getting galaxies in unlcoekd status " + unlocked);
        GalaxyState state;
        List<Galaxy> selectedGalaxies = new ArrayList<Galaxy>();
        for (Galaxy galaxy : galaxies) {
            state = new GalaxyState(galaxy.getGalaxyId());
            System.out.println("galaxy has state " + state.isUnlocked());
            if (state.isUnlocked() == unlocked) {
                selectedGalaxies.add(galaxy);
            }
        }
        return selectedGalaxies;
    }

    private void unlockNextGalaxy() {
        Galaxy galToUnlock = getUnlockedGalaxies().get(0);
        GalaxyState state = new GalaxyState(galToUnlock.getGalaxyId());
        state.changeIsUnlocked(true);
    }

    public ViewResources getGameResources() {
        return gameResources;
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public List<Galaxy> getGalaxies() {
        return galaxies;
    }

    public List<Galaxy> getUnlockedGalaxies() {
        return getGalaxiesByUnlockedStatus(true);
    }

    public List<Galaxy> getLockedGalaxies() {
        return getGalaxiesByUnlockedStatus(false);
    }

    public int getLevelReached(Galaxy galaxy) {
        return galaxyIdToState.get(galaxy.getGalaxyId()).getLevelReached();
    }

    public int getCurrentLevel(Galaxy galaxy) {
        return galaxyIdToState.get(galaxy.getGalaxyId()).getCurrentLevel();
    }

    public int getHighScore(Galaxy galaxy, int level) {
        return galaxyIdToState.get(galaxy.getGalaxyId()).getHighScore(level);
    }

}
