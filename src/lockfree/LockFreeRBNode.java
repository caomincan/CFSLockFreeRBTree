package lockfree;

public class LockFreeRBNode<V extends Comparable<V>> {
	public volatile V value;
	public volatile LockFreeRBNode<V> left;
	public volatile LockFreeRBNode<V> right;
	public volatile LockFreeRBNode<V> parent;
	public volatile boolean isRed;
	
	public LockFreeRBNode(){
		this.value = null;
		this.left = null;
		this.right = null;
		this.parent = null;
		this.isRed = false;
	}
	
	public LockFreeRBNode(V value){
		this.value = value;
		this.left = null;
		this.right = null;
		this.parent = null;
		this.isRed = true;
	}	
}
