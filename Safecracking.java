package scripts.gthieving;

import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Players;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSPlayer;
import org.tribot.api2007.types.RSTile;

public class Safecracking {
	private RSTile tile = null;
	
	public RSTile findFreeSafe() {
		RSObject[] safe = Objects.findNearest(20, "Wall safe");
		if(safe.length > 0) {
			for(RSObject o : safe) {
				tile = o.getPosition();
				for(RSPlayer player : Players.getAll()) {
					if(player.getPosition() == o.getPosition() && !Player.getRSPlayer().getName().equals(player.getName())) {
						tile = null;
						break;
					}
				}
				if(tile != null) {
					return tile;
				}
			}
		}		
		return tile;
	}
	
	public boolean crackSafe() {
		RSObject[] safe = Objects.findNearest(2, "Wall safe");
		if(safe.length > 0) {
			return safe[0].click("Crack");
		}
		return false;
	}
	
	
}
 