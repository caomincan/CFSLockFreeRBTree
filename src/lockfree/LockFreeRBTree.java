package lockfree;

import java.util.LinkedList;
import java.util.List;

public class LockFreeRBTree<V extends Comparable<V>> {
	int size = 0;
	LockFreeRBNode<V> root;
	
	public LockFreeRBTree(){
		this.root = null;
	}
	
	// find the value if not in the tree return null
	public LockFreeRBNode<V> find(V value){
		if(root == null) return null;
		LockFreeRBNode<V> temp = root;
		while(temp != null && temp.value != null){
			if(value.compareTo(temp.value)<0){
				temp = temp.left;
			}else if(value.compareTo(temp.value) >0){
				temp = temp.right;
			} else{
				return temp;
			}
		}
		return temp;
	}
	public void add(V value){
		LockFreeRBNode<V> z = null;
		LockFreeRBNode<V> y = this.root;
		while(y!= null && y.value != null){
			z = y;
			if(value.compareTo(y.value)<0){
				y = y.left;
			}else{
				y =y.right;
			}
		}// end while
		// place new node x as child of z
		LockFreeRBNode<V> x = new LockFreeRBNode<V>(value);
		x.parent = z;
		if(z == null) { this.root = x; }
		else if(value.compareTo(z.value)<0){
			z.left = x;
		}else{
			z.right = x;
		}
		x.left = new LockFreeRBNode<V>();
		x.right = new LockFreeRBNode<V>();
		x.isRed = true;
		insertHelp(x);
		
	}
	
	
	// insert fix up
	protected void insertHelp(LockFreeRBNode<V> x){
		LockFreeRBNode<V> y;
		while(x.parent!= null && x.parent.isRed){
			if(x.parent == x.parent.parent.left){
				y = x.parent.parent.right;
				if(y.isRed){
					// Case 1
					x.parent.isRed = false;
					y.isRed = false;
					x.parent.parent.isRed = true;
					x = x.parent.parent;
				}else{
					if(x == x.parent.right){
						// Case 2
						x = x.parent;
						leftRotate(x);
					}// end if
					// Case 3
					x.parent.isRed =false;
					x.parent.parent.isRed = true;
					rightRotate(x.parent.parent);
				}
			}else{
				y = x.parent.parent.left;
				if(y.isRed){
					// Case 1
					x.parent.isRed = false;
					y.isRed = false;
					x.parent.parent.isRed = true;
					x = x.parent.parent;
				}else{
					if(x == x.parent.left){
						// Case 2
						x = x.parent;
						rightRotate(x);
					}// end if
					// Case 3
					x.parent.isRed =false;
					x.parent.parent.isRed = true;
					leftRotate(x.parent.parent);
				}
			}
		}// end while
		this.root.isRed = false;
	}
	
	public V remove(V value){
		LockFreeRBNode<V> curr = find(value);
		if(curr == null || curr.value == null) return null;
     	LockFreeRBNode<V> x,y;
		if(curr.left.value == null || curr.right.value == null){
			y = curr;
		}else{
			y = findMin(curr.right);
		}
		// y have left child 
		if(y.left.value != null) x = y.left;
		else x = y.right;
		// exchange the parent
		x.parent = y.parent;
		if(y.parent ==  null){
			root = x;
		}else{
			if(y == y.parent.left) y.parent.left =x;
			else y.parent.right = x;
		}
		
		if(y != curr) curr.value = y.value;
		if(!y.isRed){
			// Delete fix up
			deleteHelp(x);
		}
		return curr.value;
	}
	
	protected LockFreeRBNode<V> findMin(LockFreeRBNode<V> node){
		if(node == null) return null;
		LockFreeRBNode<V> temp = node;
		while(temp.left.value!= null){
			temp = temp.left;
		}
		return temp;
	}
	
	protected void deleteHelp(LockFreeRBNode<V> x){
		LockFreeRBNode<V> w;
		while(x != root && !x.isRed){
			if(x == x.parent.left){
				// is left child
				w = x.parent.right;
				if(w.isRed){
					//case 1
					w.isRed = false;
					x.parent.isRed = true;
					leftRotate(x.parent);
					w = x.parent.right;
				}
				if((!w.left.isRed) && (!w.right.isRed)){
					//case 2
					w.isRed = true;
					x = x.parent;
				}else{
					if(!w.right.isRed){
						//case 3
						w.left.isRed = false;
						w.isRed = true;
						rightRotate(w);
						w = x.parent.right;
					}
					w.isRed = x.parent.isRed; // case 4
					x.parent.isRed = false;
					w.right.isRed = false;
					leftRotate(x.parent);
					x= root;
				}
			}else{
				// is right child
				w = x.parent.left;
				if(w.isRed){
					//case 1
					w.isRed = false;
					x.parent.isRed = true;
					rightRotate(x.parent);
					w = x.parent.left;
				}
				if((!w.left.isRed) && (!w.right.isRed)){
					//case 2
					w.isRed = true;
					x = x.parent;
				}else{
					if(!w.left.isRed){
						//case 3
						w.right.isRed = false;
						w.isRed = true;
						leftRotate(w);
						w = x.parent.left;
					}
					w.isRed = x.parent.isRed; // case 4
					x.parent.isRed = false;
					w.right.isRed = false;
					rightRotate(x.parent);
					x= root;
				}
			}
		}
		x.isRed = false;
	}
	protected void leftRotate(LockFreeRBNode<V> x){
		if(x == null) return;
		LockFreeRBNode<V> y = x.right;
		// Turn y's left sub-tree into x's right sub-tree
		x.right = y.left;
		if(y.left != null){
			y.left.parent = x;
		}
		// y's new parent was x's parent
		y.parent = x.parent;
		// Set the parent to point to y instead of x
		// First see whether we are at the root
		if(x.parent == null) this.root = y;
		else{
			if(x == x.parent.left)
				x.parent.left  = y;
			else
				x.parent.right = y;
		}
		// Finally, put x on y's left
		y.left = x;
		x.parent = y;
	}
	// symmetric to leftRotate 
	protected void rightRotate(LockFreeRBNode<V> y){
		if(y == null) return;
		LockFreeRBNode<V> x = y.left;
		y.left = x.right;
		if(x.right != null){
			x.right.parent = y;
		}
		x.parent = y.parent;
		if(y.parent == null) this.root = x;
		else{
			if(y == y.parent.left)
				y.parent.left = x;
			else
				x.parent.right = y;
		}
		x.right = y;
		y.parent = x;
	}
	
	public void print(){
		List<List<String>> res = new LinkedList<List<String>>();
		res = printHelp(root,0,res);
		for(List<String> list:res){
			for(String word: list){
				System.out.print(word+" ");
			}
			System.out.print("\n");
		}
	}
	
	protected List<List<String>> printHelp(LockFreeRBNode<V> root,int height,List<List<String>> res){
		if(root == null) return res;
		List<String> list;
		if(height >= res.size()){
			list = new LinkedList<String>();
			res.add(list);
		}else{
			list = res.get(height);
		}
		if(root.value == null){
			list.add(" _ ");
		}else{
			list.add(root.value.toString()+(root.isRed?"_R":"_B"));
		}
		printHelp(root.left,height+1,res);
		printHelp(root.right,height+1,res);
		return res;
	}
} 
