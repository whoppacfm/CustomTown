package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bitfire.postprocessing.PostProcessor;
import com.bitfire.postprocessing.effects.Pixelator;
import com.bitfire.utils.ShaderLoader;
import com.mygdx.game.CGuiDialog_List.ListType;

import java.io.BufferedReader;
import java.util.Random;
import java.util.TimerTask;

//import com.codedisaster.steamworks.*;


public class CTown {
		
	public Boolean isGameDemo=false;
	
	public CConfigIni gameConfigIni;
	public CResourceConfig gameResourceConfig;
	public CAudio gameAudio;
	public CWorld gameWorld;
	public CGui gameGui;
	public CInput gameInput;
	public CFont gameFont;
	public OrthographicCamera gameCam;
	Matrix4 uiMatrix;
	
	//public Boolean useSteam=false;
	
	//public SteamUserStats userStats;
	
	/*
	public SteamUserStatsCallback userStatsCallback = new SteamUserStatsCallback() {
		@Override
		public void onUserStatsReceived(long gameId, SteamID steamIDUser, SteamResult result) {
			System.out.println("User stats received: gameId=" + gameId + ", userId=" + steamIDUser.getAccountID() +
					", result=" + result.toString());

			int numAchievements = userStats.getNumAchievements();
			System.out.println("Num of achievements: " + numAchievements);

			for (int i = 0; i < numAchievements; i++) {
				String name = userStats.getAchievementName(i);
				boolean achieved = userStats.isAchieved(name, false);
				System.out.println("# " + i + " : name=" + name + ", achieved=" + (achieved ? "yes" : "no"));
			}
		}

		@Override
		public void onUserStatsStored(long gameId, SteamResult result) {
			System.out.println("User stats stored: gameId=" + gameId +
					", result=" + result.toString());
		}

		@Override
		public void onUserStatsUnloaded(SteamID steamIDUser) {
			System.out.println("User stats unloaded: userId=" + steamIDUser.getAccountID());
		}

		@Override
		public void onUserAchievementStored(long gameId, boolean isGroupAchievement, String achievementName,
											int curProgress, int maxProgress) {
			System.out.println("User achievement stored: gameId=" + gameId + ", name=" + achievementName +
					", progress=" + curProgress + "/" + maxProgress);
		}

		@Override
		public void onLeaderboardFindResult(SteamLeaderboardHandle leaderboard, boolean found) {
			System.out.println("Leaderboard find result: handle=" + leaderboard.toString() +
					", found=" + (found ? "yes" : "no"));

			if (found) {
				System.out.println("Leaderboard: name=" + userStats.getLeaderboardName(leaderboard) +
						", entries=" + userStats.getLeaderboardEntryCount(leaderboard));
			}
		}
		
		@Override
		public void onGlobalStatsReceived(long gameId, SteamResult result) {
			System.out.println("Global stats received: gameId=" + gameId + ", result=" + result.toString());
		}		
		
		@Override
		public void onLeaderboardScoreUploaded(boolean success,
											   SteamLeaderboardHandle leaderboard,
											   int score,
											   boolean scoreChanged,
											   int globalRankNew,
											   int globalRankPrevious) {

			System.out.println("Leaderboard score uploaded: " + (success ? "yes" : "no") +
					", handle=" + leaderboard.toString() +
					", score=" + score +
					", changed=" + (scoreChanged ? "yes" : "no") +
					", globalRankNew=" + globalRankNew +
					", globalRankPrevious=" + globalRankPrevious);
		}		
		
		@Override
		public void onLeaderboardScoresDownloaded(SteamLeaderboardHandle leaderboard,
												  SteamLeaderboardEntriesHandle entries,
												  int numEntries) {

			System.out.println("Leaderboard scores downloaded: handle=" + leaderboard.toString() +
					", entries=" + entries.toString() + ", count=" + numEntries);

			int[] details = new int[16];

			for (int i = 0; i < numEntries; i++) {

				SteamLeaderboardEntry entry = new SteamLeaderboardEntry();
				if (userStats.getDownloadedLeaderboardEntry(entries, i, entry, details)) {

					int numDetails = entry.getNumDetails();

					System.out.println("Leaderboard entry #" + i +
							": steamIDUser=" + entry.getSteamIDUser().getAccountID() +
							", globalRank=" + entry.getGlobalRank() +
							", score=" + entry.getScore() +
							", numDetails=" + numDetails);

					for (int detail = 0; detail < numDetails; detail++) {
						System.out.println("  ... detail #" + detail + "=" + details[detail]);
					}

				}

			}
		}
	
	};
	*/

	
	//private OrthographicCamera lightCam;
	
	
	
	SpriteBatch townSpriteBatch;
	BitmapFont townBitmapFont;
	
	
	
	CPlayer player;
	CScreenInfo screenInfo;
    CFramebuffer frameBuffer;
    PostProcessor postProcessor;
    //PostProcessor postProcessor2;
    public CCursor cursor; 
    public Viewport viewport;
    public Random rand;
    
    public Texture drawGuiTexture1;
    public CObject drawGuiObject;
    public int drawGuiTexture1_x;
    public int drawGuiTexture1_y;
    public int drawGuiTexture1_w;
    public int drawGuiTexture1_h;
    
    public Boolean bRenderGui=true;
    
    public int frameBufferZoom=15;
    public int frameBufferZoom_Residents=20;
    public int frameBufferZoom_Cars=100;
    public int zoomRender_noShadows=10;
    public int frameBufferZoom_norender=22;
    
	public Boolean bZombieApocalypse=true;
	public Boolean bNoRealEstate=false;
	public Boolean bConstructionMode=false;
	
    
    //public float fSizeFactor=2f;
    public float fSizeFactor=1f;
    
    
    //INIT------------------------------------------------------------>
    public float deltaSecondsDelta=1.0f;
    public float movementSpeedDelta=1.0f;
    public int startingMonth=5;
    public int startingAddressSize=600;
    
    public int fillFridgeCost = 2000;
    
    
    //Initial Starting Money
	public int startingMoney=250000; //Townmoney
	
	public int unlockCompanyPrice=25000;
	
	public float startingZoom=3.0f;
	
	public int workHappinessDownDay=18;
	
	public int zombieStartingDay=1;
	public int zombieEntranceStartingDay=0;
	public int sickStartingDay=3; //Ab welchem Tag können Einwohner krank werden
	public int sickStartingDay_severedisease=4; 
	public int sickStartingDay_contagious=6;
	
	public int demandStartingDay_fitnessstudio=4;
	public int demandStartingDay_pub=4;
	public int demandStartingDay_breakroom=5;
	
	public int demandStartingDay_dinner=5;
	public int demandStartingDay_tv=5;
	public int demandStartingDay_tv2=5;
	public int demandStartingDay_sportscar=5;
	public int demandStartingDay_book=5;
	public int demandStartingDay_sandpit=5;
	public int demandStartingDay_slide=5;
	public int demandStartingDay_seesaw=5;
	
	
	//public int demandStartingDay_trees=0;
	//public int demandStartingDay_flowers=0;
	public int demandStartingDay_church=5;
	public int spawnFamiliesStartingDay=3;
	public int setRoomPrice=-1;
	public String gameMode="";
	
	//Max Object Values
	int supermarket_shelf_foodstock_max=400;
	int supermarket_shoppingcart_foodstock_max=100;
	int supermarket_warehouse_foodstock_max=400;//2000;
	int fridge_foodstock_max=100;
	int company_officeworkoutput_max=500;
	
	//Action Values
	int action_changeclothesvalue=3;
	
	//Initial Object Values
	int initial_supermarket_shelf_foodstock=0; //supermarket_shelf_foodstock_max;
	int initial_supermarket_warehouse_foodstock=0; //supermarket_warehouse_foodstock_max;
	int initial_fridge_foodstock=0; //fridge_foodstock_max;
	int initial_company_officeworkoutput=0; //company_officeworkoutput_max;
	int initial_paintobject_price=0;
	
	float initial_maxschooleducation=1f;//1.5f;
	float initial_maxcollegeeducation=3f;
	float initial_requirededucationforcollege=1f;
	
	int architectcosts_clone=0;
	int architectcosts_resize=250;
	int architectcosts_move=250;
	int architectcosts_planning=550;
	
	//Output and Consumption Delta Values
	//Nur für Test
	float delta_energyconsumption=1;
	float delta_waterconsumption=1;
	float delta_energyoutput=2;
	float delta_wateroutput=2;
	float delta_workoutputconsumption=1;
	float delta_fuelconsumption=1;
	
	//Research und Object Design Delta
	public float resDelta=1;	
	public float desDelta=1;
	
	//Grants/Penalties
	public float grantsDelta=1f;
	public float moneyDelta=1f;
	public float penaltyDelta=1f;
	
	//Preise
	float objectPriceDelta=1.0f;
	float residentPriceDelta=1.0f;
	float publicAdrPriceDelta=1.0f;
	float residentialAdrPriceDelta=1.0f;
	
	
	//Resident Attributes
	
    //je höher der key desto schneller muss aktion von resident wieder ausgeführt werden
	public float eatDelta=1f;
	public float cleanDelta=1f;
	public float toiletDelta=1f;
	public float clothingDelta=1f;
	
	//je höher der key desto schneller geht resident attribut hoch oder runter
	public float happinessDeltaPlus=1f;
	public float healthDeltaPlus=1f;
	public float fitnessDeltaPlus=1f;
	public float intelligenceDeltaPlus=1f;
	
	public float happinessDeltaMinus=0.5f;
	public float healthDeltaMinus=0.5f;
	public float fitnessDeltaMinus=1f;
	public float intelligenceDeltaMinus=1f;
	
	public float educationDeltaPlus=1f;
	
	public float increaseskillTaskDelta=1f; //wird einmalig nach action ausgeführt //base: (50*0.07f)
	public float increaseskillJobDelta=1f; //wird pro arbeitsstunde ausgeführt //base: (50*0.003f)
	
	
	//Gameplay / GUI
	
	//Floor Placing, Number of Floor Tiles
	public int placingCountMax=10;
	
	//-------------------------------------------------
	
	public Boolean bDevMode=false; //show FPS, change months, shortcuts, ...
	public Boolean bResearched=false; //Alle Objekte sind erforscht
	
	public Boolean bShowFPS=false;
	
	public Boolean bEncodeSavegames=true;
	
	public Boolean bUseTextureAtlas=false; //Performance Optimierung wenn viele GFX gleichzeitig gerendert werden
										   //-> Frustum wird derzeit benutzt
										   //Achtung: Heads werden noch nicht aus Atlas geladen
										   //Ziel: alle Graphiken in einen atlas (gfx verkleinern, automatisiert, evtl über texturepacker einstellung)
										   //Tool googlen: gfx verkleinern um % oder tool entwickeln
										   //CObject: Anstatt Texture -> TextureRegion verwenden 
										   //mit objectres werden derzeit die graphiken falsch ausgelesen aus textureatlas
	
	public Boolean bBaseGround=false; //Objects need Base Grounds
	//public Boolean bComfort=true;
	public Boolean bRenderPathfinding=false;
	public Boolean bRenderPathfinding_Road=false;
	public Boolean bRenderPathfinding_Address=false;
	public Boolean bRenderPathfinding_Footpath=false;
	public Boolean bRenderPathOpenNodes=false;
	public Boolean bDebugTrace_FootpathPathmap=false;
	public Boolean bDrawBathmatZoneBoundingPolygons=false;
	public Boolean bDrawBoundingPolygons=false;
	public Boolean bDrawTVZoneBoundingPolygons=false;
	public Boolean bDrawSchoolZoneBoundingPolygons=false;
	public Boolean bDrawCollegeZoneBoundingPolygons=false;
	public Boolean bDrawBedZonePolygons=false;
	public Boolean bDrawInteriorDecorZonePolygons=false;
	public Boolean bDrawNighttableZonePolygons=false;
	public Boolean bRenderBox2D=false;
	public Boolean bUseMipMapping=true;
	public Boolean bNoRequirements=false; //kein water oder energy benötigt, foodfilling sind voll, kein workinput
	public Boolean bDebugActions=false; //Actions werden öfter ausgeführt
	public Boolean bDebutSleeping=false; //Sleepvalue startet kleiner 
	public Boolean bDebugLogging=false;
	public Boolean bDebugShowRain=false;
	public Boolean bDebugShowSnow=false;
	public Boolean bUseFramebuffer=false; //pixelmode: je weiter man einzoomz desto verpixelter wird es eingestellt
	public Boolean bUsePostProcessor=false; //postprocessing pixel ist nicht für zoom eingerichtet
	public Boolean bResearchShowCompleteList=false; //Zeige alle Objekte in Research List an egal ob erforscht oder nicht
	public int lodvalue=2;
	public int nrthreads=1;
	
	float garbageCollectorTimer;
	
	//public int backgroundsize=1000;
	public int backgroundsize=900;
	//public Color dialogColor=new Color(0.02f, 0.02f, 0.02f, 0.7f);
	//public Color dialogColor=new Color(0.2f, 0.2f, 0.2f, 0.46f);
	//public Color dialogColor=new Color(0.2f, 0.2f, 0.2f, 0.42f);
	public Color dialogColor=new Color(0.2f, 0.2f, 0.2f, 0.62f);
	public Color dialogRahmenColor=new Color(0.5f, 0.5f, 0.5f, 0.46f);
	public Color dialogFontColorList=new Color(1, 1, 1, 0.88f);
	//dlgBackColor=new Color(0.2f, 0.2f, 0.2f, 0.46f);
	
	//public int mapsize=80000;
	//public int mapsize=55000; //performance
	//public int mapsize=100000;
	public int mapsize=80000;
	
	//public int nodesize=64; //beeinflusst größe der wall objekte, pathfinding, kollision
	public int nodesize=100; // (int) getSizeValue(32); //32 gut für pathfinding, schlecht für performance?, hintergrund warum nicht 64 -> level alle liegen im bett stehen nicht auf, da sie keinen path finden
	public int wallSize=100; //default
		
	//public int roadsize=64*9; //gut für gehwegplatzierung
	//public int roadrastersize=roadsize/2;
	//public int roadsize=64*5;
	public int roadsize=(int) getSizeValue(400);
	public int roadrastersize=roadsize;
	
	//public int footpathsize=64*3;
	public int footpathsize=(int) getSizeValue(200);
	
	public int defensewallsize=(int) getSizeValue(235);
	
	//public int groundsize=64*3;
	//public int groundsize=64*2;
	//public int groundsize=250;
	//public int groundsize=500;
	public int groundsize=(int) getSizeValue(800);
	public int groundprice=250;
	
	public int groundbasesize=(int) getSizeValue(350);
	
	public int floorSize=(int) getSizeValue(256); //Achtung bei Änderung muss Size/Raster für Parkingspace/Garage angepasst werden -> adr moving/cloning
	
	//public int floorrastersize=(int)getSizeValue(32);	
	public int floorrastersize=1;
	public int sizechangervalue=32;
	public int defaultFloorPlacingCountLevel=1;
	
	public int maxPathFindingsPerStep=3000; //Das beeinflusst die Performance im Moment nicht -> prüfen warum
	
	//speed: 0.02, lights: 1
	public float b2dvalueSize=0.02f;
	public float b2dvaluePos=0.02f;
	
	public int daysInYear=12;
	
	public Boolean bStartLoadingSaveGame;
	public String sLoadSaveGame;
	public Boolean bLoadingSaveGame;
	public int iSaveGameLoaded;
	
	private int iLoaded;
	private Boolean bLoadedResources;
	private Boolean bStartInit;
	private java.util.Timer autoModeTimer;
	private java.util.Timer achievementTimer;
	
	public TextureAtlas textureAtlas;
	
//	//To cancel any task:
//	this.autoModeTimer.purge();
//	//To Start a Task:
//	this.autoModeTimer.schedule(new TimerTask() {
//	            @Override public void run() {
//	            //Your code goes Here
//	            }}, 0);
		
	//INIT<------------------------------------------------------------
		
//	Stage stage1;
//	Skin skin1;
//	TextField textfield1;

//	Stage stage1;
//	Table table1;
//	Actor actor1;
//	Group group1;
//	Skin skin;
	
	public float getSizeValue(float value)
	{

		return value;
		
		//führt zu errors
		/*
		if(fSizeFactor>0)
			return value/fSizeFactor;
		
		return value;
		*/
	}
	
	public float getSizeValue2(float value)
	{

		return value;
		
		//führt zu errors
		/*
		if(fSizeFactor>0)
			return value*fSizeFactor;
		
		return value;
		*/
	}
	
	
	public void setAchievement(String name)
	{
		return;
		
		/*
		if(!useSteam)
			return;
		
		if(isGameDemo)
			return;
		
    	this.achievementTimer.schedule(new TimerTask() 
    	{
            @Override public void run() 
            {
                Gdx.app.postRunnable(new Runnable() 
                {
                	@Override
                    public void run() {
                		//if(!userStats.isAchieved(name, true))
                		//{
                		//	userStats.setAchievement(name);
                		//	userStats.storeStats();
                		//	gameAudio.playSound("EFFECT_ACHIEVEMENT", 0, false);
                		//}                	}
                }
                	);
            }
        }, 0);
        */
	}
	
	public void clearAchievements()
	{
		//userStats.clearAchievement("startgame");
	}
	
	public void init(String mode)
	{
		//if(useSteam) {
		//	userStats = new SteamUserStats(userStatsCallback);
		//}
		
		this.achievementTimer=new java.util.Timer();
		//clearAchievements();
		
		
		CTime time1 = new CTime(this);
		
		//time1.init(457722);
		
		time1.init(529500);
		
		
		try
		{
			garbageCollectorTimer=0;
			
			//Gdx.app.setLogLevel(0);
			
			initTownVariables(mode, ""); //für basis attribute für CObjects
			
			rand=new Random();
			gameFont = new CFont();
			
			Boolean bBuildTextureAtlas=false;
			
			if(bBuildTextureAtlas)
			{
				Settings settings = new Settings();
				//settings.maxWidth=8192;
				//settings.maxHeight=8192;
				settings.maxWidth=16384;
				settings.maxHeight=16384;
				//settings.maxWidth=131072;
				//settings.maxHeight=131072;
				
				settings.pot=true;
		        TexturePacker.process(settings, "bin/gfx", "bin/textureatlas", "htp_gfx");
		        
				if(1==1)
		        	return;
				
				//Beispiel:
				//TextureAtlas atlas;
				//atlas = new TextureAtlas(Gdx.files.internal("./bin/textureatlas/htp_gfx.atlas"));
				////atlas = new TextureAtlas(Gdx.files.absolute("I:/Tools_Dev/htp_source/desktop/bin/textureatlas/htp_gfx.atlas"));
				//TextureRegion region1 = atlas.findRegion("animals/b1");
			}	
			
			sLoadSaveGame="";
			bStartLoadingSaveGame=false;
			bLoadingSaveGame=false;
			iSaveGameLoaded=100;
			
			iLoaded=0;
			bLoadedResources=false;
			bStartInit=false;
			
			gameConfigIni = new CConfigIni();
		    DisplayMode[] modes = Gdx.graphics.getDisplayModes();
		    gameConfigIni.init(modes, this);
		    gameConfigIni.setDisplayMode(gameConfigIni.screenWidth, gameConfigIni.screenHeight, gameConfigIni.windowed);
		    
			townSpriteBatch = new SpriteBatch();
			townBitmapFont = new BitmapFont();
		}
		catch(Exception e)
		{
			CHelper.writeError(e.getMessage(), e.getStackTrace(), e);
		}
	}
		
	public void initResources2()
	{
		//Resourcen, die nicht beim Start benötigt werden asynchron nachladen
		//gameAudio.loadAll();
		gameResourceConfig.initAudio(1);
	}
	
	public void initResources()
	{
		try
		{
			if(bUseTextureAtlas && textureAtlas==null)
				textureAtlas = new TextureAtlas(Gdx.files.internal("./bin/textureatlas/htp_gfx.atlas"));
			
			//gameFont = new CFont();
			gameResourceConfig = new CResourceConfig();
			gameResourceConfig.init(this);
			gameAudio=new CAudio();
			gameAudio.initFirstLoad(this);
			//gameAudio.play("birds1");
			
			bLoadedResources=true;
			setAchievement("startgame");
		}
		catch(Exception e)
		{
			CHelper.writeError(e.getMessage(), e.getStackTrace(), e);
		}
	}
	
	public void initStuff()
	{
		cursor = new CCursor();
		CTime townTime = new CTime(this);
	    float w = Gdx.graphics.getWidth();
	    float h = Gdx.graphics.getHeight();
	    
	    gameCam = new OrthographicCamera(w,h);
	    gameCam.zoom = 3.0f;
	    gameCam.update();
	    
		gameWorld = new CWorld(this);
		CWorld.mapsize=mapsize;
		CWorld.nodesize=nodesize;
		gameWorld.wallSize=wallSize;
		gameWorld.floorSize=floorSize;
		gameWorld.b2dvalueSize = b2dvalueSize;
		gameWorld.b2dvaluePos = b2dvaluePos;
		gameWorld.init(gameCam, gameResourceConfig);
		gameWorld.maxPathfindingsPerStep = maxPathFindingsPerStep;
		gameWorld.bRenderPathfinding=bRenderPathfinding;
		gameWorld.bRenderBox2D = bRenderBox2D;
		gameWorld.bDebugActions = bDebugActions;
		gameWorld.worldTime = townTime;
		gameCam.position.set(gameWorld.mapsize/2, gameWorld.mapsize/2, 0);
		
		if(gameGui==null)
		{
			gameGui = new CGui();
			gameGui.init(this);
		}
		
		gameWorld.initCompanytypes();
		
		//gameWorld.gameGui=gameGui;
		
	    gameInput = new CInput(this, this);
	    gameInput.town=this;
	    Gdx.input.setInputProcessor(gameInput);
	    
	    //player = new CPlayer(gameWorld);
	    //player.init(12000, 12000);
	    
	    //frameBuffer = new CFramebuffer(this);
	    
	    //initPostprocessor(3);
	    
	    screenInfo = new CScreenInfo(this);   
	    
	    //Scene 2D Test
		//stage1 = new Stage();
		//Gdx.input.setInputProcessor(stage1);
		
		//	    table1 = new Table();
		//	    table1.setFillParent(true);
		//	    stage1.addActor(table1);
		//	    skin = new Skin();
		//	    
		//	    Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888); 
		// 		pixmap.setColor(Color.WHITE); 
		// 		pixmap.fill(); 
		// 		skin.add("white", new Texture(pixmap)); 
		 		
		 		// Store the default libgdx font under the name "default". 
		 		//skin.add("default", new BitmapFont()); 
		 		
		 		// Configure a TextButtonStyle and name it "default". Skin resources are stored by type, so this doesn't overwrite the font. 
		// 		TextButtonStyle textButtonStyle = new TextButtonStyle(); 
		// 		textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY); 
		// 		textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY); 
		// 		textButtonStyle.checked = skin.newDrawable("white", Color.BLUE); 
		// 		textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY); 
		// 		textButtonStyle.font = skin.getFont("default"); 
		// 		skin.add("default", textButtonStyle); 
		// 		
		// 		table1.add(new Image(skin.newDrawable("white", Color.RED))).size(64);
		// 		
		// 		final TextButton button = new TextButton("Click me!", skin); 
		// 		table1.add(button); 
		
	}
	
	public void initGame()
	{
		try
		{
	    
			initStuff();
			
			newGame(false, "htp");
	    
			iLoaded=1;
			gameGui.level2_objtypeid="";
			//	gameGui.listDlg.showDlg(true, null, ListType.NEWTOWN2, 0);
			
		}
		catch(Exception e)
		{
			CHelper.writeError(e.getMessage(), e.getStackTrace(), e);
		}
	}
	
	public void initTownVariables(String mode, String sfilename)
	{
		try
		{
			FileHandle file=null;
			String sini="";
			
			//if(mode.contains("design"))
			//	file = Gdx.files.internal("./data/mod/town.ini");
			//if(mode.contains("challenge"))
			//	file = Gdx.files.internal("./data/mod/town_challenge.ini");
			//file = Gdx.files.internal("./data/mod/town.ini");
			//if(!file.exists())
			
			mode=mode.toLowerCase();
			
			if(mode.equals("expert"))
				sini="config/town_expert.ini";
			else
				sini="config/town_htp.ini";
			
			/*
			if(mode.contains("challenge"))
				sini="config/town_challenge.ini";
			if(mode.contains("easy"))
				sini="config/town_easy.ini";
			if(mode.contains("normal"))
				sini="config/town_normal.ini";
			if(mode.contains("veryeasy"))
				sini="config/town_veryeasy.ini";
			if(mode.contains("design"))
				sini="config/town_design.ini";
			*/
			
			if(!sfilename.isEmpty())
				sini=sfilename;
			
			gameMode=mode;
			
			file = Gdx.files.internal(sini);
			//file = CHelper.getFileHandle("HTP/" + sini);
			
			BufferedReader reader = new BufferedReader(file.reader());
			String line = reader.readLine();
			
			while( line != null )
			{
				if(line.contains("//") || line.trim().isEmpty() || !line.contains("="))
				{
					line = reader.readLine();
					continue;
				}
				
				String arr[] = line.split("=");
				String label = arr[0];				
				String value = arr[1];
				
				if(label.toLowerCase().equals("startingmoney"))
				{
					startingMoney=Integer.parseInt(value);
					if(bNoRealEstate)
					{
						startingMoney=500000;
					}
				}

				if(label.toLowerCase().equals("zombiestartingday"))
				{
					zombieStartingDay=Integer.parseInt(value);
					zombieEntranceStartingDay=zombieStartingDay-1;
					/*
					if(bNoRealEstate)
					{
						zombieStartingDay=2;
						zombieEntranceStartingDay=zombieStartingDay;
					}
					*/
				}
				
				if(label.toLowerCase().equals("sickstartingday"))
					sickStartingDay=Integer.parseInt(value);
				if(label.toLowerCase().equals("sickstartingday_severedisease"))
					sickStartingDay_severedisease=Integer.parseInt(value);
				if(label.toLowerCase().equals("sickstartingday_contagious"))
					sickStartingDay_contagious=Integer.parseInt(value);
				if(label.toLowerCase().equals("spawnfamiliesstartingday"))
					spawnFamiliesStartingDay=Integer.parseInt(value);
				if(label.toLowerCase().equals("demandstartingday_fitnessstudio"))
					demandStartingDay_fitnessstudio=Integer.parseInt(value);
				if(label.toLowerCase().equals("demandstartingday_dinner"))
					demandStartingDay_dinner=Integer.parseInt(value);
				if(label.toLowerCase().equals("demandstartingday_tv"))
					demandStartingDay_tv=Integer.parseInt(value);
				if(label.toLowerCase().equals("demandstartingday_tv2"))
					demandStartingDay_tv2=Integer.parseInt(value);
				if(label.toLowerCase().equals("demandstartingday_sportscar"))
					demandStartingDay_sportscar=Integer.parseInt(value);
				if(label.toLowerCase().equals("demandstartingday_book"))
					demandStartingDay_book=Integer.parseInt(value);
				if(label.toLowerCase().equals("demandstartingday_sandpit"))
					demandStartingDay_sandpit=Integer.parseInt(value);
				if(label.toLowerCase().equals("demandstartingday_slide"))
					demandStartingDay_slide=Integer.parseInt(value);
				if(label.toLowerCase().equals("demandstartingday_seesaw"))
					demandStartingDay_seesaw=Integer.parseInt(value);
				if(label.toLowerCase().equals("demandstartingday_pub"))
					demandStartingDay_pub=Integer.parseInt(value);
				if(label.toLowerCase().equals("demandstartingday_breakroom"))
					demandStartingDay_breakroom=Integer.parseInt(value);
				if(label.toLowerCase().equals("demandstartingday_church"))
					demandStartingDay_church=Integer.parseInt(value);
				if(label.toLowerCase().equals("company_officeworkoutput_max"))
					company_officeworkoutput_max=Integer.parseInt(value);
				if(label.toLowerCase().equals("initial_supermarket_shelf_foodstock"))
					initial_supermarket_shelf_foodstock=Integer.parseInt(value);
				if(label.toLowerCase().equals("initial_supermarket_warehouse_foodstock"))
					initial_supermarket_warehouse_foodstock=Integer.parseInt(value);
				if(label.toLowerCase().equals("initial_fridge_foodstock"))
					initial_fridge_foodstock=Integer.parseInt(value);
				if(label.toLowerCase().equals("initial_company_officeworkoutput"))
					initial_company_officeworkoutput=Integer.parseInt(value);
				if(label.toLowerCase().equals("initial_paintobject_price"))
					initial_paintobject_price=Integer.parseInt(value);
				if(label.toLowerCase().equals("grantsdelta"))
					grantsDelta=Float.parseFloat(value);
				if(label.toLowerCase().equals("moneydelta"))
					moneyDelta=Float.parseFloat(value);
				if(label.toLowerCase().equals("penaltydelta"))
					penaltyDelta=Float.parseFloat(value);
				if(label.toLowerCase().equals("eatdelta"))
					eatDelta=Float.parseFloat(value);
				if(label.toLowerCase().equals("cleandelta"))
					cleanDelta=Float.parseFloat(value);
				if(label.toLowerCase().equals("toiletdelta"))
					toiletDelta=Float.parseFloat(value);
				if(label.toLowerCase().equals("clothingdelta"))
					clothingDelta=Float.parseFloat(value);
				if(label.toLowerCase().equals("happinessdeltaplus"))
					happinessDeltaPlus=Float.parseFloat(value);
				if(label.toLowerCase().equals("healthdeltaplus"))
					healthDeltaPlus=Float.parseFloat(value);
				if(label.toLowerCase().equals("fitnessdeltaplus"))
					fitnessDeltaPlus=Float.parseFloat(value);
				if(label.toLowerCase().equals("intelligencedeltaplus"))
					intelligenceDeltaPlus=Float.parseFloat(value);
				if(label.toLowerCase().equals("happinessdeltaminus"))
					happinessDeltaMinus=Float.parseFloat(value);
				if(label.toLowerCase().equals("healthdeltaminus"))
					healthDeltaMinus=Float.parseFloat(value);
				if(label.toLowerCase().equals("fitnessdeltaminus"))
					fitnessDeltaMinus=Float.parseFloat(value);
				if(label.toLowerCase().equals("intelligencedeltaminus"))
					intelligenceDeltaMinus=Float.parseFloat(value);
				if(label.toLowerCase().equals("educationdeltaplus"))
					educationDeltaPlus=Float.parseFloat(value);
				if(label.toLowerCase().equals("increaseskilltaskdelta"))
					increaseskillTaskDelta=Float.parseFloat(value);
				if(label.toLowerCase().equals("increaseskilljobdelta"))
					increaseskillJobDelta=Float.parseFloat(value);
				if(label.toLowerCase().equals("setroomprice"))
					setRoomPrice=Integer.parseInt(value);
				if(label.toLowerCase().equals("architectcosts_clone"))
					architectcosts_clone=Integer.parseInt(value);
				if(label.toLowerCase().equals("architectcosts_resize"))
					architectcosts_resize=Integer.parseInt(value);
				if(label.toLowerCase().equals("architectcosts_move"))
					architectcosts_move=Integer.parseInt(value);
				if(label.toLowerCase().equals("architectcosts_planning"))
					architectcosts_planning=Integer.parseInt(value);
				if(label.toLowerCase().equals("startingaddresssize"))
					startingAddressSize=Integer.parseInt(value);
				if(label.toLowerCase().equals("startingzoom"))
					startingZoom=Float.parseFloat(value);
				if(label.toLowerCase().equals("resdelta"))
					resDelta=Float.parseFloat(value);
				if(label.toLowerCase().equals("desdelta"))
					desDelta=Float.parseFloat(value);
				
				line = reader.readLine();
			}
			reader.close();
		}
		catch (Exception e) {
			CHelper.writeError(e.getMessage(), e.getStackTrace(), e);
		}		
	}
	
	public void newGame(Boolean bReset, String mode)
	{
		try
		{
			initTownVariables(mode, "");
			
			if(bReset)
			{
				CWorld.pathmap=null;
				CWorld.pathmap_road=null;
				CWorld.pathmap_footpath=null;
				CWorld.pathmap_defensewall=null;
				if(CWorld.astar!=null)
					CWorld.astar.dispose();
				CWorld.astar = null;
				
				if(CWorld.astar_road!=null)
					CWorld.astar_road.dispose();
				CWorld.astar_road = null;
				
				if(CWorld.astar_footpath!=null)
					CWorld.astar_footpath.dispose();
				CWorld.astar_footpath = null;
				
				gameGui.dispose();
				gameGui=null;
				
				gameWorld.dispose();
				gameWorld=null;
				
				/*
				Runtime r=Runtime.getRuntime();
				long freemem = r.freeMemory();
				Gdx.app.setLogLevel(5);
				Gdx.app.debug("", "free: " + freemem);
				r.gc();
				freemem = r.freeMemory();
				Gdx.app.debug("", "free2: " + freemem);
				*/
				
				//gameGui=null;
				System.gc();
				initStuff();
			}
			
			gameGui.iShowHint=0;
			gameResourceConfig.resetObjects();
			
			gameCam.zoom = startingZoom; 
		    gameCam.position.set(mapsize/2, mapsize/2, 0);
		    gameCam.update();
		    //gameWorld.initAStar(); //init/reset
		    
			if(bReset)
			{
				gameWorld.init(gameCam, gameResourceConfig);
			}
			
			gameWorld.infoEventMoving=0;
			
			gameWorld.worldTime.init(0, startingMonth, 6, 30, 0);
			gameWorld.townMoney=startingMoney;
			
			//int size=900;
			int size=startingAddressSize;
			CAddress address = null; //new CAddress(mapsize/2-size, mapsize/2-size, mapsize/2+size, mapsize/2+size, "", this, "residential");
			//address.addressName = address.addressId + " Main Street";
			//gameWorld.worldAddressList.add(address);
			if(bNoRealEstate)
			{
				address = new CAddress(0, 0, mapsize, mapsize, "", this, "residential_public");
				address.addressName = "Community";
				gameWorld.worldAddressList.add(address);
			}
			else
			{
				address = new CAddress(mapsize/2-size, mapsize/2-size, mapsize/2+size, mapsize/2+size, "", this, "residential");
			}
			
			CWorldObject resident1 = null;
			CWorldObject resident2 = null;
			CWorldObject resident3 = null;
			CWorldObject resident4 = null;
			
			//startingResidentCount -> wenn hier angepasst wird  dann auch in createcustombird anpassen
			
			if(bNoRealEstate)
			{
				resident1 = gameWorld.createRandomCitizen("m", CHuman.generateCitizenForename("m") + " " + CHuman.generateCitizenLastname(), 5+gameWorld.rand.nextInt(70), (int)mapsize/2+rand.nextInt(500)-rand.nextInt(500), (int)mapsize/2+rand.nextInt(500)-rand.nextInt(500), 0);
				resident2 = gameWorld.createRandomCitizen("w", CHuman.generateCitizenForename("w") + " " + CHuman.generateCitizenLastname(), 5+gameWorld.rand.nextInt(70), (int)mapsize/2+rand.nextInt(500)-rand.nextInt(500), (int)mapsize/2+rand.nextInt(500)-rand.nextInt(500), 0);
				resident3 = gameWorld.createRandomCitizen("m", CHuman.generateCitizenForename("m") + " " + CHuman.generateCitizenLastname(), 5+gameWorld.rand.nextInt(70), (int)mapsize/2+rand.nextInt(500)-rand.nextInt(500), (int)mapsize/2+rand.nextInt(500)-rand.nextInt(500), 0);
				resident4 = gameWorld.createRandomCitizen("w", CHuman.generateCitizenForename("w") + " " + CHuman.generateCitizenLastname(), 5+gameWorld.rand.nextInt(70), (int)mapsize/2+rand.nextInt(500)-rand.nextInt(500), (int)mapsize/2+rand.nextInt(500)-rand.nextInt(500), 0);
			}
			else
			{
				resident1 = gameWorld.createRandomCitizen("m", CHuman.generateCitizenForename("m") + " " + CHuman.generateCitizenLastname(), 5+gameWorld.rand.nextInt(70), (int)address.randomX(), (int)address.randomY(), 0);
				resident2 = gameWorld.createRandomCitizen("w", CHuman.generateCitizenForename("w") + " " + CHuman.generateCitizenLastname(), 5+gameWorld.rand.nextInt(70), (int)address.randomX(), (int)address.randomY(), 0);
				resident3 = gameWorld.createRandomCitizen("m", CHuman.generateCitizenForename("m") + " " + CHuman.generateCitizenLastname(), 5+gameWorld.rand.nextInt(70), (int)address.randomX(), (int)address.randomY(), 0);
				resident4 = gameWorld.createRandomCitizen("w", CHuman.generateCitizenForename("w") + " " + CHuman.generateCitizenLastname(), 5+gameWorld.rand.nextInt(70), (int)address.randomX(), (int)address.randomY(), 0);
			}
			
			resident1.thehuman.setEducationValue(0);
			resident1.thehuman.setAge(20+rand.nextInt(20));
			resident1.thehuman.jobSkillLevel.clear();
			resident1.thehuman.initSkillByEducation();
			
			resident2.thehuman.setEducationValue(1);
			resident2.thehuman.setAge(20+rand.nextInt(20));
			resident2.thehuman.jobSkillLevel.clear();
			resident2.thehuman.initSkillByEducation();
			
			resident3.thehuman.setEducationValue(2);
			resident3.thehuman.setAge(30+rand.nextInt(30));
			resident3.thehuman.setIntelligenceValue(90+rand.nextInt(30));
			resident3.thehuman.jobSkillLevel.clear();
			resident3.thehuman.initSkillByEducation();
			
			resident4.thehuman.setEducationValue(3);
			resident4.thehuman.setAge(30+rand.nextInt(30));
			resident4.thehuman.setIntelligenceValue(100+rand.nextInt(40));
			resident4.thehuman.jobSkillLevel.clear();
			resident4.thehuman.initSkillByEducation();
			
			for(int i=0;i<20;i++)
				gameWorld.createRandomBird(40);
			
			gameWorld.bRenderFrameBuffer=true;
						
			//Lade Resources die zum Start nicht benötgit werden asynchron
	    	this.autoModeTimer=new java.util.Timer();
	    	this.autoModeTimer.schedule(new TimerTask() 
	    	{
	            @Override public void run() 
	            {
	                Gdx.app.postRunnable(new Runnable() 
	                {
	                	@Override
	                    public void run() {
	                    	initResources2();
	                	}
	                });
	            }
	        }, 0);
		}
		catch(Exception e)
		{
			CHelper.writeError(e.getMessage(), e.getStackTrace(), e);
		}
	}
	
	public void resize (int width, int height) 
	{
		if(gameCam!=null && gameConfigIni.windowed)
		{
			gameConfigIni.setDisplayMode(width, height, gameConfigIni.windowed);
			gameCam.viewportWidth=width;
			gameCam.viewportHeight=height;
			gameCam.update();
			gameGui.init(this);
			screenInfo = new CScreenInfo(this);
			
			//gameConfigIni.windowed=true;
			gameConfigIni.screenWidth=width;
			gameConfigIni.screenHeight=height;
			gameConfigIni.writeIni();
			
			Gdx.app.setLogLevel(5);
			
			if(gameWorld!=null)
				gameWorld.bRenderFrameBuffer=true;
		}
	}
	
	public void dispose() {
	    //stage1.dispose();
	    //skin.dispose();
	}
	
	public void render_s2d()
	{
	    //Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
	    //stage1.act(Gdx.graphics.getDeltaTime());
	    //stage1.draw();		
	}
	
	public void renderFPSLoadingScreen()
	{
		Gdx.graphics.getGL20().glClearColor( 0, 0, 0, 0 );
	    Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
	    
	    uiMatrix = gameCam.combined.cpy();
	    uiMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	    townSpriteBatch.setProjectionMatrix(uiMatrix);

		townSpriteBatch.begin();
		gameFont.bfArial2.setColor(Color.WHITE);
		gameFont.bfArial2.getData().setScale(0.5f); 
		gameFont.layout.setText(gameFont.bfArial2, "Loading...");
		gameFont.bfArial2.draw(townSpriteBatch, "Loading...", Gdx.graphics.getWidth()/2+gameFont.layout.width/2-gameFont.layout.width, Gdx.graphics.getHeight()/1.6f);
		townSpriteBatch.end();
	}
	
	public void renderLoadingSavegameScreen()
	{
		Gdx.graphics.getGL20().glClearColor( 0, 0, 0, 0 );
	    Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
	    uiMatrix = gameCam.combined.cpy();
	    uiMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	    townSpriteBatch.setProjectionMatrix(uiMatrix);

		townSpriteBatch.begin();
		gameFont.bfArial2.setColor(Color.WHITE);
		gameFont.bfArial2.getData().setScale(0.5f); 
		gameFont.layout.setText(gameFont.bfArial2, "Loading...");
		gameFont.bfArial2.draw(townSpriteBatch, "Loading...", Gdx.graphics.getWidth()/2+gameFont.layout.width/2-gameFont.layout.width, Gdx.graphics.getHeight()/1.6f);
		townSpriteBatch.end();
		
	    if(!bStartLoadingSaveGame)// && iSaveGameLoaded==10000)
	    {
	    	bStartLoadingSaveGame=true;
	    	this.autoModeTimer=new java.util.Timer();
	    	this.autoModeTimer.schedule(new TimerTask() 
	    	{
	            @Override public void run() 
	            {
	                Gdx.app.postRunnable(new Runnable() 
	                {
	                	@Override
	                    public void run() {
	                    	gameWorld.loadFromFile(sLoadSaveGame);
	                	}
	                });
	            }
	        }, 0);
	    }
	    
	    return;
	}
	
	public void renderLoadingScreen()
	{
		Gdx.graphics.getGL20().glClearColor( 0, 0, 0, 0 );
	    Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
		townSpriteBatch.begin();
		//townBitmapFont.setColor(Color.WHITE);
		//townBitmapFont.draw(townSpriteBatch, "Loading...", Gdx.graphics.getWidth()/2-50, Gdx.graphics.getHeight()/1.5f);
		gameFont.bfArial2.setColor(Color.WHITE);
		gameFont.bfArial2.getData().setScale(0.5f); 
		gameFont.layout.setText(gameFont.bfArial2, "Loading...");
		gameFont.bfArial2.draw(townSpriteBatch, "Loading...", Gdx.graphics.getWidth()/2+gameFont.layout.width/2-gameFont.layout.width, Gdx.graphics.getHeight()/1.6f);
		townSpriteBatch.end();
		
	    if(!bStartInit)
	    {
	    	bStartInit=true;
	    	this.autoModeTimer=new java.util.Timer();
	    	this.autoModeTimer.schedule(new TimerTask() 
	    	{
	            @Override public void run() 
	            {
	                Gdx.app.postRunnable(new Runnable() 
	                {
	                	@Override
	                    public void run() {
	                    	initResources();
	                	}
	                });
	            }
	        }, 0);
	    }
	    
	    if(bLoadedResources)
	    {
	    	initGame();
	    }

	    return;
	}
	
	private void checkResidents()
	{
		if(gameWorld==null || gameWorld.worldHumans==null)
			return;
		int count = gameWorld.worldHumans.size();
		if(count>=10)
			setAchievement("10residents");
		if(count>=25)
			setAchievement("25residents");
		if(count>=50)
			setAchievement("50residents");
		if(count>=75)
			setAchievement("75residents");
		if(count>=100)
			setAchievement("100residents");
		if(count>=150)
			setAchievement("150residents");
		if(count>=200)
			setAchievement("200residents");
		if(count>=250)
			setAchievement("250residents");
		if(count>=300)
			setAchievement("300residents");
		if(count>=350)
			setAchievement("350residents");
		if(count>=400)
			setAchievement("400residents");
		if(count>=450)
			setAchievement("450residents");
		if(count>=500)
			setAchievement("500residents");
		if(count>=600)
			setAchievement("600residents");
		if(count>=700)
			setAchievement("700residents");
		if(count>=800)
			setAchievement("800residents");
		if(count>=900)
			setAchievement("900residents");
		if(count>=1000)
			setAchievement("1000residents");
		
		int days = gameWorld.worldTime.getCurrentDay();
		if(days>1)
		{
			setAchievement("day"+(days-1));
		}
	}
	
	public void render() 
	{
		//System.gc();
		//Gdx.app.debug("", "bUsePostProcessor: "+bUsePostProcessor);
		
		//if(Gdx.input.isKeyJustPressed(Keys.X))
		//	gameWorld.asyncExecutor = new AsyncExecutor(4);
		//if(Gdx.input.isKeyJustPressed(Keys.C))
		//	gameWorld.asyncExecutor = new AsyncExecutor(1);
		
		//garbageCollectorTimer+=Gdx.graphics.getDeltaTime();
		//if(garbageCollectorTimer>(60*20)) //Nach 20 Minuten Garbage Collector laufen lassen
		//{
		//	System.gc();
		//	garbageCollectorTimer=0;
		//}
		
		checkResidents();
		
		if(bDevMode && Gdx.input.isKeyJustPressed(Keys.V))
			gameWorld.calculateNiceFund();
				
		if(bDevMode && Gdx.input.isKeyJustPressed(Keys.Q))
			gameWorld.worldHumans.remove(0);
		
		//if(Gdx.input.isKeyJustPressed(Keys.G))
		//{
			//Gdx.app.debug("", "System.gc();");
		//	System.gc();
		//}
		
		if(Gdx.input.isKeyJustPressed(Keys.F))
			bShowFPS=!bShowFPS;			
		
		if(bDevMode && Gdx.input.isKeyJustPressed(Keys.R))
			gameWorld.createRandomCitizen("m", CHuman.generateCitizenForename("m") + " " + CHuman.generateCitizenLastname(), 5+gameWorld.rand.nextInt(70), mapsize/2, mapsize/2, 0);
		
		if(bDevMode && Gdx.input.isKeyJustPressed(Keys.Z))
			gameWorld.createRandomCitizen("m", CHuman.generateCitizenForename("m") + " " + CHuman.generateCitizenLastname(), 5+gameWorld.rand.nextInt(70), 1000, mapsize/2, 1);
		
		try
		{
		
		//Loading Screen
		if(iLoaded==0)
		{
			renderLoadingScreen();
			return;
		}
		
		//Erst rendern wenn FPS "warm" gelaufen sind, sonst zucken die Residents kurz hin und her
		if(Gdx.graphics.getFramesPerSecond()>30)
			iLoaded=100;
		
		if(iLoaded<100)
		{
			iLoaded++;
			return;
		}
		
 		if(bDebugShowRain)
 		{
 			gameWorld.worldWeather.iRenderRainFrom=0;
 			gameWorld.worldWeather.iRenderRainTo=23;
 			gameWorld.worldWeather.initializedMonth=gameWorld.worldTime.day;
 		}
 		
 		if(bDebugShowSnow)
 		{
 			gameWorld.worldWeather.iRenderSnowFrom=0;
 			gameWorld.worldWeather.iRenderSnowTo=23;
 			gameWorld.worldWeather.initializedMonth=gameWorld.worldTime.day;
 		}
 		
 		gameAudio.playAmbient();
 		
 		//Gdx.app.debug("", "bUsePostProcessor " + bUsePostProcessor);
 		
		if(bUsePostProcessor)
			postProcessor.capture();
		
		if(bUseFramebuffer && gameCam.zoom<frameBuffer.frameBufferZoom)
		{
			frameBuffer.render_start();
		}
		else
		{
			Gdx.graphics.getGL20().glClearColor( 0, 0, 0, 0 );
		    Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
		}
		
		gameCam.update();
		gameInput.render();
		gameWorld.render();
		//player.render();
		
		if(bUseFramebuffer && gameCam.zoom<frameBuffer.frameBufferZoom)
			frameBuffer.render_end();
		
		if(bUsePostProcessor)
			postProcessor.render();
		
		gameWorld.rayHandler.setCombinedMatrix(gameCam);
		
		if(gameWorld.worldTime.hours<7 || gameWorld.worldTime.hours>19) //tagsüber kein lighting
			gameWorld.rayHandler.updateAndRender();
		
		//gameWorld.renderMarkedObject(Gdx.input.getX(), CHelper.screenToLibGDX(Gdx.input.getY()));
				
		if(!bNoRealEstate)
		{
			gameWorld.renderAddress(gameWorld.worldAddressList, false,false);
			gameWorld.renderAddress(gameWorld.cloneAddressList, true, false);
		}
		gameWorld.renderMarkedObject(Gdx.input.getX(), CHelper.screenToLibGDX(Gdx.input.getY()));
		
		gameWorld.renderDeleteObject();
		gameWorld.renderCloneAddressPriceInfo();
		gameWorld.renderMouseoverObject();


		gameWorld.renderGUIElements();
		gameGui.render();
		
		screenInfo.renderFPS();
		
		//Load saved game
		if(bLoadingSaveGame)
		{
			renderLoadingSavegameScreen();
			iSaveGameLoaded=100;
			return;
		}
		else
		{
			if(bStartLoadingSaveGame && iSaveGameLoaded>1) //FPS warmlaufen lassen
			{
				renderLoadingSavegameScreen();
				iSaveGameLoaded--;
				return;
			}
		}
		
		if(iSaveGameLoaded<2)
			bStartLoadingSaveGame=false;
		
		}
		catch(Exception e)
		{
			CHelper.writeError(e.getMessage(), e.getStackTrace(), e);
		}
	}
		
	public void initPostprocessor(int value)
	{
	    //if(bUsePostProcessor)
	    {
	        ShaderLoader.BasePath = "shaders/";
        	postProcessor = new PostProcessor( false, false, true );
        	
        	//Zoomer zz1= new Zoomer(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        	//postProcessor.addEffect(zz1);
        	
        	//RadialBlur blur = new RadialBlur(Quality.High);
        	//postProcessor. ); );(blur);
        	
			//Bloom bloom = new Bloom( (int)(Gdx.graphics.getWidth() * 0.1f), (int)(Gdx.graphics.getHeight() * 0.1f) );
			//postProcessor.addEffect(bloom);
			
			Pixelator pix = new Pixelator(value,value);
			postProcessor.addEffect(pix);
			
			//Vignette vig = new Vignette(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
			//postProcessor.addEffect(vig);
			
			//int effects = Effect.TweakContrast.v | Effect.PhosphorVibrance.v | Effect.Scanlines.v | Effect.Tint.v;
			//	        int effects = Effect.TweakContrast.v | Effect.PhosphorVibrance.v | Effect.Tint.v;
			//			CrtMonitor crt = new CrtMonitor( Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, false, RgbMode.ChromaticAberrations, effects );
			//			postProcessor.addEffect(crt);
			//			Combine combine = crt.getCombinePass();
			//			combine.setSource1Intensity( 0f );
			//			combine.setSource2Intensity( 1f );
			//			combine.setSource1Saturation( 0f );
			//			combine.setSource2Saturation( 1f ); 
	    }
	}
}
