package com.samwagg.gravity.main_game_module.game.game_objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.samwagg.gravity.Constants;

public class  GravField extends GameObject {

//	public static final Texture G_FIELD_TEXT_UNLIT = new Texture(Gdx.files.internal("arrow_unlit.png"));
//	public static final Texture G_FIELD_TEXT_LIT = new Texture(Gdx.files.internal("arrow_lit.png"));

    private final float rotation;
    private boolean lit;

    public GravField(float initX, float initY, float width, float height, float rotation, World world) {
        super(initX, initY, width, height, world);
//		UNLIT_SPRITE = sprite;
//		UNLIT_SPRITE.setX(screenX);
//		UNLIT_SPRITE.setY(screenY);
//		UNLIT_SPRITE.setRotation(rotation);
//
//		LIT_SPRITE = constants.createArrowLit();
//		LIT_SPRITE.setX(screenX);
//		LIT_SPRITE.setY(screenY);
//		LIT_SPRITE.setRotation(rotation);

        this.rotation = rotation;
        lit = false;
//		this.texture = G_FIELD_TEXT_UNLIT;
    }

    @Override
    public void step(float delta) {

    }

    @Override
    protected void setupBody(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.StaticBody;
        bodyDef.position.set(this.initX, this.initY);
        body = world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.isSensor = true;
        PolygonShape box = new PolygonShape();
        box.setAsBox(width * .5f, height * .5f);
        fixtureDef.shape = box;
        Fixture fixture = body.createFixture(fixtureDef);

        fixture.setUserData(this);
    }

    public void light() {
        lit = true;
    }

    public void unlight() {
        lit = false;
    }

    public boolean isLit() {
        return lit;
    }

    public float getRotation() {
        return rotation;
    }

}
