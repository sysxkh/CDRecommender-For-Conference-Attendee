package dbHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.TreeMap;

import resultInfo.Common;
import resultInfo.Content;
import resultInfo.Monitor;

public class ExcuteSQL 
	
{
    
        public static ArrayList<Monitor> ExcuteSelect_ID(String pid,String token,String sql)
	{
		ArrayList<Monitor> result = new ArrayList<Monitor>();
		
		Connection con = Connect.connect_Social();
		Statement stat = null;
		ResultSet rs = null;
		Monitor temp = null;
		
		try 
		{
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			
			if(rs.next())
			{
				rs.beforeFirst();
				
				while(rs.next())
				{
					temp = new Monitor(rs.getString(1));
					temp.setTitle(rs.getString(2));
					temp.setUrl(rs.getString(3));
					result.add(temp);
				}
	
			}
			else
				result=null;
			
		} catch (SQLException e) {
			System.out.println(e.toString());
		}	
		
		return result;
		
	}
	
	public static void ExcuteSelect_TM(TreeMap<Integer,ArrayList<Integer>> result,String sql)
	{
		Connection con = Connect.connect_Academic();
		Statement stat = null;
		ResultSet rs = null;
		ArrayList<Integer> temp = null;
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			
			if(rs.next())
			{
				rs.beforeFirst();
				
				while(rs.next())
				{
					int id = rs.getInt("ohter");
					if(result.containsKey(id))
						result.get(id).add(rs.getInt("common"));
					else
					{
						temp = new ArrayList<Integer>();
						temp.add(rs.getInt("common"));
						result.put(id, temp);
					}
				}
	
			}
			else
				result=null;
			
		} catch (SQLException e) {
			System.out.println(e.toString());
		}	
		
	}
	
	public static ArrayList<ArrayList<Integer>> ExcuteSelect_MRA(String sql,int sid,int eid)
	{
		int total = eid-sid+1;
		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>(total);
		Connection con = Connect.connect_Academic();
		Statement stat = null;
		ResultSet rs = null;
		ArrayList<Integer> sub = null;
		for(int i=0;i<total;i++)
		{
			result.add(null);
		}
		
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			
			if(rs.next())
			{
				rs.beforeFirst();			
				while(rs.next())
				{
					int id = rs.getInt(1)-sid;
					if(result.get(id)!=null && !result.get(id).isEmpty())
						result.get(id).add(rs.getInt(2));
					else
					{
						sub = new ArrayList<Integer>();
						sub.add(rs.getInt(2));
						result.set(id, sub);
					}
				}
			}
			else
				result = null;
			
			} catch (SQLException e) {
				System.out.println(e.toString());
			}		
		
		return result;
		
	}
	public static String[] ExcuteSelect_MRS(String sql,int sid,int eid)
	{
		int total = eid - sid +1;
		String[] result = new String[total];	
		Connection con = Connect.connect_Academic();
		Statement stat = null;
		ResultSet rs = null;
		
		StringBuilder ab = null;
		
		for(int i=0;i<result.length;i++)
		{
			result[i]="noauthor";
		}
		
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			
			
			if(rs.next())
			{
				int id = rs.getInt(1); 
				int index = id-sid;
				ab = new StringBuilder();
				ab.append(rs.getString(2));
				ab.append(" ");
				
				while(rs.next())
				{
					int nextid = rs.getInt(1);
					if(id!=nextid)
					{
						result[index] = ab.toString();
						index = index + (nextid-id);
						ab = new StringBuilder();
						id = rs.getInt(1);
						ab.append(rs.getString(2));
						ab.append(" ");
					}
					else
					{
						ab.append(rs.getString(2));
						ab.append(" ");
					}
	
				}	
			}
			else
				result=null;
			
			con.close();
			stat.close();
			rs.close();
			
		} catch (SQLException e) {
			System.out.println(e.toString());
		}
		return result;
	}
	
	
	public static Common[] ExcuteSelect_CM(String sql)
	{
		ArrayList<Common> result = new ArrayList<Common>();
		Common temp = null;
		Connection con = Connect.connect_Academic();
		Statement stat = null;
		ResultSet rs = null;
		
		
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			
			
			if(rs.next())
			{
				temp = new Common(rs.getInt(1),rs.getString(3));
				temp.getCommonList().add(rs.getInt(2));
				while(rs.next())
				{
					if(temp.getId()!=rs.getInt(1))
					{
						result.add(temp);
						temp = new Common(rs.getInt(1),rs.getString(3));
						temp.getCommonList().add(rs.getInt(2));
					}
					else
					{
                                                if(rs.getInt(2)==temp.getCommonList().get(temp.getCommonList().size()-1))
                                                    continue;
                                                else
                                                    temp.getCommonList().add(rs.getInt(2));
					}

				}	
			}
			else
				result=null;
			
			con.close();
			stat.close();
			rs.close();
			
		} catch (SQLException e) {
			System.out.println(e.toString());
		}
		
		Common[] common = null;
		if(result!=null)
		{
			common = new Common[result.size()];
			result.toArray(common);
		}
		
		return common;
	}
	
	
	public static Content[] ExcuteSelect_C(String sql)
	{
		ArrayList<Content> result = new ArrayList<Content>();
		Content temp = null;
		Connection con = Connect.connect_Academic();
		Statement stat = null;
		ResultSet rs = null;
		
		
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			
			
			if(rs.next())
			{
				temp = new Content(rs.getInt(1),rs.getString(2),rs.getString(3));
				
				while(rs.next())
				{
					if(temp.getId()!=rs.getInt(1))
					{
						result.add(temp);
						temp = new Content(rs.getInt(1),rs.getString(2),rs.getString(3));
					}
					else
					{
						temp.setContent(temp.getContent()+" "+rs.getString(2));
					}

				}	
			}
			else
				result=null;
			
			con.close();
			stat.close();
			rs.close();
			
		} catch (SQLException e) {
			System.out.println(e.toString());
		}
		
		Content[] contents = null;
		if(result!=null)
		{
			contents = new Content[result.size()];
			result.toArray(contents);
		}
		
		return contents;
	}
	
	public static String ExcuteSelect(String sql,String ColName)
	{
		String result = "";	
		Connection con = Connect.connect_Academic();
		Statement stat = null;
		ResultSet rs = null;
		
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			if(rs.next()!=false)
				result=rs.getString(ColName);
			else
				result="No Result";
			
			con.close();
			stat.close();
			rs.close();
			
		} catch (SQLException e) {
			System.out.println(e.toString());
		}
		return result;
	}
	
	public static String ExcuteSelect(String sql,int ColNum)
	{
		String result = "";	
		Connection con = Connect.connect_Academic();
		Statement stat = null;
		ResultSet rs = null;
		
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData() ;   
			int colCount = rsmd.getColumnCount();  
			if(rs.next()!=false)
				if(ColNum>colCount)
					result="No Such Colum";
				else
					result=rs.getString(ColNum);
			else
				result="";
			
			con.close();
			stat.close();
			rs.close();
			
		} catch (SQLException e) {
			System.out.println(e.toString());
		}
		return result;
	}
	
	public static ResultSet ExcuteSelect_RS(String sql)
	{
		Connection con = Connect.connect_Academic();
		Statement stat = null;
		ResultSet rs = null;
		
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			
			con.close();
			stat.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return rs;
	}
	
	public static int[] ExcuteSelect_IA(String sql,int ColNum)
	{
		Connection con = Connect.connect_Academic();
		Statement stat = null;
		ResultSet rs = null;
		int[] result = null;
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			rs.last();
			if(rs.getRow()>0)
			{
				result = new int[rs.getRow()];
				rs.first();
				int i = 0;
				do
				{
					result[i]=rs.getInt(ColNum);
					i++;
				}
				while(rs.next());
			}
//			else
//			{
//				result = new int[1];
//				result[0]=-1;
//			}
			
			con.close();
			stat.close();
			rs.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public static String[] ExcuteSelect_SA(String sql,int ColNum)
	{
		Connection con = Connect.connect_Academic();
		Statement stat = null;
		ResultSet rs = null;
		String[] result = null;
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			rs.last();
			if(rs.getRow()>0)
			{
				result = new String[rs.getRow()];
				rs.first();
				int i = 0;
				do
				{
					result[i]=rs.getString(ColNum);
					i++;
				}
				while(rs.next());
			}

			
			con.close();
			stat.close();
			rs.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
}
