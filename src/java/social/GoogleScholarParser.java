package social;

import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class GoogleScholarParser extends Parser
{

	public GoogleScholarParser(String url)
	{
		super(url);
	}
        public GoogleScholarParser(JSONObject jo)
        {
            super(jo);
        }
        public GoogleScholarParser()
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
				
				Elements profile = doc.select("#gsc_prf_i");
				if(!profile.isEmpty())
				{
					this.jo.put("name", profile.select("#gsc_prf_in").text());
					this.jo.put("position", profile.select(".gsc_prf_il").first().text());
					JSONArray ja = new JSONArray();
					Elements interests = profile.select(".gsc_prf_il").get(1).select("a");
					for(Element e : interests)
					{
						ja.put(e.text());
					}
					this.jo.put("interests", ja);
				}
	
				Elements citations = doc.select("#gsc_rsb_st");
				if(!citations.isEmpty())
				{
					Elements tr = citations.select("tr");
					
					for(int i=1;i<tr.size();i++)
					{
						Elements td = tr.get(i).select("td");
						this.jo.put(td.get(0).text().toLowerCase(), td.get(1).text());
					}
				}
				
				Elements coauthors = doc.select("#gsc_rsb_co");
				if(!coauthors.isEmpty())
				{
					String viewAll ="http://scholar.google.com" + coauthors.select("a.gsc_rsb_lc").attr("href");
					URL viewAllUrl = new URL(viewAll);
					Document allCoauthors = Jsoup.parse(getHtml.getStrbyUrl(viewAllUrl,"",0));
					Elements authors = allCoauthors.select("#gsc_ccl").select("h3.gsc_1usr_name");
					JSONArray ja = new JSONArray();
					for(Element a : authors)
					{
						ja.put(a.text());
					}
					this.jo.put("coauthors", ja);
				}
				
				Elements titlesTable = doc.select("#gsc_a_b");
				if(!titlesTable.isEmpty())
				{
					JSONArray ja = new JSONArray();
					Elements title = titlesTable.select("a.gsc_a_at");
					for(Element t : title)
					{
						ja.put(t.text());
					}
					this.jo.put("titles", ja);
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
		JSONArray ja = null;
		
		if(jo.has("interests"))
		{
			ja = this.jo.getJSONArray("interests");
			for(int i=0;i<ja.length();i++)
			{
				info.append(ja.getString(i));
				info.append(" ");
			}
		}
		if(jo.has("titles"))
		{
			ja = this.jo.getJSONArray("titles");
			for(int i=0;i<ja.length();i++)
			{
				info.append(ja.getString(i));
				info.append(" ");
			}
		}
		if(jo.has("position"))
		{
			info.append(jo.getString("position"));
		}
		return info.toString()	;
	}
        
        public String[] getNeighbor()
        {
            if(this.jo.has("coauthors"))
            {
                JSONArray ja = jo.getJSONArray("coauthors");
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
