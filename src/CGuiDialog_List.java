package com.mygdx.game;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.mygdx.game.CCompany.CompanyType;
import com.mygdx.game.CGuiDialog_OptionList.OptionListType;
import com.mygdx.game.CGuiDialog_OptionList.SkillObject;
import com.mygdx.game.CGuiDialog_TextInput.TextInputType;
import com.mygdx.game.CHuman.CJobSkillClass;

public class CGuiDialog_List extends CGuiDialog {
	
	public static enum ListType	{ 
		WORKER, BED_OWNER, DINING_SEATOWNER, DINNER_COOK, SCHOOL_STUDENT, CAR_OWNER, COLLEGE_STUDENT, RESEARCH_PROJECT
		, STATISTICS_FINANCE, STATISTICS_POPULATION, STATISTICS_OTHER, WARDROBE_OWNER, VIEW_RESIDENTS
		, OBJECT_DESIGN, NEWTOWN, HOMELESS, NEWTOWN2
	}
	
	private CGuiControl_Button buttonOK;
	private CGuiControl_Button buttonNew;
	private CGuiControl_Button buttonDistribute;
	private CGuiControl_Button buttonZombieApocalypse;
	private CGuiControl_Button buttonExpert;
	private CGuiControl_Button buttonRealEstate;
	private CGuiControl_Button buttonConstruction;
	private CGuiControl_Button buttonHomeless;
	private CGuiControl_Button buttonSaveRealEstate;
	private CGuiControl_Button buttonCancel;
	private CGuiControl_Button buttonLoad;
	private CGuiControl_Button buttonPagingLeft;
	private CGuiControl_Button buttonPagingRight;
	private CGuiControl_Points pointsControl_education;
	
	CObject control_hakenok;
	int eintragYStart;
	int eintragX;
	int eintragH;
	Boolean bChosen;
	List<Integer> chosenList;
	int iChosen;
	int pageSize;
	int pageIndex;
	int activePageRowCount;
	
	ListType listType;
	int typenr;
	CGuiTooltip tooltip;
	CAddress address;
	private List<CWorldObject> listWorker;
	private List<CObject> listItem;
	private List<String> listText;
	
	BitmapFont f1;
	
	public CGuiDialog_List(List<CObject> controlList, BitmapFont font, int dialogX, int dialogY, CTown town1)
	{
		super("CGuiDialog_ChooseResident");
		
		typenr=0;
		iChosen=-1;
		chosenList=new ArrayList<Integer>();
		listWorker=null;
		town=town1;
		
		listType=ListType.WORKER;
		
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
		
		buttonOK = new CGuiControl_Button(dlgX+dialogW/2-105, dlgY+15-2, 100, 25, 43, 18, "Ok", dlgFont, null, CGuiControl_Button.ButtonType.DEFAULT, town1);
		buttonCancel = new CGuiControl_Button(dlgX+dialogW/2+5, dlgY+15-2, 100, 25, 31, 18, "Cancel", dlgFont, null, CGuiControl_Button.ButtonType.DEFAULT, town1);
		buttonLoad = new CGuiControl_Button(dlgX+dialogW/2+5, dlgY+15-2, 100, 25, 20, 18, "load town", dlgFont, null, CGuiControl_Button.ButtonType.DEFAULT, town1);
		//buttonNew = new CGuiControl_Button(dlgX+dialogW/2-105+200, dlgY+15-2, 100, 25, 19, 18, "customize", dlgFont, null, CGuiControl_Button.ButtonType.DEFAULT, town1);
		buttonNew = new CGuiControl_Button(dlgX+dialogW/2-105+200, dlgY+15-2, 100, 25, 19, 18, "Customize", dlgFont, null, CGuiControl_Button.ButtonType.DEFAULT, town1);
		buttonNew.enableTooltip(true);
		buttonNew.setTooltip("Create new Worker with the right skill for this workplace");
		buttonNew.setTooltipPosition(-200,0);
		
		buttonDistribute = new CGuiControl_Button(dlgX+dialogW/2-105+120, dlgY+15-2+30, 100, 25, 25, 13, "Distribute", dlgFont, null, CGuiControl_Button.ButtonType.CHECKBOX, town1);
		buttonZombieApocalypse = new CGuiControl_Button(dlgX+dialogW/2-105+120, dlgY+15-2+30, 100, 25, 25, 13, "Zombie Apocalypse", dlgFont, null, CGuiControl_Button.ButtonType.CHECKBOX, town1);
		buttonRealEstate = new CGuiControl_Button(dlgX+dialogW/2-105+120, dlgY+15-2+30, 100, 25, 25, 13, "No Real Estate (Experimental)", dlgFont, null, CGuiControl_Button.ButtonType.CHECKBOX, town1);
		buttonConstruction = new CGuiControl_Button(dlgX+dialogW/2-105+120, dlgY+15-2+30, 100, 25, 25, 13, "Construction", dlgFont, null, CGuiControl_Button.ButtonType.CHECKBOX, town1);
		
		buttonExpert = new CGuiControl_Button(dlgX+dialogW/2+50, dlgY+15-2+30, 100, 25, 25, 13, "Expert Challenge", dlgFont, null, CGuiControl_Button.ButtonType.CHECKBOX, town1);
		
		//buttonSaveRealEstate = new CGuiControl_Button(dlgX+dialogW/2-105+120, dlgY+15-2+30, 100, 25, 35, 18, "Save", dlgFont, null, CGuiControl_Button.ButtonType.DEFAULT, town1);
		buttonSaveRealEstate = new CGuiControl_Button(dlgX+dialogW/2-105+120, dlgY+15-2+30, 170, 25, 30, 18, "Save as Blueprint", dlgFont, null, CGuiControl_Button.ButtonType.DEFAULT, town1);
		
		buttonSaveRealEstate.setTooltip("Save Address as Real Estate Template");
		buttonHomeless = new CGuiControl_Button(dlgX+dialogW/2-105+120, dlgY+15-2+30, 100, 25, 21, 18, "Homeless", dlgFont, null, CGuiControl_Button.ButtonType.DEFAULT, town1);
		buttonHomeless.setTooltip("Show homeless people");
		buttonPagingLeft = new CGuiControl_Button(dlgX+dialogW/2-30-20, dlgY+70, 30, 30, 0, 0, "", dlgFont, town.gameResourceConfig.textures.get("gui_arrowleft"), CGuiControl_Button.ButtonType.IMAGE, town1);
		buttonPagingRight = new CGuiControl_Button(dlgX+dialogW/2+20, dlgY+70, 30, 30, 0, 0, "", dlgFont, town.gameResourceConfig.textures.get("gui_arrowright"), CGuiControl_Button.ButtonType.IMAGE, town1);
		buttonPagingLeft.setColor(new Color(1,1,1,1));
		buttonPagingRight.setColor(new Color(1,1,1,1));
		buttonPagingLeft.renderMode = 0;
		
		initPosition();
		control_hakenok=null;
		Optional<CObject> obj = controlList.stream().filter(item->item.editoraction.contains("control_hakenok")).findFirst();
		if(obj.isPresent())
			control_hakenok=obj.get();
		
		pointsControl_education=new CGuiControl_Points(town, 0, 0, 3, 1, town.gameGui.shapeRenderer, dlgSpriteBatch);
		pointsControl_education.showButtons(false);	
		pointsControl_education.setBeginShapeRenderer(true);
		pointsControl_education.setPointSize(10);
	}
	
	public void initPosition()
	{
		eintragH=20;
		eintragYStart = dlgY+dialogH-15-eintragH;
		eintragX=dlgX+30;
		
		buttonOK.setPosition(dlgX+dialogW/2-105, dlgY+15-2);
		buttonCancel.setPosition(dlgX+dialogW/2+5, dlgY+15-2);
		buttonLoad.setPosition(dlgX+dialogW/2+5, dlgY+15-2);
		
		buttonNew.setPosition(dlgX+dialogW/2+266, dlgY+15-2);
		buttonDistribute.setPosition(dlgX+dialogW/2+241, dlgY+15-2+7);
		buttonZombieApocalypse.setPosition(dlgX+dialogW/2-100, dlgY+70);
		buttonExpert.setPosition(dlgX+dialogW/2-50, dlgY+70);
		buttonRealEstate.setPosition(dlgX+dialogW/2-100, dlgY+100);
		buttonConstruction.setPosition(dlgX+dialogW/2-100, dlgY+130);
		buttonHomeless.setPosition(dlgX+dialogW/2+241+28, dlgY+15-2);
		buttonSaveRealEstate.setPosition(dlgX+28, dlgY+15-2);
		
		if(listType==ListType.VIEW_RESIDENTS)
			buttonOK.setPosition(dlgX+dialogW/2-50, dlgY+15-2);
		
		buttonPagingLeft.setPosition(dlgX+dialogW/2-30-20, dlgY+70);
		buttonPagingRight.setPosition(dlgX+dialogW/2+20, dlgY+70);
	}
	
	public void showDlg(Boolean show, CAddress adr, ListType type, int typenr)
	{
		buttonDistribute.toggleActive=false;
		buttonZombieApocalypse.toggleActive=false;
		buttonExpert.toggleActive=false;
		buttonRealEstate.toggleActive=false;
		buttonConstruction.toggleActive=true;
		
		buttonOK.labelText="Ok";
		buttonOK.textX=43;
		
		CWorldObject mobj = town.gameWorld.markerObject;
		super.showDlg(show);
		this.typenr=typenr;
		listType = type;
		
		if(show==true)
		{
			f1 = town.gameFont.bfArial2;
			f1.getData().setScale(0.58f);
			f1.setColor(1,1,1,1);
			
			dialogW=600;
			dialogH=500;
			
			if(listType==ListType.WORKER || listType==ListType.DINNER_COOK || listType==ListType.DINING_SEATOWNER || listType==ListType.VIEW_RESIDENTS || listType==ListType.HOMELESS)
				dialogW=780;
			else if(listType==ListType.SCHOOL_STUDENT || listType==ListType.BED_OWNER || 
					listType==ListType.CAR_OWNER || listType==ListType.COLLEGE_STUDENT || 
					listType==ListType.WARDROBE_OWNER)  
				dialogW=700;
			else if(listType==ListType.RESEARCH_PROJECT || listType==ListType.OBJECT_DESIGN)
				dialogW=700;
			else if(listType==ListType.NEWTOWN || listType==ListType.NEWTOWN2)
			{
				dialogW=400;
				dialogH=260+40+40+30;
			}
			else
				dialogW=600;
			
			setMiddlePosition();
			
			initPosition();
			
			address=adr;
			bChosen=false;
			iChosen=-1;
			chosenList.clear();
			pageIndex=1;
			activePageRowCount=0;
			
			listWorker = new ArrayList<CWorldObject>();
			listItem = new ArrayList<CObject>();
			listText = new ArrayList<String>();
			
			if(listType==ListType.RESEARCH_PROJECT || listType==ListType.OBJECT_DESIGN)
			{
				for(CObject obj : town.gameResourceConfig.listObject)
				{
					if(!obj.linkobjectid.isEmpty())
					{
						Boolean bContinue=false;
						for(CObject obj1 : listItem)
						{
							if(obj.linkobjectid.equals(obj1.linkobjectid))
							{
								if(obj1.iResearchCurrentWorkoutput < obj.iResearchCurrentWorkoutput)
									obj1.iResearchCurrentWorkoutput = obj.iResearchCurrentWorkoutput;
								
								bContinue=true;
								break;
							}
						}
						
						if(bContinue)
							continue;
					}
					
					if(listType==ListType.OBJECT_DESIGN && !obj.bIsResearchDesignObject)
						continue;
					
					if(listType==ListType.RESEARCH_PROJECT)
						if(obj.bIsResearchDesignObject)
							continue;
					
					if(town.bResearchShowCompleteList)
					{
						if(obj.iResearchTargetWorkoutput==0)
							continue;
					}
					else
					{
						if(town.gameWorld.worldHumans.size()<obj.ATTR_RESPCOUNT) //Mindest Anzahl Residents in Town
							continue;
						
						if(obj.iResearchTargetWorkoutput==0) //nur objekte anzeigen die erforscht werden m¸ssen
							continue;
						
						if(obj.iResearchCurrentWorkoutput>=obj.iResearchTargetWorkoutput) //erforschte objekte nicht anzeigen
							continue;
					}
					
					if(!obj.sResearchBaseObjectId.isEmpty()) //parentresearchobject muss erforscht sein
					{
						Optional<CObject> tempobj = town.gameResourceConfig.listObject.stream().filter(item->item.objectId.equals(obj.sResearchBaseObjectId)).findFirst();
						if(tempobj.isPresent())
						{
							if(tempobj.get().iResearchCurrentWorkoutput<tempobj.get().iResearchTargetWorkoutput)
							{
								continue;
							}
						}
					}
					
					listItem.add(obj);
				}
			}
			
			if(listType==ListType.SCHOOL_STUDENT)
			{
				if(address!=null && address.addressType.contains("public"))
				{
					//listWorker = town.gameWorld.worldHumans.stream().filter(item->!item.bIsDead && item.thehuman.getAge()>=7 && item.thehuman.getAge()<=17 && item.thehuman.getEducationValue()<CCompany.getMaxSchoolEducation()).collect(Collectors.toList());
					//keine Altersbeschr‰nkung f¸r Schule
					listWorker = town.gameWorld.worldHumans.stream().filter(item->!item.bIsDead && item.iZombie<1 && item.thehuman.getAge()>=7 && item.thehuman.getEducationValue()<town.initial_maxschooleducation).collect(Collectors.toList());
					listWorker.sort(new CWorldObject.JobCountComparator());
				}
			}
			
			if(listType==ListType.COLLEGE_STUDENT)
			{
				if(address!=null && address.addressType.contains("public"))
				{
					listWorker = town.gameWorld.worldHumans.stream().filter(item->!item.bIsDead && item.iZombie<1 && item.thehuman.getAge()>=14 && item.thehuman.getEducationValue()>=mobj.theobject.getRequiredWorkplaceEducation() && item.thehuman.getEducationValue()<town.initial_maxcollegeeducation).collect(Collectors.toList());
					listWorker.sort(new CWorldObject.JobCountComparator());
				}
			}
			
			if(listType==ListType.HOMELESS)
			{
				for(CWorldObject wobj : town.gameWorld.worldHumans)
				{
					if(wobj.theaddress==null && !wobj.bIsDead && wobj.iZombie<1)
						listWorker.add(wobj);
				}
			}
			
			if(listType==ListType.VIEW_RESIDENTS)
			{
				if(address!=null && address.addressType.contains("public"))
				{
					for(CWorldObject wobj : address.listWorldObjects)
					{
						if(wobj.worker!=null && !listWorker.contains(wobj.worker))
							listWorker.add(wobj.worker);
						
						if(wobj.worker2!=null && !listWorker.contains(wobj.worker2))
							listWorker.add(wobj.worker2);
					}
				}
				
				if(address!=null && address.addressType.contains("residential"))
				{
					//for(CWorldObject aaa : address.listWorldObjects)
					//{
					//	if(aaa.thehuman!=null)
					//		Gdx.app.debug("", ""+aaa.thehuman.getName());
					//}
					
					listWorker=address.listWorldObjects.stream().filter(item->!item.bIsDead && item.iZombie<1 && item.thehuman!=null).collect(Collectors.toList());
				}
				
				listWorker.sort(new CWorldObject.JobCountComparator());				
			}
			
			if(listType==ListType.WORKER)
			{
				Boolean bWork=true;
				if(!mobj.isCompanyObject())
						bWork=false;
				if(address!=null && address.addressType.contains("residential"))
					bWork=false;
				if(mobj.theobject.editoraction.toLowerCase().contains("illuminati"))
					bWork=true;
				
				//Wenn es Arbeit/Task auf Privatadresse ist, kˆnnen das nur Bewohner der Adresse tun
				//if(address!=null && address.addressType.contains("residential"))
				if(mobj.theobject.getRequiredWorkplaceEducation()>-1)
				{
					listWorker = town.gameWorld.worldHumans.stream().filter(item->!item.bIsDead && item.thehuman.getAge()>=mobj.getMinAge() && item.thehuman.getEducationValue()>=mobj.theobject.getRequiredWorkplaceEducation()).collect(Collectors.toList());
				}
				else
				{
					if(address!=null && address.addressType.contains("residential"))
						listWorker=address.listWorldObjects.stream().filter(item->!item.bIsDead && item.iZombie<1 && item.thehuman!=null && item.thehuman.getAge()>=mobj.getMinAge()).collect(Collectors.toList());
					else if(address!=null && address.addressType.contains("public"))
					{
						//Residential Task auf Public Address -> Alle Worker anzeigen
						//for(CWorldObject wobj : address.listWorldObjects)
						//	if(wobj.worker!=null && !listWorker.contains(wobj))
						//		listWorker.add(wobj);
						listWorker = town.gameWorld.worldHumans.stream().filter(item->!item.bIsDead && item.thehuman.getAge()>=16).collect(Collectors.toList());
					}
				}
				
				//if(!bWork)
				//{
					//m¸ssen>=14 jahre alt sein
				//	listWorker=address.listWorldObjects.stream().filter(item->!item.bIsDead && item.iZombie<1 && item.thehuman!=null && item.thehuman.getAge()>=mobj.getMinAge()).collect(Collectors.toList());
				//}
				//else
				//	listWorker = town.gameWorld.worldHumans.stream().filter(item->!item.bIsDead && item.thehuman.getAge()>=mobj.getMinAge() && item.thehuman.getEducationValue()>=mobj.theobject.getRequiredWorkplaceEducation()).collect(Collectors.toList());
				
				try
				{
					if(listWorker!=null && listWorker.size()>0)
						listWorker.sort(new CWorldObject.JobCountComparator());
				}
				catch(Exception ex)
				{
					//..
				}
				
				//Maintenance Worker schneller finden
				if(mobj.isMaintenanceObject())
				{
					List<CWorldObject> templist = new ArrayList<CWorldObject>();
					for(CWorldObject wobj : listWorker)
					{
						for(CWorldObject job : wobj.thehuman.workplaces.values())
							//if(job.isMaintenanceObject())
							if(job.theobject.editoraction.equals(mobj.theobject.editoraction))
							{
								if(!templist.contains(wobj))
									templist.add(wobj);
							}
					}
					
					listWorker.removeAll(templist);
					listWorker.addAll(0, templist);
				}
			}
			
			if(listType==ListType.DINNER_COOK)
			{
				if(address!=null)
				{
					Collection<CWorldObject> l1 = null;
										
					CCompany comp = mobj.theaddress.getCompany();
					
					if(address.addressType.contains("public") && comp!=null)
						l1=comp.getWorkerList();
					else if(address.addressType.contains("residential"))
						l1=address.listWorldObjects;
					
					if(l1!=null)
					{
						listWorker=l1.stream().filter(item->!item.bIsDead && item.iZombie<1 && item.thehuman!=null && item.thehuman.getAge()>=16 && item.thehuman.getEducationValue()>=mobj.theobject.getRequiredWorkplaceEducation()).collect(Collectors.toList());
					}
				}
				
				//Wenn es Arbeit/Task auf Privatadresse ist, kˆnnen das nur Bewohner der Adresse tun
				//if(address!=null && address.addressType.contains("residential"))
				//{
					//m¸ssen>=14 jahre alt sein
				//	listWorker=address.listWorldObjects.values().stream().filter(item->!item.bIsDead && item.thehuman!=null && item.thehuman.getAge()>=14).collect(Collectors.toList());
				//}
				
				listWorker.sort(new CWorldObject.JobCountComparator());
			}
			
			if(listType==ListType.DINING_SEATOWNER)
			{
				//alle die nicht auf marker dinner table als owner hinterlegt sind
				//auﬂer cook:  kann auch seatplace owner sein
				
				Collection<CWorldObject> l1 = null;
				
				CCompany comp = mobj.theaddress.getCompany();
				
				if(address.addressType.contains("public") && comp!=null)
					l1=comp.getWorkerList();
				else
					l1=address.listWorldObjects;
				
				listWorker=new ArrayList<CWorldObject>();
				for(CWorldObject resident : l1)
				{
					if(!resident.isHuman())
						continue;
					
					if(resident.bIsDead)
						continue;
					
					Boolean bcontinue=false;
					for(CWorldObject to : resident.thehuman.taskobjects.values())
					{
						if(to.theobject.editoraction.contains("diningroom_diningtable") && to.uniqueId==town.gameWorld.markerObject.uniqueId)
						{
							if(to.owner!=null && to.owner.uniqueId==resident.uniqueId)
							{
								bcontinue=true;
								break;
							}
							if(to.owner2!=null && to.owner2.uniqueId==resident.uniqueId)
							{
								bcontinue=true;
								break;
							}
							if(to.owner3!=null && to.owner3.uniqueId==resident.uniqueId)
							{
								bcontinue=true;
								break;
							}
							if(to.owner4!=null && to.owner4.uniqueId==resident.uniqueId)
							{
								bcontinue=true;
								break;
							}
							if(to.owner5!=null && to.owner5.uniqueId==resident.uniqueId)
							{
								bcontinue=true;
								break;
							}
							if(to.owner6!=null && to.owner6.uniqueId==resident.uniqueId)
							{
								bcontinue=true;
								break;
							}
							
							if(to.owner7!=null && to.owner7.uniqueId==resident.uniqueId)
							{
								bcontinue=true;
								break;
							}
							
							if(to.owner8!=null && to.owner8.uniqueId==resident.uniqueId)
							{
								bcontinue=true;
								break;
							}							
						}
					}
					
					if(bcontinue)
						continue;
					
					listWorker.add(resident);
				}
			}
			
			if(listType==ListType.BED_OWNER)
				listWorker=address.listWorldObjects.stream().filter(item->!item.bIsDead && item.iZombie<1 && item.thehuman!=null && item.thehuman.bed==null).collect(Collectors.toList());				
			
			if(listType==ListType.CAR_OWNER)
			{
				if(address==null) //car wurde nicht auf address geplaced
					listWorker=town.gameWorld.worldHumans.stream().filter(item->!item.bIsDead && item.iZombie<1 && item.thehuman!=null && item.thehuman.car==null && item.thehuman.getAge()>=16 && item.thehuman.getEducationValue()>=mobj.theobject.getRequiredWorkplaceEducation()).collect(Collectors.toList());
				else
					listWorker=address.listWorldObjects.stream().filter(item->!item.bIsDead && item.iZombie<1 && item.thehuman!=null && item.thehuman.car==null && item.thehuman.getAge()>=16 && item.thehuman.getEducationValue()>=mobj.theobject.getRequiredWorkplaceEducation()).collect(Collectors.toList());
			}
			
			if(listType==ListType.WARDROBE_OWNER)
				listWorker=address.listWorldObjects.stream().filter(item->!item.bIsDead && item.iZombie<1 && item.thehuman!=null && item.thehuman.wardrobe==null).collect(Collectors.toList());				
			
			if(listType==ListType.NEWTOWN || listType==ListType.NEWTOWN2)
			{
				listText=new ArrayList<String>();
				//listText.add("Challenge");
				//listText.add("Normal");
				//listText.add("Easy");
				//listText.add("Very Easy");
				//listText.add("Custom Town");
				listText.add("Happy Town People");
				//iChosen=1;
				iChosen=0;
				bChosen=true;
				//listText.add("Design (no Zombies)");
			}
		}
	}
	
	public Boolean doubleClick(int x, int y, int libgdxy, int button)
	{
		//if(listType==ListType.VIEW_RESIDENTS)
		//	return false;
		
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
		showDlg(false);
	
		CWorldObject mobj = town.gameWorld.markerObject;
				
		if(bChosen)
		{
			CWorldObject chosen = null;
			
			if(listType==ListType.RESEARCH_PROJECT || listType==ListType.OBJECT_DESIGN)
			{
				CObject chosenItem = listItem.get((pageIndex-1)*pageSize+iChosen);
				town.gameWorld.markerObject.researchObject = chosenItem;
				
				if(buttonDistribute.toggleActive)
				{
					for(CWorldObject wobj : town.gameWorld.markerObject.theaddress.listWorldObjects)
					{
						if(wobj.theobject.editoraction.equals(town.gameWorld.markerObject.theobject.editoraction))
							wobj.researchObject=chosenItem;
					}
				}
			}
			else if(listType==ListType.NEWTOWN || listType==ListType.NEWTOWN2)
			{
				//town.gameWorld.bZombieApocalypse=buttonZombieApocalypse.toggleActive;
				//town.gameWorld.bConstructionMode=buttonConstruction.toggleActive;
				//Gdx.app.setLogLevel(5);
				//Gdx.app.debug("", "town.gameWorld.bZombieApocalypse: " + town.gameWorld.bZombieApocalypse);
				
				String chosenItem = listText.get((pageIndex-1)*pageSize+iChosen);
				chosenItem=chosenItem.trim().toLowerCase();
				chosenItem=chosenItem.replaceAll("\\s+","");
				
				//town.bConstructionMode=true;
				//town.bZombieApocalypse=true;
				//town.bNoRealEstate=true;
				
				//if(chosenItem.toLowerCase().contains("settlement"))
				{
					//town.bConstructionMode=false;
					//town.bZombieApocalypse=false;
					//town.newGame(true, "zombie");
				}
				//else
				
				if(buttonExpert.toggleActive)
				{
					town.newGame(true, "expert");
					return;
				}
				
				town.newGame(true, "htp");
				
				//town.newGame(true, "veryeasy");
			}
			else if(listType==ListType.HOMELESS)
			{
				if(chosenList.size()>0)
				{
					for(Integer i1 : chosenList)
					{
						chosen = listWorker.get((pageIndex-1)*pageSize + i1);
						chosen.theaddressid=address.addressId;
						chosen.theaddress=address;
						address.addWorldObject(chosen);
					}
				}
			}
			else
			{
				chosen = listWorker.get((pageIndex-1)*pageSize + iChosen);
			}
			
			
			
			
			//-------------------------------------------------------------------
						
			
			if(listType==ListType.VIEW_RESIDENTS)
			{
				//move camers, set camera, goto resident
				town.gameCam.position.set(chosen.pos_x(), chosen.pos_y(), 0);
				town.gameWorld.bRenderFrameBuffer=true;
			}
						
			if(listType==ListType.BED_OWNER)
			{
				if(typenr==2)
				{
					if(mobj.owner2!=null)
						mobj.owner2.thehuman.bed=null;
					
					town.gameWorld.markerObject.owner2=chosen;
				}
				else
				{
					if(mobj.owner!=null)
						mobj.owner.thehuman.bed=null;
					
					town.gameWorld.markerObject.owner=chosen;
				}
				
				chosen.thehuman.bed=mobj;
			}
			
			if(listType==ListType.CAR_OWNER)
			{
				if(mobj.owner!=null)
					mobj.owner.thehuman.car=null;
				
				town.gameWorld.markerObject.owner=chosen;
				
				chosen.thehuman.car=mobj;
			}
					
			if(listType==ListType.WARDROBE_OWNER)
			{
				if(mobj.owner!=null)
					mobj.owner.thehuman.wardrobe=null;
				
				town.gameWorld.markerObject.owner=chosen;
				
				chosen.thehuman.wardrobe=mobj;
			}
			
			if(listType==ListType.DINING_SEATOWNER)
			{
				//nicht aus liste entfernen wenn seat owner noch als cook hinterlegt ist
				Boolean remove=true;
				
				if(mobj.worker!=null && mobj.worker.uniqueId==chosen.uniqueId)
						remove=false;
				
				chosen.thehuman.taskobjects.put(town.gameWorld.markerObject.uniqueId, town.gameWorld.markerObject);
				
				//remove dinner table from old owner task list and set new owner
				if(typenr==1)
				{
					if(remove &&  mobj.owner!=null)
						mobj.owner.thehuman.taskobjects.remove(mobj.uniqueId);
					mobj.owner=chosen;
				}
				if(typenr==2)
				{
					if(remove &&  mobj.owner2!=null)
						mobj.owner2.thehuman.taskobjects.remove(mobj.uniqueId);
					mobj.owner2=chosen;
				}
				if(typenr==3)
				{
					if(remove &&  mobj.owner3!=null)
						mobj.owner3.thehuman.taskobjects.remove(mobj.uniqueId);
					mobj.owner3=chosen;
				}
				if(typenr==4)
				{
					if(remove &&  mobj.owner4!=null)
						mobj.owner4.thehuman.taskobjects.remove(mobj.uniqueId);
					mobj.owner4=chosen;
				}
				if(typenr==5)
				{
					if(remove &&  mobj.owner5!=null)
						mobj.owner5.thehuman.taskobjects.remove(mobj.uniqueId);
					mobj.owner5=chosen;
				}
				if(typenr==6)
				{
					if(remove &&  mobj.owner6!=null)
						mobj.owner6.thehuman.taskobjects.remove(mobj.uniqueId);
					mobj.owner6=chosen;
				}
				
				if(typenr==7)
				{
					if(remove &&  mobj.owner7!=null)
						mobj.owner7.thehuman.taskobjects.remove(mobj.uniqueId);
					mobj.owner7=chosen;
				}

				if(typenr==8)
				{
					if(remove &&  mobj.owner8!=null)
						mobj.owner8.thehuman.taskobjects.remove(mobj.uniqueId);
					mobj.owner8=chosen;
				}
			}
			
			if(listType==ListType.DINNER_COOK)
			{
				//nicht entfernen wenn cook noch als seat owner hinterlegt ist
				Boolean remove=true;
				if(mobj.owner!=null && mobj.owner.uniqueId==chosen.uniqueId)
					remove=false;
				if(mobj.owner2!=null && mobj.owner2.uniqueId==chosen.uniqueId)
					remove=false;
				if(mobj.owner3!=null && mobj.owner3.uniqueId==chosen.uniqueId)
					remove=false;
				if(mobj.owner4!=null && mobj.owner4.uniqueId==chosen.uniqueId)
					remove=false;
				if(mobj.owner5!=null && mobj.owner5.uniqueId==chosen.uniqueId)
					remove=false;
				if(mobj.owner6!=null && mobj.owner6.uniqueId==chosen.uniqueId)
					remove=false;
				if(mobj.owner7!=null && mobj.owner7.uniqueId==chosen.uniqueId)
					remove=false;
				if(mobj.owner8!=null && mobj.owner8.uniqueId==chosen.uniqueId)
					remove=false;
				
				if(remove)
				{
					if(mobj.worker!=null)
						mobj.worker.thehuman.taskobjects.remove(mobj.uniqueId);
				}
				
				chosen.thehuman.taskobjects.put(town.gameWorld.markerObject.uniqueId, town.gameWorld.markerObject);
				town.gameWorld.markerObject.worker = chosen;
			}
			
			if(listType==ListType.SCHOOL_STUDENT)
			{
				CWorldObject workplace = town.gameWorld.markerObject;
				town.gameWorld.linkWorkerAndWorkplace(chosen, workplace, typenr);
			}
			
			if(listType==ListType.COLLEGE_STUDENT)
			{
				CWorldObject workplace = town.gameWorld.markerObject;
				town.gameWorld.linkWorkerAndWorkplace(chosen, workplace, typenr);
			}
			
			if(listType==ListType.WORKER)
			{
				CWorldObject workplace = town.gameWorld.markerObject;
				int nr=typenr;
				if(nr<1)
					nr=1;
				
				town.gameWorld.linkWorkerAndWorkplace(chosen, workplace, nr);
			}


			
		}
	}
	
	public Boolean checkListClick(int x, int libgdxy)
	{
		//if(listType==ListType.VIEW_RESIDENTS)
		//	return false;
						
		int listcount = 0;
		
		if(listType == ListType.RESEARCH_PROJECT || listType==ListType.OBJECT_DESIGN)
			listcount = listItem.size();
		else if(listType==ListType.NEWTOWN || listType==ListType.NEWTOWN2)
			listcount = listText.size();
		else
			listcount = listWorker.size();
		
		if(listcount > pageSize)
			listcount = pageSize;
		
		int count=0;
		for(int i=1;i<=listcount;i++)
		{
			int j=i-1;
			if(j>activePageRowCount)
				break;
			

			
			if(listType==ListType.NEWTOWN || listType==ListType.NEWTOWN2)
			{
				if(x>eintragX && x<eintragX-5+dialogW-50 && libgdxy>=eintragYStart-i*(eintragH+13)-eintragH && libgdxy<=eintragYStart-i*(eintragH+13))
				{
					bChosen=true;
					iChosen=count;
					
					return true;
				}
			}
			else if(listType==ListType.HOMELESS)
			{
				if(x>eintragX && x<eintragX-5+dialogW-5 && libgdxy>=eintragYStart-i*eintragH-eintragH && libgdxy<=eintragYStart-i*eintragH)
				{
					bChosen=true;
					iChosen=count;
					
					if(chosenList.contains(iChosen))
						chosenList.removeIf(item->item==iChosen);
					else
						chosenList.add(iChosen);
					
					iChosen=-1;
					
					return true;
				}
			}			
			else
			{
				if(x>eintragX && x<eintragX-5+dialogW-5 && libgdxy>=eintragYStart-i*eintragH-eintragH && libgdxy<=eintragYStart-i*eintragH)
				{
					bChosen=true;
					iChosen=count;
					
					return true;
				}
			}
			
			count++;
		}
		
		return false;
	}
	
	public Boolean buttonDown(int x, int y, int libgdxy, int button)
	{
		CWorldObject mobj = town.gameWorld.markerObject;
		
		if(button==0 || button==-99)
		{
			if(listType!=ListType.NEWTOWN)
			{
				if(buttonPagingLeft.buttonClick(x, libgdxy))
				{
					if(pageIndex>1)
					{
						bChosen=false;
						iChosen=-1;
						chosenList.clear();
						
						pageIndex--;
					}
	
					return true;
				}
				
				if(buttonPagingRight.buttonClick(x, libgdxy))
				{
					int size1=0;
					if(listType==ListType.RESEARCH_PROJECT  || listType==ListType.OBJECT_DESIGN)
						size1=listItem.size();
					else
						size1=listWorker.size();
					
					if(pageIndex < Math.ceil(((double)size1)/pageSize))
					{
						bChosen=false;
						iChosen=-1;
						chosenList.clear();
						pageIndex++;
					}
	
					return true;
				}
			}
			if(listType==ListType.VIEW_RESIDENTS)
			{
				if(buttonHomeless.buttonClick(x, libgdxy))
				{
					showDlg(false);
					showDlg(true, address, ListType.HOMELESS, 0);
					buttonOK.labelText="move in";
					buttonOK.textX=25;
					
					return true;
				}
				
				if(buttonSaveRealEstate.buttonClick(x, libgdxy))
				{
					showDlg(false);
					town.gameGui.textInputDlg.address=address;
					town.gameGui.textInputDlg.showDlg(true, TextInputType.SAVEREALESTATE);
					
					//town.gameWorld.saveRealEstate(address);
					//showDlg(true, address, ListType.HOMELESS, 0);
					
					return true;
				}				
			}
			
			if(buttonOK.buttonClick(x, libgdxy) || button==-99)
			{
				if(listType==ListType.VIEW_RESIDENTS)
				{
					showDlg(false);
					return true;
				}
				
				create();
				return true;
			}
			
			if(buttonNew.buttonClick(x, libgdxy))
			{
				if(listType==ListType.WORKER)
				{
					showDlg(false);
					town.gameGui.createResidentDlg.skillObject = new SkillObject(mobj.theobject.objectId, mobj.theobject.getCompanyWorkingPlaceJobTitle(0), mobj.theobject);
					town.gameGui.createResidentDlg.showDlg(true);
					town.gameGui.createResidentDlg.workplace=mobj;
					return true;
				}
				
				return true;
			}

			if(listType==ListType.RESEARCH_PROJECT || listType==ListType.OBJECT_DESIGN)
			{
				if(buttonDistribute.buttonClick(x, libgdxy))
					return true;
			}
			
			if(listType==ListType.NEWTOWN || listType==ListType.NEWTOWN2)
			{
				if(buttonExpert.buttonClick(x, libgdxy))
				{
					return true;
				}
				
				/*
				if(buttonZombieApocalypse.buttonClick(x, libgdxy))
					return true;
								
				if(buttonRealEstate.buttonClick(x, libgdxy))
					return true;		
				
				if(buttonConstruction.buttonClick(x, libgdxy))
					return true;
					*/
					
			}

			if(listType==ListType.NEWTOWN2)
			{
				if(buttonLoad.buttonClick(x, libgdxy))
				{
					showDlg(false);
					town.gameGui.optionListDlg.showDlg(true, OptionListType.LoadTown);
					town.gameGui.level2_objtypeid="";
					return true;
				}
			}
			
			if(listType!=ListType.NEWTOWN2)
			{
				if(buttonCancel.buttonClick(x, libgdxy))
				{
					showDlg(false);
					return true;
				}
			}
			
			if(listWorker!=null || listText!=null)
			{
				if(listType==ListType.VIEW_RESIDENTS)
					return false;
				
				checkListClick(x,libgdxy);
			}
		}
		else
		{
			if(listType!=ListType.NEWTOWN2)
				showDlg(false);
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
	
	public String getSickText(CWorldObject obj)
	{
		String sname1="";
		
		if(obj.thehuman.sick>0)
		{
			if(obj.thehuman.sickType==0)
				sname1+=" (Sick " + Math.round(obj.thehuman.sick) + "%)";

			if(obj.thehuman.sickType==1)
				sname1+=" (Severe Disease " + Math.round(obj.thehuman.sick) + "%)";

			if(obj.thehuman.sickType==2)
				sname1+=" (Contagious Disease " + Math.round(obj.thehuman.sick) + "%)";
		}
		else if(!obj.thehuman.canWork())
		{
			sname1+=" (Bad Health)";
		}

		return sname1;
	}
	
	public void render(int x, int libgdxy)
	{
		try
		{
		
		int istart=0;
		int ilast=0;
		int isize=0;
		
		if(listType==ListType.RESEARCH_PROJECT || listType==ListType.OBJECT_DESIGN)
		{
			isize=listItem.size();
			ilast=listItem.size()-1;
		}
		else if(listType==ListType.NEWTOWN || listType==ListType.NEWTOWN2)
		{
			isize=listText.size();
			ilast=listText.size()-1;
		}
		else
		{
			isize=listWorker.size();
			ilast=listWorker.size()-1;
		}
		
		if(isize>pageSize)
		{
			istart=pageSize*(pageIndex-1);
			
			if(pageIndex==1)
				istart=0;
			
			ilast=istart+pageSize-1;
			
			int maxPageIndex = (int)(Math.ceil(((double)isize)/pageSize)); 
			
			if(pageIndex==maxPageIndex)
			{
				ilast=(istart+isize%pageSize)-1;
			}
		}
		
		activePageRowCount=ilast-istart;

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
			
			
			//*********************
			//Mouseover GridTable
			//*********************
			int overX=0;
			int overY=0;
			int overChosen=0;
			
			if(listWorker!=null || listItem!=null || listText!=null)
			{
				int listcount = isize; //listWorker.size();
				
				if(listcount>pageSize)
					listcount=pageSize;
				
				dlgShapeRenderer.set(ShapeType.Line);
				dlgShapeRenderer.setColor(0.7f, 0.7f, 0.7f, 0.8f);
				
				if(listType==ListType.NEWTOWN || listType==ListType.NEWTOWN2)
				{
					dlgShapeRenderer.setColor(0.7f, 0.7f, 0.7f, 0.5f);
					dlgShapeRenderer.rect(eintragX-5, eintragYStart+5-eintragH+10+2, dialogW-50, eintragH+7); //header row
				}
				else
					dlgShapeRenderer.rect(eintragX-5, eintragYStart+5-eintragH+10, dialogW-50, eintragH); //header row
				
				dlgShapeRenderer.setColor(town.dialogRahmenColor);
				dlgShapeRenderer.line(dlgX+20, dlgY+50, dlgX+dialogW-20, dlgY+50); //trennlinie zu ok und cancel
				
				int icount=1;
				for(int i=1;i<=listcount;i++)
				{
					int j=i-1;
					
					if(j>activePageRowCount)
						break;
					
					if(listType==ListType.NEWTOWN || listType==ListType.NEWTOWN2)
					{
						if(x>eintragX && x<eintragX-5+dialogW-50 && libgdxy>=eintragYStart-i*(eintragH+13)-eintragH && libgdxy<=eintragYStart-i*(eintragH+13))
						{
							overChosen=icount;
							overX=x;
							overY=eintragYStart-i*eintragH-eintragH;
							dlgShapeRenderer.setColor(0.7f, 0.7f, 0.7f, 0.8f);
							dlgShapeRenderer.rect(eintragX-4, eintragYStart+5-i*(eintragH+13)-eintragH, dialogW-51, eintragH+1);
							break;
						}
					}
					else
					{
						if(x>eintragX && x<eintragX-5+dialogW-50 && libgdxy>=eintragYStart-i*eintragH-eintragH && libgdxy<=eintragYStart-i*eintragH)
						{
							overChosen=icount;
							overX=x;
							overY=eintragYStart-i*eintragH-eintragH;
							dlgShapeRenderer.setColor(0.7f, 0.7f, 0.7f, 0.8f);
							dlgShapeRenderer.rect(eintragX-5, eintragYStart+5-i*eintragH-eintragH, dialogW-50, eintragH);
							break;
						}
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
			
			if(listType!=ListType.HOMELESS)
			{
				if(isize > pageSize)
				{
					String sPageIndex = String.format("%02d", pageIndex);
					String sPageCount = String.format("%02d", (int)(Math.ceil(((double)isize)/((double)pageSize))));
					dlgFont.draw(dlgSpriteBatch, sPageIndex + "/" + sPageCount, buttonPagingLeft.controlX+buttonPagingLeft.controlW + 4f, buttonPagingLeft.controlY+buttonPagingLeft.controlH/2+6);
				}
			}
			
			if(listType==ListType.RESEARCH_PROJECT || listType==ListType.OBJECT_DESIGN)
			{
				if(listType==ListType.RESEARCH_PROJECT)
				{
					dlgFont.draw(dlgSpriteBatch, "Research Project", eintragX, eintragYStart+10);
					dlgFont.draw(dlgSpriteBatch, "Research Progress", x_spalte2, eintragYStart+10);
					dlgFont.draw(dlgSpriteBatch, "Researchers", x_spalte4, eintragYStart+10);
				}
				else if(listType==ListType.OBJECT_DESIGN)
				{
					dlgFont.draw(dlgSpriteBatch, "Design Project", eintragX, eintragYStart+10);
					dlgFont.draw(dlgSpriteBatch, "Design Progress", x_spalte2, eintragYStart+10);
					dlgFont.draw(dlgSpriteBatch, "Artists", x_spalte4, eintragYStart+10);
				}
				
				for(int i=istart;i<=ilast;i++)
				{
					CObject obj = listItem.get(i);
					
					//Icon
					int imageSize=15;
					int iconx=eintragX;
					int icony=eintragYStart-count*eintragH-eintragH-13;
			    	float dim[] = obj.getDimensionsByBase(iconx, icony, imageSize, imageSize);
			    	dlgSpriteBatch.setColor(1,1,1,0.8f);
			    	TextureRegion texture1 = obj.getBaseTextureRegion();
		        	
			    	if(obj.textureIcon!=null)
			    		dlgSpriteBatch.draw(obj.textureIcon, dim[0], dim[1], dim[2], dim[3]);
		        	else if(texture1!=null)
			    		dlgSpriteBatch.draw(texture1, dim[0], dim[1], dim[2], dim[3]);
			    	else
			    		dlgSpriteBatch.draw(obj.textureImage, dim[0], dim[1], dim[2], dim[3]);
			    	
					dlgSpriteBatch.setColor(1,1,1,1);
					
					//Name
					String sname = town.gameFont.shortenStringToWidth(dlgFont, obj.objectName, 300);
					dlgFont.draw(dlgSpriteBatch, sname, eintragX+50, eintragYStart-count*eintragH-eintragH);
					
					//Progress
					int progressx=0;
					if(listType==ListType.OBJECT_DESIGN)
						progressx=16;
					dlgFont.draw(dlgSpriteBatch, obj.iResearchCurrentWorkoutput+"/"+obj.iResearchTargetWorkoutput, x_spalte2+2-progressx, eintragYStart-count*eintragH-eintragH,120,0,false);
					
					//Nr of Researchers
					int nr=0;
					for(CCompany comp : town.gameWorld.worldCompanyList)
					{
						if(comp.companyType==CompanyType.COLLEGE && listType==ListType.RESEARCH_PROJECT)
						{
							for(CWorldObject cobj : comp.address_company.listWorldObjects)
							{
								if(cobj.theobject.editoraction.contains("company_college_workingplace_researchlab"))
								{
									if(cobj.researchObject!=null && cobj.researchObject.objectId.equals(obj.objectId))
									{
										nr++;
									}
								}
							}
						}
						
						if(comp.companyType==CompanyType.OBJECT_DESIGN && listType==ListType.OBJECT_DESIGN)
						{
							for(CWorldObject cobj : comp.address_company.listWorldObjects)
							{
								if(cobj.theobject.editoraction.contains("company_objectdesign_officeworkingplace_artist"))
								{
									if(cobj.researchObject!=null && cobj.researchObject.objectId.equals(obj.objectId))
									{
										nr++;
									}
								}
							}
						}
					}
					
					int countx=0;
					if(listType==ListType.OBJECT_DESIGN)
						countx=41;
					
					dlgFont.draw(dlgSpriteBatch, "" + nr, x_spalte4-18-countx, eintragYStart-count*eintragH-eintragH,100,0,false);
					
					count++;
				}				
			}
			
			if(listType==ListType.NEWTOWN || listType==ListType.NEWTOWN2)
			{
				f1.getData().setScale(0.58f);
				f1.setColor(1,1,1,1);
				f1.draw(dlgSpriteBatch, "New", eintragX, eintragYStart+18);
				dlgSpriteBatch.setShader(null);
				
				for(int i=istart;i<=ilast;i++)
				{
					String sobj = listText.get(i);
					town.gameFont.layout.setText(f1, sobj);
					int posx1=(int) (dlgX+dialogW/2-town.gameFont.layout.width/2+5);
					
					if(sobj.toLowerCase().contains("easy"))
						posx1-=4;
					if(sobj.toLowerCase().contains("very easy"))
						posx1+=3;
					if(sobj.toLowerCase().contains("normal"))
						posx1-=3;
					if(sobj.toLowerCase().contains("design"))
						posx1+=8;
					
					dlgFont.draw(dlgSpriteBatch, sobj, posx1, eintragYStart-12-count*(eintragH+13)-eintragH);
					count++;
				}
			}
			
			if(listType==ListType.SCHOOL_STUDENT || listType==ListType.COLLEGE_STUDENT 
					|| listType==ListType.BED_OWNER || listType==ListType.CAR_OWNER
					|| listType==ListType.WARDROBE_OWNER || listType==ListType.VIEW_RESIDENTS || listType==ListType.HOMELESS 
					)
			{
				dlgFont.draw(dlgSpriteBatch, "Name", eintragX, eintragYStart+10);
				dlgFont.draw(dlgSpriteBatch, "Jobs/Skills", x_spalte2, eintragYStart+10);
				dlgFont.draw(dlgSpriteBatch, "Age", x_spalte3, eintragYStart+10);
				dlgFont.draw(dlgSpriteBatch, "Education", x_spalte4-45, eintragYStart+10);
				dlgFont.draw(dlgSpriteBatch, "Intelligence", x_spalte5-50, eintragYStart+10);
				
				for(int i=istart;i<=ilast;i++)
				{
					CWorldObject obj = listWorker.get(i);
					String sname1 = obj.thehuman.getName();
					
					sname1+=getSickText(obj);
					
					//sname1+=" (Sick: " + Math.round(obj.thehuman.sick) + "%)";
					String sname = town.gameFont.shortenStringToWidth(dlgFont, sname1, 300);
					dlgFont.draw(dlgSpriteBatch, sname, eintragX, eintragYStart-count*eintragH-eintragH);
					int jtcount = obj.thehuman.workplaces.size()+obj.thehuman.taskobjects.size();
					int skillcount = obj.thehuman.jobSkillLevel.size();
					dlgFont.draw(dlgSpriteBatch, jtcount+"/"+skillcount, x_spalte2-10, eintragYStart-count*eintragH-eintragH,132,1,false);
					dlgFont.draw(dlgSpriteBatch, obj.thehuman.getAge()+"", x_spalte3-1, eintragYStart-count*eintragH-eintragH,25,0,false);
					dlgFont.draw(dlgSpriteBatch, (int)obj.thehuman.getIntelligenceValue()+"", x_spalte5-6-50+1, eintragYStart-count*eintragH-eintragH,75,0,false);
					dlgSpriteBatch.end();
					pointsControl_education.setValue(obj.thehuman.getEducationValue());
					pointsControl_education.setPosition(x_spalte4+27-45+1, eintragYStart-count*eintragH-eintragH-12);
					pointsControl_education.render(0,0);
					dlgSpriteBatch.begin();
					count++;
				}
			}
			
			
			if(listType==ListType.WORKER || listType==ListType.DINNER_COOK || 
					listType==ListType.DINING_SEATOWNER)
			{
				dlgFont.draw(dlgSpriteBatch, "Name", eintragX, eintragYStart+10);
				dlgFont.draw(dlgSpriteBatch, "Jobs/Skills", x_spalte2, eintragYStart+10);
				dlgFont.draw(dlgSpriteBatch, "Work Output", x_spalte3, eintragYStart+10);
				dlgFont.draw(dlgSpriteBatch, "Education", x_spalte4, eintragYStart+10);
				dlgFont.draw(dlgSpriteBatch, "Intelligence", x_spalte5, eintragYStart+10);
				
				for(int i=istart;i<=ilast;i++)
				{
					CWorldObject obj = listWorker.get(i);
					String sname1 = obj.thehuman.getName();
					//if(obj.thehuman.sick>0)
					//	sname1+=" (Illness: " + Math.round(obj.thehuman.sick) + "%)";
					sname1+=getSickText(obj);
					
					String sname = town.gameFont.shortenStringToWidth(dlgFont, sname1, 300);
					dlgFont.draw(dlgSpriteBatch, sname, eintragX, eintragYStart-count*eintragH-eintragH);
					int jtcount = obj.thehuman.workplaces.size()+obj.thehuman.taskobjects.size();
					int skillcount = obj.thehuman.jobSkillLevel.size();
					dlgFont.draw(dlgSpriteBatch, jtcount+"/"+skillcount, x_spalte2-10, eintragYStart-count*eintragH-eintragH,132,1,false);
					
					String wstext="";
					int wo1=obj.thehuman.getWorkOutputPerHour(false, null, null, false);
					int wo2=obj.thehuman.getWorkOutputPerHour(true, town.gameWorld.markerObject, null, true);
					//int wo2=obj.thehuman.getWorkOutputPerHour(true, null, true);
					
					if(wo1!=wo2)
						wstext = wo2+"/"+wo1;
					else
						wstext = wo1+"";
					
					dlgFont.draw(dlgSpriteBatch, wstext, x_spalte3+7, eintragYStart-count*eintragH-eintragH,75,0,false);
					dlgFont.draw(dlgSpriteBatch, (int)obj.thehuman.getIntelligenceValue()+"", x_spalte5-5, eintragYStart-count*eintragH-eintragH,75,0,false);
					dlgSpriteBatch.end();
					pointsControl_education.setValue(obj.thehuman.getEducationValue());
					pointsControl_education.setPosition(x_spalte4+29, eintragYStart-count*eintragH-eintragH-12);
					pointsControl_education.render(0,0);
					dlgSpriteBatch.begin();
					count++;
				}
			}
						
			//Draw Chosen Entry Check
			if(bChosen)
			{
				if(listType==ListType.NEWTOWN || listType==ListType.NEWTOWN2)
				{
					dlgSpriteBatch.end();
					//if(!Gdx.gl.glIsEnabled(GL30.GL_BLEND))
					{
						Gdx.gl.glEnable(GL30.GL_BLEND);
						Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
					}
					dlgShapeRenderer.begin();
					dlgShapeRenderer.set(ShapeType.Filled);
					dlgShapeRenderer.setColor(1,1,1,0.15f);
					dlgShapeRenderer.rect(eintragX-5, eintragYStart-(iChosen+1)*(eintragH+13)-eintragH/1.5f-3, dialogW-50, eintragH+2);
					dlgShapeRenderer.end();
					dlgSpriteBatch.begin();
					dlgSpriteBatch.draw(control_hakenok.textureImage, eintragX, eintragYStart-(iChosen+1)*(eintragH+13)-eintragH/1.5f, 15, 15);
				}
				else
				{
					if(listType==ListType.HOMELESS)
					{
						if(chosenList.size()>0)
						{
							for(int i1 : chosenList)
								dlgSpriteBatch.draw(control_hakenok.textureImage, eintragX-25, eintragYStart-(i1+1)*eintragH-eintragH/1.5f, 15, 15);
						}
					}
					else
						dlgSpriteBatch.draw(control_hakenok.textureImage, eintragX-25, eintragYStart-(iChosen+1)*eintragH-eintragH/1.5f, 15, 15);
				}
			}
						
			dlgSpriteBatch.end();
						
			buttonOK.render(x, libgdxy);
			
			if(listType==ListType.WORKER)
				buttonNew.render(x, libgdxy);

			if(listType==ListType.RESEARCH_PROJECT || listType==ListType.OBJECT_DESIGN)
				buttonDistribute.render(x, libgdxy);
			
			if(listType!=ListType.VIEW_RESIDENTS && listType!=ListType.NEWTOWN2)
				buttonCancel.render(x, libgdxy);

			if(listType==ListType.VIEW_RESIDENTS)
			{
				if(address.getCompany()==null)
					buttonHomeless.render(x, libgdxy);
				
				buttonSaveRealEstate.render(x, libgdxy);
			}
			
			if(listType==ListType.NEWTOWN || listType==ListType.NEWTOWN2)
			{
				buttonExpert.render(x, libgdxy);
				
				//buttonZombieApocalypse.render(x, libgdxy);
				//buttonRealEstate.render(x, libgdxy);
				//buttonConstruction.render(x, libgdxy);
			}
			
			if(listType==ListType.NEWTOWN2)
			{
				buttonLoad.render(x, libgdxy);	
			}
			
			if(listType!=ListType.HOMELESS)
			{
				if(listWorker.size()>pageSize || listItem.size()>pageSize)
				{
					buttonPagingLeft.render(x, libgdxy);
					buttonPagingRight.render(x, libgdxy);
				}
			}
			dlgSpriteBatch.begin();
			
			
			//Show Research Object Tooltip Information
			int index1 = ((pageIndex-1)*pageSize + overChosen-1);
			if((listType==ListType.RESEARCH_PROJECT || listType==ListType.OBJECT_DESIGN) && index1>=0)
			{
				int mx=x;
				int my=libgdxy;
				CObject colObject = listItem.get(index1);
				
				if(overX>0 && overY>0 && overChosen>0)
				{
					dlgSpriteBatch.setColor(1, 1, 1, 1f);
	        		TextureRegion texture1 = colObject.getBaseTextureRegion();
	        		
		        	//Big Object Texture
		        	float dim[] = colObject.getDimensionsForBuyMenu();
		        	float imagew = dim[0];
		        	float imageh = dim[1];
		        	
		        	tooltip.textLines.clear();
					tooltip.textLines.add(colObject.objectName);
					
					if(!colObject.ATTR_RESINFO.isEmpty())
						tooltip.textLines.add(colObject.ATTR_RESINFO);
					tooltip.textLines.addAll(colObject.getInfoTextBox());
					if(colObject.price>0)
						tooltip.textLines.add("Purchase Price: $"+colObject.price);
					
		        	int tooltipy = tooltip.textLines.size()*22;
					colObject.drawShadows(dlgSpriteBatch, 1, Math.round(mx-3), Math.round(my-imageh-50)-tooltipy, Math.round(imagew), Math.round(imageh),1);
					dlgSpriteBatch.setColor(1, 1, 1, 1f);
		        	
			    	if(colObject.textureIcon!=null)
			    		dlgSpriteBatch.draw(colObject.textureIcon, mx-3, my-imageh-50-tooltipy, imagew, imageh);
			    	else if(texture1!=null)
		        		dlgSpriteBatch.draw(texture1, mx-3, my-imageh-50-tooltipy, imagew, imageh);
		        	else
		        		dlgSpriteBatch.draw(colObject.textureImage, mx-3, my-imageh-50-tooltipy, imagew, imageh);
		        	
					dlgSpriteBatch.end();
					tooltip.draw_BuyMenu(mx+30, my+10, -1);
					tooltip.setColor(Color.WHITE);
					dlgSpriteBatch.begin();		
				}				
			}
			
			//Show Workplaces/Taskobjects Tooltip			
			if(listType==ListType.WORKER 
					|| listType==ListType.DINNER_COOK || listType==ListType.DINING_SEATOWNER
					|| listType==ListType.COLLEGE_STUDENT
					|| listType==ListType.SCHOOL_STUDENT || listType==ListType.BED_OWNER
					|| listType==ListType.CAR_OWNER || listType==ListType.WARDROBE_OWNER
					|| listType==ListType.VIEW_RESIDENTS || listType==ListType.HOMELESS
					|| listType==ListType.NEWTOWN || listType==ListType.NEWTOWN2
					)
			{
				if(overX>0 && overY>0 && overChosen>0)
				{
					CWorldObject chosen=null;
					dlgFont.setColor(town.dialogFontColorList);
					
					float theight=0;
					float twidth=0;
					
					if(listType==ListType.NEWTOWN || listType==ListType.NEWTOWN2)
					{
						overChosen--;
						
						if(overChosen>-1)
						{
							String chosenItem = listText.get(overChosen);
							tooltip.textLines.clear();
							
							if(chosenItem.toLowerCase().contains("custom town"))
							{
								tooltip.textLines.add("Build a Town");
							}
							
							if(chosenItem.toLowerCase().contains("settlement"))
							{
								tooltip.textLines.add("Build a Settlement");
							}
							
							if(chosenItem.toLowerCase().contains("community"))
							{
								tooltip.textLines.add("Build a Community");
							}

						}
					}
					else
					{
						chosen = listWorker.get((pageIndex-1)*pageSize + overChosen-1);
					
						String workplaces="";
						int ic=0;
						tooltip.textLines.clear();
						
						if(listType==ListType.VIEW_RESIDENTS)
						{
							tooltip.textLines.add("Double click to find resident");
							tooltip.textLines.add("");
						}
						
						if(chosen.thehuman.workplaces.size()>0 || chosen.thehuman.taskobjects.size()>0)
							tooltip.textLines.add("_Jobs/Tasks");
						
						for(CWorldObject wo : chosen.thehuman.workplaces.values())
						{
							float fskill=0;
							int objid = wo.theobject.getSkillObjectId();
							if(chosen.thehuman.jobSkillLevel.containsKey(objid))
								fskill = chosen.thehuman.jobSkillLevel.get(objid).fskill;
							
							String skilllevelname=chosen.thehuman.getJobSkillLevelName(objid);
							
							if(!wo.theobject.workplaceHasSkill())
								workplaces = wo.getCompanyWorkingPlaceJobTitle(0) + " " + wo.getWorkingHoursString() + "";
							else
								workplaces = wo.getCompanyWorkingPlaceJobTitle(0) + " " + wo.getWorkingHoursString() + ", " + Math.round(fskill) + " (" + skilllevelname + ")";
							
							tooltip.textLines.add(workplaces);
							ic++;
						}
						
						for(CWorldObject wo : chosen.thehuman.taskobjects.values())
						{
							int type=0;
							if(wo.worker!=null && wo.worker.uniqueId==chosen.uniqueId)
								type=1;
							
							int objid = wo.theobject.getSkillObjectId();
							
							workplaces=wo.getTaskText(type,1);
							
							//Show Skill-Level
							if(type==1 && wo.theobject.workplaceHasSkill())
							{
								float fskill=0;
								if(chosen.thehuman.jobSkillLevel.containsKey(objid))
									fskill = chosen.thehuman.jobSkillLevel.get(objid).fskill;
								
								String skilllevelname=chosen.thehuman.getJobSkillLevelName(objid);
								if(!skilllevelname.isEmpty())
									workplaces += ", " + Math.round(fskill) + " (" + skilllevelname + ")";
							}
							
							tooltip.textLines.add(workplaces);
							ic++;
						}
						
						if(workplaces.length()>0 && chosen.thehuman.jobSkillLevel.size()>0d)
							tooltip.textLines.add(" ");
						
						//Show Skills
						if(chosen.thehuman.jobSkillLevel.size()>0)
						{
							tooltip.textLines.add("_Skills");
							for(CJobSkillClass slk : chosen.thehuman.jobSkillLevel.values())
							{
								String skilllevelname=chosen.thehuman.getJobSkillLevelName(slk.theobject.getSkillObjectId());
								String skilltext = Math.round(slk.fskill)+"(" + skilllevelname + ")" + " " + slk.theobject.getCompanyWorkingPlaceJobTitle(0);
								tooltip.textLines.add(skilltext);
							}
						}
					}
					
					dlgSpriteBatch.end();
					
					if(tooltip.textLines.size()>0)
					{
						//if(listType==ListType.VIEW_RESIDENTS)
						//	tooltip.textLines.add("Double click to find resident");
						
						if(listType==ListType.NEWTOWN || listType==ListType.NEWTOWN2)
							tooltip.drawFormat(overX, overY-30, 1);
						else
							tooltip.drawFormat(overX, overY, 1);
					}
					
					dlgSpriteBatch.begin();					
				}
			}
			
			dlgSpriteBatch.end();
		}
		catch(Exception ex)
		{
			CHelper.writeError(ex.getMessage(), ex.getStackTrace(), ex);
		}
	}
}

