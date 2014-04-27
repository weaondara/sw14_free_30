package sw.superwhateverjnr.util;

import sw.superwhateverjnr.Game;
import sw.superwhateverjnr.settings.Settings;
import android.graphics.PointF;

public class Arithmetics
{
	public static PointF getControlLeftCenter()
	{
		return new PointF(margin()+radiusOuter(), 
				game().getDisplayHeight()-(margin()+radiusOuter()));
	}
	public static PointF getControlLeftLeftTop()
	{
		//new PointF(margin,game.getDisplayHeight()-(margin+2*radiusouter))
		return new PointF(margin(), 
				game().getDisplayHeight()-(margin()+2*radiusOuter()));
	}
	
	public static PointF getControlRightCenter()
	{
		return new PointF(2*margin()+3*radiusOuter(),
				  game().getDisplayHeight()-(margin()+radiusOuter()));
	}
	public static PointF getControlRightLeftTop()
	{
		//new PointF(2*margin+2*radiusouter,game.getDisplayHeight()-(margin+2*radiusouter))
		return new PointF(2*margin()+2*radiusOuter(),
						  game().getDisplayHeight()-(margin()+2*radiusOuter()));
	}
	
	public static PointF getControlJumpCenter()
	{
		return new PointF(game().getDisplayWidth()-(margin()+radiusOuter()), 
				  game().getDisplayHeight()-(margin()+radiusOuter()));
	}
	public static PointF getControlJumpLeftTop()
	{
		//new PointF(game.getDisplayWidth()-(margin+2*radiusouter),game.getDisplayHeight()-(margin+2*radiusouter))
		return new PointF(game().getDisplayWidth()-(margin()+2*radiusOuter()), 
						  game().getDisplayHeight()-(margin()+2*radiusOuter()));
	}
	
	private static Game game()
	{
		return Game.getInstance();
	}
	private static Settings settings()
	{
		return Game.getInstance().getSettings();
	}
	private static float radiusOuter()
	{
		return settings().getControlCircleRadiusOuter();
	}
	private static float margin()
	{
		return settings().getControlMargin();
	}
}
