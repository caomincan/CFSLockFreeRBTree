package test;

import lockfree.LockFreeRBTree;
import tree.RBTree;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
        LockFreeRBTree<Integer> tree = new LockFreeRBTree<Integer>();
        //RBTree<Integer> tree = new RBTree<Integer>();
        Integer[] list1 = new Integer[]{11,2,14,1,7,5,8,15,4};
        int num = 0;
        for(Integer x: list1){
        	System.out.println("The "+num+"th run:");
        	tree.add(x);
        	tree.print();
        	System.out.println("");
        	num++;
        }
        
        for(Integer x: list1){
        	System.out.println("The "+num+"th run:");
        	tree.remove(x);
        	tree.print();
        	System.out.println("");
        	num++;
        }
        
        for(Integer x: list1){
        	System.out.println("The "+num+"th run:");
        	tree.add(x);
        	tree.print();
        	System.out.println("");
        	num++;
        }
	}

}
