package smw.infinity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public interface Drawable
{
	BufferedImage getImage();
	
	void drawToScreen(Graphics2D g2D, ImageObserver i);
	
	void drawToScreen(Graphics2D g2D, int xPos, int yPos, ImageObserver i);
}
