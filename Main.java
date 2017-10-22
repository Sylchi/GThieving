package scripts.gthieving;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.util.abc.ABCUtil;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.Walking;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSItemDefinition;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

@ScriptManifest(authors = { "Genka" }, category = "Thieving", name = "GThieving")
public class Main extends Script implements Painting{
	private ABCUtil abc = new ABCUtil();
	private Constants constants = new Constants();
	private PickPocketing pickpocketing = new PickPocketing();
	private Eating eating = new Eating();
	private Stalls stalls = new Stalls();
	private Restocking restocking = new Restocking();
	private Safecracking safecracking = new Safecracking();
	private int startXp = Skills.getXP(SKILLS.THIEVING);
	private boolean running = true;
	private GUI frame;
	private int mode = -1;
	private int foodID = -1;
	private int foodAmount = -1;
	private int nextEatAt = abc.generateEatAtHP();
	private long lastAction = System.currentTimeMillis();
	private boolean dropLoot = false;
	private long lastAnimation;
	
	@Override
	public void run() {
		initGUI();
		while(frame == null || frame.isVisible()) {
			sleep(10, 500);
		}
		mode = frame.option;
		dropLoot = frame.dropLoot;
		getFoodInfo();
		while(running) {
			doTimedActions();
			switch(mode) {
	    	case 0: 
	    		pickpocketMen();
	    		break;
	    	case 1:
	    		varrockTeaStall();
	    		break;
	    	case 2: 
	    		ardyCakeStall();
	    		break;
	    	case 3:
	    		ardySilkStall();
	    		break;
	    	case 4: 
	    		pickpocketMasterFarmer();
	    		break;
	    	case 5:
	    		pickpocketVarrockGuard();
	    		break;
	    	case 6: 
	    		crackSafes();
	    		break;
	    	}
			sleep(10, 50);
			if(Player.getAnimation() != -1) {
				lastAnimation = System.currentTimeMillis();
			}
		}
		
	}
	

	
	private void pickpocketMen() {
		if(Player.getPosition().distanceTo(constants.LUMBYTILE) < 20) {
			if(eating.needToEat(nextEatAt) && eating.eat(nextEatAt, foodID)) {
				nextEatAt = abc.generateEatAtHP();
			} else if (pickpocketing.moveToTarget("Man")) {
				pickpocketing.pickPocket("Man");
			}
		} else {
			WebWalking.walkTo(constants.LUMBYTILE);
		}
	}
	
	private void ardyCakeStall() {
		if(eating.needToEat(nextEatAt)) {
			getFoodInfo();
			if(eating.eat(nextEatAt, foodID)) {
				nextEatAt = abc.generateEatAtHP();
			}
		}
		if(Player.getPosition().distanceTo(constants.ARDY_CAKE_STALL_TILE) < 2 && !Inventory.isFull() && !Player.getRSPlayer().isInCombat()) {		
			if(dropLoot && stalls.dropLoot("Silk")) {
				dropSleep();
			}
			lootStalls("Baker's stall");
		} else if(Player.getRSPlayer().isInCombat() && !Player.isMoving()) {
			Walking.walkTo(new RSTile(Player.getPosition().getX() + General.random(6,9), Player.getPosition().getY() + General.random(-1, 1)));
			sleep(500, 1000);
		} else if(Inventory.isFull()) {
			restocking.storeLoot();
		} else if(!Player.isMoving() && Player.getPosition().distanceTo(constants.ARDY_CAKE_STALL_TILE) >= 2 && !Player.getRSPlayer().isInCombat()){	
			stalls.walkToStall(constants.ARDY_CAKE_STALL_TILE);
		}
	}
	
	private void ardySilkStall() {
		if(Player.getPosition().distanceTo(constants.ARDY_SILK_STALL_TILE) < 2 && !Inventory.isFull() && !Player.getRSPlayer().isInCombat()) {		
			if(dropLoot && stalls.dropLoot("Silk")) {
				dropSleep();
			}
			lootStalls("Silk stall");
		} else if(Player.getRSPlayer().isInCombat() && !Player.isMoving()) {
			Walking.walkTo(new RSTile(Player.getPosition().getX() + General.random(6,9), Player.getPosition().getY() + General.random(-1, 1)));
			sleep(500, 1000);
		} else if(Inventory.isFull()) {
			restocking.storeLoot();
		} else if(!Player.isMoving() && Player.getPosition().distanceTo(constants.ARDY_SILK_STALL_TILE) >= 2 && !Player.getRSPlayer().isInCombat()){	
			stalls.walkToStall(constants.ARDY_SILK_STALL_TILE);
		}
	}
	
	private void varrockTeaStall() {
		if(Player.getPosition().distanceTo(constants.VARROCK_TEA_STALL_TILE) < 2) {
			if(stalls.dropLoot("Cup of tea")) {
				sleep(300, 800);
			}
			lootStalls("Tea stall");
		} else if(!Player.isMoving()){
			stalls.walkToStall(constants.VARROCK_TEA_STALL_TILE);
		}
	}
	
	private void pickpocketMasterFarmer() {
		if(Player.getPosition().distanceTo(constants.RIMMYTILE) < 20) {
			if(!eating.hasFood(foodID)|| Inventory.isFull()) {
				if((Inventory.find(foodID).length == 0 && Inventory.getAll().length > 0) || Inventory.isFull()) {
					restocking.storeLoot();
				} else if (Inventory.getAll().length == 0) {
					restocking.withdrawFood(foodID, foodAmount);
				}
			} else if(eating.needToEat(nextEatAt) && eating.eat(nextEatAt, foodID)) {
				nextEatAt = abc.generateEatAtHP();
			} else if (pickpocketing.moveToTarget("Master Farmer")) {
				sleep(1000);
				pickpocketing.pickPocket("Master Farmer");
			}
		} else {
			WebWalking.walkTo(constants.RIMMYTILE);
		}
	}
	
	private void pickpocketVarrockGuard() {
		if(!eating.hasFood(foodID)) {
			if(Inventory.find(foodID).length == 0 && Inventory.getAll().length > 0) {
				restocking.storeLoot();
			} else if (Inventory.getAll().length == 0) {
				restocking.withdrawFood(foodID, foodAmount);
			}
		} else	if(Player.getPosition().distanceTo(constants.VGUARDTILE) < 20) {
			if(eating.needToEat(nextEatAt) && eating.eat(nextEatAt, foodID)) {
				nextEatAt = abc.generateEatAtHP();
			} else if (pickpocketing.moveToTarget("Guard")) {
				pickpocketing.pickPocket("Guard");
			}
		} else {
			WebWalking.walkTo(constants.VGUARDTILE);
		}
	}
	
	private void crackSafes() {
		if(!eating.hasFood(foodID)) {
			if(Inventory.find(foodID).length == 0 && Inventory.getAll().length > 0) {
				restocking.storeLoot();
			} else if (Inventory.getAll().length == 0) {
				restocking.withdrawFood(foodID, foodAmount);
			}
		} else if(eating.needToEat(nextEatAt) && eating.eat(nextEatAt, foodID)) {
			nextEatAt = abc.generateEatAtHP();
		} else if(Timing.timeFromMark(lastAnimation) > 1000 && Player.getPosition().distanceTo(safecracking.findFreeSafe()) < 2) {
				sleep(50, 300);
				if(safecracking.crackSafe()) {
					long t = System.currentTimeMillis();
					while(Timing.timeFromMark(t) < 1000 && Player.getAnimation() == -1) {
						sleep(50, 300);
					}
				}
		} else if(Timing.timeFromMark(lastAnimation) > 5000 && Player.getPosition().distanceTo(safecracking.findFreeSafe()) >= 2) {
			Walking.walkTo(safecracking.findFreeSafe());
		}
				
	}
	
	
	private void dropSleep() {
		long t = System.currentTimeMillis();
		int count = Inventory.getAll().length;
		while(Timing.timeFromMark(t) < 1000 && Inventory.getAll().length == count) {
			sleep(10, 100);
		}
	}
	
	private void getFoodInfo() {
		RSItem[] invy = Inventory.getAll();
		for(int i = 0; i < invy.length; i++) {
			RSItemDefinition def = invy[i].getDefinition();
			if(def != null) {
				if(def.getActions()[0].equals("Eat")) {
					foodID = invy[i].getID();
					foodAmount = Inventory.find(invy[i].getID()).length;
				}
			}
		}  
	}	 
	
	private void lootStalls(String stallName) {
		if(Timing.timeFromMark(lastAction) > 1500) {
			if(stalls.lootStall(stallName)) {
				lastAction = System.currentTimeMillis();
			}
		}
	}
	
	
	
	private void initGUI() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private void doTimedActions() {
		if (abc.shouldCheckTabs()) {
			abc.checkTabs();
		}
		if (abc.shouldCheckXP()) {	
			abc.checkXP();
		}
		if (abc.shouldExamineEntity()) {
			abc.examineEntity();
		}
		if (abc.shouldMoveMouse()) {
			abc.moveMouse();
		}
		if (abc.shouldPickupMouse()) {
			abc.pickupMouse();
		}
		if (abc.shouldRightClick()) {
			abc.rightClick();
		}
		if (abc.shouldRotateCamera()) {
			abc.rotateCamera();
		}
		if (abc.shouldLeaveGame()) {
			abc.leaveGame();
		}
	}
	
	//Paint section start
    private Image getImage(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch(IOException e) {
            return null;
        }
    }

    private final Color color1 = new Color(255, 255, 255);
    private final Font font1 = new Font("Arial", 0, 10);
    private final Image img1 = getImage("http://i.imgur.com/QDPesn5.png");

	@Override
	public void onPaint(Graphics g1) {
		double multiplier = getRunningTime() / 3600000.0D;
	    int xpPerHour = (int) ((Skills.getXP(SKILLS.THIEVING) - startXp) / multiplier);		
		Graphics2D g = (Graphics2D)g1;
        g.drawImage(img1, 277, 345, null);
        g.setFont(font1);
        g.setColor(color1);
        g.drawString("Runtime: " + Timing.msToString(getRunningTime()), 366, 409);
        g.drawString("Thieving level: " + Skills.getActualLevel(SKILLS.THIEVING), 366, 421);
        g.drawString("XP: "  + (Skills.getXP(SKILLS.THIEVING) - startXp) + " (" + xpPerHour + ")", 366, 433);
		
	}
	//Paint section end

}
