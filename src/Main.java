public class Main {

	public static void main(String[] args) {
		
//		Μόνο για την περίπτωση που δεν διαβάζεται κατάλληλα το DEFAULT_FILE
//		Καλείται εναλλακτικός constructor μέσα στον οποίο δημιουργείται εκ νέου το DEFAULT_FILE
//		Database class γραμμή 179 ?
//		Database database = new Database(null); 
		
		Database database = new Database();
		
		new SignInGUI(database);
	}
}
