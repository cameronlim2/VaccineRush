package myGameEngine.Actions.Avatar;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.Node;

import vaccineRush.VaccineRush;
import net.java.games.input.Event;

public class ActivateGermAction extends AbstractInputAction
{
	private Node avatar;
	private VaccineRush game;
	public ActivateGermAction(Node a, VaccineRush g) {
		avatar = a;
		game = g;
	}

	public void performAction(float time, Event e)
	{
		game.activateGerm();
	}
}