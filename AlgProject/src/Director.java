
public class Director extends IMDBobject{
	private final int id;
	private final String lName;
	private final String fName;
	
	public Director(int id, String lName, String fName){
		this.id = id;
		this.lName = lName;
		this.fName = fName;
	}

	public int getId() {
		return id;
	}

	public String getlName() {
		return lName;
	}

	public String getfName() {
		return fName;
	}

	
}
