package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class CGuiTooltip {

	CTown town;
	SpriteBatch spriteBatch;
	ShapeRenderer shapeRenderer;
	BitmapFont font;
	private float fontscale;
	private Color fontColor;
	public Boolean bFormatMenuItem;
	Boolean bFontBig;
	Boolean bFontMiddle;
	Boolean bFontMiddle2;
	
	public GlyphLayout layout;
	public ArrayList<String> textLines;
	
	public CGuiTooltip(CTown t, SpriteBatch sb, ShapeRenderer sr)
	{
		town=t;
		font=town.gameGui.font;// gameResourceConfig.bfArial;
		layout=new GlyphLayout();
		spriteBatch=sb;
		shapeRenderer=sr;
		
		textLines=new ArrayList<String>();
		fontColor=new Color(0.9f,0.9f,0.9f,0.9f);
		fontscale=1f;
		bFontBig=false;
		bFontMiddle=false;
		bFontMiddle2=false;
	}
	

	public CGuiTooltip(CTown t)
	{
		town=t;
		font=town.gameGui.font;// gameResourceConfig.bfArial;
		layout=new GlyphLayout();
		try{
			spriteBatch=t.gameGui.editorSpriteBatch;
			shapeRenderer=t.gameGui.shapeRenderer;
		}catch(Exception e){}
		
		
		
		textLines=new ArrayList<String>();
		fontColor=new Color(0.9f,0.9f,0.9f,0.9f);
		fontscale=1f;
		bFontBig=false;
		bFontMiddle=false;
		bFontMiddle2=false;
	}
	
	public void setFontBig()
	{
		font=town.gameFont.bfArial;
		bFontBig=true;
		fontscale=0.7f;
	}

	public void setFontMiddle()
	{
		font=town.gameFont.bfArial;
		bFontMiddle=true;
		fontscale=0.53f;
	}
	
	public void setFontMiddle2()
	{
		font=town.gameFont.bfArial;
		bFontMiddle2=true;
		fontscale=0.5f;
	}
	
	public void setFontSmall()
	{
		font=town.gameGui.font;
		bFontBig=false;
		fontscale=1f;
	}

	public void setFontScale(float scale)
	{
		fontscale=scale;
	}
	
	public void setColor(Color fontColor)
	{
		this.fontColor=fontColor;
	}
	
	public void draw(int x, int y, String text)
	{
		textLines.clear();
		textLines.add(text);
		draw(x, y);
	}
		
	public void draw_BuyMenu(int x, int y, int price)
	{
		float height=0;
		float width=0;
		
		float fbig=0.62f;
		float fsmall=0.58f;
		
		float fheight_big=0;
		float fheight_small=0;
		
		BitmapFont f1 = town.gameFont.bfArial2;	
		
		for(int i=0;i<textLines.size();i++)
		{
			f1.getData().setScale(fsmall);
			
			if(i==0 || i==textLines.size()-1)
				f1.getData().setScale(fbig);
			
			layout.setText(f1, textLines.get(i));
			
			if(i==0 || i==textLines.size()-1)
				fheight_big=layout.height;
			else 
				fheight_small=layout.height;
			
			if(layout.width>width)
				width=layout.width;
			
			height+=layout.height;
		}
		
		
		//Draw Background
		float ffactor=1.8f;

		if(!Gdx.gl.glIsEnabled(GL30.GL_BLEND))
		{
			Gdx.gl.glEnable(GL30.GL_BLEND);
			Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
		}
		shapeRenderer.setAutoShapeType(true);
		shapeRenderer.begin();
			shapeRenderer.setColor(0.1f, 0.1f, 0.1f, 0.8f);
			shapeRenderer.set(ShapeType.Filled);
			shapeRenderer.rect(x-1, y-height*ffactor+6, width+5, height*ffactor-3);
			shapeRenderer.set(ShapeType.Line);
			shapeRenderer.setColor(0.7f, 0.7f, 0.7f, 0.7f);
			shapeRenderer.rect(x-2, y-height*ffactor+5, width+7, height*ffactor-1);
		shapeRenderer.end();
		//Gdx.gl.glDisable(GL30.GL_BLEND);		
		
		
		//Draw Text
		spriteBatch.begin();
		float heightall=0;
		for(int i=0;i<textLines.size();i++)
		{
			//Small
			f1.getData().setScale(fsmall);
			f1.setColor(0.9f,0.9f, 0.9f, 0.75f);
			float yval = y-heightall*1.8f;
			
			//Big
			if(i==0 || i==textLines.size()-1)
			{
				if(price>-1 || i==0)
				{
					heightall+=fheight_big;
					//heightall+=3;
					f1.getData().setScale(fbig);
				}
				
				f1.setColor(1f, 1f, 1f, 0.85f);
				
				if(i==textLines.size()-1 && textLines.size()>1) //-1 -> farbe für letzte zeile nicht ändern
				{
					f1.setColor(1f,0.8f, 0f, 0.85f);
					if(town.gameWorld.townMoney<price)
						f1.setColor(1f,0, 0f, 0.85f);
					
					if(price<0)
						f1.setColor(0.9f,0.9f, 0.9f, 0.75f);						
				}
			}
			else
				heightall+=fheight_small;
			
			f1.draw(spriteBatch, textLines.get(i), x+2, yval);
		}
    	spriteBatch.end();
    	spriteBatch.setShader(null);
		
	}
		
	public void drawDirect(int x, int y, String text)
	{
		//Draw directly 1 Textline
		
		textLines.clear();
		textLines.add(text);
		
		float width=0;
		float height=0;
		float heightComplete=0;

		font.getData().setScale(fontscale);
		font.setColor(fontColor);	

		for(String str : textLines)
		{
			layout.setText(font, str);
			if(layout.width>width)
				width=layout.width;
		}
		
		height=layout.height;
		heightComplete=layout.height*1.8f*textLines.size();
		if(bFontBig)
			heightComplete=layout.height*1.7f*textLines.size();
		if(bFontMiddle)
		{
			if(textLines.size()>2)
				heightComplete=layout.height*1.8f*textLines.size();
			else
				heightComplete=layout.height*1.7f*textLines.size();
		}
		if(bFontMiddle2)
		{
			if(textLines.size()>2)
				heightComplete=layout.height*1.8f*textLines.size();
			else
				heightComplete=layout.height*1.7f*textLines.size();
		}

		if(!Gdx.gl.glIsEnabled(GL30.GL_BLEND))
		{
			Gdx.gl.glEnable(GL30.GL_BLEND);
			Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
		}
		
	    shapeRenderer.setAutoShapeType(true);
		shapeRenderer.begin();
			shapeRenderer.setColor(0.1f, 0.1f, 0.1f, 0.8f);
			shapeRenderer.set(ShapeType.Filled);
			shapeRenderer.rect(x-1, y-heightComplete+3+3, width+5, heightComplete-3);
			shapeRenderer.set(ShapeType.Line);
			shapeRenderer.setColor(0.7f, 0.7f, 0.7f, 0.7f);
			shapeRenderer.rect(x-1-1, y-heightComplete+2+3, width+5+2, heightComplete-3+2);
		shapeRenderer.end();
		//Gdx.gl.glDisable(GL30.GL_BLEND);		
		
		if(bFontBig || bFontMiddle || bFontMiddle2)
			spriteBatch.setShader(town.gameFont.fontShader);
		
		spriteBatch.begin();
		
		int count=0;
		for(String str : textLines)
		{
			font.draw(spriteBatch, str, x+2, y-count*height*1.8f);
			count++;
		}
    	spriteBatch.end();
    	spriteBatch.setShader(null);

	}
		
	public void drawFormat(int x, int y, int ihpos)
	{
		//	_ -> Big Font
		//  r_ -> Red
		//	g_ -> Green
		
		
		float height=0;
		float width=0;
		
		//float fbig=0.62f;
		//float fsmall=0.58f;
		float fbig=0.6f;
		float fsmall=0.52f;
				
		float fheight_big=0;
		float fheight_small=0;
		
		BitmapFont f1 = town.gameFont.bfArial2;	
		
		for(int i=0;i<textLines.size();i++)
		{
			String str = textLines.get(i);
			
			f1.getData().setScale(fsmall);
			
			if(str.startsWith("_"))
				f1.getData().setScale(fbig);
			
			layout.setText(f1, textLines.get(i));
			
			if(str.startsWith("_"))
				fheight_big=layout.height;
			else 
				fheight_small=layout.height;
			
			if(layout.width>width)
				width=layout.width;
			
			height+=layout.height;
		}
		
		if(ihpos==1)
		{
			x-=width/2;
		}
		
		//Draw Background
		float ffactor=1.8f;

		if(!Gdx.gl.glIsEnabled(GL30.GL_BLEND))
		{
			Gdx.gl.glEnable(GL30.GL_BLEND);
			Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
		}
		shapeRenderer.setAutoShapeType(true);
		shapeRenderer.begin();
			shapeRenderer.setColor(0.1f, 0.1f, 0.1f, 0.8f);
			shapeRenderer.set(ShapeType.Filled);
			shapeRenderer.rect(x-1, y-height*ffactor+6, width+5, height*ffactor-3);
			shapeRenderer.set(ShapeType.Line);
			shapeRenderer.setColor(0.7f, 0.7f, 0.7f, 0.7f);
			shapeRenderer.rect(x-2, y-height*ffactor+5, width+7, height*ffactor-1);
		shapeRenderer.end();
		//Gdx.gl.glDisable(GL30.GL_BLEND);		
		
		
		//Draw Text
		spriteBatch.begin();
		float heightall=0;
		for(int i=0;i<textLines.size();i++)
		{
			String str = textLines.get(i);
			
			//Small
			f1.getData().setScale(fsmall);
			f1.setColor(0.9f,0.9f, 0.9f, 0.75f);
			float yval = y-heightall*1.8f;
			
			//Big
			if(str.startsWith("_"))
			{
				heightall+=fheight_big;
				//heightall+=3;
				f1.getData().setScale(fbig);
				f1.setColor(1f, 1f, 1f, 0.85f);
				//if(i==textLines.size()-1)
				//{
				//	f1.setColor(1f,0.8f, 0f, 0.85f);
				//	if(town.gameWorld.townMoney<price)
				//		f1.setColor(1f,0, 0f, 0.85f);
				//}
			}
			else
				heightall+=fheight_small;
			
			if(str.startsWith("r_"))
				f1.setColor(1f, 0f, 0f, 0.85f);
			if(str.startsWith("g_"))
				f1.setColor(0f, 1f, 0f, 0.85f);
			
			if(str.startsWith("_"))
				str = str.substring(1); 
			if(str.startsWith("r_"))
				str = str.substring(2); 
			if(str.startsWith("g_"))
				str = str.substring(2); 
			
			f1.draw(spriteBatch, str, x+2, yval);
		}
    	spriteBatch.end();
    	spriteBatch.setShader(null);
		
	}
	
	public void draw(int x, int y)
	{
		float width=0;
		float height=0;
		float heightComplete=0;
		
		setFontMiddle();
		
		if(fontColor.r>0.7f && fontColor.g<0.3f && fontColor.b<0.3f)
			setColor(new Color(0.8f,0f,0f,0.9f));
		else
			setColor(new Color(0.9f,0.9f, 0.9f, 0.75f));
				
				
		font.getData().setScale(fontscale);
		font.setColor(fontColor);	
		
		for(String str : textLines)
		{
			layout.setText(font, str);
			
			if(layout.width>width)
				width=layout.width;
		}
		
		height=layout.height;
		heightComplete=layout.height*1.8f*textLines.size();
		if(bFontBig)
			heightComplete=layout.height*1.7f*textLines.size();
		if(bFontMiddle)
		{
			if(textLines.size()>2)
				heightComplete=layout.height*1.8f*textLines.size();
			else
				heightComplete=layout.height*1.7f*textLines.size();
		}
		if(bFontMiddle2)
		{
			if(textLines.size()>2)
				heightComplete=layout.height*1.8f*textLines.size();
			else
				heightComplete=layout.height*1.7f*textLines.size();
		}
		
		int deltaHeight=0;
		if(textLines.size()>1)
			deltaHeight=2;

		if(!Gdx.gl.glIsEnabled(GL30.GL_BLEND))
		{
			Gdx.gl.glEnable(GL30.GL_BLEND);
			Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
		}
	    shapeRenderer.setAutoShapeType(true);
		shapeRenderer.begin();
			shapeRenderer.setColor(0.1f, 0.1f, 0.1f, 0.8f);
			shapeRenderer.set(ShapeType.Filled);
			shapeRenderer.rect(x-1, y-heightComplete+3+3-deltaHeight, width+5, heightComplete-3+deltaHeight*2);
			shapeRenderer.set(ShapeType.Line);
			shapeRenderer.setColor(0.7f, 0.7f, 0.7f, 0.7f);
			shapeRenderer.rect(x-1-1, y-heightComplete+2+3-deltaHeight, width+5+2, heightComplete-3+2+deltaHeight*2);
		shapeRenderer.end();
		//Gdx.gl.glDisable(GL30.GL_BLEND);		
		
		if(bFontBig || bFontMiddle || bFontMiddle2)
			spriteBatch.setShader(town.gameFont.fontShader);
		
		spriteBatch.begin();
		
		int count=0;
		for(String str : textLines)
		{
			font.draw(spriteBatch, str, x+2, y-count*height*1.8f+1);
			count++;
		}
    	spriteBatch.end();
    	spriteBatch.setShader(null);
	}
	
}
