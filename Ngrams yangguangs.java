import java.util.*;
import java.io.*;

/* Knowledge Technology Project 1
 * N-grams
 * @author ShiYangguang 626689
 */

public class Ngrams{
	public static void main(String[] args) {
		long a = System.currentTimeMillis();	
		String queries = "queries.10K.txt";
		String tweets = "tweets.3k.txt";
		Scanner queryInput = null;
		Scanner tweetInput = null;

		System.out.println("Please input the number N you want to analyse");
		Scanner scanner = null;

		try {
			scanner = new Scanner(System.in);
			int n = scanner.nextInt();
			queryInput = 
				new Scanner(new FileInputStream(queries));

			while (queryInput.hasNextLine()) {
				String line = queryInput.nextLine();
				String[] num = line.split("\\ ");
				int USCount = num.length;
				ArrayList<String> queryLocation = new ArrayList<String>();
				for (int j = 0; j < USCount; j++) {
					queryLocation.add(num[j]);
				}

				tweetInput = 
					new Scanner(new FileInputStream(tweets));

				while (tweetInput.hasNextLine()) {
					String tweetString = tweetInput.nextLine();
					StringTokenizer tweetWord =
						new StringTokenizer(tweetString, " \t");
					ArrayList<String> tweetList = new ArrayList<String>();

					while (tweetWord.hasMoreElements()) {
						tweetList.add(tweetWord.nextToken());
					}

					boolean matching = false;
					int k = 0;
					for(int i = 0; i < tweetList.size(); i++){
						if(getNgram(
							queryLocation.get(0),tweetList.get(i),n) < 2){
							k = i;
						}
					}
					for(int j = 0; j < queryLocation.size(); j++){
						if((k + queryLocation.size()) <= tweetList.size()){
							if(getNgram(
								queryLocation.get(j),tweetList.get(k+j),n) < 4){
								matching = true;
							}else{
								matching = false;
								break;
							}
						}
					}
					if(matching){
						System.out.println(tweetList.get(0));
					}
				}
			}

		} catch (FileNotFoundException e) {
			System.out.println("Problem opening files.");
			e.printStackTrace();
			System.exit(0);
		}
        long b = System.currentTimeMillis();
    	System.out.println("time" + (b - a));
	}

	public static int getNgram(String stringOne, String stringTwo, int n){

		ArrayList<String> listOne = new ArrayList<String>();
		ArrayList<String> listTwo = new ArrayList<String>();

		int sizeOne = stringOne.length();
		int sizeTwo = stringTwo.length();
		
		int n1 = 0;
		int n2 = 0;
		if(sizeOne < n)	
			n1 = sizeOne;
		else n1 = n;
		if(sizeTwo < n) 
			n2 = sizeTwo;
		else n2 = n;
			
		for (int i = 0; i < sizeOne - n1; i++) {
			listOne.add(stringOne.substring(i,i+n1));
		}listOne.add(stringOne.substring(sizeOne-n1, sizeOne));

		for (int i = 0; i < sizeTwo - n2; i++) {
			listTwo.add(stringTwo.substring(i,i+n2));
		}listTwo.add(stringTwo.substring(sizeTwo-n2, sizeTwo));

		int numSame = 0;
		for(String e : listTwo){
			numSame = numSame + Collections.frequency(listOne, e);
		}
		return listOne.size()+listTwo.size()-(2*numSame);
	}
}
