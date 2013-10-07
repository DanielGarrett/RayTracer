package cartesian;

public class Ray3D {
	public Vector3D t;
	public Point3D point;
	public static Ray3D nullVal = new Ray3D(Vector3D.nullVal, Point3D.nullVal);
	
	public Ray3D(Vector3D t, Point3D point) {
		this.t = t;
		this.point = point;
	}
	
	public Point3D addVectorByFactor(double factor)
	{
		return point.add(t.multiply(factor));
	}

	public String toString()
	{
		return "Point: " + point.toString() +  "Vector: " + t.toString();
	}
	

}
