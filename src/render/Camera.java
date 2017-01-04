package render;

import java.awt.Color;
import java.awt.image.BufferedImage;

import geometry.Intersection;
import geometry.Ray;
import geometry.Transform;
import geometry.Transformable;
import geometry.Vector3;
import lighting.Light;
import lighting.Material;
import model.IModel;
import scene.Scene;
import tools.RaycastHit;

/**
 * Represents the camera used to take picture of a {@link Scene}. A Camera is
 * not part of a {@link Scene}, but is instead it's own entity. This means that
 * it can take pictures of different {@link Scene}s. The resolution of the
 * Camera is also not static, but is instead passed when a the user wants to
 * take a picture of a {@link Scene}.
 * 
 * @author Donny
 *
 */
public class Camera implements Transformable {

	Vector3 pos;
	Transform camToWorld;
	double focalLength;
	boolean isOrthographic;

	public Camera(Vector3 pos, Transform cameraToWorld, double focalLength, boolean isOrthographic) {
		this.pos = pos;
		this.camToWorld = cameraToWorld;
		this.focalLength = focalLength;
		this.isOrthographic = isOrthographic;
	}

	/**
	 * Takes a picture of a {@link Scene}.
	 * 
	 * @param s
	 *            The {@link Scene} to take a picture of
	 * @param data
	 *            The resolution information to use
	 * @return A picture of the {@link Scene}
	 */
	public BufferedImage takePicture(Scene s, ImageData data) {
		BufferedImage pic = new BufferedImage(data.getWidth(), data.getHeight(), data.getType());
		for (int y = 0; y < data.getHeight(); y++) {
			for (int x = 0; x < data.getWidth(); x++) {
				pic.setRGB(x, y, raytrace(x, y, s, data).getRGB());
				// This line of code here prints out the rendering progress, but
				// slows down the rendering process drastically (System calls
				// are expensive).
				// System.out.println(100.0*(double)(y*width+x)/(double)(width*height)+"%");
			}
		}
		return pic;
	}

	/**
	 * Gets the color of a specific pixel from a {@link Scene}. Using this
	 * method across an entire image results in a fully rendered image.
	 * 
	 * @param x
	 *            The pixel's x location on the image
	 * @param y
	 *            The pixel's y location on the image
	 * @param s
	 *            The {@link Scene} to sample
	 * @param data
	 *            Information about the image
	 * @return
	 */
	public Color raytrace(int x, int y, Scene s, ImageData data) {
		// Arrays are used for anti-aliasing
		int len = data.antiAliasing ? 4 : 1;
		// System.out.println("Raycasting for pixel ("+x+","+y+")");
		Ray[] primarys = isOrthographic ? getOrthographicRay(x, y) : getPerspectiveRay(x, y, data);
		RaycastHit[] hits = new RaycastHit[len];
		for (int i = 0; i < len; i++) {
			hits[i] = raycast(primarys[i], s);
		}
		Vector3 c = new Vector3(0.0, 0.0, 0.0);
		for (int i = 0; i < len; i++) {
			if (hits[i].isHit) {
				c.add(phongShading(s, primarys[i], hits[i]));
			}
		}
		c.scale(1.0 / (double) len);// Average the colors
		return new Color(round(c.x), round(c.y), round(c.z));

	}

	/**
	 * Not really an easy thing to do with ray tracing. Will either implement
	 * this or add it later.
	 */
	private Ray[] getOrthographicRay(int x, int y) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Constructs a perspective {@link Ray} using the x and y coordinates of an
	 * image. A page on why/how this works will be added to the wiki.
	 * 
	 * @param x
	 *            The x coordinate the {@link Ray} will pass through
	 * @param y
	 *            The y coordinate the {@link Ray} will pass through
	 * @param i
	 *            Data on the image
	 * @return The {@link Ray} that is constructed
	 */
	private Ray[] getPerspectiveRay(int x, int y, ImageData i) {
		double width = (double) i.getWidth();
		double height = (double) i.getHeight();
		double nx = (double) x;
		double ny = (double) y;
		if (!i.antiAliasing) {

			double camX = (2.0 * (nx / width) - 1.0) * i.aspectRatio();
			double camY = -(2.0 * (ny / height) - 1.0);
			Vector3 dir = this.camToWorld.getTransformed(new Vector3(camX, camY, focalLength));
			Ray[] rays = { new Ray(this.pos.clone(), dir) };
			return rays;
		} else {
			double[] xa = { nx - .75, nx + .75 };
			double[] ya = { ny - .75, ny + .75 };
			Ray[] rays = new Ray[4];
			int n = 0;
			for (double xb : xa) {
				for (double yb : ya) {
					double camX = (2.0 * (xb / width) - 1.0) * i.aspectRatio();
					double camY = -(2.0 * (yb / height) - 1.0);
					Vector3 dir = this.camToWorld.getTransformed(new Vector3(camX, camY, focalLength));
					rays[n] = new Ray(this.pos.clone(), dir);
					n++;
				}
			}
			return rays;
		}
	}

	public RaycastHit raycast(Ray r, Scene s) {
		IModel primitive = null;
		Vector3 hitPoint = Vector3.ZERO;
		Vector3 normal = Vector3.ZERO;
		double dist = Double.MAX_VALUE;
		boolean isHit = false;

		for (IModel p : s.objects) {
			Intersection intersection = p.intersects(r);

			if (intersection.isHit) {
				double intersectionDist = pos.distFrom(intersection.hit);
				if (intersectionDist < dist) {
					primitive = p.clone();
					hitPoint = intersection.hit;
					normal = intersection.normal;
					dist = intersectionDist;
					isHit = true;
				}
			}

		}
		//System.out.println(hitPoint);
		return new RaycastHit(primitive, hitPoint, normal, dist, isHit);
	}

	private Vector3 phongShading(Scene s, Ray ray, RaycastHit hit) {
		Vector3 c = new Vector3(0.0, 0.0, 0.0);
		IModel h = hit.itemHit;
		Color itemColor = h.getMaterial().getColor();
		Material m = h.getMaterial();
		Vector3 n = hit.normal.clone();
		c.x = (double) itemColor.getRed() * s.ambient.getInitialIntensity();
		c.y = (double) itemColor.getGreen() * s.ambient.getInitialIntensity();
		c.z = (double) itemColor.getBlue() * s.ambient.getInitialIntensity();
		Vector3 lookVec = ray.getOrigin().getSubtract(hit.hitPoint).getNormalized();
		for (Light l : s.lights) {
			// Vector3 lightVec =
			// l.getPos().getSubtract(hit.hitPoint).getNormalized();
			// Ray r = new Ray(hit.hitPoint.clone(), lightVec.clone());)
			Ray r = Ray.createRayFromPoints(hit.hitPoint, l.getPos());
			RaycastHit hitToLight = raycast(r, s);

			if (hitToLight.isHit && hitToLight.dist > .00001) {
				/*
				 * if(hitToLight.dist<.006){
				 * System.out.println(hitToLight.dist); c = new
				 * Vector3(255,255,255); continue; }
				 */
				// continue;
			}
			Vector3 refVec = n.getScale(2 * r.getDir().dot(n)).getSubtract(r.getDir());
			// diffuse
			double d = max(r.getDir().dot(n), 0);

			c.x += m.Kd() * (double) itemColor.getRed() * d;
			c.y += m.Kd() * (double) itemColor.getGreen() * d;
			c.z += m.Kd() * (double) itemColor.getBlue() * d;
			// specular
			d = Math.pow(max(0, refVec.dot(lookVec)), hit.itemHit.getMaterial().getAlpha());
			c.x += m.Ks() * (double) l.getColor().getRed() * d;
			c.y += m.Ks() * (double) l.getColor().getGreen() * d;
			c.z += m.Ks() * (double) l.getColor().getBlue() * d;
		}
		c.x = clamp(c.x, 0.0, 255.0);
		c.y = clamp(c.y, 0.0, 255.0);
		c.z = clamp(c.z, 0.0, 255.0);
		return c;
	}

	public void translate(Vector3 v) {
		this.pos.add(v);

	}

	public void rotateX(double theta, boolean aroundOrigin) {
		this.camToWorld.rotateX(theta, aroundOrigin);

	}

	public void rotateY(double theta, boolean aroundOrigin) {
		this.camToWorld.rotateY(theta, aroundOrigin);

	}

	public void rotateZ(double theta, boolean aroundOrigin) {
		this.camToWorld.rotateZ(theta, aroundOrigin);

	}

	private double clamp(double num, double min, double max) {
		if (num < min)
			num = min;
		else if (num > max)
			num = max;
		return num;
	}

	private int clamp(int num, int min, int max) {
		if (num < min)
			num = min;
		else if (num > max)
			num = max;
		return num;
	}

	private double max(double a, double b) {
		return a > b ? a : b;
	}

	private int round(double d) {
		return (int) (d + .5);
	}
}
