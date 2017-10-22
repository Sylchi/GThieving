package scripts.gthieving;

import org.tribot.api.General;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;

public class Stalls {
	long time = 0;
	
	public boolean dropLoot(String name) {
		RSItem[] loot = Inventory.find(name);
		if(loot.length > 0) {
			if(loot[0].click("Drop")) {
				return true; 
			}
		}
		return false;
	} 
	
	public boolean lootStall(String name) {
		RSObject[] objects = Objects.findNearest(3, name);
		if(objects.length > 0 ) {
			try {
				Thread.sleep(General.randomLong(200, 350));
			} catch (InterruptedException e) {}
			if(objects[0].getModel().click("Steal-from " + name)) {	
				return true;
			}
		}
		return false;
	}
	
	public void walkToStall(RSTile stallTile) {
		if(stallTile.isOnScreen()) {
			if(stallTile.click("Walk here")) {
				try {
					Thread.sleep(600, 1000);
				} catch (InterruptedException e) {}
			}
		} else {
			WebWalking.walkTo(stallTile);
		}
	}

}
