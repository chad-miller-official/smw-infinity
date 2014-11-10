package smw.infinity.map;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import smw.infinity.Drawable;

public class Tileset implements Drawable
{
	public static final String TILESET_DIR = "res/gfx/tilesets/";
	public static List<Tileset> activeTilesets = new ArrayList<Tileset>(8);
	
	protected String tilesetName;
	protected BufferedImage tileset;
	protected int widthTiles, heightTiles;
	
	private Tileset(String tilesetName, BufferedImage tileset)
	{
		this.tilesetName = tilesetName;
		this.tileset = tileset;
		widthTiles = tileset.getWidth() / Tile.TILE_SIZE;
		heightTiles = tileset.getHeight() / Tile.TILE_SIZE;
	}
	
	public static void loadTileset(String name) throws IOException
	{
		String dir = TILESET_DIR + name + "/tileset.png";
		System.out.println(dir);
		BufferedImage tilesetImg = ImageIO.read(new File(dir));
		Tileset toAdd = new Tileset(name, tilesetImg);
		activeTilesets.add(toAdd);
	}
	
	public static Tileset getTileset(String name)
	{
		for(int i = 0, s = activeTilesets.size(); i < s; i++)
			if(activeTilesets.get(i).getName().equals(name))
				return activeTilesets.get(i);
		
		return null;
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
}