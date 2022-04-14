
public class SearchResult {

	private String title;
	private String author;
	private String status;
	private String year;

	public SearchResult(String title, String author, String status, String year) {
		this.title = title;
		this.author = author;
		this.status = status;
		this.year = year;
	}

	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	public String getStatus() {
		return status;
	}

	public String getYear() {
		return year;
	}

}
