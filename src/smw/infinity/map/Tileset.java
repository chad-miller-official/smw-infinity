package smw.infinity.map;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import smw.infinity.Drawable;

public class Tileset implements Drawable
{
	public static List<Tileset> activeTilesets = new ArrayList<Tileset>(8);
	
	protected String tilesetName;
	protected BufferedImage tileset;
	protected int widthTiles, heightTiles;
	
	public Tileset(String tilesetName)
	{
		this.tilesetName = tilesetName;
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
}