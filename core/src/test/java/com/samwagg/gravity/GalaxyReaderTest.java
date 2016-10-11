package com.samwagg.gravity;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.files.FileHandle;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Unit tests for GalaxyReader are in progress and not yet complete
 */
public class GalaxyReaderTest {

    private static final String VALID_GAL_DIR = "galaxies/valid_galaxy_7_levels";
    private static final String INVALID_GAL_DIR_MISSING_LEVEL = "galaxies/invalid_galaxy_missing_level";
    private static final String INVALID_GAL_DIR_MISSING_IMAGE = "galaxies/invalid_galaxy_missing_image";
    private static final String INVALID_GAL_DIR_MISSING_JSON = "galaxies/invalid_galaxy_missing_image";

    private  HeadlessApplication app = null;
    private Game mockGame = Mockito.mock(Game.class);

    /*
     * Necessary because using Gdx.files requires a running application
     */
    @Before
    public void before() {
        app = new HeadlessApplication(mockGame);
    }

    @After
    public void after() {
        app.exit();
        app = null;
    }

    @Test
    public void validGalaxyDirectoryShouldReadWithoutException() {
        GalaxyReader reader = new GalaxyReader();
        FileHandle handle = Gdx.files.internal(VALID_GAL_DIR);
        try {
            reader.readGalaxy(handle);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            fail("Expected no exception");
        }
    }
}
