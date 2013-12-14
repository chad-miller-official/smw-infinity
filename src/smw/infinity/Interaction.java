package smw.infinity;

import java.awt.geom.Point2D;

public class Interaction
{
	private final Interactive I_1, I_2;
	private final Direction DIR_1_ON_2, DIR_2_ON_1;
	
	public Interaction(Interactive i1, Interactive i2)
	{
		I_1 = i1;
		I_2 = i2;
		
		Point2D.Float pt1 = I_1.getCoordinate(), pt2 = I_2.getCoordinate();
		float x1 = pt1.x, x2 = pt2.x, y1 = pt1.y, y2 = pt2.y;
		float theta = (float) Math.toDegrees(Math.atan(Math.abs(x1 - x2) / Math.abs(y1 - y2)));
		
		if(theta > 45)
			DIR_1_ON_2 = (y1 > y2) ? Direction.NORTH : Direction.SOUTH;
		else
			DIR_1_ON_2 = (x1 > x2) ? Direction.WEST : Direction.EAST;
		
		DIR_2_ON_1 = Direction.getOpposite(DIR_1_ON_2);
	}
	
	public Interactive getInteractive1()
	{
		return I_1;
	}
	
	public Interactive getInteractive2()
	{
		return I_2;
	}
	
	public void interact()
	{
		I_1.react(I_2, DIR_1_ON_2);
		I_2.react(I_1, DIR_2_ON_1);
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((DIR_1_ON_2 == null) ? 0 : DIR_1_ON_2.hashCode());
		result = prime * result + ((DIR_2_ON_1 == null) ? 0 : DIR_2_ON_1.hashCode());
		result = prime * result + ((I_1 == null) ? 0 : I_1.hashCode());
		result = prime * result + ((I_2 == null) ? 0 : I_2.hashCode());
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
		
		Interaction other = (Interaction) obj;
		
		/* Comparing 1 against 2 (and vice-versa) is intentional. */
		if(DIR_1_ON_2 == null && other.DIR_2_ON_1 != null)
			return false;
		else if(!DIR_1_ON_2.equals(other.DIR_2_ON_1))
			return false;
		
		if(DIR_2_ON_1 == null && other.DIR_1_ON_2 != null)
			return false;
		else if (!DIR_2_ON_1.equals(other.DIR_1_ON_2))
			return false;
		
		/* Comparing the addresses of Interactives is intentional. */
		if(I_1 == null && other.I_2 != null)
			return false;
		else if(I_1 != other.I_2)
			return false;
		
		if(I_2 == null && other.I_1 != null)
			return false;
		else if(I_2 != other.I_1)
			return false;
		
		return true;
	}
}