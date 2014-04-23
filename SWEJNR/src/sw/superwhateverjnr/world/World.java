package sw.superwhateverjnr.world;

import com.google.common.base.Preconditions;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.AccessLevel;
import sw.superwhateverjnr.block.Block;

@Getter
@AllArgsConstructor(suppressConstructorProperties=true, access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class World
{
	private String name;
	private int width, height;
	private Location spawnLocation;
	private Block[][] data;
	
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
