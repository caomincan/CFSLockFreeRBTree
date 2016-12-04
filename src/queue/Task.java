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
 * @author Maurice Herlihy
 */


//public class CFStest {
	public class Task {
		/* Task info - cfs entity */
		int id=0; // from 1 to TASK. So Tak[0]==0 is wrong!
		int io;
		int cpu;
		int prio;				//
		int nice;				// dynamic priority
		int ori_nice;			// user defined
		int VirtualRunTime; 	// accumulated =   TimerIntThreshold * (time_slice/weight) 
		int time_slice; 		// chose one = prio + nice  = 100ms  suppose to run
		int weight;				// chose one = prio + nice 
		int start_time;
		
		/* run-time record */
		int io_runtime;
		int cpu_runtime;
	}
	