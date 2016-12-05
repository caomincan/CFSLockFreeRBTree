package tree;

import java.util.LinkedList;
import java.util.List;

public class RBTree<V extends Comparable<V>> implements Tree<V> {
	int size = 0;
	RBNode<V> root;
	
	public RBTree(){
		this.root = null;
	}
	
	// find the value if not in the tree return null
	public RBNode<V> find(V value){
		if(root == null) return null;
		RBNode<V> temp = root;
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
	
	public synchronized void add(V value){
		RBNode<V> curr = insertHelp(value);
		while(curr != this.root && curr.parent.isRed){
			if(curr.parent == curr.parent.parent.left){
				// if x's parent is a left, y is x's right uncle
				RBNode<V> y = curr.parent.parent.right;
				if(y.isRed){
					//Case 1  change the colors
					curr.parent.isRed = false;
					y.isRed = false;
					curr.parent.parent.isRed = true;
					curr = curr.parent.parent;
				}else{
					if(curr == curr.parent.right){
						// x is to the right
						// case 2 move x up and rotate
						curr = curr.parent;
						leftRotate(curr);
					}
					// case 3
					curr.parent.isRed = false;
					curr.parent.parent.isRed = true;
					rightRotate(curr.parent.parent);
				}
			}else{
				// Repeat the if part with right and left exchanged
				RBNode<V> y = curr.parent.parent.left;
				if(y.isRed){
					//Case 1  change the colors
					curr.parent.isRed = false;
					y.isRed = false;
					curr.parent.parent.isRed = true;
					curr = curr.parent.parent;
				}else{
					if(curr == curr.parent.left){
						// x is to the right
						// case 2 move x up and rotate
						curr = curr.parent;
						rightRotate(curr);
					}
					// case 3
					curr.parent.isRed = false;
					curr.parent.parent.isRed = true;
					leftRotate(curr.parent.parent);
				}
			}
		}
		this.root.isRed = false;
	}
	
	// first insert a node based on BST
	// return null means duplicate value
	// insert success will get current node position
	protected RBNode<V> insertHelp(V value){
		RBNode<V> toBeAdded = new RBNode<V>(value);
		if(root == null || root.value == null){
			root = toBeAdded;
			root.isRed = false;
			toBeAdded.left = new RBNode<V>();
			toBeAdded.right = new RBNode<V>();
			toBeAdded.left.parent = toBeAdded;
			toBeAdded.right.parent = toBeAdded;
			return toBeAdded;
		}
		RBNode<V> temp = root;
		while(temp.value != null){
			if(value.compareTo(temp.value)<0){
				temp = temp.left;
			}else if(value.compareTo(temp.value)>0){
				temp = temp.right;
			}else{
				return null;
			}
		}
		if(temp == temp.parent.left){
			temp.parent.left = toBeAdded;
		}else{
			temp.parent.right = toBeAdded;
		}
		toBeAdded.parent = temp.parent;
		toBeAdded.left = new RBNode<V>();
		toBeAdded.right = new RBNode<V>();
		toBeAdded.left.parent = toBeAdded;
		toBeAdded.right.parent = toBeAdded;
		return toBeAdded;	
	}
	
	public synchronized V remove(V value){
		RBNode<V> curr = find(value);
		if(curr == null || curr.value == null) return null;
     	RBNode<V> x,y;
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
	
	protected RBNode<V> findMin(RBNode<V> node){
		if(node == null) return null;
		RBNode<V> temp = node;
		while(temp.left.value!= null){
			temp = temp.left;
		}
		return temp;
	}
	
	protected void deleteHelp(RBNode<V> x){
		RBNode<V> w;
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
				if(w.left == null || w.right == null) break;
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
				if(w.left == null || w.right == null) break;
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
	protected void leftRotate(RBNode<V> x){
		if(x == null) return;
		RBNode<V> y = x.right;
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
	protected void rightRotate(RBNode<V> y){
		if(y == null) return;
		RBNode<V> x = y.left;
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
		Integer jack;
		List<List<String>> res = new LinkedList<List<String>>();
		res = printHelp(root,0,res,jack);
		for(List<String> list:res){
			for(String word: list){
				System.out.print(word+" ");
			}
			System.out.print(jack.valueOf(i)+"\n");
		}
	}
	
	protected List<List<String>> printHelp(RBNode<V> root,int height,List<List<String>> res, Integer jack){
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
			jack = new Integer(jack.intValue() + 1);
		}
		printHelp(root.left,height+1,res, jack);
		printHelp(root.right,height+1,res, jack);
		return res;
	}

	@Override
	public synchronized V search(V value) {
		if(root == null) return null;
		RBNode<V> temp = root;
		while(temp != null && temp.value != null){
			if(value.compareTo(temp.value)<0){
				temp = temp.left;
			}else if(value.compareTo(temp.value) >0){
				temp = temp.right;
			} else{
				return temp.value;
			}
		}
		return temp==null?null:temp.value;
	}
	
	public V leftMost(){
		RBNode<V> temp = root;
		if(temp == null || temp.value == null) return null;
		while(temp.left.value!= null){
			temp = temp.left;
		}
		return temp == null? null : temp.value;
	}
} 
