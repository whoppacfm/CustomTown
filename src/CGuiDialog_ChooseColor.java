package com.mygdx.game;

import java.util.List;
import java.util.Optional;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.mygdx.game.CGuiControl_Button.ButtonType;

public class CGuiDialog_ChooseColor extends CGuiDialog{
	SpriteBatch dlgSpriteBatch;
	ShapeRenderer dlgShapeRenderer;
	BitmapFont dlgFont;
	BitmapFont dlgFont2;
	CTown town;
	
	private CGuiControl_Slider slider_r;
	private CGuiControl_Slider slider_g;
	private CGuiControl_Slider slider_b;
	
	private CGuiControl_Button buttonOK;
	private CGuiControl_Button buttonCancel;
	
	float color_r;
	float color_g;
	float color_b;
	float slider_mult;
	
	float calcval=0.003921568f;
	
	Color tempcolor;
	Color origcolor;
	
	public CGuiDialog_ChooseColor(List<CObject> controlList, BitmapFont font, int dialogX, int dialogY, CTown town1)
	{
		super("CGuiDialog_Worktime");
		
		tempcolor=null;
		origcolor=null;
		
		town=town1;
		dlgSpriteBatch = town1.gameGui.editorSpriteBatch; 
		dlgShapeRenderer = town1.gameGui.shapeRenderer; 
		
		dlgFont=font;
		dlgFont2=town.gameFont.bfArial;
		
		dlgX=dialogX;
		dlgY=dialogY;
		dialogW=180;
		dialogH=120;
		
		setMiddlePosition();
		
		CObject sliderObj = ((Optional<CObject>)controlList.stream().filter(p->p.editoraction.equals("control_button_speed1")).findFirst()).get();			
		CObject sliderButton = ((Optional<CObject>)controlList.stream().filter(p->p.editoraction.equals("control_button_speed2")).findFirst()).get();
		
		slider_mult=0.6f;
		slider_r = new CGuiControl_Slider(dlgX+13, dlgY+dialogH-110, 0, 255*slider_mult, 255*slider_mult, sliderObj, sliderButton, town1);
		slider_g = new CGuiControl_Slider(dlgX+130, dlgY+dialogH-110, 0, 255*slider_mult, 255*slider_mult, sliderObj, sliderButton, town1);
		slider_b = new CGuiControl_Slider(dlgX+13, dlgY+dialogH-110, 0, 255*slider_mult, 255*slider_mult, sliderObj, sliderButton, town1);
		
		buttonOK=new CGuiControl_Button(dlgX+dialogW/2-105, dlgY+15-2, 100, 25, 43, 18, "ok", dlgFont, null, CGuiControl_Button.ButtonType.DEFAULT, town1);
		buttonCancel=new CGuiControl_Button(dlgX+dialogW/2+5, dlgY+15-2, 100, 25, 31, 18, "cancel", dlgFont, null, CGuiControl_Button.ButtonType.DEFAULT, town1);
	}
	
	public void showDlg(Boolean bShow)
	{
		dialogH=205;
		dialogW=242;
		super.showDlg(bShow);
		setMiddlePosition();
		
		buttonOK.setPosition(buttonOK.controlX, dlgY+15-2);
		buttonCancel.setPosition(buttonCancel.controlX, dlgY+15-2);
		
		CWorldObject mobj=town.gameWorld.markerObject;
		
		if(bShow)
		{
			if(mobj.color1!=null)
				origcolor=new Color(mobj.color1.r,mobj.color1.g,mobj.color1.b,1);
			else
				origcolor=new Color(1,1,1,1);
			
			if(mobj.color1!=null && (mobj.color1.r!=1 || mobj.color1.g!=1 || mobj.color1.b!=1))
			{
				tempcolor=new Color(mobj.color1.r, mobj.color1.g, mobj.color1.b, 1);
				slider_r.setValue((mobj.color1.r/calcval)*slider_mult);
				slider_g.setValue((mobj.color1.g/calcval)*slider_mult);
				slider_b.setValue((mobj.color1.b/calcval)*slider_mult);
			}
			else if(tempcolor!=null)
			{
				slider_r.setValue((tempcolor.r/calcval)*slider_mult);
				slider_g.setValue((tempcolor.g/calcval)*slider_mult);
				slider_b.setValue((tempcolor.b/calcval)*slider_mult);
				mobj.color1=new Color(tempcolor.r, tempcolor.g, tempcolor.b, 1);
			}
			else
				tempcolor=new Color(1,1,1,1);
		}
	}
	
	public int getColor_r()
	{
		return Math.round(slider_r.getValue()/slider_mult);
	}
	
	public int getColor_g()
	{
		return Math.round(slider_g.getValue()/slider_mult);
	}
	
	public int getColor_b()
	{
		return Math.round(slider_b.getValue()/slider_mult);
	}
	
	public Boolean buttonDown(int x, int y, int libgdxy, int button)
	{
		CWorldObject mobj=town.gameWorld.markerObject;
		
		if(button==0)
		{
			Boolean bChangeColor=false;
			
			if(slider_r.buttonDown(x, y, libgdxy, button))
			{
				bChangeColor=true;
			}
			
			if(slider_g.buttonDown(x, y, libgdxy, button))
			{
				bChangeColor=true;
			}
			
			if(slider_b.buttonDown(x, y, libgdxy, button))
			{
				bChangeColor=true;
			}
			
			if(bChangeColor)
			{
				return true;
			}
			
			if(buttonOK.buttonClick(x, libgdxy) || button==-99)
			{
				town.gameWorld.changeTownMoney(-town.initial_paintobject_price);
				
				tempcolor.r=mobj.color1.r;
				tempcolor.g=mobj.color1.g;
				tempcolor.b=mobj.color1.b;
				
				showDlg(false);
				
				return true;
			}
			
			if(buttonCancel.buttonClick(x, libgdxy))
			{
				if(origcolor!=null)
					mobj.color1=new Color(origcolor.r, origcolor.g, origcolor.b, 1);
				
				showDlg(false);
				
				return true;
			}
		}
		else
		{
			if(origcolor!=null)
				mobj.color1=new Color(origcolor.r, origcolor.g, origcolor.b, 1);
			
			showDlg(false);
		}
		
		return true;
	}
	
	public void buttonUp()
	{
		slider_r.buttonUp();
		slider_g.buttonUp();
		slider_b.buttonUp();
	}
	
	public Boolean mouseMovedDrag(int x, int y, int libgdxy)
	{
		Boolean bChangeColor=false;
		
		if(slider_r.mouseMovedDrag(x, y, libgdxy))
			bChangeColor=true;
		
		if(slider_g.mouseMovedDrag(x, y, libgdxy))
			bChangeColor=true;
		
		if(slider_b.mouseMovedDrag(x, y, libgdxy))
			bChangeColor=true;
		
		if(bChangeColor)
		{
			town.gameWorld.markerObject.color1=new Color(getColor_r()*calcval, getColor_g()*calcval, getColor_b()*calcval, 1);
			return true;
		}
		
		return false;
	}
		
	public void render(int x, int libgdxy)
	{
		int sliderAndText_PosY=20;
		
		//if(!Gdx.gl.glIsEnabled(GL30.GL_BLEND))
		{
			Gdx.gl.glEnable(GL30.GL_BLEND);
			Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
		}	    
		dlgShapeRenderer.setAutoShapeType(true);
	    dlgShapeRenderer.begin();
			
			//Dialog Background
		    dlgShapeRenderer.set(ShapeType.Filled);
		    dlgShapeRenderer.setColor(town.dialogColor.r, town.dialogColor.g, town.dialogColor.b, 0.2f);
		    dlgShapeRenderer.rect(dlgX, dlgY, dialogW, dialogH);
		    
		    //Show Color
		    //float val=0.003921568f;
		    //dlgShapeRenderer.set(ShapeType.Filled);
		    //dlgShapeRenderer.setColor(getColor_r()*val, getColor_g()*val, getColor_b()*val, 1);
		    //dlgShapeRenderer.rect(dlgX, dialogH-200, 200, 200);
		    
			//Dialog Rahmen
		    dlgShapeRenderer.set(ShapeType.Line);
			dlgShapeRenderer.setColor(town.dialogRahmenColor);
			dlgShapeRenderer.rect(dlgX, dlgY, dialogW, dialogH);
			
			//Trennlinie zu Save as default
			//int ty=dlgY+50+40;
			//dlgShapeRenderer.setColor(town.dialogRahmenColor);
			//dlgShapeRenderer.line(dlgX+10, ty+33, dlgX+dialogW-10, ty+33);
			//dlgShapeRenderer.line(dlgX+10, ty+10+30, dlgX+dialogW-10, ty+10+30);
			
			//Trennlinie zu ok, cancel
			dlgShapeRenderer.setColor(town.dialogRahmenColor);
			int ty2=dlgY+50;
			dlgShapeRenderer.line(dlgX+10, ty2, dlgX+dialogW-10, ty2);				
		dlgShapeRenderer.end();
		//Gdx.gl.glDisable(GL30.GL_BLEND);		
		
		int posy=73;
		
		dlgSpriteBatch.begin();
			dlgSpriteBatch.setColor(1,1,1,1);
			dlgFont2.setColor(town.dialogFontColorList);
						
			dlgSpriteBatch.setShader(town.gameFont.fontShader);
			dlgFont2.getData().setScale(0.6f);
			dlgFont2.draw(dlgSpriteBatch, getColor_r()+"", slider_r.controlX+slider_r.getWidth()+10, dlgY+dialogH-94+posy);
			dlgFont2.draw(dlgSpriteBatch, getColor_g()+"", slider_g.controlX+slider_r.getWidth()+10, dlgY+dialogH-94-50+posy);
			dlgFont2.draw(dlgSpriteBatch, getColor_b()+"", slider_b.controlX+slider_r.getWidth()+10, dlgY+dialogH-94-100+posy);
			dlgSpriteBatch.setShader(null);
			
			slider_r.setPosition(dlgX+13-2, dlgY+dialogH-110+posy);
			slider_g.setPosition(dlgX+13-2, dlgY+dialogH-110-50+posy);
			slider_b.setPosition(dlgX+13-2, dlgY+dialogH-110-100+posy);
		dlgSpriteBatch.end();
		
		slider_r.render();
		slider_g.render();
		slider_b.render();
		
		buttonOK.render(x, libgdxy);
		buttonCancel.render(x, libgdxy);
	}
}
