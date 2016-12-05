package test;

import java.util.ArrayList;
import java.util.List;

import tree.LockFreeRBTree;
import tree.RBTree;

public class Test {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
        LockFreeRBTree<Integer> tree = new LockFreeRBTree<Integer>();
        //RBTree<Integer> tree = new RBTree<Integer>();
        
        List<Thread> threads = new ArrayList<Thread>();
        int num_threads = 8;
        for(int i = 0;i<num_threads;i++){
        	Integer[] list = new Integer[20];
        	for(int j = 0;j<20;j++){
        		list[j] = 20*i+j;
        	}
        	if(i == 0) threads.add(new searchThread(tree,num_threads));
        	threads.add(new testThread<Integer>(tree,list));
        }
        
     for(Thread thread: threads){
        	thread.start();
        }
   
        for(Thread thread: threads){
        	thread.join();
        }
        
	}

}
