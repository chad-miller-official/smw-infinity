package smw.infinity.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.ScrollPane;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import smw.infinity.RenderStrategy;
import smw.infinity.Scene;
import smw.infinity.map.Map;
import smw.infinity.map.MapCanvas;
import smw.infinity.map.Tile;

public class LevelEditorScene extends Scene
{
	private static final long serialVersionUID = -7938914211151252837L;
	private Map map;
	private byte layer;
	
	private ScrollPane tilesetsPane;
	
	private MapCanvas mapCanvas;
	private TilesetViewer tilesetViewer;
	
	public LevelEditorScene()
	{
		super();
		
		layer = 0;
		map = new Map(25, 20);
		updatables.add(map);
		
		rs = new RenderStrategy() {
			@Override
			public void render(Graphics g)
			{
				mapCanvas.render(g, LevelEditorScene.this);
				tilesetViewer.render(g, tilesetsPane);
			}
		};
	}
	
	@Override
	protected void init()
	{
		super.init();
		
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run()
			{
				/* GUI DECLARATIONS */
				tilesetsPane = new ScrollPane();
				
				/* COMPONENT DECLARATIONS */
				mapCanvas = new MapCanvas(map);
				mapCanvas.addMouseListener(new MouseListener() {
					@Override
					public void mouseClicked(MouseEvent e)
					{
						e.consume();
					}
		
					@Override
					public void mouseEntered(MouseEvent e)
					{
						e.consume();
					}
		
					@Override
					public void mouseExited(MouseEvent e)
					{
						e.consume();
					}
		
					@Override
					public void mousePressed(MouseEvent e)
					{
						e.consume();
					}
		
					@Override
					public void mouseReleased(MouseEvent e)
					{
						int x = e.getX() / Tile.TILE_SIZE;
						int y = e.getY() / Tile.TILE_SIZE;
						
						switch(e.getButton())
						{
							case MouseEvent.BUTTON1:
								map.setTile(x, y, layer, tilesetViewer.getSelectedTile());
								map.setTileType(x, y, tilesetViewer.getSelectedTileType());
								break;
							case MouseEvent.BUTTON3:
								map.setTile(x, y, layer, null);
								map.setTileType(x, y, null);
								break;
						}
						
						e.consume();
					}
				});
				
				tilesetViewer = new TilesetViewer();
				
				/* SET UP GUI */
				tilesetsPane.add(tilesetViewer);
				tilesetsPane.setSize(new Dimension(256, 640));
				tilesetsPane.getVAdjustable().setUnitIncrement(Tile.TILE_SIZE / 3);
				tilesetsPane.getHAdjustable().setUnitIncrement(Tile.TILE_SIZE / 3);
				tilesetsPane.setVisible(true);
				tilesetsPane.revalidate();
				
				add(mapCanvas, BorderLayout.CENTER);
				add(tilesetsPane, BorderLayout.LINE_END);
				revalidate();
				
				updatables.add(mapCanvas);
				updatables.add(tilesetViewer);
			}
		});
	}
}