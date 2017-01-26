package geometry;

import tools.Epsilon;

public class Vector3 {
	public double x;
	public double y;
	public double z;

	public static final Vector3 ZERO = new Vector3(0.0, 0.0, 0.0);
	public static final Vector3 I = new Vector3(1.0, 0.0, 0.0);
	public static final Vector3 J = new Vector3(0.0, 1.0, 0.0);
	public static final Vector3 K = new Vector3(0.0, 0.0, 1.0);
	

	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double len() {

		return Math.sqrt(x * x + y * y + z * z);
	}

	/**
	 * Normalizes the vector.
	 */
	public Vector3 nor() {
		double len = len();
		try {
			this.x /= len;
			this.y /= len;
			this.z /= len;
		} catch (ArithmeticException e) {
			System.err.println("You can't normalize <0,0,0>");
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		return this;
	}
	
	public Vector3 getNormalized(){
		double len = len();
		return new Vector3(this.x/len,this.y/len,this.z/len);
	}

	/**
	 * Gets the distance from this vector to another vector
	 * 
	 * @param v
	 *            the vector being checked
	 * @return the distance between this vector and v
	 */
	public double distFrom(Vector3 v) {
		double dx = this.x - v.x;
		double dy = this.y - v.y;
		double dz = this.z - v.z;
		return Math.sqrt(dx * dx + dy + dy + dz * dz);
	}

	public double dot(Vector3 v) {
		return (x * v.x + y * v.y + z * v.z);
	}

	public Vector3 cross(Vector3 v) {
		double tmpx = y * v.z - z * v.y, 
				tmpy = z * v.x - x * v.z, 
				tmpz = x * v.y - y * v.x;
		return new Vector3(tmpx, tmpy, tmpz);
	}

	public void set(double nx, double ny, double nz) {
		x = nx;
		y = ny;
		z = nz;
	}

	public void set(Vector3 v) {
		x = v.x;
		y = v.y;
		z = v.z;
	}

	public Vector3 scl(double scalar) {
		x *= scalar;
		y *= scalar;
		z *= scalar;
		
		return this;
	}

	public Vector3 getScale(double scalar) {
		return new Vector3(x * scalar, y * scalar, z * scalar);
	}

	public Vector3 add(Vector3 v) {
		x += v.x;
		y += v.y;
		z += v.z;
		return this;
	}
	public Vector3 add(double x, double y, double z){
		this.x=x;
		this.y=y;
		this.z=z;
		return this;
	}

	public Vector3 getAdd(Vector3 v) {
		return new Vector3(x + v.x, y + v.y, z + v.z);
	}

	public Vector3 sub(Vector3 v) {
		x -= v.x;
		y -= v.y;
		z -= v.z;
		return this;
	}

	public Vector3 getSubtract(Vector3 v) {
		return new Vector3(x - v.x, y - v.y, z - v.z);
	}
	public double getSquared(){
		return x*x+y*y+z*z;
	}
	public boolean equals(Vector3 v){
		return Epsilon.nearlyEquals(x, v.x) &&
				Epsilon.nearlyEquals(y, v.y)&&
				Epsilon.nearlyEquals(z, v.z);
	}

	@Override
	public String toString() {
		return "<" + x + "," + y + "," + z + ">";
	}

	@Override
	public Vector3 clone() {
		return new Vector3(x, y, z);
	}
}
