package smw.infinity;

import java.lang.Thread.UncaughtExceptionHandler;

public class SMWUncaughtExceptionHandler implements UncaughtExceptionHandler
{
	@Override
	public void uncaughtException(Thread t, Throwable e)
	{
		e.printStackTrace();
	}
}
