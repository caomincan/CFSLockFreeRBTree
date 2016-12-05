/*
 * LockFreeQueueTest.java
 * JUnit based test
 *
 * Created on December 27, 2005, 11:15 PM
 */

package queue;

import java.util.Random;

//import junit.framework.*;

/**
 * @author Jack
 */

public class Task implements Comparable<Task>{
	/* Task info - cfs entity */
	public int id=0; // from 1 to TASK. So Tak[0]==0 is wrong!
	public int io;
	public int cpu;
	public int prio;				//
	public int nice;				// dynamic priority
	public int ori_nice;			// user defined
	public int VirtualRunTime; 	// accumulated =   TimerIntThreshold * (time_slice/weight) 
	public int time_slice; 		// chose one = prio + nice  = 100ms  suppose to run
	public int weight;				// chose one = prio + nice 
	public int start_time;
	
	/* run-time record */
	public int io_runtime;
	public int cpu_runtime;
	
	@Override
	public int compareTo(Task o) {
		// TODO Auto-generated method stub
		return VirtualRunTime-o.VirtualRunTime;
	}
	
}
	