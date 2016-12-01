package render;

import java.awt.Color;
import java.awt.image.BufferedImage;

import geometry.Intersection;
import geometry.Primitive;
import geometry.Ray;
import geometry.Transform;
import geometry.Transformable;
import geometry.Vector3;
import lighting.Light;
import lighting.Material;
import model.IModel;
import scene.Scene;
import tools.RaycastHit;

public class Camera implements Transformable {

	Vector3 pos;
	Transform camToWorld;
	double focalLength;
	boolean isOrthographic;

	public Camera(Vector3 pos, Transform cameraToWorld, double focalLength,
			boolean isOrthographic) {
		this.pos = pos;
		this.camToWorld = cameraToWorld;
		this.focalLength = focalLength;
		this.isOrthographic = isOrthographic;
	}

	public BufferedImage takePicture(Scene s, ImageData data) {
		BufferedImage pic = new BufferedImage(data.getWidth(),
				data.getHeight(), data.getType());
		int width = data.getWidth(),height = data.getHeight();
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				pic.setRGB(x, y, getColorAtPixel(x,y,s,data).getRGB());
				//System.out.println(100.0*(double)(y*width+x)/(double)(width*height)+"%");
			}
		}
		return pic;
	}

	public Color getColorAtPixel(int x, int y, Scene s, ImageData data) {
		int len = data.antiAliasing?4:1;
		// System.out.println("Raycasting for pixel ("+x+","+y+")");
		Ray[] primarys = isOrthographic ? getOrthographicRay(x, y)
				: getPerspectiveRay(x, y, data);
		RaycastHit[] hits = new RaycastHit[len];
		for(int i=0; i<len;i++){
		hits[i] = raycast(primarys[i], s);
		}
		Vector3 c = new Vector3(0.0,0.0,0.0);
		for(int i = 0; i < len; i++){
			if(hits[i].isHit){
				c.add(phongShading(s,primarys[i],hits[i]));
			}
		}
		c.scale(1.0/(double)len);
		return new Color(round(c.x),round(c.y),round(c.z));
		
	}

	private Ray[] getOrthographicRay(int x, int y) {
		throw new UnsupportedOperationException();
	}

	private Ray[] getPerspectiveRay(int x, int y, ImageData i) {
		double width = (double) i.getWidth();
		double height = (double) i.getHeight();
		double nx = (double) x;
		double ny = (double) y;
		if(!i.antiAliasing){

		double camX = (2.0 * (nx / width) - 1.0) * i.aspectRatio();
		double camY = -(2.0 * (ny / height) - 1.0);
		Vector3 dir = this.camToWorld.getTransformed(new Vector3(camX, camY,
				focalLength));
		Ray[] rays = {new Ray(this.pos.clone(), dir)};
		return rays;
		}else{
			double[] xa={nx-.75,nx+.75};
			double[] ya={ny-.75,ny+.75};
			Ray[] rays = new Ray[4];
			int n=0;
			for(double xb:xa){
				for(double yb:ya){
					double camX = (2.0 * (xb / width) - 1.0) * i.aspectRatio();
					double camY = -(2.0 * (yb / height) - 1.0);
					Vector3 dir = this.camToWorld.getTransformed(new Vector3(camX, camY,
							focalLength));
					rays[n] = new Ray(this.pos.clone(),dir);
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
			Intersection i = p.intersects(r);
			if (i.isHit) {
				double intersectionDist = r.getOrigin().distFrom(
						i.getClosestIntersection(r.getOrigin()));
				if (intersectionDist < dist) {
					primitive = p.clone();
					hitPoint = i.getClosestIntersection(r.getOrigin()).clone();
					dist = intersectionDist;
					isHit = true;
				}
			}

		}
		System.out.println(hitPoint);
		return new RaycastHit(primitive, hitPoint, dist, isHit);
	}

	private Vector3 phongShading(Scene s, Ray ray, RaycastHit hit) {
		Vector3 c = new Vector3(0.0,0.0,0.0);
		IModel h = hit.itemHit;
		Color itemColor=h.getMaterial().getColor();
		Material m = h.getMaterial();
		Vector3 n = h.getNormal(hit.hitPoint).clone();
		c.x = (double)itemColor.getRed()
				* s.ambient.getInitialIntensity();
		c.y = (double)itemColor.getGreen()
				* s.ambient.getInitialIntensity();
		c.z = (double)itemColor.getBlue()
				* s.ambient.getInitialIntensity();
		Vector3 lookVec = ray.getOrigin().getSubtract(hit.hitPoint).getNormalized();
		for (Light l : s.lights) {
			//Vector3 lightVec = l.getPos().getSubtract(hit.hitPoint).getNormalized();
			//Ray r = new Ray(hit.hitPoint.clone(), lightVec.clone());)
			Ray r = Ray.createRayFromPoints(hit.hitPoint, l.getPos() );
			RaycastHit hitToLight = raycast(r, s);
			
			if(hitToLight.isHit&&hitToLight.dist>.00001){ 
			/*	if(hitToLight.dist<.006){
					System.out.println(hitToLight.dist);
					c = new Vector3(255,255,255);
					continue;
				}*/
				//continue;
			}
			Vector3 refVec = n.getScale(2*r.getDir().dot(n)).getSubtract(r.getDir());
			//diffuse
			double d = max(r.getDir().dot(n),0);
			
			c.x += m.Kd()*(double)itemColor.getRed()*d;
			c.y += m.Kd()*(double)itemColor.getGreen()*d;
			c.z += m.Kd()*(double)itemColor.getBlue()*d;
			//specular
			d = Math.pow(max(0,refVec.dot(lookVec)), hit.itemHit.getMaterial().getAlpha());
			c.x += m.Ks()*(double)l.getColor().getRed()*d;
			c.y += m.Ks()*(double)l.getColor().getGreen()*d;
			c.z += m.Ks()*(double)l.getColor().getBlue()*d;
		}
		c.x = clamp(c.x,0.0,255.0);
		c.y = clamp(c.y,0.0,255.0);
		c.z = clamp(c.z,0.0,255.0);
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
	
	private double max(double a, double b){
		return a>b?a:b;
	}

	private int round(double d) {
		return (int) (d + .5);
	}
}
