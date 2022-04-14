
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Store {

	private List<Member> members;
	private List<Lesson> lessons;
	private List<Instructor> instructors;
	private List<Motorboat> motorboats;
	private List<Hire> hires;

	public Store(
			List<Member> members,
			List<Instructor> instructors,
			List<Motorboat> motorboats) {
		this.members = members;
		this.instructors = instructors;
		this.motorboats = motorboats;

		this.lessons = new ArrayList<>();
		this.hires = new ArrayList<>();
	}

	public List<MBLH> getMBLHMembers() {
		return members.stream()
				.filter(m -> m.getType().equals("MBLH"))
				.map(m -> (MBLH) m)
				.collect(Collectors.toList());
	}

	public List<Lesson> getLessonsForMember(Member m) {
		return lessons.stream()
				.filter(l -> l.getMember() == m)
				.collect(Collectors.toList());
	}

	public List<Hire> getHiresForMember(Member m) {
		return hires.stream()
				.filter(h -> h.getMember() == m)
				.collect(Collectors.toList());

	}

	public List<Lesson> getLessonsForInstructor(Instructor i) {
		return lessons.stream()
				.filter(l -> l.getInstructor() == i)
				.collect(Collectors.toList());
	}

	public boolean memberHasLesson(Member m, int day, int time) {
		return lessons.stream()
				.filter(l -> l.getMember() == m
				&& l.getDay() == day
				&& l.getTime() == time)
				.findAny()
				.isPresent();
	}

	public boolean instructorHasLesson(Instructor ins, int day, int time) {
		return lessons.stream()
				.filter(l -> l.getInstructor() == ins
				&& l.getDay() == day
				&& l.getTime() == time)
				.findAny()
				.isPresent();
	}

	public boolean mblhHasHire(MBLH m, int day, int time) {
		return hires.stream()
				.filter(h -> h.getMember() == m
				&& h.getDay() == day
				&& h.getTime() == time)
				.findAny()
				.isPresent();
	}

	public List<Motorboat> getAvailableMotorboats(int day, int time) {
		return motorboats.stream()
				.filter(m -> isMotorboatAvailable(m, day, time))
				.collect(Collectors.toList());
	}

	public boolean anyMotorboatsAvailable(int day, int time) {
		return motorboats.stream()
				.filter(m -> isMotorboatAvailable(m, day, time))
				.findAny()
				.isPresent();
	}

	public boolean isMotorboatAvailable(Motorboat m, int day, int time) {
		boolean inALesson = lessons.stream()
				.filter(l -> l.getMotorboat() == m
				&& l.getDay() == day
				&& l.getTime() == time)
				.findAny()
				.isPresent();

		boolean inAHire = hires.stream()
				.filter(h -> h.getMotorboat() == m
				&& h.getDay() == day
				&& h.getTime() == time)
				.findAny()
				.isPresent();

		return !(inAHire || inALesson);
	}

	public List<Member> getMembers() {
		return members;
	}

	public List<Lesson> getLessons() {
		return lessons;
	}

	public List<Instructor> getInstructors() {
		return instructors;
	}

	public List<Hire> getHires() {
		return hires;
	}

	public List<Motorboat> getMotorboats() {
		return motorboats;
	}

}
