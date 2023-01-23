import java.util.ArrayList;
import javax.swing.*;

public class InfectionGUI extends JFrame{
	
	//-----------Μέθοδοι 3ης εργασίας-----------
	 
	private JPanel panel = new JPanel();
	private Database database;
	private User loggedInUser;

	public InfectionGUI(User user, Database databases) {
		loggedInUser=user;
		database=databases; //Η βάση δεδομένων των χρηστών δόθηκε ως όρισμα για 2 λόγους: 
							//1) Για την εύκολη μεταφορά της στο SignInGUI όταν θελήσει ο χρήστης να επιστρέψει στην σελίδα εισόδου
							//2) Για την ορθή λειτουργία του backToLoginButton, προκειμένου να γίνει σωστή κλήση της SignInGUI()
		
		//Δημιουργία πλαισίου κειμένου (μη επεξεργάσιμο από το χρήστη) με τις επαφές προς εξέταση
		JTextArea infectionTextField = new JTextArea(15, 35);
		infectionTextField.setEditable(false);
		setTextTo(infectionTextField);
		
		JButton backToLoginButton = Methods.setUpBackToLoginScreenButton(this, database); 
		
		panel.add(new JScrollPane(infectionTextField));
		panel.add(backToLoginButton);
		
		this.setContentPane(panel);
		this.setSize(400,400);
		this.setTitle("Πιθανή Μετάδοση Ιού");
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void setTextTo(JTextArea field) {
		
		ArrayList<User> contacts = loggedInUser.isInfected(); //Επιστρέφει όλες τις απαιτούμενες επαφές
		
		field.append(Methods.printStarsOnTextArea() 
					+ loggedInUser.getName() + " has been infected. The following users have to be tested." + System.lineSeparator() 
					+ Methods.printStarsOnTextArea());
		
		if (contacts.isEmpty()) {
			field.append("No users have to be tested");
			return;
		}
		else {
			for (User u:contacts)
				field.append(u.getName() + System.lineSeparator());
		}
	}
}
