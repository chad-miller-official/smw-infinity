package smw.infinity;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public final class ScreenManager
{
	public static final int DEFAULT_WINDOW_WIDTH = 800, DEFAULT_WINDOW_HEIGHT = 640;
	public static final Dimension DEFAULT_DIMENSION = new Dimension(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
	
	private static Frame frame = null;
	private static GraphicsDevice videoCard = null;
	private static BufferStrategy bs = null;
	private static DisplayMode defaultMode = null, preferredMode = null;
	private static ScreenMode screenMode = ScreenMode.UNINIT;
	
	private static Scene currentScene = null;
	
	private ScreenManager() throws SMWException
	{
		throw new SMWException("Cannot instantiate ScreenManager.");
	}
	
	public static void init()
	{
		videoCard = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		defaultMode = videoCard.getDisplayMode();
		
		DisplayMode[] modes = { new DisplayMode(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT, 32, DisplayMode.REFRESH_RATE_UNKNOWN),
				new DisplayMode(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT, 24, DisplayMode.REFRESH_RATE_UNKNOWN),
				new DisplayMode(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT, 16, DisplayMode.REFRESH_RATE_UNKNOWN)
		};
		
		modeLoop:
		{
			for(DisplayMode m1 : modes)
				for(DisplayMode m2 : videoCard.getDisplayModes())
					if(m1.equals(m2))
					{
						preferredMode = m1;
						break modeLoop;
					}
			
			preferredMode = null;
		}
		
		currentScene = null;
		
		frame = new Frame();
		frame.setResizable(false);
		frame.setFocusable(true);
		frame.setIgnoreRepaint(true);
		
		frame.addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e)
			{
				return;
			}

			@Override
			public void windowClosing(WindowEvent e)
			{
				setScene(null);
				dispose();
				Scene.quit();
			}

			@Override
			public void windowClosed(WindowEvent e)
			{
				return;
			}

			@Override
			public void windowIconified(WindowEvent e)
			{
				return;
			}

			@Override
			public void windowDeiconified(WindowEvent e)
			{
				return;
			}

			@Override
			public void windowActivated(WindowEvent e)
			{
				return;
			}

			@Override
			public void windowDeactivated(WindowEvent e)
			{
				return;
			}
		});
	}
	
	public static void setFullscreen() throws SMWException
	{
		if(screenMode == ScreenMode.FULLSCREEN)
			return;
		
		if(!videoCard.isFullScreenSupported())
			throw new SMWException("Full screen mode not supported!");
		
		frame.setVisible(false);
		videoCard.setFullScreenWindow(frame);
		
		if(videoCard.isDisplayChangeSupported())
		{
			try
			{
				videoCard.setDisplayMode(preferredMode);
			}
			catch(IllegalArgumentException e)
			{
				videoCard.setDisplayMode(defaultMode);
			}
		}
		
		if(bs != null)
			bs.dispose();
		
		videoCard.getFullScreenWindow().createBufferStrategy(3);
		bs = videoCard.getFullScreenWindow().getBufferStrategy();
		screenMode = ScreenMode.FULLSCREEN;
		videoCard.getFullScreenWindow().revalidate();
	}
	
	public static void setWindowed()
	{
		if(screenMode == ScreenMode.WINDOWED)
			return;
		
		if(videoCard.isDisplayChangeSupported())
			videoCard.setDisplayMode(defaultMode);
		
		videoCard.setFullScreenWindow(null);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((d.width - frame.getWidth()) / 2, ((d.height - frame.getHeight()) / 2) - 16);
		frame.setVisible(true);
		
		if(bs != null)
			bs.dispose();
		
		frame.createBufferStrategy(3);
		bs = frame.getBufferStrategy();
		screenMode = ScreenMode.WINDOWED;
		frame.revalidate();
	}
	
	public static boolean isFullscreen()
	{
		return (screenMode == ScreenMode.FULLSCREEN);
	}
	
	public static Graphics getGraphics()
	{
		return bs.getDrawGraphics();
	}
	
	public static void render(RenderStrategy rs)
	{
		if(bs == null || bs.contentsLost())
			return;
		
		Graphics g = bs.getDrawGraphics();
		rs.render(g);
		bs.show();
		g.dispose();
	}
	
	public static void dispose()
	{
		frame.setVisible(false);
		videoCard.setFullScreenWindow(null);
		
		if(bs != null)
			bs.dispose();
		
		frame.dispose();
	}
	
	public static BufferedImage createCompatibleImage(int width, int height, int alpha)
	{
		return frame.getGraphicsConfiguration().createCompatibleImage(width, height, alpha);
	}
	
	public static void setScene(Scene s)
	{
		if(currentScene != null)
		{
			currentScene.forceStop();
			frame.remove(currentScene);
		}
		
		if(s != null)
		{
			frame.add(s);
			frame.pack();
		}
		
		frame.revalidate();
		currentScene = s;
	}
	
	public static void setTitle(String title)
	{
		frame.setTitle(title);
	}
	
	public static void pack()
	{
		frame.pack();
	}
	
	public static Frame getFrame()
	{
		return frame;
	}
	
	@Override
	protected void finalize() throws Throwable
	{
		dispose();
		super.finalize();
	}
	
	private enum ScreenMode
	{
		FULLSCREEN,
		WINDOWED,
		UNINIT;
	}
}