package smw.infinity;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

public abstract class Scene extends Container implements Runnable
{
	private static final long serialVersionUID = 1457116440924270037L;
	protected static final long FRAME_DELAY = 16, TIMER_DELAY = 1000;
	
	public static UncaughtExceptionHandler ue = new SMWUncaughtExceptionHandler();
	
	protected static String texturePackName = "SMW", musicPackName = "SMW", sfxPackName = "Classic";
	protected static BitmapFont font;
	protected static SoundPlayer sound;
	
	protected boolean running;
	protected Timer renderLoop;
	
	protected static Set<Drawable> drawables = new HashSet<Drawable>();
	protected static Set<Updatable> updatables = new HashSet<Updatable>();
	protected RenderStrategy rs;
	
	static
	{
		Utility.checkLWJGL();
		sound = new SoundPlayer(sfxPackName);
		
		try
		{
			font = new BitmapFont(ImageIO.read(new File("res/gfx/fonts/font.png")), "!\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_'abcdefghijklmnopqrstuvwxyz{|}~", new Color(255, 0, 255));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public Scene()
	{
		super();
		
		renderLoop = new Timer("renderLoop");
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
		}, TIMER_DELAY, FRAME_DELAY);
		
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run()
			{
				ScreenManager.init("");
				setIgnoreRepaint(true);
				setLayout(new BorderLayout());
				setPreferredSize(ScreenManager.DEFAULT_DIMENSION);
				setEnabled(true);
			}
		});
	}
	
	public void forceStop()
	{
		running = false;
		renderLoop.cancel();
	}
	
	public boolean isRunning()
	{
		return running;
	}

	protected void preLoop()
	{
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run()
			{
				ScreenManager.setScene(Scene.this);
				ScreenManager.pack();
			}
		});
	}

	@Override
	public void run()
	{
		preLoop();
		
		running = true;
		long startTime = System.currentTimeMillis(), totalTime = startTime, timePassed;
		
		while(running)
		{
			timePassed = System.currentTimeMillis() - totalTime;
			totalTime += timePassed;

			sceneLoop(timePassed);
			
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
	
	protected void sceneLoop(long timePassed)
	{
		for(Updatable u : updatables)
			u.update(timePassed);
	}
	
	public static void quit()
	{
		sound.close();
		ScreenManager.dispose();
		System.out.println(Utility.lang.get("credits"));
		System.out.println(Utility.lang.get("extra credits"));
	}
	
	@Override
	protected void finalize() throws Throwable
	{
		if(running)
			forceStop();
		
		super.finalize();
	}
}