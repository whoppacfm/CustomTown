package com.mygdx.game;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;









import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.mygdx.game.CCompany.CompanyType;


public class CHelper {
	
	public enum IntersectionMode {COLLISION, MOUSECLICK, COLLISION_SMALL, COLLISION_SMALL2, DEFAULT, PLACING_ON_FLOOR, COLLISION_WATER, COLLISION_PLACEWALL}
	
	public enum MapDirection
	{
		LEFT,RIGHT,UP,DOWN
	}
	
	public static FileHandle getFileHandle(String path)
	{
		return Gdx.files.external(path);
	}
	
	public static Boolean isInteger(String str)
	{
		return str.matches("^-?\\d+$");
	}
	
	public static float getDeltaSeconds(CTown town)
	{
		if(town.gameWorld.worldPause)
			return 0;
		
		//ACHTUNG bei anpassung -> age anpassung *11 ebenfalls berücksichtigen
		
		//float delta=CHelper.getSpeedControllerValue(gameWorld.gameGui)/30;
		float delta=CHelper.getSpeedControllerValue(town.gameGui)/1000;
		
		if(delta<1)
			delta=1;
		
		return Gdx.graphics.getDeltaTime()*60*delta*town.gameWorld.town.deltaSecondsDelta;
	}
	
	public static float getSpeedControllerValue(CGui gui)
	{
//		if(gui.buttonX2.toggleActive)
//			return 5000;
//		
//		if(gui.buttonX4.toggleActive)
//			return 10000;

		if(gui.buttonX2.toggleActive)
			return 2000;
		
		if(gui.buttonX4.toggleActive)
			return 4000;
		
		
		return 1;
	}
	
	public static Color setColor (Color target, float hue, float saturation, float value)
	{
        saturation = MathUtils.clamp(saturation, 0.0f, 1.0f);
        while (hue < 0) hue++;
        while (hue >= 1) hue--;
        value = MathUtils.clamp(value, 0.0f, 1.0f);
        
        float red = 0.0f;
        float green = 0.0f;
        float blue = 0.0f;

        final float hf = (hue - (int) hue) * 6.0f;
        final int ihf = (int) hf;
        final float f = hf - ihf;
        final float pv = value * (1.0f - saturation);
        final float qv = value * (1.0f - saturation * f);
        final float tv = value * (1.0f - saturation * (1.0f - f));

        switch (ihf) {
            case 0:         // Red is the dominant color
                red = value;
                green = tv;
                blue = pv;
                break;
            case 1:         // Green is the dominant color
                red = qv;
                green = value;
                blue = pv;
                break;
            case 2:
                red = pv;
                green = value;
                blue = tv;
                break;
            case 3:         // Blue is the dominant color
                red = pv;
                green = qv;
                blue = value;
                break;
            case 4:
                red = tv;
                green = pv;
                blue = value;
                break;
            case 5:         // Red is the dominant color
                red = value;
                green = pv;
                blue = qv;
                break;
        }

        return target.set(red, green, blue, target.a);
    }
	
	public static void drawDashedRect(Color color, ShapeRenderer renderer, float d1, float d2, float sx, float sy, float ex, float ey, float width)
	{
		//d1: dash length
		//d2: abstand zwischen dashes
		
		//    	CHelper.drawDashedLine(color, renderer, d1, d2, sx, sy-d1, ex, sy-d1, width); 	//links unten -> rechts unten
		//    	CHelper.drawDashedLine(color, renderer, d1, d2, sx, sy+d1*4, sx, ey-d1, width); //links unten -> links oben
		//    	
		//    	CHelper.drawDashedLine(color, renderer, d1, d2, sx, ey+d1, ex, ey+d1, width); 	//links oben -> rechts oben
		//    	CHelper.drawDashedLine(color, renderer, d1, d2, ex, sy+d1*4, ex, ey-d1, width); //rechts unten -> rechts oben
		
    	CHelper.drawDashedLine(color, renderer, d1, d2, sx+d2, sy, ex, sy, width); //links unten -> rechts unten
    	CHelper.drawDashedLine(color, renderer, d1, d2, sx, sy, sx, ey, width); //links unten -> links oben
    	
    	CHelper.drawDashedLine(color, renderer, d1, d2, sx, ey, ex, ey, width); //links oben -> rechts oben
    	CHelper.drawDashedLine(color, renderer, d1, d2, ex, sy, ex, ey, width); //rechts unten -> rechts oben
	}
	
	public static void drawDashedLine(Color color, ShapeRenderer renderer, float d1, float d2, float sx, float sy, float ex, float ey, float width) 
	{

      if (d1 == 0) {
         return ;
      }
      
      float dirX = ex - sx;
      float dirY = ey - sy;
      
      float length = Vector2.len(dirX, dirY);
      dirX /= length;
      dirY /= length;
      
      float curLen = 0;
      float curX = 0;
      float curY = 0;
      
      //renderer.begin(ShapeType.Filled);
      renderer.set(ShapeType.Filled);
      renderer.setColor(color);
      
      while (curLen <= length)
      {
         curX = (sx+dirX*curLen);
         curY = (sy+dirY*curLen);
         
         //renderer.rectLine(curX,curY , curX+dirX*d1, curY+dirY*d1, width);
         renderer.circle(curX,curY, d1);
         curLen += (d1 + d2);
      }
      
      //renderer.end();
    }
		
	public static float getScreenValueX(float xvalue)
	{
		//return xvalue;
		
		float w = Gdx.graphics.getWidth();
		
		if(w<1300)
			return (float) (xvalue/1.05);

		if(w<1200)
			return (float) (xvalue/1.1);

		if(w<1100)
			return (float) (xvalue/1.2);
		
		return xvalue;
		
		//Default: 1920x1080
		//float delta=0;
		//delta = w/1920;
		//return delta*xvalue;
	}
	
	public static void drawFloorShadow(SpriteBatch spriteBatch, Texture texture, float x, float y, float w, float h, int iGui)
	{
		if(iGui==1)
		{
			spriteBatch.setColor(0f, 0f, 0f, 1f);
			spriteBatch.draw(texture, x-13, y-13, w+26, h+27);
	    	
			spriteBatch.setColor(0.2f, 0.2f, 0.2f, 1f);
			spriteBatch.draw(texture, x-10, y-10, w+20, h+21);
		}
		else if(iGui==2)
		{
			spriteBatch.setColor(0f, 0f, 0f, 1f);
			spriteBatch.draw(texture, x-3, y-3, w+6, h+6);
	    	
			spriteBatch.setColor(0.2f, 0.2f, 0.2f, 1f);
			spriteBatch.draw(texture, x-1, y-1, w+2, h+2);
		}
		else if(iGui==3)
		{
			spriteBatch.setColor(0f, 0f, 0f, 1f);
			spriteBatch.draw(texture, x-6, y-6, w+12, h+12);
	    	
			spriteBatch.setColor(0.2f, 0.2f, 0.2f, 1f);
			spriteBatch.draw(texture, x-4, y-4, w+8, h+8);
		}	
		else
		{
			spriteBatch.setColor(0f, 0f, 0f, 1f);
			spriteBatch.draw(texture, x-23, y-23, w+46, h+46);
			
			//spriteBatch.setColor(0.2f, 0.2f, 0.2f, 1f);
			//spriteBatch.setColor(1f, 1f, 1f, 0.2f);
			spriteBatch.setColor(1f, 1f, 1f, 1f);
			spriteBatch.draw(texture, x-20, y-20, w+40, h+40);
	    	
	    	spriteBatch.setColor(0.5f, 0.5f, 0.5f, 1f);
	    	spriteBatch.draw(texture, x-12, y-12, w+24, h+24);
	    	
	    	spriteBatch.setColor(0, 0, 0, 0.2f);
		}
	}
		
	public static float getFPSValue(float value)
	{
		int fps = Gdx.graphics.getFramesPerSecond();
		if(fps==0) //zum Start bekommt man eine Weile 0 zurück
			fps=60;
		
		float delta = 60.0f / (float)fps;
		
		return value*delta;
	}
	
	public static int getFPSValue(int value)
	{
		int fps = Gdx.graphics.getFramesPerSecond();
		if(fps==0) //zum Start bekommt man eine Weile 0 zurück
			fps=60;
		float delta = 60.0f / (float)fps;
		return MathUtils.round(value*delta);
	}
	
	public static int screenToLibGDX(int y)
	{
		return Gdx.graphics.getHeight() - y;
	}
	
	public static Vector3 getMousePosition(OrthographicCamera gameCamera)
	{
		float px = Gdx.input.getX();
		float py = Gdx.input.getY();
	    Vector3 c0 = new Vector3(px,py,0);
	    c0 = gameCamera.unproject(c0);
	    return c0;
	}
	
	
	public static Object cloneObject(Object obj)
	{
        try{
            Object clone = obj.getClass().newInstance();
        	
            for (java.lang.reflect.Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if(field.get(obj) == null || Modifier.isFinal(field.getModifiers())){
                    continue;
                }
                if(field.getType().isPrimitive() || field.getType().equals(String.class)
                        || field.getType().getSuperclass().equals(Number.class)
                        || field.getType().equals(Boolean.class)){
                    field.set(clone, field.get(obj));
                }else{
                    Object childObj = field.get(obj);
                    if(childObj == obj){
                        field.set(clone, clone);
                    }else{
                        field.set(clone, cloneObject(field.get(obj)));
                    }
                }
            }
            return clone;
        }catch(Exception e){
        	//e.printStackTrace();
        	CHelper.writeError(e.getMessage(), e.getStackTrace(), e);
        }		
        return null;
	}
	
	//Achtung: die Basekoordinaten müssen der mittelpunkt des objektes sein das rotiert wird
	//NICHT MEHR BENUTZEN!!! NEU: moveVectorByRotationS2D
	public static Vector2 moveVectorByRotation(int rotation, int moveX, int moveY, int middlepointX, int middlepointY) //Wrapper Function for moveVectorByRotation
	{
		int bx = middlepointX;
		int by = middlepointY;
		
		Vector2 returnVec=new Vector2();
		returnVec.x=middlepointX;
		returnVec.y=middlepointY;
		
		//0: up, 1: down, 2: right, 3: left
		if(moveX!=0)
		{
			int mdir=0;
			int mx=moveX;
			if(mx!=0)
			{
				if(mx>0)
					mdir=2;
				else
				{
					mdir=3;
					mx=mx*-1;
				}
			}
			
			Vector2 vec = new Vector2();
			vec.x=bx;
			vec.y=by;
			vec = CHelper.moveVectorByRotation(rotation, (int)(mx), vec, mdir);
			bx=(int) vec.x;
			by=(int) vec.y;
		}
		
		if(moveY!=0)
		{
			int mdir=0;
			int my=moveY;
			if(my!=0)
			{
				if(my>0)
					mdir=0;
				else
				{
					mdir=1;
					my=my*-1;
				}
			}						
			
			Vector2 vec = new Vector2();
			vec.x=bx;
			vec.y=by;
			vec = CHelper.moveVectorByRotation(rotation, (int)(my), vec, mdir);
			bx=(int) vec.x;
			by=(int) vec.y;
		}		
		
		returnVec.x=bx;
		returnVec.y=by;
		
		return returnVec;
	}

	public static Vector2 moveObjectByHumanRotation(CWorldObject humanObj, CWorldObject targetObj, int movex, int movey)
	{
		//Vector2 v2 = moveVectorByRotationS2D(humanObj.pos_x(), humanObj.pos_y(), Math.round(movex*humanObj.getBodySizeByAge()), Math.round(movey), Math.round(humanObj.width/2*humanObj.getBodySizeByAge()), Math.round(humanObj.height/2*humanObj.getBodySizeByAge()), humanObj.rotation());
		//Vector2 v2 = moveVectorByRotationS2D(humanObj.pos_x(), humanObj.pos_y(), Math.round(movex), Math.round(movey), Math.round(humanObj.width/2*humanObj.getBodySizeByAge()), Math.round(humanObj.height/2*humanObj.getBodySizeByAge()), humanObj.rotation());
		
		//getBodySizeByAge nur auf movex -> movey funktioniert hier nicht
		Vector2 v2 = moveVectorByRotationS2D(humanObj.pos_x(), humanObj.pos_y(), Math.round(movex*humanObj.getBodySizeByAge()), Math.round(movey), Math.round(humanObj.width/2*humanObj.getBodySizeByAge()), Math.round(humanObj.height/2*humanObj.getBodySizeByAge()), humanObj.rotation());
		
		if(targetObj==null)
			return v2;
		
		v2.x -= targetObj.width/2;
		v2.y -= targetObj.height/2;
		return v2;
	}
	
	public static Vector2 moveVectorByHumanRotation(CWorldObject humanObj, int targetObjectWidth, int targetObjectHeight, int movex, int movey)
	{
		//getBodySizeByAge nur auf movex -> movey funktioniert hier nicht
		Vector2 v2 = moveVectorByRotationS2D(humanObj.pos_x(), humanObj.pos_y(), Math.round(movex*humanObj.getBodySizeByAge()), Math.round(movey), Math.round(humanObj.width/2*humanObj.getBodySizeByAge()), Math.round(humanObj.height/2*humanObj.getBodySizeByAge()), humanObj.rotation());
		
		v2.x -= targetObjectWidth/2;
		v2.y -= targetObjectHeight/2;
		return v2;
	}
	
	public static Vector2 moveHumanByObjectRotation(CWorldObject baseObj, CWorldObject humanObj, int movex, int movey)
	{
		//Größe wird in entrfernumg eingerechnet zb human der wagen zieht
		//Vector2 v2 = moveVectorByRotationS2D(baseObj.pos_x(), baseObj.pos_y(), Math.round(movex*humanObj.getBodySizeByAge()), movey, Math.round(baseObj.width/2), Math.round(baseObj.height/2), baseObj.rotation());
		//v2.x -= humanObj.width/2*baseObj.getBodySizeByAge();
		//v2.y -= humanObj.height/2*baseObj.getBodySizeByAge();
		
		//Human größe wird nicht in entfernung eingerechnet
		Vector2 v2 = CHelper.moveVectorByRotationS2D(baseObj.pos_x(), baseObj.pos_y(), movex, movey, Math.round(baseObj.width/2), Math.round(baseObj.height/2), baseObj.rotation());
		v2.x-=humanObj.width_human()/2;
		v2.y-=humanObj.height_human()/2;
		
		return v2;
	}
	
	public static Vector2 moveVectorByRotationS2D(int parentx, int parenty, int childx, int childy, int originx, int originy, float rotation)
	{
		Actor actor1 = new Actor();
		Group group1 = new Group();
		//group1.setWidth(width);
		//group1.setHeight(height);
		group1.setPosition(parentx, parenty);
		group1.setOrigin(originx, originy);
		group1.setTransform(true);
		group1.rotateBy(0);
		//actor1.setSize(50, 50);
		actor1.setOrigin(0, 0);
		actor1.setPosition(childx, childy);
		actor1.setRotation(rotation);
		group1.addActor(actor1);
		group1.setRotation(rotation);
		Vector2 vl = new Vector2();
		vl.x=actor1.getX();
		vl.y=actor1.getY();
		vl = actor1.localToStageCoordinates(new Vector2(0, 0));
		return vl;
		
		
//		Actor actor1 = new Actor();
//		Group group1 = new Group();
//		//group1.setWidth(width);
//		//group1.setHeight(height);
//		group1.setPosition(parentx, parenty);
//		group1.setOrigin(originx, originy);
//		group1.setTransform(true);
//		//group1.rotateBy(0);
//		//actor1.setSize(50, 50);
//		actor1.setOrigin(0, 0);
//		actor1.setPosition(childx, childy);
//		//actor1.setRotation(rotation());
//		group1.addActor(actor1);
//		group1.setRotation(rotation);
//		Vector2 vl = new Vector2();
//		vl.x=actor1.getX();
//		vl.y=actor1.getY();
//		vl = actor1.localToStageCoordinates(new Vector2(0, 0));
//		
//		return vl;
	}
		
	public static String getAMPMString(int hour)
	{
		switch(hour)
		{
			case 0: return "AM";
			case 1: return "AM";
			case 2: return "AM";
			case 3: return "AM";
			case 4: return "AM";
			case 5: return "AM";
			case 6: return "AM";
			case 7: return "AM";
			case 8: return "AM";
			case 9: return "AM";
			case 10: return "AM";
			case 11: return "AM";
			
			case 12: return "PM";
			case 13: return "PM";
			case 14: return "PM";
			case 15: return "PM";
			case 16: return "PM";
			case 17: return "PM";
			case 18: return "PM";
			case 19: return "PM";
			case 20: return "PM";
			case 21: return "PM";
			case 22: return "PM";
			case 23: return "PM";
		}
		
		return "";
	}
	
	public static String getAMPMHourText(int hour)
	{
		switch(hour)
		{
			case -1: return "-";
			case 0: return "12 AM";
			case 1: return "01 AM";
			case 2: return "02 AM";
			case 3: return "03 AM";
			case 4: return "04 AM";
			case 5: return "05 AM";
			case 6: return "06 AM";
			case 7: return "07 AM";
			case 8: return "08 AM";
			case 9: return "09 AM";
			case 10: return "10 AM";
			case 11: return "11 AM";

			case 12: return "12 PM";
			case 13: return "01 PM";
			case 14: return "02 PM";
			case 15: return "03 PM";
			case 16: return "04 PM";
			case 17: return "05 PM";
			case 18: return "06 PM";
			case 19: return "07 PM";
			case 20: return "08 PM";
			case 21: return "09 PM";
			case 22: return "10 PM";
			case 23: return "11 PM";
		}
		
		return "-1";
	}

	public static int getAMPMHour(int hour)
	{
		switch(hour)
		{
			case 0: return 12;
			case 1: return 1;
			case 2: return 2;
			case 3: return 3;
			case 4: return 4;
			case 5: return 5;
			case 6: return 6;
			case 7: return 7;
			case 8: return 8;
			case 9: return 9;
			case 10: return 10;
			case 11: return 11;
			
			case 12: return 12;
			case 13: return 1;
			case 14: return 2;
			case 15: return 3;
			case 16: return 4;
			case 17: return 5;
			case 18: return 6;
			case 19: return 7;
			case 20: return 8;
			case 21: return 9;
			case 22: return 10;
			case 23: return 11;
		}
		
		return -1;
	}
	
	public static Boolean isMapScrolling(MapDirection mdir, CTown town)
	{
		if(town.gameGui.dlgShowing())
			return false;
		
		if(mdir==MapDirection.DOWN)
			if(Gdx.input.isKeyPressed(Keys.DOWN) || Gdx.input.isKeyPressed(Keys.S))
				return true;
		
		if(mdir==MapDirection.LEFT)
			if(Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A))
				return true;
			
		if(mdir==MapDirection.RIGHT)
			if(Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D))
				return true;
			
		if(mdir==MapDirection.UP)
			if(Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.W))
					return true;
		
		return false;
	}
	
	public static Boolean isMapScrolling(CTown town)
	{
		if(CHelper.isMapScrolling(MapDirection.DOWN, town))
			return true;
		if(CHelper.isMapScrolling(MapDirection.LEFT, town))
			return true;
		if(CHelper.isMapScrolling(MapDirection.RIGHT, town))
			return true;
		if(CHelper.isMapScrolling(MapDirection.UP, town))
			return true;

		return false;
	}
	
	public static Vector2 translateMoveVectorByRotationForHuman(int x, int y, CWorldObject human, float stateTime)
	{
		int px=(int)((int)x-human.theobject.objectAnimation.getKeyFrame(stateTime, true).getRegionWidth()/2*human.getBodySizeByAge());
		int py=(int)((int)y-human.theobject.objectAnimation.getKeyFrame(stateTime, true).getRegionHeight()/2*human.getBodySizeByAge());
		
		Vector2 v2 = new Vector2();
		v2.x=px;
		v2.y=py;
		
		return v2;
	}
	
	//Achtung: die Basekoordinaten(vec) müssen der mittelpunkt des objektes sein das rotiert wird
	//NICHT MEHR BENUTZEN!!! NEU: moveVectorByRotationS2D
	public static Vector2 moveVectorByRotation(int rotation, int value, Vector2 vec, int direction)
	{
		rotation = Math.round(rotation/10)*10; //auf 10er schritte runden
		if(rotation<0)
			rotation+=360;
		
		if(rotation>360)
			rotation-=360;
				
		//direction: 0: up, 1: down, 2: right, 3: left
		
		//Achtung für Anpassung:
		//	
		//		  0/360
		//
		//
		//	90		 		270
		//
		//    
		//		   180
		//		
		
		
		if(direction==3) //left
		{
			if(rotation==0)
			{
				vec.x-=value;
			}
			
			if(rotation==10)
			{
				vec.x-=value;
				vec.y-=value/3.2;
			}
			
			if(rotation==20)
			{
				vec.x-=value;
				vec.y-=value/2.5;
			}
			
			if(rotation==30)
			{
				vec.x-=value;
				vec.y-=value/1.8;
			}
			
			if(rotation==40)
			{
				vec.x-=value/1.5;
				vec.y-=value/1.5;
			}
			
			if(rotation==50)
			{
				vec.x-=value/1.5;
				vec.y-=value/1.5;
			}
			
			if(rotation==60)
			{
				vec.x-=value/1.8;
				vec.y-=value;
			}		
			
			if(rotation==70)
			{
				vec.x-=value/2.5;
				vec.y-=value;
			}
			
			if(rotation==80)
			{
				vec.x-=value/3.2;
				vec.y-=value;
			}		
			
			if(rotation==90)
				vec.y-=value;
			
			if(rotation==100)
			{
				vec.y-=value;
				vec.x+=value/4;
			}
			
			if(rotation==110)
			{
				vec.y-=value;
				vec.x+=value/2.5;
			}
			
			if(rotation==120)
			{
				vec.y-=value;
				vec.x+=value/1.8;
			}
			
			if(rotation==130)
			{
				vec.y-=value/1.5;
				vec.x+=value/1.5;
			}
			
			if(rotation==140)
			{
				vec.y-=value/1.5;
				vec.x+=value/1.5;
			}
			
			if(rotation==150)
			{
				vec.y-=value/1.8;
				vec.x+=value/1.3;
			}
			
			if(rotation==160)
			{
				vec.y-=value/2.5;
				vec.x+=value;
			}
			
			if(rotation==170)
			{
				vec.y-=value/3.2;
				vec.x+=value;
			}
			
			if(rotation==180)
				vec.x+=value;
			
//			if(rotation==190)
//			{
//				vec.y+=value;
//				vec.x+=value/4;
//			}
//			
//			if(rotation==200)
//			{
//				vec.y+=value;
//				vec.x+=value/3;
//			}
//			
//			if(rotation==210)
//			{
//				vec.y+=value;
//				vec.x+=value/2;
//			}
			
			//cfm1
			if(rotation==190)
			{
				vec.y+=value/3.2;
				vec.x+=value;
			}
			
			if(rotation==200)
			{
				vec.y+=value/2.5;
				vec.x+=value;
			}
			
			if(rotation==210)
			{
				vec.y+=value/1.8;
				vec.x+=value;
			}			
			
			if(rotation==220)
			{
				vec.y+=value/1.5;
				vec.x+=value/1.5;
			}
			
			if(rotation==230)
			{
				vec.x+=value/1.5;
				vec.y+=value/1.5;
			}
			
//			if(rotation==240)
//			{
//				vec.y+=value/2;
//				vec.x+=value;
//			}
//			
//			if(rotation==250)
//			{
//				vec.y+=value/3;
//				vec.x+=value;
//			}
//			
//			if(rotation==260)
//			{
//				vec.y+=value/4;
//				vec.x+=value;
//			}
			
			if(rotation==240)
			{
				vec.y+=value;
				vec.x+=value/1.8;
			}
			
			if(rotation==250)
			{
				vec.y+=value;
				vec.x+=value/2.5;
			}
			
			if(rotation==260)
			{
				vec.y+=value;
				vec.x+=value/3.2;
			}
			
			if(rotation==270)
				vec.y+=value;
			
			if(rotation==280)
			{
				vec.x-=value/3.2;
				vec.y+=value;
			}
			
			if(rotation==290)
			{
				vec.x-=value/2.5;
				vec.y+=value;
			}
			
			if(rotation==300)
			{
				vec.x-=value/1.8;
				vec.y+=value;
			}
			
			if(rotation==310)
			{
				vec.x-=value/1.5;
				vec.y+=value/1.5;
			}
			
			if(rotation==320)
			{
				vec.x-=value/1.5;
				vec.y+=value/1.5;
			}
			
			if(rotation==330)
			{
				vec.x-=value;
				vec.y+=value/1.8;
			}
			
			if(rotation==340)
			{
				vec.x-=value;
				vec.y+=value/2.5;
			}
			
			if(rotation==350)
			{
				vec.x-=value;
				vec.y+=value/3.2;
			}	
			
			if(rotation==360)
				vec.x-=value;
		}
				
		
		if(direction==2) //right
		{
			if(rotation==0)
			{
				vec.x+=value;
			}
			
			if(rotation==10)
			{
				vec.x+=value;
				vec.y+=value/4;
			}
	
			if(rotation==20)
			{
				vec.x+=value;
				vec.y+=value/3;
			}
	
			if(rotation==30)
			{
				vec.x+=value;
				vec.y+=value/2;
			}
			
			if(rotation==40)
			{
				vec.x+=value/1.5;
				vec.y+=value/1.5;
			}
			
			if(rotation==50)
			{
				vec.x+=value/1.5;
				vec.y+=value/1.5;
			}
			
			if(rotation==60)
			{
				vec.x+=value/2;
				vec.y+=value;
			}		
	
			if(rotation==70)
			{
				vec.x+=value/3;
				vec.y+=value;
			}		
			
			if(rotation==80)
			{
				vec.x+=value/4;
				vec.y+=value;
			}		
			
			if(rotation==90)
				vec.y+=value;
	
			if(rotation==100)
			{
				vec.y+=value;
				vec.x-=value/4;
			}
	
			if(rotation==110)
			{
				vec.y+=value;
				vec.x-=value/3;
			}
	
			if(rotation==120)
			{
				vec.y+=value;
				vec.x-=value/2;
			}
			
			if(rotation==130)
			{
				vec.y+=value/1.5;
				vec.x-=value/1.5;
			}
			
			if(rotation==140)
			{
				vec.y+=value/1.5;
				vec.x-=value/1.5;
			}
			
			if(rotation==150)
			{
				vec.y+=value/2;
				vec.x-=value;
			}
			
			if(rotation==160)
			{
				vec.y+=value/3;
				vec.x-=value;
			}
			
			if(rotation==170)
			{
				vec.y+=value/4;
				vec.x-=value;
			}
			
			if(rotation==180)
				vec.x-=value;
	
			if(rotation==190)
			{
				vec.x-=value;
				vec.y-=value/4;
			}
			
			if(rotation==200)
			{
				vec.x-=value;
				vec.y-=value/3;
			}
			
			if(rotation==210)
			{
				vec.x-=value;
				vec.y-=value/2;
			}
	
			if(rotation==220)
			{
				vec.x-=value/1.5;
				vec.y-=value/1.5;
			}
	
			if(rotation==230)
			{
				vec.x-=value/1.5;
				vec.y-=value/1.5;
			}
	
			if(rotation==240)
			{
				vec.x-=value/2;
				vec.y-=value;
			}
			
			if(rotation==250)
			{
				vec.x-=value/3;
				vec.y-=value;
			}
			
			if(rotation==260)
			{
				vec.x-=value/4;
				vec.y-=value;
			}
			
			if(rotation==270)
				vec.y-=value;
	
			if(rotation==280)
			{
				vec.y-=value;
				vec.x+=value/4;
			}
			
			if(rotation==290)
			{
				vec.y-=value;
				vec.x+=value/3;
			}
	
			if(rotation==300)
			{
				vec.y-=value;
				vec.x+=value/2;
			}
			
			if(rotation==310)
			{
				vec.y-=value/1.5;
				vec.x+=value/1.5;
			}
	
			if(rotation==320)
			{
				vec.y-=value/1.5;
				vec.x+=value/1.5;
			}
	
			if(rotation==330)
			{
				vec.y-=value/2;
				vec.x+=value;
			}
			
			if(rotation==340)
			{
				vec.y-=value/3;
				vec.x+=value;
			}
			
			if(rotation==350)
			{
				vec.y-=value/4;
				vec.x+=value;
			}	
			
			if(rotation==360)
				vec.x+=value;
		}
		
		
		if(direction==1) //down
		{
			if(rotation==0)
			{
				vec.y-=value;
			}
	
			if(rotation==10)
			{
				vec.y-=value;
				vec.x+=value/3.2;
			}
	
			if(rotation==20)
			{
				vec.y-=value;
				vec.x+=value/2.5;
			}
	
			if(rotation==30)
			{
				vec.y-=value;
				vec.x+=value/1.8;
			}
			
			if(rotation==40)
			{
				vec.y-=value/1.5;
				vec.x+=value/1.5;
			}
			
			if(rotation==50)
			{
				vec.y-=value/1.5;
				vec.x+=value/1.5;
			}
			
			if(rotation==60)
			{
				vec.y-=value/1.8f;
				vec.x+=value/1.3;
			}		
	
			if(rotation==70)
			{
				vec.y-=value/2.2;
				vec.x+=value/1.1;
			}		
			
			if(rotation==80)
			{
				vec.y-=value/2.8f;
				vec.x+=value;
			}		
			
			if(rotation==90)
				vec.x+=value/1.2;
	
			if(rotation==100)
			{
				vec.x+=value/1.6;
				vec.y+=value/4;
				
				
//				vec.x+=value/2.2;
//				vec.y+=value/2.8;
			}
	
			if(rotation==110)
			{
				vec.x+=value/1.5;
				vec.y+=value/3;
			}
	
			if(rotation==120)
			{
				vec.x+=value/1.7;
				vec.y+=value/2;
			}
			
			if(rotation==130)
			{
				vec.x+=value/2.2;
				vec.y+=value/1.6;
			}
			
			if(rotation==140)
			{
				vec.x+=value/1.7;
				vec.y+=value/1.5;
			}
			
			if(rotation==150)
			{
				vec.x+=value/1.8;
				vec.y+=value/1.5;
			}
			
			if(rotation==160)
			{
				vec.x+=value/2.7;
				vec.y+=value/1.4;
			}
			
			if(rotation==170)
			{
				vec.x+=value/3.2;
				vec.y+=value/1.2;
			}
			
			if(rotation==180)
			{
				vec.y+=value/1.2;
				
			}
			
			
			//--
			if(rotation==190)
			{
				vec.y+=value/1.7;
				vec.x-=value/3.2;
			}
			
			if(rotation==200)
			{
				vec.y+=value/1.4;
				vec.x-=value/2.5;
			}
			
			if(rotation==210)
			{
				vec.y+=value/1.2;
				vec.x-=value/1.8;
			}
	
			if(rotation==220)
			{
				vec.y+=value/1.6;
				vec.x-=value/1.6;
			}
	
			if(rotation==230)
			{
				vec.y+=value/1.5;
				vec.x-=value/1.5;
			}
	
			if(rotation==240)
			{
				vec.y+=value/1.8;
				vec.x-=value/1.3;
			}
			
			if(rotation==250)
			{
				vec.y+=value/2.5;
				vec.x-=value/1.1;
			}
			
			if(rotation==260)
			{
				vec.y+=value/3.2;
				vec.x-=value;
			}
			
			if(rotation==270)
				vec.x-=value;
			//---
			
			
			
			
			
			if(rotation==280)
			{
				vec.x-=value;
				vec.y-=value/3.2;
			}
			
			if(rotation==290)
			{
				vec.x-=value;
				vec.y-=value/2.5;
			}
	
			if(rotation==300)
			{
				vec.x-=value;
				vec.y-=value/1.8;
			}
			
			if(rotation==310)
			{
				vec.x-=value/1.5;
				vec.y-=value/1.5;
			}
	
			if(rotation==320)
			{
				vec.x-=value/1.5;
				vec.y-=value/1.5;
			}
	
			if(rotation==330)
			{
				vec.x-=value/1.8;
				vec.y-=value;
			}
			
			if(rotation==340)
			{
				vec.x-=value/2.5;
				vec.y-=value;
			}
			
			if(rotation==350)
			{
				vec.x-=value/3.2;
				vec.y-=value;
			}	
			
			if(rotation==360)
				vec.y-=value;
		}
		
		
		if(direction==0) //up
		{
			if(rotation==0)
			{
				vec.y+=value;
			}
	
			if(rotation==10)
			{
				vec.y+=value;
				vec.x-=value/3.2;
			}
	
			if(rotation==20)
			{
				vec.y+=value;
				vec.x-=value/2.5;
			}
	
			if(rotation==30)
			{
				vec.y+=value;
				vec.x-=value/1.8;
			}
			
			if(rotation==40)
			{
				vec.y+=value/1.5;
				vec.x-=value/1.5;
			}
			
			if(rotation==50)
			{
				vec.y+=value/1.5;
				vec.x-=value/1.5;
			}
			
			if(rotation==60)
			{
				vec.y+=value/1.8;
				vec.x-=value;
			}		
	
			if(rotation==70)
			{
				vec.y+=value/2.5;
				vec.x-=value;
			}		
			
			if(rotation==80)
			{
				vec.y+=value/3.2;
				vec.x-=value;
			}		
			
			if(rotation==90)
				vec.x-=value;
	
			if(rotation==100)
			{
				vec.x-=value;
				vec.y-=value/3.2;
			}
	
			if(rotation==110)
			{
				vec.x-=value;
				vec.y-=value/2.5;
			}
	
			if(rotation==120)
			{
				vec.x-=value;
				vec.y-=value/1.8;
			}
			
			if(rotation==130)
			{
				vec.x-=value/1.5;
				vec.y-=value/1.5;
			}
			
			if(rotation==140)
			{
				vec.x-=value/1.5;
				vec.y-=value/1.5;
			}
			
			if(rotation==150)
			{
				vec.x-=value/2;
				vec.y-=value;
			}
			
			if(rotation==160)
			{
				vec.x-=value/3;
				vec.y-=value;
			}
			
			if(rotation==170)
			{
				vec.x-=value/4;
				vec.y-=value;
			}
			
			if(rotation==180)
				vec.y-=value;
	
			if(rotation==190)
			{
				vec.y-=value;
				vec.x+=value/4;
			}
			
			if(rotation==200)
			{
				vec.y-=value;
				vec.x+=value/3;
			}
			
			if(rotation==210)
			{
				vec.y-=value;
				vec.x+=value/2;
			}
	
			if(rotation==220)
			{
				vec.y-=value/1.5;
				vec.x+=value/1.5;
			}
	
			if(rotation==230)
			{
				vec.y-=value/1.5;
				vec.x+=value/1.5;
			}
	
			if(rotation==240)
			{
				vec.y-=value/2;
				vec.x+=value;
			}
			
			if(rotation==250)
			{
				vec.y-=value/3;
				vec.x+=value;
			}
			
			if(rotation==260)
			{
				vec.y-=value/4;
				vec.x+=value;
			}
			
			if(rotation==270)
				vec.x+=value;
	
			if(rotation==280)
			{
				vec.x+=value;
				vec.y+=value/4;
			}
			
			if(rotation==290)
			{
				vec.x+=value;
				vec.y+=value/3;
			}
	
			if(rotation==300)
			{
				vec.x+=value;
				vec.y+=value/2;
			}
			
			if(rotation==310)
			{
				vec.x+=value/1.5;
				vec.y+=value/1.5;
			}
	
			if(rotation==320)
			{
				vec.x+=value/1.5;
				vec.y+=value/1.5;
			}
	
			if(rotation==330)
			{
				vec.x+=value/2;
				vec.y+=value;
			}
			
			if(rotation==340)
			{
				vec.x+=value/3;
				vec.y+=value;
			}
			
			if(rotation==350)
			{
				vec.x+=value/4;
				vec.y+=value;
			}
			
			if(rotation==360)
				vec.y+=value;			
		}
		
		return vec;
	}
	
	public static int getEuclidianDistance(int x1, int y1, int x2, int y2)
	{
		return (int) Math.sqrt((double)( x1 - x2 )*( x1 - x2 ) + ( y1 - y2 )*( y1 - y2));
	}
	
	public static int getEuclidianDistance(CWorldObject obj1, CWorldObject obj2)
	{
		return (int) Math.sqrt((double)( (obj1.pos_x()+obj1.width/2) - (obj2.pos_x()+obj2.width/2) )*( (obj1.pos_x()+obj1.width/2) - (obj2.pos_x()+obj2.width/2) ) + ( (obj1.pos_y()+obj1.height/2) - (obj2.pos_y()+obj2.height/2) )*( (obj1.pos_y()+obj1.height/2) - (obj2.pos_y()+obj2.height/2)));
	}
	
	public static float getRandomFloat(float min, float max, Random rand)
	{
		return rand.nextFloat() * (max - min) + min;
	}
	
	public static float roundFloat(float nr, int nachkommastellen)//auf nachkommastellen runden
	{
		float x1=0;
		
		if(nachkommastellen==3)
			x1=1000;
		
		if(nachkommastellen==2)
			x1=100;

		if(nachkommastellen==1)
			x1=10;
		
		return Math.round(nr*x1)/x1;	
	}
	

	
//	PolygonSprite poly;
//	PolygonSpriteBatch polyBatch = new PolygonSpriteBatch(); // To assign at the beginning
//	Texture textureSolid;
//
//	// Creating the color filling (but textures would work the same way)
//	Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
//	pix.setColor(0xDEADBEFF); // DE is red, AD is green and BE is blue.
//	pix.fill();
//	textureSolid = new Texture(pix);
//	PolygonRegion polyReg = new PolygonRegion(new TextureRegion(textureSolid),
//	  new float[] {      // Four vertices
//	    0, 0,            // Vertex 0         3--2
//	    100, 0,          // Vertex 1         | /|
//	    100, 100,        // Vertex 2         |/ |
//	    0, 100           // Vertex 3         0--1
//	}, new short[] {
//	    0, 1, 2,         // Two triangles using vertex indices.
//	    0, 2, 3          // Take care of the counter-clockwise direction. 
//	});
//	poly = new PolygonSprite(polyReg);
//	poly.setOrigin(a, b);
//	polyBatch = new PolygonSpriteBatch();
	
	
//	public static void initErrorLogging()
//	{
//		FileHandle file = Gdx.files.local("errorlog/error.log");
//		file.delete();
//	}
	
	public static void writeError(String logtext, StackTraceElement[] stack, Exception e)
	{
		try
		{
			if(logtext==null)
				logtext="Errormessage: null";
			
			if(logtext.contains("SpriteBatch.end must be called before begin"))
				return;
			
			if(e!=null)
				e.printStackTrace();
			
			Date date = new Date();
			
			String ls = System.getProperty("line.separator");
			
			String str="";
			if(stack!=null)
			{
				for(StackTraceElement st : stack)
				{
					str+="Stacktrace: " + st.getClassName() + ", " + st.getFileName() + ", " + st.getMethodName() + ", line: " + st.getLineNumber();
					str+=ls;
				}
			}
			
			DateFormat dateFormatFile = new SimpleDateFormat("yyyyMMddHHmmss");
			//FileHandle file = Gdx.files.local("data/errorlog/error_"+dateFormatFile.format(date)+".log");
			FileHandle file = CHelper.getFileHandle("appdata/local/HTP/data/errorlog/error_"+dateFormatFile.format(date)+".log");
			//file.delete();
			
			BufferedWriter bw = new BufferedWriter(file.writer(true));
			
			if(file!=null && bw!=null && logtext!=null)
			{
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				bw.write(dateFormat.format(date));
				bw.write(ls);
				bw.write(logtext);
				bw.write(ls);
				bw.write(str);
				bw.write(ls);
				bw.close();
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
//Collision detection, intersection
	//   Rectangle bounds;
	//   Polygon polygon;
	//   
	//   Rectangle bounds2;
	//   Polygon polygon2;
	//
	//   public void create() {
	//
	//      bounds = new Rectangle(0, 0, 32, 20);
	//      polygon = new Polygon(new float[]{0,0,bounds.width,0,bounds.width,bounds.height,0,bounds.height});
	//      polygon.setOrigin(bounds.width/2, bounds.height/2);
	//      
	//      bounds2 = new Rectangle(0, 0, 32, 20);
	//      polygon2 = new Polygon(new float[]{0,0,bounds2.width,0,bounds2.width,bounds2.height,0,bounds2.height});
	//      polygon2.setOrigin(bounds2.width/2, bounds2.height/2);
	//}
	//   @Override
	//   public void render() {
	//      polygon.setPosition(car1.x, car1.y);
	//      polygon.setRotation(car1.rotation);
	//      polygon2.setPosition(car2.x, car2.y);
	//      polygon2.setRotation(car2.rotation);
	//        if(Intersector.overlapConvexPolygons(polygon, polygon2)){
	//            //COLLISION DON'T HAPPEN!!!
	//        }
	//}
	
	public static CWorldObject findActiveAndAvailableObjectByActionstring(String editorActionString, List<CWorldObject> objectList)
	{
		//Optional<CWorldObject> opt = objectList.stream().filter(item->item.theobject.editoraction.contains(editorActionString) && !item.bDeleted && item.isOccupiedBy==null && item.isOccupiedByExtern==null).findFirst();
		Optional<CWorldObject> opt = objectList.stream().filter(item->item.theobject.editoraction.contains(editorActionString) && !item.bDeleted && item.isOccupiedBy==null && item.isOccupiedByExtern==null && item.isActiveByEnergyConsumption() && item.isActiveByWaterConsumption()).findFirst();
		if(opt.isPresent())
		{
			//if(!opt.get().isActiveByEnergyConsumption() || !opt.get().isActiveByWaterConsumption())  
			//	return null;
			return opt.get();
		}
		
		return null;
	}
}
