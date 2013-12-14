package smw.infinity;

import java.util.HashSet;
import java.util.concurrent.ConcurrentLinkedQueue;

public class InteractionQueue extends ConcurrentLinkedQueue<Interaction>
{
	private static final long serialVersionUID = 7096005959862753124L;
	private HashSet<Interaction> interactions;	//for generally constant-time lookups for offer()
	
	public InteractionQueue()
	{
		super();
		interactions = new HashSet<Interaction>();
	}
	
	@Override
	public boolean offer(Interaction i)
	{
		if(interactions.contains(i))
			return false;
		else
		{
			interactions.add(i);
			return super.offer(i);
		}
	}
	
	@Override
	public Interaction poll()
	{
		Interaction toReturn = super.poll();
		interactions.remove(toReturn);
		return toReturn;
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