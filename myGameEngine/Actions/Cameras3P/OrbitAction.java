package myGameEngine.Actions.Cameras3P;

import myGameEngine.Cameras.CameraController3P;

import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;

public class OrbitAction extends AbstractInputAction
{
	private CameraController3P c;
	private float cAzimuth, cAzimuthMin, cAzimuthMax;
	
	public OrbitAction(CameraController3P c, float cAzimuth, float cAzimuthMin, float cAzimuthMax)
	{
		this.c = c;
		this.cAzimuth = cAzimuth;
		this.cAzimuthMin = cAzimuthMin;
		this.cAzimuthMax = cAzimuthMax;
	}
	
	@Override
	
	public void performAction(float time, Event e)
	{
		float rotation;
		
		if (e.getValue() > 0.2)
		{
			rotation = -1.2f;
		}
		else if (e.getValue() < -0.2)
		{
			rotation = 1.2f;
		}
		else
		{
			rotation = 0.0f;
		}
		
		cAzimuth += rotation;
		if (cAzimuth > cAzimuthMax)
		{
			cAzimuth = cAzimuthMax;
		}
		if (cAzimuth < cAzimuthMin)
		{
			cAzimuth = cAzimuthMin;
		}
	
		c.updateCameraPos();
		
	}
}
