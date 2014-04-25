package sw.superwhateverjnr.settings;

import sw.superwhateverjnr.Game;

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
	
	private int backgroudColor=-1;
	

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
		if(controlCircleOpacityOuter<0)
		{
			return 0x37;
		}
		return controlCircleOpacityOuter;
	}
	public byte getControlCircleOpacityInner()
	{
		if(controlCircleOpacityInner<0)
		{
			return 0x37;
		}
		return controlCircleOpacityInner;
	}
	public byte getControlArrowOpacity()
	{
		if(controlArrowOpacity<0)
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
	public int getBackgroudColor()
	{
		if(backgroudColor==-1)
		{
			return 0x275FFF;
		}
		return backgroudColor;
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
	public void setBackgroudColor(int backgroudColor)
	{
		this.backgroudColor = backgroudColor;
	}
}
