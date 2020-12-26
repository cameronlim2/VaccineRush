package myGameEngine.Actions.Avatar;

import ray.input.action.AbstractInputAction;
import net.java.games.input.Event;
import ray.rage.scene.*;
import vaccineRush.VaccineRush;

public class AvatarForwardAction extends AbstractInputAction
{
	private SceneNode avatar;
	private VaccineRush game;
	
	public AvatarForwardAction(SceneNode node, VaccineRush g)
	{
		avatar = node;
		game = g;
	}
	
	@Override
	public void performAction(float time, Event e)
	{
		float speed = game.getEngine().getElapsedTimeMillis() * .005f;
		game.avatarN.moveForward(speed * 1.5f);
		game.syncAvatarPhysics(avatar);
		//game.updateVerticalPosition();
		//protClient.sendMoveMessage(avatar.getWorldPosition());
	}
}
