package smw.infinity;

public class Vector2D
{
	public float x, y;
	
	public Vector2D(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public float magnitude()
	{
		return (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
	
	public Vector2D normal()
	{
		float mag = magnitude();
		return new Vector2D(x / mag, y / mag);
	}
}