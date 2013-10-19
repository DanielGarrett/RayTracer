package primitive.portal;

import primitive.Plane;
import primitive.PortalRing;
import primitive.Primitive;
import cartesian.Point3D;
import cartesian.Ray3D;
import cartesian.Vector3D;
import color.IntColor;

public class Portal implements Primitive {
	
	Plane plane;
	Point3D center;
	double radius;
	Portal link;
	PortalRing ring;
	Vector3D u;
	Vector3D v;
	Vector3D w;
	
	
	
	public Portal(Point3D center1, Vector3D normal1, Vector3D up1, double rad1, Point3D center2, 
			Vector3D normal2, Vector3D up2, double rad2, IntColor base)
	{
		this.plane = new Plane(center1, normal1, base, 0, 0);
		this.center = center1;
		this.radius = rad1;
		this.link = new Portal(center2, normal2, rad2, base, this);
		this.ring = new PortalRing(plane, radius, center, 0, 0, base);
	}
	
	private Portal(Point3D center1, Vector3D normal1, double rad1, IntColor base, Portal other)
	{
		this.plane = new Plane(center1, normal1, base, 0, 0);
		this.center = center1;
		this.radius = rad1;
		this.link = other;
		this.ring = new PortalRing(plane, radius, center, 0, 0, base);
	}
	
	public Portal getOther()
	{
		return link;
	}
	
	public PortalRing getRing()
	{
		return ring;
	}
	
	public Point3D pointToBasis()
	{
		return null;
	}
	
	public Point3D basisToPoint()
	{
		return null;
	}
	
	public Vector3D vectorToBasis()
	{
		return null;
	}
	
	public Vector3D basisToVector()
	{
		return null;
	}
	
	public Ray3D rayToBasis()
	{
		return null;
	}
	
	public Ray3D basisToRay()
	{
		return null;
	}

	@Override
	public Point3D findFirstIntersect(Ray3D ray) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector3D findNormalAt(Point3D point) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isOnSurface(Point3D point) 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Ray3D findReflectAt(Point3D point, Vector3D incident) {
		return Ray3D.nullVal;
	}

	@Override
	public Ray3D findRefractAt(Point3D point, Vector3D vector) {
		return null;
	}
}
