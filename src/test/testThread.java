package test;

import java.util.concurrent.locks.Lock;

import tree.Tree;

public class testThread<V> extends Thread {
	public static int ID_GEN =0 ;
	
	public int id;
	private Tree<V> tree;
	private V[] list;
	private Lock lock;
	
	public testThread(Tree<V> tree, V[] list){
		this.id = ID_GEN++;
		this.tree = tree;
		this.list = list;
		this.lock  = null;
	}
	
	public testThread(Tree<V> tree, V[] list,Lock lock){
		this.id = ID_GEN++;
		this.tree = tree;
		this.list = list;
		this.lock  = lock;
	}
	
	@Override
	public void run(){
		for(V element: list){
			//System.out.println("Thread "+id+" add "+element.toString());
			if(lock != null) lock.lock();
			try{ 
				this.tree.add(element);
			}catch (NullPointerException ne){
				// ignore
			}
			if(lock != null) lock.unlock();
			//this.tree.print();
		}
		//System.out.println("Thread "+id+": finished");
		//this.tree.print();
		//System.out.println("");
	}
}
