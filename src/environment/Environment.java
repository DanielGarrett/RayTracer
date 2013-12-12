package environment;

import java.util.ArrayList;


import primitive.ConstructiveSolid;
import primitive.Cube;
import primitive.Plane;
import primitive.Primitive;
import primitive.Shape;
import primitive.Sphere;
import primitive.ConstructiveSolid.SetOperation;
import primitive.TextureSphere;
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
	private final IntColor fogColor = new IntColor(128, 128, 128); 
	private final IntColor baseColor = new IntColor(0,0,0); 
	private final int MAX_REFLECT = 8;
	private Point3D lightSource;
	private ArrayList<Portal> portals;
	private boolean isPortal = false;
	private boolean hasFog;
	
	public Environment(boolean fog)
	{
		hasFog = fog;
		lightSource = new Point3D(-40, 90, 80);
		primatives = new ArrayList<Shape>();
		portals = new ArrayList<Portal>();
		Plane floor = new Plane(new Point3D(0,0,-.01), new Vector3D(0,0,1),
				new IntColor(150, 150, 150), .6, 0);
		Portal portal = new Portal(new Point3D(20,10,7), new Vector3D(-.25,1,.01), new Vector3D(0,0,1), 5, 
				new Point3D(0,-10,10), new Vector3D(0,1,0), new Vector3D(0,0,1), 7, new IntColor(0,255,0));
		TextureSphere trial = new TextureSphere(new Point3D(-10, -40, 10), 10, 0, 0, 
				new Vector3D(0, 0, 1), new Vector3D(1, 0, 0), "earthmap1k.jpg");
		Sphere trial2 = new Sphere(new Point3D(43, -40, 5), 6.5, new IntColor(255, 0, 0), .3, 0);
		Cube trial3 = new Cube(new Point3D(43, -40, 5), new Vector3D(0, 0, 1), new Vector3D(1, 1, 0), 
				new IntColor(0, 255, 0), 10, .3, 0);
		ConstructiveSolid cs = new ConstructiveSolid(trial3, trial2, SetOperation.Subtract);
		primatives.add(floor);
		primatives.add(cs);
		primatives.add(portal.getRing());
		primatives.add(portal.getOther().getRing());
		portals.add(portal);
		portals.add(portal.getOther());
		primatives.add(trial);
		camera = new Camera(new Point3D(0, 90, 11), new Vector3D(0, -1, 0), new Vector3D(0,0,1),
				51.75, 40, 2000, 1500);
		rays = camera.getRays();
		colors = new IntColor[rays.length][rays[0].length];
		for(int x = 0; x < colors.length; x++)
		{
			for(int y = 0; y < colors[x].length; y++)
			{
				if(y == 700 && x == 900)
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
			if(hasFog)
				return fogColor;
			else
				return baseColor;
		Point3D intersect = s.findFirstIntersect(ray);
		double dist = Vector3D.makeVector(intersect, ray.point).length();
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
			if(hasFog)
				combine = addFog(combine, dist);
		}
		else
		{
			combine = s.getBaseColor(intersect).takeFraction(1);
		}
		return combine.toIntColor();
	}
	
	private boolean isInShadow(Point3D point) {
		Ray3D lightRay = new Ray3D(Vector3D.makeVector(point, lightSource), point);
		Primitive s = getFirstIntersect(lightRay);
		if(s == null)
			return false;
		double dist = Vector3D.makeVector(s.findFirstIntersect(lightRay), point).length();
		return dist < lightRay.t.length() && !isPortal;
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
	
	private DoubleColor addFog(DoubleColor given, double dist)
	{
		double factor = Math.pow(.5, dist/100);
		given = given.takeFraction(factor);
		DoubleColor thisfog = fogColor.takeFraction(1-factor);
		return given.add(thisfog);
	}

}
