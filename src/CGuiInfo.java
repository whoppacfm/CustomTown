package com.mygdx.game;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;


public class CGuiInfo {

	Texture iconTexture;
	
	float controlX;
	float controlY;
	int iconSizeW;
	int iconSizeH;
	int tooltipMoveX;
	int tooltipMoveY;
	
	public float alphaval;
	String labelText;
	CGuiTooltip tooltip;
	
	SpriteBatch ctrlSpriteBatch;
	ShapeRenderer ctrlShapeRenderer;
	BitmapFont ctrlFont;
	
	Boolean beginSpriteBatch;
	CTown town;
	Boolean bShowing;
	
	Color color;
	
	public CGuiInfo(int x, int y, Texture texture, String label, String tooltipText, String tooltipText2, int w, int h, CTown town)
	{
		init(x, y, texture, label, tooltipText, w, h, town);
		tooltip.textLines.add(tooltipText2);
	}
	
	public CGuiInfo(int x, int y, Texture texture, String label, String tooltipText, int w, int h, CTown town)
	{
		init(x, y, texture, label, tooltipText, w, h, town);
	}
	
	private void init(int x, int y, Texture texture, String label, String tooltipText, int w, int h, CTown town)
	{
		controlX=x;
		controlY=y;
		iconSizeW=w;
		iconSizeH=h;
		iconTexture=texture;
		labelText=label;
		this.town=town;
		
		ctrlSpriteBatch = town.gameGui.editorSpriteBatch;
		ctrlShapeRenderer = town.gameGui.shapeRenderer;
		
		ctrlFont=new BitmapFont();
		
		tooltip=new CGuiTooltip(town,ctrlSpriteBatch,ctrlShapeRenderer);
		tooltip.textLines.add(tooltipText);
		
		bShowing=false;
		tooltipMoveX=0;
		tooltipMoveY=0;
		
		color=new Color(1,1,1,1);
		
		alphaval=0.7f;
		
		beginSpriteBatch=true;
		//		ctrlSpriteBatch=town.gameGui.editorSpriteBatch;
		//		ctrlShapeRenderer=town.gameGui.shapeRenderer;
		//		ctrlFont=town.gameGui.font;
		
	}
	
	public void render(int x, int libgdxy, float cx, float f)
	{
		controlX=cx;
		controlY=f;
		render(x, libgdxy);
	}
	
	public void setctrlSpriteBatch(SpriteBatch sb)
	{
		ctrlSpriteBatch=sb;
		beginSpriteBatch=false;
	}
	
	public void render(int x, int libgdxy)
	{
		bShowing=true;
		
		if(beginSpriteBatch)
			ctrlSpriteBatch.begin();
		
		if(!ctrlSpriteBatch.isDrawing())
			ctrlSpriteBatch.begin();
			
			//ctrlSpriteBatch.draw(iconTexture, controlX, controlY, iconSizeW, iconSizeH);
			
			ctrlSpriteBatch.setColor(color.r,color.g,color.b,0.3f);
			//ctrlSpriteBatch.setColor(0,0,1,0.3f);
			ctrlSpriteBatch.draw(iconTexture, controlX-2, controlY-2, iconSizeW+4, iconSizeH+4);
        	
			ctrlSpriteBatch.setColor(color.r,color.g,color.b,0.3f);
			//ctrlSpriteBatch.setColor(0,0,1,0.3f);
			ctrlSpriteBatch.draw(iconTexture, controlX, controlY, iconSizeW, iconSizeH);
        	
			ctrlSpriteBatch.setColor(color.r,color.g,color.b,alphaval);
			//ctrlSpriteBatch.setColor(0,0,1,alphaval);
			ctrlSpriteBatch.draw(iconTexture, controlX, controlY, iconSizeW, iconSizeH);
			
			if(!labelText.isEmpty())
				ctrlFont.draw(ctrlSpriteBatch, labelText, controlX+iconSizeW+10, controlY);
			
		if(beginSpriteBatch)
			ctrlSpriteBatch.end();
	}
	
	public void moveTooltipPosition(int x, int y)
	{
		tooltipMoveX=x;
		tooltipMoveY=y;
	}
	
	public void renderTooltip(int x, int libgdxy)
	{
		if(!bShowing)
			return;
		
		if(tooltip.textLines.size()>0)
		{
			if(x>=controlX && x<=controlX+iconSizeW && libgdxy>=controlY && libgdxy<=controlY+iconSizeH)
			{
				//tooltip.draw(x+20+tooltipMoveX, libgdxy+20+tooltipMoveY);
				//tooltip.drawFormat(x+20+tooltipMoveX, libgdxy+20+tooltipMoveY, 0);
				
				if(town.gameWorld.markerObject!=null && town.gameWorld.markerObject.worker!=null) 
				{
					if(Gdx.input.isKeyJustPressed(Keys.G))
					{
						town.gameCam.position.set(town.gameWorld.markerObject.worker.pos_x(), town.gameWorld.markerObject.worker.pos_y(), 0);
						town.gameWorld.bRenderFrameBuffer=true;
					}
					
					tooltip.draw(x+20+tooltipMoveX, libgdxy+20+tooltipMoveY-20, "(Press 'g' to center camera on worker)");
				}
			}
		}
		
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

}



