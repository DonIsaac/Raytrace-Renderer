package lighting;

import geometry.Vector3;

import java.awt.Color;


public interface Light {
	public Color getColor();
	public Vector3 getPos();
	public double getInitialIntensity();
	public double getIntensityAt(Vector3 p);
	public double getIntensityAt(double distance);
}
