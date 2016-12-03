package lockfree;

import java.util.concurrent.atomic.*;

public class TreeNode<K extends Comparable<K>,V> {
	AtomicStampedReference<dNode<K,V>> pNode; // pointer node and flag
	dNode<K,V> dNode;
	
	public TreeNode(dNode<K,V> node,int flag){
		pNode = new AtomicStampedReference<dNode<K,V>>(node,flag);
		dNode = node;
	}

}
