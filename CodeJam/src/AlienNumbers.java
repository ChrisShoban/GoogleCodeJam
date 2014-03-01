import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class AlienNumbers {

	public void translate(String file) {
		try {
			Scanner reader = new Scanner(new File(file));
			String line = reader.nextLine();
			int totalLines = Integer.parseInt(line);
			int i = 1;
			while(reader.hasNextLine() && i <= totalLines) {
				line = reader.nextLine();
				String[] nums = line.split(" ");
				// get the 3 numbers
				// convert left number to type of right number
				System.out.println("Case #" + i + ": " + 
				convert(nums[0], nums[1], nums[2]));
				i++;
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private String convert(String string, String string2, String string3) {
		HashMap<Character, Integer> convert2 = new HashMap<Character, Integer>();
		for(int i = 0; i < string2.length(); i++) {
			convert2.put(string2.charAt(i), i);
		}
		
		int base10Convert = 0;
		int j = 0;
		for(int i = string.length() - 1; i >= 0; i--, j++) {
			base10Convert += (Math.pow(string2.length(), j) * convert2.get(string.charAt(i)));
		}

		ArrayList<Integer> resultIndex = new ArrayList<Integer>();
		
		while (base10Convert != 0) {
			resultIndex.add(base10Convert%string3.length());
			base10Convert /= string3.length();
		}
		
		StringBuffer result = new StringBuffer();
		for(Integer i : resultIndex) {
			result.insert(0, string3.charAt(i));
		}
		return result.toString();
	}

	public static void main(String[] args) {
		AlienNumbers an = new AlienNumbers();
		an.translate("A-large-practice.in");
	}
}
