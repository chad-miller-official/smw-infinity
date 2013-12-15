package smw.infinity;

import static paulscode.sound.SoundSystemConfig.ATTENUATION_NONE;
import static paulscode.sound.SoundSystemConfig.addLibrary;
import static paulscode.sound.SoundSystemConfig.getDefaultRolloff;
import static paulscode.sound.SoundSystemConfig.setCodec;
import static paulscode.sound.SoundSystemConfig.setSoundFilesPackage;

import java.io.File;
import java.net.MalformedURLException;

import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemException;
import paulscode.sound.codecs.CodecJOgg;
import paulscode.sound.codecs.CodecWav;
import paulscode.sound.libraries.LibraryLWJGLOpenAL;

public final class SoundPlayer
{
	public static final String SFX_PATH = "res/sfx/packs/";
	private static final String BGM = "bgm", START = "start";
	private static SoundSystem player = null;
	private static String sfxPack = null;
	
	static
	{
		try
		{
			setCodec("ogg", CodecJOgg.class);
			setCodec("wav", CodecWav.class);
			addLibrary(LibraryLWJGLOpenAL.class);
		}
		catch(SoundSystemException e)
		{
			e.printStackTrace();
		}
		
		player = new SoundSystem();
	}
	
	private SoundPlayer() throws SMWException
	{
		throw new SMWException("Cannot instantiate SoundPlayer.");
	}
	
	public static void setSFXPack(String sfxPack)
	{
		setSoundFilesPackage(new File(SFX_PATH + sfxPack).toURI().toString());
		SoundPlayer.sfxPack = sfxPack;
	}
	
	public static void playSound(String sound)
	{
		if(player.playing(sound))
		{
			try
			{
				player.quickPlay(false, new File(SFX_PATH + sfxPack + "/" + sound + ".wav").toURI().toURL(), sound + ".wav", false, 0, 0, 0, ATTENUATION_NONE, getDefaultRolloff());
			}
			catch (MalformedURLException e)
			{
				e.printStackTrace();
			}
		}
		else
			player.play(sound);
	}
	
	public static void stopSound(String sound)
	{
		player.stop(sound);
	}
	
	public static void playMusic(File music, boolean loop)
	{
		try
		{
			player.backgroundMusic(BGM, music.toURI().toURL(), "ogg", loop);
		}
		catch(MalformedURLException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void playMusic(File start, final File loop)
	{
		try
		{
			player.backgroundMusic(START, start.toURI().toURL(), "ogg", false);
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		
		new Thread(new Runnable() {
			@Override
			public void run()
			{
				while(player.playing(START));
				
				playMusic(loop, true);
			}
		}).start();
	}
	
	public static void stopMusic()
	{
		if(player.playing(START))
			player.stop(START);
		
		if(player.playing(BGM))
			player.stop(BGM);
	}
	
	public static void restartMusic()
	{
		player.play(BGM);
	}
	
	public static void close()
	{
		if(player != null)
			player.cleanup();
	}
	
	@Override
	protected void finalize() throws Throwable
	{
		close();
		super.finalize();
	}
}