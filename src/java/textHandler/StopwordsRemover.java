package textHandler;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


public class StopwordsRemover {
	
	private Map<String,String> stopmap;

	public StopwordsRemover( FileInputStream instream ) throws IOException {
		// load and store the stop words from the fileinputstream with appropriate data structure
		// that you believe is suitable for matching stop words.
		stopmap=new HashMap<String,String>();
		BufferedReader reader = null;  
		reader = new BufferedReader(new InputStreamReader(instream)); //Reading File line by line using BufferedReader
		String line = reader.readLine();
		while(line!=null)                  //as observing from the stopword file, all words are in one line.
		{			
			stopmap.put(line, line);       // if put char[] as a key into a map, when you use containsKey, function will use the address to match, so use String as key
			line=reader.readLine();
		}
	}
	
	public boolean isStopword( String word ) {
		// return true if the input word is a stopword, or false if not
		

		if(stopmap.containsKey(word))   //use new String() function is faster than StringBuilder
		//if(stopmap.containsKey(word))
			return true;
		else
			return false;
	}

	
}
