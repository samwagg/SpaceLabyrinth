package com.samwagg.gravity.main_game_module.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.samwagg.gravity.GravityGame;
import com.samwagg.gravity.main_game_module.Map;
import com.samwagg.gravity.main_game_module.game.game_objects.Explosion64;
import com.samwagg.gravity.main_game_module.game.game_objects.GravField;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by sam on 7/27/16.
 */
public class GravityGameModel {

    private GravityGame game;
    private GravityGameController controller;
    private World world;

    private com.samwagg.gravity.main_game_module.game.game_objects.GameCharacter character;
    private List<com.samwagg.gravity.main_game_module.game.game_objects.Wall> walls;
    private List<com.samwagg.gravity.main_game_module.game.game_objects.GravField> gravFields;
    private List<com.samwagg.gravity.main_game_module.game.game_objects.GravField> currentGravFields;
    private List<com.samwagg.gravity.main_game_module.game.game_objects.MovingWall> movingWalls;
    private List<com.samwagg.gravity.main_game_module.game.game_objects.Explosion64> explosions;
    private List<com.samwagg.gravity.main_game_module.game.game_objects.FinishSensor> endSensors;

    private Vector2 gravVect;
    
    private boolean shipGone;
    private boolean gamePaused;
    private boolean explosionBegun;
    private boolean explosionEvent;
    private boolean restart;
    private float crashForce;


    private Map map;
    private float startScore;

    private float score;
    private boolean levelFinished;

    private final float WALL_WIDTH = 128;
    private final float WALL_HEIGHT = 128;
    private float worldWidth;
    private float worldHeight;
    private float mapHeight;
    private float mapWidth;

    private float accumulator = 0;

    private float countDown;

    private LevelCompleteListener callback;



    public GravityGameModel(final GravityGame game, Map map) {
        this.game = game;



        World.setVelocityThreshold(0);

//		multiPlex = new InputMultiplexer();
//		multiPlex.addProcessor(vSetter.getInputProcessor());

        this.map = map;
        startScore = map.getInitScore();


        resetLevel();
    }

    private void resetLevel() {
        System.out.println("level reset");
        countDown = 3;

        startScore = map.getInitScore();

        mapHeight = map.getHeight();
        mapWidth = map.getWidth();
        worldWidth = mapWidth * WALL_WIDTH;
        worldHeight = mapHeight * WALL_HEIGHT;

        world= new World(new Vector2(0, 0), true);
        world.setContactListener(new ForceListener());

        walls = new ArrayList<com.samwagg.gravity.main_game_module.game.game_objects.Wall>();
        gravFields = new ArrayList<com.samwagg.gravity.main_game_module.game.game_objects.GravField>();
        endSensors = new ArrayList<com.samwagg.gravity.main_game_module.game.game_objects.FinishSensor>();
        currentGravFields = new LinkedList<com.samwagg.gravity.main_game_module.game.game_objects.GravField>();
        movingWalls = new LinkedList<com.samwagg.gravity.main_game_module.game.game_objects.MovingWall>();

        explosions = new LinkedList<com.samwagg.gravity.main_game_module.game.game_objects.Explosion64>();

        gravVect = new Vector2(0, -3);
        world.setGravity(gravVect);

        score = startScore;
        levelFinished = false;
        shipGone = false;
        gamePaused = false;
        explosionBegun = false;
        crashForce = 0;
        explosionEvent = false;

        Map.GameTile[][] tileArray = map.getTileArray();
        System.out.println(map);
        genGameObjects(tileArray);
        System.out.println(map);


    }


    /*
 * Create all game objects from map member variable.
 */
    private void genGameObjects(Map.GameTile[][] tileArray) {
        
        float xPos;
        float yPos;
        Map.GameTile currTile;
        for (int i = 0; i < mapHeight; i++) {

            for (int j = 0; j < mapWidth; j++) {

                xPos = j * WALL_WIDTH;
                yPos = -i * WALL_HEIGHT;
                //System.out.println("xpos = " + xPos + " and ypos = " + yPos);
                currTile = tileArray[i][j];
                if (currTile == null) continue;

                switch (currTile) {
                    case WALL:
                        System.out.println("making wall");
                        Rectangle block = extendTile(i, i, j, j, currTile, tileArray);
                        walls.add(new com.samwagg.gravity.main_game_module.game.game_objects.Wall(xPos, yPos - WALL_HEIGHT * (block.height - 1), block.width * WALL_WIDTH, block.height * WALL_HEIGHT,world,game.constants));
                        break;
                    case START:
                        character = new com.samwagg.gravity.main_game_module.game.game_objects.GameCharacter(xPos, yPos,world,game.constants);
                        System.out.println("i = " + i + " and j = " + j);
                        character.updatePosition();
                        System.out.println("here at char");
                        break;
                    case FORCE_RIGHT:
                        gravFields.add(new com.samwagg.gravity.main_game_module.game.game_objects.GravField(xPos, yPos, 0,world,game.constants));
                        break;
                    case FORCE_UP:
                        gravFields.add(new com.samwagg.gravity.main_game_module.game.game_objects.GravField(xPos, yPos, 90,world,game.constants));
                        break;
                    case FORCE_LEFT:
                        gravFields.add(new com.samwagg.gravity.main_game_module.game.game_objects.GravField(xPos, yPos, 180,world,game.constants));
                        break;
                    case FORCE_DOWN:
                        gravFields.add(new com.samwagg.gravity.main_game_module.game.game_objects.GravField(xPos, yPos, 270,world,game.constants));
                        break;
                    case END:
                        endSensors.add(new com.samwagg.gravity.main_game_module.game.game_objects.FinishSensor(xPos, yPos,world,game.constants));
                        break;
                    case AI_START: // not implemented yet
                        break;
                    default:
                        ;// Do nothing
                }

                if (currTile.isMovBlock()) {
                    Rectangle movBlock = extendTile(i, i, j, j, currTile, tileArray);
                    makeMovBlock(i, j, (int) movBlock.width, (int) movBlock.height, currTile.getSpeed(), WALL_WIDTH, WALL_HEIGHT, tileArray);
                }
            }
        }

    }

    /*
     * Search outward (either vertically or horizontally) from (i,j) for "RANGE" tiles, to define a MovingWall object
     * centered at (i,j).
     */
    private void makeMovBlock(int i, int j, int width, int height, float speed, float WALL_WIDTH, float WALL_HEIGHT, Map.GameTile[][] tileArray) {

        if (tileArray[i + height][j] == Map.GameTile.RANGE_VERT
                || tileArray[i - 1][j] == Map.GameTile.RANGE_VERT
                || tileArray[i + height][j] == Map.GameTile.END_RANGE
                || tileArray[i - 1][j] == Map.GameTile.END_RANGE) {

            int rangeAbove = 0;
            int rangeBelow = 0;

            int k = 1;
            while (true) {
                if (tileArray[i + height - 1 + k][j] == Map.GameTile.RANGE_VERT) {
                    rangeBelow += 1;
                }
                else if (tileArray[i + height - 1 + k][j] == Map.GameTile.END_RANGE) {
                    rangeBelow += 1;
                    break;
                }
                else break;
                k++;
            }

            k = 1;
            while (true) {
                if (tileArray[i - k][j] == Map.GameTile.RANGE_VERT) {
                    rangeAbove += 1;
                }
                else if (tileArray[i - k][j] == Map.GameTile.END_RANGE) {
                    rangeAbove += 1;
                    break;
                }
                else break;
                k++;
            }

            movingWalls.add(new com.samwagg.gravity.main_game_module.game.game_objects.MovingWall(j * WALL_WIDTH, -i * WALL_HEIGHT - (height - 1) * WALL_HEIGHT, width * WALL_WIDTH, height * WALL_HEIGHT, new Vector2(0, 0), new Vector2(-i * WALL_HEIGHT - rangeBelow * WALL_HEIGHT - (height - 1) * WALL_HEIGHT, -i * WALL_HEIGHT + rangeAbove * WALL_HEIGHT - (height - 1) * WALL_HEIGHT),world,true, speed, game.constants));
        }

        else if (tileArray[i][j + width] == Map.GameTile.RANGE_HOR
                || tileArray[i][j - 1] == Map.GameTile.RANGE_HOR
                || tileArray[i][j + width] == Map.GameTile.END_RANGE
                || tileArray[i][j - 1] == Map.GameTile.END_RANGE) {
            int rangeRight = 0;
            int rangeLeft = 0;

            int k = 1;
            while (true) {
                if (tileArray[i][j + width - 1 + k] == Map.GameTile.RANGE_HOR) {
                    rangeRight += 1;
                }
                else if (tileArray[i][j + width - 1 + k] == Map.GameTile.END_RANGE) {
                    rangeRight += 1;
                    break;
                }
                else break;
                k++;
            }

            k = 1;
            while (true) {
                if (tileArray[i][j - k] == Map.GameTile.RANGE_HOR) {
                    rangeLeft += 1;
                }
                else if (tileArray[i][j - k] == Map.GameTile.END_RANGE) {
                    rangeLeft += 1;
                    break;
                }
                else break;
                k++;
            }

            movingWalls.add(new com.samwagg.gravity.main_game_module.game.game_objects.MovingWall(j * WALL_WIDTH, -i * WALL_HEIGHT - (height - 1) * WALL_HEIGHT, width * WALL_WIDTH, height * WALL_HEIGHT, new Vector2(j * WALL_WIDTH - rangeLeft * WALL_WIDTH, j * WALL_WIDTH + rangeRight * WALL_WIDTH), new Vector2(0, 0),world,false, speed, game.constants));
        }
    }

    /*
     * Recursively search outward from rectangular tile area represented by iLow, iHigh, jLow, jHigh, for tiles of
     * type type, in order to traverse and record the dimensions of the area of like tiles.
     */
    private Rectangle extendTile(int iLow, int iHigh, int jLow, int jHigh, Map.GameTile type, Map.GameTile[][] map) {

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

    public void doLogic(float delta) {

        if (!gamePaused) {
            if (countDown > 0) countDown -= delta;
            else {
                if (score < 1 && !explosionBegun) {
                    explosions.add(new com.samwagg.gravity.main_game_module.game.game_objects.Explosion64(character.getScreenX(), character.getScreenY()));
                    explosionBegun = true;
                    shipGone = true;
                    world.destroyBody(character.getBody());
                    System.out.println("Beginning explosion");
                }

                for (Iterator<Explosion64> iter = explosions.iterator(); iter.hasNext(); ) {
                    Explosion64 exp = iter.next();
                    if (exp.done()) iter.remove();
                    System.out.println("Explosion done: " + exp.done() + "\nExplosion CurrentFrame: " + exp.getCurrentFrame());
                }

                if (score == 0 && explosions.isEmpty() || restart) {
                    System.out.println("explosions: " + explosions + "\nrestart = " + restart);
                    healthDepleted();
                    return;
                }

                float gravFieldDirection;
                for (GravField field : currentGravFields) {
                    gravFieldDirection = field.getRotation() * (float) Math.PI / 180;
                    character.getBody().applyForceToCenter((float) (200 * Math.cos(gravFieldDirection)), (float) (200 * Math.sin(gravFieldDirection)), true);
                }

                doPhysicsStep(delta);

            }
        }

//        if (countDown > 0) {
//            displayDialog = true;
//            countDown -= delta;
//        }
//        else if (!levelFinished && !optionsClicked) {
//            Gdx.input.setInputProcessor(vSetter.getInputProcessor());
//            doPhysicsStep(delta);
//        }
    }

    private void doPhysicsStep(float deltaTime) {
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        while (accumulator >= 1 / 45f) {

            world.step(1 / 45f, 6, 2);

            if (score > 0 && !levelFinished) score--;

            for (com.samwagg.gravity.main_game_module.game.game_objects.MovingWall wall : movingWalls) {
                wall.move();
            }
            accumulator -= 1 / 45f;
        }
        character.updatePosition();
    }

    public void setGravity(float x, float y) {
        gravVect.x = x;
        gravVect.y = y;
        world.setGravity(gravVect);
    }

    public float getWallCrash() {
        float lastCrashForce = crashForce;
        crashForce = 0;
        if (lastCrashForce > 0) System.out.println("lastCrashForce = " + lastCrashForce);
        return lastCrashForce;
    }

    public boolean getExplosionEvent() {
        if (explosionBegun && !explosionEvent) {
            explosionEvent = true;
            return true;
        }
        else return false;
    }

    public void changeLevel(Map map) {
        this.map = map;
        resetLevel();
    }

    public float getCountDownToStart() {
        return countDown;
    }

    public com.samwagg.gravity.main_game_module.game.game_objects.GameCharacter getCharacter() {
        return character;
    }

    public List<com.samwagg.gravity.main_game_module.game.game_objects.Wall> getWalls() {
        return walls;
    }

    public List<com.samwagg.gravity.main_game_module.game.game_objects.GravField> getGravFields() {
        return gravFields;
    }

    public List<com.samwagg.gravity.main_game_module.game.game_objects.MovingWall> getMovingWalls() {
        return movingWalls;
    }

    public List<com.samwagg.gravity.main_game_module.game.game_objects.Explosion64> getExplosions() {
        return explosions;
    }

    public boolean isShipGone() {
        return shipGone;
    }

    public void pause(boolean pause) {
        gamePaused = pause;
    }

    public boolean isPaused() {
        return gamePaused;
    }

    public void restart() {
        resetLevel();
    }

    public GravityGame getGame() {
        return game;
    }

    public float getStartScore() {
        return startScore;
    }

    public float getScore() {
        return score;
    }

    public float getWorldHeight() {
        return worldHeight;
    }

    public float getWorldWidth() {
        return worldWidth;
    }

    public void registerLevelCompleteCallback(LevelCompleteListener callback) {
        this.callback = callback;
    }



    private void healthDepleted() {
        resetLevel();
    }

    private void onWallCollision(float force) {

        System.out.println(force);
//        collisionSound.play();
        crashForce = force;
        score = score - GravityGame.CONTACT_DECREMENT * force < 0 ? 0 : score - GravityGame.CONTACT_DECREMENT * force;
    }

    private void onLevelCompleted() {
        callback.onLevelCompleted((int) score);
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

            com.samwagg.gravity.main_game_module.game.game_objects.GravField field = (com.samwagg.gravity.main_game_module.game.game_objects.GravField) forceFix.getUserData();
            field.unlight();
            currentGravFields.remove(field);
        }

        @Override
        public void beginContact(Contact contact) {

            Fixture fixA = contact.getFixtureA();
            Fixture fixB = contact.getFixtureB();

            Fixture forceFix;

            if (fixA.getUserData().getClass().equals(com.samwagg.gravity.main_game_module.game.game_objects.GravField.class)) {
                forceFix = contact.getFixtureA();
            }
            else if (fixB.getUserData().getClass().equals(com.samwagg.gravity.main_game_module.game.game_objects.GravField.class)) {
                forceFix = contact.getFixtureB();
            }
            else if (fixA.getUserData().getClass().equals(com.samwagg.gravity.main_game_module.game.game_objects.FinishSensor.class) ||
                    fixB.getUserData().getClass().equals(com.samwagg.gravity.main_game_module.game.game_objects.FinishSensor.class)) {
                levelFinished = true;
                onLevelCompleted();
                return;
            }
            else return;

            com.samwagg.gravity.main_game_module.game.game_objects.GravField field = (com.samwagg.gravity.main_game_module.game.game_objects.GravField) forceFix.getUserData();
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

            if (fixA.getUserData().getClass().equals(com.samwagg.gravity.main_game_module.game.game_objects.GameCharacter.class) &&
                    fixB.getUserData().getClass().equals(com.samwagg.gravity.main_game_module.game.game_objects.Wall.class) ||
                    fixA.getUserData().getClass().equals(com.samwagg.gravity.main_game_module.game.game_objects.Wall.class) &&
                            fixB.getUserData().getClass().equals(com.samwagg.gravity.main_game_module.game.game_objects.GameCharacter.class)) {

                onWallCollision(forceStren);

            }
            else if (fixA.getUserData().getClass().equals(com.samwagg.gravity.main_game_module.game.game_objects.GameCharacter.class) &&
                    fixB.getUserData().getClass().equals(com.samwagg.gravity.main_game_module.game.game_objects.MovingWall.class) ||
                    fixA.getUserData().getClass().equals(com.samwagg.gravity.main_game_module.game.game_objects.MovingWall.class) &&
                            fixB.getUserData().getClass().equals(com.samwagg.gravity.main_game_module.game.game_objects.GameCharacter.class)) {
                onWallCollision(forceStren);
            }
        }
    }

}
