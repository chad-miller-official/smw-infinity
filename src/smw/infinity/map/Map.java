package smw.infinity.map;

import smw.infinity.Scene;
import smw.infinity.Updatable;

public class Map implements Updatable
{
	public static final int MAP_WIDTH = Scene.SCENE_WIDTH / Tile.TILE_SIZE;
	public static final int MAP_HEIGHT = Scene.SCENE_HEIGHT / Tile.TILE_SIZE;
	
	private Tile[][] tiles;
	
	public Map()
	{
		tiles = new Tile[MAP_WIDTH][MAP_HEIGHT];
	}
	
	public Tile getTile(int x, int y)
	{
		return tiles[x][y];
	}
	
	public void setTile(Tile tile, int x, int y)
	{
		tiles[x][y] = tile;
	}

	@Override
	public void update(long delta)
	{
		for(int i = 0; i < tiles.length; i++)
			for(int j = 0; j < tiles[i].length; j++)
				if(tiles[i][j] instanceof Updatable)
					((Updatable) tiles[i][j]).update(delta);
	}
}