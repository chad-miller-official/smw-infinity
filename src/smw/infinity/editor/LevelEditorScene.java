package smw.infinity.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import smw.infinity.Scene;
import smw.infinity.ScreenManager;
import smw.infinity.Updatable;
import smw.infinity.map.Tile;

public class LevelEditorScene extends Scene implements MouseListener, MouseMotionListener
{
	private int mouseX, mouseY, mouseX32, mouseY32;
	private boolean mouseOnScreen;
	
	private TilesetViewer tilesetViewer;
	
	public LevelEditorScene()
	{
		super("Level Editor");
		
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		
		mouseX = mouseY = mouseX32 = mouseY32 = 0;
		mouseOnScreen = false;
		
		tilesetViewer = new TilesetViewer();
	}
	
	@Override
	public void init()
	{
		super.init();
		ScreenManager.addComponent(tilesetViewer, BorderLayout.LINE_END);
	}
	
	@Override
	public void update(long delta)
	{
		for(Updatable u : updatables)
			u.update(delta);
		
		tilesetViewer.update(delta);
	}
	
	@Override
	public void render()
	{
		Graphics2D g2D = (Graphics2D) buffer.getDrawGraphics();
		g2D.setColor(Color.WHITE);
		g2D.fillRect(0, 0, ScreenManager.DEFAULT_WINDOW_WIDTH, ScreenManager.DEFAULT_WINDOW_HEIGHT);
		
		if(mouseOnScreen)
		{
			g2D.setColor(Color.BLACK);
			g2D.drawRect(mouseX32, mouseY32, Tile.TILE_SIZE, Tile.TILE_SIZE);
		}
		
		g2D.dispose();
		tilesetViewer.render();
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		e.consume();
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		mouseOnScreen = true;
		e.consume();
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		mouseOnScreen = false;
		e.consume();
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		e.consume();
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		e.consume();
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		e.consume();
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		mouseX = e.getX();
		mouseY = e.getY();
		
		e.consume();
		
		mouseX32 = (mouseX / Tile.TILE_SIZE) * 32;
		mouseY32 = (mouseY / Tile.TILE_SIZE) * 32;
	}
}
