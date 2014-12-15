import java.util.ArrayList;


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

	private String fName;
	private String lName;
	private char gender;
	private int numMovies;
	private ArrayList<Integer> actors = new ArrayList<>();
	
	public ArrayList<Integer> getActorsPlayedWith(){
		return actors;
	}
	
	public int getActorsSize(){
		return actors.size();
	}
	public Actor(int id, ArrayList<Integer> actors){
		this.id = id;
		this.actors = actors;
		for (int i = 0; i < actors.size(); i++) {
			if(actors.get(i)==id){
				actors.remove(i);
			}
		}
	}
	
	public Actor(int id, String lName, String fName, char gender, int numMovies){
		this.id = id;
		this.fName = fName;
		this.lName = lName;
		this.gender = gender;
		this.numMovies = numMovies;
	}
}
