package geometry;

import tools.Epsilon;

public class Transform implements Transformable{
	private double[][] matrix;

	private Transform(double[][] matrix) {
		if (matrix.length != 4 || matrix[0].length != 4)
			throw new IllegalArgumentException(
					"Transformation matrix must be 4x4");
		this.matrix = matrix;
	}

	public static Transform getIdentityInstance() {
		double[][] matrix = { 
				{ 1.0, 0.0, 0.0, 0.0 }, 
				{ 0.0, 1.0, 0.0, 0.0 },
				{ 0.0, 0.0, 1.0, 0.0 }, 
				{ 0.0, 0.0, 0.0, 1.0 } 
				};
		return new Transform(matrix);
	}
	public static Transform getTransformInstance(Vector3 v){
		double[][] matrix = { 
				{ 1.0, 0.0, 0.0, v.x }, 
				{ 0.0, 1.0, 0.0, v.y },
				{ 0.0, 0.0, 1.0, v.z }, 
				{ 0.0, 0.0, 0.0, 1.0 } 
				};
		return new Transform(matrix);
	}
	public static Transform getRotationXInstance(double theta) {
		double sin = Math.sin(theta);
		double cos = Math.cos(theta);
		
		double[][] matrix = { 
				{ 1.0, 0.0, 0.0, 0.0 }, 
				{ 0.0, cos, -sin, 0.0 },
				{ 0.0, sin, cos, 0.0 }, 
				{ 0.0, 0.0, 0.0, 1.0 } 
				};
		return new Transform(matrix);
		
	}
	public static Transform getRotationYInstance(double theta) {
		double sin = Math.sin(theta);
		double cos = Math.cos(theta);
		
		double[][] matrix = { 
				{ cos, 0.0, sin, 0.0 }, 
				{ 0.0, 1.0, 0.0, 0.0 },
				{ -sin, 0.0, cos, 0.0 }, 
				{ 0.0, 0.0, 0.0, 1.0 } 
				};
		return new Transform(matrix);
	}
	public static Transform getRotationZInstance(double theta) {
		double sin = Math.sin(theta);
		double cos = Math.cos(theta);
		
		double[][] matrix = { 
				{ cos, -sin, 0.0, 0.0 }, 
				{ sin, cos, 0.0, 0.0 },
				{ 0.0, 0.0, 1.0, 0.0 }, 
				{ 0.0, 0.0, 0.0, 1.0 } 
				};
		return new Transform(matrix);
	}

	public void translate(Vector3 v){
		translate(v.x,v.y,v.z);
	}
	public void translate(double x, double y, double z){
		matrix[3][0]+=x;
		matrix[3][1]+=y;
		matrix[3][2]+=z;
	}
	public void rotateX(double theta, boolean aroundOrigin) {
		Vector3 t = Vector3.ZERO;
		if(aroundOrigin){
			t = new Vector3(matrix[3][0], matrix[3][1], matrix[3][2]);
			this.translate(-t.x,-t.y,-t.z);
		}
		this.matrix = multiply(Transform.getRotationXInstance(theta).getMatrix());
		
		translate(t);
	}
	public void rotateY(double theta, boolean aroundOrigin) {
		Vector3 t = Vector3.ZERO;
		if(aroundOrigin){
			t = new Vector3(matrix[3][0], matrix[3][1], matrix[3][2]);
			this.translate(-t.x,-t.y,-t.z);
		}
		this.matrix = multiply(Transform.getRotationYInstance(theta).getMatrix());
		
		translate(t);
	}
	public void rotateZ(double theta, boolean aroundOrigin) {
		Vector3 t = Vector3.ZERO;
		if(aroundOrigin){
			t = new Vector3(matrix[3][0], matrix[3][1], matrix[3][2]);
			this.translate(-t.x,-t.y,-t.z);
		}
		this.matrix = multiply(Transform.getRotationZInstance(theta).getMatrix());
		
		translate(t);	
	}

	public Vector3 getTransformed(Vector3 v){
		double[] vec = {v.x,v.y,v.z,1.0};
		double x = dot(matrix[0],vec);
		double y = dot(matrix[1],vec);
		double z = dot(matrix[2],vec);
		return new Vector3(x,y,z);
	}
	
	private double[][] multiply(double[][] m){
		double[][] mult = Transform.getIdentityInstance().getMatrix();
		for(int row = 0; row < 4; row++){
			for(int col = 0; col < 4; col++){
				double[] mCol = {m[0][col], m[1][col], m[2][col], m[3][col]};
				mult[row][col]= dot(matrix[row], mCol);
			}
		}
		return mult;
	}
	private double dot(double[] row, double[] col){
		double d=0.0;
		for(int i=0;i<row.length;i++){
			d += row[i]*col[i];
		}
		return d;
	}
	public double[][] getMatrix(){
		return this.matrix;
	}
	@Override
	public Transform clone(){
		double[][] mx = { 
				{ matrix[0][0], matrix[0][1], matrix[0][2], matrix[0][3] }, 
				{ matrix[1][0], matrix[1][1], matrix[1][2], matrix[1][3] },
				{ matrix[2][0], matrix[2][1], matrix[2][2], matrix[2][3] }, 
				{ matrix[3][0], matrix[3][1], matrix[3][2], matrix[3][3] } 
				};
		return new Transform(mx);
	}
}
