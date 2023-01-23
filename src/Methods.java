//Βοηθητικές μέθοδοι για ευκολότερη και πιο κατανοητή εκτέλεση

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Methods {
	
	//Οι μέθοδοι της 4ης εργασίας ξεκινούν από τη γραμμή 104
	
	//-----------Μέθοδοι 2ης εργασίας-----------
	
	/* 
	 * Το format αποτελείται από 3 τμήματα: Κωδικός τμήματος, Ψηφία μητρώου, Email Domain
	 * Για το διαχωρισμό αυτό απαιτούνται 2 διαχωριστικά: 
	 * 1) Το τέλος του Κωδικού τμήματος και η αρχή των Ψηφίων (Νοητό διαχωριστικό)
	 * 2) Το @, που χωρίζει ορατά τα Ψηφία από το Email Domain
	 */
	
	public static boolean properEmailFormat(String email)
	{
		/*
		 * Ο Κωδικός τμήματος καταλαμβάνει σταθερά τις 3 πρώτες θέσεις. 
		 * Αν και ο Κωδικός τελειώνει στη θέση 2, η χρήση του 3 δείχνει από ποιο σημείο και μετά 
		 * τελειώνει ο Κωδικός και ξεκινούν τα Ψηφία μητρώου, και επίσης βοηθά σε διάφορες συναρτήσεις αργότερα
		 */
		final int codeEnd = 3;
		
		int atSignIndex=email.indexOf('@', codeEnd); //Εφόσον ο αριθμός μητρώου δεν έχει σταθερό μέγεθος, απαιτείται η εύρεση 
													//της θέσης του @ για τον έλεγχο εγκυρότητας του email
		
		if (!properCode(email, codeEnd)) return false; //Έλεγχος εγκυρότητας Κωδικού τμήματος
		if (!properDigitLength(email, codeEnd, atSignIndex)) return false; //Έλεγχος εγκυρότητας Ψηφίων μητρώου
		if (!properEmailDomain(email, atSignIndex)) return false; //Έλεγχος εγκυρότητας Domain
		
		//Αν όλα τα τμήματα έχουν το σωστό format, ο constructor User μπορεί να προχωρήσει με τη δημιουργία του χρήστη
		return true; 
	}
	
	private static boolean properCode(String email, int codeEnd)
	{
		String[] codes = {"iis", "ics", "dai"};
		String emailCode=email.substring(0, codeEnd); //Εδώ το codeEnd=3 περιορίζει το substring στους 3 πρώτους χαρακτήρες
		
		for (String s:codes)
			if (s.equals(emailCode)) return true;
		
		return false;
	}
	
	private static boolean properDigitLength(String email, int codeEnd, int atSignIndex)
	{
		/*
		 * Τα Ψηφία μητρώου πρέπει να είναι από 3 έως 5, έτσι τα σωστά format για AM στο email είναι:
		 * κωδxxx@ (@ στη θέση 6) 
		 * κωδxxxx@ (@ στη θέση 7) 
		 * κωδxxxxx@ (@ στη θέση 8)
		 * Έτσι, η αφαίρεση μετράει πόσες θέσεις καταλαμβάνουν τα Ψηφία αυτά
		 */
		int difference = atSignIndex - codeEnd; 
		
		//Υπό την προϋπόθεση ότι το codeEnd=3, τα αποδεκτά αποτελέσματα της αφαίρεσης είναι από 3 έως 5
		return (difference >=3 && difference<=5);
	}
	
	private static boolean properEmailDomain(String email, int atSignIndex)
	{
		String emailDomain = "uom.edu.gr";
		String domain = email.substring(atSignIndex+1); //Η αναζήτηση του Domain ξεκινάει μία θέση μετά το @, και τρέχει ως το τέλος του string
														//Το email που δίνεται ως όρισμα είναι το διορθωμένο από το email = email.replace(" ", "");
		
		return domain.equals(emailDomain);
	}
	
	//-----------Μέθοδοι 3ης εργασίας-----------	
	
	/*
	 * Δημιουργία κουμπιού επιστροφής στη Σελίδα Εισόδου.
	 * Επειδή το κουμπί αυτό αξιοποιείται με πανομοιότυπο τρόπο από παραπάνω από μία μεθόδους,
	 * είναι προτιμότερο να υλοποιηθεί ως στατική μέθοδος εξωτερικά.
	 */
		
	public static JButton setUpBackToLoginScreenButton(JFrame frame, Database databases)
	{
		/*
		 * Το frame δόθηκε ως παράμετρος έτσι ώστε το παράθυρο του InfectionGUI/UserPageGUI να κλείνει όταν χρησιμοποιηθεί το backToLoginButton
		 * H database δόθηκε ως παράμετρος για την σωστή κλήση του SignInGUI όταν χρησιμοποιηθεί το backToLoginButton
		 */
		
		JButton backToLoginButton = new JButton("Back to Login Screen");
		
		backToLoginButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new SignInGUI(databases);
				frame.dispose();
			}		
		});
		
		return (backToLoginButton);
	}
	
	public static String printStarsOnTextArea()
	{
		String stars = "";
		for (int i=0;i<72;i++) stars=stars.concat("*");
		stars=stars.concat(System.lineSeparator());
		return stars;
	}
	
	//-----------Μέθοδοι 4ης εργασίας-----------
	
	//Όλες οι γενικές μέθοδοι στη συγκεκριμένη κλάση καλούν τη μέθοδο popMessage() για την εμφάνιση μηνυμάτων σε παράθυρα MessageDialog
	
	private static void popMessage(String str) {
		JOptionPane.showMessageDialog(null, str);
	}
	
	public static void popMessageUserNotFound(String name) {
		popMessage("User " + name + " Not Found");
	}
	
	public static void popMessageUserAlreadyExists(String name) {
		popMessage("Cannot create user with name " + name + ". User already exists.");
	}
	
	public static void popMessageNotProperEmailFormat() {
		popMessage("Email format not proper, please try again.");
	}
	
	public static void popMessageUserCreated(User user) {
		popMessage("Account for user " + user.getName() + " created successfully! (Email: " + user.getEmail() + ")");
	}
	
	public static void popMessageUsersAlreadyFriends(User loggedInUser, User friend) {
		popMessage(loggedInUser.getName() + ", you are already friends with " + friend.getName() + "!");
	}

	public static void popMessageUsersNowFriends(User loggedInUser, User friend) {
		popMessage(loggedInUser.getName() + " and " + friend.getName() + " are now friends!");		
	}
	
	public static void popMessagePostIsEmpty(User loggedInUser) {
		popMessage("Your post cannot be empty!");
	}
	
	public static void popMessageUserEnrolledInGroup(User loggedInUser, Group group) {
		popMessage(loggedInUser.getName() + ", you have been successfully enrolled in " + group.getName() + "!");
	}
	
	public static void popMessageUserAlreadyInGroup(User loggedInUser, Group group) {
		popMessage(loggedInUser.getName() + ", you are already enrolled in " + group.getName() + "!");
	}
	
	public static void popMessageUserCannotEnrollInGroup(User loggedInUser, Group group) {
		popMessage("FAILED: " + loggedInUser.getName() + ", you cannot enroll in " + group.getName() + "!");
	}
	
	public static void popMessageUsersAreSame() {
		popMessage("No adding yourself to the list!");
	}
}