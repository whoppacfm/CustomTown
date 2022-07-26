package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.mygdx.game.CStatistics.CStatisticsData_Finance;
import com.mygdx.game.CStatistics.CStatisticsData_Other;
import com.mygdx.game.CStatistics.CStatisticsData_Population;

public class CGuiDialog_ListStatistics extends CGuiDialog {
	
	public static enum ListTypeStatistics { 
		DEFAULT, STATISTICS_FINANCE, STATISTICS_POPULATION_COUNT, STATISTICS_POPULATION_ATTRIBUTES, STATISTICS_OTHER, NOTHING
	}
	
	private CGuiControl_Button buttonOK;
	private CGuiControl_Button buttonCancel;
	private CGuiControl_Button buttonPagingLeft;
	private CGuiControl_Button buttonPagingRight;
	private CGuiControl_Points pointsControl;
	
	CObject control_hakenok;
	int eintragYStart;
	int eintragX;
	int eintragH;
	Boolean bChosen;
	int iChosen;
	int pageSize;
	int pageIndex;
	int activePageRowCount;
	
	ListTypeStatistics listType;
	int typenr;
	CGuiTooltip tooltip;
	CAddress address;
	
	ArrayList<CStatisticsData_Finance> financeStatistics;
	ArrayList<CStatisticsData_Other> otherStatistics;
	ArrayList<CStatisticsData_Population> populationStatistics;
	
	public CGuiDialog_ListStatistics(List<CObject> controlList, BitmapFont font, int dialogX, int dialogY, CTown town1)
	{
		super("CGuiDialog_ChooseResident");
		
		typenr=0;
		iChosen=-1;
		town=town1;
		
		listType=ListTypeStatistics.DEFAULT;
		
		dlgSpriteBatch=town1.gameGui.editorSpriteBatch;
		dlgShapeRenderer=town1.gameGui.shapeRenderer;
		dlgFont=font;
		tooltip=new CGuiTooltip(town1);
		dlgX=dialogX;
		dlgY=dialogY;
		
		dialogW=600;
		dialogH=500;
		
		setMiddlePosition();
		
		pageSize=17;
		pageIndex=1;
		activePageRowCount=0;
		
		bChosen=false;
		
		buttonOK=new CGuiControl_Button(dlgX+dialogW/2-105, dlgY+15-2, 100, 25, 43, 18, "ok", dlgFont, null, CGuiControl_Button.ButtonType.DEFAULT, town1);
		buttonCancel=new CGuiControl_Button(dlgX+dialogW/2+5, dlgY+15-2, 100, 25, 31+4, 18, "close", dlgFont, null, CGuiControl_Button.ButtonType.DEFAULT, town1);
		
		buttonPagingLeft=new CGuiControl_Button(dlgX+dialogW/2-30-20, dlgY+70, 30, 30, 0, 0, "", dlgFont, town.gameResourceConfig.textures.get("gui_arrowleft"), CGuiControl_Button.ButtonType.IMAGE, town1);
		buttonPagingRight=new CGuiControl_Button(dlgX+dialogW/2+20, dlgY+70, 30, 30, 0, 0, "", dlgFont, town.gameResourceConfig.textures.get("gui_arrowright"), CGuiControl_Button.ButtonType.IMAGE, town1);
		buttonPagingLeft.setColor(new Color(1,1,1,1));
		buttonPagingRight.setColor(new Color(1,1,1,1));
		buttonPagingLeft.renderMode = 0;
		
		initPosition();
		control_hakenok=null;
		Optional<CObject> obj = controlList.stream().filter(item->item.editoraction.contains("control_hakenok")).findFirst();
		if(obj.isPresent())
			control_hakenok=obj.get();
		
		pointsControl=new CGuiControl_Points(town, 0, 0, 3, 1, town.gameGui.shapeRenderer, dlgSpriteBatch);
		pointsControl.showButtons(false);	
		pointsControl.setBeginShapeRenderer(true);
		pointsControl.setPointSize(10);
	}
	
	public void initPosition()
	{
		eintragH=20;
		eintragYStart = dlgY+dialogH-15-eintragH;
		eintragX=dlgX+30;
		
		buttonOK.setPosition(dlgX+dialogW/2-105, dlgY+15-2);
		buttonCancel.setPosition(dlgX+dialogW/2-50, dlgY+15-2);
		
		buttonPagingLeft.setPosition(dlgX+dialogW/2-30-20, dlgY+70);
		buttonPagingRight.setPosition(dlgX+dialogW/2+20, dlgY+70);
	}
	
	public void showDlg(Boolean show, ListTypeStatistics type)
	{
		CWorldObject mobj = town.gameWorld.markerObject;
		super.showDlg(show);
		listType = type;
		
		if(show==true)
		{
			dialogW=900;
			setMiddlePosition();
			initPosition();
			bChosen=false;
			iChosen=-1;
			pageIndex=1;
			activePageRowCount=0;
			
			if(listType==ListTypeStatistics.STATISTICS_FINANCE)
				financeStatistics = town.gameWorld.townStatistics.statisticsData_Finance;
			if(listType==ListTypeStatistics.STATISTICS_POPULATION_ATTRIBUTES || listType==ListTypeStatistics.STATISTICS_POPULATION_COUNT)
				populationStatistics = town.gameWorld.townStatistics.statisticsData_Population;
			if(listType==ListTypeStatistics.STATISTICS_OTHER)
				otherStatistics = town.gameWorld.townStatistics.statisticsData_Other;
		}
	}
	
	public Boolean doubleClick(int x, int y, int libgdxy, int button)
	{
//		if(checkListClick(x,libgdxy))
//		{
//			create();
//			return true;
//		}
		
		return false;
	}
	
	public Boolean keyTyped(char character)
	{
		return true;
	}
	
	public void create()
	{
		showDlg(false);
	}
	
	public Boolean checkListClick(int x, int libgdxy)
	{
//		int listcount = 0;
//		
//		listcount = listWorker.size();
//		
//		if(listcount > pageSize)
//			listcount = pageSize;
//		
//		int count=0;
//		for(int i=1;i<=listcount;i++)
//		{
//			int j=i-1;
//			if(j>activePageRowCount)
//				break;
//			
//			if(x>eintragX && x<eintragX-5+dialogW-5 && libgdxy>=eintragYStart-i*eintragH-eintragH && libgdxy<=eintragYStart-i*eintragH)
//			{
//				bChosen=true;
//				iChosen=count;
//				
//				return true;
//			}
//			count++;
//		}
		
		return false;
	}
	
	public Boolean buttonDown(int x, int y, int libgdxy, int button)
	{
		if(button==0 || button==-99)
		{
			if(buttonPagingLeft.buttonClick(x, libgdxy))
			{
				if(pageIndex>1)
				{
					bChosen=false;
					iChosen=-1;
					
					pageIndex--;
				}
				
				return true;
			}
			
			if(buttonPagingRight.buttonClick(x, libgdxy))
			{
				int size1=0;
				
				if(listType==ListTypeStatistics.STATISTICS_FINANCE)
					size1=financeStatistics.size();
				if(listType==ListTypeStatistics.STATISTICS_POPULATION_ATTRIBUTES || listType==ListTypeStatistics.STATISTICS_POPULATION_COUNT)
					size1=populationStatistics.size();
				if(listType==ListTypeStatistics.STATISTICS_OTHER)
					size1=otherStatistics.size();				
				
				if(pageIndex < Math.ceil(((double)size1)/pageSize))
				{
					bChosen=false;
					iChosen=-1;
					pageIndex++;
				}
				
				return true;
			}
			
			//			if(buttonOK.buttonClick(x, libgdxy) || button==-99)
			//			{
			//				create();
			//				return true;
			//			}
			
			if(buttonCancel.buttonClick(x, libgdxy))
			{
				showDlg(false);
				return true;
			}
			
			//			if(listWorker!=null)
			//			{
			//				checkListClick(x,libgdxy);
			//			}
		}
		else
		{
			showDlg(false);
		}
		
		return true;
	}
	
	public void buttonUp()
	{
	}
	
	public Boolean mouseMovedDrag(int x, int y, int libgdxy)
	{
		return false;
	}
	
	public void render(int x, int libgdxy)
	{
		try
		{
		int istart=0;
		int ilast=0;
		int isize=0;
		
		if(listType==ListTypeStatistics.STATISTICS_FINANCE)
		{
			isize=financeStatistics.size();
			ilast=financeStatistics.size()-1;
		}
		else if(listType==ListTypeStatistics.STATISTICS_OTHER)
		{
			isize=otherStatistics.size();
			ilast=otherStatistics.size()-1;
		}
		else if(listType==ListTypeStatistics.STATISTICS_POPULATION_ATTRIBUTES || listType==ListTypeStatistics.STATISTICS_POPULATION_COUNT)
		{
			isize=populationStatistics.size();
			ilast=populationStatistics.size()-1;
		}
		
		if(isize>pageSize)
		{
			istart=pageSize*(pageIndex-1);
			
			if(pageIndex==1)
				istart=0;
			
			ilast=istart+pageSize-1;
			
			int maxPageIndex = (int)(Math.ceil(((double)isize)/pageSize)); 
			
			if(pageIndex==maxPageIndex)
			{
				ilast=(istart+isize%pageSize)-1;
			}
		}
		
		activePageRowCount=ilast-istart;
		//if(!Gdx.gl.glIsEnabled(GL30.GL_BLEND))
		{
			Gdx.gl.glEnable(GL30.GL_BLEND);
			Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
		}
		dlgShapeRenderer.setAutoShapeType(true);
	    dlgShapeRenderer.begin();
	    	
			//Dialog Background
		    dlgShapeRenderer.set(ShapeType.Filled);
		    dlgShapeRenderer.setColor(town.dialogColor);
		    dlgShapeRenderer.rect(dlgX, dlgY, dialogW, dialogH);
			
			//Dialog Rahmen
		    dlgShapeRenderer.set(ShapeType.Line);
			//dlgShapeRenderer.setColor(0.7f, 0.7f, 0.7f, 0.8f);
		    dlgShapeRenderer.setColor(town.dialogRahmenColor);
			dlgShapeRenderer.rect(dlgX, dlgY, dialogW, dialogH);
			
			//*********************
			//Mouseover GridTable
			//*********************
			int overX=0;
			int overY=0;
			int overChosen=0;
			
			if(financeStatistics!=null || populationStatistics!=null || otherStatistics!=null)
			{
				int listcount=isize;
				
				if(listcount>pageSize)
					listcount=pageSize;
				
				dlgShapeRenderer.set(ShapeType.Line);
				dlgShapeRenderer.setColor(0.7f, 0.7f, 0.7f, 0.8f);
				dlgShapeRenderer.rect(eintragX-5, eintragYStart+5-eintragH+10, dialogW-50, eintragH); //header row
				dlgShapeRenderer.setColor(town.dialogRahmenColor);
				dlgShapeRenderer.line(dlgX+20, dlgY+50, dlgX+dialogW-20, dlgY+50); //Trennlinie zu ok und cancel				
				
				int icount=1;
				for(int i=1;i<=listcount;i++)
				{
					int j=i-1;
					
					if(j>activePageRowCount)
						break;
					
					if(x>eintragX && x<eintragX-5+dialogW-50 && libgdxy>=eintragYStart-i*eintragH-eintragH && libgdxy<=eintragYStart-i*eintragH)
					{
						overChosen=icount;
						overX=x;
						overY=eintragYStart-i*eintragH-eintragH;
						dlgShapeRenderer.rect(eintragX-5, eintragYStart+5-i*eintragH-eintragH, dialogW-50, eintragH);
						break;
					}
					
					icount++;
				}
			}
		dlgShapeRenderer.end();
		//Gdx.gl.glDisable(GL30.GL_BLEND);		
				
		dlgSpriteBatch.begin();
			dlgSpriteBatch.setColor(1,1,1,1);
			dlgFont.getData().setScale(1);
			dlgFont.setColor(town.dialogFontColorList);
			
			
			
			
			//********************
			//Draw Table Entries
			//********************
			if(isize > pageSize)
			{
				String sPageIndex = String.format("%02d", pageIndex);
				String sPageCount = String.format("%02d", (int)(Math.ceil(((double)isize)/((double)pageSize))));
				dlgFont.draw(dlgSpriteBatch, sPageIndex + "/" + sPageCount, buttonPagingLeft.controlX+buttonPagingLeft.controlW + 4f, buttonPagingLeft.controlY+buttonPagingLeft.controlH/2+6);
			}
			
			if(listType==ListTypeStatistics.STATISTICS_FINANCE)
			{
				GlyphLayout lo = town.gameFont.layout;
				
				String sDay="Day";
				String sAddresses="Addresses";
				String sObjects="Objects";
				String sResidents="Residents";
				String sTown="Town";
				String sHappiness="Happiness";
				String sChildSupport="Child Support";
				String sEducation="Education";
				String sDeceased="Deceased";
				
				int count=0;
				int colw=25;
				
				lo.setText(dlgFont, sDay);
				int x_spalte2=(int) (eintragX+lo.width)+colw;
				lo.setText(dlgFont, sAddresses);
				int x_spalte3=(int) (x_spalte2+lo.width)+colw;
				lo.setText(dlgFont, sObjects);
				int x_spalte4=(int) (x_spalte3+lo.width)+colw;
				lo.setText(dlgFont, sResidents);
				int x_spalte5=(int) (x_spalte4+lo.width)+colw;
				lo.setText(dlgFont, sTown);
				int x_spalte6=(int) (x_spalte5+lo.width)+colw;
				lo.setText(dlgFont, sHappiness);
				int x_spalte7=(int) (x_spalte6+lo.width)+colw;
				lo.setText(dlgFont, sChildSupport);
				int x_spalte8=(int) (x_spalte7+lo.width)+colw;
				lo.setText(dlgFont, sEducation);
				int x_spalte9=(int) (x_spalte8+lo.width)+colw;
				lo.setText(dlgFont, sDeceased);
				int x_spalte10=(int) (x_spalte9+lo.width)+colw;
				
				dlgFont.draw(dlgSpriteBatch, sDay, eintragX, eintragYStart+10);
				dlgFont.draw(dlgSpriteBatch, sAddresses, x_spalte2, eintragYStart+10);
				dlgFont.draw(dlgSpriteBatch, sObjects, x_spalte3, eintragYStart+10);
				dlgFont.draw(dlgSpriteBatch, sResidents, x_spalte4, eintragYStart+10);
				dlgFont.draw(dlgSpriteBatch, sTown, x_spalte5, eintragYStart+10);
				dlgFont.draw(dlgSpriteBatch, sHappiness, x_spalte6, eintragYStart+10);
				dlgFont.draw(dlgSpriteBatch, sChildSupport, x_spalte7, eintragYStart+10);
				dlgFont.draw(dlgSpriteBatch, sEducation, x_spalte8, eintragYStart+10);
				dlgFont.draw(dlgSpriteBatch, sDeceased, x_spalte9, eintragYStart+10);
				dlgFont.draw(dlgSpriteBatch, "Total", x_spalte10, eintragYStart+10);
				
				for(int i=istart;i<=ilast;i++)
				{
					CStatisticsData_Finance obj = financeStatistics.get(i);
					
					dlgFont.draw(dlgSpriteBatch, String.format("%04d", obj.day), eintragX, eintragYStart-count*eintragH-eintragH);
					dlgFont.draw(dlgSpriteBatch, "$"+(obj.sellAddress-obj.buyAddress), x_spalte2, eintragYStart-count*eintragH-eintragH);
					dlgFont.draw(dlgSpriteBatch, "$"+(obj.sellObject-obj.buyObject), x_spalte3, eintragYStart-count*eintragH-eintragH);
					dlgFont.draw(dlgSpriteBatch, "$"+(obj.buyResident*-1), x_spalte4, eintragYStart-count*eintragH-eintragH);
					dlgFont.draw(dlgSpriteBatch, "$"+(obj.residentEarnsMoney-obj.residentSpendsMoney), x_spalte5, eintragYStart-count*eintragH-eintragH);
					dlgFont.draw(dlgSpriteBatch, "$"+(obj.happinessplus-obj.happinessminus), x_spalte6, eintragYStart-count*eintragH-eintragH);
					dlgFont.draw(dlgSpriteBatch, "$"+(obj.childsupport), x_spalte7, eintragYStart-count*eintragH-eintragH);
					dlgFont.draw(dlgSpriteBatch, "$"+(obj.education), x_spalte8, eintragYStart-count*eintragH-eintragH);
					dlgFont.draw(dlgSpriteBatch, "$-"+(obj.deceased), x_spalte9, eintragYStart-count*eintragH-eintragH);
					dlgFont.draw(dlgSpriteBatch, "$"+obj.sum, x_spalte10, eintragYStart - count*eintragH-eintragH);
					
					count++;
				}
				
				//Show Tooltip
				if(overX>0)
				{
					dlgSpriteBatch.end();
					tooltip.textLines.clear();
					if(overX>x_spalte2 && overX<x_spalte3-10)
						tooltip.textLines.add("Bought, Sold and Resized Addresses");
					if(overX>x_spalte3 && overX<x_spalte4-10)
						tooltip.textLines.add("Bought and Sold Objects");
					if(overX>x_spalte4 && overX<x_spalte5-10)
						tooltip.textLines.add("Customized Residents");
					if(overX>x_spalte5 && overX<x_spalte6-10)
						tooltip.textLines.add("Residents Earned and Spent Money");
					if(overX>x_spalte6 && overX<x_spalte7-10)
						tooltip.textLines.add("Happy Town People");
					if(overX>x_spalte7 && overX<x_spalte8-10)
						tooltip.textLines.add("Child Support");
					if(overX>x_spalte8 && overX<x_spalte9-10)
						tooltip.textLines.add("Education Funding");
					if(overX>x_spalte9 && overX<x_spalte10-10)
						tooltip.textLines.add("Deceased Residents");
					if(overX>x_spalte10 && overX<x_spalte10+50)
						tooltip.textLines.add("Total");
					
					if(tooltip.textLines.size()>0)
						tooltip.draw(overX, overY);
					
					dlgSpriteBatch.begin();
				}				
			}
			
			if(listType==ListTypeStatistics.STATISTICS_POPULATION_COUNT)
			{
				int count=0;
				int colwidth=70;
				int x_spalte2=eintragX+100;
				int x_spalte3=eintragX+100+colwidth;
				int x_spalte4=eintragX+100+colwidth*2;
				int x_spalte5=eintragX+100+colwidth*3;
				int x_spalte6=eintragX+100+colwidth*4;
				int x_spalte7=eintragX+100+colwidth*5;
				int x_spalte8=eintragX+100+colwidth*6;
				int x_spalte9=eintragX+100+colwidth*7;
				int x_spalte10=eintragX+100+colwidth*8;
				int x_spalte11=eintragX+100+colwidth*9;
				int x_spalte12=eintragX+100+colwidth*10;
				
				dlgFont.draw(dlgSpriteBatch, "Day", eintragX, eintragYStart+10);
				dlgFont.draw(dlgSpriteBatch, "Total", x_spalte2, eintragYStart+10);
				dlgFont.draw(dlgSpriteBatch, "Women", x_spalte3, eintragYStart+10);
				dlgFont.draw(dlgSpriteBatch, "Men", x_spalte4, eintragYStart+10);
				dlgFont.draw(dlgSpriteBatch, "Homeless", x_spalte5, eintragYStart+10);
				dlgFont.draw(dlgSpriteBatch, "Age:", x_spalte6+23, eintragYStart+10);
				dlgFont.draw(dlgSpriteBatch, "0-20", x_spalte7, eintragYStart+10);
				dlgFont.draw(dlgSpriteBatch, "21-40", x_spalte8, eintragYStart+10);
				dlgFont.draw(dlgSpriteBatch, "41-60", x_spalte9, eintragYStart+10);
				dlgFont.draw(dlgSpriteBatch, "61-80", x_spalte10, eintragYStart+10);
				dlgFont.draw(dlgSpriteBatch, "81-100", x_spalte11, eintragYStart+10);
				dlgFont.draw(dlgSpriteBatch, ">100", x_spalte12, eintragYStart+10);
				
				for(int i=istart;i<=ilast;i++)
				{
					//Count
					CStatisticsData_Population obj = populationStatistics.get(i);
					
					dlgFont.draw(dlgSpriteBatch, String.format("%04d", obj.day), eintragX, eintragYStart - count*eintragH-eintragH);
					dlgFont.draw(dlgSpriteBatch, "" + obj.countAll, x_spalte2, eintragYStart - count*eintragH-eintragH);
					dlgFont.draw(dlgSpriteBatch, "" + obj.countWomen, x_spalte3, eintragYStart - count*eintragH-eintragH);
					dlgFont.draw(dlgSpriteBatch, "" + obj.countMen, x_spalte4, eintragYStart - count*eintragH-eintragH);
					dlgFont.draw(dlgSpriteBatch, "" + obj.countHomeless, x_spalte5, eintragYStart - count*eintragH-eintragH);
					dlgFont.draw(dlgSpriteBatch, "" + obj.count0To20, x_spalte7, eintragYStart - count*eintragH-eintragH);
					dlgFont.draw(dlgSpriteBatch, "" + obj.count21To40, x_spalte8, eintragYStart - count*eintragH-eintragH);
					dlgFont.draw(dlgSpriteBatch, "" + obj.count41To60, x_spalte9, eintragYStart - count*eintragH-eintragH);
					dlgFont.draw(dlgSpriteBatch, "" + obj.count61To80, x_spalte10, eintragYStart - count*eintragH-eintragH);
					dlgFont.draw(dlgSpriteBatch, "" + obj.count81To100, x_spalte11, eintragYStart - count*eintragH-eintragH);
					dlgFont.draw(dlgSpriteBatch, "" + obj.count101ToX, x_spalte12, eintragYStart - count*eintragH-eintragH);
					
					count++;
				}
			}
			
			if(listType==ListTypeStatistics.STATISTICS_POPULATION_ATTRIBUTES)
			{
				int count=0;
				int colwidth=85;
				int x_spalte2=eintragX+100-20-8;
				int x_spalte3=eintragX+100+colwidth-20-8;
				int x_spalte4=eintragX+100+colwidth*2+3-20-8;
				int x_spalte5=eintragX+100+colwidth*3+5-20;
				int x_spalte6=eintragX+100+colwidth*4-10;
				
				int x_spalte7=eintragX+100+colwidth*5;
				int x_spalte8=eintragX+100+colwidth*6+5;
				int x_spalte9=eintragX+100+colwidth*7+32;
				
				//int x_spalte10=eintragX+100+colwidth*8;
				//int x_spalte11=eintragX+100+colwidth*9;
				//int x_spalte12=eintragX+100+colwidth*10;
				
				dlgFont.draw(dlgSpriteBatch, "Day", eintragX, eintragYStart+10);
				dlgFont.draw(dlgSpriteBatch, "Age", x_spalte2, eintragYStart+10);
				dlgFont.draw(dlgSpriteBatch, "Health", x_spalte3, eintragYStart+10);
				dlgFont.draw(dlgSpriteBatch, "Happiness", x_spalte4, eintragYStart+10);
				dlgFont.draw(dlgSpriteBatch, "Fitness", x_spalte5, eintragYStart+10);
				dlgFont.draw(dlgSpriteBatch, "Intelligence", x_spalte6, eintragYStart+10);
				dlgFont.draw(dlgSpriteBatch, "Work Output", x_spalte7, eintragYStart+10);
				dlgFont.draw(dlgSpriteBatch, "Health A.", x_spalte8, eintragYStart+10);
				dlgFont.draw(dlgSpriteBatch, "Positive A.", x_spalte9, eintragYStart+10);
				
				for(int i=istart;i<=ilast;i++)
				{
					//Count
					CStatisticsData_Population obj = populationStatistics.get(i);
					
					dlgFont.draw(dlgSpriteBatch, String.format("%04d", obj.day), eintragX, eintragYStart - count*eintragH-eintragH);
//					dlgFont.draw(dlgSpriteBatch, "" + obj.ageAVG+"("+obj.ageMin+"/"+obj.ageMax + ")", x_spalte2, eintragYStart - count*eintragH-eintragH);
//					dlgFont.draw(dlgSpriteBatch, "" + obj.healthAVG+"("+obj.healthMin+"/"+obj.healthMax+ ")", x_spalte3, eintragYStart - count*eintragH-eintragH);
//					dlgFont.draw(dlgSpriteBatch, "" + obj.happinessAVG+"("+obj.happinessMin+"/"+obj.happinessMax+ ")", x_spalte4, eintragYStart - count*eintragH-eintragH);
//					dlgFont.draw(dlgSpriteBatch, "" + obj.fitnessAVG+"("+obj.fitnessMin+"/"+obj.fitnessMax+ ")", x_spalte5, eintragYStart - count*eintragH-eintragH);
//					dlgFont.draw(dlgSpriteBatch, "" + obj.intelligenceAVG+"("+obj.intelligenceMin+"/"+obj.intelligenceMax+ ")", x_spalte6, eintragYStart - count*eintragH-eintragH);
//					dlgFont.draw(dlgSpriteBatch, "" + obj.workoutputAVG+"("+obj.workoutputMin+"/"+obj.workoutputMax+ ")", x_spalte7, eintragYStart - count*eintragH-eintragH);
//					dlgFont.draw(dlgSpriteBatch, "" + CHelper.roundFloat(obj.healthAttitudeAVG,2)+"("+CHelper.roundFloat(obj.healthAttitudeMin,2)+"/"+CHelper.roundFloat(obj.healthAttitudeMax,2)+ ")", x_spalte8, eintragYStart - count*eintragH-eintragH);
//					dlgFont.draw(dlgSpriteBatch, "" + CHelper.roundFloat(obj.positiveAttitudeAVG,2)+"("+CHelper.roundFloat(obj.positiveAttitudeMin,2)+"/"+CHelper.roundFloat(obj.positiveAttitudeMax,2)+ ")", x_spalte9, eintragYStart - count*eintragH-eintragH);
					
					dlgFont.draw(dlgSpriteBatch, "" + obj.ageAVG+" ("+obj.ageMin+"/"+obj.ageMax + ")", x_spalte2, eintragYStart - count*eintragH-eintragH);
					dlgFont.draw(dlgSpriteBatch, "" + obj.healthAVG+" ("+obj.healthMin+"/"+obj.healthMax+ ")", x_spalte3, eintragYStart - count*eintragH-eintragH);
					dlgFont.draw(dlgSpriteBatch, "" + obj.happinessAVG+" ("+obj.happinessMin+"/"+obj.happinessMax+ ")", x_spalte4, eintragYStart - count*eintragH-eintragH);
					dlgFont.draw(dlgSpriteBatch, "" + obj.fitnessAVG+" ("+obj.fitnessMin+"/"+obj.fitnessMax+ ")", x_spalte5, eintragYStart - count*eintragH-eintragH);
					dlgFont.draw(dlgSpriteBatch, "" + obj.intelligenceAVG+" ("+obj.intelligenceMin+"/"+obj.intelligenceMax+ ")", x_spalte6, eintragYStart - count*eintragH-eintragH);
					dlgFont.draw(dlgSpriteBatch, "" + obj.workoutputAVG+" ("+obj.workoutputMin+"/"+obj.workoutputMax+ ")", x_spalte7, eintragYStart - count*eintragH-eintragH);
					dlgFont.draw(dlgSpriteBatch, "" + CHelper.roundFloat(obj.healthAttitudeAVG,2)+" ("+CHelper.roundFloat(obj.healthAttitudeMin,2)+"/"+CHelper.roundFloat(obj.healthAttitudeMax,2)+ ")", x_spalte8, eintragYStart - count*eintragH-eintragH);
					dlgFont.draw(dlgSpriteBatch, "" + CHelper.roundFloat(obj.positiveAttitudeAVG,2)+" ("+CHelper.roundFloat(obj.positiveAttitudeMin,2)+"/"+CHelper.roundFloat(obj.positiveAttitudeMax,2)+ ")", x_spalte9, eintragYStart - count*eintragH-eintragH);
					
					count++;
				}
				
				//Show Tooltip
				dlgSpriteBatch.end();
				tooltip.textLines.clear();
				tooltip.textLines.add("Average (Min / Max)");
				if(tooltip.textLines.size()>0)
					tooltip.draw(overX, overY);
				dlgSpriteBatch.begin();
			}
			
			if(listType==ListTypeStatistics.STATISTICS_OTHER)
			{
				int count=0;
				int colwidth=85;
				int x_spalte2=eintragX+100;
				int x_spalte3=eintragX+100+colwidth;
				int x_spalte4=eintragX+100+colwidth*2;
				int x_spalte5=eintragX+100+colwidth*3;
				int x_spalte6=eintragX+100+colwidth*4;
				int x_spalte7=eintragX+100+colwidth*5+30;
				
				//int x_spalte8=eintragX+100+colwidth*6;
				//int x_spalte9=eintragX+100+colwidth*7;
				//int x_spalte10=eintragX+100+colwidth*8;
				//int x_spalte11=eintragX+100+colwidth*9;
				//int x_spalte12=eintragX+100+colwidth*10;
				
//				//Traffic
//				int countFootpath;
//				int countRoad;
//				int countParkingspace;
//				int countGarage;
//				int countVehiclesPrivate;
//				int countVehiclesPublic;
				
				dlgFont.draw(dlgSpriteBatch, "Day", eintragX, eintragYStart+10);
				dlgFont.draw(dlgSpriteBatch, "Energy", x_spalte2, eintragYStart+10);
				dlgFont.draw(dlgSpriteBatch, "Water", x_spalte3, eintragYStart+10);
				dlgFont.draw(dlgSpriteBatch, "Address", x_spalte4, eintragYStart+10);
				dlgFont.draw(dlgSpriteBatch, "Company", x_spalte5, eintragYStart+10);
				dlgFont.draw(dlgSpriteBatch, "Road/Footpath", x_spalte6, eintragYStart+10);
				dlgFont.draw(dlgSpriteBatch, "Vehicles", x_spalte7, eintragYStart+10);
				//dlgFont.draw(dlgSpriteBatch, "", x_spalte8, eintragYStart+10);
				//dlgFont.draw(dlgSpriteBatch, "", x_spalte9, eintragYStart+10);
				
				for(int i=istart;i<=ilast;i++)
				{
					//Count
					CStatisticsData_Other obj = otherStatistics.get(i);
					
					dlgFont.draw(dlgSpriteBatch, String.format("%04d", obj.day), eintragX, eintragYStart - count*eintragH-eintragH);
					dlgFont.draw(dlgSpriteBatch, "" + (obj.energyOutput-obj.energyConsumption), x_spalte2, eintragYStart - count*eintragH-eintragH);
					dlgFont.draw(dlgSpriteBatch, "" + (obj.waterOutput-obj.waterConsumption), x_spalte3, eintragYStart - count*eintragH-eintragH);
					dlgFont.draw(dlgSpriteBatch, "" + (obj.countAddressAll+"/"+obj.countAddressResidential+"/"+obj.countAddressPublic), x_spalte4, eintragYStart - count*eintragH-eintragH);
					dlgFont.draw(dlgSpriteBatch, "" + (obj.countCompanies+"/"+obj.countWorkerAll+"/"+obj.countWorkplacesAll), x_spalte5, eintragYStart - count*eintragH-eintragH);
					dlgFont.draw(dlgSpriteBatch, "" + (obj.countRoad+"/"+obj.countFootpath), x_spalte6, eintragYStart - count*eintragH-eintragH);
					dlgFont.draw(dlgSpriteBatch, "" + (obj.countVehiclesPublic+"/"+obj.countVehiclesPrivate), x_spalte7, eintragYStart - count*eintragH-eintragH);
					//dlgFont.draw(dlgSpriteBatch, "" + , x_spalte8, eintragYStart - count*eintragH-eintragH);
					//dlgFont.draw(dlgSpriteBatch, "" + , x_spalte9, eintragYStart - count*eintragH-eintragH);
					
					count++;
				}
				
				//Show Tooltip
				if(overX>0)
				{
					dlgSpriteBatch.end();
					tooltip.textLines.clear();
					if(overX>x_spalte2 && overX<x_spalte3-10)
						tooltip.textLines.add("Energy Output and Consumption");	
					if(overX>x_spalte3 && overX<x_spalte4-10)
						tooltip.textLines.add("Water Output and Consumption");	
					if(overX>x_spalte4 && overX<x_spalte5-10)
						tooltip.textLines.add("All Addresses / Residential / Public and Commercial");	
					if(overX>x_spalte5 && overX<x_spalte6-10)
						tooltip.textLines.add("Companies / Workers / Workplaces");	
					if(overX>x_spalte6 && overX<x_spalte7-10)
						tooltip.textLines.add("Parts of Road / Footpath");
					if(overX>x_spalte7 && overX<x_spalte7+50)
						tooltip.textLines.add("Public Services Vehicles / Private Vehicles");	
					
					if(tooltip.textLines.size()>0)
						tooltip.draw(overX, overY);
					
					dlgSpriteBatch.begin();
				}
			}
			
			
			dlgSpriteBatch.end();
			
			
			
			
			
			
			
			
			//pointsControl.setValue(obj.thehuman.getEducationValue());
			//pointsControl.setPosition(x_spalte4+27-45, eintragYStart-count*eintragH-eintragH-12);
			//pointsControl.render(0,0);
			//dlgSpriteBatch.begin();
			
			//			dlgSpriteBatch.end();
			//			pointsControl.setValue(obj.thehuman.getEducationValue());
			//			pointsControl.setPosition(x_spalte4+27, eintragYStart-count*eintragH-eintragH-12);
			//			pointsControl.render(0,0);
			//			dlgSpriteBatch.begin();
			
						//Draw Chosen Entry Check
			//			if(bChosen)
			//			{
			//				dlgSpriteBatch.draw(control_hakenok.textureImage, eintragX-25, eintragYStart-(iChosen+1)*eintragH-eintragH/1.5f, 15, 15);
			//			}
			
		//buttonOK.render(x, libgdxy);
		buttonCancel.render(x, libgdxy);
		
		int size=0;
		if(listType==ListTypeStatistics.STATISTICS_FINANCE)
			size=financeStatistics.size();
		if(listType==ListTypeStatistics.STATISTICS_POPULATION_ATTRIBUTES || listType==ListTypeStatistics.STATISTICS_POPULATION_COUNT)
			size=populationStatistics.size();
		if(listType==ListTypeStatistics.STATISTICS_OTHER)
			size=otherStatistics.size();
		
		if(size > pageSize)
		{
			buttonPagingLeft.render(x, libgdxy);
			buttonPagingRight.render(x, libgdxy);
		}
		
		}
		catch(Exception ex)
		{
			CHelper.writeError(ex.getMessage(), ex.getStackTrace(), ex);
		}
	}
}

