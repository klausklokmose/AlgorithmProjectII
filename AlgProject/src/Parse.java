import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Parse {

	public static void main(String[] args) throws FileNotFoundException {
		long start = System.currentTimeMillis();
		File f = new File("imdb-r.txt");
		BufferedReader br = new BufferedReader(new FileReader(f));
		Scanner scan = null;
		try {
			scan = new Scanner(br);
			ArrayList<ArrayList<IMDBobject>> l = new ArrayList<>();

			populateArray(scan, l);

			for (int i = 0; i < 3; i++) {
				ArrayList<IMDBobject> imdb = l.get(i);
				IMDBobject o1 = imdb.get(1);
				IMDBobject o2 = imdb.get(2);

				if (o1 instanceof Actor) {
					Actor a1 = (Actor) o1;
					Actor a2 = (Actor) o2;
//					System.out.println(a1.getId() + " " + a1.getfName() + " "
//							+ a1.getlName());
//					System.out.println(a2.getId() + " " + a2.getfName() + " "
//							+ a2.getlName());
				} else if (o1 instanceof Director) {
					Director d1 = (Director) o1;
					Director d2 = (Director) o2;
//					System.out.println(d1.getId() + " " + d1.getfName() + " "
//							+ d1.getlName());
//					System.out.println(d2.getId() + " " + d2.getfName() + " "
//							+ d2.getlName());
				} else if (o1 instanceof DirectorGenre) {
					DirectorGenre d1 = (DirectorGenre) o1;
					DirectorGenre d2 = (DirectorGenre) o2;
//					System.out.println(d1.getId() + " " + d1.getGenre() + " "
//							+ d1.getRating());
//					System.out.println(d2.getId() + " " + d2.getGenre() + " "
//							+ d2.getRating());
				} else if (o1 instanceof Movie) {
					Movie d1 = (Movie) o1;
					Movie d2 = (Movie) o2;
//					System.out.println(d1.getId() + " " + d1.getTitle() + " "
//							+ d1.getRating());
//					System.out.println(d2.getId() + " " + d2.getTitle() + " "
//							+ d2.getRating());
				}
			}
//			for (ArrayList<IMDBobject> arr : l) {
//				if (arr != null)
//					System.out.println(((Name) arr.get(0)).getName());
//			}
			// for (String str : l) {
			// System.out.println(str);
			// }
			System.out.println(" Run time of parsing: "+(System.currentTimeMillis()-start)/1000+" sec");
			System.out.println(l.get(0).size());
		} finally {
			scan.close();
		}

	}

	private static void populateArray(Scanner scan,
			ArrayList<ArrayList<IMDBobject>> l) {
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
				IMDBobject o = new Name(line);
				data.add(o);
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
				case "actors":
					Actor actor = new Actor(Integer.parseInt(s[0]), s[1], s[2],
							s[3].charAt(0), Integer.parseInt(s[4]));
					data.add(actor);
					break;
				case "directors":
					Director d = new Director(Integer.parseInt(s[0]), s[1],
							s[2]);
					data.add(d);
					break;
				case "directors_genres":
					DirectorGenre dg = new DirectorGenre(
							Integer.parseInt(s[0]), s[1],
							Double.parseDouble(s[2]));
					data.add(dg);
					break;
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
				// case "roles":
				// //movieID, ActorID, role
				// break;
				// default:
				// System.out.println("ERROR");
				// break;
				}
			}
		}
		l.add(data);
	}

}
