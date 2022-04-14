
public class Lesson {

	private int day;
	private int time;
	private Member member;
	private Instructor instructor;
	private Motorboat motorboat;

	public Lesson(int day, int time, Member member, Instructor instructor, Motorboat motorboat) {
		this.day = day;
		this.time = time;
		this.member = member;
		this.instructor = instructor;
		this.motorboat = motorboat;
	}

	public int getDay() {
		return day;
	}

	public int getTime() {
		return time;
	}

	public Member getMember() {
		return member;
	}

	public Instructor getInstructor() {
		return instructor;
	}

	public Motorboat getMotorboat() {
		return motorboat;
	}

}
