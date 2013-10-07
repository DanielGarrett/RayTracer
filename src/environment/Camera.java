package environment;

import cartesian.Point3D;
import cartesian.Ray3D;
import cartesian.Vector3D;

public class Camera {
	private Point3D base;
	private Vector3D w;
	private Vector3D v;
	private Vector3D u;
	private double angleWidth;
	private double angleHeight;
	private int pixelsWidth;
	private int pixelsHeight;
	
	public Camera(Point3D base, Vector3D direction, Vector3D orientation,
			double angleWidth, double angleHeight, int pixelsWidth,
			int pixelsHeight) {
		this.base = base;
		this.w = direction.unit().multiply(-1);
		this.u = orientation.cross(this.w).unit();
		this.v = w.cross(u);
		this.angleWidth = angleWidth;
		this.angleHeight = angleHeight;
		this.pixelsWidth = pixelsWidth;
		this.pixelsHeight = pixelsHeight;
	}
	
	public Ray3D[][] getRays()
	{
		double actualWidth = 2*Math.tan(Math.toRadians(angleWidth/2));
		double actualHeight = 2*Math.tan(Math.toRadians(angleHeight/2));
		double deltaWidth = actualWidth / pixelsWidth;
		double deltaHeight = actualHeight / pixelsHeight;
		
		Point3D topLeft = base.add(w.multiply(-1).add(v.multiply(actualHeight/2)).add(u.multiply(-actualWidth/2)));
		topLeft = topLeft.add(v.multiply(-deltaHeight/2)).add(u.multiply(deltaWidth/2));
		Ray3D[][] rays = new Ray3D[pixelsWidth][pixelsHeight];
		
		for(int i = 0; i < rays.length; i++)
		{
			for(int j = 0; j < rays[i].length; j++)
			{
				Point3D endpoint = topLeft.add(u.multiply(i*deltaWidth)).add(v.multiply(-j*deltaHeight));
				rays[i][j] = new Ray3D(Vector3D.makeVector(base, endpoint), base);
			}
		}
		
		return rays;
	}
	
	
	

}
