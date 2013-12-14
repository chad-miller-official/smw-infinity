package smw.infinity.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;

import smw.infinity.map.AnimatedTileset;
import smw.infinity.map.Tile;
import smw.infinity.map.TileType;
import smw.infinity.map.Tileset;

public class TilesetViewer extends EditorComponent
{
	private static final long serialVersionUID = -3881092074045360430L;
	private Tileset tileset;
	private Tile tile;
	private TileType tileType;
	
	public TilesetViewer()
	{
		tile = null;
		tileType = null;
		tileset = new Tileset("Classic");
		fitToTileset();
		
		TilesetViewerMouseListener ml = new TilesetViewerMouseListener();
		addMouseListener(ml);
		addMouseMotionListener(ml);
	}
	
	public void fitToTileset()
	{
		Dimension d = tileset.getDimension();
		setPreferredSize(d);
		setSize(d);
		setMinimumSize(d);
		validate();
	}
	
	public void setTileset(String name, boolean animated)
	{
		tileset = animated ? new AnimatedTileset(name) : new Tileset(name);
		fitToTileset();
	}
	
	@Override
	public void render(Graphics g, ImageObserver ob)
	{
		Graphics2D g2D = (Graphics2D) g;
		tileset.drawToScreen(g2D, ob);
		
		if(mouseOnScreen)
		{
			g2D.setColor(Color.WHITE);
			g2D.drawRect(mouse32X * Tile.TILE_SIZE, mouse32Y * Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
		}
		
		g2D.dispose();
	}
	
	public Tile getSelectedTile()
	{
		return tile;
	}
	
	public TileType getSelectedTileType()
	{
		return tileType;
	}
	
	public Tile getTile(int x, int y)
	{
		return tileset.getTile(x, y);
	}
	
	public TileType getTileType(int x, int y)
	{
		return tileset.getType(x, y);
	}

	@Override
	public void update(long timePassed)
	{
		if(tileset instanceof AnimatedTileset)
			((AnimatedTileset) tileset).update(timePassed);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + mouseX;
		result = prime * result + mouseY;
		result = prime * result + ((tile == null) ? 0 : tile.hashCode());
		result = prime * result + ((tileType == null) ? 0 : tileType.hashCode());
		result = prime * result + ((tileset == null) ? 0 : tileset.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		
		if(obj == null)
			return false;
		
		if(getClass() != obj.getClass())
			return false;
		
		TilesetViewer other = (TilesetViewer) obj;
		
		if(mouseX != other.mouseX)
			return false;
		
		if(mouseY != other.mouseY)
			return false;
		
		if(tile == null && other.tile != null)
			return false;
		
		else if (!tile.equals(other.tile))
			return false;
		
		if(tileType != other.tileType)
			return false;
		
		if(tileset == null&& other.tileset != null)
			return false;
		else if (!tileset.equals(other.tileset))
			return false;
		
		return true;
	}
	
	private class TilesetViewerMouseListener extends EditorComponentMouseListener
	{
		@Override
		public void mouseReleased(MouseEvent e)
		{
			tile = getTile(mouse32X, mouse32Y);
			tileType = getTileType(mouse32X, mouse32Y);
			super.mouseReleased(e);
		}
	}
}