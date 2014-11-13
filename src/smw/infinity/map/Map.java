package smw.infinity.map;

import java.awt.Graphics2D;
import java.io.Serializable;

import smw.infinity.Scene;
import smw.infinity.Updatable;

public class Map extends Tileform implements Serializable
{
	private static final long serialVersionUID = 3467145046108184101L;
	public static final int MAP_WIDTH = Scene.SCENE_WIDTH / Tile.TILE_SIZE;
	public static final int MAP_HEIGHT = Scene.SCENE_HEIGHT / Tile.TILE_SIZE;
	
	public static byte INTERACTIVE_LAYER = 0;
	
	public Map()
	{
		tiles = new Tile[2][MAP_WIDTH][MAP_HEIGHT];
	}

	@Override
	public void update(long delta)
	{
		for(int l = 0; l < tiles.length; l++)
			for(int i = 0; i < tiles[l].length; i++)
				for(int j = 0; j < tiles[l][i].length; j++)
					if(tiles[l][i][j] != null && tiles[l][i][j] instanceof Updatable)
						((Updatable) tiles[l][i][j]).update(delta);
	}

	@Override
	public void drawToScreen(Graphics2D g2D, int x, int y)
	{
		//Draw non-interactive layers
		for(int l = 1; l < tiles.length; l++)
			for(int i = 0; i < tiles[l].length; i++)
				for(int j = 0; j < tiles[l][i].length; j++)
					if(tiles[l][i][j] != null)
						tiles[l][i][j].drawToScreen(g2D, i * Tile.TILE_SIZE, j * Tile.TILE_SIZE);
		
		//Draw interactive layer
		for(int i = 0; i < tiles[INTERACTIVE_LAYER].length; i++)
			for(int j = 0; j < tiles[INTERACTIVE_LAYER][i].length; j++)
				if(tiles[INTERACTIVE_LAYER][i][j] != null)
					tiles[INTERACTIVE_LAYER][i][j].drawToScreen(g2D, i * Tile.TILE_SIZE, j * Tile.TILE_SIZE);
	}
}