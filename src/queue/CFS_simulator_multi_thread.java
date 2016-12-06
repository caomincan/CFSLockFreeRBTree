/*
 * CFS_simulator_multi_thread.java
 *
 * Created on December 5, 2016, 11:15 PM
 * TASK = 117 1089000
 * THREADS = 4
 * AVL
 * 16335
 * 15403
 * 17287
 * 15647
 * 13809
 * 16779
 * 16760
 * 15562
 * 14409
 * RB
 * 16580
 * 16077
 * 16091
 * 18478
 * 17865
 * 17032
 * 16967
 * 
 * TASK = 117 add 890000
 * THREADS = 3
 * AVL
 * 16752
 * 15126
 * 14940
 * 14474
 * RB
 * 13881
 * 14546
 * 14791
 * 17396
 * 15549
 * 15981
 * 14485
 * 16139
 * 
 * 
 * 
 * good
 * TASK = 170 add 890000
 * THREADS = 3
 * AVL
 * 10940 -
 * RB
 * 10012 -
 * 
 * 
 * TASK = 260 add 980000
 * THREADS = 3
 * AVL
 * 11266
 * 11954
 * 11606
 * 10888
 * 11060
 * 
 * RB
 * 9500
 * 9885
 * 10507
 * 12180
 * 9909
 * 10438
 * 10117
 * 10649
 * 
 * 
 * 
 * TASK =  add 
 * THREADS = 3
 * AVL
 * 
 * 
 * 
 */
package queue;

import tree.*;
import java.io.*;
import java.util.Random;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.io.BufferedReader;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.atomic.AtomicInteger;

public class CFS_simulator_multi_thread<T extends Comparable<T>> {
	static boolean IS_RBTREE = false; 	// RBTree/AVLTree

	/* default values */ /* unit=us */
	static int THREADS = 3; 					// number of workers (simulated CPUs, not task!!!!!!!!!!!)
	static int TimerIntThreshold = 1000*1000;	// timer interrupt ticks 1ms
	static int min_granunarity = 1000*1000;		// minimum granularity // 1ms
	static int dynaic_nice_rang = 5;			// nice(dynamic) = original_nice +-dynaic_nice_rang
	static int how_many_int = 10*1000; 			// periodically debug
	
	static int TASK = -1; 				// global number of tasks (threads) assigned by in.txt
	static int g_time;					// global time
  	static AtomicInteger[] finished_order_queue; // check finishing order // Be careful id is from 1~Task
	static AtomicInteger g_queue_thread_num = new AtomicInteger(0);		// global number of threads in run_queue
	static AtomicInteger g_done_thread_num = new AtomicInteger(0);		// global number of threads done
    
	static boolean DEBUG = false;		
	static boolean TEST1 = false;		// concurrent addition test
	static boolean TEST2 = false;		// concurrent deletion test
	static boolean g_done = false;		
	static boolean g_is_interrupted = false;

	static Task[] task; 			// all tasks going to run in this simulation
  	public static Task[] run_queue; // tasks should be ran
  	static Tree<Task> instance=null;
	private static Random random = new Random(); 
	
	public CFS_simulator_multi_thread(String testName, int thread, int duration, int n, int ops) {
		g_time = 0;
	}
	
	public static void main(String[] args) throws Exception {
	  	int i;

	  	/* Initialization */
	  	if (IS_RBTREE==false) {
		  	//Tree<Task> instance = new AVL<Task>(); 			// Wrong 
		  	//Tree<Task> instance = new AvlTree<Task>(); 		// Wrong
		  	instance = new AVLTree2<Task>();					// Correct AVL tree
	  		System.out.println("AVLtree:");
	  	}
	  	else {
	  		instance = new RBTree<Task>();
	  		System.out.println("RBtree:");
	  	}
		ReentrantLock lock = new ReentrantLock();
		Hashtable<String, String> htable = new Hashtable<>();
		
		/* determine how many threads */
	  	TASK = read_file_lines();
	  	if(TASK<=0) {
	  		System.out.println("0 thread created");
	  		return;
	  	}
	  	
		System.out.println("TASK = " + TASK);
		System.out.println("THREADS = " + THREADS);
	  	
		//  Create THREADS threads(CPUs)
		task = new Task[TASK];					// all tasks in this simulation
	  	for(i=0; i<TASK; i++) {
	  		task[i] = new Task();
	  	}
	  	
		//  Create an array for recording threads finished order
	  	finished_order_queue = new AtomicInteger[TASK+1]; // check finishing order // Be careful id is from 1~Task
	  	for(i=1; i<TASK+1; i++) {
	  		finished_order_queue[i] = new AtomicInteger(0);
	  	}
	  	finished_order_queue[0] = new AtomicInteger(0); // 0 records how many threads done

	  	// initialize threads (task[i])
	  	read_file_lines2();
	  			
if(DEBUG){  	
		System.out.print("id" + "\t");
		System.out.print("cpu" + "\t");
		System.out.print("io" + "\t");
		System.out.print("prio" + "\t");
		System.out.print("nice" + "\t");
		System.out.print("start_time" + "\t");
		System.out.println("");
	  	for(i=0; i<TASK; i++) {
	  		// read from txt
	  		System.out.print(task[i].id + "\t");
	  		System.out.print(task[i].cpu + "\t");
	  		System.out.print(task[i].io + "\t");
	  		System.out.print(task[i].prio + "\t");
	  		System.out.print(task[i].nice + "\t");
	  		System.out.print(task[i].start_time + "\t");
	  		System.out.println("");
		}
}

		/* test 1. concurrent addition */
if(TEST1){
		ReentrantLock lock1 = new ReentrantLock();
		Tree<Task> instance1 = new RBTree<Task>();
		Hashtable<String, String> htable1 = new Hashtable<>();
		Thread[] myThreads1 = new Thread[THREADS];
		int add_num = 24;
		for (i = 0; i < THREADS; i++) {
			myThreads1[i] = new ADDThread(i, instance1, htable1, lock1, add_num); 
		}
		for (i = 0; i < THREADS; i ++) {
			myThreads1[i].start();
		}
		for (i = 0; i < THREADS; i ++) {
	    	myThreads1[i].join();
		}
		System.out.println("THREADS=" + THREADS + "\tadd_num=" + add_num);
		instance1.print();

		Thread.sleep(5*1000);
		for (i = 0; i < 5; i++) {
			System.out.println(""); 
		}
}

		/* test 2. concurrent deletion */
if(TEST2){
		ReentrantLock lock2 = new ReentrantLock();
		Tree<Task> instance2 = new RBTree<Task>();
		Hashtable<String, String> htable2 = new Hashtable<>();
		
		int del_num = 24;
		for (i = 0; i < del_num; i++) { // sequentially add for deletion
			Task _task = new Task(); 
			_task.id=i; _task.VirtualRunTime=i;
			push_to_rbtree(_task, instance2, lock2, htable2);
		}
		Thread[] myThreads2 = new Thread[THREADS];
		for (i = 0; i < THREADS; i++) {
			myThreads2[i] = new DELThread(i, instance2, htable2, lock2, del_num);
		}
		for (i = 0; i < THREADS; i ++) {
			myThreads2[i].start();
		}
		for (i = 0; i < THREADS; i ++) {
	    	myThreads2[i].join();
		}
		instance2.print();
		
		Thread.sleep(5*1000);
}

		/* main */
		g_queue_thread_num.set(0);
		// Every task will be executed in the threads
		// When a thread is done/out of time slice, it enq()/deq() global queue.
		Thread[] myThreads = new Thread[THREADS];
	    for (i = 0; i < THREADS; i++) {
	    	myThreads[i] = new CPUThread(i, instance, htable, lock); 
	    }
	    for (i = 0; i < THREADS; i ++) {
	    	myThreads[i].start();
	    }
	    long start_time = System.currentTimeMillis();
	  	/* main while(1) thread keeps checks all task start time */
		/* main while(1) thread only check whether should I place a Task from pool to the run_queue(rbtree) */
		/* mimicking external interrupt with polling*/
		while(true) { // infinite loop until every work is done
			g_time++;

			if(DEBUG){
			if(g_time>0 && TimerIntThreshold>0 && g_time>TimerIntThreshold) {
				if (how_many_int/(g_time/TimerIntThreshold)==0) {
					System.out.println("c" + how_many_int/(g_time/TimerIntThreshold));
					System.out.println("--------------------------------------------");
					System.out.println("g_time=" + g_time);
					System.out.println("TASK=" + TASK);		// main() is the only one access TASK table
					System.out.println("g_queue_thread_num=" + g_queue_thread_num.get());
					System.out.println("g_done_thread_num=" + g_done_thread_num.get());
				}
			}
}	

			/* check any thread should set to run_queue */
			for(i=0; i<TASK; i++) { // check any thread ready to run
				//System.out.println("task[i].id=" + task[i].id);
				if( task[i].id>0 && (task[i].start_time >= (g_time-1)) ) {  // if so, put it to run_queue					
					Task _task = new Task();		// redundant?
					thread_copy(_task, task[i]);	// redundant?
					
					// 1. enqueue() to run_queue
					push_to_rbtree(_task, instance, lock, htable);
					// 2. kill the task in task[] (task table)
					thread_clean(task[i]);	// remove from task table
if(DEBUG){					
					System.out.println("queue_num = " + g_queue_thread_num.get() + "\t" + 
										"done_num = " + g_done_thread_num.get());
}
				}
			}

			if(g_done_thread_num.get()==TASK) // all TASK are done
				break;
			
		} // while(1) end
		
		for (i = 0; i < THREADS; i ++) {
	    	myThreads[i].join();
	    }
		
		long end_time = System.currentTimeMillis();
		
		if (instance.leftMost()!=null)
			System.out.println("ERROR: tasks not done");
		else // ==null
			System.out.println("[Good]: tasks are all done");
		
		System.out.println("--------------------parameters------------------------");
		System.out.println("TASK = " + TASK + ",\t" + "THREADS = " + THREADS); // number of workers & simulated CPUs
		System.out.println("TimerIntThreshold = " + TimerIntThreshold + ",\t" + "min_granunarity = " + min_granunarity); /// minimum granularity // 1ms
		System.out.println("dynaic_nice_rang = " + dynaic_nice_rang); // nice(dynamic) = original_nice +-dynaic_nice_rang	
		System.out.println("------------------------------------------------------");
		if (IS_RBTREE==false) {
	  		System.out.println("AVLtree:");
	  	}
	  	else {
	  		System.out.println("RBtree:");
	  	}
		System.out.println("g_queue_thread_num = " + g_queue_thread_num.get() + ",\t" + "g_done_thread_num = " + g_done_thread_num.get());
		System.out.println("g_time = " + g_time/1000 + " ms (system virtual ticks)");
		System.out.println("g_time = " + g_time/1000/1000 + " s (system virtual ticks)");
		
		System.out.print("Finished order: ");
		for(i=1; i<TASK+1; i++) {
			System.out.print(finished_order_queue[i].intValue() + " ");		
		}
		System.out.println("");
		System.out.println( "Total execution time = " + (end_time - start_time) + " ms (time in reality)");
		System.out.println( "Total execution time = " + (end_time - start_time)/1000 + " s (time in reality)");
		
		//System.out.println( "add happens " + ((108*10000)+ (9*1000)) );
		System.out.println( "add happens " + ((80*10000)+ (2*90*1000)) );
	}

	/* adjust_Vtime() are all embedded in push_to_tree(). Automatically done. */
	private static synchronized void adjust_Vtime(Task _task, Hashtable<String, String> _htable) {
		while (true) {
			if (_htable.get(_task.VirtualRunTime.toString()) == null){ // new key
				_htable.put(_task.VirtualRunTime.toString(),_task.VirtualRunTime.toString());
				_task.VirtualRunTime= new Integer(_task.VirtualRunTime.intValue());
				break;
			}else { //repeat key
				_task.VirtualRunTime = new Integer(_task.VirtualRunTime.intValue() + 1); // adjust, ++	
				continue;
			}
		}
	}

	private static int read_file_lines() {
		int line_num = 0; 
		String fileName ="in.txt";
		File currentDir = new File("").getAbsoluteFile();
		System.out.println("Find input file in \"" + currentDir + "\"");
		String line;
		try {
			// use BufferedReader and apply on FileReader. process streaming apply on node streaming
			BufferedReader in = new BufferedReader(new FileReader(currentDir+"/"+fileName));

			// read the first line 
			line = in.readLine();
			line = in.readLine();
			while(line!=null)
			{
				String delims_space = "[ \t]+"; //target: "the it   hard        concentrate";
				String[] tokens = line.split(delims_space);
				String[] args = new String[100];
				for (int i = 1; i < tokens.length; i++) {
					args[i] = tokens[i];
				}
				
				//just count thread numbers
				if(Integer.parseInt(tokens[1])>0)
					line_num += Integer.parseInt(tokens[1]);
				//System.out.println("-->num " + Integer.parseInt(tokens[1]) +  "\t");

				line = in.readLine();	// read the next line
			} //file ends 
			in.close();	//close the "pipe"
		
		} catch(IOException iox) {
			System.out.println("FILE_NOT_FOUND: ERROR cannot open file " + fileName);
		}

		return line_num;
	}

	private static int read_file_lines2() {
		int i;
		int line_num = 0;
		int old_line_num = 0;

		String fileName ="in.txt";
		File currentDir = new File("").getAbsoluteFile();
		//System.out.println(currentDir);
		String line;
		try {
			// use BufferedReader and apply on FileReader. process streaming apply on node streaming
			BufferedReader in = new BufferedReader(new FileReader(currentDir+"/"+fileName));

			System.out.print ("Task id =");
			// read the first line 
			line = in.readLine(); // ignore first line
			line = in.readLine();
			while(line!=null)
			{
				String delims_space = "[ \t]+"; //target: "the it   hard        concentrate";
				String[] tokens = line.split(delims_space);
				String[] args = new String[100];
				for (i=1; i < tokens.length; i++) {
					//System.out.println(tokens[i]);
					args[i] = tokens[i];
				}

				old_line_num = line_num;
				// count # of threads
				if(Integer.parseInt(tokens[1])>0)
					line_num += Integer.parseInt(tokens[1]);

				 for(i=old_line_num; i<line_num; i++) {
					// line_num++;
			  		// read from txt
					task[i].id = i+1; // from 0 to Task
					System.out.print ((i+1) + " ");
					//= Integer.parseInt(tokens[1]);					// number of thread
					task[i].cpu = Integer.parseInt(tokens[2])*1000;		// cpu (ms) need to *1000
					task[i].io = Integer.parseInt(tokens[3])*1000;		// io (ms) need to * 1000
					task[i].prio = Integer.parseInt(tokens[4]);			// prio
					task[i].nice = Integer.parseInt(tokens[5]);			// nice 
					task[i].ori_nice = Integer.parseInt(tokens[5]);		// ori_nice 
					task[i].start_time = Integer.parseInt(tokens[6])*1000;	// start_time (used for interrupt or mimicing preemptive tasks) 
					
					task[i].VirtualRunTime = new Integer(0); 
				  	task[i].time_slice = 0;
				  	task[i].weight = 0;
				}
				// read the next line
				line = in.readLine();
			} //file ends 
			System.out.println("");
			in.close(); //close the "pipe"
		
		} catch(IOException iox) {
			System.out.println("FILE_NOT_FOUND: ERROR cannot open file " + fileName);
		}
		
		return line_num;
	}
	
	private static void push_to_rbtree(Task _task, Tree<Task> instance, ReentrantLock lock, Hashtable<String, String> _htable) {	
		lock.lock();  // block until condition holds
	    try {
	    	adjust_Vtime(_task, _htable);
	    	instance.add(_task); // must succeed
	    	g_queue_thread_num.getAndIncrement();
			//System.out.println("height"+instance.height());
	    	//instance.print();
	    } finally {
	    	lock.unlock();
	    }
	    //instance.print();
	}
	
	public static void kill_from_rbtree(Task _task, Tree<Task> instance, ReentrantLock lock) {
		lock.lock();  // block until condition holds
	    try {
			instance.remove(_task);
			g_done_thread_num.getAndIncrement();
	    } finally {
	    	lock.unlock();
	    }
	}
	
	public static Task pop_from_rbtree(Tree<Task> instance, ReentrantLock lock) {
		Task _task;
		//= new Task();
		lock.lock();  // block until condition holds
	    try {
	    	//instance.print();
	    	_task = instance.leftMost();
			if(_task==null)
				return null;
			else {
				Task temp_task;
				temp_task = instance.remove(_task);
				if (temp_task == null) 
					System.out.println("ERROE: remove failed !!!!!!!!!!!!!!!!!!!");
				g_queue_thread_num.getAndDecrement();
				return _task;
			}
	    } finally {
	    	lock.unlock();
	    }
	}

	public static boolean JobTask(Task task, int virtualtime) { 	/* a thread, a task */
		int rand=-1; // randomly finish jobs (io/cpu)
		if(task.cpu<=0 && task.io<=0) {
			// PROBLEM: time was up but didn't report
			return true;
		}
		else if(task.cpu<=0) { // do io
			task.io--;
			task.io_runtime++;
			//record the weight --
			return false;
		}
		else if(task.io<=0) { // do cpu
			task.cpu--;
			task.cpu_runtime++;
			//record the weight ++
			return false;
		}
		else { // task.cpu>0 && task.io>0
			rand=random.nextInt(2);	// random for choose io or cpu bound task
			if (rand==1) { // do cpu
				task.cpu--;
				task.cpu_runtime++;
				//record the weight ++
			}
			else if (rand==0){ // do io
				task.io--;
				task.io_runtime++;
				//record the weight --
			}
			else { System.out.println("ERROR: rand()"); } //wrong
		}

		if(task.cpu<=0 && task.io<=0) // check should be interrupt?
			return true;
		else 
			return false;
    }
	
	private static void thread_copy(Task task1, Task task2) {
		task1.id = task2.id;
		task1.io = task2.io;
		task1.cpu = task2.cpu;
		task1.prio = task2.prio;		
		task1.nice = task2.nice;
		task1.ori_nice = task2.ori_nice;
		task1.VirtualRunTime = new Integer(task2.VirtualRunTime.intValue()); 
		
		task1.time_slice = task2.time_slice; 		
		task1.weight = task2.weight;
		task1.start_time = task2.start_time;

		task1.io_runtime = task2.io_runtime;
		task1.cpu_runtime = task2.cpu_runtime;
	}

	private static void thread_clean(Task task1) {
		task1.id = 0;
	}
	
	
	static class ADDThread extends Thread {
		private volatile int id=-1;  
		int t_time=0; // thread run time
		private Hashtable<String, String> _htable;
		private Tree<Task> instance;
		boolean is_exit=false;
		private ReentrantLock _lock;
		volatile boolean reschedule=true;
		int add_num;
		
		public ADDThread(int i, Tree<Task> tree, Hashtable<String, String> htable, ReentrantLock lock, int _add_num) {
			id = i;
			instance=tree;
			_htable=htable;
			_lock=lock;
			add_num = _add_num;
		}
	
		public void run() {
			int i=0;
			try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
			int base = (id++ * add_num/THREADS);
			for (i=base; i<base+(add_num/THREADS) ; i++) {
				Task _task = new Task();
				_task.id =i;
				_task.VirtualRunTime=id;
				push_to_rbtree(_task, instance, _lock, _htable);
				System.out.println("test1: inserting id=" + _task.id);
			}

		}
	}
		
	static class DELThread extends Thread {
		private volatile int id=-1;  
		int t_time=0; // thread run time
		private Tree<Task> instance;
		boolean is_exit=false;
		private ReentrantLock _lock;
		volatile boolean reschedule=true;
		int del_num;
		
		public DELThread(int i, Tree<Task> tree, Hashtable<String, String> htable, ReentrantLock lock, int _del_num) {
			id = i;
			instance=tree;
			_lock=lock;
			del_num = _del_num;
		}
	
		public void run() {
			int i=0;
			try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }
			int base = (id++ * del_num/THREADS);
			for (i=base; i<base+(del_num/THREADS) ; i++) {
				Task _task;
				_task = pop_from_rbtree(instance, _lock);
				System.out.println("i=" + i+ "\ttest2: deleting id=" + _task.id);
			}
		}
	}
	
	static class CPUThread extends Thread {
		private volatile int TotalDeq=0; //not used
		private volatile int GoodDeq=0;
		private volatile int id=-1;  
		int t_time=0; // thread run time
		private Hashtable<String, String> _htable;
		private Tree<Task> instance;
		boolean is_exit=false;
		private ReentrantLock _lock;
		private Random random = new Random();
		volatile boolean reschedule=true;
		
		public CPUThread(int i, Tree<Task> tree, Hashtable<String, String> htable, ReentrantLock lock) {
			id = i;
			instance=tree;
			_htable=htable;
			_lock=lock;
		}
		
		public int getrand(int tmp) {
			return random.nextInt(tmp);
		}

		public void run() {
			Task curr_task = null;
			CPUThread currThread = (CPUThread) CPUThread.currentThread();
			// try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }
			while(true) {
				if(reschedule==true) {
					curr_task = pop_from_rbtree(instance, _lock);
					if (curr_task==null) {
						if (g_done_thread_num.get() == TASK ) 
							break;
						continue;	// nothing in the run_queue(tree)
					}
				}
				reschedule=true;
if(DEBUG){				
				System.out.println("Thread_id = " + currThread.id + ", Task_id = " + curr_task.id);
}
				/* Load a new task to run */
				// sched2. - recalculate time_slice 
				if(curr_task.nice==0) // In the formula I use, nice=0 is wrong (cannot support nice=0)
					curr_task.nice=1;
				curr_task.time_slice = (int) ((1*1000) * (float)(curr_task.nice / (1024 / Math.pow(1.25, curr_task.nice))));
				
				if (curr_task.time_slice <= min_granunarity)
					curr_task.time_slice=min_granunarity;

				// sched2. - clean(initialize) all runtime info
				curr_task.cpu_runtime=0;	// run_time record used for dynamic priority
				curr_task.io_runtime=0; 	// run_time record used for dynamic priority
				
				t_time=0;					// initialize thread timer
				is_exit=false; 				// clear exit flag
				if ( ((curr_task.cpu+curr_task.io) <= 0) ) {
					System.out.println("ERROR: finished before runing");
				}
				do {
						t_time++;
						is_exit = JobTask(curr_task, 0); 
					if (t_time > TimerIntThreshold || is_exit==true) // case1 + case2
						break;
				}while(true);
				
				// kernel space - context switch:
				// case 1. exit()
				if(is_exit==true) { // feature - exit() interrupt			
					if ( ((curr_task.cpu+curr_task.io) <= 0) ) {	// task done			  			
			  			/* clean runtime info to record for the next run */
			  			kill_from_rbtree(curr_task, instance, _lock);
if(DEBUG){						
			  			System.out.println("Thread_id = " + currThread.id + ", task done =" + curr_task.id);
			  			System.out.println("queue_num = " + g_queue_thread_num.get() + "\t" + 
			  								"done_num = " + g_done_thread_num.get() + "\t" +
			  								"done id = " + curr_task.id );
}
						finished_order_queue[finished_order_queue[0].incrementAndGet()].set(curr_task.id);
			  			reschedule=true;
					}
				}
				else if (t_time > TimerIntThreshold) { // feature - timer interrupt
					// case 1. Job not done BUT time_slice is out. recycle(reclaim).
					if ( curr_task.time_slice <= (curr_task.cpu_runtime+curr_task.io_runtime) ) { // time_slice expired must deq()	
			  			// sched1. update Virtual Time - virtual += time_slice (before push)
			  			int temp_int=0;
			  			temp_int += curr_task.VirtualRunTime.intValue();
			  			temp_int += curr_task.cpu_runtime+curr_task.io_runtime; // + actual run time NOT time_slice 
						curr_task.VirtualRunTime = new Integer(temp_int); 
			  			
			  			// sched1. update nice
			  			if (curr_task.io_runtime*2 > curr_task.cpu_runtime) {
			  				curr_task.nice++;
							if (curr_task.nice > curr_task.ori_nice+dynaic_nice_rang)
								curr_task.nice = curr_task.ori_nice+dynaic_nice_rang;
						}
						if (curr_task.io_runtime*2 < curr_task.cpu_runtime) {
							curr_task.nice--;
							if (curr_task.nice < curr_task.ori_nice-dynaic_nice_rang)
								curr_task.nice = curr_task.ori_nice-dynaic_nice_rang;
						}
if(DEBUG){						
						System.out.println("Thread_id = " + currThread.id + ", slice out: inserting id=" + curr_task.id + 
											"\t left cpu=" + curr_task.cpu + ", left io=" + curr_task.io );
}
						push_to_rbtree(curr_task, instance, _lock, _htable);
						reschedule=true;
			  		}  // expired end
					else {
						reschedule=false;
						System.out.println("ERROR: shouldn't be here !!!!!!!!!!!!!!");
					}
				}
				else { // case 3. keep occupying the CPU 
					reschedule=false;
					// time_slice remains, keep running}
				} // kernel ends
				if (g_done_thread_num.get()==TASK)
					break;
			} //while end
			System.out.println("Thread_id = " + currThread.id + " DONE");
		}
		public int GetTotalDeq() {
		      return TotalDeq;  
		}
		public int GetGoodDeq() {
		      return GoodDeq;  
		}
	}
}

/** example code - hashtable
Task __task = new Task();
__task.VirtualRunTime= new Integer(10); // set value
htable.put(__task.VirtualRunTime.toString(), __task.VirtualRunTime.toString());
__task.VirtualRunTime = new Integer(__task.VirtualRunTime.intValue() + 1); // ++
htable.put(__task.VirtualRunTime.toString(), __task.VirtualRunTime.toString());

__task.VirtualRunTime.intValue(); //get
System.out.println("int = " + __task.VirtualRunTime.intValue()); 

htable.put(__task.VirtualRunTime.toString(), __task.VirtualRunTime.toString());

System.out.println("ok key=" + htable.get(__task.VirtualRunTime.toString())); 
System.out.println("bad key=" + htable.get("123213")); 
*/	

/** example code - tree operation
Task _task = new Task();

_task.VirtualRunTime=12;
int inserted_runtime = VirtualRunTime_update(_task, 12);
instance.add(_task);
// don't change it

Task p_task;
p_task = instance.get_leftmost();
//p_task = instance.get_leftmost();
//
if (instance.remove(p_task) !=null) {
	//do
}
else {
	System.out.println("ERROR: cannot remove from tree");
}	
p_task.VirtualRunTime = 100;
*/
