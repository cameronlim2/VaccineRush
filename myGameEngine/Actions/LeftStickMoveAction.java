package myGameEngine.Actions;

import ray.input.action.AbstractInputAction;
import ray.rage.game.*;
import vaccineRush.VaccineRush;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.Event;

public class LeftStickMoveAction extends AbstractInputAction
{
	private VaccineRush g;
	private Controller c;
	
	public LeftStickMoveAction(VaccineRush g, Controller c)
	{
		this.g = g;
		this.c = c;
	}
	
	@Override
	public void performAction(float time, Event e)
	{
		Component leftStickX = c.getComponent(net.java.games.input.Component.Identifier.Axis.X);
		Component leftStickY = c.getComponent(net.java.games.input.Component.Identifier.Axis.Y);
		
		System.out.println(" Left stick used... X: " + leftStickX.getPollData());
		System.out.println("                    Y: " + leftStickY.getPollData() + "\n");
	}
}
