import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class SignInGUI extends JFrame{

	//Το frame ορίζεται προκειμένου το παράθυρο του SignInGUI να κλείνει όταν χρησιμοποιηθεί οποιοδήποτε κουμπί προς άλλο GUI
	private JFrame frame = this; 
	private JPanel panel = new JPanel(); 
	
	private JTextField usernameField, emailField;
	private User usernameFieldUser; //Χρήστης ο οποίος θα οριστεί αν και μόνο αν βρεθεί εγγραφή με το όνομα που δίνεται στο usernameField
	
	private JButton userPageButton, infectionPageButton, newUserButton;
	private JButton resetPamakbookButton, savePamakbookButton;
	
	private Database database;
	
	public SignInGUI(Database databases) {
		database=databases;
		
		usernameField = new JTextField("User name", 10); 
		emailField = new JTextField("User email", 15);
		
		userPageButton = new JButton("Enter User Page");
		infectionPageButton = new JButton("Show Potential Infections");
		newUserButton = new JButton("New User");
		resetPamakbookButton = new JButton("Reset PamakBook");
		savePamakbookButton = new JButton("Save PamakBook");
		
		UserRelatedButtonListener userRelatedlistener = new UserRelatedButtonListener();
		userPageButton.addActionListener(userRelatedlistener);
		infectionPageButton.addActionListener(userRelatedlistener);
		newUserButton.addActionListener(userRelatedlistener);
		
		PamakBookStatusButtonListener statusListener = new PamakBookStatusButtonListener();
		savePamakbookButton.addActionListener(statusListener);
		resetPamakbookButton.addActionListener(statusListener);
		
		panel.add(newUserButton);
		panel.add(usernameField);
		panel.add(emailField);
		panel.add(userPageButton);
		panel.add(infectionPageButton);
		panel.add(savePamakbookButton);
		panel.add(resetPamakbookButton);
		
		this.setContentPane(panel);	
		this.setSize(400,200);
		this.setTitle("Είσοδος Χρήστη");
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/*
	 * ActionListener για κουμπιά που σχετίζονται με συγκεκριμένο χρήστη
	 */
	class UserRelatedButtonListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			String userName = usernameField.getText(); 
			usernameFieldUser = database.containsUser(userName);
			
			if (e.getSource().equals(userPageButton)) {
				if (usernameFieldUser!=null) {
					new UserPageGUI(usernameFieldUser, database);
					frame.dispose();
				}
				else Methods.popMessageUserNotFound(userName);		
			}
			else if (e.getSource().equals(infectionPageButton)) {
				if (usernameFieldUser!=null) {
					new InfectionGUI(usernameFieldUser, database);
					frame.dispose(); 
				}
				else Methods.popMessageUserNotFound(userName);
			}
			else if (e.getSource().equals(newUserButton)) {
				String email = emailField.getText();
				
				if (usernameFieldUser!=null) {
					Methods.popMessageUserAlreadyExists(userName);
				}
				else if (!Methods.properEmailFormat(email)) {
					Methods.popMessageNotProperEmailFormat();
				}
				else {
					User newUser=new User(userName, email);
					database.addToDatabase(newUser);
					Methods.popMessageUserCreated(newUser);
				}					
			}		
		}	
	}
	
	/*
	 * ActionListener για κουμπιά που σχετίζονται με την επαναφορά ή την αποθήκευση του PamakBook
	 */
	class PamakBookStatusButtonListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(resetPamakbookButton)) {
				database.resetPamakbook();
				System.out.println("Pamakbook status has been reset" + System.lineSeparator());
			}
			else if (e.getSource().equals(savePamakbookButton)) {
				database.savePamakbookStatus(database);
				System.out.println("Pamakbook status has been saved" + System.lineSeparator());
			}	
		}					
	}		
}
