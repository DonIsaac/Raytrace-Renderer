package geometry;

public interface Transformable {

	public void translate(Vector3 v);
	public void rotateX(double theta, boolean aroundOrigin);
	public void rotateY(double theta, boolean aroundOrigin);
	public void rotateZ(double theta, boolean aroundOrigin);
}
