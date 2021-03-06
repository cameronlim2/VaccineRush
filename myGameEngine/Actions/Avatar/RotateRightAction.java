package myGameEngine.Actions.Avatar;

import ray.input.action.AbstractInputAction;
import ray.rml.*;
import ray.rage.scene.*;

import net.java.games.input.Event;

public class RotateRightAction extends AbstractInputAction
{
	private SceneNode avatar;
	
	
	public RotateRightAction(SceneNode n)
	{
		avatar = n;
	}
	
	public void performAction(float time, Event e)
	{
		Angle rotation = Degreef.createFrom(-2.0f);
		avatar.yaw(rotation);
	}
}
