package debug;

import engine.Main;
import tools.Epsilon;

public class Assert {

	public static void assertEquals(String message, Object expected, Object actual){
		if(!expected.equals(actual))
			fail(message,expected,actual);
	} 
	
	public static void assertEquals(Object expected, Object actual){
		assertEquals(null,expected,actual);
	}
	
	public static void assertEquals(String message, boolean expected, boolean actual){
		if(expected != actual){
			fail(message, expected, actual);
		}
	}
	public static void assertEquals(boolean expected, boolean actual){
		assertEquals(null, expected, actual);
	}
	
	public static void assertEquals(String message, int expected, int actual){
		if(expected != actual){
			fail(message, expected, actual);
		}
	}
	public static void assertEquals(int expected, int actual){
		assertEquals(null, expected, actual);
	}
	
	public static void assertEquals(String message, double expected, double actual){
		assertEquals(message, 0.0, expected, actual);
	}
	
	public static void assertEquals(String message, double errorMargin, double expected, double actual){
		if(!Epsilon.nearlyEquals(expected, actual)){
			fail(message, expected, actual);
		}
	}
	public static void assertEquals(double expected, double actual){
		assertEquals(null, expected, actual);
	}
	private static void fail(String message, Object expected, Object actual){
		if(Main.DEBUG){
		String formatted = "Values should be different. ";
        if (message != null) {
            formatted = message + ". ";
        }

        formatted += "Expected: "+expected +" Actual: " + actual;
        throw new AssertionError(formatted);
		}
	}
}
