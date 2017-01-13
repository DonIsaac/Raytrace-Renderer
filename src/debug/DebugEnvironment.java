package debug;

import geometry.Intersection;
import geometry.Triangle;
import geometry.Vector3;
import render.Camera;

public class DebugEnvironment {
	// 850, 825
	public DebugEnvironment() {
		Vector3 v2 = new Vector3(-1.000000, 1.000000, 1.000000);
		Vector3 v3 = new Vector3(-1.000000, -1.000000, 1.000000);
		Vector3 v1 = new Vector3(1.000000, 1.000000, 1.000000);
		Triangle t = new Triangle(v1,
				v2, v3 );
		Intersection i = t.intersects(Camera.brokenRay);
		System.out.println(i.isHit);
	}
}
