package com.samwagg.gravity.main_game_module.game.game_objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class AntiGravButton implements InputProcessor {

    private Texture image;
    private float screenX, screenY;
    private Camera camera;

    private boolean isTouched;

    public AntiGravButton(Camera camera) {
        image = new Texture(Gdx.files.internal("grav_button.png"));
        this.camera = camera;
        isTouched = false;
    }

    public void setScreenCoords(float screenX, float screenY) {
        this.screenX = screenX;
        this.screenY = screenY;
    }

    public Texture getImage() {
        return image;
    }

    public float getScreenX() {
        return screenX;
    }

    public float getScreenY() {
        return screenY;
    }

    public boolean getIsTouched() {
        return isTouched;
    }

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
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 pos = new Vector3(screenX, screenY, 0);
        camera.unproject(pos);
        if (pos.x > this.screenX && pos.x < this.screenX + image.getWidth() &&
                pos.y > this.screenY && pos.y < this.screenY + image.getHeight()) {
            isTouched = true;
            return true;
        }
        else {
            return false;
        }


    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // TODO Auto-generated method stub
        Vector3 pos = new Vector3(screenX, screenY, 0);
        camera.unproject(pos);
        if (pos.x > this.screenX && pos.x < this.screenX + image.getWidth() &&
                pos.y > this.screenY && pos.y < this.screenY + image.getHeight()) {
            isTouched = false;
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector3 pos = new Vector3(screenX, screenY, 0);
        camera.unproject(pos);
        if (pos.x > this.screenX && pos.x < this.screenX + image.getWidth() &&
                pos.y > this.screenY && pos.y < this.screenY + image.getHeight()) {

            return true;
        }
        else {
            return false;
        }
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
