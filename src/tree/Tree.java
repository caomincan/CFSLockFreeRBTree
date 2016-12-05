package tree;

public interface Tree<V> {
	public void add(V value);
	public V remove(V value);
	public void print();
	public V search(V value);
}
