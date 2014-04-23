package sw.superwhateverjnr;

import android.graphics.PointF;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import sw.superwhateverjnr.entity.Entity;
import sw.superwhateverjnr.entity.Player;

@Getter
@ToString
@EqualsAndHashCode
public class Game
{
	private Player player;
	private PointF minDisplayPoint;
	
	private float displayWidth;
	private float displayHeight;
	
	private int textureWidth;
	private int textureHeight;
	
	public Game()
	{
		player=null;
		minDisplayPoint=new PointF(0,0);
		SWEJNR.getInstance();
	}
}
