package application;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import resultInfo.Author;
import textHandler.StopwordsRemover;
import dbHelper.ExcuteSQL;



public class RecommendationList
{
	public static void main(String[] args)
	{
		if( args==null || args.length<3 )
		{
			System.out.println("Usage:");
			System.out.println("  args[0]: The Author's ID");
			System.out.println("  args[1]: The total number want to return");
			System.out.println("  args[2]: The calculate method [0-2]");
			System.exit(0);
		}
		int id = Integer.valueOf(args[0]);
		int total = Integer.valueOf(args[1]);
		int method = Integer.valueOf(args[2]);
		String sw = args[3];
		StopwordsRemover swr = null;
		try {
			swr = new StopwordsRemover((new FileInputStream(sw)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<Author> RL = getList(id,total,method,swr);
		
		for(int i=0;i<RL.size();i++)
		{
			System.out.println(i+" id:"+RL.get(i).getId()+" score:"+RL.get(i).getScore());
		}
	}

	public static List<Author> getList(int id,int total,int method,StopwordsRemover swr) 
	{
		ArrayList<Author> RL = new ArrayList<Author>();
		Author author = null;
		
		String sql = "select max(index1) from Coauthor";
		int totalAuthor =Integer.valueOf(ExcuteSQL.ExcuteSelect(sql, 1));
		
		for(int i=1;i<totalAuthor;i++)
		{
			double score = LCScore.getScore(id, i, method,swr);
			if(score != 0.0 && score != -1.0)
			{
				author = new Author(i);
				author.setScore(score);
				RL.add(author);
			}
			else
				continue;
		}
		
		
		Collections.sort(RL, new result_comparator());
		
		if(RL.size()>total)
			return RL.subList(0, total);
		else
			return RL;
	}
	
	static class result_comparator implements Comparator<Author> //a comparator to sort
	{

		@Override
		public int compare(Author a1, Author a2) {
			if(a1.getScore()>a2.getScore())
				return -1;
			else if(a1.getScore()<a2.getScore())
				return 1;
			else
				return 0;
		}
	
	}
	
}
