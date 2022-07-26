package com.mygdx.game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.badlogic.gdx.utils.async.AsyncResult;
import com.badlogic.gdx.utils.async.AsyncTask;
import com.mygdx.game.CAction.ActionMode;
import com.mygdx.game.CAction.ValueType;
import com.mygdx.game.CAddress.AddressSide;
import com.mygdx.game.CAnimationTextEvent.AnimationEventType;
import com.mygdx.game.CCompany.CompanyType;
import com.mygdx.game.CCompany.WorkoutputType;
import com.mygdx.game.CHelper.IntersectionMode;
import com.mygdx.game.CHuman.CJobSkillClass;
import com.mygdx.game.CStatistics.CStatisticsData_Finance;
import com.mygdx.game.CStatistics.CStatisticsData_Other;
import com.mygdx.game.CStatistics.CStatisticsData_Population;

public class CWorld {
	ArrayList<CAnimationTextEvent> animationEvents;
	ArrayList<CInfoTextEvent> infoEvents;
	int infoEventMoving;
	ArrayList<CSpriteMoveEvent> spriteMoveEvents;
	class CWolke{
		int x;
		int y;
		int w;
		int h;
		int z;
	}
	public List<CWolke> worldWolken;
	int wolke1x;
	int wolke1y;
	int wolke1dir;
	int wolke2x;
	int wolke2y;
	int wolke1w;
	int wolke1h;
	int wolke2w;
	int wolke2h;
	
	public AsyncExecutor asyncExecutor;
	public AsyncResult<Void> asyncResult;
	public Hashtable<Integer, Integer> tempObjectBonusPlaceList;
		
	public List<CWorldObject> warningObjects;
	public List<CWorldObject> worldObjects;
	public List<CWorldObject> worldFootpath;
	public List<CWorldObject> worldRoad;
	public List<CWorldObject> worldOutdoorLights;
	public List<CWorldObject> worldCarpets;
	public List<CWorldObject> worldHumans;
	public List<CWorldObject> worldUnemployed;
	public List<CWorldObject> worldZombies;
	public List<CWorldObject> worldZombieEntrances;
	public List<CWorldObject> worldDefenseWarning;
	public List<CWorldObject> worldWatersystems;
	public List<CWorldObject> tempConstructionObjects;
	
	public Hashtable<Integer, CWorldObject> tempHumansDead;// Für Cemetery Action 
	//Achtung: die instanzen in dieser liste enthalten nicht die aktuellen feldwerte die in worldhumans gesetzt sind
	
	public List<CWorldObject> worldBirds;
	public List<CWorldObject> worldDrawSpecial;
	public List<CWorldObject> worldDrawSpecial2; //zorder 1 höher als worldDrawSpecial zb foodpallett auf pallet truck
	public List<CWorldObject> worldCars;
	
	public List<CWorldObject> worldCoverlights;
	
	public List<CWorldObject> worldGarbageContainers;
	public List<CWorldObject> worldGroundObjects;
	public List<CWorldObject> worldWaterObjects; //Water ist zusätzlich in eigener Waterliste (ist auch in ground liste -> ground kann über water sein und umgekehrt)
	
	public List<CWorldObject> tempListDrawAdditional;
	
	public List<CWorldObject> worldRoomObjects;
	public List<CWorldObject> worldTempRoomObjects;
	public List<CAddress> worldAddressList;
	public List<CAddress> cloneAddressList;
	public List<CCompany> worldCompanyList;
	public List<CWorldObject> cloneRoomList;
	
	//int news_countHumanContagious;
	int news_zombieApocalypse;
	
	//public Hashtable<Integer, Integer> priceObjects; //ObjectId, count
	//public Hashtable<Integer, Integer> tempPriceObjects_temp; //ObjectId, count
	//public Hashtable<Integer, Integer> tempPriceObjects; //ObjectId, count
	
    FrameBuffer fbo;
    SpriteBatch fboBatch;
    Boolean bRenderFrameBuffer=true;
	
    public Boolean bCloneAddressRender;
    
    public Boolean bHandleObject;
    
	public CStatistics townStatistics;
	public int addressArchitectPlanningValue;
	public CWeather worldWeather;
	public int townMoney;
	private int waterOutput;
	private int energyOutput;
	private int waterConsumption;
	private int energyConsumption;
	public Boolean bBackwhite;
	public float renderSecond;
	public float scrollTimer;
	public float renderHour;
	public int birdcount;
	public CWorldObject mouseOverObject;
	
	int countIlluminatiDefenseSystems;
	
	int tempWaterOutput;
	int tempEnergyOutput;
	int tempWaterConsumption;
	int tempEnergyConsumption;
	public Boolean bObjectInfoTooltipIsRendering;
	public Boolean bPlacingOK;
	public Boolean bMoveHouse;
	public World box2dworld;
	public SpriteBatch worldSpriteBatch;
	//public OrthographicCamera gameCamera;
	//public OrthographicCamera lightCamera;
	public float stateTime;
	public Box2DDebugRenderer box2dDebugRenderer;
	public CWorldObject markerObject;
	public CResourceConfig gameResourceConfig;
	//public CGui gameGui;
	public float alphaWorldObjects;
	public float alpharoomObjects;
	public RayHandler rayHandler;
	public CTime worldTime;
	public BitmapFont font;
	public int i;
	public int j;
	public Boolean bButtonDown;
	public Boolean worldPause;
	public Boolean bUseMipMapping;
	public Boolean bRenderBox2D;
	public int floorSize;
	public int wallSize;
	Texture background;
	public Boolean bDebugActions;
	public static int mapsize;
	
	public CAddress delAddress; //Delete Address Action
	
	//Pathfinding
	public Boolean bRenderPathfinding;
	public int pathmapsize;
	public int pathmapsize_road;
	public int pathmapsize_footpath;
	public int pathmapsize_defensewall;
    public static int nodesize;
	public static CWorldObject[] pathmap;
	public static CWorldObject[] pathmap_road;
	public static CWorldObject[] pathmap_footpath;
	public static CWorldObject[] pathmap_defensewall;
 	public static CAStar astar = null;
 	public static CAStar astar_road = null;
 	public static CAStar astar_footpath = null;
 	public int maxPathfindingsPerStep;
 	
	public ShapeRenderer shapeRenderer1;
	public CExtendedShapeRenderer shapeRenderer2;
	
 	//public float b2dvalue=0.02f; //Box2d Mapping Value
 	public float b2dvalueSize=0.02f; //Box2d Mapping Value -> Für Lights funktioniert ein anderer Wert nicht, wenn Kollision benutzt werden soll und Performance runter geht -> was anderes überlegen
 	public float b2dvaluePos=0.02f;
 	
 	public long renderUniqueNumber; //Jeder Render Step bekommt eine Id um im Pathfinding zu erkennen wieviele getPath Anfragen pro Render ankommen und zu begrenzen -> das bringt nichts
 	public Random rand;
 	
	public Boolean bNewResidentSpawn=false;
	public int spawnNewResidentHour=-1;
	public int spawnNewZombieHour=-1;
 	
	int lastHappinessFundDay;
	
 	//IntArray path = astar.getPath(startX, startY, targetX, targetY);
 	//int x = path.get(i);
 	//int y = path.get(i + 1);
 	
	public PointLight placinglight; //for objectplacing by night
	//public PointLight placinglight2; //for objectplacing by night
	public PointLight infolight;
	
	public CTown town;
	
	private float stateTime_pause;
	
	public float getStateTime()
	{
		if(worldPause)
			return stateTime_pause;
		else
		{
			stateTime_pause=stateTime;
			return stateTime;
		}
	}
	
 	//INIT----------------------------------------------------->
 	public CWorld(CTown t)
 	{
 		//Gdx.app.debug("", "initialize");
 		
 		
 		town=t;
 		rand=new Random();
 		
 		
 		bHandleObject=true;
 		news_zombieApocalypse=0;
 		birdcount=0;
 		addressArchitectPlanningValue=0;
 		bObjectInfoTooltipIsRendering=false;
 		bPlacingOK=true;
 		bMoveHouse=false;
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        
 		mapsize=5000;
 		nodesize=100;
 		//background = new Texture(Gdx.files.internal("gfx/background/gras_neu5.jpg"));
 		//background = new Texture(Gdx.files.internal("gfx/background/gras_snow3.jpg"));
 		bDebugActions=false;
 		renderUniqueNumber=0;
 		maxPathfindingsPerStep=10;
 		
 		townMoney=town.startingMoney;
 		delAddress=null;
 		 		
 		renderSecond=0;
 		scrollTimer=0;
 		renderHour=0;
 		
		floorSize=128; //default
		wallSize=64; //default
 	}
 	
 	public void resetMarkerObject()
 	{
 		if(markerObject!=null)
 		{
 			if(markerObject.scrollwidth>0)
 			{
 				markerObject.width=markerObject.scrollwidth;
 				markerObject.height=markerObject.scrollheight;
 			}
 			
	 		markerObject.scrollheight=0;
	 		markerObject.scrollwidth=0;
	 		markerObject=null;
 		}
 	}
 	
    protected void finalize()
    {
       //Gdx.app.debug("", "finalize");
    }
 	
	public void init(OrthographicCamera cam, CResourceConfig config) 
	{
		if(asyncExecutor!=null)
			asyncExecutor.dispose();
		
		//int cores = Runtime.getRuntime().availableProcessors();
		//int threads=cores-2;
		//if(threads<1)
		//	threads=1;
		//cores=3;
		
		town.gameConfigIni.readIni();
		int threads=town.nrthreads;
		if(threads<1)
			threads=1;
		
		//int threads=1;
		asyncExecutor = new AsyncExecutor(threads); //je mehr threads desto weniger fps aber desto flüssiger die animationen
				
		bCloneAddressRender=false;
		town.lodvalue=town.gameConfigIni.lodValue;
		countIlluminatiDefenseSystems=0;
		addressArchitectPlanningValue=0;
		bObjectInfoTooltipIsRendering=false;
		lastHappinessFundDay=-1;
		waterOutput=0;
		energyOutput=0;
		waterConsumption=0;
		energyConsumption=0;		
		
		spawnNewZombieHour=-1;
		spawnNewResidentHour=-1;
		
		animationEvents=new ArrayList<CAnimationTextEvent>();
		infoEvents=new ArrayList<CInfoTextEvent>();
		infoEventMoving=0;
		spriteMoveEvents=new ArrayList<CSpriteMoveEvent>();
		tempConstructionObjects=new ArrayList<CWorldObject>();
		
		initWolken();
		
		mouseOverObject=null;
		tempWaterOutput=0;
		tempEnergyOutput=0;
		tempWaterConsumption=0;
		tempEnergyConsumption=0;
		
    	worldTime = new CTime(town);
    	worldTime.init(1, 1, 0, 0, 0);		
		
		bRenderBox2D=false;
		
		initAStar();
    	
		if(shapeRenderer1!=null)
			shapeRenderer1.dispose();
		if(shapeRenderer2!=null)
			shapeRenderer2.dispose();
		
    	shapeRenderer1 = new ShapeRenderer();
    	shapeRenderer2 = new CExtendedShapeRenderer(town.gameCam.combined);
    	
    	bUseMipMapping = false;
		bButtonDown = false;
		gameResourceConfig = config;
		
		warningObjects = new ArrayList<CWorldObject>();
		
		worldObjects = new ArrayList<CWorldObject>();
		worldDrawSpecial = new ArrayList<CWorldObject>();
		worldDefenseWarning = new ArrayList<CWorldObject>();
		worldWatersystems = new ArrayList<CWorldObject>();
		worldCars = new ArrayList<CWorldObject>();
		worldDrawSpecial2 = new ArrayList<CWorldObject>();
		worldCarpets = new ArrayList<CWorldObject>();
		worldGarbageContainers = new ArrayList<CWorldObject>();
		tempListDrawAdditional = new ArrayList<CWorldObject>();
		
		worldGroundObjects = new ArrayList<CWorldObject>();
		worldWaterObjects = new ArrayList<CWorldObject>();
		
		worldFootpath = new ArrayList<CWorldObject>();
		worldOutdoorLights = new ArrayList<CWorldObject>();
		worldRoad = new ArrayList<CWorldObject>();
		worldHumans = new ArrayList<CWorldObject>();
		worldUnemployed = new ArrayList<CWorldObject>();
		worldBirds = new ArrayList<CWorldObject>();
		worldCoverlights = new ArrayList<CWorldObject>();
		
		worldZombies = new ArrayList<CWorldObject>();
		worldZombieEntrances = new ArrayList<CWorldObject>();
		//tempHumansDead = new ArrayList<CWorldObject>();
		tempHumansDead = new Hashtable<Integer, CWorldObject>(); 
		tempObjectBonusPlaceList = new Hashtable<Integer, Integer>();
				
		//priceObjects = new Hashtable<Integer, Integer>();
		//tempPriceObjects = new Hashtable<Integer, Integer>();
		//tempPriceObjects_temp = new Hashtable<Integer, Integer>();
		
		worldTempRoomObjects = new ArrayList<CWorldObject>();
		worldRoomObjects = new ArrayList<CWorldObject>();
		worldAddressList = new ArrayList<CAddress>();
		cloneAddressList = new ArrayList<CAddress>();
		worldCompanyList = new ArrayList<CCompany>();
		cloneRoomList = new ArrayList<CWorldObject>();

		//worldJobList = new ArrayList<CJob>();
		
		//worldRoomTilemap_floor = new Hashtable<Vector2, CWorldObject>();
		//worldRoomTilemap_wall = new Hashtable<Vector2, CWorldObject>();
		
		if(worldSpriteBatch!=null)
			worldSpriteBatch.dispose();
		worldSpriteBatch = new SpriteBatch();
		//gameCamera = cam;
		//lightCamera = lightcam;
		stateTime = 0.0f;
		markerObject = null;
		worldPause = false;
		alphaWorldObjects = 1f;
		alpharoomObjects = 1f;
		
		
		Box2D.init(); 
		initBox2D();
		
		worldWeather = new CWeather(town);
		townStatistics = new CStatistics(town);
		
		town.gameResourceConfig.resetObjects();
		
		//		box2dworld = new World(new Vector2(0, 0), true);
		//		box2dDebugRenderer = new Box2DDebugRenderer();
		
		//    	RayHandler.setGammaCorrection(true); 
		//    	RayHandler.useDiffuseLight(true); 
		//    	rayHandler = new RayHandler(this.box2dworld);
		//    	rayHandler.setCombinedMatrix(cam);
		//    	rayHandler.setShadows(true);
		//    	rayHandler.setBlurNum(1);
		
		//    	float ambvalue = getAmbientLightValue(worldTime);
		//    	rayHandler.setAmbientLight(ambvalue,ambvalue,ambvalue,ambvalue);
		//    	Light.setContactFilter((short)255, (short)255, (short)0);
		

	}
	public void initBox2D()
	{
		if(rayHandler!=null)
			rayHandler.dispose();
		
		if(box2dworld!=null)
			box2dworld.dispose();
		
		//Box2D.init(); 
		box2dworld = new World(new Vector2(0, 0), true);
		box2dDebugRenderer = new Box2DDebugRenderer();
		
    	RayHandler.setGammaCorrection(true); 
    	RayHandler.useDiffuseLight(true); 
    	rayHandler = new RayHandler(this.box2dworld);
    	//rayHandler.setCombinedMatrix(gameCamera.combined);
    	
    	rayHandler.setCombinedMatrix(town.gameCam);
    	rayHandler.setShadows(true);
    	
    	float ambvalue = getAmbientLightValue(worldTime);
    	rayHandler.setAmbientLight(ambvalue,ambvalue,ambvalue,ambvalue);
    	rayHandler.setBlurNum(1);
    	rayHandler.setCulling(true);
    	Light.setGlobalContactFilter((short)255, (short)255, (short)0);
    	
    	
    	placinglight = new PointLight(rayHandler, 20, Color.WHITE, 1000, 0, 0);
    	placinglight.setXray(true);
    	placinglight.setStaticLight(true);
    	placinglight.setActive(false);
    	
//    	infolight = new PointLight(rayHandler, 20, Color.WHITE, 1000, 0, 0);
//    	infolight.setXray(true);
//    	infolight.setStaticLight(true);
//    	infolight.setActive(false);
    	
    	
    	//placinglight2 = new PointLight(rayHandler, 100, Color.WHITE, 2000, 0, 0);
    	//placinglight.setActive(false);
    	
    	//Taschenlampe:
    	//light = new ConeLight(rayHandler, 10, null, 1000, 0, 0, 0f, 30); 
    	//light.attachToBody(body ,0, 0, 0);
	}	
	public void initAStar()
	{
		pathmapsize=mapsize/nodesize;
		pathmapsize_road=Math.round(mapsize/town.roadrastersize);
		pathmapsize_footpath=Math.round(mapsize/town.footpathsize);
		pathmapsize_defensewall=Math.round(mapsize/town.defensewallsize);
		
		pathmap = new CWorldObject[pathmapsize*pathmapsize];
		
		pathmap_road = new CWorldObject[pathmapsize_road*pathmapsize_road];
		pathmap_footpath = new CWorldObject[pathmapsize_footpath*pathmapsize_footpath];
		pathmap_defensewall = new CWorldObject[pathmapsize_defensewall*pathmapsize_defensewall];
		
		if(CWorld.astar!=null)
			CWorld.astar.dispose();
		CWorld.astar = null;
		
		if(CWorld.astar_road!=null)
			CWorld.astar_road.dispose();
		CWorld.astar_road = null;
		
		if(CWorld.astar_footpath!=null)
			CWorld.astar_footpath.dispose();
		CWorld.astar_footpath = null;
		
     	astar = new CAStar(pathmapsize, pathmapsize, town) 
    	{
    		protected CWorldObject isValid (int x, int y)
    		{
    			try
    			{
    				return CWorld.pathmap[x + y * pathmapsize];
    			}
    			catch(Exception ex)
    			{
    				//String text = ex.getMessage();
    			}
    			
    			return null;
    		}
    	};
     	astar_road = new CAStar(pathmapsize_road, pathmapsize_road, town) 
    	{
    		protected CWorldObject isValid (int x, int y) 
    		{
    			try
    			{
    				return CWorld.pathmap_road[x + y * pathmapsize_road];
    			}
    			catch(Exception ex)
    			{
    				//String text = ex.getMessage();
    			}
    			
    			return null;
    		}
    	};
    	astar_road.isRoad=true;
    	
     	astar_footpath = new CAStar(pathmapsize_footpath, pathmapsize_footpath, town) 
    	{
     		protected CWorldObject isValid (int x, int y)
    		{
    			try
    			{
    				return CWorld.pathmap_footpath[x + y * pathmapsize_footpath];
    			}
    			catch(Exception ex)
    			{
    				//String text = ex.getMessage();
    			}
    			
    			return null;
    		}
    	};
    	astar_footpath.isFootpath=true;
	}
	
	public void dispose()
	{
		for(CWorldObject obj : worldObjects)
			obj.dispose();
		worldObjects.clear();

		for(CWorldObject obj : worldDrawSpecial2)
			obj.dispose();
		worldDrawSpecial2.clear();
		
		for(CWorldObject obj : worldCarpets)
			obj.dispose();
		worldCarpets.clear();		
		
		for(CWorldObject obj : worldCars)
			obj.dispose();
		worldCars.clear();		
		
		for(CWorldObject obj : worldHumans)
			obj.dispose();
		worldHumans.clear();
		worldUnemployed.clear();

		for(CWorldObject obj : worldBirds)
			obj.dispose();
		worldBirds.clear();

		for(CWorldObject obj : worldCoverlights)
			obj.dispose();
		worldCoverlights.clear();
		
		for(CWorldObject obj : worldDrawSpecial)
			obj.dispose();
		worldDrawSpecial.clear();
				
		for(CWorldObject obj : worldDefenseWarning)
			obj.dispose();
		worldDefenseWarning.clear();		

		for(CWorldObject obj : worldWatersystems)
			obj.dispose();
		worldWatersystems.clear();		
		
		for(CWorldObject obj : worldZombies)
			obj.dispose();
		worldZombies.clear();

		for(CWorldObject obj : worldZombieEntrances)
			obj.dispose();
		worldZombieEntrances.clear();
		
		for(CWorldObject obj : worldGarbageContainers)
			obj.dispose();
		worldGarbageContainers.clear();
		
		for(CWorldObject obj : worldGroundObjects)
			obj.dispose();
		worldGroundObjects.clear();
		
		//for(CWorldObject obj : worldWaterObjects) //Water wird schon in ground disposed
			//obj.dispose();
		worldWaterObjects.clear();
		
		for(CWorldObject obj : worldFootpath)
			obj.dispose();
		worldFootpath.clear();
		
		for(CWorldObject obj : worldOutdoorLights)
			obj.dispose();
		worldOutdoorLights.clear();
		
		for(CWorldObject obj : worldRoad)
			obj.dispose();
		worldRoad.clear();
		
		for(CWorldObject obj : worldRoomObjects)
			obj.dispose();
		worldRoomObjects.clear();
		
		for(CWorldObject obj : worldTempRoomObjects)
			obj.dispose();
		worldTempRoomObjects.clear();
		
		tempHumansDead.clear();
		worldAddressList.clear();
		cloneAddressList.clear();
		worldCompanyList.clear();
				
		infoEvents.clear();
		
		townStatistics.clear();
		
		gameResourceConfig=null;
		town=null;
		
		
		
		if(shapeRenderer1!=null)
		{
			shapeRenderer1.dispose();
			shapeRenderer1=null;
		}
		if(shapeRenderer2!=null)
		{
			shapeRenderer2.dispose();
			shapeRenderer2=null;
		}
		if(worldSpriteBatch!=null)
		{
			worldSpriteBatch.dispose();
			worldSpriteBatch=null;
		}
		
		
		//reset variables
		//?
	}
	
	//INIT<--------------------------------------------------
	
	
	//SAVE/LOAD--------------------------------------------->
	private void writeString(BufferedWriter bw, String svalue) throws IOException
	{
		String newstring="";
		
		if(!town.bEncodeSavegames)
		{
			bw.write(svalue);
		}
		else
		{
			for(int i=0;i<svalue.length();i++)
			{
				char c1 = svalue.charAt(i);
				if(c1==255)
					c1=0;
				else 
					c1++;
				newstring+=c1;
			}
			
			bw.write(newstring);
		}
	}
	
	private String readString(String svalue)
	{
		String newstring="";
		
		if(!town.bEncodeSavegames)
		{
			return svalue;
		}
		
		for(int i=0;i<svalue.length();i++)
		{
			char c1 = svalue.charAt(i);
			if(c1==0)
				c1=255;
			else
				c1--;
			newstring+=c1;
		}
		
		return newstring;
	}
	
	private void writeStatisticsData(BufferedWriter bw) throws IOException
	{
		
		String ls = System.getProperty("line.separator");
		
		for(CStatisticsData_Finance fi : townStatistics.statisticsData_Finance)
		{
			writeString(bw, "STATISTICS_FINANCE" + ";" + fi.day + ";" + fi.buyAddress + ";" + fi.buyObject + ";" + fi.buyResident + ";" + fi.childsupport + ";" + fi.deceased + ";" + fi.education + ";" + fi.happinessplus +";" + fi.happinessminus+ ";" + fi.residentEarnsMoney + ";" + fi.residentSpendsMoney + ";" + fi.sellAddress + ";" + fi.sellObject + ";" + fi.sum);
			bw.write(ls);
		}
		
		for(CStatisticsData_Population po : townStatistics.statisticsData_Population)
		{
			writeString(bw, "STATISTICS_POPULATION" + ";" + po.day + ";" + po.ageAVG + ";" + po.ageMax + ";" + po.ageMin + ";" + po.cleanAVG + ";" + po.count0To20 + ";" + po.count101ToX + ";" + po.count21To40 + ";" + po.count41To60 + ";" + po.count61To80 + ";" + po.count81To100 + ";" + po.countAll + ";"  + po.countHomeless + ";" + po.countMen + ";" + po.countWomen + ";" + po.eatAVG + ";" + po.educationAVG + ";" + po.educationMax + ";" + po.educationMin + ";" + po.fitnessAVG + ";" + po.fitnessMax + ";" + po.fitnessMin + ";" + po.happinessAVG + ";" + po.happinessMax + ";" + po.happinessMin + ";" + po.healthAttitudeAVG + ";" + po.healthAttitudeMax + ";" + po.healthAttitudeMin + ";" + po.healthAVG + ";" + po.healthMax + ";" + po.healthMin + ";" + po.intelligenceAVG + ";" + po.intelligenceMax + ";" + po.intelligenceMin + ";" + po.positiveAttitudeAVG + ";" + po.positiveAttitudeMax + ";" + po.positiveAttitudeMin + ";" + po.sleepAVG + ";" + po.toiletAVG + ";" + po.workoutputAVG + ";" + po.workoutputMax + ";" + po.workoutputMin);
			bw.write(ls);
		}
		
		for(CStatisticsData_Other ot : townStatistics.statisticsData_Other)
		{
			writeString(bw, "STATISTICS_OTHER" + ";" + ot.day + ";" + ot.countAddressAll + ";" + ot.countAddressPublic + ";" + ot.countAddressResidential + ";" + ot.countCompanies + ";" + ot.countFootpath + ";" + ot.countGarage + ";" + ot.countParkingspace + ";" + ot.countRoad + ";" + ot.countVehiclesPrivate + ";" + ot.countVehiclesPublic + ";" + ot.countWorkerAll + ";" + ot.countWorkplacesAll + ";" + ot.energyConsumption + ";" + ot.energyOutput + ";" + ot.waterConsumption + ";" + ot.waterOutput);
			bw.write(ls);
		}
			
//		public ArrayList<CStatisticsData_Finance> statisticsData_Finance;
//		public ArrayList<CStatisticsData_Population> statisticsData_Population;
//		public ArrayList<CStatisticsData_Other> statisticsData_Other;		
	}
	
	private void writeWorldObjectToFile(BufferedWriter bw, CWorldObject wobj) throws IOException
	{
		String ls = System.getProperty("line.separator");
		
		if(wobj.actionstring1.isEmpty())
			wobj.actionstring1="-";
		if(wobj.actionstring2.isEmpty())
			wobj.actionstring2="-";
		if(wobj.actionstring3.isEmpty())
			wobj.actionstring3="-";
		
		int occ=0;
		int occ2=0;
		int occ3=0;
		int occ4=0;
		int occ5=0;
		int occ6=0;
		int occ7=0;
		int occ8=0;
		int occ9=0;
		int occext=0;
		
		if(wobj.isOccupiedBy!=null)
			occ=wobj.isOccupiedBy.uniqueId;
		if(wobj.isOccupiedBy2!=null)
			occ2=wobj.isOccupiedBy2.uniqueId;
		if(wobj.isOccupiedBy3!=null)
			occ3=wobj.isOccupiedBy3.uniqueId;
		if(wobj.isOccupiedBy4!=null)
			occ4=wobj.isOccupiedBy4.uniqueId;
		if(wobj.isOccupiedBy5!=null)
			occ5=wobj.isOccupiedBy5.uniqueId;
		if(wobj.isOccupiedBy6!=null)
			occ6=wobj.isOccupiedBy6.uniqueId;
		if(wobj.isOccupiedBy7!=null)
			occ7=wobj.isOccupiedBy7.uniqueId;
		if(wobj.isOccupiedBy8!=null)
			occ8=wobj.isOccupiedBy8.uniqueId;
		if(wobj.isOccupiedBy9!=null)
			occ9=wobj.isOccupiedBy9.uniqueId;
		if(wobj.isOccupiedByExtern!=null)
			occext=wobj.isOccupiedByExtern.uniqueId;
		
		String researchObj="0";
		if(wobj.researchObject!=null)
			researchObj=wobj.researchObject.objectId;
		
		int adrid=0;
		if(wobj.theaddress!=null)
			adrid=wobj.theaddress.addressId;
		
		int adrid2=0;
		if(wobj.theaddress2!=null)
			adrid2=wobj.theaddress2.addressId;
		
		int roomid=0;
		if(wobj.theroom!=null)
			roomid=wobj.theroom.uniqueId;
		
		if(wobj.isTaskObject() && !wobj.isCompanyObject()) //company task objects als companyobject speichern
		{
			int wid=0;
			if(wobj.worker!=null)
				wid=wobj.worker.uniqueId;					
			
			int wid2=0;
			if(wobj.worker2!=null)
				wid2=wobj.worker2.uniqueId;					
			
			//Task-Object
			writeString(bw, "TASKOBJECT" + ";" + wobj.uniqueId + ";" + wobj.theobject.objectId + ";" + adrid + ";" + wobj.pos_x() + ";" + wobj.pos_y() + ";" + (int)wobj.rotation() + ";");
			writeString(bw, wid+";"+wid2+";");
			writeString(bw, wobj.objectFilling+";"+wobj.doObjectAction+";" + occ + ";" + occ2 + ";" + occ3 + ";" + occ4 + ";" + occ5 + ";"+ occ6 + ";"+ occ7 + ";"+ occ8 + ";"+ occ9 + ";"+ occext + ";" + wobj.x_temp + ";" + wobj.y_temp + ";" + (int)wobj.rotation_temp + ";" + wobj.movementX+ ";");
			writeString(bw, wobj.tempcount+";"+wobj.actionvar1+";" + wobj.actionvar2 + ";" + wobj.actionvar3 + ";" + wobj.actionvar4 + ";" + wobj.actionvar5 + ";" 
					+ wobj.actionvar6 + ";"+ wobj.actionvar7 + ";"+ wobj.actionvar8 + ";"+ wobj.actionvar9 + ";" + wobj.actionstring1 + ";" + wobj.actionstring2 + ";" 
					+ wobj.actionstring3 + ";"+wobj.actionanim1+";"+wobj.actionanim2 + ";" + wobj.actionfield1 + ";" + wobj.actionfield2 + ";" + wobj.actionfield3 + ";");
			
			int oid = 0;
			if(wobj.owner!=null)
				oid=wobj.owner.uniqueId;
			int o2id = 0;
			if(wobj.owner2!=null)
				o2id=wobj.owner2.uniqueId;
			int o3id = 0;
			if(wobj.owner3!=null)
				o3id=wobj.owner3.uniqueId;
			int o4id = 0;
			if(wobj.owner4!=null)
				o4id=wobj.owner4.uniqueId;
			int o5id = 0;
			if(wobj.owner5!=null)
				o5id=wobj.owner5.uniqueId;
			int o6id = 0;
			if(wobj.owner6!=null)
				o6id=wobj.owner6.uniqueId;
			int o7id = 0;
			if(wobj.owner7!=null)
				o7id=wobj.owner7.uniqueId;
			int o8id = 0;
			if(wobj.owner8!=null)
				o6id=wobj.owner8.uniqueId;
			
			writeString(bw, oid + ";" + o2id + ";" + o3id + ";" + o4id + ";" + o5id + ";" + o6id+ ";" + o7id+ ";" + o8id);
			writeString(bw, ";" + wobj.actiontime1 + ";"+ wobj.actiontime2 + ";" + wobj.actiontime3 + ";" + wobj.actiontimenr + ";" + wobj.actiontime1check + ";"+ wobj.actiontime2check + ";" + wobj.actiontime3check);
			writeString(bw, ";" + researchObj);
			writeString(bw, ";" + wobj.iOccupied1_Arrived + ";" + wobj.iOccupiedExtern_Arrived);
			
			//if(wobj.theobject.editoraction.contains("fridge"))
			//	Gdx.app.debug("", ""+wobj.getObjectFillingMultiSaveString());
			
			writeString(bw, ";" + wobj.getObjectFillingMultiSaveString());
			writeString(bw, ";" + roomid);
			writeString(bw, ";" + wobj.color1.r+";"+ wobj.color1.g+";"+wobj.color1.b+";"+wobj.color1.a+";"+wobj.shadowDistance+";"+wobj.movementSpeed);
			writeString(bw, ";" + wobj.dynamicwidth+";"+wobj.dynamicheight);
			if(wobj.actionColor1==null)
				writeString(bw, ";-1;-1;-1;-1");
			else
				writeString(bw, ";"+wobj.actionColor1.r+";"+ wobj.actionColor1.g+";"+wobj.actionColor1.b+";"+wobj.actionColor1.a);
			
			writeString(bw, ";" + adrid2);
			writeString(bw, ";" + wobj.dynamicprice);
			writeString(bw, ";" + wobj.iObjectIsReady);
			
			bw.write(ls);
		}
		else if(wobj.thehuman!=null)
		{
			writeString(bw, "HUMAN" + ";" + wobj.uniqueId + ";"+ wobj.theobject.objectId + ";" + adrid + ";" + wobj.pos_x() + ";" + wobj.pos_y() + ";" + (int)wobj.rotation() + ";");
			writeString(bw, wobj.thehuman.headTextureId +";"+wobj.thehuman.getName() + ";"+ wobj.thehuman.gender + ";" + wobj.thehuman.ageSeconds.intValue() + ";" + wobj.thehuman.getHealthValue() + ";" + wobj.thehuman.healthValueMax + ";" + wobj.thehuman.getHappynessValue() + ";" + wobj.thehuman.happinessValueMax + ";" + (int)wobj.thehuman.sleepValue + ";" + (int)wobj.thehuman.energyValue + ";" + (int)wobj.thehuman.eatValue + ";" + (int)wobj.thehuman.cleanValue + ";" + (int)wobj.thehuman.toiletValue + ";" + (int)wobj.thehuman.workValue + ";" + (int)wobj.thehuman.clothingValue + ";");
			writeString(bw, wobj.thehuman.getIntelligenceValue()+";" + wobj.thehuman.getFitnessValue() + ";" + wobj.thehuman.healthAttitude + ";" + wobj.thehuman.positiveAttitude + ";" + wobj.thehuman.getEducationValue()+";" + wobj.thehuman.coffeinLevel + ";");
			writeString(bw, wobj.ziel_x+";"+wobj.ziel_y+";"+CAction.getTextForActionMode(wobj.activeActionMode)+";" + CAction.getTextForActionMode(wobj.activeActionModeTemp)+";");
			writeString(bw, wobj.objectFilling+";"+wobj.doObjectAction+";" + occ + ";" + occ2 + ";" + occ3 + ";" + occ4 + ";" + occ5 + ";"+ occ6 + ";"+ occ7 + ";"+ occ8 + ";"+ occ9 + ";"+ occext + ";" +  wobj.x_temp + ";" + wobj.y_temp + ";" + (int)wobj.rotation_temp + ";" + wobj.movementX+";");
			writeString(bw, wobj.tempcount+";"+wobj.actionvar1+";" + wobj.actionvar2 + ";" + wobj.actionvar3 + ";" + wobj.actionvar4 + ";" + wobj.actionvar5 + ";" + wobj.actionvar6 + ";" + wobj.actionvar7 + ";"+ wobj.actionvar8 + ";"+ wobj.actionvar9 + ";" + wobj.actionstring1 + ";" + wobj.actionstring2 + ";" + wobj.actionstring3 + ";"+ wobj.actionanim1 +";" + wobj.actionanim2 + ";" +wobj.actionfield1 + ";" + wobj.actionfield2 + ";" + wobj.actionfield3+";");
			
			int targetid=0;
			if(wobj.goByCar_TargetObject!=null)
				targetid=wobj.goByCar_TargetObject.uniqueId;
			int tparkid=0;
			if(wobj.goByCar_TargetParkingplace!=null)
				tparkid=wobj.goByCar_TargetParkingplace.uniqueId;
			
			writeString(bw, wobj.goByCar_ActionState + ";" + wobj.goByCar_X + ";" + wobj.goByCar_Y + ";" + targetid + ";" + tparkid);
			
			writeString(bw, ";" + wobj.objectAnimSpeedModifier);
			
			writeString(bw, ";" + wobj.actionvar_fs1 + ";" + CAction.getTextForActionMode(wobj.lastActionMode) + ";" + wobj.lastTargetActionObjectType);
			
			String expString="";
			for(Object objid : wobj.thehuman.jobSkillLevel.keySet().toArray())
			{
				//;objid,exp-objid,exp;
				int id = (int)objid;
				float exp = wobj.thehuman.jobSkillLevel.get(objid).fskill;
				
				if(expString.length()>0)
					expString+="-";
				
				expString+=id+","+exp;
			}
			
			if(expString.isEmpty())
				expString=" ";
			
			writeString(bw, ";" + expString);
			writeString(bw, ";" + researchObj);
			
			writeString(bw, ";" + wobj.thehuman.sick);
			writeString(bw, ";" + wobj.thehuman.doctorHealingValue);
			writeString(bw, ";" + wobj.thehuman.sickType);
			writeString(bw, ";" + wobj.iOccupied1_Arrived+ ";" + wobj.iOccupiedExtern_Arrived);
			writeString(bw, ";" + wobj.thehuman.clothingColor_Top.r + ";" + wobj.thehuman.clothingColor_Top.g + ";" + wobj.thehuman.clothingColor_Top.b);
			writeString(bw, ";" + wobj.thehuman.clothingColor_Bottom.r + ";" + wobj.thehuman.clothingColor_Bottom.g + ";" + wobj.thehuman.clothingColor_Bottom.b);
			writeString(bw, ";" + wobj.thehuman.clothingColor_Shoes.r + ";" + wobj.thehuman.clothingColor_Shoes.g + ";" + wobj.thehuman.clothingColor_Shoes.b);
			writeString(bw, ";" + wobj.color1.r+";"+ wobj.color1.g+";"+wobj.color1.b+";"+wobj.color1.a+";"+wobj.shadowDistance+";"+wobj.movementSpeed);
			writeString(bw, ";" + wobj.dynamicwidth+";"+wobj.dynamicheight);
			if(wobj.actionColor1==null	)
				writeString(bw, ";-1;-1;-1;-1");
			else
				writeString(bw, ";"+wobj.actionColor1.r+";"+ wobj.actionColor1.g+";"+wobj.actionColor1.b+";"+wobj.actionColor1.a);
			
			writeString(bw, ";"+wobj.thehuman.fruitLevel);
			writeString(bw, ";"+adrid2);
			writeString(bw, ";"+wobj.thehuman.getDemandSaveString());
			writeString(bw, ";" + wobj.dynamicprice);
			writeString(bw, ";" + wobj.iZombie);
			writeString(bw, ";" + wobj.timeInTown);
			
			writeString(bw, ";" + wobj.thehuman.abilitySpaceshipTechnology);
						
									
			bw.write(ls);
			
			
			
			//Action
			//[ACTIONMODE];[WORLDOBJECTID];[DOACTIONMODE];[GOTOACTIONMODE];[TARGETACTIONOBJECTID];[TARGETACTIONOBJECT2ID];[TARGETACTIONOBJECT3ID];[TARGETACTIONOBJECT4ID];[TARGETACTIONOBJECT5ID]
			//	[ACTIONSTATE];[ACTIONREPEATER];[TARGETCOMPANYID];[ACTIONDURATION]
			if(wobj.activeActionTemp!=null)
				writeActionObject(bw, wobj.activeActionTemp);
			
			if(wobj.activeAction!=null)
				writeActionObject(bw, wobj.activeAction);
		}
		else if(wobj.theobject.isRoomObject)
		{
			writeString(bw, "ROOMOBJECT" + ";" + wobj.uniqueId + ";"+ wobj.theobject.objectId + ";"+adrid+";" + wobj.pos_x() + ";" + wobj.pos_y() + ";" + (int)wobj.rotation() + ";");
			writeString(bw, wobj.objectFilling+";"+wobj.doObjectAction+";" + occ + ";" + occ2 + ";" + occ3 + ";" + occ4 + ";" + occ5 + ";"+ occ6 + ";"+ occ7 + ";"+ occ8 + ";"+ occ9 + ";"+ occext + ";" + wobj.x_temp + ";" + wobj.y_temp + ";" + (int)wobj.rotation_temp + ";" + wobj.movementX);
			writeString(bw, ";" + researchObj);
			writeString(bw, ";" + wobj.iOccupied1_Arrived+ ";" + wobj.iOccupiedExtern_Arrived);
			writeString(bw, ";"+wobj.color1.r+";"+ wobj.color1.g+";"+wobj.color1.b+";"+wobj.color1.a+";"+wobj.shadowDistance);
			writeString(bw, ";"+wobj.dynamicwidth+";"+wobj.dynamicheight);
			//Gdx.app.debug("", ""+wobj.dynamicwidth + ", " + wobj.uniqueId);
			
			if(wobj.actionColor1==null)
				writeString(bw, ";-1;-1;-1;-1");
			else
				writeString(bw, ";"+wobj.actionColor1.r+";"+ wobj.actionColor1.g+";"+wobj.actionColor1.b+";"+wobj.actionColor1.a);
			
			writeString(bw, ";"+adrid2);
			writeString(bw, ";" + wobj.dynamicprice);

			if(wobj.belongsToCompany!=null)
				writeString(bw, ";" + wobj.belongsToCompany.companyId);
			else
				writeString(bw, ";" + "-");
			
			writeString(bw, ";" + wobj.iObjectIsReady);
			
			bw.write(ls);
		}
		else if(wobj.isCompanyObject() && !wobj.theobject.isRoomObject)
		{
			//Company WorldObject
			int cid=0;
			if(wobj.belongsToCompany!=null)
				cid=wobj.belongsToCompany.companyId;
			
			int wid=0;
			if(wobj.worker!=null)
				wid=wobj.worker.uniqueId;
			
			int wid2=0;
			if(wobj.worker2!=null)
				wid2=wobj.worker2.uniqueId;					
						
			writeString(bw, "COMPANYOBJECT" + ";" + wobj.uniqueId + ";" + wobj.theobject.objectId  + ";" + adrid + ";" + wobj.pos_x() + ";" + wobj.pos_y() + ";" + (int)wobj.rotation() + ";");
			writeString(bw, cid + ";" + wid + ";" + wid2 + ";" + wobj.workTime1_From + ";" + wobj.workTime1_To+";"+wobj.workTime2_From + ";" + wobj.workTime2_To+";");
			writeString(bw, wobj.objectFilling+";"+wobj.doObjectAction+";" + occ + ";" + occ2 + ";" + occ3 + ";" + occ4 + ";" + occ5 + ";"+ occ6 + ";"+ occ7 + ";"+ occ8 + ";"+ occ9 + ";"+ occext + ";" + wobj.x_temp + ";" + wobj.y_temp + ";" + (int)wobj.rotation_temp + ";" + wobj.movementX+ ";" + wobj.objectCondition + ";");
			writeString(bw, wobj.tempcount+";"+wobj.actionvar1+";" + wobj.actionvar2 + ";" + wobj.actionvar3 + ";" + wobj.actionvar4 + ";" + wobj.actionvar5 + ";" + wobj.actionvar6+";"+ wobj.actionvar7 + ";"+ wobj.actionvar8 + ";"+ wobj.actionvar9 + ";" + wobj.actionstring1 + ";" + wobj.actionstring2 + ";" + wobj.actionstring3 + ";"+wobj.actionanim1 +";" + wobj.actionanim2 + ";" + wobj.actionfield1 + ";" + wobj.actionfield2 + ";" + wobj.actionfield3);
			writeString(bw, ";" + researchObj);
			writeString(bw, ";" + wobj.fuelValue);
			
			writeString(bw, ";" + wobj.iOccupied1_Arrived+ ";" + wobj.iOccupiedExtern_Arrived);
			writeString(bw, ";" + wobj.getObjectFillingMultiSaveString());
			writeString(bw, ";" + roomid);
			writeString(bw, ";"+wobj.color1.r+";"+ wobj.color1.g+";"+wobj.color1.b+";"+wobj.color1.a+";"+wobj.shadowDistance+";"+wobj.movementSpeed);
			writeString(bw, ";"+wobj.dynamicwidth+";"+wobj.dynamicheight);
			if(wobj.actionColor1==null	)
				writeString(bw, ";-1;-1;-1;-1");
			else
				writeString(bw, ";"+wobj.actionColor1.r+";"+ wobj.actionColor1.g+";"+wobj.actionColor1.b+";"+wobj.actionColor1.a);
			
			writeString(bw, ";"+adrid2);
			writeString(bw, ";" + wobj.dynamicprice);
			writeString(bw, ";" + wobj.iObjectIsReady);
			
			bw.write(ls);
		}
		else
		{
			//Base WorldObject
			writeString(bw, "WORLDOBJECT" + ";" + wobj.uniqueId + ";"+ wobj.theobject.objectId + ";" + adrid + ";" + wobj.pos_x() + ";" + wobj.pos_y() + ";" + (int)wobj.rotation()+";");
			writeString(bw, wobj.objectFilling+";"+wobj.doObjectAction+";" + occ + ";" + occ2 + ";" + occ3 + ";" + occ4 + ";" + occ5 + ";"+ occ6 + ";"+ occ7 + ";"+ occ8 + ";"+ occ9 + ";"+ occext + ";"+ wobj.x_temp + ";" + wobj.y_temp + ";" + (int)wobj.rotation_temp + ";" + wobj.movementX + ";");
			writeString(bw, wobj.tempcount+";"+wobj.actionvar1+";" + wobj.actionvar2 + ";" + wobj.actionvar3 + ";" + wobj.actionvar4 + ";" + wobj.actionvar5 + ";" + wobj.actionvar6 + ";" + wobj.actionvar7 + ";"+ wobj.actionvar8 + ";"+ wobj.actionvar9 + ";" + wobj.actionstring1 + ";" + wobj.actionstring2 + ";" + wobj.actionstring3 + ";"+ wobj.actionanim1 + ";" + wobj.actionanim2 + ";"+wobj.actionfield1 + ";" + wobj.actionfield2 + ";" + wobj.actionfield3);
			
			int oid = 0;
			if(wobj.owner!=null)
				oid=wobj.owner.uniqueId;
			int o2id = 0;
			if(wobj.owner2!=null)
				o2id=wobj.owner2.uniqueId;
			
			writeString(bw, ";"+oid+";" + o2id);
			writeString(bw, ";" + researchObj);
			writeString(bw, ";" + wobj.fuelValue);
			writeString(bw, ";" + wobj.iOccupied1_Arrived+ ";" + wobj.iOccupiedExtern_Arrived);
			writeString(bw, ";" + wobj.getObjectFillingMultiSaveString());
			writeString(bw, ";" + roomid);
			writeString(bw, ";"+wobj.color1.r+";"+ wobj.color1.g+";"+wobj.color1.b+";"+wobj.color1.a+";"+wobj.shadowDistance+";"+wobj.movementSpeed);
			writeString(bw, ";"+wobj.dynamicwidth+";"+wobj.dynamicheight);
			if(wobj.actionColor1==null	)
				writeString(bw, ";-1;-1;-1;-1");
			else
				writeString(bw, ";"+wobj.actionColor1.r+";"+ wobj.actionColor1.g+";"+wobj.actionColor1.b+";"+wobj.actionColor1.a);
			
			writeString(bw, ";"+adrid2);
			writeString(bw, ";" + wobj.dynamicprice);
			
			writeString(bw, ";" + wobj.workTime1_From_temp);
			writeString(bw, ";" + wobj.workTime1_To_temp);
			writeString(bw, ";" + wobj.iZombie);
			writeString(bw, ";" + wobj.iObjectIsReady);
						
			bw.write(ls);
		}
	}
	
	public void writeActionObject(BufferedWriter bw, CAction action) throws IOException
	{
		String ls = System.getProperty("line.separator");
		
		int uid1=0;
		int uid2=0;
		int uid3=0;
		int uid4=0;
		int uid5=0;
		int uid6=0;
		int uid7=0;
		int uid8=0;
		int uid9=0;
		int uid10=0;
		int uid11=0;
		
		if(action.targetActionObject!=null)
			uid1=action.targetActionObject.uniqueId;
		if(action.targetActionObject2!=null)
			uid2=action.targetActionObject2.uniqueId;
		if(action.targetActionObject3!=null)
			uid3=action.targetActionObject3.uniqueId;
		if(action.targetActionObject4!=null)
			uid4=action.targetActionObject4.uniqueId;
		if(action.targetActionObject5!=null)
			uid5=action.targetActionObject5.uniqueId;
		if(action.targetActionObject6!=null)
			uid6=action.targetActionObject6.uniqueId;
		if(action.targetActionObject7!=null)
			uid7=action.targetActionObject7.uniqueId;
		if(action.targetActionObject8!=null)
			uid8=action.targetActionObject8.uniqueId;
		if(action.targetActionObject9!=null)
			uid9=action.targetActionObject9.uniqueId;
		if(action.targetActionObject10!=null)
			uid10=action.targetActionObject10.uniqueId;
		if(action.targetActionObject11!=null)
			uid11=action.targetActionObject11.uniqueId;
		
		int tid=0;
		if(action.targetCompany!=null)
			tid=action.targetCompany.companyId;
		
		String ts1=action.actionTemp_String1;
		String ts2=action.actionTemp_String2;
		if(ts1.isEmpty())
			ts1=" ";
		if(ts2.isEmpty())
			ts2=" ";
			
		writeString(bw, "ACTION" + ";" + CAction.getTextForActionMode(action.actionMode) +";" 
				+ CAction.getTextForValueType(action.valueType)+ ";" + action.baseWorldObject.uniqueId + ";" + action.bActionMode 
				+ ";" + action.bGotoActionMode + ";" +uid1 + ";" + uid2 + ";" + uid3+ ";" + uid4 + ";" + uid5 + ";" + uid6 + ";"+ uid7 + ";"+ uid8 + ";"+ uid9 + ";"+ uid10 + ";"+ uid11 + ";"
				+ action.actionState + ";" + action.actionRepeater + ";" + tid + ";" 
				+ action.actionDuration + ";" + action.attributeTimer + ";" +action.deltaTimer + ";" 
				+ action.deltaTimer2 + ";" + action.deltaTimer3 + ";"
				+ action.hourTimer + ";"
				+ action.actionTemp_Float1 + ";" + action.actionTemp_Float2 + ";" + action.actionTemp_Float3 + ";"
				+ action.actionTemp_Float4 + ";" + action.actionTemp_Float5 + ";"
				+ ts1 + ";" + ts2
				);
		
		bw.write(ls);
	}
	
	public void saveToFile(String sfilename, String deletef)
	{
		try
		{
			sfilename=sfilename.replace(" ", "_");
			Date dt = new Date();
			sfilename=sfilename+"_"+town.gameMode+"_"+dt.getTime();
			
			if(!deletef.isEmpty())
			{
				//FileHandle filedelete = Gdx.files.local("data/save/"+deletef+".town");
				FileHandle filedelete = CHelper.getFileHandle("appdata/local/HTP/data/save/"+deletef+".town");
				filedelete.delete();
			}
			
			//FileHandle file = Gdx.files.local("data/save/"+sfilename+".town");
			FileHandle file = CHelper.getFileHandle("appdata/local/HTP/data/save/"+sfilename+".town");
			file.delete();
			saveToFile(sfilename, file);
			
			//Write Savegame History
			//FileHandle file2 = Gdx.files.local("data/savehistory/"+sfilename+"_"+dt.getTime()+".town");
			FileHandle file2 = CHelper.getFileHandle("appdata/local/HTP/data/savehistory/"+sfilename+"_"+dt.getTime()+".town");
			file2.delete();
			saveToFile(sfilename, file2);
		}
		catch(Exception e)
		{
			CHelper.writeError(e.getMessage(), e.getStackTrace(), e);
		}		
	}
	
	public String loadRealEstateObjectReadPrice(String filename)
	{
		try
		{
			FileHandle file = null;
			
			String p1 = "appdata/local/HTP/data/realestate/"+filename+".csv";
			String p2 = "realestate/"+filename+".csv";
			if(Gdx.files.external(p1).exists())
				file = CHelper.getFileHandle(p1);
			else if(Gdx.files.internal(p2).exists())
				file = Gdx.files.internal(p2);
			
			BufferedReader reader = new BufferedReader(file.reader());
			String line = reader.readLine();
			line = readString(line);
			reader.close();
			return line;
		}
		catch(Exception e){}
		
		return "";
	}
	
	public CAddress loadRealEstateObject(String filename)
	{
		CAddress tempAddress=null;
		
		try
		{
			FileHandle file = null;
			
			String p1 = "appdata/local/HTP/data/realestate/"+filename+".csv";
			String p2 = "realestate/"+filename+".csv";
			if(Gdx.files.external(p1).exists())
				file = CHelper.getFileHandle(p1);
			else if(Gdx.files.internal(p2).exists())
				file = Gdx.files.internal(p2);
			
			BufferedReader reader = new BufferedReader(file.reader());
			String line = reader.readLine();
			
			int count=0;
			while(line != null) 
			{
				count++;
				try
				{
					line = readString(line);
					
					if(count==1 && line.contains("HEADER"))
					{
						//preis
						continue;
					}
					
					String[] splitstring = line.split(";");
					String type = splitstring[0];
					
					Object o1 = loadType(type, splitstring, false);
					
					int newAdrId=CAddress.getNewAddressId(town);
					
					if(o1!=null)
					{
						if(o1.getClass().getName().contains("CAddress"))
						{
							tempAddress = (CAddress)o1;
							tempAddress.addressId=newAdrId;
							tempAddress.addressName=tempAddress.addressId + " Main Street";
						}
						if(o1.getClass().getName().contains("CWorldObject"))
						{
							CWorldObject temp1 = (CWorldObject)o1;
							CWorldObject tempw = new CWorldObject(temp1.theobject, town, true);
							tempw.setPosition(temp1.pos_x(), temp1.pos_y());
							tempw.setRotation(temp1.rotation());
							tempw.width=temp1.width;
							tempw.height=temp1.height;
							tempw.dynamicwidth=temp1.dynamicwidth;
							tempw.dynamicheight=temp1.dynamicheight;
							tempw.dynamicprice = temp1.dynamicprice;
							tempw.color1 = new Color(temp1.color1);
							tempw.workTime1_From = temp1.workTime1_From;
							tempw.workTime1_To = temp1.workTime1_To;
							tempw.workTime2_From = temp1.workTime2_From;
							tempw.workTime2_To = temp1.workTime2_To;
							tempw.actiontime1=temp1.actiontime1;
							tempw.actiontime2=temp1.actiontime2;
							tempw.actiontime3=temp1.actiontime3;
							
							if(!tempw.isHuman())
							{
								//((CWorldObject)o1).theaddressid=newAdrId;
								//((CWorldObject)o1).theaddress=tempAddress;
								//tempAddress.addWorldObject((CWorldObject)o1);
								tempw.theaddressid = newAdrId;
								tempw.theaddress = tempAddress;
								tempAddress.addWorldObject(tempw);
							}
						}
					}
				}
				catch(Exception e)
				{
					CHelper.writeError(e.getMessage(), e.getStackTrace(), e);
				}
				
				line = reader.readLine();
			}
			
			reader.close();
		}
		catch(Exception e)
		{
			CHelper.writeError(e.getMessage(), e.getStackTrace(), e);
		}
		
		return tempAddress;
	}
	
	public void loadRealEstate(String filename)
	{
		try
		{
			CAddress tempAddress=loadRealEstateObject(filename);
			
			
			if(tempAddress!=null)
			{
				town.gameGui.bAddressCloning=true;
				tempAddress.isCloning=true;
				town.gameGui.clonedAddress = tempAddress;
				town.gameGui.realEstateCloneName = filename;
				cloneAddressList.clear();
				cloneAddressList.add(town.gameGui.clonedAddress);
			}
			
		}
		catch(Exception e)
		{
			CHelper.writeError(e.getMessage(), e.getStackTrace(), e);
		}				
	}
	
	
	public void saveRealEstate(CAddress address, String name1)
	{
		try
		{
			//String filename1 = "appdata/local/HTP/data/realestate/"+name1+"_"+address.addressType + "_"+address.getCloningPrice();
			String filename1 = "appdata/local/HTP/data/realestate/"+name1;
			//if(address.getCompany()!=null)
			//	filename1+="_"+address.getCompany().getCompanyTypeLabel();
			filename1+=".csv";
			FileHandle file = CHelper.getFileHandle(filename1);
			
			String ls = System.getProperty("line.separator");
			BufferedWriter bw=null;
			bw = new BufferedWriter(file.writer(false));
			
			String comp = "residential";
			
			if(address.getCompany()!=null)
				comp=address.getCompany().getCompanyTypeLabel();
			writeString(bw, "HEADER"+";"+address.getCloningPrice() + ";" + comp);
			bw.write(ls);
			
			writeString(bw, "ADDRESS" + ";" + address.addressId + ";" + address.addressName + ";" +  address.addressType + ";" + address.sx + ";" + address.sy + ";" + address.ex + ";" + address.ey);
			bw.write(ls);
			
			if(address.getCompany()!=null)
			{
				writeString(bw, "COMPANY" + ";" + 0 + ";" + address.getCompany().getCompanyTypeLabel() + ";" +  address.getCompany().getCompanyTypeString() + ";"+ 0 + ";" + 0 + ";" + 0 + ";" + 0 + ";" + 0 + ";" + 0);
				bw.write(ls);
			}
			
			for(CWorldObject item1 : address.listWorldObjects)
				if(!item1.isHuman() && !item1.theobject.editoraction.contains("garbagebag"))
					writeWorldObjectToFile(bw, item1);

			for(CWorldObject item1 : address.listWorldObjects_Floors)
				writeWorldObjectToFile(bw, item1);

			for(CWorldObject item1 : address.listWorldObjects_Ground)
				writeWorldObjectToFile(bw, item1);
			
			bw.close();
		}
		catch(Exception e)
		{
			CHelper.writeError(e.getMessage(), e.getStackTrace(), e);
		}
	}
	
	public void saveToFile(String sfilename, FileHandle file)
	{
		try
		{
			//Alt
			
			//[OBJECTID];[POSX];[POSY];[ROTATION];[HEAD_TEXTURE_FILENAME];[NAME];[GENDER];[AGE];[ADDRESS_ID]
			//[A];[ADDRESS_ID];[ADDRESS_NAME];[SX];[SY];[EX];[EY]
			
			//Neu
			
			//Base WorldObject
			//[TYPE=WORLDOBJECT];[UNIQUE_ID];[OBJECT_ID];[ADDRESS_ID];[POSX];[POSY];[ROTATION];
			//	[FOODFILLING];[DOOBJECTACTION];[ISOCCUPIED];[ISOCCUPIEDBYEXTERN];[XTEMP];[YTEMP];[ROTATIONTEMP];[MOVEMENTX]
			//	[tempcount];[actionvar1];[actionvar2];[actionvar3];[actionvar4];[actionvar5];[actionvar6]
			//  [OWNER_ID];[OWNER2_ID]
			
			//Room-Object
			//[TYPE=ROOMOBJECT];[UNIQUE_ID];[OBJECT_ID];[ADDRESS_ID];[POSX];[POSY];[ROTATION];
			//	[FOODFILLING];[DOOBJECTACTION];[ISOCCUPIED];[ISOCCUPIEDBYEXTERN];[XTEMP];[YTEMP];[ROTATIONTEMP];[MOVEMENTX]
			
			//Task-Object
			//[TYPE=TASKOBJECT];[UNIQUE_ID];[OBJECT_ID];[ADDRESS_ID];[POSX];[POSY];[ROTATION];
			//[RESPONSIBLE_ID]
			//	[FOODFILLING];[DOOBJECTACTION];[ISOCCUPIED];[ISOCCUPIEDBYEXTERN];[XTEMP];[YTEMP];[ROTATIONTEMP];[MOVEMENTX]
			//	[tempcount];[actionvar1];[actionvar2];[actionvar3];[actionvar4];[actionvar5];[actionvar6]
			//	[OWNER_ID];[OWNER2_ID];[OWNER3_ID];[OWNER4_ID];[OWNER5_ID];[OWNER6_ID]
			//  [actiontime1];[actiontime2];[actiontime3];
			
			//Company WorldObject
			//[TYPE=COMPANYOBJECT];[UNIQUE_ID];[OBJECT_ID];[ADDRESS_ID];[POSX];[POSY];[ROTATION];
			//	[COMPANY_ID];[WORKER_ID];[WORKTIME_FROM];[WORKTIME_TO];
			//	[FOODFILLING];[DOOBJECTACTION];[ISOCCUPIED];[ISOCCUPIEDBYEXTERN];[XTEMP];[YTEMP];[ROTATIONTEMP];[MOVEMENTX];[OBJECTCONDITION]
			//	[tempcount];[actionvar1];[actionvar2];[actionvar3];[actionvar4];[actionvar5];[actionvar6]
			
			//Human WorldObject
			//[TYPE=HUMAN];[UNIQUE_ID];[OBJECT_ID];[ADDRESS_ID];[POSX];[POSY];[ROTATION];
			//	[WORKPLACE_ID];[HEAD_TEXTURE_FILENAME];[NAME];[GENDER];[AGE]; ...
			//	...[HEALTH_VALUE];[HEALTH_VALUE_MAX];[HAPPYNESS_VALUE];[HAPPYNESS_VALUE_MAX];[SLEEP_VALUE];[EAT_VALUE];[CLEAN_VALUE];[TOILET_VALUE];[WORK_VALUE]
			//	...[intelligenceValue];[fitnessValue];[healthAttitude];[positiveAttitude];[educationValue]
			//  ...[ZIEL_X];[ZIEL_Y];[ACTIVEACTIONMODE]
			//	[FOODFILLING];[DOOBJECTACTION];[ISOCCUPIED];[ISOCCUPIEDBYEXTERN];[XTEMP];[YTEMP];[ROTATIONTEMP];[MOVEMENTX]
			
			//Address
			//[TYPE=ADDRESS];[ADDRESS_ID];[ADDRESS_NAME];[ADDRESS_TYPE];[SX];[SY];[EX];[EY]
			
			//Company
			//[TYPE=COMPANY];[COMPANY_ID];[COMPANYNAME];[ADDRESS_ID];[WORKOUTPUT]
			
			//Action
			//[ACTIONMODE];[WORLDOBJECTID];[DOACTIONMODE];[GOTOACTIONMODE];[TARGETACTIONOBJECTID];[TARGETACTIONOBJECT2ID];[TARGETACTIONOBJECT3ID];[TARGETACTIONOBJECT4ID];[TARGETACTIONOBJECT5ID]
			//	[ACTIONSTATE];[ACTIONREPEATER];[TARGETCOMPANYID];[ACTIONDURATION]
			
			//AfterLoad set links:
			//	Company: set address
			//	Human: set address, set workplace: workplace1, workplace2 -> 1 ist company, 2 ist foodfilling usw
			//	Companyobject: set address, set company, set worker 
			//	Task-Object: responsible
			
			//AfterLoad fill lists:
			//	World: fill companylist
			//	Company: fill companyobjectlist
			//	Address: fill objectlist
			
			String ls = System.getProperty("line.separator");
			BufferedWriter bw=null;
			bw = new BufferedWriter(file.writer(true));
			
			//			LocalDateTime dt = LocalDateTime.now();
			//			bw.write(dt.getYear() + ";" + dt.getMonthValue() + ";" + dt.getDayOfMonth() + ";" + dt.getHour() + ";" + dt.getMinute()+";" + dt.getSecond());
			//			bw.write(ls);
			
			//World Values
			int izombie=1;
			if(!town.bZombieApocalypse)
				izombie=0;
			
			int inorealestate=1;
			if(!town.bNoRealEstate)
				inorealestate=0;

			int iconstructionmode=1;
			if(!town.bConstructionMode)
				iconstructionmode=0;

			
			writeString(bw, "0.1" + ";" + String.valueOf(worldTime.getTotalSeconds()) + ";" + String.valueOf(townMoney+";"+lastHappinessFundDay) + ";" + town.gameWorld.addressArchitectPlanningValue + ";" + town.gameMode + ";" + spawnNewZombieHour+ ";" + spawnNewResidentHour + ";" + izombie + ";" + inorealestate + ";"  + iconstructionmode);
			bw.write(ls);
			
			//Statistics
			writeStatisticsData(bw);
			
			
			//Bonus/Research  
			//List<String> bonuslist = new ArrayList<String>();
			String sonetimebonus="";
			for(CObject obj : gameResourceConfig.listObject)
			{
				if(obj.iResearchCurrentWorkoutput>0)
				{
					writeString(bw, "OBJECT" + ";" + obj.objectId + ";" + obj.iResearchCurrentWorkoutput);
					bw.write(ls);
				}
				
				if(obj.iOneTimeBonus==1)
				{
					//bonuslist.add(obj.objectId);
					sonetimebonus+=obj.objectId+",";
				}
			}
			
			if(!sonetimebonus.isEmpty())
			{
				//Gdx.app.debug("", "test onetimebonus save: sonetimebonus: " + sonetimebonus + ", sonetimebonus.length: " + sonetimebonus.length() + ", sonetimebonus.substring(0, sonetimebonus.length()-2): " + sonetimebonus.substring(0, sonetimebonus.length()-2));
				sonetimebonus=sonetimebonus.substring(0, sonetimebonus.length()-1);
				writeString(bw, "BONUSOBJECTS" + ";" + sonetimebonus);
				bw.write(ls);
			}
			
			if(town.gameGui.unlockedCompanyTypes.size()>0)
			{
				String cstring="UNLOCKEDCOMPANIES;";
				for(String s1 : town.gameGui.unlockedCompanyTypes)
				{
					cstring+=s1+",";
				}
				writeString(bw, cstring);
				bw.write(ls);
			}
			
			
			//World Objects
			for(CWorldObject wobj : worldObjects)
				writeWorldObjectToFile(bw, wobj);
			
			for(CWorldObject wobj : worldDrawSpecial)
				writeWorldObjectToFile(bw, wobj);

			for(CWorldObject wobj : worldDefenseWarning)
				writeWorldObjectToFile(bw, wobj);

			for(CWorldObject wobj : worldWatersystems)
				writeWorldObjectToFile(bw, wobj);
			
			for(CWorldObject wobj : worldCars)
				writeWorldObjectToFile(bw, wobj);			
			
			for(CWorldObject wobj : worldDrawSpecial2)
				writeWorldObjectToFile(bw, wobj);
			
			for(CWorldObject wobj : worldCarpets)
				writeWorldObjectToFile(bw, wobj);			
			
			for(CWorldObject wobj : worldGarbageContainers)
				writeWorldObjectToFile(bw, wobj);
			
			for(CWorldObject wobj : worldGroundObjects)
				writeWorldObjectToFile(bw, wobj);
			
			//for(CWorldObject wobj : worldWaterObjects) //Water ist auch in groundliste
			//	writeWorldObjectToFile(bw, wobj);
			
			for(CWorldObject wobj : worldFootpath)
				writeWorldObjectToFile(bw, wobj);
			
			for(CWorldObject wobj : worldOutdoorLights)
				writeWorldObjectToFile(bw, wobj);
			
			for(CWorldObject wobj : worldCoverlights)
				writeWorldObjectToFile(bw, wobj);
			
			for(CWorldObject wobj : worldRoad)
				writeWorldObjectToFile(bw, wobj);
			
			for(CWorldObject wobj : worldRoomObjects)
				writeWorldObjectToFile(bw, wobj);
			
			for(CWorldObject wobj : worldHumans)
				writeWorldObjectToFile(bw, wobj);

			for(CWorldObject wobj : worldBirds)
				writeWorldObjectToFile(bw, wobj);

			for(CWorldObject wobj : worldZombies)
				writeWorldObjectToFile(bw, wobj);
			
			for(CWorldObject wobj : worldZombieEntrances)
				writeWorldObjectToFile(bw, wobj);
			
			
			for(CAddress adr : worldAddressList)
			{
				writeString(bw, "ADDRESS" + ";" + adr.addressId + ";" + adr.addressName + ";" +  adr.addressType + ";" + (int)adr.sx + ";" + (int)adr.sy + ";" + (int)adr.ex + ";" + (int)adr.ey);
				bw.write(ls);
			}		
			
			//Company
			for(CCompany comp : worldCompanyList)
			{
				int adrid1=0;
				if(comp.address_company!=null)
					adrid1=comp.address_company.addressId;
				String ctype="";
				
				writeString(bw, "COMPANY" + ";" + comp.companyId + ";" + comp.companyname + ";" +  comp.getCompanyTypeString() + ";"+ adrid1 + ";" + comp.getWorkOutput(WorkoutputType.DEFAULT) + ";" + comp.getWorkOutput(WorkoutputType.FINANCE) + ";" + comp.getWorkOutput(WorkoutputType.POPULATION) + ";" + comp.getWorkOutput(WorkoutputType.OTHER) + ";" + comp.getWorkOutput(WorkoutputType.INTELLIGENCE));
				bw.write(ls);
			}
			
			bw.close();
		}
		catch(Exception e)
		{
			//obj.textureIcon = new Texture(Gdx.files.internal("fliese2.jpg"));
			//e.printStackTrace();
			CHelper.writeError(e.getMessage(), e.getStackTrace(), e);
		}		
	}
	
	public Object loadType(String type, String[] splitstring, Boolean addToList)
	{
		//if(type.equals("WORLDOBJECT") || type.equals("TASKOBJECT")|| type.equals("COMPANYOBJECT") || type.equals("HUMAN")|| type.equals("ROOMOBJECT")) //HUMAN, WORLDOBJECT, TASKOBJECT
		//if(type.equals("ROOMOBJECT")) 
		//if(type.contains("STATIST"))
		//	return null;
		
		//Statistics
		if(type.equals("STATISTICS_FINANCE") || type.equals("STATISTICS_POPULATION") || type.equals("STATISTICS_OTHER"))
		{
			int day = Integer.parseInt(splitstring[1]);
			
			if(type.equals("STATISTICS_FINANCE"))
			{
				CStatisticsData_Finance fi = new CStatisticsData_Finance();
				fi.day = day;
				fi.buyAddress = Integer.parseInt(splitstring[2]);
				fi.buyObject = Integer.parseInt(splitstring[3]);
				fi.buyResident = Integer.parseInt(splitstring[4]);
				fi.childsupport = Integer.parseInt(splitstring[5]);
				fi.deceased = Integer.parseInt(splitstring[6]);
				fi.education = Integer.parseInt(splitstring[7]);
				fi.happinessplus = Integer.parseInt(splitstring[8]);
				fi.happinessminus = Integer.parseInt(splitstring[9]);
				fi.residentEarnsMoney = Integer.parseInt(splitstring[10]);
				fi.residentSpendsMoney = Integer.parseInt(splitstring[11]);
				fi.sellAddress = Integer.parseInt(splitstring[12]);
				fi.sellObject = Integer.parseInt(splitstring[13]);
				fi.sum = Integer.parseInt(splitstring[14]);
				if(addToList)
					townStatistics.statisticsData_Finance.add(fi);
			}
			
			if(type.equals("STATISTICS_POPULATION"))
			{
				CStatisticsData_Population po = new CStatisticsData_Population();
				po.day = day;
				po.ageAVG=Integer.parseInt(splitstring[2]);
				po.ageMax=Integer.parseInt(splitstring[3]);
				po.ageMin=Integer.parseInt(splitstring[4]);
				po.cleanAVG=Integer.parseInt(splitstring[5]);
				po.count0To20=Integer.parseInt(splitstring[6]);
				po.count101ToX=Integer.parseInt(splitstring[7]);
				po.count21To40=Integer.parseInt(splitstring[8]);
				po.count41To60=Integer.parseInt(splitstring[9]);
				po.count61To80=Integer.parseInt(splitstring[10]);
				po.count81To100=Integer.parseInt(splitstring[11]);
				po.countAll=Integer.parseInt(splitstring[12]);
				po.countHomeless=Integer.parseInt(splitstring[13]);
				po.countMen=Integer.parseInt(splitstring[14]);
				po.countWomen=Integer.parseInt(splitstring[15]);
				po.eatAVG=Integer.parseInt(splitstring[16]);
				po.educationAVG=Float.parseFloat(splitstring[17]);
				po.educationMax=Float.parseFloat(splitstring[18]);
				po.educationMin=Float.parseFloat(splitstring[19]);
				po.fitnessAVG=Integer.parseInt(splitstring[20]);
				po.fitnessMax=Integer.parseInt(splitstring[21]);
				po.fitnessMin=Integer.parseInt(splitstring[22]);
				po.happinessAVG=Integer.parseInt(splitstring[23]);
				po.happinessMax=Integer.parseInt(splitstring[24]);
				po.happinessMin=Integer.parseInt(splitstring[25]);
				po.healthAttitudeAVG=Float.parseFloat(splitstring[26]);
				po.healthAttitudeMax=Float.parseFloat(splitstring[27]);
				po.healthAttitudeMin=Float.parseFloat(splitstring[28]);
				po.healthAVG=Integer.parseInt(splitstring[29]);
				po.healthMax=Integer.parseInt(splitstring[30]);
				po.healthMin=Integer.parseInt(splitstring[31]);
				po.intelligenceAVG=Integer.parseInt(splitstring[32]);
				po.intelligenceMax=Integer.parseInt(splitstring[33]);
				po.intelligenceMin=Integer.parseInt(splitstring[34]);
				po.positiveAttitudeAVG=Float.parseFloat(splitstring[35]);
				po.positiveAttitudeMax=Float.parseFloat(splitstring[36]);
				po.positiveAttitudeMin=Float.parseFloat(splitstring[37]);
				po.sleepAVG=Integer.parseInt(splitstring[38]);
				po.toiletAVG=Integer.parseInt(splitstring[39]);
				po.workoutputAVG=Integer.parseInt(splitstring[40]);
				po.workoutputMax=Integer.parseInt(splitstring[41]);
				po.workoutputMin=Integer.parseInt(splitstring[42]);
				if(addToList)
					townStatistics.statisticsData_Population.add(po);
			}
			
			if(type.equals("STATISTICS_OTHER"))
			{
				CStatisticsData_Other ot = new CStatisticsData_Other();
				ot.day = day;
				ot.countAddressAll=Integer.parseInt(splitstring[2]);
				ot.countAddressPublic=Integer.parseInt(splitstring[3]);
				ot.countAddressResidential=Integer.parseInt(splitstring[4]);
				ot.countCompanies=Integer.parseInt(splitstring[5]);
				ot.countFootpath=Integer.parseInt(splitstring[6]);
				ot.countGarage=Integer.parseInt(splitstring[7]);
				ot.countParkingspace=Integer.parseInt(splitstring[8]);
				ot.countRoad=Integer.parseInt(splitstring[9]);
				ot.countVehiclesPrivate=Integer.parseInt(splitstring[10]);
				ot.countVehiclesPublic=Integer.parseInt(splitstring[11]);
				ot.countWorkerAll=Integer.parseInt(splitstring[12]);
				ot.countWorkplacesAll=Integer.parseInt(splitstring[13]);
				ot.energyConsumption=Integer.parseInt(splitstring[14]);
				ot.energyOutput=Integer.parseInt(splitstring[15]);
				ot.waterConsumption=Integer.parseInt(splitstring[16]);
				ot.waterOutput=Integer.parseInt(splitstring[17]);
				if(addToList)
					townStatistics.statisticsData_Other.add(ot);
			}
		}
		
		
		//Base WorldObject
		//Task-Object
		if(type.equals("WORLDOBJECT") || type.equals("TASKOBJECT"))
		{
			String uid = splitstring[1];
			String objectid = splitstring[2];
			int iobjid = 0; 
			
			String adrid=splitstring[3];
			int posx = Integer.parseInt(splitstring[4]);
			int posy = Integer.parseInt(splitstring[5]);
			int rotation = Integer.parseInt(splitstring[6]);
			
			CObject obj=null;
			
			if(objectid.contains("_"))
			{
				//Modkürzel nicht parsen
				String ar1[] = objectid.split("_");
				iobjid = Integer.parseInt(ar1[0]);
			}
			else
			{
				iobjid = Integer.parseInt(objectid);
			}
			
			if(iobjid<001000000)
				obj = (CObject) gameResourceConfig.listObjectres.stream().filter(item->item.objectId.equals(objectid)).findFirst().get();
			else
			{
				obj = (CObject) gameResourceConfig.listObject.stream().filter(item->item.objectId.equals(objectid)).findFirst().get();
			}
			
			//Gdx.app.debug("log_4", "WORLDOBJECT/TASKOBJECT " + uid + ", " + obj.objectName);
			
			obj.pos_x = posx;
			obj.pos_y = posy;
			obj.rotation = rotation;
						
			CWorldObject tempWO = new CWorldObject(obj, this.town, true);
						
			if(!addToList) //keine temporären lichter in der pampa anzeigen (template loading)
			{
				if(tempWO.plight!=null)
					tempWO.plight.setActive(false);
				if(tempWO.clight!=null)
					tempWO.clight.setActive(false);
				if(tempWO.clight2!=null)
					tempWO.clight2.setActive(false);
				if(tempWO.dlight!=null)
					tempWO.dlight.setActive(false);
			}
			
			tempWO.theaddressid = Integer.parseInt(adrid);
			
			int itask=0;
			if(type.equals("TASKOBJECT"))
			{
				int respid=Integer.parseInt(splitstring[7]);
				tempWO.workerId=respid;
				
				int respid2=Integer.parseInt(splitstring[8]);
				tempWO.worker2Id=respid;
				
				itask=2;
			}
			
			//	[FOODFILLING];[DOOBJECTACTION];[ISOCCUPIED];[ISOCCUPIEDBYEXTERN];[XTEMP];[YTEMP];[ROTATIONTEMP];[MOVEMENTX]
			tempWO.objectFilling=Integer.parseInt(splitstring[7+itask]);
			tempWO.doObjectAction=Boolean.parseBoolean(splitstring[8+itask]);
			tempWO.isOccupiedById=Integer.parseInt(splitstring[9+itask]);
			tempWO.isOccupiedBy2Id=Integer.parseInt(splitstring[10+itask]);
			tempWO.isOccupiedBy3Id=Integer.parseInt(splitstring[11+itask]);
			
			tempWO.isOccupiedBy4Id=Integer.parseInt(splitstring[12+itask]);
			tempWO.isOccupiedBy5Id=Integer.parseInt(splitstring[13+itask]);
			tempWO.isOccupiedBy6Id=Integer.parseInt(splitstring[14+itask]);
			tempWO.isOccupiedBy7Id=Integer.parseInt(splitstring[15+itask]);
			tempWO.isOccupiedBy8Id=Integer.parseInt(splitstring[16+itask]);
			tempWO.isOccupiedBy9Id=Integer.parseInt(splitstring[17+itask]);
			tempWO.isOccupiedByExternId=Integer.parseInt(splitstring[18+itask]);
			
			tempWO.x_temp=Integer.parseInt(splitstring[19+itask]);
			tempWO.y_temp=Integer.parseInt(splitstring[20+itask]);
			tempWO.rotation_temp=Integer.parseInt(splitstring[21+itask]);
			tempWO.movementX=Integer.parseInt(splitstring[22+itask]);
			
			tempWO.tempcount=Float.parseFloat(splitstring[23+itask]);
			tempWO.actionvar1=Float.parseFloat(splitstring[24+itask]);
			tempWO.actionvar2=Float.parseFloat(splitstring[25+itask]);
			tempWO.actionvar3=Float.parseFloat(splitstring[26+itask]);
			tempWO.actionvar4=Float.parseFloat(splitstring[27+itask]);
			tempWO.actionvar5=Float.parseFloat(splitstring[28+itask]);
			tempWO.actionvar6=Float.parseFloat(splitstring[29+itask]);
			tempWO.actionvar7=Float.parseFloat(splitstring[30+itask]);
			tempWO.actionvar8=Float.parseFloat(splitstring[31+itask]);
			tempWO.actionvar9=Float.parseFloat(splitstring[32+itask]);
			
			tempWO.actionstring1=splitstring[33+itask];
			tempWO.actionstring2=splitstring[34+itask];
			tempWO.actionstring3=splitstring[35+itask];
			
			tempWO.actionanim1=Float.parseFloat(splitstring[36+itask]);
			tempWO.actionanim2=Float.parseFloat(splitstring[37+itask]);
			tempWO.actionfield1=Float.parseFloat(splitstring[38+itask]);
			tempWO.actionfield2=Float.parseFloat(splitstring[39+itask]);
			tempWO.actionfield3=Float.parseFloat(splitstring[40+itask]);
									
			//writeString(bw, wobj.tempcount+";"+wobj.actionvar1+";" + wobj.actionvar2 + ";" + wobj.actionvar3 + ";" + wobj.actionvar4 + ";" + wobj.actionvar5 + ";" + wobj.actionvar6 + ";");
			
			if(type.equals("WORLDOBJECT"))
			{
				tempWO.ownerid=Integer.parseInt(splitstring[41]);
				tempWO.owner2id=Integer.parseInt(splitstring[42]);
				tempWO.researchObjectId=splitstring[43];
				tempWO.setResearchProject(tempWO.researchObjectId);
				tempWO.fuelValue=Float.parseFloat(splitstring[44]);
				tempWO.iOccupied1_Arrived=Integer.parseInt(splitstring[45]);
				tempWO.iOccupiedExtern_Arrived=Integer.parseInt(splitstring[46]);
				String smulti=splitstring[47];
				tempWO.setObjectFillingMultiFromSaveString(smulti);
				
				tempWO.theroomid=Integer.parseInt(splitstring[48]);
				
				tempWO.color1 = new Color(Float.parseFloat(splitstring[49]), Float.parseFloat(splitstring[50]), Float.parseFloat(splitstring[51]), Float.parseFloat(splitstring[52]));
				tempWO.shadowDistance = Integer.parseInt(splitstring[53]);
				tempWO.movementSpeed = Integer.parseInt(splitstring[54]);
				
				tempWO.dynamicwidth = Integer.parseInt(splitstring[55]);
				tempWO.dynamicheight = Integer.parseInt(splitstring[56]);
				tempWO.setDynamicSize(0);
				
				if(Float.parseFloat(splitstring[57])>-1)
					tempWO.actionColor1 = new Color(Float.parseFloat(splitstring[57]), Float.parseFloat(splitstring[58]), Float.parseFloat(splitstring[59]), Float.parseFloat(splitstring[60]));
				//writeString(bw, ";"+wobj.color1.r+";"+ wobj.color1.g+";"+wobj.color1.b+";"+wobj.color1.a+";"+wobj.shadowDistance+";"+wobj.movementSpeed);
				
				tempWO.theaddressid2 = Integer.parseInt(splitstring[61]);
				tempWO.dynamicprice = Integer.parseInt(splitstring[62]);
				if(tempWO.dynamicprice>0)
					tempWO.theobject.price=tempWO.dynamicprice;
				
				tempWO.workTime1_From_temp = Integer.parseInt(splitstring[63]);							
				tempWO.workTime1_To_temp = Integer.parseInt(splitstring[64]);
				tempWO.iZombie = Float.parseFloat(splitstring[65]);
				tempWO.iObjectIsReady = Float.parseFloat(splitstring[66]);
			}
			
			if(type.equals("TASKOBJECT"))
			{
				tempWO.ownerid=Integer.parseInt(splitstring[41+itask]);
				tempWO.owner2id=Integer.parseInt(splitstring[42+itask]);
				tempWO.owner3id=Integer.parseInt(splitstring[43+itask]);
				tempWO.owner4id=Integer.parseInt(splitstring[44+itask]);
				tempWO.owner5id=Integer.parseInt(splitstring[45+itask]);
				tempWO.owner6id=Integer.parseInt(splitstring[46+itask]);
				tempWO.owner7id=Integer.parseInt(splitstring[47+itask]);
				tempWO.owner8id=Integer.parseInt(splitstring[48+itask]);
											
				tempWO.actiontime1=Integer.parseInt(splitstring[49+itask]);
				tempWO.actiontime2=Integer.parseInt(splitstring[50+itask]);
				tempWO.actiontime3=Integer.parseInt(splitstring[51+itask]);
				tempWO.actiontimenr=Integer.parseInt(splitstring[52+itask]);
				
				tempWO.actiontime1check=Boolean.parseBoolean(splitstring[53+itask]);
				tempWO.actiontime2check=Boolean.parseBoolean(splitstring[54+itask]);
				tempWO.actiontime3check=Boolean.parseBoolean(splitstring[55+itask]);
				
				tempWO.researchObjectId=splitstring[56+itask];
				tempWO.setResearchProject(tempWO.researchObjectId);
				
				tempWO.iOccupied1_Arrived=Integer.parseInt(splitstring[57+itask]);
				tempWO.iOccupiedExtern_Arrived=Integer.parseInt(splitstring[58+itask]);
				
				String smulti=splitstring[59+itask];
				tempWO.setObjectFillingMultiFromSaveString(smulti);
				
				tempWO.theroomid = Integer.parseInt(splitstring[60+itask]);
				tempWO.color1 = new Color(Float.parseFloat(splitstring[61+itask]), Float.parseFloat(splitstring[62+itask]), Float.parseFloat(splitstring[63+itask]), Float.parseFloat(splitstring[64+itask]));
				tempWO.shadowDistance = Integer.parseInt(splitstring[65+itask]);
				tempWO.movementSpeed = Integer.parseInt(splitstring[66+itask]);
				//writeString(bw, ";"+wobj.color1.r+";"+ wobj.color1.g+";"+wobj.color1.b+";"+wobj.color1.a+";"+wobj.shadowDistance+";"+wobj.movementSpeed);
				
				tempWO.dynamicwidth = Integer.parseInt(splitstring[67+itask]);
				tempWO.dynamicheight = Integer.parseInt(splitstring[68+itask]);
				tempWO.setDynamicSize(0);
				if(Float.parseFloat(splitstring[69+itask])>-1)
					tempWO.actionColor1 = new Color(Float.parseFloat(splitstring[69+itask]), Float.parseFloat(splitstring[70+itask]), Float.parseFloat(splitstring[71+itask]), Float.parseFloat(splitstring[72+itask]));
				
				tempWO.theaddressid2 = Integer.parseInt(splitstring[73+itask]);
				tempWO.dynamicprice = Integer.parseInt(splitstring[74+itask]);
				
				tempWO.iObjectIsReady = Float.parseFloat(splitstring[75+itask]);
				if(tempWO.dynamicprice>0)
					tempWO.theobject.price=tempWO.dynamicprice;
			}
			
			tempWO.uniqueId=Integer.parseInt(uid);
			
			
			if(addToList)
			{
				if(obj.isGarbageContainer)
					worldGarbageContainers.add(tempWO);
				else if(obj.isGroundObject || obj.isGroundBaseObject)
				{
					worldGroundObjects.add(tempWO);
					if(obj.isWaterObject)
						worldWaterObjects.add(tempWO);
				}
				else if(obj.editoraction.contains("road_road_footpath"))
					worldFootpath.add(tempWO);
				else if(obj.editoraction.contains("zombie_entrance"))
					worldZombieEntrances.add(tempWO);
				else if(obj.editoraction.contains("road_road_road"))
					worldRoad.add(tempWO);
				else if(obj.editoraction.contains("zombie_entrance"))
					worldZombieEntrances.add(tempWO);
				else if(obj.editoraction.contains("outdoor_light"))
					worldOutdoorLights.add(tempWO);
				else if(obj.editoraction.contains("bird"))
					worldBirds.add(tempWO);
				else if(obj.editoraction.contains("interior_light"))
					worldCoverlights.add(tempWO);
				else if(obj.editoraction.contains("traffic_car"))
					worldCars.add(tempWO);
				else if(obj.editoraction.contains("illuminati_defensewarningsystem"))
					worldDefenseWarning.add(tempWO);
				else if(obj.editoraction.contains("company_waterworks_groundwaterextractionsystem"))
					worldWatersystems.add(tempWO);
				else if(obj.editoraction.contains("supermarket_foodpallet"))
					worldDrawSpecial2.add(tempWO);
				else if(obj.editoraction.contains("_carpet"))
					worldCarpets.add(tempWO);						
				else if(obj.isDrawSpecial())
				{
					worldDrawSpecial.add(tempWO);
				}
				else
					worldObjects.add(tempWO);
				
				if(tempWO.theobject.IsDrawAdditionalObject())
					tempListDrawAdditional.add(tempWO);						
				
				addObjectToPathmap(tempWO, true);
			}
			

			
			return tempWO;
		}
		
		
		//Room-Object
		if(type.equals("ROOMOBJECT"))
		{
			String uid = splitstring[1];
			
			String objectid = splitstring[2];
			int iobjid = Integer.parseInt(objectid);
			String adrid=splitstring[3];
			int posx = Integer.parseInt(splitstring[4]);
			int posy = Integer.parseInt(splitstring[5]);
			int rotation = Integer.parseInt(splitstring[6]);
									
			CObject obj=null;
			if(iobjid<001000000)
				obj = (CObject) gameResourceConfig.listObjectres.stream().filter(item->item.objectId.equals(objectid)).findFirst().get();
			else
				obj = (CObject) gameResourceConfig.listObject.stream().filter(item->item.objectId.equals(objectid)).findFirst().get();
			
			obj.pos_x = posx;
			obj.pos_y = posy;
			obj.rotation = rotation;
			
			CWorldObject tempWO = new CWorldObject(obj, this.town, true);
			
			if(!adrid.isEmpty())
				tempWO.theaddressid = Integer.parseInt(adrid);
			
			tempWO.uniqueId=Integer.parseInt(uid);
			
			//	[FOODFILLING];[DOOBJECTACTION];[ISOCCUPIED];[ISOCCUPIEDBYEXTERN];[XTEMP];[YTEMP];[ROTATIONTEMP];[MOVEMENTX]
			tempWO.objectFilling=Integer.parseInt(splitstring[7]);
			tempWO.doObjectAction=Boolean.parseBoolean(splitstring[8]);
			tempWO.isOccupiedById=Integer.parseInt(splitstring[9]);
			tempWO.isOccupiedBy2Id=Integer.parseInt(splitstring[10]);
			tempWO.isOccupiedBy3Id=Integer.parseInt(splitstring[11]);
			
			tempWO.isOccupiedBy4Id=Integer.parseInt(splitstring[12]);
			tempWO.isOccupiedBy5Id=Integer.parseInt(splitstring[13]);
			tempWO.isOccupiedBy6Id=Integer.parseInt(splitstring[14]);
			tempWO.isOccupiedBy7Id=Integer.parseInt(splitstring[15]);
			tempWO.isOccupiedBy8Id=Integer.parseInt(splitstring[16]);
			tempWO.isOccupiedBy9Id=Integer.parseInt(splitstring[17]);
			
			tempWO.isOccupiedByExternId=Integer.parseInt(splitstring[18]);
			tempWO.x_temp=Integer.parseInt(splitstring[19]);
			tempWO.y_temp=Integer.parseInt(splitstring[20]);
			tempWO.rotation_temp=Integer.parseInt(splitstring[21]);
			tempWO.movementX=Integer.parseInt(splitstring[22]);
			tempWO.researchObjectId=splitstring[23];
			tempWO.setResearchProject(tempWO.researchObjectId);
			
			tempWO.iOccupied1_Arrived=Integer.parseInt(splitstring[24]);
			tempWO.iOccupiedExtern_Arrived=Integer.parseInt(splitstring[25]);
			
			tempWO.color1 = new Color(Float.parseFloat(splitstring[26]), Float.parseFloat(splitstring[27]), Float.parseFloat(splitstring[28]), Float.parseFloat(splitstring[29]));
			tempWO.shadowDistance = Integer.parseInt(splitstring[30]);
			
			tempWO.dynamicwidth = Integer.parseInt(splitstring[31]);
			tempWO.dynamicheight = Integer.parseInt(splitstring[32]);
			//Gdx.app.debug("1", "width: " + tempWO.width + ", tempWO.dynamicwidth: " + tempWO.dynamicwidth);
			tempWO.setDynamicSize(0);
			//Gdx.app.debug("2", "width: " + tempWO.width + ", tempWO.dynamicwidth: " + tempWO.dynamicwidth);
			
			if(Float.parseFloat(splitstring[33])>-1)
				tempWO.actionColor1 = new Color(Float.parseFloat(splitstring[33]), Float.parseFloat(splitstring[34]), Float.parseFloat(splitstring[35]), Float.parseFloat(splitstring[36]));
			
			tempWO.theaddressid2 = Integer.parseInt(splitstring[37]);
			tempWO.dynamicprice = Integer.parseInt(splitstring[38]);
			if(tempWO.dynamicprice>0)
				tempWO.theobject.price=tempWO.dynamicprice;
			
			if(!splitstring[39].equals("-"))
				tempWO.belongsToCompanyId = Integer.parseInt(splitstring[39]);
			
			tempWO.iObjectIsReady = Float.parseFloat(splitstring[40]);
			
			if(addToList)
				worldRoomObjects.add(tempWO);
			
			//addObjectToPathmap(tempWO, true);
			
			return tempWO;
		}
		
		
		//Company WorldObject
		if(type.equals("COMPANYOBJECT"))
		{
			String uid = splitstring[1];
			
			//Gdx.app.debug("log_0", "COMPANYOBJECT " + uid);
			
			String objectid = splitstring[2];
			int iobjid = Integer.parseInt(objectid);
			
			int adrid = Integer.parseInt(splitstring[3]);
			
			int posx = Integer.parseInt(splitstring[4]);
			int posy = Integer.parseInt(splitstring[5]);
			int rotation = Integer.parseInt(splitstring[6]);
			
			int cid = Integer.parseInt(splitstring[7]);
			
			int wid = Integer.parseInt(splitstring[8]);
			int wid2 = Integer.parseInt(splitstring[9]);
			
			int from = Integer.parseInt(splitstring[10]);
			int to = Integer.parseInt(splitstring[11]);
			
			int from2 = Integer.parseInt(splitstring[12]);
			int to2 = Integer.parseInt(splitstring[13]);
			
			//int vstock = Integer.parseInt(splitstring[11]);
			
			CObject obj=null;
			if(iobjid<001000000)
				obj = (CObject) gameResourceConfig.listObjectres.stream().filter(item->item.objectId.equals(objectid)).findFirst().get();
			else
				obj = (CObject) gameResourceConfig.listObject.stream().filter(item->item.objectId.equals(objectid)).findFirst().get();
			
			obj.pos_x = posx;
			obj.pos_y = posy;
			obj.rotation = rotation;
			
			CWorldObject tempWO = new CWorldObject(obj, this.town, true);
			tempWO.theaddressid = adrid;
			tempWO.belongsToCompanyId=cid;
			tempWO.workerId=wid;
			tempWO.workTime1_From=from;
			tempWO.workTime1_To=to;
			
			tempWO.worker2Id=wid2;
			tempWO.workTime2_From=from2;
			tempWO.workTime2_To=to2;
			
			tempWO.uniqueId=Integer.parseInt(uid);
			
			//	[FOODFILLING];[DOOBJECTACTION];[ISOCCUPIED];[ISOCCUPIEDBYEXTERN];[XTEMP];[YTEMP];[ROTATIONTEMP];[MOVEMENTX]
			tempWO.objectFilling=Integer.parseInt(splitstring[14]);
			tempWO.doObjectAction=Boolean.parseBoolean(splitstring[15]);
			tempWO.isOccupiedById=Integer.parseInt(splitstring[16]);
			
			tempWO.isOccupiedBy2Id=Integer.parseInt(splitstring[17]);
			tempWO.isOccupiedBy3Id=Integer.parseInt(splitstring[18]);
			tempWO.isOccupiedBy4Id=Integer.parseInt(splitstring[19]);
			tempWO.isOccupiedBy5Id=Integer.parseInt(splitstring[20]);
			tempWO.isOccupiedBy6Id=Integer.parseInt(splitstring[21]);
			tempWO.isOccupiedBy7Id=Integer.parseInt(splitstring[22]);
			tempWO.isOccupiedBy8Id=Integer.parseInt(splitstring[23]);
			tempWO.isOccupiedBy9Id=Integer.parseInt(splitstring[24]);
			
			tempWO.isOccupiedByExternId=Integer.parseInt(splitstring[25]);
			tempWO.x_temp=Integer.parseInt(splitstring[26]);
			tempWO.y_temp=Integer.parseInt(splitstring[27]);
			tempWO.rotation_temp=Integer.parseInt(splitstring[28]);
			tempWO.movementX=Integer.parseInt(splitstring[29]);
			tempWO.objectCondition=Integer.parseInt(splitstring[30]);					
			
			tempWO.tempcount=Float.parseFloat(splitstring[31]);
			tempWO.actionvar1=Float.parseFloat(splitstring[32]);
			tempWO.actionvar2=Float.parseFloat(splitstring[33]);
			tempWO.actionvar3=Float.parseFloat(splitstring[34]);
			tempWO.actionvar4=Float.parseFloat(splitstring[35]);
			tempWO.actionvar5=Float.parseFloat(splitstring[36]);
			tempWO.actionvar6=Float.parseFloat(splitstring[37]);						
			tempWO.actionvar7=Float.parseFloat(splitstring[38]);
			tempWO.actionvar8=Float.parseFloat(splitstring[39]);
			tempWO.actionvar9=Float.parseFloat(splitstring[40]);
			
			tempWO.actionstring1=splitstring[41];
			tempWO.actionstring2=splitstring[42];
			tempWO.actionstring3=splitstring[43];
			
			tempWO.actionanim1=Float.parseFloat(splitstring[44]);
			tempWO.actionanim2=Float.parseFloat(splitstring[45]);
			
			tempWO.actionfield1=Float.parseFloat(splitstring[46]);
			tempWO.actionfield2=Float.parseFloat(splitstring[47]);
			tempWO.actionfield3=Float.parseFloat(splitstring[48]);
			tempWO.researchObjectId=splitstring[49];
			tempWO.setResearchProject(tempWO.researchObjectId);
			tempWO.fuelValue=Float.parseFloat(splitstring[50]);
			
			tempWO.iOccupied1_Arrived=Integer.parseInt(splitstring[51]);
			tempWO.iOccupiedExtern_Arrived=Integer.parseInt(splitstring[52]);
			
			String smulti=splitstring[53];
			tempWO.setObjectFillingMultiFromSaveString(smulti);
			
			tempWO.theroomid=Integer.parseInt(splitstring[54]);
			
			tempWO.color1 = new Color(Float.parseFloat(splitstring[55]), Float.parseFloat(splitstring[56]), Float.parseFloat(splitstring[57]), Float.parseFloat(splitstring[58]));
			tempWO.shadowDistance = Integer.parseInt(splitstring[59]);
			tempWO.movementSpeed = Integer.parseInt(splitstring[60]);
			
			tempWO.dynamicwidth = Integer.parseInt(splitstring[61]);
			tempWO.dynamicheight = Integer.parseInt(splitstring[62]);
			tempWO.setDynamicSize(0);
			
			if(Float.parseFloat(splitstring[63])>-1)
				tempWO.actionColor1 = new Color(Float.parseFloat(splitstring[63]), Float.parseFloat(splitstring[64]), Float.parseFloat(splitstring[65]), Float.parseFloat(splitstring[66]));
			
			tempWO.theaddressid2 = Integer.parseInt(splitstring[67]);
			tempWO.dynamicprice = Integer.parseInt(splitstring[68]);
			tempWO.iObjectIsReady = Float.parseFloat(splitstring[69]);
			
			if(tempWO.dynamicprice>0)
				tempWO.theobject.price=tempWO.dynamicprice;
			
			//						if(tempWO.theobject.editoraction.contains("company_waterworks_groundwaterextractionsystem"))
			//						{
			//							
			//						}
			
			tempWO.theobject.setDynamicAttributes();

			if(addToList)
			{
				if(obj.editoraction.contains("interior_light"))
					worldCoverlights.add(tempWO);
				else if(obj.editoraction.contains("traffic_car"))
					worldCars.add(tempWO);
				else if(obj.editoraction.contains("illuminati_defensewarningsystem"))
					worldDefenseWarning.add(tempWO);
				else if(obj.editoraction.contains("company_waterworks_groundwaterextractionsystem"))
					worldWatersystems.add(tempWO);
				else if(obj.editoraction.contains("supermarket_foodpallet"))
					worldDrawSpecial2.add(tempWO);
				else if(obj.editoraction.contains("zombie_entrance"))
					worldZombieEntrances.add(tempWO);
	
				else if(obj.editoraction.contains("_carpet"))
					worldCarpets.add(tempWO);
				else if(obj.isDrawSpecial())
				{
					worldDrawSpecial.add(tempWO);				
				}
				else
					worldObjects.add(tempWO);
				
				if(tempWO.theobject.IsDrawAdditionalObject())
					tempListDrawAdditional.add(tempWO);	
				
				addObjectToPathmap(tempWO, true);			
			}
			
			return tempWO;
		}
		
		//Human WorldObject
		if(type.equals("HUMAN"))
		{
			String uid = splitstring[1];
			String objectid = splitstring[2];
			int iobjid = Integer.parseInt(objectid);
			String adrid=splitstring[3];
			
			//String wrkplaceid=splitstring[3];
			int posx = Integer.parseInt(splitstring[4]);
			int posy = Integer.parseInt(splitstring[5]);
			int rotation = Integer.parseInt(splitstring[6]);
			
			CObject obj=null;
			if(iobjid < 001000000)
				obj = (CObject) gameResourceConfig.listObjectres.stream().filter(item->item.objectId.equals(objectid)).findFirst().get();
			else
			{
				Optional<CObject> opt1 = gameResourceConfig.listObjectHead_Women.stream().filter(item->item.objectId.equals(objectid)).findFirst();
				if(opt1.isPresent())
					obj=opt1.get();
				else
				{
					Optional<CObject> opt2 = gameResourceConfig.listObjectHead_Men.stream().filter(item->item.objectId.equals(objectid)).findFirst();
					if(opt2.isPresent())
						obj=opt2.get();
				}
			}
			
			obj.pos_x = posx;
			obj.pos_y = posy;
			obj.rotation = rotation;
			
			CWorldObject tempWO = new CWorldObject(obj, this.town, true);
			tempWO.thehuman.headTextureId=splitstring[7];
			tempWO.thehuman.setName(splitstring[8]);
			tempWO.thehuman.gender=(splitstring[9].charAt(0));
			tempWO.thehuman.ageSeconds = new BigDecimal(Integer.parseInt(splitstring[10]));
			
			tempWO.thehuman.setHealthValue(Float.parseFloat(splitstring[11]));
			tempWO.thehuman.healthValueMax = Integer.parseInt(splitstring[12]);
			tempWO.thehuman.setHappynessValue(Float.parseFloat(splitstring[13]));
			tempWO.thehuman.happinessValueMax = Integer.parseInt(splitstring[14]);
			tempWO.thehuman.sleepValue = Float.parseFloat(splitstring[15]);
			tempWO.thehuman.energyValue = Float.parseFloat(splitstring[16]);
			tempWO.thehuman.eatValue = Float.parseFloat(splitstring[17]);
			tempWO.thehuman.cleanValue = Float.parseFloat(splitstring[18]);
			tempWO.thehuman.toiletValue = Float.parseFloat(splitstring[19]);
			tempWO.thehuman.workValue = Float.parseFloat(splitstring[20]);
			tempWO.thehuman.clothingValue = Float.parseFloat(splitstring[21]);
			
			tempWO.thehuman.setIntelligenceValue(Float.parseFloat(splitstring[22]));
			tempWO.thehuman.setFitnessValue(Float.parseFloat(splitstring[23]));
			tempWO.thehuman.healthAttitude = Float.parseFloat(splitstring[24]);
			tempWO.thehuman.positiveAttitude = Float.parseFloat(splitstring[25]);
			tempWO.thehuman.setEducationValue(Float.parseFloat(splitstring[26]));
			tempWO.thehuman.coffeinLevel = Float.parseFloat(splitstring[27]);
			
			tempWO.ziel_x=Integer.parseInt(splitstring[28]);
			tempWO.ziel_y=Integer.parseInt(splitstring[29]);
			
			tempWO.activeActionMode=CAction.getActionModeForText(splitstring[30]);
			tempWO.activeActionModeTemp=CAction.getActionModeForText(splitstring[31]);
			
			//tempWO.setHead(new Texture(Gdx.files.internal("./bin/"+tempWO.thehuman.headTextureFilename)));
			tempWO.initHead(tempWO.thehuman.gender+"", tempWO.thehuman.headTextureId);
			
			//	[FOODFILLING];[DOOBJECTACTION];[ISOCCUPIED];[ISOCCUPIEDBYEXTERN];[XTEMP];[YTEMP];[ROTATIONTEMP];[MOVEMENTX]
			tempWO.objectFilling=Integer.parseInt(splitstring[32]);
			tempWO.doObjectAction=Boolean.parseBoolean(splitstring[33]);
			tempWO.isOccupiedById=Integer.parseInt(splitstring[34]);
			
			tempWO.isOccupiedBy2Id=Integer.parseInt(splitstring[35]);
			tempWO.isOccupiedBy3Id=Integer.parseInt(splitstring[36]);
			tempWO.isOccupiedBy4Id=Integer.parseInt(splitstring[37]);
			tempWO.isOccupiedBy5Id=Integer.parseInt(splitstring[38]);
			tempWO.isOccupiedBy6Id=Integer.parseInt(splitstring[39]);
			tempWO.isOccupiedBy7Id=Integer.parseInt(splitstring[40]);
			tempWO.isOccupiedBy8Id=Integer.parseInt(splitstring[41]);
			tempWO.isOccupiedBy9Id=Integer.parseInt(splitstring[42]);
			
			tempWO.isOccupiedByExternId=Integer.parseInt(splitstring[43]);
			tempWO.x_temp=Integer.parseInt(splitstring[44]);
			tempWO.y_temp=Integer.parseInt(splitstring[45]);
			tempWO.rotation_temp=Integer.parseInt(splitstring[46]);
			tempWO.movementX=Integer.parseInt(splitstring[47]);								
									
			tempWO.tempcount=Float.parseFloat(splitstring[48]);
			tempWO.actionvar1=Float.parseFloat(splitstring[49]);
			tempWO.actionvar2=Float.parseFloat(splitstring[50]);
			tempWO.actionvar3=Float.parseFloat(splitstring[51]);
			tempWO.actionvar4=Float.parseFloat(splitstring[52]);
			tempWO.actionvar5=Float.parseFloat(splitstring[53]);
			tempWO.actionvar6=Float.parseFloat(splitstring[54]);
			tempWO.actionvar7=Float.parseFloat(splitstring[55]);
			tempWO.actionvar8=Float.parseFloat(splitstring[56]);
			tempWO.actionvar9=Float.parseFloat(splitstring[57]);
								
			tempWO.actionstring1=splitstring[58];
			tempWO.actionstring2=splitstring[59];
			tempWO.actionstring3=splitstring[60];
			
			tempWO.actionanim1=Float.parseFloat(splitstring[61]);
			tempWO.actionanim2=Float.parseFloat(splitstring[62]);
			tempWO.actionfield1=Float.parseFloat(splitstring[63]);
			tempWO.actionfield2=Float.parseFloat(splitstring[64]);
			tempWO.actionfield3=Float.parseFloat(splitstring[65]);
			
			tempWO.goByCar_ActionState=Integer.parseInt(splitstring[66]);
			tempWO.goByCar_X=Integer.parseInt(splitstring[67]);
			tempWO.goByCar_Y=Integer.parseInt(splitstring[68]);
			tempWO.goByCar_TargetObjectId=Integer.parseInt(splitstring[69]);
			tempWO.goByCar_TargetParkingplaceId=Integer.parseInt(splitstring[70]);
			
			tempWO.objectAnimSpeedModifier=Float.parseFloat(splitstring[71]);
			
			tempWO.actionvar_fs1=Float.parseFloat(splitstring[72]);
			tempWO.lastActionMode=CAction.getActionModeForText(splitstring[73]);
			tempWO.lastTargetActionObjectType=splitstring[74];
			
			String expString = splitstring[75];
			
			if(expString.length()>0)
			{
				//;objid,exp-objid,exp;
				String[] str1 = expString.split("-");
				for(String p1 : str1)
				{
					String[] str2 = p1.split(",");
					if(str2 != null && str2.length==2)
					{
						int id = Integer.parseInt(str2[0]);
						float exp = Float.parseFloat(str2[1]);
						CJobSkillClass jsc1 = new CJobSkillClass();
						jsc1.fskill = exp;
						Optional<CObject> obj1=null;
						if(id==999999999)
							obj1 = gameResourceConfig.listObject.stream().filter(item->item.editoraction.contains("diningroom_diningtable")).findFirst();
						else
							obj1 = gameResourceConfig.listObject.stream().filter(item->Integer.parseInt(item.objectId)==id).findFirst();
						
						jsc1.theobject = obj1.get();
						tempWO.thehuman.jobSkillLevel.put(id, jsc1);
					}
				}
			}
			
			tempWO.researchObjectId = splitstring[76];
			tempWO.setResearchProject(tempWO.researchObjectId);
			
			tempWO.thehuman.sick = Float.parseFloat(splitstring[77]);
			tempWO.thehuman.doctorHealingValue = Float.parseFloat(splitstring[78]);
			tempWO.thehuman.sickType = Integer.parseInt(splitstring[79]);
			
			tempWO.iOccupied1_Arrived=Integer.parseInt(splitstring[80]);
			tempWO.iOccupiedExtern_Arrived=Integer.parseInt(splitstring[81]);
			
			tempWO.thehuman.clothingColor_Top.r = Float.parseFloat(splitstring[82]);
			tempWO.thehuman.clothingColor_Top.g = Float.parseFloat(splitstring[83]);
			tempWO.thehuman.clothingColor_Top.b = Float.parseFloat(splitstring[84]);
			
			tempWO.thehuman.clothingColor_Bottom.r = Float.parseFloat(splitstring[85]);
			tempWO.thehuman.clothingColor_Bottom.g = Float.parseFloat(splitstring[86]);
			tempWO.thehuman.clothingColor_Bottom.b = Float.parseFloat(splitstring[87]);
			
			tempWO.thehuman.clothingColor_Shoes.r = Float.parseFloat(splitstring[88]);
			tempWO.thehuman.clothingColor_Shoes.g = Float.parseFloat(splitstring[89]);
			tempWO.thehuman.clothingColor_Shoes.b = Float.parseFloat(splitstring[90]);
			
			tempWO.color1 = new Color(Float.parseFloat(splitstring[91]), Float.parseFloat(splitstring[92]), Float.parseFloat(splitstring[93]), Float.parseFloat(splitstring[94]));
			tempWO.shadowDistance = Integer.parseInt(splitstring[95]);
			tempWO.movementSpeed = Integer.parseInt(splitstring[96]);
			//writeString(bw, ";"+wobj.color1.r+";"+ wobj.color1.g+";"+wobj.color1.b+";"+wobj.color1.a+";"+wobj.shadowDistance+";"+wobj.movementSpeed);
			
			tempWO.dynamicwidth = Integer.parseInt(splitstring[97]);
			tempWO.dynamicheight = Integer.parseInt(splitstring[98]);
			tempWO.setDynamicSize(0);			
			
			//ACHTUNG: if number count ist gleich wie erster wert in nachfolgender zeile
			if(Float.parseFloat(splitstring[99])>-1)
				tempWO.actionColor1 = new Color(Float.parseFloat(splitstring[99]), Float.parseFloat(splitstring[100]), Float.parseFloat(splitstring[101]), Float.parseFloat(splitstring[102]));
			
			tempWO.thehuman.fruitLevel = Float.parseFloat(splitstring[103]);
			tempWO.theaddressid2 = Integer.parseInt(splitstring[104]);
			tempWO.thehuman.setDemandFromSaveString(splitstring[105]);
			tempWO.dynamicprice = Integer.parseInt(splitstring[106]);
			if(tempWO.dynamicprice>0)
				tempWO.theobject.price=tempWO.dynamicprice;
			
			tempWO.iZombie = Float.parseFloat(splitstring[107]);
			tempWO.timeInTown = Float.parseFloat(splitstring[108]);
			if(splitstring.length>109)
				tempWO.thehuman.abilitySpaceshipTechnology = Integer.parseInt(splitstring[109]);
			
			
			tempWO.thehuman.clothingColor_Top_Now.r=tempWO.thehuman.clothingColor_Top.r;
			tempWO.thehuman.clothingColor_Top_Now.g=tempWO.thehuman.clothingColor_Top.g;
			tempWO.thehuman.clothingColor_Top_Now.b=tempWO.thehuman.clothingColor_Top.b;
			
			tempWO.thehuman.clothingColor_Bottom_Now.r=tempWO.thehuman.clothingColor_Bottom.r;
			tempWO.thehuman.clothingColor_Bottom_Now.g=tempWO.thehuman.clothingColor_Bottom.g;
			tempWO.thehuman.clothingColor_Bottom_Now.b=tempWO.thehuman.clothingColor_Bottom.b;
			
			tempWO.thehuman.clothingColor_Shoes_Now.r=tempWO.thehuman.clothingColor_Shoes.r;
			tempWO.thehuman.clothingColor_Shoes_Now.g=tempWO.thehuman.clothingColor_Shoes.g;
			tempWO.thehuman.clothingColor_Shoes_Now.b=tempWO.thehuman.clothingColor_Shoes.b;
			
			tempWO.theaddressid = Integer.parseInt(adrid);
			tempWO.uniqueId = Integer.parseInt(uid);
			
			if(addToList)
			{
				if(tempWO.iZombie>=1)
					worldZombies.add(tempWO);
				else
					worldHumans.add(tempWO);
				
				addObjectToPathmap(tempWO, true);
			}
			
			return tempWO;
		}
		
		
		//Address
		//[TYPE=ADDRESS];[ADDRESS_ID];[ADDRESS_NAME];[SX];[SY];[EX];[EY]
		if(type.equals("ADDRESS"))
		{
			String addressid = splitstring[1];
			int adrid = Integer.parseInt(addressid);
			String adrname = splitstring[2];
			String adrtype = splitstring[3];
			float x1 = Float.parseFloat(splitstring[4]);
			float y1 = Float.parseFloat(splitstring[5]);
			float x2 = Float.parseFloat(splitstring[6]);
			float y2 = Float.parseFloat(splitstring[7]);
			
			CAddress address = new CAddress(x1, y1, x2, y2, adrname, town, adrid, adrtype);
			
			if(addToList)
				worldAddressList.add(address);
			
			return address;
		}
		
		
		//Company
		//[TYPE=COMPANY];[COMPANY_ID];[COMPANYNAME];[COMPANY_TYPE: ELECTRICAL_WORKS, WATERWORKS, SUPERMARKET, SOFTWARE_DEVELOPMENT, NOT_DEFINED];[ADDRESS_ID];[WORKOUTPUT]
		if(type.equals("COMPANY"))
		{
			int icompid = Integer.parseInt(splitstring[1]);
			String compname = splitstring[2];
			String ctype = splitstring[3];
			int iadrid = Integer.parseInt(splitstring[4]);
			//Gdx.app.debug("", compname + " company adrid: " + iadrid);
			int workoutput = Integer.parseInt(splitstring[5]);
			int workoutput_finance = Integer.parseInt(splitstring[6]);
			int workoutput_population = Integer.parseInt(splitstring[7]);
			int workoutput_other = Integer.parseInt(splitstring[8]);
			int workoutput_intelligence=0;
			if(splitstring.length>9)
				workoutput_intelligence = Integer.parseInt(splitstring[9]);
			
			CCompany comp = new CCompany(icompid, town, compname, iadrid, ctype, workoutput);
			comp.addWorkOutput(workoutput_finance, WorkoutputType.FINANCE);
			comp.addWorkOutput(workoutput_population, WorkoutputType.POPULATION);
			comp.addWorkOutput(workoutput_other, WorkoutputType.OTHER);
			comp.addWorkOutput(workoutput_intelligence, WorkoutputType.INTELLIGENCE);
			
			if(addToList)
			{
				worldCompanyList.add(comp);
				comp.setAchievement();
			}
			
			return comp;
		}
							
		if(type.equals("OBJECT"))
		{
			String sid = splitstring[1];
			int wo = Integer.parseInt(splitstring[2]);
			Optional<CObject> obj = gameResourceConfig.listObject.stream().filter(item->item.objectId.equals(sid)).findFirst();
			if(obj.isPresent())
				obj.get().iResearchCurrentWorkoutput=wo;
		}

		if(type.equals("BONUSOBJECTS"))
		{
			String ids = splitstring[1];
			String[] aids = ids.split(",");
			for(String sid : aids)
			{
				Optional<CObject> obj = gameResourceConfig.listObject.stream().filter(item->item.objectId.equals(sid)).findFirst();
				if(obj.isPresent())
					obj.get().iOneTimeBonus=1;
			}
		}
		
		if(type.equals("UNLOCKEDCOMPANIES"))
		{
			String ids = splitstring[1];
			String[] aids = ids.split(",");
			for(String sid : aids)
			{
				if(sid.length()>2)
				{
					town.gameGui.unlockedCompanyTypes.add(sid);
				}
			}
		}
		
		//Action
		//[ACTIONMODE];[WORLDOBJECTID];[DOACTIONMODE];[GOTOACTIONMODE];[TARGETACTIONOBJECTID];[TARGETACTIONOBJECT2ID];[TARGETACTIONOBJECT3ID];[TARGETACTIONOBJECT4ID];[TARGETACTIONOBJECT5ID]
		//	[ACTIONSTATE];[ACTIONREPEATER];[TARGETCOMPANYID];[ACTIONDURATION]
		if(type.equals("ACTION"))
		{
			ActionMode amode = CAction.getActionModeForText(splitstring[1]);
			ValueType vtype = CAction.getValueTypeForText(splitstring[2]);
			int worldobjectid = Integer.parseInt(splitstring[3]);
			
			Optional<CWorldObject> tresident = worldHumans.stream().filter(item->item.uniqueId==worldobjectid).findFirst(); 
			if(!tresident.isPresent())
				tresident = worldZombies.stream().filter(item->item.uniqueId==worldobjectid).findFirst();
			CWorldObject human = tresident.get(); 
			
			CAction tempAction=null;
			
			Optional<CAction> ac = human.actionList_Work.stream().filter(item->item.actionMode==amode).findFirst();
			if(!ac.isPresent())
				ac = human.actionList_Default.stream().filter(item->item.actionMode==amode).findFirst();
			if(!ac.isPresent())
				ac = human.actionList_Default_Priority.stream().filter(item->item.actionMode==amode).findFirst();
			
			if(ac.isPresent())
			{
				tempAction=ac.get();
				
				tempAction.valueType=vtype;
				tempAction.bActionMode=Boolean.parseBoolean(splitstring[4]);
				tempAction.bGotoActionMode=Boolean.parseBoolean(splitstring[5]);
				tempAction.targetActionObjectId = Integer.parseInt(splitstring[6]);
				tempAction.targetActionObject2Id = Integer.parseInt(splitstring[7]);
				tempAction.targetActionObject3Id = Integer.parseInt(splitstring[8]);
				tempAction.targetActionObject4Id = Integer.parseInt(splitstring[9]);
				tempAction.targetActionObject5Id = Integer.parseInt(splitstring[10]);
				tempAction.targetActionObject6Id = Integer.parseInt(splitstring[11]);
				tempAction.targetActionObject7Id = Integer.parseInt(splitstring[12]);
				tempAction.targetActionObject8Id = Integer.parseInt(splitstring[13]);
				tempAction.targetActionObject9Id = Integer.parseInt(splitstring[14]);
				tempAction.targetActionObject10Id = Integer.parseInt(splitstring[15]);
				tempAction.targetActionObject11Id = Integer.parseInt(splitstring[16]);
				
				tempAction.actionState = Float.parseFloat(splitstring[17]);
				tempAction.actionRepeater = Integer.parseInt(splitstring[18]);
				tempAction.targetCompanyId = Integer.parseInt(splitstring[19]);
				tempAction.actionDuration = Float.parseFloat(splitstring[20]);
				
				tempAction.attributeTimer = Float.parseFloat(splitstring[21]);
				tempAction.deltaTimer = Float.parseFloat(splitstring[22]);
				tempAction.deltaTimer2 = Float.parseFloat(splitstring[23]);
				tempAction.deltaTimer3 = Float.parseFloat(splitstring[24]);
				tempAction.hourTimer = Float.parseFloat(splitstring[25]);
				
				tempAction.actionTemp_Float1 = Float.parseFloat(splitstring[26]);
				tempAction.actionTemp_Float2 = Float.parseFloat(splitstring[27]);
				tempAction.actionTemp_Float3 = Float.parseFloat(splitstring[28]);
				tempAction.actionTemp_Float4 = Float.parseFloat(splitstring[29]);
				tempAction.actionTemp_Float5 = Float.parseFloat(splitstring[30]);
				
				tempAction.actionTemp_String1 = splitstring[31];
				tempAction.actionTemp_String2 = splitstring[32];
				
				if(human.activeActionMode==tempAction.actionMode)
					human.activeAction=tempAction;
				
				if(human.activeActionModeTemp==tempAction.actionMode)
					human.activeActionTemp=tempAction;
			}
		}
		
		return null;
	}
	
	public void initCompanytypes()
	{
		town.gameGui.unlockedCompanyTypes=new ArrayList<String>();
		town.gameGui.unlockedCompanyTypes.add("009001");
		town.gameGui.unlockedCompanyTypes.add("009002");
		town.gameGui.unlockedCompanyTypes.add("009003");
		town.gameGui.unlockedCompanyTypes.add("009004");
		town.gameGui.unlockedCompanyTypes.add("009014");
		town.gameGui.unlockedCompanyTypes.add("009022");
	}
	
	public void loadFromFile(String sfilename)
	{
		try 
		{
			initCompanytypes();
			
			town.gameResourceConfig.resetObjects();
			town.bLoadingSaveGame=true;
			
			town.gameCam.zoom = 3.0f;
			town.gameCam.position.set(mapsize/2, mapsize/2, 0);
			town.gameCam.update();
			
			initAStar();
			
			town.gameGui.buttonX2.toggleActive=false;
			town.gameGui.buttonX4.toggleActive=false;
			town.gameGui.iShowHint=0;
			
			infoEventMoving=0;
			
			initBox2D();
			
			if(town.gameGui.bFlashlight)
				placinglight.setActive(true);
			
			//FileHandle file = Gdx.files.internal("data/save/"+sfilename+".town");
			FileHandle file = CHelper.getFileHandle("appdata/local/HTP/data/save/"+sfilename+".town");
			
			BufferedReader reader = new BufferedReader(file.reader());
			String line = reader.readLine();
			
			//dispose(); Dispose führt hier zu Absturz, nur clearen funktioniert 
			
			//dispose();
			/*
			tempListDrawAdditional.clear();
			worldDrawSpecial.clear();
			worldDefenseWarning.clear();
			worldWatersystems.clear();
			worldCars.clear();
			worldObjects.clear();
			worldDrawSpecial2.clear();
			worldCarpets.clear();
			worldHumans.clear();
			worldUnemployed.clear();
			worldCoverlights.clear();
			worldBirds.clear();
			worldZombies.clear();
			worldZombieEntrances.clear();
			worldGarbageContainers.clear();
			worldGroundObjects.clear();
			worldWaterObjects.clear();
			worldFootpath.clear();
			worldOutdoorLights.clear();
			worldRoad.clear();
			worldRoomObjects.clear();
			worldTempRoomObjects.clear();
			tempHumansDead.clear();
			worldAddressList.clear();
			cloneAddressList.clear();
			worldCompanyList.clear();
			infoEvents.clear();
			townStatistics.clear();
			tempObjectBonusPlaceList.clear();
			*/
			//System.gc();
			
			//town.gameResourceConfig.resetObjects();
			
			int icount=0;
			while( line != null ) 
			{
				//[OBJECTID];[OBJECTNAME];[OBJECTTYPEID];[WIDTH];[HEIGHT];[ICONFILENAME];[FILENAME];[FRAMECOLS];[FRAMEROWS];[MOVE_STARTFRAME];[MOVE_ENDFRAME];[ACTION1_STARTFRAME];[ACTION1_ENDFRAME];[IDLEFRAME]
				try
				{
					
					line = readString(line);
					
					String sSaveFormat=""; //0.1
					
					if(icount==0) //World Values
					{
						//writeString(bw, "0.1" + ";" + String.valueOf(worldTime.getTotalSeconds()) + ";" + String.valueOf(townMoney+";"+lastHappinessFundDay) + ";" + town.gameWorld.addressArchitectPlanningValue);
						String[] splitstring = line.split(";");
						sSaveFormat = splitstring[0];
						worldTime.init(Long.parseLong(splitstring[1]));
						townMoney=Integer.parseInt(splitstring[2]);
						lastHappinessFundDay=Integer.parseInt(splitstring[3]);
						addressArchitectPlanningValue=Integer.parseInt(splitstring[4]);
						town.gameMode=splitstring[5];
						if(splitstring.length>6)
						{
							spawnNewZombieHour=Integer.parseInt(splitstring[6]);
							spawnNewResidentHour=Integer.parseInt(splitstring[7]);
						}
						
						town.bZombieApocalypse=true;
						if(splitstring.length>8)
						{
							int izombie = Integer.parseInt(splitstring[8]);
							if(izombie==1)
								town.bZombieApocalypse=true;
							else
								town.bZombieApocalypse=false;
						}
						//bZombieApocalypse=false;
						
						town.bNoRealEstate=false;
						if(splitstring.length>9)
						{
							int inorealestate = Integer.parseInt(splitstring[9]);
							if(inorealestate==1)
								town.bNoRealEstate=true;
							else
								town.bNoRealEstate=false;
						}
						
						town.bConstructionMode=false;
						if(splitstring.length>10)
						{
							int iconstructionmode = Integer.parseInt(splitstring[10]);
							if(iconstructionmode==1)
								town.bConstructionMode=true;
							else
								town.bConstructionMode=false;
						}
						
						town.initTownVariables(town.gameMode, "");
						
						line = reader.readLine();
						icount++;
						continue;
					}
					
					//Neu
					String[] splitstring = line.split(";");
					String type = splitstring[0];
					
					loadType(type, splitstring, true);
				}
				catch(Exception e)
				{
					//e.printStackTrace();
					CHelper.writeError(e.getMessage(), e.getStackTrace(), e);
				}
				
				line = reader.readLine();
			}
			reader.close();
			
			
			//AfterLoad set object-links / fill lists:
			
			//Worldobject: set address
			//Address: fill objectlist
			//Set Owner
			//Set OccupiedBy, OccupiedByExtern
			for(CWorldObject wobj : worldGarbageContainers)
			{
				loadWorldObjectSetLinks(wobj);
			}
			
			for(CWorldObject wobj : worldObjects)
			{
				loadWorldObjectSetLinks(wobj);
			}
			
			for(CWorldObject wobj : worldCoverlights)
			{
				loadWorldObjectSetLinks(wobj);
			}

			for(CWorldObject wobj : worldOutdoorLights)
			{
				loadWorldObjectSetLinks(wobj);
			}
			
			
			
			for(CWorldObject wobj : worldDrawSpecial)
			{
				loadWorldObjectSetLinks(wobj);
			}
			
			
			

			for(CWorldObject wobj : worldDefenseWarning)
			{
				loadWorldObjectSetLinks(wobj);
			}
			
			for(CWorldObject wobj : worldWatersystems)
			{
				loadWorldObjectSetLinks(wobj);
			}			
			
			for(CWorldObject wobj : worldCars)
			{
				loadWorldObjectSetLinks(wobj);
			}
			
			for(CWorldObject wobj : worldDrawSpecial2)
			{
				loadWorldObjectSetLinks(wobj);
			}
			
			for(CWorldObject wobj : worldCarpets)
			{
				loadWorldObjectSetLinks(wobj);
			}			
			
			for(CWorldObject wobj : worldRoad)
			{
				loadWorldObjectSetLinks(wobj);
			}

			for(CWorldObject wobj : worldFootpath)
			{
				loadWorldObjectSetLinks(wobj);
			}

			for(CWorldObject wobj : worldGroundObjects)
			{
				loadWorldObjectSetLinks(wobj);
			}

			
			//Roomobject: set address
			//Address: fill objectlist	
			//Set OccupiedBy, OccupiedByExtern
			for(CWorldObject wobj : worldRoomObjects)
			{
				if(wobj.isOccupiedById!=0)
					wobj.isOccupiedBy=getWorldObjectById(wobj.isOccupiedById, "");
					//wobj.isOccupiedBy=worldHumans.stream().filter(item->item.uniqueId==wobj.isOccupiedById).findFirst().get();
				if(wobj.isOccupiedBy2Id!=0)
					wobj.isOccupiedBy2=getWorldObjectById(wobj.isOccupiedBy2Id, "");
					//wobj.isOccupiedBy2=worldHumans.stream().filter(item->item.uniqueId==wobj.isOccupiedBy2Id).findFirst().get();
				if(wobj.isOccupiedBy3Id!=0)
					wobj.isOccupiedBy3=getWorldObjectById(wobj.isOccupiedBy3Id, "");
					//wobj.isOccupiedBy3=worldHumans.stream().filter(item->item.uniqueId==wobj.isOccupiedBy3Id).findFirst().get();
				if(wobj.isOccupiedBy4Id!=0)
					wobj.isOccupiedBy4=getWorldObjectById(wobj.isOccupiedBy4Id, "");
					//wobj.isOccupiedBy4=worldHumans.stream().filter(item->item.uniqueId==wobj.isOccupiedBy4Id).findFirst().get();
				if(wobj.isOccupiedBy5Id!=0)
					wobj.isOccupiedBy5=getWorldObjectById(wobj.isOccupiedBy5Id, "");
					//wobj.isOccupiedBy5=worldHumans.stream().filter(item->item.uniqueId==wobj.isOccupiedBy5Id).findFirst().get();
				if(wobj.isOccupiedBy6Id!=0)
					wobj.isOccupiedBy6=getWorldObjectById(wobj.isOccupiedBy6Id, "");
					//wobj.isOccupiedBy6=worldHumans.stream().filter(item->item.uniqueId==wobj.isOccupiedBy6Id).findFirst().get();
				if(wobj.isOccupiedBy7Id!=0)
					wobj.isOccupiedBy7=getWorldObjectById(wobj.isOccupiedBy7Id, "");
					//wobj.isOccupiedBy7=worldHumans.stream().filter(item->item.uniqueId==wobj.isOccupiedBy7Id).findFirst().get();
				if(wobj.isOccupiedBy8Id!=0)
					wobj.isOccupiedBy8=getWorldObjectById(wobj.isOccupiedBy8Id, "");
					//wobj.isOccupiedBy8=worldHumans.stream().filter(item->item.uniqueId==wobj.isOccupiedBy8Id).findFirst().get();
				if(wobj.isOccupiedBy9Id!=0)
					wobj.isOccupiedBy9=getWorldObjectById(wobj.isOccupiedBy9Id, "");
					//wobj.isOccupiedBy9=worldHumans.stream().filter(item->item.uniqueId==wobj.isOccupiedBy9Id).findFirst().get();
				if(wobj.isOccupiedByExternId!=0)
					wobj.isOccupiedByExtern=getWorldObjectById(wobj.isOccupiedByExternId, "");
					//wobj.isOccupiedByExtern=worldHumans.stream().filter(item->item.uniqueId==wobj.isOccupiedByExternId).findFirst().get();
				
				if(wobj.belongsToCompanyId>0)
				{
					CCompany comp = worldCompanyList.stream().filter(item->item.companyId==wobj.belongsToCompanyId).findFirst().get();
					wobj.belongsToCompany=comp;
				}
				
				if(wobj.theaddressid>0 || wobj.theaddressid2>0)
				{
					for(CAddress adr : worldAddressList)
					{
						if(adr.addressId==wobj.theaddressid)
						{
							wobj.theaddress=adr;
							adr.addWorldObject(wobj);
						}
						
						if(adr.addressId==wobj.theaddressid2)
						{
							wobj.theaddress2=adr;
							adr.addWorldObject(wobj);
						}
					}
				}
				
				addObjectToPathmap(wobj, true);
			}			
			
			//Company: set address
			for(CCompany comp : worldCompanyList)
			{
				if(comp.addressId>0)
				{
					CAddress adr = worldAddressList.stream().filter(item->item.addressId==comp.addressId).findFirst().get();
					comp.address_company=adr;
				}
			}
						
			
			
			setWorldObjectLinks(worldObjects);
			setWorldObjectLinks(worldDrawSpecial);
			setWorldObjectLinks(worldDrawSpecial2);
			setWorldObjectLinks(worldCarpets);
			setWorldObjectLinks(worldCoverlights);
			setWorldObjectLinks(worldDefenseWarning);
			setWorldObjectLinks(worldWatersystems);
			setWorldObjectLinks(worldCars);
			//Company: fill companyobjectlist (companyobjects)
			//Companyobject: set company
			
			
			//Human: set address, set action references
			for(CWorldObject hum : worldHumans)
			{
				//Address
				if(hum.theaddressid>0)
				{
					CAddress adr = worldAddressList.stream().filter(item->item.addressId==hum.theaddressid).findFirst().get();
					hum.theaddress=adr;
					adr.addWorldObject(hum);
				}
				
				if(hum.theaddressid2>0)
				{
					CAddress adr = worldAddressList.stream().filter(item->item.addressId==hum.theaddressid2).findFirst().get();
					hum.theaddress2=adr;
					adr.addWorldObject(hum);
				}
				
				if(hum.goByCar_TargetObjectId>0)
					hum.goByCar_TargetObject=getWorldObjectById(hum.goByCar_TargetObjectId, "");
				if(hum.goByCar_TargetParkingplaceId>0)
					hum.goByCar_TargetParkingplace=getWorldObjectById(hum.goByCar_TargetParkingplaceId, "");
				
				
				//Active Action
				if(hum.activeAction!=null)
				{
					if(hum.activeAction.targetActionObjectId>0)
						hum.activeAction.targetActionObject=getWorldObjectById(hum.activeAction.targetActionObjectId, "");
					if(hum.activeAction.targetActionObject2Id>0)
						hum.activeAction.targetActionObject2=getWorldObjectById(hum.activeAction.targetActionObject2Id, "");
					if(hum.activeAction.targetActionObject3Id>0)
						hum.activeAction.targetActionObject3=getWorldObjectById(hum.activeAction.targetActionObject3Id, "");
					if(hum.activeAction.targetActionObject4Id>0)
						hum.activeAction.targetActionObject4=getWorldObjectById(hum.activeAction.targetActionObject4Id, "");
					if(hum.activeAction.targetActionObject5Id>0)
						hum.activeAction.targetActionObject5=getWorldObjectById(hum.activeAction.targetActionObject5Id, "");
					
					if(hum.activeAction.targetCompanyId>0)
					{
						Optional<CCompany> comp = worldCompanyList.stream().filter(item->item.companyId==hum.activeAction.targetCompanyId).findFirst(); 
						if(comp.isPresent())
							hum.activeAction.targetCompany=comp.get();
					}
				}
			}
			
			//Set Room Id : ein weiterer durchlauf: hier sind address objects auf adrs hinterlegt und addresses gesetzt
			//Wird im Moment nur für public toilet room m und w verwendet
			//Performance: statt roomid: editoraction des rooms speichern
					

			
			setRoomId(worldObjects);
			setRoomId(worldDrawSpecial);
			setRoomId(worldDrawSpecial2);
			setRoomId(worldCarpets);
			setRoomId(worldCoverlights);
			setRoomId(worldCars);
			
		}
		catch(Exception e)
		{
			//obj.textureIcon = new Texture(Gdx.files.internal("fliese2.jpg"));
			CHelper.writeError(e.getMessage(), e.getStackTrace(), e);
			//e.printStackTrace();
		}
		
		worldWeather.resetWeather();
		worldWeather.initDayWeather();
		town.bLoadingSaveGame=false;
		bRenderFrameBuffer=true;
	}
	
	private void setRoomId(List<CWorldObject> worldObjects2)
	{
		for(CWorldObject wobj : worldObjects2)
		{
			if(wobj.theroomid>0 && wobj.theaddressid>0)
			{
				if(wobj.theaddress==null || wobj.theroomid<1)
					continue;
				
				Optional<CWorldObject> opt = wobj.theaddress.listWorldObjects_Floors.stream().filter(item->item.uniqueId==wobj.theroomid).findFirst();
				if(opt!=null && opt.isPresent())
					wobj.theroom = opt.get();
			}
		}
	}
		
	private void setWorldObjectLinks(List<CWorldObject> worldObjects2)
	{
		//Company: fill companyobjectlist (companyobjects)
		//Companyobject: set company
		for(CWorldObject obj : worldObjects2)
		{
			if(obj.belongsToCompanyId>0)
			{
				CCompany comp = worldCompanyList.stream().filter(item->item.companyId==obj.belongsToCompanyId).findFirst().get();
				obj.belongsToCompany=comp;
			}
		}
		
		//Workplaces: set Worker / Taskobjects: set Resp
		//Human: add workplaces, add taskobjects
		for(CWorldObject wobj : worldObjects2)
		{
			if(wobj.workerId>0)
			{
				CWorldObject hum = worldHumans.stream().filter(item->item.uniqueId==wobj.workerId).findFirst().get();
				wobj.worker=hum;
				
				if(wobj.isTaskObject())
					hum.thehuman.taskobjects.put(wobj.uniqueId, wobj);
				
				if(wobj.isCompanyWorkingPlace())
					hum.thehuman.workplaces.put(wobj.uniqueId, wobj);
			}
			
			if(wobj.worker2Id>0)
			{
				CWorldObject hum = worldHumans.stream().filter(item->item.uniqueId==wobj.worker2Id).findFirst().get();
				wobj.worker2=hum;
				
				if(wobj.isTaskObject())
					hum.thehuman.taskobjects.put(wobj.uniqueId, wobj);
				
				if(wobj.isCompanyWorkingPlace())
					hum.thehuman.workplaces.put(wobj.uniqueId, wobj);
			}
		}
	}
	
	private void loadWorldObjectSetLinks(CWorldObject wobj)
	{
		if(wobj.isOccupiedById!=0)
			wobj.isOccupiedBy=getWorldObjectById(wobj.isOccupiedById, "HUMAN");
			//wobj.isOccupiedBy=worldHumans.stream().filter(item->item.uniqueId==wobj.isOccupiedById).findFirst().get();
		if(wobj.isOccupiedBy2Id!=0)
			wobj.isOccupiedBy2=getWorldObjectById(wobj.isOccupiedBy2Id, "HUMAN");
			//wobj.isOccupiedBy2=worldHumans.stream().filter(item->item.uniqueId==wobj.isOccupiedBy2Id).findFirst().get();
		if(wobj.isOccupiedBy3Id!=0)
			wobj.isOccupiedBy3=getWorldObjectById(wobj.isOccupiedBy3Id, "HUMAN");
			//wobj.isOccupiedBy3=worldHumans.stream().filter(item->item.uniqueId==wobj.isOccupiedBy3Id).findFirst().get();
		if(wobj.isOccupiedBy4Id!=0)
			wobj.isOccupiedBy4=getWorldObjectById(wobj.isOccupiedBy4Id, "HUMAN");
			//wobj.isOccupiedBy4=worldHumans.stream().filter(item->item.uniqueId==wobj.isOccupiedBy4Id).findFirst().get();
		if(wobj.isOccupiedBy5Id!=0)
			wobj.isOccupiedBy5=getWorldObjectById(wobj.isOccupiedBy5Id, "HUMAN");
			//wobj.isOccupiedBy5=worldHumans.stream().filter(item->item.uniqueId==wobj.isOccupiedBy5Id).findFirst().get();
		if(wobj.isOccupiedBy6Id!=0)
			wobj.isOccupiedBy6=getWorldObjectById(wobj.isOccupiedBy6Id, "HUMAN");
			//wobj.isOccupiedBy6=worldHumans.stream().filter(item->item.uniqueId==wobj.isOccupiedBy6Id).findFirst().get();
		if(wobj.isOccupiedBy7Id!=0)
			wobj.isOccupiedBy7=getWorldObjectById(wobj.isOccupiedBy7Id, "HUMAN");
			//wobj.isOccupiedBy7=worldHumans.stream().filter(item->item.uniqueId==wobj.isOccupiedBy7Id).findFirst().get();
		if(wobj.isOccupiedBy8Id!=0)
			wobj.isOccupiedBy8=getWorldObjectById(wobj.isOccupiedBy8Id, "HUMAN");
			//wobj.isOccupiedBy8=worldHumans.stream().filter(item->item.uniqueId==wobj.isOccupiedBy8Id).findFirst().get();
		if(wobj.isOccupiedBy9Id!=0)
			wobj.isOccupiedBy9=getWorldObjectById(wobj.isOccupiedBy9Id, "HUMAN");
			//wobj.isOccupiedBy9=worldHumans.stream().filter(item->item.uniqueId==wobj.isOccupiedBy9Id).findFirst().get();
		if(wobj.isOccupiedByExternId!=0)
			wobj.isOccupiedByExtern=getWorldObjectById(wobj.isOccupiedByExternId, "HUMAN");
			//wobj.isOccupiedByExtern=worldHumans.stream().filter(item->item.uniqueId==wobj.isOccupiedByExternId).findFirst().get();
		
		if(wobj.ownerid>0)
		{
			Optional<CWorldObject> opt1 = worldHumans.stream().filter(item->item.uniqueId==wobj.ownerid).findFirst();
			
			if(!opt1.isPresent()) //Owner kann auch Objekt sein
				opt1 = worldObjects.stream().filter(item->item.uniqueId==wobj.ownerid).findFirst();

			if(!opt1.isPresent()) //Owner kann auch Objekt sein
				opt1 = worldDrawSpecial.stream().filter(item->item.uniqueId==wobj.ownerid).findFirst();

			if(!opt1.isPresent()) //Owner kann auch Objekt sein
				opt1 = worldDrawSpecial2.stream().filter(item->item.uniqueId==wobj.ownerid).findFirst();

			if(!opt1.isPresent()) //Owner kann auch Objekt sein
				opt1 = worldCarpets.stream().filter(item->item.uniqueId==wobj.ownerid).findFirst();

			if(!opt1.isPresent()) //Owner kann auch Objekt sein
				opt1 = worldCars.stream().filter(item->item.uniqueId==wobj.ownerid).findFirst();

			if(!opt1.isPresent()) //Owner kann auch Objekt sein
				opt1 = worldDefenseWarning.stream().filter(item->item.uniqueId==wobj.ownerid).findFirst();

			if(!opt1.isPresent()) //Owner kann auch Objekt sein
				opt1 = worldWatersystems.stream().filter(item->item.uniqueId==wobj.ownerid).findFirst();
			
			if(opt1.isPresent())
			{
				wobj.owner=opt1.get();
			
				if(wobj.theobject.editoraction.contains("bedroom_bed"))
					wobj.owner.thehuman.bed=wobj;
				if(wobj.theobject.editoraction.contains("traffic_car_residential"))
					wobj.owner.thehuman.car=wobj;
				if(wobj.theobject.editoraction.contains("bedroom_wardrobe"))
					wobj.owner.thehuman.wardrobe=wobj;
				if(wobj.theobject.editoraction.contains("diningroom_diningtable"))
					wobj.owner.thehuman.taskobjects.put(wobj.uniqueId, wobj);
			}
		}
		if(wobj.owner2id>0)
		{
			Optional<CWorldObject> opt1 = worldHumans.stream().filter(item->item.uniqueId==wobj.owner2id).findFirst();
			if(!opt1.isPresent()) //Owner2 kann auch Objekt sein
				opt1 = worldObjects.stream().filter(item->item.uniqueId==wobj.owner2id).findFirst();
			if(!opt1.isPresent()) //Owner2 kann auch Objekt sein
				opt1 = worldDrawSpecial.stream().filter(item->item.uniqueId==wobj.owner2id).findFirst();
			if(!opt1.isPresent()) //Owner2 kann auch Objekt sein
				opt1 = worldDrawSpecial2.stream().filter(item->item.uniqueId==wobj.owner2id).findFirst();
			if(!opt1.isPresent()) //Owner2 kann auch Objekt sein
				opt1 = worldCars.stream().filter(item->item.uniqueId==wobj.owner2id).findFirst();
			if(!opt1.isPresent()) //Owner2 kann auch Objekt sein
				opt1 = worldDefenseWarning.stream().filter(item->item.uniqueId==wobj.owner2id).findFirst();
			if(!opt1.isPresent()) //Owner2 kann auch Objekt sein
				opt1 = worldWatersystems.stream().filter(item->item.uniqueId==wobj.owner2id).findFirst();
			if(!opt1.isPresent()) //Owner2 kann auch Objekt sein
				opt1 = worldCarpets.stream().filter(item->item.uniqueId==wobj.owner2id).findFirst();

			
			if(opt1.isPresent())
			{
				wobj.owner2=opt1.get();
			
				//wobj.owner2=worldHumans.stream().filter(item->item.uniqueId==wobj.owner2id).findFirst().get();
				if(wobj.theobject.editoraction.contains("bedroom_bed"))
					wobj.owner2.thehuman.bed=wobj;
				if(wobj.theobject.editoraction.contains("diningroom_diningtable"))
					wobj.owner2.thehuman.taskobjects.put(wobj.uniqueId, wobj);
			}
		}
		if(wobj.owner3id>0)
		{
			Optional<CWorldObject> opt1=worldHumans.stream().filter(item->item.uniqueId==wobj.owner3id).findFirst();
			if(opt1.isPresent())
			{
				wobj.owner3=opt1.get();
				if(wobj.theobject.editoraction.contains("bedroom_bed"))
					wobj.owner3.thehuman.bed=wobj;
				if(wobj.theobject.editoraction.contains("diningroom_diningtable"))
					wobj.owner3.thehuman.taskobjects.put(wobj.uniqueId, wobj);
			}
		}
		if(wobj.owner4id>0)
		{
			Optional<CWorldObject> opt1=worldHumans.stream().filter(item->item.uniqueId==wobj.owner4id).findFirst();
			if(opt1.isPresent())
			{
				wobj.owner4=opt1.get();
				if(wobj.theobject.editoraction.contains("bedroom_bed"))
					wobj.owner4.thehuman.bed=wobj;
				if(wobj.theobject.editoraction.contains("diningroom_diningtable"))
					wobj.owner4.thehuman.taskobjects.put(wobj.uniqueId, wobj);
			}
		}
		if(wobj.owner5id>0)
		{
			Optional<CWorldObject> opt1=worldHumans.stream().filter(item->item.uniqueId==wobj.owner5id).findFirst();
			if(opt1.isPresent())
			{
				wobj.owner5=opt1.get();
				if(wobj.theobject.editoraction.contains("bedroom_bed"))
					wobj.owner5.thehuman.bed=wobj;
				if(wobj.theobject.editoraction.contains("diningroom_diningtable"))
					wobj.owner5.thehuman.taskobjects.put(wobj.uniqueId, wobj);
			}
		}
		if(wobj.owner6id>0)
		{
			Optional<CWorldObject> opt1=worldHumans.stream().filter(item->item.uniqueId==wobj.owner6id).findFirst();
			if(opt1.isPresent())
			{			
				wobj.owner6=opt1.get();
				if(wobj.theobject.editoraction.contains("bedroom_bed"))
					wobj.owner6.thehuman.bed=wobj;
				if(wobj.theobject.editoraction.contains("diningroom_diningtable"))
					wobj.owner6.thehuman.taskobjects.put(wobj.uniqueId, wobj);
			}
		}
		if(wobj.owner7id>0)
		{
			Optional<CWorldObject> opt1=worldHumans.stream().filter(item->item.uniqueId==wobj.owner7id).findFirst();
			if(opt1.isPresent())
			{		
				wobj.owner7=opt1.get();
				if(wobj.theobject.editoraction.contains("bedroom_bed"))
					wobj.owner7.thehuman.bed=wobj;
				if(wobj.theobject.editoraction.contains("diningroom_diningtable"))
					wobj.owner7.thehuman.taskobjects.put(wobj.uniqueId, wobj);
			}
		}
		if(wobj.owner8id>0)
		{
			Optional<CWorldObject> opt1=worldHumans.stream().filter(item->item.uniqueId==wobj.owner8id).findFirst();
			
			if(opt1.isPresent())
			{
				wobj.owner8=opt1.get();
				if(wobj.theobject.editoraction.contains("bedroom_bed"))
					wobj.owner8.thehuman.bed=wobj;
				if(wobj.theobject.editoraction.contains("diningroom_diningtable"))
					wobj.owner8.thehuman.taskobjects.put(wobj.uniqueId, wobj);
			}
		}
		
		//				if(wobj.cookid>0)
		//				{
		//					wobj.cook=worldHumans.stream().filter(item->item.uniqueId==wobj.cookid).findFirst().get();
		//				}
		
		if(wobj.theaddressid>0)
		{
			Optional<CAddress> opt1 = worldAddressList.stream().filter(item->item.addressId==wobj.theaddressid).findFirst();
			
			if(opt1.isPresent())
			{
				CAddress adr=opt1.get(); 
				//Gdx.app.debug("", "wobj.theaddressid: " + wobj.theaddressid + ", adr:" + adr + ", wobj:" + wobj.theobject.objectId);
				wobj.theaddress=adr;
				adr.addWorldObject(wobj);
			}
		}
	}
	//SAVE/LOAD<---------------------------------------------
	
	
	//OTHER--------------------------------------------->
//	public CCompany getNearestActiveCompany(CompanyType compType, CWorldObject fromObject)
//	{
//		CCompany nearestCompany=null;
//		
//		for(CCompany co : worldCompanyList)
//		{
//			int dist = 0;
//						
//			if(co.companyType == compType && co.isActive())
//			{
//				if(nearestCompany!=null)
//				{
//					int distTemp = CHelper.getEuclidianDistance(fromObject.pos_x(), fromObject.pos_y(), (int)co.address.sx, (int)co.address.sy);
//					if(distTemp<dist)
//					{
//						dist=distTemp;
//						nearestCompany=co;
//					}
//				}
//				else
//				{
//					dist = CHelper.getEuclidianDistance(fromObject.pos_x(), fromObject.pos_y(), (int)co.address.sx, (int)co.address.sy);
//					nearestCompany=co;
//				}
//			}
//		}
//		
//		return nearestCompany;
//	}
	
	public void calculateFunds()
	{
		if((worldTime.hours==12 && worldTime.day+worldTime.hours!=lastHappinessFundDay) || (worldTime.hours==23 && worldTime.day+worldTime.hours!=lastHappinessFundDay))
		{
			lastHappinessFundDay=worldTime.hours+worldTime.day;
			calculateHappinessFund();
			calculateChildSupportFund();
			calculateGreenFund();
			calculateNiceFund();
		}
	}
	
	public void calculateNiceFund()
	{
		int count=0;
		int countbonus=0;
		int countbonusgroundsadr=0;
		//Max 5 für normalen bonus zählen
		//bonus: max 5 trees, flowers, plants mit additional bonus werden gezählt
		
        for(CAddress adr : worldAddressList)
        {
			int countadr=0;
			countbonusgroundsadr=0;
			
			if((adr.listWorldObjects.size()-adr.countFlora)<3) //es müssen mindestens x andere objekte auf adr sein als ground und flora
				continue;
			
			String objectstring="";
			
        	for(CWorldObject wobj : adr.listWorldObjects_Ground)
        	{
        		Boolean bAdd=true;
        		
    			if(objectstring.contains(wobj.theobject.objectId))
    				bAdd=false;
    			
    			if(bAdd && countadr<=5)
        		{
        			countadr+=1;
        			count+=1;
        			objectstring+=";"+wobj.theobject.objectId;
        		}
    			
    			if(countadr>5)
    				break;
        	}
		}
        
		//int fundValue=count*150;
        int fundValue=count*210;
		
		if(fundValue>0)
		{
			fundValue/=2;
			fundValue*=town.grantsDelta;
		}
		
		//int maxfund=worldAddressList.size()*1000;
		//if(fundValue>maxfund)
		//	fundValue=maxfund;
		
		if(fundValue>0)
		{
			town.gameWorld.changeTownMoney(fundValue);
			townStatistics.getCurrentStatistics_Finance().happinessplus+=Math.abs(fundValue);
			infoEvents.add(new CInfoTextEvent(town, "Nice Town", "$"+fundValue, Color.ORANGE));
		}		
	}	
	
	public void calculateGreenFund()
	{
		int count=0;
		int countbonus=0;
		int countbonustreesadr=0;

		int differentBonus=0;
		String differentbonusstring="";

        for(CAddress adr : worldAddressList)
        {
			//if((adr.listWorldObjects.size()-adr.countFlora-adr.listWorldObjects_Ground.size())<3) //es müssen mindestens x andere objekte auf adr sein als ground und flora
			//	continue;
			
			if((adr.listWorldObjects.size()-adr.countFlora)<3) //es müssen mindestens x andere objekte auf adr sein als ground und flora
				continue;
			
        	adr.countTrees=0;
			adr.countFlowers=0;
			countbonustreesadr=0;
			
        	for(CWorldObject wobj : adr.list_GreenTown)
        	{
        		Boolean bAdd=true;
    			
        		if(!differentbonusstring.contains(wobj.theobject.objectId))
        		{
        			differentbonusstring+=";"+wobj.theobject.objectId;
        			differentBonus+=1;
        		}
        		
        		if(bAdd)
        		{
					if(wobj.theobject.editoraction.contains("outdoor_tree"))
					{
						if(wobj.theobject.width<100)
							adr.countTrees+=0.5f;
						else if(wobj.theobject.width<200)
							adr.countTrees+=1f;
						else if(wobj.theobject.width<300)
							adr.countTrees+=1.5f;
						else if(wobj.theobject.width<400)
							adr.countTrees+=2f;
						else if(wobj.theobject.width<600)
							adr.countTrees+=2.5f;
						else
							adr.countTrees+=3f;
					}
					
					if(wobj.theobject.editoraction.contains("outdoor_plant"))
					{
						adr.countFlowers+=0.5f;
					}
					
					if(wobj.theobject.editoraction.contains("outdoor_flower"))
					{
						adr.countFlowers+=0.5f;
					}        			
        		}
        	}
        	
			if(adr.countFlowers>30)
				count+=15f;
			else
				count+=Math.round(adr.countFlowers/2);
			
			if(adr.countTrees>25)
				count+=25;
			else
				count+=adr.countTrees;
		}
        
        count+=countbonus;
        
		//int fundValue=count*25;
        int fundValue=count*60;
		
		if(fundValue>0)
		{
			fundValue/=2;
			fundValue*=town.grantsDelta;
		}
		
		if(differentBonus>5)
			differentBonus=5;
		fundValue+=differentBonus*300;
		
		if(fundValue>0)
		{
			town.gameWorld.changeTownMoney(fundValue);
			townStatistics.getCurrentStatistics_Finance().happinessplus+=Math.abs(fundValue);
			infoEvents.add(new CInfoTextEvent(town, "Green Town", "$"+fundValue, new Color(0,0.8f,0,0.8f)));
		}
		
//		if(differentBonus>1)
//		{
//			if(differentBonus>5)
//				differentBonus=5;
//			
//			//int multifund=differentBonus*1200;
//			int multifund=differentBonus*300;
//			town.gameWorld.changeTownMoney(multifund);
//			townStatistics.getCurrentStatistics_Finance().happinessplus+=Math.abs(multifund);
//			infoEvents.add(new CInfoTextEvent(town, "Green Town Multi", "$"+multifund, new Color(0,0.8f,0,0.8f)));
//		}
		
	}
	
	public void calculateHappinessFund()
	{
		//if(worldTime.hours>6 && worldTime.day+worldTime.hours!=lastHappinessFundDay)
		//if((worldTime.hours==12 && worldTime.day+worldTime.hours!=lastHappinessFundDay) || (worldTime.hours==23 && worldTime.day+worldTime.hours!=lastHappinessFundDay))
		if(worldHumans.size()==0)
			return;
		
		{
			int fundValue=0;
			int avgHappiness=0;
			
			for(CWorldObject wobj : worldHumans)
			{
				int fvtemp = (int) wobj.thehuman.getHappynessValue();
				avgHappiness+=wobj.thehuman.getHappynessValue();
			}
			
			avgHappiness/=worldHumans.size();
						
			//fundValue=Math.round(avgHappiness*350+(worldHumans.size()*300));
			fundValue=Math.round(avgHappiness*350); //(worldHumans.size()*200));
			
			if(fundValue==0)
				return;
			
			//Town Delta
			if(fundValue<0)
				fundValue*=town.penaltyDelta;
			if(fundValue>0)
				fundValue*=town.grantsDelta;
			
			//Gdx.app.debug("", "town.grantsDelta: "+town.grantsDelta + ", fundValue: " + fundValue + ", avgHappiness: " + avgHappiness);
			
			if(fundValue>0)
			{
				fundValue/=4;
				town.gameWorld.changeTownMoney(fundValue);
			}
			
			if(townMoney<0)
				townMoney=0;
			
			if(fundValue<0)
			{
				townStatistics.getCurrentStatistics_Finance().happinessminus+=Math.abs(fundValue);
				infoEvents.add(new CInfoTextEvent(town, "Unhappy Town People", "$"+fundValue, new Color(0.7f,0,0,0.8f)));
			}
			else if(fundValue>0)
			{
				townStatistics.getCurrentStatistics_Finance().happinessplus+=Math.abs(fundValue);
				infoEvents.add(new CInfoTextEvent(town, "Happy Town People", "$"+fundValue, new Color(1,1,0,0.8f)));
			}
		}
	}
	
	public void calculateChildSupportFund()
	{
		//if(worldTime.hours>6 && worldTime.day+worldTime.hours!=lastHappinessFundDay)
		//if((worldTime.hours==12 && worldTime.day+worldTime.hours!=lastHappinessFundDay) || (worldTime.hours==23 && worldTime.day+worldTime.hours!=lastHappinessFundDay))
		{
			//lastHappinessFundDay=worldTime.hours+worldTime.day;
			int fundValue=0;
			for(CWorldObject wobj : worldHumans)
			{
				if(wobj.thehuman.getAge()<18 && !wobj.bIsDead)
					fundValue+=10000/wobj.thehuman.getAge();
			}
			
			if(fundValue==0)
				return;
			
			town.gameWorld.changeTownMoney(fundValue);
			townStatistics.getCurrentStatistics_Finance().childsupport+=Math.abs(fundValue);
			//townMoney+=fundValue;
			
			if(townMoney<0)
				townMoney=0;
			
			//infoEvents.add(new CInfoTextEvent(town, "Child Support", "$" + fundValue, new Color(1,1,0,0.8f)));
			infoEvents.add(new CInfoTextEvent(town, "Child Support", "$" + fundValue, Color.WHITE));
		}
	}
	
	public void spawnNewZombieEntrance()
	{
		if(!town.bZombieApocalypse)
			return;
		
		if(town.gameWorld.worldTime.getCurrentDay()<town.zombieEntranceStartingDay)
			return;
				
		//if(worldZombieEntrances.size()==1) {
		//	if(town.gameWorld.worldTime.getCurrentDay()<town.zombieEntranceStartingDay+5)
		//		return;
		//}
		
		if(worldZombieEntrances.size()>3) {
			if(town.gameWorld.worldTime.getCurrentDay()<town.zombieEntranceStartingDay+2)
				return;
		}

		if(worldZombieEntrances.size()>5) {
			if(town.gameWorld.worldTime.getCurrentDay()<town.zombieEntranceStartingDay+4)
				return;
		}

		if(worldZombieEntrances.size()>7) {
			if(town.gameWorld.worldTime.getCurrentDay()<town.zombieEntranceStartingDay+6)
				return;
		}

		if(worldZombieEntrances.size()>9) {
			if(town.gameWorld.worldTime.getCurrentDay()<town.zombieEntranceStartingDay+8)
				return;
		}
		
		
		if(worldZombieEntrances.size()>10)
		{
			return;
			/*
			for(int i=0;i<3;i++)
			{
				CWorldObject f1 = worldZombieEntrances.get(0);
				worldZombieEntrances.remove(0);
				f1.dispose();
			}
			*/
		}
		
		
		if(worldZombies.size()>30)
			return;
		
		//int maxEntranceCount=town.gameWorld.worldTime.getCurrentDay()/3;
		
		//if(worldZombieEntrances.size()>=maxEntranceCount)
		//	return;
		
		CWorldObject entrance1;
		
		//if(worldZombieEntrances.size()>2) {
		//	entrance1=worldZombieEntrances.get(0);
		//}
		//else {
			entrance1 = gameResourceConfig.createWorldObject("zombie_entrance", rand.nextInt(town.mapsize-2000), rand.nextInt(town.mapsize-2000), null);
		//}
		
		int icount=0;
		
		while(zombieEntranceCollides(entrance1))
		{
			entrance1.setPosition(rand.nextInt(town.mapsize-2000), rand.nextInt(town.mapsize-2000));
			icount++;
			if(icount>100)
			{
				worldZombieEntrances.remove(entrance1);
				return;
			}
		}
		
		//if(worldZombieEntrances.size()==1)
		{
			town.gameCam.position.set(entrance1.pos_x(), entrance1.pos_y(), 0);
			town.gameWorld.bRenderFrameBuffer=true;
			town.gameAudio.playSound("EFFECT_ZOMBIEENTRANCE", 0, false);
		}
	}

	public Boolean zombieEntranceCollides(CWorldObject zombieEntrance)
	{
		for(CWorldObject wobj : worldFootpath)
		{
			if(wobj.collides(zombieEntrance))
				return true;
		}
		
		for(CWorldObject wobj : worldRoad)
		{
			if(wobj.collides(zombieEntrance))
				return true;
		}
				
		
		//for(CWorldObject wobj : worldGroundObjects)
		//{
		//	if(wobj.collides(zombieEntrance))
		//		return true;
		//}
		
		
		for(CWorldObject wobj : worldWaterObjects)
		{
			if(wobj.collides(zombieEntrance))
				return true;
		}
		
		
		//for(CAddress adr : worldAddressList)
		//{
		//	if(adr.testpoint(zombieEntrance.pos_x(), zombieEntrance.pos_y()))
		//		return true;
		//}

		//cfm777
		for(CWorldObject wobj: worldObjects)
		{
			if(wobj.theobject.editoraction.contains("illuminati_defensesystem")) {
				if(wobj.collides(zombieEntrance))
					return true;
			}
		}

		
		return false;
	}
	
	public void spawnNewZombies()
	{
			
		if(!town.bZombieApocalypse)
			return;
		
		if(worldZombieEntrances.size()==0)
			return;
		
				
		if(worldZombies.size()>25 && town.gameMode!="expert")
			return;
		
		if(worldZombies.size()>50 && town.gameMode=="expert")
			return;
		
		
		if(town.gameMode.toLowerCase().contains("design"))
			return;
		
		if(worldTime.getCurrentDay() < town.zombieStartingDay && news_zombieApocalypse==0)
		{
			int count = town.zombieStartingDay-worldTime.getCurrentDay();		
			if(count==1 && worldTime.hours>=0)
			{
				if(news_zombieApocalypse==0 && town.gameWorld.getTownHallIntelligenceDeskIsOnline())
				{
					town.gameWorld.infoEvents.add(new CInfoTextEvent(town, "Zombie Apocalypse is Imminent", "Town Hall Intelligence Desk: ", Color.RED));
					news_zombieApocalypse=1;
				}
			}
			
			return;
		}
		
		if(worldTime.hours==spawnNewZombieHour)
		{
			spawnNewZombieHour+=3+rand.nextInt(5);
			
			//Zombies greifen erstmal nur am Tag an
			while(spawnNewZombieHour>18 || spawnNewZombieHour<7)
			{
				spawnNewZombieHour+=rand.nextInt(5);
				
				if(spawnNewZombieHour>23)
					spawnNewZombieHour=6+rand.nextInt(5);
			}
			//--------------------------------------
			
			if(spawnNewZombieHour>23)
			{
				spawnNewZombieHour=0;
				spawnNewZombieHour+=3+rand.nextInt(5);
			}
		}
		else
		{
			if(spawnNewZombieHour<0)
				spawnNewZombieHour=8+rand.nextInt(5);
			
			return;
		}
		
		int mapx=0;
		int mapy=0;
		int rand1 = rand.nextInt(4);
		
		if(rand1==0) //oben
		{
			mapy=mapsize;
			mapx=rand.nextInt(mapsize);
		}
		if(rand1==1) //unten
		{
			mapy=0;
			mapx=rand.nextInt(mapsize);
		}
		if(rand1==2) //links
		{
			mapx=0;
			mapy=rand.nextInt(mapsize);
		}
		if(rand1==3) //rechts
		{
			mapx=mapsize;
			mapy=rand.nextInt(mapsize);
		}
		
		//int zombiecount=worldTime.getCurrentDay()-town.zombieStartingDay;
		//int diff=(worldTime.getCurrentDay()-town.zombieStartingDay);
		//diff=1+diff*2;
		//diff=1+diff;
		
		//int zombiecount=worldTime.getCurrentDay() + diff;
		int zombiecount=0; //worldTime.getCurrentDay();
		
		if(zombiecount==0)
			zombiecount=1;
		
		//int addz=0;
		//if(town.gameMode=="expert"){
		//	addz=3;
		//}
		
		if(worldTime.getCurrentDay() == town.zombieEntranceStartingDay)// zombieStartingDay)
			zombiecount=1;
		
		if(worldTime.getCurrentDay() == town.zombieEntranceStartingDay+1)
			zombiecount=2;
		
		if(worldTime.getCurrentDay() == town.zombieEntranceStartingDay+2)
			zombiecount=5;
		
		if(worldTime.getCurrentDay() == town.zombieEntranceStartingDay+3)
			zombiecount=6;
		
		if(worldTime.getCurrentDay() == town.zombieEntranceStartingDay+4)
			zombiecount=7;
		
		if(worldTime.getCurrentDay() > town.zombieEntranceStartingDay+5)
			zombiecount=8;

		if(worldTime.getCurrentDay() > town.zombieEntranceStartingDay+6)
			zombiecount=9;

		if(worldTime.getCurrentDay() > town.zombieEntranceStartingDay+7)
			zombiecount=10;

		if(worldTime.getCurrentDay() > town.zombieEntranceStartingDay+8)
			zombiecount=12;

		if(worldTime.getCurrentDay() > town.zombieEntranceStartingDay+9)
			zombiecount=15;

		if(worldTime.getCurrentDay() > town.zombieEntranceStartingDay+10)
			zombiecount=17;
		
		if(worldTime.getCurrentDay() > town.zombieEntranceStartingDay+11)
			zombiecount=19;
		
		if(worldTime.getCurrentDay() > town.zombieEntranceStartingDay+12)
			zombiecount=22;

		if(worldTime.getCurrentDay() > town.zombieEntranceStartingDay+13)
			zombiecount=24;
		
		if(worldTime.getCurrentDay() > town.zombieEntranceStartingDay+15)
			zombiecount=25;
		
		int ieinerichtung=rand.nextInt(2);
		
		//Gdx.app.debug("spawnzombie", "zombiecount: "+zombiecount);
		
		for(int i=0;i<zombiecount;i++)
		{
			if(ieinerichtung==0)
			{
				rand1 = rand.nextInt(4);
				
				if(rand1==0) //oben
				{
					mapy=mapsize;
					mapx=rand.nextInt(mapsize);
				}
				if(rand1==1) //unten
				{
					mapy=0;
					mapx=rand.nextInt(mapsize);
				}
				if(rand1==2) //links
				{
					mapx=0;
					mapy=rand.nextInt(mapsize);
				}
				if(rand1==3) //rechts
				{
					mapx=mapsize;
					mapy=rand.nextInt(mapsize);
				}				
			}
			
			
			Boolean gend=rand.nextBoolean();
			String sgend="";
			
			if(gend)
				sgend="m";
			else 
				sgend="w";			
			
			int index = rand.nextInt(worldZombieEntrances.size());
			CWorldObject entr = worldZombieEntrances.get(index);
			
			//Gdx.app.setLogLevel(10);
			//Gdx.app.debug("", "spawn zombie");
			
			createRandomCitizen(sgend, CHuman.generateCitizenForename(sgend) + " " + CHuman.generateCitizenLastname(), 5+rand.nextInt(70), entr.pos_x()+entr.width/2, entr.pos_y()+entr.height/2, 1);
			
			//if(town.gameMode=="expert")
		}
	}
	
	public void spawnNewResidents()
	{
		if(worldTime.getCurrentDay()==1) //Am ersten Tag keine neuen Residents spawnen
			return;
		
		if(bNewResidentSpawn==false)
		{
			bNewResidentSpawn=true;
			spawnNewResidentHour=spawnNewResidentHour-(1+rand.nextInt(2));
			
			if(spawnNewResidentHour<0)
				spawnNewResidentHour=23;
		}
		
		if(bNewResidentSpawn)
		{
			if(worldTime.hours==spawnNewResidentHour)
			{
				bNewResidentSpawn = false;
				CWorldObject citizen1 = null;
				
				int spawncount=1;
				Boolean bSpawnFamily=false;
				String sfamilyname="";
				
				if(worldTime.getCurrentDay()>=town.spawnFamiliesStartingDay)
				{
					int r1 = rand.nextInt(5);
					if(r1==0)
					{
						bSpawnFamily=true;
						spawncount=3+rand.nextInt(3);
						sfamilyname = CHuman.generateCitizenLastname();
					}
				}
				
				for(int i=0;i<spawncount;i++)
				{
					int px=0;
					int py=0;
					CAddress adr=null;
					for(int j=0;j<100;j++)
					{
						px = town.mapsize/2+rand.nextInt(5000)-rand.nextInt(5000);
						py = town.mapsize/2+rand.nextInt(5000)-rand.nextInt(5000);;
						adr = getAddressByPoint(px, py);
						if(adr == null)
							break;
					}
					
					if(adr!=null)
						return;
					
					Boolean bMan=rand.nextBoolean();
					String sgender="m";
					
					if(!bMan)
						sgender="w";
					
					int age = 18+rand.nextInt(60);
					
					if(bSpawnFamily && i>1)
						age = 5+rand.nextInt(10);
					
					if(sfamilyname.isEmpty())
						sfamilyname=CHuman.generateCitizenLastname();
					
					citizen1 = createRandomCitizen(sgender, CHuman.generateCitizenForename(sgender) + " " + sfamilyname, age, px, py, 0);
				}
				
				if(spawncount==1)
					town.gameWorld.infoEvents.add(new CInfoTextEvent(town, citizen1.thehuman.getName() + " has just arrived", new Color(1,1,1,0.8f)));
				if(spawncount>1)
				{
					if(bSpawnFamily)
						town.gameWorld.infoEvents.add(new CInfoTextEvent(town, "The " + sfamilyname + " family has just arrived", new Color(1,1,1,0.8f)));
					else
						town.gameWorld.infoEvents.add(new CInfoTextEvent(town, spawncount + " new residents have just arrived", new Color(1,1,1,0.8f)));
				}
			}
		}
	}
	
	public void createRandomBird(int maxsize)
	{
		int px = 0;
		int py = 0; 
		
		if(worldAddressList.size()>0)
		{
			int index = rand.nextInt(worldAddressList.size());
			CAddress targetAdr = worldAddressList.get(index);
			int w=rand.nextInt((int)targetAdr.ex-(int)targetAdr.sx)+rand.nextInt(800)-rand.nextInt(800);
			int h=rand.nextInt((int)targetAdr.ey-(int)targetAdr.sy)+rand.nextInt(800)-rand.nextInt(800);
			px = (int)targetAdr.sx + w;
			py = (int)targetAdr.sy + h;
		}
		else
		{
			px=mapsize/2+rand.nextInt(1500)-rand.nextInt(1500);
			py=mapsize/2+rand.nextInt(1500)-rand.nextInt(1500);
		}
		
		//startingResidentCount
		if(worldHumans.size()>4 && town.gameCam.zoom<10)
		{
			int count=0;
			while(town.gameCam.frustum.pointInFrustum(px, py, 0))
			{
				px = town.rand.nextInt(mapsize);
				py = town.rand.nextInt(mapsize);
				count++;
				if(count>20)
					break;
			}
		}
		
		String sbird = "bird";
		
		CWorldObject b1 = gameResourceConfig.createWorldObject(sbird, px, py, null);
		
		int maxsize1=25+rand.nextInt(20);
		int maxsize2=25+rand.nextInt(45);
		
		if(maxsize>0)
			maxsize2=maxsize;
		
		b1.dynamicwidth = 10+rand.nextInt(maxsize1);
		
		if(b1.theobject.editoraction.contains("bird2"))
			b1.dynamicwidth = 10+rand.nextInt(maxsize2);
		
		b1.dynamicheight = b1.dynamicwidth/3;
		if(b1.theobject.editoraction.contains("bird2"))
			b1.dynamicheight = (int)(b1.dynamicwidth/2.7f);
		
		//Gdx.app.debug("", ""+b1.dynamicheight + ", " + b1.dynamicwidth);
		if(b1.dynamicwidth>35)
		{
			b1.dynamicheight/=1.5f;
			b1.dynamicwidth/=1.5f;
		}
		
		//b1.dynamicheight=(int) town.getSizeValue(b1.dynamicheight);
		//b1.dynamicwidth=(int) town.getSizeValue(b1.dynamicwidth);
		
		b1.setDynamicSize(0);
		
		b1.shadowDistance = b1.width*3;
		
		b1.hposition=b1.width/15;
		//b1.dynamicwidth;//*=b1.hposition;
		//b1.dynamicheight;//*=b1.hposition;
		//b1.setDynamicSize(0);
		
		b1.movementSpeed = 18 - b1.dynamicwidth / 8;
		if(b1.movementSpeed<4)
			b1.movementSpeed=4;
		
		
		//Color
		int col=rand.nextInt(10);
		if(col>5)
		{
			float c1 = rand.nextFloat();
			if(c1>0.3f)
				c1=0.3f;
			if(c1<0.3f)
				c1=0.3f;
			
			b1.color1 = new Color(c1,c1,c1,1);
		}
		else
		{
			float teilerr=2;
			float teilerg=2;
			float teilerb=2;
			
			if(b1.dynamicwidth>90)
			{
				teilerr=5;
				teilerg=5;
				teilerb=5;
			}
			
			float r=rand.nextFloat()/teilerr;
			float g=rand.nextFloat()/teilerg;
			float b=rand.nextFloat()/teilerb;
			
			if(r>0.3f)
				r=0.3f;
			if(g>0.3f)
				g=0.3f;
			if(b>0.3f)
				b=0.3f;

			if(r<0.2f)
				r=0.2f;
			if(g<0.2f)
				g=0.2f;
			if(b<0.2f)
				b=0.2f;
			
			b1.color1 = new Color(r,g,b,1);
		}
	}
	
	public CWorldObject createRandomCitizen(String gender, String sname, int age, int x, int y, int izombie)
	{
		Optional<CObject> baseobject=null;
		Optional<CObject> baseobject2=null;
		List<CObject> listHeads=null;
		
		if(izombie>=1)
			sname+=" (Zombie)";
		
		if(gender.equals("w"))
		{
			listHeads = gameResourceConfig.listObjectHead_Women;
			baseobject = gameResourceConfig.listObjectres.stream().filter(item->item.editoraction.equals("human_woman")).findFirst();
		}
		
		if(gender.equals("m"))
		{
			listHeads = gameResourceConfig.listObjectHead_Men;
			baseobject = gameResourceConfig.listObjectres.stream().filter(item->item.editoraction.equals("human_man")).findFirst();
		}
		
		int index = rand.nextInt(listHeads.size()-1);
		CObject headObject= listHeads.get(index);
		
		baseobject.get().pos_x = (int)x;
		baseobject.get().pos_y = (int)y;
		CWorldObject newWorldObject = new CWorldObject(baseobject.get(), town, true);
		newWorldObject.thehuman = new CHuman(town, newWorldObject, gender.charAt(0));
		newWorldObject.thehuman.setAge(age);
		newWorldObject.thehuman.setName(sname);
		newWorldObject.thehuman.headTextureId=headObject.objectId;
		newWorldObject.initHead(gender, headObject.objectId);
		newWorldObject.iZombie=izombie;
		
		//**************
		//Set Attributes
		//**************
		newWorldObject.thehuman.healthValueMax=rand.nextInt(160);
		if(newWorldObject.thehuman.healthValueMax<80)
			newWorldObject.thehuman.healthValueMax=80;
		newWorldObject.thehuman.setHealthValue(newWorldObject.thehuman.healthValueMax);
		
		newWorldObject.thehuman.happinessValueMax=rand.nextInt(160);
		if(newWorldObject.thehuman.happinessValueMax<80)
			newWorldObject.thehuman.happinessValueMax=80;
		newWorldObject.thehuman.setHappynessValue(newWorldObject.thehuman.happinessValueMax);
		
		newWorldObject.thehuman.setIntelligenceValue(100+rand.nextInt(50)-rand.nextInt(50));
		newWorldObject.thehuman.setFitnessValue(30+rand.nextInt(110));
		
		newWorldObject.thehuman.healthAttitude=CHelper.getRandomFloat(0, 3, rand);
		newWorldObject.thehuman.positiveAttitude=CHelper.getRandomFloat(0, 3, rand);
		
		
		//*************
		//Set Education
		//*************
		if(age>20)
		{
			int maxEducation=1;
			
			if(newWorldObject.thehuman.getIntelligenceValue()>80)
				maxEducation=2;
			
			if(newWorldObject.thehuman.getIntelligenceValue()>100)
				maxEducation=3;
				
			float ed = CHelper.getRandomFloat(0, maxEducation, rand);
			if(ed>maxEducation)
				ed=maxEducation;
			
			newWorldObject.thehuman.setEducationValue(ed);
		}
		else if(age<8)
		{
			newWorldObject.thehuman.setEducationValue(0);
		}
		else
		{
			//Age 8 - 20
			
			int div=10;
			if(age<18)
				div=15;
			if(age<16)
				div=20;
			if(age<10)
				div=25;
			
			float educationmax = age/20;
			float edval = rand.nextFloat()+rand.nextFloat();
			if(edval>educationmax)
				edval=educationmax;
			
			newWorldObject.thehuman.setEducationValue(edval);
		}
				
		
		//***********
		//Set Skills
		//***********
		if(age>17)
		{
			int skill_min=age/2;
			int skill_max=age*2;
			
			if(skill_min>50)
				skill_min=50;
			
			if(skill_max>75)
				skill_max=75;
			
			int skillnr = rand.nextInt(4);
			int tempskillnr1=-1;
			int tempskillnr2=-1;
			
			for(int i=0;i<skillnr;i++)
			{
				float fskill = CHelper.getRandomFloat(skill_min, skill_max, rand);
				int skillCount = gameResourceConfig.listWorkplacesAndTasksWithSkill.size()-1;
				int randomSkillnr=-1;
				
				while(randomSkillnr==-1 || randomSkillnr==tempskillnr1 || randomSkillnr==tempskillnr2)
					randomSkillnr=rand.nextInt(skillCount+1);
				
				if(tempskillnr1 == -1)
					tempskillnr1 = randomSkillnr;
				else if(tempskillnr2 == -1)
					tempskillnr2 = randomSkillnr;
				
				CObject skillobj = gameResourceConfig.listWorkplacesAndTasksWithSkill.get(randomSkillnr);
				CJobSkillClass jsc1 = new CJobSkillClass();
				jsc1.theobject=skillobj;
				jsc1.fskill=fskill;
				int iskill=skillobj.getSkillObjectId();
				newWorldObject.thehuman.jobSkillLevel.put(iskill, jsc1);	
				
				float requireded = skillobj.getRequiredWorkplaceEducation();
				
				if(newWorldObject.thehuman.getEducationValue() < requireded)
				{
					newWorldObject.thehuman.setEducationValue(requireded);
				}
			}
		}
		
		if(newWorldObject.thehuman!=null && newWorldObject.iZombie<1)
			newWorldObject.thehuman.initDemand();
		
		town.gameGui.addWorldObject(newWorldObject, false);
		
		return newWorldObject;
	}
		
	public Boolean isBaseGroundUnderObject(CWorldObject mobject)
	{
		//Prüfen ob sich ein Objekt auf Base Ground befindet
		if(mobject.theobject.isGroundBaseObject)
		{
			CAddress adr =  getAddressByPoint(mobject.pos_x()+mobject.width/2, mobject.pos_y()+mobject.height/2);
			
			if(adr!=null)
			{
				for(CWorldObject obj : adr.listWorldObjects)
				{
					if(obj.theobject.ATTR_BASEREQ>0)
					{
						Boolean bCol = Intersector.overlapConvexPolygons(obj.getBoundingPolygon(IntersectionMode.COLLISION), mobject.getBoundingPolygon(IntersectionMode.COLLISION));
						if(bCol)
							return true;
					}
				}
				
				for(CWorldObject obj : adr.listWorldObjects_Floors)
				{
					if(obj.theobject.ATTR_BASEREQ>0)
					{
						Boolean bCol = Intersector.overlapConvexPolygons(obj.getBoundingPolygon(IntersectionMode.COLLISION), mobject.getBoundingPolygon(IntersectionMode.COLLISION));
						if(bCol)
							return true;
					}
				}
				
			}
		}
		
		return false;
	}
	
	public Boolean isFloorUnderObject(CWorldObject mobject)
	{
		if(mobject.theobject.isRoomObject || mobject.theobject.isGroundObject)
		{
			CAddress adr =  getAddressByPoint(mobject.pos_x()+mobject.width/2, mobject.pos_y()+mobject.height/2);
			if(adr!=null)
			{
				for(CWorldObject obj : adr.listWorldObjects)
				{
					Boolean bCheck=true;
					if(mobject.theobject.isRoomObject && !obj.theobject.isHouseObject())
						bCheck=false;
					if(mobject.theobject.isGroundObject && obj.theobject.isHuman())
						bCheck=false;
					
					if(bCheck)
					{
						Boolean bCol = Intersector.overlapConvexPolygons(obj.getBoundingPolygon(IntersectionMode.COLLISION), mobject.getBoundingPolygon(IntersectionMode.COLLISION));
						if(bCol)
							return true;
					}
					
					if(mobject.theobject.isRoomObject)
					{
						if(checkWindowDoorOnFloor(obj.theobject, mobject))
						{
							return true;
						}
					}
				}
				
				if(mobject.theobject.isGroundObject)
				{
					for(CWorldObject obj : adr.listWorldObjects_Floors)
					{
						Boolean bCol = Intersector.overlapConvexPolygons(obj.getBoundingPolygon(IntersectionMode.COLLISION), mobject.getBoundingPolygon(IntersectionMode.COLLISION));
						if(bCol)
							return true;
					}			
				}
				
			}
		}
		
		return false;
	}
	
	public int isFloorUnderObject_Count(CWorldObject floor)
	{
		int count=0;
		if(floor.theobject.isRoomObject)
		{
			//CAddress adr = getAddressByPolygonInside(mobject.getBoundingPolygon(IntersectionMode.COLLISION));
			CAddress adr = getAddressByPoint(floor.pos_x()+floor.width/2, floor.pos_y()+floor.height/2);
			if(adr!=null)
			{
				for(CWorldObject obj : adr.listWorldObjects)
				{
					if(obj.theobject.isHouseObject())
					{
						//floor ist quadratisch
						//prüfe vier eckpunkte des objekts						
						Boolean bCol = floor.containsObject(obj);
						//Boolean bCol = Intersector.overlapConvexPolygons(obj.getBoundingPolygon(IntersectionMode.COLLISION), floor.getBoundingPolygon(IntersectionMode.COLLISION));
						if(bCol)
						{
							count++;
						}
					}
				}
			}
		}
		
		return count;
	}
	
	public Boolean checkAddressPlacing(Rectangle adrRect)
	{
		if(!town.gameGui.bAddressPlacing && !town.gameGui.bAddressCloning && !town.gameGui.bAddressMoving)
			return false;
		
		//Intersection with other Addresses
		for(CAddress adr : worldAddressList)
		{
			if(town.gameGui.movingAddress!=null && adr.addressId==town.gameGui.movingAddress.addressId)
				continue;
			
			if(Intersector.overlaps(adrRect, adr.getBoundingRect(CAddress.AddressOverlap.RESIZE)))
			{
				return false;
			}
		}
		
		//Address Cloning/Moving and Outside Objects: road, tree..
		if(town.gameGui.bAddressCloning || town.gameGui.bAddressMoving)
		{
			for(CWorldObject wobj : worldObjects)
			{
				if(wobj.theobject.coltype.equals("0") || wobj.theobject.editoraction.contains("bird"))
					continue;
				
				if(town.gameGui.movingAddress!=null && wobj.theaddress!=null && wobj.theaddress.addressId==town.gameGui.movingAddress.addressId)
					continue;
				
				if(Intersector.overlaps(adrRect, wobj.getBoundingPolygon(IntersectionMode.COLLISION).getBoundingRectangle()))
				{
					return false;
				}
			}
			
			
			for(CWorldObject wobj : worldDrawSpecial) //java.util.ConcurrentModificationException
			{
				if(wobj.theobject.coltype.equals("0") || wobj.theobject.editoraction.contains("bird"))
					continue;
				
				if(town.gameGui.movingAddress!=null && wobj.theaddress!=null && wobj.theaddress.addressId==town.gameGui.movingAddress.addressId)
					continue;
				
				if(Intersector.overlaps(adrRect, wobj.getBoundingPolygon(IntersectionMode.COLLISION).getBoundingRectangle()))
				{
					return false;
				}
			}	
			
			for(CWorldObject wobj : worldDefenseWarning)
			{
				if(wobj.theobject.coltype.equals("0") || wobj.theobject.editoraction.contains("bird"))
					continue;
				
				if(town.gameGui.movingAddress!=null && wobj.theaddress!=null && wobj.theaddress.addressId==town.gameGui.movingAddress.addressId)
					continue;
				
				if(Intersector.overlaps(adrRect, wobj.getBoundingPolygon(IntersectionMode.COLLISION).getBoundingRectangle()))
				{
					return false;
				}
			}	
			
			for(CWorldObject wobj : worldWatersystems)
			{
				if(wobj.theobject.coltype.equals("0") || wobj.theobject.editoraction.contains("bird"))
					continue;
				
				if(town.gameGui.movingAddress!=null && wobj.theaddress!=null && wobj.theaddress.addressId==town.gameGui.movingAddress.addressId)
					continue;
				
				if(Intersector.overlaps(adrRect, wobj.getBoundingPolygon(IntersectionMode.COLLISION).getBoundingRectangle()))
				{
					return false;
				}
			}				
			
			for(CWorldObject wobj : worldCars)
			{
				if(wobj.theobject.coltype.equals("0") || wobj.theobject.editoraction.contains("bird"))
					continue;
				
				if(town.gameGui.movingAddress!=null && wobj.theaddress!=null && wobj.theaddress.addressId==town.gameGui.movingAddress.addressId)
					continue;
				
				if(Intersector.overlaps(adrRect, wobj.getBoundingPolygon(IntersectionMode.COLLISION).getBoundingRectangle()))
				{
					return false;
				}
			}	
			
			for(CWorldObject wobj : worldDrawSpecial2)
			{
				if(wobj.theobject.coltype.equals("0") || wobj.theobject.editoraction.contains("bird"))
					continue;
				
				if(town.gameGui.movingAddress!=null && wobj.theaddress!=null && wobj.theaddress.addressId==town.gameGui.movingAddress.addressId)
					continue;
				
				if(Intersector.overlaps(adrRect, wobj.getBoundingPolygon(IntersectionMode.COLLISION).getBoundingRectangle()))
				{
					return false;
				}
			}
			
			for(CWorldObject wobj : worldCarpets)
			{
				if(wobj.theobject.coltype.equals("0") || wobj.theobject.editoraction.contains("bird"))
					continue;
				
				if(town.gameGui.movingAddress!=null && wobj.theaddress!=null && wobj.theaddress.addressId==town.gameGui.movingAddress.addressId)
					continue;
				
				if(Intersector.overlaps(adrRect, wobj.getBoundingPolygon(IntersectionMode.COLLISION).getBoundingRectangle()))
				{
					return false;
				}
			}				
		}
		
		return true;
	}
	
	public void doAddressResizing(float x, float y)
	{
		if(town.gameGui.addressResizing==null || !town.gameGui.bAddressResizing)
			return;
		
		Vector3 vm = new Vector3();
		vm.x=x;
		vm.y=y;
		vm=town.gameCam.unproject(vm);
		
		float sx=town.gameGui.addressResizing.sx;
		float ex=town.gameGui.addressResizing.ex;
		float sy=town.gameGui.addressResizing.sy;
		float ey=town.gameGui.addressResizing.ey;
		
		Vector2 lineStart=new Vector2();
		Vector2 lineEnd=new Vector2();
		
		if(town.gameGui.addressResizingSide==AddressSide.LEFT)
		{
			town.gameGui.addressResizing.sx=vm.x;
			lineStart.x=town.gameGui.addressResizing.sx;
			lineStart.y=sy;
			lineEnd.x=town.gameGui.addressResizing.sx;
			lineEnd.y=ey;
		}
		
		if(town.gameGui.addressResizingSide==AddressSide.RIGHT)
		{
			town.gameGui.addressResizing.ex=vm.x;
			lineStart.x=town.gameGui.addressResizing.ex;
			lineStart.y=sy;
			lineEnd.x=town.gameGui.addressResizing.ex;
			lineEnd.y=ey;
		}
		
		if(town.gameGui.addressResizingSide==AddressSide.TOP)
		{
			town.gameGui.addressResizing.ey=vm.y;
			lineStart.x=sx;
			lineStart.y=town.gameGui.addressResizing.ey;
			lineEnd.x=ex;
			lineEnd.y=town.gameGui.addressResizing.ey;
		}
		
		if(town.gameGui.addressResizingSide==AddressSide.BOTTOM)
		{
			//Gdx.app.debug("", "test");
			town.gameGui.addressResizing.sy=vm.y;
			lineStart.x=sx;
			lineStart.y=town.gameGui.addressResizing.sy;
			lineEnd.x=ex;
			lineEnd.y=town.gameGui.addressResizing.sy;
		}
		
		CAddress overlap = getAddressByPolygonOverlap(town.gameGui.addressResizing.getBoundingPolygon(CAddress.AddressOverlap.RESIZE), town.gameGui.addressResizing.addressId);
		
		if(overlap!=null && overlap.addressId != town.gameGui.addressResizing.addressId)
		{
			town.gameGui.addressResizing.sx=sx;
			town.gameGui.addressResizing.ex=ex;
			town.gameGui.addressResizing.sy=sy;
			town.gameGui.addressResizing.ey=ey;
			town.gameGui.addressResizing=null;
			town.gameGui.bAddressResizing=false;
			
			return;
		}
		
		Boolean boverlap=false;
		for(CWorldObject wo : town.gameGui.addressResizing.listWorldObjects)
		{
			if(wo.isHuman() && (!wo.actionstring1.contains("show_grave") && !wo.actionstring1.contains("show_coffin")))
				continue;
			
			Polygon poly = wo.getBoundingPolygon(IntersectionMode.COLLISION);
			if(Intersector.intersectSegmentPolygon(lineEnd, lineStart, poly))
			{
				boverlap=true;
				break;
			}
		}
		
		if(boverlap==false)
		{
			for(CWorldObject wo : town.gameGui.addressResizing.listWorldObjects_Floors)
			{
				Polygon poly = wo.getBoundingPolygon(IntersectionMode.COLLISION);
				if(Intersector.intersectSegmentPolygon(lineEnd, lineStart, poly))
				{
					boverlap=true;
					break;
				}
			}
		}
		
		Boolean nomoney=false;
		Boolean overmove=false;
		
		if(town.gameGui.addressResizing.sx > town.gameGui.addressResizing.ex || town.gameGui.addressResizing.sy > town.gameGui.addressResizing.ey)
			overmove=true;
		
		//Gdx.app.debug("", "boverlap: " + boverlap + ", nomoney: " + nomoney + ", overmove: " + overmove);
		
		if(boverlap || nomoney || overmove)
		{
			town.gameGui.addressResizing.sx=sx;
			town.gameGui.addressResizing.ex=ex;
			town.gameGui.addressResizing.sy=sy;
			town.gameGui.addressResizing.ey=ey;
			town.gameGui.addressResizing=null;
			town.gameGui.bAddressResizing=false;
		}
		else
		{
			//..
		}
	}
	
	public Boolean checkObjectPlacingList_Outside(List<CWorldObject> list1, CObject tempObject, Boolean bCheckPlacingCount)
	{
		//Check Collision outside and inside of address borders
		for(CWorldObject wobj : list1)
		{
			if(wobj.isHuman())
				continue;
			
			if(markerObject!=null && markerObject.uniqueId==wobj.uniqueId) //keine kollision auf sich selbst prüfen  
				continue;
			
			if(wobj.theobject.editoraction.contains("bird"))
				continue;
			
			if(wobj.theobject.roomtype.contains("outdoor") &&  wobj.theobject.coltype.equals("0")) //zb ground
				continue;
			
			
			//move road, footpath		
			if(tempObject.editoraction.contains("road_road_road") || tempObject.editoraction.contains("footpath"))
			{
				if(wobj.theobject.editoraction.contains("outdoor_tree") || wobj.theobject.editoraction.contains("outdoor_plant") || wobj.theobject.editoraction.contains("outdoor_flower"))
				{
					if(tempObject.testpoint(wobj.pos_x()+wobj.width/2, wobj.pos_y()+wobj.height/2, IntersectionMode.COLLISION))
						return false;
				}
			}
			
			//outdoor
			//			if((tempObject.editoraction.contains("outdoor") || wobj.theobject.editoraction.contains("outdoor")))
			//			{
			//				if(Intersector.overlapConvexPolygons(tempObject.getBoundingPolygon(true, IntersectionMode.COLLISION_SMALL), wobj.getBoundingPolygon(IntersectionMode.COLLISION_SMALL)))
			//				{
			//					return false;
			//				}
			//			}
			
			//Ground/Ground
			//if((tempObject.editoraction.contains("outdoor_ground") && wobj.theobject.editoraction.contains("outdoor_ground")))
			//{
				//if(Intersector.overlapConvexPolygons(tempObject.getBoundingPolygon(bCheckPlacingCount, IntersectionMode.COLLISION_SMALL), wobj.getBoundingPolygon(IntersectionMode.COLLISION_SMALL)))
			//	if(Intersector.overlapConvexPolygons(tempObject.getBoundingPolygon(true, IntersectionMode.COLLISION_SMALL), wobj.getBoundingPolygon(IntersectionMode.COLLISION_SMALL)))
			//	{
			//		return false;
			//	}
			//}
			
			//Outdoor/Outside/Room
			if(tempObject.isRoomObject && (wobj.theobject.roomtype.contains("outside") || wobj.theobject.roomtype.contains("outdoor")))
			{
				if(Intersector.overlapConvexPolygons(tempObject.getBoundingPolygon(bCheckPlacingCount, IntersectionMode.COLLISION_SMALL), wobj.getBoundingPolygon(IntersectionMode.COLLISION_SMALL)))
					return false;
			}
			
			//Outdoor/Outside/Room
			if(wobj.theobject.isRoomObject && (tempObject.roomtype.contains("outside") || tempObject.roomtype.contains("outdoor")))
			{
				if(Intersector.overlapConvexPolygons(tempObject.getBoundingPolygon(bCheckPlacingCount, IntersectionMode.COLLISION_SMALL), wobj.getBoundingPolygon(IntersectionMode.COLLISION_SMALL)))
					return false;
			}
			
			//illuminati defense
			if(tempObject.editoraction.contains("illuminati_defense") && wobj.theobject.editoraction.contains("illuminati_defense"))
			{
				if(Intersector.overlapConvexPolygons(tempObject.getBoundingPolygon(bCheckPlacingCount, IntersectionMode.COLLISION_SMALL), wobj.getBoundingPolygon(IntersectionMode.COLLISION_SMALL)))
					return false;
			}
			
			
			//Car-Tree
			//			if((tempObject.editoraction.contains("traffic_car") && wobj.theobject.editoraction.contains("outside_flora_tree")) || (tempObject.editoraction.contains("outside_flora_tree") && wobj.theobject.editoraction.contains("traffic_car")))
			//			{
			//				if(Intersector.overlapConvexPolygons(tempObject.getBoundingPolygon(bCheckPlacingCount, IntersectionMode.COLLISION_SMALL), wobj.getBoundingPolygon(IntersectionMode.COLLISION_SMALL)))
			//					return false;
			//			}
			
						//Light/Light/Car
			//			if((tempObject.editoraction.contains("traffic_car") || tempObject.editoraction.contains("light")) && (wobj.theobject.editoraction.contains("traffic_car") || wobj.theobject.editoraction.contains("light") ))
			//			{
			//				if(Intersector.overlapConvexPolygons(tempObject.getBoundingPolygon(bCheckPlacingCount, IntersectionMode.COLLISION_SMALL), wobj.getBoundingPolygon(IntersectionMode.COLLISION_SMALL)))
			//					return false;
			//			}
			
						//Car-Car
			//			if(tempObject.editoraction.contains("traffic_car") && wobj.theobject.editoraction.contains("traffic_car"))
			//			{
			//				if(Intersector.overlapConvexPolygons(tempObject.getBoundingPolygon(bCheckPlacingCount, IntersectionMode.COLLISION_SMALL), wobj.getBoundingPolygon(IntersectionMode.COLLISION_SMALL)))
			//					return false;
			//			}
		}
		
		return true;
	}
	
	//Check if placing object is ok at this location
	public Boolean checkObjectPlacing(CObject moveObject, CObject createObject) //moveobject: damit das objekt nicht sich selbst testet
	{
		
		CObject tempObject = town.gameGui.objPlacing;
		
		if(moveObject != null)
			tempObject = moveObject;
		
		if(createObject != null)
			tempObject = createObject;
		
		Boolean bCheckPlacingCount=false;
		if(moveObject == null && createObject == null)
			bCheckPlacingCount=true;
		
		//if(moveObject!=null)
		//	town.gameGui.objPlacing=moveObject.theobject; //Das verursacht Probleme, da moveobject nicht aktuell ist
		//if(markerObject!=null)
		//	town.gameGui.objPlacing=markerObject.theobject; //Das verursacht Probleme, da moveobject nicht aktuell ist
		
		if(tempObject == null)
			return false;
				
		int size1=100;
		if(tempObject.width>size1)
			size1=tempObject.width;
		if(tempObject.height>size1)
			size1=tempObject.height;
		size1+=1000;
		
		if(tempObject.pos_x<size1 || tempObject.pos_x>town.mapsize-size1 || tempObject.pos_y<size1 || tempObject.pos_y>town.mapsize-size1)
			return false;
				
		if(tempObject.isHuman())
			return true;
		
		if(tempObject.roomtype.contains("outdoor") &&  tempObject.coltype.equals("0"))
			return true;
		
		tempObject.tempRoom=null;
				
//		float px = Gdx.input.getX();
//		float py = Gdx.input.getY();
//        Vector3 c0 = new Vector3(px,py,0);
//        Vector3 c1 = gameCamera.unproject(c0);
//		Gdx.app.debug("", "tempObject: " + tempObject.pos_x + ", " + tempObject.pos_y + ", mouse: " +c1.x + ", " + c1.y);
				
		if(!tempObject.editoraction.contains("traffic_car") && !tempObject.editoraction.contains("residential_garage") && !tempObject.editoraction.contains("road_parkingspace"))
		{
			int mov=-1;
			if(tempObject.editoraction.contains("footpath"))
				mov=-1;
			
			if(tempObject.isOnRoad(mov, -1, -1)!=null)
				return false;
		}
		
		//Wall/Wall
		if(tempObject.editoraction.contains("illuminati_defensewall"))
		{
			if(tempObject.isOnDefense(-1, -1, -1)!=null)
				return false;
		}
		
		if(!tempObject.isHuman() && !tempObject.editoraction.contains("residential_garage") && !tempObject.editoraction.contains("road_parkingspace") && !tempObject.editoraction.contains("road_road_road"))
		{
			int mov=-1;
			
			//if(tempObject.editoraction.contains("road_road_road"))
			//	mov=(int) town.getSizeValue(100);
			
			CWorldObject fp = tempObject.isOnFootpath(mov, -1, -1, false);//mov 100 für road auf footpath eingestellt
			
			if(fp!=null)
			{
				return false;
			}
		}
		
		//		//Road nicht doppelt übereinander
		//		if(tempObject.editoraction.contains("road_road_road") && tempObject.isOnRoad(-1, -1, -1))
		//			return false;
		//		
		//		//Footpath nicht doppelt übereinander
		//		if(tempObject.editoraction.contains("road_road_footpath") && tempObject.isOnFootpath(-1, -1, -1, false))
		//			return false;
		//
		//		//Footpath/Road
		//		if(tempObject.editoraction.contains("road_road_footpath") && tempObject.isOnRoad(-1, -1, -1))
		//			return false;
		//
		//		//Footpath/Road
				//if(tempObject.editoraction.contains("road_road_road") && tempObject.isOnFootpath(-1, -1, -1, false))
				//	return false;
		
		{
			CAddress adr =null;
			if(tempObject.isGroundBaseObject)
			{
				if(adr==null)
				{
					adr=getAddressByPoint(tempObject.pos_x+tempObject.width/2, tempObject.pos_y+tempObject.height/2);
					
					if(adr==null)
						return false;
										
					for(CWorldObject wobj : adr.listWorldObjects_Ground)
					{
						if(markerObject!=null && markerObject.uniqueId == wobj.uniqueId)
							continue;
						
						if(Intersector.overlapConvexPolygons(tempObject.getBoundingPolygon(bCheckPlacingCount, IntersectionMode.COLLISION_SMALL2), wobj.getBoundingPolygon(IntersectionMode.COLLISION_SMALL2)))
						{
							return false;
						}
					}
					
					return true;
				}
			}
			else
			{
				adr = getAddressByPolygonInside(tempObject.getBoundingPolygon(bCheckPlacingCount, IntersectionMode.COLLISION));

				//nicht adress objekte können außerhalb platziert werden und da kann auch kollision mit adressobjekten stattfinden
				if(adr==null && !tempObject.isAddressObject())
				{
					adr = getAddressByPolygonOverlap(tempObject.getBoundingPolygon(bCheckPlacingCount, IntersectionMode.COLLISION), -1);				
				}
			}
			
			
			
			if(adr==null || (tempObject.isGroundObject))
			{
				//Vorerst können humans außerhalb adresse verschoben werden
				if(moveObject!=null && moveObject.isHuman())
					return true;
				
				//Wenn nicht auf adresse -> nicht setzen
				if(tempObject.isAddressObject())
					return false;
				
				Boolean bret = checkObjectPlacingList_Outside(worldOutdoorLights, tempObject, bCheckPlacingCount);
				if(!bret)
					return false;
				
				bret = checkObjectPlacingList_Outside(worldObjects, tempObject, bCheckPlacingCount);
				if(!bret)
					return false;
				
				bret = checkObjectPlacingList_Outside(worldDrawSpecial, tempObject, bCheckPlacingCount);
				if(!bret)
					return false;
				
				bret = checkObjectPlacingList_Outside(worldCars, tempObject, bCheckPlacingCount);
				if(!bret)
					return false;
				
				bret = checkObjectPlacingList_Outside(worldDrawSpecial2, tempObject, bCheckPlacingCount);
				if(!bret)
					return false;				
				
				bret = checkObjectPlacingList_Outside(worldDefenseWarning, tempObject, bCheckPlacingCount);
				if(!bret)
					return false;
				
				bret = checkObjectPlacingList_Outside(worldWatersystems, tempObject, bCheckPlacingCount);
				if(!bret)
					return false;
			}
			
			//***********
			//Auf Adresse
			//***********
			else
			{
				if(tempObject.isAddressObject())
				{
					if(adr.addressType.equals("residential") && !tempObject.isResidentialAddressObject())
						return false;
					
					if(adr.addressType.equals("public") && !tempObject.isPublicOrCommercialAddressObject())
					{
						if(moveObject==null || moveObject.isHuman()) //humans kann man auf business address verschieben nur nicht erstellen
							return false;
					}
				}
				
				
				//Server nur auf Companies placen die Workoutput Konto haben
				CCompany tempcomp = adr.getCompany();
				if(tempObject.editoraction.contains("company_anycompany_server") && tempcomp != null && tempcomp.companyHasWorkoutput()<=0)
					return false;
				
				//humans können erstmal auf allen objekten platziert werden
				//es gibt probleme beim platzieren von humans wenn sich andere humans da befinden
				
				//test overlapping with other objects
				Boolean bOverlap=false;
				
				//auskommentiert - für alle
				//if(Integer.parseInt(tempObject.coltype)>0 || tempObject.isFloorObject || !tempObject.isHouseObject() || tempObject.editoraction.contains("interior_light"))
				{
					//Houseobject muss auf floor platziert werden, Objekt muss mit 4 eckpunkten und mittelpunkt jeweils mit einem floor überschneiden
					float[] pVertices=null;
					Boolean middle=false;
					if(tempObject.isHouseObject() || tempObject.ATTR_BASEREQ>0)
					{
						pVertices = tempObject.getBoundingPolygon(false, IntersectionMode.PLACING_ON_FLOOR).getTransformedVertices();
					}
					
					//Room nicht auf Road
					if(tempObject.isRoomObject && tempObject.isOnRoad(-1, -1, -1)!=null)
						return false;
					
					//Garbage Container muss in Road-Nähe
					Boolean bRet=true;
					
					//Base Ground Required
					if(tempObject.ATTR_BASEREQ>0)
					{
						bRet=checkBaseground(tempObject, adr.listWorldObjects_Ground, pVertices);
						if(!bRet)
						{
							return false;
						}
					}
					
					//Door, Window müssen auf Floor geplaced werden
					if((tempObject.editoraction.contains("window") || tempObject.editoraction.contains("object_door")))
					{
						bRet = checkWindowFloorPlacing(tempObject, adr);
						return bRet;
					}
					
					bRet=checkObjectPlacingList(markerObject, tempObject, adr.listWorldObjects, bCheckPlacingCount, pVertices, false);
					if(!bRet)
					{
						return false;
					}
					
					bRet=checkObjectPlacingList(markerObject, tempObject, adr.listWorldObjects_Floors, bCheckPlacingCount, pVertices, true);
					if(!bRet)
					{
						return false;
					}
					
					if(!tempObject.isHouseObject() && !tempObject.editoraction.contains("footpath") && !tempObject.editoraction.contains("road_road_road"))
					{
						bRet=checkObjectPlacingList(markerObject, tempObject, (ArrayList<CWorldObject>)worldRoad, bCheckPlacingCount, pVertices, false);
						if(!bRet)
						{
							return false;
						}
						
						bRet=checkObjectPlacingList(markerObject, tempObject, (ArrayList<CWorldObject>)worldFootpath, bCheckPlacingCount, pVertices, false);
						if(!bRet)
						{
							return false;
						}
					}
				}
			}
		}
		
		return true;
	}
	
	public Vector2[] getWindowDoorVector(CObject window_door)
	{
		Vector2[] arr = new Vector2[6];
		
		//Prüfe Floors auf beiden Seiten
		Vector2 v1 = new Vector2();
		Vector2 v2 = new Vector2();
		
		//Mittelpunkt, oben, unten testen
		Vector2 v3 = new Vector2();
		Vector2 v4 = new Vector2();
		Vector2 v5 = new Vector2();
		Vector2 v6 = new Vector2();
		
		int value=window_door.width;
		
		//0,180: links,rechts
		if(window_door.rotation==0 || window_door.rotation==360 || window_door.rotation==180)
		{
			//links
			v1.x=window_door.pos_x+window_door.width/2-value;
			v1.y=window_door.pos_y+window_door.height/2;
			//rechts
			v2.x=window_door.pos_x+window_door.width/2+value;
			v2.y=window_door.pos_y+window_door.height/2;
			
			//Test ob oben und unten mit abstand auf Floor liegen
			//links oben/unten
			v3.x=v1.x;
			v3.y=v1.y+window_door.height/2;
			v4.x=v1.x;
			v4.y=v1.y-window_door.height/2;
			//rechts oben/unten
			v5.x=v2.x;
			v5.y=v2.y+window_door.height/2;
			v6.x=v2.x;
			v6.y=v2.y-window_door.height/2;
		}
		
		//90,270: oben,unten
		if(window_door.rotation==90 || window_door.rotation==270)
		{
			v1.x=window_door.pos_x+window_door.width/2;
			v1.y=window_door.pos_y+window_door.height/2-value;
			v2.x=window_door.pos_x+window_door.width/2;
			v2.y=window_door.pos_y+window_door.height/2+value;
			
			//Test ob oben und unten mit abstand auf auf Floor liegen
			v3.x=v1.x+window_door.height/2;
			v3.y=v1.y;
			v4.x=v1.x-window_door.height/2;
			v4.y=v1.y;
			
			v5.x=v2.x+window_door.height/2;
			v5.y=v2.y;
			v6.x=v2.x-window_door.height/2;
			v6.y=v2.y;			
		}	

		arr[0]=v1;
		arr[1]=v2;
		arr[2]=v3;
		arr[3]=v4;
		arr[4]=v5;
		arr[5]=v6;		
		
		return arr;
	}
	
	public Boolean checkWindowDoorOnFloor(CObject window_door, CWorldObject floor)
	{
		if(!window_door.editoraction.contains("window") && !window_door.editoraction.contains("object_door"))
		{
			return false;
		}
		
		int movx=0;
		int movy=0;
		
		//Prüfe Floors auf beiden Seiten
		Vector2 v1 = new Vector2();
		Vector2 v2 = new Vector2();
		
		//Mittelpunkt, oben, unten testen
		Vector2 v3 = new Vector2();
		Vector2 v4 = new Vector2();
		Vector2 v5 = new Vector2();
		Vector2 v6 = new Vector2();
		
		Vector2 arr[] = getWindowDoorVector(window_door);
		v1=arr[0];
		v2=arr[1];
		v3=arr[2];
		v4=arr[3];
		v5=arr[4];
		v6=arr[5];
		
		Boolean bObenOK=false;
		Boolean bUntenOK=false;
		
		if(floor.testpoint(v3.x, v3.y, IntersectionMode.DEFAULT) || floor.testpoint(v5.x, v5.y, IntersectionMode.DEFAULT))
			bObenOK=true;
		if(floor.testpoint(v4.x, v4.y, IntersectionMode.DEFAULT) || floor.testpoint(v6.x, v6.y, IntersectionMode.DEFAULT))
			bUntenOK=true;
		
		if(bObenOK || bUntenOK)
			return true;
		
		return false;
	}
	
	public Boolean checkWindowFloorPlacing(CObject tempObject, CAddress adr)
	{
		int movx=0;
		int movy=0;
		
		//Prüfe Floors auf beiden Seiten
		Vector2 v1 = new Vector2();
		Vector2 v2 = new Vector2();
		
		//Mittelpunkt, oben, unten testen
		Vector2 v3 = new Vector2();
		Vector2 v4 = new Vector2();
		Vector2 v5 = new Vector2();
		Vector2 v6 = new Vector2();
		
		Vector2 arr[] = getWindowDoorVector(tempObject);
		v1=arr[0];
		v2=arr[1];
		v3=arr[2];
		v4=arr[3];
		v5=arr[4];
		v6=arr[5];
		
		Boolean bObenOK=false;
		Boolean bUntenOK=false;
		
		for(CWorldObject obj : adr.listWorldObjects_Floors)
		{
			//Darf nicht direkt auf Floor liegen: Mittelpunkt und 2 Außenpunkte testen
			
			//Mittelpunkt
			if(obj.testpoint(tempObject.pos_x+tempObject.width/2, tempObject.pos_y+tempObject.height/2, IntersectionMode.DEFAULT))
			{
				return false;
			}
			
			//links/rechts
			if(tempObject.rotation==0 || tempObject.rotation==360 || tempObject.rotation==180)
			{
				if(obj.testpoint(tempObject.pos_x+tempObject.width/2, tempObject.pos_y+tempObject.height, IntersectionMode.DEFAULT))
				{
					return false;
				}
				if(obj.testpoint(tempObject.pos_x+tempObject.width/2, tempObject.pos_y, IntersectionMode.DEFAULT))
				{
					return false;
				}
			}
			
			//oben/unten
			if(tempObject.rotation==90 || tempObject.rotation==270)
			{
				if(obj.testpoint(tempObject.pos_x+tempObject.width, tempObject.pos_y+tempObject.height/2, IntersectionMode.DEFAULT))
				{
					return false;
				}
				if(obj.testpoint(tempObject.pos_x, tempObject.pos_y+tempObject.height/2, IntersectionMode.DEFAULT))
				{
					return false;
				}				
			}
						
			if(obj.testpoint(v1.x, v1.y, IntersectionMode.DEFAULT))
			{
				CWorldObject wobj = adr.getFloorByPoint((int)v2.x, (int)v2.y);
				if(wobj!=null && wobj.theobject.editoraction.equals(obj.theobject.editoraction))
					return false;
			}
			
			if(obj.testpoint(v2.x, v2.y, IntersectionMode.DEFAULT))
			{
				CWorldObject wobj = adr.getFloorByPoint((int)v1.x, (int)v1.y);
				if(wobj!=null && wobj.theobject.editoraction.equals(obj.theobject.editoraction))
					return false;
			}
			
			if(obj.testpoint(v3.x, v3.y, IntersectionMode.DEFAULT) || obj.testpoint(v5.x, v5.y, IntersectionMode.DEFAULT))
				bObenOK=true;
			if(obj.testpoint(v4.x, v4.y, IntersectionMode.DEFAULT) || obj.testpoint(v6.x, v6.y, IntersectionMode.DEFAULT))
				bUntenOK=true;
		}
		
		//Window/Door nicht übereinander
		for(CWorldObject wobj : adr.listWorldObjects)
		{
			if(markerObject!=null && markerObject.uniqueId==wobj.uniqueId)
				continue;
			
			if(wobj.theobject.editoraction.contains("window") || wobj.theobject.editoraction.contains("_door"))
			{
				Boolean bOverlap = Intersector.overlapConvexPolygons(wobj.getBoundingPolygon(0), tempObject.getBoundingPolygon(false, IntersectionMode.COLLISION));
				if(bOverlap)
					return false;
			}
		}
		
		if(bObenOK && bUntenOK)
			return true;
		
		return false;
	}
	
	public Boolean checkBaseground(CObject tempObject, ArrayList<CWorldObject> list1, float[] pVertices)
	{
		Boolean bMiddleOK=false;
		
		for(CWorldObject obj : list1)
		{
			if(!obj.theobject.isGroundBaseObject || !obj.bObjectIsReady)
				continue;
			
			if(markerObject!=null && markerObject.uniqueId == obj.uniqueId)
				continue;
			
			if(obj.testpoint(tempObject.pos_x+tempObject.width/2, tempObject.pos_y+tempObject.height/2, IntersectionMode.DEFAULT))
				bMiddleOK=true;
			
			Boolean bSet=false;
			for(int i=0;i<pVertices.length;i+=2)
			{
				if(obj.testpoint(pVertices[i], pVertices[i+1], IntersectionMode.DEFAULT))
				{
					pVertices[i]=-1;
					bSet=true;
				}
			}
		}
		
		if(!bMiddleOK)
			return false;
		
		for(int i=0;i<pVertices.length;i+=2)
			if(pVertices[i]>-1)
				return false;
				
		return true;
	}
		
	private Boolean checkObjectPlacingList(CWorldObject markerObject, CObject tempObject, ArrayList<CWorldObject> list1, Boolean bCheckPlacingCount, float[] pVertices, Boolean bCheckVertices)
	{
		Boolean bOverlap=false;
		Boolean bBaseOK=false; //Object hat Baseobject
		
		Boolean bMiddleOK=false;
		int objectcount=0;
		Boolean bDoorWindowOK=false;
		
		for(CWorldObject obj : list1)
		{
			if(markerObject!=null && markerObject.uniqueId == obj.uniqueId)
				continue;
			
			if(obj.isHuman())
				continue;
			
			if(obj.theobject.roomtype.contains("outdoor") &&  obj.theobject.coltype.equals("0")) //zb ground
				continue;
												
			//Footpath, Road, Parkingspace, Garage können unter Human geplaced werden
			//			if(obj.isHuman() &&
			//					(tempObject.editoraction.contains("road_road_footpath") ||
			//					tempObject.editoraction.contains("road_road_road") ||
			//					tempObject.editoraction.contains("road_road_parkingspace") ||
			//					tempObject.editoraction.contains("residential_garage")))			
			//				continue;
			
			//Car/Parkingspace/Garage
			if((tempObject.editoraction.contains("parkingspace") || tempObject.editoraction.contains("garage")) && (obj.theobject.editoraction.contains("traffic_car") || obj.theobject.editoraction.contains("footpath") || obj.theobject.editoraction.contains("road_road_road")))
				continue;
			if((obj.theobject.editoraction.contains("parkingspace") || obj.theobject.editoraction.contains("garage")) && (tempObject.editoraction.contains("traffic_car") || tempObject.editoraction.contains("footpath") || tempObject.editoraction.contains("road_road_road")))
				continue;
			
			//Coltype 0: keine placing kollision
			//if(!obj.theobject.isRoomObject && obj.theobject.coltype.equals("0")) //Birds
			//	continue;
			
			//if(!tempObject.isRoomObject && obj.theobject.coltype.equals("0")) //Birds
			//	continue;
			
			//Coltype 2: placing kollision nur mit Objekten mit gleichem Objekttyp
			//if(!tempObject.isRoomObject && Integer.parseInt(obj.theobject.coltype)==2 && !obj.theobject.editoraction.contains(tempObject.editoraction)) //outdoor lights
			//	continue;
			
			//Ground/Ground
			//if((tempObject.editoraction.contains("outdoor_ground") && obj.theobject.editoraction.contains("outdoor_ground")))
			//{
			//	if(Intersector.overlapConvexPolygons(tempObject.getBoundingPolygon(bCheckPlacingCount, IntersectionMode.COLLISION_SMALL), obj.getBoundingPolygon(IntersectionMode.COLLISION_SMALL)))
			//		return false;
			//}
			
			//Bathmat
			if((tempObject.editoraction.contains("bathroom_bathmat") && obj.theobject.editoraction.contains("bathroom_bathmat")))
			{
				if(Intersector.overlapConvexPolygons(tempObject.getBoundingPolygon(false, IntersectionMode.DEFAULT), obj.getBoundingPolygon(IntersectionMode.DEFAULT)))
					return false;
			}			
			
			//cover light / cover light / cover light hat kein col
			if((tempObject.editoraction.contains("interior_light") && obj.theobject.editoraction.contains("interior_light")))
			{
				if(Intersector.overlapConvexPolygons(tempObject.getBoundingPolygon(false, IntersectionMode.DEFAULT), obj.getBoundingPolygon(IntersectionMode.DEFAULT)))
					return false;
			}
			
			//Road / Footpath
			//if((tempObject.editoraction.contains("road_road_footpath") && obj.theobject.editoraction.contains("road_road_road")) || (tempObject.editoraction.contains("road_road_road") && obj.theobject.editoraction.contains("road_road_footpath")))
			//{
			//	if(Intersector.overlapConvexPolygons(tempObject.getBoundingPolygon(false, IntersectionMode.COLLISION_SMALL), obj.getBoundingPolygon(IntersectionMode.COLLISION_SMALL)))
			//	{
			//		return false;
			//	}
			//}
			
			//Outdoor/Outside/Room
			if(obj.theobject.isRoomObject && (tempObject.roomtype.contains("outside") || tempObject.roomtype.contains("outdoor")))
			{
				IntersectionMode imode = IntersectionMode.COLLISION;
				if(tempObject.editoraction.contains("garage") || tempObject.editoraction.contains("parkingspace"))
					imode=IntersectionMode.DEFAULT;
				
				if(Intersector.overlapConvexPolygons(tempObject.getBoundingPolygon(bCheckPlacingCount, imode), obj.getBoundingPolygon(imode)))
					return false;
			}
			
			if(tempObject.isRoomObject && (obj.theobject.roomtype.contains("outside") || obj.theobject.roomtype.contains("outdoor")))
			{
				IntersectionMode imode = IntersectionMode.COLLISION_SMALL;
				if(obj.theobject.editoraction.contains("garage") || obj.theobject.editoraction.contains("parkingspace"))
					imode=IntersectionMode.DEFAULT;
				
				if(Intersector.overlapConvexPolygons(tempObject.getBoundingPolygon(bCheckPlacingCount, imode), obj.getBoundingPolygon(imode)))
					return false;
			}
			
			//Parking Space nicht auf Room
			//			if(tempObject.isRoomObject && obj.theobject.editoraction.contains("road_road_parkingspace"))
			//			{
			//				if(Intersector.overlapConvexPolygons(tempObject.getBoundingPolygon(bCheckPlacingCount, IntersectionMode.COLLISION), obj.getBoundingPolygon(IntersectionMode.COLLISION)))
			//				{
			//					return false;
			//				}
			//			}
			
			//Parking Space nicht auf Parking Space
			if(tempObject.editoraction.contains("road_road_parkingspace") && tempObject.editoraction.contains(obj.theobject.editoraction))
			{
				if(Intersector.overlapConvexPolygons(tempObject.getBoundingPolygon(bCheckPlacingCount, IntersectionMode.COLLISION_SMALL), obj.getBoundingPolygon(IntersectionMode.COLLISION_SMALL)))
					return false;
			}
			
			//Garage nicht auf Garage
			if(tempObject.editoraction.contains("residential_garage") && tempObject.editoraction.contains(obj.theobject.editoraction))
			{
				if(Intersector.overlapConvexPolygons(tempObject.getBoundingPolygon(bCheckPlacingCount, IntersectionMode.COLLISION_SMALL), obj.getBoundingPolygon(IntersectionMode.COLLISION_SMALL)))
					return false;
			}
			
			//Car-Car, Tree-Tree
			//						if((tempObject.editoraction.contains("traffic_car") || tempObject.editoraction.contains("outside_flora_tree")) && (obj.theobject.editoraction.contains("traffic_car") || obj.theobject.editoraction.contains("outside_flora_tree")))
			//						{
			//							if(Intersector.overlapConvexPolygons(tempObject.getBoundingPolygon(bCheckPlacingCount, IntersectionMode.COLLISION_SMALL), obj.getBoundingPolygon(IntersectionMode.COLLISION_SMALL)))
			//								return false;
			//						}
			
			if(!town.bNoRealEstate)
			{
				//Max eine Firma pro Adresse
				if(tempObject.isPublicOrCommercialAddressObject() && obj.theobject.isPublicOrCommercialAddressObject() && !tempObject.isResidentialAddressObject() && !obj.theobject.isResidentialAddressObject())
				{
					if(tempObject.getCompanyTypeString().contains("company") && obj.theobject.getCompanyTypeString().contains("company"))
					{
						Boolean ctype = (tempObject.getCompanyTypeString().equals(obj.theobject.getCompanyTypeString()));
						if(!ctype)
						{
							if(!tempObject.getCompanyTypeString().contains("anycompany") && !obj.theobject.getCompanyTypeString().contains("anycompany"))
								return false;
						}
					}
				}
			}
			
			//Max Objekte eines Typs
			if(tempObject.editoraction.equals(obj.theobject.editoraction))
				objectcount++;
			
			if(tempObject.maxObjectCount>0)
			{
				if(objectcount>=tempObject.maxObjectCount)
					return false;
			}
			
			//Heizung muss an Wand stehen
			//						if(tempObject.editoraction.contains("radiator") && obj.theobject.isFloorObject)
			//						{
			//							if(obj.testpoint(testPointTop1.x, testPointTop1.y, IntersectionMode.COLLISION))
			//								radTopCol=true;
			//							if(obj.testpoint(testPointTop2.x, testPointTop2.y, IntersectionMode.COLLISION))
			//								radTopCol=true;
			//							
			//							if(obj.testpoint(testPointBottom1.x, testPointBottom1.y, IntersectionMode.COLLISION))
			//								radBottomCol=true;
			//							if(obj.testpoint(testPointBottom2.x, testPointBottom2.y, IntersectionMode.COLLISION))
			//								radBottomCol=true;
			//						}
			
			if(bCheckVertices)
			{
				//Houseobject muss auf Floor
				if(tempObject.isHouseObject() && obj.theobject.isRoomObject)
				{
					Boolean roomOK=false;
					
					if(obj.theobject.checkRoomType(tempObject))
						roomOK=true;
					
					//if(town.bConstructionMode && !obj.bObjectIsReady)
					//	roomOK=false;
					
					//					//Roomtype
					//					if((obj.theobject.editoraction.contains(tempObject.getRoomType())) || tempObject.getRoomType().isEmpty())
					//						roomOK=true;
					//					
					//					//Any Room
					//					if(tempObject.editoraction.contains("anyroomresidential"))
					//					{
					//						roomOK=true;
					//					}
					//					
					//					//Any Company Office Object -> auf allen Office Floors platzierbar
					//					if(tempObject.editoraction.contains("anycompany") && 
					//							tempObject.editoraction.contains("office") && 
					//							obj.theobject.editoraction.contains("office"))
					//					{
					//						roomOK=true;
					//					}
					
					if(roomOK)
					{
						if(!bMiddleOK)
						{
							if(obj.testpoint(tempObject.pos_x+tempObject.width/2, tempObject.pos_y+tempObject.height/2, IntersectionMode.DEFAULT))
								bMiddleOK=true;
						}
						
						int itol=20;
						//Gdx.app.debug("before check placing on "+ obj.theobject.editoraction , ""+pVertices[0] + ", " + pVertices[2] + ", " + pVertices[4] + ", " + pVertices[6]);
												
						
						Boolean bSet=false;
						
						for(int i=0;i<pVertices.length;i+=2)
						{
							if(obj.testpoint(pVertices[i], pVertices[i+1], IntersectionMode.DEFAULT))
							{
								pVertices[i]=-1;
								bSet=true;
							}
						}
						
						if(bSet)
						{
							tempObject.tempRoom=obj;
						}
					}
				}
			}
			
			Boolean bcontinue=false; //false: check overlap
			
			//Coltype 0: keine Kollision
			if(Integer.parseInt(obj.theobject.coltype)==0)
				bcontinue=true;
			if(Integer.parseInt(tempObject.coltype)==0)
				bcontinue=true;
			
			//Coltype 2: Kollision nur mit Objekten mit gleichem Objekttyp
			if(Integer.parseInt(obj.theobject.coltype)==2 && !obj.theobject.editoraction.contains(tempObject.editoraction))
				bcontinue=true;
			if(Integer.parseInt(tempObject.coltype)==2 && !obj.theobject.editoraction.contains(tempObject.editoraction))
				bcontinue=true;
			
			//------------------------------------------------
			//			//Objekt kann nicht in building platziert werden
			//			if(obj.theobject.isFloorObject && tempObject.isHouseObject_Or_InhouseAndOutsideObject()==false)
			//				bcontinue=false;
			//			if(tempObject.isFloorObject && obj.theobject.isHouseObject_Or_InhouseAndOutsideObject()==false)
			//				bcontinue=false;
			//------------------------------------------------
						
			//Human kann in Building platziert werden
			
			if(tempObject.isHuman() && obj.theobject.isRoomObject)
				bcontinue=true;
			if(obj.theobject.isHuman() && tempObject.isRoomObject)
				bcontinue=true;
			
			
			//----------------------------------------
			
			
			//if(tempObject.isGroundObject || obj.theobject.isGroundObject)
			//	bcontinue=true;
			if(tempObject.isFloraObject() && obj.theobject.isFloraObject())
				bcontinue=true;						
			if(tempObject.isCar && (obj.theobject.editoraction.contains("road_road_road")))
				bcontinue=true;
			if(obj.theobject.isCar && (tempObject.editoraction.contains("road_road_road")))
				bcontinue=true;
			if(tempObject.isRoomObject && obj.theobject.isRoomObject)
				bcontinue=false;
			
			
			//Road/Footpath - Recyclingmachine
			if(tempObject.editoraction.contains("road_road_road") && obj.theobject.editoraction.contains("company_recyclingcenter_recyclingmachine"))
				continue;
			if(tempObject.editoraction.contains("road_road_road") &&  obj.theobject.editoraction.contains("footpath")) //collision wird schon zu anfang geprüft
				continue;
			if(tempObject.editoraction.contains("footpath") &&  obj.theobject.editoraction.contains("road_road_road")) //collision wird schon zu anfang geprüft
				continue;
			if(tempObject.editoraction.contains("company_recyclingcenter_recyclingmachine") && obj.theobject.editoraction.contains("road_road_road"))
				continue;
			if(tempObject.editoraction.contains("footpath") && obj.theobject.editoraction.contains("company_recyclingcenter_recyclingmachine"))
				continue;
			if(tempObject.editoraction.contains("company_recyclingcenter_recyclingmachine") && obj.theobject.editoraction.contains("footpath"))
				continue;
						
			//Tree-Tree, Flower-Flower
			if(tempObject.editoraction.contains("outdoor_tree") && obj.theobject.editoraction.contains("outdoor_tree"))
				continue;
			if(tempObject.editoraction.contains("outdoor_flower") && obj.theobject.editoraction.contains("outdoor_plant"))
				continue;
						
			
			//ACHTUNG: FALL: BASEOBJECT WIRD ROTIERT WIRD DERZEIT NICHT BERÜCKSICHTIGT
			
			//Object mit Baseobject wird geplaced/gemoved
			if(!bCheckVertices)
			{
				if(!tempObject.sBaseObject.isEmpty() && obj.theobject.editoraction.contains(tempObject.sBaseObject))
				{
					bBaseOK=obj.testpoint(tempObject.pos_x+tempObject.width/2, tempObject.pos_y+tempObject.height/2, IntersectionMode.COLLISION_SMALL);
					bcontinue=true;
				}
			}
			
			//Baseobject wird geplaced zb break room table
			if(!obj.theobject.sBaseObject.isEmpty() && tempObject.editoraction.contains(obj.theobject.sBaseObject))
			{
				int movxy=5;
				
				if(town.gameInput.bMouseIsDragging)
				{
					if(Gdx.input.getX()!=Gdx.input.getDeltaX() || Gdx.input.getY()!=Gdx.input.getDeltaY())
					{
						if(Intersector.overlapConvexPolygons(tempObject.getBoundingPolygon(bCheckPlacingCount, IntersectionMode.COLLISION), obj.getBoundingPolygon(IntersectionMode.COLLISION)))
							obj.setPosition(obj.pos_x()-rand.nextInt(movxy)+rand.nextInt(movxy), obj.pos_y()-rand.nextInt(movxy)+rand.nextInt(movxy));
					}
				}
				
				bcontinue=true;
			}
			
			if(bcontinue)
				continue;
			
			Boolean bCheckOverlap=true;
			
			
			//Tree, Flower nur Mittelpunkt auf Road/Footpath prüfen
			
			//Move Road, Footpath - tree,flower
			if(tempObject.editoraction.contains("road_road_road") || tempObject.editoraction.contains("footpath"))
			{
				if(obj.theobject.editoraction.contains("outdoor_tree") || obj.theobject.editoraction.contains("outdoor_plant") || obj.theobject.editoraction.contains("outdoor_flower"))
				{
					bCheckOverlap=false;
					if(tempObject.testpoint(obj.pos_x()+obj.width/2, obj.pos_y()+obj.height/2, IntersectionMode.COLLISION))
						return false;
				}
			}
			
			//Move Tree, Flower - road,footpath
			if(tempObject.editoraction.contains("outdoor_tree") || tempObject.editoraction.contains("outdoor_plant") || tempObject.editoraction.contains("outdoor_flower"))
			{
				if(obj.theobject.editoraction.contains("road_road_road") || obj.theobject.editoraction.contains("footpath"))
				{
					bCheckOverlap=false;
					if(obj.testpoint(tempObject.pos_x+tempObject.width/2, tempObject.pos_y+tempObject.height/2, IntersectionMode.COLLISION))
						return false;
				}
			}
			
			//Test Object Collision
			Boolean bcol=false;
			if(bCheckOverlap)
			{
				if(tempObject.editoraction.contains("road_road_road") && obj.theobject.editoraction.contains("road_road_road"))
				{
					if(tempObject.isOnRoad(-1, -1 ,-1)!=null)
					{
						return false;
					}
				}
				else if(tempObject.editoraction.contains("footpath") && obj.theobject.editoraction.contains("footpath"))
				{
					if(tempObject.isOnRoad(-1, -1 ,-1)!=null)
					{
						return false;
					}
				}
				else if(tempObject.editoraction.contains("illuminati_defensewall") && obj.theobject.editoraction.contains("illuminati_defensewall"))
				{
					if(tempObject.isOnDefense(-1, -1 ,-1)!=null)
					{
						return false;
					}
				}
				else
				{
					Boolean btest=true;					
					if(tempObject.isHouseObject() && obj.theobject.isFloraObject()) //hausobjekte und bäume nicht testen
						btest=false;
					
					if(btest)
					{
						if(tempObject.editoraction.contains("buildingwall") && obj.theobject.editoraction.contains("buildingwall"))
							bcol = (Intersector.overlapConvexPolygons(tempObject.getBoundingPolygon(bCheckPlacingCount, IntersectionMode.COLLISION_PLACEWALL), obj.getBoundingPolygon(IntersectionMode.COLLISION_PLACEWALL)));
						else
							bcol = (Intersector.overlapConvexPolygons(tempObject.getBoundingPolygon(bCheckPlacingCount, IntersectionMode.COLLISION), obj.getBoundingPolygon(IntersectionMode.COLLISION)));
					}
				}
			}
			
			if(bcol)
			{
				bOverlap=true;
				return false;
			}
		}
		
		if(bCheckVertices)
		{
			//Houseobject muss auf Floor platziert werden
			if(tempObject.isHouseObject())
			{
				if(!bMiddleOK)
					return false;
				
				for(int i=0;i<pVertices.length;i+=2)
					if(pVertices[i]>-1)
						return false;
			}
		}
		else
		{
			if(!tempObject.sBaseObject.isEmpty() && !bBaseOK)
				return false;
		}
		
		if(bOverlap)
		{
			return false;
		}
		
		return true;
	}
	
	public CWorldObject getWorldObjectById(int id, String stype)
	{
		Optional<CWorldObject> obj = null;
		
		if(stype.equals("HUMAN"))
		{
			obj = worldHumans.stream().filter(item->item.uniqueId==id).findFirst();
			if(obj.isPresent())
				return obj.get();
		}
		
		obj = worldObjects.stream().filter(item->item.uniqueId==id).findFirst();		
		if(obj.isPresent())
			return obj.get();
		
		obj = worldDrawSpecial.stream().filter(item->item.uniqueId==id).findFirst();		
		if(obj.isPresent())
			return obj.get();
		
		obj = worldCars.stream().filter(item->item.uniqueId==id).findFirst();		
		if(obj.isPresent())
			return obj.get();
		
		obj = worldDrawSpecial2.stream().filter(item->item.uniqueId==id).findFirst();		
		if(obj.isPresent())
			return obj.get();
		
		obj = worldDefenseWarning.stream().filter(item->item.uniqueId==id).findFirst();		
		if(obj.isPresent())
			return obj.get();

		obj = worldWatersystems.stream().filter(item->item.uniqueId==id).findFirst();		
		if(obj.isPresent())
			return obj.get();
				
		if(!stype.equals("HUMAN"))
		{
			obj = worldHumans.stream().filter(item->item.uniqueId==id).findFirst();
			if(obj.isPresent())
				return obj.get();
		}
		
		obj = worldGarbageContainers.stream().filter(item->item.uniqueId==id).findFirst();
		if(obj.isPresent())
			return obj.get();
		
		obj = worldGroundObjects.stream().filter(item->item.uniqueId==id).findFirst();
		if(obj.isPresent())
			return obj.get();

		
		obj = worldCarpets.stream().filter(item->item.uniqueId==id).findFirst();		
		if(obj.isPresent())
			return obj.get();
		//obj = worldWaterObjects.stream().filter(item->item.uniqueId==id).findFirst();
		//if(obj.isPresent())
		//	return obj.get();
		
		obj = worldFootpath.stream().filter(item->item.uniqueId==id).findFirst();
		if(obj.isPresent())
			return obj.get();
		
		obj = worldRoad.stream().filter(item->item.uniqueId==id).findFirst();
		if(obj.isPresent())
			return obj.get();
		
		obj = worldRoomObjects.stream().filter(item->item.uniqueId==id).findFirst();
		if(obj.isPresent())
			return obj.get();
				
		obj = worldCoverlights.stream().filter(item->item.uniqueId==id).findFirst();
		if(obj.isPresent())
			return obj.get();
		
		obj = worldOutdoorLights.stream().filter(item->item.uniqueId==id).findFirst();
		if(obj.isPresent())
			return obj.get();		
		
		return null;
	}
	
	public void addObjectToPathmap(CWorldObject tempWO, Boolean bAdd)
	{
		if(tempWO==null || tempWO.theobject==null)
			return;
		
		if(tempWO.theobject.editoraction.contains("buildingwall"))
		{
			try
			{
				int px = tempWO.pos_x()+tempWO.width/2;
				int py = tempWO.pos_y()+tempWO.height/2;
				int index1= Math.round(px/town.nodesize + py/town.nodesize * pathmapsize);
				
				if(index1>-1)
				{
					if(bAdd)
						pathmap[index1]=tempWO;
					else
						pathmap[index1]=null;
				}
			}
			catch(Exception e)
			{
				CHelper.writeError(e.getMessage(), e.getStackTrace(), e);
			}
			
			return;
		}
		
		if(tempWO.theobject.editoraction.contains("road_road_road"))
		{
			try
			{
				int px = tempWO.pos_x()+tempWO.width/2;
				int py = tempWO.pos_y()+tempWO.height/2;
				int index1= Math.round(px/town.roadrastersize + py/town.roadrastersize * pathmapsize_road);
				
				if(index1>-1)
				{
					if(bAdd)
						pathmap_road[index1]=tempWO;
					else
						pathmap_road[index1]=null;
				}
			}
			catch(Exception e)
			{
				CHelper.writeError(e.getMessage(), e.getStackTrace(), e);
			}
			
			return;
		}
		
		if(tempWO.theobject.editoraction.contains("road_road_footpath"))
		{
			try
			{
				int px = tempWO.pos_x()+tempWO.width/2;
				int py = tempWO.pos_y()+tempWO.height/2;
				int index1= Math.round(px/town.footpathsize + py/town.footpathsize * pathmapsize_footpath);
				if(index1>-1)
				{
					if(bAdd)
						pathmap_footpath[index1]=tempWO;
					else
						pathmap_footpath[index1]=null;
				}
			}
			catch(Exception e)
			{
				CHelper.writeError(e.getMessage(), e.getStackTrace(), e);
			}
			
			return;
		}
		
		
		if(tempWO.theobject.editoraction.contains("illuminati_defensewall"))
		{
			try
			{
				int px = tempWO.pos_x()+tempWO.width/2;
				int py = tempWO.pos_y()+tempWO.height/2;
				int index1= Math.round(px/town.defensewallsize + py/town.defensewallsize * pathmapsize_defensewall);
				if(index1>-1)
				{
					if(bAdd)
						pathmap_defensewall[index1]=tempWO;
					else
						pathmap_defensewall[index1]=null;
				}
			}
			catch(Exception e)
			{
				CHelper.writeError(e.getMessage(), e.getStackTrace(), e);
			}
			
			return;
		}		
	}
	public void linkWorldObjectAndCompany(CWorldObject tempw)
	{
		//Any Company Objects und Residential&Public Objects werden keiner Company hinzugefügt (-> action objekte über address suchen)
		//Man kann einfacher andere company errichten mit vorhandener basis infrastruktur
		//wenn doch objectinfo darstellung anpassne zb dinner table
		if(tempw.theobject.editoraction.contains("anycompany")) //office server, toiletroom, breakroom, ..
			return;
		
		//Entfernen von Firma
		if(tempw.belongsToCompany!=null)
		{
			tempw.belongsToCompanyId=0;
			tempw.belongsToCompany=null;
		}
		
		//Hinzufügen zu Firma / Firma neu erstellen
		if(tempw.isCompanyObject())
		{
			{
				
				String companyString = tempw.getCompanyTypeString();
				CompanyType ctype = CCompany.getCompanyTypeByEditorActionString(companyString);
				CAddress adr = getAddressByPolygonInside(tempw.getBoundingPolygon(IntersectionMode.COLLISION));
				
				//Es gibt nur eine Illuminati Company mit mehreren Standworten
				Boolean bOnlyOneCompany=false;
				//if(tempw.theobject.editoraction.toLowerCase().contains("illuminati"))
				//	bOnlyOneCompany=true;

				if(town.bNoRealEstate) {
					//	tempcomp=null;
					bOnlyOneCompany=true;
				}
				
				if(adr!=null || bOnlyOneCompany)
				{
					//ermittle company auf adr
					CCompany tempcomp=null;
					for(CCompany ccomp : worldCompanyList)
					{
						//Gdx.app.setLogLevel(10);
						//Gdx.app.debug("", "companyString: " + ctype.name() + "==" + ccomp.getCompanyTypeString());
						
						//if(bOnlyOneCompany && ccomp.getCompanyTypeString().equals(companyString))
						if(bOnlyOneCompany && ccomp.getCompanyTypeString().equals(ctype.name()))
						{
							tempcomp=ccomp;
							break;
						}
						
						if(adr!=null && !bOnlyOneCompany)
						{
							if(ccomp.address_company!=null && adr.addressId==ccomp.address_company.addressId)
							{
								tempcomp=ccomp;
								break;
							}
						}
					}
					
					if(tempcomp!=null)
					{
						if(tempcomp.address_company==null)
							tempcomp.address_company=adr;
						
						tempw.belongsToCompany=tempcomp;
						tempw.belongsToCompanyId=tempcomp.companyId;
					}
					else
					{
						if(tempw.theobject.editoraction.contains("anycompany")) //office server, toiletroom, breakroom
							return;
						
						CCompany newCompany = new CCompany(town, "", adr, tempw.getCompanyTypeString());
						
						if(newCompany.companyType==CompanyType.ARCHITECTURE_BUREAU)
							newCompany.addWorkOutput(town.company_officeworkoutput_max, WorkoutputType.DEFAULT);
						
						tempw.belongsToCompany=newCompany;
						tempw.belongsToCompanyId=newCompany.companyId;
						worldCompanyList.add(newCompany);
						newCompany.setAchievement();
					}
				}
			}
		}
	}
	public void linkAddressAndWorldObject(CWorldObject wobj, Boolean bAdd, Boolean bMoveObject)
	{
		//objects definition file
		//	outdoor: kann überall platziert werden, adresse wird zugewiesen und geändert/entfernt
		//	inhouse: kann nicht draußen geplaced werden
		
		//Bäume nicht clonen, die müssen wachsen
		//Road nicht clonen, Footpath nicht clonen (Raster muss angepasst werden wenn doch)
		//Vehicles werden gecloned
		if(!wobj.isAddressObject() && 
				!wobj.theobject.editoraction.contains("traffic_car") && 
				//!wobj.theobject.editoraction.contains("outdoor")//light 
				!wobj.theobject.roomtype.contains("outdoor")//light
				) 
			return;
		
		
		if(wobj.thehuman!=null)
		{
			if((bMoveObject && bMoveHouse) || bAdd || wobj.theaddress==null)
			{
				CAddress adr = null;
				
				if(wobj.isHuman())
					adr = getAddressByPolygonOverlap(wobj.getBoundingPolygon(IntersectionMode.MOUSECLICK), 0);
				else
					adr = getAddressByPolygonInside(wobj.getBoundingPolygon(IntersectionMode.COLLISION));
				
				if(adr!=null)
				{
					if(!adr.addressType.contains("residential"))
						return;
					
					//Remove Residential Tasks
					if(wobj.theaddress!=null && adr.addressId!=wobj.theaddress.addressId)
						wobj.removeResidentialTasks();
					
					//					if(adr.addressType.equals("residential") && !wobj.theobject.isResidentialAddressObject())
					//						return;
					//
					//					if(adr.addressType.equals("public") && !wobj.theobject.isPublicOrCommercialAddressObject())
					//						return;
					
					Boolean bRemoveOwner=true;
					if(wobj.theaddress!=null && wobj.theaddress.addressId==adr.addressId)
						bRemoveOwner=false;
					
					if(bRemoveOwner)
					{
						removeOwner(wobj, wobj.thehuman.bed);
						removeOwner(wobj, wobj.thehuman.car);
						removeOwner(wobj, wobj.thehuman.wardrobe);
						
						List<CWorldObject> dtables = wobj.thehuman.taskobjects.values().stream().filter(item->item.theobject.editoraction.contains("diningtable")).collect(Collectors.toList());
						if(dtables!=null && dtables.size()>0)
						{
							for(CWorldObject table : dtables)
							{
								removeOwner(wobj, table);
							}
						}
					}
					
					if(wobj.theaddress!=null)
					{
						//worldAddressList.get(wobj.theaddressid).listWorldObjects.remove(wobj.uniqueId);
						wobj.theaddress.removeWorldObject(wobj);
						wobj.theaddress=null;
						wobj.theaddressid=0;
					}					
					
					wobj.theaddressid=adr.addressId;
					wobj.theaddress=adr;
					adr.addWorldObject(wobj);
					
					if(bMoveHouse)
					{
						bMoveHouse=false;
						CInfoTextEvent ievent = new CInfoTextEvent(town, wobj.thehuman.getName() + " moved house to " + adr.addressName, Color.WHITE);
						ievent.moveduration=0.2f;
						ievent.stayduration=2;
						infoEvents.add(ievent);
						
						//animationEvents.add(new CAnimationTextEvent(town, wobj.pos_x(), wobj.pos_y(), 0, AnimationEventType.MOVEHOUSE, gameCamera.zoom));
						//town.gameAudio.play("swoosh1");
					}
				}
				
				return;
			}
		}
		else if(wobj.isCompanyObject())
		{
			//objekt von alter company entfernen
			//gibt es company type auf der adresse? -> add, sonst new company
			
			Boolean remove=false;
			//if(wobj.theobject.editoraction.contains("illuminati_defensesystem"))
			//	remove=true;
			
			if((!bAdd && !bMoveObject) || remove)
			{
				if(wobj.theaddress!=null)
				{
					wobj.theaddress.removeWorldObject(wobj);
					wobj.theaddress=null;
					wobj.theaddressid=0;
				}
			}
			
			if(bAdd || bMoveObject)
			{
				CAddress adr = getAddressByPolygonInside(wobj.getBoundingPolygon(IntersectionMode.COLLISION));
				
				if(adr!=null)
				{
					Boolean bSetAdr=false;
					if(adr.addressType.contains("public"))
						bSetAdr=true;
					
					//if(wobj.theobject.editoraction.contains("illuminati_defensesystem"))
					//	bSetAdr=true;
					
					if(bSetAdr)
					{
						if(wobj.theaddress!=null)
						{
							wobj.theaddress.removeWorldObject(wobj);
							wobj.theaddress=null;
							wobj.theaddressid=0;
						}					
						
						wobj.theaddressid=adr.addressId;
						wobj.theaddress=adr;
						adr.addWorldObject(wobj);
						
						linkWorldObjectAndCompany(wobj);
					}
				}
				
				return;
			}			
		}
		else
		{
			if(bAdd || bMoveObject)
			{
				//CAddress adr = getAddressByPolygonInside(wobj.getBoundingPolygon(IntersectionMode.COLLISION));
				CAddress adr = getAddressByPoint(wobj.pos_x()+wobj.width/2, wobj.pos_y()+wobj.height/2); //für baseground
				
				if(adr!=null)
				{
					Boolean bRemoveOwner=true;
					
					//INFOS ATTRIBUTES
					//	basket
					// 		isoccupied2=waschmaschine/trockner
					//		nicht: actionvar1=id waschmaschine/trockner
					//	waschmaschine/trockner
					//		actionvar1=1 fertig
					//  	isoccupied2=basket
					//		nicht: actionvar2=id basket - wozu?					
					
					//Reset linked Objects
					if(wobj.theaddress!=null && adr.addressId!=wobj.theaddress.addressId)
					{
						if(wobj.theobject.editoraction.contains("laundrybasket") ||
								wobj.theobject.editoraction.contains("bathroom_washingmachine") ||
								wobj.theobject.editoraction.contains("laundryroom_dryer")
								)
						{
							wobj.objectFillingMulti.clear();
							wobj.actionvar1=0;
							
							//reset link basket/washingmachine/dryer
							if(wobj.isOccupiedBy2!=null) 
							{
								wobj.isOccupiedBy2.actionvar1=0;
								wobj.isOccupiedBy2.objectFillingMulti.clear();
								wobj.isOccupiedBy2.isOccupiedBy2=null;
								wobj.isOccupiedBy2=null;
							}
						}
					}
					
					//Remove Residential Tasks
					if(wobj.theaddress!=null && adr.addressId!=wobj.theaddress.addressId)
					{
						if(wobj.isResidentialTaskObject())
						{
							if(wobj.worker!=null)
							{
								wobj.worker.thehuman.taskobjects.remove(wobj.uniqueId);
								wobj.worker=null;
							}

							if(wobj.worker2!=null)
							{
								wobj.worker2.thehuman.taskobjects.remove(wobj.uniqueId);
								wobj.worker2=null;
							}
						}
					}
					
					if(wobj.theaddress!=null && wobj.theaddress.addressId==adr.addressId)
						bRemoveOwner=false;
					
					if(wobj.theaddress!=null)
					{
						//worldAddressList.get(wobj.theaddressid).listWorldObjects.remove(wobj.uniqueId);
						wobj.theaddress.removeWorldObject(wobj);
						wobj.theaddress=null;
						wobj.theaddressid=0;
					}					
					
					if(wobj.isOwnerObject()>0 && bRemoveOwner)
					{
						//owner: bed, car, dining table
						if(!wobj.theobject.editoraction.contains("traffic_car_residential"))
							removeAllOwnerAndWorker(wobj);
					}
					
					//Dinner Table ist speziell - kein owner object
					if(wobj.theobject.editoraction.contains("diningtable") && bRemoveOwner)
						removeAllOwnerAndWorker(wobj);
					
					wobj.theaddressid=adr.addressId;
					wobj.theaddress=adr;
					adr.addWorldObject(wobj);
					
					return;
				}
				else
				{
					if(wobj.theobject.roomtype.contains("outdoor"))
					{
						if(wobj.theaddress!=null)
						{
							wobj.theaddress.removeWorldObject(wobj);
							wobj.theaddress=null;
							wobj.theaddressid=0;							
						}
					}
				}
			}
		}
	}
	
	public void removeOwner(CWorldObject owner, CWorldObject object)
	{
		if(owner==null || object==null)
			return;

		if(object.theobject.editoraction.contains("bedroom_bed"))
		{
			//if(owner.thehuman.bed.owner!=null && owner.thehuman.bed.owner.uniqueId==owner.uniqueId)
			//	owner.thehuman.bed.owner=null;
			//if(owner.thehuman.bed.owner2!=null && owner.thehuman.bed.owner2.uniqueId==owner.uniqueId)
			//	owner.thehuman.bed.owner2=null;
			
			owner.thehuman.bed=null;
		}
		
		if(object.theobject.editoraction.contains("traffic_car_residential"))
		{
			//owner.thehuman.car.owner=null;
			owner.thehuman.car=null;
		}

		if(object.theobject.editoraction.contains("bedroom_wardrobe"))
		{
			//owner.thehuman.wardrobe.owner=null;
			owner.thehuman.wardrobe=null;
		}
		
		if(object.theobject.editoraction.contains("diningroom_diningtable"))
		{
			Boolean remove=true;
			if(object.worker!=null && object.worker.uniqueId==owner.uniqueId)
				remove=false;
			
			if(remove)
				owner.thehuman.taskobjects.remove(object.uniqueId);
		}
		
		if(object.owner!=null && object.owner.uniqueId==owner.uniqueId)
			object.owner=null;
		if(object.owner2!=null && object.owner2.uniqueId==owner.uniqueId)
			object.owner2=null;
		if(object.owner3!=null && object.owner3.uniqueId==owner.uniqueId)
			object.owner3=null;
		if(object.owner4!=null && object.owner4.uniqueId==owner.uniqueId)
			object.owner4=null;
		if(object.owner5!=null && object.owner5.uniqueId==owner.uniqueId)
			object.owner5=null;
		if(object.owner6!=null && object.owner6.uniqueId==owner.uniqueId)
			object.owner6=null;
		if(object.owner7!=null && object.owner7.uniqueId==owner.uniqueId)
			object.owner7=null;
		if(object.owner8!=null && object.owner8.uniqueId==owner.uniqueId)
			object.owner8=null;
	}
	
	public void removeWorker(CWorldObject worker, CWorldObject workplace)
	{
		if(workplace==null || (worker==null))
			return;
		
		if(workplace.worker != null && workplace.worker.uniqueId==worker.uniqueId)
			workplace.worker=null;

		if(workplace.worker2 != null && workplace.worker2.uniqueId==worker.uniqueId)
			workplace.worker2=null;
		
		if(workplace.isTaskObject())
		{
			Boolean remove=true;
			
			if(workplace.theobject.editoraction.contains("diningroom_diningtable")) //Wenn als owner hinterlegt nicht aus tasklist entfernen (dinner table)
			{
				if(workplace.owner!=null && workplace.owner.uniqueId==worker.uniqueId)
					remove=false;
				if(workplace.owner2!=null && workplace.owner2.uniqueId==worker.uniqueId)
					remove=false;
				if(workplace.owner3!=null && workplace.owner3.uniqueId==worker.uniqueId)
					remove=false;
				if(workplace.owner4!=null && workplace.owner4.uniqueId==worker.uniqueId)
					remove=false;
				if(workplace.owner5!=null && workplace.owner5.uniqueId==worker.uniqueId)
					remove=false;
				if(workplace.owner6!=null && workplace.owner6.uniqueId==worker.uniqueId)
					remove=false;
				if(workplace.owner7!=null && workplace.owner7.uniqueId==worker.uniqueId)
					remove=false;
				if(workplace.owner8!=null && workplace.owner8.uniqueId==worker.uniqueId)
					remove=false;
			}
			
			if(remove)
				worker.thehuman.taskobjects.remove(workplace.uniqueId);
		}
		else
		{
			worker.thehuman.workplaces.remove(workplace.uniqueId);
		}		
	}
	
	public void removeAllOwnerAndWorker(CWorldObject wobj)
	{
		if(wobj.owner!=null)
			removeOwner(wobj.owner, wobj);
		if(wobj.owner2!=null)
			removeOwner(wobj.owner2, wobj);
		if(wobj.owner3!=null)
			removeOwner(wobj.owner3, wobj);
		if(wobj.owner4!=null)
			removeOwner(wobj.owner4, wobj);
		if(wobj.owner5!=null)
			removeOwner(wobj.owner5, wobj);
		if(wobj.owner6!=null)
			removeOwner(wobj.owner6, wobj);
		if(wobj.owner7!=null)
			removeOwner(wobj.owner7, wobj);
		if(wobj.owner8!=null)
			removeOwner(wobj.owner8, wobj);
		
		if(wobj.worker!=null)
			removeWorker(wobj.worker, wobj);
			//removeOwner(wobj.worker, wobj);
		if(wobj.worker2!=null)
			removeWorker(wobj.worker2, wobj);
			//removeOwner(wobj.worker2, wobj);
	}
	
	public void unlinkWorkerAndWorkplace(CWorldObject worker)
	{
		for(CWorldObject wp : worker.thehuman.taskobjects.values())
		{
			if(wp.worker!=null && wp.worker.uniqueId==worker.uniqueId)
				wp.worker=null;
			if(wp.worker2!=null && wp.worker2.uniqueId==worker.uniqueId)
				wp.worker2=null;
						
			if(wp.owner!=null && wp.owner.uniqueId==worker.uniqueId)
				wp.owner=null;
			if(wp.owner2!=null && wp.owner2.uniqueId==worker.uniqueId)
				wp.owner2=null;
			if(wp.owner3!=null && wp.owner3.uniqueId==worker.uniqueId)
				wp.owner3=null;
			if(wp.owner4!=null && wp.owner4.uniqueId==worker.uniqueId)
				wp.owner4=null;
			if(wp.owner5!=null && wp.owner5.uniqueId==worker.uniqueId)
				wp.owner5=null;
			if(wp.owner6!=null && wp.owner6.uniqueId==worker.uniqueId)
				wp.owner6=null;
			if(wp.owner7!=null && wp.owner7.uniqueId==worker.uniqueId)
				wp.owner7=null;
			if(wp.owner8!=null && wp.owner8.uniqueId==worker.uniqueId)
				wp.owner8=null;
		}
		
		worker.thehuman.taskobjects.clear();
		
		for(CWorldObject wp : worker.thehuman.workplaces.values())
		{
			if(wp.worker!=null && wp.worker.uniqueId==worker.uniqueId)
				wp.worker=null;
			
			if(wp.worker2!=null && wp.worker2.uniqueId==worker.uniqueId)
				wp.worker2=null;
		}
		
		worker.thehuman.workplaces.clear();
	}
	public void linkWorkerAndWorkplace(CWorldObject chosen, CWorldObject workplace, int nr)
	{
		if(workplace.isTaskObject())
		{
			if(workplace.worker!=null && nr==1)
				workplace.worker.thehuman.taskobjects.remove(workplace.uniqueId);

			if(workplace.worker2!=null && nr==2)
				workplace.worker2.thehuman.taskobjects.remove(workplace.uniqueId);
			
			chosen.thehuman.addTaskobject(workplace);
			workplace.setWorker(chosen, nr);
		}
		else
		{
			if(workplace.worker!=null && nr==1)
				workplace.worker.thehuman.workplaces.remove(workplace.uniqueId);

			if(workplace.worker2!=null && nr==2)
				workplace.worker2.thehuman.workplaces.remove(workplace.uniqueId);
			
			chosen.thehuman.addWorkplace(workplace);
			workplace.setWorker(chosen, nr);
		}
	}
	
	public void moveObjectEnd()
	{
		if(!checkObjectPlacing(markerObject.theobject, null))
		{
			markerObject.setPosition(town.gameGui.moveObject_origin_x, town.gameGui.moveObject_origin_y);
			markerObject.setRotation(town.gameGui.moveObject_origin_rot);
			town.gameGui.objPlacing=null;
		}
		
		markerObject.theroom = markerObject.theobject.tempRoom;
		markerObject.setTempSize(0);
		
		addObjectToPathmap(markerObject, true);
		linkAddressAndWorldObject(markerObject, false, true);
		town.gameGui.objPlacing=null;
		town.gameGui.bObjMovement=false;
		markerObject.bObjMoving=false;
		
		if(markerObject.pos_x()!=town.gameGui.moveObject_origin_x || markerObject.pos_y()!=town.gameGui.moveObject_origin_y)
		{
			markerObject.resetPathFinding();
			markerObject.cancelAction1();
		}
		
		dropObject();
		
		bRenderFrameBuffer=true;
	}
	
	public void dropObject()
	{
		//town.gameAudio.playSound("EFFECT_DROP", 0, false);
	}
	
	public Boolean moveObjectStart(int x, int y)
	{
		if(markerObject.isHuman() && markerObject.bIsDead)
			return false;
		
		if(markerObject.theobject.editoraction.contains("illuminati_defense"))
			return false;
		
		Boolean bCol=false;
		
		//Floor/Ground darf nicht gemoved werden, wenn Houseobject drauf ist
		//Boolean bCol = isFloorUnderObject(markerObject);
		//if(bCol)
	//		return false;
		
		bCol = isBaseGroundUnderObject(markerObject);
		if(bCol)
			return false;
		
		Vector3 c0 = new Vector3(x, y, 0);
		Vector3 c1 = town.gameCam.unproject(c0);
		
		town.gameGui.moveX = (int) (c1.x-markerObject.pos_x()); //"Jump" des Objekts zum Cursor verhindern
		town.gameGui.moveY = (int) (c1.y-markerObject.pos_y());
		
		//Gdx.app.debug("", ""+gameGui.moveX + ", " + gameGui.moveY);
		
		town.gameGui.bObjMovement=true;
		markerObject.bObjMoving=true;
		markerObject.theobject.placingCountLevel=0;
		
		town.gameGui.moveObject_origin_x=markerObject.pos_x();
		town.gameGui.moveObject_origin_y=markerObject.pos_y();
		town.gameGui.moveObject_origin_rot=(int)markerObject.rotation();
		
		addObjectToPathmap(markerObject, false); //Wenn Objekt verschoben oder rotiert wird pathmap eintragungen des objekts entfernen
		
		markerObject.setTempSize(1);
		
		
		//if(town.gameWorld.markerObject!=null)
		//	Gdx.app.debug("moveObjectStart log marker 3", "markerObject.pos_x(): " + town.gameWorld.markerObject.pos_x() + ", markerObject.pos_y(): " + town.gameWorld.markerObject.pos_y());

		
		//Gdx.app.debug("test123_start", "markerObject.pos_x(): " + markerObject.pos_x() + ", markerObject.pos_y(): " + markerObject.pos_y());
		
		//markerObject.resetPathFinding();
		//markerObject.cancelAction();
		
		return true;
	}
//	public CAddress getAddressByLocation(int x, int y)
//	{
//		for(CAddress adr : worldAddressList)
//		{
//			if(x>=adr.sx && x<=adr.ex && y>=adr.sy && y<=adr.ey)
//				return adr;
//		}
//		
//		return null;
//	}
//	public CAddress getAddressByLocation(int x, int y, int width, int height)
//	{
//		for(CAddress adr : worldAddressList)
//		{
//			int size=width;
//			if(height>width)
//				size=height;
//			int size2=50;
//			
//			if(x+size2>=adr.sx && x-size2+size/2<=adr.ex && y+size2+size/2>=adr.sy && y-size2+size/2<=adr.ey)
//				return adr;
//		}
//		
//		return null;
//	}
	
	public void changeTownMoney(int money)
	{
		townMoney+=money;
		if(townMoney<0)
			townMoney=0;
		
		//town.gameAudio.play("cash1");
		//town.gameAudio.play("place1");
		
	}
	
	private void sellList(ArrayList<CWorldObject> list)
	{
		for(CWorldObject wobj : list)
		{
			if(wobj.thehuman!=null)
			{
				wobj.theaddress=null;
				continue;
			}
			
			townStatistics.getCurrentStatistics_Finance().sellObject += Math.abs(wobj.theobject.getSellingPrice()); //muss hier oben stehen nicht nachdem wobj entfernt wurde
			
			if(wobj.belongsToCompany!=null)
			{
				if(worldCompanyList.contains(wobj.belongsToCompany))
					worldCompanyList.remove(wobj.belongsToCompany);
				
				//wobj.belongsToCompany.listWorldObjects.clear();
				wobj.belongsToCompany=null;
			}
			
			removeAllOwnerAndWorker(wobj);
			
			if(wobj.theobject.isRoomObject)
				town.gameGui.removeRoomObject(null, wobj, true);
			else
				town.gameGui.removeWorldObject(wobj, true);
		}	
	}
	
	
	public void sellAddress(CAddress address)
	{
		int sellingprice = address.getSellingPrice("1");
		
		sellList(address.listWorldObjects_Floors);
		sellList(address.listWorldObjects_Ground);
		sellList(address.listWorldObjects);
		
		address.listWorldObjects_Floors.clear();
		address.listWorldObjects_Ground.clear();
		address.listWorldObjects.clear();
		
		worldAddressList.remove(address);
		
		changeTownMoney(sellingprice);
		townStatistics.getCurrentStatistics_Finance().sellAddress+=Math.abs(address.getSellingPrice("0"));
		
		//townMoney+=sellingprice;
		//animationEvents.add(new CAnimationTextEvent(town, (int)address.sx, (int)address.sy, sellingprice, AnimationEventType.MONEY, gameCamera.zoom));
	}
	
	public CAddress getAddressByPoint(int x, int y)
	{
		for(CAddress adr : worldAddressList)
		{
			if(x>adr.sx && x<adr.ex && y>adr.sy && y<adr.ey)
				return adr;
		}
		
		return null;
	}
	
	public CAddress getAddressByPolygonInside(Polygon p1)
	{
		float[] verts = p1.getTransformedVertices();
		for(CAddress adr : worldAddressList)
		{
			//Rectangle adrRect = adr.getBoundingRect();
			Boolean bIsIn=true;
			
			//Gdx.app.debug("adr", adr.sx + ", " + adr.sy + ", " + adr.ex + ", " + adr.ey);
			
			for(int i=0;i<verts.length;i+=2)
			{
				int x=(int)verts[i];
				int y=(int)verts[i+1];
				bIsIn=(x>=adr.sx && x<=adr.ex && y>=adr.sy && y<=adr.ey);
				
				if(!bIsIn)
				{
					break;
				}
			}
			
			if(bIsIn)
				return adr;
		}
		
		return null;	
	}
	
	public CAddress getAddressByPolygonOverlap(Polygon p1, int notthisid)
	{
		for(CAddress adr : worldAddressList)
		{
			if(adr.addressId==notthisid)
				continue;
			
			if(Intersector.overlapConvexPolygons(p1, adr.getBoundingPolygon(CAddress.AddressOverlap.DEFAULT)))
				return adr;
			
			// if(x+size2>=adr.sx && x-size2+size/2<=adr.ex && y+size2+size/2>=adr.sy && y-size2+size/2<=adr.ey)
			//	return adr;
		}
		
		return null;	
	}
	
	public float getAmbientLightValue(CTime wtime)
	{
		float deltaMinutes = (float)wtime.minutes/600;
		//komplett hell: 9-16
		//komplett dunkel: 23-4
		
		float lvalue=1.0f;
		
		switch (wtime.hours) {
	        case 0:
	        	lvalue= 0.05f;
	        	break;
	        	//return 0.01f+deltaMinutes/3;
	        case 1:
	        	lvalue= 0.05f;
	        	break;
	        	//return 0.05f+deltaMinutes/3;
	        case 2:
	        	lvalue= 0.07f;
	        	break;
	        	//return 0.07f+deltaMinutes/3;
	        case 3:
	        	lvalue= 0.09f+deltaMinutes;
	        	break;
	        	//return 0.1f+deltaMinutes;
	        case 4:
	        	lvalue= 0.17f+deltaMinutes;
	        	break;
	        case 5:
	        	lvalue= 0.3f+deltaMinutes;
	        	break;
	        case 6:
	        	lvalue= 0.41f+deltaMinutes;
	        	break;
	        case 7:
	        	lvalue= 0.58f+deltaMinutes;
	        	break;
	        case 8:
	        	lvalue= 0.7f+deltaMinutes;
	        	break;
	        case 9:
	        	lvalue= 0.82f+deltaMinutes;
	        	break;
	        case 10:
	        	lvalue= 1f+deltaMinutes;
	        	break;
	        case 11:
	        	lvalue= 1f+deltaMinutes;
	        	break;
	        case 12:
	        	lvalue= 1f;
	        	break;
	        case 13:
	        	lvalue= 1f;
	        	break;
	        case 14:
	        	lvalue= 1f;
	        	break;
	        case 15:
	        	lvalue= 1f;
	        	break;
	        case 16: 
	        	lvalue= 0.9f-deltaMinutes;
	        	break;
	        case 17:
	        	lvalue= 0.75f-deltaMinutes;
	        	break;
	        case 18:
	        	lvalue= 0.63f-deltaMinutes;
	        	break;
	        case 19: 
	        	lvalue= 0.51f-deltaMinutes;
	        	break;
	        case 20: 
	        	lvalue= 0.35f-deltaMinutes;
	        	break;
	        case 21: 
	        	lvalue= 0.24f-deltaMinutes;
	        	break;
	        case 22:
	        	lvalue= 0.15f-deltaMinutes;
	        	break;
	        case 23:
	        	lvalue= 0.08f;
	        	break;
	        default:
	        	lvalue=1;
		}
		
		if(lvalue<0.25f)
			lvalue=0.25f;
		
		return lvalue;
	}
	private void resetConsumptionAttributes()
	{
		tempWaterOutput=waterOutput;
		tempEnergyOutput=energyOutput;
		tempWaterConsumption=waterConsumption;
		tempEnergyConsumption=energyConsumption;		
		
		waterOutput=0;
		energyOutput=0;
		waterConsumption=0;
		energyConsumption=0;		
	}
	
	public Boolean getEnoughWater()
	{
		if(tempWaterConsumption<=tempWaterOutput)
			return true;
		
		return false;
	}
	
	public Boolean getEnoughEnergy()
	{
		if(tempEnergyConsumption<=tempEnergyOutput)
			return true;
		
		return false;
	}
	
	public int getWaterOutput()
	{
		return tempWaterOutput;
	}
	public int getEnergyOutput()
	{
		return tempEnergyOutput;
	}
	public int getWaterConsumption()
	{
		return tempWaterConsumption;
	}
	public int getEnergyConsumption()
	{
		return tempEnergyConsumption;
	}
	public int getRenderWaterOutput()
	{
		return waterOutput;
	}
	public int getRenderEnergyOutput()
	{
		return energyOutput;
	}
	public int getRenderWaterConsumption()
	{
		return waterConsumption;
	}
	public int getRenderEnergyConsumption()
	{
		return energyConsumption;
	}	
	public void setWaterOutput(int value)
	{
		waterOutput=value;
	}
	public void setEnergyOutput(int value)
	{
		energyOutput=value;
	}
	public void setWaterConsumption(int value)
	{
		waterConsumption=value;
	}
	public void setEnergyConsumption(int value)
	{
		energyConsumption=value;
	}	
	//OTHER<---------------------------------------------
	
	
	//INPUT---------------------------------------------->
	public Boolean mouseMovedDrag(int x, int y, int libgdxy)
	{
		setPlacingLight(x, y, libgdxy);
		
//		if(gameGui.moveAddress!=null)
//			bRenderFrameBuffer=true;
		
		return false;
	}
	
	public Boolean mouseCursorMoved(int x, int y, int libgdxy)
	{
		setPlacingLight(x, y, libgdxy);
		
		if(town.gameGui.bAddressMoving!=null)
			bRenderFrameBuffer=true;
		
		return false;
	}	
	
	public void setPlacingLight(int x, int y, int libgdxy)
	{
        Vector3 c0 = new Vector3(x, y, 0);
        Vector3 c1 = town.gameCam.unproject(c0);
        x=(int) c1.x;
        y=(int) c1.y;
		placinglight.setPosition(c1.x, c1.y);
	}
	
	public void buttonUp()
	{
		bButtonDown=false;
	}
	
	public Boolean buttonDown(int x, int y, int libgdxy, int button)
	{
		bButtonDown=true;
		return false;
	}
	//INPUT<---------------------------------------------
	
	
	//RENDER--------------------------------------------->
	public void renderTilemap(Hashtable<Vector2, CWorldObject> tilemap)
	{
		//Räume: Boden und Mauern
		//for(CWorldObject obj : worldRoomObjects)
		for (Vector2 key: tilemap.keySet())
		{
			CWorldObject obj = tilemap.get(key);
			
	        TextureRegion currentFrame=obj.theobject.textureRegion[0];
	        
	        if(obj.theobject.objectAnimation!=null)
	        {
	        	currentFrame = obj.theobject.objectAnimation.getKeyFrame(stateTime, true);
	        }
	        
	        float scale=1f;
	        
	        worldSpriteBatch.setColor(1, 1, 1, alpharoomObjects);
			if(town.gameGui.bDeletemode==true)
			{
				int mx = Gdx.input.getX();
				int my = Gdx.input.getY();
				int lgdxmy = CHelper.screenToLibGDX(my);
				
		        Vector3 c0 = new Vector3(mx, my, 0);
		        Vector3 c1 = town.gameCam.unproject(c0);
		        mx=(int) c1.x;
		        my=(int) c1.y;
		        
		       //if(obj.box2dBody!=null)
		        //{
					//if(obj.box2dBody.getFixtureList().first().testPoint(mx, my))
		        	if(obj.testpoint(mx, my, IntersectionMode.MOUSECLICK))
					{
						worldSpriteBatch.setColor(1, 0, 0, 0.8f);
						scale=0.1f;
					}
				//		        }
				//		        else
				//		        {
				//		        	if(obj.theobject.collides(mx, my, lgdxmy))
				//		        	{
				//						worldSpriteBatch.setColor(1, 0, 0, 0.8f);
				//						scale=0.1f;
				//		        	}
				//		        }
			}
			
			worldSpriteBatch.draw(currentFrame, obj.pos_x(), obj.pos_y(), obj.width/2, obj.height/2, obj.width, obj.height, scale, scale, obj.rotation());
		}
	}
	
	public void renderWorldObjects(CAddress address1)
	{
		try
		{
			bCloneAddressRender=true;
			renderWorldObjectsFilter(1000, address1.listWorldObjects_Ground, "");
			renderWorldObjectsFilter(1000, address1.listWorldObjects_Floors, "");
			renderWorldObjectsFilter(1000, address1.listWorldObjects, "");
			bCloneAddressRender=false;
		}
		catch(Exception e1)
		{
			bCloneAddressRender=false;
			CHelper.writeError(e1.getMessage(), e1.getStackTrace(), e1);
		}
	}
	
	public void renderWorldObjects(Collection<CWorldObject> listWorldObjects, Collection<CWorldObject> listDrawSpecial2, Collection<CWorldObject> listRoomObjects, Collection<CWorldObject> listHumans, Collection<CWorldObject> listGarbageContainers, Collection<CWorldObject> listFootpath, Collection<CWorldObject> listRoad, Collection<CWorldObject> listOutdoorLights, Collection<CWorldObject> listGroundObjects, Collection<CWorldObject> listZombies, Boolean bClone)
	{
		//if(town.gameCam.zoom > town.frameBufferZoom_norender)
		//	bRenderFrameBuffer=false;
		
		if(town.bLoadingSaveGame)
			return;
		
		bCloneAddressRender=bClone;
		
		try
		{
		bPlacingOK=true;
		
		//Werden im Render des Worldobjects gesetzt
		townStatistics.getCurrentStatistics_Other().clearTemp();
		
		
		if(bRenderFrameBuffer && !bClone)
		{
			/*
			if(fbo!=null)
			{
				fbo.dispose();
				fbo=null;
				fboBatch.dispose();
				fboBatch=null;
			}
			*/
			
			if(fbo!=null)
			{
				if(fbo.getWidth() != Gdx.graphics.getWidth() || fbo.getHeight()!=Gdx.graphics.getHeight())
				{
					fbo.dispose();
					fbo=null;
					fboBatch.dispose();
					fboBatch=null;
				}
			}
			
			if(fbo==null)
				fbo = new FrameBuffer(Pixmap.Format.RGB565, (int) (Gdx.graphics.getWidth()), (int) (Gdx.graphics.getHeight()), false);
			
		    fbo.getColorBufferTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		    
		    if(fboBatch==null)
		    {
		    	fboBatch = new SpriteBatch();
		    }
		    
		    fboBatch.disableBlending();
	    	
		    fbo.begin();
			Gdx.gl.glViewport(0, 0, fbo.getWidth(), fbo.getHeight());
			Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
			Gdx.gl.glEnable(GL30.GL_BLEND);
			Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
		}
		
		
		if(bRenderFrameBuffer && !bClone)
		{
			/*
			worldSpriteBatch.end();
			worldSpriteBatch.dispose();
			worldSpriteBatch = new  SpriteBatch();
			worldSpriteBatch.setProjectionMatrix(town.gameCam.combined);
			worldSpriteBatch.begin();
			*/
		}
				
		if(bRenderFrameBuffer && !bClone)
			renderBackground();
		
		renderWorldObjectsFilter(-3, listGroundObjects, "");
		
		if(listZombies!=null)
			renderWorldObjectsFilter(2, listZombies, "zombies"); //tote zombies 
		
		if(bRenderFrameBuffer || bClone)
			renderShadows("road_road_footpath", listFootpath, true);
		renderWorldObjectsFilter(-2, listFootpath, "");
		
		if(bRenderFrameBuffer || bClone)
			renderShadows("road_road_road", listRoad, true);
		renderWorldObjectsFilter(-1, listRoad, "");
		
		if(bRenderFrameBuffer || bClone)
			renderShadows("floor", listRoomObjects, true);
		
		renderWorldObjectsFilter(0, listRoomObjects, "");
		
		if(bRenderFrameBuffer)
			renderWorldObjectsFilter(1000, worldCarpets, "");
		
		if(bRenderFrameBuffer)
			renderWorldObjectsFilter(1000, worldZombieEntrances, "");
		
		renderWorldObjectsFilter(1000, worldDefenseWarning, "");
		
		//renderWorldObjectsFilter(1000, worldWatersystems, "");
		
		renderWorldObjectsFilter(1000, listWorldObjects, "");
		
		{
			
			if(!bClone)
			{
				
				if(town.gameCam.zoom > town.frameBufferZoom && bRenderFrameBuffer)
				{
				}
								
				
				if(bRenderFrameBuffer)
				{
					bHandleObject=false;
					
					try
					{
						renderWorldObjectsFilter(2, listGarbageContainers, ""); //wird derzeit drübergerendert wegen action
						renderWorldObjectsFilter(1000, worldDrawSpecial, "");
						renderWorldObjectsFilter(1000, worldWatersystems, "");
						renderWorldObjectsFilter(1000, listDrawSpecial2, "");
						
						
						renderWorldObjectsFilter(2, tempHumansDead.values(), ""); //lebende residents laufen nicht unter toten residents durch
						
						/* Humans laufen rum -> wenn framebuffer dann auch noch: humans gibts mehrfach
						if(listHumans!=null)
						{
							townStatistics.getCurrentStatistics_Population_Temp().clear();
							renderWorldObjectsFilter(99, listHumans, "humans");
							townStatistics.getCurrentStatistics_Population_Temp().calculateAVG();
							townStatistics.transferTempToOriginal_Population();
						}
						
						if(listZombies!=null)
							renderWorldObjectsFilter(99, listZombies, "zombies");
						*/ 
						renderWorldObjectsFilter(1000, worldCoverlights, "");
						renderWorldObjectsFilter(5, listOutdoorLights, "");
						//renderWorldObjectsFilter(1000, worldBirds, ""); gleich wie humans siehe text oben
					}
					catch(Exception e){
						//..
					}
					bHandleObject=true;
				}
			}
		}
		
		//--------------------------------------------------------------------
		//Framebuffer Ende
		//--------------------------------------------------------------------
		
		if(bRenderFrameBuffer && !bClone)
			bRenderFrameBuffer=false;
		
		if(!bRenderFrameBuffer && !bClone)
		{
			//rendere den bestehenden framebuffer
			worldSpriteBatch.end();
			fbo.end();
			fboBatch.begin();
			fboBatch.draw(fbo.getColorBufferTexture(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0f, 0f, 1, 1);
			fboBatch.end();
			worldSpriteBatch.begin();
		}
		
		//--------------------------------------------------------------------
		
		renderWorldObjectsFilter(2, listGarbageContainers, "");
		
		if(!bClone)
		{
			//renderWorldObjectsFilter(1000, worldZombieEntrances, "");
			
			for(CWorldObject wobj : tempListDrawAdditional) //Additional über Framebuffer drüber rendern
			{
				Boolean bDrawObjectByCamera = town.gameCam.frustum.boundsInFrustum(wobj.pos_x()+wobj.width/2, wobj.pos_y()+wobj.height/2, 0, wobj.width/2, wobj.height/2, 0);
				
				if(bDrawObjectByCamera)
					wobj.drawAdditionalActionObjects();
			}
		}
		
		if(!bClone)
		{
			renderWorldObjectsFilter(1000, worldDrawSpecial, "");
			renderWorldObjectsFilter(1000, worldWatersystems, "");
			renderWorldObjectsFilter(1000, listDrawSpecial2, "");
			
			renderWorldObjectsFilter(1000, worldCars, "");
			renderWorldObjectsFilter(2, tempHumansDead.values(), ""); //lebende residents laufen nicht unter toten residents durch
			
			if(town.bConstructionMode)
			{
				for(CWorldObject wobj : tempConstructionObjects)
				{
					wobj.render(worldSpriteBatch, false);
					wobj.renderTextInfo();
				}
			}			
			
			if(listHumans!=null)
			{
				townStatistics.getCurrentStatistics_Population_Temp().clear();
				renderWorldObjectsFilter(99, listHumans, "humans");
				townStatistics.getCurrentStatistics_Population_Temp().calculateAVG();
				townStatistics.transferTempToOriginal_Population();
			}
			
			if(listZombies!=null)
			{
				renderWorldObjectsFilter(99, listZombies, "zombies");
			}
			
			//renderWorldObjectsFilter(1000, worldCoverlights, "");
			//renderWorldObjectsFilter(5, listOutdoorLights, "");
			//renderWorldObjectsFilter(1000, worldBirds, "");				
			
			//Draw Defense (Warning) Range
			Boolean bDrawDefenseZone=false;
			if(town.gameGui.objPlacing!=null && (town.gameGui.objPlacing.editoraction.contains("illuminati_defensesystem") || town.gameGui.objPlacing.editoraction.contains("illuminati_defensewarningsystem")))
				bDrawDefenseZone=true;

			//Defense Systems
			if(bDrawDefenseZone || worldDefenseWarning.size()>0)
			{
				worldSpriteBatch.end();
				Gdx.gl.glEnable(GL30.GL_BLEND);
				Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
				shapeRenderer1.setProjectionMatrix(town.gameCam.combined);
				shapeRenderer1.setAutoShapeType(true);
				shapeRenderer1.begin();
				
				int id1=0;
				if(markerObject!=null)
					id1=markerObject.uniqueId;
				
				for(CWorldObject wobj : worldDefenseWarning)
				{
					if(bDrawDefenseZone && id1!=wobj.uniqueId)
						wobj.renderDefenseZone(shapeRenderer1);
					wobj.drawDefenseWarningRange(shapeRenderer1);
				}
				
				if(bDrawDefenseZone)
				{
					for(CWorldObject wobj : worldObjects)
						if(id1!=wobj.uniqueId)
							wobj.renderDefenseZone(shapeRenderer1);
				}
				
				shapeRenderer1.end();
				worldSpriteBatch.begin();
			}
			
			
			//Draw Water System Range
			Boolean bDrawWaterZone=false;
			if(town.gameGui.objPlacing!=null && (town.gameGui.objPlacing.editoraction.contains("company_waterworks_groundwaterextractionsystem"))) {
				bDrawWaterZone=true;
			}
			if(town.gameWorld.markerObject!=null && town.gameWorld.markerObject.theobject.editoraction.contains("company_waterworks_groundwaterextractionsystem")) {
				bDrawWaterZone=true;
			}
			
			if(town.gameGui.clonedAddress!=null) {
				bDrawWaterZone=true;
			}
			
			if(bDrawWaterZone || worldWatersystems.size()>0)
			{
				worldSpriteBatch.end();
				Gdx.gl.glEnable(GL30.GL_BLEND);
				Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
				shapeRenderer1.setProjectionMatrix(town.gameCam.combined);
				shapeRenderer1.setAutoShapeType(true);
				shapeRenderer1.begin();
				
				int id1=0;
				if(markerObject!=null)
					id1=markerObject.uniqueId;
				
				for(CWorldObject wobj : worldWatersystems)
				{
					if(bDrawWaterZone && id1!=wobj.uniqueId) {
						wobj.renderDefenseZone(shapeRenderer1);
					}
					//wobj.drawDefenseWarningRange(shapeRenderer1);
				}
				
				if(town.gameGui.clonedAddress!=null) {
					for(CWorldObject w1 : town.gameGui.clonedAddress.listWorldObjects) {
						if(id1!=w1.uniqueId) {
							if(w1.theobject.editoraction.contains("company_waterworks_groundwaterextractionsystem")) {
								w1.renderDefenseZone(shapeRenderer1);
							}
						}
					}
				}
				
				if(town.gameGui.objPlacing!=null) {
					if(town.gameGui.objPlacing.editoraction.contains("company_waterworks_groundwaterextractionsystem")) {
						//Gdx.app.setLogLevel(10);
						//Gdx.app.log("", "test1");
						town.gameGui.objPlacing.renderDefenseZone(shapeRenderer1);
					}
				}
				
				//if(bDrawWaterZone)
				//{
				//	for(CWorldObject wobj : worldObjects)
				//		if(id1!=wobj.uniqueId)
				//			wobj.renderDefenseZone(shapeRenderer1);
				//}
				
				shapeRenderer1.end();
				worldSpriteBatch.begin();
			}			
		}
		
		if(!bClone)
		{
			//if(town.gameCam.zoom <= town.frameBufferZoom)
			renderWorldObjectsFilter(1000, worldCoverlights, "");
		}
		
		renderPlacing(1000);
				
		if(!bClone)
		{
			//if(town.gameCam.zoom <= town.frameBufferZoom)
			renderWorldObjectsFilter(5, listOutdoorLights, "");
		}
		
		if(!bClone)
		{
			//if(town.gameCam.zoom <= town.frameBufferZoom)
			renderWorldObjectsFilter(1000, worldBirds, "");
		}
		
		if(!bClone)
		{
			townStatistics.getCurrentStatistics_Other().transferTempToReal();
			townStatistics.calculateCurrentOtherStatistics();
		}
		
		if(!bPlacingOK)
		{
			if(worldSpriteBatch.isDrawing())
				worldSpriteBatch.end();
			
			int mx = Gdx.input.getX();
	        int my = CHelper.screenToLibGDX(Gdx.input.getY());
	        
	        town.gameGui.editorSpriteBatch.begin();
	        
	        //town.gameGui.setGUICamera();
	        town.gameGui.renderForbidden(mx, my);
	        //setWorldCamera();
	        
	        town.gameGui.editorSpriteBatch.end();
			
			worldSpriteBatch.begin();
		}

		drawPlacingBoundingPolygons();
		
		
		}
		catch(Exception e)
		{
			bCloneAddressRender=false;
			CHelper.writeError(e.getMessage(), e.getStackTrace(), e);
		}		
		bCloneAddressRender=false;
		
		
	}
	public void renderShadows(String editorAction, Collection<CWorldObject> list, Boolean bContains)
	{
		
		String[] listEd = editorAction.split(",");
		
		for(CWorldObject obj : list)
		{
			//Gdx.app.debug("", ""+obj.theobject.editoraction);
			
			Boolean bSet=false;
			for(String ed : listEd)
			{
				//Gdx.app.debug("", ""+ed);
				
				if(bContains)
				{
					if(obj.theobject.editoraction.contains(ed))
					{
						bSet=true;
						break;
					}
				}
				else
				{
					if(obj.theobject.editoraction.equals(ed))
					{
						bSet=true;
						break;
					}
				}
			}
			
			if(bSet)
			{
				//if(town.gameCam.frustum.pointInFrustum(obj.pos_x()+obj.width/2, obj.pos_y()+obj.height/2, 0))
				{
					if(obj.bDrawObject && obj.bDrawObjectByCamera)
					{
						//if(obj.bDrawObjectByCamera)
						obj.drawShadows(worldSpriteBatch, obj.theobject.textureRegion[0]);
					}
				}
				continue;
			}
		}
	}
	//public void renderWorldObjectsFilter(int zorder, List<CWorldObject> list)
	
	
	public void autoSetWorker(CWorldObject obj)
	{
		if(!obj.bObjectIsReady)
			return;
		
		
		if(!obj.isHuman() && obj.theaddress==null)
			return;
		
		if(!obj.isHuman() && obj.theaddress.isCloning)
			return;

		if(obj.isHuman() && obj.thehuman.workplaces.size()==0 && !obj.bIsDead)
		{
			if(obj.thehuman.getAge()>=18 && !worldUnemployed.contains(obj) && obj.thehuman.canWork())
				worldUnemployed.add(obj);
		}
		
		if(obj.isHuman() && obj.thehuman.workplaces.size()>0)
			worldUnemployed.remove(obj);
		
		if(obj.isResidentialTaskObject())
		{
			if(obj.worker==null)
			{
				CWorldObject temp0=null;
				CWorldObject temp1=null;
				CWorldObject temp2=null;
				CWorldObject temp3=null;
				
				for(CWorldObject w1 : obj.theaddress.listWorldObjects)
				{
					if(w1.isHuman() && !w1.bIsDead && w1.thehuman.canWork() && w1.thehuman.car!=null && !w1.isZombie())
						temp0=w1;
					
					if(w1.isHuman() && !w1.bIsDead && w1.thehuman.getAge()>=16 && w1.thehuman.canWork() && w1.thehuman.taskobjects.size()==0 && w1.thehuman.workplaces.size()==0 && !w1.isZombie())
						temp1=w1;
					
					if(w1.isHuman() && !w1.bIsDead&& w1.thehuman.getAge()>=16 && w1.thehuman.canWork() && w1.thehuman.taskobjects.size()==0 && !w1.isZombie())
						temp2=w1;
					
					if(w1.isHuman() && !w1.bIsDead && w1.thehuman.getAge()>=16 && w1.thehuman.canWork() && !w1.isZombie())
						temp3=w1;
				}
				
				CWorldObject set1 = null;
				
				if(temp0!=null)
					set1=temp0;
				else if(temp1!=null)
					set1=temp1;
				else if(temp2!=null)
					set1=temp2;
				else if(temp3!=null)
					set1=temp3;
				
				if(set1!=null)
				{
					linkWorkerAndWorkplace(set1, obj, 1);
				}
			}
			else
			{
				//Entferne kranken Resident
				ArrayList<CWorldObject> rlist = new ArrayList<CWorldObject>();
				if(!obj.worker.thehuman.canWork())
				{
					for(CWorldObject tobj : obj.worker.thehuman.taskobjects.values())
					{
						if(tobj.isResidentialTaskObject())
						{
							if(tobj.worker==null)
								continue;
							if(tobj.theobject.editoraction.contains("diningtable")) //&& tobj.worker.uniqueId!=obj.worker.uniqueId)
								continue;
							
							rlist.add(tobj);
						}
					}
					
					for(CWorldObject robj : rlist)
					{
						if(obj.worker==null)
						{
							robj.worker=null;
							continue;
						}

						if(obj.worker.thehuman.taskobjects.containsKey(robj.uniqueId))
							obj.worker.thehuman.taskobjects.remove(robj.uniqueId);	
						
						robj.worker=null;
					}
				}
			}
		}
		
		if((obj.isCompanyWorkingPlace() || obj.isCompanyTaskObject()))
		{
			if(obj.worker==null || (obj.theobject.editoraction.contains("studentsdesk") && obj.worker2==null))
			{
				CWorldObject temp1=null;
				CWorldObject temp2=null;
				
				//if(obj.isMaintenanceObject())
				
				for(CWorldObject w1 : worldUnemployed)
				{
					if(w1.isZombie())
						continue;
					
					if(w1.thehuman.workplaces.size()>0)
						continue;
						
					if(((!w1.bIsDead && w1.thehuman.getEducationValue()>=obj.theobject.getRequiredWorkplaceEducation()) && (w1.thehuman.getEducationValue()<=obj.theobject.getRequiredWorkplaceEducation()+0.5f)) && w1.thehuman.getSkill(Integer.parseInt(obj.theobject.objectId))>0)
					{
						temp1=w1;
						break;
					}
					
					if(((!w1.bIsDead && w1.thehuman.getEducationValue()>=obj.theobject.getRequiredWorkplaceEducation()) && (w1.thehuman.getEducationValue()<=obj.theobject.getRequiredWorkplaceEducation()+0.5f)))
					{
						temp2=w1;
						break;
					}
				}
				
				CWorldObject theworker=null;
				if(temp1!=null)
					theworker=temp1;
				else if(temp2!=null)
					theworker=temp2;
				
				if(theworker!=null)
				{
					if(obj.worker==null) {
						linkWorkerAndWorkplace(theworker, obj, 1);
					} else if(obj.theobject.editoraction.contains("studentsdesk") && obj.worker2==null) {
						linkWorkerAndWorkplace(theworker, obj, 2);
					}
					
					worldUnemployed.remove(theworker); //not working
					
					//Setze Worker auf bis zu 3 maintenance objects
					if(obj.isMaintenanceObject())
					{
						for(CWorldObject mobj : obj.theaddress.listWorldObjects)
						{
							if(theworker.thehuman.workplaces.size()>2)
								break;
							
							if(mobj.isMaintenanceObject() && mobj.worker==null)
							{
								linkWorkerAndWorkplace(theworker, mobj, 1);
							}
						}
					}
				}
			}
			else
			{
				if(!obj.worker.thehuman.canWork() || obj.worker.bIsDead) 
				{
					//Entferne kranken Resident von Arbeitsplatz
					
					ArrayList<CWorldObject> rlist = new ArrayList<CWorldObject>();
					ArrayList<CWorldObject> rlist2 = new ArrayList<CWorldObject>();
					
					for(CWorldObject tobj : obj.worker.thehuman.workplaces.values())
					{
						if(tobj.isCompanyWorkingPlace())
							rlist.add(tobj);
					}
					
					for(CWorldObject tobj : obj.worker.thehuman.taskobjects.values())
					{
						if(tobj.isCompanyTaskObject())
							rlist2.add(tobj);
					}						
					
					for(CWorldObject robj : rlist)
					{
						if(obj.worker==null){
							robj.worker=null;
							continue;
						}
						obj.worker.thehuman.workplaces.remove(robj.uniqueId);
						robj.worker=null;
					}
					
					for(CWorldObject robj : rlist2)
					{
						if(obj.worker==null) {
							robj.worker=null;
							continue;
						}
						obj.worker.thehuman.taskobjects.remove(robj.uniqueId);
						robj.worker=null;
					}					
				}
				
				if(obj.worker2!=null) 
				{
					if(!obj.worker2.thehuman.canWork() || obj.worker2.bIsDead || obj.isZombie()) 
					{
						//Entferne kranken Resident von Arbeitsplatz
						
						ArrayList<CWorldObject> rlist = new ArrayList<CWorldObject>();
						ArrayList<CWorldObject> rlist2 = new ArrayList<CWorldObject>();
						
						for(CWorldObject tobj : obj.worker2.thehuman.workplaces.values())
						{
							if(tobj.isCompanyWorkingPlace())
								rlist.add(tobj);
						}
						
						for(CWorldObject tobj : obj.worker2.thehuman.taskobjects.values())
						{
							if(tobj.isCompanyTaskObject())
								rlist2.add(tobj);
						}						
						
						for(CWorldObject robj : rlist)
						{
							if(obj.worker2==null){
								robj.worker2=null;
								continue;
							}
							obj.worker2.thehuman.workplaces.remove(robj.uniqueId);
							robj.worker2=null;
						}
						
						for(CWorldObject robj : rlist2)
						{
							if(obj.worker2==null) {
								robj.worker2=null;
								continue;
							}
							obj.worker2.thehuman.taskobjects.remove(robj.uniqueId);
							robj.worker2=null;
						}					
					
					}		
				}
			}
		}
	}
	
	public void autoSetDefault(CWorldObject obj)
	{
		if(obj.theobject.editoraction.contains("diningtable"))
		{
			if(obj.actiontime1==-1 && obj.actiontime2==-1 && obj.actiontime3==-1)
				obj.actiontime1=20;
		}
	}
	
	public void renderWorldObjectsFilter(int zorder, Collection<CWorldObject> list, String slisttype)
	{
		try
		{
		//**********
		//Statistics
		//**********
		CStatisticsData_Population stat = townStatistics.getCurrentStatistics_Population_Temp();
		
		List<CWorldObject> removeList=new ArrayList<CWorldObject>();;
		List<CWorldObject> moveZombieList=null;
		List<Integer> removeZombieList=null;
		if(slisttype.equals("humans") || slisttype.equals("zombies"))
		{
			moveZombieList=new ArrayList<CWorldObject>();
			removeZombieList=new ArrayList<Integer>();
		}
		
		tempObjectBonusPlaceList.clear();
		for(CWorldObject obj : list) // hier exception mit temphumansdead, aber list ist nicht null
		{
			if(obj.theobject.editoraction.contains("illuminati_defensewall") && obj.objectCondition<=0)
				removeList.add(obj);
			
			String objid = obj.theobject.objectId;
			int iobjid = Integer.parseInt(objid);
			
			autoSetWorker(obj);
			autoSetDefault(obj);			
			
			//Object Price by Count
//			if(obj.theobject.zorder==zorder && 
//					(obj.theobject.editoraction.contains("outdoor_tree") || 
//							obj.theobject.editoraction.contains("outdoor_flower") || 
//							obj.theobject.editoraction.contains("outdoor_plant") || 
//							obj.theobject.editoraction.contains("outdoor_ground")))
//			{
//				int value=0;
//				
//				if(tempPriceObjects_temp.containsKey(iobjid))
//				{
//					value = tempPriceObjects_temp.get(iobjid);
//					value++;
//					tempPriceObjects_temp.put(iobjid, value);
//				}
//				else
//				{
//					value++;
//					tempPriceObjects_temp.put(iobjid, value);
//				}
//			}
			
//			if(obj.theobject.editoraction.contains("tree2"))
//				Gdx.app.debug("test1", obj.theobject.editoraction + ", bj.theobject.ATTR_PLACEBONUS: " + obj.theobject.ATTR_PLACEBONUS + ", obj.theobject.ATTR_PLACECOUNT_ " + obj.theobject.ATTR_PLACECOUNT + ", zorder: " + zorder + ", obj.theobject.zorder: " + obj.theobject.zorder);

			
			//One-Time Placing Bonus
			if((obj.theobject.zorder==zorder || zorder==1000) && obj.theobject.iOneTimeBonus==0)
			{
				//Complete Playground
				if(obj.theobject.editoraction.toLowerCase().contains("playground") && obj.theobject.iOneTimeBonus==0)
				{
					if(obj.theaddress!=null)
					{
						Boolean bSandpit=false;
						Boolean bSlide=false;
						Boolean bSwing=false;
						Boolean bSeesaw=false;
						
						for(CWorldObject pgobj : obj.theaddress.listWorldObjects)
						{
							if(pgobj.theobject.editoraction.toLowerCase().contains("sandpit"))
								bSandpit=true;
							if(pgobj.theobject.editoraction.toLowerCase().contains("slide"))
								bSlide=true;
							if(pgobj.theobject.editoraction.toLowerCase().contains("swing"))
								bSwing=true;
							if(pgobj.theobject.editoraction.toLowerCase().contains("seesaw"))
								bSeesaw=true;
						}
						
						if(bSandpit && bSlide && bSwing && bSeesaw)
						{
							for(CObject pgobj : town.gameResourceConfig.listObject)
							{
								if(pgobj.editoraction.toLowerCase().contains("sandpit"))
									pgobj.iOneTimeBonus=1;
								if(pgobj.editoraction.toLowerCase().contains("slide"))
									pgobj.iOneTimeBonus=1;
								if(pgobj.editoraction.toLowerCase().contains("swing"))
									pgobj.iOneTimeBonus=1;
								if(pgobj.editoraction.toLowerCase().contains("seesaw"))
									pgobj.iOneTimeBonus=1;
							}
							
							for(CWorldObject pgobj : obj.theaddress.listWorldObjects)
							{
								if(pgobj.theobject.editoraction.toLowerCase().contains("sandpit"))
									pgobj.theobject.iOneTimeBonus=1;
								if(pgobj.theobject.editoraction.toLowerCase().contains("slide"))
									pgobj.theobject.iOneTimeBonus=1;
								if(pgobj.theobject.editoraction.toLowerCase().contains("swing"))
									pgobj.theobject.iOneTimeBonus=1;
								if(pgobj.theobject.editoraction.toLowerCase().contains("seesaw"))
									pgobj.theobject.iOneTimeBonus=1;
							}
														
							int ibonus=10000;
							changeTownMoney(ibonus);
							obj.addAnimationEvent(AnimationEventType.MONEY, ibonus);
						}
					}
				}
				
				if(obj.theobject.ATTR_PLACEBONUS>0 && obj.theobject.ATTR_PLACECOUNT>0)
				{
					int value=0;
					if(tempObjectBonusPlaceList.containsKey(iobjid))
					{
						value = tempObjectBonusPlaceList.get(iobjid);
						value++;
						if(value<obj.theobject.ATTR_PLACECOUNT)
							tempObjectBonusPlaceList.put(iobjid, value);
					}
					else
					{
						value=1;
						tempObjectBonusPlaceList.put(Integer.parseInt(obj.theobject.objectId), value);
					}
					
					if(value==obj.theobject.ATTR_PLACECOUNT)
					{
						value=11;
						tempObjectBonusPlaceList.put(iobjid, value);
						
						Optional<CObject> obj1 = gameResourceConfig.listObject.stream().filter(item->item.objectId.equals(objid)).findFirst();
						
						if(obj1.isPresent() && obj1.get().iOneTimeBonus==0)
						{
							obj1.get().iOneTimeBonus=1;
							changeTownMoney(obj.theobject.ATTR_PLACEBONUS);
							obj.addAnimationEvent(AnimationEventType.MONEY, obj.theobject.ATTR_PLACEBONUS);
						}
					}
					
				}
			}
			
			if(obj.isHuman() && !obj.bIsDead)
			{
				//Statistics
				stat.countAll++;
				if(obj.thehuman.gender=='m')
					stat.countMen++;
				else
					stat.countWomen++;
				
				if(obj.theaddress==null)
					stat.countHomeless++;
				if(obj.thehuman.getAge()<20)
					stat.count0To20++;
				if(obj.thehuman.getAge()>=21 && obj.thehuman.getAge()<=40)
					stat.count21To40++;
				if(obj.thehuman.getAge()>=41 && obj.thehuman.getAge()<=60)
					stat.count41To60++;
				if(obj.thehuman.getAge()>=61 && obj.thehuman.getAge()<=80)
					stat.count61To80++;
				if(obj.thehuman.getAge()>=81 && obj.thehuman.getAge()<=100)
					stat.count81To100++;
				if(obj.thehuman.getAge()>=101)
					stat.count101ToX++;
				
				//Attributes
				stat.ageAVG+=obj.thehuman.getAge();
				stat.educationAVG+=obj.thehuman.getEducationValue();
				stat.happinessAVG+=obj.thehuman.getHappynessValue();
				stat.healthAVG+=obj.thehuman.getHealthValue();
				stat.fitnessAVG+=obj.thehuman.getFitnessValue();
				stat.intelligenceAVG+=obj.thehuman.getIntelligenceValue();
				stat.workoutputAVG+=obj.thehuman.getWorkOutputPerHour(false, null, null, false);
				stat.healthAttitudeAVG+=obj.thehuman.healthAttitude;
				stat.positiveAttitudeAVG+=obj.thehuman.positiveAttitude;
				stat.sleepAVG+=obj.thehuman.getSleepPercent();
				stat.eatAVG+=obj.thehuman.getEatPercent();
				stat.cleanAVG+=obj.thehuman.getCleanPercent();
				stat.toiletAVG+=obj.thehuman.getToiletPercent();
				
				if(obj.thehuman.getAge()<stat.ageMin)
					stat.ageMin=obj.thehuman.getAge();
				if(obj.thehuman.getEducationValue()<stat.educationMin)
					stat.educationMin=obj.thehuman.getEducationValue();
				if(obj.thehuman.getHappynessValue()<stat.happinessMin)
					stat.happinessMin=(int)obj.thehuman.getHappynessValue();
				if(obj.thehuman.getHealthValue()<stat.healthMin)
					stat.healthMin=(int)obj.thehuman.getHealthValue();
				if(obj.thehuman.getFitnessValue()<stat.fitnessMin)
					stat.fitnessMin=(int)obj.thehuman.getFitnessValue();
				if(obj.thehuman.getIntelligenceValue()<stat.intelligenceMin)
					stat.intelligenceMin=(int)obj.thehuman.getIntelligenceValue();
				if(obj.thehuman.getWorkOutputPerHour(false, null, null, false)<stat.workoutputMin);
					stat.workoutputMin=obj.thehuman.getWorkOutputPerHour(false, null, null, false);
				if(obj.thehuman.healthAttitude<stat.healthAttitudeMin)
					stat.healthAttitudeMin=obj.thehuman.healthAttitude;
				if(obj.thehuman.positiveAttitude<stat.positiveAttitudeMin)
					stat.positiveAttitudeMin=obj.thehuman.positiveAttitude;
				
				if(obj.thehuman.getAge()>stat.ageMax)
					stat.ageMax=obj.thehuman.getAge();
				if(obj.thehuman.getEducationValue()>stat.educationMax)
					stat.educationMax=obj.thehuman.getEducationValue();
				if(obj.thehuman.getHappynessValue()>stat.happinessMax)
					stat.happinessMax=(int)obj.thehuman.getHappynessValue();
				if(obj.thehuman.getHealthValue()>stat.healthMax)
					stat.healthMax=(int)obj.thehuman.getHealthValue();
				if(obj.thehuman.getFitnessValue()>stat.fitnessMax)
					stat.fitnessMax=(int)obj.thehuman.getFitnessValue();
				if(obj.thehuman.getIntelligenceValue()>stat.intelligenceMax)
					stat.intelligenceMax=(int)obj.thehuman.getIntelligenceValue();
				if(obj.thehuman.getWorkOutputPerHour(false, null, null, false)>stat.workoutputMax);
					stat.workoutputMax=obj.thehuman.getWorkOutputPerHour(false, null, null, false);
				if(obj.thehuman.healthAttitude>stat.healthAttitudeMax)
					stat.healthAttitudeMax=obj.thehuman.healthAttitude;
				if(obj.thehuman.positiveAttitude>stat.positiveAttitudeMax)
					stat.positiveAttitudeMax=obj.thehuman.positiveAttitude;
			}
			
			
			//******
			//ZOrder
			//******
			Boolean bContinue=false;
			
			if(zorder != 1000 && obj.theobject.zorder!=zorder) //1000 -> cloneAddress Mode
				bContinue=true;
			
			if(zorder==99 && obj.isHuman() && obj.bIsDead) //tote müssen render funktion aus worldhumans durchlaufen
				bContinue=false;
			
			if(bContinue)
				continue;
			
			if(town.bConstructionMode)
			{
				if(obj.iObjectIsReady>=100)
				{
					obj.bObjectIsReady=true;
					tempConstructionObjects.remove(obj);
				}
				else
				{
					obj.bObjectIsReady=false;
					if(!tempConstructionObjects.contains(obj))
						tempConstructionObjects.add(obj);
				}
				
				if(obj.iObjectIsReady<100)
				{
					continue;
				}
			}
			
			obj.render(worldSpriteBatch, false);
						
			
			if(slisttype.equals("zombies"))
			{
				if(obj.iZombie>=1 && obj.zombieShowDeadTimer<=0)
				{
					moveZombieList.add(obj);
					removeZombieList.add(obj.uniqueId);
				}
			}
			
			if(slisttype.equals("humans"))
			{
				if(obj.iZombie>=1)
				{
					moveZombieList.add(obj);
					removeZombieList.add(obj.uniqueId);
				}
			}			
		}
		
		if(slisttype.equals("humans"))
		{
			if(moveZombieList.size()>0)
			{
				worldHumans.removeAll(moveZombieList);
				worldZombies.addAll(moveZombieList);
			}
		}
		
		if(slisttype.equals("zombies"))
		{
			if(moveZombieList.size()>0)
			{
				worldZombies.removeAll(moveZombieList);
			}
		}
		
		if(removeList.size()>0)
		{
			for(CWorldObject wobj : removeList)
				town.gameGui.removeWorldObject(wobj, false);
		}

		
		}
		catch(Exception e)
		{
			CHelper.writeError(e.getMessage(), e.getStackTrace(), e);
		}
	}
	
	public void renderEditor()
	{
		//Editor wird ausgeführt
		for(CWorldObject obj : worldTempRoomObjects)
		{
	        TextureRegion currentFrame=obj.theobject.textureRegion[0];
	        if(obj.theobject.objectAnimation!=null)
	        {
	        	currentFrame = obj.theobject.objectAnimation.getKeyFrame(stateTime, true);
	        }
	    	
	        worldSpriteBatch.draw(currentFrame, obj.pos_x(), obj.pos_y(), obj.width/2, obj.height/2, obj.width, obj.height, 1, 1, obj.rotation());
		}
		
		renderAddressPlacing();
	}
	public void renderMarkedObject(int x, int libgdxy)
	{
		if(!town.gameGui.dlgShowing())
			town.gameGui.objectInfoDlg.render(x, libgdxy);
	}
	public void renderPathfindingMap()
	{
		if(!bRenderPathfinding && !town.bRenderPathfinding_Road && !town.bRenderPathfinding_Footpath && !town.bRenderPathfinding_Address)
			return;
		
		this.shapeRenderer1.setProjectionMatrix(town.gameCam.combined);
		this.shapeRenderer1.setAutoShapeType(true);
	    worldSpriteBatch.end();
	    
		//if(!Gdx.gl.glIsEnabled(GL30.GL_BLEND))
		{
			Gdx.gl.glEnable(GL30.GL_BLEND);
			Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
		}	    
	    this.shapeRenderer1.begin();
			this.shapeRenderer1.setColor(1, 1, 1, 0.5f);
			
			if(town.bRenderPathfinding_Address)
			{
				for(CAddress adr : worldAddressList)
				{
					int nodecountx = (int) (adr.getWidth())/town.floorrastersize;
					int nodecounty = (int) (adr.getHeight())/town.floorrastersize;
					
					this.shapeRenderer1.set(ShapeType.Line);
					
					for(int i=0;i<nodecountx;i+=1)
			        {
			        	for(int i2=0;i2<nodecounty;i2+=1)
			        	{
			        		this.shapeRenderer1.setColor(1, 1, 1, 0.5f);
			        		int ind1 = (int) (i+i2*adr.pathmapsize);
			        		
			        		if(adr.pathmap[ind1]!=null)
			        		{
			        			this.shapeRenderer1.set(ShapeType.Filled);
			        			this.shapeRenderer1.setColor(0, 0, 1, 0.2f);
			        		}
			        		else
			        			this.shapeRenderer1.set(ShapeType.Line);
			        		
			        		this.shapeRenderer1.rect(adr.sx+i*town.floorrastersize, adr.sy+i2*town.floorrastersize, town.floorrastersize, town.floorrastersize);
			        	}
			        }
				}
			}
			
			if(town.bRenderPathfinding_Road)
			{
				int nodecount=CWorld.mapsize/town.roadrastersize;
				this.shapeRenderer1.set(ShapeType.Line);
				
		        for(int i=0;i<nodecount;i+=1)
		        {
		        	for(int i2=0;i2<nodecount;i2+=1)
		        	{
		        		if(CWorld.pathmap_road[i+i2*this.pathmapsize_road]!=null)
		        			this.shapeRenderer1.set(ShapeType.Filled);
		        		else
		        			this.shapeRenderer1.set(ShapeType.Line);
		        		
		        		this.shapeRenderer1.rect(i*town.roadrastersize,i2*town.roadrastersize,town.roadrastersize,town.roadrastersize);
		        	}
		        }
			}
			
			if(town.bRenderPathfinding_Footpath)
			{
				int nodecount=CWorld.mapsize/town.footpathsize;
				this.shapeRenderer1.set(ShapeType.Line);
		        for(int i=0;i<nodecount;i+=1)
		        {
		        	for(int i2=0;i2<nodecount;i2+=1)
		        	{
		        		if(CWorld.pathmap_footpath[i+i2*this.pathmapsize_footpath]!=null)
		        			this.shapeRenderer1.set(ShapeType.Filled);
		        		else
		        			this.shapeRenderer1.set(ShapeType.Line);
		        		
		        		this.shapeRenderer1.rect(i*town.footpathsize,i2*town.footpathsize,town.footpathsize,town.footpathsize);
		        	}
		        }
			}			
			
			if(bRenderPathfinding)
			{
				int nodecount=CWorld.mapsize/CWorld.nodesize;
				this.shapeRenderer1.set(ShapeType.Line);
		        for(int i=0;i<nodecount;i+=1)
		        {
		        	for(int i2=0;i2<nodecount;i2+=1)
		        	{
		        		if(CWorld.pathmap[i+i2*this.pathmapsize]!=null)
		        			this.shapeRenderer1.set(ShapeType.Filled);
		        		else
		        			this.shapeRenderer1.set(ShapeType.Line);
	
		        		this.shapeRenderer1.rect(i*CWorld.nodesize,i2*CWorld.nodesize,CWorld.nodesize,CWorld.nodesize);
		        	}
		        }
			}
	        this.shapeRenderer1.end();
	        
	        //Gdx.gl.glDisable(GL30.GL_BLEND);
	        
	    worldSpriteBatch.begin();
	}
	
	public void renderPathOpen(CAStar astar1)
    {
		if(!town.bRenderPathOpenNodes)
			return;
		
		worldSpriteBatch.end();
		//if(astar.target!=null)
		{
	    	if(astar1.drawOpenlist!=null && astar1.drawOpenlist.size()>0)
	    	{
	    		shapeRenderer1.setProjectionMatrix(town.gameCam.combined);
	    	    shapeRenderer1.setAutoShapeType(true);
	    	    
	    	    shapeRenderer1.begin();
	    	    	shapeRenderer1.setColor(255, 1, 1, 0.1f);
	    	    	
					for (int i = 0; i < astar1.drawOpenlist.size()-1; i++) {
						int x = astar1.drawOpenlist.get(i).x;
						int y = astar1.drawOpenlist.get(i).y;
						shapeRenderer1.circle(x * nodesize + nodesize / 2, y * nodesize + nodesize / 2, nodesize / 4, 30);
					}
	        	shapeRenderer1.end();
	    	}
		}
		worldSpriteBatch.begin();
    }	
	
    public void renderPath(IntArray thepath)
    {
    	if(thepath!=null)
    	{
    		if(!bRenderPathfinding && !town.bRenderPathfinding_Road && !town.bRenderPathfinding_Footpath)
    			return;    		
    		
    		if(thepath.size<5)
    			return;
    		
    		shapeRenderer1.setProjectionMatrix(town.gameCam.combined);
    	    shapeRenderer1.setAutoShapeType(true);
    	    
    	    
    	    if(!shapeRenderer1.isDrawing())
    	    	shapeRenderer1.begin();
    			//Gdx.gl.glEnable(GL30.GL_BLEND);
    			//Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
    			
    	    	shapeRenderer1.setColor(255, 1, 1, 0.1f);
    	    	
    	    	if(town.bRenderPathfinding_Road)
    	    	{
					for (int i = 0, n = thepath.size; i < n; i += 2) {
						int x = thepath.get(i);
						int y = thepath.get(i + 1);
						shapeRenderer1.circle(x * town.roadrastersize + town.roadrastersize / 2, y * town.roadrastersize + town.roadrastersize / 2, town.roadrastersize / 4, 30);
					}
    	    	}
    	    	
    	    	if(town.bRenderPathfinding_Footpath)
    	    	{
					for (int i = 0, n = thepath.size; i < n; i += 2) {
						int x = thepath.get(i);
						int y = thepath.get(i + 1);
						shapeRenderer1.circle(x * town.footpathsize + town.footpathsize / 2, y * town.footpathsize + town.footpathsize / 2, town.footpathsize / 4, 30);
					}
    	    	}
    	    	
    	    	if(bRenderPathfinding)
    	    	{
					for (int i = 0, n = thepath.size; i < n; i += 2) {
						int x = thepath.get(i);
						int y = thepath.get(i + 1);
						shapeRenderer1.circle(x * nodesize + nodesize / 2, y * nodesize + nodesize / 2, nodesize / 4, 30);
					}
    	    	}
    	    	
        	shapeRenderer1.end();
    	}    	
    }		
	
    public Boolean isWhite()
    {
    	float ambvalue = getAmbientLightValue(worldTime);
    	 return (bBackwhite && ambvalue>0.8f && (worldTime.day==12 || (worldTime.day==1 || worldTime.day==11)));    	
    }
    public void renderBackground()
	{
        //Render Background
        //spriteBatch2.setColor(1, 1, 1, 0.4f);
        //worldSpriteBatch.setProjectionMatrix(gameCamera.combined);
        int greensize=CWorld.mapsize/town.backgroundsize;
        
        bBackwhite=false;
        
        background=gameResourceConfig.textures.get("background_grass");
        
        //if(worldTime.day==11)
        //	background=gameResourceConfig.textures.get("background_grass");
        if(worldTime.day==11)
        	background=gameResourceConfig.textures.get("background_grasssnow1");
        if(worldTime.day==11 && worldTime.hours>4)
        {
        	bBackwhite=true;
        	background=gameResourceConfig.textures.get("background_grasssnow2");
        }
        
        if(worldTime.day==12)
        {
        	bBackwhite=true;
        	background=gameResourceConfig.textures.get("background_grasssnow2");
        }
        if(worldTime.day==12 && worldTime.hours>4)
        {
        	bBackwhite=true;
        	background=gameResourceConfig.textures.get("background_grasssnow3");
        }
        if(worldTime.day==12 && worldTime.hours>12)
        {
        	bBackwhite=true;
        	background=gameResourceConfig.textures.get("background_grasssnow3");
        }
        
        if(worldTime.day==1)
        {
        	bBackwhite=true;
        	background=gameResourceConfig.textures.get("background_grasssnow3");
        }
        if(worldTime.day==1 && worldTime.hours>4)
        {
        	bBackwhite=true;
        	background=gameResourceConfig.textures.get("background_grasssnow3");
        }
        if(worldTime.day==1 && worldTime.hours>14)
        {
        	bBackwhite=true;
        	background=gameResourceConfig.textures.get("background_grasssnow2");
        }
        if(worldTime.day==1 && worldTime.hours>22)
        	background=gameResourceConfig.textures.get("background_grasssnow2");
        
        if(worldTime.day==2)
        {
        	bBackwhite=true;
        	background=gameResourceConfig.textures.get("background_grasssnow2");
        }
        if(worldTime.day==2 && worldTime.hours>4)
        	background=gameResourceConfig.textures.get("background_grasssnow1");
        if(worldTime.day==2 && worldTime.hours>22)
        	background=gameResourceConfig.textures.get("background_gras_t2");
        
        if(worldTime.day==3)
        	background=gameResourceConfig.textures.get("background_gras_t2");
        if(worldTime.day==4)
        	background=gameResourceConfig.textures.get("background_gras_t2");
        if(worldTime.day==10)
        	background=gameResourceConfig.textures.get("background_gras_t2");
        
        worldSpriteBatch.setColor(1,1,1,1f);
        //Gdx.app.debug("", "zoom: " + gameCamera.zoom);
        for(int i=0;i<greensize;i++)
        {
        	for(int i2=0;i2<greensize;i2++)
        	{
        		int bx=i*town.backgroundsize;
        		int by=i2*town.backgroundsize;
        		if(town.gameCam.frustum.boundsInFrustum(bx, by, 0, town.backgroundsize, town.backgroundsize, 0))
        			worldSpriteBatch.draw(background, bx, by, town.backgroundsize, town.backgroundsize);
        	}
        }
	}
	public void renderPlacing(int zorder)
	{
		if(town.gameGui.objPlacing==null)
			return;
		
		CObject objpl = town.gameGui.objPlacing;
		
		//if(town.gameGui.objPlacing!=null && zorder!=town.gameGui.objPlacing.zorder)
		if(town.gameGui.objPlacing!=null && (zorder!=town.gameGui.objPlacing.zorder && zorder!=1000))
			return;
		
		//if(!town.gameGui.bObjPlacing && !town.gameGui.bRoomPlacing)
		Boolean move=false;
		if(markerObject!=null && markerObject.bObjMoving)
			move=true;
		if(!town.gameGui.bObjPlacing && !town.gameGui.bRoomPlacing && !move)
			return;

		if(town.gameGui.objPlacing.ATTR_BOUNDX>0 || town.gameGui.objPlacing.ATTR_BOUNDY>0)
		{
			worldSpriteBatch.end();
			town.gameGui.objPlacing.drawBoundingPolygon(shapeRenderer2);
			worldSpriteBatch.begin();
		}
		
		
		
		//		if(markerObject!=null)
		//		{
		//			town.gameGui.objPlacing.pos_x=markerObject.pos_x();
		//			town.gameGui.objPlacing.pos_y=markerObject.pos_y();
		//		}
		
		int mx = Gdx.input.getX();
        int my = CHelper.screenToLibGDX(Gdx.input.getY());
		
		float px = Gdx.input.getX();
		float py = Gdx.input.getY();
		
        Vector3 c0 = new Vector3(px,py,0);
        Vector3 c1 = town.gameCam.unproject(c0);
        px = c1.x;
        py = c1.y;
        
        px=px-town.gameGui.objPlacing.width/2; //cursor mittig von objekt
        py=py-town.gameGui.objPlacing.height/2;
        
		//Rasterisierung
        if(town.gameGui.objPlacing.doRasterPlacement)
		{
        	if(town.gameGui.objPlacing.editoraction.contains("floor") ||
        			(town.gameGui.objPlacing.isGroundObject)||
        			(town.gameGui.objPlacing.isGroundBaseObject)||
        			(town.gameGui.objPlacing.editoraction.contains("residential_garage"))||
        			(town.gameGui.objPlacing.editoraction.contains("road_road_parkingspace"))||
        			(town.gameGui.objPlacing.editoraction.contains("road_road")||
        			town.gameGui.objPlacing.iRasterValue>0
        		)
        	)
        	{
				px = (int)(px / town.gameGui.objPlacing.iRasterValue);
				px = (int)(px * town.gameGui.objPlacing.iRasterValue);
				py = (int)(py / town.gameGui.objPlacing.iRasterValue);
				py = (int)(py * town.gameGui.objPlacing.iRasterValue);
				
				px+=town.gameGui.objPlacing.iRasterValue_movx;
				py+=town.gameGui.objPlacing.iRasterValue_movy;
        	}
        	else
        	{
				px = (int)(px / this.wallSize);
				px = (int)(px * this.wallSize);
				py = (int)(py / this.wallSize);
				py = (int)(py * this.wallSize);
        	}
		}
        
        worldSpriteBatch.setColor(1,1,1,0.6f);
		
        if(town.bConstructionMode)
			worldSpriteBatch.setColor(1,1,1,0.1f);
                
		if(move)
			worldSpriteBatch.setColor(1,1,1,1f);
		
		if(!move)
		{
			town.gameGui.objPlacing.pos_x = (int) px;
			town.gameGui.objPlacing.pos_y = (int) py;
		}
		
        //Objekte nur auf Adresse platzierbar und nicht über andere objekte
		bPlacingOK = checkObjectPlacing(null, null);
		
		float floorscale1=1f;
		float floorshadowscale1=99f;
		float rotation = town.gameGui.objPlacing.rotation;
		
		int iframe=0;
		if(markerObject!=null)
			iframe=markerObject.getKeyFrame();
		
		TextureRegion rgn1=null;
		if(town.gameGui.objPlacing.textureRegion.length>iframe)
			rgn1 = town.gameGui.objPlacing.textureRegion[iframe];
		else
			rgn1 = town.gameGui.objPlacing.textureRegion[0];
    	
    	if(move)
    	{
    		px=markerObject.pos_x();
    		py=markerObject.pos_y();
    		town.gameGui.objPlacing.width=markerObject.width;
    		town.gameGui.objPlacing.height=markerObject.height;
    		if(markerObject.thehuman!=null)
    			return;
    	}
    	
    	
    	//*****
    	//Floor
    	//*****
    	if(town.gameGui.objPlacing.editoraction.contains("floor") || town.gameGui.objPlacing.isGroundObject || town.gameGui.objPlacing.isGroundBaseObject)
    	{
    		if(town.gameGui.objPlacing.placingCountLevel==1)
    		{
    			town.gameGui.objPlacing.drawShadows(worldSpriteBatch, 2, Math.round(px), Math.round(py), town.gameGui.objPlacing.width, town.gameGui.objPlacing.height, floorshadowscale1);
    			worldSpriteBatch.setColor(1,1,1,0.6f);
    			if(town.bConstructionMode && town.gameGui.objPlacing.isRoomObject)
    				worldSpriteBatch.setColor(1,1,1,0.1f);
    			worldSpriteBatch.draw(rgn1, px, py, town.gameGui.objPlacing.width/2, town.gameGui.objPlacing.height/2, town.gameGui.objPlacing.width, town.gameGui.objPlacing.height, floorscale1, floorscale1, rotation);
    		}
    		
    		if(town.gameGui.objPlacing.placingCountLevel>1)
    		{
    			for(int i=0;i<town.gameGui.objPlacing.placingCountLevel;i++)
    			{
        			for(int j=0;j<town.gameGui.objPlacing.placingCountLevel;j++)
        				town.gameGui.objPlacing.drawShadows(worldSpriteBatch, 2, Math.round(px+i*town.gameGui.objPlacing.width), Math.round(py-j*town.gameGui.objPlacing.height), town.gameGui.objPlacing.width, town.gameGui.objPlacing.height, floorshadowscale1);
    			}
    			
    			worldSpriteBatch.setColor(1,1,1,0.6f);
    			if(town.bConstructionMode && town.gameGui.objPlacing.isRoomObject)
    				worldSpriteBatch.setColor(1,1,1,0.1f);
    			
    			for(int i=0;i<town.gameGui.objPlacing.placingCountLevel;i++)
    			{
        			for(int j=0;j<town.gameGui.objPlacing.placingCountLevel;j++)
        			{
        				worldSpriteBatch.draw(rgn1, px+i*town.gameGui.objPlacing.width, py-j*town.gameGui.objPlacing.height, town.gameGui.objPlacing.width/2, town.gameGui.objPlacing.height/2, town.gameGui.objPlacing.width, town.gameGui.objPlacing.height, floorscale1, floorscale1, rotation);
        			}
    			}
    		}
    	}
    	
    	//*******
    	//Default
    	//*******  	
    	else
    	{
    		if(markerObject==null)
    		{
	    		if (objpl.editoraction.contains("illuminati_defensesystem") 
	    				|| objpl.editoraction.contains("illuminati_defensewarningsystem"))
	    		{
	    			worldSpriteBatch.end();
	    			Gdx.gl.glEnable(GL30.GL_BLEND);
					Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
					shapeRenderer1.setProjectionMatrix(town.gameCam.combined);
					shapeRenderer1.setAutoShapeType(true);
					shapeRenderer1.begin();
	    			shapeRenderer1.setColor(0.0f, 0.6f, 0.0f, 0.6f);
	    			shapeRenderer1.set(ShapeType.Filled);
	   				shapeRenderer1.circle(objpl.pos_x+objpl.width/2, objpl.pos_y+objpl.height/2, objpl.defense_reichweite);
	    			shapeRenderer1.end();
	    			worldSpriteBatch.begin();
	    		}    		
    		}
    		
    		float scale1=1.08f;
    		float shadowscale1=1.08f;
    		scale1=1f;
    		shadowscale1=1f;
    		
    		if(town.gameGui.objPlacing.editoraction.contains("road_road_road") || 
    				town.gameGui.objPlacing.editoraction.contains("footpath") || 
    				town.gameGui.objPlacing.editoraction.contains("garage"))
    			shadowscale1=99;
    		
    		if(!(town.gameGui.objPlacing.editoraction.contains("road_road_road") || town.gameGui.objPlacing.editoraction.contains("road_road_footpath")))
    			town.gameGui.objPlacing.drawShadows(worldSpriteBatch, 2, Math.round(px), Math.round(py), town.gameGui.objPlacing.width, town.gameGui.objPlacing.height, shadowscale1);
    		//else
    		//	town.gameGui.objPlacing.drawShadows(worldSpriteBatch, 2, Math.round(px), Math.round(py), town.gameGui.objPlacing.width, town.gameGui.objPlacing.height, shadowscale1);
    		
   			
   			worldSpriteBatch.setColor(1,1,1,0.7f);
    		if(move)
    			worldSpriteBatch.setColor(1,1,1,1f);
    		
    		if(town.gameGui.objPlacing.editoraction.contains("road_road_road"))
    			worldSpriteBatch.setColor(1,1,1,0.9f);
    		
    		
    		if(town.gameGui.objPlacing.ATTR_RPLACING==0)
    		{
    			//Default Placing
    			worldSpriteBatch.draw(rgn1, px, py, town.gameGui.objPlacing.width/2, town.gameGui.objPlacing.height/2, town.gameGui.objPlacing.width, town.gameGui.objPlacing.height, scale1, scale1, rotation);
    		}
    		else if(town.gameGui.objPlacing.ATTR_RPLACING>0)//town.gameGui.objPlacing.editoraction.contains("road_road_road") || town.gameGui.objPlacing.editoraction.contains("defensewall"))
    		{
				if(town.gameGui.placeStartX==0 && town.gameGui.placeStartY==0)
				{
	    			worldSpriteBatch.setColor(1, 1, 1, 0.5f);
					worldSpriteBatch.draw(rgn1, px, py, town.gameGui.objPlacing.width/2, town.gameGui.objPlacing.height/2, town.gameGui.objPlacing.width, town.gameGui.objPlacing.height, scale1, scale1, rotation);
				}
    			
    			town.gameGui.roadPlacing(1, rgn1);
    		}
    	}
    	
    	if(objpl.editoraction.contains("company_waterworks_groundwaterextractionsystem"))
    	{
    		town.gameGui.tooltip.textLines.clear();
    		town.gameGui.tooltip.textLines.add(objpl.objectName);
    		town.gameGui.tooltip.textLines.addAll(objpl.getInfoTextBox());
			if(objpl.price>0)
				town.gameGui.tooltip.textLines.add("$"+objpl.price);
			
			worldSpriteBatch.end();
			town.gameGui.tooltip.draw_BuyMenu(mx+30, my+10, objpl.price);
			town.gameGui.tooltip.setColor(Color.WHITE);
			worldSpriteBatch.begin();    		
    	}
	}
	
	public Boolean getTownHallIntelligenceDeskIsOnline()
	{
		for(CCompany comp : worldCompanyList)
		{
			if(comp.companyType!=null && comp.companyType==CompanyType.TOWN_HALL && comp.address_company!=null)
			{
				for(CWorldObject wobj : comp.address_company.listWorldObjects)
				{
					if(wobj.theobject.editoraction.contains("townhall_officeworkingplace_intelligence") && wobj.onlineByWorkInput && wobj.isActiveByEnergyConsumption())
					{
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	public int getObjectCount()
	{
		
		//public List<CWorldObject> warningObjects.;
		int count=0;
		count+=worldObjects.size();
		count+=worldFootpath.size();
		count+=worldRoad.size();
		count+=worldOutdoorLights.size();
		count+=worldCarpets.size();
		count+=worldHumans.size();
		//public List<CWorldObject> worldUnemployed;
		//public List<CWorldObject> worldZombies;
		//public List<CWorldObject> worldZombieEntrances;
		//public List<CWorldObject> worldDefenseWarning;
		count+=worldBirds.size();
		count+=worldDrawSpecial.size();
		count+=worldDrawSpecial2.size(); 
		count+=worldCars.size();
		count+=worldCoverlights.size();
		count+=worldGarbageContainers.size();
		count+=worldGroundObjects.size();
		count+=worldWaterObjects.size();
		
		return count;
	}
	
	
	public void render()
	{
		//if(Gdx.input.isKeyJustPressed(Input.Keys.B)) {
			//town.gameAudio.playSound("EFFECT_DESIGNCOMPLETE", -0.7f, false);
			//town.gameAudio.playSound("EFFECT_RESEARCHCOMPLETE", -0.7f, false);
			//town.gameAudio.playSound("DESIGNCOMPLETE", 0, false);
			//town.gameAudio.playSound("EFFECT_ZOMBIEENTRANCE", 0, false);
			//Gdx.app.setLogLevel(10);
			//Gdx.app.log("", "test");
		//}
		
		//Gdx.app.setLogLevel(10);
		//Gdx.app.log("", ""+worldTime.getMonthString() + ", " + worldTime.day + ", " + worldTime.year + ", " + worldTime.getCurrentDay() + ", " + worldTime.getCurrentDayString());
		
		if(CHelper.isMapScrolling(town))
		{
			bRenderFrameBuffer=true;
		}
		
		warningObjects.clear();
		countIlluminatiDefenseSystems=0;
		birdcount=0;
		townStatistics.getCurrentStatistics_Finance().calculateSum();
		
		if(!town.gameGui.bFlashlight)
		{
			if(town.gameGui.bObjPlacing || town.gameGui.bDeletemode || town.gameGui.bAddressCloning || town.gameGui.bAddressMoving || town.gameGui.bAddressResizingStart)
			{
				if(town.gameGui.bObjPlacing && town.gameGui.objPlacing!=null && (town.gameGui.objPlacing.editoraction.contains("illuminati_defensesystem") 
						|| town.gameGui.objPlacing.editoraction.contains("illuminati_defensewarningsystem")))
				{
					//..
				}
				else
					placinglight.setActive(true);
			}
			else
				placinglight.setActive(false);
		}
		
		renderUniqueNumber = rand.nextLong();
		town.gameGui.deleteObject=null;
		mouseOverObject=null;
		
		if(!worldPause)
		{
			worldTime.step(CHelper.getDeltaSeconds(town));
			renderSecond+=CHelper.getDeltaSeconds(town);
			renderHour+=CHelper.getDeltaSeconds(town);
		}
		
		if(!worldPause)
		{
			spawnNewResidents();
			spawnNewZombies();
			spawnNewZombieEntrance();
			calculateFunds();
		}
		
		float ambvalue = getAmbientLightValue(worldTime);
    	rayHandler.setAmbientLight(ambvalue,ambvalue,ambvalue,ambvalue);
    	
		worldSpriteBatch.setProjectionMatrix(town.gameCam.combined);
		
		resetConsumptionAttributes();
			
			worldSpriteBatch.begin();
			
			worldSpriteBatch.setColor(1, 1, 1, alphaWorldObjects);
			stateTime += Gdx.graphics.getDeltaTime();
			
			renderWorldObjects(worldObjects, worldDrawSpecial2, worldRoomObjects, worldHumans, worldGarbageContainers, worldFootpath, worldRoad, worldOutdoorLights, worldGroundObjects, worldZombies, false);
			
			drawPlacingRects();
			
			if(town.gameGui.clonedAddress!=null)
			{
				renderWorldObjects(town.gameGui.clonedAddress);
			}
			
			if(town.gameGui.bRoomCloning)
			{
				try
				{
					bCloneAddressRender=true;
					renderWorldObjectsFilter(1000, town.gameWorld.cloneRoomList, "");
					bCloneAddressRender=false;
				}
				catch(Exception e1)
				{
					bCloneAddressRender=false;
					CHelper.writeError(e1.getMessage(), e1.getStackTrace(), e1);
				}				
			}
			
			
			//render filter
			/*
			int bsize=town.backgroundsize;
	        int greensize=CWorld.mapsize/bsize;
	        Texture background1=town.gameResourceConfig.textures.get("filter1");// town.gameResourceConfig.listObject.stream().filter(p->p.editoraction.contains("filter1")).findFirst().get().textureImage; //gameResourceConfig.textures.get("company_supermarket_food1");
	        worldSpriteBatch.setColor(1,1,1,0.1f);
	        for(int i=0;i<greensize;i++)
	        {
	        	for(int i2=0;i2<greensize;i2++)
	        	{
	        		int bx=i*bsize;
	        		int by=i2*bsize;
	        		if(town.gameCam.frustum.boundsInFrustum(bx, by, 0, bsize, bsize, 0))
	        			worldSpriteBatch.draw(background1, bx, by, bsize, bsize);
	        	}
	        }
	        */			
	        //worldSpriteBatch.draw(background, town.gameCam.position.x-town.gameCam.viewportWidth/2, town.gameCam.position.y-town.gameCam.viewportHeight/2, town.gameCam.viewportWidth, town.gameCam.viewportHeight);
			
			
			for(CWorldObject warningobj : warningObjects)
				warningobj.drawWarnings(worldSpriteBatch, 1);
			
			renderSpriteMoveEvents(1, worldSpriteBatch);
			
			renderWolken(worldSpriteBatch); //wird hier nach placing abbruch nicht mehr gerendert
			
			renderObjectZone(worldSpriteBatch);
			
			if(!town.bNoRealEstate)
			{
				
				renderAddress(worldAddressList, false, true);
				renderAddress(cloneAddressList, true, true);
			}
			
			//if(town.gameCam.zoom<30)
			//renderWolken(worldSpriteBatch); //wird hier ab zoom 30 nicht mehr gerendert
			
			renderEditor();
			
			renderPathfindingMap();
			
			if(town.bRenderPathOpenNodes)
			{
				for(CAddress adr : worldAddressList)
					renderPathOpen(adr.astar);
			}
			
		worldSpriteBatch.end();
		
		if(town.gameCam.zoom<20)
			worldWeather.renderWeather();
		
		if(bRenderBox2D)
			box2dDebugRenderer.render(box2dworld, town.gameCam.combined);
		
        box2dworld.step(Gdx.graphics.getDeltaTime(), 6, 2);
        
        if(renderSecond>1)
        	renderSecond=0;
        
        if(renderHour>3600)
        {
        	Boolean bCreateBird=true;
        	
			if(birdcount>worldAddressList.size()*5)
				bCreateBird=false;
			
			//int residents = worldHumans.size();
			
			if(birdcount>30)
				bCreateBird=false;
			
			if(bCreateBird)
			{
				createRandomBird(0);
			}
			
        	renderHour=0;
        }
        
        town.gameInput.bMouseIsDragging=false;
        
        if(!worldPause)
        {
    		for(CAddress adr : worldAddressList)
    		{
    			adr.countFlora=adr.countFloraTemp;
    			adr.countFloraTemp=0;
    			
    			adr.list_GreenTown.clear();
    			adr.list_GreenTown.addAll(adr.tempList_GreenTown);
    			adr.tempList_GreenTown.clear();
    		}
        }        
        
		//Set<Integer> set1 = tempPriceObjects_temp.keySet();
		//tempPriceObjects.clear();
		//tempPriceObjects.putAll(tempPriceObjects_temp);
		
		//gameGui.setMultiPlacingPrice(gameGui.objPlacing);
		
		//Gdx.app.debug("test counting", "" + value);
		
		//Gdx.app.debug("1", ""+tempPriceObjects.get(key));
		//for(int i : set1)
		//{
		//}        
                
        //rayHandler.setCombinedMatrix(gameCamera.combined);
		//rayHandler.updateAndRender();
	}
	
	public void drawPlacingBoundingPolygons()
	{
		//Draw placing bounding polygons
		if((town.gameGui.bObjPlacing || town.gameGui.bObjMovement) && town.gameGui.objPlacing!=null)
		{
			CAddress adr = town.gameWorld.getAddressByPoint(town.gameGui.objPlacing.pos_x+town.gameGui.objPlacing.width/2, town.gameGui.objPlacing.pos_y+town.gameGui.objPlacing.height/2);
			if(adr!=null)
			{
				town.gameWorld.worldSpriteBatch.end();
				//shapeRenderer2.begin();
				for(CWorldObject wobj : adr.listWorldObjects)
				{
					if(town.gameWorld.markerObject!=null && wobj.uniqueId==town.gameWorld.markerObject.uniqueId)
						continue;
					
					if(wobj.isAddressObject())
					{
						if(wobj.theobject.ATTR_BOUNDX>0 || wobj.theobject.ATTR_BOUNDY>0)
						{
							wobj.theobject.drawBoundingPolygon(shapeRenderer2);
						}
					}	
				}
				//town.gameWorld.shapeRenderer2.end();
				town.gameWorld.worldSpriteBatch.begin();

				try
				{
					//Zeichne betroffene Objekte nochmal über die Boundzone
					for(CWorldObject wobj : adr.listWorldObjects)
					{
						if(town.gameWorld.markerObject!=null && wobj.uniqueId==town.gameWorld.markerObject.uniqueId)
							continue;
						
						if(wobj.bObjectIsReady && wobj.isAddressObject())
						{
							if(wobj.theobject.ATTR_BOUNDX>0 || wobj.theobject.ATTR_BOUNDY>0)
							{
								worldSpriteBatch.draw(wobj.currentFrame, wobj.pos_x(), wobj.pos_y(), wobj.width/2, wobj.height/2, wobj.width,wobj.height, 1, 1, wobj.rotation());
							}
						}
					}
				}
				catch(Exception e) {}
					
				try
				{
					for(CWorldObject wobj : adr.listWorldObjects)
					{
						if(town.gameWorld.markerObject!=null && wobj.uniqueId==town.gameWorld.markerObject.uniqueId)
							continue;
	
						if(wobj.isHuman())
							wobj.render(worldSpriteBatch, false);
					}
				}
				catch(Exception e) {}

				
			}
		}
	}
	
	public void drawPlacingRects()
	{
		if(town.gameGui.objPlacing!=null)// && gameGui.bObjPlacing)
		{
			CObject po = town.gameGui.objPlacing; 
			CAddress pa = getAddressByPoint(po.pos_x+po.width/2, po.pos_y+po.height/2);
			if(pa!=null)
			{
				worldSpriteBatch.end(); //immer Benötigt, da sonst crash gfx erfolgt
		    	shapeRenderer1.setAutoShapeType(true);
		    	shapeRenderer1.begin();
		    	shapeRenderer1.set(ShapeType.Filled);
		    	for(CWorldObject wobj : pa.listWorldObjects)
		    		wobj.drawPlacingRect(shapeRenderer1);
				shapeRenderer1.end();
				worldSpriteBatch.begin();
			}
		}
	}
	
	public void initWolken()
	{
		worldWolken=new ArrayList<CWolke>();
		for(int i=0;i<40;i++)
		{
			CWolke w1 = new CWolke();
			w1.x=rand.nextInt(mapsize);
			w1.y=rand.nextInt(mapsize);
			w1.w=4000+rand.nextInt(12000);
			w1.h=4000+rand.nextInt(12000);
			w1.z=4+rand.nextInt(12);
			
	 		int maxx=mapsize-(w1.w+1000);
	 		int maxy=mapsize-(w1.h+1000);
	 		if(w1.x>maxx)
	 			w1.x=maxx;
	 		if(w1.y>maxy)
	 			w1.y=maxy;			
	 		
			worldWolken.add(w1);
		}
		
		wolke1dir=0;
		
 		wolke1x=rand.nextInt(town.mapsize);
 		wolke1y=rand.nextInt(town.mapsize);
 		wolke1w=rand.nextInt(10000);
 		wolke1h=rand.nextInt(10000);
 		
 		int maxx=mapsize-15000;
 		int maxy=mapsize-15000;
 		if(wolke1x>maxx)
 			wolke1x=maxx;
 		if(wolke1y>maxy)
 			wolke1y=maxy;
 		
 		wolke2x=rand.nextInt(town.mapsize);
 		wolke2y=rand.nextInt(town.mapsize);
 		wolke2w=rand.nextInt(10000);
 		wolke2h=rand.nextInt(10000);
	}
	
	public void renderWolken(SpriteBatch spriteBatch)
	{
		spriteBatch.setColor(1f, 1f, 1f, 0.07f);
		//if(town.gameInput.iScrollingDown==1||town.gameInput.iScrollingLeft==1||town.gameInput.iScrollingRight==1||town.gameInput.iScrollingUp==1)
		if(CHelper.isMapScrolling(town))
			spriteBatch.setColor(1f, 1f, 1f, 0.03f);
		
		float fspeedvalue=1;
		fspeedvalue = CHelper.getSpeedControllerValue(town.gameGui);     		
		float speed = CHelper.getFPSValue(2f+fspeedvalue/1000);
		
		if(!town.gameWorld.worldPause)
		{
			if(wolke1dir==0)
				wolke1y-=speed;
			else
				wolke1y+=speed;
		}
		
		if(wolke1y>(mapsize-15000))
			wolke1dir=0;
		if(wolke1y<4000)
			wolke1dir=1;
		
		Texture moveTexture = town.gameResourceConfig.textures.get("school_paperball");
		
		for(CWolke w1 : worldWolken)
		{
			if(town.gameCam.zoom > w1.z)
			{
				if(town.gameCam.frustum.boundsInFrustum(w1.x+w1.w/2, w1.y+w1.h/2, 0, w1.w/2, w1.h/2, 0))
				{
					spriteBatch.draw(moveTexture, wolke1x, wolke1y, w1.w, w1.h);
					spriteBatch.draw(moveTexture, w1.x, w1.y, w1.w, w1.h);
				}
			}
		}
		
		//workaround: folgender code muss hier stehen, sonst werden wolken nicht gerendert
		spriteBatch.end();
		spriteBatch.begin();
	}
	
	public void renderGUIElements()
	{
		if(town.gameCam.zoom<=town.frameBufferZoom)
				renderAnimationEvents();
		
		renderAddressPlacingPriceInfo();
		renderPlacingPrice();
		renderCloneRoomPriceAndCollision();
        renderInfoTextEvents();
        
	}
	
	public void setWorldCamera()
	{
	    shapeRenderer1.setProjectionMatrix(town.gameCam.combined);
	    worldSpriteBatch.setProjectionMatrix(town.gameCam.combined);
	}
	
	public void renderAddressPlacingPriceInfo()
	{
		if(town.gameGui.bAddressResizing)
		{
			int pricediff = town.gameGui.getAddressResizingPriceDiff();
			int pricediff_real = pricediff;
			int pricediff_planning = 0; 
			
			if(pricediff_real>0 && addressArchitectPlanningValue>0)
			{
				pricediff_planning = pricediff_real/100*addressArchitectPlanningValue;
				pricediff_real = pricediff_real - pricediff_planning;
			}
			
			town.gameGui.tooltip.setFontBig();
			
			town.gameGui.tooltip.setColor(new Color(1,1,1,0.8f));
			
			if(townMoney<pricediff_real)
				town.gameGui.tooltip.setColor(Color.RED);
			
			if(pricediff_real > 0 && addressArchitectPlanningValue>0)
				town.gameGui.tooltip.drawDirect(Gdx.input.getX(), CHelper.screenToLibGDX(Gdx.input.getY()-30), "$"+pricediff + " - $" + pricediff_planning + " Planning = $" + pricediff_real);
			else
				town.gameGui.tooltip.drawDirect(Gdx.input.getX(), CHelper.screenToLibGDX(Gdx.input.getY()-30), "$"+pricediff_real);
			
			town.gameGui.tooltip.setFontSmall();
		}
		
		if(town.gameGui.bAddressPlacing)
		{
			//worldSpriteBatch.end();
			if(town.gameGui.startAddressPlacing==null)
				return;
			
			Vector3 v3start = new Vector3();
			v3start.x=town.gameGui.startAddressPlacing.x;
			v3start.y=town.gameGui.startAddressPlacing.y;
			
			Vector3 v3end = new Vector3();
			v3end.x = Gdx.input.getX();
			v3end.y = Gdx.input.getY();
			v3end = town.gameCam.unproject(v3end);
			
			int adrSize = (int)(Math.abs(v3end.x-v3start.x)*Math.abs(v3end.y-v3start.y));
			
			int text_adrw=Math.round(Math.abs(v3end.x-v3start.x)/125);
			int text_adrh=Math.round(Math.abs(v3end.y-v3start.y)/125);
			text_adrw=(int) town.getSizeValue2(text_adrw);
			text_adrh=(int) town.getSizeValue2(text_adrh);
			
			
			//Gdx.app.debug("", "trace adrtype: " +gameGui.addressPlacingType);
			int price = CAddress.getPrice(adrSize, town.gameGui.addressPlacingType, town);
			
			int price_real = price;
			int price_planning = 0; 
			
			if(price_real>0 && addressArchitectPlanningValue>0)
			{
				price_planning = price_real/100*addressArchitectPlanningValue;
				price_real = price_real - price_planning;
			}
						
			v3start = town.gameCam.project(v3start);
			v3end = town.gameCam.project(v3end);
			
	    	float sx=v3start.x;
	    	float sy=v3start.y;
	    	float ex=v3end.x;
	    	float ey=v3end.y;
	    	
	    	if(ex<sx)
	    	{
	    		sx=v3end.x;
	    		ex=v3start.x;
	    	}
	    	if(ey<sy)
	    	{
	    		sy=v3end.y;
	    		ey=v3start.y;
	    	}
			
	    	town.gameGui.editorSpriteBatch.begin();
		    
		    //gameGui.font.setColor(0.8f,0,0,1);
		    //gameGui.font.draw(gameGui.editorSpriteBatch, "$" + price, v3start.x+Math.abs(v3end.x-v3start.x)/2, v3start.y-Math.abs(v3end.y-v3start.y)/2);
		    //gameGui.font.setColor(Color.WHITE);
		    
	    	town.gameGui.editorSpriteBatch.setShader(town.gameFont.fontShader);
		    town.gameFont.bfArial.getData().setScale(0.72f);
		    
		    if(price_real <= townMoney)
		    	town.gameFont.bfArial.setColor(0.8f, 0.8f, 0.8f, 0.9f);
		    else
		    	town.gameFont.bfArial.setColor(0.8f, 0f, 0f, 0.8f);
		    
		    String stext = "$" + price;
			if(price_real > 0 && addressArchitectPlanningValue>0)
				stext = "$"+price + " - $" + price_planning + " Planning = $" + price_real;
			
		    
		    town.gameFont.layout.setText(town.gameFont.bfArial, stext);
		    float h1 = town.gameFont.layout.height;
		    int textwidth=(int) town.gameFont.layout.width;
		    town.gameFont.bfArial.draw(town.gameGui.editorSpriteBatch, stext, sx+(ex-sx)/2+textwidth/2, sy+Math.abs(ey-sy)/2+10+30, 0, 0, false);
		    		    
		    town.gameFont.bfArial.getData().setScale(0.68f);
		    town.gameFont.bfArial.setColor(0.8f, 0.8f, 0.8f, 0.8f);
		    String stext2=text_adrw + "m x " + text_adrh + "m";
		    town.gameFont.layout.setText(town.gameFont.bfArial, stext2);
		    textwidth=(int) town.gameFont.layout.width;
		    town.gameFont.bfArial.draw(town.gameGui.editorSpriteBatch, stext2, sx+(ex-sx)/2+textwidth/2, sy+Math.abs(ey-sy)/2+10-h1, 0, 0, false);
		    
		    town.gameFont.bfArial.getData().setScale(1);
		    town.gameGui.editorSpriteBatch.setShader(null);
	    	
		    town.gameGui.editorSpriteBatch.end();
		    //worldSpriteBatch.begin();
		}		
	}
	
	public void renderMouseoverObject()
	{
		if(town.gameGui.bObjMovement || town.gameGui.bObjPlacing || town.gameGui.bAddressPlacing || town.gameGui.bAddressResizing || town.gameGui.bAddressCloning || town.gameGui.bAddressMoving)
			return;
		
		if(mouseOverObject==null)
			return;
		
		if(town.gameGui.dlgShowing())
			return;
		
		if(bObjectInfoTooltipIsRendering)
		{
			bObjectInfoTooltipIsRendering=false;
			return;
		}
		
		//worldSpriteBatch.end();
		
    	if(!town.gameGui.bDeletemode && town.gameGui.deleteObject==null)
    	{
    		int mx = Gdx.input.getX();
    		int my = CHelper.screenToLibGDX(Gdx.input.getY());
    		
    		town.gameGui.tooltip.textLines.clear();
	    	if(mouseOverObject.thehuman!=null)
	    		town.gameGui.tooltip.textLines.add(mouseOverObject.thehuman.getName());
	    	else
	    		town.gameGui.tooltip.textLines.add(mouseOverObject.theobject.objectName);
	    	
	    	town.gameGui.tooltip.setColor(Color.WHITE);
	    	town.gameGui.tooltip.draw(mx, my+50);
	    }
    	
		shapeRenderer1.setProjectionMatrix(town.gameCam.combined);
		shapeRenderer1.setAutoShapeType(true);
		//if(!Gdx.gl.glIsEnabled(GL30.GL_BLEND))
		{
			Gdx.gl.glEnable(GL30.GL_BLEND);
			Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
		}		
		float bs = mouseOverObject.getBodySizeByAge();
		
		shapeRenderer1.begin();
			int w = mouseOverObject.width;
			int h = mouseOverObject.height;
			int s=h;
			if(w>h)
				s=w;
			int lsize=s*2;
			lsize=lsize/3;
			
			if(mouseOverObject.theobject.editoraction.contains("supermarket_shelf"))
				lsize=lsize-80;
			
			shapeRenderer1.setColor(1, 1, 1, 0.11f);
			shapeRenderer1.set(ShapeType.Filled);
			shapeRenderer1.rect((float)mouseOverObject.pos_x()+(mouseOverObject.width*bs)/2-lsize*bs, (float)mouseOverObject.pos_y()+(mouseOverObject.height*bs)/2-lsize*bs, 0, 0, (float)lsize*2*bs, (float)lsize*2*bs, 1f, 1f, 0);				
			
			shapeRenderer1.setColor(0.4f, 0.4f, 0.4f, 0.9f);
			shapeRenderer1.set(ShapeType.Line);
			for(int i=1;i<5;i++)
			{
				//Gdx.app.debug("", "render");
				shapeRenderer1.rect((float)mouseOverObject.pos_x()+(mouseOverObject.width*bs)/2-lsize*bs, (float)mouseOverObject.pos_y()+(mouseOverObject.height*bs)/2-lsize*bs, 0, 0, (float)lsize*2*bs, (float)lsize*2*bs, 1f, 1f, 0);
			}
			shapeRenderer1.end();
		//Gdx.gl.glDisable(GL30.GL_BLEND);
		//worldSpriteBatch.begin();
	}
	
	public void renderDeleteObject() //render selling object
	{
    	if(town.gameGui.bDeletemode && town.gameGui.deleteObject!=null)
    	{
    		int mx = Gdx.input.getX();
    		int my = CHelper.screenToLibGDX(Gdx.input.getY());
    		
    		//worldSpriteBatch.begin();
    		//gameGui.deleteObject.render(worldSpriteBatch, true);
    		//if(worldSpriteBatch.isDrawing())
    		//worldSpriteBatch.end();
	    	
    		town.gameGui.tooltip.textLines.clear();
    		town.gameGui.tooltip.textLines.add("Sell " + town.gameGui.deleteObject.theobject.objectName);
    		town.gameGui.tooltip.textLines.add("$"+town.gameGui.deleteObject.theobject.getSellingPrice());
	    	if(town.gameGui.deleteObject.theobject.getSellingPrice()<0)
	    		town.gameGui.tooltip.setColor(new Color(0.8f,0,0,1));
	    	else
	    		town.gameGui.tooltip.setColor(Color.WHITE);
	    	town.gameGui.tooltip.draw(mx, my+50);
	    	//worldSpriteBatch.begin();
    	}
    	
    	if(town.gameGui.bDeletemode && delAddress!=null)
    	{
    		int mx = Gdx.input.getX();
    		int my = CHelper.screenToLibGDX(Gdx.input.getY());
    		
    		//worldSpriteBatch.begin();
	    	//worldSpriteBatch.end();
    		town.gameGui.tooltip.textLines.clear();
    		town.gameGui.tooltip.textLines.add("Sell address with all objects");
    		town.gameGui.tooltip.textLines.add("$"+delAddress.getSellingPrice("1"));
    		town.gameGui.tooltip.setColor(Color.WHITE);
    		town.gameGui.tooltip.draw(mx, my+50);
	    	//worldSpriteBatch.begin();    		
    	}
	}
	public void renderCloneAddressPriceInfo()
	{
    	if(town.gameGui.bAddressCloning && town.gameGui.cloneAddress!=null)
    	{
    		int mx = Gdx.input.getX();
    		int my = CHelper.screenToLibGDX(Gdx.input.getY());
    		
	    	//worldSpriteBatch.end();
    		town.gameGui.tooltip.textLines.clear();
    		town.gameGui.tooltip.textLines.add("Clone Address and all of its Objects");
	    	
	    	int icprice=town.gameGui.cloneAddress.getCloningPrice();
	    	int addressprice=town.gameGui.cloneAddress.getPrice();
			int price_real = icprice;
			int price_planning = 0; 
			
			if(price_real>0 && addressArchitectPlanningValue>0)
			{
				price_planning = addressprice/100*addressArchitectPlanningValue;
				price_real = price_real - addressprice/100*addressArchitectPlanningValue;
			}
			
			if(price_real > 0 && addressArchitectPlanningValue>0)
				town.gameGui.tooltip.textLines.add("$"+icprice + " - $" + price_planning + " Planning = $" + price_real);
			else
				town.gameGui.tooltip.textLines.add("$"+price_real);
			
	    	//gameGui.tooltip.textLines.add("$"+icprice);
			town.gameGui.tooltip.setColor(Color.WHITE);
	    	if(townMoney<price_real)
	    	{
	    		//gameGui.tooltip.textLines.add("NOT ENOUGH MONEY");
	    		town.gameGui.tooltip.setColor(Color.RED);
	    	}
	    	town.gameGui.tooltip.draw(mx, my+50);
	    	//worldSpriteBatch.begin();    		
    	}
	}
	
	public int getCloneRoomPrice()
	{
		int price=0;
		for(CWorldObject wobj : town.gameWorld.cloneRoomList)
		{
			price+=wobj.theobject.price;
		}
		
		return price;
	}
	
	public CWorldObject getCloneRoom()
	{
		for(CWorldObject wobj : town.gameWorld.cloneRoomList)
		{
			if(wobj.theobject.isRoomObject)
				return wobj;
		}
		
		return null;
	}
	
	public void renderCloneRoomPriceAndCollision()
	{
		if(!town.gameGui.bRoomCloning)
			return;
		
		CWorldObject room = getCloneRoom();
		int price = getCloneRoomPrice();
		
		Vector3 pv = new Vector3();
		pv.x = room.pos_x()+room.width/2;
		pv.y = room.pos_y()+room.height/2;
		pv = town.gameCam.project(pv);
		pv.y+=45;
		town.gameFont.bfArial.getData().setScale(0.7f);
		town.gameFont.layout.setText(town.gameFont.bfArial, "$" + price);
		int pricetextwidth=(int) town.gameFont.layout.width;
		int pricetextheight=(int) town.gameFont.layout.height;
	    
		//Draw Background
		Gdx.gl.glEnable(GL30.GL_BLEND);
		Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
		town.gameGui.shapeRenderer.setAutoShapeType(true);
		town.gameGui.shapeRenderer.begin();
		
		town.gameGui.shapeRenderer.setColor(0.1f, 0.1f, 0.1f, 0.5f);
		town.gameGui.shapeRenderer.set(ShapeType.Filled);
		town.gameGui.shapeRenderer.rect(pv.x-2, pv.y-pricetextheight-2, pricetextwidth+4, pricetextheight+4);
		
		town.gameGui.shapeRenderer.set(ShapeType.Line);
		town.gameGui.shapeRenderer.setColor(0.7f, 0.7f, 0.7f, 0.7f);
		town.gameGui.shapeRenderer.rect(pv.x-2, pv.y-pricetextheight-2, pricetextwidth+4, pricetextheight+4);

		//Check object placing
		if(!town.gameWorld.checkObjectPlacing(room.theobject, null))
		{
			town.gameGui.shapeRenderer.end();
			shapeRenderer1.setProjectionMatrix(town.gameCam.combined);
			shapeRenderer1.begin();
			shapeRenderer1.setColor(0.8f, 0.1f, 0.1f, 0.5f);
			shapeRenderer1.set(ShapeType.Filled);
			shapeRenderer1.rect(room.pos_x(), room.pos_y(), room.width, room.height);
			shapeRenderer1.end();
			town.gameGui.shapeRenderer.begin();
		}
		
		town.gameGui.shapeRenderer.end();
		
		//Draw Price
		town.gameGui.editorSpriteBatch.begin();
		town.gameGui.editorSpriteBatch.setShader(town.gameFont.fontShader);
	    
	    if(price <= townMoney)
	    	town.gameFont.bfArial.setColor(1f, 1f, 1f, 0.8f);
	    else
	    	town.gameFont.bfArial.setColor(0.8f, 0f, 0f, 0.8f);
	    
	    town.gameFont.bfArial.draw(town.gameGui.editorSpriteBatch, "$" + price, pv.x, pv.y);
	    
	    town.gameFont.bfArial.getData().setScale(1);
	    town.gameGui.editorSpriteBatch.setShader(null);
	    town.gameGui.editorSpriteBatch.end();
	}
	
	public void renderPlacingPrice()
	{
		if(town.gameGui.bObjPlacing && town.gameGui.objPlacing!=null)
		{
			if(town.gameGui.objPlacing.editoraction.contains("company_waterworks_groundwaterextractionsystem"))
				return;
			
			CObject op = town.gameGui.objPlacing;
			int price = town.gameGui.objPlacing.placingCountLevel*town.gameGui.objPlacing.placingCountLevel*town.gameGui.objPlacing.price;
			
			if(town.gameGui.rPlacingPrice>0 && (town.gameGui.placeStartX>0 || town.gameGui.placeStartY>0)) //Roadplacing
				price=town.gameGui.rPlacingPrice;
			
			Vector3 pv = new Vector3();
			pv.x = town.gameGui.objPlacing.pos_x+town.gameGui.objPlacing.width/2;
			pv.y = town.gameGui.objPlacing.pos_y+town.gameGui.objPlacing.height/2;
			pv = town.gameCam.project(pv);
			pv.y+=45;
			town.gameFont.bfArial.getData().setScale(0.7f);
			town.gameFont.layout.setText(town.gameFont.bfArial, "$" + price);
			int pricetextwidth=(int) town.gameFont.layout.width;
			int pricetextheight=(int) town.gameFont.layout.height;
			
		    float w = CHelper.roundFloat((float)op.width/125f,2);
		    float h = CHelper.roundFloat((float)op.height/125f,2);
		    String stext2=w + "m x " + h + "m";
		    town.gameFont.layout.setText(town.gameFont.bfArial, stext2);
		    int sizetextwidth=(int) town.gameFont.layout.width;
		    int sizetextheight=(int) town.gameFont.layout.height;
		    
			//Draw Background
		    
			Gdx.gl.glEnable(GL30.GL_BLEND);
			Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
			town.gameGui.shapeRenderer.setAutoShapeType(true);
			town.gameGui.shapeRenderer.begin();
			
			town.gameGui.shapeRenderer.setColor(0.1f, 0.1f, 0.1f, 0.5f);
			town.gameGui.shapeRenderer.set(ShapeType.Filled);
			town.gameGui.shapeRenderer.rect(pv.x-2, pv.y-pricetextheight-2, pricetextwidth+4, pricetextheight+4);
			if(op.editoraction.contains("_floor"))
				town.gameGui.shapeRenderer.rect(pv.x-sizetextwidth/2-2, pv.y-80-sizetextheight-2, sizetextwidth+2, sizetextheight+4);
			
			town.gameGui.shapeRenderer.set(ShapeType.Line);
			town.gameGui.shapeRenderer.setColor(0.7f, 0.7f, 0.7f, 0.7f);
			town.gameGui.shapeRenderer.rect(pv.x-2, pv.y-pricetextheight-2, pricetextwidth+4, pricetextheight+4);
			if(op.editoraction.contains("_floor"))
				town.gameGui.shapeRenderer.rect(pv.x-sizetextwidth/2-2, pv.y-80-sizetextheight-2, sizetextwidth+2, sizetextheight+4);
			
			town.gameGui.shapeRenderer.end();
			
			//Draw Price
			town.gameGui.editorSpriteBatch.begin();
			town.gameGui.editorSpriteBatch.setShader(town.gameFont.fontShader);
		    
		    if(price <= townMoney)
		    	town.gameFont.bfArial.setColor(1f, 1f, 1f, 0.8f);
		    else
		    	town.gameFont.bfArial.setColor(0.8f, 0f, 0f, 0.8f);
		    
		    town.gameFont.bfArial.draw(town.gameGui.editorSpriteBatch, "$" + price, pv.x, pv.y);
		    		    
		    if(op.editoraction.contains("_floor"))
		    {
			    town.gameFont.bfArial.getData().setScale(0.68f);
			    town.gameFont.bfArial.setColor(0.8f, 0.8f, 0.8f, 0.8f);
			    town.gameFont.bfArial.draw(town.gameGui.editorSpriteBatch, stext2, pv.x-sizetextwidth/2, pv.y-80);
		    }
		    
		    if(op.isGroundObject || op.isRoomObject)
		    {
		    	town.gameFont.bfArial.draw(town.gameGui.editorSpriteBatch, "Use the mouse wheel for resizing", pv.x-sizetextwidth/2, pv.y-120);
		    	town.gameFont.bfArial.draw(town.gameGui.editorSpriteBatch, "Press and hold x or c for Horz/Vert", pv.x-sizetextwidth/2, pv.y-140);
		    }
		    
		    town.gameFont.bfArial.getData().setScale(1);
		    town.gameGui.editorSpriteBatch.setShader(null);
		    town.gameGui.editorSpriteBatch.end();
		}
	}
	public void renderAddress(List<CAddress> addressList, Boolean bClone, Boolean bFromGameWorld)
	{
		//if(bFromGameWorld)
		//	worldSpriteBatch.end();

		if(town.gameCam.zoom>30)
		{
			return;
		}
				
		if(town.gameGui.iShowAddresses==0 && !town.gameGui.bObjPlacing && !town.gameGui.bRoomPlacing && !town.gameGui.bAddressCloning && !town.gameGui.bAddressMoving) //für objectplacing und roomplacing immer adressen rendern
		{
			//if(bFromGameWorld)
			//	worldSpriteBatch.begin();
			
			return;
		}
		
		if(addressList.size()>0)
		{
			this.shapeRenderer1.setProjectionMatrix(town.gameCam.combined);
			this.shapeRenderer1.setAutoShapeType(true);
			
			//if(!Gdx.gl.glIsEnabled(GL30.GL_BLEND))
			{
				Gdx.gl.glEnable(GL30.GL_BLEND);
				Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
			}	    	
	    	Vector3 vm = new Vector3();
	    	vm.x = Gdx.input.getX();
	    	vm.y = Gdx.input.getY();
	    	vm = town.gameCam.unproject(vm);
	    	
	    	int mx1=(int) vm.x;
	    	int my1=(int) vm.y;
	    	
			int mx = Gdx.input.getX();
			int my = CHelper.screenToLibGDX(Gdx.input.getY());
			
	    	Boolean bSetAdrRes=false;
	    	
	    	
			if(bFromGameWorld && town.gameCam.zoom<30)
			{

		    this.shapeRenderer1.begin();
		    	for(CAddress adr : addressList)
		    	{
		    		Boolean bDrawObjectByCamera = town.gameCam.frustum.boundsInFrustum(adr.sx, adr.sy, 0, (adr.ex-adr.sx), (adr.ey-adr.sy), 0);
		    		if(!bDrawObjectByCamera)
		    			continue;
		    		
		    		if(adr.checkLineIntersect(vm, 20)!=AddressSide.NONE)
		    		{
		    			if(town.gameGui.bAddressResizingStart && !town.gameGui.bAddressResizing)
		    			{
		    				bSetAdrRes=true;
		    				
		    				if(!town.gameGui.bAddressResizing)
		    					town.gameGui.addressResizing=adr;
		    			}
		    		}
		    		
			    		if(town.gameGui.addressResizing != null && town.gameGui.addressResizing.addressId==adr.addressId && (town.gameGui.bAddressResizing || (town.gameGui.bAddressResizingOver && !town.gameGui.bAddressResizing)))
			    		{
			    			//Resizing Mode
					    	Color dashColor1 = new Color(0f,0f,0f,0.5f);
					    	CHelper.drawDashedRect(dashColor1, shapeRenderer1, 15f, 35, adr.sx+4, adr.sy-4, adr.ex-4, adr.ey+4, 8);
					    	
					    	dashColor1 = new Color(1f,1f,1f,0.35f);
					    	CHelper.drawDashedRect(dashColor1, shapeRenderer1, 15f, 35, adr.sx, adr.sy, adr.ex, adr.ey, 8);
			    		}
			    		else
			    		{
			    			if(town.gameCam.zoom>10)
			    			{
			    				if(town.gameGui.iShowAddresses==1)
			    				{
			    					shapeRenderer1.setColor(1f,1f,1f,0.3f);
			    				}
				    			else if(town.gameGui.iShowAddresses==2)
				    			{
			    					shapeRenderer1.setColor(1f,1f,1f,0.3f);
				    			}
				    			else if(town.gameGui.iShowAddresses==3)
				    			{
			    					shapeRenderer1.setColor(1f,1f,1f,0.5f);
				    			}
			    				
		    					shapeRenderer1.rect(adr.sx,  adr.sy, adr.ex-adr.sx, adr.ey-adr.sy);
			    			}
			    			else
			    			{
				    			//Default
				    			if(town.gameGui.iShowAddresses==1 || town.gameGui.iShowAddresses==2)
				    			{
				    				float f1=0.3f;
				    				if(worldTime.day==12||worldTime.day==11||worldTime.day==1||worldTime.day==2)
				    					f1=0.6f;
				    					
									shapeRenderer1.set(ShapeType.Line);
							    	Color dashColor1 = new Color(1f,1f,1f,f1);
							    	CHelper.drawDashedRect(dashColor1, shapeRenderer1, 10f, 35, adr.sx, adr.sy, adr.ex, adr.ey, 8);
				    			}
				    			else if(town.gameGui.iShowAddresses==3)
				    			{
				    				
									shapeRenderer1.set(ShapeType.Line);
							    	Color dashColor1 = new Color(1f,1f,1f,0.3f);
							    	CHelper.drawDashedRect(dashColor1, shapeRenderer1, 12f, 35, adr.sx, adr.sy, adr.ex, adr.ey, 8);
							    	dashColor1 = new Color(1f,1f,1f,0.2f);
							    	CHelper.drawDashedRect(dashColor1, shapeRenderer1, 12f, 35, adr.sx-3, adr.sy-3, adr.ex+6, adr.ey+6, 8);
							    	CHelper.drawDashedRect(dashColor1, shapeRenderer1, 12f, 35, adr.sx+3, adr.sy+3, adr.ex-6, adr.ey-6, 8);
							    	
				    			}
			    			}
			    		}
	    			//}
		    		
					if(bClone || (town.gameGui.movingAddress!=null && town.gameGui.movingAddress.addressId==adr.addressId))
					{
						if(!checkAddressPlacing(adr.getBoundingRect(CAddress.AddressOverlap.RESIZE)))
						{
							//if(gameGui.clonedAddress!=null)
							//	Gdx.app.debug("", "gameGui.clonedAddress "+gameGui.clonedAddress.addressId);
							//if(town.gameWorld.cloneAddressList.size()>0)
							//	Gdx.app.debug("", "town.gameWorld.cloneAddressList "+town.gameWorld.cloneAddressList.size());
							
							shapeRenderer1.set(ShapeType.Filled);
							this.shapeRenderer1.setColor(1,0,0,0.2f);
							shapeRenderer1.rect(adr.sx-4, adr.sy-4, adr.ex-adr.sx+8, adr.ey-adr.sy+8);
						}
					}
		    	}
		    	
		    	town.gameGui.bAddressResizingOver = bSetAdrRes;
		    	
		    this.shapeRenderer1.end();
			}
		    
		    
			
			
			
			
			
			
    		if(town.gameCam.zoom>30)
    		{
    			//if(bFromGameWorld)
    			//	worldSpriteBatch.begin();
    			return;
    		}
    		
    		if(!bFromGameWorld)
    		{
    		
    		delAddress=null;
    		town.gameGui.cloneAddress=null;
    		town.gameGui.moveAddress=null;
    		town.gameGui.mouseOverAddressInfo=null;
    		//Gdx.app.setLogLevel(5);
    		//Gdx.app.debug("", "test");
    		
	    	for(CAddress adr : addressList)
	    	{
	    		float sc=1.5f-town.gameCam.zoom/10;
	    		if(sc<0.5f)
	    			sc=05f;
	    		if(sc>0.7f)
	    			sc=0.7f;
	    		
	    		sc=0.5f;
	    		if(town.gameCam.zoom<10)
	    			sc=0.6f;
	    		
	    		String adrtype = "address_public";
	    		if(adr.addressType.contains("residential"))
	    			adrtype="address_residential";
	    		int icsize=(int)(40*sc);
	    		
	    		int sx=(int) adr.sx;
	    		int sy=(int) adr.sy;
	    		Vector3 v2 = new Vector3();
	    		v2.x=sx;
	    		v2.y=sy;
	    		v2 = town.gameCam.project(v2);
	    		sx=(int) v2.x+3;
	    		float zfactor=town.gameCam.zoom;
	    		sy=(int) Math.round(v2.y+zfactor)+10;
	    		
	    		String coStr="";
	    		
	    		int counthumans=0;
	    		
	    		for(Object ob : adr.listWorldObjects)
	    		{
	    			CWorldObject wo1 = ((CWorldObject)ob);
	    			if(wo1.isHuman() && !wo1.bIsDead && wo1.iZombie<1)
	    				counthumans++;
	    			
	    			if(((CWorldObject)ob).belongsToCompany!=null)
	    			{
		    			if(!coStr.contains(((CWorldObject)ob).belongsToCompany.getCompanyTypeLabel()))
		    			{
		    				coStr+=((CWorldObject)ob).belongsToCompany.getCompanyTypeLabel()+", ";
		    				break;
		    			}
	    			}
	    		}
	    		
	    		if(coStr.isEmpty())
	    		{
		    		for(Object ob : adr.listWorldObjects_Floors)
		    		{
		    			if(((CWorldObject)ob).belongsToCompany!=null)
		    			{
			    			if(!coStr.contains(((CWorldObject)ob).belongsToCompany.getCompanyTypeLabel()))
			    			{
			    				coStr+=((CWorldObject)ob).belongsToCompany.getCompanyTypeLabel()+", ";
			    				break;
			    			}
		    			}
		    		}
	    		}
	    		
	    		if(coStr.length()>3)
	    			coStr=coStr.substring(0, coStr.length()-2);	    		
	    		
	    		town.gameFont.bfArial.getData().setScale(sc);
	    		town.gameFont.layout.setText(town.gameFont.bfArial, adr.addressName);
	    		float width1 = town.gameFont.layout.width;
	    		float height1 = town.gameFont.layout.height;
	    		float width2 = 0;
	    		float height2 = 0;
	    		
	    		if(coStr.length()>0)
	    		{
	    			town.gameFont.layout.setText(town.gameFont.bfArial, coStr);
		    		width2 = town.gameFont.layout.width;
		    		height2 = town.gameFont.layout.height;
	    		}
	    		
	    		if(adrtype.contains("residential"))
	    			height2 = town.gameFont.layout.height;
	    		
	    		if(width2>width1)
	    			width1=width2;
	    		height1+=height2;
	    		height1+=20;
	    		
	    		if(coStr.length()>0)
	    			height1+=11;
	    		
	    		if(adrtype.contains("residential"))
	    			height1+=11;
	    		
	    		Boolean delMode=false;
	    		Boolean showMode=false;
	    		
	    		//if(gameGui.bDeletemode || gameGui.bAddressCloning || gameGui.bAddressMoving)
	    		{
	    			int gx=sx-5;
	    			int gy=(int) (sy-height1-21);
	    			int gw=(int) (width1+icsize+23);
	    			int gh=(int) height1;
	    			
	    			int csize=0;
	    			if(mx>=gx-csize && mx <= gx+gw && my>=gy-csize && my<=gy+gh)
	    			{
	    				if(town.gameGui.bDeletemode)
	    				{
	    					delMode=true;
	    					delAddress=adr;
	    				}
	    				else if(town.gameGui.bAddressCloning)
	    				{
	    					showMode=true;
	    					town.gameGui.cloneAddress=adr;
	    				}
	    				else if(town.gameGui.bAddressMoving)
	    				{
	    					showMode=true;
	    					town.gameGui.moveAddress=adr;
	    					
	    					if(town.gameGui.moveAddress_Origin_x==0)
	    						town.gameGui.moveAddress_Origin_x=adr.sx;
	    					
	    					if(town.gameGui.moveAddress_Origin_y==0)
	    						town.gameGui.moveAddress_Origin_y=adr.sy;
	    				}
	    				else
	    				{
	    					town.gameGui.mouseOverAddressInfo=adr;
	    				}
	    			}
	    		}
	    		
	    		town.gameGui.shapeRenderer.setAutoShapeType(true);
	    		//if(!Gdx.gl.glIsEnabled(GL30.GL_BLEND))
	    		{
	    			Gdx.gl.glEnable(GL30.GL_BLEND);
	    			Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
	    		}
	    		
	    		town.gameGui.shapeRenderer.begin();
	    		town.gameGui.shapeRenderer.set(ShapeType.Filled);
	    		town.gameGui.shapeRenderer.setColor(0,0,0,0.4f);
		    			
		    			if(delMode)
		    				town.gameGui.shapeRenderer.setColor(0.6f,0.6f,0,0.4f);
		    			
		    			if(showMode || (town.gameGui.mouseOverAddressInfo!=null && adr.addressId==town.gameGui.mouseOverAddressInfo.addressId && !town.gameGui.bObjPlacing && !town.gameGui.bObjMovement))
		    				town.gameGui.shapeRenderer.setColor(0,1,0,0.2f);
		    			
		    			float awidth=width1+icsize+23;
		    			if(awidth<60)
		    				awidth=60;
		    			
		    			Boolean drawrect=false;
		    			
		    			//if(!gameGui.dlgShowing() && !gameGui.bPaintObject && !gameGui.bObjPlacing && !gameGui.bObjMovement)
		    			if(!town.gameGui.dlgShowing() && !town.gameGui.bPaintObject && !town.gameGui.bObjPlacing)
		    			{
			    			if(town.gameGui.mouseOverAddressInfo!=null && town.gameGui.mouseOverAddressInfo.addressId==adr.addressId)
			    				drawrect=true;
			    			
			    			if(delMode || showMode || drawrect || worldTime.day==12 || worldTime.day==11 || worldTime.day==1 || worldTime.day==2){
			    				if(town.gameGui.iShowAddresses>1)
			    					town.gameGui.shapeRenderer.rect(sx-5, sy-height1-21, awidth, height1);
			    			}
		    			}
		    			
		    			town.gameGui.shapeRenderer.set(ShapeType.Line);
		    			town.gameGui.shapeRenderer.setColor(0.6f,0.6f,0.6f,0.4f);
		    			if(delMode || showMode || drawrect || worldTime.day==12 || worldTime.day==11 || worldTime.day==1 || worldTime.day==2) {
		    				town.gameGui.shapeRenderer.rect(sx-5, sy-height1-21, awidth, height1);
		    			}
		    			
		    			town.gameGui.shapeRenderer.end();
			    //Gdx.gl.glDisable(GL30.GL_BLEND);

		    			
	    		if(town.gameGui.iShowAddresses<2)
	    			return;
		    			
	    		if(!town.gameGui.editorSpriteBatch.isDrawing())
	    			town.gameGui.editorSpriteBatch.begin();
	    		
	    		float a1=0.4f;
	    		float c1=1f;
	    		
	    		
	    		if(town.gameGui.iShowAddresses==3)
	    			a1=0.7f;
	    		
	    		town.gameGui.editorSpriteBatch.setColor(c1,c1,c1,a1);
	    		town.gameGui.editorSpriteBatch.setShader(null);
	    		town.gameGui.editorSpriteBatch.draw(gameResourceConfig.textures.get(adrtype), sx-1, sy-28-icsize, icsize, icsize);
	    		
	    		town.gameFont.bfArial.setColor(c1, c1, c1, a1);
	    		town.gameGui.editorSpriteBatch.setShader(town.gameFont.fontShader);
	    		town.gameFont.bfArial.draw(town.gameGui.editorSpriteBatch, adr.addressName, sx+icsize+11, sy-30);
	    		town.gameFont.bfArial.draw(town.gameGui.editorSpriteBatch, coStr, sx+icsize+11, sy-47-sc*18);	   
	    		
	    		if(adrtype.contains("residential"))
	    		{
	    			town.gameGui.editorSpriteBatch.setShader(null);
	    			town.gameGui.editorSpriteBatch.setColor(c1,c1,c1,a1);
	    			town.gameGui.editorSpriteBatch.draw(gameResourceConfig.textures.get("guiinfo_population"), sx-1, sy-67+5-sc*18, icsize-6, icsize-6);
	    			town.gameGui.editorSpriteBatch.setShader(town.gameFont.fontShader);
		    		town.gameFont.bfArial.setColor(c1,c1,c1, a1);
		    		//town.gameFont.bfArial.draw(gameGui.editorSpriteBatch, adr.listWorldObjects.stream().filter(item->item.isHuman()).count()+"", sx+icsize+11, sy-47-sc*18);
		    		town.gameFont.bfArial.draw(town.gameGui.editorSpriteBatch, counthumans+"", sx+icsize+11, sy-47-sc*18);
	    		}
		    	
		    	if(town.gameGui.editorSpriteBatch.isDrawing())
		    		town.gameGui.editorSpriteBatch.end();
	    	}
    		}
	    	
	    	town.gameFont.bfArial.getData().setScale(1);
	    	worldSpriteBatch.setShader(null);
	    	town.gameGui.editorSpriteBatch.setShader(null);
		}
		
		//worldSpriteBatch.begin();
	}
	public void renderAddressPlacing()
	{
		 //worldSpriteBatch.end();
		if(town.gameGui.bAddressPlacing)
		{
	    	Vector3 start = town.gameGui.startAddressPlacing;
	    	if(start==null)
	    		return;
			
			this.shapeRenderer1.setProjectionMatrix(town.gameCam.combined);
			this.shapeRenderer1.setAutoShapeType(true);
			worldSpriteBatch.end();
			//if(!Gdx.gl.glIsEnabled(GL30.GL_BLEND))
			{
				Gdx.gl.glEnable(GL30.GL_BLEND);
				Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
			}	    	
	    	Vector3 end = new Vector3();
	    	end.x = Gdx.input.getX();
	    	end.y = Gdx.input.getY();
	    	end = town.gameCam.unproject(end);
	    	
	    	float sx=start.x;
	    	float sy=start.y;
	    	float ex=end.x;
	    	float ey=end.y;
	    	if(ex<sx)
	    	{
	    		sx=end.x;
	    		ex=start.x;
	    	}
	    	if(ey<sy)
	    	{
	    		sy=end.y;
	    		ey=start.y;
	    	}
	    	
	    	Boolean bPlacingOK = checkAddressPlacing(new Rectangle(sx, sy, Math.abs(ex-sx), Math.abs(ey-sy)));
	    	
		    this.shapeRenderer1.begin();
		    
		    	shapeRenderer1.set(ShapeType.Filled);
		    	this.shapeRenderer1.setColor(0.27f, 0.27f, 0.27f, 0.6f);
				if(!bPlacingOK)
					this.shapeRenderer1.setColor(1, 0, 0, 0.3f);
				shapeRenderer1.rect(start.x, start.y, end.x-start.x, end.y-start.y);
				
				this.shapeRenderer1.setColor(0.8f, 0.8f, 0.8f, 0.8f);
				shapeRenderer1.set(ShapeType.Line);
				Color dashColor1 = new Color(1f,1f,1f,0.3f);
		    	CHelper.drawDashedRect(dashColor1, shapeRenderer1, 8, 35, start.x, start.y, end.x, end.y, 8);
		    	
		    this.shapeRenderer1.end();
		    
		    //Gdx.gl.glDisable(GL30.GL_BLEND);
			
			worldSpriteBatch.begin();
		}
	}
	public void renderAnimationEvents()
	{
		ArrayList<CAnimationTextEvent> delList = new ArrayList<CAnimationTextEvent>();
		int tempid=0;
		for(CAnimationTextEvent event : animationEvents)
		//if(animationEvents.size()>0)
		{
			//Gdx.app.debug("", ""+event.);
			
			//CAnimationEvent event = animationEvents.get(0);
			if(event.targetObject!=null && event.targetObject.uniqueId==tempid)
				continue;
			else if(event.targetObject!=null)
				tempid=event.targetObject.uniqueId;
			
			event.render();
			
			if(event.isFinished())
			{
				delList.add(event);
				//animationEvents.remove(event);
			}
		}
		
		animationEvents.removeAll(delList);
	}
	
	public void renderInfoTextEvents()
	{
		for(CInfoTextEvent ev1 : infoEvents)
			ev1.render();
		
		if(infoEvents.size()>4)
		{
			int index=-1;
			
			int count=0;
			for(int i=0;i<infoEvents.size();i++)
				if(infoEvents.get(i).posy<0)
					count++;
			
			if(count>3)
			{
				if(infoEvents.get(0).posy<0)
					index=0;
				else if(infoEvents.get(1).posy<0)
					index=1;
				else if(infoEvents.get(2).posy<0)
					index=2;
				else if(infoEvents.get(3).posy<0)
					index=3;
				
				if(index>-1)
				{
					//int index=infoEvents.size()-1;
					if(infoEventMoving==infoEvents.get(index).eventid)
					{
						infoEventMoving=0;
					}
					
					infoEvents.remove(index);
				}
			}
		}
		
		if(!worldPause)
		{
			for(CInfoTextEvent ev1 : infoEvents)
				ev1.setDelTimer();
		}
		
		if(infoEvents.size()>0 && infoEvents.get(0).bRemove)
		{
			if(infoEventMoving==infoEvents.get(0).eventid)
				infoEventMoving=0;
			
			infoEvents.remove(0);
		}
	}
	
	public void renderSpriteMoveEvents(int zorder, SpriteBatch spriteBatch)
	{
		if(worldPause)
			return;
		
		try
		{
			ArrayList<CSpriteMoveEvent> delList = new ArrayList<CSpriteMoveEvent>();
			
			for(CSpriteMoveEvent event : spriteMoveEvents)
			{
				if(event.zorder==zorder)
				{
					event.render(spriteBatch);
					
					if(event.bIsFinished)
					{
						delList.add(event);
					}
				}
			}
			
			spriteMoveEvents.removeAll(delList);
		}
		catch(Exception e)
		{
			//..
		}
	}
	
	
	void renderObjectZone(SpriteBatch spriteBatch)
	{
		//Wenn Heizung markiert oder geplaced wird
		
		CObject obj=null;
		if(town.gameWorld.markerObject!=null)
			obj=town.gameWorld.markerObject.theobject;
		if(town.gameGui.objPlacing!=null)
			obj=town.gameGui.objPlacing;
		
		if(obj!=null && obj.editoraction!=null)
		{
			//************
			//Spritebatch
			//************
			/*
			if(obj.editoraction.contains("radiator"))
			{
				Vector2 v2 = CHelper.moveVectorByRotationS2D(obj.pos_x, obj.pos_y, obj.width/2, obj.height-obj.getRadiatorHeatingPower()/2-obj.height, obj.width/2, obj.height/2, obj.rotation);
				Texture wrect = gameResourceConfig.textures.get("poly_whiterect");
				spriteBatch.setColor(0.4f,0.3f,0.3f,0.85f);
				spriteBatch.draw(wrect, v2.x-obj.getRadiatorHeatingPower()/2, v2.y-obj.getRadiatorHeatingPower()/2, obj.getRadiatorHeatingPower()/2, obj.getRadiatorHeatingPower()/2, obj.getRadiatorHeatingPower(), obj.getRadiatorHeatingPower(), 1, 1, obj.rotation, 0, 0, wrect.getWidth(), wrect.getHeight(), false, false);
				spriteBatch.end();
				spriteBatch.begin();
			}
			*/
			
			//*************
			//ShapeRenderer
			//*************
			if(obj.editoraction.contains("_light"))
			{
				spriteBatch.end();
				
				//if(!Gdx.gl.glIsEnabled(GL30.GL_BLEND))
				{
					Gdx.gl.glEnable(GL30.GL_BLEND);
					Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
				}				
				shapeRenderer1.setAutoShapeType(true);
				shapeRenderer1.setColor(1,1,1,0.7f);
				shapeRenderer1.begin();
				
					Circle c = obj.getLightZoneCircle();
					shapeRenderer1.set(ShapeType.Filled);
					shapeRenderer1.setColor(0.3f,0.3f,0.3f,0.3f);
					shapeRenderer1.circle(c.x, c.y, c.radius);
				
				shapeRenderer1.end();
		    	//Gdx.gl.glDisable(GL30.GL_BLEND);
				spriteBatch.begin();
			}
		}
	}
	
	//RENDER<---------------------------------------------

}











