package tools;

import geometry.Vector3;
import model.IModel;
/**
 * A struct to store information about a raycast attempt.
 * @author Donny
 *
 */
public class RaycastHit {
	/***/
	public IModel itemHit;
	public Vector3 hitPoint;
	public double dist;
	public boolean isHit;
	
	public RaycastHit(IModel itemHit, Vector3 hitPoint, double dist, boolean isHit){
		this.itemHit=itemHit;
		this.hitPoint=hitPoint;
		this.dist=dist;
		this.isHit=isHit;
	}
}
