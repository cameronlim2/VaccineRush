package myGameEngine.Actions.Cameras3P;

import myGameEngine.Cameras.CameraController3P;

import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;

public class ElevateOrbitAction extends AbstractInputAction
{
	private CameraController3P c;
	private float cElevation;
	
	public ElevateOrbitAction(CameraController3P c, float cElevation)
	{
		this.c = c;
		this.cElevation = cElevation;
	}
	
	@Override
	
	public void performAction(float time, Event e)
	{
		float rotation;
		
		if (e.getValue() > 0.2)
		{
			rotation = -1.5f;
		}
		else if (e.getValue() < -0.2)
		{
			rotation = 1.5f;
		}
		else
			rotation = 0;
		
		cElevation += rotation;
		if (cElevation < -40.0f)
			{
			cElevation = -40.0f;
			}
		if (cElevation > 40.0f)
		{
			cElevation = 40.0f;
		}
		c.updateCameraPos();
		
	}
}
