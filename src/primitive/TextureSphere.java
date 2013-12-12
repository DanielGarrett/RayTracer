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

	public TextureSphere(Point3D center, double radius, 
			double reflectivity, double transmittivity, Vector3D up, Vector3D primeMer, String filename) {
		super(center, radius, new IntColor(0,0,0), reflectivity, transmittivity);
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
					num = num << 8;
					num = num >>> 8;
					int blue = num % 256;
					num /= 256;
					int green = num % 256;
					num /= 256;
					int red = num % 256;
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
		Vector3D temp = radius.cross(north).unit();
		Vector3D radiusOnEq = north.cross(temp);
		double eastwestcostheta = radiusOnEq.dot(prime)/(radiusOnEq.length()*prime.length());
		double eastwestfactor = (Math.acos(eastwestcostheta))/(Math.PI);
		Vector3D eastern = north.cross(prime);
		double easterndot = eastern.dot(radius);
		if(easterndot > 0)
			eastwestfactor = (eastwestfactor+1)/2;
		else
			eastwestfactor = (-eastwestfactor+1)/2;
		return map[(int)(eastwestfactor*map.length)][(int)(northsouthfactor*map[0].length)];
	}

}
