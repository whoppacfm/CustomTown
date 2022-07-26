package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class CInfoTextEvent {
	
	String showText;
	String showText2;
	float showTimer;
	float stayTimer;
	
	public float infoSize;
	
	Color textColor;
	private Boolean isFinished;
	private CTown town;
	SpriteBatch spriteBatch;
	ShapeRenderer sr;
	float sizeValue;
	CWorldObject targetObject;
	public float moveduration=2;
	public float stayduration=8;
	public int posy;
	public float deleteTimer;
	public Boolean bRemove;
	public int eventid;
	
	public CInfoTextEvent(CTown t, String text, String text2, Color color)
	{
		infoSize=1;
		//posy=Gdx.graphics.getHeight();
		posy=-100;
		
		textColor=color;
		town=t;
		showText=text.toUpperCase();
		showText2=text2.toUpperCase();
		showTimer=0;
		stayTimer=0;
		isFinished=false;
		spriteBatch=t.gameGui.editorSpriteBatch;
		sr=t.gameGui.shapeRenderer;
		deleteTimer=120;
		bRemove=false;
		generateEventId();
	}
	
	public CInfoTextEvent(CTown t, String text, Color color)
	{
		//posy=Gdx.graphics.getHeight();
		infoSize=1;
		posy=-100;
		
		textColor=color;
		town=t;
		showText=text.toUpperCase();
		showText2="";
		showTimer=0;
		stayTimer=0;
		isFinished=false;
		spriteBatch=t.gameGui.editorSpriteBatch;
		sr=t.gameGui.shapeRenderer;
		deleteTimer=120;
		bRemove=false;
		generateEventId();
	}
	
	private void generateEventId()
	{
		eventid = 1+town.gameWorld.rand.nextInt(10000);
	}
	
	public void setDelTimer()
	{
		deleteTimer-=Gdx.graphics.getDeltaTime();
		//Gdx.app.debug("", ""+deleteTimer);
		if(deleteTimer<0)
		{
			deleteTimer=0;
			bRemove=true;
		}
	}
	
	public void render()
	{
		//Gdx.app.debug("1", ""+showText);
		if(1==1)
			return; //Diese Funktion führt zu Abstürzen
		
		
		
		if(posy<0)
		{
			if(town.gameWorld.infoEventMoving==eventid)
				town.gameWorld.infoEventMoving=0;
			
			return;
		}
		
		//Gdx.app.debug("", "test_2");
		//Gdx.app.debug("2", ""+showText + ", moving event id: " + town.gameWorld.infoEventMoving + ", this event id: " + eventid);
		
		if(town.gameWorld.infoEventMoving==0)
			town.gameWorld.infoEventMoving=eventid;
				
		if(town.gameWorld.infoEventMoving!=eventid)
			return;
		
		//Gdx.app.debug("3", ""+showText);
		
		//Set Position
		
		//Set Font
		spriteBatch.setShader(town.gameFont.fontShader);
		
		if(infoSize==1) {
			town.gameFont.bfArial.getData().setScale(0.7f);
		}
		else if (infoSize==2) {
			town.gameFont.bfArial.getData().setScale(0.9f);
		}
		
		town.gameFont.bfArial.setColor(textColor.r, textColor.g, textColor.b, 0.7f);
		
		if(textColor.r>0.5f && textColor.g<0.5f && textColor.b<0.5f) //rot muss stärker dargestellt werden
			town.gameFont.bfArial.setColor(textColor.r, textColor.g, textColor.b, 0.8f);
		
		town.gameFont.layout.setText(town.gameFont.bfArial, showText);
		int x=Math.round(Gdx.graphics.getWidth()/2-town.gameFont.layout.width/2);
		int width1=(int) town.gameFont.layout.width;
		
		town.gameFont.layout.setText(town.gameFont.bfArial, showText2);
		int x2=Math.round(Gdx.graphics.getWidth()/2-town.gameFont.layout.width/2);
		int width2=(int) town.gameFont.layout.width;
		
		if(width2>width1)
		{
			width1=width2;
			x=x2;
		}
				
		sr.setAutoShapeType(true);

		if(!Gdx.gl.glIsEnabled(GL30.GL_BLEND))
		{
			Gdx.gl.glEnable(GL30.GL_BLEND);
			Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
		}
		sr.begin();
			sr.setColor(town.dialogColor.r-0.1f,town.dialogColor.g-0.1f,town.dialogColor.b-0.1f, 0.7f);
			sr.set(ShapeType.Filled);
			
			int height = (int)town.gameFont.layout.height;
			
			if(!showText2.isEmpty())
				height=(int) (town.gameFont.layout.height*2+20);
			
			if(!showText2.isEmpty())
			{
				sr.rect(x-10, posy-height/2, width1+20, height+20);
			}
			else
			{
				sr.rect(x-10, posy-height-11, width1+20, height+20);
			}
			
		sr.end();
		//Gdx.gl.glDisable(GL30.GL_BLEND);
		
		spriteBatch.begin();
		town.gameFont.bfArial.draw(spriteBatch, showText, x, posy);
		if(!showText2.isEmpty())
			town.gameFont.bfArial.draw(spriteBatch, showText2, x2, posy + town.gameFont.layout.height + 20);
		
		spriteBatch.end();
		spriteBatch.setShader(null);
		
		showTimer+=CHelper.getDeltaSeconds(town);
		
		//if(showTimer>0.05f)
		{
			showTimer=0;
			posy-=CHelper.getFPSValue(3);
			//posy-=1000;
		}
	}
	
	public Boolean isFinished()
	{
		return isFinished;
	}
}
