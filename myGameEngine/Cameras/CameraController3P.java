package myGameEngine.Cameras;

import myGameEngine.Actions.*;
import myGameEngine.Actions.Cameras3P.*;

import ray.input.InputManager;
import ray.input.action.AbstractInputAction;
import ray.input.action.Action;
import ray.rage.scene.Camera;
import ray.rage.scene.SceneNode;
import ray.rml.Angle;
import ray.rml.Degreef;
import ray.rml.Vector3;
import ray.rml.Vector3f;

import java.util.ArrayList;

import net.java.games.input.Controller;
import net.java.games.input.Event;

public class CameraController3P 
{
	private Camera c;
	private SceneNode cNode, cTarget;
	private float cAzimuth, cAzimuthMax, cAzimuthMin, cElevation, radius;
	private Vector3 worldUpVector;
	private InputManager im;
	private String controllerName, keyboardName;
	
	public CameraController3P(Camera c, SceneNode cNode, SceneNode cTarget, String keyboardName, String controllerName, InputManager im)
	{
		this.cTarget = cTarget;
		this.c = c;
		this.cNode = cNode;
		setupInputs(im, controllerName);
		radius = 2.0f;
		cElevation = 20.0f;
		cAzimuth = 180.0f;
		cAzimuthMax = 220.0f;
		cAzimuthMin = 120.0f;
		worldUpVector = Vector3f.createFrom(0.0f, 1.0f, 0.0f);
		updateCameraPos();
	}

	private void setupInputs(InputManager im, String controllerName)
	{
		
		Action avatarRightAction = new AvatarRightAction();
		Action avatarLeftAction = new AvatarLeftAction();
		Action elevateOrbitAction = new ElevateOrbitAction();
		Action elevateCameraUpAction = new ElevateCameraUpAction();
		Action elevateCameraDownAction = new ElevateCameraDownAction();
		Action orbitAction = new OrbitAction();
		Action orbitRadiusAction = new OrbitRadiusAction();
		Action yaw3PCameraLeftAction = new Yaw3PCameraLeftAction();
		Action yaw3PCameraRightAction = new Yaw3PCameraRightAction();
		Action zoomInAction = new ZoomInAction();
		Action zoomOutAction = new ZoomOutAction();
		
		ArrayList<Controller> contArrayList = im.getControllers();
		for (Controller keyboards : contArrayList)
		{
			if (keyboards.getType() == Controller.Type.KEYBOARD) 
			{
				im.associateAction(keyboards,
						net.java.games.input.Component.Identifier.Key.LEFT,
						yaw3PCameraLeftAction,
						InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				
				im.associateAction(keyboards,
						net.java.games.input.Component.Identifier.Key.RIGHT,
						yaw3PCameraRightAction,
						InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				
				im.associateAction(keyboards,
						net.java.games.input.Component.Identifier.Key.UP,
						elevateCameraUpAction,
						InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				
				im.associateAction(keyboards,
						net.java.games.input.Component.Identifier.Key.DOWN,
						elevateCameraDownAction,
						InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				
				im.associateAction(keyboards,
						net.java.games.input.Component.Identifier.Key.Q,
						avatarLeftAction,
						InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				
				im.associateAction(keyboards,
						net.java.games.input.Component.Identifier.Key.E,
						avatarRightAction,
						InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				
				im.associateAction(keyboards,
						net.java.games.input.Component.Identifier.Key.ADD,
						zoomInAction,
						InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				
				im.associateAction(keyboards,
						net.java.games.input.Component.Identifier.Key.SUBTRACT,
						zoomOutAction,
						InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				
				}
			}
		
		if (controllerName == null) {}
		else
			{
				im.associateAction(controllerName,
						net.java.games.input.Component.Identifier.Axis.RY,
						elevateOrbitAction,
						InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				
				im.associateAction(controllerName,
						net.java.games.input.Component.Identifier.Axis.RX,
						orbitAction,
						InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				
				im.associateAction(controllerName,
						net.java.games.input.Component.Identifier.Axis.Z,
						orbitRadiusAction,
						InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				im.associateAction(controllerName,
						net.java.games.input.Component.Identifier.Button._4,
						avatarLeftAction,
						InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				im.associateAction(controllerName,
						net.java.games.input.Component.Identifier.Button._5,
						avatarRightAction,
						InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		}
	}
	
	public Vector3 updateCameraPos()
	{
		double phi = Math.toRadians(cElevation);
		double theta = Math.toRadians(cAzimuth);
		double x = radius * Math.cos(phi) * Math.sin(theta);
		double y = radius * Math.sin(phi);
		double z = radius * Math.cos(phi) * Math.cos(theta);
		
		cNode.setLocalRotation(cTarget.getWorldRotation());
		cNode.setLocalPosition(Vector3f.createFrom( (float)x, (float)y, (float)z).add(cTarget.getWorldPosition()));
		cNode.lookAt(cTarget, cTarget.getWorldUpAxis());
		
		return cNode.getLocalPosition();
	}
	
	public void setCameraPos(float x, float y, float z)
	{
		cNode.setLocalPosition(x, y, z);
	}
	
	private class AvatarLeftAction extends AbstractInputAction
	{
		@Override
		
		public void performAction(float time, Event e)
		{
			Angle rotationSpeed = Degreef.createFrom(1.5f);
			cTarget.yaw(rotationSpeed);
			cAzimuth += 1.5;
			cAzimuthMin += 1.5f;
			cAzimuthMax += 1.5f;
			if (cAzimuth > cAzimuthMax)
			{
				cAzimuth = cAzimuthMax;
			}
			if (cAzimuth < cAzimuthMin)
			{
				cAzimuth = cAzimuthMin;
			}
			
			updateCameraPos();
			
		}
	}
	
	private class AvatarRightAction extends AbstractInputAction
	{
		public void performAction(float time, Event e)
		{
			Angle rotationSpeed = Degreef.createFrom(-1.5f);
			cTarget.yaw(rotationSpeed);
			cAzimuth -= 1.5;
			cAzimuthMin -= 1.5f;
			cAzimuthMax -= 1.5f;
			if (cAzimuth > cAzimuthMax)
			{
				cAzimuth = cAzimuthMax;
			}
			if (cAzimuth < cAzimuthMin)
			{
				cAzimuth = cAzimuthMin;
			}
			
			updateCameraPos();
			
		}
	}
	
	private class ElevateCameraDownAction extends AbstractInputAction
	{
		@Override
		public void performAction(float time, Event e)
		{
			cElevation += 1.5f;
			
			if (cElevation > 40.0f)
			{
				cElevation = 40.0f;
			}
			if (cElevation < -40.0f)
			{
				cElevation = -40.0f;
			}
			
			updateCameraPos();
			
		}
	}
	
	private class ElevateCameraUpAction extends AbstractInputAction
	{
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
			
			updateCameraPos();
			
		}
	}
	
	private class ElevateOrbitAction extends AbstractInputAction
	{
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
			updateCameraPos();
		}
	}
	
	private class OrbitAction extends AbstractInputAction
	{
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
		
			updateCameraPos();
		}
	}
	
	private class OrbitRadiusAction extends AbstractInputAction
	{
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
			updateCameraPos();
		}
	}
	
	private class Yaw3PCameraLeftAction extends AbstractInputAction
	{
		public void performAction(float time, Event e)
		{
			cAzimuth += 1.5f;
			if (cAzimuth > cAzimuthMax)
			{
				cAzimuth = cAzimuthMax;
			}
			if (cAzimuth < cAzimuthMin)
			{
				cAzimuth = cAzimuthMin;
			}
			
			updateCameraPos();
			
		}
	}
	
	private class Yaw3PCameraRightAction extends AbstractInputAction
	{
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
			
			updateCameraPos();
		}

	}
	
	private class ZoomInAction extends AbstractInputAction
	{
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
			updateCameraPos();
			
		}
	}
	
	private class ZoomOutAction extends AbstractInputAction
	{
		public void performAction(float time, Event e)
		{
			radius -= 0.05f;
			
			//Check Bounds
			if (radius > 4.0f)
			{
				radius = 4.0f;
			}
			if (radius < 1.0f)
			{
				radius = 1.0f;
			}
			updateCameraPos();
			
		}
	}
	
	public void getCTarget()
	{
		System.out.println(cTarget);
	}
}
