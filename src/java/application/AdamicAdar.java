package application;

import dbHelper.ExcuteSQL;

public class AdamicAdar 
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
		double result = getAA(a1,a2);
		
		if(result==-1)
		{
			System.exit(0);
		}
		else
		{
			System.out.println("The Adamic/Adar between those two authors is "+result);
		}
		
	}
	

	public static double getAA(int index1,int index2)
	{
		
		double result = 0.0;
		
		int[] z = CommonNeighbor.getCommon(index1, index2,0);
		
		if(z[0]!=-1 && z[0]!=-2)
		{
			for(int i=0;i<z.length;i++)
			{
				String sql1 = "select count(*) from Coauthor where index1="+String.valueOf(z[i]);
				String sql2 = "select count(*) from Coauthor where index2="+String.valueOf(z[i]);
				int num = Integer.valueOf( ExcuteSQL.ExcuteSelect(sql1, 1) );
				num = num + Integer.valueOf( ExcuteSQL.ExcuteSelect(sql2, 1) );
				if(num!=1)
					result = result + (1/Math.log(num));
				else
					continue;
			}	
		}
		else
		{
			if(z[0]==-1)
				System.out.println("There is not any common neighbor between those two authors!");
			result=-1;
		}
	
		return result;
	}

}
