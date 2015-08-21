package com.samwagg.gravity;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.samwagg.gravity.ai.EnemySteeringAgent;
import com.samwagg.gravity.objects.AntiGravButton;
import com.samwagg.gravity.objects.Explosion64;
import com.samwagg.gravity.objects.FinishSensor;
import com.samwagg.gravity.objects.GameCharacter;
import com.samwagg.gravity.objects.GravField;
import com.samwagg.gravity.objects.Map;
import com.samwagg.gravity.objects.Map.GameTile;
import com.samwagg.gravity.objects.MovingWall;
import com.samwagg.gravity.objects.Wall;

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
	
	private boolean displayDialog;
	private boolean optionsClicked;
	
	private boolean restart;
	
	private ExtendViewport extViewport;
	
	private FPSLogger logger = new FPSLogger();

	private Texture wallTex;
	private float x;
	private float y;

		

	private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();

	/**
	 * Set up camera, physics options, VectorSetter UI component, 
	 * and load level 1
	 * 
	 * @param game the game instance
	 */
	public GravityGameScreen(final GravityGame game, int level) {
		this.game = game;
		
		Gdx.graphics.setVSync(true);
		
		WORLD = new World(new Vector2(0,0), true);
		
		wallTex = new Texture(Gdx.files.internal("wall.png"));
		wallTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1000, 600);
		extViewport = new ExtendViewport(1000,600,camera);
		
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
		
		multiPlex = new InputMultiplexer();
		multiPlex.addProcessor(vSetter.getInputProcessor());

		try {
			map = new Map(level);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		background = new Texture(Gdx.files.internal("Space-02.png"));

		genGameObjects();
		WORLD.setContactListener(new ForceListener());
		
		score = map.getInitScore(level);
		startScore = score;
		this.level = level;
		levelFinished = false;
		
		//ScalingViewport viewport = new ScalingViewport(Scaling.fill,800,480);
	    stage = new Stage();
	    
	    Gdx.input.setInputProcessor(stage);
	    
	    table = new Table();
	    table.setFillParent(true);
	    stage.addActor(table);
	    skin = new Skin(Gdx.files.internal("uiskin.json"));
	    table.setSkin(skin); 
	    
	    countDownLabel = new Label(Integer.toString((int)countDown),skin);
	    countDownLabel.setHeight(500);
	    countDownLabel.setWidth(500);
	    countDownLabel.setFontScale(1.5f);
	    table.add(countDownLabel);
	    
	    optionsTex = new Texture(Gdx.files.internal("options.png"));


	}

	private void genGameObjects() {
		float wallWidth = Wall.WALL_TEXT.getWidth();
		float wallHeight = Wall.WALL_TEXT.getHeight();
		walls = new ArrayList<Wall>();
		gravFields = new ArrayList<GravField>();
		endSensors = new ArrayList<FinishSensor>();
		
		float mapHeight = map.getTileArray().length;
		float mapWidth  = map.getTileArray()[0].length;
		worldWidth = mapWidth * wallWidth;
		worldHeight = mapHeight * wallHeight;

		
		float xPos;
		float yPos;
		Map.GameTile currTile;
		for (int i = 0; i < mapHeight; i++) {
			
			for (int j = 0; j < mapWidth; j++) {

				xPos = j * wallWidth;
				yPos = - i * wallHeight;
				System.out.println("xpos = " + xPos + " and ypos = " + yPos);
				currTile = map.getTileArray()[i][j];
				if (currTile == null) continue;

				switch (currTile) {
				case WALL: 
					Rectangle block = extendTile(i, i, j, j, currTile, map.getTileArray()); 
					walls.add(new Wall(xPos, yPos - wallHeight*(block.height-1), block.width*wallWidth, block.height*wallHeight, WORLD));
					break;
				case START: character = new GameCharacter(xPos, yPos, WORLD); System.out.println("i = " + i + " and j = " + j);character.updatePosition(); System.out.println("here at char"); break;
				case FORCE_RIGHT: gravFields.add(new GravField(xPos, yPos, 0, WORLD)); break;
				case FORCE_UP: gravFields.add(new GravField(xPos, yPos, 90, WORLD)); break;
				case FORCE_LEFT: gravFields.add(new GravField(xPos, yPos, 180, WORLD)); break;
				case FORCE_DOWN: gravFields.add(new GravField(xPos, yPos, 270, WORLD)); break;
				case END: endSensors.add(new FinishSensor(xPos, yPos, WORLD)); break;
				case AI_START: enemies.add(new EnemySteeringAgent(xPos, yPos, WORLD, character)); break;
				default: ;// Do nothing
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
		
		x = character.getScreenX();
		y = character.getScreenY();
	}
	
	private void makeMovBlock(int i, int j, int width, int height, float speed, float wallWidth, float wallHeight) {
		if (	map.getTileArray()[i+height][j] == Map.GameTile.RANGE
				|| map.getTileArray()[i-1][j] == Map.GameTile.RANGE
				|| map.getTileArray()[i+height][j] == Map.GameTile.END_RANGE
				|| map.getTileArray()[i-1][j] == Map.GameTile.END_RANGE) {
			
			int rangeAbove = 0;
			int rangeBelow = 0;
			
			int k = 1;
			while (true) {
				if (map.getTileArray()[i+height-1+k][j] == Map.GameTile.RANGE) {
					rangeBelow += 1;
				}
				else if (map.getTileArray()[i+height-1+k][j] == Map.GameTile.END_RANGE) {
					rangeBelow += 1;
					break;
				}
				else break;
				k++;
			}
			
			k = 1;
			while (true) {
				if (map.getTileArray()[i-k][j] == Map.GameTile.RANGE) {
					rangeAbove += 1;
				}
				else if (map.getTileArray()[i-k][j] == Map.GameTile.END_RANGE) {
					rangeAbove += 1;
					break;
				}
				else break;
				k++;
			}
			
			movingWalls.add(new MovingWall(j*wallWidth, -i*wallHeight-(height-1)*wallHeight, width*wallWidth, height*wallHeight,  new Vector2(0,0), new Vector2(-i*wallHeight - rangeBelow * wallHeight -(height-1)*wallHeight,-i*wallHeight + rangeAbove * wallHeight - (height-1)*wallHeight ), WORLD, true, speed));					

			
			
		}
		
		if (	map.getTileArray()[i][j+width] == Map.GameTile.RANGE
				|| map.getTileArray()[i][j-1] == Map.GameTile.RANGE
				|| map.getTileArray()[i][j+width] == Map.GameTile.END_RANGE
				|| map.getTileArray()[i][j-1] == Map.GameTile.END_RANGE) {
			int rangeRight = 0;
			int rangeLeft = 0;
			
			int k = 1;
			while (true) {
				if (map.getTileArray()[i][j+width-1+k] == Map.GameTile.RANGE) {
					rangeRight += 1;
				}
				else if (map.getTileArray()[i][j+width-1+k] == Map.GameTile.END_RANGE) {
					rangeRight += 1;
					break;
				}
				else break;
				k++;
			}
			
			k = 1;
			while (true) {
				if (map.getTileArray()[i][j-k] == Map.GameTile.RANGE) {
					rangeLeft += 1;
				}
				else if (map.getTileArray()[i][j-k] == Map.GameTile.END_RANGE) {
					rangeLeft += 1;  
					break;
				}
				else break;
				k++;
			}
			
			movingWalls.add(new MovingWall(j*wallWidth, -i*wallHeight - (height-1)*wallHeight, width*wallWidth, height*wallHeight,  new Vector2(j*wallWidth - rangeLeft * wallWidth, j*wallWidth + rangeRight  * wallWidth), new Vector2(0,0), WORLD, false, speed));					
			
		}
		

	}
	
	
	public Rectangle extendTile(int iLow, int iHigh, int jLow, int jHigh, GameTile type, GameTile[][] map) {
		
		boolean expandableRight = true, expandableLeft = true, expandableUp = true, expandableDown = true;
		
		for (int i = iLow; i <= iHigh; i++) {
			if (expandableRight && !(jHigh+1 < map[0].length && map[i][jHigh+1] == type)) {
				expandableRight = false;
				
			}
			if (expandableLeft && !(jLow-1 >= 0 && map[i][jLow-1] == type)) {
				expandableLeft = false;
			}
		}
		
		if (expandableRight) jHigh++;
		if (expandableLeft)  jLow--;
		
		for (int j = jLow; j <= jHigh; j++) {
			if (expandableUp && !(iHigh+1 < map.length && map[iHigh+1][j] == type)) {
				expandableUp = false;
			}
			if (expandableDown && !(iLow-1 >= 0 && map[iLow-1][j] == type)) {
				expandableDown = false;
			}
		}
		
		if (expandableUp)    iHigh++;
		if (expandableDown)  iLow--;
		
		
		if (!expandableUp && !expandableDown && !expandableRight && !expandableLeft) {
			for (int i = iLow; i <= iHigh; i++) {
				for (int j = jLow; j <= jHigh; j++ ) {
					map[i][j] = Map.GameTile.EMPTY;
				}
			}
			
			return new Rectangle(jLow, iLow, jHigh-jLow + 1, iHigh-iLow + 1);
		} else {
			//System.out.println("Recurse");
			return extendTile(iLow, iHigh, jLow, jHigh, type, map);
		}
		
	}

	@Override
	public void render(float delta) {
		
		logger.log();
		Gdx.gl.glClearColor(0, 0, .3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		

		
		if (levelFinished) {
			
//			stage = new Stage();
//			Table table = new Table();
//			table.setFillParent(true);
//
//			stage.addActor(table);
//		 
//			//game.setScreen(new LevelCompleteMenu(game, level, (int)score));	
//			Gdx.input.setInputProcessor(stage);
//			Label label = new Label("Level Complete",skin);
//			label.scaleBy(.5f);
//			Table finishedDialog = new Table(skin);
//			finishedDialog.add("Level Complete");
//			finishedDialog.row();
//			finishedDialog.add("Score: " + (int)score);
//			finishedDialog.row();
//			finishedDialog.add("Best: " + game.readHighScore(level));
//			finishedDialog.row();
//			Button continueButton = new Button(skin);
//			continueButton.add("Continue");
//			
//			game.writeHighScore(level, (int) score);
//			
//			
//			continueButton.addListener(new EventListener() {
//
//				@Override
//				public boolean handle(Event event) {
//					// TODO Auto-generated method stub
//					game.setScreen(new GravityGameScreen(game, level + 1));					
//					return true;
//				}
//				
//			});
//			finishedDialog.add(continueButton);
//			table.add(finishedDialog);
			game.setScreen( new  LevelCompleteMenu(game, level, (int) score) );
			displayDialog = true;
		}
		
		if (optionsClicked) {
			
			stage = new Stage();
			Container<HorizontalGroup> container = new Container<HorizontalGroup>();
			Gdx.input.setInputProcessor(stage);

			container.setFillParent(true);
			HorizontalGroup group = new HorizontalGroup();
			
			ImageButton resumeButton = new ImageButton(new Image(new Texture(Gdx.files.internal("resume.png"))).getDrawable());
			ImageButton restartButton = new ImageButton(new Image(new Texture(Gdx.files.internal("restart.png"))).getDrawable());
			ImageButton menuButton = new ImageButton(new Image(new Texture(Gdx.files.internal("menu.png"))).getDrawable());
			
			resumeButton.addListener(new EventListener() {

				@Override
				public boolean handle(Event event) {
					displayDialog = false;
					return true;
				}
				
			});
			
			restartButton.addListener(new EventListener() {

				@Override
				public boolean handle(Event event) {
					restart = true;
					return true;
				}
				
			});
			
			menuButton.addListener(new EventListener() {
				
				@Override
				public boolean handle(Event event) {
					game.mainMenuStartPressed(getInstance());
					return true;
				}
				
			});

			stage.addActor(container);
			group.addActor(resumeButton);
			resumeButton.padRight(20);
			group.addActor(restartButton);
			restartButton.padRight(20);

			group.addActor(menuButton);
			menuButton.padRight(20);

//			Label label = new Label("Game Paused",skin);
//			label.scaleBy(.5f);
//			Table finishedDialog = new Table(skin);
//			finishedDialog.add("Game Paused");

			container.setActor(group);
			container.bottom();
		    optionsClicked = false;
			displayDialog = true;
			
		}
				
			game.batch.setProjectionMatrix(camera.combined);
			game.batch.begin();
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			
			
			tiledBackground.draw(game.batch, -camera.viewportWidth, -worldHeight - camera.viewportHeight, worldWidth+2*camera.viewportWidth, worldHeight + 2*camera.viewportHeight );
	
			for (GravField field : gravFields) {
				field.getSprite().draw(game.batch);
			}
			
			if (!shipGone) {
				 character.getSprite().draw(game.batch);
				 character.getSprite().rotate(.2f);
			 }
			
			for (Wall wall : walls) {
				
				wall.draw(game.batch, camera);
	
			} 
	
			for (MovingWall wall : movingWalls) {
				wall.draw(game.batch, camera);
			}
			
			
			for (EnemySteeringAgent enemy : enemies) {
				game.batch.draw(enemy.getGameObject().getTexture(), enemy.getGameObject().getScreenX(), enemy.getGameObject().getScreenY());
			}
			
			if (score == 0 && !explosionBegun) {
				explosions.add(new Explosion64(character.getScreenX(), character.getScreenY()));
				explosionBegun = true;
			}
			
			
			for (Explosion64 exp : explosions) {
				if (shipGone) {
					if (exp.drawNextFrame(game.batch)) finishedExplosions.add(exp);
				} else {
					if (exp.drawNextFrame(game.batch, character.getScreenX(), character.getScreenY())) finishedExplosions.add(exp);
	
				}
				if (exp.halfDone()) shipGone = true;
			}
			
			
			for (Explosion64 exp : finishedExplosions) {
				explosions.remove(exp);
			}
			

			//game.batch.draw(wallTex, this.x, this.y);
			game.batch.setProjectionMatrix(staticCamera.combined);
			game.batch.draw(optionsTex, staticCamera.viewportWidth - optionsTex.getWidth(), staticCamera.viewportHeight - optionsTex.getHeight());	
			
			game.batch.end();
			
			if (countDown > 0) {
				stage.act(delta);
				stage.draw();
				countDownLabel.setText(Integer.toString((int) countDown+1));
			}
			else if (!displayDialog) Gdx.input.setInputProcessor(multiPlex);
			
			if (score == 0 && explosions.isEmpty() || restart) {
				game.setScreen(new GravityGameScreen(game, level));
				return;
			}
			
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			game.shapeRenderer.begin();
			game.shapeRenderer.setProjectionMatrix(staticCamera.combined);
			//game.shapeRenderer.setProjectionMatrix(camera.);
			game.shapeRenderer.setColor(1-1/(startScore/score),0f,score/startScore,.6f);
			game.shapeRenderer.set(ShapeType.Filled);
	
			game.shapeRenderer.rect(0, 0,  (score/startScore) * staticCamera.viewportWidth, 20);
			vSetter.render(game.shapeRenderer, camera);
			
			game.shapeRenderer.end();
			Gdx.gl.glDisable(GL20.GL_BLEND);
	
			gravVect.x = vSetter.getXComponent() * .05f;
			gravVect.y = vSetter.getYComponent() * .05f;
			WORLD.setGravity(gravVect);
			
			float gravFieldDirection;
			for (GravField field : currentGravFields) {
				gravFieldDirection = field.getRotation()*(float)Math.PI/180;
				character.getBody().applyForceToCenter((float)(200*Math.cos(gravFieldDirection)), (float)(200*Math.sin(gravFieldDirection)), true);
			}			
		
			if (countDown > 0) {
				countDown -= delta;
			} else if (!levelFinished && !displayDialog) {
				doPhysicsStep(delta);
			}
			
			if (displayDialog) {
				stage.act();
				stage.draw();
			} 
			
			if (Gdx.input.justTouched()) {
				Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(),0);
				staticCamera.unproject(touchPos);

				if (touchPos.x > staticCamera.viewportWidth - optionsTex.getWidth() && touchPos.y > staticCamera.viewportHeight - optionsTex.getHeight()) {
					if (displayDialog) displayDialog = false;
					else optionsClicked = true;
				}
			}
			
			//camera.update();
			camera.position.set(character.getScreenX(), character.getScreenY(), 0);
			camera.update();


		
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
//		camera.translate(character.getScreenX() - camera.position.x,
//				character.getScreenY() - camera.position.y);


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
		//WORLD.dispose();
		//optionsTex.dispose();
		//stage.dispose();
		//skin.dispose();
		
	}
	
	public GravityGameScreen getInstance() {
		return this;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resize(int width, int height) {
		extViewport.update(width, height);

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		dispose();
		// TODO Auto-generated method stub

	}
	
	public class ForceListener implements ContactListener {
		@Override
		public void endContact(Contact contact) {
			Fixture forceFix;
			Fixture charFix;
			if (contact.getFixtureA().isSensor()) {
				forceFix = contact.getFixtureA();
				charFix = contact.getFixtureB();
			} else if (contact.getFixtureB().isSensor()){
				forceFix = contact.getFixtureB();
				charFix = contact.getFixtureA();
			} else return;

			GravField field = (GravField) forceFix.getUserData();
			field.unlight();
			currentGravFields.remove(field);
			

		}

		@Override
		public void beginContact(Contact contact) {
			
			Fixture fixA = contact.getFixtureA();
			Fixture fixB = contact.getFixtureB();
			
			Fixture forceFix;
			Fixture charFix;
			
			if (fixA.getUserData().getClass().equals(GravField.class)) {
				forceFix = contact.getFixtureA();
				charFix = contact.getFixtureB();
			} else if (fixB.getUserData().getClass().equals(GravField.class)){
				forceFix = contact.getFixtureB();
				charFix = contact.getFixtureA();
			} else if (fixA.getUserData().getClass().equals(GameCharacter.class) &&
						fixB.getUserData().getClass().equals(Wall.class) ||
						fixA.getUserData().getClass().equals(Wall.class) &&
						fixB.getUserData().getClass().equals(GameCharacter.class)) {
				score = score - GravityGame.CONTACT_DECREMENT < 0? 0 : score - GravityGame.CONTACT_DECREMENT;
				return;
			}else if (fixA.getUserData().getClass().equals(GameCharacter.class) &&
					fixB.getUserData().getClass().equals(MovingWall.class) ||
					fixA.getUserData().getClass().equals(MovingWall.class) &&
					fixB.getUserData().getClass().equals(GameCharacter.class)) {
			score = score - GravityGame.CONTACT_DECREMENT < 0? 0 : score - GravityGame.CONTACT_DECREMENT;
			return;
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
		public void preSolve(Contact contact, Manifold oldManifold)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void postSolve(Contact contact, ContactImpulse impulse)
		{
			// TODO Auto-generated method stub

		}
	}
	
	

}
