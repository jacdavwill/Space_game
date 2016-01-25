package com.mycompany.mygame;
import java.util.*;

public class Jregion
{
	int [][] recs = null;
	
	public Jregion (int [][] item)
	{
		recs = item;
	}
	
	public boolean contains(double x, double y)
	{
		for (int [] rec : recs)
		{
			if (x >= rec[0] && x <= rec[0] + rec[2] && y >= rec[1] && y <= rec[1] + rec[3])
			{
				return true;
			}
		}
		
		return false;
	}
}
