
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BookerPrize {

	private int year;
	private Book winner;
	private List<Book> shortList = new ArrayList<>();
	private List<String> panel = new ArrayList<>();
	private String chairPerson;

	public void setYear(int year) {
		this.year = year;
	}

	public void setWinner(Book winner) {
		this.winner = winner;
	}

	public void setShortList(List<Book> shortList) {
		this.shortList = shortList;
	}

	public void setPanel(List<String> panel) {
		this.panel = panel;
	}

	public void setChairPerson(String chairPerson) {
		this.chairPerson = chairPerson;
	}

	public int getYear() {
		return year;
	}

	public Book getWinner() {
		return winner;
	}

	public List<Book> getShortList() {
		return shortList;
	}

	public List<String> getPanel() {
		return panel;
	}

	public String getChairPerson() {
		return chairPerson;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		// remove the (chair) suffix
		String chairPersonName = chairPerson
				.substring(0, chairPerson.indexOf("("));

		List<String> winnerRow = bookToRow(winner).stream()
				.map(String::toUpperCase)
				.collect(Collectors.toList());

		// dummy cells to get the pipe chars
		winnerRow.add("");
		winnerRow.add("");

		List<List<String>> shortListRows = shortList.stream()
				.map(b -> bookToRow(b))
				.collect(Collectors.toList());

		List<String> cols = Arrays.asList(
				"Author", "Book Title", "Publisher", "Chair", "Panel");

		List<List<String>> allRows = Stream.of(Arrays.asList(winnerRow), shortListRows)
				.flatMap(Collection::stream)
				.collect(Collectors.toList());

		List<Integer> colWidths = TableUtils.colWidths(cols, allRows);

		// set the chair and panel column widths manually
		colWidths.set(colWidths.size() - 2, chairPersonName.length());
		colWidths.set(colWidths.size() - 1, panel.stream()
				.mapToInt(String::length)
				.max().getAsInt());

		int colWidthsSum = colWidths.stream()
				.mapToInt(i -> i)
				.sum();

		int bookColWidthsSum = colWidths.stream()
				.limit(3)
				.mapToInt(i -> i)
				.sum();

		int boundaryLength = colWidthsSum + (3 * cols.size()) + 1;
		String boundary = TableUtils.repeatString("-", boundaryLength);

		int shortBoundaryLength = bookColWidthsSum + (3 * (cols.size() - 2)) + 1;
		String shortBoundary = TableUtils.repeatString("-", shortBoundaryLength);

		// print header
		sb.append(boundary).append("\n");
		sb.append(TableUtils.makeRow(cols, colWidths)).append("\n");
		sb.append(boundary).append("\n");

		// print winner book row
		sb.append(TableUtils.makeRow(winnerRow, colWidths)).append("\n");
		sb.append(shortBoundary).append("\n");

		// add the chair and panel cell values in the middle of the table 
		// and the rest with empty strings (to add the pipe chars)
		for (int i = 0; i < shortListRows.size(); i++) {
			List<String> row = shortListRows.get(i);

			if (i == (Math.floor(shortListRows.size() / 2) - 1)) {
				row.add(chairPersonName);
			} else {
				row.add("");
			}

			try {
				String panelMember = panel.get(i);
				row.add(panelMember);
			} catch (IndexOutOfBoundsException e) {
				row.add("");
			}
		}

		// print shortlist rows
		shortListRows.forEach(row -> {
			sb.append(TableUtils.makeRow(row, colWidths)).append("\n");
		});

		sb.append(boundary).append("\n");

		return sb.toString();
	}

	public List<String> bookToRow(Book b) {
		return new ArrayList<>(
				Arrays.asList(
						b.getAuthor(),
						b.getTitle(),
						b.getPublisher()));
	}
}
