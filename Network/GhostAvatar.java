package Network;

import java.util.UUID;

import ray.rage.scene.SceneNode;
import ray.rage.scene.Entity;
import ray.rml.*;

public class GhostAvatar 
{
	private UUID id;
	private SceneNode node;
	private Vector3 pos;
	private Entity entity;
	
	public GhostAvatar(UUID id, Vector3 pos)
	{
		this.id = id;
		this.pos = pos;
	}
	
	public void setNode(SceneNode ghostN)
	{
		node = ghostN;
	}
	
	public void setPos(Vector3 ghostN)
	{
		node.setLocalPosition(ghostN);
	}
	
	public void setEntity(Entity ghostE)
	{
		entity = ghostE;
	}
	
	public UUID getID()
	{
		return id;
	}
	
	public SceneNode getSceneNode()
	{
		return node;
	}
	
	public Vector3 getPosition()
	{
		return pos;
	}
	
	public Entity getEntity()
	{
		return entity;
	}
}
