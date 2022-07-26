package com.mygdx.game;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

public class CAudio {
	
	public static class CAudioEffect
	{
		String sAudioType;
		//Sound soundEffect;
		Music soundEffect;
		
		public CAudioEffect(String at, String file1)
		{
			sAudioType=at;
			//soundEffect=Gdx.audio.newSound(Gdx.files.internal(file1));
			soundEffect=Gdx.audio.newMusic(Gdx.files.internal(file1));
		}
	}
	
	public static class CAudioAmbient
	{
		public String sAudioType;
		public Music soundAmbient;
		public int totalSeconds;
		
		public CAudioAmbient(String at, String file1, int seconds)
		{
			sAudioType=at;
			totalSeconds=seconds;
			soundAmbient=Gdx.audio.newMusic(Gdx.files.internal(file1));
		}
	}
	
	CTown town;
	
	private float fmastervolumeeffects;
	private float fmastervolumeambient;
	private float fmastervolumemusic;
	
	//private Hashtable<String,Sound> sounds;
	//private Hashtable<String,Music> ambient;
	
//	public String[] arrAmbient = {
//			"ambient_day_birds1","ambient_day_birds2","ambient_day_birds3","ambient_day_birds4","ambient_day_birds5","ambient_day_birds6","ambient_day_birds7",
//			"ambient_night_cold1","ambient_night_cold2",
//			"ambient_night_warm1","ambient_night_warm2","ambient_night_warm3"
//			//"ambient_rain1","ambient_rain2"
//	};
	
//	int countAmbientDay=7;
//	int countAmbientNightCold=2;
//	int countAmbientNightWarm=2;
//	int countAmbientRain=2;
	
	Music rainAmbientSound1;
	Music rainAmbientSound2;
	Music music1;
	
	CAudioAmbient activeAmbientSound1;
	CAudioAmbient fadeoutambient;
	
	String activeAmbientString;
	
	float deltaTimerRain;
	float deltaTimerAmbient;
	
	int musicnr;
	
	
	
	
	
	
	
	public CAudio()
	{
		fadeoutambient=null;
		musicnr=0;
	}
	
	public float getVolumeAmbient()
	{
		return fmastervolumeambient;
	}
	
	public float getVolumeMusic()
	{
		return fmastervolumemusic;
	}
	
	public float getVolumeEffects()
	{
		return fmastervolumeeffects;
	}
	
	public void setVolumeAmbient_NoFadeIn(float fval)
	{
		fmastervolumeambient = fval;
		if(activeAmbientSound1!=null)
			activeAmbientSound1.soundAmbient.setVolume(fval);
	}
	
	public void setVolumeMusic(float fval)
	{
		fmastervolumemusic = fval;
		if(music1!=null)
			music1.setVolume(fval);
	}	
	
	public void setVolumeEffects(float fval)
	{
		fmastervolumeeffects = fval;
	}
	
	public Boolean soundIsPlaying(String ssound)
	{
		ssound=ssound.toLowerCase();
		
		CAudioEffect s1 = town.gameResourceConfig.listAudioEffect_ByType.get(ssound);
		return s1.soundEffect.isPlaying();
	}
	
	public void playSound(String ssound, float volumediff, Boolean bZoom)
	{
		if(fmastervolumeeffects==0)
			return;
		
		float vol = fmastervolumeeffects+volumediff;
		
		if(vol<0)
			vol=0.1f;
		
		if(bZoom && vol > 0.1f)
			vol -= town.gameCam.zoom/40;
		
		if(vol<0)
		{
			vol=0;
			return;
		}
		if(vol>1)
			vol=1;
		
		ssound=ssound.toLowerCase();
		
		//Sounds wenn notwendig asychron laden und zwischendurch bei Bedarf zwischenladen
		if(town.gameResourceConfig.listAudioEffect_ByType.containsKey(ssound))
		{
			CAudioEffect s1 = town.gameResourceConfig.listAudioEffect_ByType.get(ssound);
			if(!s1.soundEffect.isPlaying())
			{
				town.gameResourceConfig.listAudioEffect_ByType.get(ssound).soundEffect.setVolume(vol);
				try
				{
					town.gameResourceConfig.listAudioEffect_ByType.get(ssound).soundEffect.play();
				}
				catch(Exception e)
				{
					//..
				}					
			}
		}
	}
	
	public void stopSound(String ssound)
	{
		ssound=ssound.toLowerCase();
		town.gameResourceConfig.listAudioEffect_ByType.get(ssound).soundEffect.stop();
	}
	
	public float convertAmbientVolume_Rain(float val)
	{
		float frainvolumemax=val;
		frainvolumemax -= town.gameCam.zoom/40;
		if(frainvolumemax<0)
			frainvolumemax=0;
		frainvolumemax=frainvolumemax/6.5f;
		
		return frainvolumemax;
	}
	
	public float convertAmbientVolume_Ambient(float val)
	{
		float retval=val;
		
		retval -= town.gameCam.zoom/40;
		//Gdx.app.debug("", ""+town.gameCam.zoom);
		
		if(retval<0)
			retval=0;
		
		if(activeAmbientString.contains("night"))
		{
			float fmax=0.5f;
			if(activeAmbientString.contains("warm1"))
				fmax=0.7f;
			if(activeAmbientString.contains("warm2"))
				fmax=0.3f;
			
			retval=val-0.2f;
			if(retval>fmax)
				retval=fmax;
			if(retval<0f)
				retval=0f;
		}
		
		return retval;
	}
	
	public void playRain()
	{
		if(fmastervolumeambient==0)
		{
			if(rainAmbientSound1!=null && rainAmbientSound1.isPlaying())
				rainAmbientSound1.stop();
			if(rainAmbientSound2!=null && rainAmbientSound2.isPlaying())
				rainAmbientSound2.stop();
			
			return;
		}
		
		float frainvolumemax=0.15f;
		frainvolumemax=convertAmbientVolume_Rain(fmastervolumeambient); ///6.5f;
				
		//Gdx.app.debug("", ""+frainvolumemax);
				
		if(town.gameWorld.worldPause)
		{
			if(rainAmbientSound1!=null && rainAmbientSound1.isPlaying())
				rainAmbientSound1.pause();
			
			if(rainAmbientSound2!=null && rainAmbientSound2.isPlaying())
				rainAmbientSound2.pause();
			
			return;
		}
				
		if(town.gameWorld.worldWeather.bIsRaining)
		{
			deltaTimerRain+=Gdx.graphics.getDeltaTime(); //CHelper.getDeltaSeconds(town.gameWorld);
			
			if(deltaTimerRain>0.07f)
			{
				deltaTimerRain=0;
								
				//Fade In / Fade out
				if(rainAmbientSound1!=null && rainAmbientSound1.isPlaying())
				{
					if(rainAmbientSound1.getPosition()>7 && rainAmbientSound1.getVolume()>0)
					{
						if(rainAmbientSound1.getVolume()>0)
							rainAmbientSound1.setVolume(rainAmbientSound1.getVolume()-0.001f);
					}
					else if(rainAmbientSound1.getVolume()<frainvolumemax)
						rainAmbientSound1.setVolume(rainAmbientSound1.getVolume()+0.001f);
				}
				
				if(rainAmbientSound2!=null && rainAmbientSound2.isPlaying())
				{
					if(rainAmbientSound2.getPosition()>7)
					{
						if(rainAmbientSound2.getVolume()>0)
							rainAmbientSound2.setVolume(rainAmbientSound2.getVolume()-0.001f);
					}
					else if(rainAmbientSound2.getVolume()<frainvolumemax)
						rainAmbientSound2.setVolume(rainAmbientSound2.getVolume()+0.001f);
				}
			}
			
			if(rainAmbientSound1!=null && rainAmbientSound1.isPlaying())
			{
				if(rainAmbientSound1.getPosition()>6)
				{
					if(rainAmbientSound2!=null && !rainAmbientSound2.isPlaying())
					{
						//rainAmbientSound2=getRandomAmbientByType("rain");
						rainAmbientSound2.setVolume(0.01f);
						
						try
						{
							rainAmbientSound2.play();
							rainAmbientSound2.setLooping(true);
						}
						catch(Exception e)
						{
							rainAmbientSound2=null;
						}
					}
				}
			}
			
			if(rainAmbientSound1==null || !rainAmbientSound1.isPlaying())
			{
				//rainAmbientSound=getRandomAmbientByType("rain");
				setRainAmbientVars();
				if(rainAmbientSound1!=null)
				{
					rainAmbientSound1.setVolume(0.01f);
					
					try
					{
						rainAmbientSound1.play();
						rainAmbientSound1.setLooping(true);
					}
					catch(Exception e)
					{
						rainAmbientSound1=null;
					}
				}
			}
		}
		else
		{
			if(rainAmbientSound1!=null && rainAmbientSound1.isPlaying())
			{
				rainAmbientSound1.stop();
				rainAmbientSound1=null;
			}
			
			if(rainAmbientSound2!=null && rainAmbientSound2.isPlaying())
			{
				rainAmbientSound2.stop();
				rainAmbientSound2=null;
			}
		}
	}
	
	private void setRainAmbientVars()
	{
		if(town.gameResourceConfig.listAudioAmbient_Rain.size()<1)
			return;
		
		int index = town.rand.nextInt(town.gameResourceConfig.listAudioAmbient_Rain.size());
		int count=0;
		
		Object[] arr = town.gameResourceConfig.listAudioAmbient_Rain.values().toArray();
		Object[] arr2 = town.gameResourceConfig.listAudioAmbient_Rain2.values().toArray();
		
		for(int i=0;i<arr.length;i++)
		{
			if(i==index)
			{
				rainAmbientSound1 = ((CAudioAmbient)arr[i]).soundAmbient;
				rainAmbientSound2 = ((CAudioAmbient)arr2[i]).soundAmbient;
			}
		}
	}
	
	public CAudioAmbient getRandomAmbientByType(String stype)
	{
		Hashtable<String, CAudioAmbient> templist = null;
		Music tempmusic=null;
		
		if(stype.toLowerCase().contains("rain"))
			templist=town.gameResourceConfig.listAudioAmbient_Rain;
		if(stype.toLowerCase().contains("day"))
			templist=town.gameResourceConfig.listAudioAmbient_Day;
		if(stype.toLowerCase().contains("night_cold"))
			templist=town.gameResourceConfig.listAudioAmbient_NightCold;
		if(stype.toLowerCase().contains("night_warm"))
			templist=town.gameResourceConfig.listAudioAmbient_NightWarm;
		
		if(templist.size()<1)
			return null;
		
		int index = town.rand.nextInt(templist.size());
		int count=0;
		for(CAudioAmbient amb : templist.values())
		{
			if(count==index)
			{
				return amb;
			}
			count++;
		}
		
		return null;
	}
	
	public void playAmbient()
	{
		/*
		if(music1==null)
		{
			if(fmastervolumemusic>0)
			{
			int index1 = town.rand.nextInt(town.gameResourceConfig.listAudioMusic.size());
			String file1 = town.gameResourceConfig.listAudioMusic.get(index1);
			while(musicnr==0 && (file1.toLowerCase().contains("naraina") || file1.toLowerCase().contains("killers") || file1.toLowerCase().contains("saltyditty")))
			{
				index1 = town.rand.nextInt(town.gameResourceConfig.listAudioMusic.size());
				file1 = town.gameResourceConfig.listAudioMusic.get(index1);
			}
			music1 = Gdx.audio.newMusic(Gdx.files.internal(file1));
			fmastervolumemusic=town.gameConfigIni.soundmusicvolume;
			music1.play();
			music1.setVolume(fmastervolumemusic/100);
						
			if(musicnr==0)
				musicnr=1;
			}
		}
		else if(!music1.isPlaying())
		{
			music1.dispose();
			music1=null;
		}
		*/
				
		playRain();
		
		if(fmastervolumeambient==0)
		{
			if(activeAmbientSound1!=null && activeAmbientSound1.soundAmbient.isPlaying())
				activeAmbientSound1.soundAmbient.stop();
			
			if(fadeoutambient!=null && fadeoutambient.soundAmbient.isPlaying())
				fadeoutambient.soundAmbient.stop();
			
			return;
		}
		
		float fmastervolumeambientintern = convertAmbientVolume_Ambient(fmastervolumeambient);
		
		if(town.gameWorld.worldPause)
		{
			if(activeAmbientSound1!=null && activeAmbientSound1.soundAmbient.isPlaying())
				activeAmbientSound1.soundAmbient.pause();
			
			if(fadeoutambient!=null && fadeoutambient.soundAmbient.isPlaying())
				fadeoutambient.soundAmbient.pause();
			
			return;
		}
		
		deltaTimerAmbient+=Gdx.graphics.getDeltaTime(); //CHelper.getDeltaSeconds(town.gameWorld);
		
		Boolean bDay=false;
		if(town.gameWorld.worldTime.hours >= 5 && town.gameWorld.worldTime.hours <= 20)
			bDay=true;

		Boolean bNight=false;
		if(town.gameWorld.worldTime.hours == 0 || town.gameWorld.worldTime.hours == 1)
			bNight=true;
		
		Boolean bCold=false;
		if(town.gameWorld.worldTime.day>=10 || town.gameWorld.worldTime.day<=2)
			bCold=true;
		
		Boolean bCancel=false;
		if(bDay && !activeAmbientString.contains("day"))
			bCancel=true;
		if(!bDay && !activeAmbientString.contains("night"))
			bCancel=true;
		if(bNight && bCold && !activeAmbientString.contains("night_cold"))
			bCancel=true;
		if(bNight && !bCold && !activeAmbientString.contains("night_warm"))
			bCancel=true;
		
		fadeOutAmbient(bCancel);
		
		initActiveAmbient(bDay, bCold, bNight);
		
		fadeActiveAmbient(bCancel, fmastervolumeambientintern);
		
	}
	
	private void fadeOutAmbient(Boolean bCancel)
	{
		if(bCancel || (activeAmbientSound1!=null && activeAmbientSound1.soundAmbient.getPosition() > activeAmbientSound1.totalSeconds-7))
		{
			if(fadeoutambient==null)
			{
				fadeoutambient=activeAmbientSound1;
				activeAmbientSound1=null;
			}
		}
			
		if(fadeoutambient!=null && fadeoutambient.soundAmbient.isPlaying())
		{
			if(deltaTimerAmbient>0.14f)
			{
				if(fadeoutambient.soundAmbient.getVolume()>0.008f)
				{
					fadeoutambient.soundAmbient.setVolume(fadeoutambient.soundAmbient.getVolume()-0.008f);
				}
				else
				{
					fadeoutambient.soundAmbient.stop();
				}
			}
		}
		
		if(fadeoutambient!=null && !fadeoutambient.soundAmbient.isPlaying())
			fadeoutambient=null;
	}
	
	private void fadeActiveAmbient(Boolean bCancel, float fmastervolumeambientintern)
	{
		if(deltaTimerAmbient>0.14f)
		{
			if(!bCancel && activeAmbientSound1!=null && activeAmbientSound1.soundAmbient.getVolume()<fmastervolumeambientintern)
			{
				activeAmbientSound1.soundAmbient.setVolume(activeAmbientSound1.soundAmbient.getVolume()+0.01f);
			}
			
			if(!bCancel && activeAmbientSound1!=null && activeAmbientSound1.soundAmbient.getVolume()>fmastervolumeambientintern)
			{
				activeAmbientSound1.soundAmbient.setVolume(activeAmbientSound1.soundAmbient.getVolume()-0.01f);
			}			
			
			deltaTimerAmbient=0;
		}
	}
	
	private void initActiveAmbient(Boolean bDay, Boolean bCold, Boolean bNight)
	{
		if(activeAmbientSound1==null || !activeAmbientSound1.soundAmbient.isPlaying())
		{
			if(bDay)
			{
				activeAmbientString="ambient_day_birds";
				activeAmbientSound1=getRandomAmbientByType("day");
			}
			
			if(bCold && bNight)
			{
				activeAmbientString="ambient_night_cold";
				activeAmbientSound1=getRandomAmbientByType("night_cold");
			}
			
			if(!bCold && bNight)
			{
				activeAmbientString="ambient_night_warm";
				activeAmbientSound1=getRandomAmbientByType("night_warm");
			}
			
			if(activeAmbientSound1!=null)
			{
				try{
					activeAmbientSound1.soundAmbient.play(); // Unable to allocate audio buffers
					activeAmbientSound1.soundAmbient.setVolume(0.01f);
				}
				catch(Exception e)
				{
					activeAmbientSound1=null;
				}
			}
		}
	}
	
	public void initFirstLoad(CTown t)
	{
		town=t;
		
		fmastervolumeeffects=(float)town.gameConfigIni.soundeffectsvolume/100;
		fmastervolumeambient=(float)town.gameConfigIni.soundambientvolume/100;
		
		rainAmbientSound1=null;
		activeAmbientSound1=null;
		activeAmbientString="";
		
		//		sounds = new Hashtable<String,Sound>();
		//		ambient = new Hashtable<String,Music>();
		//		
		//		int iAmbDay = 1+town.rand.nextInt(countAmbientDay);
		//		ambient.put("ambient_day_birds"+iAmbDay, Gdx.audio.newMusic(Gdx.files.internal("sfx/ambient/ambient_day_birds"+iAmbDay+".mp3")));
		//		
		//		int iAmbNightCold = 1+town.rand.nextInt(countAmbientNightCold);
		//		ambient.put("ambient_night_cold"+iAmbNightCold, Gdx.audio.newMusic(Gdx.files.internal("sfx/ambient/ambient_night_cold"+iAmbNightCold+".mp3")));
		//		
		//		int iAmbNightWarm = 1+town.rand.nextInt(countAmbientNightWarm);
		//		ambient.put("ambient_night_warm"+iAmbNightWarm, Gdx.audio.newMusic(Gdx.files.internal("sfx/ambient/ambient_night_warm"+iAmbNightWarm+".mp3")));
		//		
		//		//int iAmbRain = 2; //1+town.rand.nextInt(countAmbientRain);
		//		FileHandle rain1 = Gdx.files.internal("sfx/ambient/ambient_rain1.mp3");
		//		ambient.put("ambient_rain2_1", Gdx.audio.newMusic(rain1));
		//		ambient.put("ambient_rain2_2", Gdx.audio.newMusic(rain1));
	}
	
	
}











