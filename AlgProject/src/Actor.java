import java.util.ArrayList;

public class Actor extends IMDBobject {
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

	private String fName;
	private String lName;
	private char gender;
	private int numMovies;
	private ArrayList<Integer> actors;

	public ArrayList<Integer> getActorsPlayedWith() {
		return actors;
	}

	public int getActorsSize() {
		return actors.size();
	}

	public Actor(int id, ArrayList<Integer> actors1) {
		this.id = id;
		this.actors = new ArrayList<Integer>();
		this.actors = (ArrayList<Integer>) actors1.clone();

		int indexToRemove = actors.indexOf(id);
		if(indexToRemove != -1){
			this.actors.remove(indexToRemove);
		}
	}

	public Actor(int id, String lName, String fName, char gender, int numMovies) {
		this.id = id;
		this.fName = fName;
		this.lName = lName;
		this.gender = gender;
		this.numMovies = numMovies;
	}
}
