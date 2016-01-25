package com.mycompany.mygame;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.mycompany.mygame.*;
import java.util.*;

public class MyGdxGame implements ApplicationListener
{
	Texture shipTexture;
	Texture ship2Texture;
	Texture ship3Texture;
	Texture shipOutlineTexture;
	Texture wallTexture;
	Texture bulletTexture;

	SpriteBatch batch;

	OrthographicCamera camera;

	BitmapFont font;

	Jship ship1;
	Jship selectedShip;
	Jenemy enemy1;

	Jwall wall1;
	Jwall wall2;
	Jwall wall3;
	Jwall wall4;
	Jwall wall5;
	Jwall wall6;
	Jwall wall7;
	Jwall wall8;

	int width = 720; // in pix
	int height = 1280;
	int gameWidth = 2000;
	int gameHeight = 2000;
	int shiftSpeed = 12;

	boolean start = true;

	ArrayList<Jship> allShips = new ArrayList<>();
	ArrayList<Jwall> allWalls = new ArrayList<>();
	ArrayList<Jenemy> allEnemys = new ArrayList<>();
	ArrayList<Jbullet> allBullets = new ArrayList<>();

	@Override
	public void create()
	{
		shipTexture = new Texture(Gdx.files.internal("ship.png"));
		ship2Texture = new Texture(Gdx.files.internal("triangle.png"));
		ship3Texture = new Texture(Gdx.files.internal("ball.png"));
		shipOutlineTexture = new Texture(Gdx.files.internal("outline_2.png"));
		bulletTexture = new Texture(Gdx.files.internal("rocket.png"));

		wallTexture = new Texture(Gdx.files.internal("square.png"));

		batch = new SpriteBatch();

		font = new BitmapFont();

		camera = new OrthographicCamera();

		ship1 = new Jship(shipTexture, shipOutlineTexture);

		enemy1 = new Jenemy(ship2Texture, shipOutlineTexture);

		wall1 = new Jwall(150,700,200,100);
		wall2 = new Jwall(350,500,100,200);
		wall3 = new Jwall(350,800,100,200);
		wall4 = new Jwall(450,700,200,100);
		wall5 = new Jwall(0,0,20,2000);
		wall6 = new Jwall(0,0,2000,20);
		wall7 = new Jwall(0,1980,2000,20);
		wall8 = new Jwall(1980,0,20,2000);

		allShips.add(ship1);
		allEnemys.add(enemy1);
		allWalls.add(wall1);
		allWalls.add(wall2);
		allWalls.add(wall3);
		allWalls.add(wall4);
		allWalls.add(wall5);
		allWalls.add(wall6);
		allWalls.add(wall7);
		allWalls.add(wall8);

		configureCamera();

		enemy1.x = 700;
		enemy1.y = 1000;
	}

	private void configureCamera()
	{
		if (Gdx.graphics.getHeight() < Gdx.graphics.getWidth())
		{
			camera.setToOrtho(false, 1280, 720);
		}else
		{
			camera.setToOrtho(false, 720, 1280);
		}
	}

	@Override
	public void render()
	{   
		enemy1.x -= 2;
		enemy1.y -= 2;

		if (start)
		{
			start = false;

			allBullets.add(ship1.fire(enemy1));
		}

		if (Gdx.input.isTouched())
		{
			if (selectedShip == null)
			{
				for (Jship ship : allShips)
				{
					if ((Gdx.input.getX() + camera.position.x - (width / 2)) > (ship.x - 40) && (Gdx.input.getX() + camera.position.x - (width / 2)) < (ship.x + ship.width + 40) && (height - Gdx.input.getY() + camera.position.y - (height / 2)) > (ship.y - 40) && (height - Gdx.input.getY() + camera.position.y - (height / 2)) < (ship.y + ship.height + 40))
					{
						selectedShip = ship;
						break;
					}
				}
			}

			if (selectedShip != null)
			{
				selectedShip.moveToward((int) (Gdx.input.getX() + camera.position.x - (width / 2)),(int) (height - Gdx.input.getY() + camera.position.y - (height / 2)), allWalls);
			}

			if(Gdx.input.getX() <= 100 && camera.position.x >= (width / 2) + shiftSpeed)
			{
				camera.position.x -= shiftSpeed;
			}

			if (Gdx.input.getX() >= width - 100 && camera.position.x <= gameWidth - (width / 2) - shiftSpeed)
			{
				camera.position.x += shiftSpeed;
			}

			if(Gdx.input.getY() <= 100 && camera.position.y <= gameHeight - (height / 2) - shiftSpeed)
			{
				camera.position.y += shiftSpeed;
			}

			if(Gdx.input.getY() >= height - 100 && camera.position.y >= (height / 2) + shiftSpeed)
			{
				camera.position.y -= shiftSpeed;
			}

		}else
		{
			selectedShip = null;
		}

		Gdx.gl.glClearColor(0, 0, 0, 1);
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		for (Jwall wall : allWalls)
		{
			batch.draw(wallTexture, (float) wall.x, (float) wall.y, (float) wall.width, (float) wall.height);
		}

		for (Jship ship : allShips)
		{
			if (ship != selectedShip)
			{
				batch.draw(ship.pic, (float) ship.x, (float) ship.y, (float) (ship.width / 2), (float) (ship.height / 2), (float) ship.width, (float) ship.height, 1f, 1f, 0, 0, 0, 512, 512, false, false);
			}
		}

		for (Jbullet bullet : allBullets)
		{
			bullet.move();
			batch.draw(bullet.bulletTexture, (float) bullet.x, (float) bullet.y, (float) bullet.width / 2, (float) bullet.height / 2, (float) bullet.width, (float) bullet.height, 1f, 1f, 0, 0, 0, 512, 512, false, false);
		}

		if (selectedShip != null)
		{
			batch.draw(selectedShip.pic, (float) selectedShip.x, (float) selectedShip.y, (float) (selectedShip.width / 2), (float) (selectedShip.height / 2), (float) selectedShip.width, (float) selectedShip.height, 1f, 1f, 0, 0, 0, 512, 512, false, false);
			batch.draw(selectedShip.outline, (float) (selectedShip.x - 20), (float) (selectedShip.y - 20), (float) ((selectedShip.width / 2) + 20), (float) ((selectedShip.height / 2) + 20), (float) selectedShip.width + 40, (float) selectedShip.height + 40, 1f, 1f, 0, 0, 0, 522, 522, false, false);
		}

		if(Gdx.input.isTouched())
		{
			font.draw(batch, "X: " + (Gdx.input.getX() + camera.position.x - 360), camera.position.x - 350, camera.position.y - 600);
			font.draw(batch, "Y: " + (height - Gdx.input.getY() + camera.position.y - 680), camera.position.x - 350, camera.position.y - 615);
		}

		batch.draw(enemy1.pic, (float) enemy1.x, (float) enemy1.y, (float) (enemy1.width / 2), (float) (enemy1.height / 2), (float) enemy1.width, (float) enemy1.height, 1f, 1f, 0, 0, 0, 512, 512, false, false);

		batch.end();
	}

	@Override
	public void dispose()
	{
		batch.dispose();
	}

	@Override
	public void resize(int p1, int p2)
	{
		configureCamera();
	}

	@Override
	public void pause()
	{
	}

	@Override
	public void resume()
	{
	}

} 
