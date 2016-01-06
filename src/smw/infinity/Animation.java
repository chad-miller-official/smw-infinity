package smw.infinity;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class Animation implements Cloneable, Drawable, Updatable
{
	private Frame[] frames;
	private long frameRate, phase, totalTime;
	private int index, width, height;

	public Animation( long frameRate, Image... images )
	{
		frames = new Frame[images.length];
		totalTime = 0;

		for( int i = 0; i < images.length; i++ )
		{
			totalTime += frameRate;
			frames[i] = new Frame( images[i], totalTime );
		}

		this.frameRate = frameRate;
		phase = 0;
		index = 0;
		width = frames[0].image.getWidth( null );
		height = frames[0].image.getHeight( null );
	}

	private Animation( long frameRate, long timeSinceUpdate, long totalTime, int index, int width, int height, Frame[] frames )
	{
		this.frameRate = frameRate;
		this.phase     = timeSinceUpdate;
		this.totalTime = totalTime;
		this.index     = index;
		this.width     = width;
		this.height    = height;
		this.frames    = frames;
	}

	@Override
	public void update( long delta )
	{
		phase += delta;

		if( phase > totalTime )
		{
			phase = 0;
			index = 0;
		}

		while( phase > frames[index].endTime )
			index++;
	}

	@Override
	public void drawToScreen( Graphics2D g2D, int x, int y )
	{
		g2D.drawImage( frames[index].image, x, y, null );
	}

	@Override
	public Image getImage()
	{
		return frames[index].image;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	@Override
	public Animation clone()
	{
		return new Animation( frameRate, phase, totalTime, index, width, height, frames );
	}

	public Animation getSubanimation( int x, int y, int w, int h )
	{
		BufferedImage[] subimgs = new BufferedImage[frames.length];

		for( int i = 0; i < subimgs.length; i++ )
			subimgs[i] = ( (BufferedImage) frames[i].image ).getSubimage( x, y, w, h );

		return new Animation(frameRate, subimgs);
	}

	public void setPhase( long phase )
	{
		this.phase = phase;
	}

	public long getPhase()
	{
		return phase;
	}

	private class Frame implements Cloneable
	{
		private Image image;
		private long endTime;

		private Frame( Image image, long endTime )
		{
			this.image   = image;
			this.endTime = endTime;
		}

		@Override
		public Frame clone()
		{
			return new Frame( image, endTime );
		}
	}
}
