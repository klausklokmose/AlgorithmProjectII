
public class Actor extends IMDBobject{
	final private int id;
	public int getId() {
		return id;
	}

	public String getfName() {
		return fName;
	}

	public String getlName() {
		return lName;
	}

	public char getGender() {
		return gender;
	}

	public int getNumMovies() {
		return numMovies;
	}

	final private String fName;
	final private String lName;
	final private char gender;
	final private int numMovies;
	
	public Actor(int id, String lName, String fName, char gender, int numMovies){
		this.id = id;
		this.fName = fName;
		this.lName = lName;
		this.gender = gender;
		this.numMovies = numMovies;
	}
}
