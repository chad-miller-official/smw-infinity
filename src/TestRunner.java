import java.awt.EventQueue;

import smw.infinity.ScreenManager;
import smw.infinity.editor.LevelEditorScene;

public class TestRunner
{
	public static void main(String[] args)
	{
		LevelEditorScene s = new LevelEditorScene();
		
		EventQueue.invokeLater(() -> {
			ScreenManager.init(s);
			ScreenManager.start();
		});
	}
}