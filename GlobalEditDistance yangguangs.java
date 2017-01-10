import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Arrays;
import java.util.Set;

/* Knowledge Technology Project 1
 * Global edit distance
 * @author ShiYangguang 626689
 */

public class GlobalEditDistance {
	public static final int removeCost = 1;
	public static final int addCost = 1;
	public static final int replaceCost = 1;
	
	public static String readTweet() {

		String newTweet = "";
		try {
			File file = new File("tweets.3K.txt");
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			String line;
			String[] arr = null;
			String tweet = null;
			String tweetID = null;
			while ((line = bufferedReader.readLine()) != null) {
				arr = line.split("\t");
				tweetID = arr[0];
				tweet = arr[1];
				newTweet += (tweetID + "\t" + tweet + "\n");
			}

			fileReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return newTweet;

	}

	public static String readLocation() {
		String newLoacation = "";
		try {
			File file = new File("queries.10K.txt");
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				newLoacation += line + "\n";
			}
			fileReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return newLoacation;
	}
	
	public static int distance(String input,String transformed){
		int inputSize = input.length();
		int transformedSize = transformed.length();
		int S[][] = new int[inputSize + 1][transformedSize + 1];
		
		for(int i = 0; i < inputSize  + 1; i++ ){
		S[i][0] = i;
		}
		
		for(int j = 0; j < transformedSize + 1; j++){
		S[0][j] = j;
		}
		
		
		for(int i = 1; i < inputSize + 1; i++ ){
			for(int j = 1; j < transformedSize + 1; j++){
				if(input.charAt(i-1)== transformed.charAt(j-1)){
					S[i][j] = S[i-1][j-1];
				}else if(input.charAt(i-1)!= transformed.charAt(j-1)){
					S[i][j] = S[i-1][j-1] + replaceCost;
				}
				
				S[i][j] = Math.min(S[i][j], Math.min(S[i][j-1] + addCost,
						S[i-1][j] + removeCost));
			}
		}
		return S[inputSize][transformedSize];		
	}

	public static void main(String[] args) {
		long a = System.currentTimeMillis();	

		String[] tweet = readTweet().split("\n");
		String location = readLocation();

		PrintWriter outputStream = null;
		String ReOutput = "result.txt";
		
		String[] t = null;
		String tID;
		String tText;
		try {
			outputStream = new PrintWriter(new FileOutputStream(ReOutput));
		} catch (FileNotFoundException e) {
			System.out.println("Problem opening files.");
			e.printStackTrace();
			System.exit(0);
		}
		for (String s : tweet) {
			t = s.split("\t");
			tID = t[0];
			tText = t[1];
			String[] wordsForOneTweetLine = tText.split(" ");
			for (int i = 0; i < wordsForOneTweetLine.length; i++) {
				for (String oneLocation : location.split("\n")) {
					String[] locationWords = oneLocation.split(" ");
					int lengthOfWordsOfOneLocation = locationWords.length;
					int needlemanDistance = 0;
					float errRate = 0;
					boolean matching = false;
					String locationFound = "";

					for (String singleWordForLocation : oneLocation.split(" ")) {

						if (i > wordsForOneTweetLine.length - 1){
							break;

						}
						matching = false;
						needlemanDistance = distance(wordsForOneTweetLine[i], singleWordForLocation);
						errRate = (float) needlemanDistance / (float) singleWordForLocation.length();

						if (singleWordForLocation.length() <= 4) {
							if (errRate * singleWordForLocation.length() <= 1)
								matching = true;
						} else {
							if (errRate < 0.2)
								matching = true;
						}
						if (matching) {
							lengthOfWordsOfOneLocation--;
							if (lengthOfWordsOfOneLocation == 0) {
								locationFound += wordsForOneTweetLine[i];
								System.out.println(tID);
								outputStream.println(tID);
								break;
							} else {
								locationFound += wordsForOneTweetLine[i] + " ";
								i++;
							}
						}

						else {
							break;
						}
					}
				}
			}
		}
		
		
        long b = System.currentTimeMillis();
    	System.out.println("time" + (b - a));

		outputStream.close();
	}
	
	
}