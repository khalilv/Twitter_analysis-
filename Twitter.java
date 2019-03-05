import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.io.*; 

public class Twitter {
	
	//attributes 
	private ArrayList<Tweet> tweets; 
	
	//constructor that initializes a blank arrayList of tweets
	public Twitter(){
		this.tweets = new ArrayList<Tweet>(); 
	}
	
	//a method to load all valid tweets into the arrayList 
	public void loadDB(String name){
		//read the file with the input name and store each line into the arrayList 
		FileReader fr; 
		BufferedReader br; 
		String currentLine;
		ArrayList<String> arrayReading = new ArrayList<String>();
		try{
			fr = new FileReader(name);
			br = new BufferedReader(fr);
			currentLine = br.readLine();
			while(currentLine != null){
				arrayReading.add(currentLine);
				currentLine = br.readLine();
			}
			br.close();
			fr.close();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		/*loop through the arrayList which now contains all the lines from the input file. 
		 * split each line based on a tab. the first instance will be the username, the second will be the date, 
		 * the third will be the time, the fourth will be the message. Then, initialize a tweet with those attributes and add it to the arrayList of this.tweets. 
		 */
		String userName; 
		String date; 
		String time; 
		String message; 
		for(int i=0;i<arrayReading.size();i++){
			String data = arrayReading.get(i);
			String[] splitData = data.split("	");
			userName = splitData[0];
			date = splitData[1];
			time = splitData[2];
			message = splitData[3];
			Tweet newTweet = new Tweet(userName, date, time, message);
			this.tweets.add(newTweet);
		}
		//loop through the messages of each tweet in this.tweets. if the message is invalid, remove it from the list of this.tweets 
		try{
			for(int i=0;i<this.tweets.size();i++){
				if(this.tweets.get(i).checkMessage()==false){
					this.tweets.remove(i);
				}
			}	
		//if the stopWords file has not been uploaded(equals null) print respective message to let user know what went wrong.  
		}catch(NullPointerException e){
			System.out.print("Error checking the stopWords database: The file of stopWords has not been loaded yet"); 
		}
		//call on method to sort the tweets in this.tweets based on the time they were posted 
		sortTwitter();
	}
	
	//a method to sort tweets from earliest to latest 
	public void sortTwitter(){
		for(int iter = 0; iter<this.tweets.size()-2;iter++){
			for(int i=0;i<this.tweets.size()-1-iter;i++){
				//if a tweet was posted later than the tweet after it in this.tweets, switch the two. 
				if(this.tweets.get(i).isBefore(this.tweets.get(i+1))==false){
					swap(i, i+1); 
				}
			}
		}
	}
	
	//a method to switch two tweets 
	private void swap(int i, int j) {
		Tweet temp = this.tweets.get(i);
		this.tweets.set(i, this.tweets.get(j));
		this.tweets.set(j, temp);
	}
	
	//return size of this.tweets (with only the valid tweets left) 
	public int getSizeTwitter(){
		return this.tweets.size();
	}
	
	//a method to get the tweet at a particular index 
	public Tweet getTweet(int index){
		return this.tweets.get(index);
	}
	
	//a method to return a string that contains all of the valid tweets 
	public String printDB(){
		String info="";
		for(int i=0;i<this.tweets.size();i++){
			info+= this.tweets.get(i).toString();
		}
		return info; 
	}
	
	//a method to get the tweets between the range specified 
	public ArrayList<Tweet> rangeTweets(Tweet tweet1, Tweet tweet2){
		ArrayList<Tweet> middleTweets = new ArrayList<Tweet>();
		int k=0;
		int j=0; 
		//loop through this.tweets and find indices of the two input tweets 
		for(int i=0;i<this.tweets.size();i++){
			if(this.tweets.get(i)==tweet1){
				k=i;
			}
			if(this.tweets.get(i)==tweet2){
				j=i; 
			}	
		}
		//add the tweets between the parameters (inclusive) to a new arrayList of tweets 
		if(j<k){
			for(int a=j;a<=k;a++){
				middleTweets.add(this.tweets.get(a));
			}
		}
		if(j>k){
			for(int a=k;a<=j;a++){
				middleTweets.add(this.tweets.get(a));
			}
		}
		return middleTweets; 
	}
	
	//write the list of valid tweets into a file of the input name 
	public void saveDB(String name){
		FileWriter fw; 
		BufferedWriter bw; 
		try{
			fw = new FileWriter(name);
			bw = new BufferedWriter(fw);
			String message = printDB(); 
			bw.write(message);
			bw.close();
			fw.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	//a method to find which word (excluding stopWords) occurs the most in the validTweets 
	public String trendingTopic(){
		ArrayList<String> words = new ArrayList<String>();
		//loop through this.tweets. split each message based on a space an add it to a new array list of words. 
		for(int i=0;i<this.tweets.size();i++){
			String[]splitMessage = this.tweets.get(i).getMessage().split(" ");
		
			
			for(int j=0;j<splitMessage.length;j++){
				
				///deals with the special case where there is a '.',',',':' or';' appended to the end of the word. Will shorten the word by 1 thus removing the last character 
				if(splitMessage[j].charAt((splitMessage[j].length())-1)==';'||splitMessage[j].charAt((splitMessage[j].length())-1)=='.'||splitMessage[j].charAt((splitMessage[j].length())-1)==','||splitMessage[j].charAt((splitMessage[j].length())-1)==':'){
					String shorten=""; 
					for(int j1=0;j1<splitMessage[j].length()-1;j1++){
						shorten+= splitMessage[j].charAt(j1);
					}
					splitMessage[j]=shorten; 
				}
				
				//adds words of message to a hashSet then puts them back into the array splitMessage. To get rid of the duplicate words that might appear in the message 
				HashSet<String> noDuplicates = new HashSet<String>();
				for(int k=0;k<splitMessage.length;k++){
					noDuplicates.add(splitMessage[k]);
				}
				splitMessage = noDuplicates.toArray(new String[noDuplicates.size()] );
				
				//will only add word to the array list if it is not a stop word 
				boolean contains = false; 
				for(String item1: Tweet.stopWords){
					if(splitMessage[j].equalsIgnoreCase(item1)){
						contains = true; 
						break; 
					}
				}
				if(contains == false){
					words.add(splitMessage[j]);
				}
			}
			//then repeat for all messages in all tweets 
		}
		
		//now find frequencies of all words in string 
		Map<String,Integer> frequency = new HashMap<String,Integer>();
		
		for(String item: words){
			String input = item.toLowerCase();
			if(frequency.containsKey(input)){
				Integer count = new Integer((frequency.get(input))+1);
				frequency.put(input, count);
			}else{
				frequency.put(input, 1);
			}
		}
		//loop through the Hashmap and find the word associated with the highest frequency(the word that occurs the most). Return that word 
		int max = -1000;
		String maxString = "";
		for(String key : frequency.keySet()){
			Integer count = new Integer(frequency.get(key));
			if(count > max){
				max = count;
				maxString = key;
			}
		}
		System.out.println(max);
		return maxString; 
	}
	
	public static void main(String[]args){
		Twitter example = new Twitter(); 
		Tweet.loadStopWords("stopWords.txt");
		example.loadDB("tweets.txt"); 
		System.out.println("The number of tweets is: " + example.getSizeTwitter());
		System.out.println(example.printDB());
		System.out.println(example.rangeTweets(example.getTweet(4), example.getTweet(2)));
		System.out.println(example.trendingTopic());
	}
}
	
	

