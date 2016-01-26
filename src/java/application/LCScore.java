package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;

import resultInfo.Author;
import resultInfo.JCScore;
import resultInfo.SimScore;
import textHandler.StopwordsRemover;

public class LCScore 
	
{
	public static void main(String[] args)
	{
		if( args==null || args.length<3 )
		{
			System.out.println("Usage:");
			System.out.println("  args[0]: The first Author's ID");
			System.out.println("  args[1]: The second Author's ID");
			System.out.println("  args[2]: The calculate method [0-2]");
			System.exit(0);
		}
		int a1 = Integer.valueOf(args[0]);
		int a2 = Integer.valueOf(args[1]);
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
		double result = getScore(a1,a2,method,swr);
		if(result!=-1)
			System.out.println("The Score between those two authors are: "+result);
		else
			System.exit(0);
	}
	
	public static Author[] getScores(int cid,int authorid,StopwordsRemover swr)
	{
		ArrayList<Author> result = new ArrayList<Author>();
		Hashtable<Integer,SimScore> Similarities = application.Similarity.getSimilarities(authorid, cid, swr);
		Hashtable<Integer,JCScore> Jaccards = application.JaccardCoefficient.getJaccards(authorid, cid);
		
                Similarities.remove(authorid);
                Jaccards.remove(authorid);
                Similarities.remove(0);
                Jaccards.remove(0);
                
		for(Iterator<Integer> itr = Similarities.keySet().iterator(); itr.hasNext();)
		{	
			int key = (int) itr.next();
			Author temp = new Author(key);
			SimScore sscore = (SimScore) Similarities.get(key);
			
			temp.setSimscore(sscore.getScore());
			
			double score = sscore.getScore() * 0.5;
			
			temp.setAffiliations(sscore.getAffliations());
			
			if(Jaccards.containsKey(key))
			{
				JCScore jscore =(JCScore) Jaccards.get(key);
				score = score + 0.5 * jscore.getScore();
				
				temp.setJcscore(jscore.getScore());
				temp.setName(jscore.getName());
				
				Jaccards.remove(key);
			}	
			else
			{
				temp.setJcscore(0.0);
				temp.setName("");
			}
			
			temp.setScore(score);
			
			result.add(temp);
		}
		
		if(!Jaccards.isEmpty())
		{
			for(Iterator<Integer> itr = Jaccards.keySet().iterator(); itr.hasNext();)
			{
				int key = (int) itr.next();
				
				Author temp = new Author(key);
				temp.setAffiliations("");
				temp.setSimscore(0.0);
				JCScore jscore = (JCScore) Jaccards.get(key);
				double score = jscore.getScore();
				score = score * 0.5;
				temp.setScore(score);
				temp.setName(jscore.getName());
				temp.setJcscore(jscore.getScore());
				result.add(temp);
			}
		}

		Author[] authors = new Author[result.size()];
		result.toArray(authors);
		return authors;
	}
	
	public static Author[] getScores(int sid,int eid,int authorid,int method,int[] CN,StopwordsRemover swr) 
	{
		
		Author[] result = new Author[eid-sid+1];
		System.out.println("start sim score from "+sid+" to "+eid);
		double[] Similirities = application.Similarity.getSimilarities(authorid, sid, eid,swr);
		System.out.println("finish sim score from "+sid+" to "+eid);
		Author temp = null;
		switch(method)
		{
			case 1:
			{
				System.out.println("start jc score from "+sid+" to "+eid);
				double[] Jaccards = application.JaccardCoefficient.getJaccards_H2(authorid, sid, eid);
				for(int i=0;i<result.length;i++)
				{
					double SC = Similirities[i];
					double JC = Jaccards[i];
					double score = 0.0;
					if(SC != -1 && JC !=-1)
						score = 0.5 * SC + 0.5 * JC;
					else
						score = -1;
					temp = new Author(sid+i);
					temp.setScore(score);
					result[i] = temp;
				}
				System.out.println("finish jc score from "+sid+" to "+eid);
			}
			case 2:
			{
				double[] Jaccards = application.JaccardCoefficient.getJaccards(authorid, sid, eid);
				for(int i=0;i<result.length;i++)
				{
					int id = sid+i;
					double SC = Similirities[i];
					double JC = Jaccards[i];
					double score = 0.0;
					if(SC != -1 && JC !=-1 && Arrays.asList(CN).contains(id))
					{
						double AS = application.AdamicAdar.getAA(authorid, id);
						//score = 0.5 * SC + 0.25 * JC + 0.25 * AS;
						score = (SC + JC + AS)/3;
						
					}
					else if(SC != -1 && JC !=-1 && !Arrays.asList(CN).contains(sid))
						//score = 0.5 * SC + 0.25 * JC;
						score = SC/3 + JC/3;
					else
						score = -1;
					
					temp = new Author(id);
					temp.setScore(score);
					
					result[i] = temp;
				}
			}
				
		}
		
		return result;
	}
	
	public static double getScore(int index1,int index2,int method,StopwordsRemover swr)
	{
		double result = 0.0;
		
		double sim = Similarity.getSimilarity(index1, index2,swr);
		
		if(sim != -1)
		{
		
			result = result + 0.5*sim;
			
			double JC = JaccardCoefficient.getJaccard(index1, index2);
			
			if(JC != -1)
			{
				if(method == 0)
						result = result + 0.5 * JC;
				if(method == 1)
				{
					double AA = AdamicAdar.getAA(index1, index2) ;
					if(AA != -1)
						result = result + 0.5 * (0.5*JC + 0.5*AA);
					else
						result = result + 0.25 * JC;
				}
				if(method == 2)
				{
					double AA = AdamicAdar.getAA(index1, index2) ;
					if(AA != -1)
						result = result + (0.5/3) * (CommonNeighbor.getCommon(index1, index2, 0).length + JC + AA);	
					else
						result = -1;
				}
			}
			else
				result =-1;
		}
		else
			result = -1;
		
		
		return result;
	}

}
