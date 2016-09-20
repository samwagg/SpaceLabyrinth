package com.samwagg.gravity;

import com.badlogic.gdx.files.FileHandle;

import java.util.List;

/**
 * Created by sam on 9/14/16.
 */
public class Galaxy {

    private String galaxyId;
    private List<FileHandle> levels;
    private FileHandle lockedImageFile;
    private FileHandle unlockedImageFile;

    public Galaxy(String galaxyId, List<FileHandle> levels, FileHandle lockedImageFile, FileHandle unlockedImageFile) {
        this.galaxyId = galaxyId;
        this.levels = levels;
        this.lockedImageFile = lockedImageFile;
        this.unlockedImageFile = unlockedImageFile;
    }

    public String getGalaxyId() {
        return galaxyId;
    }

    public List<FileHandle> getLevels() {
        return levels;
    }

    public FileHandle getLockedImageFile() {
        return lockedImageFile;
    }

    public FileHandle getUnlockedImageFile() {
        return unlockedImageFile;
    }
}
