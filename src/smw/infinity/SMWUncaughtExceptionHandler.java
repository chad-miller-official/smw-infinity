package smw.infinity;

import java.lang.Thread.UncaughtExceptionHandler;

public class SMWUncaughtExceptionHandler implements UncaughtExceptionHandler
{
	@Override
	public void uncaughtException(Thread t, Throwable e)
	{
		System.err.println("Exception " + e.getClass().getSimpleName() + " caught in thread " + t.getName() + ".");
		e.printStackTrace();
	}
}
