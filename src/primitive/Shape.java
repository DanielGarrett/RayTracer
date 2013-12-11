package primitive;

import cartesian.*;
import color.*;



public abstract class Shape implements Primitive{
	public double reflectivity;
	public double transmittivity;
	public IntColor baseColor;
	public abstract Point3D findFirstIntersect(Ray3D ray);
	public abstract Vector3D findNormalAt(Point3D point);

	public Ray3D findReflectAt(Point3D point, Vector3D incident) {
		Vector3D normal = findNormalAt(point);
		double dot = incident.dot(normal);
		double factor = -2 * dot;
		Vector3D normalMult = normal.multiply(factor);
		Vector3D finalVector = normalMult.add(incident);
		Ray3D finalRay = new Ray3D(finalVector, point);
		return finalRay;
	}
	
	public Ray3D findRefractAt(Point3D point, Vector3D vector)
	{
		return Ray3D.nullVal;
	}
	public abstract boolean isOnSurface(Point3D point);
	
	public IntColor getBaseColor(Point3D point)
	{
		return baseColor;
	}
	
	public double getTransmittivity()
	{
		return transmittivity;
	}
	
	public double getReflectivity()
	{
		return reflectivity;
	}
	
	public abstract boolean isInside(Point3D point);
}
