package com.mycompany.mygame;

public class Jwall
{
	int width;
	int height;
	int x;
	int y;
	
	Jregion region = null;;
	
	public Jwall (int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		region = new Jregion(new int [][] {{x,y,width,height}});
	}
}
