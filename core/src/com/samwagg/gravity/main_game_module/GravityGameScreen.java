package com.samwagg.gravity.main_game_module;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
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
import com.samwagg.gravity.controller.GravityGameController;
import com.samwagg.gravity.main_game_module.game_objects.Explosion64;
import com.samwagg.gravity.main_game_module.game_objects.FinishSensor;
import com.samwagg.gravity.main_game_module.game_objects.GameCharacter;
import com.samwagg.gravity.main_game_module.game_objects.GravField;
import com.samwagg.gravity.Map;
import com.samwagg.gravity.Map.GameTile;
import com.samwagg.gravity.Map.MapFormatException;
import com.samwagg.gravity.main_game_module.game_objects.MovingWall;
import com.samwagg.gravity.main_game_module.game_objects.Wall;
import com.samwagg.gravity.main_game_module.widgets.PauseMenu;

public class GravityGameScreen implements Screen {


    private OrthographicCamera camera;
    private OrthographicCamera staticCamera;
    private VectorSetter vSetter;
    private Map map;

    private List<Wall> walls;
    private List<FinishSensor> endSensors;
    private GameCharacter character;
    private List<GravField> gravFields;
    private List<MovingWall> movingWalls;
    private List<EnemySteeringAgent> enemies;

    private List<Explosion64> explosions;
    private List<Explosion64> finishedExplosions;

    private final GravityGame game;
    private final GravityGameController controller;

    private float accumulator = 0;

    private Vector2 gravVect;

    private List<GravField> currentGravFields;

    private float score;
    private boolean explosionBegun;
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


    /**
     * @param controller
     * @param game
     * @param galaxy The galaxy number in which the level appears is in
     * @param level The level to load
     */
    public GravityGameScreen(GravityGameController controller, final GravityGame game, int galaxy, int level) {
        this.game = game;
        this.controller = controller;

        Gdx.graphics.setVSync(true);

        WORLD = new World(new Vector2(0, 0), true);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1000, 600);
        extViewport = new ExtendViewport(1000, 600, camera);

        staticCamera = new OrthographicCamera();
        staticCamera.setToOrtho(false, 960, 576);

        currentGravFields = new LinkedList<GravField>();
        enemies = new LinkedList<EnemySteeringAgent>();
        movingWalls = new LinkedList<MovingWall>();

        explosions = new LinkedList<Explosion64>();
        finishedExplosions = new LinkedList<Explosion64>();

        gravVect = new Vector2(0, -3);
        WORLD.setGravity(gravVect);
        World.setVelocityThreshold(0);

        vSetter = new VectorSetter(staticCamera);

        shipGone = false;

//		multiPlex = new InputMultiplexer();
//		multiPlex.addProcessor(vSetter.getInputProcessor());

        try {
            map = new Map(galaxy, level);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MapFormatException e) {
            e.printStackTrace();
        }

        background = new Texture(Gdx.files.internal("Space-02.png"));
        background.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        genGameObjects();
        WORLD.setContactListener(new ForceListener());

        score = map.getInitScore();
        startScore = score;
        this.level = level;
        levelFinished = false;

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
    }

    /*
     * Create all game objects from map member variable.
     */
    private void genGameObjects() {
        float wallWidth = 128;
        float wallHeight = 128;
        walls = new ArrayList<Wall>();
        gravFields = new ArrayList<GravField>();
        endSensors = new ArrayList<FinishSensor>();

        float mapHeight = map.getTileArray().length;
        float mapWidth = map.getTileArray()[0].length;
        worldWidth = mapWidth * wallWidth;
        worldHeight = mapHeight * wallHeight;

        float xPos;
        float yPos;
        Map.GameTile currTile;
        for (int i = 0; i < mapHeight; i++) {

            for (int j = 0; j < mapWidth; j++) {

                xPos = j * wallWidth;
                yPos = -i * wallHeight;
                //System.out.println("xpos = " + xPos + " and ypos = " + yPos);
                currTile = map.getTileArray()[i][j];
                if (currTile == null) continue;

                switch (currTile) {
                    case WALL:
                        Rectangle block = extendTile(i, i, j, j, currTile, map.getTileArray());
                        walls.add(new Wall(xPos, yPos - wallHeight * (block.height - 1), block.width * wallWidth, block.height * wallHeight, WORLD, game.constants));
                        break;
                    case START:
                        character = new GameCharacter(xPos, yPos, WORLD, game.constants);
                        System.out.println("i = " + i + " and j = " + j);
                        character.updatePosition();
                        System.out.println("here at char");
                        break;
                    case FORCE_RIGHT:
                        gravFields.add(new GravField(xPos, yPos, 0, WORLD, game.constants));
                        break;
                    case FORCE_UP:
                        gravFields.add(new GravField(xPos, yPos, 90, WORLD, game.constants));
                        break;
                    case FORCE_LEFT:
                        gravFields.add(new GravField(xPos, yPos, 180, WORLD, game.constants));
                        break;
                    case FORCE_DOWN:
                        gravFields.add(new GravField(xPos, yPos, 270, WORLD, game.constants));
                        break;
                    case END:
                        endSensors.add(new FinishSensor(xPos, yPos, WORLD, game.constants));
                        break;
                    case AI_START:
                        enemies.add(new EnemySteeringAgent(xPos, yPos, WORLD, character));
                        break;
                    default:
                        ;// Do nothing
                }

                if (currTile.isMovBlock()) {
                    Rectangle movBlock = extendTile(i, i, j, j, currTile, map.getTileArray());
                    makeMovBlock(i, j, (int) movBlock.width, (int) movBlock.height, currTile.getSpeed(), wallWidth, wallHeight);
                }
            }
        }
        backgroundReg = new TextureRegion(background, background.getWidth(), background.getHeight());
        tiledBackground = new TiledDrawable(backgroundReg);

        camera.position.x = character.getScreenX();
        camera.position.y = character.getScreenY();
        camera.update();
    }

    /*
     * Search outward (either vertically or horizontally) from (i,j) for "RANGE" tiles, to define a MovingWall object
     * centered at (i,j).
     */
    private void makeMovBlock(int i, int j, int width, int height, float speed, float wallWidth, float wallHeight) {

        if (map.getTileArray()[i + height][j] == Map.GameTile.RANGE_VERT
                || map.getTileArray()[i - 1][j] == Map.GameTile.RANGE_VERT
                || map.getTileArray()[i + height][j] == Map.GameTile.END_RANGE
                || map.getTileArray()[i - 1][j] == Map.GameTile.END_RANGE) {

            int rangeAbove = 0;
            int rangeBelow = 0;

            int k = 1;
            while (true) {
                if (map.getTileArray()[i + height - 1 + k][j] == Map.GameTile.RANGE_VERT) {
                    rangeBelow += 1;
                }
                else if (map.getTileArray()[i + height - 1 + k][j] == Map.GameTile.END_RANGE) {
                    rangeBelow += 1;
                    break;
                }
                else break;
                k++;
            }

            k = 1;
            while (true) {
                if (map.getTileArray()[i - k][j] == Map.GameTile.RANGE_VERT) {
                    rangeAbove += 1;
                }
                else if (map.getTileArray()[i - k][j] == Map.GameTile.END_RANGE) {
                    rangeAbove += 1;
                    break;
                }
                else break;
                k++;
            }

            movingWalls.add(new MovingWall(j * wallWidth, -i * wallHeight - (height - 1) * wallHeight, width * wallWidth, height * wallHeight, new Vector2(0, 0), new Vector2(-i * wallHeight - rangeBelow * wallHeight - (height - 1) * wallHeight, -i * wallHeight + rangeAbove * wallHeight - (height - 1) * wallHeight), WORLD, true, speed, game.constants));


        }

        else if (map.getTileArray()[i][j + width] == Map.GameTile.RANGE_HOR
                || map.getTileArray()[i][j - 1] == Map.GameTile.RANGE_HOR
                || map.getTileArray()[i][j + width] == Map.GameTile.END_RANGE
                || map.getTileArray()[i][j - 1] == Map.GameTile.END_RANGE) {
            int rangeRight = 0;
            int rangeLeft = 0;

            int k = 1;
            while (true) {
                if (map.getTileArray()[i][j + width - 1 + k] == Map.GameTile.RANGE_HOR) {
                    rangeRight += 1;
                }
                else if (map.getTileArray()[i][j + width - 1 + k] == Map.GameTile.END_RANGE) {
                    rangeRight += 1;
                    break;
                }
                else break;
                k++;
            }

            k = 1;
            while (true) {
                if (map.getTileArray()[i][j - k] == Map.GameTile.RANGE_HOR) {
                    rangeLeft += 1;
                }
                else if (map.getTileArray()[i][j - k] == Map.GameTile.END_RANGE) {
                    rangeLeft += 1;
                    break;
                }
                else break;
                k++;
            }

            movingWalls.add(new MovingWall(j * wallWidth, -i * wallHeight - (height - 1) * wallHeight, width * wallWidth, height * wallHeight, new Vector2(j * wallWidth - rangeLeft * wallWidth, j * wallWidth + rangeRight * wallWidth), new Vector2(0, 0), WORLD, false, speed, game.constants));
        }
    }

    /*
     * Recursively search outward from rectangular tile area represented by iLow, iHigh, jLow, jHigh, for tiles of
     * type type, in order to traverse and record the dimensions of the area of like tiles.
     */
    private Rectangle extendTile(int iLow, int iHigh, int jLow, int jHigh, GameTile type, GameTile[][] map) {

        boolean expandableRight = true, expandableLeft = true, expandableUp = true, expandableDown = true;

        for (int i = iLow; i <= iHigh; i++) {
            if (expandableRight && !(jHigh + 1 < map[0].length && map[i][jHigh + 1] == type)) {
                expandableRight = false;

            }
            if (expandableLeft && !(jLow - 1 >= 0 && map[i][jLow - 1] == type)) {
                expandableLeft = false;
            }
        }

        if (expandableRight) jHigh++;
        if (expandableLeft) jLow--;

        for (int j = jLow; j <= jHigh; j++) {
            if (expandableUp && !(iHigh + 1 < map.length && map[iHigh + 1][j] == type)) {
                expandableUp = false;
            }
            if (expandableDown && !(iLow - 1 >= 0 && map[iLow - 1][j] == type)) {
                expandableDown = false;
            }
        }

        if (expandableUp) iHigh++;
        if (expandableDown) iLow--;


        if (!expandableUp && !expandableDown && !expandableRight && !expandableLeft) {
            for (int i = iLow; i <= iHigh; i++) {
                for (int j = jLow; j <= jHigh; j++) {
                    map[i][j] = Map.GameTile.EMPTY;
                }
            }

            return new Rectangle(jLow, iLow, jHigh - jLow + 1, iHigh - iLow + 1);
        }
        else {
            //System.out.println("Recurse");
            return extendTile(iLow, iHigh, jLow, jHigh, type, map);
        }

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, .3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        tiledBackground.draw(game.batch, -camera.viewportWidth, -worldHeight - camera.viewportHeight, worldWidth + 2 * camera.viewportWidth, worldHeight + 2 * camera.viewportHeight);

        for (GravField field : gravFields) {
            field.getSprite().draw(game.batch);
        }

        if (!shipGone) {
            character.getSprite().draw(game.batch);
            character.getSprite().rotate(.2f);
        }

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        for (Wall wall : walls) {
            wall.draw(game.batch, camera);
        }

        for (MovingWall wall : movingWalls) {
            wall.draw(game.batch, camera);
        }

        Gdx.gl.glDisable(GL20.GL_BLEND);

        for (EnemySteeringAgent enemy : enemies) {
            game.batch.draw(enemy.getGameObject().getTexture(), enemy.getGameObject().getScreenX(), enemy.getGameObject().getScreenY());
        }

        for (Explosion64 exp : explosions) {
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

        handleInput();
        renderVSetter();
        doLogic(delta);

        camera.position.set(character.getScreenX(), character.getScreenY(), 0);
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

    private void doLogic(float delta) {

        if (levelFinished) {
            displayDialog = true;
            controller.levComplete((int) score);
        }

        if (score == 0 && !explosionBegun) {
            explosions.add(new Explosion64(character.getScreenX(), character.getScreenY()));
            explosionBegun = true;
            shipGone = true;
            System.out.println("Beginning explosion");
        }

        for (Iterator<Explosion64> iter = explosions.iterator(); iter.hasNext();) {
            Explosion64 exp = iter.next();
            if (exp.done()) iter.remove();
            System.out.println("Explosion done: " + exp.done() + "\nExplosion CurrentFrame: " + exp.getCurrentFrame());
        }

        if (score == 0 && explosions.isEmpty() || restart) {
            System.out.println("explosions: " + explosions + "\nrestart = " + restart);
            controller.healthDepleted();
            return;
        }

        gravVect.x = vSetter.getXComponent() * .05f;
        gravVect.y = vSetter.getYComponent() * .05f;
        WORLD.setGravity(gravVect);

        float gravFieldDirection;
        for (GravField field : currentGravFields) {
            gravFieldDirection = field.getRotation() * (float) Math.PI / 180;
            character.getBody().applyForceToCenter((float) (200 * Math.cos(gravFieldDirection)), (float) (200 * Math.sin(gravFieldDirection)), true);
        }

        if (optionsClicked) {

            vSetter.onInputTurnedOff();
            Gdx.input.setInputProcessor(stage);

            switch (pauseMenu.displayIfPaused()) {
                case RESUME:
                    pauseMenu.reset();
                    optionsClicked = false;
                    System.out.println("resume pressed");
                    Gdx.input.setInputProcessor(vSetter.getInputProcessor());
                    break;

                case RESTART:
                    restart = true;
                    break;

                case MAIN_MENU:
                    System.out.println("Here at switch mainmenu");
                    game.mainMenuStartPressed(this);
                    break;

                case REMAIN:
                    ;
            }
        }

        if (countDown > 0) {
            displayDialog = true;
            countDown -= delta;
        }
        else if (!levelFinished && !optionsClicked) {
            Gdx.input.setInputProcessor(vSetter.getInputProcessor());
            doPhysicsStep(delta);
        }
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

    private void doPhysicsStep(float deltaTime) {
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        while (accumulator >= 1 / 45f) {

            WORLD.step(1 / 45f, 6, 2);

            if (score > 0 && !levelFinished) score--;

            for (MovingWall wall : movingWalls) {
                wall.move();
            }

            for (EnemySteeringAgent enemy : enemies) {
                enemy.update(1 / 45f);
            }

            accumulator -= 1 / 45f;
        }
        character.updatePosition();

        for (EnemySteeringAgent enemy : enemies) {
            enemy.getGameObject().updatePosition();
        }
    }

    public void dispose() {
        for (Explosion64 exp : explosions) {
            exp.dispose();
        }
        this.background.dispose();
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

    /**
     * ForceListener is a ContactListener that handles all contact events between
     */
    public class ForceListener implements ContactListener {

        @Override
        public void endContact(Contact contact) {
            Fixture forceFix;
            if (contact.getFixtureA().isSensor()) {
                forceFix = contact.getFixtureA();
            }
            else if (contact.getFixtureB().isSensor()) {
                forceFix = contact.getFixtureB();
            }
            else return;

            GravField field = (GravField) forceFix.getUserData();
            field.unlight();
            currentGravFields.remove(field);
        }

        @Override
        public void beginContact(Contact contact) {

            Fixture fixA = contact.getFixtureA();
            Fixture fixB = contact.getFixtureB();

            Fixture forceFix;

            if (fixA.getUserData().getClass().equals(GravField.class)) {
                forceFix = contact.getFixtureA();
            }
            else if (fixB.getUserData().getClass().equals(GravField.class)) {
                forceFix = contact.getFixtureB();
            }
            else if (fixA.getUserData().getClass().equals(FinishSensor.class) ||
                    fixB.getUserData().getClass().equals(FinishSensor.class)) {
                levelFinished = true;
                return;
            }
            else return;

            GravField field = (GravField) forceFix.getUserData();
            field.light();
            currentGravFields.add(field);
        }


        @Override
        public void preSolve(Contact contact, Manifold oldManifold) {

        }

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {
            Fixture fixA = contact.getFixtureA();
            Fixture fixB = contact.getFixtureB();

            float forceStren = impulse.getNormalImpulses()[0];

            if (fixA.getUserData().getClass().equals(GameCharacter.class) &&
                    fixB.getUserData().getClass().equals(Wall.class) ||
                    fixA.getUserData().getClass().equals(Wall.class) &&
                            fixB.getUserData().getClass().equals(GameCharacter.class)) {

                score = score - GravityGame.CONTACT_DECREMENT * forceStren < 0 ? 0 : score - GravityGame.CONTACT_DECREMENT * forceStren;

            }
            else if (fixA.getUserData().getClass().equals(GameCharacter.class) &&
                    fixB.getUserData().getClass().equals(MovingWall.class) ||
                    fixA.getUserData().getClass().equals(MovingWall.class) &&
                            fixB.getUserData().getClass().equals(GameCharacter.class)) {
                score = score - GravityGame.CONTACT_DECREMENT * forceStren < 0 ? 0 : score - GravityGame.CONTACT_DECREMENT * forceStren;
            }
        }
    }


}
