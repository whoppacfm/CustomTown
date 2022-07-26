package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.mygdx.game.CGuiDialog_OptionList.OptionListType;
import com.mygdx.game.CGuiDialog_OptionList.SkillObject;

public class CGuiDialog_CreateResident extends CGuiDialog {

	public String sDialogInput1;
	public int maxLenghtInput1;

	public String sGender;
	public int iAge;
	public int iSkill;
	public int iMaxhealth;
	public int iMaxHappyness;
	public int iIntelligence;
	public int iFitness;
	public float fHealthAttitude;
	public float fPositiveAttitude;
	public float fEducation;
	public int price;
	public CWorldObject workplace;
	int posy;
	int posyall;

	int companycount;
	int avghealth; 
	int avghappyness; 
	int residentcount;
	int target_companycount=20;
	int target_avghealth=80; 
	int target_avghappyness=70; 
	int target_residentcount=100;
	
	private String sskills;
	public SkillObject skillObject;

	private CObject buttonControl;
	private CObject genderIcon_Woman;
	private CObject genderIcon_Man;

	private CGuiControl_Slider slider;
	private CGuiControl_Button buttonCreate;
	private CGuiControl_Button buttonCancel;

	private CGuiControl_Button buttonPagingLeft;
	private CGuiControl_Button buttonPagingRight;

	private CGuiControl_Button buttonAddSkill;
	private CGuiControl_Button buttonRemoveSkills;

	public CGuiControl_Button buttonSpaceshipTechnologist;
	
	private CGuiControl_Slider slider_maxhealth;
	private CGuiControl_Slider slider_maxhappyness;
	private CGuiControl_Slider slider_intelligence;
	private CGuiControl_Slider slider_fitness;
	private CGuiControl_Slider slider_education;

	private CGuiControl_Slider slider_skill;

	CGuiControl_Points pointsControl_healthAttitude;
	CGuiControl_Points pointsControl_positiveAttitude;
	CGuiControl_Points pointsControl_education;

	int textInputX;
	int textInputY;
	int textInputW;
	int textInputH;

	int genderWomanX;
	int genderWomanY;
	int genderWomanW;
	int genderWomanH;

	int genderManX;
	int genderManY;
	int genderManW;
	int genderManH;

	int headindex;
	float valueScale;
	float priceScale;

	Color labelColor;
	Color valueColor;

	int righttextx;

	int headx;
	int heady;

	int pricey;
	int pricex;
	
	List<CObject> controlList;

	public CGuiDialog_CreateResident(List<CObject> controlList_, BitmapFont font, int dialogX, int dialogY,
			CTown town1) {
		super("CGuiDialog_CreateResident");
		workplace = null;
		
		town = town1;
		dlgX = dialogX;
		dlgY = dialogY;
		controlList = controlList_;
		buttonControl = ((Optional<CObject>) controlList.stream()
				.filter(p -> p.editoraction.equals("control_button_play1")).findFirst()).get();
		genderIcon_Woman = ((Optional<CObject>) controlList.stream()
				.filter(p -> p.editoraction.equals("control_icon_woman")).findFirst()).get();
		genderIcon_Man = ((Optional<CObject>) controlList.stream()
				.filter(p -> p.editoraction.equals("control_icon_man")).findFirst()).get();
		dlgFont = font;
		skillObject = null;

		init();
	}

	public void init() {
		workplace = null;

		dialogW = 640;
		dialogH = 474;

		posyall = 0;
		pricey = -25;
		pricex = 260;

		sskills = "No Skill chosen";

		dlgSpriteBatch = town.gameGui.editorSpriteBatch; 
		dlgShapeRenderer = town.gameGui.shapeRenderer; 
		dlgFont2 = town.gameFont.bfArial;

		valueScale = 0.63f;
		priceScale = 0.7f;

		price = 4500;

		labelColor = new Color(1f, 1f, 1f, 0.94f);
		valueColor = new Color(1f, 1f, 1f, 0.9f);

		sGender = "w";
		iAge = 18;

		iSkill = 0;
		iMaxhealth = 100;
		iMaxHappyness = 100;
		iIntelligence = 100;
		iFitness = 100;

		fHealthAttitude = 1;
		fPositiveAttitude = 1;
		fEducation = 1;

		sDialogInput1 = "";
		maxLenghtInput1 = 30;

		setMiddlePosition();
		dlgY = Gdx.app.getGraphics().getHeight() - dialogH - 80;

		headx = -447;
		heady = dlgY + dialogH - 80;

		buttonPagingLeft = new CGuiControl_Button(dlgX + dialogW - 135 - 37 + headx, heady, 30, 30, 0, 0, "", dlgFont,
				town.gameResourceConfig.textures.get("gui_arrowleft"), CGuiControl_Button.ButtonType.IMAGE, town);
		buttonPagingRight = new CGuiControl_Button(dlgX + dialogW - 135 + 56 + headx, heady, 30, 30, 0, 0, "", dlgFont,
				town.gameResourceConfig.textures.get("gui_arrowright"), CGuiControl_Button.ButtonType.IMAGE, town);
		buttonPagingLeft.setColor(new Color(1, 1, 1, 1));
		buttonPagingRight.setColor(new Color(1, 1, 1, 1));
		buttonPagingLeft.renderMode = 0;

		textInputX = dlgX + 100 + 150 - 20 + 8 + 5 + 12;
		textInputY = dlgY + dialogH - 59;

		textInputW = 340;
		textInputH = 25;

		int genderx = 95;
		genderWomanX = dlgX + 100 + 361 + genderx;
		genderWomanY = heady - 50;

		genderWomanW = 20 - 5;
		genderWomanH = 28 - 5;

		genderManX = dlgX + 100 + 393 + genderx;
		genderManY = genderWomanY;

		genderManW = 24 - 5;
		genderManH = 26 - 5;

		int ctrlleft_leftside = 10 + 20 + 10; // controls linke seite
		int ctrlleft = 100 + 30 + 20 + 12; // controls rechte seite
		righttextx = 15 + 20;
		int allslidery = 1;

		CObject sliderObj = ((Optional<CObject>) controlList.stream()
				.filter(p -> p.editoraction.equals("control_button_speed1")).findFirst()).get();
		CObject sliderButton = ((Optional<CObject>) controlList.stream()
				.filter(p -> p.editoraction.equals("control_button_speed2")).findFirst()).get();
		int sliderx = dlgX + 100 + 10 + 55 + ctrlleft_leftside;
		int slidery = heady - 50;
		if (slider == null) {
			slider = new CGuiControl_Slider(sliderx, slidery, 5, 120, 35, sliderObj, sliderButton, town);
			slider.setValue(iAge);
		}
		slider.controlX = sliderx;
		slider.controlY = slidery;

		posy = 14 - posyall;

		int x_slider_maxhealth = dlgX + 100 + 10 + 55 + ctrlleft_leftside;
		int y_slider_maxhealth = heady - 100;
		if (slider_maxhealth == null) {
			slider_maxhealth = new CGuiControl_Slider(x_slider_maxhealth, y_slider_maxhealth, 80, 200, 100, sliderObj,
					sliderButton, town);
			slider_maxhealth.setValue(iMaxhealth);
		}
		slider_maxhealth.controlX = x_slider_maxhealth;
		slider_maxhealth.controlY = y_slider_maxhealth;

		int x_slider_maxhappyness = dlgX + 100 + 10 + 55 + ctrlleft_leftside;
		int y_slider_maxhappyness = y_slider_maxhealth - 30;
		if (slider_maxhappyness == null) {
			slider_maxhappyness = new CGuiControl_Slider(x_slider_maxhappyness, y_slider_maxhappyness, 80, 200, 100,
					sliderObj, sliderButton, town);
			slider_maxhappyness.setValue(iMaxHappyness);
		}
		slider_maxhappyness.controlX = x_slider_maxhappyness;
		slider_maxhappyness.controlY = y_slider_maxhappyness;

		int x_slider_intelligence = dlgX + 100 + 10 + 55 + ctrlleft_leftside;
		int y_slider_intelligence = y_slider_maxhappyness - 45;
		if (slider_intelligence == null) {
			slider_intelligence = new CGuiControl_Slider(x_slider_intelligence, y_slider_intelligence, 30, 160, 100,
					sliderObj, sliderButton, town);
			slider_intelligence.setValue(iIntelligence);
		}
		slider_intelligence.controlX = x_slider_intelligence;
		slider_intelligence.controlY = y_slider_intelligence;

		int x_slider_fitness = dlgX + 100 + 10 + 55 + ctrlleft_leftside;
		int y_slider_fitness = y_slider_intelligence - 30;
		if (slider_fitness == null) {
			slider_fitness = new CGuiControl_Slider(x_slider_fitness, y_slider_fitness, 30, 160, 100, sliderObj,
					sliderButton, town);
			slider_fitness.setValue(iFitness);
		}
		slider_fitness.controlX = x_slider_fitness;
		slider_fitness.controlY = y_slider_fitness;

		int x_slider_skill = dlgX + 100 + 10 + 55 + ctrlleft_leftside;
		int y_slider_skill = y_slider_fitness - 45;
		if (slider_skill == null) {
			slider_skill = new CGuiControl_Slider(x_slider_skill, y_slider_skill, 0, 100, 100, sliderObj, sliderButton, town);
			slider_skill.setValue(iSkill);
		}
		slider_skill.controlX = x_slider_skill;
		slider_skill.controlY = y_slider_skill;

		int buttonaddskillx = dlgX + 19;
		int buttonaddskilly = slider_skill.controlY;
		if (buttonAddSkill == null) {
			buttonAddSkill = new CGuiControl_Button(buttonaddskillx, buttonaddskilly, 24, 24, 0, 0, "", dlgFont,
					town.gameResourceConfig.textures.get("guiinfo_intelligence"), CGuiControl_Button.ButtonType.IMAGE,
					town);
			buttonAddSkill.setTooltip("Set Skill");
		}
		buttonAddSkill.controlX = buttonaddskillx;
		buttonAddSkill.controlY = buttonaddskilly;
		
		// buttonRemoveSkills = new CGuiControl_Button(
		// dlgX + 19+32,
		// dlgY + dialogH - 544,
		// 24,
		// 24,
		// 0,
		// 0,
		// "",
		// dlgFont,
		// town.gameResourceConfig.textures.get("guiinfo_clear"),
		// CGuiControl_Button.ButtonType.IMAGE, town);
		// buttonRemoveSkills.setTooltip("Remove Skills");
		
		int pointsize = 17;
		
		int x_pointsControl_healthAttitude = dlgX + 100 + 280 + ctrlleft;
		int y_pointsControl_healthAttitude = y_slider_maxhealth;
		if (pointsControl_healthAttitude == null) {
			pointsControl_healthAttitude = new CGuiControl_Points(town, x_pointsControl_healthAttitude,
					y_pointsControl_healthAttitude, 3, 1, dlgShapeRenderer, dlgSpriteBatch);
			pointsControl_healthAttitude.setPointSize(pointsize);
		}
		pointsControl_healthAttitude.controlX = x_pointsControl_healthAttitude;
		pointsControl_healthAttitude.controlY = y_pointsControl_healthAttitude;
		
		int x_pointsControl_positiveAttitude = dlgX + 100 + 280 + ctrlleft;
		int y_pointsControl_positiveAttitude = y_slider_maxhappyness;
		if (pointsControl_positiveAttitude == null) {
			pointsControl_positiveAttitude = new CGuiControl_Points(town, x_pointsControl_positiveAttitude,
					y_pointsControl_positiveAttitude, 3, 1, dlgShapeRenderer, dlgSpriteBatch);
			pointsControl_positiveAttitude.setPointSize(pointsize);
		}
		pointsControl_positiveAttitude.controlX = x_pointsControl_positiveAttitude;
		pointsControl_positiveAttitude.controlY = y_pointsControl_positiveAttitude;

		int x_pointsControl_education = dlgX + 100 + 280 + ctrlleft;
		int y_pointsControl_education = y_slider_intelligence;
		if (pointsControl_education == null) {
			pointsControl_education = new CGuiControl_Points(town, x_pointsControl_education, y_pointsControl_education,
					3, 1, dlgShapeRenderer, dlgSpriteBatch);
			pointsControl_education.setPointSize(pointsize);
		}
		pointsControl_education.controlX = x_pointsControl_education;
		pointsControl_education.controlY = y_pointsControl_education;
		
		int buttoncreatex = dlgX + dialogW / 2 - 105;
		int buttoncreatey = dlgY + 15 - 2;
		if (buttonCreate == null)
			buttonCreate = new CGuiControl_Button(buttoncreatex, buttoncreatey, 100, 25, 43, 18, "ok", dlgFont, null,
					CGuiControl_Button.ButtonType.DEFAULT, town);
		buttonCreate.controlX = buttoncreatex;
		buttonCreate.controlY = buttoncreatey;
		
		int buttoncancelx = dlgX + dialogW / 2 + 5;
		int buttoncancely = dlgY + 15 - 2;
		if (buttonCancel == null)
			buttonCancel = new CGuiControl_Button(buttoncancelx, buttoncancely, 100, 25, 31, 18, "cancel", dlgFont,
					null, CGuiControl_Button.ButtonType.DEFAULT, town);
		buttonCancel.controlX = buttoncancelx;
		buttonCancel.controlY = buttoncancely;
		
		if(buttonSpaceshipTechnologist==null) {
			//buttonSpaceshipTechnologist = new CGuiControl_Button(x_pointsControl_education+60, y_pointsControl_education-40, 25, 25, -220, 14, "Spaceship Technologist", town.gameFont.bfArial2, 0.54f, labelColor, null, CGuiControl_Button.ButtonType.CHECKBOX, town);
			buttonSpaceshipTechnologist = new CGuiControl_Button(x_pointsControl_education+60, y_pointsControl_education-75, 25, 25, -220, 14, "Spaceship Technologist", town.gameFont.bfArial2, 0.54f, labelColor, null, CGuiControl_Button.ButtonType.CHECKBOX, town);
		}
	}
	
	public void showDlg(Boolean show) {
		if (show)
			init();

		Boolean w = rand.nextBoolean();
		if (w) {
			headindex = rand.nextInt(town.gameResourceConfig.listObjectHead_Women.size());
			town.gameGui.chosenHeadObj = town.gameResourceConfig.listObjectHead_Women.get(headindex);
		} else {
			headindex = rand.nextInt(town.gameResourceConfig.listObjectHead_Men.size());
			town.gameGui.chosenHeadObj = town.gameResourceConfig.listObjectHead_Men.get(headindex);
		}

		super.showDlg(show);

		if (show == true) {
			sDialogInput1 = "";
		}
	}

	public Boolean keyTyped(char character) {
		int ch = character;

		if (ch == 8) {
			if (sDialogInput1.length() > 0) {
				sDialogInput1 = sDialogInput1.substring(0, sDialogInput1.length() - 1);
				return true;
			}
		} else {
			town.gameFont.layout.setText(dlgFont, sDialogInput1 + character);
			if (town.gameFont.layout.width <= textInputW)
				sDialogInput1 += character;

			// if(sDialogInput1.length()<maxLenghtInput1)
			// sDialogInput1+=character;

			return true;
		}

		return true;
	}

	public Boolean buttonDown(int x, int y, int libgdxy, int button) {
		if (button == 0 || button == -99) {
			if (x > genderWomanX - 5 && x < genderWomanX + genderWomanW && libgdxy > genderWomanY - 10
					&& libgdxy < genderWomanY + genderWomanH) {
				if (sGender.equals("m")) {
					headindex = rand.nextInt(town.gameResourceConfig.listObjectHead_Women.size() - 1);
					town.gameGui.chosenHeadObj = town.gameResourceConfig.listObjectHead_Women.get(headindex);
				}

				sGender = "w";
				return true;
			}

			if (x > genderManX - 5 && x < genderManX + genderManW && libgdxy > genderManY - 10
					&& libgdxy < genderManY + genderManH) {
				if (sGender.equals("w")) {
					headindex = rand.nextInt(town.gameResourceConfig.listObjectHead_Men.size() - 1);
					town.gameGui.chosenHeadObj = town.gameResourceConfig.listObjectHead_Men.get(headindex);
				}

				sGender = "m";
				return true;
			}

			if (slider_skill.buttonDown(x, y, libgdxy, button))
				return true;

			if (buttonAddSkill.buttonClick(x, libgdxy)) {
				town.gameGui.optionListDlg.showDlg(true, OptionListType.Skills);
				this.showDlg(false);
			}

			if (slider.buttonDown(x, y, libgdxy, button))
				return true;
			if (slider_maxhealth.buttonDown(x, y, libgdxy, button))
				return true;
			if (slider_maxhappyness.buttonDown(x, y, libgdxy, button))
				return true;
			if (slider_intelligence.buttonDown(x, y, libgdxy, button))
				return true;
			if (slider_fitness.buttonDown(x, y, libgdxy, button))
				return true;

			// if(buttonRemoveSkills.buttonClick(x, libgdxy))
			// {
			// Gdx.app.debug("", "todo_ remove skills");
			// }

			if (buttonPagingLeft.buttonClick(x, libgdxy)) {
				headindex--;

				if (sGender.equals("w")) {
					if (headindex < 0)
						headindex = town.gameResourceConfig.listObjectHead_Women.size() - 1;

					town.gameGui.chosenHeadObj = town.gameResourceConfig.listObjectHead_Women.get(headindex);
				}
				if (sGender.equals("m")) {
					if (headindex < 0)
						headindex = town.gameResourceConfig.listObjectHead_Men.size() - 1;

					town.gameGui.chosenHeadObj = town.gameResourceConfig.listObjectHead_Men.get(headindex);
				}
			}

			if (buttonPagingRight.buttonClick(x, libgdxy)) {
				headindex++;

				if (sGender.equals("w")) {
					if (headindex > town.gameResourceConfig.listObjectHead_Women.size() - 1)
						headindex = 0;

					town.gameGui.chosenHeadObj = town.gameResourceConfig.listObjectHead_Women.get(headindex);
				}
				if (sGender.equals("m")) {
					if (headindex > town.gameResourceConfig.listObjectHead_Men.size() - 1)
						headindex = 0;

					town.gameGui.chosenHeadObj = town.gameResourceConfig.listObjectHead_Men.get(headindex);
				}
			}

			if (buttonCreate.buttonClick(x, libgdxy) || button == -99) {
				createResident();
				return true;
			}

			if(buttonSpaceshipTechnologist.buttonClick(x, libgdxy))
			{
				//if(companycount<target_companycount || residentcount<target_residentcount || avghealth<target_avghealth || avghappyness<target_avghappyness) {	
				if(residentcount<target_residentcount || avghappyness<target_avghappyness) {
					buttonSpaceshipTechnologist.toggleActive=false;
				}
				
				return true;
			}
			
			
			if (buttonCancel.buttonClick(x, libgdxy)) {
				showDlg(false);
				return true;
			}
		} else {
			createResident();
			// showDlg(false);
		}

		// Gdx.app.debug("pos", "x: " + x +",y: " + libgdxy + ", genderManX: "+
		// genderManX + ", genderManY: "+genderManY);

		return true;
	}

	public void createResident() {
		if (price > town.gameWorld.townMoney)
			return;

		if (sDialogInput1.length() < 1) {
			sDialogInput1 = CHuman.generateCitizenForename(sGender) + " " + CHuman.generateCitizenLastname();
		}

		town.gameGui.objPlacing = town.gameGui.chosenHeadObj;
		town.gameGui.objPlacing.price = price;
		town.gameGui.bObjPlacing = true;
		
		town.setAchievement("residentcustomized");
		
		showDlg(false);
	}

	public void buttonUp() {
		slider.buttonUp();
		slider_maxhealth.buttonUp();
		slider_skill.buttonUp();
		slider_maxhappyness.buttonUp();
		slider_intelligence.buttonUp();
		slider_fitness.buttonUp();
		// slider_education.buttonUp();
	}

	public void calculatePrice() {
		// age 5-120

		// health 80-200
		// intelligence 30-160
		// happyness 80-200
		// fitness 30-160

		// health attitude 0-3
		// positive attitude 0-3
		//
		// je jünger desto mehr kosten attributspunkte

		// public int iMaxhealth;
		// public int iMaxHappyness;
		// public int iIntelligence;
		// public int iFitness;
		// public float fHealthAttitude;
		// public float fPositiveAttitude;
		// public float fEducation;

		// preis steigt exponentiell bei niedrigem alter

		// es soll belohnt werden wenn man kinder erstellt
		// kinder für education notwendig?
		// -> education enorm teuer in jedem alter

		price = getBasePrice();

		price += iIntelligence * getMult(iIntelligence) * 1.5f;
		price += iFitness * getMult(iFitness);

		price += iMaxhealth * getMult(iMaxhealth);
		price += iMaxHappyness * getMult(iMaxHappyness);

		if (skillObject != null && iSkill > 0)
			price += (iSkill * getMult(iSkill)) * 20;

		price += fHealthAttitude * getMultAttitude(fHealthAttitude) * 3;
		price += fPositiveAttitude * getMultAttitude(fPositiveAttitude) * 3;
		
		float edu = fEducation * getMultAttitude(fEducation) * 80;
		if (iAge < 18)
			edu *= 200;
		price += edu;

		price *= 5;

		// Preis an Alter anpassen
		float ipricemult = getAgeMult(iAge);
		if (ipricemult < 1)
			ipricemult = 1;
		if (iAge < 18) // Preis wird wieder günstiger
		{
			float fadd = 0.1f;
			ipricemult += fadd;
			if (iAge < 17)
				ipricemult += fadd;
			if (iAge < 16)
				ipricemult += fadd;
			if (iAge < 15)
				ipricemult += fadd;
			if (iAge < 14)
				ipricemult += fadd;
			if (iAge < 13)
				ipricemult += fadd;
			if (iAge < 12)
				ipricemult += fadd;
			if (iAge < 11)
				ipricemult += fadd;
			if (iAge < 10)
				ipricemult += fadd;
			if (iAge < 9)
				ipricemult += fadd;
			if (iAge < 8)
				ipricemult += fadd;
			if (iAge < 7)
				ipricemult += fadd;
			if (iAge < 6)
				ipricemult += fadd;
		}

		price /= ipricemult;

		price /= 15;

		if(buttonSpaceshipTechnologist.toggleActive) {
			price+=1000000;
		}
				
		price *= town.residentPriceDelta;
	}

	private float getAgeMult(int iAge) {
		return 1 + iAge * 0.05f;

		// if(iAge<=18)
		// return 1f;
		// if(iAge==19)
		// return 1.1f;
		// if(iAge==20)
		// return 1.1f;
		// if(iAge==21)
		// return 1.1f;
		// if(iAge==22)
		// return 1.1f;
		// if(iAge==23)
		// return 1.1f;
		// if(iAge==24)
		// return 1.1f;
		// if(iAge==25)
		// return 1.1f;
		// if(iAge==26)
		// return 1.1f;
		// if(iAge==27)
		// return 1.1f;
		// if(iAge==28)
		// return 1.1f;
		// if(iAge==29)
		// return 1.1f;
		// if(iAge==30)
		// return 1.1f;
		// if(iAge==31)
		// return 1.1f;
		// if(iAge==32)
		// return 1.1f;
		// if(iAge==33)
		// return 1.1f;
		// if(iAge==34)
		// return 1.1f;
		// if(iAge==35)
		// return 1.1f;
		// if(iAge==36)
		// return 1.1f;
		// if(iAge==37)
		// return 1.1f;
		// if(iAge==38)
		// return 1.1f;
		// if(iAge==39)
		// return 1.1f;
		// if(iAge==40)
		// return 1.1f;
		// if(iAge==41)
		// return 1.1f;
		// if(iAge==42)
		// return 1.1f;
		// if(iAge==43)
		// return 1.1f;
		// if(iAge==44)
		// return 1.1f;
		// if(iAge==45)
		// return 1.1f;
		// if(iAge==46)
		// return 1.1f;
		// if(iAge==47)
		// return 1.1f;
		// if(iAge==48)
		// return 1.1f;
		// if(iAge==49)
		// return 1.1f;
		// if(iAge==50)
		// return 1.1f;
		// if(iAge==51)
		// return 1.1f;
		// if(iAge==52)
		// return 1.1f;
		// if(iAge==53)
		// return 1.1f;
		// if(iAge==54)
		// return 1.1f;
		// if(iAge==55)
		// return 1.1f;
		// if(iAge==56)
		// return 1.1f;
		// if(iAge==57)
		// return 1.1f;
		// if(iAge==58)
		// return 1.1f;
		// if(iAge==59)
		// return 1.1f;
		// if(iAge==60)
		// return 1.1f;

	}

	public int getBasePrice() {
		int price = 0;

		if (iAge > 5)
			price = 5000;
		if (iAge > 6)
			price = 10000;
		if (iAge > 7)
			price = 15000;
		if (iAge > 8)
			price = 20000;
		if (iAge > 9)
			price = 25000;
		if (iAge > 10)
			price = 30000;
		if (iAge > 11)
			price = 35000;
		if (iAge > 12)
			price = 40000;
		if (iAge > 13)
			price = 45000;
		if (iAge > 14)
			price = 50000;
		if (iAge > 15)
			price = 55000;
		if (iAge > 16)
			price = 60000;
		if (iAge > 17)
			price = 65000;

		return price / 10;
	}

	public float getMult(int value) {
		float mult = 1;

		int plus1 = 15;

		if (value > 10)
			mult = 1f + plus1;
		if (value > 20)
			mult = 3f + plus1;
		if (value > 30)
			mult = 6f + plus1;
		if (value > 40)
			mult = 9f + plus1;
		if (value > 50)
			mult = 12f + plus1;
		if (value > 60)
			mult = 15f + plus1;
		if (value > 70)
			mult = 18f + plus1;
		if (value > 80)
			mult = 21f + plus1;
		if (value > 90)
			mult = 24f + plus1;

		if (value > 100)
			mult = 27f + plus1;
		if (value > 105)
			mult = 30f + plus1;
		if (value > 110)
			mult = 33f + plus1;
		if (value > 115)
			mult = 36f + plus1;
		if (value > 120)
			mult = 39f + plus1;
		if (value > 130)
			mult = 42f + plus1;
		if (value > 140)
			mult = 45f + plus1;
		if (value > 150)
			mult = 48f + plus1;
		if (value > 160)
			mult = 9f + 45f + 20f + plus1;
		if (value > 170)
			mult = 10 + 70 + 28f;
		if (value > 180)
			mult = 11f + 90 + 32;
		if (value > 190)
			mult = 12f + 120 + 38;

		return mult;
	}

	public float getMultAttitude(float value) {
		float base = 60;

		if (value < 0.5f)
			return base * 1f;
		if (value < 1)
			return base * 3f;
		if (value < 1.5f)
			return base * 8f;
		if (value < 2f)
			return base * 20f;
		if (value < 2.5f)
			return base * 35;

		if (value <= 3f)
			return base * 50;

		return 1;
	}

	public Boolean mouseMovedDrag(int x, int y, int libgdxy) {
		if (slider_skill.mouseMovedDrag(x, y, libgdxy))
			return true;
		if (slider.mouseMovedDrag(x, y, libgdxy))
			return true;
		if (slider_maxhealth.mouseMovedDrag(x, y, libgdxy))
			return true;
		if (slider_maxhappyness.mouseMovedDrag(x, y, libgdxy))
			return true;
		if (slider_intelligence.mouseMovedDrag(x, y, libgdxy))
			return true;
		if (slider_fitness.mouseMovedDrag(x, y, libgdxy))
			return true;
		// if(slider_education.mouseMovedDrag(x, y, libgdxy))
		// return true;

		// Gdx.app.debug("mouseMoved", "bRoomPlacing: " +
		// bRoomPlacing.toString() + ", bObjPlacing: " + bObjPlacing.toString()
		// + ", bButtonDown: " + bButtonDown.toString());
		// if(bAgeSliderDown)
		// {
		// ageControllerX=x;
		// return true;
		// }

		return false;
	}

	public void drawLabelText(String stext, int x, int y, int width) {
		dlgFont = town.gameFont.bfArial2;
		dlgFont.getData().setScale(0.54f);

		dlgFont.setColor(labelColor);
		if (width == 0)
			dlgFont.draw(dlgSpriteBatch, stext, x, y);
		else
			dlgFont.draw(dlgSpriteBatch, stext, x, y, width, 0, false);
	}

	public void drawValueText(String stext, int x, int y, int width, Color color) {
		dlgSpriteBatch.setShader(town.gameFont.fontShader);
		dlgFont2.setColor(color);
		dlgFont2.getData().setScale(valueScale);

		if (width == 0)
			dlgFont2.draw(dlgSpriteBatch, stext, x, y);
		else
			dlgFont2.draw(dlgSpriteBatch, stext, x, y, width, 0, false);

		dlgSpriteBatch.setShader(null);
	}

	public void render(int x, int libgdxy) {
		calculatePrice();

		if (town.gameGui.chosenHeadObj.editoraction.contains("head_man"))
			sGender = "m";
		else if (town.gameGui.chosenHeadObj.editoraction.contains("head_woman"))
			sGender = "w";

		// if(!Gdx.gl.glIsEnabled(GL30.GL_BLEND))
		{
			Gdx.gl.glEnable(GL30.GL_BLEND);
			Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
		}
		dlgShapeRenderer.setAutoShapeType(true);
		dlgShapeRenderer.begin();

		// Dialog Background
		dlgShapeRenderer.set(ShapeType.Filled);
		dlgShapeRenderer.setColor(town.dialogColor);
		dlgShapeRenderer.rect(dlgX, dlgY, dialogW, dialogH);

		// Dialog Rahmen
		dlgShapeRenderer.set(ShapeType.Line);
		dlgShapeRenderer.setColor(town.dialogRahmenColor);
		dlgShapeRenderer.rect(dlgX, dlgY, dialogW, dialogH);

		// Textinput Name
		dlgShapeRenderer.set(ShapeType.Filled);
		dlgShapeRenderer.setColor(0.6f, 0.6f, 0.6f, 0.1f);
		dlgShapeRenderer.rect(textInputX + 5, textInputY, textInputW, textInputH);

		// Gender Auswahlrahmen
		dlgShapeRenderer.set(ShapeType.Line);
		dlgShapeRenderer.setColor(0.7f, 0.7f, 0.7f, 0.8f);
		if (sGender.equals("w"))
			dlgShapeRenderer.rect(genderWomanX - 3, genderWomanY - 4, genderWomanW + 7, genderWomanH + 9);
		else
			dlgShapeRenderer.rect(genderManX - 3, genderManY - 4, genderManW + 7, genderManH + 9);

		// Umrahmung Head
		dlgShapeRenderer.set(ShapeType.Filled);
		dlgShapeRenderer.setColor(0f, 0f, 0f, 0.2f);
		dlgShapeRenderer.rect(dlgX + dialogW - 135 - 5 + headx, heady, 60, 62);
		dlgShapeRenderer.set(ShapeType.Line);
		dlgShapeRenderer.setColor(0.7f, 0.7f, 0.7f, 0.2f);
		dlgShapeRenderer.rect(dlgX + dialogW - 135 - 5 + headx, heady, 60, 62);

		// Trenner Head / Name
		// int br1=30;
		// dlgShapeRenderer.setColor(0.7f, 0.7f, 0.7f, 0.1f);
		// int trx1=dlgX+dialogW-135-5+headx-br1-5+145+1;
		// int try1=dlgY+80-5+heady-br1-7+3;
		// dlgShapeRenderer.line(trx1, try1, trx1, try1+133);

		// Trenner Controls
		// dlgShapeRenderer.setColor(0.7f, 0.7f, 0.7f, 0.1f);
		// int tcx1=dlgX+335+34;
		// dlgShapeRenderer.line(tcx1, dlgY+134, tcx1, dlgY+50+440);

		// Rahmen Name
		// dlgShapeRenderer.set(ShapeType.Line);
		// dlgShapeRenderer.setColor(0.7f, 0.7f, 0.7f, 0.1f);
		// dlgShapeRenderer.rect(textInputX-60-1, textInputY-18+1,
		// textInputW+75+1, 60);
		// dlgShapeRenderer.rect(textInputX-61-1, textInputY-19+1,
		// textInputW+77+1, 62);

		// Trennlinie zu ok, cancel
		dlgShapeRenderer.setColor(town.dialogRahmenColor);
		dlgShapeRenderer.line(dlgX + 20, dlgY + 50, dlgX + dialogW - 20, dlgY + 50);

		// Zwischentrennlinien
		int ly = 30;
		dlgShapeRenderer.setColor(0.7f, 0.7f, 0.7f, 0.1f);
		// dlgShapeRenderer.line(dlgX+0, dlgY+474-posy+posyall+ly,
		// dlgX+dialogW-20+20, dlgY+474-posy+posyall+ly);
		// dlgShapeRenderer.line(dlgX+0, dlgY+385-posy+posyall+ly,
		// dlgX+dialogW-20+20, dlgY+385-posy+posyall+ly);
		// dlgShapeRenderer.line(dlgX+0, dlgY+250-posy+posyall+ly,
		// dlgX+dialogW-20+20, dlgY+250-posy+posyall+ly);
		// dlgShapeRenderer.line(dlgX+0, dlgY+250-posy-132+posyall+ly,
		// dlgX+dialogW-20+20, dlgY+250-posy-132+posyall+ly);
		
		// Price Background
		String st = "$" + price;
		dlgFont2.getData().setScale(priceScale);
		town.gameFont.layout.setText(dlgFont2, st);
		
		// Price unterstreichen
		dlgShapeRenderer.setColor(1f, 1f, 1f, 1f);
		dlgShapeRenderer.set(ShapeType.Filled);
		dlgShapeRenderer.rect((dlgX + dialogW / 2 - town.gameFont.layout.width / 2) + pricex,
				dlgY + 100 - town.gameFont.layout.height - 4 + pricey, town.gameFont.layout.width - 1, 0.9f);
		
		dlgShapeRenderer.end();
		// Gdx.gl.glDisable(GL30.GL_BLEND);
		
		dlgSpriteBatch.begin();
		dlgSpriteBatch.setColor(1, 1, 1, 1);
		
		dlgFont.getData().setScale(1);
		dlgFont.setColor(Color.WHITE);
		
		// Kopf
		// if(!Gdx.gl.glIsEnabled(GL30.GL_BLEND))
		{
			Gdx.gl.glEnable(GL30.GL_BLEND);
			Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
		}
		dlgSpriteBatch.setColor(1, 1, 1, 0.8f);
		dlgSpriteBatch.draw(town.gameGui.chosenHeadObj.textureImage, dlgX + dialogW - 135 + headx, heady + 4, 50, 50);
		dlgSpriteBatch.setColor(1, 1, 1, 1);
		
		// Name
		drawLabelText("Name", textInputX - 50, textInputY + 17, 0);
		drawLabelText(sDialogInput1, textInputX + 5, textInputY + 17, 0);
		
		// Gender
		dlgSpriteBatch.setColor(0.9f, 0.9f, 0.9f, 0.9f);
		if (x >= genderWomanX && x <= genderWomanX + genderWomanW && libgdxy >= genderWomanY
				&& libgdxy <= genderWomanY + genderWomanH)
			dlgSpriteBatch.draw(genderIcon_Woman.textureImage, genderWomanX - 1, genderWomanY - 1, genderWomanW + 2,
					genderWomanH + 2);
		else
			dlgSpriteBatch.draw(genderIcon_Woman.textureImage, genderWomanX, genderWomanY, genderWomanW, genderWomanH);

		if (x >= genderManX && x <= genderManX + genderManW && libgdxy >= genderManY
				&& libgdxy <= genderManY + genderManH)
			dlgSpriteBatch.draw(genderIcon_Man.textureImage, genderManX - 1, genderManY - 1, genderManW + 2,
					genderManH + 2);
		else
			dlgSpriteBatch.draw(genderIcon_Man.textureImage, genderManX, genderManY, genderManW, genderManH);

		drawLabelText("Gender", dlgX + 100 + 240 + 5 + righttextx, genderManY + 18, 0);

		// Age
		// editorSpriteBatch.draw(buttonControl.textureIcon, textInputX,
		// textInputY-110, iEditorIconSizeW, iEditorIconSizeH, 0, 0,
		// buttonControl.textureIcon.getWidth(),
		// buttonControl.textureIcon.getHeight(), true, false);
		// editorSpriteBatch.draw(buttonControl.textureIcon, textInputX+80,
		// textInputY-110, iEditorIconSizeW, iEditorIconSizeH, 0, 0,
		// buttonControl.textureIcon.getWidth(),
		// buttonControl.textureIcon.getHeight(), false, false);
		int slidertexty = 3;
		
		drawLabelText("Age", dlgX + 100 - 80, slider.controlY + 14 + slidertexty, 0);
		drawValueText(String.valueOf(iAge), slider.controlX - 70, slider.controlY + 18, 60, valueColor);
		
		drawLabelText("Skill", dlgX + 100 - 50, slider_skill.controlY + 14 + slidertexty, 0);
		drawValueText("" + iSkill, slider_skill.controlX - 70, slider_skill.controlY + 18, 60, valueColor);
		
		if (skillObject != null)
			sskills = skillObject.jobname;
		drawLabelText(sskills, dlgX + 20, slider_skill.controlY - 20, 0);
		
		drawLabelText("Max Health", dlgX + 100 - 80, slider_maxhealth.controlY + 14 + slidertexty, 0);
		drawValueText("" + iMaxhealth, slider_maxhealth.controlX - 70, slider_maxhealth.controlY + 18, 60, valueColor);
		
		drawLabelText("Max Happiness", dlgX + 100 - 80, slider_maxhappyness.controlY + 14 + slidertexty, 0);
		drawValueText("" + iMaxHappyness, slider_maxhappyness.controlX - 70, slider_maxhappyness.controlY + 18, 60,
				valueColor);

		drawLabelText("Intelligence", dlgX + 100 - 80, slider_intelligence.controlY + 14 + slidertexty, 0);
		drawValueText("" + iIntelligence, slider_intelligence.controlX - 70, slider_intelligence.controlY + 18, 60,
				valueColor);

		drawLabelText("Fitness", dlgX + 100 - 80, slider_fitness.controlY + 14 + slidertexty, 0);
		drawValueText("" + iFitness, slider_fitness.controlX - 70, slider_fitness.controlY + 18, 60, valueColor);

		// drawLabelText("Education", textInputX+240,
		// slider_education.controlY+14, 0);
		// drawValueText(""+ (int)fEducation, slider_education.controlX-70,
		// slider_education.controlY+18, 60, Color.WHITE);

		drawLabelText("Health Attitude", dlgX + 100 + 240 + 7 + righttextx,
				pointsControl_healthAttitude.controlY + 14 + slidertexty, 0);

		drawLabelText("Positive Attitude", dlgX + 100 + 240 + 7 + righttextx,
				pointsControl_positiveAttitude.controlY + 14 + slidertexty, 0);

		drawLabelText("Education", dlgX + 100 + 240 + 7 + righttextx,
				pointsControl_education.controlY + 14 + slidertexty, 0);

		Color pcol = valueColor;
		if (price > town.gameWorld.townMoney)
			pcol = Color.RED;

		float temp1 = valueScale;
		valueScale = priceScale;
		drawValueText(st, (int) (dlgX + dialogW / 2 - town.gameFont.layout.width / 2) + pricex, dlgY + 100 + pricey, 0,
				pcol);
		valueScale = temp1;

		dlgSpriteBatch.end();

		slider.render();
		iAge = (int) slider.getValue();

		slider_skill.render();
		iSkill = (int) slider_skill.getValue();

		slider_maxhealth.render();
		iMaxhealth = (int) slider_maxhealth.getValue();

		slider_maxhappyness.render();
		iMaxHappyness = (int) slider_maxhappyness.getValue();

		slider_intelligence.render();
		iIntelligence = (int) slider_intelligence.getValue();

		slider_fitness.render();
		iFitness = (int) slider_fitness.getValue();

		// slider_education.render();
		// fEducation=slider_education.getValue();

		pointsControl_education.render(x, libgdxy);
		fEducation = pointsControl_education.getValue();

		pointsControl_healthAttitude.render(x, libgdxy);
		fHealthAttitude = pointsControl_healthAttitude.getValue();

		pointsControl_positiveAttitude.render(x, libgdxy);
		fPositiveAttitude = pointsControl_positiveAttitude.getValue();
		
		buttonPagingLeft.render(x, libgdxy);
		buttonPagingRight.render(x, libgdxy);
		
		buttonAddSkill.render(x, libgdxy);
		// buttonRemoveSkills.render(x, libgdxy);
		
		// buttonAddSkill.renderTooltip(x, libgdxy);
		// buttonRemoveSkills.renderTooltip(x, libgdxy);
		
		buttonCreate.render(x, libgdxy);
		buttonCancel.render(x, libgdxy);
		
		buttonSpaceshipTechnologist.render(x, libgdxy);
		
		if(x>buttonSpaceshipTechnologist.controlX && x<buttonSpaceshipTechnologist.controlX+buttonSpaceshipTechnologist.controlW &&
				libgdxy>buttonSpaceshipTechnologist.controlY && libgdxy<buttonSpaceshipTechnologist.controlY+buttonSpaceshipTechnologist.controlH) {
			
			companycount = town.gameWorld.worldCompanyList.stream().collect(Collectors.toCollection(() -> new TreeSet<CCompany>((p1, p2) -> p1.companyname.compareTo(p2.companyname)))).size();
			avghealth = town.gameWorld.townStatistics.getCurrentStatistics_Population().healthAVG; 
			avghappyness = town.gameWorld.townStatistics.getCurrentStatistics_Population().happinessAVG; 
			residentcount = town.gameWorld.worldHumans.size();
			
			//companycount=target_companycount;
			//residentcount=target_residentcount;
			
			town.gameGui.tooltip.textLines.clear();
			town.gameGui.tooltip.textLines.add("_Ability to research Spaceship Technology");
			town.gameGui.tooltip.textLines.add("");
			town.gameGui.tooltip.textLines.add("_Spaceship Technologists have some Requirements for their Community:");
			
			//if(companycount>=target_companycount)
			//	town.gameGui.tooltip.textLines.add("All companies are available (" + companycount + "/"+target_companycount+")");
			//else
			//	town.gameGui.tooltip.textLines.add("r_All possible companies are available (" + companycount + "/" + target_companycount+")");
			
			if(residentcount>=target_residentcount)
				town.gameGui.tooltip.textLines.add("Community has "+ target_residentcount + " or more residents (" + residentcount + "/"+target_residentcount+")");
			else
				town.gameGui.tooltip.textLines.add("r_Community has " + target_residentcount + " or more residents (" + residentcount + "/" + target_residentcount+")");
			
			if(avghappyness>=target_avghappyness)
				town.gameGui.tooltip.textLines.add("Average happiness is greater than "+target_avghappyness+" ("+ avghappyness + "/"+target_avghappyness+")");
			else
				town.gameGui.tooltip.textLines.add("r_Average happiness is greater than "+target_avghappyness+" ("+ avghappyness + "/"+target_avghappyness+")");
			
			//if(avghealth>=target_avghealth)
			//	town.gameGui.tooltip.textLines.add("Average health is greater than 80 (" + avghealth + "/"+target_avghealth+")");
			//else
			//	town.gameGui.tooltip.textLines.add("r_Average health is greater than 80 (" + avghealth + "/" + target_avghealth+ ")");
			
			town.gameGui.tooltip.textLines.add("");
			
			
			town.gameGui.tooltip.drawFormat(x-550, libgdxy+200, 200);
		}
	}
}
