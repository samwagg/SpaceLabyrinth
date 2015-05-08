package com.samwagg.gravity;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * VectorSetter is a libgdx UI component intended as a game controller.
 * It allows the user to graphically (assuming it's rendered) set the
 * magnitude and direction of a vector. The VectorSetter only has magnitude
 * and direction when pressing down on the viewport. It is centered at the
 * location of the initial touch, and set based upon the drag direction and
 * length.
 * @author sam Waggoner
 *
 */
public class VectorSetter {

	private float magnitude;
	private float direction;
	private float xComponent;
	private float yComponent;
	
	Vector3 pos;
	private boolean onScreen;
	private VInputProcessor inputProcessor;
	
	private Camera camera;
	
	private final float innerRad = 10;
	
	private Vector3 touchDownPos;
	private Vector3 prevCamLoc; // used to track camera motion
	private Vector3 offset; // position offset of graphical object due to camera motion
	private Vector3 cameraTouchDownPos;
	
	/**
	 * Initialize VectorSetter to 0 magnitude, off-screen,
	 * and instantiate the VInputProcessor
	 * @param camera the camera this instance is associated with
	 */
	public VectorSetter(Camera camera) {
		magnitude = 0;
		direction = 0;
		onScreen = false;
		pos = new Vector3(0,0,0);
		inputProcessor = new VInputProcessor();
		
		prevCamLoc = new Vector3(0,0,0);
		offset = new Vector3(0,0,0);
		touchDownPos = new Vector3(0,0,0); 
		cameraTouchDownPos = new Vector3(0,0,0);
		this.camera = camera;
		
	}
	
	/**
	 * Render this object 
	 * @param renderer rendering must be done with a ShapeRenderer
	 * @param camera the camera where this VectorSetter will be displayed
	 */
	public void render(ShapeRenderer renderer, Camera camera) {
		if (onScreen) {

			
			renderer.setColor( 1, 1, 1, .3f );
			renderer.set(ShapeRenderer.ShapeType.Line);
			renderer.circle( pos.x, pos.y , magnitude + innerRad );
			renderer.set(ShapeRenderer.ShapeType.Filled);
			renderer.circle( pos.x , pos.y , magnitude *.1f);
			renderer.rect(pos.x , pos.y , .1f, 0, .2f, 1, magnitude, magnitude + innerRad, direction);
			
			
			//prevCamLoc = camera.position.cpy();
		}
	}
	
	/**
	 * Notify this object that camera has changed so it
	 * can update position
	 */
	public void translate(float x, float y) {
		pos.x += x;
		pos.y += y;
	}
	
	public float getMagnitude() {
		return magnitude;
	}
	
	public float getDirection() {
		return direction * (float) (180/Math.PI);
	}
	
	public float getXComponent() {
		return xComponent;
	}
	
	public float getYComponent() {
		return yComponent;
	}
	
	public InputProcessor getInputProcessor() {
		return inputProcessor;
	}
	
	
	/**
	 * Custom input processor associated with VectorSetter that
	 * listens for input and directly updates the VectorSetter
	 */
	public class VInputProcessor implements InputProcessor {

		Vector3 draggedPos = new Vector3();
		
		boolean isDown = false; 
		
		@Override
		public boolean keyDown(int keycode) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean keyUp(int keycode) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean keyTyped(char character) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean touchDown(int screenX, int screenY, int pointer,
				int button) {
			onScreen = true;
			pos.x = screenX;
			pos.y = screenY;
			
			camera.unproject(pos);
			
			isDown = true;
			//camera.project(pos);
			
			//touchDownPos.set(pos.x, pos.y, 0);
			//cameraTouchDownPos.set(camera.position.cpy());
			//offset.set(0,0,0);
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			// TODO Auto-generated method stub
			onScreen = false;
			magnitude = 0;
			xComponent = 0;
			yComponent = 0;
			isDown = false;
			return true;
		}

		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer) {
			// TODO Auto-generated method stub
			draggedPos.x = screenX;
			draggedPos.y = screenY;
			
			camera.unproject(draggedPos);

			
			magnitude = pos.dst(draggedPos);
			xComponent = draggedPos.x - pos.x;
			yComponent = draggedPos.y - pos.y;
			direction = (float) (MathUtils.atan2(yComponent, xComponent) * 180/Math.PI) - 90;
			return false;
		}

		@Override
		public boolean mouseMoved(int screenX, int screenY) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean scrolled(int amount) {
			// TODO Auto-generated method stub
			return false;
		}
		
	}
	
	
	
	
}
