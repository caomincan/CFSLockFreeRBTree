package lockfree;

import java.util.concurrent.atomic.*;

public class opNode<K extends Comparable<K>,V> {
	public static final int WAITING = 0;
	public static final int IN_PROGRESS = 1;
	public static final int COMPLETED = 2;
	
	public static final int SEARCH = 1;
	public static final int INSEART = 2;
	
	volatile int type;
	volatile K key;
	volatile V value;
	volatile int pid;
    AtomicStampedReference<TreeNode<K,V>> state;
    
    public opNode(K key,V value,int type,int id){
    	this.type = type;
    	this.value = value;
    	this.pid = id;
    	this.state = new AtomicStampedReference<TreeNode<K,V>>(null,-1);
    }
}
