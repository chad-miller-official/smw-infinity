package smw.infinity;

import java.awt.Graphics2D;
import java.awt.Image;

public interface Drawable
{
	void drawToScreen(Graphics2D g2D, int x, int y);
	
	Image getImage();
}