package com.mygdx.game;

import java.util.ArrayList;

import org.omg.PortableServer.ServantRetentionPolicyValue;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class CGuiControl_Button {

	public enum ButtonType { DEFAULT, IMAGE, TOGGLE_IMAGE, CHECKBOX }
	
	ButtonType buttonType;
	Texture buttonTexture;
	Boolean toggleActive;
	int toggleStyle;
	private Boolean bChecked;
	int renderMode;
	int controlX;
	float controlY;
	int controlX2;
	int controlY2;
	Boolean duplicated;
	int controlW;
	int controlH;
	int controlW2;
	int controlH2;
	int tooltipx;
	int tooltipy;
	int textX;
	int textY;
	int imageButtonType=0;
	String labelText;
	SpriteBatch ctrlSpriteBatch;
	ShapeRenderer ctrlShapeRenderer;
	BitmapFont ctrlFont;
	Color buttonColor;
	CGuiTooltip tooltip;
	public Boolean isShowing;
	private Boolean renderTooltip;
	CTown town;
	Boolean bClick;
	float ctrlFontSize;	
	Color ctrlfontcolor;
	
	

	public void setValues(int x, int y, int w, int h, int tx, int ty, String label, BitmapFont font, Texture image, ButtonType bt, CTown town) {
		ctrlfontcolor=new Color(0.83f,0.83f,0.83f,0.8f); //town.dialogFontColorList;
		ctrlFontSize=1f;
		this.town=town;
		buttonType = bt;
		buttonTexture = image;
		toggleActive=false;
		isShowing=false;
		tooltip = new CGuiTooltip(town);
		renderTooltip=true;
		bChecked=true;
		toggleStyle=1;
		renderMode=0; //Default render und Toggle render in der gleichen funktion, 1: getrennt in renderToggle
		
		duplicated=false;
		controlX=x;
		controlY=y;
		controlX2=-1;
		controlY2=-1;
		controlW=w;
		controlH=h;
		
		controlW2=w;
		controlH2=h;
		textX=tx;
		textY=ty;
		
		bClick=false;
		
		buttonColor = new Color(0.9f, 0.9f, 0.9f, 0.9f);
		
		labelText=label;
		ctrlShapeRenderer= town.gameGui.shapeRenderer;
		ctrlSpriteBatch= town.gameGui.editorSpriteBatch;
		ctrlFont = font;
		
		if(buttonType==ButtonType.CHECKBOX)
		{
			controlW=15;
			controlH=15;
		}
	}
	
	public CGuiControl_Button(int x, int y, int w, int h, int tx, int ty, String label, BitmapFont font, float fontsize, Color fontcolor, Texture image, ButtonType bt, CTown town) {
		setValues(x, y, w, h, tx, ty, label, font, image, bt, town);
		ctrlfontcolor=fontcolor;
		ctrlFontSize=fontsize;
		ctrlFont = font;
	}
	
	public CGuiControl_Button(int x, int y, int w, int h, int tx, int ty, String label, BitmapFont font, Texture image, ButtonType bt, CTown town)
	{
		setValues(x, y, w, h, tx, ty, label, font, image, bt, town);
	}
	
	public void enableTooltip(Boolean enable)
	{
		renderTooltip=enable;
	}
	
	public void setDuplicate(Boolean dupl)
	{
		duplicated=dupl;
	}
	
	public void setPosition(int x, float y)
	{
		controlX=x;
		controlY=y;
	}
	
	public void setDuplicatePosition(int x, int y)
	{
		controlX2=x;
		controlY2=y;
	}
	
	public void setDuplicateSize(int w, int h)
	{
		controlW2=w;
		controlH2=h;
	}
	
	public Boolean buttonClick(int x, int libgdxy)
	{
		//if(tooltip!=null && tooltip.textLines!=null && tooltip.textLines.size()>0 && tooltip.textLines.get(0).toLowerCase().contains("remove cook"))
		//	Gdx.app.debug("", "buttonclick 1");
		
		if(x>controlX && x<controlX+controlW && libgdxy>controlY && libgdxy<controlY+controlH)
		{
			bClick=true;
			toggleActive=!toggleActive;
			return true;
		}
		
		if(duplicated)
		{
			//Gdx.app.debug("", "x: " + x + ", cx: " + controlX2 + ", cw: " + controlW2 + ", y: " + libgdxy + ", cy: " + controlY2 + ", ch: " + controlH2);
			if(x>controlX2 && x<controlX2+controlW2 && libgdxy>controlY2 && libgdxy<controlY2+controlH2)
			{
				bClick=true;
				toggleActive=!toggleActive;
				return true;
			}
		}
		
		return false;
	}

	public void renderToggle(int x, int libgdxy)
	{
		//render Toggle Markierungsrahmen
		
		renderTogglePos(x, libgdxy, controlX, controlY, controlW, controlH);
		
		if(duplicated)
			renderTogglePos(x, libgdxy, controlX2, controlY2, controlW2, controlH2);
	}
	
	public void renderTogglePos(int x, int libgdxy, int posx, float posy, int w, int h)
	{
		//render Toggle Markierungsrahmen
		
		int cw=w;
		int ch=h;
		int cx=posx;
		float cy=posy;
		
		//if(!Gdx.gl.glIsEnabled(GL30.GL_BLEND))
		{
			Gdx.gl.glEnable(GL30.GL_BLEND);
			Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
		}
		ctrlShapeRenderer.setAutoShapeType(true);
	    ctrlShapeRenderer.begin();
	    	ctrlShapeRenderer.set(ShapeType.Line);
	    	
	    	if(buttonType==ButtonType.TOGGLE_IMAGE)
	    	{
		    	if(x>posx && x<posx+w && libgdxy>posy && libgdxy<posy+h)
		    	{
		    		cw+=2;
		    		ch+=2;
		    		cx-=1;
		    		cy-=1;
		    	}
		    	
		    	if(toggleActive)
		    	{
		    		if(toggleStyle==0) //Default
		    		{
		    			ctrlShapeRenderer.set(ShapeType.Filled);
			    		ctrlShapeRenderer.setColor(0.6f, 0.6f, 0.6f, 0.7f);
			    		ctrlShapeRenderer.rect(cx-2, cy-2, cw+5, ch+5);
			    		ctrlShapeRenderer.set(ShapeType.Line);
			    		ctrlShapeRenderer.setColor(0.6f, 0.6f, 0.6f, 0.4f);
			    		ctrlShapeRenderer.rect(cx-2, cy-2, cw+5, ch+5);		    		
		    		}
		    		else if(toggleStyle==1) //stärker markiert
		    		{
			    		ctrlShapeRenderer.set(ShapeType.Line);
			    		ctrlShapeRenderer.setColor(1f, 1f, 1f, 1f);
			    		ctrlShapeRenderer.rect(cx-1, cy-1, cw+2, ch+2);
			    		ctrlShapeRenderer.setColor(0.5f, 0.5f, 0.5f, 1f);
			    		ctrlShapeRenderer.rect(cx, cy, cw+2, ch+2);
			    		ctrlShapeRenderer.setColor(0.5f, 0.5f, 0.5f, 1f);
			    		ctrlShapeRenderer.rect(cx-2, cy-2, cw+5, ch+5);
		    		}
		    		else if(toggleStyle==2) //mehr platz zum image
		    		{
			    		ctrlShapeRenderer.set(ShapeType.Line);
			    		ctrlShapeRenderer.setColor(1f, 1f, 1f, 1f);
			    		ctrlShapeRenderer.rect(cx-3, cy-3, cw+6, ch+6);

			    		ctrlShapeRenderer.setColor(0.5f, 0.5f, 0.5f, 1f);
			    		ctrlShapeRenderer.rect(cx-2, cy-2, cw+6, ch+6);
			    		
			    		ctrlShapeRenderer.setColor(0.5f, 0.5f, 0.5f, 1f);
			    		ctrlShapeRenderer.rect(cx-4, cy-4, cw+9, ch+9);
		    		}		    		
		    	}
	    	}
	    
		ctrlShapeRenderer.end();
		//Gdx.gl.glDisable(GL30.GL_BLEND);
	}
	
	public void setColor(Color col)
	{
		buttonColor=col;
	}
	
	public void setTooltip(ArrayList<String> text)
	{
		tooltip.textLines = text;
	}
	
	public void setTooltip(String text)
	{
		tooltip.textLines.clear();
		tooltip.textLines.add(text);
	}
	
	public void setTooltipPosition(int x, int y)
	{
		tooltipx=x;
		tooltipy=y;
	}
	
	public void setTooltip(String text, String text2)
	{
		tooltip.textLines.clear();
		tooltip.textLines.add(text);
		tooltip.textLines.add(text2);
	}
	
	public void setTooltip(String text, String text2, String text3)
	{
		tooltip.textLines.clear();
		tooltip.textLines.add(text);
		tooltip.textLines.add(text2);
		tooltip.textLines.add(text3);
	}
	
	public void render(int x, int libgdxy)
	{
		renderPos(x, libgdxy, controlX, controlY, controlW, controlH, false);
		
		if(duplicated)
		{
			renderPos(x, libgdxy, controlX2, controlY2, controlW2, controlH2, true);
		}
	}
	
	public void renderDuplicateOnly(int x, int libgdxy)
	{
		renderPos(x, libgdxy, controlX2, controlY2, controlW2, controlH2, true);
	}
	
	public void renderDuplicateTooltip(int x, int libgdxy)
	{
		renderTooltip(x, libgdxy);	
	}
	
	public void renderPos(int x, int libgdxy, int posx, float posy, int w, int h, Boolean renderBackground)
	{
		

		town.gameGui.bButtonMouseover=false;
		
		int cw=w;
		int ch=h;
		int cx=posx;
		float cy=posy;
		
		controlX=posx;
		controlY=posy;
		controlW=w;
		controlH=h;
		
		isShowing=true;
		//if(!Gdx.gl.glIsEnabled(GL30.GL_BLEND))
		{
			Gdx.gl.glEnable(GL30.GL_BLEND);
			Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
		}	   
		
		ctrlShapeRenderer.setAutoShapeType(true);
	    ctrlShapeRenderer.begin();
	    	
	    	if(renderBackground)
	    	{
	    		ctrlShapeRenderer.set(ShapeType.Filled);
	    		ctrlShapeRenderer.setColor(0.1f, 0.1f, 0.1f, 0.5f);
	    		ctrlShapeRenderer.rect(cx-2, cy-2, cw+4, ch+4);
	    		ctrlShapeRenderer.set(ShapeType.Line);
	    		ctrlShapeRenderer.setColor(0.8f, 0.8f, 0.8f, 0.7f);
	    		ctrlShapeRenderer.rect(cx-2, cy-2, cw+4, ch+4);
	    	}
	    	
	    	ctrlShapeRenderer.set(ShapeType.Line);
	    	
	    	if(buttonType==ButtonType.DEFAULT)
	    	{
		    	ctrlShapeRenderer.setColor(0.7f, 0.7f, 0.7f, 0.8f);
		    	
		    	//Mouseover
		    	if(x>posx && x<posx+w && libgdxy>posy && libgdxy<posy+h)
		    	{
		    		ctrlShapeRenderer.setColor(0.9f, 0.9f, 0.9f, 0.9f);
		    	}
		    	
		    	ctrlShapeRenderer.rect(posx, posy, w, h);
	    	}
	    	
	    	if(buttonType==ButtonType.CHECKBOX)
	    	{
		    	ctrlShapeRenderer.setColor(0.7f, 0.7f, 0.7f, 0.8f);
		    	
		    	//Mouseover
		    	if(x>posx && x<posx+w && libgdxy>posy && libgdxy<posy+h)
		    		ctrlShapeRenderer.setColor(0.9f, 0.9f, 0.9f, 0.9f);
		    	
		    	ctrlShapeRenderer.rect(posx, posy, w, h);
	    	}
	    	
	    	if(buttonType==ButtonType.IMAGE)
	    	{
	    		//Mouseover
		    	if(x>posx && x<posx+w && libgdxy>posy && libgdxy<posy+h)
		    	{
					//		    		cw+=2;
					//		    		ch+=2;
					//		    		cx-=1;
					//		    		cy-=1;
		    		
		    		cw+=4;
		    		ch+=4;
		    		cx-=2;
		    		cy-=2;
		    	}
		    	
		    	if(bClick)
		    	{
		    		cw+=6;
		    		ch+=6;
		    		cx-=3;
		    		cy-=3;
		    		
		    		bClick=false;
		    	}
	    	}
	    	
	    	if(buttonType==ButtonType.TOGGLE_IMAGE)
	    	{
	    		//Mouseover
		    	if(x>posx && x<posx+w && libgdxy>posy && libgdxy<posy+h)
		    	{
		    		town.gameGui.bButtonMouseover=true;
		    		
		    		cw+=2;
		    		ch+=2;
		    		cx-=1;
		    		cy-=1;
		    	}
		    	
	    		if(renderMode==0)
	    		{
	    			ctrlShapeRenderer.end();
	    			renderTogglePos(x, libgdxy, posx, posy, w, h);
	    			ctrlShapeRenderer.begin();
	    		}
	    	}
	    
		ctrlShapeRenderer.end();
		//Gdx.gl.glDisable(GL30.GL_BLEND);
		
		try{
			if(!ctrlSpriteBatch.isDrawing())
				ctrlSpriteBatch.begin();
		}catch(Exception ex) {
			Gdx.app.debug("", ""+ex.getMessage());
		}
		
			if(buttonType==ButtonType.IMAGE || buttonType==ButtonType.TOGGLE_IMAGE)
    		{
				
				//ctrlSpriteBatch.setColor(buttonColor.r, buttonColor.g, buttonColor.b, 0.3f);
				ctrlSpriteBatch.setColor(buttonColor.r, buttonColor.g, buttonColor.b, 0.5f);
				if(imageButtonType==1)
					ctrlSpriteBatch.draw(buttonTexture, cx-3, cy-3, cw+6, ch+6);
				else
					ctrlSpriteBatch.draw(buttonTexture, cx, cy, cw, ch);
				
				ctrlSpriteBatch.setColor(buttonColor.r, buttonColor.g, buttonColor.b, 0.6f);
				//ctrlSpriteBatch.setColor(buttonColor.r, buttonColor.g, buttonColor.b, 0.8f);
				if(imageButtonType==1)
					ctrlSpriteBatch.draw(buttonTexture, cx, cy, cw, ch);
				else
					ctrlSpriteBatch.draw(buttonTexture, cx+3, cy+3, cw-6, ch-6);
    		}
			
			if(buttonType==ButtonType.CHECKBOX)
    		{
				if(toggleActive)
				{
					ctrlSpriteBatch.setColor(buttonColor);
					ctrlSpriteBatch.draw(town.gameResourceConfig.textures.get("control_hakenok"), cx-5, cy, cw+10, ch+5);
				}
				
				if(labelText!="")
				{
					ctrlFont.getData().setScale(ctrlFontSize);
					ctrlFont.setColor(ctrlfontcolor);
					ctrlFont.draw(ctrlSpriteBatch, labelText, posx+textX, posy+textY);
				}
    		}
			
			if(buttonType==ButtonType.DEFAULT)
    		{
				ctrlFont.getData().setScale(ctrlFontSize);
				ctrlFont.setColor(ctrlfontcolor);
				ctrlFont.draw(ctrlSpriteBatch, labelText, posx+textX, posy+textY);
    		}
			
		ctrlSpriteBatch.end();
		
		//Tooltip
		if(renderTooltip)
		{
			if(tooltip.textLines.size()>0)
			{
		    	if(x>posx && x<posx+w && libgdxy>posy && libgdxy<posy+h)
		    	{
		    		town.gameGui.bButtonMouseover=true;
		    		tooltip.draw(x+tooltipx, libgdxy-20+tooltipy);
		    	}
			}
		}
	}
	
	public void renderTooltip(int x, int libgdxy)
	{
		
		if(tooltip.textLines.size()>0)
		{
	    	if(x>controlX2 && x<controlX2+controlW2 && libgdxy>controlY2 && libgdxy<controlY2+controlH2)
	    	{
	    		town.gameWorld.bObjectInfoTooltipIsRendering=true;
	    		tooltip.draw(x+tooltipx, libgdxy-20+tooltipy);
	    	}
		}		
		
		if(tooltip.textLines.size()>0)
		{
	    	if(x>controlX && x<controlX+controlW && libgdxy>controlY && libgdxy<controlY+controlH)
	    	{
	    		town.gameWorld.bObjectInfoTooltipIsRendering=true;
	    		tooltip.draw(x+tooltipx, libgdxy-20+tooltipy);
	    	}
		}
	}

	
}

