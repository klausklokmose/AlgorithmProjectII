
public class Movie extends IMDBobject{
	private final int id;
	private final String title;
	private final int year;
	private final double rating;
	private final int something;
	
	public Movie(int id, String title, int year, double rating, int something){
		this.id = id;
		this.title = title;
		this.year = year;
		this.rating = rating;
		this.something = something;
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
