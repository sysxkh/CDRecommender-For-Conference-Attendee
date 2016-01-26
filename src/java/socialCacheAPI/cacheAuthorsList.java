/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialCacheAPI;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import social.getHtml;

/**
 *
 * @author Kehao Xu
 */
public class cacheAuthorsList extends HttpServlet {

 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        String cid = request.getParameter("cid");
        String OS = System.getProperty("os.name").toLowerCase();
        String path = "";
        if(OS.indexOf("linux")>=0)
            path = "/home/kex3";
        if(OS.indexOf("windows")>=0)
            path = "C:\\study";
        String cookie = "";
        if(request.getParameter("cookie")==null)
            cookie = "__utma=135525616.407634302.1411264048.1428708672.1440528638.4; __utmz=135525616.1440528638.4.2.utmcsr=ischool.pitt.edu|utmccn=(referral)|utmcmd=referral|utmcct=/sisint/courses/16_1/2161.html; __uvt=; _ga=GA1.2.124741748.1409445750; __utmt=1; uvts=3UMw3DqzXEVoBUtk; PHPSESSID=n0ugf83nv3i79d38oadi0p9585; __utma=174642747.124741748.1409445750.1441078903.1441830211.7; __utmb=174642747.2.10.1441830211; __utmc=174642747; __utmz=174642747.1440787168.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none)";
        else
            cookie = request.getParameter("cookie");
        File dir =new File(path+File.separator+"socialCache"+File.separator+cid);
        if(!dir.exists())
            dir.mkdir();
        String listUrl = "http://halley.exp.sis.pitt.edu/cn3/getAuthor.php?type=ALL&conferenceID="+cid;
        URL url=null;
        url = new URL(listUrl);
        Document authorsList = Jsoup.parse(getHtml.getStrbyUrl(url, cookie,0));
        Elements trs = authorsList.select("#table-basicinfo").select("tr");
        for(int i=1;i<trs.size();i++)
        {
            Elements tds = trs.get(i).select("td");
            String name = tds.get(1).text();
            String affiliation = tds.get(4).text();
            File cache = new File(dir.getAbsolutePath()+File.separator+name+"@"+affiliation);
            if(!cache.exists())
                cache.mkdir();
        }
        response.getWriter().print(dir);
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
