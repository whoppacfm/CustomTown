package com.mygdx.game;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class CGuiDialog {
	private Boolean bShow; 
	
	protected int dlgX;
	protected int dlgY;
	protected int dialogW;
	protected int dialogH;	
	
	public Boolean bCloseOnEscape;
	
	public Boolean bClickOK;
	public Boolean bMouseOver;
	protected CTown town;
	
	protected SpriteBatch dlgSpriteBatch;
	protected ShapeRenderer dlgShapeRenderer;
	protected BitmapFont dlgFont;
	public int dlgId;
	public String dlgName;
	
	Random rand;
	protected BitmapFont dlgFont2;
	
	public CGuiDialog(String name)
	{
		bCloseOnEscape=true;
		bClickOK=false;
		dlgName=name;
		bShow=false;
		rand=new Random();
		dlgId=rand.nextInt(1000);
	}
	
	public void setMiddlePosition()
	{
		dlgX=Gdx.graphics.getWidth()/2-dialogW/2;
		//dlgY=Gdx.graphics.getHeight()/2-dialogH/2;
		//if(Gdx.graphics.getWidth()>1200)
		
		dlgY=(int)(Gdx.graphics.getHeight()/1.7f-dialogH/2);
		int min1=Gdx.graphics.getHeight()-dialogH-50;
		
		if(dlgY>min1)
			dlgY=min1;
		
		//Gdx.app.debug("", ""+Gdx.graphics.getHeight());
		
		//if(Gdx.graphics.getHeight()<768)
		//	dlgY=min1;
	}
	
	public void setUpperMiddlePosition()
	{
		dlgX=Gdx.graphics.getWidth()/2-dialogW/2;
		//dlgY=Gdx.graphics.getHeight()/2-dialogH/2;
		//if(Gdx.graphics.getWidth()>1200)
		dlgY=(int) (Gdx.graphics.getHeight()/1.3f-dialogH/2);
	}
	
	public void setMiddlePositionHorz()
	{
		dlgX=Gdx.graphics.getWidth()/2-dialogW/2;
		//Gdx.app.debug("", "dialogW: " + dialogW + ", width: " + Gdx.graphics.getWidth() + ", dlgX: " + dlgX);
	}
	
	public void closeDlgOK()
	{
		buttonDown(0, 0, 0, -99);
	}
	
	public void render(int x, int libgdxy)
	{
		return;
	}
	
	public Boolean keyTyped(char character)
	{
		return false;
	}
	
	public Boolean doubleClick(int x, int y, int libgdxy, int button)
	{
		return false;
	}
		
	public Boolean buttonDown(int x, int y, int libgdxy, int button)
	{
		return false;
	}
	
	public void buttonUp()
	{
		return;
	}
	
	public Boolean mouseMovedDrag(int x, int y, int libgdxy)
	{
		return false;
	}
		
	public void showDlg(Boolean show)
	{
		bClickOK=false;
		bShow=show;
	}
	
	public Boolean dlgShowing()
	{
		return bShow;
	}	
}
