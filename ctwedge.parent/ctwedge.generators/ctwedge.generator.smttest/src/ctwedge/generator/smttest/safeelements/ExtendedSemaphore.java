package ctwedge.generator.smttest.safeelements;

import java.util.concurrent.Semaphore;

/**
 * An extended class for storing not only the semaphore status but also the identifier of the thread owning the semaphore
 */
public class ExtendedSemaphore extends Semaphore {

	private static final long serialVersionUID = 1L;
	
	public static ExtendedSemaphore OPERATION_SEMAPHORE = new ExtendedSemaphore();
	
	long threadID;

	public ExtendedSemaphore() {
		super(1);
		threadID = -1;
	}
	
	/**
	 * Overrides the acquire of the semaphore by storing the thread ID
	 * @throws InterruptedException 
	 */
	@Override
	public void acquire() throws InterruptedException {
		super.acquire();
		this.threadID = Thread.currentThread().getId();
	}
	
	/**
	 * Overrides the tryAcquire of the semaphore by storing the thread ID
	 * @throws InterruptedException 
	 */
	@Override
	public boolean tryAcquire() {
		if (super.tryAcquire()) {
			threadID = Thread.currentThread().getId();
			return true;
		}
		return false;
	}
	
	/**
	 * Overrides the release of the semaphore by storing the thread ID
	 * @throws InterruptedException 
	 */
	@Override
	public void release(){
		this.threadID = -1;
		super.release();
	}
	
	/**
	 * Checks if the mutex is locked by the current thread
	 * 
	 * @return true if the semaphore is locked by the current thread, false otherwise
	 */
	public boolean lockedByCaller() {
		return this.threadID == Thread.currentThread().getId();
	}

}
