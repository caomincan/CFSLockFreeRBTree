package lockfree;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.*;

public class LockFreeRBTree2<K extends Comparable<K>,V> {
	int num_threads;
	TreeNode<K,V> root;
	List<opNode<K,V>> ST;  // search table
	List<opNode<K,V>> MT;  // modify table
	
	public LockFreeRBTree2(int num){
		this.num_threads = num;
		this.root  = new TreeNode<K,V>(null,dNode.FREE);
		this.ST = new ArrayList<opNode<K,V>>(num);
		this.MT = new ArrayList<opNode<K,V>>(num);
		for(int i=0;i<num;i++){
			ST.set(i, null);
			MT.set(i, null);
		}
	}
	
	public V search(K key,int myid){
		// create and initialize a new operation record
		//  key,value, type, process id
		opNode<K,V> opData = new opNode<K,V>(key,null,opNode.SEARCH,myid);
		// initialize the operation state
		opData.state.set(null, opNode.IN_PROGRESS);
		// initialize the search table entry
		int[] stampHolder = new int[1];
		ST.set(myid,opData);
		// traverse the tree
		traverse(opData);
		if(opData.state.get(stampHolder) != null){
			//read the value stored in the record using Chuong algorithm
			return opData.state.getReference().dNode.value.get();
		}else{
			return null;
		}	
	}
	
	public void traverse(opNode<K,V> opData){
		TreeNode<K,V> curr = this.root;
		// start from the root of the tree
		while(curr != null && !curr.dNode.isLeaf()){
			if(opData.state.getStamp() == opNode.COMPLETED)
				return;
			if(opData.key.compareTo(curr.dNode.key) <0){
				curr = curr.dNode.left.getReference();
			}else{
				curr = curr.dNode.right.getReference();
			}
		}
		V valData;
		if(curr.dNode.key.compareTo(opData.key) == 0){
		  opData.state.set(curr, opNode.COMPLETED);
		  valData = curr.dNode.value.get();
		}else{
		  valData = null;	
		  opData.state.set(new TreeNode<K,V>(new dNode<K,V>(opData.key,valData),-1), opNode.COMPLETED);
		}
		
	}
	/*
	 *  pid should obtain by round-robin manner
	 */
	public void insert(K key, V value,int myid,int pid){
		V valData = null;
		search(key,myid);
		valData = ST.get(myid).state.getReference().dNode.value.get();
		if(valData == null){
			// phase 2: try to add the key-value pair to the tree using the MTL-framework
			// select a search operation to help at the end of phase 2 to ensure wait-freedom
			opNode<K,V> pidOpData = ST.get(pid);
			// create and initialize a new operation record
			opNode<K,V> opData = new opNode<K,V>(key,value,opNode.INSEART,myid);
			// add the key-value pair to the tree
			executeOperation(opData,myid,pid);
			valData = opData.state.getReference().dNode.value.get();
			// help the selected search operation complete
			if(pidOpData != null){
				traverse(pidOpData);
			}
		}
		if(valData != null){
			// phase 3: update the value in the record using Chuing algorithm
		}
	}
	
	protected void executeOperation(opNode<K,V> opData,int myid,int pid){
		//initialize the operation state
		opData.state.set(root, opNode.WAITING);
		// initialize the modify table entry
		MT.set(myid, opData);
		// selected a modify operation to help later at the end to ensure wait-freedom
		opNode<K,V> pidOpData = MT.get(pid);
		
		//inject the operation into the tree
		injectOperation(opData);
		//repeatdly execute transactions until the operation completes
		int[] status = new int[1];
		AtomicStampedReference<dNode<K,V>> pCurrent = opData.state.get(status).pNode;
		while(status[0] != opNode.COMPLETED){
			dNode<K,V> dCurrent = pCurrent.getReference();
			if(dCurrent.operation.get() == opData){
				executeWindowTransaction(pCurrent,dCurrent);
			}
			pCurrent = opData.state.get(status).pNode;
		}
		if(pidOpData != null){
			injectOperation(pidOpData);
		}
	}
	
	protected void injectOperation(opNode<K,V> opData){
		// repeatedly try until the operation is jnjected into the tree
		while(opData.state.getStamp() == opNode.WAITING){
			dNode<K,V> dRoot = root.pNode.getReference();
			// execute a window transaction, if needed
			if(dRoot.operation.get() != null){
				executeWindowTransaction(root.pNode,dRoot);
			}
			// read the address of the data node again
			// if they match, then try to inject the operation into the tree; otherwise restart
			dNode<K,V> dNow = root.pNode.getReference();
			if(dRoot == dNow){
				dNode<K,V> dCopy = new dNode<K,V>(null,null);
				dCopy.clone(dRoot);
				dCopy.operation.set(opData);
				if(root.pNode.compareAndSet(dRoot, dCopy, dNode.FREE, dNode.OWNED)){
					opData.state.compareAndSet(root, root,opNode.WAITING, opNode.IN_PROGRESS);
				}
			}
		}
	}
	
	protected void executeWindowTransaction(AtomicStampedReference<dNode<K,V>> pNode,dNode<K,V> dNode){
		// execute a window transaction for the operation stored in dNode
		opNode<K,V> opData = dNode.operation.get();
		// read the contents of pNode again
		int[] flag = new int[1];
		dNode<K,V> dCurrent = pNode.get(flag);
		if(dCurrent.operation.get() == opData){
			if(flag[0] == lockfree.dNode.OWNED){
				if(pNode == root.pNode){
					// the operation may have just been injected into the tree, but
					// the operation state may not have been updated yet; update the state
					//opData.state.compareAndSet(root, root, , newStamp)
				}
			}
		}
	}
}
