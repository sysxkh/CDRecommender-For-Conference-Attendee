package application;

import dbHelper.ExcuteSQL;

public class HasPaper 
{
	public static boolean hasPaper(int id)
	{
		boolean has = false;
		String sql = "Select count(*) from Author2Paper where authorid="+String.valueOf(id);
		String num = ExcuteSQL.ExcuteSelect(sql, 1);
		if(!num.equals("0"))
			has = true;	
		return has;
	}
}
