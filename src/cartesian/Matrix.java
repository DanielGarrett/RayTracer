package cartesian;

public class Matrix {
	
	private double[][] matrix;
	
	
	public Matrix(double[][] starter)
	{
		if(starter == null)
			throw new IllegalArgumentException("\"starter\" cannot be null");
		if(starter.length < 1 || starter[0].length < 1)
			throw new IllegalArgumentException("\"starter\" must be at least 1x1");
		matrix = starter;
	}
	
	public static Matrix StandardBasisToOtherBasis(Vector3D u, Vector3D v, Vector3D w, Point3D origin)
	{
		double[][] transform = new double[][]  {{u.x, u.y, u.z, -origin.x},
												{v.x, v.y, v.z, -origin.y},
												{w.x, w.y, w.z, -origin.z},
												{0, 0, 0, 1}};
		return new Matrix(transform);
	}
	
	public static Matrix OtherBasisToStandardBasis(Vector3D u, Vector3D v, Vector3D w, Point3D origin)
	{
		double[][] transform = new double[][]  {{u.x, v.x, w.x, origin.x},
												{u.y, v.y, w.y, origin.y},
												{u.z, v.z, w.z, origin.z},
												{0, 0, 0, 1}};
		return new Matrix(transform);
	}
	
	public static Matrix PointToMatrix(Point3D point)
	{
		return new Matrix(new double[][] {{point.x},{point.y},{point.z}});
	}
	
	public static Matrix PointToAffineMatrix(Point3D point)
	{
		return new Matrix(new double[][] {{point.x},{point.y},{point.z},{1}});
	}
	
	public static Matrix VectorToAffineMatrix(Point3D point)
	{
		return new Matrix(new double[][] {{point.x},{point.y},{point.z},{0}});
	}
	
	public Matrix Multiply(Matrix second)
	{
		if(this.matrix[0].length != second.matrix.length)
			throw new IllegalArgumentException("# columns in the first matrix must equal # rows in the "
												+ "second matrix");
		double[][] newMatrix = new double[this.matrix.length][second.matrix[0].length];
		for(int x = 0; x < newMatrix.length; x++)
			for(int y = 0; y < newMatrix[0].length; y++)
				newMatrix[0][0] = 0;
		
		for(int x = 0; x < this.matrix.length; x++)
		{
			for(int y = 0; y < second.matrix[0].length; y++)
			{
				for(int z = 0; z < second.matrix.length; z++)
				{
					newMatrix[x][y] += this.matrix[x][z] * second.matrix[z][y];
				}
			}
		}
		return new Matrix(newMatrix);
	}
	
	public Point3D toPoint()
	{
		if(matrix.length < 3)
			throw new IllegalArgumentException("cannot make point from this matrix!");
		return new Point3D(matrix[0][0], matrix[1][0], matrix[2][0]);
			
	}
	
	public Vector3D toVector()
	{
		if(matrix[0].length < 3)
			throw new IllegalArgumentException("cannot make point from this matrix!");
		return new Vector3D(matrix[0][0], matrix[0][1], matrix[0][2]);
			
	}

}
