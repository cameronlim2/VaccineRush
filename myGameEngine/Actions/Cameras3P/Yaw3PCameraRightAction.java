package myGameEngine.Actions.Cameras3P;

import myGameEngine.Cameras.CameraController3P;

import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.Camera;

public class Yaw3PCameraRightAction extends AbstractInputAction
{
	private CameraController3P c;
	private float cAzimuth, cAzimuthMin, cAzimuthMax;
	
	public Yaw3PCameraRightAction(CameraController3P c, float cAzimuth, float cAzimuthMin, float cAzimuthMax)
	{
		this.c = c;
		this.cAzimuth = cAzimuth;
		this.cAzimuthMin = cAzimuthMin;
		this.cAzimuthMax = cAzimuthMax;
	}
	
	@Override
	
	public void performAction(float time, Event e)
	{
		cAzimuth -= 1.5f;
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
