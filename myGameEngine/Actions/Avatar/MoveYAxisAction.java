package myGameEngine.Actions.Avatar;

import ray.input.action.AbstractInputAction;
import ray.rage.scene.SceneNode;
import vaccineRush.VaccineRush;
import net.java.games.input.Event;

public class MoveYAxisAction extends AbstractInputAction
{
	private SceneNode avatar;
	private VaccineRush game;
	
	public MoveYAxisAction(VaccineRush g, SceneNode n)
	{
		game = g;
		avatar = n;
	}
	
	public void performAction(float time, Event e)
	{
		if (e.getValue() < -0.5)
		{
			avatar.moveForward(0.18f);
		}
		else if (e.getValue() > 0.5)
		{
			avatar.moveBackward(0.18f);
		}
		game.syncAvatarPhysics(avatar);
		//protClient.sendMoveMessage(avatar.getWorldPosition());
	}
}
