package com.mygdx.game;

import java.util.Optional;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class CGuiControl_Slider {

	int sliderX;
	int sliderXStart;
	float sliderValue;
	Boolean bSliderDown;
	float minValue;
	float maxValue;
	SpriteBatch spriteBatch;
	int controlX;
	int controlY;
	int iDiff;
	
	ShapeRenderer shapeRenderer;
	
	CObject buttonControl_slider;
	CObject buttonControl_button;
	
	CObject buttonControl_slider_temp;
	CObject buttonControl_button_temp;
	
	Boolean bEnabled;
	
	public CGuiControl_Slider(int x, int y, float min, float max, float startingValue, CObject objSlider, CObject objButton, CTown town)
	{
		bEnabled=true;
		shapeRenderer = town.gameGui.shapeRenderer;
		
		//buttonControl_speed1_slider = ((Optional<CObject>)editorControlsShowing.stream().filter(p->p.editoraction.equals("control_button_speed1")).findFirst()).get();			
		//buttonControl_speed2_button = ((Optional<CObject>)editorControlsShowing.stream().filter(p->p.editoraction.equals("control_button_speed2")).findFirst()).get();
		
		buttonControl_slider = objSlider;
		buttonControl_button = objButton;
		
		buttonControl_slider_temp = new CObject(null);
		buttonControl_button_temp = new CObject(null);
		
		iDiff=0;
		controlX=x;
		controlY=y;
		sliderX = x;
		sliderXStart=x;
		bSliderDown=false;
		minValue=min;
		maxValue=max;
		spriteBatch=town.gameGui.editorSpriteBatch;
		
		setValue(startingValue);
	}
	
	public void setEnabled(Boolean enable)
	{
		bEnabled=enable;
	}
	
	public void setPosition(int x, int y)
	{
		if(controlX!=x || controlY!=y)
		{
			controlX=x;
			controlY=y;
			sliderX = x;
			sliderXStart=x;
			
			setValue(sliderValue);
		}
	}
	
	public float getValue()
	{
		return sliderValue;
	}
	
	public void setValue(float value)
	{
		sliderValue=value;
		sliderX = Math.round(sliderXStart + sliderValue - minValue);
		
		//buttonControl_slider_temp.icon_pos_x
	}
	
	public int getWidth()
	{
		return (int)(maxValue-minValue+buttonControl_button_temp.icon_width);		
	}
	
	public void render()
	{
		int mx = Gdx.input.getX();
        int my = CHelper.screenToLibGDX(Gdx.input.getY());		
        
        spriteBatch.begin();
		spriteBatch.setColor(1, 1, 1, 1f);
    	
    	//buttonControl_speed2_button.icon_pos_y = (int)Gdx.graphics.getHeight()-42;
    	//buttonControl_speed1_slider.icon_pos_x = (int)Gdx.graphics.getWidth()-490;
		buttonControl_button_temp.icon_pos_y = controlY; //(int)Gdx.graphics.getHeight()-(int)CHelper.getScreenValueY(80);
    	buttonControl_slider_temp.icon_pos_x = controlX; //(int)Gdx.graphics.getWidth()-(int)CHelper.getScreenValueX(236);
    	//buttonControl_slider_temp.icon_pos_y = (int) (buttonControl_button_temp.icon_pos_y+CHelper.getScreenValueX(7));
    	buttonControl_slider_temp.icon_pos_y = (int) (buttonControl_button_temp.icon_pos_y+7);
		//    	buttonControl_button_temp.icon_width = (int)CHelper.getScreenValueX(15);
		//    	buttonControl_button_temp.icon_height = (int)CHelper.getScreenValueY(15);
		//    	buttonControl_slider_temp.icon_width = (int)CHelper.getScreenValueX(maxValue-minValue+buttonControl_button_temp.icon_width);
		//    	buttonControl_slider_temp.icon_height = (int)CHelper.getScreenValueY(2);
    	
    	//buttonControl_button_temp.icon_width = (int)(15);
    	//buttonControl_button_temp.icon_height = (int)(15);
    	buttonControl_button_temp.icon_width = (int)(22);
    	buttonControl_button_temp.icon_height = (int)(22);
    	
    	buttonControl_slider_temp.icon_width = (int)(maxValue-minValue+buttonControl_button_temp.icon_width);
    	buttonControl_slider_temp.icon_height = (int)(2);
    	
    	if(sliderX<buttonControl_slider_temp.icon_pos_x)
    		sliderX=buttonControl_slider_temp.icon_pos_x;
    	
		//    	if(sliderX>buttonControl_slider_temp.icon_pos_x+buttonControl_slider_temp.icon_width)
		//    		sliderX=buttonControl_slider_temp.icon_pos_x+buttonControl_slider_temp.icon_width;
    	if(sliderX>buttonControl_slider_temp.icon_pos_x+buttonControl_slider_temp.icon_width-buttonControl_button_temp.icon_width)
    		sliderX=buttonControl_slider_temp.icon_pos_x+buttonControl_slider_temp.icon_width-buttonControl_button_temp.icon_width;
    	
    	buttonControl_button_temp.icon_pos_x=(int)sliderX; //(int)Gdx.graphics.getWidth()-520;
    	
    	sliderValue = minValue+((sliderX)-(buttonControl_slider_temp.icon_pos_x)); //*(int)CHelper.getScreenValueX_U(100);
    	if(sliderValue<0)
    		sliderValue=0;
    	
    	
    	
    	//Gdx.app.debug(String.valueOf(speedControllerValue), "");
    	//speedControllerValue=20000;
    	
		//if(buttonControl_slider_temp.collidesIcon(mx, my))
		//	spriteBatch.setColor(1,1,70,0.7f);
    	
    	if(bEnabled)
    		spriteBatch.setColor(1,1,1,0.8f);
    	else
    		spriteBatch.setColor(0.7f,0.7f,0.7f,0.7f);
    	
		spriteBatch.draw(buttonControl_slider.textureImage, buttonControl_slider_temp.icon_pos_x, buttonControl_slider_temp.icon_pos_y, buttonControl_slider_temp.icon_width, buttonControl_slider_temp.icon_height);
		
    	//if(buttonControl_button_temp.collidesIcon(mx, my))
    	//	spriteBatch.setColor(1,1,70,0.7f);
    			
		//spriteBatch.setColor(1,1,1,0.8f);
		
		if(!bEnabled)
			buttonControl_button_temp.icon_pos_x=buttonControl_slider_temp.icon_pos_x+buttonControl_slider_temp.icon_width/2-buttonControl_button_temp.icon_width/2;
		
		buttonControl_button_temp.icon_pos_y-=2.4f;
		
		if(buttonControl_button_temp.collidesIcon(mx, my) && bEnabled)
			spriteBatch.draw(buttonControl_button.textureImage, buttonControl_button_temp.icon_pos_x-1, buttonControl_button_temp.icon_pos_y-1, buttonControl_button_temp.icon_width+2, buttonControl_button_temp.icon_height+2);
		else
			spriteBatch.draw(buttonControl_button.textureImage, buttonControl_button_temp.icon_pos_x, buttonControl_button_temp.icon_pos_y, buttonControl_button_temp.icon_width, buttonControl_button_temp.icon_height);

		spriteBatch.setColor(1,1,1,1f);
		
    	spriteBatch.end();
    	
    	
		//**********
		//Mouseover
		//**********
//    	if(buttonControl_button_temp.collidesIcon(mx, my))
//    	{
//			Gdx.gl.glEnable(GL30.GL_BLEND);
//		    Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
//		    shapeRenderer.setAutoShapeType(true);
//			shapeRenderer.begin();
//				shapeRenderer.setColor(0.4f, 0.4f, 0.4f, 0.9f);
//				shapeRenderer.set(ShapeType.Line);
//				shapeRenderer.rect(buttonControl_button_temp.icon_pos_x-5, buttonControl_button_temp.icon_pos_y-5, buttonControl_button_temp.icon_width+10, buttonControl_button_temp.icon_height+10); 
//			shapeRenderer.end();
//			Gdx.gl.glDisable(GL30.GL_BLEND);
//    	}
    	
	}
	
	public Boolean buttonUp()
	{
		bSliderDown=false;
		return true;
	}
	
	public Boolean mouseMovedDrag(int x, int y, int libgdxy)
	{
		if(bSliderDown && bEnabled)
		{
			sliderX=x-iDiff;
			return true;
		}
		
		return false;
	}	
	
	public Boolean buttonDown(int x, int y, int libgdxy, int button)
	{
		//Klick auf Slider Button
		if(buttonControl_button_temp.collidesIcon(x, libgdxy) && bEnabled)
		{
			bSliderDown=true;
			iDiff=x-sliderX;
			return true;
		}
		
		//Klick auf Slider-Linie
		if(bEnabled)
		{
			if(x>controlX && x<controlX+getWidth() && libgdxy>controlY-5 && libgdxy<controlY+20)
			{
				sliderX=x-buttonControl_button_temp.icon_width/2+2;
				return true;
			}
		}
		
		return false;
	}
}
