package com.mygdx.game;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.Glyph;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.mygdx.game.CCompany.ArchitectWorkType;
import com.mygdx.game.CCompany.WorkoutputType;

public class CGuiDialog_TextInput extends CGuiDialog {
	
	public static enum TextInputType { 
		ADDRESS, SAVETOWN, CLONE_ADDRESS, SAVEREALESTATE
	}
	
	public TextInputType textInputType;
	
	public String sDialogInput1;
	public int maxLengthInput1;
	int adrPrice;
	private CGuiControl_Button buttonCreate;
	private CGuiControl_Button buttonCancel;
	
	int textInputX;
	int textInputY;
	int textInputW;
	int textInputH;
	
	public CAddress address;
	
	public CGuiDialog_TextInput(List<CObject> controlList, BitmapFont font, int dialogX, int dialogY, CTown town1)
	{
		super("CGuiDialog_TextInput");
		
		textInputType=TextInputType.ADDRESS;
		town=town1;
		adrPrice=0;
		address=null;
		
		dlgSpriteBatch = town1.gameGui.editorSpriteBatch;
		dlgShapeRenderer = town1.gameGui.shapeRenderer;
		dlgFont=font;
		sDialogInput1="";
		
		dlgX=dialogX;
		dlgY=dialogY;
		
		dialogW=400;
		dialogH=145;
		
		setMiddlePosition();
		
		textInputX=dlgX+70;
		textInputY=dlgY+dialogH-60;
		
		textInputW=310;
		textInputH=25;
		maxLengthInput1=textInputW-15;
		
		//buttonCreate=new CGuiControl_Button(textInputX, textInputY-300, 100, 25, 33, 18, "create", dlgFont, null, CGuiControl_Button.ButtonType.DEFAULT, town1);
		//buttonCancel=new CGuiControl_Button(textInputX+300, textInputY-300, 100, 25, 32, 18, "cancel", dlgFont, null, CGuiControl_Button.ButtonType.DEFAULT, town1);
		
		buttonCreate=new CGuiControl_Button(dlgX+dialogW/2-105, dlgY+15-2, 100, 25, 37, 18, "ok", dlgFont, null, CGuiControl_Button.ButtonType.DEFAULT, town1);
		buttonCancel=new CGuiControl_Button(dlgX+dialogW/2+5, dlgY+15-2, 100, 25, 31, 18, "cancel", dlgFont, null, CGuiControl_Button.ButtonType.DEFAULT, town1);
	}
	
	public void showDlg(Boolean show, TextInputType type)
	{
		textInputType=type;
		
		if(textInputType==TextInputType.ADDRESS)
		{
			buttonCreate.labelText="  ok";
			buttonCreate.textX=36;
		}
		
		if(textInputType==TextInputType.CLONE_ADDRESS)
		{
			buttonCreate.labelText="  ok";
			buttonCreate.textX=36;
		}
		
		if(textInputType==TextInputType.SAVETOWN)
		{
			buttonCreate.labelText="save";
			buttonCreate.textX=37;
		}

		if(textInputType==TextInputType.SAVEREALESTATE)
		{
			buttonCreate.labelText="save";
			buttonCreate.textX=37;
		}

		
		super.showDlg(show);

		if(show==true)
			sDialogInput1="";
	}
	
	public void setAddress(CAddress adr, int price)
	{
		address=adr;
		adrPrice=price;
	}
	
	public Boolean keyTyped(char character)
	{
		int ch = character;
		
		//if(ch==Input.Keys.ENTER)
		//Gdx.app.debug("", "enter");
		
		//Gdx.app.debug("a", String.valueOf(ch));
		
		if(ch==8) //Backspace
		{
			if(sDialogInput1.length()>0)
			{
				sDialogInput1 = sDialogInput1.substring(0, sDialogInput1.length()-1);
				return true;
			}
		}
		else
		{
			//Space, Zahlen, Großbuchstaben, Kleinbuchstaben
			if(ch==32 || (ch>=48 && ch<=57) || (ch>=65 && ch<=90) || (ch>=97 && ch<=122))
			{
				dlgFont.getData().setScale(1);
				town.gameFont.layout.setText(dlgFont, sDialogInput1);
				
				if(town.gameFont.layout.width<maxLengthInput1)
					sDialogInput1+=character;
			}
			
			return true;
		}
		
		return true;
	}
	
	private void setNewIds(CAddress adr)
	{
		for(CWorldObject wobj : adr.listWorldObjects)
		{
			wobj.uniqueId=wobj.getNextUniqueId();
			wobj.theaddressid=adr.addressId;
			wobj.theaddress=adr;
		}
		
		for(CWorldObject wobj : adr.listWorldObjects_Floors)
		{
			wobj.uniqueId=wobj.getNextUniqueId();
			wobj.theaddressid=adr.addressId;
			wobj.theaddress=adr;
		}
		
		for(CWorldObject wobj : adr.listWorldObjects_Ground)
		{
			wobj.uniqueId=wobj.getNextUniqueId();
			wobj.theaddressid=adr.addressId;
			wobj.theaddress=adr;
		}
	}
	
	private void cloneAddressList(ArrayList<CWorldObject> list)
	{
		for(CWorldObject wobj : list)
		{
			if(wobj.isHuman())
				continue;
			
			//wobj.uniqueId = wobj.
			
			town.gameWorld.townStatistics.getCurrentStatistics_Finance().buyObject+=Math.abs(wobj.theobject.price);
			
			
			//Gdx.app.debug("", "" + wobj.theobject.editoraction);
			
			if(wobj.theobject.isRoomObject)
			{
				town.gameWorld.linkWorldObjectAndCompany(wobj);
				town.gameWorld.worldRoomObjects.add(wobj);
				town.gameWorld.addObjectToPathmap(wobj, true);
			}
			else if(wobj.theobject.isGarbageContainer)
			{
				town.gameWorld.worldGarbageContainers.add(wobj);
				town.gameWorld.addObjectToPathmap(wobj, true);
			}
			else if(wobj.theobject.isGroundObject || wobj.theobject.isGroundBaseObject)
			{
				town.gameWorld.worldGroundObjects.add(wobj);
				if(wobj.theobject.isWaterObject)
					town.gameWorld.worldWaterObjects.add(wobj);
			}
			else if(wobj.theobject.editoraction.contains("road_road_road"))
			{
				town.gameWorld.worldRoad.add(wobj);
				town.gameWorld.addObjectToPathmap(wobj, true);
			}
			else if(wobj.theobject.editoraction.contains("road_road_footpath"))
			{
				town.gameWorld.worldFootpath.add(wobj);
				town.gameWorld.addObjectToPathmap(wobj, true);
			}
			else if(wobj.theobject.editoraction.contains("outdoor_light"))
			{
				town.gameWorld.worldOutdoorLights.add(wobj);
				//town.gameWorld.addObjectToPathmap(wobj, true);
			}
			else if(wobj.theobject.editoraction.contains("traffic_car"))
			{
				town.gameWorld.linkWorldObjectAndCompany(wobj);
				town.gameWorld.worldCars.add(wobj);
			}
			else if(wobj.theobject.editoraction.contains("zombie_entrance"))
			{
				town.gameWorld.worldZombieEntrances.add(wobj);
			}			
			else if(wobj.theobject.editoraction.contains("supermarket_foodpallet"))
			{
				town.gameWorld.linkWorldObjectAndCompany(wobj);
				town.gameWorld.worldDrawSpecial2.add(wobj);
				town.gameWorld.addObjectToPathmap(wobj, true);
			}
			else if(wobj.theobject.editoraction.contains("illuminati_defensewarningsystem"))
			{
				town.gameWorld.linkWorldObjectAndCompany(wobj);
				town.gameWorld.worldDefenseWarning.add(wobj);
				town.gameWorld.addObjectToPathmap(wobj, true);
			}			
			else if(wobj.theobject.editoraction.contains("company_waterworks_groundwaterextractionsystem"))
			{
				town.gameWorld.linkWorldObjectAndCompany(wobj);
				town.gameWorld.worldWatersystems.add(wobj);
				town.gameWorld.addObjectToPathmap(wobj, true);
			}			
			else if(wobj.theobject.isDrawSpecial())
			{
				town.gameWorld.linkWorldObjectAndCompany(wobj);
				town.gameWorld.worldDrawSpecial.add(wobj);
				town.gameWorld.addObjectToPathmap(wobj, true);
			}
			else if(wobj.theobject.editoraction.contains("_carpet"))
			{
				town.gameWorld.linkWorldObjectAndCompany(wobj);
				town.gameWorld.worldCarpets.add(wobj);
				town.gameWorld.addObjectToPathmap(wobj, true);
			}
			else if(wobj.theobject.editoraction.contains("interior_light"))
			{
				town.gameWorld.worldCoverlights.add(wobj);
			}
			else
			{
				town.gameWorld.linkWorldObjectAndCompany(wobj);
				town.gameWorld.worldObjects.add(wobj);
				town.gameWorld.addObjectToPathmap(wobj, true);
			}
			
			if(wobj.theobject.IsDrawAdditionalObject())
				town.gameWorld.tempListDrawAdditional.add(wobj);
		}
	}
	
	public void placeClonedAddress()
	{
		
		town.gameWorld.addressArchitectPlanningValue=0;
		address.addressId=CAddress.getNewAddressId(town);
		town.gameWorld.worldAddressList.add(address);
		town.gameWorld.changeTownMoney(-adrPrice);
		town.gameWorld.townStatistics.getCurrentStatistics_Finance().buyAddress+=Math.abs(adrPrice);
		
		if(sDialogInput1.length()<1)
			sDialogInput1 = address.addressId + " Main Street";
		
		address.addressName = sDialogInput1;
		
		cloneAddressList(address.listWorldObjects);
		cloneAddressList(address.listWorldObjects_Floors);
		cloneAddressList(address.listWorldObjects_Ground);
		setNewIds(address);
		
		address.isCloning=false;
		address=null;
		town.gameWorld.cloneAddressList.clear();
		town.gameWorld.bRenderFrameBuffer=true;
		town.gameGui.clonedAddress=null;
		town.gameGui.bAddressCloning=false;
		
		//Nach dem Setzen direkt neues Adrplacing starten für residential
		if(town.gameGui.realEstateCloneName!=null && town.gameGui.realEstateCloneName!="")
		{
			CAddress tempAddress=town.gameWorld.loadRealEstateObject(town.gameGui.realEstateCloneName);
			
			if(tempAddress!=null)
			{
				town.gameGui.bAddressCloning=true;
				tempAddress.isCloning=true;
				town.gameGui.clonedAddress = tempAddress;
				town.gameWorld.cloneAddressList.clear();
				town.gameWorld.cloneAddressList.add(tempAddress);
			}
		}
	}
	
	public Boolean buttonDown(int x, int y, int libgdxy, int button)
	{
		if(button==0 || button==-99)
		{
			if(buttonCreate.buttonClick(x, libgdxy) || button==-99)
			{
				if(textInputType==TextInputType.ADDRESS)
				{
					if(sDialogInput1.length()<1)
						sDialogInput1 = address.addressId + " Main Street";
					
					town.gameWorld.addressArchitectPlanningValue=0;
					
					address.addressName=sDialogInput1;
					town.gameWorld.worldAddressList.add(address);
					town.gameWorld.changeTownMoney(-adrPrice);
					town.gameWorld.townStatistics.getCurrentStatistics_Finance().buyAddress+=Math.abs(adrPrice);
					
					//town.gameWorld.townMoney-=adrPrice;
					address=null;
				}
				
				if(textInputType==TextInputType.CLONE_ADDRESS)
				{
					placeClonedAddress();
					
				}
				
				if(textInputType==TextInputType.SAVETOWN)
				{
					if(sDialogInput1==null || sDialogInput1.isEmpty())
						return true;
					
					town.gameGui.optionListDlg.showDlg(false);
					//Date dt = new Date();
					//sDialogInput1=sDialogInput1.replace(" ", "_");
					//town.gameWorld.saveToFile(sDialogInput1+"_"+town.gameMode+"_"+dt.getTime());
					town.gameWorld.saveToFile(sDialogInput1, "");
				}
				
				if(textInputType==TextInputType.SAVEREALESTATE)
				{
					if(sDialogInput1==null || sDialogInput1.isEmpty())
						return true;
					
					town.gameGui.optionListDlg.showDlg(false);
					town.gameWorld.saveRealEstate(address, sDialogInput1);
					town.setAchievement("firsttemplate");
				}				
				
				showDlg(false);
				
				return true;
			}
			
			if(buttonCancel.buttonClick(x, libgdxy))
			{
				cancelCloneAddress();
				
				sDialogInput1="";
				address=null;
				showDlg(false);
				return true;
			}
		}
		else
		{
			cancelCloneAddress();
			showDlg(false);
		}
		
		return true;
	}
	
	public void cancelCloneAddress()
	{
		if(textInputType==TextInputType.CLONE_ADDRESS)
		{
			int architect_costs = CCompany.getArchitectCosts(town, ArchitectWorkType.CLONE);
			
			Gdx.input.setCursorPosition(Gdx.input.getX(), Gdx.input.getY()); //wegen cursor catched
			town.gameWorld.cloneAddressList.clear();
			
			if(address!=null)
				address.dispose();
			
			if(town.gameGui.clonedAddress!=null)
				town.gameGui.clonedAddress.dispose();
			town.gameGui.clonedAddress=null;
			town.gameGui.cloneAddress=null;
			town.gameGui.bAddressCloning=false;
			
			if(town.gameGui.tempActionObject!=null && town.gameGui.tempActionObject.belongsToCompany != null)
			{
				town.gameGui.tempActionObject.belongsToCompany.addWorkOutput(architect_costs, WorkoutputType.DEFAULT);
			}
		}
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
		    dlgShapeRenderer.setColor(town.dialogRahmenColor);
			dlgShapeRenderer.rect(dlgX, dlgY, dialogW, dialogH);
			
			//Textinput Name
			dlgShapeRenderer.set(ShapeType.Filled);
			dlgShapeRenderer.setColor(0.6f, 0.6f, 0.6f, 0.24f);
			dlgShapeRenderer.rect(textInputX, textInputY, textInputW, textInputH);
			
			//Trennlinie zu ok, cancel
			dlgShapeRenderer.set(ShapeType.Line);
			dlgShapeRenderer.setColor(town.dialogRahmenColor);
			dlgShapeRenderer.line(dlgX+10, dlgY+50, dlgX+dialogW-10, dlgY+50);
			
		
		dlgShapeRenderer.end();
		//Gdx.gl.glDisable(GL30.GL_BLEND);		
		
		dlgSpriteBatch.begin();
		dlgSpriteBatch.setColor(1,1,1,1);
		
		dlgFont.getData().setScale(1);
		dlgFont.setColor(Color.WHITE);
		
		//Name
		dlgFont.draw(dlgSpriteBatch, "Name", textInputX-50, textInputY+19);
		dlgFont.draw(dlgSpriteBatch, sDialogInput1, textInputX+5, textInputY+17);
				
		dlgSpriteBatch.end();
				
		buttonCreate.render(x, libgdxy);
		buttonCancel.render(x, libgdxy);
	}
	
}
