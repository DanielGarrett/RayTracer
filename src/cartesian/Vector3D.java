package cartesian;

public class Vector3D extends Point3D{
	
	public static Vector3D nullVal = new Vector3D(1.0/0,1.0/0,1.0/0);
	
	public Vector3D(double x, double y, double z) {
		super(x, y, z);
	}
	
	public static Vector3D makeVector(Point3D start, Point3D end)
	{
		if(start == Point3D.nullVal || end == Point3D.nullVal)
			return nullVal;
		return new Vector3D(end.x-start.x, end.y-start.y, end.z-start.z);
	}
	
	public double dot(Vector3D v)
	{
		return (this.x * v.x) + (this.y * v.y) + (this.z * v.z);
	}
	
	public Vector3D add(Point3D p)
	{
		return new Vector3D(this.x + p.x, this.y + p.y, this.z + p.z);
	}
	
	public Vector3D subtract(Point3D p)
	{
		return this.add(p.multiply(-1));
	}
	
	public Vector3D multiply(double factor)
	{
		return new Vector3D(this.x*factor, this.y*factor, this.z*factor);
	}
	
	public Vector3D cross(Vector3D v)
	{
		return new Vector3D(this.y*v.z - this.z*v.y,
							this.z*v.x - this.x*v.z,
							this.x*v.y - this.y*v.x);
	}
	
	public Vector3D unit()
	{
		double length = Math.sqrt((x*x)+(y*y)+(z*z));
		return multiply(1/length);
	}
	
	public double length()
	{
		return Math.sqrt((x*x)+(y*y)+(z*z));
	}
	
	public String toString()
	{
		return "<" + x + ", " + y + ", " + z + ">\n";
	}

}
