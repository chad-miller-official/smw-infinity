package smw.infinity.map;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import smw.infinity.Drawable;

public class Tileset implements Drawable
{
	public static Hashtable<String, Tileset> tilesets = new Hashtable<String, Tileset>();
	
	public static final String TILESET_DIR = "res/gfx/tilesets/";
	protected String tilesetName;
	protected TileType[][] types;
	protected BufferedImage tileset;
	protected int tileWidth, tileHeight;
	
	public Tileset(String tilesetName)
	{
		this.tilesetName = tilesetName;
		
		if(new File(TILESET_DIR + tilesetName).exists())
			load();
	}
	
	protected void load()
	{
		ObjectInputStream in = null;
		
		try
		{
			tileset = ImageIO.read(new File(TILESET_DIR + tilesetName + "/tileset.png"));
			tileWidth = tileset.getWidth() / Tile.TILE_SIZE;
			tileHeight = tileset.getHeight() / Tile.TILE_SIZE;
			File f = new File(TILESET_DIR + tilesetName + "/tileset.tls");
			
			if(f.exists())
			{
				in = new ObjectInputStream(new FileInputStream(f));
				types = (TileType[][]) in.readObject();
			}
			else
			{
				types = new TileType[tileWidth][tileHeight];
				saveTilesetData();
			}
			
			tilesets.put(tilesetName, this);
		}
		catch(IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(in != null)
					in.close();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public String getTilesetName()
	{
		return tilesetName;
	}
	
	public TileType getType(int x, int y)
	{
		return types[x][y];
	}
	
	public void setType(int x, int y, TileType type)
	{
		types[x][y] = type;
	}
	
	public Tile getTile(int x, int y)
	{
		return new Tile(x, y, tilesetName);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + tileHeight;
		result = prime * result + tileWidth;
		result = prime * result + ((tilesetName == null) ? 0 : tilesetName.hashCode());
		result = prime * result + Arrays.hashCode(types);
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
		
		Tileset other = (Tileset) obj;
		
		if(tileHeight != other.tileHeight)
			return false;
		
		if(tileWidth != other.tileWidth)
			return false;
		
		if(tilesetName == null && other.tilesetName != null)
			return false;
		else if(!tilesetName.equals(other.tilesetName))
			return false;
		
		if(!Arrays.deepEquals(types, other.types))
			return false;
		
		return true;
	}

	@Override
	public BufferedImage getImage()
	{
		return tileset;
	}
	
	@Override
	public void drawToScreen(Graphics2D g2D, ImageObserver i)
	{
		drawToScreen(g2D, 0, 0, i);
	}

	@Override
	public void drawToScreen(Graphics2D g2D, int xPos, int yPos, ImageObserver i)
	{
		g2D.drawImage(tileset, xPos, yPos, i);
	}
	
	public void saveTilesetData()
	{
		ObjectOutputStream out = null;
		
		try
		{
			out = new ObjectOutputStream(new FileOutputStream(TILESET_DIR + tilesetName + "/tileset.tls"));
			out.writeObject(types);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(out != null)
					out.close();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public Dimension getDimension()
	{
		return new Dimension(tileWidth * Tile.TILE_SIZE, tileHeight * Tile.TILE_SIZE);
	}
}