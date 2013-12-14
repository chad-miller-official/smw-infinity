package smw.infinity.map;

import java.awt.geom.Point2D;

import smw.infinity.Interactive;

public abstract class InteractiveTile extends AnimatedTile implements Interactive
{
	private static final long serialVersionUID = 3466101316762384209L;
	protected Point2D.Float coord;

	public InteractiveTile(int tilesetX, int tilesetY)
	{
		super(tilesetX, tilesetY, "interactive");
	}
	
	@Override
	public abstract Object clone();
}