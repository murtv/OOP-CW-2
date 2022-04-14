
public class Novice extends Member {

	private Instructor instructor;

	public Novice(String name, Instructor instructor) {
		super("NOVICE", name);
		this.instructor = instructor;
	}

	public Instructor getInstructor() {
		return instructor;
	}

}
