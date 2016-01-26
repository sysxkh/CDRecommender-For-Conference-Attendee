package application;

//import java.sql.ResultSet;
//import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;

import dbHelper.ExcuteSQL;

public class CommonNeighbor 

{
	public static void main(String[] args)
	{
		if( args==null || args.length<2 )
		{
			System.out.println("Usage:");
			System.out.println("  args[0]: The first Author's ID");
			System.out.println("  args[1]: The second Author's ID");
			System.out.println("  args[2]: If you want to show CN list, 1 means show, other means not show");
			System.exit(0);
		}
		int a1 = Integer.valueOf(args[0]);
		int a2 = Integer.valueOf(args[1]);
		int show = 0;
		if(args.length==3 && Integer.valueOf(args[2])==1)
			show = 1;
		
		int[] result = getCommon(a1,a2,show);
		
		if(result.length==1 && result[0]!=-1 && result[0]!=-2)
		{
			System.out.println("There is only one common neighbor:");
			System.out.println("Author "+result[0]);
		}
		else if(result[0]==-1)
		{
			System.out.println("Those two Authors have no common neighbor !");
		}
		else if(result[0]==-2)
		{
			System.exit(0);
		}
		else
		{
			System.out.println("There are "+ result.length +" common neighbors :");
			for(int i=0;i<result.length;i++)
			{
				System.out.println("Author "+result[i]);
			}
		}	
		
	}
	
	
//	public static int totalAuthor()
//	{
//		int total = 0;
//		String sql = "select max(index1) from coauthor";
//		total=Integer.valueOf(ExcuteSQL.ExcuteSelect(sql, 1));
//		return total;
//	}
//	
//	private static int[][] getMatrix()
//	{
//		int total = totalAuthor();
//		int[][] matrix = new int[total][total];
//		for(int i=0;i<total;i++)
//		{
//			for (int j=0;j<total;j++)
//			{
//				matrix[i][j]=0;
//			}
//		}
//		String sql = "select index1,index2 from coauthor";
//		ResultSet rs = ExcuteSQL.ExcuteSelect_RS(sql);
//		try {
//			while(rs.next())
//				matrix[rs.getInt(1)][rs.getInt(2)] = 1;
//		} catch (SQLException e) {
//			System.out.println(e.toString());
//		}
//		return matrix;
//	}
	
	public static TreeMap<Integer,ArrayList<Integer>> getCommons(int sid,int eid,int authorid)
	{
		TreeMap<Integer,ArrayList<Integer>> result = new TreeMap<Integer,ArrayList<Integer>>();
		String sql = "Select t1.a1 as ohter,t2.a1 as author,t1.a2 as common from (Select index1 as a1,index2 as a2 from Coauthor where index1 between "+sid+" and "+eid+" union all Select index2 as a1,index1 as a2 from Coauthor where index2 between "+sid+" and "+eid+" ) t1,(Select index1 as a1,index2 as a2 from Coauthor where index1="+authorid+"  union all Select index2 as a1,index1 as a2 from Coauthor where index2="+authorid+") t2 where t1.a2=t2.a2 ORDER by t1.a1 ASC";
		ExcuteSQL.ExcuteSelect_TM(result, sql);
		
		return result;
	}
	
	public static int[] getHop2Neighbor(int authorid)
	{
		String sql_h2 = " Select distinct hop2 from (Select distinct index2 as hop2 from Coauthor where index1 in (Select a2 from (Select index1 as a1,index2 as a2 from Coauthor where index1="+String.valueOf(authorid)+" union all Select index2 as a1,index1 as a2 from Coauthor where index2="+String.valueOf(authorid)+" ) t1) union all Select distinct index1 as hop2 from Coauthor where index2 in (Select a2 from (Select index1 as a1,index2 as a2 from Coauthor where index1="+String.valueOf(authorid)+" union all Select index2 as a1,index1 as a2 from Coauthor where index2="+String.valueOf(authorid)+" ) t2) union all Select distinct a2 as hop2 from (Select index1 as a1,index2 as a2 from Coauthor where index1="+String.valueOf(authorid)+" union all Select index2 as a1,index1 as a2 from Coauthor where index2="+String.valueOf(authorid)+" ) t4 )  t3  ORDER by hop2 ASC";
		//String sql_h2 = " Select distinct hop2 from (Select distinct index2 as hop2 from Coauthor where index1 in (Select a2 from (Select index1 as a1,index2 as a2 from Coauthor where index1="+String.valueOf(authorid)+" union all Select index2 as a1,index1 as a2 from Coauthor where index2="+String.valueOf(authorid)+" ) t1) union all Select distinct index1 as hop2 from Coauthor where index2 in (Select a2 from (Select index1 as a1,index2 as a2 from Coauthor where index1="+String.valueOf(authorid)+" union all Select index2 as a1,index1 as a2 from Coauthor where index2="+String.valueOf(authorid)+" ) t2) ) t3 ORDER by hop2 ASC";
		int[] authorNeighbor = ExcuteSQL.ExcuteSelect_IA(sql_h2, 1);
		return authorNeighbor;
	}
	
	public static int[]	getHasCNList(int sid,int eid,int authorid)
	{
		String sql = "Select distinct t1.a1 as ohter from (Select index1 as a1,index2 as a2 from Coauthor where index1 between "+sid+" and "+eid+" union all Select index2 as a1,index1 as a2 from Coauthor where index2 between "+sid+" and "+eid+" ) t1,(Select index1 as a1,index2 as a2 from Coauthor where index1="+authorid+"  union all Select index2 as a1,index1 as a2 from Coauthor where index2="+authorid+") t2 where t1.a2=t2.a2 ORDER by t1.a1 ASC";
		int [] result = ExcuteSQL.ExcuteSelect_IA(sql, 1);
		return result;
	}
	
	public static int[] getCommon(int index1,int index2,int show)
	{
		ArrayList<Integer> common = new ArrayList<Integer>();
		int[] result = null;
//		int[][] matrix = getMatrix();
//		int[] author1 = matrix[index1];
//		int[] author2 = matrix[index2];
//		total = author1.length;
//		for(int i=0;i<total;i++)
//		{
//			if(author1[i]==author2[i])
//				overLapping++;
//		}
		String sql11 = "Select index2 from Coauthor where index1="+String.valueOf(index1);
		String sql12 = "Select index1 from Coauthor where index2="+String.valueOf(index1);
		
		String sql21 = "Select index2 from Coauthor where index1="+String.valueOf(index2);
		String sql22 = "Select index1 from Coauthor where index2="+String.valueOf(index2);

		int[] author1 = addArray( ExcuteSQL.ExcuteSelect_IA(sql11, 1), ExcuteSQL.ExcuteSelect_IA(sql12, 1));
		int[] author2 = addArray( ExcuteSQL.ExcuteSelect_IA(sql21, 1), ExcuteSQL.ExcuteSelect_IA(sql22, 1) );
		
		if(author1!=null && author2!=null)
		{
			if(show==1)
			{
				System.out.println("The author "+index1+" has CN:");
				showArray(author1);
				System.out.println("The author "+index1+" has CN:");
				showArray(author2);
			}
			
			HashSet<Integer> coauthor = new HashSet<Integer>();
			for(int i=0;i<author1.length;i++)
			{
				coauthor.add(author1[i]);
			}
			for(int i=0;i<author2.length;i++)
			{
				boolean co = coauthor.add(author2[i]);
				if(co==false)
					common.add(author2[i]);
			}
			
			if(common.size()!=0)
				result = new int[common.size()];
			else
			{
				result = new int[1];
				result[0]=-1;
			}
				
			for(int i=0;i<common.size();i++)
			{
				result[i]=common.get(i);
			}
		}
		else
		{
			result = new int[1];
			result[0]=-2;
			if(author1==null)
				System.out.println("Author "+index1+" is not existed,please enter a valid id!");
			if(author2==null)
				System.out.println("Author "+index2+" is not existed,please enter a valid id!");
		}
		return result;
		
	}
	
	private static void showArray(int[] a)
	{
		if(a!=null)
		{
			for(int i=0;i<a.length;i++)
			{
				System.out.println(a[i]);
			}
		}
		else
			System.out.println("No result");
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
