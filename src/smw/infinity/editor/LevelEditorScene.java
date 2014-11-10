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
import smw.infinity.map.Map;
import smw.infinity.map.Tile;

public class LevelEditorScene extends Scene implements MouseListener, MouseMotionListener
{
	private int mouseX, mouseY, mouseX32, mouseY32;
	private boolean mouseInBounds;
	
	private Map map;
	private TilesetViewer tilesetViewer;
	
	public LevelEditorScene()
	{
		super("Level Editor");
		
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		
		mouseX = mouseY = mouseX32 = mouseY32 = 0;
		mouseInBounds = false;
		
		map = new Map();
		tilesetViewer = new TilesetViewer("Classic");
	}
	
	@Override
	public void init()
	{
		super.init();
		ScreenManager.addComponent(tilesetViewer, BorderLayout.LINE_END);
		tilesetViewer.init();
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
		g2D.fillRect(0, 0, Scene.SCENE_WIDTH, Scene.SCENE_HEIGHT);
		
		for(int i = 0; i < Map.MAP_WIDTH; i++)
			for(int j = 0; j < Map.MAP_HEIGHT; j++)
				if(map.getTile(i, j) != null)
					map.getTile(i, j).drawToScreen(g2D, i * Tile.TILE_SIZE, j * Tile.TILE_SIZE);
		
		if(mouseInBounds)
		{
			g2D.setColor(Color.BLACK);
			g2D.drawRect(mouseX32 * Tile.TILE_SIZE, mouseY32 * Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
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
		mouseInBounds = true;
		e.consume();
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		mouseInBounds = false;
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
		if(e.getButton() == MouseEvent.BUTTON1)
			map.setTile(tilesetViewer.getSelectedTile(), mouseX32, mouseY32);
		else if(e.getButton() == MouseEvent.BUTTON3)
			map.setTile(null, mouseX32, mouseY32);
		
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
		
		mouseX32 = mouseX / Tile.TILE_SIZE;
		mouseY32 = mouseY / Tile.TILE_SIZE;
	}
}
