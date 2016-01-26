package social;

import org.json.JSONObject;



public abstract class Parser 
{
	protected String url;
	protected JSONObject jo;
	
	public Parser(String url)
	{
		if(url.startsWith("https://"))
		{
			url = "http://" + url.substring(8);
		}
		if(!url.startsWith("http://"))
		{
			url = "http://" + url;
		}
		this.setUrl(url);
		this.jo = new JSONObject();
	}
        
        public Parser(JSONObject jo)
        {
            this.jo = jo;
        }
        
        public Parser()
        {
            this.jo = new JSONObject("{}");
        }
	
	public abstract boolean parse();
	public abstract String getInfo();
	
	public String getJson()
	{
            return jo.toString();
	}
        
        public void setJson(JSONObject jo)
        {
            this.jo = jo;
        }
                
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	

}
