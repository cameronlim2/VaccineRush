package myGameEngine.Actions.Avatar;

import ray.input.action.AbstractInputAction;
import net.java.games.input.Event;
import ray.rage.scene.*;

public class AvatarMoveRightAction extends AbstractInputAction
{
	private SceneNode avatar;
	
	public AvatarMoveRightAction(SceneNode node)
	{
		avatar = node;
	}
	
	@Override
	public void performAction(float time, Event e)
	{
		avatar.moveRight(-0.25f);
		//protClient.sendMoveMessage(avatar.getWorldPosition());
	}
}
