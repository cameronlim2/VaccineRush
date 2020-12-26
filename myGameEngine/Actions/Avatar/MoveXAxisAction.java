package myGameEngine.Actions.Avatar;

import ray.input.action.AbstractInputAction;
import ray.rage.scene.SceneNode;
import vaccineRush.VaccineRush;
import net.java.games.input.Event;

public class MoveXAxisAction extends AbstractInputAction
{
	private SceneNode avatar;
	private VaccineRush game;
	
	public MoveXAxisAction(VaccineRush g, SceneNode n)
	{
		game = g;
		avatar = n;
	}
	
	public void performAction(float time, Event e)
	{
		if (e.getValue() < -0.5)
		{
			avatar.moveLeft(-0.25f);
		}
		else if (e.getValue() > 0.5)
		{
			avatar.moveRight(-0.25f);
		}
		game.syncAvatarPhysics(avatar);
		//protClient.sendMoveMessage(avatar.getWorldPosition());
	}
}
