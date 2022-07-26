package com.mygdx.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.List;
import sun.rmi.server.UnicastRef;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.CAction.ActionMode;
import com.mygdx.game.CAddress.AddressOverlap;
import com.mygdx.game.CAnimationTextEvent.AnimationEventType;
import com.mygdx.game.CCompany.CompanyType;
import com.mygdx.game.CHelper.IntersectionMode;
import com.mygdx.game.CHuman.CJobSkillClass;
import com.mygdx.game.CSpriteMoveEvent.SpriteMoveEventType;

public class CAction {
	
	private CTown town;
	
	public static enum ActionMode {
	  IDLE, BED, SHOWER/*or Bathtub*/, TOILET, FRIDGE/*eat*/, TOILET_FLOOR, WORKPLACE
	  , SUPERMARKET_BUYIN, COOK_DINNER, EAT_DINNER, WASH_DISHES
	  , READ_BOOK, WATCH_TV, PLAYGROUND, TAKE_OUT_GARBAGE, PUB_ACTION, FITNESS_STUDIO, CHURCH
	  , VEHICLE_REFUEL, FUNERAL_ATTEND, GOTO_DOCTOR, LAUNDRY, CHANGE_CLOTHES, BREAK_ROOM
	  , ZOMBIE, ORDER_PIZZA
	}
	
	public static enum ValueType {
		COMPLEX, WORK, WORK_COMPLEX, WORKACTION, SLEEP, CLEAN, TOILET, EAT 
	}
	
	public static enum MathType {
		ADD, SUBTRACT, SET
	}
	
	public static String getTextForValueType(ValueType vtype)
	{
		if(vtype!=null)
			return vtype.name();
		
		return "";
	}
	
	public static ValueType getValueTypeForText(String text)
	{
		if(!text.trim().isEmpty())
			return ValueType.valueOf(text);
		else 
			return ValueType.COMPLEX;
	}
	
	public static String getTextForActionMode(ActionMode acmode)
	{
		return acmode.name();
	}
	
	public static ActionMode getActionModeForText(String text)
	{
		return ActionMode.valueOf(text);
	}
	
	public CWorldObject baseWorldObject;
	public int baseWorldObjectId;
	public ActionMode actionMode;
	public ValueType valueType;
	//public static final int initMoneyActionMinTownMoney=100;
	
	public Boolean bActionMode;
	public Boolean bGotoActionMode;
	public CWorldObject targetActionObject;
	public CWorldObject targetActionObject2;
	public CWorldObject targetActionObject3;
	public CWorldObject targetActionObject4;
	public CWorldObject targetActionObject5;
	public CWorldObject targetActionObject6;
	public CWorldObject targetActionObject7;
	public CWorldObject targetActionObject8;
	public CWorldObject targetActionObject9;
	public CWorldObject targetActionObject10;
	public CWorldObject targetActionObject11;
	
	public int targetActionObjectId;
	public int targetActionObject2Id;
	public int targetActionObject3Id;
	public int targetActionObject4Id;
	public int targetActionObject5Id;
	public int targetActionObject6Id;
	public int targetActionObject7Id;
	public int targetActionObject8Id;
	public int targetActionObject9Id;
	public int targetActionObject10Id;
	public int targetActionObject11Id;

	public int priority;
	public float actionState;
	public int actionRepeater;
	Random rand;
	public CCompany targetCompany;
	public int targetCompanyId;
	public String editorActionString;
	
	public float actionTemp_Float1;
	public float actionTemp_Float2;
	public float actionTemp_Float3;
	public float actionTemp_Float4;
	public float actionTemp_Float5;
	public String actionTemp_String1;
	public String actionTemp_String2;
	
	private int randomActionAnim;
	private float randomActionDeltaTime;
	
	private float actionValueDuration;
	private float actionValueTrigger;
	private float actionValueTriggerRed;
	private float actionValueTriggerMax;
	public float hourTimer;
	public float deltaTimer;
	public float deltaTimer2;
	public float deltaTimer3;
	public float actionDuration;
	public int iActionBlocker;
	private float fBlocker_DeltaSeconds;
	private int iActionBlocker_default;
	private int noPathCount;
	public float attributeTimer;
	
	public String actionText;
	public String goingToActionText;
	
	public void dispose()
	{
		town=null;
		baseWorldObject=null;
		targetActionObject=null;
		targetActionObject2=null;
		targetActionObject3=null;
		targetActionObject4=null;
		targetActionObject5=null;
		targetActionObject6=null;
		targetActionObject7=null;
		targetActionObject8=null;
		targetActionObject9=null;
		targetActionObject10=null;
		targetActionObject11=null;		
		targetCompany=null;
	}
	
	public CAction(CTown t1, CWorldObject wobj, CAction.ActionMode mode)
	{
		priority=0;
		
		town=t1;
		actionState=0;
		
		baseWorldObject = wobj;
		iActionBlocker=0;
		actionMode = mode;
		targetActionObject2=null;
		targetActionObject3=null;
		targetActionObject4=null;
		targetActionObject5=null;
		targetActionObject6=null;
		targetActionObject7=null;
		targetActionObject8=null;
		targetActionObject9=null;
		targetActionObject10=null;
		targetActionObject11=null;
		
		hourTimer=0;
		
		targetCompany=null;
		//actionValue=aValue;
		bActionMode=false;
		bGotoActionMode=false;
		fBlocker_DeltaSeconds=0;
		iActionBlocker_default=3000;
		actionText="";
		actionDuration=0;
		attributeTimer=0;
		//goingToActionText="";
		noPathCount=0;
		rand = new Random();
		actionTemp_Float1=0;
		actionTemp_Float2=0;
		actionTemp_Float3=0;
		actionTemp_Float4=0;
		actionTemp_Float5=0;
		actionTemp_String1="";
		actionTemp_String2="";
				
		
		//Action Modes
		if(actionMode == ActionMode.BED)
		{
			valueType=ValueType.SLEEP;
			editorActionString = "bedroom_bed";
			actionValueDuration = 3600*8; // 8 Stunden
			actionText="SLEEPING";
		}
		
		if(actionMode == ActionMode.SHOWER)
		{
			//valueType=ValueType.CLEAN;
			valueType=ValueType.COMPLEX;
			editorActionString="bathroom_shower";
			actionValueDuration = 3600*2; // 60*20; // 20 Minuten
			actionText="TAKING A SHOWER";
		}
		
		if(actionMode == ActionMode.TOILET)
		{
			//valueType=ValueType.TOILET;
			valueType=ValueType.COMPLEX;
			editorActionString="bathroom_toilet";
			actionValueDuration = 60*10; // 10 Minuten
			actionText="TOILET";
		}
		
		if(actionMode == ActionMode.TOILET_FLOOR)
		{
			valueType=ValueType.TOILET;
			editorActionString="toilet_floor";
			actionValueDuration = 60*10; // 10 Minuten
			actionText="NO TOILET";
		}
		
		if(actionMode == ActionMode.FRIDGE)
		{
			valueType=ValueType.COMPLEX;
			editorActionString="kitchen_fridge";
			actionValueDuration = 60*15; // 15 Minuten
			actionText="EATING";
		}
		
		if(actionMode == ActionMode.WORKPLACE)
		{
			valueType=ValueType.WORK;
			//editorActionString="work";
			actionValueDuration = 3600; // 1 Stunde am Stück
			actionText="WORKING";
		}
		
		if(actionMode == ActionMode.COOK_DINNER)
		{
			valueType=ValueType.COMPLEX;
			//editorActionString="work";
			actionValueDuration=3600*2;
			actionText="COOKING DINNER";
		}
		
		if(actionMode == ActionMode.READ_BOOK)
		{
			valueType=ValueType.COMPLEX;
			//editorActionString="work";
			actionValueDuration=3600*2;
			actionText="READING A  BOOK";
		}
		
		if(actionMode == ActionMode.WATCH_TV)
		{
			valueType=ValueType.COMPLEX;
			//editorActionString="work";
			actionValueDuration=3600*2;
			actionText="WATCHING TV";
		}
		
		if(actionMode == ActionMode.PLAYGROUND)
		{
			valueType=ValueType.COMPLEX;
			//editorActionString="work";
			actionValueDuration=3600*3;
			actionText="PLAYGROUND";
		}
		
		if(actionMode == ActionMode.EAT_DINNER)
		{
			valueType=ValueType.COMPLEX;
			//editorActionString="work";
			actionValueDuration=3600*2;
			actionText="EATING";
		}
		
		if(actionMode == ActionMode.WASH_DISHES)
		{
			valueType=ValueType.COMPLEX;
			//editorActionString="work";
			actionValueDuration=3600*2;
			actionText="WASHING DISHES";
		}
		
		if(actionMode == ActionMode.TAKE_OUT_GARBAGE)
		{
			valueType=ValueType.COMPLEX;
			//editorActionString="work";
			actionValueDuration=3600*2;
			actionText="TAKING OUT THE GARBAGE";
		}
		
		if(actionMode == ActionMode.PUB_ACTION)
		{
			valueType=ValueType.COMPLEX;
			//editorActionString="work";
			actionValueDuration=3600*4;
			actionText="PUB";
		}
		
		if(actionMode == ActionMode.BREAK_ROOM)
		{
			valueType=ValueType.COMPLEX;
			//editorActionString="work";
			actionValueDuration=3600*1;
			actionText="BREAK ROOM";
		}

		if(actionMode == ActionMode.ORDER_PIZZA)
		{
			valueType=ValueType.COMPLEX;
			//editorActionString="work";
			actionValueDuration=3600*1;
			actionText="ORDER PIZZA";
		}
				
		if(actionMode == ActionMode.FITNESS_STUDIO)
		{
			valueType=ValueType.COMPLEX;
			//editorActionString="work";
			actionValueDuration=3600*4;
			actionText="FITNESS STUDIO";
		}
		
		if(actionMode == ActionMode.CHURCH)
		{
			valueType=ValueType.COMPLEX;
			//editorActionString="work";
			actionValueDuration=3600*4;
			actionText="CHURCH";
		}
		
		if(actionMode == ActionMode.SUPERMARKET_BUYIN)
		{
			valueType=ValueType.COMPLEX;
			//editorActionString="work";
			actionValueDuration=3600*4;
			actionText="BUYING IN";
		}
		
		if(actionMode == ActionMode.LAUNDRY)
		{
			valueType=ValueType.COMPLEX;
			//editorActionString="work";
			actionValueDuration=3600*4;
			actionText="DOING THE LAUNDRY";
		}
		
		if(actionMode == ActionMode.CHANGE_CLOTHES)
		{
			valueType=ValueType.COMPLEX;
			//editorActionString="work";
			actionValueDuration=3600*4;
			actionText="CHANGING CLOTHES";
		}
		
		if(actionMode == ActionMode.VEHICLE_REFUEL)
		{
			valueType=ValueType.COMPLEX;
			//editorActionString="work";
			actionValueDuration=3600*4;
			actionText="REFUELING";
		}
				
		if(actionMode == ActionMode.FUNERAL_ATTEND)
		{
			valueType=ValueType.COMPLEX;
			//editorActionString="work";
			actionValueDuration=3600*4;
			actionText="ATTENDING A FUNERAL";
		}
		
		if(actionMode == ActionMode.GOTO_DOCTOR)
		{
			valueType=ValueType.COMPLEX;
			//editorActionString="work";
			actionValueDuration=3600*4;
			actionText="VISITING THE DOCTOR";
		}
		
		
		
		
		
		
		
		
		
		//****************
		//Action Values
		//****************
		
		if(valueType==ValueType.SLEEP)
		{
			actionValueTrigger = wobj.thehuman.sleepValueTrigger; //3600*16; //16 Stunden wach -> 8 Stunden Schlaf
			actionValueTriggerRed = wobj.thehuman.sleepValueTriggerRed;
			actionValueTriggerMax = wobj.thehuman.sleepValueTriggerMax;
		}
		
		if(valueType==ValueType.CLEAN)
		{
			actionValueTrigger = wobj.thehuman.cleanValueTrigger; //3600*24; //1x/Tag duschen
			actionValueTriggerRed = wobj.thehuman.cleanValueTriggerRed; //3600*24; //1x/Tag duschen
			actionValueTriggerMax = wobj.thehuman.cleanValueTriggerMax; //3600*24; //1x/Tag duschen
		}
		
		if(valueType==ValueType.EAT)
		{
			actionValueTrigger = wobj.thehuman.eatValueTrigger; //3600*4; //alle 4 Stunden
			actionValueTriggerRed = wobj.thehuman.eatValueTriggerRed; //3600*4; //alle 4 Stunden
			actionValueTriggerMax = wobj.thehuman.eatValueTriggerMax; //3600*4; //alle 4 Stunden
		}
		
		if(valueType==ValueType.TOILET) //for Toilet Floor
		{
			actionValueTrigger = wobj.thehuman.toiletValueTrigger; //3600*4; //alle 4 Stunden
			actionValueTriggerRed = wobj.thehuman.toiletValueTriggerRed; //3600*4; //alle 4 Stunden
			actionValueTriggerMax = wobj.thehuman.toiletValueTriggerMax; //3600*4; //alle 4 Stunden
		}
		
		if(actionMode == ActionMode.TOILET) //Complex Action
		{
			actionValueTrigger=0;
		}
		
		//Special Actions
		if(actionMode == ActionMode.TOILET_FLOOR)
		{
			actionValueTrigger=wobj.thehuman.toiletValueTriggerMax-1;
		}
		
		if(actionMode == ActionMode.WORKPLACE)
		{
			actionValueTrigger=wobj.thehuman.workValueTrigger;
			
			//actionValueTrigger_Time_Start=wobj.thehuman.workPlace1.workTime1_From-1;
			//actionValueTrigger_Time_End=wobj.thehuman.workPlace1.workTime1_To;
		}
		
		if(actionMode == ActionMode.COOK_DINNER)
		{
			actionValueTrigger=0;
		}	
		
		if(actionMode == ActionMode.READ_BOOK)
		{
			actionValueTrigger=0;
		}	
		
		if(actionMode == ActionMode.WATCH_TV)
		{
			actionValueTrigger=0;
		}	
		
		if(actionMode == ActionMode.PLAYGROUND)
		{
			actionValueTrigger=0;
		}	
		
		if(actionMode == ActionMode.EAT_DINNER)
		{
			actionValueTrigger=0;
		}	
		
		if(actionMode == ActionMode.WASH_DISHES)
		{
			actionValueTrigger=0;
		}	
		
		if(actionMode == ActionMode.TAKE_OUT_GARBAGE)
		{
			actionValueTrigger=0;
		}	
		
		if(actionMode == ActionMode.PUB_ACTION)
		{
			actionValueTrigger=0;
		}	
		
		if(actionMode == ActionMode.BREAK_ROOM)
		{
			actionValueTrigger=0;
		}
		
		if(actionMode == ActionMode.ORDER_PIZZA)
		{
			actionValueTrigger=0;
		}			
		
		if(actionMode == ActionMode.FITNESS_STUDIO)
		{
			actionValueTrigger=0;
		}	
		if(actionMode == ActionMode.CHURCH)
		{
			actionValueTrigger=0;
		}	
		if(actionMode == ActionMode.SUPERMARKET_BUYIN)
		{
			actionValueTrigger=0;
			
			//actionValueTrigger_Time_Start=wobj.thehuman.workPlace1.workTime1_From-1;
			//actionValueTrigger_Time_End=wobj.thehuman.workPlace1.workTime1_To;
		}		
		if(actionMode == ActionMode.LAUNDRY)
		{
			actionValueTrigger=0;
			
			//actionValueTrigger_Time_Start=wobj.thehuman.workPlace1.workTime1_From-1;
			//actionValueTrigger_Time_End=wobj.thehuman.workPlace1.workTime1_To;
		}	
		if(actionMode == ActionMode.CHANGE_CLOTHES)
		{
			actionValueTrigger=0;
			
			//actionValueTrigger_Time_Start=wobj.thehuman.workPlace1.workTime1_From-1;
			//actionValueTrigger_Time_End=wobj.thehuman.workPlace1.workTime1_To;
		}	
		if(actionMode == ActionMode.SHOWER)
		{
			actionValueTrigger=0;
			
			//actionValueTrigger_Time_Start=wobj.thehuman.workPlace1.workTime1_From-1;
			//actionValueTrigger_Time_End=wobj.thehuman.workPlace1.workTime1_To;
		}			
		if(actionMode == ActionMode.VEHICLE_REFUEL)
		{
			actionValueTrigger=0;
			
			//actionValueTrigger_Time_Start=wobj.thehuman.workPlace1.workTime1_From-1;
			//actionValueTrigger_Time_End=wobj.thehuman.workPlace1.workTime1_To;
		}	
				
		if(actionMode == ActionMode.FUNERAL_ATTEND)
		{
			actionValueTrigger=0;
			
			//actionValueTrigger_Time_Start=wobj.thehuman.workPlace1.workTime1_From-1;
			//actionValueTrigger_Time_End=wobj.thehuman.workPlace1.workTime1_To;
		}	
		
		if(actionMode == ActionMode.GOTO_DOCTOR)
		{
			actionValueTrigger=0;
			
			//actionValueTrigger_Time_Start=wobj.thehuman.workPlace1.workTime1_From-1;
			//actionValueTrigger_Time_End=wobj.thehuman.workPlace1.workTime1_To;
		}	
		
//		if(actionMode == ActionMode.SUPERMARKET_REFILLSHELF)
//		{
//			actionValueTrigger=0;
//			
//			//actionValueTrigger_Time_Start=wobj.thehuman.workPlace1.workTime1_From-1;
//			//actionValueTrigger_Time_End=wobj.thehuman.workPlace1.workTime1_To;
//		}			
		
		if(town.gameWorld.bDebugActions)
		{
			//	Random rand=new Random();
			//	actionValueTrigger=rand.nextInt(2000);
			//	actionValueDuration = 60*5;
		}
	}
	
	public void setActionValue(float val, MathType mtype)
	{
		if(baseWorldObject.thehuman!=null)
		{
			if(valueType == ValueType.CLEAN)
			{
				if(mtype==MathType.ADD)
					baseWorldObject.thehuman.cleanValue+=val;
				if(mtype==MathType.SUBTRACT)
					baseWorldObject.thehuman.cleanValue-=val;
				if(mtype==MathType.SET)
					baseWorldObject.thehuman.cleanValue=val;
			}
						
			if(valueType == ValueType.EAT)
			{
				if(mtype==MathType.ADD)
					baseWorldObject.thehuman.eatValue+=val;
				if(mtype==MathType.SUBTRACT)
					baseWorldObject.thehuman.eatValue-=val;
				if(mtype==MathType.SET)
					baseWorldObject.thehuman.eatValue=val;
			}
			
			if(valueType == ValueType.SLEEP)
			{
				if(mtype==MathType.ADD)
					baseWorldObject.thehuman.sleepValue+=val;
				if(mtype==MathType.SUBTRACT)
					baseWorldObject.thehuman.sleepValue-=val;
				if(mtype==MathType.SET)
					baseWorldObject.thehuman.sleepValue=val;				
			}
			
			if(valueType == ValueType.TOILET)
			{
				//Gdx.app.debug("", "set value " + parentWorldObject.thehuman.toiletValue);
				if(mtype==MathType.ADD)
					baseWorldObject.thehuman.toiletValue+=val;
				if(mtype==MathType.SUBTRACT)
					baseWorldObject.thehuman.toiletValue-=val;
				if(mtype==MathType.SET)
					baseWorldObject.thehuman.toiletValue=val;					
			}
			if(valueType == ValueType.WORK || valueType == ValueType.WORK_COMPLEX)
			{
				if(mtype==MathType.ADD)
					baseWorldObject.thehuman.workValue+=val;
				if(mtype==MathType.SUBTRACT)
					baseWorldObject.thehuman.workValue-=val;
				if(mtype==MathType.SET)
					baseWorldObject.thehuman.workValue=val;
			}			
		}
	}
	
	public float getActionValue()
	{
		if(baseWorldObject.thehuman!=null)
		{
			//			if(actionMode==ActionMode.SINK)
			//				return 1;
			
			if(valueType == ValueType.CLEAN)
				return baseWorldObject.thehuman.cleanValue;
			
			if(valueType == ValueType.EAT)
				return baseWorldObject.thehuman.eatValue;
			
			if(valueType == ValueType.SLEEP)
				return baseWorldObject.thehuman.sleepValue;
			
			if(valueType == ValueType.TOILET)
				return baseWorldObject.thehuman.toiletValue;
			
			if(valueType == ValueType.WORK)
				return baseWorldObject.thehuman.workValue;

			if(valueType == ValueType.WORK_COMPLEX)
				return baseWorldObject.thehuman.workValue;
			
			if(valueType == ValueType.COMPLEX)
				return 1;
			
			if(valueType == ValueType.WORKACTION)
				return 1;	
		}
		
		return 0;
	}
	
	public void clearTargetActionObject(CWorldObject target)
	{
		if(target!=null)
		{
			target.clearActionVariables();
			target.clearOccupied(baseWorldObject);
			target.doObjectAction=false;
		}
	}
	
	public void resetActionObjects()
	{
		baseWorldObject.thehuman.setClothingBack();
				
		actionTemp_Float1=0;
		actionTemp_Float2=0;
		actionTemp_Float3=0;
		actionTemp_Float4=0;
		actionTemp_Float5=0;
		actionTemp_String1="";
		actionTemp_String2="";
		
		baseWorldObject.clearActionVariables();
		baseWorldObject.objectAnimSpeedModifier=0;
		
		clearTargetActionObject(targetActionObject);
		clearTargetActionObject(targetActionObject2);
		clearTargetActionObject(targetActionObject3);
		clearTargetActionObject(targetActionObject4);
		clearTargetActionObject(targetActionObject5);
		clearTargetActionObject(targetActionObject6);
		clearTargetActionObject(targetActionObject7);
		clearTargetActionObject(targetActionObject8);
		clearTargetActionObject(targetActionObject9);
		clearTargetActionObject(targetActionObject10);
		clearTargetActionObject(targetActionObject11);
		
		targetActionObject=null;
		targetActionObject2=null;
		targetActionObject3=null;
		targetActionObject4=null;
		targetActionObject5=null;
		targetActionObject6=null;
		targetActionObject7=null;
		targetActionObject8=null;
		targetActionObject9=null;
		targetActionObject10=null;
		targetActionObject11=null;
	}
	
	public CAction.ActionMode handleAction(float stateTime, CAction.ActionMode activeActionMode)
	{
		//		if(parentWorldObject.thehuman.workplaces.size()>0 && parentWorldObject.thehuman.workplaces.values().stream().findFirst().get().theobject.editoraction.contains("software"))
		//		{
		//			if(actionMode==ActionMode.WORKPLACE)
		//				Gdx.app.debug("", "software handle action");
		//		}
		
		//if(actionMode==ActionMode.VEHICLE_REFUEL)
			//Gdx.app.debug("debug refuel 4", "");
		
		//if(baseWorldObject.uniqueId==42)
		//	Gdx.app.debug("", "actionmode: " + actionMode.toString() + ", bactionmode: " + bActionMode);
		
		if(actionMode==ActionMode.ZOMBIE)
		{
			doAction_Zombie();
			//parentWorldObject.activeAction=parentWorldObject.action_idle;
			return ActionMode.ZOMBIE;
		}
		
		if(actionMode==ActionMode.IDLE)
		{
			doAction_Idle();
			//parentWorldObject.activeAction=parentWorldObject.action_idle;
			return ActionMode.IDLE;
		}
		
		try
		{
			//Action abbrechen nach Maxdauer*1.5 oder spätestens nach 12 Stunden
			if(bActionMode || bGotoActionMode)
			{
				actionDuration+=CHelper.getDeltaSeconds(town);
				
				if(actionDuration>3600*12 || actionDuration>actionValueDuration*10)
				{
					//Gdx.app.debug("cancel action", getTextForActionMode(activeActionMode) + ", duration: " + actionDuration/3600 + " hours");
					
					//if(actionMode==ActionMode.VEHICLE_REFUEL)
					//	Gdx.app.debug("debug refuel 5", "");
					
					resetActionObjects();
					
					bActionMode=false;
					bGotoActionMode=false;
				}
			}
			
			//if(actionMode==ActionMode.VEHICLE_REFUEL)
			//	Gdx.app.debug("debug refuel 6", "");
			
			//Action für gewisse Zeit nicht ausführen bzw. blockieren, wenn sie abgebrochen wurde
			if(iActionBlocker>0)
			{
				if(fBlocker_DeltaSeconds>3600)
					fBlocker_DeltaSeconds=0;
				
				if(fBlocker_DeltaSeconds>10)
				{
					iActionBlocker-=10;

					fBlocker_DeltaSeconds=0;

					if(iActionBlocker<0)
						iActionBlocker=0;
				}
				
				fBlocker_DeltaSeconds+=CHelper.getDeltaSeconds(town);
			}
			
			if(activeActionMode!=CAction.ActionMode.IDLE && activeActionMode!=actionMode)
				return activeActionMode;
			
			if(iActionBlocker>0)
			{
				//if(baseWorldObject.thehuman.getName().contains("Sarah Robinson"))
				//	Gdx.app.debug("debugactionblocker1", "iActionBlocker: "+iActionBlocker + ", resident: " + baseWorldObject.thehuman.getName() + ", action: " + actionMode.toString());
				
				resetActionObjects();
				bActionMode=false;
				bGotoActionMode=false;
				baseWorldObject.activeAction=null;
				return ActionMode.IDLE;
			}
			
			initAction(stateTime);
			
			checkCancel();
			
			gotoActionMode(stateTime);
			
			doActionMode(stateTime);
			
			checkCancel();
			
			if(bActionMode || bGotoActionMode)
			{
				baseWorldObject.activeAction=this;
			}
			else
			{
				if(baseWorldObject.activeAction!=null && baseWorldObject.activeAction.actionMode==actionMode) //Falls auf Zwischenaction (zb refuel car umgeschaltet wurde)
				{
					baseWorldObject.activeAction=null;
				}
			}
		}
		catch(Exception ex)
		{
			//Wenn zB Objekte gelöscht werden, die in einer Action verwendet werden
			CHelper.writeError(ex.getMessage(), ex.getStackTrace(), ex);
			
			resetActionObjects();
			
			bActionMode=false;
			iActionBlocker=iActionBlocker_default;
		}		
		
		if(bActionMode || bGotoActionMode)
			return actionMode;
		else
		{
			if(baseWorldObject.activeAction!=null) //Es wurde während Action auf andere Action / Zwischenaction umgeschaltet
				return baseWorldObject.activeAction.actionMode;
				
			return CAction.ActionMode.IDLE;
		}
	}
	
	public Boolean objectIsReady(CWorldObject obj)
	{
		if(obj==null)
			return true;
		if(obj.iObjectIsReady<100)
			return false;
		
		return true;
	}
	
	public void checkCancel()
	{
		Boolean bcancel=false;
		if(bActionMode)
		{
			if(targetActionObject!=null && targetActionObject.theobject.editoraction.contains("construction_pickup"))
				return;
			
			if(!objectIsReady(targetActionObject))
				bcancel=true;
			if(!objectIsReady(targetActionObject2))
				bcancel=true;
			if(!objectIsReady(targetActionObject3))
				bcancel=true;
			if(!objectIsReady(targetActionObject4))
				bcancel=true;
			if(!objectIsReady(targetActionObject5))
				bcancel=true;
			if(!objectIsReady(targetActionObject6))
				bcancel=true;
			if(!objectIsReady(targetActionObject7))
				bcancel=true;
			if(!objectIsReady(targetActionObject8))
				bcancel=true;
			if(!objectIsReady(targetActionObject9))
				bcancel=true;
			if(!objectIsReady(targetActionObject10))
				bcancel=true;
			if(!objectIsReady(targetActionObject11))
				bcancel=true;
			
			if(bcancel)
				cancelAction("");
		}
	}
	
	public void initAction(float stateTime)
	{
		//if(baseWorldObject.thehuman.name.contains("Hall"))
		//	Gdx.app.debug("", "init action bActionMode: " + bActionMode);
		

		
		//Wenn Resident krank ist können nicht mehr alle Actions ausgeführt werden
		if(!baseWorldObject.thehuman.canWork())
		{
			if(actionMode!=ActionMode.BED && 
					actionMode!=ActionMode.SHOWER &&
					actionMode!=ActionMode.TOILET &&
					actionMode!=ActionMode.TOILET_FLOOR &&
					actionMode!=ActionMode.IDLE &&
					actionMode!=ActionMode.FRIDGE &&
					actionMode!=ActionMode.READ_BOOK &&
					actionMode!=ActionMode.WATCH_TV &&
					actionMode!=ActionMode.GOTO_DOCTOR && 
					actionMode!=ActionMode.CHANGE_CLOTHES
				)
				
			return;
		}
		
		if(baseWorldObject.town.bDebugLogging)
			Gdx.app.debug(baseWorldObject.uniqueId +", Before Action init " + CAction.getTextForActionMode(actionMode), "actionvalue: " + getActionValue() + ", trigger: " + actionValueTrigger + "actionMode: " + getTextForActionMode(actionMode) + ", bActionMode: " + bActionMode + ", bGotoActionMode: " + bGotoActionMode);
		
		Boolean bInit=(getActionValue() > actionValueTrigger && !bActionMode && !bGotoActionMode);
		if(bInit==false)
		{
			//Task wird als Work durchgeführt (Leichenwagen fahren, Totenrede halten)
			bInit=(actionMode==ActionMode.WORKPLACE && baseWorldObject.thehuman.getWorkTaskPlaceByTrigger()!=null && !bActionMode && !bGotoActionMode); 
		}
		
		
		if(bInit)
		{
			//HIER PERFORMANCE PROBLEM -> eigene liste für alle actionobjekte oder wenns immer noch probleme macht: eigene liste für jede objektart 
				//-> liste von actionobjekten für Adresse -> dann immer nur von aktueller adresse holen und wenn nix gefunden -> von nachbaradresse
			
			randomActionAnim=0;
			randomActionDeltaTime=0;
			actionTemp_String1="";
			actionTemp_String2="";
			actionDuration=0;
			actionState=0;
			
			actionTemp_Float1=0;
			actionTemp_Float2=0;
			actionTemp_Float3=0;
			actionTemp_Float4=0;
			actionTemp_Float5=0;
			
			baseWorldObject.actionanim1=0;
			baseWorldObject.actionanim2=0;
			
			if(!baseWorldObject.bInitAction_NoResetTargetObjects)
			{
				targetActionObject=null;
				targetActionObject2=null;
				targetActionObject3=null;
				targetActionObject4=null;
				targetActionObject5=null;
				targetActionObject6=null;
				targetActionObject7=null;
				targetActionObject8=null;
				targetActionObject9=null;
				targetActionObject10=null;
				targetActionObject11=null;
			}
			
			Optional<CWorldObject> objtemp=null;
			CWorldObject obj=null;
			
			if(valueType==ValueType.WORK || valueType==ValueType.WORK_COMPLEX)
			{
				obj = baseWorldObject.thehuman.getWorkTaskPlaceByTrigger(); //hat prio vor normaler work
				
				//if(obj!=null && obj.isHuman())
				//	Gdx.app.debug("", "log obj: "+obj.theobject.editoraction);
				
				if(obj==null)
				{
					obj = baseWorldObject.thehuman.getWorkplaceByTime(true);
					
					//if(baseWorldObject.thehuman.getName().contains("Michael Williams"))
					//{
					//	Gdx.app.debug("", "test1 " + actionMode.toString() + " " + obj);
					//}
					
					//if(obj!=null)
					//	Gdx.app.debug("", "init 2 " + obj.theobject.editoraction);
				}
				else
				{
					//Gdx.app.debug("", "init 1 " + obj.theobject.editoraction);
				}
				
				//				if(baseWorldObject.town.bDebugLogging)
				//				{	if(obj==null)
				//					{
				//						Gdx.app.debug("WorkplaceByTime is null", "");
				//					}
				//					else
				//					{
				//						Gdx.app.debug("WorkplaceByTime", obj.getCompanyWorkingPlaceJobTitle(0) + ", activeByEnergy: " + obj.isActiveByEnergyConsumption());
				//					}
				//				}
				
				if(obj!=null)
				{
					//Maintenance Objects - reparieren wenn condition unter 90%
					Boolean binit = initComplexWorkAction(obj);
					
					if(binit)
					{
						float frand = CHelper.getRandomFloat(0.8f, 1.2f, rand);
						setActionValue(actionValueDuration*frand, MathType.SET);
						
						return;
					}
					
					valueType=ValueType.WORK;
				}
			}
			else if(actionMode==ActionMode.CHANGE_CLOTHES)
			{
				//	Requirements:
				//  clothingvalue ist auf trigger wobj.thehuman.clothingValueTrigger
				//	laundry basket value<3?, laundrybasket occupied1&2==null
				//	wardrobe value >= 3?
				
				//	Action:
				//	reserve laundry basket			
				//	goto wardrobe
				//	change clothes
				//	goto laundry basket
				
				if(baseWorldObject.thehuman.clothingValue > baseWorldObject.thehuman.clothingValueTrigger)
				{
					if(baseWorldObject.thehuman.wardrobe != null && baseWorldObject.thehuman.wardrobe.objectFilling >= town.gameWorld.town.action_changeclothesvalue)
					{
						targetActionObject = baseWorldObject.thehuman.wardrobe;
						bActionMode = true;

						return;
					}
				}
				
				return;
			}
			else if(actionMode==ActionMode.LAUNDRY)
			{
				if(baseWorldObject.thehuman.getName().contains("Mark Lewis")){
				//	Gdx.app.debug(baseWorldObject.thehuman.getName(), "Laundry_1");
				}
				
				if(!baseWorldObject.laundryTaskIsReadyToDo())
					return;

				
				if(baseWorldObject.thehuman.getName().contains("Mark Lewis")){
					//Gdx.app.debug(baseWorldObject.thehuman.getName(), "Laundry_2");
				}

				
				CWorldObject[] taskobjects = baseWorldObject.getLaundryTaskObject();
				
				//basket
				//  isoccupied2=waschmaschine/trockner
				//	nicht: actionvar1=id waschmaschine/trockner
				//waschmaschine/trockner
				//	actionvar1=1 fertig
				//  isoccupied2=basket
				//	nicht: actionvar2=id basket - wozu?
				
				if(taskobjects!=null && taskobjects[0]!=null)
				{
					//Basket -> Washingmachine
					if(taskobjects[1]!=null)
					{
						//retarr[0]=basket;
						//retarr[1]=wm; //washingmachine
						
						//Requirements
						//Waschmaschine: active, empty, occupied==null
						
						//Action
						//Gehe zum Basket, speichere Ursprungskoordinaten
						//Nimm Basket mit und gehe zur Waschmaschine
						//Befülle Waschmaschine, stelle Basket links davor ab
						//basket actionvar1=washingmachineid
						//washingmachine actionvar2=basketid
						
						actionTemp_String1="basket_washingmachine";
						targetActionObject=taskobjects[0];
						targetActionObject2=taskobjects[1];
						if(targetActionObject2.isActiveByEnergyConsumption() && 
								targetActionObject2.isActiveByWaterConsumption() && 
								//targetActionObject2.getObjectFillingMulti()==0 &&
								(targetActionObject2.isOccupiedBy==null || targetActionObject2.isOccupiedBy.uniqueId==baseWorldObject.uniqueId))
						{
							targetActionObject.isOccupiedBy=baseWorldObject; 
							targetActionObject2.isOccupiedBy=baseWorldObject;
							
							bActionMode=true;
						}
						
						return;
					}
					
					
					//Washingmachine -> Dryer
					if(taskobjects[2]!=null)
					{
						//retarr[0]=basket;
						//retarr[2]=obj2; //washingmachine
						//retarr[3]=dryer;
						
						//Requirements
						//Trockner aktiv, empty, occupied==null
						//waschmaschine befüllt	objectFillingMulti
						//waschmaschine fertig mit waschen/trocknen	actionvar1==1
						
						//Action
						//Gehe zu Basket
						//Gehe mit Basket zur Waschmaschine
						//Befülle Basket, leere washingmachine
						//washingmachine reset actionvar1, actionvar2
						//basket reset actionvar1
						//Bringe Basket zum Trockner
						//Stelle Basket links davor
						//basket actionvar1=dryerid
						//dryer actionvar2=basketid
						//Befülle Trockner
						
						actionTemp_String1="washingmachine_dryer";
						targetActionObject=taskobjects[0];
						targetActionObject2=taskobjects[2]; //Washingmachine
						targetActionObject3=taskobjects[3]; //Dryer
						if(targetActionObject3.isActiveByEnergyConsumption() && 
								targetActionObject3.isActiveByWaterConsumption() && 
								//targetActionObject3.getObjectFillingMulti()==0 && 
								(targetActionObject3.isOccupiedBy==null || targetActionObject3.isOccupiedBy.uniqueId==baseWorldObject.uniqueId) &&
								targetActionObject2.getObjectFillingMulti()>0 
								&& targetActionObject2.actionvar1==1
								)
						{
							targetActionObject.isOccupiedBy=baseWorldObject;
							targetActionObject2.isOccupiedBy=baseWorldObject;
							targetActionObject3.isOccupiedBy=baseWorldObject;

							bActionMode=true;
						}
						
						//if(baseWorldObject.thehuman.getName().contains("Jennifer Gonzalez"))
						//	Gdx.app.debug("", "targetActionObject2.getObjectFillingMulti(): " + targetActionObject2.getObjectFillingMulti() + ", targetActionObject2.actionvar1_ "+ targetActionObject2.actionvar1);
						
						return;
					}
					
					
					//Dryer -> Basket
					if(taskobjects[4]!=null)
					{
						//retarr[0]=basket;
						//retarr[4]=obj2; //Dryer
						
						//Requirements
						//-
						
						//Action
						//Gehe zu Basket
						//Gehe mit Basket zu Dryer
						//Befülle Basket
						//reset dryer actionvar1, actionvar2
						//reset basket actionvar1
						//Bringe Basket zum Ursprungsplatz zurück
						
						actionTemp_String1="dryer_basket";
						targetActionObject=taskobjects[0];
						targetActionObject2=taskobjects[4];
						
						//if(targetActionObject2.actionvar1==1)
						if(targetActionObject2.getObjectFillingMulti()>0)
						{
							targetActionObject.isOccupiedBy=baseWorldObject;
							targetActionObject2.isOccupiedBy=baseWorldObject;
							
							bActionMode=true;
						}
						
						return;
					}
					
					
					//Collect Laundry from floor
					if(taskobjects[5]!=null)
					{
						actionTemp_String1="collect_laundry";
						
						targetActionObject2 = taskobjects[5];
						targetActionObject2.isOccupiedBy = baseWorldObject;
						
						targetActionObject = taskobjects[0];
						targetActionObject.isOccupiedBy = baseWorldObject;
						
						bActionMode = true;
					}
					
				}
				
				return;
			}
			else if(actionMode==ActionMode.TOILET_FLOOR)
			{
				bActionMode=true;
				targetActionObject = baseWorldObject;
				
				float frand = CHelper.getRandomFloat(0.8f, 1.2f, rand); //Dynamischen Faktor einbauen
				setActionValue(actionValueDuration*frand, MathType.SET);
				
				float changehap = baseWorldObject.thehuman.changeHappinessValue(-50);
				baseWorldObject.addAnimationEvent(AnimationEventType.HAPPINESS, changehap);
				
				return;
			}
			else
			{
				if(actionMode==ActionMode.FRIDGE) //Essen aus Kühlschrank holen
				{
					//Objekt auf Adresse suchen auf der sich der Einwohner aktuell befindet
					//CAddress locationAdr = gameWorld.getAddressByLocation(parentWorldObject.pos_x(), parentWorldObject.pos_y());
					if(baseWorldObject.thehuman.eatValue<baseWorldObject.thehuman.eatValueTrigger*1.8f)
						return;
					
					CWorldObject dinnerTable = baseWorldObject.getNextDinnertableEvent(3); //Wenn in den nächsten 2 Stunden ein Dinner stattfindet, dann snacke nicht
					if(dinnerTable!=null)
						return;
					
					//Aktuelle Work Adr
					CWorldObject wp = baseWorldObject.thehuman.getWorkplaceByTime(true);
					if(wp!=null)
					{
						CAddress locationAdr = wp.theaddress;
						//CAddress locationAdr = gameWorld.getAddressByPoint(baseWorldObject.pos_x(), baseWorldObject.pos_y());
						if(locationAdr!=null)
							objtemp = locationAdr.listWorldObjects.stream().filter(item->item.theobject.editoraction.contains(editorActionString) && item.isOccupiedBy==null && item.bDeleted==false && item.bReachable==true && item.isActiveByEnergyConsumption() && item.getObjectFillingMulti()>0).findFirst();
					}
					
					//Objekt auf Heim-Adresse suchen
					if(objtemp==null || !objtemp.isPresent())
					{
						if(baseWorldObject.thehuman.eatValue < baseWorldObject.thehuman.eatValueTriggerRed)
						{
							if(baseWorldObject.thehuman.isWorking()) //Nur wenn Trigger Red dann von Arbeit nach Hause gehen
								return;
						}
						
						//fridge action
						if(baseWorldObject.theaddress!=null)
						{
							CWorldObject fridge=null;
							for(CWorldObject fr1 : baseWorldObject.theaddress.listWorldObjects)
							{
								if(fr1.theobject.editoraction.contains("fridge") && fr1.isActiveByEnergyConsumption() && fr1.getObjectFillingMulti()>0)
								{
									if(fr1.isOccupiedBy==null || fr1.isOccupiedBy.uniqueId==baseWorldObject.uniqueId)
										fridge=fr1;
									else
									{
										if(fridge==null)
											fridge=fr1;
									}
								}
							}
							
							if(fridge!=null)
							{
								targetActionObject=fridge;
								if(fridge.isOccupiedBy==null)
									targetActionObject.isOccupiedBy=baseWorldObject;
								bActionMode=true;
								return;			
							}
						}
					}
				}
				else if(actionMode==ActionMode.PLAYGROUND)
				{
					if(baseWorldObject.thehuman.getAge()>14)
						return;
					
					if(baseWorldObject.thehuman.energyValue < 70)
						return;
					
					
					//Im Winter kein Spielplatz
					int month = baseWorldObject.town.gameWorld.worldTime.day;
					if(month==1 || month==2 || month==11 || month==12)
						return;
										
					//Suche Playground mit geringster Entfernung
					CCompany playground=CCompany.getNextActiveCompany(CompanyType.PLAYGROUND, baseWorldObject.town, baseWorldObject.pos_x(), baseWorldObject.pos_y(), -1, 99999);
					if(playground==null)
						return;
					
					//Suche freies Spielgerät
					CWorldObject playobject=null;
					if(playground.address_company.listWorldObjects.size()>0)
					{
						int count=0;
						while(playobject==null)
						{
							count++;
							if(count>playground.address_company.listWorldObjects.size()+2)
								break;
							
							int index = rand.nextInt(playground.address_company.listWorldObjects.size());
							CWorldObject wobj = (CWorldObject)playground.address_company.listWorldObjects.get(index);
							
							if(wobj.bObjMoving)
								continue;
							
							if(wobj.theobject.editoraction.contains("company_playground_sandpit"))
							{
								if(reserveObject(wobj, 1))
								{
									playobject=wobj;
									break;
								}

								if(reserveObject(wobj, 2))
								{
									playobject=wobj;
									break;
								}

								if(reserveObject(wobj, 3))
								{
									playobject=wobj;
									break;
								}

								if(reserveObject(wobj, 4))
								{
									playobject=wobj;
									break;
								}
							}
							
							if(wobj.theobject.editoraction.contains("company_playground_slide"))
							{
								if(reserveObject(wobj, 1))
								{
									playobject=wobj;
									break;
								}
						
							}
							
							if(wobj.theobject.editoraction.contains("company_playground_swing"))
							{
								if(reserveObject(wobj, 1))
								{
									playobject=wobj;
									break;
								}
						
							}
							
							if(wobj.theobject.editoraction.contains("company_playground_seesaw"))
							{
								if(reserveObject(wobj, 1))
								{
									playobject=wobj;
									break;
								}
								
								if(reserveObject(wobj, 10))
								{
									playobject=wobj;
									break;
								}
							}
						}
					}
					
					if(playobject!=null)
					{
						bActionMode=true;
						targetActionObject=playobject;
					}
					
					return;
				}
				else if(actionMode==ActionMode.WATCH_TV)
				{
					if(baseWorldObject.theaddress==null)
						return;
					
					if(baseWorldObject.thehuman.getIntelligenceValue()>140) //will nicht fernsehen
						return;
					
					if(baseWorldObject.thehuman.energyValue > 80 && baseWorldObject.thehuman.getHappynessValue()>80)
					{
						int r1 = rand.nextInt(10);
						if(r1>0)
							return;
					}
					
					//wahrscheinlichkeitsfaktor einbauen: hohe intelligenz und education -> eher nicht tv schauen
					int factor=(int)(200-baseWorldObject.thehuman.getIntelligenceValue()-baseWorldObject.thehuman.getEducationValue()*30);
					if(factor<1)
						factor=1;
					int ir = rand.nextInt(factor);
					if(ir<20)
						return;
					
					//targetActionObject	tv
					//targetActionObject2	couch, armchair
					
					Object[] tvlist = baseWorldObject.theaddress.listWorldObjects.stream().filter(item->item.theobject.editoraction.contains("_tv")).toArray();
					
					if(tvlist!=null && tvlist.length>0)
					{
						for(CWorldObject cwobj : baseWorldObject.theaddress.listWorldObjects)
						{
							if( (cwobj.theobject.editoraction.contains("livingroom_couch") && (cwobj.isOccupiedBy==null || cwobj.isOccupiedByExtern==null)) || (cwobj.theobject.editoraction.contains("livingroom_armchair") && cwobj.isOccupiedBy==null) )
							{
								for(Object tvobj : tvlist)
								{
									if(!((CWorldObject)tvobj).isActiveByEnergyConsumption())
										continue;
									
									if(Intersector.overlapConvexPolygons(((CWorldObject)tvobj).theobject.getTVZonePolygon(), cwobj.getBoundingPolygon(IntersectionMode.COLLISION)))
									{
										if(Intersector.overlapConvexPolygons(cwobj.theobject.getTVZonePolygon(), ((CWorldObject)tvobj).getBoundingPolygon(IntersectionMode.COLLISION)))
										{
											targetActionObject=(CWorldObject)tvobj;
											targetActionObject2=cwobj;
											break;
										}
									}
								}
							}
						}
					}
					
					if(targetActionObject!=null && targetActionObject2!=null)
					{
						bActionMode=true;
					}
					
					return;
				}
				else if(actionMode==ActionMode.READ_BOOK)
				{
					if(baseWorldObject.theaddress==null)
						return;
					
					if(baseWorldObject.thehuman.getEducationValue()<CHuman.getRequiredEducationForReading()) //kann nicht lesen
						return;
					
					//if(parentWorldObject.thehuman.getIntelligenceValue()<50) //will kein buch lesen
					//	return;
					
					//wahrscheinlichkeitsfaktor einbauen
					int factor=(int)(200-baseWorldObject.thehuman.getIntelligenceValue()-baseWorldObject.thehuman.getEducationValue()*10);
					if(factor<1)
						factor=1;
					int ir = rand.nextInt(factor);
					if(ir>20)
						return;
					
					//targetActionObject	bookshelf
					//targetActionObject2	couch, armchair
					
					for(CWorldObject cwobj : baseWorldObject.theaddress.listWorldObjects)
					{
						if(cwobj.theobject.editoraction.contains("livingroom_bookshelf") && cwobj.isOccupiedBy==null)
							targetActionObject=cwobj;
						
						if(cwobj.theobject.editoraction.contains("livingroom_couch") && (cwobj.isOccupiedBy==null || cwobj.isOccupiedByExtern==null))
							targetActionObject2=cwobj;
						
						if(cwobj.theobject.editoraction.contains("livingroom_armchair") && cwobj.isOccupiedBy==null)
							targetActionObject2=cwobj;
					}
					
					if(targetActionObject!=null && targetActionObject2!=null)
					{
						bActionMode=true;
					}
					
					return;
				}
				else if(actionMode==ActionMode.CHURCH)
				{
					//if(!baseWorldObject.thehuman.demandIsOpen("CHURCH"))
					//	return;
										
					if(baseWorldObject.thehuman.energyValue < 60)
						return;
					
					if(baseWorldObject.thehuman.energyValue < 70)
					{
						int r1 = rand.nextInt(1);
						if(r1==0)
							return;
					}
					
					if(baseWorldObject.thehuman.energyValue < 80)
					{
						int r1 = rand.nextInt(2);
						if(r1==0)
							return;
					}
					
					//Suche aktive church
					//CCompany comp1 = CCompany.getNextActiveCompany(CompanyType.CHURCH, baseWorldObject.town, baseWorldObject.pos_x(), baseWorldObject.pos_y(), 0);
					for(CCompany comp : town.gameWorld.worldCompanyList)
					{
						if(comp.companyType==CompanyType.CHURCH)
						{
							for(CWorldObject wobj : comp.address_company.listWorldObjects)
							{
								if(wobj.theobject.editoraction.contains("company_church_bank"))
								{
									//Sitzplatz frei?
									int ifreeseatnr=0;
									if(wobj.isOccupiedBy==null)
										ifreeseatnr=1;
									if(wobj.isOccupiedBy2==null)
										ifreeseatnr=2;
									if(wobj.isOccupiedBy3==null)
										ifreeseatnr=3;
									if(wobj.isOccupiedBy4==null)
										ifreeseatnr=4;
									
									if(ifreeseatnr==0)
										continue;
									
									CWorldObject tobj = wobj.getTeachersDeskForStudentsDesk(3);
									
									if(tobj!=null && tobj.worker!=null && tobj.worker.thehuman.canWork() && tobj.isWorkerActive())
									{
										if(!reserveObject(wobj, ifreeseatnr))
											continue;
										
										targetActionObject=wobj;
										targetActionObject2=tobj;
										bActionMode=true;
										return;
									}
								}	
							}
						}
					}
				}
				else if(actionMode==ActionMode.FITNESS_STUDIO)
				{
					if(baseWorldObject.thehuman.getAge()<14)
						return;
					
					if(baseWorldObject.thehuman.getFitnessValue()>=baseWorldObject.thehuman.fitnessValueMax)
						return;
										
					if(baseWorldObject.thehuman.energyValue < 60)
						return;
					
					if(baseWorldObject.thehuman.energyValue < 70)
					{
						int r1 = rand.nextInt(1);
						if(r1==0)
							return;
					}
					
					if(baseWorldObject.thehuman.energyValue < 80)
					{
						int r1 = rand.nextInt(2);
						if(r1==0)
							return;
					}
					
					
					//wahrscheinlichkeitsfaktor: hoher health attitude: eher ins fs gehen
					//wenig gesundheit: eher ins fs gehen
					int factor=(int)(100-baseWorldObject.thehuman.healthAttitude*30);
					if(factor<1)
						factor=1;
					int ir = rand.nextInt(factor);
					
					if(ir>20)
					{
						//Wenn Gesundheit niedrig ist: doch ins gym gehen
						int val = (int)(baseWorldObject.thehuman.getHealthValue()-baseWorldObject.thehuman.healthAttitude*10);
						if(val<1)
							val=1;
						int f2 = rand.nextInt();
						if(f2>20)
							return;
					}
										
					CCompany gym = CCompany.getNextActiveCompany(CompanyType.FITNESS_STUDIO, baseWorldObject.town, baseWorldObject.pos_x(), baseWorldObject.pos_y(), -1, 99999);
					if(gym==null)
						return;
					
					CWorldObject gymobject=null;
					CWorldObject temp_gymobject=null;
					
					if(gym.address_company.listWorldObjects.size()>0)
					{
						int count=0;
						while(gymobject==null)
						{
							count++;
							if(count>gym.address_company.listWorldObjects.size()+2)
								break;
							
							int index = rand.nextInt(gym.address_company.listWorldObjects.size());
							CWorldObject wobj = (CWorldObject)gym.address_company.listWorldObjects.get(index);
							
							if(wobj.bObjMoving || !wobj.onlineByWorkInput)
								continue;
							
							if(wobj.theobject.editoraction.contains("company_fitnessstudio_treadmill")
									|| wobj.theobject.editoraction.contains("company_fitnessstudio_stationarybike")
									|| wobj.theobject.editoraction.contains("company_fitnessstudio_shoulderpress")
									|| wobj.theobject.editoraction.contains("company_fitnessstudio_pecmachine")
									|| wobj.theobject.editoraction.contains("company_fitnessstudio_legpress")
									|| wobj.theobject.editoraction.contains("company_fitnessstudio_latpull")
									|| wobj.theobject.editoraction.contains("company_fitnessstudio_dumbbellrack")
									|| wobj.theobject.editoraction.contains("company_fitnessstudio_barbell"))
							{
								if(wobj.isOccupiedBy==null)
								{
									if(baseWorldObject.lastTargetActionObjectType.equals(wobj.theobject.editoraction))
										continue;
									
									if(reserveObject(wobj, 1))
									{
										gymobject=wobj;
										break;
									}
								}
							}
						}
					}
					
					if(gymobject!=null)
					{
						targetActionObject=gymobject;
						targetActionObject.iOccupied1_Arrived=0;
						bActionMode=true;
					}
					
					return;
				}
				else if(actionMode==ActionMode.ORDER_PIZZA)
				{
					//..
					
				}
				else if(actionMode==ActionMode.BREAK_ROOM)
				{
					CWorldObject workplace = baseWorldObject.thehuman.getWorkplaceByTime(false);
					if(workplace==null || workplace.belongsToCompany==null)
						return;
					
					CWorldObject breakobject1=null;
					CWorldObject fridge=workplace.theaddress.getWorldObject(baseWorldObject, "fridge", "coffeebeans", "", true, true, false, 5);
					CWorldObject wobj=null;
					int count=0;
					
					//Gdx.app.debug("", "size: : " + workplace.theaddress.listWorldObjects.size());
					
					while(true)
					{
						count++;
						if(count>workplace.theaddress.listWorldObjects.size())
							return;
						
						int index = rand.nextInt(workplace.theaddress.listWorldObjects.size());
						
						wobj = workplace.theaddress.listWorldObjects.get(index);
						if(wobj==null)
							continue;
						
						if(wobj.theobject.editoraction.contains("coffeepot") && wobj.isOccupiedBy==null)
						{
							breakobject1=wobj;
							breakobject1.isOccupiedBy=baseWorldObject;
							break;
						}
						
						if(wobj.theobject.editoraction.contains("_coffeemachine") && 
								wobj.isActiveByEnergyConsumption() && 
								wobj.isActiveByWaterConsumption() &&
								wobj.objectFilling2>0 //Tassen
								)
						{
							float healthattitude = baseWorldObject.thehuman.healthAttitude;
							float coffeinlevel = baseWorldObject.thehuman.coffeinLevel;
							
							if(coffeinlevel>0.3f) //über 1 tasse koffein im blut
							{
								if(healthattitude>2.5f)
									return;
								
								if(healthattitude>2 && coffeinlevel>0.4f) // 1.5 tassen koffein im blut
									return;
								
								if(healthattitude>1 && coffeinlevel>0.5f) // 2 tassen koffein im blut
									return;
								
								if(healthattitude>0.5f && coffeinlevel>0.6f) // über 2 tassen koffein im blut
									return;
							}
							
							if(wobj.objectFilling==0)
							{
								//Fridge vorhanden? -> Kaffeemachine auffüllen
								if(fridge==null)
									continue;
							}
							
							if(reserveObject(wobj, 1))
							{
								breakobject1=wobj;
								
								if(wobj.objectFilling==0)
								{
									fridge.isOccupiedBy=baseWorldObject;
									targetActionObject9=fridge;
								}
								
								break;
							}
						}
						
						
						if(wobj.theobject.editoraction.contains("_fruitplate"))
						{
							if(baseWorldObject.thehuman.fruitLevel>=3)
								continue;
								
							if(baseWorldObject.thehuman.getEatPercent() < 80)
							{
								if(wobj.objectFilling==0) //Fruitplate ist leer
								{
									//Fridge vorhanden? -> Fruitplate auffüllen
									if(fridge==null)
										continue;
								}
								
								if(reserveObject(wobj, 1))
								{
									breakobject1=wobj;
									if(wobj.objectFilling==0)
									{
										fridge.isOccupiedBy=baseWorldObject;
										targetActionObject9=fridge;
									}
									break;
								}
							}
						}
						
						
						if(wobj.theobject.editoraction.contains("_billard") ||
								wobj.theobject.editoraction.contains("_foosball"))
						{
							if(reserveObject(wobj, 1))
							{
								breakobject1=wobj;
								break;
							}
							else if(reserveObject(wobj, 2))
							{
								breakobject1=wobj;
								break;
							}
						}
					}
					
					if(breakobject1!=null)
					{
						targetActionObject=breakobject1;
						bActionMode=true;
						return;
					}							
					
				}
				else if(actionMode==ActionMode.PUB_ACTION)
				{
					//- bar,barkeeper mit worktime==aktuell
					//- age 18-x
					//- health attitude in entscheidung was gemacht wird
					//- müdigkeitslevel
					
					if(baseWorldObject.thehuman.energyValue < 50)
						return;
					
					if(baseWorldObject.thehuman.energyValue < 60 && baseWorldObject.thehuman.getHappynessValue()<80)
					{
						int r1 = rand.nextInt(2);
						if(r1==0)
							return;
					}
					
					if(baseWorldObject.thehuman.energyValue < 70 && baseWorldObject.thehuman.getHappynessValue()<85)
					{
						int r1 = rand.nextInt(3);
						if(r1==0)
							return;
					}
					
					if(baseWorldObject.thehuman.getAge()<18)
						return;
					
					//if(gameWorld.townMoney < CAction.initMoneyActionMinTownMoney)
					//	return;
					
					//Nächstes pub finden das offen ist
					CCompany pub=CCompany.getNextActiveCompany(CompanyType.PUB, baseWorldObject.town, baseWorldObject.pos_x(), baseWorldObject.pos_y(), -1, 99999);
					if(pub==null)
						return;
					
					//int r1 = rand.nextInt(Math.round(parentWorldObject.thehuman.healthAttitude*100));
					//if(r1<50)
					{
						CWorldObject pubobject=null;
						if(pub.address_company.listWorldObjects.size()>0)
						{
							int count=0;
							while(pubobject==null)
							{
								count++;
								if(count>pub.address_company.listWorldObjects.size()+2)
									break;
								
								int index = rand.nextInt(pub.address_company.listWorldObjects.size());
								CWorldObject wobj = (CWorldObject)pub.address_company.listWorldObjects.get(index);
								
								if(wobj.bObjMoving)
									continue;
								
								if(wobj.theobject.editoraction.contains("company_pub_workingplace_bar"))
								{
									//Entscheidung ob bier konsumiert wird abhängig von gesundheit und einstellung zur gesundheit
									//float health = parentWorldObject.thehuman.getHealthValue();
									//float hatt = parentWorldObject.thehuman.healthAttitude;
									
									//if(health<30 && hatt>0.5f)
									//	continue;
									//if(health<40 && hatt>1f)
									//	continue;
									//if(health<50 && hatt>1.4f)
									//	continue;
									//if(health<60 && hatt>1.8f)
									//	continue;
									//if(health<70 && hatt>2.3f)
									//	continue;
									//if(health<80 && hatt>2.8f)
									//	continue;
									
									
									//Platz zufällig auswählen - wenn 2 mal fehlgeschlagen: alle plätze durchgehen  
									int r2 = rand.nextInt(7);
									
									if(reserveObject(wobj, r2))
										pubobject=wobj;
									
									if(pubobject==null)
									{
										r2 = rand.nextInt(7);
										
										if(reserveObject(wobj, r2))
											pubobject=wobj;
									}
									
									if(pubobject==null)
									{
										if(reserveObject(wobj, 1))
											pubobject=wobj;
										else if(reserveObject(wobj, 2))
											pubobject=wobj;
										else if(reserveObject(wobj, 3))
											pubobject=wobj;
										else if(reserveObject(wobj, 4))
											pubobject=wobj;
										else if(reserveObject(wobj, 5))
											pubobject=wobj;
										else if(reserveObject(wobj, 6))
											pubobject=wobj;
										else if(reserveObject(wobj, 7))
											pubobject=wobj;
									}
								}
								
								if(wobj.theobject.editoraction.contains("company_pub_table"))
								{
									//Entscheidung ob bier konsumiert wird abhängig von gesundheit und einstellung zur gesundheit
									//float health = parentWorldObject.thehuman.getHealthValue();
									//float hatt = parentWorldObject.thehuman.healthAttitude;
									
									//if(health<30 && hatt>0.5f)
									//	continue;
									//if(health<40 && hatt>1f)
									//	continue;
									//if(health<50 && hatt>1.4f)
									//	continue;
									//if(health<60 && hatt>1.8f)
									//	continue;
									//if(health<70 && hatt>2.3f)
									//	continue;
									//if(health<80 && hatt>2.8f)
									//	continue;
									
									if(reserveObject(wobj, 1))
										pubobject=wobj;
									else if(reserveObject(wobj, 2))
										pubobject=wobj;
									else if(reserveObject(wobj, 3))
										pubobject=wobj;
									else if(reserveObject(wobj, 4))
										pubobject=wobj;
								}

								if(wobj.theobject.editoraction.contains("_billard") ||
										wobj.theobject.editoraction.contains("_foosball")
										)
								{
									if(reserveObject(wobj, 1))
										pubobject=wobj;
									else if(reserveObject(wobj, 2))
										pubobject=wobj;
								}
								
								
								if(wobj.theobject.editoraction.contains("_pinball") ||
										wobj.theobject.editoraction.contains("_arcademachine")
										)
								{
									if(reserveObject(wobj, 1))
										pubobject=wobj;
								}
								
								if(pubobject!=null)
									break;
							}
						}
						
						if(pubobject!=null)
						{
							targetActionObject=pubobject;
							bActionMode=true;
						}
					}
					
					return;
				}
				else if(actionMode==ActionMode.TAKE_OUT_GARBAGE)
				{
					//recyclingcenter_garbagebag
					//recyclingcenter_garbagecontainer
					//Gibt es einen Müllcontainer der nicht voll ist?
					//Gibt es einen Mülleimer?
					//Gibt es Müll?
					
					if(baseWorldObject.thehuman.energyValue < 70)
						return;
					
					CAddress adr=null;
					
					CWorldObject workplace = baseWorldObject.thehuman.getWorkplaceByTime(false);
					if(workplace!=null)
						adr=workplace.theaddress;
					else
						adr=baseWorldObject.theaddress;
					
					if(adr==null)
						return;
					
					//Nur wenn sich Resident gerade auf der Adr befindet
					if(!adr.testpoint(baseWorldObject.pos_x(), baseWorldObject.pos_y()))
						return;
					
					CWorldObject gc = null;
					CWorldObject gca = null;
					CWorldObject gb = null;
					int distGarbage = -1;
					int distContainer = -1;
					int distCan = -1;
					for(CWorldObject wobj : adr.listWorldObjects)
					{
						if(wobj.theobject.editoraction.contains("recyclingcenter_garbagecontainer") && wobj.objectFilling < wobj.getObjectFillingMax() && wobj.isOccupiedBy == null)
						{
							int disttemp = CHelper.getEuclidianDistance(baseWorldObject, wobj);
							if(distContainer==-1 || distContainer>disttemp)
							{
								distContainer=disttemp;
								gc=wobj;
							}
						}
						
						if(wobj.theobject.editoraction.contains("garbagecan") && wobj.isOccupiedBy == null)
						{
							int disttemp = CHelper.getEuclidianDistance(baseWorldObject, wobj);
							if(distCan==-1 || distCan>disttemp)
							{
								distCan=disttemp;
								gca=wobj;
							}
						}
						
						if((wobj.theobject.editoraction.contains("recyclingcenter_garbagebag") ||
								wobj.theobject.editoraction.contains("supermarket_buyin")) &&
								wobj.isOccupiedBy==null)
						{
							int disttemp = CHelper.getEuclidianDistance(baseWorldObject, wobj);
							if(distGarbage==-1 || distGarbage>disttemp)
							{
								distGarbage=disttemp;
								gb=wobj;
							}
						}
					}
					
					//Bringe Can zu Container
					if(gca!=null && 
							gca.objectFilling>(((float)gca.getObjectFillingMax()/100f)*80f) &&
							gc!=null
							)
					{
						gca.isOccupiedBy=baseWorldObject;
						gc.isOccupiedBy=baseWorldObject;
						targetActionObject=gca;
						targetActionObject2=gc;
						bActionMode = true;
						return;
					}

					//Bringe Garbage zu Can
					if(gca!=null && gb!=null && gca.objectFilling<gca.getObjectFillingMax())
					{
						gb.isOccupiedBy=baseWorldObject;
						gca.isOccupiedBy=baseWorldObject;
						targetActionObject=gb;
						targetActionObject2=gca;
						bActionMode = true;
						return;
					}
					
					//Bringe Garbage zu Container
					if(gc!=null && gb!=null)
					{
						gb.isOccupiedBy=baseWorldObject;
						gc.isOccupiedBy=baseWorldObject;
						targetActionObject=gb;
						targetActionObject2=gc;
						bActionMode = true;
						return;
					}
				}
				else if(actionMode==ActionMode.WASH_DISHES)
				{

					if(baseWorldObject.thehuman.energyValue < 60)
						return;
					
					if(baseWorldObject.thehuman.getAge()<12)
						return;
					
					//targetActionObject	Dinnertable
					//targetActionObject2	Sink
					//targetActionObject3	Cupboard
					
					targetActionObject = baseWorldObject.getNextDinnertableEvent(-1);
					
					if(targetActionObject == null)
						return;
					
					if(targetActionObject.actiontime1check || targetActionObject.actiontime2check || targetActionObject.actiontime3check)
						return;
					
					//Alles vorhanden was benötigt wird?
					CAddress adr = targetActionObject.theaddress;

					//Nur wenn sich Resident gerade auf der Adr befindet
					if(!adr.testpoint(baseWorldObject.pos_x(), baseWorldObject.pos_y()))
						return;
					
					for(CWorldObject wobj : adr.listWorldObjects)
					{
						//Sink
						if(wobj.theobject.editoraction.contains("kitchen_sink"))
							targetActionObject2=wobj;
						
						//Cupboard
						if(wobj.theobject.editoraction.contains("kitchen_cupboard"))
							targetActionObject3=wobj;
					}
					
					//Gehe direkt in bActionMode ohne gotoaction 
					if(targetActionObject!=null && targetActionObject2!=null && targetActionObject3!=null)
					{
						bActionMode=true;
					}
					
					return;
				}
				else if(actionMode==ActionMode.EAT_DINNER)
				{
					targetActionObject = baseWorldObject.getNextDinnertableEvent(0);
					
					//Gehe direkt in bActionMode ohne gotoaction 
					if(targetActionObject!=null)
					{
						bActionMode=true;
					}
					
					return;
				}
				else if(actionMode==ActionMode.TOILET)
				{
					if(baseWorldObject.thehuman.toiletValue < baseWorldObject.thehuman.toiletValueTrigger)
						return;
					
					CWorldObject toiletobj[] = {null, null};
					CAddress adr=null;
					
					//Aktuelle Work Adr
					CWorldObject wp = baseWorldObject.thehuman.getWorkplaceByTime(true);
					if(wp!=null)
					{
						adr = wp.theaddress;
						//CAddress adr = gameWorld.getAddressByPoint(baseWorldObject.pos_x(), baseWorldObject.pos_y());
						if(adr!=null)
							toiletobj = adr.getToiletObjects(baseWorldObject.thehuman.gender);
					}
					
					//Zu Hause
					if((adr==null || toiletobj[0]==null) && baseWorldObject.theaddress!=null)
					{
						if(baseWorldObject.thehuman.toiletValue < baseWorldObject.thehuman.toiletValueTriggerRed)
						{
							if(baseWorldObject.thehuman.isWorking()) //Nur wenn Trigger Red dann von Arbeit nach Hause gehen
								return;
						}						
						
						//Wenn Druck groß, gehe nach Hause auf Toilette
						toiletobj = baseWorldObject.theaddress.getToiletObjects(baseWorldObject.thehuman.gender);
					}
					
					if(toiletobj[0]!=null)
					{
						targetActionObject = toiletobj[0];
						targetActionObject.isOccupiedBy=baseWorldObject;
						bActionMode=true;
						
						if(toiletobj[1]!=null)
						{
							targetActionObject2=toiletobj[1];
							//targetActionObject2.isOccupiedBy=parentWorldObject; -> dynamisch in der action
						}
					}
					
					return;
				}
				else if(actionMode==ActionMode.COOK_DINNER)
				{
					actionState=-3;
					targetActionObject=null;	//Dinner Table
					targetActionObject2=null;	//Fridge
					targetActionObject3=null;	//Stove
					targetActionObject4=null;	//cupboard
					targetActionObject5=null;	//sink
					
					int timenr=-1;
					
					//Suche nächsten Dinner Table Task
					for(CWorldObject to : baseWorldObject.thehuman.taskobjects.values())
					{
						if(to.theobject.editoraction.contains("diningroom_diningtable"))
						{
							//mindestens ein platz muss besetzt sein
							if(to.owner==null && to.owner2==null && to.owner3==null && to.owner4==null && to.owner5==null && to.owner6==null && to.owner7==null && to.owner8==null)
								continue;
							
							if(to.worker!=null && to.worker.uniqueId==baseWorldObject.uniqueId)
							{
								Boolean bCook=false;
								
								if(to.actiontime1check==false && (to.actiontime1==0 && (town.gameWorld.worldTime.hours==23 || town.gameWorld.worldTime.hours==22)))
								{
									timenr=1;
									bCook=true;
								}
								if(to.actiontime2check==false && (to.actiontime2==0 && (town.gameWorld.worldTime.hours==23 || town.gameWorld.worldTime.hours==22)))
								{
									timenr=2;
									bCook=true;
								}
								if(to.actiontime3check==false && (to.actiontime3==0 && (town.gameWorld.worldTime.hours==23 || town.gameWorld.worldTime.hours==22)))
								{
									timenr=3;
									bCook=true;
								}
								
								if(to.actiontime1check==false && (town.gameWorld.worldTime.hours == to.actiontime1-2 || town.gameWorld.worldTime.hours == to.actiontime1-1))
								{
									timenr=1;
									bCook=true;									
								}
								if(to.actiontime2check==false && (town.gameWorld.worldTime.hours == to.actiontime2-2 || town.gameWorld.worldTime.hours == to.actiontime2-1))
								{
									timenr=2;
									bCook=true;									
								}
								if(to.actiontime3check==false && (town.gameWorld.worldTime.hours == to.actiontime3-2 || town.gameWorld.worldTime.hours == to.actiontime3-1))
								{
									timenr=3;
									bCook=true;									
								}
								
								if(bCook)
								{
									if(to.tempcount==2)
										continue;
									
									to.actiontimenr=timenr;
									
									targetActionObject=to;
									break;
								}
							}
						}
					}
					
					if(targetActionObject!=null)
					{
						//Alle benötigten Objekte auf Adresse vorhanden und einsatzbereit?
						
						CAddress adr = targetActionObject.theaddress;
						for(CWorldObject wobj : adr.listWorldObjects)
						{
							//Fridge
							int requiredFood=targetActionObject.getOwnerCount()*4;
							
							if(wobj.theobject.editoraction.contains("kitchen_fridge") && wobj.isActiveByEnergyConsumption() && wobj.getObjectFillingMulti() >=requiredFood)
								targetActionObject2=wobj;
							
							//Stove
							if(wobj.theobject.editoraction.contains("kitchen_stove") && wobj.isActiveByEnergyConsumption())
								targetActionObject3=wobj;
							
							//Cupboard
							if(wobj.theobject.editoraction.contains("kitchen_cupboard"))
								targetActionObject4=wobj;
							
							//Sink
							if(wobj.theobject.editoraction.contains("kitchen_sink") && wobj.isActiveByWaterConsumption())
								targetActionObject5=wobj;
						}
						
						//Gdx.app.debug("", ""+targetActionObject + ", " + targetActionObject2 + ", " + targetActionObject3 + ", " +targetActionObject4 + ", " + targetActionObject5);
						
						//Gehe direkt in bActionMode ohne gotoaction 
						if(targetActionObject!=null && targetActionObject2!=null && targetActionObject3!=null&& targetActionObject4!=null && targetActionObject5!=null)
						{
							targetActionObject.actiontimenr=timenr;
							bActionMode=true;
							return;
						}
					}
					
					return;
				}
				else if(actionMode==ActionMode.GOTO_DOCTOR)
				{
					int triggerhealthvalue=50;
					if(baseWorldObject.thehuman.healthAttitude>1f)
						triggerhealthvalue=55;
					if(baseWorldObject.thehuman.healthAttitude>1.5f)
						triggerhealthvalue=60;
					if(baseWorldObject.thehuman.healthAttitude>2f)
						triggerhealthvalue=65;
					if(baseWorldObject.thehuman.healthAttitude>2.5f)
						triggerhealthvalue=70;
						
					if((baseWorldObject.thehuman.sick>0 && baseWorldObject.thehuman.doctorHealingValue==0) || baseWorldObject.thehuman.getHealthValue()<triggerhealthvalue)
					{
						//Nächstes aktives Doctor's Office finden
						CCompany comp = CCompany.getNextActiveCompany(CompanyType.DOCTORS_OFFICE, baseWorldObject.town, baseWorldObject.pos_x(), baseWorldObject.pos_y(), baseWorldObject.uniqueId, 99999);
						if(comp!=null)
						{
							CWorldObject[] wobjs = comp.getActive_doctorsOffice(baseWorldObject.uniqueId);
							
							if(wobjs!=null)
							{
								if(wobjs[0].isOccupiedBy!=null && wobjs[0].isOccupiedBy.uniqueId!=baseWorldObject.uniqueId)
									return;
								
								targetActionObject = wobjs[1]; //Reception
								targetActionObject2 = wobjs[0]; //Waiting Chair
								targetActionObject3 = wobjs[2]; //Waiting Table
								targetActionObject2.isOccupiedBy = baseWorldObject;
								bActionMode=true;
								return;
							}
						}
					}
					
					return;
				}
				else if(actionMode==ActionMode.FUNERAL_ATTEND)
				{
					if(baseWorldObject.theaddress==null)
						return;
				
					//Nicht an Beerdigung als Besucher teilnehmen wenn Fahrer oder Sprecher
					String ttext="";
					for(CWorldObject tobj : baseWorldObject.thehuman.taskobjects.values())
						ttext+=tobj.theobject.editoraction;
					ttext=ttext.toLowerCase();
					//if(baseWorldObject.uniqueId==2)
					//	Gdx.app.debug("", ""+ttext + ", " + baseWorldObject.thehuman.taskobjects.size());
					if(ttext.contains("cemetery"))
						return;
					
					for(CWorldObject wobj : baseWorldObject.theaddress.listWorldObjects)
					{
						if(wobj.isHuman() && wobj.bIsDead && wobj.actionstring1.equals("show_coffin"))
						{
							targetActionObject=wobj;
							break;
						}
					}
					
					//2. durchlauf notwendig, da targetactionobject gesetzt sein muss
					if(targetActionObject!=null)
					{
						CAddress adr =town.gameWorld.getAddressByPoint(targetActionObject.pos_x(), targetActionObject.pos_y());
						
						if(adr!=null)
						{
							for(CWorldObject wobj : adr.listWorldObjects)
							{
								//actionvar9==id des deceased
								//Set Rostrum and Grave, damit action gecanceled wird wenn objekt verschoben wird
								
								//Grave Empty
								if(wobj.theobject.editoraction.contains("company_urbancemetery_graveempty") && wobj.actionvar9==targetActionObject.uniqueId)
									targetActionObject2 = wobj;
								
								//Rostrum
								if(wobj.theobject.editoraction.contains("company_urbancemetery_rostrum") && wobj.actionvar9==targetActionObject.uniqueId)
									targetActionObject3 = wobj;
							}
						}
						
						if(targetActionObject2!=null && targetActionObject3!=null && targetActionObject3.worker!=null && targetActionObject3.worker.thehuman.canWork())
						{
							bActionMode=true;
						}
					}
					
					return;
				}
				else if(actionMode==ActionMode.VEHICLE_REFUEL)
				{
					//if(gameWorld.townMoney < CAction.initMoneyActionMinTownMoney)
					//	return;
					
					if(baseWorldObject.thehuman.car==null && targetActionObject5==null)
						return;
					
					if(targetActionObject5==null)
						targetActionObject5=baseWorldObject.thehuman.car;
					
					if(targetActionObject5.fuelValue>targetActionObject5.fuelValueMax/3)
						return;
					
					//Suche nächste offene Tankstelle
					//CCompany comp = gameWorld.getNearestActiveCompany(CompanyType.GAS_STATION, parentWorldObject.thehuman.car);
					//CompanyType ctype, List<CCompany> clist, int x, int y
					CCompany comp = CCompany.getNextActiveCompany(CompanyType.GAS_STATION, baseWorldObject.town, targetActionObject5.pos_x(), targetActionObject5.pos_y(), -1, 99999);
					
					if(comp!=null)
					{
						CWorldObject gaspump=null;
						CWorldObject checkout=null;
						
						for(CWorldObject wobj : comp.address_company.listWorldObjects)
						{
							//Gas Pump muss aktiv(workoutput) sein
							if(wobj.theobject.editoraction.contains("company_gasstation_gaspump"))
							{
								if(wobj.isActiveByEnergyConsumption() && wobj.onlineByWorkInput)
								{
									gaspump=wobj;
								}
							}
							
							//Checkout muss aktiv(energy) und mit worker besetzt sein				
							if(wobj.theobject.editoraction.contains("company_gasstation_workingplace_cashpoint"))
							{
								if(wobj.isWorkerActive() && wobj.isActiveByEnergyConsumption() && wobj.isActiveByWaterConsumption())
								{
									checkout=wobj;
								}
							}
						}
						
						if(gaspump!=null && checkout!=null)
						{
							gaspump.isOccupiedBy=baseWorldObject;
							
							targetActionObject=gaspump;
							targetActionObject2=checkout;
							
							targetCompany=comp;
							bActionMode=true;
						}
						
						return;
					}
				}
				else if(actionMode==ActionMode.SUPERMARKET_BUYIN) //Im Supermarkt einkaufen und nächstes zu befüllendes Task-Objekt befüllen, für das der Einwohner zuständig ist
				{
					//Achtung: 
					//Grund dass nicht eingekauft wird ist oft dass townmoney zu wenig
					
					targetActionObject=null;
					targetActionObject2=null;
					targetActionObject3=null;
					targetActionObject4=null;
					
					//if(gameWorld.townMoney < CAction.initMoneyActionMinTownMoney)
					//	return;
					
					//if(baseWorldObject.thehuman.getName().contains("Anthony Mitchell"))
					//	Gdx.app.debug("debug buyin 1", "");
					
					if(baseWorldObject.thehuman!=null && baseWorldObject.thehuman.taskobjects.size()>0)
					{
						for(CWorldObject obj1 : baseWorldObject.thehuman.taskobjects.values())
						{
							if(obj1.theobject.editoraction.contains("fridge"))
							{
								if(!obj1.isActiveByEnergyConsumption() || !obj1.isActiveByWaterConsumption())  
									continue;
								
								if(obj1.getObjectFillingMulti()<obj1.getObjectFillingMultiMax()/2)
								{
									//Während der Arbeitszeit nicht für Heimadresse einkaufen
									if(baseWorldObject.theaddress!=null)
									{
										if(baseWorldObject.thehuman.timeForWork() && obj1.theaddress.addressId==baseWorldObject.theaddress.addressId)
											continue;
									}
									
									targetActionObject3=obj1;
									
									break;
								}
							}
						}
					}
					
					if(targetActionObject3!=null)
					{
						CCompany nearestSupermarket = CCompany.getNextActiveCompany(CompanyType.SUPERMARKET, baseWorldObject.town, baseWorldObject.pos_x(), baseWorldObject.pos_y(), -1, 99999);
						
						//if(baseWorldObject.thehuman.getName().contains("Anthony Mitchell"))
						//	Gdx.app.debug("debug buyin 2", "");
						
						if(nearestSupermarket!=null)
						{
							//if(baseWorldObject.thehuman.getName().contains("Anthony Mitchell"))
							//	Gdx.app.debug("debug buyin 3", "");
							
							targetCompany=nearestSupermarket;
							objtemp = targetCompany.address_company.listWorldObjects.stream().filter(item->item.theobject.editoraction.contains("supermarket_shoppingcart") && item.isOccupiedBy==null && !item.bDeleted).findFirst();
						}
					}
				}
				else if(valueType==ValueType.SLEEP)
				{
					objtemp=null;
					obj=null;
					targetActionObject=null;
					
					if(baseWorldObject.thehuman.bed!=null)
						obj=baseWorldObject.thehuman.bed;
				}
				else if(actionMode==ActionMode.SHOWER)
				{
					//Entscheidung ob Shower oder Bathtub
					
					//wenn happiness unter grenzwert -> bathtub
					//wenn müdigkeit unter grenzwert -> bathtub 
					
					//if(baseWorldObject.uniqueId==4)
					//	Gdx.app.debug("", "test1");
					
					if(baseWorldObject.theaddress==null)
						return;
					
					if(baseWorldObject.thehuman.cleanValue<baseWorldObject.thehuman.cleanValueTrigger)
						return;

					
					int iBath=0;
					
					if(baseWorldObject.thehuman.getHappynessValue()<80)
						iBath++;
					else 
						iBath--;
					
					if(town.gameWorld.worldTime.hours>16)
						iBath++;
					else
						iBath--;
					
					if(baseWorldObject.thehuman.sleepValue>18*3600)
						iBath++;
					else if(baseWorldObject.thehuman.sleepValue<3600*8)
						iBath--;

					if(baseWorldObject.thehuman.energyValue < 70)
						iBath++;
					else 
						iBath--;
					
					if(baseWorldObject.thehuman.energyValue < baseWorldObject.thehuman.energyValueTrigger)
						iBath=1;
					
					CWorldObject bathtub=null;
					CWorldObject shower=null;
					CWorldObject towelcabinet=null;
					CWorldObject laundrybasket=null;
					
					for(CWorldObject wobj : baseWorldObject.theaddress.listWorldObjects)
					{
						if(wobj.theobject.editoraction.contains("bathroom_bathtub") && wobj.isOccupiedBy==null && wobj.bDeleted==false && wobj.isActiveByWaterConsumption())
							bathtub=wobj;
						
						if(wobj.theobject.editoraction.contains("bathroom_shower") && wobj.isOccupiedBy==null && wobj.bDeleted==false && wobj.isActiveByWaterConsumption())
							shower=wobj;
						
						if(wobj.theobject.editoraction.contains("bathroom_towelcabinet") && wobj.bDeleted==false && wobj.objectFilling>0)
							towelcabinet=wobj;
					}
					
					if((bathtub!=null || shower!=null) && towelcabinet!=null)
					{
						if(shower!=null)
							targetActionObject=shower;
						
						if((shower==null && bathtub!=null) || (iBath>0 && bathtub!=null))
							targetActionObject=bathtub;
						
						targetActionObject2=towelcabinet;
						targetActionObject3=laundrybasket;
						targetActionObject.isOccupiedBy=baseWorldObject;
						
						bActionMode=true;
					}
					
					return;
				}
				else
				{
					//*********************************************************************
					//Objekt auf Adresse suchen auf der sich der Einwohner aktuell befindet
					//*********************************************************************
					if((!(valueType==ValueType.CLEAN) && !(valueType==ValueType.SLEEP)))
					{
						//CAddress locationAdr = gameWorld.getAddressByLocation(parentWorldObject.pos_x(), parentWorldObject.pos_y());
						CAddress locationAdr = town.gameWorld.getAddressByPolygonOverlap(baseWorldObject.getBoundingPolygon(IntersectionMode.COLLISION),-1);
						if(locationAdr!=null)
						{
							objtemp = locationAdr.listWorldObjects.stream().filter(item->item.theobject.editoraction.contains(editorActionString) && item.isOccupiedBy==null && item.bDeleted==false && item.bReachable==true).findFirst();
						}
					}
					
					//*******************************
					//Objekt auf Heim-Adresse suchen
					//*******************************
					if((objtemp==null || !objtemp.isPresent()))
					{
						if(baseWorldObject.theaddress!=null)
							objtemp = baseWorldObject.theaddress.listWorldObjects.stream().filter(item->item.theobject.editoraction.contains(editorActionString) && item.isOccupiedBy==null && item.bDeleted==false && item.bReachable==true).findFirst();
					}
				}
				
				if(objtemp!=null && objtemp.isPresent())
					obj=objtemp.get();
			}
			
			
			//No action on Object with no source
			if(obj!=null && obj.getEnergyConsumption()>0 && town.gameWorld.getEnergyConsumption() > town.gameWorld.getEnergyOutput())
				return;
			
			if(obj!=null && obj.getWaterConsumption()>0 && town.gameWorld.getWaterConsumption() > town.gameWorld.getWaterOutput())
				return;
					
			
			if(obj!=null && obj!=targetActionObject)
			{
				//Nur arbeiten wenn Projekt
				if(obj.theobject.editoraction.contains("company_objectdesign_officeworkingplace_artist") && obj.researchObject==null)
					return;
				
				targetActionObject=obj;
				
				int tx = targetActionObject.pos_x()+targetActionObject.width/2;
				int ty = targetActionObject.pos_y()+targetActionObject.height/2;
				
				//if(tx!=sourceWorldObject.ziel_x && ty!=sourceWorldObject.ziel_y)
				{
					//if(parentWorldObject.activeActionMode==ActionMode.VEHICLE_REFUEL)
					//	Gdx.app.debug("debug refuel 8_1", "ziel_x: " + parentWorldObject.ziel_x);
					
					baseWorldObject.initTargetPath(tx, ty, false, targetActionObject);
					
					//if(parentWorldObject.activeActionMode==ActionMode.VEHICLE_REFUEL)
					//	Gdx.app.debug("debug refuel 8_2", "activeActionModeTemp: " + parentWorldObject.activeActionModeTemp + ", action-actionMode: " + actionMode);
					
					//trigger -> es wurde in inittargetpath umgeschaltet auf zwischenaction
					if(baseWorldObject.activeAction!=null) //Es wurde auf Zwischenaction geswitched -> abbrechen
					{
						bGotoActionMode=false;
						bActionMode=false;
						targetActionObject=null;
						return;
					}
					
					//if(parentWorldObject.activeActionMode==ActionMode.VEHICLE_REFUEL)
					//	Gdx.app.debug("debug refuel 8_3", "ziel_x: " + parentWorldObject.ziel_x + ", path: " + parentWorldObject.path);
					
					baseWorldObject.pathFinding(stateTime);
					
					//if(parentWorldObject.activeActionMode==ActionMode.VEHICLE_REFUEL)
					//	Gdx.app.debug("debug refuel 9",  "ziel_x: " + parentWorldObject.ziel_x + ", path: " + parentWorldObject.path);
					
					if(baseWorldObject.path==null) // || parentWorldObject.path.items.length<17)
					{
						//if(parentWorldObject.activeActionMode==ActionMode.VEHICLE_REFUEL)
						//	Gdx.app.debug("debug refuel 10", "");
						//if(parentWorldObject.uniqueId==428)
						//	Gdx.app.debug("", "jumpout1");
						
						baseWorldObject.bJumpOut=true;
						bGotoActionMode=false;
						bActionMode=false;
						targetActionObject=null;
						return;
					}
					
					if(baseWorldObject.path==null || baseWorldObject.path.size==0)
					{
						bGotoActionMode=false;
						targetActionObject=null;
					}
					else
					{
						bGotoActionMode=true;
						targetActionObject.isOccupiedBy=baseWorldObject;
						actionDuration=0;
					}
				}
			}
			else
			{
				targetActionObject=null;
			}
		}
	}
	
	private Boolean initComplexWorkAction(CWorldObject obj)
	{
		//Battle Priest
		if(obj.theobject.editoraction.contains("company_church_workingplace_battlepriest"))
		{
			valueType = ValueType.WORK_COMPLEX;
			targetActionObject = obj;
			//bGotoActionMode=true;
			//gotoObject(obj, true);
			bActionMode=true;
			return true;
		}

		//Battle Priest
		if(obj.theobject.editoraction.contains("company_construction_pickup1_traffic_car"))
		{
			valueType = ValueType.WORK_COMPLEX;
			targetActionObject = obj;
			//bGotoActionMode=true;
			//gotoObject(obj, true);
			bActionMode=true;
			return true;
		}
		
		
		//Fitness Trainer
		if(obj.theobject.editoraction.contains("company_fitnessstudio_fitnessworkingplace"))
		{
			valueType = ValueType.WORK_COMPLEX;
			
			targetActionObject = obj;
			
			if(targetActionObject.isActiveByEnergyConsumption() && targetActionObject.isActiveByWaterConsumption())
				bActionMode=true;
			
			return true;
		}
		
		//Supermarket Warehouse
		if(obj.theobject.editoraction.contains("supermarket_shelf"))
		{
			valueType = ValueType.WORK_COMPLEX;
			
			targetActionObject3 = obj;
			
			//targetActionObject=null;  // pallettruck
			//targetActionObject2=null; // warehouse
			//targetActionObject3=null; // shelf
			targetCompany=null;
			
			if(targetActionObject3!=null)
			{
				targetCompany=targetActionObject3.belongsToCompany;
				if(targetCompany==null)
					return true;
				
				for(CWorldObject wobj : targetCompany.address_company.listWorldObjects)
				{
					int takeFilling=10;
					if(wobj.theobject.editoraction.contains("supermarket_foodpallet") && !wobj.bDeleted && wobj.isOccupiedBy==null && wobj.isOccupiedByExtern==null && wobj.objectFilling>=takeFilling)
						targetActionObject2=wobj;
					
					if(wobj.theobject.editoraction.contains("supermarket_pallettruck") && !wobj.bDeleted && wobj.isOccupiedBy==null && wobj.isOccupiedByExtern==null && wobj.isActiveByEnergyConsumption() && wobj.isActiveByWaterConsumption())
						targetActionObject=wobj;
				}
				
				if(targetActionObject!=null && targetActionObject2!=null && targetActionObject3!=null)
				{
					targetActionObject.isOccupiedBy=baseWorldObject;
					targetActionObject2.isOccupiedBy=baseWorldObject;
					targetActionObject3.isOccupiedBy=baseWorldObject;
					bActionMode=true;
				}
			}
			
			return true;
		}
		
		//Medical Receptionist
		if(obj.theobject.editoraction.contains("company_doctorsoffice_reception_desk"))
		{
			valueType = ValueType.WORK_COMPLEX;
			
			targetActionObject = obj;
			
			if(targetActionObject.isActiveByEnergyConsumption() && targetActionObject.isActiveByWaterConsumption())
				bActionMode=true;
			
			return true;
		}
		
		//Doctor
		if(obj.theobject.editoraction.contains("company_doctorsoffice_treatment_chair"))
		{
			valueType = ValueType.WORK_COMPLEX;
			
			targetActionObject = obj;
			
			if(targetActionObject.isActiveByEnergyConsumption() && targetActionObject.isActiveByWaterConsumption())
				bActionMode=true;
			
			return true;
		}
		
		//Pub-Bar
		if(obj.theobject.editoraction.contains("company_pub_workingplace_bar"))
		{
			valueType = ValueType.WORK_COMPLEX;
			
			targetActionObject = obj;
			
			if(targetActionObject.isActiveByEnergyConsumption() && targetActionObject.isActiveByWaterConsumption())
				bActionMode=true;
			
			return true;
		}
		
		//Müllfahrzeug
		if(obj.theobject.editoraction.contains("company_recyclingcenter_garbagetruck"))
		{
			//- wenn es müllcontainer mit filling > 0 gibt action starten
			//	fahre die nahen müllcontainer zuerst an
			//- worker geht zu truck, und verschwindet
			//- truck fährt los
			
			valueType = ValueType.WORK_COMPLEX;
			targetActionObject = obj;
			
			if(targetActionObject.fuelValue < targetActionObject.fuelValueMax/3) //Tanken wenn notwendig
			{
				baseWorldObject.action_vehiclerefuel.targetActionObject5=obj;
				cancelAction("cancel garbage truck action, refuel garbage truck");
				baseWorldObject.bInitAction_NoResetTargetObjects=true;
				baseWorldObject.action_vehiclerefuel.initAction(town.gameWorld.stateTime);
				if(baseWorldObject.action_vehiclerefuel.bActionMode)
				{
					//parentWorldObject.activeActionTemp=parentWorldObject.activeAction;
					//parentWorldObject.activeActionModeTemp=parentWorldObject.activeActionMode;
						//	das bringt nix -> action wurde noch nicht aufgenommen
					
					baseWorldObject.activeAction=baseWorldObject.action_vehiclerefuel;
					baseWorldObject.activeActionMode=baseWorldObject.action_vehiclerefuel.actionMode;
				}
				
				baseWorldObject.bInitAction_NoResetTargetObjects=false;
				
				return true;
			}
			
			if(targetActionObject.fuelValue<1) //Action nicht starten wenn kein Kraftstoff mehr vorhanden
				return true;
			
			if(baseWorldObject.town.gameWorld.worldGarbageContainers.size()>0)
			{
				List<CWorldObject> list = baseWorldObject.town.gameWorld.worldGarbageContainers.stream().filter(item->item.isOccupiedBy==null && item.objectFilling>item.getObjectFillingMax()/2).collect(Collectors.toList());
				
				for(CWorldObject wobj : list)
				{
					wobj.tempcount = CHelper.getEuclidianDistance(obj, wobj);
				}
				
				list.sort(new CWorldObject.TempCountComparator());
				
				if(list.size()>0)
				{
					targetActionObject2=list.get(0);
					targetActionObject2.isOccupiedBy=baseWorldObject;
				}
				if(list.size()>1)
				{
					targetActionObject3=list.get(1);
					targetActionObject3.isOccupiedBy=baseWorldObject;
				}
				if(list.size()>2)
				{
					targetActionObject4=list.get(2);
					targetActionObject4.isOccupiedBy=baseWorldObject;
				}
				if(list.size()>3)
				{
					targetActionObject5=list.get(3);
					targetActionObject5.isOccupiedBy=baseWorldObject;
				}
				if(list.size()>4)
				{
					targetActionObject6=list.get(4);
					targetActionObject6.isOccupiedBy=baseWorldObject;
				}
				
				if(targetActionObject2!=null)
				{
					bActionMode=true;
				}
			}
			
			return true;
		}
				
		//Funeral Speaker
		if(obj.theobject.editoraction.contains("company_urbancemetery_rostrum"))
		{
			//Gdx.app.debug("init complex work action", "__________test set rostrum 1");
		
			
			valueType=ValueType.WORK_COMPLEX;
			targetActionObject=obj;
			
			if(targetActionObject.actionstring1.equals("trigger_funeral") 
					&& targetActionObject.belongsToCompany!=null 
					&& targetActionObject.belongsToCompany.isActive(-1)
					)
			{
				
				//Actionvar9==id des deceased
				
				//Set Coffin and Grave, damit speaker action gecanceled wird wenn objekt verschoben wird
				for(CWorldObject wobj : obj.belongsToCompany.address_company.listWorldObjects)
				{
					//Grave Empty
					if(wobj.theobject.editoraction.contains("company_urbancemetery_graveempty") && wobj.actionvar9==targetActionObject.actionvar9)
					{
						targetActionObject2 = wobj;
					}
					
					//Gdx.app.setLogLevel(10);
					//Gdx.app.log("", "1");
					
					if(wobj.actionstring1.contains("show_coffin"))
					{
						targetActionObject3=wobj;
						
						//Gdx.app.log("", "2");
						
						if(wobj.actionstring1.contains("show_coffin_move"))
						{
							//Gdx.app.log("", "3");
							return true; //Rede ist beendet
						}
					}
				}				
				
				//Gdx.app.log("", "4");
				targetActionObject.isOccupiedBy=baseWorldObject; //Als Trigger dafür, ob Rostrum besetzt ist, oder ob Rede beendet ist zB für Driver
				bActionMode=true;
			}
			
			return true;
		}
		
		
		//Leichenwagen
		if(obj.theobject.editoraction.contains("company_urbancemetery_hearse_traffic_car"))
		{
			valueType=ValueType.WORK_COMPLEX;
			targetActionObject=obj;
			
			//Tanken?
			if(targetActionObject.fuelValue < targetActionObject.fuelValueMax/3) //Tanken wenn notwendig
			{
				baseWorldObject.action_vehiclerefuel.targetActionObject5=obj;
				cancelAction("cancel hearse action, refuel hearse");
				baseWorldObject.bInitAction_NoResetTargetObjects=true;
				baseWorldObject.action_vehiclerefuel.initAction(town.gameWorld.stateTime);
				
				if(baseWorldObject.action_vehiclerefuel.bActionMode)
				{
					//parentWorldObject.activeActionTemp=parentWorldObject.activeAction;
					//parentWorldObject.activeActionModeTemp=parentWorldObject.activeActionMode;
					//das bringt nix -> action wurde noch nicht aufgenommen/initialisiert
					
					baseWorldObject.activeAction=baseWorldObject.action_vehiclerefuel;
					baseWorldObject.activeActionMode=baseWorldObject.action_vehiclerefuel.actionMode;
				}
				
				baseWorldObject.bInitAction_NoResetTargetObjects=false;
				
				return true;
			}
			
			if(targetActionObject.fuelValue<1) //Action nicht starten wenn kein Kraftstoff mehr vorhanden
				return true;
			
			//Gdx.app.debug("", "init hearse 1");
						
			if(!obj.belongsToCompany.isActive(-1))
				return true;
						
			//Gdx.app.debug("", "init hearse 2 size: " + gameWorld.tempHumansDead.size());
			
			int deceasedId=0;
			
			//Gibt es einen Verstorbenen der abgeholt werden muss?
			for(CWorldObject wobj : town.gameWorld.tempHumansDead.values())
			{
				if(wobj.actionstring1.contains("show_coffin")) 
				{
					return true; //sarg muss erst zum grab gebracht werden 
				}
				
				if((wobj.isOccupiedBy==null || wobj.isOccupiedBy.uniqueId==baseWorldObject.uniqueId) && 
						!wobj.actionstring1.contains("show_coffin") && 
						!wobj.actionstring1.contains("show_grave"))
				{
					targetActionObject2=wobj;
					deceasedId=wobj.uniqueId;
				}
			}
			
			if(targetActionObject2==null)
				return true;
			
			//Gdx.app.debug("", "init hearse 3");
			
			//Action starten wenn
			for(CWorldObject wobj : obj.belongsToCompany.address_company.listWorldObjects)
			{
				//Empty Grave vorhanden und nicht occupied
				if(wobj.theobject.editoraction.contains("company_urbancemetery_graveempty"))
				{
					//if(wobj.isOccupiedBy!=null)
					//	Gdx.app.debug("", "grave empty occupied " + wobj.isOccupiedBy.thehuman.name);
					
					if(wobj.onlineByWorkInput && (wobj.isOccupiedBy==null || wobj.isOccupiedBy.uniqueId==baseWorldObject.uniqueId))
					{
						targetActionObject4=wobj;
					}
				}
				
				//Rednerpult aktiv und Worker hinterlegt
				if(wobj.theobject.editoraction.contains("company_urbancemetery_rostrum"))
				{
					if(wobj.onlineByWorkInput && wobj.worker!=null && wobj.isOccupiedBy==null) 
					{
						targetActionObject3=wobj;
					}
				}
			}
			
			//Gdx.app.debug("", "init hearse 3_1 targetActionObject4: " + targetActionObject4 + ", targetActionObject3: " + targetActionObject3);
			
			if(targetActionObject2!=null && targetActionObject3!=null && targetActionObject4!=null)
			{
				//Gdx.app.debug("init complete hearse action", "pwo: " + parentWorldObject.uniqueId);
				//Gdx.app.debug("", "init hearse 4");
				
				targetActionObject3.isOccupiedBy=baseWorldObject; //rostrum wird von driver occupied, als reservierungsmaßnahme
				targetActionObject2.isOccupiedBy=baseWorldObject;
				targetActionObject4.isOccupiedBy=baseWorldObject;
				
				targetActionObject3.actionvar9=deceasedId;
				targetActionObject4.actionvar9=deceasedId;
				bActionMode=true;
			}
			
			return true;
		}
		
		//Grave Digger
		if(obj.isHuman() && obj.bIsDead)
		{
			//Init Gravedigger 
			//Gdx.app.debug("", "init grave digger 1");
			
			if(obj.actionstring1.contains("show_coffin") && (obj.isOccupiedBy==null || obj.isOccupiedBy.uniqueId==baseWorldObject.uniqueId))
			{
				//Get Hearse Task Object
				Optional<CWorldObject> oobj = baseWorldObject.thehuman.taskobjects.values().stream().filter(item->item.theobject.editoraction.contains("company_urbancemetery_hearse_traffic_car")).findFirst();
				if(!oobj.isPresent() && oobj.get().belongsToCompany!=null)
					return true;
								
				//Gdx.app.debug("", "gravedigger init 1_1");
				
				for(CWorldObject wobj : oobj.get().belongsToCompany.address_company.listWorldObjects)
				{
					//Wird gerade Totenrede gehalten? -> nicht gravediggern
					if(wobj.theobject.editoraction.contains("company_urbancemetery_rostrum"))
					{
						if(wobj.actionvar9==obj.uniqueId && wobj.isOccupiedBy!=null)
						{
							//Gdx.app.debug("", "gravedigger init cancel ");			
							return true;
						}
					}
					
					//Grave Empty occupien
					if(wobj.theobject.editoraction.contains("company_urbancemetery_graveempty") && (wobj.isOccupiedBy==null || wobj.isOccupiedBy.uniqueId==baseWorldObject.uniqueId))
					{
						targetActionObject2 = wobj;
					}
				}
				
				if(targetActionObject2!=null)
				{
					//Gdx.app.debug("", "gravedigger init 2");
					
					targetActionObject2.isOccupiedBy = baseWorldObject;
					targetActionObject=obj;
					targetActionObject.isOccupiedBy = baseWorldObject;
					valueType=ValueType.WORK_COMPLEX;
					targetActionObject.actionstring2="gravedigger_action";
					bActionMode=true;
					//Gdx.app.debug("init complete gravedigger action", "pwo: " + parentWorldObject.uniqueId);
				}
			}
			return true;
		}
		
		
		if(obj.theobject.editoraction.contains("company_college_workingplace_researchlab"))
		{
			valueType=ValueType.WORK_COMPLEX;
			
			if(obj.researchObject!=null && obj.isActiveByEnergyConsumption())
			{
				obj.isOccupiedBy=baseWorldObject;
				targetActionObject=obj;
				bActionMode=true;
			}
			
			return true;
		}
		
		
		if(obj.theobject.editoraction.contains("company_church_workingplace_altar"))
		{
			valueType=ValueType.WORK_COMPLEX;
			
			obj.isOccupiedBy=baseWorldObject;
			targetActionObject=obj;
			bActionMode=true;
			
			return true;
		}
		
		if(obj.theobject.editoraction.contains("company_school_workingplace_studentsdesk"))
		{
			//Student Desk benötigt Teacher's Table mit hinterlegtem Worker
			CWorldObject tobj = obj.getTeachersDeskForStudentsDesk(0);
			
			//Abbrechen:
			if(tobj==null)
				return true;
			if(!tobj.worker.thehuman.canWork())
				return true;
		}
		
		if(obj.theobject.editoraction.contains("company_college_workingplace_studentsdesk"))
		{
			//Student Desk benötigt Professor
			CWorldObject tobj = obj.getTeachersDeskForStudentsDesk(1);
			
			//Abbrechen:
			if(tobj==null)
				return true;
			if(!tobj.worker.thehuman.canWork())
				return true;
			if(!tobj.isActiveByEnergyConsumption())
				return true;
		}
		
		if(obj.theobject.editoraction.contains("company_school_workingplace_teachersdesk") 
				|| obj.theobject.editoraction.contains("company_school_workingplace_studentsdesk"))
		{
			valueType=ValueType.WORK_COMPLEX;
			
			if(obj.worker!=null && obj.worker.uniqueId==baseWorldObject.uniqueId)
			{
				obj.isOccupiedBy=baseWorldObject;
			}
			
			if(obj.worker2!=null && obj.worker2.uniqueId==baseWorldObject.uniqueId)
			{
				obj.isOccupiedByExtern=baseWorldObject;
			}
			
			targetActionObject=obj;
			bActionMode=true;
			
			return true;
		}
		
		if(obj.theobject.editoraction.contains("company_college_workingplace_profslectern") 
				|| obj.theobject.editoraction.contains("company_college_workingplace_studentsdesk"))
		{
			valueType=ValueType.WORK_COMPLEX;
			
			if(obj.theobject.editoraction.contains("company_college_workingplace_profslectern") && !obj.isActiveByEnergyConsumption())
				return true;
			
			if(obj.worker!=null && obj.worker.uniqueId==baseWorldObject.uniqueId)
			{
				obj.isOccupiedBy=baseWorldObject;
			}
			
			if(obj.worker2!=null && obj.worker2.uniqueId==baseWorldObject.uniqueId)
			{
				obj.isOccupiedByExtern=baseWorldObject;
			}
			
			targetActionObject=obj;
			bActionMode=true;
			
			return true;
		}
		
		return false;
	}
	
	private void doActionMode(float stateTime)
	{
		if(bActionMode && !bGotoActionMode)
		{
			try
			{
				if(targetActionObject!=null && targetActionObject.bDeleted==true)
				{
					targetActionObject=null;
					bActionMode=false;
					bGotoActionMode=false;
				}
				
				handleAttributes();
				
				deltaTimer+=CHelper.getDeltaSeconds(town); //Complex Action Delta Speed
				deltaTimer2+=CHelper.getDeltaSeconds(town); //Complex Action Delta Speed
				deltaTimer3+=CHelper.getDeltaSeconds(town); //Complex Action Delta Speed
				if(hourTimer>3600)
					hourTimer=0;
				hourTimer+=CHelper.getDeltaSeconds(town); //macht immer eine stunde voll
				//hier nicht manipulieren, damit wird normal speed verfälscht, und x2 und x4 laufen richtig
				
				handleWorkValues();
				doAction_SupermarketBuyIn();
				doAction_Fridge();
				doAction_CookDinner();
				doAction_EatDinner();
				doAction_WashDishes();
				doAction_ReadBook();
				doAction_WatchTV();
				doAction_WorkComplex();
				doAction_Playground();
				doAction_TakeOutGarbage();
				doAction_Toilet();
				doAction_PUB_Action();
				doAction_Breakroom_Action();
				doAction_Fitness_Studio();
				doAction_VehicleRefuel();
				doAction_FuneralAttend();
				doAction_DoctorsOffice_GotoDoctor();				
				doAction_Laundry();				
				doAction_ChangeClothes();
				doAction_Shower();
				doAction_Church();
				
				
				//----------------------------------------------------------------------------
				
				
				float avdelta=2;
				if(actionMode==ActionMode.BED || actionMode==ActionMode.SHOWER)
				{
					if(getActionValue()>actionValueTrigger)
						avdelta=5;
				}
				
				setActionValue(CHelper.getDeltaSeconds(town)*avdelta, MathType.SUBTRACT);
				
				if(getActionValue()<=0)
				{
					//Supermarket Checkout / Gas Station: Wenn Kunde kommt -> Arbeitsplatz nicht verlassen
					if(targetActionObject!=null && (targetActionObject.theobject.editoraction.contains("supermarket_checkout") 
							|| targetActionObject.theobject.editoraction.contains("company_gasstation_workingplace_cashpoint")))
					{
						if(actionMode==ActionMode.WORKPLACE && targetActionObject!=null && targetActionObject.isOccupiedByExtern!=null)
						{
							setActionValue(1, MathType.ADD);
							return;
						}
					}
					
					handleEndAction();
					
					handleActionValueBonus(0);
					
					bActionMode=false;
					
					resetActionObjects();
					
					//targetActionObject.isOccupiedBy=null;
					//targetActionObject.doObjectAction=false;
					//targetActionObject=null;
				}
			}
			catch(Exception e)
			{
				//Wenn zB Objekte gelöscht werden, die in einer Action verwendet werden
				resetActionObjects();
				
				bActionMode=false;
				iActionBlocker=iActionBlocker_default;
				
				//ex.printStackTrace();
				CHelper.writeError(e.getMessage(), e.getStackTrace(), e);
			}
	}
	
	//		if(bActionMode || bGotoActionMode)
	//			bActionMode=true;
	//		else 
	//			bActionMode=false;
	}
	
	private void handleEndAction()
	{
		if(actionMode==ActionMode.WORKPLACE)
		{
			if(valueType==valueType.WORK_COMPLEX)
			{
				if(targetActionObject!=null && 
						(targetActionObject.theobject.editoraction.contains("company_school_workingplace_") || 
								targetActionObject.theobject.editoraction.contains("company_college_workingplace_")))
				{
					if(targetActionObject.worker!=null && targetActionObject.worker.uniqueId == baseWorldObject.uniqueId)
					{
						targetActionObject.actionvar1=0;
					}
					
					if(targetActionObject.worker2!=null && targetActionObject.worker2.uniqueId == baseWorldObject.uniqueId)
					{
						targetActionObject.actionvar2=0;
					}
					
					targetActionObject.tempcount=0;
				}
			}
		}
	}

	private void increaseSkillLevel(CWorldObject skillworkplace, float value)
	{
		CJobSkillClass jsc1 = null;
		
		if(baseWorldObject.thehuman.jobSkillLevel.containsKey(skillworkplace.theobject.getSkillObjectId()))
		{
			jsc1 = baseWorldObject.thehuman.jobSkillLevel.get(skillworkplace.theobject.getSkillObjectId());
		}
		else
		{
			jsc1 = new CJobSkillClass();
			jsc1.fskill=0;
			jsc1.theobject=skillworkplace.theobject;
		}
		
		jsc1.fskill+=value;
		
		if(jsc1.fskill>100)
			jsc1.fskill=100;
		
		baseWorldObject.thehuman.jobSkillLevel.put(skillworkplace.theobject.getSkillObjectId(), jsc1);
	}
	
	private void increaseTaskSkillLevel(CWorldObject taskobject)
	{
		CJobSkillClass jsc1 = null;
		
		if(baseWorldObject.thehuman.jobSkillLevel.containsKey(taskobject.theobject.getSkillObjectId()))
		{
			jsc1 = baseWorldObject.thehuman.jobSkillLevel.get(taskobject.theobject.getSkillObjectId());
		}
		else
		{
			jsc1 = new CJobSkillClass();
			jsc1.fskill=0;
			jsc1.theobject=taskobject.theobject;
		}
		
		//skill += parentWorldObject.thehuman.getIntelligenceValue()*0.002f;
		//intelligence: ca 100, workoutput ca 50 0.002 -> 0.004
		//float mvalue=0.07f;
		float mvalue=0.3f;// 0.2f;
		if(targetActionObject.theobject.editoraction.contains("company_fitnessstudio_fitnessworkingplace"))
			mvalue=0.01f;
			//mvalue=0.001f;
		
		jsc1.fskill += ((float)(baseWorldObject.thehuman.getWorkOutputPerHour(false, null, taskobject, false))*mvalue)*town.gameWorld.town.increaseskillTaskDelta; //-> 40*0.06=2.8 -> 1 - 3 jahre bis 100 Expert
		if(jsc1.fskill>100)
			jsc1.fskill=100;
		
		//wird auch in CAction.handleWorkValues() angepasst für nicht-task-jobs
		baseWorldObject.thehuman.jobSkillLevel.put(taskobject.theobject.getSkillObjectId(), jsc1);
	}
	
	private void handleActionValueBonus(int mode)
	{
		if(mode==0) //default action
		{
		}
		
		if(mode==1) //complex actions
		{
			if(actionMode==ActionMode.SHOWER)
			{
				//Resident Energy
				int comfort=0;
				if(targetActionObject!=null && targetActionObject.theobject.ATTR_COMFORT>0)
					comfort=targetActionObject.theobject.ATTR_COMFORT;
				if(targetActionObject2!=null && targetActionObject2.theobject.ATTR_COMFORT>0)
					comfort=targetActionObject2.theobject.ATTR_COMFORT;
				
				if(comfort!=0)
				{
					baseWorldObject.thehuman.changeEnergyValue(comfort);
					baseWorldObject.addAnimationEvent(AnimationEventType.COMFORT, comfort);
				}
			}
			
			if(actionMode==ActionMode.CHURCH && targetActionObject2.worker!=null)
			{
				float skill = targetActionObject2.worker.thehuman.getSkill(targetActionObject2.theobject.getSkillObjectId());
				float changehap=0;
				changehap=10+skill/10;
				
				//Gdx.app.debug("", "skill: "+skill + ", changehap: " + changehap);
								
				if(changehap>0)
				{
					changehap = baseWorldObject.thehuman.changeHappinessValue(changehap);
					baseWorldObject.addAnimationEvent(AnimationEventType.HAPPINESS, changehap);
				}
			}
			
			//Happiness
			if(actionMode==ActionMode.SHOWER || actionMode==ActionMode.TOILET || actionMode==ActionMode.CHANGE_CLOTHES)
			{
				float changehap=0;
				
				if(targetActionObject!=null && targetActionObject.theobject.ATTR_HAPPINESS>0)
					changehap=targetActionObject.theobject.ATTR_HAPPINESS;
				if(targetActionObject2!=null && targetActionObject2.theobject.ATTR_HAPPINESS>0)
					changehap=targetActionObject2.theobject.ATTR_HAPPINESS;
				
				if(changehap>0)
				{
					changehap = baseWorldObject.thehuman.changeHappinessValue(changehap);
					baseWorldObject.addAnimationEvent(AnimationEventType.HAPPINESS, changehap);
				}
			}
			
			//Doctor versorgt Resident
			if(targetActionObject.theobject.editoraction.contains("company_doctorsoffice_treatment_chair"))
			{
				if(actionState==5)
				{
					//Increase Skill Level
					increaseTaskSkillLevel(targetActionObject);
					
					//Treat Resident
					float wo = baseWorldObject.thehuman.getWorkOutputPerHour(true, targetActionObject, null, false);
					CWorldObject resident = targetActionObject.isOccupiedByExtern;
					resident.thehuman.sick-=wo/2;
					if(resident.thehuman.sick<0) {
						resident.thehuman.sick=0;
					}
					if(resident.thehuman.sick>0) {
						resident.thehuman.doctorHealingValue=wo/80;
					}
					resident.thehuman.changeHealthValue(wo/2);
					resident.thehuman.changeHappinessValue(wo/2);
					resident.addAnimationEvent(AnimationEventType.HEALTH, wo/2);
					resident.addAnimationEvent(AnimationEventType.HAPPINESS, wo/2);
				}
			}
			
			//Visit Doctor
			if(actionMode==ActionMode.GOTO_DOCTOR)
			{
				//Lese Magazin
				if(actionState==7)
				{
					float changehap = baseWorldObject.thehuman.changeHappinessValue(1);
					baseWorldObject.addAnimationEvent(AnimationEventType.HAPPINESS, changehap);
					baseWorldObject.thehuman.changeEnergyValue(5);
				}
			}
			
			//Beerdigung
			if(actionMode==ActionMode.FUNERAL_ATTEND)
			{
				//float changevalue = (parentWorldObject.thehuman.getHappynessValue()/1.5f);
				//parentWorldObject.thehuman.changeHappynessValue(changevalue);
				
				float hap=10;
				
				if(targetActionObject3.worker!=null)
				{
					hap = targetActionObject3.worker.thehuman.getWorkOutputPerHour(true, targetActionObject3, null, false);
				}
				
				//Gdx.app.debug("", "show attend: " + baseWorldObject.uniqueId);
				
				float changehap = baseWorldObject.thehuman.changeHappinessValue(hap/1.8f);
				
				baseWorldObject.addAnimationEvent(AnimationEventType.HAPPINESS, changehap);
			}
			
			//Kaffee trinken
			if(targetActionObject.theobject.editoraction.contains("_coffeemachine"))
			{
				int ihappiness=5;
				if(targetActionObject!=null && targetActionObject.theobject.ATTR_HAPPINESS>0)
					ihappiness=targetActionObject.theobject.ATTR_HAPPINESS;
				
				if(baseWorldObject.thehuman.bIsCold)
					ihappiness/=1.2f;
				if(baseWorldObject.thehuman.bIsDark)
					ihappiness/=1.2f;
								
				baseWorldObject.thehuman.coffeinLevel+=0.25f;
				
				baseWorldObject.thehuman.changeEnergyValue(15);
				
				float changehap = baseWorldObject.thehuman.changeHappinessValue(ihappiness);
				baseWorldObject.addAnimationEvent(AnimationEventType.HAPPINESS, changehap);
			}
			
			//Fruitplate
			if(targetActionObject.theobject.editoraction.contains("_fruitplate"))
			{
				int ihappiness=4;
				if(targetActionObject!=null && targetActionObject.theobject.ATTR_HAPPINESS>0)
					ihappiness=targetActionObject.theobject.ATTR_HAPPINESS;
				
				if(baseWorldObject.thehuman.bIsCold)
					ihappiness/=1.2f;
				if(baseWorldObject.thehuman.bIsDark)
					ihappiness/=1.2f;

				baseWorldObject.thehuman.fruitLevel+=1;
				
				baseWorldObject.thehuman.changeEnergyValue(5);
				
				baseWorldObject.thehuman.eatValue/=1.4f;
				
				float changehap = baseWorldObject.thehuman.changeHappinessValue(ihappiness);
				baseWorldObject.addAnimationEvent(AnimationEventType.HAPPINESS, changehap);
			}
			
			//Fitness Studio
			if(targetActionObject.theobject.editoraction.contains("company_fitnessstudio_"))
			{
				float mult = baseWorldObject.actionvar_fs1;
				if(mult==0)
					mult=0.5f;
				if(baseWorldObject.actionfield1>0) //Trainer Booster
					mult+=baseWorldObject.actionfield1/20;
				
				int ihappiness=Math.round(1f*mult);
				float iFitness=0.5f*mult;
				float iHealth=1f*mult;
				
				if(baseWorldObject.thehuman.bIsCold)
				{
					ihappiness/=1.2f;
					iFitness/=1.2f;
					iHealth/=1.2f;
				}
				
				baseWorldObject.thehuman.changeEnergyValue(10);
				
				float changehap = baseWorldObject.thehuman.changeHappinessValue(ihappiness);
				baseWorldObject.addAnimationEvent(AnimationEventType.HAPPINESS, changehap);
				
				float changefit = baseWorldObject.thehuman.changeFitnessValue(iFitness);
				baseWorldObject.addAnimationEvent(AnimationEventType.FITNESS, changefit);
				
				float changehe = baseWorldObject.thehuman.changeHealthValue(iHealth);
				baseWorldObject.addAnimationEvent(AnimationEventType.HEALTH, changehe);
			}
			
			
			//Pub / Break Room
			if(targetActionObject!=null && (targetActionObject.theobject.editoraction.contains("company_pub_pinball") 
					|| targetActionObject.theobject.editoraction.contains("company_pub_arcademachine")
					|| targetActionObject.theobject.editoraction.contains("_foosball")
					|| targetActionObject.theobject.editoraction.contains("_billard")
					))
			{
				int ihappiness=7;
				
				if(targetActionObject!=null && targetActionObject.theobject.ATTR_HAPPINESS>0)
					ihappiness=targetActionObject.theobject.ATTR_HAPPINESS;
				
//				if(targetActionObject.theobject.editoraction.contains("company_pub_pinball") 
//						|| targetActionObject.theobject.editoraction.contains("company_pub_arcademachine"))
//					ihappiness=4;
//				if(targetActionObject.theobject.editoraction.contains("_foosball"))
//					ihappiness=5;
//				if(targetActionObject.theobject.editoraction.contains("_billard"))
//					ihappiness=6;
				
				if(baseWorldObject.thehuman.bIsCold)
					ihappiness/=1.2f;
				
				baseWorldObject.thehuman.changeEnergyValue(5);
				
				//ihappiness-=4;
				//if(parentWorldObject.thehuman.bIsDark)
				//	ihappiness/=2;
				//ihappiness-=4;
				
				float changehap = baseWorldObject.thehuman.changeHappinessValue(ihappiness);
				baseWorldObject.addAnimationEvent(AnimationEventType.HAPPINESS, changehap);
			}
			
			
			//Pub Bar
			if(targetActionObject!=null && (targetActionObject.theobject.editoraction.contains("company_pub_workingplace_bar") 
					|| targetActionObject.theobject.editoraction.contains("company_pub_table")))
			{
				int ihappiness=10;
				if(targetActionObject!=null && targetActionObject.theobject.ATTR_HAPPINESS>0)
					ihappiness=targetActionObject.theobject.ATTR_HAPPINESS;
								
				if(baseWorldObject.thehuman.bIsCold)
					ihappiness/=1.2f;
				
				baseWorldObject.thehuman.alcoholLevel+=0.25f;
				
				float changehap = baseWorldObject.thehuman.changeHappinessValue(ihappiness);
				baseWorldObject.addAnimationEvent(AnimationEventType.HAPPINESS, changehap);
				
				baseWorldObject.thehuman.eatValue-=baseWorldObject.thehuman.eatValue/4;
				if(baseWorldObject.thehuman.eatValue<0)
					baseWorldObject.thehuman.eatValue=0;
				
				baseWorldObject.thehuman.changeEnergyValue(5);
				
				float changehe = baseWorldObject.thehuman.changeHealthValue(-7);
				baseWorldObject.addAnimationEvent(AnimationEventType.HEALTH, changehe);
			}
			
			
			//Playground
			if(targetActionObject!=null && targetActionObject.theobject.editoraction.contains("company_playground_"))
			{
				baseWorldObject.thehuman.changeEnergyValue(3);
				if(targetActionObject.theobject.editoraction.contains("company_playground_slide"))
				{
					float changehap = baseWorldObject.thehuman.changeHappinessValue(1);
					baseWorldObject.addAnimationEvent(AnimationEventType.HAPPINESS, changehap);
					
					float changefit = baseWorldObject.thehuman.changeFitnessValue(0.2f);
					baseWorldObject.addAnimationEvent(AnimationEventType.FITNESS, changefit);
				}
				else if(targetActionObject.theobject.editoraction.contains("company_playground_sandpit"))
				{
					float changehap = baseWorldObject.thehuman.changeHappinessValue(5);
					baseWorldObject.addAnimationEvent(AnimationEventType.HAPPINESS, changehap);
				}
				else
				{
					float changehap = baseWorldObject.thehuman.changeHappinessValue(3);
					baseWorldObject.addAnimationEvent(AnimationEventType.HAPPINESS, changehap);
					
					float changefit = baseWorldObject.thehuman.changeFitnessValue(0.5f);
					baseWorldObject.addAnimationEvent(AnimationEventType.FITNESS, changefit);
				}
			}
			
			
			//School Education and Government Funding												   
			if(targetActionObject!=null && targetActionObject.theobject.editoraction.contains("company_school_workingplace_studentsdesk"))
			{
				//	- durchschnitt 10 jahre(tage) 7-17 mit durschschnitt workoutput
				//	- berechnen wie lange dauert schulzeit für 1.5f education?
				//		pro schulstunde 
				//		gesamt: 10 * 6 stunden, 50 stunden 
				//
				//		-> 0.03f/stunde	
				//		-> (workoutput / 1000)-(distance/40000)
				
				if(targetActionObject.teachersDesk==null)
					return;
				
				float workoutput1 = baseWorldObject.thehuman.getWorkOutputPerHour(true, null, null, false); //schüler
				float workoutput2 = targetActionObject.teachersDesk.worker.thehuman.getWorkOutputPerHour(true, null, targetActionObject.teachersDesk, false); //teacher
				float workoutput = (workoutput1+workoutput2)/2;
				
				//float workoutput = baseWorldObject.thehuman.getWorkOutputPerHour(true, null, targetActionObject.teachersDesk, false);
				//Gdx.app.debug("", ""+workoutput + ", " + targetActionObject.teachersDesk);
				
				
				float distance = actionTemp_Float1;
				float edu = (workoutput/1000) - (distance/40000); //+ (targetActionObject.teachersDesk.worker.thehuman.getWorkOutputPerHour(true, targetActionObject.teachersDesk, false)/10000);

				if(baseWorldObject.thehuman.getAge()>=18)
					edu*=2.5f;
				edu*=1.5f;				
				
				if(edu>0)
				{
					float changeed = baseWorldObject.thehuman.changeEducationValue(edu);
					baseWorldObject.addAnimationEvent(AnimationEventType.EDUCATION, changeed);
					//float funding = 50f/edu;
					//float funding = edu*20000;
					float funding = edu*10000;
					
					funding*=baseWorldObject.town.grantsDelta;
					baseWorldObject.town.gameWorld.changeTownMoney(Math.round(funding));
					town.gameWorld.townStatistics.getCurrentStatistics_Finance().education+=funding;
					baseWorldObject.addAnimationEvent(AnimationEventType.TEXT, "+$" + Math.round(funding) + " Education Funding");
				}
			}
			
			//College Education 										   
			if(targetActionObject!=null && targetActionObject.theobject.editoraction.contains("company_college_workingplace_studentsdesk"))
			{
				//college education 1.5: 5 jahre (Tage)
				if(targetActionObject.teachersDesk==null)
					return;
				
				float workoutput1 = baseWorldObject.thehuman.getWorkOutputPerHour(true, null, null, false); //schüler
				float workoutput2 = targetActionObject.teachersDesk.worker.thehuman.getWorkOutputPerHour(true, null, targetActionObject.teachersDesk, false); //teacher
				float workoutput = (workoutput1+workoutput2)/2;
				
				float distance = actionTemp_Float1;
				float edu = (workoutput/600) - (distance/40000); // + (targetActionObject.teachersDesk.worker.thehuman.getWorkOutputPerHour(true, targetActionObject.teachersDesk, false)/10000); 
				edu*=2;
				edu*=1.5f;
				
				if(edu>0)
				{
					float changeed = baseWorldObject.thehuman.changeEducationValue(edu);
					baseWorldObject.addAnimationEvent(AnimationEventType.EDUCATION, changeed);
				}
			}
			
			if(actionMode==ActionMode.FRIDGE)
			{
				baseWorldObject.thehuman.eatValue/=10;
				baseWorldObject.thehuman.changeEnergyValue(5);
				//int ihappiness=1.5;
				
				//if(baseWorldObject.thehuman.bIsCold)
				//	ihappiness/=1.8f;
				//if(baseWorldObject.thehuman.bIsDark)
				//	ihappiness/=1.5f;
				
				//float changehap = baseWorldObject.thehuman.changeHappinessValue(ihappiness);
				//baseWorldObject.addAnimationEvent(AnimationEventType.HAPPINESS, changehap);				
			}
			
			//Eat Dinner
			if(targetActionObject!=null && targetActionObject.theobject.editoraction.contains("diningroom_diningtable"))
			{
				baseWorldObject.thehuman.eatValue/=10;
				
				float cook_workoutput=0;
				if(targetActionObject.worker!=null)
					cook_workoutput = targetActionObject.worker.thehuman.getWorkOutputPerHour(true, targetActionObject, null, false);
				
				int ihappiness=12+Math.round(cook_workoutput/2.5f);
				
				if(baseWorldObject.thehuman.bIsCold)
					ihappiness/=1.2f;
				if(baseWorldObject.thehuman.bIsDark)
					ihappiness/=1.2f;
				
				baseWorldObject.thehuman.changeEnergyValue(5);
				
				float changehap = baseWorldObject.thehuman.changeHappinessValue(ihappiness);
				baseWorldObject.addAnimationEvent(AnimationEventType.HAPPINESS, changehap);
			}
			
			//Read Book
			if(targetActionObject!=null && targetActionObject.theobject.editoraction.contains("livingroom_bookshelf"))
			{
				int happiness=0;
				if(targetActionObject2!=null && targetActionObject2.theobject.ATTR_HAPPINESS>0)
					happiness=targetActionObject2.theobject.ATTR_HAPPINESS;

				happiness+=targetActionObject2.checkOverlap(CWorldObject.OverlapType.DECOR);
				
				if(baseWorldObject.thehuman.bIsCold)
					happiness/=1.2;
				if(baseWorldObject.thehuman.bIsDark)
					happiness/=1.2;
				
				float changehap=0;
				if(happiness>0)
					changehap = baseWorldObject.thehuman.changeHappinessValue(happiness);
				
				float changefit=0;
				if(baseWorldObject.thehuman.getEnergyPercent()<100)
					changefit = baseWorldObject.thehuman.changeFitnessValue(-1);
				
				float changeint = baseWorldObject.thehuman.changeIntelligenceValue(1f);
				
				baseWorldObject.thehuman.changeEnergyValue(5);
				
				if(changehap>0)
					baseWorldObject.addAnimationEvent(AnimationEventType.HAPPINESS, changehap);
				
				if(changefit!=0)
					baseWorldObject.addAnimationEvent(AnimationEventType.FITNESS, changefit);
				baseWorldObject.addAnimationEvent(AnimationEventType.INTELLIGENCE, changeint);
			}
			
			//Watch TV
			if(targetActionObject!=null && targetActionObject.theobject.editoraction.contains("livingroom_tv"))
			{
				int happiness=0;
				
				if(targetActionObject!=null && targetActionObject.theobject.ATTR_HAPPINESS>0)
					happiness=targetActionObject.theobject.ATTR_HAPPINESS;
				
				happiness+=targetActionObject2.checkOverlap(CWorldObject.OverlapType.DECOR);
				
				if(baseWorldObject.thehuman.bIsCold)
					happiness/=1.2;
				//if(baseWorldObject.thehuman.bIsDark)
				//	happiness-=3;
				
				float changehap=0;
				if(happiness>0)
					changehap = baseWorldObject.thehuman.changeHappinessValue(happiness);
								
				float changefit=0;
				if(baseWorldObject.thehuman.getEnergyPercent()<100)
					changefit = baseWorldObject.thehuman.changeFitnessValue(-1);
				
				float changeint = baseWorldObject.thehuman.changeIntelligenceValue(-1f);
				
				baseWorldObject.thehuman.changeEnergyValue(10);
				
				if(happiness>0)
					baseWorldObject.addAnimationEvent(AnimationEventType.HAPPINESS, changehap);
				if(changefit!=0)
					baseWorldObject.addAnimationEvent(AnimationEventType.FITNESS, changefit);
				
				baseWorldObject.addAnimationEvent(AnimationEventType.INTELLIGENCE, changeint);
			}
		}
	}
	
	private void gotoActionMode(float stateTime)
	{
		if(bGotoActionMode)
		{
			if(targetActionObject.bDeleted)
			{
				//Gdx.app.debug("", "test " + baseWorldObject.thehuman.getName() + ", " +baseWorldObject.goByCar_X);
				
				//gehe trotzdem zu gelöschtem zielobjekt, da sonst auto alleine weiterfährt oder stehen bleibt
				//alternative: fahre nach hause
				
				//resetActionObjects();
				//bGotoActionMode=false;
				//baseWorldObject.resetPathFinding();
				//return;
			}
			
			//Das folgende führt zu extremer rekalkulation von pfaden und kostet extrem performance
//			if(targetActionObject.bIsOccupied)
//			{
//				targetActionObject=null;
//				bGotoActionMode=false;
//				sourceWorldObject.resetPathFinding();
//			}
			
			//Optional<CWorldObject> bed1 = gameWorld.worldObjects.stream().filter(item->item.theobject.editoraction.equals("bed")).findFirst();
			if(targetActionObject!=null)
			{
				
				//Erstmal auskommentiert - nicht löschen
				//für checkout -> collide wurde nicht ausgeführt
				
				//if(parentWorldObject.ziel_x==-1)
				//{
				//	resetTargetActionObjects();
				//	bGotoActionMode=false;
				//	parentWorldObject.resetPathFinding();
				//	return;
				//}
				
				//if(baseWorldObject.collides(targetActionObject) || baseWorldObject.ziel_x==-1)
				if(baseWorldObject.goByCar_X<0 && (baseWorldObject.collides(targetActionObject) || baseWorldObject.ziel_x==-1))
				{
					baseWorldObject.resetPathFinding();
					
					//Double Bed
					if(targetActionObject.theobject.editoraction.contains("bedroom_bed_double"))
					{
						targetActionObject.theobject.ObjectAction_Move_Pixels_X=0;
						
						if(targetActionObject.owner!=null && targetActionObject.owner2!=null)
						{
							if(baseWorldObject.uniqueId==targetActionObject.owner.uniqueId)
								targetActionObject.theobject.ObjectAction_Move_Pixels_X=(int) town.getSizeValue(-40);
							if(baseWorldObject.uniqueId==targetActionObject.owner2.uniqueId)
								targetActionObject.theobject.ObjectAction_Move_Pixels_X=(int) town.getSizeValue(40);
						}
					}
					
//					if(targetActionObject.theobject.editoraction.contains("company_waterworks_groundwaterextractionsystem"))
//					{
//						targetActionObject.theobject.ObjectAction_Move_Pixels_X=1;//targetActionObject.theobject.width/2-baseWorldObject.width_human()/2;
//						targetActionObject.theobject.ObjectAction_Move_Pixels_Y=1;//targetActionObject.theobject.height/2-baseWorldObject.height_human()/2;
//					}
					
					if(targetActionObject.theobject.ObjectAction_Move_Pixels_X!=0 || targetActionObject.theobject.ObjectAction_Move_Pixels_Y!=0)
					{
						float zx = targetActionObject.pos_x()+targetActionObject.width/2f;
						float zy = targetActionObject.pos_y()+targetActionObject.height/2f;
						
						Vector2 v2 = CHelper.moveVectorByRotationS2D((int)zx, (int)zy, targetActionObject.theobject.ObjectAction_Move_Pixels_X, targetActionObject.theobject.ObjectAction_Move_Pixels_Y, 0, 0, targetActionObject.rotation());
						v2.x-=baseWorldObject.width_human()/2;
						v2.y-=baseWorldObject.height_human()/2;
						baseWorldObject.setPosition((int)(v2.x), (int)(v2.y));
					}
					
					float rot1 = targetActionObject.rotation();
					if(targetActionObject.theobject.ObjectAction_Rotation!=0)
					{
						rot1+=targetActionObject.theobject.ObjectAction_Rotation;
					}
					baseWorldObject.setRotation(rot1);
					
					bActionMode=true;
					
					if(actionMode!=ActionMode.BED && actionMode!=ActionMode.SHOWER)
					{
						float frand = CHelper.getRandomFloat(0.8f, 1.2f, rand); //Dynamischen Faktor einbauen
						setActionValue(actionValueDuration*frand, MathType.SET);
					}
					
					bGotoActionMode=false;
					
					baseWorldObject.resetPathFinding();
				}
			}
		}		
	}
	
	public CWorldObject getActiveWorkplaceOrCompanyTaskobject()
	{
		CWorldObject workObject=null;
		
		if(actionMode==ActionMode.WORKPLACE && bActionMode && !bGotoActionMode && !baseWorldObject.town.gameWorld.worldPause)
		{
			if(targetActionObject5 != null &&  targetActionObject5.theobject != null && targetActionObject5.theobject.getRequiredWorkplaceEducation()>0)
				workObject=targetActionObject5;
			
			if(targetActionObject4 != null && targetActionObject4.theobject != null && targetActionObject4.theobject.getRequiredWorkplaceEducation()>0)
				workObject=targetActionObject4;

			if(targetActionObject3 != null && targetActionObject3.theobject != null && targetActionObject3.theobject.getRequiredWorkplaceEducation()>0)
				workObject=targetActionObject3;
			
			if(targetActionObject2 != null && targetActionObject2.theobject != null && targetActionObject2.theobject.getRequiredWorkplaceEducation()>0)
				workObject=targetActionObject2;
			
			if(targetActionObject != null && targetActionObject.theobject != null && targetActionObject.theobject.getRequiredWorkplaceEducation()>0)
				workObject=targetActionObject;
		}
		
		return workObject;
	}
	
	private void handleAttributes()
	{
		//Zeitinterval
		attributeTimer+=CHelper.getDeltaSeconds(town);
		if(attributeTimer>3500)
			attributeTimer=0;
		else
			return;
		
		//Resident Energy
		int comfort=0;
		if(targetActionObject!=null && targetActionObject.theobject!=null && targetActionObject.theobject.ATTR_COMFORT>0)
			comfort=targetActionObject.theobject.ATTR_COMFORT;
		if(targetActionObject2!=null && targetActionObject2.theobject!=null && targetActionObject2.theobject.ATTR_COMFORT>0)
			comfort=targetActionObject2.theobject.ATTR_COMFORT;
		
		if(actionMode==ActionMode.WORKPLACE)
			comfort-=3;
		else if(actionMode==ActionMode.COOK_DINNER)
			comfort-=2;
		else if(actionMode==ActionMode.WASH_DISHES)
			comfort-=2;
		else if(actionMode==ActionMode.READ_BOOK)
			comfort-=2;
		else if(actionMode==ActionMode.FITNESS_STUDIO)
			comfort-=6;
		else
			comfort-=1;
		
		if(comfort!=0 && actionMode!=ActionMode.SHOWER) //Shower -> handleActionValueBonus
		{
			baseWorldObject.thehuman.changeEnergyValue(comfort);
			baseWorldObject.addAnimationEvent(AnimationEventType.COMFORT, comfort);
		}
		
		
		//Happiness geht runter, wenn Education zu weit abweicht
		CWorldObject workObject = getActiveWorkplaceOrCompanyTaskobject();
		if(workObject!=null)
		{
			float fWorkplaceEducation = workObject.theobject.getRequiredWorkplaceEducation();
			
			if(baseWorldObject.thehuman.getEducationValue()>fWorkplaceEducation)
			{
				float diff = baseWorldObject.thehuman.getEducationValue()-fWorkplaceEducation;
				
				//happiness: 100
				//edu: 0-3
				
				if(diff>1f)
				{
					float diffhapp=(diff*5f)*-1f;
					baseWorldObject.thehuman.changeHappinessValue(diffhapp);
					baseWorldObject.addAnimationEvent(AnimationEventType.HAPPINESS, diffhapp);
				}
			}
		}
	}
	
	private void handleWorkValues()
	{
		Boolean bHandleWorkValues=false;
		
		if(targetActionObject==null)
			return;
		
		//Wenn parentWorldObject ein Worker des targetActionObjects ist
		if(targetActionObject.worker!=null && targetActionObject.worker.uniqueId==baseWorldObject.uniqueId)
			bHandleWorkValues=true;
		if(targetActionObject.worker2!=null && targetActionObject.worker2.uniqueId==baseWorldObject.uniqueId)
			bHandleWorkValues=true;
		
		if(!bHandleWorkValues)
			return;
		
		//ACHTUNG
		//
		//	Für WorkTasks wird Skill zusätzlich erhöht wenn ein Task erfolgreich abgeschlossen wurde
		//	-> increaseTaskSkillLevel
		
		//ACHTUNG MUSS 3600 SEIN, DA SONST HIER ÖFTER REINGEGANGEN WIRD!!!
		if(hourTimer > 3600 && bActionMode && targetActionObject != null && targetActionObject.theobject.workplaceHasSkill() && !targetActionObject.theobject.workplaceHasSkill_taskbased())
		{
			if(targetActionObject.theobject.editoraction.contains("company_church_workingplace_battlepriest"))
				return;
			
			int objid = targetActionObject.theobject.getSkillObjectId(); // Integer.parseInt(targetActionObject.theobject.objectId);
			
			CJobSkillClass jsc1=null;
			if(baseWorldObject.thehuman.jobSkillLevel.containsKey(objid))
			{
				jsc1 = baseWorldObject.thehuman.jobSkillLevel.get(objid);
			}
			else
			{
				jsc1=new CJobSkillClass(); //CHuman.getNewCJobSkillClass();
				jsc1.fskill=0;
				jsc1.theobject=targetActionObject.theobject;
			}
			
			if(jsc1.fskill>=100)
				return;
			
			//1 stunde: ~0.1 - 0.3 je nach intelligenz
			//0.1: 10 jahre bis expert
			//0.3: 3.5 jahre bis expert
			//max: 100
			
			//intelligence: 0-160
			//normal: 100
			
			//skill += parentWorldObject.thehuman.getIntelligenceValue()*0.002f;
			//intelligence: ca 100, workoutput ca 50 0.002 -> 0.004
			//Gdx.app.debug("", "davor: " + skill);
			//Gdx.app.debug("", "add: " + (float)(parentWorldObject.thehuman.getWorkOutputPerHour(false, null))*0.003f);
			
			//jsc1.fskill += ((float)(baseWorldObject.thehuman.getWorkOutputPerHour(false, null, false))*0.003f)*gameWorld.town.increaseskillJobDelta;
			jsc1.fskill += ((float)(baseWorldObject.thehuman.getWorkOutputPerHour(false, null, targetActionObject, false))*0.01f)*town.gameWorld.town.increaseskillJobDelta;
			
			//Gdx.app.debug("", "danach: " + skill);
			if(jsc1.fskill>100)
				jsc1.fskill=100;
			
			//wird auch in CAction.increaseTaskSkillLevel() erhöht für tasks zb dinner cook, funeral speaker
			baseWorldObject.thehuman.jobSkillLevel.put(objid, jsc1);
						
		}
	}
	
	
	
	
	//***************
	//Complex Actions
	//***************
	
	//*****************
	//Helper Functions
	//*****************
	private Boolean reserveObject(CWorldObject obj, int nr)
	{
		if(nr==1 && obj.isOccupiedBy==null)
		{
			obj.isOccupiedBy=baseWorldObject;
			return true;
		}

		if(nr==2 && obj.isOccupiedBy2==null)
		{
			obj.isOccupiedBy2=baseWorldObject;
			return true;
		}
		
		if(nr==3 && obj.isOccupiedBy3==null)
		{
			obj.isOccupiedBy3=baseWorldObject;
			return true;
		}
		
		if(nr==4 && obj.isOccupiedBy4==null)
		{
			obj.isOccupiedBy4=baseWorldObject;
			return true;
		}
		if(nr==5 && obj.isOccupiedBy5==null)
		{
			obj.isOccupiedBy5=baseWorldObject;
			return true;
		}
		if(nr==6 && obj.isOccupiedBy6==null)
		{
			obj.isOccupiedBy6=baseWorldObject;
			return true;
		}
		if(nr==7 && obj.isOccupiedBy7==null)
		{
			obj.isOccupiedBy7=baseWorldObject;
			return true;
		}
		if(nr==8 && obj.isOccupiedBy8==null)
		{
			obj.isOccupiedBy8=baseWorldObject;
			return true;
		}
		if(nr==9 && obj.isOccupiedBy9==null)
		{
			obj.isOccupiedBy9=baseWorldObject;
			return true;
		}
		
		if(nr==10 && obj.isOccupiedByExtern==null)
		{
			if(obj.isOccupiedByExtern==null)
			{
				obj.isOccupiedByExtern=baseWorldObject;
				return true;
			}
		}
		
		return false;
	}
	private Boolean reserveObject(CWorldObject obj)
	{
		if(obj.isOccupiedBy==null)
		{
			obj.isOccupiedBy=baseWorldObject;
			//obj.bIsOccupied=true;
			return true;
		}
		else
		{
			if(obj.isOccupiedBy.uniqueId==baseWorldObject.uniqueId)
				return true;
			
			if(obj.theobject.editoraction.contains("couch"))
			{
				if(obj.isOccupiedByExtern==null)
				{
					obj.isOccupiedByExtern=baseWorldObject;
					//obj.bIsOccupiedByExtern=true;
					return true;
				}
			}
		}
		
		return false;
	}
	private void releaseObject(CWorldObject occObj)
	{
		if(occObj==null || baseWorldObject==null)
			return;
		
		if(occObj.isOccupiedBy!=null && occObj.isOccupiedBy.uniqueId==baseWorldObject.uniqueId)
		{
			occObj.isOccupiedBy=null;
			occObj.iOccupied1_Arrived=0;
			//occObj.bIsOccupied=false;
		}
		
		if(occObj.isOccupiedBy2!=null && occObj.isOccupiedBy2.uniqueId==baseWorldObject.uniqueId)
		{
			occObj.isOccupiedBy2=null;
			//occObj.bIsOccupied2=false;
		}
		
		if(occObj.isOccupiedBy3!=null && occObj.isOccupiedBy3.uniqueId==baseWorldObject.uniqueId)
		{
			occObj.isOccupiedBy3=null;
			//occObj.bIsOccupied3=false;
		}

		if(occObj.isOccupiedBy4!=null && occObj.isOccupiedBy4.uniqueId==baseWorldObject.uniqueId)
		{
			occObj.isOccupiedBy4=null;
			//occObj.bIsOccupied3=false;
		}
		if(occObj.isOccupiedBy5!=null && occObj.isOccupiedBy5.uniqueId==baseWorldObject.uniqueId)
		{
			occObj.isOccupiedBy5=null;
			//occObj.bIsOccupied3=false;
		}
		if(occObj.isOccupiedBy6!=null && occObj.isOccupiedBy6.uniqueId==baseWorldObject.uniqueId)
		{
			occObj.isOccupiedBy6=null;
			//occObj.bIsOccupied3=false;
		}
		if(occObj.isOccupiedBy7!=null && occObj.isOccupiedBy7.uniqueId==baseWorldObject.uniqueId)
		{
			occObj.isOccupiedBy7=null;
			//occObj.bIsOccupied3=false;
		}
		if(occObj.isOccupiedBy8!=null && occObj.isOccupiedBy8.uniqueId==baseWorldObject.uniqueId)
		{
			occObj.isOccupiedBy8=null;
			//occObj.bIsOccupied3=false;
		}
		if(occObj.isOccupiedBy9!=null && occObj.isOccupiedBy9.uniqueId==baseWorldObject.uniqueId)
		{
			occObj.isOccupiedBy9=null;
			//occObj.bIsOccupied3=false;
		}
				
		if(occObj.isOccupiedByExtern!=null && occObj.isOccupiedByExtern.uniqueId==baseWorldObject.uniqueId)
		{
			occObj.iOccupiedExtern_Arrived=0;
			occObj.isOccupiedByExtern=null;
			//occObj.bIsOccupiedByExtern=false;
		}
	}
	private void initMoveTo(float targetX, float targetY)
	{
		actionTemp_Float1=targetX;
		actionTemp_Float2=targetY;
	}
	private Boolean moveTo()
	{
		int newx=baseWorldObject.pos_x();
		int newy=baseWorldObject.pos_y();
		
		if(actionTemp_Float1>baseWorldObject.pos_x())
			newx+=1;
		if(actionTemp_Float1<baseWorldObject.pos_x())
			newx-=1;
			
		if(actionTemp_Float2>baseWorldObject.pos_y())
			newy+=1;
		if(actionTemp_Float2<baseWorldObject.pos_y())
			newy-=1;
		
		if(actionTemp_Float1==baseWorldObject.pos_x() && actionTemp_Float2==baseWorldObject.pos_y())
			return true;
		
		baseWorldObject.setPosition(newx, newy);
		
		return false;
	}
	private Boolean isOccupiedByMe(CWorldObject obj, int nr)
	{
		Boolean bRet = false;
		
		if(nr==1)
			bRet = (obj.isOccupiedBy!=null && obj.isOccupiedBy.uniqueId==baseWorldObject.uniqueId);
		if(nr==2)
			bRet = (obj.isOccupiedBy2!=null && obj.isOccupiedBy2.uniqueId==baseWorldObject.uniqueId);
		if(nr==3)
			bRet = (obj.isOccupiedBy3!=null && obj.isOccupiedBy3.uniqueId==baseWorldObject.uniqueId);
		if(nr==4)
			bRet = (obj.isOccupiedBy4!=null && obj.isOccupiedBy4.uniqueId==baseWorldObject.uniqueId);
		if(nr==5)
			bRet = (obj.isOccupiedBy5!=null && obj.isOccupiedBy5.uniqueId==baseWorldObject.uniqueId);
		if(nr==6)
			bRet = (obj.isOccupiedBy6!=null && obj.isOccupiedBy6.uniqueId==baseWorldObject.uniqueId);
		if(nr==7)
			bRet = (obj.isOccupiedBy7!=null && obj.isOccupiedBy7.uniqueId==baseWorldObject.uniqueId);
		if(nr==8)
			bRet = (obj.isOccupiedBy8!=null && obj.isOccupiedBy8.uniqueId==baseWorldObject.uniqueId);
		if(nr==9)
			bRet = (obj.isOccupiedBy9!=null && obj.isOccupiedBy9.uniqueId==baseWorldObject.uniqueId);
		
		if(nr==10)
			bRet = (obj.isOccupiedByExtern!=null && obj.isOccupiedByExtern.uniqueId==baseWorldObject.uniqueId);
		
		return bRet;
	}
	private Boolean gotoObject(CWorldObject obj, Boolean cancelAction)
	{
		return gotoObject(obj, cancelAction, obj.pos_x()+obj.width/2, obj.pos_y()+obj.height/2);
	}
	private Boolean gotoObject(CWorldObject driveobj, CWorldObject targetobj, Boolean cancelAction, int tx, int ty)
	{
		if(tx<1 && ty<1 && targetobj!=null)
		{
			tx=targetobj.pos_x()+targetobj.width/2;
			ty=targetobj.pos_y()+targetobj.height/2;
		}
		
		driveobj.initTargetPath(tx, ty, false, targetobj);
		driveobj.pathFinding(town.gameWorld.stateTime);
		
		if(driveobj.path==null)
		{
			if(cancelAction)
			{
				cancelAction(""+driveobj.theobject.objectName + "->" + targetobj.theobject.objectName);
			}
			
			return false;
		}
		else
		{
			return true;
		}
	}
	private Boolean gotoObject_Arrived(CWorldObject driveobj, CWorldObject target, int movex, int movey, float rotation, Boolean checkCollide)
	{
		//checkCollide: ohne collide prüfung kann zielpunkt genau angesteuert werden ohne sprung
		
		Boolean bTargetReached=false;
		if(checkCollide)
		{
			if(driveobj.collides(target) || driveobj.ziel_x==-1)
				bTargetReached=true;
		}
		else
		{
			if(driveobj.ziel_x==-1)
				bTargetReached=true;
		}
		
		if(bTargetReached)
		{
			driveobj.resetPathFinding();
			
			if(target!=null)
			{
				//************
				//ACHTUNG
				//***********
				//Anstatt minus target.width.. muss hier eventuell -driveobj.width gemacht werden -> siehe garbage truck entleerung
								
				Vector2 v2 = CHelper.moveVectorByRotationS2D(target.pos_x(), target.pos_y(), movex, movey, Math.round(target.width/2), Math.round(target.height/2), target.rotation());
				//v2.x-=target.width/2;
				//v2.y-=target.height/2;
				v2.x-=driveobj.width/2;
				v2.y-=driveobj.height/2;
				
				if(rotation!=-1)
					driveobj.setRotation(rotation);
				if(movex!=-1 || movey!=-1)
					driveobj.setPosition((int)v2.x, (int)v2.y);
			}
			
			return true;
		}
		
		return false;
	}
	private Boolean gotoObject(CWorldObject obj, Boolean cancelAction, int tx, int ty)
	{
		baseWorldObject.initTargetPath(tx, ty, false, obj);
		baseWorldObject.pathFinding(town.gameWorld.stateTime);
		
		//if(actionMode==ActionMode.READ_BOOK)
		//	Gdx.app.debug("", ""+baseWorldObject.thehuman.getName() + ", path: " + baseWorldObject.path);
		
		if(baseWorldObject.path==null)
		{
			if(cancelAction)
			{
				cancelAction(""+baseWorldObject.thehuman.getName() + "->" + obj.theobject.objectName);
			}
			return false;
		}
		else
		{
			return true;
		}
	}
	private Boolean myCarOnAdr(CAddress adr)
	{
		if(baseWorldObject.thehuman.car != null)
		{
			CAddress carAdr = baseWorldObject.thehuman.car.getCurrentAddress();
			if(carAdr!=null && adr!=null)
			{
				if(carAdr.addressId == adr.addressId)
				{
					return true;
				}
			}
			
			if(carAdr!=null) {
				CCompany comp =carAdr.getCompany(); 
				if(comp!=null && comp.companyType==CompanyType.PUBLIC_PARKING) {
					int distanceToTarget = CHelper.getEuclidianDistance(baseWorldObject.pos_x(), baseWorldObject.pos_y(), baseWorldObject.thehuman.car.pos_x(), baseWorldObject.thehuman.car.pos_y());
					if(distanceToTarget<2000) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	//	private Boolean gotoObject_toMoveByRotationPoint(CWorldObject obj, Boolean cancelAction, int movByBaseObj_X, int movByBaseObj_Y)
	//	{
	//		Vector2 v2 = CHelper.moveHumanByObjectRotation(obj, parentWorldObject, movByBaseObj_X, movByBaseObj_Y);
	//		return gotoObject(obj, cancelAction, (int)v2.x, (int)v2.y);
	//	}

	private Boolean gotoXY(int x, int y)
	{
		baseWorldObject.initTargetPath(x, y, false, null);
		baseWorldObject.pathFinding(town.gameWorld.stateTime);
		
		if(baseWorldObject.path==null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	private Boolean gotoXY_Arrived(int x, int y, float distance)
	{
		int dist = CHelper.getEuclidianDistance(baseWorldObject.pos_x(), baseWorldObject.pos_y(), x, y);
		
		if(dist<=distance)
		{
			baseWorldObject.resetPathFinding();
			return true;
		}
		
		return false;
	}
	
	private Boolean gotoObject_Arrived(CWorldObject target, int distance)
	{
		if(baseWorldObject.collides(target, distance) || baseWorldObject.ziel_x==-1)
		{
			baseWorldObject.resetPathFinding();
			return true;
		}
		
		return false;
	}
	private Boolean gotoObjectXY(CWorldObject obj, int movx, int movy)
	{
		Vector2 v2 = CHelper.moveHumanByObjectRotation(obj, baseWorldObject, movx, movy);
		baseWorldObject.initTargetPath((int)v2.x, (int)v2.y, false, obj);
		baseWorldObject.pathFinding(town.gameWorld.stateTime);
		
		if(baseWorldObject.path==null)
		{
			cancelAction(""+baseWorldObject.thehuman.getName() + "->" + obj.theobject.objectName);
			return false;
		}
		else
		{
			return true;
		}
	}
	private Boolean gotoObjectXY(CWorldObject driveObj, CWorldObject targetObj, int movx, int movy)
	{
		Vector2 v2 = CHelper.moveVectorByRotationS2D(targetObj.pos_x(), targetObj.pos_y(), movx, movy, targetObj.width/2, targetObj.height/2, targetObj.rotation());
		//Vector2 v2 = CHelper.moveHumanByObjectRotation(targetObj, driveObj, movx, movy);
		
		driveObj.initTargetPath((int)v2.x, (int)v2.y, false, targetObj);
		driveObj.pathFinding(town.gameWorld.stateTime);
		
		if(driveObj.path==null)
		{
			cancelAction("");
			return false;
		}
		else
		{
			return true;
		}
	}	
	private Boolean gotoObject_Arrived(CWorldObject target, int movex, int movey, float rotation, Boolean checkCollide, int distance)
	{
		if(baseWorldObject.collides(target, distance) || baseWorldObject.ziel_x==-1)
		{
			baseWorldObject.ziel_x=-1;
			return gotoObject_Arrived(target, movex, movey, rotation, checkCollide);
		}
		
		return false;
	}
	private Boolean gotoObject_Arrived(CWorldObject target, int movex, int movey, float rotation, Boolean checkCollide)
	{
		//checkCollide: ohne collide prüfung kann zielpunkt genau angesteuert werden ohne sprung
		
		Boolean bTargetReached=false;
		if(checkCollide)
		{
			if(baseWorldObject.collides(target))
				bTargetReached=true;
			
			if(baseWorldObject.ziel_x==-1)
			{
				bTargetReached=true;
				
				if(baseWorldObject.goByCar_X>-1)
					bTargetReached=false;
			}
		}
		else
		{
			if(baseWorldObject.ziel_x==-1)
				bTargetReached=true;
			
			if(baseWorldObject.goByCar_X>-1)
				bTargetReached=false;
		}
		
		
		if(bTargetReached)
		{
			//Gdx.app.debug("" + baseWorldObject.thehuman.getName(), ""+baseWorldObject.goByCar_X + ", " + baseWorldObject.goByCar_Y);
			
			//if(target!=null && CHelper.getEuclidianDistance(baseWorldObject, target)>1000)
			//{
			//	baseWorldObject.resetGoByCar();
			//	cancelAction("");
			//	return false;
			//}
			
			baseWorldObject.resetPathFinding();
			
			if(target!=null)
			{
				Vector2 v2 = CHelper.moveHumanByObjectRotation(target, baseWorldObject, movex, movey);
				if(rotation>-1)
					baseWorldObject.setRotation(rotation);
				
				if(movex!=-1 || movey!=-1)
					baseWorldObject.setPosition((int)v2.x, (int)v2.y);
			}
			return true;
		}
		
		return false;
	}
	
	private void moveHumanByObjectRotation(CWorldObject human, CWorldObject target, int movex, int movey, float rotation)
	{
		Vector2 v2 = CHelper.moveHumanByObjectRotation(target, human, movex, movey);
		if(rotation>-1)
			human.setRotation(rotation);
		if(movex!=-1 || movey!=-1)
			human.setPosition((int)v2.x, (int)v2.y);
	}
	private void moveObjectByHumanRotation(CWorldObject human, CWorldObject target, int movex, int movey, float rotation)
	{
		Vector2 v2 = CHelper.moveVectorByHumanRotation(human, target.width, target.height, movex, movey);
		target.setPosition((int)v2.x, (int)v2.y);
		if(rotation>-1)
			target.setRotation(rotation);
	}
	private void moveObjectByObjectRotation(CWorldObject base, CWorldObject target, int movex, int movey, float rotation)
	{
		Vector2 v2 = CHelper.moveVectorByRotationS2D(base.pos_x(), base.pos_y(), movex, movey, base.width/2, base.height/2, base.rotation());
		target.setPosition((int)v2.x-target.width/2, (int)v2.y-target.height/2);
		if(rotation>-1)
			target.setRotation(rotation);
	}
	private Boolean cancelIfDark()
	{
		if(baseWorldObject.thehuman.bIsDark)
		{
			iActionBlocker=iActionBlocker_default;
			return true;
		}
		
		return false;
	}
	private void cancelAction(String debugString)
	{
		//Gdx.app.debug("cancelaction", "human: " + parentWorldObject.uniqueId + ", actionstate: " + actionState + ", " + debugString);
		bActionMode=false;
		resetActionObjects();
	}
	
	private void resetRandomActionAnim()
	{
		baseWorldObject.actionanim1=0;
		baseWorldObject.actionanim2=0;
		randomActionAnim=0;
		randomActionDeltaTime=0;
	}
	
	private void playRandomActionAnim()
	{
		randomActionDeltaTime+=CHelper.getDeltaSeconds(town);
		
		if(randomActionAnim==0)
			randomActionAnim=1;//+rand.nextInt(4);
		
		if(randomActionAnim==1)
		{
			if(randomActionDeltaTime>20)
			{
				baseWorldObject.actionanim1 = rand.nextInt(15);
				//parentWorldObject.actionanim2 = rand.nextInt(5);
				randomActionDeltaTime=0;
			}
		}
		
		if(randomActionAnim==2)
		{
			baseWorldObject.actionanim1=7;
			
			if(randomActionDeltaTime>100)
			{
				baseWorldObject.actionanim1=12;
			}
			
			if(randomActionDeltaTime>150)
			{
				baseWorldObject.actionanim1=11;
			}
			if(randomActionDeltaTime>170)
			{
				baseWorldObject.actionanim1=10;
			}
			if(randomActionDeltaTime>190)
			{
				baseWorldObject.actionanim1=11;
			}
			if(randomActionDeltaTime>210)
			{
				baseWorldObject.actionanim1=10;
			}
			if(randomActionDeltaTime>230)
			{
				baseWorldObject.actionanim1=0;
				randomActionDeltaTime=0;
				randomActionAnim=0;
			}
		}
		
	}
	//----------------------------------------------------------------------------------------
	
	
	
	//**********************************************************
	//
	//ACHTUNG Funktion moveVectorByRotation NICHT MEHR VERWENDEN
	//
	//NEU: 	moveVectorByRotationS2D
	//		moveObjectByHumanRotation
	//		moveHumanByObjectRotation
	//
	//ACHTUNG beim draw muss mid position von x und y abgezogen werden sonst passt rotationsposition nicht 
	//
	//Vector2 v2 = CHelper.moveVectorByRotationS2D(pos_x(), pos_y(), 45, 75, width/2, height/2, rotation());
	//gameWorld.worldSpriteBatch.draw(plate, v2.x-platesize/2, v2.y-platesize/2, platesize/2, platesize/2, platesize, platesize, 1, 1, rotation(), 0, 0, plate.getWidth(), plate.getHeight(), false, false);
	//
	//**********************************************************

	
	
	
	
	
	
	//********************
	//Complex Action Logic
	//********************
	
	
	
	//HINWEIS: resetPathFinding ausführen nach COLLIDES Prüfung, damit resident stehen bleibt
	
	
	
	
	//******************
	//Generic Functions 
	//******************
	private Boolean doAction_RefillObject(CWorldObject refillObject, CWorldObject sourceObject, Boolean bCarryRefillObject)
	{
		//Bisher implementiert für: 
		//
		//	- Fruitplate
		//
		
		if(sourceObject==null || refillObject==null)
			return false;
		
		int carryRefillObjectX=(int)(refillObject.width/1.4f);
		int carryRefillObjectY=(int)town.getSizeValue(30);
		
		int sourceObjectPosX=(int)town.getSizeValue(20);
		int sourceObjectPosY=-(int)town.getSizeValue(40);
		float sourceObjectPosRot=sourceObject.rotation()+90;
		
		//Objekte werden in init reserviert/occupied
		//Achtung Sourceobject muss hier freigegeben werden
		
		if(actionState==0)
		{
			if(refillObject.objectFilling==refillObject.getObjectFillingMax() || sourceObject.objectFilling==0)
			{
				releaseObject(sourceObject);
				return false;
			}
			
			if(bCarryRefillObject) //bringe refillobject zu source
			{
				//Gehe zu Refillobject
				gotoObject(refillObject, true);
				actionState=1;
			}
			else
			{
				Gdx.app.debug("", "todo: doAction_RefillObject type 2");
				
				//Hole refillmaterial von source und bringe zu refillobject
				gotoObject(sourceObject, true);
				//actionState=x;
			}
			
			return true;
		}
		
		if(actionState==1)
		{
			//Am Refillobject angekommen 
			if(gotoObject_Arrived(refillObject, 0))
			{
				//parentWorldObject.actionanim1=2;
				baseWorldObject.actionanim1=1;
				deltaTimer=0;
				actionState=2;
			}
			
			return true;
		}
		
		if(actionState==2)
		{
			if(deltaTimer>20)
			{
				refillObject.x_temp=refillObject.pos_x();
				refillObject.y_temp=refillObject.pos_y();
				
				//Gehe zu Sourceobject
				gotoObjectXY(sourceObject,sourceObjectPosX,sourceObjectPosY);
				actionState=3;
			}
			
			return true;
		}
		
		if(actionState==3)
		{
			moveObjectByHumanRotation(baseWorldObject, refillObject, carryRefillObjectX, carryRefillObjectY, 0);
			
			//Am Sourceobject angekommen
			if(gotoObject_Arrived(sourceObject, sourceObjectPosX, sourceObjectPosY, sourceObjectPosRot, true))
			{
				actionState=4;
			}
			
			return true;
		}
		
		if(actionState==4)
		{
			moveObjectByHumanRotation(baseWorldObject, refillObject, carryRefillObjectX, carryRefillObjectY, 0);
			baseWorldObject.actionanim1=1;
			
			//Sourceobject aufmachen
			sourceObject.doObjectAction=true; 
			actionState=5;
			deltaTimer=0;
			
			return true;
		}
		
		if(actionState==5)
		{
			//Refillobject befüllen
			baseWorldObject.setRotation(sourceObject.rotation()+140);
			moveObjectByHumanRotation(baseWorldObject, refillObject, carryRefillObjectX, carryRefillObjectY, 0);
			
			int deltavalue=30;
			if(baseWorldObject.thehuman.bIsDark)
				deltavalue=400;
			
			if(deltaTimer>deltavalue)
			{
				if(sourceObject.getObjectFillingMultiMax()>0)
					sourceObject.addObjectFillingMulti(0, -1);
				else
					sourceObject.objectFilling-=1;
				
				refillObject.objectFilling+=1;
				
				deltaTimer=0;
			}
			
			int sourceFilling=sourceObject.objectFilling;
			if(sourceObject.getObjectFillingMultiMax()>0)
				sourceFilling=sourceObject.getObjectFillingMulti();
				
			//Fertig mit Befüllung
			if(sourceFilling<1 || refillObject.objectFilling==refillObject.getObjectFillingMax())
			{
				sourceObject.doObjectAction=false;
				releaseObject(sourceObject);
				
				//Create Garbage Bag
				if(!refillObject.theobject.editoraction.contains("fruitplate"))
					town.gameWorld.gameResourceConfig.createWorldObject("recyclingcenter_garbagebag", baseWorldObject.pos_x(), baseWorldObject.pos_y(), sourceObject.theaddress);
				
				//RefillObject zurückbringen
				if(refillObject.x_temp>0)
				{
					//Gdx.app.debug("", "refillObject.x_temp: " + refillObject.x_temp + ", refillObject.y_temp: " + refillObject.y_temp);
					gotoXY(refillObject.x_temp, refillObject.y_temp);
					deltaTimer=0;
					actionState=6;
				}
				else
				{
					actionState=0;
					bActionMode=false;
					return false;
				}
			}
			
			return true;
		}
		
		if(actionState==6)
		{
			moveObjectByHumanRotation(baseWorldObject, refillObject, carryRefillObjectX, carryRefillObjectY, 0);
			
			//Am Ursprungsplatz des Refillobjekts angekommen
			if(baseWorldObject.ziel_x==-1)
			{
				baseWorldObject.resetPathFinding();
				refillObject.setPosition(refillObject.x_temp, refillObject.y_temp);
				
				baseWorldObject.actionanim1=0;
				actionState=0;
				bActionMode=false;
				targetActionObject9=null; //Trigger, dass Subaction beendet ist
				
				return false;
			}
		}
		
		return true;
	}
		
	//*****
	//Idle
	//*****
	private void doAction_Idle()
	{
		if(actionMode!=ActionMode.IDLE)
			return;
		
		if(baseWorldObject.ziel_x==-1)
		{
			int doit = rand.nextInt(100);
			if(doit==1)
			{
				//playRandomActionAnim();
				baseWorldObject.actionanim1 = rand.nextInt(15);
			}
			deltaTimer+=CHelper.getDeltaSeconds(town);
		}
		
		if(deltaTimer>50)
		{
			deltaTimer=0;
			resetRandomActionAnim();
			baseWorldObject.walkAround(false);
		}
	}
	
	private CWorldObject getNextConstructionObjectFromList(List<CWorldObject> list, Boolean badr)
	{
		if(list.size()==0)
			return null;
		
		if(badr)
		{
			int z1 = rand.nextInt(list.size());
			CWorldObject w1 = list.get(z1);
			Boolean bS1 = true;
			if(w1==null || w1.theobject==null || w1.iObjectIsReady>=100)
				bS1=false;
			if(w1.theobject!=null && w1.isHouseObject() && (w1.theroom==null || !w1.theroom.bObjectIsReady))
				bS1=false;
			
			if(bS1)
			{
				return w1;
			}
		}
		
		
		{
			CWorldObject theobject=null;
			for(CWorldObject tc : list)
			{
				if(tc.iObjectIsReady>=100)
					continue;
				
				if(tc.theobject==null)
					continue;
			
				if(tc.isHouseObject() && (tc.theroom==null || !tc.theroom.bObjectIsReady))
						continue;
				
				if(theobject!=null)
				{
					int dist1 = CHelper.getEuclidianDistance(baseWorldObject, tc);
					int dist2 = CHelper.getEuclidianDistance(baseWorldObject, theobject);
					if(dist1<dist2)
						theobject=tc;
				}
				else
				{
					theobject=tc;
				}
			}
			
			return theobject;
		}
		
		//return null;
	}
	
	private void setNextConstructionObject(CWorldObject wobj)
	{
		CWorldObject w1=null;
		
		if(wobj!=null && wobj.theaddress!=null)
			w1 = getNextConstructionObjectFromList(wobj.theaddress.listWorldObjects_Ground, true);
		
		if(w1==null)
		{
			if(wobj!=null && wobj.theaddress!=null)
				w1 = getNextConstructionObjectFromList(wobj.theaddress.listWorldObjects_Floors, true);
		}
		
		if(w1==null)
		{
			if(wobj!=null && wobj.theaddress!=null)
				w1=getNextConstructionObjectFromList(wobj.theaddress.listWorldObjects, true);
			if(w1==null)
			{
				w1=getNextConstructionObjectFromList(town.gameWorld.tempConstructionObjects, false);
			}
		}
		
		if(w1==null)
		{
			targetActionObject2=null;
			bActionMode=false;
		}
		else
		{
			targetActionObject2=w1;
		}
	}
	
	private void doAction_Construction()
	{
		if(actionState==0)
		{
			setNextConstructionObject(targetActionObject2);
			
			if(targetActionObject2!=null && targetActionObject2.iObjectIsReady<100)
			{
				gotoObject(targetActionObject2, true);
				actionState=1;
			}
			else
			{
				bActionMode=false;
				iActionBlocker=iActionBlocker_default;
			}
			
			return;
		}
		
		if(actionState==1)
		{
			if(baseWorldObject.ziel_x==-1 || baseWorldObject.ziel_y==-1)
			{
				gotoObject(targetActionObject2, true);
			}
			
			if(baseWorldObject.collides(targetActionObject2, 0) || baseWorldObject.ziel_x==-1)
			{
				baseWorldObject.ziel_x=-1;
				actionState=2;
				deltaTimer=0;
				deltaTimer2=0;
				return;
			}
		}
		
		if(actionState==2)
		{
			baseWorldObject.actionanim1=1;
			if(deltaTimer>5)
			{
				deltaTimer=0;
				targetActionObject2.iObjectIsReady+=5;
				if(town.gameGui.buttonX2.toggleActive)
					targetActionObject2.iObjectIsReady+=5;
				if(town.gameGui.buttonX4.toggleActive)
					targetActionObject2.iObjectIsReady+=15;
			}
			
			if(deltaTimer2>100 || targetActionObject2.iObjectIsReady<=5)
			{
				deltaTimer2=0;
				
				if(targetActionObject2.bDrawObjectByCamera)
				{
					int ind = rand.nextInt(15);
					ind++;
					town.gameAudio.playSound("EFFECT_CONSTRUCTION"+ind, -0.9f, true);
				}
			}
			
			if(targetActionObject2.iObjectIsReady>=100)
			{
				targetActionObject2.iObjectIsReady=100;
				targetActionObject2.bObjectIsReady=true;
				town.gameWorld.bRenderFrameBuffer=true;
				actionState=0;
			}
		}
	}
	
	private void doAction_BattlePriest()
	{
		//baseWorldObject.actionanim1=1;
		
		//		if(actionState==0)
		//		{
		//			Gdx.app.debug("", "start priest");
		//			
		//			//bGotoActionMode=true;
		//			actionState=1;
		//		}
		
		if(actionState==0)
		{
			if(baseWorldObject.ziel_x==-1)
			{
				int dist=CWorld.mapsize;
				CWorldObject target=null;
				
				for(CWorldObject wobj : town.gameWorld.worldZombies)
				{
					if(wobj.bIsDead)
						continue;
					
					int disttemp = CHelper.getEuclidianDistance(baseWorldObject, wobj);
					if(disttemp<dist)
					{
						target=wobj;
						dist=disttemp;
					}
				}
				
				if(target==null)
				{
					for(CWorldObject wobj : town.gameWorld.worldZombieEntrances)
					{
						int disttemp = CHelper.getEuclidianDistance(baseWorldObject, wobj);
						if(disttemp<dist)
						{
							target=wobj;
							dist=disttemp;
						}
					}
				}
				
				if(target!=null)
				{
					baseWorldObject.zombieRun=1;
					//baseWorldObject.actionanim1=1;
					
					if(dist>(int)town.getSizeValue(500))
					{
						int tx=baseWorldObject.pos_x();
						int ty=baseWorldObject.pos_y();
						if(target.pos_x()>tx)
							tx+=(int)town.getSizeValue(100);
						else
							tx-=(int)town.getSizeValue(100);
						
						if(target.pos_y()>ty)
							ty+=(int)town.getSizeValue(100);
						else
							ty-=(int)town.getSizeValue(100);
						
						gotoXY(tx, ty);
					}
					else
					{
						if(dist>100)
							gotoObject (target, false);
						else
							baseWorldObject.ziel_x=-1;
					}
					
					//Gdx.app.setLogLevel(10);
					//Gdx.app.debug("", "" + dist);
					
					if(dist<120 || (dist<220 && baseWorldObject.ziel_x==-1))
					{
						baseWorldObject.setRotation(rand.nextInt(360));
						target.spriteMoveEvents.add(new CSpriteMoveEvent(SpriteMoveEventType.PLACING, target, 0));
						target.spriteMoveEvents.add(new CSpriteMoveEvent(SpriteMoveEventType.SPLATTER, target, 1));

						float skill=baseWorldObject.thehuman.getSkill(targetActionObject.theobject.getSkillObjectId());
						float damage=skill/20;
						if(damage<=0)
							damage=0.1f;

						if(target.thehuman!=null)
							target.thehuman.changeHealthValue(-damage);
						else
						{
							target.objectCondition-=damage;
							if(target.objectCondition<0)
							{
								target.objectCondition=0;
								baseWorldObject.town.gameGui.removeWorldObject(target, false);
							}
						}
						
						//Gdx.app.debug("", "increase skill " + target.thehuman.getHealthValue());
						//if(target.thehuman.getHealthValue()<=0)
						increaseSkillLevel(targetActionObject, 0.01f);
					}
				}
				else
				{
					if(deltaTimer2>3600) //Pause wenn keine Zombies da sind
					{
						bActionMode=false;
						deltaTimer2=0;
					}					
					
					baseWorldObject.zombieRun=0;
					//baseWorldObject.actionanim1=0;
					baseWorldObject.walkAround(false);
				}
			}
		}
	}
	
	private void doAction_Zombie()
	{
		if(actionMode!=ActionMode.ZOMBIE || baseWorldObject.iZombie<1 || baseWorldObject.bIsDead)
			return;
		
		bActionMode=true;
		baseWorldObject.actionanim1=1;
		
		if(targetActionObject10!=null && targetActionObject10.iZombie>=1)
			targetActionObject10=null;
		
		if(baseWorldObject.ziel_x==-1)
		{
			int dist=CWorld.mapsize;
			CWorldObject target=targetActionObject10;
			
			//if(gameWorld.worldHumans.size()>0)
			//{
			//	int index = rand.nextInt(gameWorld.worldHumans.size());
			//	target = gameWorld.worldHumans.get(index);
			//}
			
			if(target==null)
			{
				for(CWorldObject wobj : town.gameWorld.worldHumans)
				{
					if(wobj.thehuman.isWorking("company_church_workingplace_battlepriest"))
						continue;
					
					if(wobj.actionstring1.contains("show_coffin") || wobj.actionstring1.contains("show_grave") || wobj.bIsDead)
						continue;
					
					int disttemp = CHelper.getEuclidianDistance(baseWorldObject, wobj);
					if(disttemp<dist)
					{
						target=wobj;
						targetActionObject10=target;
						dist=disttemp;
					}
				}
			}
			
			if(target!=null)
			{
				baseWorldObject.zombieRun=1;
				
				if(dist>500)
				{
					int tx=baseWorldObject.pos_x();
					int ty=baseWorldObject.pos_y();
					if(target.pos_x()>tx)
						tx+=(int)town.getSizeValue(100);
					else
						tx-=(int)town.getSizeValue(100);
					
					if(target.pos_y()>ty)
						ty+=(int)town.getSizeValue(100);
					else
						ty-=(int)town.getSizeValue(100);
					
					gotoXY(tx, ty);
				}
				else
					gotoObject(target, false);
			}
			else
			{
				baseWorldObject.zombieRun=0;
				baseWorldObject.walkAround(false);
			}
		}
	}
	
	
	//******************
	//Laundry / Clothing
	//******************
	private void doAction_Laundry()
	{
		if(actionMode != ActionMode.LAUNDRY)
			return;
		
		if(actionTemp_String1.contains("basket_washingmachine"))
			doAction_Laundry_Basket_Washingmachine();
		
		if(actionTemp_String1.contains("washingmachine_dryer"))
			doAction_Laundry_Washingmachine_Dryer();

		if(actionTemp_String1.contains("dryer_basket"))
			doAction_Laundry_Dryer_Basket();
		
		if(actionTemp_String1.contains("collect_laundry"))
			doAction_Laundry_Collect();
	}
	private void doAction_Laundry_Basket_Washingmachine()
	{
		CWorldObject basket = targetActionObject;
		CWorldObject washingmachine = targetActionObject2;
		
		//Gehe zum Basket
		//speichere Ursprungskoordinaten
		//Nimm Basket mit und gehe zur Waschmaschine
		//stelle Basket links davor ab, Befülle Waschmaschine 
		//setze gegenseitige links über occupied2
		
		if(actionState==0)
		{
			gotoObject(basket, true);
			actionState=1;
			
			return;
		}
		
		if(actionState==1)
		{
			//Am Basket angekommen
			if(gotoObject_Arrived(basket, (int)town.getSizeValue(30)))
			{
				basket.x_temp = basket.pos_x();
				basket.y_temp = basket.pos_y();
				
				//Nimm Basket mit und gehe zur Washingmachine
				gotoObject(washingmachine, true);
				
				actionState=2;
				
				return;
			}
			
			return;
		}
		
		if(actionState==2)
		{
			baseWorldObject.actionanim1=9;
			basket.setPosition_byHumanBase(baseWorldObject, (int)town.getSizeValue(50), 0, 0);
			
			//An der Waschmaschine angekommen
			if(gotoObject_Arrived(washingmachine, washingmachine.width/2-(int)town.getSizeValue(20), (int)town.getSizeValue(-40), washingmachine.rotation()+180, true, (int)town.getSizeValue(30)))
			{
				//Stelle Basket links davor ab
				basket.setPosition_byObjectBase(washingmachine, -basket.width, -basket.height, washingmachine.rotation());
				
				baseWorldObject.actionanim1=0;
				
				//Setze Links
				basket.isOccupiedBy2=washingmachine;
				washingmachine.isOccupiedBy2=basket;
				
				deltaTimer=0;
				actionState=3;
			}
			
			return;
		}
		
		if(actionState==3)
		{
			washingmachine.doObjectAction=true; //Tür offen
						
			//Befülle Waschmaschine schrittweise
			if(deltaTimer>5)
			{
				deltaTimer=0;
				
				if(baseWorldObject.rotation()==washingmachine.rotation()+180)
				{
					//int r1 = rand.nextInt(4);
					//parentWorldObject.actionanim1=16+r1;
					baseWorldObject.actionanim1=21;
					baseWorldObject.setRotation(washingmachine.rotation()+180+80);
				}
				else
				{
					baseWorldObject.actionanim1=9;
					baseWorldObject.setRotation(washingmachine.rotation()+180);
				}
				
				if(basket.getObjectFillingMulti()>0 && washingmachine.getObjectFillingMulti()<washingmachine.getObjectFillingMultiMax())
				{
					for(Integer key1 : basket.objectFillingMulti.keySet())
					{
						int val1 = basket.objectFillingMulti.get(key1);
						if(val1>0)
						{
							if(washingmachine.objectFillingMulti.containsKey(key1))
							{
								int val2 = washingmachine.objectFillingMulti.get(key1);
								val2++;
								washingmachine.objectFillingMulti.put(key1, val2);
							}
							else
							{
								washingmachine.objectFillingMulti.put(key1, 1);
							}
							
							val1--;
							basket.objectFillingMulti.put(key1, val1);
							
							break;
						}
					}
				}
				else
				{
					//Einschalten und Action beenden
					washingmachine.actionvar1=2; //Einschalten
					washingmachine.actionvar2=0; //Timer zurücksetzen
					washingmachine.doObjectAction=false; //Tür geschlossen
					basket.objectFillingMulti.clear();
					bActionMode=false;
				}
			}
			
			return;
		}
	}
	private void doAction_Laundry_Washingmachine_Dryer()
	{
		CWorldObject basket = targetActionObject;
		CWorldObject washingmachine = targetActionObject2;
		CWorldObject dryer = targetActionObject3;
		
		//Gehe zu Basket
		//Gehe mit Basket zur Waschmaschine
		//Befülle Basket, leere washingmachine
		//washingmachine reset, basket reset
		//Bringe Basket zum Trockner
		//Stelle Basket links davor
		//setze gegenseitige occupied2 links
		//Befülle Trockner
		
		if(actionState==0)
		{
			baseWorldObject.actionanim1=0;
			gotoObject(basket, true);
			actionState=1;
			
			return;
		}
		
		if(actionState==1)
		{
			//Am Basket angekommen
			if(gotoObject_Arrived(basket, (int)town.getSizeValue(30)))
			{
				//Nimm Basket mit und gehe zur Washingmachine
				gotoObject(washingmachine, true);
				
				actionState=2;
				
				return;
			}
			
			return;
		}
		
		if(actionState==2)
		{
			baseWorldObject.actionanim1=9;
			basket.setPosition_byHumanBase(baseWorldObject, (int)town.getSizeValue(50), 0, 0);
			
			//An der Waschmaschine angekommen
			if(gotoObject_Arrived(washingmachine, washingmachine.width/2-(int)town.getSizeValue(20), (int)town.getSizeValue(-40), washingmachine.rotation()+180, true, (int)town.getSizeValue(30)))
			{
				//Stelle Basket links davor ab
				basket.setPosition_byObjectBase(washingmachine, -basket.width, -basket.height, washingmachine.rotation());
				
				baseWorldObject.actionanim1=0;
				
				//Setze Links
				basket.isOccupiedBy2=washingmachine;
				washingmachine.isOccupiedBy2=basket;
				//basket.objectFillingMulti.clear();
				
				deltaTimer=0;
				actionState=3;
			}
			
			return;
		}
		
		if(actionState==3)
		{
			//Befülle Basket, leere Washingmachine
			baseWorldObject.actionanim1=2;
			washingmachine.doObjectAction=true; //Tür offen
			
			if(deltaTimer>5)
			{
				deltaTimer=0;
				
				if(baseWorldObject.rotation()==washingmachine.rotation()+180)
				{
					baseWorldObject.actionanim1=9;
					baseWorldObject.setRotation(washingmachine.rotation()+180+80);
				}
				else
				{
					//int r1 = rand.nextInt(4);
					//parentWorldObject.actionanim1=16+r1;
					baseWorldObject.actionanim1=21;
					baseWorldObject.setRotation(washingmachine.rotation()+180);
				}
				
				if(washingmachine.getObjectFillingMulti()>0 && basket.getObjectFillingMulti()<basket.getObjectFillingMultiMax())
				{
					for(Integer key1 : washingmachine.objectFillingMulti.keySet())
					{
						int val1 = washingmachine.objectFillingMulti.get(key1);
						if(val1>0)
						{
							if(basket.objectFillingMulti.containsKey(key1))
							{
								int val2 = basket.objectFillingMulti.get(key1);
								val2++;
								basket.objectFillingMulti.put(key1, val2);
							}
							else
							{
								basket.objectFillingMulti.put(key1, 1);
							}
							
							val1--;
							washingmachine.objectFillingMulti.put(key1, val1);
							
							break;
						}
					}
				}
				else
				{
					washingmachine.actionvar1=0; //Ausschalten
					washingmachine.doObjectAction=false; //Tür geschlossen
					washingmachine.isOccupiedBy=null;
					washingmachine.isOccupiedBy2=null;
					basket.isOccupiedBy2=null;
					washingmachine.objectFillingMulti.clear();
					
					//Nimm Basket mit und gehe zum Dryer
					gotoObject(dryer, true);
					
					actionState=4;
				}
			}
			
			return;
		}
		
		if(actionState==4)
		{
			baseWorldObject.actionanim1=9;
			basket.setPosition_byHumanBase(baseWorldObject, (int)town.getSizeValue(50), 0, 0);
			
			//Am Dryer angekommen
			if(gotoObject_Arrived(dryer, dryer.width/2-(int)town.getSizeValue(20), (int)town.getSizeValue(-40), dryer.rotation()+180, true, (int)town.getSizeValue(30)))
			{
				//Stelle Basket links davor ab
				basket.setPosition_byObjectBase(dryer, -basket.width, -basket.height, dryer.rotation());
				
				baseWorldObject.actionanim1=0;
				
				//Setze Links
				basket.isOccupiedBy2=dryer;
				dryer.isOccupiedBy2=basket;
				//dryer.objectFillingMulti.clear();
				
				deltaTimer=0;
				actionState=5;
			}
			
			return;
		}
		
		if(actionState==5)
		{
			//Befülle Dryer, leere Basket
			baseWorldObject.actionanim1=2;
			dryer.doObjectAction=true; //Tür offen
			
			if(deltaTimer>5)
			{
				deltaTimer=0;
				
				if(baseWorldObject.rotation()==dryer.rotation()+180)
				{
					//int r1 = rand.nextInt(4);
					//parentWorldObject.actionanim1=16+r1;
					baseWorldObject.actionanim1=21;
					baseWorldObject.setRotation(dryer.rotation()+180+80);
				}
				else
				{
					baseWorldObject.actionanim1=9;
					baseWorldObject.setRotation(dryer.rotation()+180);
				}
				
				if(basket.getObjectFillingMulti()>0 && dryer.getObjectFillingMulti()<dryer.getObjectFillingMultiMax()) 
				{
					for(Integer key1 : basket.objectFillingMulti.keySet())
					{
						int val1 = basket.objectFillingMulti.get(key1);
						if(val1>0)
						{
							if(dryer.objectFillingMulti.containsKey(key1))
							{
								int val2 = dryer.objectFillingMulti.get(key1);
								val2++;
								dryer.objectFillingMulti.put(key1, val2);
							}
							else
							{
								dryer.objectFillingMulti.put(key1, 1);
							}
							
							val1--;
							basket.objectFillingMulti.put(key1, val1);
							
							break;
						}
					}
				}
				else
				{
					dryer.actionvar1=2; //Einschalten
					dryer.actionvar2=0; //Timer zurücksetzen
					dryer.doObjectAction=false; //Tür geschlossen
					//dryer.isOccupiedBy=null;
					dryer.isOccupiedBy2=basket;
					basket.isOccupiedBy2=dryer;
					
					basket.objectFillingMulti.clear();
					
					//Action beenden
					bActionMode=false;
				}
			}
			
			return;
		}
	}
	private void doAction_Laundry_Dryer_Basket()
	{
		CWorldObject basket = targetActionObject;
		CWorldObject dryer = targetActionObject2;
		
		//Gehe zu Basket
		//Gehe mit Basket zu Dryer
		//Befülle Basket
		//reset dryer actionvar1, actionvar2
		//reset basket actionvar1
		//Bringe Basket zum Kleiderschrank
		//Bringe Basket zum Ursprungsplatz zurück		
				
		if(actionState==0)
		{
			baseWorldObject.actionanim1=0;
			gotoObject(basket, true);
			actionState=1;
			
			return;
		}
		
		if(actionState==1)
		{
			//Am Basket angekommen
			if(gotoObject_Arrived(basket, (int)town.getSizeValue(30)))
			{
				//Nimm Basket mit und gehe zu Dryer
				gotoObject(dryer, true);
				
				actionState=2;
				
				return;
			}
			
			return;
		}
		
		if(actionState==2)
		{
			baseWorldObject.actionanim1=9;
			basket.setPosition_byHumanBase(baseWorldObject, (int)town.getSizeValue(50), 0, 0);
			
			//An der Waschmaschine angekommen
			if(gotoObject_Arrived(dryer, dryer.width/2-(int)town.getSizeValue(20), (int)town.getSizeValue(-40), dryer.rotation()+180, true, (int)town.getSizeValue(30)))
			{
				//Stelle Basket links davor ab
				basket.setPosition_byObjectBase(dryer, -basket.width, -basket.height, dryer.rotation());
				
				baseWorldObject.actionanim1=0;
				
				//Setze Links
				basket.isOccupiedBy2=dryer;
				dryer.isOccupiedBy2=basket;
				//basket.objectFillingMulti.clear();
				
				deltaTimer=0;
				actionState=3;
			}
			
			return;
		}
		
		if(actionState==3)
		{
			//Befülle Basket, leere Dryer
			baseWorldObject.actionanim1=2;
			dryer.doObjectAction=true; //Tür offen
			
			if(deltaTimer>5)
			{
				deltaTimer=0;
				
				if(baseWorldObject.rotation()==dryer.rotation()+180)
				{
					baseWorldObject.actionanim1=9;
					baseWorldObject.setRotation(dryer.rotation()+180+80);
				}
				else
				{
					//int r1 = rand.nextInt(4);
					//parentWorldObject.actionanim1=16+r1;
					baseWorldObject.actionanim1=21;
					baseWorldObject.setRotation(dryer.rotation()+180);
				}
				
				if(dryer.getObjectFillingMulti()>0 && basket.getObjectFillingMulti()<basket.getObjectFillingMultiMax())
				{
					for(Integer key1 : dryer.objectFillingMulti.keySet())
					{
						int val1 = dryer.objectFillingMulti.get(key1);
						if(val1>0)
						{
							if(basket.objectFillingMulti.containsKey(key1))
							{
								int val2 = basket.objectFillingMulti.get(key1);
								val2++;
								basket.objectFillingMulti.put(key1, val2);
							}
							else
							{
								basket.objectFillingMulti.put(key1, 1);
							}
							
							val1--;
							dryer.objectFillingMulti.put(key1, val1);
							
							break;
						}
					}
				}
				else
				{
					dryer.actionvar1=0; //Ausschalten
					dryer.doObjectAction=false; //Tür geschlossen
					dryer.isOccupiedBy=null;
					dryer.isOccupiedBy2=null;
					basket.isOccupiedBy2=null;
					dryer.objectFillingMulti.clear(); //Rest entfernen
					
					//Nimm Basket mit und laufe die einzelnen Positionen ab
					actionState=4;
				}
			}
			
			return;
		}
		
		
		//Bringe gewaschene und getrocknete Wäsche zurück zu den Ursprungsstationen
		if(actionState==4)
		{
			Boolean endAction=false;
			
			if(basket.getObjectFillingMulti()<1)
			{
				//bActionMode=false;
				actionState=7;
				gotoXY(basket.x_temp, basket.y_temp);
				return;
			}
			
			//Nimm Basket mit und laufe die einzelnen Positionen ab
			for(int key1 : basket.objectFillingMulti.keySet())
			{
				int val1 = basket.objectFillingMulti.get(key1);
				if(val1>0)
				{
					targetActionObject3 = basket.theaddress.getObjectById(key1);
					if(targetActionObject3!=null && targetActionObject3.objectFilling<targetActionObject3.getObjectFillingMax())
					{
						gotoObject(targetActionObject3, true);
						actionState=5;
						return;
					}
				}
			}
			
			//Objekt existiert nicht (mehr)
			basket.objectFillingMulti.clear();
			//bActionMode=false;
			gotoXY(basket.x_temp, basket.y_temp);
			actionState=7;
			
			return;
		}
		
		
		if(actionState==5)
		{
			baseWorldObject.actionanim1=9;
			basket.setPosition_byHumanBase(baseWorldObject, (int)town.getSizeValue(50), 0, 0);
			
			//Am Objekt angekommen
			if(gotoObject_Arrived(targetActionObject3, targetActionObject3.width/2-(int)town.getSizeValue(20), (int)town.getSizeValue(-40), targetActionObject3.rotation()+180, true, (int)town.getSizeValue(30)))
			{
				//Stelle Basket links davor ab
				basket.setPosition_byObjectBase(targetActionObject3, -basket.width, -basket.height, targetActionObject3.rotation());
				
				baseWorldObject.actionanim1=0;
				
				//Setze Links
				basket.isOccupiedBy2=targetActionObject3;
				targetActionObject3.isOccupiedBy2=basket;
				//basket.objectFillingMulti.clear();
				
				deltaTimer=0;
				actionState=6;
			}
			
			return;
		}
		
		if(actionState==6)
		{
			//Befülle targetActionObject3, leere basket
			baseWorldObject.actionanim1=2;
			targetActionObject3.doObjectAction=true; //Tür offen
			
			if(deltaTimer>5)
			{
				deltaTimer=0;
				
				if(baseWorldObject.rotation()==targetActionObject3.rotation()+180)
				{
					//int r1 = rand.nextInt(4);
					//parentWorldObject.actionanim1=16+r1;
					baseWorldObject.actionanim1=21;
					baseWorldObject.setRotation(targetActionObject3.rotation()+180+80);
				}
				else
				{
					baseWorldObject.actionanim1=9;
					baseWorldObject.setRotation(targetActionObject3.rotation()+180);
				}
				
				if(basket.objectFillingMulti.containsKey(targetActionObject3.uniqueId) &&
						basket.objectFillingMulti.get(targetActionObject3.uniqueId)>0 &&
						targetActionObject3.objectFilling<targetActionObject3.getObjectFillingMax())
				{
					int val = basket.objectFillingMulti.get(targetActionObject3.uniqueId);
					val--;
					basket.objectFillingMulti.put(targetActionObject3.uniqueId, val);
					targetActionObject3.objectFilling++;
					if(val==0)
						basket.objectFillingMulti.remove(targetActionObject3.uniqueId);
				}
				else
				{
					//dryer.actionvar1=0; //Ausschalten
					targetActionObject3.doObjectAction=false; //Tür geschlossen
					targetActionObject3.isOccupiedBy=null;
					targetActionObject3.isOccupiedBy2=null;
					basket.isOccupiedBy2=null;
					
					//In Schleife durchgehen
					actionState=4;
				}
			}
			
			return;
		}
		
		if(actionState==7)
		{
			//Bringe Basket zurück zur Startposition
			baseWorldObject.actionanim1=9;
			basket.setPosition_byHumanBase(baseWorldObject,(int)town.getSizeValue(50), 0, 0);
			
			if(baseWorldObject.ziel_x==-1)
			{
				basket.setPosition(basket.x_temp, basket.y_temp);
				basket.isOccupiedBy=null;
				basket.isOccupiedBy2=null;
				bActionMode=false;
			}
		}
	}
	private void doAction_Laundry_Collect()
	{
		CWorldObject laundry1 = targetActionObject2;
		CWorldObject basket = targetActionObject;
		
		//Gehe zu Laundry
		if(actionState==0)
		{
			gotoObjectXY(laundry1, 0, 0);
			actionState=1;
			return;
		}
		
		if(actionState==1)
		{
			//Am laundry1 angekommen
			if(gotoObject_Arrived(laundry1, 0))
			{
				baseWorldObject.actionanim1=1;
				actionState=2;
				deltaTimer=0;
				return;
			}
			
			return;
		}

		if(actionState==2)
		{
			//Laundry aufheben
			if(deltaTimer>40)
			{
				baseWorldObject.actionanim1=21;
				gotoObject(basket, true);
				actionState=3;
			}
			
			return;
		}
		
		if(actionState==3)
		{
			//hier pos setzen
			Vector2 v2 = CHelper.moveObjectByHumanRotation(baseWorldObject, laundry1, (int)town.getSizeValue(60), (int)town.getSizeValue(20));
			laundry1.setPosition((int)v2.x, (int)v2.y);
			//laundry1.setPosition(parentWorldObject.pos_x(), parentWorldObject.pos_y());
			
			if(gotoObject_Arrived(basket, (int)town.getSizeValue(30)))
			{
				basket.addObjectFillingMulti(laundry1, false);
				town.gameGui.removeWorldObject(laundry1, false);
				baseWorldObject.actionanim1=7;
				deltaTimer=0;
				actionState=4;
			}
			
			return;
		}
		
		if(actionState==4)
		{
			if(deltaTimer>10)
			{
				basket.isOccupiedBy=null;
				bActionMode=false;
			}
			
			return;
		}

		
		
		
	}
	private void doAction_ChangeClothes()
	{
		if(actionMode!=ActionMode.CHANGE_CLOTHES)
			return;
		
		CWorldObject wardrobe = targetActionObject;
		
		//Gehe zu Wardrobe
		if(actionState==0)
		{
			gotoObjectXY(wardrobe, 0, 0);
			actionState=1;
			return;
		}
		
		if(actionState==1)
		{
			//Am Wardrobe angekommen
			//if(gotoObject_Arrived(wardrobe, wardrobe.width/2, -30, wardrobe.rotation()+180, true, 10))
			if(gotoObject_Arrived(wardrobe, wardrobe.width/2, (int)town.getSizeValue(-50), wardrobe.rotation()+180, true, (int)town.getSizeValue(10)))
			{
				wardrobe.doObjectAction=true;
				baseWorldObject.actionanim1=1;
				
				actionState=2;
				deltaTimer=0;
				
				return;
			}
			
			return;
		}
		
		if(actionState==2)
		{
			if(deltaTimer>120)
			{
				deltaTimer=0;
				actionState=3;
			}
			
			return;
		}
		
		if(actionState==3)
		{
			//Change Clothes
			if(deltaTimer>40)
			{
				baseWorldObject.actionanim1=21;
				baseWorldObject.thehuman.clothingValue=0;
				wardrobe.objectFilling-=town.gameWorld.town.action_changeclothesvalue;
				wardrobe.doObjectAction=false;
				baseWorldObject.thehuman.setRandomClothingColor();
				handleActionValueBonus(1);
				
				
				//suche laundry basket
				if(baseWorldObject.theaddress!=null)
				{
					for(CWorldObject aobj : baseWorldObject.theaddress.listWorldObjects)
					{
						if(aobj.theobject.editoraction.contains("laundrybasket"))
						{
							if((aobj.isOccupiedBy==null || aobj.isOccupiedBy.uniqueId==baseWorldObject.uniqueId) 
									&& aobj.isOccupiedBy2==null 
									&& (aobj.getObjectFillingMultiMax() - aobj.getObjectFillingMulti()) >= town.gameWorld.town.action_changeclothesvalue)
							{
								
								targetActionObject2 = aobj;
								targetActionObject2.isOccupiedBy = baseWorldObject;
								break;
							}
						}
					}
				}
				
				CWorldObject laundry1 = town.gameWorld.gameResourceConfig.createWorldObject("laundryobject", baseWorldObject.pos_x(), baseWorldObject.pos_y(), town.gameWorld.getAddressByPoint(baseWorldObject.pos_x(),  baseWorldObject.pos_y()));
				Vector2 v2 = CHelper.moveVectorByHumanRotation(baseWorldObject, laundry1.width, laundry1.height, 30+rand.nextInt(20), 70+rand.nextInt(20));
				laundry1.setPosition((int)v2.x, (int)v2.y);
				laundry1.objectFillingMulti.put(wardrobe.uniqueId, town.gameWorld.town.action_changeclothesvalue);
				laundry1.color1=new Color(baseWorldObject.thehuman.clothingColor_Top.r, baseWorldObject.thehuman.clothingColor_Top.g, baseWorldObject.thehuman.clothingColor_Top.b, 1);
				targetActionObject9=laundry1;
				
				if(targetActionObject2!=null)
				{
					gotoObject(targetActionObject2, true);
					actionState=4;
				}
				else
				{
					bActionMode=false;
				}
			}
			
			return;
		}
		
		if(actionState==4)
		{
			Vector2 v2 = CHelper.moveObjectByHumanRotation(baseWorldObject, targetActionObject9, (int)town.getSizeValue(60), (int)town.getSizeValue(20));
			targetActionObject9.setPosition((int)v2.x, (int)v2.y);
			
			if(gotoObject_Arrived(targetActionObject2, (int)town.getSizeValue(10)))
			{
				targetActionObject2.addObjectFillingMulti(targetActionObject9, false);
				town.gameGui.removeWorldObject(targetActionObject9, false);
				
				baseWorldObject.actionanim1=7;
				deltaTimer=0;
				actionState=5;
			}
			
			return;
		}
		
		if(actionState==5)
		{
			if(deltaTimer>10)
			{
				targetActionObject2.isOccupiedBy=null;
				bActionMode=false;
			}
			
			return;
		}
	}
	
	//***************
	//Doctor's Office
	//***************
	private void doAction_DoctorsOffice_GotoDoctor()
	{
		if(actionMode!=ActionMode.GOTO_DOCTOR)
			return;
		
		//targetActionObject	Reception
		//targetActionObject2	Waiting Chair
		
		if(targetActionObject==null || targetActionObject2==null)
		{
			bActionMode=false;
			return;
		}
		
		CWorldObject reception = targetActionObject;
		CWorldObject waitingchair = targetActionObject2;
		CWorldObject waitingtable = targetActionObject3;
		CWorldObject treatmentchair = targetActionObject4;
		
		if(actionState==0)
		{
			actionTemp_Float1=rand.nextInt(100);
			actionTemp_Float2=rand.nextInt(70);
			gotoObjectXY(reception, Math.round((int) town.getSizeValue(-50+actionTemp_Float1)), Math.round((int) town.getSizeValue(-27-actionTemp_Float2)));
			actionState=1;
			return;
		}
		
		if(actionState==1)
		{
			//An der Rezeption angekommen
			//if(gotoObject_Arrived(reception,  Math.round(50+actionTemp_Float1), Math.round(-27-actionTemp_Float2), reception.rotation()+180, true))
			if(baseWorldObject.ziel_x==-1)
			{
				baseWorldObject.setRotation(reception.rotation()+180);
				actionState=2;
				deltaTimer=0;
				return;
			}
		}
		
		if(actionState==2)
		{
			//Warte bis Rezeption besetzt ist
			if(reception.worker.activeAction!=null && reception.worker.activeAction.bActionMode && reception.isOccupiedBy!=null)
			{
				reception.worker.actionanim1=15;
				actionState=3;
				deltaTimer=0;
				return;
			}
			
			//Max Wartezeit
			if(deltaTimer>1500)
			{
				bActionMode=false;
				waitingchair.isOccupiedBy=null;
			}
			
			return;
		}
		
		if(actionState==3)
		{
			//Anmelden an der Rezeption
			if(deltaTimer>150)
			{
				reception.worker.actionanim1=0;
				
				//Wenn es Waiting Table gibt -> Zeitschrift holen
				if(waitingtable!=null && baseWorldObject.thehuman.getEducationValue()>=CHuman.getRequiredEducationForReading())
				{
					gotoObject(waitingtable, true, waitingtable.pos_x(), waitingtable.pos_y());
					actionState=4;
				}
				else
				{
					//Sonst direkt zum Wartestuhl gehen
					gotoObject(waitingchair, true);
					actionState=6;
				}
			}
			
			return;
		}
		
		if(actionState==4)
		{
			//Am Zeitschriftentable angekommen
			try
			{
				if(waitingtable!=null && (baseWorldObject.collides(waitingtable) || baseWorldObject.ziel_x==-1))
				{
					baseWorldObject.resetPathFinding();
					actionState=5;
					deltaTimer=0;
					baseWorldObject.actionanim1=1;
				}
			}
			catch(Exception ex)
			{
				gotoObject(waitingchair, true);
				actionState=6;
				deltaTimer=0;
			}
			
			return;
		}		
		
		if(actionState==5)
		{
			//Zeitschrift nehmen
			if(deltaTimer>100)
			{
				//Zum Wartestuhl gehen
				gotoObject(waitingchair, true);
				actionState=6;
				baseWorldObject.actionanim1=0;
				baseWorldObject.actionfield1 = 1; //+rand.nextInt(3);
			}
		}
		
		if(actionState==6)
		{
			//Am Wartestuhl angekommen
			if(gotoObject_Arrived(waitingchair, waitingchair.width/2, (int)town.getSizeValue(40), waitingchair.rotation(), true))
			{
				actionState=7;
				deltaTimer=0;
				deltaTimer2=0;
				baseWorldObject.actionanim2=4; //Beine
				waitingchair.iOccupied1_Arrived=1;
			}
			
			return;
		}
		
		if(actionState==7)
		{
			//Im Dunkeln keine Zeitschrift lesen
			if(baseWorldObject.thehuman.bIsDark)
				baseWorldObject.actionfield1=0;
			
			//Treatmentchair wurde von Receptionist reserviert
			if(treatmentchair!=null)
			{
				waitingchair.iOccupied1_Arrived=0;
				waitingchair.isOccupiedBy=null;
				baseWorldObject.actionfield1=0;
				baseWorldObject.actionanim2=0;
				gotoObject(treatmentchair, true);
				actionState=8;
				return;
			}
			
			//Warten auf Termin
			if(deltaTimer2>600 && baseWorldObject.actionfield1>0)
			{
				handleActionValueBonus(1);
				deltaTimer2=0;
			}
				
			//Warten abbrechen nach Max Zeit oder wenn keine Reception oder kein Doctor mehr aktiv 
			Boolean bReception=false;
			Boolean bTreatmentChair=false;
			for(CWorldObject wobj : waitingchair.belongsToCompany.address_company.listWorldObjects)
			{
				if(wobj.theobject.editoraction.contains("company_doctorsoffice_reception_desk") && wobj.isWorkerActive())
					bReception=true;
				if(wobj.theobject.editoraction.contains("company_doctorsoffice_treatment_chair") && wobj.isWorkerActive())
					bTreatmentChair=true;
			}
			
			if(deltaTimer>8000 || bReception==false || bTreatmentChair==false)
			{
				bActionMode=false;
				waitingchair.iOccupied1_Arrived=0;
				waitingchair.isOccupiedBy=null;
				baseWorldObject.actionfield1=0;
				baseWorldObject.actionanim2=0;
				return;
			}
			
			return;
		}
		
		if(actionState==8)
		{
			//Am Behandlungsstuhl angekommen
			int movx=0;
			if(baseWorldObject.thehuman.getAge()<16)
				movx=15;
			
			if(gotoObject_Arrived(treatmentchair, (int)town.getSizeValue(54+movx), (int)town.getSizeValue(76), treatmentchair.rotation(), true))
			{
				treatmentchair.iOccupiedExtern_Arrived=1;
				actionState=9;
				baseWorldObject.actionanim2=4;
				deltaTimer=0;
			}
			
			return;
		}
		
		if(actionState==9)
		{
			if(baseWorldObject.thehuman.bIsDark) //Action abbrechen
			{
				iActionBlocker=2000;
				actionState=99;
				return;
			}
			
			//Max Watezeit auf Behandlungsstuhl
			if(deltaTimer>5000)
			{
				iActionBlocker=2000;
				actionState=99;
				return;
			}
			
			return;
		}
		
		if(actionState==99)
		{
			bActionMode=false;
			baseWorldObject.actionanim2=0;
			treatmentchair.iOccupiedExtern_Arrived=0;
			treatmentchair.isOccupiedByExtern=null; 
			baseWorldObject.actionfield1=0;
		}
		
	}
	private void doAction_DoctorsOffice_Doctor()
	{
		CWorldObject treatmentchair = targetActionObject;
		
		if(actionState==0)
		{
			//Zum Behandlungsstuhl
			gotoObject(treatmentchair, true);
			actionState=1;
			deltaTimer2=0; //Pause
			return;
		}
		
		if(actionState==1)
		{
			//Am Behandlungsstuhl angekommen
			int movx = (int)town.getSizeValue(140);
			int movy = (int)town.getSizeValue(50);
			
			if(gotoObject_Arrived(treatmentchair, movx, movy, treatmentchair.rotation()+240, true))
			{
				actionState=2;
				deltaTimer=0;
				return;
			}
		}
		
		if(actionState==2)
		{
			if(baseWorldObject.thehuman.bIsDark) //Action abbrechen
			{
				iActionBlocker=2000;
				actionState=99;
				return;
			}
		
			//Behandlung beginnen wenn Resident Platz genommen hat
			if(treatmentchair.iOccupiedExtern_Arrived==1)
			{
				actionState=3;
				return;
			}
			
			//Pause
			if(deltaTimer2>3600)
			{
				bActionMode=false;
				treatmentchair.isOccupiedBy=null;
				return;
			}			
		}
				
		
		//**********
		//Behandlung
		//**********
		if(actionState==3)
		{
			//Zum Behandlungstisch drehen und Licht einschalten
			baseWorldObject.setRotation(treatmentchair.rotation()+240-80);
			
			actionState=4;
			deltaTimer=0;
			baseWorldObject.actionanim1=1;
			
			return;
		}
		
		if(actionState==4)
		{
			//Licht ein und wieder zum Resident drehen 
			if(deltaTimer>70)
			{
				baseWorldObject.actionanim1=1;
				treatmentchair.actionfield1=1; //Licht an
				baseWorldObject.setRotation(treatmentchair.rotation()+240);
				actionState=5;
				deltaTimer=0;
				deltaTimer2=0;
			}
			
			return;
		}
		
		if(actionState==5)
		{
			if(baseWorldObject.thehuman.bIsDark) //Action abbrechen
			{
				iActionBlocker=2000;
				actionState=99;
				return;
			}
			
			if(deltaTimer2>160)
			{
				baseWorldObject.setRotation(treatmentchair.rotation()+240-80);
			}
			
			if(deltaTimer2>200)
			{
				baseWorldObject.setRotation(treatmentchair.rotation()+240);
				deltaTimer2=0;
			}
			
			//Behandlung abgeschlossen
			if(deltaTimer>1500)
			{
				handleActionValueBonus(1);
				
				actionState=99;
				return;
			}
			
			return;
		}
		
		if(actionState==99)
		{
			//Behandlung beenden
			treatmentchair.isOccupiedByExtern.activeAction.bActionMode=false;
			treatmentchair.isOccupiedByExtern.actionanim2=0;
			treatmentchair.iOccupiedExtern_Arrived=0;
			treatmentchair.isOccupiedByExtern=null; 
			treatmentchair.actionfield1=0;
			
			baseWorldObject.actionanim1=0;
			bActionMode=false;
			treatmentchair.isOccupiedBy=null;			
			
			return;
		}
		
	}
	private void doAction_DoctorsOffice_MedicalReceptionist()
	{
		CWorldObject receptiondesk = targetActionObject;
		CWorldObject resident = targetActionObject2;
		CWorldObject treatmentchair = targetActionObject3;
		
		if(actionState==0)
		{
			int movx = receptiondesk.width/2;
			int movy = (int) town.getSizeValue(90);
			
			//Gdx.app.debug("", "x: " + receptiondesk.pos_x() + ", y: " + receptiondesk.pos_y());
			gotoObjectXY(receptiondesk, movx, movy);
			actionState=1;
			deltaTimer2=0; //Pause
			return;
		}
		
		if(actionState==1)
		{
			int movx = receptiondesk.width/2-(int) town.getSizeValue(20);
			int movy = (int) town.getSizeValue(90);
			
			//Gdx.app.debug("", "x: " + parentWorldObject.pos_x() + ", y: " + parentWorldObject.pos_y());
			//Gdx.app.debug("", "ziel_x: " + parentWorldObject.ziel_x + ", ziel_y: " + parentWorldObject.ziel_y);
			
			if(gotoObject_Arrived(receptiondesk, movx, movy, receptiondesk.rotation(), true))
			{
				actionState=2;
				deltaTimer=0;
				deltaTimer2=0; //Für Pause
				receptiondesk.isOccupiedBy=baseWorldObject;
				return;
			}
		}
		
		if(actionState==2)
		{
			if(deltaTimer>500)
			{
				for(CWorldObject wobj : receptiondesk.belongsToCompany.address_company.listWorldObjects)
				{
					//Sitzt jemand im Wartezimmer?				
					if(wobj.theobject.editoraction.contains("company_doctorsoffice_reception_chair"))
					{
						if(wobj.iOccupied1_Arrived>0 && wobj.isOccupiedBy!=null)
						{
							targetActionObject2 = wobj.isOccupiedBy;
						}
					}
					
					//Ist ein Treating Chair bereit?
					if(wobj.theobject.editoraction.contains("company_doctorsoffice_treatment_chair"))
					{
						//if(wobj.isOccupiedByExtern!=null)
						//	Gdx.app.debug("", "treatmentchair_occupiedbyextern: " + wobj.isOccupiedByExtern.uniqueId);
						
						if(wobj.isOccupiedByExtern==null && wobj.isWorkerActive() && wobj.isActiveByEnergyConsumption() && wobj.isActiveByWaterConsumption())
						{
							targetActionObject3 = wobj;
						}
					}
				}
				
				deltaTimer=0;
				
				if(targetActionObject2!=null && targetActionObject3!=null)
				{
					//Treatment Chair reservieren und Resident Bescheid geben
					targetActionObject3.isOccupiedByExtern = targetActionObject2; 
					gotoObject(targetActionObject2, true);
					actionState=3;
					return;
				}
								
				if(deltaTimer2>3600) //Pause
				{
					bActionMode=false;
					receptiondesk.isOccupiedBy=null;
					return;
				}
				
				return;
			}
		}
		
		if(actionState==3)
		{
			//Am Resident angekommen und Bescheid geben
			if(baseWorldObject.collides(resident, 50) || baseWorldObject.ziel_x==-1)
			{
				baseWorldObject.resetPathFinding();
				baseWorldObject.actionanim1=12;
				actionState=4;
				deltaTimer=0;
			}
		}
		
		if(actionState==4)
		{
			if(deltaTimer>100)
			{
				baseWorldObject.actionanim1=0;
				
				if(treatmentchair!=null)
				{
					resident.activeAction.targetActionObject4 = treatmentchair;
				}
				
				//Pause
				bActionMode=false;
				receptiondesk.isOccupiedBy=null;
				return;
			}
		}
	}
	
	//********
	//Cemetery
	//********
	private void doAction_FuneralAttend()
	{
		if(actionMode!=ActionMode.FUNERAL_ATTEND)
			return;
		
		if(targetActionObject==null)
		{
			bActionMode=false;
			return;
		}
		
		if(targetActionObject3==null || targetActionObject3.worker==null || !targetActionObject3.worker.thehuman.canWork())
		{
			iActionBlocker=3500;
			return;
		}
		
		if(targetActionObject.actionstring1.contains("show_coffin_move"))
		{
			handleActionValueBonus(1);
			
			 //Rede ist beendet
			bActionMode=false;
			return;
		}
				
		CWorldObject rostrum = targetActionObject3;
				
		//TargetActionObject ist verstorbener Resident
		if(actionState==0)
		{
			//Gehe zu Begräbnis
			
			actionState=1;
			
			actionTemp_Float1=rand.nextInt(400); // MovX
			actionTemp_Float2=rand.nextInt(350); // MovY
			
			//nicht auf sarg stellen
			//x 65, y 60, w 100, h 240
			while(actionTemp_Float1>(180) && actionTemp_Float1<(330))
				actionTemp_Float1=rand.nextInt(400);
						
			Vector2 v2 = CHelper.moveHumanByObjectRotation(targetActionObject, baseWorldObject, (int)town.getSizeValue(-200+actionTemp_Float1), (int)town.getSizeValue(actionTemp_Float2));
			gotoObject(targetActionObject, true, (int)v2.x, (int)v2.y);
			
			return;
		}
		
		if(actionState==1)
		{
			//Am Begräbnis angekommen
			//Gdx.app.debug("", "debuglog1: " + targetActionObject + ", " + actionTemp_Float1 + ", " + actionTemp_Float2);
			if(gotoObject_Arrived(targetActionObject, (int)town.getSizeValue(-200+actionTemp_Float1), (int)town.getSizeValue(actionTemp_Float2), targetActionObject.rotation(), true))
			{
				actionState=2;
				deltaTimer=0;
			}
			
			return;
		}
		
		//Der Totenrede zuhören
		if(actionState==2)
		{
			if(!targetActionObject.actionstring1.equals("show_coffin"))
				bActionMode=false;

			if(targetActionObject.actionstring1.equals("show_coffin_move"))
				bActionMode=false;
			
			//if(rostrum.isOccupiedBy==null)
			if(rostrum.actionvar9==0)
				bActionMode=false;
			
			if(deltaTimer>1000) //Max Wartezeit, falls Redner nicht erscheint
			{
				iActionBlocker=3500;
				handleActionValueBonus(1);
			}
			
			if(bActionMode==false)
				handleActionValueBonus(1);
			
			return;
		}
	}
	private void doAction_FuneralSpeaker()
	{
		CWorldObject rostrum = targetActionObject;
		
		//Gdx.app.debug("", "show actionstate doAction_FuneralSpeaker: " + actionState);
				
		if(targetActionObject3!=null && targetActionObject3.actionstring1.contains("show_coffin_move"))
		{
			bActionMode=false;
			targetActionObject.isOccupiedBy=null;
			return; //Rede ist beendet
		}
		
		//workaround da actionstate von driver 10.1 übernommen wird
		if(actionState>2)
			actionState=0;
		
		
		if(actionState==0)
		{
			//Gehe zu Rednerpodium
			gotoObject(rostrum, true);			
			actionState=1;
			
			return;
		}
		
		if(actionState==1)
		{
			//Am Rednerpodium angekommen
			if(gotoObject_Arrived(rostrum, (int)town.getSizeValue(60), (int)town.getSizeValue(-10), rostrum.rotation()+180, true))
			{
				actionState=2;
				rostrum.actionfield1=1; //show paper on desk
				deltaTimer=0;
				deltaTimer2=0;
				actionTemp_Float1=rand.nextInt(50);
			}
			
			return;
		}
		
		//Totenrede halten
		if(actionState==2)
		{
			if(deltaTimer>actionTemp_Float1)
			{
				deltaTimer=0;
				actionTemp_Float1=rand.nextInt(50);
				baseWorldObject.actionanim1 = rand.nextInt(15+1);
			}
			
			if(deltaTimer2 > (3600+1500)) //ca 1,5 Stunden Dauer
			{
				//Increase Skill Level
				increaseTaskSkillLevel(rostrum);
				
				bActionMode=false;
				rostrum.isOccupiedBy=null;
				rostrum.actionstring1=""; //funeral trigger zurücksetzen (trigger_funeral)
				rostrum.actionfield1=0; //blatt wegnehmen
				CWorldObject wobj = town.gameWorld.tempHumansDead.get(Math.round(rostrum.actionvar9));
				if(wobj!=null)
				{
					wobj.actionstring1="show_coffin_move"; //trigger für besucher: burial ist beendet
				}
				
				rostrum.actionvar9=0; //deceased id entfernen
			}
			
			return;
		}
	}
	private void doAction_Gravedigger()
	{
		//Zusatzaction wird von Hearse Driver ausgeführt
		
		//Muss immer schauen ob Graves irgendwo herumliegen
		//Gdx.app.debug("doAction_Gravedigger " + + parentWorldObject.uniqueId, "actionstate: " + actionState);
		CWorldObject deceased = targetActionObject;
		CWorldObject graveempty = targetActionObject2;
		
		if(actionState==0)
		{
			gotoObject(deceased, true);
			actionState=1;
			
			return;
		}
		
		if(actionState==1)
		{
			//Am Deceased angekommen
			if(gotoObject_Arrived(deceased, -1, -1, -1, true))
			{
				//Bringe deceased zum grave
				deceased.actionstring1="show_coffin_move"; //trigger für besucher: burial ist beendet
				gotoObject(graveempty, true);
				actionState=2;
			}
		}
		
		if(actionState==2)
		{
			Vector2 v2 = CHelper.moveObjectByHumanRotation(baseWorldObject, deceased, (int)town.getSizeValue(57), (int)town.getSizeValue(-97));
			deceased.setPosition((int)v2.x, (int)v2.y);
			deceased.setRotation(baseWorldObject.rotation());
			baseWorldObject.actionanim1=2;
			
			//Am Grave angekommen
			if(gotoObject_Arrived(graveempty, -1, -1, -1, true))
			{
				actionState=3;
			}
		}
		
		if(actionState==3)
		{
			//Deceased in grave umwandeln und auf grave empty positionieren
			deceased.setPosition(graveempty.pos_x(), graveempty.pos_y());
			deceased.setRotation(graveempty.rotation());
			
			Optional<CWorldObject> obj=null;
			
			deceased.actionstring1="show_grave" + (1+rand.nextInt(10)); //1-10
			obj = graveempty.theaddress.listWorldObjects.stream().filter(item->item.theobject.editoraction.contains(deceased.actionstring1)).findFirst();
			
			//Nehme wenn möglich ein Grab-GFX, das noch nicht auf dem Friedhof existiert
			int icount=0;
			while(obj!=null && obj.isPresent())
			{
				deceased.actionstring1="show_grave" + (1+rand.nextInt(10+1));
				obj = graveempty.theaddress.listWorldObjects.stream().filter(item->item.theobject.editoraction.contains(deceased.actionstring1)).findFirst();
				icount++;
				if(icount>200)
					break;
			}
			
			//Adresse ändern
			deceased.changeAddress(graveempty.theaddress);
			
			//Grave empty entfernen
			town.gameGui.removeWorldObject(graveempty, false);
			
			deceased.isOccupiedBy=null;
			bActionMode=false;
			
			return;
		}
	}
	private void doAction_Hearse()
	{
		//Gdx.app.debug("", ""+actionState); 
		
		CWorldObject vehicle = targetActionObject;
		CWorldObject deceased = targetActionObject2;
		CWorldObject rostrum = targetActionObject3;
		CWorldObject graveempty = targetActionObject4;
		
		if(actionState==0)
		{
			rostrum.isOccupiedBy = baseWorldObject; //Trigger wenn von speaker auf null gesetzt wird
			
			//Speichere Parkkoordinaten, wohin der Hearse nach der Action wieder zurückgebracht wird
			if(vehicle.x_temp>0) //Koordinaten nur überschreiben wenn Standort-Position auf Company-Adr ist 
			{
				CAddress adr = town.gameWorld.getAddressByPoint(vehicle.pos_x(), vehicle.pos_y());
				if(adr!=null && vehicle.belongsToCompany!=null && vehicle.belongsToCompany.address_company!=null && adr.addressId==vehicle.belongsToCompany.address_company.addressId)
				{
					vehicle.x_temp=vehicle.pos_x();
					vehicle.y_temp=vehicle.pos_y();
					vehicle.rotation_temp=vehicle.rotation();
				}
			}
			else
			{
				vehicle.x_temp=vehicle.pos_x();
				vehicle.y_temp=vehicle.pos_y();
				vehicle.rotation_temp=vehicle.rotation();
			}
			
			if(deceased.theaddress2!=null)
			{
				if(deceased.theaddress2.listWorldObjects.contains(deceased))
					deceased.theaddress2.listWorldObjects.remove(deceased);
			}
			deceased.theaddress2=rostrum.theaddress;
			deceased.theaddress2.listWorldObjects.add(deceased);
			
			
			//Gehe zum Vehicle
			gotoObjectXY(vehicle, vehicle.width, (int)(vehicle.height/2));
						
			deltaTimer=0;
			actionState=0.1f;
			
			return;
		}
		
		if(actionState==0.1f)
		{
			//Am Vehicle angekommen
			if(gotoObject_Arrived(vehicle, vehicle.width, (int)(vehicle.height/2), vehicle.rotation(), false))
			{
				vehicle.isOccupiedBy=baseWorldObject;
				vehicle.actionanim1=1;
				deltaTimer=0;
				actionState=1;
			}
			
			return;
		}
		
		if(actionState==1)
		{
			if(deltaTimer>25)
			{
				//Fahre mit Vehicle zum Verstorbenen bzw zur Adresse
				vehicle.actionanim1=0;
				
				gotoObjectXY(vehicle, deceased, vehicle.width, vehicle.height/2);

				actionState=2;
			}
			
			return;
		}
		
		if(actionState==2)
		{
			baseWorldObject.bDrawObject=false;
			baseWorldObject.setPosition((int)vehicle.pos_x(), (int)vehicle.pos_y());
			
			//Befindet sich Deceased auf Adresse: nicht in adr reinfahren
			CAddress adr = baseWorldObject.town.gameWorld.getAddressByPoint(deceased.pos_x(), deceased.pos_y());
			Boolean badr=false;
			if(adr!=null)
				badr = adr.testpoint(vehicle.pos_x(), vehicle.pos_y());
			
			//Am Ziel angekommen
			if(gotoObject_Arrived(vehicle, deceased, vehicle.width, vehicle.height/2, vehicle.rotation(), true) || badr)
			{
				vehicle.resetPathFinding();
				vehicle.actionanim1=1;
				deltaTimer=0;
				
				Vector2 v2 = CHelper.moveHumanByObjectRotation(vehicle, baseWorldObject, vehicle.width, vehicle.height/2);
				baseWorldObject.setPosition((int)v2.x, (int)v2.y);
				baseWorldObject.bDrawObject=true;
				actionState=2.1f;
			}
			
			return;
		}
				
		if(actionState==2.1f)
		{
			if(deltaTimer>25)
			{
				vehicle.actionanim1=0;
				actionState=3;
			}
			
			return;
		}
				
		if(actionState==3)
		{
			//Worker steigt aus und holt Verstorbenen
			gotoObject(deceased, true);
			
			baseWorldObject.actionanim1=2; //Sarg schieben
			vehicle.actionanim1=2;
			
			actionState=4;
			
			return;
		}
		
		if(actionState==4)
		{
			//Am Verstorbenen angekommen
			if(gotoObject_Arrived(deceased, -1, -1, -1, true))
			{
				baseWorldObject.actionanim1=0;
				Vector2 v2=CHelper.moveHumanByObjectRotation(vehicle, baseWorldObject, -vehicle.width/2, vehicle.height+(int)town.getSizeValue(200));
				gotoObject(vehicle, true, (int)v2.x, (int)v2.y);
				actionState=5;
			}
			
			return;
		}
		
		if(actionState==5)
		{
			baseWorldObject.actionanim1=2;
			deceased.bDrawObject=false;
			
			//Verstorbenen zum Hearse bringen
			if(gotoObject_Arrived(vehicle, -1, -1, -1, false))
			{
				baseWorldObject.setRotation(vehicle.rotation());
				
				Vector2 v2 = CHelper.moveHumanByObjectRotation(vehicle, baseWorldObject, vehicle.width/2-targetActionObject2.width/2, vehicle.height-(int)town.getSizeValue(70));
				gotoObject(vehicle, true, (int)v2.x, (int)v2.y);
				//deceased.bDrawObject=false;
				actionState=6;
			}
			
			Vector2 v2 = CHelper.moveObjectByHumanRotation(baseWorldObject, deceased, (int)town.getSizeValue(57), (int)town.getSizeValue(-10));
			deceased.setPosition((int)v2.x, (int)v2.y);
			
			return;
		}
		
		if(actionState==6)
		{
			//Verstorbener am Hearse
			//if(gotoObject_Arrived(vehicle, -1, -1, -1, false))
			if(baseWorldObject.ziel_x==-1)
			{
				//Steige ein
				baseWorldObject.actionanim1=0;
				gotoObjectXY(vehicle, vehicle.width, vehicle.height/2);
				actionState=6.1f;
				
				return;
			}
			
			Vector2 v2 = CHelper.moveObjectByHumanRotation(baseWorldObject, targetActionObject2, (int)town.getSizeValue(57), (int)town.getSizeValue(-10));
			deceased.setPosition((int)v2.x, (int)v2.y);
			
			return;
		}
		
		if(actionState==6.1f)
		{
			if(baseWorldObject.ziel_x==-1)
			{
				vehicle.actionanim1=1;
				deltaTimer=0;
				actionState=7f;
			}
			
			return;
		}
		
		if(actionState==7)
		{
			if(deltaTimer>25)
			{
				//Fahre zur Friedhof-Parkposition
				vehicle.actionanim1=0;
				baseWorldObject.bDrawObject=false;
				gotoObject(vehicle, null, true, (int)vehicle.x_temp, (int)vehicle.y_temp);
				actionState=8;
			}
			
			return;
		}
		
		if(actionState==8)
		{
			deceased.setPosition((int)vehicle.pos_x(), (int)vehicle.pos_y());
			baseWorldObject.setPosition((int)vehicle.pos_x(), (int)vehicle.pos_y());
			
			//Am Friedhof angekommen
			if(gotoObject_Arrived(vehicle, null, 0, 0, 0, false))
			{
				vehicle.setPosition((int)vehicle.x_temp, (int)vehicle.y_temp);
				vehicle.setRotation((int)vehicle.rotation_temp);
				vehicle.isOccupiedBy = null; //Licht ausmachen
				
				actionState=8.1f;
				vehicle.actionanim1=1;
			}
			
			return;
		}
		
		if(actionState==8.1f)
		{
			//Tür auf
			vehicle.actionanim1=1;
			deltaTimer=0;
			actionState=9;
			
			return;
		}
		
		if(actionState==9)
		{
			if(deltaTimer>25)
			{
				vehicle.actionanim1=2;
				
				//Bringe Sarg zum Rednerpult
				Vector2 v2 = CHelper.moveHumanByObjectRotation(vehicle, baseWorldObject, vehicle.width, vehicle.height/2);
				baseWorldObject.setPosition((int)v2.x, (int)v2.y);
				baseWorldObject.bDrawObject=true;
				
				//Hole Sarg raus
				v2 = CHelper.moveHumanByObjectRotation(vehicle, baseWorldObject, vehicle.width/2-targetActionObject2.width/2, vehicle.height-(int)town.getSizeValue(70));
				gotoObject(vehicle, true, (int)v2.x, (int)v2.y);
				
				actionState=9.1f;
			}
			
			return;
		}
		
		if(actionState==9.1f)
		{
			if(baseWorldObject.ziel_x==-1)
			{
				Vector2 v2 = CHelper.moveHumanByObjectRotation(rostrum, baseWorldObject, 1, (int)town.getSizeValue(300));
				gotoObject(rostrum, true, (int)v2.x, (int)v2.y);
				baseWorldObject.actionanim1=2;
				actionState=10f;
			}
			
			return;
		}
		
		if(actionState==10)
		{
			vehicle.actionanim1=0;
			
			//Am Rednerpult angekommen
			if(gotoObject_Arrived(rostrum, (int)town.getSizeValue(150), (int)town.getSizeValue(300), rostrum.rotation(), true))
			{
				actionState=10.1f;
				rostrum.actionstring1="trigger_funeral";
				deceased.actionstring1="show_coffin";
				deceased.bDrawObject=true;
				
				Vector2 v2 = CHelper.moveHumanByObjectRotation(rostrum, baseWorldObject, rostrum.width/2, (int)town.getSizeValue(300));
				deceased.setPosition((int)v2.x, (int)v2.y);
				deceased.setRotation(rostrum.rotation());
				baseWorldObject.actionanim1=0;
			}
			
			return;
		}
		
		if(actionState==10.1f)
		{
			deceased.isOccupiedBy=null;
			bActionMode=false;
			return;
		}
		
		//----------------------------------------------------------------------------
		//----------------------------------------------------------------------------
				
		if(actionState==11)
		{
			//Warte auf Ende der Rede
			if(rostrum.isOccupiedBy==null)
			{
				gotoObject(deceased, true);
				actionState=12;
			}
			
			return;
		}
		
		if(actionState==12)
		{
			//Am Deceased angekommen
			if(gotoObject_Arrived(deceased, -1, -1, -1, true))
			{
				//Bringe deceased zum grave
				deceased.actionstring1="show_coffin_move"; //trigger für besucher: burial ist beendet
				gotoObject(graveempty, true);
				actionState=13;
			}
		}
		
		if(actionState==13)
		{
			Vector2 v2 = CHelper.moveObjectByHumanRotation(baseWorldObject, deceased, (int)town.getSizeValue(57), (int)town.getSizeValue(-97));
			deceased.setPosition((int)v2.x, (int)v2.y);
			deceased.setRotation(baseWorldObject.rotation());
			baseWorldObject.actionanim1=2;
			
			//Am Grave angekommen
			if(gotoObject_Arrived(graveempty, -1, -1, -1, true))
			{
				actionState=14;
			}
		}
		
		if(actionState==14)
		{
			//Deceased in grave umwandeln und auf grave empty positionieren
			deceased.setPosition(graveempty.pos_x(), graveempty.pos_y());
			deceased.setRotation(graveempty.rotation());
			
			Optional<CWorldObject> obj=null;
			
			deceased.actionstring1="show_grave" + (1+rand.nextInt(10)); //1-10
			obj = graveempty.theaddress.listWorldObjects.stream().filter(item->item.theobject.editoraction.contains(deceased.actionstring1)).findFirst();
			
			//Nehme wenn möglich ein Grab-GFX, das noch nicht auf dem Friedhof existiert
			int icount=0;
			while(obj!=null && obj.isPresent())
			{
				deceased.actionstring1="show_grave" + (1+rand.nextInt(10+1));
				obj = graveempty.theaddress.listWorldObjects.stream().filter(item->item.theobject.editoraction.contains(deceased.actionstring1)).findFirst();
				icount++;
				if(icount>200)
					break;
			}
			
			//Adresse ändern
			deceased.changeAddress(graveempty.theaddress);
			
			//Grave empty entfernen
			town.gameGui.removeWorldObject(graveempty, false);
			
			deceased.isOccupiedBy=null;
			bActionMode=false;
			
			return;
		}
	}
	
	//*******
	//Vehicle
	//*******
	private void doAction_VehicleRefuel()
	{
		if(actionMode!=ActionMode.VEHICLE_REFUEL)
			return;
		
		CWorldObject vehicle = targetActionObject5; //parentWorldObject.thehuman.car;
		CWorldObject gaspump = targetActionObject;
		CWorldObject checkout = targetActionObject2;
		//hier anpassen für truck, prüfe: ist man im truck oder nicht?
				
		if(actionState==0)
		{
			//Speichere Vehicle Koordinaten
			vehicle.x_temp = vehicle.pos_x();
			vehicle.y_temp = vehicle.pos_y();
			vehicle.rotation_temp = vehicle.rotation();
			
			//Gehe zum Fahrzeug
			gotoObject(vehicle, true);
			
			actionState=1;
			
			return;
		}
		
		if(actionState==1)
		{
			//Am Fahrzeug angekommen
			if(gotoObject_Arrived(vehicle, -1, -1, -1, true))
			{
				vehicle.isOccupiedBy=baseWorldObject;
				
				//Fahre zu Gaspump
				gotoObject(vehicle, gaspump, true, 0, 0);
				actionState=2;
			}
			
			return;
		}
		
		if(actionState==2)
		{
			baseWorldObject.bDrawObject=false;
			baseWorldObject.setPosition((int)vehicle.pos_x(), (int)vehicle.pos_y());
			
			//Am Ziel angekommen
			int posx=vehicle.width+(int)town.getSizeValue(192);
			int posy=(vehicle.height/2)-(int)town.getSizeValue(50);
			
			if(vehicle.theobject.editoraction.contains("company_recyclingcenter_garbagetruck_traffic_car"))
			{
				posx=(int)town.getSizeValue(245);
				posy=(int)town.getSizeValue(200);
			}
			
			if(vehicle.theobject.editoraction.contains("company_urbancemetery_hearse_traffic_car"))
			{
				posx=(int)town.getSizeValue(245+185);
				posy=(int)town.getSizeValue(200+200);
			}
			
			if(gotoObject_Arrived(vehicle, gaspump, posx, posy, gaspump.rotation()+180, true))
				actionState=3;
			
			return;
		}
		
		if(actionState==3)
		{
			//Resident steigt aus und tankt
			Vector2 v2 = CHelper.moveHumanByObjectRotation(vehicle, baseWorldObject, (int)town.getSizeValue(-10), vehicle.height);
			baseWorldObject.setPosition((int)v2.x, (int)v2.y);
			baseWorldObject.bDrawObject=true;
			
			gaspump.actionfield1=1; //show tankbutt
			deltaTimer=0;
			actionState=4;
			
			return;
		}
		
		if(actionState==4)
		{
			if(deltaTimer>800)
			{
				float diff = vehicle.fuelValueMax - vehicle.fuelValue;
				actionTemp_Float1=diff;
				vehicle.fuelValue=vehicle.fuelValueMax;
				
				deltaTimer=0;
				gaspump.actionfield1=0;
				actionState=5;
			}
			
			return;
		}
		
		if(actionState==5)
		{
			if(checkout.isActiveByEnergyConsumption() && checkout.isWorkerActive() && checkout.isOccupiedByExtern==null)
			{
				checkout.isOccupiedByExtern=baseWorldObject;
				gotoObject(checkout, true);
				actionState=6;
				deltaTimer=0;
			}
			
			if(deltaTimer>1000) //warten dass checkout frei wird
			{
				cancelAction("Refuel: checkout not active");
			}
			
			return;
		}
		
		if(actionState==6)
		{
			//Am Checkout angekommen
			if(gotoObject_Arrived(checkout, (int)town.getSizeValue(120), (int)town.getSizeValue(130), checkout.rotation(), false))
			{
				deltaTimer=0;
				actionState=7;
			}
			
			return;
		}
		
		if(actionState==7)
		{
			if(checkout.worker!=null)
			{
				if(checkout.worker.activeAction!=null && checkout.worker.activeAction.bGotoActionMode==true)
					deltaTimer=0;
				else
					checkout.worker.actionanim1=1;
			}
						
			//Bezahlen
			if(deltaTimer>300)
			{
				if(checkout.worker!=null)
					checkout.worker.actionanim1=0;
				
				//gameWorld.changeTownMoney(-(int)actionTemp_Float1);
				//gameWorld.townStatistics.getCurrentStatistics_Finance().residentSpendsMoney+=actionTemp_Float1;
				//baseWorldObject.addAnimationEvent(AnimationEventType.MONEY, -(int)actionTemp_Float1);
				
				actionState=8;
			}
		}
		
		if(actionState==8)
		{
			//Action beenden
			gaspump.isOccupiedBy=null;
			checkout.isOccupiedByExtern=null;
			targetActionObject5=null;
			bActionMode=false;
			
			return;
		}
	}
	
	//***************
	//Fitness Studio
	//**************
	private void doAction_Fitness_Studio_Treadmill()
	{
		if(actionState==0)
		{
			gotoObject(targetActionObject, true);
			actionState=1;
			return;
		}
		
		if(actionState==1)
		{
			if(gotoObject_Arrived(targetActionObject, baseWorldObject.width_human()/2, (int)town.getSizeValue(140), targetActionObject.rotation()+180, true))
			{
				targetActionObject.iOccupied1_Arrived=1;
				actionState=2;
				deltaTimer=0;
			}
			
			return;
		}

		if(actionState==2)
		{
			baseWorldObject.actionanim1=4;
			if(deltaTimer>70)
			{
				actionState=3;
				baseWorldObject.actionanim1=101;
				deltaTimer=0;
				deltaTimer2=0;
				baseWorldObject.objectAnimSpeedModifier=rand.nextInt(10)/10;
				
				baseWorldObject.actionvar1=600+rand.nextInt(600);
				baseWorldObject.actionvar2=rand.nextInt(100);
				if(baseWorldObject.actionvar2>50)
					baseWorldObject.actionvar2=50-baseWorldObject.actionvar2;
			}
			
			return;
		}
		
		
		if(actionState==3)
		{
			if(deltaTimer>50)
			{
				//Vector2 v2 = CHelper.moveHumanByObjectRotation(targetActionObject, parentWorldObject, parentWorldObject.width_human()/2+rand.nextInt(8)-rand.nextInt(8), 100+rand.nextInt(10)-rand.nextInt(10)+(int)parentWorldObject.actionvar2);
				Vector2 v2 = CHelper.moveHumanByObjectRotation(targetActionObject, baseWorldObject, (int)town.getSizeValue(67), (int)town.getSizeValue(100+rand.nextInt(10)-rand.nextInt(10)+(int)baseWorldObject.actionvar2));
				baseWorldObject.setRotation(targetActionObject.rotation()+180);
				baseWorldObject.setPosition((int)v2.x, (int)v2.y);
				
				if(deltaTimer2>baseWorldObject.actionvar1)
				{
					bActionMode=false;
					baseWorldObject.objectAnimSpeedModifier=0;
					handleActionValueBonus(1);
					resetActionObjects();
				}
			}
			
			return;
		}
	}
	private void doAction_Fitness_Studio_StationaryBike()
	{
		if(actionState==0)
		{
			gotoObject(targetActionObject, true);
			actionState=1;
			return;
		}
		
		if(actionState==1)
		{
			if(baseWorldObject.ziel_x==-1 || baseWorldObject.collides(targetActionObject))
			{
				targetActionObject.iOccupied1_Arrived=1;
				actionState=2;
				baseWorldObject.actionanim1=3;
				baseWorldObject.actionanim2=1;
				deltaTimer=0;
				deltaTimer2=0;
				
				//parentWorldObject.objectAnimSpeedModifier=rand.nextInt(10)/10;
				baseWorldObject.actionvar1=600+rand.nextInt(600);
				//parentWorldObject.actionvar2=rand.nextInt(100);
				//if(parentWorldObject.actionvar2>50)
				//	parentWorldObject.actionvar2=50-parentWorldObject.actionvar2;
				
				baseWorldObject.resetPathFinding();
				
				
				
				//Vector2 v2 = CHelper.moveHumanByObjectRotation(targetActionObject, parentWorldObject, 50, 115-parentWorldObject.height_human()/2);
				Vector2 v2 = CHelper.moveHumanByObjectRotation(targetActionObject, baseWorldObject, (int)town.getSizeValue(50), Math.round((int)town.getSizeValue(52)/baseWorldObject.getBodySizeByAge()));
				baseWorldObject.setRotation(targetActionObject.rotation()+180);
				baseWorldObject.setPosition((int)v2.x, (int)v2.y);
			}
			
			return;
		}
		
		if(actionState==2)
		{
			//			Vector2 v2 = CHelper.moveHumanByObjectRotation(targetActionObject, parentWorldObject, 50, 39);
			//			parentWorldObject.setRotation(targetActionObject.rotation()+180);
			//			parentWorldObject.setPosition((int)v2.x, (int)v2.y);
			
			if(deltaTimer2>baseWorldObject.actionvar1)
			{
				bActionMode=false;
				baseWorldObject.objectAnimSpeedModifier=0;
				handleActionValueBonus(1);
				resetActionObjects();
			}
			
			return;
		}
	}
	private void doAction_Fitness_Studio_Pecmachine()
	{
		if(actionState==0)
		{
			gotoObject(targetActionObject, true);
			actionState=1;
			return;
		}
		
		if(actionState==1)
		{
			if(gotoObject_Arrived(targetActionObject, (int)town.getSizeValue(103), (int)town.getSizeValue(106), targetActionObject.rotation(), true))
			{
				targetActionObject.iOccupied1_Arrived=1;
				baseWorldObject.resetPathFinding();
				actionState=2;
				deltaTimer=0;
				deltaTimer2=0;
				baseWorldObject.actionanim2=3;
				baseWorldObject.actionvar1=600+rand.nextInt(600);
			}
			
			return;
		}
		
		if(actionState==2)
		{
			if(deltaTimer>30)
			{
				if(targetActionObject.actionfield1==0)
				{
					targetActionObject.actionfield1=1;
					baseWorldObject.actionanim1=11;
				}
				else
				{
					targetActionObject.actionfield1=0;
					baseWorldObject.actionanim1=10;
				}
				
				deltaTimer=0;
			}
			
			if(deltaTimer2>baseWorldObject.actionvar1)
			{
				bActionMode=false;
				handleActionValueBonus(1);
				resetActionObjects();
			}
			
			return;
		}
		
		return;
	}
	private void doAction_Fitness_Studio_ShoulderPress()
	{
		//Dip Machine
		
		if(actionState==0)
		{
			gotoObject(targetActionObject, true);
			actionState=1;
			return;
		}
		
		if(actionState==1)
		{
			if(gotoObject_Arrived(targetActionObject, (int)town.getSizeValue(78), Math.round((int)town.getSizeValue(76)*baseWorldObject.getBodySizeByAge()), targetActionObject.rotation(), true))
			{
				targetActionObject.iOccupied1_Arrived=1;
				baseWorldObject.resetPathFinding();
				actionState=2;
				deltaTimer=0;
				deltaTimer2=0;
				baseWorldObject.actionanim2=3;
				baseWorldObject.actionvar1=600+rand.nextInt(600);
			}
			
			return;
		}
		
		if(actionState==2)
		{
			if(deltaTimer>30)
			{
				if(targetActionObject.actionfield1==1)
				{
					targetActionObject.actionfield1=0;
					baseWorldObject.actionanim1=9;
					if(baseWorldObject.thehuman.getAge()<17)
						baseWorldObject.actionanim1=11;
				}
				else
				{
					targetActionObject.actionfield1=1;
					baseWorldObject.actionanim1=8;
					if(baseWorldObject.thehuman.getAge()<17)
						baseWorldObject.actionanim1=10;

				}
				
				deltaTimer=0;
			}
			
			if(deltaTimer2>baseWorldObject.actionvar1)
			{
				bActionMode=false;
				handleActionValueBonus(1);
				resetActionObjects();
			}
			
			return;
		}
		
		return;
	}
	private void doAction_Fitness_Studio_Legpress()
	{
		if(actionState==0)
		{
			gotoObject(targetActionObject, true);
			actionState=1;
			return;
		}
		
		if(actionState==1)
		{
			//body size ausgleich: hier muss durch size geteilt werden damit position so angepasst wird, dass arme an das gerät kommen
			//if(gotoObject_Arrived(targetActionObject, 68, Math.round(60/parentWorldObject.getBodySizeByAge()), targetActionObject.rotation()+180, true))
			if(gotoObject_Arrived(targetActionObject, (int)town.getSizeValue(73), Math.round((int)town.getSizeValue(22)/baseWorldObject.getBodySizeByAge()), targetActionObject.rotation()+180, true))
			{
				targetActionObject.iOccupied1_Arrived=1;
				baseWorldObject.resetPathFinding();
				actionState=2;
				deltaTimer=0;
				deltaTimer2=0;
				baseWorldObject.actionanim1=0;
				baseWorldObject.actionvar1=1000+rand.nextInt(800);
			}
			
			return;
		}
		
		if(actionState==2)
		{
			if(deltaTimer>30)
			{
				if(targetActionObject.actionfield1==1)
				{
					targetActionObject.actionfield1=0;
					baseWorldObject.actionanim2=4;
				}
				else
				{
					targetActionObject.actionfield1=1;
					baseWorldObject.actionanim2=3;
				}
				
				deltaTimer=0;
			}
			
			if(deltaTimer2>baseWorldObject.actionvar1)
			{
				bActionMode=false;
				handleActionValueBonus(1);
				resetActionObjects();
			}
			
			return;
		}
		
		return;		
	}
	private void doAction_Fitness_Studio_Latpull()
	{
		if(actionState==0)
		{
			gotoObject(targetActionObject, true);
			actionState=1;
			return;
		}
		
		if(actionState==1)
		{
			//body size ausgleich: hier muss durch size geteilt werden damit position so angepasst wird, dass arme an das gerät kommen
			if(gotoObject_Arrived(targetActionObject, (int)town.getSizeValue(68), Math.round((int)town.getSizeValue(60)/baseWorldObject.getBodySizeByAge()), targetActionObject.rotation()+180, true))
			{
				targetActionObject.iOccupied1_Arrived=1;
				baseWorldObject.resetPathFinding();
				actionState=2;
				deltaTimer=0;
				deltaTimer2=0;
				baseWorldObject.actionvar1=600+rand.nextInt(600);
			}
			
			return;
		}
		
		if(actionState==2)
		{
			if(deltaTimer>30)
			{
				if(targetActionObject.actionfield1==1)
				{
					targetActionObject.actionfield1=0;
					baseWorldObject.actionanim1=13;
				}
				else
				{
					targetActionObject.actionfield1=1;
					baseWorldObject.actionanim1=14;
				}
				
				deltaTimer=0;
			}
			
			if(deltaTimer2>baseWorldObject.actionvar1)
			{
				bActionMode=false;
				handleActionValueBonus(1);
				resetActionObjects();
			}
			
			return;
		}
		
		return;
	}
	private void doAction_Fitness_Studio_DumbBell()
	{
		
	}
	private void doAction_Fitness_Studio_BarBell()
	{
		
	}
	private void doAction_Fitness_Studio()
	{
		if(actionMode!=ActionMode.FITNESS_STUDIO)
			return;
		
		if(targetActionObject==null)
			return;
		
		if(!targetActionObject.isActiveByEnergyConsumption())
		{
			bActionMode=false;
			resetActionObjects();
			return;
		}
		
		cancelIfDark();
		
		if(targetActionObject.theobject.editoraction.contains("company_fitnessstudio_treadmill"))
			doAction_Fitness_Studio_Treadmill();
		
		if(targetActionObject!=null && targetActionObject.theobject.editoraction.contains("company_fitnessstudio_stationarybike"))
			doAction_Fitness_Studio_StationaryBike();

		if(targetActionObject!=null && targetActionObject.theobject.editoraction.contains("company_fitnessstudio_shoulderpress"))
			doAction_Fitness_Studio_ShoulderPress();

		if(targetActionObject!=null && targetActionObject.theobject.editoraction.contains("company_fitnessstudio_pecmachine"))
			doAction_Fitness_Studio_Pecmachine();

		if(targetActionObject!=null && targetActionObject.theobject.editoraction.contains("company_fitnessstudio_legpress"))
			doAction_Fitness_Studio_Legpress();

		if(targetActionObject!=null && targetActionObject.theobject.editoraction.contains("company_fitnessstudio_latpull"))
			doAction_Fitness_Studio_Latpull();

		if(targetActionObject!=null && targetActionObject.theobject.editoraction.contains("company_fitnessstudio_dumbbellrack"))
			doAction_Fitness_Studio_DumbBell();
		
		if(targetActionObject!=null && targetActionObject.theobject.editoraction.contains("company_fitnessstudio_barbell"))
			doAction_Fitness_Studio_BarBell();
	}	
	private void doAction_FitnessTrainer()
	{
		CWorldObject fitnessWorkspace = targetActionObject;
		
		if(actionState==0)
		{
			gotoObject(targetActionObject, true);
			actionState=1;
			return;
		}
		
		if(actionState==1)
		{
			if(gotoObject_Arrived(fitnessWorkspace, (int)town.getSizeValue(150), (int)town.getSizeValue(30), 180, true))
			{
				actionState=2;
				deltaTimer=0;
				deltaTimer2=0;
				actionTemp_Float1=50+rand.nextInt(100);
			}
			
			return;
		}
		
		if(actionState==2)
		{
			cancelIfDark();
			
			//Am Workspace arbeiten
			if(deltaTimer>actionTemp_Float1)
				baseWorldObject.actionanim1=5;
			
			if(deltaTimer>actionTemp_Float1+20)
			{
				deltaTimer=0;
				actionTemp_Float1=50+rand.nextInt(100);
				baseWorldObject.actionanim1=0;
			}
			
			//Wenn Gäste da sind diese boosten
			int i=0;
			if(fitnessWorkspace.belongsToCompany.address_company.listWorldObjects.size()>1)
			{
				while(i<10)
				{
					i++;
					
					int r1 = rand.nextInt(fitnessWorkspace.belongsToCompany.address_company.listWorldObjects.size());
					CWorldObject obj1 = fitnessWorkspace.belongsToCompany.address_company.listWorldObjects.get(r1);
					
					if(obj1.isOccupiedBy==null || obj1.iOccupied1_Arrived==0)
						continue;
					
					if(targetActionObject2!=null && targetActionObject2.uniqueId==obj1.isOccupiedBy.uniqueId)
						continue;
					
					if(obj1.isOccupiedBy.uniqueId!=baseWorldObject.uniqueId)
					{
						targetActionObject2=obj1.isOccupiedBy;
						actionState=3;
						baseWorldObject.actionanim1=0;
						return;
					}
				}
			}
			
			//Pause
			if(deltaTimer2>3600)
			{
				baseWorldObject.actionanim1=0;
				bActionMode=false;
				fitnessWorkspace.isOccupiedBy=null;
				return;
			}
			
			return;
		}
		
		if(actionState==3)
		{
			gotoObject(targetActionObject2, true);
			actionState=4;
			
			return;
		}
		
		if(actionState==4)
		{
			if(baseWorldObject.collides(targetActionObject2, 50))
			{
				baseWorldObject.resetPathFinding();
				baseWorldObject.actionanim1=7;
				actionState=5;
				deltaTimer=0;
				resetRandomActionAnim();
			}
			
			return;
		}
		
		if(actionState==5)
		{
			playRandomActionAnim();
			
			if(deltaTimer>250)
			{
				increaseTaskSkillLevel(targetActionObject);
				baseWorldObject.actionanim1=0;
				targetActionObject2.actionfield1 = baseWorldObject.thehuman.getWorkOutputPerHour(true, targetActionObject, null, false);
				actionState=0;
				resetRandomActionAnim();
				return;
			}
			
			return;
		}
	}
	
	//**********
	//Break Room
	//**********
	private void doAction_Breakroom_Action()
	{
		if(actionMode!=ActionMode.BREAK_ROOM)
			return;
		
		if(targetActionObject==null)
			return;
		
		if(targetActionObject.theobject.editoraction.contains("_billard"))
		{
			doAction_Billard(false);
			return;
		}
		
		if(targetActionObject.theobject.editoraction.contains("_foosball"))
		{
			doAction_Foosball(false);
			return;
		}

		if(targetActionObject.theobject.editoraction.contains("_coffeemachine"))
		{
			doAction_CoffeeMachine();
			return;
		}

		if(targetActionObject.theobject.editoraction.contains("coffeepot"))
		{
			doAction_CleanCup();
			return;
		}

		if(targetActionObject.theobject.editoraction.contains("_fruitplate"))
		{
			doAction_FruitPlate();
			return;
		}
		
		
		return;
	}
	private void doAction_RefillCoffeeMachine()
	{
		CWorldObject fridge = targetActionObject9;
		int moveCoffeeBeansX=62;
		int moveCoffeeBeansY=32;
		
		if(actionState==0)
		{
			//Gehe zum Fridge
			if(fridge.theobject.editoraction.contains("coffeebeans"))
				gotoObject(fridge, true);
			else
				gotoObjectXY(fridge, (int) town.getSizeValue(15), (int) town.getSizeValue(40));
			
			actionState=1;
			return;
		}
		
		if(actionState==1)
		{
			//Am Fridge angekommen
			if(baseWorldObject.ziel_x==-1)
			{
				baseWorldObject.resetPathFinding();
				if(fridge.theobject.editoraction.contains("fridge"))
					baseWorldObject.setRotation(fridge.rotation()+180);
				deltaTimer=0;
				actionState=2;
				fridge.doObjectAction=true;
				baseWorldObject.actionanim1=1;
				actionState=3;
			}
		}
		
		if(actionState==3) 
		{
			//Kaffee rausholen
			int deltavalue=1;
			if(baseWorldObject.thehuman.bIsDark)
				deltavalue=2;
			
			if(deltaTimer>deltavalue)
			{
				if(fridge.theobject.editoraction.contains("coffeebeans"))
				{
					targetActionObject5=fridge;
					gotoObjectXY(targetActionObject, (int) town.getSizeValue(70), (int) town.getSizeValue(-25));
					baseWorldObject.actionanim1=24;
					deltaTimer=0;
					baseWorldObject.actionanim1=0;
					actionState=4;
				}
				else
				{
					if(fridge.getObjectFillingMulti()>0 && baseWorldObject.objectFilling<targetActionObject.getObjectFillingMax())
					{
						deltaTimer=0;
						baseWorldObject.objectFilling++;
						//targetActionObject.objectFilling--;
						fridge.addObjectFillingMulti(0, -1);
						return;
					}
					else
					{
						fridge.doObjectAction=false;
						releaseObject(fridge);
						
						if(baseWorldObject.objectFilling>0)
						{
							CWorldObject coffeebeans=town.gameWorld.gameResourceConfig.createWorldObject("coffeebeans1", baseWorldObject.pos_x(), baseWorldObject.pos_y(), targetActionObject.theaddress);
							town.gameWorld.gameResourceConfig.createWorldObject("recyclingcenter_garbagebag", baseWorldObject.pos_x(), baseWorldObject.pos_y(), targetActionObject.theaddress);
							
							coffeebeans.owner=targetActionObject;
							coffeebeans.isOccupiedBy=baseWorldObject;
							coffeebeans.objectFilling=baseWorldObject.objectFilling;
							targetActionObject5=coffeebeans;
							baseWorldObject.objectFilling=0;
							
							//Gehe zu Coffee Machine
							gotoObjectXY(targetActionObject, (int) town.getSizeValue(70), (int) town.getSizeValue(-25));
							baseWorldObject.actionanim1=24;
							deltaTimer=0;
							baseWorldObject.actionanim1=0;
							actionState=4;
						}
						else
						{
							baseWorldObject.actionanim1=0;
							bActionMode=false;
							targetActionObject.isOccupiedBy=null;
							targetActionObject9=null; //fridge auf null setzen
						}
					}
				}
			}
			
			return;
		}
		
		if(actionState==4)
		{
			baseWorldObject.actionanim1=2;
			
			//An der Machine
			if(baseWorldObject.ziel_x==-1)
			{
				baseWorldObject.resetPathFinding();
				moveHumanByObjectRotation(baseWorldObject, targetActionObject, (int)town.getSizeValue(70), (int)town.getSizeValue(-25), targetActionObject.rotation()+180);
				deltaTimer=0;
				baseWorldObject.actionanim1=1;
				actionState=5;
			}
			
			//Trage Coffee Beans
			moveObjectByHumanRotation(baseWorldObject, targetActionObject5, (int)town.getSizeValue(moveCoffeeBeansX), (int)town.getSizeValue(moveCoffeeBeansY), baseWorldObject.rotation());
			
			return;
		}
		
		if(actionState==5)
		{
			//Befülle Machine
			if(deltaTimer>50)
			{
				targetActionObject.objectFilling=targetActionObject5.objectFilling;
				town.gameGui.removeWorldObject(targetActionObject5, false);
				actionState=6;
			}
			
			return;
		}
		
		if(actionState==6)
		{
			//Wieder auf Drink Coffee Action umschalten
			actionState=0;
			//targetActionObject4=null;
			targetActionObject9=null; //fridge auf null setzen
			return;
		}
		
	}
	private void doAction_CleanCup()
	{
		//targetActionObject : cup
		//targetActionObject.owner : coffee machine
		//targetActionObject3 : sink
		
		if(actionState>=2)
		{
			//Trage Kaffeetasse
			int moveCupX=62;
			int moveCupY=32;
			int cupRotation=200;
			moveObjectByHumanRotation(baseWorldObject, targetActionObject, (int)town.getSizeValue(moveCupX), (int)town.getSizeValue(moveCupY), baseWorldObject.rotation()+cupRotation);
		}
		
		if(actionState==0)
		{
			//Gibt es Sink? -> Abwaschen und Tasse zurückbringen
			targetActionObject3=null;
			for(CWorldObject wobj : targetActionObject.owner.theaddress.listWorldObjects)
			{
				if(!wobj.theobject.editoraction.contains("kitchen_sink"))
					continue;
				if(!wobj.isActiveByEnergyConsumption())
					continue;
				if(!wobj.isActiveByWaterConsumption())
					continue;
				
				targetActionObject3=wobj;
				
				if(wobj.isOccupiedBy!=null)
					continue;
				
				break;
			}
			
			if(targetActionObject3!=null)
			{
				//Zu Tasse gehen
				gotoObject(targetActionObject, true);
				targetActionObject3.isOccupiedBy=baseWorldObject;
				actionState=1;
			}
			else
			{
				//Tasse zu Coffee Machine zurückbringen
				actionState=1;
				int distance1 = CHelper.getEuclidianDistance(targetActionObject, targetActionObject.owner);
				if(distance1<targetActionObject.owner.width/2)
				{
					bActionMode=false;
					if(targetActionObject!=null)
						targetActionObject.isOccupiedBy=null;
				}
			}
			
			return;		
		}

		if(actionState==1)
		{
			//An Tasse angekommen
			if(baseWorldObject.ziel_x==-1)
			{
				baseWorldObject.resetPathFinding();
				
				//Zu Sink gehen, sonst zu Coffeemachine
				if(targetActionObject3!=null)
				{
					gotoObjectXY(targetActionObject3, (int) town.getSizeValue(54), (int) town.getSizeValue(-5));
					actionState=2;
				}
				else
				{
					gotoObjectXY(targetActionObject.owner, (int) town.getSizeValue(230), (int) town.getSizeValue(30));
					actionState=4;
				}
				
				baseWorldObject.actionanim1=24;
			}
			
			return;
		}
		
		if(actionState==2)
		{
			//Am Sink angekommen
			if(gotoObject_Arrived(targetActionObject3, (int) town.getSizeValue(54), (int) town.getSizeValue(-5), targetActionObject3.rotation()+180, true))
			{
				actionState=3;
				deltaTimer=0;
				baseWorldObject.actionanim1=1;
			}
			
			return;
		}
		
		if(actionState==3)
		{
			//Tasse abspülen und zurückbringen
			if(deltaTimer>50)
			{
				targetActionObject3.isOccupiedBy=null;
				baseWorldObject.actionanim1=24;
				targetActionObject.objectFilling=0;
				gotoObjectXY(targetActionObject.owner, (int) town.getSizeValue(230), (int) town.getSizeValue(30));
				actionState=4;
			}
			
			return;
		}
		
		if(actionState==4)
		{
			if(baseWorldObject.ziel_x==-1)
			{
				baseWorldObject.resetPathFinding();
				
				//Saubere Tasse wieder reinstellen				
				if(targetActionObject3!=null)
				{
					targetActionObject.owner.objectFilling2++;
					
					if(targetActionObject.owner.objectFilling2 > targetActionObject.owner.getObjectFillingMax2())
						targetActionObject.owner.objectFilling2 = targetActionObject.owner.getObjectFillingMax2();
					
					actionState=5;
				}
				else
				{
					actionState=5;
				}
			}
			
			return;
		}
		
		if(actionState==5)
		{
			if(targetActionObject3!=null)
			{
				//Saubere Tasse wieder auf Tablett
				town.gameGui.removeWorldObject(targetActionObject, false);
			}
			else
			{
				//Schmutz Tasse oben drauf stellen, Tassenowner ist coffeemachine
				int moveCupX=targetActionObject.owner.width/2+Math.abs(rand.nextInt(targetActionObject.owner.width/2-(int)town.getSizeValue(20)));
				int moveCupY=30+rand.nextInt(targetActionObject.owner.height-(int)town.getSizeValue(60));
				int cupRotation=rand.nextInt(360);
				moveObjectByObjectRotation(targetActionObject.owner, targetActionObject, moveCupX, moveCupY, cupRotation);
				targetActionObject.isOccupiedBy=null;
			}
			
			bActionMode=false;
			return;
		}
		
	}
	private void doAction_CoffeeMachine()
	{
		CWorldObject coffeemachine = targetActionObject;
		CWorldObject fridge = targetActionObject9;
		//targetActionObject2=Coffee Cup
		//targetActionObject3=sink
		
		int moveCupX=(int)town.getSizeValue(62);
		int moveCupY=(int)town.getSizeValue(32);
		int drinkCupX=(int)town.getSizeValue(53);
		int drinkCupY=(int)town.getSizeValue(38);
		int cupRotation=200;
		
		//Kaffeemachine auffüllen
		if(fridge!=null)
		{
			doAction_RefillCoffeeMachine();
			return;
		}
		releaseObject(targetActionObject9);
		
		if(actionState>=3 && actionState!=5)
		{
			//Trage Kaffeetasse
			moveObjectByHumanRotation(baseWorldObject, targetActionObject2, moveCupX, moveCupY, baseWorldObject.rotation()+cupRotation);
		}
		
		if(actionState==0)
		{
			//Gehe zu Kaffeemachine
			gotoObjectXY(coffeemachine, (int) town.getSizeValue(230), (int) town.getSizeValue(30));
			actionState=1;
			return;
		}
		
		if(actionState==1)
		{
			//Hole Tasse
			if(baseWorldObject.ziel_x==-1)
			{
				baseWorldObject.resetPathFinding();
				baseWorldObject.setRotation(coffeemachine.rotation()+180);
				deltaTimer=0;
				actionState=2;
				baseWorldObject.actionanim1=24;
			}
			
			return;
		}
		
		if(actionState==2)
		{
			if(deltaTimer>30)
			{
				//Gehe rüber zu Machine
				gotoObjectXY(coffeemachine, (int) town.getSizeValue(70), (int) town.getSizeValue(25));
				actionState=3;
				
				//Nimm Tasse
				coffeemachine.objectFilling2--;
				if(coffeemachine.objectFilling2<0)
					coffeemachine.objectFilling2=0;
				
				CWorldObject cup1 = town.gameWorld.gameResourceConfig.createWorldObject("coffeepot1", baseWorldObject.pos_x(), baseWorldObject.pos_y(), coffeemachine.theaddress);
				cup1.owner = coffeemachine;
				cup1.isOccupiedBy=baseWorldObject;
				cup1.objectFilling=0;
				targetActionObject2=cup1;
			}
			
			return;
		}
		
		if(actionState==3)
		{
			//An der Machine
			if(baseWorldObject.ziel_x==-1)
			{
				baseWorldObject.resetPathFinding();
				baseWorldObject.setRotation(coffeemachine.rotation()+180);
				deltaTimer=0;
				actionState=4;
			}
			
			return;
		}
		
		if(actionState==4)
		{
			//Kaffee einfüllen
			if(deltaTimer>30)
				targetActionObject2.objectFilling=1;

			if(deltaTimer>40)
				targetActionObject2.objectFilling=2;

			if(deltaTimer>50)
				targetActionObject2.objectFilling=3;

			if(deltaTimer>60)
				targetActionObject2.objectFilling=4;

			if(deltaTimer>70)
				targetActionObject2.objectFilling=5;
			
			if(deltaTimer>90)
			{
				coffeemachine.isOccupiedBy=null;
				coffeemachine.objectFilling--;
				
				if(coffeemachine.objectFilling<0)
					coffeemachine.objectFilling=0;
				
				//Gehe zu Stehtisch wenn vorhanden
				//Sonst stelle dich irgendwo anders hin
				gotoXY(baseWorldObject.pos_x()+rand.nextInt((int)town.getSizeValue(300))-rand.nextInt((int)town.getSizeValue(300)), baseWorldObject.pos_y()+rand.nextInt((int)town.getSizeValue(300))-rand.nextInt((int)town.getSizeValue(300)));
				deltaTimer=0;
				actionState=5;
			}
			
			return;
		}
		
		if(actionState==5)
		{
			//Kaffee trinken
			
			if(deltaTimer<=100)
			{
				moveObjectByHumanRotation(baseWorldObject, targetActionObject2, moveCupX, moveCupY, baseWorldObject.rotation()+cupRotation);
			}
			else if(deltaTimer>100 && deltaTimer<=140 && targetActionObject2.actionfield1==0)
			{
				baseWorldObject.actionanim1=25;
				targetActionObject2.actionfield1=1; //zoom tasse
				
				moveObjectByHumanRotation(baseWorldObject, targetActionObject2, drinkCupX, drinkCupY, baseWorldObject.rotation()+cupRotation);
			}
			else if(deltaTimer>140 && deltaTimer<=200 && targetActionObject2.actionfield1==1)
			{
				baseWorldObject.actionanim1=24;
				targetActionObject2.actionfield1=0; //zoom tasse
				
				moveObjectByHumanRotation(baseWorldObject, targetActionObject2, moveCupX, moveCupY, baseWorldObject.rotation()+cupRotation);
				
				targetActionObject2.objectFilling--;
				
				if(targetActionObject2.objectFilling==1)
				{
					//parentWorldObject.actionanim1=0;
					handleActionValueBonus(1);
					actionState=6;
					
					return;
				}				
			}
			else if(deltaTimer>200)
			{
				moveObjectByHumanRotation(baseWorldObject, targetActionObject2, moveCupX, moveCupY, baseWorldObject.rotation()+cupRotation);
				
				gotoXY(baseWorldObject.pos_x()+rand.nextInt((int)town.getSizeValue(300))-rand.nextInt((int)town.getSizeValue(300)), baseWorldObject.pos_y()+rand.nextInt((int)town.getSizeValue(300))-rand.nextInt((int)town.getSizeValue(300)));
				deltaTimer=0;
			}
			
			return;
		}
		
		if(actionState==6)
		{
			//Trigger Clean Cup Action
			targetActionObject=targetActionObject2;
			actionState=0;
			
			return;
		}
	}
	private void doAction_FruitPlate()
	{
		CWorldObject fruitplate = targetActionObject;
		CWorldObject fridge = targetActionObject9;
		
		//Fruitplate auffüllen
		if(doAction_RefillObject(fruitplate, fridge, true))
			return;
		releaseObject(targetActionObject9);
		
		//Fruit essen
		if(actionState==0)
		{
			if(fruitplate.objectFilling<1)
			{
				bActionMode=false;
				releaseObject(fruitplate);
				return;
			}
			
			//Gehe zu Fruitplate
			gotoObject(fruitplate, true);
			actionState=1;
			return;
		}
		
		if(actionState==1)
		{
			//Am Fruitplate angekommen
			//if(gotoObject_Arrived(fruitplate, 0))
			if(baseWorldObject.collides(fruitplate,(int)town.getSizeValue(-15)) || baseWorldObject.ziel_x==-1)
			{
				baseWorldObject.resetPathFinding();
				deltaTimer=0;
				deltaTimer2=0;
				
				baseWorldObject.actionanim1=15; //
				
				fruitplate.objectFilling-=1;
				if(fruitplate.objectFilling<0)
					fruitplate.objectFilling=0;
				actionState=2;
			}
			
			return;
		}
		
		if(actionState==2)
		{
			if(deltaTimer2>10)
			{
				if(baseWorldObject.actionanim1==20)
					baseWorldObject.actionanim1=15;
				else
					baseWorldObject.actionanim1=20;
				
				deltaTimer2=0;
			}
			
			if(deltaTimer>300)
			{
				handleActionValueBonus(1);
				Random rand = baseWorldObject.town.gameWorld.rand;
				town.gameWorld.gameResourceConfig.createWorldObject("recyclingcenter_garbagebag", baseWorldObject.pos_x(), baseWorldObject.pos_y(), fruitplate.theaddress);
				baseWorldObject.actionanim1=0;
				bActionMode=false;
				releaseObject(fruitplate);
				
				baseWorldObject.action_takeoutgarbage.initAction(town.gameWorld.stateTime);
				baseWorldObject.activeAction=baseWorldObject.action_takeoutgarbage;
			}
			
			return;
		}
	}
	
	//****
	//Pub
	//****
	private void doAction_PUB_Bar(int seatType)
	{
		int movx=0;
		int movy=0;
		float rot=targetActionObject.rotation();
		Vector2 v2 = null;
		Vector3 v3 = null;
		
		if(actionState==0 || actionState==1)
		{
			int nr = targetActionObject.isOccupiedByMe(baseWorldObject);
			v3 = getBarSeatPosition((int)nr, 1, seatType, town);
			movx=(int)v3.x;
			movy=(int)v3.y;
			rot+=v3.z;
			
			v2 = CHelper.moveHumanByObjectRotation(targetActionObject, baseWorldObject, (int)v3.x, (int)v3.y);
		}
		
		if(actionState==0)
		{
			actionDuration=1500;
			
			//Zur Bar
			gotoObject(targetActionObject, true, (int)v2.x, (int)v2.y);
			actionState=1;
			return;
		}
		
		if(actionState==1)
		{
			//An der Bar angekommen
			if(gotoObject_Arrived(targetActionObject, movx, movy, rot, false))
			{
				actionState=2;
				deltaTimer=0;
			}
			
			return;
		}
		
		if(actionState==2)
		{
			//Was trinken, wenn was da steht
			int nr = targetActionObject.isOccupiedByMe(baseWorldObject);
			float av = targetActionObject.actionVarByNr(nr);
			
			//Achtung: Reihenfolge der Ifs ist hier wichtig für Speed X4 da hier 70 und 200 im gleichen frame sind
			if(baseWorldObject.actionanim1>0 && deltaTimer>70 && (av/10)>1)
			{
				baseWorldObject.actionanim1=0;
				targetActionObject.actionVarByNr(nr, Math.round(av/10)-1);
				deltaTimer=0;
				
				if(Math.round(av/10)==2)
				{
					handleActionValueBonus(1);
					
					//int nexti = rand.nextInt(Math.round(parentWorldObject.thehuman.healthAttitude*10));
					//if(nexti>2)
					{
						bActionMode=false;
						targetActionObject.clearOccupied(baseWorldObject);
						baseWorldObject.clearActionVariables();
						return;
					}
				}
				
				return;
			}
			
			float timelapse=300+baseWorldObject.thehuman.healthAttitude*20;
			
			if((av>1 && av<7  && deltaTimer>timelapse) || (av==7  && deltaTimer>(timelapse*10)))
			{
				baseWorldObject.actionanim1=6;
				targetActionObject.actionVarByNr(nr, av*10);
				deltaTimer=0;
				return;
			}
			
			return;
		}
	}
	private void doAction_PUB_Pinball(int type)
	{
		if(actionState==0)
		{
			gotoObject(targetActionObject, true);
			actionState=1;
			return;
		}
		
		if(actionState==1)
		{
			if(gotoObject_Arrived(targetActionObject, (int)town.getSizeValue(65), (int)town.getSizeValue(-30), targetActionObject.rotation()+180, true))
			{
				//targetActionObject.gameWorld.changeTownMoney(Math.round(-5));
				//gameWorld.townStatistics.getCurrentStatistics_Finance().residentSpendsMoney+=5;
				//targetActionObject.addAnimationEvent(AnimationEventType.MONEY, Math.round(-5));
				deltaTimer=0;
				baseWorldObject.actionanim1=7;
				if(type==2)
					baseWorldObject.actionanim1=1;
				targetActionObject.actionanim1=101;
				actionState=2;
			}
			
			return; 
		}
		
		if(actionState==2)
		{
			if(deltaTimer>1000)
			{
				handleActionValueBonus(1);
				targetActionObject.isOccupiedBy=null;
				targetActionObject.actionanim1=0;
				baseWorldObject.actionanim1=0;
				bActionMode=false;
			}
			
			return;
		}
	}
	private void doAction_Billard(Boolean bpay)
	{
		//Gehe zum Objekt
		if(actionState==0)
		{
			baseWorldObject.actionanim1=0;
			gotoObject(targetActionObject, true);
			actionState=1;
			return;
		}
		
		//Angekommen
		if(actionState==1)
		{
			int movx = 0;
			int movy = 0;
			float rot = 0;
			
			if(isOccupiedByMe(targetActionObject, 1))
			{
				rot=targetActionObject.rotation();
				movx+=100;
				movy=220;
			}
			else if(isOccupiedByMe(targetActionObject, 2))
			{
				rot=targetActionObject.rotation()+180;
				movx+=100;
				movy=-30;
			}
			
			//if(gotoObject_Arrived(targetActionObject, movx, movy, rot, true))
			if(baseWorldObject.ziel_x==-1 || baseWorldObject.collides(targetActionObject))
			{
				baseWorldObject.resetPathFinding();
				baseWorldObject.walkAround(true);
				actionState=2;
				deltaTimer=0;
				deltaTimer3=0;
			}
			
			return;
		}
		
		//Geld bezahlen / spielen wenn 2 Spieler vollzählig
		if(actionState==2)
		{
			if(baseWorldObject.thehuman.bIsDark)
			{
				iActionBlocker=1500;
				return;
			}
			
			if(targetActionObject.isOccupiedBy!=null && targetActionObject.isOccupiedBy2!=null)
			{
				if(isOccupiedByMe(targetActionObject, 1))
				{
					if(targetActionObject.actionfield2==50)
					{
						targetActionObject.actionfield2=101;
						if(bpay)
						{
							//targetActionObject.gameWorld.changeTownMoney(Math.round(-5));
							//gameWorld.townStatistics.getCurrentStatistics_Finance().residentSpendsMoney+=5;
							//targetActionObject.addAnimationEvent(AnimationEventType.MONEY, Math.round(-5));
						}
						deltaTimer3=0;
					}
				}
				
				if(isOccupiedByMe(targetActionObject, 2))
					if(targetActionObject.actionfield2==0)
						targetActionObject.actionfield2=50;
				
				
				//Übergeben an Mitspieler
				if(deltaTimer3>50)
				{
					if(targetActionObject.actionfield2==101)
					{
						if(isOccupiedByMe(targetActionObject, 1))
						{
							if(baseWorldObject.actionfield1==4 || (baseWorldObject.actionfield1==0 && targetActionObject.isOccupiedBy2.actionfield1==0))
							{
								baseWorldObject.actionfield1=0;
								baseWorldObject.actionanim1=0;
								baseWorldObject.walkAround(true);
								
								targetActionObject.isOccupiedBy2.actionfield1=1;
								targetActionObject.isOccupiedBy2.actionanim1=5;
							}
						}
						
						if(isOccupiedByMe(targetActionObject, 2))
						{
							if(baseWorldObject.actionfield1==4 || (baseWorldObject.actionfield1==0 && targetActionObject.isOccupiedBy.actionfield1==0))
							{
								baseWorldObject.actionfield1=0;
								baseWorldObject.actionanim1=0;
								baseWorldObject.walkAround(true);
								
								targetActionObject.isOccupiedBy.actionfield1=1;
								targetActionObject.isOccupiedBy.actionanim1=5;
							}
						}
					}
				}
			}
			
			if(baseWorldObject.actionfield1==1)
			{
				actionState=3;
				deltaTimer3=0;
			}
			
			if(deltaTimer>1500)
			{
				handleActionValueBonus(1);
				bActionMode=false;
				targetActionObject.actionfield1=0;
				baseWorldObject.actionfield1=0;
				targetActionObject.actionfield2=0;
				releaseObject(targetActionObject);
			}
			
			return;
		}
		
		//Stoßen vorbereiten
		if(actionState==3)
		{
			if(baseWorldObject.actionfield1==1) //wird von anderem Spieler gesetzt
			{
				if(deltaTimer3>50)	
				{
					int movx = 0;
					int movy = 0;
					float rot=0;
					
					int seite=rand.nextInt(3+1);
					if(targetActionObject.actionfield1==0)
						seite=2;
										
					Vector2 v2 = null;
					
					if(seite==0) //oben
					{
						actionTemp_Float3 = targetActionObject.rotation();
						
						movx=-100;
						movy=120;
												
						actionTemp_Float1=80+rand.nextInt(210);
						actionTemp_Float2=220;
					}
					
					if(seite==1) //unten
					{
						actionTemp_Float3 = targetActionObject.rotation()+180;
						
						movx=+450;
						movy=100;
						
						actionTemp_Float1=80+rand.nextInt(210);
						actionTemp_Float2=-30;
					}
					
					if(seite==2) //links
					{
						actionTemp_Float3 = targetActionObject.rotation()+90;
						
						movx=-100;
						movy=100;
						
						actionTemp_Float1=-100;
						actionTemp_Float2=60+rand.nextInt(80);
					}
					
					if(seite==3) //rechts
					{
						actionTemp_Float3 = targetActionObject.rotation()+270;
						
						movx=+450;
						movy=100;
						
						actionTemp_Float1=400;
						actionTemp_Float2=60+rand.nextInt(80);
					}
					
					if(targetActionObject.actionfield1==0)
						actionTemp_Float2=110;
					
					v2 = CHelper.moveHumanByObjectRotation(targetActionObject, baseWorldObject, (int)town.getSizeValue(movx), (int)town.getSizeValue(movy));
					
					gotoObject(null, true, (int)v2.x, (int)v2.y);
					
					
					actionState=4;
					deltaTimer3=0;
				}
			}
			
			return;
		}
		
		if(actionState==4)
		{
			//Stoßen vorbereiten 2
			if(baseWorldObject.ziel_x==-1)
			{
				baseWorldObject.resetPathFinding();
				Vector2 v2 = CHelper.moveHumanByObjectRotation(targetActionObject, baseWorldObject, (int)town.getSizeValue(actionTemp_Float1), (int)town.getSizeValue(actionTemp_Float2));
				gotoObject(null, true, (int)v2.x, (int)v2.y);
				
				deltaTimer3=0;
				actionState=5;
			}
			
			return;
		}
		
		
		if(actionState==5)
		{
			//Stoßen vorbereiten 3
			if(baseWorldObject.ziel_x==-1)
			{
				baseWorldObject.resetPathFinding();
				baseWorldObject.setRotation(actionTemp_Float3);
				Vector2 v2 = CHelper.moveHumanByObjectRotation(targetActionObject, baseWorldObject, (int)town.getSizeValue(actionTemp_Float1), (int)town.getSizeValue(actionTemp_Float2));
				baseWorldObject.setPosition((int)v2.x, (int)v2.y);
				actionState=6;
				deltaTimer3=0;
			}			
		}
		
		
		if(actionState==6)
		{
			//Stoßen
			if(deltaTimer3>50)
			{
				baseWorldObject.actionanim1=5;
				baseWorldObject.actionfield1=4;
				
				targetActionObject.actionfield1++;
				
				if(targetActionObject.actionfield1>4)
					targetActionObject.actionfield1=1;
				
				actionState=2;
				
				deltaTimer3=0;
			}
			
			return;
		}
		
	}
	private void doAction_Foosball(Boolean bpay)
	{
		if(actionState==0)
		{
			gotoObject(targetActionObject, true);
			if(targetActionObject==null)
				return;
			actionState=1;
			targetActionObject.actionanim1=0;
			return;
		}
		
		if(actionState==1)
		{
			int movx = 0;
			int movy = 0;
			float rot=0;
			
			if(isOccupiedByMe(targetActionObject, 1))
			{
				rot=targetActionObject.rotation();
				movx+=100;
				movy=220;
			}
			else if(isOccupiedByMe(targetActionObject, 2))
			{
				rot=targetActionObject.rotation()+180;
				movx+=100;
				movy=-30;
			}
			
			if(gotoObject_Arrived(targetActionObject, (int)town.getSizeValue(movx), (int)town.getSizeValue(movy), rot, true))
			{
				actionState=2;
				deltaTimer=0;
				deltaTimer2=0;
			}
			
			return;
		}
		
		if(actionState==2)
		{
			if(baseWorldObject.thehuman.bIsDark)
			{
				iActionBlocker=1500;
				return;
			}
			
			baseWorldObject.actionanim1=0;
			baseWorldObject.actionanim2=0;
			if(targetActionObject.isOccupiedBy!=null && targetActionObject.isOccupiedBy2!=null)
			{
				if(isOccupiedByMe(targetActionObject, 1))
				{
					if(targetActionObject.actionanim1==50)
					{
						targetActionObject.actionanim1=101;
						
						if(bpay)
						{
							//targetActionObject.gameWorld.changeTownMoney(Math.round(-5));
							//gameWorld.townStatistics.getCurrentStatistics_Finance().residentSpendsMoney+=5;
							//targetActionObject.addAnimationEvent(AnimationEventType.MONEY, Math.round(-5));
						}
					}
				}
				
				if(isOccupiedByMe(targetActionObject, 2))
					if(targetActionObject.actionanim1==0)
						targetActionObject.actionanim1=50;
				
				targetActionObject.theobject.objectAnimation.setFrameDuration(0.04f);
				
				if(deltaTimer2>2 && targetActionObject.actionanim1==101)
				{
					baseWorldObject.actionanim1=1;
					baseWorldObject.actionanim2=5;
					
					int movx = 0;
					int movy = 0;
					
					if(isOccupiedByMe(targetActionObject, 1))
					{
						movx+=100;
						movy=220;
					}
					else if(isOccupiedByMe(targetActionObject, 2))
					{
						movx+=100;
						movy=-30;
					}					
										
					Vector2 v2 = CHelper.moveHumanByObjectRotation(targetActionObject, baseWorldObject, (int)town.getSizeValue(movx+rand.nextInt(80)-rand.nextInt(80)), (int)town.getSizeValue(movy));
					baseWorldObject.setPosition((int)v2.x, (int)v2.y);
					
					deltaTimer2=0;
				}
			}
			else
			{
				targetActionObject.actionanim1=0;
				targetActionObject.actionanim2=0;
			}
									
			if(deltaTimer>1200)
			{
				handleActionValueBonus(1);
				bActionMode=false;
				baseWorldObject.actionanim1=0;
				baseWorldObject.actionanim2=0;
				targetActionObject.actionfield1=0;
				releaseObject(targetActionObject);
			}
		}
		
	}
	private void doAction_PUB_Action()
	{
		if(actionMode!=ActionMode.PUB_ACTION)
			return;
		
		if(targetActionObject==null)
			return;
		
		if(!targetActionObject.belongsToCompany.isActive(0))
			cancelAction("");
		
		if(targetActionObject.theobject.editoraction.contains("company_pub_workingplace_bar"))
			doAction_PUB_Bar(1);
		
		if(targetActionObject.theobject.editoraction.contains("company_pub_table"))
			doAction_PUB_Bar(2);
		
		if(targetActionObject.theobject.editoraction.contains("company_pub_billard"))
			doAction_Billard(true);
		
		if(targetActionObject.theobject.editoraction.contains("company_pub_foosball"))
			doAction_Foosball(true);
		
		if(targetActionObject.theobject.editoraction.contains("company_pub_pinball"))
			doAction_PUB_Pinball(1);
		
		if(targetActionObject.theobject.editoraction.contains("company_pub_arcademachine"))
			doAction_PUB_Pinball(2);
		
		return;
	}
	public static Vector3 getBarSeatPosition(int nr, int type, int seatType, CTown town)
	{
		Vector3 v3= new Vector3();
		
		//type=1: seat
		//type=2: glass
		//type=3: barkeeper
		
		//seattype=1: bar
		//seattype=2: table
				
		if(seatType==2) //table
		{
			if(nr==1)
			{
				v3.x=90;
				v3.y=260;
				v3.z=45;
				if(type==2)
				{
					v3.x=100;
					v3.y=200;
				}
			}
			
			if(nr==2)
			{
				v3.x=248;
				v3.y=248;
				v3.z=305;
				if(type==2)
				{
					v3.x=180;
					v3.y=180;
				}

			}
			
			if(nr==3)
			{
				v3.x=80;
				v3.y=105;
				v3.z=120;
				if(type==2)
				{
				}
			}
			
			if(nr==4)
			{
				v3.x=240;
				v3.y=90;
				v3.z=220;
				if(type==2)
				{
					v3.x=160;
					v3.y=90;
				}
			}
		}
		
		if(seatType==1) //bar
		{
			if(nr==1)
			{
				v3.x=50;
				v3.y=260;
				v3.z=45;
				if(type==2)
				{
					v3.y-=60;
					v3.x+=20;
				}
				if(type==3)
				{
					v3.y-=110;
					v3.x+=150;
				}
			}
			
			if(nr==2)
			{
				v3.x=190;
				v3.y=350;
				if(type==2)
				{
					v3.y-=80;
					v3.x-=25;
				}
				if(type==3)
				{
					v3.y-=110;
					v3.x+=100;
				}
			}
			
			if(nr==3)
			{
				v3.x=290;
				v3.y=350;
				if(type==2)
				{
					v3.y-=80;
					v3.x-=25;
				}
				if(type==3)
				{
					v3.y-=110;
					v3.x+=50;
				}
			}
			
			if(nr==4)
			{
				v3.x=390;
				v3.y=350;
				if(type==2)
				{
					v3.y-=80;
					v3.x-=30;
				}
				if(type==3)
				{
					v3.y-=120;
					v3.x-=70;
				}
			}
			
			if(nr==5)
			{
				v3.x=490;
				v3.y=350;
				if(type==2)
				{
					v3.y-=80;
					v3.x-=30;
				}
				if(type==3)
				{
					v3.y-=120;
					v3.x-=70;
				}
			}
			
			if(nr==6)
			{
				v3.x=550;
				v3.y=210;
				v3.z=270;
				if(type==2)
				{
					v3.x-=80;
					v3.y-=20;
				}
				if(type==3)
				{
					v3.x-=90;
					v3.y-=30;
				}
			}
	
			if(nr==7)
			{
				v3.x=550;
				v3.y=100;
				v3.z=270;
				if(type==2)
				{
					v3.x-=80;
					v3.y-=20;
				}
				if(type==3)
				{
					v3.x-=90;
					v3.y-=30;
				}
			}
		}
		
		v3.x=(int)town.getSizeValue(v3.x);
		v3.y=(int)town.getSizeValue(v3.y);
		
		return v3;
	}
	private int getNoOrEmptyBeerCount(CWorldObject wobj)
	{
		int count=0;
		
		//Kein Getränk?
		if(wobj.isOccupiedBy!=null && wobj.actionvar1==0 && wobj.isOccupiedBy.activeAction.actionState>1)
			count++;
		if(wobj.isOccupiedBy2!=null && wobj.actionvar2==0 && wobj.isOccupiedBy2.activeAction.actionState>1)
			count++;
		if(wobj.isOccupiedBy3!=null && wobj.actionvar3==0 && wobj.isOccupiedBy3.activeAction.actionState>1)
			count++;
		if(wobj.isOccupiedBy4!=null && wobj.actionvar4==0 && wobj.isOccupiedBy4.activeAction.actionState>1)
			count++;
		if(wobj.isOccupiedBy5!=null && wobj.actionvar5==0 && wobj.isOccupiedBy5.activeAction.actionState>1)
			count++;
		if(wobj.isOccupiedBy6!=null && wobj.actionvar6==0 && wobj.isOccupiedBy6.activeAction.actionState>1)
			count++;
		if(wobj.isOccupiedBy7!=null && wobj.actionvar7==0 && wobj.isOccupiedBy7.activeAction.actionState>1)
			count++;
		
		//Leeres Glas?
		if(wobj.actionvar1==1)
			count++;
		if(wobj.actionvar2==1)
			count++;
		if(wobj.actionvar3==1)
			count++;
		if(wobj.actionvar4==1)
			count++;
		if(wobj.actionvar5==1)
			count++;
		if(wobj.actionvar6==1)
			count++;
		if(wobj.actionvar7==1)
			count++;		
		
		return count;
	}
	private void doAction_PUB_Barkeeper()
	{
		CWorldObject bar = targetActionObject;
		
		//An den Tresen gehen Step 1
		if(actionState==0)
		{
			CWorldObject table = null;
			baseWorldObject.actionfield1=0;
			Vector2 v2 = CHelper.moveHumanByObjectRotation(bar, baseWorldObject, (int)town.getSizeValue(-100), (int)town.getSizeValue(-200));
			gotoObject(targetActionObject, true, (int)v2.x, (int)v2.y);
			actionState=1;
			return;
		}
		
		//An den Tresen gehen Step 2
		if(actionState==1)
		{
			if(baseWorldObject.ziel_x==-1)
			{
				Vector2 v2 = CHelper.moveHumanByObjectRotation(bar, baseWorldObject, bar.width/2+(int)town.getSizeValue(50), bar.height/2+(int)town.getSizeValue(40));
				gotoObject(bar, true, (int)v2.x, (int)v2.y);
				actionState=2;
				return;
			}
		}
		
		if(actionState==2)
		{
			int movx = 295;
			int movy = 128;
			
			//if(gotoObject_Arrived(bar, movx, movy, bar.rotation()+180, true))
			if(baseWorldObject.ziel_x==-1)
			{
				actionState=3;
				deltaTimer=0;
				//targetActionObject.actionvar1=1;
				return;
			}
		}
		
		
		//Getränk Hinstellen oder abräumen
		//	actionTemp_Float1: Getränk hinstellen
		//	actionTemp_Float2: abräumen
		int movx=0;
		int movy=0;
		float rot=0;
		
		if(actionState==3)
		{
			//Find Table or Bar
			int count = 0;
			for(CWorldObject nextTable :  bar.belongsToCompany.address_company.listWorldObjects)
			{
				if(nextTable.theobject.editoraction.contains("company_pub_table")
						|| nextTable.theobject.editoraction.contains("company_pub_workingplace_bar"))
				{
					//ist ein occupied ohne bier oder ist ein leeres glas am start?
					int count2 = getNoOrEmptyBeerCount(nextTable);
					if(nextTable.isOccupiedByExtern!=null)
						continue;
					
					if(count2>count)
					{
						targetActionObject2=nextTable;
						
						if(targetActionObject2.theobject.editoraction.contains("company_pub_table"))
							targetActionObject2.isOccupiedByExtern=baseWorldObject;
						
						count=count2;
					}
				}
			}
			
			deltaTimer=0;
			
			
			
			if(targetActionObject2==null)
				return;
				
			
			
			//Neues Getränk bringen
			actionTemp_Float3=0;
			if(targetActionObject2.isOccupiedBy!=null && targetActionObject2.actionvar1==0 && targetActionObject2.isOccupiedBy.activeAction.actionState>1)
			{
				actionTemp_Float1=1;
				actionTemp_Float3++;
			}
			if(targetActionObject2.isOccupiedBy2!=null && targetActionObject2.actionvar2==0 && targetActionObject2.isOccupiedBy2.activeAction.actionState>1)
			{
				actionTemp_Float1=2;
				actionTemp_Float3++;
			}
			if(targetActionObject2.isOccupiedBy3!=null && targetActionObject2.actionvar3==0 && targetActionObject2.isOccupiedBy3.activeAction.actionState>1)
			{
				actionTemp_Float1=3;
				actionTemp_Float3++;
			}
			if(targetActionObject2.isOccupiedBy4!=null && targetActionObject2.actionvar4==0 && targetActionObject2.isOccupiedBy4.activeAction.actionState>1)
			{
				actionTemp_Float1=4;
				actionTemp_Float3++;
			}
			if(targetActionObject2.isOccupiedBy5!=null && targetActionObject2.actionvar5==0 && targetActionObject2.isOccupiedBy5.activeAction.actionState>1)
			{
				actionTemp_Float1=5;
				actionTemp_Float3++;
			}
			if(targetActionObject2.isOccupiedBy6!=null && targetActionObject2.actionvar6==0 && targetActionObject2.isOccupiedBy6.activeAction.actionState>1)
			{
				actionTemp_Float1=6;
				actionTemp_Float3++;
			}
			if(targetActionObject2.isOccupiedBy7!=null && targetActionObject2.actionvar7==0 && targetActionObject2.isOccupiedBy7.activeAction.actionState>1)
			{
				actionTemp_Float1=7;
				actionTemp_Float3++;
			}
			
			
			if(actionTemp_Float1==0)
			{
				//Abräumen?
				if(targetActionObject2.actionvar1==1)
				{
					actionTemp_Float2 = 1;
					actionTemp_Float3++;
				}
				if(targetActionObject2.actionvar2==1)
				{
					actionTemp_Float2 = 2;
					actionTemp_Float3++;
				}
				if(targetActionObject2.actionvar3==1)
				{
					actionTemp_Float2 = 3;
					actionTemp_Float3++;
				}
				if(targetActionObject2.actionvar4==1)
				{
					actionTemp_Float2 = 4;
					actionTemp_Float3++;
				}
				if(targetActionObject2.actionvar5==1)
				{
					actionTemp_Float2 = 5;
					actionTemp_Float3++;
				}
				if(targetActionObject2.actionvar6==1)
				{
					actionTemp_Float2 = 6;
					actionTemp_Float3++;
				}
				if(targetActionObject2.actionvar7==1)
				{
					actionTemp_Float2 = 7;
					actionTemp_Float3++;
				}
			}
			
			if(targetActionObject2.theobject.editoraction.contains("company_pub_table1"))
			{
				if(actionTemp_Float1>4)
					actionTemp_Float1=0;
				
				if(actionTemp_Float2>4)
					actionTemp_Float2=0;					
			}
		
			if(actionTemp_Float1>0 || actionTemp_Float2>0)
			{
				Vector3 v3=null;
				Vector2 v2=null;
				if(actionTemp_Float1>0) //hole getränk
				{
					v2 = CHelper.moveHumanByObjectRotation(bar, baseWorldObject, (int)town.getSizeValue(210), (int)town.getSizeValue(180));
				}
				else if(actionTemp_Float2>0) //abräumen
				{
					int seatType=1;
					if(targetActionObject2.theobject.editoraction.contains("company_pub_table1"))
					{
						//seatType=2;
						v2 = new Vector2();
						v2.x=targetActionObject2.pos_x()+targetActionObject2.width/2;
						v2.y=targetActionObject2.pos_y()+targetActionObject2.height/2;
					}
					else
					{
						v3 = getBarSeatPosition((int)actionTemp_Float2, 3, seatType, town);
						v2 = CHelper.moveHumanByObjectRotation(targetActionObject2, baseWorldObject, (int)v3.x, (int)v3.y);
					}
				}
				
				gotoObject(null, true, (int)v2.x, (int)v2.y);
				actionState=4;
			}
			
			
			return;
		}
		
		
		//Befülle Glas/Räume ab
		if(actionState==4)
		{
			//if(parentWorldObject.ziel_x==-1)
			{
				if(baseWorldObject.actionfield1==0)
					deltaTimer=0;
				
				if(actionTemp_Float1>0) //Befüllen
				{
					if(baseWorldObject.ziel_x==-1)
					{
						baseWorldObject.actionfield2=actionTemp_Float3;
						baseWorldObject.actionanim1=2;
						baseWorldObject.actionfield1=1;
						
						if(deltaTimer>5)
							baseWorldObject.actionfield1=2;
						if(deltaTimer>10)
							baseWorldObject.actionfield1=3;
						if(deltaTimer>15)
							baseWorldObject.actionfield1=4;
						if(deltaTimer>20)
							baseWorldObject.actionfield1=5;
						if(deltaTimer>25)
							baseWorldObject.actionfield1=6;
						if(deltaTimer>30)
							baseWorldObject.actionfield1=7;
						
						if(deltaTimer>60)
						{
							int seatType=1;
							if(targetActionObject2.theobject.editoraction.contains("company_pub_table"))
								seatType=2;
							
							Vector3 v3 = getBarSeatPosition((int)actionTemp_Float1, 3, seatType,town);
							Vector2 v2 = CHelper.moveHumanByObjectRotation(targetActionObject2, baseWorldObject, (int)v3.x, (int)v3.y);
							
							//Zum Table
							gotoObject(null, true, (int)v2.x, (int)v2.y);
							actionState=5;
							return;
						}
					}
				}
				
				//Abräumen
				if(actionTemp_Float2>0)
				{
					Boolean bArrived=false;
					if(baseWorldObject.ziel_x==-1)
						bArrived=true;
					
					if(targetActionObject2.theobject.editoraction.contains("company_pub_table"))
					{
						if(baseWorldObject.collides(targetActionObject2))
						{
							bArrived=true;
							baseWorldObject.resetPathFinding();
						}
					}
					
					if(bArrived)
					{
						//Alle auf einmal abräumen
						actionTemp_Float3=0;
						if(targetActionObject2.actionvar1==1)
						{
							actionTemp_Float3++;
							targetActionObject2.actionvar1=0;
						}
						if(targetActionObject2.actionvar2==1)
						{
							actionTemp_Float3++;
							targetActionObject2.actionvar2=0;
						}
						if(targetActionObject2.actionvar3==1)
						{
							actionTemp_Float3++;
							targetActionObject2.actionvar3=0;
						}
						if(targetActionObject2.actionvar4==1)
						{
							actionTemp_Float3++;
							targetActionObject2.actionvar4=0;
						}
						if(targetActionObject2.actionvar5==1)
						{
							actionTemp_Float3++;
							targetActionObject2.actionvar5=0;
						}
						if(targetActionObject2.actionvar6==1)
						{
							actionTemp_Float3++;
							targetActionObject2.actionvar6=0;
						}
						if(targetActionObject2.actionvar7==1)
						{
							actionTemp_Float3++;
							targetActionObject2.actionvar7=0;
						}
						
						
						//Einzel abräumen
//						if(actionTemp_Float2==1)
//							targetActionObject2.actionvar1=0;
//						if(actionTemp_Float2==2)
//							targetActionObject2.actionvar2=0;
//						if(actionTemp_Float2==3)
//							targetActionObject2.actionvar3=0;
//						if(actionTemp_Float2==4)
//							targetActionObject2.actionvar4=0;
//						if(actionTemp_Float2==5)
//							targetActionObject2.actionvar5=0;
//						if(actionTemp_Float2==6)
//							targetActionObject2.actionvar6=0;
//						if(actionTemp_Float2==7)
//							targetActionObject2.actionvar7=0;
						
						
						baseWorldObject.actionanim1=2;
						baseWorldObject.actionfield1=1;
						baseWorldObject.actionfield2=actionTemp_Float3;
						
						if(targetActionObject2.theobject.editoraction.contains("company_pub_table"))
							targetActionObject2.isOccupiedByExtern=null;
						
						actionState=5;
						
						//Zurück zur Bar
						Vector2 v2 = CHelper.moveHumanByObjectRotation(bar, baseWorldObject,  bar.width/2+(int)town.getSizeValue(120), bar.height/2+(int)town.getSizeValue(60));
						gotoObject(bar, true, (int)v2.x, (int)v2.y);
						
						return;
					}
				}
				
				if(baseWorldObject.ziel_x==-1)
				{
					if(actionTemp_Float1==0 && actionTemp_Float2==0)
						actionState=3;
				}
			}
			
			return;
		}
		
		if(actionState==5)
		{
			if(actionTemp_Float1>0) //Getränk abstellen
			{
				Boolean bArrived=false;
				if(baseWorldObject.ziel_x==-1)
					bArrived=true;
				
				if(targetActionObject2.theobject.editoraction.contains("company_pub_table"))
				{
					if(baseWorldObject.collides(targetActionObject2))
					{
						bArrived=true;
						baseWorldObject.resetPathFinding();
					}
				}
				
				if(bArrived)
				{
					float mcount=actionTemp_Float3;
					if(actionTemp_Float3>0 && targetActionObject2.isOccupiedBy!=null && targetActionObject2.actionvar1==0 && targetActionObject2.isOccupiedBy.activeAction.actionState>1)
					{
						actionTemp_Float1=1;
						actionTemp_Float3--;
						targetActionObject2.actionvar1=7;
					}
					if(actionTemp_Float3>0 && targetActionObject2.isOccupiedBy2!=null && targetActionObject2.actionvar2==0 && targetActionObject2.isOccupiedBy2.activeAction.actionState>1)
					{
						actionTemp_Float1=2;
						actionTemp_Float3--;
						targetActionObject2.actionvar2=7;
					}
					if(actionTemp_Float3>0 && targetActionObject2.isOccupiedBy3!=null && targetActionObject2.actionvar3==0 && targetActionObject2.isOccupiedBy3.activeAction.actionState>1)
					{
						actionTemp_Float1=3;
						actionTemp_Float3--;
						targetActionObject2.actionvar3=7;
					}
					if(actionTemp_Float3>0 && targetActionObject2.isOccupiedBy4!=null && targetActionObject2.actionvar4==0 && targetActionObject2.isOccupiedBy4.activeAction.actionState>1)
					{
						actionTemp_Float1=4;
						actionTemp_Float3--;
						targetActionObject2.actionvar4=7;
					}
					if(actionTemp_Float3>0 && targetActionObject2.isOccupiedBy5!=null && targetActionObject2.actionvar5==0 && targetActionObject2.isOccupiedBy5.activeAction.actionState>1)
					{
						actionTemp_Float1=5;
						actionTemp_Float3--;
						targetActionObject2.actionvar5=7;
					}
					if(actionTemp_Float3>0 && targetActionObject2.isOccupiedBy6!=null && targetActionObject2.actionvar6==0 && targetActionObject2.isOccupiedBy6.activeAction.actionState>1)
					{
						actionTemp_Float1=6;
						actionTemp_Float3--;
						targetActionObject2.actionvar6=7;
					}
					if(actionTemp_Float3>0 && targetActionObject2.isOccupiedBy7!=null && targetActionObject2.actionvar7==0 && targetActionObject2.isOccupiedBy7.activeAction.actionState>1)
					{
						actionTemp_Float1=7;
						actionTemp_Float3--;
						targetActionObject2.actionvar7=7;
					}
					
					//Einzeln
					//					if(actionTemp_Float1==1)
					//						targetActionObject2.actionvar1=7;
					//					if(actionTemp_Float1==2)
					//						targetActionObject2.actionvar2=7;
					//					if(actionTemp_Float1==3)
					//						targetActionObject2.actionvar3=7;
					//					if(actionTemp_Float1==4)
					//						targetActionObject2.actionvar4=7;
					//					if(actionTemp_Float1==5)
					//						targetActionObject2.actionvar5=7;
					//					if(actionTemp_Float1==6)
					//						targetActionObject2.actionvar6=7;
					//					if(actionTemp_Float1==7)
					//						targetActionObject2.actionvar7=7;
					
					//CWorldObject wobj = targetActionObject2.isOccupiedBy((int)actionTemp_Float1);
					//if(wobj!=null)
					//{
						//wobj.gameWorld.changeTownMoney(Math.round(-5*mcount));
						//gameWorld.townStatistics.getCurrentStatistics_Finance().residentSpendsMoney+=5*mcount;
						//targetActionObject2.addAnimationEvent(AnimationEventType.MONEY, Math.round(-5*mcount));
						//targetActionObject2.addAnimationEvent(AnimationEventType.MONEY, Math.round(-5*actionTemp_Float3));
					//}
					//else
					//{
						//baseWorldObject.gameWorld.changeTownMoney(Math.round(-5*mcount));
						//gameWorld.townStatistics.getCurrentStatistics_Finance().residentSpendsMoney+=5*mcount;
						//targetActionObject2.addAnimationEvent(AnimationEventType.MONEY, Math.round(-5*mcount));
						//targetActionObject2.addAnimationEvent(AnimationEventType.MONEY, Math.round(-5*actionTemp_Float3));
					//}
					
					actionTemp_Float1=0;
					actionTemp_Float3=0;
					actionState=6;
					baseWorldObject.actionanim1=0;
					baseWorldObject.actionfield1=0;
					
					if(targetActionObject2.theobject.editoraction.contains("company_pub_table"))
						targetActionObject2.isOccupiedByExtern=null;
					
					
					//Zurück zur Bar
					Vector2 v2 = CHelper.moveHumanByObjectRotation(bar, baseWorldObject, bar.width/2+(int)town.getSizeValue(80), (int)town.getSizeValue(190));
					gotoObject(null, true, (int)v2.x, (int)v2.y);
					return;
				}
			}
			
			if(actionTemp_Float2>0) //Leeres Glas zurückbringen
			{
				if(baseWorldObject.ziel_x==-1)
				{
					Vector2 v2 = CHelper.moveHumanByObjectRotation(bar, baseWorldObject, bar.width/2, bar.height/2+(int)town.getSizeValue(40));
					gotoObject(bar, true, (int)v2.x, (int)v2.y);
					actionState=6;
				}
			}
		}
		
		if(actionState==6)
		{
			if(baseWorldObject.ziel_x==-1)
			{
				if(actionTemp_Float2>0) //Leeres Glas zurückbringen
				{
					if(baseWorldObject.ziel_x==-1)
					{
						actionTemp_Float3=0;
						actionTemp_Float2=0;
						baseWorldObject.actionanim1=0;
						baseWorldObject.actionfield1=0;
					}
				}
				
				actionState=3;
			}
		}
	}
	
	
	private void doAction_Church()
	{
		if(actionMode!=ActionMode.CHURCH)
			return;
		
		if(actionState==0)
		{
			gotoObject(targetActionObject, true);
			actionState=1;
			return;
		}		
		
		if(actionState==1)
		{
			int movx=0;
			
			if(isOccupiedByMe(targetActionObject, 1))
				movx=95;
			if(isOccupiedByMe(targetActionObject, 2))
				movx=215;
			if(isOccupiedByMe(targetActionObject, 3))
				movx=335;
			if(isOccupiedByMe(targetActionObject, 4))
				movx=455;
			
			if(gotoObject_Arrived(targetActionObject, (int)town.getSizeValue(movx), (int)town.getSizeValue(40), targetActionObject.rotation() + 180, false, 0))
			{
				actionTemp_Float1=rand.nextInt(3);
				if(actionTemp_Float1==0)
					actionTemp_Float1=22;
				if(actionTemp_Float1==1)
					actionTemp_Float1=2;
				if(actionTemp_Float1==2)
					actionTemp_Float1=3;
				
				baseWorldObject.actionanim1=actionTemp_Float1;
				baseWorldObject.actionanim2=4;
				deltaTimer=0;
				actionState=2;
			}
			
			return;
		}
		
		if(actionState==2)
		{
			cancelIfDark();
			
			if(deltaTimer>(60*45))
			{
				baseWorldObject.actionanim2=0;
				handleActionValueBonus(1);
				bActionMode=false;
				releaseObject(targetActionObject);
			}
		}
	}
	
	
	
	
	//********
	//Garbage
	//********
	private void doAction_GarbageTruck()
	{
		CWorldObject truck = targetActionObject;
		
		if(actionState==0)
		{
			//Speichere Parkkoordinaten, wohin der Truck nach der Action wieder zurückgebracht wird
			if(truck.x_temp>0) //Koordinaten nur überschreiben wenn Standort-Position auf Company-Adr ist 
			{
				CAddress adr = town.gameWorld.getAddressByPoint(truck.pos_x(), truck.pos_y());
				if(adr!=null && truck.belongsToCompany!=null && truck.belongsToCompany.address_company!=null && adr.addressId==truck.belongsToCompany.address_company.addressId)
				{
					truck.x_temp=truck.pos_x();
					truck.y_temp=truck.pos_y();
					truck.rotation_temp=truck.rotation();
				}
			}
			else
			{
				truck.x_temp=truck.pos_x();
				truck.y_temp=truck.pos_y();
				truck.rotation_temp=truck.rotation();
			}
			
			//Gehe zum Truck
			Vector2 v2 = CHelper.moveVectorByRotationS2D(truck.pos_x(), truck.pos_y(), truck.width, truck.height/2+(int)town.getSizeValue(30), truck.width/2, truck.height/2, truck.rotation());
			gotoXY((int)v2.x, (int)v2.y);
			actionState=1;
			
			return;
		}
		
		if(actionState==1)
		{
			//Am Truck angekommen
			//if(baseWorldObject.ziel_x==-1)
			if(gotoObject_Arrived(truck, truck.width+(int)town.getSizeValue(50), truck.height/2-(int)town.getSizeValue(30), truck.rotation(), false))
			{
				truck.actionanim1=1;
				truck.isOccupiedBy=baseWorldObject;
				actionState=1.1f;
				deltaTimer=0;
			}
			
			return;
		}
		
		if(actionState==1.1f)
		{
			if(deltaTimer>50)
			{
				truck.actionanim1=0;
				actionState=1.2f;
			}
			
			return;
		}
		
		if(actionState==1.2f)
		{
			//Fahre mit Truck zu erstem Müllcontainer
			gotoObject(truck, targetActionObject2, true, 0, 0);
						
			//gotoObject(truck, targetActionObject2, false, 0, 0);
			//if(truck.path==null) {
				//cancelAction(""+truck.theobject.objectName + "->" + targetActionObject2.theobject.objectName);
				//return;
			//}
			
			//gotoObjectXY(truck, targetActionObject2, truck.width, truck.height/2);
			actionState=2;
			return;
		}
		
		if(actionState==2)
		{
			baseWorldObject.bDrawObject=false;
			baseWorldObject.setPosition((int)truck.pos_x(), (int)truck.pos_y());
			
			//An der Zieladresse angekommen
			if(gotoObject_Arrived(truck, targetActionObject2, -1, -1, -1, true))
				actionState=3;
			
			return;
		}
		
		if(actionState==3)
		{
			//Worker steigt aus und holt Mülltonne
			Vector2 v2 = CHelper.moveHumanByObjectRotation(truck, baseWorldObject, truck.width, truck.height/2+(int)town.getSizeValue(30));
			baseWorldObject.setPosition((int)v2.x, (int)v2.y);
			baseWorldObject.bDrawObject=true;
			
			gotoObject(targetActionObject2, true);
			actionState=3.1f;
			truck.actionanim1=1;
			deltaTimer=0;
			
			return;
		}
		
		if(actionState==3.1f)
		{
			if(deltaTimer>25)
			{
				truck.actionanim1=0;
				actionState=4f;
			}
			
			return;
		}
		
		if(actionState==4f)
		{
			//An Mülltonne angekommen
			if(gotoObject_Arrived(targetActionObject2, -1, -1, -1, true))
			{
				targetActionObject2.x_temp=targetActionObject2.pos_x();
				targetActionObject2.y_temp=targetActionObject2.pos_y();
				targetActionObject2.rotation_temp=targetActionObject2.rotation();
				
				Vector2 v2 = CHelper.moveHumanByObjectRotation(truck, baseWorldObject, -truck.width/2, truck.height+(int)town.getSizeValue(130));
				gotoObject(truck, true, (int)v2.x, (int)v2.y);
				actionState=5;
				deltaTimer=0;
			}
			
			return;
		}
		
		if(actionState==5)
		{
			baseWorldObject.actionanim1=2;
			
			//Mülltonne zum Truck bringen
			if(gotoObject_Arrived(truck, -1, -1, -1, false))
			{
				baseWorldObject.setRotation(truck.rotation());
				Vector2 v2 = CHelper.moveHumanByObjectRotation(truck, baseWorldObject, truck.width/2-targetActionObject2.width/2, truck.height-(int)town.getSizeValue(70));
				gotoObject(truck, true, (int)v2.x, (int)v2.y);
				actionState=6;
			}
			
			Vector2 v2 = CHelper.moveObjectByHumanRotation(baseWorldObject, targetActionObject2, (int)town.getSizeValue(57), (int)town.getSizeValue(-10));
			targetActionObject2.setPosition((int)v2.x, (int)v2.y);
			
			return;
		}
		
		if(actionState==6)
		{
			//Mülltonne am Truck
			if(gotoObject_Arrived(truck, -1, -1, -1, false))
			{
				actionState=7;
				deltaTimer=0;
				truck.actionanim1=2;
			}
			
			Vector2 v2 = CHelper.moveObjectByHumanRotation(baseWorldObject, targetActionObject2, (int)town.getSizeValue(57), (int)town.getSizeValue(-10));
			targetActionObject2.setPosition((int)v2.x, (int)v2.y);
			
			return;
		}
		
		if(actionState==7)
		{
			//Mülltonne zum Platz zurück, wenn geleert
			if(deltaTimer>50)
			{
				truck.objectFilling+=targetActionObject2.objectFilling;
				targetActionObject2.objectFilling=0;
				
				if(truck.objectFilling>truck.getObjectFillingMax())
					truck.objectFilling=truck.getObjectFillingMax();
				
				truck.actionanim1=0;
				
				gotoObject(null, true, targetActionObject2.x_temp, targetActionObject2.y_temp);
				actionState=8;
			}
			
			return;
		}
		
		if(actionState==8)
		{
			Vector2 v2 = CHelper.moveObjectByHumanRotation(baseWorldObject, targetActionObject2, (int)town.getSizeValue(57), (int)town.getSizeValue(-10));
			targetActionObject2.setPosition((int)v2.x, (int)v2.y);
			
			//Mülltonne am Platz
			if(gotoObject_Arrived(null, -1, -1, -1, false))
			{
				targetActionObject2.setPosition(targetActionObject2.x_temp, targetActionObject2.y_temp);
				targetActionObject2.setRotation(targetActionObject2.rotation_temp);
				baseWorldObject.actionanim1=0;
				Vector2 v3 = CHelper.moveHumanByObjectRotation(truck, baseWorldObject, truck.width, truck.height/2+(int)town.getSizeValue(30));
				gotoObject(truck, true, (int)v3.x, (int)v3.y);
				actionState=8.1f;
			}
			
			return;
		}
		
		if(actionState==8.1f)
		{
			//Zurück am Truck
			if(gotoObject_Arrived(truck, truck.width, truck.height/2+(int)town.getSizeValue(30), truck.rotation(), false))
			{
				deltaTimer=0;
				truck.actionanim1=1;
				actionState=9;
			}
			
			return;
		}
		
		if(actionState==9f)
		{
			if(deltaTimer>25)
			{
				truck.actionanim1=0;
				actionState=10;
			}
			
			return;
		}		
		
		if(actionState==10)
		{
			if(targetActionObject11!=null)
			{
				//Zurückfahren zum truck leeren und parken am abstellplatz
				gotoObject(truck, targetActionObject11, true, targetActionObject11.pos_x()+targetActionObject11.width/2, targetActionObject11.pos_y()+targetActionObject11.height/2);						
				actionState=11;
				
				return;
			}
			
			//Nächste Mülltonne anfahren oder zurück zum Recycling Center
			baseWorldObject.bDrawObject=false;
			baseWorldObject.setPosition((int)truck.pos_x(), (int)truck.pos_y());
			
			releaseObject(targetActionObject2);
			targetActionObject2=null;
			
			if(targetActionObject3!=null)
			{
				targetActionObject2=targetActionObject3;
				targetActionObject3=null;
			}
			else if(targetActionObject4!=null)
			{
				targetActionObject2=targetActionObject4;
				targetActionObject4=null;
			}
			else if(targetActionObject5!=null)
			{
				targetActionObject2=targetActionObject5;
				targetActionObject5=null;
			}
			else if(targetActionObject6!=null)
			{
				targetActionObject2=targetActionObject6;
				targetActionObject6=null;
			}			
			
			if(targetActionObject2==null || truck.objectFilling>=truck.getObjectFillingMax())
			{
				//Zurückfahren zum truck leeren und parken am abstellplatz
				releaseObject(targetActionObject2);
				releaseObject(targetActionObject3);
				releaseObject(targetActionObject4);
				releaseObject(targetActionObject5);
				releaseObject(targetActionObject6);
				
				//Suche recycling machine
				if(targetActionObject11==null)
				{
					for(CWorldObject wobj : truck.belongsToCompany.address_company.listWorldObjects)
					{
						if(wobj.theobject.editoraction.contains("company_recyclingcenter_recyclingmachine"))
						{
							targetActionObject11=wobj;
							return;
						}
					}
					
					//Es gibt keine Recycling Machine: Fahre zurück zur Company Adresse
					gotoObject(truck, null, true, truck.x_temp, truck.y_temp);						
					actionState=11;
					return;
					
				}
			}
			
			//Nächste Tonne anfahren
			if(targetActionObject2!=null)
			{
				gotoObject(truck, targetActionObject2, true, 0, 0);
				actionState=2;
			}
			
			return;
		}
		
		if(actionState==11)
		{
			baseWorldObject.setPosition((int)truck.pos_x(), (int)truck.pos_y());
			
			//Am Recycling Center angekommen
			if(targetActionObject11!=null && targetActionObject11.onlineByWorkInput)
			{
				if(truck.ziel_x<0 || truck.collides(targetActionObject11))
				{
					if(reserveObject(targetActionObject11))
					{
						//if(gotoObject_Arrived(truck, targetActionObject11, targetActionObject11.width/2+truck.width/2-60, -targetActionObject11.height+targetActionObject11.theobject.ObjectAction_Move_Pixels_Y, targetActionObject11.rotation(), true))
						if(truck.ziel_x==-1 || truck.collides(targetActionObject11))
						{
							Vector2 v2 = CHelper.moveVectorByRotationS2D(targetActionObject11.pos_x(), targetActionObject11.pos_y(), targetActionObject11.theobject.ObjectAction_Move_Pixels_X, targetActionObject11.theobject.ObjectAction_Move_Pixels_Y, Math.round(targetActionObject11.width/2), Math.round(targetActionObject11.height/2), targetActionObject11.rotation());
							v2.x-=truck.width/2;
							v2.y-=truck.height/2;
							truck.setPosition((int)v2.x, (int)v2.y);
							truck.setRotation(targetActionObject11.rotation());
							
							truck.actionanim1=2;
							actionState=12;
							deltaTimer=0;
						}
					}
				}
			}
			else
			{
				//Es gibt keine Recycling Machine oder Recycling Machine ist nicht online
				if(truck.ziel_x==-1)
				{
					truck.setPosition(truck.x_temp, truck.y_temp);
					truck.setRotation(truck.rotation_temp);
					truck.isOccupiedBy=null;
					baseWorldObject.bDrawObject=true;
					bActionMode=false;
					
					return;
				}
			}
			
			return;
		}
		
		if(actionState==12)
		{
			//Truck entladen
			if(deltaTimer>100)
			{
				int money=Math.round(truck.objectFilling*targetActionObject11.theobject.ATTR_MONEY);
				targetActionObject11.addAnimationEvent(AnimationEventType.MONEY, money);
				town.gameWorld.changeTownMoney(money);
				
				truck.objectFilling=0;
				releaseObject(targetActionObject11);
				
				//Zum Abstellplatz
				truck.actionanim1=0;
				gotoObject(truck, null, true, truck.x_temp, truck.y_temp);
				actionState=13;
			}
		}
		
		if(actionState==13)
		{
			//Truck abstellen
			if(gotoObject_Arrived(truck, null, truck.x_temp, truck.y_temp, truck.rotation_temp, false))
			{
				truck.setPosition(truck.x_temp, truck.y_temp);
				truck.setRotation(truck.rotation_temp);
				
				Vector2 v2 = CHelper.moveHumanByObjectRotation(truck, baseWorldObject, truck.width, truck.height/2+(int)town.getSizeValue(30));
				baseWorldObject.setPosition((int)v2.x, (int)v2.y);
				baseWorldObject.setRotation(truck.rotation_temp);
				
				truck.actionanim1=1;
				truck.isOccupiedBy=null;
				
				baseWorldObject.bDrawObject=true;
				deltaTimer=0;
				actionState=13.1f;
			}
		}
		
		if(actionState==13.1f)
		{
			if(deltaTimer>25)
			{
				truck.actionanim1=0;
				bActionMode=false;
			}
		}
	}
	private void doAction_TakeOutGarbage()
	{
		if(actionMode!=ActionMode.TAKE_OUT_GARBAGE)
			return;
		
		//targetActionObject	Garbage Bag oder Garbage Can
		//targetActionObject	Garbage Container
		
		int movCanX=(int)town.getSizeValue(57);
		int movCanY=(int)town.getSizeValue(5);
		float movCanRot=baseWorldObject.rotation();
		
		if(actionState==0)
		{
			//Gehe zu Garbage oder Can
			gotoObject(targetActionObject, true);
			actionState=1;
			
			return;
		}
		
		if(actionState==1)
		{
			//Am Garbage/Can angekommen
			//if(gotoObject_Arrived(targetActionObject, -1, -1, -1, false))
			if(gotoObject_Arrived(targetActionObject, 5))
			{
				actionState=2;
				targetActionObject.x_temp=targetActionObject.pos_x();
				targetActionObject.y_temp=targetActionObject.pos_y();
				targetActionObject.rotation_temp=targetActionObject.rotation();
				baseWorldObject.actionanim1=2;
				
				//Gehe zum Container
				gotoObjectXY(targetActionObject2, targetActionObject2.width/2, (int) town.getSizeValue(-50));
				
				actionState=2;
			}
			
			return;
		}
		
		if(actionState==2)
		{
			//Trage Müll oder Can
			moveObjectByHumanRotation(baseWorldObject,targetActionObject, movCanX, movCanY, movCanRot);
			
			//Am Container angekommen
			if(gotoObject_Arrived(targetActionObject2, targetActionObject2.width/2, (int) town.getSizeValue(-50), targetActionObject2.rotation()+180, false))
			{
				deltaTimer=0;
				targetActionObject2.doObjectAction=true;
				actionState=3;
			}
			
			return;
		}
		
		if(actionState==3)
		{
			//Trage Müll oder Can
			moveObjectByHumanRotation(baseWorldObject, targetActionObject, movCanX, movCanY, movCanRot);
			targetActionObject.actionColor1=new Color(0.3f,0.3f,0.6f,0.7f);
			
			if(deltaTimer>30)
			{
				targetActionObject.actionColor1=null;
				
				targetActionObject2.doObjectAction=false;
				releaseObject(targetActionObject2);
				
				if(targetActionObject.theobject.editoraction.contains("garbagecan"))
				{
					while(targetActionObject.objectFilling>0 && targetActionObject2.objectFilling<targetActionObject2.getObjectFillingMax())
					{
						targetActionObject.objectFilling--;
						targetActionObject2.objectFilling++;
					}
					gotoXY(targetActionObject.x_temp, targetActionObject.y_temp);
					actionState=4;
				}
				
				if(targetActionObject.theobject.editoraction.contains("recyclingcenter_garbagebag") || 
						targetActionObject.theobject.editoraction.contains("supermarket_buyin"))
				{
					targetActionObject2.objectFilling++;
					if(targetActionObject2.objectFilling>targetActionObject2.getObjectFillingMax())
						targetActionObject2.objectFilling=targetActionObject2.getObjectFillingMax();
					baseWorldObject.town.gameGui.removeWorldObject(targetActionObject, false);
					baseWorldObject.actionanim1=0;
					bActionMode=false;
				}
				
				return;
			}
		}
		
		if(actionState==4)
		{
			//Can zurückbringen
			moveObjectByHumanRotation(baseWorldObject, targetActionObject, movCanX, movCanY, movCanRot);
			
			if(baseWorldObject.ziel_x==-1)
			{
				baseWorldObject.resetPathFinding();
				targetActionObject.setPosition(targetActionObject.x_temp, targetActionObject.y_temp);
				targetActionObject.setRotation(targetActionObject.rotation_temp);
				baseWorldObject.actionanim1=0;
				
				bActionMode=false;
			}
			
			return;
		}
	}
	
	//**********
	//Playground
	//**********
	private void doAction_Playground_Swing()
	{
		if(actionState==0)
		{
			gotoObject(targetActionObject, true);
			actionState=1;
			return;
		}
		
		if(actionState==1)
		{
			int movx = 0;
			int movy = 0;
			float rot=0;
			
			if(gotoObject_Arrived(targetActionObject, targetActionObject.width/2, targetActionObject.height/2, targetActionObject.rotation(), true))
			{
				actionState=2;
				deltaTimer=0;
				deltaTimer2=0;
				return;
			}
		}
		
		if(actionState==2)
		{
			if(deltaTimer>10)
			{
				if(targetActionObject.actionfield1==0)
				{
					if(targetActionObject.actionfield2==1)
					{
						targetActionObject.actionfield2=0;
						targetActionObject.actionfield1=1;
					}
					else
					{
						targetActionObject.actionfield2=1;
						targetActionObject.actionfield1=2;
					}
				}
				else if(targetActionObject.actionfield1==1)
					targetActionObject.actionfield1=0;
				else if(targetActionObject.actionfield1==2)
					targetActionObject.actionfield1=0;
				
				deltaTimer=0;
			}
			
			if(targetActionObject.actionfield1==0)
			{
				Vector2 v2 = CHelper.moveHumanByObjectRotation(targetActionObject, baseWorldObject, targetActionObject.width/2, targetActionObject.height/2);
				baseWorldObject.setPosition((int)v2.x, (int)v2.y);
			}
			
			if(targetActionObject.actionfield1==1)
			{
				Vector2 v2 = CHelper.moveHumanByObjectRotation(targetActionObject, baseWorldObject, targetActionObject.width/2, targetActionObject.height/2-(int)town.getSizeValue(80));
				baseWorldObject.setPosition((int)v2.x, (int)v2.y);
			}

			if(targetActionObject.actionfield1==2)
			{
				Vector2 v2 = CHelper.moveHumanByObjectRotation(targetActionObject, baseWorldObject, targetActionObject.width/2, targetActionObject.height/2+(int)town.getSizeValue(70));
				baseWorldObject.setPosition((int)v2.x, (int)v2.y);
			}

			
			if(deltaTimer2>1300)
			{
				handleActionValueBonus(1);
				bActionMode=false;
				targetActionObject.actionfield1=0;
				targetActionObject.actionfield2=0;
				releaseObject(targetActionObject);
			}
		}
	}
	private void doAction_Playground_Seesaw()
	{
		if(actionState==0)
		{
			gotoObject(targetActionObject, true);
			actionState=1;
			return;
		}
		
		if(actionState==1)
		{
			int movx = 0;
			int movy = 40;
			float rot=0;
			
			if(isOccupiedByMe(targetActionObject, 1))
			{
				rot=targetActionObject.rotation()+90;
				movx+=45;
			}
			else if(isOccupiedByMe(targetActionObject, 10))
			{
				rot=targetActionObject.rotation()+270;
				movx+=385;
			}
			
			if(gotoObject_Arrived(targetActionObject, (int)town.getSizeValue(movx), (int)town.getSizeValue(movy), rot, true))
			{
				actionState=2;
				deltaTimer=0;
				deltaTimer2=0;
				baseWorldObject.actionanim1=2;
				return;
			}
		}
		
		if(actionState==2)
		{
//			if(!isOccupiedByMe(targetActionObject, 1) && !isOccupiedByMe(targetActionObject, 4))
//			{
//				bActionMode=false;
//				return;
//			}
						
			if(isOccupiedByMe(targetActionObject, 1) && targetActionObject.isOccupiedByExtern!=null)
			{
				if(deltaTimer2>8)
				{
					if(targetActionObject.actionfield1==0)
						targetActionObject.actionfield1=1;
					else
						targetActionObject.actionfield1=0;
				
					deltaTimer2=0;
				}
			}
			
			int movx = 0;
			int movy = 40;
			
			if(isOccupiedByMe(targetActionObject, 1))
			{
				if(targetActionObject.actionfield1==0) //oben
					movx+=30+35-10;
				if(targetActionObject.actionfield1==1) //unten
					movx+=10;
				
				Vector2 v2 = CHelper.moveHumanByObjectRotation(targetActionObject, baseWorldObject, (int)town.getSizeValue(movx), (int)town.getSizeValue(movy));
				baseWorldObject.setPosition((int)v2.x, (int)v2.y);
			}
			else if(isOccupiedByMe(targetActionObject, 10))
			{
				if(targetActionObject.actionfield1==0) //unten
					movx+=385;
				if(targetActionObject.actionfield1==1) //oben
					movx+=385-60+15;
				
				Vector2 v2 = CHelper.moveHumanByObjectRotation(targetActionObject, baseWorldObject, (int)town.getSizeValue(movx), (int)town.getSizeValue(movy));
				baseWorldObject.setPosition((int)v2.x, (int)v2.y);
			}
									
			if(deltaTimer>1200)
			{
				handleActionValueBonus(1);
				bActionMode=false;
				baseWorldObject.actionanim1=0;
				targetActionObject.actionfield1=0;
				releaseObject(targetActionObject);
			}
		}
	}
	private void doAction_Playground_Sandpit()
	{
		if(actionState==0)
		{
			actionState=1;
			return;
		}
		
		if(actionState==1)
		{
			gotoObject(targetActionObject, true, targetActionObject.pos_x()+targetActionObject.width/2+rand.nextInt((int)town.getSizeValue(200))-rand.nextInt((int)town.getSizeValue(200)), targetActionObject.pos_y()+targetActionObject.height/2+rand.nextInt((int)town.getSizeValue(200))-rand.nextInt((int)town.getSizeValue(200)));
			actionState=2;
			baseWorldObject.actionanim1=1;
		}
		
		if(actionState==2)
		{
//			if(!isOccupiedByMe(targetActionObject, 1) && !isOccupiedByMe(targetActionObject, 2) && !isOccupiedByMe(targetActionObject, 3 )&& !isOccupiedByMe(targetActionObject, 4))
//			{
//				bActionMode=false;
//				parentWorldObject.actionanim1=0;
//				releaseObject(targetActionObject);
//				deltaTimer2=0;
//				deltaTimer=0;
//				return;
//			}
						
			if(deltaTimer2>3200)
			{
				handleActionValueBonus(1);
				bActionMode=false;
				baseWorldObject.actionanim1=0;
				releaseObject(targetActionObject);
				deltaTimer2=0;
				deltaTimer=0;
			}
			
			if(deltaTimer>1200)
			{
				bActionMode=false;
				baseWorldObject.actionanim1=0;
				releaseObject(targetActionObject);
			}
		}
	}
	private void doAction_Playground_Slide()
	{
		int movy=60;
		
		if(actionState==0)
		{
			Vector2 v2 = CHelper.moveVectorByRotationS2D(targetActionObject.pos_x(), targetActionObject.pos_y(), (int)town.getSizeValue(-100), (int)town.getSizeValue(150), targetActionObject.width/2, targetActionObject.height/2, targetActionObject.rotation());
			gotoObject(targetActionObject, true, (int)v2.x, (int)v2.y);
			actionState=1;
			return;
		}
		
		if(actionState==1)
		{
			actionState=2;
			if(baseWorldObject.ziel_x==-1)
			{
				Vector2 v2 = CHelper.moveVectorByRotationS2D(targetActionObject.pos_x(), targetActionObject.pos_y(), (int)town.getSizeValue(-50), (int)town.getSizeValue(80), targetActionObject.width/2, targetActionObject.height/2, targetActionObject.rotation());
				gotoObject(targetActionObject, true, (int)v2.x, (int)v2.y);
				actionState=2;
				return;
			}
		}
		
		if(actionState==2)
		{
			int movx = 0;
			if(gotoObject_Arrived(targetActionObject, (int)town.getSizeValue(movx), (int)town.getSizeValue(movy), targetActionObject.rotation()+80, false))
			{
				actionState=3;
				deltaTimer=0;
				actionTemp_Float1=0;
				return;
			}
		}
		
		if(actionState==3)
		{
			if(deltaTimer>10)
			{
				actionTemp_Float1+=10;
				Vector2 v2 = CHelper.moveHumanByObjectRotation(targetActionObject, baseWorldObject, (int)town.getSizeValue(actionTemp_Float1), (int)town.getSizeValue(movy));
				baseWorldObject.setPosition((int)v2.x, (int)v2.y);
				deltaTimer=0;
				
				if(actionTemp_Float1>100)
				{
					actionState=4;
					deltaTimer=0;
				}
			}
		}
		
		if(actionState==4)
		{
			if(deltaTimer>1)
			{
				actionTemp_Float1+=10;
				Vector2 v2 = CHelper.moveHumanByObjectRotation(targetActionObject, baseWorldObject, (int)town.getSizeValue(actionTemp_Float1), (int)town.getSizeValue(movy));
				baseWorldObject.setPosition((int)v2.x, (int)v2.y);
				deltaTimer=0;
				
				if(actionTemp_Float1>(int)town.getSizeValue(450))
				{
					actionState=5;
				}
			}
		}
		
		if(actionState==5)
		{
			handleActionValueBonus(1);
			releaseObject(targetActionObject);
			bActionMode=false;
		}
	}
	private void doAction_Playground()
	{
		if(actionMode != ActionMode.PLAYGROUND)
			return;
		
		if(targetActionObject==null)
			return;
		
		if(targetActionObject!=null && targetActionObject.theobject.editoraction.contains("company_playground_slide"))
			doAction_Playground_Slide();
		if(targetActionObject!=null && targetActionObject.theobject.editoraction.contains("company_playground_sandpit"))
			doAction_Playground_Sandpit();
		if(targetActionObject!=null && targetActionObject.theobject.editoraction.contains("company_playground_swing"))
			doAction_Playground_Swing();		
		if(targetActionObject!=null && targetActionObject.theobject.editoraction.contains("company_playground_seesaw"))
			doAction_Playground_Seesaw();		
	}
	
	//**************
	//School/College
	//**************
	private void doAction_Professor()
	{
		//..
		//- setzt sich an den platz
		//- blatt/ordner liegt auf dem tisch wenn er occupied ist
		//- läuft hin und wieder zur tafel und schreibt was auf
		
		CWorldObject teachersDesk = targetActionObject;
		
		if(actionState==0)
		{
			//Zum lectern gehen
			gotoObject(teachersDesk, true);
			actionState=1;
			return;
		}
		
		if(actionState==1)
		{
			//Am lectern angekommen
			int movx = 295+12;
			int movy = 128;
			
			if(gotoObject_Arrived(teachersDesk, (int)town.getSizeValue(movx), (int)town.getSizeValue(movy), teachersDesk.rotation()+180, true))
			{
				actionState=2;
				deltaTimer=0;
				targetActionObject.actionvar1=1;
				return;
			}
		}
		
		if(actionState==2)
		{
			if(deltaTimer2>50)
				baseWorldObject.actionanim1=5;
			else
				baseWorldObject.actionanim1=0;
			
			if(deltaTimer2>80)
				deltaTimer2=0;
			
			//Leinwand durchschalten
			if(deltaTimer>100)
				targetActionObject.tempcount=1;
			if(deltaTimer>300)
				targetActionObject.tempcount=2;
			if(deltaTimer>500)
				targetActionObject.tempcount=3;
			if(deltaTimer>700)
				targetActionObject.tempcount=4;
			if(deltaTimer>900)
				targetActionObject.tempcount=5;
			if(deltaTimer>1100)
				targetActionObject.tempcount=6;
			if(deltaTimer>1300)
				targetActionObject.tempcount=7;
			
			if(deltaTimer>1500)
				deltaTimer=80;
			
		}
	}
	private void doAction_Teacher()
	{
		//- setzt sich an den platz
		//- blatt/ordner liegt auf dem tisch wenn er occupied ist
		//- läuft hin und wieder zur tafel und schreibt was auf
		
		CWorldObject teachersDesk = targetActionObject;
		
		if(actionState==0)
		{
			gotoObject(teachersDesk, true);
			actionState=1;
			return;
		}
		
		if(actionState==1)
		{
			int movx = 295;
			int movy = 128;
			
			if(gotoObject_Arrived(teachersDesk, (int)town.getSizeValue(movx), (int)town.getSizeValue(movy), teachersDesk.rotation()+180, true))
			{
				actionState=2;
				deltaTimer=0;
				targetActionObject.actionvar1=1;
				return;
			}
		}
		
		if(actionState==2)
		{
			if(deltaTimer>1500)
			{
				//An Tafel stellen
				
				Vector2 v2 = CHelper.moveHumanByObjectRotation(teachersDesk, baseWorldObject, (int)town.getSizeValue(140), (int)town.getSizeValue(100));
				baseWorldObject.setRotation(targetActionObject.rotation()-25);
				baseWorldObject.setPosition((int)v2.x, (int)v2.y);
				deltaTimer=0;
				actionState=3;
				baseWorldObject.actionanim1=15;
			}
		}
		
		if(actionState==3)
		{
			//Tafel beschreiben
			
			if(deltaTimer>100)
				targetActionObject.tempcount=1;
			if(deltaTimer>300)
				targetActionObject.tempcount=2;
			if(deltaTimer>500)
				targetActionObject.tempcount=3;
			if(deltaTimer>700)
				targetActionObject.tempcount=4;
			if(deltaTimer>900)
				targetActionObject.tempcount=5;
			if(deltaTimer>1100)
				targetActionObject.tempcount=5;
			if(deltaTimer>1200)
				targetActionObject.tempcount=5;
			
		}
	}
	private void doAction_Preacher()
	{
		CWorldObject altar = targetActionObject;
		
		if(actionState==0)
		{
			//Zum altar gehen
			gotoObjectXY(altar, altar.width/2, altar.height/2);
			actionState=1;
			return;
		}
		
		if(actionState==1)
		{
			//Am altar angekommen
			if(gotoObject_Arrived(altar, altar.width/2, altar.height+(int) town.getSizeValue(30), altar.rotation()+180, true))
			{
				//altar.actionanim1=1;
				//actionState=2;
				//deltaTimer=0;
				//deltaTimer2=0;
				//actionTemp_Float1=rand.nextInt(800);
				actionState=2;
			}
			return;
		}
		
		if(actionState==2)
		{
			cancelIfDark();
			
			playRandomActionAnim();
			
			//if(deltaTimer>250)
			//{
			//	increaseTaskSkillLevel(targetActionObject);
			//	baseWorldObject.actionanim1=0;
			//	targetActionObject2.actionfield1 = baseWorldObject.thehuman.getWorkOutputPerHour(true, targetActionObject, false);
			//	actionState=0;
			//	resetRandomActionAnim();
			//	return;
			//}
			return;
		}
	}
	
	
	private void doAction_Research()
	{
		CWorldObject researchTable = targetActionObject;
		
		if(actionState==0)
		{
			//Zum table gehen
			gotoObjectXY(researchTable, (int) town.getSizeValue(317), (int) town.getSizeValue(-22));
			actionState=1;
			return;
		}
		
		if(actionState==1)
		{
			//Am table angekommen
			if(gotoObject_Arrived(researchTable, (int) town.getSizeValue(317), (int) town.getSizeValue(-22), researchTable.rotation()+180, true))
			{
				researchTable.actionanim1=1;
				actionState=2;
				deltaTimer=0;
				deltaTimer2=0;
				actionTemp_Float1=rand.nextInt(800);
			}
			return;
		}
		
		if(actionState==2)
		{
			if(deltaTimer2>150)
				baseWorldObject.actionanim1=5;
			else
				baseWorldObject.actionanim1=0;
			
			if(deltaTimer2>180)
				deltaTimer2=0;
			
			if(deltaTimer>actionTemp_Float1)
			{
				actionState=3;
				deltaTimer=0;
				deltaTimer2=0;
			}
			return;
		}
		
		if(actionState==2)
		{
			gotoObjectXY(researchTable, researchTable.width/2, (int) town.getSizeValue(-10));
			actionState=3;
			return;
		}
		
		if(actionState==3)
		{
			if(baseWorldObject.ziel_x==-1)
			{
				gotoObjectXY(researchTable, (int) town.getSizeValue(75), (int) town.getSizeValue(-31));
				actionState=4;
			}
			return;
		}
		
		if(actionState==4)
		{
			if(gotoObject_Arrived(researchTable, (int) town.getSizeValue(82), (int) town.getSizeValue(-31), researchTable.rotation()+180, false))
			{
				actionState=5;
				deltaTimer=0;
				deltaTimer2=0;
				actionTemp_Float1=rand.nextInt(1200);
				baseWorldObject.actionanim1=1;
			}
			return;
		}
		
		if(actionState==5)
		{
			//if(deltaTimer>0)
			//	parentWorldObject.actionanim1=1;
			//if(deltaTimer>140)
			//	parentWorldObject.actionanim1=0;
			
			if(deltaTimer>actionTemp_Float1)
			{
				actionState=0;
				deltaTimer=0;
				deltaTimer2=0;
				gotoObjectXY(researchTable, researchTable.width/2, (int) town.getSizeValue(-10));
				actionState=6;
				baseWorldObject.actionanim1=0;
			}
			return;
		}
		
		if(actionState==6)
		{
			if(baseWorldObject.ziel_x==-1)
			{
				actionState=0;
			}
			return;
		}
	}
	private void doAction_Student(int itype)
	{
		//..
		//- blatt/ordner liegt auf dem tisch wenn er occupied ist
		//-> 2. occupied benötigt
		//- treibt unfug, wirft papierflieger, ...
		//- kind haut anderes kind
		
		CWorldObject studentDesk = targetActionObject;
		
		if(actionState==0)
		{
			gotoObject(studentDesk, true);
			actionState=1;
			return;
		}
		
		if(actionState==1)
		{
			int movx = 55;
			int movy = 60;
			int student=1;
			
			if(studentDesk.worker2!=null && studentDesk.worker2.uniqueId==baseWorldObject.uniqueId)
			{
				movx = 155;
				movy = 60;
				student=2;
			}
			
			if(gotoObject_Arrived(studentDesk, (int)town.getSizeValue(movx), (int)town.getSizeValue(movy), studentDesk.rotation()+180, true))
			{
				actionState=2;
				deltaTimer=0;
				
				if(student==1)
					targetActionObject.actionvar1=1;
				if(student==2)
					targetActionObject.actionvar2=1;
				
				return;
			}
		}
		
		if(actionState==2)
		{
			if(targetActionObject.teachersDesk!=null)
			{
				int distance = CHelper.getEuclidianDistance(targetActionObject, targetActionObject.teachersDesk);
				
				//Human Actionanim
				if(deltaTimer3>=actionTemp_Float1 && deltaTimer3<=actionTemp_Float2)
				{
					baseWorldObject.actionanim1=15;
				}
				if(deltaTimer3>=actionTemp_Float3)
				{
					actionTemp_Float1=rand.nextInt(100);
					actionTemp_Float2=100+rand.nextInt(100);
					actionTemp_Float3=300+rand.nextInt(100);
					
					int b1 = rand.nextInt(2);
					if(b1==0)
						baseWorldObject.actionanim1=8;
					else if(b1==1)
						baseWorldObject.actionanim1=2;
					else
						baseWorldObject.actionanim1=10;
					
					deltaTimer3=0;
				}
				
				//Schulstundenabschnitte
				if(deltaTimer2>3400)
				{
					deltaTimer2=0;
					actionTemp_Float1=distance;
					handleActionValueBonus(1);
				}
				
				//Papierflieger usw
				if(itype==0) //School
				{
					int deltathrow=10000;
					
					deltathrow=800-distance;
					if(deltathrow<10)
						deltathrow=10;
					
					if(deltaTimer>deltathrow)
					{
						int targetX=rand.nextInt(600);
						int targetY=rand.nextInt(600);
						
						if(targetX<300)
						{
							targetX*=-1;
						}
						else
							targetX/=2;
						
						if(targetY<300)
						{
							targetY*=-1;
						}
						else
							targetX/=2;
						
						targetX=baseWorldObject.pos_x()+targetX;
						targetY=baseWorldObject.pos_y()+targetY;
						
						SpriteMoveEventType texturetype = SpriteMoveEventType.PAPERFLYER;
						Boolean btype = rand.nextBoolean();
						if(btype)
							texturetype = SpriteMoveEventType.PAPERBALL;
						
						baseWorldObject.town.gameWorld.spriteMoveEvents.add(new CSpriteMoveEvent(texturetype, baseWorldObject.pos_x(), baseWorldObject.pos_y(), targetX, targetY, baseWorldObject.town, 1));
						
						deltaTimer=0;
					}
				}
			}
		}
	}
	
	//***************************
	//Residential/Private Actions
	//***************************
	private void doAction_WatchTV()
	{
		//targetActionObject	tv
		//targetActionObject2	couch, armchair
		
		if(actionMode==ActionMode.WATCH_TV)
		{
			CWorldObject tv = targetActionObject;
			CWorldObject seat = targetActionObject2;
			
			if(!tv.isActiveByEnergyConsumption())
			{
				baseWorldObject.cancelAction1();
				//parentWorldObject.cancelAction2();
				return;
			}
			
			if(actionState==0) //goto tv
			{
				if(seat.bObjMoving)
				{
					bActionMode=false;
					return;
				}
				
				if(tv.tempcount>0) //tv läuft schon
				{
					if(!reserveObject(seat))
					{
						bActionMode=false;
						return;
					}
					
					gotoObject(seat, true);
					tv.tempcount++;
					actionState=3;
					
					return;
				}
				
				gotoObject(tv, true);
				actionState=1;
				return;
			}
			
			if(actionState==1)
			{
				if(tv.tempcount>0) //jemand hat in der zwischenzeit tv angeschalten
				{					
					if(!reserveObject(seat))
					{
						bActionMode=false;
						return;
					}
					
					gotoObject(seat, true);
					tv.tempcount++;
					actionState=3;
					return;
				}
				
				if(gotoObject_Arrived(tv, tv.width/2, (int)town.getSizeValue(-50), tv.rotation()+180, true))
				{
					actionState=2;
					deltaTimer=0;
					return;
				}
			}

			if(actionState==2) //tv einschalten
			{
				if(deltaTimer>5)
				{
					if(!reserveObject(seat))
					{
						bActionMode=false;
						return;
					}
					
					gotoObject(seat, true);
					tv.tempcount++;
					actionState=3;
				}
			}
			
			if(actionState==3) //am seat
			{
				//Armchair
				int movex=seat.width/2;
				int movey=25;
				
				Boolean bCancel=true;
				
				if(seat.theobject.editoraction.contains("_armchair"))
				{
					if(seat.isOccupiedBy!=null && seat.isOccupiedBy.uniqueId==baseWorldObject.uniqueId)
						bCancel=false;
				}
				
				//Couch
				if(seat.theobject.editoraction.contains("_couch"))
				{
					if(seat.isOccupiedBy!=null && seat.isOccupiedBy.uniqueId==baseWorldObject.uniqueId)
					{
						bCancel=false;
						movex=seat.width/4;
						movey=(int)town.getSizeValue(25);
					}
					
					if(seat.isOccupiedByExtern!=null && seat.isOccupiedByExtern.uniqueId==baseWorldObject.uniqueId)
					{
						bCancel=false;
						movex=seat.width-seat.width/3;
						movey=(int)town.getSizeValue(25);
					}
				}
				
				if(bCancel)
				{
					bActionMode=false;
					baseWorldObject.resetPathFinding();
					return;
				}
				
				if(gotoObject_Arrived(seat, movex, movey, seat.rotation(), true))
				{
					actionTemp_Float1=rand.nextInt(3);
					if(actionTemp_Float1==0)
						actionTemp_Float1=22;
					if(actionTemp_Float1==1)
						actionTemp_Float1=2;
					if(actionTemp_Float1==2)
						actionTemp_Float1=3;
					//baseWorldObject.actionanim1=22;
					//baseWorldObject.actionanim1=2;
					//baseWorldObject.actionanim1=3;
					
					actionState=4;
					deltaTimer=0;
					return;
				}
			}
			
			if(actionState==4) //watching tv
			{
				baseWorldObject.actionanim2=4;
				baseWorldObject.actionanim1=actionTemp_Float1;
								
				if(seat.bObjMoving || tv.bObjMoving)
				{
					bActionMode=false;
					releaseObject(seat);
					baseWorldObject.actionanim2=0;
					return;
				}
				
				if(deltaTimer>3100)
				{
					baseWorldObject.actionanim2=0;
					releaseObject(seat);
					
					handleActionValueBonus(1);
					
					if(tv.tempcount==1)
					{
						gotoObject(tv, true);
						actionState=5;
						return;
					}
					
					if(tv.tempcount>0)
						tv.tempcount--;
					
					bActionMode=false;
					return;
				}
			}
			
			if(actionState==5) //tv
			{
				if(gotoObject_Arrived(tv, tv.width/2, (int)town.getSizeValue(-40), tv.rotation()+180, true))
				{
					actionState=6;
					deltaTimer=0;
					return;
				}
			}
			
			if(actionState==6) //tv ausschalten
			{
				if(deltaTimer>5)
				{
					if(tv.tempcount>0)
						tv.tempcount--;
					
					bActionMode=false;
				}
			}
		}
	}
	private void doAction_ReadBook()
	{
		//targetActionObject	bookshelf
		//targetActionObject2	couch, armchair
		
		if(actionMode==ActionMode.READ_BOOK)
		{
			CWorldObject bookshelf = targetActionObject;
			CWorldObject seat = targetActionObject2;
			
			if(actionState==0) //goto bookshelf
			{
				if(!reserveObject(bookshelf))
				{
					bActionMode=false;
					return;
				}
				
				gotoObject(bookshelf, true);
				
				actionState=1;
				return;
			}
			
			if(actionState==1)
			{
				//if(actionMode==ActionMode.READ_BOOK)
				//	Gdx.app.debug("debugbeam", ""+baseWorldObject.thehuman.getName() + ", x: " + baseWorldObject.pos_x() + ", y: " + baseWorldObject.pos_y());
				
				if(gotoObject_Arrived(bookshelf, bookshelf.width/2, (int)town.getSizeValue(-50), bookshelf.rotation()+180, true))
				{
					actionState=2;
					deltaTimer=0;
					return;
				}
			}
			
			if(actionState==2) //buch aussuchen
			{
				if(cancelIfDark())
				{
					releaseObject(bookshelf);
					return;
				}
				
				if(deltaTimer>30)
				{
					releaseObject(bookshelf);
										
					if(reserveObject(seat))
					{
						gotoObject(seat, true);
						actionState=3;
						baseWorldObject.actionanim1=23;
					}
				}
			}
			
			if(actionState==3) //am seat
			{
				//Armchair
				int movex=seat.width/2;
				int movey=(int)town.getSizeValue(45);
				
				//Couch
				if(seat.theobject.editoraction.contains("couch") && seat.isOccupiedBy!=null && seat.isOccupiedBy.uniqueId==baseWorldObject.uniqueId)
				{
					movex=seat.width/4;
					movey=(int)town.getSizeValue(45);
				}
				
				if(seat.theobject.editoraction.contains("couch") && seat.isOccupiedByExtern!=null && seat.isOccupiedByExtern.uniqueId==baseWorldObject.uniqueId)
				{
					movex=seat.width-seat.width/3;
					movey=(int)town.getSizeValue(45);
				}
				
				if(gotoObject_Arrived(seat, movex, movey, seat.rotation(), true))
				{
					actionState=4;
					deltaTimer=0;
					return;
				}
			}
			
			if(actionState==4) //buch lesen
			{
				//parentWorldObject.actionanim2=4;
				
				if(cancelIfDark())
				{
					releaseObject(seat);
					baseWorldObject.actionanim1=0;
					return;
				}
				
				if(deltaTimer>3100)
				{
					releaseObject(seat);
										
					if(reserveObject(bookshelf))
					{
						handleActionValueBonus(1);
						gotoObject(bookshelf, true);
						//parentWorldObject.actionanim2=0;
						actionState=5;
					}
				}
			}
			
			if(actionState==5) //bookshelf
			{
				if(gotoObject_Arrived(bookshelf, bookshelf.width/2, (int)town.getSizeValue(-40), bookshelf.rotation()+180, true))
				{
					actionState=6;
					deltaTimer=0;
					return;
				}
			}
			
			if(actionState==6) //buch zurück
			{
				if(deltaTimer>10)
				{
					bookshelf.tempcount=0;
					releaseObject(bookshelf);
					bActionMode=false;
					baseWorldObject.actionanim1=0;
				}
			}
		}
	}
	private void doAction_WashDishes()
	{
		//targetActionObject	Dinnertable
		//targetActionObject2	Sink
		//targetActionObject3	Cupboard
		
		if(actionMode==ActionMode.WASH_DISHES)
		{
			CWorldObject dinnerTable = targetActionObject;
			CWorldObject sink = targetActionObject2;
			CWorldObject cupboard = targetActionObject3;
			
			if(actionState==0) //goto dinnertable
			{
				if(!reserveObject(dinnerTable))
				{
					bActionMode=false;
					return;
				}
				
				gotoObject(dinnerTable, true);
				actionState=1;
				return;
			}
			
			if(actionState==1)
			{
				if(gotoObject_Arrived(dinnerTable, 0))
				{
					actionState=2;
					deltaTimer=0;
					return;
				}
			}

			if(actionState==2) //abräumen
			{
				int delta=8;
				if(baseWorldObject.thehuman.bIsDark)
					delta=600;
					
				if(deltaTimer>delta)
				{
					dinnerTable.tempcount=0;
					dinnerTable.actionvar1=0;
					dinnerTable.actionvar2=0;
					dinnerTable.actionvar3=0;
					dinnerTable.actionvar4=0;
					dinnerTable.actionvar5=0;
					dinnerTable.actionvar6=0;
					
					releaseObject(dinnerTable);
										
					if(reserveObject(sink))
					{
						gotoObject(sink, true, sink.pos_x()+sink.width/2, sink.pos_y());
						actionState=3;
					}
				}
			}
			
			if(actionState==3) //am sink angekommen
			{
				if(gotoObject_Arrived(sink, (int)town.getSizeValue(54), (int)town.getSizeValue(-5), sink.rotation()+180, true))
				{
					actionState=4;
					deltaTimer=0;
					return;
				}
			}
			
			if(actionState==4) //abwaschen
			{
				int icount = dinnerTable.getOwnerCount();
				
				int delta=0;
				if(baseWorldObject.thehuman.bIsDark)
					delta=200;
				
				sink.tempcount=1;
				
				
				if(deltaTimer>60+delta)
					baseWorldObject.setRotation(sink.rotation()+180-45);
				if(deltaTimer>80+delta)
					baseWorldObject.setRotation(sink.rotation()+180);
				if(deltaTimer>120+delta)
					baseWorldObject.setRotation(sink.rotation()+180-45);
				if(deltaTimer>140+delta)
					baseWorldObject.setRotation(sink.rotation()+180);
				if(deltaTimer>180+delta)
					baseWorldObject.setRotation(sink.rotation()+180-45);
				if(deltaTimer>200+delta)
					baseWorldObject.setRotation(sink.rotation()+180);
				if(deltaTimer>240+delta)
					baseWorldObject.setRotation(sink.rotation()+180-45);
				if(deltaTimer>260+delta)
					baseWorldObject.setRotation(sink.rotation()+180);
				
				
				if(deltaTimer>60+delta)
					sink.tempcount=2;
				if(deltaTimer>120+delta*2)
					sink.tempcount=3;
				if(deltaTimer>180+delta*3)
					sink.tempcount=4;
				if(deltaTimer>240+delta*4)
					sink.tempcount=5;
				
				if(deltaTimer>icount*80)
				{
					if(reserveObject(cupboard))
					{
						sink.tempcount=0;
						releaseObject(sink);
						
						gotoObject(cupboard, true);
						actionState=5;
					}
				}
			}
			
			if(actionState==5) //am schrank angekommen
			{
				if(gotoObject_Arrived(cupboard, (int)town.getSizeValue(60), (int)town.getSizeValue(10), cupboard.rotation()+180, true))
				{
					cupboard.doObjectAction=true;
					actionState=6;
					deltaTimer=0;
					return;
				}
			}
			
			if(actionState==6) //geschirr einräumen
			{
				int delta=20;
				if(baseWorldObject.thehuman.bIsDark)
					delta=600;
				
				if(deltaTimer>delta)
				{
					releaseObject(cupboard);
					cupboard.doObjectAction=false;
					//resetTargetActionObjects();
					bActionMode=false;
				}
			}
		}
	}
	private void doAction_EatDinner()
	{
		if(actionMode==ActionMode.EAT_DINNER)
		{
			CWorldObject dinnerTable = targetActionObject;
			int ownerNumber=-1;
			if(dinnerTable.owner!=null && dinnerTable.owner.uniqueId==baseWorldObject.uniqueId)
				ownerNumber=1;
			if(dinnerTable.owner2!=null && dinnerTable.owner2.uniqueId==baseWorldObject.uniqueId)
				ownerNumber=2;
			if(dinnerTable.owner3!=null && dinnerTable.owner3.uniqueId==baseWorldObject.uniqueId)
				ownerNumber=3;
			if(dinnerTable.owner4!=null && dinnerTable.owner4.uniqueId==baseWorldObject.uniqueId)
				ownerNumber=4;
			if(dinnerTable.owner5!=null && dinnerTable.owner5.uniqueId==baseWorldObject.uniqueId)
				ownerNumber=5;
			if(dinnerTable.owner6!=null && dinnerTable.owner6.uniqueId==baseWorldObject.uniqueId)
				ownerNumber=6;
			if(dinnerTable.owner7!=null && dinnerTable.owner7.uniqueId==baseWorldObject.uniqueId)
				ownerNumber=7;
			if(dinnerTable.owner8!=null && dinnerTable.owner8.uniqueId==baseWorldObject.uniqueId)
				ownerNumber=8;
			
			if(actionState==0)
			{
				gotoObject(dinnerTable, true, dinnerTable.pos_x(), dinnerTable.pos_y());
				actionState=1;
				return;
			}
			
			if(actionState==1)
			{
				int movex=0;
				int movey=0;
				float rotation=0;
			
				int seatcount=4;
				if(dinnerTable.theobject.editoraction.contains("_count6"))
					seatcount=6;
				
				if(dinnerTable.theobject.editoraction.contains("_count8"))
					seatcount=8;
				
				int movx=0;
				if(seatcount==4)
					movx=(int)town.getSizeValue(-90);
				
				int movx_dreiVier=0;
				int movx_5Bis8=0;
				if(seatcount==8)
				{
					movx=(int)town.getSizeValue(-80);
					movx_dreiVier=(int)town.getSizeValue(10);
					movx_5Bis8=(int)town.getSizeValue(240);
				}
				
				//if(seatcount==8)
				//{
				//}
				//else
				{
					if(ownerNumber==1) //links oben
					{
						rotation=dinnerTable.rotation();
						movey=(int)(dinnerTable.height)-(int)town.getSizeValue(50)-(int)town.getSizeValue(22)+(int)town.getSizeValue(5);
						movex=(int)town.getSizeValue(76+100-5)+movx;
					}
					
					if(ownerNumber==2) //links unten
					{
						rotation=dinnerTable.rotation()+180;
						movey=(int)(dinnerTable.height)-(int)town.getSizeValue(195)-(int)town.getSizeValue(50)-(int)town.getSizeValue(5);
						movex=(int)town.getSizeValue(76+100-5)+movx;
					}
					
					if(ownerNumber==3) //rechts oben
					{
						rotation=dinnerTable.rotation();
						movey=(int)(dinnerTable.height)-(int)town.getSizeValue(50)-(int)town.getSizeValue(22)+(int)town.getSizeValue(5);
						movex=(int)town.getSizeValue(180+100-5)+movx+movx_dreiVier;
					}
					
					if(ownerNumber==4) //rechts unten
					{
						rotation=dinnerTable.rotation()+180;
						movey=(int)(dinnerTable.height)-(int)town.getSizeValue(195)-(int)town.getSizeValue(50)-(int)town.getSizeValue(5);
						movex=(int)town.getSizeValue(180+100-5)+movx+movx_dreiVier;
					}
					
					if(seatcount==8)
					{
						if(ownerNumber==5) //links oben
						{
							rotation=dinnerTable.rotation();
							movey=(int)(dinnerTable.height)-(int)town.getSizeValue(50)-(int)town.getSizeValue(22)+(int)town.getSizeValue(5);
							movex=(int)town.getSizeValue(76+100-5)+movx+movx_5Bis8;
						}
						
						if(ownerNumber==6) //links unten
						{
							rotation=dinnerTable.rotation()+180;
							movey=(int)(dinnerTable.height)-(int)town.getSizeValue(195)-(int)town.getSizeValue(50)-(int)town.getSizeValue(5);
							movex=(int)town.getSizeValue(76+100-5)+movx+movx_5Bis8;
						}
						
						if(ownerNumber==7) //rechts oben
						{
							rotation=dinnerTable.rotation();
							movey=(int)(dinnerTable.height)-(int)town.getSizeValue(50)-(int)town.getSizeValue(22)+(int)town.getSizeValue(5);
							movex=(int)town.getSizeValue(180+100-5)+movx+movx_dreiVier+movx_5Bis8;
						}
						
						if(ownerNumber==8) //rechts unten
						{
							rotation=dinnerTable.rotation()+180;
							movey=(int)(dinnerTable.height)-(int)town.getSizeValue(195)-(int)town.getSizeValue(50)-(int)town.getSizeValue(5);
							movex=(int)town.getSizeValue(180+100-5)+movx+movx_dreiVier+movx_5Bis8;
						}						
					}
										
					if(seatcount==6)
					{
						if(ownerNumber==5) //links
						{
							rotation=dinnerTable.rotation()+90;
							movey=(int)(dinnerTable.height)-(int)town.getSizeValue(50)-(int)town.getSizeValue(22)+(int)town.getSizeValue(5)-(int)town.getSizeValue(100)+(int)town.getSizeValue(5);
							movex=(int)town.getSizeValue(76-5);
						}
						
						if(ownerNumber==6) //rechts
						{
							rotation=dinnerTable.rotation()+270;
							movey=(int)(dinnerTable.height)-(int)town.getSizeValue(50)-(int)town.getSizeValue(22)+(int)town.getSizeValue(5)-(int)town.getSizeValue(100)+(int)town.getSizeValue(10);
							movex=(int)town.getSizeValue(180+100-5+90);
						}
					}
				}
				
				if(gotoObject_Arrived(dinnerTable, movex, movey, rotation, true))
				{
					actionState=2;
					deltaTimer=0;
					return;
				}
			}
			
			if(actionState==2)
			{
				int delta=200;
				if(baseWorldObject.thehuman.bIsDark)
					delta=500;
				
				if(deltaTimer>delta)
				{
					deltaTimer=0;
					//get correct actionvar
					//	1	3
					//5		  6
					//	2	4
					
					int platefilling=-1;
					
					Boolean bAte=false;
					
					if(dinnerTable.actionvar1>0 && dinnerTable.owner!=null && baseWorldObject.uniqueId==dinnerTable.owner.uniqueId)
					{
						bAte=true;
						dinnerTable.actionvar1--;
						platefilling=(int) dinnerTable.actionvar1;
					}
					
					if(dinnerTable.actionvar2>0 && dinnerTable.owner2!=null && baseWorldObject.uniqueId==dinnerTable.owner2.uniqueId)
					{
						bAte=true;
						dinnerTable.actionvar2--;
						platefilling=(int)dinnerTable.actionvar2;
					}

					if(dinnerTable.actionvar3>0 && dinnerTable.owner3!=null && baseWorldObject.uniqueId==dinnerTable.owner3.uniqueId)
					{
						bAte=true;
						dinnerTable.actionvar3--;
						platefilling=(int)dinnerTable.actionvar3;
					}

					if(dinnerTable.actionvar4>0 && dinnerTable.owner4!=null && baseWorldObject.uniqueId==dinnerTable.owner4.uniqueId)
					{
						bAte=true;
						dinnerTable.actionvar4--;
						platefilling=(int)dinnerTable.actionvar4;
					}

					if(dinnerTable.actionvar5>0 && dinnerTable.owner5!=null && baseWorldObject.uniqueId==dinnerTable.owner5.uniqueId)
					{
						bAte=true;
						dinnerTable.actionvar5--;
						platefilling=(int)dinnerTable.actionvar5;
					}
					
					if(dinnerTable.actionvar6>0 && dinnerTable.owner6!=null && baseWorldObject.uniqueId==dinnerTable.owner6.uniqueId)
					{
						bAte=true;
						dinnerTable.actionvar6--;
						platefilling=(int)dinnerTable.actionvar6;
					}
					
					if(dinnerTable.actionvar7>0 && dinnerTable.owner7!=null && baseWorldObject.uniqueId==dinnerTable.owner7.uniqueId)
					{
						bAte=true;
						dinnerTable.actionvar7--;
						platefilling=(int)dinnerTable.actionvar7;
					}					
					
					if(dinnerTable.actionvar8>0 && dinnerTable.owner8!=null && baseWorldObject.uniqueId==dinnerTable.owner8.uniqueId)
					{
						bAte=true;
						dinnerTable.actionvar8--;
						platefilling=(int)dinnerTable.actionvar8;
					}					
										
					if(platefilling<1)
					{
						if(bAte)
						{
							handleActionValueBonus(1);
						}
						
						resetActionObjects();
						bActionMode=false;
						
						iActionBlocker=iActionBlocker_default; //zucken des residents verhindern (beenden und neu startet der action)
						return;
					}
				}
			}
		}
	}
	private void doAction_CookDinner()
	{
		if(actionMode==ActionMode.COOK_DINNER)
		{
			CWorldObject dinnerTable = targetActionObject;
			CWorldObject fridge = targetActionObject2;
			CWorldObject stove = targetActionObject3;
			CWorldObject cupboard = targetActionObject4;
			CWorldObject sink = targetActionObject5;
			
			if(actionState==-3)
			{
				if(dinnerTable.actiontimenr==1)
					dinnerTable.actiontime1check=true;
				if(dinnerTable.actiontimenr==2)
					dinnerTable.actiontime2check=true;
				if(dinnerTable.actiontimenr==3)
					dinnerTable.actiontime3check=true;
				
				baseWorldObject.objectFilling=0; //food filling der pfanne
				dinnerTable.tempcount=0; //Show Plates
				dinnerTable.actionvar1=6; //Init Plate Food Filling
				dinnerTable.actionvar2=6;
				dinnerTable.actionvar3=6;
				dinnerTable.actionvar4=6;
				
				if(dinnerTable.getOwnerCount()==6)
				{
					dinnerTable.actionvar5=6;
					dinnerTable.actionvar6=6;
				}
				
				if(dinnerTable.getOwnerCount()==8)
				{
					dinnerTable.actionvar7=6;
					dinnerTable.actionvar8=6;
				}
				
				if(reserveObject(cupboard))
				{
					if(gotoObject(cupboard, true))
					{
						actionState=-2;
						return;
					}
				}
			}
			
			if(actionState==-2)
			{
				//Am Cupboard angekommen
				if(gotoObject_Arrived(cupboard, (int)town.getSizeValue(60), (int)town.getSizeValue(10), cupboard.rotation()+180, true))
				{
					//					if(cancelIfDark())
					//					{
					//						releaseObject(cupboard);
					//						return;
					//					}
					
					deltaTimer=0;
					actionState=-1;
					cupboard.doObjectAction=true;
				}
			}
			
			if(actionState==-1)
			{
				//Schranktür auf, Schranktür zu, weiter
				int delta=8;
				if(baseWorldObject.thehuman.bIsDark)
					delta=400;
				
				if(deltaTimer>delta)
				{
					actionState=0;
					releaseObject(cupboard);
					cupboard.doObjectAction=false;
					deltaTimer=0;
				}
			}
			
			if(actionState==0)
			{
				//if(fridge.isOccupiedBy!=null && fridge.isOccupiedBy.activeAction != null && fridge.isOccupiedBy.activeAction.actionMode==ActionMode.FRIDGE)
				//	fridge.isOccupiedBy.cancelAction();
				//fridge action gibts nicht mehr
				
				if(fridge.isOccupiedBy!=null && fridge.isOccupiedBy.uniqueId!=baseWorldObject.uniqueId && deltaTimer<800) //max 30 minuten warten dann einfach hingehen
				{
					//bActionMode=false;
					return;
				}
				
				if(fridge.getObjectFillingMulti()>0)
				{
					fridge.isOccupiedBy=baseWorldObject;
					
					if(gotoObject(fridge, true))
					{
						actionState=1;
						return;
					}
				}
				else
				{
					bActionMode=false;
					return;
				}
			}
			
			if(actionState==1)
			{
				//Am Fridge angekommen
				//if(gotoObject_Arrived(fridge, 20, -60, fridge.rotation()+90, true))
				if(gotoObject_Arrived(fridge, (int)town.getSizeValue(60), (int)town.getSizeValue(-23), fridge.rotation()+90, true))
				{					
					deltaTimer=0;
					actionState=2;					
				}
			}
			
			if(actionState==2)
			{
				fridge.doObjectAction=true; //Kühlschrank auf
				actionState=3;
				deltaTimer=0;
				deltaTimer2=0;
				return;
			}
			
			if(actionState==3) //Zutaten in den Kochtopf/Pfanne
			{
				int deltavalue=10;
				if(baseWorldObject.thehuman.bIsDark)
					deltavalue=500;
				
				if(deltaTimer2>6)
				{
					deltaTimer2=0;
					
					if(baseWorldObject.rotation()==fridge.rotation()+140)
						baseWorldObject.setRotation(fridge.rotation()+139);
					else
						baseWorldObject.setRotation(fridge.rotation()+140);
				}
				
				if(baseWorldObject.objectFilling<=3)
				{
					if(deltaTimer>deltavalue)
					{
						deltaTimer=0;
						baseWorldObject.objectFilling++;
					}
				}
				else
				{
					int objCount=dinnerTable.getOwnerCount()*4;
					int fval = fridge.objectFillingMulti.get(0);
					fval-=objCount;
					fridge.objectFillingMulti.put(0, fval);
					
					if(fridge.getObjectFillingMulti()<0)
						fridge.objectFillingMulti.put(0, 0);
					
					fridge.doObjectAction=false;
					releaseObject(fridge);
					
					//Create Garbage Objects
					int gcount=dinnerTable.getOwnerCount();
					for(int i=0;i<gcount;i++)
					{
						town.gameWorld.gameResourceConfig.createWorldObject("recyclingcenter_garbagebag", baseWorldObject.pos_x(), baseWorldObject.pos_y(), fridge.theaddress);
					}
					
					deltaTimer=0;
					actionState=4;
				}
				
				return;
			}
			
			if(actionState==4) //Zum Herd gehen
			{
				if(deltaTimer>1800) //max 30 min warten
				{
					bActionMode=false;
					return;
				}
				
				if(reserveObject(stove))
				{
					if(gotoObject(stove, true))
					{
						actionState=5;
						deltaTimer=0;
						return;
					}
				}
			}
			
			if(actionState==5) //Am Herd angekommen
			{
				if(gotoObject_Arrived(stove, (int)town.getSizeValue(55), (int)town.getSizeValue(-40), stove.rotation()+180, true))
				{
					//if(cancelIfDark())
					//if(parentWorldObject.thehuman.checkIsDark())
					//{
					//	releaseObject(stove);
					//	return;
					//}
					
					stove.x_temp=baseWorldObject.pos_x();
					stove.y_temp=baseWorldObject.pos_y();
					stove.rotation_temp=baseWorldObject.rotation();
					
					deltaTimer=0;
					actionState=6;
				}
			}
			
			if(actionState==6)
			{
				if(deltaTimer>1800) //max 30 min warten
				{
					releaseObject(stove);
					bActionMode=false;
					return;
				}
				
				//Gehe zu Cupboard, Pfanne bleibt auf dem Herd
				if(reserveObject(cupboard))
				{
					if(gotoObject(cupboard, true))
					{
						actionState=7;
						deltaTimer=0;
					}
				}
				
				return;
			}
			
			if(actionState==7) //Am Schrank angekommen
			{
				if(gotoObject_Arrived(cupboard, (int)town.getSizeValue(60), (int)town.getSizeValue(10), cupboard.rotation()+180, true))
				{
					//					if(cancelIfDark())
					//					{
					//						releaseObject(cupboard);
					//						return;
					//					}
					
					deltaTimer=0;
					
					actionState=8;
					cupboard.doObjectAction=true;
					return;
				}
			}
			
			if(actionState==8) //Schranktür auf, Schranktür zu, weiter
			{
				int delta=8;
				if(baseWorldObject.thehuman.bIsDark)
					delta=400;
					
				if(deltaTimer>delta)
				{
					cupboard.doObjectAction=false;
					releaseObject(cupboard);
					
					actionState=9;
					deltaTimer=0;
					//gotoObject(dinnerTable, true, dinnerTable.pos_x(), dinnerTable.pos_y());
					gotoObject(dinnerTable, true);
					return;
				}
			}
			
			if(actionState==9) 
			{
				if(gotoObject_Arrived(dinnerTable, -1, -1, -1, true)) //am Dinnertable angekommen
				{
					actionState=10;
					deltaTimer=0;

				}
			}
			
			if(actionState==10) //verteile Teller
			{
				int delta=50;
				if(baseWorldObject.thehuman.bIsDark)
					delta=400;
				
				if(deltaTimer>delta)
				{
					dinnerTable.tempcount=1;
					actionState=11;
					deltaTimer=0;
				}
			}
			
			if(actionState==11)
			{
				gotoObject(stove, true);
				actionState=12;
				deltaTimer=0;
				return;
			}
			
			if(actionState==12) //Am Herd fertig kochen
			{
				if(gotoObject_Arrived(stove, (int)town.getSizeValue(55), (int)town.getSizeValue(-40), stove.rotation()+180, true))
				{
					int delay=100;
					if(baseWorldObject.thehuman.bIsDark)
						delay=420;
					if(deltaTimer>delay*dinnerTable.getOwnerCount())
					{
						releaseObject(stove);
						//gotoObject(dinnerTable, true, dinnerTable.pos_x(), dinnerTable.pos_y());
						gotoObject(dinnerTable, true);
						actionState=13;
					}
				}
			}
			
			if(actionState==13)
			{
				if(gotoObject_Arrived(dinnerTable, -1, -1, -1, true))
				{
					actionState=14;
					deltaTimer=0;
				}
			}
			
			if(actionState==14)
			{
				int delta=10;
				if(baseWorldObject.thehuman.bIsDark)
					delta=450;
				
				if(deltaTimer>delta)
				{
					//Teller befüllen
					dinnerTable.tempcount=2;

					//Increase Skill Level
					increaseTaskSkillLevel(dinnerTable);
										
					resetActionObjects();
					bActionMode=false;
				}
			}
		}
	}
	private void doAction_Toilet()
	{
		//Toilet -> Folgeaction=Sink
		if(actionMode==ActionMode.TOILET)
		{
			if(getActionValue()<10)
				setActionValue(1, MathType.SET); //Action nicht automatisch beenden
			
			if(deltaTimer>3600)
				deltaTimer=0;
			
			CWorldObject toilet = targetActionObject;
			CWorldObject sink = targetActionObject2;
			
			if(actionState==0)
			{
				gotoObjectXY(toilet, toilet.width/2+toilet.theobject.ObjectAction_Move_Pixels_X, toilet.height/2+toilet.theobject.ObjectAction_Move_Pixels_Y-(int) town.getSizeValue(100));
				actionState=1;
				return;
			}
			
			if(actionState==1)
			{
				//An Toilet angekommen
				//Gdx.app.debug("", "rot: "+toilet.theobject.ObjectAction_Rotation + ", movx: " + toilet.theobject.ObjectAction_Move_Pixels_X + ", movy: " + toilet.theobject.ObjectAction_Move_Pixels_Y);
				//if(gotoObject_Arrived(toilet, toilet.width/2+toilet.theobject.ObjectAction_Move_Pixels_X, toilet.height/2+toilet.theobject.ObjectAction_Move_Pixels_Y, toilet.theobject.ObjectAction_Rotation, true))
				//if(gotoObject_Arrived(toilet, 1))
				if(baseWorldObject.ziel_x==-1)
				{
					baseWorldObject.resetPathFinding();
					
					actionState=2;
					deltaTimer=0;
					
					toilet.doObjectAction=true;
					if(toilet.theobject.editoraction.contains("urinal"))
					{
						moveHumanByObjectRotation(baseWorldObject, toilet, toilet.width/2+toilet.theobject.ObjectAction_Move_Pixels_X, toilet.height/2+toilet.theobject.ObjectAction_Move_Pixels_Y, toilet.rotation()+toilet.theobject.ObjectAction_Rotation);
						baseWorldObject.actionanim2=0;
						baseWorldObject.actionanim1=23;
						actionState=3;
					}
				}
				
				return;
			}

			if(actionState==2)
			{
				if(deltaTimer>40)
				{
					//if(gotoObject_Arrived(toilet, toilet.width/2+toilet.theobject.ObjectAction_Move_Pixels_X, toilet.height/2+toilet.theobject.ObjectAction_Move_Pixels_Y, toilet.rotation()+toilet.theobject.ObjectAction_Rotation, true))
					moveHumanByObjectRotation(baseWorldObject, toilet, toilet.width/2+toilet.theobject.ObjectAction_Move_Pixels_X, toilet.height/2+toilet.theobject.ObjectAction_Move_Pixels_Y, toilet.rotation()+toilet.theobject.ObjectAction_Rotation);
					baseWorldObject.actionanim2=3;
					deltaTimer=0;
					actionState=3;
				}
				
				return;
			}
			
			if(actionState==3)
			{
				baseWorldObject.thehuman.toiletValue-=CHelper.getDeltaSeconds(town);
				
				if(baseWorldObject.thehuman.toiletValue<0)
					baseWorldObject.thehuman.toiletValue=0;
				
				if(deltaTimer>12*60)
				{
					baseWorldObject.actionanim2=0;
					baseWorldObject.thehuman.toiletValue=0;
					toilet.isOccupiedBy=null;
					baseWorldObject.thehuman.cleanValue+=baseWorldObject.thehuman.cleanValueTrigger/3;
					moveHumanByObjectRotation(baseWorldObject, toilet, toilet.width/2+toilet.theobject.ObjectAction_Move_Pixels_X, toilet.height/2+toilet.theobject.ObjectAction_Move_Pixels_Y-40, toilet.rotation()+180);
					deltaTimer=0;
					actionState=4;
				}
				
				return;
			}

			if(actionState==4)
			{
				if(deltaTimer>40)
				{
					actionState=5;
					deltaTimer=0;
					toilet.doObjectAction=false;
					
					if(sink==null || sink.isOccupiedBy!=null)
					{
						bActionMode=false;
						return;
					}
					
					sink.isOccupiedBy=baseWorldObject;
					gotoObjectXY(sink, targetActionObject.width/2, 0);
				}
				
				return;
			}
			
			if(actionState==5)
			{
				if(baseWorldObject.collides(sink) || baseWorldObject.ziel_x==-1)
				//if(gotoObject_Arrived(sink, sink.width/2+sink.theobject.ObjectAction_Move_Pixels_X, sink.height/2+sink.theobject.ObjectAction_Move_Pixels_Y, sink.rotation()+sink.theobject.ObjectAction_Rotation, true))
				{
					baseWorldObject.resetPathFinding();
					Vector2 v2 = CHelper.moveHumanByObjectRotation(sink, baseWorldObject, sink.width/2, (int) town.getSizeValue(-20));
					baseWorldObject.setRotation(sink.rotation()+180);
					baseWorldObject.setPosition((int)v2.x, (int)v2.y);
					baseWorldObject.actionanim1=1;
					
					deltaTimer=0;
					actionState=6;
				}
				
				return;
			}
			
			if(actionState==6) //Achtung: bei änderung hier muss actionstate für water anim angepasst werden
			{
				if(deltaTimer>90)
				{
					baseWorldObject.thehuman.cleanValue-=baseWorldObject.thehuman.cleanValueTrigger/3;
					
					if(baseWorldObject.thehuman.cleanValue<0)
						baseWorldObject.thehuman.cleanValue=0;
					
					baseWorldObject.actionanim1=0;
					
					handleActionValueBonus(1);
					
					resetActionObjects();
					
					bActionMode=false;
				}
				
				return;
			}
		}
	}
	private void doAction_Shower()
	{
		if(actionMode!=ActionMode.SHOWER)
			return;
		
		CWorldObject shower_bath = targetActionObject;
		CWorldObject towelcabinet = targetActionObject2;
		//targetActionObject3 ist laundrybasket
		
		//if(baseWorldObject.thehuman.name.contains("Hall"))
		//	Gdx.app.debug("", "log shower 1 " + actionState);
		
		if(actionState==0)
		{
			gotoObject(shower_bath, true);
			actionState=1;
			
			return;
		}
		
		if(actionState==1)
		{
			//An Shower / Bath angekommen
			if(gotoObject_Arrived(shower_bath, shower_bath.width/2+shower_bath.theobject.ObjectAction_Move_Pixels_X, shower_bath.height/2+shower_bath.theobject.ObjectAction_Move_Pixels_Y, shower_bath.rotation()+shower_bath.theobject.ObjectAction_Rotation, true))
			{
				if(targetActionObject.theobject.editoraction.contains("shower"))
					baseWorldObject.actionanim1=20;
				else
				{
					baseWorldObject.actionanim2=3;
				}
				
				baseWorldObject.thehuman.setClothingNaked();
				
				shower_bath.actionfield1=1;
				deltaTimer=0;
				actionState=2;
				//parentWorldObject.thehuman.cleanValue=actionDuration;
				
				targetActionObject.actionfield2=60*(5+rand.nextInt(10));
				
				if(targetActionObject.theobject.editoraction.contains("bathtub"))
					targetActionObject.actionfield2=60*(10+rand.nextInt(30));
				
				return;
			}
			
			return;
		}
		
		if(actionState==2)
		{
			int duration = (int) targetActionObject.actionfield2;
			
			if(deltaTimer>duration)
			{
				//Fertig mit Waschvorgang, Gehe zu Towelschrank und trockne ab
				baseWorldObject.thehuman.cleanValue=0;
				shower_bath.isOccupiedBy=null;
				shower_bath.actionfield1 = 0;
				shower_bath.actionfield2 = 0;
				baseWorldObject.actionanim1=0;
				baseWorldObject.actionanim2=0;
				handleActionValueBonus(1);
				
				if(towelcabinet!=null && towelcabinet.objectFilling>0)
					gotoObject(towelcabinet, true);
				else
				{
					targetActionObject.isOccupiedBy=null;
					baseWorldObject.thehuman.setClothingBack();
					bActionMode=false;
					return;
				}
				
				actionState=3;
			}
			
			return;
		}
		
		if(actionState==3)
		{
			//Am Towelschrank angekommen
			if(gotoObject_Arrived(towelcabinet, 5))
			{
				baseWorldObject.actionanim1=15;
				towelcabinet.objectFilling-=1;
				deltaTimer=0;
				//parentWorldObject.inventory=towelcabinet.theobject.editoraction;
				actionState=4;
			}
			
			return;
		}
		
		if(actionState==4)
		{
			//Towel rausnehmen
			if(deltaTimer>20)
			{
				baseWorldObject.actionanim1=20;
				actionState=5;
			}
			
			return;
		}
		
		if(actionState==5)
		{
			//dry off, abtrocknen
			
			//suche laundry basket
			if(deltaTimer>60*3)
			{
				baseWorldObject.thehuman.setClothingBack();
				
				for(CWorldObject wobj : baseWorldObject.theaddress.listWorldObjects)
				{
					if(wobj.theobject.editoraction.contains("laundrybasket"))
					{
						if(wobj.isOccupiedBy==null 
								&& wobj.isOccupiedBy2==null 
								&& wobj.bDeleted==false 
								&& wobj.getObjectFillingMulti()<wobj.getObjectFillingMultiMax())
							targetActionObject3=wobj;
					}
				}
				
				CWorldObject laundry1 = town.gameWorld.gameResourceConfig.createWorldObject("laundryobject", baseWorldObject.pos_x(), baseWorldObject.pos_y(), town.gameWorld.getAddressByPoint(baseWorldObject.pos_x(),  baseWorldObject.pos_y()));
				Vector2 v2 = CHelper.moveVectorByHumanRotation(baseWorldObject, laundry1.width, laundry1.height, 40, 80);
				laundry1.setPosition((int)v2.x, (int)v2.y);
				laundry1.objectFillingMulti.put(towelcabinet.uniqueId, 1);
				targetActionObject9=laundry1;
				
				if(targetActionObject3!=null)
				{
					gotoObject(targetActionObject3, true);
					baseWorldObject.actionanim1=21;
					actionState=6;
					return;
				}
				else
				{
					baseWorldObject.actionanim1=0;
					bActionMode=false;
					return;
				}
			}
			
			return;
		}
		
		if(actionState==6)
		{
			Vector2 v2 = CHelper.moveObjectByHumanRotation(baseWorldObject, targetActionObject9, (int)town.getSizeValue(60), (int)town.getSizeValue(20));
			targetActionObject9.setPosition((int)v2.x, (int)v2.y);
			
			//Am Laundrybasket angekommen
			if(gotoObject_Arrived(targetActionObject3, 5))
			{
				deltaTimer=0;
				baseWorldObject.actionanim1=7;
				actionState=7;
			}
			
			return;
		}
		
		if(actionState==7)
		{
			//Handtuch in Laundry Basket schmeissen
			if(deltaTimer>10)
			{
				int val=1;
				if(targetActionObject3.objectFillingMulti.containsKey(towelcabinet.uniqueId))
					val+=targetActionObject3.objectFillingMulti.get(towelcabinet.uniqueId);
				
				targetActionObject3.objectFillingMulti.put(towelcabinet.uniqueId, val);
				targetActionObject3.isOccupiedBy=null;
				shower_bath.isOccupiedBy=null;
				bActionMode=false;
				town.gameGui.removeWorldObject(targetActionObject9, false);
			}
			
			return;
		}
	}
	private void doAction_Fridge()
	{
		if(actionMode==ActionMode.FRIDGE && bActionMode && !bGotoActionMode)
		{
			CWorldObject fridge = targetActionObject;
			
			if(actionState==0)
			{
				if(fridge.isOccupiedBy==null)
					fridge.isOccupiedBy=baseWorldObject;
				
				if(fridge.isOccupiedBy.uniqueId!=baseWorldObject.uniqueId)
				{
					if(deltaTimer<800)
						return;
				}
				
				if(fridge.getObjectFillingMulti()<10)
				{
					bActionMode=false;
					return;
				}
				
				fridge.isOccupiedBy=baseWorldObject;
				actionState=0.1f;
				return;
			}
			
			if(actionState==0.1f)
			{
				//if(baseWorldObject.thehuman.getName().contains("Scott"))
				//	Gdx.app.debug("debug fridge", "actionState: "+actionState + ", delta: " + deltaTimer);
				
				gotoObjectXY(fridge, fridge.width/2+fridge.theobject.ObjectAction_Move_Pixels_X, fridge.height/2+fridge.theobject.ObjectAction_Move_Pixels_Y);
				actionState=1;
				return;
			}
			
			if(actionState==1)
			{
				if(gotoObject_Arrived(fridge, fridge.width/2+fridge.theobject.ObjectAction_Move_Pixels_X, fridge.height/2+fridge.theobject.ObjectAction_Move_Pixels_Y, fridge.rotation()+fridge.theobject.ObjectAction_Rotation, true, 0))
				{
					actionState=2;
					deltaTimer=0;
					deltaTimer2=0;
					baseWorldObject.actionfield1=1+rand.nextInt(8);
					baseWorldObject.actionfield2=1;
				}
				return;
			}
			
			if(actionState==2)
			{
				fridge.doObjectAction=true;
				
				if(deltaTimer2>10)
				{
					baseWorldObject.actionfield2-=0.01f;
					
					if(baseWorldObject.actionfield2<0.3f)
						baseWorldObject.actionfield2=0.3f;

					baseWorldObject.actionanim1=20;
					
					deltaTimer2=0;
				}
				
				if(deltaTimer>400)
				{
					int eatvalue=0;
					
					if(fridge.getObjectFillingMulti()>=3)
						eatvalue=3;
					else if(fridge.getObjectFillingMulti()>=2)
						eatvalue=2;
					else if(fridge.getObjectFillingMulti()>=1)
						eatvalue=1;
					
					fridge.addObjectFillingMulti(0, -eatvalue);
					releaseObject(fridge);
					handleActionValueBonus(1);
					town.gameWorld.gameResourceConfig.createWorldObject("recyclingcenter_garbagebag", baseWorldObject.pos_x(), baseWorldObject.pos_y(), fridge.theaddress);
					bActionMode=false;
					fridge.doObjectAction=false;
					baseWorldObject.actionfield1=0;
				}
			}
		}		
	}
	
	//***********
	//Supermarket
	//***********
	private void doAction_SupermarketRefillShelf()
	{
		//targetActionObject=null;  //pallettruck
		//targetActionObject2=null; //warehouse
		//targetActionObject3=null; //Shelf
		//targetCompany=null;
		
		if(targetActionObject==null || targetActionObject.bDeleted)
		{
			resetActionObjects();
			bActionMode=false;
			return;
		}
		
		CWorldObject wareHouse = targetActionObject2;
		CWorldObject shelf = targetActionObject3;
		
		//int palletMovX=90;
		int palletMovX=(int)town.getSizeValue(60);
		int palletMovY=(int)town.getSizeValue(70);
		
		//if(actionMode == ActionMode.SUPERMARKET_REFILLSHELF)
		{
			if(deltaTimer>3600)
				deltaTimer=0;
			
			if(actionState==0.1f)
			{
				targetActionObject.x_temp=targetActionObject.pos_x();
				targetActionObject.y_temp=targetActionObject.pos_y();
				targetActionObject.rotation_temp=targetActionObject.rotation();
			}
			
			baseWorldObject.actionanim1=0;
			
			if(actionState>0.1f && (actionState<3 || actionState>=6))
			{
				baseWorldObject.actionanim1=2;
				
				//Positioniere Pallettruck vor Worker
				Vector2 v3= CHelper.moveObjectByHumanRotation(baseWorldObject, targetActionObject, (int)town.getSizeValue(57), (int)town.getSizeValue(-120));
				targetActionObject.setRotation(baseWorldObject.rotation());
				targetActionObject.setPosition((int)v3.x, (int)v3.y);
			}
			
			if(actionState==0)
			{
				gotoObject(targetActionObject, true);
				
				actionState=0.1f;
				return;
			}
						
			if(actionState==0.1f)
			{
				//Weg zum warehouse (targetActionObject2) und palette aufladen
				if(baseWorldObject.collides(targetActionObject) || baseWorldObject.ziel_x==-1)
				{
					baseWorldObject.resetPathFinding();
					
					wareHouse.isOccupiedBy=baseWorldObject;
					shelf.isOccupiedBy=baseWorldObject;
					
					gotoObject(wareHouse, true);
					//parentWorldObject.initTargetPath(wareHouse.pos_x(), wareHouse.pos_y(), false, wareHouse);
					//parentWorldObject.pathFinding(gameWorld.stateTime);
					
					if(baseWorldObject.path==null)
					{
						bActionMode=false;
						resetActionObjects();
						return;
					}
					else
					{
						actionState=1;
						return;
					}
				}
				
				return;
			}
			
			
			if(actionState==1)
			{
				//Am Warehouse angekommen
				if(baseWorldObject.collides(wareHouse) || baseWorldObject.ziel_x==-1)
				{
					baseWorldObject.resetPathFinding(); 
					
					//Palette drauf
					wareHouse.x_temp=wareHouse.pos_x();
					wareHouse.y_temp=wareHouse.pos_y();
					wareHouse.rotation_temp=wareHouse.rotation();
					moveObjectByObjectRotation(targetActionObject, wareHouse, palletMovX, palletMovY, targetActionObject.rotation());
					
//					if(wareHouse.objectFilling>=targetActionObject.getObjectFillingMax())
//					{
//						wareHouse.objectFilling-=targetActionObject.getObjectFillingMax()-targetActionObject.objectFilling;
//						targetActionObject.objectFilling=targetActionObject.getObjectFillingMax();
//					}
//					else
//					{
//						targetActionObject.objectFilling+=wareHouse.objectFilling;
//						wareHouse.objectFilling=0;
//						if(targetActionObject.objectFilling>targetActionObject.getObjectFillingMax())
//							targetActionObject.objectFilling=targetActionObject.getObjectFillingMax();
//					}
					
					//Weg zum Shelf
					gotoObjectXY(shelf, -60, 190);
					//parentWorldObject.initTargetPath(shelf.pos_x(), shelf.pos_y(), false, shelf);
					//parentWorldObject.pathFinding(gameWorld.stateTime);
					
					if(baseWorldObject.path==null)
					{
						bActionMode=false;
						resetActionObjects();
						return;
					}
					else
					{
						actionState=2;
						return;
					}
				}
			}
			
			
			if(actionState==2)
			{
				//Am Shelf angekommen
				if(baseWorldObject.collides(shelf) || baseWorldObject.ziel_x==-1)
				{
					baseWorldObject.resetPathFinding(); 
										
					//Positionieren
					Vector2 v2 = CHelper.moveHumanByObjectRotation(shelf, baseWorldObject, (int)town.getSizeValue(-60), (int)town.getSizeValue(190));
					baseWorldObject.setRotation(shelf.rotation());
					baseWorldObject.setPosition((int)v2.x, (int)v2.y);
					
					//Positioniere Truck vor Worker
					Vector2 v3= CHelper.moveObjectByHumanRotation(baseWorldObject, targetActionObject, (int)town.getSizeValue(38), (int)town.getSizeValue(-80));
					targetActionObject.setRotation(baseWorldObject.rotation());
					targetActionObject.setPosition((int)v3.x, (int)v3.y);						
					
					actionState=3;
					
					return;
				}
				
				moveObjectByObjectRotation(targetActionObject, wareHouse, palletMovX, palletMovY, targetActionObject.rotation());
			}
			
			
			if(actionState==3)
			{
				moveObjectByObjectRotation(targetActionObject, wareHouse, palletMovX, palletMovY, targetActionObject.rotation());
				
				//Human löst sich von truck, dreht sich leicht zum shelf und befüllt
				moveHumanByObjectRotation(baseWorldObject, shelf, (int)town.getSizeValue(-30), (int)town.getSizeValue(100), shelf.rotation()+60);
				//Vector2 v2 = CHelper.moveHumanByObjectRotation(shelf, parentWorldObject, -30, 100);
				//parentWorldObject.setRotation(shelf.rotation()+60);
				//parentWorldObject.setPosition((int)v2.x, (int)v2.y);
				
				deltaTimer=0;
				actionState=4;
				return;
			}
			
			
			if(actionState==4)
			{
				moveObjectByObjectRotation(targetActionObject, wareHouse, palletMovX, palletMovY, targetActionObject.rotation());
			
				baseWorldObject.actionanim1=1;
				
				//Shelf auffüllen
				//targetActionObject	//pallettruck
				//targetActionObject2	//warehouse
				//targetActionObject3	//Shelf
				if(shelf.objectFilling<shelf.getObjectFillingMax() && wareHouse.objectFilling>=10)
				{
					int delta=8;
					if(baseWorldObject.thehuman.bIsDark)
						delta=600;
					
					if(deltaTimer>delta)
					{
						if(baseWorldObject.rotation()==shelf.rotation()+60)
							baseWorldObject.setRotation(shelf.rotation()+10);
						else
							baseWorldObject.setRotation(shelf.rotation()+60);
						
						deltaTimer=0;
						
						shelf.objectFilling+=10;
						wareHouse.objectFilling-=10;
						if(wareHouse.objectFilling<10 && shelf.objectFilling<shelf.getObjectFillingMax()-wareHouse.objectFilling)
						{
							shelf.objectFilling+=wareHouse.objectFilling;
							wareHouse.objectFilling=0;
						}
						
						if(shelf.objectFilling>shelf.getObjectFillingMax())
							shelf.objectFilling=shelf.getObjectFillingMax();
					}
				}
				else
				{
					CWorldObject garbage1 = town.gameWorld.gameResourceConfig.createWorldObject("recyclingcenter_garbagebag", baseWorldObject.pos_x(), baseWorldObject.pos_y(), targetActionObject.theaddress);
					garbage1.color1 = new Color(1-rand.nextFloat()/5, 1-rand.nextFloat()/5, 1-rand.nextFloat()/5, 1f);
					garbage1.width=100+rand.nextInt(60);
					garbage1.height=100+rand.nextInt(60);
					
					//shelf.bIsOccupied=false;
					shelf.isOccupiedBy=null;
					actionState=5;
					deltaTimer=0;
					baseWorldObject.actionanim1=0;
					return;
				}
			}
						
			if(actionState==5)
			{
				moveObjectByObjectRotation(targetActionObject, wareHouse, palletMovX, palletMovY, targetActionObject.rotation());
				
				//Bringe palette zurück zum warehouse
				gotoXY(wareHouse.x_temp+wareHouse.width/2, wareHouse.y_temp+wareHouse.height/2);
				actionState=6;
				return;
			}
			
			if(actionState==6)
			{
				moveObjectByObjectRotation(targetActionObject, wareHouse, palletMovX, palletMovY, targetActionObject.rotation());
				
				//Im Warehouse angekommen
				//if(parentWorldObject.ziel_x==-1)
				if(gotoXY_Arrived(wareHouse.x_temp+wareHouse.width/2, wareHouse.y_temp+wareHouse.height/2, (int)town.getSizeValue(310)))
				{
					baseWorldObject.resetPathFinding();
					
					//Palette abladen
					wareHouse.setPosition(wareHouse.x_temp , wareHouse.y_temp);
					wareHouse.setRotation(wareHouse.rotation_temp);
					
					actionState=7;
					
					return;
				}
			}
						
			if(actionState==7)
			{
				//Bringe pallettruck zurück zum ausgangspunkt
				
				baseWorldObject.initTargetPath(targetActionObject.x_temp, targetActionObject.y_temp, false, null);
				baseWorldObject.pathFinding(town.gameWorld.stateTime);
				
				if(baseWorldObject.path==null)
				{
					bActionMode=false;
					resetActionObjects();
					
					//					targetActionObject.bIsOccupied=false;
					//					targetActionObject.bIsOccupiedByExtern=false;
					//					wareHouse.bIsOccupied=false;
					//					wareHouse.bIsOccupiedByExtern=false;
					//					shelf.bIsOccupied=false;
					//					shelf.bIsOccupiedByExtern=false;					
					//					targetActionObject=null;
					return;
				}
				else
				{
					actionState=8;
					return;
				}				
			}
			
			if(actionState==8)
			{
				if(baseWorldObject.ziel_x==-1)
				{
					//Pathfinding deaktivieren
					baseWorldObject.resetPathFinding();
					
					targetActionObject.setRotation(targetActionObject.rotation_temp);
					targetActionObject.setPosition(targetActionObject.x_temp, targetActionObject.y_temp);
					
					resetActionObjects();
					
//					targetActionObject.bIsOccupiedByExtern=false;
//					targetActionObject.bIsOccupied=false;
//					targetActionObject=null;
//					
//					wareHouse.bIsOccupied=false;
//					wareHouse.bIsOccupiedByExtern=false;
//					shelf.bIsOccupied=false;
//					shelf.bIsOccupiedByExtern=false;					
					
					bActionMode=false;
					
					return;
				}
			}
		}
	}
	private void doAction_SupermarketBuyIn()
	{
		if(actionMode == ActionMode.SUPERMARKET_BUYIN)
		{
			float maxCarryByFoot=10+(baseWorldObject.thehuman.getFitnessValue()/5);
			
			if(deltaTimer>3600)
				deltaTimer=0;
			
			int delta=15;
			int delta2=5;
			
			if(baseWorldObject.thehuman.bIsDark)
			{
				delta=140;
				delta2=20;
			}
			
			//Objekt-Zuordnungen:
			//		wagen: targetactionobject
			//		regal: targetActionObject2
			//		checkout: targetActionObject4
			//		fridge: targetActionObject3
			//		supermarket: targetCompany
			
			if(actionState==0)
			{
				//nicht prüfen ob orig koordinaten auf firma -> spieler soll auch mal aufräumen
				targetActionObject.x_temp=targetActionObject.pos_x();
				targetActionObject.y_temp=targetActionObject.pos_y();
				targetActionObject.rotation_temp=targetActionObject.rotation();
			}
			
			CWorldObject fridge=targetActionObject3;
			CWorldObject shelf=targetActionObject2; //aus save/load wieder herstellen
			CWorldObject checkout=targetActionObject4;
			
			if(actionState==0)
			{
				deltaTimer=0;
				actionState=1;
				return;
			}
			
			if(actionState==1)
			{
				baseWorldObject.walkAround(true);
				
				if(deltaTimer>1)
				{
					if(targetActionObject.getObjectFillingMulti() == fridge.getObjectFillingMultiMax() || 
							targetActionObject.getObjectFillingMulti() == targetActionObject.getObjectFillingMultiMax()) //targetActionObject.getObjectFillingMultiMax()) //Wenn Wagen voll -> direkt zur Kasse
					{
						actionState=5;
						return;
					}
					
					deltaTimer=0;
					actionState=2;
					return;
				}
			}
			
			if(actionState==2)
			{
				//Suche gefülltes Regal
				targetActionObject2 = targetCompany.supermarket_getFilledShelf();
				shelf=targetActionObject2;
				
				targetActionObject4 = targetCompany.supermarket_getOpenCheckout();
				checkout=targetActionObject4;
				
				if(checkout==null && deltaTimer<1500) //warten bis kasse frei wird
				{
					return;
				}
				
				if(shelf!=null && checkout!=null && checkout.isWorkerActive())
				{
					actionTemp_Float1=100+rand.nextInt(750);
					actionTemp_Float2=-30;
					int r1 = rand.nextInt(2);
					if(r1==0)
						actionTemp_Float2=130;
					gotoObjectXY(shelf, (int) town.getSizeValue(actionTemp_Float2), (int) town.getSizeValue(actionTemp_Float1));
					//gotoObjectXY(shelf, -30, 1000);
					
					if(baseWorldObject.path==null)
					{
						bActionMode=false;
						resetActionObjects();
						return;
					}
					else
					{
						deltaTimer=0;
						actionState=3;
						return;
					}
				}
				else
				{
					iActionBlocker=1000;
					bActionMode=false;
					resetActionObjects();
					return;
				}
			}
			
			if(actionState==3)
			{
				baseWorldObject.actionanim1=2;
				moveObjectByHumanRotation(baseWorldObject, targetActionObject, (int)town.getSizeValue(57), (int)town.getSizeValue(-50), baseWorldObject.rotation());
				
				if(shelf.objectFilling<10)
				{
					iActionBlocker=1000;
					return;
				}
				
				//Am Regal angekommen: positionieren
				if(baseWorldObject.collides(shelf) || baseWorldObject.ziel_x==-1)
				{
					baseWorldObject.resetPathFinding(); //Pathfinding deaktivieren
					
					//Kunde am Regal positionieren
					//Vector2 v2 = CHelper.moveHumanByObjectRotation(shelf, baseWorldObject, -30, 700);
					Vector2 v2 = CHelper.moveHumanByObjectRotation(shelf, baseWorldObject, (int)town.getSizeValue(actionTemp_Float2),  (int)town.getSizeValue(actionTemp_Float1));
					baseWorldObject.setRotation(shelf.rotation());
					baseWorldObject.setPosition((int)v2.x, (int)v2.y);					
					
					//Einkaufswagen positionieren
					Vector2 v2_cart = CHelper.moveObjectByHumanRotation(baseWorldObject, targetActionObject, (int)town.getSizeValue(57), (int)town.getSizeValue(-50));
					targetActionObject.setRotation(baseWorldObject.rotation());
					targetActionObject.setPosition((int)v2_cart.x, (int)v2_cart.y);
					
					//Kunde zum Regal drehen
					baseWorldObject.setRotation(shelf.rotation()+45);
					
					deltaTimer=0;
					deltaTimer2=0;
					actionState=4;
					return;
				}
			}
			
			if(actionState==4) //Befülle Einkaufswagen, leere Regal
			{
				Boolean bcar = myCarOnAdr(shelf.theaddress);
				Boolean bweiter=true;
				
				if(!bcar && targetActionObject.getObjectFillingMulti()>=maxCarryByFoot) //zu fuß max 25 tragen
					bweiter=false;
				
				if((targetActionObject.getObjectFillingMulti()<fridge.getObjectFillingMultiMax() 
						&& targetActionObject.getObjectFillingMulti()<targetActionObject.getObjectFillingMultiMax() 
						&& shelf.objectFilling>=10) 
						&& bweiter)
				{
					baseWorldObject.actionanim1=1;
					
					if(deltaTimer2>delta2)
					{
						deltaTimer2=0;
						if(baseWorldObject.rotation()==shelf.rotation()+45)
							baseWorldObject.setRotation(shelf.rotation());
						else
							baseWorldObject.setRotation(shelf.rotation()+45);
					}
					
					if(deltaTimer>delta)
					{
						deltaTimer=0;
						targetActionObject.addObjectFillingMulti(0, 10);
						shelf.objectFilling-=10;
					}
				}
				else
				{
					actionState=5;
					actionRepeater=0;
					deltaTimer=0;
					deltaTimer=2;
					
					if(targetActionObject.getObjectFillingMulti()==0) //keinen leeren einkaufswagen zur kasse und nach hause schieben
					{
						iActionBlocker=1000;
						return;
					}					
					
					return;
				}
			}
			
			if(actionState==5)
			{
				baseWorldObject.actionanim1=2;
				moveObjectByHumanRotation(baseWorldObject, targetActionObject, (int)town.getSizeValue(57), (int)town.getSizeValue(-50), baseWorldObject.rotation());
				
				//Suche besetzte/geöffnete Kasse
				targetActionObject4 = targetCompany.supermarket_getOpenCheckout();
				checkout=targetActionObject4;
				
				if(checkout!=null && checkout.isOccupiedByExtern!=null)
				{
					if(deltaTimer2<1500)
						return;
					else
						checkout=null;
				}
				
				if(checkout!=null)
				{
					checkout.objectFilling=0;
					checkout.isOccupiedByExtern=baseWorldObject;
					Vector2 v2 = CHelper.moveHumanByObjectRotation(checkout, baseWorldObject, (int)town.getSizeValue(20), (int)town.getSizeValue(-45));
					gotoXY((int)v2.x, (int)v2.y);			
					
					if(baseWorldObject.path==null)
					{
						bActionMode=false;
						resetActionObjects();
						return;
					}
					else
					{
						deltaTimer=0;
						actionState=6;
						return;
					}
				}
				else
				{
					if(deltaTimer>1)
					{
						actionRepeater++;
						deltaTimer=0;
					}
					
					if(actionRepeater>60*10) //10 Minuten warten
					{
						iActionBlocker=1000;
						bActionMode=false;
						resetActionObjects();
						return;
					}
				}
			}
			
			if(actionState==6)
			{
				baseWorldObject.actionanim1=2;
				moveObjectByHumanRotation(baseWorldObject, targetActionObject, (int)town.getSizeValue(57), (int)town.getSizeValue(-50), baseWorldObject.rotation());
				
				//An der Kasse angekommen
				if(baseWorldObject.ziel_x==-1)
				{
					if(!checkout.isWorkerActive())
					{
						checkout=null;
						actionState=5;
						return;
					}

					baseWorldObject.resetPathFinding();
					
					//Kunde an der Kasse positionieren
					moveHumanByObjectRotation(baseWorldObject, checkout, (int)town.getSizeValue(20), (int)town.getSizeValue(-45), checkout.rotation()+90);
					
					//Einkaufswagen positionieren
					moveObjectByHumanRotation(baseWorldObject, targetActionObject, (int)town.getSizeValue(57), (int)town.getSizeValue(-50), baseWorldObject.rotation());
					
					actionState=7;
					deltaTimer=0;
					deltaTimer2=0;
					return;
				}
			}
			
			if(actionState==7) //Waren auf das Band legen
			{
				if(targetActionObject.getObjectFillingMulti()>0)
				{
					baseWorldObject.actionanim1=1;
					
					if(deltaTimer2>delta2)
					{
						deltaTimer2=0;
						if(baseWorldObject.rotation()==checkout.rotation()+90)
							baseWorldObject.setRotation(checkout.rotation()+105);
						
						if(baseWorldObject.rotation()==checkout.rotation()+105)
							baseWorldObject.setRotation(checkout.rotation()+165);
						else
							baseWorldObject.setRotation(checkout.rotation()+105);
					}
					
					if(deltaTimer>delta)
					{
						deltaTimer=0;
						targetActionObject.addObjectFillingMulti(0, -10);
						checkout.objectFilling+=10;
					}
				}
				else
				{
					actionState=8;
					actionRepeater=0;
					deltaTimer=0;
					checkout.movementX=0;
					
					//Wagen ans Ende der Kasse schieben
					baseWorldObject.setRotation(baseWorldObject.rotation()-45); //zurück nach vorne schauen
					Vector2 v2 = CHelper.moveHumanByObjectRotation(checkout, baseWorldObject,(int)town.getSizeValue(400), (int)town.getSizeValue(-55));
					gotoXY((int)v2.x, (int)v2.y);
					
					return;
				}				
			}
			
			if(actionState==8) //Lebensmittel werden gescannt
			{
				baseWorldObject.actionanim1=2;
				moveObjectByHumanRotation(baseWorldObject, targetActionObject, (int)town.getSizeValue(57), (int)town.getSizeValue(-50), baseWorldObject.rotation());
				
				if(baseWorldObject.ziel_x!=-1)
					return;
				
				if(baseWorldObject.ziel_x==-1 && baseWorldObject.rotation()!=checkout.rotation()+90)
					baseWorldObject.setRotation(checkout.rotation()+90);
				
				//das band startet erst wenn ein verkäufer am checkout ist
				if(checkout.worker!=null && checkout.worker.activeActionMode==ActionMode.WORKPLACE && checkout.worker.activeAction.bActionMode==true)
				{
					if(checkout.movementX<(int)town.getSizeValue(200))
					{
						if(deltaTimer>0.1f)
						{
							checkout.movementX+=(int)town.getSizeValue(5);
							deltaTimer=0;
						}
					}
					else
					{
						actionState=9;
						actionRepeater=0;
						deltaTimer=0;
					}
				}
				else
				{
					//Action abbrechen wenn kein Verkäufer kommt
					if(deltaTimer>1)
					{
						actionRepeater++;
						deltaTimer=0;
					}
					
					if(actionRepeater>60*10) // 10 Minuten warten
					{
						deltaTimer2=0;
						iActionBlocker=1000;
						bActionMode=false;
						checkout.objectFilling=0;
						resetActionObjects();
						return;
					}					
				}
				return;
			}
			
			if(actionState==9) //Waren wieder in den Einkaufswagen
			{
				if(checkout.objectFilling>0)
				{
					baseWorldObject.actionanim1=1;
					
					if(deltaTimer2>delta2)
					{
						deltaTimer2=0;
						if(baseWorldObject.rotation()==checkout.rotation()+90)
							baseWorldObject.setRotation(checkout.rotation()+105);
						if(baseWorldObject.rotation()==checkout.rotation()+105)
							baseWorldObject.setRotation(checkout.rotation()+165);
						else
							baseWorldObject.setRotation(checkout.rotation()+105);
					}
					
					if(deltaTimer>delta)
					{
						deltaTimer=0;
						targetActionObject.addObjectFillingMulti(0, 10);
						checkout.objectFilling-=10;
						checkout.movementX+=(int)town.getSizeValue(20);
					}
				}
				else
				{
					//float fprice=Math.round(targetActionObject.getObjectFillingMulti()/1.2f)*-1;
					//gameWorld.changeTownMoney((int) fprice);
					//gameWorld.townStatistics.getCurrentStatistics_Finance().residentSpendsMoney+=Math.abs(fprice);
					//checkout.addAnimationEvent(AnimationEventType.MONEY, fprice);
					
					checkout.isOccupiedByExtern=null;
					checkout.movementX=0;
					actionState=10;
					actionRepeater=0;
					deltaTimer=0;
					baseWorldObject.setRotation(baseWorldObject.rotation()-45); //geradeaus schauen
					
					//Hat ein Auto - lade Einkauf in den Kofferraum
					if(baseWorldObject.thehuman.car != null)
					{
						CAddress carAdr = baseWorldObject.thehuman.car.getCurrentAddress();
						CAddress adr = checkout.theaddress;
						
						if(carAdr!=null && adr!=null && carAdr.addressId == adr.addressId)
						{
							//Einkauf in Kofferraum einladen
							gotoObjectXY(baseWorldObject.thehuman.car, (int) town.getSizeValue(10), baseWorldObject.thehuman.car.height+targetActionObject.width+5);
							actionState=9.1f;
							return;
						}
					}
					
					//Hat kein Auto - gehe mit dem Einkaufswagen zurück
					actionState=11.1f;
					
					return;
				}
				
				return;
			}
			
			
			if(actionState==9.1f)
			{
				//Gehe zum Auto
				baseWorldObject.actionanim1=2;
				moveObjectByHumanRotation(baseWorldObject, targetActionObject, (int)town.getSizeValue(57), (int)town.getSizeValue(-50), baseWorldObject.rotation());
				
				//Am Auto angekommen
				if(baseWorldObject.ziel_x==-1)
				{
					baseWorldObject.resetPathFinding();
					
					//Resident am Kofferraum positionieren
					moveHumanByObjectRotation(baseWorldObject, baseWorldObject.thehuman.car, (int)town.getSizeValue(38), baseWorldObject.thehuman.car.height+targetActionObject.width-(int)town.getSizeValue(60), baseWorldObject.thehuman.car.rotation()+90);
					
					//Einkaufswagen positionieren
					moveObjectByHumanRotation(baseWorldObject, targetActionObject, (int)town.getSizeValue(57), (int)town.getSizeValue(-50), baseWorldObject.rotation());
					
					deltaTimer=0;
					deltaTimer2=0;
					
					actionState=9.2f;
				}
				
				return;
			}
			
			
			if(actionState==9.2f)
			{
				baseWorldObject.actionanim1=0;
				
				//Einkauf ins Auto einräumen
				if(baseWorldObject.thehuman.car.getObjectFillingMulti() < baseWorldObject.thehuman.car.getObjectFillingMultiMax() 
						&& targetActionObject.getObjectFillingMulti()>0)
				{
					baseWorldObject.thehuman.car.actionanim1=2;
					baseWorldObject.actionanim1=1;
					
					if(deltaTimer2>delta2)
					{
						deltaTimer2=0;
						if(baseWorldObject.rotation()==baseWorldObject.thehuman.car.rotation()+90)
							baseWorldObject.setRotation(baseWorldObject.thehuman.car.rotation()+20);
						else
							baseWorldObject.setRotation(baseWorldObject.thehuman.car.rotation()+90);
					}
					
					if(deltaTimer>delta)
					{
						deltaTimer=0;
						targetActionObject.addObjectFillingMulti(0,-10);
						baseWorldObject.thehuman.car.addObjectFillingMulti(0,10);
					}
				}
				else
				{
					//Einkaufswagen zurückbringen
					baseWorldObject.thehuman.car.doObjectAction=false;
					baseWorldObject.actionanim1=0;
					baseWorldObject.thehuman.car.actionanim1=0;
					if(targetActionObject.x_temp>0 && targetActionObject.y_temp>0)
						gotoXY(targetActionObject.x_temp, targetActionObject.y_temp);
					actionState=9.3f;
				}
				
				return;
			}
			
			if(actionState==9.3f)
			{
				baseWorldObject.actionanim1=2;
				moveObjectByHumanRotation(baseWorldObject, targetActionObject, (int)town.getSizeValue(57), (int)town.getSizeValue(-50), baseWorldObject.rotation());
				
				if(baseWorldObject.ziel_x==-1)
				{
					if(targetActionObject.x_temp>0 && targetActionObject.y_temp>0)
					{
						targetActionObject.setPosition(targetActionObject.x_temp, targetActionObject.y_temp);
						targetActionObject.setRotation(targetActionObject.rotation_temp);
					}
					targetActionObject.x_temp=-1;
					targetActionObject.y_temp=-1;
					targetActionObject.rotation_temp=-1;

					//Zurück nach Hause uum Kühlschrank 
					Vector2 vh=CHelper.moveHumanByObjectRotation(fridge, baseWorldObject, fridge.width/2, -10);
					gotoXY((int)vh.x, (int)vh.y);
					fridge.isOccupiedBy=baseWorldObject;
					
					if(baseWorldObject.path==null)
					{
						//Gdx.app.debug("", "test");
						iActionBlocker=1000;
						bActionMode=false;
						resetActionObjects();
					}
					else
					{
						actionState=11.2f;
					}
				}
				
				return;
			}
			
			//Zurück zum zu befüllenden Objekt gehen
			if(actionState==11.1f)
			{
				actionState=11.41f;
				return;
				
//				if(targetActionObject.x_temp>-1)
//				{
//					baseWorldObject.actionanim1=2;
//					moveObjectByHumanRotation(baseWorldObject, targetActionObject, 57, -50, baseWorldObject.rotation());
//				}
//				
//				//Zum Kühlschrank 
//				Vector2 vh=CHelper.moveHumanByObjectRotation(fridge, baseWorldObject, fridge.width/2, -10);
//				gotoXY((int)vh.x, (int)vh.y);
//				fridge.isOccupiedBy=baseWorldObject;
//				
//				if(baseWorldObject.path==null)
//				{
//					iActionBlocker=iActionBlocker_default;
//					bActionMode=false;
//					resetActionObjects();
//				}
//				else
//				{
//					actionState=11.2f;
//				}
//				
//				return;
			}
			
			if(actionState==11.2f)
			{
				//Zu Fuß unterwegs
//				if(targetActionObject.x_temp>-1)
//				{
//					baseWorldObject.actionanim1=2;
//					moveObjectByHumanRotation(baseWorldObject, targetActionObject, 57, -50, baseWorldObject.rotation());
//					if(baseWorldObject.collides(fridge) || baseWorldObject.ziel_x==-1)
//					{
//						baseWorldObject.resetPathFinding();
//						moveHumanByObjectRotation(baseWorldObject, fridge, 0, -40, fridge.rotation()+90);
//						actionState=11.7f;
//					}						
//					return;
//				}
				
				//Kommt mit dem Auto am Parkplatz an
				//else
				{
					if(baseWorldObject.goByCar_X==-1)
					{
						//Hole Einkäufe aus dem Kofferraum
						gotoObjectXY(baseWorldObject.thehuman.car, (int) town.getSizeValue(10), baseWorldObject.thehuman.car.height+targetActionObject.width+5);
						actionState=11.3f;
						return;
					}
				}
				
				return;
			}
			
			if(actionState==11.3f)
			{
				//Hole Einkäufe aus dem Kofferraum
				baseWorldObject.actionanim1=0;
				if(baseWorldObject.ziel_x==-1)
				{
					moveHumanByObjectRotation(baseWorldObject, baseWorldObject.thehuman.car, (int) town.getSizeValue(38+5), baseWorldObject.thehuman.car.height+targetActionObject.width-(int) town.getSizeValue(60), baseWorldObject.thehuman.car.rotation());
					actionState=11.4f;
					deltaTimer=0;
				}
			}
			
			if(actionState==11.4f)
			{
				//Hole Einkäufe aus dem Kofferraum
				baseWorldObject.actionanim1=1;
				baseWorldObject.thehuman.car.actionanim1=2;
				baseWorldObject.thehuman.car.doObjectAction=true;
				if(deltaTimer>30)
				{
					baseWorldObject.thehuman.car.doObjectAction=false;
					targetActionObject5 = town.gameWorld.gameResourceConfig.createWorldObject("supermarket_buyin", baseWorldObject.pos_x(), baseWorldObject.pos_y(), baseWorldObject.theaddress);
					//Gdx.app.debug("", "targetActionObject5.theobject.ATTR_FILL3: " + targetActionObject5.theobject.ATTR_FILL3);
					targetActionObject5.theobject.zorder=4;
					targetActionObject5.addObjectFillingMulti(baseWorldObject.thehuman.car, true);
					targetActionObject5.theaddress=baseWorldObject.theaddress;
					targetActionObject5.isOccupiedBy=baseWorldObject;
					
					actionState=11.5f;
				}
				
				return;
			}
			
			if(actionState==11.41f) //einkaufswagen zum platz zurückbringen
			{
				if(targetActionObject.x_temp>0 && targetActionObject.y_temp>0)
					gotoXY(targetActionObject.x_temp, targetActionObject.y_temp);
				actionState=11.42f;
				return;
			}
			
			if(actionState==11.42f) //einkaufswagen zum platz zurückbringen
			{
				moveObjectByHumanRotation(baseWorldObject, targetActionObject, (int)town.getSizeValue(57), (int)town.getSizeValue(-50), baseWorldObject.rotation());
				
				if(baseWorldObject.ziel_x==-1)
				{
					actionState=11.43f;
					deltaTimer=0;
					if(targetActionObject.x_temp>0 && targetActionObject.y_temp>0)
					{
						targetActionObject.setRotation(targetActionObject.rotation_temp);
						targetActionObject.setPosition(targetActionObject.x_temp, targetActionObject.y_temp);
					}
				}
				
				return;
			}
			
			if(actionState==11.43f) //zu fuß
			{
				baseWorldObject.actionanim1=1;
				
				if(deltaTimer>30)
				{
					targetActionObject5 = town.gameWorld.gameResourceConfig.createWorldObject("supermarket_buyin", baseWorldObject.pos_x(), baseWorldObject.pos_y(), baseWorldObject.theaddress);
					targetActionObject5.theobject.zorder=4;
					targetActionObject5.addObjectFillingMulti(targetActionObject, true);
					targetActionObject5.isOccupiedBy=baseWorldObject;
					targetActionObject.objectFillingMulti.clear();
					targetActionObject.x_temp=-1;
					targetActionObject.y_temp=-1;
					
					actionState=11.5f;
				}
				
				return;
			}
			
			if(actionState==11.5f)
			{
				//Trage Einkaufskiste zum Fridge
				if(baseWorldObject.thehuman.car!=null)  {
					//if(myCarOnAdr(shelf.theaddress)) { //zeile verursacht exception
						baseWorldObject.thehuman.car.actionanim1=0;
					//}
				}
				
				baseWorldObject.actionanim1=1;
				
				moveObjectByHumanRotation(baseWorldObject, targetActionObject5, (int)town.getSizeValue(60), (int)town.getSizeValue(20), baseWorldObject.rotation());
				
				Vector2 v2 = CHelper.moveHumanByObjectRotation(fridge, baseWorldObject, (int)town.getSizeValue(60), (int)town.getSizeValue(-20));
				
				gotoXY((int)v2.x, (int)v2.y);
				
				actionState=11.6f;
				
				return;
			}
			
			if(actionState==11.6f)
			{
				if(targetActionObject5!=null) //Trage Einkaufskiste
				{
					//if(baseWorldObject.thehuman.getName().contains("Sarah Robinson"))
					//	Gdx.app.debug("debug1", ""+targetActionObject5.pos_x() + ", " + targetActionObject5.pos_y());
					
					moveObjectByHumanRotation(baseWorldObject, targetActionObject5, (int)town.getSizeValue(60), (int)town.getSizeValue(20), baseWorldObject.rotation());
				}
				
				baseWorldObject.fBremse=0.5f;
				
				if(baseWorldObject.ziel_x==-1)
				{
					//Positionierung Resident vor Fridge
					baseWorldObject.resetPathFinding();
					moveHumanByObjectRotation(baseWorldObject, fridge, (int)town.getSizeValue(45), (int)town.getSizeValue(-20), fridge.rotation()+90);
					actionState=11.7f;
				}
				
				return;
			}
			
			if(actionState==11.7f)
			{
				//Am Fridge angekommen
				//if(targetActionObject.x_temp>-1) //Hat Einkaufswagen dabei
				//{
				//	baseWorldObject.actionanim1=2;
				//	moveObjectByHumanRotation(baseWorldObject, targetActionObject, 57, -50, baseWorldObject.rotation());
				//}
				
				if(targetActionObject5!=null) //Trage Einkaufskiste
					moveObjectByHumanRotation(baseWorldObject, targetActionObject5, (int)town.getSizeValue(60), (int)town.getSizeValue(20), baseWorldObject.rotation());
				
				fridge.doObjectAction=true; //Kühlschrank auf
				actionState=12;
				deltaTimer=0;
				deltaTimer2=0;
				
				return;
			}
			
			//Einkauf in Fridge einräumen
			if(actionState==12)
			{
				CWorldObject wareObject=targetActionObject;
				if(targetActionObject5!=null)
					wareObject=targetActionObject5;
				
				baseWorldObject.actionanim1=0;
				
				if(fridge.getObjectFillingMulti()<fridge.getObjectFillingMultiMax() && 
						wareObject.getObjectFillingMulti()>0)
				{
					baseWorldObject.actionanim1=1;
					if(targetActionObject5!=null)
						targetActionObject5.theobject.zorder=2;
					
					if(deltaTimer2>delta2)
					{
						deltaTimer2=0;
						if(baseWorldObject.rotation()==fridge.rotation()+140)
							baseWorldObject.setRotation(fridge.rotation()+90);
						else
							baseWorldObject.setRotation(fridge.rotation()+140);
					}
					
					if(deltaTimer>delta)
					{
						deltaTimer=0;
						wareObject.addObjectFillingMulti(0,-10);
						fridge.addObjectFillingMulti(0,10);
					}
				}
				else
				{
					baseWorldObject.actionanim1=0;
					fridge.doObjectAction=false;
					fridge.isOccupiedBy=null;
					deltaTimer=0;
					
					//Kiste ist jetzt leer
					if(targetActionObject5!=null)
						town.gameGui.removeWorldObject(targetActionObject5, false);
					
					//Garbage vom Einkauf
					town.gameWorld.gameResourceConfig.createWorldObject("recyclingcenter_garbagebag", baseWorldObject.pos_x(), baseWorldObject.pos_y(), baseWorldObject.theaddress);
					
					//Wenn Einkaufswagen dabei -> Bringe Einkaufswagen wieder zurück
					//if(targetActionObject.x_temp>-1)
					//{
					//	gotoXY(targetActionObject.x_temp, targetActionObject.y_temp);
					//	actionState=13;
					//}
					//else
					{
						bActionMode=false;
						baseWorldObject.actionanim1=0;
						resetActionObjects();
					}
				}
				
				return;
			}
			
			if(actionState==13)
			{
				baseWorldObject.actionanim1=2;
				moveObjectByHumanRotation(baseWorldObject, targetActionObject, (int)town.getSizeValue(57), (int)town.getSizeValue(-50), baseWorldObject.rotation());
				
				if(baseWorldObject.ziel_x==-1)
				{
					if(targetActionObject.x_temp>0 && targetActionObject.y_temp>0)
					{
						targetActionObject.setRotation(targetActionObject.rotation_temp);
						targetActionObject.setPosition(targetActionObject.x_temp, targetActionObject.y_temp);
					}
					baseWorldObject.resetPathFinding();
					resetActionObjects();
					bActionMode=false;
				}
			}
		}	
	}
	
	
	
	//*******************************
	//Complex Work Verteiler Funktion
	//*******************************
	private void doAction_WorkComplex()
	{
		if(valueType==ValueType.WORK_COMPLEX)
		{
			if(targetActionObject==null)
			{
				bActionMode=false;
				return;
			}
			
//			//Arbeit pünktlich beenden
//			if(targetActionObject.workTime1_From>-1 && !baseWorldObject.thehuman.timeForWork())
//			{
//				resetActionObjects();
//				bActionMode=false;
//				return;
//			}
			
			if(targetActionObject.theobject.editoraction.contains("company_church_workingplace_battlepriest"))
			{
				setActionValue(10000, MathType.SET); //Action nicht automatisch beenden
				doAction_BattlePriest();
				return;
			}
			
			if(targetActionObject.theobject.editoraction.contains("company_construction_pickup1_traffic_car"))
			{
				setActionValue(10000, MathType.SET); //Action nicht automatisch beenden
				doAction_Construction();
				return;
			}
			
			if(targetActionObject.theobject.editoraction.contains("supermarket_pallettruck"))
			{
				setActionValue(10000, MathType.SET); //Action nicht automatisch beenden
				doAction_SupermarketRefillShelf();
				return;
			}
			
			if(targetActionObject.theobject.editoraction.contains("company_fitnessstudio_fitnessworkingplace"))
			{
				setActionValue(10000, MathType.SET); //Action nicht automatisch beenden
				doAction_FitnessTrainer();
				return;
			}
			
			if(targetActionObject.theobject.editoraction.contains("company_doctorsoffice_treatment_chair"))
			{
				setActionValue(10000, MathType.SET); //Action nicht automatisch beenden
				doAction_DoctorsOffice_Doctor();
				return;
			}
			
			if(targetActionObject.theobject.editoraction.contains("company_doctorsoffice_reception_desk"))
			{
				setActionValue(10000, MathType.SET); //Action nicht automatisch beenden
				doAction_DoctorsOffice_MedicalReceptionist();
				return;
			}
			
			if(targetActionObject.actionstring2.contains("gravedigger_action") 
					&& targetActionObject.isHuman() && targetActionObject.bIsDead
					&& targetActionObject.actionstring1.contains("show_coffin") 
					&& (targetActionObject.isOccupiedBy!=null 
					|| targetActionObject.isOccupiedBy.uniqueId==baseWorldObject.uniqueId))
			{
				setActionValue(10000, MathType.SET); //Action nicht automatisch beenden
				doAction_Gravedigger();
				return;
			}
			
			if(targetActionObject.theobject.editoraction.contains("company_urbancemetery_rostrum"))
			{
				setActionValue(10000, MathType.SET); //Action nicht automatisch beenden
				doAction_FuneralSpeaker();
				return;
			}
			
			if(targetActionObject.theobject.editoraction.contains("company_urbancemetery_hearse_traffic_car"))
			{
				setActionValue(10000, MathType.SET); //Action nicht automatisch beenden
				doAction_Hearse();
				return;
			}
			
			if(targetActionObject.theobject.editoraction.contains("company_college_workingplace_researchlab"))
			{
				doAction_Research();
				return;
			}
						
			if(targetActionObject.theobject.editoraction.contains("company_church_workingplace_altar"))
			{
				doAction_Preacher();
				return;
			}
			
			if(targetActionObject.theobject.editoraction.contains("company_recyclingcenter_garbagetruck"))
			{
				setActionValue(10000, MathType.SET); //Action nicht automatisch beenden
				doAction_GarbageTruck();
				return;
			}
			
			if(targetActionObject.theobject.editoraction.contains("company_pub_workingplace_bar"))
			{
				//setActionValue(10000, MathType.SET); //Action nicht automatisch beenden
				//Wenn das reingenommen wird, -> in complex action pause einbauen
				doAction_PUB_Barkeeper();
				return;
			}
			
			if(targetActionObject.theobject.editoraction.contains("company_school_workingplace_teachersdesk"))
			{
				doAction_Teacher();
				return;
			}
			
			if(targetActionObject.theobject.editoraction.contains("company_school_workingplace_studentsdesk"))
			{
				doAction_Student(0);
				return;
			}
			
			if(targetActionObject.theobject.editoraction.contains("company_college_workingplace_profslectern"))
			{
				doAction_Professor();
				return;
			}
			
			if(targetActionObject.theobject.editoraction.contains("company_college_workingplace_studentsdesk"))
			{
				doAction_Student(1);
				return;
			}
		}
	}

}







