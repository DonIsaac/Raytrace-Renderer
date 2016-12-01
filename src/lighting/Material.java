package lighting;

import java.awt.Color;
/**
 * Contains information used by the {@link Camera} to render {@link IModel}s.
 * @author Donny
 *
 */
public interface Material {
	/**
	 * 
	 * @return the color of this {@link Material}
	 */
	public Color getColor();
	/**
	 * Shininess constant.
	 */
	public double getAlpha();
	/**
	 * 
	 * @return Specular coefficient
	 */
	public double Ks();
	/**
	 * 
	 * @return Diffuse coefficient
	 */
	public double Kd();
	public Material clone();
}
