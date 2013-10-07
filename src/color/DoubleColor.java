package color;

public class DoubleColor {
	
	public double red;
	public double blue;
	public double green;
	
	public DoubleColor(double r, double b, double g)
	{
		red = checkBounds(r);
		blue = checkBounds(b);
		green = checkBounds(g);
	}
	
	private double checkBounds(double c)
	{
		if(c>255)
			c=255;
		if(c<0)
			c=0;
		return c;
	}
	
	public IntColor toIntColor()
	{
		return new IntColor(roundOff(red),roundOff(blue),roundOff(green));
	}
	
	private int roundOff(double c)
	{
		return (int)Math.round(c);
	}
	
	public DoubleColor add(DoubleColor d)
	{
		return new DoubleColor(red+d.red,blue+d.blue,green+d.green);
	}
	
	public DoubleColor inverse()
	{
		return new DoubleColor(255-red, 255-blue, 255-green);
	}
	public DoubleColor takeFraction(double fract)
	{
		return new DoubleColor(red*fract, blue*fract, green*fract);
	}

}
