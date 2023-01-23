import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

/* Η κλάση περιέχει όλα τα δεδομένα απαραίτητα για την ορθή λειτουργία του Pamakbook σε κάθε χρονική στιγμή
 * 
 * Παρέχεται επιπλέον εναλλακτικός Constructor που καλείται από την PamakbookErgasia2Status
 */

public class Database implements Serializable {
	
	private static final String DEFAULT_FILE = "pamakbookDefaultStatus.ser";
	private static final String CHANGED_FILE = "pamakbookStatusChanged.ser";
	private ArrayList<User> users = new ArrayList<>();
	private ArrayList<Group> groups = new ArrayList<>();

	public Database() {
	/* 
	 * Η μέθοδος createNewFile() επιστρέφει true αν δεν υπάρχει το αρχείο CHANGED_FILE 
	 * και το δημιουργεί, ειδάλλως, αν το αρχείο υπάρχει, επιστρέφει false.
	 * Ο βρόχος if εκτελείται αν το αρχείο δημιουργηθεί. Αρχικά εισάγονται στα γνωρίσματα του this τιμές από το DEFAULT_FILE,
	 * ενώ παράλληλα αποθηκεύεται η κατάσταση του this στο νέο αρχείο CHANGED_FILE (Έτσι ώστε να περιέχεται ένα στιγμιότυπο 
	 * των δεδομένων στο νέο αρχείο, σε περίπτωση που σταματήσει το πρόγραμμα για οποιοδήποτε λόγο). 
	 * Με τη νέα εκκίνηση του προγράμματος, τώρα που το CHANGED_FILE υπάρχει και έχει στοιχεία, θα εκτελεστεί ο βρόχος else 
	 * και θα ανακτηθούν από αυτό τα δεδομένα που αποθηκεύτηκαν τελευταία (είτε από το χρήστη είτε από τον αρχικό βρόχο)
	 */
		try {
			if (new File(CHANGED_FILE).createNewFile()) {
				getPamakbookStatus(DEFAULT_FILE);
				savePamakbookStatus(this);
			}
			else
				getPamakbookStatus(CHANGED_FILE);
				
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<User> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}

	public ArrayList<Group> getGroups() {
		return groups;
	}

	public void setGroups(ArrayList<Group> groups) {
		this.groups = groups;
	}
	
	public void addToDatabase(User user) {
		this.users.add(user);
	}
	
	public User containsUser(String name) {
		
		for (User user:this.users) 
			if (user.getName().equals(name)) 
				return user;
		
		return null;
	}
	
	//------------------------
	//Μέθοδοι ανάκτησης και αποθήκευσης δεδομένων από και σε αρχεία
	
	
	/*
	 * Ανακτά τα κατάλληλα δεδομένα από ένα συγκεκριμένο αρχείο, τα οποία εισάγονται στα γνωρίσματα του this 
	 */
	private void getPamakbookStatus(String filename) {
		
		try {
			FileInputStream fileIn = new FileInputStream(filename);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			Database database = (Database) in.readObject();
			this.setUsers(database.getUsers());
			this.setGroups(database.getGroups());
			
			in.close();
			fileIn.close();
			
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException i) {
			i.printStackTrace();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	/*
	 * Αποθηκεύει την κατάσταση του Pamakbook στο CHANGED_FILE
	 */
	public void savePamakbookStatus(Database database) {
		
		try {
			FileOutputStream fileOut = new FileOutputStream(CHANGED_FILE);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(database);
			
			out.close();
			fileOut.close();
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	/* 
	 * Καλείται με το πάτημα του resetPamakbookButton, και επανορίζει τα δεδομένα του Pamakbook βάσει της κατάστασης του DEFAULT_FILE με χρήση της getPamakbookStatus().
	 * Η νέα κατάσταση της βάσης δεδομένων αποθηκεύεται στη συνέχεια στο CHANGED_FILE και αντικαθιστά την προηγούμενη αποθηκευμένη κατάσταση.
	 */
	public void resetPamakbook() {
		getPamakbookStatus(DEFAULT_FILE);
		savePamakbookStatus(this);
	}
	
	//-------------
	//Μέθοδοι υπολογισμού διαμέτρου του δικτύου χρηστών
	
	/*
	 * Υπολογίζει και τυπώνει τη διάμετρο του δικτύου χρηστών κάθε φορά που δημιουργείται μια νέα φιλία στο δίκτυο.
	 *  
	 * Όταν δημιουργηθεί μια νέα φιλία, η μέθοδος εξετάζει κάθε χρήστη του δικτύου μέσω επαναληπτικής δομής, 
	 * βρίσκοντας την μεγαλύτερη απόσταση του τρέχοντος χρήστη (rootUserLength) από τους υπόλοιπους χρήστες του δικτύου, 
	 * και συγκρίνοντάς τη με τις μεγαλύτερες αποστάσεις των υπολοίπων χρηστών. Η μεγαλύτερη απόσταση από αυτές συνιστά τη διάμετρο του δικτύου.
	 * 
	 * Ο χρήστης στην κάθε επανάληψη ονομάζεται root, καθώς στο τέλος κάθε επανάληψης δημιουργείται ουσιαστικά μια δομή δέντρου, όπου κάθε επίπεδό του  
	 * καθορίζει την απόσταση των υπολοίπων χρηστών από τον root. Περισσότερες λεπτομέρειες στην traverseNetworkContacts().
	 */
	public void calcDatabaseGraphDiameter() {
		
		ArrayList<User> remainingUsers = new ArrayList<>();
		int diameter=-1;
		
		for(User root:this.users) {
			remainingUsers.addAll(this.users); //Λίστα με όλους τους χρήστες του δικτύου
			
			int rootUserLength=0;
			rootUserLength=traverseNetworkContacts(root, remainingUsers, rootUserLength);
			if (rootUserLength>diameter) diameter=rootUserLength;
			//System.out.println(root.getName() + "'s longest path: " + rootUserLength);
			
			remainingUsers.removeAll(this.users); //Άδειασμα της λίστας για να επανοριστεί εξ αρχής για τον επόμενο χρήστη root
		}
		
		System.out.println("Network diameter is currently: " + diameter);
	}
	
	/*
	 * Η κύρια μέθοδος που χρησιμοποιείται έχει ως ορίσματα: (ArrayList<User> currentLevelUsers, ArrayList<User> remainingUsers, int longestPath)
	 * Επειδή στη ρίζα του δέντρου (επίπεδο 0) περιλαμβάνεται μόνο ο χρήστης root, καλείται η συγκεκριμένη περίπτωση της μεθόδου 
	 * για να δημιουργηθεί η λίστα currentLevelUsers με μοναδικό μέλος το χρήστη root, και στη συνέχεια να δημιουργηθεί το υπόλοιπο δέντρο.
	 */
	private int traverseNetworkContacts(User root, ArrayList<User> remainingUsers, int longestPath) {
		ArrayList<User> currentLevelUser=new ArrayList<>();
		currentLevelUser.add(root);
		return traverseNetworkContacts(currentLevelUser, remainingUsers, longestPath);
	}

	
	/* 
	 * Η συγκεκριμένη μέθοδος καλείται αναδρομικά για κάθε επίπεδο του δέντρου που δημιουργείται.
	 * Κάθε αναδρομή προσθέτει ένα επιπλέον επίπεδο στο δέντρο, ενώ στο τέλος επιστρέφεται η τιμή του βάθους του δέντρου, 
	 * και κατά συνέπεια η μεγαλύτερη απόσταση του root από τους υπόλοιπους χρήστες του δικτύου
	 * 
	 * Οι currentLevelUsers είναι οι χρήστες που εξετάζονται στο τρέχον επίπεδο του δέντρου.
	 * 
	 * Η λίστα remainingUsers περιέχει αρχικά όλους τους χρήστες του δικτύου. Κατά την δημιουργία του κάθε επιπέδου,
	 * αφαιρούνται οι χρήστες του currentLevelUsers από την remainingUsers, καθώς έχει πλέον καθοριστεί η απόστασή τους από τον root.
	 * Μετά το πέρας της δημιουργίας του δέντρου, η remainingUsers καταλήγει χωρίς μέλη.
	 * 
	 * Το longestPath είναι η μεταβλητή που τελικά επιστρέφει την τιμή της στην rootUserLength. Ως τιμή της ορίζεται το τρέχον επίπεδο του δέντρου. 
	 */
	private int traverseNetworkContacts(ArrayList<User> currentLevelUsers, ArrayList<User> remainingUsers, int longestPath) {
		
		/*
		 * Η nextLevelUsers περιέχει τους φίλους των χρηστών στην currentLevelUsers, οι οποίοι φίλοι ωστόσο πληρούν ορισμένες προϋποθέσεις προκειμένου
		 * να εισαχθούν σε αυτήν, τις οποίες εξετάζει η μέθοδος willBecomeNextLevelUser().
		 * Αφού οριστεί η nextLevelUsers, δίνεται ως όρισμα στην νέα κλήση της traverseNetworkContacts() στη θέση της currentLevelUsers,
		 * προκειμένου να προχωρήσει στο επόμενο επίπεδο του δέντρου και να επαναληφθεί η διαδικασία.
		 * Όταν η nextLevelUsers δεν περιέχει κανένα μέλος, σημαίνει πως η δημιουργία του δέντρου ολοκληρώθηκε, και επιστρέφεται η τρέχουσα τιμή του longestPath. 
		 */
		ArrayList<User> nextLevelUsers=new ArrayList<>();
	
		for (User u:currentLevelUsers) {
			remainingUsers.remove(u);
			for (User friend:u.getFriends()) {
				if (willBecomeNextLevelUser(friend, nextLevelUsers, currentLevelUsers, remainingUsers))
					nextLevelUsers.add(friend);
			}
		}
		
		if (!nextLevelUsers.isEmpty())
			return traverseNetworkContacts(nextLevelUsers, remainingUsers, longestPath+1);
		else
			return longestPath;
	}

	/*
	 * Εξετάζει αν ένας χρήστης friend θα προστεθεί στην nextLevelUsers.
	 * Αυτό θα γίνει αν καμία από τις ακόλουθες προϋποθέσεις δεν ισχύει. Όλες τους αναφέρονται σε συγκεκριμένα επίπεδα του δέντρου:
	 * 1) Να περιέχεται ήδη ο friend στην nextLevelUsers. Δεν επιτρέπονται επαναλήψεις χρηστών στη λίστα.
	 * 2) Να περιέχεται ο friend στην currentLevelUsers. Αν βρίσκεται σε αυτήν, σημαίνει πως έχει καθοριστεί η θέση του στο δέντρο στο τρέχον επίπεδο.
	 * 3) Να μην περιέχεται στην remainingUsers. Αν δεν περιέχεται σε αυτήν, σημαίνει πως έχει ήδη καθοριστεί η θέση του στο δέντρο, 
	 * 	  είτε στο τρέχον επίπεδο (currentLevelUsers) είτε σε κάποιο προηγούμενο. 

	 * (Η διαφορά της συνθήκης 3 από την 2 είναι ότι ο χρήστης μπορεί να περιέχεται στην currentLevelUsers αλλά όχι στην remainingUsers κατά τον έλεγχο,
	 * καθώς η <for (User u:currentLevelUsers)> κάνει iterate πάνω στην currentLevelUsers και δεν επιτρέπεται η αλλαγή της, 
	 * κάτι το οποίο καλύπτει η remainingUsers με την ενημέρωσή της με τον ορισμό νέου χρήστη u.)
	 * 
	 * Αν δεν ισχύει καμία από τις προϋποθέσεις, ο friend μπορεί να προστεθεί στην nextLevelUsers.
	 */
	private boolean willBecomeNextLevelUser(User friend, ArrayList<User> nextLevelUsers, ArrayList<User> currentLevelUsers, ArrayList<User> remainingUsers) {
		
		if (nextLevelUsers.contains(friend)) return false;
		if (currentLevelUsers.contains(friend)) return false;
		if (!remainingUsers.contains(friend)) return false;
		
		return true;
	}
	
	//---------------------------
	
	/*
	 * Εναλλακτικός constructor. Δημιουργεί το DEFAULT_FILE από την αρχή, και του αναθέτει τα κατάλληλα στοιχεία για την έναρξη του προγράμματος.
	 */
	
	public Database(Object o) {
		PamakbookErgasia2Status.setUpErgasia2Status(this);
		createDefaultPamakbookStatusFile(this);
	}
	
	private void createDefaultPamakbookStatusFile(Database database) {
		
		try {
			FileOutputStream fileOut = new FileOutputStream(DEFAULT_FILE);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(database);
			
			out.close();
			fileOut.close();
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}