import smw.infinity.editor.LevelEditorScene;

public class LevelEditorRunner
{
	public static void main(String[] args)
	{
		new Thread(new LevelEditorScene()).start();
	}
}