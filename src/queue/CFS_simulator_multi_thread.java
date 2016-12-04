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
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import avl.*;
import java.io.*;  

public class CFS_simulator_multi_thread<T extends Comparable<T>> {
	/* default values */ /* unit=us */
	static int THREADS = 8; 					// number of workers (simulated CPUs, not task!!!!!!!!!!!)
	static int TimerIntThreshold = 1000*1000; 	// here time is ns
	static int min_granunarity = 1000*1000;		// minimum granularity // 1ms
	static int dynaic_nice_rang = 5;			// nice(dynamic) = original_nice +-dynaic_nice_rang
	
	static int TASK = -1; 				// global number of tasks (threads) assigned by in.txt
	static int g_time;					// global time
	static int g_queue_thread_num;		// global number of threads in run_queue
	//static int g_exec_thread_num; 		// global number of executing threads on simulated CPUs
	static AtomicInteger g_done_thread_num;		// global number of threads done
    //static AtomicInteger totalItems;
    //g_done_thread_num.get();
    //g_done_thread_num.getAndIncrement();
    
	static boolean DEBUG = true;			
	static boolean g_done = false;			
	static boolean g_is_interrupted = false;

	static Task[] task; 			// all tasks gonna run in this simulation
  	public static Task[] run_queue; // tasks should be ran
  	static Task[] running_tasks; 	// running on simulated CPU
  	static boolean[] done_queue; 	// Be careful id is from 1~Task
  	static Task[] finishing_order_queue;

	private static Random random = new Random();
	
	static //private Node<T> root = null;
	AVL<Integer> instance;
	
	public CFS_simulator_multi_thread(String testName, int thread, int duration, int n, int ops) {
		g_time = 0;
		g_queue_thread_num = 0;
		instance = new AVL<Integer>();
	}
  
	public static void main(String[] args) throws Exception {
	  	int i, j, k;
	  	int left;
	  	boolean is_interrupted[];
	  	int virtualtime = -1;
	  	int timer; // timer interrupt cnt

	  	  	
		int data = 1;
		//this.root = new Node<T>(null);
		
		/* Test */
		//empty
		System.out.println("tree height = " + instance.height());
		System.out.println("tree get leftmost = " + instance.get_leftmost());
		
		// 4
		instance.add(4);		
		System.out.println("tree height = " + instance.height());
		System.out.println("tree get leftmost = " + instance.get_leftmost());
		
		// 5
		instance.add(5);		
		System.out.println("tree height = " + instance.height());
		System.out.println("tree get leftmost = " + instance.get_leftmost());
				
		// 6
		instance.add(6);		
		System.out.println("tree height = " + instance.height());
		System.out.println("tree get leftmost = " + instance.get_leftmost());
		
		// 3
		instance.add(3);		
		System.out.println("tree height = " + instance.height());
		System.out.println("tree get leftmost = " + instance.get_leftmost());
		
		// 2
		instance.add(2);		
		System.out.println("tree height = " + instance.height());
		System.out.println("tree get leftmost = " + instance.get_leftmost());
				
		// 1
		instance.add(1);		
		System.out.println("tree height = " + instance.height());
		System.out.println("tree get leftmost = " + instance.get_leftmost());
		
		// empty
		instance.remove(data);	
		System.out.println("tree height = " + instance.height());
		System.out.println("tree get leftmost = " + instance.get_leftmost());
	
		
		

	  	
	  	
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
		 * 
		 * */
		
		
		// Create THREADS threads
		
		
		// Create Tasks	
		task = new Task[TASK];					// all tasks in this simulation
	  	// xxxxxxxxxxx							// enqueue to tree
		
		//run_queue = new Task[TASK];				// visible while run time
	  	//running_tasks = new Task[THREADS];		// executing // thread will take care of this
	  	finishing_order_queue = new Task[TASK]; // check finishing order // for record
	  	
	  	is_interrupted = new boolean[THREADS];	
	  	for(i=0; i<THREADS; i++)
	  		is_interrupted[i]=false;
	  	
	  	done_queue = new boolean[TASK+1]; 	// Be careful id is from 1~Task
	  	for(i=1; i<TASK+1; i++)
	  		done_queue[i]=false;

	  	for(i=0; i<TASK; i++) {
	  		task[i] = new Task();
	  		//run_queue[i] = new Task();	
	  		finishing_order_queue[i] = new Task();	
	  	}
	  	
	  	//for(i=0; i<THREADS; i++) {
	  		//running_tasks[i] = new Task();
	  	//}
	
	  	// init threads
	  	read_file_lines2();
	  	/*
	  	for(i=0; i<TASK; i++) {
	  		// read from txt
	  		task[i].id = i+1; // from 0 to Task

	  		task[i].cpu = 1*1000*1000*10; // user defined (ms)
	  		task[i].io = 1*1000*1000*10; // user defined (ms)
	  		task[i].prio = 1000; //99 (high prio) ~ 0 (low prio) 
	  		task[i].start_time = 0; // optional
	  		
	  		// init
	  		task[i].nice = 1; // -20 (high prio) ~ 19 (low prio)   // -19 (high prio) ~ 20 (low prio)
	  		task[i].VirtualRunTime = 0; 
	  		task[i].time_slice = 0;
	  		task[i].weight = 0;
		}
		*/
		
if(DEBUG){  	
	  	for(i=0; i<TASK; i++) {
	  		// read from txt
	  		System.out.print(task[i].id + " ");
	  		System.out.print(task[i].cpu + " ");
	  		System.out.print(task[i].io + " ");
	  		System.out.print(task[i].prio + " ");
	  		System.out.print(task[i].nice + " ");
	  		System.out.print(task[i].start_time + " ");

	  		System.out.println("");
		}
}
	  	
	  	System.out.println("task[0] = " + task[0]);
		System.out.println("task[0].id = " + task[0].id);
		System.out.println("task[1].id = " + task[1].id);
	  	
		// init all CPU
		//for(i=0; i<THREADS; i++) { // traverse all CPU
		//	running_tasks[i].id=0;
		//}
		
		
		/* after tasks are all enqueued */
		Thread[] myThreads = new Thread[THREADS];
	    for (i = 0; i < THREADS; i++) {
	    	myThreads[i] = new CPUThread(i); 
	    }
	    for (i = 0; i < THREADS; i ++) {
	      //myThreads[i].start();
	    }
	    for (i = 0; i < THREADS; i ++) {
	      //myThreads[i].join();
	    }
	    
	  	/* main keeps checks all task start time */
		
		/* main thead only check whether should I place a Task from pool to the run_queue(rbtree) */
		/* mimicing external interrupt with polling*/
		while(true) { // infinite loop until every work is done
			//do {
			  	
			  	/*clear flags*/
			  	//g_is_interrupted=false;

				/* periodically debug */
				int how_many_int=100;
if(DEBUG){
				if(g_time>0 && TimerIntThreshold>0) {
					if (how_many_int/g_time/TimerIntThreshold==0) {
						System.out.println("--------------------------------------------");
						System.out.println("g_time=" + g_time);
						System.out.println("TASK=" + TASK);		// main() is the only one access TASK table
						//System.out.println("g_queue_thread_num=" + g_queue_thread_num);
						//System.out.println("g_exec_thread_num=" + g_exec_thread_num);
						System.out.println("g_done_thread_num=" + g_done_thread_num.get());
						//TODO: done_queue
					}
				}
}	
				
				/* check any thread should set to run_queue */
				for(i=0; i<TASK; i++) { // check any thread ready to run
					if( task[i].start_time >= g_time) {  // if so put it to runqueue
						/*
						int least_nice=1; // Feature: min garauntee
						for(k=0; k<TASK; k++) { // assign the least nice value to the new task
							if (run_queue[k].id!=0){
								if (least_nice > run_queue[k].nice)
									least_nice = run_queue[k].nice;
							}
						}
						task[i].nice = least_nice;
						*/
						
						// TODO: replace all run_queue with rbtree
						int least_Vtime=1; 		// Feature: min garauntee
						for(k=0; k<TASK; k++) { // assign the least nice value to the new task
							if (run_queue[k].id!=0) {
								if (least_Vtime > run_queue[k].VirtualRunTime)
									least_Vtime = run_queue[k].VirtualRunTime;
							}
						}
						task[i].VirtualRunTime = least_Vtime;
						
						
						// TODO: 1. enqueue() to run_queue
						//if (push_to_rbtree(task[i]) < 0 ) { //1. find a empty slot & insert the task to run_queue
						//	System.out.println("ERROR: enq() run_queue is full");
						//	break; // no more task can be inserted!!!!
						//}
						
						
						// 2. kill the task in task[] (task table)
						//g_queue_thread_num++; 	// record
						thread_clean(task[i]);	// remove from task table
					}
				}

				/*
				// if ideal cpu, push task
				for(i=0; i<THREADS; i++) { // traverse all CPU
					if(running_tasks[i].id==0) { // found ideal cpu
						if ( pop_from_rbtree(running_tasks[i]) >=0 ) {	// insert task to the cpu
							if(running_tasks[i].id!=0) { // if successful, record
								g_exec_thread_num++;
							}
						}
					}
				}
				
				System.out.println("thread id on CPUs:");
				for(i=0; i<THREADS; i++)
					System.out.print(running_tasks[i].id + " ");
				System.out.println("");
				
				boolean q_is_empty=true;
				for(i=0; i<THREADS; i++) { // traverse all CPU
					if( running_tasks[i].id!=0 || g_exec_thread_num!=0) { 
						q_is_empty=false;	// if is_task in run_queue
						break;
					}
				}
				if(q_is_empty==true)
					continue; // to do while(true) // if no task, keep looping 
				*/
				
				
			//	if(g_done_thread_num.get()==TASK) // all TASK are done
			//		break;
			//	
		  	//} while(true);
		  	
			if(g_done_thread_num.get()==TASK) // all TASK are done
				break;
			
		} // while
		
		// double check
		//int done_cnt=0;
		//for(i=1; i<TASK+1; i++) {
		//	if( done_queue[i]==true)
		//		done_cnt++; 
		//}
		//System.out.println("simulation done: " + done_cnt + " tasks finished");
		//System.out.println("simulation done: " + (TASK-done_cnt) + " tasks not finished");
		//if ((TASK-done_cnt)!=0)
		//	System.out.println("ERROR: tasks not done");

		if (instance.get_leftmost()!=null)
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
		//System.out.println("g_queue_thread_num=" + g_queue_thread_num);
		//System.out.println("g_exec_thread_num=" + g_exec_thread_num);
		System.out.println("g_done_thread_num=" + g_done_thread_num.get());
		System.out.println("g_time=" + g_time + " us");
		System.out.println("g_time=" + g_time/1000 + " ms");
		System.out.println("g_time=" + g_time/1000/1000 + " s");
		
		for(i=0; i<TASK; i++) {
			System.out.print(finishing_order_queue[i].id + " ");			
		}
		System.out.println("");
	}

	
	private static int read_file_lines() {
		int line_num = 0; 
		String fileName ="in.txt";
		File currentDir = new File("").getAbsoluteFile();
		System.out.println(currentDir);
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
		System.out.println(currentDir);
		String line;
		try {
			// use BufferedReader and apply on FileReader. process streaming apply on node streaming
			BufferedReader in = new BufferedReader(new FileReader(currentDir+"/"+fileName));

			// read the first line 
			line = in.readLine(); // ignore first line
			line = in.readLine();
			while(line!=null)
			{
				String line22 = "";
				//String line22 = new String;
				String delims_space = "[ \t]+"; //target: "the it   hard        concentrate";
				String[] tokens = line.split(delims_space);
				String[] args = new String[100];
				for (i=1; i < tokens.length; i++) {
					//System.out.println(tokens[i]);
					line22 += tokens[i];
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
					System.out.println("id=" + (i+1));
					//= Integer.parseInt(tokens[1]);	// number of thread
					task[i].cpu = Integer.parseInt(tokens[2])*1000;	// cpu (ms) need to *1000
					task[i].io = Integer.parseInt(tokens[3])*1000;	// io (ms) need to * 1000
					task[i].prio = Integer.parseInt(tokens[4]);	// prio
					task[i].nice = Integer.parseInt(tokens[5]);	// nice 
					task[i].ori_nice = Integer.parseInt(tokens[5]);	// ori_nice 
					task[i].start_time = Integer.parseInt(tokens[6]);	// start_time (used for interrupt or mimicing preemptive tasks) 
					
					task[i].VirtualRunTime = 0; 
				  	task[i].time_slice = 0;
				  	task[i].weight = 0;
				}

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
	
	// This is FIFO O(1) version //TODO: replace with tree 
	private static void push_to_rbtree(Task _task) {
		int i;
		
		instance.add(_task); // must succeed

		/*
		for(i=0; i<TASK ;i++) {
			if(run_queue[i].id==(0)) {
				//TODO: ERROR: copy hand by hand
				thread_copy(run_queue[i], _task);
				
				//TODO: sched1 - virtual += timslice 
				//				copy all info hand by hand
				
				//Reminder: TODO: sched2 - recalculate time_slice 
				//							clean runtime_info

				g_queue_thread_num++;
				return 0; // pushed successfully
			}
		}
		return -1; // queue is full
		*/
	}
	
	// This is FIFO O(1) version //TODO: replace with tree 
	private static T pop_from_rbtree(Task _task) { // TODO
		//int i;
		return (T) instance.get_leftmost();
		/*
		//Task _task = run_queue[0];
		//System.out.println("pop[0].id: " + run_queue[0].id);
		
		if(run_queue[0].id!=0) { // auto-sorted array, left-most aligned
			// copy
			thread_copy(_task, run_queue[0]);
			
			//TODO: sched2 - recalculate time_slice 
			//				clean runtime_info
			
			// weight = 1024 / (1.25 ^ nice_value)
			// runtime=(base) 1/allthread
			// virtual time += runtime * (nice / weight)
			// 1*1000*1000*1000(1s)
			// virtual time += (1*1000*1000*1000) * (_task.nice / (1024 / (1.25 ^ _task.nice)))
			_task.time_slice = (int) ((1*1000) * (float)(_task.nice / (1024 / Math.pow(1.25, _task.nice)))); //TODO: nice=0 is wrong => this is wrong
			
			if (_task.time_slice <= min_granunarity)
				_task.time_slice=min_granunarity;
			//System.out.println("_task.time_slice = " +_task.time_slice + "\t nice=" + _task.nice);


			// clean old queue
			run_queue[0].id=0;

			// clean(init) all runtime info
	  		_task.cpu_runtime=0;	// used for 
	  		_task.io_runtime=0; 	// used for dy
	  	
	  		g_queue_thread_num--;
	  		
  			// rearrange the queue - auto-sorted array, left-most aligned
	  		//int j=0;
	  		for (i=0; i<TASK-1; i++) {
	  			if (run_queue[i+1]!=null) { // end
	  				if (run_queue[i].id==0 && run_queue[i+1].id!=0) {
	  					thread_copy(run_queue[i], run_queue[i+1]);
	  					thread_clean(run_queue[i+1]);
	  					i=0; continue; 	// O(N^2)
	  				}
	  			}
	  		}
	  		
if(DEBUG){
	  		for (i=0; i<TASK-1; i++) {
	  			System.out.print( run_queue[i].id + " ");
	  		}
  			System.out.println("");
}
			
			return 0;
		}
		else { 
			System.out.println("pop failed: run_queue is empty (run_queue=" + g_queue_thread_num + ")");
			for(i=0; i<TASK; i++) { // check any thread ready to run
				System.out.print( run_queue[i].id + " ");
			}
			System.out.println("");

			return -1;
		}
		*/
	}
	
	/* 
	 * weight = 1 * 2 * 3
	 * 
	 * 1. use the old weight to determine the virtual time should be added on 
	 * 			take original  1 * 2 * 3 adding to virtual time
	 * 
	 * 2. update a new weight by this run (time_slice - I should run next time) 
	 * 	(AVOIDE TO CHAGNE TO MUCH IF THIS RUN DOESN'T RUN MUCH)
	 * 			TODO:
	 * 
	 * */
	public static int reschedule() { // 有拿到cpu的不會跑到這邊來
		int i=0;
		// make sure no any task in executing
		for (i=0; i<THREADS;i++) {
			if ( running_tasks[i].id!=(0)) {
				System.out.println("ERROR: reschedule(): runiing_task[] is not empty");
				return -2;
			}
		}
		// 寫在外面更簡單明瞭＝＝ 因為只針對要改變的調整而已
		// start to travers all task in run queueue 
		for(i=0; i<TASK; i++) { // traverse all task in queue. This is simple no need to take consideration into init
	  		
			//if ( (run_queue[i].io_runtime==0) && (run_queue[i].cpu_runtime==0) ) { 	//剛加入 或者上次沒搶到cpu
			//	;
			//}
			//else { // 剛剛有cpu幹點事情  來計算吧
				/* 1. use the old weight to determine the virtual time should be added on  */
				/*1-formula
				if( run_queue[i].nice>0 )
					run_queue[i].time_slice = (TimerIntThreshold * 4 * (run_queue[i].nice+20) /20); // t x 4 x 40(20+20)
				else										 // 					160x
					run_queue[i].time_slice = (TimerIntThreshold * 1 * (run_queue[i].nice+20) /20); // t x 1 x 1(-19+20)
				*/ 
				//2-another ideal runtime formula 
				//(run_queue[i]io_runtime + run_queue[i]_cpu_runtime) * (1024/run_queue[i].nice+20);
/*
				// I use version 1
				if( run_queue[i].nice>0 )
					run_queue[i].time_slice = ( (float)((float)(run_queue[i].io_runtime+run_queue[i].cpu_runtime)/(float)TimerIntThreshold) * (float)4 * (float)((run_queue[i].nice+20)/20)); // t x 4 x 40(20+20)
				else										 // 					160x
					run_queue[i].time_slice = ( (float)((float)(run_queue[i].io_runtime+run_queue[i].cpu_runtime)/(float)TimerIntThreshold) * (float)1 * (float)((run_queue[i].nice+20)/20)); // t x 1 x 1(-19+20)

				
				run_queue[i].VirtualRunTime += run_queue[i].time_slice;
				System.out.println("debug time_slice=" + run_queue[i].time_slice + " [i]=" + i);
*/			
				/* 2. update a new weight by this run (time_slice - I should run next time) */ 
/*

				if (run_queue[i].io_runtime > run_queue[i].cpu_runtime) {
					run_queue[i].nice++;	//TODO: this is too rough
				}
				
				if (run_queue[i].io_runtime < run_queue[i].cpu_runtime) {
					run_queue[i].nice--;	//TODO: this is too rough
				}
*/
			//}
		} 
		
		return 0;
	}
	
	/* Thread */
	public static boolean JobTask(Task task, int virtualtime) { //單一task
		// pass in the fake thread, virtualtime
		int weight=0; //used for kernel for determine this is a io or cpu bound task
		int rand=-1;
		//TODO: check realtime code how to do the periodic check (do we need to do?) 
		
		//int cpu=cpu;
		//int io=io;
		//randomly chose io/cpu and do it until time slice ends
		
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
		task1.VirtualRunTime = task2.VirtualRunTime;
		task1.time_slice = task2.time_slice; 		
		task1.weight = task2.weight;
		task1.start_time = task2.start_time;

		task1.io_runtime = task2.io_runtime;
		task1.cpu_runtime = task2.cpu_runtime;

	}

	private static void thread_clean(Task task1) {
		task1.id = 0;

		/*
		int cpu;
		int prio;				//
		int nice;
		int VirtualRunTime; 	// accumulated =   TimerIntThreshold * (time_slice/weight) 
		float time_slice; 		// chose one = prio + nice  = 100ms 
		int weight;				// chose one = prio + nice 
		int start_time;

		int io_runtime;
		int cpu_runtime;
		 
		 */
	}
	
	static class CPUThread extends Thread {
		private volatile int TotalDeq=0; //not used
		private volatile int GoodDeq=0;
		private volatile int id=-1;  
		int t_time=0; // thread run time
		
		
		private Random random = new Random();
		public CPUThread(int i) {
			id = i;
		}

		public int getrand(int tmp) {
			return random.nextInt(tmp);
		}
		
		public void run() {
			int i=0;
			int timer=0;
			Task curr_task;
			boolean is_exit=false;
			
			do{
				//if (instance.get_leftmost()==null) 
				if (pop_from_rbtree(curr_task)==null)
					continue;	// nothing in run queue

				/**
				calculate timeslice	
				JobTask
					if (done) // implement exit() with polling
				
				if(timeout)
				update runtime & nice
				push_tree();
				else if (done)
					done++ //atomically
				*/
				
				// calculate time_slice
				//TODO: sched2 - recalculate time_slice 
				//				clean runtime_info
				
				// weight = 1024 / (1.25 ^ nice_value)
				// runtime=(base) 1/allthread
				// virtual time += runtime * (nice / weight)
				// 1*1000*1000*1000(1s)
				// virtual time += (1*1000*1000*1000) * (_task.nice / (1024 / (1.25 ^ _task.nice)))
				curr_task.time_slice = (int) ((1*1000) * (float)(curr_task.nice / (1024 / Math.pow(1.25, curr_task.nice)))); //TODO: nice=0 is wrong => this is wrong
				
				if (curr_task.time_slice <= min_granunarity)
					curr_task.time_slice=min_granunarity;
				//System.out.println("_task.time_slice = " +_task.time_slice + "\t nice=" + _task.nice);

				// clean old queue
				//run_queue[0].id=0;

				// clean(init) all runtime info
				curr_task.cpu_runtime=0;	// used for 
				curr_task.io_runtime=0; 	// used for dy
				
				
				do {
					//if (timer<TimerIntThreshold ) { // keep running
						t_time++;
						timer++;
						
						is_exit=false; // clear
						is_exit = JobTask(curr_task, 0); 
					
					if (timer > TimerIntThreshold )
						break;
				}while(true);
				
				if (timer > TimerIntThreshold) { // feature - timer interrupt
					//check time_slice passed?
					// 3. not done but time slice is reached. recycle(reclaim).
					if ( curr_task.time_slice <= (curr_task.cpu_runtime+curr_task.io_runtime) ) { // expired mush deq()	
						// time_slice passed(out)
						System.out.println("1. " + curr_task.cpu_runtime + "\t2. " +curr_task.io_runtime + "\t3. " + running_tasks[i].time_slice);
			  			// update Virtual Time
			  			curr_task.VirtualRunTime += (curr_task.cpu_runtime+curr_task.io_runtime); // + actual run time NOT time_slice  // TODO: check time_slice is > 0
			  			
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

			  			// enq()
			  			//if ( push_to_rbtree(curr_task) >=0) {  // this should save all info and calculate time_slice before execute. TODO: rember to clean after calculation
							
							//TODO: sched1 - virtual += timslice (before push)
							//				copy all info hand by hand
							//Reminder: TODO: sched2 - recalculate time_slice (after pop) 
							//							clean runtime_info
							push_to_rbtree(curr_task);
			  					
			  				//g_exec_thread_num--;
			  				//g_queue_thread_num++;
			  				thread_clean(curr_task);
			  			//}
			  			//else { System.out.println("ERROR: push_torbtree failed");}
			  			//queue_arrange(running_taks);
			  		}
				}
			
				if(is_exit==true) { // feature - exit() interrupt			
					if ( ((curr_task.cpu+curr_task.io) <= 0) ) {	// task done

			  			// before cleaning info, record successful done threads
			  			//g_queue_thread_num--; 					// this is not comprehensive
			  			//System.out.println("id=?" + running_tasks[i].id + " done?=" + done_queue[running_tasks[i].id]);
			  			done_queue[running_tasks[i].id]=true; 	// record (before id=0)	1~Task
			  			//System.out.println("id=?" + running_tasks[i].id + " done?=" + done_queue[running_tasks[i].id]);
			  				
			  			thread_copy(finishing_order_queue[g_done_thread_num.get()], curr_task);
			  			//g_done_thread_num++;
			  			g_done_thread_num.getAndIncrement();
			  			//g_exec_thread_num--;

			  			/* clean runtime info to record for the next run */
			  			curr_task.cpu_runtime=0;
			  			curr_task.io_runtime=0;
			  			curr_task.id=0; 			// kill!! the task so that it will not be pushed back to the tree		
			  		}

				}
			}while(true);
		}
		      
		public int GetTotalDeq() { //
		      return TotalDeq;  
		}

		public int GetGoodDeq() { //
		      return GoodDeq;  
		}
		   
	}


}
