package application;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

import resultInfo.Common;
import resultInfo.JCScore;
import dbHelper.ExcuteSQL;

public class JaccardCoefficient 
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
		double result = getJaccard(a1,a2);
		
		if(result==-1)
		{
			System.exit(0);
		}
		else
		{
			System.out.println("The Jaccard Coefficient between those two authors is "+result);
		}
		
	}
	
	public static double calcJaccard(int[] author1,int[] author2)
	{
                if(author1.length==0 || author2.length==0)
                    return 0.0;
                
		int intersection = 0;
		int union = 0;
		double result = 0.0;
                
		HashSet<Integer> coauthor = new HashSet<Integer>();
		if(author1!=null && author2!=null)
                {
                    for(int i=0;i<author1.length;i++)
                    {
                            coauthor.add(author1[i]);
                            union++;
                    }
                    for(int i=0;i<author2.length;i++)
                    {
                            boolean co = coauthor.add(author2[i]);
                            if(co==false)
                                    intersection++;
                            else
                                    union++;
                    }		
                    if(union==0)
                        return 0.0;
                    result = (double) intersection/ (double) union;
                }

		return result;
	}
        
        public static double calcJaccard(String[] author1,String[] author2)
	{
                
            
		int intersection = 0;
		int union = 0;
		double result = 0.0;

		HashSet<String> coauthor = new HashSet<String>();
		if(author1!=null && author2!=null)
                {
                    if(author1.length==0 || author2.length==0)
                        return 0.0;
                    for(int i=0;i<author1.length;i++)
                    {
                            coauthor.add(author1[i]);
                            union++;
                    }
                    for(int i=0;i<author2.length;i++)
                    {
                            boolean co = coauthor.add(author2[i]);
                            if(co==false)
                                    intersection++;
                            else
                                    union++;
                    }		
                    if(union==0)
                        return 0.0;
                    result = (double) intersection/ (double) union;
                }

		return result;
	}
	
	public static double[] getJaccards_H2(int authorid,int sid,int eid)
	{
		double[] result =new double[eid-sid+1];
		
		int[] authorNeighbor = CommonNeighbor.getHop2Neighbor(authorid);
		
		String sql_other = "Select * from (Select index1 as a1,index2 as a2 from Coauthor where index1 between "+sid+" and "+eid+" union all Select index2 as a1,index1 as a2 from Coauthor where index2 between "+sid+" and "+eid+" ) t1 ORDER by t1.a1 ASC";
		
		ArrayList<ArrayList<Integer>> otherNeighbor = ExcuteSQL.ExcuteSelect_MRA(sql_other,sid,eid);
		
		
		for(int i=0;i<otherNeighbor.size();i++)
		{
			ArrayList<Integer> other = otherNeighbor.get(i);
			if(other!=null && !other.isEmpty() && authorNeighbor!=null)
			{
				int[] temp = new int[other.size()];
				for(int j=0;j<temp.length;j++)
					temp[j] = other.get(j);
				result[i]=calcJaccard(authorNeighbor,temp);
			}
			else
				result[i]=0;

		}
		
		return result;
		
	}
	
	
	public static Hashtable<Integer,JCScore> getJaccards(int authorid,int cid )
	{
		String sql_a = "Select index1 as cauthor from Coauthor where index2="+String.valueOf(authorid)+" union all Select index2 as cauthor from Coauthor where index1="+String.valueOf(authorid);
//		String sql11 = "Select index2 from Coauthor where index1="+String.valueOf(authorid);
//		String sql12 = "Select index1 from Coauthor where index2="+String.valueOf(authorid);
//		int[] authorNeighbor = addArray( ExcuteSQL.ExcuteSelect_IA(sql11, 1), ExcuteSQL.ExcuteSelect_IA(sql12, 1));
		int[] authorNeighbor = ExcuteSQL.ExcuteSelect_IA(sql_a, 1);
		String sql_c = "Select index1 as author,index2 as coauthor,n from Coauthor,Map,Author where Map.aminer_id=Coauthor.index1 and Map.aminer_id=Author.id and Map.conf_id="+String.valueOf(cid)+" union all Select index2 as author,index1 as coauthor,n from Coauthor,Map,Author where Map.aminer_id=Coauthor.index2 and Map.aminer_id=Author.id and Map.conf_id="+String.valueOf(cid)+" ORDER by author ASC,coauthor ASC";
		
		Common[] otherNeighbor = ExcuteSQL.ExcuteSelect_CM(sql_c);
		Hashtable<Integer,JCScore> result = new Hashtable<Integer,JCScore>();
		
		for(int i=0;i<otherNeighbor.length;i++)
		{
			Common other = otherNeighbor[i];
			ArrayList<Integer> tempA = other.getCommonList();
			int[] tempCommon = new int[tempA.size()];
			
			for(int j=0;j<tempCommon.length;j++)
				tempCommon[j] = tempA.get(j);
			
			JCScore temp = new JCScore(other.getName(),calcJaccard(authorNeighbor,tempCommon));
			result.put(other.getId(),temp);
		}
		
		return result;
		
	}
	
	
	
	
	public static double[] getJaccards(int authorid,int sid,int eid)
	{
		double[] result =new double[eid-sid+1];
		String sql11 = "Select index2 from Coauthor where index1="+String.valueOf(authorid);
		String sql12 = "Select index1 from Coauthor where index2="+String.valueOf(authorid);
		int[] authorNeighbor = addArray( ExcuteSQL.ExcuteSelect_IA(sql11, 1), ExcuteSQL.ExcuteSelect_IA(sql12, 1));
		;
		
		String sql_other = "Select * from (Select index1 as a1,index2 as a2 from Coauthor where index1 between "+sid+" and "+eid+" union all Select index2 as a1,index1 as a2 from Coauthor where index2 between "+sid+" and "+eid+" ) t1 ORDER by t1.a1 ASC";
		
		ArrayList<ArrayList<Integer>> otherNeighbor = ExcuteSQL.ExcuteSelect_MRA(sql_other,sid,eid);
		
		
		for(int i=0;i<otherNeighbor.size();i++)
		{
			ArrayList<Integer> other = otherNeighbor.get(i);
			if(other!=null && !other.isEmpty() && authorNeighbor!=null)
			{
				int[] temp = new int[other.size()];
				for(int j=0;j<temp.length;j++)
					temp[j] = other.get(j);
				result[i]=calcJaccard(authorNeighbor,temp);
			}
			else
				result[i]=0;

		}
		
		return result;
		
	}
	
	
	public static double getJaccard(int index1,int index2)
	{
		double result = 0.0;

		
		String sql11 = "Select index2 from Coauthor where index1="+String.valueOf(index1);
		String sql12 = "Select index1 from Coauthor where index2="+String.valueOf(index1);
		
		String sql21 = "Select index2 from Coauthor where index1="+String.valueOf(index2);
		String sql22 = "Select index1 from Coauthor where index2="+String.valueOf(index2);

		int[] author1 = addArray( ExcuteSQL.ExcuteSelect_IA(sql11, 1), ExcuteSQL.ExcuteSelect_IA(sql12, 1));
		int[] author2 = addArray( ExcuteSQL.ExcuteSelect_IA(sql21, 1), ExcuteSQL.ExcuteSelect_IA(sql22, 1) );
		
		
		if(author1!=null && author2!=null)
		{
			result = calcJaccard(author1,author2);
		}
		else
		{
			if(author1==null)
				System.out.println("Author "+index1+" is not existed,please enter a valid id!");
			if(author2==null)
				System.out.println("Author "+index2+" is not existed,please enter a valid id!");
			result=-1;
		}		
		
		return result;
	}
	
	
	private static int[] addArray(int[] a,int[] b)
	{
		int[] result = null;
		
		if(a!=null && b!=null)
		{
			result = new int[a.length+b.length];
			System.arraycopy(a, 0, result, 0, a.length);
			System.arraycopy(b, 0, result, a.length, b.length);
		}
		else
		{
			if(b!=null)
				return b;
			if(a!=null)
				return a;
		}
		
		return result;
		
	}

}
