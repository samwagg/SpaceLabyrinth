package com.samwagg.gravity.main_game_module.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.samwagg.gravity.GravityGame;
import com.samwagg.gravity.main_game_module.Map;
import com.samwagg.gravity.main_game_module.game.game_objects.*;
import com.samwagg.gravity.main_game_module.game.view_objects.ExplosionDrawer;
import com.samwagg.gravity.main_game_module.widgets.PauseMenuListener;
import com.samwagg.gravity.main_game_module.widgets.VectorSetter;
import com.samwagg.gravity.main_game_module.widgets.PauseMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * View component in model-view-controller design Responsible for rendering a GravityGameModel instance
 */
public class GravityGameScreen implements Screen, MainGameView, PauseMenuListener {

    private final static boolean DEBUG_RENDER = false;

    private Box2DDebugRenderer debugRenderer;

    private OrthographicCamera camera;
    private OrthographicCamera staticCamera;
    private VectorSetter vSetter;
    private Map map;

    private Texture background;
    private TextureRegion backgroundReg;
    private TiledDrawable tiledBackground;
    private TextureRegion arrowUnlitTex;
    private TextureRegion arrowLitTex;
    private TextureRegion shipTex;
    private TextureRegion enemyShipUnlitTex;
    private TextureRegion enemyShipLitTex;
    private TextureRegion wallTex;
    private TiledDrawable tiledWall;
    private Label countDownLabel;
    private Texture optionsTex;
    private ExplosionDrawer expDrawer;

    private float shipRotation;
    private java.util.Map<AICharacter, Float> aiToRotation;

    private Stage stage;
    private Table table;

    private float countDown = 3;

    private Skin skin;

    private boolean displayOptionsMenu;

    private boolean restart;

    private ExtendViewport extViewport;

    private PauseMenu pauseMenu;

    private Sound collisionSound;
    private Sound explodeSound;
    private Music music;

    private GravityGameModel model;
    private MainGameViewListener listener;
    private final GravityGame game;

    private static final float PHYS_SCALE = .05f;

    /**
     * @param model the model to represent graphically
     * @param game
     */
    public GravityGameScreen(ViewResources resources, GravityGameModel model, GravityGame game) {

        this.game = game;
        this.model = model;

        Gdx.graphics.setVSync(true);

        debugRenderer = new Box2DDebugRenderer();

        arrowLitTex = resources.getArrowLit();
        arrowUnlitTex = resources.getArrowUnlit();
        shipTex = resources.getShip();
        enemyShipUnlitTex = resources.getEnemyUnlit();
        enemyShipLitTex = resources.getEnemyLit();
        wallTex = resources.getWall();
        tiledWall = new TiledDrawable(wallTex);
        expDrawer = new ExplosionDrawer();

        shipRotation = 0;
        aiToRotation = new HashMap<AICharacter, Float>();
        for (AICharacter enemy : model.getEnemies()) {
            aiToRotation.put(enemy, 0f);
        }

        music = Gdx.audio.newMusic(Gdx.files.internal("keith.mp3"));
        music.setVolume(.2f);
        music.setLooping(true);
        music.play();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1000, 600);
        extViewport = new ExtendViewport(1000, 600, camera);

        staticCamera = new OrthographicCamera();
        staticCamera.setToOrtho(false, 960, 576);

        vSetter = new VectorSetter(staticCamera);

        collisionSound = Gdx.audio.newSound(Gdx.files.internal("muf_exp.wav"));
        explodeSound = Gdx.audio.newSound(Gdx.files.internal("DeathFlash.mp3"));

        background = new Texture(Gdx.files.internal("Space-02.png"));
        background.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        //ScalingViewport viewport = new ScalingViewport(Scaling.fill,800,480);
        Viewport stageViewport = new ExtendViewport(1800, 900);
        stage = new Stage(stageViewport);

        Gdx.input.setInputProcessor(vSetter.getInputProcessor());

        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        table.setSkin(skin);

        pauseMenu = new PauseMenu(stageViewport);
        pauseMenu.registerPauseMenuListener(this);
        displayOptionsMenu = false;

        countDownLabel = new Label(Integer.toString((int) countDown), skin);
        countDownLabel.setHeight(500);
        countDownLabel.setWidth(500);
        countDownLabel.setFontScale(1.5f);
        table.add(countDownLabel);

        optionsTex = new Texture(Gdx.files.internal("options.png"));

        backgroundReg = new TextureRegion(background, background.getWidth(), background.getHeight());
        tiledBackground = new TiledDrawable(backgroundReg);

        camera.position.x = model.getCharacter().getX() / PHYS_SCALE;
        camera.position.y = -model.getCharacter().getY() / PHYS_SCALE;
        camera.update();
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, .3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        tiledBackground.draw(game.batch, -camera.viewportWidth, -model.getWorldHeight()/PHYS_SCALE- camera.viewportHeight, model.getWorldWidth()/PHYS_SCALE + 2 * camera.viewportWidth, model.getWorldHeight()/PHYS_SCALE + 2 * camera.viewportHeight);

        for (GravField field : model.getGravFields()) {
            if (field.isLit()) {
                game.batch.draw(arrowLitTex, physToScreen(field.getX(), field.getWidth()), physToScreen(field.getY(), field.getHeight()),
                        field.getWidth()/PHYS_SCALE/2, field.getHeight()/PHYS_SCALE/2,
                        field.getWidth()/PHYS_SCALE, field.getHeight()/PHYS_SCALE, 1, 1, field.getRotation());
            }
            else {
                game.batch.draw(arrowUnlitTex, physToScreen(field.getX(), field.getWidth()), physToScreen(field.getY(), field.getHeight()),
                        field.getWidth()/PHYS_SCALE/2, field.getHeight()/PHYS_SCALE/2,
                        field.getWidth()/PHYS_SCALE, field.getHeight()/PHYS_SCALE, 1, 1, field.getRotation());
            }
        }

        if (!model.isShipGone()) {
            float screenWidth = model.getCharacter().getWidth()/PHYS_SCALE;
            float screenHeight = model.getCharacter().getHeight()/PHYS_SCALE;
            game.batch.draw(shipTex, physToScreen(model.getCharacter().getX(), model.getCharacter().getWidth()),
                    physToScreen(model.getCharacter().getY(), model.getCharacter().getHeight()),
                    screenWidth/2, screenHeight/2, screenWidth, screenHeight, 1, 1, shipRotation);
            shipRotation += 1f;
        }

        for (AICharacter enemy : model.getEnemies()) {

            TextureRegion shipTexture;
            if (enemy.isPursuing()) {
                aiToRotation.put(enemy, aiToRotation.remove(enemy) + 1);
                shipTexture = enemyShipLitTex;
            }
            else {
                shipTexture = enemyShipUnlitTex;
            }

            game.batch.draw(shipTexture, physToScreen(enemy.getX(), enemy.getWidth()), physToScreen(enemy.getY(), enemy.getHeight()),
                    0, 0, enemy.getWidth()/PHYS_SCALE/2, enemy.getHeight()/PHYS_SCALE/2, 1, 1, aiToRotation.get(enemy));
        }

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);


        for (Wall wall : model.getWalls()) {
            drawWall(wall);
        }
        for (Wall wall : model.getMovingWalls()) {
            drawWall(wall);
        }

        Gdx.gl.glDisable(GL20.GL_BLEND);

        for (Explosion exp : model.getExplosions()) {
            expDrawer.drawFrame(game.batch, exp, PHYS_SCALE);
        }

        game.batch.setProjectionMatrix(staticCamera.combined);
        game.batch.draw(optionsTex, staticCamera.viewportWidth - optionsTex.getWidth(), staticCamera.viewportHeight - optionsTex.getHeight());

        game.batch.end();

        if (model.getCountDownToStart() > 0 && !displayOptionsMenu && !pauseMenu.isTransitioning()) {
            stage.act(delta);
            stage.draw();
            countDownLabel.setText(Integer.toString((int) model.getCountDownToStart() + 1));
        }
        else if (displayOptionsMenu || pauseMenu.isTransitioning()) {
            if (model.getCountDownToStart() > 0) stage.draw();
            vSetter.onInputTurnedOff();
            pauseMenu.getStage().act();
            pauseMenu.getStage().draw();
        }
        else {
            Gdx.input.setInputProcessor(vSetter.getInputProcessor());
            if (listener != null) listener.vSetterState(vSetter.getMagnitude(), vSetter.getXComponent(), vSetter.getYComponent());
        }

        handleInput();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        game.shapeRenderer.begin();
        game.shapeRenderer.setProjectionMatrix(staticCamera.combined);
        if (!displayOptionsMenu) renderVSetter();
        renderHealthBar();
        game.shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        float force = model.getWallCrash();
        if (model.getExplosionEvent()) explodeSound.play(.5f);
        else if (force != 0) {
            onWallCollision(force);
        }

        model.doLogic(delta);

        camera.position.set(model.getCharacter().getX()/PHYS_SCALE, model.getCharacter().getY()/PHYS_SCALE, 0);
        if (!model.isShipGone()) camera.update();

        if (DEBUG_RENDER) {
            debugRenderer.render(model.getBox2dWorld(), staticCamera.combined);
        }
    }

    public void displayOptionsMenu(boolean display) {
        if (display) {
            Gdx.input.setInputProcessor(pauseMenu.getStage());
            if (!displayOptionsMenu) {
                pauseMenu.startInTransition();
            }
        }
        else {
            Gdx.input.setInputProcessor(vSetter.getInputProcessor());
            if (displayOptionsMenu) {
                pauseMenu.startOutTransition();
            }
        }
        displayOptionsMenu = display;
    }

    public boolean isOptionsMenuDisplayed() {
        return displayOptionsMenu;
    }

    public void dispose() {
        this.background.dispose();
        music.dispose();
        collisionSound.dispose();
        System.out.println("disposed");
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {
        extViewport.update(width, height);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        vSetter.onInputTurnedOff();
        System.out.println("hiding");
    }

    @Override
    public void registerUserInputListener(MainGameViewListener listener) {
        this.listener = listener;
    }

    @Override
    public void onResumeClicked() {
        System.out.println("resume clicked"); listener.resumeClicked();
    }

    @Override
    public void onRestartClicked() {
        listener.restartClicked();
    }

    @Override
    public void onMainMenuClicked() {
        System.out.println("here!"); listener.mainMenuClicked();
    }

    private float physToScreen(float x, float dimen) {
        return (x-dimen/2)/PHYS_SCALE;
    }

    private void renderVSetter() {
        vSetter.render(game.shapeRenderer, camera);
    }

    private void renderHealthBar() {
        game.shapeRenderer.setColor(1 - 1 / (model.getStartScore() / model.getScore()), 0f, model.getScore() / model.getStartScore(), .6f);
        game.shapeRenderer.set(ShapeType.Filled);
        game.shapeRenderer.rect(0, 0, (model.getScore() / model.getStartScore()) * staticCamera.viewportWidth, 20);
    }

    /*
     * Handle input that GavityGameScreen listens for directly. As this code is refactored, this method should eventually
     * go away in favor of separate UI objects with their own listening.
     */
    private void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            staticCamera.unproject(touchPos);

            if (touchPos.x > staticCamera.viewportWidth - optionsTex.getWidth() && touchPos.y > staticCamera.viewportHeight - optionsTex.getHeight()) {
                if (listener != null) listener.optionsClicked();
            }
        }
    }

    private void onWallCollision(float force) {

        float volume;
        if (force < 100) volume = .1f;
        else if (force < 1000) volume = .2f;
        else if (force < 3000) volume = .5f;
        else if (force < 5000) volume = .7f;
        else if (force < 8000) volume = .85f;
        else volume = 1;

        collisionSound.play(volume);
        System.out.println(volume);
        System.out.println(force);
//        collisionSound.play();
    }

    private void drawWall(Wall wall) {

        float screenX = physToScreen(wall.getX(), wall.getWidth());
        float screenY = physToScreen(wall.getY(), wall.getHeight());
        float screenWidth = wall.getWidth() / PHYS_SCALE;
        float screenHeight = wall.getHeight() / PHYS_SCALE;

        BoundingBox testBox = new BoundingBox(new Vector3(screenX, screenY, 0),
                new Vector3(screenX + screenWidth, screenY + screenHeight, 0));
        if (camera.frustum.boundsInFrustum(testBox)) {
            int xStart = (int) screenX;
            int yStart = (int) screenY;
            int xEnd = (int) (screenX + screenWidth);
            int yEnd = (int) (screenY + screenHeight);

            // Due to graphical artifacts at the border of the wall textures that I was unable to completely eliminate
            // by changes in filtering, padding, etc, walls are drawn by overlaying 4 tiled patterns, each of which
            // individually have no bordering wall tiles.
            tiledWall.draw(game.batch, xStart - 64, yStart + 64, screenWidth % 256 == 0 ? screenWidth + 64 : screenWidth + 128, screenHeight % 256 == 0 ? screenHeight : screenHeight - 64);
            tiledWall.draw(game.batch, xStart + 64, yStart + 64, screenWidth % 256 == 0 ? screenWidth : screenWidth - 64, screenHeight % 256 == 0 ? screenHeight : screenHeight - 64);
            tiledWall.draw(game.batch, xStart - 64, yStart - 64, screenWidth % 256 == 0 ? screenWidth + 64 : screenWidth + 128, screenHeight % 256 == 0 ? screenHeight + 64 : screenHeight + 128);
            tiledWall.draw(game.batch, xStart + 64, yStart - 64, screenWidth % 256 == 0 ? screenWidth : screenWidth - 64, screenHeight % 256 == 0 ? screenHeight + 64 : screenHeight + 128);
        }
    }
}
