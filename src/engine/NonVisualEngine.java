package engine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import geometry.Transform;
import geometry.Vector3;
import lighting.AmbientLight;
import lighting.DefaultMaterial;
import lighting.PointLight;
import model.ModelInstance;
import model.ModelLoader;
import model.PlaneModel;
import model.SphereModel;
import render.Camera;
import render.ImageData;
import scene.Scene;

/**
 * Non-Visual driver class for the ray tracing program.
 * 
 * @author Donny
 *
 */
public class NonVisualEngine {
	private int width, height;
	private BufferedImage img;

	long startTime;

	int scale = 120;
	Camera cam;
	Scene s;
	ImageData data;
	int x, y;
	private boolean _16_9 = false;
	DefaultMaterial red, blue, pink, green, orange, white, yellow, cyan, grey, black;

	private void initialize() {
		// cam = new Camera(new Vector3(0, .5, -.7), Transform.getIdentityInstance(),
		// 1.7);
		cam = new Camera(new Vector3(0, 0, 0), Transform.getIdentityInstance(), 1.7);
		s = new Scene();
		loadMaterials();
		s.ambient = new AmbientLight(Color.white, .1);
		// loadSnowman();
		loadSphereModels();
		// loadSphereModels2();
		// loadBMW();
		// loadModel();
		// loadMonkey();
		data = new ImageData(width, height, BufferedImage.TYPE_INT_RGB, false);

		x = y = 0;
		startTime = System.currentTimeMillis();
		System.out.println("Initialized");
	}

	public void run() {
		img = cam.takePicture(s, data);
		System.out.println("Finished!");
		long dt = System.currentTimeMillis() - startTime;
		int seconds = (int) ((dt / 1000) % 60);
		long minutes = ((dt - seconds) / 1000) / 60;
		System.out.println("Total render time: " + minutes + "m, " + seconds + "s");
		screenshot();
	}

//	private void loadMonkey() {
//		s.lights.add(new PointLight(new Vector3(-3, 5, -2), Color.white, 1.0));
//		
//		try {
//			ModelInstance m = ModelLoader.loadObjModel(new File(), material)
//		}
//		
//	}
	private void loadModel() {
		s.lights.add(new PointLight(new Vector3(-3, 5, -2), Color.white, 1.0));
		// cam.translate(new Vector3(-1,.5,-3));
		cam.translate(new Vector3(2.5, 1.5, -2.5));
		cam.rotateX(.3, true);
		cam.rotateY(-Math.PI / 4.0, true);
		// cam.rotateX(.2, true);
		// s.objects.add(new SphereModel(new Vector3(0, 0,0), 2.3, green));
		try {
			ModelInstance m = ModelLoader.loadObjModel(new File("moneky_smooth.obj"), red);
			System.out.println(m);
			s.objects.add(m);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadBMW() {
		s.lights.add(new PointLight(new Vector3(3, 5, -2), Color.white, 1.0));
		// cam.translate(new Vector3(-2.0, 1.5, -4.7));
		cam.translate(new Vector3(4.5, 1.5, -2.5));
		cam.rotateX(.2, true);
		cam.rotateY(-Math.PI / 4.0, true);
		// cam.rotateY(.3, true);
		// cam.rotateX(.2, true);
		// s.objects.add(new SphereModel(new Vector3(0, 0,0), 2.3, green));
		try {
			ModelInstance m = ModelLoader.loadObjModel(new File("BMWModel2.obj"), red);
			System.out.println(m);
			s.objects.add(m);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadSnowman() {
		s.lights.add(new PointLight(new Vector3(-3, 4, 0), Color.white, 1.0));
		s.objects.add(new PlaneModel(new Vector3(0, -1.2, 0), new Vector3(0, 1, 0), green));
		s.objects.add(new SphereModel(new Vector3(0, -.7, 4), 1.0, white));// bottom
		s.objects.add(new SphereModel(new Vector3(0, .7, 4), .8, white));// middle
		s.objects.add(new SphereModel(new Vector3(0, 1.8, 4), .5, white));// top
		s.objects.add(new SphereModel(new Vector3(-.2, 1.7, 3), .1, black));// left
																			// eye
		s.objects.add(new SphereModel(new Vector3(.2, 1.7, 3), .1, black));// right
																			// eye

	}

	private void loadSphereModels() {
		cam.translate(new Vector3(-.3f, .7f, 0f));
		s.lights.add(new PointLight(new Vector3(-3, 4, 0), Color.white, 1.0));
		s.objects.add(new SphereModel(new Vector3(0, 0, 3.5), 1.0, green));
		s.objects.add(new SphereModel(new Vector3(0, 1.0, 3.8), 1.5, pink));
		s.objects.add(new SphereModel(new Vector3(1.0, -.2, 3.0), .5, yellow));
		s.objects.add(new SphereModel(new Vector3(-1, .8, 3.2), .7, cyan));
		s.objects.add(new SphereModel(new Vector3(.8, .6, 2.8), .56, orange));
	}

	private void loadSphereModels2() {
		cam.translate(Vector3.J.getScale(2));
		cam.rotateX(20 * Math.PI / 180, true);
		s.lights.add(new PointLight(new Vector3(-3, 3, 3.5), Color.white, 1.0));
		s.objects.add(new PlaneModel(Vector3.ZERO, Vector3.J, blue));
		s.objects.add(new SphereModel(new Vector3(-1, .8, 4), 1.2, green));
		s.objects.add(new SphereModel(new Vector3(0, .8, 5.5), 1.2, red));
	}

	private void loadMaterials() {
		double alpha = 100, ks = .25, kd = 1.0;
		red = new DefaultMaterial(Color.red, alpha, ks, kd);
		orange = new DefaultMaterial(Color.orange, alpha, ks, kd);
		green = new DefaultMaterial(Color.green, alpha, ks, kd);
		cyan = new DefaultMaterial(Color.cyan, alpha, ks, kd);
		yellow = new DefaultMaterial(Color.yellow, alpha, ks, kd);
		blue = new DefaultMaterial(Color.blue, alpha, ks, kd);
		pink = new DefaultMaterial(Color.pink, alpha, ks, kd);
		white = new DefaultMaterial(Color.white, alpha, ks, kd);
		grey = new DefaultMaterial(Color.white.darker(), alpha, ks, kd);
		black = new DefaultMaterial(Color.black, alpha, ks, kd);
	}

	public NonVisualEngine() {
		if (_16_9) {
			width = scale * 16;
			height = scale * 9;
		} else {
			width = 300;
			height = 300;
		}
		initialize();
		setupInput();
		this.run();
		System.exit(0);
	}

	private void setupInput() {
		JFrame frame = new JFrame();
		KeyListener l = new KeyListener() {

			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_P)
					System.out.println(cam.requestProgress() + "%");

			}

			public void keyReleased(KeyEvent arg0) {
			}

			public void keyTyped(KeyEvent arg0) {
			}

		};
		frame.addKeyListener(l);
		frame.setSize(300, 300);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(3);
		frame.setVisible(true);
	}

	private void screenshot() {
		try {
			Date now = new Date();
			DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
			String dateString = df.format(now)
					.replace(' ', '-').replace(':', '-')
					+ "-" + now.getHours()+ "h-" + now.getMinutes() + "m";
					
			String signature = "Created by Don Isaac (" + df.format(now) + ")";

			Graphics g = img.getGraphics();
			g.setFont(new Font("Arial", Font.PLAIN, (int) (width / 4 * .1)));
			g.setColor(Color.white);
			g.drawString(signature, 5, height - g.getFontMetrics().getHeight());
			Random r = new Random();

			String i = "Image-" + dateString + ".png";
			ImageIO.write(img, "png", new File("D:\\Personal\\Raytrace Files\\Pictures\\" + i) {
				{
					createNewFile();
				}
			});
			System.out.println("File Saved as " + i + "!");
		} catch (IOException e1) {

			e1.printStackTrace();
		}

	}

}
