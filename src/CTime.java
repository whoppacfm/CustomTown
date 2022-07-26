package com.mygdx.game;

import com.badlogic.gdx.Gdx;

public class CTime {
	
	public int year;
	public int day;
	public int hours;
	public int minutes;
	public int seconds;
	public CTown town;
	public float sumDelta;
	
	public CTime(CTown t)
	{
		town=t;
	}
	
	public long getTotalSeconds()
	{
		return year*town.daysInYear*86400+day*86400+hours*3600+minutes*60+seconds;
	}
	
	public int getCurrentDay()
	{
		return 1 + Math.round(getTotalSeconds()/3600/24)-town.startingMonth;
	}
	
	public String getTimeString()
	{
		//return String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds) + "     Day  " + day;
		//return getMonthString().toUpperCase() + "-" + String.format("%04d", year) + " " + String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
		return String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
	}
	
	public String getAMPMTimeString()
	{
		//return String.format("%02d", CHelper.getAMPMHour(hours)) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
		return String.format("%02d", CHelper.getAMPMHour(hours)) + ":" + String.format("%02d", minutes) + CHelper.getAMPMString(hours);
	}
	
	public String getYearString()
	{
		//return String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds) + "     Day  " + day;
		//return getMonthString().toUpperCase() + "-" + String.format("%04d", year) + " " + String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
		return String.format("%04d", year);
	}
	
	public String getCurrentDayString()
	{
		//return String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds) + "     Day  " + day;
		//return getMonthString().toUpperCase() + "-" + String.format("%04d", year) + " " + String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
		return String.format("%04d", getCurrentDay());
	}
	
	public static Boolean isHourBetweenSchedule(int from, int to, int hour)
	{
		if(from>-1 && to>-1)
		{
			if(from == to)
			{
				if(hour==from)
					return true;
			}
			else if(from > to)
			{
				//Beispiele:
				
				// 07:00 - 02:00
				//	true:
				// 		07:00 - 00:00
				// 		00:00-02:00
				//	false: 
				//		03:00 - 06:00
				//
				
				// 23:00-02:00
				// 	true:  	  
				//		00:00
				//  	01:00
				
				
				if(hour>=to && hour<from)
					return false;
				
				return true;
				
				//if(hour >= from)
				//	return true;
				
				//if(hour <= to)
				//	return true;
			}
			else
			{
				if(hour>=from && hour<to)
					return true;
			}
		}
		
		return false;
	}
	
	public String getMonthString()
	{
		if(day==1)
			return "Jan";
		if(day==2)
			return "Feb";
		if(day==3)
			return "Mar";
		if(day==4)
			return "Apr";
		if(day==5)
			return "May";
		if(day==6)
			return "Jun";
		if(day==7)
			return "Jul";
		if(day==8)
			return "Aug";
		if(day==9)
			return "Sep";
		if(day==10)
			return "Oct";
		if(day==11)
			return "Nov";
		if(day==12)
			return "Dec";
		
		return "";
	}
	
	public void init(long totalseconds)
	{
		if(totalseconds<1)
			return;
		
		double totalsecondstemp=0;
		double seconds1=0;
		double minutes1=0;
		double hours1=0;
		double days1=0;
		double years1=0;

		if(totalseconds<1)
			return;

		years1=totalseconds/(12*86400);
		totalsecondstemp=totalseconds%(12*86400);
		
		days1 = totalsecondstemp/86400;
		totalsecondstemp = totalseconds%86400;
		
		if(totalsecondstemp>=1)
		{
			hours1=totalsecondstemp/3600;
			totalsecondstemp=totalsecondstemp%3600;
	
			if(totalsecondstemp>=1)
			{
				minutes1=totalsecondstemp/60;
				totalsecondstemp=totalsecondstemp%60;
				
				if(totalsecondstemp>=1)
				{
					seconds1 = totalsecondstemp;
				}
			}
		}
		
		year=(int)years1;
		day=(int)days1;
		hours=(int)hours1;
		minutes=(int)minutes1;
		seconds=(int)seconds1;
		
		if(day==0) {
			day=12;
		}

		if(day==13) {
			day=1;
		}
	}
	
	public void init(int year_, int day_, int hours_, int minutes_, int seconds_)
	{
		year=year_;
		day=day_;
		hours=hours_;
		minutes=minutes_;
		seconds=seconds_;
		sumDelta=0;
	}
	
	public void step(float delta)
	{
		//Gdx.app.setLogLevel(5);
		//Gdx.app.debug("test0", "totalseconds: "+getTotalSeconds() + ", year: " + year + ", day: " + day + ", hours: " + hours + ", minutes: " + minutes + ", sec: " +  seconds);
		//year*town.daysInYear*86400+day*86400+hours*3600+minutes*60+seconds						
		
		sumDelta+=delta;
		while(sumDelta>=1f)
		{
			seconds+=1;
			sumDelta-=1;
		}
		
		if(seconds>=60)
		{
			minutes+=seconds/60;
			seconds=seconds%60;
			
			if(minutes>=60)
			{
				hours+=minutes/60;
				minutes=minutes%60;
				
				if(hours>=24)
				{
					//Gdx.app.debug("", ""+hours);
					day+=hours/24;
					hours=hours%24;
					//Gdx.app.debug("", ""+hours + ", " + day + ", " + year);
					
					if(day>town.daysInYear) //12
					{
						year+=day/town.daysInYear;
						day=day%town.daysInYear;
						
						if(day==0)
							day=1;
						
						//Gdx.app.debug("", ""+hours + ", " + day + ", " + year);
					}
				}
			}
		}
	}
}





