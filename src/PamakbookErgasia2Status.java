import java.util.ArrayList;

/*
 * Η συγκεκριμένη κλάση χρησιμοποείται για τη δημιουργία και εγγραφής των δεδομένων της εργασίας 2 
 * στο DefaultFile στο φάκελο του project, στην περίπτωση που δεν βρίσκεται ήδη εκεί για να ανακτηθεί 
 */

public class PamakbookErgasia2Status {
	
	public static void setUpErgasia2Status(Database database) {
		
		User u1 = new User("Makis", "iis1998@uom.edu.gr");
		User u2 = new User("Petros", "ics1924@ uom.edu.gr");
		User u3 = new User("Maria", "iis2012@uom.edu.gr");
		User u4 = new User("Gianna", "iis19133@uom.edu.gr");
		User u5 = new User("Nikos", "dai1758@uom.edu.gr");
		User u6 = new User("Babis", "ics19104@uom.edu.gr");
		User u7 = new User("Stella", "dai1827@uom.edu.gr");
		User u8 = new User("Eleni", "ics2086@gmail.com");
		
		//Προσθήκη φίλων
		
		u1.addFriend1(u2); //Εναλλακτική εκδοχή της addFriend(), γραμμή 165 στην κλάση User
		u1.addFriend1(u5);
		
		u5.addFriend1(u6);
		
		u3.addFriend1(u4);
		u3.addFriend1(u2);
		
		u4.addFriend1(u6);
		
		u5.addFriend1(u3);
		
		u1.addFriend1(u6);
		
		u5.addFriend1(u2);
		
		u7.addFriend1(u1);
		
		Group g1 = new Group("WebGurus","A group for web passionates");
		ClosedGroup g2 = new ClosedGroup("ExamSolutions","Solutions to common exam questions");
		
		g1.addMember1(u4); //Εναλλακτική εκδοχή της addMember(), γραμμή 35 στην κλάση Group
		g1.addMember1(u3);
		g1.addMember1(u2);
		
		g2.addMember1(u4);
		g2.addMember1(u5);
		g2.addMember1(u6);
		g2.addMember1(u5);
		
		ArrayList<User> allUsers = new ArrayList<>();
		allUsers.add(u1);
		allUsers.add(u2);
		allUsers.add(u3);
		allUsers.add(u4);
		allUsers.add(u5);
		allUsers.add(u6);
		allUsers.add(u7);
		
		ArrayList<Group> allGroups = new ArrayList<>();
		allGroups.add(g1);
		allGroups.add(g2);
		
		database.setUsers(allUsers);
		database.setGroups(allGroups);
	}
}
