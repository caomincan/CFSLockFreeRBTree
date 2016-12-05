package tree;

import java.util.concurrent.atomic.*;

public class LockFreeRBNode<V extends Comparable<V>> {
	public volatile V value;
	public volatile LockFreeRBNode<V> left;
	public volatile LockFreeRBNode<V> right;
	public volatile LockFreeRBNode<V> parent;
	public volatile boolean isRed;
	public AtomicBoolean flag;
	
	public LockFreeRBNode(){
		this.value = null;
		this.left = null;
		this.right = null;
		this.parent = null;
		this.isRed = false;
		this.flag = new AtomicBoolean(false);
	}
	
	public LockFreeRBNode(V value){
		this.value = value;
		this.left = null;
		this.right = null;
		this.parent = null;
		this.isRed = true;
		this.flag = new AtomicBoolean(false);
	}	
	
	public String toString(){
		return (value==null)?" _ ":value.toString()+(isRed?"_R":"_B")+(flag.get()?"T":"F");
	}
}
