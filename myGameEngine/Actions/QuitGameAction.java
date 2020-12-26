package myGameEngine.Actions;

import ray.rage.game.*;
import ray.input.action.AbstractInputAction;
import vaccineRush.VaccineRush;
import net.java.games.input.Event;

public class QuitGameAction extends AbstractInputAction
{
	private VaccineRush g;
	
	public QuitGameAction(VaccineRush g)
	{
		this.g = g;
	}
	
	@Override
	public void performAction(float time, Event e)
	{
		System.out.println("Quitting...");
		g.setState(Game.State.STOPPING);
	}
}
