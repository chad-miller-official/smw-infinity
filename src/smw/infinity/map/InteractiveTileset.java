package smw.infinity.map;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import smw.infinity.Animation;

public class InteractiveTileset extends AnimatedTileset
{
	public static String INTERACTIVE_TILESET_DIR = ANIM_TILESET_DIR + "Interactive/";
	protected static byte interactiveTilesetIndex = -1;
	
	protected InteractiveTileset(String tilesetName)
	{
		super(tilesetName);
	}
	
	public static void loadInteractiveTileset() throws IOException
	{
		String dir = INTERACTIVE_TILESET_DIR;
		
		InteractiveTileset toAdd = new InteractiveTileset("Interactive");
		List<Image> images = new LinkedList<Image>();
		int index = 1;
		Image img = ImageIO.read(new File(dir + index + ".png"));
		
		while(img != null)
		{
			images.add(img);
			index++;
			
			try
			{
				img = ImageIO.read(new File(dir + index + ".png"));
			}
			catch(IOException e)
			{
				break;
			}
		}
		
		toAdd.tilesetAnim = new Animation(175, images.toArray(new Image[images.size()]));
		toAdd.widthTiles = toAdd.tilesetAnim.getWidth() / Tile.TILE_SIZE;
		toAdd.heightTiles = toAdd.tilesetAnim.getHeight() / Tile.TILE_SIZE;
		
		activeTilesetsIndices.put("Interactive", activeTilesets.size());
		interactiveTilesetIndex = (byte) activeTilesets.size();
		activeTilesets.add(toAdd);
	}
	
	public static byte getInteractiveTilesetIndex()
	{
		return interactiveTilesetIndex;
	}
	
	@Override
	public Tile getTile(short x, short y)
	{
		return new InteractiveTile(x, y);
	}
}
