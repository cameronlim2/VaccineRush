package Network;

import java.io.IOException;

import myGameEngine.NPC.NPCController;
import ray.networking.IGameConnection.ProtocolType;

public class NetworkingServer 
{
	private GameServerUDP thisUDPServer;
	private NPCController npcCtrl;
	private long lastUpdateTime;

	public NetworkingServer(int port, String protocol)
	{
		long startTime = System.nanoTime();
		lastUpdateTime = startTime;
		//npcCtrl = new NPCController();
		try
		{
			thisUDPServer = new GameServerUDP(port, npcCtrl);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	//	npcCtrl.setupNPC();
//		npcLoop();
	}
	
//	public void npcLoop()
//	{
//		while (true)
//		{
//			long frameStartTime = System.nanoTime();
//			float elapMilSecs = (frameStartTime-lastUpdateTime)/(1000000.0f);
//			if(elapMilSecs >= 50.0f)
//			{
//				lastUpdateTime = frameStartTime;
//			//	npcCtrl.updateNPCs();
//				thisUDPServer.sendNPCinfo();
//			}
//			Thread.yield();
//		}
//	}
	
	public static void main(String[] args)
	{
		if (args.length > 1)
		{
			NetworkingServer app = new NetworkingServer(Integer.parseInt(args[0]), args[1]);
		}
	}
}
