package test;

import java.util.Random;

import tree.Tree;

public class searchThread extends Thread{
	public static int ID_GEN =100 ;
	
	public int id;
	private Tree<Integer> tree;
	private int num;
	
	public searchThread(Tree<Integer> tree,int num){
		this.id = ID_GEN++;
		this.tree = tree;
		this.num = num;
	}
	
	@Override
	public void run(){
		Random rand = new Random();
		for(int i=0;i<100;i++){
			Integer target = rand.nextInt(20*num);
			//System.out.println("Thread "+id+" search "+target);
			Integer result = this.tree.search(target);
			if(result == null) System.out.println("Search fail:  "+target);
			else System.out.println("Search found: "+result);
			//this.tree.print();
		}
		System.out.println("Thread "+id+": finished");
		//this.tree.print();
		//System.out.println("");
	}
}
