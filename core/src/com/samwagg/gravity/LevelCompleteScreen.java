//package com.samwagg.gravity;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Screen;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.OrthographicCamera;
//import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.math.Vector3;
//
//public class LevelCompleteScreen implements Screen {
//
//	
//	private final GravityGame game;
//	private OrthographicCamera camera;
//	
//	private int level;
//	private int score;
//	
//	private final Vector2 VIEW_DIM = new Vector2(400, 240);
//	
//	private final Vector2 LEV_COMPLETE = new Vector2(VIEW_DIM.x*.5f - VIEW_DIM.x*.15f, VIEW_DIM.y);
//	private final Vector2 SCORE = new Vector2(VIEW_DIM.x*.5f - VIEW_DIM.x*.08f, VIEW_DIM.y - VIEW_DIM.y/10);
//	private final Vector2 CONTINUE = new Vector2(VIEW_DIM.x*.5f - VIEW_DIM.x*.085f, VIEW_DIM.y - VIEW_DIM.y*.6f);
//	private final Vector2 RETRY = new Vector2(VIEW_DIM.x*.5f - VIEW_DIM.x*.09f, VIEW_DIM.y - VIEW_DIM.y*.4f);
//
//	
//	
//	public LevelCompleteScreen(final GravityGame game, int level, int score) {
//		this.game = game;
//		this.score = score;
//		camera = new OrthographicCamera();
//		camera.setToOrtho(false, VIEW_DIM.x, VIEW_DIM.y);
//		
//		this.level = level;
//		
//	}
//
//
//	@Override
//	public void show() {
//		// TODO Auto-generated method stub
//	}
//
//	@Override
//	public void render(float delta) {
//        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        
//        camera.update();
//        game.batch.setProjectionMatrix(camera.combined);
//        
//        game.batch.begin();
//        game.font.draw(game.batch, "Level " + level + " Completed" , LEV_COMPLETE.x, LEV_COMPLETE.y);
//        game.font.draw(game.batch, "Score: " + score, SCORE.x, SCORE.y);
//        game.font.draw(game.batch, "Continue", CONTINUE.x, CONTINUE.y);
//        game.font.draw(game.batch, "Try Again", RETRY.x, RETRY.y);
//        
//
//        game.batch.end();
//        
//       // game.shapeRenderer.begin();
//        //game.shapeRenderer.rect(x, y, width, height);
//        
//        if (Gdx.input.isTouched()) {
//        	Vector3 touchPos = new Vector3();
//        	touchPos.x = Gdx.input.getX();
//        	touchPos.y = Gdx.input.getY();
//        	camera.unproject(touchPos);
//        	
//        	if (touchPos.y > CONTINUE.y - 50 && touchPos.y < CONTINUE.y) {
//        		game.setScreen(new GravityGameScreen(game, level + 1));
//        		dispose();
//        	}
//        	else if (touchPos.y > RETRY.y - 50 && touchPos.y < RETRY.y) {
//        		game.setScreen(new GravityGameScreen(game, level));
//        		dispose();
//        	}
//        }
//        
//		
//	}
//
//	@Override
//	public void resize(int width, int height) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void pause() {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void resume() {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void hide() {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void dispose() {
//		// TODO Auto-generated method stub
//		
//	}
//}
