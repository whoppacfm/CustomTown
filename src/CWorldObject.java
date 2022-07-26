package com.mygdx.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Random;
import java.util.Set;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import sun.misc.GC;
import box2dLight.DirectionalLight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.badlogic.gdx.utils.async.AsyncResult;
import com.badlogic.gdx.utils.async.AsyncTask;
import com.mygdx.game.CAction.ActionMode;
import com.mygdx.game.CAction.ValueType;
import com.mygdx.game.CAnimationTextEvent.AnimationEventType;
import com.mygdx.game.CCompany.CompanyType;
import com.mygdx.game.CCompany.WorkoutputType;
import com.mygdx.game.CGuiDialog_YesNo.YesNoDlgType;
import com.mygdx.game.CHelper.IntersectionMode;
import com.mygdx.game.CSpriteMoveEvent.SpriteMoveEventType;
import com.mygdx.game.CStatistics.CStatisticsData_Other;

public class CWorldObject implements AsyncTask<Void>
{
	public static enum OverlapType {
			DECOR
		}
	public static class JobCountComparator implements Comparator<CWorldObject> 
	{
	  public int compare(CWorldObject s1, CWorldObject s2)
	  {
		  float taskcount=0;
		  for(CWorldObject wobj : s1.thehuman.taskobjects.values())
		  {
			  if(wobj.isCompanyObject())
				  taskcount++;
			  else 
				  taskcount+=0.5f;
		  }
		  
		  float taskcount2=0;
		  for(CWorldObject wobj : s2.thehuman.taskobjects.values())
		  {
			  if(wobj.isCompanyObject())
				  taskcount2++;
			  else 
				  taskcount2+=0.5f;
		  }		  
		  
		  if ((s1.thehuman.workplaces.size()+taskcount) == (s2.thehuman.workplaces.size()+taskcount2)) 
		  {
			  int wo1=0;
			  int wo2=0;
			  if(s1.town.gameWorld.markerObject!=null)
			  {
				  wo1=s1.thehuman.getWorkOutputPerHour(true, s1.town.gameWorld.markerObject, null, true);
				  wo2=s2.thehuman.getWorkOutputPerHour(true, s2.town.gameWorld.markerObject, null, true);
			  }
			  
			  if(wo1>wo2)
				  return -1;
			  if(wo2>wo1)
				  return 1;
	    	
			  return 0;
		  } 
		  else 
		  {
			  //return (((s1.thehuman.workplaces.size()+taskcount) < (s2.thehuman.workplaces.size()+taskcount2)) ? -1 : 1);
			  return (((s1.thehuman.workplaces.size()+taskcount) < (s2.thehuman.workplaces.size()+taskcount2)) ? -1 : 1);
		  }
	  }
	}
	public static class TempCountComparator implements Comparator<CWorldObject> 
	{
	  public int compare(CWorldObject s1, CWorldObject s2)
	  {
	    if (s1.tempcount == s2.tempcount) 
	    {
	      return 0;
	    } 
	    else 
	    {
	      return s1.tempcount < s2.tempcount ? -1 : 1;
	    }
	  }
	}
	
	//public enum EWorldObjectType { DEFAULT, COMPANY  }
	public CObject theobject; //Objektinformationen die sich im Spielverlauf nicht verändern können
	//public CObjectInfo objectinfo; //Objektinformationen die sich im Spielverlauf verändern können
	//public CObject headObject;
	public CHuman thehuman;
	public CAddress theaddress;
	public CAddress theaddress2;
	public int theaddressid;
	public int theaddressid2;
	CGuiTooltip tooltip;
	
	
	
	
	//public Boolean bMoveHouse;
	public CWorldObject owner; //owner of object(bed, dinner table seat...)
	public int ownerid;
	public CWorldObject owner2;
	public int owner2id;
	public CWorldObject owner3;
	public int owner3id;
	public CWorldObject owner4;
	public int owner4id;
	public CWorldObject owner5;
	public int owner5id;
	public CWorldObject owner6;
	public int owner6id;
	public CWorldObject owner7;
	public int owner7id;
	public CWorldObject owner8;
	public int owner8id;
	
	public int iLaunchstatus;
	
	public float timeInTown;
	Boolean bObjectIsReady;
	float iObjectIsReady;

	TextureRegion currentFrame;
	
	Boolean bCarOnFootpath;
	
	public float defenseWarningTimer;
	
	public Boolean bDrawObject; //For Actions
	public Boolean bDrawObjectZoom; //for Zoom
	public Boolean bDrawObjectByCamera;
	ArrayList<CSpriteMoveEvent> spriteMoveEvents;
	
	private Boolean bHandleActionsNow;
	
	Boolean bSwimming;
	
	float defenseDestroyTimer;
	
	float iZombie;
	float zombieRun;
	int zombieDeadFrame=0;
	public float zombieEscapeTimer=0;
	public int zombieShowDeadTimer=60*60*3; 
	
	public Boolean bDefenseAlarm;
	
	public float fBremse;
	
	//public Boolean debugPathfinding;
	//	public CWorldObject noAutoClear_object1; //todo: save/load, vars derzeit nicht in benutzung
	//	public int noAutoClear_object1id;
	//	public CWorldObject noAutoClear_object2;
	//	public int noAutoClear_object2id;
	
	public CWorldObject theroom;
	public int theroomid;
	
	//Research Lab
	public CObject researchObject; //Was wird gerade geresearched?
	public String researchObjectId;
	
	//Company Object
	public CCompany belongsToCompany;
	int belongsToCompanyId; 
	Boolean bObjMoving;
	public Boolean bJumpOut; //jump out wenn eingesperrt
	private float fRenderSpeakTime;
	
	//Company Workplace
	CWorldObject worker;
	int workerId;
	CWorldObject worker2;
	int worker2Id;
	
	int workTime1_From;
	int workTime1_To;
	//int workTime_Hours;
	//int workTime1_mode;
	int workTime1_From_temp;
	int workTime1_To_temp;
		
	int workTime2_From;
	int workTime2_To;
	//int workTime2_Hours;
	//int workTime2_mode;
	int workTime2_From_temp;
	int workTime2_To_temp;
	
	//public CWorldObject cook;
	//public int cookid;
	public int actiontime1;	//zB dinnerTime für dinnertable
	public int actiontime2;
	public int actiontime3;
	public Boolean actiontime1check;
	public Boolean actiontime2check;
	public Boolean actiontime3check;
	
	public int actiontimenr;
	
	public float fuelValue;
	public float fuelValueMax;
	
	float doorOpenTimer;
	
	int lastActionListSortDay;
	
	int movementX; //for animations from CAction
	
	//Water/Energy
	//int energyConsumption;
	//int waterConsumption;
	//int defaultEnergyOutput;
	//int defaultWaterOutput;
	
	int hposition;
	
	//Show Value Animations
	//int showMaintain;
	//int showMoney;
	//int showWorkoutput;
	//float showTimer;
	
	float pathfindingTimer;
	
	//Workoutput/input
	Boolean onlineByWorkInput;
	//int neededWorkInputPerHour;
	int objectCondition; //zustand des objekts
	int defaultObjectCondition;
	int workHour; //by Worker
	float renderHour; //resets after 1h
	float render4Hour; //resets after 2h
	float render2Hour;
	float renderMinute;
	float renderAnim;
	float renderAnimTarget=40;
	float birdFlyToAddressTime;
	
	//Fridge, ...
	int objectFilling; //Befüllungsstand für unter anderem Food für Kühlschrank, küchenschrank, ..., abfall für müllcontainer
	int objectFilling2;
	Hashtable<Integer, Integer> objectFillingMulti; //washing machine, dryer, laundry basket
													//achtung: save/load bisher für: Taskobject, Worldobject, Companyobject
	//public WorldObjectType worldObjectType;
	
	public Color color1;
	public Color actionColor1; //für actions
	
	public int shadowDistance;
	public int movementSpeed;
	
	public CTown town;
	//public CWorld gameWorld;
	
	//Informationen die den räumlichen betreffen
	private int x;
	private int y;
	private float frotation;
	public int width;
	public int height;
	public int dynamicwidth; //Objekt hat andere size als original size
	public int dynamicheight;
	public int tempwidth; //Move Object, size zwischenspeichern
	public int tempheight;
	
	public int scrollwidth;
	public int scrollheight;
	
	public int dynamicprice;
	
	public Boolean showMarker;
	public Body box2dBody; //wird derzeit nicht verwendet
	public Body box2dBody_light; //wird derzeit nicht verwendet
	
	public CWorldObject teachersDesk;
	public Boolean bStudentDeskHasTeachersDesk;
	
	//Actions
	CAction action_Fridge; 
	CAction action_Bed;
	CAction action_idle;
	CAction action_church;
	CAction action_Shower;
	CAction action_Toilet;
	CAction action_ToiletFloor;
	CAction action_Workplace;
	CAction action_SupermarketBuyin;
	CAction action_SupermarketRefillShelf;
	CAction action_cookdinner;
	CAction action_eatdinner;
	CAction action_washdishes;
	CAction action_takeoutgarbage;
	CAction action_pubaction;
	CAction action_fitnessstudio;
	CAction action_readbook;
	CAction action_watchtv;
	CAction action_playground;
	CAction action_vehiclerefuel;
	CAction action_funeralattend;
	CAction action_gotodoctor;
	CAction action_laundry;
	CAction action_changeclothes;
	CAction action_breakroom;
	CAction action_zombie;
	CAction action_orderpizza;
	
	public Boolean doObjectAction;
	
	public int x_temp; //Position zwischenspeichern CAction
	public int y_temp; 
	public float rotation_temp;
	
	float actionvar_fs1; //Action var multivalue fitness studio
	ActionMode lastActionMode; // vorherige action tracken
	String lastTargetActionObjectType; // object-typ des letzten action objects
	
	//Variablen die nicht automatisch zurückgesetzt werden
	//public float noAutoClear_var1;
	//public float noAutoClear_var2;
	
	//Actionübergreifende Actionvariablen, werden nur in cancelaction zurückgesetzt
	public float tempcount=0;
	public float actionvar1=0;
	public float actionvar2=0;
	public float actionvar3=0;
	public float actionvar4=0;
	public float actionvar5=0;
	public float actionvar6=0;
	public float actionvar7=0;
	public float actionvar8=0;
	public float actionvar9=0;
	
	public String actionstring1="-";
	public String actionstring2="-";
	public String actionstring3="-";
	
	public Boolean bInitAction_NoResetTargetObjects=false;
	
	//Variablen die automatisch zurückgesetzt werden, nicht action-übergreifend einsetzbar
	public float actionanim1=0;
	public float actionanim2=0;
	public float actionfield1=0;
	public float actionfield2=0;
	public float actionfield3=0;
	public float objectAnimSpeedModifier=0;
		
	//Pathfinding
    int ziel_x;
    int ziel_y;
    int ziel_x_changed;
    int ziel_y_changed;
    int temp_ziel_x;
    int temp_ziel_y;
    
    int temp_ziel_rot_x;
    int temp_ziel_rot_y;
    
    Boolean bonlyEmptyTargetNode;
    public IntArray path;
    //ShapeRenderer shapeRenderer1;
    Boolean bReachable;	//Pathfinding
    CWorldObject targetPathObject;
    
    Texture head;
    Animation headAnimation;
    TextureRegion[] headFrames;
    TextureRegion currentHeadFrame;    
    float rendertime;
    float renderTime2;
    float rotationTime;
    float animationtimer;
    float recyclingmachine3Timer;
    float recyclingmachine3Timer_Trigger;
    float rotmod;
    int uniqueId;
    Random rand;
    Boolean bDeleted; //Objekt wurde gelöscht
    
    int iOccupied1_Arrived;
    int iOccupiedExtern_Arrived;
    
    //Boolean bIsOccupied; //Objekt in Benutzung
    public int test1;
    int test2;
    CWorldObject isOccupiedBy;
    public int isOccupiedById;
    
    //Boolean bIsOccupied2; //Objekt in Benutzung
    CWorldObject isOccupiedBy2;
    public int isOccupiedBy2Id;
    
    //Boolean bIsOccupied3; //Objekt in Benutzung
    CWorldObject isOccupiedBy3;
    public int isOccupiedBy3Id;
    
    CWorldObject isOccupiedBy4;
    public int isOccupiedBy4Id;
    CWorldObject isOccupiedBy5;
    public int isOccupiedBy5Id;
    CWorldObject isOccupiedBy6;
    public int isOccupiedBy6Id;
    CWorldObject isOccupiedBy7;
    public int isOccupiedBy7Id;
    CWorldObject isOccupiedBy8;
    public int isOccupiedBy8Id;
    CWorldObject isOccupiedBy9;
    public int isOccupiedBy9Id;
    
    //Boolean bIsOccupiedByExtern; //Wenn Action Zielobjekt ein Arbeitsplatz ist (zb Checkout) dann ist occupied immer true, daher zusätzlich: Kasse besetzt von Kunde
    CWorldObject isOccupiedByExtern;
    public int isOccupiedByExternId;
        
    //ConeLight light;
    PointLight plight;
    ConeLight clight;
    ConeLight clight2;
    DirectionalLight dlight;
    
    //Go By Car
    CWorldObject goByCar_TargetParkingplace;
    int goByCar_TargetParkingplaceId;
    CWorldObject goByCar_TargetObject;
    int goByCar_TargetObjectId;
    int goByCar_ActionState;
    int goByCar_X;
    int goByCar_Y;
	
    
    //Humans
    public Boolean bIsDead;
    
    //Actions
   	CAction.ActionMode activeActionMode;
   	CAction.ActionMode activeActionModeTemp; //aktuelle Action zwischenspeichern und später fortführen
   	CAction activeAction;
   	CAction activeActionTemp;
   	
   	List<CAction> actionList_Freetime;
   	List<CAction> actionList_Default;
   	List<CAction> actionList_Default_Priority;
   	List<CAction> actionList_Work;
   	
	float deltaSecond;
	float deltaRotation;
	
	//AsyncExecutor asyncExecutor;
	AsyncResult<Void> asyncResult;
	//AsyncTask<void> aaa;
	
	public Boolean isZombie()
	{
		if(iZombie<1){
			return false;
		}
		
		return true;
	}
	
	public CWorldObject(CObject obj, CTown t, Boolean createBody){
		
		tooltip = new CGuiTooltip(t);
		//asyncExecutor = new AsyncExecutor(1);
		bObjectIsReady=true;
		iObjectIsReady=100;
		
		iLaunchstatus=0;
		/*
		if(t!=null && t.bConstructionMode && obj!=null && 
				(obj.isRoomObject || 
				obj.editoraction.contains("road_road_road") || 
				obj.editoraction.contains("footpath")
				//obj.editoraction.contains("parkingspace") ||
				//obj.editoraction.contains("garage") ||
				//obj.editoraction.contains("outdoor_ground")
				))
		{
			bObjectIsReady=false;
			iObjectIsReady=0;
		}
		*/

		if(t!=null && t.bConstructionMode && obj!=null && !obj.isHuman() && !obj.editoraction.contains("bird") && !obj.editoraction.contains("construction"))
		{
			bObjectIsReady=false;
			iObjectIsReady=0;
			
			if(obj.editoraction.contains("supermarket_buyin") ||
					obj.editoraction.contains("laundryobject") ||
					obj.editoraction.contains("recyclingcenter_garbagebag") ||
					obj.editoraction.contains("coffeepot1") ||
					obj.editoraction.contains("coffeebeans1") ||
					obj.editoraction.contains("zombie_entrance")
					)
			{
				bObjectIsReady=true;
				iObjectIsReady=100;
			}
			
		}
		
		//obj.baseWorldObject=this;
		
		test1=0;
		test2=0;
		
		timeInTown=0;
		fBremse=1;
		bCarOnFootpath=false;
		defenseWarningTimer=0;
		
		iZombie=0;
		bDefenseAlarm=false;
		
		bSwimming=false;
				
		animationtimer=0;
		scrollwidth=0;
		scrollheight=0;
		
		bHandleActionsNow=true;
		defenseDestroyTimer=0;
		
		dynamicprice=obj.price;
		
		recyclingmachine3Timer=0;
		recyclingmachine3Timer_Trigger=0;
		
		doorOpenTimer=0;
		hposition=0;
		fRenderSpeakTime=0;
		
		if(obj==null || t==null || createBody==null)
		{
			Gdx.app.debug("Error Creating World Object", "obj: " + obj + ", town: " + t + ", createBody: " + createBody);
			return;
		}
		
		spriteMoveEvents=new ArrayList<CSpriteMoveEvent>();
		
		color1=new Color(1,1,1,1);
		actionColor1=null;
		
		shadowDistance=0;
		movementSpeed=0;
				
		theroom = obj.tempRoom;
		
		//Init Variables
		town=t;
		theobject=obj.clone();
		theobject.baseWorldObject=this;
		
		//theobject=obj;
		
		
		//		if(theobject.editoraction.contains("company_waterworks_groundwaterextractionsystem"))
		//		{
		//			Gdx.app.debug("", ""+width + ", " + dynamicwidth + ", " + theobject.width + ", " + theobject.original_width);
		//			//setDynamicSize(1);
		//			//setDynamicSize(0);
		//		}
		//		theobject.setDynamicAttributes();
				
		//shapeRenderer1 = new ShapeRenderer();
		//shapeRenderer1.setProjectionMatrix(town.gameCam.combined);
		
		goByCar_ActionState=0;
		goByCar_X=-1;
		goByCar_Y=-1;
		goByCar_TargetObject=null;
		goByCar_TargetObjectId=-1;
		goByCar_TargetParkingplace=null;
		goByCar_TargetParkingplaceId=-1;
		
		fuelValueMax=obj.getFuelValueMax();
		fuelValue=fuelValueMax;
				
		bDrawObject=true;
		bDrawObjectZoom=true;
		bDrawObjectByCamera=true;
		
		
	    isOccupiedById=0;
	    isOccupiedBy2Id=0;
	    isOccupiedBy3Id=0;
	    isOccupiedBy4Id=0;
	    isOccupiedBy5Id=0;
	    isOccupiedBy6Id=0;
	    isOccupiedBy7Id=0;
	    isOccupiedBy8Id=0;
	    isOccupiedBy9Id=0;
	    isOccupiedByExternId=0;
	    
	    iOccupied1_Arrived=0;
	    iOccupiedExtern_Arrived=0;
	    
	    isOccupiedBy=null;
	    isOccupiedBy2=null;
	    isOccupiedBy3=null;
	    isOccupiedBy4=null;
	    isOccupiedBy5=null;
	    isOccupiedBy6=null;
	    isOccupiedBy7=null;
	    isOccupiedBy8=null;
	    isOccupiedBy9=null;
	    isOccupiedByExtern=null;
	        
		//noAutoClear_object1=null;
		//noAutoClear_object2=null;
		//noAutoClear_object1id=0;
		//noAutoClear_object2id=0;
	    
	    //bIsOccupied=false;
	    //bIsOccupied2=false;
	    //bIsOccupied3=false;
	    //bIsOccupiedByExtern=false;
	    
		//debugPathfinding=false;
	    temp_ziel_rot_x=0;
	    temp_ziel_rot_y=0;
	    
	    pathfindingTimer=0;
	    
	    theaddressid=0;
		theaddress=null;
		
	    theaddressid2=0;
		theaddress2=null;
		
		ownerid=0;
		owner2id=0;
		owner3id=0;
		owner4id=0;
		owner5id=0;
		owner6id=0;
		owner7id=0;
		owner8id=0;
		
		theroom=null;
		theroomid=0;
		
		actiontime1=-1;
		actiontime2=-1;
		actiontime3=-1;
		actiontime1check=false;
		actiontime2check=false;
		actiontime3check=false;
		actiontimenr=-1;
		
		lastActionListSortDay=0;
		
		teachersDesk=null;
		bStudentDeskHasTeachersDesk=false;
		//bMoveHouse=false;
		deltaSecond=0;
		deltaRotation=0;
		//neededWorkInputPerHour=0;
		birdFlyToAddressTime=0;
		renderHour=0;
		render4Hour=0;
		render2Hour=0;
		renderMinute=0;
		renderAnim=0;
		rotationTime=0;
		bIsDead=false;
		activeAction=null;
		activeActionTemp=null;
		rotmod=0;
		rendertime=0;
		plight=null;
		bReachable=true;
		doObjectAction=false;
		targetPathObject=null;
		x=obj.pos_x;
		y=obj.pos_y;
		frotation = obj.rotation;
		width = obj.width;
		height = obj.height;
		showMarker=false;
		bDeleted=false;
		ziel_x=-1;
		ziel_y=-1;
		movementX=0;
		bObjMoving=false;
		
		//showMaintain=0;
		//showMoney=0;
		//showWorkoutput=0;
		//showTimer=0;
		
		bJumpOut=false;
		
		actionvar_fs1=0; //Action var multivalue fitness studio
		lastActionMode=ActionMode.IDLE; // vorherige action tracken
		lastTargetActionObjectType="none"; // object-typ des letzten action objects
				
		rand=new Random();
		uniqueId = getNextUniqueId();
		
		objectFillingMulti = new Hashtable<Integer, Integer>();
		
		objectFilling = 0; //Objekte sind erstmal leer und müssen befüllt werden //getFoodFillingMax();
		objectFilling2 = 0; //Objekte sind erstmal leer und müssen befüllt werden //getFoodFillingMax();
		
		if(theobject.editoraction.contains("supermarket_shelf"))
			objectFilling = town.initial_supermarket_shelf_foodstock;
		if(theobject.editoraction.contains("supermarket_foodpallet"))
			objectFilling = town.initial_supermarket_warehouse_foodstock;
		if(theobject.editoraction.contains("bathroom_towelcabinet"))
			objectFilling = getObjectFillingMax();
		if(theobject.editoraction.contains("bedroom_wardrobe"))
			objectFilling = getObjectFillingMax();
		
		if(theobject.editoraction.contains("_coffeemachine"))
		{
			objectFilling = getObjectFillingMax();
			objectFilling2 = getObjectFillingMax2();
		}
		
		if(theobject.editoraction.contains("fruitplate"))
			objectFilling = getObjectFillingMax();
		
		if(theobject.editoraction.contains("kitchen_fridge"))
			objectFillingMulti.put(0, town.initial_fridge_foodstock);
		
		//Energy/Water Consumption Object
		//energyConsumption=theobject.getEnergyConsumption();
		//waterConsumption=theobject.getWaterConsumption();
		//defaultEnergyOutput=theobject.getEnergyOutput();
		//defaultWaterOutput=theobject.getWaterOutput();
		
		//Workoutput
		//neededWorkInputPerHour=theobject.getNeededWorkinputPerHour();
		onlineByWorkInput=true;
		
		//Condition
		objectCondition=0;
		defaultObjectCondition=0;
		
		if(isMaintenanceObject())
		{
			defaultObjectCondition=100;
			objectCondition=100;
		}
		
		if(theobject.ATTR_OBJCONDITION>0)
		{
			defaultObjectCondition=theobject.ATTR_OBJCONDITION;
			objectCondition=theobject.ATTR_OBJCONDITION;
		}
		
		if(town.bNoRequirements)
		{
			//energyConsumption=0;
			//waterConsumption=0;
			//neededWorkInputPerHour=0; //muss für supermarket activities auskommentiert sein (warehouse)
			
			if(getObjectFillingMax()>0)
				objectFilling=getObjectFillingMax()/2+1;
			
			if(theobject.editoraction.contains("fridge"))
				objectFilling=0;
				//objectFilling=getObjectFillingMax();
		}
		
		//Workplace Object
		worker=null;
		workerId=0;
		
		worker2=null;
		worker2Id=0;
		
		workTime1_From=town.gameConfigIni.default_worktime_from;
		workTime1_To=town.gameConfigIni.default_worktime_to;
		
		workTime2_From=town.gameConfigIni.default_worktime_from2;
		workTime2_To=town.gameConfigIni.default_worktime_to2;
		
		workTime1_From_temp=-1;
		workTime1_To_temp=-1;
		
		if(obj.editoraction.contains("company_pub_workingplace_bar"))
		{
			workTime1_From=17;
			workTime1_To=1;
			
			workTime2_From=-1;
			workTime2_To=-1;
		}
				
		belongsToCompany=null;
		belongsToCompanyId=0;
		workHour=0;
		
		//Human
		thehuman=null;
		actionList_Freetime = new ArrayList<CAction>();
		actionList_Default = new ArrayList<CAction>();
		actionList_Default_Priority = new ArrayList<CAction>();
		actionList_Work = new ArrayList<CAction>();
		
		if(obj.editoraction.contains("human"))
		{
			thehuman = new CHuman(town, this, ' ');
			action_idle = new CAction(town, this, CAction.ActionMode.IDLE);
			action_church = new CAction(town, this, CAction.ActionMode.CHURCH);
			action_zombie = new CAction(town, this, CAction.ActionMode.ZOMBIE);
			action_orderpizza = new CAction(town, this, CAction.ActionMode.ORDER_PIZZA);
			action_Shower = new CAction(town, this, CAction.ActionMode.SHOWER);
			action_Fridge = new CAction(town, this, CAction.ActionMode.FRIDGE); 
			action_Bed = new CAction(town, this, CAction.ActionMode.BED);
			action_Toilet = new CAction(town, this, CAction.ActionMode.TOILET);
			action_ToiletFloor = new CAction(town, this, CAction.ActionMode.TOILET_FLOOR);
			action_Workplace = new CAction(town, this, CAction.ActionMode.WORKPLACE);
			action_SupermarketBuyin = new CAction(town, this, CAction.ActionMode.SUPERMARKET_BUYIN);
			//action_SupermarketRefillShelf = new CAction(gameWorld, this, CAction.ActionMode.SUPERMARKET_REFILLSHELF);
			action_cookdinner = new CAction(town, this, CAction.ActionMode.COOK_DINNER);
			action_eatdinner = new CAction(town, this, CAction.ActionMode.EAT_DINNER);
			action_washdishes = new CAction(town, this, CAction.ActionMode.WASH_DISHES);
			action_takeoutgarbage = new CAction(town, this, CAction.ActionMode.TAKE_OUT_GARBAGE);
			action_readbook = new CAction(town, this, CAction.ActionMode.READ_BOOK);
			action_watchtv = new CAction(town, this, CAction.ActionMode.WATCH_TV);
			action_playground = new CAction(town, this, CAction.ActionMode.PLAYGROUND);
			action_pubaction = new CAction(town, this, CAction.ActionMode.PUB_ACTION);
			action_breakroom = new CAction(town, this, CAction.ActionMode.BREAK_ROOM);
			action_fitnessstudio = new CAction(town, this, CAction.ActionMode.FITNESS_STUDIO);
			action_vehiclerefuel = new CAction(town, this, CAction.ActionMode.VEHICLE_REFUEL);
			action_funeralattend = new CAction(town, this, CAction.ActionMode.FUNERAL_ATTEND);
			action_gotodoctor = new CAction(town, this, CAction.ActionMode.GOTO_DOCTOR);
			action_laundry = new CAction(town, this, CAction.ActionMode.LAUNDRY);
			action_changeclothes = new CAction(town, this, CAction.ActionMode.CHANGE_CLOTHES);
			
			
			//*********************************************************************
			//---- ACHTUNG ---- in handleActions_Human -> Ausschlussliste ergänzen
			//*********************************************************************
			
			actionList_Default_Priority.add(action_cookdinner);
			actionList_Default_Priority.add(action_eatdinner);
			actionList_Default_Priority.add(action_SupermarketBuyin);
			//actionList_Default_Priority.add(action_SupermarketRefillShelf); //ist auch in worklist, wieso?
			actionList_Default_Priority.add(action_laundry);
			
			actionList_Default.add(action_Shower);
			actionList_Default.add(action_washdishes);
			actionList_Default.add(action_takeoutgarbage);
			actionList_Default.add(action_Fridge);
			actionList_Default.add(action_Toilet);
			actionList_Default.add(action_ToiletFloor);
			actionList_Default.add(action_changeclothes);
			actionList_Default.add(action_Bed);
			actionList_Default.add(action_orderpizza);
			
						
			//Freetime Activities
			actionList_Freetime.add(action_readbook);
			actionList_Freetime.add(action_watchtv);
			actionList_Freetime.add(action_playground);
			actionList_Freetime.add(action_pubaction);
			actionList_Freetime.add(action_fitnessstudio);
			actionList_Freetime.add(action_church);
			sortFreeTimeActionList();
			
			//------------------------------------------------------
			actionList_Work.add(action_laundry);
			actionList_Work.add(action_SupermarketBuyin);
			actionList_Work.add(action_orderpizza);
			actionList_Work.add(action_cookdinner);
			actionList_Work.add(action_washdishes);
			actionList_Work.add(action_takeoutgarbage);
			actionList_Work.add(action_eatdinner);
			actionList_Work.add(action_Fridge);
			actionList_Work.add(action_Toilet);
			actionList_Work.add(action_ToiletFloor);
			actionList_Work.add(action_breakroom);
			actionList_Work.add(action_Workplace);
			//actionList_Work.add(action_SupermarketRefillShelf); //ist auch in default prio list - wieso?
			actionList_Work.add(action_takeoutgarbage);
			//------------------------------------------------------
		
			activeActionMode=CAction.ActionMode.IDLE;
			activeActionModeTemp=CAction.ActionMode.IDLE;
		}
		
		if(createBody)
			initBox2DBody();
		
		if(obj.editoraction.contains("light"))// && box2dBody!=null)
		{
			//plight = new PointLight(gameWorld.rayHandler, 100, Color.WHITE, 1000, pos_x()+width/2, pos_y()+height/2);
			//plight = new PointLight(gameWorld.rayHandler, 100, Color.WHITE, theobject.getLightPower()*1.6f, pos_x()+width/2, pos_y()+height/2);
			plight = new PointLight(town.gameWorld.rayHandler, 8, Color.WHITE, theobject.getLightPower()*1.6f, pos_x()+width/2, pos_y()+height/2);
			plight.setActive(false);
			//plight.setActive(true);
			//light = new ConeLight(gameWorld.rayHandler, 100, null, 1000, pos_x, pos_y, 360f, 360);
			//plight.attachToBody(box2dBody ,0, 0, 0);
		}
		
		if(obj.editoraction.contains("_tv"))
		{
			int size = width;
			if(height>size)
				size=height;
			
			Vector2 v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), width/2-(int)town.getSizeValue(10), 0, width/2, height/2, rotation());
						
			//clight = new ConeLight(gameWorld.rayHandler, 10, new Color(1,1,1,0.01f) , size, v2.x, v2.y, rotation()+270, 90);
			clight = new ConeLight(town.gameWorld.rayHandler, 10, new Color(1,1,1,0.2f) , size, v2.x, v2.y, rotation()+270, 90);
			clight.setActive(false);
		}
		
		if(obj.editoraction.contains("traffic_car"))
		{
			int size = width;
			if(height>size)
				size=height;
			
			size=(int)town.getSizeValue(1200);
			
			Vector2 v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), width/2+width/4, 0, width/2, height/2, rotation());
			clight = new ConeLight(town.gameWorld.rayHandler, 10, null , size, v2.x, v2.y, rotation()+270, 12);
			
			v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), width/2-width/4, 0, width/2, height/2, rotation());
			clight2 = new ConeLight(town.gameWorld.rayHandler, 10, null , size, v2.x, v2.y, rotation()+270, 12);
			
			clight.setActive(false);
			clight2.setActive(false);
		}
		
		if(theobject.editoraction.contains("outdoor_plant") || 
				theobject.editoraction.contains("outdoor_tree") 
				|| theobject.editoraction.contains("outdoor_flower"))
		{
			setDynamicSize(1);
		}
		
		//energyConsumption*=town.delta_energyconsumption;
		//waterConsumption*=town.delta_waterconsumption;
		//defaultEnergyOutput*=town.delta_energyoutput;
		//defaultWaterOutput*=town.delta_wateroutput;
		
		//Set dynamic snow texture
		if(theobject.editoraction.contains("outdoor_tree") || 
				theobject.editoraction.contains("outdoor_plant") || 
				theobject.editoraction.contains("outdoor_flower"))
		{
			int ival = rand.nextInt(town.gameResourceConfig.animations.get("texture_snow1").getKeyFrames().length);
			TextureRegion snow1 = town.gameResourceConfig.animations.get("texture_snow1").getKeyFrames()[ival];
			theobject.textureRegion2 = new TextureRegion[1];
			theobject.textureRegion2[0]=snow1;
		}
		
	}
	
	public int getNextUniqueId()
	{
		//UniqueId
		//rand = new Random();
		//uniqueId=rand.nextInt(Integer.MAX_VALUE);
		OptionalInt opt = town.gameWorld.worldObjects.stream().mapToInt(item->item.uniqueId).max();
		OptionalInt opt2 = town.gameWorld.worldHumans.stream().mapToInt(item->item.uniqueId).max();
		OptionalInt opt3 = town.gameWorld.worldRoomObjects.stream().mapToInt(item->item.uniqueId).max();
		OptionalInt opt4 = town.gameWorld.worldGarbageContainers.stream().mapToInt(item->item.uniqueId).max();
		OptionalInt opt5 = town.gameWorld.worldFootpath.stream().mapToInt(item->item.uniqueId).max();
		OptionalInt opt6 = town.gameWorld.worldRoad.stream().mapToInt(item->item.uniqueId).max();
		OptionalInt opt7 = town.gameWorld.worldOutdoorLights.stream().mapToInt(item->item.uniqueId).max();
		OptionalInt opt8 = town.gameWorld.worldGroundObjects.stream().mapToInt(item->item.uniqueId).max();
		OptionalInt opt9 = town.gameWorld.worldZombies.stream().mapToInt(item->item.uniqueId).max();
		OptionalInt opt10 = town.gameWorld.worldBirds.stream().mapToInt(item->item.uniqueId).max();
		OptionalInt opt11 = town.gameWorld.worldDrawSpecial.stream().mapToInt(item->item.uniqueId).max();
		OptionalInt opt12 = town.gameWorld.worldCoverlights.stream().mapToInt(item->item.uniqueId).max();
		OptionalInt opt13 = town.gameWorld.worldDrawSpecial2.stream().mapToInt(item->item.uniqueId).max();
		OptionalInt opt14 = town.gameWorld.worldCars.stream().mapToInt(item->item.uniqueId).max();
		OptionalInt opt15 = town.gameWorld.worldDefenseWarning.stream().mapToInt(item->item.uniqueId).max();
		OptionalInt opt16 = town.gameWorld.worldZombieEntrances.stream().mapToInt(item->item.uniqueId).max();
		OptionalInt opt17 = town.gameWorld.worldWatersystems.stream().mapToInt(item->item.uniqueId).max();
		
		int tempi=0;
		if(opt.isPresent())
			tempi=opt.getAsInt();
		if(opt2.isPresent() && opt2.getAsInt()>tempi)
			tempi=opt2.getAsInt();
		if(opt3.isPresent() && opt3.getAsInt()>tempi)
			tempi=opt3.getAsInt();
		if(opt4.isPresent() && opt4.getAsInt()>tempi)
			tempi=opt4.getAsInt();
		if(opt5.isPresent() && opt5.getAsInt()>tempi)
			tempi=opt5.getAsInt();
		if(opt6.isPresent() && opt6.getAsInt()>tempi)
			tempi=opt6.getAsInt();
		if(opt7.isPresent() && opt7.getAsInt()>tempi)
			tempi=opt7.getAsInt();
		if(opt8.isPresent() && opt8.getAsInt()>tempi)
			tempi=opt8.getAsInt();
		if(opt9.isPresent() && opt9.getAsInt()>tempi)
			tempi=opt9.getAsInt();
		if(opt10.isPresent() && opt10.getAsInt()>tempi)
			tempi=opt10.getAsInt();
		if(opt11.isPresent() && opt11.getAsInt()>tempi)
			tempi=opt11.getAsInt();
		if(opt12.isPresent() && opt12.getAsInt()>tempi)
			tempi=opt12.getAsInt();
		if(opt13.isPresent() && opt13.getAsInt()>tempi)
			tempi=opt13.getAsInt();
		if(opt14.isPresent() && opt14.getAsInt()>tempi)
			tempi=opt14.getAsInt();
		if(opt15.isPresent() && opt15.getAsInt()>tempi)
			tempi=opt15.getAsInt();
		if(opt16.isPresent() && opt16.getAsInt()>tempi)
			tempi=opt16.getAsInt();
		if(opt17.isPresent() && opt17.getAsInt()>tempi)
			tempi=opt17.getAsInt();
		
		tempi++;
		
		return tempi;		
		
	}
	
	public void sortFreeTimeActionList()
	{
		Collections.shuffle(actionList_Freetime);
		
		//	actionList_Freetime.add(action_readbook);				ok
		//	actionList_Freetime.add(action_watchtv);				ok
		//	actionList_Freetime.add(action_playground);				
		//	actionList_Freetime.add(action_pubaction);				
		//	actionList_Freetime.add(action_fitnessstudio);			
		
		//happiness
		//health
			
		//health attitude
		//positive attitude
			
		//fitness
		//intelligence
			
		//random
		
		//wenn fitness oder intelligence max level -> action nicht ausführens -> in CAction umgesetzt initAction
		
		//Distance to target
			//CCompany pub=CCompany.getActiveCompanyByDistance(CompanyType.PUB, gameWorld.worldCompanyList, parentWorldObject.pos_x(), parentWorldObject.pos_y());
			//if(pub==null)
			//	return;
	}
	
	public void setWorker(CWorldObject w, int nr)
	{
		if(nr==1)
		{
			worker=w;
			workerId=w.uniqueId;
		}
		
		if(nr==2)
		{
			worker2=w;
			worker2Id=w.uniqueId;
		}
	}
	
	public void initHead(String gender, String headTextureId)
	{
		CObject hobj=null;
		if(gender.equals("m"))
			hobj = town.gameResourceConfig.listObjectHead_Men.stream().filter(item->item.objectId.equals(headTextureId)).findFirst().get();
		else if(gender.equals("w"))
			hobj = town.gameResourceConfig.listObjectHead_Women.stream().filter(item->item.objectId.equals(headTextureId)).findFirst().get();
		
		head=hobj.textureImage;
		headFrames=hobj.textureRegion;
		
		//head = new Texture(Gdx.files.internal(texturename), true);
		//head.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		//headFrames = new TextureRegion[1];
		//headFrames[0] = new TextureRegion(head);
        //headFrames[0].flip(false, true);
	}
	
	public CWorldObject clone()
	{
		CWorldObject clone = new CWorldObject(theobject, town, true);
		
		clone.actiontime1=actiontime1;
		clone.actiontime2=actiontime2;
		clone.actiontime3=actiontime3;
		
		clone.workTime1_From=workTime1_From;
		clone.workTime1_To=workTime1_To;
		clone.workTime2_From=workTime2_From;
		clone.workTime2_To=workTime2_To;
		//clone.workTime_Hours=workTime_Hours;
		//clone.workTime2_Hours=workTime2_Hours;
		
		clone.dynamicheight=dynamicheight;
		clone.dynamicwidth=dynamicwidth;
		clone.dynamicprice=dynamicprice;
		clone.scrollheight=scrollheight;
		clone.scrollwidth=scrollwidth;
		
		clone.setPosition(pos_x(), pos_y());
		clone.setRotation(rotation());
		
		return clone;
	}
		
//	private float defense_schussfrequenz_timer;
//  private float defense_schussfrequenz;
//	private float defense_reichweite;
//	private float defense_schaden;
//	private float defense_verlangsamung;
//	private float defense_schussgeschwindigkeit;	
	public float getDefense_reichweite()
	{
		return (theobject.defense_reichweite/100)*objectConditionPercent();
	}
	
	public float getDefense_schaden()
	{
		return theobject.defense_schaden; //*objectConditionPercent();
	}
	
	public float getDefense_schussfrequenz()
	{
		return theobject.defense_schussfrequenz;//*objectConditionPercent();
	}
	public float getDefense_projektilgeschwindigkeit()
	{
		return theobject.defense_projektilgeschwindigkeit;//*objectConditionPercent();
	}

	
	//POSITION---------------------------------------->
	public void adjustConeLightPosition()
	{
		if(clight!=null)
		{
			if(theobject.editoraction.contains("_tv"))
			{
				int size = width;
				if(height>size)
					size=height;
				
				Vector2 v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), width/2-(int)town.getSizeValue(10), (int)town.getSizeValue(30), width/2, height/2, rotation());
				clight.setDirection(rotation()+270);
				clight.setPosition(v2.x, v2.y);
			}
			
			if(theobject.editoraction.contains("traffic_car"))
			{
				int size = width;
				if(height>size)
					size=height;
				
				Vector2 v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), width/2+width/4, 0, width/2, height/2, rotation());
				clight.setDirection(rotation()+270);
				clight.setPosition(v2.x, v2.y);
				
				v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), width/2-width/4, 0, width/2, height/2, rotation());
				clight2.setDirection(rotation()+270);
				clight2.setPosition(v2.x, v2.y);
			}
		}
	}
	public float rotation()
	{
		return frotation;
	}
	public int pos_x()
	{
		return x;
	}
	public int pos_y()
	{
		return y;
	}
	public void setRotation(float rot)
	{
		try
		{
		if(thehuman==null && theobject.isCar==false && !theobject.editoraction.contains("bird"))
			town.gameWorld.addObjectToPathmap(this, false);
		
		frotation=rot;
		
		if(theobject!=null)
			theobject.rotation=(int) rot;
		
		if(box2dBody!=null && box2dBody.getAngle()!=rot/57)
		{
       		box2dBody.setTransform(box2dBody.getPosition().x, box2dBody.getPosition().y, rot/57);
		}
		
		adjustConeLightPosition();
		
		if(thehuman==null && theobject.isCar==false && !theobject.editoraction.contains("bird"))
			town.gameWorld.addObjectToPathmap(this, true);
		}
		catch(Exception e)
		{
			//..
		}
	}
	public void setPosition(int px, int py)
	{
		x=px;
		y=py;
		
		if(theobject!=null)
		{
			theobject.pos_x=x;
			theobject.pos_y=y;
		}
		
		if(plight!=null)
		{
			plight.setPosition(x+width/2, y+height/2);
		}
		
		adjustConeLightPosition();
		
		if(box2dBody!=null)
		{
			int objanim_x=0;
			int objanim_y = 0;
			
			if(theobject.objectAnimation != null)
			{
				objanim_x = theobject.objectAnimation.getKeyFrame(0, true).getRegionWidth()/2;
				objanim_y = theobject.objectAnimation.getKeyFrame(0, true).getRegionHeight()/2;
			}
			
			if(objanim_x==0)
			{
				objanim_x = theobject.width/2;
				objanim_y = theobject.height/2;
			}
			
        	float pos_x1 = (x + objanim_x)*town.gameWorld.b2dvaluePos;
        	float pos_y1 = (y + objanim_y)*town.gameWorld.b2dvaluePos;
			
    		box2dBody.setTransform(pos_x1, pos_y1, box2dBody.getAngle());
		}
		
		if(box2dBody_light!=null)
		{
			int objanim_x=0;
			int objanim_y = 0;
			
			if(theobject.objectAnimation != null)
			{
				objanim_x = theobject.objectAnimation.getKeyFrame(0, true).getRegionWidth()/2;
				objanim_y = theobject.objectAnimation.getKeyFrame(0, true).getRegionHeight()/2;
			}
			
			if(objanim_x==0)
			{
				objanim_x = theobject.width/2;
				objanim_y = theobject.height/2;
			}
			
        	float pos_x1 = (x + objanim_x)*town.gameWorld.b2dvaluePos;
        	float pos_y1 = (y + objanim_y)*town.gameWorld.b2dvaluePos;
			
        	if(Math.abs(box2dBody_light.getPosition().x-pos_x1)>1 || Math.abs(box2dBody_light.getPosition().y-pos_y1)>1)
        	{
        		box2dBody_light.setTransform(pos_x1, pos_y1, box2dBody.getAngle());
        	}			
		}
	}
	public void setPosition_byHumanBase(CWorldObject human, int movx, int movy, float rot)
	{
		//Vector2 v2 = CHelper.moveVectorByRotationS2D(human.pos_x(), human.pos_y(), movx, movy, human., originy, rotation);
		Vector2 v2 = CHelper.moveObjectByHumanRotation(human, this, movx, movy);
		setPosition((int)v2.x, (int)v2.y);
		setRotation(rot);
	}
	public void setPosition_byObjectBase(CWorldObject obj, int movx, int movy, float rot)
	{
		setPosition_byObjectBase(obj.pos_x(), obj.pos_y(), obj.width, obj.height, obj.rotation(), movx, movy, rot);
	}
	public void setPosition_byObjectBase(CObject obj, int movx, int movy, float rot)
	{
		setPosition_byObjectBase(obj.pos_x, obj.pos_y, obj.width, obj.height, obj.rotation, movx, movy, rot);
	}
	public void setPosition_byObjectBase(int basex, int basey, int basew, int baseh, float baserot, int movx, int movy, float rot)
	{
		Vector2 v2 = CHelper.moveVectorByRotationS2D(basex, basey, movx, movy, basew/2, baseh/2, baserot); 
		//Vector2 v2 = CHelper.moveVectorByRotationS2D(obj.pos_x(), obj.pos_y(), movx, movy, 0, 0, obj.rotation());
		setPosition((int)v2.x, (int)v2.y);
		setRotation(rot);
	}
	public int width_human()
	{
		int man_=0;
		
		if(thehuman!=null && thehuman.getAge()>17)
		{
			if(thehuman.gender=='m')
				man_=10;
		}
		
		return(int)(width*getBodySizeByAge())-man_;
	}
	public int height_human()
	{
		int man_=0;
		if(thehuman!=null && thehuman.getAge()>17)
		{
			if(thehuman.gender=='m')
				man_=10;
		}
		return(int)(height/getHeadSizeByAge())-man_;
	}
	public Vector3 getScreenPosition(int addWorldValueX, int addWorldValueY)
	{
		Vector3 vdpos=new Vector3();
		vdpos.y=pos_y()+addWorldValueY;
		vdpos.x=pos_x()+addWorldValueX;
		vdpos=town.gameCam.project(vdpos);
		return vdpos;
	}
	//POSITION<----------------------------------------
	
	
	//ACTIONS----------------------------------------->
	public void handleActions(float stateTime)
	{
		//		int a = 0;
		//		if(a==0)
		//			return;
		
		if(town.gameWorld.worldPause)
			return;
		
		if(bIsDead)
			return;
		
		if(iZombie>=1)
		{
			if(activeAction==null)
			{
				activeActionMode = ActionMode.ZOMBIE;
				activeAction = action_zombie;
			}
			
			activeActionMode = activeAction.handleAction(stateTime, activeActionMode);
			return;
		}
		
		if(bObjMoving)
		{
			return;
		}
		//activeAction=null; //für Informationszwecke
		//auskommentiert -> da activeaction vor listen verwendet wird
		
		if(theobject.editoraction.contains("human") && iZombie<1)
		{
			if(!thehuman.isWorking("company_church_workingplace_battlepriest"))
			{
				Boolean bZombieEscape = checkZombieEscape();
				if(bZombieEscape)
					return;
				else
					zombieRun=0;
			}
			
			handleActions_Human(stateTime);
		}
		else if(theobject.editoraction.contains("bird"))
		{
			walkAround_Bird();
		}
	}
	public void checkZombieDefense()
	{
		//if(1==1)
		//	return;
		
		if(iObjectIsReady<100)
			return;
		
		//bDefenseAlarm=false;
		if(theobject.editoraction.contains("illuminati_defensesystem") 
				|| theobject.editoraction.contains("illuminati_defensewarningsystem"))
		{
			if(isActiveByEnergyConsumption() && isActiveByWaterConsumption() && onlineByWorkInput)
			{
				int dist=CWorld.mapsize;
				CWorldObject zombie=null;
				for(CWorldObject z1 : town.gameWorld.worldZombies)
				{
					if(z1.bIsDead)
						continue;
					
					int tempdist = CHelper.getEuclidianDistance(this, z1);
					
					if(tempdist<dist)
					{
						dist=tempdist;
						zombie=z1;
					}
				}
				
				if(theobject.editoraction.contains("illuminati_defensewarningsystem"))
				{
					if(zombie!=null && dist<getDefense_reichweite())
					{
						if(!bDefenseAlarm)
						{
							if(!town.gameAudio.soundIsPlaying("EFFECT_ALARM"))
							{
								town.gameAudio.playSound("EFFECT_ALARM", 0, false);
								if(!town.gameAudio.soundIsPlaying("EFFECT_ALARM"))
									town.gameAudio.playSound("EFFECT_ALARM", 0, false);
								if(!town.gameAudio.soundIsPlaying("EFFECT_ALARM"))
									town.gameAudio.playSound("EFFECT_ALARM", 0, false);
								if(!town.gameAudio.soundIsPlaying("EFFECT_ALARM"))
									town.gameAudio.playSound("EFFECT_ALARM", 0, false);
							}
						}
						
						bDefenseAlarm=true;
					}
					else
						bDefenseAlarm=false;
				}
				
				if(theobject.editoraction.contains("illuminati_defensesystem") 
						&& dist<theobject.defense_reichweite 
						&& zombie!=null)
				{
					theobject.defense_schussfrequenz_timer+=CHelper.getDeltaSeconds(town);
					
					if(theobject.defense_schussfrequenz_timer>theobject.defense_schussfrequenz)
					{
						theobject.defense_schussfrequenz_timer=0;
						SpriteMoveEventType texturetype = SpriteMoveEventType.BULLET;
						CSpriteMoveEvent ev1 = new CSpriteMoveEvent(texturetype, pos_x()+width/2+rand.nextInt(30)-rand.nextInt(30), pos_y()+height/2+rand.nextInt(30)-rand.nextInt(30), zombie.pos_x()+zombie.width/2+rand.nextInt(120)-rand.nextInt(120), zombie.pos_y()+zombie.height/2+rand.nextInt(120)-rand.nextInt(120), town, 1);
						ev1.speed1 = theobject.defense_projektilgeschwindigkeit;
						//float f1 = theobject.defense_schaden/100f;
						//float f2 = objectConditionPercent();
						ev1.damage1 = getDefense_schaden();
						//spriteMoveEvents.add(ev1);
						town.gameWorld.spriteMoveEvents.add(ev1);
						
						if(town.gameCam.frustum.pointInFrustum(pos_x()+width/2, pos_y()+height/2, 0))
						{
							if(!town.gameAudio.soundIsPlaying("EFFECT_SHOT1"))
								town.gameAudio.playSound("EFFECT_SHOT1", -0.1f, true);
							else if(!town.gameAudio.soundIsPlaying("EFFECT_SHOT2"))
								town.gameAudio.playSound("EFFECT_SHOT2", -0.5f, true);
							else if(!town.gameAudio.soundIsPlaying("EFFECT_SHOT3"))
								town.gameAudio.playSound("EFFECT_SHOT3", -0.3f, true);
						}
					}
				}
			}
		}
	}
	public void unlinkResident()
	{
		town.gameWorld.unlinkWorkerAndWorkplace(this); //and taskobject owner
		town.gameWorld.removeOwner(this, this.thehuman.bed);
		town.gameWorld.removeOwner(this, this.thehuman.car);
		town.gameWorld.removeOwner(this, this.thehuman.wardrobe);
	}
	public Boolean checkZombieEscape()
	{
		//if(1==1)
		//	return false;
		
		if(iZombie>=1)
			return false;
		
		int dist=town.gameWorld.mapsize;
		CWorldObject zombie=null;
		if(zombieEscapeTimer>0)
			zombieEscapeTimer-=CHelper.getDeltaSeconds(town);
		
		for(CWorldObject z1 : town.gameWorld.worldZombies)
		{
			if(z1.bIsDead)
				continue;
			
			int tempdist = CHelper.getEuclidianDistance(this, z1);
			if(tempdist<dist)
			{
				dist=tempdist;
				zombie=z1;
			}
		}
		
		if(zombie!=null && dist<50 && iZombie<1)
		{
			iZombie+=0.1f;
			if(iZombie>=1)
			{
				thehuman.lastname+=" (Zombie)";
				unlinkResident();
			}
		}
		
		if(zombie!=null && ((dist<500 || zombieEscapeTimer>0)))
		{
			cancelAction1();
			
			int gotox=0;
			int gotoy=0;
			
			int rundist=300;
			
			if(zombie.pos_x()>pos_x())
				gotox=pos_x()-rand.nextInt(rundist);
			else
				gotox=pos_x()+rand.nextInt(rundist);
			
			if(zombie.pos_y()>pos_y())
				gotoy=pos_y()-rand.nextInt(rundist);
			else
				gotoy=pos_y()+rand.nextInt(rundist);
			
			if(gotox>CWorld.mapsize)
				gotox=0;
			
			if(gotox<0)
				gotox=CWorld.mapsize;
			
			if(gotoy>CWorld.mapsize)
				gotoy=0;
			
			if(gotoy<0)
				gotoy=CWorld.mapsize;
			
			zombieRun=1;
			initTargetPath(gotox, gotoy, false, null);
			pathFinding(town.gameWorld.stateTime);
			if(zombieEscapeTimer<=0)
				zombieEscapeTimer=200;
			
			return true;
		}
		
		if(zombieEscapeTimer>0)
			return true;
		
		return false;
	}
	public void handleActions_Human(float stateTime)
	{
		Boolean bdebugcanceled=false;
		
		if(activeAction==null)
		{
			activeActionMode=ActionMode.IDLE; //Exception Error verhindern
			activeAction=action_idle;
		}
		
		//**************
		//Vehicle Refuel wird zwischendurch ausgeführt, aktuelle Action wird zwischengespeichert
		//**************
		if(activeActionMode==ActionMode.VEHICLE_REFUEL)
			activeActionMode = activeAction.handleAction(stateTime, activeActionMode);
		
		//Wenn Zwischenaction gesetzt wurde, diese wieder einsetzen
		if(activeActionMode == ActionMode.IDLE)
		{
			if(activeActionModeTemp != ActionMode.IDLE)
			{
				activeActionMode=activeActionModeTemp;
				activeAction=activeActionTemp;
				activeActionTemp=null;
				activeActionModeTemp=ActionMode.IDLE;
			}
		}
		
		if(activeActionMode==ActionMode.VEHICLE_REFUEL)
			return;
		
		Boolean bTimeForWork = false;
		Boolean bTimeForTask = false;
				
		if(thehuman.canWork())
		{
			bTimeForWork = thehuman.timeForWork();
			bTimeForTask = thehuman.timeForTask();
			
			if(!bTimeForTask)
			{
				//-> Only High Prio WorkTasks !
				if(thehuman.getWorkTaskPlaceByTrigger()!=null)
				{
					bTimeForTask=true;
				}
			}
		}
		
		
		//*************
		//High Priority Actions die alles andere canceln
		//*************
		//if(2==1)
		{
			CAction highPrioAction = thehuman.getHighPrioActionByTrigger();
			
			if(highPrioAction!=null)
			{
				bTimeForTask=true;
				Boolean bSetNewAction=true;
				
				//Bestehende Action nur ersetzen, wenn neue Action eine höhere Priorität hat
				if(activeAction!=null)
				{
					int activeActionPrio=0;
					
					String seditoraction="";
					if(activeAction!=null && activeAction.targetActionObject!=null)
						seditoraction=activeAction.targetActionObject.theobject.editoraction;
					
					String sheditoraction="";
					if(highPrioAction!=null && highPrioAction.targetActionObject!=null)
					{
						sheditoraction=highPrioAction.targetActionObject.theobject.editoraction;
					}
					
					if(activeAction!=null)
						activeActionPrio = thehuman.getHighPrioActionPriority(seditoraction, activeAction);
					
					int highPrioActionPrio = thehuman.getHighPrioActionPriority(sheditoraction, highPrioAction);
					
					if(highPrioActionPrio<=activeActionPrio)
						bSetNewAction=false;
				}
				
				if(bSetNewAction) 
				{
					if(highPrioAction!=null)
					{
						if(activeAction!=null && activeAction.actionMode!=highPrioAction.actionMode)
						{
							cancelAction1();
							activeActionMode=highPrioAction.actionMode;
							activeAction=highPrioAction;
						}
					}
				}
			}
		}
		
		
		//******
		//Schlaf kann nur von Alarm Clock für Tätigkeit abgebrochen werden
		//******
		if(activeActionMode==ActionMode.BED)
		{
			if(bTimeForWork || bTimeForTask) //Check Alarm Clock
			{
				//check alarmclocks
				if(theaddress!=null)
				{
				for(CWorldObject wobj : theaddress.listWorldObjects)
				{
					if(wobj.theobject.editoraction.contains("_nightstand"))
					{
						int direction=0; //links im doppelbett oder mitte im einzelbett
						if(thehuman.bed.theobject.editoraction.contains("bedroom_bed_double"))
						{
							if(thehuman.bed.owner2!=null && thehuman.bed.owner2.uniqueId==uniqueId) 
								direction=1; //rechts
						}
						
						Boolean bCol = Intersector.overlapConvexPolygons(thehuman.bed.theobject.getZonePolygonForNighttablePlacement(direction), wobj.getBoundingPolygon(IntersectionMode.COLLISION));		
						if(bCol)
						{
							if(wobj.theobject.ATTR_HAPPINESS>0 && activeAction!=null && activeAction.bActionMode && activeAction.actionMode == ActionMode.BED)
							{
								thehuman.changeHappinessValue(wobj.theobject.ATTR_HAPPINESS);
								addAnimationEvent(AnimationEventType.HAPPINESS, wobj.theobject.ATTR_HAPPINESS);
							}
							
							wobj.tempcount=400; //wecker klingelt
							cancelAction1(); //test ob hier gecanceled werden darf
							bdebugcanceled=true;
							break;
						}
					}	
				}
				}
			}
		}
		
		if((bTimeForWork || bTimeForTask) && activeAction!=null)
		{
			//hier Actions ergänzen die nicht in worklist enthalten sind und abgebrochen werden sollen für work
			if( 
					activeActionMode==ActionMode.READ_BOOK || activeActionMode==ActionMode.WATCH_TV
					|| activeActionMode==ActionMode.PLAYGROUND || activeActionMode==ActionMode.PUB_ACTION
					|| activeActionMode==ActionMode.FITNESS_STUDIO || activeActionMode==ActionMode.CHURCH
					)
			{
				cancelAction1(); //test ob hier gecanceled werden darf
			}
		}
		else 
		{
			//Arbeit außerhalb Arbeitszeit beenden
			if(activeActionMode==ActionMode.WORKPLACE)
			{
				if(activeAction!=null) //canceln, da sonst action nicht beendet wird und dann irgendwo anders plötzlich gearbeitet wird
				{
					cancelAction1();
				}
			}
		}
		
		
		if(activeActionMode==ActionMode.IDLE)
		{
			action_idle.handleAction(stateTime, activeActionMode);
			activeAction=null;
		}
		
		if(activeAction!=null && activeActionMode!=ActionMode.IDLE)
		{
			activeActionMode = activeAction.handleAction(stateTime, activeActionMode);
		}
		else
		{
			for(CAction behavior : actionList_Work)
			{
				activeActionMode = behavior.handleAction(stateTime, activeActionMode);
				if(activeAction!=null)
					break;
			}
			
			if(!thehuman.timeForWork() || !thehuman.canWork())
			{
				if(activeAction==null)
				{
					for(CAction behavior : actionList_Default_Priority)
					{
						activeActionMode = behavior.handleAction(stateTime, activeActionMode);
						if(activeAction!=null)
							break;
					}
				}
				
				if(activeAction==null)
				{
					for(int i=0;i<actionList_Default.size();i++)
					{
						CAction behavior=actionList_Default.get(i);
						activeActionMode = behavior.handleAction(stateTime, activeActionMode);
						if(activeAction!=null)
							break;
					}
				}
				
				//if(uniqueId==4)
				//	Gdx.app.debug("", ""+activeActionMode.toString());
				
				if(activeAction==null)
				{
					for(CAction behavior : actionList_Freetime)
						activeActionMode = behavior.handleAction(stateTime, activeActionMode);
				}
			}
			
			if(activeAction==null)
			{
				activeAction=action_idle;
				activeActionMode=ActionMode.IDLE;
			}
		}
		
		//Last Action mode / last targetactionobject type
		//Set Fitnessstudio Multivalue
		if(activeActionMode!=CAction.ActionMode.IDLE)
		{
			if(activeAction!=null && activeAction.targetActionObject!=null)
			{
				if(activeAction.actionMode==ActionMode.FITNESS_STUDIO && lastActionMode==ActionMode.FITNESS_STUDIO)
				{
					if(!lastTargetActionObjectType.equals(activeAction.targetActionObject.theobject.editoraction))
						actionvar_fs1+=0.5f;
					
					if(actionvar_fs1>3)
						actionvar_fs1=3;
				}
				
				if(activeAction.actionMode!=ActionMode.FITNESS_STUDIO)
					actionvar_fs1=0;
				
				lastTargetActionObjectType="";
				lastTargetActionObjectType=activeAction.targetActionObject.theobject.editoraction;
			}
			
			lastActionMode=activeActionMode;
		}
	}
	public void walkAround_Bird()
	{
		if(theobject.editoraction.contains("bird"))
			birdFlyToAddressTime+=Gdx.graphics.getDeltaTime();
		
		if(ziel_x<0 && ziel_y<0)
		{
			//Problem wenn Ziel zu weit entfernt -> Flugrichtung und Rotation stimmen nicht überein 
			//Daher Ziele in unmittelbarer Nähe festlegen
			int tx = -1;
			int ty = -1;
			
			if(theobject.editoraction.contains("bird"))
			{
				//Birds fliegen nachts nicht herum
				if((town.gameWorld.worldTime.hours>21 || town.gameWorld.worldTime.hours<4) && ziel_x<0)
				{
					if(bDrawObject)
					{
						if(!bDrawObjectByCamera)
							bDrawObject=false;
						else
						{
							//Bird fliegt weg von Viewport
							tx=pos_x()+rand.nextInt(2000)-rand.nextInt(2000);
							ty=pos_y()+rand.nextInt(2000)-rand.nextInt(2000);
						}
					}
				}
				else
				{
					if(!bDrawObject) //Birds erscheinen tagsüber wieder
					{
						if(!bDrawObjectByCamera)
						{
							bDrawObject=true;
							birdFlyToAddressTime=actionfield1+1;
						}
						else
						{
							//Bird fliegt weg von Viewport
							tx=pos_x()+rand.nextInt(2000)-rand.nextInt(2000);
							ty=pos_y()+rand.nextInt(2000)-rand.nextInt(2000);
						}
					}
					
					if(actionfield1==0)
						actionfield1=60+town.rand.nextInt(80);
					
					if(birdFlyToAddressTime>actionfield1)
					{
						int iGotoWater=rand.nextInt(2);
						if(town.gameWorld.worldWaterObjects.size()>0 && iGotoWater==0)
						{
							int index = rand.nextInt(town.gameWorld.worldWaterObjects.size());
							CWorldObject target = town.gameWorld.worldWaterObjects.get(index);
							
							tx = (int)target.pos_x() + rand.nextInt(400)-rand.nextInt(400);
							ty = (int)target.pos_y() + rand.nextInt(400)-rand.nextInt(400);
							
							birdFlyToAddressTime=0;
							actionfield1=60+town.rand.nextInt(80);
						}
						else if(town.gameWorld.worldAddressList.size()>0)
						{
							int index = rand.nextInt(town.gameWorld.worldAddressList.size());
							CAddress targetAdr = town.gameWorld.worldAddressList.get(index);
							
							tx = (int)targetAdr.sx + rand.nextInt((int)targetAdr.ex-(int)targetAdr.sx);
							ty = (int)targetAdr.sy + rand.nextInt((int)targetAdr.ey-(int)targetAdr.sy);
							
							birdFlyToAddressTime=0;
							actionfield1=60+town.rand.nextInt(80);
						}
					}
					else
					{
						int l1 = width*50;
						if(width>40)
							l1=width*70;
						if(width>50)
							l1=width*90;
						if(width>60)
							l1=width*110;
						if(width>70)
							l1=width*130;
						
						tx = pos_x()+rand.nextInt(l1)-rand.nextInt(l1);
						ty = pos_y()+rand.nextInt(l1)-rand.nextInt(l1);
					}
				}
			}
			else
			{
				tx = pos_x()+rand.nextInt(300)-rand.nextInt(300);
				ty = pos_y()+rand.nextInt(300)-rand.nextInt(300);
			}
			
			if(tx>-1 && ty>-1)
				initTargetPath(tx, ty, false, null);
		}
	}
	public void walkAround(Boolean bCheckDoIt)
	{
		if(ziel_x<0 && ziel_y<0)
		{
			if(bCheckDoIt)
			{
				int doit = rand.nextInt(20);
				if(doit!=1)
					return;
			}
			
			int tx = -1;
			int ty = -1;
			
			int icount=0;
			while(true)
			{
				icount++;
				if(icount>10000)
				{
					tx = -1;
					break;
				}
								
				int bereich=500;
				
				int x = rand.nextInt(bereich);
				int y = rand.nextInt(bereich);
				
				//tx = rand.nextInt((gameWorld.mapsize - 1) + 1) + 1;
				//ty = rand.nextInt((gameWorld.mapsize - 1) + 1) + 1;
				
				if(x>bereich/2)
					tx=pos_x()+x/2;
				else
					tx=pos_x()-x/2;
				
				if(y>bereich/2)
					ty=pos_y()+y/2;
				else
					ty=pos_y()-y/2;
				
				if(tx>0 && tx<town.mapsize && ty>0 && ty<town.mapsize)
					break;
				
				//if(gameWorld.astar.isValid(tx/gameWorld.nodesize, ty/gameWorld.nodesize)!=null)
				//{
				//	break;
				//}
			}
		    
		    //int tx = rand.nextInt((8 - 1) + 1) + 1;
		    //int ty = rand.nextInt((8 - 1) + 1) + 1;
		    
			if(tx>-1 && ty>-1)
			{
				initTargetPath(tx, ty, false, null);
			}
		}
	}
	public float actionVarByNr(int nr)
	{
		if(nr==1)
			return actionvar1;
		if(nr==2)
			return actionvar2;
		if(nr==3)
			return actionvar3;
		if(nr==4)
			return actionvar4;
		if(nr==5)
			return actionvar5;
		if(nr==6)
			return actionvar6;
		if(nr==7)
			return actionvar7;
		if(nr==8)
			return actionvar8;
		if(nr==9)
			return actionvar9;
		
		return -1;
	}
	public void actionVarByNr(int nr, float setValue)
	{
		if(nr==1)
			actionvar1=setValue;
		if(nr==2)
			actionvar2=setValue;
		if(nr==3)
			actionvar3=setValue;
		if(nr==4)
			actionvar4=setValue;
		if(nr==5)
			actionvar5=setValue;
		if(nr==6)
			actionvar6=setValue;
		if(nr==7)
			actionvar7=setValue;
		if(nr==8)
			actionvar8=setValue;
		if(nr==9)
			actionvar9=setValue;
	}
	public CWorldObject isOccupiedBy(int nr)
	{
		if(nr==1)
			return isOccupiedBy;
		if(nr==2)
			return isOccupiedBy2;
		if(nr==3)
			return isOccupiedBy3;
		if(nr==4)
			return isOccupiedBy4;
		if(nr==5)
			return isOccupiedBy5;
		if(nr==6)
			return isOccupiedBy6;
		if(nr==7)
			return isOccupiedBy7;
		if(nr==8)
			return isOccupiedBy8;
		if(nr==9)
			return isOccupiedBy9;
		if(nr==10)
			return isOccupiedByExtern;
		
		return null;
	}
	public int isOccupiedByMe(CWorldObject me)
	{
		Boolean bRet = false;
		
		if(isOccupiedBy!=null && isOccupiedBy.uniqueId==me.uniqueId)
			return 1;
		if(isOccupiedBy2!=null && isOccupiedBy2.uniqueId==me.uniqueId)
			return 2;
		if(isOccupiedBy3!=null && isOccupiedBy3.uniqueId==me.uniqueId)
			return 3;
		if(isOccupiedBy4!=null && isOccupiedBy4.uniqueId==me.uniqueId)
			return 4;
		if(isOccupiedBy5!=null && isOccupiedBy5.uniqueId==me.uniqueId)
			return 5;
		if(isOccupiedBy6!=null && isOccupiedBy6.uniqueId==me.uniqueId)
			return 6;
		if(isOccupiedBy7!=null && isOccupiedBy7.uniqueId==me.uniqueId)
			return 7;
		if(isOccupiedBy8!=null && isOccupiedBy8.uniqueId==me.uniqueId)
			return 8;
		if(isOccupiedBy9!=null && isOccupiedBy9.uniqueId==me.uniqueId)
			return 9;
		if(isOccupiedByExtern!=null && isOccupiedByExtern.uniqueId==me.uniqueId)
			return 10;
			
		return -1;
	}
	//ACTIONS<----------------------------------------
	
	
	//PATHFINDING------------------------------------->
	public void releaseParkingPlaceOrGarage(CWorldObject vehicle)
	{
		CAddress adr = town.gameWorld.getAddressByPoint(vehicle.pos_x(), vehicle.pos_y());
		if(adr!=null)
		{
			for(CWorldObject wobj : adr.listWorldObjects)
			{
				if(wobj.theobject.editoraction.contains("_parkingspace") || 
						wobj.theobject.editoraction.contains("_garage") ||
						wobj.theobject.editoraction.contains("company_gasstation_gaspump")
						)
				{
					if(wobj.isOccupiedBy != null && wobj.isOccupiedBy.uniqueId==vehicle.uniqueId)
					{
						wobj.isOccupiedBy=null;
						return;
					}
				}
			}
		}
	}
	public void resetGoByCar()
	{
		if(thehuman!=null && thehuman.car!=null)
			thehuman.car.isOccupiedBy=null;
		
		goByCar_ActionState=0;
		goByCar_X=-1;
		goByCar_Y=-1;
		goByCar_TargetObject=null;
		
		//Gdx.app.debug("debug space", "release " + thehuman.getName() + ", goByCar_TargetParkingplace: " + goByCar_TargetParkingplace);
		if(goByCar_TargetParkingplace!=null)
			goByCar_TargetParkingplace.isOccupiedBy=null;
		
		goByCar_TargetParkingplace=null;
	}
	public int goByCar()
	{
		
		//Rückgabewert: true wenn in car eingestiegen
		if(goByCar_X<0)
			return 2;
		
		doorOpenTimer+=CHelper.getDeltaSeconds(town);
		
		//Am Car angekommen
		if(goByCar_ActionState==0)
		{
			if(ziel_x<0) //|| collides(thehuman.car))
			{
				setRotation(thehuman.car.rotation());
				doorOpenTimer=0;
				thehuman.car.actionanim1=1;
				goByCar_ActionState=1;
			}
			
			return 2;
		}
		
		if(goByCar_ActionState==1)
		{
			if(doorOpenTimer>40)
			{
				doorOpenTimer=0;
				thehuman.car.actionanim1=0;
				goByCar_ActionState=2;
			}
			
			return 0;
		}
		
		if(goByCar_ActionState==2)
		{
			if(goByCar_TargetParkingplace==null && (goByCar_X<0 || goByCar_Y<0))
			{
				goByCar_ActionState=4;
				return 0;
			}
			
			if(goByCar_TargetParkingplace==null)
			{
				thehuman.car.initTargetPath(goByCar_X, goByCar_Y, false, null);
				thehuman.car.pathFinding(town.gameWorld.stateTime);
			}
			else
			{
				thehuman.car.initTargetPath(goByCar_TargetParkingplace.pos_x()+goByCar_TargetParkingplace.width/2, goByCar_TargetParkingplace.pos_y()+goByCar_TargetParkingplace.height/2, false, goByCar_TargetParkingplace);
				thehuman.car.pathFinding(town.gameWorld.stateTime);
			}
			
			if(thehuman.car.path==null)
			{
				thehuman.car.resetPathFinding();
				ziel_x=goByCar_X;
				ziel_y=goByCar_Y;
				targetPathObject=goByCar_TargetObject;
				resetGoByCar();
				
				return 0;
			}
			
			//Aktuellen Parkingplace / Garage freigeben
			thehuman.car.releaseParkingPlaceOrGarage(thehuman.car);
			
			goByCar_ActionState=3;
			ziel_x=goByCar_X;
			ziel_y=goByCar_Y;
			targetPathObject=goByCar_TargetObject;
			bDrawObject=false;
			doorOpenTimer=0;
			
			return 1;
		}
		
		//Car kommt am Zielparkplatz an
		if(goByCar_ActionState==3)
		{
			if(thehuman.car.ziel_x<0) // || thehuman.car.collides(goByCar_TargetParkingplace))
			{
				thehuman.car.actionanim1=1;
				
				//Fahrzeug einparken
				if(goByCar_TargetParkingplace!=null)
				{
					Vector2 v2 = CHelper.moveVectorByRotationS2D(goByCar_TargetParkingplace.pos_x(), goByCar_TargetParkingplace.pos_y(), goByCar_TargetParkingplace.width/2, goByCar_TargetParkingplace.height/2, goByCar_TargetParkingplace.width/2, goByCar_TargetParkingplace.height/2, goByCar_TargetParkingplace.rotation());
					thehuman.car.setRotation(goByCar_TargetParkingplace.rotation());
					thehuman.car.setPosition((int)v2.x-thehuman.car.width/2, (int)v2.y-thehuman.car.height/2);
					
					v2 = CHelper.moveVectorByRotationS2D(thehuman.car.pos_x(), thehuman.car.pos_y(), thehuman.car.width+55, thehuman.car.height/2+10, thehuman.car.width/2, thehuman.car.height/2, thehuman.car.rotation());
					setPosition((int)v2.x, (int)v2.y);
					
					if(doorOpenTimer>50)
					{
						thehuman.car.actionanim1=0;
						goByCar_ActionState=4;
					}
				}
				else
				{
					goByCar_ActionState=4;
					return 1;
				}
					
			}
			else
			{
				doorOpenTimer=0;
			}
			
			return 1;
		}
		
		if(goByCar_ActionState==4)
		{
			if(goByCar_TargetParkingplace!=null || (goByCar_X>=0 && goByCar_Y>=0))
			{
				if(thehuman!=null && thehuman.car!=null)
				{
					setPosition(thehuman.car.pos_x(), thehuman.car.pos_y());
				}
			}
			
			goByCar_X=-1;
			goByCar_Y=-1;
			goByCar_TargetObject=null;
			goByCar_ActionState=0;
			bDrawObject=true;
			if(thehuman!=null && thehuman.car!=null)
			{
				thehuman.car.isOccupiedBy=null;
				
				float changehap=0;
				if(thehuman.car!=null && thehuman.car.theobject.ATTR_HAPPINESS>0)
					changehap=thehuman.car.theobject.ATTR_HAPPINESS;
				if(changehap>0)
				{
					changehap = thehuman.changeHappinessValue(changehap);
					addAnimationEvent(AnimationEventType.HAPPINESS, changehap);
				}
			}
			
			return 1;
		}
		
		return 2;
	}
	public void resetPathFinding()
	{
		//path=null; //nicht null setzen -> kommt sonst nicht am ziel an bzw zappelt herum
		temp_ziel_rot_x=0;
		temp_ziel_rot_y=0;
		ziel_x=-1;
		ziel_y=-1;
	}
	public void initTargetPath(int targetX, int targetY, Boolean onlyEmptyTargetNode, CWorldObject targetObj)
	{
		goByCar_TargetParkingplace=null;
		goByCar_TargetObject=null;
		goByCar_X=-1;
		goByCar_Y=-1;
		goByCar_ActionState=0;
		
		if(thehuman!=null && thehuman.car!=null && thehuman.car.bObjectIsReady)
		{
			Boolean bUseCar = true;
			
			CAddress targetAdr = town.gameWorld.getAddressByPoint(targetX, targetY);
			CAddress currentAdr = town.gameWorld.getAddressByPoint(pos_x(), pos_y());
			
			if(targetAdr!=null && currentAdr!=null && targetAdr.addressId==currentAdr.addressId)
				bUseCar = false;
			
			if(targetAdr==null)
				bUseCar = false;
			
			if(bUseCar)
			{
				//Mindestabtand zum Ziel
				int distanceToTarget = CHelper.getEuclidianDistance(this.pos_x(), this.pos_y(), targetX, targetY);
				
				if(distanceToTarget>5000)
				{
					//Ist Fahrzeug weniger weit weg als Zielobjekt?
					int distanceToCar = CHelper.getEuclidianDistance(this, thehuman.car);
					
					if(distanceToTarget>distanceToCar)
					{
						
						//Muss getankt werden?
						if(activeActionMode!=ActionMode.VEHICLE_REFUEL)
						{
							if(thehuman.car.fuelValue < thehuman.car.fuelValueMax/3)
							{
								//CCompany comp = gameWorld.getNearestActiveCompany(CompanyType.GAS_STATION, thehuman.car);
								CCompany comp = CCompany.getNextActiveCompany(CompanyType.GAS_STATION, town.gameWorld.town, thehuman.car.pos_x(), thehuman.car.pos_y(), uniqueId, 99999);
								if(comp!=null)
								{
									//Aktive Action unterbrechen
									activeActionTemp=activeAction;
									activeActionModeTemp=activeActionMode;
									
									//GAS_STATION Action initialisieren
									activeAction=action_vehiclerefuel;
									activeActionMode=ActionMode.VEHICLE_REFUEL;
									
									//Pathfinding muss zurückgesetzt werden, damit in initaction keine andere action gesetzt wird
									ziel_x=-1;
									ziel_y=-1;
									path=null;
									
									return;
								}
							}
						}
						
						//Gibt es einen freien Abstellplatz?
						if(targetObj != null && targetObj.theaddress!=null)
						{
							targetAdr = targetObj.theaddress;
						}
						else
							targetAdr = town.gameWorld.getAddressByPoint(targetX, targetY);
						
						if(targetAdr!=null)
						{
							goByCar_TargetParkingplace = targetAdr.getFreeParkingPlaceOrGarage(thehuman.car);
							
							if(goByCar_TargetParkingplace!=null)
							{
								//Gdx.app.setLogLevel(10);
								//Gdx.app.log("", ""+thehuman.getName()+", " + targetAdr.addressName + ", " + theaddress.addressName + ", "+targetAdr.addressId + ", " + theaddress.addressId + ", " + goByCar_TargetParkingplace.theobject.editoraction);
								
								goByCar_TargetParkingplace.isOccupiedBy = thehuman.car;
								//Gdx.app.debug("", "test1: " + goByCar_TargetParkingplace.test1 + ", test2: " + goByCar_TargetParkingplace.test2);
								goByCar_TargetParkingplace.test1=1;
								goByCar_TargetParkingplace.test2=1;
								goByCar_TargetObject = targetObj;
								goByCar_X = targetX;
								goByCar_Y = targetY;
								thehuman.car.isOccupiedBy = this;
								
								Vector2 v2 = CHelper.moveVectorByRotationS2D(thehuman.car.pos_x(), thehuman.car.pos_y(), thehuman.car.width+60, thehuman.car.height/2+30, thehuman.car.width/2, thehuman.car.height/2, thehuman.car.rotation());
								ziel_x = (int)v2.x;
								ziel_y = (int)v2.y;
								bonlyEmptyTargetNode = onlyEmptyTargetNode;
								targetPathObject = thehuman.car;
								
								return; //wenn car driving
							}
							else
							{
								if(targetAdr.addressId == theaddress.addressId)
								{
									//no need for garage on residential address
									goByCar_X = (int) targetAdr.sx;
									goByCar_Y = (int) targetAdr.sy;
									
									Vector2 v2 = CHelper.moveVectorByRotationS2D(thehuman.car.pos_x(), thehuman.car.pos_y(), thehuman.car.width+60, thehuman.car.height/2+30, thehuman.car.width/2, thehuman.car.height/2, thehuman.car.rotation());
									ziel_x = (int)v2.x;
									ziel_y = (int)v2.y;
									bonlyEmptyTargetNode = onlyEmptyTargetNode;
									targetPathObject = thehuman.car;
									
									return; //wenn car driving
								}
							}
						}
					}
				}
			}
		}
		
		//Default Pathfinding
		if(targetX!=ziel_x || targetY!=ziel_y)
		{
			temp_ziel_rot_x=0;
			temp_ziel_rot_y=0;
			
			ziel_x = targetX;
			ziel_y = targetY;
			
			bonlyEmptyTargetNode=onlyEmptyTargetNode;
			targetPathObject=targetObj;
		}
	}
	public void pathFinding_RealBird(float stateTime)
	{
		if(ziel_x<0 || ziel_y<0)
			return;
		
		TextureRegion currentFrame=theobject.textureRegion[0];
		if(theobject.objectAnimation!=null)
			currentFrame=theobject.objectAnimation.getKeyFrame(stateTime, true);
		
		float bodyx = pos_x();
		float bodyy = pos_y();
		
		float fspeedvalue=1;
		
		if(town.gameGui!=null)
			fspeedvalue = CHelper.getSpeedControllerValue(town.gameGui);
		
		float speed = CHelper.getFPSValue(movementSpeed+fspeedvalue/2500);
		
		//Gibt es einen Weg der abgelaufen werden muss?
		if(Math.abs(ziel_x - (int)bodyx) > speed + CWorld.nodesize || Math.abs(ziel_y - (int)bodyy) > speed + CWorld.nodesize)
	    {
			temp_ziel_x = ziel_x;
			temp_ziel_y = ziel_y;
			
	    	float speedx=0;
	    	float speedy=0;
	    	
	        if(temp_ziel_x > (int)bodyx)
	        	speedx = speed;
	        else if(temp_ziel_x < (int)bodyx)
	        	speedx = speed * -1;
	        if(temp_ziel_y > (int)bodyy)
	        	speedy = speed;
	        else if(temp_ziel_y < (int)bodyy)
	        	speedy = speed * -1;
	        
        	frotation = (float) Math.atan2(((float)temp_ziel_y - (float)bodyy), ((float)((float)temp_ziel_x - ((float)bodyx))));
        	frotation = MathUtils.radiansToDegrees*frotation;
        	frotation = frotation+90;
        	
        	int x = (int)pos_x()+(int)speedx;
        	int y = (int)pos_y()+(int)speedy;
        	
        	setPosition(x, y);
	    }
		else
		{
			ziel_x=-1;
			ziel_y=-1;
		}
	}
	public void pathFinding_Bird(float stateTime)
	{
		if(ziel_x<0 || ziel_y<0)
			return;
		
		//if(box2dBody==null)
		//	return;
		
		TextureRegion currentFrame=theobject.textureRegion[0];
		if(theobject.objectAnimation!=null)
			currentFrame=theobject.objectAnimation.getKeyFrame(stateTime, true);
		
		//float bodyx = box2dBody.getPosition().x/gameWorld.b2dvaluePos-currentFrame.getRegionWidth()/2;
		//float bodyy = box2dBody.getPosition().y/gameWorld.b2dvaluePos-currentFrame.getRegionHeight()/2;
		float bodyx = pos_x(); //box2dBody.getPosition().x/gameWorld.b2dvaluePos-currentFrame.getRegionWidth()/2;
		float bodyy = pos_y(); //box2dBody.getPosition().y/gameWorld.b2dvaluePos-currentFrame.getRegionHeight()/2;

		
		float fspeedvalue=1;
		if(town.gameGui!=null)
			fspeedvalue = CHelper.getSpeedControllerValue(town.gameGui);     		
		
		//float speed = CHelper.getFPSValue(0.05f+fspeedvalue/25000);
		float speed = CHelper.getFPSValue(2f+fspeedvalue/1000);
		
		//Car fährt auf Straße schneller und sonst langsamer
		if(theobject.isCar) 
		{
			try
			{
				CWorldObject onRoad = isOnRoad(200);
				
				if(onRoad!=null)
					speed*=10;
				else
					speed/=2;
			}
			catch(Exception e)
			{
				if(town.bDevMode)
					e.printStackTrace();
			}
		}
				
		//Gibt es einen Weg der abgelaufen werden muss?
		if(Math.abs(ziel_x - (int)bodyx) > speed + CWorld.nodesize || Math.abs(ziel_y - (int)bodyy) > speed + CWorld.nodesize)
	    {
			temp_ziel_x = ziel_x;
			temp_ziel_y = ziel_y;
			
	    	float speedx=0; // = CHelper.getFPSValue(3000.0f);
	    	float speedy=0;
	    	
	        if(temp_ziel_x > (int)bodyx)
	        {
	        	speedx = speed;
	        }
	        else if(temp_ziel_x < (int)bodyx)
	        {
	        	speedx = speed * -1;
	        }
	        
	        if(temp_ziel_y > (int)bodyy)
	        {
	        	speedy = speed;
	        }
	        else if(temp_ziel_y < (int)bodyy)
	        {
	        	speedy = speed * -1;
	        }
	        
	        //if(Math.abs(ziel_x - (int)bodyx) > 10 || Math.abs(ziel_y - (int)bodyy) > 10)
	        {
	        	frotation = (float) Math.atan2(((float)temp_ziel_y - (float)bodyy), ((float)((float)temp_ziel_x - ((float)bodyx))));
	        	frotation = MathUtils.radiansToDegrees*frotation;
	        	frotation = frotation+90;
	        	setRotation(frotation);
	        }
	        
	        //if(box2dBody!=null)
	        {
				//	        	box2dBody.setTransform(box2dBody.getPosition().x+speedx, box2dBody.getPosition().y+speedy, frotation/57); // check
				//	        	int pos_x1 = (int)(box2dBody.getPosition().x/gameWorld.b2dvaluePos)-theobject.objectAnimation.getKeyFrame(stateTime, true).getRegionWidth();
				//	        	int pos_y1 = (int)(box2dBody.getPosition().y/gameWorld.b2dvaluePos)-theobject.objectAnimation.getKeyFrame(stateTime, true).getRegionHeight();
				//	        	setPosition(pos_x1, pos_y1);
	        	//setPosition((int)((box2dBody.getPosition().x+speedx)/gameWorld.b2dvaluePos)-theobject.objectAnimation.getKeyFrame(stateTime, true).getRegionWidth()/2, (int)((box2dBody.getPosition().y+speedy)/gameWorld.b2dvaluePos)-theobject.objectAnimation.getKeyFrame(stateTime, true).getRegionHeight()/2);
	        	
	        	//int x = (int)((box2dBody.getPosition().x+speedx)/gameWorld.b2dvaluePos)-(int)(currentFrame.getRegionWidth()/2);
	        	//int y = (int)((box2dBody.getPosition().y+speedy)/gameWorld.b2dvaluePos)-(int)(currentFrame.getRegionHeight()/2);
	        	int x = (int) (pos_x()+speedx);// (int)((box2dBody.getPosition().x+speedx)/gameWorld.b2dvaluePos)-(int)(currentFrame.getRegionWidth()/2);
	        	int y = (int) (pos_y()+speedy);//(int)((box2dBody.getPosition().y+speedy)/gameWorld.b2dvaluePos)-(int)(currentFrame.getRegionHeight()/2);

	        	setPosition(x, y);
	        }
	    }
		else
		{
			ziel_x=-1;
			ziel_y=-1;
		}
	}
	public CWorldObject isOnRoad(int mov)
	{
		return theobject.isOnRoad(mov, pos_x()+width/2, pos_y()+height/2);
	}
	public CWorldObject isOnDefense(int mov)
	{
		return theobject.isOnDefense(mov, pos_x()+width/2, pos_y()+height/2);
	}	
	public CWorldObject isOnFootpath(int mov, Boolean bDebug)
	{
		return theobject.isOnFootpath(mov, pos_x()+width_human()/2, pos_y()+height_human()/2, bDebug);
	}
	public Boolean pathFinding(float stateTime)
	{
		//if(1==1)
		//	return false;
		
		pathfindingTimer+=CHelper.getDeltaSeconds(town);
		
		if(theobject.editoraction.contains("bird"))
		{
			pathFinding_RealBird(stateTime);
			return true;
		}
		
		int iret = goByCar(); //Erster Aufruf / 2. Aufruf ist am Ende der Funktion, damit es vor einer Action das Ergebnis bekommt
		if(iret==0)
			return false;
		if(iret==1)
			return true;
		
		if(bIsDead)
			return false;
		
		if(bJumpOut && !theobject.isCar)
		{
			if(ziel_x<0 && ziel_y<0)
				walkAround_Bird();
			
			pathFinding_Bird(stateTime);
			
			if(ziel_x<0 && ziel_y<0)
				bJumpOut=false;
			
			return true;
		}
		
		if(ziel_x<0 || ziel_y<0)
			return false;
		
		int pathnodesize = CWorld.nodesize;
		if(theobject.isCar)
		{
			pathnodesize = town.roadrastersize;
			if(bCarOnFootpath)
				pathnodesize=town.footpathsize;
			
			//pathnodesize = town.footpathsize;
		}
		else
		{
			if(path!=null && path.size>0 && path.get(0)!=-99)
				pathnodesize = town.footpathsize;
		}
		
		//Kraftstoff verbrauchen
		if(theobject.isCar)
		{
			if(deltaSecond>0.8f)
			{
				//fuelValue-=0.01f*town.delta_fuelconsumption*theobject.getDeltaFuelConsumption();
				fuelValue-=0.03f*town.delta_fuelconsumption*theobject.getDeltaFuelConsumption();
				
				if(fuelValue<0)
					fuelValue=0;
			}
		}
		
		if(town.gameWorld.town.bDebugLogging)
			Gdx.app.debug("pathFinding", "1");
		
		TextureRegion currentFrame = theobject.textureRegion[0];
		TextureRegion currentFrame2=null;
		if(theobject.objectAnimation!=null)
			currentFrame = theobject.objectAnimation.getKeyFrame(stateTime, true);
		
		//float bodyx = box2dBody.getPosition().x/gameWorld.b2dvaluePos-currentFrame.getRegionWidth()/2;
		//float bodyy = box2dBody.getPosition().y/gameWorld.b2dvaluePos-currentFrame.getRegionHeight()/2;
		float bodyx = pos_x(); //box2dBody.getPosition().x/gameWorld.b2dvaluePos-currentFrame.getRegionWidth()/2;
		float bodyy = pos_y(); //box2dBody.getPosition().y/gameWorld.b2dvaluePos-currentFrame.getRegionHeight()/2;
		
		//if(!theobject.editoraction.contains("garbagetruck"))
		{
			bodyx+=width/2/getHeadSizeByAge();
			bodyy+=height/2/getHeadSizeByAge();
			bodyx-=pathnodesize/2;
			bodyy-=pathnodesize/2;
		}
		
		float fspeedvalue=1;
		if(town.gameGui!=null)
			fspeedvalue = CHelper.getSpeedControllerValue(town.gameGui);     		
		
		//float speed = CHelper.getFPSValue(2f+fspeedvalue/1000);
		float speed = CHelper.getFPSValue(2f+fspeedvalue/270);
		speed*=town.movementSpeedDelta;
		
		CWorldObject onRoad=null;
		
		//Car fährt auf Straße schneller und sonst langsamer
		if(theobject.isCar)
		{
			try
			{
				onRoad = isOnRoad(200);
				if(town.bConstructionMode && onRoad!=null && !onRoad.bObjectIsReady)
					onRoad=null;
				
				float road_speed=1;
				if(onRoad!=null && onRoad.theobject.ATTR_ROAD_SPEED>0)
					road_speed=onRoad.theobject.ATTR_ROAD_SPEED;
				
				if(onRoad!=null)
					speed*=((theobject.getVehicleSpeed_Road()+road_speed)/2); //11
				else
					speed*=theobject.getVehicleSpeed_Terrain(); //3
			}
			catch(Exception e)
			{
				if(town.bDevMode)
					e.printStackTrace();
			}
		}
		else
		{
			try
			{
				Boolean bdebug=false;
				//if(uniqueId==2)
				//	bdebug=true;
				
				//*******
				//Fitness
				//*******
				float fitness = thehuman.getFitnessValue()/35;
				float energy = thehuman.energyValue/85;
				
				speed+=fitness;
				speed*=(energy);
								
				//********
				//Footpath
				//********
				CWorldObject onFootpath = isOnFootpath(50, true);
				if(town.bConstructionMode && onFootpath!=null && !onFootpath.bObjectIsReady)
					onFootpath=null;

				float road_speed=2.4f;
				
				if(onFootpath!=null && onFootpath.theobject.ATTR_ROAD_SPEED>0)
					road_speed=onFootpath.theobject.ATTR_ROAD_SPEED;
				
				if(onFootpath!=null)
					speed*=road_speed;
				else
				{
					if(zombieRun==1)
						speed*=1.3f;
					else
						speed/=1.7f;
					
					//achtung x4 -> geht auf 50 hoch
//					//Zucken verhindern
//					if(speed>14)
//						speed=14;
				}

				//if(thehuman.getName().contains("Betty"))
				//	Gdx.app.debug("speeddebug1", "speed: " + speed);
				
				//achtung x4 -> geht auf 50 hoch
				//Zucken verhindern
				//if(speed>14)
				//	speed=14;				

				//if(thehuman.getName().contains("Betty"))
				//	Gdx.app.debug("speeddebug2", "speed: " + speed);
					
				
				//Gdx.app.debug("1", "speed: "+ speed);
				speed*=fBremse;
				fBremse=1; //Reset
				//Gdx.app.debug("2", "speed: "+ speed);
								
				
				if(speed<1) //mindestens 1 damit sich resident fortbewegen kann und nicht auf der stelle steht
					speed=1;
				
				if(iZombie>=1)
				{
					CWorldObject defense1 = isOnDefense(50);
					if(defense1!=null && defense1.objectCondition>0 && defense1.iObjectIsReady==100)
					{
						speed=0;
						
						defenseDestroyTimer+=CHelper.getDeltaSeconds(town);
						if(defenseDestroyTimer>200)
						{
							defenseDestroyTimer=0;
							//defense1.objectCondition-=1;
							//defense1.objectCondition-=3;
							defense1.objectCondition-=2;
						}
					}
				}
			}
			catch(Exception e)
			{
				if(town.bDevMode)
					e.printStackTrace();
				
				CHelper.writeError(e.getMessage(), e.getStackTrace(), e);
			}
		}
		
		//Fehler: Wenn Speedvalue zu hoch, dann kommt resident nicht am zielort an und bleibt einfach stehen ->  kommt nicht mehr in die funktion rein
		float speedval=speed;
		if(thehuman!=null)
		{
			if(speedval>7)
				speedval=7;
		}
		//----------------------------	
				
		//Gibt es einen Weg der abgelaufen werden muss?
		if(Math.abs(ziel_x - (int)bodyx) > speedval+pathnodesize || Math.abs(ziel_y - (int)bodyy) > speedval+pathnodesize)
		//if(Math.abs(ziel_x - (int)bodyx) > gameWorld.nodesize || Math.abs(ziel_y - (int)bodyy) > gameWorld.nodesize)
	    {
			if(town.gameWorld.town.bDebugLogging)
			{
				Gdx.app.debug("pathFinding", "2");
				
				if(path == null)
					Gdx.app.debug("path is null", "");
				else 
					Gdx.app.debug("path", ""+path.size);
			}
			
			//Am letzten footpath-wegpunkt angekommen -> per dynamicWalk weiter zum Ziel
			{
				if(path==null || path.size<2)
				{
					if(ziel_x>-1)
					{
						//Gdx.app.debug("", "test_1: fast am ziel");
						path = new IntArray(10);
						path.add(-99);
						path.add(2);
						path.add(3);
						temp_ziel_x=ziel_x;
						temp_ziel_y=ziel_y;
					}
				}
			}
			
			//Neues Ziel
			if(ziel_x!=ziel_x_changed || ziel_y != ziel_y_changed)
			{
				ziel_x_changed=ziel_x;
				ziel_y_changed=ziel_y;
				
				Boolean bDynamicWalk=true;
				
				if(theobject.isCar)
				{
					bCarOnFootpath=false;
					pathnodesize = town.roadrastersize;
					getRoadPath(bodyx, bodyy, CWorld.astar_road, town.gameWorld.worldRoad, town.roadsize);
					
					if(path==null || path.size<4)
					{
						getRoadPath(bodyx, bodyy, CWorld.astar_footpath, town.gameWorld.worldFootpath, town.footpathsize);
						if(path!=null && path.size>3)
						{
							pathnodesize = town.footpathsize;
							bCarOnFootpath=true;
						}
					}
					
					if(path!=null && path.size>0)
						bDynamicWalk = false;
				}
				else //Resident
				{
					int dist = CHelper.getEuclidianDistance((int)bodyx, (int)bodyy, ziel_x, ziel_y);
					if(dist>2000)
					{
						CAddress startadr = town.gameWorld.getAddressByPoint((int)bodyx, (int)bodyy);
						CAddress zieladr = town.gameWorld.getAddressByPoint(ziel_x, ziel_y);
						
						//if((startadr!=null || zieladr!=null) && iZombie<1)
						if(iZombie<1)
						{
							//&& startadr.addressId!=zieladr.addressId && iZombie<1
							if((startadr!=null && zieladr!=null) && startadr.addressId!=zieladr.addressId)
							{
								//Ermittle nächstes footpath-tile zum resident
								int dist1=1000000000;
								CWorldObject start=null;
								for(CWorldObject wobj : town.gameWorld.worldFootpath)
								{
									int distTemp = CHelper.getEuclidianDistance((int)bodyx, (int)bodyy, wobj.pos_x(), wobj.pos_y());
									if(distTemp<dist1)
									{
										dist1=distTemp;
										start=wobj;
									}
								}
								
								//Ermittle nächstes footpath-tile zum target
								int dist2=1000000000;
								CWorldObject ziel=null;
								for(CWorldObject wobj : town.gameWorld.worldFootpath)
								{
									int distTemp = CHelper.getEuclidianDistance((int)ziel_x, (int)ziel_y, wobj.pos_x(), wobj.pos_y());
									if(distTemp<dist2)
									{
										dist2=distTemp;
										ziel=wobj;
									}
								}
								
								//if(dist1<dist/2 && dist2<dist/2 && start!=null && ziel!=null)
								if(dist1<dist/2 && dist2<dist && start!=null && ziel!=null)
								{
									//Trace Footpath Pathmap
									if(town.bDebugTrace_FootpathPathmap)
									{
										String fields="";
										int i=0;
										for(CWorldObject b1 : CWorld.pathmap_footpath)
										{
											i++;
											if(b1!=null)
												fields+=", "+ i + ": " + b1;
										}
										
										//Gdx.app.debug("Footpath Pathmap", ""+fields);
									}
									
									int sx = (start.pos_x()+start.width/2)/town.footpathsize;
									int sy = (start.pos_y()+start.height/2)/town.footpathsize;
									int tx = (ziel.pos_x()+ziel.width/2)/town.footpathsize;
									int ty = (ziel.pos_y()+ziel.height/2)/town.footpathsize;
									
									//ermittle weg vom start zum ziel
									path = CWorld.astar_footpath.getPath(sx, sy, tx, ty, false, targetPathObject, this);
									
									if(path!=null && path.size>0)
									{
										pathnodesize = town.footpathsize;
										bDynamicWalk=false;
									}
								}
							}
						}
					}
					else
					{
						//..
					}
				}				
				
				if(bDynamicWalk)
				{
					//Pathfinding auf Adresse
//					CAddress sadr = gameWorld.getAddressByPoint(pos_x(), pos_y());
//					CAddress tadr = gameWorld.getAddressByPoint(ziel_x, ziel_y);
//					
//					if(sadr!=null && tadr!=null && sadr.addressId==tadr.addressId && targetPathObject!=null)
//					{
//						path = sadr.astar.getPath((int)((pos_x()-sadr.sx)/town.floorrastersize), (int)((pos_y()-sadr.sy)/town.floorrastersize), (int)((ziel_x-sadr.sx)/town.floorrastersize), (int)((ziel_y-sadr.sy)/town.floorrastersize), false, targetPathObject, this);
//						//Gdx.app.debug("", ""+uniqueId + ", " +path);
//						
//						if(path!=null && path.size>0)
//						{
//							//Gdx.app.debug("", "test_1");
//							pathnodesize = town.floorrastersize;
//							bDynamicWalk=false;
//						}
//					}
					
					if(bDynamicWalk)
					{
						path = new IntArray(10);
						path.add(-99);
						path.add(2);
						path.add(3);
						temp_ziel_x=ziel_x;
						temp_ziel_y=ziel_y;
					}
				}
				
				if((path!=null && path.size>0 && path.get(0)!=-99))
				{
					if(path==null)
					{
						bJumpOut=true;
						return true;
					}
					
					//cfm111
					//pathnodesize=town.footpathsize;
					
					if(path!=null && path.size>1)
					{
						temp_ziel_y = path.pop()*pathnodesize;
						if(path.size>0)
							temp_ziel_x = path.pop()*pathnodesize;
						
						//Gdx.app.debug("", "temp_ziel_x: " + temp_ziel_x + ", temp_ziel_y: " + temp_ziel_y + ", path: " + path + ", pathnodesize: " + pathnodesize);
					}
				}
			}
			
			
			Boolean bnew=false;
			
			if((path!=null && path.size>0) || temp_ziel_x>-1)
			{
				if((path!=null && path.size>0 && path.get(0)!=-99))
				{
	     		try{
	     			if(Math.abs(temp_ziel_x - (int)bodyx) < speed*2f && Math.abs(temp_ziel_y - (int)bodyy) < speed*2f)
					{
						if(path.size>1)
						{
							//cfm111
							//pathnodesize=town.footpathsize;

							temp_ziel_y = path.pop()*pathnodesize;
							
							if(path.size>0)
								temp_ziel_x = path.pop()*pathnodesize;
							
							//							if(theobject.isCar) //rechts fahren
							//							{
							//								Vector2 v2 = CHelper.moveVectorByRotationS2D(temp_ziel_x, temp_ziel_y, -40, 0, width/2, height/2, rotation());
							//								temp_ziel_x=(int)v2.x-width/2;
							//								temp_ziel_y=(int)v2.y-height/2;
							//								//Gdx.app.debug("", "x: "+pos_x() + ", y: " + pos_y() + ", temp_ziel_x: " + temp_ziel_x + ", temp_ziel_y: " + temp_ziel_y);
							//							}
							
							bnew=true;
						}
					}
	     		}
	     		catch(Exception e){}
				}
				else
				{
					//..
				}
				
		        rotationTime+=CHelper.getDeltaSeconds(town);
		        float temprotation=0;
	        	temprotation = (float) Math.atan2((float)temp_ziel_y - (float)bodyy, (float)(temp_ziel_x - (bodyx)));
	        	temprotation = MathUtils.radiansToDegrees*temprotation;
	        	temprotation = temprotation+90;
	        	
	        	if(temprotation<0)
	        		temprotation=360+temprotation;
	        	if(temprotation>360)
	        		temprotation=360-temprotation;
	        	
				//if(theobject.isCar && bnew)
				//{
					//Betrunken fahren, drunken driving
					//Vector2 v2 = CHelper.moveVectorByRotationS2D(temp_ziel_x, temp_ziel_y, 20, 0, width/2, height/2, temprotation);
					//temp_ziel_x=(int)v2.x-width/2;
					//temp_ziel_y=(int)v2.y-height/2;
					
					//Vector2 v2 = CHelper.moveVectorByRotationS2D(temp_ziel_x, temp_ziel_y, 3, 0, town.roadsize/2, town.roadsize/2, temprotation);
					//temp_ziel_x=(int)v2.x-width/2;
					//temp_ziel_y=(int)v2.y-height/2;
				//}
	        	
		    	float speedx=0; // = CHelper.getFPSValue(3000.0f);
		    	float speedy=0;
		    	
		        if(temp_ziel_x > (int)bodyx)
		        {
		        	speedx = speed;
		        }
		        else if(temp_ziel_x < (int)bodyx)
		        {
		        	speedx = speed*-1;
		        }
		        
		        if(temp_ziel_y > (int)bodyy)
		        {
		        	speedy = speed;
		        }
		        else if(temp_ziel_y < (int)bodyy)
		        {
		        	speedy = speed * -1;
		        }	
		        
		        if(Math.abs(ziel_x - (int)bodyx) > 10 || Math.abs(ziel_y - (int)bodyy) > 10)
		        {
//		        	if(theobject.isCar)
//		        	{
//			        	setRotation(temprotation);
//			        	temp_ziel_rot_x=temp_ziel_x;
//			        	temp_ziel_rot_y=temp_ziel_y;
//		        	}
//		        	else
		        	{
		        		//if(thehuman!=null)
		        		{
					        if(temp_ziel_rot_x!=temp_ziel_x || temp_ziel_rot_y!=temp_ziel_y) //rotation nur bei neuem wegpunkt anpassen
					        {
					        	if(temprotation!=rotation())
					        	{
						        	setRotation(temprotation);
						        	temp_ziel_rot_x=temp_ziel_x;
						        	temp_ziel_rot_y=temp_ziel_y;
					        	}
					        }
					        else
					        {
					        	if(!theobject.isCar || (theobject.isCar && onRoad==null))  //auskommentiert da cars sonst schräg fahren ohne road
					        		//car: bei langsamem speed und dynamicwalk wird rotation nicht richtig gesetzt, daher muss hier nachjustiert werden
					        	//nachtrag: für human erstmal bedingung auskommentiert, da rotation beim sarg-schieben nicht gepasst hat
					        	{
					        		//if((path!=null && path.size>0 && path.get(0)==-99))
					        		{
							        	if(temprotation!=rotation())
							        	{
							        		//Gdx.app.debug("", "set rot");
								        	setRotation(temprotation);
								        	temp_ziel_rot_x=temp_ziel_x;
								        	temp_ziel_rot_y=temp_ziel_y;
							        	}
					        		}
					        	}
					        }
		        		}
		        	}
		        	
//		        	if(thehuman==null)
//		        	{
//			        	//if(rotationTime>0.01f)
//			        	{
//				        	float frot=0;
//				        	float tfrot=8f;
//				        	
//				        	if(Math.abs(temprotation-rotation())<tfrot)
//				        		tfrot=Math.abs(temprotation-rotation());
//				        	
//				        	if(Math.abs(temprotation-rotation())>300)
//				        		tfrot=Math.abs(temprotation-rotation());
//				        					        	
//				        	//Gdx.app.debug("", ""+rotation() + " - " + temprotation);
//				        	
//				        	if(temprotation>rotation())
//				        		frot=tfrot;
//				        	else if(temprotation<rotation())
//				        		frot=tfrot*-1;
//				        	
//				        	if(frot!=0)
//				        		setRotation(rotation()+frot);
//				        	
//				        	if(Math.abs((int)rotation()-(int)temprotation)<10)
//				        	{
//				        		temp_ziel_rot_x=temp_ziel_x;
//				        		temp_ziel_rot_y=temp_ziel_y;
//				        	}
//			        	}
//		        	}
		        }
		        
		        float ftimer=0.05f;
		        if(thehuman!=null && (thehuman.bIsCold || thehuman.bIsDark))
		        	ftimer=0.7f;
		        	//ftimer=1.2f;
		        	
		        if(zombieRun==1)
		        	ftimer=0;
		        
				if(pathfindingTimer>ftimer)
		        //if(Math.abs((int)rotation()-(int)temprotation)<100)
		        {
					pathfindingTimer=0;
					int x = (int) (pos_x()+speedx); // (int)((box2dBody.getPosition().x+speedx)/gameWorld.b2dvaluePos)-currentFrame.getRegionWidth()/2;
					int y = (int) (pos_y()+speedy); // (int)((box2dBody.getPosition().y+speedy)/gameWorld.b2dvaluePos)-currentFrame.getRegionHeight()/2;
		        	setPosition(x, y);
		        }
		        
		        if(rotationTime>10)
		        	rotationTime=0;
		        
		        goByCar();
		        return true;
			}
			else
			{
				resetPathFinding();
			}
	    }
		else
	    {
			resetPathFinding();
	    }
		
		goByCar();
		
		return false;
	}
	public void getRoadPath(float bodyx, float bodyy, CAStar astar1, List<CWorldObject> roads, int roadsize)
	{
		int dist = CHelper.getEuclidianDistance((int)bodyx, (int)bodyy, ziel_x, ziel_y);

		{
			//Ermittle nächstgelegene Road zum Fahrzeug
			int dist1=1000000000;
			CWorldObject start=null;
			for(CWorldObject wobj : roads)
			{
				int distTemp = CHelper.getEuclidianDistance((int)bodyx, (int)bodyy, wobj.pos_x(), wobj.pos_y());
				if(distTemp<dist1)
				{
					dist1=distTemp;
					start=wobj;
				}
			}
			
			//Ermittle nächste Road zum Ziel
			int dist2=1000000000;
			CWorldObject ziel=null;
			for(CWorldObject wobj : roads)
			{
				int distTemp = CHelper.getEuclidianDistance((int)ziel_x, (int)ziel_y, wobj.pos_x(), wobj.pos_y());
				if(distTemp<dist2)
				{
					dist2=distTemp;
					ziel=wobj;
				}
			}
			
			if(dist1<dist && start!=null && ziel!=null)
			{
				//Trace Footpath Pathmap
				//if(town.bDebugTrace_FootpathPathmap)
				//{
				//	String fields="";
				//	int i=0;
				//	for(Boolean b1 : CWorld.pathmap_footpath)
				//	{
				//		i++;
				//		if(b1)
				//			fields+=", "+ i + ": " + b1;
				//	}
				//	Gdx.app.debug("Footpath Pathmap", ""+fields);
				//}
				
				int sx = (start.pos_x()+start.width/2)/roadsize;
				int sy = (start.pos_y()+start.height/2)/roadsize;
				int tx = (ziel.pos_x()+ziel.width/2)/roadsize;
				int ty = (ziel.pos_y()+ziel.height/2)/roadsize;
				//int index1 = sx+sy*pathmapsize;
				
				//ermittle weg vom start zum ziel
				path = astar1.getPath(sx, sy, tx, ty, false, targetPathObject, this);
			}
			
		}
		
	}
	//PATHFINDING<-------------------------------------
	
	
	//COLLISION DETECTION----------------------------->
	public Boolean collides(CWorldObject obj)
	{
		return Intersector.overlapConvexPolygons(getBoundingPolygon(IntersectionMode.COLLISION), obj.getBoundingPolygon(IntersectionMode.COLLISION));
		
		//		if(obj!=null && obj.testpoint(pos_x()+width, pos_y()+height) || obj.testpoint(pos_x(), pos_y()) || obj.testpoint(pos_x()+width, pos_y()) || obj.testpoint(pos_x(), pos_y()+height))
		//			return true;
		//		else return false;
	}
	public Boolean collides(CWorldObject obj, int distance)
	{
		return Intersector.overlapConvexPolygons(
				getBoundingPolygon(distance)
				, obj.getBoundingPolygon(distance)
			);
	}
	public Boolean testpoint(float x, float y, IntersectionMode imode)
	{
		Vector2 v2 = new Vector2();
		v2.x=x;
		v2.y=y;
		float[] verts = getBoundingPolygon(imode).getTransformedVertices();
		return Intersector.isPointInPolygon(verts, 0, verts.length, x, y);
	}
	public int checkOverlap(OverlapType type)
	{
		int value=0;
		
		if(type==OverlapType.DECOR)
		{
			if(theaddress!=null)
			{
				for(CWorldObject wobj : theaddress.listWorldObjects)
				{
					if(wobj.theobject.editoraction.contains("_carpet") || wobj.theobject.editoraction.contains("_plant"))
					{
						if(Intersector.overlapConvexPolygons(theobject.getBoundingPolygon(false, IntersectionMode.COLLISION), wobj.theobject.getInteriorDecorZonePolygon()))
						{
							value+=1;
						}
					}
				}
			}
			
			if(value>10)
				value=10;
		}
		
		return value;
	}
	public Boolean containsObject(CWorldObject wobj)
	{
		float pVertices[] = wobj.getBoundingPolygon(0).getTransformedVertices();
		
		for(int i=0;i<pVertices.length;i+=2)
		{
			if(testpoint(pVertices[i], pVertices[i+1], IntersectionMode.DEFAULT))
			{
				pVertices[i]=-1;
			}
		}
		
		for(int i=0;i<pVertices.length;i+=2)
		{
			if(pVertices[i]!=-1)
				return false;
		}
		
		return true;
	}
	
	//COLLISION DETECTION<-----------------------------
	public void setDynamicSize(int type)
	{
		//Gdx.app.debug("", ""+theobject.editoraction);
		if(type==0)
		{
			if(dynamicwidth>0)
			{
				width=dynamicwidth;
				theobject.width=dynamicwidth;
			}
			
			if(dynamicheight>0)
			{
				height=dynamicheight;
				theobject.height=dynamicheight;
			}
		}
		
		if(type==1)
		{
			if(width>0)
				dynamicwidth=width;
			
			if(height>0)
				dynamicheight=height;
		}
	}
	public void setTempSize(int type)
	{
		if(type==0) //reset default size
		{
			width=tempwidth;
			height=tempheight;
			tempwidth=0;
			tempheight=0;
		}
		
		if(type==1) //set big size for placing/moving
		{
			tempwidth=width;
			tempheight=height;
			
			//floor/garage/road/footpath
			if(thehuman!=null 
					|| theobject.isRoomObject 
					|| theobject.editoraction.contains("garage") 
					|| theobject.editoraction.contains("parkingspace")
					|| theobject.editoraction.contains("road_road_road") 
					|| theobject.editoraction.contains("footpath")
				)
			{
				width*=1f;
				height*=1f;
			}
			else
			{
				//width*=1.1f;
				//height*=1.1f;
			}
		}
	}
	//PATHFINDING<-------------------------------------

	
	//Object Type----------------------------->
	public void changeAddress(CAddress newAddress)
	{
		if(theaddress!=null)
		{
			theaddress.removeWorldObject(this);
			//theaddress.listWorldObjects.removeIf(item->item.uniqueId==uniqueId);
		}
		
		theaddress=newAddress;
		theaddress.addWorldObject(this);
		//newAddress.listWorldObjects.add(this);
	}
	public void removeResidentialTasks()
	{
		ArrayList<CWorldObject> removetasks = new ArrayList<CWorldObject>(); 
		for(CWorldObject tobj : thehuman.taskobjects.values())
		{
			if(tobj.isResidentialTaskObject())
				removetasks.add(tobj);
		}
		for(CWorldObject robj : removetasks)
		{
			if(robj.worker!=null && robj.worker.uniqueId==uniqueId)
				robj.worker=null;
			if(robj.worker2!=null && robj.worker2.uniqueId==uniqueId)
				robj.worker2=null;
			thehuman.taskobjects.remove(robj.uniqueId);
		}		
	}
	public Boolean isWorkerAtWorkplace()
	{
		if(isWorkerActive() && worker.activeAction!=null && worker.activeAction.bActionMode)
			return true;
		
		return false;
	}
	
	public Boolean isWorkerActive()
	{
		if(worker!=null && worker.thehuman.canWork())
		{
			Boolean time1 = CTime.isHourBetweenSchedule(workTime1_From, workTime1_To, town.gameWorld.worldTime.hours);
			Boolean time2 = CTime.isHourBetweenSchedule(workTime2_From, workTime2_To, town.gameWorld.worldTime.hours);
			
			if(time1 || time2)
				return true;
		}
		
		return false;
	}
	
	public Boolean isWorkerActive(int addHour)
	{
		if(worker!=null && worker.thehuman.canWork())
		{
			//addHour=-1;
			int hours = town.gameWorld.worldTime.hours;
			
			//if(hours>23)
			//	hours=0;
			//if(hours<0)
			//	hours=23;

			//Gdx.app.debug("", "workTime1_From: " + workTime1_From + ", workTime1_To: " + workTime1_To + ", town.gameWorld.worldTime.hours: " + town.gameWorld.worldTime.hours);
			
			Boolean time1 = CTime.isHourBetweenSchedule(workTime1_From, workTime1_To, hours);
			Boolean time2 = CTime.isHourBetweenSchedule(workTime2_From, workTime2_To, hours);
			
			//Gdx.app.debug("", "workTime2_From: " + workTime2_From + ", workTime2_To: " + workTime2_To + ", town.gameWorld.worldTime.hours: " + town.gameWorld.worldTime.hours);
			//Gdx.app.debug("", "time1: "+time1 + ", time2: " + time2);
						
			
			if(time1 || time2)
				return true;
		}
		
		return false;
	}	
	
	public Boolean isUnemployedHuman()
	{
		if(thehuman==null)
			return false;
		
		if(thehuman.workplaces.size()==0)
			return true;
				
		
		return false;
	}
	public Boolean isHuman()
	{
		//try
		//{
		if(theobject==null)
			return false;
		
			return theobject.isHuman();
		//}
		//catch(Exception ex)
		//{
		//	return false;
		//}
	}
	public Boolean isAddressObject()
	{
		return theobject.isAddressObject();
	}
	public Boolean isHouseObject()
	{
		return theobject.isHouseObject();
	}
	public Boolean isActiveByEnergyConsumption()
	{
		if(getEnergyConsumption()>0 && !town.gameWorld.getEnoughEnergy())
			return false;
		
		return true;
	}
	public Boolean isActiveByWaterConsumption()
	{
		if(theobject.getWaterConsumption()>0) { //&& !town.gameWorld.getEnoughWater())
			for(CWorldObject wobj : town.gameWorld.worldWatersystems) {
				if(!wobj.bObjectIsReady || !wobj.isActiveByEnergyConsumption()) {
					continue;
				}
				
				int dist = CHelper.getEuclidianDistance(this, wobj);
				dist = dist - wobj.getWatersystemRange();
				
				if(dist<1) {
					return true;
				}
			}
		}
		else {
			return true;
		}
		
		return false;
	}	
	public int getEnergyOutput()
	{
		if(theobject.getEnergyOutput()>0)
		{
			if(onlineByWorkInput)
			{
				float op = theobject.getEnergyOutput();
				
				if(defaultObjectCondition>0 && objectCondition<90)
				{
					return Math.round((op/100f)*(float)objectCondition);
					
					//					if(objectCondition<50)
					//						op=Math.round(op/1.5f);
					//					if(objectCondition<30)
					//						op=Math.round(op/2f);
					//					if(objectCondition<10)
					//						op=Math.round(op/3f);
					//					if(objectCondition<1)
					//						op=0;
				}
				
				return (int) op;
			}
		}
		
		return 0;
	}
	public int getWaterOutput()
	{
		if(!isActiveByEnergyConsumption())
			return 0;
		
		if(theobject.getWaterOutput()>0)
		{
			if(onlineByWorkInput)
			{
				int op = theobject.getWaterOutput();
				
				if(defaultObjectCondition>0 && objectCondition<90)
				{
					return Math.round((op/100f)*(float)objectCondition);
					
					//					if(objectCondition<50)
					//						op=Math.round(op/1.5f);
					//					if(objectCondition<30)
					//						op=Math.round(op/2f);
					//					if(objectCondition<10)
					//						op=Math.round(op/3f);
					//					if(objectCondition<1)
					//						op=0;
				}
				
				return op;

			}
		}
		
		return 0;
	}	
	public int getEnergyConsumption()
	{
		//return energyConsumption;
		return theobject.getEnergyConsumption();
	}
	public int getWaterConsumption()
	{
		//return waterConsumption;
		return theobject.getWaterConsumption();
	}
	
	public Boolean showAsCompanyObject()
	{
		if(theobject.editoraction.contains("illuminati_defense"))
			return true;
		
		return false;
	}
	
	public int getMinAge()
	{
		return theobject.getMinAge();
		
//		if(isCompanyWorkingPlace() || isCompanyTaskObject() || theobject.editoraction.contains("illuminati"))
//			return 18;
//		else if(isResidentialTaskObject())
//			return 14;
		
//		int iMinAge=0;
//		if(isCompanyObject())
//			iMinAge=18;
//		if(theaddress!=null && theaddress.addressType.contains("residential"))
//			iMinAge=14;
//		if(theobject.editoraction.toLowerCase().contains("illuminati"))
//			iMinAge=18;
		
	//	return 0;
	}
	
	public Boolean isCompanyObject()
	{
		if(theobject.editoraction.contains("company_"))
			return true;
		
		return false;
	}
	
	public Boolean isCompanyOfficeWorkingPlace()
	{
		if(theobject.editoraction.contains("workingplace") && theobject.editoraction.contains("company"))
			return true;
		
		return false;
	}
	
	public Boolean isCompanyWorkingPlace()
	{
		return theobject.isCompanyWorkingPlace();
	}
	
	public Boolean isCompanyObjectFillingByWorkerObject()
	{
		return theobject.isCompanyObjectFillingByWorkerObject();
	}
	
	public Boolean isMaintenanceObject()
	{
		//Gdx.app.debug("", ""+uniqueId);
		//if(theobject!=null)
		//	Gdx.app.debug("", ""+theobject.editoraction);
		return theobject.isMaintenanceObject();
	}
	
	public int isOwnerObject()
	{
		if(theobject.editoraction.contains("bedroom_bed_double"))
			return 2;
		
		if(theobject.editoraction.contains("bedroom_bed"))
			return 1;
		
		if(theobject.editoraction.contains("traffic_car_residential"))
			return 1;
		
		if(theobject.editoraction.contains("bedroom_wardrobe"))
			return 1;
		
		//if(theobject.editoraction.contains("diningroom_diningtable"))
		//	return 4;
		
		return 0;
	}
	
	public Boolean isFoodFillingByWorkerObject()
	{
		return theobject.isFoodFillingByWorkerObject();
	}
	
	public String getTaskText(int type, int showhours) //For Choose Worker Dialog
	{
		if(theobject.editoraction.contains("laundrybasket"))
		{
			//if(showhours==1)
			//	return "Laundry " + getWorkingHoursString();
			//else
			return "Laundry";
		}
		
		if(theobject.editoraction.contains("company_urbancemetery_hearse_traffic_car"))
			return "Urban Cemetery / Hearse Driver"; // ," + theaddress.addressName;
		
		if(theobject.editoraction.contains("company_urbancemetery_rostrum"))
			return "Urban Cemetery / Funeral Speaker"; // ," + theaddress.addressName;
		
		if(theobject.editoraction.contains("kitchen_fridge"))
			return "Fridge / Supermarket Shopping"; // ," + theaddress.addressName;
		
		if(theobject.editoraction.contains("diningroom_diningtable"))
		{
			String sTimes="";
			if(actiontime1>-1)
				sTimes=CHelper.getAMPMHourText(actiontime1)+", ";
			if(actiontime2>-1)
				sTimes+=CHelper.getAMPMHourText(actiontime2) + ", ";
			if(actiontime3>-1)
				sTimes+=CHelper.getAMPMHourText(actiontime3) + ", ";
			
			if(sTimes.length()>0)
				sTimes=sTimes.substring(0, sTimes.length()-2);
			else
				sTimes=" - ";
			
			sTimes=" " + sTimes;
			
			if(showhours==0)
				sTimes="";
			
			if(type==1)
				return "Cook Dinner" + sTimes + "";
			else
				return "Dinner" + sTimes + "";
		}
		
		return theobject.objectName;
	}
	
	public Boolean isResidentialTaskObject()
	{
		return theobject.isResidentialTaskObject();
		
//		if(theobject.editoraction.contains("laundrybasket"))
//			return true;
//		
//		if(theobject.editoraction.contains("kitchen_fridge"))
//			return true;
//		
//		if(theobject.editoraction.contains("diningroom_diningtable"))
//			return true;
//		
//		return false;
	}
	
	public Boolean isTaskObject()
	{
		if(isResidentialTaskObject())
			return true;
		
		if(isCompanyTaskObject())
			return true;
		
		return false;
	}
	
	public Boolean isCompanyTaskObject()
	{
		return theobject.isCompanyTaskObject();
	}
	
	public String dinnerTableIsOnline(int mode)
	{
		String swarning="";
		
		if(theobject.editoraction.contains("diningroom_diningtable"))
		{
//			Boolean bFridge=false;
//			Boolean bStove=false;
//			Boolean bCupboard=false;
//			Boolean bSink=false;
			
			CAddress adr = theaddress;
//			for(CWorldObject wobj : adr.listWorldObjects)
//			{
//				//Fridge
//				int requiredFood=getOwnerCount()*4;
//				if(wobj.theobject.editoraction.contains("kitchen_fridge") && 
//						wobj.isActiveByEnergyConsumption() && 
//						wobj.getObjectFillingMulti()>=requiredFood)
//					bFridge=true;
//				
//				//Stove
//				if(wobj.theobject.editoraction.contains("kitchen_stove") && 
//						wobj.isActiveByEnergyConsumption())
//					bStove=true;
//				
//				//Cupboard
//				if(wobj.theobject.editoraction.contains("kitchen_cupboard"))
//					bCupboard=true;
//				
//				//Sink
//				if(wobj.theobject.editoraction.contains("kitchen_sink") && 
//						wobj.isActiveByWaterConsumption())
//					bSink=true;
//			}		
			
//			if(!bFridge)
//				swarning+="no fridge or not enough food, ";
//			if(!bStove)
//				swarning+="no stove, ";
//			if(!bCupboard)
//				swarning+="no cupboard, ";
//			if(!bSink)
//				swarning+="no sink, ";
				
//			if(mode==1)
//			{
//				if(owner==null)
//					swarning+="seat owner 1 not chosen, ";
//				if(owner2==null)
//					swarning+="seat owner 2 not chosen, ";
//				if(owner3==null)
//					swarning+="seat owner 3 not chosen, ";
//				if(owner4==null)
//					swarning+="seat owner 4 not chosen, ";
//				
//				if(theobject.editoraction.contains("_count6"))
//				{
//					if(owner5==null)
//						swarning+="seat owner 5 not chosen, ";
//					if(owner6==null)
//						swarning+="seat owner 6 not chosen, ";
//				}
//			}
		}
		
		return swarning;
	}
	
	public CWorldObject getNextDinnertableEvent(int mode)
	{
		
		//*************************************************
		
		//mode < 0		Abwasch
		//mode == 0		Essen
		//mode > 0		Zukünftige Dinner Actions?
		//mode==100		alarm clock für cook oder eater
		
		//*************************************************
		
		CWorldObject targetActionObject=null;
		
		for(CWorldObject to : thehuman.taskobjects.values())
		{
			if(to.theobject.editoraction.contains("diningroom_diningtable"))
			{
				CWorldObject dinnerTable = to;
				if(dinnerTable.worker==null)
					return null;
				
				int ownerNumber=-1;
				if(dinnerTable.owner!=null && dinnerTable.owner.uniqueId==uniqueId)
					ownerNumber=1;
				if(dinnerTable.owner2!=null && dinnerTable.owner2.uniqueId==uniqueId)
					ownerNumber=2;
				if(dinnerTable.owner3!=null && dinnerTable.owner3.uniqueId==uniqueId)
					ownerNumber=3;
				if(dinnerTable.owner4!=null && dinnerTable.owner4.uniqueId==uniqueId)
					ownerNumber=4;
				if(dinnerTable.owner5!=null && dinnerTable.owner5.uniqueId==uniqueId)
					ownerNumber=5;
				if(dinnerTable.owner6!=null && dinnerTable.owner6.uniqueId==uniqueId)
					ownerNumber=6;
				if(dinnerTable.owner7!=null && dinnerTable.owner7.uniqueId==uniqueId)
					ownerNumber=7;
				if(dinnerTable.owner8!=null && dinnerTable.owner8.uniqueId==uniqueId)
					ownerNumber=8;
				
				
				if(mode<0)
				{
					//auch der koch kann abspülen zb wenn nur kinder unter 12 am tisch sitzen
					if(dinnerTable.worker!=null && dinnerTable.worker.uniqueId==uniqueId)
						ownerNumber=7;
				}
				
				if((ownerNumber>0 && to.tempcount==2) || mode==100)
				{
					if(mode==0)
					{
						//ist mein teller voll?
						if(ownerNumber==1 && to.actionvar1<1)
							continue;
						if(ownerNumber==2 && to.actionvar2<1)
							continue;
						if(ownerNumber==3 && to.actionvar3<1)
							continue;
						if(ownerNumber==4 && to.actionvar4<1)
							continue;
						if(ownerNumber==5 && to.actionvar5<1)
							continue;
						if(ownerNumber==6 && to.actionvar6<1)
							continue;
						if(ownerNumber==7 && to.actionvar7<1)
							continue;
						if(ownerNumber==8 && to.actionvar8<1)
							continue;
					}
					
					if(mode==0 || mode==100) //default eat dinner action
					{
						if((to.actiontime1==0 && town.gameWorld.worldTime.hours==23))
						{
							targetActionObject=to;
							break;
						}
						if(to.actiontime2==0 && town.gameWorld.worldTime.hours==23)
						{
							targetActionObject=to;
							break;
						}
						if(to.actiontime3==0 && town.gameWorld.worldTime.hours==23)
						{
							targetActionObject=to;
							break;
						}
												
						if(to.actiontime1>-1 &&(town.gameWorld.worldTime.hours == to.actiontime1-1 && town.gameWorld.worldTime.minutes>15) || (town.gameWorld.worldTime.hours == to.actiontime1))
						{
							targetActionObject=to;
							break;
						}
						if(to.actiontime2>-1 &&(town.gameWorld.worldTime.hours == to.actiontime2-1 && town.gameWorld.worldTime.minutes>45) || (town.gameWorld.worldTime.hours == to.actiontime2))
						{
							targetActionObject=to;
							break;
						}
						if(to.actiontime3>-1 && (town.gameWorld.worldTime.hours == to.actiontime3-1 && town.gameWorld.worldTime.minutes>45) || (town.gameWorld.worldTime.hours == to.actiontime3))
						{
							targetActionObject=to;
							break;
						}
					}
					
					if(mode==100 && dinnerTable.worker!=null && dinnerTable.worker.uniqueId==uniqueId) //alarm clock für cook
					{
						mode=2;
					}
					
					if(mode>0 && mode<99) //prüfe zukunft auf dinner actions
					{
						if(to.actiontime1 > -1 && 
							(to.actiontime1 > town.gameWorld.worldTime.hours) && 
							((to.actiontime1 - town.gameWorld.worldTime.hours)<=mode))
						{
							targetActionObject=to;
							break;
						}
						
						if(to.actiontime2>-1 && (to.actiontime2 > town.gameWorld.worldTime.hours) && ((to.actiontime2 - town.gameWorld.worldTime.hours)<mode))
						{
							targetActionObject=to;
							break;
						}
						
						if(to.actiontime3>-1 && (to.actiontime3 > town.gameWorld.worldTime.hours) && ((to.actiontime3 - town.gameWorld.worldTime.hours)<mode))
						{
							targetActionObject=to;
							break;
						}
					}
					
					
					if(mode<0) //abwasch, wash dishes
					{
						//wenn tempcount==eatmode und alle teller leer gegessen 
						if(to.tempcount==2) //&& to.actionvar1<1 && to.actionvar2<1 && to.actionvar3<1 && to.actionvar4<1 && to.actionvar5<1 && to.actionvar6<1)
						{
							Boolean bRet=false;
							
							int at1=to.actiontime1+1;
							int at2=to.actiontime2+1;
							int at3=to.actiontime3+1;

							
							//alle Teller leer gegessen
							if(to.actionvar1<1 && to.actionvar2<1 && to.actionvar3<1 && to.actionvar4<1 && to.actionvar5<1 && to.actionvar6<1)
								bRet=true;
							
							
							//2 stunden nach dinnerbeginn
							if(to.actiontimenr==1 && to.actiontime1>-1)
							{
								if(town.gameWorld.worldTime.hours>at1)
									bRet=true;
								
								if(at1==24 && town.gameWorld.worldTime.hours<4 && town.gameWorld.worldTime.hours>1)
									bRet=true;
							}
		
							if(to.actiontimenr==2 && to.actiontime2>-1)
							{
								if(town.gameWorld.worldTime.hours>at2)
									bRet=true;
								
								if(at2==24 && town.gameWorld.worldTime.hours<4 && town.gameWorld.worldTime.hours>1)
									bRet=true;
							}

							if(to.actiontimenr==3 && to.actiontime3>-1)
							{
								if(town.gameWorld.worldTime.hours>at3)
									bRet=true;
								
								if(at3==24 && town.gameWorld.worldTime.hours<4 && town.gameWorld.worldTime.hours>1)
									bRet=true;
							}
														
							if(bRet)
							{
								targetActionObject=to;
								break;
							}
						}
					}
				}
			}
		}
		
		if(targetActionObject!=null)
		{
			//mindestens ein platz muss besetzt sein
			if(targetActionObject.owner==null && targetActionObject.owner2==null && targetActionObject.owner3==null && targetActionObject.owner4==null && targetActionObject.owner5==null && targetActionObject.owner6==null && targetActionObject.owner7==null && targetActionObject.owner8==null)
				targetActionObject=null;
		}
		
		return targetActionObject;
	}
	
	//	public CWorldObject getNextSupermarketShelfForFoodFilling()
	//	{
	//		if(thehuman!=null && thehuman.taskobjects.size()>0)
	//		{
	//			for(CWorldObject obj : thehuman.taskobjects.values())
	//			{
	//				if((obj.isCompanyFoodFillingByWorkerObject()) && obj.isTimeForAction() && obj.isOccupiedByExtern==null && obj.isOccupiedBy==null)
	//				{
	//					return obj;
	//				}
	//			}
	//		}
	//		
	//		return null;
	//	}
	
	
	Boolean laundryTaskIsReadyToDo()
	{
		CWorldObject[] taskobjects = getLaundryTaskObject();
		
		
		
		if(taskobjects!=null && taskobjects[0]!=null)
		{
			//Basket -> Washingmachine
			if(taskobjects[1]!=null)
			{
				CWorldObject t2=taskobjects[1];
				if(t2.isActiveByEnergyConsumption() && 
						t2.isActiveByWaterConsumption() 
						&& (t2.isOccupiedBy==null || t2.isOccupiedBy.uniqueId==uniqueId)
						)
				{
					return true;
				}

				
				return false;
			}
			
			
			//Washingmachine -> Dryer
			if(taskobjects[2]!=null)
			{
				CWorldObject t2=taskobjects[2]; //Washingmachine
				CWorldObject t3=taskobjects[3]; //Dryer
				if(t3.isActiveByEnergyConsumption() && 
						t3.isActiveByWaterConsumption() && 
						//targetActionObject3.getObjectFillingMulti()==0 && 
						(t3.isOccupiedBy==null || t3.isOccupiedBy.uniqueId==uniqueId) && 
						t2.getObjectFillingMulti()>0 && 
						t2.actionvar1==1
						)
				{
					return true;
				}
				
				return false;
			}
			
			
			//Dryer -> Basket
			if(taskobjects[4]!=null)
			{
				CWorldObject t2 =taskobjects[4];
				
				if(t2.actionvar1==1)
				{
					return true;
				}
				
				//return false;
			}
									
			//Collect Laundry from floor
			if(taskobjects[5]!=null)
			{
				return true;
			}
			
		}
		
		return false;
	}
	
	
	public CWorldObject[] getLaundryTaskObject()
	{
		//Return:
		//	0: basket 
		//	1: washingmachine / dryer
		
		if(theaddress==null || thehuman==null)
			return null;
		
		CWorldObject retarr[] = {null, null, null, null, null, null};
		
		//Prüfe Taskobjects
		for(CWorldObject basket : thehuman.taskobjects.values())
		{
			//Laundry
			if(basket.theobject.editoraction.contains("laundrybasket"))
			{
				//if(thehuman.getWorktimeIsNow(basket.workTime1_From, basket.workTime1_To) || thehuman.getWorktimeIsNow(basket.workTime2_From, basket.workTime2_To))
				{

					//Zauber
					for(int key1 : basket.objectFillingMulti.keySet())
					{
						Optional<CWorldObject> opt = basket.theaddress.listWorldObjects.stream().filter(item->item.theobject.editoraction.toLowerCase().contains("machine") || item.theobject.editoraction.toLowerCase().contains("dryer")).findFirst();
						if(opt.isPresent()) {
							int val1 = basket.objectFillingMulti.get(key1);
							if(val1>0)
							{
								CWorldObject targetActionObject3 = basket.theaddress.getObjectById(key1);
								if(targetActionObject3!=null && targetActionObject3.objectFilling<targetActionObject3.getObjectFillingMax())
								{
									basket.objectFillingMulti.put(targetActionObject3.uniqueId, 0);
									targetActionObject3.objectFilling=targetActionObject3.getObjectFillingMax();
								}
							}
						}
					}
					//---------------------------
					
					
					//Laundry on the floor
					//if(basket!=null)
					//	Gdx.app.debug("", "log_0");
					
					if(basket.getObjectFillingMulti() < basket.getObjectFillingMultiMax() - town.action_changeclothesvalue 
							//&& (basket.isOccupiedBy==null || basket.isOccupiedBy.uniqueId==uniqueId) 
							//&& basket.isOccupiedBy2==null
							)
					{
						for(CWorldObject laundry1 : basket.theaddress.listWorldObjects)
						{
							if(laundry1.theobject.editoraction.contains("laundryobject"))
							{
								//if(laundry1.isOccupiedBy==null)
								{
									retarr[0]=basket;
									retarr[5]=laundry1;
									
									return retarr;
								}
							}
						}
					}
					
					//basket
					//  isoccupied2=waschmaschine/trockner
					//	nicht: actionvar1=id waschmaschine/trockner
					//waschmaschine/trockner
					//	actionvar1=1 fertig
					//  isoccupied2=basket
					//	nicht: actionvar2=id basket - wozu?
					
					//Washingmachine / Dryer fertig
					if(basket.getObjectFillingMulti()==0 
							&& basket.isOccupiedBy2!=null
							)
					{
						
						if(thehuman.getName().contains("Mark Lewis")){
							//Gdx.app.debug(thehuman.getName(), "Laundry_1_1");
						}
						
						
						/*
						CAddress theadr = basket.theaddress;
						for(CAddress adr1 : town.gameWorld.worldAddressList)
						{
							if(basket.theaddress.addressId==adr1.addressId) {
								theadr = adr1;
							}
						}
						 */
						
						for(CWorldObject obj2 : basket.theaddress.listWorldObjects)
						//for(CWorldObject obj2 : basket.theaddress.listWorldObjects)
						//for(CWorldObject obj2 : town.gameWorld.worldObjects)
						{
							
							if(obj2.theobject.editoraction.contains("laundryroom_dryer")){
							//if(thehuman.getName().contains("Helen Hill")){
								//Gdx.app.debug(theaddress.addressName, " --- " + obj2.theobject.editoraction);
								
								//cfm123
								/*
								for(int key1 : obj2.objectFillingMulti.keySet())
								{
									Gdx.app.debug("fill_3","");
									int val1 = obj2.objectFillingMulti.get(key1);
									if(val1>0)
									{
										Gdx.app.debug("fill_4","");
										CWorldObject targetActionObject3 = obj2.theaddress.getObjectById(key1);
										if(targetActionObject3!=null && targetActionObject3.objectFilling<targetActionObject3.getObjectFillingMax())
										{
											Gdx.app.debug("fill_5","");
											obj2.objectFillingMulti.put(targetActionObject3.uniqueId, 0);
											targetActionObject3.objectFilling=targetActionObject3.getObjectFillingMax();
										}
									}
								}
								 */

								
							}
														
							
							
							
							if(obj2.theobject.editoraction.contains("laundryroom_dryer") && obj2.getObjectFillingMulti()>0){
							//if(obj2.theobject.editoraction.contains("laundryroom_dryer")){
									if(thehuman.getName().contains("Mark Lewis")){
										//Gdx.app.debug(thehuman.getName(), "Laundry_1_3");
									}
									
									retarr[0]=basket;
									retarr[4]=obj2; //Dryer
									//Gdx.app.debug("", "log_2");
									return retarr;
							}

							
							if(obj2.theobject.editoraction.contains("laundryroom_washingmachine")  
									
									//&& ((basket.isOccupiedBy2!=null && basket.isOccupiedBy2.uniqueId==obj2.uniqueId) 
									//|| obj2.isOccupiedBy2==null || basket.isOccupiedBy2==null) 
									//&& obj2.actionvar1==1 //fertig
								)
							{
								
								//Washingmachine Requirement: Dryer: active, empty, not occupied
								if(obj2.theobject.editoraction.contains("laundryroom_washingmachine"))
								{
									if(thehuman.getName().contains("Mark Lewis")){
										//Gdx.app.debug(thehuman.getName(), "Laundry_1_4");
									}
									
									for(CWorldObject dryer : basket.theaddress.listWorldObjects)
									{
										if(dryer.theobject.editoraction.contains("laundryroom_dryer"))
										{
											if(dryer.isActiveByWaterConsumption() 
													&& dryer.isActiveByEnergyConsumption() 
													//&& dryer.getObjectFillingMulti()==0 
													&& (dryer.isOccupiedBy==null || dryer.isOccupiedBy.uniqueId==uniqueId)
													&& dryer.actionvar1==0
													)
											{
												retarr[0]=basket;
												retarr[2]=obj2; //washingmachine
												retarr[3]=dryer;
												//Gdx.app.debug("", "log_3");
												return retarr;
											}
										}
									}
								}
							}
						}
					}
					
					
					//Basket ist voll/bereit für Wäsche
					if(basket.getObjectFillingMulti()>basket.getObjectFillingMultiMax()/2f && (basket.isOccupiedBy==null || basket.isOccupiedBy.uniqueId==uniqueId)) //occupied prüfung: falls laundrybasket von anderer stelle aus occupied wurde
					{
						//Requirement: Waschmaschine: active, empty, not occupied
						for(CWorldObject wm : basket.theaddress.listWorldObjects)
						{
							if(wm.theobject.editoraction.contains("laundryroom_washingmachine"))
							{
								if(wm.isActiveByWaterConsumption() 
										&& wm.isActiveByEnergyConsumption() 
										&& (wm.isOccupiedBy==null || wm.isOccupiedBy.uniqueId==uniqueId)
										&& wm.actionvar1==0
										)
								{
									retarr[0]=basket;
									retarr[1]=wm; //washingmachine
									return retarr;
								}
							}
						}
					}
					
				}
			}
		}
		
		return retarr;
	}
	
	public int getOwnerCount()
	{
		int count=0;
		
		if(owner!=null)
			count++;
		if(owner2!=null)
			count++;
		if(owner3!=null)
			count++;
		if(owner4!=null)
			count++;
		if(owner5!=null)
			count++;
		if(owner6!=null)
			count++;
		if(owner7!=null)
			count++;
		if(owner8!=null)
			count++;
		
		return count;
	}
	
	public String getWorkerTitle()
	{
		return theobject.getWorkerTitle();
	}
	public String getCompanyWorkingPlaceJobTitle(int type)
	{
		return theobject.getCompanyWorkingPlaceJobTitle(type);
	}
	public String getCompanyTypeString()
	{
		return theobject.getCompanyTypeString().toLowerCase();
	}
		
	//	public String getCompanyName()
	//	{
	//		return theobject.getCompanyName();
	//	}
	
	public String getCompanyObjectString()
	{
		String companyString="";
		
		try
		{
			String[] companyStringA = theobject.editoraction.split("_");
			companyString = companyStringA[2];
		}
		catch(Exception e){}
		
		return companyString;
	}
	
	public String getWorkingHoursString()
	{
		String sRet="";

		if(workTime1_From>-1 && workTime1_To>-1)
		{
			//sRet=workTime_Hours + " hours" + " from " + CHelper.getHourText(workTime1_From) +" to " + CHelper.getHourText(workTime1_To);
			sRet=CHelper.getAMPMHourText(workTime1_From) +" to " + CHelper.getAMPMHourText(workTime1_To);
			if(workTime2_From>-1)
				sRet+=" / "+CHelper.getAMPMHourText(workTime2_From) +" to " + CHelper.getAMPMHourText(workTime2_To);
		}
		
		return sRet;
	}
		
	public CAddress getCurrentAddress()
	{
		return town.gameWorld.getAddressByPoint(pos_x(), pos_y());
	}
		
	public int objectConditionPercent()
	{
		return objectCondition/(defaultObjectCondition/100);
	}
	
	private void handleConsumptionValues()
	{
		town.gameWorld.setEnergyConsumption(town.gameWorld.getRenderEnergyConsumption()+getEnergyConsumption());
		town.gameWorld.setWaterConsumption(town.gameWorld.getRenderWaterConsumption()+getWaterConsumption());
		town.gameWorld.setEnergyOutput(town.gameWorld.getRenderEnergyOutput()+getEnergyOutput());
		town.gameWorld.setWaterOutput(town.gameWorld.getRenderWaterOutput()+getWaterOutput());
	}
	
	public String getObjectFillingText()
	{
		return theobject.getObjectFillingText();
	}
	
	public String getObjectFilling2Text()
	{
		return theobject.getObjectFilling2Text();
	}
		
	public String getObjectFillingMultiSaveString()
	{
		String multiString="";
		
		for(int ikey : objectFillingMulti.keySet())
		{
			//;objid,exp-objid,exp;
			int id = (int)ikey;
			int exp = objectFillingMulti.get(ikey);
			if(multiString.length()>0)
				multiString+="-";
			multiString+=id+","+exp;
		}
		
		if(multiString.isEmpty())
			multiString="-";
		
		return multiString;
	}
	
	public void setObjectFillingMultiFromSaveString(String saveString)
	{
		if(saveString.length()>0)
		{
			//;objid,exp-objid,exp;
			String[] str1 = saveString.split("-");
			for(String p1 : str1)
			{
				String[] str2 = p1.split(",");
				if(str2 != null && str2.length==2)
				{
					int id = Integer.parseInt(str2[0]);
					int val = Integer.parseInt(str2[1]);
					
					objectFillingMulti.put(id, val);
				}
			}
		}		
	}
	
	public int getObjectFillingMulti()
	{
		int sum=0;
		
		for(int val : objectFillingMulti.values())
		{
			sum+=val;
		}
		
		return sum;
	}
	
	public int getObjectFillingMax()
	{
		return theobject.getObjectFillingMax();
	}
	
	public int getObjectFillingMax2()
	{
		return theobject.getObjectFillingMax2();
	}
	
	public int getObjectFillingMultiMax()
	{
		return theobject.getObjectFillingMultiMax();
	}
	
	public void addObjectFillingMulti(CWorldObject source, Boolean bRemoveFromSource)
	{
		if(source.objectFillingMulti==null)
			return;
		
		Set<Integer> set1 = source.objectFillingMulti.keySet();
		for(int key : set1)
		{
			int val = source.objectFillingMulti.get(key);
			
			if(objectFillingMulti.containsKey(key))
				val+=objectFillingMulti.get(key);
			
			objectFillingMulti.put(key, val);
			
			if(bRemoveFromSource)
				source.objectFillingMulti.put(key, 0);
		}
	}
	
	//Object Type<-----------------------------
	
	public void clearActionVariables()
	{
		actionanim1=0; //Animation zurücksetzen
		actionanim2=0; //Animation zurücksetzen
		
		actionfield1=0;
		actionfield2=0;
		actionfield3=0;

		
		bDrawObject=true;
		actionColor1=null;
		
		//achtung variablen sind manchmal action und resident übergreifend
		
		//		doObjectAction=false;
		//		
		//		x_temp=-1; //Position zwischenspeichern CAction
		//		y_temp=-1;
		//		rotation_temp=-1;
		
		//		tempcount=0;
		//		actionvar1=0;
		//		actionvar2=0;
		//		actionvar3=0;
		//		actionvar4=0;
		//		actionvar5=0;
		//		actionvar6=0;		
	}
	
	public void cancelAction1()
	{
		
//		if(theobject.editoraction.contains("laundrybasket") ||
//				theobject.editoraction.contains("laundryroom_washingmachine") ||
//				theobject.editoraction.contains("laundryroom_dryer") ||
//				thehuman!=null && activeAction!=null && activeAction.actionMode==ActionMode.LAUNDRY
//				)
//		{
//			//actionvars und occupied nicht zurücksetzen da objekte sonst "kaputt" sind
//			
//			//Action nicht canceln da sonst nicht weitergemacht wird
//			//Laundry nichts zurücksetzen bei cancel, da sonst die Objekteinstellungen gelöscht werden
//			//und die Objekte unbrauchbar werden
//			
//			if(activeAction!=null)
//			{
//				resetPathFinding();
//				activeAction.bActionMode=false;
//				activeAction.bGotoActionMode=false;
//				//activeAction.resetActionObjects();
//				activeAction=null;
//			}
//			
//			return;

		//		}
		
		
		//Boolean bResetActionVars=true;
		
		
//		if(theobject.editoraction.contains("laundryroom_washingmachine") ||
//				theobject.editoraction.contains("laundryroom_dryer"))
//		{
//			//Achtung: zu beachten: für change clothes müssen occupied zurückgesetzt werden
//			
//			//Machines: sollen weiterlaufen: actionvar1:schalter, actionvar2:timer
//			bResetActionVars=true;
//		}
		
		//Burial Cancel: Setze auch die Actionübergreifenden Variablen zurück
		//if(theobject.editoraction.contains("cemetery"))
		
		resetActionTimeChecks();
		
		Boolean bResetActionVars=true;
		if(theobject.editoraction.contains("diningroom_diningtable"))
			bResetActionVars=false;
		
		
		if(bResetActionVars)
		{
			Boolean bResetActionString1=true;
			Boolean bResetActionString2=true;
			
			if(actionstring1.contains("show_coffin") || actionstring1.contains("show_grave"))
				bResetActionString1=false;
			
			if(actionstring2.contains("gravedigger_action"))
				bResetActionString2=false;
			
			if(bResetActionString1)
				actionstring1="";
			
			if(bResetActionString2)
				actionstring2="";
						
			actionstring3="";
			actionvar1=0;
			actionvar2=0;
			actionvar3=0;
			actionvar4=0;
			actionvar5=0;
			actionvar6=0;
			actionvar7=0;
			actionvar8=0;
			actionvar9=0;
		}
		
		
		if(isHuman())
		{
			if(activeAction==null)
				return;
			activeActionMode=ActionMode.IDLE;
		}
		
		//Object: Occupied Action abbrechen
		//		if(bIsOccupied==true && isOccupiedBy!=null)
		//			isOccupiedBy.cancelAction1();
		//
		//		if(bIsOccupied2==true && isOccupiedBy2!=null)
		//			isOccupiedBy2.cancelAction1();
		//
		//		if(bIsOccupied3==true && isOccupiedBy3!=null)
		//			isOccupiedBy3.cancelAction1();
				
				//Object: OccupiedByExtern Action abbrechen
		//		if(bIsOccupiedByExtern==true && isOccupiedByExtern!=null)
		//			isOccupiedByExtern.cancelAction1();		
		
		iOccupied1_Arrived=0;
		iOccupiedExtern_Arrived=0;
		
		if(isOccupiedBy!=null)
		{
			//if(uniqueId==isOccupiedBy.uniqueId)
			{
				isOccupiedBy.cancelAction1();
				isOccupiedBy=null;
			}
		}
		
		if(isOccupiedBy2!=null)
		{
			//if(human.uniqueId==isOccupiedBy2.uniqueId)
			{
				CWorldObject temp1 = isOccupiedBy2;
				isOccupiedBy2=null;
				temp1.cancelAction1();
				
//				Boolean bcanc=true;
//				if(isOccupiedBy2.isOccupiedBy2!=null && isOccupiedBy2.isOccupiedBy2.uniqueId==uniqueId) //laundry doppelverknüpfung
//				{
//					if(isOccupiedBy2.isOccupiedBy2!=null)
//						isOccupiedBy2.cancelAction1();
//					
//					bcanc=false;
//				}
//				
//				if(bcanc)
//				{
//					isOccupiedBy2.cancelAction1();
//				}
//				
//				isOccupiedBy2=null;
			}
		}
		
		if(isOccupiedBy3!=null)
		{
			//if(human.uniqueId==isOccupiedBy3.uniqueId)
			{
				isOccupiedBy3.cancelAction1();
				isOccupiedBy3=null;
			}
		}
		
		if(isOccupiedBy4!=null)
		{
			{
				isOccupiedBy4.cancelAction1();
				isOccupiedBy4=null;
			}
		}
		if(isOccupiedBy5!=null)
		{
			{
				isOccupiedBy5.cancelAction1();
				isOccupiedBy5=null;
			}
		}
		if(isOccupiedBy6!=null)
		{
			{
				isOccupiedBy6.cancelAction1();
				isOccupiedBy6=null;
			}
		}
		if(isOccupiedBy7!=null)
		{
			{
				isOccupiedBy7.cancelAction1();
				isOccupiedBy7=null;
			}
		}
		if(isOccupiedBy8!=null)
		{
			isOccupiedBy8.cancelAction1();
			isOccupiedBy8=null;
		}
		if(isOccupiedBy9!=null)
		{
			isOccupiedBy9.cancelAction1();
			isOccupiedBy9=null;
		}
		
		
		if(isOccupiedByExtern!=null)
		{
			//if(human.uniqueId==isOccupiedByExtern.uniqueId)
			{
				//bIsOccupiedByExtern=false;
				isOccupiedByExtern.cancelAction1();
				isOccupiedByExtern=null;
			}
		}		
		
		if(worker!=null)
			worker.cancelAction1();
		
		if(worker2!=null)
			worker2.cancelAction1();
		
		
		//Owner
		if(owner!=null)
			owner.cancelAction1();
		if(owner2!=null)
			owner2.cancelAction1();
		if(owner3!=null)
			owner3.cancelAction1();
		if(owner4!=null)
			owner4.cancelAction1();
		if(owner5!=null)
			owner5.cancelAction1();
		if(owner6!=null)
			owner6.cancelAction1();
		if(owner7!=null)
			owner7.cancelAction1();		
		if(owner8!=null)
			owner8.cancelAction1();
		
		//Darf erst zum Schluss gemacht werden, da hier die occupied genullt werden
		if(activeAction!=null)
		{
			//Gdx.app.debug("", "cancel1");
			resetPathFinding();
			activeAction.bActionMode=false;
			activeAction.bGotoActionMode=false;
			activeAction.resetActionObjects();
			activeAction.iActionBlocker=100; //kurzer Blocker, damit nicht umgehend wieder angefangen wird - architect move address
			activeAction.actionState=0;
			activeAction=null;
		}
		
		resetGoByCar(); //führt sonst zu problemen in gobycar - black screen nullpointer exceptionw
		
	}
	
	public void setResearchProject(String objectId)
	{
		if(!objectId.isEmpty())
		{
			researchObject = town.gameWorld.gameResourceConfig.getObjectById(objectId);
		}
	}
	
	public float getHeadSizeByAge()
	{	
		if(thehuman==null)
			return 1;
		
//		int a=0;
//		if(a==0)
//			return 1;
		
		float fsizefactor1=1;
		float fsizefactor=1;
		//Erwachsen
		if(thehuman.getAge()>=18)
		{
			fsizefactor1=1.2f;
	    	fsizefactor=0.8f;
		}
		
		//Jugendlich
		if(thehuman.getAge()<=17)
		{
	    	fsizefactor1=1f;
	    	fsizefactor=1f;
		}
		
		//Kind
		if(thehuman.getAge()<=14)
		{
	    	fsizefactor1=0.8f;
	    	fsizefactor=1.2f;
		}
		
		//Klinkind			
		if(thehuman.getAge()<=9)
		{
			fsizefactor1=0.6f;
			fsizefactor=1.4f;
		}
		
		return fsizefactor;
	}
	
	public float getBodySizeByAge() //passt nicht für Y-Wert -> getbodySizeByAge2 benutzen oder so wie bei fitness studio
	{	
		if(thehuman==null)
			return 1;
		
		float fsizefactor1=1;
		float fsizefactor=1;
		
		//Erwachsen
		if(thehuman.getAge()>=18)
		{
			fsizefactor1=1.2f;
	    	fsizefactor=0.8f;
		}
		
		//Jugendlich
		if(thehuman.getAge()<=17)
		{
	    	fsizefactor1=1f;
	    	fsizefactor=1f;
		}
		
		//Kind
		if(thehuman.getAge()<=14)
		{
	    	fsizefactor1=0.8f;
	    	fsizefactor=1.2f;
		}
		
		//Klinkind			
		if(thehuman.getAge()<=9)
		{
			fsizefactor1=0.6f;
			fsizefactor=1.4f;
		}
		
		return fsizefactor1;
	}
	
	public float getBodySizeByAge2(int ipos)
	{	
		//ipos=0: width
		//ipos=1: height
		
		if(thehuman==null)
			return 1;
		
		float fsizefactor1=1;
		
		//Erwachsen
		if(thehuman.getAge()>=18)
		{
			fsizefactor1=1.2f;
			if(ipos==1)
				fsizefactor1=1;
				
		}
		
		//Jugendlich
		if(thehuman.getAge()<=17)
		{
	    	fsizefactor1=1f;
		}
		
		//Kind
		if(thehuman.getAge()<=14)
		{
	    	fsizefactor1=0.8f;
		}
		
		//Klinkind			
		if(thehuman.getAge()<=9)
		{
			fsizefactor1=0.6f;
			if(ipos==1)
				fsizefactor1=0.8f;
				
		}
		
		return fsizefactor1;
	}	
		
	public void drawDefenseWarningRange(ShapeRenderer shapeRenderer)
	{
		if ((theobject.editoraction.contains("illuminati_defensewarningsystem") && bDefenseAlarm))// || theobject.editoraction.contains("company_waterworks_groundwaterextractionsystem"))
		{
			//shapeRenderer.begin();			
			shapeRenderer.setColor(1.0f, 0.0f, 0.0f, 0.3f);
			shapeRenderer.set(ShapeType.Filled);
			
			float reichweite = 1000;
			defenseWarningTimer+=CHelper.getDeltaSeconds(town);
			if(defenseWarningTimer>10)
				defenseWarningTimer=0;
			if(town.gameWorld.getAmbientLightValue(town.gameWorld.worldTime)<0.4f) // && town.gameCam.zoom>30)
			{
				shapeRenderer.setColor(1.0f, 0.0f, 0.0f, 0.7f);
				reichweite=4000;
			}
			
			shapeRenderer.circle(pos_x()+width/2, pos_y()+height/2, reichweite/(1+defenseWarningTimer));
			//shapeRenderer.end();
		}
	}
	
	public int getKeyFrame()
	{
    	if(theobject.editoraction.contains("supermarket_foodpallet"))
    	{
    		float filling = (objectFilling/50)-1;
    		if(filling<0)
    			filling=0;
    		
    		int val=Math.round((getObjectFillingMax()/50)-filling-1);
    		if(objectFilling>0 && val==7)
    			val=6;
    		
    		return val;
    	}
		
    	if(theobject.editoraction.contains("supermarket_shelf"))
    	{
			if(objectFilling>getObjectFillingMax()/1.1f)
				return 0;

			if(objectFilling>getObjectFillingMax()/1.3f)
				return 2;

			if(objectFilling>getObjectFillingMax()/1.7f)
				return 2;
			
			if(objectFilling>getObjectFillingMax()/2.4f)
				return 3;

			if(objectFilling>getObjectFillingMax()/3.7)
				return 4;

			if(objectFilling>getObjectFillingMax()/5)
				return 5;
			
			if(objectFilling>0)
				return 6;
			    		
			if(objectFilling==0)
				return 7;
    	}
    	
    	if(theobject.editoraction.contains("laundrybasket"))
    	{
    		float fillvalue = getObjectFillingMulti();
    		float fVal = (float)getObjectFillingMultiMax() / 6f;
    		int filling = Math.round(fillvalue / fVal);
    		if(filling>6)
    			filling=6;
    		if(fillvalue>0 && fillvalue<3)
    			filling=1;
    		
    		return filling;
    	}
    	
    	return 0;
	}
	
	private void drawSpecialTexture(TextureRegion currentFrame, Boolean bShowDel, Boolean bShowSell)
	{
		float sx=1;
		float sy=1;
		
		if(bShowDel)
		{
			sx=1f;
			sy=1f;
		}
		
		if(iObjectIsReady<100) //DrawSpecial
		{
			float status = iObjectIsReady/100f;
			if(status<0.5f)
				status=0.5f;
			
			//Gdx.app.debug("", "statusdebug: "+status + ", iObjectIsReady: " + iObjectIsReady);
			town.gameWorld.worldSpriteBatch.setColor(1, 1, 1, status);
			
			//gameWorld.worldSpriteBatch.setColor(1, 1, 1, 0.1f);
		}
		
		if(bShowDel)
			town.gameWorld.worldSpriteBatch.setColor(1, 0, 0, 0.4f);
		
		if(theobject.editoraction.contains("outdoor_tree") || 
				theobject.editoraction.contains("outdoor_plant") ||
				theobject.editoraction.contains("outdoor_flower")
				)
		{
			int month=town.gameWorld.worldTime.day;
			town.gameWorld.worldSpriteBatch.setColor(1,1,1,1);
			
			if(month==11||month==12||month==1||month==2)
			{
				town.gameWorld.worldSpriteBatch.setColor(0.55f,0.55f,0.55f,1);
			}
			if(month==10)
			{
				town.gameWorld.worldSpriteBatch.setColor(0.55f,0.55f,0.55f,1);
			}
			if(month==3)
			{
				town.gameWorld.worldSpriteBatch.setColor(0.7f,0.7f,0.7f,1);
			}
			if(month==4)
			{
				town.gameWorld.worldSpriteBatch.setColor(0.8f,0.8f,0.8f,1);
			}
			
			if(color1!=null && (color1.r!=1 || color1.g!=1 || color1.b!=1))
				town.gameWorld.worldSpriteBatch.setColor(color1);

			if(iObjectIsReady<100) //Tree
			{
				float status = iObjectIsReady/100f;
				if(status<0.5f)
					status=0.5f;
				town.gameWorld.worldSpriteBatch.setColor(1, 1, 1, status);
				
				//gameWorld.worldSpriteBatch.setColor(1, 1, 1, 0.5f);
			}
			
			if(bShowDel)
				town.gameWorld.worldSpriteBatch.setColor(1, 0, 0, 0.4f);
			
			town.gameWorld.worldSpriteBatch.draw(currentFrame, pos_x(), pos_y(), width/2, height/2, width, height, sx+hposition/1.8f, sy+hposition/1.8f, rotation());
			
			
			//Draw snow
			if(month==11||month==12||month==1||month==2)
			{
				if(month==12||month==1)
					town.gameWorld.worldSpriteBatch.setColor(1f,1f,1f,0.7f);
				
				if(month==2||month==11)
					town.gameWorld.worldSpriteBatch.setColor(1f,1f,1f,0.5f);
				
				if(iObjectIsReady<100) //Tree
				{
					float status = iObjectIsReady/100f;
					if(status<0.5f)
						status=0.5f;
					town.gameWorld.worldSpriteBatch.setColor(1, 1, 1, status);
					
					//gameWorld.worldSpriteBatch.setColor(1, 1, 1, 0.3f);
				}
				
				if(theobject.textureRegion2!=null)
					town.gameWorld.worldSpriteBatch.draw(theobject.textureRegion2[0], pos_x(), pos_y(), width/2, height/2, width, height, sx, sy, rotation());
			}
		}
		
		if(theobject.editoraction.contains("outdoor_ground_water"))
		{
			town.gameWorld.worldSpriteBatch.setShader(town.gameWorld.gameResourceConfig.snowShader);
			
			int day=town.gameWorld.worldTime.day;
			if(day==11||day==12||day==1||day==2)
			{
				if(day==11||day==2)
				{
					currentFrame = theobject.textureRegion2[0];
					town.gameWorld.worldSpriteBatch.setColor(1, 1, 1, 0.735f);
				}
				
				if(day==12||day==1)
				{
					currentFrame = theobject.textureRegion2[0];
					town.gameWorld.worldSpriteBatch.setColor(1, 1, 1, 0.8f);
				}
				
				town.gameWorld.worldSpriteBatch.draw(currentFrame, pos_x(), pos_y(), width/2, height/2, width, height, sx, sy, rotation());
			}
			else
			{
				animationtimer+=CHelper.getDeltaSeconds(town);
				
				if(animationtimer>1f)
					animationtimer=0;
				
	    		currentFrame = theobject.textureRegion[0];
	    		int mov=2;
	    		
	    		int px=pos_x()+rand.nextInt(mov)-rand.nextInt(mov);
	    		int py=pos_y()+rand.nextInt(mov)-rand.nextInt(mov);
	    		
	    		int w=width+rand.nextInt(mov)-rand.nextInt(mov);
	    		int h=height+rand.nextInt(mov)-rand.nextInt(mov);
	    		
	    		town.gameWorld.worldSpriteBatch.setColor(1, 1, 1, 0.4f);
	    		town.gameWorld.worldSpriteBatch.draw(currentFrame, px, py, w/2, h/2, w, h, sx, sy, rotation());
	    		
	    		TextureRegion waterFrame=null;
	    		waterFrame = town.gameWorld.gameResourceConfig.animations.get("anim_shower2").getKeyFrame(town.gameWorld.getStateTime(), true);
	    		town.gameWorld.worldSpriteBatch.setColor(0.0f, 0.0f, 0.8f, 0.1f);
	    		int tm=20;
	    		town.gameWorld.worldSpriteBatch.draw(waterFrame, px, py, w/2, h/2, w, h, sx, sy, rotation());
	    		town.gameWorld.worldSpriteBatch.draw(waterFrame, px+tm, py+tm, w/2-tm, h/2-tm, w-tm*2, h-tm*2, sx, sy, rotation());
	    		town.gameWorld.worldSpriteBatch.draw(waterFrame, px, py, w/2, h/2, w, h, sx, sy, rotation()+180);
	    		town.gameWorld.worldSpriteBatch.draw(waterFrame, px+tm, py+tm, w/2-tm, h/2-tm, w-tm*2, h-tm*2, sx, sy, rotation()+180);
			}
			
			town.gameWorld.worldSpriteBatch.setShader(null);
		}
		

    	if(theobject.editoraction.contains("company_college_spaceship"))
    	{
    		currentFrame = theobject.textureRegion[0];
    		float sx2=sx;
    		float sy2=sy;
    		
    		if(iLaunchstatus>0 && iLaunchstatus<150) {
	    		if(renderAnim>renderAnimTarget) {
	    			iLaunchstatus++;
	    		}
    		}
    		
    		if(iLaunchstatus==120) {
    			town.setAchievement("launch_rocket");
    			town.gameGui.yesnoDlg.showDlg(true, YesNoDlgType.LAUNCHSPACESHIP); //endgamedialog
    		}
    		
    		//Gdx.app.setLogLevel(10);
    		//Gdx.app.log("", ""+iLaunchstatus);
    		
			float fls = iLaunchstatus;
			sx2+=fls/10f;
			sy2+=fls/10f;
    					
			if(iLaunchstatus<100) {
				town.gameWorld.worldSpriteBatch.draw(currentFrame, pos_x(), pos_y(), width/2, height/2, width, height, sx2, sy2, rotation());
			}
    	}

		
    	if(theobject.editoraction.contains("company_college_workingplace_researchlab"))
    	{
    		currentFrame = theobject.textureRegion[0];
    		
    		if(isWorkerAtWorkplace())
    			currentFrame = theobject.textureRegion[1];
    		
    		town.gameWorld.worldSpriteBatch.draw(currentFrame, pos_x(), pos_y(), width/2, height/2, width, height, sx, sy, rotation());
    	}
    	
    	if(theobject.editoraction.contains("company_illuminati_officeworkingplace"))
    	{
    		currentFrame = theobject.textureRegion[0];
    		TextureRegion anim1 = theobject.textureRegion[2];
    		
    		if(isWorkerAtWorkplace())
    			currentFrame = theobject.textureRegion[1];
    		
    		town.gameWorld.worldSpriteBatch.draw(currentFrame, pos_x(), pos_y(), width/2, height/2, width, height, sx, sy, rotation());
    		
    		if(isWorkerAtWorkplace())
    		{
    			town.gameWorld.worldSpriteBatch.setColor(1, 1, 1, 0.5f);
    			int w=width;
    			int h=height;
    			float rotation1=rotation();
    			
    			town.gameWorld.worldSpriteBatch.draw(anim1, pos_x(), pos_y(), w/2, h/2, w, h, sx, sy, rotation1);
    		}
    	}
    	
    	if(theobject.editoraction.contains("illuminati_defensewall"))
    	{
    		int index=0;
    		
    		if(objectConditionPercent()<80)
    			index=1;
    		if(objectConditionPercent()<60)
    			index=2;
    		if(objectConditionPercent()<40)
    			index=3;
    		if(objectConditionPercent()<20)
    			index=4;
    		
    		currentFrame = theobject.textureRegion[index];
    		
    		town.gameWorld.worldSpriteBatch.draw(currentFrame, pos_x(), pos_y(), width/2, height/2, width, height, sx, sy, rotation());
    	}
    	
    	if(theobject.editoraction.contains("company_objectdesign_officeworkingplace_artist1"))
    	{
    		currentFrame = theobject.textureRegion[0];
    		
    		if(isWorkerAtWorkplace())
    			currentFrame = theobject.textureRegion[1];
    		
    		town.gameWorld.worldSpriteBatch.draw(currentFrame, pos_x(), pos_y(), width/2, height/2, width, height, sx, sy, rotation());
    	}
    	
    	if(theobject.editoraction.contains("bathroom_towelcabinet"))
    	{
   			currentFrame = theobject.objectAnimation.getKeyFrames()[getObjectFillingMax()-objectFilling];
   			town.gameWorld.worldSpriteBatch.draw(currentFrame, pos_x(), pos_y(), width/2, height/2, width, height, sx, sy, rotation());
    	}
    	
    	if(theobject.editoraction.contains("supermarket_foodpallet"))
    	{
//    		float filling = (objectFilling/50)-1;
//    		if(filling<0)
//    			filling=0;
//    		
//    		int val=Math.round((getObjectFillingMax()/50)-filling-1);
//    		if(objectFilling>0 && val==7)
//    			val=6;
    		
   			currentFrame = theobject.objectAnimation.getKeyFrames()[getKeyFrame()];
   			town.gameWorld.worldSpriteBatch.draw(currentFrame, pos_x(), pos_y(), width/2, height/2, width, height, sx, sy, rotation());
    	}
    	
    	if(theobject.editoraction.contains("supermarket_buyin"))
    	{
    		float teiler = (getObjectFillingMultiMax()/theobject.objectAnimation.getKeyFrames().length);
    		float filling = (getObjectFillingMulti()/teiler)-1;
    		if(filling<0)
    			filling=0;
    		
    		if(getObjectFillingMulti()>0 && Math.round(filling)==0)
    			filling=1;    			
    			
    		//Gdx.app.debug("", "filling: "+filling + ", getObjectFillingMulti(): " + getObjectFillingMulti());
    		
   			currentFrame = theobject.objectAnimation.getKeyFrames()[Math.round(filling)];
   			town.gameWorld.worldSpriteBatch.draw(currentFrame, pos_x(), pos_y(), width/2, height/2, width, height, sx, sy, rotation());
    	}
    	    	
    	if(theobject.editoraction.contains("traffic_car"))
    	{
   			currentFrame = theobject.objectAnimation.getKeyFrames()[(int) actionanim1];
   			town.gameWorld.worldSpriteBatch.draw(currentFrame, pos_x(), pos_y(), width/2, height/2, width, height, sx, sy, rotation());
    	}
    	
    	if(theobject.editoraction.contains("fruitplate"))
    	{
   			currentFrame = theobject.objectAnimation.getKeyFrames()[8-objectFilling];
   			town.gameWorld.worldSpriteBatch.draw(currentFrame, pos_x(), pos_y(), width/2, height/2, width, height, sx, sy, rotation());
    	}
    	
    	if(theobject.editoraction.contains("recyclingcenter_garbagebag"))
    	{
    		int form1 = width%5;
    		if(form1>4)
    			form1=4;
    		
   			currentFrame = theobject.objectAnimation.getKeyFrames()[form1];
   			town.gameWorld.worldSpriteBatch.draw(currentFrame, pos_x(), pos_y(), width/2, height/2, width, height, sx, sy, rotation());
    	}
    	
    	if(theobject.editoraction.contains("coffeepot1"))
    	{
   			currentFrame = theobject.objectAnimation.getKeyFrames()[objectFilling];
   			
   			if(actionfield1==1)
   			{
   				sx=1.5f;
   				sy=1.5f;
   			}
   			
   			town.gameWorld.worldSpriteBatch.draw(currentFrame, pos_x(), pos_y(), width/2, height/2, width, height, sx, sy, rotation());
    	}
    	
    	if(theobject.editoraction.contains("laundrybasket"))
    	{
//    		float fillvalue = getObjectFillingMulti();
//    		float fVal = (float)getObjectFillingMultiMax() / 6f;
//    		int filling = Math.round(fillvalue / fVal);
//    		if(filling>6)
//    			filling=6;
//    		if(fillvalue>0 && fillvalue<3)
//    			filling=1;
    		
   			currentFrame = theobject.objectAnimation.getKeyFrames()[getKeyFrame()];
    		
   			town.gameWorld.worldSpriteBatch.draw(currentFrame, pos_x(), pos_y(), width/2, height/2, width, height, sx, sy, rotation());
    	}
    	    	
    	if(theobject.editoraction.contains("company_fitnessstudio_legpress"))
    	{
    		if(actionfield1==0)
    			currentFrame = theobject.objectAnimation.getKeyFrames()[0];
    		if(actionfield1==1)
    			currentFrame = theobject.objectAnimation.getKeyFrames()[1];
    		
    		town.gameWorld.worldSpriteBatch.draw(currentFrame, pos_x(), pos_y(), width/2, height/2, width, height, sx, sy, rotation());
    	}
		
    	if(theobject.editoraction.contains("company_fitnessstudio_shoulderpress"))
    	{
    		if(actionfield1==0)
    			currentFrame = theobject.objectAnimation.getKeyFrames()[0];
    		if(actionfield1==1)
    			currentFrame = theobject.objectAnimation.getKeyFrames()[1];
    		
    		town.gameWorld.worldSpriteBatch.draw(currentFrame, pos_x(), pos_y(), width/2, height/2, width, height, sx, sy, rotation());
    	}
		
    	if(theobject.editoraction.contains("company_fitnessstudio_pecmachine"))
    	{
    		if(actionfield1==0)
    			currentFrame = theobject.objectAnimation.getKeyFrames()[0];
    		if(actionfield1==1)
    			currentFrame = theobject.objectAnimation.getKeyFrames()[1];
    		
    		town.gameWorld.worldSpriteBatch.draw(currentFrame, pos_x(), pos_y(), width/2, height/2, width, height, sx, sy, rotation());
    	}
    	
    	if(theobject.editoraction.contains("_billard"))
    	{
    		//Gdx.app.debug("", "billard 1");
   			currentFrame = theobject.objectAnimation.getKeyFrames()[(int)actionfield1];
   			town.gameWorld.worldSpriteBatch.draw(currentFrame, pos_x(), pos_y(), width/2, height/2, width, height, sx, sy, rotation());
    	}
    	
    	if(theobject.editoraction.contains("company_playground_swing"))
    	{
    		if(actionfield1==1)
    			currentFrame = theobject.objectAnimation.getKeyFrames()[1];
    		if(actionfield1==2)
    			currentFrame = theobject.objectAnimation.getKeyFrames()[2];
    		
    		town.gameWorld.worldSpriteBatch.draw(currentFrame, pos_x(), pos_y(), width/2, height/2, width, height, sx, sy, rotation());
    	}
		
		if(theobject.editoraction.contains("flora_"))
		{
			town.gameWorld.worldSpriteBatch.setColor(1f, 1f, 1f, 1f);

			if(town.gameWorld.worldTime.day==10) 
				town.gameWorld.worldSpriteBatch.setColor(0.7f, 0.45f, 0.25f, 1f);
			if(town.gameWorld.worldTime.day==11) 
				town.gameWorld.worldSpriteBatch.setColor(0.6f, 0.35f, 0.15f, 1f);
			if(town.gameWorld.worldTime.day==12) 
				town.gameWorld.worldSpriteBatch.setColor(0.5f, 0.25f, 0.05f, 1f);
			if(town.gameWorld.worldTime.day==1) 
				town.gameWorld.worldSpriteBatch.setColor(0.6f, 0.35f, 0.15f, 1f);
			if(town.gameWorld.worldTime.day==2)
				town.gameWorld.worldSpriteBatch.setColor(0.7f, 0.45f, 0.25f, 1f);
			if(town.gameWorld.worldTime.day==3 || town.gameWorld.worldTime.day==4) 
				town.gameWorld.worldSpriteBatch.setColor(0.8f, 0.65f, 0.45f, 1f);
			
			town.gameWorld.worldSpriteBatch.draw(currentFrame, pos_x(), pos_y(), width/2, height/2, width, height, sx, sy, rotation());
		}
		
		if(theobject.editoraction.contains("company_playground_seesaw"))
		{
			if(actionfield1==1)
			{
				town.gameWorld.worldSpriteBatch.draw(theobject.textureRegion2[0], pos_x(), pos_y(), width/2, height/2, width, height, sx, sy, rotation());
			}
			else
			{
				town.gameWorld.worldSpriteBatch.draw(currentFrame, pos_x(), pos_y(), width/2, height/2, width, height, sx, sy, rotation());
			}
		}
		
		if(theobject.editoraction.contains("supermarket_checkout"))
		{
			town.gameWorld.worldSpriteBatch.draw(currentFrame, pos_x(), pos_y(), width/2, height/2, width, height, sx, sy, rotation());
			
			Vector2 v2=null;
			Texture food=null;
			int gox=(int) town.getSizeValue(10);
			int foodSize=(int) town.getSizeValue(50);
			
			if(objectFilling>0)
			{
				v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), (int)town.getSizeValue(10)+2*gox+movementX, (int)town.getSizeValue(30), width/2, height/2, rotation());
				food = town.gameWorld.gameResourceConfig.textures.get("company_supermarket_food1");
				town.gameWorld.worldSpriteBatch.draw(food, v2.x-foodSize/2, v2.y-foodSize/2, foodSize/2, foodSize/2, foodSize, foodSize, 1, 1, rotation(), 0, 0, food.getWidth(), food.getHeight(), false, false);
			}
			
			if(objectFilling>10)
			{
				v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), (int)town.getSizeValue(30)+2*gox+movementX, (int)town.getSizeValue(50), width/2, height/2, rotation());
				food = town.gameWorld.gameResourceConfig.textures.get("company_supermarket_food2");
				town.gameWorld.worldSpriteBatch.draw(food, v2.x-foodSize/2, v2.y-foodSize/2, foodSize/2, foodSize/2, foodSize, foodSize, 1, 1, rotation(), 0, 0, food.getWidth(), food.getHeight(), false, false);
			}
			
			if(objectFilling>20)
			{
				v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), (int)town.getSizeValue(50)+2*gox+movementX, (int)town.getSizeValue(60), width/2, height/2, rotation());
				food = town.gameWorld.gameResourceConfig.textures.get("company_supermarket_food3");
				town.gameWorld.worldSpriteBatch.draw(food, v2.x-foodSize/2, v2.y-foodSize/2, foodSize/2, foodSize/2, foodSize, foodSize, 1, 1, rotation(), 0, 0, food.getWidth(), food.getHeight(), false, false);
			}
			
			if(objectFilling>30)
			{
				v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), (int)town.getSizeValue(70)+2*gox+movementX, (int)town.getSizeValue(70), width/2, height/2, rotation());
				food = town.gameWorld.gameResourceConfig.textures.get("company_supermarket_food4");
				town.gameWorld.worldSpriteBatch.draw(food, v2.x-foodSize/2, v2.y-foodSize/2, foodSize/2, foodSize/2, foodSize, foodSize, 1, 1, rotation(), 0, 0, food.getWidth(), food.getHeight(), false, false);
			}
			
			if(objectFilling>40)
			{
				v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), (int)town.getSizeValue(80)+2*gox+movementX, (int)town.getSizeValue(40), width/2, height/2, rotation());
				food = town.gameWorld.gameResourceConfig.textures.get("company_supermarket_food5");
				town.gameWorld.worldSpriteBatch.draw(food, v2.x-foodSize/2, v2.y-foodSize/2, foodSize/2, foodSize/2, foodSize, foodSize, 1, 1, rotation(), 0, 0, food.getWidth(), food.getHeight(), false, false);
			}		
			
			if(objectFilling>50)
			{
				v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), (int)town.getSizeValue(90)+2*gox+movementX, (int)town.getSizeValue(50), width/2, height/2, rotation());
				food = town.gameWorld.gameResourceConfig.textures.get("company_supermarket_food6");
				town.gameWorld.worldSpriteBatch.draw(food, v2.x-foodSize/2, v2.y-foodSize/2, foodSize/2, foodSize/2, foodSize, foodSize, 1, 1, rotation(), 0, 0, food.getWidth(), food.getHeight(), false, false);
			}
			
			if(objectFilling>60)
			{
				v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), (int)town.getSizeValue(100)+2*gox+movementX, (int)town.getSizeValue(60), width/2, height/2, rotation());
				food = town.gameWorld.gameResourceConfig.textures.get("company_supermarket_food7");
				town.gameWorld.worldSpriteBatch.draw(food, v2.x-foodSize/2, v2.y-foodSize/2, foodSize/2, foodSize/2, foodSize, foodSize, 1, 1, rotation(), 0, 0, food.getWidth(), food.getHeight(), false, false);
			}	
			
			if(objectFilling>70)
			{
				v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), (int)town.getSizeValue(110)+2*gox+movementX, (int)town.getSizeValue(50), width/2, height/2, rotation());
				food = town.gameWorld.gameResourceConfig.textures.get("company_supermarket_food8");
				town.gameWorld.worldSpriteBatch.draw(food, v2.x-foodSize/2, v2.y-foodSize/2, foodSize/2, foodSize/2, foodSize, foodSize, 1, 1, rotation(), 0, 0, food.getWidth(), food.getHeight(), false, false);
			}	
			
			if(objectFilling>80)
			{
				v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), (int)town.getSizeValue(120)+2*gox+movementX, (int)town.getSizeValue(40), width/2, height/2, rotation());
				food = town.gameWorld.gameResourceConfig.textures.get("company_supermarket_food1");
				town.gameWorld.worldSpriteBatch.draw(food, v2.x-foodSize/2, v2.y-foodSize/2, foodSize/2, foodSize/2, foodSize, foodSize, 1, 1, rotation(), 0, 0, food.getWidth(), food.getHeight(), false, false);
			}
			
			if(objectFilling>90)
			{
				v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), (int)town.getSizeValue(130)+2*gox+movementX, (int)town.getSizeValue(60), width/2, height/2, rotation());
				food = town.gameWorld.gameResourceConfig.textures.get("company_supermarket_food2");
				town.gameWorld.worldSpriteBatch.draw(food, v2.x-foodSize/2, v2.y-foodSize/2, foodSize/2, foodSize/2, foodSize, foodSize, 1, 1, rotation(), 0, 0, food.getWidth(), food.getHeight(), false, false);
			}				
		}
		
		//Pos wird in richtung tür versetzt
		if(theobject.editoraction.contains("laundryroom_washingmachine") ||
				theobject.editoraction.contains("laundryroom_dryer") 
				)
		{
			int runx=0;
			int runy=0;
			
			//Maschine läuft
			if(actionvar1==2 && !town.gameWorld.worldPause)
			{
				actionvar2+=CHelper.getDeltaSeconds(town);
				int val=5;
				runx=rand.nextInt(val)-rand.nextInt(val);
				runy=rand.nextInt(val)-rand.nextInt(val);
			}
			
			//Maschine ist fertig
			if(actionvar2>7000)
			{
				actionvar2=0;
				actionvar1=1;
			}
			
			if((doObjectAction || (objectFilling==0 && getObjectFillingMulti()==0)) && theobject.textureRegion.length>1)
	        {
				town.gameWorld.worldSpriteBatch.draw(theobject.textureRegion[1], pos_x()+runx, pos_y()+runy, width/2, height/2, width, height, sx, sy, rotation());
	        }
	        else
	        	town.gameWorld.worldSpriteBatch.draw(currentFrame, pos_x()+runx, pos_y()+runy, width/2, height/2, width, height, sx, sy, rotation());
	        			
	        return;
		}
		
		
		//pos wird mittig gesetzt
		if(theobject.editoraction.contains("fridge"))
		{
	        if((doObjectAction || (objectFilling==0 && getObjectFillingMulti()==0)) && theobject.textureRegion.length>1)
	        {
	    		currentFrame=theobject.textureRegion[1];
	    		town.gameWorld.worldSpriteBatch.draw(currentFrame, pos_x(), pos_y(), width/2, height/2, width, height, sx, sy, rotation());
	        }
	        else
	        	town.gameWorld.worldSpriteBatch.draw(currentFrame, pos_x(), pos_y(), width/2, height/2, width, height, sx, sy, rotation());
	        
	        return;
		}


		//nicht zusammenführen mit fridge, andere checks
		if(theobject.editoraction.contains("bedroom_wardrobe"))
		{
	        if((doObjectAction) && theobject.textureRegion.length>1)
	    		currentFrame = theobject.textureRegion[1];
	        
	        town.gameWorld.worldSpriteBatch.draw(currentFrame, pos_x(), pos_y(), width/2, height/2, width, height, sx, sy, rotation());
	        
	        return;
		}
		
		//nicht mit anderen objects zusammenführen
		if(theobject.editoraction.contains("kitchen_cupboard"))
		{
	        if((doObjectAction) && theobject.textureRegion.length>1)
	    		currentFrame = theobject.textureRegion[1];
	        
	        town.gameWorld.worldSpriteBatch.draw(currentFrame, pos_x(), pos_y(), width/2, height/2, width, height, sx, sy, rotation());
	        
	        return;
		}
		
		if(theobject.editoraction.contains("recyclingcenter_garbagecontainer") || theobject.editoraction.contains("garbagecan"))
		{
	        if((doObjectAction) && theobject.textureRegion.length>1)
	    		currentFrame=theobject.textureRegion[1];
	        
	        town.gameWorld.worldSpriteBatch.draw(currentFrame, pos_x(), pos_y(), width/2, height/2, width, height, sx, sy, rotation());
	        
	        return;
		}
		
		if(theobject.editoraction.contains("company_supermarket_shoppingcart"))
		{
			TextureRegion tempreg=currentFrame;
			TextureRegion reg[] = theobject.objectAnimation.getKeyFrames();
			
			if(getObjectFillingMulti()>0)
				tempreg=reg[1];
			if(getObjectFillingMulti()>20)
				tempreg=reg[2];
			if(getObjectFillingMulti()>40)
				tempreg=reg[3];
			if(getObjectFillingMulti()>60)
				tempreg=reg[4];
			if(getObjectFillingMulti()>80)
				tempreg=reg[5];
			
			town.gameWorld.worldSpriteBatch.draw(tempreg, pos_x(), pos_y(), width/2, height/2, width, height, sx, sy, rotation());
			
			return;
		}
		
		if(theobject.editoraction.contains("company_supermarket_shelf"))
		{
			TextureRegion tempreg=currentFrame;
			TextureRegion reg[] = theobject.objectAnimation.getKeyFrames();
			
//			if(objectFilling==0)
//				tempreg=reg[7];
//			
//			if(objectFilling>0)
//				tempreg=reg[6];
//			
//			if(objectFilling>getObjectFillingMax()/5)
//				tempreg=reg[5];
//			
//			if(objectFilling>getObjectFillingMax()/3.7)
//				tempreg=reg[4];
//			
//			if(objectFilling>getObjectFillingMax()/2.4f)
//				tempreg=reg[3];
//			
//			if(objectFilling>getObjectFillingMax()/1.7f)
//				tempreg=reg[2];
//			
//			if(objectFilling>getObjectFillingMax()/1.3f)
//				tempreg=reg[1];
//			
//			if(objectFilling>getObjectFillingMax()/1.1f)
//				tempreg=reg[0];
			
			tempreg=reg[getKeyFrame()];
			
			town.gameWorld.worldSpriteBatch.draw(tempreg, pos_x(), pos_y(), width/2, height/2, width, height, sx, sy, rotation());
			
			return;
		}		
				
		if(theobject.editoraction.contains("company_waterworks_groundwaterextractionsystem"))
		{
			if(!bObjectIsReady)
			{
				float status = iObjectIsReady/100f;
				if(status<0.2f)
					status=0.2f;

				town.gameWorld.worldSpriteBatch.setColor(1, 1, 1, status);
			}
									
			town.gameWorld.worldSpriteBatch.draw(currentFrame, pos_x(), pos_y(), width/2, height/2, width, height, sx, sy, rotation());
			
			float rotation1=0;
			if(onlineByWorkInput&&town.gameWorld.getEnoughEnergy())
				rotation1=deltaRotation;

			if(!bObjectIsReady)
				rotation1=0;
			
			town.gameWorld.worldSpriteBatch.draw(theobject.textureRegion3[0], pos_x(), pos_y(), width/2, height/2, width, height, sx, sy, rotation1*-1*1.8f);
			town.gameWorld.worldSpriteBatch.draw(theobject.textureRegion2[0], pos_x(), pos_y(), width/2, height/2, width, height, sx, sy, rotation1*-1*1.2f);
						
			town.gameWorld.worldSpriteBatch.setColor(1,1,1,0.5f);
			town.gameWorld.worldSpriteBatch.setShader(town.gameWorld.gameResourceConfig.snowShader);
			town.gameWorld.worldSpriteBatch.draw(currentFrame, pos_x(), pos_y(), width/2, height/2, width, height, 1, 1, rotation());
			town.gameWorld.worldSpriteBatch.setShader(null);
			
			return;
			
		}
		
		if(theobject.editoraction.contains("company_electricalworks_generator"))
		{
			town.gameWorld.worldSpriteBatch.draw(theobject.textureRegion[0], pos_x(), pos_y(), width/2, height/2, width, height, sx, sy, rotation());
			
			if(onlineByWorkInput)
			{
				int objsize=260;
				town.gameWorld.worldSpriteBatch.setColor(1,1,1,0.4f);
				
				int xm = rand.nextInt(5);
				int xm2 = rand.nextInt(5);
				int ym = rand.nextInt(5);
				int ym2 = rand.nextInt(5);
				
				int xm3 = rand.nextInt(10);
				int xm4 = rand.nextInt(10);
				int ym3 = rand.nextInt(10);
				int ym4 = rand.nextInt(10);
				
				if(town.gameWorld.worldPause)
				{
					xm = 0;
					xm2 = 0;
					ym = 0;
					ym2 = 0;
					
					xm3 = 0;
					xm4 = 0;
					ym3 = 0;
					ym4 = 0;					
				}
				
				town.gameWorld.worldSpriteBatch.draw(theobject.textureRegion[1], pos_x()+38+xm-xm2, pos_y()+45+ym-ym2, width/2-40-xm3+xm4, height/2-40+ym3-ym4, width-100+xm-xm2, height-100+ym-ym2, 1, 1, deltaRotation*3);
			}
			
			return;
		}
	}
	
	public void handleLights()
	{
		if(getEnergyConsumption()>0)
		{
			if(plight!=null)
			{
				if(isActiveByEnergyConsumption() && bObjectIsReady)
				{
					plight.setActive(true);
				}
				else
				{
					plight.setActive(false);
				}
			}
			
			if(clight!=null)
			{
				if(isActiveByEnergyConsumption() && bObjectIsReady)
				{
					clight.setActive(true);
					
					if(theobject.editoraction.contains("_tv") && tempcount<1)
						clight.setActive(false);
				}
				else
				{
					clight.setActive(false);
				}
			}
		}
		
		if(theobject.editoraction.contains("traffic_car"))
		{
			if(clight!=null && clight2!=null)
			{
				if(isOccupiedBy!=null)
				{
					clight.setActive(true);
					clight2.setActive(true);
				}
				else
				{
					clight.setActive(false);
					clight2.setActive(false);
				}
			}
		}
	}
	
	public void clearOccupied(CWorldObject human)
	{
		if(isOccupiedBy!=null)
		{
			if(human.uniqueId==isOccupiedBy.uniqueId)
			{
				//bIsOccupied=false;
				isOccupiedBy=null;
				iOccupied1_Arrived=0;
			}
		}
		
		if(isOccupiedBy2!=null)
		{
			if(human.uniqueId==isOccupiedBy2.uniqueId)
			{
				//bIsOccupied2=false;
				isOccupiedBy2=null;
			}
		}

		if(isOccupiedBy3!=null)
		{
			if(human.uniqueId==isOccupiedBy3.uniqueId)
			{
				//bIsOccupied3=false;
				isOccupiedBy3=null;
			}
		}
		if(isOccupiedBy4!=null)
		{
			if(human.uniqueId==isOccupiedBy4.uniqueId)
			{
				isOccupiedBy4=null;
			}
		}
		if(isOccupiedBy5!=null)
		{
			if(human.uniqueId==isOccupiedBy5.uniqueId)
			{
				isOccupiedBy5=null;
			}
		}
		if(isOccupiedBy6!=null)
		{
			if(human.uniqueId==isOccupiedBy6.uniqueId)
			{
				isOccupiedBy6=null;
			}
		}
		if(isOccupiedBy7!=null)
		{
			if(human.uniqueId==isOccupiedBy7.uniqueId)
			{
				isOccupiedBy7=null;
			}
		}
		if(isOccupiedBy8!=null)
		{
			if(human.uniqueId==isOccupiedBy8.uniqueId)
			{
				isOccupiedBy8=null;
			}
		}
		if(isOccupiedBy9!=null)
		{
			if(human.uniqueId==isOccupiedBy9.uniqueId)
			{
				isOccupiedBy9=null;
			}
		}
		
		
		if(isOccupiedByExtern!=null)
		{
			if(human.uniqueId==isOccupiedByExtern.uniqueId)
			{
				//bIsOccupiedByExtern=false;
				iOccupiedExtern_Arrived=0;
				isOccupiedByExtern=null;
			}
		}		
	}
	
	public void clearNotUsedOccupied()
	{
		//Implementiert wegen Problem bei Doctor Action: treatment chair war occupied von resident der action beendet hatte
		
		if(theobject.editoraction.contains("laundrybasket") ||
				theobject.editoraction.contains("laundryroom_washingmachine") ||
				theobject.editoraction.contains("laundryroom_dryer")  ||
				theobject.editoraction.contains("parkingspace") ||
				theobject.editoraction.contains("garage")
				)
		{
			//actionvars und occupied nicht zurücksetzen da objekte sonst "kaputt" sind
			return;
		}
		
		if(isOccupiedBy!=null && isOccupiedBy.activeAction==null)
			isOccupiedBy=null;
		if(isOccupiedBy2!=null && isOccupiedBy2.activeAction==null)
			isOccupiedBy2=null;
		if(isOccupiedBy3!=null && isOccupiedBy3.activeAction==null)
			isOccupiedBy3=null;
		if(isOccupiedBy4!=null && isOccupiedBy4.activeAction==null)
			isOccupiedBy4=null;
		if(isOccupiedBy5!=null && isOccupiedBy5.activeAction==null)
			isOccupiedBy5=null;
		if(isOccupiedBy6!=null && isOccupiedBy6.activeAction==null)
			isOccupiedBy6=null;
		if(isOccupiedBy7!=null && isOccupiedBy7.activeAction==null)
			isOccupiedBy7=null;
		if(isOccupiedBy8!=null && isOccupiedBy8.activeAction==null)
			isOccupiedBy8=null;
		if(isOccupiedBy9!=null && isOccupiedBy9.activeAction==null)
			isOccupiedBy9=null;
		if(isOccupiedByExtern!=null && isOccupiedByExtern.activeAction==null)
			isOccupiedByExtern=null;
	}

	public void resetActionTimeChecks()
	{
		int whours = town.gameWorld.worldTime.hours;
		
		if(actiontime1==22 && whours==0)
			actiontime1check=false;
		else if(actiontime1==23 && whours==1)
			actiontime1check=false;
		else if(whours==actiontime1+2)
			actiontime1check=false;
		
		if(actiontime2==22 && whours==0)
			actiontime2check=false;
		else if(actiontime2==23 && whours==1)
			actiontime2check=false;
		else if(whours==actiontime2+2)
			actiontime2check=false;
		
		if(actiontime3==22 && whours==0)
			actiontime3check=false;
		else if(actiontime3==23 && whours==1)
			actiontime3check=false;
		else if(whours==actiontime3+2)
			actiontime3check=false;
	}
	
	public void checkSwimming()
	{
		bSwimming=false;
		
		//Resident im Wasser
		int day = town.gameWorld.worldTime.day;
		if(isHuman() && day!=11 && day!=12 && day!=1 && day!=2)
		{
			for(CWorldObject wobj : town.gameWorld.worldWaterObjects)
			{
				if(wobj.testpoint(pos_x()+width_human()/2, pos_y()+height_human()/2, IntersectionMode.COLLISION_WATER))
				{
					bSwimming=true;
					break;
				}
			}
		}
	}
	
	public void setMipMapping()
	{
		theobject.setMipMapping();
	}
	
	public void handleObject()
	{
	
		//if(thehuman!=null && thehuman.sick>0 && thehuman.sickType==2)
		//	news_countHumanContagious++;
		
		if(thehuman!=null && theaddress==null)
		{
			//Adr geht verloren zb nach savegame load -> remove bed, car und tasks
			if(thehuman.bed!=null)
			{
				town.gameWorld.removeOwner(this, thehuman.bed);
				town.gameWorld.removeOwner(this, thehuman.car);
			}
			
			removeResidentialTasks();
		}
		
		
		//if(1==1)
		//	return;		
		
		if(isOwnerObject()>0 || theobject.editoraction.contains("diningroom_diningtable"))
		{
			if(owner!=null && (owner.bIsDead || iZombie>=1))
				owner=null;
			if(owner2!=null && (owner2.bIsDead || iZombie>=1))
				owner2=null;
			if(owner3!=null && (owner3.bIsDead || iZombie>=1))
				owner3=null;
			if(owner4!=null && (owner4.bIsDead || iZombie>=1))
				owner4=null;
			if(owner5!=null && (owner5.bIsDead || iZombie>=1))
				owner5=null;
			if(owner6!=null && (owner6.bIsDead || iZombie>=1))
				owner6=null;
			if(owner7!=null && (owner7.bIsDead || iZombie>=1))
				owner7=null;
			if(owner8!=null && (owner8.bIsDead || iZombie>=1))
				owner8=null;
		}
		
		if(bIsDead || iZombie>=1)
		{
			thehuman.taskobjects.clear();
			thehuman.workplaces.clear();
		}
		
		if(theobject.editoraction.contains("illuminati_defensesystem"))
			town.gameWorld.countIlluminatiDefenseSystems++;
		
		//Reset Actiontime Checks 2 Stunden nach Actiontime
		resetActionTimeChecks();
		//--------------------------------------------------
		
	

		
		checkZombieDefense();
		
		renderTime2+=CHelper.getDeltaSeconds(town);
		if(renderTime2>100000)
			renderTime2=0;
		
		checkSwimming();

		//if(theobject.editoraction.contains("illuminati_defensewarningsystem") && bDefenseAlarm)
		//	bDrawObjectByCamera = town.gameCam.frustum.boundsInFrustum(pos_x()+width/2, pos_y()+height/2, 0, getDefense_reichweite()/2, getDefense_reichweite()/2, 0);
		//else
		//	bDrawObjectByCamera = town.gameCam.frustum.boundsInFrustum(pos_x()+width/2, pos_y()+height/2, 0, width, height, 0); //wegen footpath, road /2 weg
		//	//bDrawObjectByCamera = gameWorld.gameCamera.frustum.boundsInFrustum(pos_x()+width/2, pos_y()+height/2, 0, width/2, height/2, 0);
					
		if(theobject.editoraction.contains("bird"))
			town.gameWorld.birdcount++;
		
		if(town.bDevMode)
		{
			if(Gdx.input.isKeyJustPressed(Input.Keys.O))
				doObjectAction=!doObjectAction;
			
			if(belongsToCompany!=null)
				belongsToCompany.addWorkOutput(1000, WorkoutputType.DEFAULT);
			
    		if(Gdx.input.isKeyJustPressed(Keys.Q))
    		{
    			actionanim1++;
    			if(actionanim1>2)
    				actionanim1=0;
    		}
		}
		
		if(!town.gameWorld.worldPause)
		{
			deltaSecond+=Gdx.graphics.getDeltaTime();
			deltaRotation+=CHelper.getDeltaSeconds(town)-0.8f;
			renderHour+=CHelper.getDeltaSeconds(town);
			render4Hour+=CHelper.getDeltaSeconds(town);
			render2Hour+=CHelper.getDeltaSeconds(town);
			renderMinute+=CHelper.getDeltaSeconds(town);
			renderAnim+=CHelper.getDeltaSeconds(town);
			
			if(activeActionMode == CAction.ActionMode.WORKPLACE && activeAction!=null && activeAction.bActionMode)
				workHour+=CHelper.getDeltaSeconds(town);
		}
		
		clearNotUsedOccupied();
				
		//Essen wird schlecht
		if(theobject.editoraction.contains("fridge"))
		{
			if(renderHour>3600)
			{
				if(getObjectFillingMultiMax()>0)
				{
					addObjectFillingMulti(0, -1);
				}
			}
		}
		
		CStatisticsData_Other stat = town.gameWorld.townStatistics.getCurrentStatistics_Other();
		
		if(theobject.editoraction.contains("road_road_parkingspace"))
			stat.temp_countParkingspace++;
		if(theobject.editoraction.contains("residential_garage"))
			stat.temp_countGarage++;
		if(theobject.editoraction.contains("traffic_car_residential"))
			stat.temp_countVehiclesPrivate++;
		if(theobject.editoraction.contains("traffic_car") && theobject.editoraction.contains("company"))
			stat.temp_countVehiclesPublic++;
				
		if(isHuman() && bIsDead)
		{
			//Eigene Liste für verstorbene Residents, die dynamisch aufgebaut wird
			//z.B. für Hearse Action
			//theobject.zorder=2; //lebende residents laufen nicht unter toten residents durch
			
			//hier problem
			//gameWorld.tempHumansDead.remove(uniqueId); //concurrent modification exception
			
			if(iZombie<1)
			{
				if(!town.gameWorld.tempHumansDead.containsKey(uniqueId))
					town.gameWorld.tempHumansDead.put(uniqueId, this);
			}
		}
		
		if(town.bNoRequirements)
		{
			//Fuel

			//Workoutput
			if(belongsToCompany!=null)
			{
				belongsToCompany.addWorkOutput(1000, WorkoutputType.DEFAULT);
				
				if(belongsToCompany.getWorkOutput(WorkoutputType.DEFAULT)<400)
					belongsToCompany.addWorkOutput(500, WorkoutputType.DEFAULT);
				
				if(belongsToCompany.getWorkOutput(WorkoutputType.FINANCE)<400)
					belongsToCompany.addWorkOutput(500, WorkoutputType.FINANCE);
				
				if(belongsToCompany.getWorkOutput(WorkoutputType.OTHER)<400)
					belongsToCompany.addWorkOutput(500, WorkoutputType.OTHER);

				if(belongsToCompany.getWorkOutput(WorkoutputType.INTELLIGENCE)<400)
					belongsToCompany.addWorkOutput(500, WorkoutputType.INTELLIGENCE);
				
				if(belongsToCompany.getWorkOutput(WorkoutputType.POPULATION)<400)
					belongsToCompany.addWorkOutput(500, WorkoutputType.POPULATION);
			}
		}
		
		if(!town.gameWorld.worldPause)
		{
			if(thehuman!=null)
			{
				thehuman.handleAttributeValues();
				
				//Fitness erhöht sich beim Laufen
				if(renderMinute>60)
				{
					if(thehuman!=null && ziel_x>0 && goByCar_X<1)
					{
						if(thehuman.getFitnessValue()<50)
							thehuman.changeFitnessValue(0.016f);
					}
				}			
			}
		}
		
		//zähle bäume, blumen in der nähe von adrs
		if(theobject.editoraction.contains("outdoor_tree") || theobject.editoraction.contains("outdoor_plant") || theobject.editoraction.contains("outdoor_flower"))
		{
			if(theaddress!=null)
				theaddress.countFloraTemp++;
			
			for(CAddress adr : town.gameWorld.worldAddressList)
			{
				if(adr.getMinDistanceToAddress(pos_x(), pos_y())<800)
				{
					adr.tempList_GreenTown.add(this);
				}
			}
		}


		if(bObjectIsReady)
		{
			if(renderMinute>60)
			{
				handleOther();
				handleObjectDependencies();
			}
			
			handleConsumerPlaces(); //nicht nur pro stunde prüfen
					
			//if(!gameWorld.worldPause) pause darf hier nicht geprüft werden
			handleConsumptionValues();
			
		}

		//Lights
		handleLights();
		
		
		//Priorität der Freetime Actionlist neu sortieren
		if(town.gameWorld.worldTime.hours==0 && lastActionListSortDay!=town.gameWorld.worldTime.day)
		{
			lastActionListSortDay=town.gameWorld.worldTime.day;
			sortFreeTimeActionList();
		}
				
		//Object Condition
		if(!town.gameWorld.worldPause)
		{
			if(theobject.editoraction.contains("illuminati_defensesystem")
					|| theobject.editoraction.contains("illuminati_defensewarningsystem")
					)
			{
				if(render4Hour>14400)
				{
					if(defaultObjectCondition>0 && isMaintenanceObject())
					{
						objectCondition-=1;
						if(objectCondition<0)
							objectCondition=0;
					}
				}
			}
			else
			{
				if(render2Hour>7200)
				{
					if(defaultObjectCondition>0 && isMaintenanceObject())
					{
						objectCondition-=1;
						if(objectCondition<0)
							objectCondition=0;
					}
				}
			}
		}
		
		//Foodfilling
		if(getObjectFillingMax()>0)
		{
			if(objectFilling>getObjectFillingMax())
				objectFilling=getObjectFillingMax();
			if(objectFilling<0)
				objectFilling=0;
		}
	}
	
	public void renderTextInfo()
	{
		/*
		if(town.bConstructionMode && iObjectIsReady<100 && theobject!=null && theobject.editoraction!=null)
		{
			town.gameWorld.worldSpriteBatch.setShader(town.gameFont.fontShader);
			town.gameFont.bfArial2.getData().setScale(1);
			town.gameFont.bfArial2.setColor(Color.WHITE);
			String stext = Math.round(iObjectIsReady)+"% "; // + theobject.objectName;
			town.gameFont.layout.setText(town.gameFont.bfArial2, stext);
			town.gameFont.bfArial2.draw(town.gameWorld.worldSpriteBatch, stext, pos_x()+width/2-town.gameFont.layout.width/2, pos_y()+height/2+town.gameFont.layout.height/2);
			town.gameWorld.worldSpriteBatch.setShader(null);
		}
		*/
	}
	
//	public void render(SpriteBatch worldSpriteBatch, Boolean bDeleteMode)
//	{
//		if(bHandleActionsNow)
//		{
//	    	autoModeTimer.schedule(new TimerTask() 
//	    	{
//	            @Override public void run() 
//	            {
//	                Gdx.app.postRunnable(new Runnable() 
//	                {
//	                	@Override
//	                    public void run() {
//	                    	render(worldSpriteBatch, bDeleteMode);
//	                	}
//	                });
//	            }
//	        }, 0);
//		}
//	}
	
	public void renderDefenseZone(ShapeRenderer sr1)
	{
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
		
		if (theobject.editoraction.contains("company_waterworks_groundwaterextractionsystem"))
		{
			//ACHTUNG: Funktion gibt es auch auf CObject 
			sr1.setColor(0.0f, 0.4f, 0.0f, 0.5f);
			sr1.set(ShapeType.Filled);
			sr1.circle(pos_x()+width/2, pos_y()+height/2, getWatersystemRange());
		}
		
	}
	
	public int getWatersystemRange()
	{
		return getWaterOutput()*60;
	}
	
	public void setRoom()
	{
		//Setze room wenn keiner hinterlegt
		if(isHouseObject() && theroom==null)
		{
			if(isHouseObject() && theaddress!=null)
			{
				for(CWorldObject floor1 : theaddress.listWorldObjects_Floors)
				{
					Boolean roomOK=false;
					if(floor1.theobject.checkRoomType(theobject))
						roomOK=true;
					
					if(roomOK)
					{
						Boolean bSet=false;
						if(floor1.testpoint(pos_x()+width/2, pos_y()+height/2, IntersectionMode.DEFAULT))
							bSet=true;
						
						if(bSet)
						{
							theobject.tempRoom=floor1;
							theroom=floor1;
						}								
					}		
				}
			}
		}
	}
	
	public void render(SpriteBatch worldSpriteBatch, Boolean bDeleteMode)
	{
		//if(uniqueId==75)
		//{
		//	Gdx.app.setLogLevel(5);
		//	Gdx.app.debug("75", ""+theobject.pos_x + ", " + theobject.pos_y + ", " + pos_x() + ", " + pos_y());
		//}
		
		//if(1==1)
		//	return;
		
		if(theobject==null)
			return;
		
		//if(theobject.editoraction.contains("tree"))
		//	return;
				
		
		
		//if(theobject.editoraction.contains("dryer")) {
		//	theaddress.listWorldObjects.clear();// removeIf(item->item.uniqueId==this.uniqueId);
		//	theaddress.listWorldObjects.add(this);
			//theaddress.listWorldObjects.add(this);
				/*
			Gdx.app.debug("", "start for " + theaddress.addressName);
			for(CWorldObject obj : theaddress.listWorldObjects){
				if(theobject.editoraction.contains("dryer")) {
					Gdx.app.debug("", "dryer is in list " + this.uniqueId);
				}
			}
			Gdx.app.debug("", "end for " + theaddress.addressName);
			*/
		//}
		
		
		setRoom();
		
		bDrawObjectZoom=true;
		if(town.gameCam.zoom > town.frameBufferZoom 
				&& !town.gameWorld.bRenderFrameBuffer
				&& !theobject.editoraction.contains("traffic_car")
				&& !town.gameWorld.bCloneAddressRender
				&& !isHuman()
			)
		{
			bDrawObjectZoom=false;
		}
		
		if(isHuman() && town.gameCam.zoom > town.frameBufferZoom_Residents)
			bDrawObjectZoom=false;
		
		if(theobject.isCar && town.gameCam.zoom > town.frameBufferZoom_Cars)
			bDrawObjectZoom=false;
		
		//if(!town.gameWorld.bRenderFrameBuffer)
		//	return;
		//	bDrawObjectZoom=false;
		
		//if(theobject.editoraction.contains("groundwaterextractionsystem"))
		//	Gdx.app.debug("", "bDrawObjectZoom: "+bDrawObjectZoom + ", zoom: " + town.gameCam.zoom);
		
		//if(town.gameCam.zoom > town.frameBufferZoom_norender)
		//{
		//	bDrawObjectZoom=false;
		//}
		
		//if(isHuman())
		//	Gdx.app.debug("", "town.gameCam.zoom: " +  town.gameCam.zoom + ", town.gameWorld.bRenderFrameBuffer: " + town.gameWorld.bRenderFrameBuffer + ", bDrawObject: " + bDrawObject);
		
		//Gdx.app.debug("", "town.gameWorld.getEnergyConsumption(): " + town.gameWorld.getEnergyConsumption() + ", town.gameWorld.getEnergyOutput(): " + town.gameWorld.getEnergyOutput());
		
//		if(theobject.editoraction.contains("fridge"))
//		{
//			objectFilling=0;
//			objectFillingMulti.put(0,0);
//		}
		
		//if(uniqueId==26)
		//	thehuman.changeHealthValue(-1000);
		
		//if(uniqueId==2 && activeAction!=null && activeAction.targetActionObject!=null)
		//{
		
		//	Gdx.app.debug("", "" + activeAction.actionMode + ", "+activeAction.targetActionObject.theobject.editoraction);
		//}
		
		//if(theobject.editoraction.contains("bird"))
		//	return;
		
		//if(uniqueId==26)
		//	objectFillingMulti.put(0,0);
		
		//actionobjects nicht in den framebuffer
		if(town.gameWorld.bRenderFrameBuffer)
		{
			if(theobject.editoraction.contains("recyclingcenter_garbagecontainer") ||
					theobject.editoraction.contains("supermarket_shoppingcart") ||
					theobject.editoraction.contains("supermarket_buyin") ||
					theobject.editoraction.contains("laundryobject") ||
					theobject.editoraction.contains("recyclingcenter_garbagebag") ||
					theobject.editoraction.contains("coffeebeans1") ||
					theobject.editoraction.contains("coffeepot1") ||
					theobject.editoraction.contains("supermarket_pallettruck") ||
					theobject.editoraction.contains("company_college_spaceship")
					)
			return;
		}
		
		
		if(!town.gameWorld.worldPause)
			timeInTown+=CHelper.getDeltaSeconds(town);
		
		if(town.bDevMode && belongsToCompany!=null)
		{
			belongsToCompany.addWorkOutput(1000, WorkoutputType.DEFAULT);
		}
		
		if(town.bDevMode)
		{
			if(theobject.editoraction.toLowerCase().contains("recyclingcenter_garbagecontainer"))
				objectFilling=getObjectFillingMax();
		}
		
		bHandleActionsNow=false;
		
		try
		{
			//Im Winter gibt es keine sehr kleinen Birds
			if(theobject.editoraction.contains("bird"))
			{
				if(town.gameWorld.worldTime.day==11 || town.gameWorld.worldTime.day==12 || town.gameWorld.worldTime.day==1 || town.gameWorld.worldTime.day==2)
				{
					if(width<30)
						return;
				}
			}
			
			setMipMapping();
			
			if(theobject.editoraction.contains("illuminati_defensewarningsystem") && bDefenseAlarm)
				bDrawObjectByCamera = town.gameCam.frustum.boundsInFrustum(pos_x()+width/2, pos_y()+height/2, 0, getDefense_reichweite()/2, getDefense_reichweite()/2, 0);
			else
				bDrawObjectByCamera = town.gameCam.frustum.boundsInFrustum(pos_x()+width/2, pos_y()+height/2, 0, width, height, 0); //wegen footpath, road /2 weg

	
			//if(1==1)
			//	return;		
			
			if(!town.gameWorld.worldPause && town.gameWorld.bHandleObject)
			{
				handleObject();
			}
		
			if(town.gameWorld.worldPause)
			{
				town.gameAudio.stopSound("EFFECT_ALARM");
			}
		
			
		//worldSpriteBatch.end();
		//gameWorld.renderPath(path);
		//worldSpriteBatch.begin();
		
		worldSpriteBatch.setColor(1, 1, 1,town.gameWorld.alphaWorldObjects);
		
        //TextureRegion currentFrame = theobject.textureRegion[0];
		currentFrame = theobject.textureRegion[0];
        if(thehuman!=null && thehuman.gender=='w' && thehuman.getAge()<14) //Oberweite erst ab 14
        	currentFrame=theobject.textureRegion2[0];
        
        TextureRegion currentFrame2 = null;
        TextureRegion currentFrame3 = null;
        TextureRegion currentFrame4 = null;
        
        Boolean bMoving = false;
        
        if(!town.gameWorld.worldPause)
        {
        	if(isHuman() || theobject.isCar || theobject.editoraction.contains("bird"))
        		if(!town.gameWorld.bRenderFrameBuffer)
        			bMoving = pathFinding(town.gameWorld.stateTime);
        	        	
        	//Ansteckende Krankheit

        	if(!town.gameWorld.bRenderFrameBuffer)
        	{
        	
        	if(isHuman() && thehuman.sick>0 && thehuman.sickType==2)
        	{
        		for(CWorldObject hobj : town.gameWorld.worldHumans)
        		{
        			if(hobj.thehuman.sick>0)
        				continue;
        			
        			if(hobj.uniqueId!=uniqueId && !hobj.bIsDead)
        			{
        				if(!hobj.thehuman.getAllJobsTitleString().contains("doctor")) //nicht doctor anstecken
        				{
	        				int dist = CHelper.getEuclidianDistance(this, hobj);
	        				if(dist<100)
	        				{
	        					hobj.thehuman.sickType=2;
	        					hobj.thehuman.sick=30+rand.nextInt(70);
	        				}
        				}
        			}
        		}
        	}
        	}
        	
        	if(!bJumpOut)
        	{
        		if(isHuman() || theobject.editoraction.contains("bird"))
        		{
        			//Async verursacht ConcurrentModificationException wenn Action ausgeführt wird und Liste modifiziert und gleichzeitig durchlaufen wird
        			//Test mit 600 Residents: hatte keine Auswirkungen auf Performance mit 1 Thread
        			/*
						remove/add garbage?
						remove/add supermarket_buyin?
						remove/add laundry
						remove/add coffe pot
						remove/add coffeebeans1        			 
        			*/
        			
        			//Bei nur einer adresse bringt das einen performanceschub, aber vorher muss alles thread tauglich gemacht werden siehe oben 
        			
        			//if(town.bNoRealEstate)
        			//{
		        	//	if(asyncResult==null || asyncResult.isDone())
		        	//	{
		        	//		if(!town.gameWorld.bRenderFrameBuffer)
		        	//			asyncResult = town.gameWorld.asyncExecutor.submit(this);
		        	//	}
        			//}
        			
        			if(!town.gameWorld.bRenderFrameBuffer)
        				handleActions(town.gameWorld.stateTime);
        		}
        	}
        }
        
        if(!bIsDead)
        {
	        if(theobject.editoraction.contains("bird") && !town.gameWorld.worldPause) //immer am moven
	        {
	        	currentFrame = theobject.objectAnimation.getKeyFrame(town.gameWorld.stateTime, true);
	        }
	        else if((bMoving || actionanim1==101) && theobject.objectAnimation!=null && !town.gameWorld.worldPause)
        	{
	        	//Achtung: beeinflusst Shadow Darstellung
	        	if(thehuman!=null)
	        	{
	        		if(thehuman.gender=='w' && thehuman.getAge()<14) //Oberweite erst ab 14
	        		{
	        			currentFrame = theobject.objectAnimation2.getKeyFrame(town.gameWorld.stateTime*(1+objectAnimSpeedModifier), true);
	        		}
	        		else
	        			currentFrame = theobject.objectAnimation.getKeyFrame(town.gameWorld.stateTime*(1+objectAnimSpeedModifier), true);
	        		
        			if(thehuman.gender=='w')
        			{
        				currentFrame2 = town.gameResourceConfig.animations.get("human_woman_arms").getKeyFrame(town.gameWorld.stateTime, true);
        			}
        			else
        			{
        				currentFrame2 = town.gameResourceConfig.animations.get("human_man_arms").getKeyFrame(town.gameWorld.stateTime, true);
        			}
        			
        			currentFrame3 = town.gameResourceConfig.animations.get("human_man_legs").getKeyFrame(town.gameWorld.stateTime, true);
        			currentFrame4 = town.gameResourceConfig.animations.get("human_man_shoes").getKeyFrame(town.gameWorld.stateTime, true);
	        	}
	        }
	        else
	        {
	        	//..
	        }
        }
        
        /*
        if(town.fSizeFactor>0)
        {
        	if(currentFrame!=null)
        	{
        		currentFrame.setRegionWidth((int) (currentFrame.getRegionWidth()/town.fSizeFactor));
        		currentFrame.setRegionHeight((int) (currentFrame.getRegionHeight()/town.fSizeFactor));
        	}

        	if(currentFrame2!=null)
        	{
	        	currentFrame2.setRegionWidth((int) (currentFrame2.getRegionWidth()/town.fSizeFactor));
	        	currentFrame2.setRegionHeight((int) (currentFrame2.getRegionHeight()/town.fSizeFactor));
        	}

        	if(currentFrame3!=null)
        	{
        		currentFrame3.setRegionWidth((int) (currentFrame3.getRegionWidth()/town.fSizeFactor));
        		currentFrame3.setRegionHeight((int) (currentFrame3.getRegionHeight()/town.fSizeFactor));
        	}

        	if(currentFrame4!=null)
        	{
	        	currentFrame4.setRegionWidth((int) (currentFrame4.getRegionWidth()/town.fSizeFactor));
	        	currentFrame4.setRegionHeight((int) (currentFrame4.getRegionHeight()/town.fSizeFactor));
        	}
        }
        */
        
		Boolean bShowDel=false;
		
		if(bObjMoving) //moving wird im worldobjectrender geprüft im gegensatz zum placing -> renderPlacing
		{
			if(!town.gameWorld.checkObjectPlacing(this.theobject, null))
			{
				//bShowDel=true; //gibt probleme mit shadow bei road -> sieht merkwürdig aus wenn nur shadow sichtbar aber road nicht
				town.gameGui.bRenderForbidden=true;
			}
			else
				town.gameGui.bRenderForbidden=false;
		}
		


		
        //Schatten - Shadow
        if(bDrawObject && bDrawObjectByCamera && bDrawObjectZoom)
        {
        	
    		

        	
	        if(!theobject.editoraction.contains("road_road_road") 
	        		&& !theobject.editoraction.contains("flora_") 
	        		&& !theobject.isGroundObject
	        		&& !theobject.isGroundBaseObject
	        		//&& !theobject.editoraction.contains("residential_garage")
	        		&& !theobject.editoraction.contains("road_road_footpath")
	        		)
	        {
	        		//wird selbst aus world render aufgerufen
	        		Boolean renderTheShadow=true;
	        		if(theobject.isRoomObject && !town.gameWorld.bCloneAddressRender)
	        			renderTheShadow=false;
	        		
	        		if(renderTheShadow)
	        		{
		        			if(town.gameWorld.bRenderFrameBuffer || isHuman() || theobject.editoraction.contains("bird") || theobject.isDrawSpecial() || town.gameWorld.bCloneAddressRender)
		        			{
		        				Boolean brend=false;
		        				if(town.gameCam.zoom<=town.zoomRender_noShadows)
		        					brend=true;
		        				if(town.gameWorld.bRenderFrameBuffer)		        				
		        					brend=true;
		        				
		        				//if(brend)
		        				drawShadows(worldSpriteBatch, currentFrame);
		        			}
	        		}
	        }
	        
	        /*
			if(isAddressObject())
			{
				if(theobject.ATTR_BOUNDX>0 || theobject.ATTR_BOUNDY>0)
				{
					if(town.gameGui.bObjPlacing && town.gameGui.objPlacing!=null)
					{
						CAddress adr = town.gameWorld.getAddressByPoint(town.gameGui.objPlacing.pos_x+town.gameGui.objPlacing.width/2, town.gameGui.objPlacing.pos_y+town.gameGui.objPlacing.height/2);
						if(adr!=null && theaddress.addressId == adr.addressId)
						{
							theobject.pos_x = pos_x();
							theobject.pos_y = pos_y();
							town.gameWorld.worldSpriteBatch.end();
							theobject.drawBoundingPolygon(town.gameWorld.shapeRenderer1);
							town.gameWorld.worldSpriteBatch.begin();
						}
					}
				}
			}		        
	        */
	        
        }
        
		int mx = Gdx.input.getX();
		int my = Gdx.input.getY();
		
		Boolean bShowSell=false;
		
		if(bDeleteMode) //render nur für delteobject
		{
			bShowSell=true;
		}
		else
		{
			if(!town.gameGui.bMouseActionMode || town.gameGui.bDeletemode || town.gameGui.bPaintObject)
			{
		        Vector3 c0 = new Vector3(mx, my, 0);
		        Vector3 c1 = town.gameCam.unproject(c0);
		        mx=(int) c1.x;
		        my=(int) c1.y;
		        
		        if(testpoint(mx, my, IntersectionMode.MOUSECLICK))
				{
		        	if(town.gameGui.bDeletemode==true && thehuman==null && !theobject.editoraction.contains("bird"))// && box2dBody!=null)
		        		town.gameGui.deleteObject=this;
		        	
		        	town.gameWorld.mouseOverObject=this;
				}
			}
		}
		
		if(!town.gameWorld.bRenderFrameBuffer)
			renderSpriteMoveEvents(0);
		
		Boolean bDrawByHPos=true;

		if(town.gameCam.zoom<hposition)
			bDrawByHPos=false;

		
		
		
		if(bDrawObject && bDrawObjectByCamera && bDrawObjectZoom && bDrawByHPos)
		{
			

			
			if(theobject.isHuman())
			{
				drawHuman(worldSpriteBatch, currentFrame, currentFrame2, currentFrame3, currentFrame4, bShowDel);
			}
			else
			{
				float scale=1f;
				
				worldSpriteBatch.setColor(1, 1, 1,town.gameWorld.alphaWorldObjects);
				
				if(color1!=null)
					worldSpriteBatch.setColor(color1);
				if(actionColor1!=null)
					worldSpriteBatch.setColor(actionColor1);
				
				if(bShowDel)
					worldSpriteBatch.setColor(1, 0, 0, 0.4f);
				
				//if(!Gdx.gl.glIsEnabled(GL30.GL_BLEND))
				{
					Gdx.gl.glEnable(GL30.GL_BLEND);
					Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
				}
		    			    	
				//Draw Placing Abhängigkeiten als Rect
				//if(town.gameGui.objPlacing!=null && town.gameGui.objPlacing.objectId.equals(theobject.objectId)) //performance opt1 so kann man das nicht machen, da hier alle objekte durchlufen werden müssen
				
				worldSpriteBatch.end(); //immer Benötigt, da sonst crash gfx erfolgt
//				if(town.gameGui.objPlacing!=null)
//				{
//			    	gameWorld.shapeRenderer1.setAutoShapeType(true);
//			    	gameWorld.shapeRenderer1.begin();
//			    	gameWorld.shapeRenderer1.set(ShapeType.Filled);
//					drawPlacingRect(gameWorld.shapeRenderer1);
//					gameWorld.shapeRenderer1.end();
//				}
				worldSpriteBatch.begin();
				
				
				//if(theobject.editoraction.contains("groundwaterextractionsystem"))
				//	Gdx.app.debug("", "town.gameWorld.bRenderFrameBuffer : " + town.gameWorld.bRenderFrameBuffer);
				
				if(!bDeleteMode 
						&& !town.gameWorld.bRenderFrameBuffer 
						&& !theobject.editoraction.contains("bird")
								&& (!theobject.isDrawSpecial()||theobject.editoraction.contains("ground_water"))
								&& !town.gameWorld.bCloneAddressRender
								&& !theobject.editoraction.contains("light")
								&& bObjectIsReady
					)
				{
					//if(theobject.editoraction.contains("illuminati_defensewarningsystem"))
					//	Gdx.app.debug("", "gameWorld.bRenderFrameBuffer : " + gameWorld.bRenderFrameBuffer + ", theobject.isDrawSpecial(): " + theobject.isDrawSpecial() + ", gameWorld.bCloneAddressRender: " + gameWorld.bCloneAddressRender);
				
					/*
					if(theobject.editoraction.contains("zombie_entrance"))
					{
						Gdx.app.setLogLevel(10);
						Gdx.app.debug("", "render");
					}
					*/	
					
					if(!town.gameWorld.bRenderFrameBuffer)
						renderSpriteMoveEvents(1);
					
					if(!town.gameWorld.bRenderFrameBuffer)
						if(bDrawObject && bDrawObjectByCamera && bDrawObjectZoom)
							drawWarnings(worldSpriteBatch, 0);
					
					if(!town.gameWorld.bRenderFrameBuffer)
						handleWorkoutput();
					
					
					if(deltaRotation>360)
						deltaRotation=0;
						
					if(deltaSecond>1)
						deltaSecond=0;
									
					if(workHour>3600)
						workHour=0;
					
					if(renderHour>3600)
						renderHour=0;
					
					if(render4Hour>14400)
						render4Hour=0;

					if(render2Hour>7200)
						render2Hour=0;
					
					if(renderMinute>60)
						renderMinute=0;

					if(renderAnim>renderAnimTarget)
						renderAnim=0;
					
					drawAdditionalActionObjects();
					
					return;
				}				
				
				

				
				
				//Draw Object
				if(town.gameWorld.gameResourceConfig.isSpecialObject(theobject.editoraction) 
						|| town.gameWorld.gameResourceConfig.isOnlyDrawSpecialObject(theobject.editoraction))
				{
					//Gdx.app.debug("", ""+theobject.editoraction);
					//if(gameWorld.bRenderFrameBuffer)
					
	
					drawSpecialTexture(currentFrame, bShowDel, bShowSell);
				}
				else
				{
				
					if(iObjectIsReady<100) //Default
					{
						float status = iObjectIsReady/100f;
						if(status<0.4f)
							status=0.4f;
						if(theobject.editoraction.contains("road_road_road"))
							if(status<0.3f)
								status=0.3f;
						if(theobject.editoraction.contains("outdoor_ground"))
							if(status<0.5f)
								status=0.5f;
						if(theobject.editoraction.contains("residential_garage"))
							if(status<0.6f)
								status=0.6f;

						if(theobject.editoraction.contains("ground_base"))
							if(status<0.5f)
								status=0.5f;

						//if(theobject.isRoomObject)
						//Gdx.app.debug(""+uniqueId, "statusdebug: "+status + ", iObjectIsReady: " + iObjectIsReady + ", bObjectIsReady: " + bObjectIsReady);
						
						worldSpriteBatch.setColor(1, 1, 1, status);
					}
					
					if(bShowDel && !theobject.isRoomObject)
					{
						worldSpriteBatch.draw(currentFrame, pos_x(), pos_y(), width/2, height/2, width, height, scale, scale, rotation());
					}
					else
					{
						//if(theobject.editoraction.contains("interior_light"))
						//	Gdx.app.debug("", "interior2");
						
	
						//Gdx.app.setLogLevel(10);
						//if(theobject.editoraction.contains("paceship"))
						//	Gdx.app.log("", "test_3");

						
						//worldSpriteBatch.setShader(gameWorld.gameResourceConfig.snowShader);
						if(town.gameWorld.bRenderFrameBuffer || theobject.editoraction.contains("bird") || theobject.isDrawSpecial() || town.gameWorld.bCloneAddressRender || theobject.editoraction.contains("light") || !bObjectIsReady)
						{
							//Gdx.app.setLogLevel(10);
							//if(theobject.editoraction.contains("paceship"))
							//	Gdx.app.log("", "test_3");
							
							worldSpriteBatch.draw(currentFrame, pos_x(), pos_y(), width/2, height/2, width, height, scale+hposition/1.8f, scale+hposition/1.8f, rotation());
						}
						//worldSpriteBatch.setShader(null);
						
						if(town.gameWorld.town.bDrawTVZoneBoundingPolygons)
						{
							worldSpriteBatch.end();
							town.gameWorld.shapeRenderer1.begin();
							
							if(theobject.editoraction.contains("_tv") || theobject.editoraction.contains("_couch") || theobject.editoraction.contains("_armchair"))
								town.gameWorld.shapeRenderer1.polygon(theobject.getTVZonePolygon().getTransformedVertices());
							
							town.gameWorld.shapeRenderer1.end();
							worldSpriteBatch.begin();
						}
						
						if(town.gameWorld.town.bDrawBathmatZoneBoundingPolygons)
						{
							worldSpriteBatch.end();
							town.gameWorld.shapeRenderer1.begin();
							
							if(theobject.editoraction.contains("bathroom_bathmat") || 
									theobject.editoraction.contains("bathroom_shower") || 
									theobject.editoraction.contains("bathroom_bathtub"))
								town.gameWorld.shapeRenderer1.polygon(theobject.getBathmatZonePolygon().getTransformedVertices());
							
							town.gameWorld.shapeRenderer1.end();
							worldSpriteBatch.begin();
						}
						
						if(town.gameWorld.town.bDrawSchoolZoneBoundingPolygons)
						{
							worldSpriteBatch.end();
							town.gameWorld.shapeRenderer1.begin();
							
							//if(theobject.editoraction.contains("company_school_workingplace_") || theobject.editoraction.contains("company_church_"))
							if(theobject.editoraction.contains("company_school_workingplace_"))
								town.gameWorld.shapeRenderer1.polygon(theobject.getSchoolDeskZonePolygon().getTransformedVertices());
							
							town.gameWorld.shapeRenderer1.end();
							worldSpriteBatch.begin();
						}
												
						if(town.gameWorld.town.bDrawCollegeZoneBoundingPolygons)
						{
							worldSpriteBatch.end();
							town.gameWorld.shapeRenderer1.begin();
							
							if(theobject.editoraction.contains("company_college_workingplace_"))
								town.gameWorld.shapeRenderer1.polygon(theobject.getSchoolDeskZonePolygon().getTransformedVertices());
							
							town.gameWorld.shapeRenderer1.end();
							worldSpriteBatch.begin();
						}
						
						if(town.gameWorld.town.bDrawBedZonePolygons)
						{
							worldSpriteBatch.end();
							town.gameWorld.shapeRenderer1.begin();
							
							if(theobject.editoraction.contains("bedroom_bed"))
								town.gameWorld.shapeRenderer1.polygon(theobject.getBedZonePolygon().getTransformedVertices());
							
							town.gameWorld.shapeRenderer1.end();
							worldSpriteBatch.begin();
						}
						
						if(town.gameWorld.town.bDrawInteriorDecorZonePolygons)
						{
							worldSpriteBatch.end();
							town.gameWorld.shapeRenderer1.begin();
							
							if(theobject.editoraction.contains("_carpet") || theobject.editoraction.contains("_plant"))
								town.gameWorld.shapeRenderer1.polygon(theobject.getInteriorDecorZonePolygon().getTransformedVertices());
							
							town.gameWorld.shapeRenderer1.end();
							worldSpriteBatch.begin();
						}
						
						if(town.gameWorld.town.bDrawNighttableZonePolygons)
						{
							worldSpriteBatch.end();
							town.gameWorld.shapeRenderer1.begin();
							
							if(theobject.editoraction.contains("bedroom_bed"))
							{
								town.gameWorld.shapeRenderer1.polygon(theobject.getZonePolygonForNighttablePlacement(0).getTransformedVertices());
								town.gameWorld.shapeRenderer1.polygon(theobject.getZonePolygonForNighttablePlacement(1).getTransformedVertices());
							}
							
							town.gameWorld.shapeRenderer1.end();
							worldSpriteBatch.begin();
						}
						
						
						
						if(town.gameWorld.town.bDrawBoundingPolygons)
						{
							//if(worldSpriteBatch.isDrawing())
							//	worldSpriteBatch.end();
							town.gameWorld.shapeRenderer1.begin();
							
							town.gameWorld.shapeRenderer1.polygon(getBoundingPolygon(IntersectionMode.COLLISION).getTransformedVertices());
														
							//if(theobject.editoraction.contains("radiator"))
							//	town.gameWorld.shapeRenderer1.polygon(theobject.getRadiatorZonePolygon().getTransformedVertices());
							
							//if(theobject.editoraction.contains("_light"))
							//{
							//	Circle c = theobject.getLightZoneCircle();
							//	town.gameWorld.shapeRenderer1.circle(c.x, c.y, c.radius);
							//}
							
							town.gameWorld.shapeRenderer1.end();
							//worldSpriteBatch.begin();
						}
						
						drawAdditionalActionObjects();
					}
				}
				
				//Draw winter, schnee, snow
				if(!bShowDel && !isHouseObject() && !theobject.isRoomObject)
				{
					int day=town.gameWorld.worldTime.day;
					Boolean bWhite=false;
					
					if(day==11 || day==12 || day==1 || day==2)
						bWhite=true;
					
					if(theobject.editoraction.contains("outdoor_tree") &&(day==11 || day==12 || day==1 || day==2 || day==10)) 
						bWhite=true;
					
					if(theobject.editoraction.contains("outdoor_tree"))
						bWhite=false;
					
					if(bWhite)
					{
						if((theobject.isGroundObject || theobject.isGroundBaseObject) && !theobject.isWaterObject) //max 2 für ground, da schwarzer rand
						{
							if(day==11)
								worldSpriteBatch.setColor(1,1,1,0.3f);
							if(day==12)
								worldSpriteBatch.setColor(1,1,1,0.5f);
							if(day==1)
								worldSpriteBatch.setColor(1,1,1,0.5f);
							if(day==2)
								worldSpriteBatch.setColor(1,1,1,0.3f);
							
							TextureRegion snow1 = town.gameResourceConfig.animations.get("texture_snow1").getKeyFrames()[2];
							worldSpriteBatch.draw(snow1, pos_x(), pos_y(), width/2, height/2, width, height, 1, 1, rotation());
						}
						else
						{
							if(theobject.editoraction.contains("outdoor_tree"))
							{
								if(day==10)
									worldSpriteBatch.setColor(0.3f,0.3f,0.3f,0.5f);
								if(day==11)
									worldSpriteBatch.setColor(0.8f,0.8f,0.8f,0.4f);
								if(day==12)
									worldSpriteBatch.setColor(0.8f,0.8f,0.8f,0.6f);
								if(day==1)
									worldSpriteBatch.setColor(0.8f,0.8f,0.8f,0.5f);
								if(day==2)
									worldSpriteBatch.setColor(1f,1f,1f,0.4f);
							}
							else
							{
								if(day==11)
									worldSpriteBatch.setColor(1,1,1,0.2f);
								if(day==12)
									worldSpriteBatch.setColor(1,1,1,0.4f);
								if(day==1)
									worldSpriteBatch.setColor(1,1,1,0.3f);
								if(day==2)
									worldSpriteBatch.setColor(1,1,1,0.15f);
							}
							
							worldSpriteBatch.setShader(town.gameFont.fontShader);
							worldSpriteBatch.draw(currentFrame, pos_x(), pos_y(), width/2, height/2, width, height, 1, 1, rotation());
							worldSpriteBatch.setShader(null);
						}
					}
				}
			}
		}
		
		
		
		if(!town.gameWorld.bRenderFrameBuffer)
			renderSpriteMoveEvents(1);
		
		if(bDrawObject && bDrawObjectByCamera && bDrawObjectZoom)
		{
			if(!town.gameWorld.bRenderFrameBuffer)
				drawWarnings(worldSpriteBatch, 0);
		}
		
		if(!town.gameWorld.bRenderFrameBuffer)
			handleWorkoutput();
		
		//------------------------------------------------------------------------------
		
		
		if(deltaRotation>360)
			deltaRotation=0;
			
		if(deltaSecond>1)
			deltaSecond=0;
						
		if(workHour>3600)
			workHour=0;
		
		if(renderHour>3600)
			renderHour=0;
		
		if(render4Hour>14400)
			render4Hour=0;
		
		if(render2Hour>7200)
			render2Hour=0;
		
		if(renderMinute>60)
			renderMinute=0;
		
		worldSpriteBatch.setColor(1, 1, 1, town.gameWorld.alphaWorldObjects);
		}
		catch(Exception e)
		{
			bHandleActionsNow=true;
			CHelper.writeError(e.getMessage(), e.getStackTrace(), e);
		}		
		
		bHandleActionsNow=true;
	}

	void drawPlacingRect(ShapeRenderer sr1)
	{
		//float falpha=0.6f;
		float falpha=0.6f;
		Boolean bDraw=false;
		
		Color cr = new Color(0.6f, 0f, 0f, falpha);
		Color cg = new Color(0f, 0.6f, 0f, falpha);
		
		//Placing of Bathmat
		if(theobject.editoraction.contains("bathroom_shower") || theobject.editoraction.contains("bathroom_bathtub"))
		{
			if(town.gameGui.objPlacing!=null && town.gameGui.objPlacing.editoraction.contains("bathroom_bathmat"))
			{
				Boolean bCol = Intersector.overlapConvexPolygons(theobject.getBathmatZonePolygon(), town.gameGui.objPlacing.getBoundingPolygon(false, IntersectionMode.COLLISION));		
				
				if(bCol)
					sr1.setColor(cg);
				else
					sr1.setColor(cr);
				
				bDraw=true;
			}
		}
		
		//Placing of Nightstand
		if(theobject.editoraction.contains("bedroom_bed"))
		{
			if(town.gameGui.objPlacing!=null && town.gameGui.objPlacing.editoraction.contains("_nightstand"))
			{
				Boolean bCol = Intersector.overlapConvexPolygons(theobject.getZonePolygonForNighttablePlacement(2), town.gameGui.objPlacing.getBoundingPolygon(false, IntersectionMode.COLLISION));		
				
				if(bCol)
					sr1.setColor(cg);
				else
					sr1.setColor(cr);
				
				bDraw=true;
			}
		}
		
		//Placing of Bed
		if(theobject.editoraction.contains("_nightstand"))
		{
			if(town.gameGui.objPlacing!=null && town.gameGui.objPlacing.editoraction.contains("bedroom_bed"))
			{
				Boolean bCol = Intersector.overlapConvexPolygons(town.gameGui.objPlacing.getZonePolygonForNighttablePlacement(2), theobject.getBoundingPolygon(false, IntersectionMode.COLLISION));		
				
				if(bCol)
					sr1.setColor(cg);
				else
					sr1.setColor(cr);
				
				bDraw=true;
			}
		}
		

		//Placing Couch/Armchair
		if(theobject.editoraction.contains("_tv"))
		{
			if(town.gameGui.objPlacing!=null && (town.gameGui.objPlacing.editoraction.contains("livingroom_couch") ||town.gameGui.objPlacing.editoraction.contains("livingroom_armchair")))
			{
				sr1.setColor(cr);
				
				if(Intersector.overlapConvexPolygons(theobject.getTVZonePolygon(), town.gameGui.objPlacing.getBoundingPolygon(false, IntersectionMode.COLLISION)))
				{
					if(Intersector.overlapConvexPolygons(town.gameGui.objPlacing.getTVZonePolygon(), theobject.getBoundingPolygon(false, IntersectionMode.COLLISION)))
					{
						sr1.setColor(cg);
					}
				}
				
				bDraw=true;
			}
		}
		
		//Placing TV
		if(theobject.editoraction.contains("livingroom_couch") || theobject.editoraction.contains("livingroom_armchair"))
		{
			if(town.gameGui.objPlacing!=null && town.gameGui.objPlacing.editoraction.contains("_tv"))
			{
				sr1.setColor(cr);
				
				if(Intersector.overlapConvexPolygons(theobject.getTVZonePolygon(), town.gameGui.objPlacing.getBoundingPolygon(false, IntersectionMode.COLLISION)))
				{
					if(Intersector.overlapConvexPolygons(town.gameGui.objPlacing.getTVZonePolygon(), theobject.getBoundingPolygon(false, IntersectionMode.COLLISION)))
					{
						sr1.setColor(cg);
					}
				}
				
				bDraw=true;
			}
		}
		
		
		//Placing Church Bank
		if(theobject.editoraction.contains("company_church_workingplace_altar"))
		{
			if(town.gameGui.objPlacing!=null && (town.gameGui.objPlacing.editoraction.contains("company_church_bank")))
			{
				sr1.setColor(cr);
				
				if(Intersector.overlapConvexPolygons(theobject.getSchoolDeskZonePolygon(), town.gameGui.objPlacing.getBoundingPolygon(false, IntersectionMode.COLLISION)))
				{
					if(Intersector.overlapConvexPolygons(town.gameGui.objPlacing.getSchoolDeskZonePolygon(), theobject.getBoundingPolygon(false, IntersectionMode.COLLISION)))
					{
						sr1.setColor(cg);
					}
				}
				
				bDraw=true;
			}
		}
		
		
		//Placing Church Altar
		if(theobject.editoraction.contains("company_church_bank"))
		{
			if(town.gameGui.objPlacing==null)
				return;
			
			if(town.gameGui.objPlacing!=null && (town.gameGui.objPlacing.editoraction.contains("company_church_workingplace_altar")))
			{
				sr1.setColor(cr);
				
				if(Intersector.overlapConvexPolygons(theobject.getSchoolDeskZonePolygon(), town.gameGui.objPlacing.getBoundingPolygon(false, IntersectionMode.COLLISION)))
				{
					if(Intersector.overlapConvexPolygons(town.gameGui.objPlacing.getSchoolDeskZonePolygon(), theobject.getBoundingPolygon(false, IntersectionMode.COLLISION)))
					{
						sr1.setColor(cg);
					}
				}
				
				bDraw=true;
			}
		}
		
		
		//Placing School Student Desk
		if(theobject.editoraction.contains("company_school_workingplace_teachersdesk"))
		{
			if(town.gameGui.objPlacing!=null && (town.gameGui.objPlacing.editoraction.contains("company_school_workingplace_studentsdesk")))
			{
				sr1.setColor(cr);
				
				if(Intersector.overlapConvexPolygons(theobject.getSchoolDeskZonePolygon(), town.gameGui.objPlacing.getBoundingPolygon(false, IntersectionMode.COLLISION)))
				{
					if(Intersector.overlapConvexPolygons(town.gameGui.objPlacing.getSchoolDeskZonePolygon(), theobject.getBoundingPolygon(false, IntersectionMode.COLLISION)))
					{
						sr1.setColor(cg);
					}
				}
				
				bDraw=true;
			}
		}
		
	
		//Placing Teachers Desk
		if(theobject.editoraction.contains("company_school_workingplace_studentsdesk"))
		{
			if(town.gameGui.objPlacing!=null && (town.gameGui.objPlacing.editoraction.contains("company_school_workingplace_teachersdesk")))
			{
				sr1.setColor(cr);
				
				if(Intersector.overlapConvexPolygons(theobject.getSchoolDeskZonePolygon(), town.gameGui.objPlacing.getBoundingPolygon(false, IntersectionMode.COLLISION)))
				{
					if(Intersector.overlapConvexPolygons(town.gameGui.objPlacing.getSchoolDeskZonePolygon(), theobject.getBoundingPolygon(false, IntersectionMode.COLLISION)))
					{
						sr1.setColor(cg);
					}
				}
				
				bDraw=true;
			}
		}
		
		
		//Placing College Student Desk
		if(theobject.editoraction.contains("company_college_workingplace_profslectern"))
		{
			if(town.gameGui.objPlacing!=null && (town.gameGui.objPlacing.editoraction.contains("company_college_workingplace_studentsdesk")))
			{
				sr1.setColor(cr);
				
				if(Intersector.overlapConvexPolygons(theobject.getSchoolDeskZonePolygon(), town.gameGui.objPlacing.getBoundingPolygon(false, IntersectionMode.COLLISION)))
				{
					if(Intersector.overlapConvexPolygons(town.gameGui.objPlacing.getSchoolDeskZonePolygon(), theobject.getBoundingPolygon(false, IntersectionMode.COLLISION)))
					{
						sr1.setColor(cg);
					}
				}
				
				bDraw=true;
			}
		}
		
		
		//Placing Professors Desk
		if(theobject.editoraction.contains("company_college_workingplace_studentsdesk"))
		{
			if(town.gameGui.objPlacing!=null && (town.gameGui.objPlacing.editoraction.contains("company_college_workingplace_profslectern")))
			{
				sr1.setColor(cr);
				
				if(Intersector.overlapConvexPolygons(theobject.getSchoolDeskZonePolygon(), town.gameGui.objPlacing.getBoundingPolygon(false, IntersectionMode.COLLISION)))
				{
					if(Intersector.overlapConvexPolygons(town.gameGui.objPlacing.getSchoolDeskZonePolygon(), theobject.getBoundingPolygon(false, IntersectionMode.COLLISION)))
					{
						sr1.setColor(cg);
					}
				}
				
				bDraw=true;
			}
		}		
		
		if(bDraw)
		{
			Rectangle r1 = getBoundingPolygon(20).getBoundingRectangle();
			//sr1.rect(r1.x, r1.y, r1.width, r1.height);

			sr1.set(ShapeType.Line);
			//sr1.setColor(0.4f, 0.4f, 0.4f, 0.1f);
			sr1.rect(r1.x, r1.y, r1.width, r1.height);
			
			for(int i=0;i<10;i++)
				sr1.rect(r1.x-i, r1.y-i, r1.width+i*2, r1.height+i*2);
			
		}
		
		//Gdx.gl.glDisable(GL30.GL_BLEND);
	}
	public void drawShadows(SpriteBatch worldSpriteBatch, TextureRegion currentFrame)
	{
	
		if(!town.gameWorld.bRenderFrameBuffer && !town.gameWorld.bCloneAddressRender)
			return;
		
		
		Boolean brend=true;
		if(town.gameCam.zoom>town.zoomRender_noShadows)
			brend=false;
		if(town.gameWorld.bRenderFrameBuffer)		        				
			brend=true;
	
		if(!brend)
			return;
		
		//Performance1
		if(town.lodvalue<4)
		{
			if(theobject.isRoomObject)
			{
				int z1=20;
				
				if(town.lodvalue==0)
					z1=20;
				if(town.lodvalue==1)
					z1=25;
				if(town.lodvalue==2)
					z1=30;
				if(town.lodvalue==3)
					z1=35;
					
				if(town.gameCam.zoom>z1)
					return;
			}
			else if(theobject.editoraction.contains("road_road") ||
					theobject.editoraction.contains("residential_garage")
					)
			{
				//..
				//if(town.gameCam.zoom>10)
				//	if(town.gameCam.zoom>30)
				//		return;
			}
			else
			{
				if(town.gameCam.zoom>5)
					return;
			}
		}
		
		if(theobject.drawShadow)
		{
			int itype=(int) town.getSizeValue(99);
			if(bObjectIsReady)
				itype=1;
			
			Boolean bShadowIsDrawing = false;
			Boolean dsbytype = true;
			if(!bObjectIsReady && (theobject.editoraction.contains("road_road_road") || theobject.editoraction.contains("footpath")))
				dsbytype=false;
			
			if(dsbytype)
			{
				bShadowIsDrawing = theobject.drawShadows(worldSpriteBatch, 0, pos_x(), pos_y(), width, height, itype);
			}
			
			if(bShadowIsDrawing)
				return;
			
			if(theobject.editoraction.contains("parkingspace"))
				return;
			
			//if((theobject.roomtype.contains("outdoor") || theobject.roomtype.contains("outside")))// && (width>300 || height>300))
			if(theobject.ATTR_SHADOW>0)
			{
				worldSpriteBatch.setColor(0.16f, 0.16f, 0.16f, 0.134f);
				int sz=theobject.ATTR_SHADOW;
				worldSpriteBatch.draw(currentFrame, x, y, (width+sz)/2, (height+sz)/2, width+sz, height+sz,1,1, rotation());
				return;
			}
			
			if(theobject.editoraction.contains("outdoor_tree"))
			{
				//shadow1
				worldSpriteBatch.setColor(0.16f, 0.16f, 0.16f, 0.134f);
				int sz=(int) town.getSizeValue(30);
				worldSpriteBatch.draw(currentFrame, x, y, (width+sz)/2, (height+sz)/2, width+sz, height+sz,1,1, rotation());
				return;
			}
			
			worldSpriteBatch.setColor(0, 0, 0, 0.2f);
			
			if(theobject.editoraction.contains("bird"))
			{
				worldSpriteBatch.setColor(0, 0, 0, 0.15f);
				
				int delta1=0;
				int delta2=0;
				int delta3=0;
				int delta4=0;
				
				if(!town.gameWorld.worldPause)
				{
					delta1 = town.gameWorld.rand.nextInt(30);
					delta2 = town.gameWorld.rand.nextInt(30);
					delta3 = town.gameWorld.rand.nextInt(30);
					delta4 = town.gameWorld.rand.nextInt(30);
				}
				
				worldSpriteBatch.draw(currentFrame, pos_x()-shadowDistance-delta1, pos_y()-shadowDistance-delta2, width/2, height/2, width+delta3, height+delta4, 1f, 1f, rotation());
				worldSpriteBatch.setColor(1, 1, 1, town.gameWorld.alphaWorldObjects);
				return;
			}
			
			if(theobject.editoraction.contains("supermarket_shelf"))
			{
				worldSpriteBatch.draw(currentFrame, pos_x()-7, pos_y()-7, width/2, height/2, width+14, height+14, 1f, 1f, rotation());
				worldSpriteBatch.setColor(1, 1, 1, town.gameWorld.alphaWorldObjects);
				return;
			}
			
			if(theobject.editoraction.contains("traffic_car"))
			{
				worldSpriteBatch.setColor(0, 0, 0, 0.16f);
				worldSpriteBatch.draw(currentFrame, pos_x(), pos_y(), width/2, height/2, width+18, height+15, 1f, 1f, rotation());
				worldSpriteBatch.setColor(1, 1, 1, town.gameWorld.alphaWorldObjects);
				return;
			}
			
			if(thehuman!=null)
			{
				int val=(int) town.getSizeValue(15);
				worldSpriteBatch.setColor(0, 0, 0, 0.12f);
				worldSpriteBatch.draw(currentFrame, pos_x()-val*getBodySizeByAge(), pos_y()-val*1.5f*getBodySizeByAge(), width/2*getBodySizeByAge()+val*getBodySizeByAge(), height/2*getBodySizeByAge()+val*getBodySizeByAge(), (width+20)*getBodySizeByAge(), (height+40)*getBodySizeByAge(), 1f, 1f, rotation());
				worldSpriteBatch.setColor(1, 1, 1, town.gameWorld.alphaWorldObjects);
				return;
			}
			
			if(theobject.editoraction.contains("laundryroom_washingmachine") ||
					theobject.editoraction.contains("laundryroom_dryer") 
					)
			{
				int runx=0;
				int runy=0;
				if(!town.gameWorld.worldPause)
				{
					if(actionvar1==2)
					{
						int val=5;
						runx=rand.nextInt(val)-rand.nextInt(val);
						runy=rand.nextInt(val)-rand.nextInt(val);
					}
				}
				
				worldSpriteBatch.setColor(0.4f, 0.4f, 0.4f, 0.15f);
				worldSpriteBatch.draw(currentFrame, pos_x()+runx, pos_y()+runy, width/2, height/2, width+15, height+15, 1f, 1f, rotation());
				worldSpriteBatch.setColor(1, 1, 1, town.gameWorld.alphaWorldObjects);
				return;
			}
//			else if(theobject.editoraction.contains("recyclingcenter_recyclingmachine"))
//			{
//				worldSpriteBatch.setColor(0.4f, 0.4f, 0.4f, 0.15f);
//				worldSpriteBatch.draw(currentFrame, pos_x(), pos_y(), width/2, height/2, width+40, height+40, 1f, 1f, rotation());
//				worldSpriteBatch.setColor(1, 1, 1, gameWorld.alphaWorldObjects);
//				return;
//			}
			else
			{
				worldSpriteBatch.setColor(0.4f, 0.4f, 0.4f, 0.15f);
				//int size=width/10;
				//if(size<15)
				int size=15;
				worldSpriteBatch.draw(currentFrame, pos_x(), pos_y(), width/2, height/2, width+size, height+size, 1f, 1f, rotation());
				worldSpriteBatch.setColor(1, 1, 1, town.gameWorld.alphaWorldObjects);
				return;
			}
		}
	}
	
	void drawSickWarning(SpriteBatch worldSpriteBatch, CWorldObject wobj, float wmult)
	{
		if(wobj.thehuman.getAge()>70 && wobj.thehuman.sick==0)
		{
			return;
		}
				
		int movx=45;
		int movy=60;
		
		if(theobject.editoraction.contains("supermarket_shelf"))
		{
			movx=width/2;
			movy=-height/2;
		}
		
		if(wobj.thehuman.doctorHealingValue>0)
			worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_ill2"), pos_x()+movx, pos_y()-movy, 50*wmult, 50*wmult);
		else
		{
			if(wobj.thehuman.sickType==0)
			{
				if(wobj.thehuman.sick==0)
				{
					worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_health"), pos_x()+movx, pos_y()-movy, 50*wmult, 50*wmult);
				}
				else
				{
					if(wobj.thehuman.doctorHealingValue>0)
						worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_ill2"), pos_x()+movx, pos_y()-movy, 50*wmult, 50*wmult);				
					else
						worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_ill"), pos_x()+movx, pos_y()-movy, 50*wmult, 50*wmult);
				}
			}
			
			if(wobj.thehuman.sickType==1)
			{
				if(wobj.thehuman.doctorHealingValue>0)
					worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_ill_severe2"), pos_x()+movx, pos_y()-movy, 50*wmult, 50*wmult);
				else
					worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_ill_severe"), pos_x()+movx, pos_y()-movy, 50*wmult, 50*wmult);
			}
			
			if(wobj.thehuman.sickType==2)
			{
				if(wobj.thehuman.doctorHealingValue>0)
					worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_ill_contagious2"), pos_x()+movx, pos_y()-movy, 50*wmult, 50*wmult);
				else
					worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_ill_contagious"), pos_x()+movx, pos_y()-movy, 50*wmult, 50*wmult);
			}
		}		
	}
	
	void drawWarnings(SpriteBatch worldSpriteBatch, int type)
	{
		
		if(!bObjectIsReady)
			return;
		
		if(bIsDead)
			return;
		
		worldSpriteBatch.setColor(1, 1, 1, 1f);
		
		//float wmult = gameWorld.gameCamera.zoom/3;
		float wmult = 1; //gameWorld.gameCamera.zoom/5;
		if(town.gameCam.zoom>10)
			wmult = 2;
		if(town.gameCam.zoom>15)
			wmult = 3;
		if(town.gameCam.zoom>20)
			wmult = 4;
		
		//show blinking source warnings
		if(deltaSecond>0.5f || town.gameWorld.worldPause)
		{
			//worldSpriteBatch.setColor(1, 1, 1, 1);
			
			if(isCompanyWorkingPlace() || isTaskObject() || isFoodFillingByWorkerObject())
			{
				if(worker!=null && !worker.thehuman.canWork())
				{
					if(type==0)
					{
						town.gameWorld.warningObjects.add(this);
						return;
					}
					else
						drawSickWarning(worldSpriteBatch, worker, wmult);
				}
			}
			
			if(thehuman!=null && iZombie<1)
			{
				if(!thehuman.canWork()) //(thehuman.sick>0))
				{
					if(type==0)
					{
						town.gameWorld.warningObjects.add(this);
						return;
					}
					else
						drawSickWarning(worldSpriteBatch, this, wmult);
				}
				
				if(thehuman!=null && thehuman.bIsDark)
				{
					Boolean bShowWarning=true;
					
					if(activeAction!=null && activeAction.actionMode==ActionMode.WATCH_TV && activeAction.bActionMode)
						bShowWarning=false;
					
					if(activeAction==null || !activeAction.bActionMode)
						bShowWarning=false;
						
					if(bShowWarning)
					{
						if(type==0)
						{
							town.gameWorld.warningObjects.add(this);
							return;
						}
						else
							worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_dark"), pos_x()+45, pos_y(), 50*wmult, 50*wmult);
					}
				}
				
				if(thehuman!=null && thehuman.coffeinLevel>CHuman.getCriticalCoffeinLevel(0))
				{
					float wmulttemp=wmult;
					if(wmulttemp>3)
						wmulttemp=3;
					
					if(type==0)
					{
						town.gameWorld.warningObjects.add(this);
						return;
					}
					else
						worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_caffeine"), pos_x()+45, pos_y()+50, 50*wmulttemp, 50*wmulttemp);
				}
				
				if(thehuman!=null && thehuman.bIsCold)
				{
					if(type==0)
					{
						town.gameWorld.warningObjects.add(this);
						return;
					}
					else
						worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_cold"), pos_x()-40, pos_y(), 50*wmult, 50*wmult);
				}
				
				if(thehuman!=null && theaddress==null)
				{
					if(type==0)
					{
						town.gameWorld.warningObjects.add(this);
						return;
					}
					else
						worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_homeless"), pos_x()+100, pos_y()+60, 70*town.gameCam.zoom/4, 70*town.gameCam.zoom/4);
				}
				
				if(thehuman!=null && theaddress!=null && thehuman.wardrobe==null && thehuman.clothingValue>thehuman.clothingValueTrigger)
				{
					if(type==0)
					{
						town.gameWorld.warningObjects.add(this);
						return;
					}
					else
						worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_warning"), pos_x()+100, pos_y()+60, 70*wmult, 50*wmult);
				}
			}
			
			if(getEnergyConsumption()>0 && town.gameWorld.getEnergyConsumption() > town.gameWorld.getEnergyOutput())
			{
				if(type==0)
				{
					town.gameWorld.warningObjects.add(this);
					return;
				}
				else
				{
					if(theobject.editoraction.contains("radiator"))
					{
						worldSpriteBatch.setColor(1, 1, 1, 0.4f);
						worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_energy"), pos_x()+width/2-3+40, pos_y()-3, (70+6)*wmult, (70+6)*wmult);
						
						worldSpriteBatch.setColor(1, 1, 1, 1);
						worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_energy"), pos_x()+(width/2+40), pos_y(), 70*wmult, 70*wmult);
					}
					else
					{
						worldSpriteBatch.setColor(1, 1, 1, 0.4f);
						worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_energy"), pos_x()+width/3-3, pos_y()-3, (70+6)*wmult, (70+6)*wmult);
						
						worldSpriteBatch.setColor(1, 1, 1, 1);
						worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_energy"), pos_x()+(width/3), pos_y(), 70*wmult, 70*wmult);
					}
				}
			}
			
			if(!isActiveByWaterConsumption())// getWaterConsumption()>0 && town.gameWorld.getWaterConsumption() > town.gameWorld.getWaterOutput())
			{
				if(type==0)
				{
					town.gameWorld.warningObjects.add(this);
					return;
				}
				else
				{
					if(theobject.editoraction.contains("radiator"))
					{
						worldSpriteBatch.setColor(1, 1, 1, 0.4f);
						worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_water"), pos_x()+width/2-13-40, pos_y()-3, (40+6)*wmult, (50+6)*wmult);
						
						worldSpriteBatch.setColor(1, 1, 1, 1);
						worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_water"), pos_x()+width/2-10-40, pos_y(), 40*wmult, 50*wmult);					
					}
					else if(theobject.editoraction.contains("bathroom"))
					{
						//Gdx.app.setLogLevel(10);
						//Gdx.app.log("", "not active " + theobject.editoraction);
						
						worldSpriteBatch.setColor(1, 1, 1, 0.4f);
						worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_water"), pos_x()+width/3-3, pos_y()+height/2-3, (40+6)*wmult, (50+6)*wmult);
						
						worldSpriteBatch.setColor(1, 1, 1, 1);
						worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_water"), pos_x()+(width/3), pos_y()+height/2, 40*wmult, 50*wmult);
					}
					else if(theobject.editoraction.contains("kitchen_sink"))
					{
						worldSpriteBatch.setColor(1, 1, 1, 0.4f);
						worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_water"), pos_x()+(width/2)-3, pos_y()+(height/2)-3,  (40+6)*wmult, (50+6)*wmult);
						
						worldSpriteBatch.setColor(1, 1, 1, 1);
						worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_water"), pos_x()+(width/2), pos_y()+(height/2),  40*wmult, 50*wmult);
					}
					else if(theobject.editoraction.contains("laundryroom_washingmachine"))
					{
						worldSpriteBatch.setColor(1, 1, 1, 0.4f);
						worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_water"), pos_x()+width/3-3, pos_y()-3+70, (40+6)*wmult, (50+6)*wmult);
						
						worldSpriteBatch.setColor(1, 1, 1, 1);
						worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_water"), pos_x()+(width/3), pos_y()+70, 40*wmult, 50*wmult);
					}
					else
					{
						worldSpriteBatch.setColor(1, 1, 1, 0.4f);
						worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_water"), pos_x()+(width/3)-103, pos_y()-3,  (40+6)*wmult, (50+6)*wmult);
						
						worldSpriteBatch.setColor(1, 1, 1, 1);
						worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_water"), pos_x()+(width/3)-100, pos_y(),  40*wmult, 50*wmult);
					}
				}
			}
			
			if(theobject.getNeededWorkinputPerHour()>0 && !onlineByWorkInput)
			{
				if(type==0)
				{
					town.gameWorld.warningObjects.add(this);
					return;
				}
				else
				{
					int x=pos_x()+width/3+100;
					int y=pos_y();
					
					if(theobject.editoraction.contains("_fitnessstudio"))
					{
						x=pos_x()+width/2-35;
						y=pos_y()+height/2-40;
					}
					
					worldSpriteBatch.setColor(1, 1, 1, 1);
					worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_workinput"), x, y, 70*wmult, 80*wmult);
				}
			}
			
			if(theobject.isCar)
			{
				if(fuelValue<1)
				{
					if(type==0)
					{
						town.gameWorld.warningObjects.add(this);
						return;
					}
					else
					{
						worldSpriteBatch.setColor(1, 1, 1, 1f);
						worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_fuel1"), pos_x()+width/2-35*wmult, pos_y(), (70)*wmult, (70)*wmult);
					}
				}
			}
			
			if(isCompanyOfficeWorkingPlace())
			{
				if(theobject.editoraction.contains("company_school_workingplace_studentsdesk"))
				{
					if(!bStudentDeskHasTeachersDesk)
					{
						if(type==0)
						{
							town.gameWorld.warningObjects.add(this);
							return;
						}
						else
						{
							worldSpriteBatch.setColor(1, 1, 1, 1);
							worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_warning"), pos_x()+(width/2)-35, pos_y()+(height/2)-35, 70*wmult, 70*wmult);
						}
					}
					
					if(worker==null)
					{
						worldSpriteBatch.setColor(1, 1, 1, 1);
						worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_worker2"), pos_x()+(width/3)-town.getSizeValue(60), pos_y()+(height/2), 30*wmult, 60*wmult);
					}
					
					if(worker2==null)
					{
						if(type==0)
						{
							town.gameWorld.warningObjects.add(this);
							return;
						}
						else
						{
							worldSpriteBatch.setColor(1, 1, 1, 1);
							worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_worker2"), pos_x()+(width)-town.getSizeValue(40), pos_y()+(height/2), 30*wmult, 60*wmult);
						}
					}
				}
				else if(theobject.editoraction.contains("company_school_workingplace_teachersdesk"))
				{
					if(worker==null)
					{
						if(type==0)
						{
							town.gameWorld.warningObjects.add(this);
							return;
						}
						else
						{
							worldSpriteBatch.setColor(1, 1, 1, 1);
							worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_worker2"), pos_x()+(width/2)-15, pos_y()+(height/2)-30, 30*wmult, 60*wmult);
						}
					}
				}
				else if(theobject.editoraction.contains("company_college_workingplace_researchlab") ||
						theobject.editoraction.contains("company_objectdesign_officeworkingplace_artist")
						)
				{
					if(researchObject==null)
					{
						if(type==0)
						{
							town.gameWorld.warningObjects.add(this);
							return;
						}
						else
						{
							worldSpriteBatch.setColor(1, 1, 1, 1);
							worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_researchproject"), pos_x()+(width/2)+60, pos_y()+(height/2), 60*wmult, 60*wmult);
						}
					}
					
					if(worker==null)
					{
						if(type==0)
						{
							town.gameWorld.warningObjects.add(this);
							return;
						}
						else
						{
							worldSpriteBatch.setColor(1, 1, 1, 1);
							worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_worker2"), pos_x()+(width/3)-town.getSizeValue(60), pos_y()+(height/2), 30*wmult, 60*wmult);
						}
					}
				}
				else if(theobject.editoraction.contains("company_college_workingplace_studentsdesk"))
				{
					if(!bStudentDeskHasTeachersDesk)
					{
						if(type==0)
						{
							town.gameWorld.warningObjects.add(this);
							return;
						}
						else
						{
							worldSpriteBatch.setColor(1, 1, 1, 1);
							worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_warning"), pos_x()+(width/2)-35, pos_y()+(height/2)-35, 70*wmult, 70*wmult);
						}
					}
					
					if(worker==null)
					{
						if(type==0)
						{
							town.gameWorld.warningObjects.add(this);
							return;
						}
						else
						{
							worldSpriteBatch.setColor(1, 1, 1, 1);
							worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_worker2"), pos_x()+(width/3)-town.getSizeValue(60), pos_y()+(height/2), 30*wmult, 60*wmult);
						}
					}
					
					if(worker2==null)
					{
						if(type==0)
						{
							town.gameWorld.warningObjects.add(this);
							return;
						}
						else
						{
							worldSpriteBatch.setColor(1, 1, 1, 1);
							worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_worker2"), pos_x()+(width)-town.getSizeValue(40), pos_y()+(height/2), 30*wmult, 60*wmult);
						}
					}
				}
				else if(theobject.editoraction.contains("company_college_workingplace_profslectern"))
				{
					if(worker==null)
					{
						if(type==0)
						{
							town.gameWorld.warningObjects.add(this);
							return;
						}
						else
						{
							worldSpriteBatch.setColor(1, 1, 1, 1);
							worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_worker2"), pos_x()+(width/2)-15, pos_y()+(height/2)-30, 30*wmult, 60*wmult);
						}
					}
				}
				else
				{
					if(worker==null)
					{
						if(type==0)
						{
							town.gameWorld.warningObjects.add(this);
							return;
						}
						else
						{
							int x=Math.round(pos_x()+(width/3)-60);
							int y=Math.round(pos_y()+town.getSizeValue(140));
														
							if(theobject.editoraction.contains("_fitnessstudio") || theobject.editoraction.contains("company_gasstation_workingplace_cashpoint"))
							{
								x=Math.round(pos_x()+(width/2)-15);
								y=Math.round(pos_y()+(height/2)-30);
							}
							
							worldSpriteBatch.setColor(1, 1, 1, 1);
							worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_worker2"), x, y, 30*wmult, 60*wmult);
						}
					}
				}
			}
			
			if(theobject.editoraction.contains("company_recyclingcenter_garbagetruck_traffic_car") ||
					theobject.editoraction.contains("company_construction_pickup1_traffic_car") ||
					theobject.editoraction.contains("company_urbancemetery_hearse_traffic_car") || 
					theobject.editoraction.contains("company_urbancemetery_rostrum") || 
					theobject.editoraction.contains("company_doctorsoffice_treatment_chair") ||
					theobject.editoraction.contains("company_doctorsoffice_reception_desk") ||
					theobject.editoraction.contains("laundrybasket")
					)
			{
				if(worker==null)
				{
					if(type==0)
					{
						town.gameWorld.warningObjects.add(this);
						return;
					}
					else
					{
						worldSpriteBatch.setColor(1, 1, 1, 1);
						worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_worker2"), pos_x()+(width/2)-20, pos_y()+(height/2)-40, 30*wmult, 60*wmult);
					}
				}
			}
						
			if(isFoodFillingByWorkerObject()  && worker==null)
			{
				if(type==0)
				{
					town.gameWorld.warningObjects.add(this);
					return;
				}
				else
				{
					worldSpriteBatch.setColor(1, 1, 1, 1);
					worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_worker2"), pos_x()+5, pos_y()+(height/1.4f), 30*wmult, 60*wmult);
				}
			}

			if(isCompanyObjectFillingByWorkerObject()  && worker==null)
			{
				if(type==0)
				{
					town.gameWorld.warningObjects.add(this);
					return;
				}
				else
				{
					worldSpriteBatch.setColor(1, 1, 1, 1);
					worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_worker2"), pos_x()+(width/2)-15, pos_y()+(height/2f)-30, 30*wmult, 60*wmult);
				}
			}
			
			
			if(theobject.editoraction.contains("diningroom_diningtable"))
			{
				if(worker==null)
				{
					if(type==0)
					{
						town.gameWorld.warningObjects.add(this);
						return;
					}
					else
					{
						worldSpriteBatch.setColor(1, 1, 1, 1);
						worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_worker2"), pos_x()+(width/2)-15, pos_y()+(height/2)-15, 30*wmult, 60*wmult);
					}
				}
			}
			
			
			if(theobject.editoraction.contains("bedroom_bed"))
			{
				if(theobject.getBedtoOtherBedsZoneCollisionCount(theaddress)>0)
				{
					if(type==0)
					{
						town.gameWorld.warningObjects.add(this);
						return;
					}
					else
					{
						worldSpriteBatch.setColor(1, 1, 1, 1);
						worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_warning"), pos_x()-45, pos_y()+(height)-90, 70*wmult, 70*wmult);
					}
				}
			}
			
			
			if(isOwnerObject()>0)
			{
				if(theobject.editoraction.contains("bedroom_bed_double"))
				{
					if(owner==null)
					{
						if(type==0)
						{
							town.gameWorld.warningObjects.add(this);
							return;
						}
						else
						{
							worldSpriteBatch.setColor(1, 1, 1, 1);
							worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_worker2"), pos_x(), pos_y()+(height)-60, 30*wmult, 60*wmult);
						}
					}
					
					if(owner2==null)
					{
						if(type==0)
						{
							town.gameWorld.warningObjects.add(this);
							return;
						}
						else
						{
							worldSpriteBatch.setColor(1, 1, 1, 1);
							worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_worker2"), pos_x()+(width)-30, pos_y()+(height)-60, 30*wmult, 60*wmult);
						}
					}
				}
				else
				{
					if(owner==null)
					{
						if(type==0)
						{
							town.gameWorld.warningObjects.add(this);
							return;
						}
						else
						{
							worldSpriteBatch.setColor(1, 1, 1, 1);
							worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_worker2"), pos_x()+(width/2)-15, pos_y()+(height/2)-15, 30*wmult, 60*wmult);
						}
					}
				}
			}
			
			float ftrig=1.3f;
			if(theobject.editoraction.contains("illuminati_defense"))
				ftrig=1.1f;
			if(defaultObjectCondition>0 && objectCondition<(defaultObjectCondition/ftrig) && isMaintenanceObject())
			{
				if(type==0)
				{
					town.gameWorld.warningObjects.add(this);
					return;
				}
				else
				{
					worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_condition"), pos_x()+(width/3), pos_y()+100, 80*wmult, 80*wmult);
				}
			}
			
			if(theobject.editoraction.contains("supermarket_foodpallet") && !onlineByWorkInput && objectFilling<getObjectFillingMax())
			{
				if(type==0)
				{
					town.gameWorld.warningObjects.add(this);
					return;
				}
				else
				{
					worldSpriteBatch.setColor(1, 1, 1, 1);
					worldSpriteBatch.draw(town.gameResourceConfig.textures.get("warning_workinput"), pos_x()+(width/3)+100, pos_y(), 70*wmult, 80*wmult);
				}
			}
		}
	}
	public void renderSpriteMoveEvents(int zorder)
	{
		
		if(town.gameWorld.worldPause)
			return;
		
		ArrayList<CSpriteMoveEvent> delList = new ArrayList<CSpriteMoveEvent>();
		
		for(CSpriteMoveEvent event : spriteMoveEvents)
		{
			if(event.zorder==zorder)
			{
				event.render(town.gameWorld.worldSpriteBatch);
				
				if(event.bIsFinished)
				{
					delList.add(event);
				}
			}
		}
		
		spriteMoveEvents.removeAll(delList);
	}
	
	//noch nicht rauslöschen
	//void drawValueAnimations(SpriteBatch worldSpriteBatch)
	//{
		
		//float animSize=7+gameWorld.gameCamera.zoom;
		
		
		//if(!gameWorld.worldPause) //placing money auch im pause mode
//		{
			//**********
			//Show money
			//**********
//			if((showMoney>0 || showMoney<0) && showTimer<=0)
//			{
//				showTimer=1f;
//			}
//			if(showTimer>0 && (showMoney>0 || showMoney<0))
//			{
//				String stat="+";
//				if(showMoney>0)
//				{
//					town.gameFont.bfArial.setColor(Color.YELLOW);
//				}
//				else if(showMoney<0)
//				{
//					town.gameFont.bfArial.setColor(Color.RED);
//					stat="-";
//				}
//				
//				worldSpriteBatch.setShader(town.gameFont.fontShader);
//				town.gameFont.bfArial.getData().setScale(showTimer*animSize);
//				worldSpriteBatch.setShader(town.gameFont.fontShader);
//				town.gameFont.bfArial.draw(worldSpriteBatch, stat+"$" + Math.abs(showMoney), pos_x()+width/2, pos_y()+height/2);
//				worldSpriteBatch.setShader(null);
//				town.gameFont.bfArial.setColor(Color.WHITE);
//				town.gameFont.bfArial.getData().setScale(1);
//				worldSpriteBatch.setShader(null);
//				
//				showTimer-=Gdx.graphics.getDeltaTime()/2;
//				if(showTimer<=0)
//					showMoney=0;
//			}
//			else
//			{
//				showMoney=0;
//			}
			
			
			//Im X Speed Performance sparen
			//if(town.gameGui.buttonX2.toggleActive || town.gameGui.buttonX4.toggleActive)
			//	return;
			
			
			//***************
			//Show workoutput
			//***************		
//			if((showWorkoutput>0 || showWorkoutput<0) && showTimer<=0)
//			{
//				showTimer=1f;
//			}
//			if(showTimer>0 && (showWorkoutput>0 || showWorkoutput<0))
//			{
//				String stat="+";
//				
//				if(showWorkoutput>0)
//				{
//					town.gameFont.bfArial.setColor(Color.WHITE);
//					worldSpriteBatch.setColor(1,1,1,0.7f);
//				}
//				else if(showWorkoutput<0)
//				{
//					town.gameFont.bfArial.setColor(Color.BLUE);
//					worldSpriteBatch.setColor(1,0,0,0.7f);
//					stat="-";
//				}
//				
//				float iconsize=50*showTimer*animSize;
//						
//				town.gameFont.bfArial.getData().setScale(showTimer*animSize);
//				worldSpriteBatch.setShader(town.gameFont.fontShader);
//				town.gameFont.bfArial.draw(worldSpriteBatch, stat+Math.abs(showWorkoutput), pos_x()+width/2+iconsize, pos_y()+height/2);
//				worldSpriteBatch.setShader(null);
//				town.gameFont.bfArial.setColor(Color.WHITE);
//				town.gameFont.bfArial.getData().setScale(1);
//				
//				worldSpriteBatch.draw(gameWorld.gameResourceConfig.textures.get("anim_showwork"),pos_x()+width/2, pos_y()+height/3, iconsize, iconsize);
//				
//				showTimer-=Gdx.graphics.getDeltaTime()/2;
//				if(showTimer<=0)
//					showWorkoutput=0;
//			}
//			else
//			{
//				showWorkoutput=0;
//			}
			
			
			//****************
			//Show condition
			//****************		
//			if((showMaintain>0 || showMaintain<0) && showTimer<=0)
//			{
//				showTimer=1f;
//			}
//			if(showTimer>0 && (showMaintain>0 || showMaintain<0))
//			{
//				String stat="+";
//				
//				if(showMaintain>0)
//				{
//					town.gameFont.bfArial.setColor(Color.WHITE);
//					worldSpriteBatch.setColor(1,1,1,0.7f);
//				}
//				else if(showMaintain<0)
//				{
//					town.gameFont.bfArial.setColor(Color.RED);
//					worldSpriteBatch.setColor(1,0,0,0.7f);
//					stat="-";
//				}
//				
//				//float iconsize=300*showTimer;
//				float iconsize=50*showTimer*animSize;
//				
//				town.gameFont.bfArial.getData().setScale(showTimer*animSize);
//				worldSpriteBatch.setShader(town.gameFont.fontShader);
//				town.gameFont.bfArial.draw(worldSpriteBatch, stat+Math.abs(showMaintain), pos_x()+width/2+iconsize, pos_y()+height/2);
//				worldSpriteBatch.setShader(null);
//				town.gameFont.bfArial.setColor(Color.WHITE);
//				town.gameFont.bfArial.getData().setScale(1);
//								
//				worldSpriteBatch.draw(gameWorld.gameResourceConfig.textures.get("guiinfo_condition"),pos_x()+width/2, pos_y()+height/3, iconsize, iconsize);
//				
//				showTimer-=Gdx.graphics.getDeltaTime()/2;
//				if(showTimer<=0)
//					showMaintain=0;
//			}
//			else
//			{
//				showMaintain=0;
//			}			
//		}
//	}
	
	public int getResizeByScrollingPrice()
	{
		int w=width;
		int h=height;
		if(tempwidth>0)
			w=tempwidth;
		if(tempheight>0)
			h=tempheight;
		
		return theobject.getResizeByScrollingPrice(w+h, scrollwidth+scrollheight);
	}
	

	public float getRegionWidth(TextureRegion reg)
	{
		if(town.fSizeFactor>0)
		{
			return reg.getRegionWidth()/town.fSizeFactor;
		}
		
		return reg.getRegionWidth();
	}
	
	public float getRegionHeight(TextureRegion reg)
	{
		if(town.fSizeFactor>0)
		{
			return reg.getRegionHeight()/town.fSizeFactor;
		}
		
		return reg.getRegionHeight();
	}
	
	
	
	void drawHuman(SpriteBatch worldSpriteBatch, TextureRegion currentFrame, TextureRegion currentFrame2, TextureRegion currentFrame3, TextureRegion currentFrame4, Boolean bShowDel)
	{
		fRenderSpeakTime-=CHelper.getDeltaSeconds(town);
		
		if(activeActionMode == CAction.ActionMode.TOILET_FLOOR && !bIsDead)
		{
			worldSpriteBatch.setColor(1, 1, 1, 0.8f);
			worldSpriteBatch.draw(town.gameWorld.gameResourceConfig.textures.get("res_waterPuddle"), pos_x(), pos_y(), width/2, height/2, width, height, 0.9f, 0.9f, rotation(), 0, 0, town.gameWorld.gameResourceConfig.textures.get("res_waterPuddle").getWidth(), town.gameWorld.gameResourceConfig.textures.get("res_waterPuddle").getHeight(), false, false);
			worldSpriteBatch.setColor(1, 1, 1, town.gameWorld.alphaWorldObjects);
		}					
		
    	float fsizefactor1=0; //body
    	float fsizefactor=0; //head 
    	
    	fsizefactor1=getBodySizeByAge();
    	fsizefactor=getHeadSizeByAge();
    	
    	if(town.gameGui.bObjMovement && 
    			town.gameWorld.markerObject!=null && 
    			town.gameWorld.markerObject.uniqueId==uniqueId)
    	{
    		fsizefactor1+=0.2f;
    		fsizefactor-=0.2f;
    	}
    	
    	if(iZombie>=1 && bIsDead)
    	{
			worldSpriteBatch.setColor(1,1,1,1f);
			worldSpriteBatch.draw(town.gameResourceConfig.animations.get("deadzombie").getKeyFrames()[zombieDeadFrame], pos_x(), pos_y(), width_human()*1.5f, height_human()*1.5f);
			return;
    	}
    	
    	
		//Performance1
		if(town.gameCam.zoom<15)
			drawHuman_UnderBody(worldSpriteBatch);
		
    	//Draw Body
    	Boolean bDrawHumanHeadAndBody=true;
    	if(actionstring1.contains("show_coffin") || actionstring1.contains("show_grave"))
    		bDrawHumanHeadAndBody=false;
    	
    	if(bDrawHumanHeadAndBody)
    	{	
    		//Performance1
    		if(town.gameCam.zoom<10)
    		{
	    		if(!bSwimming)
	    		{
	    			worldSpriteBatch.setColor(thehuman.clothingColor_Top_Now.r, thehuman.clothingColor_Top_Now.g, thehuman.clothingColor_Top_Now.b, thehuman.clothingColor_Top_Now.a);
	    			//worldSpriteBatch.draw(currentFrame, pos_x(), pos_y(), currentFrame.getRegionWidth()/2*fsizefactor1, currentFrame.getRegionHeight()/2*fsizefactor1, currentFrame.getRegionWidth()*fsizefactor1, currentFrame.getRegionHeight()*fsizefactor1, 1, 1, rotation());
	    			
	    			float w1=getRegionWidth(currentFrame);
	    			float h1=getRegionHeight(currentFrame);
	    			
	    			worldSpriteBatch.draw(currentFrame, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());
	    		}
	    		
	    		//Draw Arms, Legs, Shoes
	    		
	    		//Arms
	    		if(currentFrame2!=null && actionanim1==0)
	    		{
					//worldSpriteBatch.setColor(1, 1, 1, thehuman.clothingColor_Top_Now.a); //für naked hier alpha von top benutzen
	    			worldSpriteBatch.setColor(1, 1, 1, thehuman.clothingColor_Top_Now.a); //für naked hier alpha von top benutzen
	
	    			if(iZombie>=1)
	    				worldSpriteBatch.setColor(0, 1, 0, 1);
					
	    			
	    			float w1=getRegionWidth(currentFrame2);
	    			float h1=getRegionHeight(currentFrame2);

	    			
	    			worldSpriteBatch.draw(currentFrame2, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());
	    		}
	    		
	    		//Legs
	    		if(!bSwimming)
	    		{
		    		if(currentFrame3!=null && actionanim2==0)
		    		{
		    			
		    			float w1=getRegionWidth(currentFrame3);
		    			float h1=getRegionHeight(currentFrame3);

		    			
						worldSpriteBatch.setColor(thehuman.clothingColor_Bottom_Now.r, thehuman.clothingColor_Bottom_Now.g, thehuman.clothingColor_Bottom_Now.b, thehuman.clothingColor_Bottom_Now.a);
						worldSpriteBatch.draw(currentFrame3, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());
		    		}
		    		
		    		//Shoes
		    		if(currentFrame4!=null && actionanim2==0)
		    		{
		    			float w1=getRegionWidth(currentFrame4);
		    			float h1=getRegionHeight(currentFrame4);
		    			
		    			worldSpriteBatch.setColor(thehuman.clothingColor_Shoes_Now.r, thehuman.clothingColor_Shoes_Now.g, thehuman.clothingColor_Shoes_Now.b, thehuman.clothingColor_Shoes_Now.a);
						worldSpriteBatch.draw(currentFrame4, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());
		    		}
	    		}
    		}
	    }
    	
    	worldSpriteBatch.setColor(1, 1, 1, 1f);
		//Performance1
		if(town.gameCam.zoom<10)
		{
	    	drawHumanActionAnim(worldSpriteBatch, currentFrame, fsizefactor1);
			//worldSpriteBatch.setColor(1, 1, 1, 1f);
		}
		
    	if(!town.gameWorld.worldPause && !bIsDead)
    		rendertime+=Gdx.graphics.getDeltaTime();
    	
    	float limit=0.7f;
    	if(ziel_x>-1)
    		limit=0.1f;
    	
    	if(rendertime>limit)
    	{
    		rendertime=0;
	    	{
	    		rotmod=0;
	    		
	    		int val = rand.nextInt(8);
	    		if(val==1)
	    			rotmod=5;
	    		if(val==2)
	    			rotmod=-5;
	    		
	    		if(val==3)
	    			rotmod=8;
	    		if(val==4)
	    			rotmod=-8;
	    		
	    		if(val==5)
	    			rotmod=10;
	    		if(val==6)
	    			rotmod=-10;
	    			
	    		rotmod*=2;
	    	}
    	}
    	
    	if(currentHeadFrame==null)
    		currentHeadFrame = headFrames[0];
    	    	
    	
    	float h=0.7f;
    	
    	if(bShowDel)
    		worldSpriteBatch.setColor(1, 0, 0, 0.4f);
    	else
    		worldSpriteBatch.setColor(h, h, h, 1f);
    	
    	
		//Performance1
		if(town.gameCam.zoom<10)
			drawHumanAction_UnderHead(currentHeadFrame, fsizefactor1);
    	
    	//Draw Head
    	//if(currentHeadFrame!=null && currentHeadFrame.getTexture()!=null) //gibt wohl manchmal probleme
    	try
    	{
	    	if(currentHeadFrame!=null) //getTexture prüfung ist teuer -> test mit try catch
	    	{
	        	if(bDrawHumanHeadAndBody)
	        	{
	        		worldSpriteBatch.setColor(h, h, h, 0.92f);
	        		
	        		if(iZombie>=1)
	        			worldSpriteBatch.setColor(0, 0.5f, 0, 0.92f);
	        		
	        		float w1 = getRegionWidth(currentFrame);
	        		float h1 = getRegionHeight(currentFrame);
	        		
	        		//Pastor
	        		if(thehuman.timeForWork("company_church_workingplace_altar"))
	        		{
	        			Texture pastor = town.gameWorld.gameResourceConfig.textures.get("pastor_head");
	        			town.gameWorld.worldSpriteBatch.draw(pastor, pos_x()+(int)town.getSizeValue(30)/fsizefactor, pos_y()+(int)town.getSizeValue(35)/fsizefactor, w1/2/fsizefactor-town.getSizeValue(30)/fsizefactor, h1/2/fsizefactor-town.getSizeValue(35)/fsizefactor, town.getSizeValue(60)/fsizefactor, town.getSizeValue(70)/fsizefactor, 1, 1, rotation()+180+rotmod, 0, 0, pastor.getWidth(), pastor.getHeight(), false, false);	        			
	        		}
	        		//Battle Priest
	        		else if(thehuman.timeForWork("company_church_workingplace_battlepriest"))
	        		{
	        			Texture battlepriest_spear = town.gameWorld.gameResourceConfig.textures.get("battlepriest_spear");
	        			town.gameWorld.worldSpriteBatch.draw(battlepriest_spear, pos_x()+town.getSizeValue(30)/fsizefactor, pos_y()+town.getSizeValue(35)/fsizefactor, w1/2/fsizefactor-30/fsizefactor, h1/2/fsizefactor-35/fsizefactor, town.getSizeValue(38), town.getSizeValue(160), 1, 1, rotation()+180+rotmod, 0, 0, battlepriest_spear.getWidth(), battlepriest_spear.getHeight(), false, false);	        			
	        			
	        			Texture battlepriest = town.gameWorld.gameResourceConfig.textures.get("battlepriest_head");
	        			town.gameWorld.worldSpriteBatch.draw(battlepriest, pos_x()+town.getSizeValue(30)/fsizefactor, pos_y()+town.getSizeValue(35)/fsizefactor, w1/2/fsizefactor-town.getSizeValue(30)/fsizefactor, h1/2/fsizefactor-town.getSizeValue(35)/fsizefactor, town.getSizeValue(60)/fsizefactor, town.getSizeValue(70)/fsizefactor, 1, 1, rotation()+180+rotmod, 0, 0, battlepriest.getWidth(), battlepriest.getHeight(), false, false);	        			
	        		}
	        		//Construction
	        		else if(thehuman.timeForWork("company_construction_pickup1_traffic_car"))
	        		{
	        			Texture battlepriest = town.gameWorld.gameResourceConfig.textures.get("construction_head");
	        			town.gameWorld.worldSpriteBatch.draw(battlepriest, pos_x()+town.getSizeValue(30)/fsizefactor, pos_y()+town.getSizeValue(35)/fsizefactor, w1/2/fsizefactor-town.getSizeValue(30)/fsizefactor, h1/2/fsizefactor-town.getSizeValue(35)/fsizefactor, town.getSizeValue(60)/fsizefactor, town.getSizeValue(70)/fsizefactor, 1, 1, rotation()+180+rotmod, 0, 0, battlepriest.getWidth(), battlepriest.getHeight(), false, false);	        			
	        		}	        		
	        		else
	        		{
	        			//Default
	        			worldSpriteBatch.draw(currentHeadFrame, pos_x()+town.getSizeValue(30)/fsizefactor, pos_y()+town.getSizeValue(35)/fsizefactor, w1/2/fsizefactor-town.getSizeValue(30)/fsizefactor, h1/2/fsizefactor-town.getSizeValue(35)/fsizefactor, town.getSizeValue(60)/fsizefactor, town.getSizeValue(70)/fsizefactor, 1, 1, rotation()+rotmod);
	        		}
	        	}
	    	}
    	}
    	catch(Exception ex)
    	{
    		//Gdx.app.debug("", "error: "+ex.getMessage());
    	}
    	
		//Performance1
		if(town.gameCam.zoom<10)
			drawHumanAction_OverHead(worldSpriteBatch, currentFrame, fsizefactor, fsizefactor1, rotmod);
	}
	
	public void drawHumanActionAnim(SpriteBatch worldSpriteBatch, TextureRegion currentFrame, float fsizefactor1)
	{
		float w1=getRegionWidth(currentFrame);
		float h1=getRegionHeight(currentFrame);
		
		//Legs
		if(activeAction!=null && actionanim2==1 && !town.gameWorld.worldPause)
		{
			//Walk only
			worldSpriteBatch.setColor(thehuman.clothingColor_Bottom_Now.r, thehuman.clothingColor_Bottom_Now.g, thehuman.clothingColor_Bottom_Now.b, thehuman.clothingColor_Bottom_Now.a); //für naked darstellung
    		//TextureRegion workFrame = gameWorld.gameResourceConfig.animations.get("base_walkonly").getKeyFrame(CHelper.getDeltaSeconds(gameWorld)+gameWorld.stateTime+rand.nextFloat()/10, true);
			TextureRegion workFrame = town.gameWorld.gameResourceConfig.animations.get("human_man_legs").getKeyFrame(CHelper.getDeltaSeconds(town)+town.gameWorld.stateTime+rand.nextFloat()/10, true);
        	worldSpriteBatch.draw(workFrame, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());
        	worldSpriteBatch.setColor(1, 1, 1, 1); //für naked darstellung
		}
		
		if(activeAction!=null && actionanim2==2 && !town.gameWorld.worldPause)
		{
			//Leg Action 1
			TextureRegion workFrame = town.gameWorld.gameResourceConfig.animations.get("human_action6").getKeyFrame(CHelper.getDeltaSeconds(town)+town.gameWorld.stateTime+rand.nextFloat()/10, true);
			worldSpriteBatch.setColor(thehuman.clothingColor_Bottom_Now.r, thehuman.clothingColor_Bottom_Now.g, thehuman.clothingColor_Bottom_Now.b, thehuman.clothingColor_Bottom_Now.a); //für naked darstellung
        	worldSpriteBatch.draw(workFrame, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());
        	
			TextureRegion workFrame0 = town.gameWorld.gameResourceConfig.animations.get("human_action6_shoes").getKeyFrame(CHelper.getDeltaSeconds(town)+town.gameWorld.stateTime+rand.nextFloat()/10, true);
			worldSpriteBatch.setColor(thehuman.clothingColor_Shoes_Now.r, thehuman.clothingColor_Shoes_Now.g, thehuman.clothingColor_Shoes_Now.b, thehuman.clothingColor_Shoes_Now.a); //für naked darstellung
			worldSpriteBatch.draw(workFrame0, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());			
		}
		
		if(activeAction!=null && actionanim2==3)
		{
			//Legs vorne
    		TextureRegion workFrame = town.gameWorld.gameResourceConfig.animations.get("human_action6").getKeyFrames()[0];
    		worldSpriteBatch.setColor(thehuman.clothingColor_Bottom_Now.r, thehuman.clothingColor_Bottom_Now.g, thehuman.clothingColor_Bottom_Now.b, thehuman.clothingColor_Bottom_Now.a); //für naked darstellung
        	worldSpriteBatch.draw(workFrame, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());
        	
    		TextureRegion workFrame0 = town.gameWorld.gameResourceConfig.animations.get("human_action6_shoes").getKeyFrames()[0];
    		worldSpriteBatch.setColor(thehuman.clothingColor_Shoes_Now.r, thehuman.clothingColor_Shoes_Now.g, thehuman.clothingColor_Shoes_Now.b, thehuman.clothingColor_Shoes_Now.a); //für naked darstellung
        	worldSpriteBatch.draw(workFrame0, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());
		}
		
		if(activeAction!=null && actionanim2==4)
		{
			//Legs leicht vorne
    		TextureRegion workFrame = town.gameWorld.gameResourceConfig.animations.get("human_action6").getKeyFrames()[1];
    		worldSpriteBatch.setColor(thehuman.clothingColor_Bottom_Now.r, thehuman.clothingColor_Bottom_Now.g, thehuman.clothingColor_Bottom_Now.b, thehuman.clothingColor_Bottom_Now.a); //für naked darstellung
    		
    		//Vector2 v2 = CHelper.moveVectorByHumanRotation(this, (int)(currentFrame.getRegionWidth()*fsizefactor1), (int)(currentFrame.getRegionHeight()*fsizefactor1), 0, 0);
    		//worldSpriteBatch.draw(workFrame, v2.x, v2.y, currentFrame.getRegionWidth()/2*fsizefactor1, currentFrame.getRegionHeight()/2*fsizefactor1, currentFrame.getRegionWidth()*fsizefactor1, currentFrame.getRegionHeight()*fsizefactor1, 1, 2, rotation());
    		worldSpriteBatch.draw(workFrame, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());
    		
    		TextureRegion workFrame0 = town.gameWorld.gameResourceConfig.animations.get("human_action6_shoes").getKeyFrames()[1];
    		worldSpriteBatch.setColor(thehuman.clothingColor_Shoes_Now.r, thehuman.clothingColor_Shoes_Now.g, thehuman.clothingColor_Shoes_Now.b, thehuman.clothingColor_Shoes_Now.a); //für naked darstellung
        	worldSpriteBatch.draw(workFrame0, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());
		}
		
		if(activeAction!=null && actionanim2==5 && !town.gameWorld.worldPause)
		{
			//Leg Action 2, näher am körper
			TextureRegion workFrame = town.gameWorld.gameResourceConfig.animations.get("human_action6_2").getKeyFrame(CHelper.getDeltaSeconds(town)+town.gameWorld.stateTime+rand.nextFloat()/10, true);
			worldSpriteBatch.setColor(thehuman.clothingColor_Bottom_Now.r, thehuman.clothingColor_Bottom_Now.g, thehuman.clothingColor_Bottom_Now.b, thehuman.clothingColor_Bottom_Now.a); //für naked darstellung
        	worldSpriteBatch.draw(workFrame, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());
        	
			//TextureRegion workFrame0 = gameWorld.gameResourceConfig.animations.get("human_action6_shoes").getKeyFrame(CHelper.getDeltaSeconds(gameWorld)+gameWorld.stateTime+rand.nextFloat()/10, true);
			//worldSpriteBatch.setColor(thehuman.clothingColor_Shoes_Now.r, thehuman.clothingColor_Shoes_Now.g, thehuman.clothingColor_Shoes_Now.b, thehuman.clothingColor_Shoes_Now.a); //für naked darstellung
			//worldSpriteBatch.draw(workFrame0, pos_x(), pos_y(), currentFrame.getRegionWidth()/2*fsizefactor1, currentFrame.getRegionHeight()/2*fsizefactor1, currentFrame.getRegionWidth()*fsizefactor1, currentFrame.getRegionHeight()*fsizefactor1, 1, 1, rotation());			
		}
		
		
		
		
		
		//Arms
		worldSpriteBatch.setColor(1, 1, 1, thehuman.clothingColor_Top_Now.a); //für naked darstellung
		
		if(activeAction!=null && actionanim1==1 && !town.gameWorld.worldPause)
		{
			if(iZombie>=1)
			{
				//Beide Arme arbeiten vorne normale breite
				TextureRegion workFrame = thehuman.getAction1Frame(9);
				//worldSpriteBatch.setColor(0, 0.7f, 0, 1);
				worldSpriteBatch.setColor(0, 0.6f, 0, 1);
				worldSpriteBatch.draw(workFrame, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());
			}
			else
			{
				//Beide Arme arbeiten vorne normale breite
				TextureRegion workFrame = thehuman.getAction1Frame(1);
				worldSpriteBatch.draw(workFrame, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());
			}
		}
		
		if(activeAction!=null && actionanim1==2)
		{
			//Arme nach vorne gerade
    		TextureRegion workFrame = thehuman.getAction1Frame(0);
    		worldSpriteBatch.draw(workFrame, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());
		}
		
		if(activeAction!=null && actionanim1==3)
		{
			//Arme nach vorne offen
    		TextureRegion workFrame = thehuman.getAction1Frame(2);
    		worldSpriteBatch.draw(workFrame, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());
		}
		
		if(activeAction!=null && actionanim1==4 && !town.gameWorld.worldPause)
		{
			//Ein Arm arbeitet vorne
    		TextureRegion workFrame = thehuman.getAction2Frame(0);
    		worldSpriteBatch.draw(workFrame, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());
		}
		
		if(activeAction!=null && actionanim1==5)
		{
			//Ein Arm vorne 1
    		TextureRegion workFrame = thehuman.getAction2Frame(1);
    		worldSpriteBatch.draw(workFrame, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());
		}
		
		if(activeAction!=null && actionanim1==6)
		{
			//Ein Arm vorne 2
    		TextureRegion workFrame = thehuman.getAction2Frame(2);
    		worldSpriteBatch.draw(workFrame, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());
		}
		
		if(activeAction!=null && actionanim1==7 && !town.gameWorld.worldPause)
		{
			//Beide Arme arbeiten vorne breit
    		TextureRegion workFrame = thehuman.getAction3Frame(1);
    		worldSpriteBatch.draw(workFrame, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());
		}
		
		if(activeAction!=null && actionanim1==8)
		{
    		//Beide Arme vorne breit stufe 2
    		TextureRegion workFrame = thehuman.getAction3Frame(0);
    		worldSpriteBatch.draw(workFrame, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());
		}
		
		if(activeAction!=null && actionanim1==9)
		{
			//Beide Arme vorne weniger breit stufe 2
    		TextureRegion workFrame = thehuman.getAction3Frame(2);
    		worldSpriteBatch.draw(workFrame, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());
		}
				
		if(activeAction!=null && actionanim1==10)
		{
			//Beide Arme sehr breit auseinander breit stufe 3
    		TextureRegion workFrame = thehuman.getAction4Frame(0);
    		worldSpriteBatch.draw(workFrame, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());
		}
		
		if(activeAction!=null && actionanim1==11)
		{
			//Beide Arme weniger breit auseinander breit stufe 3
    		TextureRegion workFrame = thehuman.getAction4Frame(1);
    		worldSpriteBatch.draw(workFrame, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());
		}
		
		if(activeAction!=null && actionanim1==12 && !town.gameWorld.worldPause)
		{
			//Arme vor zurück
    		TextureRegion workFrame = thehuman.getAction5Frame(0);
    		worldSpriteBatch.draw(workFrame, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());
		}
		
		if(activeAction!=null && actionanim1==13)
		{
			//Arme vor zurück - vor
    		TextureRegion workFrame = thehuman.getAction5Frame(1);
    		worldSpriteBatch.draw(workFrame, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());
		}
		
		if(activeAction!=null && actionanim1==14)
		{
			//Arme vor zurück - zurück
    		TextureRegion workFrame = thehuman.getAction5Frame(2);
    		worldSpriteBatch.draw(workFrame, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());
		}

		if(activeAction!=null && actionanim1==15 && !town.gameWorld.worldPause)
		{
			//Ein Arm arbeitet vorne, anderer arm leicht hinten
    		TextureRegion workFrame = town.gameWorld.gameResourceConfig.animations.get("human_action8").getKeyFrame(CHelper.getDeltaSeconds(town)+town.gameWorld.stateTime+rand.nextFloat()/10, true);
    		worldSpriteBatch.draw(workFrame, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());
		}
		
		if(activeAction!=null && actionanim1==16)
		{
			//Show laundry in hands 1
    		TextureRegion workFrame = town.gameWorld.gameResourceConfig.animations.get("human_action9").getKeyFrames()[0];
    		worldSpriteBatch.draw(workFrame, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());
		}
		
		if(activeAction!=null && actionanim1==17)
		{
			//Show laundry in hands 2
    		TextureRegion workFrame = town.gameWorld.gameResourceConfig.animations.get("human_action9").getKeyFrames()[1];
    		worldSpriteBatch.draw(workFrame, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());
		}
		
		if(activeAction!=null && actionanim1==18)
		{
			//Show laundry in hands 3
    		TextureRegion workFrame = town.gameWorld.gameResourceConfig.animations.get("human_action9").getKeyFrames()[2];
    		worldSpriteBatch.draw(workFrame, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());
		}
		
		if(activeAction!=null && actionanim1==19)
		{
			//Show laundry in hands 4
    		TextureRegion workFrame = town.gameWorld.gameResourceConfig.animations.get("human_action9").getKeyFrames()[3];
    		worldSpriteBatch.draw(workFrame, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());
		}
		
		if(activeAction!=null && actionanim1==20)
		{
			//Arme am Kopf Anim
    		TextureRegion workFrame = town.gameWorld.gameResourceConfig.animations.get("human_action10").getKeyFrame(CHelper.getDeltaSeconds(town)+town.gameWorld.stateTime+rand.nextFloat()/10, true);
    		worldSpriteBatch.draw(workFrame, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());
		}
		
		if(activeAction!=null && actionanim1==21)
		{
			//Show laundry in hands 5 -> white
    		TextureRegion workFrame = town.gameWorld.gameResourceConfig.animations.get("human_action9").getKeyFrames()[4];
    		worldSpriteBatch.draw(workFrame, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());
		}
		
		if(activeAction!=null && actionanim1==22) //nicht verwenden
		{
			//Hände eng am Körper 1
    		TextureRegion workFrame = town.gameWorld.gameResourceConfig.animations.get("human_action11").getKeyFrames()[0];
    		worldSpriteBatch.draw(workFrame, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());
		}
		
		if(activeAction!=null && actionanim1==23)
		{
			//Hände eng am Körper 2
    		TextureRegion workFrame = town.gameWorld.gameResourceConfig.animations.get("human_action11").getKeyFrames()[1];
    		worldSpriteBatch.draw(workFrame, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());
		}
		
		if(activeAction!=null && actionanim1==24)
		{
			//Hände Trinkaction weiter weg
    		TextureRegion workFrame = town.gameWorld.gameResourceConfig.animations.get("human_action12").getKeyFrames()[0];
    		worldSpriteBatch.draw(workFrame, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());
		}
		
		if(activeAction!=null && actionanim1==25)
		{
			//Hände Trinkaction näher dran
    		TextureRegion workFrame = town.gameWorld.gameResourceConfig.animations.get("human_action12").getKeyFrames()[1];
    		worldSpriteBatch.draw(workFrame, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());
		}
		
		
		//ACHTUNG: && !gameWorld.worldPause
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		//----------------------------------------------------------------------------------
				
		
		
		//NICHT MEHR WIE FOLGT UMSETZEN - DIREKT AUS ACTION HERAUS STEUERN MIT ACTIONANIM VARIABLEN
		
		
		
    	//Workanimation
        if(activeAction!=null && activeActionMode==ActionMode.WORKPLACE && activeAction.targetActionObject!=null && activeAction.bActionMode && !activeAction.bGotoActionMode && !bIsDead && !town.gameWorld.worldPause)
        {
        	Boolean bShowAnim=true;
        	
        	//Alle Office Workingplaces
        	if(activeAction.targetActionObject.theobject.editoraction.contains("officeworkingplace"))
        	{
        		TextureRegion workFrame = thehuman.getAction1Frame(1);
        		worldSpriteBatch.draw(workFrame, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());
        	}
        	
        	//Maintenance Objects
        	if(activeAction.targetActionObject.isMaintenanceObject())
        	{
        		if(activeAction.targetActionObject.theobject.editoraction.contains("company_waterworks_groundwaterextractionsystem"))
        		{
        			TextureRegion workFrame = thehuman.getActionRepairFrame(0);
        			worldSpriteBatch.draw(workFrame, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());
        		}
        		else
        		{
	        		TextureRegion workFrame = thehuman.getAction1Frame(1);
	        		worldSpriteBatch.draw(workFrame, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());
        		}
        	}
        	        	
        	//Supermarket
        	if(activeAction.targetActionObject.theobject.editoraction.contains("supermarket_checkout"))
        	{
        		if(activeAction.targetActionObject.objectFilling>=1) //nur wenn kunde an der kasse
        		{
            		TextureRegion workFrame = thehuman.getAction1Frame(1); //gameWorld.gameResourceConfig.animations.get("human_action1").getKeyFrame(CHelper.getDeltaSeconds(gameWorld)+gameWorld.stateTime+rand.nextFloat()/10, true);
            		worldSpriteBatch.draw(workFrame, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());
        		}
        	}
        	
        	//School / College
        	//hier ersetzen:
			//        	if(activeAction.valueType==ValueType.WORK_COMPLEX)
			//        	{
			//        		bShowAnim=false;
			//        		
			//        		if(activeAction.targetActionObject!=null && 
			//        				activeAction.targetActionObject.theobject.editoraction.contains("company_school_workingplace_")
			//        					//|| activeAction.targetActionObject.theobject.editoraction.contains("company_college_workingplace_studentsdesk")
			//        				)
			//        		{
			//            		TextureRegion workFrame = thehuman.getAction1Frame(1); //gameWorld.gameResourceConfig.animations.get("human_action1").getKeyFrame(CHelper.getDeltaSeconds(gameWorld)+gameWorld.stateTime+rand.nextFloat()/10, true);
			//    	        	worldSpriteBatch.draw(workFrame, pos_x(), pos_y(), currentFrame.getRegionWidth()/2*fsizefactor1, currentFrame.getRegionHeight()/2*fsizefactor1, currentFrame.getRegionWidth()*fsizefactor1, currentFrame.getRegionHeight()*fsizefactor1, 1, 1, rotation());
			//        		}
			//        	}
        }
        
        //Supermarket Refill Shelf
        //if(activeAction!=null && activeActionMode==ActionMode.WORKPLACE && activeAction.bActionMode && !activeAction.bGotoActionMode && !bIsDead)
        //{
        //	if(activeAction.targetActionObject.theobject.editoraction.contains("supermarket_pallettruck"))
        //	{
	        	//Truck fahren: Arme vorne
	        	//if(activeAction.actionState<3 || activeAction.actionState>=6)
	        	//{
	        	//	TextureRegion workFrame = thehuman.getAction1Frame(0);
	        	//	//TextureRegion workFrame = gameWorld.gameResourceConfig.animations.get("human_action1").getKeyFrame(0, true);
	        	//	worldSpriteBatch.draw(workFrame, pos_x(), pos_y(), currentFrame.getRegionWidth()/2*fsizefactor1, currentFrame.getRegionHeight()/2*fsizefactor1, currentFrame.getRegionWidth()*fsizefactor1, currentFrame.getRegionHeight()*fsizefactor1, 1, 1, rotation());
	        	//}
	        	
	        	//Shelf auffüllen
	        	//if(activeAction.actionState==4)
	        	//{
	        	//	if(deltaSecond>0.7f)
	        	//	{
	        	//		TextureRegion workFrame = thehuman.getAction1Frame(1);
			     //   	//TextureRegion workFrame = gameWorld.gameResourceConfig.animations.get("human_action1").getKeyFrame(gameWorld.stateTime, true);
			      //  	worldSpriteBatch.draw(workFrame, pos_x(), pos_y(), currentFrame.getRegionWidth()/2*fsizefactor1, currentFrame.getRegionHeight()/2*fsizefactor1, currentFrame.getRegionWidth()*fsizefactor1, currentFrame.getRegionHeight()*fsizefactor1, 1, 1, rotation());
	        	//	}
	        	//}
        //	}
        //}
        
        //Bookshelf
        if(activeAction!=null && activeActionMode==ActionMode.READ_BOOK && (activeAction.actionState==2 || activeAction.actionState==6) && !town.gameWorld.worldPause)
        {
    		TextureRegion actionFrame = thehuman.getAction1Frame(1);
        	worldSpriteBatch.draw(actionFrame, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());
        }
                
        //Wash hands
//        if(activeAction!=null && activeActionMode==ActionMode.TOILET && activeAction.actionState==2 && !gameWorld.worldPause)
//        {
//    		TextureRegion actionFrame = thehuman.getAction1Frame(1);
//        	worldSpriteBatch.draw(actionFrame, pos_x(), pos_y(), currentFrame.getRegionWidth()/2*fsizefactor1, currentFrame.getRegionHeight()/2*fsizefactor1, currentFrame.getRegionWidth()*fsizefactor1, currentFrame.getRegionHeight()*fsizefactor1, 1, 1, rotation());
//        }
        
        //Snacken
        //if(activeAction!=null && activeActionMode==ActionMode.FRIDGE && activeAction.bActionMode && !gameWorld.worldPause)
        //{
        //	if(deltaSecond>0.7f)
        //	{
	    //		TextureRegion actionFrame = thehuman.getAction1Frame(1);
	    //		worldSpriteBatch.draw(actionFrame, pos_x(), pos_y(), currentFrame.getRegionWidth()/2*fsizefactor1, currentFrame.getRegionHeight()/2*fsizefactor1, currentFrame.getRegionWidth()*fsizefactor1, currentFrame.getRegionHeight()*fsizefactor1, 1, 1, rotation());
        //	}
        //}
        
        
        //Supermarket Buy in: Special Draw
        //if(activeAction!=null && activeActionMode==ActionMode.SUPERMARKET_BUYIN && activeAction.bActionMode && !bIsDead && !gameWorld.worldPause)
        //{
        	//Wagen schieben: Arme vorne
        	//if(activeAction.actionState<4 || activeAction.actionState==5 || activeAction.actionState==6 || activeAction.actionState==8 || activeAction.actionState == 10 || activeAction.actionState==11 || activeAction.actionState==13)
        	//{
        	//	TextureRegion workFrame = thehuman.getAction1Frame(0);
	        	//TextureRegion workFrame = gameWorld.gameResourceConfig.animations.get("human_action1").getKeyFrame(0, true);
	        //	worldSpriteBatch.draw(workFrame, pos_x(), pos_y(), currentFrame.getRegionWidth()/2*fsizefactor1, currentFrame.getRegionHeight()/2*fsizefactor1, currentFrame.getRegionWidth()*fsizefactor1, currentFrame.getRegionHeight()*fsizefactor1, 1, 1, rotation());
        	//}
        	
        	//Show Humen Action Animation
        	//if(activeAction.actionState==4 || activeAction.actionState==7 || activeAction.actionState==9 || activeAction.actionState==12) 
        	//{
        		//4 Einkaufswagen mit Waren befüllen
        		//7 Waren aufs Band legen
        		//9 Waren vom Band in den Wagen
        		//11 Waren vom Wagen in den Kühlschrank
        		
        		//if(deltaSecond>0.7f)
        		//{
        		//	TextureRegion workFrame = thehuman.getAction1Frame(1);
		        	//TextureRegion workFrame = gameWorld.gameResourceConfig.animations.get("human_action1").getKeyFrame(gameWorld.stateTime, true);
		        //	worldSpriteBatch.draw(workFrame, pos_x(), pos_y(), currentFrame.getRegionWidth()/2*fsizefactor1, currentFrame.getRegionHeight()/2*fsizefactor1, currentFrame.getRegionWidth()*fsizefactor1, currentFrame.getRegionHeight()*fsizefactor1, 1, 1, rotation());
        		//}
        	//}
        //}
        
        //Wash Dishes
		if(activeAction!=null && activeAction.actionMode!=null && town.gameWorld!=null && activeAction.actionMode==ActionMode.WASH_DISHES && activeAction.bActionMode && !bIsDead && !town.gameWorld.worldPause)
		{
			if(activeAction.actionState==2 || activeAction.actionState==4 || activeAction.actionState==6)
			{
	    		TextureRegion actionFrame = thehuman.getAction1Frame(1);
	    		worldSpriteBatch.draw(actionFrame, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());
			}
		}
        
        //Cook Dinner
		if(activeAction!=null && activeAction.actionMode!=null && activeAction.actionMode==ActionMode.COOK_DINNER && activeAction.bActionMode && !bIsDead && !town.gameWorld.worldPause)
		{
			if(activeAction.actionState==-1 || activeAction.actionState==3 || activeAction.actionState==8 || activeAction.actionState==10 || activeAction.actionState==13 || activeAction.actionState==14)
			{
	    		TextureRegion actionFrame = thehuman.getAction1Frame(1);
	    		worldSpriteBatch.draw(actionFrame, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());
			}
		}
		
        //Eat Dinner
		if(activeAction!=null && activeAction.actionMode!=null && activeAction.actionMode==ActionMode.EAT_DINNER && activeAction.bActionMode && !bIsDead && !town.gameWorld.worldPause)
		{
			if(activeAction.actionState==2)
			{
				Boolean bShow=false;
				
				//Animation nur anzeigen wenn noch essen auf dem Teller ist
				if(activeAction.targetActionObject.owner!=null && activeAction.targetActionObject.owner.uniqueId==uniqueId && activeAction.targetActionObject.actionvar1>0)
					bShow=true;
				if(activeAction.targetActionObject.owner2!=null && activeAction.targetActionObject.owner2.uniqueId==uniqueId && activeAction.targetActionObject.actionvar2>0)
					bShow=true;
				if(activeAction.targetActionObject.owner3!=null && activeAction.targetActionObject.owner3.uniqueId==uniqueId && activeAction.targetActionObject.actionvar3>0)
					bShow=true;
				if(activeAction.targetActionObject.owner4!=null && activeAction.targetActionObject.owner4.uniqueId==uniqueId && activeAction.targetActionObject.actionvar4>0)
					bShow=true;
				if(activeAction.targetActionObject.owner5!=null && activeAction.targetActionObject.owner5.uniqueId==uniqueId && activeAction.targetActionObject.actionvar5>0)
					bShow=true;
				if(activeAction.targetActionObject.owner6!=null && activeAction.targetActionObject.owner6.uniqueId==uniqueId && activeAction.targetActionObject.actionvar6>0)
					bShow=true;
				if(activeAction.targetActionObject.owner7!=null && activeAction.targetActionObject.owner7.uniqueId==uniqueId && activeAction.targetActionObject.actionvar7>0)
					bShow=true;
				if(activeAction.targetActionObject.owner8!=null && activeAction.targetActionObject.owner8.uniqueId==uniqueId && activeAction.targetActionObject.actionvar8>0)
					bShow=true;
				
				if(bShow && deltaSecond>rand.nextFloat())
				{
		    		TextureRegion actionFrame = thehuman.getAction1Frame(1);
		        	worldSpriteBatch.draw(actionFrame, pos_x(), pos_y(), w1/2*fsizefactor1, h1/2*fsizefactor1, w1*fsizefactor1, h1*fsizefactor1, 1, 1, rotation());
				}
			}
		}
	}
	
	public void drawHuman_UnderBody(SpriteBatch worldSpriteBatch)
	{
		//Beerdigung: Sarg tragen
		if(activeAction != null && activeActionMode == CAction.ActionMode.WORKPLACE && activeAction.bActionMode && !bIsDead)
		{
			if(activeAction.targetActionObject!=null && activeAction.targetActionObject.theobject.editoraction.contains("company_urbancemetery_hearse_traffic_car"))
			{
				if(activeAction.actionState==4 || //bringe sarg zum wagen 
						activeAction.actionState==5 || //bringe sarg zum wagen 
						activeAction.actionState==6 || //bringe sarg zum wagen
						activeAction.actionState==10 //bringe sarg zum rednerpult
						//actionstring1.equals("show_coffin") //zeige coffin anstatt resident 
						) 
				{
					Texture texture_coffin = town.gameResourceConfig.textures.get("urbancemetery_coffin");
					CWorldObject wobj = this;
		    		int w=100;
		    		int h=240;
		    		if(town.fSizeFactor>0)
		    		{
			    		w/=town.fSizeFactor;
			    		h/=town.fSizeFactor;
		    		}
					Vector2 v2 = CHelper.moveVectorByRotationS2D(wobj.pos_x(), wobj.pos_y(), 65, -80, wobj.width/2, wobj.height/2, wobj.rotation());
					worldSpriteBatch.setColor(1, 1, 1, 1);
					town.gameWorld.worldSpriteBatch.draw(texture_coffin, v2.x-w/2, v2.y-h/2, w/2, h/2, w, h, 1, 1, wobj.rotation(), 0, 0, texture_coffin.getWidth(), texture_coffin.getHeight(), false, false);
				}
			}
		}
		
		//Bathtub
		if(activeAction != null && activeActionMode == CAction.ActionMode.SHOWER && activeAction.bActionMode && activeAction.targetActionObject!=null && !bIsDead)
		{
			String edaction=activeAction.targetActionObject.theobject.editoraction;
			if(activeAction.targetActionObject.actionfield1==1 && edaction.contains("bathroom_bathtub"))
			{
				TextureRegion waterFrame=null;
				TextureRegion waterFrame2=null;
				waterFrame = town.gameWorld.gameResourceConfig.animations.get("anim_shower").getKeyFrame(town.gameWorld.getStateTime(), true);
				waterFrame2 = town.gameWorld.gameResourceConfig.animations.get("anim_shower2").getKeyFrame(town.gameWorld.getStateTime(), true);
				worldSpriteBatch.setColor(1f, 1f, 1f, 0.13f);
				
				//Variante 2: Anim wird auf Grundlage des Targetobjekts gezeichnet
				if(edaction.contains("bathroom_bathtub2"))
				{
					int tm=15;
					worldSpriteBatch.draw(waterFrame2, activeAction.targetActionObject.pos_x()+tm, activeAction.targetActionObject.pos_y()+tm, activeAction.targetActionObject.width/2-tm, activeAction.targetActionObject.height/2-tm, activeAction.targetActionObject.width-tm*2, activeAction.targetActionObject.height-tm*2, 1, 1, activeAction.targetActionObject.rotation());
					worldSpriteBatch.draw(waterFrame2, activeAction.targetActionObject.pos_x()+tm+2, activeAction.targetActionObject.pos_y()+tm+2, activeAction.targetActionObject.width/2-tm, activeAction.targetActionObject.height/2-tm, activeAction.targetActionObject.width-(tm-2)*2, activeAction.targetActionObject.height-(tm-2)*2, 1, 1, activeAction.targetActionObject.rotation());
					worldSpriteBatch.draw(waterFrame2, activeAction.targetActionObject.pos_x()+tm, activeAction.targetActionObject.pos_y()+tm, activeAction.targetActionObject.width/2-tm, activeAction.targetActionObject.height/2-tm, activeAction.targetActionObject.width-tm*2, activeAction.targetActionObject.height-tm*2, 1, 1, activeAction.targetActionObject.rotation()+180);
					worldSpriteBatch.draw(waterFrame2, activeAction.targetActionObject.pos_x()+tm+2, activeAction.targetActionObject.pos_y()+tm+2, activeAction.targetActionObject.width/2-tm, activeAction.targetActionObject.height/2-tm, activeAction.targetActionObject.width-(tm+2)*2, activeAction.targetActionObject.height-(tm+2)*2, 1, 1, activeAction.targetActionObject.rotation()+180);
					worldSpriteBatch.setColor(1, 1, 1, town.gameWorld.alphaWorldObjects);
				}
				else if(edaction.contains("bathroom_bathtub3"))
				{
					int tm=10;
					worldSpriteBatch.draw(waterFrame2, activeAction.targetActionObject.pos_x()+tm, activeAction.targetActionObject.pos_y()+tm, activeAction.targetActionObject.width/2-tm, activeAction.targetActionObject.height/2-tm, activeAction.targetActionObject.width-tm*2, activeAction.targetActionObject.height-tm*2, 1, 1, activeAction.targetActionObject.rotation());
					worldSpriteBatch.draw(waterFrame2, activeAction.targetActionObject.pos_x()+tm+2, activeAction.targetActionObject.pos_y()+tm+2, activeAction.targetActionObject.width/2-tm, activeAction.targetActionObject.height/2-tm, activeAction.targetActionObject.width-(tm-2)*2, activeAction.targetActionObject.height-(tm-2)*2, 1, 1, activeAction.targetActionObject.rotation());
					worldSpriteBatch.draw(waterFrame2, activeAction.targetActionObject.pos_x()+tm, activeAction.targetActionObject.pos_y()+tm, activeAction.targetActionObject.width/2-tm, activeAction.targetActionObject.height/2-tm, activeAction.targetActionObject.width-tm*2, activeAction.targetActionObject.height-tm*2, 1, 1, activeAction.targetActionObject.rotation()+180);
					worldSpriteBatch.draw(waterFrame2, activeAction.targetActionObject.pos_x()+tm+2, activeAction.targetActionObject.pos_y()+tm+2, activeAction.targetActionObject.width/2-tm, activeAction.targetActionObject.height/2-tm, activeAction.targetActionObject.width-(tm+2)*2, activeAction.targetActionObject.height-(tm+2)*2, 1, 1, activeAction.targetActionObject.rotation()+180);
					worldSpriteBatch.setColor(1, 1, 1, town.gameWorld.alphaWorldObjects);
				}
				else
				{
					int tm=20;
					worldSpriteBatch.draw(waterFrame, activeAction.targetActionObject.pos_x()+tm, activeAction.targetActionObject.pos_y()+tm, activeAction.targetActionObject.width/2-tm, activeAction.targetActionObject.height/2-tm, activeAction.targetActionObject.width-tm*2, activeAction.targetActionObject.height-tm*2, 1, 1, activeAction.targetActionObject.rotation());
					worldSpriteBatch.draw(waterFrame, activeAction.targetActionObject.pos_x()+tm+2, activeAction.targetActionObject.pos_y()+tm+2, activeAction.targetActionObject.width/2-tm, activeAction.targetActionObject.height/2-tm, activeAction.targetActionObject.width-(tm-2)*2, activeAction.targetActionObject.height-(tm-2)*2, 1, 1, activeAction.targetActionObject.rotation());
					worldSpriteBatch.draw(waterFrame, activeAction.targetActionObject.pos_x()+tm, activeAction.targetActionObject.pos_y()+tm, activeAction.targetActionObject.width/2-tm, activeAction.targetActionObject.height/2-tm, activeAction.targetActionObject.width-tm*2, activeAction.targetActionObject.height-tm*2, 1, 1, activeAction.targetActionObject.rotation()+180);
					worldSpriteBatch.draw(waterFrame, activeAction.targetActionObject.pos_x()+tm+2, activeAction.targetActionObject.pos_y()+tm+2, activeAction.targetActionObject.width/2-tm, activeAction.targetActionObject.height/2-tm, activeAction.targetActionObject.width-(tm+2)*2, activeAction.targetActionObject.height-(tm+2)*2, 1, 1, activeAction.targetActionObject.rotation()+180);
					worldSpriteBatch.setColor(1, 1, 1, town.gameWorld.alphaWorldObjects);
				}
			}
		}
		
		//Washing dishes
		if(activeAction != null && (activeActionMode == CAction.ActionMode.WASH_DISHES || activeActionMode == CAction.ActionMode.BREAK_ROOM) && activeAction.bActionMode && !bIsDead)
		{
			Boolean bShow = false;

			CWorldObject sink=null;
			
			//Dinner
			if(activeActionMode == CAction.ActionMode.WASH_DISHES && activeAction.targetActionObject2!=null)
			{
				if(activeAction.actionState==4)
				{
					bShow=true;
					sink=activeAction.targetActionObject2;
				}
			}
			
			//Coffee
			if(activeActionMode == CAction.ActionMode.BREAK_ROOM && 
					activeAction.targetActionObject!=null && 
					activeAction.targetActionObject.theobject.editoraction.contains("coffeepot"))
			{
				if(activeAction.actionState==3)
				{
					bShow=true;
					sink=activeAction.targetActionObject3;
				}
			}
			
			if(bShow)
			{
				TextureRegion waterFrame=null;
				waterFrame = town.gameWorld.gameResourceConfig.animations.get("anim_shower").getKeyFrame(town.gameWorld.getStateTime(), true);
				worldSpriteBatch.setColor(0, 0, 1, 0.2f);

				//Variante 2: Anim wird auf Grundlage des Targetobjekts gezeichnet
				Vector2 v2 = CHelper.moveVectorByRotationS2D(sink.pos_x(), sink.pos_y(), 58, 26, sink.width/2, sink.height/2, sink.rotation());
				int tm=0;
				int w=60;
				int h=87;
	    		if(town.fSizeFactor>0)
	    		{
		    		w/=town.fSizeFactor;
		    		h/=town.fSizeFactor;
	    		}				
				
				worldSpriteBatch.draw(waterFrame, (int)v2.x-w/2, (int)v2.y-h/2, w/2, h/2, w, h, 1, 1, sink.rotation());
				worldSpriteBatch.draw(waterFrame, (int)v2.x-w/2, (int)v2.y-h/2, w/2, h/2, w, h, 1, 1, sink.rotation());
				worldSpriteBatch.setColor(1, 1, 1, town.gameWorld.alphaWorldObjects);
			}
		}
		
		//Washing Hands
		if(activeAction != null && activeActionMode == CAction.ActionMode.TOILET && activeAction.bActionMode && activeAction.targetActionObject2!=null && !bIsDead)
		{
			//if(activeAction.actionState==4)
			//sink, händfe waschen
			//if(activeAction.actionState==6)
			//{
			//	TextureRegion waterFrame=null;
			//	waterFrame = gameWorld.gameResourceConfig.animations.get("anim_shower").getKeyFrame(gameWorld.getStateTime(), true);
			//	worldSpriteBatch.setColor(1, 1, 1, 0.5f);
			//	int tm=10;
			//	CWorldObject obj = activeAction.targetActionObject2;
			//	int size=50;
			//	Vector2 v2 = CHelper.moveVectorByRotationS2D(obj.pos_x(), obj.pos_y(), obj.width/2-size/2, 0, obj.width/2, obj.height/2, obj.rotation());
			//	int px = (int)v2.x;
			//	int py = (int)v2.y;
			//	worldSpriteBatch.draw(waterFrame, px, py,  obj.width/2, obj.height/2, size, size, 1, 1, obj.rotation());
			//	worldSpriteBatch.draw(waterFrame, px, py,  obj.width/2, obj.height/2, size, size, 1, 1, obj.rotation());
			//	worldSpriteBatch.setColor(1, 1, 1, gameWorld.alphaWorldObjects);
			//}
		}
	}
	
	public void drawHumanAction_OverHead(SpriteBatch worldSpriteBatch, TextureRegion currentFrame, float fsizefactor, float fsizefactor1, float rotmod)
	{
		
    	//Paperflyer in School
		
//        if(activeAction!=null && activeAction.valueType==ValueType.WORK_COMPLEX && activeAction.bActionMode && !bIsDead && !gameWorld.worldPause)
//        {
//        	if(activeAction.targetActionObject!=null && activeAction.targetActionObject.theobject.editoraction.contains("company_school_workingplace_studentsdesk"))
//        	{
//        		if(tempcount>0)
//        		{
//        			if(deltaSecond>0.02f)
//        			{
//	        			float speed=10;
//	        			
//	        			if(actionvar3>actionvar1)
//	        				actionvar3-=speed;
//	        			if(actionvar3<actionvar1)
//	        				actionvar3+=speed;
//	        			
//	        			if(actionvar4>actionvar2)
//	        				actionvar4-=speed;
//	        			if(actionvar4<actionvar2)
//	        				actionvar4+=speed;
//        			}
//        			
//        			
//        			float rotFlyer=0;
//        			
//    		        if(Math.abs(actionvar1 - (int)actionvar3) > 10 || Math.abs(actionvar2 - (int)actionvar4) > 10)
//    		        {
//    		        	rotFlyer = (float) Math.atan2((float)actionvar2 - (float)actionvar4, (float)(actionvar1 - (actionvar3)));
//    		        	rotFlyer = MathUtils.radiansToDegrees*rotFlyer;
//    		        	rotFlyer=rotFlyer+270;
//    		        }
//    		        
//        			worldSpriteBatch.setColor(1,1,1,1f);
//        			Texture flyerTexture = gameWorld.gameResourceConfig.textures.get("school_paperflyer");
//        			worldSpriteBatch.draw(flyerTexture, actionvar3, actionvar4, 20, 30, 40, 60, 1, 1, rotFlyer, 0,0,flyerTexture.getWidth(), flyerTexture.getHeight(), false, false);
//        			
//        			if(Math.abs(actionvar1-actionvar3)<50 && Math.abs(actionvar2-actionvar4)<50)
//        			{
//        				//bleibt eine weile liegen
//        				if(deltaSecond>0.1f)
//        					tempcount++;
//        			}
//        			
//        			if(tempcount>100) 
//        			{
//        				tempcount=0;
//        				actionvar1=0;
//        				actionvar2=0;
//        				actionvar3=0;
//        				actionvar4=0;
//        			}
//        		}
//        	}
//        }
		
		

//		//Lat Pulldown
//		if(activeAction!=null && activeAction.actionMode==ActionMode.FITNESS_STUDIO && activeAction.bActionMode && !bIsDead)
//		{
//			if(activeAction.targetActionObject!=null && activeAction.targetActionObject.theobject.editoraction.contains("company_fitnessstudio_latpull"))
//			{
//				worldSpriteBatch.setColor(1,1,1,1f);
//				Texture lat2 = town.gameResourceConfig.textures.get("fitnessstudio_lat2");
//				Texture lat3 = town.gameResourceConfig.textures.get("fitnessstudio_lat3");
//				CWorldObject wobj = activeAction.targetActionObject;
//	    		int w_lat2=150;
//	    		int h_lat2=10;
//	    		
//	    		float zx=1;
//	    		int movy=100;
//	    		if(wobj.actionfield1==1)
//	    		{
//	    			zx=0.8f;
//	    			movy=93;
//	    		}
//	    		
//				Vector2 v2 = CHelper.moveVectorByRotationS2D(wobj.pos_x(), wobj.pos_y(), 65, movy, wobj.width/2, wobj.height/2, wobj.rotation());
//	    		gameWorld.worldSpriteBatch.draw(lat2, v2.x-w_lat2/2, v2.y-h_lat2/2, w_lat2/2, h_lat2/2, w_lat2, h_lat2, zx, zx, wobj.rotation(), 0, 0, lat2.getWidth(), lat2.getHeight(), false, false);
//			}
//		}
		
		
		//Demand anzeigen
		if(!bIsDead && iZombie<1 && !town.gameWorld.worldPause)
		{
			//int speakDuration=20+rand.nextInt(20);
			//int speakDuration=20+rand.nextInt(100);
			//fRenderSpeakTime-=CHelper.getDeltaSeconds(gameWorld);
						
			//if(((renderHour>thehuman.getAge()*20) && (renderHour<thehuman.getAge()*20+10)) || fRenderSpeakTime<speakDuration)
			if(fRenderSpeakTime<0)
			{
				
				if(fRenderSpeakTime<-50)
					fRenderSpeakTime=50+rand.nextInt(50);

//				fRenderSpeakTime-=CHelper.getDeltaSeconds(gameWorld);
//				if(fRenderSpeakTime<0)
//					fRenderSpeakTime=speakDuration;
//				if(fRenderSpeakTime>speakDuration)
//					fRenderSpeakTime=speakDuration;
				
				//int ieat = (int) (thehuman.eatValueTrigger-thehuman.eatValue);
				//int isleep = (int) (thehuman.sleepValueTrigger-thehuman.sleepValue);
				//int ienergy = (int) thehuman.energyValue;
				//int iclean = (int) (thehuman.cleanValueTrigger-thehuman.cleanValue);
				//int iclothing = (int) (thehuman.clothingValueTrigger-thehuman.clothingValue);
				//int itoilet = (int) (thehuman.toiletValueTrigger-thehuman.toiletValue);
				
				Boolean eat1 = thehuman.eatValue>=thehuman.eatValueTrigger3;
				Boolean sleep1 = thehuman.sleepValue>=thehuman.sleepValueTrigger3;
				Boolean clean1 = thehuman.cleanValue>=thehuman.cleanValueTriggerRed;
				Boolean clothing1 = thehuman.clothingValue>=thehuman.clothingValueTriggerRed;
				Boolean toilet1 = thehuman.toiletValue>=thehuman.toiletValueTrigger3;
				
				
				int ivalue=0;
				int posx=0;
				int posy=0;
				
				Texture texture1 = null;
				
				//Open Demand
				//if(texture1==null)
				{
					ArrayList<String> demandList = thehuman.getOpenDemandList();
					
					if(demandList.size()>0)
					{
						String str1 = demandList.get(0);
						texture1 = CCompany.getCompanyIconByCompanyTypeString(town, str1);
						
						//TV TV2 BOOK DININGTABLE SPORTSCAR SANDPIT SLIDE SEESAW
						if(texture1==null)
							texture1=town.gameResourceConfig.textures.get("demand_"+str1.toLowerCase());
					}
				}
				
				//if(iclothing<ivalue)
				if(clothing1)
				{
					texture1 = town.gameWorld.gameResourceConfig.textures.get("guiinfo_clothing");
					//ivalue=iclothing;
				}
				//if(iclean<ivalue)
				if(clean1)
				{
					texture1 = town.gameWorld.gameResourceConfig.textures.get("info_attr_clean");
					//ivalue=iclean;
					posx=-3;
				}
				//if(itoilet<ivalue)
				if(toilet1)
				{
					texture1 = town.gameWorld.gameResourceConfig.textures.get("info_attr_toilet");
					//ivalue=itoilet;
					posx=-1;
					posy=-2;
				}
				//if(isleep<ivalue)
				if(sleep1)
				{
					texture1 = town.gameWorld.gameResourceConfig.textures.get("info_attr_sleep");
					//ivalue=isleep;
				}
				//if(ieat<ivalue)
				if(eat1)
				{
					texture1 = town.gameWorld.gameResourceConfig.textures.get("info_attr_eat");
					//ivalue=ieat;
				}
				if(texture1!=null)
				{
					if(thehuman.energyValue < thehuman.energyValueTrigger)
						texture1 = town.gameWorld.gameResourceConfig.textures.get("guiinfo_residentenergy");
				}
				
				//texture1 = CCompany.getCompanyIconByCompanyTypeString(town, "CHURCH");
				
				if(texture1!=null)
				{
					float aval = rand.nextFloat(); 
					if(aval<0.5f)
						aval=0.5f;
					
					Texture textureSpeak = town.gameWorld.gameResourceConfig.textures.get("speak_empty");
					worldSpriteBatch.setColor(1,1,1,aval);
					town.gameWorld.worldSpriteBatch.draw(textureSpeak, pos_x()+width_human(), pos_y()+height_human(), 120, 146);
					
					worldSpriteBatch.setColor(0.15f,0.15f,0.15f,aval);
					town.gameWorld.worldSpriteBatch.draw(texture1, pos_x()+width_human()+28+posx, pos_y()+height_human()+50+posy, 70, 70);
					worldSpriteBatch.setColor(1,1,1,1f);
				}
			}
		}
		
		//Abtrocknen nach Shower, Bath
		Boolean bcheck=false;
		if(activeAction!=null)
			if(activeAction.actionMode!=null)
				if(activeAction.actionMode==ActionMode.SHOWER)
					if(activeAction.bActionMode)
						if(!bIsDead)
							bcheck=true;
				
		//if(activeAction!=null && activeAction.actionMode!=null && activeAction.actionMode==ActionMode.SHOWER && activeAction.bActionMode && !bIsDead)
		if(bcheck)
		{
			if(activeAction.actionState==5)
			{
				worldSpriteBatch.setColor(1,1,1,1f);
				Texture texture = town.gameWorld.gameResourceConfig.textures.get("bathroom_towel");
				int w=40+rand.nextInt(20);
				int h=40+rand.nextInt(20);
				int mx=rand.nextInt(30)-rand.nextInt(30);
				int my=rand.nextInt(30)-rand.nextInt(30);
				
	    		if(town.fSizeFactor>0)
	    		{
		    		w/=town.fSizeFactor;
		    		h/=town.fSizeFactor;
	    		}		
	    		if(town.fSizeFactor>0)
	    		{
	    			mx/=town.fSizeFactor;
		    		my/=town.fSizeFactor;
	    		}		
				CWorldObject wobj = activeAction.baseWorldObject;
				
				Vector2 v2 = CHelper.moveVectorByRotationS2D(wobj.pos_x()+mx, wobj.pos_y()+my, 65, 60, wobj.width/2, wobj.height/2, wobj.rotation());
				town.gameWorld.worldSpriteBatch.draw(texture, v2.x-w/2, v2.y-h/2, w/2, h/2, w, h, 1, 1, wobj.rotation(), 0, 0, texture.getWidth(), texture.getHeight(), false, false);
			}
		}
		
		
		//Umziehen, change clothes, change_clothes
		if(activeAction!=null && activeAction.actionMode!=null && activeAction.actionMode==ActionMode.CHANGE_CLOTHES && activeAction.bActionMode && !bIsDead)
		{
			if(activeAction.actionState==3)
			{
				//worldSpriteBatch.setColor(0.1f,0.1f,0.1f,0.9f);
				worldSpriteBatch.setColor(0,0,0,0.7f);
				Texture texture = town.gameWorld.gameResourceConfig.textures.get("bathroom_towel");
				int w=40+rand.nextInt(70);
				int h=40+rand.nextInt(70);
				int mx=rand.nextInt(20)-rand.nextInt(40);
				int my=rand.nextInt(20)-rand.nextInt(40);
				
	    		if(town.fSizeFactor>0)
	    		{
		    		w/=town.fSizeFactor;
		    		h/=town.fSizeFactor;
	    		}	
	    		if(town.fSizeFactor>0)
	    		{
		    		mx/=town.fSizeFactor;
		    		my/=town.fSizeFactor;
	    		}	
				
				CWorldObject wobj = activeAction.baseWorldObject;
				
				Vector2 v2 = CHelper.moveVectorByRotationS2D(wobj.pos_x()+mx, wobj.pos_y()+my, (int)town.getSizeValue(65), (int)town.getSizeValue(60), wobj.width/2, wobj.height/2, wobj.rotation());
				town.gameWorld.worldSpriteBatch.draw(texture, v2.x-w/2, v2.y-h/2, w/2, h/2, w, h, 1, 1, wobj.rotation(), 0, 0, texture.getWidth(), texture.getHeight(), false, false);
	    		worldSpriteBatch.setColor(1f,1f,1f,1f);
			}
		}
		
		
    	//Schaukel Balken über Human
		if(activeAction!=null && activeAction.actionMode!=null && activeAction.actionMode==ActionMode.PLAYGROUND && activeAction.bActionMode && !bIsDead)
		{
			if(activeAction.targetActionObject!=null && activeAction.targetActionObject.theobject.editoraction.contains("company_playground_swing"))
			{
				worldSpriteBatch.setColor(1,1,1,1f);
				Texture texture = activeAction.targetActionObject.theobject.textureImage2; //gameWorld.gameResourceConfig.textures.get("playground_swing_rail");
				town.gameWorld.worldSpriteBatch.draw(texture, activeAction.targetActionObject.pos_x(), activeAction.targetActionObject.pos_y(), activeAction.targetActionObject.width/2, activeAction.targetActionObject.height/2, activeAction.targetActionObject.width, activeAction.targetActionObject.height, 1, 1, activeAction.targetActionObject.rotation(), 0, 0, texture.getWidth(), texture.getHeight(), false, false);
			}
		}
		
    	//Sleepanimation
        if(activeAction!=null && activeAction.actionMode!=null && activeAction.valueType==ValueType.SLEEP && activeAction.bActionMode && !bIsDead && !town.gameWorld.worldPause)
        {	    	
        	worldSpriteBatch.setColor(1, 1, 1, 0.6f);
        	
        	TextureRegion sleepFrame = town.gameWorld.gameResourceConfig.animations.get("anim_sleep").getKeyFrame(town.gameWorld.stateTime, true);
        	//TextureRegion sleepFrame = gameWorld.gameResourceConfig.animations.get("anim_sleep").getKeyFrame(gameWorld.stateTime/2.5f, true);
        	//TextureRegion sleepFrame = gameWorld.gameResourceConfig.animations.get("anim_sleep").getKeyFrame((CHelper.getDeltaSeconds(gameWorld)+gameWorld.stateTime+rand.nextFloat()/10), true);
        	//TextureRegion sleepFrame = gameWorld.gameResourceConfig.animations.get("anim_sleep").getKeyFrame(CHelper.getDeltaSeconds(gameWorld)+0.08f, true);
        	
        	float w1 = getRegionWidth(currentFrame);
        	float h1 = getRegionHeight(currentFrame);
        	
        	float wp = w1/1.4f/fsizefactor;
        	float hp = h1/1.4f/fsizefactor;
        	worldSpriteBatch.draw(sleepFrame, pos_x()+wp, pos_y()+hp, w1/2/fsizefactor-wp, h1/2/fsizefactor-hp, 80/fsizefactor, 90/fsizefactor, 1, 1, rotation()+rotmod);
        	worldSpriteBatch.setColor(1, 1, 1, 1f);
        }		
		
		
		//Showeranimation
		if(activeAction != null && activeAction.actionMode!=null && activeActionMode == CAction.ActionMode.SHOWER && activeAction.bActionMode && activeAction.targetActionObject!=null && !bIsDead)
		{
			if(activeAction.targetActionObject.actionfield1==1 && activeAction.targetActionObject.theobject.editoraction.contains("bathroom_shower"))
			{
				TextureRegion waterFrame=null;
				waterFrame = town.gameWorld.gameResourceConfig.animations.get("anim_shower").getKeyFrame(town.gameWorld.getStateTime(), true);
				//worldSpriteBatch.setColor(1f, 1f, 1f, 0.4f);
				
				//Variante 2: Anim wird auf Grundlage des Targetobjekts gezeichnet
				//if(activeAction.targetActionObject.theobject.editoraction.contains("bathroom_shower2"))
				{
					worldSpriteBatch.setColor(1f, 1f, 1f, 0.3f);
					Vector2 v2 = CHelper.moveVectorByRotationS2D(activeAction.targetActionObject.pos_x(), activeAction.targetActionObject.pos_y(), activeAction.targetActionObject.width/2, activeAction.targetActionObject.height/2, activeAction.targetActionObject.width/2, activeAction.targetActionObject.height/2, rotation());
					int w1=activeAction.targetActionObject.width;
					int h1=activeAction.targetActionObject.height;
					int tm=17;
					worldSpriteBatch.draw(waterFrame, v2.x-w1/2+tm, v2.y-h1/2+tm, w1/2-tm, h1/2-tm, w1-tm*2, h1-tm*2, 1, 1, activeAction.targetActionObject.rotation());
					worldSpriteBatch.draw(waterFrame, v2.x-w1/2+tm+2, v2.y-h1/2+tm+2, w1/2-tm, h1/2-tm, w1-(tm-2)*2, h1-(tm-2)*2, 1, 1, activeAction.targetActionObject.rotation());
					worldSpriteBatch.draw(waterFrame, v2.x-w1/2+tm, v2.y-h1/2+tm, w1/2-tm, h1/2-tm, w1-tm*2, h1-tm*2, 1, 1, activeAction.targetActionObject.rotation()+180);
					worldSpriteBatch.draw(waterFrame, v2.x-w1/2+tm+2, v2.y-h1/2+tm+2, w1/2-tm, h1/2-tm, w1-(tm+2)*2, h1-(tm+2)*2, 1, 1, activeAction.targetActionObject.rotation()+180);
					worldSpriteBatch.setColor(1, 1, 1, town.gameWorld.alphaWorldObjects);
				}
//				else
//				{
//					int tm=20;
//					worldSpriteBatch.draw(waterFrame, activeAction.targetActionObject.pos_x()+tm, activeAction.targetActionObject.pos_y()+tm, activeAction.targetActionObject.width/2-tm, activeAction.targetActionObject.height/2-tm, activeAction.targetActionObject.width-tm*2, activeAction.targetActionObject.height-tm*2, 1, 1, activeAction.targetActionObject.rotation());
//					worldSpriteBatch.draw(waterFrame, activeAction.targetActionObject.pos_x()+tm+2, activeAction.targetActionObject.pos_y()+tm+2, activeAction.targetActionObject.width/2-tm, activeAction.targetActionObject.height/2-tm, activeAction.targetActionObject.width-(tm-2)*2, activeAction.targetActionObject.height-(tm-2)*2, 1, 1, activeAction.targetActionObject.rotation());
//					worldSpriteBatch.draw(waterFrame, activeAction.targetActionObject.pos_x()+tm, activeAction.targetActionObject.pos_y()+tm, activeAction.targetActionObject.width/2-tm, activeAction.targetActionObject.height/2-tm, activeAction.targetActionObject.width-tm*2, activeAction.targetActionObject.height-tm*2, 1, 1, activeAction.targetActionObject.rotation()+180);
//					worldSpriteBatch.draw(waterFrame, activeAction.targetActionObject.pos_x()+tm+2, activeAction.targetActionObject.pos_y()+tm+2, activeAction.targetActionObject.width/2-tm, activeAction.targetActionObject.height/2-tm, activeAction.targetActionObject.width-(tm+2)*2, activeAction.targetActionObject.height-(tm+2)*2, 1, 1, activeAction.targetActionObject.rotation()+180);
//					worldSpriteBatch.setColor(1, 1, 1, gameWorld.alphaWorldObjects);
//				}
				
				//Variante 0: Anim wird auf Grundlage der Rotationsverschiebung gezeichnet - funktioneirt im Moment nicht für kleine Einwohner
				//Vector2 targetv = new Vector2();
				//targetv.x=pos_x();
				//targetv.y=pos_y();
				//targetv = CHelper.moveVectorByRotation((int)rotation(), 15, 22, (int)targetv.x, (int)targetv.y);
				//worldSpriteBatch.draw(waterFrame, targetv.x, targetv.y, currentFrame.getRegionWidth()/2*getBodySizeByAge(), currentFrame.getRegionHeight()/2*getBodySizeByAge(), width, height, 1f, 1f, rotation());
										
				//Variante 1: Anim wird auf Grundlage des Heads gezeichnet
				//int tm=30;
				//worldSpriteBatch.draw(waterFrame, pos_x()+tm/getHeadSizeByAge(), pos_y()+tm/getHeadSizeByAge(), currentFrame.getRegionWidth()/2/getHeadSizeByAge()-tm/getHeadSizeByAge(), currentFrame.getRegionHeight()/2/getHeadSizeByAge()-tm/getHeadSizeByAge(), 80/getHeadSizeByAge(), 90/getHeadSizeByAge(), 1, 1, rotation()+rotmod);
	
				//Variante 3: Anim wird auf Grundlage des Heads mit fester rotation gezeichnet
				//int tm=30;
				//worldSpriteBatch.draw(waterFrame, pos_x()+tm, pos_y()+tm, currentFrame.getRegionWidth()/2-tm, currentFrame.getRegionHeight()/2-tm, 80, 90, 1, 1, rotation());
			}
		}	
		
		if(bIsDead)
		{
			worldSpriteBatch.setColor(1,1,1,1f);
			
			if(actionstring1.contains("show_coffin") || actionstring1.contains("show_grave"))
			{
				Texture texture_coffin = town.gameResourceConfig.textures.get("urbancemetery_coffin");
				
				if(actionstring1.contains("show_grave"))
				{
					String grtext = "company_urbancemetery_grave" + actionstring1.substring(actionstring1.length()-1);
					texture_coffin = town.gameResourceConfig.textures.get(grtext);
					if(texture_coffin==null)
						texture_coffin = town.gameResourceConfig.textures.get("company_urbancemetery_grave1");
				}
				
				CWorldObject wobj = this;
	    		int w=100;
	    		int h=240;
	    		
				if(town.fSizeFactor>0)
				{
					w/=town.fSizeFactor;
					h/=town.fSizeFactor;
				} 		
	    		
				Vector2 v2 = CHelper.moveVectorByRotationS2D(wobj.pos_x(), wobj.pos_y(), (int)town.getSizeValue(65), (int)town.getSizeValue(60), wobj.width/2, wobj.height/2, wobj.rotation());
				worldSpriteBatch.setColor(1, 1, 1, 1);
				town.gameWorld.worldSpriteBatch.draw(texture_coffin, v2.x-w/2, v2.y-h/2, w/2, h/2, w, h, 1, 1, wobj.rotation(), 0, 0, texture_coffin.getWidth(), texture_coffin.getHeight(), false, false);
			}
			else
			{
				//if(bIsDead)
		    	//	Gdx.app.debug("dead_1", "" + thehuman.getName());
				
				worldSpriteBatch.draw(town.gameWorld.gameResourceConfig.textures.get("icon_dead"), pos_x(), pos_y(), town.getSizeValue(120)*fsizefactor1, town.getSizeValue(120)*fsizefactor1);
			}
		}
	}
	
	public void drawHumanAction_UnderHead(TextureRegion currentFrame, float fsizefactor1)
	{
		
		//*******************
		//ACHTUNG
		//******************
		
		//wenn um human rotiert werden soll -> so wie bei read book machen
		//bei den anderen funktioniert es nur weil w und h des objekts gleich sind
		
		//-> moveVectorByHumanRotation verwenden!
		
		
		if(activeAction==null || activeAction.targetActionObject==null)
			return;
				
		
		//Billard Queue
		if(activeAction!=null && activeAction.bActionMode && !bIsDead)
		{
			if(activeAction.targetActionObject.theobject.editoraction.contains("_billard"))
			{
				if(actionfield1>0)
				{
					int bwidth=(int) town.getSizeValue(15);
					int bheight=(int) town.getSizeValue(220);
					int ypos=(int) town.getSizeValue(70);
					
					if(actionfield1==4)
						ypos-=town.getSizeValue(50);
					
					Texture bev = town.gameResourceConfig.textures.get("pub_billard_queue1");
					Vector2 v2 = CHelper.moveVectorByHumanRotation(this, bwidth, bheight, 50, ypos);
					town.gameWorld.worldSpriteBatch.draw(bev, v2.x, v2.y, bwidth/2, bheight/2, bwidth, bheight, 1, 1, rotation()+180, 0, 0, bev.getWidth(), bev.getHeight(), false, false);
				}
			}
		}
		
		
		//Pub Bier tragen
		if(activeAction!=null && activeAction.actionMode!=null && activeAction.actionMode==ActionMode.WORKPLACE && activeAction.bActionMode && !bIsDead)
		{
			if(activeAction.targetActionObject!=null && activeAction.targetActionObject.theobject.editoraction.contains("company_pub_workingplace_bar"))
			{
				if(actionfield1>0)
				{
					int bwidth=(int) town.getSizeValue(70);
					int bheight=(int) town.getSizeValue(50);
									
					
					TextureRegion bev =null;
					
					if(actionfield1==1)
						bev = town.gameResourceConfig.animations.get("pub_beverage").getKeyFrames()[0];
					if(actionfield1==2)
						bev = town.gameResourceConfig.animations.get("pub_beverage").getKeyFrames()[1];
					if(actionfield1==3)
						bev = town.gameResourceConfig.animations.get("pub_beverage").getKeyFrames()[2];
					if(actionfield1==4)
						bev = town.gameResourceConfig.animations.get("pub_beverage").getKeyFrames()[3];
					if(actionfield1==5)
						bev = town.gameResourceConfig.animations.get("pub_beverage").getKeyFrames()[4];
					if(actionfield1==6)
						bev = town.gameResourceConfig.animations.get("pub_beverage").getKeyFrames()[5];
					if(actionfield1==7)
						bev = town.gameResourceConfig.animations.get("pub_beverage").getKeyFrames()[6];
					
					Vector2 v2 = CHelper.moveVectorByHumanRotation(this, bwidth, bheight, (int)town.getSizeValue(50), (int)town.getSizeValue(30));
			    	//gameWorld.worldSpriteBatch.draw(bev, v2.x, v2.y, bwidth/2, bheight/2, bwidth, bheight, 1, 1, rotation()+90, 0, 0, bev.getWidth(), bev.getHeight(), false, false);
					town.gameWorld.worldSpriteBatch.draw(bev, v2.x, v2.y, bwidth/2, bheight/2, bwidth, bheight, 1, 1, rotation()+90);
			    	
			    	if(actionfield2>1)
			    	{
						v2 = CHelper.moveVectorByHumanRotation(this, bwidth, bheight, (int)town.getSizeValue(50+50), (int)town.getSizeValue(30));
						town.gameWorld.worldSpriteBatch.draw(bev, v2.x, v2.y, bwidth/2, bheight/2, bwidth, bheight, 1, 1, rotation()+90);
			    	}
			    	
			    	if(actionfield2>2)
			    	{
						v2 = CHelper.moveVectorByHumanRotation(this, bwidth, bheight, (int)town.getSizeValue(50), (int)town.getSizeValue(30-50));
						town.gameWorld.worldSpriteBatch.draw(bev, v2.x, v2.y, bwidth/2, bheight/2, bwidth, bheight, 1, 1, rotation()+90);
			    	}
			    	
			    	if(actionfield2>3)
			    	{
						v2 = CHelper.moveVectorByHumanRotation(this, bwidth, bheight, (int)town.getSizeValue(50+50), (int)town.getSizeValue(30-50));
						town.gameWorld.worldSpriteBatch.draw(bev, v2.x, v2.y, bwidth/2, bheight/2, bwidth, bheight, 1, 1, rotation()+90);
			    	}

			    	if(actionfield2>5)
			    	{
						v2 = CHelper.moveVectorByHumanRotation(this, bwidth, bheight, (int)town.getSizeValue(50), (int)town.getSizeValue(-5));
						town.gameWorld.worldSpriteBatch.draw(bev, v2.x, v2.y, bwidth/2, bheight/2, bwidth, bheight, 1, 1, rotation()+90);
			    	}
			    	
			    	if(actionfield2>6)
			    	{
						v2 = CHelper.moveVectorByHumanRotation(this, bwidth, bheight, (int)town.getSizeValue(50+50), (int)town.getSizeValue(-10));
						town.gameWorld.worldSpriteBatch.draw(bev, v2.x, v2.y, bwidth/2, bheight/2, bwidth, bheight, 1, 1, rotation()+90);
			    	}
				}
			}
		}
		
		
		//Action "Cook Dinner"
		if(activeAction!=null && activeAction.actionMode!=null && activeAction.actionMode==ActionMode.COOK_DINNER && activeAction.bActionMode && !bIsDead)
		{
			//Trage/Befülle Pfanne
			if((activeAction.actionState>=0 && activeAction.actionState<=5) || activeAction.actionState==13 || activeAction.actionState==14)
			{
		    	int panWidth=(int) town.getSizeValue(64);
		    	int panHeight=(int) town.getSizeValue(100);
		    	TextureRegion pan=null;
		    	if(objectFilling==0)
		    		pan = town.gameResourceConfig.animations.get("kitchen_pan").getKeyFrames()[0];
		    	if(objectFilling==1)
		    		pan = town.gameResourceConfig.animations.get("kitchen_pan").getKeyFrames()[1];
		    	if(objectFilling==2)
		    		pan = town.gameResourceConfig.animations.get("kitchen_pan").getKeyFrames()[2];
		    	if(objectFilling>=3)
		    		pan = town.gameResourceConfig.animations.get("kitchen_pan").getKeyFrames()[3];
		    	
		    	//todo: umstellen auf moveVectorByHumanRotation
		    	Vector2 v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), width/2, Math.round(panHeight/4f), width/2, height/2, rotation());
		    	town.gameWorld.worldSpriteBatch.draw(pan, v2.x-panWidth/2, v2.y-panHeight/2, panWidth/2, panHeight/2, panWidth, panHeight, 1, 1, rotation()+180);
			}
			
			//Trage Teller zum Dinner Table
			if(activeAction.actionState>=8 && activeAction.actionState<=10)
			{
				int plateSize=(int) town.getSizeValue(50);
				
				TextureRegion plate = town.gameResourceConfig.animations.get("kitchen_plate").getKeyFrames()[0];
				
				//todo: umstellen auf moveVectorByHumanRotation
		    	Vector2 v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), width/2, 30, width/2, height/2, rotation());
		    	//gameWorld.worldSpriteBatch.draw(plate, v2.x-plateSize/2, v2.y-plateSize/2, plateSize/2, plateSize/2, plateSize, plateSize, 1, 1, rotation(), 0, 0, plate.getWidth(), plate.getHeight(), false, false);
		    	town.gameWorld.worldSpriteBatch.draw(plate, v2.x-plateSize/2, v2.y-plateSize/2, plateSize/2, plateSize/2, plateSize, plateSize, 1, 1, rotation());
			}
		}
		
		
		//Read Book
		if(activeAction!=null && activeAction.actionMode!=null && activeAction.actionMode==ActionMode.READ_BOOK && activeAction.bActionMode && !bIsDead)
		{
			//livingroom_openbook
			if(activeAction.actionState==3 || activeAction.actionState==4 || activeAction.actionState==5) 
			{
				int bookSizeW=(int) town.getSizeValue(70);
				int bookSizeH=(int) town.getSizeValue(40);
				
				//todo: umstellen auf moveVectorByHumanRotation
				Texture plate = town.gameResourceConfig.textures.get("livingroom_openbook");
				Vector2 v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), Math.round((width/2)*getBodySizeByAge2(0)), Math.round(30), Math.round(width/2*getBodySizeByAge2(0)), Math.round(height/2*getBodySizeByAge2(1)), rotation());
				town.gameWorld.worldSpriteBatch.draw(plate, v2.x-bookSizeW/2, v2.y-bookSizeH/2, bookSizeW/2, bookSizeH/2, bookSizeW, bookSizeH, 1, 1, rotation(), 0, 0, plate.getWidth(), plate.getHeight(), false, false);
			}
		}
		
		
		//Eat Fridge
		if(activeAction!=null && activeAction.actionMode!=null && activeAction.actionMode==ActionMode.FRIDGE && activeAction.bActionMode && !bIsDead)
		{
			if(activeAction.actionState==2) 
			{
				if(actionfield2==0)
					actionfield2=1;
				
				int sizeW=Math.round((50)*actionfield2);
				int sizeH=Math.round((50)*actionfield2);
				
				int my=0;
				if(thehuman.getAge()<18)
				{
					sizeW-=((18-thehuman.getAge()));
					sizeH-=((18-thehuman.getAge()));
					my=(18-thehuman.getAge())*2;
				}
				
				sizeW=(int) town.getSizeValue(sizeW);
				sizeH=(int) town.getSizeValue(sizeH);
				my=(int) town.getSizeValue(my);
				
				//todo: umstellen auf moveVectorByHumanRotation
				if(actionfield1<1)
					actionfield1=1+rand.nextInt(8);
				String tname = "company_supermarket_food"+Math.round(actionfield1);
				Texture plate = town.gameResourceConfig.textures.get(tname);
				Vector2 v2 = CHelper.moveVectorByRotationS2D(pos_x()-rand.nextInt(5), pos_y()+rand.nextInt(5), Math.round((width/2)*getBodySizeByAge2(0)), Math.round(15+my), Math.round(width/2*getBodySizeByAge2(0)), Math.round(height/2*getBodySizeByAge2(1)), rotation());
				town.gameWorld.worldSpriteBatch.draw(plate, v2.x-sizeW/2, v2.y-sizeH/2, sizeW/2, sizeH/2, sizeW, sizeH, 1, 1, rotation(), 0, 0, plate.getWidth(), plate.getHeight(), false, false);
			}
		}		
		
		
		//Visit Doctor
		if(activeAction!=null && activeAction.actionMode!=null && activeAction.actionMode==ActionMode.GOTO_DOCTOR && activeAction.bActionMode && !bIsDead)
		{
			//Read Magazine, zeitschrift
			if((activeAction.actionState==6 || activeAction.actionState==7)  && actionfield1>0)  
			{
				int bookSizeW=(int) town.getSizeValue(70);
				int bookSizeH=(int) town.getSizeValue(40);
				
				//todo: umstellen auf moveVectorByHumanRotation
				Texture plate = null;
				plate = town.gameResourceConfig.textures.get("reading_magazine2");
				
				Vector2 v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), Math.round((width/2)*getBodySizeByAge2(0)), Math.round(30), Math.round(width/2*getBodySizeByAge2(0)), Math.round(height/2*getBodySizeByAge2(1)), rotation());
				town.gameWorld.worldSpriteBatch.draw(plate, v2.x-bookSizeW/2, v2.y-bookSizeH/2, bookSizeW/2, bookSizeH/2, bookSizeW, bookSizeH, 1, 1, rotation(), 0, 0, plate.getWidth(), plate.getHeight(), false, false);
			}
		}
		
		
		//Action "Wash Dishes"
		if(activeAction!=null && activeAction.actionMode!=null && activeAction.actionMode==ActionMode.WASH_DISHES && activeAction.bActionMode && !bIsDead)
		{
			//Trage Teller
			if(activeAction.actionState==3 || activeAction.actionState==5)
			{
				int plateSize=(int) town.getSizeValue(50);
				
				//Texture plate = town.gameResourceConfig.textures.get("kitchen_plate1");
				TextureRegion plate = town.gameResourceConfig.animations.get("kitchen_plate").getKeyFrames()[0];
				
				if(activeAction.actionState==3) //Schmutzteller
					plate = town.gameResourceConfig.animations.get("kitchen_plate").getKeyFrames()[7];
				
				//todo: umstellen auf moveVectorByHumanRotation
		    	Vector2 v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), width/2, 30, width/2, height/2, rotation());
		    	//gameWorld.worldSpriteBatch.draw(plate, v2.x-plateSize/2, v2.y-plateSize/2, plateSize/2, plateSize/2, plateSize, plateSize, 1, 1, rotation(), 0, 0, plate.getWidth(), plate.getHeight(), false, false);
		    	town.gameWorld.worldSpriteBatch.draw(plate, v2.x-plateSize/2, v2.y-plateSize/2, plateSize/2, plateSize/2, plateSize, plateSize, 1, 1, rotation());
			}
		}
	}
	
	public void drawAdditionalActionObjects()
	{
		//Beerdigung: Zeige Sarg am Rednerpult an
		//		if(theobject.editoraction.contains("company_urbancemetery_rostrum") && actionvar1==1)
		//		{
		//			Texture texture_coffin = town.gameResourceConfig.textures.get("urbancemetery_coffin");
		//			CWorldObject wobj = this;
		//			int w=100;
		//			int h=240;
		//			Vector2 v2 = CHelper.moveVectorByRotationS2D(wobj.pos_x(), wobj.pos_y(), wobj.width/2, -300, wobj.width/2, wobj.height/2, wobj.rotation()+180);
		//			gameWorld.worldSpriteBatch.setColor(1, 1, 1, 1);
		//			gameWorld.worldSpriteBatch.draw(texture_coffin, v2.x-w/2, v2.y-h/2, w/2, h/2, w, h, 1, 1, wobj.rotation(), 0, 0, texture_coffin.getWidth(), texture_coffin.getHeight(), false, false);
		//		}
				
		
		if(theobject.editoraction.contains("ground_water"))
		{
			if(town.gameConfigIni.waterAnimation==1)
			{
				int day=town.gameWorld.worldTime.day;
				
				if(day!=11&&day!=12&&day!=1&&day!=2)
				{
					town.gameWorld.worldSpriteBatch.setShader(town.gameWorld.gameResourceConfig.snowShader);
					animationtimer+=CHelper.getDeltaSeconds(town);
					
					if(animationtimer>1f)
						animationtimer=0;
					
		    		TextureRegion currentFrame = theobject.textureRegion[0];
		    		int mov=2;
		    		
		    		int px=pos_x()+rand.nextInt(mov)-rand.nextInt(mov);
		    		int py=pos_y()+rand.nextInt(mov)-rand.nextInt(mov);
		    		
		    		int w=width+rand.nextInt(mov)-rand.nextInt(mov);
		    		int h=height+rand.nextInt(mov)-rand.nextInt(mov);
		    		
		    		float sx=1;
		    		float sy=1;
		    		
		    		town.gameWorld.worldSpriteBatch.setColor(1, 1, 1, 0.5f);
		    		town.gameWorld.worldSpriteBatch.draw(currentFrame, px, py, w/2, h/2, w, h, sx, sy, rotation());
		    		
		    		TextureRegion waterFrame=null;
		    		waterFrame = town.gameWorld.gameResourceConfig.animations.get("anim_shower2").getKeyFrame(town.gameWorld.getStateTime(), true);
		    		town.gameWorld.worldSpriteBatch.setColor(0.0f, 0.0f, 0.8f, 0.1f);
		    		int tm=20;
		    		town.gameWorld.worldSpriteBatch.draw(waterFrame, px, py, w/2, h/2, w, h, sx, sy, rotation());
		    		//gameWorld.worldSpriteBatch.draw(waterFrame, px+tm, py+tm, w/2-tm, h/2-tm, w-tm*2, h-tm*2, sx, sy, rotation());
		    		//gameWorld.worldSpriteBatch.draw(waterFrame, px, py, w/2, h/2, w, h, sx, sy, rotation()+180);
		    		//gameWorld.worldSpriteBatch.draw(waterFrame, px+tm, py+tm, w/2-tm, h/2-tm, w-tm*2, h-tm*2, sx, sy, rotation()+180);
		    		town.gameWorld.worldSpriteBatch.setShader(null);
				}
				else
				{
					TextureRegion currentFrame=null;
					
					if(day==11||day==2)
					{
						currentFrame = theobject.textureRegion2[0];
						town.gameWorld.worldSpriteBatch.setColor(1, 1, 1, 0.735f);
					}
					
					if(day==12||day==1)
					{
						currentFrame = theobject.textureRegion2[0];
						town.gameWorld.worldSpriteBatch.setColor(1, 1, 1, 0.8f);
					}
					
					town.gameWorld.worldSpriteBatch.draw(currentFrame, pos_x(), pos_y(), width/2, height/2, width, height, 1, 1, rotation());
				}
			}
		}
		
		
		//ANPASSEN: IsDrawAdditionalObject()
		
    	if(theobject.editoraction.contains("company_gasstation_gaspump"))
    	{
    		if(actionfield1==1)
    		{
        		Texture tankbutt1 = theobject.textureImage2;
        		town.gameWorld.worldSpriteBatch.draw(tankbutt1, pos_x(), pos_y(), width/2, height/2, width, height, 1, 1, rotation(), 0, 0, tankbutt1.getWidth(), tankbutt1.getHeight(), false, false);
    		}
    		//}
    		//else
    		//{
	    	//	gameWorld.worldSpriteBatch.draw(currentFrame, pos_x(), pos_y(), width/2, height/2, width, height, sx, sy, rotation());
    		//}
    	}
		
		
		
		//Coffee Machine
    	if(theobject.editoraction.contains("_coffeemachine"))
    	{
			int animW=(int) town.getSizeValue(110);
			int animH=(int) town.getSizeValue(110);
			
			TextureRegion anim = theobject.textureRegion2[objectFilling2];
			Vector2 v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), 150, 65, width/2, height/2, rotation());
			
			town.gameWorld.worldSpriteBatch.setColor(1, 1, 1, 1f);
			town.gameWorld.worldSpriteBatch.draw(anim, v2.x-animW/2, v2.y-animH/2, animW/2, animH/2, animW, animH, 1, 1, rotation());
    	}		
    	
    	
		//Foosbal
		if(theobject.editoraction.contains("_foosball"))
		{
			if(isOccupiedBy!=null && isOccupiedBy2!=null && isOccupiedBy.activeAction!=null && isOccupiedBy.activeAction.actionState==2 &&
					isOccupiedBy2.activeAction!=null && isOccupiedBy2.activeAction.actionState==2)
			{
				Texture poly_whiterect = town.gameResourceConfig.textures.get("poly_whiterect2");
				
				float textureW=10;
				float textureH=10;
				
				actionvar1+=rand.nextInt(10)-rand.nextInt(10);
				actionvar2+=rand.nextInt(10)-rand.nextInt(10);
				
				int gr1=40;
						
				if(actionvar1>width-gr1)
					actionvar1=width/2;
				if(actionvar1<gr1)
					actionvar1=width/2;
				
				if(actionvar2>height-gr1)
					actionvar2=height/2;
				if(actionvar2<gr1)
					actionvar2=height/2;
												
				Vector2 v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), (int)actionvar1, (int)actionvar2, width/2, height/2, rotation());
				town.gameWorld.worldSpriteBatch.draw(poly_whiterect, v2.x-textureW/2, v2.y-textureH/2, textureW/2, textureH/2, textureW, textureH, 1, 1, rotation(), 0, 0, poly_whiterect.getWidth(), poly_whiterect.getHeight(), false, false);
								
			}
		}
		
		
		//Toilet open
		if(theobject.editoraction.contains("bathroom_toilet") || theobject.editoraction.contains("toiletroom_toiletstall"))
		{
			if(doObjectAction)
			{
				Texture toilet_open = town.gameResourceConfig.textures.get("toilet_open");
				int textureW=0;
				int textureH=0;
				int px=0;
				int py=0;
				
				if(theobject.editoraction.contains("toiletroom_toiletstall"))
				{
					px=(int) town.getSizeValue(82);
					py=(int) town.getSizeValue(200);
					textureW=(int) town.getSizeValue(60);
					textureH=(int) town.getSizeValue(115);
				}
				
				if(theobject.editoraction.contains("bathroom_toilet"))
				{
					px=(int) town.getSizeValue(43);
					py=(int) town.getSizeValue(74);
					textureW=(int) town.getSizeValue(60);
					textureH=(int) town.getSizeValue(115);
				}
				
				Vector2 v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), px, py, width/2, height/2, rotation());
				town.gameWorld.worldSpriteBatch.draw(toilet_open, v2.x-textureW/2, v2.y-textureH/2, textureW/2, textureH/2, textureW, textureH, 1, 1, rotation(), 0, 0, toilet_open.getWidth(), toilet_open.getHeight(), false, false);
			}	
	        return;
		}
				
		//Doctor's Office
		if(theobject.editoraction.contains("company_doctorsoffice_treatment_chair"))
		{
			if(actionfield1==1)
			{
				town.gameWorld.worldSpriteBatch.end();
				
				//shapeRenderer.setProjectionMatrix(gameCamera.combined);
				//if(!Gdx.gl.glIsEnabled(GL30.GL_BLEND))
				{
					Gdx.gl.glEnable(GL30.GL_BLEND);
					Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
				}
				town.gameWorld.shapeRenderer1.begin();
				
				town.gameWorld.shapeRenderer1.set(ShapeType.Filled);
				town.gameWorld.shapeRenderer1.setColor(1, 0.7f, 0, 0.5f);
				Vector2 v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), (int) town.getSizeValue(213), (int) town.getSizeValue(140), width/2, height/2, rotation());
				town.gameWorld.shapeRenderer1.circle((int)v2.x, (int)v2.y, 28);
				
				town.gameWorld.shapeRenderer1.end();
				town.gameWorld.worldSpriteBatch.begin();
			}
		}
				
		//Research Table Animation
		if(theobject.editoraction.contains("company_college_workingplace_researchlab") && !town.gameWorld.worldPause)
		{
			if(actionanim1==1)
			{
				int animW=(int) town.getSizeValue(80);
				int animH=(int) town.getSizeValue(100);
				
				float delta=town.gameWorld.stateTime; //CHelper.getDeltaSeconds(gameWorld); //+gameWorld.stateTime+rand.nextFloat()/10
				TextureRegion anim = theobject.objectAnimation2.getKeyFrame(delta, true); //town.gameResourceConfig.animations.get("anim_researchlab1").getKeyFrame(delta, true);
				Vector2 v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), (int)town.getSizeValue(210), (int)town.getSizeValue(60), width/2, height/2, rotation());
				
				town.gameWorld.worldSpriteBatch.setColor(1,1,1,0.6f);
				town.gameWorld.worldSpriteBatch.draw(anim, v2.x-animW/2, v2.y-animH/2, animW/2, animH/2, animW, animH, 1, 1, rotation());
				
			}
		}
				
		//Lat Pulldown
		if(theobject.editoraction.contains("company_fitnessstudio_latpull"))
		//if(activeAction!=null && activeAction.actionMode==ActionMode.FITNESS_STUDIO && activeAction.bActionMode && !bIsDead)
		{
			//if(activeAction.targetActionObject!=null && activeAction.targetActionObject.theobject.editoraction.contains("company_fitnessstudio_latpull"))
			{
				town.gameWorld.worldSpriteBatch.setColor(1,1,1,1f);
				Texture lat2 = theobject.textureImage2; //town.gameResourceConfig.textures.get("fitnessstudio_lat2");
				CWorldObject wobj = this;
	    		int w_lat2=(int) town.getSizeValue(150);
	    		int h_lat2=(int) town.getSizeValue(10);
	    		
	    		float zx=1;
	    		int movy=(int) town.getSizeValue(100);
	    		if(wobj.actionfield1==1)
	    		{
	    			zx=0.8f;
	    			movy=(int) town.getSizeValue(93);
	    		}
	    		
				Vector2 v2 = CHelper.moveVectorByRotationS2D(wobj.pos_x(), wobj.pos_y(), 65, movy, wobj.width/2, wobj.height/2, wobj.rotation());
				town.gameWorld.worldSpriteBatch.draw(lat2, v2.x-w_lat2/2, v2.y-h_lat2/2, w_lat2/2, h_lat2/2, w_lat2, h_lat2, zx, zx, wobj.rotation(), 0, 0, lat2.getWidth(), lat2.getHeight(), false, false);
			}
		}
		
		//Bar
		if(theobject.editoraction.contains("company_pub_workingplace_bar") || theobject.editoraction.contains("company_pub_table"))
		{
			int bwidth=(int) town.getSizeValue(70);
			int bheight=(int) town.getSizeValue(50);
			TextureRegion bev =null;
			
			for(int i=1;i<8;i++)
			{
				if(theobject.editoraction.contains("company_pub_table") && i>4)
					break;
				
				float thevar=0;
				if(i==1)
					thevar=actionvar1;
				if(i==2)
					thevar=actionvar2;
				if(i==3)
					thevar=actionvar3;
				if(i==4)
					thevar=actionvar4;
				if(i==5)
					thevar=actionvar5;
				if(i==6)
					thevar=actionvar6;
				if(i==7)
					thevar=actionvar7;
				
				if(thevar>0)
				{
					int seatType=1;
					if(theobject.editoraction.contains("company_pub_table"))
						seatType=2;
					
					Vector3 v3 = CAction.getBarSeatPosition((int)i, 2, seatType, town);
					
					float sx=1;
					float sy=1;
					
					if(thevar>=10)
					{
						sx=1.2f;
						sy=1.2f;
						thevar=Math.round(thevar/10);
					}

					if(thevar==1)
						bev = town.gameResourceConfig.animations.get("pub_beverage").getKeyFrames()[0];
					if(thevar==2)
						bev = town.gameResourceConfig.animations.get("pub_beverage").getKeyFrames()[1];
					if(thevar==3)
						bev = town.gameResourceConfig.animations.get("pub_beverage").getKeyFrames()[2];
					if(thevar==4)
						bev = town.gameResourceConfig.animations.get("pub_beverage").getKeyFrames()[3];
					if(thevar==5)
						bev = town.gameResourceConfig.animations.get("pub_beverage").getKeyFrames()[4];
					if(thevar==6)
						bev = town.gameResourceConfig.animations.get("pub_beverage").getKeyFrames()[5];
					if(thevar==7)
						bev = town.gameResourceConfig.animations.get("pub_beverage").getKeyFrames()[6];
					
					Vector2 v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), (int)v3.x, (int)v3.y, width/2, height/2, rotation());
					//gameWorld.worldSpriteBatch.draw(bev, v2.x, v2.y, bwidth/2, bheight/2, bwidth, bheight, sx, sy, rotation()+v3.z+180, 0, 0, bev.getWidth(), bev.getHeight(), false, false);
					town.gameWorld.worldSpriteBatch.draw(bev, v2.x, v2.y, bwidth/2, bheight/2, bwidth, bheight, sx, sy, rotation()+v3.z+180);
				}
			}
		}
				
		//Tv ist eingeschaltet
		if(theobject.editoraction.contains("livingroom_tv") && tempcount>0 && !town.gameWorld.worldPause)
		{
			if(isActiveByEnergyConsumption() && theobject.objectAnimation2!=null && theobject.objectAnimation2.getKeyFrames().length>0)
			{
				int animW=theobject.ATTR_T2W; //110;
				int animH=theobject.ATTR_T2H; //5;
				
				TextureRegion animtv = theobject.objectAnimation2.getKeyFrame(CHelper.getDeltaSeconds(town)+town.gameWorld.stateTime+rand.nextFloat()/10, true);
				Vector2 v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), theobject.ATTR_T2X, theobject.ATTR_T2Y, width/2, height/2, rotation());
				
				town.gameWorld.worldSpriteBatch.setColor(1,1,1,0.6f);
				town.gameWorld.worldSpriteBatch.draw(animtv, v2.x-animW/2, v2.y-animH/2, animW/2, animH/2, animW, animH, 1, 1, rotation());
			}
		}
		
		//School Desk wird benutzt
		if(theobject.editoraction.contains("company_school_workingplace_studentsdesk") || theobject.editoraction.contains("company_school_workingplace_teachersdesk"))
		{
			int textureW=(int) town.getSizeValue(35);
			int textureH=(int) town.getSizeValue(50);
			
			int movx=0;
			int movy=0;
			
			if(actionvar1==1)
			{
				movx=(int) town.getSizeValue(55);
				movy=(int) town.getSizeValue(110);
				
				if(theobject.editoraction.contains("company_school_workingplace_teachersdesk"))
				{
					movx = (int) town.getSizeValue(290);
					movy = (int) town.getSizeValue(180);
				}
				
				Texture paperwork = town.gameResourceConfig.textures.get("desk_paperwork");
				Vector2 v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), movx, movy, width/2, height/2, rotation());
				
				town.gameWorld.worldSpriteBatch.setColor(1,1,1,1f);
				town.gameWorld.worldSpriteBatch.draw(paperwork, v2.x-textureW/2, v2.y-textureH/2, textureW/2, textureH/2, textureW, textureH, 1, 1, rotation(), 0, 0, paperwork.getWidth(), paperwork.getHeight(), false, false);
				
				//Info Entfernung zum teachers desk
				//				if(theobject.editoraction.contains("company_school_workingplace_studentsdesk"))
				//				{
				//					CWorldObject tdesk = getTeachersDeskForStudentsDesk();
				//					if(tdesk!=null)
				//					{
				//						int distance = CHelper.getEuclidianDistance(this, tdesk);
				//						Gdx.app.debug("", "distance: " + distance);
				//					}
				//				}
			}
			
			if(actionvar2==1)
			{
				movx=(int) town.getSizeValue(155);
				movy=(int) town.getSizeValue(110);
				
				Texture paperwork = town.gameResourceConfig.textures.get("desk_paperwork");
				Vector2 v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), movx, movy, width/2, height/2, rotation());
				
				town.gameWorld.worldSpriteBatch.setColor(1,1,1,1f);
				town.gameWorld.worldSpriteBatch.draw(paperwork, v2.x-textureW/2, v2.y-textureH/2, textureW/2, textureH/2, textureW, textureH, 1, 1, rotation(), 0, 0, paperwork.getWidth(), paperwork.getHeight(), false, false);
			}
		}
		
		//Blackboard wird beschrieben
		if(theobject.editoraction.contains("company_school_workingplace_teachersdesk"))
		{
			if(tempcount>0)
			{
				int textureW=(int) town.getSizeValue(200);
				int textureH=(int) town.getSizeValue(40);
				
				int movx=(int) town.getSizeValue(120);
				int movy=(int) town.getSizeValue(38);
				
				TextureRegion blackboard = theobject.textureRegion2[0]; //town.gameResourceConfig.animations.get("school_blackboard").getKeyFrames()[0];
				
				if(tempcount==1)
					blackboard = theobject.textureRegion2[0];
				if(tempcount==2)
					blackboard = theobject.textureRegion2[1];
				if(tempcount==3)
					blackboard = theobject.textureRegion2[2];
				if(tempcount==4)
					blackboard = theobject.textureRegion2[3];
				if(tempcount==5)
					blackboard = theobject.textureRegion2[4];
				if(tempcount==6)
					blackboard = theobject.textureRegion2[5];
				if(tempcount==7)
					blackboard = theobject.textureRegion2[6];
				
				Vector2 v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), movx, movy, width/2, height/2, rotation());
				town.gameWorld.worldSpriteBatch.setColor(1,1,1,1f);
				//gameWorld.worldSpriteBatch.draw(blackboard, v2.x-textureW/2, v2.y-textureH/2, textureW/2, textureH/2, textureW, textureH, 1, 1, rotation(), 0, 0, blackboard.getWidth(), blackboard.getHeight(), false, false);
				town.gameWorld.worldSpriteBatch.draw(blackboard, v2.x-textureW/2, v2.y-textureH/2, textureW/2, textureH/2, textureW, textureH, 1, 1, rotation());
			}
		}
		
		
		//Projector Screen
		if(theobject.editoraction.contains("company_college_workingplace_profslectern"))
		{
			if(tempcount>0)
			{
				int textureW=(int) town.getSizeValue(335);
				int textureH=(int) town.getSizeValue(12);
				
				int movx=(int) town.getSizeValue(195);
				int movy=(int) town.getSizeValue(41);
				
				TextureRegion reg1 = theobject.textureRegion2[0];//town.gameResourceConfig.animations.get("projector_screen").getKeyFrames()[0];
				
				if(tempcount==1)
					reg1 = theobject.textureRegion2[1];
				if(tempcount==2)
					reg1 = theobject.textureRegion2[2];
				if(tempcount==3)
					reg1 = theobject.textureRegion2[3];
				if(tempcount==4)
					reg1 = theobject.textureRegion2[4];
				if(tempcount==5)
					reg1 = theobject.textureRegion2[5];
				if(tempcount==6)
					reg1 = theobject.textureRegion2[6];
				if(tempcount==7)
					reg1 = theobject.textureRegion2[7];
				
				Vector2 v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), movx, movy, width/2, height/2, rotation());
				town.gameWorld.worldSpriteBatch.setColor(1,1,1,1f);
				//gameWorld.worldSpriteBatch.draw(reg1, v2.x-textureW/2, v2.y-textureH/2, textureW/2, textureH/2, textureW, textureH, 1, 1, rotation(), 0, 0, blackboard.getWidth(), blackboard.getHeight(), false, false);
				town.gameWorld.worldSpriteBatch.draw(reg1, v2.x-textureW/2, v2.y-textureH/2, textureW/2, textureH/2, textureW, textureH, 1, 1, rotation());
			}
		}
		
		
		if(theobject.editoraction.contains("company_recyclingcenter_recyclingmachine3"))
		{
			float delta1=7f;
			
			if(recyclingmachine3Timer_Trigger==0)
				recyclingmachine3Timer+=CHelper.getDeltaSeconds(town)*delta1;
			if(recyclingmachine3Timer_Trigger==1)
				recyclingmachine3Timer-=CHelper.getDeltaSeconds(town)*delta1;
			
			if(recyclingmachine3Timer>20)
				recyclingmachine3Timer_Trigger=1;
			if(recyclingmachine3Timer<1)
				recyclingmachine3Timer_Trigger=0;
			
			int textureW=(int) town.getSizeValue(14);
			int textureH=(int) town.getSizeValue(14);
			
			if(recyclingmachine3Timer>25)
				recyclingmachine3Timer=25;
			
			if(recyclingmachine3Timer<0)
				recyclingmachine3Timer=0;
			
			
			textureW*=recyclingmachine3Timer;
			textureH*=recyclingmachine3Timer;
			
			
			int movx=0;
			int movy=0;
			
			if(isActiveByEnergyConsumption())
			{
				movx=width/2;
				movy=height/2;
				
				Texture recyclingmachine3_anim = town.gameResourceConfig.textures.get("recyclingmachine3_anim");
				Vector2 v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), movx, movy, width/2, height/2, rotation());
				
				town.gameWorld.worldSpriteBatch.setColor(1,1,1,1f);
				town.gameWorld.worldSpriteBatch.draw(recyclingmachine3_anim, v2.x-textureW/2, v2.y-textureH/2, textureW/2, textureH/2, textureW, textureH, 1, 1, rotation(), 0, 0, recyclingmachine3_anim.getWidth(), recyclingmachine3_anim.getHeight(), false, false);
			}
		}
		
		
		//Friedhof Rednerpult wird benutzt
		if(theobject.editoraction.contains("company_urbancemetery_rostrum"))
		{
			int textureW=(int) town.getSizeValue(35);
			int textureH=(int) town.getSizeValue(50);
			
			int movx=0;
			int movy=0;
			
			if(actionfield1==1)
			{
				movx=(int) town.getSizeValue(65);
				movy=(int) town.getSizeValue(20);
				
				Texture paperwork = town.gameResourceConfig.textures.get("desk_paperwork");
				Vector2 v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), movx, movy, width/2, height/2, rotation());
				
				town.gameWorld.worldSpriteBatch.setColor(1,1,1,1f);
				town.gameWorld.worldSpriteBatch.draw(paperwork, v2.x-textureW/2, v2.y-textureH/2, textureW/2, textureH/2, textureW, textureH, 1, 1, rotation(), 0, 0, paperwork.getWidth(), paperwork.getHeight(), false, false);
				
			}
		}
		
		
		//College Desk & Lectern wird benutzt
		if(theobject.editoraction.contains("company_college_workingplace_studentsdesk"))
		{
			int textureW=(int) town.getSizeValue(35);
			int textureH=(int) town.getSizeValue(50);
			
			int movx=0;
			int movy=0;
			
			if(actionvar1==1)
			{
				movx=(int) town.getSizeValue(55);
				movy=(int)town.getSizeValue(110);
				
				//if(theobject.editoraction.contains("company_college_workingplace_profslectern"))
				//{
				//	movx = 290;
				//	movy = 180;
				//}
				
				Texture paperwork = town.gameResourceConfig.textures.get("desk_paperwork");
				Vector2 v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), movx, movy, width/2, height/2, rotation());
				
				town.gameWorld.worldSpriteBatch.setColor(1,1,1,1f);
				town.gameWorld.worldSpriteBatch.draw(paperwork, v2.x-textureW/2, v2.y-textureH/2, textureW/2, textureH/2, textureW, textureH, 1, 1, rotation(), 0, 0, paperwork.getWidth(), paperwork.getHeight(), false, false);
								
				//Info Entfernung zum teachers desk
				//				if(theobject.editoraction.contains("company_school_workingplace_studentsdesk"))
				//				{
				//					CWorldObject tdesk = getTeachersDeskForStudentsDesk();
				//					if(tdesk!=null)
				//					{
				//						int distance = CHelper.getEuclidianDistance(this, tdesk);
				//						Gdx.app.debug("", "distance: " + distance);
				//					}
				//				}
			}
			
			if(actionvar2==1)
			{
				movx=(int) town.getSizeValue(155);
				movy=(int) town.getSizeValue(110);
				
				Texture paperwork = town.gameResourceConfig.textures.get("desk_paperwork");
				Vector2 v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), movx, movy, width/2, height/2, rotation());
				
				town.gameWorld.worldSpriteBatch.setColor(1,1,1,1f);
				town.gameWorld.worldSpriteBatch.draw(paperwork, v2.x-textureW/2, v2.y-textureH/2, textureW/2, textureH/2, textureW, textureH, 1, 1, rotation(), 0, 0, paperwork.getWidth(), paperwork.getHeight(), false, false);
			}
		}
		
		//Alarm Clock
		if(theobject.editoraction.contains("bedroom_nightstand") && tempcount>0 && !town.gameWorld.worldPause)
		{
			tempcount-=CHelper.getDeltaSeconds(town);
			if(tempcount<0)
				tempcount=0;
			
			Texture anim_nightstand = theobject.textureImage2; //town.gameResourceConfig.textures.get("anim_nightstand");
			//int textureSize=(int)(20+10*rendertime);
			int textureSize=(int)town.getSizeValue((renderTime2*1.7f));
			if(renderTime2>40f)
				renderTime2=0;
			
			town.gameWorld.worldSpriteBatch.setColor(1,1,1,0.8f);			
			Vector2 v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), 35, 22, width/2, height/2, rotation());
			town.gameWorld.worldSpriteBatch.draw(anim_nightstand, v2.x-textureSize/2, v2.y-textureSize/2, textureSize/2, textureSize/2, textureSize, textureSize, 1, 1, rotation(), 0, 0, anim_nightstand.getWidth(), anim_nightstand.getHeight(), false, false);
			//gameWorld.worldSpriteBatch.draw(anim_nightstand, pos_x(), pos_y(), width/2, height/2, width, height, 1, 1, rotation());
			town.gameWorld.worldSpriteBatch.setColor(1,1,1,1f);
		}
		
		//Action "Wash Dishes", show dishes on sink
		if(theobject.editoraction.contains("kitchen_sink") && tempcount>0)
		{
			int plateSize=(int) town.getSizeValue(50);
			
			//Texture plate = town.gameResourceConfig.textures.get("kitchen_plate1");
			TextureRegion plate = town.gameResourceConfig.animations.get("kitchen_plate").getKeyFrames()[0];
			
			if(tempcount>0)
			{
				Vector2 v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), width/2-(int)town.getSizeValue(50), (int)town.getSizeValue(50), width/2, height/2, rotation());
				//gameWorld.worldSpriteBatch.draw(plate, v2.x-plateSize/2, v2.y-plateSize/2, plateSize/2, plateSize/2, plateSize, plateSize, 1, 1, rotation(), 0, 0, plate.getWidth(), plate.getHeight(), false, false);
				town.gameWorld.worldSpriteBatch.draw(plate, v2.x-plateSize/2, v2.y-plateSize/2, plateSize/2, plateSize/2, plateSize, plateSize, 1, 1, rotation());
			}
			
			if(tempcount>1)
			{
				Vector2 v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), width/2+(int)town.getSizeValue(40), (int)town.getSizeValue(40), width/2, height/2, rotation());
		    	//gameWorld.worldSpriteBatch.draw(plate, v2.x-plateSize/2, v2.y-plateSize/2, plateSize/2, plateSize/2, plateSize, plateSize, 1, 1, rotation(), 0, 0, plate.getWidth(), plate.getHeight(), false, false);
				town.gameWorld.worldSpriteBatch.draw(plate, v2.x-plateSize/2, v2.y-plateSize/2, plateSize/2, plateSize/2, plateSize, plateSize, 1, 1, rotation());
			}
			
			if(tempcount>2)
			{
				Vector2 v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), width/2+(int)town.getSizeValue(44), (int)town.getSizeValue(44), width/2, height/2, rotation());
				town.gameWorld.worldSpriteBatch.draw(plate, v2.x-plateSize/2, v2.y-plateSize/2, plateSize/2, plateSize/2, plateSize, plateSize, 1, 1, rotation());
			}
			
			if(tempcount>3)
			{
				Vector2 v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), width/2+(int)town.getSizeValue(36), (int)town.getSizeValue(44), width/2, height/2, rotation());
				town.gameWorld.worldSpriteBatch.draw(plate, v2.x-plateSize/2, v2.y-plateSize/2, plateSize/2, plateSize/2, plateSize, plateSize, 1, 1, rotation());
			}
			
			if(tempcount>4)
			{
				Vector2 v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), width/2+(int)town.getSizeValue(38), (int)town.getSizeValue(36), width/2, height/2, rotation());
				town.gameWorld.worldSpriteBatch.draw(plate, v2.x-plateSize/2, v2.y-plateSize/2, plateSize/2, plateSize/2, plateSize, plateSize, 1, 1, rotation());
			}
			
			if(tempcount>5)
			{
				Vector2 v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), width/2+(int)town.getSizeValue(40), (int)town.getSizeValue(46), width/2, height/2, rotation());
				town.gameWorld.worldSpriteBatch.draw(plate, v2.x-plateSize/2, v2.y-plateSize/2, plateSize/2, plateSize/2, plateSize, plateSize, 1, 1, rotation());
			}
		}
		
		//Action "Cook Dinner" -> Draw Pan on stove
		if(theobject.editoraction.contains("kitchen_stove") && isOccupiedBy!=null)
		{
			if(isOccupiedBy.activeAction!=null && isOccupiedBy.activeAction.actionMode==ActionMode.COOK_DINNER && isOccupiedBy.activeAction.bActionMode && !isOccupiedBy.bIsDead)
			{
				if(isOccupiedBy.activeAction.actionState>=6 && isOccupiedBy.activeAction.actionState<=12)
				{
			    	int panWidth=(int) town.getSizeValue(64);
			    	int panHeight=(int) town.getSizeValue(100);
			    	
			    	TextureRegion pan=null;
			    	pan = town.gameResourceConfig.animations.get("kitchen_pan").getKeyFrames()[3];
			    	Vector2 v2 = CHelper.moveVectorByRotationS2D(x_temp, y_temp, isOccupiedBy.width/2, 0, isOccupiedBy.width/2, isOccupiedBy.height/2, rotation_temp);
			    	//Vector2 v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), width/2+panWidth/2, height/2+panHeight/2, isOccupiedBy.width/2, isOccupiedBy.height/2, rotation()+180);
			    	town.gameWorld.worldSpriteBatch.draw(pan, v2.x-panWidth/2, v2.y-panHeight/2, panWidth/2, panHeight/2, panWidth, panHeight, 1, 1, rotation_temp+180);
				}
			}
		}
				
		//Teller auf Dinner Table
		if(theobject.editoraction.contains("diningroom_diningtable"))
		{
			if(tempcount>0)
			{
				int platesize = (int) town.getSizeValue(40);
				int ownercount = getOwnerCount();
				TextureRegion plate=null;
				
				int seatcount=4;
				if(theobject.editoraction.contains("_count6"))
					seatcount=6;
								
				if(theobject.editoraction.contains("_count8"))
					seatcount=8;
				
				//		1	3
				//	5		  	6
				//		2	4
				
				int platefilling=0;
				Vector2 v2 = null;
				
				int movx=0;
				int movy=0;
				if(seatcount==4)
				{
					movx=(int) town.getSizeValue(-85);
					movy=0;
				}
				
				int movx8=0;
				if(seatcount==8)
				{
					movx8=(int) town.getSizeValue(10);
					movx=(int) town.getSizeValue(-75);
					movy=0;
				}				
				
				//if(!theobject.editoraction.contains("count8"))
				{
					//oben links
					if(owner!=null)
					{
						plate=getPlateFillingTexture((int)actionvar1);
						v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), (int)town.getSizeValue(45)+platesize/2+(int)town.getSizeValue(100)+movx, (int)town.getSizeValue(75+60)+platesize/2+(int)town.getSizeValue(50)+movy, width/2, height/2, rotation());
						town.gameWorld.worldSpriteBatch.draw(plate, v2.x-platesize/2, v2.y-platesize/2, platesize/2, platesize/2, platesize, platesize, 1, 1, rotation());
					}
					
					//unten links
					if(owner2!=null)
					{
						plate=getPlateFillingTexture((int)actionvar2);
						v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), (int)town.getSizeValue(45)+platesize/2+(int)town.getSizeValue(100)+movx, (int)town.getSizeValue(75)+platesize/2+(int)town.getSizeValue(22)+movy, width/2, height/2, rotation());
						town.gameWorld.worldSpriteBatch.draw(plate, v2.x-platesize/2, v2.y-platesize/2, platesize/2, platesize/2, platesize, platesize, 1, 1, rotation());
					}
					
					//oben rechts
					if(owner3!=null)
					{
						plate=getPlateFillingTexture((int)actionvar3);
						v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), (int)town.getSizeValue(155)+platesize/2+(int)town.getSizeValue(100)+movx+movx8, (int)town.getSizeValue(75+60)+platesize/2+(int)town.getSizeValue(50)+movy, width/2, height/2, rotation());
						town.gameWorld.worldSpriteBatch.draw(plate, v2.x-platesize/2, v2.y-platesize/2, platesize/2, platesize/2, platesize, platesize, 1, 1, rotation());
					}
					
					//unten rechts
					if(owner4!=null)
					{
						plate=getPlateFillingTexture((int)actionvar4);
						v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), (int)town.getSizeValue(155)+platesize/2+(int)town.getSizeValue(100)+movx+movx8, (int)town.getSizeValue(75)+platesize/2+(int)town.getSizeValue(22)+movy, width/2, height/2, rotation());
						town.gameWorld.worldSpriteBatch.draw(plate, v2.x-platesize/2, v2.y-platesize/2, platesize/2, platesize/2, platesize, platesize, 1, 1, rotation());
					}
				}
				
				if(theobject.editoraction.contains("count6"))
				{
					//links
					if(owner5!=null)
					{
						plate=getPlateFillingTexture((int)actionvar5);
						v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), (int)town.getSizeValue(45)+platesize/2+(int)town.getSizeValue(50), (int)town.getSizeValue(75)+platesize/2+(int)town.getSizeValue(65), width/2, height/2, rotation());
						town.gameWorld.worldSpriteBatch.draw(plate, v2.x-platesize/2, v2.y-platesize/2, platesize/2, platesize/2, platesize, platesize, 1, 1, rotation());
					}
					
					//rechts
					if(owner6!=null)
					{
						plate=getPlateFillingTexture((int)actionvar6);
						v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), (int)town.getSizeValue(155)+platesize/2+(int)town.getSizeValue(100+50), (int)town.getSizeValue(75)+platesize/2+(int)town.getSizeValue(65), width/2, height/2, rotation());
						town.gameWorld.worldSpriteBatch.draw(plate, v2.x-platesize/2, v2.y-platesize/2, platesize/2, platesize/2, platesize, platesize, 1, 1, rotation());
					}
				}
				
				if(theobject.editoraction.contains("count8"))
				{
					int mx=(int) town.getSizeValue(237);
					
					//oben links
					if(owner5!=null)
					{
						plate=getPlateFillingTexture((int)actionvar5);
						v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), (int)town.getSizeValue(45)+platesize/2+(int)town.getSizeValue(100)+movx+mx, (int)town.getSizeValue(75+60)+platesize/2+(int)town.getSizeValue(50)+movy, width/2, height/2, rotation());
						town.gameWorld.worldSpriteBatch.draw(plate, v2.x-platesize/2, v2.y-platesize/2, platesize/2, platesize/2, platesize, platesize, 1, 1, rotation());
					}
					
					//unten links
					if(owner6!=null)
					{
						plate=getPlateFillingTexture((int)actionvar6);
						v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), (int)town.getSizeValue(45)+platesize/2+(int)town.getSizeValue(100)+movx+mx, (int)town.getSizeValue(75)+platesize/2+(int)town.getSizeValue(22)+movy, width/2, height/2, rotation());
						town.gameWorld.worldSpriteBatch.draw(plate, v2.x-platesize/2, v2.y-platesize/2, platesize/2, platesize/2, platesize, platesize, 1, 1, rotation());
					}
					
					//oben rechts
					if(owner7!=null)
					{
						plate=getPlateFillingTexture((int)actionvar7);
						v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), (int)town.getSizeValue(155)+platesize/2+(int)town.getSizeValue(100)+movx+movx8+mx, (int)town.getSizeValue(75+60)+platesize/2+(int)town.getSizeValue(50)+movy, width/2, height/2, rotation());
						town.gameWorld.worldSpriteBatch.draw(plate, v2.x-platesize/2, v2.y-platesize/2, platesize/2, platesize/2, platesize, platesize, 1, 1, rotation());
					}
					
					//unten rechts
					if(owner8!=null)
					{
						plate=getPlateFillingTexture((int)actionvar8);
						v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), (int)town.getSizeValue(155)+platesize/2+(int)town.getSizeValue(100)+movx+movx8+mx, (int)town.getSizeValue(75)+platesize/2+(int)town.getSizeValue(22)+movy, width/2, height/2, rotation());
						town.gameWorld.worldSpriteBatch.draw(plate, v2.x-platesize/2, v2.y-platesize/2, platesize/2, platesize/2, platesize, platesize, 1, 1, rotation());
					}					
					
				}				
			}
		}
	}
	
	public TextureRegion getPlateFillingTexture(int platefilling)
	{
		TextureRegion plate=null;
		
		//plate = town.gameResourceConfig.textures.get("kitchen_plate1");
		
		plate = town.gameResourceConfig.animations.get("kitchen_plate").getKeyFrames()[0];
		
		if(tempcount==2)
			plate = town.gameResourceConfig.animations.get("kitchen_plate").getKeyFrames()[1];
		
		if(platefilling==5)
			plate=town.gameResourceConfig.animations.get("kitchen_plate").getKeyFrames()[2];
		if(platefilling==4)
			plate=town.gameResourceConfig.animations.get("kitchen_plate").getKeyFrames()[3];
		if(platefilling==3)
			plate=town.gameResourceConfig.animations.get("kitchen_plate").getKeyFrames()[4];
		if(platefilling==2)
			plate=town.gameResourceConfig.animations.get("kitchen_plate").getKeyFrames()[5];
		if(platefilling==1)
			plate=town.gameResourceConfig.animations.get("kitchen_plate").getKeyFrames()[6];
		if(platefilling<1)
			plate=town.gameResourceConfig.animations.get("kitchen_plate").getKeyFrames()[7];
		
		return plate;
	}
	
	public CAnimationTextEvent addAnimationEvent(AnimationEventType animEventType, float value)
	{
		if((town.gameGui.buttonX2.toggleActive || town.gameGui.buttonX4.toggleActive) && 
				animEventType!=AnimationEventType.MONEY  && 
				animEventType!=AnimationEventType.WORKOUTPUT && 
				animEventType!=AnimationEventType.EDUCATION &&
				animEventType!=AnimationEventType.MOVEHOUSE &&
				animEventType!=AnimationEventType.TEXT &&
				animEventType!=AnimationEventType.RESEARCH &&
				animEventType!=AnimationEventType.MAINTAIN &&
						town.gameWorld.animationEvents.size()>5)
			return null;
		
		CAnimationTextEvent event = new CAnimationTextEvent(town, pos_x()+width/2, pos_y()+height/2, value, animEventType, 3+town.gameCam.zoom);
		event.targetObject=this;
		town.gameWorld.animationEvents.add(event);
		
		return event;
	}
	
	public void addAnimationEvent(AnimationEventType animEventType, String value)
	{
		if((town.gameGui.buttonX2.toggleActive || town.gameGui.buttonX4.toggleActive) && animEventType!=AnimationEventType.MONEY && town.gameWorld.animationEvents.size()>5)
			return;
		
		CAnimationTextEvent event = new CAnimationTextEvent(town, pos_x()+width/2, pos_y()+height/2, 0, animEventType, 3+town.gameCam.zoom);
		event.showText=value;
		event.targetObject=this;
		town.gameWorld.animationEvents.add(event);
	}
	
	public void addObjectFillingMulti(int index, int value)
	{
		int val=0;
		
		if(objectFillingMulti.containsKey(index))
			val=objectFillingMulti.get(index);
		
		val+=value;
		
		if(val<0)
			val=0;
		
		if(val>getObjectFillingMultiMax())
			val=getObjectFillingMultiMax();
		
		objectFillingMulti.put(index, val);
	}
	
	void handleWorkoutput()
	{
		if(town.gameWorld.worldPause)
			return;

		if(!bObjectIsReady)
			return;
		
		if(researchObject!=null && researchObject.iResearchCurrentWorkoutput>=researchObject.iResearchTargetWorkoutput)
			researchObject=null;		
		
		//Generate Work Output by Human
		if(workHour>3600)
		{
			if(thehuman!=null)
			{
				int workoutput = thehuman.getWorkOutputPerHour(true, null, null, false);
				CWorldObject wp = thehuman.getWorkplaceByTime(true);
				
				if(wp!=null)
				{
					//*****************
					//Object Condition
					//*****************
					if(wp.defaultObjectCondition>0)
					{
						int mvalue=workoutput/5;
						wp.objectCondition+=mvalue;
						addAnimationEvent(AnimationEventType.MAINTAIN, mvalue);
						if(wp.objectCondition>wp.defaultObjectCondition)
							wp.objectCondition=wp.defaultObjectCondition;
					}
					
					//*********
					//Research
					//*********
					else if(wp.theobject.editoraction.contains("company_college_workingplace_researchlab") || 
							wp.theobject.editoraction.contains("company_objectdesign_officeworkingplace_artist"))
					{
						if(wp.researchObject != null)
						{
							if(wp.researchObject.iResearchCurrentWorkoutput<wp.researchObject.iResearchTargetWorkoutput)
							{
								float icountresearchers=0;
								for(CWorldObject rdesk : wp.theaddress.listWorldObjects) {
									if(rdesk.researchObject!=null && rdesk.researchObject.editoraction.equals(wp.researchObject.editoraction)) {
										icountresearchers++;
									}
								}
								icountresearchers/=1.5f;
								if(icountresearchers>1) {
									workoutput/=icountresearchers;
								}
								
								if(wp.researchObject.editoraction.contains("spaceship") && wp.worker.thehuman.abilitySpaceshipTechnology<1) {
									workoutput=0;
								}
								
								wp.researchObject.iResearchCurrentWorkoutput+=workoutput;
								
								addAnimationEvent(AnimationEventType.WORKOUTPUT, workoutput);
								
								if(wp.researchObject.iResearchCurrentWorkoutput>wp.researchObject.iResearchTargetWorkoutput)
								{
									wp.researchObject.iResearchCurrentWorkoutput=wp.researchObject.iResearchTargetWorkoutput;
									
									if(wp.theobject.editoraction.contains("company_college_workingplace_researchlab")) {
										CInfoTextEvent r1 = new CInfoTextEvent(town, "Research Complete: " + wp.researchObject.objectName, "", new Color(0.3f,0.3f,1f,1f));
										town.gameAudio.playSound("EFFECT_RESEARCHCOMPLETE", -0.7f, false);
										town.gameWorld.infoEvents.add(0, r1);
									}
									else if(wp.theobject.editoraction.contains("company_objectdesign_officeworkingplace_artist")) {
										CInfoTextEvent d1 = new CInfoTextEvent(town, "Object Design Complete: " + wp.researchObject.objectName, "", new Color(0.3f,0.3f,1f,1f));
										town.gameAudio.playSound("EFFECT_DESIGNCOMPLETE", -0.7f, false);
										town.gameWorld.infoEvents.add(0, d1);
									}
									
									String thename = wp.researchObject.objectName;
									thename=thename.toLowerCase();
									thename=thename.trim();
									thename=thename.replaceAll("\\s+","");
									town.setAchievement("res_"+thename);
									town.setAchievement("des_"+wp.researchObject.objectId);
									
									wp.researchObject=null;
								}
								
							}
							else
							{
								if(!wp.researchObject.linkobjectid.isEmpty())
								{
									for(CObject robj : town.gameResourceConfig.listObject)
									{
										if(robj.linkobjectid.equals(wp.researchObject.linkobjectid))
										{
											robj.iResearchCurrentWorkoutput = wp.researchObject.iResearchCurrentWorkoutput;
										}
									}
								}
								
								if(wp.researchObject.iResearchCurrentWorkoutput>wp.researchObject.iResearchTargetWorkoutput)
									wp.researchObject=null;
							}
						}
					}
					
					//**********
					//Earn Money
					//**********
					else if(wp.belongsToCompany!=null && 
							wp.belongsToCompany.companyType==CompanyType.SOFTWARE_DEVELOPMENT)
					{
						int money = Math.round(workoutput*8f);
						
						if(wp.theobject.ATTR_MONEY>0)
							money*=wp.theobject.ATTR_MONEY;
						town.gameWorld.changeTownMoney(Math.round(money));
						town.gameWorld.townStatistics.getCurrentStatistics_Finance().residentEarnsMoney+=Math.round(money);
						addAnimationEvent(AnimationEventType.MONEY, money);
					}
					
					//***********
					//Workoutput
					//***********
					else if(wp.belongsToCompany!=null && wp.generatesOfficeWorkoutput())
					{
						wp.belongsToCompany.addWorkOutput(workoutput, wp.getWorkoutputType());
						addAnimationEvent(AnimationEventType.WORKOUTPUT, workoutput);
					}
				}
			}
		}
		
		//Consume Workoutput
			//Work Consumption / set active/inactive
			
			//Default
			if(theobject.getNeededWorkinputPerHour()>0)
			{
				//Supermarket Warehouse
				if(belongsToCompany!=null && theobject.editoraction.contains("supermarket_foodpallet"))
				{
					if(renderHour>3600 || onlineByWorkInput==false)
					{
						if(objectFilling<getObjectFillingMax())
						{
							int consume=0;
							consume = belongsToCompany.consumeWorkOutput(theobject.getNeededWorkinputPerHour(), getWorkoutputType());
							objectFilling+=consume;
							//showWorkoutput=-consume;
							addAnimationEvent(AnimationEventType.WORKOUTPUT, -consume);
							
							if(objectFilling>getObjectFillingMax())
								objectFilling=getObjectFillingMax();
							
							onlineByWorkInput=true;
						}
						
						if(objectFilling<1)
							onlineByWorkInput=false;
					}
					
					return;
				}				
				else
				{
					if(renderHour>3600 || onlineByWorkInput==false)
					{
						if(belongsToCompany!=null && isActiveByEnergyConsumption() && belongsToCompany.consumeMinWorkOutput(theobject.getNeededWorkinputPerHour(), getWorkoutputType()))
						{
							addAnimationEvent(AnimationEventType.WORKOUTPUT, theobject.getNeededWorkinputPerHour()*-1);
							onlineByWorkInput=true;
							return;
						}
						else
						{
							//Illuminati Defense System konsumiert von irgendeiner illuminati company workoutput
							if((theobject.editoraction.contains("illuminati_defensesystem") || theobject.editoraction.contains("illuminati_defensewarningsystem"))
									&& isActiveByEnergyConsumption()
									)
							{
								for(CCompany comp : town.gameWorld.worldCompanyList)
								{
									if(comp.companyType==CompanyType.ILLUMINATI && comp.getWorkOutput(getWorkoutputType()) >= theobject.getNeededWorkinputPerHour())
									{
										comp.consumeMinWorkOutput(theobject.getNeededWorkinputPerHour(), getWorkoutputType());
										addAnimationEvent(AnimationEventType.WORKOUTPUT, theobject.getNeededWorkinputPerHour()*-1);
										onlineByWorkInput=true;
										return;
									}
								}								
								
								onlineByWorkInput=false;
								return;
							}
							
							onlineByWorkInput=false;
						}
					}
				}
				
				return;
			}			
	}
	
	public Boolean generatesOfficeWorkoutput()
	{
		if(theobject.editoraction.contains("officeworkingplace"))
			return true;
		
		return false;
	}
	
	public WorkoutputType getWorkoutputType()
	{
		if(theobject.editoraction.contains("company_townhall_officeworkingplace_finance"))
			return WorkoutputType.FINANCE;
		
		if(theobject.editoraction.contains("company_townhall_officeworkingplace_resident"))
			return WorkoutputType.POPULATION;
		
		if(theobject.editoraction.contains("company_townhall_officeworkingplace_other"))
			return WorkoutputType.OTHER;

		if(theobject.editoraction.contains("townhall_officeworkingplace_intelligence"))
			return WorkoutputType.INTELLIGENCE;

		
		return WorkoutputType.DEFAULT;
	}
	
	public void dispose()
	{
		//Gdx.app.setLogLevel(5);
		//Gdx.app.debug("", "dispose " + uniqueId);
		
		bDeleted = true;
		
		try
		{
		if(plight!=null)
		{
			plight.setActive(false);
			plight.remove();
			//plight.dispose();
			plight=null;
		}
		
		if(clight!=null)
		{
			clight.setActive(false);
			clight.remove();
			//clight.dispose();
			clight=null;
		}
		
		if(clight2!=null)
		{
			clight2.setActive(false);
			clight2.remove();
			//clight2.dispose();
			clight2=null;
		}
		
		if(dlight!=null)
		{
			dlight.setActive(false);
			dlight.remove();
			//dlight.dispose();
			dlight=null;
		}

		if(box2dBody!=null)
			town.gameWorld.box2dworld.destroyBody(box2dBody);
		if(box2dBody_light!=null)
			town.gameWorld.box2dworld.destroyBody(box2dBody_light);
		}
		catch(Exception ex){}

		
		
		


		theaddress=null;
		theaddress2=null;
		tooltip=null;
		owner=null;
		owner2=null;
		owner3=null;
		owner4=null;
		owner5=null;
		owner6=null;
		owner7=null;
		owner8=null;
		currentFrame=null;
		spriteMoveEvents=null;
		theroom=null;
		researchObject=null;
		belongsToCompany=null;
		worker=null;
		worker2=null;
		objectFillingMulti=null;
		box2dBody=null;
		box2dBody_light=null;
		
		if(actionList_Freetime!=null)
		{
			disposeActions(actionList_Freetime);
			actionList_Freetime.clear();
			actionList_Freetime=null;
		}

		if(actionList_Default!=null)
		{
			disposeActions(actionList_Default);
			actionList_Default.clear();
			actionList_Default=null;
		}

		if(actionList_Default_Priority!=null)
		{
			disposeActions(actionList_Default_Priority);
			actionList_Default_Priority.clear();
			actionList_Default_Priority=null;
		}
		if(actionList_Work!=null)
		{
			disposeActions(actionList_Work);
			actionList_Work.clear();
			actionList_Work=null;
		}
		
		action_Fridge=null; 
		action_Bed=null;
		action_idle=null;
		action_church=null;
		action_Shower=null;
		action_Toilet=null;
		action_ToiletFloor=null;
		action_Workplace=null;
		action_SupermarketBuyin=null;
		action_SupermarketRefillShelf=null;
		action_cookdinner=null;
		action_eatdinner=null;
		action_washdishes=null;
		action_takeoutgarbage=null;
		action_pubaction=null;
		action_fitnessstudio=null;
		action_readbook=null;
		action_watchtv=null;
		action_playground=null;
		action_vehiclerefuel=null;
		action_funeralattend=null;
		action_gotodoctor=null;
		action_laundry=null;
		action_changeclothes=null;
		action_breakroom=null;
		action_zombie=null;
		action_orderpizza=null;
		path=null;
		targetPathObject=null;
		head=null;
		headAnimation=null;
		headFrames=null;
		currentHeadFrame=null;
		isOccupiedBy=null;
		isOccupiedBy2=null;
		isOccupiedBy3=null;
		isOccupiedBy4=null;
		isOccupiedBy5=null;
		isOccupiedBy6=null;
		isOccupiedBy7=null;
		isOccupiedBy8=null;
		isOccupiedBy9=null;
		isOccupiedByExtern=null;
		goByCar_TargetParkingplace=null;
		goByCar_TargetObject=null;

		asyncResult=null;
		
		
		theobject.baseWorldObject=null;
		theobject.town=null;
		theobject=null;
		
		if(thehuman!=null)
		{
			thehuman.baseWorldObject=null;
			thehuman.town=null;
		}
		
		//town.gameWorld=null;
		town=null;
	}
	
	public void disposeActions(List<CAction> list1)
	{
		for(CAction ac : list1)
		{
			if(ac!=null)
			{
				ac.dispose();
			}
		}
	}
	
	public Polygon getBoundingPolygon(int distance)
	{
		Rectangle bounds = null; 
		Polygon polygon = null;
		int btol2=distance;
		
		int boundx=0;
		int boundy=0;
		int boundxm=0;
		int boundym=0;
		
		if(theobject!=null)
		{
			boundx=theobject.ATTR_BOUNDX;
			boundy=theobject.ATTR_BOUNDY;
			boundxm=theobject.ATTR_BOUNDXMOV;
			boundym=theobject.ATTR_BOUNDYMOV;
		}
		
		bounds = new Rectangle(pos_x()-btol2-boundx, pos_y()-btol2-boundy, width+btol2*2+boundx*2, height+btol2*2+boundy*2);
		
		polygon = new Polygon(new float[]{0,0,bounds.width,0,bounds.width,bounds.height,0,bounds.height});
		polygon.setOrigin(bounds.width/2, bounds.height/2);
		polygon.setRotation(rotation());
		
		Vector2 v2 = CHelper.moveVectorByRotationS2D((int)pos_x(), (int)pos_y(), (int)width/2+boundxm, height/2+boundym, (int)(width/2), (int)(height/2), rotation());
		polygon.setPosition(v2.x-bounds.width/2, v2.y-bounds.height/2);
		
		return polygon;
	}
	
	public Polygon getBoundingPolygon(IntersectionMode imode)
	{
		//itype:
		//	0: collision
		//: 1: klick		
		
		Rectangle bounds = null; 
		Polygon polygon = null;
		
		int btol=0; //kleiner machen
		int btol2=0; //größer machen
		
		if(theobject.editoraction.contains("flora")) //flora kleineren bounding radius
			btol=width/4;
		
		if(theobject.editoraction.contains("fence")) //fence: für mouseklick größer
			btol2=7;

		if(imode==IntersectionMode.COLLISION_PLACEWALL)
			btol=1;

		
		if(imode==IntersectionMode.COLLISION_SMALL)
			btol=(int)town.getSizeValue(20);
		
		if(imode==IntersectionMode.COLLISION_SMALL2)
			btol=(int)town.getSizeValue(50);
		
		if(imode==IntersectionMode.COLLISION_WATER)
		{
			btol=width/4;
		}
		
		if(imode==IntersectionMode.COLLISION)
		{
			//btol=5;
			
			if(theobject.editoraction.contains("fence"))
			{
				btol2=0;
				btol=(int)town.getSizeValue(10);
			}
			
			if(theobject.editoraction.contains("floor")) //floor kleineren bounding radius
				btol=(int)town.getSizeValue(10);
		}
		
		int boundx=theobject.ATTR_BOUNDX;
		int boundy=theobject.ATTR_BOUNDY;
		int boundxm=theobject.ATTR_BOUNDXMOV;
		int boundym=theobject.ATTR_BOUNDYMOV;
		
		bounds = new Rectangle(pos_x()+btol-btol2-boundx, pos_y()+btol-btol2-boundy, width-btol*2+btol2*2+boundx*2, height-btol*2+btol2*2+boundy*2);
		
		//Darf hier nicht geprüft werden, da das cobject objekt aller floors das gleiche ist wie das placing cobject
		//		if(theobject.placingCountLevel==2)
		//		{
		//			bounds.width=width*2;
		//			bounds.height=height*2;
		//			bounds.y=pos_y()-height;
		//		}
		//		
		//		if(theobject.placingCountLevel==3)
		//		{
		//			bounds.width=width*3;
		//			bounds.height=height*3;
		//			bounds.y=pos_y()-height*2;
		//		}
		
		polygon = new Polygon(new float[]{0,0,bounds.width,0,bounds.width,bounds.height,0,bounds.height});
		polygon.setOrigin(bounds.width/2, bounds.height/2);
		//polygon.setOrigin(width/2, height/2);
		//polygon.setOrigin(width/2-boundxm/2, height/2-boundym/2);
		polygon.setRotation(rotation());
		
		Vector2 v2 = CHelper.moveVectorByRotationS2D((int)pos_x(), (int)pos_y(), (int)width/2+boundxm, height/2+boundym, (int)(width/2), (int)(height/2), rotation());
		polygon.setPosition(v2.x-bounds.width/2, v2.y-bounds.height/2);
		
		//polygon.setPosition(pos_x(), pos_y());
		//polygon.setPosition(bounds.x, bounds.y);
		
		return polygon;
	}
	
	public CWorldObject getTeachersDeskForStudentsDesk(int itype)
	{
		Object[] teachersdesks = null;
		
		if(itype==0)
			teachersdesks = theaddress.listWorldObjects.stream().filter(item->item.theobject.editoraction.contains("company_school_workingplace_teachersdesk")).toArray();
		else if(itype==3)
			teachersdesks = theaddress.listWorldObjects.stream().filter(item->item.theobject.editoraction.contains("company_church_workingplace_altar")).toArray();
		else
			teachersdesks = theaddress.listWorldObjects.stream().filter(item->item.theobject.editoraction.contains("company_college_workingplace_profslectern")).toArray();
		
		if(teachersdesks!=null && teachersdesks.length>0)
		{
			//Gdx.app.debug("", "test church 2_1");
		
			for(Object teachersdesk : teachersdesks)
			{
				//Gdx.app.debug("", "test church 2_2");
				
				if(((CWorldObject)teachersdesk).worker==null)
					continue;
				
				//Gdx.app.debug("", "test church 2_3");
				
				if(Intersector.overlapConvexPolygons(((CWorldObject)teachersdesk).theobject.getSchoolDeskZonePolygon(), getBoundingPolygon(IntersectionMode.COLLISION)))
				{
					//Gdx.app.debug("", "test church 2_4");
					
					if(Intersector.overlapConvexPolygons(theobject.getSchoolDeskZonePolygon(), ((CWorldObject)teachersdesk).getBoundingPolygon(IntersectionMode.COLLISION)))
					{
						//Gdx.app.debug("", "test church 2_5");
						
						return (CWorldObject)teachersdesk;
					}
				}
			}
		}
		
		return null;
	}

	
	public void handleConsumerPlaces()
	{
		//Autoset Consumerplaces
		//Consumer Places automatisch besetzen
		
		if(theaddress==null)
			return;
		
		if(theaddress.isCloning)
			return;
		
		
		if(!bObjectIsReady)
			return;
		
		//Residential Car
		if(theobject.isCar && !theobject.isCompanyWorkingPlace())
		{
			//Gdx.app.debug("", "owner: " + owner.theobject.editoraction);
			//if(owner!=null)
			//	Gdx.app.debug("", "owner.thehuman: " + owner.thehuman);
			
			if(owner!=null && !owner.thehuman.canWork())
				owner=null;
			
			if(theaddress!=null && owner==null)
			{
				CWorldObject fridge1=null;
				CWorldObject worker1=null;
				for(CWorldObject wobj : theaddress.listWorldObjects)
				{
					if(wobj.bObjectIsReady && wobj.theobject.editoraction.contains("fridge") && wobj.worker!=null && wobj.worker.thehuman.getAge()>=16 && wobj.worker.thehuman.car==null && wobj.worker.thehuman.canWork())
					{
						fridge1=wobj.worker;
						break;
					}
					
					if(wobj.bObjectIsReady && wobj.isHuman() && wobj.thehuman.canWork() && wobj.thehuman.getAge()>=16 && wobj.thehuman.workplaces.size()>0 && wobj.thehuman.car==null)
						worker1=wobj;
				}
				
				CWorldObject setowner=null;
				if(fridge1!=null)
					setowner=fridge1;
				else if(worker1!=null)
					setowner=worker1;
				
				if(setowner!=null)
				{
					owner=setowner;
					setowner.thehuman.car=this;
				}
			}
		}
		
		//School Student Desk
		if(theobject.editoraction.contains("company_school_workingplace_studentsdesk"))
		{
			if(worker==null || worker2==null)
			{
				for(CWorldObject human : town.gameWorld.worldHumans)
				{
					if(human.thehuman.getAge()<7 || human.thehuman.getAge()>18)
						continue;
					
					if(human.thehuman.getEducationValue()>=town.initial_maxschooleducation)
						continue;
					
					if(human.thehuman.workplaces.size()>0)
						continue;
					
					if(human.thehuman.taskobjects.size()>0)
						continue;
					
					//Alle Schulkinder hinterlegen
					//if(human.theaddress==null)
					//	continue;
					
					//int dist = CHelper.getEuclidianDistance((int)human.theaddress.sx, (int)human.theaddress.sy, pos_x(), pos_y());
					//if(dist>10000)
					//	continue;
					
					int typenr=-1;
					if(worker==null)
					{
						if(worker2!=null && human.uniqueId==worker2.uniqueId)
							return;
						
						typenr=1;
					}
					else if(worker2==null)
					{
						if(worker!=null && human.uniqueId==worker.uniqueId)
							return;
						
						typenr=2;
					}
					
					if(typenr>0)
						town.gameWorld.linkWorkerAndWorkplace(human, this, typenr);
					
					return;
				}
			}
		}
		
		//Wardrobe Setze Owner des nächsten  Beds
		if(theobject.editoraction.contains("bedroom_wardrobe") && owner==null && theaddress!=null)
		{
			Polygon wardrobe_polygon = theobject.getWardrobeZonePolygon();
			
			//Suche Bed
			CWorldObject bed1=null;
			int dist1=0;
			
			for(CWorldObject wobj : theaddress.listWorldObjects)
			{
				if(!wobj.bObjectIsReady)
					continue;
				
				if(wobj.theobject.editoraction.contains("bedroom_bed") && (wobj.owner!=null || wobj.owner2!=null))
				{
					//Bed Owner hat schon Wardrobe
					Boolean cont=true;
					if(wobj.owner!=null && wobj.owner.thehuman.wardrobe==null)
						cont=false;
					if(wobj.owner2!=null && wobj.owner2.thehuman.wardrobe==null)
						cont=false;
					
					if(cont)
						continue;
					
					Boolean bCollides = Intersector.overlapConvexPolygons(wardrobe_polygon, wobj.getBoundingPolygon(0));
					
					if(bCollides)
					{
						if(wobj.owner!=null && wobj.owner.thehuman.wardrobe==null)
						{
							owner=wobj.owner;
							owner.thehuman.wardrobe=this;
						}
						else if(wobj.owner2!=null && wobj.owner2.thehuman.wardrobe==null)
						{
							owner=wobj.owner2;
							owner.thehuman.wardrobe=this;
						}			
					}
				}
			}
			
//			if(owner==null)
//			{
//				for(CWorldObject wobj : theaddress.listWorldObjects)
//				{
//					if(wobj.isHuman())
//					{
//						owner=wobj;
//						break;
//					}
//				}
//			}
		}
		
		
		//Bed
		if(theobject.editoraction.contains("bedroom_bed"))
		{
			if(theaddress!=null && (owner==null || (theobject.editoraction.contains("bedroom_bed_double") && owner2==null)))
			{
				for(CWorldObject human : theaddress.listWorldObjects)
				{
					if(human.isHuman() && human.thehuman.bed==null)
					{
						if(owner==null)
						{
							if(owner2!=null && human.uniqueId==owner2.uniqueId)
								return;
							
							human.thehuman.bed=this;
							owner=human;
						}
						else if(theobject.editoraction.contains("bedroom_bed_double") && owner2==null)
						{
							if(owner!=null && human.uniqueId==owner.uniqueId)
								return;
							
							human.thehuman.bed=this;
							owner2=human;
						}
						
						break;
					}
				}
				
				//Add homeless humans to address
				if(owner==null || (theobject.editoraction.contains("bedroom_bed_double") && owner2==null))
				{
					Boolean bKidsOK=false;
					for(CWorldObject h1 : theaddress.listWorldObjects)
					{
						if(h1.isHuman() && h1.thehuman.getAge()>=18)
						{
							bKidsOK=true;
							break;
						}
					}
					
					
					for(CWorldObject human : town.gameWorld.worldHumans)
					{
						if(human.isHuman() && human.thehuman.bed==null && human.theaddress==null)
						{
							if(human.thehuman.getAge()<18 && !bKidsOK)
								continue;
							
							human.theaddressid=theaddress.addressId;
							human.theaddress=theaddress;
							theaddress.addWorldObject(human);
							
							break;
						}
					}
				}
				
			}
		}
		
		
		//Dining Table
		if(theobject.editoraction.contains("diningtable"))
		{
			Boolean bSearch=false;
			if(owner==null || owner2==null)
				bSearch=true;
			
			if(theobject.editoraction.contains("count4") || theobject.editoraction.contains("count6") || theobject.editoraction.contains("count8"))
				if(owner3==null || owner4==null)
					bSearch=true;
			
			if(theobject.editoraction.contains("count6"))
				if(owner5==null || owner6==null)
					bSearch=true;
			
			if(theobject.editoraction.contains("count8"))
				if(owner7==null || owner8==null)
					bSearch=true;			
			
			if(bSearch)
			{
				for(CWorldObject obj : theaddress.listWorldObjects)
				{
					if(!obj.bObjectIsReady)
						continue;
					
					CWorldObject human=null;
					
					if(theaddress.addressType.contains("public")) //auf public adr sind humans nur als worker auf den workplaces hinterlegt
					{
						if(obj.worker!=null)
							human=obj.worker;
						if(obj.worker2!=null)
							human=obj.worker2;
					}
					else
					{
						if(obj.isHuman())
							human=obj;
					}
					
					//if(human==null && human2==null)
					if(human==null)
						continue;
					
					setResidentAsDiningtableSeatOwner(human);
					
					//setResidentAsDiningtableSeatOwner(human2);
				}
			}
		}
	}
	
	public void setResidentAsDiningtableSeatOwner(CWorldObject human)
	{
		//Hat Resident bereits einen Dinner Table?
		if(human==null || theaddress==null || human.thehuman==null)
			return;
		
		Optional<CWorldObject> table1 = human.thehuman.taskobjects.values().stream().filter(item->item.bObjectIsReady && item.theobject.editoraction.contains("diningtable") && item.theaddress.addressType.contains(theaddress.addressType)).findFirst();
		if(table1.isPresent())
		{
			
			//Set Cook as Eater
			CWorldObject t1 = table1.get();
			if(t1.worker!=null && t1.worker.uniqueId==human.uniqueId)
			{
				Boolean bSet=true;
				if(t1.owner!=null && t1.owner.uniqueId==human.uniqueId)
					bSet=false;
				if(t1.owner2!=null && t1.owner2.uniqueId==human.uniqueId)
					bSet=false;
				if(t1.owner3!=null && t1.owner3.uniqueId==human.uniqueId)
					bSet=false;
				if(t1.owner4!=null && t1.owner4.uniqueId==human.uniqueId)
					bSet=false;
				if(t1.owner5!=null && t1.owner5.uniqueId==human.uniqueId)
					bSet=false;
				if(t1.owner6!=null && t1.owner6.uniqueId==human.uniqueId)
					bSet=false;
				if(t1.owner7!=null && t1.owner7.uniqueId==human.uniqueId)
					bSet=false;
				if(t1.owner8!=null && t1.owner8.uniqueId==human.uniqueId)
					bSet=false;
				
				
				if(bSet)
				{
					if(t1.owner==null)
						t1.owner=human;
					else if(t1.owner2==null)
						t1.owner2=human;
					else if(t1.owner3==null && (t1.theobject.editoraction.contains("count4") || t1.theobject.editoraction.contains("count6") || t1.theobject.editoraction.contains("count8")))
						t1.owner3=human;
					else if(t1.owner4==null && (t1.theobject.editoraction.contains("count4") || t1.theobject.editoraction.contains("count6") || t1.theobject.editoraction.contains("count8")))
						t1.owner4=human;
					else if(t1.owner5==null && (t1.theobject.editoraction.contains("count6") || t1.theobject.editoraction.contains("count8")))
						t1.owner5=human;
					else if(t1.owner6==null && (t1.theobject.editoraction.contains("count6") || t1.theobject.editoraction.contains("count8")))
						t1.owner6=human;
					else if(t1.owner7==null && (t1.theobject.editoraction.contains("count8")))
						t1.owner7=human;
					else if(t1.owner8==null && (t1.theobject.editoraction.contains("count8")))
						t1.owner8=human;
				}
			}
			
			return;
		}
		
		//Gdx.app.debug("", "" + human.uniqueId + ", " + human.thehuman.taskobjects.size());
		
		if(owner==null)
		{
			owner=human;
			human.thehuman.taskobjects.put(uniqueId, this);
			return;
		}
		
		if(owner2==null)
		{
			owner2=human;
			human.thehuman.taskobjects.put(uniqueId, this);
			return;
		}
		
		if(theobject.editoraction.contains("count4") || theobject.editoraction.contains("count6") || theobject.editoraction.contains("count8"))
		{
			if(owner3==null)
			{
				owner3=human;
				human.thehuman.taskobjects.put(uniqueId, this);
				return;
			}
			
			if(owner4==null)
			{
				owner4=human;
				human.thehuman.taskobjects.put(uniqueId, this);
				return;
			}
		}
		
		if(theobject.editoraction.contains("count6") || theobject.editoraction.contains("count8"))
		{
			if(owner5==null)
			{
				owner5=human;
				human.thehuman.taskobjects.put(uniqueId, this);
				return;
			}
			
			if(owner6==null)
			{
				owner6=human;
				human.thehuman.taskobjects.put(uniqueId, this);
				return;
			}
		}
		
		if(theobject.editoraction.contains("count8"))
		{
			if(owner7==null)
			{
				owner7=human;
				human.thehuman.taskobjects.put(uniqueId, this);
				return;
			}
			
			if(owner8==null)
			{
				owner8=human;
				human.thehuman.taskobjects.put(uniqueId, this);
				return;
			}
		}
	}
	
	public void handleOther()
	{
		//School: Student von Platz entfernen, wenn Max Education erreicht
		if(theobject.editoraction.contains("company_school_workingplace_studentsdesk"))
		{
			if(worker!=null && worker.thehuman.getEducationValue()>=town.initial_maxschooleducation)
			{
				town.gameWorld.infoEvents.add(new CInfoTextEvent(town, worker.thehuman.getName() + " graduated from school", Color.WHITE));
				town.gameWorld.removeWorker(worker, this);
			}
			
			if(worker2!=null && worker2.thehuman.getEducationValue()>=town.initial_maxschooleducation)
			{
				town.gameWorld.infoEvents.add(new CInfoTextEvent(town, worker2.thehuman.getName() + " graduated from school", Color.WHITE));
				town.gameWorld.removeWorker(worker2, this);
			}
		}
		
		//College: Student von Platz entfernen, wenn Max Education erreicht
		if(theobject.editoraction.contains("company_college_workingplace_studentsdesk"))
		{
			if(worker!=null && worker.thehuman.getEducationValue()>=2f && worker.thehuman.getEducationValue()<2.1f)
			{
				town.gameWorld.infoEvents.add(new CInfoTextEvent(town, worker.thehuman.getName() + " earned the bachelor's degree", Color.WHITE));
				worker.thehuman.setEducationValue(2.1f);
			}
			if(worker2!=null && worker2.thehuman.getEducationValue()==2f && worker2.thehuman.getEducationValue()<2.1f)
			{
				town.gameWorld.infoEvents.add(new CInfoTextEvent(town, worker2.thehuman.getName() + " earned the bachelor's degree", Color.WHITE));
				worker2.thehuman.setEducationValue(2.1f);
			}
			
			if(worker!=null && worker.thehuman.getEducationValue()>=town.initial_maxcollegeeducation)
			{
				town.gameWorld.infoEvents.add(new CInfoTextEvent(town, worker.thehuman.getName() + " earned the master's degree", Color.WHITE));
				town.gameWorld.removeWorker(worker, this);
			}
			
			if(worker2!=null && worker2.thehuman.getEducationValue()>=town.initial_maxcollegeeducation)
			{
				town.gameWorld.infoEvents.add(new CInfoTextEvent(town, worker2.thehuman.getName() + " earned the master's degree", Color.WHITE));
				town.gameWorld.removeWorker(worker2, this);
			}
		}
	}
	
	public void handleObjectDependencies()
	{
		//School: Student Desk benötigt Teacher's Desk
		if(theobject.editoraction.contains("company_school_workingplace_studentsdesk"))
		{
			bStudentDeskHasTeachersDesk=false;
			workTime1_From = -1;
			workTime1_To = -1;
			
			workTime2_From = -1;
			workTime2_To = -1;
			
			teachersDesk = getTeachersDeskForStudentsDesk(0);
			
			if(teachersDesk!=null)
			{
				bStudentDeskHasTeachersDesk=true;
				workTime1_From = teachersDesk.workTime1_From;
				workTime1_To = teachersDesk.workTime1_To;
				
				workTime2_From = teachersDesk.workTime2_From;
				workTime2_To = teachersDesk.workTime2_To;
			}
		}
		
		//College: Student Desk benötigt Professors Lectern
		if(theobject.editoraction.contains("company_college_workingplace_studentsdesk"))
		{
			bStudentDeskHasTeachersDesk=false;
			workTime1_From = -1;
			workTime1_To = -1;
			workTime2_From = -1;
			workTime2_To = -1;
			
			teachersDesk = getTeachersDeskForStudentsDesk(1);
			
			if(teachersDesk!=null)
			{
				bStudentDeskHasTeachersDesk=true;

				workTime1_From = teachersDesk.workTime1_From;
				workTime1_To = teachersDesk.workTime1_To;
				
				workTime2_From = teachersDesk.workTime2_From;
				workTime2_To = teachersDesk.workTime2_To;
			}
		}
	}
	
//	public Rectangle getBoundingRectangle()
	
	public void initBox2DBody()
	{
		//if(1==1)
		//	return;
	
		
		//Default: keinen box2d body erstellen
				
		if(theobject.editoraction.length()>0) //Box2dBody nicht benutzen
			return;
		
		
		
		
		
		{
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyType.StaticBody;
			bodyDef.position.set((pos_x()+width/2)*town.gameWorld.b2dvaluePos, (pos_y()+height/2)*town.gameWorld.b2dvaluePos);
			box2dBody = town.gameWorld.box2dworld.createBody(bodyDef);
			
			PolygonShape shape = new PolygonShape();
			
			float fparam=2.2f;
			
			shape.setAsBox((width/fparam)*town.gameWorld.b2dvalueSize, (height/fparam)*town.gameWorld.b2dvalueSize);
				        
			FixtureDef fixtureDef = new FixtureDef();
						
			fixtureDef.shape = shape;
			fixtureDef.density = 0.5f; 
			fixtureDef.friction = 0.4f;
			fixtureDef.restitution = 2f; // Make it bounce a little bit
			
			if(thehuman!=null)
				fixtureDef.isSensor=true;
			else
				fixtureDef.isSensor=true; //20150830 -> alle Objekte sind Sensoren
			
			Fixture fixture = box2dBody.createFixture(fixtureDef);
			
			Filter filt = fixture.getFilterData(); 

			//lichtdurchlässig
			//if(theobject.editoraction.contains("light") || theobject.editoraction.contains("window"))
			{
				filt.categoryBits=0;
				filt.groupIndex=0;
				filt.maskBits=0;
			}
			
			//nicht lichtdurchlässig
			if(theobject.editoraction.contains("buildingwall"))
			{
				filt.categoryBits=255;
				filt.groupIndex=255;
				filt.maskBits=0;
			}
			
			fixture.setFilterData(filt);
			float rotation2 = rotation();
			if(box2dBody!=null)
				box2dBody.setTransform((pos_x()+width/2)*town.gameWorld.b2dvaluePos, (pos_y()+height/2)*town.gameWorld.b2dvaluePos, rotation2/57);
			shape.dispose();
			
			if(box2dBody!=null)
				box2dBody.setUserData(this.theobject); //das geht nicht
			
			//Zweiter Box2dbody für Box2DLight 
			box2dBody_light=null;
//			if(theobject.editoraction.contains("wall") || theobject.editoraction.contains("door"))
//			{
//				BodyDef bodyDeflight = new BodyDef();
//				bodyDeflight.type = BodyType.StaticBody;
//				bodyDeflight.position.set((pos_x()+width/2), (pos_y()+height/2));
//				box2dBody_light = gameWorld.box2dworld.createBody(bodyDeflight);
//				PolygonShape shape2 = new PolygonShape();
//				shape2.setAsBox((width)/1.5f, (height)/1.5f);
//				FixtureDef fixtureDef2 = new FixtureDef();
//				fixtureDef2.shape = shape2;
//				fixtureDef2.density = 0.5f; 
//				fixtureDef2.friction = 0.4f;
//				fixtureDef2.restitution = 2f; // Make it bounce a little bit				
//				fixtureDef2.isSensor=true; //20150830 -> alle Objekte sind Sensoren
//				Fixture fixture2 = box2dBody_light.createFixture(fixtureDef2);
//				Filter filt2 = fixture2.getFilterData();
//				filt2.categoryBits=255;
//				filt2.groupIndex=255;
//				filt2.maskBits=0;
//				fixture2.setFilterData(filt2);
//				box2dBody_light.setTransform((pos_x()+width/2), (pos_y()+height/2), rotation2/57);
//				shape2.dispose();
//				box2dBody_light.setUserData(this.theobject); //des geht idde
//			}
		}		
	}

	
//	@Override
//	protected Void doInBackground() {
//		handleActions(gameWorld.stateTime);
//	}
	
	@Override
	public Void call() throws Exception {
		//Gdx.app.debug("", "call async1");
		handleActions(town.gameWorld.stateTime);
		return null;
	}
}

