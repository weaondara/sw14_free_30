package sw.superwhateverjnr.render;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import lombok.Getter;
import lombok.Setter;

import android.graphics.Bitmap;
import android.opengl.GLUtils;

@Getter
public class GLTex
{
    private float vertices[];
    private float texturevertices[];
    private short index[];
    
    private FloatBuffer vertex;
    private FloatBuffer textureVertex;
    private ShortBuffer indeces;
    
    private Object ref;
    @Getter @Setter
    private Bitmap bitmap;
    private int glTextureId;
    
    public GLTex(Object ref, Bitmap bitmap)
    {
        this.ref = ref;
        this.bitmap = bitmap;
        
        vertices = new float[]{
            0.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.0f
        };
        
        texturevertices = new float[]{
            0.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 1.0f,
            1.0f, 0.0f
        };
        
        index = new short[]{
            0, 1, 2,
            2, 1, 3
        };
        
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4); 
        byteBuffer.order(ByteOrder.nativeOrder());
        vertex = byteBuffer.asFloatBuffer();
        
        
        byteBuffer = ByteBuffer.allocateDirect(texturevertices.length * 4); 
        byteBuffer.order(ByteOrder.nativeOrder());
        textureVertex = byteBuffer.asFloatBuffer();
        
        byteBuffer = ByteBuffer.allocateDirect(index.length * 4); 
        byteBuffer.order(ByteOrder.nativeOrder());
        indeces = byteBuffer.asShortBuffer();
        
        
        textureVertex.put(texturevertices);
        textureVertex.position(0);
        
        indeces.put(index);
        indeces.position(0);
    }
    
    public boolean upload(GL10 gl)
    {
        int texturemap[] = new int[1];
        gl.glGenTextures(1, texturemap, 0);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texturemap[0]);
        
        if(texturemap[0] == 0)
        {
            return false;
        }
        
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
//        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
//        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
        
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        
        glTextureId=texturemap[0];
        
        return true;
    }
    public void delete(GL10 gl)
    {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4); 
        byteBuffer.order(ByteOrder.nativeOrder());
        IntBuffer tex = byteBuffer.asIntBuffer();
        tex.put(new int[]{glTextureId});
        tex.position(0);
        
        gl.glDeleteTextures(1, tex);
        glTextureId = 0;
    }
    
    
    public void position(float x, float y, float width, float height, float dwidth, float dheight)
    {
        float left=(x - dwidth / 2) / (dwidth / 2);
        float right=left + width / (dwidth / 2);
        float top=(dheight / 2 - y) / (dheight / 2);
        float bottom=top - height / (dheight / 2);
        
        vertices = new float[]{
            left,  bottom, 0.0f,
            left,  top,    0.0f,
            right, bottom, 0.0f,
            right, top,    0.0f
        };
        
        vertex.put(vertices);
        vertex.position(0);
    }
    
    private float angle;
    public void rotate(GL10 gl, float degrees)
    {
        angle=+degrees;
        gl.glRotatef(angle, 0, 0, 1);
    }
    public void clearRotate(GL10 gl)
    {
        gl.glRotatef(-angle, 0, 0, 1);
        angle=0;
    }
    
    private float transx;
    private float transy;
    public void translate(GL10 gl, float x, float y, float dwidth, float dheight)
    {
    	transx=x;
    	transy=y;
    	
    	float left=(x - dwidth / 2) / (dwidth / 2);
    	float top=(dheight / 2 - y) / (dheight / 2);
    	
    	System.out.println(left+" "+top);
    	
    	gl.glTranslatef(left, top, 0);
    }
    public void clearTranslate(GL10 gl, float dwidth, float dheight)
    {
    	float left=(transx - dwidth / 2) / (dwidth / 2);
    	float top=(dheight / 2 - transy) / (dheight / 2);
    	
    	gl.glTranslatef(-left, -top, 0);
    	
    	transx=0;
    	transy=0;
    }
    
    public void draw(GL10 gl)
    {
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        
        gl.glBindTexture(GL10.GL_TEXTURE_2D, glTextureId);
        
        gl.glFrontFace(GL10.GL_CW);
        
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertex);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureVertex);
        
        gl.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_SHORT, indeces);
        
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisable(GL10.GL_TEXTURE_2D);
    }
}
