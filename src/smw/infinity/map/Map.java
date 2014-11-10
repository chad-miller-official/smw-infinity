package smw.infinity.map;

import smw.infinity.Scene;

public class Map
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
}