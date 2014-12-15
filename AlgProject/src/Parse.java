import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Parse {

	private static ArrayList<ArrayList<IMDBobject>> l = new ArrayList<>();
	private static HashMap<Integer, Movie> movs = new HashMap<>();
	
	public static void main(String[] args) throws FileNotFoundException, InterruptedException, BrokenBarrierException {
		long start = System.currentTimeMillis();
		File f = new File("imdb-r.txt");
		BufferedReader br = new BufferedReader(new FileReader(f));
		Scanner scan = null;
		try {
			scan = new Scanner(br);

			populateArray(scan);
			
			System.out.println(" Run time of parsing: "+(System.currentTimeMillis()-start)/1000+" sec");
			for (int i = 0; i < l.size(); i++) {
				System.out.println(l.get(i).size()); 
			}
			
			ArrayList<Movie> movies = new ArrayList<>();
			
			//find the movies we want to look at
			
			movies.add(movs.get(18979));  //Apollo 13
			System.out.println("Added movie: "+18979);
			movies.add(movs.get(117874)); //forest gump
			System.out.println("Added movie: "+117874);
			movies.add(movs.get(134077)); //the green mile
			System.out.println("Added movie: "+134077);
			
			//print cast of movies
			for (int i = 0; i < movies.size(); i++) {
				System.out.println("MOVIE: "+movies.get(i).getTitle()+" ----- "+ movies.get(i).getCast().size()+ " --------------------------------");
				for (int j = 0; j < movies.get(i).getCast().size(); j++) {
					System.out.println(movies.get(i).getCast().get(j));
				}
			}
			ArrayList<Actor> actors = new ArrayList<>();
			
			for (int i = 0; i < movies.size(); i++) {
				ArrayList<Integer> ms = movies.get(i).getCast();
				for (int j = 0; j < ms.size(); j++) {
					actors.add(new Actor(ms.get(j), ms));
				}
			}
			
			//TODO union actors list
			
			double[][] M = new double[actors.size()][actors.size()];
			for (int j = 0; j < M.length; j++) {
				Actor a = actors.get(j);
				for (int i = 0; i < M.length; i++) {
					if(a.getActorsPlayedWith().contains(actors.get(i).getId())){
						M[j][i] = (double)1/(double)a.getActorsSize();
						System.out.println("actor "+a.getId()+" has played with "+actors.get(i).getId());
					}
				}
			}
			for (int i = 0; i < M.length; i++) {
				for (int j = 0; j < M.length; j++) {
					System.out.print(M[i][j]+" ");
				}
				System.out.println();
			}
		} finally {
			scan.close();
		}

	}

	private static void populateArray(Scanner scan) {
		String line;
		ArrayList<IMDBobject> data = null;
		String workingSet = null;
		while (scan.hasNextLine()) {
			line = scan.nextLine().trim();
			/*
			 * adds the name of the list to the array
			 */
			if (line.startsWith("LOCK")) {
				if (data != null) {
					l.add(data);
				}
				data = new ArrayList<>();
				line = line.split(" ")[2].replaceAll("`", "");
//				IMDBobject o = new Name(line);
//				data.add(o);
				workingSet = line;
			} else {
				// adds the data from the list to the array
				// "1994,'27th Annual Academy Awards, The',1955,NULL,136"
				// "3,'$1,000 Reward',1913,NULL,141"

				String rawline = line;
				
				line = line.replaceAll("[[\\\\]]'", "");
				int i1 = line.indexOf("'");
				int i2 = line.indexOf("'", i1 + 1);
				
				if (i1 != -1 && i2 != -1 && i1 != i2) {
					String temp = line.substring(i1, i2);
					int i3 = temp.indexOf("\\d*\",\"\\d*"); // check for numbers
														// such as 30,000 in the
														// title
					
//					System.out.println(line);
					if(i3 != -1){
						temp = temp.replaceAll(",", ".");
						line = line.substring(0, i1) + temp + line.substring(i2);
					}else if(i3 == -1) {
						int i4 = temp.lastIndexOf(","); // find index of ", The"
						if (i4 != -1 && i4 < temp.length()) {
							//7399,'Broderie d\'art chez les Ursulines, c. 1640: c. 1880, La',1984,NULL,179
							temp = temp.replaceAll(",", " ");
							temp = temp.substring(i4).trim() + " "
													+ temp.substring(0, i4).trim();
							line = line.substring(0, i1) + temp + line.substring(i2);
//							System.out.println(rawline);
//							System.out.println(line);
						}
					}
				}
				line = line.replaceAll("'", "");
				String[] s = line.split(",");
				switch (workingSet) {
//				case "actors":
//					Actor actor = new Actor(Integer.parseInt(s[0]), s[1], s[2],
//							s[3].charAt(0), Integer.parseInt(s[4]));
//					data.add(actor);
//					break;
//				case "directors":
//					Director d = new Director(Integer.parseInt(s[0]), s[1],
//							s[2]);
//					data.add(d);
//					break;
//				case "directors_genres":
//					DirectorGenre dg = new DirectorGenre(
//							Integer.parseInt(s[0]), s[1],
//							Double.parseDouble(s[2]));
//					data.add(dg);
//					break;
				case "movies":
					if (s[3].equals("NULL")) {
						s[3] = "-1";
					}
					Movie m = null;
					try {
						m = new Movie(Integer.parseInt(s[0]), s[1],
								Integer.parseInt(s[2]), 
								Double.parseDouble(s[3]),
								Integer.parseInt(s[4]));
					} catch (NumberFormatException e) {
						e.printStackTrace();
//						System.out.println(rawline);
					}
					movs.put(m.getId(), m);
//					data.add(m);
					break;
				// case "movies_directors":
				//
				// break;
				// case "movie_genres":
				//
				// break;
				 case "roles":
				// //movieID, ActorID, role
					Role r = null;
					try {
						r = new Role(Integer.parseInt(s[0]), Integer.parseInt(s[1]));
					} catch (NumberFormatException e) {
						System.out.println("ROLE ERROR");
//						e.printStackTrace();
					}
//					System.out.println(r.getMovieID());
					Movie m1 = movs.get(r.getMovieID());
					if(m1 != null){
						m1.addActor(r.getActorID());
						movs.put(r.getMovieID(), m1);
					}else{
						Movie newMovie = new Movie(r.getMovieID(), null, 0, 0, 0);
						newMovie.addActor(r.getActorID());
						movs.put(r.getMovieID(), newMovie);
					}
//					data.add(r);
					break;
//				 default:
//				 System.out.println("ERROR");
//				 break;
				}
			}
		}
		l.add(data);
	}
}
