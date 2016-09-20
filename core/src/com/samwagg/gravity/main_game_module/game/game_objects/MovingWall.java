package com.samwagg.gravity.main_game_module.game.game_objects;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.samwagg.gravity.Constants;

public class MovingWall extends Wall {

    public float speed;

    private final boolean isVert; // moves vertically. If false, moves horizontally]
    private final Vector2 minMaxY;
    private final Vector2 minMaxX;

    public MovingWall(float initX, float initY, float physWidth, float physHeight, Vector2 minMaxX, Vector2 minMaxY, World world, Boolean isVert, float speed) {

        super(initX, initY, physWidth, physHeight, world);

        this.speed = speed;
        this.isVert = isVert;
        this.minMaxY = minMaxY;
        this.minMaxX = minMaxX;
    }

    @Override
    public void step(float delta) {
        move();
    }

    @Override
    protected void setupBody(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.KinematicBody;
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

    public void move() {

        if (isVert) {
            if (body.getLinearVelocity().y >= 0 && getY() < minMaxY.y) {
                body.setLinearVelocity(0, speed);
            }
            else if (getY() > minMaxY.x) {
                body.setLinearVelocity(0, -speed);
            }
            else {
                body.setLinearVelocity(0, speed);
            }
        }
        else {
            if (body.getLinearVelocity().x >= 0 && getX() < minMaxX.y) {
                body.setLinearVelocity(speed, 0);
            }
            else if (getX() > minMaxX.x) {
                body.setLinearVelocity(-speed, 0);
            }
            else body.setLinearVelocity(speed, 0);
        }
    }
}
