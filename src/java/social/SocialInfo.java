package social;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class SocialInfo 
{
	private String name;
//	private static final String token = "94365990226b28dbb57b60ecae24f85ae2b1f445";
	//private static final String apiUrl = "http://readability.com/api/content/v1/parser?";
        private static final String apiUrl = "http://picso.org:8889/~highman/GoogleMonitor/Read.php?url=";

        private String infomation;
        private String dir;

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }
        private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public JSONArray getNormal() {
        return normal;
    }

    public void setNormal(JSONArray normal) {
        this.normal = normal;
    }
        private TwitterParser tp;
        private GoogleScholarParser gsp;
        private ResearchGateParser rgp;
        private WikiParser wp;
        private JSONArray normal;
        private GoogleSearch googlesearchresult;

    public GoogleSearch getGooglesearchresult() {
        return googlesearchresult;
    }

    public void setGooglesearchresult(GoogleSearch googlesearchresult) {
        this.googlesearchresult = googlesearchresult;
    }

    public String getInfomation() {
        return infomation;
    }

    public void setInfomation(String infomation) {
        this.infomation = infomation;
    }


    public TwitterParser getTp() {
        return tp;
    }

    public void setTp(TwitterParser tp) {
        this.tp = tp;
    }

    public GoogleScholarParser getGsp() {
        return gsp;
    }

    public void setGsp(GoogleScholarParser gsp) {
        this.gsp = gsp;
    }

    public ResearchGateParser getRgp() {
        return rgp;
    }

    public void setRgp(ResearchGateParser rgp) {
        this.rgp = rgp;
    }

    public WikiParser getWp() {
        return wp;
    }

    public void setWp(WikiParser wp) {
        this.wp = wp;
    }
    
    

	
	public SocialInfo(String name,int type,String dir)
	{
		this.setName(name);
                this.setGsp(null);
                this.setRgp(null);
                this.setTp(null);
                this.setWp(null); 
                this.setType(type);
                this.infomation="";
                this.normal = new JSONArray();
                this.dir = dir;
	}
        
        public SocialInfo()
        {
            this.setGsp(null);
            this.setRgp(null);
            this.setTp(null);
            this.setWp(null);      
            this.setType(-1);
            this.normal = new JSONArray();
            this.infomation = "";
        }
        
        public boolean getInfo(String dir)
        {
                File gsJSON = new File(dir+File.separator+"GoogleScholar.json");
                File rgJSON = new File(dir+File.separator+"ResearchGate.json");
                File tJSON = new File(dir+File.separator+"Twitter.json");
                File wJSON = new File(dir+File.separator+"Wiki.json");
                File nJSON = new File(dir+File.separator+"Normal.json");
                File gJSON = new File(dir+File.separator+"GoogleSearch.json");
                
                if(!gsJSON.exists() && !rgJSON.exists() &&  !tJSON.exists() && !wJSON.exists() && ! nJSON.exists() && ! gJSON.exists())
                {
                    return false;
                }
                else
                {
                    this.setGsp(new GoogleScholarParser());
                    this.setRgp(new ResearchGateParser());
                    this.setTp(new TwitterParser());
                    this.setWp(new WikiParser()); 
                    this.normal = new JSONArray();
                    this.infomation = "";
                    this.googlesearchresult = null;
                    try
                    {
                        String line = "";
                        if(gJSON.exists())
                        {
                            BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(gJSON.getAbsolutePath()), "UTF-8"));
                            if((line = r.readLine())!=null)
                            {
                                this.googlesearchresult = new GoogleSearch(new JSONArray(line));
                            }
                            r.close();
                        }
                        if(gsJSON.exists())
                        {
                            BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(gsJSON.getAbsolutePath()), "UTF-8")); 
                            if((line = r.readLine())!=null)
                            {
                                JSONObject gsJ = new JSONObject(line);
                                this.gsp = new GoogleScholarParser(gsJ);
                            }
                            r.close();
                        }
                        if(rgJSON.exists())
                        {
                            BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(rgJSON.getAbsolutePath()), "UTF-8")); 
                            if((line = r.readLine())!=null)
                            {
                                JSONObject rgJ = new JSONObject(line);
                                this.rgp = new ResearchGateParser(rgJ);
                            }
                            r.close();
                        }
                        if(tJSON.exists())
                        {
                            BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(tJSON.getAbsolutePath()), "UTF-8")); 
                            
                            if((line = r.readLine())!=null)
                            {
                                JSONObject tJ = new JSONObject(line);
                                this.tp = new TwitterParser(tJ);
                            }
                            r.close();
                        }
                        if(wJSON.exists())
                        {
                            BufferedReader  r = new BufferedReader(new InputStreamReader(new FileInputStream(wJSON.getAbsolutePath()), "UTF-8")); 
                            if((line = r.readLine())!=null)
                            {
                                JSONObject wJ = new JSONObject(line);
                                this.wp = new WikiParser(wJ);
                            }
                            r.close();
                        }
                        if(nJSON.exists())
                        {
                            BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(nJSON.getAbsolutePath()), "UTF-8")); 
                            if((line = r.readLine())!=null)
                            {
                                JSONArray nJ = new JSONArray(line);
                                this.normal = nJ;
                                StringBuilder info = new StringBuilder();
                                for(int i=0;i<this.normal.length();i++)
                                {
                                    info.append(normal.getJSONObject(i).getString("content"));
                                    info.append(" ");
                                }
                                this.infomation = info.toString();
                                if(this.infomation.trim().isEmpty())
                                {
                                    File googleQuote = new File(dir+File.separator+"GoogleSearch.json");
                                    r = new BufferedReader(new FileReader(googleQuote.getAbsolutePath())); 
                                    if((line = r.readLine())!=null)
                                    {
                                        JSONArray qJ = new JSONArray(line);
                                        info = new StringBuilder();
                                        for(int i=0;i<qJ.length();i++)
                                        {
                                            info.append(qJ.getJSONObject(i).getString("quote"));
                                            info.append(" ");
                                        }
                                        this.infomation = info.toString();
                                    }
                                }
                            }
                            else
                                this.infomation = "";
                            r.close();
                            
                        }
                        
                    }catch(IOException e)
                    {
                        e.printStackTrace();
                        return false;
                    }
                }
                return true;
        }
        
	
	public boolean getInfo()
	{
		StringBuilder info = new StringBuilder();
		try
		{
			boolean twitterFlag = false;
                        boolean gsFlag = false;
                        boolean rgFlag = false;
                        boolean wikiFlag = false;
                        JSONArray result;
                        if(type==1)
                        {
                            if(this.dir != null && !this.dir.isEmpty())
                            {
                                File cache = new File(this.dir+File.separator+"GoogleSearch.json");
                                BufferedReader r = new BufferedReader(new FileReader(cache.getAbsolutePath()));
                                result = new JSONArray(r.readLine());
                            }
                            else
                                return false;
                        }
                        else if(type==0)
                        {
                            if(name==null || name.isEmpty())
                                return false;
                            GoogleSearch googlesearch = new GoogleSearch(name);                           
                            googlesearch.parse();
                            this.googlesearchresult = googlesearch;
                            result = googlesearch.getGoogleSearchResult();                           
                        }
                        else
                            return false;
			if(result!=null)
                        {
                            for(int i = 0; i<result.length();i++)
                            {
                                JSONObject jo = result.getJSONObject(i);
                                    if(!twitterFlag &&  jo.getString("url").contains("twitter.com") )
                                    {                                           
                                            String originalUrl = jo.getString("url");
                                            TwitterParser ti = new TwitterParser(originalUrl);                                          
                                            if(ti.parse())
                                            {
                                                this.tp = ti;
//                                                info.append(ti.getInfo());
//                                                info.append(" ");
                                                twitterFlag = true;
                                            }

                                    }
                                    else if(!gsFlag && jo.getString("url").contains("scholar.google"))
                                    {                                            
                                            //GoogleScholarParser gsp = new GoogleScholarParser(monitorUrl+monitor.getId()+".html");
                                            GoogleScholarParser gsp = new GoogleScholarParser(jo.getString("url"));
                                            boolean isSuccess = gsp.parse();
                                            if(isSuccess)
                                            {
//                                                    info.append(gsp.getInfo());
                                                    this.gsp = gsp;
//                                                    info.append(" ");
                                                    gsFlag = true;
                                            }
                                         
                                    }
                                    else if(!rgFlag && jo.getString("url").contains("www.researchgate.net"))
                                    {
                                       
                                            ResearchGateParser rgp = new ResearchGateParser(jo.getString("url"));
                                            boolean isSuccess = rgp.parse();
                                            if(isSuccess)
                                            {
//                                                    info.append(rgp.getInfo());
                                                    this.rgp = rgp;
//                                                    info.append(" ");
                                                    rgFlag = true;
                                            }
                                           
                                    }
                                    else if(!wikiFlag && jo.getString("url").contains("en.wikipedia.org"))
                                    {
                                            WikiParser wp = new WikiParser(jo.getString("url"));
                                            boolean isSuccess = wp.parse();
                                            if(isSuccess)
                                            {
//                                                    info.append(wp.getInfo());
                                                    this.wp = wp;
//                                                    info.append(" ");
                                                    wikiFlag = true;
                                            }
                                            
                                    }
                                    else
                                    {
                                            //String readabilityApiUrl = apiUrl+"token="+token+"&url="+jo.getString("url");
                                            String readabilityApiUrl = apiUrl+jo.getString("url");
                                            URL readabilityApi = new URL(readabilityApiUrl);
                                            String readResult = getHtml.getStrbyUrl(readabilityApi,"",0);
                                            if(readResult.length()>3)
                                            {
                                                    //JSONObject temp = new JSONObject(json);
                                                    Document readDoc = Jsoup.parse(readResult);
                                                    JSONObject readJSON = readParser(readDoc);
                                                    info.append(readJSON.getString("content"));
                                                    info.append(" ");
                                                    JSONObject normalJSON = new JSONObject();
                                                    normalJSON.put("index", jo.getInt("index"));
                                                    normalJSON.put("content", readJSON.getString("content"));
                                                    normalJSON.put("title", readJSON.getString("title"));
                                                    normal.put(normalJSON);
                                            }
                                            else
                                                    continue;
                                           
                                    }
                            }
			}
                        else
                            return false;
			
		} catch (MalformedURLException e) {
		
			e.printStackTrace();
                        return false;
		}catch(IOException e)
                {
                    e.printStackTrace();
                    return false;
                }
		
		this.infomation = info.toString();
		
		return true;
	}
        
        private JSONObject readParser(Document readDoc)
        {
            JSONObject readJSON = new JSONObject();
            String result = readDoc.text();
            if(result.contains("Looks like we couldn't find the content."))
            {
                readJSON.put("title", "");
                readJSON.put("content", "");
            }
            else
            {
                int titleStart = result.indexOf("== Title =====================================")+46;
                int titleEnd = result.indexOf("== Body ======================================");
                int bodyStart = titleEnd+46;
                int bodyEnd = result.indexOf("==Explode Output========================================");
                if(bodyEnd==-1)
                    bodyEnd = result.length();
                String title = result.substring(titleStart, titleEnd);
                String content = result.substring(bodyStart, bodyEnd);
                readJSON.put("title", title);
                readJSON.put("content", content);
            }
            
            
            return readJSON;
        }
        
        public boolean cache()
        {
            if(this.dir==null || this.dir.isEmpty()  )
                return false;
            try
            {
                if(this.type==0 && !this.googlesearchresult.cache(this.dir))
                    return false;
                File gsJSON = new File(this.dir+File.separator+"GoogleScholar.json");
                File rgJSON = new File(this.dir+File.separator+"ResearchGate.json");
                File tJSON = new File(this.dir+File.separator+"Twitter.json");
                File wJSON = new File(this.dir+File.separator+"Wiki.json");
                File nJSON = new File(this.dir+File.separator+"Normal.json");
                if(!gsJSON.exists())
                    gsJSON.createNewFile();
                if(!rgJSON.exists())
                    rgJSON.createNewFile();
                if(!tJSON.exists())
                    tJSON.createNewFile();
                if(!wJSON.exists())
                    wJSON.createNewFile();
                if(!nJSON.exists())
                    nJSON.createNewFile();
                 BufferedWriter w = null;
                if(this.gsp!=null)
                {
                    w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(gsJSON.getAbsolutePath()),"UTF-8"));
                    w.write(gsp.getJson());
                    w.close();
                }
                if(this.rgp!=null)
                {
                    w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(rgJSON.getAbsolutePath()),"UTF-8"));
                    w.write(rgp.getJson());
                    w.close();
                }
                if(this.tp!=null)
                {
                    w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tJSON.getAbsolutePath()),"UTF-8"));
                    w.write(tp.getJson());
                    w.close();
                }
                if(this.wp!=null)
                {
                    w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(wJSON.getAbsolutePath()),"UTF-8"));
                    w.write(wp.getJson());
                    w.close();
                }
                if(this.normal!=null)
                {
                    w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(nJSON.getAbsolutePath()),"UTF-8"));
                    w.write(this.normal.toString());
                    w.close();
                }
                
            }catch(IOException e)
            {
                 e.printStackTrace();
                return false;
            }
            
            return true;
        }
	 
	


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



}
