package scripts.gthieving;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.ListSelectionModel;
import javax.swing.JTextPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;

public class GUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2895766875544775063L;
	private JPanel contentPane;
	public int option = -1;
	public boolean dropLoot = false;


	@SuppressWarnings("serial")
	public GUI() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 433, 255);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane); 
		contentPane.setLayout(null);
		
		JList<String> list = new JList<String>();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setModel(new AbstractListModel<String>() {
			String[] values = new String[] {"Man (Level 1-5)", "Varrock tea stall (Level 5)", "Ardougne cake stall (Level 5)", "Ardougne silk stall (Level 20)", "Master Farmer (Level 38)", "Varrock Guard (Level 40)", "Burthope safes (Level 50)"};
			public int getSize() {
				return values.length;
			}
			public String getElementAt(int index) {
				return values[index];
			}
		});
		list.setBounds(10, 11, 192, 134);

		contentPane.add(list);
		
		JTextPane infoPane = new JTextPane();
		infoPane.setText("Please select the preferred method");
		infoPane.setEditable(false);
		infoPane.setBounds(212, 11, 192, 134);
		contentPane.add(infoPane);
		
		JCheckBox chckbxDropLoot = new JCheckBox("Drop loot");
		chckbxDropLoot.setBounds(10, 152, 97, 23);
		contentPane.add(chckbxDropLoot);
		
		JButton btnStart = new JButton("Start");
		btnStart.setEnabled(false);
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				option = list.getSelectedIndex();
				if(chckbxDropLoot.isSelected()) {
					dropLoot = true;
				}
				dispose();
			}
		});
		btnStart.setBounds(10, 182, 394, 23);
		contentPane.add(btnStart);
		

		
		list.addListSelectionListener(new ListSelectionListener() {
		    @Override
		    public void valueChanged(ListSelectionEvent event) {
		    	btnStart.setEnabled(true);
		    	switch(list.getSelectedIndex()) {
		    	case 0: 
		    		infoPane.setText("Steals from lumbridge men. Steals until the player dies. Eats food if there is any in the inventory, but doesnt bank for more. Good for lvl 1 clues and getting level 1-5 thieving.");
		    		break;
		    	case 1:
		    		infoPane.setText("Script will webwalk to Varrock tea stall and steal tea. No food needed. Drops the tea.");
		    		break;
		    	case 2: 
		    		infoPane.setText("Webwalks to Ardougne cake stall and steals cakes. Banks the cakes and also uses them for food if needed.");
		    		break;
		    	case 3:
		    		infoPane.setText("Webwalks to Ardougne silk stall and steals silk. Banks the silk");
		    		break;
		    	case 4: 
		    		infoPane.setText("Webwalks to rimmington Master Farmer and pickpockes seeds. Start with the preferred amount of preferred food in inventory, banks for more. Drops seeds worth under 500gp");
		    		break;
		    	case 5:
		    		infoPane.setText("Webwalks to Varrock guards and pickpockets them. Start with the preferred amount of preferred food in inventory, banks for more.");
		    		break;
		    	case 6: 
		    		infoPane.setText("Steals from Burthope safes. Start with the preferred amount of preferred food in inventory, banks for more. Supports stethoscope");
		    		break;
		    	
		    	}
		    }
		});
		

	}
}
