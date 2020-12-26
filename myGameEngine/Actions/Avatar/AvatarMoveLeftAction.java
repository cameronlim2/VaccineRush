package myGameEngine.Actions.Avatar;

import ray.input.action.AbstractInputAction;
import net.java.games.input.Event;
import ray.rage.scene.*;
import vaccineRush.VaccineRush;

public class AvatarMoveLeftAction extends AbstractInputAction
{
	private SceneNode avatar;
	private VaccineRush game;
	
	public AvatarMoveLeftAction(SceneNode node, VaccineRush g)
	{
		avatar = node;
		game = g;
	}
	
	@Override
	public void performAction(float time, Event e)
	{
		avatar.moveLeft(-0.25f);
		game.syncAvatarPhysics(avatar);
	//	protClient.sendMoveMessage(avatar.getWorldPosition());
	}
}
