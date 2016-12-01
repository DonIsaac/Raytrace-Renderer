package geometry;

public class Ray {
	private Vector3 origin;
	private Vector3 dir;
	public Ray(Vector3 origin, Vector3 direction){
		this.origin=origin;
		this.dir=direction;
		this.dir.normalize();
	}
	public Ray(Vector3 direction){
		this.origin = Vector3.ZERO;
		this.dir=direction;
	}
	public static Ray createRayFromPoints(Vector3 u, Vector3 v){
		Vector3 dir = v.getSubtract(u);
		return new Ray(u.clone(),dir);
	}
	
	public Vector3 pointOnRay(double x){
		return origin.getAdd(dir.getScale(x));
	}
	public Vector3 getOrigin() {
		return origin;
	}
	public void setOrigin(Vector3 origin) {
		this.origin = origin;
	}
	public Vector3 getDir() {
		return dir;
	}
	public void setDir(Vector3 dir) {
		this.dir = dir;
		this.dir.normalize();
	}
	@Override
	public String toString(){
		return "<"+origin.x+","+origin.y+","+origin.z+">+ t<"+dir.x+","+dir.y+","+dir.z+">";
	}
}
