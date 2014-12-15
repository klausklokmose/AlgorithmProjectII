import java.util.ArrayList;


public class Movie extends IMDBobject{
	private final int id;
	private final String title;
	private final int year;
	private final double rating;
	private final int something;
	private ArrayList<Integer> cast = new ArrayList<>();
	
	public Movie(int id, String title, int year, double rating, int something){
		this.id = id;
		this.title = title;
		this.year = year;
		this.rating = rating;
		this.something = something;
	}

	public void addActor(int id){
		cast.add(id);
	}
	
	public ArrayList<Integer> getCast(){
		return cast;
	}
	
	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public int getYear() {
		return year;
	}

	public double getRating() {
		return rating;
	}

	public int getSomething() {
		return something;
	}
}
