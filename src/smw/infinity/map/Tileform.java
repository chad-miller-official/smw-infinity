package smw.infinity.map;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import smw.infinity.Drawable;
import smw.infinity.Updatable;

public abstract class Tileform implements Drawable, Updatable, Serializable
{
	private static final long serialVersionUID = 8562670684710287908L;
	public static final byte MAX_NUM_LAYERS = 10;
	protected int tileWidth, tileHeight;
	protected List<Tile[][]> tiles;
	protected TileType[][] types;
	
	public Tileform(int tileWidth, int tileHeight)
	{
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		tiles = new LinkedList<Tile[][]>();
		tiles.add(new Tile[tileWidth][tileHeight]);
		types = new TileType[tileWidth][tileHeight];
	}
	
	public void setTile(int x, int y, int layer, Tile t)
	{
		tiles.get(layer)[x][y] = t;
	}
	
	public void setTileType(int x, int y, TileType t)
	{
		types[x][y] = t;
	}
	
	public boolean hasTileAt(int x, int y)
	{
		for(Tile[][] t : tiles)
			if(t[x][y] != null)
				return true;
		
		return false;
	}
	
	public TileType tileTypeAt(int x, int y)
	{
		return types[x][y];
	}
	
	public int getTileWidth()
	{
		return tileWidth;
	}
	
	public int getTileHeight()
	{
		return tileHeight;
	}
	
	public int getPixelWidth()
	{
		return (tileWidth * Tile.TILE_SIZE);
	}
	
	public int getPixelHeight()
	{
		return (tileHeight * Tile.TILE_SIZE);
	}
	
	public int getNumLayers()
	{
		return tiles.size();
	}
	
	public void addLayer()
	{
		if(tiles.size() < MAX_NUM_LAYERS)
			tiles.add(new Tile[tileWidth][tileHeight]);
	}
	
	public void removeLayer(int layer)
	{
		if(tiles.size() > 1)
			tiles.remove(layer);
	}
	
	public void crop(int tileWidthNew, int tileHeightNew)
	{
		for(int i = 0; i < tileWidthNew; i++)
			tiles.set(i, Arrays.copyOf(tiles.get(i), tileHeightNew));
		
		tiles = tiles.subList(0, tileWidthNew);
		tileWidth = tileWidthNew;
		tileHeight = tileHeightNew;
	}
	
	@Override
	public void update(long timePassed)
	{
		for(Tile[][] t : tiles)
			for(Tile[] t1 : t)
				for(Tile t2 : t1)
					if(t2 != null && t2 instanceof AnimatedTile)
						((AnimatedTile) t2).update(timePassed);
	}
	
	@Override
	public void drawToScreen(Graphics2D g2D, ImageObserver i)
	{
		drawToScreen(g2D, 0, 0, i);
	}
	
	@Override
	public void drawToScreen(Graphics2D g2D, int xPos, int yPos, ImageObserver i)
	{
		for(Tile[][] t : tiles)
			for(int ix = 0; ix < tileWidth; ix++)
				for(int j = 0; j < tileHeight; j++)
					if(t[ix][j] != null)
						g2D.drawImage(t[ix][j].getImage(), xPos + (ix * Tile.TILE_SIZE), yPos + (j * Tile.TILE_SIZE), i);
	}
	
	@Override
	public BufferedImage getImage()
	{
		return null;
	}
	
	public Dimension getDimensions()
	{
		return new Dimension(tileWidth * Tile.TILE_SIZE, tileHeight * Tile.TILE_SIZE);
	}
}