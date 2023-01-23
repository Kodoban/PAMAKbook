import java.io.Serializable;
import java.util.ArrayList;

public class Group implements Serializable {
	
	private String name;
	private String description;
	protected ArrayList<User> members=new ArrayList<>();
	
	//-----------Μέθοδοι 2ης εργασίας-----------
	
	public Group(String name, String description) {
		this.name=name;
		this.description=description;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean hasMember(User u) {
		return members.contains(u);
	}
	
	public void addMember(User u) {
		if (hasMember(u))
			Methods.popMessageUserAlreadyInGroup(u, this);
		else {
			members.add(u);
			u.enrollInGroup(this);
			Methods.popMessageUserEnrolledInGroup(u, this);
		}
 	}
	
	//Εναλλακτική εκδοχή της addMember(), χρησιμοποιείται από την κλάση PamakbookErgasia2Status
	public void addMember1(User u) {
		if (!hasMember(u)) {
			members.add(u);
			u.enrollInGroup(this);
		}
	}
 }