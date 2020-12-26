package myGameEngine.Player;

import ray.rage.Engine;
import ray.rage.scene.Node;
import ray.rage.scene.SceneManager;
import ray.rage.scene.SceneNode;
import ray.rage.scene.SceneObject;
import ray.rml.Matrix3;
import ray.rml.Matrix4;
import ray.rml.Vector3;
import ray.rml.Angle;
import ray.physics.PhysicsObject;

public class Player 
{
	private Engine e;
	private SceneManager sm;
	
	public Player(Engine e, SceneManager sm)
	{
		this.e = e;
		this.sm = sm;
	}
}
