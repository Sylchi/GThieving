package scripts.gthieving;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.Walking;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.types.RSTile;

public class Restocking {
	private RSTile densBank = new RSTile(3042, 4972);

	public boolean storeLoot() {
		if(!Banking.isInBank()) {
			if(Player.getPosition().distanceTo(densBank) < 20) {
				Walking.walkTo(new RSTile(densBank.getX() + General.random(-2, 2), densBank.getY() + General.random(-2, 2)));
			} else {
				WebWalking.walkToBank();
			}
		} else if(Banking.isBankScreenOpen()) {
			Banking.depositAll();
			long t = System.currentTimeMillis();
			while(Timing.timeFromMark(t) < 2000 && Inventory.isFull()) {
				try {
					Thread.sleep(10, 1000);
				} catch (InterruptedException e) {
				}
			}
			return true;
		} else {
			Banking.openBank();
		}
		return false;
	}
	
	public void withdrawFood(int id, int amount) {
		if(Player.getPosition().distanceTo(densBank) < 20 && Skills.getActualLevel(SKILLS.AGILITY) >= 50) {
			Banking.withdraw(1, "Stethoscope");
		}
		Banking.withdraw(amount, id);
		long t = System.currentTimeMillis();
		while(Inventory.find(id).length == 0 && Timing.timeFromMark(t) < 2000) {
			try {
				Thread.sleep(100, 250);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
 