package smw.infinity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.Arrays;

public class Animation implements Cloneable, Drawable, Updatable
{
	private Frame[] frames;
	private int currentFrame;
	private long countTime, totalTime;
	private long frameRate;
	
	public Animation(long frameRate, BufferedImage... images) throws SMWException
	{
		frames = new Frame[images.length];
		
		for(int i = 1; i <= images.length; i++)
		{			
			totalTime += frameRate;
			frames[i] = new Frame(images[i - 1], totalTime);
		}
		
		countTime = totalTime = 0;
		currentFrame = 0;
		this.frameRate = frameRate;
	}
	
	private Animation(Frame[] frames, int currentFrame, long countTime, long totalTime, long frameRate)
	{
		this.frames = Arrays.copyOf(frames, frames.length);
		this.currentFrame = currentFrame;
		this.countTime = countTime;
		this.totalTime = totalTime;
		this.frameRate = frameRate;
	}
	
	public void update(long timePassed)
	{
		countTime += timePassed;
		
		if(countTime >= totalTime)
		{
			countTime = 0;
			currentFrame = 0;
		}
		
		while(countTime > frames[currentFrame].endTime)
			currentFrame++;
	}
	
	@Override
	public BufferedImage getImage()
	{
		return frames[currentFrame].image;
	}
	
	@Override
	public void drawToScreen(Graphics2D g2D, ImageObserver i)
	{
		drawToScreen(g2D, 0, 0, i);
	}

	@Override
	public void drawToScreen(Graphics2D g2D, int xPos, int yPos, ImageObserver i)
	{
		g2D.drawImage(frames[currentFrame].image, xPos, yPos, i);
	}
	
	public int getNumFrames()
	{
		return frames.length;
	}
	
	public void reset()
	{
		countTime = currentFrame = 0;
	}
	
	@Override
	public Object clone()
	{
		return new Animation(frames, currentFrame, countTime, totalTime, frameRate);
	}
	
	public long getFrameRate()
	{
		return frameRate;
	}
	
	public Animation getSubanimation(int x, int y, int w, int h)
	{
		BufferedImage[] sub = new BufferedImage[frames.length];

		for(int i = 0; i < sub.length; i++)
			sub[i] = frames[i].image.getSubimage(x, y, w, h);

		try
		{
			return new Animation(frameRate, sub);
		}
		catch(SMWException e)
		{
			return null;	//this should never happen
		}
	}
	
	public BufferedImage[] getFrames()
	{
		BufferedImage[] toReturn = new BufferedImage[frames.length];
		
		for(int i = 0; i < frames.length; i++)
			toReturn[i] = frames[i].image;
		
		return toReturn;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (countTime ^ (countTime >>> 32));
		result = prime * result + currentFrame;
		result = prime * result + (int) (frameRate ^ (frameRate >>> 32));
		result = prime * result + (int) (totalTime ^ (totalTime >>> 32));
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
		
		Animation other = (Animation) obj;
		
		if(countTime != other.countTime)
			return false;
		
		if(currentFrame != other.currentFrame)
			return false;
		
		if(frameRate != other.frameRate)
			return false;
		
		if(totalTime != other.totalTime)
			return false;
		
		return true;
	}

	private class Frame implements Cloneable
	{
		private BufferedImage image;
		private long endTime;
		
		private Frame(BufferedImage image, long endTime)
		{
			this.image = image;
			this.endTime = endTime;
		}
		
		@Override
		public Object clone()
		{
			return new Frame(image, endTime);
		}
	}
}