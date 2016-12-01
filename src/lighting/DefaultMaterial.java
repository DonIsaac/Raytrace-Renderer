package lighting;

import java.awt.Color;

public class DefaultMaterial implements Material{
	private Color c;
	private double a,ks,kd;
	//TODO opacity/snell's law, reflectiveness (not phong)
	
	public DefaultMaterial(Color color, double alpha, double specularCoefficent, double diffuseCoefficent){
		this.c=color;
		this.a=alpha;
		this.ks=specularCoefficent;
		this.kd=diffuseCoefficent;
	}
	public Color getColor() {
		return c;
	}

	public double getAlpha() {
		return a;
	}
	public double Ks(){
		return ks;
	}
	public double Kd(){
		return kd;
	}
	@Override
	public DefaultMaterial clone(){
		return new DefaultMaterial(c,a,ks,kd);
	}

}
