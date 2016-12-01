package lighting;
/**
 * Represents anything that can be rendered by a {@link Camera}.
 * @author Donny
 *
 */
public interface Renderable{
	/**
	 * 
	 * @return the Renderable  object's {@link Material}.
	 */
	public Material getMaterial();
	/**
	 * Sets the {@link Material} used by the Renderable object.
	 * @param material the new {@link Material} to use
	 */
	public void setMaterial(Material material);
}
