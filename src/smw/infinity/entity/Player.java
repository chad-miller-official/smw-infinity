package smw.infinity.entity;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import smw.infinity.Animation;
import smw.infinity.Direction;
import smw.infinity.Interactive;
import smw.infinity.SMWException;
import smw.infinity.Utility;
import smw.infinity.map.Map;
import smw.infinity.map.MapScene;

public class Player extends Entity implements KeyListener
{
	public static final float ACCEL = 2f;
	public static final float MAX_VELOCITY = 10f;
	public static final float GRAVITY = 9.8f;
	public static final float MAX_INITIAL_JUMP_VELOCITY = 10f;
	public static final float TERMINAL_VELOCITY = -15f;
	
	private static final long serialVersionUID = -5137385340745916476L;
	private static final byte UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3, TURBO = 4, USE = 5,
			STAND_RIGHT = 0, WALK_RIGHT = 1, JUMP_RIGHT = 2, TURN_RIGHT = 3, DIE = 4,
			FLAT = 5, STAND_LEFT = 6, WALK_LEFT = 7, JUMP_LEFT = 8, TURN_LEFT = 9;
	
	private boolean[] keys;
	private int keyCodes[];
	
	private BufferedImage[] sprites;
	private Animation walkRight, walkLeft;
	
	private boolean facingLeft, onGround, canMoveLeft, canMoveRight, canMoveUp;

	public Player(Point2D.Float coord, String spriteFilename, int[] keyCodes)
	{
		super(coord, spriteFilename);
		facingLeft = onGround = canMoveLeft = canMoveRight = canMoveUp = false;
		keys = new boolean[]{ false, false, false, false, false, false };
		this.keyCodes = keyCodes;
	}
	
	@Override
	protected void setupSprites(String spriteFilename)
	{
		try
		{
			BufferedImage spriteSheet = ImageIO.read(new File("res/gfx/skins/" + spriteFilename + ".png"));
			sprites = new BufferedImage[10];
			
			for(int i = 0; i < 6; i++)
				sprites[i] = spriteSheet.getSubimage(i * 32, 0, 32, 32);
			
			for(int i = 6; i < 10; i++)
				sprites[i] = Utility.getHorizontalFlippedCopy(sprites[i - 6]);
			
			walkRight = new Animation(250, sprites[STAND_RIGHT], sprites[WALK_RIGHT]);
			walkLeft = new Animation(250, sprites[STAND_LEFT], sprites[WALK_LEFT]);
		}
		catch (IOException | SMWException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void react(Interactive other, Direction dir)
	{
		//TODO write
	}
	
	@Override
	protected void updateMovement(long timePassed)
	{
		super.updateMovement(timePassed);
		
		if((keys[LEFT] && !keys[RIGHT]) || (!keys[LEFT] && keys[RIGHT]))
		{
			if(keys[LEFT])
				facingLeft = true;
			else if(keys[RIGHT])
				facingLeft = false;
			
			if((facingLeft && canMoveLeft) || (!facingLeft && canMoveRight))
			{
				if(speed.x < MAX_VELOCITY)
					speed.x += ACCEL;
				else
					speed.x = MAX_VELOCITY;
			}
			else
				speed.x = 0f;
			
		}
		else
		{
			if(speed.x > 0f)
				speed.x -= ACCEL;
			else
				speed.x = 0f;
		}
		
		if(keys[UP] && onGround)
		{
			speed.y = MAX_INITIAL_JUMP_VELOCITY;
			onGround = false;
		}
		
		if(!onGround)
		{
			if(speed.y > TERMINAL_VELOCITY)
				speed.y -= GRAVITY;
			else
				speed.y = TERMINAL_VELOCITY;
		}
		
		coord.x += facingLeft ? speed.x : -speed.x;
		
		if((canMoveUp && speed.y < 0) || speed.y > 0)
			coord.y -= speed.y;
	}

	@Override
	protected void updateImage(long timePassed)
	{
		if(speed.y != 0)
		{
			currentSprite = sprites[facingLeft ? JUMP_LEFT : JUMP_RIGHT];
			walkLeft.reset();
			walkRight.reset();
		}
		else if(speed.x > 0)
		{
			if(facingLeft)
			{
				if(keys[RIGHT])
					currentSprite = sprites[TURN_LEFT];
				else
				{
					walkLeft.update(timePassed);
					currentSprite = walkLeft.getImage();
				}
			}
			else
			{
				if(keys[LEFT])
					currentSprite = sprites[TURN_RIGHT];
				else
				{
					walkRight.update(timePassed);
					currentSprite = walkRight.getImage();
				}
			}
		}
		else
		{
			currentSprite = sprites[facingLeft ? STAND_LEFT : STAND_RIGHT];
			walkLeft.reset();
			walkRight.reset();
		}
	}
	
	@Override
	protected void resolveCollision()
	{
		Map m = MapScene.map;
		onGround = canMoveLeft = canMoveRight = canMoveUp = false;
		
		byte rX = (byte) (coord.x / 32), rXA = (byte) (rX + 1);
		byte rY = (byte) (coord.y / 32), rYA = (byte) (rY + 1);
		
		Rectangle[][] checkTiles = new Rectangle[2][2];
		
		checkTiles[0][0] = (m.tileTypeAt(rX, rY) != null) ? new Rectangle(rX, rY, 32, 32) : null;
		checkTiles[0][1] = (m.tileTypeAt(rXA, rY) != null) ? new Rectangle(rXA, rY, 32, 32) : null;
		checkTiles[1][0] = (m.tileTypeAt(rX, rYA) != null) ? new Rectangle(rX, rYA, 32, 32) : null;
		checkTiles[1][1] = (m.tileTypeAt(rXA, rYA) != null) ? new Rectangle(rXA, rYA, 32, 32) : null;
		
		for(int i = 0; i < 2; i++)
			if(intersects(checkTiles[1][i]))
			{
				onGround = true;
				break;
			}
		
		if(facingLeft)
			canMoveLeft = !intersects(checkTiles[0][1]);
		else
			canMoveRight = !intersects(checkTiles[0][0]);
		
		for(int i = 0; i < 2; i++)
			if(intersects(checkTiles[0][i]))
			{
				canMoveUp = false;
				break;
			}
	}

	@Override
	public int hashCode()
	{
		//TODO auto-generate
		return 0;
	}

	@Override
	public boolean equals(Object o)
	{
		//TODO auto-generate
		return false;
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		e.consume();
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		int code = e.getKeyCode();
		e.consume();
		
		if(code == keyCodes[UP])
			keys[UP] = true;
		
		if(code == keyCodes[DOWN])
			keys[DOWN] = true;
		
		if(code == keyCodes[LEFT])
			keys[LEFT] = true;
		
		if(code == keyCodes[RIGHT])
			keys[RIGHT] = true;
		
		if(code == keyCodes[TURBO])
			keys[TURBO] = true;
		
		if(code == keyCodes[USE])
			keys[USE] = true;
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		int code = e.getKeyCode();
		e.consume();
		
		if(code == keyCodes[UP])
			keys[UP] = false;
		
		if(code == keyCodes[DOWN])
			keys[DOWN] = false;
		
		if(code == keyCodes[LEFT])
			keys[LEFT] = false;
		
		if(code == keyCodes[RIGHT])
			keys[RIGHT] = false;
		
		if(code == keyCodes[TURBO])
			keys[TURBO] = false;
		
		if(code == keyCodes[USE])
			keys[USE] = false;
	}
}
