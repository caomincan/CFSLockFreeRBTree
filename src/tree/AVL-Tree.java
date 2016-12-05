package tree;

import java.util.Collection;



public class AVL-Tree<T extends Comparable<T>> {
    
    private Node<T> root;
    private int size;
    
    /**
     * Adds a data entry to the AVL tree
     *
     * @param data The data entry to add
     */
    public void add(T data) {
        if (contains(data)) return;
        Node<T> newNode = new Node<T>(data);
        root = add(root,newNode);
        size++;
    }
    
    private Node<T> add(Node<T> current, Node<T> n){
        if (current == null){
            n.bf = 0;
            n.height = 0;
            return n;
        }
        if (compare(n.data,current.data)>0){
            current.right = rotate(add(current.right,n));
        }
        else{
            current.left = rotate(add(current.left,n));
        }
        current = rotate(current);
        return current;
    }
    
    /**
     * Adds each data entry from the collection to this AVL tree
     *
     * @param c The collection
     */
    public void addAll(Collection<? extends T> c) {
        for(T thing:c){
            add(thing);
        }
    }
    
    /**
     * Removes a data entry from the AVL tree
     *
     * Return null if the value does not exist
     *
     * @param data The data entry to be removed
     * @return The removed data entry
     */
    public T remove(T data) {
        if(!contains(data)){
            return null;
        }
        root = rotate(remove(root,data));
        size--;
        return data;
    }
    private Node<T> remove(Node<T> current, T n){
        
        if (compare(current.data,n)==0){
            if(current.right == null && current.left== null){
                return null;
            }
            else if(current.right == null){
                return rotate(current.left);
            }
            else if(current.left == null){
                return rotate(current.right);
            }
            else{
                Node<T> pre = current.left;
                Node<T> predecessor;
                if (pre.right==null){
                    predecessor = pre;
                    predecessor.right = current.right;
                }
                else{
                    while(pre.right.right!=null){
                        pre = pre.right;
                    }
                    predecessor = pre.right;
                    pre.right = predecessor.left;
                    predecessor.left = current.left;
                    predecessor.right = current.right;
                }
                return predecessor;
            }
        }
        else{
            if (compare(n,current.data)>0){
                current.right = rotate(remove(current.right,n));
            }
            else{
                current.left = rotate(remove(current.left,n));
            }
            return rotate(current);
        }
    }
    /**
     * Checks if the AVL tree contains a data entry
     *
     * @param data The data entry to be checked
     * @return If the data entry is in the AVL tree
     */
    public boolean contains(T data) {
        if (isEmpty())return false;
        return contains(root,data);
    }
    
    private boolean contains(Node<T> current, T n){
        if(current==null)return false;
        if(compare(current.data,n) == 0){
            return true;
        }
        else{
            if(contains(current.right,n)){return true;}
            else if(contains(current.left,n)){return true;}
            return false;
        }
    }
    
    /**
     * Calculates the current height and balance factor for a node and updates the values
     *
     * THIS DOES NOT RECURSIVELY UPDATE N AND ALL OF N'S CHILDREN, ONLY UPDATE N
     * (caps because it's important! Don't kill the running time of everything!)
     *
     * @param n The node whose values are to be calculated and updated
     * @return The node passed in with updated values
     */
    private Node<T> updateHeightAndBF(Node<T> n) {
        
        int left,right;
        left = n.left!=null ? n.left.height : -1;
        right = n.right!=null ? n.right.height : -1;
        n.bf = left-right;
        n.height = (right>left ? right : left) +1;
        return n;
    }
    
    /**
     * Determines what rotation, if any, needs to be performed on a node and does the appropriate rotation
     *
     * @param n The node to potentially be rotated
     * @return The new root of the subtree that is now balanced due to the rotation
     * 			(possibly the same node that was passed in)
     */
    private Node<T> rotate(Node<T> n) {
        if(n == null)return n;
        n = updateHeightAndBF(n);
        if(n.bf<-1){
            if(n.right.bf>0){
                n = rightLeft(n);
            }
            else{
                n = left(n);
            }
        }
        else if(n.bf>1){
            if(n.left.bf<0){
                n = leftRight(n);
            }
            else{
                n = right(n);
            }
        }
        return n;
    }
    
    /**
     * Performs a left rotation on a node
     *
     * @param n The node to have the left rotation performed on
     * @return The new root of the subtree that is now balanced due to the rotation
     */
    private Node<T> left(Node<T> n) {
        Node<T> newRoot = n.right;
        Node<T> temp = n.right.left;
        n.right.left = n;
        n.right = temp;
        n = updateHeightAndBF(n);
        return newRoot;
    }
    
    /**
     * Performs a right rotation on a node
     *
     * @param n The node to have the right rotation performed on
     * @return The new root of the subtree that is now balanced due to the rotation
     */
    private Node<T> right(Node<T> n) {
        Node<T> newRoot = n.left;
        Node<T> temp = n.left.right;
        n.left.right = n;
        n.left = temp;
        n = updateHeightAndBF(n);
        return newRoot;
    }
    
    /**
     * Performs a left right rotation on a node
     *
     * @param n The node to have the left right rotation performed on
     * @return The new root of the subtree that is now balanced due to the rotation
     */
    private Node<T> leftRight(Node<T> n) {
        n.left = left(n.left);
        n = right(n);
        return n;
    }
    
    /**
     * Performs a right left rotation on a node
     *
     * @param n The node to have the right left rotation performed on
     * @return The new root of the subtree that is now balanced due to the rotation
     */
    private Node<T> rightLeft(Node<T> n) {
        n.right = right(n.right);
        n = left(n);
        return n;
    }
    
    /**
     * Checks to see if the AVL tree is empty
     *
     * @return If the AVL tree is empty or not
     */
    public boolean isEmpty() {
        if (size==0) return true;
        return false;
    }
    
    /**
     * Clears this AVL tree
     */
    public void clear() {
        size = 0;
        root = null;
    }
    /**
     * Compare method
     * @param d1 First thing to compare
     * @param d2 Second thing to compare
     * @return Negative int if d1>d2, 0 if equal, Positive if d1<d2
     */
    private int compare(T d1,T d2){
        if (d1==null && d2 == null){
            return 0;
        }
        else if(d1==null){
            return 1;
        }
        else if(d2==null){
            return -1;
        }
        else{
            return d1.compareTo(d2);
        }
    }
    
    
    /*
     * Getters and Setters: Do not modify anything below this point
     */
    
    public Node<T> getRoot() {
        return root;
    }
    
    public void setRoot(Node<T> root) {
        this.root = root;
    }
    
    public int size() {
        return size;
    }
    
    public void setSize(int size) {
        this.size = size;
    }
    
    public static class Node<K extends Comparable<K>> {
        
        private K data;
        private Node<K> left, right;
        private int height;
        private int bf;
        
        public Node(K data) {
            setData(data);
        }
        
        public K getData() {
            return data;
        }
        
        public void setData(K data) {
            this.data = data;
        }
        
        public Node<K> getLeft() {
            return left;
        }
        
        public void setLeft(Node<K> left) {
            this.left = left;
        }
        
        public Node<K> getRight() {
            return right;
        }
        
        public void setRight(Node<K> right) {
            this.right = right;
        }
        
        public int getHeight() {
            return height;
        }
        
        public void setHeight(int height) {
            this.height = height;
        }
        
        public int getBf() {
            return bf;
        }
        
        public void setBf(int bf) {
            this.bf = bf;
        }
    }
}
