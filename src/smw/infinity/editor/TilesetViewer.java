package smw.infinity.editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;

import smw.infinity.ScreenManager;
import smw.infinity.Updatable;

public class TilesetViewer extends Component implements Updatable
{
	private static final long serialVersionUID = -7404915507606671525L;
	
	private static final int TILESET_VIEWER_WIDTH = 192;
	private static final int TILESET_VIEWER_HEIGHT = ScreenManager.DEFAULT_WINDOW_HEIGHT;
	private static final Dimension TILESET_VIEWER_DIMENSION = new Dimension(TILESET_VIEWER_WIDTH, TILESET_VIEWER_HEIGHT);
	
	public TilesetViewer()
	{
		super();
		
		setPreferredSize(TILESET_VIEWER_DIMENSION);
		setMinimumSize(TILESET_VIEWER_DIMENSION);
		setMaximumSize(TILESET_VIEWER_DIMENSION);
	}
	
	@Override
	public void update(long delta)
	{
		return;
	}
	
	public void render()
	{
		//test code
		Graphics2D g2D = (Graphics2D) getGraphics();
		g2D.setColor(Color.GREEN);
		g2D.fillRect(0, 0, TILESET_VIEWER_WIDTH, TILESET_VIEWER_HEIGHT);
		g2D.dispose();
	}
}
