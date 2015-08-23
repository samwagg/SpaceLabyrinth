package com.samwagg.gravity.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.samwagg.gravity.GravityGame;
//import com.samwagg.gravity.Settings;

public class DesktopLauncher {
	public static void main (String[] arg) {
		
        Settings settings = new Settings();
        settings.maxWidth = 1024;
        settings.maxHeight = 1024;

//        settings.duplicatePadding = true;
       
//        settings.wrapX = TextureWrap.ClampToEdge;
        settings.wrapY = TextureWrap.Repeat;
        settings.wrapX = TextureWrap.Repeat;
//       
        settings.alphaThreshold=30;
        //float scales[] = {.5f};
        //settings.scale = scales; 
//        settings.edgePadding = true;
        settings.filterMag = TextureFilter.Linear;
        settings.filterMin = TextureFilter.Linear;
        settings.useIndexes = true;
        TexturePacker.process(settings, "images", "../android/assets", "pack");
       
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new GravityGame(), config);
		
		

	}
}
