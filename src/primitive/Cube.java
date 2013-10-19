package primitive;

import color.IntColor;
import cartesian.Point3D;
import cartesian.Ray3D;
import cartesian.Vector3D;

public class Cube extends Shape {
	Point3D center;
	double edgeLength;
	private Vector3D w;
	private Vector3D v;
	private Vector3D u;
	private Plane[] planes;
	
	
	public Cube(Point3D center, Vector3D normal, Vector3D orient, IntColor base, 
			double edgelength, double reflectivity,  double transmittivity)
	{
		this.center = center;
		this.w = normal.unit();
		this.u = orient.cross(w).unit();
		this.v = w.cross(u);
		this.baseColor = base;
		this.edgeLength = edgelength;
		this.reflectivity = reflectivity;
		this.transmittivity = transmittivity;
		this.planes = new Plane[6];
		planes[0] = new Plane(center.add(w.multiply(edgelength/2)), w, base, reflectivity, transmittivity);
		planes[1] = new Plane(center.add(w.multiply(-edgelength/2)), w.multiply(-1), base, reflectivity, transmittivity);
		planes[2] = new Plane(center.add(v.multiply(edgelength/2)), v, base, reflectivity, transmittivity);
		planes[3] = new Plane(center.add(v.multiply(-edgelength/2)), v.multiply(-1), base, reflectivity, transmittivity);
		planes[4] = new Plane(center.add(u.multiply(edgelength/2)), u, base, reflectivity, transmittivity);
		planes[5] = new Plane(center.add(u.multiply(-edgelength/2)), u.multiply(-1), base, reflectivity, transmittivity);
	}

	public Point3D findFirstIntersect(Ray3D ray) {
		Point3D closestPoint = Point3D.nullVal;
		double shortestDistance = Double.MAX_VALUE;
		for(int i = 0; i < planes.length; i++)
		{
			Point3D testPoint = planes[i].findFirstIntersect(ray);
			double testDistance = (Vector3D.makeVector(ray.point, testPoint)).length();
			if(testDistance < shortestDistance && isOnSurface(testPoint))
			{
				closestPoint = testPoint;
				shortestDistance = testDistance;
			}
		}
		return closestPoint;
	}

	//assumes that point is on surface
	public Vector3D findNormalAt(Point3D point) {
		for(int i = 0; i < planes.length; i++)
		{
			if(planes[i].isOnSurface(point))
				return planes[i].findNormalAt(point);
		}
		return Vector3D.nullVal;
	}

	public boolean isOnSurface(Point3D point) {
		boolean result = false;
		for(int i = 0; i < planes.length; i++)
		{
			if(planes[i].isOnSurface(point))
			{
				result = true;
			}
			else if(planes[i].isInFrontOfSurface(point))
			{
				return false;
			}
		}
		return result;
	}

}
