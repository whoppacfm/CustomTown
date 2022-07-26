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
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.mygdx.game.CGuiControl_Button.ButtonType;
import com.sun.corba.se.spi.legacy.connection.GetEndPointInfoAgainException;

public class CGuiDialog_Gfx extends CGuiDialog{
	
	SpriteBatch dlgSpriteBatch;
	ShapeRenderer dlgShapeRenderer;
	BitmapFont dlgFont;
	BitmapFont dlgFont2;
	
	CTown town;
	
	//private CGuiControl_Slider slider_postprocessor;
	//private CGuiControl_Slider slider_framebuffer;
	
	private CGuiControl_Slider slider_mmRoad;
	private CGuiControl_Slider slider_mmGround;
	private CGuiControl_Slider slider_mmTree;
	private CGuiControl_Slider slider_mmOther;
	private CGuiControl_Slider slider_mmFloor;
	
	private CGuiControl_Slider slider_lod;
	private CGuiControl_Slider slider_thread;
	
	private CGuiControl_Slider slider_wateranimation;
	
	private CGuiControl_Button buttonOK;
	private CGuiControl_Button buttonCancel;
	
	//private CGuiControl_Button buttonMMRoad;
	//private CGuiControl_Button buttonMMTree;
	//private CGuiControl_Button buttonMMGround;
	
	float value_postprocessor;
	float value_framebuffer;
	
	int value_mmground;
	int value_mmroad;
	int value_mmtree;
	int value_mmother;
	int value_mmfloor;
	int value_lod;
	int value_thread;
	int value_wateranimation;
	
	Boolean bMovePValue;
	Boolean bMoveFValue;
	
	float slider_mult_postprocessing;
	float slider_mult_framebuffer;
	float slider_mult_mm;
	float slider_mult_lod;
	float slider_mult_thread;
	float slider_mult_wateranimation;
	
	int postprocessing_posy=0;
	int framebuffer_posy=51;
	
	int mmGround_posy=150-100;
	int mmTree_posy=200-100;
	int mmRoad_posy=250-100;
	int mmFloor_posy=300-100;
	int mmOther_posy=350-100;
	int lod_posy=400-100;
	int thread_posy=350;
	int wateranimation_posy=400;
	int mmy=105;
	int gx=50;
	
	public CGuiDialog_Gfx(List<CObject> controlList, int dialogX, int dialogY, CTown town1)
	{
		super("CGuiDialog_Soundvolume");
		
		town=town1;
		dlgSpriteBatch = town1.gameGui.editorSpriteBatch;
		dlgShapeRenderer = town1.gameGui.shapeRenderer;
		
		dlgFont2=town.gameFont.bfArial2;
		dlgFont=town.gameFont.fontSmall;
		
		dlgX=dialogX;
		dlgY=dialogY;
		dialogW=435;
		dialogH=287+80+10+10+60+30;
		
		bMovePValue=false;
		bMoveFValue=false;
		
		value_postprocessor=-1;
		value_framebuffer=-1;
		value_mmground=-1;
		value_mmother=-1;
		value_mmroad=-1;
		value_mmtree=-1;
		value_mmfloor=-1;
		value_lod=-1;
		value_thread=-1;
		value_wateranimation=-1;
		
		setMiddlePosition();
		
		CObject sliderObj = ((Optional<CObject>)controlList.stream().filter(p->p.editoraction.equals("control_button_speed1")).findFirst()).get();			
		CObject sliderButton = ((Optional<CObject>)controlList.stream().filter(p->p.editoraction.equals("control_button_speed2")).findFirst()).get();
		
		slider_mult_postprocessing=21.1f;
		slider_mult_framebuffer=4.93f; //breite des sliders erhöhen
		
		int slx=130;
		
		//slider_postprocessor = new CGuiControl_Slider(dlgX+13+slx+gx, dlgY+dialogH-110-postprocessing_posy+80-10-3, 0, 7*slider_mult_postprocessing, town.gameConfigIni.postprocessorvalue*slider_mult_postprocessing, sliderObj, sliderButton);
		//slider_framebuffer = new CGuiControl_Slider(dlgX+13+slx+gx, dlgY+dialogH-110-framebuffer_posy+80-10, 0, 30*slider_mult_framebuffer, town.gameConfigIni.framebuffervalue*slider_mult_framebuffer, sliderObj, sliderButton);
		
		slider_mult_mm=37;
		slider_mmGround = new CGuiControl_Slider(dlgX+13+slx+gx, dlgY+dialogH-110-mmGround_posy+mmy+15, 0, 4*slider_mult_mm, town.gameConfigIni.mipmappingGround*slider_mult_mm, sliderObj, sliderButton, town1);
		slider_mmRoad = new CGuiControl_Slider(dlgX+13+slx+gx, dlgY+dialogH-110-mmRoad_posy+mmy+15, 0, 4*slider_mult_mm, town.gameConfigIni.mipmappingRoad*slider_mult_mm, sliderObj, sliderButton, town1);
		slider_mmTree = new CGuiControl_Slider(dlgX+13+slx+gx, dlgY+dialogH-110-mmTree_posy+mmy+15, 0, 4*slider_mult_mm, town.gameConfigIni.mipmappingTree*slider_mult_mm, sliderObj, sliderButton, town1);
		slider_mmOther = new CGuiControl_Slider(dlgX+13+slx+gx, dlgY+dialogH-110-mmOther_posy+mmy+15, 0, 4*slider_mult_mm, town.gameConfigIni.mipmappingOther*slider_mult_mm, sliderObj, sliderButton, town1);
		slider_mmFloor = new CGuiControl_Slider(dlgX+13+slx+gx, dlgY+dialogH-110-mmFloor_posy+mmy+15, 0, 4*slider_mult_mm, town.gameConfigIni.mipmappingFloor*slider_mult_mm, sliderObj, sliderButton, town1);
		
		slider_mult_lod=37;
		slider_lod = new CGuiControl_Slider(dlgX+13+slx+gx, dlgY+dialogH-110-lod_posy+mmy+15, 0, 4*slider_mult_lod, town.gameConfigIni.lodValue*slider_mult_lod, sliderObj, sliderButton, town1);
		
		slider_mult_thread=19;
		slider_thread = new CGuiControl_Slider(dlgX+13+slx+gx, dlgY+dialogH-110-thread_posy+mmy+15, 1, 8*slider_mult_thread, town.gameConfigIni.threadValue*slider_mult_thread, sliderObj, sliderButton, town1);
		
		slider_mult_wateranimation=153;
		slider_wateranimation = new CGuiControl_Slider(dlgX+13+slx+gx, dlgY+dialogH-110-wateranimation_posy+mmy+15, 0, 1*slider_mult_wateranimation, town.gameConfigIni.waterAnimation*slider_mult_wateranimation, sliderObj, sliderButton, town1);

		
		buttonOK=new CGuiControl_Button(dlgX+dialogW/2-50, dlgY+15-2, 100, 25, 43, 18, "ok", dlgFont, null, CGuiControl_Button.ButtonType.DEFAULT, town1);
		
		//buttonCancel=new CGuiControl_Button(dlgX+dialogW/2+5, dlgY+15-2, 100, 25, 31, 18, "cancel", dlgFont, null, CGuiControl_Button.ButtonType.DEFAULT, town1);
		//buttonMMTree=new CGuiControl_Button(dlgX+dialogW/2-50-60, dlgY+15-2+58+15, 100, 25, 25, 13, "Tree", dlgFont, null, CGuiControl_Button.ButtonType.CHECKBOX, town1);
		//buttonMMRoad=new CGuiControl_Button(dlgX+dialogW/2-40, dlgY+15-2+58+15, 100, 25, 25, 13, "Road", dlgFont, null, CGuiControl_Button.ButtonType.CHECKBOX, town1);
		//buttonMMGround=new CGuiControl_Button(dlgX+dialogW/2+35, dlgY+15-2+58+15, 100, 25, 25, 13, "Ground", dlgFont, null, CGuiControl_Button.ButtonType.CHECKBOX, town1);
	}
	
	public int getLodValue()
	{
		float fvalue=slider_lod.getValue()/slider_mult_lod;
		town.gameConfigIni.lodValue=Math.round(fvalue);
		town.gameConfigIni.writeIni();
		return Math.round(fvalue);
	}
	
	public int getThreadValue()
	{
		float fvalue=slider_thread.getValue()/slider_mult_thread;
		if(fvalue<1)
			fvalue=1;
		town.gameConfigIni.threadValue=Math.round(fvalue);
		town.gameConfigIni.writeIni();
		return Math.round(fvalue);
	}
	
	public int getWaterAnimationValue()
	{
		float fvalue=slider_wateranimation.getValue()/slider_mult_wateranimation;
		town.gameConfigIni.waterAnimation=Math.round(fvalue);
		town.gameConfigIni.writeIni();
		return Math.round(fvalue);
	}
	
	//public int getPostprocessorValue()
	{
		//float fvalue=slider_postprocessor.getValue()/slider_mult_postprocessing;
		//town.gameConfigIni.postprocessorvalue=Math.round(fvalue);
		//town.gameConfigIni.writeIni();
		//return Math.round(fvalue);
	}
	
//	public int getFramebufferValue()
	{
//		float fvalue=slider_framebuffer.getValue()/slider_mult_framebuffer;
//		town.gameConfigIni.framebuffervalue=Math.round(fvalue);
//		town.gameConfigIni.writeIni();
//		return Math.round(fvalue);
	}
	
	public int getMMGroundValue()
	{
		float fvalue=slider_mmGround.getValue()/slider_mult_mm;
		town.gameConfigIni.mipmappingGround=Math.round(fvalue);
		town.gameConfigIni.writeIni();
		return Math.round(fvalue);
	}

	public int getMMRoadValue()
	{
		float fvalue=slider_mmRoad.getValue()/slider_mult_mm;
		town.gameConfigIni.mipmappingRoad=Math.round(fvalue);
		town.gameConfigIni.writeIni();
		return Math.round(fvalue);
	}

	public int getMMTreeValue()
	{
		float fvalue=slider_mmTree.getValue()/slider_mult_mm;
		town.gameConfigIni.mipmappingTree=Math.round(fvalue);
		town.gameConfigIni.writeIni();
		return Math.round(fvalue);
	}
	
	public int getMMOtherValue()
	{
		float fvalue=slider_mmOther.getValue()/slider_mult_mm;
		town.gameConfigIni.mipmappingOther=Math.round(fvalue);
		town.gameConfigIni.writeIni();
		return Math.round(fvalue);
	}
	
	public int getMMFloorValue()
	{
		float fvalue=slider_mmFloor.getValue()/slider_mult_mm;
		town.gameConfigIni.mipmappingFloor=Math.round(fvalue);
		town.gameConfigIni.writeIni();
		return Math.round(fvalue);
	}
	
	public void showDlg(Boolean bShow)
	{
		super.showDlg(bShow);
	}
	
	public void setValues(int vpostprocessing, int vframebuffer, int mmroad, int mmground, int mmtree, int mmother, int mmfloor, int lod, int thread, int wateranimation)
	{
		//value_postprocessor=vpostprocessing;
		//slider_postprocessor.setValue(value_postprocessor*slider_mult_postprocessing);
		
		//value_framebuffer=vframebuffer;
		//slider_framebuffer.setValue(value_framebuffer*slider_mult_framebuffer);
		
		value_mmground=mmground;
		slider_mmGround.setValue(value_mmground*slider_mult_mm);
		
		value_mmroad=mmroad;
		slider_mmRoad.setValue(value_mmroad*slider_mult_mm);

		value_mmtree=mmtree;
		slider_mmTree.setValue(value_mmtree*slider_mult_mm);

		value_mmother=mmother;
		slider_mmOther.setValue(value_mmother*slider_mult_mm);
		
		value_mmfloor=mmfloor;
		slider_mmFloor.setValue(value_mmfloor*slider_mult_mm);
		
		value_lod=lod;
		slider_lod.setValue(value_lod*slider_mult_lod);
		
		value_thread=thread;
		slider_thread.setValue(value_thread*slider_mult_thread);
		
		value_wateranimation=wateranimation;
		slider_wateranimation.setValue(value_wateranimation*slider_mult_wateranimation);
	}
	
	public Boolean buttonDown(int x, int y, int libgdxy, int button)
	{
		if(button==0 || button==-99)
		{
			if(slider_lod.buttonDown(x, y, libgdxy, button))
				return true;
			
			if(slider_thread.buttonDown(x, y, libgdxy, button))
				return true;
			
			if(slider_wateranimation.buttonDown(x, y, libgdxy, button))
				return true;
						
			//if(slider_postprocessor.buttonDown(x, y, libgdxy, button))
			//	return true;
			
			//if(slider_framebuffer.buttonDown(x, y, libgdxy, button))
			//	return true;
			
			if(slider_mmRoad.buttonDown(x, y, libgdxy, button))
				return true;
			
			if(slider_mmGround.buttonDown(x, y, libgdxy, button))
				return true;
			
			if(slider_mmOther.buttonDown(x, y, libgdxy, button))
				return true;
			
			if(slider_mmTree.buttonDown(x, y, libgdxy, button))
				return true;

			if(slider_mmFloor.buttonDown(x, y, libgdxy, button))
				return true;
			
//			if(buttonMMGround.buttonClick(x, libgdxy))
//			{
//				if(buttonMMGround.toggleActive)
//					town.gameConfigIni.mipmappingGround=1;
//				else
//					town.gameConfigIni.mipmappingGround=0;
//				
//				town.gameConfigIni.writeIni();
//			}
//			
//			if(buttonMMRoad.buttonClick(x, libgdxy))
//			{
//				if(buttonMMRoad.toggleActive)
//					town.gameConfigIni.mipmappingRoad=1;
//				else
//					town.gameConfigIni.mipmappingRoad=0;
//				
//				town.gameConfigIni.writeIni();
//			}
//			
//			if(buttonMMTree.buttonClick(x, libgdxy))
//			{
//				if(buttonMMTree.toggleActive)
//					town.gameConfigIni.mipmappingTree=1;
//				else
//					town.gameConfigIni.mipmappingTree=0;
//				
//				town.gameConfigIni.writeIni();
//			}
			
			if(buttonOK.buttonClick(x, libgdxy) || button==-99)
			{
				showDlg(false);
				return true;
			}
		}
		else
		{
			showDlg(false);
		}
		
		return true;
	}
	
	public void buttonUp()
	{
		slider_lod.buttonUp();
		slider_thread.buttonUp();
		slider_wateranimation.buttonUp();
		
		//slider_postprocessor.buttonUp();
		//slider_framebuffer.buttonUp();
		
		slider_mmGround.buttonUp();
		slider_mmOther.buttonUp();
		slider_mmRoad.buttonUp();
		slider_mmTree.buttonUp();
		slider_mmFloor.buttonUp();
		
		//int pvalue=getPostprocessorValue();
		//int fvalue=getFramebufferValue();
		
		getMMGroundValue();
		getMMOtherValue();
		getMMRoadValue();
		getMMTreeValue();
		getMMFloorValue();
		getWaterAnimationValue();
		town.lodvalue=getLodValue();
		town.nrthreads=getThreadValue();
		if(town.nrthreads<1)
			town.nrthreads=1;
		town.gameWorld.asyncExecutor = new AsyncExecutor(town.nrthreads);
		
		//if(pvalue==0)
		//	town.bUsePostProcessor=false;
		
		//if(fvalue==0)
		//	town.bUseFramebuffer=false;
		
		//if(pvalue>0 && bMovePValue)
		{
			//slider_framebuffer.setValue(0);
			//town.frameBuffer.zvalue=0;
			//town.bUseFramebuffer=false;
			//town.initPostprocessor(pvalue);
			//town.bUsePostProcessor=true;
		}
		
		//if(fvalue>0 && bMoveFValue)
		{
			//slider_postprocessor.setValue(0);
			//town.bUsePostProcessor=false;
			
			//town.bUseFramebuffer=true;
			//town.frameBuffer.zvalue=fvalue/10f;
			//town.frameBuffer.bInit=true;
		}
	}
	
	public Boolean mouseMovedDrag(int x, int y, int libgdxy)
	{
		if(slider_lod.mouseMovedDrag(x, y, libgdxy))
			return true;
		if(slider_thread.mouseMovedDrag(x, y, libgdxy))
			return true;
		if(slider_wateranimation.mouseMovedDrag(x, y, libgdxy))
			return true;
		if(slider_mmGround.mouseMovedDrag(x, y, libgdxy))
			return true;
		if(slider_mmOther.mouseMovedDrag(x, y, libgdxy))
			return true;
		if(slider_mmRoad.mouseMovedDrag(x, y, libgdxy))
			return true;
		if(slider_mmTree.mouseMovedDrag(x, y, libgdxy))
			return true;
		if(slider_mmFloor.mouseMovedDrag(x, y, libgdxy))
			return true;
		
//		if(slider_postprocessor.mouseMovedDrag(x, y, libgdxy))
//		{
//			bMovePValue=true;
//			bMoveFValue=false;
//			return true;
//		}
		
//		if(slider_framebuffer.mouseMovedDrag(x, y, libgdxy))
//		{
//			bMovePValue=false;
//			bMoveFValue=true;
//			
//			return true;
//		}
		
		return false;
	}
	
	public void render(int x, int libgdxy)
	{
		int sliderAndText_PosY=21;
		
		//if(!Gdx.gl.glIsEnabled(GL30.GL_BLEND))
		{
			Gdx.gl.glEnable(GL30.GL_BLEND);
			Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
		}
		dlgShapeRenderer.setAutoShapeType(true);
	    dlgShapeRenderer.begin();
			
			//Dialog Background
		    dlgShapeRenderer.set(ShapeType.Filled);
		    dlgShapeRenderer.setColor(town.dialogColor);
		    dlgShapeRenderer.rect(dlgX, dlgY, dialogW, dialogH);
			
			//Dialog Rahmen
		    dlgShapeRenderer.set(ShapeType.Line);
			dlgShapeRenderer.setColor(town.dialogRahmenColor);
			dlgShapeRenderer.rect(dlgX, dlgY, dialogW, dialogH);
			
			//Trennlinie zu Save as default
			//int ty=dlgY+50+40-37;
			//dlgShapeRenderer.setColor(town.dialogRahmenColor);
			//dlgShapeRenderer.line(dlgX+10, ty+postprocessing_posy+105+18+2, dlgX+dialogW-10, ty+postprocessing_posy+105+18+2);
			//dlgShapeRenderer.line(dlgX+10, ty+postprocessing_posy-4+18+2, dlgX+dialogW-10, ty+postprocessing_posy-4+18+2);
			
			//Trennlinie zu ok, cancel
			dlgShapeRenderer.setColor(town.dialogRahmenColor);
			int ty2=dlgY+50;
			dlgShapeRenderer.line(dlgX+10, ty2, dlgX+dialogW-10, ty2);				
			
		dlgShapeRenderer.end();
		//Gdx.gl.glDisable(GL30.GL_BLEND);
		
		dlgSpriteBatch.begin();
			dlgSpriteBatch.setColor(1,1,1,1);
			dlgFont2.setColor(1,1,1,1);
			
			//dlgFont2.getData().setScale(0.54f);
			//dlgFont2.setColor(0.9f,0.9f,0.9f,0.9f);
			//dlgFont2.draw(dlgSpriteBatch, "Postprocessor", dlgX+11-50+gx, dlgY+dialogH-50+sliderAndText_PosY-postprocessing_posy+5-3);
			
			//dlgFont2.getData().setScale(0.63f);
			//dlgFont2.setColor(1,1,1,1);
			//dlgFont2.draw(dlgSpriteBatch, getPostprocessorValue()+"", dlgX+340+gx, slider_postprocessor.controlY+15, 20, 0, false);
			
			//dlgFont2.getData().setScale(0.54f);
			//dlgFont2.setColor(0.9f,0.9f,0.9f,0.9f);
			//dlgFont2.draw(dlgSpriteBatch, "Framebuffer", dlgX+11-50+gx, dlgY+dialogH-50+sliderAndText_PosY-framebuffer_posy-framebuffer_posy+55);
			
			//dlgFont2.getData().setScale(0.63f);
			//dlgFont2.setColor(1,1,1,1);
			//dlgFont2.draw(dlgSpriteBatch, getFramebufferValue()+"", dlgX+340+gx, slider_framebuffer.controlY+15, 20, 0, false);
			
			
			//Mipmapping
			dlgFont2.getData().setScale(0.54f);
			dlgFont2.setColor(0.9f,0.9f,0.9f,0.9f);
			dlgFont2.draw(dlgSpriteBatch, "Mipmapping Ground", dlgX+11-50+gx, dlgY+dialogH-100+sliderAndText_PosY-mmGround_posy+mmy);
			dlgFont2.draw(dlgSpriteBatch, "Mipmapping Road", dlgX+11-50+gx, dlgY+dialogH-100+sliderAndText_PosY-mmRoad_posy+mmy);
			dlgFont2.draw(dlgSpriteBatch, "Mipmapping Tree", dlgX+11-50+gx, dlgY+dialogH-100+sliderAndText_PosY-mmTree_posy+mmy);
			dlgFont2.draw(dlgSpriteBatch, "Mipmapping Floor", dlgX+11-50+gx, dlgY+dialogH-100+sliderAndText_PosY-mmFloor_posy+mmy);
			dlgFont2.draw(dlgSpriteBatch, "Mipmapping Other", dlgX+11-50+gx, dlgY+dialogH-100+sliderAndText_PosY-mmOther_posy+mmy);
			
			dlgFont2.draw(dlgSpriteBatch, "Zoom Out Detail", dlgX+11-50+gx, dlgY+dialogH-100+sliderAndText_PosY-lod_posy+mmy); //lods
			dlgFont2.draw(dlgSpriteBatch, "Logic Threads", dlgX+11-50+gx, dlgY+dialogH-100+sliderAndText_PosY-thread_posy+mmy);
			dlgFont2.draw(dlgSpriteBatch, "Water Animation", dlgX+11-50+gx, dlgY+dialogH-100+sliderAndText_PosY-wateranimation_posy+mmy);
			
			dlgFont2.getData().setScale(0.63f);
			dlgFont2.setColor(1,1,1,1);
			dlgFont2.draw(dlgSpriteBatch, getMMGroundValue()+"", dlgX+340+gx, slider_mmGround.controlY+15, 20, 0, false);
			dlgFont2.draw(dlgSpriteBatch, getMMOtherValue()+"", dlgX+340+gx, slider_mmOther.controlY+15, 20, 0, false);
			dlgFont2.draw(dlgSpriteBatch, getMMRoadValue()+"", dlgX+340+gx, slider_mmRoad.controlY+15, 20, 0, false);
			dlgFont2.draw(dlgSpriteBatch, getMMTreeValue()+"", dlgX+340+gx, slider_mmTree.controlY+15, 20, 0, false);
			dlgFont2.draw(dlgSpriteBatch, getMMFloorValue()+"", dlgX+340+gx, slider_mmFloor.controlY+15, 20, 0, false);
			dlgFont2.draw(dlgSpriteBatch, getLodValue()+"", dlgX+340+gx, slider_lod.controlY+15, 20, 0, false);
			dlgFont2.draw(dlgSpriteBatch, getThreadValue()+"", dlgX+340+gx, slider_thread.controlY+15, 20, 0, false);
			dlgFont2.draw(dlgSpriteBatch, getWaterAnimationValue()+"", dlgX+340+gx, slider_wateranimation.controlY+15, 20, 0, false);
		
		dlgSpriteBatch.end();
		
		
		//---------------
		//Render Controls
		//---------------
		//slider_postprocessor.render();
		//slider_framebuffer.render();
		
		slider_mmGround.render();
		slider_mmTree.render();
		slider_mmRoad.render();
		slider_mmOther.render();
		slider_mmFloor.render();
		
		slider_lod.render();
		slider_thread.render();
		slider_wateranimation.render();
		
		buttonOK.render(x, libgdxy);

		//buttonMMGround.render(x, libgdxy);
		//buttonMMRoad.render(x, libgdxy);
		//buttonMMTree.render(x, libgdxy);
		//buttonCancel.render(x, libgdxy);
	}
}
