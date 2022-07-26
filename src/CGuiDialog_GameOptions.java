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

public class CGuiDialog_GameOptions extends CGuiDialog{
	
	SpriteBatch dlgSpriteBatch;
	ShapeRenderer dlgShapeRenderer;
	BitmapFont dlgFont;
	BitmapFont dlgFont2;
	
	CTown town;
	
	private CGuiControl_Button buttonOK;
	private CGuiControl_Button buttonCancel;

	
	private CGuiControl_Button buttonDisplayAddressInput;
	Boolean bDisplayAddressInput;
	

	
	
	public CGuiDialog_GameOptions(List<CObject> controlList, int dialogX, int dialogY, CTown town1)
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
		
		setMiddlePosition();
		
		buttonDisplayAddressInput = new CGuiControl_Button(0, 0, 25, 25, -330, 13, "Show Address Name Dialog", dlgFont, null, CGuiControl_Button.ButtonType.CHECKBOX, town1);
				
		CObject sliderObj = ((Optional<CObject>)controlList.stream().filter(p->p.editoraction.equals("control_button_speed1")).findFirst()).get();			
		CObject sliderButton = ((Optional<CObject>)controlList.stream().filter(p->p.editoraction.equals("control_button_speed2")).findFirst()).get();
		
		int slx=130;
		
		buttonOK=new CGuiControl_Button(dlgX+dialogW/2-50, dlgY+15-2, 100, 25, 43, 18, "ok", dlgFont, null, CGuiControl_Button.ButtonType.DEFAULT, town1);
	}
	
	
	public void setDisplayAddressInputValue()
	{
		town.gameConfigIni.displayaddressinput=buttonDisplayAddressInput.toggleActive;
		town.gameConfigIni.writeIni();
	}
	
	
	public void showDlg(Boolean bShow)
	{
		super.showDlg(bShow);
		
		town.gameConfigIni.readIni();
		buttonDisplayAddressInput.toggleActive=town.gameConfigIni.displayaddressinput;

		buttonDisplayAddressInput.setPosition(dlgX+360, dlgY+dialogH-50);
	}
	
	
	public Boolean buttonDown(int x, int y, int libgdxy, int button)
	{
		if(button==0 || button==-99)
		{
			if(buttonDisplayAddressInput.buttonClick(x, libgdxy))
			{
				setDisplayAddressInputValue();
				return true;
			}
			
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
		

		buttonDisplayAddressInput.setColor(new Color(1,1,1,0.97f));
		buttonDisplayAddressInput.render(x, libgdxy);

		buttonOK.render(x, libgdxy);

	}
}
