package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class CFont {

	public BitmapFont fontSmall;
	public BitmapFont bfArial2; //klein
	public BitmapFont bfArial; //groß
	public BitmapFont bfArialBlack; //groß, breit
	public ShaderProgram fontShader;
	public GlyphLayout layout;
	
	public CFont()
	{
		layout = new GlyphLayout();
		
        fontSmall = new BitmapFont();
        fontSmall.setColor(Color.WHITE);
		
		Texture texture = new Texture(Gdx.files.internal("gfx/font/a.png"), true); 
		texture.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Linear); 	
		bfArial = new BitmapFont(Gdx.files.internal("gfx/font/a.fnt"), new TextureRegion(texture), false);
		
		Texture texture2 = new Texture(Gdx.files.internal("gfx/font/arial1.png"), true); 
		texture2.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear); 	
		bfArial2 = new BitmapFont(Gdx.files.internal("gfx/font/arial1.fnt"), new TextureRegion(texture2), false);
		
		Texture texture3 = new Texture(Gdx.files.internal("gfx/font/abl.png"), true); 
		texture3.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Linear); 	
		bfArialBlack = new BitmapFont(Gdx.files.internal("gfx/font/abl.fnt"), new TextureRegion(texture3), false);
		
		fontShader = new ShaderProgram(Gdx.files.internal("gfx/font/font.vert"), Gdx.files.internal("gfx/font/font.frag"));
		
		if (!fontShader.isCompiled()) {
		    Gdx.app.error("fontShader", "compilation failed:\n" + fontShader.getLog());
		}
	}
	
	public String shortenStringToWidth(BitmapFont font1, String str, int width)
	{
		if(str==null)
			str=" ";
		if(str.length()==0)
			str=" ";
		
		layout.setText(font1, "...");
		int pointlength=Math.round(layout.width);
		
		layout.setText(font1, str);
		String scont="";
		
		//if(layout.width>width)
		//	scont="...";
		
		while(layout.width > width-pointlength)
		{
			str=str.substring(0, str.length()-1);
			layout.setText(font1, str);
			scont="..";
		}
		
		return str + scont;
	}
	
}
