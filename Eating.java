package scripts.gthieving;

import org.tribot.api.General;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.types.RSItem;

public class Eating {

	public boolean eat(int eatAt, int foodID) {
		if(Skills.getCurrentLevel(SKILLS.HITPOINTS) <= eatAt) {
			RSItem[] food = Inventory.find(foodID);
			if(food.length > 0) {
				if(food[0].click("Eat")) {
					try {
						Thread.sleep(General.random(200, 550));
						return true;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return false;
	}
	
	
	
	public boolean needToEat(int eatAt) {
		return Skills.getCurrentLevel(SKILLS.HITPOINTS) <= eatAt;
	}
	
	public boolean hasFood(int id) {
		return Inventory.find(id).length > 0;
	} 
}
