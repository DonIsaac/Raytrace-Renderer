package scene;

import java.awt.Color;
import java.util.ArrayList;

import lighting.AmbientLight;
import lighting.Light;
import model.IModel;

/**
 * Stores information about a scene. The scene is then passed to a
 * {@link Camera} object be rendered.
 * 
 * @author Donny
 *
 */
public class Scene {
	/** Renderable objects in the Scene */
	public ArrayList<IModel> objects;
	/** Ambient light of the Scene */
	public AmbientLight ambient;
	/** Light sources in the Scene */
	public ArrayList<Light> lights;

	/**
	 * Constructs a new Scene with some objects in it. Has some ambient light,
	 * but no other light sources.
	 * 
	 * @param IModels
	 */
	public Scene(IModel... IModels) {
		objects = new ArrayList<IModel>(IModels.length);
		for (IModel i : IModels)
			objects.add(i);
		lights = new ArrayList<Light>();
		ambient = new AmbientLight(Color.white, .08);

	}
	/**
	 * Full constructor.
	 * @param objects Objects to be rendered
	 * @param lights Light sources
	 * @param ambient Ambient light source
	 */
	public Scene(ArrayList<IModel> objects, ArrayList<Light> lights, AmbientLight ambient) {
		this.objects = objects;
		this.lights = lights;
		this.ambient = ambient;

	}

}
