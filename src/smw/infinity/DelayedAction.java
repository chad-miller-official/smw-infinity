package smw.infinity;

import java.util.Timer;
import java.util.TimerTask;

public class DelayedAction
{
	private Runnable action;
	private long delay;
	
	public DelayedAction(Runnable action, long delay)
	{
		this.action = action;
		this.delay = delay;
	}
	
	public void startTimer()
	{
		new Timer().schedule(new TimerTask() {
			@Override
			public void run()
			{
				action.run();
			}
			
		}, delay);
	}
	
	public Runnable getAction()
	{
		return action;
	}
}