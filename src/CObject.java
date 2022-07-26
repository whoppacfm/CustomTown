package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Rect;
import com.mygdx.game.CHelper.IntersectionMode;
import com.sun.javafx.font.directwrite.RECT;


public class CObject {
	
	public CTown town;
	
	//public int objecttype; //can move? -> different collision list 
	public CObjecttype objecttype;
	public String objectId;
	public String objectTypeId;
	public String objectName;
	public String linkobjectid;
	public String sBaseObject; //editoraction des baseobjects für placing
	
	public CWorldObject baseWorldObject;
		
	int iOneTimeBonus;
	
	//Research Infos
	int iResearchCurrentWorkoutput;
	int iResearchTargetWorkoutput;
	String sResearchBaseObjectId;
	Boolean bIsResearchDesignObject;
	public int workoutputConsumption;
	
	public int price;
	public int tempprice;
	public int original_price;
	public int objectAction_w;
	public int objectAction_h;
	
	public int iRasterValue; 
	public int iRasterValue_movx;
	public int iRasterValue_movy;
	
	public int pos_x;
	public int pos_y;
	public int width;
	public int height;
	public int tempwidth;
	public int tempheight;
	public int original_width;
	public int original_height;
	
	public int icon_pos_x;
	public float icon_pos_y;
	public int icon_width;
	public int icon_height;	
	
	public CWorldObject tempRoom;
	
	public int ObjectAction_Rotation;
	public int ObjectAction_Move_Pixels_X;
	public int ObjectAction_Move_Pixels_Y;
	
	//public int ObjectAction_Move_Direction2;
	//public int ObjectAction_Move_Pixels2;
	
	int maxObjectCount; //max objects des gleichen typs auf einer adr
	
	public int rotation;
	
	//public Texture textureIcon;
	
	
	public Texture textureIcon;
	
	public Texture textureImage;
	public TextureRegion[] textureRegion;
	public String textureFilename;
	public Animation objectAnimation;
	public int frameCols;
	public int frameRows;
	
	public Texture textureImage2;
	public Animation objectAnimation2;
	public TextureRegion[] textureRegion2;
	
	public Texture textureImage3;
	public Animation objectAnimation3;
	public TextureRegion[] textureRegion3;
	
	public int move_startFrame;
	public int move_endFrame;
	public int action1_startFrame;
	public int action1_endFrame;
	public int idleFrame;
	public int zorder;
	public int placingCountLevel; //placing floors
	public Boolean mipmap;
	
	public String editoraction; //zB Raum erstellen
	public String roomtype;
	public String coltype; //Kollisionstyp: 0=no collision, 1=pathmap and placing collision and , 2=only placing collision no pathmap 
	
	public Boolean doRasterPlacement;
	public Boolean drawShadow;
	
	public int rotationValue;
	
	public Boolean isGarbageContainer;
	public Boolean isWallObject;
	public Boolean isRoomObject;
	public Boolean isGroundObject;
	public Boolean isGroundBaseObject;
	public Boolean isWaterObject;
	public Boolean isCar;
	public Boolean bBedTooClose;
	
    //Illuminati Defense
	float defense_schussfrequenz_timer;
    float defense_schussfrequenz;
	float defense_reichweite;
	float defense_schaden;
	float defense_projektilgeschwindigkeit;
		
	String ATTR_ICON;
	public int ATTR_RPLACING;
	int ATTR_PLACEBONUS;
	int ATTR_PLACECOUNT;
	int ATTR_OBJCONDITION;
	float ATTR_GREENBONUS; //derzeit nicht in benutzung
	float ATTR_NICEBONUS;  //derzeit nicht in benutzung
	float ATTR_DESIGNBONUS;  //derzeit nicht in benutzung
	int ATTR_BASEREQ;
	int ATTR_MIPMAPPING;
	int ATTR_SHADOW;
	int ATTR_COMFORT;
	int ATTR_HAPPINESS;
	int ATTR_REQEDU;
	float ATTR_MONEY;
	int ATTR_BOUNDXMOV;
	int ATTR_BOUNDYMOV;
	int ATTR_BOUNDX;
	int ATTR_BOUNDY;
	int ATTR_ENERGYIN;
	int ATTR_ENERGYOUT;
	int ATTR_WATERIN;
	int ATTR_WATEROUT;
	int ATTR_LIGHT;
	int ATTR_HEAT;
	int ATTR_FILL1;
	int ATTR_FILL2;
	int ATTR_FILL3;
	int ATTR_FUELCONS;
	float ATTR_VEHICLE_SPEEDONROAD;
	float ATTR_VEHICLE_SPEEDONTERRAIN;
	float ATTR_ROAD_SPEED;
	String ATTR_BUYINFO;
	int ATTR_RESPCOUNT; //Required Population Count, sonst nicht in Research Liste anzeigen
	String ATTR_RESINFO;
	String ATTR_T2;
	int ATTR_T2COLS;
	int ATTR_T2ROWS;
	int ATTR_T2W;
	int ATTR_T2H;
	int ATTR_T2X;
	int ATTR_T2Y;
	String ATTR_T3;
	int ATTR_T3COLS;
	int ATTR_T3ROWS;
	
	
	public CObject(CTown t)
	{
		ATTR_RPLACING=0;
		baseWorldObject=null;
		linkobjectid="";
		workoutputConsumption=0;
		ATTR_MIPMAPPING=-1;
		ATTR_OBJCONDITION=0;
		ATTR_BUYINFO="";
		ATTR_MONEY=0;
		ATTR_REQEDU=-1;
		ATTR_COMFORT=0;
		ATTR_HAPPINESS=0;
		ATTR_SHADOW=0;
		ATTR_BASEREQ=0;
		ATTR_PLACEBONUS=0;
		ATTR_PLACECOUNT=0;
		ATTR_GREENBONUS=0;
		ATTR_NICEBONUS=0;
		ATTR_DESIGNBONUS=0;
		ATTR_ICON="";
		ATTR_BOUNDXMOV=0;
		ATTR_BOUNDYMOV=0;
		ATTR_BOUNDX=0;
		ATTR_BOUNDY=0;
		ATTR_ENERGYIN=0;
		ATTR_ENERGYOUT=0;
		ATTR_WATERIN=0;
		ATTR_WATEROUT=0;
		ATTR_LIGHT=0;
		ATTR_HEAT=0;
		ATTR_FILL1=0;
		ATTR_FILL2=0;
		ATTR_FILL3=0;
		ATTR_FUELCONS=0;
		ATTR_ROAD_SPEED=0;
		ATTR_VEHICLE_SPEEDONROAD=0;
		ATTR_VEHICLE_SPEEDONTERRAIN=0;		
		ATTR_RESINFO="";
		ATTR_RESPCOUNT=0;
		ATTR_T2="";
		ATTR_T2COLS=0;
		ATTR_T2ROWS=0;
		ATTR_T2W=0;
		ATTR_T2H=0;
		ATTR_T2X=0;
		ATTR_T2Y=0;
		ATTR_T3="";
		ATTR_T3COLS=0;
		ATTR_T3ROWS=0;
		
		defense_schussfrequenz_timer=0;
		defense_schussfrequenz=10;
		defense_reichweite=1600;
		defense_schaden=1;
		defense_projektilgeschwindigkeit=80;
		//defense_reichweite=3000;
		
		sBaseObject="";
		tempRoom=null;
		iRasterValue=0;
		town=t;
		bBedTooClose=false;
		doRasterPlacement = false;
		isRoomObject=false;
		isGarbageContainer=false;
		isCar=false;
		isWallObject=false;
		isRoomObject=false;
		isGroundObject=false;
		isGroundBaseObject=false;
		isWaterObject=false;
		placingCountLevel=1; //placing floors
		rotationValue=10; //ACHTUNG muss in 10er Schritten erfolgen, bei änderung muss funktion moveVectorByRotation angepasst werden
		zorder=1;
		price=0;
		original_price=0;
		tempprice=0;
		editoraction="";
		roomtype="";
		objectAction_w=0;
		objectAction_h=0;
		objecttype = null;
		objectId="0";
		objectTypeId="0";
		objectName="";
		objectAction_w=0;
		objectAction_h=0;
		pos_x=0;
		pos_y=0;
		original_width=0;
		original_height=0;
		width=0;
		height=0;
		icon_pos_x=0;
		icon_pos_y=0;
		icon_width=0;
		icon_height=0;	
		ObjectAction_Rotation=0;
		ObjectAction_Move_Pixels_X=0;
		ObjectAction_Move_Pixels_Y=0;
		rotation=0;
		
		textureIcon=null;
		textureImage=null;
		textureRegion=null;
		textureFilename="";
		objectAnimation=null;
		frameCols=0;
		frameRows=0;
		
		textureImage2=null;
		objectAnimation2=null;
		textureRegion2=null;
		textureImage3=null;
		objectAnimation3=null;
		textureRegion3=null;
		
		move_startFrame=0;
		move_endFrame=0;
		action1_startFrame=0;
		action1_endFrame=0;
		idleFrame=0;
		placingCountLevel=1;
		editoraction="";
		coltype=""; 
		mipmap=true;
		iResearchTargetWorkoutput=0;
		iResearchCurrentWorkoutput=0;
		iOneTimeBonus=0;
		sResearchBaseObjectId="";
		bIsResearchDesignObject=false;
		maxObjectCount=0;
		drawShadow=true;
		
		
		//bringt hier nichts
		//if(editoraction.contains("coffeepot"))
		//	drawShadow=false;
		//if(editoraction.contains("laundryobject"))
		//	drawShadow=false;
	}
	
	public Boolean isResearched()
	{
		if(iResearchTargetWorkoutput<=0)
			return true;
		
		if(iResearchCurrentWorkoutput<iResearchTargetWorkoutput)
			return false;
		
		return true;
	}
		
	public void drawBoundingPolygon(CExtendedShapeRenderer shapeRenderer1)
	{
		Color col = new Color(0.8f, 0, 0, 0.2f);
		Gdx.gl.glEnable(GL30.GL_BLEND);
		Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
		shapeRenderer1.filledPolygon(getBoundingPolygon(true, IntersectionMode.COLLISION).getTransformedVertices(), col);// getBoundingPolygon(IntersectionMode.COLLISION).getTransformedVertices());
	}
	
	public Boolean IsDrawAdditionalObject()
	{
		if(editoraction.contains("ground_water"))
			return true;

		if(editoraction.contains("company_gasstation_gaspump"))
			return true;
		if(editoraction.contains("_coffeemachine"))
			return true;
		if(editoraction.contains("_foosball"))
			return true;
		if(editoraction.contains("bathroom_toilet") || editoraction.contains("toiletroom_toiletstall"))
			return true;
		if(editoraction.contains("company_doctorsoffice_treatment_chair"))
			return true;
		if(editoraction.contains("company_college_workingplace_researchlab"))
			return true;
		if(editoraction.contains("company_fitnessstudio_latpull"))
			return true;
		if(editoraction.contains("company_pub_workingplace_bar") || editoraction.contains("company_pub_table"))
			return true;
		if(editoraction.contains("company_pub_workingplace_bar") || editoraction.contains("company_pub_table"))
			return true;
		if(editoraction.contains("livingroom_tv"))
			return true;
		if(editoraction.contains("company_school_workingplace_studentsdesk") || editoraction.contains("company_school_workingplace_teachersdesk"))
			return true;
		if(editoraction.contains("company_school_workingplace_teachersdesk"))
			return true;
		if(editoraction.contains("company_college_workingplace_profslectern"))
			return true;
		if(editoraction.contains("company_recyclingcenter_recyclingmachine3"))
			return true;
		if(editoraction.contains("company_urbancemetery_rostrum"))
			return true;
		if(editoraction.contains("company_college_workingplace_studentsdesk"))
			return true;
		if(editoraction.contains("bedroom_nightstand"))
			return true;
		if(editoraction.contains("kitchen_sink"))
			return true;
		if(editoraction.contains("kitchen_stove"))
			return true;
		if(editoraction.contains("diningroom_diningtable"))
			return true;

		return false;
	}
	
	public void renderDefenseZone(ShapeRenderer sr1)
	{
		//ACHTUNG: Funktion gibt es auch auf CWorldObject
		/*
		if (theobject.editoraction.contains("illuminati_defensesystem"))
		{
			sr1.setColor(0.0f, 0.4f, 0.0f, 0.5f);
			sr1.set(ShapeType.Filled);
			sr1.circle(pos_x()+width/2, pos_y()+height/2, getDefense_reichweite());
		}
		
		if (theobject.editoraction.contains("illuminati_defensewarning") && !bDefenseAlarm)
		{
			sr1.setColor(0.0f, 0.4f, 0.0f, 0.5f);
			sr1.set(ShapeType.Filled);
			sr1.circle(pos_x()+width/2, pos_y()+height/2, getDefense_reichweite());
		}
		*/
		if (editoraction.contains("company_waterworks_groundwaterextractionsystem"))
		{
			sr1.setColor(0.0f, 0.4f, 0.0f, 0.5f);
			sr1.set(ShapeType.Filled);
			sr1.circle(pos_x+width/2, pos_y+height/2, getWaterOutput()*60);
		}
		
	}
	
	public Boolean isDrawSpecial() //Objekte die außerhalb des framebuffers gezeichnet werden müssen wegen animation, fortbewegung, ...
	{
		if(town.gameWorld.gameResourceConfig.isSpecialObject(editoraction))
			return true;
		if(town.gameWorld.gameResourceConfig.isOnlyDrawSpecialObject(editoraction))
			return true;
		
		if(editoraction.contains("recyclingcenter_garbagebag"))
			return true;
		if(editoraction.contains("coffeepot1"))
			return true;
		if(editoraction.contains("laundryobject"))
			return true;
		if(editoraction.contains("coffeebeans1"))
			return true;
		if(editoraction.contains("supermarket_buyin"))
			return true;
		if(editoraction.contains("traffic_car"))
			return true;
		if(editoraction.contains("supermarket_pallettruck"))
			return true;
		
		//if(editoraction.contains("interior_light")) //müssen über resident sein
		//	return true;
		
		
		return false;
	}
	
	public void setDynamicAttributes()
	{
		//Watersystem Attribute nach Größe
		if(editoraction.contains("waterworks_groundwaterextractionsystem"))
		{
			//energin		getEnergyConsumption
			//waterout		getWaterOutput
			//workinput		getNeededWorkinputPerHour
			
			int maxsize=width; //getMaxSize(0);
			
			ATTR_ENERGYIN=width/33;
			if(maxsize>=getMaxSize(1))
				ATTR_ENERGYIN=width/33;
			if(maxsize>=getMaxSize(2))
				ATTR_ENERGYIN=width/34;
			if(maxsize>=getMaxSize(3))
				ATTR_ENERGYIN=width/35;
			if(maxsize>=getMaxSize(4))
				ATTR_ENERGYIN=width/36;
			if(maxsize>=getMaxSize(5))
				ATTR_ENERGYIN=width/37;
			if(maxsize>=getMaxSize(6))
				ATTR_ENERGYIN=width/38;
			if(maxsize>=getMaxSize(7))
				ATTR_ENERGYIN=width/39;
			if(maxsize>=getMaxSize(8))
				ATTR_ENERGYIN=width/40;
			if(maxsize>=getMaxSize(9))
				ATTR_ENERGYIN=width/41;
			
			//ATTR_ENERGYIN=width/33;
			//ATTR_ENERGYIN=width/33;
			//ATTR_WATEROUT=width/17;
			
//			if(width<1800)
//				ATTR_WATEROUT=width/14;	
//			if(width<1700)
//				ATTR_WATEROUT=width/14;	
//			if(width<1600)
//				ATTR_WATEROUT=(int) (width/14.5f);	
//			if(width<1500)
//				ATTR_WATEROUT=(int) (width/14.5f);	
//			if(width<1400)
//				ATTR_WATEROUT=(int) (width/15f);	
//			if(width<1300)
//				ATTR_WATEROUT=width/15;	
//			if(width<1200)
//				ATTR_WATEROUT=(int) (width/15.5f);	
//			if(width<1100)
//				ATTR_WATEROUT=(int) (width/15.5f);	
//			if(width<1000)
//				ATTR_WATEROUT=(int) (width/16f);	
//			if(width<900)
//				ATTR_WATEROUT=(int) (width/16f);
//			if(width<800)
//				ATTR_WATEROUT=(int) (width/16.5f);
//			if(width<700)
//				ATTR_WATEROUT=width/16;
//			if(width<600)
//				ATTR_WATEROUT=(int) (width/16.5f);
//			if(width<500)
			//ATTR_WATEROUT=width/17;
			//ATTR_WATEROUT=width/15;
			//ATTR_WATEROUT=width/13; //test mehr wateroutput
			ATTR_WATEROUT=width/10; //test mehr wateroutput
			
			if(maxsize>=getMaxSize(1))
				ATTR_WATEROUT=width/9;
			if(maxsize>=getMaxSize(2))
				ATTR_WATEROUT=width/9;
			if(maxsize>=getMaxSize(3))
				ATTR_WATEROUT=width/8;
			if(maxsize>=getMaxSize(4))
				ATTR_WATEROUT=width/8;
			if(maxsize>=getMaxSize(5))
				ATTR_WATEROUT=width/7;
			if(maxsize>=getMaxSize(6))
				ATTR_WATEROUT=width/7;
			if(maxsize>=getMaxSize(7))
				ATTR_WATEROUT=width/6;
			if(maxsize>=getMaxSize(8))
				ATTR_WATEROUT=width/6;
			if(maxsize>=getMaxSize(9))
				ATTR_WATEROUT=width/5;
			
			//Gdx.app.debug("", "width: " + width + ", ATTR_WATEROUT: " + ATTR_WATEROUT);
			workoutputConsumption=width/130;
			
			if(maxsize>=getMaxSize(1))
				workoutputConsumption=width/133;
			if(maxsize>=getMaxSize(2))
				workoutputConsumption=width/136;
			if(maxsize>=getMaxSize(3))
				workoutputConsumption=width/139;
			if(maxsize>=getMaxSize(4))
				workoutputConsumption=width/142;
			if(maxsize>=getMaxSize(5))
				workoutputConsumption=width/145;
			if(maxsize>=getMaxSize(6))
				workoutputConsumption=width/148;
			if(maxsize>=getMaxSize(7))
				workoutputConsumption=width/152;
			if(maxsize>=getMaxSize(8))
				workoutputConsumption=width/154;
			if(maxsize>=getMaxSize(9))
				workoutputConsumption=width/156;
			
		}
	}
	
	public int getMaxSize(int itype)
	{
		int maxsize=400;
		
		if(editoraction.contains("waterworks_groundwaterextractionsystem"))
		{
			if(itype==0)
			{
				if(town.gameWorld.gameResourceConfig.isObjectResearched("function_resizewatersystem"))
					maxsize=getMaxSize(1);
				
				if(town.gameWorld.gameResourceConfig.isObjectResearched("function_resizewatersystem2"))
					maxsize=getMaxSize(2);
				
				if(town.gameWorld.gameResourceConfig.isObjectResearched("function_resizewatersystem3"))
					maxsize=getMaxSize(3);
				
				if(town.gameWorld.gameResourceConfig.isObjectResearched("function_resizewatersystem4"))
					maxsize=getMaxSize(4);
				
				if(town.gameWorld.gameResourceConfig.isObjectResearched("function_resizewatersystem5"))
					maxsize=getMaxSize(5);
				
				if(town.gameWorld.gameResourceConfig.isObjectResearched("function_resizewatersystem6"))
					maxsize=getMaxSize(6);
		
				if(town.gameWorld.gameResourceConfig.isObjectResearched("function_resizewatersystem7"))
					maxsize=getMaxSize(7);
				
				if(town.gameWorld.gameResourceConfig.isObjectResearched("function_resizewatersystem8"))
					maxsize=getMaxSize(8);				
				
				if(town.gameWorld.gameResourceConfig.isObjectResearched("function_resizewatersystem9"))
					maxsize=getMaxSize(9);				
			}
			
			if(itype==1)
				maxsize=900;
			if(itype==2)
				maxsize=1050;
			if(itype==3)
				maxsize=1150;
			if(itype==4)
				maxsize=1250;
			if(itype==5)
				maxsize=1350;
			if(itype==6)
				maxsize=1450;
			if(itype==7)
				maxsize=1550;
			if(itype==8)
				maxsize=1650;
			if(itype==9)
				maxsize=1750;
		}
		
		return maxsize;
	}
	
	public int getResizeByScrollingPrice(int oldsize, int newsize) //Room, Ground
	{
		float factor=1;
		
		if(isRoomObject)
			factor=1.4f;
		if(isGroundObject)
			factor=0.5f;
		if(editoraction.contains("anyroom_carpet"))
			factor=0.7f;
		
		int p1=(int) (original_price+(Math.abs(oldsize-newsize)*factor));		
		
		if(isGroundObject && p1 < original_price)
			p1=(int) (original_price+(Math.abs(oldsize-newsize)*factor));
		
		if(isGroundObject)
		{
			//Gdx.app.debug("", ""+width);
			//if(width>1500)
			//{
			//	p1+=width;
			//}
		}
		
		return p1;
	}
	
	public Boolean workplaceHasSkill_taskbased() //eigene skill erhöhung - nicht pro stunde
	{
		if(editoraction.contains("company_doctorsoffice_treatment_chair"))
			return true;
		
		if(editoraction.contains("company_fitnessstudio_fitnessworkingplace"))
			return true;
				
		return false;
	}
	
	public void setAttribute(String attr, String val)
	{
		////Optional:[RESEARCHWO];[RESEARCHID];[ENERGYIN];[WATERIN];[ENERGYOUT];[WATEROUT];[LIGHT];[HEAT];[FILL1];[FILL2];[FUELCONS];[SPEEDROAD];[SPEEDTERR];[COLTYPE]
		attr=attr.toUpperCase();
		
		if(attr.equals("RESWO"))
		{
			iResearchTargetWorkoutput = Integer.parseInt(val);
			iResearchTargetWorkoutput=(int) (iResearchTargetWorkoutput*town.resDelta);
		}
		
		if(attr.equals("RPLACING"))
			ATTR_RPLACING=Integer.parseInt(val);
		if(attr.equals("RESID"))
			sResearchBaseObjectId = val;
		if(attr.equals("OBJCONDITION"))
			ATTR_OBJCONDITION=Integer.parseInt(val);
		if(attr.equals("RESINFO"))
			ATTR_RESINFO=val;
		if(attr.equals("RESPCOUNT"))
			ATTR_RESPCOUNT=Integer.parseInt(val);
		if(attr.equals("MM"))
			ATTR_MIPMAPPING=Integer.parseInt(val);
		if(attr.equals("DESID"))
			sResearchBaseObjectId = val;
		if(attr.equals("DESINFO"))
			ATTR_RESINFO=val;
		if(attr.equals("DESPCOUNT"))
			ATTR_RESPCOUNT=Integer.parseInt(val);
		if(attr.equals("DESWO"))
		{
			iResearchTargetWorkoutput = Integer.parseInt(val);
			iResearchTargetWorkoutput=(int) (iResearchTargetWorkoutput*town.desDelta);
			bIsResearchDesignObject=true;
		}
		if(attr.equals("WOCONS"))
			workoutputConsumption=Integer.parseInt(val);;
		if(attr.equals("COLTYPE"))
			coltype = val;
		if(attr.equals("ENERGYIN"))
			ATTR_ENERGYIN=Integer.parseInt(val);
		if(attr.equals("WATERIN"))
			ATTR_WATERIN=Integer.parseInt(val);
		if(attr.equals("ENERGYOUT"))
			ATTR_ENERGYOUT=Integer.parseInt(val);
		if(attr.equals("WATEROUT"))
			ATTR_WATEROUT=Integer.parseInt(val);
		if(attr.equals("LIGHT"))
			ATTR_LIGHT=(int)town.getSizeValue(Integer.parseInt(val));
		if(attr.equals("HEAT"))
			ATTR_HEAT=(int)town.getSizeValue(Integer.parseInt(val));
		if(attr.equals("FILL1"))
			ATTR_FILL1=Integer.parseInt(val);
		if(attr.equals("FILL2"))
			ATTR_FILL2=Integer.parseInt(val);
		if(attr.equals("FILL3"))
			ATTR_FILL3=Integer.parseInt(val);
		if(attr.equals("FUELCONS"))
			ATTR_FUELCONS=Integer.parseInt(val);
		if(attr.equals("ROAD_SPEED"))
			ATTR_ROAD_SPEED=Float.parseFloat(val);
		if(attr.equals("VEHICLE_SPEEDONROAD"))
			ATTR_VEHICLE_SPEEDONROAD=Float.parseFloat(val);
		if(attr.equals("VEHICLE_SPEEDONTERRAIN"))
			ATTR_VEHICLE_SPEEDONTERRAIN=Float.parseFloat(val);
		if(attr.equals("T2"))
			ATTR_T2=val;
		if(attr.equals("T2COLS"))
			ATTR_T2COLS=Integer.parseInt(val);
		if(attr.equals("T2ROWS"))
			ATTR_T2ROWS=Integer.parseInt(val);
		
		if(attr.equals("T2W"))
			ATTR_T2W=(int) town.getSizeValue(Integer.parseInt(val));
		if(attr.equals("T2H"))
			ATTR_T2H=(int) town.getSizeValue(Integer.parseInt(val));
		if(attr.equals("T2X"))
			ATTR_T2X=(int) town.getSizeValue(Integer.parseInt(val));
		if(attr.equals("T2Y"))
			ATTR_T2Y=(int) town.getSizeValue(Integer.parseInt(val));
		
		if(attr.equals("T3"))
			ATTR_T3=val;
		if(attr.equals("T3COLS"))
			ATTR_T3COLS=Integer.parseInt(val);
		if(attr.equals("T3ROWS"))
			ATTR_T3ROWS=Integer.parseInt(val);
		if(attr.equals("BOUNDX"))
			ATTR_BOUNDX=(int) town.getSizeValue(Integer.parseInt(val));
		if(attr.equals("BOUNDY"))
			ATTR_BOUNDY=(int) town.getSizeValue(Integer.parseInt(val));
		if(attr.equals("MONEY"))
			ATTR_MONEY=Float.parseFloat(val)*town.moneyDelta;
		if(attr.equals("REQEDU"))
			ATTR_REQEDU=Integer.parseInt(val);
		if(attr.equals("COMFORT"))
			ATTR_COMFORT=Integer.parseInt(val);
		if(attr.equals("HAPPINESS"))
			ATTR_HAPPINESS=Integer.parseInt(val);
		if(attr.equals("SHADOW"))
			ATTR_SHADOW=(int) town.getSizeValue(Integer.parseInt(val));
		if(attr.equals("BUYINFO"))
			ATTR_BUYINFO=val;
		if(attr.equals("ICON"))
			ATTR_ICON=val;
		if(attr.equals("BOUNDXM"))
			ATTR_BOUNDXMOV=(int) town.getSizeValue(Integer.parseInt(val));
		
		if(attr.equals("PLACEBONUS"))
			ATTR_PLACEBONUS=Math.round(Float.parseFloat(val)*town.grantsDelta);
		if(attr.equals("PLACECOUNT"))
			ATTR_PLACECOUNT=Integer.parseInt(val);
		
		if(attr.equals("BOUNDYM"))
			ATTR_BOUNDYMOV=(int)town.getSizeValue(Integer.parseInt(val));		
		if(attr.equals("ZORDER"))
			zorder=Integer.parseInt(val);
		if(attr.equals("BASEREQ"))
			ATTR_BASEREQ=Integer.parseInt(val);
		if(attr.equals("GREENBONUS"))
			ATTR_GREENBONUS=Float.parseFloat(val);
		if(attr.equals("NICEBONUS"))
			ATTR_NICEBONUS=Float.parseFloat(val);
		if(attr.equals("DESIGNBONUS"))
			ATTR_DESIGNBONUS=Float.parseFloat(val);
				
		if(attr.equals("DEFENSE_SHOTFREQUENCY"))
			defense_schussfrequenz=Float.parseFloat(val);
		if(attr.equals("DEFENSE_RANGE"))
			defense_reichweite=(int)town.getSizeValue(Float.parseFloat(val));
		if(attr.equals("DEFENSE_DAMAGE"))
			defense_schaden=Float.parseFloat(val);
		if(attr.equals("DEFENSE_SHOTSPEED"))
			defense_projektilgeschwindigkeit=Float.parseFloat(val);
	}
	
	public Boolean workplaceHasSkill()
	{
		if(editoraction.contains("company_church_workingplace_battlepriest"))
			return true;

		if(editoraction.contains("company_construction_pickup1_traffic_car"))
			return true;
		
		if(editoraction.contains("company_church_workingplace_altar"))
			return true;
		
		if(editoraction.contains("officeworkingplace"))
			return true;
		
		if(editoraction.contains("company_electricalworks_generator"))
			return true;
		
		if(editoraction.contains("company_electricalworks_solar"))
			return true;
		
		if(editoraction.contains("company_waterworks_groundwaterextractionsystem"))
			return true;
		
		if(editoraction.contains("company_college_workingplace_researchlab"))
			return true;
		
		if(editoraction.contains("diningroom_diningtable"))
			return true;
		
		if(editoraction.contains("company_urbancemetery_rostrum"))
			return true;

		if(editoraction.contains("company_doctorsoffice_treatment_chair"))
			return true;

		if(editoraction.contains("company_fitnessstudio_fitnessworkingplace"))
			return true;

		if(editoraction.contains("company_college_workingplace_profslectern"))
			return true;

		if(editoraction.contains("company_school_workingplace_teachersdesk"))
			return true;
				
		return false;
	}
	
	public Boolean isCompanyTaskObject()
	{
		//if(theobject.editoraction.contains("supermarket_shelf"))
		//	return true;
		
		if(editoraction.contains("company_urbancemetery_hearse_traffic_car"))
			return true;

		if(editoraction.contains("company_urbancemetery_rostrum"))
			return true;
				
		//if(theobject.editoraction.contains("electricalworks_generator"))
		//	return true;
		//if(theobject.editoraction.contains("electricalworks_solar"))
		//	return true;
		//if(theobject.editoraction.contains("waterworks_groundwaterextractionsystem"))
		//	return true;
		
		return false;
	}
	
	public Boolean isCompanyWorkingPlace()
	{
		if(editoraction.contains("workingplace") && editoraction.contains("company"))
			return true;

		if(editoraction.contains("pizzaoven"))
			return true;
		
		if(editoraction.contains("pizzataxi"))
			return true;
		
		if(editoraction.contains("illuminati_defensesystem"))
			return true;
		
		if(editoraction.contains("illuminati_defensewarningsystem"))
			return true;
		
		if(editoraction.contains("electricalworks_generator"))
			return true;
		
		if(editoraction.contains("waterworks_groundwaterextractionsystem"))
			return true;
		
		if(editoraction.contains("electricalworks_solar"))
			return true;
		
		if(editoraction.contains("supermarket_shelf"))
			return true;
		
		if(editoraction.contains("company_recyclingcenter_garbagetruck"))
			return true;

		if(editoraction.contains("company_construction_pickup1_traffic_car"))
			return true;
		
		if(editoraction.contains("company_doctorsoffice_treatment_chair"))
			return true;
		
		if(editoraction.contains("company_doctorsoffice_reception_desk"))
			return true;
		
		//if(theobject.editoraction.contains("company_urbancemetery_hearse_traffic_car"))
		//	return true;
		
		//if(theobject.editoraction.contains("supermarket_shelf"))
		//	return true;
		
		return false;
	}
	
	public float getObjectEducation()
	{
		if(editoraction.contains("livingroom_bookshelf"))
			return 1f;
		
		return 0;
	}
	
	public String getCompanyWorkingPlaceJobTitle(int type)
	{
		String str = editoraction;
		
		//type: 0: lang, 1: kurz
		
		if(str.contains("company_church_workingplace_battlepriest"))
			return "Battle Priest";

		if(str.contains("company_construction_pickup1_traffic_car"))
			return "Construction Worker";
		
		if(str.contains("pizzaservice_pizzaoven"))
			return "Pizza Cook";
		
		if(str.contains("company_church_workingplace_altar"))
			return "Pastor";
		
		if(str.contains("company_doctorsoffice_treatment_chair"))
			return "Doctor";
		
		if(str.contains("company_architecturebureau_officeworkingplace"))
			return "Architect";
		
		if(str.contains("illuminati_defensesystem"))
		{
			if(type==1)
				return "Maintenance Worker";
			return "Maintenance Worker / Illuminati Defense System";
		}
		
		if(str.contains("illuminati_defensewarningsystem"))
		{
			if(type==1)
				return "Maintenance Worker";
			return "Maintenance Worker / Illuminati Defense Warning System";
		}
		
		
		if(str.contains("electricalworks_solar"))
		{
			if(type==1)
				return "Maintenance Worker";
			return "Maintenance Worker / " + objectName;//Solar Energy System";
		}
		
		if(str.contains("electricalworks_generator"))
		{
			if(type==1)
				return "Maintenance Worker";
			
			return "Maintenance Worker / " + objectName; //Electrical Works";
		}		
		
		if(str.contains("waterworks_groundwaterextractionsystem"))
		{
			if(type==1)
				return "Maintenance Worker";
			
			return "Maintenance Worker / Groundwater Extraction System";
		}
		
		if(str.contains("electricalworks_officeworkingplace"))
		{
			if(type==1)
				return "Office Worker";
			
			return "Office Worker / Electrical Works";
		}
		
		if(str.contains("fitnessstudio"))
		{
			if(type==1)
				return "Trainer";
			
			return "Trainer / Fitness Studio";
		}
		
		if(str.contains("waterworks_officeworkingplace"))
		{
			if(type==1)
				return "Office Worker";
			
			return "Office Worker / Waterworks";
		}
		
		if(str.contains("supermarket_officeworkingplace"))
		{
			if(type==1)
				return "Office Worker";
			
			return "Office Worker / Supermarket";
		}
		
		if(str.contains("supermarket_checkoutworkingplace"))
		{
			if(type==1)
				return "Checkout Worker";
			
			return "Checkout Worker / Supermarket";
		}
		
		if(str.contains("supermarket_shelf"))
		{
			if(type==1)
				return "Warehouse Worker";
			
			return "Warehouse Worker / Supermarket";
		}

		if(str.contains("softwarecompany_officeworkingplace2"))
			return "Software Developer / Physics Simulation";
		
		if(str.contains("softwarecompany_officeworkingplace"))
			return "Software Developer / Application";
		
		if(str.contains("company_school_workingplace_teachersdesk"))
			return "School Teacher";
		
		if(str.contains("company_college_workingplace_profslectern"))
			return "College Professor";
		
		if(str.contains("company_college_workingplace_studentsdesk"))
			return "College Student";
		
		if(str.contains("company_pub_workingplace_bar"))
		{
			if(type==1)
				return "Barkeeper";
			
			return "Barkeeper / Pub";
		}
		
		if(str.contains("company_school_workingplace_studentsdesk"))
			return "School Student";
		
		if(str.contains("company_recyclingcenter_garbagetruck"))
			return "Garbage Truck Driver";

		if(str.contains("company_construction_pickup1_traffic_car"))
			return "Construction Worker";
		
		if(str.contains("company_recyclingcenter_officeworkingplace"))
		{
			if(type==1)
				return "Office Worker";
			
			return "Office Worker / Recycling Center";
		}
		
		if(str.contains("company_college_workingplace_researchlab"))
		{
			if(type==1)
				return "Research Scientist";
			
			return "Research Scientist / College Research Lab";
		}
		
		if(str.contains("company_gasstation_workingplace_cashpoint"))
		{
			if(type==1)
				return "Checkout Worker";
			
			return "Checkout Worker / Gas Station";
		}
		
		if(str.contains("company_urbancemetery_hearse_traffic_car"))
		{
			if(type==1)
				return "Hearse Driver";
			
			return "Hearse Driver / Urban Cemetery";
		}
		
		if(str.contains("company_urbancemetery_rostrum"))
		{
			if(type==1)
				return "Funeral Speaker";
			
			return "Funeral Speaker / Urban Cemetery";
		}
		
		if(str.contains("diningroom_diningtable"))
			return "Cook";
		
		if(str.contains("company_townhall_officeworkingplace_finance"))
		{
			if(type==1)
				return "Office Worker";
			
			return "Office Worker / Town Hall Finance Statistics";
		}
		
		if(str.contains("company_townhall_officeworkingplace_resident"))
		{
			if(type==1)
				return "Office Worker";
			
			return "Office Worker / Town Hall Population Statistics";
		}
		
		if(str.contains("company_townhall_officeworkingplace_other"))
		{
			if(type==1)
				return "Office Worker";
			
			return "Office Worker / Town Hall Other Statistics";
		}
		
		if(str.contains("company_doctorsoffice_reception_desk"))
		{
			if(type==1)
				return "Medical Receptionist";
			
			return "Medical Receptionist / Doctor's Office";
		}
		
		return objectName;
	}
	
	public Boolean isCompanyObjectFillingByWorkerObject()
	{
		if(editoraction.contains("supermarket_shelf"))
			return true;
		
		return false;
	}
	
	public String getWorkerTitle()
	{
		if(isMaintenanceObject())
			return "Maintenance Worker";
		
		if(isCompanyObjectFillingByWorkerObject())
		{
			if(editoraction.contains("supermarket_shelf"))
				return "Warehouse Worker";
				
			return "Responsible Worker";
		}
		
		if(isFoodFillingByWorkerObject())
			return "Responsible Resident";
		
		if(editoraction.contains("company_school_workingplace_teachersdesk"))
			return "Teacher";
		
		if(editoraction.contains("company_college_workingplace_profslectern"))
			return "Professor";

		if(editoraction.contains("pizzaservice_pizzaoven"))
			return "Pizza Cook";
		
		if(editoraction.contains("company_pub_workingplace_bar"))
			return "Barkeeper";
		
		if(editoraction.contains("company_school_workingplace_studentsdesk"))
			return "School Student";
		
		if(editoraction.contains("company_college_workingplace_studentsdesk"))
			return "College Student";
		
		if(editoraction.contains("company_college_workingplace_researchlab"))
			return "Research Scientist";

		if(editoraction.contains("company_doctorsoffice_treatment_chair"))
			return "Doctor";
		
		if(editoraction.contains("officeworkingplace"))
			return "Office Worker";
		
		if(editoraction.contains("company_church_workingplace_battlepriest"))
			return "Battle Priest";
		
		if(editoraction.contains("company_construction_pickup1_traffic_car"))
			return "Construction Worker";
				
		return "Worker";
	}
	
	public Boolean isMaintenanceObject()
	{
		if(editoraction.contains("electricalworks_solar") 
				|| editoraction.contains("electricalworks_generator") 
				|| editoraction.contains("waterworks_groundwaterextractionsystem")
				|| editoraction.contains("illuminati_defensesystem")
				|| editoraction.contains("illuminati_defensewarningsystem")
				)
			return true;
		
		return false;
	}

	public Boolean isFoodFillingByWorkerObject()
	{
		if(editoraction.contains("kitchen_fridge"))
			return true;
		
		return false;	
	}
	
	public float getRequiredWorkplaceEducation()
	{
		if(ATTR_REQEDU>-1)
			return ATTR_REQEDU;
		
		
		//Medical Receptionist
		//if(editoraction.contains("company_doctorsoffice_reception_desk"))
		//	return 1f;
		//Doctor
		//if(editoraction.contains("company_doctorsoffice_treatment_chair"))
		//	return 3f;
		//Architect
		//if(editoraction.contains("company_architecturebureau_officeworkingplace"))
		//	return 2f;
		//Dinner Cook
		//if(editoraction.contains("diningroom_diningtable"))
		//	return 0f;
		//Fitness Studio
		//if(editoraction.contains("company_fitnessstudio"))
		//	return 1f;
		//Electrical Works
		//if(editoraction.contains("company_electricalworks_officeworkingplace"))
		//	return 1f;
		//if(editoraction.contains("company_electricalworks_solar"))
		//	return 1f;
		//if(editoraction.contains("company_electricalworks_generator"))
		//	return 1f;
		//Waterworks
		//if(editoraction.contains("company_waterworks_officeworkingplace"))
		//	return 1f;
		//if(editoraction.contains("company_waterworks_groundwaterextractionsystem"))
		//	return 1f;
		//Supermarket
		//if(editoraction.contains("company_supermarket_officeworkingplace"))
		//	return 1f;
		//if(editoraction.contains("company_supermarket_shelf"))
		//	return 1f;
		//if(editoraction.contains("company_supermarket_checkoutworkingplace"))
		//	return 1f;
		//Recycling Center
		//if(editoraction.contains("company_recyclingcenter_officeworkingplace"))
		//	return 1;
		//if(editoraction.contains("company_recyclingcenter_garbagetruck_traffic_car"))
		//	return 1;
		//Software Company
		//if(editoraction.contains("company_softwarecompany_officeworkingplace"))
		//	return 3f;
		//School
		//if(editoraction.contains("company_school_workingplace_teachersdesk"))
		//	return 2f;
		//Bar
		//if(editoraction.contains("company_pub_workingplace_bar1"))
		//	return 0f;
		//College
		//if(editoraction.contains("company_college_workingplace_profslectern"))
		//	return 3f;
		if(editoraction.contains("company_college_workingplace_studentsdesk"))
			return town.initial_requirededucationforcollege;
		//Research Lab
		//if(editoraction.contains("company_college_workingplace_researchlab"))
		//	return 3f;
		//Gas Station Checkout
		//if(editoraction.contains("company_gasstation_workingplace_cashpoint"))
		//	return 1f;
		//Urban Cemetery
		//if(editoraction.contains("company_urbancemetery_hearse_traffic_car"))
		//	return 1;
		//if(editoraction.contains("company_urbancemetery_rostrum"))
		//	return 0;
		//Townhall Office
		//if(editoraction.contains("company_townhall_officeworkingplace"))
		//	return 2;
		//Object Design
		//if(editoraction.contains("company_objectdesign_officeworkingplace_artist"))
		//	return 2;
		
		
		return -1;
	}
	
	public int getEnergyConsumption()
	{

		if(ATTR_ENERGYIN>0)
		{
			//Gdx.app.debug("", "" + ATTR_ENERGYIN);
			return (int) (ATTR_ENERGYIN*town.delta_energyconsumption);
		}

		
//		if(editoraction.contains("tv"))
//			return 3;
//		if(editoraction.contains("fridge"))
//			return 2;
//		if(editoraction.contains("electricalworks_workingplace"))
//			return 4;
//		if(editoraction.contains("waterworks_workingplace"))
//			return 4;
//		if(editoraction.contains("supermarket_checkoutworkingplace"))
//			return 4;
//		if(editoraction.contains("supermarket_pallettruck"))
//			return 6;
//		if(editoraction.contains("workingplace"))
//			return 2;
//		if(editoraction.contains("_stove"))
//			return 2;
//		if(editoraction.contains("_pinball"))
//			return 3;
//		if(editoraction.contains("_arcademachine"))
//			return 3;
//		if(editoraction.contains("_treadmill"))
//			return 2;
//		if(editoraction.contains("_stationarybike"))
//			return 2;
//		if(editoraction.contains("company_gasstation_gaspump"))
//			return 8;
//		if(editoraction.contains("company_urbancemetery_rostrum"))
//			return 1;
//		if(editoraction.contains("company_anycompany_server"))
//			return 4;
//		if(editoraction.contains("company_doctorsoffice_treatment_chair"))
//			return 3;
//		if(editoraction.contains("laundryroom_washingmachine"))
//			return 3;
//		if(editoraction.contains("laundryroom_dryer"))
//			return 3;
//		if(editoraction.contains("_coffeemachine"))
//			return 2;
		
		return 0;
	}
	
	public int getWaterConsumption()
	{
				
		if(ATTR_WATERIN>0)
		{
			return 1;
			//return (int) (ATTR_WATERIN*town.delta_waterconsumption);
		}
		
//		if(editoraction.contains("_radiator"))
//			return 1;
//		if(editoraction.contains("kitchen_sink"))
//			return 2;
//		if(editoraction.contains("bathroom_sink"))
//			return 2;
//		if(editoraction.contains("bathroom_toilet"))
//			return 3;
//		if(editoraction.contains("bathroom_shower"))
//			return 5;
//		if(editoraction.contains("bathroom_bathtub"))
//			return 7;
//		if(editoraction.contains("company_pub_workingplace_bar1"))
//			return 3;
//		if(editoraction.contains("company_doctorsoffice_treatment_chair"))
//			return 2;
//		if(editoraction.contains("_laundryroom_washingmachine"))
//			return 4;
//		if(editoraction.contains("_toiletroom_urinal"))
//			return 2;
//		if(editoraction.contains("_toiletroom_toiletstall"))
//			return 3;
//		if(editoraction.contains("_toiletroom_sink"))
//			return 2;
//		if(editoraction.contains("toiletroom_m_urinal"))
//			return 2;
//		if(editoraction.contains("_coffeemachine"))
//			return 1;
		
		return 0;
	}
	
	public int getWaterOutput()
	{
		if(ATTR_WATEROUT>0)
			return (int) (ATTR_WATEROUT*town.delta_wateroutput); 
		
		if(editoraction.contains("company_waterworks_groundwaterextractionsystem"))
			return 20;
		
		return 0;
	}
	
	public int getEnergyOutput()
	{
		if(ATTR_ENERGYOUT>0)
			return (int) (ATTR_ENERGYOUT*town.delta_energyoutput); 
		
		//if(editoraction.contains("electricalworks_solar"))
		//	return 20;
		
		//if(editoraction.contains("electricalworks_generatorv1"))
		//	return 5;
		
		//if(editoraction.contains("electricalworks_generatorv2"))
		//	return 10;
		
		return 0;
	}
	
	public int getNeededWorkinputPerHour()
	{
		//		if(editoraction.contains("company_fitnessstudio_"))
		//		{
		//			if(!editoraction.contains("_floor") && !editoraction.contains("_officeworkingplace"))
		//				return 5;
		//		}
		
		//if(editoraction.contains("company_recyclingcenter_garbagetruck_traffic_car"))
		//	return 5;
		
		//		if(obj.editoraction.contains("company_urbancemetery_graveempty"))
		//			neededWorkInputPerHour=1;
		//		if(obj.editoraction.contains("company_urbancemetery_grave"))
		//			neededWorkInputPerHour=1;
		//		if(obj.editoraction.contains("company_urbancemetery_rostrum"))
		//			neededWorkInputPerHour=1;
		//		if(obj.editoraction.contains("company_urbancemetery_hearse_traffic_car"))
		//			neededWorkInputPerHour=5;
		
		return Math.round(workoutputConsumption*town.delta_workoutputconsumption);
				
		//		if(editoraction.contains("company_recyclingcenter_recyclingmachine"))
		//			return (int)(10*town.delta_workoutputconsumption);
		//		
		//		if(editoraction.contains("electricalworks_solar"))
		//			return (int)(5*town.delta_workoutputconsumption);
		//		
		//		if(editoraction.contains("electricalworks_generatorv1"))
		//			return (int)(5*town.delta_workoutputconsumption);
		//		
		//		if(editoraction.contains("electricalworks_generatorv2"))
		//			return (int)(5*town.delta_workoutputconsumption);
		//		
		//		if(editoraction.contains("company_waterworks_groundwaterextractionsystem"))
		//			return (int)(5*town.delta_workoutputconsumption);
		//		
		//		if(editoraction.contains("company_supermarket_foodpallet"))
		//			return (int)(20*town.delta_workoutputconsumption);
		
		//return 0;
	}
	
	public String getObjectInfoText()
	{
		if(editoraction.contains("company_anycompany_server1"))
			return "Maximum Company Workoutput +500";
		if(editoraction.contains("company_anycompany_server2"))
			return "Maximum Company Workoutput +1000";
		if(editoraction.contains("company_anycompany_server3"))
			return "Maximum Company Workoutput +1500";
		if(editoraction.contains("company_softwarecompany_officeworkingplace"))
			return "";
		
		//if(editoraction.contains("nightstand"))
		//	return "Prevents Oversleeping";
		//if(editoraction.contains("footpath"))
		//	return "Residents can walk faster";
		
		if(editoraction.contains("road_road_parkingspace"))
			return "";
		if(editoraction.contains("residential_garage"))
			return "";
				
		if(editoraction.contains("company_recyclingcenter_recyclingmachine2"))
			return "Better Recycling Process";
		
		if(editoraction.contains("company_recyclingcenter_recyclingmachine3"))
			return "Better Recycling Process";
		
		if(editoraction.contains(""))
			return "";
		if(editoraction.contains(""))
			return "";
		if(editoraction.contains(""))
			return "";
		if(editoraction.contains(""))
			return "";
		if(editoraction.contains(""))
			return "";
		
		
		return "";
	}
		
	public String getHumanAttributeAffectionText()
	{
		//text oder den genauen wert anzeigen?
		//verschiedene bookshelfs, tvs..?
		
		if(editoraction.contains("indoor_plant")) // && editoraction.contains("interior"))
			return "Makes people more happy";
		
		if(editoraction.contains("_carpet"))
			return "Makes people more happy";
		
		if(editoraction.contains("window"))
			return "Makes people more happy";
		
		if(editoraction.contains("object_door"))
			return "Looks more realistic";
		
		//if(editoraction.contains("_bookshelf"))
		//	return "Happiness+, Intelligence+";
		//if(editoraction.contains("_tv"))
		//	return "Happiness+, Intelligence-, Fitness-";
		//		if(editoraction.contains(""))
		//			return "";
		//		if(editoraction.contains(""))
		//			return "";
		//		if(editoraction.contains(""))
		//			return "";
		//		if(editoraction.contains(""))
		//			return "";
		//		if(editoraction.contains(""))
		//			return "";
		//		if(editoraction.contains(""))
		//			return "";
		
		return "";
	}
		
	public ArrayList<String> getInfoTextBox()
	{
		ArrayList<String> infos = new ArrayList<String>();

//		spezielle beschreibung für spezielle objekte
//		--------------------------------------------
//		textzeile		
		
//		attribute beeinflussung
//		-----------------------
//		text was beeinflusst wird, evtl wert
//			health
//			happiness
//			fitness
//			intelligence
//			education
//			max company workoutput
		
		String inf = getObjectInfoText();
		if(!inf.isEmpty())
			infos.add(inf);
				
		if(ATTR_PLACEBONUS>0 && ATTR_PLACECOUNT>0 && iOneTimeBonus==0)
			infos.add("$" + ATTR_PLACEBONUS + " One-Time Bonus for placing " + ATTR_PLACECOUNT + " of this kind");
		
		if(!ATTR_BUYINFO.isEmpty())
			infos.add(ATTR_BUYINFO);
		
		String attr = getHumanAttributeAffectionText();
		if(!attr.isEmpty())
			infos.add(attr);
		
		if(ATTR_ROAD_SPEED>0)
			infos.add("Speed Value: " + ATTR_ROAD_SPEED);
		
		if(editoraction.contains("playground") && iOneTimeBonus==0)
			infos.add("Complete Playground Bonus: 10000");
		
		//if(ATTR_COMFORT>0 && town.bComfort)
		//if(ATTR_COMFORT>0)
		//	infos.add("+ Energy");
		//if(editoraction.contains("_tv"))
		//{
		//	infos.add("+ Happiness");
		//	infos.add("- Intelligence");
		//	infos.add("- Fitness");
		//}
		
		//if(editoraction.contains("_book"))
		//{
		//	infos.add("+ Happiness");
		//	infos.add("+ Intelligence");
		//	infos.add("- Fitness");
		//}
		
		//	infos.add("Comfort: " + ATTR_COMFORT);
		
		if(getWaterOutput()>0)
			infos.add("Water Output: " + getWaterOutput());
		
		if(getEnergyOutput()>0)
			infos.add("Energy Output: " + getEnergyOutput());
		
		if(getWaterConsumption()>0)
			infos.add("Water Consumption: " + getWaterConsumption());
		
		if(getEnergyConsumption()>0)
			infos.add("Energy Consumption: " + getEnergyConsumption());
		
		if(getVehicleSpeed_Road()>0)
			infos.add("Road Speed: " + getVehicleSpeed_Road_Text(getVehicleSpeed_Road()));
		
		if(getVehicleSpeed_Terrain()>0)
			infos.add("Terrain Speed: " + getVehicleSpeed_Terrain_Text(getVehicleSpeed_Terrain()));
		
		if(getFuelValueMax()>0)
			infos.add("Max Fuel: " + getFuelValueMax());
		
		if(getDeltaFuelConsumption()>0)
			infos.add("Fuel Consumption: " + getDeltaFuelConsumptionName(getDeltaFuelConsumption()));
		
		if(!editoraction.contains("_coffeemachine"))
		{
			int maxcap=0;
			
			maxcap=getObjectFillingMax();
			maxcap=getObjectFillingMultiMax();
			
			if(maxcap>0)
				infos.add("Capacity: " + maxcap);
		}
		
		//if(editoraction.contains("_coffeemachine"))
		//	infos.add("Requires Coffee Beans (Fridge)");
		//if(editoraction.contains("_fruitplate"))
		//	infos.add("Requires Fruits (Fridge)");
		
		if(getRadiatorHeatingPower()>0)
			infos.add("Heating Power: " + getRadiatorHeatingPower());
		
		if(getLightPower()>0)
			infos.add("Power: " + getLightPower());		
		
		if(getNeededWorkinputPerHour()>0)
			infos.add("Office Workoutput Consumption: " + getNeededWorkinputPerHour()+"/hour");
		
		if(ATTR_BASEREQ>0 && town.bBaseGround)
			infos.add("Requires Base Ground");
		
		//Required: coffee beans (Fridge)
		
		if(getMinAge()>0)
			infos.add("Worker/Responsible is at least " + getMinAge() + " years old");
		
		int reqed = Math.round(getRequiredWorkplaceEducation());
		if(reqed>0)
		{
			String sed="";
			if(reqed==1)
				sed="(Highschool)";
			if(reqed==2)
				sed="(Bachelor)";
			if(reqed==3)
				sed="(Master)";

			infos.add("Required Education: " + reqed+" "+sed);
		}
		
		if(ATTR_NICEBONUS>0)
			infos.add("Additional Nice Town Bonus Level: " + CHelper.roundFloat(ATTR_NICEBONUS,1));
		if(ATTR_GREENBONUS>0)
			infos.add("Green Town Bonus Level: " + CHelper.roundFloat(ATTR_GREENBONUS,1));
		if(ATTR_DESIGNBONUS>0)
			infos.add("Additional Town Design Bonus Level: " + CHelper.roundFloat(ATTR_DESIGNBONUS,1));
		
		String splacing = getSpecialPlacingText();
		if(!splacing.isEmpty())
			infos.add(splacing);
				
		if(editoraction.contains("anycompany_server"))
		{
			infos.add("Only one server instance for each company");
		}
				
		return infos;
	}
	
	public int getMinAge()
	{
		if(isCompanyWorkingPlace() || isCompanyTaskObject() || editoraction.contains("illuminati"))
			return 18;
		else if(isResidentialTaskObject())
			return 14;
	
		return 0;
	}
	
	public Boolean isResidentialTaskObject()
	{
		if(editoraction.contains("laundrybasket"))
			return true;
		
		if(editoraction.contains("kitchen_fridge"))
			return true;
		
		if(editoraction.contains("diningroom_diningtable"))
			return true;
		
		return false;
	}
		
	public int getSkillObjectId()
	{
		//4009013005
		int ret = Integer.parseInt(objectId);
		if(editoraction.contains("diningroom_diningtable"))
			ret=999999999;
		
		return ret;
	}
		 
	public String getSpecialPlacingText()
	{
		if(editoraction.contains("_fruitplate"))
			return "Requires Breakroom Table";
		
		//if(editoraction.contains("recyclingcenter_garbagecontainer"))
		//	return "Must be placed inside any Address";
		
		if(editoraction.contains("company_anycompany_server"))
			return "Requires Office Room";
		
		//if(editoraction.contains("bedroom_bed"))
		//	return "Should be placed at a distance from other beds";
		
		//if(editoraction.contains("bedroom_nightstand"))
		//	return "Should be placed near any bed";
		
		//if(editoraction.toLowerCase().contains("anyroom"))
		//	return "Requires any Building";
		
		return "";
	}
	
	public int getObjectFillingMultiMax()
	{
		if(ATTR_FILL3>0)
			return ATTR_FILL3;
		
		// 1 mal umziehen==3, handtuch==1
		//if(editoraction.contains("bedroom_wardrobe"))
		//	return 40;
		if(editoraction.contains("laundryroom_washingmachine"))
			return 25;
		if(editoraction.contains("laundryroom_dryer"))
			return 25;
		if(editoraction.contains("laundrybasket"))
			return 25;
		//if(editoraction.contains("bathroom_towelcabinet"))
		//	return 12;
		
		return 0;
	}
	
	public int getObjectFillingMax2()
	{
		if(ATTR_FILL2>0)
			return ATTR_FILL2;
		
		if(editoraction.contains("_coffeemachine"))
			return 12; //Tassen
		
		return 0;
	}
	
	public void setTempSize(int type)
	{
		if(type==0) //reset default size
		{
			width=tempwidth;
			height=tempheight;
		}
		
		if(type==1) //set big size for placing/moving
		{
			tempwidth=width;
			tempheight=height;
			
			//width*=1.12f;
			//height*=1.12f;
		}
	}
	public int getObjectFillingMax()
	{
		if(ATTR_FILL1>0)
			return ATTR_FILL1;
				
		//Food
		
		if(editoraction.contains("_coffeemachine"))
			return 20; //Coffee
		
		//if(editoraction.contains("fridge"))
		//	return town.fridge_foodstock_max;
		
		if(editoraction.contains("supermarket_shoppingcart"))
			return town.supermarket_shoppingcart_foodstock_max;
		
		if(editoraction.contains("supermarket_shelf"))
			return town.supermarket_shelf_foodstock_max;
		
		//if(editoraction.contains("supermarket_pallettruck"))
		//	return 400;
		
		if(editoraction.contains("supermarket_foodpallet"))
			return town.supermarket_warehouse_foodstock_max;
		
		if(editoraction.contains("bedroom_wardrobe"))
			return 40;
		
		if(editoraction.contains("bathroom_towelcabinet"))
			return 12;
		
		if(editoraction.contains("fruitplate"))
			return 8;
				
		//----------------------------------------------------------------
				
		//Garbage
		
		if(editoraction.contains("recyclingcenter_garbagecontainer"))
			return 50;

		if(editoraction.contains("garbagecan"))
			return 20;
		
		if(editoraction.contains("company_recyclingcenter_garbagetruck"))
			return 250;
		
		
		return 0;
	}
	
	public String getObjectFilling2Text()
	{
		if(editoraction.contains("_coffeemachine"))
			return "Cups";
		
		return "";
	}
	
	public String getObjectFillingText()
	{
		if(editoraction.contains("traffic_car_residential"))
			return "Trunk";
		
		if(editoraction.contains("supermarket_buyin"))
			return "Food";
		
		if(editoraction.contains("fridge"))
			return "Food";
		
		if(editoraction.contains("_coffeemachine"))
			return "Coffee";
		
		if(editoraction.contains("supermarket_shoppingcart"))
			return "Food";
		
		if(editoraction.contains("supermarket_shelf"))
			return "Food";
		
		if(editoraction.contains("supermarket_pallettruck"))
			return "Food";		
		
		if(editoraction.contains("supermarket_foodpallet"))
			return "Food";
		
		if(editoraction.contains("recyclingcenter_garbagecontainer"))
			return "Garbage";
		
		if(editoraction.contains("garbagecan"))
			return "Garbage";
		
		if(editoraction.contains("company_recyclingcenter_garbagetruck"))
			return "Garbage";
		
		if(editoraction.contains("bedroom_wardrobe"))
			return "Clothing";
		if(editoraction.contains("laundryroom_washingmachine"))
			return "Laundry";
		if(editoraction.contains("laundryroom_dryer"))
			return "Laundry";
		if(editoraction.contains("laundrybasket"))
			return "Laundry";
		if(editoraction.contains("bathroom_towelcabinet"))
			return "Towles";
		
		return "";
	}
	
	public int getRadiatorHeatingPower()
	{
		if(ATTR_HEAT>0)
			return ATTR_HEAT; 
		
		if(editoraction.contains("radiator0"))
			return 300;
		if(editoraction.contains("radiator1"))
			return 400;
		if(editoraction.contains("radiator2"))
			return 500;
		
		return 0;
	}
	
	public int getLightPower()
	{
		if(ATTR_LIGHT>0)
			return ATTR_LIGHT; 
				
		if(editoraction.contains("interior_light1"))
			return 300;
		if(editoraction.contains("interior_light2"))
			return 350;
		if(editoraction.contains("interior_light3"))
			return 400;
		if(editoraction.contains("interior_light4"))
			return 450;		
		if(editoraction.contains("interior_light5"))
			return 500;	
		if(editoraction.contains("interior_light6"))
			return 550;	
		if(editoraction.contains("interior_light7"))
			return 600;	
		if(editoraction.contains("interior_light8"))
			return 650;
		
		
		if(editoraction.contains("outdoor_light1"))
			return 400;
		if(editoraction.contains("outdoor_light2"))
			return 500;
		if(editoraction.contains("outdoor_light3"))
			return 600;
		if(editoraction.contains("outdoor_light4"))
			return 700;
		if(editoraction.contains("outdoor_light5"))
			return 800;
		if(editoraction.contains("outdoor_light6"))
			return 900;
		if(editoraction.contains("outdoor_light7"))
			return 1000;
		if(editoraction.contains("light"))
			return 200;
			//return 570;
		
		return 0;
	}
	
	public int getFuelValueMax()
	{
		if(editoraction.contains("traffic_car"))
			return 100;

		return 0;
	}
	
	public float getDeltaFuelConsumption()
	{
		if(ATTR_FUELCONS>0)
			return ATTR_FUELCONS;
		
		if(editoraction.contains("traffic_car"))
		{
			//return 3f; //Extremely High
			//return 2.5f; //Very High
			//return 2f; //High
			//return 1.5f; //Medium
			
			return 1;
		}
		
		return 0;
	}
	
	public String getDeltaFuelConsumptionName(float val)
	{
		if(val<=1)
			return "Low";
		if(val<=1.5f)
			return "Medium";
		if(val<=2)
			return "High";
		if(val<=2.5f)
			return "Very High";
		if(val>=2.5f)
			return "Extremely High";
		
		return "";	
	}
	
	public float getVehicleSpeed_Road()
	{
		if(ATTR_VEHICLE_SPEEDONROAD>0)
			return ATTR_VEHICLE_SPEEDONROAD;
		
		if(editoraction.contains("traffic_car"))
		{
			//return 15; //Very Fast
			//return 13; //Fast
			return 11;   //Medium
			//return 9;  //Slow
			//return 7;  //Very Slow
		}
		
		return 0;
	}
	
	public String getVehicleSpeed_Road_Text(float val)
	{
		if(val>=15)
			return "Very Fast";
		if(val>=13 && val<15)
			return "Fast";
		if(val>=11 && val<13)
			return "Medium";
		if(val>7 && val<11)
			return "Slow";
		if(val<=7)
			return "Very Slow";
		
		return "";
	}
	
	public float getVehicleSpeed_Terrain()
	{
		if(ATTR_VEHICLE_SPEEDONTERRAIN>0)
			return ATTR_VEHICLE_SPEEDONTERRAIN;
		
		if(editoraction.contains("traffic_car"))
		{
			//return 6; //Very Fast
			//return 5; //Fast
			//return 4; //Medium
			return 3;	//SLow
			//return 2;	//Very Slow
		}
		
		return 0;
	}
	
	public String getVehicleSpeed_Terrain_Text(float f)
	{
		if(f>=6)
			return "Very Fast";
		if(f>=5 && f<6)
			return "Fast";
		if(f>=4 && f<5)
			return "Medium";
		if(f>=3 && f<4)
			return "Slow";
		if(f<=2)
			return "Very Slow";
		
		return "";
	}
	
	public String getCompanyTypeString()
	{
		String companyString="";

		try
		{
			String[] companyStringA = editoraction.split("_");
			companyString = companyStringA[0] + "_" + companyStringA[1];
		}
		catch(Exception e){}
		
		return companyString;
	}	
	
	public Boolean isAddressObject() //Address Object muss auf einer Adresse platziert werden
	{
		if(roomtype.contains("outdoor"))
			return false;
		
		return true;
	}
	
	public Boolean isHuman()
	{
		if(editoraction.contains("head_"))
			return true;
		
		if(editoraction.contains("human_"))
			return true;
		
		return false;
	}
	
	public Boolean isResidentialAddressObject() //Ausschlussliste Objekte die hier nicht platziert werden dürfen
	{
		//für objectplacing zum ausschließen dass objekte unpassend platziert werden
		
		//		if(editoraction.contains("road_"))
		//			return false;
		//		if(editoraction.contains("outside_"))
		//			return false;
		//		if(editoraction.contains("traffic_"))
		//			return false;
		
		//if(editoraction.contains(("recyclingcenter_garbagecontainer")))
		//		return true;
		
		if(editoraction.contains("company_"))
			return false;
		
		if(editoraction.contains("road_parkingspace"))
			return false;
		
		return true;
	}
	
	public Boolean isPublicOrCommercialAddressObject() //Ausschlussliste Objekte die hier nicht platziert werden dürfen
	{
		//für objectplacing zum ausschließen dass objekte unpassend platziert werden
		
		//if(editoraction.contains("road_"))
		//	return false;
		//if(editoraction.contains("outside_"))
		//	return false;
		//if(editoraction.contains("traffic_"))
		//	return false;
		
		if(editoraction.equals("residential"))
			return false;
		
//		if(editoraction.contains("residential_garage"))
//			return false;
//		if(editoraction.contains("bedroom"))
//			return false;
//		if(editoraction.contains("livingroom"))
//			return false;
//		if(editoraction.contains("anyroomresidential"))
//			return false;
//		if(editoraction.contains("bathroom"))
//			return false;
//		if(editoraction.contains("laundryroom"))
//			return false;
		
		if(editoraction.contains("head_"))
			return false;
		
		//if(editoraction.contains("recyclingcenter_garbagecontainer"))
		//	return false;
		
		//if(editoraction.contains("diningroom"))
		//	return true;
		//if(editoraction.contains("kitchen"))
		//	return true;
		
		return true;
	}	
	
	public Boolean isFloraObject()
	{
		if(editoraction.contains("outdoor_tree"))
			return true;
		
		if(editoraction.contains("outdoor_plant") || editoraction.contains("outdoor_flower"))
			return true;
		
		return false;
	}
	
	public Boolean isHouseObject()
	{
		//False -> müssen vor true geprüft werden (floors zb toiletroom)
		
		//		if(editoraction.contains("solar"))
		//		return false;
		//	if(editoraction.contains("groundwater"))
		//		return false;
		//	if(editoraction.contains("outside"))
		//		return false;
		//	if(editoraction.contains("traffic"))
		//		return false;
		//	if(editoraction.contains("road"))
		//		return false;		
		
		if(!isAddressObject())
			return false;
		
		//if(editoraction.contains("residential_garage"))
		//	return false;	
		
		//if(editoraction.contains("road_road_parkingspace"))
		//	return false;
		
		if(roomtype.contains("outside"))
			return false;

		if(roomtype.contains("outdoor"))
			return false;
		
		//if(editoraction.contains("garbagecontainer"))
		//	return false;
		
		if(isRoomObject)
			return false;
		
		if(isGroundObject)
			return false;

		if(isGroundBaseObject)
			return false;

		
		if(isHuman())
			return false;
		
		if(editoraction.contains("bird"))
			return false;

		//if(editoraction.contains("company"))
		//{
		//	if(editoraction.contains("workingplace")) 
		//		return true;
			
		//	if(editoraction.contains("room")) //test für breakroom, fruit plate 
		//		return true;
			
		//	if(editoraction.contains("inhouse")) 
		//		return true;			
			//if(editoraction.contains("supermarket_shelf"))
			//	return true;

			//		if(editoraction.contains("toiletroom")) 
			//			return true;
			//return false;
		//}
		
		return true;
	}
	
	public Boolean testpoint(float x, float y, IntersectionMode imode)
	{
		Vector2 v2 = new Vector2();
		v2.x=x;
		v2.y=y;
		float[] verts =  getBoundingPolygon(false, imode).getTransformedVertices();//getBoundingPolygon(imode).getTransformedVertices();
		return Intersector.isPointInPolygon(verts, 0, verts.length, x, y);
	}

	public CWorldObject isOnDefense(int mov, int posx, int posy)
	{
		try
		{
			int sx = posx;
			int sy = posy;
			if(sx<0)
				sx=pos_x+width/2;
			if(sy<0)
				sy=pos_y+height/2;
			
			int px = sx;
			int py = sy;
			
			if(mov==-1) //nur mittelpunkt testen
			{
				//mitte
				int index1 = (int)(px/town.defensewallsize + py/town.defensewallsize * town.gameWorld.pathmapsize_defensewall);
				return CWorld.pathmap_defensewall[index1];
			}
			
			int index1 = (int)(px/town.defensewallsize + py/town.defensewallsize * town.gameWorld.pathmapsize_defensewall);
			
			//rechts oben
			int index2 = (int)((px+mov)/town.defensewallsize + (py-mov)/town.defensewallsize * town.gameWorld.pathmapsize_defensewall);
			
			//rechts unten
			int index3 = (int)((px-mov)/town.defensewallsize + (py+mov)/town.defensewallsize * town.gameWorld.pathmapsize_defensewall);
			
			//links unten
			int index4 = (int)((px+mov)/town.defensewallsize + (py+mov)/town.defensewallsize * town.gameWorld.pathmapsize_defensewall);
			
			//links oben
			int index5 = (int)((px-mov)/town.defensewallsize + (py-mov)/town.defensewallsize * town.gameWorld.pathmapsize_defensewall);
			
			CWorldObject onRoad = null;
			CWorldObject onRoad2 = null;
			CWorldObject onRoad3 = null;
			CWorldObject onRoad4 = null;
			CWorldObject onRoad5 = null;
			
			onRoad = CWorld.pathmap_defensewall[index1];
			onRoad2 = CWorld.pathmap_defensewall[index2];
			onRoad3 = CWorld.pathmap_defensewall[index3];
			onRoad4 = CWorld.pathmap_defensewall[index4];
			onRoad5 = CWorld.pathmap_defensewall[index5];
			
			if(onRoad!=null)
				return onRoad;
			if(onRoad2!=null)
				return onRoad2;
			if(onRoad3!=null)
				return onRoad3;
			if(onRoad4!=null)
				return onRoad4;
			if(onRoad5!=null)
				return onRoad5;
			
			return null;
		}
		catch(Exception ex)
		{
			return null;
		}
	}	
	
	public CWorldObject isOnRoad(int mov, int posx, int posy)
	{
		try
		{
			int sx = posx;
			int sy = posy;
			if(sx<0)
				sx=pos_x+width/2;
			if(sy<0)
				sy=pos_y+height/2;
			
			int px = sx;
			int py = sy;
			
			if(mov==-1) //nur mittelpunkt testen
			{
				//mitte
				int index1 = (int)(px/town.roadrastersize + py/town.roadrastersize * town.gameWorld.pathmapsize_road);
				return CWorld.pathmap_road[index1];
			}
			
			int index1 = (int)(px/town.roadrastersize + py/town.roadrastersize * town.gameWorld.pathmapsize_road);
			
			//rechts oben
			int index2 = (int)((px+mov)/town.roadrastersize + (py-mov)/town.roadrastersize * town.gameWorld.pathmapsize_road);
			
			//rechts unten
			int index3 = (int)((px-mov)/town.roadrastersize + (py+mov)/town.roadrastersize * town.gameWorld.pathmapsize_road);
			
			//links unten
			int index4 = (int)((px+mov)/town.roadrastersize + (py+mov)/town.roadrastersize * town.gameWorld.pathmapsize_road);
			
			//links oben
			int index5 = (int)((px-mov)/town.roadrastersize + (py-mov)/town.roadrastersize * town.gameWorld.pathmapsize_road);
			
			CWorldObject onRoad = null;
			CWorldObject onRoad2 = null;
			CWorldObject onRoad3 = null;
			CWorldObject onRoad4 = null;
			CWorldObject onRoad5 = null;
			
			onRoad = CWorld.pathmap_road[index1];
			onRoad2 = CWorld.pathmap_road[index2];
			onRoad3 = CWorld.pathmap_road[index3];
			onRoad4 = CWorld.pathmap_road[index4];
			onRoad5 = CWorld.pathmap_road[index5];
			
			if(onRoad!=null)
				return onRoad;
			if(onRoad2!=null)
				return onRoad2;
			if(onRoad3!=null)
				return onRoad3;
			if(onRoad4!=null)
				return onRoad4;
			if(onRoad5!=null)
				return onRoad5;
			
			return null;
		}
		catch(Exception ex)
		{
			return null;
		}
	}

	public CWorldObject isOnFootpath(int mov, int posx, int posy, Boolean bDebug)
	{
		try
		{
			int sx = posx;
			int sy = posy;
			if(sx<0)
				sx=pos_x+width/2;
			if(sy<0)
				sy=pos_y+height/2;
			
			int px = sx;
			int py = sy;
			
			if(mov==-1) //nur mittelpunkt testen
			{
				int index1 = (int)(px/town.footpathsize+py/town.footpathsize*town.gameWorld.pathmapsize_footpath);
				return CWorld.pathmap_footpath[index1];
			}
			
			//mitte
			int index1 = (int)(px/town.footpathsize+py/town.footpathsize*town.gameWorld.pathmapsize_footpath);
			if(bDebug)
			{
				//Gdx.app.debug("index1: ", "("+px+")/"+town.footpathsize+"+(" + py + ")/" + town.footpathsize + "*" + town.gameWorld.pathmapsize_footpath + "=" + index1);
				//Gdx.app.debug("", ""+index1 + ", px: " + px + ", py: " + py);
			}
			
			//rechts oben
			//int index2 = Math.round((float)((px+(float)mov)/(float)town.footpathsize) + (float)((py+(float)mov)/(float)town.footpathsize) * (float)town.gameWorld.pathmapsize_footpath);
			int index2 = (int)((px+mov)/town.footpathsize + (py+mov)/town.footpathsize*town.gameWorld.pathmapsize_footpath);
			
			//rechts unten
			//int index3 = Math.round((float)((px+(float)mov)/(float)town.footpathsize) + (float)((py-(float)mov)/(float)town.footpathsize) * (float)town.gameWorld.pathmapsize_footpath);
			int index3 = (int)((px+mov)/town.footpathsize + (py-mov)/town.footpathsize*town.gameWorld.pathmapsize_footpath);
			
			//links unten
			//int index4 = Math.round((float)((px-(float)mov)/(float)town.footpathsize) + (float)((py-(float)mov)/(float)town.footpathsize) * (float)town.gameWorld.pathmapsize_footpath);
			int index4 = (int)((px-mov)/town.footpathsize + (py-mov)/town.footpathsize*town.gameWorld.pathmapsize_footpath);
			
			//links oben
			//int index5 = Math.round((float)((px-(float)mov)/(float)town.footpathsize) + (float)((py+(float)mov)/(float)town.footpathsize) * (float)town.gameWorld.pathmapsize_footpath);
			int index5 = (int)((px-mov)/town.footpathsize + (py+mov)/town.footpathsize*town.gameWorld.pathmapsize_footpath);
			
			CWorldObject onRoad = null;
			CWorldObject onRoad2 = null;
			CWorldObject onRoad3 = null;
			CWorldObject onRoad4 = null;
			CWorldObject onRoad5 = null;
			
			onRoad = CWorld.pathmap_footpath[index1];
			onRoad2 = CWorld.pathmap_footpath[index2];
			onRoad3 = CWorld.pathmap_footpath[index3];
			onRoad4 = CWorld.pathmap_footpath[index4];
			onRoad5 = CWorld.pathmap_footpath[index5];
			
			if(onRoad!=null)
				return onRoad;
			if(onRoad2!=null)
				return onRoad2;
			if(onRoad3!=null)
				return onRoad3;
			if(onRoad4!=null)
				return onRoad4;
			if(onRoad5!=null)
				return onRoad5;
			
			return null;
			//return true;
			//return onRoad||onRoad2||onRoad3||onRoad4||onRoad5;
		}
		catch(Exception ex)
		{
			return null;
		}
	}
		
	public Circle getLightZoneCircle()
	{
		Circle c = new Circle();
		c.radius=getLightPower();
		c.x=pos_x+width/2;
		c.y=pos_y+height/2;
		return c;
	}
		
	public Polygon getRadiatorZonePolygon()
	{
		Vector2 v2 = CHelper.moveVectorByRotationS2D(pos_x, pos_y, width/2, height-getRadiatorHeatingPower()/2, width/2, height/2, rotation);
		Polygon polygon = new Polygon(new float[]{0,0,getRadiatorHeatingPower(),0,getRadiatorHeatingPower(),getRadiatorHeatingPower(),0,getRadiatorHeatingPower()});
		polygon.setOrigin(getRadiatorHeatingPower()/2, getRadiatorHeatingPower()/2);
		polygon.setRotation(rotation);
		polygon.setPosition(v2.x-getRadiatorHeatingPower()/2, v2.y-getRadiatorHeatingPower()/2);
		return polygon;
	}

	public Polygon getWardrobeZonePolygon()
	{
		//int zone=(int) town.getSizeValue(400);
		int zone=(int) town.getSizeValue(200);
		Vector2 v2 = CHelper.moveVectorByRotationS2D(pos_x, pos_y, width/2, height-zone/2, width/2, height/2, rotation);
		Polygon polygon = new Polygon(new float[]{0,0,zone,0,zone,zone,0,zone});
		polygon.setOrigin(zone/2, zone/2);
		polygon.setRotation(rotation);
		polygon.setPosition(v2.x-zone/2, v2.y-zone/2);
		return polygon;
	}
	
	public Polygon getTVZonePolygon()
	{
		int tvzonew=width+(int) town.getSizeValue(400);
		int tvzoneh=(int) town.getSizeValue(500);
		//Vector2 v2 = CHelper.moveVectorByRotationS2D(pos_x, pos_y, width/2, height-tvzoneh/2, width/2, height/2, rotation);
		//Vector2 v2 = CHelper.moveVectorByRotationS2D(pos_x, pos_y, width/2, -tvzoneh/2-80, width/2, height/2, rotation);
		Vector2 v2 = CHelper.moveVectorByRotationS2D(pos_x, pos_y, width/2, -tvzoneh/2-(int)town.getSizeValue(140), width/2, height/2, rotation);
		Polygon polygon = new Polygon(new float[]{0,0,tvzonew,0,tvzonew,tvzoneh,0,tvzoneh});
		polygon.setOrigin(tvzonew/2, tvzoneh/2);
		polygon.setRotation(rotation);
		polygon.setPosition(v2.x-tvzonew/2, v2.y-tvzoneh/2);
		return polygon;
	}

	public Polygon getSchoolDeskZonePolygon()
	{
		//int zonew=width/2;
		//int zoneh=300;
		int zonew=width;
		int zoneh=(int) town.getSizeValue(800);
		
		Vector2 v2 = CHelper.moveVectorByRotationS2D(pos_x, pos_y, width/2, +zoneh/2+(int)town.getSizeValue(350), width/2, height/2, rotation);
		Polygon polygon = new Polygon(new float[]{0,0,zonew,0,zonew,zoneh,0,zoneh});
		polygon.setOrigin(zonew/2, zoneh/2);
		polygon.setRotation(rotation);
		polygon.setPosition(v2.x-zonew/2, v2.y-zoneh/2);
		
		return polygon;
	}

	public Polygon getBathmatZonePolygon()
	{
		//Achtung: für Bathmat kein Polygon anzeigen wenn bathtub oder shower geplaced werden
		
		if(editoraction.contains("bathroom_shower"))
		{
			int zonew=width/2;
			int zoneh=(int) town.getSizeValue(20);
			
			Vector2 v2 = CHelper.moveVectorByRotationS2D(pos_x, pos_y, width/2, height, width/2, height/2, rotation);
			Polygon polygon = new Polygon(new float[]{0,0,zonew,0,zonew,zoneh,0,zoneh});
			polygon.setOrigin(zonew/2, zoneh/2);
			polygon.setRotation(rotation);
			polygon.setPosition(v2.x-zonew/2, v2.y-zoneh/2);
			
			return polygon;
		}
		
		if(editoraction.contains("bathroom_bathtub"))
		{
			int zonew=width+(int) town.getSizeValue(80);
			int zoneh=height+(int) town.getSizeValue(80);
			
			Vector2 v2 = CHelper.moveVectorByRotationS2D(pos_x, pos_y, width/2, height/2, width/2, height/2, rotation);
			Polygon polygon = new Polygon(new float[]{0,0,zonew,0,zonew,zoneh,0,zoneh});
			polygon.setOrigin(zonew/2, zoneh/2);
			polygon.setRotation(rotation);
			polygon.setPosition(v2.x-zonew/2, v2.y-zoneh/2);
			
			return polygon;
		}
		
		return null;
	}	
		
	public Polygon getBedZonePolygon() // distance beds
	{
		if(editoraction.contains("bedroom_bed"))
		{
			int zonew=(int) (width+town.getSizeValue(30));
			int zoneh=(int) (height+town.getSizeValue(30));
			
			Vector2 v2 = CHelper.moveVectorByRotationS2D(pos_x, pos_y, width/2, height/2, width/2, height/2, rotation);
			Polygon polygon = new Polygon(new float[]{0,0,zonew,0,zonew,zoneh,0,zoneh});
			polygon.setOrigin(zonew/2, zoneh/2);
			polygon.setRotation(rotation);
			polygon.setPosition(v2.x-zonew/2, v2.y-zoneh/2);
			
			return polygon;
		}
		
		return null;
	}
	
	public int getBedtoOtherBedsZoneCollisionCount(CAddress adr)
	{
		bBedTooClose=false;
		int icount=0;
		
		if(editoraction.contains("bedroom_bed"))
		{
			if(adr==null || adr.listWorldObjects==null)
				return 0;
			
			for(CWorldObject wobj : adr.listWorldObjects)
			{
				if(wobj.theobject.editoraction.contains("bedroom_bed"))
				{
					if(wobj.pos_x()==pos_x && wobj.pos_y()==pos_y)
						continue; //nicht auf sich selbst testen
					
					Boolean bCol = Intersector.overlapConvexPolygons(wobj.theobject.getBedZonePolygon(), getBedZonePolygon());
					if(bCol)
					{
						bBedTooClose=true;
						icount++;
					}
				}
			}
		}
		
		return icount;
	}
	
	public void setMipMapping()
	{
		if(ATTR_MIPMAPPING>-1)
		{
			if(ATTR_MIPMAPPING==0)
				setMipMapping(TextureFilter.Nearest);
			
			if(ATTR_MIPMAPPING==1)
				setMipMapping(TextureFilter.Linear);
			
			if(ATTR_MIPMAPPING==2)
				setMipMapping(TextureFilter.MipMapNearestNearest);
			
			if(ATTR_MIPMAPPING==3)
				setMipMapping(TextureFilter.MipMapLinearLinear);
			
			if(ATTR_MIPMAPPING==4)
				setMipMapping(TextureFilter.MipMap);
			
			return;
		}
				
		if(editoraction.contains("road_road") || editoraction.contains("footpath"))
		{
			if(town.gameConfigIni.mipmappingRoad==0)
				setMipMapping(TextureFilter.Nearest);
			
			if(town.gameConfigIni.mipmappingRoad==1)
				setMipMapping(TextureFilter.Linear);
			
			if(town.gameConfigIni.mipmappingRoad==2)
				setMipMapping(TextureFilter.MipMapNearestNearest);
			
			if(town.gameConfigIni.mipmappingRoad==3)
				setMipMapping(TextureFilter.MipMapLinearLinear);
			
			if(town.gameConfigIni.mipmappingRoad==4)
				setMipMapping(TextureFilter.MipMap);
		}
		else if(editoraction.contains("outdoor_tree"))
		{
			if(town.gameConfigIni.mipmappingTree==0)
				setMipMapping(TextureFilter.Nearest);

			if(town.gameConfigIni.mipmappingTree==1)
				setMipMapping(TextureFilter.Linear);
			
			if(town.gameConfigIni.mipmappingTree==2)
				setMipMapping(TextureFilter.MipMapNearestNearest);
			
			if(town.gameConfigIni.mipmappingTree==3)
				setMipMapping(TextureFilter.MipMapLinearLinear);

			if(town.gameConfigIni.mipmappingTree==4)
				setMipMapping(TextureFilter.MipMap);
		}
		else if(editoraction.contains("_floor"))
		{
			if(town.gameConfigIni.mipmappingFloor==0)
				setMipMapping(TextureFilter.Nearest);
			
			if(town.gameConfigIni.mipmappingFloor==1)
				setMipMapping(TextureFilter.Linear);
			
			if(town.gameConfigIni.mipmappingFloor==2)
				setMipMapping(TextureFilter.MipMapNearestNearest);
			
			if(town.gameConfigIni.mipmappingFloor==3)
				setMipMapping(TextureFilter.MipMapLinearLinear);
			
			if(town.gameConfigIni.mipmappingFloor==4)
				setMipMapping(TextureFilter.MipMap);
		}
		else if(editoraction.contains("_ground"))
		{
			if(town.gameConfigIni.mipmappingGround==0)
				setMipMapping(TextureFilter.Nearest);

			if(town.gameConfigIni.mipmappingGround==1)
				setMipMapping(TextureFilter.Linear);
			
			if(town.gameConfigIni.mipmappingGround==2)
				setMipMapping(TextureFilter.MipMapNearestNearest);
			
			if(town.gameConfigIni.mipmappingGround==3)
				setMipMapping(TextureFilter.MipMapLinearLinear);

			if(town.gameConfigIni.mipmappingGround==4)
				setMipMapping(TextureFilter.MipMap);
		}
		else
		{
			if(town.gameConfigIni.mipmappingOther==0)
				setMipMapping(TextureFilter.Nearest);

			if(town.gameConfigIni.mipmappingOther==1)
				setMipMapping(TextureFilter.Linear);
			
			if(town.gameConfigIni.mipmappingOther==2)
				setMipMapping(TextureFilter.MipMapNearestNearest);
			
			if(town.gameConfigIni.mipmappingOther==3)
				setMipMapping(TextureFilter.MipMapLinearLinear);

			if(town.gameConfigIni.mipmappingOther==4)
				setMipMapping(TextureFilter.MipMap);
		}
	}
	
	public void setMipMapping(TextureFilter filter)
	{
//		TextureFilter filter1 = TextureFilter.MipMapLinearNearest;
//		TextureFilter filter2 = TextureFilter.Nearest;
//		
//		textureImage.setFilter(filter1,filter2);
//		if(textureImage2!=null)
//			textureImage2.setFilter(filter2,filter2);
//		if(textureImage3!=null)
//			textureImage3.setFilter(filter2,filter2);
//		if(textureIcon!=null)
//			textureIcon.setFilter(filter2,filter2);
		
		textureImage.setFilter(filter,filter);
		if(textureImage2!=null)
			textureImage2.setFilter(filter,filter);
		if(textureImage3!=null)
			textureImage3.setFilter(filter,filter);
		if(textureIcon!=null)
			textureIcon.setFilter(filter,filter);
	}
	
	public Polygon getZonePolygonForNighttablePlacement(int direction)
	{
		if(editoraction.contains("bedroom_bed_double") && direction < 2)
		{
			int tvzonew=(width+70)/2;
			int tvzoneh=20;
			
			Vector2 v2=null; 
			
			if(direction==0) //left
			{
				v2 = CHelper.moveVectorByRotationS2D(pos_x, pos_y, 40, height-30, width/2, height/2, rotation);
			}
			else if(direction==1) //right
				v2 = CHelper.moveVectorByRotationS2D(pos_x, pos_y, width-40, height-30, width/2, height/2, rotation);
			
			Polygon polygon = new Polygon(new float[]{0,0,tvzonew,0,tvzonew,tvzoneh,0,tvzoneh});
			polygon.setOrigin(tvzonew/2, tvzoneh/2);
			polygon.setRotation(rotation);
			polygon.setPosition(v2.x-tvzonew/2, v2.y-tvzoneh/2);
			return polygon;
		}
		else
		{
			int tvzonew=width+70;
			int tvzoneh=20;
		
			Vector2 v2 = CHelper.moveVectorByRotationS2D(pos_x, pos_y, width/2, height-30, width/2, height/2, rotation);
			Polygon polygon = new Polygon(new float[]{0,0,tvzonew,0,tvzonew,tvzoneh,0,tvzoneh});
			polygon.setOrigin(tvzonew/2, tvzoneh/2);
			polygon.setRotation(rotation);
			polygon.setPosition(v2.x-tvzonew/2, v2.y-tvzoneh/2);
			return polygon;
		}
	}
	
	public Polygon getInteriorDecorZonePolygon()
	{
		int zonew=(int) (width+town.getSizeValue(600));
		int zoneh=(int) (height+town.getSizeValue(600));
		
		Vector2 v2 = CHelper.moveVectorByRotationS2D(pos_x, pos_y, width/2, height/2, width/2, height/2, rotation);
		Polygon polygon = new Polygon(new float[]{0,0,zonew,0,zonew,zoneh,0,zoneh});
		polygon.setOrigin(zonew/2, zoneh/2);
		polygon.setRotation(rotation);
		polygon.setPosition(v2.x-zonew/2, v2.y-zoneh/2);
		
		return polygon;
	}	
	
	

	
	public Polygon getBoundingPolygon(Boolean bPlacingCountLevel, IntersectionMode imode)
	{
		//itype:
		//	0: collision
		//  1: klick
		
		
		
		Rectangle bounds = null;
		Polygon polygon = null;
		
		int btol=0;
		int btol2=0;
		if(editoraction.contains("flora")) //flora kleineren bounding radius
			btol=width/4;
		if(editoraction.contains("fence")) //fence: für mouseklick größer
			btol2=7;
		
		if(imode==IntersectionMode.PLACING_ON_FLOOR)
		{
			if(editoraction.contains("buildingwall"))
			{
				btol=0;	
			}
		}

		if(imode==IntersectionMode.COLLISION_PLACEWALL)
			btol=1;

		
		if(imode==IntersectionMode.COLLISION_SMALL)
			btol=20;
		
		if(imode==IntersectionMode.COLLISION_SMALL2)
			btol=50;
		
		if(imode==IntersectionMode.COLLISION)
		{
			//btol=5;
			
			if(editoraction.contains("floor")) //floor kleineren bounding radius
				btol=10;
		}
		
		int boundx=ATTR_BOUNDX;
		int boundy=ATTR_BOUNDY;
		int boundxm=ATTR_BOUNDXMOV;
		int boundym=ATTR_BOUNDYMOV;
		
		bounds = new Rectangle(pos_x+btol-btol2-boundx, pos_y+btol-btol2-boundy, width-btol*2+btol2*2+boundx*2, height-btol*2+btol2*2+boundy*2);
		
		if(bPlacingCountLevel)
		{
			if(placingCountLevel>1)
			{
				bounds.width=width*(placingCountLevel);
				
				if(editoraction.contains("floor")) //floor kleineren bounding radius
					bounds.width-=10;
				
				bounds.height=height*(placingCountLevel);
				bounds.y=pos_y-height*(placingCountLevel-1);
			}
		}
		
		if(imode==IntersectionMode.PLACING_ON_FLOOR)
		{
			polygon = new Polygon(new float[]{0,0,  bounds.width/6,0,    bounds.width/5,0,     bounds.width/4,0,      bounds.width/3,0,     bounds.width/1.5f,0,       bounds.width/1.4f,0,        bounds.width/1.3f,0,        bounds.width/1.2f,0,        bounds.width/1.1f,0,          bounds.width,0,    bounds.width,bounds.height/6,    bounds.width,bounds.height/3,     bounds.width,bounds.height/1.5f,    bounds.width,bounds.height,      bounds.width/1.5f,bounds.height,       bounds.width/3,bounds.height,       bounds.width/6,bounds.height,       0,bounds.height,     0,bounds.height/1.5f,        0,bounds.height/3,         0,bounds.height/6});
		}
		else
			polygon = new Polygon(new float[]{0,0,bounds.width,0,bounds.width,bounds.height,0,bounds.height});
		
		polygon.setOrigin(bounds.width/2, bounds.height/2);
		polygon.setRotation(rotation);
		
		if(placingCountLevel>0 && (isGroundObject || isRoomObject || isGroundBaseObject))
		{
			polygon.setPosition(bounds.x, bounds.y);
		}
		else
		{
			Vector2 v2 = CHelper.moveVectorByRotationS2D((int)pos_x, (int)pos_y, (int)width/2+boundxm, height/2+boundym, (int)(width/2), (int)(height/2), rotation);
			polygon.setPosition(v2.x-bounds.width/2, v2.y-bounds.height/2);
		}
		
		return polygon;
	}
	
	public Boolean drawShadows(SpriteBatch worldSpriteBatch, int itype, int x, int y, int w, int h, float scale1)
	{
		//Draw Shadow: Buy Menu
		float deltaval=1;
		float type2f=0.5f;
		
		if(drawShadow)
		{
			if(editoraction.contains("outdoor_ground"))
			{
				if(1==1)
					return true;
				
				if(editoraction.contains("outdoor_ground_water"))
					return true;
				
				float c1=1f;
				worldSpriteBatch.setColor(c1,c1, c1, 0.85f);
				
				if(itype==2)
					worldSpriteBatch.setColor(c1,c1,c1, type2f+0.1f);
				
				if(itype==1)
					deltaval=8;
				
				int val=Math.round(20/deltaval);
				
				return true;
			}
			
			if(editoraction.contains("road_road"))
			{
				
				//if(editoraction.contains("road_road_road") || editoraction.contains("footpath"))
				//{
				//	return false;
				//}
				
				if(editoraction.contains("parkingspace"))
					return false;
				
				if(textureRegion2!=null && textureRegion2.length>0)
				{
					worldSpriteBatch.setColor(1f, 1f, 1f, 1f);
					
					if(itype==2)
						worldSpriteBatch.setColor(1f, 1f, 1f, type2f);
					
					if(scale1==99)
						worldSpriteBatch.setColor(1,1,1, 0.3f);
						//worldSpriteBatch.setColor(1,1,1, 0.4f);
					
					//if(scale1==100)
					//	return true;
					
					if(itype==1)
						deltaval=17;
					
					if(scale1==99)
						deltaval=0.7f;
					
					int val=Math.round(40/deltaval);
					if(editoraction.contains("footpath") && val>20)
						val=20;
					
					val = (int) town.getSizeValue(val);
					
					worldSpriteBatch.draw(textureRegion2[0], x-val, y-val, w+val*2, h+val*2);
				}
				
				return true;
			}
			
			if(editoraction.contains("floor"))
			{
				if(itype==1)
					deltaval=9;
				
				//int val=Math.round(town.floorrastersize/deltaval);
				int val=(int) town.getSizeValue(Math.round(32/deltaval));
				
				int movx=0;
				int movy=0;

				//itype=2;				
				
				if(itype==0)
				{
					worldSpriteBatch.setColor(0.16f, 0.16f, 0.16f, 0.124f);
					int sz=(int) town.getSizeValue(55);
					worldSpriteBatch.draw(textureImage, x-sz, y-sz, w+sz*2, h+sz*2);
				}
				
				worldSpriteBatch.setColor(0.3f,0.3f,0.3f,1f);
				
				if(itype==2)
					worldSpriteBatch.setColor(0.56f, 0.56f, 0.56f, type2f+0.1f);
				
				if(scale1==99) //placing from menu
					worldSpriteBatch.setColor(0.56f, 0.56f, 0.56f, 0.4f);
				
				if(town.bConstructionMode && baseWorldObject!=null && baseWorldObject.iObjectIsReady<100)
				{
					float status = baseWorldObject.iObjectIsReady/100f;
					worldSpriteBatch.setColor(0.56f, 0.56f, 0.56f, status);
				}
				
				worldSpriteBatch.draw(textureImage, x-val-movx, y-val*0.98f-movy, w+val*2, h+val*2.02f);
				
				worldSpriteBatch.setColor(0.46f,0.46f,0.46f,1f);
				Texture texture = town.gameResourceConfig.textures.get("poly_whiterect3");
				worldSpriteBatch.draw(texture, x-val-movx, y-val*0.98f-movy, w+val*2, h+val*2.02f);
				
				return true;
			}
			
			if(editoraction.contains("residential_garage"))
			{
				Texture texture = town.gameResourceConfig.textures.get("road_shadow1");
				worldSpriteBatch.setColor(0.26f, 0.26f, 0.26f, 0.94f);
				
				if(itype==2)
					worldSpriteBatch.setColor(0.26f, 0.26f, 0.26f, type2f+0.1f);
				
				if(scale1==99) //placing from menu
				{
					//worldSpriteBatch.setColor(0.26f, 0.26f, 0.26f, 0.8f);
					//deltaval=0.6f;
				}
				
				if(itype==1)
					deltaval=6;
				
				int val=(int) town.getSizeValue(Math.round(26f/deltaval));
				
				//worldSpriteBatch.draw(texture, x-val, y-val*1.2f, (w+val*2)/2, (h+val*2.4f)/2, w+val*2, h+val*2.4f, 1f, 1f, rotation, 0, 0, texture.getWidth(), texture.getHeight(), false, false);
				worldSpriteBatch.draw(texture, x-val*1.2f, y-val*1.2f, (w+val*2.4f)/2, (h+val*2.4f)/2, w+val*2.4f, h+val*2.4f, 1f, 1f, rotation, 0, 0, texture.getWidth(), texture.getHeight(), false, false);
				
				return true;
			}
		}
		
		return false;
	}
	
	public int getSellingPrice()
	{
		return (int)(price/3);
	}
	
	public CObject clone()
	{
		CObject clone = new CObject(town);
		
		//Gfx wird gelinked
        for (java.lang.reflect.Field field : this.getClass().getDeclaredFields()) 
        {
            field.setAccessible(true);
            
            String n1 = field.getName();
            //if(n1.contains("textureImage") || n1.contains("textureRegion") || n1.contains("objectAnimation"))
            //	continue;
            
            try {
            	field.set(clone, field.get(this));
            } catch (Exception e) {
				//e.printStackTrace();
			}
        }
        
        clone.placingCountLevel=1;
                
        /*
        clone.textureImage=textureImage;
        clone.textureImage2=textureImage2;
        clone.textureImage3=textureImage3;
        clone.textureRegion=textureRegion;
        clone.textureRegion2=textureRegion2;
        clone.textureRegion3=textureRegion3;
        clone.objectAnimation=objectAnimation;
        clone.objectAnimation2=objectAnimation2;
        clone.objectAnimation3=objectAnimation3;
        */
                
        return clone;
		
//      CObject cobj = new CObject();
//		cobj.objectId=objectId;
//		cobj.objectTypeId=objectTypeId;
//		cobj.objectName=objectName;
//		cobj.price=price;
//		cobj.pos_x=pos_x;
//		cobj.pos_y=pos_y;
//		cobj.width=width;
//		cobj.height=height;
//		cobj.icon_pos_x=icon_pos_x;
//		cobj.icon_pos_y=icon_pos_y;
//		cobj.icon_width=icon_width;
//		cobj.icon_height=icon_height;	
//		cobj.ObjectAction_Rotation=ObjectAction_Rotation;
//		cobj.ObjectAction_Move_Pixels_X=ObjectAction_Move_Pixels_X;
//		cobj.ObjectAction_Move_Pixels_Y=ObjectAction_Move_Pixels_Y;
//		cobj.rotation=rotation;
//		cobj.textureIcon=textureIcon;
//		cobj.textureImage=textureImage;
//		cobj.textureRegion=textureRegion;
//		cobj.textureFilename=textureFilename;
//		cobj.iconFilename=iconFilename;
//		cobj.objectAnimation=objectAnimation;
//		cobj.frameCols=frameCols;
//		cobj.frameRows=frameRows;
//		cobj.move_startFrame=move_startFrame;
//		cobj.move_endFrame=move_endFrame;
//		cobj.action1_startFrame=action1_startFrame;
//		cobj.action1_endFrame=action1_endFrame;
//		cobj.idleFrame=idleFrame;
//		cobj.zorder=zorder;
//		cobj.placingCountLevel=placingCountLevel; //placing floors
//		cobj.mipmap=mipmap;
//		cobj.editoraction=editoraction; //zB Raum erstellen
//		cobj.coltype=coltype; //Kollisionstyp: 0=no collision, 1=static, 2=dynamic_human, 3=dynamic_car?
//		cobj.doRasterPlacement=doRasterPlacement;
//		cobj.drawShadow=drawShadow;
//		cobj.rotationValue=rotationValue;
//		cobj.isRoomObject=isRoomObject;
//		cobj.isWallObject=isWallObject;
//		cobj.isFloorObject=isFloorObject;
//		
//		return cobj;
	}
		
	public float[] getDimensionsForBuyMenu()
	{
		//Buy Menu / Research List
		
    	float imagew = width/1.5f;
    	float imageh = height/1.5f;
    	
    	if(width<400 && height<400)
    	{
        	imagew=width/1.4f;
        	imageh=height/1.4f;
    	}
    	if(width<300 && height<300)
    	{
        	imagew=width/1.3f;
        	imageh=height/1.3f;
    	}
    	if(width<200 && height<200)
    	{
        	imagew=width/1.3f;
        	imageh=height/1.3f;
    	}
    	if(width<130 && height<130)
    	{
        	imagew=width/1.2f;
        	imageh=height/1.2f;
    	}	        	
    	if(width<100 && height<100)
    	{
        	imagew=width;
        	imageh=height;
    	}
    	if(width>500 || height>500)
    	{
        	imagew=width/2.5f;
        	imageh=height/2.5f;
    	}
    	if((width+height)>750)
    	{
        	imagew=width/2f;
        	imageh=height/2f;
    	}
    	if((width+height)>850)
    	{
        	imagew=width/2.4f;
        	imageh=height/2.4f;
    	}    	
    	if(width>120 && height>120)
    	{
    		imagew/=1.1f;
    		imageh/=1.1f;
    	}
    	if(isRoomObject)
    	{
        	imagew=town.floorSize;
        	imageh=town.floorSize;
    	}
    	
    	return new float[]{imagew, imageh};
	}

	public float[] getDimensionsByBase(float posx, float posy, float baseWidth, float baseHeight)
	{
    	//Icongröße an Objekt-Größenverhältnis anpassen
		float newiconw = (float)baseWidth;
		float newiconh =  (float)baseHeight;
		float newiconx = posx;
		float newicony = posy;			
		
		if(width>height)
		{
			newiconw=baseWidth;
			newiconh=(float)baseHeight/((float)width/(float)height);
			newicony+=(baseHeight-newiconh)/2;
		}
		else if(height>width)
		{
			newiconw=(float)baseWidth/((float)height/(float)width);
			newiconh=baseHeight;
			newiconx+=(baseWidth-newiconw)/2;
		}
		
		if(editoraction.contains("fridge")) 
		{
			newiconx-=6;
			newicony-=10;
			newiconw+=15;
			newiconh+=15;
		}
		
		if(editoraction.contains("washingmachine") || 
				editoraction.contains("dryer"))
		{
			newiconx-=2;
			newicony-=13;
			newiconw+=8;
			newiconh+=8;
		}
		
		if(editoraction.contains("cupboard"))
			newicony-=5;
		
		return new float[]{newiconx, newicony, newiconw, newiconh};
	}	
	
	public TextureRegion getBaseTextureRegion()
	{
		TextureRegion texture1=null;
		
		if(objectAnimation!=null && frameCols>1)
		{
			TextureRegion objanim[] = objectAnimation.getKeyFrames();
			if(objanim.length>0)
			{
				texture1 = objanim[0];
			}
		}
		else if(textureRegion!=null)
		{
			if(textureRegion.length>0)
			{
				texture1 = textureRegion[0];
			}
		}
		
		return texture1;
	}
	
	public Boolean checkRoomType(CObject placingobj)
	{
		//*****************************************************
		//Check Roomtype betwenn 1 Floor and one Placing Object
		//obj is not the room
		//*****************************************************
		
		if(placingobj.roomtype.contains("anywhere"))
			return true;
		
		if(!isRoomObject)
			return false;
		
		if(placingobj.isRoomObject || placingobj.isHuman())// || !placingobj.isHouseObject())
			return false;
		
		if(roomtype.contains(placingobj.roomtype))
			return true;
		
		//Company any room
		if(editoraction.contains("company") && placingobj.roomtype.contains("anyroomcompany"))
			return true;
		
		//Company office room
		if(editoraction.contains("office") && placingobj.roomtype.contains("anyofficeroomcompany"))
			return true;
		
		//Residential any room
		if(!editoraction.contains("company") && placingobj.roomtype.contains("anyroomresidential"))
			return true;
		
		//Any Room
		if(placingobj.roomtype.contains("anyroom"))
			return true;

		
		return false;
	}
	
	public Vector2 getRaster()
	{
		Vector2 v2 = new Vector2();
		v2.x=pos_x;
		v2.y=pos_y;
		
		v2.x = (int)(v2.x / width);
		v2.x = (int)(v2.x * width);
		v2.y = (int)(v2.y / height);
		v2.y = (int)(v2.y * height);			
		
		return v2;
	}
	
	public Boolean collides(int x, int y, int libgdxy)
	{

//		float bodyx = body.getPosition().x-walkAnimation.getKeyFrame(stateTime, true).getRegionWidth()/2;
//		float bodyy = body.getPosition().y-walkAnimation.getKeyFrame(stateTime, true).getRegionHeight()/2;

//  	px = (int)body.getPosition().x-walkAnimation.getKeyFrame(stateTime, true).getRegionWidth();
//	    py = (int)body.getPosition().y-walkAnimation.getKeyFrame(stateTime, true).getRegionHeight();
//
//    	rotation = (float) Math.atan2((float)ziel_y - (float)bodyy, (float)(ziel_x - (bodyx)));
//    	rotation = MathUtils.radiansToDegrees*rotation;
//    	rotation=rotation+90f;
//
//      Vector3 c0 = new Vector3(screenX,screenY,0);
//    	Vector3 c1 = gameCam.unproject(c0);
//
//      dummy.rotation = (float) Math.atan2((float)dummy.ziel_y - (float)dummy.py, (float)(dummy.ziel_x - (float)dummy.px));
//    	dummy.rotation = MathUtils.radiansToDegrees*dummy.rotation;
//    
//    	dummy.rotation=dummy.rotation+90f;
		
		//dummy.rotation = (float) Math.atan2((float)dummy.ziel_y - (float)dummy.py, (float)(dummy.ziel_x - (float)dummy.px));
	    //dummy.rotation = MathUtils.radiansToDegrees*dummy.rotation;
		
//		int x1=pos_x;
//		int y1=pos_y;

//		int x2=x1;
//		int y2=y1;
		
//		Vector3 c0 = new Vector3(x,libgdxy,0);
//    	Vector3 c1 = cam.unproject(c0);		
//		x = (int) c1.x;
//		y = (int) c1.y;
    	
		int x1= pos_x;
		int y1= pos_y;
		int x2= pos_x + width; //(int) (x1 + Math.cos(rotation) * width);
		int y2= pos_y + height; //(int) (y1 + Math.sin(rotation) * height);		
		
		if(x>=x1 && x<=x2 && y>=y1 && y <= y2)
		//if(x>=pos_x && x<=pos_x+width && y>=pos_y && y <= pos_y+height)
		{
			//Gdx.app.debug("collide", "1");
			return true;
		}
		
		return false;
	}	
		
	public Boolean collidesIcon(int x, int y)
	{
		if(x>=icon_pos_x && x<=icon_pos_x+icon_width && y>=icon_pos_y && y <= icon_pos_y+icon_height)
		{
			return true;
		}
		
		return false;
	}

}




