package test;

import java.util.List;

import tree.Tree;

public class testThread<V> extends Thread {
	public static int ID_GEN =0 ;
	
	public int id;
	private Tree<V> tree;
	private V[] list;
	
	public testThread(Tree<V> tree, V[] list){
		this.id = ID_GEN++;
		this.tree = tree;
		this.list = list;
	}
	
	@Override
	public void run(){
		for(V element: list){
			//System.out.println("Thread "+id+" add "+element.toString());
			this.tree.add(element);
			try {
				Thread.sleep(3);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//this.tree.print();
		}
		System.out.println("Thread "+id+": finished");
		//this.tree.print();
		//System.out.println("");
	}
}
