package render.strategies;

import geometry.Ray;
import geometry.Vector3;
import render.Camera;
import scene.Scene;
import tools.RaycastHit;

/**
 * Represents a way that a raycast result can be rendered. Strategies can be
 * used through the static method getStrategy().
 * 
 * @author Don Isaac
 *
 */
public abstract class RenderStrategy {
	private static RenderStrategy SINGLETON;

	public static RenderStrategy getStrategy() {
		if (SINGLETON == null)
			SINGLETON = new PhongStrategy();

		return SINGLETON;
	}

	public static RenderStrategy getStrategy(String strategy) {
		if (SINGLETON == null || !SINGLETON.name.equals(strategy))
			SINGLETON = resolveStrategyFromName(strategy);
		return SINGLETON;
	}

	private static RenderStrategy resolveStrategyFromName(String name) {
		switch (name.toLowerCase()) {
		case "phong":
			return new PhongStrategy();
		default:
			throw new IllegalArgumentException("Invalid strategy name.");
		}
	}

	/**
	 * Name of the strategy. Each strategy has a unique name, and thus the name acts
	 * as a unique identifier.
	 */
	protected String name;

	public String getName() {
		return this.name;
	}

	/**
	 * Processes a raycast result and determines the resulting color.
	 * 
	 * @param cam the camera taking the picture
	 * @param s   the scene the picture is of
	 * @param ray the primary ray that originates at the Camera's origin
	 * @param hit the result of the raycast
	 * 
	 * @return the resulting color. X, Y, and Z store the R, G, and B values
	 *         respectively. Each value will be from 0f to 255f inclusive.
	 *         Information is returned as a Vector3 instead of a Color for ease of
	 *         computation
	 */
	public abstract Vector3 render(Camera cam, Scene s, Ray ray, RaycastHit hit);

	@Override
	public String toString() {
		return "RenderStrategy#" + this.name;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (!(obj instanceof RenderStrategy))
			return false;

		RenderStrategy strat = (RenderStrategy) obj;

		if (this.name == null || strat.name == null) {
			throw new IllegalStateException("RenderStrategy's name was never defined.");
		}
		return this.name.equals(strat.name);
	}

	/**
	 * Inclusively clamps a number to a range. If the number is lower than the range
	 * minimum, the range minimum is returned. If the number is higher than the
	 * range maximum, the range maximum is returned. Otherwise, the original number
	 * is returned.
	 * 
	 * @param num the number to clamp
	 * @param min the range minimum
	 * @param max the range maximum
	 * @return the clamped number
	 */
	protected double clamp(double num, double min, double max) {
		if (num < min)
			num = min;
		else if (num > max)
			num = max;
		return num;
	}

	/**
	 * Finds the maximum of two numbers.
	 * 
	 * @param a the first number
	 * @param b the second number
	 * @return a or b, whichever is greater.
	 */
	protected double max(double a, double b) {
		return a > b ? a : b;
	}
	
}
