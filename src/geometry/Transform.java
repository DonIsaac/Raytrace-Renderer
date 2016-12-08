package geometry;

/**
 * Represents a 4x4 transformation matrix. Transformation matrices are usually
 * used for applying geometric transformations onto objects.
 * 
 * @author Donny
 *
 */
/*
 * NOTE: I know that there are more efficent methods for computing the rotations
 * than creating a whole new matrix and multiplying by that, but it works and
 * optimizing that kind of thing takes a lot of nitty-gritty coding that I don't
 * want to do.
 */
public class Transform implements Transformable {
	/**
	 * The transformation matrix.
	 */
	private double[][] matrix;

	/**
	 * Full constructor. Users wishing to create a new Transform object should
	 * use the provided facory methods.
	 * 
	 * @param matrix
	 *            The transformation matrix
	 */
	private Transform(double[][] matrix) {
		if (matrix.length != 4 || matrix[0].length != 4)
			throw new IllegalArgumentException("Transformation matrix must be 4x4");
		this.matrix = matrix;
	}

	/**
	 * Creates an instance of an identity matrix.
	 * 
	 * @return The resulting transformation matrix.
	 */
	public static Transform getIdentityInstance() {
		double[][] matrix = { { 1.0, 0.0, 0.0, 0.0 }, { 0.0, 1.0, 0.0, 0.0 }, { 0.0, 0.0, 1.0, 0.0 },
				{ 0.0, 0.0, 0.0, 1.0 } };
		return new Transform(matrix);
	}

	/**
	 * Creates a new transformation matrix translated by a specified
	 * {@link Vector3}.
	 * 
	 * @param v
	 *            The amount to translate the matrix along the x,y,and z axis.
	 * @return The resulting transformation matrix.
	 */
	public static Transform getTranslationInstance(Vector3 v) {
		double[][] matrix = { { 1.0, 0.0, 0.0, v.x }, { 0.0, 1.0, 0.0, v.y }, { 0.0, 0.0, 1.0, v.z },
				{ 0.0, 0.0, 0.0, 1.0 } };
		return new Transform(matrix);
	}

	/**
	 * Creates a new transformation matrix rotated around the X-axis.
	 * 
	 * @param theta
	 *            The angle to rotate by in radians.
	 * @return The resulting transformation matrix.
	 */
	public static Transform getRotationXInstance(double theta) {
		double sin = Math.sin(theta);
		double cos = Math.cos(theta);

		double[][] matrix = { { 1.0, 0.0, 0.0, 0.0 }, { 0.0, cos, -sin, 0.0 }, { 0.0, sin, cos, 0.0 },
				{ 0.0, 0.0, 0.0, 1.0 } };
		return new Transform(matrix);

	}

	/**
	 * Creates a new transformation matrix rotated around the Y-axis.
	 * 
	 * @param theta
	 *            The angle to rotate by in radians.
	 * @return The resulting transformation matrix.
	 */
	public static Transform getRotationYInstance(double theta) {
		double sin = Math.sin(theta);
		double cos = Math.cos(theta);

		double[][] matrix = { { cos, 0.0, sin, 0.0 }, { 0.0, 1.0, 0.0, 0.0 }, { -sin, 0.0, cos, 0.0 },
				{ 0.0, 0.0, 0.0, 1.0 } };
		return new Transform(matrix);
	}

	/**
	 * Creates a new transformation matrix rotated around the Z-axis.
	 * 
	 * @param theta
	 *            The angle to rotate by in radians.
	 * @return The resulting transformation matrix.
	 */
	public static Transform getRotationZInstance(double theta) {
		double sin = Math.sin(theta);
		double cos = Math.cos(theta);

		double[][] matrix = { { cos, -sin, 0.0, 0.0 }, { sin, cos, 0.0, 0.0 }, { 0.0, 0.0, 1.0, 0.0 },
				{ 0.0, 0.0, 0.0, 1.0 } };
		return new Transform(matrix);
	}

	/**
	 * Translates the transformation matrix along the X, Y, and Z axis.
	 * 
	 * @param v
	 *            The amount to translate by
	 */
	public void translate(Vector3 v) {
		translate(v.x, v.y, v.z);
	}

	/**
	 * Translates the transformation matrix along the X, Y, and Z axis.
	 * 
	 * @param x
	 *            The translation along the X-axis
	 * @param y
	 *            The translation along the Y-axis
	 * @param z
	 *            The translation along the Z-axis
	 */
	public void translate(double x, double y, double z) {
		matrix[3][0] += x;
		matrix[3][1] += y;
		matrix[3][2] += z;
	}

	/**
	 * Rotates the transformation matrix around the X-axis. Because the rotation
	 * occurs around the axis, if the matrix is not centered on the axis, it
	 * will rotate in a circular path around it, not on it's local axis. To make
	 * the rotation occur around the local axis, set <b>aroundOrigin</b> to
	 * true.
	 * 
	 * @param theta
	 *            The angle to rotate by in radians
	 * @param aroundOrigin
	 *            Whether or not the rotation should occur around the local axis
	 */
	public void rotateX(double theta, boolean aroundOrigin) {
		Vector3 t = Vector3.ZERO;
		if (aroundOrigin) {
			t = new Vector3(matrix[3][0], matrix[3][1], matrix[3][2]);
			this.translate(-t.x, -t.y, -t.z);
		}
		this.matrix = multiply(Transform.getRotationXInstance(theta).getMatrix());

		translate(t);
	}

	/**
	 * Rotates the transformation matrix around the Y-axis. Because the rotation
	 * occurs around the axis, if the matrix is not centered on the axis, it
	 * will rotate in a circular path around it, not on it's local axis. To make
	 * the rotation occur around the local axis, set <b>aroundOrigin</b> to
	 * true.
	 * 
	 * @param theta
	 *            The angle to rotate by in radians
	 * @param aroundOrigin
	 *            Whether or not the rotation should occur around the local axis
	 */
	public void rotateY(double theta, boolean aroundOrigin) {
		Vector3 t = Vector3.ZERO;
		if (aroundOrigin) {
			t = new Vector3(matrix[3][0], matrix[3][1], matrix[3][2]);
			this.translate(-t.x, -t.y, -t.z);
		}
		this.matrix = multiply(Transform.getRotationYInstance(theta).getMatrix());

		translate(t);
	}

	/**
	 * Rotates the transformation matrix around the Z-axis. Because the rotation
	 * occurs around the axis, if the matrix is not centered on the axis, it
	 * will rotate in a circular path around it, not on it's local axis. To make
	 * the rotation occur around the local axis, set <b>aroundOrigin</b> to
	 * true.
	 * 
	 * @param theta
	 *            The angle to rotate by in radians
	 * @param aroundOrigin
	 *            Whether or not the rotation should occur around the local axis
	 */
	public void rotateZ(double theta, boolean aroundOrigin) {
		Vector3 t = Vector3.ZERO;
		if (aroundOrigin) {
			t = new Vector3(matrix[3][0], matrix[3][1], matrix[3][2]);
			this.translate(-t.x, -t.y, -t.z);
		}
		this.matrix = multiply(Transform.getRotationZInstance(theta).getMatrix());

		translate(t);
	}

	/**
	 * Transforms a {@link Vector3} using this matrix using matrix
	 * multiplication. This method does not affect the {@link Vector3} passed,
	 * as it creates a new {@link Vector3} to transform using the passed
	 * {@link Vector3}.
	 * 
	 * @param v
	 *            The {@link Vector3} to transform.
	 * @return The transformed vector.
	 */
	public Vector3 getTransformed(Vector3 v) {
		double[] vec = { v.x, v.y, v.z, 1.0 };
		double x = dot(matrix[0], vec);
		double y = dot(matrix[1], vec);
		double z = dot(matrix[2], vec);
		return new Vector3(x, y, z);
	}

	/**
	 * Multiplies two transformation matrices and stores the value in the
	 * left-hand matrix.<br/><br/> i.e:</br> <code>A :=AxB</code>
	 * 
	 * @param m
	 * @return
	 */
	private double[][] multiply(double[][] m) {
		double[][] mult = Transform.getIdentityInstance().getMatrix();
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				double[] mCol = { m[0][col], m[1][col], m[2][col], m[3][col] };
				mult[row][col] = dot(matrix[row], mCol);
			}
		}
		return mult;
	}
	/**
	 * Dot product between two 4d vectors. Used in matrix multiplication.
	 * @param row The first vector to dot.
	 * @param col The second vector to dot.
	 * @return the product
	 */
	private double dot(double[] row, double[] col) {
		double d = 0.0;
		for (int i = 0; i < row.length; i++) {
			d += row[i] * col[i];
		}
		return d;
	}
	/**
	 * @return The matrix array used by the transformation matrix
	 */
	public double[][] getMatrix() {
		return this.matrix;
	}

	@Override
	public Transform clone() {
		double[][] mx = { { matrix[0][0], matrix[0][1], matrix[0][2], matrix[0][3] },
				{ matrix[1][0], matrix[1][1], matrix[1][2], matrix[1][3] },
				{ matrix[2][0], matrix[2][1], matrix[2][2], matrix[2][3] },
				{ matrix[3][0], matrix[3][1], matrix[3][2], matrix[3][3] } };
		return new Transform(mx);
	}
}
