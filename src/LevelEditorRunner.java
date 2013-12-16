import java.awt.EventQueue;

import smw.infinity.Scene;
import smw.infinity.ScreenManager;
import smw.infinity.SoundPlayer;
import smw.infinity.Utility;
import smw.infinity.editor.LevelEditorScene;

public class LevelEditorRunner
{
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run()
			{
				Thread.currentThread().setUncaughtExceptionHandler(Utility.exHandler);
				Utility.initLWJGL();
				SoundPlayer.setSFXPack(Scene.sfxPackName);
				
				LevelEditorScene le = new LevelEditorScene();
				le.init();
				
				ScreenManager.init();
				ScreenManager.setWindowed();
				ScreenManager.setScene(le);
				
				new Thread(le, "Level Editor Thread").start();
			}
		});
	}
}