package lockfree;

import java.util.concurrent.atomic.*;

public class dNode<K extends Comparable<K>,V> {
	public static final int FREE = 0;
	public static final int OWNED = 1;
	//node color
	AtomicBoolean color; 
	//node K value
	K key;   
	// the address of the record that contains the value
	// associated with the key
	AtomicReference<V> value;
	// operation data
	AtomicReference<opNode<K,V>> operation;
	
	AtomicStampedReference<TreeNode<K,V>> left;
	AtomicStampedReference<TreeNode<K,V>> right;
	// information about the executing window transaction
	// the address of next location of the operation window
	AtomicStampedReference<TreeNode<K,V>> next;
	
	public dNode(K key, V value){
		this.key= key;
		this.value = new AtomicReference<V>(value);
		this.color = new AtomicBoolean(false);
		this.operation = new AtomicReference<opNode<K,V>>(null);
		this.left = new AtomicStampedReference<TreeNode<K,V>>(null,FREE);
		this.right = new AtomicStampedReference<TreeNode<K,V>>(null,FREE);
		
		this.next = new AtomicStampedReference<TreeNode<K,V>>(null,-1);
	}
	
	public boolean isLeaf(){
		int[] stampHolder = new int[1];
		if(left.get(stampHolder)!= null || right.get(stampHolder) != null)
			return false;
		else
			return true;
	}
	
	public  void clone(dNode<K,V> node){
		this.key = node.key;
		this.value.set(node.value.get());
		this.color.set(node.color.get());
		this.left.set(node.left.getReference(), node.left.getStamp());
		this.right.set(node.right.getReference(), node.right.getStamp());
	}
}
