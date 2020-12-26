package myGameEngine.Actions.Avatar;

import ray.input.action.AbstractInputAction;
import net.java.games.input.Event;
import ray.rage.scene.*;
import vaccineRush.VaccineRush;

public class AvatarBackwardAction extends AbstractInputAction
{
	private SceneNode avatar;
	private VaccineRush game;
	
	public AvatarBackwardAction(SceneNode node, VaccineRush g)
	{
		avatar = node;
		game = g;
	}
	
	@Override
	public void performAction(float time, Event e)
	{
		avatar.moveBackward(0.25f);
		game.syncAvatarPhysics(avatar);
		//protClient.sendMoveMessage(avatar.getWorldPosition());
	}
}
