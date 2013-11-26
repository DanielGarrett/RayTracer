package environment;

import java.util.ArrayList;


import primitive.Cube;
import primitive.Plane;
import primitive.Primitive;
import primitive.Shape;
import primitive.Sphere;
import primitive.portal.Portal;
import cartesian.Point3D;
import cartesian.Ray3D;
import cartesian.Vector3D;
import color.DoubleColor;
import color.IntColor;

public class Environment {
	
	private ArrayList<Shape> primatives;
	private Camera camera;
	private Ray3D[][] rays;
	private IntColor[][] colors;
	private final IntColor baseColor = new IntColor(0, 0, 0);
	private final int MAX_REFLECT = 8;
	private Point3D lightSource;
	private ArrayList<Portal> portals;
	private boolean isPortal = false;
	
	public Environment()
	{
		lightSource = new Point3D(40, 100, 50);
		primatives = new ArrayList<Shape>();
		portals = new ArrayList<Portal>();
		Plane floor = new Plane(new Point3D(0,0,0), new Vector3D(0,0,1),
				new IntColor(128, 128, 128), .6, 0);
		Cube obj = new Cube(new Point3D(0,0,10), new Vector3D(1,0,0), new Vector3D(0,1,0),
				new IntColor(255, 0, 0), 20, .2, 0);
		Cube obj2 = new Cube(new Point3D(13,3,2), new Vector3D(1,0,0), new Vector3D(0,1,0),
				new IntColor(0, 255, 0), 4, .2, 0);
		Sphere sphere = new Sphere(new Point3D(3, 15, 2), 2, new IntColor(0, 0, 255), .3, 0);
		Sphere sphere2 = new Sphere(new Point3D(-6, 15, 4), 4, new IntColor(0x54, 0x77, 0x38), .5, 0);
		Portal portal = new Portal(new Point3D(0,10.1,10), new Vector3D(0,1,0), new Vector3D(0,0,1), 7, 
				new Point3D(0,-10.1,10), new Vector3D(0,1,0), new Vector3D(0,0,-1), 7, new IntColor(0,255,0));
		primatives.add(floor);
		primatives.add(obj);
		primatives.add(obj2);
		primatives.add(sphere);
		primatives.add(sphere2);
		primatives.add(portal.getRing());
		primatives.add(portal.getOther().getRing());
		portals.add(portal);
		portals.add(portal.getOther());
		//primatives.add(sphere3);
		camera = new Camera(new Point3D(0, -60, 10), new Vector3D(0, 3, 0), new Vector3D(0,0,1),
				51.75, 40, 2000, 1500);
		rays = camera.getRays();
		colors = new IntColor[rays.length][rays[0].length];
		for(int x = 0; x < colors.length; x++)
		{
			for(int y = 0; y < colors[x].length; y++)
			{
				if(x==1000 && y == 750)
					System.out.println("Found it!"); // debug line, ignore
				colors[x][y] = getColor(rays[x][y], 0);
					
			}
		}
		
	}
	
	public IntColor[][] getColorMap()
	{
		return colors;
	}
	
	private IntColor getColor(Ray3D ray, int recurseNum)
	{
		Primitive s = getFirstIntersect(ray);
		boolean localIsPortal = isPortal;
		if(s==null)
			return baseColor;
		Point3D intersect = s.findFirstIntersect(ray);
		double fract = s.getReflectivity();
		DoubleColor combine;
		if(recurseNum < MAX_REFLECT)
		{
			DoubleColor base = s.getBaseColor(intersect).takeFraction(1-fract);
			Point3D reflectPoint = s.findFirstIntersect(ray);
			Ray3D reflectRay = s.findReflectAt(reflectPoint, ray.t);
			DoubleColor reflectColor = getColor(reflectRay, recurseNum+1).takeFraction(fract);
			combine = base.add(reflectColor);
			//shading
			Vector3D l = Vector3D.makeVector(reflectPoint, lightSource).unit();
			Vector3D v = ray.t.multiply(-1).unit();
			Vector3D h = v.add(l).unit();
			Vector3D n = s.findNormalAt(reflectPoint).unit();
			if (!localIsPortal) {
				if (!isInShadow(reflectPoint)) {
					//Lambertian
					double lambert = .3 * n.dot(l);
					if (lambert < 0)
						lambert = 0;
					combine = combine.add(combine.inverse().takeFraction(
							lambert));
					//blinn-phong
					double bp = Math.pow(h.dot(n), 25);
					if (bp < 0)
						bp = 0;
					combine = combine.add(combine.inverse().takeFraction(bp));
				} else {
					combine = combine.takeFraction(.5);
				}
			}
		}
		else
		{
			combine = s.getBaseColor(intersect).takeFraction(1);
		}
		return combine.toIntColor();
	}
	
	private boolean isInShadow(Point3D point) {
		Primitive s = getFirstIntersect(new Ray3D(Vector3D.makeVector(point, lightSource), point));
		return s != null;
	}

	private Primitive getFirstIntersect(Ray3D ray)
	{
		Shape best = null;
		double shortestShape = Double.MAX_VALUE;
		for (Shape s : primatives)
		{
			Point3D p = s.findFirstIntersect(ray);
			if(p == Point3D.nullVal)
				continue;
			double length = Vector3D.makeVector(ray.point, p).length();
			if(length < shortestShape)
			{
				shortestShape = length;
				best = s;
			}
		}
		Portal bestPortal = null;
		double shortestPortal = Double.MAX_VALUE;
		for (Portal s : portals)
		{
			Point3D p = s.findFirstIntersect(ray);
			if(p == Point3D.nullVal)
				continue;
			double length = Vector3D.makeVector(ray.point, p).length();
			if(length < shortestPortal)
			{
				shortestPortal = length;
				bestPortal = s;
			}
		}
		isPortal = shortestPortal < shortestShape;
		if(isPortal)
			return bestPortal;
		else
			return best;
	}

}
