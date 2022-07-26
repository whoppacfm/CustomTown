package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;

public class CCursor {
	
	private Cursor pm_default;
	private Cursor pm_klick;
	private Cursor pm_grab;
	
	public enum CursorType {
	    DEFAULT, KLICK, GRAB
	}
	
	public CCursor()
	{
		pm_default = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("gfx/cursor/hand1.png")), 0, 0);
		pm_klick = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("gfx/cursor/hand2.png")), 0, 0);
		pm_grab = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("gfx/cursor/hand3.png")), 0, 0);
		Gdx.graphics.setCursor(pm_default);
	}

	public void setCursor(CursorType ct)
	{
		if(ct==CursorType.DEFAULT)
			Gdx.graphics.setCursor(pm_default);
		
		if(ct==CursorType.KLICK)
			Gdx.graphics.setCursor(pm_klick);

		if(ct==CursorType.GRAB)
			Gdx.graphics.setCursor(pm_grab);
	}
			
	public void dispose()
	{
		pm_default.dispose();
		pm_klick.dispose();
		pm_grab.dispose();
	}
}
