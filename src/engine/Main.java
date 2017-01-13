package engine;

import debug.DebugEnvironment;

public class Main {
	public static boolean VISUAL = false;
	public static boolean RUN_DEBUG_ENVIRONMENT = false;
	public static boolean DEBUG=false;
	public static void main(String[] args) {
		if(RUN_DEBUG_ENVIRONMENT)
			new DebugEnvironment();
		else if(VISUAL)
			new VisualEngine("Raytracing Engine");
		else
			new NonVisualEngine();

	}

}
