/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import social.SocialInfo;

/**
 *
 * @author Kehao Xu
 */
public class getDashBoard extends HttpServlet {




    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
        //request.setCharacterEncoding("utf-8");
 //       System.out.println( request.getCharacterEncoding());
        response.setContentType("text/html; charset=UTF-8");
        String name=request.getParameter("name");  
        
//        System.out.println(name);
 //       name = URLDecoder.decode(name, "UTF-8");
      
//        System.out.println(name);
        String OS = System.getProperty("os.name").toLowerCase();
        String path = "";
        if(OS.indexOf("linux")>=0)
            path = "/home/kex3";
        if(OS.indexOf("windows")>=0)
            path = "C:\\study";
        
        
        String cid = request.getParameter("cid");
        File dir = null;
        if(name.contains("@"))
            dir =new File(path+File.separator+"socialCache"+File.separator+cid+File.separator+name);
        else
        {
            File conference = new File(path+File.separator+"socialCache"+File.separator+cid);
            String[] list = conference.list();
            for(int i=0;i<list.length;i++)
            {
                if(list[i].contains(name))
                    dir=new File(conference.getAbsolutePath()+File.separator+list[i]);
                else
                    continue;
            }
        }
        if(dir==null)
            dir = new File(path+File.separator+"socialCache"+File.separator+cid+File.separator+"nosuchfile");
        String result = "";
        if(dir.exists())
        {
          System.out.println(name+"   "+cid);         
          SocialInfo si = new SocialInfo();
          si.getInfo(dir.toString());
          result = getHTML(si);
        }
        if(result.trim().isEmpty())
            result = "No Data !";
        else
        {
            String title ="<h2>The Profile for "+ name +"</h2>" ;
            title = title + "<p style=\"text-transform: none\"> This Profile contains what we get from Google Search.<br> The information from <b>Google Scholar</b>, <b>Research Gate</b>, <b>Twitter</b> and <b>Wiki</b> is independently process and dispaly.</p>";
            title = title + "<a href=\"Recommendation.html?cid="+cid+"&name="+name+"\" target=\"_blank\">Get the Recommendation for "+ name +" </a>";
            result = title+result;
        }
        response.getWriter().print(result);
        
       
    }
    
    private String getHTML(SocialInfo si)
    {
        StringBuilder sb = new StringBuilder();
        
        if(si.getGsp()!=null)
        {
            JSONObject jo = new JSONObject(si.getGsp().getJson());           
            sb.append("<h2 style=\"background-color: rgba(1, 1, 1, 1); margin-top:75px;\"> Google Scholar </h2>");
            if(jo.toString().equals("{}"))
                sb.append("<b>Sorry we do not get your Google Scholar information from Google search Result.</b>");
            else
            {
                if(jo.has("name"))
                {
                    sb.append("<h3>");
                    sb.append(jo.getString("name"));
                    sb.append("</h3>");
                }
                if(jo.has("position"))
                {
                    sb.append("<h3> Position: ");
                    sb.append(jo.getString("position"));
                    sb.append("</h3>");
                }
                if(jo.has("citations") && jo.has("h-index") && jo.has("i10-index") )
                {
                    sb.append("<h3> Citation Info : </h3>");
                    sb.append("<ul class=\"features\">");
                    sb.append("<li> Citations: ");
                    sb.append(jo.getString("citations"));
                    sb.append("</li>");
                    sb.append("<li> h-index: ");
                    sb.append(jo.getString("h-index"));
                    sb.append("</li>");
                    sb.append("<li> i10-index: ");
                    sb.append(jo.getString("i10-index"));
                    sb.append("</li>");
                    sb.append("</ul>");
                }
              
                if(jo.has("interests"))
                {
                    sb.append("<h3> Interests: </h3>");
                    JSONArray ja =jo.getJSONArray("interests");
                    sb.append("<ul class=\"features\">");

                    for(int i=0;i<ja.length();i++)
                    {              
                        sb.append("<li>");
                        sb.append(ja.get(i));
                        sb.append("</li>");
                    }
                    sb.append("</ul>");
                }
                if(jo.has("coauthors"))
                {
                    sb.append("<h3> Coauthors : </h3>");
                    JSONArray ja =jo.getJSONArray("coauthors");
                    sb.append("<ul class=\"features\">");
                    for(int i=0;i<ja.length();i++)
                    {                   
                        sb.append("<li>");
                        sb.append(ja.get(i));
                        sb.append("</li>");
                    }
                    sb.append("</ul>");
                }
                if(jo.has("titles"))
                {
                    sb.append("<h3> Papers title : </h3>");
                    JSONArray ja =jo.getJSONArray("titles");
                    sb.append("<ul class=\"features\">");
                    for(int i=0;i<ja.length();i++)
                    {          
                        sb.append("<li >");
                        sb.append(ja.get(i));
                        sb.append("</li>");
                    }
                    sb.append("</ul>");
                }
            }
        }
        if(si.getRgp()!=null)
        {
            JSONObject jo = new JSONObject(si.getRgp().getJson());
            sb.append("<h2 style=\"background-color: rgba(1, 1, 1, 1); margin-top:75px;\"> Research Gate </h2>");
            if(jo.toString().equals("{}"))
                sb.append("<b>Sorry we do not get your ResearchGate information from Google search Result.</b>");
            else
            {
                if(jo.has("institution"))
                {
                    sb.append("<h3>");
                    sb.append(jo.getString("institution"));
                    sb.append("</h3>");
                }
                if(jo.has("skills"))
                {
                    sb.append("<h3> Skills : </h3>");
                    JSONArray ja =jo.getJSONArray("skills");
                    sb.append("<ul class=\"features\">");
                    for(int i=0;i<ja.length();i++)
                    {          
                        sb.append("<li >");
                        sb.append(ja.get(i));
                        sb.append("</li>");
                    }
                    sb.append("</ul>");
                }
                if(jo.has("topics"))
                {
                    sb.append("<h3> Topics : </h3>");
                    JSONArray ja =jo.getJSONArray("topics");
                    sb.append("<ul class=\"features\">");
                    for(int i=0;i<ja.length();i++)
                    {          
                        sb.append("<li >");
                        sb.append(ja.get(i));
                        sb.append("</li>");
                    }
                    sb.append("</ul>");
                }
                if(jo.has("publications"))
                {
                    sb.append("<h3> Publications : </h3>");
                    JSONArray ja =jo.getJSONArray("publications");
                    sb.append("<ul class=\"features\">");
                    for(int i=0;i<ja.length();i++)
                    {          
                        sb.append("<li>");
                        sb.append(ja.get(i));
                        sb.append("</li>");
                    }
                    sb.append("</ul>");
                }
                if(jo.has("topcoauthors"))
                {
                    sb.append("<h3> Top Coauthors : </h3>");
                    JSONArray ja =jo.getJSONArray("topcoauthors");
                    sb.append("<ul class=\"features\">");
                    for(int i=0;i<ja.length();i++)
                    {          
                        sb.append("<li>");
                        sb.append(ja.get(i));
                        sb.append("</li>");
                    }
                    sb.append("</ul>");
                }
            }
        }
        if(si.getTp()!=null)
        {
            JSONObject jo = new JSONObject(si.getTp().getJson());
          
            sb.append("<h2 style=\"background-color: rgba(1, 1, 1, 1); margin-top:75px;\"> Twitter </h2>");
            if(jo.toString().equals("{}"))
                sb.append("<b>Sorry we do not get your Twitter information from Google search Result.</b>");
            else
            {
                if(jo.has("timelines"))
                {
                    sb.append("<h3> Timelines : </h3>");
                    JSONArray ja =jo.getJSONArray("timelines");
                    sb.append("<ul class=\"features\">");
                    for(int i=0;i<ja.length();i++)
                    {          
                        sb.append("<li>");
                        sb.append(ja.get(i));
                        sb.append("</li>");
                    }
                    sb.append("</ul>");
                }
                if(jo.has("favorites"))
                {
                    sb.append("<h3> Favorites : </h3>");
                    JSONArray ja =jo.getJSONArray("favorites");
                    sb.append("<ul class=\"features\">");
                    for(int i=0;i<ja.length();i++)
                    {          
                        sb.append("<li >");
                        sb.append(ja.get(i));
                        sb.append("</li>");
                    }
                    sb.append("</ul>");
                }
                if(jo.has("friends"))
                {
                    sb.append("<h3> Friends : </h3>");
                    JSONArray ja =jo.getJSONArray("friends");
                    sb.append("<ul class=\"features\">");
                    for(int i=0;i<ja.length();i++)
                    {          
                        sb.append("<li>");
                        sb.append(ja.get(i));
                        sb.append("</li>");
                    }
                    sb.append("</ul>");
                }
            }
        }
        if(si.getWp()!=null)
        {
            JSONObject jo = new JSONObject(si.getWp().getJson());
            sb.append("<h2 style=\"background-color: rgba(1, 1, 1, 1); margin-top:75px;\"> Wikipedia </h2>");
            if(jo.toString().equals("{}"))
                sb.append("<b>Sorry we do not get your Wikipedia information from Google search Result.</b>");
            else
            {   
                JSONArray ja = si.getGooglesearchresult().getGoogleSearchResult();
                String wikiurl="";
                for(int i=0;i<ja.length();i++)
                {
                    if(ja.getJSONObject(i).getString("url").contains("wikipedia"))
                    {
                        wikiurl = ja.getJSONObject(i).getString("url");
                        break;
                    }
                }
                sb.append("<iframe src=\"");
                sb.append(wikiurl);
                sb.append("\" style=\"width:80%; height:900px\"></iframe>");
//                Iterator<String> keys =  jo.keys();
//                while(keys.hasNext())
//                {
//                    String key = keys.next();
//                    sb.append("<p style=\"text-transform: none\"><b>");
//                    sb.append(key);
//                    sb.append("</b>: ");
//                    sb.append(jo.getString(key));
//                    sb.append("</p><br>");
//                }
            }
        }
            
        if(si.getGooglesearchresult()!=null)
        {
            boolean hasContent = false;
            StringBuilder normal = new StringBuilder();
            JSONArray ja = si.getGooglesearchresult().getGoogleSearchResult();
            normal.append("<h2 style=\"background-color: rgba(1, 1, 1, 1); margin-top:75px;\">General Google Search Result</h2>");
            normal.append("<ul class=\"features\">");
            for(int i=0;i<ja.length();i++)
            {
                JSONObject jo = ja.getJSONObject(i);
                String url = jo.getString("url");
                if(url.contains("scholar.google") || url.contains("twitter.com") || url.contains("wikipedia") || url.contains("researchgate.net"))
                    continue;
                //normal.append("<div class=\"googlesearch\">");
                normal.append("<li>");
                normal.append("<a href=\"");
                normal.append(url);
                normal.append("\" target=\"_blank\"> ");
                normal.append(jo.getString("title"));
                normal.append("</a");
                normal.append("</li>");
                
//                normal.append("<h3>");
//                normal.append(jo.getString("content"));
//                normal.append("</h3>");
               // normal.append("</div>");
                if(!jo.getString("title").isEmpty())
                    hasContent = true;
            }
            normal.append("</ul>");
            if(hasContent)
                sb.append(normal.toString());
        }
        return sb.toString();
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
