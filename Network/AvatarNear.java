//package Network;
//
//import myGameEngine.NPC.NPCController;
//import ray.ai.behaviortrees.BTCondition;
//
//public class AvatarNear extends BTCondition
//{
//	private GameServerUDP server;
//	private NPCController npcc;
//	private NPC npc;
//	
//	public AvatarNear(GameServerUDP s, NPCController c, NPC n, boolean toNegate)
//	{
//		super(toNegate);
//		server = s;
//		npcc = c;
//		npc = n;
//	}
//	
//	protected boolean check()
//	{
////		server.sendCheckForAvatarNear();
////		return npcc.getNearFlag();
//		return true;
//	}
//}
