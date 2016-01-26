package social;

import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ResearchGateParser extends Parser
{
	public ResearchGateParser(String url)
	{
		super(url);
	}
        
         public ResearchGateParser(JSONObject jo)
        {
            super(jo);
        }
         public ResearchGateParser()
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
				
				Elements profile = doc.select("h1.profile-header-name");
				if(!profile.isEmpty())
				{
					Elements as = profile.get(0).select("a");
					for(Element a : as)
					{
						if(a.attr("itemprop").equals("name"))
							this.jo.put("name", a.text());
						if(a.attr("itemprop").equals("url"))
							this.jo.put("institution", a.text());
					}
				}
				
				Elements skills = doc.select("div.profile-skills").select("ul");
				if(!skills.isEmpty())
				{
					JSONArray ja = new JSONArray();
					Elements lis = skills.select("li");
					for(Element li : lis)
					{
						ja.put(li.select("div").first().select("a").first().text());
					}
					this.jo.put("skills", ja);
				}
				
				Elements topics = doc.select("ul.keyword-list");
				if(!topics.isEmpty())
				{
					JSONArray ja = new JSONArray();
					Elements lis = topics.select("li");
					for(Element li : lis)
					{
						ja.put(li.select("div").first().select("a").first().text());
					}
					this.jo.put("topics", ja);
				}
                                
                                Elements publications = doc.select("span.publication-title");
                                if(!publications.isEmpty())
                                {
                                    JSONArray ja = new JSONArray();
                                    for(Element span : publications)
                                    {
                                        ja.put(span.text());
                                    }
                                    this.jo.put("publications", ja);
                                }
                                
                                Elements topcoauthors = doc.select("div.js-coauthor-container");
                                if(!topcoauthors.isEmpty())
                                {
                                    JSONArray ja = new JSONArray();
                                    Elements lis = topcoauthors.select("li");
                                    for(Element li : lis)
                                    { 
                                        if(!li.select("h5").select("a").isEmpty())
                                            ja.put(li.select("h5").select("a").first().text());
                                    }
                                    this.jo.put("topcoauthors", ja);
                                }
                                
			}
			else
			{
				return false;
			}
			
		}catch (MalformedURLException e) {
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
		JSONArray ja = null;
		if(jo.has("skills"))
		{
			ja = this.jo.getJSONArray("skills");
			for(int i=0;i<ja.length();i++)
			{
				info.append(ja.getString(i));
				info.append(" ");
			}
		}
		if(jo.has("topics"))
		{
			ja = this.jo.getJSONArray("topics");
			for(int i=0;i<ja.length();i++)
			{
				info.append(ja.getString(i));
				info.append(" ");
			}
		}
		
		if(jo.has("institution"))
		{
			info.append(this.jo.getString("institution"));
			info.append(" ");
		}
                
                if(jo.has("publications"))
		{
			ja = this.jo.getJSONArray("publications");
			for(int i=0;i<ja.length();i++)
			{
				info.append(ja.getString(i));
				info.append(" ");
			}
		}
                

                
		return info.toString();
	}
        
        public String[] getNeighbor()
        {
            if(this.jo.has("topcoauthors"))
            {
                JSONArray ja = jo.getJSONArray("topcoauthors");
                String[] neighbor = new String[ja.length()];
                for(int i=0;i<neighbor.length;i++)
                {
                    neighbor[i]=ja.getString(i);
                }
                return neighbor;
            }
            else
                return null;
        }
}
