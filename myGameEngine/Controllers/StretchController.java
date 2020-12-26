package myGameEngine.Controllers;

import ray.rage.scene.controllers.AbstractController;
import ray.rage.scene.Node;
import ray.rml.Vector3;
import ray.rml.Vector3f;

public class StretchController extends AbstractController
{
	private float lifeCycle = 1000.0f;
	private float totalTime = 0.0f;
	private float growthRate = .01f;
	private float growth;
	private float direction = 1.0f;
	
	@Override
	protected void updateImpl(float elapsedTime)
	{
		totalTime += elapsedTime;
		
		growth = 1.0f + (growthRate * direction);
		if (totalTime > lifeCycle)
		{
			// Reverse size growth (Large --> Small)
			direction = -direction;
			totalTime = 0.0f;
		}
		for (Node node : super.controlledNodesList)
		{
			Vector3 scale = node.getLocalScale();
			scale = Vector3f.createFrom(scale.x() * growth, scale.y(), scale.z() * growth);
			node.setLocalScale(scale);
		}
	}
}
