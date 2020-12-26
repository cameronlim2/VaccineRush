package myGameEngine.NPC;

import ray.rage.asset.texture.Texture;
import ray.rage.rendersystem.Renderable.Primitive;
import ray.rage.rendersystem.states.RenderState;
import ray.rage.rendersystem.states.TextureState;
import ray.rage.scene.SceneManager;
import ray.rage.scene.SceneNode;
import ray.rage.scene.SkeletalEntity;
import ray.rml.Vector3;
import ray.rml.Vector3f;
import vaccineRush.VaccineRush;

import java.io.IOException;
import java.util.Random;

public class NPC 
{
	
	VaccineRush game;
	SceneNode npcAvatar;
	SceneManager sm;
	private int npcId;
	private float delayTime = 0, x, y, z;
	private float[] origin;
	private boolean chasing = false, active = false, isWalking = false, isDelayed = false, isDelaying = false;
	
	private Random rand = new Random();
	
	public NPC(VaccineRush game, float x, float y, float z, int id, SceneManager sm) throws IOException
	{
		npcId = id;
		this.game = game;
		this.x = x;
		this.y = y;
		this.z = z;
		this.sm = sm;
		createNPC(-47.86f, 0, 63.44f, sm);
		
	}
	
	public void delay(float time)
	{
		if (getIsDelaying() == true)
		{
			setIsDelaying(false);
			setIsDelayed(true);
		}
		if(getIsDelayed() == true && getIsDelaying() == false)
		{
			setDelayTime(game.getElapsedTime());
			setIsDelaying(true);
		}
	}
	
	public void updateLocation()
	{
		if (getDistanceBetween(Vector3f.createFrom(this.getX(), this.getY(), this.getZ()), game.avatarN.getLocalPosition()) < 20.0f && getDistanceBetween(Vector3f.createFrom(this.getX(), this.getY(), this.getZ()), Vector3f.createFrom(origin[0], origin[1], origin[2])) < 20.0f)
		{
			
		}
	}
	
	public void createNPC(float x, float y, float z, SceneManager sm) throws IOException
	{
		 	SkeletalEntity npcE = game.sm.createSkeletalEntity("npc" + npcId, "morgan.rkm", "morgan.rks");
	        Texture monkeyT = game.sm.getTextureManager().getAssetByPath("morgan.jpeg");
	        TextureState monkeyTS = (TextureState) game.sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
	        monkeyTS.setTexture(monkeyT);
	        npcE.setRenderState(monkeyTS); 
	        npcE.setPrimitive(Primitive.TRIANGLES);
	        SceneNode npcN = game.sm.getRootSceneNode().createChildSceneNode(npcE.getName() + "Node");
	        npcN.scale(.1f,.1f,.1f);
	        npcN.attachObject(npcE);
	        npcN.moveLeft(1.0f);
	        npcN.setLocalPosition(x, y ,z);
	        
	        npcE.loadAnimation("bladeAction", "blade.rka");
	        npcE.loadAnimation("walkAction", "walk.rka");
	}
	
	//Getters
	
	public float[] getNpcLocation()
	{
		float[] loc = {x, y, z};
		return loc;
	}
	
	public SceneNode getNpcSceneNode()
	{
		return this.npcAvatar;
	}
	
	public boolean getIsDelaying()
	{
		return this.isDelaying;
	}
	
	public boolean getIsDelayed()
	{
		return this.isDelayed;
	}
	
	public boolean getIsWalking()
	{
		return this.isWalking;
	}
	
	public float getDelayTime()
	{
		return this.delayTime;
	}
	
	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}
	
	public float getZ()
	{
		return z;
	}

	//Setters
	
	public void setIsDelaying(boolean answer)
	{
		this.isDelaying = answer;
	}
	
	public void setIsDelayed(boolean answer)
	{
		this.isDelayed = answer;
	}

	
	public void setIsWalking(boolean answer)
	{
		this.isWalking = answer;
	}
	
	public void setDelayTime(float answer)
	{
		this.delayTime = answer;
	}
	
	public float getDistanceBetween(Vector3 a, Vector3 b)
	{
		return (float)Math.sqrt(Math.pow(a.x() - b.x(), 2) + Math.pow(a.y() - b.y(), 2) + Math.pow(a.z() - b.z(), 2));
	}
	
	private void setX(float x)
	{
		this.x = x;
	}
	
	private void setY(float y)
	{
		this.y = y;
	}
	
	private void setZ(float z)
	{
		this.z = z;
	}
	
	
}
