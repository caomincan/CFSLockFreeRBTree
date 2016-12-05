package test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import tree.*;

public class Test {

	public static void main(String[] args) throws InterruptedException {
	   long start= 0,duration = 0;
	   int num_threads = 4;
       int insert_nodes = 20;
       if(args.length == 2){
    	   if(args[0].matches("^[0-9]+$")){
    		   num_threads = Integer.valueOf(args[0]);
    	   }
    	   if(args[1].matches("^[0-9]+$")){
    		   insert_nodes = Integer.valueOf(args[1]);
    	   }
       }
		// TODO Auto-generated method stub
       Tree<Integer> tree = null;
       List<Thread> threads = new ArrayList<Thread>();
       Lock mylock = new ReentrantLock();
       
       threads.clear();
       tree = new RBTree<Integer>();    
       for(int i = 0;i<num_threads;i++){
    	   Integer[] list = new Integer[insert_nodes];
    	   for(int j = 0;j<insert_nodes;j++){
    		   list[j] = insert_nodes*i+j;
            }
        //if(i == 0) threads.add(new searchThread(tree,num_threads));
    	   threads.add(new testThread<Integer>(tree,list,mylock));
        }
       start = System.nanoTime(); 
       for(Thread thread: threads) thread.start();
       for(Thread thread: threads) thread.join();
       duration = System.nanoTime() - start;
       System.out.println("RBTree each thread insert "+insert_nodes+" nodes using " +(double)duration/1000.0 + " us");
       tree.print();
       System.out.println("");
       
       
       threads.clear();
       tree = new LockFreeRBTree<Integer>();
       for(int i = 0;i<num_threads;i++){
    	   Integer[] list = new Integer[insert_nodes];
    	   for(int j = 0;j<insert_nodes;j++){
    		   list[j] = insert_nodes*i+j;
            }
        //if(i == 0) threads.add(new searchThread(tree,num_threads));
    	   threads.add(new testThread<Integer>(tree,list));
        }
       start = System.nanoTime(); 
       for(Thread thread: threads) thread.start();
       for(Thread thread: threads) thread.join();
       duration = System.nanoTime() - start;
       System.out.println("LockFreeRBTree each thread insert "+insert_nodes+" nodes using " +(double)duration/1000.0 + " us");
       tree.print();
       System.out.println("");
	}

}
