package smw.infinity.editor;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.ImageObserver;

import smw.infinity.Updatable;
import smw.infinity.map.Tile;

public abstract class EditorComponent extends Component implements Updatable
{
	private static final long serialVersionUID = -6640466572733688556L;
	protected int mouseX, mouseY;
	protected int mouse32X, mouse32Y;
	protected boolean mouseOnScreen, mouseDown;
	
	public EditorComponent()
	{
		mouseX = mouseY = mouse32X = mouse32Y = 0;
		mouseOnScreen = mouseDown = false;
		setIgnoreRepaint(true);
		setEnabled(true);
		setVisible(true);
	}
	
	public abstract void render(Graphics g, ImageObserver ob);
	
	protected abstract class EditorComponentMouseListener implements MouseListener, MouseMotionListener
	{
		@Override
		public void mouseDragged(MouseEvent e)
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
			mouseDown = true;
			e.consume();
		}

		@Override
		public void mouseReleased(MouseEvent e)
		{
			mouseDown = false;
			e.consume();
		}
		
		@Override
		public void mouseClicked(MouseEvent e)
		{
			e.consume();
		}
		
		@Override
		public void mouseMoved(MouseEvent e)
		{
			mouseX = e.getX();
			mouseY = e.getY();
			mouse32X = (int) (mouseX / Tile.TILE_SIZE);
			mouse32Y = (int) (mouseY / Tile.TILE_SIZE);
			e.consume();
		}
	}
}