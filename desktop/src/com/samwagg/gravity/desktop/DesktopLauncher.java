package com.samwagg.gravity.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.samwagg.gravity.GravityGame;
//import com.samwagg.gravity.Settings;

public class DesktopLauncher {
	public static void main (String[] arg) {
		
//        Settings settings = new Settings();
//        settings.maxWidth = 1024;
//        settings.maxHeight = 1024;
//        TexturePacker.process(settings, "images", "images", "pack");
        
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new GravityGame(), config);
		
		

	}
}
