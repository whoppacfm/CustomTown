package com.mygdx.game;

import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class CAnimationTextEvent {

	public static enum AnimationEventType {
		MONEY, HAPPINESS, HEALTH, INTELLIGENCE, FITNESS, EDUCATION, MOVEHOUSE, TEXT, RESEARCH, WORKOUTPUT, MAINTAIN, COMFORT
	}
	
	int x;
	int y;
	private AnimationEventType animEventType;
	float showValue; //show money animation
	String showText;
	String showValueText;
	float showTimer;
	private Boolean isFinished;
	private CTown town;
	SpriteBatch spriteBatch;
	float sizeValue;
	CWorldObject targetObject;
	int iFast;
	
	public CAnimationTextEvent(CTown t, int showX, int showY, float showValue, AnimationEventType eventType, float size)
	{
		town=t;
		animEventType=eventType;
		x=showX;
		y=showY;
		showText="";
		showValueText="";
		this.showValue=showValue;
		showValueText=""+Math.abs(this.showValue);
		iFast=0;
		
		if(Math.abs(this.showValue)>9)
			showValueText=""+Math.round(Math.abs(this.showValue)); //ab 10 keine nachkommastellen mehr anzeigen
		else if(Math.abs(this.showValue)==Math.round(Math.abs(this.showValue))) //wenn nach komma==0 dann nicht anzeigen
			showValueText=""+Math.round(Math.abs(this.showValue));
		else
			showValueText = CHelper.roundFloat(Math.abs(showValue), 2)+""; //2 nachkommastellen
		
		isFinished=false;
		//spriteBatch=t.gameWorld.worldSpriteBatch;
		spriteBatch=t.gameGui.editorSpriteBatch;
		this.sizeValue=size;
		targetObject=null;
	}
	
	public void render()
	{
		//Auf X2 und X4 keine nur Money Animations anzeigen, die anderen sind zu performance lastig
		//nein -> performance einbruch liegt am fitness studio
		//if(town.gameGui.buttonX2.toggleActive || town.gameGui.buttonX4.toggleActive)
		//{
		//	if(animEventType!=AnimationEventType.MONEY && animEventType!=AnimationEventType.TEXT)
		//	{
		//		isFinished=true;
		//		return;
		//	}
		//}
		
		spriteBatch.begin();
		
		float animSize=sizeValue;
		
		if(animEventType==AnimationEventType.MONEY)
			animSize=4/town.gameCam.zoom;
		else
			animSize=4/town.gameCam.zoom;

		if(animEventType==AnimationEventType.TEXT)
			animSize=4/town.gameCam.zoom;
		
		if(animSize>3)
			animSize=3;
		if(animSize<1)
			animSize=1;
		
		if(targetObject!=null)
		{
			x=targetObject.pos_x()+targetObject.width/2;
			y=targetObject.pos_y()+targetObject.height/2;
			
			Vector3 wcoord = new Vector3();
			wcoord.x=x;
			wcoord.y=y;
			wcoord = town.gameCam.project(wcoord);
			x=(int) wcoord.x;
			y=(int) wcoord.y;
		}
		
		if((showValue>0 || showValue<0 || !showText.isEmpty()) && showTimer<=0)
		{
			showTimer=1f;
		}
		
		if(showTimer>0 && (showValue>0 || showValue<0 || !showText.isEmpty()))
		{
			//Show Money
			if(animEventType==AnimationEventType.MONEY)
			{
				String stat="+";
				if(showValue<0)
					stat="-";
				
				town.gameFont.bfArial.setColor(Color.GOLD);
				
				//				if(showValue>0)
				//					town.gameFont.bfArial.setColor(Color.YELLOW);
				//				else if(showValue<0)
				//				{
				//					town.gameFont.bfArial.setColor(Color.RED);
				//					stat="-";
				//				}
				
				spriteBatch.setShader(town.gameFont.fontShader);
				town.gameFont.bfArial.getData().setScale(showTimer*animSize);
				spriteBatch.setShader(town.gameFont.fontShader);
				town.gameFont.bfArial.draw(spriteBatch, stat+"$" + Math.abs(Math.round(showValue)), x, y);
				
				spriteBatch.setShader(null);
				town.gameFont.bfArial.setColor(0.8f,0.8f,0.8f,0.8f);
				town.gameFont.bfArial.getData().setScale(1);
				spriteBatch.setShader(null);
			}
			else if(animEventType==AnimationEventType.TEXT)
			{
				town.gameFont.bfArial.setColor(Color.YELLOW);
				
				spriteBatch.setShader(town.gameFont.fontShader);
				town.gameFont.bfArial.getData().setScale(showTimer*animSize);
				spriteBatch.setShader(town.gameFont.fontShader);
				town.gameFont.bfArial.draw(spriteBatch, showText, x, y);
				
				spriteBatch.setShader(null);
				town.gameFont.bfArial.setColor(0.8f,0.8f,0.8f,0.8f);
				town.gameFont.bfArial.getData().setScale(1);
				spriteBatch.setShader(null);
			}
			else //Show Icon Anims
			{
				//spriteBatch.begin();
				
				String stat="+";
				
				if(showValue>0)
				{
					town.gameFont.bfArial.setColor(0.8f,0.8f,0.8f,0.8f);
					spriteBatch.setColor(0.8f,0.8f,0.8f,0.8f);
				}
				else if(showValue<0)
				{
					town.gameFont.bfArial.setColor(Color.RED);
					spriteBatch.setColor(1,0,0,0.8f);
					stat="-";
					
					if(animEventType == AnimationEventType.WORKOUTPUT)
					{
						//town.gameFont.bfArial.setColor(Color.BLUE);
						town.gameFont.bfArial.setColor(0,0,1f,0.7f);
						spriteBatch.setColor(0,0,1f,0.7f);
					}
				}
				
				//float iconsize=300*showTimer;
				float iconsize=50*showTimer*animSize;
				
				town.gameFont.bfArial.getData().setScale(showTimer*animSize);
				spriteBatch.setShader(town.gameFont.fontShader);
				
				String sshow=""+Math.abs(showValue);
				if(!showValueText.isEmpty())
					sshow=showValueText;
				town.gameFont.bfArial.draw(spriteBatch, stat+sshow, x+iconsize, y);
				town.gameFont.bfArial.draw(spriteBatch, stat, x+iconsize, y);
				spriteBatch.setShader(null);

				//Reset
				town.gameFont.bfArial.setColor(0.8f,0.8f,0.8f,0.8f);
				town.gameFont.bfArial.getData().setScale(1);
				
				Texture texture=null;
				if(animEventType==AnimationEventType.EDUCATION)
					texture=town.gameResourceConfig.textures.get("guiinfo_education");
				if(animEventType==AnimationEventType.FITNESS)
					texture=town.gameResourceConfig.textures.get("guiinfo_fitness");
				if(animEventType==AnimationEventType.HAPPINESS)
					texture=town.gameResourceConfig.textures.get("icon_happiness");
				if(animEventType==AnimationEventType.HEALTH)
					texture=town.gameResourceConfig.textures.get("icon_health");
				if(animEventType==AnimationEventType.INTELLIGENCE)
					texture=town.gameResourceConfig.textures.get("guiinfo_intelligence");
				if(animEventType==AnimationEventType.MOVEHOUSE)
					texture=town.gameResourceConfig.textures.get("guiinfo_intelligence");
				if(animEventType==AnimationEventType.RESEARCH)
					texture=town.gameResourceConfig.textures.get("guiinfo_researchproject");
				if(animEventType==AnimationEventType.WORKOUTPUT)
					texture=town.gameResourceConfig.textures.get("anim_showwork");
				if(animEventType==AnimationEventType.MAINTAIN)
					texture=town.gameResourceConfig.textures.get("guiinfo_condition");
				if(animEventType==AnimationEventType.COMFORT)
					texture=town.gameResourceConfig.textures.get("guiinfo_residentenergy");
								
				spriteBatch.draw(texture, x, y, iconsize, iconsize);
			}
			
			if(iFast>0)
				showTimer-=Gdx.graphics.getDeltaTime()*iFast;
			else
				showTimer-=Gdx.graphics.getDeltaTime()/2;
			
			if(showTimer<=0)
			{
				showValue=0;
				showText="";
				isFinished=true;
			}
		}
		else
		{
			showText="";
			showValue=0;
		}		
		
		spriteBatch.end();
	}
	
	public Boolean isFinished()
	{
		return isFinished;
	}
}
