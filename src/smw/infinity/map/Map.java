package smw.infinity.map;

import java.awt.Graphics2D;
import java.awt.image.ImageObserver;

public class Map extends Tileform
{
	private static final long serialVersionUID = 8546227846724513314L;
	private String mapName;
	private InteractiveTile[][] iTiles;
	
	public Map(int tileWidth, int tileHeight)
	{
		super(tileWidth, tileHeight);
		iTiles = new InteractiveTile[tileWidth][tileHeight];
	}
	
	public void setITile(int x, int y, InteractiveTile iTile)
	{
		iTiles[x][y] = iTile;
	}
	
	public String getMapName()
	{
		return mapName;
	}
	
	public void setMapName(String mapName)
	{
		this.mapName = mapName;
	}
	
	@Override
	public synchronized void update(long timePassed)
	{
		super.update(timePassed);
		
		for(InteractiveTile[] t : iTiles)
			for(InteractiveTile t1 : t)
				if(t1 != null)
					t1.update(timePassed);
	}
	
	@Override
	public void drawToScreen(Graphics2D g2D, int xPos, int yPos, ImageObserver i)
	{
		super.drawToScreen(g2D, xPos, yPos, i);
		
		for(int ix = 0; ix < tileWidth; ix++)
			for(int j = 0; j < tileHeight; j++)
				if(iTiles[ix][j] != null)
					g2D.drawImage(iTiles[ix][j].getImage(), xPos + (ix * Tile.TILE_SIZE), yPos + (j * Tile.TILE_SIZE), i);
	}
}
