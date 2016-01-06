package smw.infinity.editor;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.ScrollPane;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import smw.infinity.SMWComponent;
import smw.infinity.Scene;
import smw.infinity.Updatable;
import smw.infinity.map.Tile;
import smw.infinity.map.Tileset;

public class TilesetViewer extends Canvas implements MouseListener, MouseMotionListener, SMWComponent, Updatable
{
	private static final long serialVersionUID = -7404915507606671525L;

	public static final int TILESET_VIEWER_PANE_WIDTH  = 256;
	public static final int TILESET_VIEWER_PANE_HEIGHT = Scene.SCENE_HEIGHT;

	public static final Dimension TILESET_VIEWER_PANE_DIMENSION
		= new Dimension( TILESET_VIEWER_PANE_WIDTH, TILESET_VIEWER_PANE_HEIGHT );

	private ScrollPane scrollPane;

	private volatile BufferStrategy buffer;

	private int mouseX;
	private int mouseY;
	private int mouseX32;
	private int mouseY32;

	private boolean mouseInBounds;

	private Tile selectedTile;
	private Tileset currentTileset;

	private boolean interactiveMode;
	private boolean alreadyInit;

	public TilesetViewer( String tileset )
	{
		super();

		currentTileset = Tileset.getTileset( tileset );

		setPreferredSize( currentTileset.getDimensions() );
		setMinimumSize( currentTileset.getDimensions() );
		setMaximumSize( currentTileset.getDimensions() );

		addMouseListener( this );
		addMouseMotionListener( this );

		scrollPane = new ScrollPane( ScrollPane.SCROLLBARS_AS_NEEDED );
		scrollPane.setPreferredSize( TilesetViewer.TILESET_VIEWER_PANE_DIMENSION );
		scrollPane.setMinimumSize( TilesetViewer.TILESET_VIEWER_PANE_DIMENSION );
		scrollPane.setMaximumSize( TilesetViewer.TILESET_VIEWER_PANE_DIMENSION );
		scrollPane.add( this );

		interactiveMode = false;
		alreadyInit     = false;
	}

	public void init()
	{
		if( alreadyInit )
			return;

		createBufferStrategy( 3 );
		buffer = getBufferStrategy();

		alreadyInit = true;
	}

	public void stop()
	{
		buffer.dispose();
	}

	@Override
	public void update( long delta )
	{
		if( currentTileset instanceof Updatable )
			( (Updatable) currentTileset ).update( delta );

		if( selectedTile instanceof Updatable )
			( (Updatable) selectedTile).update( delta );
	}

	public void render()
	{
		Point p = scrollPane.getScrollPosition();

		Graphics2D g2D = (Graphics2D) buffer.getDrawGraphics();
		g2D.setColor( Color.WHITE );
		g2D.fillRect( p.x, p.y, TILESET_VIEWER_PANE_WIDTH, TILESET_VIEWER_PANE_HEIGHT );

		currentTileset.drawToScreen( g2D, 0, 0 );

		if( mouseInBounds )
		{
			g2D.setColor( Color.BLACK );
			g2D.drawRect( mouseX32 * Tile.TILE_SIZE, mouseY32 * Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE );
		}

		g2D.dispose();
		buffer.show();
	}

	@Override
	public void mouseClicked( MouseEvent e )
	{
		e.consume();
	}

	@Override
	public void mouseEntered( MouseEvent e )
	{
		mouseInBounds = true;
		e.consume();
	}

	@Override
	public void mouseExited( MouseEvent e )
	{
		mouseInBounds = false;
		e.consume();
	}

	@Override
	public void mousePressed( MouseEvent e )
	{
		e.consume();
	}

	@Override
	public void mouseReleased( MouseEvent e )
	{
		if( inBounds( mouseX, mouseY ) )
			selectedTile = currentTileset.getTile( (short) mouseX32, (short) mouseY32 );

		e.consume();
	}

	@Override
	public void mouseDragged( MouseEvent e )
	{
		e.consume();
	}

	@Override
	public void mouseMoved( MouseEvent e )
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

	@Override
	public Component getAddable()
	{
		return scrollPane;
	}

	@Override
	public Object getConstraint()
	{
		return BorderLayout.LINE_END;
	}

	public void changeTileset( String tilesetName )
	{
		currentTileset = Tileset.getTileset( tilesetName );

		setPreferredSize( currentTileset.getDimensions() );
		setMinimumSize( currentTileset.getDimensions() );
		setMaximumSize( currentTileset.getDimensions() );

		scrollPane.revalidate();
	}

	private boolean inBounds( int x, int y )
	{
		return x >= 0
			&& x <= currentTileset.getWidth()
			&& y >= 0
			&& y <= currentTileset.getHeight();
	}

	public boolean getInteractiveMode()
	{
		return interactiveMode;
	}

	public void setInteractiveMode( boolean interactiveMode )
	{
		this.interactiveMode = interactiveMode;
	}
}
