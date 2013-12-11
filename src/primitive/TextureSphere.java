package primitive;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import cartesian.Point3D;
import cartesian.Vector3D;
import color.IntColor;

public class TextureSphere extends Sphere {
	
	private Vector3D north;
	private Vector3D prime;
	private IntColor[][] map;
	private double max = 0/-1;
	private double min = 0/1;

	public TextureSphere(Point3D center, double radius, IntColor base,
			double reflectivity, double transmittivity, Vector3D up, Vector3D primeMer, String filename) {
		super(center, radius, base, reflectivity, transmittivity);
		this.north = up.unit();
		Vector3D temp = primeMer.cross(north).unit();
		this.prime = north.cross(temp);
		
		try {
			BufferedImage image = ImageIO.read(new File(filename));
			map = new IntColor[image.getWidth()][image.getHeight()];
			for(int x = 0; x < map.length; x++)
			{
				for(int y = 0; y < map[x].length; y++)
				{
					int num = image.getRGB(x, y);
					int blue = num % 255;
					num /= 256;
					int green = num % 255;
					num /= 256;
					int red = num % 255;
					map[x][y] = new IntColor(red, blue, green);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}
	
	public IntColor getBaseColor(Point3D point)
	{
		Vector3D radius = Vector3D.makeVector(center, point); // vector from center to point;
		double northsouthcostheta = radius.dot(north)/(radius.length()*north.length());
		double northsouthfactor = (Math.acos(northsouthcostheta))/(Math.PI);
		if(northsouthfactor > max)
			max = northsouthfactor;
		if(northsouthfactor < min)
			min = northsouthfactor;
		System.out.println(min + ", " + max);
		Vector3D temp = radius.cross(north).unit();
		Vector3D radiusOnEq = north.cross(temp);
		double eastwestcostheta = radiusOnEq.dot(prime)/(radiusOnEq.length()*prime.length());
		double eastwestfactor = (Math.acos(eastwestcostheta))/(Math.PI*2);
		return new IntColor(255-(int)(northsouthfactor*255), (int)(northsouthfactor*255), 0);
	}

}
