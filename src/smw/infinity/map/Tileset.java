package smw.infinity.map;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.imageio.ImageIO;

import smw.infinity.Drawable;

public class Tileset implements Drawable
{
	public static final String TILESET_DIR = "res/gfx/tilesets/";
	
	public static List<Tileset> activeTilesets = new ArrayList<Tileset>(8);
	protected static Hashtable<String, Integer> activeTilesetsIndices = new Hashtable<String, Integer>();
	
	protected String tilesetName;
	protected Image tileset;
	protected int widthTiles, heightTiles;
	
	protected Tileset(String tilesetName)
	{
		this.tilesetName = tilesetName;
	}
	
	public static void loadTileset(String name) throws IOException
	{
		String dir = TILESET_DIR + name + "/tileset.png";
		
		Tileset toAdd = new Tileset(name);
		toAdd.tileset = ImageIO.read(new File(dir));
		toAdd.widthTiles = toAdd.tileset.getWidth(null) / Tile.TILE_SIZE;
		toAdd.heightTiles = toAdd.tileset.getHeight(null) / Tile.TILE_SIZE;
		
		activeTilesetsIndices.put(name, activeTilesets.size());
		activeTilesets.add(toAdd);
	}
	
	public static Tileset getTileset(String name)
	{
		return activeTilesets.get(activeTilesetsIndices.get(name));
	}

	@Override
	public void drawToScreen(Graphics2D g2D, int x, int y)
	{
		g2D.drawImage(tileset, x, y, null);
	}
	
	@Override
	public Image getImage()
	{
		return tileset;
	}
	
	public Tile getTile(short x, short y)
	{
		return new Tile(x, y, (byte) activeTilesets.indexOf(this));
	}
	
	public String getName()
	{
		return tilesetName;
	}
	
	public int getWidth()
	{
		return widthTiles * Tile.TILE_SIZE;
	}
	
	public int getHeight()
	{
		return heightTiles * Tile.TILE_SIZE;
	}
	
	public Dimension getDimensions()
	{
		return new Dimension(widthTiles * Tile.TILE_SIZE, heightTiles * Tile.TILE_SIZE);
	}
}