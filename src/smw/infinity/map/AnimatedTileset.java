package smw.infinity.map;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import smw.infinity.Animation;
import smw.infinity.Updatable;

public class AnimatedTileset extends Tileset implements Updatable
{
	public static String ANIM_TILESET_DIR = TILESET_DIR + "anim/";
	private Animation tilesetAnim;
	
	protected AnimatedTileset(String tilesetName)
	{
		super(tilesetName);
	}
	
	public static void loadTileset(String name) throws IOException
	{
		String dir = ANIM_TILESET_DIR + name + "/";
		
		AnimatedTileset toAdd = new AnimatedTileset(name);
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
		
		activeTilesets.add(toAdd);
	}

	@Override
	public void update(long delta)
	{
		tilesetAnim.update(delta);
		tileset = tilesetAnim.getImage();
	}
	
	public Animation getImageAnim()
	{
		return tilesetAnim;
	}
	
	@Override
	public Tile getTile(short x, short y)
	{
		AnimatedTile toReturn = new AnimatedTile(x, y, (byte) activeTilesets.indexOf(this));
		toReturn.setPhase(tilesetAnim.getPhase());
		return toReturn;
	}
}
