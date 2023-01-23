public class ClosedGroup extends Group {
	
	//-----------Μέθοδοι 2ης εργασίας-----------
	
	public ClosedGroup(String name, String description) {
		super(name, description);
	}

	public void addMember(User u) {
		
		if (this.members.isEmpty()) //Μόνο για το 1ο μέλος του κλειστού γκρουπ
			super.addMember(u);
		else if (!this.hasMember(u)) {	//Για 2ο μέλος και εξής, μπαίνει στο loop μόνο αν
										//ο χρήστης-όρισμα δεν είναι ήδη στο γκρουπ
			for (User member: members)
				if (member.isFriendsWith(u)) {
					super.addMember(u);
					return;
				}
			
			Methods.popMessageUserCannotEnrollInGroup(u, this);		
		}
		else 
			Methods.popMessageUserAlreadyInGroup(u, this);
	}
}
