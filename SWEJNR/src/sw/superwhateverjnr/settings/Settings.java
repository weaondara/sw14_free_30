package sw.superwhateverjnr.settings;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import java.nio.ByteBuffer;
import sw.superwhateverjnr.Game;
import sw.superwhateverjnr.SWEJNR;

public class Settings
{
	private float controlCircleRadiusOuter=-1;
	private float controlCircleRadiusInner=-1;
	private float controlArrowSize=-1;
	
	private int controlCircleColorOuter=-1;
	private int controlCircleColorInner=-1;
	private int controlArrowColor=-1;
	
	private byte controlCircleOpacityOuter=-1;
	private byte controlCircleOpacityInner=-1;
	private byte controlArrowOpacity=-1;
	
	private float controlMargin=-1;
	
	private int backgroundColor=-1;
	
	private boolean useGL = true;
    
    public Settings()
    {
        
    }
    
    public void load()
    {
    	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(SWEJNR.getInstance());
        controlCircleRadiusOuter = Integer.valueOf(sp.getString("prefOuterButtonSize", "-1"));
        controlCircleRadiusInner = Integer.valueOf(sp.getString("prefInnerButtonSize", "-1"));
        controlArrowSize = Integer.valueOf(sp.getString("prefArrowSize", "-1"));
        controlCircleColorOuter=Integer.decode(sp.getString("prefOuterColour", "-1"));
        controlCircleColorInner=Integer.decode(sp.getString("prefInnerColour", "-1"));
        controlArrowColor=Integer.decode(sp.getString("prefArrowColour", "-1"));
        controlCircleOpacityOuter=ByteBuffer.allocate(4).putInt(Integer.decode(sp.getString("prefOuterOpacity", "-1"))).array()[3];
        controlCircleOpacityInner=ByteBuffer.allocate(4).putInt(Integer.decode(sp.getString("prefInnerOpacity", "-1"))).array()[3];
        controlArrowOpacity=ByteBuffer.allocate(4).putInt(Integer.decode(sp.getString("prefArrowOpacity", "-1"))).array()[3];
        backgroundColor=Integer.decode(sp.getString("prefBackgroundColour", "-1"));
        useGL=!sp.getBoolean("prefSoftwareRendering", false);
    }
	
	public float getControlCircleRadiusOuter()
	{
		if(controlCircleRadiusOuter<0)
		{
			return (float)Game.getInstance().getDisplayWidth()/20;
		}
		return controlCircleRadiusOuter;
	}
	public float getControlCircleRadiusInner()
	{
		if(controlCircleRadiusInner<0)
		{
			return (float)Game.getInstance().getDisplayWidth()/25;
		}
		return controlCircleRadiusInner;
	}
	public float getControlArrowSize()
	{
		if(controlArrowSize<0)
		{
			return 7.5F;
		}
		return controlArrowSize;
	}
	public int getControlCircleColorOuter()
	{
		if(controlCircleColorOuter==-1)
		{
			return 0x275FFF;
		}
		return controlCircleColorOuter;
	}
	public int getControlCircleColorInner()
	{
		if(controlCircleColorInner==-1)
		{
			return 0xFFFFFF;
		}
		return controlCircleColorInner;
	}
	public int getControlArrowColor()
	{
		if(controlArrowColor==-1)
		{
			return 0x275FFF;
		}
		return controlArrowColor;
	}
	public byte getControlCircleOpacityOuter()
	{
		if(controlCircleOpacityOuter == -1)
		{
			return 0x37;
		}
		return controlCircleOpacityOuter;
	}
	public byte getControlCircleOpacityInner()
	{
		if(controlCircleOpacityInner == -1)
		{
			return 0x37;
		}
		return controlCircleOpacityInner;
	}
	public byte getControlArrowOpacity()
	{
		if(controlArrowOpacity == -1)
		{
			return 0x7F;
		}
		return controlArrowOpacity;
	}
	public float getControlMargin()
	{
		if(controlMargin<0)
		{
			return (float)Game.getInstance().getDisplayWidth()/60;
		}
		return controlMargin;
	}
	public int getBackgroundColor()
	{
		if(backgroundColor==-1)
		{
			return 0x275FFF;
		}
		return backgroundColor;
	}
	public boolean useGL()
	{
		return useGL;
	}
	
	
	public void setControlCircleRadiusOuter(float controlCircleRadiusOuter)
	{
		this.controlCircleRadiusOuter = controlCircleRadiusOuter;
	}
	public void setControlCircleRadiusInner(float controlCircleRadiusInner)
	{
		this.controlCircleRadiusInner = controlCircleRadiusInner;
	}
	public void setControlArrowSize(float controlArrowSize)
	{
		this.controlArrowSize = controlArrowSize;
	}
	public void setControlCircleColorOuter(int controlCircleColorOuter)
	{
		this.controlCircleColorOuter = controlCircleColorOuter;
	}
	public void setControlCircleColorInner(int controlCircleColorInner)
	{
		this.controlCircleColorInner = controlCircleColorInner;
	}
	public void setControlArrowColor(int controlArrowColor)
	{
		this.controlArrowColor = controlArrowColor;
	}
	public void setControlCircleOpacityOuter(byte controlCircleOpacityOuter)
	{
		this.controlCircleOpacityOuter = controlCircleOpacityOuter;
	}
	public void setControlCircleOpacityInner(byte controlCircleOpacityInner)
	{
		this.controlCircleOpacityInner = controlCircleOpacityInner;
	}
	public void setControlArrowOpacity(byte controlArrowOpacity)
	{
		this.controlArrowOpacity = controlArrowOpacity;
	}
	public void setControlMargin(float controlMargin)
	{
		this.controlMargin = controlMargin;
	}
	public void setBackgroundColor(int backgroundColor)
	{
		this.backgroundColor = backgroundColor;
	}
	public void useGL(boolean useGL)
	{
		this.useGL = useGL;
	}
}
