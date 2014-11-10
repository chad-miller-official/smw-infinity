import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;

import smw.infinity.ScreenManager;
import smw.infinity.editor.LevelEditorScene;
import smw.infinity.map.Tileset;

public class TestRunner
{
	public static void main(String[] args)
	{
		loadTilesets();
		
		LevelEditorScene s = new LevelEditorScene();
		
		EventQueue.invokeLater(() -> {
			ScreenManager.init(s);
			ScreenManager.start();
		});
	}
	
	public static void loadTilesets()
	{
		try
		{
			File tilesetDir = new File(Tileset.TILESET_DIR);
			
			for(File file : tilesetDir.listFiles())
				if(file.isDirectory() && !file.getName().equals("anim"))
					Tileset.loadTileset(file.getName());
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}