package smw.infinity.map;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;

import smw.infinity.Updatable;

public class MapCanvas extends Canvas implements Updatable
{
	private static final long serialVersionUID = -318135444594212036L;
	private Map map;
	
	public MapCanvas(Map map)
	{
		super();
		setMap(map);
		setIgnoreRepaint(true);
		setVisible(true);
	}
	
	public void pack()
	{
		Dimension d = map.getDimensions();
		setPreferredSize(d);
		setMinimumSize(d);
		validate();
	}
	
	public Map getMap()
	{
		return map;
	}
	
	public void setMap(Map map)
	{
		this.map = map;
		pack();
	}
	
	@Override
	public void update(long timePassed)
	{
		map.update(timePassed);
	}

	public void render(Graphics g, ImageObserver ob)
	{
		Graphics2D g2D = (Graphics2D) g;
		map.drawToScreen(g2D, ob);
		g2D.dispose();
	}
}
