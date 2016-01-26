package social;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import org.json.JSONObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WikiParser extends Parser
{

	public WikiParser(String url)
	{
		super(url);
	}
        
        public WikiParser(JSONObject jo)
        {
            super(jo);
        }
        
        public WikiParser()
        {
            super();
        }


	public boolean parse() 
	{
		URL docUrl = null;
		
		try 
		{
			docUrl = new URL(url);
			String html = getHtml.getStrbyUrl(docUrl,"",0);
			
			
			if(html.length()>3)
			{
				Document doc = Jsoup.parse(html);
				
				Elements content = doc.select("#mw-content-text");
				if(!content.isEmpty())
				{
					StringBuilder sb = new StringBuilder();
					Elements ps = content.select("p");
					for(Element p : ps)
					{
						sb.append(p.text());
						sb.append(" ");
					}
					this.jo.put("content", sb.toString());
				}
				
				Elements profile = doc.select("table.infobox");
				if(!profile.isEmpty())
				{
					Elements trs = profile.select("tr");
					for(Element tr : trs)
					{
						if(  (!tr.select("th").isEmpty()) && (!tr.select("td").isEmpty())  )
						{
							this.jo.put(tr.select("th").text(), tr.select("td").text());
						}
					}			
				}

			}
			else
			{
				return false;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		} 

		if(this.jo.length()<1)
			return false;

		
		
		return true;
	}


	public String getInfo() 
	{
		StringBuilder info = new StringBuilder();
		@SuppressWarnings("unchecked")
		Iterator<String> keys =   this.jo.keys();
		while(keys.hasNext())
		{
			String key = keys.next();
			info.append(this.jo.get(key));
			info.append(" ");
		}
		
		return info.toString();
	}
	
	
}
