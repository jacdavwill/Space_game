package com.mycompany.mygame;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.*;

public class Jbullet
{
	double angle;
	
	boolean expired;
	
	int x = 0;
	int y = 0;
	int speed = 0;
	int width = 25;
	int height = 25;
	
	float time = 0;
	
	Texture bulletTexture;
	
	public Jbullet(int x, int y, double angle, int speed, Texture bulletTexture)
	{
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.speed = speed;
		this.bulletTexture = bulletTexture;
	}
	
	public void move()
	{
		time = Gdx.graphics.getDeltaTime();
		
		x += Math.cos(angle) * speed * time;
		y += Math.sin(angle) * speed * time;
	}
}
