package tools;
/**
 * Holds the value of Machine Epsilon and related methods.
 * @author Donny
 *
 */
public class Epsilon {
	/** Machine Epsilon */
	public static final double E=createE();
	/**
	 * Determines if two doubles are equal to each other while factoring in imprecisions.
	 * @param a The first double to compare
	 * @param b The second double to compare
	 * @return <code>true</code> if equal, <code>false</code> otherwise
	 */
	public static boolean nearlyEquals(double a, double b){
		if(a==b)
			return true;
		double absA=Math.abs(a);
		double absB=Math.abs(b);
		double diff=Math.abs(a-b);
		
		if (a == 0 || b == 0 || diff < Float.MIN_NORMAL) {
			// a or b is zero or both are extremely close to it
			// relative error is less meaningful here
			return diff < (E * Float.MIN_NORMAL);
		} else { // use relative error
			return diff / Math.min((absA + absB), Float.MAX_VALUE) < E;
		}
	}
	/**
	 * Determines the Machine Epsilon at runtime.
	 * @return Machine Epsilon
	 */
	private static double createE(){
		double e=.5;
		while(1.0+e>1.0){
			e*=.5;
		}
		return e;
	}
}
