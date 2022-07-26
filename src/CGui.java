package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import javax.print.DocFlavor.STRING;

import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.mygdx.game.CAddress.AddressSide;
import com.mygdx.game.CAnimationTextEvent.AnimationEventType;
import com.mygdx.game.CCompany.ArchitectWorkType;
import com.mygdx.game.CCompany.WorkoutputType;
import com.mygdx.game.CGuiControl_Button.ButtonType;
import com.mygdx.game.CGuiDialog_List.ListType;
import com.mygdx.game.CGuiDialog_OptionList.OptionListType;
import com.mygdx.game.CGuiDialog_TextInput.TextInputType;
import com.mygdx.game.CGuiDialog_YesNo.YesNoDlgType;
import com.mygdx.game.CHelper.IntersectionMode;
import com.mygdx.game.CHuman.CJobSkillClass;
import com.mygdx.game.CSpriteMoveEvent.SpriteMoveEventType;

public class CGui {
	
	//public CResourceConfig gameResourceConfig;
	//public CWorld gameWorld;
	//public CTown town;
	//public OrthographicCamera gameCamera;
	public SpriteBatch editorSpriteBatch;
	public String level1_objtypeid;
	public String level2_objtypeid;
	public List<CObject> editorObjectsShowing;
	public List<CObject> editorControlsShowing;
	public CObject deleteIcon;
	public CObject addressIcon;
	CGuiTooltip tooltip;
	CGuiTooltip tooltiphint;
	CGuiTooltip tooltip_timeinformation;
	public CWorldObject deleteObject;
	public float renderSecond;
	public Boolean bRender;
	CObject objFlashlightControl1;
	CObject objFlashlightControl2;
	CObject objAddressControl1;
	public Boolean bRenderForbidden;
	CGuiControl_Button buttonX2;
	CGuiControl_Button buttonX4;
	private Random rand;
	public CAddress mouseOverAddressInfo;
	public Boolean bButtonMouseover;
	public int iShowHint; 
	public Boolean bSetHint;
	Matrix4 uiMatrix;
	
	ArrayList<String> unlockedCompanyTypes;
	
	//Roadplacing
	public float placeStartX;
	public float placeStartY;
	public int rPlacingPrice;
	
	
	//Dialogs
	List<CGuiDialog> listDialogs;
	public CGuiDialog_CreateResident createResidentDlg;
	public CGuiDialog_List listDlg;
	public CGuiDialog_ListStatistics statisticsListDlg;
	public CGuiDialog_OptionList optionListDlg;
	public CGuiDialog_OptionList blueprintDlg;
	public CGuiDialog_YesNo yesnoDlg;
	public CGuiDialog_Worktime setWorktimeDlg;
	public CGuiDialog_Soundvolume soundVolumeDlg;
	public CGuiDialog_Gfx gfxDlg;
	public CGuiDialog_GameOptions gameOptionsDlg;
	public CGuiDialog_ObjectInfo objectInfoDlg;
	public CGuiDialog_TextInput textInputDlg;
	public CGuiDialog_ChooseColor chooseColorDlg;
	
	public CObject chosenHeadObj;
	public Boolean bAddressPlacing;
	public Boolean bAddressCloning;
	public CAddress cloneAddress;
	public CAddress clonedAddress;
	Boolean bRoomCloning;
	public String realEstateCloneName;
	public Boolean bAddressMoving;
	public CAddress moveAddress;
	public CAddress movingAddress;
	public float moveAddress_Origin_x;
	public float moveAddress_Origin_y;
	public Boolean bRoomPlacing;
	public Boolean bDeletemode;
	public Boolean bAddressResizingStart;
	public Boolean bAddressResizing;
	public Boolean bAddressResizingOver;
	public float addressResizing_sx;
	public float addressResizing_ex;
	public float addressResizing_sy;
	public float addressResizing_ey;
	public Boolean bResizeCarpetActive;
	public Boolean bResizeGroundActive;
	
	CWorldObject tempActionObject;
	public AddressSide addressResizingSide;
	public CAddress addressResizing;
	public Boolean bMouseActionMode;
	public Vector3 vectorRoomStart;
	public Boolean bObjPlacing;
	public Boolean bPaintObject;
	public CObject objPlacing;
	public Boolean bObjMovement;
	public int moveObject_origin_x;
	public int moveObject_origin_y;
	public int moveObject_origin_rot;
	public Boolean bButtonDown;
	public Boolean bButtonDown_MoveObject;
	public Boolean bFlashlight;
	public int iShowAddresses;
	public float speedControllerX;
	
	int iSliderDiff;
	public Boolean bSliderDown;
	
	private float moveobjectdelaytimer;
	public ShapeRenderer shapeRenderer;
	public BitmapFont font;
	public PointLight pointlight1;
	public Vector3 startAddressPlacing;
	public String addressPlacingType;
	
	int lastplacement_x;
	int lastplacement_y;
	
	int moveX;
	int moveY;
	
	CGuiInfo guiinfo_electricity;
	//CGuiInfo guiinfo_water;
	CGuiInfo guiinfo_population;
	CGuiInfo guiinfo_money;
	CGuiInfo guiinfo_icon_health;
	CGuiInfo guiinfo_icon_happiness;
	
	public CGuiInfo guiinfo_sleep;
	public CGuiInfo guiinfo_residentenergy;
	public CGuiInfo guiinfo_eat;
	public CGuiInfo guiinfo_clean;
	public CGuiInfo guiinfo_toilet;
	
	public CTown town;
	
	
	public void init(CTown tg)
	{
		//Achtung: Hier keine Spiel-Werte zurücksetzen (wird zb in screenresolution change verwendet) 
		
		placeStartX=0;
		placeStartY=0;
		rPlacingPrice=0;
		
		bSetHint=false;
		iShowHint=0;
		bResizeCarpetActive=false;
		bResizeGroundActive=false;
		bButtonMouseover=false;
		mouseOverAddressInfo=null;
		tempActionObject=null;
		town=tg;
		//gameResourceConfig = config;
		
		//gameCamera = cam;
		
		font = new BitmapFont();
        font.setColor(Color.WHITE);
		font.getData().setScale(1);
		
		renderSecond=0;
		moveobjectdelaytimer=0;
		addressResizing=null;
		bMouseActionMode=false;
		bAddressPlacing=false;
		bAddressCloning=false;
		bRoomCloning=false;
		bAddressMoving=false;
		moveAddress=null;
		movingAddress=null;
		iSliderDiff=0;
		chosenHeadObj=null;
		rand=new Random();
		iShowAddresses=2;
		moveX=0;
		moveY=0;
		deleteObject=null;
		if(shapeRenderer!=null)
			shapeRenderer.dispose();
		//shapeRenderer = tg.townShapeRenderer;//new ShapeRenderer();
		shapeRenderer = new ShapeRenderer();
		bButtonDown = false;
		speedControllerX = (int)Gdx.graphics.getWidth()-520;
		
		bSliderDown=false;
		addressPlacingType="";
		bRenderForbidden=false;
		bAddressResizing=false;
		bAddressResizingOver=false;
		addressResizingSide=AddressSide.NONE;
		moveObject_origin_x=0;
		moveObject_origin_y=0;
		moveObject_origin_rot=0;
		bAddressResizingStart=false;
		
		int lastplacement_x=0;
		int lastplacement_y=0;
		
		bButtonDown_MoveObject=false;
		bButtonDown=false;
		
		if(editorSpriteBatch!=null)
			editorSpriteBatch.dispose();
		//editorSpriteBatch = tg.townSpriteBatch;// new SpriteBatch();
		editorSpriteBatch = new SpriteBatch();
		
		int bsize=26;
		buttonX2 = new CGuiControl_Button(0, 0, bsize, bsize, 0, 0, "", null, town.gameResourceConfig.textures.get("control_button_play2"), ButtonType.TOGGLE_IMAGE, town);
		buttonX4 = new CGuiControl_Button(0, 0, bsize, bsize, 0, 0, "", null, town.gameResourceConfig.textures.get("control_button_play3"), ButtonType.TOGGLE_IMAGE, town);
        buttonX2.setColor(new Color(0.9f, 0.9f, 0.9f, 0.9f));
        buttonX4.setColor(new Color(0.9f, 0.9f, 0.9f, 0.9f));
        buttonX2.renderMode=1;
        buttonX4.renderMode=1;
        buttonX2.toggleStyle=1;
        buttonX4.toggleStyle=1;
        buttonX2.imageButtonType=1;
        buttonX4.imageButtonType=1;
        
		editorObjectsShowing = new ArrayList<CObject>();
		editorControlsShowing = new ArrayList<CObject>();
		
		level1_objtypeid = "";
		level2_objtypeid = "";
		
		objPlacing=new CObject(town);
		bObjPlacing=false;
		bPaintObject=false;
		bObjMovement=false;
		bDeletemode=false;
		bRoomPlacing=false;
		vectorRoomStart=new Vector3();
		
		tooltip = new CGuiTooltip(town);
		tooltiphint = new CGuiTooltip(town);
		tooltip_timeinformation = new CGuiTooltip(town);
        editorControlsShowing = town.gameResourceConfig.listObjectres.stream().filter(p->p.editoraction.contains("control")).collect(Collectors.toList());
        deleteIcon = town.gameResourceConfig.listObjectres.stream().filter(p->p.editoraction.contains("gui_sell")).findFirst().get();
        addressIcon = town.gameResourceConfig.listObjectres.stream().filter(p->p.editoraction.contains("gui_address")).findFirst().get();
        
		createResidentDlg=new CGuiDialog_CreateResident(editorControlsShowing, font, (int)(Gdx.graphics.getWidth()/3), (int)(Gdx.graphics.getHeight()/3f), town);
		listDlg=new CGuiDialog_List(editorControlsShowing, font, (int)(Gdx.graphics.getWidth()/3), (int)(Gdx.graphics.getHeight()/3), town);
		statisticsListDlg=new CGuiDialog_ListStatistics(editorControlsShowing, font, (int)(Gdx.graphics.getWidth()/3), (int)(Gdx.graphics.getHeight()/3), town);
		setWorktimeDlg=new CGuiDialog_Worktime(editorControlsShowing, font, (int)(Gdx.graphics.getWidth()/2), (int)(Gdx.graphics.getHeight()/2), town);
		optionListDlg=new CGuiDialog_OptionList(editorControlsShowing, font, (int)(Gdx.graphics.getWidth()/3), (int)(Gdx.graphics.getHeight()/3), town);
		blueprintDlg=new CGuiDialog_OptionList(editorControlsShowing, font, 10, 10, town);
		blueprintDlg.dlgX=10;
		blueprintDlg.dlgY=(int)(Gdx.graphics.getHeight()/4);
		blueprintDlg.showDlg(true, OptionListType.LoadRealEstate2);
		yesnoDlg=new CGuiDialog_YesNo(editorControlsShowing, font, (int)(Gdx.graphics.getWidth()/3), (int)(Gdx.graphics.getHeight()/3), town);
		textInputDlg=new CGuiDialog_TextInput(editorControlsShowing, font, (int)(Gdx.graphics.getWidth()/3), (int)(Gdx.graphics.getHeight()/3), town);
		chooseColorDlg=new CGuiDialog_ChooseColor(editorControlsShowing, font, (int)(Gdx.graphics.getWidth()/3), (int)(Gdx.graphics.getHeight()/3), town);
		soundVolumeDlg=new CGuiDialog_Soundvolume(editorControlsShowing, (int)(Gdx.graphics.getWidth()/2), (int)(Gdx.graphics.getHeight()/2), town);
		gfxDlg=new CGuiDialog_Gfx(editorControlsShowing, (int)(Gdx.graphics.getWidth()/2), (int)(Gdx.graphics.getHeight()/2), town);
		gameOptionsDlg=new CGuiDialog_GameOptions(editorControlsShowing, (int)(Gdx.graphics.getWidth()/2), (int)(Gdx.graphics.getHeight()/2), town);
		objectInfoDlg = new CGuiDialog_ObjectInfo(town.gameResourceConfig.listObjectres, font, 0, 0, town);
		
		listDialogs = new ArrayList<CGuiDialog>();
		listDialogs.add(createResidentDlg);
		listDialogs.add(listDlg);
		listDialogs.add(statisticsListDlg);
		listDialogs.add(setWorktimeDlg);
		listDialogs.add(optionListDlg);
		listDialogs.add(soundVolumeDlg);
		listDialogs.add(gfxDlg);
		listDialogs.add(yesnoDlg);
		listDialogs.add(textInputDlg);
		listDialogs.add(chooseColorDlg);
		listDialogs.add(gameOptionsDlg);
		
		objAddressControl1 = ((Optional<CObject>)editorControlsShowing.stream().filter(p->p.editoraction.equals("control_address1")).findFirst()).get();
		//objAddressControl2 = ((Optional<CObject>)editorControlsShowing.stream().filter(p->p.editoraction.equals("control_address2")).findFirst()).get();
		
		objFlashlightControl1 = ((Optional<CObject>)editorControlsShowing.stream().filter(p->p.editoraction.equals("control_flashlight1")).findFirst()).get();
		objFlashlightControl2 = ((Optional<CObject>)editorControlsShowing.stream().filter(p->p.editoraction.equals("control_flashlight2")).findFirst()).get();
		
		bFlashlight=false;
		town.gameWorld.placinglight.setActive(bFlashlight);
			
		
		int icsize=19;
		//if(guiinfo_electricity==null)
		{
			
			//Black/White
			guiinfo_electricity=new CGuiInfo(0, 0, town.gameResourceConfig.textures.get("guiinfo_electricity"), "", "Electrical Energy Output and Consumption", icsize-5, icsize+1, town);
			//guiinfo_water=new CGuiInfo(0, 0, town.gameResourceConfig.textures.get("guiinfo_water"), "", "Water Output and Consumption", icsize, icsize, town);
			guiinfo_population=new CGuiInfo(0, 0, town.gameResourceConfig.textures.get("guiinfo_population"), "", "Population", icsize, icsize, town);
			guiinfo_money=new CGuiInfo(0, 0, town.gameResourceConfig.textures.get("guiinfo_money"), "", "Money", icsize-6, icsize-2, town);
			
			//Color
			//			guiinfo_electricity=new CGuiInfo(0, 0, town.gameResourceConfig.textures.get("towninfo_energy"), "", "Electrical Energy Consumption / Output", icsize-1, icsize+1, town);
			//			guiinfo_water=new CGuiInfo(0, 0, town.gameResourceConfig.textures.get("towninfo_water"), "", "Water Consumption / Output", icsize, icsize, town);
			//			guiinfo_population=new CGuiInfo(0, 0, town.gameResourceConfig.textures.get("towninfo_population"), "", "Population", icsize, icsize, town);
			//			guiinfo_money=new CGuiInfo(0, 0, town.gameResourceConfig.textures.get("towninfo_money"), "", "Money", icsize-6, icsize-2, town);
			
			guiinfo_sleep = new CGuiInfo(0, 0, town.gameResourceConfig.textures.get("info_attr_sleep"), "", "Ø Sleep", icsize, icsize, town);
			guiinfo_residentenergy = new CGuiInfo(0, 0, town.gameResourceConfig.textures.get("guiinfo_residentenergy"), "", "Ø Energy", icsize, icsize, town);
			guiinfo_eat = new CGuiInfo(0, 0, town.gameResourceConfig.textures.get("info_attr_eat"), "",	"Ø Eat", icsize, icsize, town);
			guiinfo_clean = new CGuiInfo(0, 0, town.gameResourceConfig.textures.get("info_attr_clean"), "", "Ø Clean", icsize, icsize, town);
			guiinfo_toilet = new CGuiInfo(0, 0, town.gameResourceConfig.textures.get("info_attr_toilet"), "", "Ø Toilet",	icsize, icsize, town);						

			//			guiinfo_sleep.moveTooltipPosition(-100, -38);
			//			guiinfo_eat.moveTooltipPosition(-100, -38);
			//			guiinfo_clean.moveTooltipPosition(-100, -38);
			//			guiinfo_toilet.moveTooltipPosition(-100, -38);
			
			guiinfo_icon_health=new CGuiInfo(0, 0, town.gameResourceConfig.textures.get("icon_health"), "", "Average Health", icsize, icsize, town);
			guiinfo_icon_happiness=new CGuiInfo(0, 0, town.gameResourceConfig.textures.get("icon_happiness"), "", "Average Happiness", icsize+1, icsize+1, town);
			
			//Tooltip Position anpassen
			guiinfo_electricity.moveTooltipPosition(-15, -38);
			//guiinfo_water.moveTooltipPosition(-15, -38);
			guiinfo_population.moveTooltipPosition(-15, -38);
			guiinfo_money.moveTooltipPosition(-15, -38);
			guiinfo_icon_health.moveTooltipPosition(-15, -38);
			guiinfo_icon_happiness.moveTooltipPosition(-100, -38);
			
			//Gui Spritebatch verwenden und nicht begin oder end
			guiinfo_sleep.setctrlSpriteBatch(editorSpriteBatch);
			guiinfo_residentenergy.setctrlSpriteBatch(editorSpriteBatch);
			guiinfo_eat.setctrlSpriteBatch(editorSpriteBatch);
			guiinfo_clean.setctrlSpriteBatch(editorSpriteBatch);
			guiinfo_toilet.setctrlSpriteBatch(editorSpriteBatch);
			guiinfo_electricity.setctrlSpriteBatch(editorSpriteBatch);
			//guiinfo_water.setctrlSpriteBatch(editorSpriteBatch);
			guiinfo_population.setctrlSpriteBatch(editorSpriteBatch);
			guiinfo_money.setctrlSpriteBatch(editorSpriteBatch);
			guiinfo_icon_health.setctrlSpriteBatch(editorSpriteBatch);
			guiinfo_icon_happiness.setctrlSpriteBatch(editorSpriteBatch);
		}
				
		bRender=true;
	}
	
	//MENUE-------------------------------------------------------------->
	public CObjecttype getMenuCollision_ObjectTypes(int libgdx_x, int libgdx_y)
	{
		//if(createResidentDlg.dlgShowing() || createAddressDlg.dlgShowing() || chooseWorkerDlg.dlgShowing())
		//	return null;
		
		//if(gameWorld.worldPause)
		//	return null;
		
		if(dlgShowing())
			return null;
		
		if(bObjPlacing || bRoomPlacing)
			return null;
		
		int y = libgdx_y;
		int x = libgdx_x;
		
		for(CObjecttype objtype : town.gameResourceConfig.listObjecttype)
		{
			if(objtype.collidesIcon(x, y))
			{
				if(objtype.objectTypeId.length()==3)
				{
					if(town.bNoRealEstate && (objtype.iconFileName.toLowerCase().contains("address")))
						continue;

					return objtype;
				}
				
				if(objtype.objectTypeId.length()==6 && objtype.objectTypeId.substring(0, 3).equals(level1_objtypeid))
				{
					//Firma nicht anzeigen
					if(town.gameMode.toLowerCase().contains("design") && objtype.iconFileName.toLowerCase().contains("illuminati"))
						continue;
					if(!town.bZombieApocalypse && objtype.iconFileName.toLowerCase().contains("illuminati"))
						continue;
					
					if(town.bNoRealEstate && (objtype.iconFileName.toLowerCase().contains("address")))
						continue;
					
					return objtype;
				}
			}
		}
		
		return null;
	}
	public CObject getControlCollision_Objects(int libgdx_x, int libgdx_y)
	{
		//if(gameWorld.worldPause)
		//{
		//	gameWorld.worldPause=false;
		//	return null;
		//}
		
		if(bObjPlacing || bRoomPlacing)// || bCreateResident)
			return null;
		
		//		if(gameWorld.worldPause)
		//			buttonControl = ((Optional<CObject>)editorControlsShowing.stream().filter(p->p.editoraction.equals("control_button_play1")).findFirst()).get();
		//		else
		//			buttonControl = ((Optional<CObject>)editorControlsShowing.stream().filter(p->p.editoraction.equals("control_button_pause1")).findFirst()).get();
		//		
		//		buttonControl_speed1 = ((Optional<CObject>)editorControlsShowing.stream().filter(p->p.editoraction.equals("control_button_speed1")).findFirst()).get();			
		//		buttonControl_speed2 = ((Optional<CObject>)editorControlsShowing.stream().filter(p->p.editoraction.equals("control_button_speed2")).findFirst()).get();
		
		int y = libgdx_y;
		int x = libgdx_x;
		
		for(CObject obj : editorControlsShowing)
		{
			if(obj.collidesIcon(x, y))
			{
				if(obj.editoraction.contains("control_button_play") || obj.editoraction.contains("control_button_pause"))
				{
					if(town.gameWorld.worldPause==true)
						town.gameWorld.worldPause=false;
					else
						town.gameWorld.worldPause=true;
					
					handleGuiButtonClick();
				}
				
				if(obj.editoraction.contains("control_button_speed"))
				{
					bSliderDown=true;
					iSliderDiff=(int) (x-speedControllerX);
					handleGuiButtonClick();
				}
				
				if(obj.editoraction.contains("control_flashlight"))
				{
					bFlashlight=!bFlashlight;
					town.gameWorld.placinglight.setActive(bFlashlight);
					handleGuiButtonClick();
				}				
								
				if(obj.editoraction.contains("control_address"))
				{
					iShowAddresses++;
					if(iShowAddresses>3)
						iShowAddresses=0;
					handleGuiButtonClick();
				}					
				
				return obj;
			}
		}
		return null;
	}
	public CObject getMenuCollision_Objects(int libgdx_x, int libgdx_y)
	{
		//if(gameWorld.worldPause)
		//	return null;
		
		if(bObjPlacing || bRoomPlacing)
			return null;
		
		int y = libgdx_y;
		int x = libgdx_x;
		
		for(CObject obj : editorObjectsShowing)
		{
			if(obj.collidesIcon(x, y))
			{
				obj.tempprice=obj.original_price;
				//if(getMultiPlacingCount(obj)>0)
				
				setMultiPlacingPrice(obj);
					//obj.price=obj.tempprice*(getMultiPlacingCount(obj));
				return obj;
			}
		}
		return null;
	}
	
//	public int getMultiPlacingPrice(CObject obj)
//	{
//		int id1 = Integer.parseInt(obj.objectId);
//		if(gameWorld.tempPriceObjects.containsKey(id1))
//		{
//			int count = gameWorld.tempPriceObjects.get(id1);
//			if(count>0)
//				return count*(obj.original_price/10);
//		}
//		
//		return 0;
//	}

//	public void setMultiPlacingPrice(CObject obj)
//	{
//		obj.tempprice=obj.original_price;
//		if(getMultiPlacingCount(obj)>0)
//			obj.price=obj.tempprice*getMultiPlacingCount(obj);
//	}
	
	public void setMultiPlacingPrice(CObject obj)
	{
		if(obj==null)
			return;
		
		if(obj.original_price==0)
			return;
	
		//obj.price=obj.original_price;
		
		//if(1==1)
		//	return;
		
		
		if(obj.tempprice==0)
			return;
		
		if(!obj.editoraction.contains("outdoor_tree") && 
				!obj.editoraction.contains("outdoor_plant") && 
				!obj.editoraction.contains("outdoor_flower") &&
				!obj.editoraction.contains("indoor_plant") &&
				//!obj.editoraction.contains("waterworks_groundwaterextractionsystem") &&
				!obj.isRoomObject &&
				!obj.isGroundObject)
		{
			//obj.price=(int)obj.tempprice;
			return;
		}
		
		//float mult=(float)getMultiPlacingCount(obj)/1f;
		
		//mult/=5;
				
		//if(obj.editoraction.contains("outdoor_flower") || obj.editoraction.contains("outdoor_plant"))
		//	mult/=5f;
		
		//if(mult<1)
		int mult=1;
		
		if(obj.tempprice==0 && obj.original_price>0)
			obj.tempprice=obj.original_price;
		
		obj.price=(int) (obj.tempprice*mult);
	}
	
//	public int getMultiPlacingCount(CObject obj)
//	{
//		int id1 = Integer.parseInt(obj.objectId);
//		if(gameWorld.tempPriceObjects.containsKey(id1))
//		{
//			int count = gameWorld.tempPriceObjects.get(id1);
//			return count;
//		}
//		
//		return 0;
//	}
	
	public Boolean handleMenuAction(int x, int libgdxy)
	{
		//if(createResidentDlg.dlgShowing() || createAddressDlg.dlgShowing() || chooseWorkerDlg.dlgShowing())
		//	return true;
		
		for(CGuiDialog dlg : listDialogs)
		{
			if(dlg.dlgShowing())
				return false;
		}
		
		if(bObjPlacing || bRoomPlacing)
			return false;
		
		CObjecttype objtype = getMenuCollision_ObjectTypes(x, libgdxy);
		if(objtype!=null)
		{
			if(objtype.objectTypeId.length()==3)
			{
				if(objtype.actionCode.equals("customizeresident"))
				{
					createResidentDlg.showDlg(true);
					level2_objtypeid="";
					return true;
				}
				
				if(objtype.actionCode.equals("delete"))
				{
					bDeletemode=true;
					closeMenu();
					return true;
				}				
				
				level1_objtypeid = objtype.objectTypeId;
				level2_objtypeid="";
			}
			
			if(objtype.objectTypeId.length()==6)
			{
				if(level1_objtypeid.isEmpty())
					return false;
				
				if(objtype.objectTypeId.length()==6 && objtype.objectTypeId.substring(0, 3).equals("009"))
				{
					if(town.isGameDemo && (Integer.parseInt(objtype.objectTypeId)>9004 && Integer.parseInt(objtype.objectTypeId)!=9014 && Integer.parseInt(objtype.objectTypeId)!=9022))
					{
						//..
					}
					else
					{
						if(town.gameGui.unlockedCompanyTypes.contains(objtype.objectTypeId))
						{
							level2_objtypeid = objtype.objectTypeId;
						}
						else
						{
							if(town.gameWorld.townMoney>=town.unlockCompanyPrice)
							{
								yesnoDlg.unlockcompanyid = objtype.objectTypeId;
								yesnoDlg.unlockcompanyname = objtype.objectTypeName;
								yesnoDlg.showDlg(true, YesNoDlgType.UNLOCKCOMPANY);
							}
							else
							{
								//if(!town.gameAudio.soundIsPlaying("EFFECT_DIRTYL"))
								//	town.gameAudio.playSound("EFFECT_DIRTYL", 0, false);
							}
						}
					}
				}
				else
				{
					level2_objtypeid = objtype.objectTypeId;
				}
				
				if(objtype.actionCode.equals("newtown"))
				{
					//yesnoDlg.showDlg(true, CGuiDialog_YesNo.YesNoDlgType.NEWTOWN);
					//town.newGame(true, "easy");
					listDlg.showDlg(true, null, ListType.NEWTOWN, 0);
					level2_objtypeid="";
				}
				
				if(objtype.actionCode.equals("savetown"))
				{
					optionListDlg.showDlg(true, OptionListType.SaveTown);
					level2_objtypeid="";
				}
				
				if(objtype.actionCode.equals("loadtown"))
				{
					optionListDlg.showDlg(true, OptionListType.LoadTown);
					level2_objtypeid="";
				}
				
				if(objtype.actionCode.equals("resolution"))
				{
					optionListDlg.showDlg(true, OptionListType.ScreenResolution);
					level2_objtypeid="";
				}
				
				if(objtype.actionCode.equals("gfx"))
				{
					gfxDlg.showDlg(true);
					level2_objtypeid="";
				}				
				
				if(objtype.actionCode.equals("gameoptions"))
				{
					gameOptionsDlg.showDlg(true);
					level2_objtypeid="";
				}				
				
				if(objtype.actionCode.equals("soundvolume"))
				{
					soundVolumeDlg.showDlg(true);
					level2_objtypeid="";
				}
				
				if(objtype.actionCode.equals("info"))
				{
					//Gdx.net.openURI("http://store.steampowered.com/app/517700/");
					level2_objtypeid="";
				}				
				
				if(objtype.actionCode.equals("quit"))
				{
					yesnoDlg.showDlg(true, CGuiDialog_YesNo.YesNoDlgType.QUIT);
										
					closeMenu();
					//Gdx.app.exit();
					return true;
				}
								
				if(objtype.actionCode.equals("complete_realestate"))
				{
					//gameWorld.loadRealEstate();
					optionListDlg.showDlg(true, OptionListType.LoadRealEstate);
					return true;
					//bCompletePlacing=true;
					//addressPlacingType=objtype.actionCode;
					//startAddressPlacing=null;
					//level2_objtypeid="";
				}
				
				if(objtype.actionCode.contains("address"))
				{
//					if(objtype.actionCode.contains("address_clone"))
//					{
//						bAddressCloning=true;
//						cloneAddress=null;
//						clonedAddress=null;
//					}
//					else if(objtype.actionCode.contains("address_move"))
//					{
//						if(moveAddress!=null && moveAddress.listWorldObjects.size()>0)
//						{
//							for(CWorldObject wobj : moveAddress.listWorldObjects.values())
//							{
//								wobj.cancelAction1();
//							}
//						}
//												
//						bAddressMoving=true;
//						moveAddress=null;
//						movingAddress=null;
//						moveAddress_Origin_x=0;
//						moveAddress_Origin_y=0;
//					}
//					else
					{
						if(iShowAddresses==0)
							iShowAddresses=1;
						
						bAddressPlacing=true;
						addressPlacingType=objtype.actionCode;
						startAddressPlacing=null;
						//	endAddressPlacing=null;
					}
					
					//closeMenu();
					level2_objtypeid=""; //keine objektliste anzeigen, da es sich um eine action handelt
				}				
			}
			
			if(objtype.objectTypeId.length()==9)
			{
				//..
			}			
			
			return true;
		}
		
		CObject obj = getMenuCollision_Objects(x, libgdxy);
		if(obj!=null)
		{
			if(town.gameWorld.townMoney<obj.price) //zu wenig geld
				return true;
			
			startObjectPlacing(obj);
			
			return true;
		}
		
		return false;
	}
	//MENUE<--------------------------------------------------------------		
	
	//INPUT-------------------------------------------------------------->
	public Boolean doubleClick(int x, int y, int libgdxy, int button)
	{
	
		for(CGuiDialog dlg : listDialogs)
		{
			if(dlg.dlgShowing())
			{
				Boolean bc = dlg.doubleClick(x, y, libgdxy, button);
				if(bc)
					return true;
			}
		}

		if(blueprintDlg.dlgShowing())
		{
			Boolean bc1 = blueprintDlg.doubleClick(x, y, libgdxy, button);
			if(bc1)
				return true;
		}

		
		return false;
	}
	
	public void checkRoadPlacing(int itype, Vector3 v3, TextureRegion rgn1, int posx, int posy)
	{
		Boolean checkPlacing = town.gameWorld.checkObjectPlacing(null, town.gameGui.objPlacing);
		
		if(checkPlacing)
		{
			if(itype==0)
			{
				CWorldObject newWorldObject = new CWorldObject(objPlacing, town, true);
				Boolean placingOK=addWorldObject(newWorldObject, true);
				
				if(placingOK)
				{
					if(objPlacing.editoraction.contains("road_road_road"))
					{
						CWorldObject fp = objPlacing.isOnFootpath((int)town.getSizeValue(50), -1, -1, false); //mov 100 für road auf footpath eingestellt
						while(fp!=null)
						{
							sellObject(fp);						
							fp = objPlacing.isOnFootpath((int)town.getSizeValue(50), -1, -1, false);
						}
					}
				}
			}

			if(itype==1)
			{
				town.gameWorld.worldSpriteBatch.draw(rgn1, posx, posy, town.gameGui.objPlacing.width/2, town.gameGui.objPlacing.height/2, town.gameGui.objPlacing.width, town.gameGui.objPlacing.height, 1, 1, objPlacing.rotation);
				town.gameGui.rPlacingPrice+=town.gameGui.objPlacing.price;
			}
		}
	}
	
	public int roadPlacingDrawLine(String sway, int county, int countx, int lastposx, int lastposy, int itype, Vector3 v3, TextureRegion rgn1)
	{
		if(sway.equals("vert"))
		{
			int posx=0;
			int posy=0;

			for(int i=0;i<county;i++)
			{
				posx=lastposx;
				posy=(int) town.gameGui.placeStartY;
				
				if(v3.y > town.gameGui.placeStartY)
					posy+=town.gameGui.objPlacing.iRasterValue*i;
				else
					posy-=town.gameGui.objPlacing.iRasterValue*i;
				
				town.gameGui.objPlacing.pos_x=posx;
				town.gameGui.objPlacing.pos_y=posy;
				
				checkRoadPlacing(itype, v3, rgn1, posx, posy);
			}
			
			return posy;
		}
		
		if(sway.equals("horz"))
		{
			int posx=0;
			int posy=0;

			for(int i=0;i<countx;i++)
			{
				posx=(int) town.gameGui.placeStartX;
				posy=lastposy;
								
				if(v3.x > town.gameGui.placeStartX)
					posx+=town.gameGui.objPlacing.iRasterValue*i;
				else
					posx-=(town.gameGui.objPlacing.iRasterValue*i);// + town.gameGui.objPlacing.width);
	
				town.gameGui.objPlacing.pos_x=posx;
				town.gameGui.objPlacing.pos_y=posy;
				
				checkRoadPlacing(itype, v3, rgn1, posx, posy);
			}
			
			return posx;
		}
		
		return 0;
	}
	
	public void roadPlacing(int itype, TextureRegion rgn1)
	{
		if(bObjPlacing && objPlacing!=null && (objPlacing.ATTR_RPLACING>0) && (placeStartX>0 || placeStartY>0))
		{
			Vector3 v3 = CHelper.getMousePosition(town.gameCam);
			
			int temprast=town.gameGui.objPlacing.iRasterValue;
			if(town.gameGui.objPlacing.editoraction.contains("buildingwall"))
				town.gameGui.objPlacing.iRasterValue=town.gameGui.objPlacing.width;
			
			if(town.gameGui.objPlacing.iRasterValue>0)
			{
				v3.x = (int)(v3.x / town.gameGui.objPlacing.iRasterValue);
				v3.x = (int)(v3.x * town.gameGui.objPlacing.iRasterValue);
				v3.y = (int)(v3.y / town.gameGui.objPlacing.iRasterValue);
				v3.y = (int)(v3.y * town.gameGui.objPlacing.iRasterValue);
			}
			
			v3.x+=town.gameGui.objPlacing.iRasterValue_movx;
			v3.y+=town.gameGui.objPlacing.iRasterValue_movy;
			
			int distx = (int) Math.abs((v3.x-town.gameGui.placeStartX));
			int disty = (int) Math.abs((v3.y-town.gameGui.placeStartY));
			int countx = distx/objPlacing.iRasterValue;
			int county = disty/objPlacing.iRasterValue;
			if(itype==1) //draw
			{
				countx = distx/town.gameGui.objPlacing.width;
				county = disty/town.gameGui.objPlacing.width;
			}
			
			if(itype==1)
				town.gameGui.rPlacingPrice=0;
			
			if(countx==0 && county==0)
			{
				town.gameGui.objPlacing.pos_x=(int) v3.x;
				town.gameGui.objPlacing.pos_y=(int) v3.y;
				
				checkRoadPlacing(itype, v3, rgn1, (int)v3.x, (int)v3.y);
			}
			
			if(itype==1)
				town.gameWorld.worldSpriteBatch.setColor(1f, 1f, 1f, 0.5f);
			
			int lastposx=(int) town.gameGui.placeStartX;
			int lastposy=(int) town.gameGui.placeStartY;
			
			if(disty>distx)
			{
				lastposy=roadPlacingDrawLine("vert", county, countx, lastposx, lastposy, itype, v3, rgn1);
				lastposx=roadPlacingDrawLine("horz", county, countx, lastposx, lastposy, itype, v3, rgn1);
			}
			else
			{
				lastposx=roadPlacingDrawLine("horz", county, countx, lastposx, lastposy, itype, v3, rgn1);
				lastposy=roadPlacingDrawLine("vert", county, countx, lastposx, lastposy, itype, v3, rgn1);
			}
			
			if(itype==0)
			{
				placeStartX=0;
				placeStartY=0;
				town.gameWorld.bRenderFrameBuffer=true;
			}
			
			if(town.gameGui.objPlacing.editoraction.contains("buildingwall"))
				town.gameGui.objPlacing.iRasterValue=temprast;
		}
	}
	
	public Boolean buttonUp(int x, int y, int libgdxy, int button)
	{
		bSliderDown=false;
		bButtonDown=false;
		
		//Roadplacing
		if(button==0)
		{
			roadPlacing(0, null);
		}
		
		if(button==0 && bSetHint)
		{
			if(iShowHint>0)
				iShowHint=0;
			else
				iShowHint=1;
			
			bSetHint=false;
		}
		
		if(button==0 && bAddressResizing)
		{
			int pricediff=getAddressResizingPriceDiff();
			
			int pricediff_real = pricediff;
			int pricediff_planning = 0; 
			if(pricediff_real>0 && town.gameWorld.addressArchitectPlanningValue>0)
			{
				pricediff_planning = pricediff_real/100*town.gameWorld.addressArchitectPlanningValue;
				pricediff_real = pricediff_real - pricediff_real/100*town.gameWorld.addressArchitectPlanningValue;
			}
			
			if(town.gameWorld.townMoney<pricediff_real)
			{
				addressResizing.sx=addressResizing_sx;
				addressResizing.ex=addressResizing_ex;
				addressResizing.sy=addressResizing_sy;
				addressResizing.ey=addressResizing_ey;
			}
			else
			{
				town.gameWorld.addressArchitectPlanningValue=0;
				
				//gameWorld.townMoney-=pricediff;
				town.gameWorld.changeTownMoney(-pricediff_real);
				
				if(pricediff>0)
					town.gameWorld.townStatistics.getCurrentStatistics_Finance().buyAddress+=pricediff_real;
				else
					town.gameWorld.townStatistics.getCurrentStatistics_Finance().sellAddress+=Math.abs(pricediff_real);
				
				//gameWorld.animationEvents.add(new CAnimationTextEvent(town, Math.round(addressResizing.sx+(addressResizing.ex-addressResizing.sx)/2), Math.round(addressResizing.sy+(addressResizing.ey-addressResizing.sy)/2), pricediff*-1, AnimationEventType.MONEY, town.gameCam.zoom+5));
				addressResizing=null;
			}
			
			bAddressResizing=false;
			bAddressResizingStart=false;
			return true;
		}
	


		
		for(CGuiDialog dlg : listDialogs)
		{
			if(dlg.dlgShowing())
			{
				dlg.buttonUp();
			}
		}
		
		if(blueprintDlg.dlgShowing())
		{
			blueprintDlg.buttonUp();
		}
		
		if(bButtonDown_MoveObject)
		{
			bButtonDown_MoveObject=false;
			town.gameWorld.moveObjectEnd();
		}
		
		if(button==0)
		{
			if(bAddressPlacing && startAddressPlacing!=null)
			{
				return createAdr(x, y);
			}
			
			if(bRoomPlacing)
			{
				this.createRoomFromTempWorldObjects();
				
				bRoomPlacing = false;
				bObjPlacing = false;
				objPlacing = null;
				
				return true;
			}
			
			return handleMenuAction(x, libgdxy);
		}
		
		return false;
	}
	public Boolean createAdr(float x, float y)
	{
		Vector3 endAddressPlacing=new Vector3();
		endAddressPlacing.x=x;
		endAddressPlacing.y=y;
		endAddressPlacing=town.gameCam.unproject(endAddressPlacing);
		
		float sx=startAddressPlacing.x;
		float sy=startAddressPlacing.y;
		float ex=endAddressPlacing.x;
		float ey=endAddressPlacing.y;
		
		if(endAddressPlacing.x<sx)
		{
			sx=endAddressPlacing.x;
			ex=startAddressPlacing.x;
		}
		
		if(endAddressPlacing.y<sy)
		{
			sy=endAddressPlacing.y;
			ey=startAddressPlacing.y;
		}
					
		int adrSize = (int)(Math.abs(ex-sx)*Math.abs(ey-sy));
		
		if(adrSize<60000)
			return true;
		
    	Boolean bPlacingOK = town.gameWorld.checkAddressPlacing(new Rectangle(sx, sy, Math.abs(ex-sx), Math.abs(ey-sy)));
    	if(!bPlacingOK)
    	{
    		Gdx.input.setCursorPosition(Gdx.input.getX(), Gdx.input.getY()); //wegen cursor catched
    		bAddressPlacing=false;
    		return true;
    	}
		
		int price = CAddress.getPrice(adrSize, addressPlacingType, town);
		int price_real = price;
		int price_planning = 0; 
		if(price_real>0 && town.gameWorld.addressArchitectPlanningValue>0)
		{
			price_planning = price_real/100*town.gameWorld.addressArchitectPlanningValue;
			price_real = price_real - price_real/100*town.gameWorld.addressArchitectPlanningValue;
		}
		
		Gdx.input.setCursorPosition(Gdx.input.getX(), Gdx.input.getY()); //wegen cursor catched
		if(price_real <= town.gameWorld.townMoney)
		{
			String adrtype = addressPlacingType;
			if(adrtype.contains("residential"))
				adrtype="residential";
			else 
				adrtype="public";
			CAddress address = new CAddress(sx, sy, ex, ey, "", town, adrtype);
			textInputDlg.setAddress(address, price_real);
			
			if(town.gameConfigIni.displayaddressinput)
			{
				textInputDlg.showDlg(true, TextInputType.ADDRESS);
			}
			else
			{
				address.addressName = address.addressId + " Main Street";
				town.gameWorld.addressArchitectPlanningValue=0;
				town.gameWorld.worldAddressList.add(address);
				town.gameWorld.changeTownMoney(-address.getPrice());
				town.gameWorld.townStatistics.getCurrentStatistics_Finance().buyAddress+=Math.abs(address.getPrice());
			}
			
			town.setAchievement("firstaddress");
		}
		
		bAddressPlacing = false;
		
		return true;
	}
	
	public void changeAddressSize()
	{
		//town.gameAudio.playSound("effect_address", 0, false);
	}
	public Boolean mouseMovedDrag(int x, int y, int libgdxy)
	{

		//if(town.gameWorld.markerObject!=null)
		//{
		//	Gdx.app.debug("mouseMovedDrag 1 log marker", "markerObject.pos_x(): " + town.gameWorld.markerObject.pos_x() + ", markerObject.pos_y(): " + town.gameWorld.markerObject.pos_y());
		//}

		//Roadplacing
		if(bObjPlacing && objPlacing!=null && objPlacing.ATTR_RPLACING>0)// (objPlacing.editoraction.contains("road_road_road") || objPlacing.editoraction.contains("defensewall")))
		{
			//placeStartX=0;
			//placeStartY=0;
			return true;
		}

		
		checkMoveObject(x, libgdxy);

		//if(town.gameWorld.markerObject!=null)
		//	Gdx.app.debug("mouseMovedDrag 2 log marker", "markerObject.pos_x(): " + town.gameWorld.markerObject.pos_x() + ", markerObject.pos_y(): " + town.gameWorld.markerObject.pos_y());

		
		if(bAddressResizing || bAddressPlacing)
			changeAddressSize();
		
		if(bAddressResizing)
		{
			town.gameWorld.doAddressResizing((float)x, (float)y);
			return true;
		}
		
		//		if(createResidentDlg.dlgShowing())
		//		{
		//			createResidentDlg.mouseMovedDrag(x,y,libgdxy);
		//		}
				
		//		if(createAddressDlg.dlgShowing())
		//		{
		//			createAddressDlg.mouseMovedDrag(x,y,libgdxy);
		//		}		


		
		for(CGuiDialog dlg : listDialogs)
		{
			if(dlg.dlgShowing())
				dlg.mouseMovedDrag(x,y,libgdxy);
		}

		if(blueprintDlg.dlgShowing())
			blueprintDlg.mouseMovedDrag(x,y,libgdxy);

		
		if(bSliderDown)
		{
			//speedControllerX=x;
			speedControllerX=x-iSliderDiff;
		}
		
		if(bDeletemode && bButtonDown)
		{
			checkDeleteObject(x, y, libgdxy);
			return true;
		}		
		
		if(bRoomPlacing && bButtonDown)
		{
			roomPlacing(x, y);
		}			
		
		if(bObjPlacing && objPlacing!=null && bButtonDown)
		{
			placeSingleObject(x,y, libgdxy, "mouseMoved", false, true);
		}
		
		if(town.gameWorld.markerObject!=null && bButtonDown_MoveObject)
		{
			placeSingleObject(x,y, libgdxy, "mouseMoved", true, true);
		}
		
		return true;
	}
	public Boolean mouseScrolled(int amount)
	{
		if(town.gameWorld.markerObject!=null && town.gameWorld.markerObject.theobject.isGroundBaseObject)
			return false;
		if(objPlacing!=null && objPlacing.isGroundBaseObject)
			return false;
		if(town.gameWorld.markerObject!=null && town.gameWorld.markerObject.theobject.editoraction.contains("illuminati_defense"))
			return false;
		if(objPlacing!=null && objPlacing.editoraction.contains("illuminati_defense"))
			return false;
		if(town.gameWorld.markerObject!=null && town.gameWorld.markerObject.theobject.editoraction.contains("company_waterworks_groundwaterextractionsystem"))
			return false;
		
		//Markerobject
		if(town.gameWorld.markerObject!=null)
		{
			CWorldObject mobj = town.gameWorld.markerObject;
			
			Boolean bCarpet=false;
			if(town.gameWorld.markerObject.theobject.editoraction.contains("anyroom_carpet") && bResizeCarpetActive)
				bCarpet=true;
			
			Boolean bGround=false;
			if(town.gameWorld.markerObject.theobject.isGroundObject && bResizeGroundActive && !town.gameWorld.markerObject.theobject.isGroundBaseObject)
				bGround=true;
			
			//Ground darf nicht rotiert werden, wenn Objekt drauf
			if(town.gameWorld.markerObject.theobject.isGroundObject && !bResizeGroundActive)
			{
				Boolean bCol = town.gameWorld.isFloorUnderObject(mobj);
				if(bCol)
					return false;
			}
			
			//Resize Room
			if(mobj.theobject.isRoomObject || bGround || bCarpet)
			{
				Boolean bResearched = false;
				//if(town.gameWorld.markerObject.theobject.isRoomObject)
				//	bResearched=town.gameWorld.gameResourceConfig.isObjectResearched("function_resizeroom");
				//if(town.gameWorld.markerObject.theobject.isGroundObject)
				//	bResearched=town.gameWorld.gameResourceConfig.isObjectResearched("function_resizeground");
				
				bResearched=true;
				
				//if(bCarpet)
				//	bResearched=true;
				
				int price=25;
				
				if(town.gameWorld.markerObject.theobject.isGroundObject)
					price=10;
				
				if(town.gameWorld.markerObject.theobject.editoraction.contains("anyroom_carpet"))
					price=10;
				
				if(!bResearched || town.gameWorld.townMoney<price) 
					return false;
				
				if(mobj.scrollwidth==0)
				{
					mobj.scrollwidth=mobj.width;
					mobj.scrollheight=mobj.height;
				}
				
				int resval = 1;
				if(amount==1)
				{
					if(town.gameWorld.markerObject.theobject.editoraction.contains("anyroom_carpet") && town.gameWorld.markerObject.width>600)
						return true;
					
					town.gameWorld.markerObject.setDynamicSize(1);
					
					if(town.gameWorld.markerObject.theobject.isRoomObject || town.gameWorld.markerObject.theobject.isGroundObject)
					{
						if(Gdx.input.isKeyPressed(Keys.X))
						{
							town.gameWorld.markerObject.dynamicwidth+=(town.sizechangervalue*(resval));
						}
						else if(Gdx.input.isKeyPressed(Keys.C))
						{
							town.gameWorld.markerObject.dynamicheight+=town.sizechangervalue;
						}
						else
						{
							town.gameWorld.markerObject.dynamicwidth+=(town.sizechangervalue*(resval));
							town.gameWorld.markerObject.dynamicheight+=town.sizechangervalue;
						}
					}
					else
					{
						town.gameWorld.markerObject.dynamicwidth+=(town.sizechangervalue*(resval));
						town.gameWorld.markerObject.dynamicheight+=town.sizechangervalue;
					}
					
					//town.gameWorld.markerObject.dynamicwidth+=(town.floorrastersize*(mobj.width/mobj.height));
					//town.gameWorld.markerObject.dynamicheight+=town.floorrastersize;
					town.gameWorld.markerObject.setDynamicSize(0);
					
					if(town.gameWorld.markerObject.theobject.editoraction.contains("anyroom_carpet"))
					{
						if(!town.gameWorld.checkObjectPlacing(town.gameWorld.markerObject.theobject, null))
						{
							town.gameWorld.markerObject.dynamicwidth-=(town.sizechangervalue*(resval));
							town.gameWorld.markerObject.dynamicheight-=town.sizechangervalue;
							town.gameWorld.markerObject.setDynamicSize(0);
							return true;
						}
					}
					
					if(town.gameWorld.markerObject.theobject.isRoomObject)
					{
						CAddress adr = town.gameWorld.getAddressByPolygonInside(town.gameWorld.markerObject.getBoundingPolygon(0));
						if(adr==null)
						{
							if(Gdx.input.isKeyPressed(Keys.X))
								town.gameWorld.markerObject.dynamicwidth-=town.sizechangervalue;
							else if(Gdx.input.isKeyPressed(Keys.C))
								town.gameWorld.markerObject.dynamicheight-=town.sizechangervalue;
							else
							{
								town.gameWorld.markerObject.dynamicwidth-=town.sizechangervalue;
								town.gameWorld.markerObject.dynamicheight-=town.sizechangervalue;
							}
							
							
							town.gameWorld.markerObject.setDynamicSize(0);
							return true;
						}
					}
					
					//Kollision mit anderen Floors, Outside Objects
					if(town.gameWorld.markerObject.theobject.isRoomObject)
					{
						Boolean breset=false;
						
						if(!town.gameWorld.checkObjectPlacing(town.gameWorld.markerObject.theobject, null))
							breset=true;
						
						if(breset)
						{
							
							if(Gdx.input.isKeyPressed(Keys.X))
								town.gameWorld.markerObject.dynamicwidth-=town.sizechangervalue;
							else if(Gdx.input.isKeyPressed(Keys.C))
								town.gameWorld.markerObject.dynamicheight-=town.sizechangervalue;
							else
							{
								town.gameWorld.markerObject.dynamicwidth-=town.sizechangervalue;
								town.gameWorld.markerObject.dynamicheight-=town.sizechangervalue;
							}							
							//town.gameWorld.markerObject.dynamicwidth-=town.floorrastersize;
							//town.gameWorld.markerObject.dynamicheight-=town.floorrastersize;
							
							
							town.gameWorld.markerObject.setDynamicSize(0);
							return true;
						}
					}
					
					return true;
				}
				
				if(amount==-1)
				{
					int minsize=town.sizechangervalue;
					if(town.gameWorld.markerObject.theobject.editoraction.contains("anyroom_carpet"))
						minsize=250;
					
					if(town.gameWorld.markerObject.width>minsize)
					{
						int count1 = town.gameWorld.isFloorUnderObject_Count(town.gameWorld.markerObject);
						town.gameWorld.markerObject.setDynamicSize(1);
						
						
						if(town.gameWorld.markerObject.theobject.isRoomObject || town.gameWorld.markerObject.theobject.isGroundObject)
						{
							if(Gdx.input.isKeyPressed(Keys.X))
							{
								town.gameWorld.markerObject.dynamicwidth-=(town.sizechangervalue*(resval));
							}
							else if(Gdx.input.isKeyPressed(Keys.C))
							{
								town.gameWorld.markerObject.dynamicheight-=town.sizechangervalue;
							}
							else
							{
								town.gameWorld.markerObject.dynamicwidth-=(town.sizechangervalue*(resval));
								town.gameWorld.markerObject.dynamicheight-=town.sizechangervalue;
							}
						}
						else
						{
							town.gameWorld.markerObject.dynamicwidth-=(town.sizechangervalue*(resval));
							town.gameWorld.markerObject.dynamicheight-=town.sizechangervalue;
						}						
						//town.gameWorld.markerObject.dynamicwidth-=(town.floorrastersize*(mobj.width/mobj.height));
						//town.gameWorld.markerObject.dynamicheight-=town.floorrastersize;
						town.gameWorld.markerObject.setDynamicSize(0);
						int count2 = town.gameWorld.isFloorUnderObject_Count(town.gameWorld.markerObject);
						
						if(count1!=count2)
						{
							
							if(Gdx.input.isKeyPressed(Keys.X))
								town.gameWorld.markerObject.dynamicwidth+=town.sizechangervalue;
							else if(Gdx.input.isKeyPressed(Keys.C))
								town.gameWorld.markerObject.dynamicheight+=town.sizechangervalue;
							else
							{
								town.gameWorld.markerObject.dynamicwidth+=town.sizechangervalue;
								town.gameWorld.markerObject.dynamicheight+=town.sizechangervalue;
							}
							
							//town.gameWorld.markerObject.dynamicwidth+=(town.floorrastersize*(resval));
							//town.gameWorld.markerObject.dynamicheight+=town.floorrastersize;
							
							town.gameWorld.markerObject.setDynamicSize(0);
						}
						else
						{
							town.gameWorld.changeTownMoney((int)-price);
						}
					}
				}
				
				return true;
			}
			
			if(town.gameWorld.markerObject.theobject.isRoomObject)// || gameWorld.markerObject.theobject.isGroundObject)
				return false;
			if(town.gameWorld.markerObject.thehuman!=null)
				return false;
			if(town.gameWorld.markerObject.theobject.editoraction.contains("road_road") && !town.gameWorld.markerObject.theobject.editoraction.contains("parkingspace"))
				return false;
			
			float origin_rot=town.gameWorld.markerObject.rotation();
			
			if(amount==1)
			{
				town.gameWorld.markerObject.setRotation(town.gameWorld.markerObject.rotation()+town.gameWorld.markerObject.theobject.rotationValue);
				if(town.gameWorld.markerObject.rotation()>360)
					town.gameWorld.markerObject.setRotation(town.gameWorld.markerObject.theobject.rotationValue);
			}
			else if(amount==-1)
			{
				town.gameWorld.markerObject.setRotation(town.gameWorld.markerObject.rotation()-town.gameWorld.markerObject.theobject.rotationValue);
				if(town.gameWorld.markerObject.rotation()<0)
					town.gameWorld.markerObject.setRotation(360-town.gameWorld.markerObject.theobject.rotationValue);					
			}
			
			town.gameGui.objPlacing=town.gameWorld.markerObject.theobject;
			
			if(!bButtonDown_MoveObject)
			{
				if(!town.gameWorld.checkObjectPlacing(town.gameWorld.markerObject.theobject, null))
					town.gameWorld.markerObject.setRotation(origin_rot);
			}
			
			if(!bButtonDown_MoveObject)
				town.gameGui.objPlacing=null;
			
			//Human: aktuelle Action und die der occupied abbrechen
			town.gameWorld.markerObject.cancelAction1();
			town.gameWorld.markerObject.resetPathFinding();
			
			return true;
		}
		
		//Objectplacing
//		if(bObjPlacing && (objPlacing.isRoomObject)) // || objPlacing.isGroundObject))
//		{
//			if(amount==1)
//			{
//				objPlacing.placingCountLevel++;
//				if(objPlacing.placingCountLevel>town.placingCountMax)
//					objPlacing.placingCountLevel=1;
//			}
//			
//			if(amount==-1)
//			{
//				objPlacing.placingCountLevel--;
//				if(objPlacing.placingCountLevel<1)
//					objPlacing.placingCountLevel=town.placingCountMax;
//			}
//			
//			return true;
//		}
		
		if(bObjPlacing && !objPlacing.editoraction.contains("buildingwall")) //Wall kann nicht rotiert werden -> nicht notwendig und Schatten passen nicht mehr
		{
			if(objPlacing.editoraction.contains("road_road") && !objPlacing.editoraction.contains("road_road_parkingspace"))
				return false;
			
			if(objPlacing.editoraction.contains("waterworks_groundwaterextractionsystem"))
			{
				if(!town.gameWorld.gameResourceConfig.isObjectResearched("function_resizewatersystem"))
					return false;
			}
			
			if(objPlacing.editoraction.contains("outdoor_tree") || 
					objPlacing.editoraction.contains("outdoor_plant") || 
					objPlacing.editoraction.contains("outdoor_flower") ||
					objPlacing.editoraction.contains("indoor_plant") || 
					objPlacing.editoraction.contains("waterworks_groundwaterextractionsystem") ||
					objPlacing.isRoomObject ||
					objPlacing.isGroundObject
				)
			{
				int minsize=(int) town.getSizeValue(50);
				int maxsize=(int) town.getSizeValue(1700);
				int sval=(int) town.getSizeValue(50);
				
				if(objPlacing.editoraction.contains("anyroom_carpet"))
				{
					sval=(int) town.getSizeValue(10);
					minsize=(int) town.getSizeValue(100);
					maxsize=(int) town.getSizeValue(800);
				}
				
				if(objPlacing.editoraction.contains("outdoor_plant"))
				{
					sval=(int) town.getSizeValue(5);
					minsize=(int) town.getSizeValue(10);
					maxsize=(int) town.getSizeValue(120);
				}
				
				if(objPlacing.editoraction.contains("outdoor_flower"))
				{
					sval=(int) town.getSizeValue(5);
					minsize=(int) town.getSizeValue(10);
					maxsize=(int) town.getSizeValue(60);
				}
				
				if(objPlacing.editoraction.contains("indoor_plant"))
				{
					sval=(int) town.getSizeValue(10);
					minsize=(int) town.getSizeValue(100);
					maxsize=(int) town.getSizeValue(150);
				}
				
				if(objPlacing.editoraction.contains("waterworks_groundwaterextractionsystem"))
				{
					sval=100;
					minsize=400;
					maxsize=objPlacing.getMaxSize(0);
				}
				
				if(objPlacing.isGroundObject)
				{
					sval=town.sizechangervalue*4;
					//minsize=128;
					minsize=250;
					maxsize=2000;
				}
				
				if(objPlacing.isRoomObject)
				{
					sval=town.sizechangervalue;
					minsize=128;
					maxsize=2000;
				}
				
				int price = (sval/3); //getMultiPlacingCount(objPlacing);
				
				if(amount==1)
				{
					if(objPlacing.width<maxsize)
					{
						if(objPlacing.isGroundObject || objPlacing.isRoomObject)
						{
							objPlacing.tempprice = objPlacing.getResizeByScrollingPrice(objPlacing.original_height+objPlacing.original_width, objPlacing.width+sval+objPlacing.height+sval);
							setMultiPlacingPrice(objPlacing);
							//Gdx.app.debug("", ""+ getMultiPlacingPrice(objPlacing) + ", " + objPlacing.getResizeByScrollingPrice(objPlacing.original_height+objPlacing.original_width, objPlacing.width+sval+objPlacing.height+sval));
						}
						else
						{
							objPlacing.tempprice+=price;
							setMultiPlacingPrice(objPlacing);
						}

						if(objPlacing.isRoomObject || objPlacing.isGroundObject)
						{
							if(Gdx.input.isKeyPressed(Keys.X))
							{
								objPlacing.width+=sval;
							}
							else if(Gdx.input.isKeyPressed(Keys.C))
							{
								objPlacing.height+=sval;
							}
							else
							{
								objPlacing.width+=sval;
								objPlacing.height+=sval;
							}
						}
						else
						{
							objPlacing.width+=sval;
							objPlacing.height+=sval;
						}
					}
				}
				else if(amount==-1)
				{
					if(objPlacing.width>minsize)
					{
						if(objPlacing.isGroundObject || objPlacing.isRoomObject)
						{
							objPlacing.tempprice = objPlacing.getResizeByScrollingPrice(objPlacing.original_height+objPlacing.original_width, (objPlacing.width-sval)+(objPlacing.height-sval));
							setMultiPlacingPrice(objPlacing);
						}
						else
						{
							objPlacing.tempprice-=price;
							
							setMultiPlacingPrice(objPlacing);
							
							if(objPlacing.price<5)
								objPlacing.price=5;
						}
												
						if(objPlacing.isRoomObject || objPlacing.isGroundObject)
						{
							if(Gdx.input.isKeyPressed(Keys.X))
							{
								objPlacing.width-=sval;
							}
							else if(Gdx.input.isKeyPressed(Keys.C))
							{
								objPlacing.height-=sval;
							}
							else
							{
								objPlacing.width-=sval;
								objPlacing.height-=sval;
							}
						}
						else
						{
							objPlacing.width-=sval;
							objPlacing.height-=sval;
						}						
					}
				}
				
				if(objPlacing.editoraction.contains("outdoor_ground"))
				{
					//					if(objPlacing.price<45)
					//						objPlacing.price=70;
					//					else if(objPlacing.price<85)
					//						objPlacing.price=85;
					if(objPlacing.price<objPlacing.original_price)
						objPlacing.price=objPlacing.original_price;
				}
				
				if(objPlacing.isRoomObject)
				{
					if(objPlacing.price<objPlacing.original_price)
						objPlacing.price=objPlacing.original_price;
				}				
				
				if(objPlacing.editoraction.contains("company_waterworks_groundwaterextractionsystem"))
				{
					objPlacing.setDynamicAttributes();
					//objPlacing.price=objPlacing.width*21; //8500
					objPlacing.price=8500+((objPlacing.width-objPlacing.original_width)*15);
				}
			}
			else
			{
				if(amount==1)
				{
					objPlacing.rotation+=objPlacing.rotationValue;
					if(objPlacing.rotation>360)
						objPlacing.rotation=objPlacing.rotationValue;
				}
				else if(amount==-1)
				{
					objPlacing.rotation-=objPlacing.rotationValue;
					if(objPlacing.rotation<0)
						objPlacing.rotation=360-objPlacing.rotationValue;				
				}
			}
			
			return true;
		}
		
		return false;
	}
	
	private void handleGuiButtonClick()
	{
		town.gameWorld.resetMarkerObject();
	}
	
	public Boolean buttonDown(int x, int y, int libgdxy, int button)
	{
		//ACHTUNG
		//REIHENFOLGE SPIELT HIER EINE ROLLE -> hier einfügen: "HIER EINFÜGEN"
		
		try
		{
		
		if(button==0)
		{
			if(bAddressPlacing && startAddressPlacing!=null)
			{
				return true; // createAdr();
			}
		}
		
		
		if(button==1)
		{
			if(bPaintObject && town.gameWorld.markerObject!=null)
			{
				if(!chooseColorDlg.dlgShowing())
					bPaintObject=false;
			}
		}
		
		Boolean guiDlg=false;
		Boolean bObjectInfoDlg=false;
		Boolean bTimeControl=false;
		
		CObject objControl = getControlCollision_Objects(x, libgdxy);
		if(objControl!=null)
			bTimeControl=true;
		
		if(buttonX2.buttonClick(x, libgdxy))
		{
			if(buttonX2.toggleActive)
				buttonX4.toggleActive=false;
			//handleGuiButtonClick();
			return true;
		}
		
		if(buttonX4.buttonClick(x, libgdxy))
		{
			if(buttonX4.toggleActive)
				buttonX2.toggleActive=false;
			//handleGuiButtonClick();
			return true;
		}
		
		//if(gameWorld.worldPause)
		//	return true;
		
		
		//*******
		//Dialogs
		//*******
		


		
		int count = listDialogs.size();
		
		for(int i=count-1;i>-1;i--)
		{
			CGuiDialog dlg = listDialogs.get(i);
			
			if(dlg.dlgShowing())
			{
				if(dlg.buttonDown(x, y, libgdxy, button))
				{
					guiDlg=true;
					return true;
				}
			}
		}				
		
		if(blueprintDlg.dlgShowing())
		{
			if(blueprintDlg.buttonDown(x, y, libgdxy, button))
			{
				//guiDlg=true;
				return true;
			}
		}
		
		if(objectInfoDlg.dlgShowing())
		{
			if(objectInfoDlg.buttonDown(x, y, libgdxy, button))
				bObjectInfoDlg=true;
		}
		
		if(bTimeControl || bObjectInfoDlg)
			return true;
		
		if(bButtonDown_MoveObject)
			return true;
		
		bButtonDown=true;
		bButtonDown_MoveObject=false;
		
		//Wenn sich Mouseoverobject nicht ändert dann Markerobject nicht zurücksetzen
		Boolean bReset=true;
		if(town.gameWorld.markerObject!=null && town.gameWorld.mouseOverObject != null && town.gameWorld.mouseOverObject.uniqueId==town.gameWorld.markerObject.uniqueId)
			bReset=false;
		if(bReset)
			town.gameWorld.resetMarkerObject();
		
		//Deletemode abbrechen
		if(bDeletemode && button==1) 
		{
			Gdx.input.setCursorPosition(Gdx.input.getX(), Gdx.input.getY()); //wegen cursor catched
			bDeletemode=false;
			return true;
		}
		
		//Addressplacing abbrechen
		if(bAddressPlacing && button==1) 
		{
			Gdx.input.setCursorPosition(Gdx.input.getX(), Gdx.input.getY()); //wegen cursor catched
			if(startAddressPlacing!=null)
			{
				startAddressPlacing=null;
				return true;
			}
			else
			{
				bAddressPlacing=false;
				return true;
			}
		}		 
		
		//Room cloning abbrechen
		if(bRoomCloning && button==1) 
		{
			Gdx.input.setCursorPosition(Gdx.input.getX(), Gdx.input.getY()); //wegen cursor catched
			bRoomCloning=false;
			for(CWorldObject wobj : town.gameWorld.cloneRoomList)
				wobj.dispose();
			town.gameWorld.cloneRoomList.clear();
		}			
		
		//Room cloning placen
		if(bRoomCloning && button==0) 
		{
			int price = town.gameWorld.getCloneRoomPrice();
			CWorldObject room = town.gameWorld.getCloneRoom();
			if(!town.gameWorld.checkObjectPlacing(room.theobject, null))
				return true;
			
			if(town.gameWorld.townMoney>=price)
			{
				town.gameWorld.townStatistics.getCurrentStatistics_Finance().buyObject+=Math.abs(price);
				town.gameWorld.changeTownMoney(-price);
				
				for(CWorldObject wobj : town.gameWorld.cloneRoomList)
				{
					if(wobj.theobject.isRoomObject)
					{
						town.gameGui.addRoomObject(new Vector2(wobj.pos_x(), wobj.pos_y()), wobj);
					}
					else
					{
						town.gameGui.addWorldObject(wobj, false);
					}
				}
				
				town.gameWorld.bRenderFrameBuffer=true;
				//bRoomCloning=false;
				//town.gameWorld.cloneRoomList.clear();
								
				town.gameWorld.cloneRoomList.clear();
				CWorldObject newroom = room.clone();
				town.gameWorld.cloneRoomList.add(newroom);
				count=0;
				for(CWorldObject wobj : room.theaddress.listWorldObjects)
				{
					if(room.testpoint(wobj.pos_x()+wobj.width/2, wobj.pos_y()+wobj.height/2, IntersectionMode.COLLISION))
					{
						if(!wobj.isHuman())
						{
							count++;
							CWorldObject co = wobj.clone();
							co.uniqueId+=count;
							co.theaddressid=0;
							co.theaddress=null;
							town.gameWorld.cloneRoomList.add(co);
						}
					}
				}
				
				Gdx.input.setCursorPosition(Gdx.input.getX(), Gdx.input.getY()); //wegen cursor catched
			}
		}			
		
		//Addresscloning starten
		if(button==0 && bAddressCloning && cloneAddress!=null)
		{
	    	int icprice=cloneAddress.getCloningPrice();
	    	int addressprice=cloneAddress.getPrice();
			int price_real = icprice;
			int price_planning = 0; 
			
			if(price_real>0 && town.gameWorld.addressArchitectPlanningValue>0)
			{
				price_planning = addressprice/100*town.gameWorld.addressArchitectPlanningValue;
				price_real = price_real - addressprice/100*town.gameWorld.addressArchitectPlanningValue;
			}
			
			if(town.gameWorld.townMoney >= price_real)
			{
				if(clonedAddress == null)
				{
					clonedAddress = cloneAddress.clone();
					town.gameWorld.cloneAddressList.clear();
					clonedAddress.isCloning=true;
					town.gameWorld.cloneAddressList.add(clonedAddress);
				}
			}
			
			return true;
		}
		
		//Addressmoving starten
		if(button==0 && bAddressMoving && moveAddress!=null && movingAddress==null) 
		{
			Vector3 c0 = new Vector3(x, y, 0);
			Vector3 c1 = town.gameCam.unproject(c0);
			
			moveX=(int) (c1.x-moveAddress.sx); // "Jump" des Objekts zum Cursor verhindern
			moveY=(int) (c1.y-moveAddress.sy);
			
			movingAddress=moveAddress;
			movingAddress.cancelActions();
			
			return true;
		}
		
		//Place Address
		if(button==0 && movingAddress!=null && bAddressMoving)
		{
			if(town.gameWorld.checkAddressPlacing(movingAddress.getBoundingRect(CAddress.AddressOverlap.RESIZE)))
			{
				moveAddress=null;
				movingAddress=null;
				moveAddress_Origin_x=0;
				moveAddress_Origin_y=0;
				bAddressMoving=false;
			}
			
			return true;
		}
		
		
		//Cancel Address Moving
		
		//Fall 1: Start Mode
		if(button==1 && movingAddress==null && bAddressMoving)
		{
			int architect_costs = CCompany.getArchitectCosts(town, ArchitectWorkType.MOVE);
			tempActionObject.belongsToCompany.addWorkOutput(architect_costs, WorkoutputType.DEFAULT);
			town.gameWorld.bRenderFrameBuffer=true;
			bAddressMoving=false;
			moveAddress=null;
			movingAddress=null;
			moveAddress_Origin_x=0;
			moveAddress_Origin_y=0;
		}
		
		//Fall 2: Adr is moving
		if(button==1 && movingAddress!=null && bAddressMoving)
		{
			int architect_costs = CCompany.getArchitectCosts(town, ArchitectWorkType.MOVE);
			//movingAddress.getCompany().addWorkOutput(architect_costs, WorkoutputType.DEFAULT);
			tempActionObject.belongsToCompany.addWorkOutput(architect_costs, WorkoutputType.DEFAULT);
			
			Gdx.input.setCursorPosition(Gdx.input.getX(), Gdx.input.getY()); //wegen cursor catched
			
			float ax=movingAddress.sx;
			float ay=movingAddress.sy;
			
			movingAddress.ex=moveAddress_Origin_x+(movingAddress.ex-movingAddress.sx);
			movingAddress.ey=moveAddress_Origin_y+(movingAddress.ey-movingAddress.sy);
			movingAddress.sx=moveAddress_Origin_x;
			movingAddress.sy=moveAddress_Origin_y;
			
	    	float diffX = movingAddress.sx-ax;
	    	float diffY = movingAddress.sy-ay;
			
			for(CWorldObject wobj : movingAddress.listWorldObjects)
			{
				if(!wobj.isHuman())
					wobj.setPosition(wobj.pos_x()+Math.round(diffX), wobj.pos_y()+Math.round(diffY));
			}

			for(CWorldObject wobj : movingAddress.listWorldObjects_Floors)
			{
				wobj.setPosition(wobj.pos_x()+Math.round(diffX), wobj.pos_y()+Math.round(diffY));
			}
			
			for(CWorldObject wobj : movingAddress.listWorldObjects_Ground)
			{
				wobj.setPosition(wobj.pos_x()+Math.round(diffX), wobj.pos_y()+Math.round(diffY));
			}			
			
			town.gameWorld.bRenderFrameBuffer=true;
			
			moveAddress=null;
			movingAddress=null;
			moveAddress_Origin_x=0;
			moveAddress_Origin_y=0;
			bAddressMoving=false;
			
			return true;
		}
		
		//Place cloned address
		if(button==0 && clonedAddress!=null)
		{
	    	//int icprice=cloneAddress.getCloningPrice();
	    	//int addressprice=cloneAddress.getPrice();
	    	int icprice=clonedAddress.getCloningPrice();
	    	int addressprice=clonedAddress.getPrice();
	    	
			int price_real = icprice;
			int price_planning = 0; 
			
			if(price_real>0 && town.gameWorld.addressArchitectPlanningValue>0)
			{
				price_planning = addressprice/100*town.gameWorld.addressArchitectPlanningValue;
				price_real = price_real - addressprice/100*town.gameWorld.addressArchitectPlanningValue;
			}
			
			//Gdx.app.debug("check123", "gameWorld.townMoney: " + gameWorld.townMoney + ", price_real: " + price_real);
			
			ArrayList<String> listre = clonedAddress.allObjectsResearchedOrUnlocked();
			if(listre.size()>0) {
				yesnoDlg.listre=listre;
				yesnoDlg.showDlg(true, YesNoDlgType.NOTRESEARCHED);
				return true;
			}
			
			//Boolean bunlocked = clonedAddress.allUnlocked();
			//if(!bunlocked) {
			//	yesnoDlg.showDlg(true, YesNoDlgType.NOTUNLOCKED);
			//	return true;
			//}
			
			if(town.gameWorld.townMoney<=price_real)
				return true;
			
			if(town.gameWorld.checkAddressPlacing(clonedAddress.getBoundingRect(CAddress.AddressOverlap.RESIZE)))
			{
				bAddressCloning=false;
				cloneAddress=null;
				textInputDlg.setAddress(clonedAddress, price_real);
				textInputDlg.sDialogInput1="";
				
				if(town.gameConfigIni.displayaddressinput)
				{
					textInputDlg.showDlg(true, TextInputType.CLONE_ADDRESS);
					clonedAddress=null;
				}
				else
				{
					textInputDlg.placeClonedAddress();
					if(town.gameGui.realEstateCloneName=="")
						clonedAddress=null;
				}
				
			 }
			 
			 return true;
		}
		
		
		//Cancel address cloning
		if(bAddressCloning && button==1) //Addresscloning abbrechen
		{
			if(clonedAddress!=null)
			{
				clonedAddress.isCloning=false;
			
				int architect_costs = CCompany.getArchitectCosts(town, ArchitectWorkType.CLONE);
				if(tempActionObject!=null && tempActionObject.belongsToCompany!=null)
					tempActionObject.belongsToCompany.addWorkOutput(architect_costs, WorkoutputType.DEFAULT);
				
				Gdx.input.setCursorPosition(Gdx.input.getX(), Gdx.input.getY()); //wegen cursor catched
				
				town.gameWorld.cloneAddressList.clear();
				
				if(clonedAddress!=null)
					clonedAddress.dispose();
			}
			
			realEstateCloneName="";
			clonedAddress=null;
			cloneAddress=null;
			bAddressCloning=false;
		}	
		
		if(bAddressPlacing && button==0) //Addressplacing starten
		{
			startAddressPlacing=new Vector3();
			startAddressPlacing.x=x;
			startAddressPlacing.y=y;
			startAddressPlacing = town.gameCam.unproject(startAddressPlacing);
			return true;
		}	
		
		if(bDeletemode)
		{
			int sid=-1;
			if(town.gameWorld.delAddress!=null)
					sid = town.gameWorld.delAddress.addressId;
			
			//if(gameWorld.delAddress!=null && gameWorld.delAddress.listWorldObjects.size()==0)
			if(town.gameWorld.delAddress!=null)
			{
				town.gameWorld.sellAddress(town.gameWorld.delAddress);
				town.gameWorld.delAddress=null;
				town.gameWorld.bRenderFrameBuffer=true;
				return true;
			}
			
			checkDeleteObject(x, y, libgdxy);
			return true;
		}
		
		if(bRoomPlacing)
		{
			roomPlacingStart(x,y,libgdxy, button);
			return true;
		}
		
		if(bObjPlacing && objPlacing!=null)
		{
			bButtonDown=true;
			if(button==1)
			{
				//if(objPlacing.editoraction.contains("company_waterworks_groundwaterextractionsystem"))
				{
					//Gdx.app.debug("", ""+objPlacing.editoraction + ", objPlacing.original_width: " + objPlacing.original_width + ", width: " + objPlacing.width);
					placeStartX=0;
					placeStartY=0;
					rPlacingPrice=0;
					
					if(objPlacing.original_price>0)
						objPlacing.price=objPlacing.original_price;
					if(objPlacing.original_width>0)
						objPlacing.width=objPlacing.original_width;
					if(objPlacing.original_height>0)
						objPlacing.height=objPlacing.original_height;
					
					objPlacing.setDynamicAttributes();
				}				
				
				bButtonDown = false;
				bObjPlacing = false;
				objPlacing = null;
				return true;
			}
			
			//Roadplacing
			if(objPlacing.ATTR_RPLACING>0)// objPlacing.editoraction.contains("road_road_road") || objPlacing.editoraction.contains("defensewall"))
			{
				placeStartX=objPlacing.pos_x;
				placeStartY=objPlacing.pos_y;
				
				if(town.gameGui.objPlacing.iRasterValue>0)
				{
					placeStartX = (int)(placeStartX / town.gameGui.objPlacing.iRasterValue);
					placeStartX = (int)(placeStartX * town.gameGui.objPlacing.iRasterValue);
					placeStartY = (int)(placeStartY / town.gameGui.objPlacing.iRasterValue);
					placeStartY = (int)(placeStartY * town.gameGui.objPlacing.iRasterValue);
				}
				placeStartX+=town.gameGui.objPlacing.iRasterValue_movx;
				placeStartY+=town.gameGui.objPlacing.iRasterValue_movy;
				
				return true;
			}
			
			placeSingleObject(x,y, libgdxy, "buttonDown", false, true);
			return true;
		}
		
		if(button==0)
		{
			CObjecttype objtype = getMenuCollision_ObjectTypes(x, libgdxy);
			if(objtype!=null)
				return true;
			
			CObject obj = getMenuCollision_Objects(x, libgdxy);
			if(obj!=null)
				return true;
		}
		
		
		//HIER EINFÜGEN
				
		if(button==0 && bAddressResizingOver && bAddressResizingStart && !bAddressResizing)
		{
			bAddressResizing=true;
			
			addressResizing_sx=addressResizing.sx;
			addressResizing_ex=addressResizing.ex;
			addressResizing_sy=addressResizing.sy;
			addressResizing_ey=addressResizing.ey;
			
			Vector3 vm = new Vector3();
			vm.x=x;
			vm.y=y;
			vm=town.gameCam.unproject(vm);
			addressResizingSide = addressResizing.checkLineIntersect(vm, 100);
			
			return true;
		}		
		
		if(button==1 && (bAddressResizing || bAddressResizingStart))
		{
			int architect_costs = CCompany.getArchitectCosts(town, ArchitectWorkType.RESIZE);
			tempActionObject.belongsToCompany.addWorkOutput(architect_costs, WorkoutputType.DEFAULT);
			
			if(bAddressResizing && addressResizing!=null)
			{
				addressResizing.sx=addressResizing_sx;
				addressResizing.ex=addressResizing_ex;
				addressResizing.sy=addressResizing_sy;
				addressResizing.ey=addressResizing_ey;
				addressResizing=null;
			}
			
			bAddressResizingStart=false;
			bAddressResizing=false;
			
			return true;
		}
		
		if(button==1) //Menü einklappen
		{
			if(getMenuOpen())
			{
				closeMenu();
				return true;
			}
		}
		
		if(button==0)
		{
			//Show Address Residents or Worker
			if(mouseOverAddressInfo!=null)
				listDlg.showDlg(true, mouseOverAddressInfo, ListType.VIEW_RESIDENTS, 1);
			
			//Start Object Moving / Setze Markerobject
			if(!bButtonDown_MoveObject)
			{
				if(checkObjectMarker(x,y,libgdxy)) //wenn Objekt markiert ist, kann es verschoben oder rotiert werden
					return true;
			}
		}
		}
		catch(Exception e)
		{
			CHelper.writeError(e.getMessage(), e.getStackTrace(), e);
		}
		
		
		return false;
	}
	
	public Boolean keyTyped(char character)
	{
//		if(createResidentDlg.dlgShowing())
//		{
//			createResidentDlg.keyTyped(character);
//			return true;
//		}
//		
//		if(createAddressDlg.dlgShowing())
//		{
//			createAddressDlg.keyTyped(character);
//			return true;
//		}		
		
		//for(CGuiDialog dlg : listDialogs)
		for(int i=listDialogs.size()-1;i>-1;i--)
		{
			CGuiDialog dlg = listDialogs.get(i);
			if(dlg.dlgShowing())
			{
				dlg.keyTyped(character);
				return true;
			}
		}
		
		return false;
	}
	public Boolean keyDown(int keycode)
	{
//		if(createResidentDlg.dlgShowing())
//			return true;
//		
//		if(createAddressDlg.dlgShowing())
//			return true;		
		
		for(CGuiDialog dlg : listDialogs)
		{
			if(dlg.dlgShowing())
			{
				return true;
			}
		}	
		
		return false;
	}
	public Boolean keyUp(int keycode)
	{
//		if(createResidentDlg.dlgShowing())
//		{
//	        if (keycode==Input.Keys.ESCAPE)
//	        	createResidentDlg.showDlg(false);	
//			
//			return true;
//		}
//
//		if(createAddressDlg.dlgShowing())
//		{
//	        if (keycode==Input.Keys.ESCAPE)
//	        	createAddressDlg.showDlg(false);	
//			
//			return true;
//		}
		
		
		
		if(textInputDlg.dlgShowing())
		{
	        if (keycode==Input.Keys.ESCAPE)
	        	textInputDlg.showDlg(false);
			
	        if (keycode==Input.Keys.ENTER)
	        	textInputDlg.closeDlgOK();
		}
		
		
		for(CGuiDialog dlg : listDialogs)
		{
			if(dlg.dlgShowing())
			{
		        if (keycode==Input.Keys.ESCAPE)
		        {
		        	if(dlg.bCloseOnEscape)
		        		dlg.showDlg(false);
		        }
				
		        if (keycode==Input.Keys.ENTER)
		        	dlg.closeDlgOK();
		        
				return true;
			}
		}
		
		return false;
	}
	//INPUT<--------------------------------------------------------------
	
	//OTHER-------------------------------------------------------------->
	
	//Funktion wird nicht verwendet
	public Boolean checkObjectMarkerList(int x, int y, int libgdxy, List<CWorldObject> list)
	{
        Vector3 c0 = new Vector3(x,y,0); //Gameworld needs unprojected Coordinates
        Vector3 c1 = town.gameCam.unproject(c0);
        x=(int)c1.x;
        y=(int)c1.y;
        
        int zorder=-10;
        
        for(int i=list.size()-1;i>-1;i--) //In der richtigen Reihenfolge: erst das oberste Objekt
		{
        	CWorldObject obj = list.get(i);
			{
				if(obj.testpoint(x, y, IntersectionMode.MOUSECLICK))
				{
					if(obj.theobject.zorder>zorder)
					{
						zorder=obj.theobject.zorder;
						
						obj.showMarker=true;
						
						if(town.gameWorld.markerObject!=null)
							town.gameWorld.markerObject.showMarker=false;
												
						if(bPaintObject)
							chooseColorDlg.showDlg(true);
						
						town.gameWorld.markerObject=obj;
					}
				}
			}
		}
        
		if(zorder>-10)
			return true;
		
		return false;		
	}
	public Boolean checkObjectMarker(int x, int y, int libgdxy)
	{
		if(town.gameWorld.mouseOverObject==null)
			return false;

		if(bPaintObject && town.gameWorld.mouseOverObject.isHuman())
			return false;
		
		town.gameWorld.markerObject=town.gameWorld.mouseOverObject;

		if(bPaintObject)
			chooseColorDlg.showDlg(true);
		
		return true;
	}
	public void resetReachable()
	{
		//Wenn Objekt gelöscht wurde: Alle Objekte könnten wieder Reachable sein
		//for(CWorldObject obj : gameWorld.worldObjects)
		//	obj.bReachable=true;	
		
		//for(CWorldObject obj : gameWorld.worldHumans)
		//	obj.bReachable=true;	
	}	
	public void startObjectPlacing(CObject obj) 
	{
		//started from menu
		objPlacing = obj;
		if(obj.editoraction.contains("floor"))
			objPlacing.placingCountLevel=town.defaultFloorPlacingCountLevel;
		objPlacing.rotation=0;
		
		//objPlacing.setTempSize(1);
		
		bObjPlacing = true;
	}
	public Boolean dlgShowing()
	{
		for(CGuiDialog dlg : listDialogs)
		{
			if(dlg.dlgShowing())
				return true;
		}
		
		return false;
	}
	public int getAddressResizingPriceDiff()
	{
		//Gdx.app.debug("", "trace adrtype 2: " +addressResizing.addressType);
		
		int oldPrice = (int)CAddress.getPrice((int)(Math.abs(addressResizing_ex-addressResizing_sx)*Math.abs(addressResizing_ey-addressResizing_sy)), addressResizing.addressType, town);
		int oldSellingPrice = (int)CAddress.getSellingPrice((int)(Math.abs(addressResizing_ex-addressResizing_sx)*Math.abs(addressResizing_ey-addressResizing_sy)), addressResizing.addressType, town);
		
		int newprice = addressResizing.getPrice();
		int pricediff = newprice-oldPrice;
		
		if(pricediff<0)
		{
			newprice = addressResizing.getSellingPrice("0");
			pricediff = newprice-oldSellingPrice;
		}
		
		return pricediff;
	}
	//OTHER<--------------------------------------------------------------
	
	//EDITOR------------------------------------------------------------->
	public void createRoomFromTempWorldObjects()
	{
		//Take temp Room Objects from Editor to Render List
		
		town.gameWorld.worldTempRoomObjects.forEach(item->item.initBox2DBody());

//		Hashtable<Vector2, CWorldObject> map1 = null;
//		
//		for(CWorldObject obj : gameWorld.worldTempRoomObjects)
//		{
//			if(obj.theobject.isFloorObject)
//				map1 = gameWorld.worldRoomTilemap_floor;
//
//			if(obj.theobject.isWallObject)
//				map1 = gameWorld.worldRoomTilemap_wall;
//			
//			CWorldObject wobj = map1.get(new Vector2(obj.pos_x, obj.pos_y));
//			if(wobj!=null)
//			{
//				gameWorld.addObjectToPathmap(wobj, false);
//				wobj.dispose();
//			}
//			
//			map1.put(new Vector2(obj.pos_x, obj.pos_y), obj);
//					
//			if(obj.theobject.isWallObject)
//				gameWorld.addObjectToPathmap(obj, true);
//		}
		
		town.gameWorld.worldRoomObjects.addAll(town.gameWorld.worldRoomObjects.size(), town.gameWorld.worldTempRoomObjects);
		
		town.gameWorld.worldTempRoomObjects.clear();
	}	
	public void placeSingleObject(int x, int y, int libgdxy, String action, Boolean moveOnly, Boolean unproject)
	{
		if(objPlacing==null)
			return;

        int x1=(int) x;
        int y1=(int) y;
        
		if(unproject)
		{
			Vector3 c01 = new Vector3(x, y, 0);
	        Vector3 c11 = town.gameCam.unproject(c01);
	        x1=(int) c11.x;
	        y1=(int) c11.y;
		}
		
        //Gdx.app.debug("test123_start2", "x1: " + x1 + ", y1: " + y1);
        		
        if(x1>0 && x1<CWorld.mapsize-objPlacing.width && y1>0 && y1<CWorld.mapsize-objPlacing.height)
        {
        	
        	
        	//***************************
        	//Multiplatzierung oder Move
        	//***************************
			if(action=="mouseMoved")
			{
				if(!moveOnly)
				{
					if(objPlacing.isHuman()) //kein massenplatzieren von einwohnern
						return;					
				}
				
				Vector3 c0 = new Vector3(x, y, 0);
		        Vector3 c1 = town.gameCam.unproject(c0);
		        x=(int) c1.x;
		        y=(int) c1.y;
		        
		        Boolean bSet=false;
		        
		        if(moveOnly)
		        {
		        	x=x-moveX;
		        	y=y-moveY;
		        	
		            if(town.gameGui.objPlacing.doRasterPlacement)
		    		{
		            	if(town.gameGui.objPlacing.editoraction.contains("floor") ||
		            			(town.gameGui.objPlacing.isGroundObject)||
		            			(town.gameGui.objPlacing.isGroundBaseObject)||
		            			(town.gameGui.objPlacing.editoraction.contains("residential_garage"))||
		            			(town.gameGui.objPlacing.editoraction.contains("road_road_parkingspace"))||
		            			(town.gameGui.objPlacing.editoraction.contains("road_road") ||
		            					town.gameGui.objPlacing.iRasterValue>0
		            					)
		            	)
		            	{
		    				x = (int)(x / town.gameGui.objPlacing.iRasterValue);
		    				x = (int)(x * town.gameGui.objPlacing.iRasterValue);
		    				y = (int)(y / town.gameGui.objPlacing.iRasterValue);
		    				y = (int)(y * town.gameGui.objPlacing.iRasterValue);
		    				
		    				x+=town.gameGui.objPlacing.iRasterValue_movx;
		    				y+=town.gameGui.objPlacing.iRasterValue_movy;
		            	}
		            	else
		            	{
		    				x = (int)(x / town.gameWorld.wallSize);
		    				x = (int)(x * town.gameWorld.wallSize);
		    				y = (int)(y / town.gameWorld.wallSize);
		    				y = (int)(y * town.gameWorld.wallSize);
		            	}
		    		}			        	
		            
		        	objPlacing.pos_x=x;
		        	objPlacing.pos_y=y;
		        	town.gameWorld.markerObject.setPosition(x, y);
		        	
		        	//Sell underlying footpaths
		        	/*
		        	if(objPlacing.editoraction.contains("road_road_road"))
		        	{
						CWorldObject fp = objPlacing.isOnFootpath((int)town.getSizeValue(100), -1, -1, false); //mov 100 für road auf footpath eingestellt
						while(fp!=null)
						{
							sellObject(fp);						
							fp = objPlacing.isOnFootpath((int)town.getSizeValue(100), -1, -1, false);
						}
		        	}
		        	*/
		        	
		        	//if(town.gameWorld.markerObject!=null)
		    		//	Gdx.app.debug("placeSingleObject 3 log marker 3", "markerObject.pos_x(): " + town.gameWorld.markerObject.pos_x() + ", markerObject.pos_y(): " + town.gameWorld.markerObject.pos_y());
		        	
		        	//Gdx.app.debug("", "objPlacing.pos_x: " + objPlacing.pos_x + ", " + objPlacing.pos_y + ", gameWorld.markerObject: " + gameWorld.markerObject.pos_x() + ", " + gameWorld.markerObject.pos_y());
		        	
					if(moveOnly)
					{
						//Human: aktuelle Action abbrechen
						town.gameWorld.markerObject.cancelAction1();
						town.gameWorld.markerObject.resetPathFinding();
					}
		        }
		        else
		        {
		        
				//Rasterisierung
		        //if(objPlacing.doRasterPlacement)
				//{
	        		x=x-objPlacing.width/2; //cursor mittig von objekt
	        		y=y-objPlacing.height/2;
	                
		            //if(town.gameGui.objPlacing.doRasterPlacement)
		    		{
		            	if(town.gameGui.objPlacing.editoraction.contains("floor") ||
		            			(town.gameGui.objPlacing.isGroundObject)||
		            			(town.gameGui.objPlacing.isGroundBaseObject)||
		            			(town.gameGui.objPlacing.editoraction.contains("residential_garage"))||
		            			(town.gameGui.objPlacing.editoraction.contains("road_road_parkingspace"))||
		            			(town.gameGui.objPlacing.editoraction.contains("road_road") ||
		            			town.gameGui.objPlacing.iRasterValue>0
           					)
		            	)
		            	{
		    				x = (int)(x / town.gameGui.objPlacing.iRasterValue);
		    				x = (int)(x * town.gameGui.objPlacing.iRasterValue);
		    				y = (int)(y / town.gameGui.objPlacing.iRasterValue);
		    				y = (int)(y * town.gameGui.objPlacing.iRasterValue);
		    				
		    				x+=town.gameGui.objPlacing.iRasterValue_movx;
		    				y+=town.gameGui.objPlacing.iRasterValue_movy;
		            	}
		            	else
		            	{
		    				x = (int)(x / town.gameWorld.wallSize);
		    				x = (int)(x * town.gameWorld.wallSize);
		    				y = (int)(y / town.gameWorld.wallSize);
		    				y = (int)(y * town.gameWorld.wallSize);
		            	}
		    		}		
		    		
		    		int w=objPlacing.width;
		    		int h=objPlacing.height;
		    		
		    		if(town.gameGui.objPlacing.isGroundObject)
		    		{
			    		w=Math.round(objPlacing.width/1.3f);
			    		h=Math.round(objPlacing.height/1.3f);
		    		}
		    		
		    		if(town.gameGui.objPlacing.editoraction.contains("road_road_road") 
		    				|| town.gameGui.objPlacing.editoraction.contains("footpath")
		    				|| town.gameGui.objPlacing.isGroundBaseObject
		    				)
		    		{
			    		w=objPlacing.iRasterValue;
			    		h=objPlacing.iRasterValue;
		    		}
		    		
		        	if(Math.abs(x-lastplacement_x)>=w || Math.abs(y-lastplacement_y)>=h)
		        	{
		        		//fail mass placement
		        		bSet=true;
		        	}
		        }
		        
		        if(bSet)
		        {
					objPlacing.pos_x = (int)x;
					objPlacing.pos_y = (int)y;
					lastplacement_x=x;
					lastplacement_y=y;
					
					if(objPlacing.isRoomObject)
					{
						if(objPlacing.placingCountLevel>1)
							return;
						
						addRoomObject(new Vector2(objPlacing.pos_x, objPlacing.pos_y), new CWorldObject(objPlacing, town, true));
					}
					else
					{
						CWorldObject obj = new CWorldObject(objPlacing, town, true);
						Boolean bRet = addWorldObject(obj, true);
						
						if(!bRet)
							obj.dispose();
						else
						{
							//..
						}
					}
		        }
			}
			
			
			//Einfache Platzierung - ein klick
			if(action=="buttonDown")
			{
		        Vector3 c0 = new Vector3(x, y, 0);
		        Vector3 c1 = town.gameCam.unproject(c0);
		        x=(int) c1.x;
		        y=(int) c1.y;
		        
	        	x=x-objPlacing.width/2; //cursor mittig von objekt
	        	y=y-objPlacing.height/2;
	        	
	            if(town.gameGui.objPlacing.doRasterPlacement)
	    		{
	            	if(town.gameGui.objPlacing.editoraction.contains("floor") ||
	            			(town.gameGui.objPlacing.isGroundObject)||
	            			(town.gameGui.objPlacing.isGroundBaseObject)||
	            			(town.gameGui.objPlacing.editoraction.contains("residential_garage"))||
	            			(town.gameGui.objPlacing.editoraction.contains("road_road_parkingspace"))||
	            			(town.gameGui.objPlacing.editoraction.contains("road_road") || 
	            			town.gameGui.objPlacing.iRasterValue>0	            					
	            		)
	            	)
	            	{
	    				x = (int)(x / town.gameGui.objPlacing.iRasterValue);
	    				x = (int)(x * town.gameGui.objPlacing.iRasterValue);
	    				y = (int)(y / town.gameGui.objPlacing.iRasterValue);
	    				y = (int)(y * town.gameGui.objPlacing.iRasterValue);
	    				
	    				x+=town.gameGui.objPlacing.iRasterValue_movx;
	    				y+=town.gameGui.objPlacing.iRasterValue_movy;
	            	}
	            	else
	            	{
	    				x = (int)(x / town.gameWorld.wallSize);
	    				x = (int)(x * town.gameWorld.wallSize);
	    				y = (int)(y / town.gameWorld.wallSize);
	    				y = (int)(y * town.gameWorld.wallSize);
	            	}
	    		}				        
		        
	            
		        if(objPlacing.isRoomObject)
				{
					objPlacing.pos_x = (int)x;
					objPlacing.pos_y = (int)y;	 
					
					addRoomObject(new Vector2(objPlacing.pos_x, objPlacing.pos_y), new CWorldObject(objPlacing, town, true));
					
					if(objPlacing.placingCountLevel==2)
					{	
						CObject obj2 = objPlacing.clone();
						CObject obj3 = objPlacing.clone();
						CObject obj4 = objPlacing.clone();
						obj2.pos_x += objPlacing.width;
						obj3.pos_x += objPlacing.width;
						obj3.pos_y -= objPlacing.width;
						obj4.pos_y -= objPlacing.width;
						
						addRoomObject(new Vector2(obj2.pos_x, obj2.pos_y), new CWorldObject(obj2, town, true));
						addRoomObject(new Vector2(obj3.pos_x, obj3.pos_y), new CWorldObject(obj3, town, true));
						addRoomObject(new Vector2(obj4.pos_x, obj4.pos_y), new CWorldObject(obj4, town, true));
					}					
					
					if(objPlacing.placingCountLevel>2)
					{
		    			for(int i=0;i<objPlacing.placingCountLevel;i++)
		    			{
		        			for(int j=0;j<objPlacing.placingCountLevel;j++)
		        			{
		        				if(i==0 && j==0)
		        					continue;
		        				
		        				CObject obj = objPlacing.clone();
		        				obj.pos_x+=i*objPlacing.width;
		        				obj.pos_y-=j*objPlacing.width;
		        				
		        				addRoomObject(new Vector2(obj.pos_x, obj.pos_y), new CWorldObject(obj, town, true));
		        			}
		    			}
					}
				}	        
		        else
		        {
					objPlacing.pos_x = (int)x;
					objPlacing.pos_y = (int)y;
					lastplacement_x=x;
					lastplacement_y=y;
					
					CWorldObject newWorldObject=null;
					Boolean placingOK=false;
					
					if(objPlacing.isHuman())
					{
						Boolean checkPlacing = town.gameWorld.checkObjectPlacing(null, objPlacing); //muss hier mit defaultsize getestet werden, da sich size je je nach age ändert
						if(!checkPlacing)
							return;
						
						Optional<CObject> obj1=null;
						
						if(createResidentDlg.sGender.equals("m"))
							obj1 = town.gameResourceConfig.listObjectres.stream().filter(item->item.editoraction.equals("human_man")).findFirst();
						else
							obj1 = town.gameResourceConfig.listObjectres.stream().filter(item->item.editoraction.equals("human_woman")).findFirst();
						
						obj1.get().pos_x = (int)x;
						obj1.get().pos_y = (int)y;
						newWorldObject = new CWorldObject(obj1.get(), town, true);
						newWorldObject.thehuman = new CHuman(town, newWorldObject, createResidentDlg.sGender.charAt(0));
						newWorldObject.thehuman.setAge(createResidentDlg.iAge);
						
						if(createResidentDlg.iSkill>0)
						{
							CJobSkillClass jsc1 = new CJobSkillClass();
							jsc1.fskill=createResidentDlg.iSkill;
							if(createResidentDlg.skillObject!=null && createResidentDlg.skillObject.theobject!=null)
							{
								jsc1.theobject=createResidentDlg.skillObject.theobject;
								newWorldObject.thehuman.jobSkillLevel.put(createResidentDlg.skillObject.theobject.getSkillObjectId(), jsc1);
							}
						}
						
						String stempname=createResidentDlg.sDialogInput1;
						
						newWorldObject.thehuman.forename="";
						newWorldObject.thehuman.lastname="";
						if(stempname.contains(" "))
						{
							String arr1[] = stempname.split(" ");
							if(arr1.length>0)
								newWorldObject.thehuman.forename=arr1[0];
							if(arr1.length>1)
								newWorldObject.thehuman.lastname=arr1[1];
						}
						else
						{
							newWorldObject.thehuman.forename=createResidentDlg.sDialogInput1;
						}
						
						newWorldObject.thehuman.headTextureId=objPlacing.objectId;
						newWorldObject.thehuman.healthValueMax=createResidentDlg.iMaxhealth;
						newWorldObject.thehuman.setHealthValue(newWorldObject.thehuman.healthValueMax);
						newWorldObject.thehuman.happinessValueMax=createResidentDlg.iMaxHappyness;
						newWorldObject.thehuman.setHappynessValue(newWorldObject.thehuman.happinessValueMax);
						newWorldObject.thehuman.setIntelligenceValue(createResidentDlg.iIntelligence);
						newWorldObject.thehuman.setFitnessValue(createResidentDlg.iFitness);
						newWorldObject.thehuman.healthAttitude=createResidentDlg.fHealthAttitude;
						newWorldObject.thehuman.setEducationValue(createResidentDlg.fEducation);
						newWorldObject.thehuman.positiveAttitude=createResidentDlg.fPositiveAttitude;
						newWorldObject.initHead(createResidentDlg.sGender, newWorldObject.thehuman.headTextureId);
						newWorldObject.theobject.price=objPlacing.price;
											
						if(createResidentDlg.buttonSpaceshipTechnologist.toggleActive)
							newWorldObject.thehuman.abilitySpaceshipTechnology=1;
						//if(createResidentDlg.buttonSuperstar.toggleActive)
						//	newWorldObject.thehuman.abilitySuperstar = 1;
												
						//Placing beenden
						placingOK=true;
					}
					else
					{
						newWorldObject = new CWorldObject(objPlacing, town, true);
					}
					
					placingOK=addWorldObject(newWorldObject, true);
					
					if(placingOK!=null && newWorldObject.thehuman!=null)
					{
						if(createResidentDlg.workplace!=null)
						{
							if(newWorldObject.thehuman.getEducationValue()>=createResidentDlg.workplace.theobject.getRequiredWorkplaceEducation())
							{
								if(newWorldObject.thehuman.getAge()>=createResidentDlg.workplace.getMinAge())
									town.gameWorld.linkWorkerAndWorkplace(newWorldObject, createResidentDlg.workplace, 1);
							}
						}
					}
					
					if(objPlacing.placingCountLevel==2)
					{	
						CObject obj2 = objPlacing.clone();
						CObject obj3 = objPlacing.clone();
						CObject obj4 = objPlacing.clone();
						obj2.pos_x += objPlacing.width;
						obj3.pos_x += objPlacing.width;
						obj3.pos_y -= objPlacing.width;
						obj4.pos_y -= objPlacing.width;
						addWorldObject(new CWorldObject(obj2, town, true), true);
						addWorldObject(new CWorldObject(obj3, town, true), true);
						addWorldObject(new CWorldObject(obj4, town, true), true);
					}					
					
					if(objPlacing.placingCountLevel>2)
					{
		    			for(int i=0;i<objPlacing.placingCountLevel;i++)
		    			{
		        			for(int j=0;j<objPlacing.placingCountLevel;j++)
		        			{
		        				if(i==0 && j==0)
		        					continue;
		        				
		        				CObject obj = objPlacing.clone();
		        				obj.pos_x+=i*objPlacing.width;
		        				obj.pos_y-=j*objPlacing.width;
		        				addWorldObject(new CWorldObject(obj, town, true), true);
		        			}
		    			}						
					}
					
					if(placingOK)
					{
						//Human placing abbrechen, human nur einmal placen
						if(newWorldObject.thehuman!=null)
						{
							int ig = rand.nextInt(2);
							String sgender1="m";
							if(ig==0)
								sgender1="w";
							createResidentDlg.sGender=sgender1;
							createResidentDlg.sDialogInput1=CHuman.generateCitizenForename(sgender1) + " " + CHuman.generateCitizenLastname();
							
							List<CObject> listHeads=null;
							if(sgender1.equals("w"))
								listHeads = town.gameResourceConfig.listObjectHead_Women;
							
							if(sgender1.equals("m"))
								listHeads = town.gameResourceConfig.listObjectHead_Men;
							
							int index = rand.nextInt(listHeads.size()-1);
							objPlacing = listHeads.get(index);
							objPlacing.price = createResidentDlg.price;
						}
					}
					else
					{
						if(newWorldObject!=null)
							newWorldObject.dispose();
					}
		        }
			}
		}

        
        town.gameWorld.bRenderFrameBuffer=true;
	}
	
	public void sellObject(CWorldObject theobj)
	{
		int sellingprice=theobj.theobject.getSellingPrice();
		town.gameWorld.changeTownMoney(sellingprice);
		town.gameWorld.townStatistics.getCurrentStatistics_Finance().sellObject+=Math.abs(sellingprice);

		if(theobj.theobject.isRoomObject)
			removeRoomObject(null, theobj, false);
		else
			removeWorldObject(theobj, false);
	}
	
	public Boolean checkDeleteObject(int x, int y, int libgdxy)
	{
		if(deleteObject!=null && deleteObject.thehuman==null && !deleteObject.theobject.editoraction.contains("bird"))
		{
			int sellingprice=deleteObject.theobject.getSellingPrice();
			
			//int px=deleteObject.pos_x();
			//int py=deleteObject.pos_y();
			
			Boolean deleted=true;
			
			if(deleteObject.theobject.isRoomObject)
				deleted=removeRoomObject(null, deleteObject, false);
			else
				removeWorldObject(deleteObject, false);
			
			if(deleted)
			{
				town.gameWorld.changeTownMoney(sellingprice);
				
				town.gameWorld.townStatistics.getCurrentStatistics_Finance().sellObject+=Math.abs(sellingprice);
				
				//gameWorld.townMoney+=sellingprice;
				//int showEventX=(int)(px-50+rand.nextInt(200));
				//int showEventY=(int)(py-50+rand.nextInt(200));
				//gameWorld.animationEvents.add(new CAnimationTextEvent(town, showEventX, showEventY, sellingprice, AnimationEventType.MONEY, gameWorld.gameCamera.zoom));
			}
			
			deleteObject=null;
			
			town.gameWorld.bRenderFrameBuffer=true;
			
			return true;
		}
		
		return false;
				
		
//        Vector3 c0 = new Vector3(x,y,0); //Gameworld needs unprojected Coordinates
//        Vector3 c1 = gameWorld.gameCamera.unproject(c0);
//        x=(int)c1.x;
//        y=(int)c1.y;		
//		
//		CWorldObject delObj=null;
//		//for(CWorldObject obj : gameWorld.worldObjects)
//		for(int i=gameWorld.worldObjects.size()-1;i>=0;i--)
//		{
//			CWorldObject obj=gameWorld.worldObjects.get(i);
//			if(obj.box2dBody!=null)
//			{
//				//if(obj.box2dBody.getFixtureList().first().testPoint(new Vector2(x,y)))
//				if(obj.testpoint(x, y))
//				{
//					if(bDeletemode)
//					{
//						delObj=obj;
//						break;
//					}
//				}
//			}
//		}		
//		
//		if(delObj==null)
//		{
//			for(int i=gameWorld.worldHumans.size()-1;i>=0;i--)
//			{
//				CWorldObject obj=gameWorld.worldHumans.get(i);
//				if(obj.box2dBody!=null)
//				{
//					//if(obj.box2dBody.getFixtureList().first().testPoint(new Vector2(x,y)))
//					if(obj.testpoint(x, y))
//					{
//						if(bDeletemode)
//						{
//							delObj=obj;
//							break;
//						}
//					}
//				}
//			}				
//		}
//				
//		if(delObj!=null)
//		{
//			removeWorldObject(delObj);
//			return true;
//		}
//		
//		delObj=null;
//		//for(CWorldObject obj : gameWorld.worldRoomObjects)
//		for(int i=gameWorld.worldRoomObjects.size()-1;i>=0;i--)
//		{
//			CWorldObject obj=gameWorld.worldRoomObjects.get(i);
//			
//			if(obj.box2dBody!=null)
//			{
//				//if(obj.box2dBody.getFixtureList().first().testPoint(new Vector2(x,y)))
//				if(obj.testpoint(x, y))
//				{
//					if(bDeletemode)
//					{
//						delObj=obj;
//						break;
//					}
//				}
//			}
//		}
//		
//		if(delObj!=null)
//		{
//			removeRoomObject(null, delObj);
//			return true;
//		}		
	}	
	public void roomPlacingStart(int x, int y, int libgdxy, int button)
	{
		vectorRoomStart.x = x;
		vectorRoomStart.y = y;
		vectorRoomStart = town.gameCam.unproject(vectorRoomStart);
		
		objPlacing.width=objPlacing.height=town.gameWorld.floorSize;
		
		//Rasterisierung
		//if(objPlacing.height==16 && objPlacing.width==16)
		{
			vectorRoomStart.x = (int)(vectorRoomStart.x / objPlacing.width);
			vectorRoomStart.x = (int)(vectorRoomStart.x * objPlacing.width);
			vectorRoomStart.y = (int)(vectorRoomStart.y / objPlacing.height);
			vectorRoomStart.y = (int)(vectorRoomStart.y * objPlacing.height);
			//			vectorRoomStart.x = (int)(vectorRoomStart.x / gameWorld.nodesize);
			//			vectorRoomStart.x = (int)(vectorRoomStart.x * gameWorld.nodesize);
			//			vectorRoomStart.y = (int)(vectorRoomStart.y / gameWorld.nodesize);
			//			vectorRoomStart.y = (int)(vectorRoomStart.y * gameWorld.nodesize);
		}					
		
		//Nur im Levelbereich Räume definieren
        if(vectorRoomStart.x<0 || vectorRoomStart.x>CWorld.mapsize-objPlacing.width || vectorRoomStart.y<0 || vectorRoomStart.y>CWorld.mapsize-objPlacing.height)
        {
			bButtonDown = false;
			bRoomPlacing = false;
			objPlacing = null;
			town.gameWorld.worldTempRoomObjects.clear();
			return;
        }
		
		bButtonDown=true;
		if(button==1)
		{
			bButtonDown = false;
			bRoomPlacing = false;
			objPlacing = null;
			town.gameWorld.worldTempRoomObjects.clear();
			return;
		}
		
		objPlacing.pos_x =  (int)vectorRoomStart.x;
		objPlacing.pos_y =  (int)vectorRoomStart.y;
	}
	public void roomPlacing(int x, int y)
	{
        Vector3 c0 = new Vector3(x, y, 0);
        Vector3 c1 = town.gameCam.unproject(c0);
        x=(int) c1.x;
        y=(int) c1.y;
        
		//y=libgdxy;
		//Rasterisierung
		//if(objPlacing.height==16 && objPlacing.width==16)
        
        objPlacing.width=town.gameWorld.floorSize;
        objPlacing.height=town.gameWorld.floorSize;
        
        int wallsize1=town.gameWorld.wallSize;
        
		{
			x = (int)(x / objPlacing.width);
			x = (int)(x * objPlacing.width);
			y = (int)(y / objPlacing.height);
			y = (int)(y * objPlacing.height);
			
//			x = (int)(x / gameWorld.nodesize);
//			x = (int)(x * gameWorld.nodesize);
//			y = (int)(y / gameWorld.nodesize);
//			y = (int)(y * gameWorld.nodesize);			
		}		        


        if(x<0 || x>CWorld.mapsize-objPlacing.width || y<0 || y>CWorld.mapsize-objPlacing.height)
        	return;
		
		
		if(x!=objPlacing.pos_x || y != objPlacing.pos_y)
		{
			//Build Room
			town.gameWorld.worldTempRoomObjects.clear();
			
			//vectorRoomStart.y
			
			int roomHeight = (int) (y - vectorRoomStart.y);
			int roomWidth = (int) (x - vectorRoomStart.x);
			int countH = (int)(roomHeight / objPlacing.height);
			int countW = (int)(roomWidth / objPlacing.width);
			
			int wcountH = (int)(roomHeight / wallsize1);
			int wcountW = (int)(roomWidth / wallsize1);
			
			int minusFactorH=1;
			int minusFactorW=1;
			
			if(countH<0)
			{
				countH=countH*-1;
				minusFactorH = -1;
			}
			if(countW<0)
			{
				countW=countW*-1;
				minusFactorW=-1;
			}
			
			//Optional<CObject> cornerObj = gameResourceConfig.listObject.stream().filter(item->item.editoraction.equals("wall1")).findFirst();
			//Optional<CObject> straightObj = gameResourceConfig.listObject.stream().filter(item->item.editoraction.equals("wall2")).findFirst();

			Optional<CObject> cornerObj = town.gameResourceConfig.listObject.stream().filter(item->item.editoraction.equals("cornerwall")).findFirst();
			Optional<CObject> straightObj = town.gameResourceConfig.listObject.stream().filter(item->item.editoraction.equals("straightwall")).findFirst();
			
			CObject wall1 = cornerObj.get();
			CObject wall2 = straightObj.get();
			CObject tempwall=wall2;
			
			Optional<CObject> floorObj = town.gameResourceConfig.listObject.stream().filter(item->item.editoraction.contains("floor")).findFirst();
			
//			float x1 = vectorRoomStart.x + (1*straightObj.get().width*minusFactorW);
//			float y1 = vectorRoomStart.y;
//			float x2 = vectorRoomStart.x;
//			float y2 = vectorRoomStart.y+ (1*straightObj.get().height*minusFactorH);
//			float x3 = vectorRoomStart.x + (1*straightObj.get().width*minusFactorW);
//			float y3 = y;
//			float x4 = x;
//			float y4 = vectorRoomStart.y + (1*straightObj.get().height*minusFactorH);
			
			int rot1 = 270;
			int rot2 = 0;
			int rot3 = 180;
			int rot4 = 90;

			float sx = vectorRoomStart.x;
			float sy = vectorRoomStart.y;
			float ex = x;
			float ey = y;

			
			
			//Boden legen
			
			//links unten nach rechts oben
			for(float fx=sx;fx<=ex;fx+=floorObj.get().width)
			{
				for(float fy=sy;fy<=ey;fy+=floorObj.get().height)
				{
					floorObj.get().pos_x = (int)fx;
					floorObj.get().pos_y = (int)fy;
					town.gameWorld.worldTempRoomObjects.add(new CWorldObject(floorObj.get(), town,false));
				}
			}
			
			//rechts unten nach links oben
			for(float fx=sx;fx>=ex;fx-=floorObj.get().width)
			{
				for(float fy=sy;fy<=ey;fy+=floorObj.get().height)
				{
					floorObj.get().pos_x = (int)fx;
					floorObj.get().pos_y = (int)fy;
					town.gameWorld.worldTempRoomObjects.add(new CWorldObject(floorObj.get(),town,false));
				}
			}
			
			//rechts oben nach links unten
			for(float fx=sx;fx>=ex;fx-=floorObj.get().width)
			{
				for(float fy=sy;fy>=ey;fy-=floorObj.get().height)
				{
					floorObj.get().pos_x = (int)fx;
					floorObj.get().pos_y = (int)fy;
					town.gameWorld.worldTempRoomObjects.add(new CWorldObject(floorObj.get(),town,false));
				}
			}
			
			//links oben nach rechts unten
			for(float fx=sx;fx<=ex;fx+=floorObj.get().width)
			{
				for(float fy=sy;fy>=ey;fy-=floorObj.get().height)
				{
					floorObj.get().pos_x = (int)fx;
					floorObj.get().pos_y = (int)fy;
					town.gameWorld.worldTempRoomObjects.add(new CWorldObject(floorObj.get(),town,false));
				}
			}
			
			
			
			
			//-------
			//Mauern
			//-------
			if(wcountH<0)
			{
				wcountH=wcountH*-1;
			}
			if(wcountW<0)
			{
				wcountW=wcountW*-1;
			}

			float wsx=vectorRoomStart.x;
			float wsy=vectorRoomStart.y;			
			
			
			
			//--------------------------------
			//Berechne Rotation für Eckpfeiler
			//--------------------------------
			if(sx<ex && sy>ey) //ziehbewegung: links oben nach rechts unten
			{
				rot1 = 270; //lo
				rot2 = 0; 	//ro
				rot3 = 180; //lu
				rot4 = 90;	//ru			
			}
			
			if(sx>ex && sy>ey) //ziehbewegung: rechts oben nach links unten
			{
				rot1 = 180;rot2 = 90;rot3 = 270;rot4 = 0;				
			}
			
			if(sx<ex && sy<ey) //ziehbewegung: links unten nach rechts oben
			{
				rot1 = 0;rot2 = 270;rot3 = 90;rot4 = 180;				
			}

			if(sx>ex && sy<ey) //ziehbewegung: rechts unten nach links oben
			{
				rot1 = 90;rot2 = 180;rot3 = 0;
			}
			
					

			
			//--------------------
			//Positionen anpassen
			//--------------------
			if(sx<ex && sy>ey) //ziehbewegung: links oben nach rechts unten
			{
				wcountW+=4;
				wcountH+=3;
				wsy+=3*town.gameWorld.wallSize;
				x+=3*town.gameWorld.wallSize;
			}
			
			if(sx>ex && sy>ey) //ziehbewegung: rechts oben nach links unten
			{
				wcountW+=4;
				wcountH+=3;
				wsx+=3*town.gameWorld.wallSize;
				wsy+=3*town.gameWorld.wallSize;
			}
			
			if(sx<ex && sy<ey) //ziehbewegung: links unten nach rechts oben
			{
				wcountW+=4;
				wcountH+=3;
				x+=3*town.gameWorld.wallSize;
				y+=3*town.gameWorld.wallSize;
			}

			if(sx>ex && sy<ey) //ziehbewegung: rechts unten nach links oben
			{
				wcountW+=4;
				wsx+=3*town.gameWorld.wallSize;
				y+=3*town.gameWorld.wallSize;
				wcountH+=4;
			}
			
			
			
			
			//-------------
			//1 links oben
			//-------------
			cornerObj.get().rotation=rot1;
			cornerObj.get().pos_x =  (int)wsx;
			cornerObj.get().pos_y =  (int)wsy;
			town.gameWorld.worldTempRoomObjects.add(new CWorldObject(cornerObj.get(),town,false));
			
			int istart=1;
			
			for(int i1=istart;i1<wcountW;i1++)
			{
				//if(tempwall==wall1) tempwall=wall2;	else tempwall=wall1;
				tempwall.rotation=0;
				tempwall.pos_x =  (int)wsx + (i1*tempwall.width*minusFactorW);
				tempwall.pos_y =  (int)wsy;
				town.gameWorld.worldTempRoomObjects.add(new CWorldObject(tempwall,town,false));
			}
			
			//-------------
			//2 rechts oben
			//-------------
			cornerObj.get().rotation=rot2;
			cornerObj.get().pos_x = (int)wsx;
			cornerObj.get().pos_y = (int)y;
			town.gameWorld.worldTempRoomObjects.add(new CWorldObject(cornerObj.get(),town,false));
			
			for(int i1=istart;i1<wcountH;i1++)
			{
				//if(tempwall==wall1) tempwall=wall2;	else tempwall=wall1;
				tempwall.rotation=90;
				tempwall.pos_x = (int)wsx;
				tempwall.pos_y = (int)wsy+ (i1*tempwall.height*minusFactorH);
				town.gameWorld.worldTempRoomObjects.add(new CWorldObject(tempwall,town,false));
			}
			
			//-------------
			//3 links unten
			//-------------
			cornerObj.get().rotation=rot3;
			cornerObj.get().pos_x =  (int)x;
			cornerObj.get().pos_y =  (int)wsy;
			town.gameWorld.worldTempRoomObjects.add(new CWorldObject(cornerObj.get(),town,false));
			
			for(int i1=istart;i1<wcountW;i1++)
			{
				//if(tempwall==wall1) tempwall=wall2;	else tempwall=wall1;
				tempwall.rotation=0;
				tempwall.pos_x =  (int)wsx + (i1*tempwall.width*minusFactorW);
				tempwall.pos_y =  (int)y;
				town.gameWorld.worldTempRoomObjects.add(new CWorldObject(tempwall,town,false));
			}
			
			//--------------
			//4 rechts unten
			//--------------
			cornerObj.get().rotation=rot4;
			cornerObj.get().pos_x = (int)x;
			cornerObj.get().pos_y = (int)y;
			town.gameWorld.worldTempRoomObjects.add(new CWorldObject(cornerObj.get(),town,false));
			
			for(int i1=istart;i1<wcountH;i1++)
			{
				//if(tempwall==wall1) tempwall=wall2;	else tempwall=wall1;
				tempwall.rotation=90;
				tempwall.pos_x =  (int)x;
				tempwall.pos_y =  (int)wsy + (i1*tempwall.height*minusFactorH);
				town.gameWorld.worldTempRoomObjects.add(new CWorldObject(tempwall,town,false));
			}
			
			//----------------------------
			objPlacing.pos_x =  (int)x;
			objPlacing.pos_y =  (int)y;
		}
	}
	public void roomPlacing_alt(int x, int y)
	{
        Vector3 c0 = new Vector3(x, y, 0);
        Vector3 c1 = town.gameCam.unproject(c0);
        x=(int) c1.x;
        y=(int) c1.y;
        
		//y=libgdxy;
		//Rasterisierung
		//if(objPlacing.height==16 && objPlacing.width==16)
        
        //objPlacing.width=192;
        //objPlacing.height=192;
        
		{
			x = (int)(x / objPlacing.width);
			x = (int)(x * objPlacing.width);
			y = (int)(y / objPlacing.height);
			y = (int)(y * objPlacing.height);
		}		        
		
		if(x!=objPlacing.pos_x || y != objPlacing.pos_y)
		{
			//Build Room
			town.gameWorld.worldTempRoomObjects.clear();
			
			//vectorRoomStart.y
			
			int roomHeight = (int) (y - vectorRoomStart.y);
			int roomWidth = (int) (x - vectorRoomStart.x);
			int countH = (int)(roomHeight / objPlacing.height);
			int countW = (int)(roomWidth / objPlacing.width);

			int minusFactorH=1;
			int minusFactorW=1;
			
			if(countH<0)
			{
				countH=countH*-1;
				minusFactorH = -1;
			}
			if(countW<0)
			{
				countW=countW*-1;
				minusFactorW=-1;
			}
			
			
			Optional<CObject> cornerObj = town.gameResourceConfig.listObject.stream().filter(item->item.editoraction.equals("wall1")).findFirst();
			Optional<CObject> straightObj = town.gameResourceConfig.listObject.stream().filter(item->item.editoraction.equals("wall2")).findFirst();
						
			CObject wall1 = cornerObj.get();
			CObject wall2 = cornerObj.get();
			CObject tempwall=wall1;
			
			Optional<CObject> floorObj = town.gameResourceConfig.listObject.stream().filter(item->item.editoraction.equals("floor")).findFirst();
			
			
//			float x1 = vectorRoomStart.x + (1*straightObj.get().width*minusFactorW);
//			float y1 = vectorRoomStart.y;
//			float x2 = vectorRoomStart.x;
//			float y2 = vectorRoomStart.y+ (1*straightObj.get().height*minusFactorH);
//			float x3 = vectorRoomStart.x + (1*straightObj.get().width*minusFactorW);
//			float y3 = y;
//			float x4 = x;
//			float y4 = vectorRoomStart.y + (1*straightObj.get().height*minusFactorH);
			
			int rot1 = 270;
			int rot2 = 0;
			int rot3 = 180;
			int rot4 = 90;

			float sx = vectorRoomStart.x;
			float sy = vectorRoomStart.y;
			float ex = x;
			float ey = y;
			
			
			
			//Boden legen
			
			//links unten nach rechts oben
			for(float fx=sx;fx<=ex;fx+=floorObj.get().width)
			{
				for(float fy=sy;fy<=ey;fy+=floorObj.get().height)
				{
					floorObj.get().pos_x = (int)fx;
					floorObj.get().pos_y = (int)fy;
					town.gameWorld.worldTempRoomObjects.add(new CWorldObject(floorObj.get(), town,false));
				}
			}
			
			//rechts unten nach links oben
			for(float fx=sx;fx>=ex;fx-=floorObj.get().width)
			{
				for(float fy=sy;fy<=ey;fy+=floorObj.get().height)
				{
					floorObj.get().pos_x = (int)fx;
					floorObj.get().pos_y = (int)fy;
					town.gameWorld.worldTempRoomObjects.add(new CWorldObject(floorObj.get(),town,false));
				}
			}
			
			//rechts oben nach links unten
			for(float fx=sx;fx>=ex;fx-=floorObj.get().width)
			{
				for(float fy=sy;fy>=ey;fy-=floorObj.get().height)
				{
					floorObj.get().pos_x = (int)fx;
					floorObj.get().pos_y = (int)fy;
					town.gameWorld.worldTempRoomObjects.add(new CWorldObject(floorObj.get(),town,false));
				}
			}
			
			//links oben nach rechts unten
			for(float fx=sx;fx<=ex;fx+=floorObj.get().width)
			{
				for(float fy=sy;fy>=ey;fy-=floorObj.get().height)
				{
					floorObj.get().pos_x = (int)fx;
					floorObj.get().pos_y = (int)fy;
					town.gameWorld.worldTempRoomObjects.add(new CWorldObject(floorObj.get(),town,false));
				}
			}
			
			
			
			//Mauern
			
			//Berechne Rotation für Eckpfeiler
			if(sx<ex && sy>ey)
			{
				rot1 = 270; //lo
				rot2 = 0; 	//ro
				rot3 = 180; //lu
				rot4 = 90;	//ru			
			}
			
			if(sx>ex && sy>ey)
			{
				rot1 = 180;
				rot2 = 90;
				rot3 = 270;
				rot4 = 0;				
			}
			
			if(sx<ex && sy<ey)
			{
				rot1 = 0;
				rot2 = 270;
				rot3 = 90;
				rot4 = 180;				
			}

			if(sx>ex && sy<ey)
			{
				rot1 = 90;
				rot2 = 180;
				rot3 = 0;
				rot4 = 270;				
			}
						
			float wsx=vectorRoomStart.x;
			float wsy=vectorRoomStart.y;
					
			
			//1 links oben
			cornerObj.get().rotation=rot1;
			cornerObj.get().pos_x =  (int)wsx;
			cornerObj.get().pos_y =  (int)wsy;
			town.gameWorld.worldTempRoomObjects.add(new CWorldObject(cornerObj.get(),town,false));
			
			int istart=1;

			for(int i1=istart;i1<countW;i1++)
			{
				if(tempwall==wall1) tempwall=wall2;	else tempwall=wall1;
				
				tempwall.rotation=0;
				tempwall.pos_x =  (int)wsx + (i1*tempwall.width*minusFactorW);
				tempwall.pos_y =  (int)wsy;
				town.gameWorld.worldTempRoomObjects.add(new CWorldObject(tempwall,town,false));
			}
			
			//2 rechts oben
			cornerObj.get().rotation=rot2;
			cornerObj.get().pos_x =  (int)wsx;
			cornerObj.get().pos_y =  (int)y;
			town.gameWorld.worldTempRoomObjects.add(new CWorldObject(cornerObj.get(),town,false));
			
			for(int i1=istart;i1<countH;i1++)
			{
				if(tempwall==wall1) tempwall=wall2;	else tempwall=wall1;
				tempwall.rotation=90;
				tempwall.pos_x =  (int)wsx;
				tempwall.pos_y =  (int)wsy+ (i1*tempwall.height*minusFactorH);
				town.gameWorld.worldTempRoomObjects.add(new CWorldObject(tempwall,town,false));
			}
			
			//3 links unten
			cornerObj.get().rotation=rot3;
			cornerObj.get().pos_x =  (int)x;
			cornerObj.get().pos_y =  (int)wsy;
			town.gameWorld.worldTempRoomObjects.add(new CWorldObject(cornerObj.get(),town,false));
			
			for(int i1=istart;i1<countW;i1++)
			{
				if(tempwall==wall1) tempwall=wall2;	else tempwall=wall1;
				tempwall.rotation=0;
				tempwall.pos_x =  (int)wsx + (i1*tempwall.width*minusFactorW);
				tempwall.pos_y =  (int)y;
				town.gameWorld.worldTempRoomObjects.add(new CWorldObject(tempwall,town,false));
			}
			
			//4 rechts unten
			cornerObj.get().rotation=rot4;
			cornerObj.get().pos_x =  (int)x;
			cornerObj.get().pos_y =  (int)y;
			town.gameWorld.worldTempRoomObjects.add(new CWorldObject(cornerObj.get(),town,false));
			
			for(int i1=istart;i1<countH;i1++)
			{
				if(tempwall==wall1) tempwall=wall2;	else tempwall=wall1;
				tempwall.rotation=90;
				tempwall.pos_x =  (int)x;
				tempwall.pos_y =  (int)wsy + (i1*tempwall.height*minusFactorH);
				town.gameWorld.worldTempRoomObjects.add(new CWorldObject(tempwall,town,false));
			}

			objPlacing.pos_x =  (int)x;
			objPlacing.pos_y =  (int)y;
		}
	}
	
	public void addRoomObject(Vector2 addKey, CWorldObject wobj)
	{
		Boolean bPlacingOK=town.gameWorld.checkObjectPlacing(null, wobj.theobject);
		if(!bPlacingOK)
		{
			return;
		}
		
		//		if(wobj.isAddressObject())
		//		{
		//			//CAddress adr = gameWorld.getAddressByLocation(wobj.pos_x(), wobj.pos_y());
		//			CAddress adr = gameWorld.getAddressByPolygonInside(wobj.getBoundingPolygon());
		//			if(adr==null)
		//				return;
		//		}
		
		//Resize Room
		if((wobj.theobject.original_width!=wobj.theobject.width) || (wobj.theobject.original_height!=wobj.theobject.height))
			wobj.setDynamicSize(1);
				
		if(town.gameWorld.townMoney>=wobj.theobject.price)
		{
			town.gameWorld.worldRoomObjects.add(wobj);
			resetReachable();
			town.gameWorld.linkAddressAndWorldObject(wobj, true, false);
			town.gameWorld.addObjectToPathmap(wobj, true);
			town.gameWorld.changeTownMoney(-wobj.theobject.price);
			town.gameWorld.townStatistics.getCurrentStatistics_Finance().buyObject+=Math.abs(wobj.theobject.price);
			//gameWorld.townMoney-= wobj.theobject.price;
			//gameWorld.animationEvents.add(new CAnimationTextEvent(town, wobj.pos_x()+wobj.width/2, wobj.pos_y()+wobj.height/2, -wobj.theobject.price, AnimationEventType.MONEY, town.gameCam.zoom+5));
			
			town.gameWorld.dropObject();
			//wobj.spriteMoveEvents.add(new CSpriteMoveEvent(SpriteMoveEventType.PLACING, wobj, 0));
		}
		
		//		if(wobj.theobject.isFloorObject)
		//			gameWorld.worldRoomTilemap_floor.put(addKey, wobj);
		//		else
		//		{
		//			gameWorld.worldRoomTilemap_wall.put(addKey, wobj);
		//			gameWorld.addObjectToPathmap(wobj, true);
		//		}
		//		resetReachable();
	}
	public Boolean addWorldObject(CWorldObject tempw, Boolean bCheckPlacing)
	{
		if(tempw==null)
			return false;
		
		if(tempw.thehuman!=null)
			tempw.setPosition(tempw.pos_x()-50, tempw.pos_y()-50); //Mitte Mauszeiger platzieren
		

		Vector3 c0 = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		Vector3 c1 = town.gameCam.unproject(c0);
		
		//Gdx.app.debug("", "tempw.x: " + tempw.pos_x() + ", tempw.y: " + tempw.pos_y() + ", tempw.theobject: " + tempw.theobject.pos_x + ", " + tempw.theobject.pos_y + ", c1.x: " + c1.x + ", c1.y: " + c1.y);
		
		Boolean bPlacingOK=true;
		if(bCheckPlacing && !tempw.isHuman()) //human hier nicht nochmal prüfen, da es probleme mit unterschiedlicher größe placingobject gibt und dann manchmal nicht geplaced wird obwohl anzeige ok
			bPlacingOK=town.gameWorld.checkObjectPlacing(null, tempw.theobject);
			
		
		if(!bPlacingOK)
			return false;
		
		//Gdx.app.debug("", "tempw.theobject.original_width: " + tempw.theobject.original_width + ", tempw.theobject.width: " + tempw.theobject.width);
		
		//Resize Ground, ...
		if((tempw.theobject.original_width!=tempw.theobject.width) || (tempw.theobject.original_height!=tempw.theobject.height))
			tempw.setDynamicSize(1);
		
		if(town.gameWorld.townMoney>=tempw.theobject.price)
		{
			if(tempw.thehuman!=null)
			{
				if(tempw.thehuman.sDemandList.isEmpty())
					tempw.thehuman.initDemand();
			}
			
			if(tempw.theobject.editoraction.contains("bird"))
				town.gameWorld.worldBirds.add(tempw);
			else if(tempw.iZombie>=1)
				town.gameWorld.worldZombies.add(tempw);
			else if(tempw.theobject.editoraction.contains("zombie_entrance"))
				town.gameWorld.worldZombieEntrances.add(tempw);
			else if(tempw.thehuman!=null)
				town.gameWorld.worldHumans.add(tempw);
			else if(tempw.theobject.isGarbageContainer)
				town.gameWorld.worldGarbageContainers.add(tempw);
			else if(tempw.theobject.isGroundObject || tempw.theobject.isGroundBaseObject)
			{
				town.gameWorld.worldGroundObjects.add(tempw);
				if(tempw.theobject.isWaterObject)
					town.gameWorld.worldWaterObjects.add(tempw);
			}
			else if(tempw.theobject.editoraction.contains("road_road_footpath"))
				town.gameWorld.worldFootpath.add(tempw);
			else if(tempw.theobject.editoraction.contains("traffic_car"))
				town.gameWorld.worldCars.add(tempw);
			else if(tempw.theobject.editoraction.contains("road_road_road"))
				town.gameWorld.worldRoad.add(tempw);
			else if(tempw.theobject.editoraction.contains("outdoor_light"))
				town.gameWorld.worldOutdoorLights.add(tempw);
			else if(tempw.theobject.editoraction.contains("interior_light"))
				town.gameWorld.worldCoverlights.add(tempw);
			else if(tempw.theobject.editoraction.contains("supermarket_foodpallet"))
				town.gameWorld.worldDrawSpecial2.add(tempw);
			else if(tempw.theobject.editoraction.contains("illuminati_defensewarningsystem"))
				town.gameWorld.worldDefenseWarning.add(tempw);
			else if(tempw.theobject.editoraction.contains("company_waterworks_groundwaterextractionsystem"))
				town.gameWorld.worldWatersystems.add(tempw);
			else if(tempw.theobject.isDrawSpecial())
				town.gameWorld.worldDrawSpecial.add(tempw);
			else if(tempw.theobject.editoraction.contains("zombie_entrance"))
				town.gameWorld.worldZombieEntrances.add(tempw);
			else if(tempw.theobject.editoraction.contains("_carpet"))
				town.gameWorld.worldCarpets.add(tempw);
			else
				town.gameWorld.worldObjects.add(tempw);
			
			if(tempw.theobject.IsDrawAdditionalObject())
				town.gameWorld.tempListDrawAdditional.add(tempw);
			
			tempw.theroom = tempw.theobject.tempRoom;
			
			town.gameWorld.addObjectToPathmap(tempw, true);
			town.gameWorld.linkAddressAndWorldObject(tempw, true, false);
			resetReachable();
			town.gameWorld.linkWorldObjectAndCompany(tempw);
			
			town.gameWorld.changeTownMoney(-tempw.theobject.price);
			
			if(tempw.isHuman())
				town.gameWorld.townStatistics.getCurrentStatistics_Finance().buyResident+=Math.abs(tempw.theobject.price);
			else
				town.gameWorld.townStatistics.getCurrentStatistics_Finance().buyObject+=Math.abs(tempw.theobject.price);
			
			town.gameWorld.dropObject();
		}
		else 
			return false;
		
		return true;
	}
	public Boolean removeRoomObject(Vector2 delKey, CWorldObject delObj, Boolean sellAddressMode)
	{
		if(!sellAddressMode && town.gameWorld.isFloorUnderObject(delObj)) //floor nicht löschen wenn objekt drauf steht
			return false;
		
		if(delObj.theaddress!=null && !sellAddressMode)
			delObj.theaddress.removeWorldObject(delObj);
		
		//		if(delObj.belongsToCompany!=null)
		//		{
		//			delObj.belongsToCompany.listWorldObjects.removeIf(item->item.uniqueId==delObj.uniqueId);
		//
		//			if(delObj.belongsToCompany.listWorldObjects.size()<1)
		//				gameWorld.worldCompanyList.remove(delObj.belongsToCompany);
		//		}
		
		town.gameWorld.addObjectToPathmap(delObj, false);
		town.gameWorld.worldRoomObjects.remove(delObj);
		
		if(delObj.belongsToCompany!=null)
		{
			Optional<CWorldObject> comp1 = delObj.belongsToCompany.address_company.listWorldObjects.stream().filter(item->item.belongsToCompany!=null && item.belongsToCompany.companyId==delObj.belongsToCompany.companyId).findFirst();
			if(!comp1.isPresent())
			{
				comp1 = delObj.belongsToCompany.address_company.listWorldObjects_Floors.stream().filter(item->item.belongsToCompany!=null && item.belongsToCompany.companyId==delObj.belongsToCompany.companyId).findFirst();
			}
			
			if(!comp1.isPresent())
			{
				town.gameWorld.worldCompanyList.remove(delObj.belongsToCompany);
			}
		}
		
		delObj.dispose();
		resetReachable();		
		
		//		if(delObj.theobject.isFloorObject)
		//		{
		//			gameWorld.worldRoomTilemap_floor.remove(delKey);
		//		}
		//		else
		//		{
		//			gameWorld.addObjectToPathmap(delObj, false);
		//			gameWorld.worldRoomTilemap_wall.remove(delKey);
		//		}
		//		
		//		delObj.dispose();
		//		resetReachable();
		
		return true;
	}
	
	public void removeWorldObject(CWorldObject delObj, Boolean sellAddressMode)
	{
		if(delObj==null || delObj.theobject==null)
			return;
		
		town.gameWorld.addObjectToPathmap(delObj, false);
		
		if(delObj.theaddress!=null && !sellAddressMode)
			delObj.theaddress.removeWorldObject(delObj);
		
		if(delObj.belongsToCompany!=null)
		{
			//entferne objekt aus company object list
			Optional<CWorldObject> comp1 = delObj.belongsToCompany.address_company.listWorldObjects.stream().filter(item->item.belongsToCompany!=null && item.belongsToCompany.companyId==delObj.belongsToCompany.companyId).findFirst();
			if(!comp1.isPresent())
				comp1 = delObj.belongsToCompany.address_company.listWorldObjects_Floors.stream().filter(item->item.belongsToCompany!=null && item.belongsToCompany.companyId==delObj.belongsToCompany.companyId).findFirst();
			
			if(!comp1.isPresent())
				town.gameWorld.worldCompanyList.remove(delObj.belongsToCompany);
			
			//entferne referenzen auf workplace in human links
			if(delObj.worker!=null)
				delObj.worker.thehuman.workplaces.remove(delObj.uniqueId);
			if(delObj.worker2!=null)
				delObj.worker2.thehuman.workplaces.remove(delObj.uniqueId);
		}
			
			//Reset Verknüpfungen
			//Reset linked Objects
				if(delObj.theobject.editoraction.contains("laundrybasket") ||
						delObj.theobject.editoraction.contains("bathroom_washingmachine") ||
						delObj.theobject.editoraction.contains("laundryroom_dryer")
						)
				{
					//reset link basket/washingmachine/dryer
					if(delObj.isOccupiedBy2!=null) 
					{
						delObj.isOccupiedBy2.actionvar1=0;
						if(delObj.isOccupiedBy2.objectFillingMulti!=null) {
							delObj.isOccupiedBy2.objectFillingMulti.clear();
						}
						delObj.isOccupiedBy2.isOccupiedBy2=null;
						delObj.isOccupiedBy2=null;
					}
				}
				
			//Andere Worker/Owner Objects
			{
				//entferne referenzen auf taskobject in human links
				if(delObj.worker!=null)
				{
					if(delObj.worker.thehuman.taskobjects.containsKey(delObj.uniqueId))
						delObj.worker.thehuman.taskobjects.remove(delObj.uniqueId);
				}
				if(delObj.worker2!=null)
				{
					if(delObj.worker2.thehuman.taskobjects.containsKey(delObj.uniqueId))
						delObj.worker2.thehuman.taskobjects.remove(delObj.uniqueId);
				}				
				
				if(delObj.owner!=null && delObj.owner.thehuman!=null)
				{
					if(delObj.owner.thehuman.bed!=null && delObj.owner.thehuman.bed.uniqueId==delObj.uniqueId)
						delObj.owner.thehuman.bed=null;
					
					if(delObj.owner.thehuman.car!=null && delObj.owner.thehuman.car.uniqueId==delObj.uniqueId)
						delObj.owner.thehuman.car=null;
					
					if(delObj.owner.thehuman.wardrobe!=null && delObj.owner.thehuman.wardrobe.uniqueId==delObj.uniqueId)
						delObj.owner.thehuman.wardrobe=null;
					
					if(delObj.owner.thehuman.taskobjects.containsKey(delObj.uniqueId))
						delObj.owner.thehuman.taskobjects.remove(delObj.uniqueId);
				}
				
				if(delObj.owner2!=null && delObj.owner2.thehuman!=null)
				{
					if(delObj.owner2.thehuman.bed!=null && delObj.owner2.thehuman.bed.uniqueId==delObj.uniqueId)
						delObj.owner2.thehuman.bed=null;
					
					if(delObj.owner2.thehuman.taskobjects.containsKey(delObj.uniqueId))
						delObj.owner2.thehuman.taskobjects.remove(delObj.uniqueId);
				}
				
				if(delObj.owner3!=null && delObj.owner3.thehuman!=null)
				{
					if(delObj.owner3.thehuman.taskobjects.containsKey(delObj.uniqueId))
						delObj.owner3.thehuman.taskobjects.remove(delObj.uniqueId);
				}
				
				if(delObj.owner4!=null && delObj.owner4.thehuman!=null)
				{
					if(delObj.owner4.thehuman.taskobjects.containsKey(delObj.uniqueId))
						delObj.owner4.thehuman.taskobjects.remove(delObj.uniqueId);
				}
				
				if(delObj.owner5!=null && delObj.owner5.thehuman!=null)
				{
					if(delObj.owner5.thehuman.taskobjects.containsKey(delObj.uniqueId))
						delObj.owner5.thehuman.taskobjects.remove(delObj.uniqueId);
				}
				
				if(delObj.owner6!=null && delObj.owner6.thehuman!=null)
				{
					if(delObj.owner6.thehuman.taskobjects.containsKey(delObj.uniqueId))
						delObj.owner6.thehuman.taskobjects.remove(delObj.uniqueId);
				}
				
				if(delObj.owner7!=null && delObj.owner7.thehuman!=null)
				{
					if(delObj.owner7.thehuman.taskobjects.containsKey(delObj.uniqueId))
						delObj.owner7.thehuman.taskobjects.remove(delObj.uniqueId);
				}				
				
				if(delObj.owner8!=null && delObj.owner8.thehuman!=null)
				{
					if(delObj.owner8.thehuman.taskobjects.containsKey(delObj.uniqueId))
						delObj.owner8.thehuman.taskobjects.remove(delObj.uniqueId);
				}
			}
			
			//wenn id nicht enthalten: es wird nichts gemacht
			town.gameWorld.worldGarbageContainers.remove(delObj);
			town.gameWorld.worldGroundObjects.remove(delObj);
			town.gameWorld.worldWaterObjects.remove(delObj);
			town.gameWorld.worldFootpath.remove(delObj);
			town.gameWorld.worldOutdoorLights.remove(delObj);
			town.gameWorld.worldCoverlights.remove(delObj);
			town.gameWorld.worldRoad.remove(delObj);
			town.gameWorld.worldBirds.remove(delObj);
			town.gameWorld.worldDrawSpecial.remove(delObj);
			town.gameWorld.worldCarpets.remove(delObj);
			town.gameWorld.worldDefenseWarning.remove(delObj);
			town.gameWorld.worldWatersystems.remove(delObj);
			town.gameWorld.worldCars.remove(delObj);
			town.gameWorld.worldObjects.remove(delObj);
			town.gameWorld.worldDrawSpecial2.remove(delObj);
			town.gameWorld.tempListDrawAdditional.remove(delObj);
			town.gameWorld.worldZombieEntrances.remove(delObj);
			
			//muss das auch da raus?
			town.gameWorld.worldHumans.remove(delObj);
			town.gameWorld.worldZombies.remove(delObj);
			
			
		if(!sellAddressMode)
			town.gameWorld.linkAddressAndWorldObject(delObj, false, false);
		
		delObj.dispose();
		resetReachable();
	}
	
	public void dispose()
	{
		if(editorSpriteBatch!=null)
		{
			editorSpriteBatch.dispose();
			editorSpriteBatch=null;
		}
		if(shapeRenderer!=null)
		{
			shapeRenderer.dispose();
			shapeRenderer=null;
		}
	}
	
	public void closeMenu()
	{
		level1_objtypeid="";
		level2_objtypeid="";
	}
	
	public Boolean getMenuOpen()

	{
		if(level1_objtypeid.length()>0 || level2_objtypeid.length()>0)
			return true;
		
		return false;
	}
	//EDITOR<-------------------------------------------------------------
	
	
	//RENDER------------------------------------------------------------->
	public void renderForbidden(int x, int y)
	{
		//editorSpriteBatch.begin();
		editorSpriteBatch.draw(town.gameResourceConfig.textures.get("gui_forbidden"), x-20, y-30, 50, 50);
		//editorSpriteBatch.end();
	}
	
	public void setGUICamera()
	{
	    uiMatrix = town.gameCam.combined.cpy();
	    uiMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	    shapeRenderer.setProjectionMatrix(uiMatrix);
	    editorSpriteBatch.setProjectionMatrix(uiMatrix);
	}
	
	
	public void render() {

		int mx = Gdx.input.getX();
        int my = CHelper.screenToLibGDX(Gdx.input.getY());
        
		if(renderSecond>1)
			renderSecond=0;
		renderSecond+=Gdx.graphics.getDeltaTime();
		
		if(!bRender)
			return;
		
		calculateAddressCloningAndMovingPosition();
		calculateRoomCloningAndMovingPosition();
		
        editorObjectsShowing.clear();
        Gdx.app.setLogLevel(5);
        
        //setGUICamera();
        
        editorSpriteBatch.begin();
        
		if(town.isGameDemo)
		{
			if(town.gameWorld.worldTime.getCurrentDay()>2 && !yesnoDlg.dlgShowing())
			{
				Gdx.net.openURI("http://store.steampowered.com/app/517700/");
				Gdx.app.exit();
				return;
			}

			if(town.gameWorld.worldTime.getCurrentDay()>1 && !yesnoDlg.dlgShowing())
			{
				yesnoDlg.showDlg(true, CGuiDialog_YesNo.YesNoDlgType.DEMO);
			}
		}
		
			Boolean bDlgShow=false;
			int dlgShowingCount=0;
			for(CGuiDialog dlg : listDialogs)
			{
				if(dlg.dlgShowing())
				{
					bDlgShow=true;
					dlgShowingCount++;
				}
			}
			
			if(!bObjPlacing && !bRoomPlacing && !bDeletemode && !bAddressPlacing && !bAddressCloning && !bAddressMoving && !bAddressResizingStart && !bRoomCloning)  //damit wird komplettes menü ausgeblendet
			{
				renderTimeInformationControl(mx, my);
				
				renderFlashlightButton(mx, my);
				
				renderAddressButton(mx, my);
			}	
			
			editorSpriteBatch.end();
			
			Boolean bMouseOver=true;
			int count=listDialogs.size();
			//for(CGuiDialog dlg : listDialogs)
			for(int i=count-1;i>-1;i--)
			{
				CGuiDialog dlg = listDialogs.get(i);
				
				if(dlg.dlgShowing())
				{
					//if(dlgShowingCount>0 && (dlg.dlgName==objectInfoDlg.dlgName)) //wenn anderer dialog aktiv, dann object info nicht rendern / auch wegen mouseover buttons
					//	continue;
					dlg.bMouseOver=bMouseOver;
					bMouseOver=false;
					dlg.render(mx, my);
					break;
				}
			}				
			
			if(blueprintDlg.dlgShowing())
			{
				//if(dlgShowingCount>0 && (dlg.dlgName==objectInfoDlg.dlgName)) //wenn anderer dialog aktiv, dann object info nicht rendern / auch wegen mouseover buttons
				//	continue;
				blueprintDlg.bMouseOver=bMouseOver;
				bMouseOver=false;
				blueprintDlg.render(mx, my);
			}
			
			
			editorSpriteBatch.begin();
			
			renderTownInfo(mx,my);
			
			//if(!bObjPlacing && !bRoomPlacing && !bDeletemode && !bAddressPlacing && !bDlgShow)
			
			if(!bObjPlacing && !bRoomPlacing && !bDeletemode && !bAddressPlacing && !bAddressCloning && !bAddressMoving && !bAddressResizingStart && !bRoomCloning)
			{
				renderMenu(mx,my);		
				
				renderTowninfoTooltips(mx,my);				
				
				//renderHint(mx,my);
			}
			
			renderMouse(mx,my);
			
			renderPause();
			
		editorSpriteBatch.end();
	}
	public void renderMenu(int mx, int my)
	{
		//Gdx.app.debug("", "level2_objtypeid: "+level2_objtypeid);
		//Gdx.app.debug("", "level1_objtypeid: "+level1_objtypeid);
		//tooltip.setFontMiddle();
		//tooltip.setColor(new Color(0.9f,0.9f, 0.9f, 0.9f));
		//bObjPlacing=true;
		
		Boolean bShowMouseOver=false;
		
		if(!bObjPlacing && !bRoomPlacing && !bDeletemode && !bAddressPlacing && !bAddressCloning && !dlgShowing() && !bAddressMoving && !bAddressResizingStart)
		{
			bShowMouseOver=true;
		}
		else if(!bObjPlacing && !bAddressPlacing) //Menü nicht schließen, damit nicht immer neu navigieren muss
			closeMenu();
		
		//if(gameWorld.worldPause)
		//	bShowMouseOver=false;
		
		int w = Gdx.graphics.getWidth();
		int h = Gdx.graphics.getHeight();
		int iEditorIconSizeW = (int)(60/1.7f);
		int iEditorIconSizeH = (int)(55/1.7f);
		
		iEditorIconSizeW/=1.1f;
		iEditorIconSizeH/=1.1f;
		
		int ilevel1=0;
		int ilevel2=0;
		int ilevel3=0;
		
		int iy=0;
		int ix=0;
		
		//Menü-Position
		int x = Math.round(Gdx.graphics.getWidth()/2-(iEditorIconSizeW*9.9f/2f)); //(int) (CHelper.getScreenValueX(700-7)+ilevel1_abstand_x+ilevel1*((int)(iEditorIconSizeW*1.4)));
		int y = Math.round(Gdx.graphics.getHeight()-40f);
		y+=1;
		
		if(Gdx.graphics.getWidth()<920)
		{
			y-=38;
			x=5;
		}
		
		if(Gdx.graphics.getWidth()>1500)
			y-=1;
		else if(Gdx.graphics.getWidth()>1400)
			y-=0.5f;
		
		
		//Object-Types Background
		int y_back=y-5;
		int x_back=x-10;
		int h_back=iEditorIconSizeH+5;
		int w_back=(int) Math.round(iEditorIconSizeW*9.9);
		
		if(!level1_objtypeid.isEmpty())
			y_back-=iEditorIconSizeH;
		
		//For Object-Showing
		editorObjectsShowing = town.gameResourceConfig.listObject.stream().filter(p->p.objectTypeId.equals(level2_objtypeid)).collect(Collectors.toList());
		
		if(!level2_objtypeid.isEmpty())
		{
			float colMult = 1;
			int ilevel=0;
			for(CObject co : editorObjectsShowing)
			{
				if(co.iResearchTargetWorkoutput>0 && co.iResearchCurrentWorkoutput<co.iResearchTargetWorkoutput)
					continue;
				
				if(ilevel>7)
				{
					ilevel=0;
					colMult+=0.5f;
				}
				ilevel++;
			}
			
			y_back=y-7-Math.round(iEditorIconSizeH*2*colMult*1.2f)+10;
			h_back=Math.round(iEditorIconSizeH*2*colMult*1.2f)-iEditorIconSizeH-3;
		}
		
		int objytr=30;
		
		int cy=y_back;
		if(y_back<y-7-Math.round(iEditorIconSizeH*1*1*1.2f))
			cy=y-7-Math.round(iEditorIconSizeH*1*1*1.2f);
		
		float spY=Gdx.graphics.getHeight()-82+6+10;
		float spH=30;
		
		int sc=13;
		
		//		if(Gdx.graphics.getWidth()<1050)
		//		{
		//			if(!level1_objtypeid.isEmpty())
		//			{
		//				spY-=37-6;
		//				spH+=34-6;
		//			}			
		//			
		//			x_back/=1.2;
		//			spH+=sc;
		//			spY-=sc;
		//		}
		//		else
		{
			if(!level1_objtypeid.isEmpty())
			{
				spY-=45-8;
				spH+=40;
			}			
			
			spH+=sc;
			spY-=sc;
		}
		
		x_back+=1.3f+6;
		spH-=sc;
		spY+=sc;
		y_back+=7;
		
		
		//Adjust Backgroundposition, when more than one objecttype row
		long isize = town.gameResourceConfig.listObjecttype.stream().filter(item->item.objectTypeId.length()==6 && item.objectTypeId.substring(0, 3).equals(level1_objtypeid)).count();
		if(isize>8)
		{
			y_back-=iEditorIconSizeH;
			y_back-=6;
		}
		if(isize>16)
		{
			y_back-=iEditorIconSizeH;
			y_back-=6;
		}
		
		
		//Draw Object Background
		editorSpriteBatch.end();
		
		//if(!Gdx.gl.glIsEnabled(GL30.GL_BLEND))
		{
			Gdx.gl.glEnable(GL30.GL_BLEND);
			Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
		}
		Gdx.gl.glEnable(GL20.GL_LINEAR_MIPMAP_LINEAR);
	    
		
		
	    shapeRenderer.setAutoShapeType(true);
		shapeRenderer.begin();
			
			shapeRenderer.set(ShapeType.Filled);
			
			//Object Background
			if(!level2_objtypeid.isEmpty())
			{
				shapeRenderer.set(ShapeType.Filled);
				shapeRenderer.setColor(0.02f, 0.02f, 0.02f, 0.4f);
				shapeRenderer.rect(x_back, y_back-objytr-3, w_back, h_back+6);
				shapeRenderer.setColor(0.02f, 0.02f, 0.02f, 0.4f);
				shapeRenderer.rect(x_back-6, y_back-9-objytr, w_back+12, h_back+18);
			}
			
			//Categories Background
			int icount1=0;
			int rowcount=0;
			
			for(CObjecttype objtype : town.gameResourceConfig.listObjecttype)
			{
				if(objtype.objectTypeId.length()==6 && (objtype.objectTypeId.substring(0, 3).equals(level1_objtypeid)))
				{
					//Firma nicht anzeigen
					if(town.gameMode.toLowerCase().contains("design") && objtype.iconFileName.toLowerCase().contains("illuminati"))
						continue;
					if(!town.bZombieApocalypse && objtype.iconFileName.toLowerCase().contains("illuminati"))
						continue;

					if(town.bNoRealEstate && (objtype.iconFileName.toLowerCase().contains("address")))
						continue;
										
					if(icount1>7)
					{
						ilevel2=0;
						rowcount++;
					}
					
					icount1++;
				}		
			}
			
			if(!level1_objtypeid.isEmpty())
			{
				int ih=iEditorIconSizeH+5;
				
				if(rowcount>1)
				{
					ih=iEditorIconSizeH*2+10;
				}
				if(rowcount>2)
				{
					ih=iEditorIconSizeH*3+18;
				}
				
				shapeRenderer.set(ShapeType.Filled);
				shapeRenderer.setColor(town.dialogColor);
			}
			
		shapeRenderer.end();
		//Gdx.gl.glDisable(GL30.GL_BLEND);
		editorSpriteBatch.begin();		
		
		//Show Object Categories
		CObjecttype coltype=null;
		int icolor=0;
		ilevel1=0;
		int ilevel2Row=0;
		
		int icount=0;
		for(CObjecttype objtype : town.gameResourceConfig.listObjecttype)
		{
			//Firma nicht anzeigen
			//if(town.gameMode.toLowerCase().contains("design") && objtype.iconFileName.toLowerCase().contains("illuminati"))
			//	continue;
			if(!town.bZombieApocalypse && objtype.iconFileName.toLowerCase().contains("illuminati"))
				continue;

			if(town.bNoRealEstate && (objtype.iconFileName.toLowerCase().contains("address")))
				continue;
			
			if(!town.bConstructionMode && objtype.objectTypeId.equals("009022"))
				continue;
			
			icount++;
			Boolean bDraw=false;
			if(objtype.objectTypeId.length()==3)
			{
				bDraw=true;
				
				ix = (int) x+ilevel1*((int)(iEditorIconSizeW*1.23));
				iy = y-3;
				iy+=2f;
				
				ilevel1++;
			}
			
			if(objtype.objectTypeId.length()==6 && (objtype.objectTypeId.substring(0, 3).equals(level1_objtypeid)))
			{
				bDraw=true;

				if(ilevel2>7)
				{
					ilevel2=0;
					ilevel2Row++;
				}
				
				ix = (int) x+ilevel2*((int)(iEditorIconSizeW*1.23)); 
				iy = Math.round(y-3-iEditorIconSizeH-4.7f);
				iy -= iEditorIconSizeH*ilevel2Row;
				iy -= 7*ilevel2Row;
								
				ilevel2++;
			}
			
			if(bDraw)
			{
				objtype.pos_x = ix;
				objtype.pos_y = iy;
				objtype.width = iEditorIconSizeW;
				objtype.height = iEditorIconSizeH;
				
		        Gdx.gl.glEnable(GL20.GL_LINEAR_MIPMAP_LINEAR);
				editorSpriteBatch.end();
				
				//if(!Gdx.gl.glIsEnabled(GL30.GL_BLEND))
				{
					Gdx.gl.glEnable(GL30.GL_BLEND);
					Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
				}
				
			    shapeRenderer.setAutoShapeType(true);
				shapeRenderer.begin();
				
				float fz=1f;
				int delta=10;
				
				//Button Background
				shapeRenderer.set(ShapeType.Filled);
				shapeRenderer.setColor(town.dialogColor);
				
				if(objtype.objectTypeId.length()==6 && objtype.objectTypeId.substring(0, 3).equals("009"))
				{
					if(town.isGameDemo && (Integer.parseInt(objtype.objectTypeId)>9004 && Integer.parseInt(objtype.objectTypeId)!=9014 && Integer.parseInt(objtype.objectTypeId)!=9022))
						shapeRenderer.setColor(Color.GRAY);
					if(!town.gameGui.unlockedCompanyTypes.contains(objtype.objectTypeId))
						shapeRenderer.setColor(Color.GRAY);
				}
				shapeRenderer.rect(ix-4+1, iy-3, iEditorIconSizeW*fz+delta-2, iEditorIconSizeH*fz+delta-2);
								
				shapeRenderer.set(ShapeType.Line);
				shapeRenderer.setColor(0.9f,0.9f,0.9f,0.15f);
				shapeRenderer.rect(ix-4+1, iy-3, iEditorIconSizeW*fz+delta-2, iEditorIconSizeH*fz+delta-2);
				
				shapeRenderer.set(ShapeType.Filled);
				shapeRenderer.setColor(0.9f,0.9f,0.9f,0.07f);
				shapeRenderer.rect(ix-4+1, iy-3, iEditorIconSizeW*fz+delta-2, iEditorIconSizeH*fz+delta-2);
				
				shapeRenderer.end();
				editorSpriteBatch.begin();
				//Gdx.gl.glDisable(GL30.GL_BLEND);
				
		        editorSpriteBatch.setColor(1,1,1,0.85f);
		        editorSpriteBatch.setShader(null);
		        
		        if(bShowMouseOver && objtype.collidesIcon(mx, my))
		        {
		        	coltype=objtype;
		        	
		       		float fv=0.96f;
		        	
		        	int mp=2;
		        	
		        	editorSpriteBatch.setColor(fv,fv,fv,0.3f);
		        	editorSpriteBatch.draw(objtype.textureIcon, ix-3-mp, iy-3-mp, iEditorIconSizeW+mp*2+6, iEditorIconSizeH+mp*2+6);
		        	
		        	editorSpriteBatch.setColor(fv,fv,fv,0.3f);
		        	editorSpriteBatch.draw(objtype.textureIcon, ix-mp, iy-mp, iEditorIconSizeW+mp*2, iEditorIconSizeH+mp*2);
		        	
		        	editorSpriteBatch.setColor(fv,fv,fv,0.6f);
		        	editorSpriteBatch.draw(objtype.textureIcon, ix-mp, iy-mp, iEditorIconSizeW+mp*2, iEditorIconSizeH+mp*2);	
		        }
		        else
		        {
		        	float fv=0.96f;
		        	editorSpriteBatch.setColor(fv,fv,fv,0.3f);
		        	editorSpriteBatch.draw(objtype.textureIcon, ix-3, iy-3, iEditorIconSizeW+6, iEditorIconSizeH+6);
		        	
		        	editorSpriteBatch.setColor(fv,fv,fv,0.3f);
		        	editorSpriteBatch.draw(objtype.textureIcon, ix, iy, iEditorIconSizeW, iEditorIconSizeH);
		        	
		        	editorSpriteBatch.setColor(fv,fv,fv,0.6f);
		        	editorSpriteBatch.draw(objtype.textureIcon, ix, iy, iEditorIconSizeW, iEditorIconSizeH);
		        }
			}
		}
		
		
		//Show Objects
		CObject colObject=null;
		if(!level2_objtypeid.isEmpty())
		{
			float colMult=1;
			int colx=0;
			int coly=0;
			
			for(CObject obj : editorObjectsShowing)
			{
				if(obj.iResearchCurrentWorkoutput < obj.iResearchTargetWorkoutput) //nur erforschte objekte im menü anzeigen
					continue;
				
				if(ilevel3>7)
				{
					ilevel3=0;
					colMult+=0.5f;
				}					
				
				ix = (int) x+ilevel3*((int)(iEditorIconSizeW*1.2));
				ix+=6;
				iy = y-4-Math.round(iEditorIconSizeH*2*colMult*1.2f)-objytr+12;
				iy+=7;
				
				iy-=ilevel2Row*iEditorIconSizeH;
				
				if(isize>8)
				{
					iy-=6;
				}
				if(isize>16)
				{
					iy-=6;
				}
				obj.icon_pos_x = ix;
				obj.icon_pos_y = iy;
				obj.icon_width = iEditorIconSizeW;
				obj.icon_height = iEditorIconSizeH;
				
				Boolean bCollide=false;
		    	
		    	float h1=1f;
		    	editorSpriteBatch.setColor(h1, h1, h1, 0.8f);
		    	
		    	//Icongröße an Objekt-Größenverhältnis anpassen
		    	float dim[] = obj.getDimensionsByBase(ix, iy, iEditorIconSizeW, iEditorIconSizeH);
	    		float newiconx = dim[0];
	    		float newicony = dim[1];	
	    		float newiconw = dim[2];
	    		float newiconh = dim[3];
	    		
		    	if(bShowMouseOver && obj.collidesIcon(mx, my))
		    	{
		    		obj.tempprice=obj.original_price;
		    		setMultiPlacingPrice(obj);
		        	colObject=obj;
		        	bCollide=true;
		        	colx=ix;
		        	coly=iy;
		    	}
		    	else
		    	{
		    		obj.drawShadows(editorSpriteBatch, 1, (int)newiconx, (int)newicony, (int)newiconw, (int)newiconh, 1);
		    		editorSpriteBatch.setColor(h1, h1, h1, 0.8f);
		    		
		    		TextureRegion texture1 = obj.getBaseTextureRegion();
		    		
		    		if(obj.textureIcon!=null)
		    			editorSpriteBatch.draw(obj.textureIcon, newiconx, newicony, newiconw, newiconh);
		    		else if(texture1!=null)
		    			editorSpriteBatch.draw(texture1, newiconx, newicony, newiconw, newiconh);
		    		else
		    			editorSpriteBatch.draw(obj.textureImage, newiconx, newicony, newiconw, newiconh);
		    	}
		    	
				ilevel3++;
			}
			
			town.drawGuiTexture1=null;
			if(colObject!=null)
			{
	        	editorSpriteBatch.setColor(1, 1, 1, 1f);
	        	
		    	//Icongröße an Objekt-Größenverhältnis anpassen
		    	float dim[] = colObject.getDimensionsByBase(colx, coly, iEditorIconSizeW, iEditorIconSizeH);
		    	colx = (int) dim[0];
		    	coly = (int) dim[1];	
	    		float newiconw = dim[2];
	    		float newiconh = dim[3];
	        	
        		colObject.drawShadows(editorSpriteBatch, 1, Math.round(colx-3), Math.round(coly-3), Math.round(newiconw*1.2f), Math.round(newiconh*1.2f), 1);
        		
        		TextureRegion texture1 = colObject.getBaseTextureRegion();
        		
          		//Small Object Texture
	        	if(colObject.isRoomObject)
	        	{
		        	editorSpriteBatch.setColor(1, 1, 1, 0.8f);
		    		if(texture1!=null)
		        		editorSpriteBatch.draw(texture1, colx-3, coly-3, newiconw*1.2f, newiconh*1.2f);
		        	else
		        		editorSpriteBatch.draw(colObject.textureImage, colx-3, coly-3, newiconw*1.2f, newiconh*1.2f);
	        	}
	        	else
	        	{
		        	editorSpriteBatch.setColor(1, 1, 1, 0.8f);
		        	if(colObject.textureIcon!=null)
		        		editorSpriteBatch.draw(colObject.textureIcon, colx-3, coly-3, newiconw*1.2f, newiconh*1.2f);
		        	else if(texture1!=null)
			        	editorSpriteBatch.draw(texture1, colx-3, coly-3, newiconw*1.2f, newiconh*1.2f);
		        	else
		        		editorSpriteBatch.draw(colObject.textureImage, colx-3, coly-3, newiconw*1.2f, newiconh*1.2f);
	        	}
	        	
	        	//Big Object Texture
	        	dim = colObject.getDimensionsForBuyMenu();
	        	float imagew = dim[0];
	        	float imageh = dim[1];
	        	int tooltipy = colObject.getInfoTextBox().size()*32+50;
				colObject.drawShadows(editorSpriteBatch, 1, Math.round(mx-3), Math.round(my-imageh-50)-tooltipy, Math.round(imagew), Math.round(imageh),1);
	        	editorSpriteBatch.setColor(1, 1, 1, 1f);
	        	
				//	        	town.drawGuiObject=colObject;
				//	        	town.drawGuiTexture1_x=mx-3;
				//	        	town.drawGuiTexture1_y=(int) (my-imageh-50-tooltipy);
				//	        	town.drawGuiTexture1_w=(int) imagew;
				//	        	town.drawGuiTexture1_h=(int) imageh;
	        	
	        	colObject.setMipMapping();
	        	
	        	if(texture1!=null)
	        	{
	        		editorSpriteBatch.draw(texture1, mx-3, my-imageh-50-tooltipy, imagew, imageh);
	        	}
	        	else
	        	{
	        		//colObject.textureImage.
	        		editorSpriteBatch.draw(colObject.textureImage, mx-3, my-imageh-50-tooltipy, imagew, imageh);
	        	}
			}
		}
		
		//Show Collision Info, tooltips
		if(coltype!=null)
		{
			//			town.gameFont.layout.setText(font, coltype.objectTypeName);
			//			editorSpriteBatch.end();
			//			Gdx.gl.glEnable(GL30.GL_BLEND);
			//		    Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
			//		    shapeRenderer.setAutoShapeType(true);
			//			shapeRenderer.begin();
			//				shapeRenderer.setColor(0.1f, 0.1f, 0.1f, 0.8f);
			//				shapeRenderer.set(ShapeType.Filled);
			//				shapeRenderer.rect(mx+2, my-18-town.gameFont.layout.height, town.gameFont.layout.width+4, town.gameFont.layout.height+5);
			//				shapeRenderer.set(ShapeType.Line);
			//				shapeRenderer.setColor(0.7f, 0.7f, 0.7f, 0.7f);
			//				shapeRenderer.rect(mx+1, my-17-town.gameFont.layout.height, town.gameFont.layout.width+6, town.gameFont.layout.height+5);
			//			shapeRenderer.end();
			//			Gdx.gl.glDisable(GL30.GL_BLEND);
			//			editorSpriteBatch.begin();
			//			
			//			font.setColor(1f,1f,1f,1f);
			//			font.draw(editorSpriteBatch, coltype.objectTypeName, mx+5, my-15);
			
			
        	tooltip.textLines.clear();
			tooltip.textLines.add(coltype.objectTypeName);
			
			if(coltype.objectTypeId.substring(0,3).equals("009") && !unlockedCompanyTypes.contains(coltype.objectTypeId))
			{
				tooltip.textLines.add("Click to unlock");
				
				if(town.gameWorld.townMoney<town.unlockCompanyPrice)
				{
					tooltip.textLines.clear();
					tooltip.textLines.add(coltype.objectTypeName);
					tooltip.textLines.add("Not enough money to unlock ($"+town.unlockCompanyPrice+")");
				}
			}
			
			if(coltype.actionCode.toLowerCase().contains("info"))
			{
				tooltip.textLines.clear();
				tooltip.textLines.add("Happy Town People");
				//tooltip.textLines.add("");
			}
			editorSpriteBatch.end();
			tooltip.setColor(Color.WHITE);
			tooltip.drawFormat(mx, my-15, 1);
			
			editorSpriteBatch.begin();
		}
		
		if(colObject!=null)
		{
        	String st="";
        	Boolean bRed=false;
        	
        	if(town.gameWorld.townMoney < colObject.price)
        	{
        		tooltip.setColor(Color.RED);
        		st=" - NOT ENOUGH MONEY";
        	}
        	
        	tooltip.textLines.clear();
			tooltip.textLines.add(colObject.objectName);
			tooltip.textLines.addAll(colObject.getInfoTextBox());
			if(colObject.price>0)
				tooltip.textLines.add("$"+colObject.price + st);
			
			editorSpriteBatch.end();
			tooltip.draw_BuyMenu(mx+30, my+10, colObject.price);
			tooltip.setColor(Color.WHITE);
			editorSpriteBatch.begin();
		}		
	}
	public void renderMouse(int mx, int my)
	{
		if(dlgShowing())
			return;
		
		float fsize=1.5f;
		
		if((bAddressResizing || bAddressResizingOver || bAddressResizingStart) && (!bObjMovement && !bObjPlacing && !bDeletemode && !bPaintObject))
		{
			if(bAddressResizing)
				editorSpriteBatch.setColor(1,1,1,1f);
			else
				editorSpriteBatch.setColor(1,1,1,0.6f);
			
			editorSpriteBatch.draw(town.gameResourceConfig.textures.get("cursor_resize"), mx, my-40*fsize, 20*fsize, 20*fsize);			
		}
		
		if(bDeletemode)
		{
			editorSpriteBatch.setColor(1,1,1,0.8f);
			editorSpriteBatch.draw(deleteIcon.textureImage, mx, my-40*fsize, 16*fsize, 20*fsize);
		}
		
		if(bAddressPlacing)
		{
			editorSpriteBatch.setColor(1,1,1,0.8f);
			editorSpriteBatch.draw(addressIcon.textureImage, mx, my-40*fsize, 15*fsize, 22*fsize);
		}
				
		if(bAddressCloning)
		{
			editorSpriteBatch.setColor(1,1,1,0.8f);
			editorSpriteBatch.draw(town.gameResourceConfig.textures.get("address_cloning_icon"), mx, my-40*fsize, 25*fsize, 25*fsize);
		}
		
		if(bPaintObject)
		{
			editorSpriteBatch.setColor(1,1,1,0.8f);
			editorSpriteBatch.draw(town.gameResourceConfig.textures.get("guiinfo_paint"), mx, my-40*fsize, 25*fsize, 25*fsize);
		}
		
		if(bAddressMoving)
		{
			editorSpriteBatch.setColor(1,1,1,0.8f);
			editorSpriteBatch.draw(town.gameResourceConfig.textures.get("address_moving_icon"), mx, my-40*fsize, 25*fsize, 25*fsize);
		}
				
		if(bRenderForbidden && bObjMovement)
		{
			renderForbidden(mx, my);
		}
				
		bMouseActionMode=false;
		if(bDeletemode || bAddressPlacing || bAddressResizingOver || bAddressResizing || bAddressCloning  || bAddressMoving || bAddressResizingStart || bPaintObject)
			bMouseActionMode=true;
	}
	
	public void renderPlacing_notused(int mx, int my)
	{
		
		//Funktion verschoben nach World, damit zorder berücksichtigt wird
		
			float px = Gdx.input.getX();
			float py = Gdx.input.getY();
	        Vector3 c0 = new Vector3(px,py,0);
	        Vector3 c1 = town.gameCam.unproject(c0);
	        px = c1.x;
	        py = c1.y;
	        
			//Rasterisierung
			//if(objPlacing.height==16 && objPlacing.width==16)
	        if(objPlacing.doRasterPlacement)
			{
//				px = (int)(px / objPlacing.width);
//				px = (int)(px * objPlacing.width);
//				py = (int)(py / objPlacing.height);
//				py = (int)(py * objPlacing.height);
	        	
				px = (int)(px / town.gameWorld.wallSize);
				px = (int)(px * town.gameWorld.wallSize);
				py = (int)(py / town.gameWorld.wallSize);
				py = (int)(py * town.gameWorld.wallSize);	        	
			}		        
	        
			float rotation = objPlacing.rotation;
        	TextureRegion rgn1 = objPlacing.textureRegion[0];
//        	if(objPlacing.editoraction.contains("door") || objPlacing.editoraction.contains("window"))
//        	{
//        		if(rotation==90 || rotation==270)
//        			editorSpriteBatch.draw(rgn1, px+objPlacing.width/2, py+objPlacing.width/2, objPlacing.width/2, objPlacing.height/2, objPlacing.width, objPlacing.height, 1, 1, rotation);
//        		else
//        			editorSpriteBatch.draw(rgn1, px, py, objPlacing.width/2, objPlacing.height/2, objPlacing.width, objPlacing.height, 1, 1, rotation);
//        	}
//        	else
        		editorSpriteBatch.draw(rgn1, px, py, objPlacing.width/2, objPlacing.height/2, objPlacing.width, objPlacing.height, 1, 1, rotation);
        	
        	editorSpriteBatch.end();

	}
	
	public void renderBalken100(int x, int y, int value, ShapeRenderer srender, float alphavalue)
	{
		srender.set(ShapeType.Line);
		srender.setColor(0.8f, 0.8f, 0.8f, 0.7f);
		srender.rect(x+30, y, CHelper.getScreenValueX(101), (int)(11));
		
		srender.set(ShapeType.Filled);
		srender.setColor(0f, 0.8f, 0f, alphavalue-0.1f);
		if(value<70)
			srender.setColor(0.8f,0.8f,0, alphavalue);
		if(value<30)
			srender.setColor(0.6f, 0, 0, alphavalue-0.2f);
		srender.rect(x+30, y, CHelper.getScreenValueX(value), (int)(10));					
	}
	
	public void renderTownInfo(int mx, int libgdxy)
	{
		int yhoch=1;
		int resy=0; 
		
		//if(Gdx.graphics.getWidth()<920)
		//{
			//resy=-Gdx.graphics.getHeight()+50;
		//}
				
		//Populationinfo
		int height2=35;
		int width2=480;
		int x2 = Gdx.graphics.getWidth()-width2;
		int y2 = Gdx.graphics.getHeight()-height2+yhoch+resy;
		int w2 = width2;
		int h2 = height2+5;
		
		//int avghealth = (int) gameWorld.worldHumans.stream().filter(item->item.bIsDead==false).mapToInt(i -> (int)i.thehuman.getHealthValue()).average().orElse(0);
		//int avghappyness = (int) gameWorld.worldHumans.stream().filter(item->item.bIsDead==false).mapToInt(i -> (int)i.thehuman.getHappynessValue()).average().orElse(0);
		
		int avghealth = town.gameWorld.townStatistics.getCurrentStatistics_Population().healthAVG; 
		int avghappyness = town.gameWorld.townStatistics.getCurrentStatistics_Population().happinessAVG; 
		int avgclean = town.gameWorld.townStatistics.getCurrentStatistics_Population().cleanAVG;
		int avgsleep = town.gameWorld.townStatistics.getCurrentStatistics_Population().sleepAVG;
		int avgeat = town.gameWorld.townStatistics.getCurrentStatistics_Population().eatAVG;
		int avgtoilet = town.gameWorld.townStatistics.getCurrentStatistics_Population().toiletAVG;
		
		if(avghealth>100)
			avghealth=100;
		if(avghappyness>100)
			avghappyness=100;
		
		int iage_x=x2+80;
		int iage_y=y2+h2-30;
		
		int ihealth_x=x2+155;
		int ihealth_y=y2+h2-30;
		int ihappyness_x=x2+320;
		int ihappyness_y=y2+h2-30;
		
		if(Gdx.graphics.getWidth()<1200)
		{
			ihealth_x=x2+155+30;
			ihealth_y=y2+h2-30;
			ihappyness_x=x2+320+20;
			ihappyness_y=y2+h2-30;
		}
				
		//Towninfo
		int height=35;
		int width=380;
		int x = 0;
		int y = Gdx.graphics.getHeight()-height+yhoch+resy;
		int w = width;
		int h = height+5;
		
		//sleep,eat,clean,toilet
		//int attrx=Gdx.graphics.getWidth()-150;
		//int attry=Gdx.graphics.getHeight()-300;
		int attrw=170;
		int attrx=Gdx.graphics.getWidth()/2-attrw*2;
		int attry=17;
		
		int energyconsumption = town.gameWorld.getEnergyConsumption();
		int energyoutput = town.gameWorld.getEnergyOutput();
		//int waterconsumption = town.gameWorld.getWaterConsumption();
		//int wateroutput = town.gameWorld.getWaterOutput();
		int population = town.gameWorld.worldHumans.size();
		
		BitmapFont tiFont=font;
		//tiFont.getData().setScale(0.482f);
		tiFont.getData().setScale(1f);
		town.gameFont.layout.setText(tiFont, String.valueOf(population));
		int populationwidth = (int)town.gameFont.layout.width;
		populationwidth=8;
		
		//>1200
		int icsize=18;
		int wcol=-2; //Abstände zwischen einzelnen Werten
		int po=0; //Population
		
		//<1200
		int py=9+1;
		int textleft=2;
		int posy=-5-1;
		float fup=1.7f;
		float texttop=0.1f;
		float fsizeleft=0;
		
		if(Gdx.graphics.getWidth()>1200)
		{
			tiFont=town.gameFont.bfArial;
			tiFont.getData().setScale(0.59f);
			town.gameFont.layout.setText(tiFont, String.valueOf(population));
			populationwidth = (int)town.gameFont.layout.width;
			
			town.gameFont.layout.setText(tiFont, String.valueOf(town.gameWorld.townMoney));
			float tm = (int)town.gameFont.layout.width;
			
			icsize=18;
			wcol=-2; //Abstände zwischen einzelnen Werten
			po=0; //Population
			py=10;
			
			fsizeleft = x+190+populationwidth+wcol+80+po+icsize+tm+18;
			if(Gdx.graphics.getWidth()<1800)
			{
				wcol=-5;
				po=-6;
				fsizeleft = x+190+populationwidth+wcol+80+po+icsize+tm+20;
			}
			
			if(Gdx.graphics.getWidth()<1600)
			{
				wcol=-9;
				po=-8;
				fsizeleft = x+190+populationwidth+wcol+80+po+icsize+tm+20;
			}
		}
		else
		{
			tiFont=font;
			tiFont=town.gameFont.bfArial2;
			tiFont.getData().setScale(0.53f);
			town.gameFont.layout.setText(tiFont, String.valueOf(population));
			populationwidth = (int)town.gameFont.layout.width;
			
			town.gameFont.layout.setText(tiFont, String.valueOf(town.gameWorld.townMoney));
			float tm = (int)town.gameFont.layout.width;
			
			icsize=18;
			
			py=9+1;
			textleft=2;
			posy=-5-1;
			fup=1.7f;
			
			fsizeleft = x+190+populationwidth+15+23-textleft+5+icsize+tm-4;
		}
				
		editorSpriteBatch.end();
	    shapeRenderer.setAutoShapeType(true);
	    
		//if(!Gdx.gl.glIsEnabled(GL30.GL_BLEND))
		{
			Gdx.gl.glEnable(GL30.GL_BLEND);
			Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
		}
		Gdx.gl.glEnable(GL20.GL_LINEAR_MIPMAP_LINEAR);
		shapeRenderer.begin();
			
			//Towninfo background
			shapeRenderer.set(ShapeType.Filled);
			shapeRenderer.setColor(0,0,0,0.11f);
			shapeRenderer.rect(-5, Gdx.graphics.getHeight()-42.8f+2+resy, fsizeleft+5, 35);
			shapeRenderer.setColor(0,0,0,0.14f);
			shapeRenderer.rect(-5, Gdx.graphics.getHeight()-38.8f+2+resy, fsizeleft, 27);
			
			int balkeny=7+2-yhoch;
			
			//Health
			shapeRenderer.set(ShapeType.Line);
			shapeRenderer.setColor(0.8f, 0.8f, 0.8f, 0.7f);
			shapeRenderer.rect(ihealth_x+30, ihealth_y-balkeny, CHelper.getScreenValueX(101), (int)(11));
			
			shapeRenderer.set(ShapeType.Filled);
			shapeRenderer.setColor(0f, 0.8f, 0f, 0.8f);
			if(avghealth<70)
				shapeRenderer.setColor(0.8f,0.8f,0,0.9f);
			if(avghealth<30)
				shapeRenderer.setColor(0.6f, 0, 0, 0.7f);
			shapeRenderer.rect(ihealth_x+30, ihealth_y-balkeny, CHelper.getScreenValueX(avghealth), (int)(10));
			
			//Happiness
			shapeRenderer.set(ShapeType.Line);
			shapeRenderer.setColor(0.8f, 0.8f, 0.8f, 0.7f);
			shapeRenderer.rect(ihappyness_x+30, ihappyness_y-balkeny, CHelper.getScreenValueX(101), (int)(11));
			
			shapeRenderer.set(ShapeType.Filled);
			shapeRenderer.setColor(0f, 0.8f, 0f, 0.8f);
			if(avghappyness<70)
				shapeRenderer.setColor(0.8f,0.8f,0,0.9f);
			if(avghappyness<30)
				shapeRenderer.setColor(0.6f, 0, 0, 0.7f);
			shapeRenderer.rect(ihappyness_x+30, ihappyness_y-balkeny, CHelper.getScreenValueX(avghappyness), (int)(10));			
			
		shapeRenderer.end();
		//Gdx.gl.glDisable(GL30.GL_BLEND);
		editorSpriteBatch.begin();
				
		if(town.bNoRequirements)
		{
			energyconsumption=0;
			energyoutput=456;
			//waterconsumption=0;
			//wateroutput=2444;
			population=1747;
		}
		
		String senergyvalue=""+(Math.abs(energyoutput-energyconsumption));
		//String swatervalue=""+(Math.abs(wateroutput-waterconsumption));
		float ambvalue = town.gameWorld.getAmbientLightValue(town.gameWorld.worldTime);
		
		int populationmin=40;
		
		//Towninfo
		if(Gdx.graphics.getWidth()>1200)
		{
			if(Gdx.graphics.getWidth()>1400)
				populationmin=42;
			if(Gdx.graphics.getWidth()>1500)
				populationmin=45;
			if(Gdx.graphics.getWidth()>1600)
				populationmin=47;
			if(Gdx.graphics.getWidth()>1700)
				populationmin=51;
			if(Gdx.graphics.getWidth()>1800)
				populationmin=54;
			
			town.gameFont.layout.setText(tiFont, String.valueOf(population));
			populationwidth = (int)town.gameFont.layout.width;
			Color dcol = new Color(1f, 1f, 1f, 0.6f);
			editorSpriteBatch.setColor(0.8f,0.8f,0.8f,1);
			po=0; //Population
			py=10;
			
			editorSpriteBatch.setShader(null);
			
			guiinfo_electricity.render(mx, libgdxy, x+10, y+h-30-1-py);
			//guiinfo_water.render(mx, libgdxy, x+100+wcol, y+h-30-py);
			guiinfo_population.render(mx, libgdxy, x+190+wcol+wcol+8-populationmin, y+h-30-py);
			guiinfo_money.render(mx, libgdxy, x+190+populationwidth+wcol+80+po-5, y+h-30-py+0.53f);
			
			posy=-3;
			
			editorSpriteBatch.setShader(town.gameFont.fontShader);
			
			tiFont.setColor(dcol);
			if(energyconsumption > energyoutput)
				tiFont.setColor(1f,0f, 0f, 1f);
			tiFont.draw(editorSpriteBatch, senergyvalue, x+40-2-textleft-5, y+h-20+posy-texttop);
			
			tiFont.setColor(dcol);
			//if(waterconsumption > wateroutput)
			//	tiFont.setColor(1f,0f, 0f, 1f);
			//tiFont.draw(editorSpriteBatch, swatervalue, x+wcol+130-textleft, y+h-20+posy-texttop);
			
			tiFont.setColor(dcol);
			tiFont.draw(editorSpriteBatch, String.valueOf(population), x+wcol*2+220-textleft+8-populationmin, y+h-20+posy-texttop);
			
			tiFont.setColor(dcol);
			tiFont.draw(editorSpriteBatch, ""+town.gameWorld.townMoney, x+190+populationwidth+103-textleft+po+wcol-5, y+h-20+posy-texttop);
			
			editorSpriteBatch.setShader(null);
			guiinfo_icon_health.render(mx, libgdxy, ihealth_x+3, ihealth_y-10-1);
			guiinfo_icon_happiness.render(mx, libgdxy, ihappyness_x+2, ihappyness_y-10-1);
			
		}
		else
		{
			tiFont=font;
			tiFont=town.gameFont.bfArial2;
			tiFont.getData().setScale(0.53f);
			town.gameFont.layout.setText(tiFont, String.valueOf(population));
			populationwidth = (int)town.gameFont.layout.width;
			Color dcol = new Color(1f, 1f, 1f, 1f);
			editorSpriteBatch.setColor(0.8f,0.8f,0.8f,1);
						
			editorSpriteBatch.setShader(null);
			guiinfo_electricity.render(mx, libgdxy, x+10, y+h-30-1-py);
			//guiinfo_water.render(mx, libgdxy, x+100-20, y+h-30-py);
			guiinfo_population.render(mx, libgdxy, x+190-40+2-populationmin, y+h-30-py);
			guiinfo_money.render(mx, libgdxy, x+190+populationwidth+15+5, y+h-30-py+0.5f+0.7f);
			
			fup+=0.8f;
			tiFont.setColor(dcol);
			if(energyconsumption>energyoutput)
				tiFont.setColor(1f,0f, 0f, 1f);
			tiFont.draw(editorSpriteBatch, senergyvalue, x+40-2-textleft-5, y+h-20+posy+fup);
			
			tiFont.setColor(dcol);
			//if(waterconsumption>wateroutput)
			//	tiFont.setColor(1f,0f, 0f, 1f);
			//tiFont.draw(editorSpriteBatch, swatervalue, x+130-20-textleft, y+h-20+posy+fup);
			
			tiFont.setColor(dcol);
			tiFont.draw(editorSpriteBatch, String.valueOf(population), x+220-40-textleft+2-populationmin, y+h-20+posy+fup);
			
			tiFont.setColor(dcol);
			tiFont.draw(editorSpriteBatch, ""+town.gameWorld.townMoney, x+190+populationwidth+15+23-textleft+5, y+h-20+posy+fup);
						
			fup-=0.8f;
			
			editorSpriteBatch.setShader(null);
			guiinfo_icon_health.render(mx, libgdxy, ihealth_x+3, ihealth_y-9-2);
			guiinfo_icon_happiness.render(mx, libgdxy, ihappyness_x+2, ihappyness_y-9-2);
			editorSpriteBatch.setShader(town.gameFont.fontShader);
		}			
	}
	
	public void renderHint(int mx, int libgdxy)
	{
		if(dlgShowing() && iShowHint==9) //wegen startmenu
			iShowHint=10;
		
		if(dlgShowing()) //wegen startmenu
			return;
		
		if(town.gameWorld.worldAddressList.size()==0)
		{
			//if(iShowHint==0)
			{
				town.gameFont.bfArial.getData().setScale(0.57f);
				String shint="HINTS";
				String shint2="ADDRESS TEMPLATES";
				if(iShowHint>0)
					shint="CANCEL HINTS";
				town.gameFont.layout.setText(town.gameFont.bfArial, shint);
				//int posy = (int) (Gdx.graphics.getHeight()/1.5f);
				int posy = (int) (buttonX2.controlY-50);
				int posx = (int) (Gdx.graphics.getWidth()-town.gameFont.layout.width-70);
				
				int posy2 = (int) (buttonX2.controlY-70);
				int posx2 = (int) (Gdx.graphics.getWidth()-town.gameFont.layout.width-70);
				
				int delta=5;			
				
				//Background
				editorSpriteBatch.end();
				Gdx.gl.glEnable(GL30.GL_BLEND);
				Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
		    	shapeRenderer.setAutoShapeType(true);
				shapeRenderer.begin();
				shapeRenderer.setColor(0.1f, 0.1f, 0.1f, 0.4f);
				shapeRenderer.set(ShapeType.Filled);
				int w=(int) (town.gameFont.layout.width+delta*2+100);
				int h=(int) (town.gameFont.layout.height+delta*2);
				shapeRenderer.rect(posx-delta, posy-town.gameFont.layout.height-delta, w, h);
				shapeRenderer.end();
				editorSpriteBatch.begin();
				
				//Text
				editorSpriteBatch.setShader(town.gameFont.fontShader);
				town.gameFont.bfArial.setColor(1,1,1,0.5f);
				bSetHint=false;
				if(mx>posx-delta && mx<posx-delta+w && libgdxy>posy-town.gameFont.layout.height-delta && libgdxy<posy-delta+h/2)
				{
					bSetHint=true;
					town.gameFont.bfArial.setColor(1,1,1,0.9f);
				}
				
				town.gameFont.bfArial.draw(editorSpriteBatch, shint, posx, posy);
				//town.gameFont.bfArial.draw(editorSpriteBatch, shint2, posx2, posy2);
				editorSpriteBatch.setShader(null);
			}
		}
		
		if(iShowHint==1)
		{
			tooltiphint.textLines.clear();
			tooltiphint.textLines.add("The first step would be to create your first address blueprint.");
			tooltiphint.textLines.add("Please click on the 'Real Estate' Menu Button.");
			if(level1_objtypeid.equals("003"))
				iShowHint=2;
		}
		
		if(iShowHint==2)
		{
			tooltiphint.textLines.clear();
			tooltiphint.textLines.add("Now click the 'Public/Commercial Address' Button from the second Menu.");
			if(bAddressPlacing && addressPlacingType.equals("address_public"))
				iShowHint=3;
		}
		
		if(iShowHint==3)
		{
			tooltiphint.textLines.clear();
			tooltiphint.textLines.add("You are now in the 'Create Address' Mode.");
			tooltiphint.textLines.add("You can cancel this mode by clicking on the right mouse button.");
			tooltiphint.textLines.add("Please click the left mouse button and move your mouse.");
			tooltiphint.textLines.add("If you have reached your preferred address size, you can release the mouse button or click again.");
			if(town.gameWorld.worldAddressList.size()>0)
				iShowHint=4;
		}
		
		if(iShowHint>0 && iShowHint<4)
			if(town.gameWorld.worldAddressList.size()>0)
				iShowHint=4;
		
		if(iShowHint==4)
		{
			tooltiphint.textLines.clear();
			tooltiphint.textLines.add("Please click the 'Public/Commercial' Menu Button and choose 'Electrical Works' from the second Menu below.");
			if(level2_objtypeid.equals("009001"))
				iShowHint=5;
		}
		
		if(iShowHint==5)
		{
			tooltiphint.textLines.clear();
			tooltiphint.textLines.add("Now choose the 'Electrical Works Office' Building from the Menu and place it inside your address");
			tooltiphint.textLines.add("You can adjust the size of the Building by scrolling the Mouse Wheel.");
														       
			if(objPlacing!=null && objPlacing.objectId.contains("009001001"))
				iShowHint=6;
		}
		
		if(iShowHint==6)
		{
			if(objPlacing==null || !objPlacing.objectId.contains("009001001"))
				iShowHint=7;
		}
		
		if(iShowHint==7)
		{
			tooltiphint.textLines.clear();
			tooltiphint.textLines.add("You can create Address Blueprints.");
			tooltiphint.textLines.add("Click the Label/Infobox of your first address and choose the 'Save as Blueprint' Button ");
			tooltiphint.textLines.add("at the bottom left corner of the dialog.");

			//Gdx.app.debug("", ""+bAddressCloning);
			if(bAddressCloning)
				iShowHint=8;
		}
		
		if(iShowHint==8)
		{
			if(!bAddressCloning)
				iShowHint=9;
		}
		
		if(iShowHint==9)
		{
			tooltiphint.textLines.clear();
			tooltiphint.textLines.add("Please click the 'Real Estate' Menu Button and choose 'Create Address from Template'.");
			tooltiphint.textLines.add("Choose your Blueprint and double click it or click the 'load' button.");
			//if(listDlg.dlgShowing())
			//	iShowHint=10;
		}
		
		if(iShowHint==10)
		{
			iShowHint=0;
			tooltiphint.textLines.clear();
		}
		
		if(iShowHint>0 && tooltiphint.textLines.size()>0)
		{
			int hx = (int) (Gdx.graphics.getWidth()/2-tooltiphint.layout.width/2);
			int hy = (int) (Gdx.graphics.getHeight()/1.2f);
			editorSpriteBatch.end();
			tooltiphint.draw(hx, hy);
			editorSpriteBatch.begin();
		}
	}
	
	public void renderTowninfoTooltips(int mx, int libgdxy)
	{
		editorSpriteBatch.end();
		guiinfo_sleep.renderTooltip(mx, libgdxy);
		guiinfo_residentenergy.renderTooltip(mx, libgdxy);
		guiinfo_eat.renderTooltip(mx, libgdxy);
		guiinfo_clean.renderTooltip(mx, libgdxy);
		guiinfo_toilet.renderTooltip(mx, libgdxy);
		
		guiinfo_electricity.renderTooltip(mx, libgdxy);
		//guiinfo_water.renderTooltip(mx, libgdxy);
		guiinfo_population.renderTooltip(mx, libgdxy);
		guiinfo_money.renderTooltip(mx, libgdxy);
		guiinfo_icon_health.renderTooltip(mx, libgdxy);
		guiinfo_icon_happiness.renderTooltip(mx, libgdxy);
		editorSpriteBatch.begin();		
	}
	
	public void renderFlashlightButton(int mx, int my)
	{
		int resy=0;
		//if(Gdx.graphics.getWidth()<920)
		//	resy=40;//Gdx.graphics.getHeight()-80;
		
		CObject flashlight=null;
		
		if(bFlashlight)
			flashlight = objFlashlightControl2;
		else
			flashlight = objFlashlightControl1;
		
		editorSpriteBatch.setColor(1, 1, 1, 1f);
		
		flashlight.icon_pos_x = (int)Gdx.graphics.getWidth()-30;
		flashlight.icon_pos_y = 20+resy;//(int)Gdx.graphics.getHeight()-(int)CHelper.getScreenValueY(70);
		flashlight.icon_width = (int) town.getSizeValue2(flashlight.width);
		flashlight.icon_height = (int) town.getSizeValue2(flashlight.height);

		if(flashlight.collidesIcon(mx, my))
			editorSpriteBatch.draw(flashlight.textureImage, flashlight.icon_pos_x-2, flashlight.icon_pos_y-4, flashlight.icon_width*1.2f, flashlight.icon_height*1.2f);
		else
			editorSpriteBatch.draw(flashlight.textureImage, flashlight.icon_pos_x, flashlight.icon_pos_y, flashlight.icon_width, flashlight.icon_height);
        
	}
	public void renderAddressButton(int mx, int my)
	{
		int resy=0;
		
		TextureRegion address=null;
		address = objAddressControl1.textureRegion[iShowAddresses];
		editorSpriteBatch.setColor(1, 1, 1, 1f);
		
		objAddressControl1.icon_pos_x = (int)Gdx.graphics.getWidth()-60;
		objAddressControl1.icon_pos_y = 20+resy;
		objAddressControl1.icon_width = (int) town.getSizeValue2(objAddressControl1.width);
		objAddressControl1.icon_height = (int) town.getSizeValue2(objAddressControl1.height);
		
		if(objAddressControl1.collidesIcon(mx, my))
			editorSpriteBatch.draw(address, objAddressControl1.icon_pos_x-3, objAddressControl1.icon_pos_y-3, objAddressControl1.icon_width*1.2f, objAddressControl1.icon_height*1.2f);
		else
			editorSpriteBatch.draw(address, objAddressControl1.icon_pos_x, objAddressControl1.icon_pos_y, objAddressControl1.icon_width, objAddressControl1.icon_height);
	}	
	public void renderTimeInformationControl(int mx, int my)
	{
		int topy=0;
		int topx=(int)Gdx.graphics.getWidth()-(int)CHelper.getScreenValueX(190)-2;
		topx+=10;
		
		int dx=topx+12;
		int dy=(int)Gdx.graphics.getHeight()-(int)(200)-(int)topy-5+50;
		
		if(Gdx.graphics.getWidth()>1200)
		{
			dy-=25;
		}
		else
		{
			dy-=19;
		}
		
		
        editorSpriteBatch.end();
        
		//Background
		//editorSpriteBatch.setColor(1,1,1,0.4f);
		//editorSpriteBatch.draw(gameResourceConfig.textures.get("gui_calendar"), dx-4, dy, 124+9, 95);
		//		editorSpriteBatch.setColor(1,1,1,0.2f);
		//		editorSpriteBatch.draw(gameResourceConfig.textures.get("gui_calendar"), dx-5, dy, 124+10, 95);
		//		editorSpriteBatch.setColor(1,1,1,0.046f);
		//		editorSpriteBatch.draw(gameResourceConfig.textures.get("gui_calendar"), dx-4-5, dy-4, 124+8+10, 95+8);
		//		editorSpriteBatch.setColor(1,1,1,0.046f);
		//		editorSpriteBatch.draw(gameResourceConfig.textures.get("gui_calendar"), dx-7-5, dy-7, 124+14+10, 95+14);
		
        
		//if(!Gdx.gl.glIsEnabled(GL30.GL_BLEND))
		{
			Gdx.gl.glEnable(GL30.GL_BLEND);
			Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
		}
    	shapeRenderer.setAutoShapeType(true);
		shapeRenderer.begin();
		
		//mitte
		shapeRenderer.setColor(0.1f, 0.1f, 0.1f, 0.2f);
		shapeRenderer.set(ShapeType.Filled);
		shapeRenderer.rect(dx-5f, dy+28.36f, 124+10, 39.15f); //großer rahmen
		shapeRenderer.rect(dx+1-1, dy+35-2, 122+4, 24f+5); //kleiner rahmen
		
		//oben
		shapeRenderer.rect(dx-5f, dy+68.36f, 124+10, 33.15f); //großer rahmen
		shapeRenderer.rect(dx+1-1, dy+68.36f, 122+4, 24f+5); //kleiner rahmen
		
		//unten
		//shapeRenderer.rect(dx-5f, dy-10, 124+10, 39.15f+6); //großer rahmen
		//shapeRenderer.rect(dx+1-1, dy-10, 122+4, 24f+5+4); //kleiner rahmen
		
		//shapeRenderer.set(ShapeType.Line);
		//shapeRenderer.setColor(0.7f, 0.7f, 0.7f, 0.2f);
		//shapeRenderer.rect(dx+1-1, dy+35-1, 122+4, 24f+5);
		
		shapeRenderer.end();
		//Gdx.gl.glDisable(GL30.GL_BLEND);
		
		
		editorSpriteBatch.begin();
		
        //*************************
        //Show Datetime Information
        //*************************
		editorSpriteBatch.setShader(town.gameFont.fontShader);
		town.gameFont.bfArial.setColor(1,1,1,0.8f);
		
		//Year
		int x_year=dx+33;
		int y_year=dy+94;
		town.gameFont.bfArial.getData().setScale(0.8f);
		town.gameFont.layout.setText(town.gameFont.bfArial, town.gameWorld.worldTime.getYearString().toUpperCase());
		int fheight=(int)town.gameFont.layout.height;
		int fyearwidth=(int)town.gameFont.layout.width;
		//gameResourceConfig.bfArial.draw(editorSpriteBatch, gameWorld.worldTime.getYearString().toUpperCase(), x_year, y_year);
		
		//Month
		town.gameFont.bfArial.getData().setScale(1.4f);
		String mstring = town.gameWorld.worldTime.getMonthString().toUpperCase() + "/" + town.gameWorld.worldTime.getCurrentDayString().toUpperCase();
		
		//		int movex=40;
		//		if(mstring.equals("FEB") || mstring.equals("JUL"))
		//			movex+=5;
		//		if(mstring.equals("SEP"))
		//			movex+=1;
		//		if(mstring.equals("AUG"))
		//			movex-=1;
		
		float fv=0.95f;
		town.gameFont.bfArial.setColor(fv,fv,fv,0.8f);
		//Time
		town.gameFont.bfArial.getData().setScale(0.9f);
		town.gameFont.layout.setText(town.gameFont.bfArial, town.gameWorld.worldTime.getAMPMTimeString().toUpperCase());
		int ftimewidth=(int)town.gameFont.layout.width;
		town.gameFont.bfArial.draw(editorSpriteBatch, town.gameWorld.worldTime.getAMPMTimeString().toUpperCase(), dx+3, dy+60f);
		
		
		town.gameFont.bfArial.getData().setScale(0.7f);
		town.gameFont.layout.setText(town.gameFont.bfArial, mstring);
		int fmonthwidth=(int)town.gameFont.layout.width;
		int x_month=dx+ftimewidth/2-(int)(town.gameFont.layout.width/2)+3;//+movex;
		int y_month=dy+22+70;
		town.gameFont.bfArial.draw(editorSpriteBatch, mstring, x_month, y_month);
				
		editorSpriteBatch.setShader(null);
				
		
		//Tooltips
//		if(mx>x_year && mx<x_year+fyearwidth && my>y_year-fheight && my<y_year+fheight-fheight)
//		{
//			editorSpriteBatch.end();
//			tooltip_timeinformation.draw(mx, my-20, "Year");
//			editorSpriteBatch.begin();
//		}
		
		
		//Show Editor Controls
		CObject buttonControl=null;
		
		if(town.gameWorld.worldPause)
			buttonControl = ((Optional<CObject>)editorControlsShowing.stream().filter(p->p.editoraction.equals("control_button_play1")).findFirst()).get();
		else
			buttonControl = ((Optional<CObject>)editorControlsShowing.stream().filter(p->p.editoraction.equals("control_button_pause1")).findFirst()).get();
		
    	
    	//******************
		//Play/Pause Control
    	//******************
        buttonControl.icon_pos_x = dx+21; //(int)Gdx.graphics.getWidth()-(int)CHelper.getScreenValueX(240);
        buttonControl.icon_pos_y = (int)dy-40+36;
        buttonControl.icon_width = (int)26;
        buttonControl.icon_height = (int)26;
        
        buttonX2.setPosition(buttonControl.icon_pos_x+36-8+5+4, buttonControl.icon_pos_y);
        buttonX4.setPosition(buttonControl.icon_pos_x+72-16+5+6, buttonControl.icon_pos_y);
        
        editorSpriteBatch.end();
        buttonX2.render(mx, my);
        buttonX4.render(mx, my);
        editorSpriteBatch.begin();
        
        editorSpriteBatch.setColor(0.9f, 0.9f, 0.9f, 0.6f);
        if(buttonControl.collidesIcon(mx, my))
        	editorSpriteBatch.draw(buttonControl.textureImage, buttonControl.icon_pos_x-1, buttonControl.icon_pos_y-1, buttonControl.icon_width+2, buttonControl.icon_height+2);
        else
        	editorSpriteBatch.draw(buttonControl.textureImage, buttonControl.icon_pos_x, buttonControl.icon_pos_y, buttonControl.icon_width, buttonControl.icon_height);
        
        editorSpriteBatch.end();
        buttonX2.renderToggle(mx, my); //toggle soll über anderen buttons sein
        buttonX4.renderToggle(mx, my);		
        		
		if(mx>x_month && mx<x_month+fmonthwidth && my>y_month-fheight && my<y_month+fheight-fheight)
		{
			//editorSpriteBatch.end();
			tooltip_timeinformation.draw(mx, my-20, "Month / Day");
			
		}
		editorSpriteBatch.begin();
	}
	
	public void calculateRoomCloningAndMovingPosition()
	{
		if(bRoomCloning)
		{
			CWorldObject theroom = null;
			for(CWorldObject wobj : town.gameWorld.cloneRoomList)
			{
				if(wobj.theobject.isRoomObject)
				{
					theroom = wobj;
					break;
				}
			}
			
	    	Vector3 vm = new Vector3();
	    	vm.x = Gdx.input.getX();
	    	vm.y = Gdx.input.getY();
	    	vm = town.gameCam.unproject(vm);
	    	float mx=vm.x;
	    	float my=vm.y;
	    	
	    	float diffX = mx-theroom.pos_x();
	    	float diffY = my-theroom.pos_y();
	    	
	    	int oldx = theroom.pos_x();
	    	int oldy = theroom.pos_y();
	    	
	    	int newx = theroom.pos_x()+Math.round(diffX);
	    	int newy = theroom.pos_y()+Math.round(diffY);
			if(theroom.theobject.iRasterValue>0)
			{
				newx = (int)(newx / theroom.theobject.iRasterValue);
				newx = (int)(newx * theroom.theobject.iRasterValue);
				newy = (int)(newy / theroom.theobject.iRasterValue);
				newy = (int)(newy * theroom.theobject.iRasterValue);
			}
	    	
	    	theroom.setPosition(newx, newy);
	    	
	    	int diffxnew = newx-oldx;
	    	int diffynew = newy-oldy;
	    	
	    	for(CWorldObject wobj : town.gameWorld.cloneRoomList)
			{
	    		if(!wobj.theobject.isRoomObject)
	    		{
	    			int px1 = wobj.pos_x()+Math.round(diffxnew);
	    			int py1 = wobj.pos_y()+Math.round(diffynew);
	    			wobj.setPosition((px1), (py1));
	    		}
			}
		}
	}
	
	public void calculateAddressCloningAndMovingPosition()
	{
		if(movingAddress!=null)
		{
	    	Vector3 vm = new Vector3();
	    	vm.x = Gdx.input.getX();
	    	vm.y = Gdx.input.getY();
	    	vm = town.gameCam.unproject(vm);
	    	float mx=vm.x;
	    	float my=vm.y;
	    	
	    	float diffX = mx-movingAddress.sx-moveX;
	    	float diffY = my-movingAddress.sy-moveY;
	    	
	    	movingAddress.sx+=Math.round(diffX);
	    	movingAddress.sy+=Math.round(diffY);
	    	movingAddress.ex+=Math.round(diffX);
	    	movingAddress.ey+=Math.round(diffY);
	    	
			for(CWorldObject wobj : movingAddress.listWorldObjects)
			{
				if(!wobj.isHuman())
				{
					int px1 = wobj.pos_x()+Math.round(diffX);
					int py1 = wobj.pos_y()+Math.round(diffY);
					wobj.setPosition((px1), (py1));
				}
			}
			
			for(CWorldObject wobj : movingAddress.listWorldObjects_Floors)
			{
				int px1 = wobj.pos_x()+Math.round(diffX);
				int py1 = wobj.pos_y()+Math.round(diffY);
				//wobj.setPosition(getRasterValue(px1), getRasterValue(py1));
				wobj.setPosition((px1), (py1));
			}
			
			for(CWorldObject wobj : movingAddress.listWorldObjects_Ground)
			{
				int px1 = wobj.pos_x()+Math.round(diffX);
				int py1 = wobj.pos_y()+Math.round(diffY);
				//wobj.setPosition(getRasterValue(px1), getRasterValue(py1));
				wobj.setPosition((px1), (py1));
			}			
		}
		
		if(clonedAddress!=null)
		{
	    	Vector3 vm = new Vector3();
	    	vm.x = Gdx.input.getX();
	    	vm.y = Gdx.input.getY();
	    	vm = town.gameCam.unproject(vm);
	    	float mx=vm.x;
	    	float my=vm.y;
	    	
	    	float diffX=mx-clonedAddress.sx-((clonedAddress.ex-clonedAddress.sx)/2);
	    	float diffY=my-clonedAddress.sy-((clonedAddress.ey-clonedAddress.sy)/2);
	    	
	    	clonedAddress.sx+=Math.round(diffX);
	    	clonedAddress.sy+=Math.round(diffY);
	    	clonedAddress.ex+=Math.round(diffX);
	    	clonedAddress.ey+=Math.round(diffY);
	    	
			for(CWorldObject wobj : clonedAddress.listWorldObjects)
			{
				int px1 = wobj.pos_x()+Math.round(diffX);
				int py1 = wobj.pos_y()+Math.round(diffY);
				wobj.setPosition((px1), (py1));
			}
			
			for(CWorldObject wobj : clonedAddress.listWorldObjects_Floors)
			{
				int px1 = wobj.pos_x()+Math.round(diffX);
				int py1 = wobj.pos_y()+Math.round(diffY);
				wobj.setPosition((px1), (py1));
			}
			
			for(CWorldObject wobj : clonedAddress.listWorldObjects_Ground)
			{
				int px1 = wobj.pos_x()+Math.round(diffX);
				int py1 = wobj.pos_y()+Math.round(diffY);
				wobj.setPosition((px1), (py1));
			}			
		}
	}
	
	private void checkMoveObject(int mx, int my)
	{
		if(dlgShowing() || bButtonMouseover)
			return;
		
		if(town.gameWorld.markerObject!=null && town.gameWorld.markerObject.iZombie>=1)
			return;
		
		if(!bButtonDown_MoveObject && town.gameWorld.markerObject!=null && Gdx.input.isButtonPressed(0))
		{
			moveobjectdelaytimer+=Gdx.graphics.getDeltaTime();
			
			if(moveobjectdelaytimer>0.1f)
			{
				if(town.gameWorld.moveObjectStart(Gdx.input.getX(),Gdx.input.getY()))
				{
					bButtonDown_MoveObject=true;
					objPlacing = town.gameWorld.markerObject.theobject; //start object placing movement
				}
				
				moveobjectdelaytimer=0;
			}
		}
	}
	
	/*
	private int getRasterValue(int value)
	{
		int raster=town.floorrastersize;
		
		float xval = (float)value/(float)raster;
		if(xval>(int)xval)
		{
			value = (int)(value/raster);
			value = (int)(value*raster);
		}
		
		return value;
	}
	*/
	public void renderPause()
	{
		//if(town.gameWorld.worldPause)
		//	return;
		
		if(!town.gameWorld.worldPause)
			return;
		
		//if(renderSecond>0.5f)
		{
			String pausetext="P A U S E";
			town.gameFont.bfArial.getData().setScale(0.6f);
			town.gameFont.layout.setText(town.gameFont.bfArial, pausetext);
			//int px = (int)(Gdx.graphics.getWidth()/2-town.gameFont.layout.width/2);
			//int py = (int)(Gdx.graphics.getHeight()/2+town.gameFont.layout.height/2);
			int px = (int)(buttonX2.controlX-35);
			int py = (int)(buttonX2.controlY-14);
						
			editorSpriteBatch.end();
			
			//if(!Gdx.gl.glIsEnabled(GL30.GL_BLEND))
			{
				Gdx.gl.glEnable(GL30.GL_BLEND);
				Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
			}
			
		    shapeRenderer.setAutoShapeType(true);
			shapeRenderer.begin();
				
			shapeRenderer.setColor(0.1f, 0.1f, 0.1f, 0.6f);
			shapeRenderer.set(ShapeType.Filled);
			//shapeRenderer.rect(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
			shapeRenderer.rect(buttonX2.controlX-40,buttonX2.controlY-34,100,25);
				
			shapeRenderer.end();
			//Gdx.gl.glDisable(GL30.GL_BLEND);
			editorSpriteBatch.begin();
						
			editorSpriteBatch.setShader(town.gameFont.fontShader);
			town.gameFont.bfArial.setColor(0.8f,0.8f,0.8f,0.8f);
			town.gameFont.bfArial.draw(editorSpriteBatch, pausetext, px, py);
			editorSpriteBatch.setShader(null);
		}
	}
	//RENDER<-------------------------------------------------------------
}














