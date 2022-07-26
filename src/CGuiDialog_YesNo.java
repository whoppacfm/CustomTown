package com.mygdx.game;

import java.awt.JobAttributes.DialogType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.mygdx.game.CGuiControl_Button.ButtonType;

public class CGuiDialog_YesNo extends CGuiDialog{
	
	public static enum YesNoDlgType	{ 
		DEFAULT, QUIT, SAVETOWN, NEWTOWN, DEMO, UNLOCKCOMPANY, LAUNCHSPACESHIP, NOTRESEARCHED, NOTUNLOCKED
	}
	
	SpriteBatch dlgSpriteBatch;
	ShapeRenderer dlgShapeRenderer;
	BitmapFont dlgFont;
	public YesNoDlgType dlgType;
	public String sDlgText;
	public String sSaveTown_OverwriteFileName;
	public String sSaveTown_OverwriteFileName_clear;
	public String unlockcompanyid;
	public String unlockcompanyname;
		
	public ArrayList<String> listre;
	CTown town;
	
	private CGuiControl_Button buttonOK;
	private CGuiControl_Button buttonCancel;
	
	private CGuiControl_Button buttonBUY;
	private CGuiControl_Button buttonNEWGAME;
	private CGuiControl_Button buttonQUIT;
	
	private CGuiControl_Button buttonUnlock;
	
	public CGuiDialog_YesNo(List<CObject> controlList, BitmapFont font, int dialogX, int dialogY, CTown town1)
	{
		super("CGuiDialog_YesNo");
		
		bCloseOnEscape=false;
		sDlgText="";
		dlgType=YesNoDlgType.DEFAULT;
		town=town1;
		dlgSpriteBatch = town1.gameGui.editorSpriteBatch;
		dlgShapeRenderer = town1.gameGui.shapeRenderer;
		dlgFont=font;
		
		dlgX=dialogX;
		dlgY=dialogY;
		dialogW=300;
		//dialogH=150;
		dialogH=180;
		
		setUpperMiddlePosition();
		
		CObject sliderObj = ((Optional<CObject>)controlList.stream().filter(p->p.editoraction.equals("control_button_speed1")).findFirst()).get();			
		CObject sliderButton = ((Optional<CObject>)controlList.stream().filter(p->p.editoraction.equals("control_button_speed2")).findFirst()).get();
		
		//buttonOK=new CGuiControl_Button(dlgX+10, dlgY+10, 100, 25, 33, 18, "ok", dlgFont, null, ButtonType.DEFAULT, town1);
		//buttonCancel=new CGuiControl_Button(dlgX+dialogW-110, dlgY+10, 100, 25, 32, 18, "cancel", dlgFont, null, ButtonType.DEFAULT, town1);
		
		buttonOK=new CGuiControl_Button(dlgX+dialogW/2-105, dlgY+15-2, 100, 25, 43, 18, "Ok", dlgFont, null, CGuiControl_Button.ButtonType.DEFAULT, town1);
		buttonCancel=new CGuiControl_Button(dlgX+dialogW/2+5, dlgY+15-2, 100, 25, 29, 18, "Cancel", dlgFont, null, CGuiControl_Button.ButtonType.DEFAULT, town1);
		
		buttonBUY=new CGuiControl_Button(dlgX, dlgY+15-2, 100, 25, 40, 18, "Buy", dlgFont, null, CGuiControl_Button.ButtonType.DEFAULT, town1);
		buttonNEWGAME=new CGuiControl_Button(dlgX+100, dlgY+15-2, 100, 25, 15, 18, "New Game", dlgFont, null, CGuiControl_Button.ButtonType.DEFAULT, town1);
		buttonQUIT=new CGuiControl_Button(dlgX+200, dlgY+15-2, 100, 25, 35, 18, "Quit", dlgFont, null, CGuiControl_Button.ButtonType.DEFAULT, town1);
		
		buttonUnlock=new CGuiControl_Button(dlgX+dialogW/2-105, dlgY+15-2, 100, 25, 30, 18, "Unlock", dlgFont, null, CGuiControl_Button.ButtonType.DEFAULT, town1);
	}
	
	public Boolean buttonDown(int x, int y, int libgdxy, int button)
	{
		if(button==0 || button==-99)
		{
			if(dlgType==YesNoDlgType.UNLOCKCOMPANY)
			{
				if(buttonUnlock.buttonClick(x, libgdxy))
				{
					town.gameWorld.changeTownMoney(-town.unlockCompanyPrice);
					town.gameWorld.townStatistics.getCurrentStatistics_Finance().buyObject+=town.unlockCompanyPrice;

					town.gameGui.unlockedCompanyTypes.add(unlockcompanyid);
					town.gameAudio.playSound("EFFECT_ACHIEVEMENT", 0, false);
					showDlg(false);
					return true;
				}
				
				if(buttonCancel.buttonClick(x, libgdxy))
				{
					showDlg(false);
					return true;
				}
			}
			
			if(dlgType==YesNoDlgType.DEMO)
			{
				if(buttonBUY.buttonClick(x, libgdxy))
				{
					Gdx.net.openURI("http://store.steampowered.com/app/517700/");
					Gdx.app.exit();
				}
				
				if(buttonNEWGAME.buttonClick(x, libgdxy))
				{
					showDlg(false);
					//town.bZombieApocalypse=true;
					town.bNoRealEstate=false;
					//town.bConstructionMode=true;
					town.newGame(true, "htp");
				}
				
				if(buttonQUIT.buttonClick(x, libgdxy))
				{
					Gdx.app.exit();
				}
			}
			
			if(buttonOK.buttonClick(x, libgdxy) || button==-99)
			{
				if(dlgType==YesNoDlgType.QUIT)
				{
					Gdx.app.exit();
				}
				
				if(dlgType==YesNoDlgType.NEWTOWN)
				{
					Gdx.app.debug("", "not implemented");
					
					//Achtung: bzombie, bconstruction, bnorealestate darauf achten dass das geht wenn hier ausgeführt
					
					//town.initgame
					//town.newGame(true, "veryeasy");
				}
				
				if(dlgType==YesNoDlgType.SAVETOWN)
				{
					town.gameGui.optionListDlg.showDlg(false);
					
					//FileHandle file = Gdx.files.local("data/save/"+sSaveTown_OverwriteFileName+".town");
					FileHandle file = CHelper.getFileHandle("appdata/local/HTP/data/save/"+sSaveTown_OverwriteFileName+".town");
					file.delete();
					
					town.gameWorld.saveToFile(sSaveTown_OverwriteFileName_clear, sSaveTown_OverwriteFileName);
				}
				
				if(dlgType==YesNoDlgType.LAUNCHSPACESHIP) 
				{
					//..
				}
				
				showDlg(false);
				return true;
			}
			
			
			
			if(buttonCancel.buttonClick(x, libgdxy))
			{
				showDlg(false);
				return true;
			}
		}
		else
		{
			showDlg(false);
		}
		
		//Gdx.app.debug("pos", "x: " + x +",y: " + libgdxy + ", genderManX: "+ genderManX + ", genderManY: "+genderManY);
		
		return true;
	}

	public void showDlg(Boolean show, YesNoDlgType dlgtype)
	{
		dialogH=180;
		if(dlgtype==YesNoDlgType.DEMO)
			dialogH=150;

		if(dlgtype==YesNoDlgType.LAUNCHSPACESHIP) {
			dialogW=500;
			dialogH=250;
		}
		
		if(dlgtype==YesNoDlgType.NOTRESEARCHED || dlgtype==YesNoDlgType.NOTUNLOCKED) {
			dialogW=600;
		}
		
		super.showDlg(show);
		this.dlgType=dlgtype;
	}
	
	public void buttonUp()
	{
	}
	
	public Boolean mouseMovedDrag(int x, int y, int libgdxy)
	{
		return false;
	}
	
	public void render(int x, int libgdxy)
	{
		
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
			
			//Trennlinie zu ok, cancel
			//dlgShapeRenderer.setColor(0.7f, 0.7f, 0.7f, 0.8f);
			dlgShapeRenderer.setColor(town.dialogRahmenColor);
			dlgShapeRenderer.line(dlgX+10, dlgY+50, dlgX+dialogW-10, dlgY+50); //Trennlinie zu ok und cancel				
			
		dlgShapeRenderer.end();
		//Gdx.gl.glDisable(GL30.GL_BLEND);		
		
		dlgSpriteBatch.begin();
			dlgSpriteBatch.setColor(1,1,1,1);
			
			dlgFont.getData().setScale(1);
			dlgFont.setColor(Color.WHITE);
			
			if(dlgType==YesNoDlgType.UNLOCKCOMPANY)
			{
				dlgFont.draw(dlgSpriteBatch, "Unlock " + unlockcompanyname+"?", dlgX+57, dlgY+130);
				dlgFont.draw(dlgSpriteBatch, "($"+town.unlockCompanyPrice+" One Time Fee)", dlgX+57, dlgY+110);
			}
			
			if(dlgType==YesNoDlgType.NOTRESEARCHED) {
				dlgFont.draw(dlgSpriteBatch, "The Blueprint contains objects that are not researched - ", dlgX+70, dlgY+130);
				dlgFont.draw(dlgSpriteBatch, "or objects from companies that are not unlocked.", dlgX+70, dlgY+110);
				String nr = "";
				if(listre.size()>0)
					nr+=listre.get(0);
				if(listre.size()>1)
					nr+=", " + listre.get(1);
				if(listre.size()>2)
					nr+=", " + listre.get(2);
				if(listre.size()>3)
					nr+=", ...";
				dlgFont.draw(dlgSpriteBatch, ""+nr, dlgX+70, dlgY+90);
			}

			if(dlgType==YesNoDlgType.NOTUNLOCKED)
				dlgFont.draw(dlgSpriteBatch, "The Blueprint contains objects from companies that are not unlocked.", dlgX+90, dlgY+130);
			
			if(dlgType==YesNoDlgType.QUIT)
				dlgFont.draw(dlgSpriteBatch, "Quit Game?", dlgX+90, dlgY+130);

			if(dlgType==YesNoDlgType.NEWTOWN)
				dlgFont.draw(dlgSpriteBatch, "New Town?", dlgX+90, dlgY+130);

			if(dlgType==YesNoDlgType.DEMO)
				dlgFont.draw(dlgSpriteBatch, "The Game Demo has ended", dlgX+57, dlgY+130);

			if(dlgType==YesNoDlgType.LAUNCHSPACESHIP) {
				dlgFont.draw(dlgSpriteBatch, "Congratulations!", dlgX+57, dlgY+200);
				dlgFont.draw(dlgSpriteBatch, "Your Spaceship is on the way to a new world.", dlgX+57, dlgY+140);
				dlgFont.draw(dlgSpriteBatch, "You saved humanity from extermination.", dlgX+57, dlgY+120);
				dlgFont.draw(dlgSpriteBatch, "Enjoy your accomplishment and celebrate your success!", dlgX+57, dlgY+90);
			}
			
			town.gameFont.layout.setText(dlgFont, sDlgText);
			
			if(dlgType==YesNoDlgType.DEFAULT)
				dlgFont.draw(dlgSpriteBatch, sDlgText, dlgX+dialogW/2-town.gameFont.layout.width/2, dlgY+130);
			
			if(dlgType==YesNoDlgType.SAVETOWN)
			{
				dlgFont.draw(dlgSpriteBatch, sDlgText, dlgX+dialogW/2-town.gameFont.layout.width/2, dlgY+130+20);
				
				town.gameFont.layout.setText(dlgFont, sSaveTown_OverwriteFileName_clear);
				dlgFont.draw(dlgSpriteBatch, sSaveTown_OverwriteFileName_clear, dlgX+dialogW/2-town.gameFont.layout.width/2, dlgY+130-20);
			}
			
		dlgSpriteBatch.end();
				
		
		if(dlgType==YesNoDlgType.DEMO)
		{
			buttonBUY.render(x, libgdxy);
			buttonQUIT.render(x, libgdxy);
			buttonNEWGAME.render(x, libgdxy);
		}
		else if (dlgType == YesNoDlgType.NOTRESEARCHED) {
			buttonOK.render(x, libgdxy);
		}
		else if (dlgType == YesNoDlgType.NOTUNLOCKED) {
			buttonOK.render(x, libgdxy);
		}
		else if(dlgType==YesNoDlgType.UNLOCKCOMPANY)
		{
			buttonUnlock.render(x, libgdxy);
			buttonCancel.render(x, libgdxy);
		}
		else if(dlgType==YesNoDlgType.LAUNCHSPACESHIP)
		{
			buttonOK.render(x, libgdxy);
			//buttonUnlock.render(x, libgdxy);
			//buttonCancel.render(x, libgdxy);
		}
		else
		{
			buttonOK.render(x, libgdxy);
			buttonCancel.render(x, libgdxy);
		}
	}
}
