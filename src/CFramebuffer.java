package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

public class CFramebuffer {

	float frameBufferZoom=7.0f; //Framebuffer abschalten, wenn er ab gewissem zoom nicht mehr benötigt wird
    
    FrameBuffer fbo;
    SpriteBatch fboBatch;
    float tempZoom;
	//CWorld gameWorld;
    CTown town;
	public float zvalue;
	public Boolean bInit;
    
	public CFramebuffer(CTown t1)
	{
		bInit=false;
		town=t1;
		tempZoom=town.gameCam.zoom;
		zvalue=0.1f;
	}
	
    public void render_end()
    {
		if(zvalue==0)
			return;
    	
		fbo.end();
		fboBatch.begin();
		fboBatch.draw(fbo.getColorBufferTexture(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0f, 0f, 1, 1);
		fboBatch.end();
    }
    
	public void render_start()
	{
	  	//Framebuffer für Verpixelung
		//float zz = gameWorld.gameCamera.zoom;
		
		//		if(zz>8)
		//			zz=1f;
		//		else if(zz>7)
		//			zz=1.5f;
		//		else if(zz>6)
		//			zz=1.5f;
		//		else if(zz>5)
		//			zz=1.5f;
		//		else if(zz>4)
		//			zz=2f;
		//		else if(zz>3)
		//			zz=2;
		//		else if(zz>2)
		//			zz=2.5f;
		//		else if(zz>1)
		//			zz=2.5f;		
		//		else
		//			zz=2.5f;
		//		
		//		zz=1.6f;
		
		if(zvalue==0)
			return;
				
		if(tempZoom!=town.gameCam.zoom || bInit) //Framebuffer nur neu initialisieren, wenn gezoomt wurde
		{
			if(fbo!=null)
			{
				fbo.dispose();
				fbo=null;
				fboBatch.dispose();
				fboBatch=null;
				tempZoom=town.gameCam.zoom;
			}
		}
		
        if(fbo == null)
        {
        	fbo = new FrameBuffer(Pixmap.Format.RGB888, (int) (Gdx.graphics.getWidth() / zvalue), (int) (Gdx.graphics.getHeight() / zvalue), false);
        	fbo.getColorBufferTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        	//fbo.getColorBufferTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        }
        
        if(fboBatch == null)
    	   fboBatch = new SpriteBatch();
        
	   fbo.begin();
	   Gdx.gl.glViewport(0, 0, fbo.getWidth(), fbo.getHeight());
	   Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
	}
}
