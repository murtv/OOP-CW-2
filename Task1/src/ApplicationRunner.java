
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ApplicationRunner {

	public static void main(String[] args) {
		Path filePath = Paths.get("booker-data.txt");
		Scanner s = new Scanner(System.in);

		List<BookerPrize> prizes = readBookerData(filePath);
		Collections.sort(prizes, (a, b) -> a.getYear() - b.getYear());

		while (true) {
			printPrompt();

			int in = s.nextInt();

			switch (in) {
				case 0:
					System.exit(0);
				case 1:
					printPrizeTable(prizes);
					break;
				case 2:
					System.out.print("Enter year: ");
					int year = s.nextInt();
					Optional<BookerPrize> prize = getPrizeByYear(prizes, year);

					if (prize.isPresent()) {
						System.out.println(prize.get().toString());
					} else {
						System.out.println("No prize for that year found.");
					}

					break;
				case 3:
					System.out.print("Enter keywords: ");
					String kIn = s.next();
					printSearchTable(searchBookByTitle(prizes, kIn));
					break;
				default:
					break;
			}
		}
	}

	static void printPrompt() {
		System.out.println("----------------------");
		System.out.println("Booker prize menu");
		System.out.println("----------------------");
		System.out.println("List ................1");
		System.out.println("Select ..............2");
		System.out.println("Search ..............3");
		System.out.println("Exit.................0");
		System.out.println("----------------------");
		System.out.print("Enter choice:> ");
	}

	static List<BookerPrize> readBookerData(Path filePath) {
		try {
			BufferedReader reader = Files.newBufferedReader(filePath);

			List<BookerPrize> prizes = new ArrayList<>();

			String winnerLine;
			while ((winnerLine = reader.readLine()) != null) {
				BookerPrize prize = new BookerPrize();

				parseWinnerLine(prize, winnerLine);
				parseShortListLine(prize, reader.readLine());
				parsePanelLine(prize, reader.readLine());

				prizes.add(prize);
			}

			return prizes;
		} catch (IOException e) {
			System.exit(0);
		}

		return null;
	}

	static void parseWinnerLine(BookerPrize prize, String line) {
		String[] parts = line.split(":");
		String year = parts[0];
		String bookLine = parts[1];

		prize.setYear(Integer.parseInt(year));
		prize.setWinner(parseBookLine(bookLine));
	}

	static void parseShortListLine(BookerPrize prize, String line) {
		String[] parts = line.split("\\|");

		for (String part : parts) {
			prize.getShortList().add(parseBookLine(part));
		}
	}

	static void parsePanelLine(BookerPrize prize, String line) {
		String[] parts = line.split(",");

		for (String part : parts) {
			if (part.contains("(chair)")) {
				prize.setChairPerson(part.trim());
			} else {
				prize.getPanel().add(part.trim());
			}
		}
	}

	static Book parseBookLine(String line) {
		String[] parts = line.split(",");
		String author = parts[0].trim();
		String fullTitle = parts[1].trim();
		String title = fullTitle.substring(0, fullTitle.indexOf("("));
		String publisher = fullTitle.substring(
				fullTitle.indexOf("(") + 1, fullTitle.indexOf(")"));

		return new Book(title, author, publisher);
	}

	static List<SearchResult> searchBookByTitle(
			List<BookerPrize> prizes, String query) {
		query = query.toLowerCase();
		List<SearchResult> results = new ArrayList<>();

		Pattern pattern = Pattern.compile(query, Pattern.CASE_INSENSITIVE);

		for (BookerPrize prize : prizes) {
			Book winner = prize.getWinner();
			if (pattern.matcher(winner.getTitle()).find()) {
				results.add(new SearchResult(
						winner.getTitle().toLowerCase()
								.replace(query, query.toUpperCase()),
						winner.getAuthor(),
						"Winner",
						Integer.toString(prize.getYear())));
			}

			for (Book book : prize.getShortList()) {
				if (pattern.matcher(book.getTitle()).find()) {
					results.add(new SearchResult(
							book.getTitle().toLowerCase()
									.replace(query, query.toUpperCase()),
							book.getAuthor(),
							"Shortlisted",
							Integer.toString(prize.getYear())));
				}
			}
		}

		return results;
	}

	static Optional<BookerPrize> getPrizeByYear(
			List<BookerPrize> prizes, int year) {
		return prizes.stream()
				.filter(p -> p.getYear() == year)
				.findFirst();
	}

	static void printPrizeTable(List<BookerPrize> prizes) {
		List<String> cols = Arrays.asList(
				"Year", "Title", "Author", "Publisher");

		// convert the row object into a list of string values
		List<List<String>> rows = prizes.stream()
				.map(p -> Arrays.asList(
				Integer.toString(p.getYear()),
				p.getWinner().getTitle(),
				p.getWinner().getAuthor(),
				p.getWinner().getPublisher()))
				.collect(Collectors.toList());

		TableUtils.print(cols, rows);
	}

	static void printSearchTable(List<SearchResult> results) {
		List<String> cols = Arrays.asList("Title", "Author", "Status", "Year");

		// convert the row object into a list of string values
		List<List<String>> rows = results.stream()
				.map(r -> {
					List<String> row = new ArrayList<>();
					row.add(r.getTitle());
					row.add(r.getAuthor());
					row.add(r.getStatus());
					row.add(r.getYear());

					return row;
				})
				.collect(Collectors.toList());

		TableUtils.print(cols, rows);
	}
}
