package test;

import java.util.Random;
import java.util.concurrent.locks.Lock;

import tree.Tree;

public class searchThread extends Thread{
	public static int ID_GEN =100 ;
	
	public int id;
	private Tree<Integer> tree;
	private int num;
	private int nodes;
	private Lock lock = null;
	
	public searchThread(Tree<Integer> tree,int num,int nodes){
		this.id = ID_GEN++;
		this.tree = tree;
		this.num = num;
		this.nodes = nodes;
		this.lock = null;
	}
	
	public searchThread(Tree<Integer> tree,int num,int nodes,Lock lock){
		this.id = ID_GEN++;
		this.tree = tree;
		this.num = num;
		this.nodes = nodes;
		this.lock = lock;
	}
	
	@Override
	public void run(){
		Random rand = new Random();
		for(int i=0;i<1000;i++){
			if(lock != null) lock.lock();
			Integer target = rand.nextInt(nodes*num);
			//System.out.println("Thread "+id+" search "+target);
			Integer result = this.tree.search(target);
			if(lock != null) lock.unlock();
			//if(result == null) System.out.println("Search fail:  "+target);
			//else System.out.println("Search found: "+result);
			//this.tree.print();
		}
		//System.out.println("Thread "+id+": finished");
		//this.tree.print();
		//System.out.println("");
	}
}
