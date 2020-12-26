package myGameEngine.Actions;

import ray.input.action.AbstractInputAction;
import vaccineRush.VaccineRush;
import net.java.games.input.Event;

public class IncrementCounterAction extends AbstractInputAction
{
	private VaccineRush g;
	
	public IncrementCounterAction(VaccineRush g)
	{
		this.g = g;
	}
	
	@Override
	public void performAction(float time, Event e)
	{
		g.incrementCounter();
	}
}