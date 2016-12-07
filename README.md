# CFS Lock-Free RB-tree

1. CFS simulator by (Jack Chuang)
----  

[How to run CFS simulator]  
CFS simulator single-threaded version:  
	CFS_simulator_single_thread.java  
CFS simulator multi-threaded version:  
	CFS_simulator_multi_thread.java  


[Choose one run_queue data structure]  
static boolean IS_RBTREE = false; // run with AVLTree  
static boolean IS_RBTREE = true; // run with RBTree  


[Variables in code]  
static int THREADS = 3; 		    // # of workers (simulated CPUs, not task/jobs)  
static int TimerIntThreshold = 1000*1000;   // timer interrupt ticks = 1ms  
static int min_granunarity = 1000*1000;	    // minimum granularity = 1ms  
static int dynaic_nice_rang = 5;	    //nice(dynamic)=original_nice+-dynaic_nice_rang  


[Variables in input file]  
Assign tasks (jobs) for single-thread simulator:   
	$ vi in_single.txt  
Assign tasks (jobs) for multi-threaded simulator:  
	$ vi in.txt  


##Linux-like CFS schedulor simulator  
![image](https://github.com/caomincan/CFSLockFreeRBTree/raw/master/Linux-like_CFS_Simulator.png)

###Linux-like CFS simulator features:  
###CFS features -
* 	Minimum granularity for a time slice
* 	CPU resource limitation: RLIMIT_NICE   
       e.g. if RLIMIT_NICE = 25, nice can be only 20-25＝-5  
* 	New task’s vrtime is not always 0. 	Linux keep a least number for a runqueue.  
###Scheduler features -   
* 	Timer interrupt: check if delta_exec > time_slice, enq() and then deq()  
* 	thread_exit(): when a thread finishes its jobs  
* 	thread_create() : start_time  



Our contribution:  
----  
• Implementing/modifying AVL-tree & RB-tree  
• Implementing parts of lock-free RB-tree methods  
• Implementing Linux-like CFS simulators (single-threaded and ideal version, multi-threaded and concurrent version)  

References:
----  
AVL-tree:  
	Copyright on https://github.com/steven41292/AVL-Tree/blob/master/AVL.java and Ho-Ren(Jack) Chuang  
 	@author https://github.com/steven41292  
	@modified by Jack Chuang  

