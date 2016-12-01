package model;

import geometry.Primitive;
import lighting.Renderable;
/**
 * Represents a 3D model that can be rendered by a {@link Camera}.
 * @author Donny
 *
 */
public interface IModel extends Renderable, Primitive{
	public IModel clone();
}
