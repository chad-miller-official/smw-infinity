package smw.infinity.map;

import smw.infinity.Direction;

public enum TileType
{
	SOLID,
	FALLTHROUGH,
	SLIPPERY,
	KILL,
	KILLBOTTOM,
	KILLTOP,
	KILLLEFT,
	KILLRIGHT,
	INTERACTIVE;
	
	public static boolean isSolid(TileType type, Direction dirOnTile)
	{
		switch(type)
		{
			case SOLID:
			case SLIPPERY:
			case KILL:
			case KILLBOTTOM:
			case KILLTOP:
			case KILLLEFT:
			case KILLRIGHT:
			case INTERACTIVE:
				return true;
			case FALLTHROUGH:
				return (dirOnTile == Direction.SOUTH);
			default:
				return false;
		}
	}
}