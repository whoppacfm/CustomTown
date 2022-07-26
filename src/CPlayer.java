package com.mygdx.game;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.IntArray;

public class CPlayer {
    private static final int FRAME_COLS = 8;
    private static final int FRAME_ROWS = 1;
	
    //private static final int FRAME_COLS = 3;
    //private static final int FRAME_ROWS = 1;

	
    //int iIdleFrame=4;
    int iIdleFrame=1;
            
    Texture walkSheet;
    //Texture bodySheet;       
    Texture head;
    Texture hat;
    Random rand;;
    Animation walkAnimation;
    TextureRegion[] walkFrames;
    TextureRegion[] idleFrames;     
    
    Animation headAnimation;
    TextureRegion[] headFrames;
    TextureRegion currentHeadFrame;     

    Animation bodyAnimation;
    TextureRegion[] bodyFrames;
    
    
    SpriteBatch spriteBatch;          
    TextureRegion currentFrame;
    
    
    TextureRegion currentBodyFrame;        
    float stateTime;                                     

    int px;
    int py;

    int ziel_x;
    int ziel_y;
    int ziel_x_changed;
    int ziel_y_changed;
    int temp_ziel_x;
    int temp_ziel_y;
    
    public IntArray path;
    ShapeRenderer shapeRenderer1;
    
    float rendertime;
    float rotmod;
    
    float rotation;
    
    Body body;
    
    //public CWorld gameWorld;
    public CTown town;
    
    public CPlayer(CTown t1)
    {
    	town=t1;
    }
    
    public void init(int x, int y)
    {
    	//if(body!=null)
    	//	gameWorld.box2dworld.destroyBody(body); //error
    	rendertime=0;
    	rotmod=0;
    	rand = new Random();
        px = x; //Gdx.graphics.getWidth()/2;
		py = y; //Gdx.graphics.getHeight()/2;
		ziel_x=px;
		ziel_y=py;
		rotation=0;
		
		//Player
		//walkSheet = new Texture(Gdx.files.internal("man3_tr.png"),true);
		//walkSheet = new Texture(Gdx.files.internal("gfx/people/woman3_anim.png"),true);
		//walkSheet = new Texture(Gdx.files.internal("gfx/people/man2_anim.png"),true);
		
		walkSheet = new Texture(Gdx.files.internal("gfx/people/base_man1.png"),true);
		//walkSheet = new Texture(Gdx.files.internal("gfx/people/man3.png"),true);
		//walkSheet = new Texture(Gdx.files.internal("gfx/people/base_woman.png"),true);
		
		//walkSheet = new Texture(Gdx.files.internal("gfx/people/base_action1.png"),true);
		//walkSheet = new Texture(Gdx.files.internal("gfx/people/bodies/body1.png"),true);
		//bodySheet = new Texture(Gdx.files.internal("gfx/people/bodies/body1.png"),true);
		
		//walkSheet = new Texture(Gdx.files.internal("gfx/people/man1_anim.png"),true);
		
		//head = new Texture(Gdx.files.internal("gfx/people/heads/man_head5.png"),true);
		//head = new Texture(Gdx.files.internal("gfx/people/heads/woman_head29.png"),true);
		head = new Texture(Gdx.files.internal("gfx/people/heads/man/man_head7.png"),true);
		hat = new Texture(Gdx.files.internal("gfx/people/heads/hat/hat1.png"),true);
        
		
		//Player-Filter
		//walkSheet.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		walkSheet.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Linear);
		//head.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Linear);
		head.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		//head.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		hat.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Linear);
        
        headFrames = new TextureRegion[2];
        headFrames[0]=new TextureRegion(head);
        headFrames[1]=new TextureRegion(head);
        
        headFrames[0].flip(false, true);
        headFrames[1].flip(true, true);
        headAnimation = new Animation(0.8f, headFrames);
		
        //bodyFrames = new TextureRegion[2];
        //bodyFrames[0]=new TextureRegion(bodySheet);
        //bodyFrames[1]=new TextureRegion(bodySheet);
        //bodyFrames[0].flip(false, true);
        //bodyFrames[1].flip(true, true);
        //bodyAnimation = new Animation(0.2f, bodyFrames);
        
        
		TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth()/FRAME_COLS, walkSheet.getHeight()/FRAME_ROWS);
        walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        idleFrames = new TextureRegion[2 * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }
        walkAnimation = new Animation(0.08f, walkFrames);
        //walkAnimation = new Animation(0.04f, walkFrames);
        
        spriteBatch = new SpriteBatch();              
        stateTime = 0f;                     
                
		//Body
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		//bodyDef.linearDamping = 10f;
		//bodyDef.angularDamping = 10f;
		bodyDef.linearDamping = 50f;
		bodyDef.angularDamping = 30f;
		
		//bodyDef.bullet=true;
		bodyDef.fixedRotation=true;
		
		bodyDef.position.set(((px)+walkAnimation.getKeyFrame(0).getRegionWidth()/2)*town.gameWorld.b2dvaluePos, (py+walkAnimation.getKeyFrame(0).getRegionHeight()/2)*town.gameWorld.b2dvaluePos);
		//bodyDef.position.set(((px))*gameWorld.b2dvaluePos, (py)*gameWorld.b2dvaluePos);
		body = town.gameWorld.box2dworld.createBody(bodyDef);
		
		
		//body.setLinearVelocity(0.1f, 0.1f);
		//MassData aa = new MassData();
		//aa.mass=0;
		//body.setMassData(aa);
		
		//Array<Fixture> fixtures=null;
		//Array<Body> bodies=null;
		//gameWorld.box2dworld.getFixtures(fixtures);
		//gameWorld.box2dworld.getBodies(bodies); 
		//float angle = bodies.get(0).localVector.angle();
		//CObject obj = (CObject)bodies.get(0).getUserData();
		
		CircleShape circle = new CircleShape();
		//circle.setRadius(45f*gameWorld.b2dvalue);
		//circle.setRadius(1f*gameWorld.b2dvalue);
		//circle.setRadius(4f);
		circle.setRadius(30f*town.gameWorld.b2dvalueSize);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		//fixtureDef.density = 0.1f; 
		//fixtureDef.friction = 0.1f;
		//fixtureDef.restitution = 0.5f; // Make it bounce a little bit
		fixtureDef.density = 0f; 
		fixtureDef.friction = 0f;
		fixtureDef.restitution = 2f;
		
		fixtureDef.isSensor=true;
		
		Fixture fixture = body.createFixture(fixtureDef);
		
		Filter filt = fixture.getFilterData(); //nicht lichtdurchlässig
		filt.categoryBits=255;
		filt.groupIndex=255;
		filt.maskBits=0;
		
		fixture.setFilterData(filt);
		
		circle.dispose();
    	
		spriteBatch.setColor(1, 1, 1, 0.7f);
		
		shapeRenderer1=new ShapeRenderer();
    }
    
    public void pathFinding()
    {
    	if(body==null)
    		return;
    	
    	if(ziel_x<0 || ziel_y<0)
    		return;
    	
		float bodyx = body.getPosition().x/town.gameWorld.b2dvaluePos;//+walkAnimation.getKeyFrame(stateTime, true).getRegionWidth()/2;
		float bodyy = body.getPosition().y/town.gameWorld.b2dvaluePos;//+walkAnimation.getKeyFrame(stateTime, true).getRegionHeight()/2;
		
		bodyx-=8;
		bodyy-=8;
		
		float fspeedvalue=1;
		if(town.gameGui!=null)
			fspeedvalue = CHelper.getSpeedControllerValue(town.gameGui);     		
		float speed = CHelper.getFPSValue(0.05f+fspeedvalue/25000);
		
		
		
		//Gdx.app.debug("path", "zielx: " + ziel_x + ", ziel_y: " + ziel_y);
		
		//Gibt es einen Weg der abgelaufen werden muss?
		if(Math.abs(ziel_x - (int)bodyx) > speed+town.gameWorld.nodesize || Math.abs(ziel_y - (int)bodyy) > speed+town.gameWorld.nodesize)
	    {
			if(ziel_x!=ziel_x_changed || ziel_y != ziel_y_changed)
			{
				ziel_x_changed=ziel_x;
				ziel_y_changed=ziel_y;
				//cfm reset
				path = town.gameWorld.astar.getPath((int)bodyx/town.gameWorld.nodesize, (int)bodyy/town.gameWorld.nodesize, (int)ziel_x/town.gameWorld.nodesize, (int)ziel_y/town.gameWorld.nodesize, true, null, null);
				
				if(path!=null && path.size>1)
				{
					temp_ziel_y = path.pop()*town.gameWorld.nodesize; //gameWorld.path.get(gameWorld.path.size-1)*gameWorld.nodesize;
					if(path.size>0)
						temp_ziel_x = path.pop()*town.gameWorld.nodesize; //gameWorld.path.get(gameWorld.path.size)*gameWorld.nodesize;
				}
			}
			
			if(path!=null)
			{
	     		try{
					if(Math.abs(temp_ziel_x - (int)bodyx) < speed*100f && Math.abs(temp_ziel_y - (int)bodyy) < speed*100)
					{
						if(path.size>1)
						{
							temp_ziel_y = path.pop()*town.gameWorld.nodesize; //gameWorld.path.get(gameWorld.path.size-1)*gameWorld.nodesize;
							if(path.size>0)
								temp_ziel_x = path.pop()*town.gameWorld.nodesize; //gameWorld.path.get(gameWorld.path.size)*gameWorld.nodesize;
						}
					}
	     		}
	     		catch(Exception e){}
	     		
		    	float speedx=0; // = CHelper.getFPSValue(3000.0f);
		    	float speedy=0;
		    	
		    	
		    	
		        if(temp_ziel_x > (int)bodyx)
		        {
		        	speedx = speed;
		        }
		        else if(temp_ziel_x < (int)bodyx)
		        {
		        	speedx = speed*-1;
		        }
		        
		        if(temp_ziel_y > (int)bodyy)
		        {
		        	speedy = speed;
		        }
		        else if(temp_ziel_y < (int)bodyy)
		        {
		        	speedy = speed * -1;
		        }
		        
		        if(Math.abs(ziel_x - (int)bodyx) > 10 || Math.abs(ziel_y - (int)bodyy) > 10)
		        {
		        	rotation = (float) Math.atan2((float)temp_ziel_y - (float)bodyy, (float)(temp_ziel_x - (bodyx)));
		        	rotation = MathUtils.radiansToDegrees*rotation;
		        	rotation=rotation+90f;
		        }
		        
		        //body.applyLinearImpulse(speedx, speedy, body.getPosition().x, body.getPosition().y, true);
		        //body.applyAngularImpulse(50000000, true);
		        //body.setGravityScale(1000000000);
		        //body.setLinearVelocity(1000000000, 1000000000);
		        //body.applyForce(speedx, speedy, body.getPosition().x, body.getPosition().y, true);
		        //body.setTransform(body.getPosition().x+ziel_x/3000, body.getPosition().y+ziel_y/3000, rotation);
		        //body.setLinearVelocity(speedx, speedy);
		        //body.setTransform(body.getPosition().x+speedx, body.getPosition().y+speedy, rotation);
		    	if(body!=null)
		    		body.setTransform(body.getPosition().x+speedx, body.getPosition().y+speedy, rotation);
		        
		    	stateTime += Gdx.graphics.getDeltaTime();
		    	currentFrame = walkAnimation.getKeyFrame(stateTime, true);
			}
			else
				currentFrame=walkAnimation.getKeyFrames()[iIdleFrame];
	    }
	    else
	    {
	    	ziel_x=-1;
	    	ziel_y=-1;
	    	currentFrame=walkAnimation.getKeyFrames()[iIdleFrame];
	    }
    }
    
    public void renderPath()
    {
    	if(path!=null)
    	{
    		if(!town.gameWorld.bRenderPathfinding)
    			return;    		
    		
    		shapeRenderer1.setProjectionMatrix(town.gameCam.combined);
    	    shapeRenderer1.setAutoShapeType(true);
    	    
    	    shapeRenderer1.begin();
    			//Gdx.gl.glEnable(GL30.GL_BLEND);
    			//Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
    	    	
    	    	shapeRenderer1.setColor(255, 1, 1, 0.1f);
    	    	
				for (int i = 0, n = path.size; i < n; i += 2) {
					int x = path.get(i);
					int y = path.get(i + 1);
					shapeRenderer1.circle(x * town.gameWorld.nodesize + town.gameWorld.nodesize / 2, y * town.gameWorld.nodesize + town.gameWorld.nodesize / 2, town.gameWorld.nodesize / 4, 30);
				}
				
        	shapeRenderer1.end();
    	}    	
    }
    
	public void render()
	{
		pathFinding();
		renderPath();
	    px = (int)(body.getPosition().x/town.gameWorld.b2dvaluePos)-walkAnimation.getKeyFrame(stateTime, true).getRegionWidth()/2;
	    py = (int)(body.getPosition().y/town.gameWorld.b2dvaluePos)-walkAnimation.getKeyFrame(stateTime, true).getRegionHeight()/2;
	    spriteBatch.setProjectionMatrix(town.gameCam.combined);
	    spriteBatch.begin();
		    //Schatten - Shadow
			//spriteBatch.setColor(0, 0, 0, 0.2f);
			//spriteBatch.draw(currentFrame, px, py, currentFrame.getRegionWidth(), currentFrame.getRegionHeight(), currentFrame.getRegionWidth()*2+6, currentFrame.getRegionHeight()*2+15, 1, 1, rotation);
			//spriteBatch.draw(currentFrame, px, py, currentFrame.getRegionWidth(), currentFrame.getRegionHeight(), currentFrame.getRegionWidth()*1.7f, currentFrame.getRegionHeight()*1.7f, 1, 1, rotation+180);
	    	
	    	spriteBatch.setColor(1, 1, 1, 0.9f);
	    	//spriteBatch.setColor(1, 1f, 1f, 1f);
	    	//spriteBatch.draw(currentFrame, px, py, currentFrame.getRegionWidth(), currentFrame.getRegionHeight(), currentFrame.getRegionWidth()*2, currentFrame.getRegionHeight()*2, 1, 1, rotation);
	    	
	    	//Erwachsen
	    	//float fsizefactor1=1.7f;
	    	//float fsizefactor=0.6f;
	    	
	    	//Jugendlich
	    	//float fsizefactor1=1.2f;
	    	//float fsizefactor=0.8f;
	    	float fsizefactor1=1f;
	    	float fsizefactor=1f;
	    	
	    	//Kind
	    	//float fsizefactor1=1f;
	    	//float fsizefactor=1f;
	    	
	    	//Kleinkind
	    	//float fsizefactor1=0.8f;
	    	//float fsizefactor=1.2f;
	    	
	    	spriteBatch.draw(currentFrame, px, py, currentFrame.getRegionWidth()/2*fsizefactor1, currentFrame.getRegionHeight()/2*fsizefactor1, currentFrame.getRegionWidth()*fsizefactor1, currentFrame.getRegionHeight()*fsizefactor1, 1, 1, rotation);
	    	//spriteBatch.draw(currentFrame, px, py, currentFrame.getRegionWidth()/2*fsizefactor1, currentFrame.getRegionHeight()/2*fsizefactor1, currentFrame.getRegionWidth()*fsizefactor1, currentFrame.getRegionHeight()*fsizefactor1, 1, 1, rotation);
	    	
	    	
	    	
	    	
	    	float a =  Gdx.graphics.getDeltaTime();
	    	float irot = rotation;
	    	rendertime+=Gdx.graphics.getDeltaTime();
	    	
	    	float limit=0.7f;
	    	if(ziel_x>-1)
	    		limit=0.1f;
	    	
	    	if(rendertime>limit)
	    	{
	    		rendertime=0;
		    	//if(ziel_x>-1 && ziel_y>-1)
		    	{
		    		rotmod=0;
		    		
		    		int val = rand.nextInt(8);
		    		if(val==1)
		    			rotmod=5;
		    		if(val==2)
		    			rotmod=-5;
		    		
		    		if(val==3)
		    			rotmod=8;
		    		if(val==4)
		    			rotmod=-8;
		    		
		    		if(val==5)
		    			rotmod=10;
		    		if(val==6)
		    			rotmod=-10;
		    	}
	    	}
	    	
	    	currentHeadFrame = headAnimation.getKeyFrame(stateTime, true);
	    	
	    	float h=0.8f;
	    	spriteBatch.setColor(h, h, h, 1f);
	    	//spriteBatch.draw(currentHeadFrame, px/fsizefactor, py/fsizefactor, currentFrame.getRegionWidth()/2/fsizefactor, currentFrame.getRegionHeight()/2/fsizefactor, 60/fsizefactor, 70/fsizefactor, 1, 1, rotation+rotmod);
	    	spriteBatch.draw(currentHeadFrame, px+30/fsizefactor, py+35/fsizefactor, currentFrame.getRegionWidth()/2/fsizefactor-30/fsizefactor, currentFrame.getRegionHeight()/2/fsizefactor-35/fsizefactor, 60/fsizefactor, 70/fsizefactor, 1, 1, rotation+rotmod);
	    	
	    	
	    	
	    	//spriteBatch.draw(head, px+20, py+30, currentFrame.getRegionWidth()-20, currentFrame.getRegionHeight()-30, 60, 70, 1, 1, rotation+rotmod, 0, 0, head.getWidth(), head.getHeight(), false, true);
	    	
	    	
	    	//spriteBatch.draw(hat, px+25, py+35, currentFrame.getRegionWidth()-25, currentFrame.getRegionHeight()-35, 50, 60, 1, 1, rotation, 0, 0, hat.getWidth(), hat.getHeight(), false, true);
	    	
	    	//spriteBatch.draw(hat, px+20, py+30, currentFrame.getRegionWidth()-20, currentFrame.getRegionHeight()-30, 60, 70, 1, 1, rotation, 0, 0, hat.getWidth(), hat.getHeight(), false, true);
	    	//spriteBatch.draw(hat, px+20, py+30, currentFrame.getRegionWidth()-20, currentFrame.getRegionHeight()-30, 50, 70, 1, 1, rotation, 0, 0, hat.getWidth(), hat.getHeight(), false, true);
	    	
	    	//spriteBatch.draw(head, px+20, py+30, currentFrame.getRegionWidth()-20, currentFrame.getRegionHeight()-30, 60, 80, 1, 1, rotation, 0, 0, head.getWidth(), head.getHeight(), false, true);

	    	//Child:
	    	//spriteBatch.draw(currentFrame, px, py, currentFrame.getRegionWidth(), currentFrame.getRegionHeight(), currentFrame.getRegionWidth()*2, currentFrame.getRegionHeight()*2, 1, 1, rotation);
	    	//spriteBatch.draw(head, px+5, py+40, currentFrame.getRegionWidth()-5, currentFrame.getRegionHeight()-40, 80, 80, 1, 1, rotation, 0, 0, head.getWidth(), head.getHeight(), false, true);
	    	
	    	//spriteBatch.draw(head, px+25, py+35, currentFrame.getRegionWidth()-25, currentFrame.getRegionHeight()-35, 50, 50, 1, 1, rotation, 0, 0, head.getWidth(), head.getHeight(), false, false);
	    	//spriteBatch.draw(currentFrame, px, py, currentFrame.getRegionWidth()*1.5f, currentFrame.getRegionHeight()*1.5f, currentFrame.getRegionWidth()*3, currentFrame.getRegionHeight()*3, 1, 1, rotation);
	    	
	    	//spriteBatch.draw draw(head, px, py, 20, 20);
	    	//spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	    spriteBatch.end();
	}
}


