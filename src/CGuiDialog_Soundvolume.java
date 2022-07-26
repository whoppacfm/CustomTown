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
import com.sun.corba.se.spi.legacy.connection.GetEndPointInfoAgainException;

public class CGuiDialog_Soundvolume extends CGuiDialog{
	
	SpriteBatch dlgSpriteBatch;
	ShapeRenderer dlgShapeRenderer;
	BitmapFont dlgFont;
	BitmapFont dlgFont2;
	
	CTown town;
	
	private CGuiControl_Slider slider_soundeffects;
	private CGuiControl_Slider slider_soundambient;
	private CGuiControl_Slider slider_soundmusic;
	
	private CGuiControl_Button buttonOK;
	private CGuiControl_Button buttonCancel;
	
	int volume_soundeffects;
	int volume_soundambient;
	int volume_soundmusic;
	
	float slider_mult;
	int ambient_posy=120;
	int music_posy=240;
	
	public CGuiDialog_Soundvolume(List<CObject> controlList, int dialogX, int dialogY, CTown town1)
	{
		super("CGuiDialog_Soundvolume");
		
		town=town1;
		dlgSpriteBatch = town1.gameGui.editorSpriteBatch;
		dlgShapeRenderer = town1.gameGui.shapeRenderer;
		
		dlgFont2=town.gameFont.bfArial2;
		dlgFont=town.gameFont.fontSmall;
		
		dlgX=dialogX;
		dlgY=dialogY;
		dialogW=250;
		dialogH=407;
		
		volume_soundeffects=-1;
		volume_soundambient=-1;
		volume_soundmusic=-1;
		
		setMiddlePosition();
		
		CObject sliderObj = ((Optional<CObject>)controlList.stream().filter(p->p.editoraction.equals("control_button_speed1")).findFirst()).get();			
		CObject sliderButton = ((Optional<CObject>)controlList.stream().filter(p->p.editoraction.equals("control_button_speed2")).findFirst()).get();
		slider_mult=1.48f; //breite des sliders erhöhen
		
		slider_soundeffects = new CGuiControl_Slider(dlgX+13, dlgY+dialogH-110, 0, 100*slider_mult, 0, sliderObj, sliderButton, town1);
		slider_soundambient = new CGuiControl_Slider(dlgX+13, dlgY+dialogH-110-ambient_posy, 0, 100*slider_mult, 0, sliderObj, sliderButton, town1);
		slider_soundmusic = new CGuiControl_Slider(dlgX+13, dlgY+dialogH-110-music_posy, 0, 100*slider_mult, 0, sliderObj, sliderButton, town1);
		
		buttonOK=new CGuiControl_Button(dlgX+dialogW/2-50, dlgY+15-2, 100, 25, 43, 18, "ok", dlgFont, null, CGuiControl_Button.ButtonType.DEFAULT, town1);
		//buttonCancel=new CGuiControl_Button(dlgX+dialogW/2+5, dlgY+15-2, 100, 25, 31, 18, "cancel", dlgFont, null, CGuiControl_Button.ButtonType.DEFAULT, town1);
	}
	
	public int getEffectsVolume()
	{
		float fvalue=slider_soundeffects.getValue()/slider_mult;
		//town.gameAudio.setVolumeEffects(fvalue/100f);
		//town.gameConfigIni.soundeffectsvolume=Math.round(fvalue);
		//town.gameConfigIni.writeIni();
		
		return Math.round(fvalue);
	}
	
	public int getAmbientVolume()
	{
		float fvalue=slider_soundambient.getValue()/slider_mult;
		//town.gameAudio.setVolumeAmbient_NoFadeIn(fvalue/100);
		//town.gameConfigIni.soundambientvolume=Math.round(fvalue);
		//town.gameConfigIni.writeIni();
		
		return Math.round(fvalue);
	}
	
	public int getMusicVolume()
	{
		float fvalue=slider_soundmusic.getValue()/slider_mult;
		//town.gameAudio.setVolumeMusic(fvalue/100);
		//town.gameConfigIni.soundmusicvolume=Math.round(fvalue);
		//town.gameConfigIni.writeIni();
		
		return Math.round(fvalue);
	}
	
	public void showDlg(Boolean bShow)
	{
		super.showDlg(bShow);
		
		slider_soundeffects.setValue(town.gameConfigIni.soundeffectsvolume*slider_mult);
		slider_soundambient.setValue(town.gameConfigIni.soundambientvolume*slider_mult);
		slider_soundmusic.setValue(town.gameConfigIni.soundmusicvolume*slider_mult);
	}
	
	/*
	public void setVolume(int effects, int ambient, int music)
	{
		volume_soundeffects=effects;
		slider_soundeffects.setValue(volume_soundeffects*slider_mult);
		
		volume_soundambient=ambient;
		slider_soundambient.setValue(volume_soundambient*slider_mult);

		volume_soundmusic=music;
		slider_soundmusic.setValue(volume_soundmusic*slider_mult);
	}
	*/
	
	public int getFrom()
	{
		return Math.round(slider_soundeffects.getValue()/slider_mult);
	}
	
	public Boolean buttonDown(int x, int y, int libgdxy, int button)
	{
		if(button==0 || button==-99)
		{
			if(slider_soundeffects.buttonDown(x, y, libgdxy, button))
				return true;
			
			if(slider_soundambient.buttonDown(x, y, libgdxy, button))
				return true;

			if(slider_soundmusic.buttonDown(x, y, libgdxy, button))
				return true;
			
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
		Boolean up1=false;
		Boolean up2=false;
		Boolean up3=false;
		up1=slider_soundeffects.buttonUp();
		up2=slider_soundambient.buttonUp();
		up3=slider_soundmusic.buttonUp();
		
		if(up1||up2||up3)
		{
			setConfigValues();
			town.gameConfigIni.writeIni();
		}
	}
	
	public void setConfigValues()
	{
		float val1 = slider_soundeffects.getValue()/slider_mult;
		town.gameAudio.setVolumeEffects(val1/100f);
		town.gameConfigIni.soundeffectsvolume=Math.round(val1);
		
		float val2 = slider_soundambient.getValue()/slider_mult;
		town.gameAudio.setVolumeAmbient_NoFadeIn(val2/100);
		town.gameConfigIni.soundambientvolume=Math.round(val2);
		
		float val3 = slider_soundmusic.getValue()/slider_mult;
		town.gameAudio.setVolumeMusic(val3/100);
		town.gameConfigIni.soundmusicvolume=Math.round(val3);
	}
	
	public Boolean mouseMovedDrag(int x, int y, int libgdxy)
	{
		if(slider_soundeffects.mouseMovedDrag(x, y, libgdxy))
		{
			setConfigValues();
			return true;
		}
		
		if(slider_soundambient.mouseMovedDrag(x, y, libgdxy))
		{
			setConfigValues();
			return true;
		}

		if(slider_soundmusic.mouseMovedDrag(x, y, libgdxy))
		{
			setConfigValues();
			return true;
		}
		
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
			//dlgShapeRenderer.setColor(0.7f, 0.7f, 0.7f, 0.8f);
			dlgShapeRenderer.setColor(town.dialogRahmenColor);
			dlgShapeRenderer.rect(dlgX, dlgY, dialogW, dialogH);
			
			//Trennlinie zu Save as default
			int ty=dlgY+50+40-37;
			dlgShapeRenderer.setColor(town.dialogRahmenColor);
			dlgShapeRenderer.line(dlgX+10, ty+music_posy, dlgX+dialogW-10, ty+music_posy);
			dlgShapeRenderer.line(dlgX+10, ty+ambient_posy, dlgX+dialogW-10, ty+ambient_posy);
			
			//Trennlinie zu ok, cancel
			//dlgShapeRenderer.setColor(0.7f, 0.7f, 0.7f, 0.8f);
			dlgShapeRenderer.setColor(town.dialogRahmenColor);
			int ty2=dlgY+50;
			dlgShapeRenderer.line(dlgX+10, ty2, dlgX+dialogW-10, ty2);				
			
		dlgShapeRenderer.end();
		//Gdx.gl.glDisable(GL30.GL_BLEND);		
		
		dlgSpriteBatch.begin();
			dlgSpriteBatch.setColor(1,1,1,1);
			
			dlgFont2.setColor(1,1,1,1);
			
			dlgFont2.getData().setScale(0.54f);
			//dlgFont2.setColor(town.dialogFontColorList);
			dlgFont2.setColor(0.9f,0.9f,0.9f,0.9f);
			dlgFont2.draw(dlgSpriteBatch, "Sound Effects", dlgX+11, dlgY+dialogH-50+sliderAndText_PosY);
			
			dlgFont2.getData().setScale(0.63f);
			dlgFont2.setColor(1,1,1,1);
			dlgFont2.draw(dlgSpriteBatch, getEffectsVolume()+"", dlgX+210, dlgY+dialogH-84+sliderAndText_PosY, 20, 0, false);
			slider_soundeffects.setPosition(dlgX+13-2, dlgY+dialogH-110+sliderAndText_PosY+10);
			
			dlgFont2.getData().setScale(0.54f);
			dlgFont2.setColor(0.9f,0.9f,0.9f,0.9f);
			dlgFont2.draw(dlgSpriteBatch, "Ambient Sound", dlgX+11, dlgY+dialogH-50+sliderAndText_PosY-ambient_posy);
			
			dlgFont2.getData().setScale(0.63f);
			dlgFont2.setColor(1,1,1,1);
			dlgFont2.draw(dlgSpriteBatch, getAmbientVolume()+"", dlgX+210, dlgY+dialogH-84+sliderAndText_PosY-ambient_posy, 20, 0, false);
			slider_soundambient.setPosition(dlgX+13-2, dlgY+dialogH-110+sliderAndText_PosY-ambient_posy+10);
			
			
			dlgFont2.getData().setScale(0.54f);
			dlgFont2.setColor(0.9f,0.9f,0.9f,0.9f);
			dlgFont2.draw(dlgSpriteBatch, "Music", dlgX+11, dlgY+dialogH-50+sliderAndText_PosY-music_posy);
			
			dlgFont2.getData().setScale(0.63f);
			dlgFont2.setColor(1,1,1,1);
			dlgFont2.draw(dlgSpriteBatch, getMusicVolume()+"", dlgX+210, dlgY+dialogH-84+sliderAndText_PosY-music_posy, 20, 0, false);
			slider_soundmusic.setPosition(dlgX+13-2, dlgY+dialogH-110+sliderAndText_PosY-music_posy+10);
			
		dlgSpriteBatch.end();
		
		
		//---------------
		//Render Controls
		//---------------
		
		slider_soundeffects.render();
		
		//if(hour_to>-1)
		{
			slider_soundambient.render();
		}
		
		slider_soundmusic.render();
		
		buttonOK.render(x, libgdxy);
		//buttonCancel.render(x, libgdxy);
	}
}
