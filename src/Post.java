import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Post implements Comparable, Serializable {
	
	//-----------Μέθοδοι 3ης εργασίας-----------
	
	private User originalPoster;
	private Date timestamp;
	private String text;

	public Post(User originalPoster, Date timestamp, String text) {
		this.originalPoster = originalPoster;
		this.timestamp = timestamp;
		this.text = text;
		originalPoster.addPost(this);
	}
	
	public Date getTimestamp() {
		return timestamp;
	}
	
	/*
	 * Επιστρέφει μορφοποιημένη εκδοχή της ανάρτησης που κάλεσε τη μέθοδο 
	 */
	public String formatPost() { 
		return (this.originalPoster.getName() + ", " 
				+ this.getFormattedTimestamp(this.timestamp) + ", " 
				+ this.text);
	}
	
	/*
	 * Επιστροφή timestamp με την ενδεδειγμένη μορφή ημερομηνίας και ώρας ως String
	 */
	private String getFormattedTimestamp (Date timestamp) { 
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return (formatter.format(timestamp));
	}
	
	@Override
	public int compareTo(Object o) {
		Post otherPost = (Post) o;
		return otherPost.timestamp.compareTo(this.timestamp);
	}
}

