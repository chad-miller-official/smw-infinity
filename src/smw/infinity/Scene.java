package smw.infinity;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.EventQueue;
import java.util.Timer;
import java.util.TimerTask;

public abstract class Scene extends Container implements Runnable
{
	private static final long serialVersionUID = 1457116440924270037L;
	protected static final long FRAME_DELAY = 16, BASE_TIMER_DELAY = 1000;
	public static String gfxPackName = "SMW", musicPackName = "SMW", sfxPackName = "Classic";
	protected boolean running;
	
	protected Timer renderLoop;
	protected RenderStrategy rs;
	
	public Scene()
	{
		super();
		running = false;
		
		setIgnoreRepaint(true);
		setLayout(new BorderLayout());
		setPreferredSize(ScreenManager.DEFAULT_DIMENSION);
		setEnabled(true);
		
		renderLoop = new Timer("Render Loop");
		rs = createRenderStrategy();
	}
	
	protected abstract RenderStrategy createRenderStrategy();
	
	public abstract void init();
	
	protected abstract void update(long timePassed);

	@Override
	public void run()
	{
		running = true;
		
		renderLoop.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run()
			{
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run()
					{
						ScreenManager.render(rs);
					}
				});
			}
		}, FRAME_DELAY, BASE_TIMER_DELAY);
		
		long startTime = System.currentTimeMillis(), totalTime = startTime, timePassed;
		
		while(running)
		{
			timePassed = System.currentTimeMillis() - totalTime;
			totalTime += timePassed;
			
			update(timePassed);
			
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
	
	public void stop()
	{
		running = false;
		renderLoop.cancel();
	}
	
	@Override
	protected void finalize() throws Throwable
	{
		stop();
		super.finalize();
	}
}