package smw.infinity;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.image.BufferStrategy;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public abstract class Scene
{
	public static final int SCENE_WIDTH = 768, SCENE_HEIGHT = 576;
	public static final Dimension SCENE_DIMENSION = new Dimension(SCENE_WIDTH, SCENE_HEIGHT);
	
	protected final String TITLE;
	
	protected static final long BASE_TIMER_DELAY = 100, FRAME_DELAY = 16;
	protected Set<Updatable> updatables;
	protected Set<Drawable> drawables;
	
	protected Timer updateLoop, drawRenderLoop;
	
	protected boolean running;
	
	protected Canvas canvas;
	protected volatile BufferStrategy buffer;
	
	public Scene(String title)
	{
		running = false;
		
		TITLE = title;
		updatables = new HashSet<Updatable>();
		drawables = new HashSet<Drawable>();
		
		updateLoop = new Timer("Update Loop");
		drawRenderLoop = new Timer("Draw-Render Loop");
		
		canvas = new Canvas();
		canvas.setPreferredSize(SCENE_DIMENSION);
		canvas.setMinimumSize(SCENE_DIMENSION);
		canvas.setMaximumSize(SCENE_DIMENSION);
	}
	
	public void init()
	{
		canvas.createBufferStrategy(3);
		buffer = canvas.getBufferStrategy();
	}
	
	public void start()
	{
		running = true;
		final long startTime = System.currentTimeMillis();
		
		updateLoop.scheduleAtFixedRate(new TimerTask() {
			long totalTime = startTime, delta = 0;
			
			@Override
			public void run()
			{
				delta = System.currentTimeMillis() - totalTime;
				totalTime += delta;
				update(delta);
				
				try
				{
					Thread.sleep(FRAME_DELAY);
				}
				catch(InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}, BASE_TIMER_DELAY, FRAME_DELAY);
		
		drawRenderLoop.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run()
			{
				render();
				buffer.show();
			}
		}, BASE_TIMER_DELAY, FRAME_DELAY);
	}
	
	public abstract void update(long delta);
	
	public abstract void render();
	
	public Canvas getCanvas()
	{
		return canvas;
	}
	
	public void stop()
	{
		updateLoop.cancel();
		drawRenderLoop.cancel();
		running = false;
		buffer.dispose();
	}
	
	public void addUpdatable(Updatable u)
	{
		updatables.add(u);
	}
	
	public void addDrawable(Drawable d)
	{
		drawables.add(d);
	}
	
	public boolean isRunning()
	{
		return running;
	}
	
	public String getTitle()
	{
		return TITLE;
	}
}