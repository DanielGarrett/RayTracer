package primitive;

import color.IntColor;
import cartesian.Point3D;
import cartesian.Ray3D;
import cartesian.Vector3D;

public class ConstructiveSolid extends Shape {
	
	public enum SetOperation {Union, Intersect, Subtract}
	private SetOperation opType;
	private Shape shape1;
	private Shape shape2;
	
	public ConstructiveSolid(Shape s1, Shape s2, SetOperation op)
	{
		opType = op;
		shape1 = s1;
		shape2 = s2;
	}

	@Override
	public Point3D findFirstIntersect(Ray3D ray) {
		if(ray.t == Vector3D.nullVal || ray.point == Point3D.nullVal)
			return Point3D.nullVal;
		Point3D intersect1 = shape1.findFirstIntersect(ray);
		Point3D intersect2 = shape2.findFirstIntersect(ray);
		if(intersect1 == Point3D.nullVal && intersect2 == Point3D.nullVal)
			return Point3D.nullVal;
		double distance1;
		double distance2;
		switch(opType)
		{
		case Union:
			distance1 = Vector3D.makeVector(ray.point, intersect1).length();
			distance2 = Vector3D.makeVector(ray.point, intersect2).length();
			if(distance1 < distance2)
				return intersect1;
			else
				return intersect2;
		case Intersect:
			while(!(intersect1 == Point3D.nullVal && intersect2 == Point3D.nullVal))
			{
				distance1 = Vector3D.makeVector(ray.point, intersect1).length();
				distance2 = Vector3D.makeVector(ray.point, intersect2).length();
				if(distance1 < distance2)
				{
					if(shape2.isInside(intersect1))
						return intersect1;
					ray = new Ray3D(ray.t, intersect1);
				}
				else
				{
					if(shape1.isInside(intersect2))
						return intersect2;
					ray = new Ray3D(ray.t, intersect2);
				}
				intersect1 = shape1.findFirstIntersect(ray);
				intersect2 = shape2.findFirstIntersect(ray);
			}
			return Point3D.nullVal;
		case Subtract:
			while(!(intersect1 == Point3D.nullVal && intersect2 == Point3D.nullVal))
			{
				distance1 = Vector3D.makeVector(ray.point, intersect1).length();
				distance2 = Vector3D.makeVector(ray.point, intersect2).length();
				if(distance1 < distance2)
				{
					if((!shape2.isInside(intersect1)) || shape2.isOnSurface(intersect1))
						return intersect1;
					ray = new Ray3D(ray.t, intersect1.add(ray.t.unit()));
				}
				else
				{
					if(shape1.isInside(intersect2))
						return intersect2;
					ray = new Ray3D(ray.t, intersect2.add(ray.t.unit()));
				}
				intersect1 = shape1.findFirstIntersect(ray);
				intersect2 = shape2.findFirstIntersect(ray);
			}
			return Point3D.nullVal;
		default:
			throw new IllegalArgumentException("Not an acceptable operation");
		}
	}

	public Vector3D findNormalAt(Point3D point) {
		if(!isOnSurface(point))
		{
			return Vector3D.nullVal;
		}
		if(shape1.isOnSurface(point) && shape2.isOnSurface(point))
			return shape1.findNormalAt(point).add(shape1.findNormalAt(point)).unit();
		else if(shape1.isOnSurface(point))
			return shape1.findNormalAt(point);
		else
			return shape2.findNormalAt(point);
	}

	public boolean isOnSurface(Point3D point) {
		boolean on1 = shape1.isOnSurface(point);
		boolean on2 = shape2.isOnSurface(point);
		boolean in1 = shape1.isInside(point);
		boolean in2 = shape2.isInside(point);
		switch(opType)
		{
		case Union:
			return (on1 && (!in2 || on2)) || (on2 && (!in1 || on1));
		case Intersect:
			return (on1 && in2) || (on2 && in1);
		case Subtract:
			return (on1 && (!in2 || on2)) || (on2 && in1);
		default:
			return false;
		}
	}

	public boolean isInside(Point3D point) {
		boolean in1 = shape1.isInside(point);
		boolean in2 = shape2.isInside(point);
		switch(opType)
		{
		case Union:
			return in1 || in2;
		case Intersect:
			return in1 && in2;
		case Subtract:
			return (in1 && (!in2 || shape2.isOnSurface(point)));
		default:
			return false;
		}
	}
	
	public IntColor getBaseColor(Point3D point)
	{
		if(shape1.isOnSurface(point))
			return shape1.getBaseColor(point);
		else
			return shape2.getBaseColor(point);
	}
	
	public double getReflectivity()
	{
			return (shape1.getReflectivity() + shape2.getReflectivity())/2;
	}

}
