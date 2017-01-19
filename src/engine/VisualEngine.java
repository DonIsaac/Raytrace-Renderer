package engine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import geometry.Transform;
import geometry.Vector3;
import lighting.AmbientLight;
import lighting.DefaultMaterial;
import lighting.PointLight;
import model.PlaneModel;
import model.SphereModel;
import render.Camera;
import render.ImageData;
import scene.Scene;
/**
 * Non-Visual driver class for the ray tracing program
 * @author Donny
 * @version 2.2.3.1
 *
 */
public class VisualEngine extends JPanel implements Runnable, KeyListener,
		WindowListener {
	Thread thread = null;
	private boolean isRunning = false, isCreated = false,isRendered=false;
	private int width, height;
	private BufferedImage screen;
	private Graphics g;
	
	long startTime;
	
	int scale = 40;
	Camera cam;
	Scene s;
	ImageData data;
	int x,y;
	private boolean _16_9 = true;
	DefaultMaterial red,blue,pink,green,orange,white,yellow,cyan,grey;

	private void initialize() {	
		cam = new Camera(new Vector3(0,.5,-.7), Transform.getIdentityInstance(), 1.7);
		s = new Scene();
		loadMaterials();
		s.ambient = new AmbientLight(Color.white,.1);
		s.lights.add(new PointLight(new Vector3(-3,4,0),Color.white,1.0));
		//loadSnowman();
		loadSphereModels();
		data = new ImageData(getWidth(), getHeight(),			
				BufferedImage.TYPE_INT_RGB, true);

		x = y = 0;
		startTime=System.currentTimeMillis();
	}

	private void update() {
		if (y < height) {
			Color c = cam.raytrace(x, y, s, data);
			screen.setRGB(x, y, c.getRGB());
			System.out.println(100.0*(double)(y*width+x)/(double)(width*height)+"%");
			x++;
			if (x >= width) {
				x = 0;
				y++;
			}
		}
		else{
			if(!isRendered){
			System.out.println("Finished!");
			long dt = System.currentTimeMillis()-startTime;
			int seconds=(int) ((dt/1000)%60);
			long minutes=((dt-seconds)/1000)/60;
			System.out.println("Total render time: "+minutes+"m, "+seconds+"s");
			screenshot();
			isRendered=true;
			//System.exit(0);
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
	}

	private void paintScreen() {
		Graphics gr;
		try {
			gr = this.getGraphics();
			if ((gr != null) && (screen != null))
				gr.drawImage(screen, 0, 0, null);
			Toolkit.getDefaultToolkit().sync();
			if (gr != null)
				gr.dispose();
		} catch (Exception e) {
			System.out.println("Graphics context error: " + e);
			e.printStackTrace();
		}
	}

	private void loadSnowman(){
		s.objects.add(new PlaneModel(new Vector3(0,-1.2,0),new Vector3(0,1,0),green));
		s.objects.add(new SphereModel(new Vector3(0,-.7,4), 1.0, orange));//bottom
		s.objects.add(new SphereModel(new Vector3(0,.7,4), .8, cyan));//middle
		s.objects.add(new SphereModel(new Vector3(0,1.8,4), .5, red));//top
		s.objects.add(new SphereModel(new Vector3(-.2,1.7,3), .1, yellow));//left eye
		s.objects.add(new SphereModel(new Vector3(.2,1.7,3), .1, grey));//right eye
		
	}
	private void loadSphereModels(){
		s.objects.add(new SphereModel(new Vector3(0, 0, 3.5), 1.0,green));
		s.objects.add(new SphereModel(new Vector3(0,1.0,3.8),1.5,pink));
		s.objects.add(new SphereModel(new Vector3(1.0,-.2,3.0),.5,yellow));
		s.objects.add(new SphereModel(new Vector3(-1, .8,3.2),.7,cyan));
		s.objects.add(new SphereModel(new Vector3(.8,.6,2.8),.56,orange));
	}
	private void loadMaterials(){
		double alpha = 100, ks = .25,kd=1.0;
		red = new DefaultMaterial(Color.red,alpha,ks,kd);
		orange = new DefaultMaterial(Color.orange,alpha,ks,kd);
		green = new DefaultMaterial(Color.green,alpha,ks,kd);
		cyan = new DefaultMaterial(Color.cyan,alpha,ks,kd);
		yellow = new DefaultMaterial(Color.yellow,alpha,ks,kd);
		blue = new DefaultMaterial(Color.blue,alpha,ks,kd);
		pink = new DefaultMaterial(Color.pink,alpha,ks,kd);
		white = new DefaultMaterial(Color.white,alpha,ks,kd);
		grey = new DefaultMaterial(Color.white.darker(),alpha,ks,kd);
	}
	public VisualEngine(String title) {
		if (_16_9) {
			width = scale * 16;
			height = scale * 9;
		} else {
			width = 300;
			height = 300;
		}
		this.setSize(width, height);
		this.setFocusable(true);
		this.requestFocus();
		JFrame f = createJFrame(title);
		this.start();
		initialize();
		f.setVisible(true);
	}

	private void screenshot(){
		try {
			g.setFont(new Font("Arial",Font.PLAIN, (int)(width/4*.1)));
			g.setColor(Color.white);
			g.drawString("Created by Donny Isaac", 5,height-g.getFontMetrics().getHeight());
			Random r = new Random();
			String i ="Image"+r.nextInt(9999999)+".png";
			ImageIO.write(screen, "png", new File("C:\\Users\\Donny\\Documents\\.Programming Projects\\RaycasingEngine\\Pictures\\"+i));
			System.out.println("File Saved!");
		} catch (IOException e1) {
		
			e1.printStackTrace();
		}
	
	}
	public void run() {
		while (isRunning) {
			if (isCreated) {
				update();
				paintScreen();
			} else {
				isCreated = createGraphics();
			}
		}

	}

	
	private boolean createGraphics() {
		if (screen == null) {
			screen = (BufferedImage) createImage(width, height);
			if (screen == null)
				return false;
			else {
				g = screen.getGraphics();
				for (int x = 0; x < getWidth(); x++) {
					for (int y = 0; y < getHeight(); y++) {
						screen.setRGB(x, y, Color.blue.getRGB());
					}
				}
				return true;
			}
		} else {
			g = screen.getGraphics();
			for (int x = 0; x < getWidth(); x++) {
				for (int y = 0; y < getHeight(); y++) {
					screen.setRGB(x, y, Color.blue.getRGB());
				}
			}
			return true;
		}
	}

	private synchronized void start() {
		if (thread == null || !isRunning) {
			thread = new Thread(this);
			thread.setDaemon(true);
			isRunning = true;
			isCreated = createGraphics();
			thread.start();
		}

	}

	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
			screenshot();
	}

	public void keyReleased(KeyEvent arg0) {

	}

	public void keyTyped(KeyEvent arg0) {

	}

	private JFrame createJFrame(String title) {
		JFrame frame = new JFrame(title);
		frame.add(this);
		frame.setSize(width, height);
		frame.addKeyListener(this);
		frame.addWindowListener(this);
		frame.setDefaultCloseOperation(3);
		frame.setLocationRelativeTo(null);
		return frame;
	}

	public void windowActivated(WindowEvent arg0) {

	}

	public void windowClosed(WindowEvent e) {
		stop();
	}

	public void windowClosing(WindowEvent e) {

	}

	public void windowDeactivated(WindowEvent e) {

	}

	public void windowDeiconified(WindowEvent e) {

	}

	public void windowIconified(WindowEvent e) {

	}

	public void windowOpened(WindowEvent arg0) {
		

	}
}
