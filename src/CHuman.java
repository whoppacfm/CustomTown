package com.mygdx.game;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.mygdx.game.CAction.ActionMode;
import com.mygdx.game.CAction.ValueType;
import com.mygdx.game.CAnimationTextEvent.AnimationEventType;
import com.mygdx.game.CCompany.CompanyType;
import com.mygdx.game.CHelper.IntersectionMode;
import com.mygdx.game.CHuman.CJobSkillClass;


public class CHuman {
	
	public static class CJobSkillClass
	{
		float fskill;
		CObject theobject;
	}
	
	//	public static CJobSkillClass getNewCJobSkillClass()
	//	{
	//		return new CJobSkillClass();
	//	}
	
	public static class JobSkillClassComparator implements Comparator<CJobSkillClass> 
	{
	  //Skills absteigend sortieren
	  public int compare(CJobSkillClass s1, CJobSkillClass s2)
	  {
	    if (s1.fskill == s2.fskill) 
	    {
	    	return 0;
	    } 
	    else 
	    {
	    	return s1.fskill > s2.fskill ? -1 : 1;
	    }
	  }
	}
	
	CTown town;
	CWorldObject baseWorldObject;
	CWorldObject bed;
	CWorldObject car;
	CWorldObject wardrobe;
	
	List<String> sDemandList;
	
	public String headTextureId;
	
	//public String name;
	public String forename;
	public String lastname;
	
	public BigDecimal ageSeconds;
	public char gender; //m,w
	private float healthVal;
	public int healthValueMax; //80-200
	private float happinessVal;
	public int happinessValueMax; //80-200
	private float fitnessValue; //160
	private float beliefValue;
	
	public int abilitySpaceshipTechnology;
	public int abilitySuperstar;
	
	private float intelligenceValue; //160
	private  float educationValue;
	float fitnessValueMax;
	float healthAttitude;
	float positiveAttitude;
	float alcoholLevel; //Achtung wird derzeit nicht im savegame gespeichert
	float coffeinLevel;
	float sick;
	int sickType; //1=severe, 2=contagious
	float doctorHealingValue;
	float fruitLevel; 
	
	
	 //Achtung: energyValue geht von 0-100, 0=keine energy, 100=max energy
	public float energyValue;
	public float energyValueTrigger;
	public float energyValueTriggerRed;
	public float energyValueTriggerMax;
	
	public float sleepValue;
	public float sleepValueTrigger;
	public float sleepValueTrigger2;
	public float sleepValueTrigger3;
	public float sleepValueTriggerRed;
	public float sleepValueTriggerMax;
		
	public float eatValue;
	public float eatValueTrigger;
	public float eatValueTrigger2;
	public float eatValueTrigger3;
	public float eatValueTriggerRed;
	public float eatValueTriggerMax;
		
	public float cleanValue;
	public float cleanValueTrigger;
	public float cleanValueTrigger2;
	public float cleanValueTriggerRed;
	public float cleanValueTriggerMax;
	
	public float clothingValue;
	//public float cleanValueMax;
	public float clothingValueTrigger;
	public float clothingValueTrigger2;
	public float clothingValueTriggerRed;
	public float clothingValueTriggerMax;
		
	public float toiletValue;
	//public float toiletValueMax;
	public float toiletValueTrigger;
	public float toiletValueTrigger2;
	public float toiletValueTrigger3;
	public float toiletValueTriggerRed;
	public float toiletValueTriggerMax;
		
	public float workValue;
	public float workValueTrigger; //für Pause
	private Random rand;
	
	private float hourSeconds;
	private float minuteSeconds;
		
	public Color clothingColor_Top;
	public Color clothingColor_Bottom;
	public Color clothingColor_Shoes;
	
	public Color clothingColor_Top_Now;
	public Color clothingColor_Bottom_Now;
	public Color clothingColor_Shoes_Now;
	
	public Boolean bDeceasedInReach;
	public Boolean bIsCold;
	public Boolean bIsDark;
	public float iGarbage;
	public float iLaundry;
	public float iDecor;
	
	public List<Integer> influenceList;
	
	Hashtable<Integer, CWorldObject> workplaces;
	Hashtable<Integer, CWorldObject> taskobjects; //zB kühlschrank der befüllt werden muss, dinnertable
	Hashtable<Integer, CJobSkillClass> jobSkillLevel; //workspace objectid, workexperience
	
	public String getName()
	{
		return forename + " " + lastname;
	}
	
	public void setName(String sname)
	{
		if(sname.contains(" "))
		{
			String arr1[]=sname.split(" ");
			
			if(arr1.length>0)
				forename=arr1[0];
			
			if(arr1.length>1)
				lastname=arr1[1];
			
			if(arr1.length>2)
				lastname+=" " + arr1[2];
		}
		else
			forename=sname;
	}
	
	public void initSkillByEducation()
	{
		int skill_min=getAge()/2;
		int skill_max=getAge()*2;
		
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
			int skillCount = town.gameResourceConfig.listWorkplacesAndTasksWithSkill.size()-1;
			int randomSkillnr=-1;
			
			while(randomSkillnr==-1 || randomSkillnr==tempskillnr1 || randomSkillnr==tempskillnr2)
				randomSkillnr=rand.nextInt(skillCount+1);
			
			if(tempskillnr1 == -1)
				tempskillnr1 = randomSkillnr;
			else if(tempskillnr2 == -1)
				tempskillnr2 = randomSkillnr;
			
			CObject skillobj = town.gameResourceConfig.listWorkplacesAndTasksWithSkill.get(randomSkillnr);
			CJobSkillClass jsc1 = new CJobSkillClass();
			jsc1.theobject=skillobj;
			jsc1.fskill=fskill;
			
			float requireded = skillobj.getRequiredWorkplaceEducation();
			
			if(Math.abs(getEducationValue()-requireded)<=1 && getEducationValue()>=requireded)
			{
				int objectid = skillobj.getSkillObjectId();
				jobSkillLevel.put(objectid, jsc1);
				//setEducationValue(requireded);
			}
		}
	}
	
	public void setIntelligenceValue(float value)
	{
		intelligenceValue=value;
	}
	
	public float getIntelligenceValue()
	{
		return intelligenceValue;
	}

	public float getIntelligenceValueMax()
	{
		return 160;
	}

	public float getFitnessValueMax()
	{
		return 160;
	}
	
	public String getAllJobsTitleString()
	{
		String ws="";
		if(workplaces.size()>0)
		{
			for(CWorldObject wobj : workplaces.values())
			{
				ws+=wobj.getCompanyWorkingPlaceJobTitle(0);
				ws+=", ";
			}
		}
		if(ws.length()>2)
			ws=ws.substring(0, ws.length()-2);
		
		return ws;
	}
	
	public String getAllTasksTitleString()
	{
		String ws="";
		if(taskobjects.size()>0)
		{
			for(CWorldObject wobj : taskobjects.values())
			{
				int type = 0;
				if (wobj.worker != null && wobj.worker.uniqueId == baseWorldObject.uniqueId)
					type = 1;	
				ws+=wobj.getTaskText(type, 0);
				ws+=", ";
			}
		}
		if(ws.length()>2)
			ws=ws.substring(0, ws.length()-2);
		
		return ws;
	}
	
	public String getJobSkillLevelName(int objid)
	{
		int exp=0;
		if(jobSkillLevel.containsKey(objid))
			exp = Math.round(jobSkillLevel.get(objid).fskill);
		
		String skilllevelname="Novice";
		//beginner 0-30, intermediate 31-60, advanced 61-90 expert 91-99, master 100
		if(exp>30)
			skilllevelname="Mediocre";
		if(exp>60)
			skilllevelname="Advanced";
		if(exp>=90)
			skilllevelname="Expert";
		if(exp>99)
			skilllevelname="Master";
		
		return skilllevelname;
	}
	
	public float changeIntelligenceValue(float changeValue)
	{
		if(changeValue>0)
			changeValue*=town.intelligenceDeltaPlus;
		else
			changeValue*=town.intelligenceDeltaMinus;
		
		intelligenceValue+=changeValue;
		
		//parentWorldObject.addAnimationEvent(AnimationEventType.INTELLIGENCE, changeValue);
		
		if(intelligenceValue<0)
			intelligenceValue=0;
		
		if(intelligenceValue>getIntelligenceValueMax())
			intelligenceValue=getIntelligenceValueMax();
		
		return changeValue;
	}
		
	public void setEducationValue(float value)
	{
		educationValue=value;
	}
	
	public int getSleepPercent()
	{
		int sleepPercent = (int) (sleepValue / sleepValueTriggerRed * 100);
		
		if (sleepPercent > 100)
			sleepPercent = 100;
		
		return 100-sleepPercent;
	}

	public int getEnergyPercent()
	{
		return (int) energyValue;
		
//		int energyPercent = (int) (energyValue / energyValueTriggerRed * 100);
//		
//		if (energyPercent > 100)
//			energyPercent = 100;
//		
//		return 100-energyPercent;
	}
	
	public int getEatPercent()
	{
		int eatPercent = (int) (eatValue / eatValueTriggerRed * 100);
		
		if (eatPercent > 100)
			eatPercent = 100;
		
		return 100-eatPercent;
	}
	
	public int getCleanPercent()
	{
		int cleanPercent = (int) (cleanValue / cleanValueTriggerRed * 100);
		
		if (cleanPercent > 100)
			cleanPercent = 100;
		
		return 100-cleanPercent;
	}
	
	public int getClothingPercent()
	{
		int clothingPercent = (int) (clothingValue / clothingValueTriggerRed * 100);
		
		if (clothingPercent > 100)
			clothingPercent = 100;
		
		return 100-clothingPercent;
	}
	
	public int getToiletPercent()
	{
		int toiletPercent = (int) (toiletValue / toiletValueTriggerRed * 100);
		
		if (toiletPercent > 100)
			toiletPercent = 100;
		
		return 100-toiletPercent;
	}
	
	public float getEducationValue()
	{
		return educationValue;
	}
	
	public static float getRequiredEducationForReading()
	{
		return 0.4f;
	}
	
	public float changeEducationValue(float changeValue)
	{
		if(changeValue>0)
			changeValue*=town.educationDeltaPlus;

		
		educationValue+=changeValue;
		
		//parentWorldObject.addAnimationEvent(AnimationEventType.EDUCATION, changeValue);
		
		if(educationValue<0)
			educationValue=0;
		
		if(educationValue>3)
			educationValue=3;
		
		return changeValue;
	}
	
	public void setHealthValue(float val)
	{
		healthVal=val;
	}

	public void setHappynessValue(float val)
	{
		happinessVal=val;
	}
	
	public float changeHealthValue(float changeValue)
	{
		if(changeValue>0)
			changeValue*=town.healthDeltaPlus;
		else
			changeValue*=town.healthDeltaMinus;
				
		//healthattitude als prozent von changevalue hinzufügen
		//float attr = healthAttitude*20;
		//float cv = changeValue/100*attr;
		
		if(healthAttitude<0.1f)
			healthAttitude=0.1f;
		float attr = healthAttitude*20f;
		float cv2 = Math.abs(changeValue);
		float cv = cv2/100f*attr;
		
		
		//fitnessvalue
		float fattr = fitnessValue/8f;
		float fv = Math.abs(changeValue)/100f*fattr;
		
		changeValue+=cv;
		changeValue+=fv;
		
		healthVal+=changeValue;
		
		if(changeValue>0)
		{
			//if(eatValue>eatValueTrigger3)
			//	changeValue/=2;
			if(eatValue>eatValueTriggerRed)
				changeValue/=1.5f;
			if(eatValue>eatValueTriggerMax)
				changeValue/=1.5f;
			
			//if(sleepValue>sleepValueTrigger3)
			//	changeValue/=2;
			if(sleepValue>sleepValueTriggerRed)
				changeValue/=1.5f;
			if(sleepValue>sleepValueTriggerMax)
				changeValue/=1.5f;
			
			if(cleanValue>cleanValueTriggerRed)
				changeValue/=1.5f;
			if(cleanValue>cleanValueTriggerMax)
				changeValue/=1.5f;
			
			if(energyValue<energyValueTriggerRed)
				changeValue/=1.5f;
			if(energyValue<energyValueTriggerMax)
				changeValue/=1.5f;
		}
		
		if(healthVal<0)
			healthVal=0;
		if(healthVal>healthValueMax)
			healthVal=healthValueMax;
		
		return changeValue;
	}
	
	public float getHealthValue()
	{
		return healthVal;
	}
	
	public float getHealthValueInverse()
	{
		float hval = getHealthValue();
		
		if(hval>99)
			return 0; 
		if(hval>90)
			return 0.1f;
		if(hval>80)
			return 0.2f;
		if(hval>70)
			return 0.3f;
		if(hval>60)
			return 0.4f;
		if(hval>50)
			return 0.5f;
		if(hval>40)
			return 0.6f;
		if(hval>30)
			return 0.7f;
		if(hval>20)
			return 0.8f;
		if(hval>10)
			return 0.9f;
		if(hval>0)
			return 1f;
		
		return 0;
	}
	
	public void setFitnessValue(float value)
	{
		fitnessValue=value;
	}
	
	public float changeFitnessValue(float changeValue)
	{
		if(changeValue>0)
			changeValue*=town.fitnessDeltaPlus;
		else
			changeValue*=town.fitnessDeltaMinus;
		
		fitnessValue+=changeValue;
		
		//parentWorldObject.addAnimationEvent(AnimationEventType.FITNESS, changeValue);
		//if(fitnessValue>fitnessValueMax)
		//	fitnessValue=fitnessValueMax;
		
		if(fitnessValue<1)
			fitnessValue=1;
		
		if(fitnessValue>getFitnessValueMax())
			fitnessValue=getFitnessValueMax();
		
		return changeValue;
	}
	
	public float getFitnessValue()
	{
		return fitnessValue;
	}
	
	public float getBeliefValue()
	{
		return beliefValue;
	}
	
	public void changeEnergyValue(float changeValue)
	{
		energyValue+=changeValue;
		
		if(energyValue<0)
			energyValue=0;
		
		if(energyValue>100)
			energyValue=100;
	}
	
	public float changeHappinessValue(float changeValue)
	{
		//bDeceasedInReach wird derzeit in handleattributevalues betrachtet
		//		if(parentWorldObject.thehuman.bDeceasedInReach)
		//		{
		//			if(changeValue>0)
		//				changeValue/=2;
		//		}
		
		//attitude als prozent von changevalue hinzufügen
		if(positiveAttitude<0.1f)
			positiveAttitude=0.1f;
		
		float attr = positiveAttitude*20f;
		float cv2=Math.abs(changeValue);
		//if(changeValue<0)
		//	attr=1;
		float cv = cv2/100f*attr;
		
		//fitnessvalue
		//float fattr = (float)fitnessValue/4f;
		//float fv = (float)changeValue/100f*(float)fattr;
				
		changeValue+=cv;
		
		if(changeValue>0)
		{
			if(eatValue>eatValueTrigger2)
			{
				changeValue/=1.5f;
			
				if(eatValue>eatValueTrigger3)
				{
					changeValue/=1.5f;
			
					if(eatValue>eatValueTriggerRed)
					{
						changeValue/=1.5f;
						
						if(eatValue>=eatValueTriggerMax)
						{
							changeValue/=1.5f;
						}
					}
				}
			}
			
			
			if(toiletValue>toiletValueTrigger2)
			{
				changeValue/=1.5f;
				
				if(toiletValue>toiletValueTrigger3)
				{
					changeValue/=1.5f;
				
					if(toiletValue>toiletValueTriggerRed)
					{
						changeValue/=1.5f;
						
						if(toiletValue>=toiletValueTriggerMax)
							changeValue/=1.5f;
					}
				}
			}
						
			if(cleanValue>cleanValueTrigger2)
			{
				changeValue/=1.5f;
				
				if(cleanValue>cleanValueTriggerRed)
				{
					changeValue/=1.5f;
					
					if(cleanValue>=cleanValueTriggerMax)
						changeValue/=1.5f;
				}
			}
			
			if(energyValue<energyValueTriggerRed)
			{
				changeValue/=1.5f;
				
				if(energyValue<=energyValueTriggerMax)
					changeValue/=1.5f;
			}
		}
		
		if(changeValue>0)
		{
			changeValue*=town.happinessDeltaPlus;
		}
		else
		{
			changeValue*=town.happinessDeltaMinus;
		}
		
		happinessVal+=changeValue;
		
		//parentWorldObject.addAnimationEvent(AnimationEventType.HAPPINESS, changeValue);
		
		if(happinessVal<0)
			happinessVal=0;
		if(happinessVal>happinessValueMax)
			happinessVal=happinessValueMax;
		
		return changeValue;
	}
		
	public float getHappynessValue()
	{
		return happinessVal;
	}
	
	public void setClothingNaked()
	{
		float r=184f/255f;
		float g=151f/255f;
		float b=77f/255f;
		
		r+=0.2f;
		g+=0.2f;
		b+=0.2f;
		
		clothingColor_Top_Now=new Color(r,g,b,0.4f);
		clothingColor_Bottom_Now=new Color(r,g,b,0.4f);
		clothingColor_Shoes_Now=new Color(r,g,b,0.4f);
	}
	
	public void setClothingBack()
	{
		clothingColor_Top_Now=clothingColor_Top;
		clothingColor_Bottom_Now=clothingColor_Bottom;
		clothingColor_Shoes_Now=clothingColor_Shoes;
	}
		
	public void setRandomClothingColor()
	{
		setRandomClothingColorByType(0); //Top
		setRandomClothingColorByType(1); //Bottom
		setRandomClothingColorByType(2); //Shoes
	}

	public int getSkill_PercentArchitectPlanning(int objectid1)
	{
		float percent=getSkill(objectid1);
		return Math.round(percent/2);
	}
	
	public float getSkill(int objectid1)
	{
		if(jobSkillLevel.containsKey(objectid1))
		{
			return baseWorldObject.thehuman.jobSkillLevel.get(objectid1).fskill;
		}
		
		return 0;
	}
	
	public void setRandomClothingColorByType(int type)
	{
		float r=rand.nextFloat();
		float g=rand.nextFloat();
		float b=rand.nextFloat();
		float fmax=0.5f;
		
		if(type==1)
		{
			float fmax_bottom=0.3f;
			if(r>fmax_bottom)
				r=fmax_bottom;
			if(g>fmax_bottom)
				g=fmax_bottom;
			if(b>fmax_bottom)
				b=fmax_bottom;
		}
		if(type==2)
		{
			float fmax_bottom=0.3f;
			if(r>fmax_bottom)
				r=fmax_bottom;
			if(g>fmax_bottom)
				g=fmax_bottom;
			if(b>fmax_bottom)
				b=fmax_bottom;
		}
		else
		{
			float fmax_bottom=0.4f;
			if(r>fmax_bottom)
				r=fmax_bottom;
			if(g>fmax_bottom)
				g=fmax_bottom;
			if(b>fmax_bottom)
				b=fmax_bottom;
			
			//			if(r>fmax)
			//			{
			//				r-=0.2f;
			//				g=0;
			//				b=0;
			//			}
			//			
			//			if(g>fmax)
			//			{
			//				g-=0.2f;
			//				r=0;
			//				b=0;
			//			}
			//			
			//			if(b>fmax)
			//			{
			//				b-=0.2f;
			//				r=0;
			//				g=0;
			//			}
		}
		
		if(r<0)
			r=0;
		if(g<0)
			g=0;
		if(b<0)
			b=0;
		
		if(type==0)
		{
			clothingColor_Top=new Color(r,g,b,1);
			clothingColor_Top_Now=new Color(r,g,b,1);
		}
		
		if(type==1)
		{
			clothingColor_Bottom=new Color(r,g,b,1);
			clothingColor_Bottom_Now=new Color(r,g,b,1);
		}
		
		if(type==2)
		{
			clothingColor_Shoes=new Color(r,g,b,1);
			clothingColor_Shoes_Now=new Color(r,g,b,1);
		}
	}
	
	public void initDemand()
	{
		return;
				
		/*
		if(baseWorldObject!=null && baseWorldObject.iZombie>=1)
			return;
		
		int irand = rand.nextInt(5);
		if(irand==1)
			sDemandList.add("PUB");
		
		irand = rand.nextInt(5);		
		if(irand==1)
			sDemandList.add("FITNESS_STUDIO");
		
		irand = rand.nextInt(5);		
		if(irand==1)
			sDemandList.add("BREAK_ROOM");
				
		irand = rand.nextInt(5);
		if(irand==1)
			sDemandList.add("CHURCH");
				
		irand = rand.nextInt(3);
		if(irand==1)
			sDemandList.add("DINNER");
		
		irand = rand.nextInt(8);
		if(irand==1)
			sDemandList.add("TV");
		else
		{
			irand = rand.nextInt(8);
			if(irand==1)
				sDemandList.add("TV2");			
		}
		
		irand = rand.nextInt(8);
		if(irand==1)
			sDemandList.add("SPORTSCAR");
		
		irand = rand.nextInt(8);
		if(irand==1)
			sDemandList.add("BOOKSHELF");
		
		
		if(getAge()<15)
		{
			irand = rand.nextInt(8);
			if(irand==1)
				sDemandList.add("SANDPIT");
			
			irand = rand.nextInt(8);
			if(irand==1)
				sDemandList.add("SLIDE");
			
			irand = rand.nextInt(8);
			if(irand==1)
				sDemandList.add("SEESAW");
		}
		
		if(sDemandList.size()>2)
		{
			String sd = sDemandList.get(rand.nextInt(sDemandList.size()));
			sDemandList.clear();
			sDemandList.add(sd);
		}
		*/
	}
	
	public String getDemandSaveString()
	{
		String retstr="";
		
		for(String str : sDemandList)
		{
			if(retstr.length()>0)
				retstr+=",";
			
			retstr+=str;
		}
		
		if(retstr.isEmpty())
			retstr="-";
		
		return retstr;
	}
	
	public void setDemandFromSaveString(String saveString)
	{
		if(saveString.length()>0)
		{
			String[] str1 = saveString.split(",");
			for(String p1 : str1)
			{
				sDemandList.add(p1);
			}
		}		
	}
	
	public CHuman(CTown t, CWorldObject pWorldObject, char sgender)
	{
		influenceList=new ArrayList<Integer>();
		
		town=t;
		rand=new Random();
		baseWorldObject = pWorldObject;
		gender=sgender;
		setRandomClothingColor();
		
		sDemandList=new ArrayList<String>();
		
		ageSeconds=new BigDecimal(0);
		
		workplaces = new Hashtable<Integer, CWorldObject>();
		taskobjects = new Hashtable<Integer, CWorldObject>();
		jobSkillLevel = new Hashtable<Integer, CJobSkillClass>();
		
		iLaundry=0;
		iDecor=0;
		iGarbage=0;
		
		bIsCold=false;
		bIsDark=false;
		bDeceasedInReach=false;
		
		hourSeconds=0;
		minuteSeconds=0;
		
		setHealthValue(100);
		healthValueMax=100;
		setHappynessValue(100);
		happinessValueMax=100;
				
		
		fitnessValueMax=200;
		fitnessValue=100;
		intelligenceValue=100;
		educationValue=1;
		healthAttitude=1;
		positiveAttitude=1;
		
		sleepValueTrigger=3600*16; // 16 Stunden wach -> 8 Stunden Schlaf, Zufriedenheit--, Gesundheit--
		sleepValueTrigger2=3600*25;
		sleepValueTrigger3=3600*35;
		sleepValueTriggerRed=3600*24*2; // 2 Tage
		sleepValueTriggerMax=3600*24*14; // 14 Tage
		
		energyValue=100;
		energyValueTrigger=60;
		energyValueTriggerRed=40;
		energyValueTriggerMax=20;
		
		cleanValueTrigger=3600*(15+rand.nextInt(40)); // 1x/Tag duschen
		cleanValueTrigger2=cleanValueTrigger*1.5f;
		cleanValueTriggerRed=cleanValueTrigger*2; //3600*24*2; // 2 Tage
		cleanValueTriggerMax=cleanValueTrigger*20; //3600*24*30; // 30 Tage
		
		clothingValueTrigger=3600*24*2;
		clothingValueTrigger2=3600*24*3.5f;
		clothingValueTriggerRed=3600*24*5;
		clothingValueTriggerMax=3600*24*8;
		
		eatValueTrigger=3600*4; // alle 4 Stunden
		eatValueTrigger2=3600*12; // alle 4 Stunden
		eatValueTrigger3=3600*18; // alle 4 Stunden
		eatValueTriggerRed=3600*24*1; // 1 Tage
		eatValueTriggerMax=3600*24*30; // 30 Tage
		
		toiletValueTrigger=3600*4; //alle 4 Stunden
		toiletValueTrigger2=3600*8; //alle 4 Stunden
		toiletValueTrigger3=3600*12; //alle 4 Stunden
		toiletValueTriggerRed=3600*24; // 1 Tag
		toiletValueTriggerMax=3600*24*2; // 2 Tage
		
		alcoholLevel=0;
		coffeinLevel=0;
		fruitLevel=0;
		sick=0;
		sickType=0;
		doctorHealingValue=0;
		
		workValueTrigger=60*5; // 5 Minuten Pause / Stunde
		
		if(town.gameWorld.bDebugActions)
		{
			sleepValueTrigger=1000;
			sleepValueTriggerRed=2000;
			sleepValueTriggerMax=3000;
			
			cleanValueTrigger=1000;
			cleanValueTriggerRed=2000;
			cleanValueTriggerMax=3000;
			
			clothingValueTrigger=1000;
			clothingValueTriggerRed=2000;
			clothingValueTriggerMax=3000;
			
			eatValueTrigger=1000;
			eatValueTriggerRed=2000;
			eatValueTriggerMax=3000;
			
			toiletValueTrigger=1000;
			toiletValueTriggerRed=2000;
			toiletValueTriggerMax=3000;
		}
	}
	
	public void setAge(int years)
	{
		ageSeconds=new BigDecimal(0);
		ageSeconds=ageSeconds.add(new BigDecimal(years*(86400f*town.daysInYear)));
		//ageSeconds=years*(86400f*town.daysInYear);
	}
	public int getAge()
	{
		return (int) Math.round(ageSeconds.doubleValue()/(86400*town.daysInYear));
	}
	public float getAge_Float()
	{
		return ageSeconds.floatValue()/(86400*town.daysInYear);
	}	
	
	public void addWorkplace(CWorldObject place)
	{
		if(workplaces.get(place.uniqueId) != null)
			return;
		
		//if(place.worker!=null)
		workplaces.put(place.uniqueId, place);
	}
	
	public void addTaskobject(CWorldObject obj)
	{
		if(taskobjects.get(obj.uniqueId) != null)
			return;
		
		taskobjects.put(obj.uniqueId, obj);
	}
	
	public Boolean checkIsDark()
	{
		if(baseWorldObject.bIsDead)
			return false;
		
		if(baseWorldObject.activeAction!=null && baseWorldObject.activeAction.bActionMode && baseWorldObject.activeAction.valueType==ValueType.SLEEP)
			return false;
		if(baseWorldObject.activeAction!=null && baseWorldObject.activeAction.bActionMode && baseWorldObject.activeAction.targetActionObject!=null && baseWorldObject.activeAction.targetActionObject.theobject.editoraction.contains("church_workingplace_battlepriest"))
			return false;
		if(baseWorldObject.activeAction!=null && baseWorldObject.activeAction.bActionMode && baseWorldObject.activeAction.targetActionObject!=null && baseWorldObject.activeAction.targetActionObject.theobject.editoraction.contains("company_construction_pickup1_traffic_car"))
			return false;
		
		
		
		float lightvalue = baseWorldObject.town.gameWorld.getAmbientLightValue(baseWorldObject.town.gameWorld.worldTime);
		
		if(lightvalue<0.45f)
		{
			CAddress adr = baseWorldObject.town.gameWorld.getAddressByPoint(baseWorldObject.pos_x(), baseWorldObject.pos_y());
			
			if(adr!=null)
			{
				for(CWorldObject l1 : adr.listWorldObjects)
				{
					if(l1.theobject.editoraction.contains("_light"))
					{
						Circle lc = l1.theobject.getLightZoneCircle();
						lc.radius+=15;
						Circle hc = new Circle();
						hc.radius=5;
						hc.x=baseWorldObject.pos_x();
						hc.y=baseWorldObject.pos_y();
						
						if(Intersector.overlaps(lc, hc))
							return false;
					}
				}
				
				return true;
			}
			else
			{
				for(CWorldObject l1 : town.gameWorld.worldOutdoorLights)
				{
					if(l1.theobject.editoraction.contains("_light"))
					{
						Circle lc = l1.theobject.getLightZoneCircle();
						lc.radius+=15;
						Circle hc = new Circle();
						hc.radius=5;
						hc.x=baseWorldObject.pos_x();
						hc.y=baseWorldObject.pos_y();
						
						if(Intersector.overlaps(lc, hc))
							return false;
					}
				}
			}

			return true;
		}
		
		return false;
	}
	
	private void checkDeceased()
	{
		bDeceasedInReach=false;
		for(CWorldObject deceased : baseWorldObject.town.gameWorld.tempHumansDead.values())
		{
			int distance = CHelper.getEuclidianDistance(baseWorldObject, deceased);
			if(distance<300)
			{
				bDeceasedInReach=true;
				return;
			}
		}
	}
	
	private void checkGarbageAndDecor()
	{
		if(baseWorldObject.bIsDead)
			return;
		
		//if(iDecor>4 && iLaundry>4 && iGarbage>4 && iCup>4)
		if(iDecor>2 && iLaundry>3 && iGarbage>3)
			return;
		
		//Wird in handleattri.. zurückgesetzt
		//iDecor=0;
		//iGarbage=0;
		//iLaundry=0;
		
		CAddress adr = town.gameWorld.getAddressByPoint(baseWorldObject.pos_x(), baseWorldObject.pos_y());
		if(adr!=null)
		{
			int itempgarbage=0;
			int itemplaundry=0;
			
			for(CWorldObject wobj : adr.listWorldObjects) //java.util.ConcurrentModificationException
			{
				if(influenceList.contains(wobj.uniqueId))
					continue;
				
				//Decor
				if(iDecor<3)
				{
					if(wobj.theobject.editoraction.contains("_carpet") || 
							wobj.theobject.editoraction.contains("_plant") ||
							wobj.theobject.editoraction.contains("window")
						)
					{
						int distance = CHelper.getEuclidianDistance(baseWorldObject, wobj);
						if(distance<300)
						{
							iDecor+=0.5f;
							influenceList.add(wobj.uniqueId);
						}
						
						//Gdx.app.debug("", "distance: " + distance + ", idecor: " + iDecor);
						
						if(iDecor>2)
							iDecor=2;
					}
				}

				//Dirty Cups
				//				if(iCup<5)
				//				{
				//					if(wobj.theobject.editoraction.contains("coffeecup") && wobj.objectFilling>0 && wobj.isOccupiedBy==null)
				//					{
				//						int distance = CHelper.getEuclidianDistance(parentWorldObject, wobj);
				//						//Gdx.app.debug("", "garbage distance: " + distance);
				//						if(distance<300)
				//							iCup++;
				//						
				//						if(iCup>5)
				//							iCup=5;
				//					}
				//				}
				
				//Garbage
				if(iGarbage<3)
				{
					if(wobj.theobject.editoraction.contains("recyclingcenter_garbagebag"))
					{
						int distance = CHelper.getEuclidianDistance(baseWorldObject, wobj);
						//Gdx.app.debug("", "garbage distance: " + distance);
						
						if(distance<300)
							itempgarbage++;
						
						//wenn > 6 garbage in der nähe sind wird human beeinflusst
						if(itempgarbage>6 && iGarbage<3)
						{
							influenceList.add(wobj.uniqueId);
							iGarbage++;
						}
					}
				}
				
				//Laundry
				if(iLaundry<3)
				{
					if(wobj.theobject.editoraction.contains("laundryobject"))
					{
						int distance = CHelper.getEuclidianDistance(baseWorldObject, wobj);
						
						if(distance<300)
							itemplaundry++;
						
						//wenn > 6 garbage in der nähe sind wird human beeinflusst
						if(itemplaundry>5 && iLaundry<3)
						{
							influenceList.add(wobj.uniqueId);
							iLaundry++;
						}
					}
				}
			}
		}
	}
	
	private Boolean checkIsCold()
	{
		if(baseWorldObject.bIsDead)
			return false;
		
		//wenn Nov,Dez,Jan,Feb
		if(baseWorldObject.town.gameWorld.worldTime.day==11 || baseWorldObject.town.gameWorld.worldTime.day==12 || baseWorldObject.town.gameWorld.worldTime.day==1 || baseWorldObject.town.gameWorld.worldTime.day==2)
		{
			//wenn action ausgeführt wird und targetactionobject ist houseobject
			if(baseWorldObject.activeAction!=null && 
					baseWorldObject.activeAction.bActionMode && 
					baseWorldObject.activeAction.targetActionObject!=null && 
					baseWorldObject.activeAction.targetActionObject.isHouseObject() &&
					!baseWorldObject.activeAction.targetActionObject.theobject.editoraction.contains("company_church_workingplace_battlepriest") &&
					!baseWorldObject.activeAction.targetActionObject.theobject.editoraction.contains("company_construction_pickup1_traffic_car") &&
					!baseWorldObject.activeAction.targetActionObject.theobject.editoraction.contains("company_supermarket_shelf") &&
					!baseWorldObject.activeAction.targetActionObject.theobject.roomtype.contains("outside") &&
					!baseWorldObject.activeAction.targetActionObject.isMaintenanceObject() &&
					baseWorldObject.activeAction.actionMode!=ActionMode.TAKE_OUT_GARBAGE &&
					baseWorldObject.activeAction.actionMode!=ActionMode.LAUNDRY
				)
			{
				//if(baseWorldObject.activeAction.actionMode==ActionMode.CHANGE_CLOTHES || 
				//		baseWorldObject.activeAction.actionMode==ActionMode.
				//		)
				
				//Gdx.app.setLogLevel(10);
				
				//if(town.gameWorld.markerObject!=null)
				//	Gdx.app.log("", "1: "+town.gameWorld.markerObject.theroomid);
				
				if(baseWorldObject.activeAction.targetActionObject.theroom!=null)
				{
					for(CWorldObject wobj : baseWorldObject.activeAction.targetActionObject.theaddress.listWorldObjects)
					{
						//if(wobj.theobject.editoraction.contains("radiator"))
						//		Gdx.app.log("", "1: "+wobj.theroomid);
						
						if(wobj.theobject.editoraction.contains("radiator") && wobj.bObjectIsReady && wobj.theroomid==baseWorldObject.activeAction.targetActionObject.theroomid)
						{
							return false;
						}
					}
				}
				
				return true;
				/*
				if(baseWorldObject.ziel_x>0 && CHelper.getEuclidianDistance(baseWorldObject.pos_x(), baseWorldObject.pos_y(), baseWorldObject.ziel_x, baseWorldObject.ziel_y)>400)
					return false;
				
				if(baseWorldObject.activeAction.targetActionObject.theaddress!=null)
				{
					for(CWorldObject wobj : baseWorldObject.activeAction.targetActionObject.theaddress.listWorldObjects)
					{
						if(wobj.theobject.editoraction.contains("radiator"))
						{
							float[] verts = wobj.theobject.getRadiatorZonePolygon().getTransformedVertices();
							
							//int dist = CHelper.getEuclidianDistance(wobj, baseWorldObject);
							//if(baseWorldObject.uniqueId==176)
							//	Gdx.app.debug(""+baseWorldObject.uniqueId, "dist: "+dist + ", radiator: " + wobj.theobject.getRadiatorHeatingPower() + ", id: " + wobj.uniqueId);
							//if(dist<wobj.theobject.getRadiatorHeatingPower())
							if(Intersector.isPointInPolygon(verts, 0, verts.length, baseWorldObject.pos_x()+baseWorldObject.width_human()/2, baseWorldObject.pos_y()+baseWorldObject.height_human()/2))
							{
								return false;
							}
						}
					}
					
					return true;
				}
				*/
			}
		}
		
		return false;
	}
	
	public void setHumanDead()
	{
		//Alle Mitbewohner Happiness--
		if(baseWorldObject.theaddress!=null)
		{
			for(CWorldObject wobj : baseWorldObject.theaddress.listWorldObjects)
			{
				if(wobj.isHuman() && !wobj.bIsDead && wobj.uniqueId!=baseWorldObject.uniqueId)
				{
					float changehap = wobj.thehuman.changeHappinessValue(-50);
					wobj.addAnimationEvent(AnimationEventType.HAPPINESS, changehap);
				}
			}
		}
		
		baseWorldObject.cancelAction1();
		baseWorldObject.bIsDead=true;
		baseWorldObject.theobject.zorder=2;		
		baseWorldObject.unlinkResident();
		baseWorldObject.actionanim1=0;
		baseWorldObject.actionanim2=0;
		
		if(baseWorldObject.actionstring1.contains("show_grave") || baseWorldObject.actionstring1.contains("show_coffin"))
			return;
		
		String sadr="-";
		if(baseWorldObject.theaddress!=null)
			sadr=baseWorldObject.theaddress.addressName;
		
		String sjobs="-";
		sjobs = getAllJobsTitleString();
		if(sjobs.length()==0)
			sjobs="-";
		
		String stasks="-";
		stasks = getAllTasksTitleString();
		if(stasks.length()==0)
			stasks="-";
		
		String stext=getName() + " (" + sadr + ", " + sjobs +", " + stasks +  ")";
		
		if(gender=='m')
			stext+=" lost his life at age " + getAge();
		if(gender=='w')
			stext+=" lost her life at age " + getAge();
		
		if(getAge()<60)
		{
			int penalty=300000/getAge();
			baseWorldObject.town.gameWorld.infoEvents.add(new CInfoTextEvent(town, stext, "$-" + penalty, new Color(0.7f,0,0,1f)));
			
			baseWorldObject.town.gameWorld.changeTownMoney(-penalty);
			town.gameWorld.townStatistics.getCurrentStatistics_Finance().deceased+=Math.abs(penalty);
		}
		else
		{
			baseWorldObject.town.gameWorld.infoEvents.add(new CInfoTextEvent(town, stext, new Color(0.7f,0,0,1f)));			
		}
	}
	
	public static float getCriticalCoffeinLevel(int level)
	{
		if(level==0) //too much
			return 0.4f;
		
		if(level==1) //overdose
			return 0.75f;
		
		return 0;
	}
		
//	public Boolean demandIsOpen(String sDemand)
//	{
//		for(String str : sDemandList)
//		{
//			if(str.toUpperCase().equals(sDemand.toUpperCase()))
//				return true;
//		}
//		
//		return false;
//	}
	
	public ArrayList<String> getOpenDemandList()
	{
		//Demand zurückgeben der derzeit nicht erfüllt wird
		ArrayList<String> list = new ArrayList<String>();
		
		if(getAge()>14)
		{
			sDemandList.remove("SANDPIT");
			sDemandList.remove("SLIDE");
			sDemandList.remove("SEESAW");
		}
		
		for(String str : sDemandList)
		{
			int startingday = 20000;

			if(getAge()<18)
			{
				if(str.equals("FITNESS_STUDIO") || str.equals("PUB") || str.equals("CHURCH") || str.equals("BREAK_ROOM") || str.equals("SPORTSCAR"))
					continue;
			}
						
			if(str.equals("FITNESS_STUDIO"))
				startingday = town.demandStartingDay_fitnessstudio;
			
			if(str.equals("PUB"))
				startingday = town.demandStartingDay_pub;
			
			if(str.equals("CHURCH"))
				startingday = town.demandStartingDay_church;
			
			if(str.equals("BREAK_ROOM"))
			{
				if(isWorking())
					startingday = town.demandStartingDay_breakroom;
			}
			
			if(str.equals("DINNER"))
				startingday = town.demandStartingDay_dinner;
			if(str.equals("TV"))
				startingday = town.demandStartingDay_tv;
			if(str.equals("TV2"))
				startingday = town.demandStartingDay_tv2;
			if(str.equals("SPORTSCAR"))
				startingday = town.demandStartingDay_sportscar;
			if(str.equals("BOOKSHELF"))
				startingday = town.demandStartingDay_book;
									
			if(str.equals("SANDPIT"))
				startingday = town.demandStartingDay_sandpit;
			if(str.equals("SLIDE"))
				startingday = town.demandStartingDay_slide;
			if(str.equals("SEESAW"))
				startingday = town.demandStartingDay_seesaw;
						
			if(town.gameWorld.worldTime.getCurrentDay()<startingday)
				continue;
			
			Boolean bavailable=false;
			
			for(CCompany comp : town.gameWorld.worldCompanyList)
			{
				if(str.equals("SANDPIT") || str.equals("SLIDE") || str.equals("SEESAW"))
				{
					for(CWorldObject wobj : comp.address_company.listWorldObjects)
					{
						if(wobj.theobject.editoraction.toUpperCase().contains(str))
						{
							bavailable=true;
							break;
						}
					}
				}
				else if(comp.companyType.toString().toUpperCase().equals(str.toUpperCase()))
				{
					bavailable=true;
					break;
				}
				
				if(bavailable)
					break;
			}
						
			if(str.equals("BREAK_ROOM"))
			{
				for(CWorldObject wobj : workplaces.values())
				{
					if(wobj.theaddress!=null)
					{
						for(CWorldObject wobj2 : wobj.theaddress.listWorldObjects_Floors)
						{
							if(wobj.theobject.editoraction.toLowerCase().contains("breakroom"))
							{
								bavailable=true;
								break;
							}
						}
					}
					
					if(bavailable)
						break;
				}
			}
			
			if(str.equals("TV") || str.equals("TV2") || str.equals("BOOKSHELF"))
			{
				if(baseWorldObject.theaddress!=null)
				{
					for(CWorldObject wobj : baseWorldObject.theaddress.listWorldObjects)
					{
						if(str.equals("TV") && wobj.theobject.editoraction.contains("livingroom_tv"))
						{
							bavailable=true;
							break;							
						}
						
						if(str.equals("TV2") && wobj.theobject.editoraction.contains("livingroom_tv2"))
						{
							bavailable=true;
							break;							
						}
						
						if(str.equals("BOOKSHELF") && wobj.theobject.editoraction.contains("livingroom_bookshelf1"))
						{
							bavailable=true;
							break;							
						}
					}
				}
			}
			
			//Diningtable: Taskobjects
			if(str.equals("DINNER"))
			{
				for(CWorldObject tobj : taskobjects.values())
				{
					if(tobj.theobject.editoraction.contains("diningroom_diningtable"))
					{
						if(tobj.worker!=null)
						{
							bavailable=true;
							break;							
						}
					}
				}
			}
			
			if(str.equals("SPORTSCAR"))
			{
				if(car!=null && car.theobject.editoraction.contains("traffic_car_residential_sports"))
				{
					bavailable=true;
					break;
				}
			}
			
			if(!bavailable)
				list.add(str);
		}
		
		return list;
	}
	
	public float getDemandValue()
	{
		float fdemand=0;
		
		for(String str : sDemandList)
		{
			int startingday=20000;
			if(str.equals("FITNESS_STUDIO"))
				startingday=town.demandStartingDay_fitnessstudio;
			if(str.equals("PUB"))
				startingday=town.demandStartingDay_pub;
			if(str.equals("CHURCH"))
				startingday=town.demandStartingDay_church;
			if(str.equals("BREAK_ROOM"))
			{
				if(isWorking())
					startingday=town.demandStartingDay_breakroom;
			}
			
			if(str.equals("TV"))
				startingday=town.demandStartingDay_tv;
			if(str.equals("TV2"))
				startingday=town.demandStartingDay_tv2;
			if(str.equals("BOOKSHELF"))
				startingday=town.demandStartingDay_book;
			if(str.equals("DINNER"))
				startingday=town.demandStartingDay_dinner;
			if(str.equals("SPORTSCAR"))
				startingday=town.demandStartingDay_sportscar;
			if(str.equals("SANDPIT"))
				startingday=town.demandStartingDay_sandpit;
			if(str.equals("SLIDE"))
				startingday=town.demandStartingDay_slide;
			if(str.equals("SEESAW"))
				startingday=town.demandStartingDay_seesaw;
			
			if(town.gameWorld.worldTime.getCurrentDay()<startingday)
				continue;
			
			Boolean bavailable=false;
			for(CCompany comp : town.gameWorld.worldCompanyList)
			{
				if(comp.companyType.toString().toUpperCase().equals(str.toUpperCase()))
				{
					bavailable=true;
					break;
				}
			}
			
			if(!bavailable)
				fdemand-=8;
			//else
			//	fdemand+=3;
		}
		
		return fdemand;
	}
	
	public void handleAttributeValues()
	{
		if(baseWorldObject.bIsDead)
		{
			if(baseWorldObject.iZombie>=1)
			{
				if(baseWorldObject.zombieShowDeadTimer>0)
				{
					baseWorldObject.zombieShowDeadTimer-=CHelper.getDeltaSeconds(baseWorldObject.town);
				}
			}
			
			return;
		}
		
		//Health ganz unten
		if(getHealthValue()<1)
		{
			if(baseWorldObject.iZombie<1)
			{
				setHumanDead();
			}
			else
			{
				//baseWorldObject.addAnimationEvent(AnimationEventType.MONEY, baseWorldObject.thehuman.getWorkOutputPerHour(false, null, false)*1.75f);
				baseWorldObject.zombieDeadFrame=rand.nextInt(5);
				baseWorldObject.actionanim1=0;
				baseWorldObject.actionanim2=0;
				baseWorldObject.bIsDead=true;
				baseWorldObject.theobject.zorder=2;
				baseWorldObject.setRotation(rand.nextInt(360));
				
				if(town.gameCam.frustum.pointInFrustum(baseWorldObject.pos_x()+baseWorldObject.width_human()/2, baseWorldObject.pos_y()+baseWorldObject.height_human()/2, 0))
					town.gameAudio.playSound("EFFECT_ZOMBIEDIE", 0, false);
				
			}
		}
		
		if(baseWorldObject.iZombie>=1)
			return;
		
		
		//Sehr kranker Resident muss ins Bett liegen
		if(isBedSick() && (baseWorldObject.activeAction==null || (baseWorldObject.activeAction!=null && baseWorldObject.activeAction.actionMode!=ActionMode.BED)))
		{
			//Bed triggern
			if(sleepValue<sleepValueTrigger)
				sleepValue=sleepValueTrigger+100;
		}
		
		int ms=60; //minuteseconds
		
		//Alter
		//thehuman.ageSeconds=thehuman.ageSeconds.add(new BigDecimal(CHelper.getDeltaSeconds(gameWorld))); //mapping auf 1 towntag = 1 realmonat
		
		//test mit schnellerem altern faktor 5
		//ageSeconds = ageSeconds.add(new BigDecimal(CHelper.getDeltaSeconds(parentWorldObject.gameWorld)*5));
		//ageSeconds = ageSeconds.add(new BigDecimal(CHelper.getDeltaSeconds(baseWorldObject.gameWorld)*10));
		
		//Residents werden ca 20-25 Tage alt
		//ageSeconds = ageSeconds.add(new BigDecimal(CHelper.getDeltaSeconds(baseWorldObject.gameWorld)*45));
		
		
		//ageSeconds = ageSeconds.add(new BigDecimal(CHelper.getDeltaSeconds(baseWorldObject.town)*80)); // 1 jahr = 3,5 Stunden
																									   // 1 Jahr = 2 Stunden
		
		//---------------------------------------------------------------------------------------------------------------------
		
		float delta=CHelper.getDeltaSeconds(town);
		hourSeconds+=delta;
		minuteSeconds+=delta;
		
		if(minuteSeconds>ms)
		{
			int ageval=200; //1 jahr = 5 Stunden
			if(town.gameGui.buttonX2.toggleActive)
				ageval=400;
			if(town.gameGui.buttonX4.toggleActive)
				ageval=800;
			ageSeconds = ageSeconds.add(new BigDecimal(ageval));
			
			
			if(baseWorldObject.zombieRun>0 && !isWorking("company_church_workingplace_battlepriest"))
			{
				changeEnergyValue(-3f);
			}
			
			bIsCold = checkIsCold();
			bIsDark = checkIsDark();
			checkGarbageAndDecor(); //iGarbage, iDecor
			checkDeceased();
		}
		
		
		//***************
		//Basis Attribute
		//***************
		if(timeForWork()) //pausenzeit wird hochgezählt: 5 minuten
		{
			workValue+=delta;
			
			if(baseWorldObject.town.bDebugLogging)
				Gdx.app.debug("TIMEFORWORK " + workValue, "");
		}
		
		Boolean bSleeping=false;
		if(baseWorldObject.activeActionMode == ActionMode.BED)
			bSleeping=true;
		
		//******************************************************
		//Gesundheit beeinflusst benötigten Schlaf um bis zu 30%
		//******************************************************
		//Resident benötigt mehr Schlaf wenn Gesundheit unten und weniger wenn oben
		
		//Schlaf: 6-10 Stunden
		//Health: 0-200
		
		float lessSleep=0;
		float moreSleep=0;
		
		if(getHealthValue()<80)
			moreSleep=delta/100*10;
		if(getHealthValue()<60)
			moreSleep=delta/100*20;
		if(getHealthValue()<40)
			moreSleep=delta/100*30;
		
		if(getHealthValue()>90)
			lessSleep=delta/100*10;
		if(getHealthValue()>120)
			lessSleep=delta/100*20;
		if(getHealthValue()>150)
			lessSleep=delta/100*30;		
		
		sleepValue+=delta-lessSleep+moreSleep;
		
		//energyValue+=delta;
		
		float eatdelta=delta*town.eatDelta;
		float cleandelta=delta*town.cleanDelta;
		float toiletdelta=delta*town.toiletDelta;
		float clothingdelta=delta*town.clothingDelta;
		
		

		
		//Eat
		if(bSleeping)
			eatValue+=eatdelta/12;
		else
			eatValue+=eatdelta;
		
		//Toilet
		if(eatValue<eatValueTrigger)
			toiletValue+=toiletdelta;
		
		//Clothing
		float clothingdelta2=1;
		float cleandelta2=1;
		if(clothingValue>clothingValueTrigger)
			cleandelta2=1.5f;
		if(clothingValue>clothingValueTriggerRed)
			cleandelta2=2f;
		if(cleanValue>cleanValueTrigger)
			clothingdelta2=1.5f;
		if(cleanValue>cleanValueTriggerRed)
			clothingdelta2=2f;
		clothingValue+=clothingdelta*clothingdelta2;
		cleanValue+=cleandelta*cleandelta2;
		
		
		
		
		
		
		//****************************************
		//Beeinflussung von Faktoren über die Zeit
		//****************************************
		int hs=3600;
		
		if(town.gameWorld.bDebugActions)
			hs=100;
		
		if(hourSeconds>hs)
		{
			float temp_happiness=positiveAttitude/1.5f;
			float temp_health=healthAttitude/1.5f;
			float temp_fitness=0;
			
			if(isWorking()) //happiness geht während der arbeit runter
			{
				temp_happiness-=3;
				
				Boolean bdown=false;
				for(CWorldObject wobj : workplaces.values())
					if(wobj.theobject.editoraction.toLowerCase().contains("office"))
						bdown=true;
				
				if(bdown)
				{
					if(town.gameWorld.worldTime.getCurrentDay()>town.workHappinessDownDay)
						temp_happiness-=1;
					if(town.gameWorld.worldTime.getCurrentDay()>town.workHappinessDownDay+1)
						temp_happiness-=1;
					if(town.gameWorld.worldTime.getCurrentDay()>town.workHappinessDownDay+2)
						temp_happiness-=1;
					if(town.gameWorld.worldTime.getCurrentDay()>town.workHappinessDownDay+3)
						temp_happiness-=1;
					if(town.gameWorld.worldTime.getCurrentDay()>town.workHappinessDownDay+4)
						temp_happiness-=1;
					if(town.gameWorld.worldTime.getCurrentDay()>town.workHappinessDownDay+5)
						temp_happiness-=1;
					if(town.gameWorld.worldTime.getCurrentDay()>town.workHappinessDownDay+6)
						temp_happiness-=1;
					//if(town.gameWorld.worldTime.getCurrentDay()>town.workHappinessDownDay+7)
					//	temp_happiness-=1;
					//if(town.gameWorld.worldTime.getCurrentDay()>town.workHappinessDownDay+8)
					//	temp_happiness-=1;
					//if(town.gameWorld.worldTime.getCurrentDay()>town.workHappinessDownDay+9)
					//	temp_happiness-=1;
				}
			}
			
			float demandValue = getDemandValue();
			temp_happiness+=demandValue;
			
			if(iGarbage>1)
			{
				temp_happiness-=iGarbage;
				iGarbage=0;
			}
			
			//			if(iCup>1)
			//			{
			//				temp_happiness-=iCup;
			//				iCup=0;
			//			}
			
			if(iLaundry>0)
			{
				temp_happiness-=iLaundry;
				iLaundry=0;
			}
			
			if(iDecor>0)
			{
				temp_happiness+=iDecor;
				//Gdx.app.debug("temp_happiness/idecor " + getName(), temp_happiness+", "+iDecor);
				iDecor=0;
			}
			
			influenceList.clear();
			
			fruitLevel-=0.33f;
			if(fruitLevel<0)
				fruitLevel=0;
			
			//Resident ist krank
			if(sick>0)
			{
				if(isWorking())
				{
					if(sickType==0)
						sick-=0.2f;
					if(sickType==1) //severe
						sick-=0.02f;
					if(sickType==2) //contagious
						sick-=0.1f;
				}
				else
				{
					if(sickType==0)
						sick-=0.5f;
					if(sickType==1) //severe
						sick-=0.1f;
					if(sickType==2) //contagious
						sick-=0.4f;
				}
				
				if(bSleeping)
				{
					if(sickType==0)
						sick-=1f;
					if(sickType==1) //severe
						sick-=0.2f;
					if(sickType==2) //contagious
						sick-=0.8f;
				}
				
				sick-=doctorHealingValue;
				
				if(sick<1)
				{
					doctorHealingValue=0;
					sick=0;
				}
				else
					temp_happiness-=sick/10;
			}
			else
			{
				//Resident wird krank
				//health / fitness / happiness unten
				if(town.gameWorld.worldTime.getCurrentDay()>=town.sickStartingDay)
				{
					int iCold=0;
					if(bIsCold)
						iCold=200;
					
					int day=baseWorldObject.town.gameWorld.worldTime.day;
					if(baseWorldObject.bSwimming && day==3||day==4||day==10)
						iCold+=25;
					if(baseWorldObject.bSwimming && day==11||day==2)
						iCold+=50;
					
					if(baseWorldObject.thehuman.energyValue<70) //baseWorldObject.thehuman.energyValueTrigger)
						iCold+=20;
					else if(baseWorldObject.thehuman.energyValue<50) //baseWorldObject.thehuman.energyValueTriggerRed)
						iCold+=30;
					else if(baseWorldObject.thehuman.energyValue<30) //baseWorldObject.thehuman.energyValueTriggerMax)
						iCold+=50;
					
					int sickdiff=100; //mehr: wahrscheinlichkeit geringer dass resident krank wird
					
					if(healthVal<60 || fitnessValue<50 || happinessVal<60 || getCleanPercent()<50)
					{
						int val1=(int) (healthVal+fitnessValue+happinessVal+getCleanPercent()+100-iCold+sickdiff);
						if(val1<1)
							val1=1;
						int rand1 = town.gameWorld.rand.nextInt((int)(val1));
						
						if(rand1<1)
						{
							sick=130-((healthVal+fitnessValue+happinessVal+getCleanPercent())/4);
						}
					}
					else if(healthVal<80 || fitnessValue<80 || happinessVal<80 || getCleanPercent()<60)
					{
						int rand1 = town.gameWorld.rand.nextInt((int)(healthVal+fitnessValue+happinessVal+getCleanPercent()+200-iCold+sickdiff));
						
						if(rand1<1)
						{
							sick=130-((healthVal+fitnessValue+happinessVal+getCleanPercent())/4);
						}
					}
					else
					{
						int rand1 = town.gameWorld.rand.nextInt((int)(healthVal+fitnessValue+happinessVal+getCleanPercent())+300-iCold+sickdiff);
						
						if(rand1<1)
						{
							sick=130-((healthVal+fitnessValue+happinessVal+getCleanPercent())/4);
						}
					}
					
					if(sick>100)
						sick=100;
					
					if(sick>0)
					{
						if(town.gameWorld.worldTime.getCurrentDay()>=town.sickStartingDay_severedisease)
						{
							int r1 = rand.nextInt(Math.round(5*(1+healthAttitude)));
							if(r1==1)
								sickType=1;
						}
						
						if(town.gameWorld.worldTime.getCurrentDay()>=town.sickStartingDay_contagious)
						{
							int r1 = rand.nextInt(Math.round(7*(1+healthAttitude)));
							if(r1==1)
							{
								if(town.gameWorld.getTownHallIntelligenceDeskIsOnline())
								{
									Boolean bNews=true;
									for(CWorldObject wobj : town.gameWorld.worldHumans)
									{
										if(wobj.thehuman.sickType==2)
										{
											bNews=false;
											break;
										}
									}
									
									if(bNews)
										town.gameWorld.infoEvents.add(new CInfoTextEvent(town, "Contagious Disease Warning", "Town Hall Intelligence Desk: ", Color.RED));
								}
								
								sickType=2;
							}
						}
					}
				}
			}
			
			//Resident hat zuviel Kaffee getrunken
			if(coffeinLevel>0)
			{
				if(coffeinLevel<=1f)
					temp_happiness+=5;
				//else if(coffeinLevel<1f)
				//	temp_happiness+=coffeinLevel*7;
				
				
				if(coffeinLevel>CHuman.getCriticalCoffeinLevel(0))
					temp_health-=coffeinLevel;
				
				coffeinLevel-=0.07f;
				
				if(coffeinLevel<0)
					coffeinLevel=0;
			}
			
			//Resident ist betrunken
			if(alcoholLevel>0)
			{
				if(alcoholLevel<2)
					temp_happiness+=alcoholLevel*5;
				
				temp_health-=alcoholLevel;
				
				alcoholLevel-=0.1f;
				
				if(alcoholLevel<0)
					alcoholLevel=0;
			}

			
			if(bDeceasedInReach)
			{
				temp_happiness-=5;
			}
			
			if(bIsCold)
			{
				//temp_happiness-=4;
				//temp_health-=3;
				temp_happiness-=1;
				temp_health-=1;
			}
			
			if(bSleeping)
			{

				changeEnergyValue(15);
				
				temp_happiness+=2;
				temp_health+=2;
				
				if(baseWorldObject.thehuman.bed!=null)
				{
					int count = baseWorldObject.thehuman.bed.theobject.getBedtoOtherBedsZoneCollisionCount(baseWorldObject.theaddress);
					temp_happiness-=count*8;
				}
				
				//changeHappynessValue(2);
				//changeHealthValue(2);
			}
			
			//if(!bSleeping)
			{
				//Attribut gelb
				if(sleepValue>sleepValueTrigger)
				{
					temp_health-=1;
					temp_happiness-=2;
					
					if(sleepValue>sleepValueTrigger2)
					{
						temp_health-=1;
						temp_happiness-=2;
						
						if(sleepValue>sleepValueTrigger3)
						{
							temp_health-=1;
							temp_happiness-=2;
						}
					}
				}
				
				if(energyValue<energyValueTrigger)
				{
					temp_health-=1;
					temp_happiness-=2;
				}
				
				if(eatValue>eatValueTrigger)
				{
					temp_health-=1;
					temp_happiness-=2;
					
					if(eatValue>eatValueTrigger2)
					{
						temp_health-=1;
						temp_happiness-=2;
						
						if(eatValue>eatValueTrigger3)
						{
							temp_health-=1;
							temp_happiness-=2;
						}
					}
				}
				
				if(cleanValue>cleanValueTrigger)
				{
					temp_health-=1;
					temp_happiness-=2;
					
					if(cleanValue>cleanValueTrigger2)
					{
						temp_health-=1;
						temp_happiness-=2;
					}
				}
				
				if(clothingValue>clothingValueTrigger)
				{
					temp_health-=1;
					temp_happiness-=2;
					
					if(clothingValue>clothingValueTrigger2)
					{
						temp_health-=1;
						temp_happiness-=2;
					}
				}
				
				if(toiletValue>toiletValueTrigger)
				{
					temp_health-=1;
					temp_happiness-=2;
					
					if(toiletValue>toiletValueTrigger2)
					{
						temp_health-=1;
						temp_happiness-=2;
						
						if(toiletValue>toiletValueTrigger3)
						{
							temp_health-=1;
							temp_happiness-=2;
						}
					}
				}
				
				
				
				//Attribut rot
				if(sleepValue>sleepValueTriggerRed)
				{
					temp_happiness-=1;
					temp_health-=2;
				}
				if(energyValue<energyValueTriggerRed)
				{
					temp_happiness-=1;
					temp_health-=2;
				}
				if(eatValue>eatValueTriggerRed)
				{
					temp_happiness-=1;
					temp_health-=2;
				}
				if(cleanValue>cleanValueTriggerRed)
				{
					temp_happiness-=1;
					temp_health-=2;
				}
				if(clothingValue>clothingValueTriggerRed)
				{
					temp_happiness-=1;
					temp_health-=2;
				}
				if(toiletValue>toiletValueTriggerRed)
				{
					temp_happiness-=1;
					temp_health-=2;
				}
				
				
				//Attribut Max
				if(sleepValue>=sleepValueTriggerMax)
				{
					temp_happiness-=3;
					temp_health-=3;
				}
				
				if(energyValue<=energyValueTriggerMax)
				{
					temp_happiness-=3;
					temp_health-=3;
				}				
				
				if(eatValue>=eatValueTriggerMax)
				{
					temp_happiness-=3;
					temp_health-=3;
				}
				
				if(cleanValue>=cleanValueTriggerMax)
				{
					temp_happiness-=3;
					temp_health-=3;
				}
				
				if(toiletValue>=toiletValueTriggerMax)
				{
					temp_happiness-=3;
					temp_health-=3;
					//.. erleichtert sich an ort und stelle auf den Boden
				}
				
				//Alle Attribute grün: happiness/health++
				if(sleepValue<sleepValueTrigger2 && 
						eatValue<eatValueTrigger2 && 
						cleanValue<cleanValueTrigger2 && 
						toiletValue<toiletValueTrigger2 && 
						clothingValue<clothingValueTrigger2
					)
				{
					temp_happiness+=0.5f;
					
					if(energyValue>energyValueTriggerRed)
						temp_health+=1;
				}
				
				if(sleepValue<sleepValueTrigger && 
						eatValue<eatValueTrigger && 
						cleanValue<cleanValueTrigger && 
						toiletValue<toiletValueTrigger && 
						clothingValue<clothingValueTrigger
					)
				{
					temp_happiness+=0.5f;
					//temp_health+=1;
					
					if(energyValue>energyValueTrigger)
						temp_health+=1;
				}				
				
				
			}
			
			
			temp_fitness+=-0.2f;
			
			if(fitnessValue<50 && !bSleeping)
				temp_health-=1;
			if(fitnessValue<40 && !bSleeping)
				temp_health-=1.5f;
			if(fitnessValue<30 && !bSleeping)
				temp_health-=2;
			if(fitnessValue<20 && !bSleeping)
				temp_health-=2.5f;
			if(fitnessValue<10 && !bSleeping)
				temp_health-=3;
			
			if(fitnessValue>100)
				temp_health+=1f;
			if(fitnessValue>110)
				temp_health+=1.5f;
			if(fitnessValue>120)
				temp_health+=2f;
			if(fitnessValue>130)
				temp_health+=2.5f;
			if(fitnessValue>140)
				temp_health+=3f;
			
			
			//Age, Health and Fitness
			if(getAge()>65)
			{
				if(getHappynessValue()<50)
					temp_health-=0.2f;
								
				temp_fitness-=0.2f;
			}
			if(getAge()>70)
			{
				if(getHappynessValue()<60)
					temp_health-=0.2f;
				
				temp_fitness-=0.2f;
			}
			if(getAge()>75)
			{
				if(getHappynessValue()<60)
					temp_health-=0.3f;
				
				temp_fitness-=0.2f;
				//changeFitnessValue(-0.2f);
				
				fitnessValue--;
			}
			if(getAge()>80)
			{
				if(getHappynessValue()<70)
					temp_health-=0.4f;
					//changeHealthValue(-0.4f);
				
				temp_fitness-=0.2f;
				//changeFitnessValue(-0.2f);
			}
			if(getAge()>85)
			{
				if(getHappynessValue()<70)
					temp_health-=0.5f;
					//changeHealthValue(-0.5f);
				
				temp_fitness-=0.2f;
				//changeFitnessValue(-0.2f);
			}
			if(getAge()>90)
			{
				//changeHealthValue(-0.8f);
				temp_health-=0.8f;
				
				temp_fitness-=0.2f;
				//changeFitnessValue(-0.2f);
			}		
			
			if(getAge()>100)
			{
				//changeHealthValue(-1);
				temp_health-=1f;
				
				temp_fitness-=0.2f;
				//changeFitnessValue(-0.2f);
			}
			
			if(getAge()>110)
			{
				//changeHealthValue(-1);
				temp_health-=1f;
				
				temp_fitness-=0.2f;
				//changeFitnessValue(-0.2f);
			}
			
			//Im Schlaf: Regeneration
			if(bSleeping && temp_happiness<0)
				temp_happiness/=5;
			
			if(bSleeping && temp_health<0)
				temp_health/=5;
			
			if(bSleeping && temp_fitness<0)
				temp_fitness=0;
			
			
			if(temp_happiness!=0)
				changeHappinessValue(temp_happiness);
			
			if(temp_fitness!=0)
				changeFitnessValue(temp_fitness);
			
			if(temp_health!=0)
				changeHealthValue(temp_health);
		}
		
		
		
		//*************
		//Value Limits
		//*************
		
		//Trigger Limits
		if(sleepValue>sleepValueTriggerMax) 
			sleepValue=sleepValueTriggerMax; 
		if(energyValue<energyValueTriggerMax) 
			energyValue=energyValueTriggerMax;
		if(eatValue>eatValueTriggerMax) 
			eatValue=eatValueTriggerMax;
		if(cleanValue>cleanValueTriggerMax) 
			cleanValue=cleanValueTriggerMax;
		if(toiletValue>toiletValueTriggerMax) 
			toiletValue=toiletValueTriggerMax;
		
		if(hourSeconds>hs)
			hourSeconds=0;
		
		if(minuteSeconds>ms)
			minuteSeconds=0;
				
		if(town.bDebutSleeping)
			sleepValue=sleepValueTrigger+1;
	}
		
	public static String generateCitizenForename(String gender)
	{
		String[] manNames = {"James","Christopher","Ronald","John","Daniel","Anthony","Robert","Paul","Kevin","Jason","William"
				,"Michael","Mark","Jeff","Donald","George","Kenneth","David","Richard","Steven","Charles","Joseph","Edward","Thomas","Brian"};
		
		String[] womanNames = {"Mary","Lisa","Michelle","Patricia","Nancy","Laura","Linda","Sarah","Karen","Barbara","Betty","Kimberly"
				,"Elizabeth","Helen","Deborah","Jennifer","Sandra","Maria","Donna","Susan","Carol","Dorothy","Sharon"};
		
		Random rand = new Random();
		
		if(gender.equals("m"))
		{
			int r2 = rand.nextInt(manNames.length-1);	
			return manNames[r2];
		}
		
		if(gender.equals("w"))
		{
			int r2 = rand.nextInt(womanNames.length-1);	
			return womanNames[r2];			
		}
		
		return "";
	}
	
	public static String generateCitizenLastname()
	{
		String[] lastNames = {"Clark","Wright","Mitchell","Thomas","Rodriguez","Lopez","Perez","Jackson"
				,"Thomas","Rodriguez","Lopez","Perez","Jackson","Lewis","Hill","Roberts","Jones","White","Lee"
				,"Scott","Turner","Brown","Harris","Walker","Green","Phillips","Davis","Martin","Hall","Adams","Campbell"
				,"Miller","Thompson","Allen","Baker","Parker","Wilson","Garcia","Smith","Johnson","Williams","Anderson","Hill"
				,"Young","Gonzalez","Evans","Moore","Martinez","Hernandez","Nelson","Edwards","Taylor","Robinson","King","Carter","Collins"};
		
		Random rand = new Random();
		int r1 = rand.nextInt(lastNames.length-1);
		return lastNames[r1];
	}
	
	public int getWorkOutputPerHour(Boolean bCalculated, CWorldObject workplace1, CWorldObject workplace1Worker,  Boolean bCalculatedForList)
	{
		//ACHTUNG
		
		//Workoutput wird für SKillerhöhung benutzt
		
		int ha=0;
		
		if(getHappynessValue()>100)
			ha=100;
		else 
			ha=(int)getHappynessValue();
		
		int he=0;
		
		if(getHealthValue()>100)
			he=100;
		else
			he=(int)getHealthValue();
		
		int workoutput=0;
		
		workoutput+=intelligenceValue/2f;
		workoutput+=educationValue*10;
		workoutput+=ha/5f;
		workoutput+=he/5f;
		workoutput+=fitnessValue/20f;
		workoutput+=positiveAttitude*5;
		
		if(bCalculated && !bCalculatedForList)
		{
			if(alcoholLevel>0.3f)
				workoutput/=2;
			if(alcoholLevel>1)
				workoutput/=2;
			if(alcoholLevel>2)
				workoutput/=2;
			
			if(coffeinLevel>0.3f)
				workoutput*=1.9f;
				//workoutput*=1.3f;
			else if(coffeinLevel>0.2f)
				workoutput*=1.7f;
				//workoutput*=1.25f;
			else if(coffeinLevel>0.1f)
				workoutput*=1.5f;
				//workoutput*=1.2f;
			else if(coffeinLevel>0)
				workoutput*=1.3f;
				//workoutput*=1.15f;
		}
		
		if(bCalculated && (baseWorldObject.activeAction!=null || workplace1!=null || workplace1Worker!=null))
		{
			CWorldObject workplace = null;
			
			if(workplace1==null && workplace1Worker==null) //aktuelle action
			{
				workplace = baseWorldObject.activeAction.getActiveWorkplaceOrCompanyTaskobject();
				if(workplace!=null)
				{
					int objid = workplace.theobject.getSkillObjectId();
					if(jobSkillLevel.containsKey(objid))
						workoutput += jobSkillLevel.get(objid).fskill/2.3f;
				}
			}
			else if(workplace1!=null) //Nimm Skill von Human -> Liste
			{
				int objid = workplace1.theobject.getSkillObjectId();
				if(jobSkillLevel.containsKey(objid))
					workoutput += jobSkillLevel.get(objid).fskill/2.3f;
			}
			else if(workplace1Worker!=null) //Nimm skill von aktuell hinterlegtem worker -> Action zb skill von lehrer für schüler workoutput
			{
				int objid = workplace1Worker.theobject.getSkillObjectId();
				if(workplace1Worker.worker.thehuman.jobSkillLevel.containsKey(objid))
					workoutput += workplace1Worker.worker.thehuman.jobSkillLevel.get(objid).fskill/2.3f;
			}
		}
		
		if(bCalculated && !bCalculatedForList)
		{
			workoutput-=sick;
			
			int energyvalue=(int) (100-baseWorldObject.thehuman.energyValue);
			workoutput-=energyvalue;
			
			if((bIsDark || bIsCold || bDeceasedInReach) && workoutput>0)
			{
				if(bIsDark)
					workoutput/=4;
				
				if(bIsCold)
					workoutput/=2;
				
				if(bDeceasedInReach)
					workoutput/=2;
			}
		}
		
		if(workoutput<0)
			workoutput=0;
		
		workoutput/=2; 
		//ACHTUNG: je höher workoutput desto höher der einfluss der faktoren
			//daher workoutput möglichst hoch ansetzen?
		
		//Max Workoutput by Age
		int maxwo=1000;
		
		if(getAge()>11 && getAge()<16)
			workoutput/=1.3f;
		if(getAge()>6 && getAge()<12)
			workoutput/=1.6f;
		if(getAge()<7)
			workoutput/=2;
		
		if(workoutput>maxwo)
			workoutput=maxwo;		
		
		return workoutput;
	}
	
	public TextureRegion getAction1Frame(int iFrame)
	{
		//Arme vorne, normale breite
		
		if(iFrame==0) //nur Arme vorne, gerade
			return baseWorldObject.town.gameWorld.gameResourceConfig.animations.get("human_action1").getKeyFrames()[0];
		
		if(iFrame==2) //nur Arme vorne, weit offen
			return baseWorldObject.town.gameWorld.gameResourceConfig.animations.get("human_action1").getKeyFrames()[1];
		
		if(iFrame==1) //Arme arbeiten vorne
			return baseWorldObject.town.gameWorld.gameResourceConfig.animations.get("human_action1").getKeyFrame(CHelper.getDeltaSeconds(baseWorldObject.town)+baseWorldObject.town.gameWorld.stateTime+rand.nextFloat()/10, true);

		if(iFrame==9) //Zombiearme arbeiten vorne
			return baseWorldObject.town.gameWorld.gameResourceConfig.animations.get("human_action1_z").getKeyFrame(CHelper.getDeltaSeconds(baseWorldObject.town)+baseWorldObject.town.gameWorld.stateTime+rand.nextFloat()/10, true);
		
		//Default: Arme arbeiten vorne
		return baseWorldObject.town.gameWorld.gameResourceConfig.animations.get("human_action1").getKeyFrame(CHelper.getDeltaSeconds(baseWorldObject.town)+baseWorldObject.town.gameWorld.stateTime+rand.nextFloat()/10, true);
	}
	
	public TextureRegion getAction2Frame(int iFrame)
	{
		//parentWorldObject.gameWorld.gameResourceConfig.animations.get("human_action2").setFrameDuration(0.3f);
		
		if(iFrame==0) //Ein Arm arbeitet vorne
			return baseWorldObject.town.gameWorld.gameResourceConfig.animations.get("human_action2").getKeyFrame(CHelper.getDeltaSeconds(baseWorldObject.town)+baseWorldObject.town.gameWorld.stateTime+rand.nextFloat()/10, true);

		if(iFrame==1) //Ein Arm vorne 1
			return baseWorldObject.town.gameWorld.gameResourceConfig.animations.get("human_action2").getKeyFrames()[0];
		
		if(iFrame==2) //Ein Arm vorne 2
			return baseWorldObject.town.gameWorld.gameResourceConfig.animations.get("human_action2").getKeyFrames()[1];
		
		
		//Default: Ein Arm arbeitet vorne
		return baseWorldObject.town.gameWorld.gameResourceConfig.animations.get("human_action2").getKeyFrame(CHelper.getDeltaSeconds(baseWorldObject.town)+baseWorldObject.town.gameWorld.stateTime+rand.nextFloat()/10, true);
	}
	
	public TextureRegion getAction3Frame(int iFrame) 
	{
		//Arme vorne, breit
				
		if(iFrame==2) //nur Arme vorne, gerade
			return baseWorldObject.town.gameWorld.gameResourceConfig.animations.get("human_action3").getKeyFrames()[1];

		if(iFrame==1) //Arme arbeiten vorne
			return baseWorldObject.town.gameWorld.gameResourceConfig.animations.get("human_action3").getKeyFrame(CHelper.getDeltaSeconds(baseWorldObject.town)+baseWorldObject.town.gameWorld.stateTime+rand.nextFloat()/10, true);

		
		if(iFrame==0) //nur Arme vorne, weit offen
			return baseWorldObject.town.gameWorld.gameResourceConfig.animations.get("human_action3").getKeyFrames()[0];
		
		
		//Default: Arme arbeiten vorne
		return baseWorldObject.town.gameWorld.gameResourceConfig.animations.get("human_action3").getKeyFrame(CHelper.getDeltaSeconds(baseWorldObject.town)+baseWorldObject.town.gameWorld.stateTime+rand.nextFloat()/10, true);
	}
	
	public TextureRegion getAction4Frame(int iFrame) 
	{
		if(iFrame==0) //nur Arme vorne, weit offen
			return baseWorldObject.town.gameWorld.gameResourceConfig.animations.get("human_action4").getKeyFrames()[0];
		
		if(iFrame==1) //nur Arme vorne, offen
			return baseWorldObject.town.gameWorld.gameResourceConfig.animations.get("human_action4").getKeyFrames()[1];
		
		if(iFrame==2) //Arme arbeiten vorne
			return baseWorldObject.town.gameWorld.gameResourceConfig.animations.get("human_action4").getKeyFrame(CHelper.getDeltaSeconds(baseWorldObject.town)+baseWorldObject.town.gameWorld.stateTime+rand.nextFloat()/10, true);
		
		
		//Default: Arme arbeiten vorne
		return baseWorldObject.town.gameWorld.gameResourceConfig.animations.get("human_action4").getKeyFrame(CHelper.getDeltaSeconds(baseWorldObject.town)+baseWorldObject.town.gameWorld.stateTime+rand.nextFloat()/10, true);
	}
	
	public TextureRegion getAction5Frame(int iFrame) 
	{
		if(iFrame==0) //Arme vor zurück
			return baseWorldObject.town.gameWorld.gameResourceConfig.animations.get("human_action7").getKeyFrame(CHelper.getDeltaSeconds(baseWorldObject.town)+baseWorldObject.town.gameWorld.stateTime+rand.nextFloat()/10, true);

		if(iFrame==1) //Arme hinten
			return baseWorldObject.town.gameWorld.gameResourceConfig.animations.get("human_action7").getKeyFrames()[1];
		
		if(iFrame==2) //Arme vorne
			return baseWorldObject.town.gameWorld.gameResourceConfig.animations.get("human_action7").getKeyFrames()[0];
		
		//Default: Arme arbeiten vorne
		return baseWorldObject.town.gameWorld.gameResourceConfig.animations.get("human_action7").getKeyFrame(CHelper.getDeltaSeconds(baseWorldObject.town)+baseWorldObject.town.gameWorld.stateTime+rand.nextFloat()/10, true);
	}
	
	public TextureRegion getActionRepairFrame(int iFrame) 
	{
		if(iFrame==0)
			return baseWorldObject.town.gameWorld.gameResourceConfig.animations.get("human_action_repair").getKeyFrame(CHelper.getDeltaSeconds(baseWorldObject.town)+baseWorldObject.town.gameWorld.stateTime+rand.nextFloat()/10, true);

		if(iFrame==1)
			return baseWorldObject.town.gameWorld.gameResourceConfig.animations.get("human_action_repair").getKeyFrames()[0];
		
		if(iFrame==2)
			return baseWorldObject.town.gameWorld.gameResourceConfig.animations.get("human_action_repair").getKeyFrames()[1];
		
		//Default: Arme arbeiten vorne
		return baseWorldObject.town.gameWorld.gameResourceConfig.animations.get("human_action_repair").getKeyFrame(CHelper.getDeltaSeconds(baseWorldObject.town)+baseWorldObject.town.gameWorld.stateTime+rand.nextFloat()/10, true);
	}
	
//	public String getWorkingHoursString()
//	{
//		String str="";
//		Enumeration<CWorldObject> elist = workplaces.elements();
//		while(elist.hasMoreElements())
//		{
//			str+=", "+elist.nextElement().getWorkingHoursString();
//		}
//		
//		if(str.length()>1)
//			str=str.substring(2, str.length()-2);
//		
//		//if(workPlace1!=null)
//		//	return workPlace1.getWorkingHoursString();
//		
//		return str;
//	}
	
//	public String getCompanyName()
//	{
//		String str="";
//		Enumeration<CWorldObject> elist = workplaces.elements();
//		while(elist.hasMoreElements())
//		{
//			CWorldObject el = elist.nextElement();
//			if(el.belongsToCompany!=null)
//				str+=", " + el.belongsToCompany.companyname;
//		}
//				
//		if(str.length()>1)
//			str=str.substring(2, str.length()-2);
//		
////		if(workPlace1!=null)
////			return workPlace1.belongsToCompany.companyname;
//		
//		return str;
//	}
	
//	public String getJobTitle()
//	{
//		String str="";
//		Enumeration<CWorldObject> elist = workplaces.elements();
//		
//		while(elist.hasMoreElements())
//		{
//			CWorldObject el = elist.nextElement();
//				str+=", " + el.getCompanyWorkingPlaceJobTitle();
//		}
//		if(str.length()>1)
//			str=str.substring(2, str.length()-2);
//
//		//if(workPlace1!=null)
//		//	return workPlace1.getCompanyWorkingPlaceJobTitle();
//		
//		return str;
//	}
	
	public int getHighPrioActionPriority(String actionstring, CAction action1)
	{
		//0: no high prio action
		//1-X: prio der high prio action
		int prio=0;
		
		//		if(actionstring.isEmpty())
		//			Gdx.app.debug("", "actionstring: " + actionstring);
		//		if(action1!=null)		
		//			Gdx.app.debug("", "action1: " + action1.actionMode);
		
		
		if(action1!=null && action1.actionMode==ActionMode.FUNERAL_ATTEND)
			prio = 1;
		
		if(action1!=null && action1.actionMode==ActionMode.GOTO_DOCTOR)
			prio = 2;
		
		if(!actionstring.isEmpty() && actionstring.contains("company_urbancemetery_rostrum"))
			prio = 2;
		
		if(!actionstring.isEmpty() && actionstring.contains("company_urbancemetery_hearse_traffic_car"))
			prio = 1;
				
		//		if(action1.targetActionObject!=null)
		//		{
		//			if(action1.targetActionObject.theobject.editoraction.contains("company_urbancemetery_rostrum")) //Funeral Speaker
		//			{
		//				prio = 2;
		//			}
		//			
		//			if(action1.targetActionObject.theobject.editoraction.contains("company_urbancemetery_hearse_traffic_car")) //Hearse Driver
		//			{
		//				prio = 1;
		//			}
		//		}
		
		return prio;
	}
	
	public CAction getHighPrioActionByTrigger()
	{
		//Aktivitäten die alle anderen Aktivitäten canceln
		
		//Trigger-Actions nicht nachts triggern
		CTime time1 = baseWorldObject.town.gameWorld.worldTime;
		if((time1.hours>18 && time1.hours<=23) || (time1.hours>=0 && time1.hours<=6))
			return null;
		
		//Resident is ill
		if(sick>0 && doctorHealingValue==0)
		{
			CCompany comp = CCompany.getNextActiveCompany(CompanyType.DOCTORS_OFFICE, baseWorldObject.town, baseWorldObject.pos_x(), baseWorldObject.pos_y(), baseWorldObject.uniqueId, 99999);
			if(comp!=null)
			{
				return baseWorldObject.action_gotodoctor;
			}
		}
		
		//-------------------------------------------------------------------------------------------
		//-------------------------------------------------------------------------------------------
		
		CAction actionTemp = null;
		String tempobj="";
		
		//Check Taskhumans
		if(baseWorldObject.theaddress!=null)
		{
			for(CWorldObject wobj : baseWorldObject.theaddress.listWorldObjects)
			{
				//Attend Funeral
				if(baseWorldObject.action_funeralattend.iActionBlocker<1)
				{
					if(wobj.isHuman() && wobj.bIsDead && wobj.actionstring1.equals("show_coffin"))
					{
						Boolean bSetAction=false;
						int prio1 = getHighPrioActionPriority(wobj.theobject.editoraction, null);
						
						if(!tempobj.isEmpty())
						{
							int priotemp = getHighPrioActionPriority(tempobj, null);
							if(prio1>priotemp)
							{
								tempobj=wobj.theobject.editoraction;
								bSetAction=true;
							}
						}
						else
						{
							bSetAction=true;
						}
						
						if(bSetAction)
						{
							tempobj=wobj.theobject.editoraction;
							actionTemp=baseWorldObject.action_funeralattend;
							actionTemp.targetActionObject=wobj;
							actionTemp.targetActionObject10=wobj;
						}
					}
				}
			}
		}
		
		
		//Check Taskobjects
		for(CWorldObject wobj : taskobjects.values())
		{
			//Funeral Speaker
			if(wobj.theobject.editoraction.contains("company_urbancemetery_rostrum"))
			{
				if(wobj.actionstring1.equals("trigger_funeral") 
						&& wobj.belongsToCompany!=null 
						&& wobj.belongsToCompany.isActive(-1))
				{
					Boolean bSetAction=false;
					int prio1 = getHighPrioActionPriority(wobj.theobject.editoraction, null);
					if(!tempobj.isEmpty())
					{
						int priotemp = getHighPrioActionPriority(tempobj, null);
						if(prio1>priotemp)
						{
							tempobj=wobj.theobject.editoraction;
							bSetAction=true;
						}
					}
					else
					{
						bSetAction=true;
					}
					
					if(bSetAction)
					{
						tempobj=wobj.theobject.editoraction;
						actionTemp=baseWorldObject.action_Workplace;
						actionTemp.targetActionObject=wobj;
						actionTemp.targetActionObject10=wobj;
					}
				}
			}
			
			
			//Hearse
			if(wobj.theobject.editoraction.contains("company_urbancemetery_hearse_traffic_car"))
			{
				Boolean bCheck=true;
				for(CWorldObject wobj2 : wobj.belongsToCompany.address_company.listWorldObjects)
				{
					if(wobj2.actionstring1.contains("show_coffin_move"))
						bCheck=false;
				}
				
				if(bCheck)
				{
					for(CWorldObject wh : baseWorldObject.town.gameWorld.tempHumansDead.values())
					{
						//funktion wird auch dazu benutzt zu prüfen ob es Zeit für Arbeit ist, daher muss hier auch geprüft werden ob das objekt vom human occupied ist, ansonsten würde die action abgebrochen werden
						if((wh.actionstring1.isEmpty() || wh.actionstring1.equals("-")) && (wh.isOccupiedBy==null || (wh.isOccupiedBy!=null && wh.isOccupiedBy.uniqueId==baseWorldObject.uniqueId)))
						{
							Boolean bSetAction=false;
							int prio1 = getHighPrioActionPriority(wobj.theobject.editoraction, null);
							if(!tempobj.isEmpty())
							{
								int priotemp = getHighPrioActionPriority(tempobj, null);
								if(prio1>priotemp)
								{
									tempobj=wobj.theobject.editoraction;
									bSetAction=true;
								}
							}
							else
							{
								bSetAction=true;
							}
							
							if(bSetAction)
							{
								tempobj=wobj.theobject.editoraction;
								actionTemp=baseWorldObject.action_Workplace;
								actionTemp.targetActionObject=wobj;
								actionTemp.targetActionObject10=wobj;
							}						
						}
					}
				}
			}
		}
		
		//if(actionTemp!=null && actionTemp.targetActionObject!=null)
		//	Gdx.app.debug("", "getHighPrioActionByTrigger: " + actionTemp.actionMode + ", " + actionTemp.targetActionObject.theobject.editoraction + ", id: " + parentWorldObject.uniqueId);
				
		if(actionTemp!=null)
		{
			//if(actionTemp.targetActionObject!=null)
			//	Gdx.app.debug("", "force action: " + actionTemp.actionMode + ", " + actionTemp.targetActionObject.theobject.editoraction);
		}
		
		return actionTemp;
	}
	
	public String canWorkText()
	{
		//if(isVerySick())
		if(!canWork())
		{
			if(sick>0)
				return "feels too sick to work";
			else
				return "bad health";
		}
		//if(getHealthValue()<40)
		//	return "is in poor health";
		
		return "";
	}
	
	public Boolean isBedSick()
	{
		if(sickType>0)
			return true;
		
		int svalue=0;

		if(healthAttitude>=0.5f)
			svalue=85;
		if(healthAttitude>=1)
			svalue=80;
		if(healthAttitude>=1.5f)
			svalue=75;
		if(healthAttitude>=2)
			svalue=70;
		if(healthAttitude>=2.5f)
			svalue=65;
		
		if(sick>=svalue)
			return false;
		
		return false;
	}
	
	public Boolean canWork()
	{
		//Gdx.app.debug("", ""+sickType + ", " + sick + ", " + getHealthValue());
		
	
		if(sickType>0)
			return false;
		
		if(sick>0)
			return false;
		
		//Sick Value ab dem nicht mehr gearbeitet wird
//		int svalue=1;
//		if(healthAttitude>=0.5f)
//			svalue=70;
//		if(healthAttitude>=1)
//			svalue=65;
//		if(healthAttitude>=1.5f)
//			svalue=60;
//		if(healthAttitude>=2)
//			svalue=55;
//		if(healthAttitude>=2.5f)
//			svalue=50;
//		if(sick>=svalue)
//			return false;
		
		//Health Value ab dem nicht mehr gearbeitet wird
		int value=0;
		if(healthAttitude>=0.5f)
			value=20;
		if(healthAttitude>=1)
			value=30;
		if(healthAttitude>=1.5f)
			value=40;
		if(healthAttitude>=2)
			value=50;
		if(healthAttitude>=2.5f)
			value=60;		
		if(getHealthValue()<value)
			return false;
		
		return true;
	}
	
	public CWorldObject getWorkTaskPlaceByTrigger()
	{
		//******************************
		//Check High Priority Work Tasks
		//******************************
		//-> Only WorkTasks !
		
		//Trigger-Actions nicht nachts triggern
		CTime time1 = baseWorldObject.town.gameWorld.worldTime;
		
		if((time1.hours>18 && time1.hours<=23) || (time1.hours>=0 && time1.hours<=6))
			return null;
		
		CWorldObject tempwobj=null;
		CAction tempaction=null;
		
		//Check Taskobjects
		for(CWorldObject wobj : taskobjects.values())
		{
			//Funeral Speaker
			if(wobj.theobject.editoraction.contains("company_urbancemetery_rostrum"))
			{
				if(wobj.actionstring1.equals("trigger_funeral") 
						&& wobj.belongsToCompany!=null 
						&& wobj.belongsToCompany.isActive(-1)
						)
				{
					if(tempwobj!=null && getHighPrioActionPriority(wobj.theobject.editoraction, null) > getHighPrioActionPriority(tempwobj.theobject.editoraction, null))
						tempwobj=wobj;
					
					if(tempwobj==null)
						tempwobj=wobj;
					
					continue;
				}
			}
			
			//Leichenwagen
			if(wobj.theobject.editoraction.contains("company_urbancemetery_hearse_traffic_car"))
			{
				for(CWorldObject wh : baseWorldObject.town.gameWorld.tempHumansDead.values())
				{
					//Funktion wird auch dazu benutzt zu prüfen ob es Zeit für Arbeit ist, daher muss hier auch geprüft werden ob das objekt vom human occupied ist, ansonsten würde die action abgebrochen werden
					if((wh.isOccupiedBy==null || (wh.isOccupiedBy!=null && wh.isOccupiedBy.uniqueId==baseWorldObject.uniqueId)) && !wh.actionstring1.contains("show_grave") && !wh.actionstring1.contains("show_coffin"))
					{
						if(tempwobj!=null && getHighPrioActionPriority(wobj.theobject.editoraction, null) > getHighPrioActionPriority(tempwobj.theobject.editoraction, null))
							tempwobj=wobj;
						
						if(tempwobj==null)
							tempwobj=wobj;
					}
					
					//Gravedigger Job nur ausführen wenn sonst nichts ansteht
					//if(tempwobj==null && (wh.actionstring1.contains("show_coffin") && (wh.isOccupiedBy==null || wh.isOccupiedBy.uniqueId==baseWorldObject.uniqueId)))
					if(wh.actionstring1.contains("show_coffin_move") && (wh.isOccupiedBy==null || wh.isOccupiedBy.uniqueId==baseWorldObject.uniqueId))
					{
						//Gdx.app.debug("", "test 1");
						tempwobj=wh;
						break;
					}
				}
			}
		}
		
		//if(tempwobj!=null)
		//	Gdx.app.debug("", "init " + tempwobj.theobject.editoraction);
		
		return tempwobj;
	}
	
	public CWorldObject getWorkplaceByTime(Boolean bCheckTrigger)
	{
		CWorldObject wp1 = getWorkplaceByTime(1,bCheckTrigger);
		
		if(wp1!=null)
			return wp1;
		
		CWorldObject wp2 = getWorkplaceByTime(2,bCheckTrigger);
		
		return wp2;
	}
	
	public CWorldObject getWorkplaceByTime(int worktimeNumber, Boolean bCheckTrigger)
	{
		CTime time = baseWorldObject.town.gameWorld.worldTime;
		
		CWorldObject tempWorkPlace=null;
		
		for(CWorldObject wobj : workplaces.values())
		{
			CWorldObject wp = wobj;
			
			//Maintenance Objects - reparieren wenn condition unter 90%
			if(bCheckTrigger && (wp.isMaintenanceObject() && wp.objectCondition > wp.defaultObjectCondition/1.1f))
				continue;
			
			if(bCheckTrigger && (wp.isCompanyObjectFillingByWorkerObject() && wp.objectFilling>wp.getObjectFillingMax()/2))
			{
				//if(getName().contains("Michael Williams"))
				//	Gdx.app.debug("", "test1 " + wp.objectFilling + ", " + wp.getObjectFillingMax()/2);
				continue;
			}
					
			int workStart = wp.workTime1_From;
			int workEnd = wp.workTime1_To;
			
			if(worktimeNumber==2)
			{
				workStart = wp.workTime2_From;
				workEnd = wp.workTime2_To;
			}
			
			
			
			if(getWorktimeIsNow(workStart, workEnd))
			{
				if(wp.isMaintenanceObject())
				{
					if(tempWorkPlace!=null && wp.isMaintenanceObject() && wp.objectConditionPercent()<tempWorkPlace.objectConditionPercent())
						tempWorkPlace=wp;

					if(tempWorkPlace==null)
						tempWorkPlace=wp;
				}
				
				if(wp.isCompanyObjectFillingByWorkerObject())
				{
					tempWorkPlace=wp;
					break;
				}
				
				if(!wp.isMaintenanceObject() && !wp.isCompanyObjectFillingByWorkerObject())
					return wp;
			}
		}

		//if(getName().contains("Michael Williams"))
		//	Gdx.app.debug("", "test1 " + tempWorkPlace + ", " + baseWorldObject.uniqueId  + ", " + workplaces.size() + ", nr: " + worktimeNumber);

		
		if(tempWorkPlace!=null)
			return tempWorkPlace;
		
		return null;
	}	
	
	public Boolean getWorktimeIsNow(int workStart, int workEnd)
	{
		CTime time = baseWorldObject.town.gameWorld.worldTime;
		
		if(workEnd>workStart)
		{
			if(((time.hours>workStart-2 && time.minutes>30) || time.hours>workStart-1) && time.hours<workEnd)
				return true;
		}
		else if(workEnd<workStart)
		{
			if(time.hours>=workEnd && ((time.hours<workStart-1) || (time.hours<workStart && time.minutes<30)))
				return false;
			
			return true;
		}
		
		//		if(workEnd>workStart)
		//		{
		//			if(time.hours>workStart-2 && time.hours<=workEnd)
		//			{
		//				return true;
		//			}
		//		}
		//		else if(workEnd<workStart)
		//		{
		//			if(time.hours>=workEnd && time.hours<workStart-2)
		//				return false;
		//			
		//			return true;
		//		}
		
		return false;
	}
		
	public Boolean isWorking()
	{
		return timeForWork() && baseWorldObject.activeAction!=null && baseWorldObject.activeActionMode==ActionMode.WORKPLACE && baseWorldObject.activeAction.bActionMode;		
	}

	public Boolean isWorking(String workplace_editoraction)
	{
		try
		{
			if(isWorking() && baseWorldObject.activeAction!=null && baseWorldObject.activeAction.targetActionObject!=null && baseWorldObject.activeAction.targetActionObject.theobject.editoraction.contains(workplace_editoraction))
				return true;
		}
		catch(Exception ex)
		{
			//.. sporadische nullpointer exception
		}
		
		return false;
	}
	
	public Boolean timeForTask()
	{
		CWorldObject wobj = null;
		
		//Dinner Table
		wobj = baseWorldObject.getNextDinnertableEvent(100); //100=alarm clock mode
		
		if(wobj!=null)
		{
			return true;
		}
		


		
		//Laundry Object (Basket, Washingmachine, Dryer)
		if(baseWorldObject.laundryTaskIsReadyToDo())
			return true;
		
		//CWorldObject arr[] = baseWorldObject.getLaundryTaskObject();
		//{
		//	if(arr!=null && arr[0] != null)
		//	{
		//		return true;
		//	}
		//}
		
		return false;
	}
	
	public Boolean timeForWork()
	{
		Boolean b1 = timeForWork(1, "");
		Boolean b2 = timeForWork(2, "");
		
		return b1 || b2;
	}
	
	public Boolean timeForWork(String workplacestring)
	{
		Boolean b1 = timeForWork(1, workplacestring);
		Boolean b2 = timeForWork(2, workplacestring);
		
		return b1 || b2;
	}
		
	public Boolean timeForWork(int worktime, String workplacestring)
	{
		CTime time = baseWorldObject.town.gameWorld.worldTime;
		
		Enumeration<CWorldObject> elist = workplaces.elements();
		
		while(elist.hasMoreElements())
		{
			CWorldObject wp = elist.nextElement();
			
			//7pm - 7am
			//19:00 - 07:00
			//hours: 20:00
			//6: arbeit
			//8: frei
			
			if(!workplacestring.isEmpty() && !wp.theobject.editoraction.toLowerCase().equals(workplacestring.toLowerCase()))
				continue;
				
			int workStart=wp.workTime1_From;
			int workEnd=wp.workTime1_To;
			
			if(worktime==2)
			{
				workStart=wp.workTime2_From;
				workEnd=wp.workTime2_To;
			}
			
			if(getWorktimeIsNow(workStart, workEnd))
			{
				return true;
			}
		}
		
		return false;
	}
	
	
	//public String lastname;
	//public String firstname;
	//public LocalDate birthday;
	//public int weight;
	
	//physical needs
	//	public int hunger;
	//	public int thirst;
	//	public int fatigue; //Erschöpfung, Müdigkeit 
	//	public int dirty;
	//	public int energy;
	//	public int endurance;
	//	public int power; 
	
	//mental status
	
	//skills
	
	//intelligence
	//public int intelligence_logical;
	//public int intelligence_social;
	
	//Adresses
	//public CCompany company;
	//public CBuilding home;
}
