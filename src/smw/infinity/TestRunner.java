package smw.infinity;

import java.awt.Color;
import java.awt.Graphics2D;

public class TestRunner
{
	public static void main(String[] args)
	{
		Scene s = new Scene();
		s.addDrawable(new TestDrawable());
		s.run();
		ScreenManager.init();
		ScreenManager.setCurrentScene(s);
		ScreenManager.setVisible(true);
	}
	
	private static class TestDrawable implements Drawable
	{
		@Override
		public void drawToScreen(Graphics2D g2D)
		{
			g2D.setColor(Color.GREEN);
			g2D.fill3DRect(16, 16, 128, 64, true);
		}
	}
}