
public class Hire {

	private int day;
	private int time;
	private Motorboat motorboat;
	private MBLH member;

	public Hire(int day, int time, Motorboat motorboat, MBLH member) {
		this.day = day;
		this.time = time;
		this.motorboat = motorboat;
		this.member = member;
	}

	public MBLH getMember() {
		return member;
	}

	public int getDay() {
		return day;
	}

	public int getTime() {
		return time;
	}

	public Motorboat getMotorboat() {
		return motorboat;
	}

}
