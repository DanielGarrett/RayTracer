package cartesian;

public class Point3D {
	public double x;
	public double y;
	public double z;
	public static Point3D nullVal = new Point3D(1.0/0,1.0/0,1.0/0);
	
	public Point3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Point3D multiply(double factor)
	{
		return new Point3D(this.x*factor, this.y*factor, this.z*factor);
	}
	
	public Point3D add(Point3D p)
	{
		return new Vector3D(this.x + p.x, this.y + p.y, this.z + p.z);
	}
	
	public Point3D subtract(Point3D p)
	{
		return this.add(p.multiply(-1));
	}
	
	public String toString()
	{
		return "(" + x + ", " + y + ", " + z + ")\n";
	}
	
	
	

}
