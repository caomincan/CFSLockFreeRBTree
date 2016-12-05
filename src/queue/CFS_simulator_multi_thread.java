/*
 * LockFreeQueueTest.java
 * JUnit based test
 *
 * Created on December 27, 2005, 11:15 PM
 */
package queue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import tree.*;

import java.io.*;  

public class CFS_simulator_multi_thread<T extends Comparable<T>> {
	/* default values */ /* unit=us */
	static int THREADS = 8; 					// number of workers (simulated CPUs, not task!!!!!!!!!!!)
	static int TimerIntThreshold = 1000*1000;	// timer interrupt ticks 1ms
	static int min_granunarity = 1000*1000;		// minimum granularity // 1ms
	static int dynaic_nice_rang = 5;			// nice(dynamic) = original_nice +-dynaic_nice_rang
	static int how_many_int=10*1000; 				/* periodically debug */
	
	static int TASK = -1; 				// global number of tasks (threads) assigned by in.txt
	static int g_time;					// global time
	static AtomicInteger g_queue_thread_num = new AtomicInteger(0);		// global number of threads in run_queue
	//static int g_exec_thread_num; 		// global number of executing threads on simulated CPUs
	static AtomicInteger g_done_thread_num = new AtomicInteger(0);		// global number of threads done
    
	static boolean DEBUG = true;			
	static boolean g_done = false;			
	static boolean g_is_interrupted = false;

	static Task[] task; 			// all tasks going to run in this simulation
  	public static Task[] run_queue; // tasks should be ran
  	static Task[] running_tasks; 	// running on simulated CPU
  	static boolean[] done_queue; 	// Be careful id is from 1~Task
  	static Task[] finishing_order_queue;

  	//static int[] Vtime_table; 	
  	//static int Vtime_num; 	
  	//static int Vtime_table_size = 1000000;
  	
	private static Random random = new Random();
	
	public CFS_simulator_multi_thread(String testName, int thread, int duration, int n, int ops) {
		g_time = 0;
	}
	
	public static void main(String[] args) throws Exception {
	  	int i;
	  	//, j, k;
	  	//int left;
	  	boolean is_interrupted[];
	  	//int virtualtime = -1;
	  	//int timer; // timer interrupt cnt
		//int data = 1;
	  	
		ReentrantLock lock = new ReentrantLock();
		lock.lock();  // block until condition holds
	    try {
	    	// ... method body
	    } finally {
	    	lock.unlock();
	    }
	  	
	  	/* dispatch to threads */
		//this.root = new Node<T>(null);
	  	//Tree<Task> instance = new AVL<Task>();
		Tree<Task> instance = new RBTree<Task>();
		Hashtable<String, String> htable = new Hashtable<>();
		
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
		
		/* determine how many threads */
	  	TASK = read_file_lines();
	  	if(TASK<=0) {
	  		System.out.println("0 thread created");
	  		return;
	  	}
	  	
		System.out.println("TASK = " + TASK);
		System.out.println("THREADS = " + THREADS);
	  	
		/*
		 * every task will be executed in the threads
		 * when a thread is done/out of time slice, it enq()/deq() global queue
		 */
		

		//  Create THREADS threads(CPUs)
		task = new Task[TASK];					// all tasks in this simulation
	  	finishing_order_queue = new Task[TASK]; // check finishing order // for record
	  	
	  	is_interrupted = new boolean[THREADS];	
	  	for(i=0; i<THREADS; i++)
	  		is_interrupted[i]=false;
	  	
	  	done_queue = new boolean[TASK+1]; 	// Be careful id is from 1~Task
	  	for(i=1; i<TASK+1; i++)
	  		done_queue[i]=false;

	  	//Vtime_table = new int[Vtime_table_size];
	  	//for(i=1; i<Vtime_table_size; i++)
	  	//Vtime_table[i]=0;	  	
	  	
	  	for(i=0; i<TASK; i++) {
	  		task[i] = new Task();
	  		finishing_order_queue[i] = new Task();	
	  	}
	
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

		/* after tasks are all enqueued */
		Thread[] myThreads = new Thread[THREADS];
	    for (i = 0; i < THREADS; i++) {
	    	myThreads[i] = new CPUThread(i, instance, htable, lock); 
	    }
	    for (i = 0; i < THREADS; i ++) {
	    	myThreads[i].start();
	    }
	    
	  	/* main keeps checks all task start time */
		/* main thread only check whether should I place a Task from pool to the run_queue(rbtree) */
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
					adjust_Vtime(task[i], htable);
					
					Task _task = new Task();		// redundant?
					thread_copy(_task, task[i]);	// redundant?
					
					// 1. enqueue() to run_queue
					push_to_rbtree(_task, instance, lock);
					// 2. kill the task in task[] (task table)
					thread_clean(task[i]);	// remove from task table	
				}
			}

			if(g_done_thread_num.get()==TASK) // all TASK are done
				break;
			
		} // while(1) end
		
		for (i = 0; i < THREADS; i ++) {
	    	myThreads[i].join();
	    }
		
		if (instance.leftMost()!=null)
			System.out.println("ERROR: tasks not done");
		else // ==null
			System.out.println("Good: tasks are all done");
		
		System.out.println("--------------------------------------------");
		System.out.println("THREADS" + THREADS); // number of workers (simulated CPUs, not task!!!!!!!!!!!)
		System.out.println("TimerIntThreshold" + TimerIntThreshold); // here time is ns
		System.out.println("min_granunarity" + min_granunarity); // minimum granularity // 1ms
		System.out.println("dynaic_nice_rang" + dynaic_nice_rang); // nice(dynamic) = original_nice +-dynaic_nice_rang	
		System.out.println("--------------------------------------------");
		System.out.println("TASK=" + TASK);
		System.out.println("g_queue_thread_num=" + g_queue_thread_num.get());
		System.out.println("g_done_thread_num=" + g_done_thread_num.get());
		System.out.println("g_time=" + g_time + " us");
		System.out.println("g_time=" + g_time/1000 + " ms");
		System.out.println("g_time=" + g_time/1000/1000 + " s");
		
		for(i=0; i<TASK; i++) {
			System.out.print(finishing_order_queue[i].id + " ");			
		}
		System.out.println("");
	}

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
				String line22 = "";
				String delims_space = "[ \t]+"; //target: "the it   hard        concentrate";
				String[] tokens = line.split(delims_space);
				String[] args = new String[100];
				for (int i = 1; i < tokens.length; i++) {
					line22 += tokens[i];
					args[i] = tokens[i];
				}
				
				//System.out.println("-->" + tokens[0] +  "\t");
		
				//just cnt thread numbers
				if(Integer.parseInt(tokens[1])>0)
					line_num += Integer.parseInt(tokens[1]);
				//System.out.println("-->num " + Integer.parseInt(tokens[1]) +  "\t");

				// read the next line
				line = in.readLine();
			} //file ends 
			//close the "pipe"
			in.close();
		
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
					//= Integer.parseInt(tokens[1]);	// number of thread
					task[i].cpu = Integer.parseInt(tokens[2])*1000;	// cpu (ms) need to *1000
					task[i].io = Integer.parseInt(tokens[3])*1000;	// io (ms) need to * 1000
					task[i].prio = Integer.parseInt(tokens[4]);	// prio
					task[i].nice = Integer.parseInt(tokens[5]);	// nice 
					task[i].ori_nice = Integer.parseInt(tokens[5]);	// ori_nice 
					task[i].start_time = Integer.parseInt(tokens[6]);	// start_time (used for interrupt or mimicing preemptive tasks) 
					
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
	
	private static void push_to_rbtree(Task _task, Tree<Task> instance, ReentrantLock lock) {	
		g_queue_thread_num.getAndIncrement();
		lock.lock();  // block until condition holds
	    try {
	    	instance.add(_task); // must succeed
			//System.out.println("height"+instance.height());
	    	//instance.print();
	    } finally {
	    	lock.unlock();
	    }
	}
	
	public static void kill_from_rbtree(Task _task, Tree<Task> instance, ReentrantLock lock) {
		lock.lock();  // block until condition holds
	    try {
			instance.remove(_task);
			g_queue_thread_num.getAndDecrement();	
	    } finally {
	    	lock.unlock();
	    }
	}
	
	public static Task pop_from_rbtree(Tree<Task> instance, ReentrantLock lock) {
		Task _task;
		lock.lock();  // block until condition holds
	    try {
	    	//instance.print();
	    	_task = instance.leftMost();
			if(_task==null)
				return null;
			else {
				instance.remove(_task);
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
			int i=0;
			Task curr_task;

			try { Thread.sleep(1500); } catch (InterruptedException e) { e.printStackTrace(); }
			
			while(true) {
				curr_task = pop_from_rbtree(instance, _lock);
				//System.out.println("curr_task="+curr_task);
				if (curr_task==null) {
					//System.out.println("curr_task="+curr_task);
					continue;	// nothing in run queue
				}
			
				CPUThread currThread = (CPUThread) CPUThread.currentThread();
				System.out.println("Thread_id = " + currThread.id + ", Task_id = " + curr_task.id);

				/* Load a new task to run */
				// sched2 - recalculate time_slice 
				curr_task.time_slice = (int) ((1*1000) * (float)(curr_task.nice / (1024 / Math.pow(1.25, curr_task.nice))));
										//TODO: nice=0 is wrong => this is wrong
				if (curr_task.time_slice <= min_granunarity)
					curr_task.time_slice=min_granunarity;
				//System.out.println("_task.time_slice = " +_task.time_slice + "\t nice=" + _task.nice);

				// // sched2 - clean(initialize) all runtime info
				curr_task.cpu_runtime=0;	// run_time record used for dynamic priority
				curr_task.io_runtime=0; 	// run_time record used for dynamic priority
				
				t_time=0;		// initialize thread timer
				is_exit=false; 	// clear exit flag
				if ( ((curr_task.cpu+curr_task.io) <= 0) ) {
					System.out.println("ERROR: finished before runing");
				}
				do {
						t_time++;
						is_exit = JobTask(curr_task, 0); 
					if (t_time > TimerIntThreshold || is_exit==true) // case1 + case2
						break;
				}while(true);
				
				// kernel space : 
				// case 1. exit()
				if(is_exit==true) { // feature - exit() interrupt			
					if ( ((curr_task.cpu+curr_task.io) <= 0) ) {	// task done
						System.out.println("curr id=" +curr_task.id + ", cpu=" + curr_task.cpu + ", io=" + curr_task.io);			

			  			g_done_thread_num.getAndIncrement();
			  			/* clean runtime info to record for the next run */
			  			//curr_task.cpu_runtime=0;
			  			//curr_task.io_runtime=0;
			  			kill_from_rbtree(curr_task, instance, _lock);
						System.out.println("queue_num = " + g_queue_thread_num.get());			
						System.out.println("done_num = " + g_done_thread_num.get());
			  			//System.out.println("why height = " + ((AVL<Task>)instance).height());
					}
				}
				else if (t_time > TimerIntThreshold) { // feature - timer interrupt
					// case 1. Job not done BUT time slice is reached. recycle(reclaim).
					if ( curr_task.time_slice <= (curr_task.cpu_runtime+curr_task.io_runtime) ) { // expired must deq()	
						// time_slice passed(out) 
						//System.out.println("1. " + curr_task.cpu_runtime + "\t2. " +curr_task.io_runtime + "\t3. " + curr_task[i].time_slice);
			  			// sched1 - update Virtual Time - virtual += timslice (before push)
			  			int temp_int=0;
			  			temp_int += curr_task.VirtualRunTime.intValue();
			  			temp_int += curr_task.cpu_runtime+curr_task.io_runtime; // + actual run time NOT time_slice 
						System.out.println("1.cpu_run. " + curr_task.cpu_runtime + "\t2io_run. " +curr_task.io_runtime + "\t3slice. " + curr_task.time_slice + "\t4new_slice. " + temp_int);

						curr_task.VirtualRunTime = new Integer(temp_int); 
			  			adjust_Vtime(curr_task, _htable);
			  			
			  			// update nice
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
						push_to_rbtree(curr_task, instance, _lock);
			  			//thread_clean(curr_task);
			  		} // expired end
				} //kernel end
				//else { // time_slice remains, keep running}
			} //while end
		}
		
		public int GetTotalDeq() {
		      return TotalDeq;  
		}

		public int GetGoodDeq() {
		      return GoodDeq;  
		}
		   
	}


}
