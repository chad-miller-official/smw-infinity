package smw.infinity.map;

import smw.infinity.Animation;
import smw.infinity.Updatable;

public class AnimatedTile extends Tile implements Cloneable, Updatable
{
	private static final long serialVersionUID = -8088947781669879370L;
	protected transient Animation tileImgAnim;

	public AnimatedTile(short tilesetX, short tilesetY, byte tilesetIndex)
	{
		super(tilesetX, tilesetY, tilesetIndex, true);
		tileImgAnim = ((AnimatedTileset) Tileset.activeTilesets.get(tilesetIndex)).getImageAnim().getSubanimation(tilesetX * TILE_SIZE, tilesetY * TILE_SIZE, TILE_SIZE, TILE_SIZE);
		tileImg = tileImgAnim.getImage();
	}
	
	protected AnimatedTile(short tilesetX, short tilesetY, byte tilesetIndex, boolean noSet)
	{
		super(tilesetX, tilesetY, tilesetIndex, true);
	}

	@Override
	public void update(long delta)
	{
		tileImgAnim.update(delta);
		tileImg = tileImgAnim.getImage();
	}
	
	@Override
	public AnimatedTile clone()
	{
		AnimatedTile toReturn = new AnimatedTile(tilesetX, tilesetY, tilesetIndex, true);
		toReturn.tileImgAnim = (Animation) this.tileImgAnim.clone();
		toReturn.tileImg = toReturn.tileImgAnim.getImage();
		return toReturn;
	}
	
	public void setPhase(long phase)
	{
		tileImgAnim.setPhase(phase);
	}
	
	public long getPhase()
	{
		return tileImgAnim.getPhase();
	}
}
