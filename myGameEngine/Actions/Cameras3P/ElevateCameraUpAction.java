package myGameEngine.Actions.Cameras3P;

import myGameEngine.Cameras.CameraController3P;

import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;

public class ElevateCameraUpAction extends AbstractInputAction
{
	private CameraController3P c;
	private float cElevation;
	
	public ElevateCameraUpAction(CameraController3P c, float cElevation)
	{
		this.c = c;
		this.cElevation = cElevation;
	}
	
	@Override
	
	public void performAction(float time, Event e)
	{
		cElevation -= 1.5f;
		
		if (cElevation > 40.0f)
		{
			cElevation = 40.0f;
		}
		if (cElevation < -40.0f)
		{
			cElevation = -40.0f;
		}
		
		c.updateCameraPos();
		
	}
}
