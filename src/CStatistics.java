package com.mygdx.game;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.badlogic.gdx.Gdx;


public class CStatistics {
	
//	enum StatisticType {
//		FINANCE, POPULATION, OTHER
//	}
	
	CTown town;
	
	public static class CStatisticsData_Finance
	{
		public int day;
		int buyAddress;
		int sellAddress;
		int buyObject;
		int sellObject;
		int buyResident;
		int residentSpendsMoney;
		int residentEarnsMoney;
		int happinessplus;
		int happinessminus;
		int childsupport;
		int education;
		int deceased;
		int sum;
		
		public void calculateSum()
		{
			sum = sellAddress 
				- buyAddress 
				+ sellObject 
				- buyObject 
				- buyResident 
				+ residentEarnsMoney
				- residentSpendsMoney
				+ happinessplus
				- happinessminus
				+ childsupport
				+ education
				- deceased;
		}
		
		public CStatisticsData_Finance()
		{
			day=0;
			buyAddress=0;
			sellAddress=0;
			buyResident=0;
			buyObject=0;
			sellObject=0;
			residentSpendsMoney=0;
			residentEarnsMoney=0;
			happinessplus=0;
			happinessminus=0;
			childsupport=0;
			education=0;
			deceased=0;			
			sum=0;
		}
		
		public void clear()
		{
			day=0;
			buyAddress=0;
			sellAddress=0;
			buyResident=0;
			buyObject=0;
			sellObject=0;
			residentSpendsMoney=0;
			residentEarnsMoney=0;
			happinessplus=0;
			happinessminus=0;
			childsupport=0;
			education=0;
			deceased=0;			
			sum=0;
		}
	}
	
	public static class CStatisticsData_Population
	{
		public int day;

		//Count
		int countAll;
		int countMen;
		int countWomen;
		int countHomeless;
		int count0To20;
		int count21To40;
		int count41To60;
		int count61To80;
		int count81To100;
		int count101ToX;
		
		//Attributes
		int ageAVG;
		int ageMin;
		int ageMax;
		
		int happinessAVG;
		int happinessMin;
		int happinessMax;
		
		int healthAVG;
		int healthMin;
		int healthMax;
		
		int eatAVG;
		int cleanAVG;
		int sleepAVG;
		int toiletAVG;
				
		int fitnessAVG;
		int fitnessMin;
		int fitnessMax;
		
		int intelligenceAVG;
		int intelligenceMin;
		int intelligenceMax;
		
		int workoutputAVG;
		int workoutputMin;
		int workoutputMax;
		
		float educationAVG;
		float educationMin;
		float educationMax;
		
		float healthAttitudeAVG;
		float healthAttitudeMin;
		float healthAttitudeMax;
		
		float positiveAttitudeAVG;
		float positiveAttitudeMin;
		float positiveAttitudeMax;
		
		public CStatisticsData_Population()
		{
			day=0;
			
			//Count
			countAll=0;
			countMen=0;
			countWomen=0;
			countHomeless=0;
			count0To20=0;
			count21To40=0;
			count41To60=0;
			count61To80=0;
			count81To100=0;
			count101ToX=0;
			
			//Attributes
			ageAVG=0;
			educationAVG=0;
			happinessAVG=0;
			healthAVG=0;
			fitnessAVG=0;
			intelligenceAVG=0;
			workoutputAVG=0;
			healthAttitudeAVG=0;
			positiveAttitudeAVG=0;
			
			sleepAVG=0;
			eatAVG=0;
			cleanAVG=0;
			toiletAVG=0;
			
			ageMin=500;
			educationMin=500;
			happinessMin=500;
			healthMin=500;
			fitnessMin=500;
			intelligenceMin=500;
			workoutputMin=500;
			healthAttitudeMin=500;
			positiveAttitudeMin=500;			
			
			ageMax=0;
			educationMax=0;
			happinessMax=0;
			healthMax=0;
			fitnessMax=0;
			intelligenceMax=0;
			workoutputMax=0;
			healthAttitudeMax=0;
			positiveAttitudeMax=0;			
		}
		
		public void clear()
		{
			day=0;
			
			//Count
			countAll=0;
			countMen=0;
			countWomen=0;
			countHomeless=0;
			count0To20=0;
			count21To40=0;
			count41To60=0;
			count61To80=0;
			count81To100=0;
			count101ToX=0;
			
			//Attributes
			ageAVG=0;
			educationAVG=0;
			happinessAVG=0;
			healthAVG=0;
			fitnessAVG=0;
			intelligenceAVG=0;
			workoutputAVG=0;
			healthAttitudeAVG=0;
			positiveAttitudeAVG=0;			
			
			sleepAVG=0;
			eatAVG=0;
			cleanAVG=0;
			toiletAVG=0;
			
			ageMin=500;
			educationMin=500;
			happinessMin=500;
			healthMin=500;
			fitnessMin=500;
			intelligenceMin=500;
			workoutputMin=500;
			healthAttitudeMin=500;
			positiveAttitudeMin=500;		
			
			ageMax=0;
			educationMax=0;
			happinessMax=0;
			healthMax=0;
			fitnessMax=0;
			intelligenceMax=0;
			workoutputMax=0;
			healthAttitudeMax=0;
			positiveAttitudeMax=0;			

		}
		
		public void calculateAVG()
		{
			if(countAll==0)
				return;
			ageAVG/=countAll;
			educationAVG/=countAll;
			happinessAVG/=countAll;
			healthAVG/=countAll;
			fitnessAVG/=countAll;
			intelligenceAVG/=countAll;
			workoutputAVG/=countAll;
			healthAttitudeAVG/=countAll;
			positiveAttitudeAVG/=countAll;
			
			sleepAVG/=countAll;
			eatAVG/=countAll;
			cleanAVG/=countAll;
			toiletAVG/=countAll;
		}
	}
	
	public static class CStatisticsData_Other
	{
		public int day;
		
		//Address
		int countAddressAll;
		int countAddressResidential;
		int countAddressPublic;
		
		//Energy/Water
		int energyOutput;
		int energyConsumption;
		int waterOutput;
		int waterConsumption;
		
		//Traffic
		int countFootpath;
		int countRoad;
		int countParkingspace;
		int countGarage;
		int countVehiclesPrivate;
		int countVehiclesPublic;
		
		int temp_countParkingspace;
		int temp_countGarage;
		int temp_countVehiclesPrivate;
		int temp_countVehiclesPublic;
		
		//Company
		int countCompanies;
		int countWorkerAll;
		int countWorkplacesAll;
		
		public CStatisticsData_Other()
		{
			day=0;
			
			//Address
			countAddressAll=0;
			countAddressResidential=0;
			countAddressPublic=0;
			
			//Energy/Water
			energyOutput=0;
			energyConsumption=0;
			waterOutput=0;
			waterConsumption=0;
			
			//Traffic
			countFootpath=0;
			countRoad=0;
			countParkingspace=0;
			countGarage=0;
			countVehiclesPrivate=0;
			countVehiclesPublic=0;
			
			//Company
			countCompanies=0;
			countWorkerAll=0;
			countWorkplacesAll=0;			
		}
		
		public void clear()
		{
			day=0;
			
			//Address
			countAddressAll=0;
			countAddressResidential=0;
			countAddressPublic=0;
			
			//Energy/Water
			energyOutput=0;
			energyConsumption=0;
			waterOutput=0;
			waterConsumption=0;
			
			//Traffic
			countFootpath=0;
			countRoad=0;
			countParkingspace=0;
			countGarage=0;
			countVehiclesPrivate=0;
			countVehiclesPublic=0;
			
			//Company
			countCompanies=0;
			countWorkerAll=0;
			countWorkplacesAll=0;			
		}
		
		public void clearTemp()
		{
			temp_countParkingspace=0;
			temp_countGarage=0;
			temp_countVehiclesPrivate=0;
			temp_countVehiclesPublic=0;
		}
		
		public void transferTempToReal()
		{
			countParkingspace=temp_countParkingspace;
			countGarage=temp_countGarage;
			countVehiclesPrivate=temp_countVehiclesPrivate;
			countVehiclesPublic=temp_countVehiclesPublic;
		}
	}
	
	public ArrayList<CStatisticsData_Finance> statisticsData_Finance;
	public ArrayList<CStatisticsData_Population> statisticsData_Population;
	public ArrayList<CStatisticsData_Other> statisticsData_Other;
	private CStatisticsData_Population tempStatisticsData_Population;
	
	public CStatistics(CTown t)
	{
		town=t;
		statisticsData_Finance = new ArrayList<CStatisticsData_Finance>();
		statisticsData_Population = new ArrayList<CStatisticsData_Population>();
		statisticsData_Other = new ArrayList<CStatisticsData_Other>();
	}
	
	public int gettheday()
	{
		return town.gameWorld.worldTime.getCurrentDay();
		//return 1;
	}
	
	public CStatisticsData_Finance getCurrentStatistics_Finance()
	{
		int ikey = gettheday(); //town.gameWorld.worldTime.getCurrentDay();
		
		if(statisticsData_Finance.size()>0 && statisticsData_Finance.get(0).day==ikey)
			return statisticsData_Finance.get(0);
		
		CStatisticsData_Finance tempFinance = new CStatisticsData_Finance();
		tempFinance.day = ikey;
		statisticsData_Finance.add(0, tempFinance);
		
		return tempFinance;
	}
	
	public CStatisticsData_Population getCurrentStatistics_Population()
	{
		int ikey = gettheday(); //town.gameWorld.worldTime.getCurrentDay();
		
		if(statisticsData_Population.size()>0 && statisticsData_Population.get(0).day==ikey)
			return statisticsData_Population.get(0);
		
		CStatisticsData_Population temp_population = new CStatisticsData_Population();
		temp_population.day = ikey;
		statisticsData_Population.add(0, temp_population);
		
		return temp_population;
	}
	
	public void transferTempToOriginal_Population()
	{
		CStatisticsData_Population current = getCurrentStatistics_Population();
				
		current.count101ToX=tempStatisticsData_Population.count101ToX;
		current.count0To20=tempStatisticsData_Population.count0To20;
		current.count21To40=tempStatisticsData_Population.count21To40;
		current.count41To60=tempStatisticsData_Population.count41To60;
		current.count61To80=tempStatisticsData_Population.count61To80;
		current.count81To100=tempStatisticsData_Population.count81To100;
		current.countAll=tempStatisticsData_Population.countAll;
		current.countHomeless=tempStatisticsData_Population.countHomeless;
		current.countMen=tempStatisticsData_Population.countMen;
		current.countWomen=tempStatisticsData_Population.countWomen;
		
		current.ageAVG=tempStatisticsData_Population.ageAVG;
		current.educationAVG=tempStatisticsData_Population.educationAVG;
		current.fitnessAVG=tempStatisticsData_Population.fitnessAVG;
		current.happinessAVG=tempStatisticsData_Population.happinessAVG;
		current.healthAttitudeAVG=tempStatisticsData_Population.healthAttitudeAVG;
		current.healthAVG=tempStatisticsData_Population.healthAVG;
		current.intelligenceAVG=tempStatisticsData_Population.intelligenceAVG;
		current.positiveAttitudeAVG=tempStatisticsData_Population.positiveAttitudeAVG;
		current.workoutputAVG=tempStatisticsData_Population.workoutputAVG;
		
		current.sleepAVG=tempStatisticsData_Population.sleepAVG;
		current.eatAVG=tempStatisticsData_Population.eatAVG;
		current.cleanAVG=tempStatisticsData_Population.cleanAVG;
		current.toiletAVG=tempStatisticsData_Population.toiletAVG;
		
		current.ageMin=tempStatisticsData_Population.ageMin;
		current.educationMin=tempStatisticsData_Population.educationMin;
		current.fitnessMin=tempStatisticsData_Population.fitnessMin;
		current.happinessMin=tempStatisticsData_Population.happinessMin;
		current.healthAttitudeMin=tempStatisticsData_Population.healthAttitudeMin;
		current.healthMin=tempStatisticsData_Population.healthMin;
		current.intelligenceMin=tempStatisticsData_Population.intelligenceMin;
		current.positiveAttitudeMin=tempStatisticsData_Population.positiveAttitudeMin;
		current.workoutputMin=tempStatisticsData_Population.workoutputMin;
		
		current.ageMax=tempStatisticsData_Population.ageMax;
		current.educationMax=tempStatisticsData_Population.educationMax;
		current.fitnessMax=tempStatisticsData_Population.fitnessMax;
		current.happinessMax=tempStatisticsData_Population.happinessMax;
		current.healthAttitudeMax=tempStatisticsData_Population.healthAttitudeMax;
		current.healthMax=tempStatisticsData_Population.healthMax;
		current.intelligenceMax=tempStatisticsData_Population.intelligenceMax;
		current.positiveAttitudeMax=tempStatisticsData_Population.positiveAttitudeMax;
		current.workoutputMax=tempStatisticsData_Population.workoutputMax;
	}
	
	public CStatisticsData_Population getCurrentStatistics_Population_Temp()
	{
		if(tempStatisticsData_Population == null)
			tempStatisticsData_Population = new CStatisticsData_Population();
		
		return tempStatisticsData_Population;
	}
	
	public CStatisticsData_Other getCurrentStatistics_Other()
	{
		int ikey = gettheday(); //town.gameWorld.worldTime.getCurrentDay();
		
		if(statisticsData_Other.size()>0 && statisticsData_Other.get(0).day==ikey)
			return statisticsData_Other.get(0);
		
		CStatisticsData_Other temp_other = new CStatisticsData_Other();
		temp_other.day = ikey;
		statisticsData_Other.add(0, temp_other);
		
		return temp_other;
	}
	
	public void calculateCurrentOtherStatistics()
	{
		CStatisticsData_Other stat = getCurrentStatistics_Other();
		
		//Address
		int temp_countAddressAll=0;
		int temp_countAddressResidential=0;
		int temp_countAddressPublic=0;
		
		for(CAddress adr : town.gameWorld.worldAddressList)
		{
			temp_countAddressAll++;
			if(adr.addressType.contains("residential"))
				temp_countAddressResidential++;
			if(adr.addressType.contains("public"))
				temp_countAddressPublic++;
		}
		
		stat.countAddressAll=temp_countAddressAll;
		stat.countAddressResidential=temp_countAddressResidential;
		stat.countAddressPublic=temp_countAddressPublic;
		
		//Energy/Water
		stat.energyOutput=town.gameWorld.getEnergyOutput();
		stat.energyConsumption=town.gameWorld.getEnergyConsumption();
		stat.waterOutput=town.gameWorld.getWaterOutput();
		stat.waterConsumption=town.gameWorld.getWaterConsumption();
		
		//Traffic
		stat.countFootpath=town.gameWorld.worldFootpath.size();
		stat.countRoad=town.gameWorld.worldRoad.size();
//		stat.countParkingspace=0;
//		stat.countGarage=0;
//		stat.countVehiclesPrivate=0;
//		stat.countVehiclesPublic=0;
//		for(CWorldObject wobj : town.gameWorld.worldObjects)
//		{
//			if(wobj.theobject.editoraction.contains("road_road_parkingspace"))
//				stat.countParkingspace++;
//			if(wobj.theobject.editoraction.contains("residential_garage"))
//				stat.countGarage++;
//			if(wobj.theobject.editoraction.contains("traffic_car"))
//				stat.countVehiclesPrivate++;
//			if(wobj.theobject.editoraction.contains("traffic_car") && wobj.theobject.editoraction.contains("company"))
//				stat.countVehiclesPublic++;
//		}
		
		//Company
		//stat.countCompanies=0;
		//stat.countWorkerAll=0;
		//stat.countWorkplacesAll=0;
		int temp_countCompanies=0;
		int temp_countWorkerAll=0;
		int temp_countWorkplacesAll=0;
		
		for(CCompany comp : town.gameWorld.worldCompanyList)
		{
			temp_countCompanies++;
			
			if(comp.address_company==null)
			{
				//Gdx.app.debug("", "company address is null: " + comp.companyType.toString());
				continue;
			}
			
			for(CWorldObject wobj : comp.address_company.listWorldObjects)
			{
				if(wobj.worker!=null)
					temp_countWorkerAll++;
				if(wobj.worker2!=null)
					temp_countWorkerAll++;
				if(wobj.isCompanyWorkingPlace() || wobj.isCompanyTaskObject())
					temp_countWorkplacesAll++;
			}
		}
		
		stat.countCompanies=temp_countCompanies;
		stat.countWorkerAll=temp_countWorkerAll;
		stat.countWorkplacesAll=temp_countWorkplacesAll;
	}
	
	public void clear()
	{
		statisticsData_Finance.clear();
		statisticsData_Population.clear();
		statisticsData_Other.clear();
	}
	
	
//	- tagesliste finances
//	
//	Date Address Residents Objects Government Other All
//
//	erster eintrag: summary bis aktuell
//
//	- erstellung von adressen
//	+ verkauf von adressen
//	- käufe von residents/objekten
//	+ verkäufe von objekten
//	- residents geben geld aus (supermarket, pub, ..?)
//	+ residents verdienen geld (software company)
//	+ grants
//	- penalties
//	summary values
//
//
//- tageslisten residents
//	über buttons die einzelnen listen umschalten
//	oder über dropdown
//	oder über basisliste mit view button
//	
//	- tagesliste resident count
//		Date Men Women 0-20 21-40 41-60 61-80 81-100 101-X Homeless All
//		m
//		w
//		homeless
//		gesamt
//		
//		
//	- tagesliste resident attributes
//		- date avg evtl /min/max
//		- wenn platz nicht reicht: zeige icons anstatt überschriften dann passen alle drauf
//		- nur avg
//
//		- Alter
//		- Education
//		- Happiness
//		- Health
//		- Fitness
//		- Intelligence
//		- Workoutput
//		- Health Attitude
//		- Positive Attitude
//		
//	- aktuelle werte
//		avg-min-max
//		- nur aktuelle werte, keine historie keine speicherung:
//		- hunger
//		- sleep
//		- clean
//		- toilet
	
//- tagesliste energy/water
//	date-energyconsumption/energy output-water consumption/water output
//	- energy output
//	- energy consumption
//	- water output
//	- water consumption
////
//- tagesliste address			
//	date count residential, count public/commercial, count gesamt
//	anzahl firmenadressen
//	anzahl residentialadressen
////
//- tagesliste traffic
//	date-count cars-count road tiles
//	- anzahl cars pro typ nach datum
//	- anzahl road tiles
////
//- tagesliste companies
//	count companies
//	count worker all
//	count workplaces all	
	
}
