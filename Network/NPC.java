package Network;

import java.util.Random;

public class NPC 
{
	double locX, locY, locZ;
	
	Random r = new Random();
	
	public double getX()
	{
		return locX;
	}
	
	public double getY()
	{
		return locY;
	}
	
	public double getZ()
	{
		return locZ;
	}
	
	public void randomLocation(int x, int y, int z)
	{
		locX = x;
		locY = y;
		locZ = z;
	}
	
	public void updateLocation()
	{
		
	}
}
