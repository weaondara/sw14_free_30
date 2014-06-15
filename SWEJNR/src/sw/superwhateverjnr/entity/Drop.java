package sw.superwhateverjnr.entity;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import sw.superwhateverjnr.Game;
import sw.superwhateverjnr.world.Location;

public class Drop extends Entity
{
    private int points;
    DropType droptype;
    
    @Getter
    @RequiredArgsConstructor
    public enum DropType
    {
        ROTTEN_FLESH(0),
        BONES(1),
        GUNPOWDER(2);
        
        @NonNull
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
        droptype = (DropType)extraData.get("type");
    }
    
    protected void die()
    {
        super.die();
    }
    
    private void getTaken()
    {
        if(hitBox.intersects(Game.getInstance().getPlayer().getHitBox().translatedTo(Game.getInstance().getPlayer().getLocation())))
        {
            Game.getInstance().addPoints(points);
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
    }
}
