package primitive;

import cartesian.Point3D;
import cartesian.Ray3D;
import cartesian.Vector3D;
import color.IntColor;

public class Plane extends Shape {
	private Point3D p;
	private double equationRight;
	private Vector3D normal;
	
	public Plane(Point3D p, Vector3D normal, IntColor base, double reflectivity,  double transmittivity)
	{
		this.p = p;
		this.normal = normal;
		this.baseColor = base;
		this.equationRight = getRight(p);
		this.reflectivity = reflectivity;
		this.transmittivity = transmittivity;
	}

	public Point3D findFirstIntersect(Ray3D ray) {
		// TODO Auto-generated method stub
		double t0Right = getRight(ray.point);
		double deltaRight = getRight(ray.t);
		double movementNeeded = equationRight-t0Right;
		double tNeeded = movementNeeded / deltaRight;
		if(tNeeded <= 0.001)
		{
			return Point3D.nullVal;
		}
		return ray.addVectorByFactor(tNeeded);
	}
	
	public double getRight(Point3D p)
	{
		return (p.x*normal.x)+(p.y*normal.y)+(p.z*normal.z);
	}

	//assumes that point is on surface
	public Vector3D findNormalAt(Point3D point) {
		return normal;
	}

	public boolean isOnSurface(Point3D point) {
		Vector3D vsurface = Vector3D.makeVector(p, point);
		double dot = vsurface.dot(normal);
		return Math.abs(dot) < .001;
	}
	
	public boolean isInFrontOfSurface(Point3D point)
	{
		Vector3D vsurface = Vector3D.makeVector(p, point);
		double dot = vsurface.dot(normal);
		return dot > -.001;
	}
	
	public boolean isInBehindSurface(Point3D point)
	{
		Vector3D vsurface = Vector3D.makeVector(p, point);
		double dot = vsurface.dot(normal);
		return dot < .001;
	}
	
	public boolean isInside(Point3D point)
	{
		return isOnSurface(point);
	}

	public IntColor getBaseColor(Point3D point) {
		return super.getBaseColor();
	}

}
