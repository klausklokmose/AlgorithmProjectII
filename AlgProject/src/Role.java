
public class Role extends IMDBobject{


	private final int movieID;
	private final int actorID;
	
	public Role(int movieID, int actorID){
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
