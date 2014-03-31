import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;


public class MazeBuilder {
	
	char[] allDirections = {'N', 'E', 'S', 'W'};
	int direction;
	
	HashMap<Character, Integer> ordered = new HashMap<Character, Integer>();
	HashMap<Character, Integer> opposite = new HashMap<Character, Integer>();
	
	{
		ordered.put('N', 0); 	
		ordered.put('S', 1); 	
		ordered.put('W', 2); 	
		ordered.put('E', 3);
		
		opposite.put('S', 0); 	
		opposite.put('N', 1); 	
		opposite.put('E', 2); 	
		opposite.put('W', 3); 
	}
	
	int leastX;
	int largestX;
	int largestY;
	int currX; 
	int currY;
	
	private HashMap<String, int[]> hashMapMaze;// = new HashMap<String, String>();

	private void init() {
		hashMapMaze = new HashMap<String, int[]>();
		leastX = 0;
		largestX = 0;
		largestY = 0;
		currX = 0; 
		currY = 0;
		direction = 2;
	}

	public void fileReader(String file) {
		
		try {
			Scanner reader = new Scanner(new File(file));
			String line = reader.nextLine();
			int totalLines = Integer.parseInt(line);
			int i = 1;
			while(reader.hasNextLine() && i <= totalLines) {
				line = reader.nextLine();
				String[] input = line.split(" ");
				// get the 2 paths
				System.out.println("Case #" + i + ":");
				discoverMaze(input);
				/*
				init(); // reset the values of everything
				buildMaze(input[0], 2, currX, currY);
				buildMaze(input[1], direction, currX, currY);
				printMaze(leastX, largestX, largestY, hashMapMaze);
				*/
				i++;
				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	// You begin outside the maze!
	private void buildMaze(String path, int dir, int x, int y) {

		direction = dir;
		boolean justW = true;
		
		if(path.charAt(0) == 'W') {
			for(int i = 1; i < path.length(); i++) {
				int[] val;
				if(justW) {
					val = getVal(hashMapMaze, currX, currY);
					int change = opposite.get(allDirections[direction]);
					val[change] = 1;
					hashMapMaze.put(currX + "," + currY, val);
				}
				if(path.charAt(i) == 'W') {
					// System.out.println("Traveling " + allDirections[direction] + " current location " + currX + "," + currY); // for testing
					
					largestX 	= (currX > largestX)	? currX : largestX;
					leastX 		= (currX < leastX) 		? currX : leastX;
					largestY 	= (currY < largestY)	? currY : largestY;
					
					val = getVal(hashMapMaze, currX, currY);
					int change = ordered.get(allDirections[direction]);
					val[change] = 1;
					hashMapMaze.put(currX + "," + currY, val);
					int[] moveNext = getNextStep(allDirections[direction]);
					currX += moveNext[0];
					currY += moveNext[1];
					justW = true;
				}
				else if(path.charAt(i) == 'L') { // TURN LEFT
					direction = (direction - 1 < 0) ? 3 : direction - 1;
					justW = false;
				}
				else if(path.charAt(i) == 'R') { // TURN RIGHT
					direction = (direction + 1 > 3) ? 0 : direction + 1;
					justW = false;
				}
			}
		}
	}
	
	private int[] getVal(HashMap<String, int[]> hashMapMaze, int currX, int currY) {
		int[] val;
		if(hashMapMaze.get(currX + "," + currY) == null) {
			val = new int[]{0,0,0,0}; // make a new one
		}
		else {
			val = hashMapMaze.get(currX + "," + currY); // get the current
		}
		return val;
	}

	private void printMaze(int leastX, int largestX, int largestY, HashMap<String, int[]> maze) {
		int totalForCell = 0;
		for(int y = 0; y >= largestY; y--) {
			for(int x = leastX; x <= largestX; x++) {
				int[] val = maze.get(x + "," + y);
				for(int k = 0; k < 4; k++) { 						// 4 sides, N, E, S, W
					// System.out.print(val[k] + ","); // for testing
					totalForCell += (Math.pow(2, k) * val[k]);
				}
				if(totalForCell > 9) {
					System.out.print((char)(87 + totalForCell)); 	// prints A - F
				}
				else {
					System.out.print(totalForCell); 				// prints 0 - 9
				}
				// System.out.print("|"); // for testing
				totalForCell = 0;
			}
			System.out.println();
		}
	}
	
	private int[] getNextStep(char c) {
		int[] result = new int[2];
		switch (c) {
			case 'N':result[1] =  1; result[0] =  0; break;
			case 'E':result[1] =  0; result[0] =  1; break;
			case 'S':result[1] = -1; result[0] =  0; break;
			case 'W':result[1] =  0; result[0] = -1; break;
			default:
			break;
		}
		return result;
	}
	
	private void endToEntFirstStep(char direction) {
		switch (direction) {
			case 'N': this.currY++; break;
			case 'E': this.currX++; break;
			case 'S': this.currY--; break;
			case 'W': this.currX--; break;
			default: return; 
		}
	}
	
	public void discoverMaze(String[] input) {
		init();
		buildMaze(input[0], 2, 0, 0);
		direction = (direction + 2 > 3) ? direction - 2 : direction + 2;
		endToEntFirstStep(allDirections[direction]);
		buildMaze(input[1], direction, currX, currY);
		printMaze(leastX, largestX, largestY, hashMapMaze);
	}

	public static void main(String[] args) {
		MazeBuilder test = new MazeBuilder();
		test.fileReader("B-large-practice.in");
		/*
		String[] r = {"WRWWLWWLWWLWLWRRWRWWWRWWRWLW", "WWRRWLWLWWLWWLWWRWWRWWLW"};//, "WWRRWLWLWWLWWLWWRWWRWWLW");
		String[] r1 = {"WW", "WW"};
		System.out.println("Case #1");
		test.discoverMaze(r);
		System.out.println("Case #2");
		test.discoverMaze(r1);
		*/
		/*
		test.init();
		test.buildMaze(r[0], 2, 0, 0);
		test.direction = (test.direction + 2 > 3) ? test.direction - 2 : test.direction + 2;
		// System.out.println("next " + test.allDirections[test.direction]); // for testing
		test.endToEntFirstStep(test.allDirections[test.direction]);
		test.buildMaze(r[1], test.direction, test.currX, test.currY);
		// System.out.println("least x = " + test.leastX);
		// System.out.println("largest x = " + test.largestX);
		// System.out.println("largest y = " + test.largestY);
		test.printMaze(test.leastX, test.largestX, test.largestY, test.hashMapMaze);
		//int[] r = test.getStartRowStartColWidthHeight("WW", "WW");
		//test.buildMaze("WW", "WW");
		//test.buildMaze("WRWWLWWLWWLWLWRRWRWWWRWWRWLW", "WWRRWLWLWWLWWLWWRWWRWWLW");
		//test.buildMaze("WWWLWLWRRWLWLWWLWRW", "WLWRWWRWWRWWLWLWWRRWLWLWRRWRWLWLWRRWWLW");
		 */
	}
}
