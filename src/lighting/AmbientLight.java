package lighting;

import geometry.Vector3;

import java.awt.Color;

public class AmbientLight implements Light{
	public Color c;
	double i;
	public AmbientLight(Color c, double intensity){
		this.c=c;
		this.i=intensity;
	}
	public Color getColor() {
		return c;
	}
	public double getInitialIntensity() {
		// TODO Auto-generated method stub
		return i;
	}
	public double getIntensityAt(Vector3 p) {
		return i;
	}
	public double getIntensityAt(double distance) {
		return i;
	}
	public Vector3 getPos() {
		return Vector3.ZERO;
	}
}
