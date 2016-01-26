package application;



import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.TreeMap;

import resultInfo.Content;
import resultInfo.SimScore;
import textHandler.Parser;
import textHandler.StopwordsRemover;
import dbHelper.ExcuteSQL;

public class Similarity 
{
	public static void main(String[] args)
	{
		if( args==null || args.length<2 )
		{
			System.out.println("Usage:");
			System.out.println("  args[0]: The first Author's ID");
			System.out.println("  args[1]: The second Author's ID");
			System.exit(0);
		}
		int a1 = Integer.valueOf(args[0]);
		int a2 = Integer.valueOf(args[1]);
		String sw = args[3];
		StopwordsRemover swr = null;
		try {
			swr = new StopwordsRemover((new FileInputStream(sw)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double result = getSimilarity(a1,a2,swr);
		if(result!=-1)
			System.out.println("The Similarity between those two authors are: "+result);
		else
			System.exit(0);
	}
		
	
	public static String getAbstract(int id)
	{
		StringBuilder ab = new StringBuilder();
		String sql_paperid = "select paperid from Author2Paper where authorid="+String.valueOf(id);
		int[] paperid = ExcuteSQL.ExcuteSelect_IA(sql_paperid, 1); //get the array of paperid which from that author back
		if(paperid!=null) //if there are some papers
		{
			for(int i=0;i<paperid.length;i++)
			{
				//use the papaerid to get the abstract
				String sql_ab = "select CONCAT(title,abstract) from Paper where id="+String.valueOf(paperid[i]);
				String ab_temp = ExcuteSQL.ExcuteSelect(sql_ab, 1);
				if(ab_temp!="")
					ab.append(ExcuteSQL.ExcuteSelect(sql_ab, 1));
				else
					continue;
				ab.append(" ");
			}	
		}
		else
		{
			ab.append("noauthor");
			System.out.println("This Author "+String.valueOf(id) +" Does not have any paper!");
		}
		if(ab.toString().equals(""))
		{
			ab.append("noauthor");
			System.out.println("This Author "+String.valueOf(id) +" Does not have any abstract!");
		}
		return ab.toString();
	}
	
	
	public static String[] getAbstract(int sid,int eid)
	{

		String sql_abstract = "select authorid,CONCAT(title,abstract) from Author2Paper,Paper where Author2Paper.authorid BETWEEN "+String.valueOf(sid)+" and "+String.valueOf(eid)+" and Author2Paper.paperid=Paper.id ORDER BY authorid ASC";
		String[] ab = ExcuteSQL.ExcuteSelect_MRS(sql_abstract,sid,eid); //get the array of paperid which from that author back

		return ab;

	}
	
	public static Content[] getAbstractbyConference(int cid)
	{
		String sql = "SELECT Author2Paper.authorid,CONCAT(title,abstract),Paper.affiliations FROM Map,Author2Paper,Paper WHERE Map.conf_id="+String.valueOf(cid)+" and Map.aminer_id=Author2Paper.authorid and Author2Paper.paperid=Paper.id ORDER by Author2Paper.authorid ASC";
		Content[] ab = ExcuteSQL.ExcuteSelect_C(sql);
		return ab;
	}
	
	public static Hashtable<Integer,SimScore> getSimilarities(int authorid,int cid,StopwordsRemover swr)
	{
		
		String ab = getAbstract(authorid);
		Content[] abs = getAbstractbyConference(cid);
		Hashtable<Integer,SimScore> sim = new Hashtable<Integer,SimScore>();

		for(int i=0;i<abs.length;i++)
		{
			SimScore temp = new SimScore(abs[i].getAffiliations(),calcSimilarity(ab,abs[i].getContent(),swr));
			sim.put(abs[i].getId(), temp);
		}
		return sim;
		
	}
	
	public static double[] getSimilarities(int authorid,int sid,int eid,StopwordsRemover swr)
	{
		
		double[] sim = new double[eid-sid+1];
		for(int i=0;i<sim.length;i++)
			sim[i]=0.0;
		String ab = getAbstract(authorid);
		String[] abs = getAbstract(sid,eid);
		
		for(int i=0;i<abs.length;i++)
		{
			sim[i]=calcSimilarity(ab,abs[i],swr);
		}
		return sim;
	}
	
	public static double calcSimilarity(String ab1,String ab2,StopwordsRemover swr)
	{
		double sim;
                
                if(ab1.trim().isEmpty() || ab2.trim().isEmpty())
		{
			return 0.0;
		}
		
		if(ab1.equals("noauthor") || ab2.equals("noauthor"))
		{
			return 0.0;
		}
		else
		{
			ArrayList<String> abList1 = Parser.stringParser(ab1,swr);
			ArrayList<String> abList2 = Parser.stringParser(ab2,swr);
			
			TreeMap<String,Integer> vector1	= iniVector(abList1,abList2);
			TreeMap<String,Integer> vector2	= iniVector(abList1,abList2);
			
			addNumber(abList1,vector1);
			addNumber(abList2,vector2);	
			
			sim = calcCosine(vector1,vector2);
		}

		return sim;
	}
	
	public static double getSimilarity(int id1,int id2,StopwordsRemover swr)
	{
		double sim = 0.0;
		
		String ab1 = getAbstract(id1);
		String ab2 = getAbstract(id2);

		
		sim = calcSimilarity(ab1,ab2,swr);

		return sim;
	}
	
	private static double calcCosine(TreeMap<String,Integer> vector1,TreeMap<String,Integer> vector2)
	{
		double result = 0.0;
		double xiyi = 0.0;
		double l1=0.0;
		double l2=0.0;
		
		Iterator<String> it = vector1.keySet().iterator(); 
		while(it.hasNext())
		{   //use cosine to calculate the similarity
			String word = it.next();
			xiyi = xiyi + ((double) vector1.get(word)) * ((double) vector2.get(word));
			l1 = l1 + ((double) vector1.get(word))*((double) vector1.get(word)); //length1
			l2 = l2 + ((double) vector2.get(word))*((double) vector2.get(word)); //lenght2
		}
		
		result = xiyi / (Math.sqrt(l1)*Math.sqrt(l2));
				
		return result;
	}
	
	private static void addNumber(ArrayList<String> abList, TreeMap<String,Integer> vector)
	{
		for(int i=0;i<abList.size();i++)
		{
			String word = abList.get(i);
			if(vector.containsKey(word))
			{
				int num = vector.get(word);  //if there is a same word, count it
				vector.put(word, num+1);
			}
			else
				vector.put(word, 1); // if this is a new word, add it
		}
	}
	
	private static TreeMap<String,Integer> iniVector(ArrayList<String> abList1,ArrayList<String> abList2)
	{
		TreeMap<String,Integer> vector	= new TreeMap<String,Integer>();
		
		for(int i=0;i<abList1.size();i++)
		{
			String word = abList1.get(i);
			if(vector.containsKey(word))
				continue;
			else
				vector.put(word, 0);     // to make all word from two abstract are all in the vector
		}								 // and initial the count to 0.
		for(int i=0;i<abList2.size();i++)
		{
			String word = abList2.get(i);
			if(vector.containsKey(word))
				continue;
			else
				vector.put(word, 0);
		}
		
		return vector;
	}
	
}
