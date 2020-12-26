package Network;

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

import myGameEngine.NPC.NPCController;
import ray.networking.server.GameConnectionServer;
import ray.networking.server.IClientInfo;

public class GameServerUDP extends GameConnectionServer<UUID> 
{

	private int connectedClients = 0;
	private NPCController npcCtrl;
	private NPC[] npc;

	public GameServerUDP(int localPort, NPCController npcCtrl2) throws IOException 
	{
		super(localPort, ProtocolType.UDP);
		System.out.println("UDP server successfully initialized.");
		System.out.println("Server address: " + this.getLocalInetAddress());
		System.out.println("Server port: " + localPort);
		npcCtrl = npcCtrl2;
	}

	@Override
	public void processPacket(Object o, InetAddress senderIP, int sendPort) 
	{
		String message = (String) o;
		String[] messageTokens = message.split(",");
		UUID clientId = null;
		if (messageTokens.length > 0) {
			if (messageTokens[0].compareTo("join") == 0) 
			{
				try 
				{
					IClientInfo clientInfo;
					clientInfo = getServerSocket().createClientInfo(senderIP, sendPort);
					clientId = UUID.fromString(messageTokens[1]);
					addClient(clientInfo, clientId);
					sendJoinedMessage(clientId, true, connectedClients++);
				} catch (IOException e) 
				{
					e.printStackTrace();
				} finally 
				{
					System.out.println(clientId + " joined the server as client #" + connectedClients + ".");
				}
			}
			
			// create message: (create, localId, x, y, z)
			if (messageTokens[0].compareTo("create") == 0) 
			{
				clientId = UUID.fromString(messageTokens[1]);
				String[] pos = 
					{
						messageTokens[2], 
						messageTokens[3], 
						messageTokens[4]
					};
				sendCreateMessage(clientId, pos);
				sendWantsDetailsMessage(clientId);
			}
			
			// Player disconnect message
			if (messageTokens[0].compareTo("bye") == 0) 
			{
				clientId = UUID.fromString(messageTokens[1]);
				System.out.println(clientId + " has left the server.");
				sendByeMessage(clientId);
				removeClient(clientId);
				connectedClients--;
			}
		}
	}
	
	public void sendMoveMessage(UUID clientID, String[] position)
	{
		System.out.println("Sent");
		System.out.println(clientID);
		try
		{
			String message = new String("move," + clientID.toString());
		    message += "," + position[0];
		    message += "," + position[1];
		    message += "," + position[2];
		    forwardPacketToAll(message, clientID);
		}
		catch (IOException e) 
		{ 
		    e.printStackTrace();
		}
	}

	public void sendJoinedMessage(UUID clientId, boolean success, int clients) 
	{
		try {
			String message = "join,";
			if (success) {
				message += "success: " + clients;
			} else {
				message += "failed: " + clients;
			}
			sendPacket(message, clientId);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendCreateMessage(UUID clientID, String[] position)
	{ // format: create, remoteId, x, y, z
		try
		{
		    String message = new String("create," + clientID.toString());
		    message += "," + position[0];
		    message += "," + position[1];
		    message += "," + position[2];
		    forwardPacketToAll(message, clientID);
		}
		catch (IOException e) 
		{ 
		    e.printStackTrace();
		}
	}
	public void sendDetailsMessage(UUID clientID, UUID remoteid, String[] position)
	{
		try
		{
		    String message = new String("dsfr," + clientID.toString());
		    message += "," + position[0];
		    message += "," + position[1];
		    message += "," + position[2];
		    sendPacket(message, remoteid);
		}
		catch (IOException e) 
		{
		    e.printStackTrace();
		}
	}
	
	// Returns request for local status update
	public void sendWantsDetailsMessage(UUID clientId) 
	{
		try 
		{
			forwardPacketToAll("wants," + clientId.toString(), clientId);
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void sendByeMessage(UUID clientId) 
	{
		try 
		{
			forwardPacketToAll("Until next time, " + clientId.toString() + "!", clientId);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	public void sendNPCinfo()
//	{
//		npc = npcCtrl.getClass();
//		for (int i = 0; i < npcCtrl.getNumberOfNPCs(); i++)
//		{
//			try
//			{
//				String message = new String("mnpc," + Integer.toString(i));
//				message += "," + (npc[i].getX());
//				message += "," + (npc[i].getY());
//				message += "," + (npc[i].getZ());
//				sendPacketToAll(message);
//			}
//			catch (IOException e)
//			{
//				e.printStackTrace();
//			}
//		}
//	}

}
