import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.*;

//Μέθοδοι 4ης εργασίας από τη γραμμή 184

public class UserPageGUI extends JFrame{

	//Όπως και στο SignInGUI, το frame ορίζεται προκειμένου το παράθυρο του UserPageGUI να κλείνει όταν χρησιμοποιηθεί το backToLoginButton
	private JFrame frame = this; 
	
	private JPanel panel = new JPanel(); 
	
	private JPanel userDataPanel=new JPanel(); 
	
	private JPanel postCreationPanel = new JPanel();
	
	private JPanel recentPostsPanel = new JPanel();
	private JTextArea recentPostsField;
	private ArrayList<Post> recentPosts = new ArrayList<>(); 

	private JPanel suggestedFriendsPanel = new JPanel();
	private JList suggestedFriendsList=new JList();
	
	private JPanel friendAddPanel = new JPanel();
	
	private JPanel groupPanel = new JPanel();
	
	private Database database;
	private User loggedInUser;
	
	//-----------Μέθοδοι 3ης εργασίας-----------
	
	public UserPageGUI(User user, Database databases) {
		loggedInUser=user;
		database=databases;
		
		panel.setLayout(new BorderLayout());
		
		createUserDataPanel(); 
		createPostCreationPanel();
		createRecentsPostsPanel();
		createFriendAddPanel();
		createSuggestedFriendsPanel();
		createGroupPanel();
		createExternalPanel();
		
		this.setContentPane(panel);
		this.setSize(500,500);
		this.setTitle("Σελίδα Χρήστη");
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	//Δημιουργία πεδίων αναγνωριστικών χρήστη, καθώς και κουμπιού backToLoginButton
	
	private void createUserDataPanel() {
		
		JLabel nameField = new JLabel(loggedInUser.getName()); 
		JLabel emailField = new JLabel(loggedInUser.getEmail()); 
		JButton backToLoginButton = Methods.setUpBackToLoginScreenButton(frame, database);
		
		userDataPanel.add(nameField);
		userDataPanel.add(emailField);
		userDataPanel.add(backToLoginButton);
	}
	
	//Δημιουργία πεδίου εισαγωγής κειμένου προς ανάρτηση, καθώς και κουμπιού ανάρτησης postButton
	
	private void createPostCreationPanel() {
		
		JTextArea postField = new JTextArea("Create post", 5, 25);
		postField.setLineWrap(true);
		JScrollPane postFieldScroll = new JScrollPane(postField);
		
		JButton postButton = new JButton("Post");
		postButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Date timestamp= new Date();
				String postText = postField.getText();
				if (!postText.isBlank()) {
					new Post(loggedInUser, timestamp, postText);
					refreshRecentPosts();
				}
				else {
					Methods.popMessagePostIsEmpty(loggedInUser);
				}
			}		
		});
		
		postCreationPanel.add(postFieldScroll);
		postCreationPanel.add(postButton);
	}
	
	//Δημιουργία πεδίου προβολής πρόσφατων αναρτήσεων
	
	private void createRecentsPostsPanel() {
		
		recentPostsField = new JTextArea(10, 25);
		recentPostsField.setLineWrap(true);
		recentPostsField.setEditable(false);
		
		setTextToRecentPostsField();
		
		recentPostsPanel.add(new JLabel("Recent posts by friends"));
		recentPostsPanel.add(new JScrollPane(recentPostsField));
	}
	
	private void setTextToRecentPostsField() {
		recentPosts = loggedInUser.getPostsFromSelfAndFriendsSorted(); //Επιστρέφει τις αναρτήσεις του χρήστη και των φίλων του ταξινομημένες κατά timestamp
		
		if (recentPosts.isEmpty())
			addToRecentPostsField("No recent posts"); 
		else {
			formatPosts(recentPosts);
		}
	}
	
	//Μετατροπή όλων των posts σε String (βάσει ενδεδειγμένης μορφής) και προσθήκη του καθενός στο ArrayList recentPostsString
	private void formatPosts(ArrayList<Post> posts) 
	{ 
		ArrayList<String> recentPostsString = new ArrayList<>();
		
		for (Post post:posts)
			recentPostsString.add(post.formatPost());
		
		addToRecentPostsField(recentPostsString);
	}
	
	private void addToRecentPostsField(String text) { //Μόνο για μία γραμμή προκαθορισμένου κειμένου
		recentPostsField.setText(text);
	}
	
	private void addToRecentPostsField(ArrayList<String> recentPostsString) {
		recentPostsField.setText(null); //Το recentPostsField επαναγράφεται εξ ολοκλήρου με την προσθήκη νέας ανάρτησης,
										//επομένως ό,τι υπάρχει στο πεδίο πρέπει να σβηστεί πριν κάνει append τις αναρτήσεις
		
		for (String stringPost:recentPostsString)
			recentPostsField.append(stringPost + System.lineSeparator());
	}
	
	//Δημιουργία πεδίου προβολής προτεινόμενων φίλων 

	private void createSuggestedFriendsPanel() {
		
		setSuggestedFriendsList(); 
		
		suggestedFriendsPanel.add(new JLabel("Suggested Friends"));
		suggestedFriendsPanel.add(suggestedFriendsList);
	}

	private void setSuggestedFriendsList() {
		DefaultListModel<String> suggestedFriendsModel = new DefaultListModel();
		ArrayList<User> suggestedFriends = loggedInUser.getFriendSuggestions();
		
		for (User suggestedFriend:suggestedFriends)
			suggestedFriendsModel.addElement(suggestedFriend.getName());
		
		suggestedFriendsList.setModel(suggestedFriendsModel);
	}
	
	//Ορισμός του εξωτερικού πάνελ panel, πάνω στο οποίο θα τοποθετηθούν τα πάνελ που δημιουργήθηκαν παραπάνω
	
	private void createExternalPanel() {
		
		JPanel internalPanel = new JPanel(); //Δημιουργία επιπλέον πάνελ για καλύτερη αισθητική εμφάνιση των ξεχωριστών πάνελ στο BorderLayout
		internalPanel.add(postCreationPanel);
		internalPanel.add(recentPostsPanel);
		internalPanel.add(friendAddPanel);
		internalPanel.add(suggestedFriendsPanel);
		
		panel.add(userDataPanel, BorderLayout.NORTH); 
		panel.add(internalPanel, BorderLayout.CENTER);
		panel.add(groupPanel, BorderLayout.SOUTH);
	}
	
	//-----------Μέθοδοι 4ης εργασίας-----------
	
	//Δημιουργία πεδίου κειμένου για προσθήκη νέων φίλων, καθώς και ανάλογου κουμπιού για τη διαδικασία
	
	private void createFriendAddPanel() {
		JTextField nameField = new JTextField("Add new friend", 10);
		JButton addFriendButton = new JButton("Add Friend");
		
		addFriendButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String userName = nameField.getText(); 
				User usernameFieldUser = database.containsUser(userName);
				
				if (usernameFieldUser==null) {
					Methods.popMessageUserNotFound(userName);
				}
				else if (loggedInUser.isFriendsWith(usernameFieldUser)) {
					Methods.popMessageUsersAlreadyFriends(loggedInUser, usernameFieldUser);
				}
				else {
					loggedInUser.addFriend(usernameFieldUser); //Η addFriend φροντίζει για την αμφίδρομη προσθήκη φιλίας
					
					refreshSuggestedList(); //Ανανέωση λίστας προτινόμενων φίλων 
					refreshRecentPosts(); //Ανανέωση λίστας πιο πρόσφατων αναρτήσεων
					database.calcDatabaseGraphDiameter(); //Υπολογισμός διαμέτρου του δικτύου
				}	
			}		
		});
		
		friendAddPanel.add(nameField);
		friendAddPanel.add(addFriendButton);
	}
	
	private void refreshSuggestedList() {
		setSuggestedFriendsList();
	}
	
	private void refreshRecentPosts() {
		setTextToRecentPostsField();
	}
	
	//Δημιουργία λίστας των διαθέσιμων γκρουπ, καθώς και ανάλογου κουμπιού για τη διαδικασία
	
	private void createGroupPanel() {
		
		JButton joinGroupButton = new JButton("Join");
		JList groupList = new JList();
		DefaultListModel<String> groupsModel = new DefaultListModel();
		ArrayList<Group> groups = database.getGroups();

		for (Group group:groups)
			groupsModel.addElement(group.getName());
		
		groupList.setModel(groupsModel);
		
		joinGroupButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String groupName = (String) groupList.getSelectedValue();
				Group selectedGroup = null;
				
				for (Group group:groups) {
					if (group.getName().equals(groupName)) {
						selectedGroup=group;
						group.addMember(loggedInUser);
					}
				}
			}		
		});
		
		groupPanel.add(new JLabel("Groups"));
		groupPanel.add(groupList);
		groupPanel.add(joinGroupButton);
	}
}