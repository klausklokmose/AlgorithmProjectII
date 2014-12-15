
public class Role extends IMDBobject{


	private final int movieID;
	private final int actorID;
	
	public Role(int actorID, int movieID){
		this.movieID = movieID;
		this.actorID = actorID;
	}
	
	public int getMovieID() {
		return movieID;
	}

	public int getActorID() {
		return actorID;
	}
}
