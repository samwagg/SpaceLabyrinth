package com.samwagg.gravity;


import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads galaxy data from filesystem and constructs a Galaxy object as an internal representation. Hence,
 * GalaxyReader defines the expected directory structure for galaxy data.
 */
public class GalaxyReader {

    private static final String GALAXY_META_JSON = "galaxy.json";
    private static final String LEVEL_FILE_BASE = "level";
    private static final String UNLOCKED_TEX_FILE = "unlocked.png";
    private static final String LOCKED_TEX_FILE = "locked.png";
    private static final String GALAXY_ID_NAME = "galaxy_id";

    private FileHandle galaxyDirectory;

    /**
     * @param galaxyDirectory directory containing the files that define a galaxy.
     * @return
     * @throws IOException when directory structure is unexpected or files malformed
     */
    public Galaxy readGalaxy(FileHandle galaxyDirectory) throws IOException {

        String galaxyId;
        List<FileHandle> levelFiles = new ArrayList<FileHandle>();
        FileHandle unlockedImageFile;
        FileHandle lockedImageFile;

        // Libgdx documentation is severely lacking description of error cases
        // TODO test what happens when file doesn't exist or json is illegal and implement appropriate handling
        // since I want to eventually be able to download new galaxies from the cloud and can't necessarily trust
        JsonReader jsonReader = new JsonReader();
        FileHandle metaFile = galaxyDirectory.child(GALAXY_META_JSON);
        if (!metaFile.exists()) throw new FileNotFoundException(galaxyDirectory.path() + "/" + GALAXY_META_JSON + " not found");
        JsonValue galaxyMeta = jsonReader.parse(metaFile);
        galaxyId = galaxyMeta.getString(GALAXY_ID_NAME);

        int i = 0;
        FileHandle level = galaxyDirectory.child(LEVEL_FILE_BASE + i);
        while (level.exists()) {
            levelFiles.add(level);
            i++;
            level = galaxyDirectory.child(LEVEL_FILE_BASE + i);
        }

        unlockedImageFile = galaxyDirectory.child(UNLOCKED_TEX_FILE);
        if (!unlockedImageFile.exists()) throw new FileNotFoundException(galaxyDirectory.path() + "/" + UNLOCKED_TEX_FILE + " not found");
        lockedImageFile = galaxyDirectory.child(LOCKED_TEX_FILE);
        if (!lockedImageFile.exists()) throw new FileNotFoundException(galaxyDirectory.path() + "/" + LOCKED_TEX_FILE + " not found");

        return new Galaxy(galaxyId, levelFiles, lockedImageFile, unlockedImageFile);
    }
}
