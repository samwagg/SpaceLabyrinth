package com.samwagg.gravity.main_game_module.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.samwagg.gravity.GravityGame;
import com.samwagg.gravity.main_game_module.Map;
import com.samwagg.gravity.main_game_module.game.game_objects.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * The logical encapsulation of the game
 */
public class GravityGameModel {

    private GravityGame game;
    private GravityGameController controller;
    private World world;

    private List<AICharacter> enemies;
    private List<AICharacter> explodingEnemies;
    private GameCharacter character;
    private List<Wall> walls;
    private List<GravField> gravFields;
    private List<ForceCharPair> activeGravCharPairs;
    private List<MovingWall> movingWalls;
    private List<Explosion> explosions;
    private List<FinishSensor> endSensors;

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

    private static final float WALL_WIDTH = 6.4f;
    private static final float WALL_HEIGHT = 6.4f;
    private static final float CHAR_RADIUS = 3.2f;
    private static final float EXPLOSION_DURATION = 1;

    private float worldWidth;
    private float worldHeight;
    private float mapHeight;
    private float mapWidth;

    private float accumulator = 0;

    private float countDown;

    private LevelCompleteListener callback;

    private static final float AI_ACTIVATION_DISTANCE = 15;



    public GravityGameModel(final GravityGame game, Map map) {
        this.game = game;
        World.setVelocityThreshold(0);
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

        enemies = new ArrayList<AICharacter>();
        explodingEnemies = new ArrayList<AICharacter>();
        walls = new ArrayList<Wall>();
        gravFields = new ArrayList<GravField>();
        endSensors = new ArrayList<FinishSensor>();
        activeGravCharPairs = new ArrayList<ForceCharPair>();
        movingWalls = new LinkedList<MovingWall>();

        explosions = new LinkedList<Explosion>();

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
                        walls.add(new Wall(xPos + (block.width-1)*WALL_WIDTH/2, yPos - (block.height-1)*WALL_HEIGHT/2, block.width * WALL_WIDTH, block.height * WALL_HEIGHT,world));
                        break;
                    case START:
                        character = new GameCharacter(xPos, yPos, CHAR_RADIUS, world);
                        System.out.println("i = " + i + " and j = " + j);
                        System.out.println("here at char");
                        break;
                    case FORCE_RIGHT:
                        gravFields.add(new GravField(xPos, yPos, WALL_WIDTH, WALL_HEIGHT , 0, world));
                        break;
                    case FORCE_UP:
                        gravFields.add(new GravField(xPos, yPos, WALL_WIDTH, WALL_HEIGHT , 90, world));
                        break;
                    case FORCE_LEFT:
                        gravFields.add(new GravField(xPos, yPos, WALL_WIDTH, WALL_HEIGHT , 180, world));
                        break;
                    case FORCE_DOWN:
                        gravFields.add(new GravField(xPos, yPos, WALL_WIDTH, WALL_HEIGHT , 270, world));
                        break;
                    case END:
                        endSensors.add(new FinishSensor(xPos, yPos, WALL_WIDTH, WALL_HEIGHT, world));
                        break;
                    case AI_START:
                        AICharacter enemy = new AICharacter(xPos, yPos, CHAR_RADIUS, world);
                        enemies.add(enemy);
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

            movingWalls.add(new MovingWall(j * WALL_WIDTH + (width-1)*WALL_WIDTH/2, -i * WALL_HEIGHT - (height-1)*WALL_HEIGHT/2,
                    width * WALL_WIDTH, height * WALL_HEIGHT, new Vector2(0, 0),
                    new Vector2(-i * WALL_HEIGHT - (height-1)*WALL_HEIGHT/2 - rangeBelow * WALL_HEIGHT,
                            -i * WALL_HEIGHT - (height-1)*WALL_HEIGHT/2 + rangeAbove * WALL_HEIGHT),
                    world, true, speed));
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

            movingWalls.add(new MovingWall(j * WALL_WIDTH + (width-1)*WALL_WIDTH/2, -i * WALL_HEIGHT - (height-1)*WALL_WIDTH/2,
                    width * WALL_WIDTH, height * WALL_HEIGHT,
                    new Vector2(j * WALL_WIDTH + (width-1)*WALL_WIDTH/2 - rangeLeft * WALL_WIDTH, j * WALL_WIDTH + (width-1)*WALL_WIDTH/2 + rangeRight * WALL_WIDTH),
                    new Vector2(0, 0),
                    world,false, speed));
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

    /**
     * Step game forward
     * @param delta
     */
    public void doLogic(float delta) {

        if (!gamePaused) {
            if (countDown > 0) countDown -= delta;
            else {
                if (score < 1 && !explosionBegun) {
                    explosions.add(new Explosion(character.getX(), character.getY(), EXPLOSION_DURATION));
                    explosionBegun = true;
                    explosionEvent = true;
                    shipGone = true;
                    world.destroyBody(character.getBody());
                    System.out.println("Beginning explosion");
                }

                for (Iterator<Explosion> iter = explosions.iterator(); iter.hasNext(); ) {
                    Explosion exp = iter.next();
                    if (exp.done()) iter.remove();
                }

                if (score < 1 && explosions.isEmpty() || restart) {
                    System.out.println("explosions: " + explosions + "\nrestart = " + restart);
                    healthDepleted();
                    return;
                }

                float gravFieldDirection;
                for (ForceCharPair pair : activeGravCharPairs) {
                    gravFieldDirection = pair.force.getRotation() * (float) Math.PI / 180;
                    pair.character.applyForce((float) (200 * Math.cos(gravFieldDirection)), (float) (200 * Math.sin(gravFieldDirection)));
                }

                AICharacter enemy;
                for (Iterator<AICharacter> iter = enemies.iterator(); iter.hasNext(); ) {
                    enemy = iter.next();
                    if (!enemy.isPursuing() && enemy.getBody().getPosition().dst(character.getBody().getPosition()) < AI_ACTIVATION_DISTANCE) {
                        enemy.pursue(character);
                    }
                    if (enemy.isExploding()) {
                        iter.remove();
                        world.destroyBody(enemy.getBody());
                    }
                    else {
                        enemy.step(delta);
                    }
                }

                doPhysicsStep(delta);

            }
        }
    }

    private void doPhysicsStep(float deltaTime) {
        float frameTime = Math.min(deltaTime, 0.25f);

        for (Explosion exp : explosions) {
            exp.step(deltaTime);
        }
        for (MovingWall wall : movingWalls) {
            wall.step(deltaTime);
        }

        accumulator += frameTime;
        while (accumulator >= 1 / 45f) {

            world.step(1 / 45f, 6, 2);

            if (score > 0 && !levelFinished) score--;

            accumulator -= 1 / 45f;
        }
    }

    public void setGravity(float x, float y) {
        character.getBody().applyForceToCenter(x, y, true);
    }

    public float getWallCrash() {
        float lastCrashForce = crashForce;
        crashForce = 0;
        if (lastCrashForce > 0) System.out.println("lastCrashForce = " + lastCrashForce);
        return lastCrashForce;
    }

    public boolean getExplosionEvent() {
        if (explosionEvent) {
            explosionEvent = false;
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

    public GameCharacter getCharacter() {
        return character;
    }

    public List<Wall> getWalls() {
        return walls;
    }

    public List<GravField> getGravFields() {
        return gravFields;
    }

    public List<MovingWall> getMovingWalls() {
        return movingWalls;
    }

    public World getBox2dWorld() {
        return world;
    }

    public List<AICharacter> getEnemies() {
        return enemies;
    }

    public List<Explosion> getExplosions() {
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


    private class ForceCharPair {
        GravField force;
        GameCharacter character;
        ForceCharPair(GravField force, GameCharacter character) {
            this.force = force;
            this.character = character;
        }
    }

    /*
     * ForceListener is a ContactListener that handles all contact events between
     */
    private class ForceListener implements ContactListener {


        @Override
        public void endContact(Contact contact) {
            Fixture forceFix;
            Fixture charFix;
            if (contact.getFixtureA().isSensor()) {
                forceFix = contact.getFixtureA();
                charFix = contact.getFixtureB();
            }
            else if (contact.getFixtureB().isSensor()) {
                forceFix = contact.getFixtureB();
                charFix = contact.getFixtureA();
            }
            else return;

            com.samwagg.gravity.main_game_module.game.game_objects.GravField field = (com.samwagg.gravity.main_game_module.game.game_objects.GravField) forceFix.getUserData();
            field.unlight();

            ForceCharPair pair;
            for (Iterator<ForceCharPair> iter = activeGravCharPairs.iterator(); iter.hasNext(); ) {
                pair = iter.next();
                if (pair.force.equals(forceFix.getUserData()) && pair.character.equals(charFix.getUserData())) {
                    iter.remove();
                }
            }
        }

        @Override
        public void beginContact(Contact contact) {

            Fixture fixA = contact.getFixtureA();
            Fixture fixB = contact.getFixtureB();

            Fixture forceFix;
            Fixture forcedCharFix;

            Fixture contactedAIFix;

            if (fixA.getUserData().getClass().equals(GravField.class)) {
                forceFix = contact.getFixtureA();
                forcedCharFix = contact.getFixtureB();
            }
            else if (fixB.getUserData().getClass().equals(GravField.class)) {
                forceFix = contact.getFixtureB();
                forcedCharFix = contact.getFixtureA();

            }
            else if (fixA.getUserData().getClass().equals(FinishSensor.class) && !fixB.getUserData().getClass().equals(AICharacter.class) ||
                    fixB.getUserData().getClass().equals(FinishSensor.class) && !fixA.getUserData().getClass().equals(AICharacter.class)) {
                levelFinished = true;
                onLevelCompleted();
                return;
            }
            else return;

            GravField field = (GravField) forceFix.getUserData();
            field.light();
            activeGravCharPairs.add(new ForceCharPair( (GravField) forceFix.getUserData(), (GameCharacter) forcedCharFix.getUserData()));
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
            else if (fixA.getUserData().getClass().equals(AICharacter.class)) {
                AICharacter aiChar = (AICharacter) fixA.getUserData();
                Vector2 normal = contact.getWorldManifold().getNormal().nor();
                Vector2 impulseToApply = normal.scl(1000f);
                System.out.println("ai collision " + impulseToApply);
                explosions.add(new Explosion(aiChar.getX(), aiChar.getY(), EXPLOSION_DURATION));
                explosionEvent = true;
                aiChar.explode();
                fixB.getBody().applyLinearImpulse(impulseToApply, fixB.getBody().getLocalCenter(), true);

                if (fixB.getUserData().getClass().equals(GameCharacter.class)) {
                    score -= 200;
                }
                else if (fixB.getUserData().getClass().equals(AICharacter.class)) {
                    ((AICharacter) fixB.getUserData()).explode();
                }


            }
            else if (fixB.getUserData().getClass().equals(AICharacter.class)) {
                AICharacter aiChar = (AICharacter) fixB.getUserData();
                Vector2 normal = contact.getWorldManifold().getNormal().nor();
                Vector2 impulseToApply = normal.scl(-1000f);
                System.out.println("ai collision " + impulseToApply);
                explosions.add(new Explosion(aiChar.getX(), aiChar.getY(), EXPLOSION_DURATION));
                explosionEvent = true;
                aiChar.explode();
                fixA.getBody().applyLinearImpulse(impulseToApply, fixA.getBody().getLocalCenter(), true);

                if (fixA.getUserData().getClass().equals(GameCharacter.class)) {
                    score -= 200;
                }
                else if (fixA.getUserData().getClass().equals(AICharacter.class)) {
                    ((AICharacter) fixA.getUserData()).explode();
                }
            }
        }
    }

}
