package com.mygdx.game;
import javax.xml.crypto.dsig.keyinfo.KeyValue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.CAnimationTextEvent.AnimationEventType;
import com.mygdx.game.CGuiDialog_YesNo.YesNoDlgType;
import com.mygdx.game.CHelper.MapDirection;

public class CInput implements InputProcessor {

//	public int iScrollingDown;
//	public int iScrollingUp;
//	public int iScrollingLeft;
//	public int iScrollingRight;	
	public float scrollspeed;
	//public CGui gameGui;
	//public OrthographicCamera gameCam;
	//public CWorld gameWorld;
	//public CTown town;
	public CTown town;
	public CTown dummy;
	public Boolean bMouseIsDragging;
	float dblclickTimer;
	int dblclick=0;
	
	public CInput( CTown dum, CTown t1)
	{
		bMouseIsDragging=false;
		dblclick=0;
		dblclickTimer=0;
		//gameGui=gui;
		//gameCam=cam;
		town=t1;
//		iScrollingDown=0;
//		iScrollingUp=0;
//		iScrollingLeft=0;
//		iScrollingRight=0;
		dummy=dum;
	}
	
	public void render()
	{
		dblclickTimer+=Gdx.graphics.getDeltaTime();

		if(dblclickTimer>100)
			dblclickTimer=0;
		
		handleCamera();
	}
	
	public void handleCamera()
	{
		//if(gameWorld.worldPause)
		//	return;
		
		//Gdx.app.debug("zoom", String.valueOf(gameCam.zoom));
		
		scrollspeed=800f;
		
		if(town.gameCam.zoom < 50f)
			scrollspeed=600f;
		if(town.gameCam.zoom < 45f)
			scrollspeed=550f;
		if(town.gameCam.zoom < 40f)
			scrollspeed=500f;
		if(town.gameCam.zoom < 35f)
			scrollspeed=450f;
		if(town.gameCam.zoom < 30f)
			scrollspeed=400f;
		if(town.gameCam.zoom < 25f)
			scrollspeed=350f;
		if(town.gameCam.zoom < 20f)
			scrollspeed=300f;
		
		
		if(town.gameCam.zoom < 15f)
			scrollspeed=250f;
		if(town.gameCam.zoom < 10f)
			scrollspeed=200f;
		if(town.gameCam.zoom < 8f)
			scrollspeed=170f;
		
		if(town.gameCam.zoom < 4.6f)
			scrollspeed=140f;
		if(town.gameCam.zoom < 4.1f)
			scrollspeed=120f;
		if(town.gameCam.zoom < 3.6f)
			scrollspeed=100f;
		if(town.gameCam.zoom < 3.1f)
			scrollspeed=90f;
		if(town.gameCam.zoom < 2.6f)
			scrollspeed=70f;
		if(town.gameCam.zoom < 2.1f)
			scrollspeed=50f;
		if(town.gameCam.zoom < 1.6f)
			scrollspeed=30f;
		if(town.gameCam.zoom < 1)
			scrollspeed=10f;
		
		
		scrollspeed=CHelper.getFPSValue(scrollspeed);
		
		
		//scrollspeed=200;
		
		//if(iScrollingDown==1)
		if(CHelper.isMapScrolling(MapDirection.DOWN, town))
			town.gameCam.translate(0, -scrollspeed, 0);
		//if(iScrollingLeft==1)
		//if(Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A))
		if(CHelper.isMapScrolling(MapDirection.LEFT, town))
			town.gameCam.translate(-scrollspeed, 0, 0);
		//if(iScrollingRight==1)
		//if(Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D))
		if(CHelper.isMapScrolling(MapDirection.RIGHT, town))
			town.gameCam.translate(scrollspeed, 0, 0);
		//if(iScrollingUp==1)
		//if(Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.W))
		if(CHelper.isMapScrolling(MapDirection.UP, town))
			town.gameCam.translate(0, scrollspeed, 0);
		
		//town.gameWorld.bRenderFrameBuffer=true;
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		
		//if(!gameGui.bObjPlacing && !gameGui.bRoomPlacing)
		try
		{
		town.cursor.setCursor(CCursor.CursorType.DEFAULT);
		
		//if(gameWorld.worldPause)
		//	return true;
		
		if(dblclick==1)
		{
			dblclick=0;
			if(dblclickTimer<0.25f)
			{
				if(town.gameGui.doubleClick(screenX, screenY, CHelper.screenToLibGDX(screenY), button))
					return true;
			}
		}
		
		if(dblclick==0)
		{
			dblclickTimer=0;
			dblclick=1;
		}
				
		town.gameWorld.buttonUp();
		
		if(town.gameGui.buttonUp(screenX, screenY, CHelper.screenToLibGDX(screenY), button))
			return true;
		
		}
		catch(Exception e)
		{
			CHelper.writeError(e.getMessage(), e.getStackTrace(), e);
		}		
		
		return false;
	}
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
        
		//if(town.gameWorld.markerObject!=null)
		//	Gdx.app.debug("touchDragged log marker", "markerObject.pos_x(): " + town.gameWorld.markerObject.pos_x() + ", markerObject.pos_y(): " + town.gameWorld.markerObject.pos_y());
		
		try
		{
		bMouseIsDragging=true;
		
		//if(gameWorld.worldPause)
		//	return true;
		
		if(town.gameWorld.mouseMovedDrag(screenX, screenY, CHelper.screenToLibGDX(screenY)))
			return true;
		
		town.gameGui.mouseMovedDrag(screenX, screenY, CHelper.screenToLibGDX(screenY));
		
		}
		catch(Exception e)
		{
			CHelper.writeError(e.getMessage(), e.getStackTrace(), e);
		}		
		return true;
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		try
		{
		//if(!gameGui.bObjPlacing && !gameGui.bRoomPlacing)
		town.cursor.setCursor(CCursor.CursorType.KLICK);
		
		if(town.gameGui.buttonDown(screenX, screenY, CHelper.screenToLibGDX(screenY), button))
			return true;
		
		//if(gameWorld.worldPause)
		//	return true;
		
        Vector3 c0 = new Vector3(screenX,screenY,0); //gameWorld needs unprojected Coordinates
        Vector3 c1 = town.gameCam.unproject(c0);
		
		if(town.gameWorld.buttonDown((int)c1.x, (int)c1.y, CHelper.screenToLibGDX(screenY), button))
			return true;
		
		//------------------------------------------------
        		
        if(c1.x>0 && c1.y>0 && c1.x<town.gameWorld.mapsize && c1.y<town.gameWorld.mapsize)
        {
        	if(dummy!=null && dummy.player!=null)
        	{
	            dummy.player.ziel_x=(int) c1.x;
	            dummy.player.ziel_y=(int) c1.y;
	            
	            dummy.player.ziel_x-=28; //halbe breite sprite
	            dummy.player.ziel_y-=29; //halbe höhe sprite
        	}
        }
                
        //dummy.rotation = (float) Math.atan2((float)dummy.ziel_y - (float)dummy.py, (float)(dummy.ziel_x - (float)dummy.px));
        //dummy.rotation = MathUtils.radiansToDegrees*dummy.rotation;
        //dummy.rotation=dummy.rotation+90f;
		}
		catch(Exception e)
		{
			CHelper.writeError(e.getMessage(), e.getStackTrace(), e);
		}                
		return true;
	}
	
	@Override
	public boolean scrolled(int amount) {
		
		try
		{
		
		town.gameWorld.bRenderFrameBuffer=true;
		
		if(town.gameGui.mouseScrolled(amount))
			return true;
		
		if(town.gameGui.dlgShowing())
			return true;
		
//		float scrollSpeed=2;//CHelper.getFPSValue(0.5f);
//		if(gameCam.zoom<3)
//			scrollSpeed=0.5f;
//		else
//			scrollSpeed=2;
		
		//float scrollSpeed = gameCam.zoom/5;
		float scrollSpeed = town.gameCam.zoom/2.6f;
		
		if(amount==1)
			town.gameCam.zoom += scrollSpeed;
		else if(amount==-1)
		{
			town.gameCam.zoom -= scrollSpeed;
			
			//zoom to mouse
			//in welchem quadrant befindet sich maus?
						
//			if(CHelper.isMapScrolling(MapDirection.DOWN))
//				gameCam.translate(0, -scrollspeed, 0);
//			if(CHelper.isMapScrolling(MapDirection.LEFT))
//				gameCam.translate(-scrollspeed, 0, 0);
//			if(CHelper.isMapScrolling(MapDirection.RIGHT))
//				gameCam.translate(scrollspeed, 0, 0);
//			if(CHelper.isMapScrolling(MapDirection.UP))
//				gameCam.translate(0, scrollspeed, 0);			
		}

		
		//Max Zoom
		//float maxzoom=1.5f;
		float maxzoom=0.1f;
		if(town.gameCam.zoom<maxzoom)
			town.gameCam.zoom=maxzoom;

		if(town.gameCam.zoom>500)
			town.gameCam.zoom=500;

		
		}
		catch(Exception e)
		{
			CHelper.writeError(e.getMessage(), e.getStackTrace(), e);
		}
		
		return true;
	}
	
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		
		try
		{
		//Intersector.overlaps(c, r)
		
		//Achtung: hier stockt das scrolling über die cursor keys wenn das einkommentiert ist
//		iScrollingDown=0;
//		iScrollingLeft=0;
//		iScrollingRight=0;
//		iScrollingUp=0;
		town.gameWorld.mouseCursorMoved(screenX, screenY, 0);
				
		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();
		int abstand=10;
		
		//kein auto scrolling
//		if(screenX >= width-abstand)
//			iScrollingRight=1;
//		if(screenX <= abstand)
//			iScrollingLeft=1;
//		
//		if(screenY >= height-abstand)
//			iScrollingDown=1;
//		
//		if(screenY <= abstand)
//			iScrollingUp=1;
		
		}
		catch(Exception e)
		{
			CHelper.writeError(e.getMessage(), e.getStackTrace(), e);
		}		
		return true;
	}
	
	@Override
	public boolean keyUp(int keycode) {

		try
		{
			town.gameWorld.bRenderFrameBuffer=true;
		
		if(town.gameGui.keyUp(keycode))
			return true;

		
//		if (CHelper.isMapScrolling(MapDirection.DOWN)) {
//        	iScrollingDown=0;
//            //cam.translate(0, -30, 0);
//        }				
//	    if (keycode==Input.Keys.LEFT || keycode==Input.Keys.A) {
//	    	iScrollingLeft=0;
//            //cam.translate(-30, 0, 0);
//        }
//        if (keycode==Input.Keys.RIGHT || keycode==Input.Keys.D) {
//        	iScrollingRight=0;
//            //cam.translate(30, 0, 0);
//        }				
//	    if (keycode==Input.Keys.UP || keycode==Input.Keys.W) {
//	    	iScrollingUp=0;
//            //cam.translate(0, 30, 0);
//        }			
		}
		catch(Exception e)
		{
			CHelper.writeError(e.getMessage(), e.getStackTrace(), e);
		}	    
		return true;
	}
	
	@Override
	public boolean keyTyped(char character) {
		
		try
		{
		//		int ch=character;
		//		if(ch==Input.Keys.ENTER)
		//			Gdx.app.debug("", ""+ch);
		
		if(town.gameGui.keyTyped(character))
			return true;
		
		}
		catch(Exception e)
		{
			CHelper.writeError(e.getMessage(), e.getStackTrace(), e);
		}
		return false;
	}
	
	@Override
	public boolean keyDown(int keycode) 
	{
		try
		{
			town.gameWorld.bRenderFrameBuffer=true;
		

			
		if(town.bDevMode)
		{
			//if(Gdx.input.isKeyJustPressed(Keys.PLUS))
			if(keycode==Input.Keys.PLUS)
				town.gameWorld.townMoney+=1000000;
			if(keycode==Input.Keys.MINUS)
				town.gameWorld.townMoney+=10000;
			
			if(keycode==Input.Keys.P)
				town.gameWorld.worldTime.step(3600);
			
			//if(keycode==Input.Keys.Y)
			//{
			//	Vector3 v3 = CHelper.getMouseWorldCoordinates(town.gameCam);
			//	CWorldObject wobj = gameWorld.gameResourceConfig.createWorldObject("recyclingcenter_garbagebag", (int)v3.x, (int)v3.y, null);
			//}
			
			if(keycode==Input.Keys.Z)
				town.gameWorld.infoEvents.add(new CInfoTextEvent(town, "new residents have arrived", Color.RED));
			
			//if(keycode==Input.Keys.T)
			//	town.gameWorld.animationEvents.add(new CAnimationTextEvent(town, 500, 500,  100, AnimationEventType.EDUCATION, 8/town.gameCam.zoom));
			
			if (keycode==Input.Keys.H)
				town.gameWorld.worldTime.hours+=1;
			if (keycode==Input.Keys.J)
				town.gameWorld.worldTime.hours-=1;
			
			if (keycode==Input.Keys.M)
				town.gameWorld.worldTime.day+=1;
			if (keycode==Input.Keys.N)
				town.gameWorld.worldTime.day-=1;
			
			if(keycode==Input.Keys.G)
			{
				//Gdx.app.debug("", ""+gameWorld.worldWeather.iRainPower);
				town.gameWorld.worldWeather.iRainPower+=1;
				//if(gameWorld.worldWeather.iRainPower>30)
				//	gameWorld.worldWeather.iRainPower=30;
			}
			
			if(keycode==Input.Keys.B)
			{
				if(town.gameWorld.worldWeather.iRenderRainFrom<0)
				{
					town.gameWorld.worldWeather.iRenderRainFrom=0;
					town.gameWorld.worldWeather.iRenderRainTo=23;
				}
				else
				{
					town.gameWorld.worldWeather.iRenderRainFrom=-1;
					town.gameWorld.worldWeather.iRenderRainTo=-1;
				}
			}
			if(keycode==Input.Keys.V)
			{
				if(town.gameWorld.worldWeather.iRenderSnowFrom<0)
				{
					town.gameWorld.worldWeather.iRenderSnowFrom=0;
					town.gameWorld.worldWeather.iRenderSnowTo=23;
				}
				else
				{
					town.gameWorld.worldWeather.iRenderSnowFrom=-1;
					town.gameWorld.worldWeather.iRenderSnowTo=-1;
				}
			}
		}
		
		if(town.gameGui.keyDown(keycode))
			return true;
		
        if (keycode==Input.Keys.ESCAPE) 
        {
        	if(town.gameGui.level1_objtypeid.equals("008"))
        	{
        		town.gameGui.yesnoDlg.showDlg(true, YesNoDlgType.QUIT);
        		town.gameGui.closeMenu();
        		
        		return false;
        	}
        	
        	if(town.bDevMode)
        		Gdx.app.exit();
        	
        	town.gameGui.level1_objtypeid="008";
        }				
        
        
        //*********
        //SCROLLING
        //*********
//        if (keycode==Input.Keys.DOWN || keycode==Input.Keys.S) {
//        	iScrollingDown=1;
//        }				
//	    if (keycode==Input.Keys.LEFT || keycode==Input.Keys.A) {
//	    	iScrollingLeft=1;
//        }
//        if (keycode==Input.Keys.RIGHT || keycode==Input.Keys.D) {
//        	iScrollingRight=1;
//        }				
//	    if (keycode==Input.Keys.UP || keycode==Input.Keys.W) {
//	    	iScrollingUp=1;
//	    	
//        }
		
		}
		catch(Exception e)
		{
			CHelper.writeError(e.getMessage(), e.getStackTrace(), e);
		}
	    
		return false;
	}

	
	
}
