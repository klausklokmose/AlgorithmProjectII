import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;

public class Parse {

	private static ArrayList<ArrayList<IMDBobject>> l = new ArrayList<>();
	private static HashMap<Integer, Movie> movs = new HashMap<>();

	public static void main(String[] args) throws FileNotFoundException,
			InterruptedException, BrokenBarrierException {
		long start = System.currentTimeMillis();
		File f = new File("imdb-r.txt");
		BufferedReader br = new BufferedReader(new FileReader(f));
		Scanner scan = null;
		try {
			scan = new Scanner(br);

			populateArray(scan);

			ArrayList<Movie> movies = new ArrayList<>();

			// find the movies we want to look at
			int[] ids = new int[] { 18979, 117874, 134077, 313459, 313460,
					313461, 342384, 20371, 82274, 267039, 65501, 411815,
					362312, 271095, 219030, 219739, 250737, 159167, 159172,
					159175, 347870, 330670, 121758, 333856, 31702, 30686,
					25192, 333856, 56871, 57654, 73574, 123849, 304765, 365450,
					846, 2229, 2888, 2893, 3021, 3053, 3055, 3730, 5941, 16251,
					17515, 17549, 18021, 23972, 24955, 31553, 33032, 33033,
					33046, 34646, 43451, 45250, 46857, 67228, 73983, 74674,
					74724, 78642, 85871, 91948, 92573, 92868, 93091, 103679,
					108261, 113489, 113550, 114127, 114152, 114606, 120290,
					120574, 122629, 130953, 131509, 138706, 138721, 139666,
					142364, 145044, 145046, 145372, 152905, 158006, 159172,
					159174, 159177, 160293, 160295, 160299, 160309, 160315,
					379269, 379418, 388268, 404721, 171094 };
			
			/*
			 * 160319, 160424, 161392, 165259, 165262, 165264, 165265,
					168469, 171094, 175469, 175542, 180741, 186637, 190755,
					194111, 198875, 202264, 203610, 204863, 206255, 209423,
					210620, 212025, 217434, 222727, 223076, 227153, 230394,
					231106, 233928, 234188, 238645, 240755, 245340, 257744,
					262959, 263206, 272230, 276089, 278830, 279720, 279757,
					280115, 281621, 291878, 291880, 292813, 295636, 297172,
					301801, 312287, 321711, 325150, 328188, 331343, 332065,
					333193, 333265, 340555, 345377, 347969, 365972, 366949,
					367567, 367815, 368426, 368949, 372233, 374028, 378842,
			 */
			for (int i : ids) {
				movies.add(movs.get(i));
			}
			System.out.println("number of movies: "+ids.length);
			// printCast(movies);
			ArrayList<Actor> actors = new ArrayList<>();

			for (int i = 0; i < movies.size(); i++) {
				ArrayList<Integer> ms = movies.get(i).getCast();
				for (int j = 0; j < ms.size(); j++) {
					actors.add(new Actor(ms.get(j), ms));
				}
			}

			// TODO union actors list
			HashMap<Integer, Actor> map = new HashMap<>();

			for (int i = 0; i < actors.size(); i++) {
				Actor thisActor = actors.get(i);
				if (map.get(thisActor.getId()) == null) {
					map.put(thisActor.getId(), thisActor);
				} else {
					Actor mapActor = map.get(thisActor.getId());
					ArrayList<Integer> mapActorsPlayedWith = mapActor
							.getActorsPlayedWith();
					ArrayList<Integer> aActorsPlayedWith = thisActor
							.getActorsPlayedWith();
					mapActorsPlayedWith.addAll(aActorsPlayedWith);

					HashMap<Integer, Integer> playedWith = new HashMap<>();
					ArrayList<Integer> actorsPlayedWith = new ArrayList<Integer>();
					for (int j = 0; j < mapActorsPlayedWith.size(); j++) {
						if (playedWith.get(mapActorsPlayedWith.get(j)) == null) {
							playedWith.put(mapActorsPlayedWith.get(j),
									mapActorsPlayedWith.get(j));
							actorsPlayedWith.add(playedWith.get(j));
						}
					}
					Actor newActor = new Actor(thisActor.getId(),
							actorsPlayedWith);
					map.put(newActor.getId(), newActor);
				}
			}
			actors.clear(); // clear old data because it is in the hashMap

			Iterator it = map.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pairs = (Map.Entry) it.next();
				Actor a = (Actor) pairs.getValue();
				actors.add(a);
				// System.out.println("ACTOR: "+a.getId() +
				// " has played with "+a.getActorsSize()+ " other actors");
				for (int i = 0; i < a.getActorsSize(); i++) {
					// System.out.print(a.getActorsPlayedWith().get(i)+", ");
				}
				// System.out.println();
				it.remove(); // avoids a ConcurrentModificationException
			}
			ArrayList<Integer> actorsID = new ArrayList<>();
			double[][] M = new double[actors.size()][actors.size()];
			for (int j = 0; j < M.length; j++) {
				Actor a = actors.get(j);
				for (int i = 0; i < M.length; i++) {
					if (a.getActorsPlayedWith().contains(actors.get(i).getId())) {

						M[j][i] = (double) 1 / (double) a.getActorsSize();
						// System.out.println("actor "+a.getId()+" has played with "+actors.get(i).getId());
					}
				}
				actorsID.add(a.getId());
			}
			// print actorID's
			for (int i = 0; i < actorsID.size(); i++) {
				System.out.print(actorsID.get(i) + " ");
			}

			System.out.println();

			for (int i = 0; i < M.length; i++) {
				for (int j = 0; j < M.length; j++) {
					System.out.print(M[i][j] + " ");
				}
				System.out.println();
			}
		} finally {
			scan.close();
		}

	}

	private static void printCast(ArrayList<Movie> movies) {
		for (int i = 0; i < movies.size(); i++) {
			System.out.println("MOVIE: " + movies.get(i).getTitle() + " ----- "
					+ movies.get(i).getCast().size()
					+ " --------------------------------");
			for (int j = 0; j < movies.get(i).getCast().size(); j++) {
				System.out.println(movies.get(i).getCast().get(j));
			}
		}
	}

	private static boolean actorIsNotAdded(ArrayList<Actor> actors,
			ArrayList<Actor> finalActors, int i) {
		return !finalActors.contains(actors.get(i));
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
				// IMDBobject o = new Name(line);
				// data.add(o);
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

					// System.out.println(line);
					if (i3 != -1) {
						temp = temp.replaceAll(",", ".");
						line = line.substring(0, i1) + temp
								+ line.substring(i2);
					} else if (i3 == -1) {
						int i4 = temp.lastIndexOf(","); // find index of ", The"
						if (i4 != -1 && i4 < temp.length()) {
							// 7399,'Broderie d\'art chez les Ursulines, c.
							// 1640: c. 1880, La',1984,NULL,179
							temp = temp.replaceAll(",", " ");
							temp = temp.substring(i4).trim() + " "
									+ temp.substring(0, i4).trim();
							line = line.substring(0, i1) + temp
									+ line.substring(i2);
							// System.out.println(rawline);
							// System.out.println(line);
						}
					}
				}
				line = line.replaceAll("'", "");
				String[] s = line.split(",");
				switch (workingSet) {
				// case "actors":
				// Actor actor = new Actor(Integer.parseInt(s[0]), s[1], s[2],
				// s[3].charAt(0), Integer.parseInt(s[4]));
				// data.add(actor);
				// break;
				// case "directors":
				// Director d = new Director(Integer.parseInt(s[0]), s[1],
				// s[2]);
				// data.add(d);
				// break;
				// case "directors_genres":
				// DirectorGenre dg = new DirectorGenre(
				// Integer.parseInt(s[0]), s[1],
				// Double.parseDouble(s[2]));
				// data.add(dg);
				// break;
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
						// System.out.println(rawline);
					}
					movs.put(m.getId(), m);
					// data.add(m);
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
						r = new Role(Integer.parseInt(s[0]),
								Integer.parseInt(s[1]));
					} catch (NumberFormatException e) {
						System.out.println("ROLE ERROR");
						// e.printStackTrace();
					}
					// System.out.println(r.getMovieID());
					Movie m1 = movs.get(r.getMovieID());
					if (m1 != null) {
						m1.addActor(r.getActorID());
						movs.put(r.getMovieID(), m1);
					} else {
						Movie newMovie = new Movie(r.getMovieID(), null, 0, 0,
								0);
						newMovie.addActor(r.getActorID());
						movs.put(r.getMovieID(), newMovie);
					}
					// data.add(r);
					break;
				// default:
				// System.out.println("ERROR");
				// break;
				}
			}
		}
		l.add(data);
	}
}
