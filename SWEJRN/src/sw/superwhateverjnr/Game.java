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
	Player player;
	PointF minDisplayPoint;
	
	float displayWidth;
	float displayHeight;
}
