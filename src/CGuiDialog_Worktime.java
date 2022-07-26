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

public class CGuiDialog_Worktime extends CGuiDialog{
	
	SpriteBatch dlgSpriteBatch;
	ShapeRenderer dlgShapeRenderer;
	BitmapFont dlgFont;
	
	CTown town;
	
	private CGuiControl_Slider slider_from;
	private CGuiControl_Slider slider_to;
	private CGuiControl_Slider slider_from2;
	private CGuiControl_Slider slider_to2;
	
	private CGuiControl_Button buttonOK;
	private CGuiControl_Button buttonCancel;
	
	private CGuiControl_Button buttonSaveDefault;
	private CGuiControl_Button buttonDistribute;
	
	private CGuiControl_Button buttonWorktime2;
	private CGuiControl_Button button24h;
	
	Boolean bSaveDefaultWorktime;
	
	int hour_from;
	int hour_to;
	int hour_from2;
	int hour_to2;
	
	int actionhour;
	int actionhournr;
	
	int slider_mult;
	//int timeposy2=160;
	int timeposy2=160;
	
	int posyhour2=0;
	
	int worktime_from_fulltime;
	int worktime_to_fulltime;
	
	
	public CGuiDialog_Worktime(List<CObject> controlList, BitmapFont font, int dialogX, int dialogY, CTown town1)
	{
		super("CGuiDialog_Worktime");
		
		town=town1;
		dlgSpriteBatch = town1.gameGui.editorSpriteBatch;
		dlgShapeRenderer = town1.gameGui.shapeRenderer;

		dlgFont=font;
		
		dlgX=dialogX;
		dlgY=dialogY;
		dialogW=250;
		dialogH=200;
		
		worktime_from_fulltime=-1;
		worktime_to_fulltime=-1;
		
		actionhour=-1;
		actionhournr=-1;
		hour_from=-1;
		hour_to=-1;
		hour_from2=-1;
		hour_to2=-1;
		
		setMiddlePosition();
		
		CObject sliderObj = ((Optional<CObject>)controlList.stream().filter(p->p.editoraction.equals("control_button_speed1")).findFirst()).get();			
		CObject sliderButton = ((Optional<CObject>)controlList.stream().filter(p->p.editoraction.equals("control_button_speed2")).findFirst()).get();
		slider_mult=4; //breite des sliders erhöhen
		slider_from = new CGuiControl_Slider(dlgX+13, dlgY+dialogH-110, 0, 23*slider_mult, 9*slider_mult, sliderObj, sliderButton, town1);
		slider_to = new CGuiControl_Slider(dlgX+130, dlgY+dialogH-110, 0, 23*slider_mult, 17*slider_mult, sliderObj, sliderButton, town1);
		
		slider_from2 = new CGuiControl_Slider(dlgX+13, dlgY+dialogH-110-timeposy2, 0, 23*slider_mult, 9*slider_mult, sliderObj, sliderButton, town1);
		slider_to2 = new CGuiControl_Slider(dlgX+130, dlgY+dialogH-110-timeposy2, 0, 23*slider_mult, 17*slider_mult, sliderObj, sliderButton, town1);
		
		buttonOK=new CGuiControl_Button(dlgX+dialogW/2-105, dlgY+15-2, 100, 25, 43, 18, "ok", dlgFont, null, CGuiControl_Button.ButtonType.DEFAULT, town1);
		buttonCancel=new CGuiControl_Button(dlgX+dialogW/2+5, dlgY+15-2, 100, 25, 31, 18, "cancel", dlgFont, null, CGuiControl_Button.ButtonType.DEFAULT, town1);
		
		buttonSaveDefault = new CGuiControl_Button(0, 0, 25, 25, -110, 13, "Save as default", dlgFont, null, CGuiControl_Button.ButtonType.CHECKBOX, town1);
		buttonSaveDefault.setPosition(dlgX+dialogW/2-105+85+67, dlgY+15-2+50);
		buttonSaveDefault.enableTooltip(true);
		buttonSaveDefault.tooltip.textLines.add("Save the current schedule as default for new workplaces");
		buttonSaveDefault.setPosition(dlgX+dialogW/2-105+85+67-1, dlgY+15-2+50-40);
		
		buttonDistribute = new CGuiControl_Button(0, 0, 25, 25, -109, 13, "Distribute", dlgFont, null, CGuiControl_Button.ButtonType.CHECKBOX, town1);
		buttonDistribute.enableTooltip(true);
		buttonDistribute.tooltip.textLines.add("Set the current schedule on all address-workplaces of the same type");
		buttonDistribute.setPosition(dlgX+dialogW/2-105+85+67-1, dlgY+15-2+50-40);
		
		buttonWorktime2 = new CGuiControl_Button(0, 0, 25, 25, -134, 13, "Second Time Entry", dlgFont, null, CGuiControl_Button.ButtonType.CHECKBOX, town1);
		buttonWorktime2.setPosition(dlgX+dialogW/2-105+85+67+10, dlgY+15-2+50+timeposy2);
		
		button24h = new CGuiControl_Button(dlgX+dialogW/2+11+20-20, 0, 25, 25, -34, 13, "24h", dlgFont, null, CGuiControl_Button.ButtonType.CHECKBOX, town1);
	}
	
	public void setActionHour(int hour, int nr)
	{
		actionhour=hour;
		actionhournr=nr;
		
		slider_from.setValue(actionhour*slider_mult);
		
		hour_from=-1;
		hour_to=-1;
	}
	
	public void showDlg(Boolean bShow)
	{
		super.showDlg(bShow);
		
		buttonSaveDefault.toggleActive=false;
		buttonDistribute.toggleActive=false;
		
		if(town.gameWorld.markerObject.workTime1_From_temp>-1 || town.gameWorld.markerObject.workTime1_To_temp>-1)
		{
			button24h.toggleActive=true;
		}
		else
			button24h.toggleActive=false;
		
		buttonWorktime2.toggleActive=false;
		
		if(hour_from2>-1)
			buttonWorktime2.toggleActive=true;
		
		dialogW=250;
		
		if(hour_to>-1)
		{
			//posyhour2=30;
			posyhour2=30;
			//dialogH=200+timeposy2+30+12;
			dialogH=200+timeposy2;//+30+12;
			setMiddlePosition();
			buttonOK.setPosition(buttonOK.controlX, dlgY+15-2);
			buttonCancel.setPosition(buttonCancel.controlX, dlgY+15-2);
			buttonSaveDefault.setPosition(buttonSaveDefault.controlX, dlgY+15-2+50);
			buttonDistribute.setPosition(buttonDistribute.controlX, dlgY+15-2+50+30);
			
			buttonWorktime2.setPosition(buttonWorktime2.controlX, dlgY+15-2+50+timeposy2-20+posyhour2-14);
			button24h.setPosition(button24h.controlX, dlgY+15-2+50+30+211-55);
			
			if(bShow) //speichere temp zwischens
			{
				worktime_from_fulltime=town.gameWorld.markerObject.workTime1_From_temp;
				worktime_to_fulltime=town.gameWorld.markerObject.workTime1_To_temp;
			}
		}
		else if(actionhour>-1)
		{
			dialogH=200;
			setMiddlePosition();
			buttonOK.setPosition(buttonOK.controlX, dlgY+15-2);
			buttonCancel.setPosition(buttonCancel.controlX, dlgY+15-2);
			buttonSaveDefault.setPosition(buttonSaveDefault.controlX, dlgY+15-2+50);
			buttonWorktime2.setPosition(buttonWorktime2.controlX, dlgY+15-2+50);
		}
	}
	
	public void setHours(int from, int to, int from2, int to2)
	{
		actionhour=-1;

		hour_from=from;
		hour_to=to;
		slider_from.setValue(from*slider_mult);
		slider_to.setValue(to*slider_mult);
		
		hour_from2=from2;
		hour_to2=to2;
		slider_from2.setValue(from2*slider_mult);
		slider_to2.setValue(to2*slider_mult);
		
	}
	
	public int getFrom()
	{
		return Math.round(slider_from.getValue()/slider_mult);
	}
	
	public int getTo()
	{
		return Math.round(slider_to.getValue()/slider_mult);
	}
	
	public int getFrom2()
	{
		if(buttonWorktime2.toggleActive)
			return Math.round(slider_from2.getValue()/slider_mult);
		else
			return -1;
	}
	
	public int getTo2()
	{
		if(buttonWorktime2.toggleActive)
			return Math.round(slider_to2.getValue()/slider_mult);
		else
			return -1;
	}
		
	public int getHours()
	{
		int hours = 0;
		int from = getFrom();// Math.round(slider_from.getValue()/slider_mult);
		int to = getTo(); //Math.round(slider_to.getValue()/slider_mult);
		
		if(from==-1 || to==-1)
			return 0;
		
		if(to>from)
			hours=Math.round(to-from);
		
		if(to<=from)
		{
			hours=24-from + to;
		}
		
		return hours;
	}	
	
	public int getHours2()
	{
		int hours = 0;
		int from = getFrom2();// Math.round(slider_from2.getValue()/slider_mult);
		int to = getTo2();//Math.round(slider_to2.getValue()/slider_mult);
		
		if(from==-1 || to==-1)
			return 0;
		
		if(to>from)
			hours=Math.round(to-from);
		
		if(to<=from)
		{
			hours=24-from + to;
		}
		
		return hours;
	}	
	
	public void resetFulltime(int mode)
	{
		CWorldObject mobj = town.gameWorld.markerObject;
		
		if(mode==0 && mobj.workTime1_From_temp>-1)
		{
			setHours(mobj.workTime1_From_temp, mobj.workTime1_To_temp, getFrom2(), getTo2());
		}
		
		if(mode==1)
			button24h.toggleActive=false;
		
		mobj.workTime1_From=town.gameWorld.markerObject.workTime1_From_temp;
		mobj.workTime1_To=town.gameWorld.markerObject.workTime1_To_temp;
		
		//if(mode==0)
		{
			mobj.workTime1_From_temp=-1; //Gesetzte Zeit zwischenspeichern, wenn fulltime auf die richtigen vars geschrieben wird
			mobj.workTime1_To_temp=-1;
		}
	}
	
	public Boolean buttonDown(int x, int y, int libgdxy, int button)
	{
		CWorldObject marker = town.gameWorld.markerObject;
		
		if(button==1)
		{
			//fulltime einstellungen bei abbruch zurücksetzen
			marker.workTime1_From_temp=worktime_from_fulltime;
			marker.workTime1_To_temp=worktime_to_fulltime;
		}
		
		if(button==0 || button==-99)
		{
			if(buttonSaveDefault.buttonClick(x, libgdxy))
				return true;
			
			if(buttonDistribute.buttonClick(x, libgdxy))
				return true;
			
			if(button24h.buttonClick(x, libgdxy))
			{
				if(button24h.toggleActive)
				{
					town.gameWorld.markerObject.workTime1_From_temp=town.gameWorld.markerObject.workTime1_From;
					town.gameWorld.markerObject.workTime1_To_temp=town.gameWorld.markerObject.workTime1_To;
				
					setHours(0, 23, getFrom2(), getTo2());
				}
				else
				{
					resetFulltime(0);
				}
				
				return true;
			}
			
			if(buttonWorktime2.buttonClick(x, libgdxy))
			{
				//- h raus: setze -1
				//- h rein: setze default
				
				if(buttonWorktime2.toggleActive)
				{
					town.gameConfigIni.readIni();
					hour_from2=town.gameConfigIni.default_worktime_from2;
					hour_to2=town.gameConfigIni.default_worktime_to2;
				}
				else
				{
					hour_from2=-1;
					hour_to2=-1;
				}
				
				setHours(getFrom(), getTo(), hour_from2, hour_to2);
				
				return true;
			}
			
			if(slider_from.buttonDown(x, y, libgdxy, button))
			{
				resetFulltime(1);
				return true;
			}
			
			if(slider_to.buttonDown(x, y, libgdxy, button))
			{
				resetFulltime(1);
				return true;
			}
			
			if(slider_from2.buttonDown(x, y, libgdxy, button))
				return true;
			
			if(slider_to2.buttonDown(x, y, libgdxy, button))
				return true;
			
			if(buttonOK.buttonClick(x, libgdxy) || button==-99)
			{
				if(actionhour>-1)
				{
					if(actionhournr==1)
						town.gameWorld.markerObject.actiontime1=getFrom();
					if(actionhournr==2)
						town.gameWorld.markerObject.actiontime2=getFrom();
					if(actionhournr==3)
						town.gameWorld.markerObject.actiontime3=getFrom();
				}
				
				if(hour_from>-1)
				{
					town.gameWorld.markerObject.workTime1_From=getFrom();
					town.gameWorld.markerObject.workTime1_To=getTo();
					
					town.gameWorld.markerObject.workTime2_From=getFrom2();
					town.gameWorld.markerObject.workTime2_To=getTo2();
					
					if(buttonSaveDefault.toggleActive)
					{
						town.gameConfigIni.default_worktime_from = town.gameWorld.markerObject.workTime1_From;
						town.gameConfigIni.default_worktime_to = town.gameWorld.markerObject.workTime1_To;
						
						town.gameConfigIni.default_worktime_from2 = town.gameWorld.markerObject.workTime2_From;
						town.gameConfigIni.default_worktime_to2 = town.gameWorld.markerObject.workTime2_To;
						
						town.gameConfigIni.writeIni();
					}
					
					if(buttonDistribute.toggleActive)
					{
						CWorldObject mobj = town.gameWorld.markerObject;
						for(CWorldObject wobj : mobj.theaddress.listWorldObjects)
						{
							if(wobj.theobject.editoraction.equals(mobj.theobject.editoraction))
							{
								wobj.workTime1_From=mobj.workTime1_From;
								wobj.workTime1_To=mobj.workTime1_To;
								wobj.workTime2_From=mobj.workTime2_From;
								wobj.workTime2_To=mobj.workTime2_To;
							}
						}
					}
				}
				
				showDlg(false);
				
				return true;
			}
			
			if(buttonCancel.buttonClick(x, libgdxy))
			{
				//fulltime einstellungen bei abbruch zurücksetzen
				town.gameWorld.markerObject.workTime1_From_temp=worktime_from_fulltime;
				town.gameWorld.markerObject.workTime1_To_temp=worktime_to_fulltime;
				
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
		slider_from.buttonUp();
		slider_to.buttonUp();
		
		slider_from2.buttonUp();
		slider_to2.buttonUp();
	}
	
	
	public Boolean mouseMovedDrag(int x, int y, int libgdxy)
	{
		if(slider_from.mouseMovedDrag(x, y, libgdxy))
			return true;
		
		if(slider_to.mouseMovedDrag(x, y, libgdxy))
			return true;
		
		if(slider_from2.mouseMovedDrag(x, y, libgdxy))
			return true;
		
		if(slider_to2.mouseMovedDrag(x, y, libgdxy))
			return true;
		
		return false;
	}
		
	public void render(int x, int libgdxy)
	{
		int sliderAndText_PosY=20;

		if(!Gdx.gl.glIsEnabled(GL30.GL_BLEND))
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
			if(hour_to > -1)
			{
				int ty=dlgY+50+40;
				dlgShapeRenderer.setColor(town.dialogRahmenColor);
				dlgShapeRenderer.line(dlgX+10, ty+33, dlgX+dialogW-10, ty+33);
				//dlgShapeRenderer.line(dlgX+10, ty+timeposy2+30-8, dlgX+dialogW-10, ty+timeposy2+30-8);
			}
			
			//Trennlinie zu ok, cancel
			dlgShapeRenderer.setColor(town.dialogRahmenColor);
			int ty2=dlgY+50;
			dlgShapeRenderer.line(dlgX+10, ty2, dlgX+dialogW-10, ty2);				
			
		dlgShapeRenderer.end();
		//Gdx.gl.glDisable(GL30.GL_BLEND);		
		
		dlgSpriteBatch.begin();
			dlgSpriteBatch.setColor(1,1,1,1);
			dlgFont.setColor(town.dialogFontColorList);
			
			if(hour_to>-1)
			{
				int hours = getHours();
				int from = getFrom();
				int to = getTo();
				
				String shours=""+hours;
				if(shours.length()==1)
					shours="0"+shours;
				
				String sfrom = CHelper.getAMPMHourText(from);
				String sto =CHelper.getAMPMHourText(to);
				if(sfrom.trim().equals("-"))
					sfrom="00 AM";
				if(sto.trim().equals("-"))
					sto="00 PM";
				if(shours.equals("00"))
					shours="00";
				
				String timetext = sfrom + " to " + sto + " (" + shours + " hours" + ")";
				dlgFont.draw(dlgSpriteBatch, timetext, dlgX+30+11, dlgY+dialogH-50+sliderAndText_PosY);
				slider_from.setPosition(dlgX+13-2, dlgY+dialogH-110+sliderAndText_PosY+10);
				slider_to.setPosition(dlgX+130-2, dlgY+dialogH-110+sliderAndText_PosY+10);
				
				//------------------------------------------------------------------------
				
				hours = getHours2();
				from = getFrom2();
				to = getTo2();
				
				shours=""+hours;
				if(shours.length()==1)
					shours="0"+shours;
				
				sfrom = CHelper.getAMPMHourText(from);
				sto =CHelper.getAMPMHourText(to);
				if(sfrom.trim().equals("-"))
					sfrom=" -- AM";
				if(sto.trim().equals("-"))
					sto=" -- PM";
				if(shours.equals("00"))
					shours=" -- ";
				
				timetext = sfrom + " to " + sto + " (" + shours + " hours" + ")";
				if(!buttonWorktime2.toggleActive)
				{
					dlgFont.setColor(0.7f,0.7f,0.7f,0.7f);
					slider_from2.setEnabled(false);
					slider_to2.setEnabled(false);
				}
				else
				{
					dlgFont.setColor(town.dialogFontColorList);
					slider_from2.setEnabled(true);
					slider_to2.setEnabled(true);
				}
				
				int pyno24=40;
				
				dlgFont.draw(dlgSpriteBatch, timetext, dlgX+30+11, dlgY+dialogH-50+sliderAndText_PosY-timeposy2-14+pyno24);
				
				slider_from2.setPosition(dlgX+13-2, dlgY+dialogH-110+sliderAndText_PosY-timeposy2+10-14+pyno24);
				slider_to2.setPosition(dlgX+130-2, dlgY+dialogH-110+sliderAndText_PosY-timeposy2+10-14+pyno24);
			}
			
			if(actionhour>-1)
			{
				int from = getFrom();
				dlgFont.draw(dlgSpriteBatch, CHelper.getAMPMHourText(from), dlgX+105, dlgY+dialogH-50);
				slider_from.setPosition(Math.round(dlgX+dialogW/2-slider_from.getWidth()/2), dlgY+dialogH-110);
			}
			
		dlgSpriteBatch.end();
		
		
		
		//---------------
		//Render Controls
		//---------------
		
		slider_from.render();
		
		if(hour_to>-1)
		{
			slider_to.render();
			slider_to2.render();
			slider_from2.render();
			buttonSaveDefault.render(x, libgdxy);
			buttonDistribute.render(x, libgdxy);
			buttonWorktime2.render(x, libgdxy);
			button24h.render(x, libgdxy);
		}
		
		buttonOK.render(x, libgdxy);
		buttonCancel.render(x, libgdxy);
	}
}
