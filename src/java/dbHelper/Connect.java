package dbHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect 
{
    	public static Connection connect_Social()
	{
        String url = "";  
        Connection con = null;
        String OS = System.getProperty("os.name").toLowerCase();
        String user = "";
        String pwd = "";
        if(OS.indexOf("linux")>=0)
        {
            url = "jdbc:mysql://localhost:3306/Google";
            user = "kex3";
            pwd = "kex3123";
        }
        if(OS.indexOf("windows")>=0)
        {
            url = "jdbc:mysql://localhost:3306/google";
            user = "root";
            pwd = "123456";
        }
	
	    try{
	           // Class.forName("org.gjt.mm.mysql.Driver").newInstance();
	           Class.forName("com.mysql.jdbc.Driver");
	           //html.append("mysql working ...");
	    }
	    catch (Exception ex){
	    	System.out.println(ex.toString());
	
	    }
	    try
	    {           
	        con = DriverManager.getConnection(url,user,pwd);
	    }
	    catch (SQLException ex){
	    	System.out.println(ex.toString());
	    }
	       
	    return con;
	}
    
    
	public static Connection connect_Academic()
	{
            String url = "";  
            Connection con = null;
            String OS = System.getProperty("os.name").toLowerCase();
            String user = "";
            String pwd = "";
            if(OS.indexOf("linux")>=0)
            {
                url = "jdbc:mysql://halley.exp.sis.pitt.edu/cn3_social";
                user = "kex3";
                pwd = "kex3123";
            }
            if(OS.indexOf("windows")>=0)
            {
                url = "jdbc:mysql://localhost:3306/db_aminer";
                user = "root";
                pwd = "123456";
            }

        try{
               // Class.forName("org.gjt.mm.mysql.Driver").newInstance();
               Class.forName("com.mysql.jdbc.Driver");
               //html.append("mysql working ...");
        }
        catch (Exception ex){
        	System.out.println(ex.toString());

        }
        try
        {           
            con = DriverManager.getConnection(url,user,pwd);
        }
        catch (SQLException ex){
        	System.out.println(ex.toString());
        }
           
        return con;
	}

}
