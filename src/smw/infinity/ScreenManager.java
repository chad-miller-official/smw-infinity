package smw.infinity;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.MenuBar;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public final class ScreenManager
{
	private static GraphicsDevice graphicsCard          = null;
	private static GraphicsConfiguration graphicsConfig = null;

	public static Panel masterPanel = null;
	private static Frame frame      = null;

	private static Scene currentScene = null;

	private static boolean alreadyInit = false;
	private static boolean stopped     = true;

	private ScreenManager() throws SMWException
	{
		throw new SMWException( "Can't instantiate ScreenManager!" );
	}

	public static void init( Scene sc )
	{
		if( alreadyInit )
			return;

		currentScene = sc;

		graphicsCard   = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		graphicsConfig = graphicsCard.getDefaultConfiguration();

		frame = new Frame( sc.getTitle() );
		frame.setResizable( false );
		frame.setFocusable( true );
		frame.setIgnoreRepaint( true );

		frame.addWindowListener(new WindowListener() {
			@Override
			public void windowActivated( WindowEvent e )
			{
				return;
			}

			@Override
			public void windowClosed( WindowEvent e )
			{
				return;
			}

			@Override
			public void windowClosing( WindowEvent e )
			{
				EventQueue.invokeLater( () -> stop() );
				System.exit( 0 );
			}

			@Override
			public void windowDeactivated( WindowEvent e )
			{
				return;
			}

			@Override
			public void windowDeiconified( WindowEvent e )
			{
				return;
			}

			@Override
			public void windowIconified( WindowEvent e )
			{
				return;
			}

			@Override
			public void windowOpened( WindowEvent e )
			{
				return;
			}
		});

		masterPanel = new Panel( new BorderLayout() );
		masterPanel.add( sc.getCanvas(), BorderLayout.CENTER );

		frame.add( masterPanel );

		//Add scene-specific components and initialize scene
		MenuBar mb = sc.getMenuBar();

		if( mb != null )
			frame.setMenuBar( mb );

		SMWComponent[] sceneComps = sc.getComponents();

		for( SMWComponent comp : sceneComps )
			addComponent( comp.getAddable(), comp.getConstraint() );

		frame.pack();

		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation( ( d.width - frame.getWidth() ) / 2, ( ( d.height - frame.getHeight() ) / 2 ) - 16 );
		sc.init();

		alreadyInit = true;
	}

	public static BufferedImage createCompatibleImage( String url ) throws IOException
	{
		BufferedImage orig = ImageIO.read( new File( url ) );
		BufferedImage toReturn = graphicsConfig
							     .createCompatibleImage( orig.getWidth(), orig.getHeight(), orig.getTransparency() );

		Graphics2D g2D = toReturn.createGraphics();
		g2D.drawImage( orig, 0, 0, orig.getWidth(), orig.getHeight(), null );
		g2D.dispose();
		return toReturn;
	}

	public static VolatileImage createCompatibleVolatileImage( String url ) throws IOException
	{
		BufferedImage orig     = ImageIO.read( new File( url ) );
		VolatileImage toReturn = graphicsConfig
								 .createCompatibleVolatileImage( orig.getWidth(), orig.getHeight(), orig.getTransparency() );

		Graphics2D g2D = toReturn.createGraphics();
		g2D.drawImage(orig, 0, 0, orig.getWidth(), orig.getHeight(), null);
		g2D.dispose();
		return toReturn;
	}

	public static void start()
	{
		if( !stopped )
			return;

		stopped = true;

		frame.setVisible( true );
		currentScene.start();
	}

	public static void stop()
	{
		if( stopped )
			return;

		stopped = true;

		if( currentScene != null )
			currentScene.stop();

		frame.setVisible( false );
		frame.dispose();
	}

	public static void addComponent( Component comp, Object constraints )
	{
		masterPanel.add( comp, constraints );
	}

	public static void removeComponent( Component comp )
	{
		masterPanel.remove( comp );
	}

	@Override
	protected void finalize() throws Throwable
	{
		stop();
		super.finalize();
	}
}
