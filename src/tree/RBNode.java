package tree;
import queue.Task;

public class RBNode<V extends Comparable<V>> {
	public volatile V value;
	public volatile RBNode<V> left;
	public volatile RBNode<V> right;
	public volatile RBNode<V> parent;
	public volatile boolean isRed;
	
	public RBNode(){
		this.value = null;
		this.left = null;
		this.right = null;
		this.parent = null;
		this.isRed = false;
	}
	
	public RBNode(V value){
		this.value = value;
		this.task = Task;
		this.left = null;
		this.right = null;
		this.parent = null;
		this.isRed = true;
	}	
}
