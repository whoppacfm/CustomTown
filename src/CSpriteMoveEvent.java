package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;

public class CSpriteMoveEvent {

	public static enum SpriteMoveEventType {
	    PAPERFLYER, PAPERBALL, PLACING, BULLET, SPLATTER
	}
	
	public SpriteMoveEventType spriteMoveEventType;
	CTown town;
	public float startX;
	public float startY;
	public float targetX;
	public float targetY;
	float rotSprite;
	float deltaSeconds;
	float deltaSeconds2;
	float xDeltaSecond;
	float waitcount;
	Boolean bIsFinished;
	float speed1;
	float damage1;
	
	//SpriteBatch spriteBatch;
	Color drawColor;
	int zorder;
	CWorldObject targetWorldObject;
	
	public CSpriteMoveEvent(SpriteMoveEventType type, float sx, float sy, float tx, float ty, CTown t, int zorder1)
	{
		this.zorder=zorder1;
		startX=sx;
		startY=sy;
		targetX=tx;
		targetY=ty;
		bIsFinished=false;
		spriteMoveEventType=type;
		town=t;
		//spriteBatch=town.gameWorld.worldSpriteBatch;
		deltaSeconds=0;
		deltaSeconds2=0;
		xDeltaSecond=0;
		waitcount=0;
		rotSprite=0;
		targetWorldObject=null;
		speed1=0;
		damage1=0;
	}
	
	public CSpriteMoveEvent(SpriteMoveEventType type, CWorldObject target, int zorder1)
	{
		targetWorldObject=target;
		this.zorder=zorder1;
		
		startX=target.width;
		startY=target.height;
		drawColor=new Color(0.6f,0.6f,0.6f,0.24f);
		//drawColor=new Color(1,1,1,1);
		
		targetX=target.width+120+target.width/2;
		targetY=target.height+120+target.height/2;
		
		if(target.theobject.editoraction.contains("floor") || 
				target.theobject.editoraction.contains("road_road_road") || 
				target.theobject.editoraction.contains("footpath")
			)
		{
			targetX=target.width+120+target.width/2;
			targetY=target.height+120+target.height/2;
		}
		
		bIsFinished=false;
		spriteMoveEventType=type;
		town=target.town;
		//spriteBatch=town.gameWorld.worldSpriteBatch;
		deltaSeconds=0;
		deltaSeconds2=0;
		xDeltaSecond=0;
		waitcount=0;
		rotSprite=0;
		speed1=0;
		damage1=0;
	}
	
	public void render(SpriteBatch spriteBatch)
	{
		deltaSeconds+=CHelper.getDeltaSeconds(town);
		deltaSeconds2+=CHelper.getDeltaSeconds(town);
		
		if(spriteMoveEventType==SpriteMoveEventType.PAPERFLYER || 
				spriteMoveEventType==SpriteMoveEventType.PAPERBALL ||
				spriteMoveEventType==SpriteMoveEventType.BULLET
				)
			renderThrowThing(spriteBatch);
		
		if(spriteMoveEventType==SpriteMoveEventType.PLACING)
			renderPlacing(spriteBatch);
		
		if(spriteMoveEventType==SpriteMoveEventType.SPLATTER)
			renderSplatter(spriteBatch);
		
		if(deltaSeconds>3600)
			deltaSeconds=0;
	}

	public void renderSplatter(SpriteBatch spriteBatch)
	{
		targetX=200; //target.width+120+target.width/2;
		targetY=200; //target.height+120+target.height/2;
		
		if(waitcount==1)
			return;
		
		if(waitcount==0)
		{
			if(deltaSeconds>0.02f)
			{
				float speed=targetX;
				//speed=41;
				speed=50;
				
				startX+=speed;
				startY+=speed;
				deltaSeconds=0;
			}
		}
        
		//spriteBatch.setColor(0.5f, 0.5f, 0.5f, 0.1f);
		spriteBatch.setColor(1,1,1,0.8f);
		//String sTexture = "school_paperball";
		int w=(int) startX;
		int h=(int) startY;
		
		//Texture moveTexture = town.gameResourceConfig.textures.get(sTexture);
		Texture moveTexture = town.gameResourceConfig.animations.get("deadzombie").getKeyFrames()[0].getTexture();
				
		CWorldObject wobj = targetWorldObject;
		Vector2 v2 = CHelper.moveVectorByRotationS2D(wobj.pos_x(), wobj.pos_y(), (int)(wobj.width/2), (int)(wobj.height/2), wobj.width/2, wobj.height/2, wobj.rotation());
		spriteBatch.draw(moveTexture, v2.x-w/2, v2.y-h/2, w/2, h/2, w, h, 1, 1, wobj.rotation(), 0, 0, moveTexture.getWidth(), moveTexture.getHeight(), false, false);
		
		if(startX>targetX || startY>targetY)
		{
			waitcount=1;
			bIsFinished=true;
		}
	}	
	
	public void renderPlacing(SpriteBatch spriteBatch)
	{
		if(waitcount==0)
		{
			if(deltaSeconds>0.02f)
			{
				float speed=targetX;
				speed=41;
				
				startX+=speed;
				startY+=speed;
				deltaSeconds=0;
			}
		}
        
		spriteBatch.setColor(0.5f, 0.5f, 0.5f, 0.1f);
		String sTexture = "school_paperball";
		int w=(int) startX;
		int h=(int) startY;
		
		Texture moveTexture = town.gameResourceConfig.textures.get(sTexture);
		CWorldObject wobj = targetWorldObject;
		Vector2 v2 = CHelper.moveVectorByRotationS2D(wobj.pos_x(), wobj.pos_y(), (int)(wobj.width/2), (int)(wobj.height/2), wobj.width/2, wobj.height/2, wobj.rotation());
		spriteBatch.draw(moveTexture, v2.x-w/2, v2.y-h/2, w/2, h/2, w, h, 1, 1, wobj.rotation(), 0, 0, moveTexture.getWidth(), moveTexture.getHeight(), false, false);
		
		if(startX>targetX || startY>targetY)
		{
			waitcount=1;
			bIsFinished=true;
		}
	}
	
	public void renderThrowThing(SpriteBatch spriteBatch)
	{
		if(waitcount==0)
		{
			if(deltaSeconds>0.02f)
			{
				float speed=10;
				if(spriteMoveEventType == SpriteMoveEventType.PAPERBALL)
					speed=20;
				if(spriteMoveEventType == SpriteMoveEventType.BULLET)
					speed=speed1;
				
				if(startX>targetX)
					startX-=speed;
				if(startX<targetX)
					startX+=speed;
				
				if(startY>targetY)
					startY-=speed;
				if(startY<targetY)
					startY+=speed;
				
				deltaSeconds=0;
			}
			
	        if(Math.abs(targetX - (int)startX) > 10 || Math.abs(targetY - (int)startY) > 10)
	        {
	        	rotSprite = (float) Math.atan2((float)targetY - (float)startY, (float)(targetX - (startX)));
	        	rotSprite = MathUtils.radiansToDegrees*rotSprite;
	        	rotSprite = rotSprite+270;
	        }
		}
        
		spriteBatch.setColor(1,1,1,1f);
		
		String sTexture = "school_paperflyer";
		int w=40;
		int h=60;
		
		if(spriteMoveEventType == SpriteMoveEventType.PAPERBALL)
		{
			sTexture = "school_paperball";
			w=30;
			h=30;
		}
		
		if(spriteMoveEventType == SpriteMoveEventType.BULLET)
		{
			sTexture = "laser1";
			w=30;
			h=30;
		}
		
		//Prüfe Treffer
		if(spriteMoveEventType == SpriteMoveEventType.BULLET)
		{
			for(CWorldObject z1 : town.gameWorld.worldZombies)
			{
				if(CHelper.getEuclidianDistance(z1.pos_x(), z1.pos_y(), (int)startX, (int)startY)<speed1)
					z1.thehuman.changeHealthValue(-damage1);
				
				if(1==1)
					continue;
				
				//z1.testpoint(x, y, imode)
				Rectangle r1=new Rectangle();
				Rectangle r2=new Rectangle();
				Rectangle r3=new Rectangle();
				
				r1.x=z1.pos_x();
				r1.y=z1.pos_y();
				r1.width=z1.width_human();
				r1.height=z1.height_human();
				
				r2.x=startX;
				r2.y=startY;
				r2.width=w;
				r2.height=h;
				
				if(Intersector.intersectRectangles(r1,r2,r3))
					z1.thehuman.changeHealthValue(-damage1);
			}
		}
		
		Texture moveTexture = town.gameResourceConfig.textures.get(sTexture);
		spriteBatch.draw(moveTexture, startX, startY, w/2, h/2, w, h, 1, 1, rotSprite, 0, 0, moveTexture.getWidth(), moveTexture.getHeight(), false, false);
		
		int durationfloor=60*7;
		
		if(spriteMoveEventType == SpriteMoveEventType.BULLET)
			durationfloor=60*2;
		
		//if(spriteMoveEventType == SpriteMoveEventType.BULLET)
		//	Gdx.app.debug("", "bIsFinished: "+bIsFinished + ", waitcount: " + waitcount + ", deltaSeconds: " + deltaSeconds + ", x: " + (Math.abs(targetX-startX)) + ", y: " + (Math.abs(targetY-startY)));
		
		if(deltaSeconds2>60*5)
			bIsFinished=true;
		
		int abstand=50;
		if(spriteMoveEventType == SpriteMoveEventType.BULLET)
			abstand=(int) speed1;
		
		if(Math.abs(targetX-startX)<abstand && Math.abs(targetY-startY)<abstand)
		{
			waitcount=1;
			
			if(deltaSeconds>durationfloor)
				bIsFinished=true;
		}
	}
}
