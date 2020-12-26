package myGameEngine.Actions.Cameras3P;

import myGameEngine.Cameras.CameraController3P;

import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;

public class OrbitRadiusAction extends AbstractInputAction
{
	private CameraController3P c;
	private float radius;
	
	public OrbitRadiusAction(CameraController3P c, float radius)
	{
		this.c = c;
		this.radius = radius;
	}
	
	@Override
	
	public void performAction(float time, Event e)
	{
		float rotation;
		
		if (e.getValue() > 0.2)
		{
			rotation = -0.05f;
		}
		else if (e.getValue() < -0.2)
		{
			rotation = 0.05f;
		}
		else
		{
			rotation = 0.0f;
		}
		
		radius += rotation;
		if (radius < 1.0f)
		{
			radius = 1.0f;
		}
		if (radius > 4.0f)
		{
			radius = 4.0f;
		}
		c.updateCameraPos();
		
	}
}
