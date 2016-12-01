package scene;

import java.awt.Color;
import java.util.ArrayList;

import lighting.AmbientLight;
import lighting.Light;
import model.IModel;

public class Scene {
	public ArrayList<IModel> objects;
	public AmbientLight ambient;
	public ArrayList<Light> lights;

	public Scene(IModel... IModels) {
		objects = new ArrayList<IModel>(IModels.length);
		for (IModel i : IModels)
			objects.add(i);
		lights = new ArrayList<Light>();
		ambient = new AmbientLight(Color.white, .08);

	}

	public Scene(ArrayList<IModel> objects, ArrayList<Light> lights, AmbientLight ambient) {
		this.objects = objects;
		this.lights = lights;
		this.ambient = ambient;

	}

}
