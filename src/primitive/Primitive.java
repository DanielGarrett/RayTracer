package primitive;

import cartesian.Point3D;
import cartesian.Ray3D;
import cartesian.Vector3D;

public interface Primitive {

	public Point3D findFirstIntersect(Ray3D ray);
	public Vector3D findNormalAt(Point3D point);
	public Ray3D findReflectAt(Point3D point, Vector3D incident);
	public Ray3D findRefractAt(Point3D point, Vector3D vector);
	public abstract boolean isOnSurface(Point3D point);

}
