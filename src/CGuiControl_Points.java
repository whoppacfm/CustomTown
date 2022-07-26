package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class CGuiControl_Points {
	
	int controlX;
	int controlY;
	int pcount;
	float value;
	private Boolean bShowButtons;
	private Boolean bBeginShapeRenderer;
	private int pointSize;
	ShapeRenderer shapeRenderer;
	SpriteBatch spriteBatch;
	CTown town;
	float valuevalue;
	
	public CGuiControl_Points(CTown town, int x, int y, int pointcount, float startingvalue, ShapeRenderer sr, SpriteBatch sb)
	{
		valuevalue=0.03f;
		this.town=town;
		controlX=x;
		controlY=y;
		pcount=pointcount;
		shapeRenderer=sr;
		spriteBatch=sb;
		value=startingvalue;
		bShowButtons=true;
		bBeginShapeRenderer=true;
		pointSize=20;
	}	

	public void setPosition(int x, int y)
	{
		controlX=x;
		controlY=y;
	}
	
	public void setPointSize(int size)
	{
		pointSize=size;
	}
	
	public void setBeginShapeRenderer(Boolean beginShaperenderer)
	{
		bBeginShapeRenderer=beginShaperenderer;
	}
	
	public void showButtons(Boolean show)
	{
		bShowButtons=show;
	}
	
	public void setValue(float v)
	{
		value=v;
	}

	public float getValue()
	{
		return value;
	}
	
	public void render(int x, int y)
	{
		if(bBeginShapeRenderer)
		{
			//if(!Gdx.gl.glIsEnabled(GL30.GL_BLEND))
			{
				Gdx.gl.glEnable(GL30.GL_BLEND);
				Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
			}		    Gdx.gl.glEnable(GL20.GL_LINEAR_MIPMAP_LINEAR);
		    shapeRenderer.setAutoShapeType(true);
		    shapeRenderer.begin();
		}
	    
	    int size=pointSize;
	    float tempValue=value;
	    
	    for(int i=0;i<pcount;i++)
	    {
		    shapeRenderer.set(ShapeType.Line);
	    	shapeRenderer.setColor(1,1,1,0.6f);
	    	shapeRenderer.rect(controlX+i*size*1.2f, controlY, size, size);
	    	
	    	float w=size;
	    	if(tempValue>1)
	    		tempValue-=1;
	    	else
	    	{
	    		w=size*tempValue;
	    		tempValue=0;
	    	}
	    	
	    	shapeRenderer.set(ShapeType.Filled);
	    	shapeRenderer.setColor(1,1,1,0.5f);
	    	shapeRenderer.rect(controlX+i*size*1.2f, controlY, w, size);
	    	
	    	if(value>=i+1)
	    	{
		    	shapeRenderer.set(ShapeType.Filled);
		    	shapeRenderer.setColor(0.9f,0.9f,0.9f,1f);
		    	shapeRenderer.rect(controlX+i*size*1.2f+1, controlY+1, w-3, size-3);
	    	}
	    }
	    
	    if(bBeginShapeRenderer)
	    {
			shapeRenderer.end();
			//Gdx.gl.glDisable(GL30.GL_BLEND);
	    }
		
		if(bShowButtons)
		{
			spriteBatch.begin();
			spriteBatch.setColor(1,1,1,0.9f);
			
			float psize=size/1.1f;
			float msize=size/1.1f;
			
			float xplus=controlX+size*pcount*1.2f+1.5f-3;
			float yplus=controlY+1-0.6f;
			
			if(x>xplus && x<xplus+psize && y>yplus && y<yplus+psize)
			{
				psize+=1;
				
				if(Gdx.input.isButtonPressed(0))
				{
					psize-=1;
					value+=valuevalue;
					if(value>pcount)
						value=pcount;
				}
			}
			
			spriteBatch.draw(town.gameResourceConfig.textures.get("gui_plus"), xplus, yplus, psize, psize);
			
			float xminus=controlX-size/1.5f-7;
			float yminus=controlY+1-0.6f;
			if(x>xminus && x<xminus+msize && y>yminus && y<yminus+msize)
			{
				msize+=1;
				if(Gdx.input.isButtonPressed(0))
				{
					msize-=1;
					value-=valuevalue;
					if(value<0)
						value=0;
				}
				
			}
			
			spriteBatch.draw(town.gameResourceConfig.textures.get("gui_minus"), xminus, yminus, msize, msize);
			spriteBatch.end();
		}
	}
	
}

