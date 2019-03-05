import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;



public class Tweet {
	//attributes of a tweet 
	private String userAccount; 
	private String date; 
	private String time; 
	private String message; 
	public static HashSet<String> stopWords; 
	
	//initialize a tweet based on input attributes 
	public Tweet(String userAccount, String date, String time, String message){
	this.userAccount=userAccount; 
	this.date=date; 
	this.time=time;
	this.message=message; 
	}
	
	//a method to see if the this.message is valid 
	public boolean checkMessage(){
		//throw NullPointerException if you have not uploaded the stopWords file 
		if(stopWords==null){
			throw new NullPointerException();
		}
		//split the message by spaces and add it to a string of validTweets 
		String[] validTweets;
		validTweets = this.message.split(" ");
		
		//loop through validTweets and if it is not a stopWord, then increase a counter by 1 
		int count = 0; 
		for(int i=0;i<validTweets.length;i++){
			//account for the special case where there is a '.',',',':',';' appended to the end of the word. Will shorten that word by length 1 then check if it is a stopWord. 
			if(validTweets[i].charAt((validTweets[i].length())-1)==';'||validTweets[i].charAt((validTweets[i].length())-1)=='.'||validTweets[i].charAt((validTweets[i].length())-1)==','||validTweets[i].charAt((validTweets[i].length())-1)==':'){
				String shorten=""; 
				for(int j=0;j<validTweets[i].length()-1;j++){
					shorten+= validTweets[i].charAt(j);
				}
				validTweets[i]=shorten; 
			}
			
			for(String item: stopWords ){
				if (validTweets[i].equalsIgnoreCase(item)){
					count++; 
				}
			}
		}
		//if there is less than 16 words and more than 0 words then it is a valid message and return true. otherwise the message is not valid and return false. 
		if((validTweets.length-count)>0 && (validTweets.length-count)<16 && stopWords!=null){
			return true; 
		}
			return false; 
	}
	
	//returns date
	public String getDate(){
		return this.date; 
	}
	
	//returns time
	public String getTime(){
		return this.time; 
	}
	
	//returns message
	public String getMessage(){
		return this.message; 
	}
	
	//returns user account 
	public String getUserAccount(){
		return this.userAccount; 
	}
	
	//prints information of tweet separated by a tab. leaves a blank space underneath for ease of reading 
	public String toString(){
		String info = "";
		info+="\n";
		info+= this.userAccount +"\t";
		info+= this.date +"\t";
		info+=this.time +"\t";
		info+=this.message+"\t";
		return info; 
	}
	
	//a method to check if this tweet was posted before the input tweet
	public boolean isBefore(Tweet inputTweet){
		//split the input tweets date based on '-'. The first integer will be the year, the second will be the month, the third will be the day 
		String[] inputDateValues = inputTweet.date.split("-");
		int inputYear = Integer.valueOf(inputDateValues[0]);
		int inputMonth = Integer.valueOf(inputDateValues[1]);
		int inputDay = Integer.valueOf(inputDateValues[2]);
		
		//split this tweets date based on '-'. The first integer will be the year, the second will be the month, the third will be the day 
		String[]dateValues = this.date.split("-");
		int year = Integer.valueOf(dateValues[0]);
		int month = Integer.valueOf(dateValues[1]);
		int day = Integer.valueOf(dateValues[2]);
		
		//split the input tweets time based on ":". The first integer will be the hour, the second will be the minute, the third will be the second. 
		String[]inputTimeValues = inputTweet.time.split(":");
		int inputHour = Integer.valueOf(inputTimeValues[0]);
		int inputMinute = Integer.valueOf(inputTimeValues[1]);
		int inputSecond = Integer.valueOf(inputTimeValues[2]);
		
		//split this tweets time based on ":". The first integer will be the hour, the second will be the minute, the third will be the second.
		String[]timeValues = this.time.split(":");
		int hour = Integer.valueOf(timeValues[0]);
		int minute = Integer.valueOf(timeValues[1]);
		int second = Integer.valueOf(timeValues[2]);
		
		//if this tweet is earlier than the input tweet return true. 
		if(year<inputYear){
			return true; 
		}
		if(year==inputYear && month<inputMonth){
			return true; 
		}
		if(year==inputYear && month==inputMonth && day<inputDay){
			return true; 
		}
		if(year==inputYear && month==inputMonth && day==inputDay && hour<inputHour){
			return true; 
		}
		if(year==inputYear && month==inputMonth && day==inputDay && hour==inputHour && minute<inputMinute){
			return true; 
		}
		if(year==inputYear && month==inputMonth && day==inputDay && hour==inputHour && minute==inputMinute && second<inputSecond){
			return true; 
		}
		return false; 
	}
	
	//a method to read a file with the input name and store each line into a Hash set named stopWords 
	public static void loadStopWords(String name){
		FileReader fr; 
		BufferedReader br; 
		String currentLine; 
		stopWords = new HashSet<String>();
		try{
			fr = new FileReader(name);
			br = new BufferedReader(fr);
			currentLine = br.readLine();
			while(currentLine != null){
				stopWords.add(currentLine);
				currentLine=br.readLine();
			}
			br.close();
			fr.close();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}	
} 
