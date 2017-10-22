package scripts.gthieving;

import org.tribot.api.General;
import org.tribot.api.types.generic.Filter;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Game;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.PathFinding;
import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSNPC;

public class PickPocketing {
	
	

	public boolean moveToTarget(String name) {
		RSNPC[] targets = getFreeNPCS(name);
		if(Banking.isBankScreenOpen()) {
			Banking.close();
		}
		if(targets.length > 0) {
			if(targets[0].isClickable()) {
				return true;
			} else if(Game.getDestination() == null){
				Walking.blindWalkTo(targets[0].getPosition());
			}
		} 
		return false;
	}
	
	public boolean pickPocket(String name) {
		RSNPC[] targets = getFreeNPCS(name);
		if(targets.length > 0 && targets[0].isClickable()) {
			if(targets[0].click("Pickpocket " + name)) {
				try {
					Thread.sleep(General.randomLong(50, 250));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				targets[0].hover();
			}
		} 
		return false;
	}
	
	private RSNPC[] getFreeNPCS(String name) {	
		return NPCs.find(new Filter<RSNPC>(){		
			@Override		
			public boolean accept(RSNPC npc) {			
				return PathFinding.canReach(npc.getPosition(), false) && npc.getName().equals(name);		
				}	});}
	
}

