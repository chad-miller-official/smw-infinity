package smw.infinity.map;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.Serializable;

import smw.infinity.Drawable;

public class Tile implements Cloneable, Drawable, Serializable
{
	private static final long serialVersionUID = 4076294701172277068L;
	public static byte TILE_SIZE = 32;
	protected transient BufferedImage tile;
	protected int tilesetX, tilesetY;
	protected String tilesetName;
	
	public Tile(int tilesetX, int tilesetY, String tilesetName)
	{
		setTile(tilesetX, tilesetY, tilesetName);
	}
	
	protected Tile(int tilesetX, int tilesetY, String tilesetName, BufferedImage tile)
	{
		this.tilesetX = tilesetX;
		this.tilesetY = tilesetY;
		this.tilesetName = tilesetName;
		this.tile = tile;
	}
	
	public void setTile(int tilesetX, int tilesetY, String tilesetName)
	{
		tile = Tileset.tilesets.get(tilesetName).getImage().getSubimage(tilesetX * Tile.TILE_SIZE, tilesetY * Tile.TILE_SIZE, TILE_SIZE, TILE_SIZE);
		this.tilesetX = tilesetX;
		this.tilesetY = tilesetY;
		this.tilesetName = tilesetName;
	}
	
	public int getTilesetX()
	{
		return tilesetX;
	}
	
	public int getTilesetY()
	{
		return tilesetY;
	}
	
	public void setTilesetX(int tilesetX)
	{
		setTile(tilesetX, tilesetY, tilesetName);
	}
	
	public void setTilesetY(int tilesetY)
	{
		setTile(tilesetX, tilesetY, tilesetName);
	}
	
	public String getTilesetName()
	{
		return tilesetName;
	}
	
	public void setTilesetName(String tilesetName)
	{
		setTile(tilesetX, tilesetY, tilesetName);
	}

	@Override
	public BufferedImage getImage()
	{
		return tile;
	}
	
	@Override
	public void drawToScreen(Graphics2D g2D, ImageObserver i)
	{
		g2D.drawImage(tile, 0, 0, i);
	}

	@Override
	public void drawToScreen(Graphics2D g2D, int xPos, int yPos, ImageObserver i)
	{
		g2D.drawImage(tile, xPos, yPos, i);
	}
	
	@Override
	public Object clone()
	{
		return new Tile(tilesetX, tilesetY, tilesetName, tile);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tilesetName == null) ? 0 : tilesetName.hashCode());
		result = prime * result + tilesetX;
		result = prime * result + tilesetY;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		
		if(obj == null)
			return false;
		
		if(getClass() != obj.getClass())
			return false;
		
		Tile other = (Tile) obj;
		
		if(tilesetName == null && other.tilesetName != null)
			return false;
		else if(!tilesetName.equals(other.tilesetName))
			return false;
		
		if(tilesetX != other.tilesetX)
			return false;
		
		if(tilesetY != other.tilesetY)
			return false;
		
		return true;
	}
}
