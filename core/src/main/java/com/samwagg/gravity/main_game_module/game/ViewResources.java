package com.samwagg.gravity.main_game_module.game;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Encapsulates the graphical representations of game objects
 */
public class ViewResources {

    private final TextureAtlas atlas;
    private final TextureAtlas.AtlasRegion wall;
    private final TextureRegion arrowLit;
    private final TextureRegion arrowUnlit;
    private final TextureRegion ship;
    private final TextureRegion enemyUnlit;
    private final TextureRegion enemyLit;

    /**
     * @param pack atlas file holding all resources referred to by the rest of the parameters
     * @param shipTexture atlas key for player character ship
     * @param enemyUnlitTexture atlas key for inactive enemy ship
     * @param enemyLitTexture atlas key for active enemy ship
     * @param wallTexture atlas key for wall
     * @param arrowLitTexture atlas key for activated force field
     * @param arrowUnlitTexture atlas key for inactive force field
     */
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
}
