package com.mygdx.game;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.CHelper.MapDirection;


public class CWeather {
		
	class CAnimObject
	{
		public double x;
		public double y;
		public double height;
		
		public CAnimObject(double x, double y, double height)
		{
			this.x=x;
			this.y=y;
			this.height=height;
		}
	}	
	
	CTown town;
	
	static float tempZoom;
	static int randx = 0;
	static int randy = 0;
	
	public int iRenderSnowFrom;
	public int iRenderRainFrom;
	public int iRenderSnowTo;
	public int iRenderRainTo;
	
	public int iSnowPower;
	public int iRainPower;
	
	public Boolean bIsRaining;
	public Boolean bIsSnowing;
	
	private Random rand;
	
	public SpriteBatch spriteBatch;
	
	public List<CAnimObject> listRaindrops;
	public List<CAnimObject> listRaindropsDel;
	
	public int initializedMonth;
	
	//ParticleEffect snowEffect;
	//	snowEffect = new ParticleEffect();
	//	snowEffect.load(Gdx.files.internal("particles/snoweffect2"), Gdx.files.internal("particles"));
	//	snowEffect.setDuration(10);
	//	snowEffect.start();	
		//Render snow
	//	gameGui.editorSpriteBatch.begin();
	//	snowEffect.setDuration(1);
	//	snowEffect.setPosition(0, 0);
	//	snowEffect.draw(gameGui.editorSpriteBatch, Gdx.graphics.getDeltaTime());
	//	snowEffect.setPosition(500, 500);
	//	snowEffect.draw(gameGui.editorSpriteBatch, Gdx.graphics.getDeltaTime());
	//	gameGui.editorSpriteBatch.end();	
		
	public CWeather(CTown t)
	{
		town=t;
		rand = new Random();
		resetWeather();
		spriteBatch=t.gameWorld.worldSpriteBatch;
		initializedMonth=-1;
		bIsRaining=false;
		bIsSnowing=false;
	}
	
	public void resetWeather()
	{
		iRenderSnowFrom=-1;
		iRenderSnowTo=-1;
		iRenderRainFrom=-1;
		iRenderRainTo=-1;
		initializedMonth=-1;
		bIsRaining=false;
		bIsSnowing=false;
	}
	
	public void initDayWeather()
	{
		//iRainPower=rand.nextInt(80);
		//iSnowPower=rand.nextInt(80);
		
		iRainPower=30; //rand.nextInt(30);
		iSnowPower=rand.nextInt(30);
		
		//Init Snow
		int iRenderSnow=0;
		if(town.gameWorld.worldTime.day==11)
			iRenderSnow = rand.nextInt(2);
		if(town.gameWorld.worldTime.day==1)
			iRenderSnow = rand.nextInt(2);
		if(town.gameWorld.worldTime.day==2)
			iRenderSnow = rand.nextInt(3);
		
		if(iRenderSnow==1)
		{
			iRenderSnowFrom=rand.nextInt(22);
			iRenderSnowTo=rand.nextInt(23);
			if(iRenderSnowFrom>iRenderSnowTo)
			{
				int itemp=iRenderSnowFrom;
				iRenderSnowFrom=iRenderSnowTo;
				iRenderSnowTo=itemp;
			}
		}
		
		if(town.gameWorld.worldTime.day==12)
		{
			iRenderSnowFrom=0;
			iRenderSnowTo=23;
		}
		
		
		//Init Rain
		int iRenderRain=0;
		if(town.gameWorld.worldTime.day==1)
			iRenderRain = rand.nextInt(10);
		if(town.gameWorld.worldTime.day==2)
			iRenderRain = rand.nextInt(7);
		if(town.gameWorld.worldTime.day==3)
			iRenderRain = rand.nextInt(3);
		if(town.gameWorld.worldTime.day==4)
			iRenderRain = rand.nextInt(2);
		if(town.gameWorld.worldTime.day==5)
			iRenderRain = rand.nextInt(4);
		if(town.gameWorld.worldTime.day==6)
			iRenderRain = rand.nextInt(2);
		if(town.gameWorld.worldTime.day==7)
			iRenderRain = rand.nextInt(5);
		if(town.gameWorld.worldTime.day==8)
			iRenderRain = rand.nextInt(5);
		if(town.gameWorld.worldTime.day==9)
			iRenderRain = rand.nextInt(5);
		if(town.gameWorld.worldTime.day==10)
			iRenderRain = rand.nextInt(5);
		if(town.gameWorld.worldTime.day==11)
			iRenderRain = rand.nextInt(10);
		if(town.gameWorld.worldTime.day==12)
			iRenderRain = rand.nextInt(10);			
		
		if(iRenderRain<3)
		{
			iRenderRainFrom=rand.nextInt(22);
			iRenderRainTo=rand.nextInt(23);
			
			if(iRenderRainFrom>iRenderRainTo)
			{
				int itemp=iRenderRainFrom;
				iRenderRainFrom=iRenderRainTo;
				iRenderRainTo=itemp;
			}
		}		
		
		//if(town.bDebugShowRain || town.bDebugShowSnow)
		//Gdx.app.debug("Weather", "iRenderRain: " + iRenderRain + ", initializedMonth: " + town.gameWorld.worldWeather.initializedMonth + ", rainfrom: " + town.gameWorld.worldWeather.iRenderRainFrom + ", rainto: " + town.gameWorld.worldWeather.iRenderRainTo+ ", snowfrom: " + town.gameWorld.worldWeather.iRenderSnowFrom + ", snowto: " + town.gameWorld.worldWeather.iRenderSnowTo);
		
	}
	
	private void initMonthWeather()
	{
		//Init Weather for Month
		if(initializedMonth==town.gameWorld.worldTime.day)
			return;

		initDayWeather();
		
		initializedMonth=town.gameWorld.worldTime.day;
	}
	
	public void renderWeather()
	{
		bIsRaining=false;
		bIsSnowing=false;
		
		if(town.bDebugShowRain || town.bDebugShowSnow)
			Gdx.app.debug("Weather", "initializedMonth: " + town.gameWorld.worldWeather.initializedMonth + ", rainfrom: " + town.gameWorld.worldWeather.iRenderRainFrom + ", rainto: " + town.gameWorld.worldWeather.iRenderRainTo+ ", snowfrom: " + town.gameWorld.worldWeather.iRenderSnowFrom + ", snowto: " + town.gameWorld.worldWeather.iRenderSnowTo);
		
		//DEBUG**********************
		//		int a=0;
		//		if(a==0)
		//		{
		//			renderRain();
		//			return;
		//		}
		//***************************
		
		if(town.gameWorld.worldPause)
		{
			if(CHelper.isMapScrolling(town))
				return;
			
//			if(town.gameInput.iScrollingDown==1)
//				return;
//			if(town.gameInput.iScrollingLeft==1)
//				return;
//			if(town.gameInput.iScrollingRight==1)
//				return;
//			if(town.gameInput.iScrollingUp==1)
//				return;
		}		
		
		//if(town.gameWorld.worldPause)
		//	return;
		
		initMonthWeather();
		
		if(iRenderSnowFrom>-1 && iRenderSnowTo>-1)
		{
			if(town.gameWorld.worldTime.hours>iRenderSnowTo)
			{
				iRenderSnowFrom=-1;
				iRenderSnowTo=-1;
			}
			
			if(town.gameWorld.worldTime.hours>=iRenderSnowFrom && town.gameWorld.worldTime.hours<=iRenderSnowTo)
				renderSnow();	
		}
		
		
		if(iRenderRainFrom>-1 && iRenderRainTo>-1)
		{
			//Gdx.app.debug("", ""+iRenderRainTo + ">" + town.gameWorld.worldTime.hours);
			if(town.gameWorld.worldTime.hours>iRenderRainTo)
			{
				iRenderRainFrom=-1;
				iRenderRainTo=-1;
			}
			
			if(town.gameWorld.worldTime.hours>=iRenderRainFrom && town.gameWorld.worldTime.hours<=iRenderRainTo)
			{
				//Gdx.app.debug("", "render Rain 3");
				renderRain();
			}
		}
		
		tempZoom=town.gameCam.zoom;
	}

	public void renderRain()
	{
		bIsRaining=true;
		
		//if(worldPause)
		//	return;
		
		if(listRaindrops==null)
			listRaindrops=new ArrayList<CAnimObject>();
		if(listRaindropsDel==null)
			listRaindropsDel=new ArrayList<CAnimObject>();
		
		int w=Gdx.graphics.getWidth();
		int h=Gdx.graphics.getHeight();
		int edge=200;
		if(!town.gameWorld.worldPause)
		{
			//for(int i=0;i<120;i++)
			for(int i=0;i<iRainPower;i++)
			{
				double x=-edge+rand.nextInt(w+edge*2);
				double y=-edge+rand.nextInt(h+edge*2);
				listRaindrops.add(new CAnimObject(x, y, 400));
			}
		}
		
		spriteBatch.begin();
		for(CAnimObject obj : listRaindrops)
		{
			if(!town.gameWorld.worldPause)
			{
				obj.height-=5;
			
				if(obj.height<=0)
				{
					listRaindropsDel.add(obj);
					continue;
				}
			}
			
			float siz=9.8f;
			
			if(!town.gameWorld.worldPause)
			{
				if(CHelper.isMapScrolling(MapDirection.DOWN, town))
					obj.y+=town.gameInput.scrollspeed/1.7f;
				if(CHelper.isMapScrolling(MapDirection.LEFT, town))
					obj.x+=town.gameInput.scrollspeed/1.7f;
				if(CHelper.isMapScrolling(MapDirection.RIGHT, town))
					obj.x-=town.gameInput.scrollspeed/1.7f;
				if(CHelper.isMapScrolling(MapDirection.UP, town))
					obj.y-=town.gameInput.scrollspeed/1.7f;
			}
			else
			{
				if(CHelper.isMapScrolling(MapDirection.DOWN, town))
					siz=20;
				if(CHelper.isMapScrolling(MapDirection.LEFT, town))
					siz=20;
				if(CHelper.isMapScrolling(MapDirection.RIGHT, town))
					siz=20;
				if(CHelper.isMapScrolling(MapDirection.UP, town))
					siz=20;
			}
			
			double vx = (obj.x-(w/2)) / w/2;
			double vy = (obj.y-(h/2)) / h/2;
			double length=2;
			double px1=obj.x+vx*Math.sqrt(obj.height);
			double py1=obj.y+vy*Math.sqrt(obj.height);
			double px2=obj.x+vx*Math.sqrt(obj.height+length);
			double py2=obj.y+vy*Math.sqrt(obj.height+length);
			
			int oh=(int)obj.height;
			spriteBatch.setColor(1,1,1,0.7f);
			Texture tex = town.gameResourceConfig.textures.get("weather_rain");
			
			float sr=(int)oh/town.gameCam.zoom/siz;
			if(sr>5)
				sr=5;
			if(sr<2)
				sr=2;

			if(tempZoom!=town.gameCam.zoom)
			{
				try
				{
				randx=-rand.nextInt((int)town.gameCam.zoom*10)+rand.nextInt((int)town.gameCam.zoom*10);
				randy=-rand.nextInt((int)town.gameCam.zoom*10)+rand.nextInt((int)town.gameCam.zoom*10);
				}
				catch(Exception e) {randx=0;randy=0;}
			}
			if(randx<0)
				randx=0;
			if(randy<0)
				randy=0;
			
			spriteBatch.draw(tex, (int)px1+randx, (int)py1+randy, sr, sr);
		}
		
		listRaindrops.removeAll(listRaindropsDel);
		listRaindropsDel.clear();
		spriteBatch.end();
	}
	
	public void renderSnow()
	{
		bIsSnowing=true;
		
		if(listRaindrops==null)
			listRaindrops=new ArrayList<CAnimObject>();
		if(listRaindropsDel==null)
			listRaindropsDel=new ArrayList<CAnimObject>();
		
		int w=Gdx.graphics.getWidth();
		int h=Gdx.graphics.getHeight();
		int edge=200;
		if(!town.gameWorld.worldPause)
		{
			//for(int i=0;i<100;i++)
			for(int i=0;i<iSnowPower;i++)
			{
				double x=-edge+rand.nextInt(w+edge*2);
				double y=-edge+rand.nextInt(h+edge*2);
				listRaindrops.add(new CAnimObject(x, y, 400));
			}
		}
		
		spriteBatch.begin();
		for(CAnimObject obj : listRaindrops)
		{
			if(!town.gameWorld.worldPause)
			{
				obj.height-=5;
				
				if(obj.height<=0)
				{
					obj.height=2;
					listRaindropsDel.add(obj);
					continue;
				}
			}
			
			if(CHelper.isMapScrolling(MapDirection.DOWN, town))
				obj.y+=town.gameInput.scrollspeed/1.7f;
			if(CHelper.isMapScrolling(MapDirection.LEFT, town))
				obj.x+=town.gameInput.scrollspeed/1.7f;
			if(CHelper.isMapScrolling(MapDirection.RIGHT, town))
				obj.x-=town.gameInput.scrollspeed/1.7f;
			if(CHelper.isMapScrolling(MapDirection.UP, town))
				obj.y-=town.gameInput.scrollspeed/1.7f;			
			
			double vx = (obj.x-(w/2)) / w/2;
			double vy = (obj.y-(h/2)) / h/2;
			double length=2;
			double px1=obj.x+vx*Math.sqrt(obj.height);
			double py1=obj.y+vy*Math.sqrt(obj.height);
			double px2=obj.x+vx*Math.sqrt(obj.height+length);
			double py2=obj.y+vy*Math.sqrt(obj.height+length);
			
			float siz=9.8f;
			int oh=(int)obj.height;
			
			spriteBatch.setColor(1,1,1,0.7f);
			
			//Texture tex = gameResourceConfig.textures.get("energy_waterdrop");
			Texture tex = town.gameResourceConfig.textures.get("weather_snow");
			
			float sr=(int)oh/town.gameCam.zoom/siz;
			if(sr>7)
				sr=5;
			if(sr<2)
				sr=2;

			if(tempZoom!=town.gameCam.zoom)
			{
				try
				{
					randx=-rand.nextInt((int)town.gameCam.zoom*10)+rand.nextInt((int)town.gameCam.zoom*10);
					randy=-rand.nextInt((int)town.gameCam.zoom*10)+rand.nextInt((int)town.gameCam.zoom*10);
				}
				catch(Exception e) {randx=0;randy=0;}
			}
			if(randx<0)
				randx=0;
			if(randy<0)
				randy=0;
			
			spriteBatch.draw(tex, (int)px1+randx, (int)py1+randy, sr, sr);
			
			
//			if(obj.height<12)
//				spriteBatch.draw(tex, (int)px1, (int)py1, 7/town.gameCam.zoom/siz, (int)oh+7/town.gameCam.zoom/siz);
//			if(obj.height<7)
//				spriteBatch.draw(tex, (int)px1, (int)py1, 4/town.gameCam.zoom/siz, (int)oh+4/town.gameCam.zoom/siz);
//			if(obj.height<3)
//				spriteBatch.draw(tex, (int)px1, (int)py1, 2/town.gameCam.zoom/siz, (int)oh+2/town.gameCam.zoom/siz);
		}
		
		listRaindrops.removeAll(listRaindropsDel);
		listRaindropsDel.clear();
		spriteBatch.end();
	}
		
	
}
