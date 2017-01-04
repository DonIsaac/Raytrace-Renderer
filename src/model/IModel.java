package model;

import geometry.Intersection;
import geometry.Ray;
import geometry.Vector3;
import lighting.Renderable;
import render.Camera;
/**
 * Represents a 3D model that can be rendered by a {@link Camera}.
 * @author Donny
 *
 */
public interface IModel extends Renderable{
	public Intersection intersects(Ray r);
	public IModel clone();
}
