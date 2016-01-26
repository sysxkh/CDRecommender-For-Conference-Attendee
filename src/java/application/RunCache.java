package application;

import java.util.ArrayList;

import dbHelper.ExcuteSQL;

public class RunCache 
{
	public static void main(String[] args)
	{
		if(args==null || args.length<2)
		{
			System.out.println("args[0] is the conference id !");
			System.out.println("args[1] is the Thread Number !");
			System.exit(0);
		}
		int cid = Integer.valueOf( args[0] );
		int threadNumber = Integer.valueOf( args[1] );
		String sql = "Select aminer_id from Map where conf_id="+args[0];
		int[] authorlist = ExcuteSQL.ExcuteSelect_IA(sql, 1);
		ArrayList<Integer> dAuthorList =  getDistinct(authorlist);
		
		int size = dAuthorList.size()/threadNumber;
		for(int i=0;i<threadNumber;i++)
		{
			int[] temp = new int[size];
			for(int j=0;j<size;j++)
			{
				temp[j] = dAuthorList.remove(0);
			}
			new CacheRL(temp,cid).start();
		}
		if(!dAuthorList.isEmpty())
		{
			int [] temp = new int[dAuthorList.size()];
			for(int j=0;j<temp.length;j++)
			{
				temp[j] = dAuthorList.remove(0);
			}
			new CacheRL(temp,cid).start();
		}
		
	}
	
	public static ArrayList<Integer> getDistinct(int[] al)
	{
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i=0;i<al.length;i++)
		{
			if(list.contains(al[i]) || al[i]==0)
				continue;
			else
				list.add(al[i]);
		}
		return list;
	}

}
