package com.samwagg.gravity.main_game_module.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.samwagg.gravity.GravityGame;
import com.samwagg.gravity.main_game_module.Map;
import com.samwagg.gravity.main_game_module.widgets.PauseMenuListener;
import com.samwagg.gravity.main_game_module.widgets.VectorSetter;
import com.samwagg.gravity.main_game_module.game.game_objects.Explosion64;
import com.samwagg.gravity.main_game_module.game.game_objects.GravField;
import com.samwagg.gravity.main_game_module.game.game_objects.MovingWall;
import com.samwagg.gravity.main_game_module.game.game_objects.Wall;
import com.samwagg.gravity.main_game_module.widgets.PauseMenu;

public class GravityGameScreen implements Screen, MainGameView, PauseMenuListener {


    private OrthographicCamera camera;
    private OrthographicCamera staticCamera;
    private VectorSetter vSetter;
    private Map map;

    private Texture background;
    private TextureRegion backgroundReg;
    private TiledDrawable tiledBackground;
    private Label countDownLabel;

    private Texture optionsTex;

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


//    public final float PHYS_SCALE = .05f;
//    public final int TILE_SIZE = 64;
//    public final int N_LEVELS = 7;

//    public final TextureAtlas ATLAS;
//
//    public final Sprite CHAR_SPRITE;
//    public final Sprite ARROW_LIT_SPRITE;
//    public final Sprite ARROW_UNLIT_SPRITE;
//    public final TextureAtlas.AtlasRegion WALL_REGION;

    /**
     *
     * @param model
     * @param game
     */
    public GravityGameScreen(GravityGameModel model, final GravityGame game) {
//
//        ATLAS = new TextureAtlas("pack.atlas");
//        CHAR_SPRITE = ATLAS.createSprite("Ship");
//        WALL_REGION = ATLAS.findRegion("wall");
//
//        ARROW_LIT_SPRITE = ATLAS.createSprite("arrow_lit");
//        ARROW_LIT_SPRITE.setSize(TILE_SIZE * 2, TILE_SIZE * 2);
//        ARROW_LIT_SPRITE.setOriginCenter();
//
//        ARROW_UNLIT_SPRITE = ATLAS.createSprite("arrow_unlit");
//        ARROW_UNLIT_SPRITE.setSize(TILE_SIZE * 2, TILE_SIZE * 2);
//        ARROW_UNLIT_SPRITE.setOriginCenter();

        this.game = game;
        this.model = model;

        Gdx.graphics.setVSync(true);

        music = Gdx.audio.newMusic(Gdx.files.internal("keith.mp3"));
        music.setVolume(.1f);
        music.play();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1000, 600);
        extViewport = new ExtendViewport(1000, 600, camera);

        staticCamera = new OrthographicCamera();
        staticCamera.setToOrtho(false, 960, 576);

        vSetter = new VectorSetter(staticCamera);

//		multiPlex = new InputMultiplexer();
//		multiPlex.addProcessor(vSetter.getInputProcessor());

        collisionSound = Gdx.audio.newSound(Gdx.files.internal("muf_exp.wav"));
        explodeSound = Gdx.audio.newSound(Gdx.files.internal("DeathFlash.mp3"));

        background = new Texture(Gdx.files.internal("Space-02.png"));
        background.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        //ScalingViewport viewport = new ScalingViewport(Scaling.fill,800,480);
        Viewport stageViewport = new ExtendViewport(1800, 900);
        stage = new Stage(stageViewport);

//        Gdx.input.setInputProcessor(stage);

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

        camera.position.x = model.getCharacter().getScreenX();
        camera.position.y = model.getCharacter().getScreenY();
        camera.update();
    }



    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, .3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        tiledBackground.draw(game.batch, -camera.viewportWidth, -model.getWorldHeight()- camera.viewportHeight, model.getWorldWidth() + 2 * camera.viewportWidth, model.getWorldHeight() + 2 * camera.viewportHeight);

        for (GravField field : model.getGravFields()) {
            field.getSprite().draw(game.batch);
        }

        if (!model.isShipGone()) {
            model.getCharacter().getSprite().draw(game.batch);
            model.getCharacter().getSprite().rotate(.2f);
        }

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        for (Wall wall : model.getWalls()) {
            wall.draw(game.batch, camera);
        }

        for (MovingWall wall : model.getMovingWalls()) {
            wall.draw(game.batch, camera);
        }

        Gdx.gl.glDisable(GL20.GL_BLEND);

        for (Explosion64 exp : model.getExplosions()) {
            exp.drawNextFrame(game.batch);

        }

        game.batch.setProjectionMatrix(staticCamera.combined);
        game.batch.draw(optionsTex, staticCamera.viewportWidth - optionsTex.getWidth(), staticCamera.viewportHeight - optionsTex.getHeight());

        game.batch.end();

        if (model.getCountDownToStart() > 0 && !displayOptionsMenu) {
            stage.act(delta);
            stage.draw();
            countDownLabel.setText(Integer.toString((int) model.getCountDownToStart() + 1));
        }
        else if (displayOptionsMenu) {
            if (model.getCountDownToStart() > 0) stage.draw();
            vSetter.onInputTurnedOff();
            pauseMenu.getStage().act();
            pauseMenu.getStage().draw();
        }
        else {
            Gdx.input.setInputProcessor(vSetter.getInputProcessor());
            if (listener != null) listener.vSetterState(vSetter.getMagnitude(), vSetter.getXComponent(), vSetter.getYComponent());

        }

//        if (levelFinished) {
//            displayDialog = true;
//            controller.levComplete((int) score);
//        }

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

        camera.position.set(model.getCharacter().getScreenX(), model.getCharacter().getScreenY(), 0);
        if (!model.isShipGone()) camera.update();
    }

    public void startCountdown() {

    }

    public void displayOptionsMenu(boolean display) {
        displayOptionsMenu = display;
        if (display) Gdx.input.setInputProcessor(pauseMenu.getStage());
    }

    public boolean isOptionsMenuDisplayed() {
        return displayOptionsMenu;
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
}
