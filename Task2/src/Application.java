
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Application {

	private Store store;
	private Scanner input;

	public Application(Store store) {
		this.store = store;
		this.input = new Scanner(System.in);
	}

	public static void main(String[] args) {
		List<Member> members = Arrays.asList(
				new Novice("Novice 1",
						new Instructor("Instructor 1"))
		);

		Application app = new Application(
				new Store(members, null, null));
//		app.begin();
		app.printScheduleGrid(Arrays.asList(
				new Lesson(0, 10, null, null, null),
				new Lesson(1, 14, null, null, null),
				new Lesson(2, 18, null, null, null)
		));
	}

	public void begin() {
		while (true) {
			printMenu(
					"Motorboat Club Menu",
					"List member lessons",
					"Book member lesson",
					"List instructor lessons",
					"Hire motorboat",
					"List motorboat bookings",
					"Exit");
			System.out.print("> ");

			int in = input.nextInt();

			switch (in) {
				case 0:
					handleListMemberLessons();
					break;
				case 1:
					handleBookMemberLesson();
					break;
				case 2:
					handleListInstructorLessons();
					break;
				case 3:
					handleListMotorboatBookings();
					break;
				case 4:
					handleHireMotorboat();
					break;
				case 5:
					System.exit(0);
				default:
					break;
			}
		}

	}

	void handleBookMemberLesson() {
		Member m = getMemberInput();

		if (m == null) {
			System.out.println("Member not found.");
			return;
		}

		List<Lesson> lessons = store.getLessonsForMember(m);
		if (lessons.size() == 3) {
			System.out.println("Cannot book any more lessons for this member.");
			return;
		}

		Instructor ins;
		if ("NOVICE".equals(m.getType())) {
			Novice nm = (Novice) m;
			ins = nm.getInstructor();

			System.out.println("Instructor: " + ins.getName());
		} else {
			ins = getInstructorInput();
			if (ins == null) {
				System.out.println("Instructor not found.");
				return;
			}
		}

		printScheduleGrid(store.getLessonsForInstructor(ins));

		int[] dayTimeInput = getDayTimeInput();
		int day = dayTimeInput[0];
		int time = dayTimeInput[1];

		if (store.memberHasLesson(m, day, time)
				|| store.instructorHasLesson(ins, day, time)) {
			System.out.println("Member or instructor is already involved with a lesson at that day/time.");
			return;
		}

		if ("MBLH".equals(m.getType())
				&& store.mblhHasHire((MBLH) m, day, time)) {
			System.out.println("MBLH already has a motorboat hire at that day/time.");
			return;
		}

		if (!store.anyMotorboatsAvailable(day, time)) {
			System.out.println("No motorboat available for that day/time.");
			return;
		}

		Motorboat boat = getMotorboatInput(day, time);
		Lesson l = new Lesson(day, time, m, ins, boat);
		store.getLessons().add(l);
		printLessons(Arrays.asList(l));
	}

	Motorboat getMotorboatInput() {
		List<Motorboat> motorboats = store.getMotorboats();
		return getMotorboatInput(motorboats);
	}

	Motorboat getMotorboatInput(int day, int time) {
		List<Motorboat> motorboats = store.getAvailableMotorboats(day, time);
		return getMotorboatInput(motorboats);
	}

	Motorboat getMotorboatInput(List<Motorboat> motorboats) {
		List<List<String>> motorboatRows = new ArrayList<>();

		for (int i = 0; i < motorboats.size(); i++) {
			Motorboat m = motorboats.get(i);
			motorboatRows.add(Arrays.asList(
					Integer.toString(i), m.getName()));
		}

		System.out.println("Pick motorboat");

		TableUtils.print(Arrays.asList("Index", "Name"), motorboatRows);

		System.out.print("Motorboat index:> ");

		int in = input.nextInt();

		if (in >= motorboats.size()) {
			return null;
		}

		return motorboats.get(in);
	}

	Member getMemberInput() {
		List<Member> members = store.getMembers();
		return getMemberInput(members);
	}

	<T extends Member> T getMemberInput(List<T> members) {
		List<List<String>> memberRows = new ArrayList<>();

		for (int i = 0; i < members.size(); i++) {
			Member m = members.get(i);
			memberRows.add(Arrays.asList(
					Integer.toString(i), m.getName(), m.getType()));
		}

		System.out.println("Pick member");

		TableUtils.print(
				Arrays.asList("Index", "Name", "Type"), memberRows);

		System.out.print("Member index:> ");

		int in = input.nextInt();

		if (in >= members.size()) {
			return null;
		}

		return members.get(in);
	}

	Instructor getInstructorInput() {
		List<Instructor> instructors = store.getInstructors();
		List<List<String>> instructorRows = new ArrayList<>();

		for (int i = 0; i < instructors.size(); i++) {
			Instructor ins = instructors.get(i);
			instructorRows.add(Arrays.asList(
					Integer.toString(i), ins.getName()));
		}

		System.out.println("Pick instructor");

		TableUtils.print(Arrays.asList("Index", "Name"), instructorRows);

		System.out.print("Instructor index:> ");

		int in = input.nextInt();

		if (in >= instructors.size()) {
			return null;
		}

		return instructors.get(in);
	}

	private void handleListMemberLessons() {
		Member m = getMemberInput();

		if (m == null) {
			System.out.println("Member not found.");
			return;
		}

		List<Lesson> lessons = store.getLessonsForMember(m);

		if (lessons.isEmpty()) {
			System.out.println("No lessons for member.");
			return;
		}

		printLessons(lessons);
	}

	private void handleListInstructorLessons() {
		Instructor ins = getInstructorInput();

		if (ins == null) {
			System.out.println("Instructor not found.");
			return;
		}

		List<Lesson> lessons = store.getLessons().stream()
				.filter(l -> l.getInstructor() == ins)
				.collect(Collectors.toList());

		if (lessons.isEmpty()) {
			System.out.println("No lessons for instructor.");
			return;
		}

		printLessons(lessons);
	}

	private void handleListMotorboatBookings() {
		Motorboat m = getMotorboatInput();

		if (m == null) {
			System.out.println("Motorboat not found.");
			return;
		}

	}

	void handleHireMotorboat() {
		List<MBLH> mblhs = store.getMBLHMembers();

		MBLH m = getMemberInput(mblhs);

		if (m == null) {
			System.out.println("Member not found.");
			return;
		}

		List<Hire> hires = store.getHiresForMember(m);
		if (hires.size() == 3) {
			System.out.println("Hire limit reached.");
			return;
		}

		int[] dayTimeInput = getDayTimeInput();
		int day = dayTimeInput[0];
		int time = dayTimeInput[1];

		if (store.memberHasLesson(m, day, time)) {
			System.out.println("Member is already involved with lesson at that day/time.");
			return;
		}

		if (!store.anyMotorboatsAvailable(day, time)) {
			System.out.println("No motorboats available at that day/time.");
			return;
		}

		Motorboat boat = getMotorboatInput(day, time);
		if (boat == null) {
			System.out.println("Motorboat not found.");
			return;
		}

		Hire hire = new Hire(day, time, boat, m);
		store.getHires().add(hire);
		printHires(Arrays.asList(hire));
	}

	int[] getDayTimeInput() {
		int[] in = new int[2];

		System.out.println("Booking day(0 - 6):> ");
		in[0] = input.nextInt();

		System.out.println("Booking time(9 - 18):> ");
		in[1] = input.nextInt();

		return in;
	}

	void printLessons(List<Lesson> lessons) {
		List<List<String>> lessonRows = new ArrayList<>();

		for (int i = 0; i < lessons.size(); i++) {
			Lesson l = lessons.get(i);
			lessonRows.add(Arrays.asList(
					Integer.toString(i),
					l.getMember().getName(),
					l.getInstructor().getName(),
					l.getMotorboat().getName(),
					Integer.toString(l.getDay()),
					Integer.toString(l.getTime())
			));
		}

		TableUtils.print(
				Arrays.asList("Member", "Instructor", "Motorboat", "Day", "Time"),
				lessonRows);
	}

	void printHires(List<Hire> hires) {
		List<List<String>> hireRows = new ArrayList<>();

		for (int i = 0; i < hires.size(); i++) {
			Hire h = hires.get(i);
			hireRows.add(Arrays.asList(
					Integer.toString(i),
					h.getMember().getName(),
					h.getMotorboat().getName(),
					Integer.toString(h.getDay()),
					Integer.toString(h.getTime())
			));
		}

		TableUtils.print(
				Arrays.asList("Member", "Motorboat", "Day", "Time"),
				hireRows);
	}

	void printScheduleGrid(List<Lesson> lessons) {
		String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
		int weekDaysColWidth = maxLength(days);
		System.out.print(padded("Day", weekDaysColWidth));

		System.out.print("|");
		for (int i = 9; i < 19; i++) {
			System.out.print(String.format(" %s |", i));
		}

		System.out.println();

		for (int i = 0; i < 7; i++) {
			System.out.print(padded(days[i], weekDaysColWidth));

			for (int j = 9; j < 19; j++) {
				if (findLesson(lessons, i, j).isPresent()) {
					System.out.print("  x  ");
				} else {
					System.out.print("     ");
				}
			}

			System.out.println();
		}
	}

	Optional<Lesson> findLesson(List<Lesson> lessons, int day, int time) {
		return lessons.stream()
				.filter(l -> l.getDay() == day
				&& l.getTime() == time)
				.findFirst();

	}

	int maxLength(String... strings) {
		return Stream.of(strings)
				.mapToInt(String::length)
				.max().getAsInt();
	}

	String padded(String s, int l) {
		if (s.length() > l) {
			return s;
		}

		return s + Utils.repeatString(" ", l - s.length());
	}

	void printMenu(String title, String... options) {
		int longestLength = Stream.of(options)
				.mapToInt(String::length)
				.max().getAsInt();

		int width = Math.max(40, longestLength + 4);

		String fullBoundary = Utils.repeatString("-", width);
		System.out.println(fullBoundary);
		System.out.println(title);
		System.out.println(fullBoundary);

		for (int i = 0; i < options.length; i++) {
			String option = options[i];
			String boundary = Utils.repeatString(".", width - option.length() - 3);
			System.out.println(
					option + " " + boundary + " " + Integer.toString(i));
		}
	}

}
