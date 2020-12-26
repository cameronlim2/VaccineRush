package myGameEngine.NPC;

import ray.ai.behaviortrees.BTCompositeType;
import ray.ai.behaviortrees.BTSequence;
import ray.ai.behaviortrees.BehaviorTree;
import ray.ai.behaviortrees.BTCondition;
import ray.ai.behaviortrees.BTStatus;
import ray.ai.behaviortrees.BTAction;
import ray.rml.Angle;
import ray.rml.Degreef;
import ray.rml.Vector3;
import ray.rml.Vector3f;
import ray.rage.scene.SceneNode;

import vaccineRush.VaccineRush;

import java.util.Random;
import java.util.Vector;
import java.lang.*;
import java.io.IOException;


public class NPCController 
{
	private int numberOfMonkeys = 10;
	private NPC[] npcs = new NPC[numberOfMonkeys];
	Random rand = new Random();
	
	BehaviorTree[] behaviorTreeMonkey = { new BehaviorTree(BTCompositeType.SELECTOR) };
	
	private float thinkStartTime, tickStateTime, lastThinkUpdateTime, lastTickUpdateTime;
	private VaccineRush game;
	private SceneNode[] npcNodes;
	
	public NPCController(VaccineRush g, SceneNode[] nodes)
	{
		
	}
}
