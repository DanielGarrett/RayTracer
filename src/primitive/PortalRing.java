package primitive;

import color.IntColor;
import cartesian.Point3D;
import cartesian.Ray3D;
import cartesian.Vector3D;

public class PortalRing extends Shape {
	
	private Plane plane;
	private double radius;
	private Point3D center;
	
	public PortalRing(Plane p, double r, Point3D c, double reflect, double refract, IntColor base)
	{
		plane = p;
		radius = r;
		center = c;
		reflectivity = reflect;
		transmittivity = refract;
		baseColor = base;
	}

	@Override
	public Point3D findFirstIntersect(Ray3D ray) {
		// TODO Auto-generated method stub
		Point3D p = plane.findFirstIntersect(ray);
		if(isOnSurface(p))
			return p;
		else
			return Point3D.nullVal;
	}

	@Override
	public Vector3D findNormalAt(Point3D point) {
		// TODO Auto-generated method stub
		return plane.findNormalAt(point);
	}

	@Override
	public boolean isOnSurface(Point3D point) {
		if(!plane.isOnSurface(point))
			return false;
		double dist = Vector3D.makeVector(point, center).length();
		return dist > radius && dist < radius*1.05;
	}

}
