package textHandler;

//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Parser 
{
	public static ArrayList<String> stringParser(String text,StopwordsRemover swr)
	{
		StringTokenizer st = new StringTokenizer(text);
		ArrayList<String> result = new ArrayList<String>();

//		try {
//			swr = new StopwordsRemover((new FileInputStream("stop_words.txt")));
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		String word = "";
		while(st.hasMoreTokens())
		{
			word = st.nextToken();   //get the token
			if(!swr.isStopword(word))  // remove stopword
			{
				word = TextNormalizer.normalize(word); // normalise it
				result.add(word);
			}
		}
		return result;
	}

}
