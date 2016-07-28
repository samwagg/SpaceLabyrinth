package com.samwagg.gravity.main_game_module;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.samwagg.gravity.GravityGame;
import com.samwagg.gravity.main_game_module.widgets.VectorSetter;
import com.samwagg.gravity.ai.EnemySteeringAgent;
import com.samwagg.gravity.main_game_module.game_objects.Explosion64;
import com.samwagg.gravity.main_game_module.game_objects.FinishSensor;
import com.samwagg.gravity.main_game_module.game_objects.GameCharacter;
import com.samwagg.gravity.main_game_module.game_objects.GravField;
import com.samwagg.gravity.main_game_module.Map.MapFormatException;
import com.samwagg.gravity.main_game_module.game_objects.MovingWall;
import com.samwagg.gravity.main_game_module.game_objects.Wall;
import com.samwagg.gravity.main_game_module.widgets.PauseMenu;

public class GravityGameScreen implements Screen {


    private OrthographicCamera camera;
    private OrthographicCamera staticCamera;
    private VectorSetter vSetter;
    private Map map;

    private final GravityGame game;
    private final GravityGameController controller;


    private Vector2 gravVect;

    private List<GravField> currentGravFields;

    private float score;
    private boolean levelFinished;
    private int level;

    public World WORLD;

    private Texture background;
    private TextureRegion backgroundReg;
    private TiledDrawable tiledBackground;

    private Texture optionsTex;

    private float worldHeight;
    private float worldWidth;

    private boolean shipGone;

    private final float startScore;

    private Stage stage;
    private Table table;

    private float countDown = 3;
    Label countDownLabel;

    private InputMultiplexer multiPlex;

    private Skin skin;

    private boolean displayDialog = true;
    private boolean optionsClicked;

    private boolean restart;

    private ExtendViewport extViewport;

    private PauseMenu pauseMenu;

    private Sound collisionSound;
    private Music music;

    GravityGameModel model;

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
     * @param controller
     * @param game
     * @param galaxy The galaxy number in which the level appears is in
     * @param level The level to load
     */
    public GravityGameScreen(GravityGameController controller, GravityGameModel model, final GravityGame game, int galaxy, int level) {
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
        this.controller = controller;
        this.model = model;

        Gdx.graphics.setVSync(true);


        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1000, 600);
        extViewport = new ExtendViewport(1000, 600, camera);

        staticCamera = new OrthographicCamera();
        staticCamera.setToOrtho(false, 960, 576);

        vSetter = new VectorSetter(staticCamera);

//		multiPlex = new InputMultiplexer();
//		multiPlex.addProcessor(vSetter.getInputProcessor());

        collisionSound = Gdx.audio.newSound(Gdx.files.internal("muf_exp.wav"));

        background = new Texture(Gdx.files.internal("Space-02.png"));
        background.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        //ScalingViewport viewport = new ScalingViewport(Scaling.fill,800,480);
        stage = new Stage(new ExtendViewport(1800, 900));

        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        table.setSkin(skin);

        pauseMenu = new PauseMenu(controller, stage);

        countDownLabel = new Label(Integer.toString((int) countDown), skin);
        countDownLabel.setHeight(500);
        countDownLabel.setWidth(500);
        countDownLabel.setFontScale(1.5f);
        table.add(countDownLabel);

        optionsTex = new Texture(Gdx.files.internal("options.png"));


        backgroundReg = new TextureRegion(background, background.getWidth(), background.getHeight());
        tiledBackground = new TiledDrawable(backgroundReg);

        camera.position.x = character.getScreenX();
        camera.position.y = character.getScreenY();
        camera.update();
    }



    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, .3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        tiledBackground.draw(game.batch, -camera.viewportWidth, -worldHeight - camera.viewportHeight, worldWidth + 2 * camera.viewportWidth, worldHeight + 2 * camera.viewportHeight);

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

        if (countDown > 0) {
            stage.act(delta);
            stage.draw();
            countDownLabel.setText(Integer.toString((int) countDown + 1));
        }

//        if (levelFinished) {
//            displayDialog = true;
//            controller.levComplete((int) score);
//        }


        handleInput();
        renderVSetter();
        model.doLogic(delta);

        camera.position.set(model.getCharacter().getScreenX(), model.getCharacter().getScreenY(), 0);
        camera.update();
    }

    private void renderVSetter() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        game.shapeRenderer.begin();
        game.shapeRenderer.setProjectionMatrix(staticCamera.combined);
        game.shapeRenderer.setColor(1 - 1 / (startScore / score), 0f, score / startScore, .6f);
        game.shapeRenderer.set(ShapeType.Filled);
        game.shapeRenderer.rect(0, 0, (score / startScore) * staticCamera.viewportWidth, 20);

        if (!optionsClicked) vSetter.render(game.shapeRenderer, camera);

        game.shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
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
                if (optionsClicked) {
                    pauseMenu.resume();
                    System.out.println("Resumed by options click");
                }
                else {
                    optionsClicked = true;
                }
            }
        }
    }

//    private void onWallCollision(float force) {
//        float volume;
//        if (force < 100) volume = .1f;
//        else if (force < 1000) volume = .2f;
//        else if (force < 3000) volume = .5f;
//        else if (force < 5000) volume = .7f;
//        else if (force < 8000) volume = .85f;
//        else volume = 1;
//
//        collisionSound.play(volume);
//        System.out.println(force);
////        collisionSound.play();
//        score = score - GravityGame.CONTACT_DECREMENT * force < 0 ? 0 : score - GravityGame.CONTACT_DECREMENT * force;
//    }




    public void dispose() {
        for (Explosion64 exp : explosions) {
            exp.dispose();
        }
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




}
