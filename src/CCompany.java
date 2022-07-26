package com.mygdx.game;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.g2d.BitmapFont;


public class CCompany {

	public enum CompanyType 
	{	
		ELECTRICAL_WORKS, WATERWORKS, SUPERMARKET, SOFTWARE_DEVELOPMENT
		, SCHOOL, PUB, GRAVEYARD, COLLEGE, DISCO
		, FITNESS_STUDIO, RESEARCH_CENTER, RECYCLING_CENTER, PLAYGROUND
		, GAS_STATION, URBAN_CEMETERY, TOWN_HALL, ARCHITECTURE_BUREAU
		, DOCTORS_OFFICE, OBJECT_DESIGN, ILLUMINATI, CHURCH, PIZZASERVICE
		, CONSTRUCTION, PUBLIC_PARKING
		, NOT_DEFINED
	}
	
	public static enum WorkoutputType {
		DEFAULT, FINANCE, POPULATION, OTHER, INTELLIGENCE
	}
	
	public static enum ArchitectWorkType {
		RESIZE, MOVE, CLONE, PLANNING, CLONE_ROOM
	}
	
	public static Texture getCompanyIconByCompanyTypeString(CTown t, String str1)
	{
		String iconstring=str1;
		
		if(str1.toLowerCase().contains("fitness"))
			iconstring="icon_fitnessstudio.png";
		
		if(str1.toLowerCase().contains("pub"))
			iconstring="icon_pub.png";

		if(str1.toLowerCase().contains("break"))
			iconstring="icon_breakroom.png";

		if(str1.toLowerCase().contains("church"))
			iconstring="icon_church.png";

		if(str1.toLowerCase().contains("pizzaservice"))
			iconstring="icon_pizzaservice.png";
		
		
		//todo: hier ergänzen
		
		for(CObjecttype objtype : t.gameResourceConfig.listObjecttype)
			if(objtype.iconFileName.contains(iconstring))
				return objtype.textureIcon;
		
		return null;
	}
	
	public class CProject
	{
		int payment;
		int maxDays; //max time duration
		int requiredWorkoutput;
		int requiredCompanyReputation;
		int requiredWorkerIntelligence;
		int requiredWorkerEducation;
		
		//Status
		int daysLeft;
		int workOutputLeft;
		
		//List<CWorldObject> listWorker;
				
		public CProject(int payment, int maxDays, int requiredWorkoutput, int requiredCompanyReputation, int requiredWorkerIntelligence, int requiredWorkerEducation)
		{
			this.payment=payment;
			this.maxDays=maxDays;
			this.requiredWorkoutput=requiredWorkoutput;
			this.requiredCompanyReputation=requiredCompanyReputation;
			this.requiredWorkerIntelligence=requiredWorkerIntelligence;
			this.requiredWorkerEducation=requiredWorkerEducation;
			
			this.daysLeft=maxDays;
			this.workOutputLeft=requiredWorkoutput;
		}
	}
	
	public int companyId;
	public String companyname;
	public CompanyType companyType;
	public CAddress address_company;
	public int addressId;
	private Boolean active; //company is running
	private int workOutput;
	private int workOutput_finance;
	private int workOutput_population;
	private int workOutput_other;
	private int workOutput_intelligence;
	private int maxWorkOutput;
	
	public CTown town;
	
	public CCompany(CTown t, String name, CAddress adr, String editoractionstring)
	{
		town=t;
		
		init();
		
		companyname = name;
		address_company = adr;
		
		OptionalInt opt = town.gameWorld.worldCompanyList.stream().mapToInt(item->item.companyId).max();
		companyId=1;
		if(opt.isPresent())
			companyId = opt.getAsInt()+1;
		
		companyType=getCompanyTypeByEditorActionString(editoractionstring);
		
		if(companyType==CompanyType.ARCHITECTURE_BUREAU)
			workOutput=town.company_officeworkoutput_max;
		
		if(companyname.isEmpty())
			companyname = getCompanyTypeLabel();
	}
	
	public CCompany(int companyid, CTown t, String name, int adrid, String sctype, int workoutput)
	{
		town=t;
		
		init();
		
		companyname = name;
		addressId = adrid;

		workOutput=workoutput;
		companyId=companyid;
		companyType=getCompanyTypeByTypeString(sctype);
	}
	
	public void setAchievement()
	{
		if(companyType==CompanyType.ARCHITECTURE_BUREAU)
			town.setAchievement("company_architect");
		if(companyType==CompanyType.CHURCH)
			town.setAchievement("company_church");
		if(companyType==CompanyType.COLLEGE)
			town.setAchievement("company_college");
		if(companyType==CompanyType.CONSTRUCTION)
			town.setAchievement("company_construction");
		if(companyType==CompanyType.PUBLIC_PARKING)
			town.setAchievement("company_publicparking");
		if(companyType==CompanyType.DOCTORS_OFFICE)
			town.setAchievement("company_doctor");
		if(companyType==CompanyType.ELECTRICAL_WORKS)
			town.setAchievement("company_electricalworks");
		if(companyType==CompanyType.FITNESS_STUDIO)
			town.setAchievement("company_fitnessstudio");
		if(companyType==CompanyType.GAS_STATION)
			town.setAchievement("company_gasstation");
		if(companyType==CompanyType.GRAVEYARD)
			town.setAchievement("company_graveyard");
		if(companyType==CompanyType.ILLUMINATI)
			town.setAchievement("company_illuminati");
		if(companyType==CompanyType.OBJECT_DESIGN)
			town.setAchievement("company_objectdesign");
		if(companyType==CompanyType.PLAYGROUND)
			town.setAchievement("company_playground");
		if(companyType==CompanyType.PUB)
			town.setAchievement("company_pub");
		if(companyType==CompanyType.RECYCLING_CENTER)
			town.setAchievement("company_recyclingcenter");
		if(companyType==CompanyType.SCHOOL)
			town.setAchievement("company_school");
		if(companyType==CompanyType.SOFTWARE_DEVELOPMENT)
			town.setAchievement("company_software");
		if(companyType==CompanyType.SUPERMARKET)
			town.setAchievement("company_supermarket");
		if(companyType==CompanyType.TOWN_HALL)
			town.setAchievement("company_townhall");
		if(companyType==CompanyType.URBAN_CEMETERY)
			town.setAchievement("company_cemetery");
		if(companyType==CompanyType.WATERWORKS)
			town.setAchievement("company_waterworks");
	}
	
	public void init()
	{
		maxWorkOutput = town.company_officeworkoutput_max;
		workOutput = town.initial_company_officeworkoutput;
		
		workOutput_finance=0;
		workOutput_population=0;
		workOutput_other=0;
		workOutput_intelligence=0;
		
		active=true;
	}
			
	public ArrayList<CWorldObject> getWorkerList()
	{
		ArrayList<CWorldObject> list = new ArrayList<CWorldObject>();
		
		for(CWorldObject wobj : address_company.listWorldObjects)
		{
			if(wobj.isHuman())
				continue;
			
			if(wobj.worker!=null)
			{
				if(!list.contains(wobj.worker))
					list.add(wobj.worker);
			}

			if(wobj.worker2!=null)
			{
				if(!list.contains(wobj.worker2))
					list.add(wobj.worker2);
			}
		}
		
		return list;
	}
		
	public void cancelActions()
	{
		if(address_company.listWorldObjects.size()>0)
		{
			for(CWorldObject wobj : address_company.listWorldObjects)
			{
				if(wobj.isHuman())
					continue;
				
				wobj.cancelAction1();
			}
		}
	}
	
	public int getCompanyMaxWorkoutput()
	{
		int addwo=0;
		if(address_company!=null && address_company.listWorldObjects!=null)
		{
			try
			{
				Optional<CWorldObject> obj = address_company.listWorldObjects.stream().filter(item->item.theobject.editoraction.contains("company_anycompany_server") && item.isActiveByEnergyConsumption()).findFirst();
				if(obj.isPresent())
				{
					if(obj.get().theobject.editoraction.contains("company_anycompany_server1"))
						addwo+=500;
					if(obj.get().theobject.editoraction.contains("company_anycompany_server2"))
						addwo+=1000;
					if(obj.get().theobject.editoraction.contains("company_anycompany_server3"))
						addwo+=1500;
				}
			}
			catch(Exception e) {}
		}
		
		return maxWorkOutput + addwo;
	}
	
	public int companyHasWorkoutput()
	{
		//0: kein workoutput
		//1: default workoutput
		//2: special workoutput
		
		if (companyType == CompanyType.ELECTRICAL_WORKS
				|| companyType == CompanyType.SUPERMARKET
				|| companyType == CompanyType.WATERWORKS
				//|| companyType == CompanyType.FITNESS_STUDIO
				|| companyType == CompanyType.ARCHITECTURE_BUREAU
				|| companyType == CompanyType.RECYCLING_CENTER
				|| companyType == CompanyType.ILLUMINATI
			)
		{
			return 1;
		}
		
		if(companyType == CompanyType.TOWN_HALL)
			return 2;
		
		
		return 0;
	}
	
	public static int getArchitectCosts(CTown town, ArchitectWorkType type)
	{
		if(type==ArchitectWorkType.CLONE)
			return town.architectcosts_clone; //250;

		if(type==ArchitectWorkType.CLONE_ROOM)
			return town.architectcosts_clone; //250;
		
		if(type==ArchitectWorkType.RESIZE)
			return town.architectcosts_resize; //250;

		if(type==ArchitectWorkType.MOVE)
			return town.architectcosts_move; //250;
	
		if(type==ArchitectWorkType.PLANNING)
			return town.architectcosts_planning; //550;
		
		return 0;
	}
	
	public int getCompanyObjectsCount()
	{
		int count=0;
		for (CWorldObject cobj : address_company.listWorldObjects) 
		{
			if (!cobj.bDeleted && cobj.isCompanyObject()) 
				count++;
		}
	
		return count;
	}
	
	public static int getStatisticsCosts(WorkoutputType type)
	{
		if(type==WorkoutputType.FINANCE)
			return 150;
		
		if(type==WorkoutputType.POPULATION)
			return 150;
		
		if(type==WorkoutputType.OTHER)
			return 150;
		
		return 0;
	}
	
	public String getCompanyTypeLabel()
	{
		if(companyType==CompanyType.TOWN_HALL)
			return "Town Hall";
		if(companyType==CompanyType.DOCTORS_OFFICE)
			return "Doctor's Office";
		if(companyType==CompanyType.ARCHITECTURE_BUREAU)
			return "Architecture Bureau";
		if(companyType==CompanyType.URBAN_CEMETERY)
			return "Urban Cemetery";
		if(companyType==CompanyType.ELECTRICAL_WORKS)
			return "Electrical Works";
		if(companyType==CompanyType.SOFTWARE_DEVELOPMENT)
			return "Software Company";
		if(companyType==CompanyType.SUPERMARKET)
			return "Supermarket";
		if(companyType==CompanyType.WATERWORKS)
			return "Waterworks";
		if(companyType==CompanyType.COLLEGE)
			return "College";
		if(companyType==CompanyType.DISCO)
			return "Disco";
		if(companyType==CompanyType.RECYCLING_CENTER)
			return "Recycling Center";
		if(companyType==CompanyType.FITNESS_STUDIO)
			return "Fitness Studio";
		if(companyType==CompanyType.GRAVEYARD)
			return "Graveyard";
		if(companyType==CompanyType.PUB)
			return "Pub";
		if(companyType==CompanyType.RESEARCH_CENTER)
			return "Research Center";
		if(companyType==CompanyType.SCHOOL)
			return "School";
		if(companyType==CompanyType.GAS_STATION)
			return "Gas Station";
		if(companyType==CompanyType.PLAYGROUND)
			return "Playground";
		if(companyType==CompanyType.OBJECT_DESIGN)
			return "Object Design";
		if(companyType==CompanyType.ILLUMINATI)
			return "Illuminati";
		if(companyType==CompanyType.CHURCH)
			return "Church";
		if(companyType==CompanyType.PIZZASERVICE)
			return "Pizza Service";
		if(companyType==CompanyType.CONSTRUCTION)
			return "Construction";
		if(companyType==CompanyType.PUBLIC_PARKING)
			return "Public Parking";

		if(companyType==CompanyType.NOT_DEFINED)
			return "";
		
		return "";
	}
		
	public CWorldObject supermarket_getFilledShelf()
	{
		if(companyType==CompanyType.SUPERMARKET)
		{
			 Optional<CWorldObject> opt = address_company.listWorldObjects.stream().filter(item->item.theobject.editoraction.contains("shelf") && item.objectFilling>=10).findFirst();
			 if(opt.isPresent())
				 return opt.get();
		}
		
		return null;
	}
	
	public CWorldObject supermarket_getOpenCheckout()
	{
		if(companyType==CompanyType.SUPERMARKET)
		{
			CWorldObject checkout=null;
			for(CWorldObject obj : address_company.listWorldObjects)
			{
				//if(obj.bIsOccupied==false && obj.theobject.editoraction.contains("checkout"))
				if(obj.theobject.editoraction.contains("checkout")) //bisoccupied-> kasse muss zurückgesetzt werden auf false
				{
					if(obj.isWorkerActive() && obj.isActiveByEnergyConsumption())
					{
						if(checkout==null)
							checkout=obj;
						if(obj.isOccupiedByExtern!=null && checkout!=null)
							checkout=obj;
					}
				}
			}
			
			return checkout;
		}
		
		return null;
	}
	
	public CWorldObject[] getActive_doctorsOffice(int residentid)
	{
		//Ist Doctor's Office Active für Patientenbehandlung?
		
		Boolean bTreatmentChair=false;
		CWorldObject reception=null;
		CWorldObject waitingChair=null;
		CWorldObject waitingTable=null;
		CWorldObject[] wobjs = {null, null, null};
		
		for(CWorldObject wobj : address_company.listWorldObjects)
		{
			//Doctor hat Sprechstunde, Treatment chair is active
			if(wobj.theobject.editoraction.contains("company_doctorsoffice_treatment_chair"))
			{
				if(residentid>0)
				{
					if(wobj.worker!=null && wobj.worker.uniqueId==residentid) //nicht zum eigenen treatment chair als consumer gehen
						continue;
				}
				
				if(wobj.isWorkerActive() && wobj.isActiveByEnergyConsumption() && wobj.isActiveByWaterConsumption())
				{
					bTreatmentChair=true;
					continue;
				}
			}
			
			//Wartezimmer hat Reception, Reception ist besetzt
			if(wobj.theobject.editoraction.contains("company_doctorsoffice_reception_desk"))
			{
				if(residentid>0)
				{
					if(wobj.worker!=null && wobj.worker.uniqueId==residentid) //nicht zum eigenen receptiondesk als consumer gehen
						continue;
				}
				
				if(wobj.isWorkerActive() && wobj.isActiveByEnergyConsumption() && wobj.isActiveByWaterConsumption())
				{
					reception=wobj;
					continue;
				}
			}
			
			//Stuhl im Wartezimmer muss frei sein
			if(wobj.theobject.editoraction.contains("company_doctorsoffice_reception_chair"))
			{
				if(wobj.isOccupiedBy==null || wobj.isOccupiedBy.uniqueId==residentid)
					waitingChair=wobj;
			}
			
			//Optional: Tisch mit Zeitschriften
			if(wobj.theobject.editoraction.contains("company_doctorsoffice_reception_table"))
			{
				waitingTable=wobj;
			}
		}
		
		//Gdx.app.debug("", "" + bTreatmentChair + ", " + reception + ", " + waitingChair);
		
		if(bTreatmentChair && reception!=null && waitingChair!=null)
		{
			wobjs[0] = waitingChair;
			wobjs[1] = reception;
			wobjs[2] = waitingTable;
			
			return wobjs;
		}
		
		return null;
	}
	
	public Boolean isActive(int consumerid)
	{
		if(companyType==CompanyType.PUBLIC_PARKING)
		{
			for(CWorldObject wobj : address_company.listWorldObjects)
			{
				if(wobj.theobject.editoraction.contains("road_road_parkingspace") && wobj.isOccupiedBy==null)
					return true;
			}
			
			return false;
		}
		
		if(companyType==CompanyType.DOCTORS_OFFICE)
		{
			CWorldObject[] objArr = getActive_doctorsOffice(consumerid);
			
			if(objArr!=null)
				return true;
			
			return false;
		}
		
		if(companyType==CompanyType.URBAN_CEMETERY)
		{
			//Aktiv/Bereit für Beisetzungs-Aktion
			
			//	- Freies Grab muss existieren und genug onlinebyworkoutput haben
			//	- Hearse muss einsatzbereit sein: Fuel bzw Tanken ist möglich mit townmoney, onlineByWorkInput    
			//	- Rednerpult mit hinterlegtem Worker, onlineByWorkInput, Energy
			
			Boolean bGrave=false;
			Boolean bHearse=false;
			Boolean bRostrum=false;
			
			for(CWorldObject obj : address_company.listWorldObjects)
			{
				//Empty Grave
				if(obj.theobject.editoraction.contains("company_urbancemetery_graveempty"))
				{
					if(obj.onlineByWorkInput)
						bGrave=true;
				}
				
				//Hearse
				if(obj.theobject.editoraction.contains("company_urbancemetery_hearse_traffic_car"))
				{
					if(obj.onlineByWorkInput)
					{
						//if(obj.fuelValue)
						bHearse=true;
					}
				}
				
				//Rostrum			
				if(obj.theobject.editoraction.contains("company_urbancemetery_rostrum"))
				{
					if(obj.worker!=null && obj.isActiveByEnergyConsumption() && obj.onlineByWorkInput)
					{
						bRostrum=true;
					}
				}
			}
			
			if(bGrave && bHearse && bRostrum)
				return true;
						
			return false;
		}
				
		if(companyType==CompanyType.GAS_STATION)
		{ 
			//Gdx.app.debug("", "debug refuel 2, size: " + listWorldObjects.size());
			
			Boolean bGasPump=false;
			Boolean bCheckOut=false;
			
			for(CWorldObject obj : address_company.listWorldObjects)
			{
				//Gas Pump muss aktiv(workoutput) sein
				if(obj.theobject.editoraction.contains("company_gasstation_gaspump"))
				{
					if(obj.isActiveByEnergyConsumption() && obj.onlineByWorkInput)
					{
						bGasPump=true;
					}
				}
				
				//Checkout muss aktiv(energy) und mit worker besetzt sein				
				if(obj.theobject.editoraction.contains("company_gasstation_workingplace_cashpoint"))
				{
					if(consumerid>0)
					{
						if(obj.worker!=null && obj.worker.uniqueId==consumerid) //nicht zum eigenen checkout gehen
							continue;
					}
					
					if(obj.isWorkerActive() && obj.isActiveByEnergyConsumption() && obj.isActiveByWaterConsumption())
					{
						bCheckOut=true;
					}
				}
			}
			
			//Gdx.app.debug("", "bCheckOut: " + bCheckOut + ", bGasPump: " + bGasPump);
			
			if(bCheckOut && bGasPump)
				return true;
			
			return false;
		}
		
		if(companyType==CompanyType.PUB)
		{
			for(CWorldObject obj : address_company.listWorldObjects)
			{
				if(obj.theobject.editoraction.contains("_bar"))
				{
					if(obj.isWorkerActive() && obj.isActiveByEnergyConsumption() && obj.isActiveByWaterConsumption())
							return true;
				}
			}
			
			return false;
		}
		
		
		if(companyType==CompanyType.FITNESS_STUDIO)
		{
			for(CWorldObject obj : address_company.listWorldObjects)
			{
				if(obj.theobject.editoraction.contains("company_fitnessstudio_fitnessworkingplace") && obj.isWorkerActive() && obj.isActiveByEnergyConsumption())
					return true;
			}
			
			return false;
		}
		
		
		if(companyType==CompanyType.SUPERMARKET)
		{
			//	- wagen vorhanden 
			//	- kasse zur zeit besetzt
			//	- regal gefüllt
			
			Boolean bCheckout=false;
			Boolean bShelf=false;
			Boolean bCart=false;
			
			for(CWorldObject obj : address_company.listWorldObjects)
			{
				if(obj.theobject.editoraction.contains("checkout"))
				{
					if(obj.isWorkerActive() && obj.isActiveByEnergyConsumption())
						bCheckout=true;
				}
				
				if(obj.theobject.editoraction.contains("shelf") && obj.objectFilling>=10)
					bShelf=true;
				
				if(obj.theobject.editoraction.contains("cart"))
					bCart=true;
			}
			
			//if(baseWorldObject.thehuman.getName().contains("Anthony Mitchell"))
				//Gdx.app.debug("", "bCheckout: "+ bCheckout + ", bShelf: " + bShelf + ", bCart: " + bCart) ;
			 
			if(bCheckout && bShelf && bCart)
				return true;
			
			return false;
		}
		
		if(companyType==CompanyType.ARCHITECTURE_BUREAU)
		{
			for(CWorldObject obj1 : address_company.listWorldObjects)
			{
				if(obj1.theobject.editoraction.contains("architecturebureau_officeworkingplace") && obj1.bObjectIsReady && obj1.isActiveByEnergyConsumption())
				{
					return true;
				}
			}
			
			return false;
		}

		
		return true;
	}
	
	public CompanyType getCompanyTypeByTypeString(String str)
	{
		str=str.toUpperCase();
		
		if(str.contains("URBAN_CEMETERY"))
			return CompanyType.URBAN_CEMETERY;
		if(str.contains("URBANCEMETERY"))
			return CompanyType.URBAN_CEMETERY;

		if(str.contains("DOCTORS_OFFICE"))
			return CompanyType.DOCTORS_OFFICE;
		if(str.contains("DOCTORSOFFICE"))
			return CompanyType.DOCTORS_OFFICE;
		
		if(str.contains("TOWN_HALL"))
			return CompanyType.TOWN_HALL;
		if(str.contains("TOWNHALL"))
			return CompanyType.TOWN_HALL;

		if(str.contains("ARCHITECTURE_BUREAU"))
			return CompanyType.ARCHITECTURE_BUREAU;
		if(str.contains("ARCHITECTUREBUREAU"))
			return CompanyType.ARCHITECTURE_BUREAU;

		if(str.contains("OBJECT_DESIGN") || str.contains("OBJECTDESIGN"))
			return CompanyType.OBJECT_DESIGN;
		
		if(str.contains("ILLUMINATI"))
			return CompanyType.ILLUMINATI;
		
		if(str.contains("CHURCH"))
			return CompanyType.CHURCH;

		if(str.contains("PIZZA_SERVICE") || str.contains("PIZZASERVICE"))
			return CompanyType.PIZZASERVICE;
		
		if(str.contains("ELECTRICAL_WORKS"))
			return CompanyType.ELECTRICAL_WORKS;
		
		if(str.contains("CONSTRUCTION"))
			return CompanyType.CONSTRUCTION;
		
		if(str.contains("PUBLICPARKING"))
			return CompanyType.PUBLIC_PARKING;
		if(str.contains("PUBLIC_PARKING"))
			return CompanyType.PUBLIC_PARKING;
				
		if(str.contains("WATERWORKS"))
			return CompanyType.WATERWORKS;

		if(str.contains("SUPERMARKET"))
			return CompanyType.SUPERMARKET;

		if(str.contains("SOFTWARE_DEVELOPMENT"))
			return CompanyType.SOFTWARE_DEVELOPMENT;
		
		if(str.contains("SCHOOL"))
			return CompanyType.SCHOOL;
		

		if(str.contains("GRAVEYARD"))
			return CompanyType.GRAVEYARD;
		
		if(str.contains("PLAYGROUND"))
			return CompanyType.PLAYGROUND;

		if(str.contains("GAS_STATION"))
			return CompanyType.GAS_STATION;
		if(str.contains("GASSTATION"))
			return CompanyType.GAS_STATION;
		
		if(str.contains("COLLEGE"))
			return CompanyType.COLLEGE;
		if(str.contains("DISCO"))
			return CompanyType.DISCO;
		if(str.contains("FITNESS_STUDIO"))
			return CompanyType.FITNESS_STUDIO;
		if(str.contains("FITNESSSTUDIO"))
			return CompanyType.FITNESS_STUDIO;
		
		if(str.contains("RESEARCH_CENTER"))
			return CompanyType.RESEARCH_CENTER;
		
		if(str.contains("RECYCLING_CENTER"))
			return CompanyType.RECYCLING_CENTER;
		
		if(str.contains("PUB"))
			return CompanyType.PUB;
		
		return CompanyType.NOT_DEFINED;
	}	
	
	public static CompanyType getCompanyTypeByEditorActionString(String str)
	{
		str=str.toLowerCase();
		
		if(str.contains("urbancemetery") || str.contains("urban_cemetery"))
			return CompanyType.URBAN_CEMETERY;
		
		if(str.contains("townhall") || str.contains("town_hall"))
			return CompanyType.TOWN_HALL;
		
		if(str.contains("architecture_bureau") || str.contains("architecturebureau"))
			return CompanyType.ARCHITECTURE_BUREAU;

		if(str.contains("object_design") || str.contains("objectdesign"))
			return CompanyType.OBJECT_DESIGN;

		if(str.contains("pizza_service") || str.contains("pizzaservice"))
			return CompanyType.PIZZASERVICE;

		if(str.contains("illuminati"))
			return CompanyType.ILLUMINATI;
		
		if(str.contains("construction"))
			return CompanyType.CONSTRUCTION;		

		if(str.toLowerCase().contains("publicparking"))
			return CompanyType.PUBLIC_PARKING;		
		if(str.toLowerCase().contains("public_parking"))
			return CompanyType.PUBLIC_PARKING;		
		
		if(str.contains("church"))
			return CompanyType.CHURCH;
		
		if(str.contains("electrical"))
			return CompanyType.ELECTRICAL_WORKS;
		
		if(str.contains("waterworks"))
			return CompanyType.WATERWORKS;
		
		if(str.contains("supermarket"))
			return CompanyType.SUPERMARKET;
		
		if(str.contains("software"))
			return CompanyType.SOFTWARE_DEVELOPMENT;
		
		if(str.contains("school"))
			return CompanyType.SCHOOL;

		if(str.contains("graveyard"))
			return CompanyType.GRAVEYARD;
		
		if(str.contains("playground"))
			return CompanyType.PLAYGROUND;
		
		if(str.toUpperCase().contains("COLLEGE"))
			return CompanyType.COLLEGE;
		
		if(str.toUpperCase().contains("DOCTORS_OFFICE") || str.toUpperCase().contains("DOCTORSOFFICE"))
			return CompanyType.DOCTORS_OFFICE;
		
		if(str.toUpperCase().contains("DISCO"))
			return CompanyType.DISCO;
		
		if(str.toUpperCase().contains("FITNESS_STUDIO") || str.toUpperCase().contains("FITNESSSTUDIO"))
			return CompanyType.FITNESS_STUDIO;
		
		if(str.toUpperCase().contains("RESEARCH_CENTER"))
			return CompanyType.RESEARCH_CENTER;
		
		if(str.toUpperCase().contains("RECYCLING_CENTER") || str.toUpperCase().contains("RECYCLING"))
			return CompanyType.RECYCLING_CENTER;
		
		if(str.toUpperCase().contains("GAS_STATION") || str.toUpperCase().contains("GASSTATION"))
			return CompanyType.GAS_STATION;

		if(str.contains("pub"))
			return CompanyType.PUB;

		
		return CompanyType.NOT_DEFINED;
	}
	
	public String getCompanyTypeString()
	{
		return companyType.name();
	}
	
	public void addWorkOutput(int wo, WorkoutputType type)
	{
		if(type==WorkoutputType.DEFAULT)
		{
			workOutput+=wo;
			
			if(workOutput>getCompanyMaxWorkoutput())
				workOutput=getCompanyMaxWorkoutput();
			
			if(workOutput<0)
				workOutput=0;
		}
		
		if(type==WorkoutputType.FINANCE)
		{
			workOutput_finance+=wo;
			
			if(workOutput_finance>getCompanyMaxWorkoutput())
				workOutput_finance=getCompanyMaxWorkoutput();
			
			if(workOutput_finance<0)
				workOutput_finance=0;
		}
		
		if(type==WorkoutputType.POPULATION)
		{
			workOutput_population+=wo;
			
			if(workOutput_population>getCompanyMaxWorkoutput())
				workOutput_population=getCompanyMaxWorkoutput();
			
			if(workOutput_population<0)
				workOutput_population=0;
		}
		
		if(type==WorkoutputType.OTHER)
		{
			workOutput_other+=wo;
			
			if(workOutput_other>getCompanyMaxWorkoutput())
				workOutput_other=getCompanyMaxWorkoutput();
			
			if(workOutput_other<0)
				workOutput_other=0;
		}
		
		if(type==WorkoutputType.INTELLIGENCE)
		{
			workOutput_intelligence+=wo;
			
			if(workOutput_intelligence>getCompanyMaxWorkoutput())
				workOutput_intelligence=getCompanyMaxWorkoutput();
			
			if(workOutput_intelligence<0)
				workOutput_intelligence=0;
		}		
	}
	
	public int consumeWorkOutput(int wo, WorkoutputType type)
	{
		if(type==WorkoutputType.DEFAULT)
		{
			if(workOutput>=wo)
			{
				workOutput-=wo;
				return wo;
			}
			else
			{
				wo=workOutput;
				workOutput=0;
				return wo;
			}
		}
		
		if(type==WorkoutputType.FINANCE)
		{
			if(workOutput_finance>=wo)
			{
				workOutput_finance-=wo;
				return wo;
			}
			else
			{
				wo=workOutput_finance;
				workOutput_finance=0;
				return wo;
			}
		}		
		
		if(type==WorkoutputType.POPULATION)
		{
			if(workOutput_population>=wo)
			{
				workOutput_population-=wo;
				return wo;
			}
			else
			{
				wo=workOutput_population;
				workOutput_population=0;
				return wo;
			}
		}		
		
		if(type==WorkoutputType.OTHER)
		{
			if(workOutput_other>=wo)
			{
				workOutput_other-=wo;
				return wo;
			}
			else
			{
				wo=workOutput_other;
				workOutput_other=0;
				return wo;
			}
		}
		
		if(type==WorkoutputType.INTELLIGENCE)
		{
			if(workOutput_intelligence>=wo)
			{
				workOutput_intelligence-=wo;
				return wo;
			}
			else
			{
				wo=workOutput_intelligence;
				workOutput_intelligence=0;
				return wo;
			}
		}
		
		return 0;
	}
	
	public Boolean consumeMinWorkOutput(int wo, WorkoutputType type)
	{
		if(type==WorkoutputType.DEFAULT)
		{
			if(workOutput>=wo)
			{
				workOutput-=wo;
				return true;
			}
			else 
			{
				return false;
			}
		}
		
		if(type==WorkoutputType.FINANCE)
		{
			if(workOutput_finance>=wo)
			{
				workOutput_finance-=wo;
				return true;
			}
			else 
			{
				return false;
			}
		}

		if(type==WorkoutputType.POPULATION)
		{
			if(workOutput_population>=wo)
			{
				workOutput_population-=wo;
				return true;
			}
			else 
			{
				return false;
			}
		}
		
		if(type==WorkoutputType.OTHER)
		{
			if(workOutput_other>=wo)
			{
				workOutput_other-=wo;
				return true;
			}
			else 
			{
				return false;
			}
		}
		
		if(type==WorkoutputType.INTELLIGENCE)
		{
			if(workOutput_intelligence>=wo)
			{
				workOutput_intelligence-=wo;
				return true;
			}
			else 
			{
				return false;
			}
		}
		
		return false;
	}	
	
	public int getWorkOutput(WorkoutputType type)
	{
		if(type==WorkoutputType.FINANCE)
			return workOutput_finance;
		
		if(type==WorkoutputType.POPULATION)
			return workOutput_population;
		
		if(type==WorkoutputType.OTHER)
			return workOutput_other;
		
		if(type==WorkoutputType.INTELLIGENCE)
			return workOutput_intelligence;
		
		return workOutput;
	}
	
	public static CWorldObject getArchitectByMinWorkoutput(CTown t, int workoutput)
	{
		for(CCompany comp : t.gameWorld.worldCompanyList)
		{
			if(comp.companyType == CompanyType.ARCHITECTURE_BUREAU)
			{
				for(CWorldObject wobj : comp.address_company.listWorldObjects)
				{
					if(wobj.theobject.editoraction.contains("architecturebureau_officeworkingplace") && wobj.bObjectIsReady && wobj.isActiveByEnergyConsumption() && wobj.belongsToCompany.getWorkOutput(WorkoutputType.DEFAULT)>=workoutput)
						return wobj;
				}
			}
		}
		
		return null;
	}
	
	public static CCompany getNextActiveCompany(CompanyType ctype, CTown t, int x, int y, int consumerid, int maxdist)
	{
		CCompany tempCompany=null;
		int tempDist=maxdist; // t.mapsize;
		for(CCompany comp : t.gameWorld.worldCompanyList)
		{
			if(comp.companyType == ctype)
			{
				if(comp.isActive(consumerid))
				{
					int dist1 = CHelper.getEuclidianDistance(x, y, (int)comp.address_company.sx, (int)comp.address_company.sy);
					if(dist1<tempDist)
					{
						tempDist=dist1;
						tempCompany=comp;
					}
				}
			}
		}
		
		return tempCompany;
	}
	
//	public static CCompany getActiveCompanyByDistance(CompanyType ctype, List<CCompany> clist, int x, int y, int consumerid)
//	{
//		CCompany company=null;
//		int distance=0;
//		for(CCompany comp : clist)
//		{
//			if(comp.companyType!=ctype)
//				continue;
//			
//			if(!comp.isActive(consumerid))
//				continue;
//			
//			if(company!=null)
//			{
//				int distanceTemp = CHelper.getEuclidianDistance(x, y, (int)comp.address.sx, (int)comp.address.sy);
//				if(distanceTemp<distance)
//				{
//					distance=distanceTemp;
//					company=comp;
//				}
//			}
//			else 
//			{
//				distance = CHelper.getEuclidianDistance(x, y, (int)comp.address.sx, (int)comp.address.sy);
//				company=comp;
//			}
//		}
//		
//		return company;
//	}
	
}
