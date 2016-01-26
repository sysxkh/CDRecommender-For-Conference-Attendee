package application;

import dbHelper.ExcuteSQL;

public class IsInConference
{

	public static boolean isIn(int id,int cid)
	{
		boolean isin = false;
		String sql = "Select count(*) from Map where aminer_id="+String.valueOf(id)+" and conf_id="+String.valueOf(cid);
		String num = ExcuteSQL.ExcuteSelect(sql, 1);
		if(!num.equals("0"))
			isin = true;	
		return isin;
	}
}
