package social;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class getHtml 
{
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36";
	
	public static String getStrbyUrl(URL url,String cookie,int times)
	{
            if(times>10)
                return "";
            if(url.toExternalForm().contains("ftp://"))
                return "";
            HttpURLConnection connection = null;
            StringBuilder strBuilder = new StringBuilder();
            try
            {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.setInstanceFollowRedirects(false);
            if(!cookie.isEmpty())
                connection.setRequestProperty("Cookie", cookie);


            if(connection.getResponseCode()==200)
            {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String str = in.readLine();
                int i = 0;
                while (str != null && i<=20971520) 
                {                    
                    i = i + str.length();
                    strBuilder.append(str);
                    str = in.readLine();
                }
            }
            else if(connection.getResponseCode()==301 || connection.getResponseCode()==302)
            {
            	String reLocation = connection.getHeaderField("Location");
                if(reLocation.equals(url.toString()))
                    return "";
            	System.out.println("Relocation to the link : "+reLocation);
                if(reLocation.contains("login"))
                    return "";
            	URL reUrl = new URL(reLocation);
                times++;
            	return getStrbyUrl(reUrl,cookie,times);
            }
            else
            {
                strBuilder.append(String.valueOf(connection.getResponseCode()));
            }
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		return strBuilder.toString();
	}

}
