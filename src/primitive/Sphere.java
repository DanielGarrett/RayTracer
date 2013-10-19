package primitive;

import color.IntColor;
import cartesian.Point3D;
import cartesian.Ray3D;
import cartesian.Vector3D;

public class Sphere extends Shape {
	private Point3D center;
	private double radius;
	
	public Sphere(Point3D center, double radius, IntColor base, double reflectivity,  double transmittivity)
	{
		this.center = center;
		this.radius = radius;
		this.baseColor = base;
		this.reflectivity = reflectivity;
		this.transmittivity = transmittivity;
	}

	public Point3D findFirstIntersect(Ray3D ray) {
		//we will use the quadratic formula to come up with a value for t
		//t = (-B(+|-)sqrt(B^2-4AC))/(2A)
		//dist = ray.point-center
		Vector3D dist = Vector3D.makeVector(center, ray.point);
		//a = ray.t*ray.t
		double a = ray.t.dot(ray.t);
		//b = 2*(ray.t*(ray.point-center))
		double b = 2 * (ray.t.dot(dist));
		//c = (ray.point-center)*(ray.point-center)-radius*radius
		double c = dist.dot(dist) - (radius*radius);
		//discr = B^2-4AC
		double discr = (b*b) - (4*a*c);
		if(discr < 0)
			return Point3D.nullVal;
		double t = (-b-Math.sqrt(discr))/(2*a);//since sqrt(anything) >= 0, we assume the 
											   //minus sqrt gives us the lowest value
		if(t < .001)
			return Point3D.nullVal;
		Point3D point = ray.addVectorByFactor(t);
		return point;
	}

	//assumes that point is on surface
	public Vector3D findNormalAt(Point3D point) {
		return Vector3D.makeVector(center, point);
	}

	public boolean isOnSurface(Point3D point) {
		Vector3D distance = findNormalAt(point);
		return Math.abs(distance.length()-radius) < .001;
	}

}
