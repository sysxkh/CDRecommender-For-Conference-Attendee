/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import social.SocialInfo;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import textHandler.StopwordsRemover;

/**
 *
 * @author Kehao Xu
 */
public class GetSocialSimTotal extends HttpServlet {


    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
               
        String name1=request.getParameter("n1").replace("%20", " ");
        String name2=request.getParameter("n2").replace("%20", " ");
        String pid = request.getParameter("pid");
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

        double result = 0.0;
        if(si1.getInfo() && si2.getInfo())
        {
            String info1 = si1.getInfomation();
            String info2 = si2.getInfomation();
            result  = application.Similarity.calcSimilarity(info1, info2, swr);
        }
        
        response.getWriter().print(result);
        
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
