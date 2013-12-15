package smw.infinity;

import java.util.concurrent.ConcurrentLinkedQueue;

public class InteractionQueue extends ConcurrentLinkedQueue<Interaction>
{
	private static final long serialVersionUID = 7096005959862753124L;
	
	@Override
	public boolean offer(Interaction i)
	{
		return contains(i) ? false : super.offer(i);
	}
	
	@Override
	protected void finalize() throws Throwable
	{
		while(!isEmpty())
		{
			poll().interact();
			
			try
			{
				Thread.sleep(Scene.FRAME_DELAY);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		
		super.finalize();
	}
}