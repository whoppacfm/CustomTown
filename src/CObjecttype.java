package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

public class CObjecttype {

	//[OBJECTTYPEID];[OBJECTTYPENAME];[ICONFILENAME];[ACTIONCODE]
	String objectTypeId;
	String baseObjectTypeId;
	CObjecttype baseObjectType;
	String objectTypeName;
	String actionCode;
	String iconFileName;
	Texture textureIcon;
	
	int pos_x;
	int pos_y;
	int width;
	int height;
	
	public Boolean collidesIcon(int x, int y)
	{
		if(x>=pos_x && x<=pos_x+width && y>=pos_y && y <= pos_y+height)
		{
			return true;
		}
		
		return false;
	}
}
