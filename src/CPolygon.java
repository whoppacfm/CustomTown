package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CPolygon {

	public PolygonSprite poly;
	public PolygonSpriteBatch polyBatch;
	public Texture textureSolid;
	public Pixmap pix;
 		
	//Usage:
//	CPolygon poly = new CPolygon();
//	poly.initTriangle();
//	poly.setPosition(500, Gdx.graphics.getHeight()-80);
//	poly.render();
	
	public void initTriangle()
	{
		polyBatch = new PolygonSpriteBatch();
		pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		pix.setColor(Color.WHITE);
		pix.fill();
		textureSolid = new Texture(pix);
		float polysize=0.2f;
		PolygonRegion polyReg = new PolygonRegion(new TextureRegion(textureSolid), new float[] { 0, 0, 70*polysize, 50*polysize, 0, 100*polysize }, new short[] { 0, 1, 2 });
		poly = new PolygonSprite(polyReg);
		//poly.setOrigin(400, 500);
		polyBatch = new PolygonSpriteBatch();
	}	

	public void scale(float a)
	{
		poly.scale(a);	
	}
	
	public void setPosition(float x, float y)
	{
		poly.setPosition(x, y);
	}

	public float getX()
	{
		return poly.getX();
	}

	public float getY()
	{
		return poly.getY();
	}
	
	public void setColor(Color color)
	{
		//poly.setColor(1,1,1,0.5f);
		poly.setColor(1,1,1,1f);
	}
	
	public void render()
	{
		polyBatch.begin();
			poly.draw(polyBatch, 1f);
		polyBatch.end();
	}
}
