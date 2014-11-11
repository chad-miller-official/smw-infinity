package smw.infinity.map;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import smw.infinity.Drawable;

public class Tile implements Drawable, Serializable
{
	private static final long serialVersionUID = 1237251774437640919L;
	public static final byte TILE_SIZE = 32;
	
	protected short tilesetX, tilesetY;
	protected byte tilesetIndex;	//index in Tileset.activeTilesets
	protected transient Image tileImg;
	
	public Tile(short tilesetX, short tilesetY, byte tilesetIndex)
	{
		this(tilesetX, tilesetY, tilesetIndex, false);
	}
	
	protected Tile(short tilesetX, short tilesetY, byte tilesetIndex, boolean noSet)
	{
		this.tilesetX = tilesetX;
		this.tilesetY = tilesetY;
		this.tilesetIndex = tilesetIndex;
		
		if(!noSet)
			tileImg = ((BufferedImage) Tileset.activeTilesets.get(tilesetIndex).getImage()).getSubimage(tilesetX * TILE_SIZE, tilesetY * TILE_SIZE, TILE_SIZE, TILE_SIZE);
	}

	@Override
	public void drawToScreen(Graphics2D g2D, int x, int y)
	{
		g2D.drawImage(tileImg, x, y, null);
	}
	
	@Override
	public Image getImage()
	{
		return tileImg;
	}
}