package smw.infinity.editor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import smw.infinity.SMWComponent;
import smw.infinity.Scene;
import smw.infinity.Updatable;
import smw.infinity.map.AnimatedTile;
import smw.infinity.map.InteractiveTile;
import smw.infinity.map.Map;
import smw.infinity.map.Tile;
import smw.infinity.map.Tileset;

public class LevelEditorScene extends Scene implements MouseListener, MouseMotionListener
{
	private int mouseX, mouseY, mouseX32, mouseY32;
	private boolean mouseInBounds;
	
	private Map map;
	
	private MenuBar menuBar;
	private Menu tilesetMenu;
	private TilesetViewer tilesetViewer;
	
	public LevelEditorScene()
	{
		super("Level Editor");
		
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		
		mouseX = mouseY = mouseX32 = mouseY32 = 0;
		mouseInBounds = false;
		
		map = new Map();
		
		menuBar = new MenuBar();
		tilesetMenu = new Menu("Tilesets", true);
		
		MenuItem ml = new MenuItem("Interactive");
		ml.addActionListener((event) -> {
			tilesetViewer.changeTileset("Interactive");
			tilesetViewer.setInteractiveMode(true);
		});
		tilesetMenu.add(ml);
		
		tilesetMenu.addSeparator();
		
		for(Tileset t : Tileset.activeTilesets)
		{
			if(t.getName().equals("Interactive"))
				continue;
			
			String name = t.getName();
			ml = new MenuItem(name);
			ml.addActionListener((event) -> {
				tilesetViewer.changeTileset(name);
				tilesetViewer.setInteractiveMode(false);
			});
			tilesetMenu.add(ml);
		}
		
		menuBar.add(tilesetMenu);
		tilesetViewer = new TilesetViewer("Classic");
		
		updatables.add(map);
		updatables.add(tilesetViewer);
	}
	
	@Override
	public void init()
	{
		super.init();
		tilesetViewer.init();
	}
	
	@Override
	public void stop()
	{
		super.stop();
		tilesetViewer.stop();
	}
	
	@Override
	public SMWComponent[] getComponents()
	{
		return new SMWComponent[]{ tilesetViewer };
	}
	
	@Override
	public void update(long delta)
	{
		for(Updatable u : updatables)
			u.update(delta);
	}
	
	@Override
	public void render()
	{
		Graphics2D g2D = (Graphics2D) buffer.getDrawGraphics();
		g2D.setColor(Color.WHITE);
		g2D.fillRect(0, 0, Scene.SCENE_WIDTH, Scene.SCENE_HEIGHT);
		
		map.drawToScreen(g2D, 0, 0);
		
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
		{
			Tile toPlace = tilesetViewer.getSelectedTile();
			
			if(tilesetViewer.getInteractiveMode())
				map.setTile(((InteractiveTile) toPlace).clone(), Map.INTERACTIVE_LAYER, mouseX32, mouseY32);
			else
			{
				if(toPlace instanceof AnimatedTile)
					map.setTile(((AnimatedTile) toPlace).clone(), 1, mouseX32, mouseY32);
				else if(toPlace instanceof Tile)
					map.setTile(toPlace, 1, mouseX32, mouseY32);
			}
		}
		else if(e.getButton() == MouseEvent.BUTTON3)
			map.setTile(null, tilesetViewer.getInteractiveMode() ? Map.INTERACTIVE_LAYER : 1, mouseX32, mouseY32);
		
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
	
	@Override
	public MenuBar getMenuBar()
	{
		return menuBar;
	}
}
