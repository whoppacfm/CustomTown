package com.mygdx.game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.files.FileHandle;


public class CConfigIni {
	
	public CTown town;
	public int screenWidth;
	public int screenHeight;
	public Boolean windowed;
	public Boolean displayaddressinput;
	public int soundeffectsvolume;
	public int soundambientvolume;
	public int soundmusicvolume;
	public int postprocessorvalue;
	public int framebuffervalue;
	public int mipmappingTree;
	public int mipmappingRoad;
	public int mipmappingGround;
	public int mipmappingOther;
	public int mipmappingFloor;	
	public int waterAnimation;
	public int lodValue;
	public int threadValue;
	public int default_worktime_from;
	public int default_worktime_to;
	public int default_worktime_from2;
	public int default_worktime_to2;
	DisplayMode[] displayModes;
	
	public void init(DisplayMode[] displayModes, CTown t)
	{
		town=t;
		this.displayModes=displayModes;
		screenWidth=-1;
		screenHeight=-1;
		windowed=false;
		displayaddressinput=false;
		soundeffectsvolume=100;
		soundambientvolume=100;
		soundmusicvolume=0;
		postprocessorvalue=1;
		framebuffervalue=0;
		mipmappingTree=0;
		mipmappingRoad=0;
		mipmappingGround=0;
		mipmappingOther=0;
		mipmappingFloor=0;
		lodValue=0;
		threadValue=0;
		waterAnimation=0;
		
		default_worktime_from=8;
		default_worktime_to=12;
		default_worktime_from2=14;
		default_worktime_to2=18;
		
		readIni();
		
		if(default_worktime_from==-1 || default_worktime_to==-1)
		{
			default_worktime_from=8;
			default_worktime_to=12;
			default_worktime_from2=14;
			default_worktime_to2=19;
			
			writeIni();
		}
		
		//Auflösung initial setzen
		if(screenWidth==-1)
		{
			screenWidth=1024;
			screenHeight=768;
			windowed=true;
			
			writeIni();
		}
	}
	
	public void setDisplayMode(int width, int height, Boolean bwindowed)
	{
		windowed=bwindowed;
		screenWidth=width;
		screenHeight=height;
		
		//		if(bwindowed)
		//		{
		//			Gdx.graphics.setWindowedMode(width, height);
		//		}
		//		else
		//		{
		//			try
		//			{
		//				Gdx.graphics.setFullscreenMode(getDisplayMode(width, height));
		//			}
		//			catch(Exception ex)
		//			{
		//				Gdx.graphics.setWindowedMode(width, height);
		//				windowed=true;
		//				bwindowed=windowed;
		//			}
		//		}
		
	    if(bwindowed)
	    {
	    	Boolean bRet = Gdx.graphics.setWindowedMode(width, height);
	    }
	    else
	    {
	    	DisplayMode mode = getDisplayMode(width, height);
	    	if(mode==null)
	    		mode=displayModes[0];
	    	Boolean bRet = Gdx.graphics.setFullscreenMode(mode);
	    	if(!bRet)
	    	{
	    		Gdx.graphics.setWindowedMode(640, 480);
		    	//if(mode==null)
		    	//	mode=displayModes[0];
		    	//Gdx.graphics.setFullscreenMode(mode);
	    	}
	    }
	}
	
	public DisplayMode getDisplayMode(int w, int h)
	{
		//for(DisplayMode mode : displayModes)
		//	Gdx.app.debug("", "bitsPerPixel: "+mode.bitsPerPixel + ", refreshRate:"+mode.refreshRate);
		
		for(DisplayMode mode : displayModes)
		{
			if(mode.width==w && mode.height==h)
				return mode;
		}
			
			
		return null;
	}
	
	public void readIni()
	{
		try
		{
			FileHandle file=null;
			
			try
			{
				//file = Gdx.files.internal("data/config/config.ini");
				file = CHelper.getFileHandle("appdata/local/HTP/data/config/config.ini"); // Gdx.files.internal("data/config/config.ini");
				//-> C:\Users\User\appdata\local\HTP
			}
			catch(Exception ex)
			{
				return;
			}
			
			if(file==null)
				return;
			
			if(!file.exists())
				return;
			
			BufferedReader reader = new BufferedReader(file.reader());
			String line = reader.readLine();
			
			while( line != null ) 
			{
				if(line.isEmpty() || line.contains("//") || !line.contains("="))
				{
					line = reader.readLine();
					continue;
				}
				
				String split[] = line.split("=");
				
				if(split==null || split.length!=2)
				{
					line = reader.readLine();
					continue;					
				}
				
				if(split[0].toLowerCase().equals("screenwidth"))
					screenWidth=Integer.parseInt(split[1]);
				
				if(split[0].toLowerCase().equals("screenheight"))
					screenHeight=Integer.parseInt(split[1]);
				
				if(split[0].toLowerCase().equals("windowed"))
					windowed=Boolean.parseBoolean(split[1]);

				if(split[0].toLowerCase().equals("displayaddressinput"))
					displayaddressinput=Boolean.parseBoolean(split[1]);
				
				if(split[0].toLowerCase().equals("soundeffectsvolume"))
					soundeffectsvolume=Integer.parseInt(split[1]);
				
				if(split[0].toLowerCase().equals("soundambientvolume"))
					soundambientvolume=Integer.parseInt(split[1]);

				if(split[0].toLowerCase().equals("soundmusicvolume"))
					soundmusicvolume=Integer.parseInt(split[1]);

				
				if(split[0].toLowerCase().equals("postprocesservalue"))
					postprocessorvalue=Integer.parseInt(split[1]);
				
				if(split[0].toLowerCase().equals("framebuffervalue"))
					framebuffervalue=Integer.parseInt(split[1]);
				
				if(split[0].toLowerCase().equals("mipmappingtree"))
					mipmappingTree=Integer.parseInt(split[1]);
				
				if(split[0].toLowerCase().equals("mipmappingroad"))
					mipmappingRoad=Integer.parseInt(split[1]);
				
				if(split[0].toLowerCase().equals("mipmappingground"))
					mipmappingGround=Integer.parseInt(split[1]);

				if(split[0].toLowerCase().equals("mipmappingother"))
					mipmappingOther=Integer.parseInt(split[1]);

				if(split[0].toLowerCase().equals("mipmappingfloor"))
					mipmappingFloor=Integer.parseInt(split[1]);

				if(split[0].toLowerCase().equals("lodvalue"))
				{
					lodValue=Integer.parseInt(split[1]);
					town.lodvalue=lodValue;
				}

				if(split[0].toLowerCase().equals("threadvalue"))
				{
					threadValue=Integer.parseInt(split[1]);
					town.nrthreads=threadValue;
				}
				
				if(split[0].toLowerCase().equals("default_worktime_from"))
					default_worktime_from=Integer.parseInt(split[1]);

				if(split[0].toLowerCase().equals("wateranimation"))
					waterAnimation=Integer.parseInt(split[1]);
				
				if(split[0].toLowerCase().equals("default_worktime_to"))
					default_worktime_to=Integer.parseInt(split[1]);
				
				if(split[0].toLowerCase().equals("default_worktime_from2"))
					default_worktime_from2=Integer.parseInt(split[1]);
				
				if(split[0].toLowerCase().equals("default_worktime_to2"))
					default_worktime_to2=Integer.parseInt(split[1]);
				
				line = reader.readLine();
			}
		}
		catch (Exception e) {
			CHelper.writeError(e.getMessage(), e.getStackTrace(), e);
		}
	}
	
	public void writeIni()
	{
		try
		{
			//FileHandle file = Gdx.files.local("data/config/config.ini");
			//FileHandle file = Gdx.files.external("HTP/data/config/config.ini");
			FileHandle file = CHelper.getFileHandle("appdata/local/HTP/data/config/config.ini");
			
			String ls = System.getProperty("line.separator");
			file.delete();
			
			BufferedWriter bw = new BufferedWriter(file.writer(false));
			
			if(file!=null && bw!=null)
			{
				bw.write("screenwidth="+screenWidth);
				bw.write(ls);
				
				bw.write("screenheight="+screenHeight);
				bw.write(ls);

				bw.write("windowed="+windowed);
				bw.write(ls);

				bw.write("displayaddressinput="+displayaddressinput);
				bw.write(ls);
				
				bw.write("soundeffectsvolume="+soundeffectsvolume);
				bw.write(ls);
				
				bw.write("soundambientvolume="+soundambientvolume);
				bw.write(ls);
				
				bw.write("soundmusicvolume="+soundmusicvolume);
				bw.write(ls);				
				
				bw.write("default_worktime_from="+default_worktime_from);
				bw.write(ls);
				
				bw.write("default_worktime_to="+default_worktime_to);
				bw.write(ls);
				
				bw.write("default_worktime_from2="+default_worktime_from2);
				bw.write(ls);
				
				bw.write("default_worktime_to2="+default_worktime_to2);
				bw.write(ls);				
				
				bw.write("postprocessorvalue="+postprocessorvalue);
				bw.write(ls);
				
				bw.write("framebuffervalue="+framebuffervalue);
				bw.write(ls);
				
				bw.write("mipmappingtree="+mipmappingTree);
				bw.write(ls);
				
				bw.write("mipmappingroad="+mipmappingRoad);
				bw.write(ls);
				
				bw.write("mipmappingGround="+mipmappingGround);
				bw.write(ls);

				bw.write("mipmappingOther="+mipmappingOther);
				bw.write(ls);

				bw.write("mipmappingFloor="+mipmappingFloor);
				bw.write(ls);

				bw.write("lodValue="+lodValue);
				bw.write(ls);
				
				bw.write("threadValue="+threadValue);
				bw.write(ls);

				bw.write("waterAnimation="+waterAnimation);
				bw.write(ls);
				
				bw.close();
			}
		}
		catch(Exception ex)
		{
			CHelper.writeError(ex.getMessage(), ex.getStackTrace(), ex);
		}
	}
	
}









