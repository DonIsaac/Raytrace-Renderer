package engine;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import tools.TeeOutputStream;

import debug.DebugEnvironment;
/**
 * Entry point for the Raytracing Engine
 * @author Donny
 * @version 3.2.1
 *
 */
public class Main {
	public static boolean VISUAL = false;
	public static boolean RUN_DEBUG_ENVIRONMENT = false;
	public static boolean DEBUG = true;
	public static boolean PIPE_STDOUT_TO_FILE = true;
	public static String LOG_NAME = "log.txt";
	
	public static void main(String[] args) throws FileNotFoundException {
		if (PIPE_STDOUT_TO_FILE) {
			TeeOutputStream stdoutStream = new TeeOutputStream(System.out, new BufferedOutputStream(new FileOutputStream(LOG_NAME)));
			System.setOut(new PrintStream(stdoutStream, true));
		}
			
		if(RUN_DEBUG_ENVIRONMENT)
			new DebugEnvironment();
		else if(VISUAL)
			new VisualEngine("Raytracing Engine");
		else
			new NonVisualEngine();

	}
	


}
