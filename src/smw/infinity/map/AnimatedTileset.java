package smw.infinity.map;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import smw.infinity.Animation;
import smw.infinity.SMWException;
import smw.infinity.Updatable;

public class AnimatedTileset extends Tileset implements Updatable
{
	public static final String ANIM_TILESET_DIR = "res/gfx/tilesets/anim/";
	public static final long ANIM_TILESET_RATE = 250;
	protected Animation tilesetAnim;
	
	public AnimatedTileset(String tilesetName)
	{
		super(tilesetName);
	}
	
	@Override
	protected void load()
	{
		List<BufferedImage> img = new LinkedList<BufferedImage>();
		File f = new File(ANIM_TILESET_DIR + tilesetName + "/1.png");
		
		try
		{
			for(int i = 1; f.exists(); i++)
			{
				img.add(ImageIO.read(f));
				f = new File(ANIM_TILESET_DIR + tilesetName + "/" + (i + 1) + ".png");
			}
			
			tilesetAnim = new Animation(ANIM_TILESET_RATE, (BufferedImage[]) img.toArray());
			tileset = tilesetAnim.getImage();
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File(ANIM_TILESET_DIR + tilesetName + "/tileset.tls")));
			types = (TileType[][]) in.readObject();
			in.close();
			tilesets.put(tilesetName, this);
		}
		catch(IOException | SMWException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void update(long timePassed)
	{
		tilesetAnim.update(timePassed);
		tileset = tilesetAnim.getImage();
	}
	
	public Animation getTilesetAnim()
	{
		return tilesetAnim;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((tilesetAnim == null) ? 0 : tilesetAnim.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		
		if(!super.equals(obj))
			return false;
		
		if(getClass() != obj.getClass())
			return false;
		
		AnimatedTileset other = (AnimatedTileset) obj;
		
		if(tilesetAnim == null && other.tilesetAnim != null)
			return false;
		else if(!tilesetAnim.equals(other.tilesetAnim))
			return false;
		
		return true;
	}
}
