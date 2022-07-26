package com.mygdx.game;

import java.awt.JobAttributes.DialogType;
import java.io.IOException;
import java.security.KeyException;
import java.security.PublicKey;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.xml.crypto.dsig.keyinfo.KeyValue;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.Glyph;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.mygdx.game.CGuiDialog_List.ListType;
import com.mygdx.game.CGuiDialog_TextInput.TextInputType;

public class CGuiDialog_OptionList extends CGuiDialog {

	public static enum OptionListType	{ 
		ScreenResolution, SaveTown, LoadTown, Skills, LoadRealEstate, LoadRealEstate2
	}
	
	public static class SkillObject
	{
		String objectid;
		String jobname;
		CObject theobject;
		
		public SkillObject(String objid, String skillname, CObject theobject)
		{
			objectid=objid;
			jobname=skillname;
			this.theobject=theobject;
		}
	}
	
	public class SaveTownObject implements Comparator<SaveTownObject>
	{
		Date datetime;
		String sdatetime;
		String name;
		
		public String getClearName(int type) //get filename without seconds appendix
		{
			String stemp="";
			
			if(sdatetime=="...")
				return "new";
			
			if(name.contains("_"))
			{
				String[] split = name.split("_");
				if(type==1)
					return split[split.length-2];
				String temp = split[split.length-1]+split[split.length-2];
				stemp = name.substring(0, name.length()-temp.length()-2);
			}
			
			return stemp;
		}

		public SaveTownObject(Date dt, String sdt, String na)
		{
			datetime=dt;
			sdatetime=sdt;
			name=na;
		}
		
		public int compare(SaveTownObject s1, SaveTownObject s2)
		{
			int comp = s1.datetime.compareTo(s2.datetime);
			
			if(comp==-1)
				comp=1;
			else if(comp==1)
				comp=-1;
			
			return comp;
		}
	}
	
	public class RealEstateObject implements Comparator<RealEstateObject>
	{
		CAddress address;
		String completename;
		String name;
		String addresstype;
		String companylabel;
		String price;
		Boolean researched;
		
		public String getClearName(int type) //get filename without seconds appendix
		{
			return name;
		}

		public RealEstateObject(String cplname)
		{
			address=null;
			completename=cplname;
			name=completename;
			
			researched=false;
			companylabel="residential";
			addresstype="";
			
			String p1 = town.gameWorld.loadRealEstateObjectReadPrice(cplname);
			//address=town.gameWorld.loadRealEstateObject(cplname);
			//price=""+address.getCloningPrice();
			//CCompany comp1 = address.getCompany();
			//if(comp1!=null)
			//	companylabel=comp1.getCompanyTypeLabel();
			
			
			if(p1.contains("HEADER"))
			{
				String[] split = p1.split(";");
				if(split.length>1)
					price=split[1];
				if(split.length>2)
					companylabel=split[2];
			}
			
			if(price==null || companylabel==null || companylabel=="")
			{
				CAddress adr = town.gameWorld.loadRealEstateObject(cplname);
				//Gdx.app.debug("", ""+adr.getCompany());
				price=""+adr.getCloningPrice();
				town.gameWorld.saveRealEstate(adr, cplname);
			}
			
			//address = town.gameWorld.loadRealEstateObject(completename);
			//if(completename.contains("_"))
			//{
			//	String[] split = completename.split("_");
			//	if(split.length>0)
			//		name=split[0];
			//	if(split.length>1)
			//		addresstype=split[1];
			//	if(split.length>3)
			//		companylabel=split[3];
			//}
		}
		
		public int compare(RealEstateObject s1, RealEstateObject s2)
		{
			int comp = s1.completename.compareTo(s2.completename);
			
			if(comp==-1)
				comp=1;
			else if(comp==1)
				comp=-1;
			
			return comp;
		}
	}	
	
	private CGuiControl_Button buttonOK;
	private CGuiControl_Button buttonCancel;
	private CGuiControl_Button buttonPagingLeft;
	private CGuiControl_Button buttonPagingRight;
	private CGuiControl_Button buttonFullscreen;
	
	Boolean bFullscreen;
	
	OptionListType listType;
	CObject control_hakenok;
	int eintragYStart;
	int eintragX;
	int eintragH;
	Boolean bChosen;
	int iChosen;
	int pageSize;
	int pageIndex;
	int activePageRowCount;
		
	DisplayMode[] displayModes;
	
	int typenr;
	
	CGuiTooltip tooltip;
	
	private List<String> list_DisplayModes;
	private List<SkillObject> list_Skills;
	private List<SaveTownObject> list_saveGames;
	private List<RealEstateObject> list_realestate;
	
	public CGuiDialog_OptionList(List<CObject> controlList, BitmapFont font, int dialogX, int dialogY, CTown town1)
	{
		super("CGuiDialog_List");
		
		typenr=0;
		iChosen=-1;
		town=town1;
		
		listType = OptionListType.ScreenResolution;
		
		dlgSpriteBatch = town1.gameGui.editorSpriteBatch;
		dlgShapeRenderer = town1.gameGui.shapeRenderer;
		dlgFont=font;
		tooltip=new CGuiTooltip(town1);
		dlgX=dialogX;
		dlgY=dialogY;
		
		dialogW=600;
		dialogH=500;
				
		setMiddlePosition();
		
		pageSize=17;
		pageIndex=1;
		activePageRowCount=0;
		
		bChosen=false;
		buttonOK=new CGuiControl_Button(dlgX+dialogW/2-105, dlgY+15-2, 100, 25, 35, 18, "apply", dlgFont, null, CGuiControl_Button.ButtonType.DEFAULT, town1);
		buttonCancel=new CGuiControl_Button(dlgX+dialogW/2+5, dlgY+15-2, 100, 25, 31, 18, "cancel", dlgFont, null, CGuiControl_Button.ButtonType.DEFAULT, town1);
		buttonFullscreen=new CGuiControl_Button(0, 0, 25, 25, -80, 13, "Windowed", dlgFont, null, CGuiControl_Button.ButtonType.CHECKBOX, town1);
		buttonPagingLeft=new CGuiControl_Button(dlgX+dialogW/2-30-20, dlgY+70, 30, 30, 0, 0, "", dlgFont, town.gameResourceConfig.textures.get("gui_arrowleft"), CGuiControl_Button.ButtonType.IMAGE, town1);
		buttonPagingRight=new CGuiControl_Button(dlgX+dialogW/2+20, dlgY+70, 30, 30, 0, 0, "", dlgFont, town.gameResourceConfig.textures.get("gui_arrowright"), CGuiControl_Button.ButtonType.IMAGE, town1);
		buttonPagingLeft.setColor(new Color(1,1,1,1));
		buttonPagingRight.setColor(new Color(1,1,1,1));
		buttonPagingLeft.renderMode = 0;
		
		initPosition();
		control_hakenok=null;
		Optional<CObject> obj = controlList.stream().filter(item->item.editoraction.contains("control_hakenok")).findFirst();
		if(obj.isPresent()) {
			control_hakenok=obj.get();
		}
	}
	
	public void initPosition()
	{
		eintragH=20;
		eintragYStart = dlgY+dialogH-15-eintragH;
		eintragX=dlgX+30;
		
		buttonOK.setPosition(dlgX+dialogW/2-105, dlgY+15-2);
		buttonCancel.setPosition(dlgX+dialogW/2+5, dlgY+15-2);
		buttonFullscreen.setPosition(dlgX+dialogW/2-105+90, dlgY+15-2+50);
		
		if(listType==OptionListType.ScreenResolution)
		{
			buttonPagingLeft.setPosition(dlgX+dialogW/2-30-20, dlgY+70+30);
			buttonPagingRight.setPosition(dlgX+dialogW/2+20, dlgY+70+30);
		}
		else if(listType==OptionListType.Skills)
		{
			buttonPagingLeft.setPosition(dlgX+dialogW/2-30-20, dlgY+70-5);
			buttonPagingRight.setPosition(dlgX+dialogW/2+20, dlgY+70-5);
		}
		else if(listType==OptionListType.LoadTown || listType==OptionListType.SaveTown)
		{
			buttonPagingLeft.setPosition(dlgX+dialogW/2-30-20, dlgY+70-5);
			buttonPagingRight.setPosition(dlgX+dialogW/2+20, dlgY+70-5);
		}
		else if(listType==OptionListType.LoadRealEstate || listType==OptionListType.LoadRealEstate2)
		{
			buttonPagingLeft.setPosition(dlgX+dialogW/2-30-20, dlgY+70-5);
			buttonPagingRight.setPosition(dlgX+dialogW/2+20, dlgY+70-5);
		}		
		else
		{
			buttonPagingLeft.setPosition(dlgX+dialogW/2-30-20, dlgY+70);
			buttonPagingRight.setPosition(dlgX+dialogW/2+20, dlgY+70);
		}
	}
	
	public void showDlg(Boolean show, OptionListType type)
	{
		super.showDlg(show);
		
		listType = type;
		
		if(show==true)
		{
			bChosen=false;
			iChosen=-1;
			
			pageIndex=1;
			activePageRowCount=0;
			
			if(listType==OptionListType.SaveTown)
			{
				dialogW=600;
				dialogH=500;
				pageSize=17;
				setMiddlePosition();
				
				buttonOK.labelText="save";
				buttonCancel.labelText="cancel";
				
				list_saveGames = new ArrayList<SaveTownObject>();
			}
			
			if(listType==OptionListType.LoadTown)
			{
				dialogW=600;
				dialogH=500;
				pageSize=17;
				setMiddlePosition();
				
				buttonOK.labelText=" load";
				buttonCancel.labelText="cancel";
				
				list_saveGames = new ArrayList<SaveTownObject>();
			}
			
			if(listType==OptionListType.LoadRealEstate || listType==OptionListType.LoadRealEstate2)
			{
				dialogW=600;
				dialogH=500;
				pageSize=17;
				//setMiddlePosition();
				
				buttonOK.labelText=" load";
				buttonCancel.labelText="cancel";
				
				list_realestate = new ArrayList<RealEstateObject>();
			}
			
			if(listType==OptionListType.LoadRealEstate2)
			{
				dialogW=300;
				dialogH=500;
			}
						
			if(listType==OptionListType.LoadRealEstate || listType==OptionListType.LoadRealEstate2)
			{
				/*
				list_realestate.add(new RealEstateObject("Basic Template 4 People"));
				list_realestate.add(new RealEstateObject("Basic Template 4 People 1 Car"));
				list_realestate.add(new RealEstateObject("Basic Template 4 People 2 Cars"));
				list_realestate.add(new RealEstateObject("Basic Template Construction Company"));
				list_realestate.add(new RealEstateObject("Basic Template Electrical Works"));
				list_realestate.add(new RealEstateObject("Basic Template Waterworks"));
				list_realestate.add(new RealEstateObject("Basic Template Supermarket"));
				list_realestate.add(new RealEstateObject("Basic Template Software Company"));
				list_realestate.add(new RealEstateObject("Basic Template Architect"));
				*/
				
				/*
				if(!town.isGameDemo)
				{
					list_realestate.add(new RealEstateObject("Basic Template Cemetery"));
					list_realestate.add(new RealEstateObject("Basic Template Church"));
					list_realestate.add(new RealEstateObject("Basic Template College"));
					list_realestate.add(new RealEstateObject("Basic Template Doctor"));
					list_realestate.add(new RealEstateObject("Basic Template Fitness Studio"));
					list_realestate.add(new RealEstateObject("Basic Template Fueling Station"));
					list_realestate.add(new RealEstateObject("Basic Template Pub"));
					list_realestate.add(new RealEstateObject("Basic Template Recycling Center"));
					list_realestate.add(new RealEstateObject("Basic Template School"));
					list_realestate.add(new RealEstateObject("Basic Template Town Hall"));
				}
				*/
				
				FileHandle dirHandle = CHelper.getFileHandle("appdata/local/HTP/data/realestate");
				
				String newString="";
				for (FileHandle entry: dirHandle.list())
				{
					if(!entry.name().contains(".csv"))
					{
						continue;
					}
					
					RealEstateObject reo = new RealEstateObject(entry.nameWithoutExtension());
					list_realestate.add(reo);
				}
				
				//list_realestate.sort((RealEstateObject s1, RealEstateObject s2) -> s1.compare(s1, s2));
			}			
			
			if(listType==OptionListType.LoadTown || listType==OptionListType.SaveTown)
			{
				//FileHandle dirHandle = Gdx.files.internal("./data/save");
				FileHandle dirHandle = CHelper.getFileHandle("appdata/local/HTP/data/save");
				
				String newString="";
				for (FileHandle entry: dirHandle.list())
				{
					long lastmod = entry.lastModified();
					Date dt = new Date(lastmod);
					DateFormat df = DateFormat.getDateInstance(DateFormat.FULL, Locale.US);
					
					newString = new SimpleDateFormat("yyyy/MM/dd hh:mma").format(dt);
					
					list_saveGames.add(new SaveTownObject(dt, newString, entry.nameWithoutExtension()));
				}
				
				list_saveGames.sort((SaveTownObject s1, SaveTownObject s2) -> s1.compare(s1, s2));
				
				if(listType==OptionListType.SaveTown)
					list_saveGames.add(0, new SaveTownObject(new Date(), "...", "new"));
			}
			
			if(listType==OptionListType.Skills)
			{
				buttonOK.labelText="  ok";
				if(list_Skills==null)
				{
					list_Skills=new ArrayList<SkillObject>();
					for(CObject obj : town.gameResourceConfig.listObject)
					{
						String title="";
						if(obj.workplaceHasSkill() || obj.workplaceHasSkill_taskbased())
						{
							title=obj.getCompanyWorkingPlaceJobTitle(0);
							Boolean bContinue=false;
							for(SkillObject so : list_Skills)
							{
								if(so.jobname.equals(title))
								{
									bContinue=true;
									break;
								}
							}
							
							if(bContinue)
								continue;
							
							list_Skills.add(new SkillObject(obj.objectId, title, obj));
						}
					}
				}
				
				Collections.sort(list_Skills,
		                 new Comparator<SkillObject>()
		                 {
		                     public int compare(SkillObject f1, SkillObject f2)
		                     {
		                         return f1.jobname.compareTo(f2.jobname);
		                     }        
		                 });				
			}
			
			if(listType==OptionListType.ScreenResolution)
			{
				dialogW=250;
				dialogH=508;
				pageSize=15;
				setMiddlePosition();
				buttonOK.labelText="apply";
				buttonCancel.labelText="cancel";
				displayModes = Gdx.graphics.getDisplayModes();
				list_DisplayModes = new ArrayList<String>();
				bFullscreen=!town.gameConfigIni.windowed;
				buttonFullscreen.toggleActive=!bFullscreen;
				
				for(DisplayMode mode : displayModes)
				{
					if(mode.width<1024)
						continue;
					
					//if(mode.width>1700) // best for menu darstellung
					//if(mode.width>2000)
					//	continue;
					
					//if(mode.height>1100)
					//	continue;
					
					if(mode.width==town.gameConfigIni.screenWidth && mode.height==town.gameConfigIni.screenHeight)
						continue;
					
					String smode=mode.width + "x" + mode.height;
					if(!list_DisplayModes.contains(smode))
						list_DisplayModes.add(smode);
				}
				
				list_DisplayModes.sort((String s1, String s2) -> s1.compareTo(s2));
				list_DisplayModes.sort(Comparator.reverseOrder());
				list_DisplayModes.add(0, town.gameConfigIni.screenWidth+"x" + town.gameConfigIni.screenHeight);
				iChosen=0;
				bChosen=true;
			}			
		}
		
		if(listType!=OptionListType.LoadRealEstate2) 
		{
			setMiddlePosition();
		}
		initPosition();
	}
	
	public Boolean doubleClick(int x, int y, int libgdxy, int button)
	{
		if(checkListClick(x,libgdxy))
		{
			create();
			return true;
		}
		
		return false;
	}
	
	public Boolean keyTyped(char character)
	{
		return true;
	}
	
	public void create()
	{
		if(listType==OptionListType.Skills)
		{
			if(bChosen)
			{
				this.showDlg(false);
				SkillObject skillobj = list_Skills.get((pageIndex-1)*pageSize+iChosen);
				town.gameGui.createResidentDlg.skillObject=skillobj;
				town.gameGui.createResidentDlg.showDlg(true);
			}
			
			return;
		}
		
		if(listType==OptionListType.SaveTown)
		{
			if(bChosen)
			{
				if(list_saveGames.get(iChosen).sdatetime.equals("..."))
				{
					town.gameGui.textInputDlg.showDlg(true, TextInputType.SAVETOWN);
				}
				else
				{
					town.gameGui.yesnoDlg.sSaveTown_OverwriteFileName = list_saveGames.get((pageIndex-1)*pageSize+iChosen).name;
					town.gameGui.yesnoDlg.sSaveTown_OverwriteFileName_clear = list_saveGames.get((pageIndex-1)*pageSize+iChosen).getClearName(0);
					
					town.gameGui.yesnoDlg.sDlgText = "Overwrite saved game?";
					town.gameGui.yesnoDlg.showDlg(true, CGuiDialog_YesNo.YesNoDlgType.SAVETOWN);
				}
			}
			
			return;
		}

		if(listType==OptionListType.LoadTown)
		{
			if(bChosen)
			{
				CWorld.pathmap=null;
				CWorld.pathmap_road=null;
				CWorld.pathmap_footpath=null;
				CWorld.pathmap_defensewall=null;
				CWorld.astar = null;
				CWorld.astar_road = null;
				CWorld.astar_footpath = null;
				if(town.gameWorld!=null)
					town.gameWorld.dispose();
				town.gameWorld=null;
				
				town.gameGui.town=null;
				if(town.gameGui!=null)
					town.gameGui.dispose();
				town.gameGui=null;
				town.gameInput.town=null;
				town.gameInput=null;
				
				System.gc();
				town.initStuff();
				
				town.sLoadSaveGame = list_saveGames.get((pageIndex-1)*pageSize+iChosen).name;
				town.bLoadingSaveGame=true;

				showDlg(false);
			}
			
			return;
		}
		
		if(listType==OptionListType.LoadRealEstate || listType==OptionListType.LoadRealEstate2)
		{
			if(bChosen)
			{
				if(listType==OptionListType.LoadRealEstate)
					showDlg(false);
				town.gameWorld.loadRealEstate(list_realestate.get((pageIndex-1)*pageSize+iChosen).completename);
			}
			
			return;
		}
		
		if(listType==OptionListType.ScreenResolution)
		{
			if(iChosen>-1)
			{
				String smode = list_DisplayModes.get((pageIndex-1)*pageSize+iChosen);
				
				String[] split = smode.split("x");
				int width=Integer.parseInt(split[0]); 
				int height=Integer.parseInt(split[1]);
				
				if(width!=Gdx.graphics.getWidth() || height!=Gdx.graphics.getHeight() || bFullscreen!=!town.gameConfigIni.windowed)
				{
					town.gameConfigIni.setDisplayMode(width, height, !bFullscreen);
					
					town.gameCam.viewportWidth=width;
					town.gameCam.viewportHeight=height;
					town.gameCam.update();	
					town.gameGui.init(town);
					town.screenInfo = new CScreenInfo(town);
					
					town.gameConfigIni.windowed=!bFullscreen;
					town.gameConfigIni.screenWidth=width;
					town.gameConfigIni.screenHeight=height;
					town.gameConfigIni.writeIni();
					
					if(town.gameWorld!=null)
						town.gameWorld.bRenderFrameBuffer=true;
				}
			}
		}
		
		showDlg(false);
	}
	
	public Boolean checkListClick(int x, int libgdxy)
	{
		int listcount = 0;
		
		if(listType==OptionListType.ScreenResolution)
			listcount = list_DisplayModes.size();
		
		if(listType==OptionListType.LoadTown || listType==OptionListType.SaveTown)
			listcount = list_saveGames.size();

		if(listType==OptionListType.LoadRealEstate || listType==OptionListType.LoadRealEstate2)
			listcount = list_realestate.size();
		
		if(listType==OptionListType.Skills)
			listcount = list_Skills.size();
		
		if(listcount>pageSize)
			listcount=pageSize;
		
		int count=0;
		
		for(int i=1;i<=listcount;i++)
		{
			int j=i-1;
			if(j>activePageRowCount)
				break;
			
			if(x>eintragX && x<eintragX-5+dialogW-5 && libgdxy>=eintragYStart-i*eintragH-eintragH && libgdxy<=eintragYStart-i*eintragH)
			{
				bChosen=true;
				iChosen=count;
				return true;
			}
			
			count++;
		}
		
		return false;
	}
	
	public Boolean buttonDown(int x, int y, int libgdxy, int button)
	{
		if(button==0 || button==-99)
		{
			if(buttonPagingLeft.buttonClick(x, libgdxy))
			{
				if(pageIndex>1)
				{
					bChosen=false;
					iChosen=-1;
					pageIndex--;
				}
				
				return true;
			}
			
			if(buttonPagingRight.buttonClick(x, libgdxy))
			{
				int tempnr = 0;
				if(listType==OptionListType.LoadTown || listType==OptionListType.SaveTown)
					tempnr = (int) Math.ceil(((double)list_saveGames.size())/pageSize);

				if(listType==OptionListType.LoadRealEstate || listType==OptionListType.LoadRealEstate2)
					tempnr = (int) Math.ceil(((double)list_realestate.size())/pageSize);

				if(listType==OptionListType.Skills)
					tempnr = (int) Math.ceil(((double)list_Skills.size())/pageSize);
				
				if(listType==OptionListType.ScreenResolution)
					tempnr = (int) Math.ceil(((double)list_DisplayModes.size())/pageSize);
				
				if(pageIndex < tempnr)
				{
					bChosen=false;
					iChosen=-1;
					pageIndex++;
				}
				
				return true;
			}
			
			if(buttonOK.buttonClick(x, libgdxy) || button==-99)
			{
				create();
				return true;
			}
			
			if(listType!=OptionListType.LoadRealEstate2)
			{
			if(buttonCancel.buttonClick(x, libgdxy))
			{
				showDlg(false);
				if(listType==OptionListType.Skills)
					town.gameGui.createResidentDlg.showDlg(true);
				return true;
			}
			}
						
			if(buttonFullscreen.buttonClick(x, libgdxy))
			{
				bFullscreen=!buttonFullscreen.toggleActive;
				return true;
			}
						
			checkListClick(x,libgdxy);
		}
		else
		{
			if(listType!=OptionListType.LoadRealEstate2)
			{
				showDlg(false);
				if(listType==OptionListType.Skills)
					town.gameGui.createResidentDlg.showDlg(true);
			}
		}
		
		if(listType==OptionListType.LoadRealEstate2)
		{
			return false;
		}
		
		return true;
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
		try
		{
		BitmapFont f1 = town.gameFont.bfArial2;
		f1.getData().setScale(0.58f);
		f1.setColor(1,1,1,1);
		
		int istart=0;
		int ilast=0;
		
		if(listType==OptionListType.ScreenResolution)
		{
			ilast = list_DisplayModes.size()-1;
			
			if(list_DisplayModes.size()>pageSize)
			{
				istart=pageSize*(pageIndex-1);
				
				if(pageIndex==1)
					istart=0;
				
				ilast=istart+pageSize-1;
				
				int maxPageIndex = (int)(Math.ceil(((double)list_DisplayModes.size())/pageSize)); 
				if(pageIndex==maxPageIndex)
				{
					ilast=(istart+list_DisplayModes.size()%pageSize)-1;
				}
			}
		}
		
		if(listType==OptionListType.LoadTown || listType==OptionListType.SaveTown)
		{
			ilast=list_saveGames.size()-1;
			
			if(list_saveGames.size()>pageSize)
			{
				istart=pageSize*(pageIndex-1);
				
				if(pageIndex==1)
					istart=0;
				
				ilast=istart+pageSize-1;
				
				int maxPageIndex = (int)(Math.ceil(((double)list_saveGames.size())/pageSize)); 
				if(pageIndex==maxPageIndex)
				{
					ilast=(istart+list_saveGames.size()%pageSize)-1;
				}
			}
		}
				
		if(listType==OptionListType.LoadRealEstate || listType==OptionListType.LoadRealEstate2)
		{
			ilast=list_realestate.size()-1;
			
			if(list_realestate.size()>pageSize)
			{
				istart=pageSize*(pageIndex-1);
				
				if(pageIndex==1)
					istart=0;
				
				ilast=istart+pageSize-1;
				
				int maxPageIndex = (int)(Math.ceil(((double)list_realestate.size())/pageSize)); 
				if(pageIndex==maxPageIndex)
				{
					ilast=(istart+list_realestate.size()%pageSize)-1;
				}
			}
		}
		
		if(listType==OptionListType.Skills)
		{
			ilast=list_Skills.size()-1;
			
			if(list_Skills.size()>pageSize)
			{
				istart=pageSize*(pageIndex-1);
				
				if(pageIndex==1)
					istart=0;
				
				ilast=istart+pageSize-1;
				
				int maxPageIndex = (int)(Math.ceil(((double)list_Skills.size())/pageSize)); 
				if(pageIndex==maxPageIndex)
				{
					ilast=(istart+list_Skills.size()%pageSize)-1;
				}
			}
		}		
		
		activePageRowCount=ilast-istart;
		
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
			
			
			//*********************
			//Mouseover GridTable
			//*********************
			int overX=0;
			int overY=0;
			int overChosen=0;
			
			int listcount = 0;
			
			if(listType==OptionListType.ScreenResolution)
				listcount = list_DisplayModes.size();
			
			if(listType==OptionListType.Skills)
				listcount = list_Skills.size();
			
			if(listType==OptionListType.LoadTown || listType==OptionListType.SaveTown)
				listcount = list_saveGames.size();
			
			if(listType==OptionListType.LoadRealEstate || listType==OptionListType.LoadRealEstate2)
				listcount = list_realestate.size();
			
			if(listcount>pageSize)
				listcount=pageSize;
			
			dlgShapeRenderer.set(ShapeType.Line);
			dlgShapeRenderer.setColor(0.7f, 0.7f, 0.7f, 0.8f);
			
			//Header Row
			//dlgShapeRenderer.rect(eintragX-5, eintragYStart+5-eintragH+15-3, dialogW-50, eintragH+6);
			
			//Trennlinie zu ok und cancel
			dlgShapeRenderer.setColor(town.dialogRahmenColor);
			dlgShapeRenderer.line(dlgX+20, dlgY+50, dlgX+dialogW-20, dlgY+50); 
			
			
			if(listType==OptionListType.ScreenResolution)
			{
				dlgShapeRenderer.line(dlgX+20, dlgY+50+40, dlgX+dialogW-20, dlgY+50+40);
				dlgShapeRenderer.line(dlgX+20, dlgY+50+90, dlgX+dialogW-20, dlgY+50+90);
			}
			
			
			if(bMouseOver)
			{
				int icount=1;
				for(int i=1;i<=listcount;i++)
				{
					int j=i-1;
					
					if(j>activePageRowCount)
						break;

					//Paging Control Rahmen
					if(x>eintragX && x<eintragX-5+dialogW-50 && libgdxy>=eintragYStart-i*eintragH-eintragH && libgdxy<=eintragYStart-i*eintragH)
					{
						overChosen=icount;
						overX=x;
						overY=eintragYStart-i*eintragH-eintragH;
						dlgShapeRenderer.setColor(0.7f, 0.7f, 0.7f, 0.8f);
						dlgShapeRenderer.rect(eintragX-5, eintragYStart+5-i*eintragH-eintragH, dialogW-50, eintragH);
						break;
					}
					
					icount++;
				}
			}
			
		dlgShapeRenderer.end();
		//Gdx.gl.glDisable(GL30.GL_BLEND);		
		
		
		
		
		dlgSpriteBatch.begin();
			dlgSpriteBatch.setColor(1,1,1,1);
			
			dlgFont.getData().setScale(1);
			dlgFont.setColor(town.dialogFontColorList);
			
			
			//********************
			//Draw Table Entries
			//********************
			int count=0;
			int x_spalte2=eintragX+300;
			int x_spalte3=eintragX+400;
			int x_spalte4=eintragX+500;
			int x_spalte5=eintragX+600;
			
			
			//Draw Paging Text
			
			if(listType==OptionListType.ScreenResolution)
			{
				String sPageIndex = String.format("%02d", pageIndex);
				String sPageCount = String.format("%02d", (int)(Math.ceil(((double)list_DisplayModes.size())/((double)pageSize))));
				dlgFont.draw(dlgSpriteBatch, sPageIndex + "/" + sPageCount, buttonPagingLeft.controlX+buttonPagingLeft.controlW + 4f, buttonPagingLeft.controlY+buttonPagingLeft.controlH/2+6);
			}
			else if(listType==OptionListType.SaveTown || listType==OptionListType.LoadTown)
			{
				if(list_saveGames.size()>pageSize)
				{
					String sPageIndex = String.format("%02d", pageIndex);
					String sPageCount = String.format("%02d", (int)(Math.ceil(((double)list_saveGames.size())/((double)pageSize))));
					dlgFont.draw(dlgSpriteBatch, sPageIndex + "/" + sPageCount, buttonPagingLeft.controlX+buttonPagingLeft.controlW + 4f, buttonPagingLeft.controlY+buttonPagingLeft.controlH/2+6);
				}
			}
			else if(listType==OptionListType.LoadRealEstate || listType==OptionListType.LoadRealEstate2)
			{
				if(list_realestate.size()>pageSize)
				{
					String sPageIndex = String.format("%02d", pageIndex);
					String sPageCount = String.format("%02d", (int)(Math.ceil(((double)list_realestate.size())/((double)pageSize))));
					dlgFont.draw(dlgSpriteBatch, sPageIndex + "/" + sPageCount, buttonPagingLeft.controlX+buttonPagingLeft.controlW + 4f, buttonPagingLeft.controlY+buttonPagingLeft.controlH/2+6);
				}
			}			
			else if(listType==OptionListType.Skills)
			{
				if(list_Skills.size()>pageSize)
				{
					String sPageIndex = String.format("%02d", pageIndex);
					String sPageCount = String.format("%02d", (int)(Math.ceil(((double)list_Skills.size())/((double)pageSize))));
					dlgFont.draw(dlgSpriteBatch, sPageIndex + "/" + sPageCount, buttonPagingLeft.controlX+buttonPagingLeft.controlW + 4f, buttonPagingLeft.controlY+buttonPagingLeft.controlH/2+6);
				}
			}
						
			
			if(listType==OptionListType.Skills)
			{
				//dlgSpriteBatch.setShader(town.gameFont.fontShader);
				f1.setColor(Color.WHITE);
				f1.draw(dlgSpriteBatch, "Choose Skill", eintragX, eintragYStart+18);
				dlgSpriteBatch.setShader(null);
			}
			
			if(listType==OptionListType.SaveTown)
			{
				//dlgSpriteBatch.setShader(town.gameFont.fontShader);
				f1.setColor(Color.WHITE);
				f1.draw(dlgSpriteBatch, "Save Town", eintragX, eintragYStart+18);
				dlgSpriteBatch.setShader(null);
			}
			
			if(listType==OptionListType.LoadTown)
			{
				//dlgSpriteBatch.setShader(town.gameFont.fontShader);
				f1.draw(dlgSpriteBatch, "Load Town", eintragX, eintragYStart+18);
				dlgSpriteBatch.setShader(null);
			}
			
			if(listType==OptionListType.LoadRealEstate || listType==OptionListType.LoadRealEstate2)
			{
				//dlgSpriteBatch.setShader(town.gameFont.fontShader);
				f1.draw(dlgSpriteBatch, "Blueprints", eintragX, eintragYStart+18);
				dlgSpriteBatch.setShader(null);
			}
			
			if(listType==OptionListType.LoadTown || listType==OptionListType.SaveTown)
			{
				for(int i=istart;i<=ilast;i++)
				{
					String dt = list_saveGames.get(i).sdatetime;
					String obj = list_saveGames.get(i).getClearName(0);
					//String mode = ""; //list_saveGames.get(i).getClearName(1);
					String mode = list_saveGames.get(i).getClearName(1);
					if(mode.toLowerCase().equals("expert"))
						mode="Expert";
					
					dlgFont.draw(dlgSpriteBatch, dt, eintragX, eintragYStart-count*eintragH-eintragH);
					String sname = town.gameFont.shortenStringToWidth(dlgFont, obj, 250);
					dlgFont.draw(dlgSpriteBatch, sname, eintragX+170, eintragYStart-count*eintragH-eintragH);
					dlgFont.draw(dlgSpriteBatch, mode.toUpperCase(), eintragX+460, eintragYStart-count*eintragH-eintragH);
					
					count++;
				}
			}
			
			if(listType==OptionListType.LoadRealEstate)
			{
				for(int i=istart;i<=ilast;i++)
				{
					RealEstateObject obj = list_realestate.get(i);
					
					String sname = town.gameFont.shortenStringToWidth(dlgFont, obj.name, 400);
					String str2 = obj.addresstype;
					if(obj.companylabel!=null && obj.companylabel.length()>0)
						str2 = obj.companylabel;
					
					dlgFont.draw(dlgSpriteBatch, sname + " ($" + obj.price + ")", eintragX, eintragYStart-count*eintragH-eintragH);
					dlgFont.draw(dlgSpriteBatch, str2.toUpperCase(), eintragX+380, eintragYStart-count*eintragH-eintragH);
					//dlgFont.draw(dlgSpriteBatch, comp.toUpperCase(), eintragX+460, eintragYStart-count*eintragH-eintragH);
					
					count++;
				}
			}			

			if(listType==OptionListType.LoadRealEstate2)
			{
				for(int i=istart;i<=ilast;i++)
				{
					RealEstateObject obj = list_realestate.get(i);
					
					String sname = town.gameFont.shortenStringToWidth(dlgFont, obj.name, 200);
					String str2 = obj.addresstype;
					if(obj.companylabel!=null && obj.companylabel.length()>0)
						str2 = obj.companylabel;
					
					//dlgFont.draw(dlgSpriteBatch, "$" + obj.price + " " + sname, eintragX, eintragYStart-count*eintragH-eintragH);
					dlgFont.draw(dlgSpriteBatch, sname + " ($" + obj.price + ")", eintragX, eintragYStart-count*eintragH-eintragH);
					
					
					count++;
				}
			}			

			
			if(listType==OptionListType.Skills)
			{
				for(int i=istart;i<=ilast;i++)
				{
					String dt = list_Skills.get(i).jobname;
					dlgFont.draw(dlgSpriteBatch, dt, eintragX, eintragYStart-count*eintragH-eintragH);
					count++;
				}
			}			
			
			if(listType==OptionListType.ScreenResolution)
			{
				f1.draw(dlgSpriteBatch, "Screen Resolution", eintragX, eintragYStart+18);
				dlgSpriteBatch.setShader(null);
				
				for(int i=istart;i<=ilast;i++)
				{
					String obj = list_DisplayModes.get(i);
					String sname = town.gameFont.shortenStringToWidth(dlgFont, obj, 300);
					dlgFont.draw(dlgSpriteBatch, sname, eintragX, eintragYStart-count*eintragH-eintragH);
					town.gameFont.layout.setText(dlgFont, sname);
					count++;
				}
			}
			
			
			//Draw Chosen Entry Check
			if(bChosen)
			{
				dlgSpriteBatch.draw(control_hakenok.textureImage, eintragX-25, eintragYStart-(iChosen+1)*eintragH-eintragH/1.5f, 15, 15);
			}
			
			
			//Tooltip
			int index1 = ((pageIndex-1)*pageSize + overChosen-1);
			if((listType==OptionListType.LoadRealEstate || listType==OptionListType.LoadRealEstate2) && index1>=0)
			{
				int mx=x;
				int my=libgdxy;
				
				RealEstateObject colObject = list_realestate.get(index1);
				
				if(overX>0 && overY>0 && overChosen>0)
				{
					dlgSpriteBatch.setColor(1, 1, 1, 1f);
		        	tooltip.textLines.clear();
					dlgSpriteBatch.end();
					tooltip.setColor(Color.WHITE);
					try
					{
						if(town.gameWorld.townMoney<Integer.parseInt(colObject.price))
						{
							tooltip.setColor(Color.RED);
							tooltip.textLines.add("Not enough money");
						}
						
						if(colObject.address==null)
						{
							//colObject.address = town.gameWorld.loadRealEstateObject(colObject.completename);
						}
							
						ArrayList<String> listnr = new ArrayList<String>();
						//ArrayList<String> listnr = colObject.address.allObjectsResearched();
						
						if(listnr.size()>0)
						{
							tooltip.setColor(Color.RED);
							tooltip.textLines.add("This Address Template contains Objects that are currently not researched");
							for(String str : listnr)
								tooltip.textLines.add("- " + str);
						}
						
						if(tooltip.textLines.size()>0)
							tooltip.draw(mx+30, my+10);
					}
					catch(Exception e){}
					
					tooltip.setColor(Color.WHITE);
					dlgSpriteBatch.begin();
				}
			}		
			
			//Tooltips			
//			if(listType==ListType.ScreenResolution)
//			{
//				if(overX>0 && overY>0 && overChosen>0)
//				{
//					CWorldObject chosen=null;
//					dlgFont.setColor(1,1,1,1);
//					float theight=0;
//					float twidth=0;
//					chosen = listWorker.get((pageIndex-1)*pageSize + overChosen-1);
//					String workplaces="";
//					int ic=0;
//					tooltip.textLines.clear();
//					
//					for(CWorldObject wo : chosen.thehuman.workplaces.values())
//					{
//						workplaces=wo.getCompanyWorkingPlaceJobTitle() + " (" + wo.getWorkingHoursString() + ")";
//						tooltip.textLines.add(workplaces);
//						ic++;
//					}
//					
//					for(CWorldObject wo : chosen.thehuman.taskobjects.values())
//					{
//						int type=0;
//						if(wo.worker!=null && wo.worker.uniqueId==chosen.uniqueId)
//							type=1;
//						workplaces=wo.getTaskText(type);
//						tooltip.textLines.add(workplaces);
//						ic++;
//					}
//					
//					dlgSpriteBatch.end();
//					if(tooltip.textLines.size()>0)
//						tooltip.draw(overX, overY);
//					dlgSpriteBatch.begin();
//				}
//			}
			
			dlgSpriteBatch.end();
			
			buttonOK.render(x, libgdxy);
			
			if(listType!=OptionListType.LoadRealEstate2)
				buttonCancel.render(x, libgdxy);
			
			buttonFullscreen.setColor(new Color(1,1,1,0.97f));
			
			if(listType==OptionListType.ScreenResolution)
				buttonFullscreen.render(x, libgdxy);
			
			if(listType==OptionListType.ScreenResolution || listType==OptionListType.Skills)
			{
				buttonPagingLeft.render(x, libgdxy);
				buttonPagingRight.render(x, libgdxy);
			}
			else if(listType==OptionListType.SaveTown || listType==OptionListType.LoadTown)
			{
				if(list_saveGames.size()>pageSize)
				{
					buttonPagingLeft.render(x, libgdxy);
					buttonPagingRight.render(x, libgdxy);
				}
			}
			else if(listType==OptionListType.LoadRealEstate || listType==OptionListType.LoadRealEstate2)
			{
				if(list_realestate.size()>pageSize)
				{
					buttonPagingLeft.render(x, libgdxy);
					buttonPagingRight.render(x, libgdxy);
				}
			}
		}
		catch(Exception ex)
		{
			CHelper.writeError(ex.getMessage(), ex.getStackTrace(), ex);
		}
	}
}


