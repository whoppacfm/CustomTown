package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;

public class CExtendedShapeRenderer extends ShapeRenderer{
	
	private ImmediateModeRenderer renderer;
	private Matrix4 matrix4;
	public CExtendedShapeRenderer(Matrix4 m4)
	{
		matrix4 = m4;
	    renderer = new ImmediateModeRenderer20(4, false, true, 0);		
	}
	
	public void dispose()
	{
		renderer.dispose();
		renderer=null;
	}
	
	public void filledPolygon(float[] vertices, Color color) {

		int offset=0;
		int count=vertices.length;
		
	    final float firstX = vertices[0];
	    final float firstY = vertices[1];
	    
        for (int i = offset, n = offset + count; i < n; i += 4) {

            final float x1 = vertices[i];
            final float y1 = vertices[i + 1];

            if (i + 2 >= count) {
                break;
            }

            final float x2 = vertices[i + 2];
            final float y2 = vertices[i + 3];

            final float x3;
            final float y3;

            if (i + 4 >= count) {
                x3 = firstX;
                y3 = firstY;
            } else {
                x3 = vertices[i + 4];
                y3 = vertices[i + 5];
            }

            float colorBits = color.toFloatBits();
            
            ShapeType st = ShapeType.Filled;
            renderer.begin(matrix4, st.getGlType());
            renderer.color(colorBits);
            renderer.vertex(x1, y1, 0);
            renderer.color(colorBits);
            renderer.vertex(x2, y2, 0);
            renderer.color(colorBits);
            renderer.vertex(x3, y3, 0);
            renderer.end();
        }
	}
	
}
