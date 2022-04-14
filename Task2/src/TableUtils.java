
import java.util.ArrayList;
import java.util.List;

public class TableUtils {

	public static void print(List<String> cols, List<List<String>> data) {
		List<Integer> colWidths = colWidths(cols, data);
		String boundary = makeBoundary(colWidths);

		System.out.println(boundary);
		System.out.println(makeRow(cols, colWidths));
		System.out.println(boundary);

		data.forEach(row -> {
			System.out.println(makeRow(row, colWidths));
		});

		System.out.println(boundary);
	}

	// take the width of the longest cell as the width for the column
	public static List<Integer> colWidths(
			List<String> cols,
			List<List<String>> rows) {
		List<Integer> widths = new ArrayList<>();

		List<List<String>> data = new ArrayList<>(rows);
		data.add(cols);

		for (int i = 0; i < cols.size(); i++) {
			final int col = i;

			Integer width = data.stream()
					.map(row -> {
						try {
							return row.get(col);
						} catch (IndexOutOfBoundsException e) {
							return "";
						}
					})
					.mapToInt(String::length)
					.max().orElse(10);

			widths.add(width);
		}

		return widths;
	}

	public static String makeBoundary(List<Integer> colWidths) {
		int fullWidth = colWidths.stream()
				.mapToInt(i -> i)
				.sum();

		// the constants 3 and 1 are for the whitespace and pipe chars added
		return Utils.repeatString("-", fullWidth + 1 + (3 * colWidths.size()));
	}

	public static String makeRow(
			List<String> row,
			List<Integer> colWidths) {

		StringBuilder sb = new StringBuilder();
		sb.append("|");

		for (int i = 0; i < row.size(); i++) {
			String cell = row.get(i);
			Integer colWidth = colWidths.get(i);
			String padding = Utils.repeatString(" ", colWidth - cell.length());

			sb.append(" " + cell + padding + " |");
		}

		return sb.toString();
	}

}
