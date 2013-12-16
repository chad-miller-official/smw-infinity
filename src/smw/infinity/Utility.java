package smw.infinity;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public final class Utility
{
	public static final byte HALF_RAD = 1, RAD = 2, THREEHALFS_RAD = 3;
	public static Properties lang;
	public static SMWUncaughtExceptionHandler exHandler = new SMWUncaughtExceptionHandler();
	
	static
	{	
		try
		{
			loadProperties("english");
		}
		catch(SMWException e)
		{
			createDefaultProperties(new Properties());
			
			try
			{
				loadProperties("english");
			}
			catch(SMWException ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	private Utility() throws SMWException
	{
		throw new SMWException("Illegal instantiation of the Utility class.");
	}
	
	public static void loadProperties(String language) throws SMWException
	{
		try
		{
			lang = new Properties();
			lang.load(new FileReader("res/lang_" + language + ".properties"));
		}
		catch(IOException e)
		{
			throw new SMWException("Failed to load properties file: " + language);
		}
	}
	
	public static void createDefaultProperties(Properties props)
	{
		/* Common */
		props.put("ready", "Ready!");
		props.put("back", "Back");
		props.put("pause-back", "Back to menu");
		props.put("continue", "Continue game");
		props.put("pause", "Pause");

		/* Utility */
		props.put("os error", "Your OS is not supported by LWJGL.");

		/* New Menu Panel */
		props.put("start game", "Start Game");
		props.put("options", "Options");
		props.put("quit", "Quit Game");

		/* Map Chooser Panel */
		props.put("start", "Start");

		/* Keybind Menu Panel */
		props.put("up", "Up");
		props.put("down", "Down");
		props.put("left", "Left");
		props.put("right", "Right");
		props.put("turbo", "Turbo");
		props.put("reserve", "Reserve");
		props.put("get default", "Use Defaults");
		props.put("save", "Save");

		/* Output Footer */
		props.put("credits", "Super Mario War: Infinity by:\nEON8ight, JM Dragon, and JJames19119\n72dpiarmy.supersanctuary.net");
		props.put("extra credits", "");
		
		try
		{
			props.store(new FileWriter("res/lang_english.properties"), "This is the default auto-generated language file.\r\n");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void initLWJGL()
	{
		String os = null;
		String opSystem = System.getProperty("os.name").toLowerCase();
		
		if(opSystem.contains("win"))
			os = "windows";
		else if(opSystem.contains("mac"))
			os = "macosx";
		else if(opSystem.contains("nix") || opSystem.contains("nux"))
			os = "linux";
		else if(opSystem.contains("sunos"))
			os = "solaris";
		else
		{
			System.err.println(Utility.lang.getProperty("os error"));
			return;
		}
		
		System.setProperty("org.lwjgl.librarypath", System.getProperty("user.dir") + "/lib/native/" + os);
	}
	
	public static boolean approxEquals(Point2D.Float one, Point2D.Float two)
	{
		if(Float.floatToIntBits(one.x) != Float.floatToIntBits(two.x))
			return false;
		
		if(Float.floatToIntBits(one.y) != Float.floatToIntBits(two.y))
			return false;
		
		return true;
	}
	
	public static BufferedImage getHorizontalFlippedCopy(BufferedImage sprite)
	{
		BufferedImage flippedSprite = new BufferedImage(sprite.getWidth(), sprite.getHeight(), sprite.getType());

		Graphics2D g = flippedSprite.createGraphics();
		g.drawImage(sprite, 0, 0, sprite.getWidth(), sprite.getHeight(), sprite.getWidth(), 0, 0, sprite.getHeight(), null);
		g.dispose();

		return flippedSprite;
	}

	public static Animation getHorizontalFlippedCopy(Animation anim)
	{
		try
		{
			BufferedImage[] frames = anim.getFrames();

			for(int i = 0; i < frames.length; i++)
				frames[i] = getHorizontalFlippedCopy(frames[i]);

			return new Animation(anim.getFrameRate(), frames);
		}
		catch(SMWException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public static BufferedImage getVerticalFlippedCopy(BufferedImage sprite)
	{
        BufferedImage flippedSprite = new BufferedImage(sprite.getWidth(), sprite.getHeight(), sprite.getType());

        Graphics2D g = flippedSprite.createGraphics();
        g.drawImage(sprite, 0, 0, sprite.getWidth(), sprite.getHeight(), 0, sprite.getHeight(), sprite.getWidth(), 0, null);
        g.dispose();

        return flippedSprite;
    }

	public static Animation getVerticalFlippedCopy(Animation anim)
	{
		try
		{
			BufferedImage[] frames = anim.getFrames();

			for(int i = 0; i < frames.length; i++)
				frames[i] = getVerticalFlippedCopy(frames[i]);

			return new Animation(anim.getFrameRate(), frames);
		}
		catch(SMWException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public static BufferedImage rotateImage90(BufferedImage image, byte mult)
	{
		int width = image.getWidth(), height = image.getHeight();
		BufferedImage rotatedImage = (mult % 2 == 0) ? new BufferedImage(width, height, image.getType()) : new BufferedImage(height, width, image.getType());
		Graphics2D g = rotatedImage.createGraphics();
		g.rotate(Math.toRadians(90 * mult), image.getWidth() / 2, image.getHeight() / 2);
		int offset = (image.getWidth() - image.getHeight()) / 2;

		if(mult > 2)
			offset = -offset;

		g.drawImage(image, null, offset, offset);
		g.dispose();
		return rotatedImage;
	}

	public static Animation rotateAnimation90(Animation anim, byte mult)
	{
		try
		{
			BufferedImage[] frames = anim.getFrames();

			for(int i = 0; i < frames.length; i++)
				frames[i] = rotateImage90(frames[i], mult);

			return new Animation(anim.getFrameRate(), frames);
		}
		catch(SMWException e)
		{
			e.printStackTrace();
		}

		return null;
	}
}