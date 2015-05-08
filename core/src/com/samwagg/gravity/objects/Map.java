package com.samwagg.gravity.objects;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Map {
	
	
	private GameTile[][] tileArray;
	
	private final int initScores[] = {2500, 2000, 1000,3750}; 
	
	public Map(int level) throws FileNotFoundException {
		MapReader reader = new MapReader("level" + level);
		tileArray = reader.readMap();
	}
	
	public GameTile[][] getTileArray() {
		return tileArray;
	}
	
	public int getInitScore(int level) {
		return initScores[level-1];
	}
	
	
	private class MapReader
	{
		
		private List<String> strList = new ArrayList<String>();
		private GameTile[][] gameMap;
		private File textLevel;
		private FileHandle mapFileHandle;
		
		private MapReader(String fileName) throws FileNotFoundException {
			mapFileHandle = Gdx.files.internal(fileName);
			
		}
		
		private GameTile[][] readMap() {
			String line;
			BufferedReader textReader = mapFileHandle.reader(200);
			try {
				while ((line = textReader.readLine()) != null) {
					strList.add(line);
				}
				
			} catch(IOException e) {
				System.err.println("Error reading map text file");
				e.printStackTrace();
			} finally {
				try {
					if (textReader != null) textReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			gameMap = new GameTile[strList.size()][strList.get(0).length()];
			
			char[] lineArray;
			for (int i = 0; i < strList.size(); i++) {
				lineArray = strList.get(i).toCharArray();
				for (int j = 0; j < lineArray.length; j++) {
					switch (lineArray[j]) {
					case 'x': gameMap[i][j] = GameTile.WALL; break;
					case '>': gameMap[i][j] = GameTile.FORCE_RIGHT; break;
					case '<': gameMap[i][j] = GameTile.FORCE_LEFT; break;
					case '^': gameMap[i][j] = GameTile.FORCE_UP; break;
					case '!': gameMap[i][j] = GameTile.FORCE_DOWN; break;
					case '1': gameMap[i][j] = GameTile.MOV_BLOCK1; break;
					case '2': gameMap[i][j] = GameTile.MOV_BLOCK2; break;
					case '3': gameMap[i][j] = GameTile.MOV_BLOCK3; break;
					case '4': gameMap[i][j] = GameTile.MOV_BLOCK4; break;
					case '5': gameMap[i][j] = GameTile.MOV_BLOCK5; break;
					case '6': gameMap[i][j] = GameTile.MOV_BLOCK6; break;
					case '7': gameMap[i][j] = GameTile.MOV_BLOCK7; break;
					case '8': gameMap[i][j] = GameTile.MOV_BLOCK8; break;
					case '9': gameMap[i][j] = GameTile.MOV_BLOCK9; break;
					case '-': gameMap[i][j] = GameTile.END_RANGE; break;
					case '|': gameMap[i][j] = GameTile.RANGE; break;
					case 's': gameMap[i][j] = GameTile.START; break;
					case 'e': gameMap[i][j] = GameTile.END; break;
					case 'a': gameMap[i][j] = GameTile.AI_START; break;
					default: gameMap[i][j] = GameTile.EMPTY;
					}
				}
			}
			return gameMap;
			
		}

	}
	


	public static enum GameTile {
		WALL, 
		EMPTY, 
		FORCE_LEFT(180), 
		FORCE_RIGHT(0), 
		FORCE_UP(90), 
		FORCE_DOWN (270), 
		MOV_BLOCK1(true, 1),
		MOV_BLOCK2(true, 2),
		MOV_BLOCK3(true, 3),
		MOV_BLOCK4(true, 4),
		MOV_BLOCK5(true, 5),
		MOV_BLOCK6(true, 6),
		MOV_BLOCK7(true, 7),
		MOV_BLOCK8(true, 8),
		MOV_BLOCK9(true, 9),
		RANGE, 
		END_RANGE, 
		START, 
		END, 
		AI_START;

		private float value;
		private boolean isMoveBlock;

		private GameTile(boolean isMovBlock, float value) {
			this.isMoveBlock = isMovBlock;
			this.value = value;
		}
		
		private GameTile(float value) {
			this.value = value;
			isMoveBlock = false;
		}

		private GameTile() {
			this.value = Float.NaN;
			isMoveBlock = false;
		}

		public float getAngle() {
			return value;
		}
		
		public float getSpeed() {
			if (isMoveBlock) return value;
			else return -1;
		}
		
		public boolean isMovBlock() {
			return isMoveBlock;
		}

	}

}
