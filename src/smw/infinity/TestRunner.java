package smw.infinity;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics2D;

public class TestRunner
{
	public static void main(String[] args)
	{
		Scene s = new Scene("Test");
		s.addDrawable(new TestDrawable());
		
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run()
			{
				ScreenManager.init(s);
				ScreenManager.setVisible(true);
				ScreenManager.start();
			}
		});
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