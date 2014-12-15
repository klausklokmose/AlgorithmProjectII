import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Parse {

	private static ArrayList<ArrayList<IMDBobject>> l = new ArrayList<>();
	
	
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
			CyclicBarrier barrier = new CyclicBarrier(4);
			
			//find the movies we want to look at
			for (int i = 1; i < l.get(3).size(); i++) {
//				if(barrier.getNumberWaiting()==1){
//					break;
//				}
				switch (((Movie) l.get(3).get(i)).getId()){
					case 18979:
						movies.add((Movie) l.get(3).get(i));  //apollo 13
						barrier.await();
						System.out.println("Added movie: "+18979);
					break;
					case 117874:
						movies.add((Movie) l.get(3).get(i)); //forest gump
						barrier.await();
						System.out.println("Added movie: "+117874);
					break;
					case 134077:
						movies.add((Movie) l.get(3).get(i)); //the green mile
						barrier.await();
						System.out.println("Added movie: "+134077);
					break;
						
				}
			}
			//add actors to the movies
			for (int i = 0; i < l.get(6).size(); i++) {
				Role r = (Role) l.get(6).get(i);
				for (int j = 0; j < movies.size(); j++) {
					if(r.getMovieID() == movies.get(j).getId()){
						movies.get(j).addActor(r.getActorID());
						System.out.println("added actor: "+r.getActorID());
					}
				}
			}
			
			//print cast of movies
			for (int i = 0; i < movies.size(); i++) {
				System.out.println("MOVIE: "+movies.get(i).getTitle()+" -------------------------------------");
				for (int j = 0; j < movies.get(i).getCast().size(); j++) {
					System.out.println(movies.get(i).getCast().get(j));
				}
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
					data.add(m);
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
					data.add(r);
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
