package color;

public class IntColor {
	
	public int red;
	public int blue;
	public int green;
	
	public IntColor(int r, int b, int g)
	{
		red = checkBounds(r);
		blue = checkBounds(b);
		green = checkBounds(g);
	}
	
	public int checkBounds(int c)
	{
		if(c>255)
			c=255;
		if(c<0)
			c=0;
		return c;
	}
	public DoubleColor takeFraction(double fract)
	{
		return new DoubleColor(red*fract, blue*fract, green*fract);
	}
}
