package smw.infinity.editor;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;

import smw.infinity.Scene;
import smw.infinity.Updatable;
import smw.infinity.map.Tile;
import smw.infinity.map.Tileset;

public class TilesetViewer extends Canvas implements MouseListener, MouseMotionListener, Updatable
{
	private static final long serialVersionUID = -7404915507606671525L;
	
	private static final int TILESET_VIEWER_WIDTH = 192;
	private static final int TILESET_VIEWER_HEIGHT = Scene.SCENE_HEIGHT;
	private static final Dimension TILESET_VIEWER_DIMENSION = new Dimension(TILESET_VIEWER_WIDTH, TILESET_VIEWER_HEIGHT);
	
	private volatile BufferStrategy buffer;
	private int mouseX, mouseY, mouseX32, mouseY32;
	private boolean mouseInBounds;
	
	private Tile selectedTile;
	private Tileset currentTileset;
	
	public TilesetViewer(String tileset)
	{
		super();
		
		setPreferredSize(TILESET_VIEWER_DIMENSION);
		setMinimumSize(TILESET_VIEWER_DIMENSION);
		setMaximumSize(TILESET_VIEWER_DIMENSION);
		
		currentTileset = Tileset.getTileset(tileset);
		
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void init()
	{
		createBufferStrategy(3);
		buffer = getBufferStrategy();
	}
	
	@Override
	public void update(long delta)
	{
		return;
	}
	
	public void render()
	{
		//test code
		Graphics2D g2D = (Graphics2D) buffer.getDrawGraphics();
		g2D.setColor(Color.WHITE);
		g2D.fillRect(0, 0, Scene.SCENE_WIDTH, Scene.SCENE_HEIGHT);
		
		currentTileset.drawToScreen(g2D, 0, 0);
		
		if(mouseInBounds)
		{
			g2D.setColor(Color.BLACK);
			g2D.drawRect(mouseX32 * Tile.TILE_SIZE, mouseY32 * Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
		}
		
		g2D.dispose();
		buffer.show();
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
		selectedTile = currentTileset.getTile((short) mouseX32, (short) mouseY32);
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
	
	public Tile getSelectedTile()
	{
		return selectedTile;
	}
}
