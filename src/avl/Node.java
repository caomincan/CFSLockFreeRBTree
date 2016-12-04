/** copyright on https://github.com/GodTamIt/java-iterative-avl-tree
 * This class represents a single item in an AVL tree.
 * @version 1.0
 */

package avl;
import queue.Task;

public class Node<T extends Comparable<T>> {
    private T data;
    private Node<T> left;
    private Node<T> right;
    private int height;
    private int balanceFactor;
    Task task;

    public Node(T d, Task task) {
        data = d; // virtual runtime
        task = new Task();
        task.cpu
    }

    public Node(T d, Node<T> l, Node<T> r) {
        data = d; 	// virtual runtime
        left = l;
        right = r;
    }

    public Task getTask() { //Jack
        return task;
    }
    
    public void setTask(Task data) { //Jack
        this.task = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Node<T> getLeft() {
        return left;
    }

    public void setLeft(Node<T> left) {
        this.left = left;
    }

    public Node<T> getRight() {
        return right;
    }

    public void setRight(Node<T> right) {
        this.right = right;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int h) {
        this.height = h;
    }

    public int getBalanceFactor() {
        return balanceFactor;
    }

    public void setBalanceFactor(int balanceFactor) {
        this.balanceFactor = balanceFactor;
    }
}
