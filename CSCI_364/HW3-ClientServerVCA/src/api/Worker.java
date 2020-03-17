/**
 * Sam Dressler
 * Worker.java
 */
package api;

import java.io.Serializable;

public abstract class Worker implements Serializable {
	private static final long serialVersionUID = -1697662415894139584L;
	private final int taskId;
	private final String taskName;

	/**
	 * @param the task id for the work task
	 * @param the name of this task
	 */
	public Worker(final int id, final String name) {
		taskId = id;
		taskName = name;
	}
	
	/**
	 * 
	 * @return the id of this worker. A unique value that distinguishes this 
	 * instance of a Worker from all other instances.
	 */
	public int getTaskId() {
		return taskId;
	}
	
	/**
	 * 
	 * @return the name of this worker
	 */
	public String getTaskName() {
		return taskName;
	}

	/**
	 * Does the work for this worker. This method shall only be called by the 
	 * volunteer client. 
	 */
	public abstract void doWork();
}
