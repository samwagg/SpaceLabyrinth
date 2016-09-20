package com.samwagg.gravity.main_game_module.game.game_objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.samwagg.gravity.Constants;

public class Wall extends GameObject {

    public Wall(float initX, float initY, float width, float height, World world) {
        super(initX, initY, width, height, world);
    }

    @Override
    public void step(float delta) {
        // Wall does nothing
    }

    @Override
    protected void setupBody(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.StaticBody;
        bodyDef.position.set(this.initX, this.initY);
        PolygonShape brickBox = new PolygonShape();
        brickBox.setAsBox(width * .5f, height * .5f);
        //ystem.out.println("brickBox " + brickBox.ge + " " + brickBox.height);
        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = brickBox;

        body = world.createBody(bodyDef);
        Fixture fixture = body.createFixture(fixDef);
        fixture.setUserData(this);

        brickBox.dispose();
    }
}
