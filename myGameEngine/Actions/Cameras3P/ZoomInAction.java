package myGameEngine.Actions.Cameras3P;

import myGameEngine.Cameras.CameraController3P;

import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;

public class ZoomInAction extends AbstractInputAction
{
	private CameraController3P c;
	private float radius;
	
	public ZoomInAction(CameraController3P c, float radius)
	{
		this.c = c;
		this.radius = radius;
	}
	
	@Override
	
	public void performAction(float time, Event e)
	{
		radius += 0.05f;
		
		//Check Bounds
		if (radius > 4.0f)
		{
			radius = 4.0f;
		}
		if (radius < 1.0f)
		{
			radius = 1.0f;
		}
		c.updateCameraPos();
		
	}
}
