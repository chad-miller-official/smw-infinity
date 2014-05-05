package smw.infinity;

import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public /*abstract*/ class Scene
{
	protected String title;
	
	protected static final long BASE_TIMER_DELAY = 100, FRAME_DELAY = 16;
	protected Set<Updatable> updatables;
	protected Set<Drawable> drawables;
	
	protected Timer updateLoop;
	protected Timer drawRenderLoop;
	
	protected boolean running;
	
	public Scene(String title)
	{
		running = false;
		
		this.title = title;
		updatables = new HashSet<Updatable>();
		drawables = new HashSet<Drawable>();
		
		updateLoop = new Timer("Update Loop");
		drawRenderLoop = new Timer("Draw-Render Loop");
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
				for(Updatable u : updatables)
				{
					delta = System.currentTimeMillis() - totalTime;
					totalTime += delta;
					
					u.update(delta);
					
					try
					{
						Thread.sleep(FRAME_DELAY);
					}
					catch(InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		}, BASE_TIMER_DELAY, FRAME_DELAY);
		
		drawRenderLoop.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run()
			{
				Graphics2D g2D = (Graphics2D) ScreenManager.getGraphics();
				
				for(Drawable d : drawables)
					d.drawToScreen(g2D);
				
				g2D.dispose();
				ScreenManager.show();
			}
		}, BASE_TIMER_DELAY, FRAME_DELAY);
	}
	
	public void stop()
	{
		updateLoop.cancel();
		drawRenderLoop.cancel();
		running = false;
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
		return title;
	}
}