package practice.db;

import java.lang.ref.WeakReference;

public class FJWAException extends Exception {
	/**
	 * 
	 */
	private WeakReference<Integer> i;
	private static final long serialVersionUID = 6561928962759259328L;
	
	public final Exception exception;
	public FJWAException(Exception exception, String message) {
		super(message + '\n' + exception.getMessage());
		this.exception = exception;
	}
	
	public FJWAException(String message) {
		super(message);
		this.exception = this;
	}
}
