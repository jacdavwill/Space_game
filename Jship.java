package com.mycompany.mygame;

import com.badlogic.gdx.graphics.*;
import java.util.*;
import com.badlogic.gdx.*;

public class Jship
{
	int x = 100;
	int y = 100;
	int screenWidth = 720;
	int screenHeight = 1280;
	int width = 64;
	int height = 64;
	int shipSpeed = 100;
	int posX = 0;
	int posY = 0;
	int bulletSpeed = 300;
	int timesFired = 0;
	
	double theta = 0;
	double pi = 3.14159;
	double xDelta = 0;
	double yDelta = 0;
	double ans = 0;
	double newX = -2;
	double newY = -2;
	double angle1 = 0;
	double angle2 = 0;
	double angle3 = 0;
	double xChange = 0;
	double yChange = 0;
	double sum = 0;
	double angle = 0;
	
	float time = 0;

	Texture pic;
	Texture outline;
	Texture bulletTexture;
	
	boolean collide = false;
	boolean anyCollide = false;
	boolean abort = false;
	boolean odd = true;
	
	double [][] collisionPoints = new double[][] {{.5, 1}, {0,.16},{1,.16},{.25,.795},{.75,.795},{0,.469},{1,.469}};
	
	Jbullet bullet;

	public Jship (Texture pic,Texture outline)
	{
		this.pic = pic;
		this.outline = outline;
		
		bulletTexture = new Texture(Gdx.files.internal("rocket.png"));
	}
	
	public double newAngle ()
	{
		angle += .01;
		return angle;
	}
	
	public Jbullet fire (Jship target)
	{
		timesFired ++;
		
		xChange = target.x + (target.width / 2) - x - (width / 2);
		yChange = target.y + (target.width / 2) - y - (width / 2);
		
		angle1 = arc("tan", xChange, yChange);
		angle2 = angle1 - target.theta + pi;
		angle3 = arc("sin", bulletSpeed / target.shipSpeed, Math.sin(angle2));
		
		bullet = new Jbullet(x + (width / 2), y + (width / 2), angle3 + angle1, bulletSpeed, bulletTexture);
		
		return bullet;
	}
	
	public void move (int xFinger, int yFinger)
	{
		if (x != xFinger || y != yFinger)
		{
		time = Gdx.graphics.getDeltaTime();
		
		timesFired ++;

		xDelta = xFinger - x;
		yDelta = yFinger - y;
		
		theta = arc("tan", xDelta, yDelta);
		
		x += Math.cos(theta) * time * shipSpeed;
		y += Math.sin(theta) * time * shipSpeed;
		}
	}
	
	public void moveToward (int xFinger, int yFinger, ArrayList<Jwall> walls)
	{
		time = Gdx.graphics.getDeltaTime();
		
		xDelta = xFinger - x;
		yDelta = yFinger - y;

		if (Math.abs(xDelta) > (shipSpeed * time) || Math.abs(yDelta) > (shipSpeed * time))
		{
			theta = arc("tan", xDelta, yDelta);

			newX = shipSpeed * Math.cos(theta) * time;
			newY = shipSpeed * Math.sin(theta) * time;
			
			for (Jwall wall : walls)
			{
				for (double [] point : collisionPoints)
				{
					if (wall.region.contains(x + newX + (point[0] * width), y + newY + (point[1] * height)))
					{
						collide = true;
						
						posX = x;
						posY = y;
						
						if ((point[0] * width) + x <= wall.x)
						{
							posX = (int) (wall.x - (width * point[0]) - 1);
							posY = (int) newY + y;
						}
						
						if ((point[0] * width) + x >= wall.x + wall.width)
						{
							posX = (int) (wall.x + wall.width - (width * point[0]) + 1);
							posY = (int) newY + y;
						}
						
						if ((point[1] * height) + y <= wall.y)
						{
							posY = (int) (wall.y - (height * point[1]) - 1);
							posX = (int) newX + x;
						}
						
						if ((point[1] * height) + y >= wall.y + wall.height)
						{
							posY = (int) (wall.y + wall.height - (height * point[1]) + 1);
							posX = (int) newX + x;
						}
						
						anyCollide = false;
						
						for (double [] point2 : collisionPoints)
						{
							if (wall.region.contains(posX + (point2[0] * width), posY + (point2[1] * height)))
							{
								anyCollide = true;
								break;
							}
						
						}
						
						if (!anyCollide)
						{
							abort = true;
							break;
						}
					}
				
					if(abort)
					{
						abort = false;
						
						for(Jwall wall2 : walls)
						{
							for (double [] point3 : collisionPoints)
							{
								if (wall2.region.contains(posX + point3[0] * width, posY + point3[1] * height))
								{
									abort = true;
									break;
								}
							}
							
							if (abort)
							{
								break;
							}
						}
						
						if (!abort)
						{
							x = posX;
							y = posY;
						}
					}
				}
			}
			
			if (!collide)
			{
				x += newX;
				y += newY;
			}
			
			collide = false;
		}
	}
	
	public double arc(String func, double deltaX, double deltaY)
	{
		if (deltaX == 0)
		{
			if (deltaY > 0)
			{
				return pi / 2;
			}else
			{
				return 3 * pi / 2;
			}
		}

		if (deltaY == 0)
		{
			if (deltaX > 0)
			{
				return 0;
			}else
			{
				return pi;
			}
		}
		
		if (func == "tan")
		{
			ans = Math.atan(deltaY / deltaX);
		}else if (func == "sin")
		{
			ans = Math.asin(deltaY / deltaX);
		}
		
		if (ans != 0)
		{
			if (deltaX > 0)
			{
				if (deltaY < 0)
				{
					ans += 2 * pi;
				}
			}else 
			{
				ans += pi;
			}
		}else
		{
			if (deltaY > 0)
			{
				ans = (pi / 2) - Math.atan(deltaX / deltaY);
			}else
			{
				ans = (3 * pi / 2) - Math.atan(deltaX / deltaY);
			}
		}

		return ans;
	}
}
