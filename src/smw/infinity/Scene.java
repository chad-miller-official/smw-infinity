package smw.infinity;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.MenuBar;
import java.awt.image.BufferStrategy;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class Scene
{
	public static final int SCENE_WIDTH = 768, SCENE_HEIGHT = 576;
	public static final Dimension SCENE_DIMENSION = new Dimension(SCENE_WIDTH, SCENE_HEIGHT);
	
	protected final String TITLE;
	
	protected static final long BASE_TIMER_DELAY = 100, FRAME_DELAY = 16;
	protected Set<Updatable> updatables;
	protected Set<Drawable> drawables;
	
	protected ScheduledThreadPoolExecutor services;
	protected ScheduledFuture<?> updateLoop, renderLoop;
	
	protected boolean running;
	
	protected Canvas canvas;
	protected volatile BufferStrategy buffer;
	
	public Scene(String title)
	{
		running = false;
		
		TITLE = title;
		updatables = new HashSet<Updatable>();
		drawables = new HashSet<Drawable>();
		
		services = new ScheduledThreadPoolExecutor(2);
		
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
		
		updateLoop = services.scheduleAtFixedRate(new Runnable() {
			long totalTime = startTime, delta = 0;
			
			@Override
			public void run()
			{
				delta = System.currentTimeMillis() - totalTime;
				totalTime += delta;
				update(delta);
			}
		}, BASE_TIMER_DELAY, FRAME_DELAY, TimeUnit.MILLISECONDS);
		
		renderLoop = services.scheduleAtFixedRate(() -> {
			render();
			buffer.show();
		}, BASE_TIMER_DELAY, FRAME_DELAY, TimeUnit.MILLISECONDS);
	}
	
	public abstract void update(long delta);
	
	public abstract void render();
	
	public abstract SMWComponent[] getComponents();
	
	public abstract MenuBar getMenuBar();
	
	public Canvas getCanvas()
	{
		return canvas;
	}
	
	public void stop()
	{
		running = false;
		services.shutdown();
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