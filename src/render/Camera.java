package render;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicInteger;

import engine.Main;
import geometry.Intersection;
import geometry.Ray;
import geometry.Transform;
import geometry.Transformable;
import geometry.Vector3;
import lighting.Light;
import lighting.Material;
import model.IModel;
import render.strategies.RenderStrategy;
import scene.Scene;
import tools.AbstractEventEmitter;
import tools.RaycastHit;
import tools.ThreadPool;

import static org.junit.Assert.assertEquals;

/**
 * Represents the camera used to take picture of a {@link Scene}. A Camera is
 * not part of a {@link Scene}, but is instead it's own entity. This means that
 * it can take pictures of different {@link Scene}s. The resolution of the
 * Camera is also not static, but is instead passed when a the user wants to
 * take a picture of a {@link Scene}.
 * 
 * @author Don Isaac
 *
 */
public class Camera extends AbstractEventEmitter implements Transformable {

	Vector3 pos;
	/**
	 * Camera to world transformation matrix. Used for turning the camera.
	 */
	Transform camToWorld;
	double focalLength;
	private int x, y, width, height;
	/**
	 * True if the camera is currently rendering an image, false otherwise.
	 */
	private boolean rendering;
	private ThreadPool pool;
	private RenderStrategy render;
	private int renderedPixelCount;

	public Camera(Vector3 pos, Transform cameraToWorld, double focalLength) {
		this.pos = pos;
		this.camToWorld = cameraToWorld;
		this.focalLength = focalLength;
		this.rendering = false;
		this.pool = new ThreadPool();
		this.render = RenderStrategy.getStrategy();
		this.renderedPixelCount = 0;
	}

	/**
	 * Takes a picture of a {@link Scene}.
	 * 
	 * @param s    The {@link Scene} to take a picture of
	 * @param data The resolution information to use
	 * @return A picture of the {@link Scene}
	 */
	public BufferedImage takePicture(Scene s, ImageData data) {
		BufferedImage pic = new BufferedImage(data.getWidth(), data.getHeight(), data.getType());
		rendering = true;
		width = data.getWidth();
		height = data.getHeight();
		this.renderedPixelCount = 0;
		int submittedCount = 0; // number of threads submitted to the thread pool

		for (y = 0; y < height; y++) {
			for (x = 0; x < width; x++) {

				this.submitPixelRenderThread(pic, s, data, x, y);
				submittedCount++;
				// This line of code here prints out the rendering progress, but
				// slows down the rendering process drastically (System calls
				// are expensive).
				// System.out.println(100.0*(double)(y*data.getWidth()+x)/(double)(data.getWidth()*data.getHeight())+"%");
//				if (Main.DEBUG) {
//					double percent = 100.0 * (double) (y * width + x) / (double) (width * height);
//					if (percent % 10 == 0 || percent % 10 == 5) {
//						System.out.println(percent + "%");
//					}
//
//				}
			}
		}
		try {
			pool.shutdown();
			System.out.println("Is pool running? " + pool.isRunning());
			System.out.println("Threads in pool after shutdown(): " + pool.getTaskQueue().size());
			System.out.println("Submitted threads: " + submittedCount);
			int expectedCalculatedPixelCount = width * height;
			assertEquals(expectedCalculatedPixelCount, getRenderedPixelCount());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (AssertionError e) {
			System.out.println(e.getMessage());
		}

		rendering = false;
		return pic;
	}
	
	private synchronized void submitPixelRenderThread(BufferedImage pic, Scene s, ImageData data, int x, int y) {
		pool.submit(() -> {
			
			try {
				synchronized (pic) {
					Color pixel = raytrace(x, y, s, data);
					pic.setRGB(x, y, pixel.getRGB());
					this.incrementRenderedPixelCount();
				}
//				} else {
//					System.out.println(String.format("Attempted to raytrace (%d, %d)", x, y));
//				}
			} catch (ArrayIndexOutOfBoundsException e) {
				System.err.println("ArrayIndexOutOfBoundsException thrown while writing pixel data to pixel ("
						+ x + "," + y + "). Width: " + width + " Height: " + height);
				e.printStackTrace();
			} 
			

		});
	}

	/**
	 * Gets the color of a specific pixel from a {@link Scene}. Using this method
	 * across an entire image results in a fully rendered image.
	 * 
	 * @param x    The pixel's x location on the image
	 * @param y    The pixel's y location on the image
	 * @param s    The {@link Scene} to sample
	 * @param data Information about the image
	 * @return
	 */
	public Color raytrace(int x, int y, Scene s, ImageData data) {
		// Arrays are used for anti-aliasing
		int len = data.antiAliasing ? 4 : 1;
		// System.out.println("Raycasting for pixel ("+x+","+y+")");
		Ray[] primaries = getPerspectiveRay(x, y, data);
		RaycastHit[] hits = new RaycastHit[len];

		for (int i = 0; i < len; i++) {
			hits[i] = raycast(primaries[i], s);
		}
		Vector3 c = new Vector3(0.0, 0.0, 0.0);
		for (int i = 0; i < len; i++) {
			if (hits[i].isHit) {
				c.add(render.render(this, s, primaries[i], hits[i]));
			}
		}
		c.scl(1.0 / (double) len);// Average the colors
		return new Color(round(c.x), round(c.y), round(c.z));

	}

	/**
	 * Constructs a perspective {@link Ray} using the x and y coordinates of an
	 * image. A page on why/how this works will be added to the wiki.
	 * 
	 * @param x The x coordinate the {@link Ray} will pass through
	 * @param y The y coordinate the {@link Ray} will pass through
	 * @param i Data on the image
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
			// 4 rays per pixel instead of 1
			double[] xa = { nx - .25, nx + .25 };
			double[] ya = { ny - .25, ny + .25 };
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
				if (intersectionDist < dist && intersectionDist > 0.0) {
					primitive = p.clone();
					hitPoint = intersection.hit;
					normal = intersection.normal;
					dist = intersectionDist;
					isHit = true;
				}
			}

		}
		// System.out.println(hitPoint);
		return new RaycastHit(primitive, hitPoint, normal, dist, isHit);
	}

	private synchronized int incrementRenderedPixelCount() {
		return this.renderedPixelCount++;
	}

	private synchronized int getRenderedPixelCount() {
		return this.renderedPixelCount;
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

	public double requestProgress() {
		if (rendering)
			return 100.0 * (double) (y * width + x) / (double) (width * height);
		else {
			System.err.println("progress requested while Camera was not rendering.");
			return -1.0;
		}
	}

	/**
	 * Rounds a floating point number to the nearest integer.
	 * 
	 * @param d the number to round
	 * @return the rounded number
	 */
	protected int round(double d) {
		return (int) (d + .5);
	}

}
