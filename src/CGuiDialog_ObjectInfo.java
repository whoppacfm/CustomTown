package com.mygdx.game;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.CAction.ActionMode;
import com.mygdx.game.CCompany.ArchitectWorkType;
import com.mygdx.game.CCompany.CompanyType;
import com.mygdx.game.CCompany.WorkoutputType;
import com.mygdx.game.CGuiDialog_ListStatistics.ListTypeStatistics;
import com.mygdx.game.CHelper.IntersectionMode;
import com.mygdx.game.CGuiDialog_List.ListType;
import com.mygdx.game.CHuman.CJobSkillClass;
import com.mygdx.game.CStatistics.CStatisticsData_Population;

public class CGuiDialog_ObjectInfo extends CGuiDialog {
	
	ShapeRenderer shapeRenderer;
	ShapeRenderer shapeRenderer_gui;
	
	BitmapFont dlgFont;
	Color dlgFontColor;
	
	private CTown town;
	private String swarnings;
	private CGuiTooltip tooltip;
	
	// Company Object
	private CObject buttonTextureEdit;
	private CGuiControl_Button buttonFill;
	private CGuiControl_Button buttonEdit_Worker;
	private CGuiControl_Button buttonPlus;
	private CGuiControl_Button buttonSave;
	private CGuiControl_Button buttonEdit_Worker2;
	private CGuiControl_Button buttonClear_Worker;
	private CGuiControl_Button buttonClear_Worker2;
	private CGuiControl_Button buttonEdit_Worktime;
	private CGuiControl_Button buttonMoveHouse;
	private CGuiControl_Button buttonEdit_ResearchProject;
	private CGuiControl_Button buttonEdit_Paint;
	private CGuiControl_Button buttonShow_Statistics;
	private CGuiControl_Button buttonShow_Statistics2;
	private CGuiControl_Button buttonAddressClone;
	private CGuiControl_Button buttonAddressPlanning;
	private CGuiControl_Button buttonAddressResize;
	private CGuiControl_Button buttonAddressMove;
	private CGuiControl_Button buttonEdit_Owner;
	private CGuiControl_Button buttonEdit_Owner2;
	private CGuiControl_Button buttonEdit_seatOwner1;
	private CGuiControl_Button buttonEdit_seatOwner2;
	private CGuiControl_Button buttonEdit_seatOwner3;
	private CGuiControl_Button buttonEdit_seatOwner4;
	private CGuiControl_Button buttonEdit_seatOwner5;
	private CGuiControl_Button buttonEdit_seatOwner6;
	private CGuiControl_Button buttonEdit_seatOwner7;
	private CGuiControl_Button buttonEdit_seatOwner8;
	private CGuiControl_Button buttonEdit_Cook;
	private CGuiControl_Button buttonClear_Cook;
	private CGuiControl_Button buttonEdit_DinnerTime1;
	private CGuiControl_Button buttonEdit_DinnerTime2;
	private CGuiControl_Button buttonEdit_DinnerTime3;
	private CGuiControl_Button buttonClear_DinnerTime1;
	private CGuiControl_Button buttonClear_DinnerTime2;
	private CGuiControl_Button buttonClear_DinnerTime3;
	private CGuiControl_Button buttonAccept;
	private CGuiControl_Button buttonRotation;
	
	ArrayList<CGuiControl_Button> listButtons;
	
	// Human
	CGuiControl_Points pointsControl_healthAttitude;
	CGuiControl_Points pointsControl_positiveAttitude;
	CGuiControl_Points pointsControl_education;
	
	private CObject icon_eat;
	private CObject icon_sleep;
	private CObject icon_clean;
	private CObject icon_toilet;
	private CObject icon_health;
	private CObject icon_happiness;
		
	private ArrayList<CGuiInfo> listGuiInfo_Human;
	private CGuiInfo guiinfo_workoutput;
	private CGuiInfo guiinfo_health;
	private CGuiInfo guiinfo_happiness;
	
	private CGuiInfo guiinfo_residentenergy;
	public CGuiInfo guiinfo_sleep;
	public CGuiInfo guiinfo_eat;
	public CGuiInfo guiinfo_clean;
	public CGuiInfo guiinfo_toilet;
	public CGuiInfo guiinfo_intelligence;
	public CGuiInfo guiinfo_fitness;
	public CGuiInfo guiinfo_education;
	public CGuiInfo guiinfo_clothing;
	//public CGuiInfo guiinfo_belief;
	private CGuiInfo guiinfo_Illuminati;
	
	
	private ArrayList<CGuiInfo> listGuiInfo_Company;
	private CGuiInfo guiinfo_CompanyFoodFillingByWorkerObject_food;
	private CGuiInfo guiinfo_food;
	private CGuiInfo guiinfo_grave;
	private CGuiInfo guiinfo_workplace;
	private CGuiInfo guiinfo_workoutput2;
	
	private CGuiInfo guiinfo_workoutput_finance;
	private CGuiInfo guiinfo_workoutput_population;
	private CGuiInfo guiinfo_workoutput_other;
	private CGuiInfo guiinfo_workoutput_intelligence;
	
	private CGuiInfo guiinfo_cup;
	private CGuiInfo guiinfo_coffeebean;
	
	private CGuiInfo guiinfo_students;
	private CGuiInfo guiinfo_teachers;
	private CGuiInfo guiinfo_energyoutput;
	private CGuiInfo guiinfo_wateroutput;
	private CGuiInfo guiinfo_condition;
	private CGuiInfo guiinfo_neededworkinput_workoutput2;
	private CGuiInfo guiinfo_energyconsumption;
	private CGuiInfo guiinfo_waterconsumption;
	private CGuiInfo guiinfo_workplace_education;
	private CGuiInfo guiinfo_workplace_education2;
	private CGuiInfo guiinfo_worker;
	private CGuiInfo guiinfo_experience;
	private CGuiInfo guiinfo_worker2;
	private CGuiInfo gui_worktime;
	private CGuiInfo guiinfo_CompanyFoodFillingByWorkerObject_education;
	private CGuiInfo guiinfo_CompanyFoodFillingByWorkerObject_worker;
	private CGuiInfo guiinfo_bookshelf_education;
	private CGuiInfo guiinfo_playground;
	private CGuiInfo guiinfo_researchproject;
	
	private CGuiInfo guiinfo_owner1;
	private CGuiInfo guiinfo_owner2;
	private CGuiInfo guiinfo_owner3;
	
	private CGuiInfo guiinfo_seatowner1;
	private CGuiInfo guiinfo_seatowner2;
	private CGuiInfo guiinfo_seatowner3;
	private CGuiInfo guiinfo_seatowner4;
	private CGuiInfo guiinfo_seatowner5;
	private CGuiInfo guiinfo_seatowner6;
	private CGuiInfo guiinfo_seatowner7;
	private CGuiInfo guiinfo_seatowner8;
	
	private CGuiInfo guiinfo_cook;
	private CGuiInfo guiinfo_dinnertime1;
	private CGuiInfo guiinfo_dinnertime2;
	private CGuiInfo guiinfo_dinnertime3;
	private CGuiInfo guiinfo_heatingpower;
	private CGuiInfo guiinfo_brightness;
	private CGuiInfo guiinfo_fuel1;
	
	private CGuiInfo guiinfo_damage;
	private CGuiInfo guiinfo_range;
	private CGuiInfo guiinfo_shotfrequency;
	private CGuiInfo guiinfo_shotspeed;
	
	
	int happiness = 0;
	int health = 0;
	String address = "";
	
	int eatPercent = 0;
	int sleepPercent = 0;
	int toiletPercent = 0;
	int cleanPercent = 0;
	int clothingPercent = 0;
	
	int y1 = 60;
	int y2 = 100;
	int y3 = 140;
	int y4 = 180;
	int y5 = 200;
	int y6 = 210;
	
	int addHumanDlgHeight;
	int addHumanDlgHeight2;
	
	public CGuiDialog_ObjectInfo(List<CObject> objectList, BitmapFont font, int dialogX, int dialogY, CTown town1) 
	{
		super("CGuiDialog_ObjectInfo");
		town = town1;
		
		dlgFontColor = new Color(1f, 1f, 1f, 0.8f);
		
		listGuiInfo_Human = new ArrayList<CGuiInfo>();
		listGuiInfo_Company = new ArrayList<CGuiInfo>();
		
		tooltip = new CGuiTooltip(town1);
		tooltip.setColor(Color.WHITE);
		
		dlgX = dialogX;
		dlgY = dialogY;
		
		dialogW = 690;
		dialogH = 250;
		
		dlgFont = font;
		dlgFont2 = town.gameFont.bfArial;
		
		createObjects(objectList);
		
		if(listButtons==null)
		{
			listButtons = new ArrayList<CGuiControl_Button>();
			listButtons.add(buttonPlus);
			listButtons.add(buttonSave);
			listButtons.add(buttonFill);
			listButtons.add(buttonEdit_Worker);
			listButtons.add(buttonEdit_Worker2);
			listButtons.add(buttonClear_Worker);
			listButtons.add(buttonClear_Worker2);
			listButtons.add(buttonEdit_Worktime);
			listButtons.add(buttonMoveHouse);
			listButtons.add(buttonEdit_ResearchProject);
			listButtons.add(buttonEdit_Paint);
			listButtons.add(buttonShow_Statistics);
			listButtons.add(buttonShow_Statistics2);
			listButtons.add(buttonAddressClone);
			listButtons.add(buttonAddressPlanning);
			listButtons.add(buttonAddressResize);
			listButtons.add(buttonAddressMove);
			listButtons.add(buttonEdit_Owner);
			listButtons.add(buttonEdit_Owner2);
			listButtons.add(buttonEdit_seatOwner1);
			listButtons.add(buttonEdit_seatOwner2);
			listButtons.add(buttonEdit_seatOwner3);
			listButtons.add(buttonEdit_seatOwner4);
			listButtons.add(buttonEdit_seatOwner5);
			listButtons.add(buttonEdit_seatOwner6);
			listButtons.add(buttonEdit_seatOwner7);
			listButtons.add(buttonEdit_seatOwner8);
			listButtons.add(buttonEdit_Cook);
			listButtons.add(buttonClear_Cook);
			listButtons.add(buttonEdit_DinnerTime1);
			listButtons.add(buttonEdit_DinnerTime2);
			listButtons.add(buttonEdit_DinnerTime3);
			listButtons.add(buttonClear_DinnerTime1);
			listButtons.add(buttonClear_DinnerTime2);
			listButtons.add(buttonClear_DinnerTime3);
			listButtons.add(buttonAccept);

			//listButtons.add(buttonRotation);
		}
	}
	
	public void initInfo() {
		
		if (town.gameWorld.markerObject==null)
			return;
		
		CWorldObject mobj = town.gameWorld.markerObject;
		
		dialogW = 610;
		dialogH = 250;
		
		if (town.gameWorld.markerObject.isHuman()) 
		{
			//dialogW = 700;
			//dialogW+=160;
			dialogW = 1000;
			addHumanDlgHeight=28; //macht skill/job mehr platz und verschiebt alles andere entsprechend
			addHumanDlgHeight2=26; //erhöht für zusätzliches attribute clothing cleanliness 
			dialogH = 250+40+addHumanDlgHeight+addHumanDlgHeight2;
		}
		
		if (mobj.isCompanyObject() || 
				mobj.showAsCompanyObject()) {
			dialogW=625;
		}
		
		//if (mobj.theobject.editoraction.contains("townhall_officeworkingplace_intelligence"))
		//	dialogH = 250+40;
			
		if (mobj.theobject.editoraction.contains("diningtable1_count8"))
			dialogH = 250+40;
		
		if (mobj.theobject.editoraction.contains("illuminati_defensesystem"))
			dialogH+=30;
		
		setMiddlePositionHorz();
		
		//buttonEdit_Worker.setTooltip("Choose " + town.gameWorld.markerObject.getWorkerTitle());
		//buttonClear_Worker.setTooltip("Remove " + town.gameWorld.markerObject.getWorkerTitle());
		buttonEdit_Worker.setTooltip("Choose Resident");
		buttonClear_Worker.setTooltip("Remove Resident");
		
		if (mobj.thehuman != null) 
		{
			happiness = (int) mobj.thehuman.getHappynessValue();
			health = (int) mobj.thehuman.getHealthValue();
			
			sleepPercent = mobj.thehuman.getSleepPercent();
			eatPercent = mobj.thehuman.getEatPercent();
			cleanPercent = mobj.thehuman.getCleanPercent();
			clothingPercent = mobj.thehuman.getClothingPercent();
			toiletPercent = mobj.thehuman.getToiletPercent();
		}
		
		if (mobj.theaddress != null)
			address = mobj.theaddress.addressName;
		else
			address = "";
		
		if (mobj.theobject.objectName.equals("Man")
				&& mobj.thehuman.getAge() < 18)
			mobj.theobject.objectName = "Boy";
		if (mobj.theobject.objectName.equals("Woman")
				&& mobj.thehuman.getAge() < 18)
			mobj.theobject.objectName = "Girl";
		
		
		
		// *********
		// GuiInfo
		// *********
		int posleft = 110+160+140; // balken und values
		int postop = 23; // balken und values
		int ipostopr = 10; // balken und values
		int icsize = 20;
		
		if (guiinfo_cup == null) {
			guiinfo_cup = new CGuiInfo(0, 0, town.gameResourceConfig.textures.get("guiinfo_cup"), "", "Cups", 20, 20, town);
			guiinfo_cup.setctrlSpriteBatch(town.gameGui.editorSpriteBatch);
		}
		
		if (guiinfo_coffeebean == null) {
			guiinfo_coffeebean = new CGuiInfo(0, 0, town.gameResourceConfig.textures.get("guiinfo_coffeebean"), "", "Coffee", 20, 20, town);
			guiinfo_coffeebean.setctrlSpriteBatch(town.gameGui.editorSpriteBatch);
		}
		if (guiinfo_owner1 == null) {
			guiinfo_owner1 = new CGuiInfo(0, 0,
					town.gameResourceConfig.textures.get("guiinfo_worker"), "",
					"Owner", 12, 21, town);
			guiinfo_owner1.setctrlSpriteBatch(town.gameGui.editorSpriteBatch);
		}
		if (guiinfo_owner2 == null) {
			guiinfo_owner2 = new CGuiInfo(0, 0,
					town.gameResourceConfig.textures.get("guiinfo_worker"), "",
					"Owner 2", 12, 21, town);
			guiinfo_owner2.setctrlSpriteBatch(town.gameGui.editorSpriteBatch);
		}
		if (guiinfo_owner3 == null) {
			guiinfo_owner3 = new CGuiInfo(0, 0,
					town.gameResourceConfig.textures.get("guiinfo_worker"), "",
					"Owner 3", 12, 21, town);
			guiinfo_owner3.setctrlSpriteBatch(town.gameGui.editorSpriteBatch);
		}
		
		// Seat Owner
		if (guiinfo_seatowner1 == null) {
			guiinfo_seatowner1 = new CGuiInfo(0, 0,
					town.gameResourceConfig.textures.get("guiinfo_worker"), "",
					"Seat Owner 1", 12, 21, town);
			guiinfo_seatowner1
					.setctrlSpriteBatch(town.gameGui.editorSpriteBatch);
		}
		if (guiinfo_seatowner2 == null) {
			guiinfo_seatowner2 = new CGuiInfo(0, 0,
					town.gameResourceConfig.textures.get("guiinfo_worker"), "",
					"Seat Owner 2", 12, 21, town);
			guiinfo_seatowner2
					.setctrlSpriteBatch(town.gameGui.editorSpriteBatch);
		}
		if (guiinfo_seatowner3 == null) {
			guiinfo_seatowner3 = new CGuiInfo(0, 0,
					town.gameResourceConfig.textures.get("guiinfo_worker"), "",
					"Seat Owner 3", 12, 21, town);
			guiinfo_seatowner3
					.setctrlSpriteBatch(town.gameGui.editorSpriteBatch);
		}
		if (guiinfo_seatowner4 == null) {
			guiinfo_seatowner4 = new CGuiInfo(0, 0,
					town.gameResourceConfig.textures.get("guiinfo_worker"), "",
					"Seat Owner 4", 12, 21, town);
			guiinfo_seatowner4
					.setctrlSpriteBatch(town.gameGui.editorSpriteBatch);
		}
		if (guiinfo_seatowner5 == null) {
			guiinfo_seatowner5 = new CGuiInfo(0, 0,
					town.gameResourceConfig.textures.get("guiinfo_worker"), "",
					"Seat Owner 5", 12, 21, town);
			guiinfo_seatowner5
					.setctrlSpriteBatch(town.gameGui.editorSpriteBatch);
		}
		if (guiinfo_seatowner6 == null) {
			guiinfo_seatowner6 = new CGuiInfo(0, 0,
					town.gameResourceConfig.textures.get("guiinfo_worker"), "",
					"Seat Owner 6", 12, 21, town);
			guiinfo_seatowner6
					.setctrlSpriteBatch(town.gameGui.editorSpriteBatch);
		}
		if (guiinfo_seatowner7 == null) {
			guiinfo_seatowner7 = new CGuiInfo(0, 0,
					town.gameResourceConfig.textures.get("guiinfo_worker"), "",
					"Seat Owner 7", 12, 21, town);
			guiinfo_seatowner7
					.setctrlSpriteBatch(town.gameGui.editorSpriteBatch);
		}
		if (guiinfo_seatowner8 == null) {
			guiinfo_seatowner8 = new CGuiInfo(0, 0,
					town.gameResourceConfig.textures.get("guiinfo_worker"), "",
					"Seat Owner 8", 12, 21, town);
			guiinfo_seatowner8
					.setctrlSpriteBatch(town.gameGui.editorSpriteBatch);
		}
		if (guiinfo_cook == null) {
			guiinfo_cook = new CGuiInfo(0, 0,
					town.gameResourceConfig.textures.get("guiinfo_cook"), "",
					"Cook", 20, 20, town);
			guiinfo_cook.setctrlSpriteBatch(town.gameGui.editorSpriteBatch);
		}
		if (guiinfo_dinnertime1 == null) {
			guiinfo_dinnertime1 = new CGuiInfo(0, 0,
					town.gameResourceConfig.textures.get("gui_worktime"), "",
					"Dinner Time", 20, 20, town);
			guiinfo_dinnertime1
					.setctrlSpriteBatch(town.gameGui.editorSpriteBatch);
		}
		if (guiinfo_dinnertime2 == null) {
			guiinfo_dinnertime2 = new CGuiInfo(0, 0,
					town.gameResourceConfig.textures.get("gui_worktime"), "",
					"Dinner Time 2", 20, 20, town);
			guiinfo_dinnertime2
					.setctrlSpriteBatch(town.gameGui.editorSpriteBatch);
		}
		if (guiinfo_dinnertime3 == null) {
			guiinfo_dinnertime3 = new CGuiInfo(0, 0,
					town.gameResourceConfig.textures.get("gui_worktime"), "",
					"Dinner Time 3", 20, 20, town);
			guiinfo_dinnertime3
					.setctrlSpriteBatch(town.gameGui.editorSpriteBatch);
		}

		if (guiinfo_heatingpower == null) {
			guiinfo_heatingpower = new CGuiInfo(0, 0,
					town.gameResourceConfig.textures
							.get("guiinfo_heatingpower"), "", "Heating Power",
					20, 20, town);
			guiinfo_heatingpower
					.setctrlSpriteBatch(town.gameGui.editorSpriteBatch);
		}

		if (guiinfo_fuel1 == null) {
			guiinfo_fuel1 = new CGuiInfo(0, 0,
					town.gameResourceConfig.textures.get("guiinfo_fuel1"), "",
					"Fuel", 27, 27, town);
			guiinfo_fuel1.setctrlSpriteBatch(town.gameGui.editorSpriteBatch);
		}

		if (guiinfo_brightness == null) {
			guiinfo_brightness = new CGuiInfo(0, 0,
					town.gameResourceConfig.textures.get("guiinfo_brightness"),
					"", "Power", 27, 27, town);
			guiinfo_brightness
					.setctrlSpriteBatch(town.gameGui.editorSpriteBatch);
		}

		if (guiinfo_bookshelf_education == null) {
			guiinfo_bookshelf_education = new CGuiInfo(0, 0,
					town.gameResourceConfig.textures.get("guiinfo_education"),
					"", "Required Education", icsize, icsize - 2, town);
			guiinfo_bookshelf_education
					.setctrlSpriteBatch(town.gameGui.editorSpriteBatch);
		}
		
		
		
		
		
		//Company Gui Info
		//-------------------------------------------------------------------------
		if (listGuiInfo_Company.isEmpty()) {

			if (guiinfo_shotfrequency == null) {
				guiinfo_shotfrequency = new CGuiInfo(0, 0, town.gameResourceConfig.textures.get("guiinfo_shotfrequency"), "", "Shot Frequency", 20, 20, town);
				guiinfo_shotfrequency.setctrlSpriteBatch(town.gameGui.editorSpriteBatch);
			}
			if (guiinfo_shotspeed == null) {
				guiinfo_shotspeed = new CGuiInfo(0, 0, town.gameResourceConfig.textures.get("guiinfo_shotspeed"), "", "Shot Speed", 20, 20, town);
				guiinfo_shotspeed.setctrlSpriteBatch(town.gameGui.editorSpriteBatch);
			}
			
			if (guiinfo_damage == null) {
				guiinfo_damage = new CGuiInfo(0, 0, town.gameResourceConfig.textures.get("guiinfo_damage"), "", "Damage", 20, 20, town);
				guiinfo_damage.setctrlSpriteBatch(town.gameGui.editorSpriteBatch);
			}
			if (guiinfo_range == null) {
				guiinfo_range = new CGuiInfo(0, 0, town.gameResourceConfig.textures.get("guiinfo_range"), "", "Range", 20, 20, town);
				guiinfo_range.setctrlSpriteBatch(town.gameGui.editorSpriteBatch);
			}
			
			guiinfo_Illuminati = new CGuiInfo(0, 0,
				town.gameResourceConfig.textures.get("guiinfo_illuminati"), "",
				"Illuminati Defense Systems", icsize, icsize, town);
			
			guiinfo_CompanyFoodFillingByWorkerObject_food = new CGuiInfo(0, 0,
					town.gameResourceConfig.textures.get("guiinfo_food"), "",
					"Food", icsize, icsize, town);
			guiinfo_food = new CGuiInfo(0, 0,
					town.gameResourceConfig.textures.get("guiinfo_food"), "",
					"Food", icsize, icsize, town);
			guiinfo_workplace = new CGuiInfo(0, 0,
					town.gameResourceConfig.textures.get("guiinfo_workplace"),
					"", "Workers / Workplaces and Tasks", icsize, icsize, town);
			guiinfo_students = new CGuiInfo(0, 0,
					town.gameResourceConfig.textures.get("guiinfo_students"),
					"", "Students / Student Desk Workplaces", icsize, icsize,
					town);
			guiinfo_teachers = new CGuiInfo(0, 0,
					town.gameResourceConfig.textures.get("guiinfo_teachers"),
					"", "Teachers / Teacher Desk Workplaces", icsize, icsize,
					town);
			guiinfo_grave = new CGuiInfo(0, 0,
					town.gameResourceConfig.textures.get("guiinfo_grave"),
					"", "Occupied Graves / All Graves", icsize, icsize,
					town);
			
			guiinfo_workoutput2 = new CGuiInfo(
					0,
					0,
					town.gameResourceConfig.textures.get("guiinfo_workoutput2"),
					"", "Company Office Work Output", icsize, icsize, town);
			
			guiinfo_workoutput_finance = new CGuiInfo(
					0,
					0,
					town.gameResourceConfig.textures.get("guiinfo_workoutput_finance"),
					"", "Company Office Work Output", "Finance Statistics", icsize, icsize, town);
			guiinfo_workoutput_population = new CGuiInfo(
					0,
					0,
					town.gameResourceConfig.textures.get("guiinfo_workoutput_population"),
					"", "Company Office Work Output", "Population Statistics", icsize, icsize, town);
			guiinfo_workoutput_other = new CGuiInfo(
					0,
					0,
					town.gameResourceConfig.textures.get("guiinfo_workoutput_other"),
					"", "Company Office Work Output", "Other Statistics", icsize, icsize, town);

			guiinfo_workoutput_intelligence = new CGuiInfo(
					0,
					0,
					town.gameResourceConfig.textures.get("guiinfo_townhallintelligence"),
					"", "Company Office Work Output", "Intelligence Desk", icsize, icsize, town);

			
			guiinfo_energyoutput = new CGuiInfo(0, 0,
					town.gameResourceConfig.textures
							.get("guiinfo_energyoutput"), "", "Energy Output",
					icsize, icsize, town);
			guiinfo_wateroutput = new CGuiInfo(
					0,
					0,
					town.gameResourceConfig.textures.get("guiinfo_wateroutput"),
					"", "Water Output", icsize, icsize, town);
			guiinfo_playground = new CGuiInfo(0, 0,
					town.gameResourceConfig.textures.get("guiinfo_playground"),
					"", "Playground Objects", icsize, icsize, town);
			guiinfo_condition = new CGuiInfo(0, 0,
					town.gameResourceConfig.textures.get("guiinfo_condition"),
					"", "Condition", icsize, icsize, town);
			guiinfo_neededworkinput_workoutput2 = new CGuiInfo(
					0, 0,
					town.gameResourceConfig.textures.get("guiinfo_workoutput2"),
					"", "Object Consumes Company Office Work Output", icsize, icsize, town);
					//"", "Office Workoutput Consumption", icsize, icsize, town);
			guiinfo_energyconsumption = new CGuiInfo(0, 0,
					town.gameResourceConfig.textures
							.get("guiinfo_energyconsumption"), "",
					"Energy Consumption", icsize, icsize, town);
			guiinfo_waterconsumption = new CGuiInfo(0, 0,
					town.gameResourceConfig.textures
							.get("guiinfo_waterconsumption"), "",
					"Water Consumption", icsize, icsize, town);
			guiinfo_workplace_education = new CGuiInfo(0, 0,
					town.gameResourceConfig.textures.get("guiinfo_education"),
					"", "Required Education", icsize, icsize, town);
			guiinfo_workplace_education2 = new CGuiInfo(0, 0,
					town.gameResourceConfig.textures.get("guiinfo_education"),
					"", "Recommended Education", icsize, icsize, town);
			guiinfo_worker = new CGuiInfo(0, 0,
					town.gameResourceConfig.textures.get("guiinfo_worker"), "",
					"Resident", 12, 21, town);
			guiinfo_worker2 = new CGuiInfo(0, 0,
					town.gameResourceConfig.textures.get("guiinfo_worker"), "",
					"Resident", 12, 21, town);
			guiinfo_researchproject = new CGuiInfo(0, 0,
					town.gameResourceConfig.textures
							.get("guiinfo_researchproject"), "",
					"Research Project", icsize, icsize, town);
			
			guiinfo_experience = new CGuiInfo(0, 0,
					town.gameResourceConfig.textures.get("guiinfo_workexp"),
					"", "Work Experience", 15, 15, town);
			gui_worktime = new CGuiInfo(0, 0,
					town.gameResourceConfig.textures.get("gui_worktime"), "",
					"Work Time", icsize, icsize, town);
			guiinfo_CompanyFoodFillingByWorkerObject_education = new CGuiInfo(
					0, 0,
					town.gameResourceConfig.textures.get("guiinfo_education"),
					"", "Required Education", icsize, icsize - 2, town);
			guiinfo_CompanyFoodFillingByWorkerObject_worker = new CGuiInfo(0,
					0, town.gameResourceConfig.textures.get("guiinfo_worker"),
					"", "Responsible Resident", 12, 21, town);
			
			
			
			SpriteBatch sb = town.gameGui.editorSpriteBatch;
			guiinfo_CompanyFoodFillingByWorkerObject_food.setctrlSpriteBatch(sb);
			
			guiinfo_shotspeed.setctrlSpriteBatch(sb);
			guiinfo_shotfrequency.setctrlSpriteBatch(sb);
			guiinfo_damage.setctrlSpriteBatch(sb);
			guiinfo_range.setctrlSpriteBatch(sb);
			guiinfo_Illuminati.setctrlSpriteBatch(sb);
			guiinfo_food.setctrlSpriteBatch(sb);
			guiinfo_workplace.setctrlSpriteBatch(sb);
			guiinfo_experience.setctrlSpriteBatch(sb);
			guiinfo_students.setctrlSpriteBatch(sb);
			guiinfo_teachers.setctrlSpriteBatch(sb);
			guiinfo_grave.setctrlSpriteBatch(sb);
			guiinfo_workoutput2.setctrlSpriteBatch(sb);
			guiinfo_workoutput_finance.setctrlSpriteBatch(sb);
			guiinfo_workoutput_population.setctrlSpriteBatch(sb);
			guiinfo_workoutput_other.setctrlSpriteBatch(sb);
			guiinfo_workoutput_intelligence.setctrlSpriteBatch(sb);
			guiinfo_energyoutput.setctrlSpriteBatch(sb);
			guiinfo_wateroutput.setctrlSpriteBatch(sb);
			guiinfo_condition.setctrlSpriteBatch(sb);
			guiinfo_neededworkinput_workoutput2.setctrlSpriteBatch(sb);
			guiinfo_energyconsumption.setctrlSpriteBatch(sb);
			guiinfo_waterconsumption.setctrlSpriteBatch(sb);
			guiinfo_workplace_education.setctrlSpriteBatch(sb);
			guiinfo_workplace_education2.setctrlSpriteBatch(sb);
			guiinfo_worker.setctrlSpriteBatch(sb);
			guiinfo_playground.setctrlSpriteBatch(sb);
			guiinfo_worker2.setctrlSpriteBatch(sb);
			gui_worktime.setctrlSpriteBatch(sb);
			guiinfo_CompanyFoodFillingByWorkerObject_education.setctrlSpriteBatch(sb);
			guiinfo_CompanyFoodFillingByWorkerObject_worker.setctrlSpriteBatch(sb);
			guiinfo_researchproject.setctrlSpriteBatch(sb);
			
			listGuiInfo_Company.add(guiinfo_shotfrequency);
			listGuiInfo_Company.add(guiinfo_shotspeed);
			
			listGuiInfo_Company.add(guiinfo_damage);
			listGuiInfo_Company.add(guiinfo_range);
			listGuiInfo_Company.add(guiinfo_CompanyFoodFillingByWorkerObject_food);
			listGuiInfo_Company.add(guiinfo_Illuminati);
			listGuiInfo_Company.add(guiinfo_food);
			listGuiInfo_Company.add(guiinfo_playground);
			listGuiInfo_Company.add(guiinfo_workplace);
			listGuiInfo_Company.add(guiinfo_experience);
			listGuiInfo_Company.add(guiinfo_students);
			listGuiInfo_Company.add(guiinfo_teachers);
			listGuiInfo_Company.add(guiinfo_grave);
			listGuiInfo_Company.add(guiinfo_workoutput2);
			listGuiInfo_Company.add(guiinfo_workoutput_finance);
			listGuiInfo_Company.add(guiinfo_workoutput_population);
			listGuiInfo_Company.add(guiinfo_workoutput_other);
			listGuiInfo_Company.add(guiinfo_workoutput_intelligence);
			listGuiInfo_Company.add(guiinfo_energyoutput);
			listGuiInfo_Company.add(guiinfo_wateroutput);
			listGuiInfo_Company.add(guiinfo_condition);
			listGuiInfo_Company.add(guiinfo_neededworkinput_workoutput2);
			listGuiInfo_Company.add(guiinfo_energyconsumption);
			listGuiInfo_Company.add(guiinfo_waterconsumption);
			listGuiInfo_Company.add(guiinfo_workplace_education);
			listGuiInfo_Company.add(guiinfo_workplace_education2);
			listGuiInfo_Company.add(guiinfo_worker);
			listGuiInfo_Company.add(guiinfo_worker2);
			listGuiInfo_Company.add(gui_worktime);
			listGuiInfo_Company.add(guiinfo_CompanyFoodFillingByWorkerObject_education);
			listGuiInfo_Company.add(guiinfo_CompanyFoodFillingByWorkerObject_worker);
			listGuiInfo_Company.add(guiinfo_researchproject);
		}
		
		
			
		
		
		
		//Human Gui Info
		//-------------------------------------------------------------------------
		if (town.gameWorld.markerObject.isHuman()
				&& listGuiInfo_Human.isEmpty()) {
				
				int iclean_toilet_left = 13;
				
				guiinfo_sleep = new CGuiInfo(dlgX + 190 + posleft, dlgY + dialogH
						- y3 + postop + ipostopr * 2, icon_sleep.textureImage, "",
						"Sleep", icsize, icsize, town);
				
				guiinfo_residentenergy = new CGuiInfo(dlgX + 360 + posleft - 40+ iclean_toilet_left
						, dlgY + dialogH - y3 + postop + ipostopr * 2, town.gameResourceConfig.textures.get("guiinfo_residentenergy"), "",
						"Energy", icsize, icsize, town);
				
				guiinfo_eat = new CGuiInfo(dlgX + 190 + posleft, dlgY + dialogH
						- y4 + postop + ipostopr * 3, icon_eat.textureImage, "",
						"Nutrition", icsize, icsize, town);
				
				guiinfo_clean = new CGuiInfo(dlgX + 190 + posleft
						, dlgY + dialogH - y5 + postop	+ ipostopr * 2
						, icon_clean.textureImage, "", "Body Cleanliness",
						icsize, icsize, town);
				
				guiinfo_clothing = new CGuiInfo(dlgX + 360 + posleft - 40+ iclean_toilet_left
						, dlgY + dialogH - y5 + postop	+ ipostopr * 2
						, town.gameResourceConfig.textures.get("guiinfo_clothing"), "", "Clothing Cleanliness",
						icsize, icsize, town);		
				
				guiinfo_toilet = new CGuiInfo(dlgX + 360 + posleft - 40
						+ iclean_toilet_left, dlgY + dialogH - y4 + postop
						+ ipostopr * 3, icon_toilet.textureImage, "", "Toilet",
						icsize, icsize, town);
				
			guiinfo_workoutput = new CGuiInfo(dlgX + 490 + posleft + 5, dlgY
					+ dialogH - y1 + postop - 3,
					town.gameResourceConfig.textures.get("guiinfo_workoutput"),
					"", "Calculated Work Output / Base Work Output", icsize, icsize, town);
			
			guiinfo_workoutput.tooltipMoveX-=550;
			guiinfo_workoutput.tooltipMoveY+=60;
			guiinfo_workoutput.tooltip.textLines.add("Base: Intelligence, Education, Happiness, Health, Fitness, Positive Attitude");
			guiinfo_workoutput.tooltip.textLines.add("Calculated: Energy, Skill, Sick, Caffeine, Luminosity, Temperature");
			
			guiinfo_intelligence = new CGuiInfo(dlgX + 490 + posleft + 5, dlgY
					+ dialogH - y2 + postop + ipostopr - 3,
					town.gameResourceConfig.textures
							.get("guiinfo_intelligence"), "", "Intelligence",
					icsize, icsize, town);
			guiinfo_intelligence.tooltipMoveX-=50;
			
			guiinfo_fitness = new CGuiInfo(dlgX + 490 + posleft + 5, dlgY
					+ dialogH - y3 + postop + ipostopr * 2 - 3,
					town.gameResourceConfig.textures.get("guiinfo_fitness"),
					"", "Fitness", icsize, icsize, town);
			guiinfo_fitness.tooltipMoveX-=50;
			
			guiinfo_education = new CGuiInfo(dlgX + 490 + posleft + 5, dlgY
					+ dialogH - y4 + postop + ipostopr * 3 - 2 + 2,
					town.gameResourceConfig.textures.get("guiinfo_education"),
					"", "Education", icsize, icsize - 2, town);
			guiinfo_education.tooltipMoveX-=50;
			
			//guiinfo_belief = new CGuiInfo(dlgX + 490 + posleft + 5, dlgY
			//		+ dialogH - y6 + postop + ipostopr * 3 - 2 + 2,
			//		town.gameResourceConfig.textures.get("attr_belief"),
			//		"", "Belief", icsize, icsize - 2, town);
			//guiinfo_belief.tooltipMoveX-=50;
						
			guiinfo_health = new CGuiInfo(dlgX + 190 + posleft, dlgY + dialogH
					- y1 + postop, icon_health.textureImage, "",
					"Health / Health Attitude", icsize, icsize, town);
			
			guiinfo_happiness = new CGuiInfo(dlgX + 190 + posleft, dlgY
					+ dialogH - y2 + postop + ipostopr,
					icon_happiness.textureImage, "",
					"Happiness / Positive Attitude", icsize, icsize, town);
			
			
			// listGuiInfo_Human.clear();
			listGuiInfo_Human.add(guiinfo_workoutput);
			listGuiInfo_Human.add(guiinfo_health);
			listGuiInfo_Human.add(guiinfo_happiness);
			listGuiInfo_Human.add(guiinfo_sleep);
			listGuiInfo_Human.add(guiinfo_residentenergy);
			listGuiInfo_Human.add(guiinfo_eat);
			listGuiInfo_Human.add(guiinfo_clean);
			listGuiInfo_Human.add(guiinfo_clothing);
			listGuiInfo_Human.add(guiinfo_toilet);
			listGuiInfo_Human.add(guiinfo_intelligence);
			listGuiInfo_Human.add(guiinfo_fitness);
			listGuiInfo_Human.add(guiinfo_education);
			//listGuiInfo_Human.add(guiinfo_belief);
			
		}
	}
	
	private void createObjects(List<CObject> objectList)
	{
		CWorldObject mobj = town.gameWorld.markerObject;
				
		shapeRenderer = town.gameGui.shapeRenderer;
		shapeRenderer_gui = town.gameGui.shapeRenderer;
		buttonTextureEdit = objectList
				.stream()
				.filter(item -> item.editoraction
						.equals("control_button_edit1")).findFirst().get();
		
		Texture textureWorktime = town.gameResourceConfig.textures.get("gui_worktime");
		
		int buttonSize = 23;
		
		buttonShow_Statistics = new CGuiControl_Button(
				dlgX + 19,
				dlgY + dialogH - 156,
				buttonSize,
				buttonSize,
				0,
				0,
				"create",
				dlgFont,
				town.gameResourceConfig.textures.get("guiinfo_summary"),
				CGuiControl_Button.ButtonType.IMAGE, town);
		buttonShow_Statistics.setTooltip("Show Statistics");
		buttonShow_Statistics.setDuplicate(true);
				
		
		buttonShow_Statistics2 = new CGuiControl_Button(
				dlgX + 19,
				dlgY + dialogH - 156,
				buttonSize,
				buttonSize,
				0,
				0,
				"create",
				dlgFont,
				town.gameResourceConfig.textures.get("guiinfo_summary"),
				CGuiControl_Button.ButtonType.IMAGE, town);
		buttonShow_Statistics2.setTooltip("Show Statistics");
		buttonShow_Statistics2.setDuplicate(true);
				
		
		buttonEdit_ResearchProject = new CGuiControl_Button(
				dlgX + 19,
				dlgY + dialogH - 156,
				buttonSize,
				buttonSize,
				0,
				0,
				"create",
				dlgFont,
				town.gameResourceConfig.textures.get("guiinfo_researchproject"),
				CGuiControl_Button.ButtonType.IMAGE, town);
		buttonEdit_ResearchProject.setTooltip("Choose Research Project");
		buttonEdit_ResearchProject.setDuplicate(true);
		
		
		buttonEdit_Paint = new CGuiControl_Button(
				dlgX + 19,
				dlgY + dialogH - 156,
				buttonSize,
				buttonSize,
				0,
				0,
				"create",
				dlgFont,
				town.gameResourceConfig.textures.get("guiinfo_paint"),
				CGuiControl_Button.ButtonType.IMAGE, town);
		buttonEdit_Paint.setTooltip("Paint Object $" + town.initial_paintobject_price);
		buttonEdit_Paint.setDuplicate(true);
		
		
		buttonAddressClone = new CGuiControl_Button(
				dlgX + 19,
				dlgY + dialogH - 156,
				buttonSize,
				buttonSize,
				0,
				0,
				"create",
				dlgFont,
				town.gameResourceConfig.textures.get("button_address_clone"),
				CGuiControl_Button.ButtonType.IMAGE, town);
		buttonAddressClone.setDuplicate(true);
		
		
		buttonAddressPlanning = new CGuiControl_Button(
				dlgX + 19,
				dlgY + dialogH - 156,
				buttonSize,
				buttonSize,
				0,
				0,
				"create",
				dlgFont,
				town.gameResourceConfig.textures.get("button_address_planning"),
				CGuiControl_Button.ButtonType.IMAGE, town);
		buttonAddressPlanning.setDuplicate(true);
		
		
		buttonAddressResize = new CGuiControl_Button(
				dlgX + 19,
				dlgY + dialogH - 156,
				buttonSize,
				buttonSize,
				0,
				0,
				"create",
				dlgFont,
				town.gameResourceConfig.textures.get("button_address_resize"),
				CGuiControl_Button.ButtonType.IMAGE, town);
		buttonAddressResize.setDuplicate(true);
		
		
		buttonAddressMove = new CGuiControl_Button(
				dlgX + 19,
				dlgY + dialogH - 156,
				buttonSize,
				buttonSize,
				0,
				0,
				"create",
				dlgFont,
				town.gameResourceConfig.textures.get("button_address_move"),
				CGuiControl_Button.ButtonType.IMAGE, town);
		buttonAddressMove.setDuplicate(true);
				
		
		buttonEdit_Worker = new CGuiControl_Button(dlgX + 19, dlgY + dialogH
				- 156, buttonSize, buttonSize, 0, 0, "create", dlgFont,
				buttonTextureEdit.textureImage,
				CGuiControl_Button.ButtonType.IMAGE, town);
		//buttonEdit_Worker.setTooltip("Choose Worker");
		buttonEdit_Worker.setTooltip("Choose Resident");
		buttonEdit_Worker.setDuplicate(true);

		buttonFill = new CGuiControl_Button(dlgX + 19, dlgY + dialogH
				- 156, buttonSize, buttonSize, 0, 0, "create", dlgFont,
				town.gameResourceConfig.textures.get("gui_plus2"),
				CGuiControl_Button.ButtonType.IMAGE, town);

		
		buttonAccept = new CGuiControl_Button(dlgX + 19, dlgY + dialogH
				- 156, buttonSize, buttonSize, 0, 0, "create", dlgFont,
				town.gameResourceConfig.textures.get("button_apply"),
				CGuiControl_Button.ButtonType.IMAGE, town);
		buttonAccept.setTooltip("Apply");
		buttonAccept.setDuplicate(true);
	
						
		
		buttonPlus = new CGuiControl_Button(dlgX + 19, dlgY + dialogH - 156, buttonSize, buttonSize, 0, 0, "create", dlgFont,
				town.gameResourceConfig.textures.get("gui_plus2"),
				CGuiControl_Button.ButtonType.IMAGE, town);
		buttonPlus.setTooltip("Increase Max Company Work Output");
		buttonPlus.setDuplicate(true);
		
		buttonSave = new CGuiControl_Button(dlgX + 19, dlgY + dialogH - 156, buttonSize, buttonSize, 0, 0, "create", dlgFont,
				town.gameResourceConfig.textures.get("gui_plus2"),
				CGuiControl_Button.ButtonType.IMAGE, town);
		buttonSave.setTooltip("Save Blueprint");
		buttonSave.setDuplicate(true);
		
		buttonEdit_Worker2 = new CGuiControl_Button(dlgX + 19, dlgY + dialogH
				- 156, buttonSize, buttonSize, 0, 0, "create", dlgFont,
				buttonTextureEdit.textureImage,
				CGuiControl_Button.ButtonType.IMAGE, town);
		//buttonEdit_Worker2.setTooltip("Choose Worker");
		buttonEdit_Worker2.setTooltip("Choose Resident");
		buttonEdit_Worker2.setDuplicate(true);
		
		buttonClear_Worker = new CGuiControl_Button(dlgX + 3, dlgY + dialogH
				- 156, buttonSize, buttonSize, 0, 0, "create", dlgFont,
				town.gameResourceConfig.textures.get("guiinfo_clear"),
				CGuiControl_Button.ButtonType.IMAGE, town);
		//buttonClear_Worker.setTooltip("Remove Worker");
		buttonClear_Worker.setTooltip("Remove Resident");
		buttonClear_Worker.setDuplicate(true);
		
		buttonClear_Worker2 = new CGuiControl_Button(dlgX + 3, dlgY + dialogH
				- 156, buttonSize, buttonSize, 0, 0, "create", dlgFont,
				town.gameResourceConfig.textures.get("guiinfo_clear"),
				CGuiControl_Button.ButtonType.IMAGE, town);
		//buttonClear_Worker2.setTooltip("Remove Worker");
		buttonClear_Worker2.setTooltip("Remove Resident");
		buttonClear_Worker2.setDuplicate(true);
		
		buttonEdit_Worktime = new CGuiControl_Button(dlgX + 19, dlgY + dialogH
				- 176, buttonSize, buttonSize, 0, 0, "create", dlgFont,
				textureWorktime, CGuiControl_Button.ButtonType.IMAGE, town);
		buttonEdit_Worktime.setTooltip("Set Work Time");
		buttonEdit_Worktime.setDuplicate(true);
		
		// Human
		buttonMoveHouse = new CGuiControl_Button(dlgX + 3,
				dlgY + dialogH - 213, buttonSize, buttonSize, 0, 0, "create",
				dlgFont,
				town.gameResourceConfig.textures
						.get("control_button_movehouse"),
				CGuiControl_Button.ButtonType.TOGGLE_IMAGE, town);
		buttonMoveHouse.setTooltip("Move House");
		buttonMoveHouse.toggleStyle = 2;
		buttonMoveHouse.setDuplicate(true);
		
		// Dinner Table
		
		// Owner 1
		buttonEdit_Owner = new CGuiControl_Button(dlgX + 19, dlgY + dialogH
				- 213, buttonSize, buttonSize, 0, 0, "create", dlgFont,
				buttonTextureEdit.textureImage,
				CGuiControl_Button.ButtonType.IMAGE, town);
		buttonEdit_Owner.setTooltip("Choose Owner");
		buttonEdit_Owner.setDuplicate(true);
		
		// buttonClear_Owner = new CGuiControl_Button(dlgX+3, dlgY+dialogH-213,
		// buttonSize, buttonSize, 0, 0, "create", dlgFont,
		// town.gameResourceConfig.textures.get("guiinfo_clear"),
		// CGuiControl_Button.ButtonType.IMAGE, town1);
		// buttonClear_Owner.setTooltip("Remove Owner");
		// buttonClear_Owner.setDuplicate(true);
		
		// Owner 2
		buttonEdit_Owner2 = new CGuiControl_Button(dlgX + 19, dlgY + dialogH
				- 213, buttonSize, buttonSize, 0, 0, "create", dlgFont,
				buttonTextureEdit.textureImage,
				CGuiControl_Button.ButtonType.IMAGE, town);
		buttonEdit_Owner2.setTooltip("Choose Owner 2");
		buttonEdit_Owner2.setDuplicate(true);
		
		// buttonClear_Owner2 = new CGuiControl_Button(dlgX+3, dlgY+dialogH-213,
		// buttonSize, buttonSize, 0, 0, "create", dlgFont,
		// town.gameResourceConfig.textures.get("guiinfo_clear"),
		// CGuiControl_Button.ButtonType.IMAGE, town1);
		// buttonClear_Owner2.setTooltip("Remove Owner 2");
		// buttonClear_Owner2.setDuplicate(true);
		
		// Seat Owner 1
		buttonEdit_seatOwner1 = new CGuiControl_Button(dlgX + 19, dlgY
				+ dialogH - 213, buttonSize, buttonSize, 0, 0, "create",
				dlgFont, buttonTextureEdit.textureImage,
				CGuiControl_Button.ButtonType.IMAGE, town);
		buttonEdit_seatOwner1.setTooltip("Choose Seat Owner 1");
		buttonEdit_seatOwner1.setDuplicate(true);
		buttonEdit_seatOwner1.enableTooltip(false);
		
		// buttonClear_seatOwner1 = new CGuiControl_Button(dlgX+3,
		// dlgY+dialogH-213, buttonSize, buttonSize, 0, 0, "create", dlgFont,
		// town.gameResourceConfig.textures.get("guiinfo_clear"),
		// CGuiControl_Button.ButtonType.IMAGE, town1);
		// buttonClear_seatOwner1.setTooltip("Remove Seat Owner 1");
		// buttonClear_seatOwner1.setDuplicate(true);
		// buttonClear_seatOwner1.enableTooltip(false);

		// Seat Owner 2
		buttonEdit_seatOwner2 = new CGuiControl_Button(dlgX + 19, dlgY
				+ dialogH - 213, buttonSize, buttonSize, 0, 0, "create",
				dlgFont, buttonTextureEdit.textureImage,
				CGuiControl_Button.ButtonType.IMAGE, town);
		buttonEdit_seatOwner2.setTooltip("Choose Seat Owner 2");
		buttonEdit_seatOwner2.setDuplicate(true);
		buttonEdit_seatOwner2.enableTooltip(false);

		// buttonClear_seatOwner2 = new CGuiControl_Button(dlgX+3,
		// dlgY+dialogH-213, buttonSize, buttonSize, 0, 0, "create", dlgFont,
		// town.gameResourceConfig.textures.get("guiinfo_clear"),
		// CGuiControl_Button.ButtonType.IMAGE, town1);
		// buttonClear_seatOwner2.setTooltip("Remove Seat Owner 2");
		// buttonClear_seatOwner2.setDuplicate(true);
		// buttonClear_seatOwner2.enableTooltip(false);

		// Seat Owner 3
		buttonEdit_seatOwner3 = new CGuiControl_Button(dlgX + 19, dlgY
				+ dialogH - 213, buttonSize, buttonSize, 0, 0, "create",
				dlgFont, buttonTextureEdit.textureImage,
				CGuiControl_Button.ButtonType.IMAGE, town);
		buttonEdit_seatOwner3.setTooltip("Choose Seat Owner 3");
		buttonEdit_seatOwner3.setDuplicate(true);
		buttonEdit_seatOwner3.enableTooltip(false);

		// buttonClear_seatOwner3 = new CGuiControl_Button(dlgX+3,
		// dlgY+dialogH-213, buttonSize, buttonSize, 0, 0, "create", dlgFont,
		// town.gameResourceConfig.textures.get("guiinfo_clear"),
		// CGuiControl_Button.ButtonType.IMAGE, town1);
		// buttonClear_seatOwner3.setTooltip("Remove Seat Owner 3");
		// buttonClear_seatOwner3.setDuplicate(true);
		// buttonClear_seatOwner3.enableTooltip(false);

		// Seat Owner 4
		buttonEdit_seatOwner4 = new CGuiControl_Button(dlgX + 19, dlgY
				+ dialogH - 213, buttonSize, buttonSize, 0, 0, "create",
				dlgFont, buttonTextureEdit.textureImage,
				CGuiControl_Button.ButtonType.IMAGE, town);
		buttonEdit_seatOwner4.setTooltip("Choose Seat Owner 4");
		buttonEdit_seatOwner4.setDuplicate(true);
		buttonEdit_seatOwner4.enableTooltip(false);

		// buttonClear_seatOwner4 = new CGuiControl_Button(dlgX+3,
		// dlgY+dialogH-213, buttonSize, buttonSize, 0, 0, "create", dlgFont,
		// town.gameResourceConfig.textures.get("guiinfo_clear"),
		// CGuiControl_Button.ButtonType.IMAGE, town1);
		// buttonClear_seatOwner4.setTooltip("Remove Seat Owner 4");
		// buttonClear_seatOwner4.setDuplicate(true);
		// buttonClear_seatOwner4.enableTooltip(false);

		// Seat Owner 5
		buttonEdit_seatOwner5 = new CGuiControl_Button(dlgX + 19, dlgY
				+ dialogH - 213, buttonSize, buttonSize, 0, 0, "create",
				dlgFont, buttonTextureEdit.textureImage,
				CGuiControl_Button.ButtonType.IMAGE, town);
		buttonEdit_seatOwner5.setTooltip("Choose Seat Owner 5");
		buttonEdit_seatOwner5.setDuplicate(true);
		buttonEdit_seatOwner5.enableTooltip(false);

		// buttonClear_seatOwner5 = new CGuiControl_Button(dlgX+3,
		// dlgY+dialogH-213, buttonSize, buttonSize, 0, 0, "create", dlgFont,
		// town.gameResourceConfig.textures.get("guiinfo_clear"),
		// CGuiControl_Button.ButtonType.IMAGE, town1);
		// buttonClear_seatOwner5.setTooltip("Remove Seat Owner 5");
		// buttonClear_seatOwner5.setDuplicate(true);
		// buttonClear_seatOwner5.enableTooltip(false);

		// Seat Owner 6
		buttonEdit_seatOwner6 = new CGuiControl_Button(dlgX + 19, dlgY
				+ dialogH - 213, buttonSize, buttonSize, 0, 0, "create",
				dlgFont, buttonTextureEdit.textureImage,
				CGuiControl_Button.ButtonType.IMAGE, town);
		buttonEdit_seatOwner6.setTooltip("Choose Seat Owner 6");
		buttonEdit_seatOwner6.setDuplicate(true);
		buttonEdit_seatOwner6.enableTooltip(false);


		// Seat Owner 7
		buttonEdit_seatOwner7 = new CGuiControl_Button(dlgX + 19, dlgY
				+ dialogH - 213, buttonSize, buttonSize, 0, 0, "create",
				dlgFont, buttonTextureEdit.textureImage,
				CGuiControl_Button.ButtonType.IMAGE, town);
		buttonEdit_seatOwner7.setTooltip("Choose Seat Owner 7");
		buttonEdit_seatOwner7.setDuplicate(true);
		buttonEdit_seatOwner7.enableTooltip(false);

		// Seat Owner 8
		buttonEdit_seatOwner8 = new CGuiControl_Button(dlgX + 19, dlgY
				+ dialogH - 213, buttonSize, buttonSize, 0, 0, "create",
				dlgFont, buttonTextureEdit.textureImage,
				CGuiControl_Button.ButtonType.IMAGE, town);
		buttonEdit_seatOwner8.setTooltip("Choose Seat Owner 8");
		buttonEdit_seatOwner8.setDuplicate(true);
		buttonEdit_seatOwner8.enableTooltip(false);
		
		
		// buttonClear_seatOwner6 = new CGuiControl_Button(dlgX+3,
		// dlgY+dialogH-213, buttonSize, buttonSize, 0, 0, "create", dlgFont,
		// town.gameResourceConfig.textures.get("guiinfo_clear"),
		// CGuiControl_Button.ButtonType.IMAGE, town1);
		// buttonClear_seatOwner6.setTooltip("Remove Seat Owner 6");
		// buttonClear_seatOwner6.setDuplicate(true);
		// buttonEdit_seatOwner6.enableTooltip(false);
		
		// Cook
		buttonEdit_Cook = new CGuiControl_Button(dlgX + 19, dlgY + dialogH
				- 213, buttonSize, buttonSize, 0, 0, "create", dlgFont,
				buttonTextureEdit.textureImage,
				CGuiControl_Button.ButtonType.IMAGE, town);
		//buttonEdit_Cook.setTooltip("Choose Cook");
		buttonEdit_Cook.setTooltip("Choose Cook");
		buttonEdit_Cook.setDuplicate(true);
		buttonEdit_Cook.enableTooltip(false);
		
		buttonClear_Cook = new CGuiControl_Button(dlgX + 3, dlgY + dialogH
				- 213, buttonSize, buttonSize, 0, 0, "create", dlgFont,
				town.gameResourceConfig.textures.get("guiinfo_clear"),
				CGuiControl_Button.ButtonType.IMAGE, town);
		//buttonClear_Cook.setTooltip("Remove Cook");
		buttonClear_Cook.setTooltip("Remove Cook");
		buttonClear_Cook.setDuplicate(true);
		buttonClear_Cook.enableTooltip(false);
		
		buttonEdit_DinnerTime1 = new CGuiControl_Button(dlgX + 19, dlgY
				+ dialogH - 176, buttonSize, buttonSize, 0, 0, "create",
				dlgFont, textureWorktime, CGuiControl_Button.ButtonType.IMAGE,
				town);
		
		buttonEdit_DinnerTime1.setTooltip("Set Dinner Time");
		buttonEdit_DinnerTime1.setDuplicate(true);
		buttonEdit_DinnerTime1.enableTooltip(false);
		
		buttonEdit_DinnerTime2 = new CGuiControl_Button(dlgX + 19, dlgY
				+ dialogH - 176, buttonSize, buttonSize, 0, 0, "create",
				dlgFont, textureWorktime, CGuiControl_Button.ButtonType.IMAGE,
				town);
		
		buttonEdit_DinnerTime2.setTooltip("Set Dinner Time 2");
		buttonEdit_DinnerTime2.setDuplicate(true);
		buttonEdit_DinnerTime2.enableTooltip(false);

		buttonEdit_DinnerTime3 = new CGuiControl_Button(dlgX + 19, dlgY
				+ dialogH - 176, buttonSize, buttonSize, 0, 0, "create",
				dlgFont, textureWorktime, CGuiControl_Button.ButtonType.IMAGE,
				town);
		
		buttonEdit_DinnerTime3.setTooltip("Set Dinner Time 3");
		buttonEdit_DinnerTime3.setDuplicate(true);
		buttonEdit_DinnerTime3.enableTooltip(false);

		buttonClear_DinnerTime1 = new CGuiControl_Button(dlgX + 3, dlgY
				+ dialogH - 213, buttonSize, buttonSize, 0, 0, "create",
				dlgFont, town.gameResourceConfig.textures.get("guiinfo_clear"),
				CGuiControl_Button.ButtonType.IMAGE, town);
		
		buttonClear_DinnerTime1.setTooltip("Remove Dinner Time");
		buttonClear_DinnerTime1.setDuplicate(true);
		buttonClear_DinnerTime1.enableTooltip(false);
		
		buttonClear_DinnerTime2 = new CGuiControl_Button(dlgX + 3, dlgY
				+ dialogH - 213, buttonSize, buttonSize, 0, 0, "create",
				dlgFont, town.gameResourceConfig.textures.get("guiinfo_clear"),
				CGuiControl_Button.ButtonType.IMAGE, town);
		
		buttonClear_DinnerTime2.setTooltip("Remove Dinner Time 2");
		buttonClear_DinnerTime2.setDuplicate(true);
		buttonClear_DinnerTime2.enableTooltip(false);
		
		buttonClear_DinnerTime3 = new CGuiControl_Button(dlgX + 3, dlgY
				+ dialogH - 213, buttonSize, buttonSize, 0, 0, "create",
				dlgFont, town.gameResourceConfig.textures.get("guiinfo_clear"),
				CGuiControl_Button.ButtonType.IMAGE, town);
		
		buttonClear_DinnerTime3.setTooltip("Remove Dinner Time 3");
		buttonClear_DinnerTime3.setDuplicate(true);
		buttonClear_DinnerTime3.enableTooltip(false);
		
		// Human
		pointsControl_healthAttitude = new CGuiControl_Points(town, 0, 0, 3, 1, shapeRenderer_gui, dlgSpriteBatch);
		pointsControl_positiveAttitude = new CGuiControl_Points(town, 0, 0, 3, 1, shapeRenderer_gui, dlgSpriteBatch);
		pointsControl_education = new CGuiControl_Points(town, 0, 0, 3, 1, shapeRenderer_gui, dlgSpriteBatch);
		
		pointsControl_healthAttitude.showButtons(false);
		pointsControl_positiveAttitude.showButtons(false);
		pointsControl_education.showButtons(false);
		
		pointsControl_healthAttitude.setBeginShapeRenderer(false);
		pointsControl_positiveAttitude.setBeginShapeRenderer(false);
		pointsControl_education.setBeginShapeRenderer(false);
		
		pointsControl_healthAttitude.setPointSize(10);
		pointsControl_positiveAttitude.setPointSize(10);
		pointsControl_education.setPointSize(10);
		
		icon_eat = objectList.stream()
				.filter(item -> item.editoraction.equals("info_attr_eat"))
				.findFirst().get();
		icon_sleep = objectList.stream()
				.filter(item -> item.editoraction.equals("info_attr_sleep"))
				.findFirst().get();
		icon_clean = objectList.stream()
				.filter(item -> item.editoraction.equals("info_attr_clean"))
				.findFirst().get();
		icon_toilet = objectList.stream()
				.filter(item -> item.editoraction.equals("info_attr_toilet"))
				.findFirst().get();
		icon_health = objectList.stream()
				.filter(item -> item.editoraction.equals("info_attr_health"))
				.findFirst().get();
		icon_happiness = objectList
				.stream()
				.filter(item -> item.editoraction.equals("info_attr_happiness"))
				.findFirst().get();
	}
	
	public Boolean buttonDown(int x, int y, int libgdxy, int button) {
		
		if(town.gameWorld.markerObject==null)
			return false;
		
		CWorldObject mobj = town.gameWorld.markerObject;
		
		if (town.gameGui.dlgShowing())
			return false;
		
		if (button == 0) {

			if (buttonEdit_Owner.isShowing && buttonEdit_Owner.buttonClick(x, libgdxy)) 
			{
				ListType crt = null;
				
				if (mobj.theobject.editoraction.contains("traffic_car_residential"))
					crt = ListType.CAR_OWNER;
				
				if (mobj.theobject.editoraction.contains("bedroom_wardrobe"))
					crt = ListType.WARDROBE_OWNER;
				
				if (mobj.theobject.editoraction.contains("bedroom_bed"))
					crt = ListType.BED_OWNER;
				
				if(crt==null)
				{
					Gdx.app.debug("Show Owner Dlg", "Listtype for object " + mobj.theobject.editoraction + " is not defined");
					return true;
				}

				town.gameGui.listDlg.showDlg(true, mobj.theaddress, crt, 1);
				
				return true;
			}
			
			if (buttonEdit_Owner2.isShowing
					&& buttonEdit_Owner2.buttonClick(x, libgdxy)) {
				town.gameGui.listDlg.showDlg(true, mobj.theaddress,
						ListType.BED_OWNER, 2);
				return true;
			}
			
			if (buttonEdit_Cook.isShowing
					&& buttonEdit_Cook.buttonClick(x, libgdxy)) {
				town.gameGui.listDlg.showDlg(true, mobj.theaddress,
						ListType.DINNER_COOK, 1);
				return true;
			}
			
			if (buttonClear_Cook.isShowing
					&& buttonClear_Cook.buttonClick(x, libgdxy)) {
				town.gameWorld.removeWorker(mobj.worker, mobj);
				return true;
			}
			
			if (buttonEdit_DinnerTime1.isShowing
					&& buttonEdit_DinnerTime1.buttonClick(x, libgdxy)) {
				town.gameGui.setWorktimeDlg.setActionHour(20, 1);
				if (mobj.actiontime1 > -1)
					town.gameGui.setWorktimeDlg.setActionHour(mobj.actiontime1, 1);
				
				town.gameGui.setWorktimeDlg.showDlg(true);
				return true;
			}

			if (buttonEdit_DinnerTime2.isShowing
					&& buttonEdit_DinnerTime2.buttonClick(x, libgdxy)) {
				town.gameGui.setWorktimeDlg.setActionHour(20, 2);
				if (mobj.actiontime2 > -1)
					town.gameGui.setWorktimeDlg.setActionHour(mobj.actiontime2, 2);

				town.gameGui.setWorktimeDlg.showDlg(true);
				return true;
			}

			if (buttonEdit_DinnerTime3.isShowing
					&& buttonEdit_DinnerTime3.buttonClick(x, libgdxy)) {
				town.gameGui.setWorktimeDlg.setActionHour(20, 3);
				if (mobj.actiontime3 > -1)
					town.gameGui.setWorktimeDlg.setActionHour(mobj.actiontime3, 3);
				
				town.gameGui.setWorktimeDlg.showDlg(true);
				return true;
			}
			
			if (buttonClear_DinnerTime1.isShowing
					&& buttonClear_DinnerTime1.buttonClick(x, libgdxy)) {
				town.gameWorld.markerObject.actiontime1 = -1;
				return true;
			}
			
			if (buttonClear_DinnerTime2.isShowing
					&& buttonClear_DinnerTime2.buttonClick(x, libgdxy)) {
				town.gameWorld.markerObject.actiontime2 = -1;
				return true;
			}
			
			if (buttonClear_DinnerTime3.isShowing
					&& buttonClear_DinnerTime3.buttonClick(x, libgdxy)) {
				town.gameWorld.markerObject.actiontime3 = -1;
				return true;
			}

			if (buttonEdit_seatOwner1.isShowing
					&& buttonEdit_seatOwner1.buttonClick(x, libgdxy)) {
				town.gameGui.listDlg.showDlg(true,
						town.gameWorld.markerObject.theaddress,
						ListType.DINING_SEATOWNER, 1);
				return true;
			}

			if (buttonEdit_seatOwner2.isShowing
					&& buttonEdit_seatOwner2.buttonClick(x, libgdxy)) {
				town.gameGui.listDlg.showDlg(true,
						town.gameWorld.markerObject.theaddress,
						ListType.DINING_SEATOWNER, 2);
				return true;
			}

			if (buttonEdit_seatOwner3.isShowing
					&& buttonEdit_seatOwner3.buttonClick(x, libgdxy)) {
				town.gameGui.listDlg.showDlg(true,
						town.gameWorld.markerObject.theaddress,
						ListType.DINING_SEATOWNER, 3);
				return true;
			}

			if (buttonEdit_seatOwner4.isShowing
					&& buttonEdit_seatOwner4.buttonClick(x, libgdxy)) {
				town.gameGui.listDlg.showDlg(true,
						town.gameWorld.markerObject.theaddress,
						ListType.DINING_SEATOWNER, 4);
				return true;
			}

			if (buttonEdit_seatOwner5.isShowing
					&& buttonEdit_seatOwner5.buttonClick(x, libgdxy)) {
				town.gameGui.listDlg.showDlg(true,
						town.gameWorld.markerObject.theaddress,
						ListType.DINING_SEATOWNER, 5);
				return true;
			}

			if (buttonEdit_seatOwner6.isShowing
					&& buttonEdit_seatOwner6.buttonClick(x, libgdxy)) {
				town.gameGui.listDlg.showDlg(true,
						town.gameWorld.markerObject.theaddress,
						ListType.DINING_SEATOWNER, 6);
				return true;
			}

			if (buttonEdit_seatOwner7.isShowing
					&& buttonEdit_seatOwner7.buttonClick(x, libgdxy)) {
				town.gameGui.listDlg.showDlg(true,
						town.gameWorld.markerObject.theaddress,
						ListType.DINING_SEATOWNER, 7);
				return true;
			}
			
			if (buttonEdit_seatOwner8.isShowing
					&& buttonEdit_seatOwner8.buttonClick(x, libgdxy)) {
				town.gameGui.listDlg.showDlg(true,
						town.gameWorld.markerObject.theaddress,
						ListType.DINING_SEATOWNER, 8);
				return true;
			}
			
			if (buttonMoveHouse.isShowing
					&& buttonMoveHouse.buttonClick(x, libgdxy)) {
				town.gameWorld.bMoveHouse = !town.gameWorld.bMoveHouse;
				
				return true;
			}

			if (buttonClear_Worker.isShowing
					&& buttonClear_Worker.buttonClick(x, libgdxy)) {
				town.gameWorld.removeWorker(town.gameWorld.markerObject.worker, town.gameWorld.markerObject);
				
				return true;
			}

			if (buttonClear_Worker2.isShowing && buttonClear_Worker2.buttonClick(x, libgdxy)) 
			{
				town.gameWorld.removeWorker(town.gameWorld.markerObject.worker2, town.gameWorld.markerObject);
				
				return true;
			}

			if (buttonEdit_Worker.isShowing && buttonEdit_Worker.buttonClick(x, libgdxy)) 
			{
				ListType rt = ListType.WORKER;
				
				if (mobj.theobject.editoraction.contains("company_school_workingplace_studentsdesk"))
					rt = ListType.SCHOOL_STUDENT;
				if (mobj.theobject.editoraction.contains("company_college_workingplace_studentsdesk"))
					rt = ListType.COLLEGE_STUDENT;
				
				town.gameGui.listDlg.showDlg(true, town.gameWorld.markerObject.theaddress, rt, 1);
				
				return true;
			}
			
			if (buttonFill.isShowing && buttonFill.buttonClick(x, libgdxy)) 
			{
				if (mobj.theobject.editoraction.contains("fridge")) 
				{
					if(mobj.getObjectFillingMulti()==0) {
						int price=town.fillFridgeCost*town.gameWorld.worldTime.getCurrentDay();
						if(town.gameWorld.townMoney > price)
						{
							town.gameWorld.townStatistics.getCurrentStatistics_Finance().buyObject+=price;
							CAnimationTextEvent event1 = town.gameWorld.markerObject.addAnimationEvent(CAnimationTextEvent.AnimationEventType.MONEY, (int)-price);
							town.gameWorld.changeTownMoney(-price);
							mobj.addObjectFillingMulti(0, 1000);
						}
					}
				}
				
				return true;
			}
			
			if (buttonPlus.isShowing && buttonPlus.buttonClick(x, libgdxy))
			{
				if(mobj.theobject.isRoomObject)
				{
					Boolean bClone=false;
					int architect_costs = CCompany.getArchitectCosts(town, ArchitectWorkType.CLONE_ROOM);
					if(architect_costs>0)
					{
						CWorldObject architect = CCompany.getArchitectByMinWorkoutput(town, architect_costs);
						if(architect!=null)
						{
							if(architect.belongsToCompany.consumeMinWorkOutput(architect_costs, WorkoutputType.DEFAULT)) {
								bClone=true;
							}
						}
					}
					else {
						bClone=true;
					}
					
					if(bClone)
					{
						town.gameWorld.cloneRoomList.clear();
						CWorldObject newroom = mobj.clone();
						town.gameWorld.cloneRoomList.add(newroom);
						int count=0;
						for(CWorldObject wobj : mobj.theaddress.listWorldObjects)
						{
							if(mobj.testpoint(wobj.pos_x()+wobj.width/2, wobj.pos_y()+wobj.height/2, IntersectionMode.COLLISION))
							{
								if(!wobj.isHuman())
								{
									count++;
									CWorldObject co = wobj.clone();
									//set123
									co.theroom=newroom;
									co.theroomid=newroom.uniqueId;
									co.uniqueId+=count;
									co.theaddressid=0;
									co.theaddress=null;
									town.gameWorld.cloneRoomList.add(co);
								}
							}
						}
						
						town.gameGui.bRoomCloning=true;
						town.gameWorld.markerObject=null;
					}
				}
				
				return true;
			}

			
			if (buttonSave.isShowing && buttonSave.buttonClick(x, libgdxy))
			{
				if(mobj.theobject.isRoomObject)
				{
					Boolean bClone=false;
					int architect_costs = CCompany.getArchitectCosts(town, ArchitectWorkType.CLONE_ROOM);
					if(architect_costs>0)
					{
						CWorldObject architect = CCompany.getArchitectByMinWorkoutput(town, architect_costs);
						if(architect!=null)
						{
							if(architect.belongsToCompany.consumeMinWorkOutput(architect_costs, WorkoutputType.DEFAULT)) {
								bClone=true;
							}
						}
					}
					else {
						bClone=true;
					}
					
					if(bClone)
					{
						town.gameWorld.cloneRoomList.clear();
						CWorldObject newroom = mobj.clone();
						town.gameWorld.cloneRoomList.add(newroom);
						int count=0;
						for(CWorldObject wobj : mobj.theaddress.listWorldObjects)
						{
							if(mobj.testpoint(wobj.pos_x()+wobj.width/2, wobj.pos_y()+wobj.height/2, IntersectionMode.COLLISION))
							{
								if(!wobj.isHuman())
								{
									count++;
									CWorldObject co = wobj.clone();
									//set123
									co.theroom=newroom;
									co.theroomid=newroom.uniqueId;
									co.uniqueId+=count;
									co.theaddressid=0;
									co.theaddress=null;
									town.gameWorld.cloneRoomList.add(co);
								}
							}
						}
						
						town.gameGui.bRoomCloning=true;
						town.gameWorld.markerObject=null;
					}
				}
				
				return true;
			}

			
			if (buttonEdit_ResearchProject.isShowing && buttonEdit_ResearchProject.buttonClick(x, libgdxy)) 
			{
				if(mobj.theobject.editoraction.contains("objectdesign"))
				{
					ListType rt = ListType.OBJECT_DESIGN;
					town.gameGui.listDlg.showDlg(true, null, rt, 0);
				}
				
				if(mobj.theobject.editoraction.contains("research"))
				{
					ListType rt = ListType.RESEARCH_PROJECT;
					town.gameGui.listDlg.showDlg(true, null, rt, 0);
				}
				
				return true;
			}
			
			if (buttonEdit_Paint.isShowing && buttonEdit_Paint.buttonClick(x, libgdxy)) 
			{
				if(!mobj.isActiveByEnergyConsumption())
					return true;
				
				if(mobj.worker==null)
					return true;
				
				if(town.gameWorld.townMoney > town.initial_paintobject_price)
				{
					town.gameGui.bPaintObject=true;
					town.gameWorld.resetMarkerObject();
					town.gameWorld.mouseOverObject=null;
				}
				
				return true;
			}
			
			if (buttonAddressClone.isShowing && buttonAddressClone.buttonClick(x, libgdxy)) 
			{
				//if(!mobj.isActiveByEnergyConsumption())
				//	return true;
				
				if(!mobj.isActiveByEnergyConsumption())
					return true;
				
				Boolean bClone=false;
				int architect_costs = CCompany.getArchitectCosts(town, ArchitectWorkType.CLONE);
				
				if(architect_costs>0)
				{
					if(mobj.belongsToCompany==null)
						bClone=false;
					else
					{
						if(mobj.bObjectIsReady)
						{
							if(mobj.belongsToCompany!=null && mobj.belongsToCompany.consumeMinWorkOutput(architect_costs, WorkoutputType.DEFAULT))
								bClone=true;
						}
					}
				}
				else
					bClone=true;
				
				if(bClone)
				{
					if(town.gameGui.iShowAddresses==0)
						town.gameGui.iShowAddresses=2;

					town.gameWorld.resetMarkerObject();
					town.gameGui.bAddressCloning=true;
					town.gameGui.cloneAddress=null;
					town.gameGui.clonedAddress=null;
				}
				
				return true;
			}
			
			if (buttonAddressPlanning.isShowing && buttonAddressPlanning.buttonClick(x, libgdxy)) 
			{
				if(!mobj.isActiveByEnergyConsumption())
					return true;
				
				if(mobj.worker!=null)
				{
					if(mobj.belongsToCompany==null || mobj.worker==null)
						return true;
					
					int percent = 0;
					if(mobj.worker!=null && mobj.worker.thehuman.getSkill(mobj.theobject.getSkillObjectId())>=0)
					{
						percent = mobj.worker.thehuman.getSkill_PercentArchitectPlanning(mobj.theobject.getSkillObjectId());
						
						int architect_costs = CCompany.getArchitectCosts(town, ArchitectWorkType.PLANNING);
						
						if(mobj.bObjectIsReady)
						{
						if(mobj.belongsToCompany.consumeMinWorkOutput(architect_costs, WorkoutputType.DEFAULT))
						{
							town.gameWorld.addressArchitectPlanningValue = percent;
						}
						}
					}
				}
				
				return true;
			}
			
			//Resize umsetzen
			if ((buttonAccept.isShowing || mobj.theobject.editoraction.contains("company_college_spaceship")) && buttonAccept.buttonClick(x, libgdxy))
			{
				if(mobj.theobject.editoraction.contains("company_college_spaceship") && mobj.iLaunchstatus<1) {
					mobj.iLaunchstatus=1; //.. launch spaceship
					town.gameAudio.playSound("EFFECT_SPACESHIP", 0f, false);
				}
				
				if(mobj.scrollwidth>0)
				{
					int price = mobj.getResizeByScrollingPrice();
					
					if(town.gameWorld.townMoney > price)
					{
						town.gameWorld.markerObject.theobject.price = town.gameWorld.markerObject.dynamicprice;
						town.gameWorld.townStatistics.getCurrentStatistics_Finance().buyObject+=price;
						CAnimationTextEvent event1 = town.gameWorld.markerObject.addAnimationEvent(CAnimationTextEvent.AnimationEventType.MONEY, (int)-price);
						
						town.gameGui.bResizeCarpetActive=false;
						town.gameGui.bResizeGroundActive=false;
						buttonAddressResize.toggleActive=false;
						
						mobj.scrollwidth=0;
						mobj.scrollheight=0;
						
						town.gameWorld.changeTownMoney(-price);
					}
					else
					{
						mobj.width=mobj.scrollwidth;
						mobj.height=mobj.scrollheight;
						
						mobj.scrollwidth=0;
						mobj.scrollheight=0;
					}
				}
			}
			
			if (buttonAddressResize.isShowing && buttonAddressResize.buttonClick(x, libgdxy)) 
			{
				if(mobj.theobject.editoraction.contains("anyroom_carpet"))
				{
					Boolean bResearched=false;
					bResearched=town.gameWorld.gameResourceConfig.isObjectResearched("function_resizecarpet");
					
					if(bResearched)
						town.gameGui.bResizeCarpetActive=!town.gameGui.bResizeCarpetActive;
				}
				else if(mobj.theobject.isGroundObject)
				{
					Boolean bResearched=false;
					bResearched=town.gameWorld.gameResourceConfig.isObjectResearched("function_resizeground");
					
					if(bResearched)
						town.gameGui.bResizeGroundActive=!town.gameGui.bResizeGroundActive;
				}
				else if(mobj.theobject.editoraction.contains("company_architecturebureau_officeworkingplace"))
				{
					if(!mobj.isActiveByEnergyConsumption())
						return true;
					
					int architect_costs = CCompany.getArchitectCosts(town, ArchitectWorkType.RESIZE);
					
					if(mobj.bObjectIsReady)
					{
					if(mobj.belongsToCompany.consumeMinWorkOutput(architect_costs, WorkoutputType.DEFAULT))
					{
						if(town.gameGui.iShowAddresses==0)
							town.gameGui.iShowAddresses=1;

						town.gameGui.bAddressResizingStart=true;
						town.gameGui.addressResizing_sx=0;
						town.gameGui.addressResizing_ex=0;
						town.gameGui.addressResizing_sy=0;
						town.gameGui.addressResizing_ey=0;					
						town.gameWorld.resetMarkerObject();
					}
					}
				}
				
				return true;
			}
			
			if (buttonAddressMove.isShowing && buttonAddressMove.buttonClick(x, libgdxy)) 
			{
				if(!mobj.isActiveByEnergyConsumption())
					return true;
				
				if(mobj==null || mobj.belongsToCompany==null)
					return false;
				
				int architect_costs = CCompany.getArchitectCosts(town, ArchitectWorkType.MOVE);
				if(mobj.bObjectIsReady)
				{
				if(mobj.belongsToCompany.consumeMinWorkOutput(architect_costs, WorkoutputType.DEFAULT))
				{
					town.gameWorld.resetMarkerObject();
					if(town.gameGui.iShowAddresses==0)
						town.gameGui.iShowAddresses=2;
					town.gameGui.bAddressMoving=true;
					town.gameGui.moveAddress=null;
					town.gameGui.movingAddress=null;
					town.gameGui.moveAddress_Origin_x=0;
					town.gameGui.moveAddress_Origin_y=0;
				}
				}
				
				return true;
			}
			
			if (buttonShow_Statistics.isShowing && buttonShow_Statistics.buttonClick(x, libgdxy)) {
				
				if(!mobj.isActiveByEnergyConsumption())
					return true;

				//nur ausführen wenn genügend workoutput
				if(mobj.belongsToCompany.consumeMinWorkOutput(CCompany.getStatisticsCosts(mobj.getWorkoutputType()), mobj.getWorkoutputType()))
				{
					if(mobj.getWorkoutputType()==WorkoutputType.FINANCE)
					{
						town.gameGui.statisticsListDlg.showDlg(true, ListTypeStatistics.STATISTICS_FINANCE);
					}
					
					if(mobj.getWorkoutputType()==WorkoutputType.POPULATION)
					{
						town.gameGui.statisticsListDlg.showDlg(true, ListTypeStatistics.STATISTICS_POPULATION_COUNT);
					}
					
					if(mobj.getWorkoutputType()==WorkoutputType.OTHER)
					{
						town.gameGui.statisticsListDlg.showDlg(true, ListTypeStatistics.STATISTICS_OTHER);
					}
					
					
					if(mobj.getWorkoutputType()==WorkoutputType.INTELLIGENCE)
					{
						//town.gameGui.statisticsListDlg.showDlg(true, ListTypeStatistics.STATISTICS_INTELLIGENCE);
						town.gameGui.statisticsListDlg.showDlg(true, ListTypeStatistics.STATISTICS_OTHER);
					}					
					//Gdx.app.debug("", "todo: buttonShow_Statistics.buttonclick");
				}
				
				return true;
			}
			
			if (buttonShow_Statistics2.isShowing && buttonShow_Statistics2.buttonClick(x, libgdxy)) {

				if(!mobj.isActiveByEnergyConsumption())
					return true;
				
				//nur ausführen wenn genügend workoutput
				if(mobj.belongsToCompany.consumeMinWorkOutput(CCompany.getStatisticsCosts(mobj.getWorkoutputType()), mobj.getWorkoutputType()))
				{
					if(mobj.getWorkoutputType()==WorkoutputType.POPULATION)
					{
						town.gameGui.statisticsListDlg.showDlg(true, ListTypeStatistics.STATISTICS_POPULATION_ATTRIBUTES);
					}
				}
				
				return true;
			}
			
			if (buttonEdit_Worker2.isShowing && buttonEdit_Worker2.buttonClick(x, libgdxy)) {

				ListType rt = ListType.WORKER;
				
				if (mobj.theobject.editoraction
						.contains("company_school_workingplace_studentsdesk"))
					rt = ListType.SCHOOL_STUDENT;
				if (mobj.theobject.editoraction
						.contains("company_college_workingplace_studentsdesk"))
					rt = ListType.COLLEGE_STUDENT;
				
				town.gameGui.listDlg.showDlg(true,
						town.gameWorld.markerObject.theaddress, rt, 2);
				
				return true;
			}
			
			if (buttonEdit_Worktime.isShowing && buttonEdit_Worktime.buttonClick(x, libgdxy)) 
			{
				town.gameGui.setWorktimeDlg.setHours(town.gameConfigIni.default_worktime_from, town.gameConfigIni.default_worktime_to,
						town.gameConfigIni.default_worktime_from2, town.gameConfigIni.default_worktime_to2
						);
				
				if (town.gameWorld.markerObject.workTime1_From > -1	&& town.gameWorld.markerObject.workTime1_To > -1)
				{
					town.gameGui.setWorktimeDlg.setHours(
							town.gameWorld.markerObject.workTime1_From,
							town.gameWorld.markerObject.workTime1_To,
							town.gameWorld.markerObject.workTime2_From,
							town.gameWorld.markerObject.workTime2_To
						);
				}
				
				town.gameGui.setWorktimeDlg.showDlg(true);
				return true;
			}
		}

		return false;
	}
	
	public void render_human(int itype, int x, int libgdxy) {
		
		buttonMoveHouse.toggleActive = town.gameWorld.bMoveHouse;
		CWorldObject mobj = town.gameWorld.markerObject;
		
		int posleft = 112+160+140; // balken und values
		int postop = 23; // balken und values
		int ipostopr = 10; // balken und values
		int iclean_toilet_left = 13;
		
		pointsControl_education.setBeginShapeRenderer(false);
		
		if (itype == 0) // Render Shapes
		{
			shapeRenderer_gui.set(ShapeType.Line);
			shapeRenderer_gui.setColor(0.2f, 0.2f, 0.2f, 0.7f);
			
			shapeRenderer_gui.line(dlgX + 584, dlgY + dialogH+addHumanDlgHeight-28, dlgX +584, dlgY + dialogH - 145-addHumanDlgHeight); // vert links
			shapeRenderer_gui.line(dlgX + 890, dlgY + dialogH+addHumanDlgHeight-28, dlgX + 890, dlgY + dialogH - 145-addHumanDlgHeight); // vert rechts

			shapeRenderer_gui.line(dlgX + 514, dlgY + dialogH-39-80 -addHumanDlgHeight-addHumanDlgHeight2, dlgX + 364+80+70, dlgY + dialogH - 147-90-addHumanDlgHeight-addHumanDlgHeight2); // vert jobs/skills
			shapeRenderer_gui.line(dlgX, dlgY + dialogH - 147-addHumanDlgHeight2, dlgX + dialogW, dlgY + dialogH - 147-addHumanDlgHeight2); // horz
			shapeRenderer_gui.line(dlgX, dlgY + dialogH - 147-90-addHumanDlgHeight-addHumanDlgHeight2, dlgX + dialogW, dlgY + dialogH - 147-90-addHumanDlgHeight-addHumanDlgHeight2); // horz warning
						
			int ibalkenposleft = 8;
			
			// Health
			shapeRenderer_gui.set(ShapeType.Line);
			shapeRenderer_gui.setColor(1f, 1f, 1f, 1f);
			shapeRenderer_gui.rect(dlgX + 225 + posleft - ibalkenposleft, dlgY + dialogH - y1 + postop, mobj.thehuman.healthValueMax + 1, (int) 11);
			shapeRenderer_gui.setColor(0.8f, 0.8f, 0.8f, 0.3f);
			shapeRenderer_gui.rect(dlgX + 225 + posleft - ibalkenposleft + mobj.thehuman.healthValueMax + 5, dlgY + dialogH - y1 + postop, 200 - mobj.thehuman.healthValueMax - 5, (int) 11);
			shapeRenderer_gui.set(ShapeType.Filled);
			shapeRenderer_gui.setColor(0f, 0.8f, 0f, 0.8f);
			if (health < 70)
				shapeRenderer_gui.setColor(0.8f, 0.8f, 0, 0.9f);
			if (health < 30)
				shapeRenderer_gui.setColor(0.6f, 0, 0, 0.7f);
			int he = health;
			if (he > 100)
				he = 100;
			shapeRenderer_gui.rect(dlgX + 225 + posleft - ibalkenposleft, dlgY + dialogH - y1 + postop, he, (int) 10);
			he = health - 100;
			if (he > 0) {
				shapeRenderer_gui.setColor(0.8f, 0.8f, 0f, 1f);
				shapeRenderer_gui.rect(dlgX + 225 + posleft - ibalkenposleft + 101, dlgY + dialogH - y1 + postop, he, (int) 10);
			}
			
			// Happiness
			shapeRenderer_gui.set(ShapeType.Line);
			shapeRenderer_gui.setColor(1f, 1f, 1f, 1f);
			shapeRenderer_gui.rect(dlgX + 225 + posleft - ibalkenposleft, dlgY + dialogH - y2 + postop + ipostopr, mobj.thehuman.happinessValueMax + 1, (int) 11);
			shapeRenderer_gui.setColor(0.8f, 0.8f, 0.8f, 0.3f);
			shapeRenderer_gui.rect(dlgX + 225 + posleft - ibalkenposleft
					+ mobj.thehuman.happinessValueMax + 5, dlgY + dialogH - y2
					+ postop + ipostopr,
					200 - mobj.thehuman.happinessValueMax - 5, (int) 11);
			shapeRenderer_gui.set(ShapeType.Filled);
			shapeRenderer_gui.setColor(0f, 0.8f, 0f, 0.8f);
			if (happiness < 70)
				shapeRenderer_gui.setColor(0.8f, 0.8f, 0, 0.9f);
			if (happiness < 30)
				shapeRenderer_gui.setColor(0.6f, 0, 0, 0.7f);
			int ha = happiness;
			if (ha > 100)
				ha = 100;
			shapeRenderer_gui.rect(dlgX + 225 + posleft - ibalkenposleft, dlgY
					+ dialogH - y2 + postop + ipostopr, ha, (int) 10);
			ha = happiness - 100;
			if (ha > 0) {
				shapeRenderer_gui.setColor(0.8f, 0.8f, 0f, 1f);
				shapeRenderer_gui.rect(dlgX + 225 + posleft - ibalkenposleft
						+ (101), dlgY + dialogH - y2 + postop + ipostopr, (ha),
						(int) (10));
			}
			
			pointsControl_healthAttitude
					.setValue(mobj.thehuman.healthAttitude);
			pointsControl_healthAttitude
					.setPosition(dlgX + 395 + posleft + 10 - ibalkenposleft
							+ 30, dlgY + dialogH - y1 + postop - 1 + 1);
			pointsControl_healthAttitude.render(0, 0);
			
			pointsControl_positiveAttitude
					.setValue(mobj.thehuman.positiveAttitude);
			pointsControl_positiveAttitude.setPosition(dlgX + 395 + posleft
					+ 10 - ibalkenposleft + 30, dlgY + dialogH - y2 + postop
					+ ipostopr - 1 + 1);
			pointsControl_positiveAttitude.render(0, 0);
			
			pointsControl_education
					.setValue(mobj.thehuman
							.getEducationValue());
			pointsControl_education.setPosition(dlgX + 490 + posleft + 10 - 10
					+ 8 + 25, dlgY + dialogH - y4 + postop + ipostopr + 17 + 1);
			pointsControl_education.render(0, 0);
			
			
			// Sleep
			shapeRenderer_gui.set(ShapeType.Line);
			shapeRenderer_gui.setColor(0.8f, 0.8f, 0.8f, 0.7f);
			shapeRenderer_gui.rect(dlgX + 225 + posleft - ibalkenposleft, dlgY
					+ dialogH - y3 + postop + ipostopr * 2, (101), (int) (11));
			
			shapeRenderer_gui.set(ShapeType.Filled);
			shapeRenderer_gui.setColor(0f, 0.8f, 0f, 0.8f);
			if (mobj.thehuman.sleepValue > mobj.thehuman.sleepValueTrigger)
				shapeRenderer_gui.setColor(0.8f, 0.8f, 0, 0.9f);
			if (mobj.thehuman.sleepValue > mobj.thehuman.sleepValueTriggerRed) {
				// ..
			} else
				shapeRenderer_gui.rect(dlgX + 225 + posleft - ibalkenposleft,
						dlgY + dialogH - y3 + postop + ipostopr * 2,
						(sleepPercent), (int) (10));
			
			
			// Energy
			shapeRenderer_gui.set(ShapeType.Line);
			shapeRenderer_gui.setColor(0.8f, 0.8f, 0.8f, 0.7f);
			shapeRenderer_gui.rect(dlgX + 395 + posleft - 40 - ibalkenposleft+ iclean_toilet_left, 
					dlgY + dialogH - y3 + postop + ipostopr * 2, (101), (int) (11));
			
			shapeRenderer_gui.set(ShapeType.Filled);
			shapeRenderer_gui.setColor(0f, 0.8f, 0f, 0.8f);
			if (mobj.thehuman.energyValue < mobj.thehuman.energyValueTrigger)
				shapeRenderer_gui.setColor(0.8f, 0.8f, 0, 0.9f);
			
			if (mobj.thehuman.energyValue < mobj.thehuman.energyValueTriggerRed) {
				// ..
			} else
				shapeRenderer_gui.rect(dlgX + 395 + posleft - 40 - ibalkenposleft + iclean_toilet_left,
						dlgY + dialogH - y3 + postop + ipostopr * 2,
						(mobj.thehuman.energyValue), (int) (10));
			
			
			// Eat
			shapeRenderer_gui.set(ShapeType.Line);
			shapeRenderer_gui.setColor(0.8f, 0.8f, 0.8f, 0.7f);
			shapeRenderer_gui.rect(dlgX + 225 + posleft - ibalkenposleft, dlgY
					+ dialogH - y4 + postop + ipostopr * 3, (101), (int) (11));
			
			shapeRenderer_gui.set(ShapeType.Filled);
			shapeRenderer_gui.setColor(0f, 0.8f, 0f, 0.8f);
			if (mobj.thehuman.eatValue > mobj.thehuman.eatValueTrigger)
				shapeRenderer_gui.setColor(0.8f, 0.8f, 0, 0.9f);
			if (mobj.thehuman.eatValue > mobj.thehuman.eatValueTriggerRed) {
				// ..
			} else
				shapeRenderer_gui.rect(dlgX + 225 + posleft - ibalkenposleft,
						dlgY + dialogH - y4 + postop + ipostopr * 3,
						(eatPercent), (int) (10));
			
			
			// Clean
			shapeRenderer_gui.set(ShapeType.Line);
			shapeRenderer_gui.setColor(0.8f, 0.8f, 0.8f, 0.7f);
			shapeRenderer_gui.rect(dlgX + 225 + posleft - ibalkenposleft
					, dlgY + dialogH - y5 + postop
					+ ipostopr * 2, (101), (int) (11));
			
			shapeRenderer_gui.set(ShapeType.Filled);
			shapeRenderer_gui.setColor(0f, 0.8f, 0f, 0.8f);
			if (town.gameWorld.markerObject.thehuman.cleanValue > mobj.thehuman.cleanValueTrigger)
				shapeRenderer_gui.setColor(0.8f, 0.8f, 0, 0.9f);
			
			if (town.gameWorld.markerObject.thehuman.cleanValue > mobj.thehuman.cleanValueTriggerRed) 
			{
				// ..
			} else
				shapeRenderer_gui.rect(dlgX + 225 + posleft - ibalkenposleft
						, dlgY + dialogH - y5 + postop + ipostopr * 2, (cleanPercent),
						(int) (10));
			
			
			// Clothing
			shapeRenderer_gui.set(ShapeType.Line);
			shapeRenderer_gui.setColor(0.8f, 0.8f, 0.8f, 0.7f);
			shapeRenderer_gui.rect(dlgX + 395 + posleft - 40 - ibalkenposleft+ iclean_toilet_left
					, dlgY + dialogH - y5 + postop + ipostopr * 2
					, (101), (int) (11));
			shapeRenderer_gui.set(ShapeType.Filled);
			shapeRenderer_gui.setColor(0f, 0.8f, 0f, 0.8f);
			if (mobj.thehuman.clothingValue > mobj.thehuman.clothingValueTrigger)
				shapeRenderer_gui.setColor(0.8f, 0.8f, 0, 0.9f);
			if (mobj.thehuman.clothingValue > mobj.thehuman.clothingValueTriggerRed) 
			{
				// ..
			} else
				shapeRenderer_gui.rect(dlgX + 395 + posleft - 40	- ibalkenposleft + iclean_toilet_left
						, dlgY + dialogH - y5 + postop + ipostopr * 2, (clothingPercent),
						(int) (10));
						
			// Toilet
			shapeRenderer_gui.set(ShapeType.Line);
			shapeRenderer_gui.setColor(0.8f, 0.8f, 0.8f, 0.7f);
			shapeRenderer_gui.rect(dlgX + 395 + posleft - 40 - ibalkenposleft+ iclean_toilet_left
					, dlgY + dialogH - y4 + postop
					+ ipostopr * 3, (101), (int) (11));
			shapeRenderer_gui.set(ShapeType.Filled);
			shapeRenderer_gui.setColor(0f, 0.8f, 0f, 0.8f);
			if (mobj.thehuman.toiletValue > mobj.thehuman.toiletValueTrigger)
				shapeRenderer_gui.setColor(0.8f, 0.8f, 0, 0.9f);
			if (mobj.thehuman.toiletValue > mobj.thehuman.toiletValueTriggerRed) {
				// ..
			} else
				shapeRenderer_gui.rect(dlgX + 395 + posleft - 40
						- ibalkenposleft + iclean_toilet_left, dlgY + dialogH
						- y4 + postop + ipostopr * 3, (toiletPercent),
						(int) (10));
		}
		
		if (itype == 1) // Render Spritebatch
		{
			String actiontext = "";
			if (mobj.activeAction != null) {
				actiontext = mobj.activeAction.actionText;
			} else
				actiontext = "IDLE";
			
			if (town.gameWorld.markerObject.bIsDead)
				actiontext = "DECEASED";
			
			String sname = mobj.thehuman.getName();
			sname = town.gameFont.shortenStringToWidth(dlgFont2, sname, 640);
			address = town.gameFont.shortenStringToWidth(dlgFont, address, 410);
			if(mobj.theaddress2!=null)
			{
				address+=" ("+mobj.theaddress2.addressName + ")";
			}
			
			int iypos = 21;
			town.gameGui.editorSpriteBatch.setShader(town.gameFont.fontShader);
			dlgFont2.setColor(dlgFontColor);
			dlgFont2.getData().setScale(0.6f);
			
			//if (town.bDevMode) 
			{
				dlgFont2.draw(town.gameGui.editorSpriteBatch, sname
						+ " (" + town.gameWorld.markerObject.uniqueId + ")",
						dlgX + 20, dlgY + dialogH + 1 - iypos);
			} 
			//else
			//{
			//	dlgFont2.draw(town.gameGui.editorSpriteBatch, sname, dlgX + 20, dlgY + dialogH + 1 - iypos);
			//}
			
			if(mobj.thehuman.abilitySpaceshipTechnology>0) {
				dlgFont2.setColor(Color.GOLD);
				dlgFont2.draw(town.gameGui.editorSpriteBatch, "Spaceship Technologist", dlgX + 20, dlgY + dialogH + 1 - iypos + 50);
				dlgFont2.setColor(dlgFontColor);
			}
			
			town.gameGui.editorSpriteBatch.setShader(null);
			dlgFont.draw(town.gameGui.editorSpriteBatch, address,
					dlgX + 20, dlgY + dialogH - 40 - iypos);

			dlgFont.draw(town.gameGui.editorSpriteBatch, "Days in Town: " + CHelper.roundFloat(mobj.timeInTown/3600/24,2),
					dlgX + 20+300, dlgY + dialogH - 60 - iypos - 5);
			
			
			dlgFont.draw(town.gameGui.editorSpriteBatch,
					(int) mobj.thehuman.getAge_Float()
							+ " years old", dlgX + 20, dlgY + dialogH - 60 - iypos - 5);
			dlgFont.draw(town.gameGui.editorSpriteBatch,
					"Target/Activity: " + actiontext, dlgX + 20, dlgY + dialogH
							- 80 - iypos - 10);
			
			ArrayList<String> sdemand = mobj.thehuman.getOpenDemandList();
			String sdemands="";
			for(String s1 : sdemand)
			{
				String ds=s1;
				if(ds.toLowerCase().contains("tv2"))
					ds="3D PLASMA TV";
				sdemands+=ds+", ";
			}
			if(sdemands.length()>0)
			{
				sdemands=sdemands.substring(0, sdemands.length()-2);
				sdemands=sdemands.replace("_", " ");
			}
						
			dlgFont.draw(town.gameGui.editorSpriteBatch,
					"Open Demand: " + sdemands, dlgX + 20, dlgY + dialogH
							- 100 - iypos - 15);
			
			
			int icsize = 20;
			
			// town.gameWorld.gameGui.editorSpriteBatch.draw(icon_health.textureImage,
			// dlgX+190+posleft, dlgY+dlgH-y1+postop, icsize, icsize);
			// town.gameWorld.gameGui.editorSpriteBatch.draw(icon_happiness.textureImage,
			// dlgX+190+posleft, dlgY+dlgH-y2+postop+ipostopr, icsize, icsize);
			// town.gameWorld.gameGui.editorSpriteBatch.draw(icon_sleep.textureImage,
			// dlgX+190+posleft, dlgY+dlgH-y3+postop+ipostopr*2, icsize,
			// icsize);
			// town.gameWorld.gameGui.editorSpriteBatch.draw(icon_eat.textureImage,
			// dlgX+190+posleft, dlgY+dlgH-y4+postop+ipostopr*3, icsize,
			// icsize);
			// town.gameWorld.gameGui.editorSpriteBatch.draw(icon_clean.textureImage,
			// dlgX+360+posleft-40, dlgY+dlgH-y3+postop+ipostopr*2, icsize,
			// icsize);
			// town.gameWorld.gameGui.editorSpriteBatch.draw(icon_toilet.textureImage,
			// dlgX+360+posleft-40, dlgY+dlgH-y4+postop+ipostopr*3, icsize,
			// icsize);
			// //town.gameWorld.gameGui.editorSpriteBatch.draw(town.gameResourceConfig.textures.get("guiinfo_workoutput"),
			// dlgX+490+posleft-25, dlgY+dlgH-y1+postop-3, icsize, icsize);
			// town.gameWorld.gameGui.editorSpriteBatch.draw(town.gameResourceConfig.textures.get("guiinfo_intelligence"),
			// dlgX+490+posleft-25, dlgY+dlgH-y2+postop+ipostopr-3, icsize,
			// icsize);
			// town.gameWorld.gameGui.editorSpriteBatch.draw(town.gameResourceConfig.textures.get("guiinfo_fitness"),
			// dlgX+490+posleft-25, dlgY+dlgH-y3+postop+ipostopr*2-3, icsize,
			// icsize);
			// town.gameWorld.gameGui.editorSpriteBatch.draw(town.gameResourceConfig.textures.get("guiinfo_education"),
			// dlgX+490+posleft-25, dlgY+dlgH-y4+postop+ipostopr*3-2+2, icsize,
			// icsize-5);
			
			town.gameGui.editorSpriteBatch.end();
			for (CGuiInfo guiinfo : listGuiInfo_Human) {
				guiinfo.render(x, libgdxy);
			}
			town.gameGui.editorSpriteBatch.begin();
			
			float woDefault = mobj.thehuman.getWorkOutputPerHour(false, null, null, false);
			float wo = mobj.thehuman.getWorkOutputPerHour(true, null, null, false);
			
			String sworkoutput = (int) woDefault + "";
			if (woDefault != wo) {
				sworkoutput = (int) wo + " / " + (int) woDefault;
				if (wo < woDefault)
					dlgFont.setColor(1, 0, 0, 0.8f);
				else
					dlgFont.setColor(0.8f, 0.8f, 0, 0.8f);
			}
			
			dlgFont.draw(town.gameGui.editorSpriteBatch, sworkoutput,
					dlgX + 490 + posleft + 10 + 25, dlgY + dialogH - y1
							+ postop - 3 + 10 + 1);
			dlgFont.setColor(dlgFontColor);
			
			dlgFont.draw(
					town.gameGui.editorSpriteBatch,
					""
							+ (int) mobj.thehuman
									.getIntelligenceValue(), dlgX + 490
							+ posleft + 10 + 25, dlgY + dialogH - y2 + postop
							+ ipostopr - 3 + 10 + 1);
			
			dlgFont.draw(
					town.gameGui.editorSpriteBatch,
					""	+ (int) mobj.thehuman
									.getFitnessValue(), dlgX + 490 + posleft
							+ 10 + 25, dlgY + dialogH - y3 + postop + ipostopr
							* 2 - 3 + 10 + 1);
			
			//dlgFont.draw(
			//		town.gameWorld.gameGui.editorSpriteBatch,
			//		""	+ (int) mobj.thehuman
			//						.getBeliefValue(), dlgX + 490 + posleft
			//				+ 10 + 25, dlgY + dialogH - y6 + postop + ipostopr
			//				* 2 - 3 + 10 + 1);
			
			// dlgFont.draw(town.gameWorld.gameGui.editorSpriteBatch,
			// ""+(int)town.gameWorld.markerObject.thehuman.educationValue,
			// dlgX+490+posleft+10, dlgY+dlgH-y3+postop+ipostopr*2-3+10+1-28);
			
			if (town.gameWorld.markerObject.bIsDead) {
				// town.gameWorld.gameGui.editorSpriteBatch.setColor(1,1,1,1f);
				// town.gameWorld.gameGui.editorSpriteBatch.draw(icon_dead,
				// dlgX+170, dlgY+dlgH-68, 50, 50);
			}
			
			String jobTaskString = "";
			String jobTaskString2 = "";
			String jobTaskString3 = "";
			
			
			// Workplaces/Jobs
			Enumeration<CWorldObject> elist = mobj.thehuman.workplaces.elements();
			int ecount = 0;
			
			while (elist.hasMoreElements()) {
				CWorldObject wp = elist.nextElement();
				
				float fskill=0;
				if(mobj.thehuman.jobSkillLevel.containsKey(wp.theobject.getSkillObjectId()))
					fskill = mobj.thehuman.jobSkillLevel.get(wp.theobject.getSkillObjectId()).fskill;
				
				String skilllevelname = mobj.thehuman.getJobSkillLevelName(wp.theobject.getSkillObjectId());
				String skill = ", " + Math.round(fskill) + " (" + skilllevelname + ")";
				if(!wp.theobject.workplaceHasSkill())
					skill="";
				
				String temps =wp.getCompanyWorkingPlaceJobTitle(0) + ", "
							+ wp.getWorkingHoursString() + skill;
						//+ ", ";
				
				if(jobTaskString.isEmpty())
					jobTaskString=temps;
				else if(jobTaskString2.isEmpty())
					jobTaskString2=temps;
				else if(jobTaskString3.isEmpty())
					jobTaskString3=temps;
				
				if (ecount == 2)
					break;
				
				ecount++;
			}
			
			// Taskobjects
			Enumeration<CWorldObject> elist2 = mobj.thehuman.taskobjects
					.elements();
			Hashtable<String, Integer> tasktable = new Hashtable<String, Integer>();
			while (elist2.hasMoreElements()) {
				CWorldObject wp = elist2.nextElement();
				String text = wp.theobject.objectName;
				
				int type = 0;
				if (wp.worker != null && wp.worker.uniqueId == mobj.uniqueId)
					type = 1;
				
				text = wp.getTaskText(type,1);
				
				//Skill anzeigen
				if(type==1 && wp.theobject.workplaceHasSkill())
				{
					float fskill=0;
					int objid=wp.theobject.getSkillObjectId();
					if(mobj.thehuman.jobSkillLevel.containsKey(objid))
						fskill = mobj.thehuman.jobSkillLevel.get(objid).fskill;
					
					String skilllevelname = mobj.thehuman.getJobSkillLevelName(objid);
					text += ", " + Math.round(fskill) + " (" +  skilllevelname + ")";
				}
				
				if (tasktable.containsKey(text))
					tasktable.put(text, tasktable.get(text) + 1);
				else
					tasktable.put(text, 1);
			}
			
			int ecount2 = 0;
			for (String str : tasktable.keySet()) {
				int count = tasktable.get(str);
				if (count > 1)
					str += " (" + count + ")";
				
				//jobTaskString += str + ", ";
				if(jobTaskString.isEmpty())
					jobTaskString=str;
				else if(jobTaskString2.isEmpty())
					jobTaskString2=str;
				else if(jobTaskString3.isEmpty())
					jobTaskString3=str;
				
				if (ecount2 == 2)
					break;
				ecount2++;
			}
			
			//letztes Komma entfernen
			//if (jobTaskString.length() > 1)
			//	jobTaskString = jobTaskString.substring(0,
			//			jobTaskString.length() - 2);
			
			//Strings Kürzen
			//			jobTaskString = town.gameFont.shortenStringToWidth(dlgFont,	jobTaskString, 500);
			//			jobTaskString2 = town.gameFont.shortenStringToWidth(dlgFont, jobTaskString2, 550);
			//			jobTaskString3 = town.gameFont.shortenStringToWidth(dlgFont, jobTaskString3, 550);
			//			
			//			dlgFont.draw(town.gameWorld.gameGui.editorSpriteBatch, "Jobs/Tasks ("+ (mobj.thehuman.workplaces.size() + mobj.thehuman.taskobjects.size()) + "): " 
			//							+ jobTaskString,
			//							dlgX + 20, dlgY + dialogH - y4 + postop + ipostopr * 3 - 40);
			//			
			//			dlgFont.draw(town.gameWorld.gameGui.editorSpriteBatch, jobTaskString2,
			//					dlgX + 20+101, dlgY + dialogH - y4 + postop + ipostopr * 3 - 40 - 20);
			//
			//			
			//			dlgFont.draw(town.gameWorld.gameGui.editorSpriteBatch, jobTaskString3,
			//					dlgX + 20+101, dlgY + dialogH - y4 + postop + ipostopr * 3 - 40 - 40);
			
			
			int zeilenabstand=22;
			
			jobTaskString = town.gameFont.shortenStringToWidth(dlgFont,	jobTaskString, 333+80+70);
			jobTaskString2 = town.gameFont.shortenStringToWidth(dlgFont, jobTaskString2, 333+80+70);
			jobTaskString3 = town.gameFont.shortenStringToWidth(dlgFont, jobTaskString3, 333+80+70);
			
			dlgFont.draw(town.gameGui.editorSpriteBatch, "Jobs/Tasks ("+ (mobj.thehuman.workplaces.size() + mobj.thehuman.taskobjects.size()) + "): ",
					dlgX + 20+29, dlgY + dialogH - y4 + postop + ipostopr * 3 - 40-addHumanDlgHeight2);
			
			dlgFont.draw(town.gameGui.editorSpriteBatch, jobTaskString,
					dlgX + 20, dlgY + dialogH - y4 + postop + ipostopr * 3 - 44 - zeilenabstand-addHumanDlgHeight2);
			
			dlgFont.draw(town.gameGui.editorSpriteBatch, jobTaskString2,
					dlgX + 20, dlgY + dialogH - y4 + postop + ipostopr * 3 - 44 - zeilenabstand*2-addHumanDlgHeight2);
			
			dlgFont.draw(town.gameGui.editorSpriteBatch, jobTaskString3,
					dlgX + 20, dlgY + dialogH - y4 + postop + ipostopr * 3 - 44 - zeilenabstand*3-addHumanDlgHeight2);
			
			
			
			//***************
			//Skills anzeigen
			//***************
			String skillString1="";
			String skillString2="";
			String skillString3="";
			
			ArrayList<CJobSkillClass> jscList = new ArrayList<CJobSkillClass>(mobj.thehuman.jobSkillLevel.values());
			if(jscList.size()>0)
			{
				jscList.sort(new CHuman.JobSkillClassComparator());
				
				if(jscList.size()>0)
				{
					CJobSkillClass jsc1 = jscList.get(0);
					int objid=jsc1.theobject.getSkillObjectId();
					String skilllevelname = mobj.thehuman.getJobSkillLevelName(objid);
					skillString1= Math.round(jsc1.fskill) + " (" + skilllevelname + ")" + " " + jsc1.theobject.getCompanyWorkingPlaceJobTitle(0);
				}
				
				if(jscList.size()>1)
				{
					CJobSkillClass jsc1 = jscList.get(1);
					int objid=jsc1.theobject.getSkillObjectId();
					String skilllevelname = mobj.thehuman.getJobSkillLevelName(objid);
					skillString2= Math.round(jsc1.fskill) + " (" + skilllevelname + ")" + " " + jsc1.theobject.getCompanyWorkingPlaceJobTitle(0);
				}
				
				if(jscList.size()>2)
				{
					CJobSkillClass jsc1 = jscList.get(2);
					int objid=jsc1.theobject.getSkillObjectId();
					String skilllevelname = mobj.thehuman.getJobSkillLevelName(objid);
					skillString3= Math.round(jsc1.fskill) + " (" + skilllevelname + ")" + " " + jsc1.theobject.getCompanyWorkingPlaceJobTitle(0);
				}
			}
			
			skillString1 = town.gameFont.shortenStringToWidth(dlgFont, skillString1, 320+80+70);
			skillString2 = town.gameFont.shortenStringToWidth(dlgFont, skillString2, 320+80+70);
			skillString3 = town.gameFont.shortenStringToWidth(dlgFont, skillString3, 320+80+70);
			
			int skillx=356+80+70;
			
			dlgFont.draw(town.gameGui.editorSpriteBatch, "Skills (" + mobj.thehuman.jobSkillLevel.size()+"): ",
					dlgX + 20+skillx+24, dlgY + dialogH - y4 + postop + ipostopr * 3 - 40-addHumanDlgHeight2);
					
			dlgFont.draw(town.gameGui.editorSpriteBatch, skillString1,
					dlgX + 20+skillx, dlgY + dialogH - y4 + postop + ipostopr * 3 - 44 - zeilenabstand-addHumanDlgHeight2);
					
			dlgFont.draw(town.gameGui.editorSpriteBatch, skillString2,
					dlgX + 20+skillx, dlgY + dialogH - y4 + postop + ipostopr * 3 - 44 - zeilenabstand*2-addHumanDlgHeight2);
			
			dlgFont.draw(town.gameGui.editorSpriteBatch, skillString3,
					dlgX + 20+skillx, dlgY + dialogH - y4 + postop + ipostopr * 3 - 44 - zeilenabstand*3-addHumanDlgHeight2);
			
			
			
			
			//*********
			// Warnings
			//*********
			swarnings = "";
			//			if(mobj.thehuman!=null && mobj.bIsDead && !mobj.actionstring1.contains("show_grave") && !mobj.actionstring1.contains("show_coffin"))
			//				swarnings += " Has to be buried - people are sad, ";
			
			if (mobj.thehuman != null && mobj.thehuman.sick > 0 && mobj.thehuman.sickType==0)
				swarnings += "Resident is sick (" + Math.round(mobj.thehuman.sick) + "%), ";

			if (mobj.thehuman != null && mobj.thehuman.sick == 0 && !mobj.thehuman.canWork())
				swarnings += "Resident is in poor health, ";

			if (mobj.thehuman != null && mobj.thehuman.sick > 0 && mobj.thehuman.sickType==1)
				swarnings += "Severe Disease (" + Math.round(mobj.thehuman.sick) + "%), ";

			if (mobj.thehuman != null && mobj.thehuman.sick > 0 && mobj.thehuman.sickType==2)
				swarnings += "Contagious Disease (" + Math.round(mobj.thehuman.sick) + "%), ";
			
			//swarnings += "Resident is sick (" + Math.round(mobj.thehuman.sick) + "%), ";
			
			if (mobj.thehuman != null && mobj.thehuman.coffeinLevel > CHuman.getCriticalCoffeinLevel(0)
					&& mobj.thehuman.coffeinLevel < CHuman.getCriticalCoffeinLevel(1))
				swarnings += "Too much Caffeine, ";
			
			if (mobj.thehuman != null && mobj.thehuman.coffeinLevel >= CHuman.getCriticalCoffeinLevel(1))
				swarnings += "Caffeine Overdose, ";
			
			if (mobj.thehuman != null && mobj.thehuman.alcoholLevel > 0.29f
					&& mobj.thehuman.alcoholLevel < 1.5f)
				swarnings += mobj.thehuman.alcoholLevel / 10
						+ " Blood Alcohol Content, ";
			
			if (mobj.thehuman != null && mobj.thehuman.alcoholLevel >= 1.5f)
				swarnings += "Resident is Drunken: " + mobj.thehuman.alcoholLevel / 10 + ", ";
			
			if (mobj.thehuman != null && mobj.theaddress != null
					&& mobj.thehuman.bed == null
					&& mobj.thehuman.sleepValue > 3600*20)
				swarnings += "no bed, ";
			
			if (mobj.thehuman != null && mobj.theaddress != null
					&& mobj.thehuman.wardrobe == null
					&& mobj.thehuman.clothingValue > 60000)
				swarnings += "no wardrobe, ";
			
			if (mobj.thehuman.bIsCold)
				swarnings += "cold, ";
				//swarnings += "cold warning, ";
			
			if (mobj.thehuman.bIsDark)
				swarnings += "not enough light, ";
			
			//			if (mobj.activeAction != null) {
			//				CWorldObject workplace = mobj.activeAction.getActiveWorkplaceOrCompanyTaskobject();
			//				if (workplace != null) {
			//					if (mobj.thehuman.getEducationValue() < workplace.theobject.getRecommendedWorkplaceEducation()) {
			//						swarnings += "education is less than recommended, ";
			//					}
			//				}
			//			}
			
			if (town.gameWorld.markerObject.activeActionMode == ActionMode.BED
					&& town.gameWorld.markerObject.thehuman.bed != null
					&& town.gameWorld.markerObject.thehuman.bed.theobject.bBedTooClose) {
				swarnings += "too close to other bed, ";
			}
			
			if (town.gameWorld.markerObject.theaddress == null) {
				swarnings += "homeless, ";
			}
			
			guiinfo_workplace.iconSizeW=19;
			guiinfo_workplace.iconSizeH=18;
			guiinfo_workplace.setTooltip("Jobs/Tasks");
			guiinfo_workplace.render(x, libgdxy, dlgX + 20, dlgY + dialogH - y4 + postop + ipostopr * 3 - 40-13-addHumanDlgHeight2);
			town.gameGui.editorSpriteBatch.end();
			guiinfo_workplace.renderTooltip(x, libgdxy);
			town.gameGui.editorSpriteBatch.begin();
			
			guiinfo_experience.setTooltip("Skills");
			guiinfo_experience.render(x, libgdxy, dlgX + 20+skillx, dlgY + dialogH - y4 + postop + ipostopr * 3 - 40-13-addHumanDlgHeight2);
			town.gameGui.editorSpriteBatch.end();
			guiinfo_experience.renderTooltip(x, libgdxy);
			town.gameGui.editorSpriteBatch.begin();
						
			town.gameGui.editorSpriteBatch.end();
			for (CGuiInfo guiinfo : listGuiInfo_Human) {
				guiinfo.renderTooltip(x, libgdxy);
			}
			
			town.gameGui.editorSpriteBatch.begin();
		}
	}
	
	public void render_companyobject(int x, int libgdxy) 
	{
		for (CGuiInfo guiinfo : listGuiInfo_Company)
			// tooltips werden unabhängig gerendert -> wenn ein button zb water
			// output nicht gerendert wird darf tooltip auch nicht angezeigt
			// werden
			guiinfo.bShowing = false;
		
		CWorldObject mobj = town.gameWorld.markerObject;
		
		if(!mobj.isCompanyObject() && !mobj.showAsCompanyObject())
			return;
		//if(!Gdx.gl.glIsEnabled(GL30.GL_BLEND))
		{
			Gdx.gl.glEnable(GL30.GL_BLEND);
			Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
		}		
		shapeRenderer_gui.setAutoShapeType(true);
		shapeRenderer_gui.begin();
		shapeRenderer_gui.set(ShapeType.Line);
		shapeRenderer_gui.setColor(0.2f, 0.2f, 0.2f, 0.7f);
		shapeRenderer_gui.line(dlgX + 310 + 75, dlgY + dialogH, dlgX + 310 + 75, dlgY + dialogH - 100); // vert
		shapeRenderer_gui.line(dlgX, dlgY + dialogH - 100, dlgX + dialogW, dlgY + dialogH - 100); // horz2
		shapeRenderer_gui.end();
		//Gdx.gl.glDisable(GL30.GL_BLEND);
		
		
		//************************
		//Object name and address
		//************************
		if(mobj.belongsToCompany != null && mobj.belongsToCompany.companyType == CompanyType.TOWN_HALL && mobj.isCompanyOfficeWorkingPlace())
		{
			String sname="";
			String sname2="";
			sname = mobj.theobject.objectName;
			
			if(sname.contains("/"))
			{
				String arr[] = sname.split("/");
				sname=arr[0];
				sname2=arr[1];
			}
			
			sname = town.gameFont.shortenStringToWidth(dlgFont2, sname, 620);
			//if(town.bDevMode)
				sname+=" ("+ mobj.uniqueId +")";
			sname2 = town.gameFont.shortenStringToWidth(dlgFont2, sname2, 620);
			address = town.gameFont.shortenStringToWidth(dlgFont, address, 620);
			
			int iypos = 21;
			town.gameGui.editorSpriteBatch.setShader(town.gameFont.fontShader);
			dlgFont2.setColor(dlgFontColor);
			dlgFont2.getData().setScale(0.6f);
			dlgFont2.draw(town.gameGui.editorSpriteBatch, sname.trim(), dlgX + 20 - 2, dlgY + dialogH + 1 - iypos + 5);
			dlgFont2.draw(town.gameGui.editorSpriteBatch, sname2.trim(), dlgX + 20, dlgY + dialogH + 1 - iypos - 20 - 5);
			town.gameGui.editorSpriteBatch.setShader(null);
			dlgFont.draw(town.gameGui.editorSpriteBatch, address, dlgX + 20, dlgY + dialogH - 40 - iypos - 20+5);
		}
		else
		{
			String sname="";
			sname = town.gameWorld.markerObject.theobject.objectName;
			//if(town.bDevMode)
				sname+=" (ID "+ mobj.uniqueId +")";
			
			sname = town.gameFont.shortenStringToWidth(dlgFont2, sname, 620);
			address = town.gameFont.shortenStringToWidth(dlgFont, address, 620);
			
			int iypos = 21;
			town.gameGui.editorSpriteBatch.setShader(town.gameFont.fontShader);
			dlgFont2.setColor(dlgFontColor);
			dlgFont2.getData().setScale(0.6f);
			dlgFont2.draw(town.gameGui.editorSpriteBatch, sname, dlgX + 20, dlgY + dialogH + 1 - iypos + 2);
			town.gameGui.editorSpriteBatch.setShader(null);
			dlgFont.draw(town.gameGui.editorSpriteBatch, address, dlgX + 20, dlgY + dialogH - 40 - iypos);
		}
		
		
		// *************
		// Objectfilling
		// *************
		if (town.gameWorld.markerObject.isCompanyObjectFillingByWorkerObject()) {
			// Supermarket Shelve
			guiinfo_CompanyFoodFillingByWorkerObject_food.render(x, libgdxy, dlgX + 400, dlgY + dialogH - 134);
			dlgFont.draw(
					town.gameGui.editorSpriteBatch,
					town.gameWorld.markerObject.objectFilling + "/"
							+ mobj.getObjectFillingMax(), dlgX + 435, dlgY
							+ dialogH - 120);
		}
		else if(mobj.theobject.editoraction.contains("_coffeemachine"))
		{
			int left = 0;
			
			guiinfo_coffeebean.setTooltip(mobj.getObjectFillingText());
			guiinfo_coffeebean.render(x, libgdxy, dlgX + 20 + left, dlgY + dialogH - 134);
			dlgFont.draw(town.gameGui.editorSpriteBatch, town.gameWorld.markerObject.objectFilling + "/"
							+ mobj.getObjectFillingMax(), 
							dlgX + 54 + left,	dlgY + dialogH - 120);
			
			guiinfo_cup.setTooltip(mobj.getObjectFilling2Text());
			guiinfo_cup.render(x, libgdxy, dlgX + 20 + left, dlgY + dialogH - 134-28);
			dlgFont.draw(town.gameGui.editorSpriteBatch, town.gameWorld.markerObject.objectFilling2 + "/"
							+ mobj.getObjectFillingMax2(), 
							dlgX + 54 + left,	dlgY + dialogH - 120-28);
			
			town.gameGui.editorSpriteBatch.end();
			guiinfo_coffeebean.renderTooltip(x, libgdxy);
			guiinfo_cup.renderTooltip(x, libgdxy);
			town.gameGui.editorSpriteBatch.begin();
		}
		else if (town.gameWorld.markerObject.getObjectFillingMax() > 0 || town.gameWorld.markerObject.getObjectFillingMultiMax()>0) 
		{
			int left = 0;
			if (mobj.theobject.editoraction.contains("company_recyclingcenter_garbagetruck")) {
				left = 380;
			}
			
			if (mobj.theobject.editoraction.contains("company_construction_pickup1_traffic_car")) {
				left = 380;
			}
			
			if (mobj.theobject.editoraction.contains("pizzataxi")) {
				left = 380;
			}			
			
			// Warehouse, Shopping Cart, coffee machine, fruit plate
			int fill1=0;
			int max1=0;
			if(mobj.getObjectFillingMax()>0)
			{
				fill1=mobj.objectFilling;
				max1=mobj.getObjectFillingMax();
			}
			if(mobj.getObjectFillingMultiMax()>0)
			{
				fill1=mobj.getObjectFillingMulti();
				max1=mobj.getObjectFillingMultiMax();
			}
			
			guiinfo_food.setTooltip(mobj.getObjectFillingText());
			guiinfo_food.render(x, libgdxy, dlgX + 20 + left, dlgY + dialogH - 134);
			dlgFont.draw(town.gameGui.editorSpriteBatch, fill1 + "/" + max1, dlgX + 54 + left, dlgY + dialogH - 120);
		}
		
		
		// *************
		// Company Info
		// *************
		if (mobj.theobject.editoraction.contains("illuminati_defense"))
		{
			//Gesamtworkoutput über alle Illuminati Companies anzeigen
			int iworkoutput=0;
			int imaxworkoutput=0;
			
			for(CCompany comp : town.gameWorld.worldCompanyList)
			{
				if(comp.companyType==CompanyType.ILLUMINATI)
				{
					iworkoutput+=comp.getWorkOutput(mobj.getWorkoutputType());
					imaxworkoutput+=comp.getCompanyMaxWorkoutput();
				}
			}
			
			int py = 10;
			
			dlgFont.draw(town.gameGui.editorSpriteBatch, "" + "Illuminati Information", dlgX + 400, dlgY + dialogH - 25 + py);
			
			guiinfo_Illuminati.render(x, libgdxy, dlgX + 400, dlgY + dialogH - 67 + py - 2);
			guiinfo_workoutput2.render(x, libgdxy, dlgX + 400, dlgY + dialogH - 93 + py - 5);
			
			int defy=30;
			
			//Damage
			if (mobj.theobject.editoraction.contains("illuminati_defensesystem"))
			{
				guiinfo_damage.render(x, libgdxy, dlgX + 400+121-2, dlgY + dialogH - 161-defy);
				float damage=CHelper.roundFloat(mobj.getDefense_schaden(),1);
				float damage2=CHelper.roundFloat(mobj.theobject.defense_schaden,1);
				damage*=100;
				damage2*=100;
				dlgFont.draw(town.gameGui.editorSpriteBatch, "" + Math.round(damage), dlgX + 430+121-2, dlgY + dialogH - 145-defy);
				
				int sy2=30;
				guiinfo_shotfrequency.render(x, libgdxy, dlgX + 400, dlgY + dialogH - 161-defy-sy2);
				int freq= (int) mobj.theobject.defense_schussfrequenz;
				dlgFont.draw(town.gameGui.editorSpriteBatch, "" + freq, dlgX + 430, dlgY + dialogH - 145-defy-sy2);
								
				guiinfo_shotspeed.render(x, libgdxy, dlgX + 400+121-2, dlgY + dialogH - 161-defy-sy2);
				int speed= (int) mobj.theobject.defense_projektilgeschwindigkeit;
				dlgFont.draw(town.gameGui.editorSpriteBatch, "" + speed, dlgX + 430+121-2, dlgY + dialogH - 145-defy-sy2);
			}
			
			//Range
			guiinfo_range.render(x, libgdxy, dlgX + 400, dlgY + dialogH - 161-defy);
			int range= (int) mobj.getDefense_reichweite();
			if(range<Math.round(mobj.theobject.defense_reichweite))
				dlgFont.draw(town.gameGui.editorSpriteBatch, "" + range + "/" + Math.round(mobj.theobject.defense_reichweite), dlgX + 430, dlgY + dialogH - 145-defy);
			else
				dlgFont.draw(town.gameGui.editorSpriteBatch, "" + range, dlgX + 430, dlgY + dialogH - 145-defy);
			dlgFont.setColor(dlgFontColor);
			
			String workoutput_text = iworkoutput + "/" + imaxworkoutput;
			town.gameFont.layout.setText(dlgFont, workoutput_text);
			int workoutput_text_width = (int)town.gameFont.layout.width; 
			
			//Office Workoutput anzeigen
			dlgFont.draw(town.gameGui.editorSpriteBatch,
					town.gameWorld.countIlluminatiDefenseSystems+"",
					dlgX + 435, dlgY + dialogH - 50 + py - 5);
			
			dlgFont.draw(town.gameGui.editorSpriteBatch,
					workoutput_text,
					dlgX + 435, dlgY + dialogH - 78 + py - 5);
			
//			town.gameWorld.gameGui.editorSpriteBatch.end();
//			if (mobj.theobject.editoraction.contains("illuminati_defensesystem"))
//				guiinfo_damage.renderTooltip(x, libgdxy);
//			guiinfo_range.renderTooltip(x, libgdxy);
//			town.gameWorld.gameGui.editorSpriteBatch.begin();
		}
		
		if (mobj.belongsToCompany != null) 
		{
			guiinfo_workplace_education.setTooltip("Required Education");
			
			if (mobj.belongsToCompany.companyType == CompanyType.SCHOOL) 
			{
				int py = 10;
				
				dlgFont.draw(town.gameGui.editorSpriteBatch, "" + mobj.belongsToCompany.companyname + " Information", dlgX + 400, dlgY + dialogH - 25 + py);
				
				int countTeachers = 0;
				int countTeacherDesks = 0;
				int countStudents = 0;
				int countStudentDeskPlaces = 0;
				
				for (CWorldObject cobj : mobj.belongsToCompany.address_company.listWorldObjects) {
					if (!cobj.bDeleted
							&& cobj.theobject.editoraction.contains("_teachersdesk")) {
						countTeacherDesks++;
						if (cobj.worker != null)
							countTeachers++;
					}
					
					if (!cobj.bDeleted && cobj.theobject.editoraction.contains("_studentsdesk")) 
					{
						countStudentDeskPlaces += 2;
						
						if (cobj.worker != null)
							countStudents++;
						
						if (cobj.worker2 != null)
							countStudents++;
					}
				}

				guiinfo_students.render(x, libgdxy, dlgX + 400, dlgY + dialogH
						- 67 + py - 2);
				guiinfo_teachers.render(x, libgdxy, dlgX + 400, dlgY + dialogH
						- 93 + py - 5);
				
				dlgFont.draw(town.gameGui.editorSpriteBatch,
						countStudents + "/" + countStudentDeskPlaces,
						dlgX + 435, dlgY + dialogH - 52 + py - 2);
				dlgFont.draw(town.gameGui.editorSpriteBatch,
						countTeachers + "/" + countTeacherDesks, dlgX + 435,
						dlgY + dialogH - 78 + py - 5);
			}
			else if (mobj.belongsToCompany.companyType == CompanyType.TOWN_HALL)
			{
				//Town Hall Company Info
				int py = 10;
				
				dlgFont.draw(town.gameGui.editorSpriteBatch, ""
						+ mobj.belongsToCompany.companyname + " Information",
						dlgX + 400, dlgY + dialogH - 25 + py);
				
				long countWorkingPlaces = 0;
				long countWorker = 0;
				
				for (CWorldObject cobj : mobj.belongsToCompany.address_company.listWorldObjects) 
				{
					if (!cobj.bDeleted && (cobj.isCompanyWorkingPlace() || cobj.isCompanyTaskObject())) 
					{
						countWorkingPlaces++;
						
						if (cobj.worker != null)
							countWorker++;
					}
				}
				
				//Workers / Workplaces
				//guiinfo_workplace.iconSizeW=20;
				//guiinfo_workplace.iconSizeH=20;
				//guiinfo_workplace.setTooltip("Workers / Workplaces and Tasks");
				//guiinfo_workplace.render(x, libgdxy, dlgX + 400, dlgY + dialogH - 67 + py - 2);
				//dlgFont.draw(town.gameWorld.gameGui.editorSpriteBatch, countWorker + "/" + countWorkingPlaces, dlgX + 435-6, dlgY + dialogH - 52 + py - 2);
				
				//Workoutput
				guiinfo_workoutput_intelligence.render(x, libgdxy, dlgX + 400, dlgY + dialogH - 67 + py - 2);
				dlgFont.draw(town.gameGui.editorSpriteBatch, mobj.belongsToCompany.getWorkOutput(WorkoutputType.INTELLIGENCE) + "/" + mobj.belongsToCompany.getCompanyMaxWorkoutput(), dlgX + 435-6, dlgY + dialogH - 52 + py - 2);
				
				//rechts oben
				guiinfo_workoutput_finance.render(x, libgdxy, dlgX + 510, dlgY + dialogH - 67 + py - 2);
				dlgFont.draw(town.gameGui.editorSpriteBatch, mobj.belongsToCompany.getWorkOutput(WorkoutputType.FINANCE) + "/" + mobj.belongsToCompany.getCompanyMaxWorkoutput(), dlgX + 535+10-6, dlgY + dialogH - 52 + py - 2);
				
				//rechts unten
				guiinfo_workoutput_population.render(x, libgdxy, dlgX + 510, dlgY + dialogH - 93 + py - 5);
				dlgFont.draw(town.gameGui.editorSpriteBatch, mobj.belongsToCompany.getWorkOutput(WorkoutputType.POPULATION) + "/" + mobj.belongsToCompany.getCompanyMaxWorkoutput(), dlgX + 535+10-6, dlgY + dialogH - 78 + py - 5);
				
				//links unten
				guiinfo_workoutput_other.render(x, libgdxy, dlgX + 400, dlgY + dialogH - 93 + py - 5);
				dlgFont.draw(town.gameGui.editorSpriteBatch, mobj.belongsToCompany.getWorkOutput(WorkoutputType.OTHER) + "/" + mobj.belongsToCompany.getCompanyMaxWorkoutput(), dlgX + 435-6, dlgY + dialogH - 78 + py - 5);
			} 
			else if (mobj.belongsToCompany.companyType == CompanyType.PLAYGROUND) {
				int py = 10;
				dlgFont.draw(town.gameGui.editorSpriteBatch, ""
						+ mobj.belongsToCompany.companyname + " Information",
						dlgX + 400, dlgY + dialogH - 25 + py);

				long countObjects = mobj.belongsToCompany.getCompanyObjectsCount();
						
				guiinfo_playground.render(x, libgdxy, dlgX + 400, dlgY
						+ dialogH - 67 + py - 2);
				dlgFont.draw(town.gameGui.editorSpriteBatch,
						countObjects + "", dlgX + 435, dlgY + dialogH - 52 + py
								- 2);
			} 
			else {
				
				int py = 10;
				
				dlgFont.draw(town.gameGui.editorSpriteBatch, ""
						+ mobj.belongsToCompany.companyname + " Information",
						dlgX + 400, dlgY + dialogH - 25 + py);
				
				long countWorkingPlaces = 0;
				long countWorker = 0;
				for (CWorldObject cobj : mobj.belongsToCompany.address_company.listWorldObjects) {
					if (!cobj.bDeleted && (cobj.isCompanyWorkingPlace() || cobj.isCompanyTaskObject())) {
						countWorkingPlaces++;
						if (cobj.worker != null)
							countWorker++;
					}
				}
				
				guiinfo_workplace.iconSizeW=20;
				guiinfo_workplace.iconSizeH=20;
				guiinfo_workplace.setTooltip("Workers / Workplaces and Tasks");
				guiinfo_workplace.render(x, libgdxy, dlgX + 400, dlgY + dialogH	- 67 + py - 2);
				guiinfo_workoutput2.render(x, libgdxy, dlgX + 400, dlgY + dialogH - 93 + py - 5);
				
				dlgFont.draw(town.gameGui.editorSpriteBatch, countWorker + "/" + countWorkingPlaces, dlgX + 435, dlgY + dialogH - 52 + py - 2);
				
				String workoutput_text = mobj.belongsToCompany.getWorkOutput(mobj.getWorkoutputType()) + "/" + mobj.belongsToCompany.getCompanyMaxWorkoutput();
				town.gameFont.layout.setText(dlgFont, workoutput_text);
				int workoutput_text_width = (int)town.gameFont.layout.width; 
				
				//buttonPlus.setDuplicateSize(11, 11);
				//buttonPlus.setTooltip("Increase Max Company Work Output");
				//buttonPlus.setDuplicatePosition((int) dlgX + 435 + workoutput_text_width + 6, (int) dlgY + dialogH - 93 + py-1);
				
				//Office Workoutput anzeigen
				if (mobj.belongsToCompany.companyHasWorkoutput()==1)
				{
					dlgFont.draw(town.gameGui.editorSpriteBatch,
							workoutput_text,
							dlgX + 435, dlgY + dialogH - 78 + py - 5);
				}
				else
				{
					dlgFont.draw(town.gameGui.editorSpriteBatch, "-",
							dlgX + 435, dlgY + dialogH - 78 + py - 5);
				}
				
				
				if (mobj.belongsToCompany.companyType == CompanyType.URBAN_CEMETERY)
				{
					int posx=100;
					
					guiinfo_grave.render(x, libgdxy, dlgX + 400 + posx+5, dlgY + dialogH - 67 + py - 2);
					
					int countOccupiedGraves=0;
					int countAllGraves=0;
					
					for(CWorldObject wobj : mobj.theaddress.listWorldObjects)
					{
						if(wobj.theobject.editoraction.contains("company_urbancemetery_graveempty"))
							countAllGraves++;
						
						if(wobj.isHuman() && wobj.actionstring1.contains("show_grave"))
						{
							countOccupiedGraves++;
							countAllGraves++;
						}
					}
					
					dlgFont.draw(town.gameGui.editorSpriteBatch, countOccupiedGraves + "/" + countAllGraves, dlgX + 435 + posx, dlgY + dialogH - 52 + py - 2);
				}
			}
		}
		
		// ***********************
		// Energy / Water Output
		// ***********************
		if (mobj.theobject.getEnergyOutput() > 0) {
			int eo = mobj.getEnergyOutput();
			if (eo == 0)
				dlgFont.setColor(Color.RED);
			
			guiinfo_energyoutput.render(x, libgdxy, dlgX + 400, dlgY + dialogH - 136);

			if(mobj.getEnergyOutput()<mobj.theobject.getEnergyOutput())
				dlgFont.draw(town.gameGui.editorSpriteBatch, "" + mobj.getEnergyOutput() + "/" + mobj.theobject.getEnergyOutput(), dlgX + 430, dlgY + dialogH - 120);
			else
				dlgFont.draw(town.gameGui.editorSpriteBatch, "" + eo,	dlgX + 430, dlgY + dialogH - 120);	
						
			dlgFont.setColor(dlgFontColor);
		}

		if (mobj.theobject.getWaterOutput() > 0) {
			int eo = mobj.getWaterOutput();
			if (eo == 0)
				dlgFont.setColor(Color.RED);
			
			guiinfo_wateroutput.render(x, libgdxy, dlgX + 400, dlgY + dialogH - 136);
			
			if(mobj.getWaterOutput() < mobj.theobject.getWaterOutput())
				dlgFont.draw(town.gameGui.editorSpriteBatch, "" + mobj.getWaterOutput() + "/" + mobj.theobject.getWaterOutput(), dlgX + 430, dlgY + dialogH - 120);
			else
				dlgFont.draw(town.gameGui.editorSpriteBatch, "" + eo, dlgX + 430, dlgY + dialogH - 120);
			
			dlgFont.setColor(dlgFontColor);
		}

		// ****************
		// Object Condition
		// ****************
		if (mobj.defaultObjectCondition > 0) {
			if (mobj.objectCondition < mobj.defaultObjectCondition / 2)
				dlgFont.setColor(Color.RED);

			guiinfo_condition.render(x, libgdxy, dlgX + 400, dlgY + dialogH
					- 161);

			dlgFont.draw(town.gameGui.editorSpriteBatch, ""
					+ mobj.objectCondition + "/" + mobj.defaultObjectCondition,
					dlgX + 430, dlgY + dialogH - 145);
			dlgFont.setColor(dlgFontColor);
		}
		
		// *********
		// Workinput
		// *********
		if (mobj.theobject.getNeededWorkinputPerHour() > 0) {
			// zeige work consumption an / wenn needed > 0
			int posy = 0;
			if (mobj.theobject.editoraction.contains("supermarket_foodpallet"))
				posy = 27;
			if (mobj.theobject.editoraction.contains("company_recyclingcenter_garbagetruck_traffic_car"))
				posy = 27;
			if (mobj.theobject.editoraction.contains("company_urbancemetery_hearse_traffic_car"))
				posy = 27;
			
			guiinfo_neededworkinput_workoutput2.render(x, libgdxy, dlgX + 520,
					dlgY + dialogH - 161 + posy);
			
			dlgFont.draw(town.gameGui.editorSpriteBatch, ""
					+ mobj.theobject.getNeededWorkinputPerHour(), dlgX + 550, dlgY + dialogH
					- 145 + posy);
		}
		
		if (mobj.getEnergyConsumption() > 0 && mobj.getWaterConsumption() > 0)
		{
			guiinfo_energyconsumption.render(x, libgdxy, dlgX + 520, dlgY + dialogH - 136);
			dlgFont.draw(town.gameGui.editorSpriteBatch, "" + mobj.getEnergyConsumption(), dlgX + 550, dlgY + dialogH - 120);
			
			guiinfo_waterconsumption.render(x, libgdxy, dlgX + 520, dlgY+ dialogH - 136-30);
			dlgFont.draw(town.gameGui.editorSpriteBatch, "" + mobj.getWaterConsumption(), dlgX + 550, dlgY + dialogH - 120-30);
		}
		else
		{
			if (mobj.getEnergyConsumption() > 0) 
			{
				guiinfo_energyconsumption.render(x, libgdxy, dlgX + 520, dlgY + dialogH - 136+4);
				dlgFont.draw(town.gameGui.editorSpriteBatch, "" + mobj.getEnergyConsumption(), dlgX + 550, dlgY + dialogH - 120+4);
			}
			
			if (mobj.getWaterConsumption() > 0)
			{
				guiinfo_waterconsumption.render(x, libgdxy, dlgX + 520, dlgY+ dialogH - 136);
				dlgFont.draw(town.gameGui.editorSpriteBatch, "" + mobj.getWaterConsumption(), dlgX + 550, dlgY + dialogH - 120);
			}
		}
		
		// *********
		// Workplace
		// *********
		if (mobj.isCompanyWorkingPlace()) {
			String sworker = " - ";
			String sworker2 = " - ";
			String sworkhours = "";
			
			if (mobj.worker != null)
			{
				sworker = mobj.worker.thehuman.getName();
				if(mobj.worker.theaddress!=null)
					sworker+=" ("+ mobj.worker.theaddress.addressName +")";
			}
			if (mobj.worker2 != null)
			{
				sworker2 = mobj.worker2.thehuman.getName();
				if(mobj.worker2.theaddress!=null)
					sworker2+=" ("+ mobj.worker2.theaddress.addressName +")";
			}
			
			sworker = town.gameFont.shortenStringToWidth(dlgFont2, sworker, 300);
			sworker2 = town.gameFont.shortenStringToWidth(dlgFont2, sworker2, 300);
			
			sworkhours = mobj.getWorkingHoursString();
			if (sworkhours.trim().isEmpty())
				sworkhours = " - ";
			
			String jobtitle = mobj.getCompanyWorkingPlaceJobTitle(1);
			town.gameFont.layout.setText(dlgFont, jobtitle);
			dlgFont.draw(town.gameGui.editorSpriteBatch,
					jobtitle, dlgX + 20, dlgY + dialogH - 120);
			
			if (mobj.theobject.getRequiredWorkplaceEducation() > -1) 
			{
				guiinfo_workplace_education
						.render(x,
								libgdxy,
								Math.round(dlgX + 20
										+ town.gameFont.layout.width + 10),
								dlgY + dialogH - 132);
				
				pointsControl_education.setValue(mobj.theobject.getRequiredWorkplaceEducation());
				pointsControl_education.setPosition((int) (dlgX + 20 + town.gameFont.layout.width + 37), dlgY + dialogH - 132);
				
				town.gameGui.editorSpriteBatch.end();
				pointsControl_education.setBeginShapeRenderer(true);
				pointsControl_education.render(0, 0);
				town.gameGui.editorSpriteBatch.begin();
			}
			
			int posx2=70;
			if (mobj.theobject.editoraction.contains("company_school_workingplace_studentsdesk")) {
				guiinfo_workplace_education.setTooltip("Max Education Reachable");
				guiinfo_workplace_education.render(x,
						libgdxy,
						Math.round(dlgX + 20
								+ town.gameFont.layout.width + 10),
						dlgY + dialogH - 132);
					
					pointsControl_education.setValue(town.initial_maxschooleducation);
					
					pointsControl_education.setPosition((int) (dlgX + 20
							+ town.gameFont.layout.width + 37), dlgY + dialogH
							- 132);
					
					town.gameGui.editorSpriteBatch.end();
					pointsControl_education.setBeginShapeRenderer(true);
					pointsControl_education.render(0, 0);
					town.gameGui.editorSpriteBatch.begin();
				
				int iy = 40;
				dlgFont.draw(town.gameGui.editorSpriteBatch, sworker, dlgX + 50, dlgY + dialogH - 120 - iy+11);
				dlgFont.draw(town.gameGui.editorSpriteBatch, sworker2, dlgX + 50+posx2 + 200, dlgY + dialogH - 120 - iy+11);
				
				//dlgFont.draw(town.gameWorld.gameGui.editorSpriteBatch, sworkhours, dlgX + 435, dlgY + dialogH - 120);
				dlgFont.draw(town.gameGui.editorSpriteBatch, sworkhours, dlgX + 50, dlgY + dialogH - 180);
				
				guiinfo_worker.setTooltip(mobj.getCompanyWorkingPlaceJobTitle(1) + " 1");
				guiinfo_worker.render(x, libgdxy, dlgX + 19, dlgY + dialogH - 134 - iy+11);
				
				guiinfo_worker2.setTooltip(mobj.getCompanyWorkingPlaceJobTitle(1) + " 2");
				guiinfo_worker2.render(x, libgdxy, dlgX + 19 + 200+posx2, dlgY + dialogH - 134 - iy+11);
				
				gui_worktime.setTooltip("Daily Schedule");
				//gui_worktime.render(x, libgdxy, dlgX + 16 + 385, dlgY + dialogH - 134);
				gui_worktime.render(x, libgdxy, dlgX + 16, dlgY + dialogH - 194);
				
			} else if (mobj.theobject.editoraction.contains("company_college_workingplace_studentsdesk")) {
				int iy = 40;
				dlgFont.draw(town.gameGui.editorSpriteBatch, sworker,
						dlgX + 50, dlgY + dialogH - 120 - iy+11);
				dlgFont.draw(town.gameGui.editorSpriteBatch,
						sworker2, dlgX + 50 + 200+posx2, dlgY + dialogH - 120 - iy+11);
				//dlgFont.draw(town.gameWorld.gameGui.editorSpriteBatch, sworkhours, dlgX + 435, dlgY + dialogH - 120);
				dlgFont.draw(town.gameGui.editorSpriteBatch, sworkhours, dlgX + 50, dlgY + dialogH - 180);
				
				guiinfo_worker.setTooltip(mobj.getCompanyWorkingPlaceJobTitle(1) + " 1");
				guiinfo_worker.render(x, libgdxy, dlgX + 19, dlgY + dialogH - 134 - iy+11);
				
				guiinfo_worker2.setTooltip(mobj.getCompanyWorkingPlaceJobTitle(1) + " 2");
				guiinfo_worker2.render(x, libgdxy, dlgX + 19 + 200+posx2, dlgY + dialogH - 134 - iy+11);
				
				gui_worktime.setTooltip("Daily Schedule");
				//gui_worktime.render(x, libgdxy, dlgX + 16 + 385, dlgY + dialogH - 134);
				gui_worktime.render(x, libgdxy, dlgX + 16, dlgY + dialogH - 194);				
				
				
			} else {
				
				if (mobj.theobject.workplaceHasSkill() && sworker.trim().length() > 1) 
				{
					int exp = 0;
					int objid=mobj.theobject.getSkillObjectId();
					if (mobj.worker.thehuman.jobSkillLevel.containsKey(objid))
						exp = Math.round(mobj.worker.thehuman.jobSkillLevel.get(objid).fskill);
					
					String skilllevelname = mobj.worker.thehuman.getJobSkillLevelName(objid);
					
					guiinfo_experience.setTooltip(mobj.getCompanyWorkingPlaceJobTitle(1) + " Skill Level");
					
					town.gameFont.layout.setText(dlgFont, sworker);
					
					guiinfo_experience.render(x, libgdxy, (int) (dlgX + 54
							+ town.gameFont.layout.width + 10), dlgY + dialogH
							- 164 + 1);
					
					dlgFont.draw(town.gameGui.editorSpriteBatch, ""
							+ exp + " (" + skilllevelname + ")", (int) (dlgX
							+ 54 + town.gameFont.layout.width + 10 + 25), dlgY
							+ dialogH - 150);
				}
				
				dlgFont.draw(town.gameGui.editorSpriteBatch, sworker,
						dlgX + 50, dlgY + dialogH - 150);
				
				dlgFont.draw(town.gameGui.editorSpriteBatch,
						sworkhours, dlgX + 50, dlgY + dialogH - 180);
				
				guiinfo_worker.setTooltip(mobj.getCompanyWorkingPlaceJobTitle(1));
				
				guiinfo_worker.render(x, libgdxy, dlgX + 19, dlgY + dialogH - 164);
				
				gui_worktime.setTooltip("Work Time");
				
				gui_worktime.render(x, libgdxy, dlgX + 16, dlgY + dialogH - 194);
				
				//Researchlab
				if (mobj.theobject.editoraction.contains("company_college_workingplace_researchlab") ||
						mobj.theobject.editoraction.contains("company_objectdesign_officeworkingplace_artist")
						) 
				{
					int ix = 181;
					String rp = " - ";
					String progress="";
					if (mobj.researchObject != null) 
					{
						rp = mobj.researchObject.objectName;
						progress = "Progress: " + mobj.researchObject.iResearchCurrentWorkoutput + "/" + mobj.researchObject.iResearchTargetWorkoutput;
						rp = town.gameFont.shortenStringToWidth(dlgFont, rp, 180);
					}
					
					dlgFont.draw(town.gameGui.editorSpriteBatch, rp, dlgX + 432, dlgY + dialogH - 118 - 30);
					dlgFont.draw(town.gameGui.editorSpriteBatch, progress, dlgX + 432, dlgY + dialogH - 118 - 30-20);
					
					guiinfo_researchproject.render(x, libgdxy, dlgX + 219 + ix, dlgY + dialogH - 132-30);
				}
			}
			
			town.gameGui.editorSpriteBatch.end();
			
			
			
			//***********
			// Buttons
			//***********
			Vector3 vdpos = mobj.getScreenPosition(mobj.width / 2, (int) (mobj.height / 2));
			
			if (mobj.theobject.editoraction.contains("company_school_workingplace_teachersdesk")) {
				//>buttonClear_Worker.setTooltip("Remove Teacher");
				buttonClear_Worker.setTooltip("Remove Resident");
				buttonClear_Worker.setDuplicatePosition((int) vdpos.x
						- getDuplicateSize() * 3 - 8, (int) vdpos.y
						+ getDuplicateSize());
				buttonClear_Worker.setDuplicateSize(getDuplicateSize(),
						getDuplicateSize());
				buttonClear_Worker.renderDuplicateOnly(x, libgdxy);

				//buttonEdit_Worker.setTooltip("Choose Teacher");
				buttonEdit_Worker.setTooltip("Choose Resident");
				buttonEdit_Worker.setDuplicatePosition((int) vdpos.x
						- getDuplicateSize(), (int) vdpos.y
						+ getDuplicateSize());
				buttonEdit_Worker.setDuplicateSize(getDuplicateSize(),
						getDuplicateSize());
				buttonEdit_Worker.renderDuplicateOnly(x, libgdxy);

				buttonEdit_Worktime.setTooltip("Edit Class Time");
				buttonEdit_Worktime.setDuplicatePosition((int) vdpos.x
						- getDuplicateSize() - getDuplicateSize() - 4,
						(int) vdpos.y + getDuplicateSize());
				buttonEdit_Worktime.setDuplicateSize(getDuplicateSize(),
						getDuplicateSize());
				buttonEdit_Worktime.renderDuplicateOnly(x, libgdxy);
			} 
			else if (mobj.theobject.editoraction.contains("company_college_workingplace_profslectern")) {
				//buttonClear_Worker.setTooltip("Remove Professor");
				buttonClear_Worker.setTooltip("Remove Resident");
				buttonClear_Worker.setDuplicatePosition((int) vdpos.x
						- getDuplicateSize() * 3 - 8, (int) vdpos.y
						+ getDuplicateSize());
				buttonClear_Worker.setDuplicateSize(getDuplicateSize(),
						getDuplicateSize());
				buttonClear_Worker.renderDuplicateOnly(x, libgdxy);

				//buttonEdit_Worker.setTooltip("Choose Professor");
				buttonEdit_Worker.setTooltip("Choose Resident");
				buttonEdit_Worker.setDuplicatePosition((int) vdpos.x
						- getDuplicateSize(), (int) vdpos.y
						+ getDuplicateSize());
				buttonEdit_Worker.setDuplicateSize(getDuplicateSize(),
						getDuplicateSize());
				buttonEdit_Worker.renderDuplicateOnly(x, libgdxy);

				buttonEdit_Worktime.setTooltip("Edit Lecture Time");
				buttonEdit_Worktime.setDuplicatePosition((int) vdpos.x
						- getDuplicateSize() - getDuplicateSize() - 4,
						(int) vdpos.y + getDuplicateSize());
				buttonEdit_Worktime.setDuplicateSize(getDuplicateSize(),
						getDuplicateSize());
				buttonEdit_Worktime.renderDuplicateOnly(x, libgdxy);
			} else if (mobj.theobject.editoraction
					.contains("company_school_workingplace_studentsdesk")
					|| mobj.theobject.editoraction
							.contains("company_college_workingplace_studentsdesk")) {
				int pleft = 12;

				buttonClear_Worker.setTooltip("Remove Student 1");
				buttonClear_Worker.setDuplicatePosition((int) vdpos.x
						- getDuplicateSize() - getDuplicateSize() - 4 - pleft,
						(int) vdpos.y + getDuplicateSize());
				buttonClear_Worker.setDuplicateSize(getDuplicateSize(),
						getDuplicateSize());
				buttonClear_Worker.renderDuplicateOnly(x, libgdxy);

				buttonEdit_Worker.setTooltip("Choose Student 1");
				buttonEdit_Worker.setDuplicatePosition((int) vdpos.x
						- getDuplicateSize() - pleft, (int) vdpos.y
						+ getDuplicateSize());
				buttonEdit_Worker.setDuplicateSize(getDuplicateSize(),
						getDuplicateSize());
				buttonEdit_Worker.renderDuplicateOnly(x, libgdxy);

				buttonClear_Worker2.setTooltip("Remove Student 2");
				buttonClear_Worker2.setDuplicatePosition((int) vdpos.x
						+ getDuplicateSize() + getDuplicateSize() + 4 - pleft,
						(int) vdpos.y + getDuplicateSize());
				buttonClear_Worker2.setDuplicateSize(getDuplicateSize(),
						getDuplicateSize());
				buttonClear_Worker2.renderDuplicateOnly(x, libgdxy);

				buttonEdit_Worker2.setTooltip("Choose Student 2");
				buttonEdit_Worker2.setDuplicatePosition((int) vdpos.x
						+ getDuplicateSize() - pleft, (int) vdpos.y
						+ getDuplicateSize());
				buttonEdit_Worker2.setDuplicateSize(getDuplicateSize(),
						getDuplicateSize());
				buttonEdit_Worker2.renderDuplicateOnly(x, libgdxy);
				
				if(mobj.worker!=null)
				{
					String text1 = buttonEdit_Worker.tooltip.textLines.get(0);
					buttonEdit_Worker.setTooltip(text1 + " (Current Student: "+mobj.worker.thehuman.getName()+")");
					
					text1 = buttonClear_Worker.tooltip.textLines.get(0);
					buttonClear_Worker.setTooltip(text1 + " (Current Student: "+mobj.worker.thehuman.getName()+")");
				}
				if(mobj.worker2!=null)
				{
					String text1 = buttonEdit_Worker2.tooltip.textLines.get(0);
					buttonEdit_Worker2.setTooltip(text1 + " (Current Student: "+mobj.worker2.thehuman.getName()+")");
					
					text1 = buttonClear_Worker2.tooltip.textLines.get(0);
					buttonClear_Worker2.setTooltip(text1 + " (Current Student: "+mobj.worker2.thehuman.getName()+")");
				}
			} 
			else 
			{
				//buttonClear_Worker.setTooltip("Remove " + mobj.getCompanyWorkingPlaceJobTitle());
				buttonClear_Worker.setTooltip("Remove Resident");
				
				// buttonClear_Worker.setTooltip("Remove Worker");
				buttonClear_Worker.setDuplicatePosition((int) vdpos.x - getDuplicateSize() * 3 - 8, (int) vdpos.y + getDuplicateSize());
				buttonClear_Worker.setDuplicateSize(getDuplicateSize(), getDuplicateSize());
				buttonClear_Worker.renderDuplicateOnly(x, libgdxy);

				// buttonEdit_Worker.setTooltip("Choose Worker");
				//buttonEdit_Worker.setTooltip("Choose "+ mobj.getCompanyWorkingPlaceJobTitle());
				buttonEdit_Worker.setTooltip("Choose Resident");
				buttonEdit_Worker.setDuplicatePosition((int) vdpos.x - getDuplicateSize(), (int) vdpos.y + getDuplicateSize());
				buttonEdit_Worker.setDuplicateSize(getDuplicateSize(), getDuplicateSize());
				buttonEdit_Worker.renderDuplicateOnly(x, libgdxy);
				
				buttonEdit_Worktime.setTooltip("Edit Work Time");
				buttonEdit_Worktime.setDuplicatePosition((int) vdpos.x - getDuplicateSize() - getDuplicateSize() - 4, (int) vdpos.y + getDuplicateSize());
				buttonEdit_Worktime.setDuplicateSize(getDuplicateSize(), getDuplicateSize());
				buttonEdit_Worktime.renderDuplicateOnly(x, libgdxy);
			}
			
			if (mobj.theobject.editoraction.contains("company_college_workingplace_researchlab")) 
			{
				buttonEdit_ResearchProject.setTooltip("Choose Research Project");
				buttonEdit_ResearchProject.setDuplicatePosition((int) vdpos.x + 4, (int) vdpos.y + getDuplicateSize());
				buttonEdit_ResearchProject.setDuplicateSize(getDuplicateSize(),getDuplicateSize());
				buttonEdit_ResearchProject.renderDuplicateOnly(x, libgdxy);
			}
			
			if (mobj.theobject.editoraction.contains("company_objectdesign_officeworkingplace_artist")) 
			{
				buttonEdit_ResearchProject.setTooltip("Choose Design Project");
				buttonEdit_ResearchProject.setDuplicatePosition((int) vdpos.x + 21, (int) vdpos.y + getDuplicateSize());
				buttonEdit_ResearchProject.setDuplicateSize(getDuplicateSize(),getDuplicateSize());
				buttonEdit_ResearchProject.renderDuplicateOnly(x, libgdxy);
				
				buttonEdit_Paint.setTooltip("Paint Object $" + town.initial_paintobject_price);
				buttonEdit_Paint.setDuplicatePosition((int) vdpos.x + 21+4 + getDuplicateSize(), (int) vdpos.y + getDuplicateSize());
				buttonEdit_Paint.setDuplicateSize(getDuplicateSize(), getDuplicateSize());
				buttonEdit_Paint.renderDuplicateOnly(x, libgdxy);
				buttonEdit_Paint.setColor(new Color(1f,1f,1f,1));
				buttonEdit_Paint.tooltip.setColor(new Color(1f,1f,1f,1));
				
				Boolean bRed=false;
				
				if(!mobj.isActiveByEnergyConsumption())
					bRed=true;
				if(mobj.worker==null)
					bRed=true;
				if(town.gameWorld.townMoney < town.initial_paintobject_price)
					bRed=true;
				
				if(bRed)
				{
					buttonEdit_Paint.setColor(new Color(1f,0f,0f,1));
					buttonEdit_Paint.tooltip.setColor(new Color(1f,0f,0f,1));
				}
			}
			
			if (mobj.theobject.editoraction.contains("company_townhall_officeworkingplace") && !mobj.theobject.editoraction.contains("intelligence")) 
			{
				int statistics_costs = CCompany.getStatisticsCosts(mobj.getWorkoutputType());
				
				//Show Statistics Button
				buttonShow_Statistics.setColor(new Color(1f,1f,1f,1));
				buttonShow_Statistics.tooltip.setColor(new Color(1f,1f,1f,1));
								
				if((mobj.belongsToCompany!=null && mobj.belongsToCompany.getWorkOutput(mobj.getWorkoutputType()) < statistics_costs) || !mobj.isActiveByEnergyConsumption())
				{
					buttonShow_Statistics.setColor(new Color(1f,0f,0f,1));
					buttonShow_Statistics.tooltip.setColor(new Color(1f,0f,0f,1));
					
					buttonShow_Statistics2.setColor(new Color(1f,0f,0f,1));
					buttonShow_Statistics2.tooltip.setColor(new Color(1f,0f,0f,1));
				}
				
				String stooltiptext1="";
				String stooltiptext2="";
				String stooltiptext3="";
				
				if (mobj.theobject.editoraction.contains("company_townhall_officeworkingplace_finance"))
				{
					stooltiptext1="Finance";
					stooltiptext2="Finance";
				}
					
				if (mobj.theobject.editoraction.contains("company_townhall_officeworkingplace_resident"))
				{
					stooltiptext1="Population";
					stooltiptext2="Population Count";
					stooltiptext3="Population Attributes";
				}
				
				if (mobj.theobject.editoraction.contains("company_townhall_officeworkingplace_other"))
				{
					stooltiptext1="Other";
					stooltiptext2="Other";
				}
				
				buttonShow_Statistics.setTooltip("Show " + stooltiptext2 + " Statistics", "(Requires " + statistics_costs + " " + stooltiptext1 + " Statistics Work Output)");
				buttonShow_Statistics.setDuplicatePosition((int) vdpos.x + 4, (int) vdpos.y + getDuplicateSize());
				buttonShow_Statistics.setDuplicateSize(getDuplicateSize(),getDuplicateSize());
				buttonShow_Statistics.renderDuplicateOnly(x, libgdxy);
				
				if (mobj.theobject.editoraction.contains("company_townhall_officeworkingplace_resident"))
				{
					buttonShow_Statistics2.setTooltip("Show " + stooltiptext3 + " Statistics", "(Requires " + statistics_costs + " " + stooltiptext1 + " Statistics Work Output)");
					buttonShow_Statistics2.setDuplicatePosition((int) vdpos.x + 4 + getDuplicateSize() + 4, (int) vdpos.y + getDuplicateSize());
					buttonShow_Statistics2.setDuplicateSize(getDuplicateSize(),getDuplicateSize());
					buttonShow_Statistics2.renderDuplicateOnly(x, libgdxy);
				}
			}
			
			if (mobj.theobject.editoraction.contains("company_architecturebureau_officeworkingplace")) 
			{
				Color cred = new Color(0.8f,0f,0f,0.9f);
				
				int movx = getDuplicateSize()/2;
				int movy=3;
				movx=getDuplicateSize()*5+4;
				movy=-getDuplicateSize()-4;
				
				buttonAddressClone.setDuplicatePosition((int) vdpos.x - getDuplicateSize()*3 - 8+movx, (int) vdpos.y+getDuplicateSize()*2+4+movy);
				buttonAddressMove.setDuplicatePosition((int) vdpos.x  - getDuplicateSize()*2 - 4+movx, (int) vdpos.y+getDuplicateSize()*2+4+movy);
				buttonAddressResize.setDuplicatePosition((int) vdpos.x - getDuplicateSize()+movx, (int) vdpos.y+getDuplicateSize()*2+4+movy);
				buttonAddressPlanning.setDuplicatePosition((int) vdpos.x - getDuplicateSize()*4 - 12+movx, (int) vdpos.y+getDuplicateSize()*2+4+movy);
				//buttonAddressPlanning.setDuplicatePosition((int) vdpos.x - getDuplicateSize()*3 - 8, (int) vdpos.y+getDuplicateSize()*3+8);

				
				buttonAddressClone.setColor(new Color(1f,1f,1f,1));
				buttonAddressClone.tooltip.setColor(new Color(1f,1f,1f,1));
				buttonAddressMove.setColor(new Color(1f,1f,1f,1));
				buttonAddressMove.tooltip.setColor(new Color(1f,1f,1f,1));
				buttonAddressResize.setColor(new Color(1f,1f,1f,1));
				buttonAddressResize.tooltip.setColor(new Color(1f,1f,1f,1));

				if(!mobj.bObjectIsReady)
				{
					buttonAddressClone.setColor(cred);
					buttonAddressMove.setColor(cred);
					buttonAddressResize.setColor(cred);
					buttonAddressPlanning.setColor(cred);
				}
				
				//Clone Address
				int architect_costs_clone = CCompany.getArchitectCosts(town, ArchitectWorkType.CLONE);
				if(architect_costs_clone>0 && mobj.belongsToCompany!=null && mobj.belongsToCompany.getWorkOutput(mobj.getWorkoutputType()) < architect_costs_clone || !mobj.isActiveByEnergyConsumption())
					buttonAddressClone.setColor(cred);
				if(architect_costs_clone>0)
					buttonAddressClone.setTooltip("Clone Address and all of its objects", "(Requires " + architect_costs_clone + " Architect Work Output)");
				else
					buttonAddressClone.setTooltip("");
				buttonAddressClone.setDuplicateSize(getDuplicateSize(),getDuplicateSize());
				buttonAddressClone.renderDuplicateOnly(x, libgdxy);
				
				//Move Address
				int architect_costs_move = CCompany.getArchitectCosts(town, ArchitectWorkType.MOVE);
				if((mobj.belongsToCompany!=null && mobj.belongsToCompany.getWorkOutput(mobj.getWorkoutputType()) < architect_costs_move) || !mobj.isActiveByEnergyConsumption())
					buttonAddressMove.setColor(cred);
				buttonAddressMove.setTooltip("Move Address and all of its objects", "(Requires " + architect_costs_move + " Architect Workoutput)");
				buttonAddressMove.setDuplicateSize(getDuplicateSize(),getDuplicateSize());
				buttonAddressMove.renderDuplicateOnly(x, libgdxy);
				
				//Resize Address
				int architect_costs_resize = CCompany.getArchitectCosts(town, ArchitectWorkType.RESIZE);
				if((mobj.belongsToCompany!=null && mobj.belongsToCompany.getWorkOutput(mobj.getWorkoutputType()) < architect_costs_resize) || !mobj.isActiveByEnergyConsumption())
					buttonAddressResize.setColor(cred);
				buttonAddressResize.setTooltip("Resize Address", "(Requires " + architect_costs_resize + " Architect Work Output)");
				buttonAddressResize.setDuplicateSize(getDuplicateSize(),getDuplicateSize());
				buttonAddressResize.renderDuplicateOnly(x, libgdxy);
				
				//Plan
				int architect_costs_planning = CCompany.getArchitectCosts(town, ArchitectWorkType.PLANNING);
				Boolean bred=false;
				int percent=0;
				if(mobj.worker!=null && mobj.worker.thehuman.getSkill(mobj.theobject.getSkillObjectId())>=0)
				{
					percent=mobj.worker.thehuman.getSkill_PercentArchitectPlanning(mobj.theobject.getSkillObjectId());
					bred=false;
				}
				if((mobj.belongsToCompany!=null && mobj.belongsToCompany.getWorkOutput(mobj.getWorkoutputType()) >= architect_costs_planning) || !mobj.isActiveByEnergyConsumption())
					bred=false;
				if(percent==0)
					bred=true;
				
				if(bred)
				{
					buttonAddressPlanning.setColor(cred);
					//buttonAddressPlanning.tooltip.setColor(cred);
				}
				buttonAddressPlanning.tooltip.textLines.clear();
				buttonAddressPlanning.tooltip.textLines.add("Planning");
				buttonAddressPlanning.tooltip.textLines.add("Reduce costs of next Address Operation New/Resize/Clone by Architect Skill (" + percent + "%)");
				buttonAddressPlanning.tooltip.textLines.add("(Requires " + architect_costs_planning + " Architect Work Output)");
				buttonAddressPlanning.setDuplicateSize(getDuplicateSize(),getDuplicateSize());
				buttonAddressPlanning.renderDuplicateOnly(x, libgdxy);
			}
			
			town.gameGui.editorSpriteBatch.begin();
		}

		
		// Company Food Filling Object
//		if (mobj.isCompanyObjectFillingByWorkerObject()) {
//			String sworker = " - ";
//
//			if (mobj.worker != null)
//				sworker = mobj.worker.thehuman.name;
//
//			String jobtitle = mobj.getCompanyWorkingPlaceJobTitle(0);
//			town.gameFont.layout.setText(dlgFont, jobtitle);
//
//			dlgFont.draw(town.gameGui.editorSpriteBatch, jobtitle, dlgX + 20,
//					dlgY + dialogH - 120);
//
//			guiinfo_CompanyFoodFillingByWorkerObject_education.render(x,
//					libgdxy,
//					Math.round(dlgX + 20 + town.gameFont.layout.width + 10),
//					dlgY + dialogH - 132);
//
//			pointsControl_education
//					.setValue(town.gameWorld.markerObject.theobject
//							.getRequiredWorkplaceEducation());
//			pointsControl_education.setPosition((int) (dlgX + 20
//					+ town.gameFont.layout.width + 37), dlgY + dialogH - 132);
//			town.gameGui.editorSpriteBatch.end();
//			pointsControl_education.setBeginShapeRenderer(true);
//			pointsControl_education.render(0, 0);
//			town.gameGui.editorSpriteBatch.begin();
//
//			dlgFont.draw(town.gameGui.editorSpriteBatch, "" + sworker,
//					dlgX + 43, dlgY + dialogH - 148);
//
//			guiinfo_CompanyFoodFillingByWorkerObject_worker.render(x, libgdxy,
//					dlgX + 19, dlgY + dialogH - 164);
//			// town.gameWorld.gameGui.editorSpriteBatch.draw(town.gameResourceConfig.textures.get("guiinfo_worker"),
//			// dlgX+19, dlgY+dlgH-164, 12, 21);
//
//			dlgFont.draw(town.gameGui.editorSpriteBatch, "Refill at 50%",
//					dlgX + 21, dlgY + dialogH - 176);
//			town.gameWorld.gameGui.editorSpriteBatch.end();
//
//			Vector3 vdpos = town.gameWorld.markerObject.getScreenPosition(
//					town.gameWorld.markerObject.width / 2,
//					(int) (town.gameWorld.markerObject.height / 2));
//			buttonEdit_Worker.setDuplicatePosition((int) vdpos.x
//					- getDuplicateSize(), (int) vdpos.y + getDuplicateSize());
//			buttonEdit_Worker.setDuplicateSize(getDuplicateSize(),
//					getDuplicateSize());
//			buttonEdit_Worker.renderDuplicateOnly(x, libgdxy);
//
//			buttonClear_Worker.setDuplicatePosition((int) vdpos.x
//					- getDuplicateSize() * 2 - 4, (int) vdpos.y
//					+ getDuplicateSize());
//			buttonClear_Worker.setDuplicateSize(getDuplicateSize(),
//					getDuplicateSize());
//			buttonClear_Worker.renderDuplicateOnly(x, libgdxy);
//
//			town.gameWorld.gameGui.editorSpriteBatch.begin();
//		}
		
		
		
		// Other Company Task Objects
		else if (mobj.isCompanyTaskObject()) 
		{
			String sworker = " - ";
			
			if (mobj.worker != null)
				sworker = mobj.worker.thehuman.getName();
			
			String jobtitle = mobj.getCompanyWorkingPlaceJobTitle(0);
			town.gameFont.layout.setText(dlgFont, jobtitle);
			
			//buttonEdit_Worker.setTooltip("Choose " + mobj.getCompanyWorkingPlaceJobTitle());
			//buttonClear_Worker.setTooltip("Remove " + mobj.getCompanyWorkingPlaceJobTitle());
			buttonEdit_Worker.setTooltip("Choose Resident");
			buttonClear_Worker.setTooltip("Remove Resident");
			
			dlgFont.draw(town.gameGui.editorSpriteBatch, jobtitle, dlgX + 20, dlgY + dialogH - 120);
			guiinfo_CompanyFoodFillingByWorkerObject_education.render(x, libgdxy, Math.round(dlgX + 20 + town.gameFont.layout.width + 10), dlgY + dialogH - 132);
			
			pointsControl_education.setValue(town.gameWorld.markerObject.theobject.getRequiredWorkplaceEducation());
			pointsControl_education.setPosition((int) (dlgX + 20 + town.gameFont.layout.width + 37), dlgY + dialogH - 132);
			town.gameGui.editorSpriteBatch.end();
			pointsControl_education.setBeginShapeRenderer(true);
			pointsControl_education.render(0, 0);
			town.gameGui.editorSpriteBatch.begin();
			
			dlgFont.draw(town.gameGui.editorSpriteBatch, "" + sworker, dlgX + 43, dlgY + dialogH - 148);
			
			guiinfo_CompanyFoodFillingByWorkerObject_worker.render(x, libgdxy,
					dlgX + 19, dlgY + dialogH - 164);
			
			//dlgFont.draw(town.gameGui.editorSpriteBatch, "Refill at 50%",dlgX + 21, dlgY + dialogH - 176);
			town.gameGui.editorSpriteBatch.end();
			
			Vector3 vdpos = town.gameWorld.markerObject.getScreenPosition(
					town.gameWorld.markerObject.width / 2,
					(int) (town.gameWorld.markerObject.height / 2));
			buttonEdit_Worker.setDuplicatePosition((int) vdpos.x
					- getDuplicateSize(), (int) vdpos.y + getDuplicateSize());
			buttonEdit_Worker.setDuplicateSize(getDuplicateSize(),
					getDuplicateSize());
			buttonEdit_Worker.renderDuplicateOnly(x, libgdxy);

			buttonClear_Worker.setDuplicatePosition((int) vdpos.x
					- getDuplicateSize() * 2 - 4, (int) vdpos.y
					+ getDuplicateSize());
			buttonClear_Worker.setDuplicateSize(getDuplicateSize(),
					getDuplicateSize());
			buttonClear_Worker.renderDuplicateOnly(x, libgdxy);
			
			town.gameGui.editorSpriteBatch.begin();
			
			//Skill
			if (mobj.theobject.workplaceHasSkill() && mobj.worker != null) 
			{
				int exp = 0;
				
				int objid=mobj.theobject.getSkillObjectId();
				
				if (mobj.worker.thehuman.jobSkillLevel.containsKey(objid))
					exp = Math.round(mobj.worker.thehuman.jobSkillLevel.get(objid).fskill);
				
				String skilllevelname = mobj.worker.thehuman.getJobSkillLevelName(objid);
				guiinfo_experience.setTooltip(mobj.getCompanyWorkingPlaceJobTitle(0) + " Skill Level");
				town.gameFont.layout.setText(dlgFont, sworker);
				
				//guiinfo_experience.setctrlSpriteBatch(town.gameWorld.gameGui.editorSpriteBatch);
				//guiinfo_experience.beginSpriteBatch=true;
				
				int skilly=2;
				
				guiinfo_experience.render(x, libgdxy, (int) (dlgX + 54
						+ town.gameFont.layout.width + 10-5), dlgY + dialogH
						- 164 + 1 + skilly);
				
				dlgFont.draw(town.gameGui.editorSpriteBatch, ""
						+ exp + " (" + skilllevelname + ")", (int) (dlgX
						+ 54 + town.gameFont.layout.width + 10 + 25-5), dlgY
						+ dialogH - 150 + skilly);
			}
		}
		
		
		
		
		//*********
		// Warnings
		//*********
		if (mobj.theobject.getNeededWorkinputPerHour() > 0 && !mobj.onlineByWorkInput) {
			swarnings += "not enough office work output, ";
		}
		
		if (mobj.theobject.editoraction
				.contains("company_school_workingplace_studentsdesk")
				&& !mobj.bStudentDeskHasTeachersDesk) {
			swarnings += "no teacher, ";
		}
		
		if (mobj.theobject.editoraction
				.contains("company_college_workingplace_studentsdesk")
				&& !mobj.bStudentDeskHasTeachersDesk) {
			swarnings += "no professor, ";
		}
		
		if (mobj.theobject.editoraction
				.contains("company_college_workingplace_researchlab")
				&& mobj.researchObject == null) {
			swarnings += "no research project, ";
		}
		
		town.gameGui.editorSpriteBatch.end();
		for (CGuiInfo guiinfo : listGuiInfo_Company) {
			guiinfo.renderTooltip(x, libgdxy);
		}
		
		town.gameGui.editorSpriteBatch.begin();
	}
	
	public int getDuplicateSize() {
//		int dsize = (int) (40 - town.gameCam.zoom * 3);
//		
//		if (dsize < 25)
//			dsize = 25;
//		
//		return 25;
		
		return 30;
	}
	
	public void render_laundryobject(int x, int libgdxy)
	{
		CWorldObject mobj = town.gameWorld.markerObject;
		
		if(mobj.theobject.editoraction.contains("laundry"))
		{
			if (town.gameWorld.markerObject.getObjectFillingMultiMax() > 0)
			{
				guiinfo_food.render(x, libgdxy, dlgX + 400, dlgY + dialogH - 134);
				
				dlgFont.draw(
						town.gameGui.editorSpriteBatch,
						town.gameWorld.markerObject.getObjectFillingMulti()
								+ "/"
								+ town.gameWorld.markerObject
										.getObjectFillingMultiMax(), dlgX + 435,
						dlgY + dialogH - 120);
				
				town.gameGui.editorSpriteBatch.end();
				
				/*
				String str1="";
				if(mobj.theobject.editoraction.contains("dryer")){
					for(CWorldObject o1 : mobj.theaddress.listWorldObjects) {
						str1+=", " + o1.theobject.editoraction;
					}
					Gdx.app.log(""+mobj.theaddress.addressName, ""+str1);
				}
				*/
				
				guiinfo_food.setTooltip(town.gameWorld.markerObject.getObjectFillingText());
				guiinfo_food.renderTooltip(x, libgdxy);
				town.gameGui.editorSpriteBatch.begin();
			}
		}
	}
	
	public void render_responsible(int x, int libgdxy)
	{
		CWorldObject mobj = town.gameWorld.markerObject;
		
		if (mobj.theobject.editoraction.contains("laundrybasket")) 
		{
			String sworker = " - ";
			if (mobj.worker != null)
				sworker = mobj.worker.thehuman.getName();
			
			dlgFont.draw(town.gameGui.editorSpriteBatch, address, dlgX + 20, dlgY + dialogH - 40 - 20);
			
			guiinfo_worker.render(x, libgdxy, dlgX + 19, dlgY + dialogH - 166);
			
			dlgFont.draw(town.gameGui.editorSpriteBatch,
					"Responsible Resident", dlgX + 22, dlgY + dialogH - 120);
			dlgFont.draw(town.gameGui.editorSpriteBatch,
					"" + sworker, dlgX + 50, dlgY + dialogH - 151);
			
			town.gameGui.editorSpriteBatch.end();
						
			guiinfo_worker.setTooltip("Responsible Resident");
			guiinfo_worker.renderTooltip(x, libgdxy);
			
			Vector3 vdpos = mobj.getScreenPosition(mobj.width / 2, (int) (mobj.height / 2));
			
			buttonClear_Worker.setTooltip("Remove Resident");
			buttonClear_Worker.setDuplicatePosition((int) vdpos.x
					- getDuplicateSize() * 3 - 4, (int) vdpos.y
					+ getDuplicateSize());
			buttonClear_Worker.setDuplicateSize(getDuplicateSize(),
					getDuplicateSize());
			buttonClear_Worker.renderDuplicateOnly(x, libgdxy);
			
			if (mobj.theobject.editoraction.contains("laundrybasket"))
			{
				buttonEdit_Worker.setTooltip("Choose Resident");
				buttonEdit_Worker.setDuplicatePosition((int) vdpos.x
						- getDuplicateSize()*2, (int) vdpos.y
						+ getDuplicateSize());
				buttonEdit_Worker.setDuplicateSize(getDuplicateSize(),
						getDuplicateSize());
				buttonEdit_Worker.renderDuplicateOnly(x, libgdxy);
			}
			
			town.gameGui.editorSpriteBatch.begin();
		}
		else
		{
			//Vector3 vdpos = mobj.getScreenPosition(mobj.width / 2, (int) (mobj.height / 2));
			
			//..
			
			//dlgFont.draw(town.gameWorld.gameGui.editorSpriteBatch, mobj.getWorkingHoursString(), dlgX + 50, dlgY + dialogH - 180-2);
			//gui_worktime.render(x, libgdxy, dlgX + 16, dlgY + dialogH - 194-3);

			//gui_worktime.setTooltip("Work Time");
			//gui_worktime.renderTooltip(x, libgdxy);
			
			//buttonEdit_Worktime.setTooltip("Edit Work Time");
			//buttonEdit_Worktime.setDuplicatePosition((int) vdpos.x
			//		- getDuplicateSize() - getDuplicateSize() - 4,
			//		(int) vdpos.y + getDuplicateSize());
			//buttonEdit_Worktime.setDuplicateSize(getDuplicateSize(),
			//		getDuplicateSize());
			//buttonEdit_Worktime.renderDuplicateOnly(x, libgdxy);
		}
	}
	
	public void render_foodfillingbyworker(int x, int libgdxy) {
		
		CWorldObject mobj = town.gameWorld.markerObject;
		
		if (mobj.isFoodFillingByWorkerObject()) {
			String sworker = " - ";
			if (mobj.worker != null)
				sworker = mobj.worker.thehuman.getName();
			
			// ************
			// Foodfilling
			// ************
			
			// Fridge
			if (town.gameWorld.markerObject.getObjectFillingMultiMax() > 0) {
				guiinfo_food.render(x, libgdxy, dlgX + 400, dlgY + dialogH - 134);
				dlgFont.draw(
						town.gameGui.editorSpriteBatch,
						town.gameWorld.markerObject.getObjectFillingMulti()
								+ "/"
								+ town.gameWorld.markerObject.getObjectFillingMultiMax(), dlgX + 435, dlgY + dialogH - 120);
			}
			
			guiinfo_worker.render(x, libgdxy, dlgX + 19, dlgY + dialogH - 166);
			
			dlgFont.draw(town.gameGui.editorSpriteBatch,
					"Responsible Resident", dlgX + 22, dlgY + dialogH - 120);
			dlgFont.draw(town.gameGui.editorSpriteBatch,
					"" + sworker, dlgX + 42, dlgY + dialogH - 151);
			dlgFont.draw(town.gameGui.editorSpriteBatch,
					"Buy in at 50%", dlgX + 22, dlgY + dialogH - 180);
			
			town.gameGui.editorSpriteBatch.end();
			
			guiinfo_worker.setTooltip("Responsible Resident");
			guiinfo_worker.renderTooltip(x, libgdxy);
			guiinfo_food.renderTooltip(x, libgdxy);
			
			Vector3 vdpos = town.gameWorld.markerObject.getScreenPosition(
					town.gameWorld.markerObject.width / 2,
					(int) (town.gameWorld.markerObject.height / 2));
			buttonEdit_Worker.setDuplicatePosition((int) vdpos.x
					- getDuplicateSize(), (int) vdpos.y + getDuplicateSize());
			buttonEdit_Worker.setDuplicateSize(getDuplicateSize(),
					getDuplicateSize());
			buttonEdit_Worker.renderDuplicateOnly(x, libgdxy);
			
			if(mobj.getObjectFillingMulti()==0 && mobj.bObjectIsReady) 
			{
				//int fprice=2850;
				int fprice=town.fillFridgeCost*town.gameWorld.worldTime.getCurrentDay();
				
				buttonFill.setTooltip("Fill Fridge ($"+fprice+")");
				
				if(town.gameWorld.townMoney < fprice)
				{
					buttonFill.buttonColor=Color.DARK_GRAY;
					buttonFill.setTooltip("Fill Fridge ($"+fprice+") - NOT ENOUGH MONEY");
				}
				else
				{
					buttonFill.buttonColor=Color.WHITE;
				}
								
				buttonFill.setDuplicatePosition((int) vdpos.x + 4
						, (int) vdpos.y
						+ getDuplicateSize());
				buttonFill.setDuplicateSize(getDuplicateSize(),
						getDuplicateSize());
				buttonFill.renderDuplicateOnly(x, libgdxy);
			}
			
			
			buttonClear_Worker.setDuplicatePosition((int) vdpos.x
					- getDuplicateSize() * 2 - 4, (int) vdpos.y
					+ getDuplicateSize());
			buttonClear_Worker.setDuplicateSize(getDuplicateSize(),
					getDuplicateSize());
			buttonClear_Worker.renderDuplicateOnly(x, libgdxy);
			town.gameGui.editorSpriteBatch.begin();
		}
	}
	
	public void renderDinnerTable(int x, int libgdxy) {
		CWorldObject mobj = town.gameWorld.markerObject;
		
		if (town.gameWorld.markerObject.theaddress != null)
			address = town.gameWorld.markerObject.theaddress.addressName;
		else
			address = "";
		
		dlgFont.draw(town.gameGui.editorSpriteBatch, address,
				dlgX + 20, dlgY + dialogH - 60);
		
		String sowner = " - ";
		String sowner2 = " - ";
		String sowner3 = " - ";
		String sowner4 = " - ";
		String sowner5 = " - ";
		String sowner6 = " - ";
		String sowner7 = " - ";
		String sowner8 = " - ";
		String scook = " - ";
		String sdiningtime1 = " - ";
		String sdiningtime2 = " - ";
		String sdiningtime3 = " - ";
		
		int seatcount = 4;
		if (mobj.theobject.editoraction.contains("_count6"))
			seatcount = 6;
		if (mobj.theobject.editoraction.contains("_count8"))
			seatcount = 8;
		if (mobj.theobject.editoraction.contains("_count2"))
			seatcount = 2;
		if (mobj.owner != null)
			sowner = mobj.owner.thehuman.getName();
		if (mobj.owner2 != null)
			sowner2 = mobj.owner2.thehuman.getName();
		if (mobj.owner3 != null)
			sowner3 = mobj.owner3.thehuman.getName();
		if (mobj.owner4 != null)
			sowner4 = mobj.owner4.thehuman.getName();
		if (mobj.owner5 != null)
			sowner5 = mobj.owner5.thehuman.getName();
		if (mobj.owner6 != null)
			sowner6 = mobj.owner6.thehuman.getName();
		if (mobj.owner7 != null)
			sowner7 = mobj.owner7.thehuman.getName();
		if (mobj.owner8 != null)
			sowner8 = mobj.owner8.thehuman.getName();
		
		if (mobj.worker != null)
			scook = mobj.worker.thehuman.getName();
		
		sdiningtime1 = CHelper.getAMPMHourText(mobj.actiontime1);
		sdiningtime2 = CHelper.getAMPMHourText(mobj.actiontime2);
		sdiningtime3 = CHelper.getAMPMHourText(mobj.actiontime3);
		if (sdiningtime1 == "-1")
			sdiningtime1 = " - ";
		if (sdiningtime2 == "-1")
			sdiningtime2 = " - ";
		if (sdiningtime3 == "-1")
			sdiningtime3 = " - ";
		
		sowner = town.gameFont.shortenStringToWidth(dlgFont, sowner, 150);
		sowner2 = town.gameFont.shortenStringToWidth(dlgFont, sowner2, 150);
		sowner3 = town.gameFont.shortenStringToWidth(dlgFont, sowner3, 150);
		sowner4 = town.gameFont.shortenStringToWidth(dlgFont, sowner4, 150);
		sowner5 = town.gameFont.shortenStringToWidth(dlgFont, sowner5, 150);
		sowner6 = town.gameFont.shortenStringToWidth(dlgFont, sowner6, 150);
		sowner7 = town.gameFont.shortenStringToWidth(dlgFont, sowner7, 150);
		sowner8 = town.gameFont.shortenStringToWidth(dlgFont, sowner8, 150);
		
		scook = town.gameFont.shortenStringToWidth(dlgFont, scook, 140);
		
		guiinfo_seatowner1.render(x, libgdxy, dlgX + 19, dlgY + dialogH - 135);
		dlgFont.draw(town.gameGui.editorSpriteBatch, sowner,
				dlgX + 45, dlgY + dialogH - 120);
		
		guiinfo_seatowner2.render(x, libgdxy, dlgX + 19, dlgY + dialogH - 165);
		dlgFont.draw(town.gameGui.editorSpriteBatch, sowner2,
				dlgX + 45, dlgY + dialogH - 150);
		
		if (seatcount > 2) {
			guiinfo_seatowner3.render(x, libgdxy, dlgX + 19 + 200, dlgY
					+ dialogH - 135);
			dlgFont.draw(town.gameGui.editorSpriteBatch, sowner3,
					dlgX + 45 + 200, dlgY + dialogH - 120);

			guiinfo_seatowner4.render(x, libgdxy, dlgX + 19 + 200, dlgY
					+ dialogH - 165);
			dlgFont.draw(town.gameGui.editorSpriteBatch, sowner4,
					dlgX + 45 + 200, dlgY + dialogH - 150);
		}
		if (seatcount == 6) {
			guiinfo_seatowner5.render(x, libgdxy, dlgX + 19, dlgY + dialogH
					- 195);
			dlgFont.draw(town.gameGui.editorSpriteBatch, sowner5,
					dlgX + 45, dlgY + dialogH - 180);

			guiinfo_seatowner6.render(x, libgdxy, dlgX + 19 + 200, dlgY
					+ dialogH - 195);
			dlgFont.draw(town.gameGui.editorSpriteBatch, sowner6,
					dlgX + 45 + 200, dlgY + dialogH - 180);
		}
		
		if (seatcount == 8) 
		{
			int ydelta=60;
			
			guiinfo_seatowner5.render(x, libgdxy, dlgX + 19, dlgY + dialogH - 135-ydelta);
			dlgFont.draw(town.gameGui.editorSpriteBatch, sowner5,
					dlgX + 45, dlgY + dialogH - 120-ydelta);
			
			guiinfo_seatowner6.render(x, libgdxy, dlgX + 19, dlgY + dialogH - 165-ydelta);
			dlgFont.draw(town.gameGui.editorSpriteBatch, sowner6,
					dlgX + 45, dlgY + dialogH - 150-ydelta);
				
			guiinfo_seatowner7.render(x, libgdxy, dlgX + 19 + 200, dlgY
					+ dialogH - 135-ydelta);
			dlgFont.draw(town.gameGui.editorSpriteBatch, sowner7,
					dlgX + 45 + 200, dlgY + dialogH - 120-ydelta);
			
			guiinfo_seatowner8.render(x, libgdxy, dlgX + 19 + 200, dlgY
					+ dialogH - 165-ydelta);
			dlgFont.draw(town.gameGui.editorSpriteBatch, sowner8,
					dlgX + 45 + 200, dlgY + dialogH - 150-ydelta);
		}		
		
		int posx = 400;
		
		//Dinner Times
		guiinfo_dinnertime1.render(x, libgdxy, dlgX + 19 + posx, dlgY + dialogH
				- 135);
		dlgFont.draw(town.gameGui.editorSpriteBatch, sdiningtime1,
				dlgX + 45 + posx + 6, dlgY + dialogH - 120);

		guiinfo_dinnertime2.render(x, libgdxy, dlgX + 19 + posx, dlgY + dialogH
				- 135 - 30);
		dlgFont.draw(town.gameGui.editorSpriteBatch, sdiningtime2,
				dlgX + 45 + posx + 6, dlgY + dialogH - 120 - 30);

		guiinfo_dinnertime3.render(x, libgdxy, dlgX + 19 + posx, dlgY + dialogH
				- 135 - 60);
		dlgFont.draw(town.gameGui.editorSpriteBatch, sdiningtime3,
				dlgX + 45 + posx + 6, dlgY + dialogH - 120 - 60);

		
		
		
		//Cook
		int cook_posy=8;
		guiinfo_workplace_education.render(x, libgdxy, Math.round(dlgX + 20	+ 42 + 10+425),	dlgY + dialogH - 132+88+cook_posy);
		
		guiinfo_cook.render(x, libgdxy, dlgX + 19 + posx-2+1, dlgY + dialogH - 135 + 70 - 10 + 30+cook_posy);
		dlgFont.draw(town.gameGui.editorSpriteBatch, "Cook", dlgX + 45 + posx + 6, dlgY + dialogH - 60 + 27+cook_posy);
				
		guiinfo_worker.render(x, libgdxy, dlgX + 19 + posx+1, dlgY + dialogH - 135 + 70 - 10+cook_posy);
		dlgFont.draw(town.gameGui.editorSpriteBatch, scook, dlgX + 45 + posx + 6, dlgY + dialogH - 60-2+cook_posy);
		
		
		//cook skill level
		if(mobj.worker!=null)
		{
			int exp = 0;
			int objid = mobj.theobject.getSkillObjectId();
			if (mobj.worker.thehuman.jobSkillLevel.containsKey(objid))
				exp = Math.round(mobj.worker.thehuman.jobSkillLevel.get(objid).fskill);
			String skilllevelname = mobj.worker.thehuman.getJobSkillLevelName(objid);
			guiinfo_experience.setTooltip(mobj.getCompanyWorkingPlaceJobTitle(0) + " Skill Level");
			guiinfo_experience.render(x, libgdxy, (int) (dlgX + 74+345), dlgY + dialogH - 163+70);
			dlgFont.draw(town.gameGui.editorSpriteBatch, "" + exp + " (" + skilllevelname + ")", (int) (dlgX + 99+345+6), dlgY + dialogH - 150+69);		
		}
		
		
		town.gameGui.editorSpriteBatch.end();
		
		
		
		
		//Required Cook Education
		pointsControl_education.setValue(mobj.theobject.getRequiredWorkplaceEducation());
		pointsControl_education.setPosition((int) (dlgX + 20 + 42 + 37+425), dlgY + dialogH- 132+88+cook_posy);
		pointsControl_education.setBeginShapeRenderer(true);
		pointsControl_education.render(0, 0);
		
		
		
		
		int posy = getDuplicateSize() / 2;
		
		Vector3 vdpos = town.gameWorld.markerObject.getScreenPosition(
				town.gameWorld.markerObject.width / 2,
				town.gameWorld.markerObject.height / 2);
		
		int posx2 = 0;
		int posx2_1 = 0;
		if (seatcount == 2) {
			posx2 = 104;
			posx2_1 = 103;
		}
		
		// Owner 1
		buttonEdit_seatOwner1.setDuplicatePosition((int) vdpos.x
				- getDuplicateSize() * 4 + posx2, (int) vdpos.y
				+ getDuplicateSize() * 4 - posy);
		buttonEdit_seatOwner1.setDuplicateSize(getDuplicateSize(),
				getDuplicateSize());
		buttonEdit_seatOwner1.renderDuplicateOnly(x, libgdxy);
		
		// Owner 2
		buttonEdit_seatOwner2.setDuplicatePosition((int) vdpos.x
				- getDuplicateSize() * 4 + posx2_1, (int) vdpos.y
				- getDuplicateSize() * 4 - posy);
		buttonEdit_seatOwner2.setDuplicateSize(getDuplicateSize(),
				getDuplicateSize());
		buttonEdit_seatOwner2.renderDuplicateOnly(x, libgdxy);
		
		
		if (seatcount > 2 && !mobj.theobject.editoraction.contains("_count8")) {
			// Owner 3
			buttonEdit_seatOwner3.setDuplicatePosition((int) vdpos.x
					+ getDuplicateSize() * 3, (int) vdpos.y
					+ getDuplicateSize() * 4 - posy + 2);
			buttonEdit_seatOwner3.setDuplicateSize(getDuplicateSize(),
					getDuplicateSize());
			buttonEdit_seatOwner3.renderDuplicateOnly(x, libgdxy);
			
			// Owner 4
			buttonEdit_seatOwner4.setDuplicatePosition((int) vdpos.x
					+ getDuplicateSize() * 3, (int) vdpos.y
					- getDuplicateSize() * 4 - posy + 2);
			buttonEdit_seatOwner4.setDuplicateSize(getDuplicateSize(),
					getDuplicateSize());
			buttonEdit_seatOwner4.renderDuplicateOnly(x, libgdxy);
		}
		
		if (mobj.theobject.editoraction.contains("_count6")) {
			// Owner 5
			buttonEdit_seatOwner5.setDuplicatePosition((int) vdpos.x
					- getDuplicateSize() * 6, (int) vdpos.y - posy + 2);
			buttonEdit_seatOwner5.setDuplicateSize(getDuplicateSize(),
					getDuplicateSize());
			buttonEdit_seatOwner5.renderDuplicateOnly(x, libgdxy);

			// Owner 6
			buttonEdit_seatOwner6.setDuplicatePosition((int) vdpos.x
					+ getDuplicateSize() * 5, (int) vdpos.y - posy + 2);
			buttonEdit_seatOwner6.setDuplicateSize(getDuplicateSize(),
					getDuplicateSize());
			buttonEdit_seatOwner6.renderDuplicateOnly(x, libgdxy);
		}
		
		if (mobj.theobject.editoraction.contains("_count8")) {
			
			// Owner 3
			buttonEdit_seatOwner3.setDuplicatePosition((int) vdpos.x
					- getDuplicateSize() * 2 + posx2, (int) vdpos.y
					+ getDuplicateSize() * 4 - posy);
			buttonEdit_seatOwner3.setDuplicateSize(getDuplicateSize(),
					getDuplicateSize());
			buttonEdit_seatOwner3.renderDuplicateOnly(x, libgdxy);
			
			// Owner 4
			buttonEdit_seatOwner4.setDuplicatePosition((int) vdpos.x
					- getDuplicateSize() * 2 + posx2_1, (int) vdpos.y
					- getDuplicateSize() * 4 - posy);
			buttonEdit_seatOwner4.setDuplicateSize(getDuplicateSize(),
					getDuplicateSize());
			buttonEdit_seatOwner4.renderDuplicateOnly(x, libgdxy);

			// Owner 5
			buttonEdit_seatOwner5.setDuplicatePosition((int) vdpos.x
					+ getDuplicateSize(), (int) vdpos.y
					+ getDuplicateSize() * 4 - posy + 2);
			buttonEdit_seatOwner5.setDuplicateSize(getDuplicateSize(),
					getDuplicateSize());
			buttonEdit_seatOwner5.renderDuplicateOnly(x, libgdxy);
			
			// Owner 6
			buttonEdit_seatOwner6.setDuplicatePosition((int) vdpos.x
					+ getDuplicateSize(), (int) vdpos.y
					- getDuplicateSize() * 4 - posy + 2);
			buttonEdit_seatOwner6.setDuplicateSize(getDuplicateSize(),
					getDuplicateSize());
			buttonEdit_seatOwner6.renderDuplicateOnly(x, libgdxy);					
			
			// Owner 7
			buttonEdit_seatOwner7.setDuplicatePosition((int) vdpos.x
					+ getDuplicateSize() * 3, (int) vdpos.y
					+ getDuplicateSize() * 4 - posy + 2);
			buttonEdit_seatOwner7.setDuplicateSize(getDuplicateSize(),
					getDuplicateSize());
			buttonEdit_seatOwner7.renderDuplicateOnly(x, libgdxy);
			
			// Owner 8
			buttonEdit_seatOwner8.setDuplicatePosition((int) vdpos.x
					+ getDuplicateSize() * 3, (int) vdpos.y
					- getDuplicateSize() * 4 - posy + 2);
			buttonEdit_seatOwner8.setDuplicateSize(getDuplicateSize(),
					getDuplicateSize());
			buttonEdit_seatOwner8.renderDuplicateOnly(x, libgdxy);
			
		}
		
		// Cook
		buttonEdit_Cook.setDuplicatePosition((int) vdpos.x + 4 - 1,
				(int) vdpos.y + getDuplicateSize() * 3 - getDuplicateSize() + 7
						- posy + 2);
		buttonEdit_Cook
				.setDuplicateSize(getDuplicateSize(), getDuplicateSize());
		buttonEdit_Cook.renderDuplicateOnly(x, libgdxy);

		buttonClear_Cook.setDuplicatePosition((int) (vdpos.x
				- getDuplicateSize() + 1) - 1, (int) vdpos.y
				+ getDuplicateSize() * 3 - getDuplicateSize() + 7 - posy + 2);
		buttonClear_Cook.setDuplicateSize(getDuplicateSize(),
				getDuplicateSize());
		buttonClear_Cook.renderDuplicateOnly(x, libgdxy);

		int timeposy = 7;
		// Dinner Time 1
		buttonEdit_DinnerTime1.setDuplicatePosition((int) vdpos.x + 4 - 1,
				(int) vdpos.y + getDuplicateSize() * 2 - 7 - getDuplicateSize()
						- getDuplicateSize() + 7 - posy + timeposy);
		buttonEdit_DinnerTime1.setDuplicateSize(getDuplicateSize(),
				getDuplicateSize());
		buttonEdit_DinnerTime1.renderDuplicateOnly(x, libgdxy);

		buttonClear_DinnerTime1.setDuplicatePosition((int) vdpos.x
				- getDuplicateSize() - 1, (int) vdpos.y + getDuplicateSize()
				* 2 - 7 - getDuplicateSize() - getDuplicateSize() + 7 - posy
				+ timeposy);
		buttonClear_DinnerTime1.setDuplicateSize(getDuplicateSize(),
				getDuplicateSize());
		buttonClear_DinnerTime1.renderDuplicateOnly(x, libgdxy);

		// Dinner Time 2
		buttonEdit_DinnerTime2.setDuplicatePosition((int) vdpos.x + 4 - 1,
				(int) vdpos.y + getDuplicateSize() - 7 * 2 - getDuplicateSize()
						- getDuplicateSize() + 7 - posy + timeposy);
		buttonEdit_DinnerTime2.setDuplicateSize(getDuplicateSize(),
				getDuplicateSize());
		buttonEdit_DinnerTime2.renderDuplicateOnly(x, libgdxy);

		buttonClear_DinnerTime2.setDuplicatePosition((int) vdpos.x
				- getDuplicateSize() - 1, (int) vdpos.y + getDuplicateSize()
				- 7 * 2 - getDuplicateSize() - getDuplicateSize() + 7 - posy
				+ timeposy);
		buttonClear_DinnerTime2.setDuplicateSize(getDuplicateSize(),
				getDuplicateSize());
		buttonClear_DinnerTime2.renderDuplicateOnly(x, libgdxy);

		// Dinner Time 3
		buttonEdit_DinnerTime3.setDuplicatePosition((int) vdpos.x + 4 - 1,
				(int) vdpos.y - 7 * 3 - getDuplicateSize() - getDuplicateSize()
						+ 7 - posy + timeposy);
		buttonEdit_DinnerTime3.setDuplicateSize(getDuplicateSize(),
				getDuplicateSize());
		buttonEdit_DinnerTime3.renderDuplicateOnly(x, libgdxy);

		buttonClear_DinnerTime3
				.setDuplicatePosition((int) vdpos.x - getDuplicateSize() - 1,
						(int) vdpos.y - 7 * 3 - getDuplicateSize()
								- getDuplicateSize() + 7 - posy + timeposy);
		buttonClear_DinnerTime3.setDuplicateSize(getDuplicateSize(),
				getDuplicateSize());
		buttonClear_DinnerTime3.renderDuplicateOnly(x, libgdxy);

		// Tooltips
		if(mobj.theobject.editoraction.contains("_count8"))
		{
			guiinfo_seatowner1.renderTooltip(x, libgdxy);
			guiinfo_seatowner2.renderTooltip(x, libgdxy);

			guiinfo_seatowner3.renderTooltip(x, libgdxy);
			guiinfo_seatowner4.renderTooltip(x, libgdxy);

			guiinfo_seatowner5.renderTooltip(x, libgdxy);
			guiinfo_seatowner6.renderTooltip(x, libgdxy);
			
			guiinfo_seatowner7.renderTooltip(x, libgdxy);
			guiinfo_seatowner8.renderTooltip(x, libgdxy);
		}
		else
		{
			guiinfo_seatowner1.renderTooltip(x, libgdxy);
			guiinfo_seatowner2.renderTooltip(x, libgdxy);

			if (seatcount > 2) {
				guiinfo_seatowner3.renderTooltip(x, libgdxy);
				guiinfo_seatowner4.renderTooltip(x, libgdxy);
			}

			if (mobj.theobject.editoraction.contains("_count6")) {
				guiinfo_seatowner5.renderTooltip(x, libgdxy);
				guiinfo_seatowner6.renderTooltip(x, libgdxy);
			}
		}
		
		guiinfo_cook.renderTooltip(x, libgdxy);

		guiinfo_dinnertime1.renderTooltip(x, libgdxy);
		guiinfo_dinnertime2.renderTooltip(x, libgdxy);
		guiinfo_dinnertime3.renderTooltip(x, libgdxy);
		
		guiinfo_experience.renderTooltip(x, libgdxy);
		
		guiinfo_worker.renderTooltip(x, libgdxy);
		guiinfo_workplace_education.renderTooltip(x, libgdxy);
		
		buttonEdit_seatOwner1.renderDuplicateTooltip(x, libgdxy);
		buttonEdit_seatOwner2.renderDuplicateTooltip(x, libgdxy);
		
		if (seatcount > 2) {
			buttonEdit_seatOwner3.renderDuplicateTooltip(x, libgdxy);
			buttonEdit_seatOwner4.renderDuplicateTooltip(x, libgdxy);
		}
		if (mobj.theobject.editoraction.contains("_count6")) {
			buttonEdit_seatOwner5.renderDuplicateTooltip(x, libgdxy);
			buttonEdit_seatOwner6.renderDuplicateTooltip(x, libgdxy);
		}
		
		if (mobj.theobject.editoraction.contains("_count8")) {
			buttonEdit_seatOwner7.renderDuplicateTooltip(x, libgdxy);
			buttonEdit_seatOwner8.renderDuplicateTooltip(x, libgdxy);
		}
		
		buttonEdit_Cook.renderDuplicateTooltip(x, libgdxy);
		buttonClear_Cook.renderDuplicateTooltip(x, libgdxy);
		
		buttonEdit_DinnerTime1.renderDuplicateTooltip(x, libgdxy);
		buttonClear_DinnerTime1.renderDuplicateTooltip(x, libgdxy);
		
		buttonEdit_DinnerTime2.renderDuplicateTooltip(x, libgdxy);
		buttonClear_DinnerTime2.renderDuplicateTooltip(x, libgdxy);
		
		buttonEdit_DinnerTime3.renderDuplicateTooltip(x, libgdxy);
		buttonClear_DinnerTime3.renderDuplicateTooltip(x, libgdxy);
		
		town.gameGui.editorSpriteBatch.begin();
	}
	
	public void renderOwnerObject(int x, int libgdxy) {
		// dlgFont.draw(town.gameWorld.gameGui.editorSpriteBatch, "Address: " +
		// address, dlgX+20, dlgY+dlgH-250);
		
		CWorldObject mobj = town.gameWorld.markerObject;
		
		int iownercount = mobj.isOwnerObject();
		if (iownercount > 0) {
			String sowner = " - ";
			String sowner2 = " - ";
			if (mobj.owner != null)
				sowner = mobj.owner.thehuman.getName();
			if (mobj.owner2 != null)
				sowner2 = mobj.owner2.thehuman.getName();
			
			if(mobj.theobject.editoraction.contains("traffic_car"))
			{
				guiinfo_owner1.render(libgdxy, libgdxy, dlgX + 19, dlgY + dialogH - 135-30);
				dlgFont.draw(town.gameGui.editorSpriteBatch, sowner, dlgX + 45, dlgY + dialogH - 120-30);
			}
			else
			{
				guiinfo_owner1.render(libgdxy, libgdxy, dlgX + 19, dlgY + dialogH - 135);
				dlgFont.draw(town.gameGui.editorSpriteBatch, sowner, dlgX + 45, dlgY + dialogH - 120);
			}
			
			if (iownercount > 1) {
				guiinfo_owner2.render(libgdxy, libgdxy, dlgX + 19, dlgY
						+ dialogH - 165);
				dlgFont.draw(town.gameGui.editorSpriteBatch, sowner2,
						dlgX + 45, dlgY + dialogH - 150);
			}
			
			town.gameGui.editorSpriteBatch.end();
			
			Vector3 vdpos = town.gameWorld.markerObject.getScreenPosition(0,
					(int) (town.gameWorld.markerObject.height + 10));
			buttonEdit_Owner.setPosition(dlgX + 19, dlgY + dialogH - 137);
			buttonEdit_Owner.setDuplicatePosition((int) vdpos.x
					- getDuplicateSize(), (int) vdpos.y);
			buttonEdit_Owner.setDuplicateSize(getDuplicateSize(),
					getDuplicateSize());
			buttonEdit_Owner.renderDuplicateOnly(x, libgdxy);
			
			if (iownercount > 1) 
			{
				Vector3 vdpos2 = town.gameWorld.markerObject.getScreenPosition((int) (town.gameWorld.markerObject.width), (int) (town.gameWorld.markerObject.height + 10));
				buttonEdit_Owner2.setPosition(dlgX + 19, dlgY + dialogH - 157);
				buttonEdit_Owner2.setDuplicatePosition((int) vdpos2.x + 5, (int) vdpos2.y);
				buttonEdit_Owner2.setDuplicateSize(getDuplicateSize(), getDuplicateSize());
				buttonEdit_Owner2.renderDuplicateOnly(x, libgdxy);
			}
			
			guiinfo_owner1.renderTooltip(x, libgdxy);
			
			if (iownercount > 1)
				guiinfo_owner2.renderTooltip(x, libgdxy);
			
			town.gameGui.editorSpriteBatch.begin();
		}
	}
	
	public void renderBookshelf(int x, int libgdxy) {
		guiinfo_bookshelf_education.render(x, libgdxy, Math.round(dlgX + 20),
				dlgY + dialogH - 132);

		town.gameGui.editorSpriteBatch.end();

		pointsControl_education.setValue(town.gameWorld.markerObject.theobject
				.getObjectEducation());
		pointsControl_education.setPosition((int) (dlgX + 20 + 27), dlgY
				+ dialogH - 132);
		pointsControl_education.setBeginShapeRenderer(true);
		pointsControl_education.render(0, 0);

		town.gameGui.editorSpriteBatch.begin();

		guiinfo_bookshelf_education.renderTooltip(x, libgdxy);
	}
	
	public void renderRadiator(int x, int libgdxy) {
		/*
		CWorldObject mobj = town.gameWorld.markerObject;

		if (mobj.theobject.getRadiatorHeatingPower() > 0) {
			guiinfo_heatingpower.render(x, libgdxy, dlgX + 20, dlgY + dialogH
					- 134);
			dlgFont.draw(town.gameGui.editorSpriteBatch,
					mobj.theobject.getRadiatorHeatingPower() + "", dlgX + 49,
					dlgY + dialogH - 120);
			town.gameGui.editorSpriteBatch.end();
			guiinfo_heatingpower.renderTooltip(x, libgdxy);
			town.gameGui.editorSpriteBatch.begin();
		}
		*/
	}
	
	public void renderLight(int x, int libgdxy) {
		CWorldObject mobj = town.gameWorld.markerObject;
		
		if (mobj.theobject.getLightPower() > 0) {
			dlgFont.setColor(dlgFontColor);
			// dlgFont.draw(town.gameWorld.gameGui.editorSpriteBatch,
			// "Brightness: " + mobj.theobject.getLightPower(), dlgX+20,
			// dlgY+dlgH-120);
			guiinfo_brightness.render(x, libgdxy, dlgX + 15, dlgY + dialogH - 139);
			town.gameGui.editorSpriteBatch.setColor(Color.WHITE);
			// town.gameWorld.gameGui.editorSpriteBatch.draw(town.gameResourceConfig.textures.get("guiinfo_brightness"),
			// dlgX+15, dlgY+dlgH-137, 23, 23);
			dlgFont.draw(town.gameGui.editorSpriteBatch, mobj.theobject.getLightPower() + "", dlgX + 50, dlgY + dialogH - 119);
			town.gameGui.editorSpriteBatch.end();
			guiinfo_brightness.renderTooltip(x, libgdxy);
			town.gameGui.editorSpriteBatch.begin();
		}
		
		// mobj.gameWorld.worldSpriteBatch.begin();
		// Vector2 v2 = CHelper.moveVectorByRotationS2D(mobj.pos_x(),
		// mobj.pos_y(), mobj.width/2, mobj.height-mobj.radiatorRange/2,
		// mobj.width/2, mobj.height/2, mobj.rotation());
		// Texture wrect =
		// mobj.gameWorld.gameResourceConfig.textures.get("poly_whiterect");
		// mobj.gameWorld.worldSpriteBatch.setColor(0.4f, 0, 0, 0.3f);
		// mobj.gameWorld.worldSpriteBatch.draw(wrect,
		// v2.x-mobj.radiatorRange/2, v2.y-mobj.radiatorRange/2,
		// mobj.radiatorRange/2, mobj.radiatorRange/2, mobj.radiatorRange,
		// mobj.radiatorRange, 1, 1, mobj.rotation(), 0, 0, wrect.getWidth(),
		// wrect.getHeight(), false, false);
		// mobj.gameWorld.worldSpriteBatch.end();
	}
	
	public void render_marker() {
		
		CWorldObject mobj = town.gameWorld.markerObject;
		shapeRenderer.setProjectionMatrix(town.gameCam.combined);
		shapeRenderer.setAutoShapeType(true);
		
		Gdx.gl.glEnable(GL30.GL_BLEND);
		Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
		float bs = town.gameWorld.markerObject.getBodySizeByAge();
		
		shapeRenderer.begin();
		
		int w = mobj.width;
		int h = mobj.height;
		int s = h;
		if (w > h)
			s = w;
		
		int lsize = s * 2;
		lsize = lsize / 3;
		
		if (mobj.theobject.editoraction.contains("supermarket_shelf"))
			lsize = lsize - 80;
		
		shapeRenderer.setColor(1, 1, 1, 0.11f);
		shapeRenderer.set(ShapeType.Filled);
		shapeRenderer.rect((float) mobj.pos_x()
				+ (mobj.width * bs) / 2 - lsize * bs,
				(float) mobj.pos_y()
						+ (mobj.height * bs) / 2 - lsize
						* bs, 0, 0, (float) lsize * 2 * bs, (float) lsize * 2
						* bs, 1f, 1f, 0);
		
		shapeRenderer.setColor(0.4f, 0.4f, 0.4f, 0.9f);
		shapeRenderer.set(ShapeType.Line);
		for (int i = 1; i < 5; i++) {
			shapeRenderer.rect(
					(float) mobj.pos_x()
							+ (mobj.width * bs) / 2
							- lsize * bs,
					(float) mobj.pos_y()
							+ (mobj.height * bs) / 2
							- lsize * bs, 0, 0, (float) lsize * 2 * bs,
					(float) lsize * 2 * bs, 1f, 1f, 0);
		}
		
		
		mobj.renderDefenseZone(shapeRenderer);
		
//		if (mobj.theobject.editoraction.contains("illuminati_defensesystem"))
//		{
//			shapeRenderer.setColor(0.0f, 0.4f, 0.0f, 0.5f);
//			shapeRenderer.set(ShapeType.Filled);
//			shapeRenderer.circle(mobj.pos_x()+mobj.width/2, mobj.pos_y()+mobj.height/2, mobj.getDefense_reichweite());
//		}
//		
//		if (mobj.theobject.editoraction.contains("illuminati_defensewarning") && !mobj.bDefenseAlarm)
//		{
//			shapeRenderer.setColor(0.0f, 0.4f, 0.0f, 0.5f);
//			shapeRenderer.set(ShapeType.Filled);
//			shapeRenderer.circle(mobj.pos_x()+mobj.width/2, mobj.pos_y()+mobj.height/2, mobj.getDefense_reichweite());
//		}
		
		shapeRenderer.end();
		town.gameGui.setGUICamera();
		
		//Gdx.gl.glDisable(GL30.GL_BLEND);
	}
	
	public void render(int x, int libgdxy) 
	{
		
		if(town.gameWorld.markerObject!=null) //Debug Workoutput
		{
			/*
			CGuidialog_objectinfo

			- town.gameWorld.worldCompanyList
			- belongsToCompany
			- belongsToCompanyId
			
			town.gameWorld.markerObject

			-> für ausgewähltes objekt loggen
			
			Gdx.app.debug("", "log obj: "+obj.theobject.editoraction);
			*/
			
			//es wird für jedes objekt neue company erstellt
			
			//Gdx.app.setLogLevel(10);
			
			//Gdx.app.debug("", "companies: " + town.gameWorld.worldCompanyList.size());
			//Gdx.app.debug("", "companyid: "+town.gameWorld.markerObject.belongsToCompanyId + ", " + town.gameWorld.markerObject.belongsToCompany.companyId);
			
			
			
			//..
		}
		
		
		showDlg(true);
		initInfo();
		
		if(town.bDevMode && town.gameWorld.markerObject!=null && town.gameWorld.markerObject.belongsToCompany!=null && Gdx.input.isKeyJustPressed(Keys.PLUS))
		{
			town.gameWorld.markerObject.belongsToCompany.addWorkOutput(1000, WorkoutputType.DEFAULT);
			town.gameWorld.markerObject.belongsToCompany.addWorkOutput(1000, WorkoutputType.FINANCE);
			town.gameWorld.markerObject.belongsToCompany.addWorkOutput(1000, WorkoutputType.OTHER);
			town.gameWorld.markerObject.belongsToCompany.addWorkOutput(1000, WorkoutputType.POPULATION);
		}
		
		if (town.gameWorld.markerObject == null) 
		{
			//*************
			//Show News Log
			//*************
			if(town.gameWorld.infoEvents.size()>0)
			{
				int maxlength=0;
				town.gameFont.bfArial.getData().setScale(0.6f);
				
				int showcount=0;
				for(int i=0;i<town.gameWorld.infoEvents.size();i++)
				{
					CInfoTextEvent ev1 = town.gameWorld.infoEvents.get(i);
					if(ev1.posy>0)
						continue;
					
					String str1 = ev1.showText + " " + ev1.showText2;
					town.gameFont.layout.setText(town.gameFont.bfArial, str1);
					if(town.gameFont.layout.width>maxlength)
						maxlength=(int) town.gameFont.layout.width;
					
					showcount++;
				}
				
				//dialogH=11+town.gameWorld.infoEvents.size()*30;
				dialogH=11+showcount*30;
				dialogW=maxlength+20;
				setMiddlePositionHorz();
				
				if(dialogW>20)
				{
					shapeRenderer_gui.setAutoShapeType(true);
					
					//if(!Gdx.gl.glIsEnabled(GL30.GL_BLEND))
					{
						Gdx.gl.glEnable(GL30.GL_BLEND);
						Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
					}
					shapeRenderer_gui.begin();
					
					shapeRenderer_gui.set(ShapeType.Filled);
					shapeRenderer_gui.setColor(0f,0f,0f,0.7f);
					shapeRenderer_gui.rect(dlgX, dlgY, dialogW, dialogH);
					
					shapeRenderer_gui.set(ShapeType.Line);
					shapeRenderer_gui.setColor(0.2f, 0.2f, 0.2f, 0.7f);
					shapeRenderer_gui.rect(dlgX, dlgY, dialogW, dialogH);
					
					shapeRenderer_gui.end();
					//Gdx.gl.glDisable(GL30.GL_BLEND);
				}
								
				SpriteBatch sb = town.gameGui.editorSpriteBatch;
				sb.begin();
				
				sb.setShader(town.gameFont.fontShader);
				
				int j=0;
				for(int i=0;i<town.gameWorld.infoEvents.size();i++)
				{
					CInfoTextEvent ev1 = town.gameWorld.infoEvents.get(i);
					
					if(ev1.posy>0)
						continue;
					
					String str1 = ev1.showText2 + " " + ev1.showText;
					town.gameFont.layout.setText(town.gameFont.bfArial, str1);
					town.gameFont.bfArial.setColor(ev1.textColor.r, ev1.textColor.g, ev1.textColor.b, 0.7f);
					
					if(ev1.textColor.r>0.5f && ev1.textColor.g>0.5f && ev1.textColor.b<0.5f)
						town.gameFont.bfArial.setColor(ev1.textColor.r, ev1.textColor.g, ev1.textColor.b, 0.6f);
					
					if(ev1.textColor.r>0.5f && ev1.textColor.g<0.5f && ev1.textColor.b<0.5f)
						town.gameFont.bfArial.setColor(ev1.textColor.r+0.1f, ev1.textColor.g, ev1.textColor.b, 0.9f);
					
					//Gdx.app.debug("", "text: " + str1 + ", y: " + ev1.posy + ", set: " + dlgY+30+j*30);
					
					j++;
					//town.gameFont.bfArial.draw(sb, str1, dlgX+dialogW/2-town.gameFont.layout.width/2-2, dlgY+30+j*30);
					town.gameFont.bfArial.draw(sb, str1, dlgX+dialogW/2-town.gameFont.layout.width/2-2, dlgY+j*30);
				}
				sb.setShader(null);
				sb.end();
			}
			
			
			//**********************************************
			//Chart Test für Town Hall Population Attributes
			//**********************************************
			Boolean bShowChart=false;
			if(bShowChart)
			{
				if(town.gameGui.editorSpriteBatch.isDrawing())
					town.gameGui.editorSpriteBatch.end();
				
				int attrw=Gdx.graphics.getWidth()-200;
				int attrh=Gdx.graphics.getHeight()-200;
				
				int attrx=100;
				int attry=100;
				
				shapeRenderer_gui.setAutoShapeType(true);
				//if(!Gdx.gl.glIsEnabled(GL30.GL_BLEND))
				{
					Gdx.gl.glEnable(GL30.GL_BLEND);
					Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
				}
				Gdx.gl.glEnable(GL20.GL_LINEAR_MIPMAP_LINEAR);
				if(!shapeRenderer.isDrawing())
					shapeRenderer_gui.begin();
					
					shapeRenderer_gui.set(ShapeType.Filled);
					shapeRenderer_gui.setColor(new Color(0,0,0,0.4f));
					shapeRenderer_gui.rect(attrx, attry, attrw, attrh);
					
					shapeRenderer_gui.set(ShapeType.Line);
					shapeRenderer_gui.setColor(0.2f, 0.2f, 0.2f, 0.7f);
					shapeRenderer_gui.rect(attrx, attry, attrw, attrh);
					
					shapeRenderer_gui.set(ShapeType.Point);
					shapeRenderer_gui.setColor(1f, 1f, 1f, 1f);
					for(CStatisticsData_Population po : town.gameWorld.townStatistics.statisticsData_Population)
						shapeRenderer_gui.point(po.day*10, po.happinessAVG*2, 0);
					
					shapeRenderer_gui.end();
				//Gdx.gl.glDisable(GL30.GL_BLEND);
			}
			
			
			//******************************
			//Show Human Physical Attributes
			//******************************
			Boolean bShowAttr=false;
			if(bShowAttr)
			{
				int avgclean = town.gameWorld.townStatistics.getCurrentStatistics_Population().cleanAVG;
				int avgsleep = town.gameWorld.townStatistics.getCurrentStatistics_Population().sleepAVG;
				int avgeat = town.gameWorld.townStatistics.getCurrentStatistics_Population().eatAVG;
				int avgtoilet = town.gameWorld.townStatistics.getCurrentStatistics_Population().toiletAVG;
				
				int attrw=170;
				int attrx=Gdx.graphics.getWidth()/2-attrw*2;
				int attry=10;
				
				float alphaval=0.4f;
				
				if(town.gameGui.editorSpriteBatch.isDrawing())
					town.gameGui.editorSpriteBatch.end();
				shapeRenderer_gui.setAutoShapeType(true);
				//if(!Gdx.gl.glIsEnabled(GL30.GL_BLEND))
				{
					Gdx.gl.glEnable(GL30.GL_BLEND);
					Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
				}
				Gdx.gl.glEnable(GL20.GL_LINEAR_MIPMAP_LINEAR);
				if(!shapeRenderer.isDrawing())
					shapeRenderer_gui.begin();
					shapeRenderer_gui.set(ShapeType.Filled);
					shapeRenderer_gui.setColor(new Color(0,0,0,0.4f));
					shapeRenderer_gui.rect(attrx-14, 0, attrw*4-15, 41);
					
					shapeRenderer_gui.set(ShapeType.Line);
					shapeRenderer_gui.setColor(0.2f, 0.2f, 0.2f, 0.7f);
					shapeRenderer_gui.rect(attrx-14, 0, attrw*4-15, 41);
				
					town.gameGui.renderBalken100(attrx, attry, avgsleep, shapeRenderer_gui, alphaval);
					town.gameGui.renderBalken100(attrx+attrw, attry, avgeat, shapeRenderer_gui, alphaval);
					town.gameGui.renderBalken100(attrx+attrw*2, attry, avgclean, shapeRenderer_gui, alphaval);
					town.gameGui.renderBalken100(attrx+attrw*3, attry, avgtoilet, shapeRenderer_gui, alphaval);
					shapeRenderer_gui.end();
				//Gdx.gl.glDisable(GL30.GL_BLEND);
				
				town.gameGui.guiinfo_sleep.alphaval=0.1f;
				town.gameGui.guiinfo_residentenergy.alphaval=0.1f;
				town.gameGui.guiinfo_eat.alphaval=0.1f;
				town.gameGui.guiinfo_clean.alphaval=0.1f;
				//town.gameGui.guiinfo_clothing.alphaval=0.1f;
				town.gameGui.guiinfo_toilet.alphaval=0.1f;
				
				town.gameGui.editorSpriteBatch.setShader(null);
				town.gameGui.editorSpriteBatch.begin();
				town.gameGui.guiinfo_sleep.render(x, libgdxy, attrx, attry);
				town.gameGui.guiinfo_residentenergy.render(x, libgdxy, attrx, attry);
				town.gameGui.guiinfo_eat.render(x, libgdxy, attrx+attrw, attry);
				town.gameGui.guiinfo_clean.render(x, libgdxy, attrx+attrw*2, attry);
				//town.gameGui.guiinfo_clean.render(x, libgdxy, attrx, attry);
				town.gameGui.guiinfo_toilet.render(x, libgdxy, attrx+attrw*3, attry);
				town.gameGui.editorSpriteBatch.end();
			}
			return;
		}
				
		initInfo();
		
		
		CWorldObject mobj = town.gameWorld.markerObject;
				
		if (mobj.theobject.editoraction.contains("diningroom_diningtable")) 
		{
			if(mobj.owner!=null)
				buttonEdit_seatOwner1.setTooltip("Choose Seat Owner 1 (Current Owner: "+mobj.owner.thehuman.getName()+")");
			if(mobj.owner2!=null)
				buttonEdit_seatOwner2.setTooltip("Choose Seat Owner 2 (Current Owner: "+mobj.owner2.thehuman.getName()+")");
			if(mobj.owner3!=null)
				buttonEdit_seatOwner3.setTooltip("Choose Seat Owner 3 (Current Owner: "+mobj.owner3.thehuman.getName()+")");
			if(mobj.owner4!=null)
				buttonEdit_seatOwner4.setTooltip("Choose Seat Owner 4 (Current Owner: "+mobj.owner4.thehuman.getName()+")");
			if(mobj.owner5!=null)
				buttonEdit_seatOwner5.setTooltip("Choose Seat Owner 5 (Current Owner: "+mobj.owner5.thehuman.getName()+")");
			if(mobj.owner6!=null)
				buttonEdit_seatOwner6.setTooltip("Choose Seat Owner 6 (Current Owner: "+mobj.owner6.thehuman.getName()+")");
			if(mobj.owner7!=null)
				buttonEdit_seatOwner7.setTooltip("Choose Seat Owner 7 (Current Owner: "+mobj.owner7.thehuman.getName()+")");
			if(mobj.owner8!=null)
				buttonEdit_seatOwner8.setTooltip("Choose Seat Owner 8 (Current Owner: "+mobj.owner8.thehuman.getName()+")");
		}		


		
		town.gameGui.tempActionObject = mobj;
		dlgFont.setColor(dlgFontColor);
		dlgFont2.setColor(dlgFontColor);
		swarnings = "";
		
		buttonEdit_ResearchProject.isShowing = false;
		buttonEdit_Paint.isShowing = false;
		
		buttonShow_Statistics.isShowing=false;
		buttonShow_Statistics2.isShowing=false;
		
		buttonAddressClone.isShowing=false;
		buttonAddressPlanning.isShowing=false;
		buttonAddressResize.isShowing=false;
		buttonAddressMove.isShowing=false;
		
		buttonEdit_Worker.isShowing = false;
		buttonClear_Worker.isShowing = false;
		
		buttonEdit_Worker2.isShowing = false;
		buttonClear_Worker2.isShowing = false;
		
		buttonEdit_Worktime.isShowing = false;
		buttonMoveHouse.isShowing = false;
		
		buttonEdit_Owner.isShowing = false;
		buttonEdit_Owner2.isShowing = false;
		
		buttonEdit_seatOwner1.isShowing = false;
		buttonEdit_seatOwner2.isShowing = false;
		buttonEdit_seatOwner3.isShowing = false;
		buttonEdit_seatOwner4.isShowing = false;
		buttonEdit_seatOwner5.isShowing = false;
		buttonEdit_seatOwner6.isShowing = false;
		buttonEdit_seatOwner7.isShowing = false;
		buttonEdit_seatOwner8.isShowing = false;
		
		buttonClear_Cook.isShowing = false;
		buttonEdit_Cook.isShowing = false;
		buttonClear_DinnerTime1.isShowing = false;
		buttonClear_DinnerTime2.isShowing = false;
		buttonClear_DinnerTime3.isShowing = false;
		buttonEdit_DinnerTime1.isShowing = false;
		buttonEdit_DinnerTime2.isShowing = false;
		buttonEdit_DinnerTime3.isShowing = false;
		
		buttonPlus.isShowing=false;
		buttonSave.isShowing=false;
		
		guiinfo_food.setTooltip(mobj.getObjectFillingText());
		guiinfo_worker.setTooltip("Resident");

//			if(1==1)
	//		return;

		
		render_marker();
		

		
		shapeRenderer_gui.setAutoShapeType(true);
		
		//if(!Gdx.gl.glIsEnabled(GL30.GL_BLEND))
		{
			Gdx.gl.glEnable(GL30.GL_BLEND);
			Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
		}		
		shapeRenderer_gui.begin();
		
		//Dialog Rect
		shapeRenderer_gui.set(ShapeType.Filled);
		shapeRenderer_gui.setColor(town.dialogColor);
		shapeRenderer_gui.rect(dlgX, dlgY, dialogW, dialogH);
		
		shapeRenderer_gui.set(ShapeType.Line);
		shapeRenderer_gui.setColor(0.2f, 0.2f, 0.2f, 0.7f);
		shapeRenderer_gui.rect(dlgX, dlgY, dialogW, dialogH);
		
		if(town.gameWorld.markerObject.thehuman!=null && town.gameWorld.markerObject.thehuman.abilitySpaceshipTechnology>0) {
			shapeRenderer_gui.set(ShapeType.Filled);
			shapeRenderer_gui.setColor(0f,0f,0f,0.7f);
			shapeRenderer_gui.rect(dlgX, dlgY+dialogH, dialogW, 45);
		}

		
		// Human
		if (mobj.thehuman != null) {
			render_human(0, x, libgdxy);
		}
		
		shapeRenderer_gui.end();
		//Gdx.gl.glDisable(GL30.GL_BLEND);
		
		town.gameGui.editorSpriteBatch.begin();
		
		dlgFont.setColor(dlgFontColor);
		town.gameGui.editorSpriteBatch.setColor(Color.WHITE);
		
		if (!mobj.isHuman() && !mobj.isCompanyObject() && !mobj.showAsCompanyObject()) {
			town.gameGui.editorSpriteBatch.setShader(town.gameFont.fontShader);
			dlgFont2.setColor(dlgFontColor);
			dlgFont2.getData().setScale(0.6f);
			String sname=mobj.theobject.objectName;
			//if(town.bDevMode)
				sname+=" (ID "+ mobj.uniqueId +")";
			dlgFont2.draw(town.gameGui.editorSpriteBatch, sname, dlgX + 20, dlgY + dialogH - 20);
			town.gameGui.editorSpriteBatch.setShader(null);
		}
		
		renderRadiator(x, libgdxy);
		renderLight(x, libgdxy);
		render_laundryobject(x, libgdxy);
		
		
		if(mobj.theobject.editoraction.contains("company_college_spaceship") && mobj.iLaunchstatus==0) {
			if(mobj.bObjectIsReady) {
				
				String tempstr = buttonAccept.tooltip.textLines.get(0);
				buttonAccept.tooltip.textLines.clear();
				buttonAccept.tooltip.textLines.add("LAUNCH SPACESHIP");
				
				buttonAccept.isShowing=true;
				Vector3 vdpos = mobj.getScreenPosition((mobj.width / 2), (int) (mobj.height / 2));
				buttonAccept.setDuplicatePosition((int) vdpos.x - getDuplicateSize(), (int) vdpos.y+getDuplicateSize()*2+4);
				buttonAccept.setColor(new Color(1f,1f,1f,1));
				buttonAccept.setDuplicateSize(getDuplicateSize(), getDuplicateSize());
				town.gameGui.editorSpriteBatch.end();
				buttonAccept.renderDuplicateOnly(x, libgdxy);
				town.gameGui.editorSpriteBatch.begin();
				
				buttonAccept.tooltip.textLines.clear();
				buttonAccept.tooltip.textLines.add(tempstr);
			}
		}
		
		//Apply Resize of existing Room, Ground, Carpet
		if(mobj.scrollwidth>0)
		{
			buttonAddressResize.isShowing=false;
			buttonAccept.isShowing=true;
			//if(mobj.theobject.editoraction.contains("anyroom_carpet"))
			Vector3 vdpos = mobj.getScreenPosition((mobj.width / 2), (int) (mobj.height / 2));
			buttonAccept.setDuplicatePosition((int) vdpos.x - getDuplicateSize(), (int) vdpos.y+getDuplicateSize()*2+4);
			buttonAccept.setColor(new Color(1f,1f,1f,1));
			buttonAccept.setDuplicateSize(getDuplicateSize(), getDuplicateSize());
			town.gameGui.editorSpriteBatch.end();
			buttonAccept.renderDuplicateOnly(x, libgdxy);
			town.gameGui.editorSpriteBatch.begin();
		}
		else
		{
			buttonAddressResize.isShowing=true;
			buttonAccept.isShowing=false;
		}
		
		//Objectdesign
		if((mobj.theobject.editoraction.contains("anyroom_carpet") 
				//mobj.theobject.isRoomObject || 
				|| mobj.theobject.isGroundObject
				)
				&& mobj.scrollwidth==0)
		{
			Boolean bResearched = false;
			if(town.gameWorld.markerObject.theobject.isRoomObject)
				bResearched=town.gameWorld.gameResourceConfig.isObjectResearched("function_resizeroom");
			if(town.gameWorld.markerObject.theobject.isGroundObject)
				bResearched=town.gameWorld.gameResourceConfig.isObjectResearched("function_resizeground");
			if(town.gameWorld.markerObject.theobject.editoraction.contains("anyroom_carpet"))
				bResearched=town.gameWorld.gameResourceConfig.isObjectResearched("function_resizecarpet");
			
			if(bResearched)
			{
				Vector3 vdpos = mobj.getScreenPosition(mobj.width / 2, (int) (mobj.height / 2));
				buttonAddressResize.setDuplicatePosition((int) vdpos.x - getDuplicateSize(), (int) vdpos.y+getDuplicateSize()*2+4);
				buttonAddressResize.setColor(new Color(1f,1f,1f,1));
				buttonAddressResize.setDuplicateSize(getDuplicateSize(),getDuplicateSize());
				buttonAddressResize.setTooltip("Resize");
				buttonAddressResize.tooltip.setColor(new Color(1f,1f,1f,1));
				buttonAddressResize.buttonType=CGuiControl_Button.ButtonType.TOGGLE_IMAGE;
				town.gameGui.editorSpriteBatch.end();
				buttonAddressResize.renderDuplicateOnly(x, libgdxy);
				town.gameGui.editorSpriteBatch.begin();
				buttonAddressResize.buttonType=CGuiControl_Button.ButtonType.IMAGE;
			}
		}
		
		if(mobj.theobject.isRoomObject)
		{
			Vector3 vdpos = mobj.getScreenPosition(-100, mobj.height+100);
			buttonPlus.setDuplicatePosition((int) vdpos.x, (int) vdpos.y);
			buttonSave.setDuplicatePosition((int) vdpos.x+getDuplicateSize()+4, (int) vdpos.y);
			
			buttonPlus.setTooltip("Clone Room (Architect Required, Cost: " + CCompany.getArchitectCosts(town, ArchitectWorkType.CLONE_ROOM) + " Architect Workoutput)");
			CCompany comp = CCompany.getNextActiveCompany(CompanyType.ARCHITECTURE_BUREAU, town, 0, 0, 0, 99999);
			buttonPlus.tooltip.setColor(Color.WHITE);
			if(comp==null || comp.getWorkOutput(WorkoutputType.DEFAULT)<CCompany.getArchitectCosts(town, ArchitectWorkType.CLONE_ROOM))
			{
				buttonPlus.tooltip.setColor(Color.RED);
				buttonPlus.tooltip.textLines.add("WARNING: Architect is missing or not active");
			}
			
			buttonPlus.setColor(new Color(1f,1f,1f,1));
			buttonPlus.setDuplicateSize(getDuplicateSize(), getDuplicateSize());
			buttonSave.setDuplicateSize(getDuplicateSize(), getDuplicateSize());

			town.gameGui.editorSpriteBatch.end();
			buttonPlus.renderDuplicateOnly(x, libgdxy);
			//buttonSave.renderDuplicateOnly(x, libgdxy);
			town.gameGui.editorSpriteBatch.begin();
		}
		
		//Supermarket BuyIn
		if(mobj.theobject.editoraction.contains("supermarket_buyin"))
		{
			int fill1=mobj.getObjectFillingMulti();
			int max1=mobj.getObjectFillingMultiMax();
			int left=380;
			guiinfo_food.setTooltip(mobj.getObjectFillingText());
			guiinfo_food.render(x, libgdxy, dlgX + 20 + left, dlgY + dialogH - 134);
			dlgFont.draw(town.gameGui.editorSpriteBatch, fill1 + "/" + max1, dlgX + 54 + left, dlgY + dialogH - 120);
		}
		
		if (mobj.theobject.editoraction.contains("recyclingcenter_garbagecontainer") ||
				mobj.theobject.editoraction.contains("bedroom_wardrobe") || 
				mobj.theobject.editoraction.contains("bathroom_towelcabinet") ||
				mobj.theobject.editoraction.contains("garbagecan")
				) 
		{
			guiinfo_food.setTooltip(mobj.getObjectFillingText());
			
			// Garbage Container, Towel Cabinet 
			if (mobj.theobject.editoraction.contains("recyclingcenter_garbagecontainer") || 
					mobj.theobject.editoraction.contains("bathroom_towelcabinet")  ||
					mobj.theobject.editoraction.contains("garbagecan")
					) 
			{
				guiinfo_food.render(x, libgdxy, dlgX + 20, dlgY + dialogH - 134);
				dlgFont.draw(town.gameGui.editorSpriteBatch,town.gameWorld.markerObject.objectFilling + "/" + mobj.getObjectFillingMax(), 
								dlgX + 54, dlgY + dialogH - 120);
			}
			
			// Wardrobe
			if (mobj.theobject.editoraction.contains("bedroom_wardrobe")) 
			{
				guiinfo_food.render(x, libgdxy, dlgX + 20 + 385, dlgY + dialogH - 134);
				dlgFont.draw(town.gameGui.editorSpriteBatch, town.gameWorld.markerObject.objectFilling + "/" + mobj.getObjectFillingMax(), 
						dlgX + 54 + 385, dlgY + dialogH - 120);
			}
			
			town.gameGui.editorSpriteBatch.end();
			guiinfo_food.renderTooltip(x, libgdxy);
			town.gameGui.editorSpriteBatch.begin();
		}
		
		// Car
		if (mobj.theobject.isCar) {
			
			String sfuel = Math.round(mobj.fuelValue) + "/" + Math.round(mobj.fuelValueMax);
			if (mobj.fuelValue < 1)
				sfuel = "RESERVE";
			
			int posx = dlgX + 13;
			int posy = dlgY + dialogH - 134 - 35;
			int textposx=posx+32;
			
			posx += 385;
			textposx=posx+32+5;
			
			if (mobj.isCompanyObject()) // Garbage Truck
				posy += 1;
			
			if(mobj.theobject.editoraction.contains("traffic_car_residential"))
			{
				if (mobj.theobject.getRequiredWorkplaceEducation() > -1) 
				{
					//guiinfo_workplace_education.render(x, libgdxy, Math.round(dlgX + 20 + town.gameFont.layout.width + 10-135-25+35)-20, dlgY + dialogH - 132);
					//pointsControl_education.setPosition((int) (dlgX + 20 + town.gameFont.layout.width + 37)-135-25+35-20, dlgY + dialogH - 132);

					guiinfo_workplace_education.render(x, libgdxy, Math.round(dlgX+15), dlgY + dialogH - 132);
					pointsControl_education.setPosition((int) (dlgX+42), dlgY + dialogH - 132);

					pointsControl_education.setValue(mobj.theobject.getRequiredWorkplaceEducation());
					
					town.gameGui.editorSpriteBatch.end();
					pointsControl_education.setBeginShapeRenderer(true);
					pointsControl_education.render(0, 0);
					guiinfo_workplace_education.renderTooltip(x, libgdxy);
					town.gameGui.editorSpriteBatch.begin();
				}			
				
				int fill1=mobj.getObjectFillingMulti();
				int max1=mobj.getObjectFillingMultiMax();
				int left=380;
				guiinfo_food.setTooltip(mobj.getObjectFillingText());
				guiinfo_food.render(x, libgdxy, dlgX + 20 + left, dlgY + dialogH - 134);
				dlgFont.draw(town.gameGui.editorSpriteBatch, fill1 + "/" + max1, dlgX + 54 + left, dlgY + dialogH - 120);
			}
			
			// Guiinfo
			guiinfo_fuel1.render(x, libgdxy, posx, posy);
			if(mobj.fuelValue<1)
				dlgFont.setColor(0.8f, 0, 0, 1);
			dlgFont.draw(town.gameGui.editorSpriteBatch, sfuel, textposx, posy + 17);
			dlgFont.setColor(dlgFontColor);
			
			// Tooltips
			town.gameGui.editorSpriteBatch.end();
			guiinfo_fuel1.renderTooltip(x, libgdxy);
			guiinfo_food.renderTooltip(x, libgdxy);
			town.gameGui.editorSpriteBatch.begin();
		}
		
		// Human
		if (mobj.thehuman != null) {
			render_human(1, x, libgdxy);
			town.gameGui.editorSpriteBatch.end();
			buttonMoveHouse.setPosition(dlgX + 18, dlgY + dialogH - 262);
			
			Vector3 vdpos = town.gameWorld.markerObject.getScreenPosition(0, (int) (town.gameWorld.markerObject.height_human() + 10));
			buttonMoveHouse.setDuplicatePosition((int) vdpos.x - getDuplicateSize(), (int) vdpos.y);
			buttonMoveHouse.setDuplicateSize(getDuplicateSize(), getDuplicateSize());
			
			if (!town.gameWorld.markerObject.bIsDead)
				buttonMoveHouse.renderDuplicateOnly(x, libgdxy);
			
			town.gameGui.editorSpriteBatch.begin();
		} else if (mobj.isCompanyObject() || mobj.showAsCompanyObject()) {
			
			Vector3 vdpos = town.gameWorld.markerObject.getScreenPosition(0,
					(int) (town.gameWorld.markerObject.height + 10));
			buttonEdit_Worker.setDuplicatePosition((int) vdpos.x
					- getDuplicateSize(), (int) vdpos.y);
			buttonEdit_Worker.setDuplicateSize(getDuplicateSize(),
					getDuplicateSize());
			
			render_companyobject(x, libgdxy);
		
		} else if (mobj.isFoodFillingByWorkerObject()) {

			buttonEdit_Worker.setPosition(dlgX + 19, dlgY + dialogH - 213 + 80);
			Vector3 vdpos = town.gameWorld.markerObject.getScreenPosition(0, (int) (town.gameWorld.markerObject.height + 10));
			buttonEdit_Worker.setDuplicatePosition((int) vdpos.x - getDuplicateSize(), (int) vdpos.y);
			buttonEdit_Worker.setDuplicateSize(getDuplicateSize(), getDuplicateSize());
			buttonClear_Worker.setDuplicateSize(getDuplicateSize(), getDuplicateSize());
			buttonClear_Worker.setPosition(dlgX + 3, dlgY + dialogH - 213 + 80);
			dlgFont.draw(town.gameGui.editorSpriteBatch, address, dlgX + 20, dlgY + dialogH - 40 - 20);
			render_foodfillingbyworker(x, libgdxy);
		} 
		else if(mobj.theobject.editoraction.contains("laundrybasket"))
		{
			render_responsible(x, libgdxy);
		}
		else if (mobj.theobject.editoraction.contains("diningroom_diningtable")) {
			renderDinnerTable(x, libgdxy);
		}
		else if (mobj.isOwnerObject() > 0) 
		{
			dlgFont.draw(town.gameGui.editorSpriteBatch, address,
					dlgX + 20, dlgY + dialogH - 40 - 20);
			renderOwnerObject(x, libgdxy);
		}
		else if (mobj.theobject.editoraction.contains("livingroom_bookshelf")) {
			dlgFont.draw(town.gameGui.editorSpriteBatch, address, dlgX + 20, dlgY + dialogH - 40 - 20);
			renderBookshelf(x, libgdxy);
		} else {
			dlgFont.draw(town.gameGui.editorSpriteBatch, address,
					dlgX + 20, dlgY + dialogH - 40 - 20);
		}
		
		if (!mobj.isCompanyObject() && !mobj.isHuman() && !mobj.showAsCompanyObject()) {
			town.gameGui.editorSpriteBatch.end();

			//if(!Gdx.gl.glIsEnabled(GL30.GL_BLEND))
			{
				Gdx.gl.glEnable(GL30.GL_BLEND);
				Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
			}
			shapeRenderer_gui.setAutoShapeType(true);
			shapeRenderer_gui.begin();
			shapeRenderer_gui.set(ShapeType.Line);
			shapeRenderer_gui.setColor(0.2f, 0.2f, 0.2f, 0.7f);
			shapeRenderer_gui.line(dlgX, dlgY + dialogH - 100, dlgX + dialogW, dlgY + dialogH - 100);
			shapeRenderer_gui.end();
			//Gdx.gl.glDisable(GL30.GL_BLEND);
			town.gameGui.editorSpriteBatch.begin();
			
			// *******************
			// Source Consumption
			// *******************
			if (mobj.getEnergyConsumption() > 0 && mobj.getWaterConsumption() > 0)
			{
				guiinfo_energyconsumption.render(x, libgdxy, dlgX + 520, dlgY + dialogH - 136);
				dlgFont.draw(town.gameGui.editorSpriteBatch, "" + mobj.getEnergyConsumption(), dlgX + 550, dlgY + dialogH - 120);
				
				guiinfo_waterconsumption.render(x, libgdxy, dlgX + 520, dlgY+ dialogH - 136-30);
				dlgFont.draw(town.gameGui.editorSpriteBatch, "" + mobj.getWaterConsumption(), dlgX + 550, dlgY + dialogH - 120-30);
			}
			else
			{
				if (mobj.getEnergyConsumption() > 0) {
					guiinfo_energyconsumption.render(x, libgdxy, dlgX + 520, dlgY
							+ dialogH - 136);
					dlgFont.draw(town.gameGui.editorSpriteBatch, ""
							+ mobj.getEnergyConsumption(), dlgX + 550, dlgY + dialogH
							- 120);
				}
				
				if (mobj.getWaterConsumption() > 0) {
					guiinfo_waterconsumption.render(x, libgdxy, dlgX + 520, dlgY
							+ dialogH - 160);
					dlgFont.draw(town.gameGui.editorSpriteBatch, ""
							+ mobj.getWaterConsumption(), dlgX + 550, dlgY + dialogH
							- 144);
				}
			}
		}
		
		


		
		// ****************
		// Object Condition
		// ****************
//		if (mobj.defaultObjectCondition > 0) {
//			if (mobj.objectCondition < mobj.defaultObjectCondition / 2)
//				dlgFont.setColor(Color.RED);
//
//			guiinfo_condition.render(x, libgdxy, dlgX + 400, dlgY + dialogH
//					- 161);
//
//			dlgFont.draw(town.gameWorld.gameGui.editorSpriteBatch, ""
//					+ mobj.objectCondition + "/" + mobj.defaultObjectCondition,
//					dlgX + 430, dlgY + dialogH - 145);
//			dlgFont.setColor(dlgFontColor);
//		}		
		
		
		//*********
		//Warnings
		//*********
		if(mobj.isCompanyWorkingPlace() || mobj.isCompanyTaskObject() || mobj.isFoodFillingByWorkerObject())
		{
			if(mobj.worker!=null && !mobj.worker.thehuman.canWork())
			{
				swarnings += mobj.worker.thehuman.getName() + " " + mobj.worker.thehuman.canWorkText()+", ";
			}
		}
		
		if (mobj.theobject.editoraction.contains("recyclingcenter_garbagebag")) {
			swarnings += "garbage makes people unhappy, ";
		}
		
		if (mobj.theobject.editoraction.contains("coffeepot") && mobj.objectFilling>0 && mobj.isOccupiedBy==null) {
			swarnings += "dirty crockery makes people unhappy, ";
		}
		
		if (mobj.theobject.editoraction.contains("laundryobject")) {
			swarnings += "laundry on the floor makes people unhappy, ";
		}
		
		if (mobj.getEnergyConsumption() > 0
				&& town.gameWorld.getEnergyConsumption() > town.gameWorld
						.getEnergyOutput()) {
			swarnings += "not enough electrical energy, ";
		}
		
		if (!mobj.isActiveByWaterConsumption()) {
				//getWaterConsumption() > 0
				//&& town.gameWorld.getWaterConsumption() > town.gameWorld
				//		.getWaterOutput()) {
			swarnings += "not enough water, ";
		}
		
		if (mobj.defaultObjectCondition > 0 && mobj.objectCondition < 50) {
			swarnings += "bad condition, ";
		}
		
		if (mobj.isCompanyWorkingPlace()) {
			if (mobj.theobject.editoraction
					.contains("company_school_workingplace_studentsdesk")) {
				if (mobj.worker == null && mobj.worker2 == null)
					swarnings += "no " + mobj.getWorkerTitle().toLowerCase()
							+ ", ";
				else if (mobj.worker == null)
					swarnings += mobj.getWorkerTitle().toLowerCase()
							+ " 1 not chosen, ";
				else if (mobj.worker2 == null)
					swarnings += mobj.getWorkerTitle().toLowerCase()
							+ " 2 not chosen, ";
			} else {
				if (mobj.worker == null)
					swarnings += "no " + mobj.getWorkerTitle().toLowerCase()
							+ ", ";
			}
		}
		
		if (mobj.isCompanyTaskObject()) {
			if (mobj.worker == null)
				swarnings += "no responsible "
						+ mobj.getWorkerTitle().toLowerCase() + ", ";
		}
		
		if (mobj.isFoodFillingByWorkerObject()
				|| mobj.isResidentialTaskObject()) {
			if (mobj.worker == null) {
				if (mobj.theobject.editoraction.contains("diningroom_diningtable"))
					swarnings += "no cook, ";
				else
					swarnings += "no responsible resident, ";
			}
		}
		
		if (mobj.isOwnerObject() > 0) {
			int count = mobj.isOwnerObject();
			
			if (count == 1 && mobj.owner == null)
				swarnings += "no owner, ";
			
			if (count == 2) {
				if (mobj.owner == null && mobj.owner2 == null)
					swarnings += "no owner, ";
				else if (mobj.owner == null)
					swarnings += "owner 1 not chosen, ";
				else if (mobj.owner2 == null)
					swarnings += "owner 2 not chosen, ";
			}
		}
		
		//swarnings += mobj.dinnerTableIsOnline(1);
		
		if (mobj.theobject.editoraction.contains("bedroom_bed")
				&& mobj.theobject.bBedTooClose) {
			swarnings += "too close to other bed, ";
		}
		
		if (mobj.theobject.editoraction.contains("traffic_car"))
		{
			if(mobj.fuelValue<1)
			{
				swarnings += "empty fuel tank, ";
			}
		}
		
		if(mobj.isHuman() && mobj.bIsDead)
		{
			swarnings="";
			
			if(!mobj.actionstring1.contains("show_grave") && !mobj.actionstring1.contains("show_coffin") && mobj.iZombie<1)
				swarnings += " People are sad - " + mobj.thehuman.getName() + " has to be buried, ";
		}
		
		//Draw Warnings
		if (swarnings.length() > 0) {
			swarnings = swarnings.substring(0, swarnings.length() - 2);
			swarnings = swarnings.toUpperCase();
			BitmapFont tfont = town.gameFont.bfArial;
			tfont.getData().setScale(0.55f);
			town.gameFont.layout.setText(tfont, swarnings);
			swarnings = town.gameFont.shortenStringToWidth(tfont, swarnings, dialogW - 90);
			int warning_delta_y=0;
			if(mobj.isHuman())
				warning_delta_y+=41+addHumanDlgHeight+addHumanDlgHeight2;
			else
				warning_delta_y=5;
			
			if (mobj.theobject.editoraction.contains("diningtable1_count8"))
				warning_delta_y=40;
				
			if (mobj.theobject.editoraction.contains("illuminati_defensesystem"))
				warning_delta_y=30;
			
			town.gameGui.editorSpriteBatch.setShader(town.gameFont.fontShader);
			
			tfont.setColor(0.7f, 0.1f, 0.1f, 1f);
			tfont.draw(town.gameGui.editorSpriteBatch, swarnings
					, dlgX + dialogW / 2 - town.gameFont.layout.width / 2 + 20
					, dlgY + dialogH - 216 - warning_delta_y);
			
			tfont.setColor(dlgFontColor);
			town.gameGui.editorSpriteBatch.setShader(null);
			town.gameGui.editorSpriteBatch.setColor(0.8f, 0, 0, 0.3f);
			town.gameGui.editorSpriteBatch.draw(
					town.gameResourceConfig.textures.get("guiinfo_warning"),
					dlgX + dialogW / 2 - town.gameFont.layout.width / 2 - 20- 2
					, dlgY + dialogH - 233 - 2 - 2-warning_delta_y
					, 30 + 5
					, 25 + 5);
			
			town.gameGui.editorSpriteBatch.setColor(0.8f, 0, 0, 0.3f);
			town.gameGui.editorSpriteBatch.draw(
					town.gameResourceConfig.textures.get("guiinfo_warning"),
					dlgX + dialogW / 2 - town.gameFont.layout.width / 2 - 20,
					dlgY + dialogH - 233 - 2-warning_delta_y
					, 30, 25);
			
			town.gameGui.editorSpriteBatch.setColor(0.8f, 0f, 0f, 0.9f);
			town.gameGui.editorSpriteBatch.draw(
					town.gameResourceConfig.textures.get("guiinfo_warning"),
					dlgX + dialogW / 2 - town.gameFont.layout.width / 2 - 20,
					dlgY + dialogH - 233 - 2-warning_delta_y
					, 30, 25);
		}
		
		//Draw Resize Price
		if(mobj.scrollwidth>0)
		{
			int price=mobj.getResizeByScrollingPrice();
			String str = "$"+price;
			town.gameFont.bfArial.setColor(1f, 1f, 1f, 1);
			if(town.gameWorld.townMoney<price)
				town.gameFont.bfArial.setColor(0.7f, 0f, 0f, 0.9f);
			
			town.gameFont.bfArial.getData().setScale(0.7f);
			town.gameFont.layout.setText(town.gameFont.bfArial, str);
			Vector3 pprice = town.gameWorld.markerObject.getScreenPosition(Math.round(mobj.width/2-town.gameFont.layout.width/2), mobj.height/2);
			town.gameGui.editorSpriteBatch.setShader(town.gameFont.fontShader);
			town.gameFont.bfArial.draw(town.gameGui.editorSpriteBatch, str, pprice.x, pprice.y);
			town.gameGui.editorSpriteBatch.setShader(null);
		}
		
		//Show Resize Info
	    if(mobj.theobject.isGroundObject || mobj.theobject.isRoomObject)
	    {
		    town.gameFont.bfArial.getData().setScale(0.68f);
		    town.gameFont.bfArial.setColor(0.8f, 0.8f, 0.8f, 0.8f);
		    town.gameGui.editorSpriteBatch.setShader(town.gameFont.fontShader);
		    Vector3 pprice = town.gameWorld.markerObject.getScreenPosition(Math.round(mobj.width/2-town.gameFont.layout.width/2), mobj.height/2);
	    	town.gameFont.bfArial.draw(town.gameGui.editorSpriteBatch, "Use the mouse wheel for resizing", pprice.x-80, pprice.y-120);
	    	town.gameFont.bfArial.draw(town.gameGui.editorSpriteBatch, "Press and hold x or c for Horz/Vert", pprice.x-80, pprice.y-140);
	    	town.gameGui.editorSpriteBatch.setShader(null);
	    }				
	    
		town.gameGui.editorSpriteBatch.end();
		guiinfo_energyconsumption.renderTooltip(x, libgdxy);
		guiinfo_waterconsumption.renderTooltip(x, libgdxy);
		
		for(CGuiControl_Button btn : listButtons)
		{
			if(btn.isShowing)
			{
				btn.enableTooltip(false);
				btn.renderTooltip(x, libgdxy);
			}
		}
	}
}




