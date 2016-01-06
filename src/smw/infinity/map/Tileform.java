package smw.infinity.map;

import java.awt.Graphics2D;
import smw.infinity.Updatable;

public abstract class Tileform implements Updatable
{
	protected Tile[][][] tiles;	//Array of 2D arrays

	public Tile getTile( int layer, int x, int y )
	{
		return tiles[layer][x][y];
	}

	public void setTile( Tile tile, int layer, int x, int y )
	{
		tiles[layer][x][y] = tile;
	}

	public abstract void drawToScreen( Graphics2D g2D, int x, int y );
}
