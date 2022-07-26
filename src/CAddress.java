package com.mygdx.game;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Optional;
import java.util.OptionalInt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntArray;
import com.mygdx.game.CCompany.CompanyType;
import com.mygdx.game.CHelper.IntersectionMode;

public class CAddress {
	public static enum AddressSide {
		LEFT, RIGHT, TOP, BOTTOM, NONE
	}
	
	public static enum AddressOverlap {
		DEFAULT, RESIZE
	}
	
	public int pathmapsize;
	public CWorldObject[] pathmap;
	public CAStar astar = null;	
	
	public Boolean isCloning;
	
	int addressId;
	CTown town;
	public float sx;
	public float sy;
	public float ex;
	public float ey;
	
	public float countFlowers; //zeug das sich auf adr und in umgebung befindet
	public float countTrees; //zeug das sich auf adr und in umgebung befindet
	public float countFlowersTemp; //zeug das sich auf adr und in umgebung befindet
	public float countTreesTemp; //zeug das sich auf adr und in umgebung befindet
	
	public int countFlora; //trees,m flowes, plants die sich direkt auf adr befinden
	public int countFloraTemp; //trees,m flowes, plants die sich direkt auf adr befinden
	
	ArrayList<CWorldObject> listWorldObjects;
	ArrayList<CWorldObject> listWorldObjects_Floors;
	ArrayList<CWorldObject> listWorldObjects_Ground;
	
	ArrayList<CWorldObject> tempList_GreenTown;
	ArrayList<CWorldObject> list_GreenTown;
	
	String addressName;
	String addressType; //residential, public
	
	private void init()
	{
		isCloning=false;
		listWorldObjects=new ArrayList<CWorldObject>();
		listWorldObjects_Floors=new ArrayList<CWorldObject>();
		listWorldObjects_Ground=new ArrayList<CWorldObject>();
		tempList_GreenTown=new ArrayList<CWorldObject>();
		list_GreenTown=new ArrayList<CWorldObject>();
		
		countFlowers=0;
		countTrees=0;
		countFlowersTemp=0;
		countTreesTemp=0;
		
		countFlora=0;
		countFloraTemp=0;
	}
	
	public CAddress(float x1, float y1, float x2, float y2, String name, CTown t, String stype)
	{
		town=t;
		init();
		addressType=stype;
		sx=x1;
		sy=y1;
		ex=x2;
		ey=y2;
		addressName=name;
		
		initAStar();
		
		addressId=getNewAddressId(t);
	}
	
	public CAddress(float x1, float y1, float x2, float y2, String name, CTown t, int adrId, String stype)
	{
		town=t;
		init();
		
		sx=x1;
		sy=y1;
		ex=x2;
		ey=y2;
		addressName=name;
		addressId=adrId;
		addressType=stype;
		
		initAStar();
		
		if(addressType.contains("residential") && addressType.contains("public"))
		{
			//..
		}
		else
		{
			if(addressType.contains("residential"))
				addressType="residential";
			else 
				addressType="public";
		}
	}
	
	private CAddress()
	{
		init();
	}	
	
	public void initAStar()
	{
		if(1==1)
			return;
		
		pathmapsize = (int) (Math.sqrt(getSize())/town.floorrastersize);
		
		//Gdx.app.debug("", "" + getSize());
		//Gdx.app.debug("", "pathmapsize: " + pathmapsize + ", getWidth()/town.floorrastersize: " + getWidth()/town.floorrastersize + ", getHeight()/town.floorrastersize: " + getHeight()/town.floorrastersize);
		
		//pathmap = new CWorldObject[pathmapsize*pathmapsize];
		pathmap = new CWorldObject[pathmapsize*pathmapsize];
		
     	//astar = new CAStar(getWidth()/town.floorrastersize, getHeight()/town.floorrastersize, town.gameWorld) 
     	astar = new CAStar(getWidth()/town.floorrastersize, getHeight()/town.floorrastersize, town)
    	{
    		protected CWorldObject isValid (int x, int y)
    		{
    			try
    			{
    				//Gdx.app.debug("", ""+(int)(x-sx + ((y-sy) * pathmapsize)));
    				return pathmap[(int)(x-sx + ((y-sy) * pathmapsize))];
    			}
    			catch(Exception ex)
    			{
    				//..
    			}
    			
    			return null;
    		}
    	};
    	
    	astar.isAdr=true;
	}
		
	public CWorldObject getFloorByPoint(int x, int y)
	{
		for(CWorldObject floor : listWorldObjects_Floors)
		{
			if(floor.testpoint(x, y, IntersectionMode.COLLISION))
				return floor;
		}
		
		return null;
	}
	
	public void removeWorldObject(CWorldObject wobj)
	{
		try
		{
			if(wobj.theobject.editoraction.contains("floor"))
			{
				listWorldObjects_Floors.removeIf(item->item.uniqueId==wobj.uniqueId);
			}
			else if(wobj.theobject.isGroundObject || wobj.theobject.isGroundBaseObject)
			{
				listWorldObjects_Ground.removeIf(item->item.uniqueId==wobj.uniqueId);
			}
			else
			{
				listWorldObjects.removeIf(item->item.uniqueId==wobj.uniqueId);
			}
		}
		catch(Exception ex)
		{
			listWorldObjects.removeIf(item->item.uniqueId==wobj.uniqueId);
			listWorldObjects_Floors.removeIf(item->item.uniqueId==wobj.uniqueId);
			listWorldObjects_Ground.removeIf(item->item.uniqueId==wobj.uniqueId);
		}
	}
	
	public int getMinDistanceToAddress(int x, int y)
	{
		int dist1 = CHelper.getEuclidianDistance(x, y, (int)sx, (int)sy);
		int dist2 = CHelper.getEuclidianDistance(x, y, (int)ex, (int)sy);
		int dist3 = CHelper.getEuclidianDistance(x, y, (int)sx, (int)ey);
		int dist4 = CHelper.getEuclidianDistance(x, y, (int)ex, (int)ey);
		int dist5 = CHelper.getEuclidianDistance(x, y, Math.round(sx+(ex-sx)/2), Math.round(sy+(ey-sy)/2));
		int dist6 = CHelper.getEuclidianDistance(x, y, Math.round(sx+(ex-sx)/2), (int)sy);
		int dist7 = CHelper.getEuclidianDistance(x, y, Math.round(sx+(ex-sx)/2), (int)ey);
		int dist8 = CHelper.getEuclidianDistance(x, y, (int)sx, Math.round(ey+(ey-sy)/2));
		int dist9 = CHelper.getEuclidianDistance(x, y, (int)ex, Math.round(ey+(ey-sy)/2));
		int dist=dist1;

		if(dist2<dist)
			dist=dist2;
		if(dist3<dist)
			dist=dist3;
		if(dist4<dist)
			dist=dist4;
		if(dist5<dist)
			dist=dist5;
		if(dist6<dist)
			dist=dist6;
		if(dist7<dist)
			dist=dist7;
		if(dist8<dist)
			dist=dist8;
		if(dist9<dist)
			dist=dist9;
		
		return dist;
	}
	
	public void addWorldObject(CWorldObject wobj)
	{
		if(town.bNoRealEstate) //Performance
		{
			if(wobj.theobject.editoraction.contains("road_road_road"))
				return;
			if(wobj.theobject.editoraction.contains("road_road_footpath"))
				return;
			if(wobj.theobject.editoraction.contains("defensewall"))
				return;
			
			if(wobj.theobject.editoraction.contains("outdoor_tree"))
				return;
			if(wobj.theobject.editoraction.contains("outdoor_plant"))
				return;
			if(wobj.theobject.editoraction.contains("outdoor_ground"))
				return;
		}
		
		if(wobj.theobject.editoraction.contains("floor")) {
				//listWorldObjects_Floors.removeIf(item->item.uniqueId==wobj.uniqueId);
				listWorldObjects_Floors.add(wobj);
		}
		else if(wobj.theobject.isGroundObject || wobj.theobject.isGroundBaseObject)	{
				//listWorldObjects_Ground.removeIf(item->item.uniqueId==wobj.uniqueId);
				listWorldObjects_Ground.add(wobj);
		}
		else
		{
			
			//int c1 = (int) listWorldObjects.stream().filter(item->item.uniqueId==wobj.uniqueId).count();
			//if(c1>0) {
			//	Gdx.app.debug(""+wobj.theobject.objectName, "count: "+c1 + ", id: " + wobj.uniqueId);
			//}
			
			if(!listWorldObjects.contains(wobj))
			{
				listWorldObjects.add(wobj);
			}
			
			
			//listWorldObjects.removeIf(item->item.uniqueId==wobj.uniqueId);
			//listWorldObjects.add(wobj);

			/*
			Optional<CWorldObject> opt = listWorldObjects.stream().filter(item->item.uniqueId==wobj.uniqueId).findFirst();
			if(!opt.isPresent())
			//if(!listWorldObjects.contains(wobj))
			{
				listWorldObjects.add(wobj);
			}
			else {
				listWorldObjects.removeIf(item->item.uniqueId==wobj.uniqueId);
				listWorldObjects.add(wobj);
			}
			*/
		}
	}
	
	public int randomX()
	{
		return (int) (sx + town.gameWorld.rand.nextInt(Math.round(ex-sx)));
	}
	
	public int randomY()
	{
		return (int) (sy + town.gameWorld.rand.nextInt(Math.round(ey-sy)));
	}
	
	public void cancelActions()
	{
		if(listWorldObjects.size()>0)
		{
			for(CWorldObject wobj : listWorldObjects)
			{
				//Residential
				if(addressType.contains("residential"))
				{
					if(wobj.isHuman())
						wobj.cancelAction1();
				}
				
				//Public Commercial
				if(addressType.contains("public"))
				{
					if(wobj.belongsToCompany!=null && wobj.belongsToCompany.address_company!=null && wobj.belongsToCompany.address_company.addressId==addressId)
					{
						if(wobj.worker!=null && wobj.worker.activeAction!=null && wobj.worker.activeAction.targetActionObject!=null && wobj.worker.activeAction.targetActionObject.uniqueId==wobj.uniqueId)
						{
							wobj.worker.cancelAction1();
						}
						if(wobj.worker2!=null && wobj.worker2.activeAction!=null && wobj.worker2.activeAction.targetActionObject!=null && wobj.worker2.activeAction.targetActionObject.uniqueId==wobj.uniqueId)
						{
							wobj.worker2.cancelAction1();
						}
					}
				}
			}
		}
	}
	
	public CWorldObject getFreeParkingPlaceOrGarage(CWorldObject car1)
	{
		//Wenn Ziel nicht Homeaddress ist sondern andere residential adress: parke nicht auf fremdem parkplatz
		
		if(car1==null || car1.owner==null || car1.owner.theaddress==null)
			return null;
		
		if(car1.owner.theaddress.addressId!=addressId) {		
			Optional<CWorldObject> opt = listWorldObjects.stream().filter(item->(item.theobject.editoraction.contains("_parkingspace") || 
					item.theobject.editoraction.contains("company_gasstation_gaspump")
					) && (item.isOccupiedBy==null ||item.isOccupiedBy.uniqueId==car1.uniqueId)).findFirst();
			
			if(opt.isPresent()) {
				return opt.get();
			}
		}
		else {
			Optional<CWorldObject> opt = listWorldObjects.stream().filter(item->(item.theobject.editoraction.contains("_parkingspace") || 
					item.theobject.editoraction.contains("_garage") || 
					item.theobject.editoraction.contains("company_gasstation_gaspump")
					) && (item.isOccupiedBy==null ||item.isOccupiedBy.uniqueId==car1.uniqueId)).findFirst();
			
			if(opt.isPresent()) {
				return opt.get();
			}
		}
		
		CCompany comp = CCompany.getNextActiveCompany(CompanyType.PUBLIC_PARKING, town, Math.round(sx+(ex-sx)/2), Math.round(sy+(ey-sy)/2), 0, 2000);
		
		if(comp!=null) {
			for(CWorldObject wobj : comp.address_company.listWorldObjects) {
				if(wobj.theobject.editoraction.contains("road_road_parkingspace") && wobj.isOccupiedBy==null && wobj.bObjectIsReady) {
					return wobj;
				}
			}
		}
		
		return null;
	}
	
	public CCompany getCompany()
	{
		for(CWorldObject wobj : listWorldObjects)
		{
			if(wobj.belongsToCompany!=null)
				return wobj.belongsToCompany;
		}
		
		for(CWorldObject wobj : listWorldObjects_Floors)
		{
			if(wobj.belongsToCompany!=null)
				return wobj.belongsToCompany;
		}
		
		return null;
	}
	
	public ArrayList<String> allObjectsResearchedOrUnlocked()
	{
		ArrayList<String> listNotResearched = new ArrayList<String>();
		
		for(int i=0;i<listWorldObjects.size();i++)
		{
			if(listWorldObjects.get(i).isHuman())
				continue;
						
			if(listWorldObjects.get(i).theobject.editoraction.contains("company")) {
				String typeid = listWorldObjects.get(i).theobject.objectId.substring(0, 6);
				if(!town.gameGui.unlockedCompanyTypes.contains(typeid)) {
					listNotResearched.add(listWorldObjects.get(i).theobject.objectName);
					continue;
				}
			}
			
			if(!listWorldObjects.get(i).theobject.isResearched())
			{
				if(!listNotResearched.contains(listWorldObjects.get(i).theobject.objectName))
					listNotResearched.add(listWorldObjects.get(i).theobject.objectName);
				//return false;
			}
			
			if(listWorldObjects.get(i).theobject.editoraction.contains("company_waterworks_groundwaterextractionsystem")) {
				if(listWorldObjects.get(i).width>listWorldObjects.get(i).theobject.getMaxSize(0))
					listNotResearched.add(listWorldObjects.get(i).theobject.objectName + " Resize Function");
			}
			
		}
		
		for(int i=0;i<listWorldObjects_Floors.size();i++)
		{
			if(listWorldObjects_Floors.get(i).theobject.editoraction.contains("company")) {
				String typeid = listWorldObjects_Floors.get(i).theobject.objectId.substring(0, 6);
				if(!town.gameGui.unlockedCompanyTypes.contains(typeid)) {
					listNotResearched.add(listWorldObjects_Floors.get(i).theobject.objectName);
					continue;
				}
			}

			if(!listWorldObjects_Floors.get(i).theobject.isResearched())
				if(!listNotResearched.contains(listWorldObjects_Floors.get(i).theobject.objectName))
					listNotResearched.add(listWorldObjects_Floors.get(i).theobject.objectName);
				//return false;
		}

		for(int i=0;i<listWorldObjects_Ground.size();i++)
		{
			if(listWorldObjects_Ground.get(i).theobject.editoraction.contains("company")) {
				String typeid = listWorldObjects_Ground.get(i).theobject.objectId.substring(0, 6);
				if(!town.gameGui.unlockedCompanyTypes.contains(typeid)) {
					listNotResearched.add(listWorldObjects_Ground.get(i).theobject.objectName);
					continue;
				}
			}
			
			if(!listWorldObjects_Ground.get(i).theobject.isResearched())
				if(!listNotResearched.contains(listWorldObjects_Ground.get(i).theobject.objectName))
					listNotResearched.add(listWorldObjects_Ground.get(i).theobject.objectName);
				//return false;
		}
		
		return listNotResearched;
		//return true;
	}
	
	public static int getNewAddressId(CTown town)
	{
		OptionalInt opt = town.gameWorld.worldAddressList.stream().mapToInt(item->item.addressId).max();
		int id=1;
		if(opt.isPresent())
			id = opt.getAsInt()+1;
		
		return id;
	}
	
	public void dispose()
	{
		if(listWorldObjects!=null && listWorldObjects.size()>0)
			for(CWorldObject wobj : listWorldObjects)
			{
				if(wobj.isHuman())
				{
					wobj.theaddress=null;
					wobj.theaddressid=0;
					wobj.thehuman.taskobjects.clear();
					wobj.cancelAction1();
				}
				else
					wobj.dispose();
			}
		
		if(listWorldObjects_Floors!=null && listWorldObjects_Floors.size()>0)
			for(CWorldObject wobj : listWorldObjects_Floors)
				wobj.dispose();

		if(listWorldObjects_Ground!=null && listWorldObjects_Ground.size()>0)
			for(CWorldObject wobj : listWorldObjects_Ground)
				wobj.dispose();
	}
	
	public CAddress clone()
	{
		CAddress adr = new CAddress();
		
		adr.addressId=getNewAddressId(town);
		adr.town = town;
		adr.sx=sx;
		adr.sy=sy;
		adr.ex=ex;
		adr.ey=ey;
		
		adr.addressName = ""; //"todo: duplicate name dlg";
		adr.addressType = addressType; //residential, public
		
		int icount=0;
		for(CWorldObject wobj : listWorldObjects)
		{
			if(wobj.isHuman())
				continue;
			
			CWorldObject clone = wobj.clone();
			clone.theaddress=adr;
			clone.theaddressid=adr.addressId;
			clone.uniqueId+=icount;
			icount++; //da das objekt nicht in der worldobjectliste ist bekommen alle die gleiche id
			
			adr.listWorldObjects.add(clone);
		}
		
		for(CWorldObject wobj : listWorldObjects_Floors)
		{
			CWorldObject clone = wobj.clone();
			clone.theaddress=adr;
			clone.theaddressid=adr.addressId;
			clone.uniqueId+=icount;
			icount++; //da das objekt nicht in der worldobjectliste ist kommt bekommen alle die gleiche id
			
			adr.listWorldObjects_Floors.add(clone);
		}
		
		for(CWorldObject wobj : listWorldObjects_Ground)
		{
			CWorldObject clone = wobj.clone();
			clone.theaddress=adr;
			clone.theaddressid=adr.addressId;
			clone.uniqueId+=icount;
			icount++; //da das objekt nicht in der worldobjectliste ist kommt bekommen alle die gleiche id
			
			adr.listWorldObjects_Ground.add(clone);
		}		
		
		//render objects die cloned liste wird im placing modus über alles gerendert
		//render address: paramter: adresse: dann nur diese rendern
		
		return adr;
	}
	
	public AddressSide checkLineIntersect(Vector3 vm, int itolerance)
	{
		int tvalue=itolerance;
		
		//left
		if(vm.x>=sx-tvalue*2 && vm.x<=sx+tvalue && vm.y>=sy && vm.y<=ey)
			return AddressSide.LEFT;
		
		//right
		if(vm.x>=ex-tvalue*2 && vm.x<=ex+tvalue && vm.y>=sy && vm.y<=ey)
			return AddressSide.RIGHT;
		
		//top
		if(vm.y>=ey-tvalue*2 && vm.y<=ey+tvalue*2 && vm.x>=sx && vm.x<=ex)
			return AddressSide.TOP;
		
		//bottom
		if(vm.y>=sy-tvalue*2 && vm.y<=sy+tvalue && vm.x>=sx && vm.x<=ex)
			return AddressSide.BOTTOM;
		
		return AddressSide.NONE;
	}
	
	public Polygon getBoundingPolygon(AddressOverlap adrOverlap)
	{
		Rectangle bounds=null;
		
		int factor=0;
		if(adrOverlap==AddressOverlap.RESIZE)
			bounds = new Rectangle(sx-factor, sy-factor, (ex-sx)+factor*2, (ey-sy)+factor*2);
		else
			bounds = new Rectangle(sx, sy, ex-sx, ey-sy);
		
		Polygon polygon = new Polygon(new float[]{0,0,bounds.width,0,bounds.width,bounds.height,0,bounds.height});
		polygon.setOrigin(bounds.width/2, bounds.height/2);
		polygon.setPosition(bounds.x, bounds.y);
		
		return polygon;
	}
	
	public Rectangle getBoundingRect(AddressOverlap adrOverlap)
	{
		int factor=0;
		if(adrOverlap==AddressOverlap.RESIZE)
			return new Rectangle(sx-factor, sy-factor, (ex-sx)+factor*2, (ey-sy)+factor*2);
		
		return new Rectangle(sx, sy, ex-sx, ey-sy);
	}	
	
	public Boolean testpoint(int x, int y)
	{
		if(x>sx && x<ex && y>sy && y<ey)
			return true;
		
		return false;
	}
		
	public CWorldObject[] getToiletObjects(char sgender)
	{
		CWorldObject[] objarr = {null, null};
		
		CWorldObject tempsink = null;
		
		for(CWorldObject wobj : listWorldObjects)
		{
			if(wobj.bDeleted || !wobj.isActiveByWaterConsumption())
				continue;
			
			//if(wobj.theobject.editoraction.contains("toiletroom_toiletstall") && wobj.isOccupiedBy!=null)
			//	Gdx.app.debug(""+wobj.uniqueId, "stall occ: " + wobj.isOccupiedBy.thehuman.name);
			
			//			if(!wobj.theobject.editoraction.contains("bathroom_toilet") &&
			//					!wobj.theobject.editoraction.contains("bathroom_sink") &&
			//					!wobj.theobject.editoraction.contains("toiletroom_m_urinal") &&
			//					!wobj.theobject.editoraction.contains("toiletroom_toiletstall")
			//					)
			//				continue;
			
			if(addressType.contains("residential"))
			{
				if(wobj.theobject.editoraction.contains("bathroom_toilet") && wobj.isOccupiedBy==null)
					objarr[0]=wobj;
				
				if(wobj.theobject.editoraction.contains("bathroom_sink"))
				{
					if(wobj.isOccupiedBy==null)
						objarr[1]=wobj;
					else
						tempsink=wobj;
				}
			}
			
			if(addressType.contains("public"))
			{
				//Gdx.app.debug("log1: ", ""+wobj.theobject.editoraction); 
				
				if(!wobj.theobject.editoraction.contains("toiletroom"))
					continue;
				
				if(wobj.theroom==null || wobj.theroom.theobject==null || wobj.theroom.theobject.editoraction==null)
					continue;
				
				//Gdx.app.debug("", "room: " + wobj.theroom.theobject.editoraction);
				
				//if(!wobj.theroom.theobject.editoraction.contains("toiletroom_w"))
				//	continue;
				
				//if(wobj.theroom!=null)
				//	Gdx.app.debug("log action1 - "+wobj.theobject.editoraction, "room: " + wobj.theroom.theobject.editoraction + ", gender: " + sgender);
				//else
				//	Gdx.app.debug("log action1 - "+wobj.theobject.editoraction, "room is null");
				
				//Gdx.app.debug("log2: " + "human: "+sgender, "room: "+wobj.theroom.theobject.editoraction);
				
				if(wobj.theroom!=null && wobj.theroom.theobject.editoraction.contains("toiletroom_m") && sgender=='w')
					continue;
				
				if(wobj.theroom!=null && wobj.theroom.theobject.editoraction.contains("toiletroom_w") && sgender=='m')
					continue;
				
				if(wobj.theobject.editoraction.contains("toiletroom_m_urinal") && sgender=='m' && wobj.isOccupiedBy==null)
					objarr[0] = wobj;
				
				if(wobj.theobject.editoraction.contains("toiletroom_toiletstall") && wobj.isOccupiedBy==null)
					objarr[0] = wobj;
				
				if(wobj.theobject.editoraction.contains("toiletroom_sink"))
				{
					if(wobj.isOccupiedBy==null)
						objarr[1] = wobj;
					else
						tempsink=wobj;
				}
			}
		}
		
		if(objarr[1]==null)
			objarr[1]=tempsink;
		
		return objarr;
	}
	
	public int getSize()
	{
		return (int)(Math.abs(ex-sx)*Math.abs(ey-sy));		
	}
	
	public int getWidth()
	{
		return (int)(Math.abs(ex-sx));		
	}
	
	public int getHeight()
	{
		return (int)Math.abs(ey-sy);		
	}
	
	public CWorldObject getWorldObject(CWorldObject human, String edac1, String edac2, String edac3, Boolean checkIsActiveByWaterAndEnergy, Boolean checkIsOccupied, Boolean checkIsOccupiedExtern, int iCheckObjectfilling)
	{
		for(CWorldObject wobj : listWorldObjects)
		{
			Boolean bContinue=true;
			if(!edac1.isEmpty() && wobj.theobject.editoraction.contains(edac1))
				bContinue=false;
			if(!edac2.isEmpty() && wobj.theobject.editoraction.contains(edac2))
				bContinue=false;
			if(!edac3.isEmpty() && wobj.theobject.editoraction.contains(edac3))
				bContinue=false;
			
			if(bContinue)
				continue;
			
			if(iCheckObjectfilling>0)
			{
				if(wobj.getObjectFillingMultiMax()>0)
				{
					if(wobj.getObjectFillingMulti()<iCheckObjectfilling)
						continue;
				}
				else if(wobj.getObjectFillingMax()>0)
				{
					if(wobj.objectFilling<iCheckObjectfilling)
						continue;
				}
			}
			
			if(checkIsActiveByWaterAndEnergy)
				if(!wobj.isActiveByEnergyConsumption() || !wobj.isActiveByWaterConsumption())
					continue;
			
			if(checkIsOccupied && human!=null)
				if(wobj.isOccupiedBy!=null && wobj.isOccupiedBy.uniqueId!=human.uniqueId)
					continue;
			
			if(checkIsOccupiedExtern && human!=null)
				if(wobj.isOccupiedByExtern!=null && wobj.isOccupiedByExtern.uniqueId!=human.uniqueId)
					continue;
			
			return wobj;
		}
		
		return null;
	}
	
	public CWorldObject getObjectById(int id)
	{
		Optional<CWorldObject> opt = listWorldObjects.stream().filter(item->item.uniqueId==id).findFirst();
		if(opt.isPresent())
			return opt.get();
		
		return null;
	}
	
	public int getSellingPrice(String mode)
	{
		int price = getSellingPrice(getSize(), addressType, town);
		
		if(mode.equals("0"))
			return price;
		
		if(listWorldObjects!=null && listWorldObjects.size()>0)
		{
			for(CWorldObject wobj : listWorldObjects)
			{
				if(!wobj.isHuman())
				{
					price+=wobj.theobject.getSellingPrice();
				}
			}
		}
		
		if(listWorldObjects_Floors!=null && listWorldObjects_Floors.size()>0)
		{
			for(CWorldObject wobj : listWorldObjects_Floors)
			{
				if(!wobj.isHuman())
				{
					price+=wobj.theobject.getSellingPrice();
				}
			}
		}
		
		if(listWorldObjects_Ground!=null && listWorldObjects_Ground.size()>0)
		{
			for(CWorldObject wobj : listWorldObjects_Ground)
			{
				price+=wobj.theobject.getSellingPrice();
			}
		}		
		
		return price;
	}
		
	public int getPrice()
	{
		return getPrice(getSize(), addressType, town);
	}
		
	public static int getSellingPrice(float size, String adrType, CTown town)
	{
		return getPrice(size, adrType, town)/3;
	}
	
	public static int getPrice(float size, String adrType, CTown town)
	{
		float modificator=1f;
		
		if(size>50000)
			modificator+=5f;
		if(size>100000)
			modificator+=6f;
		if(size>200000)
			modificator+=7f;
		if(size>300000)
			modificator+=8f;
		if(size>400000)
			modificator+=9f;
		if(size>500000)
			modificator+=10f;
		if(size>600000)
			modificator+=11f;
		if(size>700000)
			modificator+=12f;
		if(size>800000)
			modificator+=13f;
		if(size>900000)
			modificator+=14f;
		if(size>1000000)
			modificator+=15f;
		if(size>1100000)
			modificator+=16f;
		if(size>1200000)
			modificator+=18f;
		if(size>1300000)
			modificator+=21f;
		if(size>1400000)
			modificator+=25f;
		if(size>1500000)
			modificator+=30f;
		if(size>1600000)
			modificator+=35f;
		if(size>1700000)
			modificator+=40f;
		if(size>1800000)
			modificator+=45f;
		if(size>1900000)
			modificator+=50f;
		if(size>2000000)
			modificator+=55f;
		if(size>2100000)
			modificator+=60f;
		if(size>2200000)
			modificator+=65f;
		if(size>2300000)
			modificator+=70f;
		if(size>2400000)
			modificator+=75f;
		if(size>2500000)
			modificator+=80f;		
				
		if(adrType.contains("residential"))
			modificator/=(80);
		
		if(adrType.contains("public"))
			modificator/=(200);
		
		//modificator/=200;

		
		int iprice=Math.round(size/5200f*modificator);
		
		if(adrType.contains("public"))
			iprice=25000+iprice;
		
		if(adrType.contains("public"))
			iprice*=town.publicAdrPriceDelta;

		if(adrType.contains("residential"))
			iprice*=town.residentialAdrPriceDelta;
				
		return iprice;
	}
	
	public int getCloningPrice()
	{
		int iprice=0;
		
		iprice=getPrice();
		
		if(listWorldObjects!=null && listWorldObjects.size()>0)
			for(CWorldObject wobj : listWorldObjects)
				if(!wobj.isHuman())
					iprice+=wobj.theobject.price;
		
		if(listWorldObjects_Floors!=null && listWorldObjects_Floors.size()>0)
			for(CWorldObject wobj : listWorldObjects_Floors)
					iprice+=wobj.theobject.price;
		
		if(listWorldObjects_Ground!=null && listWorldObjects_Ground.size()>0)
			for(CWorldObject wobj : listWorldObjects_Ground)
					iprice+=wobj.theobject.price;		
		
		return iprice;
	}
	
}
