package smw.infinity.map;

import java.awt.image.BufferedImage;

import smw.infinity.Animation;
import smw.infinity.Updatable;

public class AnimatedTile extends Tile implements Updatable
{
	private static final long serialVersionUID = 3868310409120992158L;
	private transient Animation animTile;

	public AnimatedTile(int tilesetX, int tilesetY, String tilesetName)
	{
		super(tilesetX, tilesetY, tilesetName);
	}
	
	private AnimatedTile(int tilesetX, int tilesetY, String tilesetName, BufferedImage tile, Animation animTile)
	{
		super(tilesetX, tilesetY, tilesetName, tile);
		this.animTile = animTile;
	}
	
	@Override
	public void setTile(int tilesetX, int tilesetY, String tilesetName)
	{
		animTile = ((AnimatedTileset) Tileset.tilesets.get(tilesetName)).getTilesetAnim().getSubanimation(tilesetX, tilesetY, TILE_SIZE, TILE_SIZE);
		this.tilesetX = tilesetX;
		this.tilesetY = tilesetY;
		this.tilesetName = tilesetName;
	}

	@Override
	public void update(long timePassed)
	{
		animTile.update(timePassed);
		tile = animTile.getImage();
	}
	
	@Override
	public Object clone()
	{
		return new AnimatedTile(tilesetX, tilesetY, tilesetName, tile, (Animation) animTile.clone());
	}
}