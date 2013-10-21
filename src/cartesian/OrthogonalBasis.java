package cartesian;

public class OrthogonalBasis {
	
	private Vector3D u;
	private Vector3D v;
	private Vector3D w;
	private Point3D origin;
	private Matrix toBasis;
	private Matrix fromBasis;
	
	public OrthogonalBasis(Vector3D primary, Vector3D orient, Point3D origin)
	{
		w = primary.multiply(1/primary.length());
		Vector3D bCrossW = orient.cross(w);
		u = bCrossW.multiply(1/bCrossW.length());
		v = w.cross(u);
		this.origin = origin;
		toBasis = Matrix.StandardBasisToOtherBasis(u, v, w, origin);
		fromBasis = Matrix.OtherBasisToStandardBasis(u, v, w, origin);
	}
	
	public Point3D pointToBasis(Point3D point)
	{
		return toBasis.Multiply(Matrix.PointToAffineMatrix(point)).toPoint();
	}
	
	public Point3D basisToPoint(Point3D point)
	{
		return fromBasis.Multiply(Matrix.PointToAffineMatrix(point)).toPoint();
	}
	
	public Vector3D vectorToBasis(Vector3D vector)
	{
		return toBasis.Multiply(Matrix.VectorToAffineMatrix(vector)).toVector();
	}
	
	public Vector3D basisToVector(Vector3D vector)
	{
		return fromBasis.Multiply(Matrix.VectorToAffineMatrix(vector)).toVector();
	}
	
	public Ray3D rayToBasis(Ray3D ray)
	{
		return new Ray3D(vectorToBasis(ray.t), pointToBasis(ray.point));
	}
	
	public Ray3D basisToRay(Ray3D ray)
	{
		return new Ray3D(basisToVector(ray.t), basisToPoint(ray.point));
	}

}
