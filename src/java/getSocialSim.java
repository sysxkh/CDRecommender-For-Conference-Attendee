/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import application.JaccardCoefficient;
import application.Similarity;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import social.SocialInfo;
import textHandler.StopwordsRemover;

/**
 *
 * @author Kehao Xu
 */
public class getSocialSim extends HttpServlet {



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
        String name1=request.getParameter("name1");
        String name2=request.getParameter("name2");
        String pid1=request.getParameter("pid1");
        String pid2=request.getParameter("pid2");
        String result = "";
        StopwordsRemover swr = null;
        //System.out.println(System.getProperty("user.dir"));
        String OS = System.getProperty("os.name").toLowerCase();
        String path = "";
        if(OS.indexOf("linux")>=0)
            path = "/home/kex3";
        if(OS.indexOf("windows")>=0)
            path = "C:\\study\\Java\\IndependentStudy";
        try 
        {
            swr = new StopwordsRemover((new FileInputStream(path+File.separator+"stop_words.txt")));
        } catch (FileNotFoundException e) {
                e.printStackTrace();
        } catch (IOException e) {
                e.printStackTrace();
        }
        SocialInfo si1 = new SocialInfo(name1,0,"");
        SocialInfo si2 = new SocialInfo(name2,0,"");
        if(si1.getInfo() && si2.getInfo())
             result = getHTML(si1,si2,swr);
        
        response.getWriter().print(result);
    }
    
    private String getHTML(SocialInfo si1,SocialInfo si2,StopwordsRemover swr)
    {
        StringBuilder sb = new StringBuilder();
        
        double gsCS = Similarity.calcSimilarity(si1.getGsp().getInfo(), si2.getGsp().getInfo(), swr);
        double rgCS = Similarity.calcSimilarity(si1.getRgp().getInfo(), si2.getRgp().getInfo(), swr);
        double wkCS = Similarity.calcSimilarity(si1.getWp().getInfo(), si2.getWp().getInfo(), swr);
        double ttCS = Similarity.calcSimilarity(si1.getTp().getInfo(), si2.getTp().getInfo(), swr);
        double nmCS = Similarity.calcSimilarity(si1.getInfomation(),si2.getInfomation() , swr);
        double gsJC = JaccardCoefficient.calcJaccard(si1.getGsp().getNeighbor(), si2.getGsp().getNeighbor());
        double rgJC = JaccardCoefficient.calcJaccard(si1.getRgp().getNeighbor(), si2.getRgp().getNeighbor());
        double ttJC = JaccardCoefficient.calcJaccard(si1.getTp().getNeighbor(), si2.getTp().getNeighbor());
        double totalCS = (ttCS+rgCS+gsCS+wkCS+nmCS)/5.0;
        double totalJC = (gsJC+ttJC+gsJC)/3.0;
        double total = (totalCS + totalJC) /2.0;
        sb.append("<table>");
        
        sb.append("<tr>");
        sb.append("<th>Resource</th>");
        sb.append("<th>Content Similarity</th>");
        sb.append("<th>Jaccard Coefficient</th>");
        sb.append("<th>Linear Total</th>");        
        sb.append("</tr>");
        
        sb.append("<tr>");
        sb.append("<td>Google Scholar</td>");
        sb.append("<td>");
        sb.append(String.valueOf(gsCS));
        sb.append("</td>");
        sb.append("<td>");
        sb.append(String.valueOf(gsJC));
        sb.append("</td>");
        sb.append("<td>");
        sb.append(String.valueOf((gsCS+gsJC)/2.0));
        sb.append("</td>");
        sb.append("</tr>");
        
        sb.append("<tr>");
        sb.append("<td>Research Gate</td>");
        sb.append("<td>");
        sb.append(String.valueOf(rgCS));
        sb.append("</td>");
        sb.append("<td>");
        sb.append(String.valueOf(rgJC));
        sb.append("</td>");
        sb.append("<td>");
        sb.append(String.valueOf((rgCS+rgJC)/2.0));
        sb.append("</td>");        
        sb.append("</tr>");
        
        sb.append("<tr>");
        sb.append("<td>Twitter</td>");
        sb.append("<td>");
        sb.append(String.valueOf(ttCS));
        sb.append("</td>");
        sb.append("<td>");
        sb.append(String.valueOf(ttJC));
        sb.append("</td>");
        sb.append("<td>");
        sb.append(String.valueOf((ttCS+ttJC)/2.0));
        sb.append("</td>");        
        sb.append("</tr>");
        
        sb.append("<tr>");
        sb.append("<td>Wiki</td>");
        sb.append("<td>");
        sb.append(String.valueOf(wkCS));
        sb.append("</td>");
        sb.append("<td>");
        sb.append("N/A");
        sb.append("</td>");
        sb.append("<td>");
        sb.append("N/A");
        sb.append("</td>");
        sb.append("</tr>");
        
        sb.append("<tr>");
        sb.append("<td>Other Pages</td>");
        sb.append("<td>");
        sb.append(String.valueOf(nmCS));
        sb.append("</td>");
        sb.append("<td>");
        sb.append("N/A");
        sb.append("</td>");
        sb.append("<td>");
        sb.append("N/A");
        sb.append("</td>");
        sb.append("</tr>");
        
        sb.append("<tr>");
        sb.append("<td>Linear Total</td>");
        sb.append("<td>");
        sb.append(String.valueOf( totalCS ));
        sb.append("</td>");
        sb.append("<td>");
        sb.append(String.valueOf( totalJC ));
        sb.append("</td>");
        sb.append("<td>");
        sb.append(String.valueOf( total ) );
        sb.append("</td");
        sb.append("</tr>");
        
        
        sb.append("</table>");
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
