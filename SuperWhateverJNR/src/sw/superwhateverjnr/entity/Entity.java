package sw.superwhateverjnr.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import sw.superwhateverjnr.util.Rectangle;

@Getter
@ToString
@EqualsAndHashCode
public class Entity
{
	private int id;
	private EntityType type;
	private Rectangle hitBox;
}
