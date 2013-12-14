package smw.infinity.entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import smw.infinity.Drawable;
import smw.infinity.Interactive;
import smw.infinity.Updatable;
import smw.infinity.Vector2D;
import smw.infinity.map.MapScene;

public abstract class Entity extends Rectangle implements Drawable, Interactive, Updatable
{
	private static final long serialVersionUID = 5686750544242274162L;
	protected Point2D.Float coord;
	protected Vector2D speed;
	protected BufferedImage currentSprite;

	public Entity(Point2D.Float coord, String spriteFilename)
	{
		this(32, 32, coord, spriteFilename);
	}
	
	public Entity(int width, int height, Point2D.Float coord, String spriteFilename)
	{
		super(width, height);
		speed = new Vector2D(0, 0);
		
		setupSprites(spriteFilename);
		spawn(coord);
	}
	
	protected abstract void setupSprites(String spriteFilename);
	
	public synchronized void setCoordinate(Point2D.Float coord)
	{
		this.coord = coord;
	}
	
	@Override
	public BufferedImage getImage()
	{
		return currentSprite;
	}
	
	@Override
	public void drawToScreen(Graphics2D g2D, ImageObserver i)
	{
		drawToScreen(g2D, Math.round(coord.x), Math.round(coord.y), i);
	}
	
	@Override
	public void drawToScreen(Graphics2D g2D, int xPos, int yPos, ImageObserver i)
	{
		g2D.drawImage(currentSprite, xPos, yPos, i);
		int wrapDrawX = 0, wrapDrawY = 0;
		
		if(Math.round(xPos) > MapScene.mapPixelWidth - 32)
			wrapDrawX = xPos - MapScene.mapPixelWidth;
		
		if(Math.round(yPos) > MapScene.mapPixelHeight - 32)
			wrapDrawY = yPos - MapScene.mapPixelHeight;
			
		if(wrapDrawX > 0 || wrapDrawY > 0)
			drawToScreen(g2D, wrapDrawX, wrapDrawY, i);
	}
	
	@Override
	public Point2D.Float getCoordinate()
	{
		return coord;
	}
	
	public void die()
	{
		MapScene.removeEntity(this);
	}
	
	public void spawn(Point2D.Float coord)
	{
		this.coord = coord;
		MapScene.addEntity(this);
	}
	
	@Override
	public void update(long timePassed)
	{
		updateMovement(timePassed);
		updateImage(timePassed);
	}
	
	protected void updateMovement(long timePassed)
	{
		if(coord.x < 0)
			coord.x += MapScene.mapPixelWidth;
		else if(coord.x >= MapScene.mapPixelWidth)
			coord.x -= MapScene.mapPixelWidth;
		
		if(coord.y> MapScene.mapPixelHeight)
			coord.y = -32f;
	}

	protected abstract void updateImage(long timePassed);
	
	protected abstract void resolveCollision();
	
	@Override
	public abstract int hashCode();
	
	@Override
	public abstract boolean equals(Object o);
}
