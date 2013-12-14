package smw.infinity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

public class BitmapFont
{
	private Hashtable<Character, BufferedImage> fontTable;
	private int spaceWidth;
	
	public BitmapFont(BufferedImage fontSheet, String order, Color delimiter)
	{
		this(fontSheet, order, 8, delimiter);
	}
	
	public BitmapFont(BufferedImage fontSheet, String order, int spaceWidth, Color delimiter)
	{
		this.spaceWidth = spaceWidth;
		fontTable = new Hashtable<Character, BufferedImage>();
		int startPos = 0, endPos = 0, lim = delimiter.getRGB();
		
		for(int i = 0, s = order.length(); i < s; i++)
		{
			/*
			 * Find the end position. Special case for when i is one less than the order's
			 * length: set the end position to the end of the sheet. Otherwise, increase the
			 * end position until the delimiter color is hit.
			 */
			if(i != s - 1)
				for(endPos = startPos + 1; fontSheet.getRGB(endPos, 0) != lim; endPos++);
			else
				endPos = fontSheet.getWidth();
			
			//Cut out a letter from the sheet and map it to its proper character.
			fontTable.put(order.charAt(i), fontSheet.getSubimage(startPos, 0, (endPos - startPos), fontSheet.getHeight()));
			
			/*
			 * Reset the start and end position. Skip this entirely if i is one less than the
			 * order's length.
			 */
			if(i != s - 1)
				startPos = endPos + 1;
		}
	}
	
	public BufferedImage getImageFrom(char c)
	{
		return fontTable.get(c);
	}
	
	public void drawString(Graphics2D g2D, String str, float xPos, float yPos)
	{
		char[] arr = str.toCharArray();
		BufferedImage toDraw;
		
		for(int i = 0; i < arr.length; i++)
		{
			if(arr[i] != ' ')
			{
				toDraw = fontTable.get(arr[i]);
				g2D.drawImage(toDraw, Math.round(xPos), Math.round(yPos), null);
				xPos += toDraw.getWidth();
			}
			else
				xPos += spaceWidth;
		}
	}
}