package social;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GoogleSearch 
{
	private String query;
	private JSONArray googleSearchResult;
	//private static final String searchUrl = "https://www.google.com/search?num=100&q=";
        private static final String apiUrl = "https://www.googleapis.com/customsearch/v1?num=10&key=*******"; // use your own token here for google customer search.
            // or you can just call the searchUrl for seach, it could make you be banned or rederict to a robot test page.
	public GoogleSearch(String query)
	{
		this.setQuery(query);
		googleSearchResult = new JSONArray();
	}
        
        public GoogleSearch(JSONArray googleSearchResult)
        {
            this.googleSearchResult = googleSearchResult;
        }
        
        public boolean parse()
	{
		int index = 0;
		for(int i=1;i<=100;i=i+10)
		{
			try 
			{				
				URL url = new URL(apiUrl+"&q="+query.replace(" ", "+")+"&start="+String.valueOf(i));
                                String search = getHtml.getStrbyUrl(url, "",0);
                                int times = 0;
                                while( search.isEmpty() || !(search.trim().charAt(0)=='{'))
                                {
                                    if(times>5)
                                        continue;
                                    System.out.println(url.toString());
                                    search = getHtml.getStrbyUrl(url, "",0);
                                    times++;
                                }
				JSONObject searchResult = new JSONObject(search);
				JSONArray items = searchResult.getJSONArray("items");
				for(int j=0; j<items.length();j++)
				{
					JSONObject temp = items.getJSONObject(j);
					if(temp.has("mime"))
						continue;
					JSONObject result = new JSONObject();
                                        if(temp.has("title"))
                                        {
                                            result.put("title", temp.getString("title"));
                                            result.put("url", temp.getString("link"));
                                            result.put("quote", temp.getString("snippet"));
                                            result.put("index", index++);
                                            googleSearchResult.put(result);
                                        }
                                        else
                                        {
                                            if(temp.has("index"))
                                            {
                                                result.put("quote", temp.getString("snippet"));
                                                result.put("index", index++);
                                                googleSearchResult.put(result);
                                            }
                                        }

				}
				
				
			} catch (MalformedURLException e) {
				
				e.printStackTrace();
				return false;
			}

		}
		
		
		return true;
		
	}
	
//	public boolean parse()
//	{
//		try
//		{			
//			URL url = new URL(searchUrl+query.replace(" ", "+"));
//			System.out.println(url.toString());
//			Document doc = Jsoup.parse(getHtml.getStrbyUrl(url, ""));			
//			Elements divs = doc.select("#rso").select("div.g");		
//			if(!divs.isEmpty())
//			{
//				int index = 0;
//			
//				for(Element div : divs)
//				{					
//					Element title = div.select("h3.r").select("a").first();
//					System.out.println(div.select("h3.r").first().text());
//					String t = title.text();					
//					if(div.select("h3.r").first().text().contains("[PDF]"))
//						continue;
//					else
//					{											
//						String pageUrl = title.attr("href");	
//                                                if(pageUrl.startsWith("/url?"))
//                                                    pageUrl = title.attr("data-href");
//						String quote = div.select("span.st").first().text();
//						JSONObject result = new JSONObject();
//						result.put("title", t);
//						result.put("url",pageUrl);
//						result.put("quote", quote);
//						result.put("index", index++);
//						googleSearchResult.put(result);	
//						
//					}											
//				}
//			}
//			else
//				return false;
//	
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//			return false;
//		} 
//
//		return true;
//	}
	
	public boolean cache(String dir)
	{

		try 
		{
						
			File searchResult = new File(dir+File.separator+"GoogleSearch.json");
			if(!searchResult.exists())
				searchResult.createNewFile();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(searchResult.getAbsolutePath()),"UTF-8")); 
			writer.write(googleSearchResult.toString());
			writer.close();
			
			for(int i=0;i<googleSearchResult.length();i++)
			{
				JSONObject jo = googleSearchResult.getJSONObject(i);				
				URL page = new URL(jo.getString("url"));
				String pageCache = getHtml.getStrbyUrl(page, "",0);												
				File cache = new File(dir+File.separator+String.valueOf(jo.getInt("index"))+".html");						
				if(!cache.exists())
					cache.createNewFile();
				writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(cache.getAbsolutePath()),"UTF-8")); 
				writer.write(pageCache);
				writer.close();	
			}
			
			
		} catch (IOException e) {
		
			e.printStackTrace();
			return false;
		}

		
		
		
		return true;
	}
	
	

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public JSONArray getGoogleSearchResult() {
		return googleSearchResult;
	}

	public void setResult(JSONArray googleSearchResult) {
		this.googleSearchResult = googleSearchResult;
	}



}
