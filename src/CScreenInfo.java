package com.mygdx.game;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CScreenInfo {
    private BitmapFont font;
	SpriteBatch fpsbatch;
	FPSLogger fps; 
	//public CWorld gameWorld;
	public CTown town;
	
	public CScreenInfo(CTown t1)
	{
		town=t1;
	    font = new BitmapFont();
	    font.setColor(Color.WHITE);
	    
	    fpsbatch = t1.gameGui.editorSpriteBatch; 
	    
		fps = new FPSLogger();
		fps.log();
		
	    font = new BitmapFont();
	    font.setColor(Color.RED);		
	}
	
	public void renderFPS()
	{
	    fpsbatch.begin();
	    	if(town.gameWorld.town.bDevMode)
	    	{
	    		font.draw(fpsbatch, "DevMode", 20, 25+20);
	    	}
	    	
	    	if(town.gameWorld.town.bShowFPS)
			{
				int ifps = Gdx.graphics.getFramesPerSecond();
				//font.draw(fpsbatch, "DevMode", 20, 25+20);
				font.draw(fpsbatch, "FPS: " + ifps, 20, 25);
				//font.draw(fpsbatch, "Java Heap: " + Gdx.app.getJavaHeap(), 20+100, 25+20);
				//font.draw(fpsbatch, "Native Heap: " + Gdx.app.getNativeHeap(), 20+100, 25);
			}
		fpsbatch.end();
	}	
	
}
