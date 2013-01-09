import java.io.*;
import java.util.*;

/**
 * 
 * @author Jonas J. Schreiber
 * 
 * A LetterPress and Scrabble-type word finder
 * (Highly efficient)
 * 
 * This code contains examples of: 
 * 
 * -String operations
 * -Taking User Input
 * -File Input/Output
 * -Iterating through a file without RandomAccess
 * -ArrayList Usage
 * -Sorting ArrayList Entries (Using the java.util.Collections library)
 *
 */

public class Main {
	public static void main(String[] args) throws IOException {
		BufferedReader br2 = new BufferedReader(new InputStreamReader(System.in));
	    int numblanks = 0;
		System.out.print("Enter a sequence of characters (use * for blanks) and I'll tell you which words are possible: ");
		String input = br2.readLine();	
		input = input.toUpperCase();

		//move user input to an ArrayList, for quick operations
		final ArrayList<Character> inputChars = new ArrayList<Character>();
		for (int i = 0; i < input.length(); i++)
		{
			if (input.charAt(i) == '*')
				numblanks++;
			else
				inputChars.add(input.charAt(i));
		}

		ArrayList<String> results = generateResults(inputChars, numblanks);
    	
		sortAndWriteResults(results);
		
		return;
	}
	
	
	public static ArrayList<String> generateResults(final ArrayList<Character> inputChars, int numblanks) throws IOException
	{
		File file = new File("src" + File.separator + "Letterpress Words.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		String str;
		ArrayList<String> results = new ArrayList<String>();
		
		//go entry by entry in scrabblewords.tx
    	while ((str = br.readLine()) != null) {
    		
    		//make copy of arraylist for comparisons 
    		ArrayList<Character> inputCharsTemp = new ArrayList<Character>(inputChars); 
    		boolean hasAllChars = true;
    		int numblankstemp = numblanks;
    		
    		//go character by character within Scrabble word
    		for(int i = 0; i < str.length(); i++)
    		{
    			if(!inputCharsTemp.contains(str.charAt(i)))
    			{
    				if (numblankstemp == 0)
    				{
    					hasAllChars = false;
    					break;
    				}
    				else
    					numblankstemp--;
    			}
    			
    			//ensure that words with double letters are only added 
    			//to the results set contains that letter, twice
    			else
    				inputCharsTemp.remove(inputCharsTemp.indexOf(str.charAt(i)));
    		}
    		if (hasAllChars)
    			results.add(str);
    	}
    	return results;
    	
	}
	
	public static void sortAndWriteResults(ArrayList<String> results) throws IOException
	{
		java.util.Collections.sort(results, new MyComparator());
		
		//get userprofile environment variable
	    Map<String, String> env = System.getenv();
        String desktop = env.get("USERPROFILE") + File.separator + "Desktop";
        
        //Open the file for writing
		File output = new File(desktop +  File.separator + "results.txt");
		
		//Ensure you aren't overwriting a result set
		if (output.exists())
		{
			for (int i = 0; i < 100; i++)
			{
				output = new File(desktop +  File.separator + "results" + i + ".txt");
				if (!output.exists())
					break;
			}
		}
		
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(output));
        
        //Write the results to the file
    	for (String s : results)
    		out.write(s + "\n");
    	  	
    	out.flush();
    	out.close();
    	
    	System.out.println("\nFinished, results saved to desktop\n");
    	
    	//display a few results in console
    	System.out.println("**Top Entries**"); 
    	if (results.size() >= 5)
    	{
    		for (int i = 0; i < 5; i++)
    			System.out.println(results.get(i));
    	}
    	else
    		for (String s : results)
    			System.out.println(s);
	}
}
