package com.samwagg.gravity.main_game_module.game;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by sam on 9/1/16.
 */
public class ViewResources {

    private final TextureAtlas atlas;
//    private final Sprite charSprite;
    private final TextureAtlas.AtlasRegion wall;
    private final TextureRegion arrowLit;
    private final TextureRegion arrowUnlit;
    private final TextureRegion ship;
    private final TextureRegion enemyUnlit;
    private final TextureRegion enemyLit;

    public ViewResources(FileHandle pack, String shipTexture, String enemyUnlitTexture, String enemyLitTexture,
                         String wallTexture, String arrowLitTexture, String arrowUnlitTexture) {
        atlas = new TextureAtlas(pack);
        wall = atlas.findRegion(wallTexture);
        arrowLit = atlas.findRegion(arrowLitTexture);
        arrowUnlit = atlas.findRegion(arrowUnlitTexture);
        ship = atlas.findRegion(shipTexture);
        enemyUnlit = atlas.findRegion(enemyUnlitTexture);
        enemyLit = atlas.findRegion(enemyLitTexture);
    }

    public TextureRegion getWall() {
        return wall;
    }

    public TextureRegion getArrowLit() {
        return arrowLit;
    }

    public TextureRegion getArrowUnlit() {
        return arrowUnlit;
    }

    public TextureRegion getShip() { return ship; }

    public TextureRegion getEnemyUnlit() { return enemyUnlit; }

    public TextureRegion getEnemyLit() {
        return enemyLit;
    }

    //    public Sprite createLitArrow() {
//        Sprite sprite = atlas.createSprite("arrow_lit");
//        sprite.setSize(tileSize, tileSize);
//        sprite.setOriginCenter();
//        return sprite;
//    }
//
//    public Sprite createUnlitArrow() {
//        Sprite sprite = atlas.createSprite("arrow_unlit");
//        sprite.setSize(tileSize, tileSize * 2);
//        sprite.setOriginCenter();
//        return sprite;
//    }
}
