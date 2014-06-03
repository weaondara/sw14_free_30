package sw.superwhateverjnr.world;

import java.util.List;

import com.google.common.base.Preconditions;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import sw.superwhateverjnr.block.Block;
import sw.superwhateverjnr.entity.Entity;

@Getter
@AllArgsConstructor(suppressConstructorProperties=true, access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class World
{
	@Setter
	private String name;
	private int width, height;
	private Location spawnLocation;
	private Block[][] data;
	private List<Entity> entities;
	
	public Block getBlockAt(Location loc)
	{
		return getBlockAt(loc.getBlockX(), loc.getBlockY());
	}
	public Block getBlockAt(int x, int y)
	{
		Preconditions.checkElementIndex(x, width, "x is outside the world");
		Preconditions.checkElementIndex(y, height, "y is outside the world");
		
		return data[x][y];
	}
}
