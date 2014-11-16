
public class DirectorGenre extends IMDBobject{

	private final int id;
	private final String genre;
	private final double rating;

	public DirectorGenre(int id, String genre, double rating){
		this.id = id;
		this.genre = genre;
		this.rating = rating;
	}

	public int getId() {
		return id;
	}

	public String getGenre() {
		return genre;
	}

	public double getRating() {
		return rating;
	}
}
