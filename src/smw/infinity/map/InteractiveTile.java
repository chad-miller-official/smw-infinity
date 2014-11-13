package smw.infinity.map;

public class InteractiveTile extends AnimatedTile
{
	private static final long serialVersionUID = -6389565658662029995L;
	
	public InteractiveTile(short tilesetX, short tilesetY)
	{
		super(tilesetX, tilesetY, InteractiveTileset.getInteractiveTilesetIndex());
	}
}
