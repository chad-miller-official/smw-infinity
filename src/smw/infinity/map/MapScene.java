package smw.infinity.map;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.imageio.ImageIO;

import smw.infinity.BitmapFont;
import smw.infinity.Drawable;
import smw.infinity.Interaction;
import smw.infinity.InteractionQueue;
import smw.infinity.RenderStrategy;
import smw.infinity.Scene;
import smw.infinity.ScreenManager;
import smw.infinity.entity.Entity;

public class MapScene extends Scene
{
	private static final long serialVersionUID = 7095409016543341853L;

	private static Set<Entity> entities = new HashSet<Entity>();
	private static ConcurrentLinkedQueue<Runnable> eventQueue = new ConcurrentLinkedQueue<Runnable>();
	private static InteractionQueue interactions = new InteractionQueue();
	private Timer interactionLoop;
	
	public static Map map = new Map(25, 20);
	public static int mapPixelWidth = 0, mapPixelHeight = 0;
	private static BitmapFont score;
	private MapCanvas mapCanvas;
		
	static
	{
		try
		{
			score = new BitmapFont(ImageIO.read(new File("res/gfx/fonts/score.png")), "0123456789", new Color(255, 0, 255));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public MapScene(Map map)
	{
		super();
		
		rs = new RenderStrategy() {
			@Override
			public void drawAll(Graphics g)
			{
				mapCanvas.render(g, MapScene.this);
				Graphics2D g2D = (Graphics2D) g;
				
				for(Drawable d : drawables)
					d.drawToScreen(g2D, MapScene.this);
				
				g2D.dispose();
			}
		};
		
		MapScene.map = map;
		mapPixelWidth = map.getPixelWidth();
		mapPixelHeight = map.getPixelHeight();
		
		mapCanvas = new MapCanvas(map);
		updatables.add(mapCanvas);
		
		add(mapCanvas, BorderLayout.CENTER);
		
		interactionLoop = new Timer("interactionLoop");
		interactionLoop.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run()
			{
				for(Entity e1 : entities)
					for(Entity e2 : entities)
						if(e1 != e2 && e1.intersects(e2))
							queueInteraction(new Interaction(e1, e2));
			}
		}, TIMER_DELAY, FRAME_DELAY);
	}
	
	@Override
	protected void preLoop()
	{
		super.preLoop();
		ScreenManager.setTitle("Super Mario War: Infinity");
		ScreenManager.setWindowed();
	}
	
	@Override
	public void forceStop()
	{
		super.forceStop();
		interactionLoop.cancel();
	}
	
	@Override
	protected void sceneLoop(long timePassed)
	{
		super.sceneLoop(timePassed);
		
		while(!interactions.isEmpty())
			interactions.poll().interact();
		
		while(!eventQueue.isEmpty())
			eventQueue.poll().run();
	}
	
	public static void queueInteraction(Interaction i)
	{
		interactions.offer(i);
	}
	
	public static void queueEvent(Runnable r)
	{
		eventQueue.offer(r);
	}
	
	public static void addEntity(final Entity e)
	{
		queueEvent(new Runnable() {
			@Override
			public void run()
			{
				entities.add(e);
				drawables.add(e);
				updatables.add(e);
			}
		});
	}
	
	public static void removeEntity(final Entity e)
	{
		queueEvent(new Runnable() {
			@Override
			public void run()
			{
				entities.remove(e);
				drawables.remove(e);
				updatables.remove(e);
			}
		});
	}
}
