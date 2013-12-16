package smw.infinity.map;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Set;

import smw.infinity.Drawable;
import smw.infinity.Interaction;
import smw.infinity.InteractionQueue;
import smw.infinity.RenderStrategy;
import smw.infinity.Scene;
import smw.infinity.Updatable;
import smw.infinity.entity.Entity;

public class MapScene extends Scene
{
	private static final long serialVersionUID = -2917820952939407626L;
	
	public static Map map = null;
	public static int mapPixelWidth = 0, mapPixelHeight = 0;
	
	private static volatile Set<Drawable> drawables = new HashSet<Drawable>();
	private static volatile Set<Updatable> updatables = new HashSet<Updatable>();
	private static volatile Set<Entity> entities = new HashSet<Entity>();
	
	private static InteractionQueue interactions = new InteractionQueue();
	
	public MapScene(Map map)
	{
		super();
		
		MapScene.map = map;
		mapPixelWidth = map.getPixelWidth();
		mapPixelHeight = map.getPixelHeight();
	}

	@Override
	protected RenderStrategy createRenderStrategy()
	{
		return new RenderStrategy() {
			@Override
			public void render(Graphics g)
			{
				Graphics2D g2D = (Graphics2D) g;
				
				for(Entity e : entities)
					e.drawToScreen(g2D, MapScene.this);
				
				g2D.dispose();
			}
		};
	}

	@Override
	public void init()
	{
		//TODO
	}

	@Override
	protected void update(final long timePassed)
	{
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run()
			{
				while(!interactions.isEmpty())
					interactions.poll().interact();
				
				for(Updatable u : updatables)
					u.update(timePassed);
			}
		});
	}
	
	public static void addEntity(Entity e)
	{
		entities.add(e);
		updatables.add(e);
		drawables.add(e);
	}
	
	public static void removeEntity(Entity e)
	{
		entities.remove(e);
		updatables.remove(e);
		drawables.remove(e);
	}
	
	public static void enqueueInteraction(Interaction i)
	{
		interactions.offer(i);
	}
}
