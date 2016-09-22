package com.samwagg.gravity;

import com.badlogic.gdx.files.FileHandle;

import java.util.List;

/**
 * Holds all the data for a Galaxy (level set)
 */
public class Galaxy {

    private String galaxyId;
    private List<FileHandle> levels;
    private FileHandle lockedImageFile;
    private FileHandle unlockedImageFile;

    /**
     * @param galaxyId unique identifier used for game progress persistence between game sessions
     * @param levels level files in the expected Space Labyrinth format and in order from low to high
     * @param lockedImageFile image representation of this galaxy for menus when it is locked (currently unplayable)
     * @param unlockedImageFile image representation of this galaxy for menus when it is unlocked
     */
    public Galaxy(String galaxyId, List<FileHandle> levels, FileHandle lockedImageFile, FileHandle unlockedImageFile) {
        this.galaxyId = galaxyId;
        this.levels = levels;
        this.lockedImageFile = lockedImageFile;
        this.unlockedImageFile = unlockedImageFile;
    }

    /**
     * @return unique identifier for galaxy
     */
    public String getGalaxyId() {
        return galaxyId;
    }

    /**
     * @return level files in order from low to high
     */
    public List<FileHandle> getLevels() {
        return levels;
    }

    /**
     * @return image representation of this galaxy for menus when it is locked (currently unplayable)
     */
    public FileHandle getLockedImageFile() {
        return lockedImageFile;
    }

    /**
     * @return image representation of this galaxy for menus when it is unlocked
     */
    public FileHandle getUnlockedImageFile() {
        return unlockedImageFile;
    }
}
