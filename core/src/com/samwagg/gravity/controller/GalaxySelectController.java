//package com.samwagg.gravity.controller;
//
//import com.samwagg.gravity.GalaxySelectScreen;
//import com.samwagg.gravity.GravityGame;
//import com.samwagg.gravity.LevelSelectScreen;
//
//public class GalaxySelectController {
//	
//	GravityGame game;
//	
//	public GalaxySelectController(GravityGame game) {
//		this.game = game;
//	}
//	
//	public void showGalaxySelectScreen() {
//		game.setScreen(new GalaxySelectScreen(this, game.getGameState().galaxiesUnlocked));
//	}
//	
//	public void selectGalaxy(int galaxy) {
//		game.setScreen(new LevelSelectScreen(galaxy, game, game.getGameState()));
//	}
//
//}
