import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

//Μέθοδοι 4ης εργασίας από τη γραμμή 124

public class User implements Serializable {
	private String name;
	private String email;
	private ArrayList<User> friends = new ArrayList<>();
	private ArrayList<User> friendsOfFriends = new ArrayList<>();
	private ArrayList<Group> groups = new ArrayList<>();
	private ArrayList<Post> posts = new ArrayList<>();
	
	//-----------Μέθοδοι 2ης εργασίας-----------
	
	public User(String name, String email) {
		email = email.replace(" ", ""); //Διόρθωση του format του email σε περίπτωση λάθους 
										//για ευκολότερο έλεγχο εγκυρότητας email (εδώ αχρείαστα κενά)
		
		if (Methods.properEmailFormat(email)) {//Έλεγχος εγκυρότητας email format
			this.name=name;
			this.email=email;
		}
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<User> getFriends() {
		return friends;
	}

	public String getEmail() {
		return email;
	}

	public void addFriend(User otherUser) {

		if (this.isFriendsWith(otherUser)) { //Έλεγχος αν οι δύο χρήστες είναι ήδη φίλοι
			Methods.popMessageUsersAlreadyFriends(this, otherUser);
			return;
		}
		
		if (this.samePersonAs(otherUser)) { //Έλεγχος αν οι δύο χρήστες είναι το ίδιο άτομο
			Methods.popMessageUsersAreSame();
			return;
		}
		
		/* 
		 * Οι refreshLists φροντίζουν για την ενημέρωση των λιστών του χρήστη που την καλούν, μαζί και των φίλων του χρήστη. 
		 * Οι λίστες που επηρεάζονται είναι πλήρως ενημερωμένες για όλους τους χρήστες πριν αξιοποιηθούν από διάφορες μεθόδους, 
		 * πχ όλες οι λίστες friendsOfFriends για τον υπολογισμό της διαμέτρου του γράφου. 
		 */
		this.refreshLists(otherUser);
		otherUser.refreshLists(this);
		Methods.popMessageUsersNowFriends(this, otherUser);
	}
	
	public boolean isFriendsWith(User otherUser) {
		return this.friends.contains(otherUser);
	}
	
	private boolean samePersonAs(User otherUser) {
		return this.equals(otherUser);
	}
	
	public void enrollInGroup(Group g) {
		this.groups.add(g);
	}
	
	public ArrayList<User> isInfected() {
		ArrayList<User> allContacts = new ArrayList<>();
		allContacts.addAll(friends);
		allContacts.addAll(friendsOfFriends);
		
		return allContacts;
	}
	
	private boolean hasInContactList(User otherUser) {	
	//Η μέθοδος χρησιμοποιείται για να ελέγξει αν ο otherUser:
		
		if (this.samePersonAs(otherUser)) return true;  			//1) Είναι ο ίδιος ο χρήστης this
		if (this.isFriendsWith(otherUser)) return true; 			//2) Είναι ήδη στη λίστα this.friends
		if (this.friendsOfFriends.contains(otherUser)) return true; //3) Είναι ήδη στη λίστα this.friendsOfFriends
		
		return false; 
	}
	
	//-----------Μέθοδοι 3ης εργασίας-----------
	
	public void addPost(Post post) {
		this.posts.add(post);
	}
	
	//Μέθοδος επιστροφής αναρτήσεων του χρήστη και των φίλων του, ταξινομημένες κατά φθίνουσα timestamp 
	@SuppressWarnings("unchecked")
	public ArrayList<Post> getPostsFromSelfAndFriendsSorted() {
	
		
		ArrayList<Post> allPosts = new ArrayList<>(); //Λίστα post που συλλέγει τα posts του χρήστη 
													 //και των φίλων του, αρχικά αταξινόμητα
		
		allPosts.addAll(this.posts); //Προσθήκη των post του χρήστη στη λίστα
		
		for (User friend:this.friends)
			allPosts.addAll(friend.posts);  //Προσθήκη των post του κάθε φίλου του χρήστη στη λίστα
				
		Collections.sort(allPosts); //Ταξινόμηση των post
		
		return allPosts;
	}
	
	/*
	 * Επιστρέφει την this.friendsOfFriends ως λίστα προτεινόμενων φίλων.
	 * Η λίστα είναι πλήρως ενημερωμένη χάρη στην refreshLists.
	 */
	public ArrayList<User> getFriendSuggestions() { 
		return this.friendsOfFriends;
	}
	
	//-----------Μέθοδοι 4ης εργασίας-----------
	
	public ArrayList<User> getAllContacts() {
		return this.isInfected();
	}
	
	/* 
	 * Ενημερώνει τις λίστες friends και friendsOfFriends του this, βάσει του newFriend.
	 * Ταυτόχρονα, ενημερώνονται οι λίστες friendsOfFriends των φίλων του χρήστη, 
	 * ώστε να προστεθεί ο newFriend σε αυτές, μέσω της νέας φιλίας του με το χρήστη this.
	 */
	private void refreshLists(User newFriend) { 		
		this.refreshFriendsList(newFriend);
		this.refreshFriendsOfFriendsList(newFriend);
	}

	/* 
	 * Προσθέτει τον newFriend στη λίστα this.friends, και τον αφαιρεί από την this.friendsOfFriends, αν βρίσκεται εκεί.
	 */
	private void refreshFriendsList(User newFriend) { 
		this.friends.add(newFriend);
		if (this.friendsOfFriends.contains(newFriend)) {
			this.friendsOfFriends.remove(newFriend);
		}
	}
	
	/* 
	 * Ενημερώνονται οι λίστες friendsOfFriends όλων των φίλων του this. 
	 * Αν ο newFriend δεν βρίσκεται στις επαφές των φίλων του χρήστη, προστίθεται στη λίστα 
	 * friendsOfFriends του κάθε χρήστη friend στη λίστα this.friends.
	 * Ταυτόχρονα, ο newFriend προσθέτει τον friend αυτό στη δική του λίστα friendsOfFriends.
	 */
	private void refreshFriendsOfFriendsList(User newFriend) { 
		
		for (User friend:this.friends) 
			if (!friend.hasInContactList(newFriend)) {
				friend.friendsOfFriends.add(newFriend);
				newFriend.friendsOfFriends.add(friend);
			}
	}
	
	//-----------------------------------------
	
	//Εναλλακτική εκδοχή της addFriend(), χρησιμοποιείται από την κλάση PamakbookErgasia2Status
	public void addFriend1(User otherUser) {

		if (this.isFriendsWith(otherUser)) { //Έλεγχος αν οι δύο χρήστες είναι ήδη φίλοι
			return;
		}
		
		if (this.samePersonAs(otherUser)) { //Έλεγχος αν οι δύο χρήστες είναι το ίδιο άτομο
			return;
		}
		
		this.refreshLists(otherUser);
		otherUser.refreshLists(this);
	}
}
