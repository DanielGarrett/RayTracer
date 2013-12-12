package primitive.portal;

import primitive.Plane;
import primitive.PortalRing;
import primitive.Primitive;
import cartesian.OrthogonalBasis;
import cartesian.Point3D;
import cartesian.Ray3D;
import cartesian.Vector3D;
import color.IntColor;

public class Portal implements Primitive {
	
	private Plane plane;
	private Point3D center;
	private double radius;
	private Portal link;
	private PortalRing ring;
	private OrthogonalBasis basis;
	private IntColor base;
	
	
	
	public Portal(Point3D center1, Vector3D normal1, Vector3D up1, double rad1, Point3D center2, 
			Vector3D normal2, Vector3D up2, double rad2, IntColor base)
	{
		this.plane = new Plane(center1, normal1, base, 0, 0);
		this.center = center1;
		this.radius = rad1;
		this.link = new Portal(center2, normal2, up2, rad2, base, this);
		this.ring = new PortalRing(plane, radius, center, 0, 0, base);
		this.basis = new OrthogonalBasis(normal1, up1, center1);
		this.base = base;
	}
	
	private Portal(Point3D center1, Vector3D normal1, Vector3D up1, double rad1, IntColor base, Portal other)
	{
		this.plane = new Plane(center1, normal1, base, 0, 0);
		this.center = center1;
		this.radius = rad1;
		this.link = other;
		this.ring = new PortalRing(plane, radius, center, 0, 0, base);
		this.basis = new OrthogonalBasis(normal1, up1, center1);
		this.base = base;
	}
	
	public Portal getOther()
	{
		return link;
	}
	
	public PortalRing getRing()
	{
		return ring;
	}
	
	public Point3D pointToBasis(Point3D point)
	{
		return basis.pointToBasis(point).multiply(1/radius);
	}
	
	public Point3D basisToPoint(Point3D point)
	{
		return basis.basisToPoint(point.multiply(radius));
	}
	
	public Vector3D vectorToBasis(Vector3D vector)
	{
		return basis.vectorToBasis(vector);
	}
	
	public Vector3D basisToVector(Vector3D vector)
	{
		return basis.basisToVector(vector);
	}
	
	public Ray3D rayToBasis(Ray3D ray)
	{
		return new Ray3D(vectorToBasis(ray.t), pointToBasis(ray.point));
	}
	
	public Ray3D basisToRay(Ray3D ray)
	{
		return new Ray3D(basisToVector(ray.t), basisToPoint(ray.point));
	}

	public Point3D findFirstIntersect(Ray3D ray) 
	{
		Point3D point = plane.findFirstIntersect(ray);
		if(isOnSurface(point))
			return point;
		return Point3D.nullVal;
	}

	public Vector3D findNormalAt(Point3D point) 
	{
		return plane.findNormalAt(point);
	}

	public boolean isOnSurface(Point3D point) 
	{
		return plane.isOnSurface(point) && Vector3D.makeVector(point, center).length() < radius + .001;
	}
	
	public Ray3D findReflectAt(Point3D point, Vector3D incident) {
		if(!isOnSurface(point))
			return Ray3D.nullVal;
		Point3D basisPoint = pointToBasis(point);
		Vector3D basisVector = vectorToBasis(incident);
		Ray3D basisRay = new Ray3D(basisVector, basisPoint);
		Ray3D finalRay = link.basisToRay(basisRay);
		return finalRay;
	}

	public Ray3D findRefractAt(Point3D point, Vector3D vector) {
		return Ray3D.nullVal;
		
	}

	public double getReflectivity() {
		return 1;
	}

	public double getTransmittivity() {
		return 0;
	}

	public IntColor getBaseColor(Point3D point) {
		return base;
	}
}
