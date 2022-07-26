package com.mygdx.game;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import javax.swing.JOptionPane;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Input.Keys;

//import com.codedisaster.steamworks.*;


public class htp extends ApplicationAdapter {
	
	public CTown town;
	
// 	private SteamUser user; 
// 	private SteamUserStats userStats; 
// 	private SteamRemoteStorage remoteStorage; 
// 	private SteamUGC ugc; 
// 	private SteamUtils utils; 
// 	private SteamApps apps; 
	
	public void create () {
		//TexturePacker.process(input, output, packFileName);
		//Gdx.app.setLogLevel(Application.LOG_ERROR);
		//Gdx.app.setLogLevel(Application.LOG_DEBUG);
		
		//Boolean bInitSteam=false;
		//if(!bInitSteam)
		//{
		//	Gdx.app.error("", "ACHTUNG STEAM INIT AKTIVIEREN");
		//	Gdx.app.setLogLevel(Application.LOG_DEBUG);
		//}
		//else
		Gdx.app.setLogLevel(Application.LOG_NONE);
		
		/*
		if(bInitSteam)
		{
			try {
			    if (!SteamAPI.init()) {
			        // Steamworks initialization error, e.g. Steam client not running
			    	CHelper.writeError("Steamworks initialization error, e.g. Steam client not running", null, null);
			    	JOptionPane.showMessageDialog(null, "Steamworks initialization error, e.g. Steam client not running", "Error", JOptionPane.OK_CANCEL_OPTION);
			    	Gdx.app.exit();
			    	return;
			    }
			} catch (SteamException e) {
			    // Error extracting or loading native libraries
				CHelper.writeError("Error extracting or loading native libraries: " + e.getMessage(), e.getStackTrace(), e);
				Gdx.app.exit();
				return;
			}
		}
		*/
		town = new CTown();
		town.init("veryeasy");
	}
	
	@Override
	public void render () {
		//if (SteamAPI.isSteamRunning()) {
		//	SteamAPI.runCallbacks();
		//}
		
		/*
		if(Gdx.input.isKeyJustPressed(Keys.K))
		{
			CWorld.pathmap=null;
			CWorld.pathmap_road=null;
			CWorld.pathmap_footpath=null;
			CWorld.pathmap_defensewall=null;
			CWorld.astar = null;
			CWorld.astar_road = null;
			CWorld.astar_footpath = null;			
			
			town.gameWorld=null;
			town.init("veryeasy");
			System.gc();
			/*
			town.gameWorld=null;
			System.gc();
			//town = new CTown();
			town.init("veryeasy");
			town.initStuff();
			town.sLoadSaveGame = "small_city_htp_1483708231979";
			town.bLoadingSaveGame=true;
			//town.gameWorld.loadFromFile("small_city_htp_1483708231979");
			
		}
		*/
					
		town.render();
	}
	
    @Override
    public void dispose() {
    	//apps.dispose();
    	//if (SteamAPI.isSteamRunning())
    	//	SteamAPI.shutdown();
    }

    @Override
    public void resize(int width, int height) {
    	town.resize(width, height);
    }
	
}


