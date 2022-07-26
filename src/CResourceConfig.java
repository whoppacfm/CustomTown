package com.mygdx.game;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.mygdx.game.CAudio.CAudioAmbient;
import com.mygdx.game.CAudio.CAudioEffect;

public class CResourceConfig {
	
	public List<CObjecttype> listObjecttype;
	
	//<ID, Object>
	public ArrayList<CObject> listObject;
	//public ArrayList<CObject> listResearchFunctionObject;
	public ArrayList<CObject> listObjectres;
	public ArrayList<CObject> listLinkObject;
	public ArrayList<CObject> listWorkplacesAndTasksWithSkill;
	
	//Filename,Effect
	public Hashtable<String,CAudioEffect> listAudioEffect_ByFilename; 
	public Hashtable<String,CAudioEffect> listAudioEffect_ByType;
	public Hashtable<String,CAudioAmbient> listAudioAmbient_Rain;
	public Hashtable<String,CAudioAmbient> listAudioAmbient_Rain2;
	public Hashtable<String,CAudioAmbient> listAudioAmbient_Day;
	public Hashtable<String,CAudioAmbient> listAudioAmbient_NightWarm;
	public Hashtable<String,CAudioAmbient> listAudioAmbient_NightCold;
	public ArrayList<String> listAudioMusic;
	
	//public List<CObject> listObject; //Objekte die aus dem Editor platziert werden können
	//public List<CObject> listObjectres; //sonstige graphik-ressourcen
	//public List<CObject> listLinkObject;
	//public List<CObject> listWorkplacesAndTasksWithSkill;

	public ArrayList<CObject> listObjectHead_Women;
	public ArrayList<CObject> listObjectHead_Men;
	public List<FileHandle> listObjectModfiles;
	
	//	public List<CObject> listObjects_heads_m; //Heads stehen nicht in der objects sondern werden direkt aus dem verzeichnis geladen
	//	public List<CObject> listObjects_heads_w;
	//	public List<CObject> listObjects_heads_h;
	
	ShaderProgram snowShader;
	ShaderProgram satShader;
	
	public Hashtable<String,Texture> textures;
	public Hashtable<String,Animation> animations;
		
	//public Boolean bUseMipMapping=false;
	private CTown town;
	
	public void init(CTown t)
	{
		try
		{
			town=t;
			//town.initTownVariables();
			
			textures = new Hashtable<String,Texture>();
			animations = new Hashtable<String,Animation>();
			listWorkplacesAndTasksWithSkill = new ArrayList<CObject>();
			
			listAudioAmbient_Rain=new Hashtable<String,CAudioAmbient>(); //Filename,Ambient
			listAudioAmbient_Rain2=new Hashtable<String,CAudioAmbient>(); //Filename,Ambient
			listAudioAmbient_Day=new Hashtable<String,CAudioAmbient>(); //Filename,Ambient
			listAudioAmbient_NightWarm=new Hashtable<String,CAudioAmbient>(); //Filename,Ambient
			listAudioAmbient_NightCold=new Hashtable<String,CAudioAmbient>(); //Filename,Ambient
			listAudioEffect_ByFilename=new Hashtable<String,CAudioEffect>(); //Filename,Effect
			listAudioEffect_ByType=new Hashtable<String,CAudioEffect>(); //Filename,Effect
			listAudioMusic = new ArrayList<String>();
			
			//Mod-Liste erstellen
			initObjectfileList();
									
			//Editorstruktur laden
			listObjecttype = new ArrayList<CObjecttype>();
			initObjectTypes(Gdx.files.internal("config/object_type.csv"));
			for(FileHandle f1 : listObjectModfiles)
				if(f1.path().contains("object_type_"))
					initObjectTypes(f1);
			
			//Ressourcen ohne Editorverbindung
			listObjectres = new ArrayList<CObject>();
			initObjects(Gdx.files.internal("config/object_res.csv"), listObjectres, true);
			for(FileHandle f1 : listObjectModfiles)
				if(f1.path().contains("object_res_"))
					initObjects(f1, listObjectres, true);
			
			initResTextures();
			initResAnimations();
			
			//Link Objects
			listLinkObject = new ArrayList<CObject>();
			initObjects(Gdx.files.internal("config/object_link.csv"), listLinkObject, false);
			for(FileHandle f1 : listObjectModfiles)
				if(f1.path().contains("object_link_"))
					initObjects(f1, listLinkObject, false);
			
			//Ressourcen die aus Editor platziert werden
			listObject = new ArrayList<CObject>();
			initObjects(Gdx.files.internal("config/object_object.csv"), listObject, false);
			for(FileHandle f1 : listObjectModfiles)
			{
				if(f1.path().contains("object_object_"))
					initObjects(f1, listObject, false);
			}
			
			//Kopftexturen laden
			listObjectHead_Women = new ArrayList<CObject>();
			listObjectHead_Men = new ArrayList<CObject>();
			initHeads(Gdx.files.internal("config/object_head.csv"));
			
			for(FileHandle f1 : listObjectModfiles)
			{
				if(f1.name().contains("object_head_"))
				{
					initHeads(f1);
				}
			}
			
			//Research Functions
			//listResearchFunctionObject = new ArrayList<CObject>();
			//initResearchFunctionObjects();
			
			
			initAudio(0);
		}
		catch(Exception e)
		{
			CHelper.writeError(e.getMessage(), e.getStackTrace(), e);
		}
	}
	
	public void resetObjects()
	{
		if(listObject!=null)
		{
			for(CObject obj1 : listObject)
			{
				if(!town.bResearched)
					obj1.iResearchCurrentWorkoutput=0;
				
				obj1.iOneTimeBonus=0;
			}
		}
	}
	
	public void initAudioObjects(FileHandle file1, int loadtype) throws IOException
	{
		//loadtype 0: lade nur die notwendigen files zum spielstart, 1: lade alles was fehlt
		
		BufferedReader reader = new BufferedReader(file1.reader());
		
		String line = reader.readLine();
		while( line != null )
		{
//			try
//			{
				if(line.contains("//") || line.trim().isEmpty())
				{
					line = reader.readLine();
					continue;
				}
				
				String[] splitstring = line.split(";");
				
				if(splitstring.length<2)
				{
					line = reader.readLine();
					continue;
				}
				
				//[FILENAME];[AUDIOTYPE]
				String fileandpath = splitstring[0];
				File fi = new File(fileandpath);
				String filename = fi.getName();
				
				String audiotype = splitstring[1];
				audiotype = audiotype.toLowerCase();
				
				if(audiotype.contains("ambient") || audiotype.contains("rain"))
				{
					int totalseconds=0;
					
					if(audiotype.contains("ambient"))
							totalseconds = Integer.parseInt(splitstring[2]);
					
					Hashtable<String, CAudioAmbient> listAudioAmbient_Temp=null;
					if(audiotype.toLowerCase().contains("rain"))
						listAudioAmbient_Temp=listAudioAmbient_Rain;
					if(audiotype.contains("day"))
						listAudioAmbient_Temp=listAudioAmbient_Day;
					if(audiotype.contains("night_warm"))
						listAudioAmbient_Temp=listAudioAmbient_NightWarm;
					if(audiotype.contains("night_cold"))
						listAudioAmbient_Temp=listAudioAmbient_NightCold;
					
					//Prüfe ob filename schon existiert, wenn ja, nicht ersetzen
					if(listAudioAmbient_Temp.containsKey(filename) 
							)
					{
						line = reader.readLine();
						continue;
					}
					
					//Performance für initiales Laden: von jedem Ambient-Typ nur ein File laden
					//AMBIENT_RAIN, AMBIENT_DAY, AMBIENT_NIGHT_WARM, AMBIENT_NIGHT_COLD
					Boolean bPresent=false;
					for(CAudioAmbient amb : listAudioAmbient_Temp.values())
					{
						if(amb.sAudioType.toLowerCase().equals(audiotype))
						{
							bPresent=true;
							break;
						}
					}
					
					if(loadtype==1 || !bPresent)
					{
						listAudioAmbient_Temp.put(filename, new CAudioAmbient(audiotype, fileandpath, totalseconds));
						if(audiotype.toLowerCase().contains("rain"))
							listAudioAmbient_Rain2.put(filename, new CAudioAmbient(audiotype, fileandpath, totalseconds));
					}
				}
				
				if(audiotype.contains("music"))
				{
					listAudioMusic.add(fileandpath);
				}
				
				if(audiotype.contains("effect"))
				{
					CAudioEffect effect=new CAudioEffect(audiotype, fileandpath);
					if(listAudioEffect_ByFilename.containsKey(filename))
					{
						line = reader.readLine();
						continue;
					}
					listAudioEffect_ByFilename.put(filename, effect);
					listAudioEffect_ByType.put(audiotype, effect);
				}
				
				line = reader.readLine();
//			}
//			catch(Exception e)
//			{
//				CHelper.writeError(e.getMessage(), e.getStackTrace(), e);
//				line = reader.readLine();
//				continue;
//			}
		}
	}
	
	public void initAudio(int type)
	{
		try
		{
			//type=0: notwendigen Sound für Spielstart laden
			//type=1: alles laden 
			
			//Audio
			//Hashtable<String,CAudioEffect>
			for(FileHandle f1 : listObjectModfiles)
			{
				if(f1.name().contains("object_audio_"))
				{
					initAudioObjects(f1, type);
				}
			}
			
			initAudioObjects(Gdx.files.internal("config/object_audio.csv"), type);
		}
		catch(Exception e)
		{
			CHelper.writeError(e.getMessage(), e.getStackTrace(), e);
		}
	}
	
	public void initObjectfileList()
	{
		listObjectModfiles = new ArrayList<FileHandle>();
		
		//FileHandle dirHandle = Gdx.files.internal("./data/mod");
		FileHandle dirHandle = CHelper.getFileHandle("appdata/local/HTP/data/mod");
		
		for (FileHandle entry : dirHandle.list())
		{
			if(entry.name().contains("object"))
				listObjectModfiles.add(entry);
		}
	}
	
	public void cleanGfxDir(FileHandle dir1)
	{
		for (FileHandle fh1: dir1.list())
		{
			if(fh1.isDirectory())
			{
				String sname=fh1.name().toLowerCase();
				if(sname.contains("cursor") || sname.contains("font") || sname.contains("shaders") || sname.contains("heads"))
					continue;
				cleanGfxDir(fh1);
			}
			else
			{
				Optional<CObjecttype> objtype = listObjecttype.stream().filter(item->item.iconFileName.toLowerCase().contains(fh1.name().toLowerCase())).findFirst();
				if(objtype.isPresent())
					continue;

				Optional<CObject> obj1 = listObject.stream().filter(item->item.textureFilename.toLowerCase().contains(fh1.name().toLowerCase()) || item.textureFilename.contains(fh1.name().toLowerCase())).findFirst();
				if(obj1.isPresent())
					continue;

				Optional<CObject> objres = listObjectres.stream().filter(item->item.textureFilename.toLowerCase().contains(fh1.name().toLowerCase()) || item.textureFilename.contains(fh1.name().toLowerCase())).findFirst();
				if(objres.isPresent())
					continue;

				Optional<CObject> objlink = listLinkObject.stream().filter(item->item.textureFilename.toLowerCase().contains(fh1.name().toLowerCase()) || item.textureFilename.contains(fh1.name().toLowerCase())).findFirst();
				if(objlink.isPresent())
					continue;
				
				Gdx.app.debug("", "Delete File: " + fh1.name());
				
				fh1.delete();
			}
		}
	}
	
	public CObject getObjectById(String id)
	{
		Optional<CObject> obj = listObject.stream().filter(item->item.objectId.equals(id)).findFirst();
		if(obj.isPresent())
			return obj.get();
		
		return null;
	}
	
	public void initShader()
	{
		snowShader = new ShaderProgram(Gdx.files.internal("shaders/snow.vert"), Gdx.files.internal("shaders/snow.frag"));
		satShader = new ShaderProgram(Gdx.files.internal("shaders/sat.vert"), Gdx.files.internal("shaders/sat.frag"));

		if (!snowShader.isCompiled()) {
		    Gdx.app.error("snowShader", "compilation failed:\n" + snowShader.getLog());
		}
		
		if (!satShader.isCompiled()) {
		    Gdx.app.error("satShader", "compilation failed:\n" + satShader.getLog());
		}
	}
	
	public void initHeads(FileHandle file) {

		try
		{
			//FileHandle dirHandle = Gdx.files.internal("./bin/gfx/people/heads/man");
			//for (FileHandle entry: dirHandle.list()) {
			
			//for(FileHandle f1 : listObjectModfiles)
			//	if(f1.name().contains("object_object_"))
			//		initObjects(f1, listLinkObject, false);
			
			//FileHandle file = Gdx.files.internal("config/object_head.csv");
			//FileHandle file = Gdx.files.internal("./data/mod/object_head_1.csv");
			BufferedReader reader = new BufferedReader(file.reader());
			
			//int count=0;
			String line = reader.readLine();
			while( line != null )
			{
				if(line.contains("//") || line.trim().isEmpty())
				{
					line = reader.readLine();
					continue;
				}
				
				String[] splitstring = line.split(";");
				
				if(splitstring.length !=3)
				{
					line = reader.readLine();
					continue;
				}
				
				String id = splitstring[0];
				String gender = splitstring[1];
				String filename = splitstring[2];
				
				//String headfile="gfx/people/heads/man/man_head"+count+".png";
				String headfile=filename;
				if(!Gdx.files.internal(headfile).exists())
				{
					line = reader.readLine();
					continue;
				}
								
				ArrayList<CObject> hlist = null;
				if(gender.equals("w"))
					hlist=listObjectHead_Women;
				if(gender.equals("m"))
					hlist=listObjectHead_Men;
				
				
				//Objekte mit gleicher ID ersetzen
				String objectid=splitstring[0]; 
				CObject dobj=null;
				for(CObject obj1 : hlist)
				{
					if(obj1.objectId.equals(objectid))
					{
						dobj=obj1;
						break;
					}
				}
				
				if(dobj!=null)
					hlist.remove(dobj);
				
				//Modobjects mit Modkürzel kennzeichnen, damit sich Mods nicht gegenseitig überschreiben
				//if(dobj==null && file.path().contains("/mod/"))
				//{
				//	String[] l1 = file.nameWithoutExtension().split("_");
				//	objectid=objectid+"_"+l1[l1.length-1];
				//}
								
				CObject obj = new CObject(town);
				
				if(gender.equals("m"))
				{
					obj.objectId  = id; //"001002001";
					obj.objectName = "Man";
					obj.objectTypeId = "001002";
					obj.editoraction = "head_man";
				}
				else if(gender.equals("w"))
				{
					obj.objectId  = id; //"001001001";
					obj.objectName = "Woman";
					obj.objectTypeId = "001001";
					obj.editoraction = "head_woman";
				}
				
				//obj.textureFilename = "gfx/people/heads/man/"+ entry.name();
				obj.textureFilename = headfile;
				obj.textureImage = new Texture(Gdx.files.internal(obj.textureFilename), true); //true: generate mipmaps -> keine pixel ränder !
				obj.textureImage.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
				obj.textureRegion = new TextureRegion[1];
				obj.textureRegion[0] = new TextureRegion(obj.textureImage);		
				obj.textureRegion[0].flip(false, true);
				obj.zorder=3;
				obj.coltype="0";
				obj.width=60;
				obj.height=70;
				
				if(town.fSizeFactor>0)
				{
					obj.width/=town.fSizeFactor;
					obj.height/=town.fSizeFactor;
				}
				
				hlist.add(obj);
				
				line = reader.readLine();
			}
		}
		catch (Exception e) {
			CHelper.writeError(e.getMessage(), e.getStackTrace(), e);
		}
	}
	
	public void initResTextures()
	{
		//Achtung: So lassen - keine Zeitersparnis wenn listen nicht extra durchlaufen werden + Exception wenn etwas fehlt
		CObject res_waterPuddle = listObjectres.stream().filter(p->p.editoraction.equals("waterpuddle")).findFirst().get();
		//------------------------------
		CObject guiinfo_electricity = listObjectres.stream().filter(p->p.editoraction.equals("guiinfo_electricity")).findFirst().get();
		CObject guiinfo_water = listObjectres.stream().filter(p->p.editoraction.equals("guiinfo_water")).findFirst().get();
		CObject guiinfo_population = listObjectres.stream().filter(p->p.editoraction.equals("guiinfo_population")).findFirst().get();
		CObject guiinfo_workplace = listObjectres.stream().filter(p->p.editoraction.equals("guiinfo_workplace")).findFirst().get();
		CObject guiinfo_average = listObjectres.stream().filter(p->p.editoraction.equals("guiinfo_average")).findFirst().get();
		CObject icon_health = listObjectres.stream().filter(item->item.editoraction.equals("info_attr_health")).findFirst().get();
		CObject icon_happiness = listObjectres.stream().filter(item->item.editoraction.equals("info_attr_happiness")).findFirst().get();
		CObject guiinfo_age = listObjectres.stream().filter(item->item.editoraction.equals("guiinfo_age")).findFirst().get();
		CObject energy_flash = listObjectres.stream().filter(item->item.editoraction.equals("energy_flash")).findFirst().get();
		//CObject energy_flash2 = listObjectres.stream().filter(item->item.editoraction.equals("energy_flash2")).findFirst().get();
		CObject energy_waterdrop = listObjectres.stream().filter(item->item.editoraction.equals("energy_waterdrop")).findFirst().get();
		CObject warning_workinput = listObjectres.stream().filter(item->item.editoraction.equals("warning_workinput")).findFirst().get();
//		CObject company_supermarket_shoppingcart_f1 = listObjectres.stream().filter(item->item.editoraction.equals("company_supermarket_shoppingcart_f1")).findFirst().get();
//		CObject company_supermarket_shoppingcart_f2 = listObjectres.stream().filter(item->item.editoraction.equals("company_supermarket_shoppingcart_f2")).findFirst().get();
//		CObject company_supermarket_shoppingcart_f3 = listObjectres.stream().filter(item->item.editoraction.equals("company_supermarket_shoppingcart_f3")).findFirst().get();
//		CObject company_supermarket_shoppingcart_f4 = listObjectres.stream().filter(item->item.editoraction.equals("company_supermarket_shoppingcart_f4")).findFirst().get();
//		CObject company_supermarket_shoppingcart_f5 = listObjectres.stream().filter(item->item.editoraction.equals("company_supermarket_shoppingcart_f5")).findFirst().get();
		
		CObject company_supermarket_food1 = listObjectres.stream().filter(item->item.editoraction.equals("company_supermarket_food1")).findFirst().get();
		CObject company_supermarket_food2 = listObjectres.stream().filter(item->item.editoraction.equals("company_supermarket_food2")).findFirst().get();
		CObject company_supermarket_food3 = listObjectres.stream().filter(item->item.editoraction.equals("company_supermarket_food3")).findFirst().get();
		CObject company_supermarket_food4 = listObjectres.stream().filter(item->item.editoraction.equals("company_supermarket_food4")).findFirst().get();
		CObject company_supermarket_food5 = listObjectres.stream().filter(item->item.editoraction.equals("company_supermarket_food5")).findFirst().get();
		CObject company_supermarket_food6 = listObjectres.stream().filter(item->item.editoraction.equals("company_supermarket_food6")).findFirst().get();
		CObject company_supermarket_food7 = listObjectres.stream().filter(item->item.editoraction.equals("company_supermarket_food7")).findFirst().get();
		CObject company_supermarket_food8 = listObjectres.stream().filter(item->item.editoraction.equals("company_supermarket_food8")).findFirst().get();

//		CObject company_supermarket_shelf_f2 = listObjectres.stream().filter(item->item.editoraction.equals("company_supermarket_shelf_f2")).findFirst().get();
//		CObject company_supermarket_shelf_f3 = listObjectres.stream().filter(item->item.editoraction.equals("company_supermarket_shelf_f3")).findFirst().get();
//		CObject company_supermarket_shelf_f4 = listObjectres.stream().filter(item->item.editoraction.equals("company_supermarket_shelf_f4")).findFirst().get();
//		CObject company_supermarket_shelf_f5 = listObjectres.stream().filter(item->item.editoraction.equals("company_supermarket_shelf_f5")).findFirst().get();
//		CObject company_supermarket_shelf_f6 = listObjectres.stream().filter(item->item.editoraction.equals("company_supermarket_shelf_f6")).findFirst().get();
//		CObject company_supermarket_shelf_f7 = listObjectres.stream().filter(item->item.editoraction.equals("company_supermarket_shelf_f7")).findFirst().get();
//		CObject company_supermarket_shelf_f8 = listObjectres.stream().filter(item->item.editoraction.equals("company_supermarket_shelf_f8")).findFirst().get();
		
		
		CObject control_button_movehouse = listObjectres.stream().filter(item->item.editoraction.equals("control_button_movehouse")).findFirst().get();
		
		
		CObject icon_dead = listObjectres.stream().filter(item->item.editoraction.equals("info_dead")).findFirst().get();
		CObject guiinfo_clear = listObjectres.stream().filter(item->item.editoraction.equals("guiinfo_clear")).findFirst().get();
		CObject warning_condition = listObjectres.stream().filter(item->item.editoraction.equals("warning_condition")).findFirst().get();
		CObject guiinfo_money = listObjectres.stream().filter(item->item.editoraction.equals("guiinfo_money")).findFirst().get();
		CObject guiinfo_workoutput = listObjectres.stream().filter(item->item.editoraction.equals("guiinfo_workoutput")).findFirst().get();
		CObject guiinfo_condition = listObjectres.stream().filter(item->item.editoraction.equals("guiinfo_condition")).findFirst().get();
		CObject weather_snow = listObjectres.stream().filter(item->item.editoraction.equals("weather_snow")).findFirst().get();
		
		CObject background_grass = listObjectres.stream().filter(item->item.editoraction.equals("background_grass")).findFirst().get();
		CObject background_grasssnow1 = listObjectres.stream().filter(item->item.editoraction.equals("background_grasssnow1")).findFirst().get();
		CObject background_grasssnow2 = listObjectres.stream().filter(item->item.editoraction.equals("background_grasssnow2")).findFirst().get();
		CObject background_grasssnow3 = listObjectres.stream().filter(item->item.editoraction.equals("background_grasssnow3")).findFirst().get();
		//CObject background_grasssnow4 = listObjectres.stream().filter(item->item.editoraction.equals("background_grasssnow4")).findFirst().get();
		CObject background_gras_t2 = listObjectres.stream().filter(item->item.editoraction.equals("background_gras_t2")).findFirst().get();
		
		CObject weather_rain = listObjectres.stream().filter(item->item.editoraction.equals("weather_rain")).findFirst().get();		
		CObject gui_menu = listObjectres.stream().filter(item->item.editoraction.equals("gui_menu")).findFirst().get();
		CObject address_public = listObjectres.stream().filter(item->item.editoraction.equals("address_public")).findFirst().get();
		CObject address_residential = listObjectres.stream().filter(item->item.editoraction.equals("address_residential")).findFirst().get();
		CObject gui_forbidden = listObjectres.stream().filter(item->item.editoraction.equals("gui_forbidden")).findFirst().get();
		CObject control_button_play2 = listObjectres.stream().filter(item->item.editoraction.equals("control_button_play2")).findFirst().get();
		CObject control_button_play3 = listObjectres.stream().filter(item->item.editoraction.equals("control_button_play3")).findFirst().get();
		CObject warning_worker = listObjectres.stream().filter(item->item.editoraction.equals("warning_worker")).findFirst().get();
		CObject anim_showwork = listObjectres.stream().filter(item->item.editoraction.equals("anim_showwork")).findFirst().get();
		CObject warning_worker2 = listObjectres.stream().filter(item->item.editoraction.equals("warning_worker2")).findFirst().get();
		CObject gui_arrowleft = listObjectres.stream().filter(item->item.editoraction.equals("gui_arrowleft")).findFirst().get();
		CObject gui_arrowright = listObjectres.stream().filter(item->item.editoraction.equals("gui_arrowright")).findFirst().get();
		CObject cursor_resize = listObjectres.stream().filter(item->item.editoraction.equals("cursor_resize")).findFirst().get();
		CObject gui_plus = listObjectres.stream().filter(item->item.editoraction.equals("gui_plus")).findFirst().get();
		CObject gui_minus = listObjectres.stream().filter(item->item.editoraction.equals("gui_minus")).findFirst().get();
		CObject gui_worktime = listObjectres.stream().filter(item->item.editoraction.equals("gui_worktime")).findFirst().get();
		CObject warning_owner = listObjectres.stream().filter(item->item.editoraction.equals("warning_owner")).findFirst().get();
		CObject poly_whiterect = listObjectres.stream().filter(item->item.editoraction.equals("poly_whiterect")).findFirst().get();
		CObject poly_whiterect2 = listObjectres.stream().filter(item->item.editoraction.equals("poly_whiterect2")).findFirst().get();
		CObject warning_cold = listObjectres.stream().filter(item->item.editoraction.equals("warning_cold")).findFirst().get();
		CObject warning_dark = listObjectres.stream().filter(item->item.editoraction.equals("warning_dark")).findFirst().get();
		CObject guiinfo_intelligence = listObjectres.stream().filter(item->item.editoraction.equals("guiinfo_intelligence")).findFirst().get();
		CObject guiinfo_fitness = listObjectres.stream().filter(item->item.editoraction.equals("guiinfo_fitness")).findFirst().get();
		CObject guiinfo_education = listObjectres.stream().filter(item->item.editoraction.equals("guiinfo_education")).findFirst().get();
		CObject guiinfo_warning = listObjectres.stream().filter(item->item.editoraction.equals("guiinfo_warning")).findFirst().get();
		CObject guiinfo_worker = listObjectres.stream().filter(item->item.editoraction.equals("guiinfo_worker")).findFirst().get();
		CObject guiinfo_workoutput2 = listObjectres.stream().filter(item->item.editoraction.equals("guiinfo_workoutput2")).findFirst().get();
		CObject guiinfo_energyoutput = listObjectres.stream().filter(item->item.editoraction.equals("guiinfo_energyoutput")).findFirst().get();
		CObject guiinfo_wateroutput = listObjectres.stream().filter(item->item.editoraction.equals("guiinfo_wateroutput")).findFirst().get();
		CObject guiinfo_energyconsumption = listObjectres.stream().filter(item->item.editoraction.equals("guiinfo_energyconsumption")).findFirst().get();
		CObject guiinfo_waterconsumption = listObjectres.stream().filter(item->item.editoraction.equals("guiinfo_waterconsumption")).findFirst().get();
		CObject guiinfo_food = listObjectres.stream().filter(item->item.editoraction.equals("guiinfo_food")).findFirst().get();
		CObject guiinfo_brightness = listObjectres.stream().filter(item->item.editoraction.equals("guiinfo_brightness")).findFirst().get();
		CObject guiinfo_heatingpower = listObjectres.stream().filter(item->item.editoraction.equals("guiinfo_heatingpower")).findFirst().get();
		CObject guiinfo_cook = listObjectres.stream().filter(item->item.editoraction.equals("guiinfo_cook")).findFirst().get();
		
		CObject warning_warning = listObjectres.stream().filter(item->item.editoraction.equals("warning_warning")).findFirst().get();
		CObject towninfo_water = listObjectres.stream().filter(item->item.editoraction.equals("towninfo_water")).findFirst().get();
		CObject towninfo_energy = listObjectres.stream().filter(item->item.editoraction.equals("towninfo_energy")).findFirst().get();
		CObject towninfo_money = listObjectres.stream().filter(item->item.editoraction.equals("towninfo_money")).findFirst().get();
		CObject towninfo_population = listObjectres.stream().filter(item->item.editoraction.equals("towninfo_population")).findFirst().get();
		CObject warning_energy = listObjectres.stream().filter(item->item.editoraction.equals("warning_energy")).findFirst().get();
		CObject warning_water = listObjectres.stream().filter(item->item.editoraction.equals("warning_water")).findFirst().get();
		CObject control_hakenok = listObjectres.stream().filter(item->item.editoraction.equals("control_hakenok")).findFirst().get();
		CObject livingroom_openbook = listObjectres.stream().filter(item->item.editoraction.equals("livingroom_openbook")).findFirst().get();
		CObject warning_homeless = listObjectres.stream().filter(item->item.editoraction.equals("warning_homeless")).findFirst().get();
		CObject address_cloning_icon = listObjectres.stream().filter(item->item.editoraction.equals("address_cloning_icon")).findFirst().get();
		CObject guiinfo_students = listObjectres.stream().filter(item->item.editoraction.equals("guiinfo_students")).findFirst().get();
		CObject guiinfo_teachers = listObjectres.stream().filter(item->item.editoraction.equals("guiinfo_teachers")).findFirst().get();
		CObject desk_paperwork = listObjectres.stream().filter(item->item.editoraction.equals("desk_paperwork")).findFirst().get();
		CObject school_paperflyer = listObjectres.stream().filter(item->item.editoraction.equals("school_paperflyer")).findFirst().get();
		CObject school_paperball = listObjectres.stream().filter(item->item.editoraction.equals("school_paperball")).findFirst().get();
		CObject guiinfo_playground = listObjectres.stream().filter(item->item.editoraction.equals("guiinfo_playground")).findFirst().get();
		CObject warning_bed = listObjectres.stream().filter(item->item.editoraction.equals("warning_bed")).findFirst().get();
		CObject recyclingcenter_garbagebag = listObjectres.stream().filter(item->item.editoraction.equals("recyclingcenter_garbagebag1")).findFirst().get();
		CObject road_shadow1 = listObjectres.stream().filter(item->item.editoraction.equals("road_shadow1")).findFirst().get();
		CObject pub_billard_queue1 = listObjectres.stream().filter(item->item.editoraction.equals("pub_billard_queue1")).findFirst().get();
		CObject address_moving_icon = listObjectres.stream().filter(item->item.editoraction.equals("address_moving_icon")).findFirst().get();		
		CObject guiinfo_workexp = listObjectres.stream().filter(item->item.editoraction.equals("guiinfo_workexp")).findFirst().get();
		CObject guiinfo_researchproject = listObjectres.stream().filter(item->item.editoraction.equals("guiinfo_researchproject")).findFirst().get();
		CObject warning_researchproject = listObjectres.stream().filter(item->item.editoraction.equals("warning_researchproject")).findFirst().get();
		CObject guiinfo_fuel1 = listObjectres.stream().filter(item->item.editoraction.equals("guiinfo_fuel1")).findFirst().get();
		CObject warning_fuel1 = listObjectres.stream().filter(item->item.editoraction.equals("warning_fuel1")).findFirst().get();
		CObject urbancemetery_coffin = listObjectres.stream().filter(item->item.editoraction.equals("urbancemetery_coffin1")).findFirst().get();
		
		CObject company_urbancemetery_grave1 = listObjectres.stream().filter(item->item.editoraction.equals("company_urbancemetery_grave1")).findFirst().get();
		CObject company_urbancemetery_grave2 = listObjectres.stream().filter(item->item.editoraction.equals("company_urbancemetery_grave2")).findFirst().get();
		CObject company_urbancemetery_grave3 = listObjectres.stream().filter(item->item.editoraction.equals("company_urbancemetery_grave3")).findFirst().get();
		CObject company_urbancemetery_grave4 = listObjectres.stream().filter(item->item.editoraction.equals("company_urbancemetery_grave4")).findFirst().get();
		CObject company_urbancemetery_grave5 = listObjectres.stream().filter(item->item.editoraction.equals("company_urbancemetery_grave5")).findFirst().get();
		CObject company_urbancemetery_grave6 = listObjectres.stream().filter(item->item.editoraction.equals("company_urbancemetery_grave6")).findFirst().get();
		CObject company_urbancemetery_grave7 = listObjectres.stream().filter(item->item.editoraction.equals("company_urbancemetery_grave7")).findFirst().get();
		CObject company_urbancemetery_grave8 = listObjectres.stream().filter(item->item.editoraction.equals("company_urbancemetery_grave8")).findFirst().get();
		CObject company_urbancemetery_grave9 = listObjectres.stream().filter(item->item.editoraction.equals("company_urbancemetery_grave9")).findFirst().get();
		CObject company_urbancemetery_grave10 = listObjectres.stream().filter(item->item.editoraction.equals("company_urbancemetery_grave10")).findFirst().get();
		CObject guiinfo_grave = listObjectres.stream().filter(item->item.editoraction.equals("guiinfo_grave")).findFirst().get();
		CObject guiinfo_workoutput_finance = listObjectres.stream().filter(item->item.editoraction.equals("guiinfo_workoutput_finance")).findFirst().get();
		CObject guiinfo_workoutput_population = listObjectres.stream().filter(item->item.editoraction.equals("guiinfo_workoutput_population")).findFirst().get();
		CObject guiinfo_workoutput_other = listObjectres.stream().filter(item->item.editoraction.equals("guiinfo_workoutput_other")).findFirst().get();
		CObject guiinfo_summary = listObjectres.stream().filter(item->item.editoraction.equals("guiinfo_summary")).findFirst().get();
		CObject info_attr_clean = listObjectres.stream().filter(item->item.editoraction.equals("info_attr_clean")).findFirst().get();
		CObject info_attr_eat = listObjectres.stream().filter(item->item.editoraction.equals("info_attr_eat")).findFirst().get();
		CObject info_attr_sleep = listObjectres.stream().filter(item->item.editoraction.equals("info_attr_sleep")).findFirst().get();
		CObject info_attr_toilet = listObjectres.stream().filter(item->item.editoraction.equals("info_attr_toilet")).findFirst().get();
		CObject button_address_clone = listObjectres.stream().filter(item->item.editoraction.equals("button_address_clone")).findFirst().get();
		CObject button_address_resize = listObjectres.stream().filter(item->item.editoraction.equals("button_address_resize")).findFirst().get();
		CObject button_address_move = listObjectres.stream().filter(item->item.editoraction.equals("button_address_move")).findFirst().get();
		CObject button_address_planning = listObjectres.stream().filter(item->item.editoraction.equals("button_address_planning")).findFirst().get();
		CObject gui_plus2 = listObjectres.stream().filter(item->item.editoraction.equals("gui_plus2")).findFirst().get();
		CObject warning_ill = listObjectres.stream().filter(item->item.editoraction.equals("warning_ill")).findFirst().get();
		CObject warning_ill2 = listObjectres.stream().filter(item->item.editoraction.equals("warning_ill2")).findFirst().get();
		CObject reading_magazine2 = listObjectres.stream().filter(item->item.editoraction.equals("reading_magazine2")).findFirst().get();
		CObject guiinfo_clothing = listObjectres.stream().filter(item->item.editoraction.equals("guiinfo_clothing")).findFirst().get();
		CObject bathroom_towel = listObjectres.stream().filter(item->item.editoraction.equals("bathroom_towel")).findFirst().get();
		CObject laundryobject = listObjectres.stream().filter(item->item.editoraction.contains("laundryobject")).findFirst().get();
		CObject toilet_open = listObjectres.stream().filter(item->item.editoraction.contains("toilet_open")).findFirst().get();
		CObject guiinfo_cup = listObjectres.stream().filter(p->p.editoraction.equals("guiinfo_cup")).findFirst().get();
		CObject guiinfo_coffeebean = listObjectres.stream().filter(p->p.editoraction.equals("guiinfo_coffeebean")).findFirst().get();
		CObject coffeepot = listObjectres.stream().filter(p->p.editoraction.equals("coffeepot1")).findFirst().get();
		CObject coffeebeans1 = listObjectres.stream().filter(p->p.editoraction.equals("coffeebeans1")).findFirst().get();
		CObject warning_caffeine = listObjectres.stream().filter(p->p.editoraction.equals("warning_caffeine")).findFirst().get();
		CObject speak_empty = listObjectres.stream().filter(p->p.editoraction.equals("speak_empty")).findFirst().get();
		CObject supermarket_buyin = listObjectres.stream().filter(p->p.editoraction.equals("supermarket_buyin")).findFirst().get();
		CObject warning_ill_contagious = listObjectres.stream().filter(p->p.editoraction.equals("warning_ill_contagious")).findFirst().get();
		CObject warning_ill_severe = listObjectres.stream().filter(p->p.editoraction.equals("warning_ill_severe")).findFirst().get();
		CObject generator_anim = listObjectres.stream().filter(p->p.editoraction.equals("generator_anim")).findFirst().get();
		CObject warning_ill_contagious2 = listObjectres.stream().filter(p->p.editoraction.equals("warning_ill_contagious2")).findFirst().get();
		CObject warning_ill_severe2 = listObjectres.stream().filter(p->p.editoraction.equals("warning_ill_severe2")).findFirst().get();
		CObject recyclingmachine3_anim = listObjectres.stream().filter(p->p.editoraction.equals("recyclingmachine3_anim")).findFirst().get();
		CObject guiinfo_paint = listObjectres.stream().filter(p->p.editoraction.equals("guiinfo_paint")).findFirst().get();
		CObject button_apply = listObjectres.stream().filter(p->p.editoraction.equals("button_apply")).findFirst().get();
		CObject poly_whiterect3 = listObjectres.stream().filter(item->item.editoraction.equals("poly_whiterect3")).findFirst().get();
		CObject guiinfo_residentenergy = listObjectres.stream().filter(item->item.editoraction.equals("guiinfo_residentenergy")).findFirst().get();
		CObject laser1 = listObjectres.stream().filter(item->item.editoraction.equals("laser1")).findFirst().get();
		CObject gui_rotation = listObjectres.stream().filter(item->item.editoraction.equals("gui_rotation")).findFirst().get();
		CObject guiinfo_illuminati = listObjectres.stream().filter(item->item.editoraction.equals("guiinfo_illuminati")).findFirst().get();
		CObject attr_belief = listObjectres.stream().filter(item->item.editoraction.equals("info_attr_belief")).findFirst().get();
		CObject pastor_head = listObjectres.stream().filter(item->item.editoraction.equals("pastor_head")).findFirst().get();
		CObject battlepriest_head = listObjectres.stream().filter(item->item.editoraction.equals("battlepriest_head")).findFirst().get();
		CObject battlepriest_spear = listObjectres.stream().filter(item->item.editoraction.equals("battlepriest_spear")).findFirst().get();
		CObject guiinfo_damage = listObjectres.stream().filter(item->item.editoraction.equals("guiinfo_damage")).findFirst().get();
		CObject guiinfo_range = listObjectres.stream().filter(item->item.editoraction.equals("guiinfo_range")).findFirst().get();
		CObject warning_health = listObjectres.stream().filter(item->item.editoraction.equals("warning_health")).findFirst().get();
		CObject guiinfo_shotfrequency = listObjectres.stream().filter(item->item.editoraction.equals("guiinfo_shotfrequency")).findFirst().get();
		CObject guiinfo_shotspeed = listObjectres.stream().filter(item->item.editoraction.equals("guiinfo_shotspeed")).findFirst().get();
		CObject guiinfo_townhallintelligence = listObjectres.stream().filter(item->item.editoraction.equals("guiinfo_townhallintelligence")).findFirst().get();
		CObject demand_dinner = listObjectres.stream().filter(item->item.editoraction.equals("demand_dinner")).findFirst().get();
		CObject demand_tv = listObjectres.stream().filter(item->item.editoraction.equals("demand_tv")).findFirst().get();
		CObject demand_tv2 = listObjectres.stream().filter(item->item.editoraction.equals("demand_tv2")).findFirst().get();
		CObject demand_sportscar = listObjectres.stream().filter(item->item.editoraction.equals("demand_sportscar")).findFirst().get();
		CObject demand_bookshelf = listObjectres.stream().filter(item->item.editoraction.equals("demand_bookshelf")).findFirst().get();
		CObject demand_sandpit = listObjectres.stream().filter(item->item.editoraction.equals("demand_sandpit")).findFirst().get();
		CObject demand_slide = listObjectres.stream().filter(item->item.editoraction.equals("demand_slide")).findFirst().get();
		CObject demand_seesaw = listObjectres.stream().filter(item->item.editoraction.equals("demand_seesaw")).findFirst().get();
		CObject zombie_entrance = listObjectres.stream().filter(item->item.editoraction.equals("zombie_entrance")).findFirst().get();
		CObject construction_head = listObjectres.stream().filter(item->item.editoraction.equals("construction_head")).findFirst().get();
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		

		textures.put("construction_head", construction_head.textureImage);
		textures.put("zombie_entrance", zombie_entrance.textureImage);
		textures.put("demand_dinner", demand_dinner.textureImage);
		textures.put("demand_tv", demand_tv.textureImage);
		textures.put("demand_tv2", demand_tv2.textureImage);
		textures.put("demand_sportscar", demand_sportscar.textureImage);
		textures.put("demand_bookshelf", demand_bookshelf.textureImage);
		textures.put("demand_sandpit", demand_sandpit.textureImage);
		textures.put("demand_slide", demand_slide.textureImage);
		textures.put("demand_seesaw", demand_seesaw.textureImage);
		textures.put("guiinfo_townhallintelligence", guiinfo_townhallintelligence.textureImage);
		textures.put("guiinfo_shotspeed", guiinfo_shotspeed.textureImage);
		textures.put("guiinfo_shotfrequency", guiinfo_shotfrequency.textureImage);
		textures.put("warning_health", warning_health.textureImage);
		textures.put("guiinfo_damage", guiinfo_damage.textureImage);
		textures.put("guiinfo_range", guiinfo_range.textureImage);
		textures.put("battlepriest_spear", battlepriest_spear.textureImage);
		textures.put("battlepriest_head", battlepriest_head.textureImage);
		textures.put("pastor_head", pastor_head.textureImage);
		textures.put("attr_belief", attr_belief.textureImage);
		textures.put("guiinfo_illuminati", guiinfo_illuminati.textureImage);
		textures.put("gui_rotation", gui_rotation.textureImage);
		textures.put("laser1", laser1.textureImage);
		textures.put("guiinfo_residentenergy", guiinfo_residentenergy.textureImage);
		textures.put("poly_whiterect3", poly_whiterect3.textureImage);
		textures.put("button_apply", button_apply.textureImage);
		textures.put("guiinfo_paint", guiinfo_paint.textureImage);
		textures.put("recyclingmachine3_anim", recyclingmachine3_anim.textureImage);
		textures.put("warning_ill_contagious2", warning_ill_contagious2.textureImage);
		textures.put("warning_ill_severe2", warning_ill_severe2.textureImage);
		textures.put("generator_anim", generator_anim.textureImage);
		textures.put("warning_ill_contagious", warning_ill_contagious.textureImage);
		textures.put("warning_ill_severe", warning_ill_severe.textureImage);
		textures.put("supermarket_buyin", supermarket_buyin.textureImage);
		textures.put("speak_empty", speak_empty.textureImage);
		textures.put("warning_caffeine", warning_caffeine.textureImage);
		textures.put("coffeebeans1", coffeebeans1.textureImage);
		textures.put("coffeepot", coffeepot.textureImage);
		textures.put("guiinfo_cup", guiinfo_cup.textureImage);
		textures.put("guiinfo_coffeebean", guiinfo_coffeebean.textureImage);
		textures.put("toilet_open", toilet_open.textureImage);
		textures.put("button_address_planning", button_address_planning.textureImage);
		textures.put("laundryobject", laundryobject.textureImage);
		textures.put("bathroom_towel", bathroom_towel.textureImage);
		textures.put("guiinfo_clothing", guiinfo_clothing.textureImage);		
		textures.put("reading_magazine2", reading_magazine2.textureImage);
		textures.put("warning_ill2", warning_ill2.textureImage);
		textures.put("warning_ill", warning_ill.textureImage);
		textures.put("gui_plus2", gui_plus2.textureImage);
		textures.put("button_address_clone", button_address_clone.textureImage);
		textures.put("button_address_resize", button_address_resize.textureImage);
		textures.put("button_address_move", button_address_move.textureImage);
		textures.put("button_address_planning", button_address_planning.textureImage);
		textures.put("info_attr_clean", info_attr_clean.textureImage);
		textures.put("info_attr_eat", info_attr_eat.textureImage);
		textures.put("info_attr_sleep", info_attr_sleep.textureImage);
		textures.put("info_attr_toilet", info_attr_toilet.textureImage);
		textures.put("guiinfo_summary", guiinfo_summary.textureImage);
		textures.put("guiinfo_workoutput_finance", guiinfo_workoutput_finance.textureImage);
		textures.put("guiinfo_workoutput_population", guiinfo_workoutput_population.textureImage);
		textures.put("guiinfo_workoutput_other", guiinfo_workoutput_other.textureImage);
		textures.put("guiinfo_grave", guiinfo_grave.textureImage);
		textures.put("company_urbancemetery_grave1", company_urbancemetery_grave1.textureImage);
		textures.put("company_urbancemetery_grave2", company_urbancemetery_grave2.textureImage);
		textures.put("company_urbancemetery_grave3", company_urbancemetery_grave3.textureImage);
		textures.put("company_urbancemetery_grave4", company_urbancemetery_grave4.textureImage);
		textures.put("company_urbancemetery_grave5", company_urbancemetery_grave5.textureImage);
		textures.put("company_urbancemetery_grave6", company_urbancemetery_grave6.textureImage);
		textures.put("company_urbancemetery_grave7", company_urbancemetery_grave7.textureImage);
		textures.put("company_urbancemetery_grave8", company_urbancemetery_grave8.textureImage);
		textures.put("company_urbancemetery_grave9", company_urbancemetery_grave9.textureImage);
		textures.put("company_urbancemetery_grave10", company_urbancemetery_grave10.textureImage);
		
		textures.put("urbancemetery_coffin", urbancemetery_coffin.textureImage);
		textures.put("warning_fuel1", warning_fuel1.textureImage);
		textures.put("guiinfo_fuel1", guiinfo_fuel1.textureImage);
		textures.put("warning_researchproject", warning_researchproject.textureImage);
		textures.put("guiinfo_researchproject", guiinfo_researchproject.textureImage);
		textures.put("guiinfo_workexp", guiinfo_workexp.textureImage);
		textures.put("address_moving_icon", address_moving_icon.textureImage);
		textures.put("pub_billard_queue1", pub_billard_queue1.textureImage);
		textures.put("road_shadow1", road_shadow1.textureImage);
		textures.put("recyclingcenter_garbagebag", recyclingcenter_garbagebag.textureImage);
		textures.put("warning_bed", warning_bed.textureImage);
		textures.put("guiinfo_playground", guiinfo_playground.textureImage);
		textures.put("school_paperball", school_paperball.textureImage);
		textures.put("school_paperflyer", school_paperflyer.textureImage);
		textures.put("desk_paperwork", desk_paperwork.textureImage);
		textures.put("guiinfo_students", guiinfo_students.textureImage);
		textures.put("guiinfo_teachers", guiinfo_teachers.textureImage);
		textures.put("address_cloning_icon", address_cloning_icon.textureImage);
		textures.put("warning_homeless", warning_homeless.textureImage);
		textures.put("livingroom_openbook", livingroom_openbook.textureImage);
		textures.put("control_hakenok", control_hakenok.textureImage);
		textures.put("warning_energy", warning_energy.textureImage);
		textures.put("warning_water", warning_water.textureImage);
		textures.put("towninfo_water", towninfo_water.textureImage);
		textures.put("towninfo_energy", towninfo_energy.textureImage);
		textures.put("towninfo_money", towninfo_money.textureImage);
		textures.put("towninfo_population", towninfo_population.textureImage);
		textures.put("warning_warning", warning_warning.textureImage);
		textures.put("res_waterPuddle", res_waterPuddle.textureImage);
		
		textures.put("guiinfo_cook", guiinfo_cook.textureImage);
		textures.put("guiinfo_food", guiinfo_food.textureImage);
		textures.put("guiinfo_brightness", guiinfo_brightness.textureImage);
		textures.put("guiinfo_heatingpower", guiinfo_heatingpower.textureImage);
		textures.put("guiinfo_energyconsumption", guiinfo_energyconsumption.textureImage);
		textures.put("guiinfo_waterconsumption", guiinfo_waterconsumption.textureImage);
		textures.put("guiinfo_energyoutput", guiinfo_energyoutput.textureImage);
		textures.put("guiinfo_wateroutput", guiinfo_wateroutput.textureImage);
		textures.put("guiinfo_worker", guiinfo_worker.textureImage);
		textures.put("guiinfo_workoutput2", guiinfo_workoutput2.textureImage);
		textures.put("guiinfo_warning", guiinfo_warning.textureImage);
		textures.put("guiinfo_intelligence", guiinfo_intelligence.textureImage);
		textures.put("guiinfo_fitness", guiinfo_fitness.textureImage);
		textures.put("guiinfo_education", guiinfo_education.textureImage);
		textures.put("warning_dark", warning_dark.textureImage);
		textures.put("warning_cold", warning_cold.textureImage);
		textures.put("poly_whiterect", poly_whiterect.textureImage);
		textures.put("poly_whiterect2", poly_whiterect2.textureImage);
		textures.put("warning_owner", warning_owner.textureImage);
		textures.put("gui_worktime", gui_worktime.textureImage);
		textures.put("gui_plus", gui_plus.textureImage);
		textures.put("gui_minus", gui_minus.textureImage);
		textures.put("cursor_resize", cursor_resize.textureImage);
		textures.put("gui_arrowleft", gui_arrowleft.textureImage);
		textures.put("gui_arrowright", gui_arrowright.textureImage);
		
		warning_worker2.textureImage.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		textures.put("warning_worker2", warning_worker2.textureImage);
		
		textures.put("anim_showwork", anim_showwork.textureImage);
		textures.put("warning_worker", warning_worker.textureImage);
		textures.put("control_button_play2", control_button_play2.textureImage);
		textures.put("control_button_play3", control_button_play3.textureImage);
		textures.put("control_button_movehouse", control_button_movehouse.textureImage);
		textures.put("gui_forbidden", gui_forbidden.textureImage);
		textures.put("address_public", address_public.textureImage);
		textures.put("address_residential", address_residential.textureImage);
		textures.put("weather_rain", weather_rain.textureImage);
		textures.put("gui_menu", gui_menu.textureImage);
		textures.put("background_gras_t2", background_gras_t2.textureImage);
		
		textures.put("guiinfo_electricity", guiinfo_electricity.textureImage);
		textures.put("guiinfo_water", guiinfo_water.textureImage);
		textures.put("guiinfo_population", guiinfo_population.textureImage);
		textures.put("guiinfo_workplace", guiinfo_workplace.textureImage);
		textures.put("guiinfo_average", guiinfo_average.textureImage);
		textures.put("guiinfo_money", guiinfo_money.textureImage);
		textures.put("guiinfo_workoutput", guiinfo_workoutput.textureImage);
		textures.put("guiinfo_condition", guiinfo_condition.textureImage);
		
		textures.put("icon_health", icon_health.textureImage);
		textures.put("icon_happiness", icon_happiness.textureImage);
		textures.put("guiinfo_age", guiinfo_age.textureImage);
		textures.put("energy_flash", energy_flash.textureImage);
		//textures.put("energy_flash2", energy_flash2.textureIcon);
		textures.put("energy_waterdrop", energy_waterdrop.textureImage);
		textures.put("warning_workinput", warning_workinput.textureImage);
		
//		textures.put("company_supermarket_shoppingcart_f1", company_supermarket_shoppingcart_f1.textureIcon);
//		textures.put("company_supermarket_shoppingcart_f2", company_supermarket_shoppingcart_f2.textureIcon);
//		textures.put("company_supermarket_shoppingcart_f3", company_supermarket_shoppingcart_f3.textureIcon);
//		textures.put("company_supermarket_shoppingcart_f4", company_supermarket_shoppingcart_f4.textureIcon);
//		textures.put("company_supermarket_shoppingcart_f5", company_supermarket_shoppingcart_f5.textureIcon);
		
		textures.put("company_supermarket_food1", company_supermarket_food1.textureImage);
		textures.put("company_supermarket_food2", company_supermarket_food2.textureImage);
		textures.put("company_supermarket_food3", company_supermarket_food3.textureImage);
		textures.put("company_supermarket_food4", company_supermarket_food4.textureImage);
		textures.put("company_supermarket_food5", company_supermarket_food5.textureImage);
		textures.put("company_supermarket_food6", company_supermarket_food6.textureImage);
		textures.put("company_supermarket_food7", company_supermarket_food7.textureImage);
		textures.put("company_supermarket_food8", company_supermarket_food8.textureImage);
		
		textures.put("icon_dead",icon_dead.textureImage);
		textures.put("guiinfo_clear", guiinfo_clear.textureImage);
		textures.put("warning_condition", warning_condition.textureImage);
//		textures.put("company_supermarket_shelf_f2", company_supermarket_shelf_f2.textureIcon);
//		textures.put("company_supermarket_shelf_f3", company_supermarket_shelf_f3.textureIcon);
//		textures.put("company_supermarket_shelf_f4", company_supermarket_shelf_f4.textureIcon);
//		textures.put("company_supermarket_shelf_f5", company_supermarket_shelf_f5.textureIcon);
//		textures.put("company_supermarket_shelf_f6", company_supermarket_shelf_f6.textureIcon);
//		textures.put("company_supermarket_shelf_f7", company_supermarket_shelf_f7.textureIcon);
//		textures.put("company_supermarket_shelf_f8", company_supermarket_shelf_f8.textureIcon);
		
		textures.put("weather_snow", weather_snow.textureImage);
		textures.put("background_grass", background_grass.textureImage);
		textures.put("background_grasssnow1", background_grasssnow1.textureImage);
		textures.put("background_grasssnow2", background_grasssnow2.textureImage);
		textures.put("background_grasssnow3", background_grasssnow3.textureImage);
		//textures.put("background_grasssnow4", background_grasssnow4.textureImage);
	}
	
	private void initResAnimations()
	{

		CObject deadzombie = listObjectres.stream().filter(item->item.editoraction.equals("deadzombie")).findFirst().get();
		deadzombie.objectAnimation.setFrameDuration(0.1f);
		animations.put("deadzombie", deadzombie.objectAnimation);
		
		CObject human_woman = listObjectres.stream().filter(item->item.editoraction.equals("human_woman")).findFirst().get();
		human_woman.objectAnimation.setFrameDuration(0.1f);
		animations.put("human_woman", human_woman.objectAnimation);

		CObject human_man = listObjectres.stream().filter(item->item.editoraction.equals("human_man")).findFirst().get();
		human_man.objectAnimation.setFrameDuration(0.1f);
		animations.put("human_man", human_man.objectAnimation);
		
		CObject texture_snow1 = listObjectres.stream().filter(item->item.editoraction.equals("texture_snow1")).findFirst().get();
		texture_snow1.objectAnimation.setFrameDuration(0.1f);
		animations.put("texture_snow1", texture_snow1.objectAnimation);
		
		CObject kitchen_pan = listObjectres.stream().filter(item->item.editoraction.equals("kitchen_pan")).findFirst().get();
		kitchen_pan.objectAnimation.setFrameDuration(0.1f);
		animations.put("kitchen_pan", kitchen_pan.objectAnimation);
		
		CObject kitchen_plate = listObjectres.stream().filter(item->item.editoraction.equals("kitchen_plate")).findFirst().get();
		kitchen_plate.objectAnimation.setFrameDuration(0.1f);
		animations.put("kitchen_plate", kitchen_plate.objectAnimation);
		
		CObject obj_human_action12 = listObjectres.stream().filter(item->item.editoraction.equals("human_action12")).findFirst().get();
		obj_human_action12.objectAnimation.setFrameDuration(0.1f);
		animations.put("human_action12", obj_human_action12.objectAnimation);

		CObject human_action_repair = listObjectres.stream().filter(item->item.editoraction.equals("human_action_repair")).findFirst().get();
		human_action_repair.objectAnimation.setFrameDuration(0.1f);
		animations.put("human_action_repair", human_action_repair.objectAnimation);
		
		CObject obj_human_action11 = listObjectres.stream().filter(item->item.editoraction.equals("human_action11")).findFirst().get();
		obj_human_action11.objectAnimation.setFrameDuration(0.1f);
		animations.put("human_action11", obj_human_action11.objectAnimation);
		
		CObject obj_human_action1 = listObjectres.stream().filter(item->item.editoraction.equals("human_action1")).findFirst().get();
		obj_human_action1.objectAnimation.setFrameDuration(0.1f);
		animations.put("human_action1", obj_human_action1.objectAnimation);

		CObject obj_human_action1_z = listObjectres.stream().filter(item->item.editoraction.equals("human_action1_z")).findFirst().get();
		obj_human_action1_z.objectAnimation.setFrameDuration(0.1f);
		animations.put("human_action1_z", obj_human_action1_z.objectAnimation);
		
		CObject obj_human_action9 = listObjectres.stream().filter(item->item.editoraction.equals("human_action9")).findFirst().get();
		obj_human_action9.objectAnimation.setFrameDuration(0.1f);
		animations.put("human_action9", obj_human_action9.objectAnimation);
		
		CObject obj_human_action2 = listObjectres.stream().filter(item->item.editoraction.equals("human_action2")).findFirst().get();
		obj_human_action2.objectAnimation.setFrameDuration(0.1f);
		animations.put("human_action2", obj_human_action2.objectAnimation);
		
		CObject obj_human_action3 = listObjectres.stream().filter(item->item.editoraction.equals("human_action3")).findFirst().get();
		obj_human_action3.objectAnimation.setFrameDuration(0.1f);
		animations.put("human_action3", obj_human_action3.objectAnimation);
		
		CObject obj_human_action4 = listObjectres.stream().filter(item->item.editoraction.equals("human_action4")).findFirst().get();
		obj_human_action4.objectAnimation.setFrameDuration(0.1f);
		animations.put("human_action4", obj_human_action4.objectAnimation);
		
		CObject obj_human_action6 = listObjectres.stream().filter(item->item.editoraction.equals("human_action6")).findFirst().get();
		obj_human_action6.objectAnimation.setFrameDuration(0.1f);
		animations.put("human_action6", obj_human_action6.objectAnimation);

		CObject obj_human_action6_2 = listObjectres.stream().filter(item->item.editoraction.equals("human_action6_2")).findFirst().get();
		obj_human_action6_2.objectAnimation.setFrameDuration(0.1f);
		animations.put("human_action6_2", obj_human_action6_2.objectAnimation);
				
		CObject obj_human_action6_shoes = listObjectres.stream().filter(item->item.editoraction.equals("human_action6_shoes")).findFirst().get();
		obj_human_action6_shoes.objectAnimation.setFrameDuration(0.1f);
		animations.put("human_action6_shoes", obj_human_action6_shoes.objectAnimation);
		
		CObject obj_human_action7 = listObjectres.stream().filter(item->item.editoraction.equals("human_action7")).findFirst().get();
		obj_human_action7.objectAnimation.setFrameDuration(0.1f);
		animations.put("human_action7", obj_human_action7.objectAnimation);

		CObject obj_human_action8 = listObjectres.stream().filter(item->item.editoraction.equals("human_action8")).findFirst().get();
		obj_human_action8.objectAnimation.setFrameDuration(0.1f);
		animations.put("human_action8", obj_human_action8.objectAnimation);
		
		CObject obj_human_action10 = listObjectres.stream().filter(item->item.editoraction.equals("human_action10")).findFirst().get();
		obj_human_action10.objectAnimation.setFrameDuration(0.1f);
		animations.put("human_action10", obj_human_action10.objectAnimation);
		
		CObject human_man_arms = listObjectres.stream().filter(item->item.editoraction.equals("human_man_arms")).findFirst().get();
		human_man_arms.objectAnimation.setFrameDuration(0.08f);
		animations.put("human_man_arms", human_man_arms.objectAnimation);

		CObject human_woman_arms = listObjectres.stream().filter(item->item.editoraction.equals("human_woman_arms")).findFirst().get();
		human_woman_arms.objectAnimation.setFrameDuration(0.08f);
		animations.put("human_woman_arms", human_woman_arms.objectAnimation);
		
		CObject human_man_legs = listObjectres.stream().filter(item->item.editoraction.equals("human_man_legs")).findFirst().get();
		human_man_legs.objectAnimation.setFrameDuration(0.08f);
		animations.put("human_man_legs", human_man_legs.objectAnimation);
		
		CObject human_man_shoes = listObjectres.stream().filter(item->item.editoraction.equals("human_man_shoes")).findFirst().get();
		human_man_shoes.objectAnimation.setFrameDuration(0.08f);
		animations.put("human_man_shoes", human_man_shoes.objectAnimation);
		
		CObject anim_sleep = listObjectres.stream().filter(item->item.editoraction.equals("anim_sleep")).findFirst().get();
		anim_sleep.objectAnimation.setFrameDuration(0.2f);
		anim_sleep.objectAnimation.setPlayMode(PlayMode.LOOP_PINGPONG);
		animations.put("anim_sleep", anim_sleep.objectAnimation);
		
		CObject anim_shower = listObjectres.stream().filter(item->item.editoraction.equals("anim_shower")).findFirst().get();
		anim_shower.objectAnimation.setFrameDuration(0.08f);
		anim_shower.objectAnimation.setPlayMode(PlayMode.LOOP);
		animations.put("anim_shower", anim_shower.objectAnimation);
		
		CObject anim_shower2 = listObjectres.stream().filter(item->item.editoraction.equals("anim_shower2")).findFirst().get();
		anim_shower2.objectAnimation.setFrameDuration(0.08f);
		anim_shower2.objectAnimation.setPlayMode(PlayMode.LOOP);
		animations.put("anim_shower2", anim_shower2.objectAnimation);
		
		CObject anim_shower3 = listObjectres.stream().filter(item->item.editoraction.equals("anim_shower3")).findFirst().get();
		anim_shower3.objectAnimation.setFrameDuration(0.08f);
		anim_shower3.objectAnimation.setPlayMode(PlayMode.LOOP);
		animations.put("anim_shower3", anim_shower3.objectAnimation);
		
		CObject pub_beverage = listObjectres.stream().filter(item->item.editoraction.equals("pub_beverage")).findFirst().get();
		pub_beverage.objectAnimation.setFrameDuration(0.08f);
		animations.put("pub_beverage", pub_beverage.objectAnimation);
	}
		
	public Boolean isObjectResearched(String editoraction)
	{
		Optional<CObject> obj = listObject.stream().filter(item->item.editoraction.contains(editoraction)).findFirst();
		if(obj.isPresent())
		{
			if(obj.get().iResearchCurrentWorkoutput>=obj.get().iResearchTargetWorkoutput)
				return true;
		}
		
		return false;
	}
	
	public void initObjects(FileHandle thefile, ArrayList<CObject> listobj, Boolean isRes) 
	{
		try {
			FileHandle file = thefile; //Gdx.files.internal("config/objects.csv");
			BufferedReader reader = new BufferedReader(file.reader());
			
			//int count=0;
			String line = reader.readLine();
			while(line!=null) 
			{
				//				if(count==0) //Erste Zeile ist Überschriftenzeile
				//				{
				//					count++;
				//					line = reader.readLine();
				//					continue;
				//				}
				
				if(line.trim().isEmpty()) //Leerzeilen überspringen
				{
					line = reader.readLine();
					continue;
				}
				
				//[OBJECTID];[OBJECTNAME];[OBJECTTYPEID];[WIDTH];[HEIGHT];[textureFilename];[FILENAME];[FRAMECOLS];[FRAMEROWS];[MOVE_STARTFRAME];[MOVE_ENDFRAME];[ACTION1_STARTFRAME];[ACTION1_ENDFRAME];[IDLEFRAME]
				//;[EDITORACTION];[COLTYPE];[OBJ_ROT];[OBJ_MOVE_DIR1];[OBJ_MOV_PIX1];[OBJ_MOVE_DIR2];[OBJ_MOV_PIX2];[PRICE]
				String[] splitstring = line.split(";");
				CObject obj = new CObject(town);
				
				if(splitstring[0].contains("//")) //Kommentare überspringen
				{
					line = reader.readLine();
					continue;
				}
				
				//Objekte mit gleicher ID ersetzen
				String objectid=splitstring[0]; 
				CObject dobj=null;
				for(CObject obj1 : listobj)
				{
					if(obj1.objectId.equals(objectid))
					{
						dobj=obj1;
						break;
					}
				}
				
				if(dobj!=null)
					listobj.remove(dobj);
				
				
				//Modobjects mit Modkürzel kennzeichnen, damit sich Mods nicht gegenseitig überschreiben
				//if(dobj==null && thefile.path().contains("/mod/"))
				//{
				//	String[] l1 = thefile.nameWithoutExtension().split("_");
				//	objectid=objectid+"_"+l1[l1.length-1];
				//}
				
				
				//------------
				//Link Object
				//------------
				//Objekte werden nur einmal definiert, Gfx wird im clone prozess gelinked
				//009008010;Foosball;009008;LINKOBJECT;000000001;company_pub_billard
				String islink = splitstring[3];
				String linkid = splitstring[4];
				
				if(islink.toLowerCase().equals("linkobject"))
				{
					Optional<CObject> lobj = listLinkObject.stream().filter(item->item.objectId.equals(linkid)).findFirst();
					//CObject lobj = listLinkObject.get(objectid);
					if(lobj.isPresent())
					//if(lobj!=null)
					{
						obj = lobj.get().clone();
						obj.objectId  = objectid;
						obj.objectTypeId = splitstring[2];
						obj.roomtype = splitstring[5];
						obj.editoraction = splitstring[6];
						obj.linkobjectid=lobj.get().objectId;
						listobj.add(obj);
						
						if(obj.workplaceHasSkill())
							listWorkplacesAndTasksWithSkill.add(obj);
					}
					
					line = reader.readLine();
					continue;
				}
				
				
				//-----------------
				//Read Object Data
				//-----------------
				obj.objectId  = objectid;
				obj.objectName = splitstring[1];
				obj.objectTypeId = splitstring[2];
				
				obj.width = Integer.parseInt(splitstring[3]);
				obj.height = Integer.parseInt(splitstring[4]);
				
				if(town.fSizeFactor>0)
				{
					obj.width/=town.fSizeFactor;
					obj.height/=town.fSizeFactor;
				}
				
				
				//obj.original_width=obj.width;
				//obj.original_height=obj.height;
				
				//obj.width = (int) CHelper.getScreenValueX(obj.width);
				//obj.height = (int) CHelper.getScreenValueX(obj.height);
				obj.textureFilename = splitstring[5];
				obj.frameCols = Integer.parseInt(splitstring[6]);
				obj.frameRows = Integer.parseInt(splitstring[7]);
				obj.roomtype = splitstring[8];
				obj.editoraction = splitstring[9];
				
				obj.ObjectAction_Rotation = Integer.parseInt(splitstring[10]);
				//obj.ObjectAction_Move_Direction = Integer.parseInt(splitstring[17]); //Achtung: muss so sein wegen rotation geht move x,y nicht
				//obj.ObjectAction_Move_Pixels = Integer.parseInt(splitstring[18]);
				obj.ObjectAction_Move_Pixels_X = (int) town.getSizeValue(Integer.parseInt(splitstring[11]));
				obj.ObjectAction_Move_Pixels_Y = (int) town.getSizeValue(Integer.parseInt(splitstring[12]));
				
				//obj.price=100;
				//if(splitstring.length>13) //objectres hat teilweise keine preise
				obj.price = Integer.parseInt(splitstring[13]);
				if(obj.editoraction.contains("floor") && town.setRoomPrice>-1)
					obj.price=town.setRoomPrice;
				
				//obj.price=obj.getResizeByScrollingPrice(1, obj.width+obj.height);
				obj.original_price=obj.price;
								
				
				int lastindex=13; //Für optionale attribute -> werden weiter unten gesetzt (überschreiben default werte)
				
				//obj.ObjectAction_Move_Direction = Integer.parseInt(splitstring[17]); //Achtung: muss so sein wegen rotation geht move x,y nicht
				//obj.ObjectAction_Move_Pixels = Integer.parseInt(splitstring[18]);
				//if(splitstring.length>20)
				//{
				//	obj.ObjectAction_Move_Direction2 = Integer.parseInt(splitstring[19]); //Achtung: muss so sein wegen rotation geht move x,y nicht
				//	obj.ObjectAction_Move_Pixels2 = Integer.parseInt(splitstring[20]);
				//}
				
				//				if(obj.editoraction.contains("human_"))
				//					obj.isHuman=true;
				
				obj.mipmap = true;
				
				try
				{
					
					//-------------------------------------------
					
					//Max Objectcount
					if(obj.editoraction.contains("company_anycompany_server"))
					{
						obj.maxObjectCount=1;
					}
					
					//Z-Order
					obj.zorder=2;
					
					if(obj.editoraction.contains("outdoor_ground"))
						obj.zorder=-3;
					if(obj.editoraction.contains("road_road_footpath"))
						obj.zorder=-2;
					if(obj.editoraction.contains("road_road_road"))
						obj.zorder=-1;
					if(obj.editoraction.contains("road_road_parkingspace"))
						obj.zorder=0;
					if(obj.editoraction.contains("residential_garage"))
						obj.zorder=0;
					if(obj.editoraction.contains("floor"))
						obj.zorder=0;
					if(obj.editoraction.contains("pavement"))
						obj.zorder=0;
					if(obj.editoraction.contains("carpet"))
						obj.zorder=1;
					if(obj.editoraction.contains("buildingwall") || obj.editoraction.contains("window"))
						obj.zorder=2;
					if(obj.editoraction.contains("outdoor_plant") || obj.editoraction.contains("outdoor_flower"))
						obj.zorder=1;
					
					if(obj.editoraction.contains("illuminati_defensesystem") 
							|| obj.editoraction.contains("illuminati_defensewarningsystem"))
						obj.zorder=3;

					
					//Objekte die getragen werden
					if(obj.editoraction.contains("fruitplate"))
						obj.zorder=3;
					if(obj.editoraction.contains("recyclingcenter_garbagebag"))
						obj.zorder=3;
					if(obj.editoraction.contains("supermarket_foodpallet"))
						obj.zorder=3;
					
					//---------------------------
					
					if(obj.editoraction.contains("human")) //human hat eigene list
						obj.zorder=99;
						//obj.zorder=3;
					
					if(obj.editoraction.contains("traffic_car"))
						obj.zorder=4;
					if(obj.editoraction.contains("light"))
						obj.zorder=5;
					if(obj.editoraction.contains("tree"))
						obj.zorder=5;
					if(obj.editoraction.contains("bird"))
						obj.zorder=6;
					
					obj.coltype = "1";
					if(obj.editoraction.contains("floor"))
						obj.coltype="0";
					
					if(obj.editoraction.contains("floor"))
						obj.isRoomObject=true;
					
					if(obj.editoraction.contains("recyclingcenter_garbagecontainer"))
						obj.isGarbageContainer=true;
					
					if(obj.editoraction.contains("traffic_car"))
						obj.isCar=true;
					
					if(obj.editoraction.contains("company_gasstation_gaspump"))
						obj.drawShadow=false;
					
					if(obj.editoraction.contains("_fruitplate"))
					{
						obj.sBaseObject="_coffeetable";
						obj.drawShadow=false;
					}
					
					if(obj.editoraction.contains("fence"))
					{	
						obj.doRasterPlacement=true;
						obj.iRasterValue=obj.width;
					}
					
					if(obj.editoraction.contains("buildingwall"))
					{	
						obj.doRasterPlacement=true;
						obj.width=100;
						obj.height=100;
						obj.iRasterValue=obj.width;
						obj.roomtype="outside_anyroom_address";
						//obj.coltype="2";
					}
					
					if(obj.editoraction.contains("road_road_road"))
					{
						//obj.iRasterValue=town.roadsize;
						obj.iRasterValue=town.roadrastersize;
						//obj.width=town.roadsize;
						//obj.height=town.roadsize;
						obj.drawShadow=true;
					}
					
					if(obj.editoraction.contains("road_road_footpath"))
					{
						obj.iRasterValue=town.footpathsize;
						//obj.width=town.footpathsize;
						//obj.height=town.footpathsize;
					}
					
					if(obj.editoraction.contains("road_road_parkingspace"))
					{
						//256
						//32
						//obj.width=town.floorrastersize*(int)11; //352;
						//obj.height=town.floorrastersize*(int)(17); //544;
						
						obj.width=32*(int)11; //352;
						obj.height=32*(int)(17); //544;
						
						obj.iRasterValue=town.floorrastersize;
					}
					
					if(obj.editoraction.contains("residential_garage"))
					{
						//obj.width=350;
						//obj.height=525;
						//obj.width=town.floorrastersize*(int)(11); //352;
						//obj.height=town.floorrastersize*(int)(17); //544;
						
						obj.width=32*(int)(11); //352;
						obj.height=32*(int)(17); //544;
						
						obj.iRasterValue=town.floorrastersize;
					}
					
					if(obj.editoraction.contains("illuminati_defensewall"))
					{
						obj.doRasterPlacement=true;
						obj.iRasterValue=obj.width;
					}
					
					if(obj.editoraction.contains("floor"))
						obj.iRasterValue=town.floorrastersize;
					
					if(obj.editoraction.contains("outdoor_ground"))
						obj.iRasterValue=32;
					
					if(obj.editoraction.contains("interior_light"))
						obj.drawShadow=false;
					
					if(obj.editoraction.contains("bathroom_bathmat"))
					{
						obj.zorder=1;
						obj.drawShadow=false;
					}
					
					if(obj.editoraction.contains("floor"))
					{
						obj.isRoomObject=true;
						obj.drawShadow=true;
						//obj.price=50;
					}
					
					if(obj.editoraction.contains("outdoor_ground_water"))
					{
						obj.isWaterObject=true;
					}
					
					if(obj.editoraction.contains("outdoor_ground"))
					{
						obj.isGroundObject=true;
						obj.doRasterPlacement = true;
						obj.drawShadow=true;
						obj.price=town.groundprice;
						obj.original_price=town.groundprice;
						//obj.price=10;
					}
					
					if(obj.isRoomObject || obj.editoraction.contains("road") || obj.editoraction.contains("pavement"))
						obj.doRasterPlacement = true;
					
					if(obj.editoraction.contains("residential_garage"))
						obj.doRasterPlacement = true;
					
					if(obj.editoraction.contains("carpet"))
					{
						obj.drawShadow=false;
					}
					
					if(obj.editoraction.contains("window"))
					{
						obj.drawShadow=false;
					}
					
					if(obj.isRoomObject)
						obj.rotationValue=90;

					if(obj.editoraction.contains("window") || obj.editoraction.contains("object_door"))
					{
						obj.rotationValue=90;
						obj.doRasterPlacement=true;
						obj.iRasterValue=town.floorrastersize;
						
						if(obj.editoraction.contains("window"))
						{
							obj.iRasterValue_movx=0;
							obj.iRasterValue_movy=5;
						}
						
						if(obj.editoraction.contains("object_door"))
						{
							obj.iRasterValue_movx=-5;
							obj.iRasterValue_movy=12;
						}
					}
					
					if(obj.editoraction.contains("floor"))
					{
						obj.mipmap=false;
						obj.width=town.floorSize;
						obj.height=town.floorSize;
					}
					
					Boolean bm=false;
					
					if(obj.editoraction.contains("road_road"))
						obj.mipmap=bm;
					
					if(obj.editoraction.contains("outdoor_tree"))
						obj.mipmap=false;
					
					if(obj.editoraction.contains("outdoor_ground"))
						obj.mipmap=bm;
					
					if(obj.editoraction.contains("outdoor_ground"))
					{
						obj.width=town.groundsize;
						obj.height=town.groundsize;
					}
					
					if(obj.editoraction.contains("outside_ground_base"))
					{
						obj.width=town.groundbasesize;
						obj.height=town.groundbasesize;
						obj.drawShadow=true;
						obj.mipmap=bm;
						obj.doRasterPlacement=true;
						obj.iRasterValue=Math.round(town.groundbasesize/1.3f);
						//obj.iRasterValue=100;
						obj.isGroundObject=false;
						obj.isGroundBaseObject=true;
						obj.zorder=-3;
					}
										
					
//					if(obj.editoraction.contains("illuminati_defensesystem"))
//					{
//						obj.defense_schussfrequenz_timer=0;
//						obj.defense_schussfrequenz=10;
//						obj.defense_reichweite=1600;
//						obj.defense_schaden=1;
//						obj.defense_projektilgeschwindigkeit=80;
//					}
					
					//if(obj.editoraction.contains("illuminati_defensewarningsystem"))
					//	obj.defense_reichweite=4200;
						//obj.defense_reichweite=3000;
											
					//if(obj.isRoomObject)
					//	obj.ATTR_BASEREQ=1;
					
					//if(obj.editoraction.contains("wall"))
					//{
					//	obj.width=town.wallSize;
					//	obj.height=town.wallSize;
					//}
					
					if(obj.editoraction.contains("background"))
						obj.mipmap=false;
					
					if(!isRes && town.bUseTextureAtlas)
					{
						if(obj.textureFilename.contains("."))
							obj.textureFilename=obj.textureFilename.substring(4, obj.textureFilename.length()-4);
					}
					
					if(obj.textureFilename.contains("."))
						obj.textureImage = new Texture(Gdx.files.internal(obj.textureFilename), true);
					else
					{
						if(obj.textureFilename.contains("res="))
						{
							String split1[] = obj.textureFilename.split("=");
							obj.textureImage = listObjectres.stream().filter(item->item.editoraction.toLowerCase().equals(split1[1].toLowerCase())).findFirst().get().textureImage; 
						}
						else
						{
							try
							{
								obj.textureImage = town.textureAtlas.findRegion(obj.textureFilename).getTexture();
							}
							catch(Exception ex)
							{
								Gdx.app.debug("", "TextureAtlas FindRegion Error: " + obj.textureFilename);	
							}
						}
					}
					
					//obj.height/=5;
					//obj.width/=5;
					
					obj.original_height=obj.height;
					obj.original_width=obj.width;
					
					
					
					obj.price*=town.objectPriceDelta; //CTown Customizing Schraube
					
					
					//**************************
					//Optionale Attribute setzen
					//**************************
					lastindex--;
					while(true)
					{
						lastindex++;
						if(splitstring.length > lastindex)
						{
							if(splitstring[lastindex].contains("="))
							{
								String ar[] = splitstring[lastindex].split("=");
								String attr = ar[0];
								String val = ar[1];
								obj.setAttribute(attr, val);
							}
							else
								continue;
						}
						else
							break;
					}
					
					
					if(!town.bBaseGround)
						obj.ATTR_BASEREQ=0;
															
					if(!obj.ATTR_ICON.isEmpty())
						obj.textureIcon = new Texture(Gdx.files.internal(obj.ATTR_ICON), true);
					
					
					//********
					//Textures
					//********
					{
						initAdditionalTexture(obj, 2);
						initAdditionalTexture(obj, 3);
						
						if(obj.mipmap && town.bUseMipMapping)
						{
							if(obj.isGroundObject || obj.editoraction.contains("road_road"))
								obj.textureImage.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.MipMapLinearNearest);
								//obj.textureImage.setFilter(TextureFilter.MipMapNearestNearest, TextureFilter.MipMapNearestNearest);
								//obj.textureImage.setFilter(TextureFilter.MipMap, TextureFilter.Nearest);
							else
								//obj.textureImage.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
								//obj.textureImage.setFilter(TextureFilter.MipMap, TextureFilter.MipMap);
								obj.textureImage.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Linear);
						}
						else
							obj.textureImage.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
						
						if(obj.isRoomObject)
							obj.textureImage.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
						
						if(obj.editoraction.contains("foundation"))
							obj.textureImage.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
						
						if(town.bResearched)
						{
							obj.iResearchCurrentWorkoutput=obj.iResearchTargetWorkoutput;
						}
						
						//Animations werden auch wenn special object nicht speziell geladen
						if(obj.frameCols==0)
						{
							if(isSpecialObject(obj.editoraction) && !isRes)
							{
								loadSpecialTexture(obj);
							}
							else
							{
								obj.textureRegion = new TextureRegion[1];
								obj.textureRegion[0] = new TextureRegion(obj.textureImage);
							}
						}
						else
						{
					        TextureRegion[][] tmp = TextureRegion.split(obj.textureImage, obj.textureImage.getWidth()/obj.frameCols, obj.textureImage.getHeight()/obj.frameRows);
					        obj.textureRegion = new TextureRegion[obj.frameCols * obj.frameRows];
					        int index = 0;
					        for (int i = 0; i < obj.frameRows; i++) {
					            for (int j = 0; j < obj.frameCols; j++) {
					            	obj.textureRegion[index++] = tmp[i][j];
					            }
					        }
					        
					        obj.objectAnimation = new Animation(0.08f, obj.textureRegion); 
					        
							if(obj.editoraction.contains("bird"))
								obj.objectAnimation.setFrameDuration(0.08f);
						}
					}
				}
				catch(Exception e)
				{
					CHelper.writeError(e.getMessage(), e.getStackTrace(), e);
					line = reader.readLine();
					continue;
				}
				
				obj.setDynamicAttributes();
				
				listobj.add(obj);
				
				if(obj.workplaceHasSkill())
				{
					listWorkplacesAndTasksWithSkill.add(obj);
				}
				
				line = reader.readLine();
			}
		} 
		catch (IOException e) {
			CHelper.writeError(e.getMessage(), e.getStackTrace(), e);
		}
	}
	
	private void initAdditionalTexture(CObject obj, int nr)
	{
		String filename="";

		if(nr==2)
			filename=obj.ATTR_T2;
		if(nr==3)
			filename=obj.ATTR_T3;
		
		if(!filename.isEmpty())
		{
			if(town.bUseTextureAtlas)
			{
				if(filename.contains("."))
					filename=filename.substring(4, filename.length()-4);
			}
			
			if(filename.contains("."))
			{
				if(nr==2)
					obj.textureImage2 = new Texture(Gdx.files.internal(filename), true);
				if(nr==3)
					obj.textureImage3 = new Texture(Gdx.files.internal(filename), true);
			}
			else
			{
				try
				{
					if(nr==2)
						obj.textureImage2 = town.textureAtlas.findRegion(filename).getTexture();
					if(nr==3)
						obj.textureImage3 = town.textureAtlas.findRegion(filename).getTexture();
				}
				catch(Exception ex)
				{
					Gdx.app.debug("", "TextureAtlas FindRegion Error: " + filename);	
				}
			}
			
			if(obj.mipmap && town.bUseMipMapping)
			{
				if(nr==2)
					obj.textureImage2.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Linear);
				if(nr==3)
					obj.textureImage3.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Linear);
			}
			else
			{
				if(nr==2)
					obj.textureImage2.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
				if(nr==3)
					obj.textureImage3.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
			}
			
			if(obj.isRoomObject)
				obj.textureImage.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
			
			if(obj.editoraction.contains("foundation"))
				obj.textureImage.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
			
			if(town.bResearched)
				obj.iResearchCurrentWorkoutput=obj.iResearchTargetWorkoutput;
			
			//Animations werden auch wenn special object nicht speziell geladen
			int cols=0;
			if(nr==2)
				cols=obj.ATTR_T2COLS;
			if(nr==3)
				cols=obj.ATTR_T3COLS;

			int rows=0;
			if(nr==2)
				rows=obj.ATTR_T2ROWS;
			if(nr==3)
				rows=obj.ATTR_T3ROWS;
			
			if(cols==0)
			{
				if(nr==2)
				{
					obj.textureRegion2 = new TextureRegion[1];
					obj.textureRegion2[0] = new TextureRegion(obj.textureImage2);
				}
				
				if(nr==3)
				{
					obj.textureRegion3 = new TextureRegion[1];
					obj.textureRegion3[0] = new TextureRegion(obj.textureImage3);
				}
			}
			else
			{
				if(nr==2)
				{
			        TextureRegion[][] tmp = TextureRegion.split(obj.textureImage2, obj.textureImage2.getWidth()/cols, obj.textureImage2.getHeight()/rows);
			        obj.textureRegion2 = new TextureRegion[cols * rows];
			        int index = 0;
			        for (int i = 0; i < rows; i++) {
			            for (int j = 0; j < cols; j++) {
			            	obj.textureRegion2[index++] = tmp[i][j];
			            }
			        }
			        
			        obj.objectAnimation2 = new Animation(0.08f, obj.textureRegion2); 
				}
				
				if(nr==3)
				{
			        TextureRegion[][] tmp = TextureRegion.split(obj.textureImage3, obj.textureImage3.getWidth()/cols, obj.textureImage3.getHeight()/rows);
			        obj.textureRegion3 = new TextureRegion[cols * rows];
			        int index = 0;
			        for (int i = 0; i < rows; i++) {
			            for (int j = 0; j < cols; j++) {
			            	obj.textureRegion3[index++] = tmp[i][j];
			            }
			        }
			        
			        obj.objectAnimation3 = new Animation(0.08f, obj.textureRegion3); 
				}
			}
		}
	}
	
	public CWorldObject createWorldObject(String stype, int posx, int posy, CAddress adr, int width, int height)
	{
		CWorldObject wobj = createWorldObject(stype, posx, posy, adr);
		wobj.dynamicheight=height;
		wobj.dynamicwidth=width;
		wobj.height=height;
		wobj.width=width;
		
		if(town.fSizeFactor>0)
		{
			wobj.dynamicheight/=town.fSizeFactor;
			wobj.dynamicwidth/=town.fSizeFactor;
			wobj.height/=town.fSizeFactor;
			wobj.width/=town.fSizeFactor;
		}
		
		return wobj;
	}
		
	public CWorldObject createWorldObject(String stype, int posx, int posy, CAddress adr)
	{
	
		CWorldObject newWorldObject=null;
		CObject obj=null;
		List<CObject> list = listObjectres.stream().filter(item->item.editoraction.contains(stype)).collect(Collectors.toList());
		
		if(list!=null && list.size()>0)
		{
			int index = town.gameWorld.rand.nextInt(list.size());
			obj = list.get(index);
		}
		
		//Optional<CObject> obj = listObjectres.stream().filter(item->item.editoraction.contains(stype)).collect(Collectors.toList());
		
		if(obj!=null)
		{
			CObject obj1 = obj;
			obj1.pos_x=posx;
			obj1.pos_y=posy;
			newWorldObject = new CWorldObject(obj1, town, false);
			
			if(stype.contains("recyclingcenter_garbagebag"))
			{
				Random rand = town.gameWorld.rand;
				newWorldObject.dynamicheight=10+rand.nextInt(40);
				newWorldObject.dynamicwidth=10+rand.nextInt(40);
				newWorldObject.setDynamicSize(0);
				newWorldObject.color1 = new Color(rand.nextFloat(),rand.nextFloat(),rand.nextFloat(),1f);
				int mov=10;
				newWorldObject.setPosition(newWorldObject.pos_x()+rand.nextInt(mov)-rand.nextInt(mov),newWorldObject.pos_y()+rand.nextInt(mov)-rand.nextInt(mov));
			}
			
			//targetActionObject5.theobject.ATTR_FILL3 = baseWorldObject.thehuman.car.theobject.ATTR_FILL3;
			//newWorldObject.theobject.ATTR_FILL3 = 200;
			
			if(newWorldObject.theobject.editoraction.contains("bird"))
				town.gameWorld.worldBirds.add(newWorldObject);
			else if(newWorldObject.theobject.editoraction.contains("interior_light"))
			{
				town.gameWorld.worldCoverlights.add(newWorldObject);
				
				if(adr!=null)
				{
					newWorldObject.theaddress=adr;
					adr.addWorldObject(newWorldObject);
				}
			}
			else if(newWorldObject.theobject.editoraction.contains("supermarket_foodpallet"))
			{
				town.gameWorld.worldDrawSpecial2.add(newWorldObject);
				
				if(adr!=null)
				{
					newWorldObject.theaddress=adr;
					adr.addWorldObject(newWorldObject);
				}
			}
			else if(newWorldObject.theobject.editoraction.contains("traffic_car"))
			{
				town.gameWorld.worldCars.add(newWorldObject);
				
				if(adr!=null)
				{
					newWorldObject.theaddress=adr;
					adr.addWorldObject(newWorldObject);
				}
			}
			else if(newWorldObject.theobject.editoraction.contains("zombie_entrance"))
			{
				town.gameWorld.worldZombieEntrances.add(newWorldObject);
			}				
			else if(newWorldObject.theobject.editoraction.contains("illuminati_defensewarningsystem"))
			{
				town.gameWorld.worldDefenseWarning.add(newWorldObject);
				
				if(adr!=null)
				{
					newWorldObject.theaddress=adr;
					adr.addWorldObject(newWorldObject);
				}
			}		
			else if(newWorldObject.theobject.editoraction.contains("company_waterworks_groundwaterextractionsystem"))
			{
				town.gameWorld.worldWatersystems.add(newWorldObject);
				
				if(adr!=null)
				{
					newWorldObject.theaddress=adr;
					adr.addWorldObject(newWorldObject);
				}
			}				
			else if(newWorldObject.theobject.isDrawSpecial())
			{
				town.gameWorld.worldDrawSpecial.add(newWorldObject);
				
				if(adr!=null)
				{
					newWorldObject.theaddress=adr;
					adr.addWorldObject(newWorldObject);
				}
			}
			else if(newWorldObject.theobject.editoraction.contains("_carpet"))
			{
				town.gameWorld.worldCarpets.add(newWorldObject);
				
				if(adr!=null)
				{
					newWorldObject.theaddress=adr;
					adr.addWorldObject(newWorldObject);
				}
			}				
			else
			{
				town.gameWorld.worldObjects.add(newWorldObject);
				
				if(adr!=null)
				{
					newWorldObject.theaddress=adr;
					adr.addWorldObject(newWorldObject);
				}
			}
		}
		
		return newWorldObject;
	}
	
	Boolean isOnlyDrawSpecialObject(String eaction)
	{
		if(eaction.contains("company_college_spaceship"))
			return true;
		
		if(eaction.contains("company_waterworks_groundwaterextractionsystem"))
			return true;
		if(eaction.contains("illuminati_defensewall"))
			return true;
		if(eaction.contains("company_illuminati_officeworkingplace"))
			return true;
		if(eaction.contains("company_objectdesign_officeworkingplace_artist"))
			return true;
		if(eaction.contains("company_college_workingplace_researchlab"))
			return true;
		if(eaction.contains("outdoor_ground_water"))
			return true;
		if(eaction.contains("outdoor_tree") || eaction.contains("outdoor_plant") || eaction.contains("outdoor_flower"))
			return true;
		
		return false;
	}
	
	Boolean isSpecialObject(String eaction)
	{
		if(eaction.contains("traffic_car"))
			return true;
		
		if(eaction.contains("supermarket_buyin"))
			return true;
		
		if(eaction.contains("supermarket_foodpallet"))
			return true;
		
		if(eaction.contains("recyclingcenter_garbagebag"))
			return true;
		
		if(eaction.contains("fruitplate"))
			return true;
		
		if(eaction.contains("coffeepot1"))
			return true;
		
		if(eaction.contains("bedroom_wardrobe"))
			return true;
		
		if(eaction.contains("laundryroom_washingmachine"))
			return true;
		
		if(eaction.contains("laundryroom_dryer"))
			return true;
		
		if(eaction.contains("laundrybasket"))
			return true;

		if(eaction.contains("bathroom_towelcabinet"))
			return true;
		
		//if(eaction.contains("company_gasstation_gaspump"))
		//	return true;
		
		if(eaction.contains("company_fitnessstudio_legpress"))
			return true;
		
		if(eaction.contains("company_fitnessstudio_shoulderpress"))
			return true;
		
		if(eaction.contains("company_fitnessstudio_pecmachine"))
			return true;
		
		if(eaction.contains("company_playground_swing"))
			return true;

		if(eaction.contains("_billard"))
			return true;
		
		if(eaction.contains("kitchen_cupboard"))
			return true;

		if(eaction.contains("company_playground_seesaw"))
			return true;
		
		//if(eaction.contains("company_waterworks_groundwaterextractionsystem"))
		//	return true;
		
		if(eaction.contains("company_electricalworks_generator"))
			return true;

		if(eaction.contains("company_supermarket_shoppingcart"))
			return true;

		if(eaction.contains("fridge"))
			return true;

		if(eaction.contains("supermarket_checkout"))
			return true;
		
		if(eaction.contains("supermarket_shelf"))
			return true;
		
		if(eaction.contains("flora_"))
			return true;
		
		if(eaction.contains("recyclingcenter_garbagecontainer"))
			return true;
		
		if(eaction.contains("garbagecan"))
			return true;
		
		
		return false;
	}
	
	void loadSpecialTexture(CObject obj)
	{
		if(obj.editoraction.contains("flora_"))
		{
			obj.textureRegion = new TextureRegion[1];
			obj.textureRegion[0] = new TextureRegion(obj.textureImage);
		}

		else if(obj.editoraction.contains("company_supermarket_checkout"))
		{
			obj.textureRegion = new TextureRegion[1];
			obj.textureRegion[0] = new TextureRegion(obj.textureImage);
		}

		else if(obj.editoraction.contains("company_electricalworks_generator"))
		{
			obj.textureRegion = new TextureRegion[2];
			obj.textureRegion[0] = new TextureRegion(obj.textureImage);
			obj.textureRegion[1] = new TextureRegion(textures.get("generator_anim"));
		}
		
		else
		{
			obj.textureRegion = new TextureRegion[1];
			obj.textureRegion[0] = new TextureRegion(obj.textureImage);
		}
		
	}
	
	public void initObjectTypes(FileHandle file) {
		try {
			//Init Objecttypes
			BufferedReader reader = new BufferedReader(file.reader());
			listObjecttype.clear();
			String line = reader.readLine();
			while( line != null ) 
			{
				
				//[OBJECTTYPEID];[OBJECTTYPENAME];[textureFilename];[ACTIONCODE]
				if(line.trim().isEmpty() || line.contains("//")) //Leerzeilen und Kommentare überspringen
				{
					line = reader.readLine();
					continue;
				}
				
				String[] splitstring = line.split(";");
				CObjecttype objtype = new CObjecttype();
				objtype.objectTypeId = splitstring[0];
				
				objtype.baseObjectTypeId="";
				if(objtype.objectTypeId.length()>=6)
					objtype.baseObjectTypeId = objtype.objectTypeId.substring(0, objtype.objectTypeId.length()-3);
				
				objtype.baseObjectType=null;
				objtype.objectTypeName = splitstring[1];
				objtype.iconFileName = splitstring[2];
				objtype.actionCode = splitstring[3];
				
				Optional<CObjecttype> ot1 = listObjecttype.stream().filter(item->item.objectTypeId.equals(objtype.objectTypeId)).findFirst();
				if(ot1.isPresent())
				{
					listObjecttype.remove(ot1.get());
				}
				
//				if(!town.gameWorld.bConstructionMode && objtype.objectTypeId.equals("009022"))
//				{
//					line = reader.readLine();
//					continue;
//				}
				
				listObjecttype.add(objtype);
				
				objtype.textureIcon = new Texture(Gdx.files.internal(objtype.iconFileName));
				
				line = reader.readLine();
			}
		} 
		catch (IOException e) {
			CHelper.writeError(e.getMessage(), e.getStackTrace(), e);
		}
	}
}
















