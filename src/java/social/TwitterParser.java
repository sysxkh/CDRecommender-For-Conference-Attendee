package social;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import twitter4j.PagableResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

public class TwitterParser extends Parser
{
	private Twitter twitter;

	
	public TwitterParser(String url)
	{
		super(url);
		this.twitter = new TwitterFactory().getInstance();
                
	}
        
        public TwitterParser(JSONObject jo)
        {
            super(jo);
        }
        
        public TwitterParser()
        {
            super();
        }
	
	public boolean parse()
	{
		List<Status> statuses = null ;
		JSONArray ja = null ;
		try 
		{   
                        String name = "" ;
                        if(url.contains("?"))
                        {
                            name = url.substring(19,url.indexOf("?"));
                        }
                        else
                            name = url.substring(19);
                        System.out.println("Twitter name  :" +name);
			statuses = twitter.getUserTimeline(name);
			ja = new JSONArray();
			 for (Status status : statuses) 
			 {
			     ja.put(status.getText());
			 }
			this.jo.put("timelines", ja);
			
			statuses = twitter.getFavorites(name);
			ja = new JSONArray();
			 for (Status status : statuses) 
			 {
			     ja.put(status.getText());
			 }
			 this.jo.put("favorites", ja);
			 
			 PagableResponseList<User> friendsList = twitter.getFriendsList(name, -1);
			 ja = new JSONArray();
			 for(User friend : friendsList)
			 {
				 ja.put(friend.getScreenName());
			 }
			 this.jo.put("friends", ja);
			 
		} catch (TwitterException e) {
			
			e.printStackTrace();
			return false;
		}
		
		
		return true;
	}
	
	public String getInfo()
	{
		StringBuilder info = new StringBuilder();
                if(this.jo.has("timelines"))
                {
                    JSONArray tl = this.jo.getJSONArray("timelines");
                    for(int i=0;i<tl.length();i++)
                    {
                            info.append(tl.get(i));
                            info.append(" ");
                    }
                }
                if(this.jo.has("favorites"))
                {
                    JSONArray tl = this.jo.getJSONArray("favorites");
                    for(int i=0;i<tl.length();i++)
                    {
                            info.append(tl.get(i));
                            info.append(" ");
                    }
                }
		return info.toString();
	}
        
        public String[] getNeighbor()
        {
            if(this.jo.has("friends"))
            {
                JSONArray ja = jo.getJSONArray("friends");
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
