package smw.infinity;

import java.awt.geom.Point2D;

public interface Interactive
{
	void react(Interactive other, Direction dirOnOther);
	
	Point2D.Float getCoordinate();

	@Override
	int hashCode();
}
