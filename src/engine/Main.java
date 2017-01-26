package engine;

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
	public static boolean DEBUG=true;
	public static void main(String[] args) {
		if(RUN_DEBUG_ENVIRONMENT)
			new DebugEnvironment();
		else if(VISUAL)
			new VisualEngine("Raytracing Engine");
		else
			new NonVisualEngine();

	}

}
