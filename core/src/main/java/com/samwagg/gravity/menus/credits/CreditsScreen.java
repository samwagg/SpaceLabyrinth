package com.samwagg.gravity.menus.credits;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.samwagg.gravity.GravityGame;


public class CreditsScreen implements Screen, CreditsMenu {

    private Texture texture;

    private GravityGame game;
    private CreditsMenuListener listener;

    private OrthographicCamera camera;

    private Vector3 touchInput;

    public CreditsScreen(GravityGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        touchInput = new Vector3(0, 0, 0);
    }

    @Override
    public void show() {
        texture = new Texture(Gdx.files.internal("galaxy_complete.png"));
        // TODO Auto-generated method stub

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(texture, 0, 0, camera.viewportWidth, camera.viewportHeight);
        game.batch.end();

        camera.unproject(touchInput);

        if (Gdx.input.justTouched() && listener != null) listener.screenTouched();


        // TODO Auto-generated method stub

    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub

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
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

    @Override
    public void registerCreditsMenuListener(CreditsMenuListener listener) {
        this.listener = listener;
    }
}
