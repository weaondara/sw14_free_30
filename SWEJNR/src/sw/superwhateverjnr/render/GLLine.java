package sw.superwhateverjnr.render;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class GLLine
{
	private float vertices[];
    
    private FloatBuffer vertex;
    
    public GLLine()
    {
        vertices = new float[]{
            0.0f, 0.0f,
            0.0f, 0.0f
        };
        
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4); 
        byteBuffer.order(ByteOrder.nativeOrder());
        vertex = byteBuffer.asFloatBuffer();
    }
    
    public void position(float x, float y, float width, float height, float dwidth, float dheight)
    {
        float left=(x - dwidth / 2) / (dwidth / 2);
        float right=left + width / (dwidth / 2);
        float top=(dheight / 2 - y) / (dheight / 2);
        float bottom=top - height / (dheight / 2);
        
        vertices = new float[]{
            left,  top,
            right, bottom
        };
        
        vertex.put(vertices);
        vertex.position(0);
    }
    
    public void color(GL10 gl, int color)
    {
    	gl.glColor4f(
            (float)((color & 0x00FF0000) >> 16) / 256,
            (float)((color & 0x0000FF00) >> 8 ) / 256,
            (float)((color & 0x000000FF)      ) / 256,
            (float)((color & 0xFF000000) >> 24) / 256);
    }
    
    public void clearColor(GL10 gl)
    {
    	gl.glColor4f(1, 1, 1, 1);
    }
    
    public void draw(GL10 gl)
    {
    	gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        
        gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vertex);
        gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, 2);
        
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }
}
