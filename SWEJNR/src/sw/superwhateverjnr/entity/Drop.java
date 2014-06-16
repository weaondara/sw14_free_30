package sw.superwhateverjnr.entity;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import sw.superwhateverjnr.Game;
import sw.superwhateverjnr.world.Location;

public class Drop extends Entity
{
	@Getter
    private int points;
	@Getter
    private DropType dropType;
    
    @Getter
    @AllArgsConstructor
    public enum DropType
    {
        ROTTEN_FLESH(367, 75),
        BONES(352, 100),
        GUNPOWDER(289, 250);
        
        private int id;
        private int points;
        
        public Map<String, Object> toExtraData()
        {
            Map<String, Object> m = new HashMap<String, Object>();
            m.put("points", points);
            m.put("type", this);
            return m;
        }
    }
    
    public Drop(int id, EntityType type, Location location, Map<String, Object> extraData)
    {
        super(type, location, extraData);
        points = (Integer)extraData.get("points");
        dropType = (DropType)extraData.get("type");
    }
    
    protected void die()
    {
        super.die();
    }
    
    private void getTaken()
    {
        if(hitBox.translatedTo(location).intersects(Game.getInstance().getPlayer().getHitBox().translatedTo(Game.getInstance().getPlayer().getLocation())))
        {
            Game.getInstance().addPoints(points);
            die();
        }
    }
    
    public void takeDamage(DamageCause cause, double radius)
    {
        if(cause != DamageCause.STOMPED_BY_PLAYER)
        {
            super.takeDamage(cause, radius);
        }
    }
    
    public void tick()
    {
        super.tick();
        getTaken();
    }
}
