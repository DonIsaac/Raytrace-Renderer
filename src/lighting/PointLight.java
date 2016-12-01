package lighting;

import java.awt.Color;

import geometry.Vector3;

public class PointLight implements Light{
	Vector3 pos;
	Color c;
	double i;
	public PointLight(Vector3 pos, Color color, double intensity){
		this.pos = pos.clone();
		this.c=color;
		this.i=intensity;
	}
	public Color getColor() {
		return c;
	}
	public double getInitialIntensity() {
		return i;
	}
	public double getIntensityAt(Vector3 p) {
		double dist = p.distFrom(pos);
		return i / (dist*dist);
	}
	public double getIntensityAt(double distance) {
		return i / (distance*distance);
	}
	public Vector3 getPos(){
		return pos;
	}
	public void setPos(Vector3 newPos){
		this.pos=newPos.clone();
	}
	
	private double clamp(double num, double min, double max) {
		if (num < min)
			num = min;
		else if (num > max)
			num = max;
		return num;
	}
}
