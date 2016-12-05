package tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LockFreeRBTree<V extends Comparable<V>> implements Tree<V> {
	int size = 0;
	LockFreeRBNode<V> root;
	
	public LockFreeRBTree(){
		this.root = new LockFreeRBNode<V>();
	}
	
	// find the value if not in the tree return null
	public V search(V value){
		if(root == null) return null;
		LockFreeRBNode<V> temp = root;
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
	public void add(V value){
		LockFreeRBNode<V> y,z;
		LockFreeRBNode<V> x = new LockFreeRBNode<V>(value);
		x.flag.set(true);
		while(true){
			z = null;
			y = this.root;
			while(y!= null && y.value != null){
				z = y;
				if(value.compareTo(y.value)<0){
					y = y.left;
				}else{
					y =y.right;
				}
			}// end while
			if(!SetupLocalAreaForInsert(z)){
				z.flag.set(false); // release help flag
				continue;
			}else{
				break;
			}
		}
		// place new node x as child of z
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
	
	protected boolean SetupLocalAreaForInsert(LockFreeRBNode<V> z){
		if(z == null) return true;
		// try to get flags for rest of local area
		LockFreeRBNode<V> zp = z.parent;
		if(zp == null) return true;
		if(!z.flag.compareAndSet(false, true)){
			return false;
		}
		if(!zp.flag.compareAndSet(false, true)){
			return false;
		}
		if(zp != z.parent){
			zp.flag.set(false);
			return false;
		}
		LockFreeRBNode<V> uncle;
		if(z == z.parent.left)  // uncle is the right child
			uncle = z.parent.right;
		else
			uncle = z.parent.left;
		if(uncle!= null && !uncle.flag.compareAndSet(false, true)){
			z.parent.flag.set(false);
			return false;
		}
		// Now try to get the four intention markers above p[z]
		// The second argument is only useful for deletes so we pass z
		// which is not an ancestor of p[z] and will have no effect
		/*if(!GetFlagsAndMarkersAbove(z.parent,z)){
			z.parent.flag.set(false);
			uncle.flag.set(false);
			return false;
		 */
		return true;
	}
	
	protected LockFreeRBNode<V> MoveInserterUp(LockFreeRBNode<V> oldx, ArrayList<LockFreeRBNode<V>> working){
		LockFreeRBNode<V> oldp = oldx.parent;
		LockFreeRBNode<V> oldgp = oldp.parent;
		LockFreeRBNode<V> olduncle;
		if(oldp == oldgp.left) olduncle = oldgp.right;
		else olduncle = oldgp.left;
		
		LockFreeRBNode<V> newx,newp=null,newgp=null,newuncle=null;
		// get above flag
		newx = oldgp;
		// release old flag
		while(true && newx.parent!= null){
			newp = newx.parent;
			if(!newp.flag.compareAndSet(false, true)){
				continue;
			}
			newgp = newp.parent;
			if(newgp == null) break;
			if(!newgp.flag.compareAndSet(false, true)){
				newp.flag.set(false);
				continue;
			}
			if(newp == newgp.left) newuncle = newgp.right;
			else newuncle = newgp.left;
			if(newuncle != null && !newuncle.flag.compareAndSet(false, true)){
				newgp.flag.set(false);
				newp.flag.set(false);
				continue;
			}
			break;
		}
		// release flag
		working.add(newx);working.add(newp);working.add(newgp);working.add(newuncle);
		// releaseflags
		//oldx.flag.set(false);
		//oldp.flag.set(false);
		//olduncle.flag.set(false);
		return newx;	
	}
	
	// insert fix up
	protected synchronized void insertHelp(LockFreeRBNode<V> x){
		LockFreeRBNode<V> y,uncle = null,xp,gp = null;
		xp = x.parent;
		// Remember which node this thread are working at
		ArrayList<LockFreeRBNode<V>> working = new ArrayList<LockFreeRBNode<V>>();
		// initial working entry
		working.add(x);working.add(xp);
		if(xp != null) gp = xp.parent;	
		if(gp != null){
			if(gp.left == xp) uncle =gp.right;
			else uncle = gp.left;		
		}
		working.add(gp);working.add(uncle);
		while(x.parent!= null && x.parent.isRed){
			xp = x.parent;
			gp = xp.parent;
			if(x.parent == x.parent.parent.left){
				y = x.parent.parent.right;
				uncle = y;
				//if(uncle == null) continue;
				// insert 4pos to working array
				//working.set(0,x);working.set(1,xp);working.set(2,gp);working.set(3,uncle);
				// just add new node
				working.add(x);working.add(xp);working.add(gp);working.add(uncle);
				if(y.isRed){
					// Case 1
					x.parent.isRed = false;
					y.isRed = false;
					x.parent.parent.isRed = true;
					x = MoveInserterUp(x,working);
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
				uncle = y;
				//if(uncle == null) continue;
				// insert 4pos to working array
				//working.set(0,x);working.set(1,xp);working.set(2,gp);working.set(3,uncle);
				// just add new node
				working.add(x);working.add(xp);working.add(gp);working.add(uncle);
				if(y.isRed){
					// Case 1
					x.parent.isRed = false;
					y.isRed = false;
					x.parent.parent.isRed = true;
					x = MoveInserterUp(x,working);
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
			// release flags
		}// end while
		this.root.isRed = false;
		// release flag
		for(LockFreeRBNode<V> node:working){
			if(node!= null) node.flag.set(false);
		}
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
	protected synchronized void leftRotate(LockFreeRBNode<V> x){
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
	protected synchronized void rightRotate(LockFreeRBNode<V> y){
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
				y.parent.right = x;
		}
		x.right = y;
		y.parent = x;
	}
	
	public V leftMost(){
		LockFreeRBNode<V> temp = root;
		if(temp == null || temp.value == null) return null;
		while(temp.left.value!= null){
			temp = temp.left;
		}
		return temp == null? null : temp.value;
	}
	public synchronized void print(){
		List<List<String>> res = new LinkedList<List<String>>();
		res = printHelp(root,0,res);
		@SuppressWarnings("unchecked")
		int id = ((test.testThread<V>)Thread.currentThread()).id;
		System.out.println("Thread "+id+"printing:");
		for(List<String> list:res){
			for(String word: list){
				System.out.print(word+" ");
			}
			System.out.print("\n");
		}
	}
	
	protected synchronized List<List<String>> printHelp(LockFreeRBNode<V> root,int height,List<List<String>> res){
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
			list.add(root.value.toString()+(root.isRed?"_R":"_B")+(root.flag.get()?"T":"F"));
		}
		printHelp(root.left,height+1,res);
		printHelp(root.right,height+1,res);
		return res;
	}
	
	protected LockFreeRBNode<V> find(V value){
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
} 
